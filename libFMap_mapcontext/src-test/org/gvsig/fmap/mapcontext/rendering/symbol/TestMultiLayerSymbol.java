/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.mapcontext.rendering.symbol;

import java.util.Random;

import junit.framework.TestCase;

import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class TestMultiLayerSymbol extends TestCase {
	ISymbol[] symbols;

	public void setUp() {
		symbols = TestISymbol.getNewSymbolInstances();
	}

	public void testLayerTypeAdditionFor_MARKERS() {
		MultiLayerMarkerSymbol multiLayer = new MultiLayerMarkerSymbol();
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] instanceof IMarkerSymbol) {
				try {
				IMarkerSymbol marker = (IMarkerSymbol) symbols[i];
				multiLayer.addLayer(marker);
				} catch (Exception e) {
					fail("MultiLayerMarkerSymbol failed adding a '"+symbols[i].getClassName());
				}
			} else {
				try {
					multiLayer.addLayer(symbols[i]);
					fail("MultiLayerMarkerSymbol should not accept '"+symbols[i].getClassName()+"' symbols");
				} catch (ClassCastException ccEx){
					// this is right
				}
			}
		}
	}

	public void testLayerTypeAdditionFor_LINES() {
		MultiLayerLineSymbol multiLayer = new MultiLayerLineSymbol();
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] instanceof ILineSymbol) {
				try {
				ILineSymbol line = (ILineSymbol) symbols[i];
				multiLayer.addLayer(line);
				} catch (Exception e) {
					fail("MultiLayerLineSymbol failed adding a '"+symbols[i].getClassName());
				}
			} else {
				try {
					multiLayer.addLayer(symbols[i]);
					fail("MultiLayerLineSymbol should not accept '"+symbols[i].getClassName()+"' symbols");
				} catch (ClassCastException ccEx){
					// this is right
				}
			}
		}
	}

	public void testLayerTypeAdditionFor_FILL() {
		MultiLayerFillSymbol multiLayer = new MultiLayerFillSymbol();
		for (int i = 0; i < symbols.length; i++) {
			if (symbols[i] instanceof IFillSymbol) {
				try {
					IFillSymbol fill = (IFillSymbol) symbols[i];
				multiLayer.addLayer(fill);
				} catch (Exception e) {
					fail("MultiLayerLineSymbol failed adding a '"+symbols[i].getClassName());
				}
			} else {
				try {
					multiLayer.addLayer(symbols[i]);
					fail("MultiLayerLineSymbol should not accept '"+symbols[i].getClassName()+"' symbols");
				} catch (ClassCastException ccEx){
					// this is right
				}
			}
		}
	}

	public void testLayerAdditionPositionConsistencyMultiLayer_MARKER_Symbol() {
		MultiLayerMarkerSymbol multiLayer = new MultiLayerMarkerSymbol();
		IMarkerSymbol sym1 = new SimpleMarkerSymbol();
		IMarkerSymbol sym2 = new SimpleMarkerSymbol();
		sym1.setDescription("sym1");
		sym2.setDescription("sym2");
		try {
			multiLayer.addLayer(sym1, 0);
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should always accept adding at index 0, even when it is empty");
		}

		try {
			multiLayer.addLayer(sym2, 3);
			fail("MultiLayer cannot accept adding at an index larger than the amount of layers already contained");
		} catch (IndexOutOfBoundsException ioobEx){
			// this is right
		}

		try {
			multiLayer.addLayer(sym1, multiLayer.getLayerCount());
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should accept adding at index less or equal to the layer count");
		}

		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(1).getDescription().equals("sym2"));
		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(0).getDescription().equals("sym1"));
	}

	public void testLayerAdditionPositionConsistencyMultiLayer_LINE_Symbol() {
		MultiLayerLineSymbol multiLayer = new MultiLayerLineSymbol();
		ILineSymbol layer1 = new SimpleLineSymbol();
		ILineSymbol layer2 = new SimpleLineSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");

		try {
			multiLayer.addLayer(layer1, 0);
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should always accept adding at index 0, even when it is empty");
		}

		try {
			multiLayer.addLayer(layer2, 3);
			fail("MultiLayer cannot accept adding at an index larger than the amount of layers already contained");
		} catch (IndexOutOfBoundsException ioobEx){
			// this is right
		}

		try {
			multiLayer.addLayer(layer2, multiLayer.getLayerCount());
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should accept adding at index less or equal to the layer count");
		}

		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(1).getDescription().equals("layer2"));
		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(0).getDescription().equals("layer1"));
	}

	public void testLayerAdditionPositionConsistencyMultiLayer_FILL_Symbol() {
		MultiLayerFillSymbol multiLayer = new MultiLayerFillSymbol();
		IFillSymbol layer1 = new SimpleFillSymbol();
		IFillSymbol layer2 = new SimpleFillSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");

		try {
			multiLayer.addLayer(layer1, 0);
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should always accept adding at index 0, even when it is empty");
		}

		try {
			multiLayer.addLayer(layer2, 3);
			fail("MultiLayer cannot accept adding at an index larger than the amount of layers already contained");
		} catch (IndexOutOfBoundsException ioobEx){
			// this is right
		}

		try {
			multiLayer.addLayer(layer2, multiLayer.getLayerCount());
		} catch (IndexOutOfBoundsException ioobEx) {
			fail("MultiLayer should accept adding at index less or equal to the layer count");
		}

		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(1).getDescription().equals("layer2"));
		assertTrue("layer returned does not correspond with the index provided", multiLayer.getLayer(0).getDescription().equals("layer1"));
	}

	public void testMultiLayer_MARKER_SymbolLayerSwapping() {
		MultiLayerMarkerSymbol multiLayer = new MultiLayerMarkerSymbol();
		IMarkerSymbol layer1 = new SimpleMarkerSymbol();
		IMarkerSymbol layer2 = new SimpleMarkerSymbol();
		IMarkerSymbol layer3 = new SimpleMarkerSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);

		multiLayer.swapLayers(1,0);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(0).getDescription().equals("layer2")
				&& multiLayer.getLayer(1).getDescription().equals("layer1"));

		multiLayer.swapLayers(1,2);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(2).getDescription().equals("layer1")
				&& multiLayer.getLayer(1).getDescription().equals("layer3"));

		Random random = new Random(System.currentTimeMillis());
		int numTries = 50;
		for (int i = 0; i < numTries; i++) {
			int n = random.nextInt(multiLayer.getLayerCount());
			int m = random.nextInt(multiLayer.getLayerCount());

			ISymbol aLayer = multiLayer.getLayer(n);
			multiLayer.swapLayers(n, m);
			ISymbol otherLayer = multiLayer.getLayer(m);
			assertEquals("failed swapping layers "+n+" and "+m, aLayer, otherLayer);
		}

	}

	public void testMultiLayer_LINE_SymbolLayerSwapping() {
		MultiLayerLineSymbol multiLayer = new MultiLayerLineSymbol();
		ILineSymbol layer1 = new SimpleLineSymbol();
		ILineSymbol layer2 = new SimpleLineSymbol();
		ILineSymbol layer3 = new SimpleLineSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);

		multiLayer.swapLayers(1,0);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(0).getDescription().equals("layer2")
				&& multiLayer.getLayer(1).getDescription().equals("layer1"));

		multiLayer.swapLayers(1,2);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(2).getDescription().equals("layer1")
				&& multiLayer.getLayer(1).getDescription().equals("layer3"));

		Random random = new Random(System.currentTimeMillis());
		int numTries = 50;
		for (int i = 0; i < numTries; i++) {
			int n = random.nextInt(multiLayer.getLayerCount());
			int m = random.nextInt(multiLayer.getLayerCount());

			ISymbol aLayer = multiLayer.getLayer(n);
			multiLayer.swapLayers(n, m);
			ISymbol otherLayer = multiLayer.getLayer(m);
			assertEquals("failed swapping layers "+n+" and "+m, aLayer, otherLayer);
		}

	}

	public void testMultiLayer_FILL_SymbolLayerSwapping() {
		MultiLayerFillSymbol multiLayer = new MultiLayerFillSymbol();
		IFillSymbol layer1 = new SimpleFillSymbol();
		IFillSymbol layer2 = new SimpleFillSymbol();
		IFillSymbol layer3 = new SimpleFillSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);

		multiLayer.swapLayers(1,0);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(0).getDescription().equals("layer2")
				&& multiLayer.getLayer(1).getDescription().equals("layer1"));

		multiLayer.swapLayers(1,2);
		assertTrue("failed swapping layers 0 and 1",
				   multiLayer.getLayer(2).getDescription().equals("layer1")
				&& multiLayer.getLayer(1).getDescription().equals("layer3"));

		Random random = new Random(System.currentTimeMillis());
		int numTries = 50;
		for (int i = 0; i < numTries; i++) {
			int n = random.nextInt(multiLayer.getLayerCount());
			int m = random.nextInt(multiLayer.getLayerCount());

			ISymbol aLayer = multiLayer.getLayer(n);
			multiLayer.swapLayers(n, m);
			ISymbol otherLayer = multiLayer.getLayer(m);
			assertEquals("failed swapping layers "+n+" and "+m, aLayer, otherLayer);
		}
	}

	public void testMultiLayer_MARKER_IndexOutOfBounds() {
		MultiLayerMarkerSymbol multiLayer = new MultiLayerMarkerSymbol();
		IMarkerSymbol layer1 = new SimpleMarkerSymbol();
		IMarkerSymbol layer2 = new SimpleMarkerSymbol();
		IMarkerSymbol layer3 = new SimpleMarkerSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);

		for (int i = -4; i < multiLayer.getLayerCount()+4; i++) {
			if (i<0 || i>=multiLayer.getLayerCount()) {
				try {
					multiLayer.getLayer(i);
					fail("MultiLayerMarkerSymbol should throw an IndexOutOfBoundsException when accessing a layer at index "+i+", because layer count is "+multiLayer.getLayerCount());
				} catch (IndexOutOfBoundsException ioobEx) {
					// this is correct;
				}
			} else {
				try {
					multiLayer.getLayer(i);
				} catch (IndexOutOfBoundsException ioobEx) {
					fail("MultiLayerMarkerSymbol threw an IndexOutOfBoundsException, but it has "+multiLayer.getLayerCount()+" layers");
				}
			}
		}
	}

	public void testMultiLayer_LINE_IndexOutOfBounds() {
		MultiLayerLineSymbol multiLayer = new MultiLayerLineSymbol();
		ILineSymbol layer1 = new SimpleLineSymbol();
		ILineSymbol layer2 = new SimpleLineSymbol();
		ILineSymbol layer3 = new SimpleLineSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);

		for (int i = -4; i < multiLayer.getLayerCount()+4; i++) {
			if (i<0 || i>=multiLayer.getLayerCount()) {
				try {
					multiLayer.getLayer(i);
					fail("MultiLayerLineSymbol should throw an IndexOutOfBoundsException when accessing a layer at index "+i+", because layer count is "+multiLayer.getLayerCount());
				} catch (IndexOutOfBoundsException ioobEx) {
					// this is correct;
				}
			} else {
				try {
					multiLayer.getLayer(i);
				} catch (IndexOutOfBoundsException ioobEx) {
					fail("MultiLayerLineSymbol threw an IndexOutOfBoundsException, but it has "+multiLayer.getLayerCount()+" layers");
				}
			}
		}
	}

	public void testMultiLayer_FILL_IndexOutOfBounds() {
		MultiLayerFillSymbol multiLayer = new MultiLayerFillSymbol();
		IFillSymbol layer1 = new SimpleFillSymbol();
		IFillSymbol layer2 = new SimpleFillSymbol();
		IFillSymbol layer3 = new SimpleFillSymbol();
		layer1.setDescription("layer1");
		layer2.setDescription("layer2");
		layer3.setDescription("layer3");

		multiLayer.addLayer(layer1);
		multiLayer.addLayer(layer2);
		multiLayer.addLayer(layer3);
		for (int i = -4; i < multiLayer.getLayerCount()+4; i++) {
			if (i<0 || i>=multiLayer.getLayerCount()) {
				try {
					multiLayer.getLayer(i);
					fail("MultiLayerFillSymbol should throw an IndexOutOfBoundsException when accessing a layer at index "+i+", because layer count is "+multiLayer.getLayerCount());
				} catch (IndexOutOfBoundsException ioobEx) {
					// this is correct;
				}
			} else {
				try {
					multiLayer.getLayer(i);
				} catch (IndexOutOfBoundsException ioobEx) {
					fail("MultiLayerFillSymbol threw an IndexOutOfBoundsException, but it has "+multiLayer.getLayerCount()+" layers");
				}
			}
		}

	}
}
