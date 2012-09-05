/* jdwglib. Java Library for reading Dwg files.
 *
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 *
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.objects;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gvsig.dwg.lib.DwgFile;
import org.gvsig.dwg.lib.DwgObject;
import org.gvsig.dwg.lib.IDwg2FMap;
import org.gvsig.dwg.lib.IDwg3DTestable;
import org.gvsig.dwg.lib.IDwgBlockMember;
import org.gvsig.dwg.lib.IDwgExtrusionable;
import org.gvsig.dwg.lib.IDwgPolyline;
import org.gvsig.dwg.lib.IDwgVertex;
import org.gvsig.dwg.lib.util.AcadExtrusionCalculator;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.dwg.lib.util.GisModelCurveCalculator;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.exception.CreateGeometryException;


/**
 * The DwgLwPolyline class represents a DWG LwPolyline
 *
 * @author jmorell
 */
public class DwgLwPolyline extends DwgObject
	implements IDwgPolyline, IDwgExtrusionable,
	IDwg3DTestable, IDwg2FMap, IDwgBlockMember{

	private int flag;
	private double constWidth;
	private double elevation;
	private double thickness;
	private double[] normal;
	private List vertices;
	/*
	 *The bulge is the tangent of 1/4 of the included angle for the arc
	 *between the selected vertex and the next vertex in the polyline's vertex list.
	 *A negative bulge value indicates that the arc goes clockwise from
	 *the selected vertex to the next vertex.
	 *A bulge of 0 indicates a straight segment,
	 *and a bulge of 1 is a semicircle.
	 *
	 *http://www.faqs.org/faqs/CAD/autolisp-faq/part2/section-6.html
	 */
	private double[] bulges;
	private double[][] widths;


	public DwgLwPolyline(int index) {
		super(index);
		vertices = new ArrayList();
	}
	/**
	 * @return Returns the bulges.
	 */
	public double[] getBulges() {
		return bulges;
	}
	/**
	 * @param bulges The bulges to set.
	 */
	public void setBulges(double[] bulges) {
		this.bulges = bulges;
	}
	/**
	 * @return Returns the flag.
	 */
	public int getFlag() {
		return flag;
	}
	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(int flag) {
		this.flag = flag;
	}
	/**
	 * @return Returns the vertices.
	 */
	public List getVertices() {
		return vertices;
	}
	/**
	 * @param vertices The vertices to set.
	 */
	public void setVertices(List vertices) {
		this.vertices = vertices;
	}
    /**
     * @return Returns the elevation.
     */
    public double getElevation() {
        return elevation;
    }
    /**
     * @param elevation The elevation to set.
     */
    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
    /**
     * @return Returns the normal.
     */
    public double[] getNormal() {
        return normal;
    }
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
//	public Object clone() {
//		DwgLwPolyline dwgLwPolyline = new DwgLwPolyline(index);
//		dwgLwPolyline.setType(type);
//		dwgLwPolyline.setHandle(handle);
//		dwgLwPolyline.setVersion(version);
//		dwgLwPolyline.setMode(mode);
//		dwgLwPolyline.setLayerHandle(layerHandle);
//		dwgLwPolyline.setColor(color);
//		dwgLwPolyline.setNumReactors(numReactors);
//		dwgLwPolyline.setNoLinks(noLinks);
//		dwgLwPolyline.setLinetypeFlags(linetypeFlags);
//		dwgLwPolyline.setPlotstyleFlags(plotstyleFlags);
//		dwgLwPolyline.setSizeInBits(sizeInBits);
//		dwgLwPolyline.setExtendedData(extendedData);
//		dwgLwPolyline.setGraphicData(graphicData);
//		//dwgLwPolyline.setInsideBlock(insideBlock);
//		dwgLwPolyline.setFlag(flag);
//		dwgLwPolyline.setElevation(elevation);
//		dwgLwPolyline.setConstWidth(constWidth);
//		dwgLwPolyline.setThickness(thickness);
//		dwgLwPolyline.setNormal(normal);
//		dwgLwPolyline.setVertices(vertices);
//		dwgLwPolyline.setBulges(bulges);
//		dwgLwPolyline.setWidths(widths);
//		return dwgLwPolyline;
//	}
	/**
	 * @return Returns the constWidth.
	 */
	public double getConstWidth() {
		return constWidth;
	}
	/**
	 * @param constWidth The constWidth to set.
	 */
	public void setConstWidth(double constWidth) {
		this.constWidth = constWidth;
	}
	/**
	 * @return Returns the thickness.
	 */
	public double getThickness() {
		return thickness;
	}
	/**
	 * @param thickness The thickness to set.
	 */
	public void setThickness(double thickness) {
		this.thickness = thickness;
	}
	/**
	 * @return Returns the widths.
	 */
	public double[][] getWidths() {
		return widths;
	}
	/**
	 * @param widths The widths to set.
	 */
	public void setWidths(double[][] widths) {
		this.widths = widths;
	}
	/**
	 * @param normal The normal to set.
	 */
	public void setNormal(double[] normal) {
		this.normal = normal;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgPolyline#calculateGisModel(java.util.List)
	 */
	public void calculateGisModel(DwgFile dwgFile) {
		if (getVertices() == null) {
			return;
		}
		int flags = getFlag();
		List pts = getVertices();
		double[] bulges = getBulges();
		List newPts = new ArrayList();
		double[] newBulges = null;
		if(bulges != null){
			newBulges = new double[bulges.length];
			System.arraycopy(bulges, 0, newBulges, 0, bulges.length);
		}else{
			bulges = new double[pts.size() ];
			newBulges = new double[bulges.length];
			//dwg spec says numVertex (numSegments + 1)
			//TODO Check this
			for(int i = 0; i < newBulges.length; i++) {
				bulges[i] = 0d;
			}
			newBulges = new double[bulges.length];
			System.arraycopy(bulges, 0, newBulges, 0, bulges.length);
		}
		// TODO: Aqu� pueden existir casos no contemplados ...
//        System.out.println("flags = " + flags);
        if (flags==512 || flags==776 || flags==768) {//closed
			newBulges = new double[bulges.length+1];
			for (int j=0;j<pts.size();j++) {
				newPts.add(pts.get(j));
			}
			newPts.add(pts.get(0));
			newBulges[pts.size()] = 0;
		} else {
			for (int j=0;j<pts.size();j++) {
				newPts.add(pts.get(j));
			}
		}
		if (newPts.size() > 0) {
			setBulges(newBulges);
			List points = GisModelCurveCalculator.calculateGisModelBulge(newPts, newBulges);
			setVertices(points);
		}
//		if (pts.size() > 0) {
//			setBulges(newBulges);
//			List points = GisModelCurveCalculator.calculateGisModelBulge(newPts, newBulges);
//			setVertices(points);
//		}
//		else {
////			System.out.println("Encontrada polil�nea sin puntos ...");
//			// TODO: No se debe mandar nunca una polil�nea sin puntos, si esto
//			// ocurre es porque existe un error que hay que corregir ...
//		}
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		if (getVertices() == null) {
			return;
		}
		 List vertices = getVertices();
         double[] lwPolylineExt = getNormal();
         // Normals and Extrusions aren`t the same
         if (lwPolylineExt[0]==0 && lwPolylineExt[1]==0 && lwPolylineExt[2]==0) {
			lwPolylineExt[2] = 1.0;
		}

         double elev = getElevation();
         List lwPolylinePoints3D = new ArrayList();
         for (int j=0;j<vertices.size();j++) {
        	 double[] point = new double[3];
             point[0] = ((double[])vertices.get(j))[0];
             point[1] = ((double[])vertices.get(j))[1];
             point[2] = elev;
             lwPolylinePoints3D.add(AcadExtrusionCalculator.
            		 extrude2(point, lwPolylineExt));
         }
         setElevation(elev);
         List newVertices = new ArrayList();
         for (int j=0;j<vertices.size();j++) {
        	 double[] point = (double[]) lwPolylinePoints3D.get(j);
             newVertices.add(new double[]{point[0], point[1]});
         }
         setVertices(newVertices);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return getElevation() != 0.0;
	}
	public double getZ() {
		return getElevation();
	}

	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		return FMapUtil.ptsToMultiCurve(getVertices(),
				getGeometrySubType(is3DFile));
	}

	public String toFMapString(boolean is3DFile) {
		if(is3DFile) {
			return "FPolyline3D";
		} else {
			return "FPolyline2D";
		}
	}

	public String toString(){
		return "LwPolyline";
	}


	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks, DwgFile callBack) {
		DwgLwPolyline transformedEntity = null;
		List vertices = this.getVertices();
		Object vertice;
		double[] point;
		if (vertices!=null) {
		    List transformedVertices = new ArrayList();
			for (int i=0; i< vertices.size(); i++) {
				vertice = vertices.get(i);
				if (vertice instanceof Point2D.Double){
					point = new double[] {((Point2D.Double)vertices.get(i)).x, ((Point2D.Double)vertices.get(i)).y};
				} else {
					point = (double[]) vertices.get(i);
				}
				double[] pointAux = new double[]{point[0] - bPoint[0],
							point[1] - bPoint[1]};
				double laX = insPoint.getX() + ((pointAux[0]*scale[0])*Math.cos(rot) + (pointAux[1]*scale[1])*(-1)*Math.sin(rot));
				double laY = insPoint.getY() + ((pointAux[0]*scale[0])*Math.sin(rot) + (pointAux[1]*scale[1])*Math.cos(rot));
				transformedVertices.add(new double[]{laX, laY});
			}
			transformedEntity = (DwgLwPolyline)this.clone();
			transformedEntity.setVertices(transformedVertices);
			transformedEntity.setElevation((this.getElevation() * scale[2]));
			transformedEntity.setHandle(this.getHandle());
			dwgObjectsWithoutBlocks.add(transformedEntity);
			handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
//			dwgObjectsWithoutBlocks.add(this);
		}//if
	}

	public Object clone(){
		DwgLwPolyline dwgLwPolyline = new DwgLwPolyline(index);
		this.fill(dwgLwPolyline);
		return dwgLwPolyline;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgLwPolyline myObj = (DwgLwPolyline)obj;

		myObj.setBulges(bulges);
		myObj.setConstWidth(constWidth);
		myObj.setElevation(elevation);
		myObj.setFlag(flag);
		myObj.setNormal(normal);
		myObj.setThickness(thickness);
		myObj.setVertices(vertices);
		myObj.setWidths(widths);

	}
	public void addVertex(IDwgVertex vertex) {
		vertices.add(vertex.getPoint());

	}

	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM3D;
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.MULTICURVE;
	}

}
