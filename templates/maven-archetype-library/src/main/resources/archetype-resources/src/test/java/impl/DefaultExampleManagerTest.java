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
package ${package}.impl;

import junit.framework.TestCase;

/**
 * {@link DefaultExampleManager} unit tests.
 * 
 * @author <a href="mailto:devel@domain">Name Surname</a>
 */
public class DefaultExampleManagerTest extends TestCase {
	
	private DefaultExampleManager manager;

	protected void setUp() throws Exception {
		super.setUp();
		manager = new DefaultExampleManager();
		ExampleImplLibrary library = new ExampleImplLibrary();
		library.initialize();
		library.postInitialize();
	}

	/**
	 * Test method for {@link ${package}.DefaultExampleManager${symbol_pound}exampleMethod()}.
	 */
	public void testExampleMethod() {
		assertEquals("Example", manager.exampleMethod());
	}
}