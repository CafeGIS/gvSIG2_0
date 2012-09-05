package org.gvsig.fmap.mapcontrol.tools;

import java.awt.geom.Point2D;

import junit.framework.TestCase;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;


public class AreaListenerTest extends TestCase{
	private IProjection projectionUTM = CRSFactory.getCRS("EPSG:23030");
	private IProjection projectionGeo = CRSFactory.getCRS("EPSG:4230");
		
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		register();
	}
		
	/**
	 * Registers the libraries
	 */
	protected void register() {
		//Initializes the geometries library
		DefaultGeometryLibrary lib = new DefaultGeometryLibrary();		
		lib.initialize();
		lib.postInitialize();			
	}


	public void test1() {
		AreaListenerImpl areaListenerUTM=new AreaListenerImpl(newMapControlUTM());
		AreaListenerImpl areaListenerGeo=new AreaListenerImpl(newMapControlGeo());
		Double[] xsUTM=new Double[] {new Double(731292),new Double(731901),new Double(730138)};
		Double[] ysUTM=new Double[] {new Double(4351223),new Double(4350768),new Double(4349232)};
		double areaUTM=areaListenerUTM.returnCoordsArea(xsUTM,ysUTM,new Point2D.Double(730138,4349232));
		Double[] xsGeo=new Double[] {new Double(-0.31888183),new Double(-0.31173131),new Double(-0.33268401)};
		Double[] ysGeo=new Double[] {new Double(39.27871741),new Double(39.27464327),new Double(39.26117368)};
		double areaGeo=areaListenerGeo.returnGeoCArea(xsGeo,ysGeo,new Point2D.Double(-0.33268401,39.26117368));
		assertTrue("Area UTM igual a Geo",areaUTM<(areaGeo+1000)&& areaUTM>(areaGeo-1000));
	}
	private MapControl newMapControlUTM() {
		ViewPort vp = new ViewPort(projectionUTM);
		MapControl mc=new MapControl();
		mc.setMapContext(new MapContext(vp));
		return mc;
	}
	private MapControl newMapControlGeo() {
		ViewPort vp = new ViewPort(projectionGeo);
		MapControl mc=new MapControl();
		mc.setMapContext(new MapContext(vp));
		return mc;
	}
}
