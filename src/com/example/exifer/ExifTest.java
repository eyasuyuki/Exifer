package com.example.exifer;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.activation.MimetypesFileTypeMap;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.example.exifer.db.ExifMap;
import com.example.exifer.db.ExifMapDao;

public class ExifTest {
	
	void retrieve(File dir, ExifMapDao dao) {
		File[] files = dir.listFiles();
		if (files == null || files.length <= 0) return;
		for (File f: files) {
			if (f.isFile())      readExif(f, dao);
			if (f.isDirectory()) retrieve(f, dao);
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
		ExifMapDao dao = null;
		try {
			dao = new ExifMapDao();
			try { dao.drop(); } catch (Exception e) {}
			dao.create();

			File file = new File(args[0]);
			ExifTest test = new ExifTest();
			test.retrieve(file, dao);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { dao.close(); } catch (Exception e) {}
		try {
			dao.shutdown();
		} catch (SQLException e) {
			if ("XJ015".equals(e.getSQLState())) {
				e.printStackTrace();
			} else {
				//シャットダウン失敗
				//throw e;
			}
		}
		
		
	}
	
	void readExif(File file, ExifMapDao dao) {
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
		Timestamp tx = date == null ? null : new Timestamp(date.getTime());
		System.out.println("path="+path+", name="+name+", size="+size+", date="+date);
		// TODO insert to database
		ExifMap exifmap = new ExifMap();
		exifmap.setPath(path);
		exifmap.setName(name);
		exifmap.setSize(size);
		exifmap.setExifDate(tx);
		try {
			dao.insert(exifmap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void usage() {
		System.out.println("filename missing.");
	}

}
