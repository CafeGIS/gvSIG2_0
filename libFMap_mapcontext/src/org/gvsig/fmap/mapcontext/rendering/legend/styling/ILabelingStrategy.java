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
* $Id: ILabelingStrategy.java 13913 2007-09-20 09:36:02Z jaume $
* $Log$
* Revision 1.7  2007-09-20 09:33:15  jaume
* Refactored: fixed name of IPersistAnce to IPersistence
*
* Revision 1.6  2007/09/19 16:25:39  jaume
* ReadExpansionFileException removed from this context and removed unnecessary imports
*
* Revision 1.5  2007/05/17 09:32:06  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/26 14:40:07  jaume
* added print method
*
* Revision 1.3  2007/03/09 11:20:57  jaume
* Advanced symbology (start committing)
*
* Revision 1.2  2007/03/09 08:33:43  jaume
* *** empty log message ***
*
* Revision 1.1.2.4  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.1.2.2  2007/02/01 17:46:49  jaume
* *** empty log message ***
*
* Revision 1.1.2.1  2007/01/30 18:10:45  jaume
* start commiting labeling stuff
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.tools.task.Cancellable;

import com.iver.utiles.IPersistence;

/**
 * A LabelingStrategy is a way to define how the labels are painted in a map, or potentially other object implementing ILabelable. It contains methods for allowing the definition of labeling classes through the labeling method (see <b>ILabelingMethod</b>), the placement of such labels (see <b>IPlacementConstraints</b>), and the zoom properties (see <b>ZoomConstraints</b>)
 * @author   jaume dominguez faus - jaume.dominguez@iver.es
 */
public interface ILabelingStrategy extends IPersistence {

	/**
	 * Returns the labeling method currently in use. The labeling method handles
	 * a list of LabelClass that allows to handle several definition of labels
	 * in the layer.
	 * @return ILabelingMethod, the current one.
	 * @see ILabelingMethod
	 */
	public ILabelingMethod getLabelingMethod();

	/**
	 * Sets the labeling method that will be used the next time the the draw is invoked.
	 * @param   method, the new labeling method
	 */
	public void setLabelingMethod(ILabelingMethod method);


	/**
	 * Returns the current placement constraints that determine the position
	 * where the label is placed.
	 * @return
	 */
	public IPlacementConstraints getPlacementConstraints();

	/**
	 * Sets the PlacementConstraints that will determine where to place the labels. The change will take effect next time the draw(...) method is invoked.
	 * @param  constraints
	 */
	public void setPlacementConstraints(IPlacementConstraints constraints);

	/**
	 * Returns the current placement constraints that determine the position
	 * where the label is placed.
	 * @return
	 */
	public IZoomConstraints getZoomConstraints();

	/**
	 * Sets the PlacementConstraints that will determine where to place the labels. The change will take effect next time the draw(...) method is invoked.
	 * @param  constraints
	 */
	public void setZoomConstraints(IZoomConstraints constraints);

	/** Causes the labels to be drawn. The policy of process is determined by
	 * the LabelingStrategy previously set.
	 *
	 * @param mapImage
	 * @param mapGraphics
	 * @param viewPort
	 * @param cancel
	 * @param dpi TODO
	 * @throws ReadException
	 */
	public void draw(BufferedImage mapImage, Graphics2D mapGraphics, ViewPort viewPort,
			Cancellable cancel, double dpi) throws ReadException;

	/**
	 * Applies the printer properties to the rendering process to match its attributes.
	 * The result is manifested in the Graphics2D g which is the object sent to the printer.
	 * @param g
	 * @param viewPort
	 * @param cancel
	 * @param properties
	 * @throws ReadException
	 */
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel, PrintAttributes properties)
	throws ReadException;

	/**
	 * Returns a non-null String[] containing the names of the fields involved in the
	 * labeling. If this strategy contains more than one LabelClass the result is an
	 * array with all the names of the fields used by all the LabelClass, with no duplicates.
	 * @return
	 */
	public String[] getUsedFields();

	public void setLayer(FLayer layer) throws ReadException;

	public boolean shouldDrawLabels(double scale);

}
