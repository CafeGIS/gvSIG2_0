
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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

package org.gvsig.fmap.dal.feature;


/**
 * Abstract feature rule intended for giving a partial default implementation 
 * of the {@link FeatureRule} interface to other rule implementations. It is recommended
 * to extend this class when implementing new {@link FeatureRule}s.
 * 
 * @author Vicente Caballero Navarro
 */
public abstract class AbstractFeatureRule implements FeatureRule {
	protected String name;
	protected String description;
	protected boolean checkAtUpdate;
	protected boolean checkAtFinishEdition;

	protected AbstractFeatureRule(String name, String description) {
		this.init(name,description,true,true);
	}

	protected AbstractFeatureRule(String name, String description,
			boolean checkAtUpdate, boolean checkAtFinishEdition) {
		this.init(name, description, checkAtUpdate, checkAtFinishEdition);
	}

	protected void init(String name, String description, boolean checkAtUpdate,
			boolean checkAtFinishEdition) {
		this.name=name;
		this.description=description;
		this.checkAtUpdate = checkAtUpdate;
		this.checkAtFinishEdition = checkAtFinishEdition;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean checkAtFinishEditing() {
		return this.checkAtFinishEdition;
	}

	public boolean checkAtUpdate() {
		return this.checkAtUpdate;
	}

}
