package com.example.exifer.db;

import java.sql.SQLException;

import junit.framework.TestCase;

public class ExifMapTest extends TestCase {

	ExifMapDao dao = null;
	
	@Override
	protected void setUp() throws Exception {
		dao = new ExifMapDao();
	}
	
	public void testCreate() throws SQLException {
		dao.drop();
		dao.create();
	}

	@Override
	protected void tearDown() throws Exception {
		try { dao.close(); } catch (Exception e) {}
		try {
			dao.shutdown();
			fail();
		} catch (SQLException e) {
			if ("XJ015".equals(e.getSQLState())) {
				e.printStackTrace();
			} else {
				//シャットダウン失敗
				throw e;
			}
		}
	}
	
	

}
