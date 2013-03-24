package com.example.exifer;

import java.io.File;
import java.sql.Date;

import javax.activation.MimetypesFileTypeMap;

import org.apache.torque.Torque;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.example.exifer.db.ExifMap;

public class ExifTest {
	
	void retrieve(File dir) {
		File[] files = dir.listFiles();
		if (files == null || files.length <= 0) return;
		for (File f: files) {
			if (f.isFile())      readExif(f);
			if (f.isDirectory()) retrieve(f);
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length <= 0) {
			usage();
			return;
		}

		File file = new File(args[0]);
		ExifTest test = new ExifTest();
//		test.processFile(file);
		try {
			Torque.init("torque.properties");
			test.retrieve(file);
			
			Torque.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void readExif(File file) {
		MimetypesFileTypeMap m = new MimetypesFileTypeMap();
		if (m == null) return;
		
//		String contentType = m.getContentType(file);
//		if (contentType == null || !contentType.equals("image/jpeg")) return;
//		System.out.println("processFile: file.getName()="+file.getName()+", contentType="+contentType);;
		
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
		System.out.println("path="+path+", name="+name+", size="+size+", date="+date);
		// TODO insert to database
		ExifMap exifmap = new ExifMap();
		exifmap.setPath(path);
		exifmap.setName(name);
		exifmap.setSize(size);
		exifmap.setExifDate(new Date(date.getTime()));
	}
	
	static void usage() {
		System.out.println("filename missing.");
	}

}
