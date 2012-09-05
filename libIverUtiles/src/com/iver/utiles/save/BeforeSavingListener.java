package com.iver.utiles.save;

import java.util.EventListener;


/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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

/**
 * The listener object created from that class is then registered with a
 * component using the component's <code>addBeforeSavingListener</code> 
 * method. A save event is generated when an object that provides support of notification
 * about saving files, is going to save a file. The relevant method in the listener 
 * object is then invoked, and the <code>SaveEvent</code> is passed to it.
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public interface BeforeSavingListener extends EventListener {
	/**
     * Invoked after saving a file.
     * See the class description of {@link SaveEvent SaveEvent} for a definition of 
     * is going to save an event.
     */
    public void beforeSaving(SaveEvent e);
}
