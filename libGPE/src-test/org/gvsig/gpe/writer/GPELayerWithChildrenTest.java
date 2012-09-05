package org.gvsig.gpe.writer;

import org.gvsig.gpe.containers.Layer;

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
/* CVS MESSAGES:
 *
 * $Id: GPELayerWithChildrenTest.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.2  2007/06/07 14:52:28  jorpiell
 * Add the schema support
 *
 * Revision 1.1  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPELayerWithChildrenTest extends GPEWriterBaseTest{
	private String layer1Id = "l1";
	private String layer1Name = "Layer 1";
	private String layer1Description = "Layer with bbox Test";
	private String srs = "EPSG:23030";
	private String layer11Id = "l1.1";
	private String layer11Name = "Layer 1.1";
	private String layer11Description = "Layer with bbox Test";
	private String layer111Id = "l1.1.1";
	private String layer111Name = "Layer 1.1.1";
	private String layer111Description = "Layer with bbox Test";
	private String layer12Id = "l1.2";
	private String layer12Name = "Layer 1.2";
	private String layer12Description = "Layer with bbox Test";

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
		assertEquals(layer.getName(), layer1Name);
		
		assertEquals(layer.getLayers().size(),2);
		assertEquals(layer.getLayerAt(0).getName(), layer11Name);
		assertEquals(layer.getLayerAt(0).getLayers().size(),1);
		assertEquals(layer.getLayerAt(0).getLayerAt(0).getName(),layer111Name);
		assertEquals(layer.getLayerAt(1).getName(), layer12Name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layer1Id, null, layer1Name, layer1Description, srs);
		getWriterHandler().startLayer(layer11Id, null, layer11Name, layer11Description, srs);
		getWriterHandler().startLayer(layer111Id, null, layer111Name, layer111Description, srs);
		getWriterHandler().endLayer();
		getWriterHandler().endLayer();
		getWriterHandler().startLayer(layer12Id, null, layer12Name, layer12Description, srs);
		getWriterHandler().endLayer();
		getWriterHandler().endLayer();
		getWriterHandler().close();		
	}
}

