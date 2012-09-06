/**********************************************************************
 * $Id: OGRStyleTable.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     OGRStyleTable.java
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

public class OGRStyleTable extends JNIBase{
	
	private native void FreeOGRStyleTable(long cPtr);
	private native boolean addStyleNat( long cPtr, String pszName,String pszStyleString);
	private native boolean removeStyleNat( long cPtr, String pszName);
	private native boolean modifyStyleNat( long cPtr, String pszName, String pszStyleString);
	private native boolean saveStyleTableNat( long cPtr, String pszFilename);
	private native boolean loadStyleTableNat( long cPtr, String pszFilename);
	private native String findNat( long cPtr, String pszStyleString);
	private native boolean isExistNat( long cPtr, String pszName);
	private native String getStyleNameNat( long cPtr, String pszName);
	private native void  printNat( long cPtr, String fpOut);
	private native void  clearNat( long cPtr );
	
	
	String[] m_papszStyleTable;  //Averiguar en que momento se carga
	
	/**
	 * Constructor
	 * @param cPtr	dirección de memoria al objeto OGRStyleTable de C. 
	 */
	
	public OGRStyleTable(long cPtr){
		this.cPtr=cPtr;
	}
	
	/**
	 * Destructor 
	 */
	
	protected void finalize() throws OGRException{
		if(cPtr == 0)
			throw new OGRFailureException("Fallo al acceder al dato.");
		
		FreeOGRStyleTable(cPtr);
	}
	

	/**
	 * 
	 */
	
	public boolean addStyle(String pszName,String pszStyleString)throws OGRException{
		return true;
	}

	/**
	 * 
	 */
	
	public boolean removeStyle(String pszName)throws OGRException{
		return true;
	}
	
	/**
	 * 
	 */
	
	public boolean modifyStyle(String pszName, String pszStyleString)throws OGRException{
		return true;
	}
	

	/**
	 * 
	 */
	
	public boolean saveStyleTable(String pszFilename)throws OGRException{
		return true;
	}

	/**
	 * 
	 */
	
	public boolean loadStyleTable(String pszFilename)throws OGRException{
		return true;	
	}

	/**
	 * 
	 */
	
	public String find(String pszStyleString)throws OGRException{
		return null;
	}

	/**
	 * 
	 */
	
	public boolean isExist(String pszName)throws OGRException{
		return true;
	}

	/**
	 * 
	 */
	
	public String getStyleName(String pszName)throws OGRException{
		return null;
	}

	/**
	 * 
	 */
	
	public void  print(String fpOut)throws OGRException{
		
	}

	/**
	 * 
	 */
	
	public void  clear()throws OGRException{
		
	}
	

}