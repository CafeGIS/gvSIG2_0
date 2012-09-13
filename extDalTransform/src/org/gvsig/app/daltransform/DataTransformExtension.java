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
 
package org.gvsig.app.daltransform;

import org.gvsig.app.daltransform.impl.DefaultDataTransformLibrary;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DataTransformExtension extends Extension{
	public static DataTransformLibrary library = null;
	
	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		PluginServices.getMDIManager().addWindow(DataTransformLocator.getDataTransformManager().createWizard());
	}	

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
		library = new DefaultDataTransformLibrary();
		library.initialize();
	}	
	
	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();
		library.postInitialize();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"feature-transform",
				this.getClass().getClassLoader().getResource("images/transform.png")
		);
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {		
		return true;
	}
}

