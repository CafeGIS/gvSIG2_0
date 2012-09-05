/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/
package org.gvsig.catalog.loaders;

import java.io.IOException;

import org.gvsig.catalog.schemas.Resource;
import org.gvsig.i18n.Messages;

import com.Ostermiller.util.Browser;

public class LinkLoader extends GvSigLayerLoader{
	
	

	public LinkLoader(Resource resource) {
		super(resource);
		
	}

	/**
	 * It loads the resource to the gvSIG view
	 * @throws LayerLoaderException 
	 */
	public void loadLayer() throws LayerLoaderException {
		String link = getResource().getLinkage();
		Browser.init();
		try {
			Browser.displayURL(link);
		} catch (IOException e) {
			throw new LayerLoaderException(e.getMessage(),getWindowMessage());
		}		
		
	}

	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getErrorMessage()
	 */
	protected String getErrorMessage() {
		return Messages.getText("linkError") + ".\n" +
				Messages.getText("link") + ": " + 
				getResource().getLinkage();						
	}

	/*
	 *  (non-Javadoc)
	 * @see es.gva.cit.gvsig.catalogClient.loaders.LayerLoader#getWindowMessage()
	 */
	protected String getWindowMessage() {
		return Messages.getText("linkLoad");
	}

}
