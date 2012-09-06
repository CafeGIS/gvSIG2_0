/**********************************************************************
 * $Id: OGRSFDriverRegistrar.java 15690 2007-10-31 10:28:41Z nbrodin $
 *
 * Name:     OGRSFDriverRegistrar.java
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
 * Gestor de drivers
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class OGRSFDriverRegistrar extends JNIBase{
	
	private long driver;
	private native long openNat(String fte, int bUpdate);
	private native long getRegistrarNat();
	private native long getDriverNat(long cPtr, int i);
	private native void FreeOGRSFDriverRegistrarNat(long cPtr);
	
	private native int releaseDataSource( long cPtr, OGRDataSource ds ); //Excepciones
	private native void registerDriver( long cPtr, OGRSFDriver poDriver );
	private native OGRSFDriver getDriverByName( long cPtr, String name );
	private native OGRDataSource getOpenDS(long cPtr, int i );
	
	/**
	 * Abre un fichero de imágen
	 * @throws OGRException
	 * @param fte	fichero fuente
	 * @param bUpdate
	 * @param pdriver
	 * @return OGRDataSource
	 */
	
	public OGRDataSource open(String fte, boolean bUpdate, OGRSFDriver pdriver)throws OGRException{
		
		OGRDataSource ds;
		OGRSFDriver drv;
		driver = pdriver.getPtro();
		int bU=0;
		
		if(bUpdate)bU=1;
		
		if (fte == null || fte.equals(""))
			throw new OGRException("Fallo en parametro de entrada de OGRDataSource.open");
		
		long res = openNat(fte, bU);
				
		if(res == 0 || driver == 0)
			throw new OGRException("Error en open(). No se ha podido abrir la imágen.");
		else{
			ds = new OGRDataSource(res);
			drv = new OGRSFDriver(driver);
		}
		
		pdriver=drv;		
		return ds;
	}
	
	/**
	 * Obtiene el número de drivers registrados en el gestor
	 * @throws OGRException
	 * @return Número de drivers
	 */
	
	 public int getDriverCount()throws OGRException{
	 		
		String msg1="Error en getDriverCount. . La llamada getRegistrar de OGR no tuvo exito";
		String msg2="Error en la cuenta de drivers";
		return baseSimpleFunctions(0,msg1,msg2);
	 }
	 
	 /**
	 * Obtiene el driver indicado por el índice
	 * @throws OGRException
	 * @return un driver
	 */
		
	 public OGRSFDriver getDriver(int i)throws OGRException{

		 if(cPtr == 0)
				throw new OGRException("Fallo al acceder al dato.");

		 long drv = getDriverNat(cPtr, i);

		 if(drv == 0)
			 throw new OGRException("Error en getDriver(). No se ha podido obtener el driver indicado.");

		 return new OGRSFDriver(drv);

	 }

	/**
	* Destructor 
	*/
		
	protected void finalize() throws OGRFailureException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRSFDriverRegistrarNat(cPtr);
	}
	
	
	/**
	 * 
	 */
	
	public void releaseDataSource( OGRDataSource ds )throws OGRException{ //Excepciones
		
	}
	
	/**
	 * 
	 */
	
	public void registerDriver( OGRSFDriver poDriver )throws OGRException{
		
	}
	
	/**
	 * 
	 */
	
	public OGRSFDriver getDriverByName( String name )throws OGRException{
		return null;
	}
	
	/**
	 * 
	 */
	
	public int getOpenDSCount()throws OGRException{
		return 0;
	}
	
	/**
	 * 
	 */
	
	public OGRDataSource getOpenDS( int i )throws OGRException{
		return null;
	}
	
	
}