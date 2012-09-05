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
package com.iver.cit.gvsig.project;

import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.utiles.XMLEntity;


/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class ProjectExtent {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(ProjectExtent.class);
	private Rectangle2D extent = new Rectangle2D.Double();
    private String description;

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public Envelope getExtent() {
        try {
			return geomManager.createEnvelope(extent.getX(),extent.getY(),extent.getMaxX(),extent.getMaxY(), SUBTYPES.GEOM2D);
		} catch (CreateEnvelopeException e) {
			logger.error("Error creating the envelope", e);
		}
		return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rectangle2D
     */
    public void setExtent(Rectangle2D rectangle2D) {
        extent = rectangle2D;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEncuadre() {
        return extent.getMinX() + "," + extent.getMinY() + "," +
        extent.getWidth() + "," + extent.getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @param encuadre DOCUMENT ME!
     */
    public void setEncuadre(String encuadre) {
        String[] coords = encuadre.split(",");
        extent = new Rectangle2D.Double(new Double(coords[0]).doubleValue(),
                new Double(coords[1]).doubleValue(),
                new Double(coords[2]).doubleValue(),
                new Double(coords[3]).doubleValue());
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLEntity getXMLEntity() {
        XMLEntity xml = new XMLEntity();
        xml.putProperty("description", description);
        xml.putProperty("extentX", extent.getX());
        xml.putProperty("extentY", extent.getY());
        xml.putProperty("extentW", extent.getWidth());
        xml.putProperty("extentH", extent.getHeight());

        return xml;
    }

     /**
     * DOCUMENT ME!
     *
     * @param xml DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ProjectExtent createFromXML(XMLEntity xml) {
        ProjectExtent pe = new ProjectExtent();
        pe.description = xml.getStringProperty("description");
        pe.extent.setRect(xml.getDoubleProperty("extentX"),
            xml.getDoubleProperty("extentY"), xml.getDoubleProperty("extentW"),
            xml.getDoubleProperty("extentH"));

        return pe;
    }

//	public int computeSignature() {
//		int result = 17;
//
//		Class clazz = getClass();
//		Field[] fields = clazz.getDeclaredFields();
//		for (int i = 0; i < fields.length; i++) {
//			try {
//				String type = fields[i].getType().getName();
//				if (type.equals("boolean")) {
//					result += 37 + ((fields[i].getBoolean(this)) ? 1 : 0);
//				} else if (type.equals("java.lang.String")) {
//					Object v = fields[i].get(this);
//					if (v == null) {
//						result += 37;
//						continue;
//					}
//					char[] chars = ((String) v).toCharArray();
//					for (int j = 0; j < chars.length; j++) {
//						result += 37 + (int) chars[i];
//					}
//				} else if (type.equals("byte")) {
//					result += 37 + (int) fields[i].getByte(this);
//				} else if (type.equals("char")) {
//					result += 37 + (int) fields[i].getChar(this);
//				} else if (type.equals("short")) {
//					result += 37 + (int) fields[i].getShort(this);
//				} else if (type.equals("int")) {
//					result += 37 + fields[i].getInt(this);
//				} else if (type.equals("long")) {
//					long f = fields[i].getLong(this) ;
//					result += 37 + (f ^ (f >>> 32));
//				} else if (type.equals("float")) {
//					result += 37 + Float.floatToIntBits(fields[i].getFloat(this));
//				} else if (type.equals("double")) {
//					long f = Double.doubleToLongBits(fields[i].getDouble(this));
//					result += 37 + (f ^ (f >>> 32));
//				} else {
//					Object obj = fields[i].get(this);
//					result += 37 + ((obj != null)? obj.hashCode() : 0);
//				}
//			} catch (Exception e) { e.printStackTrace(); }
//
//		}
//		return result;
//	}
}
