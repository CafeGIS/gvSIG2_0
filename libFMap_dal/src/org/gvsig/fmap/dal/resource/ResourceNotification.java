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

/*
* AUTHORS (In addition to CIT):
* 2008 IVER T.I   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.resource;


/**
 * A resource notification is related to a change in the state of a resource.
 * It is sent to all resource observers when appropriate. The notification contains
 * the type of change and access to the resource information.
 * 
 * This includes:
 * 
 * <ul>
 * 	<li>A resource has been opened</li>
 *  <li>A resource has been closed</li>
 *  <li>A resource has been prepared</li>
 *  <li>A resource property has been changed</li>
 *  <li>A resource is being disposed</li>
 *  <li>A resource is being opened</li>
 *  <li>A resource is being closed</li>
 * </ul>	
 * 
 * @author jmvivo
 */
public interface ResourceNotification {

	/** A resource has been opened */
	public static final String OPENED = "Opened_Resource";
	/** A resource has been closed */
	public static final String CLOSED = "Closed_Resource";
	/** A resource has been prepared */
	public static final String PREPARE = "Prepare_Resource";
	/** A resource property has been changed */
	public static final String CHANGED = "Changed_Resource";
	/** A resource is being disposed */
	public static final String DISPOSE = "Begin_Dispose_Resource";
	/** A resource is being opened */
	public static final String OPEN = "Begin_Open_Resource";
	/** A resource is being closed */
	public static final String CLOSE = "Begin_Close_Resource";

	/**
	 * Returns the parameters of the resource that caused
	 * this notification.
	 * 
	 * @return
	 * 		the parameters of this notification's source
	 */
	public ResourceParameters getParameters();
	
	/**
	 * Returns the resource that caused this notification.
	 * 
	 * @return
	 * 		this notification's source
	 */
	public Resource getResource();
	
	/**
	 * Returns the type of this notification.
	 * 
	 * @return
	 * 		this notification's type. For the allowed values see the 
	 * constants defined in this interface.
	 */
	public String getType();

}

