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
import org.gvsig.dwg.lib.DwgHandleReference;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The DwgPolyline2D class represents a DWG Polyline2D
 *
 * @author jmorell
 */
public class DwgPolyline2D extends DwgObject
	implements IDwgPolyline, IDwgExtrusionable, IDwg3DTestable, IDwg2FMap, IDwgBlockMember{

	private static Logger logger = LoggerFactory.getLogger(DwgPolyline2D.class.getName());

	private int flags;
	private int curveType;
	private double initWidth;
	private double endWidth;
	private double thickness;
	private double elevation;
	private double[] extrusion;
	private DwgHandleReference firstVertexHandle = null;
	private DwgHandleReference lastVertexHandle = null;
	private DwgHandleReference seqendHandle = null;
	private List vertices;
	private double[] bulges;
	private ArrayList vertexHandles;

	public DwgPolyline2D(int index) {
		super(index);
		vertices = new ArrayList();
		vertexHandles = new ArrayList();
	}
	/**
	 * @return Returns the firstVertexHandle.
	 */
	public DwgHandleReference getFirstVertexHandle() {
		return firstVertexHandle;
	}
	/**
	 * @param firstVertexHandle The firstVertexHandle to set.
	 */
	public void setFirstVertexHandle(DwgHandleReference firstVertexHandle) {
		this.firstVertexHandle = firstVertexHandle;
	}
	/**
	 * @return Returns the flags.
	 */
	public int getFlags() {
		return flags;
	}
	/**
	 * @param flags The flags to set.
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}
	/**
	 * @return Returns the lastVertexHandle.
	 */
	public DwgHandleReference getLastVertexHandle() {
		return lastVertexHandle;
	}
	/**
	 * @param lastVertexHandle The lastVertexHandle to set.
	 */
	public void setLastVertexHandle(DwgHandleReference lastVertexHandle) {
		this.lastVertexHandle = lastVertexHandle;
	}
	/**
	 * @return Returns the pts.
	 */
	public List getPts() {
		return vertices;
	}
	/**
	 * @param pts The pts to set.
	 */
	public void setPts(List pts) {
		this.vertices = pts;
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
	 * @return Returns the initWidth.
	 */
	public double getInitWidth() {
		return initWidth;
	}
	/**
	 * @param initWidth The initWidth to set.
	 */
	public void setInitWidth(double initWidth) {
		this.initWidth = initWidth;
	}
	/**
	 * @return Returns the seqendHandle.
	 */
	public DwgHandleReference getSeqendHandle() {
		return seqendHandle;
	}
	/**
	 * @param seqendHandle The seqendHandle to set.
	 */
	public void setSeqendHandle(DwgHandleReference seqendHandle) {
		this.seqendHandle = seqendHandle;
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
	 * @return Returns the curveType.
	 */
	public int getCurveType() {
		return curveType;
	}
	/**
	 * @param curveType The curveType to set.
	 */
	public void setCurveType(int curveType) {
		this.curveType = curveType;
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
	 * @return Returns the endWidth.
	 */
	public double getEndWidth() {
		return endWidth;
	}
	/**
	 * @param endWidth The endWidth to set.
	 */
	public void setEndWidth(double endWidth) {
		this.endWidth = endWidth;
	}
	/**
	 * @return Returns the extrusion.
	 */
	public double[] getExtrusion() {
		return extrusion;
	}
	/**
	 * @param extrusion The extrusion to set.
	 */
	public void setExtrusion(double[] extrusion) {
		this.extrusion = extrusion;
	}

	/**
	 * @param Handles The vertexHandles to set.
	 */
	public void setVertexHandles(ArrayList handles){
		this.vertexHandles = handles;
	}

	/**
	 * @return Returns the vertexHandles.
	 */
	public ArrayList getVertexHandles(){
		return this.vertexHandles;
	}
	/**
	 * @param Handle The vertexHandles to add.
	 */
	public void addVertexHandle(DwgHandleReference handle){
		this.vertexHandles.add(handle);
	}


	public void calculateGisModel(DwgFile dwgFile){
		int flags = getFlags();
//		En este metodo se mantiene el mecanismo anterior
//		al refactoring de los handles references.
//		TODO: Pensar si deberiamos coger el handle completo.
//		Tal vez deberiamos tomar el handle completo y evaluar
//		a donde apuntan (pueden haber 2 handles con codigo y offset
//		distintos y que, sin embargo apunten al mismo objeto).

		ArrayList pts = new ArrayList();
		ArrayList bulges = new ArrayList();
		double[] pt = new double[3];
		double bulge = 0d;

		if(dwgFile.getDwgVersion().equalsIgnoreCase("Autocad R2004, R2005, R2006")){
			ArrayList vertexHandles = getVertexHandles();
			DwgObject seqend = null;
			for (int i=0; i<vertexHandles.size(); i++){
				DwgHandleReference vertice = (DwgHandleReference)vertexHandles.get(i);
				DwgObject objVertex = dwgFile.getDwgObjectFromHandle(vertice.getOffset());
				if (objVertex != null){
					if (objVertex instanceof DwgVertex2D) {
						pts.add(((DwgVertex2D)objVertex).getPoint());
						bulge = ((DwgVertex2D)objVertex).getBulge();
						bulges.add(new Double(bulge));
					} else {
						logger.warn("Encontrado un "+objVertex.getClass().getName()+" " +
								"con indice "+i+" en la lista de vertices de Polyline2D");
					}
				} else {
					logger.warn("No se ha encontrado el vertice "+i+" de "+vertexHandles.size()
							+" de la Polyline2D "+this.getIndex());
					if (i==0){
						seqend = dwgFile.getDwgObjectFromHandle(seqendHandle.getOffset());
					}
				}
			}
			if(seqend != null){
				if (seqend instanceof DwgVertex2D) {
					pts.add(((DwgVertex2D)seqend).getPoint());
					bulge = ((DwgVertex2D)seqend).getBulge();
					bulges.add(new Double(bulge));
				} else {
					logger.warn("Encontrado un "+seqend.getClass().getName()+" en seqend de Polyline2D "+
							this.getIndex()+" cuando debería ser un DwgVertex2D");
				}
			}

		} else {
			DwgHandleReference firstHandle = getFirstVertexHandle();
			DwgHandleReference lastHandle = getLastVertexHandle();


			DwgObject first = dwgFile.getDwgObjectFromHandle(firstHandle.getOffset());
			DwgObject last = dwgFile.getDwgObjectFromHandle(lastHandle.getOffset());
			if(first == null || last == null){
				logger.warn("Polyline2D con vertices inicial o final a null");
				return;
			}

			if(!(first instanceof DwgVertex2D)){
				logger.warn("El primer vertice de Polyline2D es "+
						first.getClass().getName());
				return;
			}

			if(!(last instanceof DwgVertex2D)){
				logger.warn("El primer vertice de Polyline2D es "+
						first.getClass().getName());
				return;
			}

			int firstObjIdx = dwgFile.getIndexOf(first);
			int lastObjIdx =  dwgFile.getIndexOf(last);
			if(firstObjIdx == -1 || lastObjIdx == -1){
				logger.warn("Calculate GIS Model: Problemas en la LinkedList: 1�="+firstObjIdx+",Ultimo="+lastObjIdx);
				return;
			}

//			pt = ((DwgVertex2D)first).getPoint();
//			pts.add(new Point2D.Double(pt[0], pt[1]));
//			bulge = ((DwgVertex2D)first).getBulge();
//			bulges.add(new Double(bulge));

			for(int i = firstObjIdx; i <= lastObjIdx; i++){
				DwgObject obj = dwgFile.getDwgObject(i);
				if(obj instanceof DwgVertex2D){
					DwgVertex2D vertex = (DwgVertex2D) obj;
					pt = vertex.getPoint();
					pts.add(new Point2D.Double(pt[0], pt[1]));
					bulge = vertex.getBulge();
					bulges.add(new Double(bulge));
				}else{
					logger.warn("Encontrado "+obj.getClass().getName()+" en la lista de vertices de Polyline2D");
				}
			}//for
		}

		if (pts.size()>0) {
			List newPts = new ArrayList();
			if ((flags & 0x1)==0x1) {
				for (int j=0;j<pts.size();j++) {
					newPts.add(pts.get(j));
				}
				newPts.add(pts.get(0));
				bulges.add(new Double(0));
			} else {
				for (int j=0;j<pts.size();j++) {
					newPts.add(pts.get(j));
				}
			}
			double[] bs = new double[bulges.size()];
			for (int j=0;j<bulges.size();j++) {
				bs[j] = ((Double)bulges.get(j)).doubleValue();
			}
			setBulges(bs);
			List points = GisModelCurveCalculator.
			calculateGisModelBulge(newPts, bs);
			setPts(points);
		} else {
			logger.warn("Encontrada polilínea sin puntos ...");
			// TODO: No se debe mandar nunca una polil�nea sin puntos, si esto
			// ocurre es porque existe un error que hay que corregir ...
		}
	}
	//TODO Este metodo es antiguo y MUY INEFICIENTE
	//No obstante lo dejo porque hace referencia a SeqEnd y a un posible bug
	public void calculateGisModel(List dwgObjects) {
		int flags = getFlags();
//		En estas dos lineas de abajo y en el resto del metodo
//		se mantiene el mecanismo anterior al refactoring.
//		TODO: Pensar si deberiamos coger el handle completo.
//		Tal vez deberiamos tomar el handle completo y evaluar
//		a donde apuntan (pueden haber 2 handles con codigo y offset
//		distintos y que, sin embargo apunten al mismo objeto).
		int firstHandle = getFirstVertexHandle().getOffset();
		int lastHandle = getLastVertexHandle().getOffset();
		ArrayList pts = new ArrayList();
		ArrayList bulges = new ArrayList();
		double[] pt = new double[3];

		//TODO Esto cambiarlo. Es lento y poco elegante

		for (int j=0;j<dwgObjects.size();j++) {
			DwgObject firstVertex = (DwgObject)dwgObjects.get(j);

			if (firstVertex instanceof DwgVertex2D) {
				int vertexHandle = firstVertex.getHandle().getOffset();
				if (vertexHandle==firstHandle) {
					int k=0;
					while (true) {
						DwgObject vertex = (DwgObject)dwgObjects.get(j+k);
						int vHandle = vertex.getHandle().getOffset();
						if (vertex instanceof DwgVertex2D) {
							pt = ((DwgVertex2D)vertex).getPoint();
							pts.add(new Point2D.Double(pt[0], pt[1]));
							double bulge = ((DwgVertex2D)vertex).getBulge();
							bulges.add(new Double(bulge));
							k++;
							if (vHandle==lastHandle && vertex instanceof DwgVertex2D) {
								break;
							}
						} else if (vertex instanceof DwgSeqend) {
                            // 051116, jmorell: Polil�neas_ACAD2000.dwg tiene un DwgSeqend en mitad de
                            //una secuencia de v�rtices. Precauci�n con esto puesto que es posible que esta
                            // condici�n fuera requerida en la carga de otros DWGs.
                            //break;
                            k++;
						}
					}//while
				}//if first handle
			}//if
		}//for

		if (pts.size()>0) {
			List newPts = new ArrayList();
			if ((flags & 0x1)==0x1) {
				for (int j=0;j<pts.size();j++) {
					newPts.add(pts.get(j));
				}
				newPts.add(pts.get(0));
				bulges.add(new Double(0));
			} else {
				for (int j=0;j<pts.size();j++) {
					newPts.add(pts.get(j));
				}
			}
			double[] bs = new double[bulges.size()];
			for (int j=0;j<bulges.size();j++) {
				bs[j] = ((Double)bulges.get(j)).doubleValue();
			}
			setBulges(bs);
			List points = GisModelCurveCalculator.
					calculateGisModelBulge(newPts, bs);
			setPts(points);
		} else {
//			System.out.println("Encontrada polil�nea sin puntos ...");
			// TODO: No se debe mandar nunca una polil�nea sin puntos, si esto
			// ocurre es porque existe un error que hay que corregir ...
		}
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwgExtrusionable#applyExtrussion()
	 */
	public void applyExtrussion() {
		  if(getPts() == null) {
			return;
		}
		  List vertices = getPts();
	      double[] polyline2DExt = getExtrusion();
	      double elev = getElevation();
	      double[][] polylinePoints3D = new double[vertices.size()][3];
	      for (int j = 0;j < vertices.size(); j++) {
	          polylinePoints3D[j][0] = ((double[])vertices.get(j))[0];
	          polylinePoints3D[j][1] = ((double[])vertices.get(j))[1];
	          polylinePoints3D[j][2] = elev;
	          polylinePoints3D[j] = AcadExtrusionCalculator.extrude2(polylinePoints3D[j], polyline2DExt);
	      }
	      setElevation(elev);
	      for (int j=0;j<vertices.size();j++) {
	          vertices.add(new double[]{(polylinePoints3D[j])[0], (polylinePoints3D[j])[1]});
	      }
	      setPts(vertices);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {
		return (getElevation() != 0.0);
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#getZ()
	 */
	public double getZ() {
		return getElevation();
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapGeometry(boolean)
	 */
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		return FMapUtil.ptsToMultiCurve(getPts(), getGeometrySubType(is3DFile));
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapString(boolean)
	 */
	public String toFMapString(boolean is3DFile) {
		if(is3DFile) {
			return "FPolyline3D";
		} else {
			return "FPolyline2D";
		}
	}

	public String toString(){
		return "Polyline2D";
	}
	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks, DwgFile callBack) {

		DwgPolyline2D transformedEntity = null;
		List vertices = this.getPts();

		if (vertices != null) {
		    List transformedVertices = new ArrayList();
			for (int i=0;i < vertices.size();i++) {
				double[] pointAux = null;
			    pointAux = new double[]{((double[]) vertices.get(i))[0] - bPoint[0],
			    		((double[]) vertices.get(i))[1] - bPoint[1]};

				double laX = insPoint.getX() + ((pointAux[0] * scale[0])*Math.cos(rot) + (pointAux[1]*scale[1])*(-1)*Math.sin(rot));
				double laY = insPoint.getY() + ((pointAux[0]*scale[0])*Math.sin(rot) + (pointAux[1]*scale[1])*Math.cos(rot));
				transformedVertices.add(new double[]{laX, laY});
			}//for
			transformedEntity = (DwgPolyline2D)this.clone();
			transformedEntity.setPts(transformedVertices);
			transformedEntity.setElevation((this.getElevation() * scale[2]));
			dwgObjectsWithoutBlocks.add(transformedEntity);
			handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgPolyline2D obj = new DwgPolyline2D(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgPolyline2D myObj = (DwgPolyline2D)obj;

		myObj.setBulges(bulges);
		myObj.setCurveType(curveType);
		myObj.setElevation(elevation);
		myObj.setEndWidth(endWidth);
		myObj.setExtrusion(extrusion);
		myObj.setFirstVertexHandle(firstVertexHandle);
		myObj.setFlags(flags);
		myObj.setInitWidth(initWidth);
		myObj.setLastVertexHandle(lastVertexHandle);
		myObj.setPts(vertices);
		myObj.setSeqendHandle(seqendHandle);
		myObj.setThickness(thickness);

	}
	public void addVertex(IDwgVertex vertex) {
		vertices.add(vertex.getPoint());
	}

	public int getGeometrySubType(boolean is3DFile) {
		return Geometry.SUBTYPES.GEOM2D;
	}

	public int getGeometryType() {
		return Geometry.TYPES.MULTICURVE;
	}

}
