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
package com.iver.utiles;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;


/**
 * Clase de utilidad para Strings
 *
 * @author Fernando González Cortés
 */
public class StringUtilities {
    /**
     * Inserta una string en otra en la posición indicada
     *
     * @param base String donde se inserta
     * @param position posición de "base" donde se inserta la String
     * @param injerto String que se inserta en "base"
     *
     * @return String con la inserción hecha
     */
    public static String insert(String base, int position, String injerto) {
        return base.substring(0, position) + injerto +
        base.substring(position);
    }

    /**
     * Busca en la cadena si la posición se encuentra entre un símbolo de
     * apertura y su correspondiente símbolo de clausura
     *
     * @param string Cadena donde se busca
     * @param position posición que se está evaluando
     * @param startSymbol símbolo de apertura
     * @param endSymbol símbolo de clausura
     *
     * @return true si la posición dada está entre un símbolo de apertura y
     *         otro de clausura
     */
    public static boolean isBetweenSymbols(String string, int position,
        String startSymbol, String endSymbol) {
        TreeSet startSymbolIndexes = new TreeSet();
        TreeSet endSymbolIndexes = new TreeSet();
        boolean sameSymbolOpen = true;

        int pos = 0;

        //Itera sobre la cadena almacenando las posiciones de los símbolos de apertura hasta posición
        while ((pos = string.indexOf(startSymbol, pos)) < position) {
            if (pos == -1) {
                break;
            }

            startSymbolIndexes.add(new Integer(pos));
            pos++;
        }

        pos = 0;

        //Itera sobre la cadena almacenando las posiciones de los símbolos de clausura hasta posición
        while ((pos = string.indexOf(endSymbol, pos)) < position) {
            if (pos == -1) {
                break;
            }

            endSymbolIndexes.add(new Integer(pos));
            pos++;
        }

        Iterator startIndexesIterator = startSymbolIndexes.iterator();
        Iterator endIndexesIterator = endSymbolIndexes.iterator();
        Integer startIndex = null;
        Integer endIndex = null;
        int count = 0;

        while (startIndexesIterator.hasNext() || endIndexesIterator.hasNext()) {
            if (startIndexesIterator.hasNext()) {
                startIndex = (Integer) startIndexesIterator.next();
            }

            if (endIndexesIterator.hasNext()) {
                endIndex = (Integer) endIndexesIterator.next();
            }

            if (startIndex == null) {
                if (endIndexesIterator.hasNext()) {
                    endIndex = (Integer) startIndexesIterator.next();
                }

                count--;
            } else if (endIndex == null) {
                if (startIndexesIterator.hasNext()) {
                    startIndex = (Integer) startIndexesIterator.next();
                }

                count++;
            } else {
                if (endIndex.intValue() < startIndex.intValue()) {
                    if (endIndexesIterator.hasNext()) {
                        endIndex = (Integer) startIndexesIterator.next();
                    } else {
                        endIndex = null;
                    }

                    count--;
                } else {
                    if (startIndexesIterator.hasNext()) {
                        startIndex = (Integer) startIndexesIterator.next();
                    } else {
                        startIndex = null;
                    }

                    count++;
                }
            }
        }

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Busca en la cadena si la posición tiene un número par de symbol delante
     * de ella o impar.
     *
     * @param string Cadena donde se busca
     * @param position posición que se está evaluando
     * @param symbol símbolo que se toma para la comprobación
     *
     * @return true si hay un número impar de símbolos delante de la posición
     *         pos y false en caso contrario
     */
    public static boolean isBetweenSymbols(String string, int position,
        String symbol) {
        int pos = 0;

        int count = 0;

        while ((pos = string.indexOf(symbol, pos)) < position) {
            if (pos == -1) {
                break;
            }

            count = 1 - count;
            pos++;
        }

        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Obtiene una cadena delimitada por símbolos
     *
     * @param string Cadena de la que se obtendrá la subcadena
     * @param symbolSet Conjunto de símbolos delimitadores
     * @param position Posición a partir de la cual se busca la subcadena
     *
     * @return Cadena delimitada por cualquier combinación de los símbolos
     * definidos en el SimbolSet, o null si no se encuentra ninguna
     */
    public static String substringWithSymbols(String string,
        SymbolSet symbolSet, int position) {
        char[] characters = new char[string.length() - position];
        string.getChars(position, string.length(), characters, 0);

        for (int i = position; i < characters.length; i++) {
            if (symbolSet.contains(characters[i])) {
                char[] buff = new char[string.length() - i];
                int j = 0;

                while (symbolSet.contains(characters[i])) {
                    buff[j] = characters[i];
                    j++;
                    i++;

                    if (i == characters.length) {
                        break;
                    }
                }

                char[] ret = new char[j];
                System.arraycopy(buff, 0, ret, 0, j);

                return new String(ret);
            }
        }

        return null;
    }

    /**
     * Encuentra una cadena delimitada por otras dos
     *
     * @param string Cadena en la que se busca
     * @param start Cadena de inicio de la delimitación
     * @param end Cadena de final de la delimitación
     * @param startingPosition Posición en la que se empieza a buscar
     *
     * @return String cadena delimitada por start y por end
     */
    public static String substringDelimited(String string, String start,
        String end, int startingPosition) {
        int startIndex = string.indexOf(start, startingPosition);

        if (startIndex == -1) {
            return null;
        }

        startIndex += start.length();

        int endIndex = string.indexOf(end, startIndex);

        if ((startIndex < endIndex) && (endIndex != -1) && (startIndex != -1)) {
            return string.substring(startIndex, endIndex);
        }

        return null;
    }

    /**
     * Obtiene una rectángulo como String
     *
     * @param rect Rectángulo a transformar
     *
     * @return String
     */
    public static String rect2String(Rectangle2D rect) {
        return rect.getMinX() + "," + rect.getMinY() + "," + rect.getWidth() +
        "," + rect.getHeight();
    }

    /**
     * Convierte un String en un rectángulo. El string ha de haber sido
     * convertida a previamente desde un rectangulo mediante el método
     * rect2String
     *
     * @param rect String
     *
     * @return Rectangle2D
     */
    public static Rectangle2D string2Rect(String rect) {
        String[] coords = new String[4];
        coords = rect.split(",");

        Rectangle2D.Double ret = new Rectangle2D.Double(new Double(coords[0]).doubleValue(),
                new Double(coords[1]).doubleValue(),
                new Double(coords[2]).doubleValue(),
                new Double(coords[3]).doubleValue());

        return ret;
    }

    /**
     * Obtiene la representación de un color como String
     *
     * @param c Color
     *
     * @return String
     */
    public static String color2String(Color c) {
    	if (c == null) return null;
        return c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," +
        c.getAlpha();
    }

    /**
     * Obtiene el color de un string generado con color2String 
     *
     * @param stringColor string 
     *
     * @return Color
     */
    public static Color string2Color(String stringColor) {
    	if (stringColor == null || stringColor.equals("null")) return null;
        String[] ints = new String[4];
        ints = stringColor.split(",");

        int[] ret = new int[4];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = new Integer(ints[i]).intValue();
        }

        return new Color(ret[0], ret[1], ret[2], ret[3]);

        /*
           long color = new Long(stringColor).longValue();
           long alpha = color / 16777216;
           color = color % 16777216;
        
           long red = color / 65536;
           color = color % 65536;
           long green = color / 256;
           color = color % 256;
           long blue = color;
           return new Color(red, green, blue, alpha);*/
    }

    /*
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String floatArray2String(float[] array) {
        String aux = "" + array[0];

        for (int i = 1; i < array.length; i++) {
            aux += ("," + array[i]);
        }

        return aux;
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static float[] string2FloatArray(String array) {
        String[] floats = new String[4];
        floats = array.split(",");

        float[] ret = new float[floats.length];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = new Float(floats[i]).floatValue();
        }

        return ret;
    }

    /**
     * returns a list (comma-separated) in one unique string
     * 
     * 
     * @return 
     * @param input 
     */
        public static String getComaSeparated(String[] input) {        
            return getXSeparated("," , input);
        } 

    /**
     * returns a list (blanck-separated) in one unique string
     * 
     * 
     * @return 
     * @param input 
     */
        public static String getBlankSeparated(String[] input) {        
          return getXSeparated(" " , input);
        } 

    /**
     * returns a list (X-Character-separated) in one unique string
     * 
     * 
     * @return 
     * @param X 
     * @param input 
     */
        private static String getXSeparated(String X, String[] input) {        
            String output = "";
            if (input.length == 0) {
                return "";
            }
            output = input[0];
            for (int i = 1; i < input.length; i++)
                output = output + X + input[i];
            return output;
        } 

    /**
     * Replace a part of a String
     * 
     * 
     * @return 
     * @param str String to find the pattern
     * @param pattern Pattern to find
     * @param replace String to replace
     */
        public static String replace(String str, String pattern, String replace) {        
            int s = 0;
            int e = 0;
            StringBuffer result = new StringBuffer();
            while ((e = str.indexOf(pattern, s)) >= 0) {
                result.append(str.substring(s, e));
                result.append(replace);
                s = e + pattern.length();
            }
            result.append(str.substring(s));
            return result.toString();
        } 
        
        /**
         * Generates one random string
         * @param length
         * String length
         * @return
         */
        public static String generateRandomString(int length) {
    		int rnd;
    		final Random r = new Random();
    		final String sorigen = 
    			"ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    			"abcdefghijklmnopqrstuvwxyz";
    		final char fuente[] = sorigen.toCharArray();
    		char buffer[] = new char[length];
    		
    		for( int i=0; i < length; i++ ) {
    			rnd = Math.abs( r.nextInt() ) % fuente.length;
    			buffer[i] = fuente[rnd];
    		}
    		return( new String( buffer ) );
    	}

    /**
     * Returns the number of occurrences of a chain of characters in an String
     * 
     * @param str The string where find into
     * @param subStr The chain of characters that we want to know how many times occurs
     * @param caseSensitive <code>true</code> if has to do case sensitive each search or <code>false</code> if not
     * 
     * @return An integer value >= 0
     * 
     * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
     */
    public static int numberOfOccurrencesOfSubStringInString(String str, String subStr, boolean caseSensitive) {
    	int aux = 0;
    	int numberOfOccurrences = 0;
    	
    	if (!caseSensitive) {
    		str = str.toLowerCase();
    		subStr = subStr.toLowerCase();
    	}
    	
    	while (((aux = str.indexOf(subStr, aux)) != -1)) {
    		aux = aux + subStr.length();
    		numberOfOccurrences++;
    	}
    	
    	return numberOfOccurrences;
    }
    

    /**
     * Returns the number of occurrences of a chain of characters in an String, between a rank of positions
     * (If the subString finishes before 'end_position' -> don't count that occurrence)
     * 
     * @param str The string where find into
     * @param subStr The chain of characters that we want to know how many times occurs
     * @param caseSensitive <code>true<code> if has to do case sensitive at each search or <code>false</code> if not
     * @param start_position the initial position used by the search
     * @param end_position the final position used by the search 
     * 
     * @return An integer value >= 0
     * 
     * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
     */
    public static int numberOfOccurrencesOfSubStringInStringBetweenPositions(String str, String subStr, boolean caseSensitive, int start_position, int end_position) {
    	int aux = start_position;
    	int numberOfOccurrences = 0;
    	boolean finish = false;
    	
    	if (!caseSensitive) {
    		str = str.toLowerCase();
    		subStr = subStr.toLowerCase();
    	}
    	
    	while ((!finish) && ((aux = str.indexOf(subStr, aux)) != -1)) {
    		aux = aux + subStr.length();
    	
    		if (aux > end_position)
    			finish = true;
    		else
    			numberOfOccurrences++;
    	}
    	
    	return numberOfOccurrences;
    }
}
