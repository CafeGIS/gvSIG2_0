/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
package org.cresques.impl.cts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.cresques.cts.ICRSFactory;
import org.cresques.cts.IProjection;
import org.cresques.impl.cts.gt2.CSDatum;
import org.cresques.impl.cts.gt2.CSGaussPt;
import org.cresques.impl.cts.gt2.CSLambertCC;
import org.cresques.impl.cts.gt2.CSMercator;
import org.cresques.impl.cts.gt2.CSUTM;
import org.cresques.impl.cts.gt2.CoordSys;


/**
 * Pool de proyeccions (cs+datum) conocidas.
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>
 */
public class ProjectionPool implements ICRSFactory {
    static TreeMap data = null;

    static {
        CoordSys cs = null;
        data = new TreeMap();

        cs = (new CSUTM(CSDatum.wgs84, 30)).toGeo();
        cs.setAbrev("EPSG:4326"); // WGS84 (World Geodesic Datum)
        data.put(cs.getAbrev(), cs);
        data.put("CRS:84", cs); // CRS:84 = EPSG:4326

        cs = (new CSUTM(CSDatum.ed50, 30)).toGeo();
        cs.setAbrev("EPSG:4230"); // Datum Europeu Internacional ED50
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.d73, 30)).toGeo();
        cs.setAbrev("EPSG:4274"); // Datum 73 de Lisboa
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.nad27, 30)).toGeo();
        cs.setAbrev("EPSG:4267"); // NAD 27
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.nad83, 30)).toGeo();
        cs.setAbrev("EPSG:4269"); // NAD 83
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.lomaQuintana, 30)).toGeo();
        cs.setAbrev("EPSG:4288"); // PSAD 56 'Loma Quintana'
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.laCanoa, 30)).toGeo();
        cs.setAbrev("EPSG:4247"); // PSAD 56 'Loma Quintana'
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.ntfParis, 30)).toGeo();
        cs.setAbrev("EPSG:4807"); // NTF Paris
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.etrs89, 30)).toGeo();
        cs.setAbrev("EPSG:4258"); // ETRS 89
        data.put(cs.getAbrev(), cs);
        for (int i = 1; i <= 60; i++) {
            String huso = Integer.toString(i);

            if (i < 10) {
                huso = "0" + huso;
            }

            cs = new CSUTM(CSDatum.wgs84, i);
            cs.setAbrev("EPSG:326" + huso);
            data.put(cs.getAbrev(), cs);

            cs = new CSUTM(CSDatum.ed50, i);
            cs.setAbrev("EPSG:230" + huso);
            data.put(cs.getAbrev(), cs);
            if (i>2 && i<=23) {
                cs = new CSUTM(CSDatum.nad27, i);
                cs.setAbrev("EPSG:267" + huso);
                data.put(cs.getAbrev(), cs);

                cs = new CSUTM(CSDatum.nad83, i);
                cs.setAbrev("EPSG:269" + huso);
                data.put(cs.getAbrev(), cs);
            }
            if (i>27 && i<39) {
                cs = new CSUTM(CSDatum.etrs89, i);
                cs.setAbrev("EPSG:258" + huso);
                data.put(cs.getAbrev(), cs);
            }
        }

        cs = CSGaussPt.hgd73;
        cs.setAbrev("EPSG:27492"); // Projecção Gauss do Datum 73 de Lisboa (no EPSG found)
        data.put(cs.getAbrev(), cs);

        cs = new CSUTM(CSDatum.d73, 29);
        cs.setAbrev("EPSG:27429"); // Projecção Gauss do Datum 73 de Lisboa (no EPSG found)
        data.put(cs.getAbrev(), cs);

        for (int i = 18; i <= 21; i++) {
            String huso = Integer.toString(i);

            if (i < 10) {
                huso = "0" + huso;
            }

            cs = new CSUTM(CSDatum.laCanoa, i);
            cs.setAbrev("EPSG:247" + huso);
            data.put(cs.getAbrev(), cs);
        }

        for (int i = 16; i <= 22; i++) {
            String huso = Integer.toString(i);
            // Psad56 Ecuador
            cs = new CSUTM(CSDatum.lomaQuintana, i);
            cs.setAbrev("EPSG:288" + huso);
            data.put(cs.getAbrev(), cs);
            cs = new CSUTM(CSDatum.lomaQuintana, i, "S");
            cs.setAbrev("EPSG:288" + (i+60) );
            data.put(cs.getAbrev(), cs);
        }

        //		cs = new CSLambertCC(CSDatum.nad27, -105D, 49D, 49D, 77D, 0, 0);
        //		cs.setAbrev("LCCCan");
        //		data.put(cs.getAbrev(), cs);
        /* Para el server WMS de canadá:
         * EPSG:42101
         * EPSG:42304
         * EPSG:4269
         */
        /*
         * 42101,PROJCS["WGS 84 / LCC Canada",
         * GEOGCS["WGS 84",DATUM["WGS_1984",
         * SPHEROID["WGS_1984",6378137,298.257223563]],
         * PRIMEM["Greenwich",0],UNIT["Decimal_Degree",0.0174532925199433]],
         *
         * PROJECTION["Lambert_Conformal_Conic_2SP"],
         * PARAMETER["central_meridian",-95.0],
         * PARAMETER["latitude_of_origin",0],
         * PARAMETER["standard_parallel_1",49.0],
         * PARAMETER["standard_parallel_2",77.0],
         * PARAMETER["false_easting",0.0],
         * PARAMETER["false_northing",-8000000.0],
         * UNIT["Meter",1],AUTHORITY["EPSG","42101"]]
         */
        cs = new CSLambertCC(CSDatum.wgs84, -95, 0, 49, 77, 0, -8000000.0);
        cs.setAbrev("EPSG:42101");
        data.put(cs.getAbrev(), cs);

        /* 42304,PROJCS["NAD83 / NRCan LCC Canada",
         * GEOGCS["NAD83",DATUM["North_American_Datum_1983",
         * SPHEROID["GRS_1980",6378137,298.257222101]],
         * PRIMEM["Greenwich",0],
         * UNIT["Decimal_Degree",0.0174532925199433]],
         *
         * PROJECTION["Lambert_Conformal_Conic_2SP"],
         * PARAMETER["central_meridian",-95.0],
         * PARAMETER["latitude_of_origin",49.0],
         * PARAMETER["standard_parallel_1",49.0],
         * PARAMETER["standard_parallel_2",77.0],
         * PARAMETER["false_easting",0.0],
         * PARAMETER["false_northing",0.0],
         * UNIT["Meter",1],AUTHORITY["EPSG","42304"]]
         */
        cs = new CSLambertCC(CSDatum.nad83, -95, 49, 49, 77, 0, 0);
        cs.setAbrev("EPSG:42304");
        data.put(cs.getAbrev(), cs);

        /*
         * EPSG:26915 - NAD83 / UTM zone 15N
         * EPSG:31466 - Gauß-Krüger band 2
         * EPSG:31467 - Gauß-Krüger band 3
         * EPSG:4314  - DHDN
         */
        /*
         * 27572=PROJCS["NTF (Paris) / Lambert zone II",
             GEOGCS["NTF (Paris)",
                    DATUM["Nouvelle_Triangulation_Francaise_Paris",
                          SPHEROID["Clarke 1880 (IGN)",6378249.2,293.4660212936269,
                                   AUTHORITY["EPSG","7011"]],
                          TOWGS84[-168,-60,320,0,0,0,0],
                          AUTHORITY["EPSG","6807"]],
                    PRIMEM["Paris",2.33722917,AUTHORITY["EPSG","8903"]],
                    UNIT["grad",0.01570796326794897,AUTHORITY["EPSG","9105"]],
                    AUTHORITY["EPSG","4807"]],
             PROJECTION["Lambert_Conformal_Conic_1SP"],
                        PARAMETER["latitude_of_origin",52],
                        PARAMETER["central_meridian",0],
                        PARAMETER["scale_factor",0.99987742],
                        PARAMETER["false_easting",600000],
                        PARAMETER["false_northing",2200000],
                        UNIT["metre",1,AUTHORITY["EPSG","9001"]],
                        AUTHORITY["EPSG","27572"]]
         */

        cs = new CSLambertCC(CSDatum.ntfParis, 0, 46.79999999999995, 0.99987742, 600000, 2200000);
        cs.setAbrev("EPSG:27572");
        data.put(cs.getAbrev(), cs);
        
        cs = new CSLambertCC(CSDatum.ntfParis, 0, 52, 0.99987742, 600000, 2200000);
        cs.setAbrev("EPSG:27582");
        data.put(cs.getAbrev(), cs);
        /*
         * # RGF93
         * <4171> +proj=longlat +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +no_defs  <>
         * # RGF93 / Lambert-93
         */
        cs = (new CSUTM(CSDatum.rgf93, 30)).toGeo();
        cs.setAbrev("EPSG:4171"); // NTF Paris
        data.put(cs.getAbrev(), cs);
        /*
         *  <2154> +proj=lcc +lat_1=49 +lat_2=44 +lat_0=46.5 +lon_0=3
         *  +x_0=700000 +y_0=6600000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0
         *  +units=m +no_defs  <>
         */
        cs = new CSLambertCC(CSDatum.rgf93, 3.0, 46.5, 49.0, 44.0, 700000.0, 6600000.0);
        cs.setAbrev("EPSG:2154");
        data.put(cs.getAbrev(), cs);

        cs = new CSMercator(CSDatum.wgs84);
        cs.setAbrev("EPSG:54004");
        data.put(cs.getAbrev(), cs);
        cs.setAbrev("EPSG:9804");
        data.put(cs.getAbrev(), cs);
        
        // Lo que faltaba: ¡planetas!
        cs = (new CSUTM(CSDatum.moon, 30)).toGeo();
        cs.setAbrev("IAU2000:30100"); // Moon
        data.put(cs.getAbrev(), cs);

        cs = (new CSUTM(CSDatum.mars, 30)).toGeo();
        cs.setAbrev("IAU2000:49900"); // Mars
        data.put(cs.getAbrev(), cs);
        
        /*
         * CRSs argentinos.
         * coordenadas geograficas
			PosGAr 4172 
			PosGAr98 4190
			
			coordenadas proyectadas
			POSGAR 94/Argentina 1 22191 
			POSGAR 94/Argentina 2 22192
			POSGAR 94/Argentina 3 22193
			POSGAR 94/Argentina 4 22194
			POSGAR 94/Argentina 5 22195
			POSGAR 94/Argentina 6 22196
			POSGAR 94/Argentina 7 22197
			
			POSGAR 98/Argentina 1 22181
			POSGAR 98/Argentina 2 22182
			POSGAR 98/Argentina 3 22183
			POSGAR 98/Argentina 4 22184
			POSGAR 98/Argentina 5 22185
			POSGAR 98/Argentina 6 22186
			POSGAR 98/Argentina 7 22187
			
			4221 GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]]
			
			22191	EPSG	22191	PROJCS["Argentina_Zone_1",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",1500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-72],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-72 +k=1.000000 +x_0=1500000 +y_0=0 +ellps=intl +units=m
			22192	EPSG	22192	PROJCS["Argentina_Zone_2",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",2500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-69],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-69 +k=1.000000 +x_0=2500000 +y_0=0 +ellps=intl +units=m
			22193	EPSG	22193	PROJCS["Argentina_Zone_3",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",3500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-66],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-66 +k=1.000000 +x_0=3500000 +y_0=0 +ellps=intl +units=m
			22194	EPSG	22194	PROJCS["Argentina_Zone_4",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",4500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-63],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-63 +k=1.000000 +x_0=4500000 +y_0=0 +ellps=intl +units=m
			22195	EPSG	22195	PROJCS["Argentina_Zone_5",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",5500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-60],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-60 +k=1.000000 +x_0=5500000 +y_0=0 +ellps=intl +units=m
			22196	EPSG	22196	PROJCS["Argentina_Zone_6",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",6500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-57],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-57 +k=1.000000 +x_0=6500000 +y_0=0 +ellps=intl +units=m
			22197	EPSG	22197	PROJCS["Argentina_Zone_7",GEOGCS["GCS_Campo_Inchauspe",DATUM["D_Campo_Inchauspe",SPHEROID["International_1924",6378388,297]],PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["False_Easting",7500000],PARAMETER["False_Northing",0],PARAMETER["Central_Meridian",-54],PARAMETER["Scale_Factor",1],PARAMETER["Latitude_Of_Origin",-90],UNIT["Meter",1]]	+proj=tmerc +lat_0=-90 +lon_0=-54 +k=1.000000 +x_0=7500000 +y_0=0 +ellps=intl +units=m

         */
        cs = new CoordSys(CSDatum.posgar);
        cs.setAbrev("EPSG:4172"); // Posgar
        data.put(cs.getAbrev(), cs);
        
        cs = new CoordSys(
			"GEOGCS[\"GCS_Campo_Inchauspe\"," +
				"DATUM[\"D_Campo_Inchauspe\"," +
					"SPHEROID[\"International_1924\",6378388,297],"+
					"TOWGS84[0,0,0,0,0,0,0]]," +
				"PRIMEM[\"Greenwich\",0]," +
				"UNIT[\"Degree\",0.017453292519943295]]");
        cs.setAbrev("EPSG:4221"); // Campo Inchauspe
        data.put(cs.getAbrev(), cs);
        
       for (int i=1; i<=7; i++) {
	        cs = new CoordSys(
	        	"PROJCS[\"Argentina_Zone_"+i+"\"," +
	        		"GEOGCS[\"GCS_Campo_Inchauspe\"," +
	        			"DATUM[\"D_Campo_Inchauspe\"," +
		        			"SPHEROID[\"International_1924\",6378388,297],"+
		        			"TOWGS84[0,0,0,0,0,0,0]]," +
		        		"PRIMEM[\"Greenwich\",0]," +
		        		"UNIT[\"Degree\",0.017453292519943295]]," +
		        	"PROJECTION[\"Transverse_Mercator\"]," +
		        	"PARAMETER[\"False_Easting\",1500000]," +
		        	"PARAMETER[\"False_Northing\",0]," +
		        	"PARAMETER[\"Central_Meridian\","+(-75+3*i)+"]," +
		        	"PARAMETER[\"Scale_Factor\",1]," +
		        	"PARAMETER[\"Latitude_Of_Origin\",-90]," +
		        	"UNIT[\"Meter\",1]]");
	        cs.setAbrev("EPSG:2219"+i); // Posgar
	        data.put(cs.getAbrev(), cs);
        }
       
       /*
        * pendiente de añadir:
        * 
        * EPSG 3003: Montemario / Italy Zone 1 - Pendiente de añadir
      	* EPSG 4149: CH1903 - Pendiente de añadir
      	* Ecuador:
      	*   PSAD56, Geo, UTM 16/17S,16/17N
      	*/
    }

    /**
     * Mete una nueva proyeccion en la Pool.
     * @param name abreviatura de la proyecccion (i.e. EPSG:23030)
     * @param proj Proyeccion
     */
    public static void add(String name, IProjection proj) {
        data.put(name, proj);
    }

    /**
     * Devuelve una proyeccion a partir de una cadena.
     * @param name abreviatura de la proyecccion (i.e. EPSG:23030)
     * @return Proyeccion si existe
     */
    public IProjection get(String name) {
        IProjection proj = null;

        if (ProjectionPool.data.containsKey(name)) {
            proj = (IProjection) ProjectionPool.data.get(name);
        } else {
        	// Consultation to remote EPSG database
        	// if (right)
        	//    buil new IProjection from GML
        	// else
            System.err.println("ProjectionPool: Key '" + name + "' not set.");
        }

        return proj;
    }

    public static Iterator iterator() {
        ArrayList projs = new ArrayList();

        Iterator iter = data.entrySet().iterator();

        while (iter.hasNext()) {
            projs.add(((Map.Entry) iter.next()).getValue());
        }

        return projs.iterator();
    }

	public boolean doesRigurousTransformations() {
		return false;
	}
}
