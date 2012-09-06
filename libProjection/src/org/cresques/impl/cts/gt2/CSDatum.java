/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-6.
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
 * cresques@gmail.com
 */
package org.cresques.impl.cts.gt2;

import java.util.Locale;

import org.cresques.cts.IDatum;
import org.geotools.cs.CoordinateSystemFactory;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.HorizontalDatum;
import org.opengis.referencing.FactoryException;


/**
 * Datum (y Ellipsoid) de GeoTools2.
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class CSDatum implements IDatum {
    private static String line1 = "DATUM[\"WGS_1984\"," +
                                  "SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]]," +
                                  "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6326\"]]";
    private static String line2 = "DATUM[\"European_Datum_1950\"," +
                                  "SPHEROID[\"International 1924\",6378388,297,AUTHORITY[\"EPSG\",\"7022\"]]," +
                                  "TOWGS84[-84,-107,-120,0,0,0,0],AUTHORITY[\"EPSG\",\"6230\"]]";
    private static String line3 = "DATUM[\"Nouvelle_Triangulation_Francaise\"," +
                                  "SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.466021293627, AUTHORITY[\"EPSG\",\"7011\"]]," +
                                  "TOWGS84[-168,-60,320,0,0,0,0],AUTHORITY[\"EPSG\",\"6275\"]]";
    private static String line4 = "DATUM[\"Datum 73\"," +
                                  "SPHEROID[\"International 1924\",6378388,297,AUTHORITY[\"EPSG\",\"7022\"]]," +
                                  "TOWGS84[-87,-98,-121,0,0,0,0],AUTHORITY[\"EPSG\",\"4274\"]]";
    private static String line5 = "DATUM[\"North_American_Datum_1927\"," +
                                  "SPHEROID[\"Clarke 1866\",6378206.4,294.978698213901,AUTHORITY[\"EPSG\",\"7008\"]]," +
                                  "TOWGS84[-3,142,183,0,0,0,0],AUTHORITY[\"EPSG\",\"6267\"]]";
    private static String line6 = "DATUM[\"North_American_Datum_1983\"," +
                                  "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]]," +
                                  "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6269\"]]";

    /*
     * INSERT INTO epsg_coordinatereferencesystem VALUES (
     * 4288, 'Loma Quintana', 1313, 'geographic 2D', 6422, 6288,
     * Null, Null, Null, Null, 'Geodetic survey.',
     * 'Superseded by La Canoa (code 4247).', '',
     * 'EPSG', '2004/01/06', '2003.37', 1, 0 );
     *
     *  DX (m) = -270.933
            DY (m) =  115.599
            DZ (m) = -360.226

            EX (") = -5.266
            EY (") = -1.238
              EZ (")  =  2.381
            FE (ppm) = -5.109
     */
    private static String line7 = "DATUM[\"Loma Quintana\"," +
                                  "SPHEROID[\"International 1924\",6378388,297,AUTHORITY[\"EPSG\",\"7022\"]]," +
                                  "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6288\"]]";

    /*
    # La Canoa
    <4247> +proj=longlat +ellps=intl +towgs84=-273.5,110.6,-357.9,0,0,0,0
    no_defs <>
    # PSAD56
    <4248> +proj=longlat +ellps=intl +towgs84=-288,175,-376,0,0,0,0 no_defs <>
     */
    private static String line8 = "DATUM[\"La Canoa\"," +
                                  "SPHEROID[\"International 1924\",6378388,297,AUTHORITY[\"EPSG\",\"7022\"]]," +
                                  "TOWGS84[-270.933,115.599,-360.226,-5.266,-1.238,2.381,-5.109],AUTHORITY[\"EPSG\",\"6288\"]]";
    private static String line9 = "GEOGCS[\"NTF (Paris)\","+
    	"DATUM[\"Nouvelle_Triangulation_Francaise_Paris\"," +
    	"SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936269,AUTHORITY[\"EPSG\",\"7011\"]]," +
    	"TOWGS84[-168,-60,320,0,0,0,0],AUTHORITY[\"EPSG\",\"8903\"]]"+
        ",PRIMEM[\"Paris\",2.33722917,AUTHORITY[\"EPSG\",\"8903\"]]," +
        "UNIT[\"grad\",0.01570796326794897,AUTHORITY[\"EPSG\",\"9105\"]]," +
        "AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST]," +
        "AUTHORITY[\"EPSG\",\"4807\"]]";
    
    private static String line10 = "GEOGCS[\"RGF93\"," +
		"DATUM[\"Reseau Geodesique Francais 1993\"," +
	    "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]]," +
	    "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6171\"]],"+
        "PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]]," +
        "UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]]," +
        "AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST]," +
        "AUTHORITY[\"EPSG\",\"4171\"]]";
    private static String line11 = "GEOGCS[\"ETRS89\","+
        "DATUM[\"European_Terrestrial_Reference_System_1989\","+
            "SPHEROID[\"GRS 1980\",6378137,298.257222101,"+
                "AUTHORITY[\"EPSG\",\"7019\"]],"+
            "AUTHORITY[\"EPSG\",\"6258\"]],"+
        "PRIMEM[\"Greenwich\",0,"+
            "AUTHORITY[\"EPSG\",\"8901\"]],"+
        "UNIT[\"degree\",0.01745329251994328,"+
            "AUTHORITY[\"EPSG\",\"9122\"]],"+
        "AUTHORITY[\"EPSG\",\"4258\"]]";
    //," +
    //"TOWGS84[0,0,0,0,0,0,0]
    private static String line12 =
    	"GEOGCS[\"Mars 2000\","+
	    	"DATUM[\"D_Mars_2000\","+
	    		"SPHEROID[\"Mars_2000_IAU_IAG\",3396190.0, 169.89444722361179],"+
	    	"TOWGS84[0,0,0,0,0,0,0]],"+
		    "PRIMEM[\"Greenwich\",0],"+
	    	"UNIT[\"Decimal_Degree\",0.0174532925199433]]";

    public final static CSDatum wgs84 = new CSDatum(line1);
    public final static CSDatum ed50 = new CSDatum(line2);
    public final static CSDatum ntf = new CSDatum(line3);
    public final static CSDatum d73 = new CSDatum(line4);
    public final static CSDatum nad27 = new CSDatum(line5);
    public final static CSDatum nad83 = new CSDatum(line6);
    public final static CSDatum lomaQuintana = new CSDatum(line7);
    public final static CSDatum laCanoa = new CSDatum(line8);
    public static CSDatum etrs89 = null;
    public static CSDatum ntfParis = null;
    public static CSDatum posgar = null;
    public static CSDatum rgf93 = null;
    public static CSDatum mars = null;
    public static CSDatum moon = null;
    private String sGeo1 = "GEOGCS[\"WGS 84\",";
    private String sGeo2 =
        ",PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]]," +
        "UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]]," +
        "AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST]," +
        "AUTHORITY[\"EPSG\",\"4326\"]]";
    private HorizontalDatum datum = null;
    static {
    	try {
			ntfParis =  new CSDatum().fromWKT(line9);
			rgf93 =  new CSDatum().fromWKT(line10);
			etrs89 = new CSDatum().fromWKT(line11);
			posgar = new CSDatum().fromWKT(
				"GEOGCS[\"POSGAR\","+
				"DATUM[\"POSGAR\"," +
                "SPHEROID[\"GRS 1980\",6378137,298.257222101,AUTHORITY[\"EPSG\",\"7019\"]]," +
                "TOWGS84[0,0,0,0,0,0,0],AUTHORITY[\"EPSG\",\"6269\"]]"+
                ",PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]]," +
                "UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]]," +
                "AXIS[\"Lat\",NORTH],AXIS[\"Long\",EAST]," +
                "AUTHORITY[\"EPSG\",\"4172\"]]");
			mars = new CSDatum().fromWKT(line12);
			moon = new CSDatum().fromWKT(
				"GEOGCS[\"Moon 2000\"," +
				"DATUM[\"D_Moon_2000\"," +
				"SPHEROID[\"Moon_2000_IAU_IAG\",1737400.0, 0.0]," +
		    	"TOWGS84[0,0,0,0,0,0,0]],"+
				"PRIMEM[\"Greenwich\",0]," +
				"UNIT[\"Decimal_Degree\",0.0174532925199433]]");
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public CSDatum() {
    }

    public CSDatum(HorizontalDatum datum) {
    	this.datum = datum;
    }

    public CSDatum(String sDatum) {
        try {
            fromWKT(sGeo1 + sDatum + sGeo2);
        } catch (FactoryException e) {
            // TODO Bloque catch generado automáticamente
            e.printStackTrace();
        }
    }
    
    public CSDatum fromWKT(String s) throws FactoryException {
        datum = ((GeographicCoordinateSystem) CoordinateSystemFactory.getDefault()
                .createFromWKT(s)).getHorizontalDatum();
        return this;
    }

    public String getName(Locale loc) {
        return datum.getName().toString();
    }

    HorizontalDatum getDatum() {
        return datum;
    }

    public double getESemiMajorAxis() {
        return datum.getEllipsoid().getSemiMajorAxis();
    }

    public double getEIFlattening() {
        return datum.getEllipsoid().getInverseFlattening();
    }

    public String toString() {
        return datum.toString();
    }
}
