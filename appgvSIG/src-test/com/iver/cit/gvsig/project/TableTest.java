/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/* CVS MESSAGES:
*
* $Id: TableTest.java 24964 2008-11-11 17:56:07Z vcaballero $
* $Log$
* Revision 1.2  2007-07-30 12:56:04  jaume
* organize imports, java 5 code downgraded to 1.4 and added PictureFillSymbol
*
* Revision 1.1  2007/07/13 12:31:03  caballero
* TableTest
*
* Revision 1.1.2.1  2006/11/15 04:10:44  jjdelcerro
* *** empty log message ***
*
* Revision 1.1  2006/11/08 10:57:55  jaume
* remove unecessary imports
*
*
*/
package com.iver.cit.gvsig.project;

import junit.framework.TestCase;
//TODO comentado para que compile
public class TableTest extends TestCase {
	public void testTable() {
//		DriverManager dm = new DriverManager();
//		dm.setValidation(new DriverValidation() {
//				public boolean validate(Driver d) {
//					return ((d instanceof ObjectDriver) ||
//					(d instanceof FileDriver) ||
//					(d instanceof DBDriver));
//				}
//			});
//		dm.loadDrivers(new File("../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers"));
//
//		String[] Campos= {"Tipo_Via","Nombre","Numero","Sexo","CoorX","CoorY"};
//        int [] fieldTypes={12,12,12,12,12,12};
//        DBFDriver driver = new DBFDriver();
//        String name = "Tabla";
//        DataSourceFactory dsf =LayerFactory.getDataSourceFactory();
//        dsf.setDriverManager(dm);
//        dsf.createFileDataSource(driver.getName(), name, "Archivo", Campos, fieldTypes);
//        DataSource dataSource = null;
//        dsf.createFileDataSource(driver.getName(), name, "MYTABLA.dbf", Campos, fieldTypes);
//
//        try {
//            dataSource = dsf.createRandomDataSource(name, DataSourceFactory.AUTOMATIC_OPENING);
//
//            dataSource.setDataSourceFactory(dsf);
//            SelectableDataSource sds = new SelectableDataSource(dataSource);
//            EditableAdapter auxea = new EditableAdapter();
//            auxea.setOriginalDataSource(sds);
//
//
//            ProjectTable projectTables = ProjectFactory.createTable(name,
//                auxea);
//
//            ProjectExtension ext = (ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
//            ext.getProject().addDocument(projectTables);
//        } catch (DriverLoadException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NoSuchTableException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (ReadDriverException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        }

//	public void testSignature() {
//		try {
//			assertTrue(p1.computeSignature() == p2.computeSignature());
//		} catch (SaveException e) {}
////		assertTrue(p1.equals(p2));
//	}
}
