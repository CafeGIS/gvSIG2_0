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
package org.gvsig.fmap.mapcontext.layers;

import java.util.ArrayList;


/**
 * @author fjp
 *
 * Class to iterate for all layers in FLayers, but assuring not FLayers is returned.
 * The layer you receive in next method is a "leaf" layer, not grouped. 
 */
public class SingleLayerIterator {

	ArrayList singleLayers  =new ArrayList();
	int i = 0;
	int subIndex = 0;	
	public SingleLayerIterator(FLayers layers)
	{
		addSubLayer(layers);
		
	}

	public boolean hasNext() {
		return (i < singleLayers.size());
	}

	public FLayer next() {
		FLayer aux = (FLayer) singleLayers.get(i); 
		i++;
		return aux;
	}
	
	private void addSubLayer(FLayer lyr) {
		FLayers layers;
		if (lyr instanceof FLayers)
		{
			layers = (FLayers)lyr;
			for (int i=0; i < layers.getLayersCount(); i++)
			{
				addSubLayer(layers.getLayer(i));
			}			
		}
		else
		{
			singleLayers.add(lyr);
		}		
	}
	
	public void rewind()
	{
		i=0;
	}

}


