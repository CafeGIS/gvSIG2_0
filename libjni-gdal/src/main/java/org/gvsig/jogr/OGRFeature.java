/**********************************************************************
 * $Id: OGRFeature.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRFeature.java
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

public class OGRFeature extends JNIBase{
	
	private native void FreeOGRFeatureNat(long cPtr);
	private native void dumpReadableNat(long cPtr, String file);
	private native long getDefnRefNat(long cPtr);
	private native int setGeometryDirectlyNat( long cPtr, OGRGeometry geom );//Excepciones
	private native int setGeometryNat( long cPtr, OGRGeometry geom );//Excepciones
	private native long getGeometryRefNat(long cPtr);
	private native long stealGeometryNat(long cPtr);
	private native long cloneFeatureNat(long cPtr);//*
	private native int equalNat( long cPtr, OGRFeature poFeature );
	private native long getFieldDefnRefNat(long cPtr, int iField );
	private native int getFieldIndexNat( long cPtr, String  pszName);
	private native int isFieldSetNat( long cPtr, int iField );
	private native void unsetFieldNat( long cPtr, int iField );
	private native long getRawFieldRefNat( long cPtr, int i );
	private native int getFieldAsIntegerNat( long cPtr, int i );
	private native double getFieldAsDoubleNat( long cPtr, int i );
	private native String getFieldAsStringNat( long cPtr, int i );
	private native int getFieldAsIntegerListNat( long cPtr, int i, int pnCount );
	private native double  getFieldAsDoubleListNat( long cPtr, int i, int pnCount );
	private native String[] getFieldAsStringListNat( long cPtr, int i );
	private native int getFieldAsIntegerNat( long cPtr, String pszFName );
	private native double getFieldAsDoubleNat( long cPtr, String pszFName );
	private native String getFieldAsStringNat( long cPtr, String pszFName );
	private native int getFieldAsIntegerListNat( long cPtr, String pszFName, int pnCount );
	private native double getFieldAsDoubleListNat( long cPtr, String pszFName, int pnCount );
	private native String[] getFieldAsStringListNat( long cPtr, String pszFName );
	private native void setFieldNat( long cPtr, int i, int nValue );
	private native void setFieldNat( long cPtr, int i, double dfValue );
	private native void setFieldNat( long cPtr, int i, String pszValue );
	private native void setFieldNat( long cPtr, int i, int nCount, int panValues );
	private native void setFieldNat( long cPtr, int i, int nCount, double padfValues );
	private native void setFieldNat( long cPtr, int i, String[] papszValues );
	private native void setFieldNat( long cPtr, int i, OGRField puValue );
	private native void setFieldNat( long cPtr, String pszFName, int nValue );
	private native void setFieldNat( long cPtr, String pszFName, double dfValue );
	private native void setFieldNat( long cPtr, String pszFName, String pszValue);
	private native void setFieldNat( long cPtr, String pszFName, int nCount, int panValues );
	private native void setFieldNat( long cPtr, String pszFName, int nCount, double padfValues );
	private native void setFieldNat( long cPtr, String pszFName, String[] papszValues );                    
	private native void setFieldNat( long cPtr, String pszFName, OGRField puValue );
	private native long getFIDNat(long cPtr);
	private native int setFIDNat( long cPtr, long nFID );//Excepciones
	private native int setFromNat( long cPtr, OGRFeature feature, int b);//Excepciones
	private native int remapFieldsNat( long cPtr, OGRFeatureDefn poNewDefn, int panRemapSource );//Excepciones
	private native String getStyleStringNat(long cPtr);
	private native void setStyleStringNat(long cPtr, String style);
	private native void setStyleTableNat(long cPtr, OGRStyleTable poStyleTable);
	private native static OGRFeature createFeatureNat( long cPtr, OGRFeatureDefn feature );
	private native static void destroyFeatureNat( long cPtr, OGRFeature feature );
	
	static int h=0;
	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRFeature de C. 
	 */
	
	public OGRFeature(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * 
	 */
	
	public void dumpReadable(String file)throws OGRException{
		h++;
		if(cPtr == 0)
			throw new OGRException("Error en dumpReadable(). El constructor ha fallado."+h);
		dumpReadableNat(cPtr, file);
		
	}
	
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRFeatureNat(cPtr);
	}
	
	
	/**
	 * 
	 */
	
	public OGRFeatureDefn getDefnRef()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public void setGeometryDirectly( OGRGeometry geom )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public void setGeometry( OGRGeometry geom )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public OGRGeometry getGeometryRef()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public OGRGeometry stealGeometry()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public OGRFeature cloneFeature()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int equal( OGRFeature poFeature )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public int getFieldCount()throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public OGRFieldDefn getFieldDefnRef( int iField )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getFieldIndex( String  pszName)throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public int isFieldSet( int iField )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public void unsetField( int iField )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public OGRField getRawFieldRef( int i )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getFieldAsInteger( int i )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public double getFieldAsDouble( int i )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public String getFieldAsString( int i )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getFieldAsIntegerList( int i, int pnCount )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public double  getFieldAsDoubleList( int i, int pnCount )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public String[] getFieldAsStringList( int i )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getFieldAsInteger( String pszFName )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public double getFieldAsDouble( String pszFName )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public String getFieldAsString( String pszFName )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getFieldAsIntegerList( String pszFName, int pnCount )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public double getFieldAsDoubleList( String pszFName, int pnCount )throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public String[] getFieldAsStringList( String pszFName )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, int nValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, double dfValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, String pszValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, int nCount, int panValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, int nCount, double padfValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, String[] papszValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( int i, OGRField puValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, int nValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, double dfValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, String pszValue)throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, int nCount, int panValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, int nCount, double padfValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, String[] papszValues )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setField( String pszFName, OGRField puValue )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public long getFID()throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public void setFID( long nFID )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public void setFrom( OGRFeature feature, int b)throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public void remapFields( OGRFeatureDefn poNewDefn, int panRemapSource )throws OGRException{//Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public String getStyleString()throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public void setStyleString(String style)throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public void setStyleTable(OGRStyleTable poStyleTable)throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public static OGRFeature  createFeature( OGRFeatureDefn feature )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public static void destroyFeature( OGRFeature feature )throws OGRException{
		
	}
	
	
}