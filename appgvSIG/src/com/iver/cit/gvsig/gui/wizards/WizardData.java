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
package com.iver.cit.gvsig.gui.wizards;

/**
 * DOCUMENT ME!
 *
 * @author Fernando González Cortés
 */
public class WizardData {
    private String title;
    private String Abstract;
    private LayerInfo layer;
    private String[] formats;

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getAbstract() {
        return Abstract;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String[] getFormats() {
        return formats;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public LayerInfo getLayer() {
        return layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setAbstract(String string) {
        Abstract = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param strings
     */
    public void setFormats(String[] strings) {
        formats = strings;
    }

    /**
     * DOCUMENT ME!
     *
     * @param info
     */
    public void setLayer(LayerInfo info) {
        layer = info;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }
}
