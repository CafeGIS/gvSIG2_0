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
package org.gvsig.fmap.mapcontext.rendering.legend;

import java.awt.Color;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


/**
 * <p>VectorialIntervalLegend (which should be better called GraduatedColorLegend) is
 * a legend that allows to classify ranges of values using a color gradient based
 * on a value.<b>
 * </p>
 * <p>
 * The color gradient will be calculated according the starting color, the
 * ending color and the amount of intervals.
 * </p>
 * @author  Vicente Caballero Navarro
 */
public class VectorialIntervalLegend extends AbstractIntervalLegend {
    private Color startColor = Color.red;
    private Color endColor = Color.blue;
    /**
     * Constructor method
     */
    public VectorialIntervalLegend() {
        //defaultSymbol = LegendFactory.DEFAULT_POLYGON_SYMBOL;
    }

    /**
     * Constructor method
     *
     * @param type type of the shape.
     */
    public VectorialIntervalLegend(int type) {
        setShapeType(type);
    }

    public XMLEntity getXMLEntity() throws XMLException {
        XMLEntity xml = new XMLEntity();
        xml.putProperty("className", this.getClass().getName());
        xml.putProperty("useDefaultSymbolB", useDefaultSymbol);
        if (getDefaultSymbol() == null) {
            xml.putProperty("useDefaultSymbol", 0);
        } else {
            xml.putProperty("useDefaultSymbol", 1);
            xml.addChild(getDefaultSymbol().getXMLEntity());
        }

        xml.putProperty("fieldName", fieldNames[0]);
        xml.putProperty("index", index);

        xml.putProperty("intervalType", intervalType);
        xml.putProperty("numKeys", keys.size());

        if (keys.size() > 0) {
            xml.putProperty("tipoValueKeys", keys.get(0).getClass().getName());

            String[] sk = new String[keys.size()];

            for (int i = 0; i < keys.size(); i++) {
                sk[i] = ((IInterval) keys.get(i)).toString();
            }

            xml.putProperty("keys", getValues());

            for (int i = 0; i < keys.size(); i++) {
                xml.addChild(getSymbols()[i].getXMLEntity());
            }
        }

        xml.putProperty("shapeType", shapeType);

        xml.putProperty("startColor", StringUtilities.color2String(startColor));
        xml.putProperty("endColor", StringUtilities.color2String(endColor));

        ///xml.putProperty("numSymbols", symbols.size());
        ///xml.putProperty("indexs", getIndexs());
        ///xml.putProperty("values", getValues());
        return xml;
    }


    public void setXMLEntity(XMLEntity xml) {
        fieldNames = new String[] {xml.getStringProperty("fieldName")};
        index = xml.getIntProperty("index");

        if (xml.contains("intervalType")) { //TODO Esta condición es para poder cargar la versión 0.3, se puede eliminar cuando ya no queramos soportar esta versión.
            intervalType = xml.getIntProperty("intervalType");
        }
        useDefaultSymbol = xml.getBooleanProperty("useDefaultSymbolB");
        int hasDefaultSymbol = xml.getIntProperty("useDefaultSymbol");

        if (hasDefaultSymbol == 1) {
            setDefaultSymbol(SymbologyFactory.createSymbolFromXML(xml.getChild(0), null));
        } else {
            setDefaultSymbol(null);
        }

        int numKeys = xml.getIntProperty("numKeys");

        if (numKeys > 0) {
            String[] sk = xml.getStringArrayProperty("keys");
            IInterval auxInterval;

            for (int i = 0; i < numKeys; i++) {
                auxInterval = FInterval.create(sk[i]);
                symbols.put(auxInterval,
                		SymbologyFactory.createSymbolFromXML(xml.getChild(i + hasDefaultSymbol), null));
                keys.add(auxInterval);
                System.out.println("auxInterval =" + auxInterval + "Symbol =" +
                		SymbologyFactory.createSymbolFromXML(xml.getChild(i + hasDefaultSymbol), null)
                           .getDescription());
            }
        }
        if (xml.contains("shapeType")){
        	shapeType = xml.getIntProperty("shapeType");
        }

        startColor = StringUtilities.string2Color(xml.getStringProperty(
                    "startColor"));
        endColor = StringUtilities.string2Color(xml.getStringProperty(
                    "endColor"));
    }


    public ILegend cloneLegend() throws XMLException {
        return LegendFactory.createFromXML(getXMLEntity());
    }

    /**
	 * Returns the final color
	 * @return Color  final color.
	 * @uml.property  name="endColor"
	 */
    public Color getEndColor() {
        return endColor;
    }

    /**
	 * Inserts the final color.
	 * @param endColor final color.
	 * @uml.property  name="endColor"
	 */
    public void setEndColor(Color endColor) {
        this.endColor = endColor;
    }

    /**
	 * Returns the initial color.
	 * @return  Color initial color.
	 * @uml.property  name="startColor"
	 */
    public Color getStartColor() {
        return startColor;
    }

    /**
	 * Inserts the initial color
	 * @param startColor initial color.
	 * @uml.property  name="startColor"
	 */
    public void setStartColor(Color startColor) {
        this.startColor = startColor;
    }

	public int getShapeType() {
		return shapeType;
	}

	public void setShapeType(int shapeType) {
		if (this.shapeType != shapeType) {
			setDefaultSymbol(SymbologyFactory.
					createDefaultSymbolByShapeType(shapeType));
			this.shapeType = shapeType;
		}
	}


	public String getClassName() {
		return getClass().getName();
	}

	//
	/**
	 * <p>This method does not exist officially. But what it does is just apply the
	 * values of a VectorialIntervalLegend to other so, It is not needed to
	 * replicate the code of populating the legend in every panel that produces
	 * a legend by intervals.<br>
	 * </p>
	 * <p>
	 * ok this method is messy, but it's useful ;-)
	 * </p>
	 */
	public static void initializeVectorialIntervalLegend(
			AbstractIntervalLegend srcLegend,
			AbstractIntervalLegend targetLegend) {
		targetLegend.shapeType        = srcLegend.shapeType;
		targetLegend.symbols          = srcLegend.symbols;
		targetLegend.keys             = srcLegend.keys;
		targetLegend.index            = srcLegend.index;
		targetLegend.setClassifyingFieldNames(srcLegend.getClassifyingFieldNames());
		targetLegend.setClassifyingFieldTypes(srcLegend.getClassifyingFieldTypes());
//		targetLegend.fieldId          = srcLegend.fieldId;
		targetLegend.defaultSymbol    = srcLegend.defaultSymbol;
		targetLegend.featureStore       = srcLegend.featureStore;
		targetLegend.intervalType     = srcLegend.intervalType;
		targetLegend.useDefaultSymbol = srcLegend.useDefaultSymbol;
	}

}
