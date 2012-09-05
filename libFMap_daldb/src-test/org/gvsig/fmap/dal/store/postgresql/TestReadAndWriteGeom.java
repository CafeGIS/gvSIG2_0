/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA  02110-1301, USA.
*
*/

/*
* AUTHORS (In addition to CIT):
* 2009 IVER T.I   {{Task}}
*/

package org.gvsig.fmap.dal.store.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import junit.framework.TestCase;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKB;
import org.gvsig.fmap.geom.operation.fromwkb.FromWKBGeometryOperationContext;
import org.gvsig.fmap.geom.operation.impl.DefaultGeometryOperationLibrary;
import org.gvsig.fmap.geom.operation.towkb.ToWKB;
import org.gvsig.fmap.geom.operation.towkt.ToWKT;
import org.gvsig.fmap.geom.primitive.impl.Point2D;
import org.gvsig.tools.ToolsLibrary;

public class TestReadAndWriteGeom extends TestCase {
	private static final int FEATURES_TO_INSERT = 12000;
	private static final String TABLE_NAME_INSERT = "testReadadnwritegeom_testcase";
	private static final String FIELD_NAME_INSERT = "geom";
	private Connection conn;
	private GeometryManager geoManager;
	private static String SQL_FOR_READ_TEST = "Select {0} from medio_ejes";

	public TestReadAndWriteGeom() throws Exception {
		ToolsLibrary toolsLib = new ToolsLibrary();
		GeometryLibrary geoLib = new GeometryLibrary();
		DefaultGeometryLibrary impGeoLib = new DefaultGeometryLibrary();
		DefaultGeometryOperationLibrary opGeoLib = new DefaultGeometryOperationLibrary();

		toolsLib.initialize();
		geoLib.initialize();
		impGeoLib.initialize();
		opGeoLib.initialize();

		toolsLib.postInitialize();
		geoLib.postInitialize();
		impGeoLib.postInitialize();
		opGeoLib.postInitialize();

		geoManager = GeometryLocator.getGeometryManager();

		Class klass = Class.forName(PostgreSQLLibrary.DEFAULT_JDCB_DRIVER_NAME);
		if (klass == null) {
			throw new Exception("Driver not found: "
					+ PostgreSQLLibrary.DEFAULT_JDCB_DRIVER_NAME);
		}

		// inicializamos operaciones de geometrias de las que dependemos
		int code = FromWKB.CODE;
		code = ToWKB.CODE;
		code = ToWKT.CODE;

	}

	public void testInsertWKB() throws Exception {
		PreparedStatement pst = null;
		try {
			crearTablaTest();
			Point2D geom = (Point2D) geoManager.createPoint(0, 0,
					Geometry.SUBTYPES.GEOM2D);
			pst = conn.prepareStatement(
					"Insert into "
					+ TABLE_NAME_INSERT.toLowerCase() +
					"  ("
					+ FIELD_NAME_INSERT + ") Values (GeomFromWKB(?))");


			int i;
			for (i = 1; i <= FEATURES_TO_INSERT; i++) {
				pst.clearParameters();
				geom.setX(i);
				geom.setY(i);
				pst.setBytes(1,
					(byte[]) geom.invokeOperation(
							ToWKB.CODE,
							null)
					);

				pst.executeUpdate();
			}

			System.out
					.println("TestReadAndWriteGeom.testInsertPostgis() Inserteds= i");




		} finally {
			if (pst != null) {
				try {pst.close();} catch (SQLException e) {e.printStackTrace();}
			}

		}


	}


	protected void setUp() throws Exception {
		super.setUp();

		conn = DriverManager.getConnection(PostgreSQLLibrary.getJdbcUrl(
				"localhost", 5432, "gis"), "postgres", "postgres");
	}

	private void crearTablaTest() throws SQLException {
		execute("DROP TABLE IF EXISTS " + TABLE_NAME_INSERT.toLowerCase());
		execute("Delete from geometry_columns  where  f_table_name = '"
				+ TABLE_NAME_INSERT.toLowerCase() + "'");
		execute("CREATE TABLE " + TABLE_NAME_INSERT.toLowerCase()
				+ " (id serial PRIMARY KEY)");
		execute("Select AddGeometryColumn('" + TABLE_NAME_INSERT.toLowerCase()
				+ "','"
				+ FIELD_NAME_INSERT + "',-1,'GEOMETRY',2)");
	}

	private void execute(String sql) throws SQLException {
		Statement st = null;
		try {
			st = conn.createStatement();
			st.execute(sql);
		} finally {
			if (st != null) {
				try {st.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}

	}

	public String getSQLForRead(String geoColumn) {
		return MessageFormat.format(SQL_FOR_READ_TEST, geoColumn);

	}

	public void testReadBinary() throws Exception {
		// st.execute("declare " + getTableName() + myCursorId +
		// "_wkb_cursor binary scroll cursor with hold for " + sqlAux);
		// rs = st.executeQuery("fetch forward " + FETCH_SIZE+ " in " +
		// getTableName() + myCursorId + "_wkb_cursor");

		Statement st = null;
		ResultSet rs = null;
		long count = 0;
		Geometry geom;
		Geometry nullGeom = geoManager.createNullGeometry(Geometry.SUBTYPES.GEOM2D);

		try {
			st = conn.createStatement();
			String cursorName = "myCursor___xxx";
			st.execute("declare " + cursorName
					+ "_wkb_cursor binary scroll cursor with hold for "
					+ getSQLForRead("the_geom"));
			rs = st.executeQuery("fetch forward all in " + cursorName
					+ "_wkb_cursor");
			byte[] buff;
			FromWKBGeometryOperationContext opContext = new FromWKBGeometryOperationContext();
			while (rs.next()) {
				count++;
				buff = rs.getBytes(1);
//				if (buff != null) {
//					opContext.setData(buff);
//					geom = (Geometry) nullGeom.invokeOperation(FromWKB.CODE,
//							opContext);
//					assertNotNull(geom);
//				}
			}

			System.out
					.println("TestReadAndWriteGeom.testReadPostgis() Count = "
							+ count);

		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void testReadWKB() throws Exception {
		Statement st = null;
		ResultSet rs = null;
		long count = 0;
		Geometry geom;
		Geometry nullGeom = geoManager
				.createNullGeometry(Geometry.SUBTYPES.GEOM2D);
		byte[] buff;
		FromWKBGeometryOperationContext opContext = new FromWKBGeometryOperationContext();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(getSQLForRead("asBinary(the_geom)"));
			while (rs.next()) {
				count++;
				buff = rs.getBytes(1);
//				if (buff != null) {
//					opContext.setData(buff);
//					geom = (Geometry) nullGeom.invokeOperation(FromWKB.CODE,
//							opContext);
//					assertNotNull(geom);
//				}
			}

			System.out
					.println("TestReadAndWriteGeom.testReadPostgis() Count = "
							+ count);


		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	public void testReadPostgis() throws Exception {
//		Statement st = null;
//		ResultSet rs = null;
//		long count = 0;
//		Geometry geom;
//		PGgeometry pgGeom;
//		PostGIS2Geometry converter = new PostGIS2Geometry();
//		try {
//			st = conn.createStatement();
//			rs = st.executeQuery(getSQLForRead("the_geom"));
//			while (rs.next()) {
//				count++;
//				pgGeom = (PGgeometry) rs.getObject(1);
//				if (pgGeom != null) {
//					geom = converter.getGeometry(pgGeom);
//					assertNotNull(geom);
//				}
//			}
//
//
//			System.out
//					.println("TestReadAndWriteGeom.testReadPostgis() Count = "
//							+ count);
//
//
//		} finally {
//			if (st != null) {
//				try {
//					st.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}


	protected void tearDown() throws Exception {
		super.tearDown();
		conn.close();
	}

}
