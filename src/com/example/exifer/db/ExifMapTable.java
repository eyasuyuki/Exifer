package com.example.exifer.db;

public interface ExifMapTable {
	public static final String DATABASE_NAME = "exifmap.db";
	public static final String TABLE_NAME = "exif_map";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_PATH = "path";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SIZE = "size";
	public static final String COLUMN_EXIF_DATE = "exif_date";
	public static final String[] COLUMNS = {
		COLUMN_ID
		,COLUMN_PATH
		,COLUMN_NAME
		,COLUMN_SIZE
		,COLUMN_EXIF_DATE
	};
	
	public static final String CREATE_EXIF_MAP_TABLE_SQL =
			"CREATE TABLE " + TABLE_NAME
			+ " ( " + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY "
	        + " GENERATED ALWAYS AS IDENTITY "
	        + " (START WITH 1, INCREMENT BY 1) "
			+ " , " + COLUMN_PATH + " VARCHAR(255) "
			+ " , " + COLUMN_NAME + " VARCHAR(255) "
			+ " , " + COLUMN_SIZE + " BIGINT "
			+ " , " + COLUMN_EXIF_DATE + " TIMESTAMP "
			+ " ) ";
	
	public static final String INSERT_EXIF_MAP_TABLE_SQL =
			" INSERT INTO " + TABLE_NAME
			+ " ( " + COLUMN_PATH
			+ " , " + COLUMN_NAME
			+ " , " + COLUMN_SIZE
			+ " , " + COLUMN_EXIF_DATE
			+ " ) VALUES ( "
			+ "  ? " // path
			+ " ,? " // name
			+ " ,? " // size
			+ " ,? " // exif date
			+ " ) ";
	
	public static final String SELECT_ALL_EXIF_MAP_TABLE_SQL =
			"SELECT " + COLUMN_ID
			+ " , " + COLUMN_PATH
			+ " , " + COLUMN_NAME
			+ " , " + COLUMN_SIZE
			+ " , " + COLUMN_EXIF_DATE
			+ " FROM " + TABLE_NAME;
	
	public static final String SELECT_ID_EXIF_MAP_TABLE_SQL =
			SELECT_ALL_EXIF_MAP_TABLE_SQL
			+ " WHERE "
			+ COLUMN_ID + " = ? ";
	
	public static final String UPDATE_EXIF_MAP_TABLE_SQL =
			"UPDATE "   + TABLE_NAME
			+ "SET "    + COLUMN_PATH      + " = ? "
			+ " , "     + COLUMN_NAME      + " = ? "
			+ " , "     + COLUMN_SIZE      + " = ? "
			+ " , "     + COLUMN_EXIF_DATE + " = ? "
			+ " WHERE " + COLUMN_ID        + " = ? ";
	
	public static final String DELETE_EXIF_MAP_TABLE_SQL =
			"DELETE FROM " + TABLE_NAME
			+ " WHERE " + COLUMN_ID + " = ? ";

	
	public static final String DROP_EXIF_MAP_TABLE_SQL =
			"DROP TABLE " + TABLE_NAME;
}
