/* jdwglib. Java Library for reading Dwg files.
 * 
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 * 
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.util;

import java.text.StringCharacterIterator;

/**
 * This class allows to convert an Autocad text in an Unicode text
 * 
 * @author jmorell
 */
public class TextToUnicodeConverter {
    
    /**
     * This method allows to convert an Autocad text in an Unicode text
     * 
     * @param s Autocad text
     * @return String Unicode text
     */
	public static String convertText(String s) {
        StringCharacterIterator stringcharacteriterator = new StringCharacterIterator(s);
        StringBuffer stringbuffer = new StringBuffer();
        int ai[] = new int[s.length()];
        int i = 0;
        int j = 0;
        for(char c = stringcharacteriterator.first(); c != '\uFFFF'; c = stringcharacteriterator.next())
            if(c == '%')
            {
                c = stringcharacteriterator.next();
                if(c != '%')
                {
                    stringbuffer.append('%');
                    c = stringcharacteriterator.previous();
                } else
                {
                    c = stringcharacteriterator.next();
                    switch(c)
                    {
                    case 37: // '%'
                        stringbuffer.append('%');
                        break;

                    case 80: // 'P'
                    case 112: // 'p'
                        stringbuffer.append('\361');
                        break;

                    case 67: // 'C'
                    case 99: // 'c'
                        stringbuffer.append('\355');
                        break;

                    case 68: // 'D'
                    case 100: // 'd'
                        stringbuffer.append('\u00b0');
                        break;

                    case 85: // 'U'
                    case 117: // 'u'
                        ai[stringbuffer.length()] ^= 1;
                        i++;
                        break;

                    case 79: // 'O'
                    case 111: // 'o'
                        ai[stringbuffer.length()] ^= 2;
                        j++;
                        break;

                    default:
                        if(c >= '0' && c <= '9')
                        {
                            int k = 3;
                            char c1 = (char)(c - 48);
                            for(c = stringcharacteriterator.next(); c >= '0' && c <= '9' && --k > 0; c = stringcharacteriterator.next())
                                c1 = (char)(10 * c1 + (c - 48));

                            stringbuffer.append(c1);
                        }
                        c = stringcharacteriterator.previous();
                        break;
                    }
                }
            } else
            if(c == '^')
            {
                c = stringcharacteriterator.next();
                if(c == ' ')
                    stringbuffer.append('^');
            } else
            {
                stringbuffer.append(c);
            }
        s = Unicode.char2DOS437(stringbuffer, 2, '?');

		String ss = s;
		return ss;
	}
}
