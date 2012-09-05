package com.iver.cit.gvsig.test;

import junit.framework.TestCase;

import com.iver.cit.gvsig.ProjectExtension;

/**
 * @author Fernando González Cortés
 */
//TODO comentado para que compile
public class Persistence extends TestCase {
	private ProjectExtension pe;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		pe = new ProjectExtension();
//		pe.inicializar();
//		LayerFactory.setDriversPath(
//		"/root/workspace/Andami/gvSIG/extensiones/com.iver.cit.gvsig/drivers");
//
//		LayerFactory.getDataSourceFactory().addFileDataSource("gdbms dbf driver", "prueba",
//				"test/cities.dbf");

	}

	public void testPersist() throws Throwable {
//		Project p = new Project();
//
//		/*
//		 * Añadimos una vista con una capa
//		 */
//		ProjectView v = new ProjectView();
//		ViewPort vp = new ViewPort( CRSFactory.getCRS("EPSG:23030"));
//		vp.setImageSize(new Dimension(500, 500));
//
//		MapContext fmap = new MapContext(vp);
//		v.setMapContext(fmap);
//		FLayer l = LayerFactory.createLayer("Vias",
//			(VectorialFileDriver) LayerFactory.getDM().getDriver("gvSIG shp driver"),
//			new File("test/cities.shp"),
//			CRSFactory.getCRS("EPSG:23030"));
//		fmap.getLayers().addLayer(l);
//
//		/*
//		 * Añadimos la tabla del tema anterior
//		 */
//		SelectableDataSource sds1 = ((FLyrVect) l).getRecordset();
//		EditableAdapter ea1 = new EditableAdapter();
//		ea1.setOriginalDataSource(sds1);
//
//		ProjectTable pt1 = ProjectFactory.createTable("tabla", ea1);
//		p.addDocument(pt1);
//
//		/*
//		 * Añadimos otra tabla
//		 */
//		SelectableDataSource sds2 = new SelectableDataSource(LayerFactory.getDataSourceFactory().createRandomDataSource("prueba", DataSourceFactory.MANUAL_OPENING));
//		EditableAdapter ea2 = new EditableAdapter();
//		ea2.setOriginalDataSource(sds2);
//		ProjectTable pt2 = ProjectFactory.createTable("tabla2", ea2);
//		p.addTable(pt2);
//
//		/*
//		 * Creamos un join
//		 */
//		String sql = "custom com_iver_cit_gvsig_arcjoin tables '"+
//		sds1.getName()+"', '"+sds1.getName()+"' values(NAME,NAME);";
//
//		SelectableDataSource result = new SelectableDataSource(
//				LayerFactory.getDataSourceFactory()
//				.executeSQL(sql, DataSourceFactory.MANUAL_OPENING));
//		EditableAdapter auxea=new EditableAdapter();
//		auxea.setOriginalDataSource(result);
//
//		pt1.replaceDataSource(auxea);
//
//		/*
//		 * Guardamos y cargamos
//		 */
//		File temp = File.createTempFile("junit-", ".gvp");
//		temp.deleteOnExit();
//		pe.writeProject(temp, p);
//
//		Project p2 = pe.readProject(temp);
//
//		/*
//		 * Comprobamos que las dos tablas son idénticas
//		 */
//		assertTrue(((ProjectTable)p2.getDocumentsByType(ProjectTableFactory.registerName).get(0)).getModel().getRecordset().getAsString().equals(((ProjectTable)p.getTables().get(0)).getModel().getRecordset().getAsString()));
//		assertTrue(((ProjectTable)p2.getDocumentsByType(ProjectTableFactory.registerName).get(1)).getModel().getRecordset().getAsString().equals(((ProjectTable)p.getTables().get(1)).getModel().getRecordset().getAsString()));
	}
}
