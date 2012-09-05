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
import org.gvsig.dwg.lib.IDwgPolyline;
import org.gvsig.dwg.lib.IDwgVertex;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The DwgPolyline3D class represents a DWG Polyline3D
 *
 * @author jmorell
 */
public class DwgPolyline3D extends DwgObject
	implements IDwgPolyline, IDwg3DTestable, IDwg2FMap, IDwgBlockMember{

	private int splineFlags;
	private int closedFlags;
	private DwgHandleReference firstVertexHandle = null;
	private DwgHandleReference lastVertexHandle = null;
	private DwgHandleReference seqendHandle = null;
	private ArrayList vertexHandles;
	private List vertices;
	private double[] bulges;

	private static Logger logger = LoggerFactory.getLogger(DwgPolyline3D.class.getName());

	public DwgPolyline3D(int index) {
		super(index);
		vertices = new ArrayList();
		vertexHandles = new ArrayList();
	}


	/**
	 * @return Returns the closedFlags.
	 */
	public int getClosedFlags() {
		return closedFlags;
	}
	/**
	 * @param closedFlags The closedFlags to set.
	 */
	public void setClosedFlags(int closedFlags) {
		this.closedFlags = closedFlags;
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
	 * @return Returns the splineFlags.
	 */
	public int getSplineFlags() {
		return splineFlags;
	}
	/**
	 * @param splineFlags The splineFlags to set.
	 */
	public void setSplineFlags(int splineFlags) {
		this.splineFlags = splineFlags;
	}

	public void calculateGisModel(DwgFile dwgFile){
		ArrayList pts = new ArrayList();
		double[] pt = new double[3];

		int closedFlags = getClosedFlags();
		if(dwgFile.getDwgVersion().equalsIgnoreCase("Autocad R2004, R2005, R2006")){
			ArrayList vertexHandles = getVertexHandles();
			DwgObject seqend = null;
			for (int i=0; i<vertexHandles.size(); i++){
				DwgHandleReference vertice = (DwgHandleReference)vertexHandles.get(i);
				DwgObject objVertex = dwgFile.getDwgObjectFromHandle(vertice.getOffset());
				if (objVertex != null){
					if (objVertex instanceof DwgVertex3D) {
						pts.add(((DwgVertex3D)objVertex).getPoint());
					} else {
						logger.warn("Encontrado un "+objVertex.getClass().getName()+" " +
								"con indice "+i+" en la lista de vertices de Polyline3D");
					}
				} else {
					logger.warn("No se ha encontrado el vertice "+i+" de "+vertexHandles.size()
							+" de la Polyline3D "+this.getIndex());
					if (i==0){
						seqend = dwgFile.getDwgObjectFromHandle(seqendHandle.getOffset());
					}
				}
			}
			if(seqend != null){
				if (seqend instanceof DwgVertex3D) {
					pts.add(((DwgVertex3D)seqend).getPoint());
				} else {
					logger.warn("Encontrado un "+seqend.getClass().getName()+" en seqend de Polyline3D "+
							this.getIndex()+" cuando debería ser un DwgVertex3D");
				}
			}

		} else {
			DwgHandleReference firstHandle = getFirstVertexHandle();
			DwgHandleReference lastHandle = getLastVertexHandle();
			double bulge = 0d;

			DwgObject first = dwgFile.getDwgObjectFromHandle(firstHandle.getOffset());
			DwgObject last = dwgFile.getDwgObjectFromHandle(lastHandle.getOffset());
			DwgObject seqend = dwgFile.getDwgObjectFromHandle(seqendHandle.getOffset());
			if(last == null){
				logger.warn("Polyline3D con vertice final a null");
				return;
			}
			if(first == null){
				logger.warn("Polyline3D con offset = "+this.getHandle().getOffset() +" con vertice inicial a null");
				if(seqend != null){
					first = last;
					last = seqend;
				} else {
					logger.warn("Polyline3D con vertice seqend a null");
				}
			}

			if(!(first instanceof DwgVertex3D)){
				logger.warn("El primer vertice de Polyline3D es "+
						first.getClass().getName());
				return;
			}

			if(!(last instanceof DwgVertex3D)){
				logger.warn("El ultimo vertice de Polyline3D es "+
						last.getClass().getName());
				return;
			}

			int firstObjIdx = dwgFile.getIndexOf(first);
			int lastObjIdx =  dwgFile.getIndexOf(last);
			if(firstObjIdx == -1 || lastObjIdx == -1){
				logger.warn("Calculate GIS Model: Problemas en la LinkedList: 1º="+firstObjIdx+",Ultimo="+lastObjIdx);
				return;
			}

			for(int i = firstObjIdx; i <= lastObjIdx; i++){
				DwgObject obj = dwgFile.getDwgObject(i);
				if(obj instanceof DwgVertex3D){
					DwgVertex3D vertex = (DwgVertex3D) obj;
					pt = (vertex).getPoint();
					pts.add(new double[]{pt[0], pt[1], pt[2]});
				}else if(obj instanceof DwgSeqend){
					//TODO En Polyline2D J.Morell no interrumpia el barrido,
					//pero aquí Sí. REVISAR
					break;
				}else{
					logger.warn("Encontrado "+obj.getClass().getName()+" en la lista de vertices de Polyline3D");
				}
			}//for
		}
		if (pts.size()>0) {
			List newPts = new ArrayList();
			for (int j=0;j<pts.size();j++) {
				newPts.add(pts.get(j));
			}
			if ((closedFlags & 0x1)== 0x1) {

				newPts.add(pts.get(0));
			}
			setPts(newPts);
		} else {
			logger.warn("Encontrada polilínea sin puntos ...");
			// TODO: No se debe mandar nunca una polilínea sin puntos, si esto
			// ocurre es porque existe un error que hay que corregir ...
		}

	}

	//TODO Metodo antiguo y muy ineficiente. Se deja hasta que se
	//revise que funciona el nuevo perfectamente
	public void calculateGisModel(List dwgObjects) {
		int closedFlags = getClosedFlags();

//		En estas dos lineas de abajo y en el resto del metodo
//		se mantiene el mecanismo anterior al refactoring.
//		TODO: Pensar si deberiamos coger el handle completo.
//		Tal vez deberiamos tomar el handle completo y evaluar
//		a donde apuntan (pueden haber 2 handles con codigo y offset
//		distintos y que, sin embargo apunten al mismo objeto).

		int firstHandle = getFirstVertexHandle().getOffset();
		int lastHandle = getLastVertexHandle().getOffset();
		ArrayList pts = new ArrayList();
		double[] pt = new double[3];

		//TODO Sustituir esto por un hashtable handle->DwgObject
		for (int j=0;j< dwgObjects.size();j++) {
			DwgObject firstVertex = (DwgObject)dwgObjects.get(j);
			if (firstVertex instanceof DwgVertex3D) {
				int vertexHandle = firstVertex.getHandle().getOffset();
				if (vertexHandle==firstHandle) {
					int k=0;
					while (true) {
						DwgObject vertex = (DwgObject)dwgObjects.get(j+k);
						int vHandle = vertex.getHandle().getOffset();
						if (vertex instanceof DwgVertex3D) {
							pt = ((DwgVertex3D)vertex).getPoint();
							pts.add(new double[]{pt[0], pt[1], pt[2]});
							k++;
							if (vHandle==lastHandle && vertex instanceof DwgVertex3D) {
								break;
							}
						} else if (vertex instanceof DwgSeqend) {
							break;
						}//if
					}//while
				}//if
			}//if
		}//for

		if (pts.size()>0) {
			List newPts = new ArrayList();
			for (int j=0;j<pts.size();j++) {
				newPts.add(pts.get(j));
			}
			if ((closedFlags & 0x1)==0x1) {
				newPts.add(pts.get(0));
			}
			setPts(newPts);
		} else {
//			System.out.println("Encontrada polilinea sin puntos ...");
			// TODO: No se debe mandar nunca una polilinea sin puntos, si esto
			// ocurre es porque existe un error que hay que corregir ...
		}
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#has3DData()
	 */
	public boolean has3DData() {

		List pts = getPts();
		if(pts == null) {
			return false;
		}
		double z = 0d;
	    for (int j = 0; j<pts.size(); j++) {
	        z = ((double[])pts.get(j))[2];
			if (z != 0.0) {
				return true;
			}
	    }
	    return false;

	}
	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg3DTestable#getZ()
	 */
	public double getZ() {
		List points3D = getPts();
		if (points3D != null) {
			boolean constantElevation = true;
			double[] firstPt = (double[]) points3D.get(0);
			for (int j = 0; j < points3D.size(); j++) {
				double[] pt = (double[]) points3D.get(j);
				if (pt[2] != firstPt[2]) {
					constantElevation = false;
					break;
				}
			}
			if (constantElevation) {
				return firstPt[2];
			}
		}
		return 0d;
	}

	public double[] getPZ() {
		List points3D = getPts();
		if (points3D != null) {
			double [] pZ = new double[points3D.size()];
			for (int i = 0; i < points3D.size(); i++) {
				double[] pt = (double[]) points3D.get(i);
				pZ[i] = pt[2];
			}
			return pZ;
		}
		return new double[] {0};
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.jdwglib.dwg.IDwg2FMap#toFMapGeometry(boolean)
	 */
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		return FMapUtil.ptsToMultiCurve(getPts(), getGeometrySubType(is3DFile));
	}
//	public Geometry toFMapGeometry_old(boolean is3DFile) {
//		FPolyline2D pline = null;
//		List points3D = getPts();
//		if (points3D != null && points3D.size() > 0) {
//			if (is3DFile) {
//				pline = FMapUtil.points3DToFPolyline3D(points3D);
//			} else {
//				List points2D = new ArrayList();
//				for (int j = 0; j < points3D.size(); j++) {
//					double[] pt3d = (double[]) points3D.get(j);
//					double[] pt  = new double[]{pt3d[0],
//							pt3d[1]};
//					points2D.add(pt);
//				}
//				pline = FMapUtil.points2DToFPolyline2D(points2D);
//			}//if
//			return  ShapeFactory.createGeometry(pline);
//		}//if
//		return null;
//
//	}
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
		return "Polyline3D";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone(){
		DwgPolyline3D obj = new DwgPolyline3D(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgPolyline3D myObj = (DwgPolyline3D)obj;

		myObj.setBulges(bulges);
		myObj.setClosedFlags(closedFlags);
		myObj.setFirstVertexHandle(firstVertexHandle);
		myObj.setLastVertexHandle(lastVertexHandle);
		myObj.setPts(vertices);
		myObj.setSeqendHandle(seqendHandle);
		myObj.setSplineFlags(splineFlags);
	}
	public void addVertex(IDwgVertex vertex) {
		vertices.add(vertex.getPoint());
	}

	//TODO Por qué Polyline3D no tenía implementado el transform2Block???
	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks, DwgFile callBack) {

		DwgPolyline3D transformedEntity = null;
		List vertices = this.getPts();

		if (vertices != null) {
		    List transformedVertices = new ArrayList();
			for (int i=0;i < vertices.size();i++) {
				double[] pointAux = null;
			    pointAux = new double[]{((double[]) vertices.get(i))[0] - bPoint[0],
			    		((double[]) vertices.get(i))[1] - bPoint[1]};

				double laX = insPoint.getX() + ((pointAux[0] * scale[0])*Math.cos(rot) + (pointAux[1]*scale[1])*(-1)*Math.sin(rot));
				double laY = insPoint.getY() + ((pointAux[0]*scale[0])*Math.sin(rot) + (pointAux[1]*scale[1])*Math.cos(rot));
				double laZ = ((double[]) vertices.get(i))[2] - bPoint[2];
				transformedVertices.add(new double[]{laX, laY, laZ});
			}//for
			transformedEntity = (DwgPolyline3D)this.clone();
			transformedEntity.setPts(transformedVertices);
			dwgObjectsWithoutBlocks.add(transformedEntity);
			handleObjWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
		}
	}

	/**
	 * @param Handles The vertexHandles to set.
	 */
	public void setVertexHandles(ArrayList Handles){
		this.vertexHandles = Handles;
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

	public int getGeometrySubType(boolean is3DFile) {
		return FMapUtil.SUBTYPE_GEOM3D;
	}

	public int getGeometryType() {
		return Geometry.TYPES.MULTICURVE;
	}



}
