/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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

package org.gvsig.gvsig3dgui.layer.properties;

import com.iver.andami.plugins.Extension;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;


/**
* El cuadro de Propiedades de visualización de 3D contiene el contenedor
* base se registran la entrada del menú del TOC y los paneles en el cuadro.
*
* @author Ángel Fraile Griñán (angel.fraile@iver.es)
* 
*/
public class Module3D extends Extension {

	public void execute(String arg0) {
		// TODO Auto-generated method stub
	}

	public void initialize() {
		ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
		
		if (!extensionPoints.containsKey("PropertiesDialog3D")) {
			extensionPoints.put(new ExtensionPoint("PropertiesDialog3D", "3D Properties registrable panels"));
		}
	
		extensionPoints.add("View_TocActions", "3DProperties",PropertiesTocMenuEntry3D.getSingleton());
		extensionPoints.add("PropertiesDialog3D", "infoPanel3D", InfoPanel3D.class);
		extensionPoints.add("PropertiesDialog3D", "vectorialPanel3D", VectorialLayerPanel3D.class);
	//	extensionPoints.add("PropertiesDialog3D", "rasterPanel3D", RasterLayerPanel3D.class);
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
	