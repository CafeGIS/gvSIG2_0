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
 * 2008 {DiSiD Technologies}  {Create Parameter object to define queries}
 */
package org.gvsig.fmap.dal;

import org.gvsig.tools.persistence.Persistent;

/**
 * Defines the properties of a collection of data, as a result of a query
 * through a DataStore.
 * <p>
 * The scale parameter can be used by the DataStore as a hint about the quality
 * or resolution of the data needed to view or operate with the data returned.
 * As an example, it may use the scale to return only a representative subset of
 * the data, or maybe to return Data with less detail, like a point or a line
 * instead of a polygon.
 * </p>
 *
 * @author <a href="mailto:cordin@disid.com">César Ordiñana</a>
 */
public interface DataQuery extends Persistent {

    /**
     * Sets the scale.
     *
     * @param scale
     *            the scale to set
     */
    public void setScale(double scale);

	/**
	 * Returns the value of an query parameter.
	 *
	 * @param name
	 *            of the parameter
	 * @return the parameter value
	 */
    public Object getQueryParameter(String name);

	/**
	 * Sets the value of an query parameter.
	 *
	 * @param name
	 *            of the query parameter
	 * @param value
	 *            for the query parameter
	 */
    public void setQueryParameter(String name, Object value);

}