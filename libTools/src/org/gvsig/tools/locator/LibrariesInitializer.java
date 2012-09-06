/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Gobernment (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
 * AUTHORS (In addition to CIT):
 * 2009 {PRODEVELOP}   {Created}
 */

package org.gvsig.tools.locator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Class with static methods to help in initializing a set of libraries
 * @author jcarras
 *
 */
public class LibrariesInitializer {
	private static final int MODE_INITIALIZE = 0;
	private static final int MODE_POSTINITIALIZE = 1;
	
	/**
	 * Initializes all the libraries in the input parameter
	 * @param libraries Accepts Class objects or Strings 
	 */
	public void initialize(Collection libraries){
		initialize_or_postinitialize(libraries, MODE_INITIALIZE);
	}
	
	/**
	 * PostInitializes all the libraries in the input parameter
	 * @param libraries Accepts Class objects or Strings 
	 */
	public void postInitialize(Collection libraries){
		initialize_or_postinitialize(libraries, MODE_POSTINITIALIZE);
	}

	/**
	 * Initializes all gvSIG known libraries
	 */
	public void initializeAndPostinitializeAll(){
		InputStream is = LibrariesInitializer.class.getResourceAsStream("libraries.txt");
		initializeAndPostinitializeFile(is);
		try {
			is.close();
		} catch (IOException e){};
	}
	
	/**
	 * Initializes and PostInitializes all libraries whose name is written
	 * in the inputstream
	 * 
	 * @param file Contains the name of all clibraries to initialize
	 */
	public void initializeAndPostinitializeFile(InputStream inputstream){
	    InputStreamReader isr = null;
	    BufferedReader bir = null;
	    ArrayList libraries = new ArrayList();
	    try {

	      isr = new InputStreamReader(inputstream);
	      
	      bir = new BufferedReader(isr);
	      
	      String className = bir.readLine();
	      	      
	      while (className != null) {
	    	  try{
	    		  Class clase = Class.forName(className);
	    		  if (Library.class.isAssignableFrom(clase))
	    			  libraries.add(clase);
	    	  } catch (ClassNotFoundException e){
	    		  //e.printStackTrace();
			  } catch (ClassCastException e){
				  //e.printStackTrace();
			  } finally {
				  className = bir.readLine();
			  }
	      }

	      isr.close();
	      bir.close();

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
	    initialize(libraries);
	    postInitialize(libraries);
	}
	
	private void initialize_or_postinitialize(Collection libraries, int mode) {
		Iterator it = libraries.iterator();
		while (it.hasNext()) {
			try {
				Object obj = it.next();
				Object lib = null;
				if (obj instanceof Class) {
					Class clas = (Class) obj;
					lib = clas.newInstance();
				} else if (obj instanceof String) {
					String str = (String) obj;
					Class clas = Class.forName(str);
					lib = clas.newInstance();
				}
				if (lib instanceof Library) {
					Library libr = (Library) lib;
					if (mode==MODE_INITIALIZE){
						libr.initialize();
						System.out.println(libr.getClass().getName()
								+ " initialized");
					} else {
						libr.postInitialize();
						System.out.println(libr.getClass().getName()
								+ " postInitialized");
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}
	

}
