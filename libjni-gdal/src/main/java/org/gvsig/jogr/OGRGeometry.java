/**********************************************************************
 * $Id: OGRGeometry.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRGeometry.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:   
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
/*Copyright (C) 2004  Nacho Brodin <brodin_ign@gva.es>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gvsig.jogr;


/** 
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRGeometry extends JNIBase{
	
	private native void FreeOGRGeometry( long cPtr );
    private native int getCoordinateDimensionNat( long cPtr );
    private native int isEmptyNat( long cPtr ); //return OGRBoolean
    private native void emptyNat( long cPtr );
    private native long cloneNat( long cPtr );
    private native void getEnvelopeNat( long cPtr,  OGREnvelope psEnvelope );
    private native void importFromWkbNat( long cPtr,  String wkt, int i );//Excepciones
    private native void exportToWkbNat( long cPtr,  boolean ogrwkbByteOrder , String wkt );//Excepciones
    private native void importFromWktNat( long cPtr,  String[] ppszInput );//Excepciones
    private native void exportToWktNat( long cPtr,  String[] ppszDstText );//Excepciones
    private native String getGeometryTypeNat( long cPtr );//Return OGRwkbGeometryType
    private native String getGeometryNameNat( long cPtr ); 
    private native void dumpReadableNat( long cPtr,  String file, String s );
    private native void flattenTo2DNat( long cPtr );
    private native String exportToGMLNat( long cPtr );
    private native void closeRingsNat( long cPtr );
    private native void assignSpatialReferenceNat( long cPtr,  OGRSpatialReference poSR );
    private native long getSpatialReferenceNat( long cPtr );
    private native void transformNat( long cPtr,  OGRCoordinateTransformation poCT ); //Excepciones
    private native int transformToNat( long cPtr,  OGRSpatialReference poSR ); //Excepciones
    private native int intersectNat( long cPtr,  OGRGeometry geom ); //return OGRBoolean
    private native int equalNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
    private native int disjointNat( long cPtr,  OGRGeometry geom ); //return OGRBoolean
    private native int touchesNat( long cPtr,  OGRGeometry geom ); //return OGRBoolean
    private native int crossesNat( long cPtr,  OGRGeometry geom ); //return OGRBoolean
    private native int withinNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
    private native int containsNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
    private native int overlapsNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
    private native long getBoundaryNat( long cPtr );
    private native double distanceNat( long cPtr,  OGRGeometry geom );
    private native long convexHullNat( long cPtr );
    private native long bufferNat( long cPtr,  double dfDist, int nQuadSegs ); 
    private native long intersectionNat( long cPtr,  OGRGeometry geom);
    private native long unionNat( long cPtr,  OGRGeometry geom );
    private native long differenceNat( long cPtr,  OGRGeometry geom );
    private native long symmetricDifferenceNat( long cPtr,  OGRGeometry geom );

	
    public OGRGeometry(){}
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRGeometry de C. 
	 */
	
	public OGRGeometry(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRGeometry(cPtr);
	}
	
	/**
	 * 
	 */
	
    public int getDimension()throws OGRException{
    	return 0;
    }

	/**
	 * 
	 */
	
    public int getCoordinateDimension()throws OGRException{
    	return 0;
    }

	/**
	 * 
	 */
	
    public int isEmpty()throws OGRException{ //return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int isSimple()throws OGRException{
    	return 0;
    }

	/**
	 * 
	 */
	
    public void empty()throws OGRException{
    	
    }

	/**
	 * 
	 */
	
    public OGRGeometry cloneGeometry()throws OGRException{
    	return null;	
    }

	/**
	 * 
	 */
	
    public void getEnvelope( OGREnvelope psEnvelope )throws OGRException{
    	
    }

	/**
	 * 
	 */
	

    // IWks Interface

	/**
	 * 
	 */
	
    public int wkbSize()throws OGRException{
    	return 0;
    }

	/**
	 * 
	 */
	
    public void importFromWkb( String wkt, int i )throws OGRException{//Excepciones
    	
    }

	/**
	 * 
	 */
	
    public void exportToWkb( boolean ogrwkbByteOrder , String wkt )throws OGRException{//Excepciones
    	
    }

	/**
	 * 
	 */
	
    public void importFromWkt( String[] ppszInput )throws OGRException{//Excepciones
    	
    }

	/**
	 * 
	 */
	
    public void exportToWkt( String[] ppszDstText )throws OGRException{//Excepciones
    	
    }

	/**
	 * 
	 */
	

    // non-standard

	/**
	 * 
	 */
	
    public String getGeometryType()throws OGRException{//Return OGRwkbGeometryType
    	return null;
    }

	/**
	 * 
	 */
	
    public String getGeometryName() throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public void dumpReadable( String file, String s )throws OGRException{
    	
    }

	/**
	 * 
	 */
	
    public void flattenTo2D()throws OGRException{
    	
    }

	/**
	 * 
	 */
	
    public String exportToGML()throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public void closeRings()throws OGRException{
    	
    }

	/**
	 * 
	 */
	
    public void assignSpatialReference( OGRSpatialReference poSR )throws OGRException{
    	
    }

	/**
	 * 
	 */
	
    public OGRSpatialReference getSpatialReference( )throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public void transform( OGRCoordinateTransformation poCT )throws OGRException{ //Excepciones
    	
    }

	/**
	 * 
	 */
	
    public void transformTo( OGRSpatialReference poSR )throws OGRException{ //Excepiones
   
    }

    // ISpatialRelation

	/**
	 * 
	 */
	
    public int intersect( OGRGeometry geom )throws OGRException{ //return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int equal( OGRGeometry geom )throws OGRException{//return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int disjoint( OGRGeometry geom )throws OGRException{ //return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int touches( OGRGeometry geom )throws OGRException{ //return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int crosses( OGRGeometry geom )throws OGRException{ //return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int within( OGRGeometry geom )throws OGRException{//return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int contains( OGRGeometry geom )throws OGRException{//return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public int overlaps( OGRGeometry geom )throws OGRException{//return OGRBoolean
    	return 0;
    }

	/**
	 * 
	 */
	
    public OGRGeometry getBoundary()throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public double distance( OGRGeometry geom )throws OGRException{
    	return 0;
    }

	/**
	 * 
	 */
	
    public OGRGeometry convexHull()throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public OGRGeometry buffer( double dfDist, int nQuadSegs )throws OGRException{ 
    	return null;
    }

	/**
	 * 
	 */
	
    public OGRGeometry intersection( OGRGeometry geom) throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public OGRGeometry union( OGRGeometry geom )throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public OGRGeometry difference( OGRGeometry geom )throws OGRException{
    	return null;
    }

	/**
	 * 
	 */
	
    public OGRGeometry symmetricDifference( OGRGeometry geom )throws OGRException{
    	return null;
    }

}