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

import java.util.Iterator;

class BaseExceptionIterator implements Iterator {
	
	Exception exception;
	
	BaseExceptionIterator(BaseException exception){
		this.exception = exception;
	}
	/** 
	 *  @return true if the iteration has more elements.
	 */
	public boolean hasNext() {
		return this.exception != null;
	}
	
	/** 
	 *  @return The next element in the iteration.
	 */
	public Object next() {
		Exception exception;
		exception = this.exception;
		this.exception = (Exception) exception.getCause();
		return exception;
	}
	
	/** 
	 *  @throws "UnsupportedOperationException" because
	 *  the remove operation will not be supported
	 *  by this Iterator.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}