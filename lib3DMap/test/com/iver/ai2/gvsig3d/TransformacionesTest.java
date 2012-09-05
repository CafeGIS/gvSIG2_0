//package com.iver.ai2.gvsig3d;
//
//import junit.framework.TestCase;
//
//import com.iver.ai2.gvsig3d.gui.Hud;
//import com.iver.ai2.gvsig3d.utils.UtilCoord;
//
//import es.upv.ai2.osgvp.Vec3;
//import es.upv.ai2.osgvp.planets.Planet;
//import es.upv.ai2.osgvp.viewer.ViewerFactory;
//
//public class TransformacionesTest extends TestCase {
//	public void testTrasnform() throws Throwable {
//
//		System.out.println("De grados a radianes " + UtilCoord.radianes(180));
//		System.out
//				.println("De radianes a grados " + UtilCoord.degrees(Math.PI));
//
//		Vec3 esferica = new Vec3(6378137, 0.0, 40.0);
//		Vec3 cartesianas = new Vec3(4885936.723, 0, 4099786.416);
//
//		Vec3 e = UtilCoord.GeoToCarte(esferica);
//		Vec3 c = UtilCoord.CarteToGeo(cartesianas);
//
//		System.out.println("De geocentricas a cartesianas " + e.x() + " "
//				+ e.y() + " " + e.z());
//		System.out.println("De cartesianas a geocentricas " + c.x() + " "
//				+ c.y() + " " + c.z());
//
//	}
//
//	public void testTraPla() throws Throwable {
//
////		ViewerFactory m_canvas3d = new ViewerFactory();
////
////		m_canvas3d.startAnimator();
////
////		Planet m_planet = new Planet();
////
////		m_planet.setCoordinateSystemType(Planet.CoordinateSystemType.PROJECTED);
////
////		Vec3 cartesianas = new Vec3(4886013.328, 0, 4099850.692);
////		Vec3 e = m_planet.convertXYZToLatLongHeight(cartesianas);
////
////		System.out.println("De geocentricas a cartesianas " + e.x() + " "
////				+ e.y() + " " + e.z());
//
//	}
//
//	public void testHome() throws Throwable {
//
//		// System.out.println(new File("home").getCanonicalPath());
//
//		String home = System.getProperty("user.home");
//		System.out.println(home);
//
//		String wd = System.getProperty("user.dir");
//		System.out.println(wd);
//
//	}
//
//	public void testGrados() throws Throwable {
//
////		System.out.println(Hud.getSexagesinal(74.90555556, false));
//
//	}
//
//	public void testJDialog() throws Throwable {
////
////		VectorLayerMenu vectorLayerMenu = new VectorLayerMenu(null);
////		vectorLayerMenu.setModal(true);
////		vectorLayerMenu.pack();
////		vectorLayerMenu.setVisible(true);
//	}
//
//}
