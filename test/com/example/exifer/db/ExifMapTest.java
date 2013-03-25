package com.example.exifer.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

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
	
	private static final String PATH = "hoge";
	private static final String NAME = "piyo";
	private static final long SIZE = 100L;
	private static final Timestamp EXIF_DATE = new Timestamp(System.currentTimeMillis());

	private ExifMap createExifMap() {
		ExifMap e = new ExifMap();
		e.setPath(PATH);
		e.setName(NAME);
		e.setSize(SIZE);
		e.setExifDate(EXIF_DATE);
		return e;
	}
	
	public void testInsert() throws SQLException {
		dao.drop();
		dao.create();
		
		ExifMap newE = dao.insert(createExifMap());
		assertNotNull(newE);
		ExifMap e2 = dao.find(newE.getId());
		assertNotNull(e2);
		assertEquals(e2.getId(), newE.getId());
		assertTrue(PATH.equals(e2.getPath()));
		assertTrue(NAME.equals(e2.getName()));
		assertEquals(SIZE, e2.getSize());
		System.out.println("EXIF_DATE="+EXIF_DATE);
		System.out.println("e2.getExifDate()="+e2.getExifDate());
		assertTrue(EXIF_DATE.equals(e2.getExifDate()));
	}

	public void testDelete() throws SQLException {
		dao.drop();
		dao.create();
		
		ExifMap newE = dao.insert(createExifMap());
		assertNotNull(newE);
		ExifMap e2 = dao.find(newE.getId());
		assertNotNull(e2);
		assertEquals(e2.getId(), newE.getId());
		assertTrue(PATH.equals(e2.getPath()));
		assertTrue(NAME.equals(e2.getName()));
		assertEquals(SIZE, e2.getSize());
		System.out.println("EXIF_DATE="+EXIF_DATE);
		System.out.println("e2.getExifDate()="+e2.getExifDate());
		assertTrue(EXIF_DATE.equals(e2.getExifDate()));
		int count = dao.delete(e2);
		assertEquals(1, count);
	}
	
	public void testFindAll() throws SQLException {
		dao.drop();
		dao.create();
		
		dao.insert(createExifMap());
		dao.insert(createExifMap());
		dao.insert(createExifMap());
		
		List<ExifMap> ex = dao.findAll();
		assertNotNull(ex);
		assertEquals(3, ex.size());
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
