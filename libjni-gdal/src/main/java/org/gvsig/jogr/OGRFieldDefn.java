/**********************************************************************
 * $Id: OGRFieldDefn.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRFieldDefn.java
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

public class OGRFieldDefn extends JNIBase{
	
	private native void FreeOGRFieldDefnNat(long cPtr);
	private native int getTypeNat(long cPtr);
	private native String getNameRefNat(long cPtr);
	private native String getFieldTypeNameNat(long cPtr, int tipo);
	
	private native void setName( long cPtr,  String name );
	private native void setType( long cPtr,  OGRFieldType eTypeIn );
	private native int getJustify( long cPtr ); //return OGRJustification
	private native void setJustify( long cPtr,  String justification );
	private native void setWidth( long cPtr,  int nWidthIn );
	private native void setPrecision( long cPtr,  int nPrecisionIn );
	private native void set( long cPtr,  String s, OGRFieldType ftipe, int i, int j,String justification );
	private native void setDefault( long cPtr,  OGRField field );
	private native long getDefaultRef( long cPtr );

	
	/**
	* Constructor
	* @param cPtr	dirección de memoria al objeto OGRFieldDefn de C. 
	*/
	
	public OGRFieldDefn(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * 
	 */
	
	public OGRFieldType getType()throws OGRException{
		
		OGRFieldType ftipo ;
		if(cPtr == 0)
			throw new OGRException("Error en getType(). El constructor ha fallado.");
		
		int tipo = getTypeNat(cPtr);
		if(tipo >= 0){
			ftipo = new OGRFieldType();
			ftipo.setType(tipo);
		}
		else 
			throw new OGRException("Error en getType(). No se ha podido obtener un tipo valido.");
		
		return ftipo;
	}
	
	/**
	 * 
	 */
	
	public String getNameRef()throws OGRException{
		
		if(cPtr == 0)
			throw new OGRException("Error en getNameRef(). El constructor ha fallado.");
		
		String ref = getNameRefNat(cPtr);
		if(ref == null)
			throw new OGRException("Error en getNameRef(). No se ha podido obtener un nombre de referencia valido.");
		
		else return ref;	
		
	}
	
	/**
	 * 
	 */
	
	public String getFieldTypeName(OGRFieldType tipo)throws OGRException{
		
		if(cPtr == 0)
			throw new OGRException("Error en getFieldTypeName(). El constructor ha fallado.");
		
		String ref = getFieldTypeNameNat(cPtr,tipo.getType());
		
		if(ref == null)
			throw new OGRException("Error en getFieldTypeName(). No se ha podido obtener un nombre de tipo de campo valido.");
		
		else return ref;
	
	}
	
	/**
	 * 
	 */
	
	public int getWidth()throws OGRException{
		
		String msg1="Error en getWidth. El constructor ha fallado.";
		String msg2="Error obteniendo el ancho";
		return baseSimpleFunctions(4,msg1,msg2);
	}
	
	/**
	 * 
	 */
	
	public int getPrecision()throws OGRException{
		
		String msg1="Error en getPrecision. El constructor ha fallado.";
		String msg2="Error obteniendo la precisión";
		return baseSimpleFunctions(5,msg1,msg2);
	}
	
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRFieldDefnNat(cPtr);
	}
	
	
    /**
     * 
     */
	
	public void setName( String name )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public void setType( OGRFieldType eTypeIn )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public int getJustify()throws OGRException{	//return justification
		return 0;
	}
	
	/**
     * 
     */
	
	public void setJustify( String justification )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public void setWidth( int nWidthIn )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public void setPrecision( int nPrecisionIn )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public void set( String s, OGRFieldType ftipe, int i, int j,String justification )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public void setDefault( OGRField field )throws OGRException{
		
	}
	
	/**
     * 
     */
	
	public OGRField getDefaultRef()throws OGRException{
		return null;
	}
	
	


}