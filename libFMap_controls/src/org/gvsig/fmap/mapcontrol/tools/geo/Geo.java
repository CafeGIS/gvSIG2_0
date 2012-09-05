package org.gvsig.fmap.mapcontrol.tools.geo;

/**
 * <p>Mathematical utilities to work with geographical data:
 *  <ul>
 *  <li>Geographical constants:
 *   <ul>
 *    <li>PI / 2.</li>
 *    <li>Degrees per radian.</li>
 *    <li>Square miles per spherical degree.</li>
 *    <li>Square kilometres per spherical degree.</li>
 *    <li>Square metres per spherical degree.</li>
 *   </ul>
 *  </li>
 *  <li>Decimal degrees equivalent to <i>m</i> meters.</li>
 *  <li>The area of a spherical polygon in spherical degrees, given the latitudes and longitudes 
 *   of <i>n</i> points, according the <a href="http://en.wikipedia.org/wiki/Haversine_formula">Haversine function</a>.
 *  </ul>
 * </p> 
 *
 * @author Vicente Caballero Navarro
 */
public class Geo {
	/**
	 * <i>PI / 2</i>, having PI = 3.14159265358979323846
	 */
	public static double HalfPi = 1.5707963267948966192313;

    /**
     * Degrees per radian.
     */
	public static double Degree = 57.295779513082320876798; /* degrees per radian */

    /**
     *  Square miles per spherical degree.
     */
	public static double SqMi = 273218.4; /* Square mi per spherical degree. */

    /**
     * Square kilometres per spherical degree.
     */
	public static double SqKm = 707632.4; /* Square km per spherical degree. */

    /**
     * Square metres per spherical degree.
     */
	public static double SqM = 707632400000.0; /* Square M per spherical degree. */


	/**
	 * <p>Gets the decimal degrees equivalent to the <i>m</i> meters.</p>
	 * <p>Uses this formula:</br>
	 * <b><i>m * R * PI</i></b>, having R = Radius of the Earth at the equator
	 * </p>
	 * 
	 * @param m distance value in meters
	 * 
	 * @return <i>m</i> * Radius at the equator
	 */
	public static double getDecimalDegrees(double m) {
    	///(m*180)/ (6378137.0 * Math.PI)
    	return (m*8.983152841195214E-6);
    }

    /**
     * <p>Operation for calculate the <a href="http://en.wikipedia.org/wiki/Haversine_formula">Haversine function</a>:
     *  <b><i>hav(x)= (1-cos(x))/2</i></b>.</p>
     * 
     * @param X length between the difference of a coordinate of two points
     */
	private static double hav(double X){
        return (1.0 - Math.cos(X)) / 2.0;
    }

    /**
     * <p>Returns the area of a spherical polygon in spherical degrees,
     *  given the latitudes and longitudes in <i>lat</i> and <i>lon</i>, respectively.</p>
     *
     * <p>The <i>n</i> data points have indexes which range from 0 to N-1.</p>
     *
     * <p>Uses the <a href="http://en.wikipedia.org/wiki/Haversine_formula">Haversine function</a> for calculating the
     *  spherical area of the polygon.</p>
     *  
     * @param lat latitude of the vertexes <i>(must be in radians)</i>
     * @param lon longitude of the vertexes <i>(must be in radians)</i>
     * @param n number of vertexes in the polygon
     * 
     * @return the area of a spherical polygon in spherical degrees
     */
    public static double sphericalPolyArea(double[] lat, double[] lon, int n) {
        int j;
        int k;
        double lam1;
        double lam2 = 0;
        double beta1;
        double beta2 = 0;
        double cosB1;
        double cosB2 = 0;
        double havA;
        double t;
        double a;
        double b;
        double c;
        double s;
        double sum;
        double excess;

        sum = 0;

        for (j = 0; j <= n; j++) {
            k = j + 1;

            if (j == 0) {
                lam1 = lon[j];
                beta1 = lat[j];
                lam2 = lon[j + 1];
                beta2 = lat[j + 1];
                cosB1 = Math.cos(beta1);
                cosB2 = Math.cos(beta2);
            } else {
                k = (j + 1) % (n + 1);
                lam1 = lam2;
                beta1 = beta2;
                lam2 = lon[k];
                beta2 = lat[k];
                cosB1 = cosB2;
                cosB2 = Math.cos(beta2);
            }

            if (lam1 != lam2) {
                havA = hav(beta2 - beta1) + (cosB1 * cosB2 * hav(lam2 - lam1));
                a = 2 * Math.asin(Math.sqrt(havA));
                b = HalfPi - beta2;
                c = HalfPi - beta1;
                s = 0.5 * (a + b + c);
                t = Math.tan(s / 2) * Math.tan((s - a) / 2) * Math.tan((s - b) / 2) * Math.tan((s -
                        c) / 2);

                excess = Math.abs(4 * Math.atan(Math.sqrt(Math.abs(t)))) * Degree;

                if (lam2 < lam1) {
                    excess = -excess;
                }

                sum = sum + excess;
            }
        }
        return Math.abs(sum);
    } /*        SphericalPolyArea. */
}
