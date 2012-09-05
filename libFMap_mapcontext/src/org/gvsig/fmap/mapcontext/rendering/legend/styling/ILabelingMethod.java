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
* $Id: ILabelingMethod.java 13913 2007-09-20 09:36:02Z jaume $
* $Log$
* Revision 1.4  2007-09-20 09:33:15  jaume
* Refactored: fixed name of IPersistAnce to IPersistence
*
* Revision 1.3  2007/03/21 11:01:28  jaume
* javadoc
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
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.utiles.IPersistence;

/**
 * Interface that defines provides support to a ILabelingStrategy to supply
 * the set of <b>LabelClass</b>(es) that handle the label's style, content, etc..
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface ILabelingMethod extends IPersistence{

	/**
	 * Returns all the labels defined by this Labeling Method
	 * @return all the labels in this labeling method;
	 */
	public LabelClass[] getLabelClasses();

	/**
	 * Returns the LabelClass contained by this ILabelingMethod whose name matches
	 * with the string passed in <b>labelName</b>.
	 * @param labelName
	 * @return
	 */
	public LabelClass getLabelClassByName(String labelName);

	/**
	 * Adds a LabelClass to the set of LabelClass contained by this ILabelingMethod.
	 * @param lbl
	 */
	public void addLabelClass(LabelClass lbl);

	/**
	 * Removes the LabelClass from the set of LabelClasses contained by this
	 * ILabelingMethod.
	 * @param lbl, the LabelClass to be removed
	 */
	public void deleteLabelClass(LabelClass lbl);

	/**
	 * Returns <b>true</b> if this ILabelingMethod allos the definition of more
	 * LabelClasses than just the default LabelClass.
	 * @return boolean
	 */
	public boolean allowsMultipleClass();

	/**
	 * Changes the mane of a given LabelClass
	 * @param lbl, the LabelClass
	 * @param newName, the new name
	 */
	public void renameLabelClass(LabelClass lbl, String newName);

	public FeatureSet getFeatureIteratorByLabelClass(FLyrVect layer, LabelClass lc, ViewPort viewPort, String[] usedFields)
	throws DataException;

	public boolean definesPriorities();

	public void setDefinesPriorities(boolean flag);

	public void clearAllClasses();

	public ILabelingMethod cloneMethod();
}
