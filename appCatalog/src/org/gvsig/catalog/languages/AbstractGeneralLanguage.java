
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
package org.gvsig.catalog.languages;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * All classes that implement a "Language" must to
 * inherit of this class
 * @author Jorge Piera Llodra (jorge.piera@iver.es)
 */
public abstract class AbstractGeneralLanguage implements ILanguages {
	public static final String EXACT_WORDS = "E";
	public static final String ANY_WORDS = "Y";
	public static final String ALL_WORDS = "A";
	public static final String AND = "And";
	public static final String OR = "Or";

	protected String currentQuery = null;
	protected String currentClause = null;

	/**
	 * Divide a phrase in lines
	 * @param concordancia If is 'E' (exact) don't divide
	 * @return Iteraror
	 * A set of words
	 * @param line phrase to search
	 * @param titleKeys 
	 */
	public Iterator parseValues(String line, String titleKeys){
		return parseValues(line, titleKeys, FilterEncoding.PROPERTY_IS_EQUALS_TO, null);
	}

	/**
	 * Divide a phrase in lines
	 *@param concordancia If is 'E' (exact) don't divide
	 * @return Iteraror
	 * A set of words
	 * @param line phrase to search
	 * @param titleKeys 
	 * @param relationship
	 * @param wildCard
	 */
	public Iterator parseValues(String line, String titleKeys, String relationship, String wildCard) {        
		Vector values = new Vector();

		if (titleKeys == null){
			titleKeys = EXACT_WORDS;
		}

		if (titleKeys.equals(EXACT_WORDS)) {
			values.add(line);
			return values.iterator();
		}
		StringTokenizer doubleQuotesTokenizer = new StringTokenizer(line, "\"",
				true);
		boolean inside = false;
		while (doubleQuotesTokenizer.hasMoreTokens()) {
			String token = doubleQuotesTokenizer.nextToken();
			if (token.equals("\"")) {
				inside = !inside;
			} else if (inside) {
				if (relationship.compareTo(FilterEncoding.PROPERTY_IS_LIKE) == 0){
					token = wildCard + token + wildCard;
				}
				values.add(token);
			} else {
				StringTokenizer spaceTokenizer = new StringTokenizer(token, " ");
				while (spaceTokenizer.hasMoreTokens()) {
					String value = spaceTokenizer.nextToken();
					if (relationship.compareTo(FilterEncoding.PROPERTY_IS_LIKE) == 0){
						value = wildCard + value + wildCard;
					}
					values.add(value);
				}
			}
		}
		return values.iterator();
	} 

	/**
	 * Return logic operators
	 * @return Or or And
	 * @param titleKeys E,A o Y --> Exact, All, anY
	 */
	public String getOperator(String titleKeys) {        
		if (titleKeys == null){
			titleKeys = EXACT_WORDS;
		} 
		if (titleKeys.equals(ANY_WORDS)) {
			return OR;
		} else {
			return AND;
		}
	} 
}
