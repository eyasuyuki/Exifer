package com.example.exifer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

public class Exifer {
	List<ExifListener> listeners = null;
	
	public void addExifListener(ExifListener listener) {
		if (listeners == null) listeners = new ArrayList<ExifListener>();
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void removeExifListener(ExifListener listener) {
		if (listeners == null) return;
		if (!listeners.contains(listener)) return;
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	File forceMkdir(File parent, String model, Date date) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
		String path = sdf.format(date);
		File dir = new File(parent, model+path);
		if (dir.exists()) {
			if (dir.isDirectory()) return dir;
			if (dir.isFile()) dir.delete();
		}
		FileUtils.forceMkdir(dir);
		return dir;
	}
	
	void copyExif(File file, File destRoot, boolean setExifDate, boolean forceCopy) throws MetadataException {
		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(file);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		if (metadata == null) return;
		Directory dir = metadata.getDirectory(ExifIFD0Directory.class);
		if (dir == null) return;
		
		String path = file.getAbsolutePath();
		String name = file.getName();
		long size = file.length();
		java.util.Date date = dir.getDate(ExifIFD0Directory.TAG_DATETIME);
		Timestamp tx = date == null ? null : new Timestamp(date.getTime());
		String model = dir.getString(ExifIFD0Directory.TAG_MODEL);
		if (model == null || model.length() == 0) model = "Unknown";
		if (listeners != null && listeners.size() > 0) {
			synchronized (listeners) {
				for (ExifListener l: listeners) {
					l.update("path="+path+", model="+model+",name="+name+", size="+size+", date="+date);
				}
			}
		}
		if (date == null) return;
		try {
			File destPath = forceMkdir(destRoot, model, date);
			File destFile = new File(destPath, name);
			if (!forceCopy && destFile.exists() && destFile.length() >= size) return;
			FileUtils.copyFileToDirectory(file, destPath);
			if (setExifDate) file.setLastModified(date.getTime());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
