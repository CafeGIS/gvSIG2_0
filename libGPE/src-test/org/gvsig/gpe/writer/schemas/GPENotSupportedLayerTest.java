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
 * $Id: GPENotSupportedLayerTest.java 162 2007-06-29 12:19:48Z jorpiell $
 * $Log$
 * Revision 1.1  2007/06/29 12:19:14  jorpiell
 * The schema validation is made independently of the concrete writer
 *
 *
 */
/**
 * This test try to add a Layer with a type that doesn't exist 
 * in its XML schema
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public abstract class GPENotSupportedLayerTest extends GPENotSupportedSchema{
	private String layer1Id = "l1";
	private String layer1Name = "Parent layer";
	private String layer1Description = "This is a test of a wrong layer feature";
	private String layer1Srs = "EPSG:23030";
	//private String layer1XsElementName = "rivers";
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#readObjects()
	 */
	public void readObjects() {
		Layer[] layers = getLayers();
		assertEquals(layers.length, 0);		
		
		//Two warinings
		assertEquals(getErrorHandler().getWarningsSize(),1);
		for (int i=0 ; i<getErrorHandler().getWarningsSize() ; i++){
			assertTrue(getErrorHandler().getWarningAt(i) instanceof NotSupportedLayerWarning);
			System.out.println(getErrorHandler().getWarningAt(i));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#writeObjects()
	 */
	public void writeObjects() {
		getWriterHandler().initialize();
		getWriterHandler().startLayer(layer1Id, null, layer1Name, layer1Description, layer1Srs);
		getWriterHandler().endLayer();
		getWriterHandler().close();	
	}
}
