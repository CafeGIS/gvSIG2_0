package org.gvsig.gpe.writer.schemas;

import org.gvsig.gpe.containers.Layer;
import org.gvsig.gpe.warnings.NotSupportedLayerWarning;

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
 * $Id: GPENotSupportedChildLayerTest.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPENotSupportedChildLayerTest  extends GPENotSupportedSchema{
	private String layer1Id = "l1";
	private String layer1Name = "Parent layer";
	private String layer1Description = "This is a test of a wrong layer feature";
	private String layer1Srs = "EPSG:23030";
	//private String layer1XsElementName = "cities";
	private String layer2Id = "l2";
	private String layer2Name = "Child layer";
	private String layer2Description = "This is a test of a wrong layer feature";
	private String layer2Srs = "EPSG:23030";
	//private String layer2XsElementName = "postal codes";	
	private String feature1Name = "city";
	private String feature1Id = "f1";
	//Schema 
	//private String xsFeature1Name = "river";
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 1);		
		Layer layer = layers[0];
	
		assertEquals(layer.getLayers().size(),0);

		boolean layerNotSupported = false;
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			if (getErrorHandler().getWarningAt(i) instanceof NotSupportedLayerWarning){
				layerNotSupported = true;
			}			
		}
		assertTrue(layerNotSupported);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layer1Id, null, layer1Name, layer1Description, layer1Srs);
		getWriterHandler().startLayer(layer2Id, null, layer2Name, layer2Description, layer2Srs);
		getWriterHandler().startFeature(feature1Id, null, feature1Name);
		getWriterHandler().endFeature();	
		getWriterHandler().endLayer();
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}	
	
}

