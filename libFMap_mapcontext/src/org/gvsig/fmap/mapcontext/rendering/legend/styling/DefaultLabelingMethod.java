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

/* CVS MESSAGES:
*
* $Id: DefaultLabelingMethod.java 10815 2007-03-20 16:16:20Z jaume $
* $Log$
* Revision 1.1  2007-03-20 16:16:20  jaume
* refactored to use ISymbol instead of FSymbol
*
* Revision 1.2  2007/03/09 08:33:43  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2007/02/01 11:42:47  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/01/30 18:10:45  jaume
* start commiting labeling stuff
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

public class DefaultLabelingMethod implements ILabelingMethod {


	private LabelClass defaultLabel;

	public DefaultLabelingMethod() {}

	public DefaultLabelingMethod(LabelClass defaultLabel) {
		this();
		this.defaultLabel = defaultLabel;
	}


	public void addLabelClass(LabelClass lbl) {
		defaultLabel = lbl;
	}


	public void deleteLabelClass(LabelClass lbl) {
		// does nothing
	}

	public String getClassName() {
		return getClass().getName();
	}

	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());

		if (defaultLabel !=null) {
			XMLEntity defaultLabelClassXML = defaultLabel.getXMLEntity();
			defaultLabelClassXML.putProperty("id", "defaultLabelClass");
			xml.addChild(defaultLabelClassXML);
		}
		return xml;
	}

	public void setXMLEntity(XMLEntity xml) {
		if (xml.findChildren("id", "defaultClass") !=null)
			defaultLabel = LabelingFactory.
				createLabelClassFromXML(xml.firstChild("id", "defaultLabelClass"));
	}


	public boolean allowsMultipleClass() {
		return false;
	}

	public void renameLabelClass(LabelClass lbl, String newName) {
		// does nothing
	}


	public FeatureSet getFeatureIteratorByLabelClass(FLyrVect layer, LabelClass lc, ViewPort viewPort, String[] usedFields)
	throws DataException {
		FeatureStore featureStore=layer.getFeatureStore();
		FeatureQuery featureQuery=featureStore.createFeatureQuery();
		featureQuery.setAttributeNames(usedFields);
		return (FeatureSet)featureStore.getFeatureSet(featureQuery);
//		layer.getSource().getFeatureIterator(
//					viewPort.getAdjustedExtent(), usedFields,
//					layer.getProjection(), true);
	}

	public boolean definesPriorities() {
		return false;
	}

	public void setDefinesPriorities(boolean flag) {
		/* nothing, since only one label class is suported */
	}
	public LabelClass getLabelClassByName(String labelName) {
		return defaultLabel;
	}

	public void clearAllClasses() {
		defaultLabel = null;
	}

	public LabelClass[] getLabelClasses() {
		return defaultLabel == null ? new LabelClass[0] : new LabelClass[] { defaultLabel };
	}

	public ILabelingMethod cloneMethod() {
		if (defaultLabel !=null)
			try {
				return new DefaultLabelingMethod(LabelingFactory.createLabelClassFromXML(defaultLabel.getXMLEntity()));
			} catch (XMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return new DefaultLabelingMethod();
	}
}
