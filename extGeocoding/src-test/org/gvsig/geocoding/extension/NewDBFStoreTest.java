
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
 * 2008 PRODEVELOP		Main development
 */

package org.gvsig.geocoding.extension;

import junit.framework.TestCase;

import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class NewDBFStoreTest extends TestCase {

	protected DataManager dataManager = null;
	private Logger log = LoggerFactory.getLogger(NewDBFStoreTest.class);

	protected void setUp() throws Exception {
		super.setUp();

		ToolsLibrary tools = new ToolsLibrary();
		tools.initialize();
		tools.postInitialize();

		DALLibrary dlib = new DALLibrary();
		dlib.initialize();
		dlib.postInitialize();

		DALFileLibrary libFile = new DALFileLibrary();
		libFile.initialize();
		libFile.postInitialize();

		GeometryLibrary lib = new GeometryLibrary();
		lib.initialize();
		lib.postInitialize();

		SHPLibrary shpLib = new SHPLibrary();
		shpLib.initialize();
		shpLib.postInitialize();

		dataManager = DALLocator.getDataManager();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testNewDBFStore() {
//		GeocoController control = new GeocoController();
//		control.createDBFTableResults();
	}

}
