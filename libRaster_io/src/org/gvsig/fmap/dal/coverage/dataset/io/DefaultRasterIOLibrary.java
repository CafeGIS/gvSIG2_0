/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.fmap.dal.coverage.dataset.io;

import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;
/**
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class DefaultRasterIOLibrary extends BaseLibrary {	
	private static final DefaultRasterIOLibrary instance     = new DefaultRasterIOLibrary();

	/**
	 * Return the singleton instance.
	 * @return the singleton instance
	 */
	public static DefaultRasterIOLibrary getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.Library#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
		super.initialize();
		
		// Registro de los drivers de lectura
		GdalDriver.register();
		ErmapperDriver.register();
		MrSidDriver.register();
		MemoryRasterDriver.register();

		// Registro de los drivers de escritura
		GdalWriter.register();
		ErmapperWriter.register();
		JpegWriter.register();
		PngWriter.register();
	}

	public void postInitialize() throws ReferenceNotRegisteredException {
		
	}
}
