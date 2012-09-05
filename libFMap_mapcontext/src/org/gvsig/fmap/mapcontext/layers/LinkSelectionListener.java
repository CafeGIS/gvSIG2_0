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



/**
 * Listener que está pendiente de la selección que se haga sobre la tabla para
 * poder seleccionar de la tabla linkada los registros oportunos.
 *
 * @author Vicente Caballero Navarro
 * @deprecated TODO Esto tendría que estar en appgvSIG en las tablas
 */
public class LinkSelectionListener implements SelectionListener {
//	private SelectableDataSource modelo1;
//	private SelectableDataSource modelo2;
	private int index1;
	private int index2;

	/**
	 * Crea un nuevo LinkSelectionListener. Donde se pasan como parámetros dos
	 * SelectableDataSource, el primero de ellos(model1), representa la tabla
	 * sobre la que al seleccionar algún registro buscaremos en el modelo2
	 * para ver el valor del campo s1 y compararlo con el valor del campo s2 y
	 * de esta forma seleccionar instantanemente los registros oportunos en la
	 * tabla al que se representa con el modelo2.
	 *
	 * @param model1 SelectableDataSource de la tabla sobre la que se efectua
	 * 		  el link.
	 * @param model2 SelectableDataSource de la tabla linkada.
	 * @param s1 nombre del campo de la tabla donde se efectua el link.
	 * @param s2 nombre del campo de la tabla linkada.
	 */
//	public LinkSelectionListener(SelectableDataSource model1,
//		SelectableDataSource model2, String s1, String s2) {
//		modelo1 = model1;
//		modelo2 = model2;
//
//		try {
//			index1 = modelo1.getFieldIndexByName(s1);
//			index2 = modelo2.getFieldIndexByName(s2);
//		} catch (ReadDriverException e) {
//			e.printStackTrace();
//		}
//
//		//field1 = s1;
//		//field2 = s2;
//	}

	/**
	 * @see com.iver.cit.gvsig.fmap.layers.LegendListener#selectionChanged(com.iver.cit.gvsig.fmap.layers.LayerEvent)
	 */
	public void selectionChanged(SelectionEvent e) {
//		FBitSet bs1 = modelo1.getSelection();
//		FBitSet bs2 = new FBitSet(); //(FBitSet)modelo2.getSelection().clone();
//		HashMap idx = new HashMap(bs1.cardinality());
//		//int index1 = -1;
//		//int index2 = -1;
//
//		try {
//			//		Construimos el índice
//			for (int i = bs1.nextSetBit(0); i >= 0;
//					i = bs1.nextSetBit(i + 1)) {
//				Value v = modelo1.getFieldValue((long) i, index1);
//
//				if (idx.get(v) == null) {
//					idx.put(v, new Integer(i));
//				}
//			}
//		} catch (ReadDriverException e2) {
//			e2.printStackTrace();
//		}
//
//		try {
//			for (int i = 0; i < modelo2.getRowCount(); i++) {
//				Value v = modelo2.getFieldValue(i, index2);
//				Integer pi = (Integer) idx.get(v);
//
//				if (pi != null) {
//					bs2.set(i);
//				}
//			}
//		} catch (ReadDriverException e1) {
//			e1.printStackTrace();
//		}
//
//		// this applies the selection to the linked table
//		if (modelo1!=modelo2)
//			modelo2.setSelection(bs2);
//		//modelo2.fireSelectionEvents();
	}
}
