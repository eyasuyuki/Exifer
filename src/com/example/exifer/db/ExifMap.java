package com.example.exifer.db;

import java.sql.Timestamp;

public class ExifMap {
	public static final long ID_UNKNOWN = -1;
	public ExifMap() {}
	long id = ID_UNKNOWN;
	String path = null;
	String model = null;
	String name = null;
	Timestamp exifDate = null;
	long size = 0L;
	public String getPath() {
		return path;
	}
	public void setId(long id) {
		this.id = id;
	} 
	public long getId() {
		return id;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getExifDate() {
		return exifDate;
	}
	public void setExifDate(Timestamp exifDate) {
		this.exifDate = exifDate;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
}
