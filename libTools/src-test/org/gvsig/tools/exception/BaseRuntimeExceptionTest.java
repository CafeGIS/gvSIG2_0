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

import java.util.Map;

public class BaseRuntimeExceptionTest extends BaseExceptionTest {

    public void testRuntimeExceptionNature() {
        Object test = new Object() {

            public String toString() {
                throw new BaseRuntimeException("ERROR", "ERROR", 1) {
                    protected Map values() {
                        return null;
                    }
                };
            }

        };

        try {
            test.toString();
            fail("RuntimeException not generated");
        } catch (RuntimeException rex) {
            // success
        }
    }

    public void testRuntimeExceptionNature2() {
		Object test = new Object() {

			public String toString() {
				throw new BaseRuntimeException("ERROR", "ERROR", 1) {
				};
			}

		};

		try {
			test.toString();
			fail("RuntimeException not generated");
		} catch (RuntimeException rex) {
			// success
			rex.toString();
		}
	}

    protected IBaseException createBadDateException(String driver,
            Throwable throwable) {
        return new BadDateException(driver, throwable);
    }

    protected IBaseException createDriverException(String driver,
            Throwable throwable) {
        return new DriverRuntimeException(driver, throwable);
    }

    class BadDateException extends DriverRuntimeException {
        private static final long serialVersionUID = -8985920349210629998L;
        private static final String KEY = "Driver_%(driverName)s_Formato_de_fecha_incorrecto";

        private static final String MESSAGE = "Driver %(driverName)s: Formato de fecha incorrecto";

        public BadDateException(String driverName, Throwable cause) {
            super(driverName, MESSAGE, cause, KEY, serialVersionUID);
        }
    }

}
