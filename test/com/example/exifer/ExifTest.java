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
	
	ExifListener listener = new ExifListener(){

		@Override
		public void update(String state) {
			System.out.println(state);
		}};
	
	void retrieve(File dir, File destRoot) throws MetadataException {
		File[] files = dir.listFiles();
		Exifer exifer = new Exifer();
		exifer.addExifListener(listener);

		if (files == null || files.length <= 0) return;
		for (File f: files) {
			if (f.isFile())      exifer.copyExif(f, destRoot);
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
	
	
	static void usage() {
		System.out.println("java com.example.exifer.Exifer <srcDir> <destDir>");
	}

}
