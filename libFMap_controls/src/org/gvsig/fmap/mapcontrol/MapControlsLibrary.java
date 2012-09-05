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
 * 2009 {DiSiD Technologies}  {Create Library class to initialize the MapControls library}
 */
package org.gvsig.fmap.mapcontrol;

import java.util.Locale;

import org.gvsig.i18n.Messages;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * Initialization of the MapControls library.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class MapControlsLibrary extends BaseLibrary {

    @Override
    public void initialize() throws ReferenceNotRegisteredException {
        if (!Messages.hasLocales()) {
            Messages.addLocale(Locale.getDefault());
        }
        Messages.addResourceFamily(
                "org.gvsig.fmap.mapcontrol.i18n.text",
                MapControlsLibrary.class.getClassLoader(),
                MapControlsLibrary.class.getClass().getName());
    }
}