package com.example.exifer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.activation.MimetypesFileTypeMap;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

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
			test.retrieve(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void readExif(File file) {
		//System.out.println("processFile: file.getName()="+file.getName());;
		MimetypesFileTypeMap m = new MimetypesFileTypeMap();
		if (m == null) return;
		
		String contentType = m.getContentType(file);
		if (contentType == null || !contentType.equals("image/jpeg")) return;
		
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
		Date date = dir.getDate(ExifIFD0Directory.TAG_DATETIME);
		System.out.println("path="+path+", name="+name+", size="+size+", date="+date);
		// TODO insert to database
	}
	
	static void usage() {
		System.out.println("filename missing.");
	}

}
