/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package com.iver.utiles;

import java.text.Collator;
import java.util.Comparator;

/**
 * Compares two chain of characters alphabetically
 *
 * @author Fernando Gonz�lez Cort�s
 * @author Pablo Piqueras Bartolom�
 */
public class StringComparator implements Comparator {
	protected boolean caseSensitive = true;
	protected LocaleRules localeRules = null;

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        String s1 = o1.toString();
        String s2 = o2.toString();

        // If localeRules is null -> use the default rules
        if (localeRules == null) {
        	if (caseSensitive) {
        		return s1.compareTo(s2);
        	}
        	else {
        		return s1.compareToIgnoreCase(s2);
        	}
        }
        else {
        	if (localeRules.isUseLocaleRules()) {
        		Collator collator = localeRules.getCollator();
        		
        		if (caseSensitive) {
        			return collator.compare(s1, s2);
        		}
        		else {
        			//return collator.compare(s1.toLowerCase(), s2.toLowerCase());
        			return collator.compare(s1.toUpperCase(), s2.toUpperCase());
        		}
        	}
        	else {
            	if (caseSensitive) {
            		return s1.compareTo(s2);
            	}
            	else {
            		return s1.compareToIgnoreCase(s2);
            	}
        	}
        }
    }

    /**
     * Returns if the comparator is sensitive to small and big letters
     *
     * @return
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Establece la sensibilidad del comparador a las mayusculas y minusculas
     *
     * @param b
     */
    public void setCaseSensitive(boolean b) {
        caseSensitive = b;
    }
    
    /**
     * Gets an object with the information for use the locale rules in comparation between strings. <br>
     * <ul>
     * <li>A boolean value -> if want or not use the locale rules</li>
     * <li>A reference to the locale rules</li>
     * </ul>
     * 
     * @return @see LocaleRules
     */
    public LocaleRules getLocaleRules() {
    	return localeRules;
    }    
    /**
     * Sets an object with the information for use the locale rules in comparation between strings. <br>
     * <ul>
     * <li>A boolean value -> if want or not use the locale rules</li>
     * <li>A reference to the locale rules</li>
     * </ul>
     * 
     * @param @see LocaleRules
     */
    public void setLocaleRules(LocaleRules locRules) {
    	localeRules = locRules;
    }
    
    /**
     * Represents the information needed by <i>StringComparator</i> for use or not locale-sensitive String comparison-rules in the <b><i>compare</i></b> method
     * 
     * @author Pablo Piqueras Bartolom�
     */
    public class LocaleRules {
    	 private boolean useLocaleRules;
    	 private Collator _collator;
    	 
    	 /**
    	  * Default constructor without parameters
    	  */
    	 public LocaleRules() {
    		 useLocaleRules = false;
    		 _collator = null;
    	 }
    	 
    	 /**
    	  * Default constructor with two parameters
    	  * 
    	  * @param b Use locale rules
    	  * @param collator A reference to an object configurated for locale-sensitive String comparison
    	  */
    	 public LocaleRules(boolean b, Collator collator) {
    		 useLocaleRules = b;
    		 _collator = collator;
    	 }
    	 
 		/**
 		 * Gets the value of the inner attribute <i>_collator</i>
 		 * 
 		 * @return Returns A reference to an object configurated for locale-sensitive String comparison
 		 */
 		public Collator getCollator() {
 			return _collator;
 		}

 		/**
 		 * Sets a value to the inner attribute <i>_collator</i>
 		 * 
 		 * @param collator A reference to an object configurated for locale-sensitive String comparison
 		 */
 		public void setCollator(Collator collator) {
 			this._collator = collator;
 		}

		/**
		 * Gets the value of the inner attribute <i>useLocaleRules</i>
		 * 
		 * @return Returns the useLocaleRules.
		 */
		public boolean isUseLocaleRules() {
			return useLocaleRules;
		}

		/**
		 * Sets a value to the inner attribute <i>useLocaleRules</i>
		 * 
		 * @param useLocaleRules The useLocaleRules to set.
		 */
		public void setUseLocaleRules(boolean useLocaleRules) {
			this.useLocaleRules = useLocaleRules;
		}
    }
}
