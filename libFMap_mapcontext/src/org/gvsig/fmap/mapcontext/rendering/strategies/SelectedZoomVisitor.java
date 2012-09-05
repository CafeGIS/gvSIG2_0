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
package org.gvsig.fmap.mapcontext.rendering.strategies;

import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.operations.LayersVisitor;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.visitor.NotSupportedOperationException;
import org.gvsig.tools.visitor.Visitable;
import org.gvsig.tools.visitor.Visitor;


/**
 * Visitor de zoom a lo seleccionado.
 *
 * @author Vicente Caballero Navarro
 */
public class SelectedZoomVisitor implements LayersVisitor, Visitor {
	private Envelope rectangle = null;

	/**
	 * Devuelve el Extent de los shapes seleccionados, y si no hay ningún shape
	 * seleccionado devuelve null.
	 *
	 * @return Extent de los shapes seleccionados.
	 */
	public Envelope getSelectBound() {
		return rectangle;
	}

	public String getProcessDescription() {
		return "Defining rectangle to zoom from selected geometries";
	}

	public void visit(Feature feature) throws BaseException {
		if (rectangle == null) {
			rectangle = (feature.getDefaultGeometry()).getEnvelope();
		} else {
			rectangle.add((feature.getDefaultGeometry())
					.getEnvelope());
		}
	}

	public void visit(FeatureReference featureRefence) throws BaseException {
		this.visit(featureRefence.getFeature());
	}

	public void visit(Object obj) throws BaseException {
		if (obj instanceof FeatureReference) {
			this.visit((FeatureReference) obj);
			return;
		}
		if (obj instanceof Feature) {
			this.visit((Feature) obj);
			return;
		}
		if (obj instanceof FLayer) {
			this.visit((FLayer) obj);
			return;
		}

		throw new NotSupportedOperationException(this, obj);
	}

	public void visit(FLayer layer) throws BaseException {
		if (!(layer instanceof SingleLayer)) {
			return;
		} else if (!layer.isActive()) {
			return;
		}
		DataSet selection = ((SingleLayer) layer).getDataStore()
				.getSelection();

		if (selection instanceof Visitable) {
			((Visitable) selection).accept(this);
		}
	}


}
