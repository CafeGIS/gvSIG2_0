/**********************************************************************
 * $Id: OGRSFDriver.java 15690 2007-10-31 10:28:41Z nbrodin $
 *
 * Name:     OGRSFDriver.java
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

public class OGRSFDriver extends JNIBase{
	
	public native String getNameNat(long cPtr);
	private native long openNat( long cPtr, String pszName, boolean bUpdate );
	private native int testCapabilityNat( long cPtr,String cap );
	private native long createDataSourceNat( long cPtr, String pszName);                                             
    private native int deleteDataSourceNat( long cPtr,String pszName );//Excepciones
    private native long copyDataSourceNat( long cPtr, 
    											long poSrcDS,
												String pszNewName,
												String[] papszOptions);
	
	public OGRSFDriver(long cPtr){
		this.cPtr=cPtr;
	}

	/**
	 * Obtiene el nombre del driver
	 * @throws OGRException
	 * @return Nombre del driver
	 */
	
	public String getName()throws OGRException {		    
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		String name = getNameNat(cPtr);
		
		if(name == null)
			throw new OGRException("Error en getName(). No se ha podido obtener el nombre del driver.");
		return name;
	}
	
	
	/**
	 * 
	 */
	
	public OGRDataSource open( String pszName, boolean bUpdate )throws OGRException {
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		long ptr_ds = openNat(cPtr, pszName, bUpdate);
		
		if(ptr_ds == 0)
			throw new OGRException("Error en open(). No se ha podido obtener un dataset valido.");
		
		return new OGRDataSource(ptr_ds);
		
	}

	/**
	 * @param capability	Capacidad del driver a testear
	 * <UL>
	 *   <LI>"RandomRead"</LI>
     *	 <LI>"SequentialWrite"</LI>
     *   <LI>"RandomWrite"</LI>
     *   <LI>"FastSpatialFilter"</LI>
     *   <LI>"FastFeatureCount"</LI>
     *   <LI>"FastGetExtent"</LI>
     *   <LI>"CreateField"</LI>
     *   <LI>"Transactions"</LI>
     *   <LI>"DeleteFeature"</LI>
     *   <LI>"CreateLayer"</LI>
     *   <LI>"DeleteLayer"</LI>
     *   <LI>"CreateDataSource"</LI>
     *   <LI>"DeleteDataSource"</LI>
     * </UL>
	 */
	
	public boolean testCapability( String capability )throws OGRException {	
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		if(capability == null || capability.equals(""))
			throw new OGRException("Error en testCapability(). El parámetro pasado a la función no es valido.");
		
		int res=-1;
		res = testCapabilityNat(cPtr, capability);
		
		if(res == 0)
			throw new OGRException("Error en testCapability(). No se ha podido obtener un valor de retorno valido.");
		
		if(res != 0)return true;
		return false;
	}

	/**
	 * 
	 */
	
	public OGRDataSource createDataSource( String pszName )throws OGRException { 	
		if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
		
		if(pszName == null || pszName.equals(""))
			throw new OGRException("Error en createDataSource(). El parámetro pasado a la función no es valido.");
				
		long ptr_ds = createDataSourceNat(cPtr, pszName);
		
		if(ptr_ds<=0)
			throw new OGRException("Error en createDataSource(). No se ha podido obtener un dataset valido.");
		
		return new OGRDataSource(ptr_ds);
	}

	/**
	 * 
	 */
	
    public void deleteDataSource( String pszName )throws OGRException {//Excepciones
    	if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
    	
    	if(pszName == null || pszName.equals(""))
			throw new OGRException("Error en deleteDataSource(). El parámetro pasado a la función no es valido.");
		
    	int ogrerr = deleteDataSourceNat(cPtr, pszName);   
    	throwException(ogrerr, "Error en deleteDataSource().");
   
    }

	/**
	 * 
	 */
	
    public OGRDataSource copyDataSource( OGRDataSource poSrcDS,
	                                           String pszNewName,
	                                           String[] papszOptions)throws OGRException {
    	if(poSrcDS == null || pszNewName == null || pszNewName.equals(""))
			throw new OGRException("Error en deleteDataSource().Algún parámetro pasado a la función no es valido.");
		
    	if(poSrcDS.getPtro() == 0)
			throw new OGRException("Fallo al acceder al dato fuente.");
    	
    	if(cPtr == 0)
			throw new OGRException("Fallo al acceder al dato.");
    	
    	long ptr_ds = copyDataSourceNat(cPtr, poSrcDS.getPtro(), pszNewName, papszOptions);
		
		if(ptr_ds == 0)
			throw new OGRException("Error en copyDataSource(). No se ha podido obtener un dataset valido.");
		
		return new OGRDataSource(ptr_ds);

    }
    

}