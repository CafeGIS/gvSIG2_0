package com.iver.utiles.save;

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
 * An abstract adapter class for receiving save file events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * <P>
 * Extend this class to create a <code>SaveEvent</code> listener 
 * and override the methods for the events of interest. (If you implement the 
 * <code>AfterSavingListener</code> interface, you have to define all of
 * the methods in it. This abstract class defines null methods for them
 * all, so you can only have to define methods for events you care about.)
 * <P>
 * Create a listener object using the extended class and then register it with 
 * a component using the component's <code>addAfterSavingListener</code> 
 * method. When an object that provides support of notification
 * about has saved a file,
 * the relevant method in the listener object is invoked,
 * and the <code>SaveEvent</code> is passed to it.
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 *
 * @see SaveEvent
 * @see AfterSavingListener
 */
public abstract class AfterSavingAdapter implements AfterSavingListener {
	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.save.AfterSavingListener#afterSaving(com.iver.utiles.save.SaveEvent)
	 */
    public abstract void afterSaving(SaveEvent e);
}
