/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.proj;

/**
 * 
 * @author Miguel García Jiménez (garciajimenez.miguel@gmail.com)
 *
 */
public class JNIBaseCrs
{
	protected long cPtr;
	protected int latLong;
	protected String _strCrs;
	protected native long loadCrs(String crs);
	protected native void freeCrs(long crs);
	protected native int isLatlong(long crs);
	protected static native int compareDatums(long datum1, long datum2);
	protected native int getErrno();
	protected static native String strErrno(int errno);
	
	
	protected static native int operation(double[] firstCoord,
		    double[] secondCoord,
		    double[] values,
		    long srcCodeString,
		    long destCodeString);

	protected static native int operationSimple(double firstcoord,
			double secondcoord,
			double values,
			long srcCodeString,
			long destCodeString);

	protected static native int operationArraySimple(double[] Coord,
			 long srcCodeString,
			 long destCodeString);
	
	static {
		System.loadLibrary("crsjniproj2.0.0");
	}
	
	protected void createCrs(String strCrs) throws CrsProjException {
		cPtr=loadCrs(strCrs);
		
		int errNo = getErrNo(); 
		// **** Provisional: strErrNo tira la máquina virtual en Windows. *****
		//TODO: Revisarlo en el código jni.
		//if (errNo<0 && errNo!=-10) throw new CrsProjException("Error creating CRS: "+strErrNo(errNo));
		if (errNo<0 && errNo!=-10) throw new CrsProjException("Error creating CRS.");
		_strCrs=strCrs;
	}
	
	protected void deleteCrs() {
		//if(cPtr>0) freeCrs(cPtr);
		if(cPtr>0); 
	}
	
	public boolean isLatlong() {
		latLong = isLatlong(cPtr);
		if(latLong == 0)
			return false;
		return true;
	}
	
	protected long getPtr() {
		/*if (cPtr>0)	return cPtr;
		else throw new CrsProjException(_strCrs);*/
		return cPtr;
	}
	
	public String getStr() {
		return _strCrs;
	}
	
	public void changeStrCrs(String code) {
		_strCrs += code;
	}
	
	protected int getErrNo(){
		return getErrno();
	}
	
	protected static String strErrNo(int errno){
		return strErrno(errno);
	}
	
	//OPERATIONS
	
	public static int operate(double[] firstCoord,
		     double[] secondCoord,
		     double[] thirdCoord,
		     CrsProj srcCrs,
		     CrsProj destCrs)
			 throws OperationCrsException{
		
		int error=operation(firstCoord,secondCoord,thirdCoord,srcCrs.getPtr(),destCrs.getPtr());
		
		// error -38: el punto transformar está fuera del ámbito del nadgrid
		// **** Provisional: strErrNo tira la máquina virtual en Windows. *****
		//TODO: Revisarlo en el código jni.
		//if(error!=0 && error !=-38) throw new OperationCrsException(srcCrs,destCrs, strErrNo(error));
		if(error!=0 && error !=-38) throw new OperationCrsException(srcCrs,destCrs, "");
		return error;
	}

	public static void operateSimple(double firstCoord,
				 double secondCoord,
				 double thirdCoord,
				 CrsProj srcCrs,
				 CrsProj destCrs) throws OperationCrsException{

		int error = operationSimple(firstCoord, secondCoord, thirdCoord,srcCrs.getPtr(),destCrs.getPtr());
		// **** Provisional: strErrNo tira la máquina virtual en Windows. *****
		//TODO: Revisarlo en el código jni.
		//if(error!=1) throw new OperationCrsException(srcCrs,destCrs, strErrNo(error));
		if(error!=1) throw new OperationCrsException(srcCrs,destCrs, "");
	}

	public static void operateArraySimple(double[] Coord,
					  CrsProj srcCrs,
					  CrsProj destCrs)throws OperationCrsException{

		int error = operationArraySimple(Coord,srcCrs.getPtr(),destCrs.getPtr());
		// **** Provisional: strErrNo tira la máquina virtual en Windows. *****
		//TODO: Revisarlo en el código jni.
		//if(error!=1) throw new OperationCrsException(srcCrs,destCrs, strErrNo(error));
		if(error!=1) throw new OperationCrsException(srcCrs,destCrs, "");
	}
	
	public static int compareDatums(CrsProj crs1, CrsProj crs2){
		int compare = 0;
		compare = compareDatums(crs1.getPtr(),crs2.getPtr());	
		return compare;
	}
}
