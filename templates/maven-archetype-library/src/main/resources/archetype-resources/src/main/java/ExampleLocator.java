#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
* 2009 {{Company}}  {{Task}}
*/
package ${package};

import org.gvsig.tools.locator.BaseLocator;
import org.gvsig.tools.locator.Locator;
import org.gvsig.tools.locator.LocatorException;

/**
 * Locator for the Example Library main object instances.
 * 
 * Replace the ExampleManager by your own services, by adding as many
 * constants and methods as needed, taking the ExampleManager as a guide.
 * 
 * @author <a href="mailto:devel@domain">Name Surname</a>
 */
public class ExampleLocator extends BaseLocator {

	public static final String EXAMPLE_MANAGER_NAME = "examplelocator.manager";

	private static final String EXAMPLE_MANAGER_DESCRIPTION = "Example Library Manager";

	/**
	 * Unique instance.
	 */
	private static final ExampleLocator instance = new ExampleLocator();

	/**
	 * Return the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public static ExampleLocator getInstance() {
		return instance;
	}

	/**
	 * Return a reference to the ExampleManager.
	 *
	 * @return a reference to ExampleManager
	 * @throws LocatorException
	 *             if there is no access to the class or the class cannot be
	 *             instantiated
	 * @see Locator${symbol_pound}get(String)
	 */
	public static ExampleManager getExampleManager()
			throws LocatorException {
		return (ExampleManager) getInstance().get(EXAMPLE_MANAGER_NAME);
	}

	/**
	 * Registers the Class implementing the ExampleManager interface.
	 *
	 * @param clazz
	 *            implementing the ExampleManager interface
	 */
	public static void registerExampleManager(Class clazz) {
		getInstance().register(EXAMPLE_MANAGER_NAME,
				EXAMPLE_MANAGER_DESCRIPTION,
				clazz);
	}
}