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

/**
 * An example library Manager interface. Use it as an example to create your own
 * Manager as your library main enter point.
 * 
 * @author <a href="mailto:devel@domain">Name Surname</a>
 */
public interface ExampleManager {

	/**
	 * Sample Manager method.
	 * 
	 * @return the sample value
	 * @throws ExampleManager
	 *             if there is an error getting the example value
	 */
	String exampleMethod() throws ExampleException;

}