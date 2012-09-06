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

import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.exception.BaseRuntimeException;


/**
 * Parent Exception for the Library errors and exceptions.
 * 
 * Create new exceptions for your Library as childs of this Exception,
 * so your Library API throws this Exception instead of each
 * child one.
 * 
 * @author <a href="mailto:devel@domain">Name Surname</a>
 */
public class ExampleException extends BaseRuntimeException {
    
    /**
	 * TODO: generate a new serialVersionUID for your exception.
	 */
	private static final long serialVersionUID = -5202384647454194305L;

	/**
     * @see BaseException${symbol_pound}BaseException(String, String, long)
     */
    public ExampleException(String message, String key, long code) {
        super(message, key, code);
    }

    /**
     * @see BaseException${symbol_pound}BaseException(String, Throwable, String, long)
     */
    public ExampleException(String message, Throwable cause, String key,
            long code) {
        super(message, cause, key, code);
    }

}