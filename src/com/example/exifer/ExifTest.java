package com.example.exifer;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

public class ExifTest {
	
	void retrieve(File dir, File destRoot) throws MetadataException {
		File[] files = dir.listFiles();
		if (files == null || files.length <= 0) return;
		for (File f: files) {
			if (f.isFile())      readExif(f, destRoot);
			if (f.isDirectory()) retrieve(f, destRoot);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			usage();
			return;
		}

		File file = new File(args[0]);
		File destRoot = new File(args[1]);
		ExifTest test = new ExifTest();
		try {
			test.retrieve(file, destRoot);
		} catch (MetadataException e) {
			e.printStackTrace();
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
	
	void readExif(File file, File destRoot) throws MetadataException {
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
		System.out.println("path="+path+", model="+model+",name="+name+", size="+size+", date="+date);
		if (date == null) return;
		try {
			File destPath = forceMkdir(destRoot, model, date);
			File destFile = new File(destPath, name);
			if (destFile.exists() && destFile.length() >= size) return;
			FileUtils.copyFileToDirectory(file, destPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void usage() {
		System.out.println("filename missing.");
	}

}
