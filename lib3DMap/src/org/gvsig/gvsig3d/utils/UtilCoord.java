package org.gvsig.gvsig3d.utils;

import java.awt.geom.Rectangle2D;

import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Camera;

public class UtilCoord {
	static String mensaje;

	private static double radio;

	static {
		radio = 6378137.0;
	}

	/**
	 * Method to transform geodesic coordinates to geometrical coordinates
	 * 
	 * @param r
	 *            radio
	 * @param alpha
	 *            angle in degrees
	 * @param beta
	 *            angle in degrees
	 * @return a vector with X,Y and Z values
	 */
	public static Vec3 GeoToCarte(double r, double alpha, double beta) {
		return GeoToCarte(new Vec3(r, alpha, beta));
	}

	/**
	 * Method to transform geodesic coordinates to geometrical coordinates
	 * 
	 * @param coord
	 *            vertor with radio, alpha and beta values in this order
	 * @return a vector with X,Y and Z values in this order
	 */
	public static Vec3 GeoToCarte(Vec3 coord) {
		Vec3 result = new Vec3();

		double h = coord.x();
		double alphaRad = radianes(coord.y());
		double betaRad = radianes(coord.z());

		double sinA = Math.sin(alphaRad);
		double cosA = Math.cos(alphaRad);
		double sinB = Math.sin(betaRad);
		double cosB = Math.cos(betaRad);

		// Calculate the X value
		result.setX(h * cosA * cosB);

		// Calculate the Y value
		result.setY(h * sinA * cosB);

		// Calculate the Z value
		result.setZ(h * sinB);

		return result;
	}

	/**
	 * Method to transform geometrical coordinates to geodesic coordinates
	 * 
	 * @param x
	 *            value
	 * @param y
	 *            value
	 * @param z
	 *            value
	 * @return a vector with radio, alpha and beta values in this order
	 */
	public static Vec3 CarteToGeo(double x, double y, double z) {
		return CarteToGeo(new Vec3(x, y, z));
	}

	/**
	 * Method to transform geometrical coordinates to geodesic coordinates
	 * 
	 * @param coord
	 *            vector with X,Y and Z values in this order
	 * @return vertor with radio, alpha and beta values in this order
	 */
	public static Vec3 CarteToGeo(Vec3 coord) {
		Vec3 result = new Vec3();

		// Caculate the radio value
		double modx = coord.x() * coord.x();
		double mody = coord.y() * coord.y();
		double modz = coord.z() * coord.z();

		result.setX(Math.sqrt(modx + mody + modz));

		// Calculate the alpha angle
		double sqrt = Math.sqrt(modx + mody);
		double alpha = Math.atan(coord.z() / sqrt);
		result.setY(degrees(alpha));

		// Calculate the beta angle
		double beta = degrees(Math.atan2(coord.y(), coord.x()));
		result.setZ(beta);

		return result;
	}

	/**
	 * Method to transform degrees to radianes
	 * 
	 * @param degrees
	 *            value
	 * @return radianes value
	 */
	public static double radianes(double degrees) {
		return ((degrees * Math.PI) / 180);
	}

	public static double degrees(double rad) {
		return ((rad * 180) / Math.PI);
	}

	public static void imprimeCamara(Camera camera) {
		Vec3 c, e, u;
		c = camera.getCenter();
		e = camera.getEye();
		u = camera.getUp();

		System.out.println("********* POSICION DE LA CAMARA *****************");
		System.out.println("CENTER : X " + c.x() + " Y " + c.y() + "Z " + c.z());
		System.out.println("EYE    : X " + e.x() + " Y " + e.y() + "Z " + e.z());
		System.out.println("UP     : X " + u.x() + " Y " + u.y() + "Z " + u.z());
		System.out.println("*************************************************");
	}
	
	public static Rectangle2D getExtendGeo(double longi, double lati, double size,double radius){

		double with = size / radius;
		double heigth = size / ( radius * Math.cos(lati));
		Rectangle2D extend = new Rectangle2D.Double(longi,lati,with,heigth);
		
		return extend;
	}

	public static Vec3 getDegreesHMS(double num) {
		// transform degrees in sexagesinal format
		int grados = (int) num;
		double resG = num - grados;
		int minutos = (int) (resG * 60);
		double minutosD = (resG * 60);
		double resM = minutosD - minutos;
		int segundos = (int) (resM * 60);
		String cadG = "";
		if (grados < 10)
			cadG = cadG + "0";
		cadG = cadG + grados;

		String cadM = "";
		if (minutos < 10)
			cadM = cadM + "0";
		cadM = cadM + minutos;

		String cadS = "";
		if (segundos < 10)
			cadS = cadS + "0";
		cadS = cadS + segundos;


		return (new Vec3(grados,minutos,segundos));
	}

}
