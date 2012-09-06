/**********************************************************************
 * $Id: OGRDataSource.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRDataSource.java
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

public class OGRDataSource extends JNIBase {
	
	private native String getNameNat(long cPtr);
	private native long getLayerNat(long cPtr, int i);
	private native void FreeOGRDataSource(long cPtr);
	
	private native long getLayerByNameNat(long cPtr, String name);
	private native int deleteLayerNat(long cPtr, int iLayer); //Excepciones
	private native int testCapabilityNat(long cPtr,  String odr );
	private native long createLayerNat(long cPtr, String pszName,
	                                 long poSpatialRef,
	                                 String eGType, //OGRwkbGeometryType OGRFeatureDefn
	                                 String[] papszOptions);
	private native long copyLayerNat(long cPtr,  long poSrcLayer,
	                                String pszNewName,
	                                String[] papszOptions);
	private native long getStyleTableNat(long cPtr);
	private native long executeSQLNat(long cPtr,  String pszStatement,
	                                long poSpatialFilter,
	                                String pszDialect );
	private native void releaseResultSetNat(long cPtr,  long poResultsSet );
	private native int syncToDiskNat(long cPtr); //Excepciones

	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRDataSource de C. 
	 */
	
	public OGRDataSource(long cPtr){
		this.cPtr=cPtr;
	}
		
	/**
	 * Obtiene el nombre del datasource
	 * @throws OGRException
	 * @return Nombre del datasource
	 */
	
	public String getName()throws OGRException{
		
		if(cPtr == 0)
			throw new OGRException("Error en getName(). El constructor ha fallado.");
		    
		String name = getNameNat(cPtr);
		
		if(name==null)
			throw new OGRException("Error en getName(). No se ha podido obtener el nombre del datasource.");
		
		return name;
	}
	
	/**
	 * Obtiene el número de capas
	 * @throws OGRException
	 * @return Número de capas
	 */
	
	 public int getLayerCount()throws OGRException{
			
		String msg1="Error en getLayerCount. El constructor no tuvo exito.";
		String msg2="Error en el conteo de capas.";
		return baseSimpleFunctions(1,msg1,msg2);
	 }
	 
	 /**
	 * Obtiene la capa indicada por el índice
	 * @throws OGRException
	 * @return una capa
	 */
			
	 public OGRLayer getLayer(int i)throws OGRException{
				
	 	if(cPtr == 0)
			throw new OGRException("Error en getLayer(). El constructor no tuvo exito");
		 
	 	if(i<0)
			throw new OGRException("Error en getLayer(). Parámetro no valido");
	 	
		long layer = getLayerNat(cPtr, i);
		
		if(layer==0)
			throw new OGRException("Error en getLayer(). No se ha podido obtener la capa indicada.");
						
		return new OGRLayer(layer);
			
	 }

	 
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRFailureException {
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRDataSource(cPtr);
	}
	 
	/**
	 * 
	 */
	
	public OGRLayer getLayerByName(String name)throws OGRException{
		
		if(cPtr == 0)
			throw new OGRException("Error en getLayerByName(). El constructor no tuvo exito");
		
		if(name==null || name.equals(""))
			throw new OGRException("Error en getLayerByName(). Parámetro no valido");
	 	
		long ptr_name = getLayerByNameNat(cPtr, name);
		
		if(ptr_name==0)
			throw new OGRException("Error en getLayerByName(). No se ha podido obtener un puntero a un OGRLayer valido.");
		
		return new OGRLayer(ptr_name);
		    
    }
    
	/**
	 * 
	 */
	
    public void deleteLayer(int iLayer)throws OGRException{ //Excepciones
    	
    	if(cPtr == 0)
			throw new OGRException("Error en deleteLayer(). El constructor no tuvo exito");
		
    	if(iLayer<0)
			throw new OGRException("Error en deleteLayer(). Parámetro no valido");
	 	
    	int ogrerr = deleteLayerNat(cPtr, iLayer);
    	throwException(ogrerr, "Error en deleteLayer()");
    }

    /**
	 * 
	 */
    
    public int testCapability( String odr )throws OGRException{
    	
    	if(cPtr == 0)
			throw new OGRException("Error en testCapability(). El constructor no tuvo exito");
		
    	if(odr==null || odr.equals(""))
			throw new OGRException("Error en testCapability(). Parámetro no valido");
	 	
    	int res = 0;
		res = testCapabilityNat(cPtr, odr);
		
		if(res == 0)
			throw new OGRException("Error en testCapability(). No se ha podido obtener un valor de retorno valido.");

		return res;
    }

    /**
	 * 
	 */
    
    public OGRLayer createLayer(String pszName,
                                     OGRSpatialReference poSpatialRef,
                                     String eGType, //OGRwkbGeometryType OGRFeatureDefn
                                     String[] papszOptions)throws OGRException{
    	long ptro_sr = 0;
    	
    	if(cPtr == 0)
			throw new OGRException("Error en createLayer(). El constructor no tuvo exito");
    	
    	if(pszName==null || pszName.equals(""))
			throw new OGRException("Error en createLayer(). Parámetros en la función no validos.");
    	    	
    	if(poSpatialRef == null || poSpatialRef.getPtro() == 0)
    		throw new OGRException("Error en createLayer(). Parámetro OGRSpatialReference no valido.");
    	
    	ptro_sr = poSpatialRef.getPtro();
    	
    	long ptr_layer = createLayerNat(cPtr, pszName, ptro_sr, eGType, papszOptions);
    	if(ptr_layer == 0)
			throw new OGRException("Error en createLayer(). No se ha podido obtener una referencia valida a un OGRLayer.");
    	
    	return new OGRLayer(ptr_layer);		    
    }
    
    /**
	 * 
	 */
    
    public OGRLayer copyLayer( OGRLayer poSrcLayer,
                                    String pszNewName,
                                    String[] papszOptions)throws OGRException{
    	if(cPtr == 0)
			throw new OGRException("Error en copyLayer(). El constructor no tuvo exito");
		
    	if(pszNewName == null || pszNewName.equals("") || poSrcLayer == null)
			throw new OGRException("Error en copyLayer(). Parámetros en la función no validos.");
    	
    	if(poSrcLayer.getPtro() == 0)
    		throw new OGRException("Error en copyLayer(). Referencia del parámetro OGRSpatialReference invalida.");
    	
    	long ptr_layer = copyLayerNat(cPtr, poSrcLayer.getPtro(), pszNewName, papszOptions);
    	if(ptr_layer == 0)
			throw new OGRException("Error en copyLayer(). No se ha podido obtener una referencia valida a un OGRLayer.");
    	
    	return new OGRLayer(ptr_layer);
    	     	
    }
    
    /**
	 * 
	 */
    
    public OGRStyleTable getStyleTable()throws OGRException{
    	
    	if(cPtr == 0)
			throw new OGRException("Error en getStyleTable(). El constructor no tuvo exito");
		
    	long ptr_styletable = getStyleTableNat(cPtr);
    	if(ptr_styletable==0)
			throw new OGRException("Error en getStyleTable(). No se ha podido obtener una referencia valida a un OGRStyleTable.");
    	
    	return new OGRStyleTable(ptr_styletable);
    	
    }

    /**
	 * 
	 */
    
    public OGRLayer executeSQL( String pszStatement,
                                    OGRGeometry poSpatialFilter,
                                    String pszDialect )throws OGRException{
    	
    	if(cPtr == 0)
			throw new OGRException("Error en executeSQL(). El constructor no tuvo exito");
		
    	if(pszStatement==null || pszStatement.equals("") || poSpatialFilter==null)
			throw new OGRException("Error en executeSQL(). Parámetros en la función no validos.");
    	
    	if(poSpatialFilter.getPtro() == 0)
    		throw new OGRException("Error en executeSQL(). Referencia del parámetro OGRGeometry invalida.");
    	
    	long ptr_layer = executeSQLNat(cPtr, pszStatement, poSpatialFilter.getPtro(), pszDialect);
    	if(ptr_layer == 0)
			throw new OGRException("Error en executeSQL(). No se ha podido obtener una referencia valida a un OGRLayer.");
    	
    	return new OGRLayer(ptr_layer);
    	
    }
    
    /**
	 * 
	 */
    
    public void releaseResultSet( OGRLayer poResultsSet )throws OGRException{
    	
    	if(cPtr == 0)
			throw new OGRException("Error en poResultsSet(). El constructor no tuvo exito");
		
    	if(poResultsSet == null)
			throw new OGRException("Error en releaseResultSet(). Parámetro en la función no valido.");
    	
    	if(poResultsSet.getPtro() == 0)
    		throw new OGRException("Error en poResultsSet(). Referencia del parámetro OGRLayer invalida.");
    	
    	releaseResultSetNat(cPtr, poResultsSet.getPtro());
    }

    /**
	 * 
	 */
    
    public void syncToDisk()throws OGRException{//Excepciones
    	
    	if(cPtr == 0)
			throw new OGRException("Error en syncToDisk(). El constructor no tuvo exito");
		
    	int ogrerr = syncToDiskNat(cPtr);
    	throwException(ogrerr, "Error en syncToDisk().");
    }

    /**
	 * 
	 */
    
    public int reference()throws OGRException{
    	
    	String msg1="Error en reference(). El constructor no tuvo exito.";
		String msg2="Error en reference().";
		return baseSimpleFunctions(6,msg1,msg2);
    }
    
    /**
	 * 
	 */
    
    public int dereference()throws OGRException{
    	
    	String msg1="Error en dereference(). El constructor no tuvo exito.";
		String msg2="Error en dereference().";
		return baseSimpleFunctions(7,msg1,msg2);
    }
    
    /**
	 * 
	 */
    
    public int getRefCount()throws OGRException{
    	
    	String msg1="Error en getRefCount(). El constructor no tuvo exito.";
		String msg2="Error en getRefCount(). No se ha obtenido un número de referencias valido.";
		return baseSimpleFunctions(8,msg1,msg2);
    }
    
    /**
	 * 
	 */
    
    public int getSummaryRefCount()throws OGRException{
    	
    	String msg1="Error en getSummaryRefCount(). El constructor no tuvo exito.";
		String msg2="Error en getSummaryRefCount().";
		return baseSimpleFunctions(9,msg1,msg2);
    } 
	 
	 	 
	 
}


	