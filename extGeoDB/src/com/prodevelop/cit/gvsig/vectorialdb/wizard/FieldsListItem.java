/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import javax.swing.JCheckBox;

import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;


/**
 * Used to instantiate field list items. Shows the database dependent type name.
 *
 * @author jldominguez
 *
 */
public class FieldsListItem extends JCheckBox {
    private String name = "[No name]";
    private String type = "[No type]";

    public FieldsListItem(FeatureAttributeDescriptor descriptor) {

    	name = descriptor.getName();
        type = descriptor.getDataTypeName();
        setText(toString());
        setSelected(true);
    }

    public String toString() {
        return name + " [" + type + "]";
    }

    public String getName() {
        return name;
    }
}
