package com.example.exifer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExifMapDao {
	private Connection conn = null;
	
	public ExifMapDao() throws SQLException, ClassNotFoundException  {
		DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
		conn = DriverManager.getConnection(
				"jdbc:derby:/Users/yasuyuki/exifMapDB;create=true");
		
	}
	
	public void create() throws SQLException {
		Statement st = conn.createStatement();
		st.execute(ExifMapTable.CREATE_EXIF_MAP_TABLE_SQL);
		System.out.println(ExifMapTable.CREATE_EXIF_MAP_TABLE_SQL);
		st.close();
	}
	
	public ExifMap insert(ExifMap exifMap) throws SQLException {
		PreparedStatement st =
				conn.prepareStatement(
						ExifMapTable.INSERT_EXIF_MAP_TABLE_SQL
						,Statement.RETURN_GENERATED_KEYS);
		int i = 1;
		st.setString(i++,    exifMap.getPath());
		st.setString(i++,    exifMap.getName());
		st.setLong(i++,      exifMap.getSize());
		st.setTimestamp(i++, exifMap.getExifDate());
		st.executeUpdate();
		ResultSet rs = st.getGeneratedKeys();
		if (rs != null && rs.next()) {
			long id = rs.getInt(1);
			exifMap.setId(id);
		} else {
			return null;
		}
		
		return exifMap;
	}
	
	public int update(ExifMap exifMap) throws SQLException {
		int count = 0;
		
		PreparedStatement st =
				conn.prepareStatement(ExifMapTable.UPDATE_EXIF_MAP_TABLE_SQL);
		int i = 1;
		st.setString(i++,    exifMap.getPath());
		st.setString(i++,    exifMap.getName());
		st.setLong(i++,      exifMap.getSize());
		st.setTimestamp(i++, exifMap.getExifDate());
		st.setLong(i++,      exifMap.getId());
		
		count = st.executeUpdate();

		return count;
	}
	
	public List<ExifMap> findAll() throws SQLException {
		List<ExifMap> result = new ArrayList<ExifMap>();

		PreparedStatement st =
				conn.prepareStatement(ExifMapTable.SELECT_ALL_EXIF_MAP_TABLE_SQL);
		ResultSet rs = st.executeQuery();
		if (rs != null) {
			while (rs.next()) {
				ExifMap e = new ExifMap();
				int i = 1;
				e.setId(rs.getLong(i++));
				e.setPath(rs.getString(i++));
				e.setName(rs.getString(i++));
				e.setSize(rs.getLong(i++));
				e.setExifDate(rs.getTimestamp(i++));
				result.add(e);
			}
		}
		
		return result;
	}
	
	public ExifMap find(long id) throws SQLException {
		ExifMap result = null;
		PreparedStatement st =
				conn.prepareStatement(ExifMapTable.SELECT_ID_EXIF_MAP_TABLE_SQL);
		st.setLong(1, id);
		ResultSet rs = st.executeQuery();
		if (rs != null) {
			while (rs.next()) {
				result = new ExifMap();
				int i = 1;
				result.setId(rs.getLong(i++));
				result.setPath(rs.getString(i++));
				result.setName(rs.getString(i++));
				result.setSize(rs.getLong(i++));
				result.setExifDate(rs.getTimestamp(i++));
				break;
			}
			return result;
		}
		
		return null;
	}
	
	public int delete(ExifMap exifMap) throws SQLException {
		PreparedStatement st =
				conn.prepareStatement(ExifMapTable.DELETE_EXIF_MAP_TABLE_SQL);
		st.setLong(1, exifMap.getId());
		return st.executeUpdate();
	}
	
	public void drop() throws SQLException {
		Statement st = conn.createStatement();
		st.execute(ExifMapTable.DROP_EXIF_MAP_TABLE_SQL);
		st.close();
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void shutdown() throws SQLException {
		Properties info = new Properties();
		info.setProperty("shutdown", Boolean.toString(true));
		DriverManager.getConnection("jdbc:derby:", info);
	}

}
