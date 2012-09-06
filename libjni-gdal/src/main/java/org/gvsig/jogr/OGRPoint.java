/**********************************************************************
 * $Id: OGRPoint.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRPoint.java
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

public class OGRPoint extends JNIBase{
	
	private native void FreeOGRPointNat(long cPtr);
   	private native void emptyNat( long cPtr );
    private native long cloneNat( long cPtr );
    private native void getEnvelopeNat( long cPtr,  OGREnvelope psEnvelope );
    private native int importFromWkbNat( long cPtr,  String wkt, int i );//Excepciones
    private native int exportToWkbNat( long cPtr,  boolean ogrwkbByteOrder , String wkt );//Excepciones
    private native int importFromWktNat( long cPtr,  String[] ppszInput );//Excepciones
    private native int exportToWktNat( long cPtr,  String[] ppszDstText );//Excepciones
    private native double getXNat( long cPtr );
    private native double getYNat( long cPtr );
    private native double getZNat( long cPtr );
    private native void setXNat( long cPtr,  double xIn );
    private native void setYNat( long cPtr,  double yIn );
    private native void setZNat( long cPtr,  double zIn );
    private native int equalNat( long cPtr,  OGRGeometry geom );//return OGRBoolean
    private native String getGeometryNameNat( long cPtr );
    private native int getGeometryTypeNat( long cPtr );//return OGRwkbGeometryType
    private native int transformNat( long cPtr,  OGRCoordinateTransformation poCT );
    private native void flattenTo2DNat( long cPtr );
	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRPoint de C. 
	 */
		
	public OGRPoint(long cPtr){
		this.cPtr = cPtr;
	}		
		
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
			
		FreeOGRPointNat(cPtr);
	}
	 
	
	/**
	 * 
	 */
	
    public OGRPoint( double x, double y, double z)throws OGRException{
    	
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
	
    public OGRGeometry clonePoint()throws OGRException{
    	return null;	
    }

	/**
	 * 
	 */
	
    public void getEnvelope( OGREnvelope psEnvelope )throws OGRException{
    	
    }


    // IPoint
    
    /**
     * 
     */
    
    public double getX()throws OGRException{
    	return 0;
    }

    /**
     * 
     */
    
    public double getY()throws OGRException{
    	return 0;
    }

    /**
     * 
     */
    
    public double getZ()throws OGRException{
    	return 0;
    }
	
	// Non standard

    /**
     * 
     */
    
    public void setX( double xIn )throws OGRException{
    	
    }

    /**
     * 
     */
    
    public void setY( double yIn )throws OGRException{
    	
    }

    /**
     * 
     */
    
    public void setZ( double zIn )throws OGRException{
    	
    }

    /**
     * 
     */
    
	
	// ISpatialRelation

    /**
     * 
     */
    
    public int equal( OGRGeometry geom )throws OGRException{
    	return 0;
    }
	
	// Non standard from OGRGeometry

    /**
     * 
     */
    
    public String getGeometryName()throws OGRException{
    	return null;
    }

    /**
     * 
     */
    
    public int getGeometryType()throws OGRException{ //return OGRwkbGeometryType
    	return 0;
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
    
	
}