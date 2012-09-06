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
 */
package org.gvsig.tools.exception;

import junit.framework.TestCase;

public class BaseExceptionTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testSimple(){
		try {
			throw new NullPointerException("Excepcion de puntero nulo");
		} catch (NullPointerException e){
		    IBaseException de = createDriverException("SimpleDriver", e);
			assertEquals("Error in the driver SimpleDrivers", de.getMessage());
			assertEquals(
                    "Error in the driver SimpleDrivers\nExcepcion de puntero nulo",
                    de.getMessageStack());
		}
	}

	public void testSimpleLocalized(){
		class MyTranslator implements ExceptionTranslator {
			public String getText(String clave) {
				return clave.toUpperCase();
			}
		}
		BaseException.setTranslator(new MyTranslator());
		try {
			throw new NullPointerException("Excepcion de puntero nulo");
		} catch (NullPointerException e){
            IBaseException de = createDriverException("SimpleDriver", e);
			assertEquals("ERROR_IN_THE_DRIVER_%(DRIVERNAME)S",de.getLocalizedMessage());
			assertEquals(
                    "ERROR_IN_THE_DRIVER_%(DRIVERNAME)S\nExcepcion de puntero nulo",
                    de.getLocalizedMessageStack());
		}
		BaseException.setTranslator(null);
	}

	public void testSimple2(){
		try {
			throw new NullPointerException("Excepcion de puntero nulo");
		} catch (NullPointerException e){
            IBaseException de = createBadDateException("SimpleDriver", e);
			assertEquals("Driver SimpleDrivers: Formato de fecha incorrecto",
                    de.getMessage());
			assertEquals(
                    "Driver SimpleDrivers: Formato de fecha incorrecto\nExcepcion de puntero nulo",
                    de.getMessageStack());
		}
	}

	public void testSimpleLocalized2(){
		class MyTranslator implements ExceptionTranslator {
			public String getText(String clave) {
				return clave.toUpperCase();
			}
		}
		BaseException.setTranslator(new MyTranslator());
		try {
			throw new NullPointerException("Excepcion de puntero nulo");
		} catch (NullPointerException e){
            IBaseException de = createBadDateException("SimpleDriver", e);
			assertEquals("DRIVER_%(DRIVERNAME)S_FORMATO_DE_FECHA_INCORRECTO",de.getLocalizedMessage());
			assertEquals(
                    "DRIVER_%(DRIVERNAME)S_FORMATO_DE_FECHA_INCORRECTO\nExcepcion de puntero nulo",
                    de.getLocalizedMessageStack());
		}
		BaseException.setTranslator(null);
	}

	public void testTranslatorWithoutInterface() {
		class MyTranslator {
			public String getText(String clave) {
				return clave.toUpperCase();
			}
		}
		BaseException.setTranslator(new MyTranslator());
		try {
			throw new NullPointerException("Excepcion de puntero nulo");
		} catch (NullPointerException e){
            IBaseException de = createBadDateException("SimpleDriver", e);
			assertEquals("DRIVER_%(DRIVERNAME)S_FORMATO_DE_FECHA_INCORRECTO",de.getLocalizedMessage());
			assertEquals(
                    "DRIVER_%(DRIVERNAME)S_FORMATO_DE_FECHA_INCORRECTO\nExcepcion de puntero nulo",
                    de.getLocalizedMessageStack());
		}
		BaseException.setTranslator(null);
		
	}
	
    public void testInsertBlanksAtStart() {
        String src = "test";

        assertEquals("test", BaseException.insertBlanksAtStart(src, 0));

        assertEquals("test", BaseException.insertBlanksAtStart(src, -5));

        assertEquals("  test", BaseException.insertBlanksAtStart(src, 2));

        assertEquals("      test", BaseException.insertBlanksAtStart(src, 6));
    }
    
    protected IBaseException createDriverException(String driver,
            Throwable throwable) {
        return new DriverException(driver, throwable);
    }
    
    protected IBaseException createBadDateException(String driver,
            Throwable throwable) {
        return new BadDateException(driver, throwable);
    }

	class BadDateException extends DriverException {
		private static final long serialVersionUID = -8985920349210629998L;
	    private static final String KEY = "Driver_%(driverName)s_Formato_de_fecha_incorrecto";

	    private static final String MESSAGE = "Driver %(driverName)s: Formato de fecha incorrecto";

		public BadDateException(String driverName, Throwable cause) {
            super(driverName, MESSAGE, cause, KEY, serialVersionUID);
		}
	}
	
}
