/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.dal.store.shp.utils;

import java.io.File;



/**
 * Clase con las constantes que representan los diferentes tipos de Shape y
 * métodos estáticos relativos a los shapes.
 *
 * @author Vicente Caballero Navarro
 */
public class SHP {

    public static final int NULL = 0;
    public static final int POINT2D = 1;
    public static final int POLYLINE2D = 3;
    public static final int POLYGON2D = 5;
    public static final int MULTIPOINT2D = 8;
    public static final int POINT3D = 11;
    public static final int POLYLINE3D = 13;
    public static final int POLYGON3D = 15;
    public static final int MULTIPOINT3D = 18;
	public final static int POINTM = 21;
	public final static int POLYLINEM = 23;
	public final static int POLYGONM = 25;
	public final static int MULTIPOINTM = 28;
    /**
     * Crea a partir del tipo de geometría un shape del tipo más adecuado.
     *
     * @param type Tipo de geometría.
     *
     * @return Geometría más adecuada.
     *
     * @throws ShapefileException Se lanza cuando es causada por la creación
     *         del shape.
     */
    public static SHPShape create(int type) {
        SHPShape shape;

        switch (type) {
        	case 0:
        		shape = new SHPNull(type);
        		break;

            case 1:
            case 11:
            case 21:
                shape = new SHPPoint(type);

                break;

            case 3:
            case 13:
            case 23:
                shape = new SHPMultiLine(type);

                break;

            case 5:
            case 15:
            case 25:
                shape = new SHPPolygon(type);

                break;

            case 8:
            case 18:
            case 28:
                shape = new SHPMultiPoint(type);

                break;

            default:
                shape = null;
        }

        return shape;
    }

    /**
     * Devuelve un array con dos doubles, el primero representa el mínimo valor
     * y el segundo el máximo de entre los valores que se pasan como parámetro
     * en forma de array.
     *
     * @param zs Valores a comprobar.
     *
     * @return Array de doubles con el valor mínimo y el valor máximo.
     */
    public static double[] getZMinMax(double[] zs) {
        if (zs == null) {
            return null;
        }

        double min = Double.MAX_VALUE;
        double max = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < zs.length; i++) {
            if (zs[i] > max) {
                max = zs[i];
            }

            if (zs[i] < min) {
                min = zs[i];
            }
        }

        return new double[] { min, max };
    }
    public static File getDbfFile(File shpFile){
    	String str = shpFile.getAbsolutePath();
		File directory=shpFile.getParentFile();
		File[] files=new File[0];
		if (directory!=null){
			MyFileFilter myFileFilter = new MyFileFilter(str);
			files=directory.listFiles(myFileFilter);
		}
		String[] ends=new String[] {"dbf","DBF","Dbf","dBf","DBf","dbF","DbF","dBF"};
		File dbfFile=findEnd(str,files,ends);
		return dbfFile;
    }

    public static File getShpFile(File dbfFile){
    	String str = dbfFile.getAbsolutePath();
		File directory=dbfFile.getParentFile();
		File[] files=new File[0];
		if (directory!=null){
			MyFileFilter myFileFilter = new MyFileFilter(str);
			files=directory.listFiles(myFileFilter);
		}
		String[] ends=new String[] {"shp","SHP","Shp","sHp","SHp","shP","ShP","sHP"};
		File shpFile=findEnd(str,files,ends);
		return shpFile;
    }

    public static File getShxFile(File shpFile){
    	String str = shpFile.getAbsolutePath();
		File directory=shpFile.getParentFile();
		File[] files=new File[0];
		if (directory!=null){
			MyFileFilter myFileFilter = new MyFileFilter(str);
			files=directory.listFiles(myFileFilter);
		}
		String[] ends=new String[] {"shx","SHX","Shx","sHx","SHx","shX","ShX","sHX"};
		File shxFile=findEnd(str,files,ends);
		return shxFile;
    }

    public static File getPrjFile(File shpFile) {
		String str = shpFile.getAbsolutePath();
		File directory = shpFile.getParentFile();
		File[] files = new File[0];
		if (directory != null) {
			MyFileFilter myFileFilter = new MyFileFilter(str);
			files = directory.listFiles(myFileFilter);
		}
		String[] ends = new String[] { "prj", "PRJ", "Prj", "pRj", "PRj",
				"prJ", "PrJ", "pRJ" };
		File shxFile = findEnd(str, files, ends);
		return shxFile;
	}

    private static File findEnd(String str,File[] files, String[] ends) {
    	for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[0])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[1])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[2])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[3])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[4])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[5])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[6])) {
				return dbfFile;
			}
		}
		for (int i=0;i<files.length;i++) {
			File dbfFile=files[i];
			if (dbfFile.getAbsolutePath().endsWith(ends[7])) {
				return dbfFile;
			}
		}
		return new File(str.substring(0, str.length() - 3) + ends[0]);
    }

}
