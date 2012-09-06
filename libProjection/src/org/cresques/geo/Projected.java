/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * For more information, contact:
 *
 * cresques@gmail.com
 */
package org.cresques.geo;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;


/**
 * <p><code>Projected</code> should be implement by all objects that can be re-projected.</p>
 *
 * @author "Luis W. Sevilla" <sevilla_lui@gva.es>*
 */
public interface Projected {
	/**
	 * <p>Returns the current projection.<p>
	 * 
	 * @return current projection
	 * 
	 * @see #reProject(ICoordTrans)
	 */
    public IProjection getProjection();

    /**
     * <p>Changes projection of the graphical information of this object.</p>
     * 
     * @param ct transformation coordinates for obtaining the new projection
     * 
     * @see #getProjection()
     */
    public void reProject(ICoordTrans ct);
}
