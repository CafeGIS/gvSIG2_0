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
 * $Id: DwgPFacePolyline.java 29050 2009-05-28 12:21:48Z jmvivo $
 * $Log$
 * Revision 1.3.2.1  2007-03-21 19:49:16  azabala
 * implementation of dwg 12, 13, 14.
 *
 * Revision 1.3  2007/03/20 19:57:08  azabala
 * source code cleaning
 *
 * Revision 1.2  2007/03/06 19:39:38  azabala
 * Changes to adapt dwg 12 to general architecture
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
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.aggregate.MultiSurface;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This DWG entity is a "Polyface Mesh".
 *
 */

/*
 * azabala DXF 12 spec says: "If the Vertex defines a face of the mesh, its
 * Vertex flags (70) group has the 128 bit set but not the 64 bit. The 10, 20,
 * and 30 (location) groups of the face entity are irrelevant and are always
 * written as zero in a DXF file.
 *
 * The vertex indexes that define the mesh are given by 71, 72, 73, and 74
 * groups, the values of which are integers specifying one of the previously
 * defined vertices by index.
 *
 * If the index is negative, the edge that begins with that vertex is invisible.
 *
 * The first zero vertex marks the end of the vertices of the face.
 *
 * Since the 71 through 74 groups are optional fields with default values of
 * zero, they are present in DXF only if nonzero."
 *
 * But DWG 12 spec doesnt say anything about 71,72,73 and 74 fields for VERTEX.
 */
public class DwgPFacePolyline extends DwgObject
	implements IDwgPolyline, IDwg2FMap, IDwgBlockMember{

	private static Logger logger = LoggerFactory.getLogger(DwgPFacePolyline.class.getName());

	public static final int NUM_VERTEX_OF_FACE = 4;


	/**
	 * number of vertices in the mesh (as it is readed from the dwg file... very
	 * often is wrong)
	 */
	private int vertexCount;
	/**
	 * number of faces of the mesh (as it is readed from the dwg file... very
	 * often is wrong)
	 */
	private int faceCount;


	private DwgHandleReference firstVertexHandle;
	private DwgHandleReference lastVertexHandle;
	private DwgHandleReference seqendHandle;

	/**
	 * Collection of all the vertices of the polyface mesh
	 */
	private List vertices;
	/**
	 * Collection of all the faces of the polyface mesh, whose vertices are in
	 * vertices List.
	 */
	private List faces;

	private ArrayList ownedObjectsHandles; //For R2004+


	/**
	 * Constructor
	 */
	public DwgPFacePolyline(int index) {
		super(index);
		vertices = new ArrayList();
		faces = new ArrayList();
		ownedObjectsHandles = new ArrayList();
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public void setFaceCount(int faceCount) {
		this.faceCount = faceCount;
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

	public int getFaceCount() {
		return faceCount;
	}

	public DwgHandleReference getFirstVertexHandle() {
		return firstVertexHandle;
	}

	public DwgHandleReference getLastVertexHandle() {
		return lastVertexHandle;
	}

	public DwgHandleReference getSeqendHandle() {
		return seqendHandle;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Object clone(){
		DwgPFacePolyline obj = new DwgPFacePolyline(index);
		this.fill(obj);
		return obj;
	}

	protected void fill(DwgObject obj){
		super.fill(obj);
		DwgPFacePolyline myObj = (DwgPFacePolyline)obj;
		myObj.setFaceCount(faceCount);
		myObj.setFirstVertexHandle(firstVertexHandle);
		myObj.setLastVertexHandle(lastVertexHandle);
		myObj.setSeqendHandle(seqendHandle);
		myObj.setVertexCount(vertexCount);
		myObj.setVertices(this.vertices);
		myObj.setFaces(this.faces);

	}

	public void setFaces(List faces) {
		this.faces = faces;
	}

	public List getFaces(){
		return this.faces;
	}

	public List getVertices() {
		return vertices;
	}

	public void setVertices(List vertices) {
		this.vertices = vertices;
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

	public void calculateGisModel(DwgFile dwgFile) {
	// In DWG 13, 14 and 2000 version, a Polyface mesh has
    // handles references to its vertices.
	// In DWG 12 vertices are after the entity
	 if(firstVertexHandle != null && lastVertexHandle != null){
		 	DwgObject first = dwgFile.getDwgObjectFromHandle(firstVertexHandle.getOffset());
			DwgObject last = dwgFile.getDwgObjectFromHandle(lastVertexHandle.getOffset());
			if(first == null || last == null){
				logger.warn("DwgPFacePolyline con vertices inicial o final a null");
				return;
			}

			if(!(first instanceof IDwgVertex)){
				logger.warn("El primer vertice de PFacePolyline es "+
								first.getClass().getName());
				return;
			}

			if(!(last instanceof IDwgVertex)){
				logger.warn("El ultimo vertice de PFacePolyline es "+
						last.getClass().getName());
				return;
			}

			 int firstObjIdx = dwgFile.getIndexOf(first);
			 int lastObjIdx =  dwgFile.getIndexOf(last);
			 if(firstObjIdx == -1 || lastObjIdx == -1){
				 logger.warn("Calculate GIS Model: Problemas en "+
						 "la localizacion de vertices: primero ="
						 	+firstObjIdx+
						 	",Ultimo ="+lastObjIdx);
				 return;
			 }
			 for(int i = firstObjIdx; i <= lastObjIdx; i++){
				 DwgObject obj = dwgFile.getDwgObject(i);
				 if(obj instanceof IDwgVertex){
				 	this.addVertex((IDwgVertex)obj);
				 }else if(obj instanceof DwgSeqend){
				 	// TODO SEE IF BREAK THE ITERATION
				 	break;
				 }else{
					 logger.warn("Encontrado "+obj.getClass().getName()+" en la lista de vertices de Polyline3D");
				 }
			 }// for

		} else {
			logger.warn("Encontrada polilínea sin puntos ...");
			// TODO: No se debe mandar nunca una polil�nea sin puntos, si
			// esto
			// ocurre es porque existe un error que hay que corregir ...
		}

	}


	public Geometry toFMapGeometry(boolean is3DFile) throws CreateGeometryException {
		GeometryManager gMan = GeometryLocator.getGeometryManager();

		int subType = getGeometrySubType(is3DFile);
		MultiSurface solution = (MultiSurface) gMan.create(getGeometryType(),
				subType);
		if(vertices != null && faces != null){
			if(vertices.size() == 0 || faces.size() == 0){
				return gMan.createNullGeometry(subType);
			}
			int numFaces = faces.size();
			for(int i = 0; i < numFaces; i++){
				DwgVertexPFaceFace face = (DwgVertexPFaceFace) faces.get(i);
				int[] verticesId = face.getVerticesidx();
				ArrayList pts = new ArrayList();
				boolean lastWasInvisible = true;
				for(int j = 0; j < NUM_VERTEX_OF_FACE; j++){
					if(verticesId[j] > 0){
	                    if(lastWasInvisible){
	                        pts.clear();
	                        lastWasInvisible = false;
	                    }
	                    // the index is 1-based
	                    try{
	                    pts.add(vertices.get(verticesId[j] -1));
	                    }catch(Throwable t){
	                    	t.printStackTrace();
	                    }

	                } else if(verticesId[j] < 0 && !lastWasInvisible){
	                    lastWasInvisible = true;
	                    pts.add(vertices.get( (verticesId[j] * -1) -1));
	                }
				}// for j

				solution.addSurface(FMapUtil.ptsToPolygon(pts, subType));


// if(!lastWasInvisible){
// if(vertex[nrV] < 0){
// line.addPoint(knot[-vertex[nrV] - 1]);
// } else{
// line.addPoint(knot[vertex[nrV] - 1]);
// }
// set.addDrawable(line);
// }

			}// for i
		}
		return solution;

	}
	/*
	 * (non-Javadoc)
	 *
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
		return "PFacePolyline";
	}

	public void addVertex(IDwgVertex vertex) {
		// here we save the vertex (not the coordinate) because
		// this Polyline could have two kind of vertex (pface and pfaceface)
		if(vertex instanceof DwgVertexPFaceFace) {
			faces.add(vertex);
		} else {
			vertices.add(vertex);
		}
	}

	public void dump(){
		System.out.println("<PFacePolyline vertexCount="+vertexCount+" faceCount"+faceCount+">");
		int numRealVertex = 1;
		for(int i = 0; i < vertices.size(); i++){
			IDwgVertex vertex = (IDwgVertex) vertices.get(i);
			double[] point = vertex.getPoint();
			System.out.println("<Vertex idx="+numRealVertex+" class="+vertex.getClass().getName()+" x="+point[0]+" y="+point[1]+" z="+point[2]+" >");
			numRealVertex++;
		}

		for(int i = 0; i < faces.size(); i++){
			IDwgVertex vertex = (IDwgVertex) faces.get(i);
			if(vertex instanceof DwgVertexPFaceFace){
				int[] point = ((DwgVertexPFaceFace)vertex).getVerticesidx();
				System.out.println("<Vertex class="+vertex.getClass().getName()+" 1="+point[0]+" 2="+point[1]+" 3="+point[2]+" 4="+ point[3]+">");
			}
		}
		System.out.println("</PFacePolyline>");
	}

	public void transform2Block(double[] bPoint, Point2D insPoint, double[] scale, double rot, List dwgObjectsWithoutBlocks, Map handle_objectsWithoutBlocks, DwgFile callBack) {
		DwgPFacePolyline transformedEntity = null;
		List vertices = this.getVertices();
		if (vertices != null) {
			if(vertices.size() == 0) {
				return;
			}
		    List transformedVertices = new ArrayList();
			for (int i=0;i < vertices.size();i++) {
				double[] pointAux = null;
				IDwgVertex vertex = (IDwgVertex) vertices.get(i);
			    if(!(vertex instanceof DwgVertexPFaceFace)){
			    	// DwgVertexPFaceFace hasnt coordinates, instead
			    	// it has indices in the vertex array
					double[] point = vertex.getPoint();
				    pointAux = new double[]{point[0] - bPoint[0],
				    		point[1] - bPoint[1], point[2] - bPoint[2]};

					double laX = insPoint.getX() + ((pointAux[0] * scale[0])*Math.cos(rot) + (pointAux[1]*scale[1])*(-1)*Math.sin(rot));
					double laY = insPoint.getY() + ((pointAux[0]*scale[0])*Math.sin(rot) + (pointAux[1]*scale[1])*Math.cos(rot));
					double laZ = pointAux[2];
					vertex.setPoint(new double[]{laX, laY, laZ});
					transformedVertices.add(vertex);
				}
			}// for
			transformedEntity = (DwgPFacePolyline)this.clone();
			fill(transformedEntity);
			dwgObjectsWithoutBlocks.add(transformedEntity);
			handle_objectsWithoutBlocks.put(new Integer(transformedEntity.getHandle().getOffset()), transformedEntity);
		}// if
	}

	public int getGeometrySubType(boolean is3DFile) {
		if (is3DFile) {
			return FMapUtil.SUBTYPE_GEOM3D;
		} else {
			return FMapUtil.SUBTYPE_GEOM2D;
		}
	}

	public int getGeometryType() {
		return Geometry.TYPES.MULTISURFACE;
	}

}

