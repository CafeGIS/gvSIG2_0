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
* $Id: LabelingFactory.java 13913 2007-09-20 09:36:02Z jaume $
* $Log$
* Revision 1.9  2007-09-20 09:33:15  jaume
* Refactored: fixed name of IPersistAnce to IPersistence
*
* Revision 1.8  2007/05/22 12:17:41  jaume
* *** empty log message ***
*
* Revision 1.7  2007/05/22 10:05:31  jaume
* *** empty log message ***
*
* Revision 1.6  2007/04/13 11:59:30  jaume
* *** empty log message ***
*
* Revision 1.5  2007/03/28 16:48:01  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/20 16:16:20  jaume
* refactored to use ISymbol instead of FSymbol
*
* Revision 1.3  2007/03/09 11:20:57  jaume
* Advanced symbology (start committing)
*
* Revision 1.2  2007/03/09 08:33:43  jaume
* *** empty log message ***
*
* Revision 1.1.2.4  2007/02/21 07:34:08  jaume
* labeling starts working
*
* Revision 1.1.2.3  2007/02/12 15:15:20  jaume
* refactored interval legend and added graduated symbol legend
*
* Revision 1.1.2.2  2007/02/09 07:47:05  jaume
* Isymbol moved
*
* Revision 1.1.2.1  2007/02/02 16:21:24  jaume
* start commiting labeling stuff
*
* Revision 1.1.2.1  2007/01/30 18:10:45  jaume
* start commiting labeling stuff
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.legend.styling;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.*;

public class LabelingFactory {
	final static private Logger logger = LoggerFactory.getLogger(SymbologyFactory.class);
	private static Class defaultLabelingStrategy = AttrInTableLabelingStrategy.class;
	/**
	 * Given a layer, a labeling method, a label placements constraints, and a label
	 * zoom constraints it will figure out the best ILabelingStrategy that meets all
	 * the needs.
	 * @param layer, the target layer
	 * @param method, the desired methods
	 * @param placement, the desired placement constraints
	 * @param zoom, the desired zoom constraints
	 * @return ILabelingStrategy
	 */
	public static ILabelingStrategy createStrategy(FLayer layer,
			ILabelingMethod method, IPlacementConstraints placement,
			IZoomConstraints zoom) 	{
		if (method == null && placement == null && zoom == null)
			return createDefaultStrategy(layer);

		ILabelingStrategy strat = createDefaultStrategy(layer);
		if (method != null)
			strat.setLabelingMethod(method);
		if (placement != null)
			strat.setPlacementConstraints(placement);
		return strat;

	}

	/**
	 * Instantiates an object from an instance of XMLEntity. The XMLEntity
	 * must follow the XMLEntity contract in IPersistence. It at least must
	 * contain a className that allow create objects via instrospection. It
	 * also should have the set of attributes that the instantiated object's
	 * setXMLEntity(XMLEntity) method expects.
	 * @param xml
	 * @return
	 */
	private static Object createFromXML(XMLEntity xml) {
		String className = null;
		try {
			className = xml.getStringProperty("className");
		} catch (NotExistInXMLEntity e) {
			logger.error("Symbol class name not set.\n" +
					" Maybe you forgot to add the" +
					" putProperty(\"className\", yourClassName)" +
					" call in the getXMLEntity method of your symbol", e);
		}


		Class clazz = null;
		Object obj = null;
		try {
			clazz = Class.forName(className);
			obj = clazz.newInstance();
			((IPersistence) obj).setXMLEntity(xml);

		} catch (InstantiationException e) {
			logger.error("Trying to instantiate an interface" +
					" or abstract class + "+className, e);
		} catch (IllegalAccessException e) {
			logger.error(null, e);
		} catch (ClassNotFoundException e) {
			logger.error("No class called " + className +
					" was found.\nCheck the following.\n<br>" +
					"\t- The fullname of the class you're looking " +
					"for matches the value in the className " +
					"property of the XMLEntity ("+className+").\n<br>" +
					"\t- The jar file containing your symbol class is in" +
					"the application classpath<br>", e);
		} catch (XMLException e) {
			logger.error("Trying to load XML data"+className, e);
		}
		return obj;
	}

    /**
     * Creates a new instance of a ILabelingStrategy from an XMLEntity.
     * 
     * @param xml
     * @return ILabelingStrategy
     * @throws ReadException
     */
	public static ILabelingStrategy createStrategyFromXML(XMLEntity xml, FLayer lyr) throws ReadException {
		ILabelingStrategy str = (ILabelingStrategy) createFromXML(xml);
		str.setLayer(lyr);
		return str;
	}

	/**
	 * Creates a new instance of ILabelingMethod from an XMLEntity.
	 * @param xml
	 * @return ILabelingMethod
	 */
	public static ILabelingMethod createMethodFromXML(XMLEntity xml) {
		return (ILabelingMethod) createFromXML(xml);
	}


	/**
	 * Creates a default labeling strategy from an XMLEntity.
	 * @param layer
	 * @return an instance of DefaultStrategy
	 */
	public static ILabelingStrategy createDefaultStrategy(FLayer layer) {
		ILabelingStrategy st;
		try {
			st = (ILabelingStrategy) defaultLabelingStrategy.newInstance();
			st.setLayer(layer);
			return st;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public static void setDefaultLabelingStrategy(Class defaultLabelingStrategy) {
		LabelingFactory.defaultLabelingStrategy = defaultLabelingStrategy;
	}

	public static IPlacementConstraints createPlacementConstraintsFromXML(XMLEntity xml) {
		return (IPlacementConstraints) createFromXML(xml);
	}

	public static IZoomConstraints createZoomConstraintsFromXML(XMLEntity xml) {
		return (IZoomConstraints) createFromXML(xml);
	}

	public static LabelClass createLabelClassFromXML(XMLEntity xml) {
		return (LabelClass) createFromXML(xml);
	}
}
