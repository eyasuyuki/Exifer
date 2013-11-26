package com.example.exifer;

import java.io.File;

import com.drew.metadata.MetadataException;

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
			if (f.isFile())      exifer.copyExif(f, destRoot, true, false);
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
