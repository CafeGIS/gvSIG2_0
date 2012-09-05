package org.gvsig.gpe.containers;

import java.util.ArrayList;

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
 * $Id: Layer.java 81 2007-05-02 11:46:50Z jorpiell $
 * $Log$
 * Revision 1.3  2007/05/02 11:46:07  jorpiell
 * Writing tests updated
 *
 * Revision 1.2  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.1  2007/04/14 16:06:35  jorpiell
 * Add the container classes
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class Layer {
	private String name = null;
	private String description = null;
	private String id = null;
	private Layer parentLayer = null;
	private ArrayList features = new ArrayList();
	private ArrayList layers = new ArrayList();
	private String srs = null;
	private Bbox bbox = null;
	
	/**
	 * @return the bbox
	 */
	public Bbox getBbox() {
		return bbox;
	}

	/**
	 * @param bbox the bbox to set
	 */
	public void setBbox(Object bbox) {
		if (bbox != null){
			this.bbox = (Bbox)bbox;
		}
	}

	/**
	 * @return the srs
	 */
	public String getSrs() {
		return srs;
	}

	/**
	 * @param srs the srs to set
	 */
	public void setSrs(String srs) {
		this.srs = srs;
	}

	public Layer() {
		super();
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the features
	 */
	public ArrayList getFeatures() {
		return features;
	}
	
	/**
	 * Adds a new feature
	 * @param layer
	 */
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	/**
	 * Adds a new layer
	 * @param layer
	 */
	public void addLayer(Layer layer){
		layers.add(layer);
	}
	
	/**
	 * Adds a new layer
	 * @param layer
	 */
	public void addFeature(Object feature){
		if (feature instanceof Feature){
			features.add(feature);
		}
	}

	/**
	 * @return the parentLayer
	 */
	public Layer getParentLayer() {
		return parentLayer;
	}

	/**
	 * @param parentLayer the parentLayer to set
	 */
	public void setParentLayer(Object parentLayer) {
		if (parentLayer != null){
			this.parentLayer = (Layer)parentLayer;
		}
	}

	/**
	 * @return the layers
	 */
	public ArrayList getLayers() {
		return layers;
	}
	
	
	/**
	 * @return the layer at position i
	 * @param i
	 * Layer position
	 */
	public Layer getLayerAt(int i) {
		return (Layer)layers.get(i);
	}
}
