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
 * 2009 {Iver T.I.}   {Task}
 */
/*
 * AUTHORS (In addition to CIT):
 * 2009 Instituto de Automática e Informática Industrial, UPV.
 */
package org.gvsig.fmap.geom.primitive;

/**
 * <p>
 * This interface is equivalent to the GM_Solid specified in <a href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012"
 * >ISO 19107</a>. Solid is a subclass of {@link Primitive}, and is the basis
 * for 3-dimensional geometry. The extent of a solid is defined by the boundary
 * surfaces.
 * </p>
 * 
 * @see <a
 *      href="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=26012">ISO
 *      19107</a>
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 * @author <a href="mailto:jtorres@ai2.upv.es">Jordi Torres Fabra</a>
 * @author <a href="mailto:rgaitan@ai2.upv.es">Rafa Gait&aacute;n Linares</a>
 */
public interface Solid extends Primitive {

	/**
	 * Add a surface to the Solid
	 * 
	 * @param surface
	 *            the surface to add
	 */
	public void addSurface(Surface surface);

	/**
	 * Remove surface from solid at concrete position
	 * 
	 * @param position
	 *            the concrete position
	 */
	public void removeSurface(int position);

	/**
	 * Get the number of surfaces in a solid
	 * 
	 * @return the number of surfaces
	 */
	public int getNumSurfaces();

	/**
	 * Gets a surface in a concrete position
	 * 
	 * @param position
	 *            the concrete position
	 * @return the surface
	 */
	public Surface getSurfaceAt(int position);

	/**
	 * Sets the appearance of a Solid
	 * 
	 * @param app
	 *            the appearance
	 */
	public void setAppearance(Appearance app);

	/**
	 * Gets the appearance of a solid
	 * 
	 * @return
	 */
	public Appearance getAppearance();

}
