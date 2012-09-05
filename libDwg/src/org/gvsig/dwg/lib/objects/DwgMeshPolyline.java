/*
 * Created on 03-feb-2007
 *
 * gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
/* CVS MESSAGES:
*
* $Id: DwgMeshPolyline.java 29050 2009-05-28 12:21:48Z jmvivo $
* $Log$
* Revision 1.4.2.1  2007-03-21 19:49:16  azabala
* implementation of dwg 12, 13, 14.
*
* Revision 1.4  2007/03/20 19:57:08  azabala
* source code cleaning
*
* Revision 1.3  2007/03/06 19:39:38  azabala
* Changes to adapt dwg 12 to general architecture
*
* Revision 1.2  2007/03/02 20:31:22  azabala
* *** empty log message ***
*
* Revision 1.1  2007/03/01 19:58:53  azabala
* refactor of pface and mesh names
*
* Revision 1.2  2007/02/07 12:44:27  fdiaz
* A�adido o modificado el metodo clone para que el DwgObject se encargue de las propiedades comunes a todos los objetos.
* A�adido el metodo fill.
*
* Revision 1.1  2007/02/05 07:03:22  azabala
* *** empty log message ***
*
*
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
import org.gvsig.dwg.lib.IDwgBlockMember;
import org.gvsig.dwg.lib.IDwgPolyline;
import org.gvsig.dwg.lib.IDwgVertex;
import org.gvsig.dwg.lib.util.FMapUtil;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.exception.CreateGeometryException;

/**
 * This class is a Mesh (polyline mesh), what is different of
 * a Polyface mesh (polyline pface).
 *
 *
 * */
public class DwgMeshPolyline extends DwgObject
		implements IDwgPolyline, IDwg2FMap, IDwgBlockMember{

	private int flags;
	/**
	 * Curves and smooth surface type (optional; default = 0);
	 * integer codes, not bit-coded:
	 		0 = No smooth surface fitted
	 		5 = Quadratic B-spline surface
	 		6 = Cubic B-spline surface
	 		8 = Bezier surface
	 * */
	private int curveType;

	/**
	 * Polygon mesh M vertex count
	 * (if curvetype is Spline, it has smooth M density)
	 */
	private int mVerticies;
	/**
	 * Polygon mesh N vertex count
	 * (if curvetype is Spline, it has smooth n density)
	 * */
	private int nVerticies;
	/**
	 * If the mesh is closed in the M direction
	 * */
	private boolean isClosedM;
	/**
	 * If the mesh is closed in the N direction
	 * */
	private boolean isClosedN;

	/**
	 * handle of the first vertex of the mesh
	 * */
	private DwgHandleReference firstVertexHandle;
	/**
	 * handle of the last vertex of the mesh
	 * */
	private DwgHandleReference lastVertexHandle;
	/**
	 * handle of the seqend of the mesh
	 * */
	private DwgHandleReference seqendHandle;

	/**
	 * Vertices of the mesh
	 * */
	private List vertices;

	/**
	 * Owned Objects Handles of the mesh
	 * */
	private ArrayList ownedObjectsHandles; //For R2004+


	/**
	 * Constructor
	 * */
	public DwgMeshPolyline(int index) {
		super(index);
		ownedObjectsHandles = new ArrayList();
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setCurveType(int curveType) {
		this.curveType = curveType;
	}

	public void setMVerticies(int verticies) {
		this.mVerticies = verticies;
	}

	public void setNVerticies(int verticies) {
		this.nVerticies = verticies;
	}


	public void setFirstVertexHandle(DwgHandleReference handle) {
		this.firstVertexHandle = handle;
	}

	public void setLastVertexHandle(DwgHandleReference handle) {
		this.lastVertexHandle = handle;
	}

	public void setSeqendHandle(DwgHandleReference handle) {
		this.seqendHandle = handle;
	}

	public int getCurveType() {
		return curveType;
	}

	public DwgHandleReference getFirstVertexHandle() {
		return firstVertexHandle;
	}

	public int getFlags() {
		return flags;
	}

	public DwgHandleReference getLastVertexHandle() {
		return lastVertexHandle;
	}


	public int getMVerticies() {
		return mVerticies;
	}


	public int getNVerticies() {
		return nVerticies;
	}

	public DwgHandleReference getSeqendHandle() {
		return seqendHandle;
	}
	public Object clone(){
		DwgMeshPolyline obj = new DwgMeshPolyline(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgMeshPolyline myObj = (DwgMeshPolyline)obj;

		myObj.setCurveType(curveType);
		myObj.setFirstVertexHandle(firstVertexHandle);
		myObj.setFlags(flags);
		myObj.setLastVertexHandle(lastVertexHandle);
		myObj.setMVerticies(mVerticies);
		myObj.setNVerticies(nVerticies);
		myObj.setSeqendHandle(seqendHandle);
	}

	public void calculateGisModel(DwgFile dwgFile) {
	}

	 /*
     * By the moment, we consideer that PFacePolyline
     * is a Polyline3D TODO Implement real conversion
     * from a polyface mesh to a FMap geometry
     * */
	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		//TODO Implementar la conversion de DWG Mesh a
		//FMap (coleccion de poligonos??)
		return null;
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
		return "MeshPolyline";
	}

	public List getVertices() {
		return vertices;
	}

	public void setVertices(List vertices) {
		this.vertices = vertices;
	}

	public boolean isClosedM() {
		return isClosedM;
	}

	public void setClosedM(boolean isClosedM) {
		this.isClosedM = isClosedM;
	}

	public boolean isClosedN() {
		return isClosedN;
	}

	public void setClosedN(boolean isClosedN) {
		this.isClosedN = isClosedN;
	}

	public void setMDensity(int density) {
		this.mVerticies = density;
	}

	public void setNDensity(int density) {
		this.nVerticies = density;
	}

	public void addVertex(IDwgVertex vertex) {
		vertices.add(vertex.getPoint());

	}

	/**
	 * @return Returns the owned objects handles.
	 */
	public ArrayList getOwnedObjectsHandles() {
		return ownedObjectsHandles;
	}
	/**
	 * @param objects The owned objects handles to set.
	 */
	public void setOwnedObjectsHandles(ArrayList handles) {
		this.ownedObjectsHandles = handles;
	}
	/**
	 * Add a handle to the ownedBbjectsHandle vector
	 *
	 * @param handle handle
	 */
	public void addOwnedObjectHandle(DwgHandleReference handle) {
		this.ownedObjectsHandles.add(handle);
	}

	public void transform2Block(double[] bPoint, Point2D insPoint,
			double[] scale, double rot,
			List dwgObjectsWithoutBlocks,
			Map handleObjWithoutBlocks, DwgFile callBack) {

		DwgPolyline3D transformedEntity = null;
		List vertices = this.getVertices();

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


	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM2D;
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.MULTICURVE;
	}

}

