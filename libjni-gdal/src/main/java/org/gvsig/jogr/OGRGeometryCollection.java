/**********************************************************************
 * $Id: OGRGeometryCollection.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRGeometryCollection.java
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
 * Esta clase representa a una fuente de datos
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRGeometryCollection extends JNIBase{
	
	private native void FreeOGRGeometryCollectionNat(long cPtr);
	private native String getGeometryNameNat( long cPtr );
	private native String getGeometryTypeNat( long cPtr );//return OGRwkbGeometryType
	private native OGRGeometry clonePolygonNat( long cPtr );
	private native void emptyNat( long cPtr );
	private native int transformNat( long cPtr,  OGRCoordinateTransformation poCT );//Excepciones
	private native void flattenTo2DNat( long cPtr );
	private native double get_AreaNat( long cPtr );
	private native int centroidNat( long cPtr,  OGRPoint poPoint );
	private native int pointOnSurfaceNat( long cPtr,  OGRPoint poPoint );
	private native int importFromWkbNat( long cPtr,  String wkt, int i );//Excepciones
	private native int exportToWkbNat( long cPtr,  boolean wktborder, String wkt );//Excepciones. 2 param=OGRwkbByteOrder
	private native int importFromWktNat( long cPtr,  String[] wkt );//Excepciones
	private native int exportToWktNat( long cPtr,  String[] ppszDstText );//Excepciones
	private native void getEnvelopeNat( long cPtr,  OGREnvelope psEnvelope );
	private native int equalNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
	private native void addGeometryNat( long cPtr, OGRGeometry geom ); //Excepciones
    private native void addGeometryDirectlyNat( long cPtr, OGRGeometry geom );//Excepciones
    private native void removeGeometryNat( long cPtr, int iIndex, int bDelete );//Excepciones
    private native void closeRingsNat(long cPtr);

	
	/**
	 * Constructor
	 * @param cPtr	direcci�n de memoria al objeto OGRGeometryCollection de C. 
	 */
		
	public OGRGeometryCollection(long cPtr){
		this.cPtr=cPtr;
	}		
		
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRGeometryCollectionNat(cPtr);
	}
	 
	 // Non standard (OGRGeometry).
	
	/**
	 * 
	 */
	
    public String getGeometryName()throws OGRException{
    	return null;
    }
	
	/**
	 * 
	 */

    public String getGeometryType()throws OGRException{//return OGRwkbGeometryType
    	return null;
    }
	
	/**
	 * 
	 */

    public OGRGeometry clonePolygon()throws OGRException{
    	return null;
    }
	
	/**
	 * 
	 */

    public void empty()throws OGRException{
    	
    }
	
	/**
	 * 
	 */

    public void transform( OGRCoordinateTransformation poCT )throws OGRException{//Excepciones
    	
    }
	
	/**
	 * 
	 */

    public void flattenTo2D()throws OGRException{
    	
    }
	
//  ISurface Interface
	
	/**
	 * 
	 */

    public double get_Area()throws OGRException{
    	return 0;
    }
	
	/**
	 * 
	 */

    public int centroid( OGRPoint poPoint )throws OGRException{
    	return 0;
    }
    
    /**
     * 
     */
    
    public int pointOnSurface( OGRPoint poPoint )throws OGRException{
    	return 0;
    }

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

    public void exportToWkb( boolean wktborder, String wkt )throws OGRException{//Excepciones. 1 param=OGRwkbByteOrder
    	
    }
	
	/**
	 * 
	 */

    public void importFromWkt( String[] wkt )throws OGRException{//Excepciones
    	
    }
	
	/**
	 * 
	 */

    public void exportToWkt( String[] ppszDstText )throws OGRException{//Excepciones
    	
    }

    // IGeometry
	
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

    public void getEnvelope( OGREnvelope psEnvelope )throws OGRException{
    	
    }

    // ISpatialRelation
	
	/**
	 * 
	 */

    public int equal( OGRGeometry geom )throws OGRException{//return OGRBoolean
    	return 0;
    }
    
    //  Non standard
    
    /**
     * 
     */
    
    public int addGeometry( OGRGeometry geom )throws OGRException{ //Excepciones
    	return 0;
    }
    
    /**
     * 
     */
    
    public int addGeometryDirectly( OGRGeometry geom )throws OGRException{//Excepciones
    	return 0;
    }
    
    /**
     * 
     */
    
    public int removeGeometry( int iIndex, int bDelete )throws OGRException{//Excepciones
    	return 0;
    }
    
    /**
     * 
     */

    public void closeRings()throws OGRException{
    	
    }
   
}