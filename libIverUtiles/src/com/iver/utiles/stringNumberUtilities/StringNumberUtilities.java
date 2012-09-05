package com.iver.utiles.stringNumberUtilities;
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

/**
 * This class has methods for verify if an string is a number, and which kind of number.
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class StringNumberUtilities {
	
	/**
	 * Returns true if the word is a number; else returns false
	 * 
	 * @param word An string
	 * @return A boolean value
	 */	
	public static boolean isNumber(String word) {
		return isRealNumberWithRealExponent(word);
	}
	
	/**
	 * Returns true if the word is a natural number; else returns false
	 * 
	 * @param word An string
	 * @return A boolean value
	 */
	public static boolean isNaturalNumber(String word) {
		// Remove all spaces and tabs at beginning and end
		word = word.trim();
		
		// If no word
		if (word.length() == 0)
			return false;

		// If first symbol is '+'
		if (word.charAt(0) == '+')
			word = word.substring(1, word.length());
		
		if (word.length() == 0)
			return false;
		
		// Analyse the word
		for (int i = 0; i < word.length(); i++) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				default:
					return false;
			}
		}
		
		return true;
	}

	/**
	 * Returns true if the word is an integer number; else returns false
	 * 
	 * If it's a natural number, it's an integer number
	 * 
	 * @param word An string
	 * @return A boolean value
	 */
	public static boolean isIntegerNumber(String word) {
		// Remove all spaces and tabs at beginning and end
		word = word.trim();
		
		// If no word
		if (word.length() == 0)
			return false;

		// Try to extract the natural number
		if ((word.charAt(0) == '-') || (word.charAt(0) == '+'))
			word = word.substring(1, word.length());
		
		return isNaturalNumber(word);
	}
	
	/**
	 * Returns true if the word is a real number; else returns false
	 * It's supposed that '.' is the symbol for separate integer from decimal part
	 *
	 * If it's a natural or integer, it's a real number
	 * 
	 * @param word An string
	 * @return A boolean value
	 */
	public static boolean isRealNumber(String word) {
		// Remove all spaces and tabs at beginning and end
		word = word.trim();
		
		int numberOfPoints = 0;
		
		// If no word
		if (word.length() == 0)
			return false;
	
		// Try to remove the sign of the number
		if ((word.charAt(0) == '-') || (word.charAt(0) == '+'))
			word = word.substring(1, word.length());
		
		if (word.length() == 0)
			return false;
		
		// Analize the word
		for (int i = 0; i < word.length(); i++) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				case '.':
					// If there was another point -> fail
					if (numberOfPoints == 1)
						return false;
					else
						numberOfPoints ++;
					
					break;
				default:
					return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * Returns true if the word is a real number with or without the 'E' (or 'e') symbol for the exponent; else returns false <br>
	 * It's supposed that '.' is the symbol for separate integer from decimal part in the base. <br>
	 * The exponent must be an integer number
	 * 
	 * If it's a natural, integer or real number, it's a real number with integer exponent
	 *  
	 * @param word An string
	 * @return A boolean value
	 */
	public static boolean isRealNumberWithIntegerExponent(String word) {
		// Remove all spaces and tabs at beginning and end
		word = word.trim();
		
		int numberOfPoints = 0;
		
		// If no word
		if (word.length() == 0)
			return false;
	
		// Try to remove the sign of the number
		if ((word.charAt(0) == '-') || (word.charAt(0) == '+'))
			word = word.substring(1, word.length());

		if (word.length() == 0)
			return false;
		
		// Analize the word
		int i = 0;
//		for (int i = 0; i < word.length(); i++) {
		while ((i < word.length()) && (Character.toUpperCase(word.charAt(i)) != 'E')) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				case '.':
					// If there was another point -> fail
					if (numberOfPoints == 1)
						return false;
					else
						numberOfPoints ++;
					
					break;
				default:
					return false;
			}
			
			i++;
		}
		
		if (i == word.length())
			return true;
		
		numberOfPoints = 0;
		
		// Jump the symbol E
		i++;
		
		if (i == word.length())
			return false;

		// Try to remove the sign of the number
		if ((word.charAt(i) == '-') || (word.charAt(i) == '+')) {
			word = word.substring(++i, word.length());
			i = 0;
		}
		
		if (word.length() == 0)
			return false;
		
		while (i < word.length()) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				default:
					return false;
			}
			i++;
		}
		
		return true;
	}
	
	
	/**
	 * Returns true if the word is a real number with or without the 'E' (or 'e') symbol for the exponent; else returns false
	 * It's supposed that '.' is the symbol for separate integer from decimal part, in the base and the exponent of the number
	 * 
	 * If it's a natural, integer, real number or real number with integer exponent, it's a real number with real exponent
	 *  
	 * @param word An string
	 * @return A boolean value
	 */
	public static boolean isRealNumberWithRealExponent(String word) {
		// Remove all spaces and tabs at beginning and end
		word = word.trim();
		
		int numberOfPoints = 0;
		
		// If no word
		if (word.length() == 0)
			return false;
	
		// Try to remove the sign of the number
		if ((word.charAt(0) == '-') || (word.charAt(0) == '+'))
			word = word.substring(1, word.length());
		
		if (word.length() == 0)
			return false;
		
		// Analize the word
		int i = 0;
//		for (int i = 0; i < word.length(); i++) {
		while ((i < word.length()) && (Character.toUpperCase(word.charAt(i)) != 'E')) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				case '.':
					// If there was another point -> fail
					if (numberOfPoints == 1)
						return false;
					else
						numberOfPoints ++;
					
					break;
				default:
					return false;
			}
			
			i++;
		}
		
		if (i == word.length())
			return true;
		
		numberOfPoints = 0;
		
		// Jump the symbol E
		i++;
		
		if (i == word.length())
			return false;

		// Try to remove the sign of the number
		if ((word.charAt(i) == '-') || (word.charAt(i) == '+')) {
			word = word.substring(++i, word.length());
			i = 0;
		}
		
		if (word.length() == 0)
			return false;
		
		while (i < word.length()) {
			switch (word.charAt(i)) {
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					// do nothing (continue)
					break;
				case '.':
					// If there was another point -> fail
					if (numberOfPoints == 1)
						return false;
					else
						numberOfPoints ++;
					
					break;
				default:
					return false;
			}
			i++;
		}
		
		return true;
	}
}