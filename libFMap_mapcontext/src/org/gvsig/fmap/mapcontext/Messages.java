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
package org.gvsig.fmap.mapcontext;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * Clase que accede a los recursos para la i18n
 */
public class Messages {
    /** DOCUMENT ME! */
    private static final String BUNDLE_NAME = "com.iver.cit.gvsig.fmap.FMap";

    /** DOCUMENT ME! */
    private static ResourceBundle RESOURCE_BUNDLE = null;

    /**
     * Inicializa la clase con el locale adecuado
     *
     * @param loc Locale de la aplicación
     */
    private static void init() {
        Locale loc = Locale.getDefault();
        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, loc);
    }

    
    /**
     * Obtiene el valor del recurso con clave 'key'
     *
     * @param key clave del recurso que se quiere obtener
     *
     * @return recurso que se quiere obtener o !key! en caso de no encontrarlo.
     *         En dicho caso no se notifica al framework ya que  estos son los
     *         mensajes propios de la aplicación y deben de estar todos
     */
    public static String getString(String key) {
        try {
            // La primera vez, cargamos las cadenas.
            if (RESOURCE_BUNDLE == null)
                init();
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
