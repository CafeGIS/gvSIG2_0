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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author Equipo de desarrollo de gvSIG
 *
 */
public abstract class ListBaseException extends BaseException implements List{
	public ListBaseException(String message, String key, long code) {
		super(message, key, code);
	}

	public ListBaseException(String message, Throwable cause, String key,
			long code) {
		super(message, cause, key, code);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -3585958130775326741L;
	private List exceptions = new ArrayList();

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return this.exceptions.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return this.exceptions.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object arg0) {
		return this.exceptions.contains(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return this.exceptions.toArray();
	}

	/**
	 * @param arg0
	 * @return
	 */
	public Object[] toArray(Object[] arg0) {
		return this.exceptions.toArray(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean add(Object arg0) {
		return this.exceptions.add(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object arg0) {
		return this.exceptions.remove(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean containsAll(Collection arg0) {
		return this.exceptions.contains(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean addAll(Collection arg0) {
		return this.exceptions.addAll(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public boolean addAll(int arg0, Collection arg1) {
		return this.exceptions.addAll(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean removeAll(Collection arg0) {
		return this.exceptions.removeAll(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 */
	public boolean retainAll(Collection arg0) {
		return this.exceptions.retainAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		this.exceptions.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.List#get(int)
	 */
	public Object get(int arg0) {
		return this.exceptions.get(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public Object set(int arg0, Object arg1) {
		return this.exceptions.set(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public void add(int arg0, Object arg1) {
		this.exceptions.add(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see java.util.List#remove(int)
	 */
	public Object remove(int arg0) {
		return this.exceptions.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object arg0) {
		return this.exceptions.indexOf(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object arg0) {
		return this.exceptions.lastIndexOf(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {
		return this.exceptions.listIterator();
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int arg0) {
		return this.exceptions.listIterator(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.List#subList(int, int)
	 */
	public List subList(int arg0, int arg1) {
		return this.exceptions.subList(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage() {
		String msg = super.getMessage();
		Exception bex;
		Iterator iter=this.exceptions.iterator();
		while (iter.hasNext()) {
			bex = (Exception) iter.next();
			msg = msg + "\n  " + bex.getMessage();
		}
		return msg;
	}

	public String getLocalizedMessage(ExceptionTranslator exceptionTranslator,
			int indent) {
		String msg = super.getLocalizedMessage(exceptionTranslator, indent);
		Exception bex;
		Iterator iter=this.exceptions.iterator();
		while (iter.hasNext()) {
			bex = (Exception) iter.next();
			if( bex instanceof BaseException ) {
				msg = msg + "\n  " + ((BaseException)bex).getLocalizedMessage(exceptionTranslator, indent);
			} else {
				msg = msg + "\n  " + bex.getLocalizedMessage();
			}
		}
		return BaseException.insertBlanksAtStart(msg,indent);
	}
	
	public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        s.println("Contained exceptions: ");
        int counter = 0;
        Iterator iter = this.exceptions.iterator();
        while (iter.hasNext()) {
            counter++;
            s.print("EXCEPTION " + counter + ": ");
            Exception ex = (Exception) iter.next();
            ex.printStackTrace(s);
        }
    }

    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        s.println("Contained exceptions: ");
        int counter = 0;
        Iterator iter = this.exceptions.iterator();
        while (iter.hasNext()) {
            counter++;
            s.print("EXCEPTION " + counter + ": ");
            Exception ex = (Exception) iter.next();
            ex.printStackTrace(s);
        }
    }
}
