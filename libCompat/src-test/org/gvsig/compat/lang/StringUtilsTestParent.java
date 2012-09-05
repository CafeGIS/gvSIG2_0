/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 {{Company}}   {{Task}}
 */
package org.gvsig.compat.lang;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Parent class for Unit tests for {@link org.gvsig.compat.lang.StringUtils}
 * implementations.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public abstract class StringUtilsTestParent extends TestCase {

    private StringUtils utils;

    protected void setUp() throws Exception {
        super.setUp();
        utils = createUtils();
    }

    /**
     * Return a new instance of an implementation of the StringUtils interface.
     * 
     * @return a new StringUtils instance
     */
    protected abstract StringUtils createUtils();

    /**
     * Test method for
     * {@link org.gvsig.compat.se.lang.StandardStringUtils#split(java.lang.String, java.lang.String)}
     * .
     */
    public void testSplitString() {
        // Check the result is the same as the returned with the String.split
        // method.
        List list1;
        List list2;

        String testString = "En un lugar de la Mancha";
        String regex = " ";

        list1 = Arrays.asList(new String[] { "En", "un", "lugar", "de", "la",
                "Mancha" });

        list2 = Arrays.asList(utils.split(testString, regex));

        assertEquals(list1, list2);

        list1 = Arrays.asList(new String[] { "En", "un lugar de la Mancha" });

        list2 = Arrays.asList(utils.split(testString, regex, 2));

        assertEquals(list1, list2);
    }

    /**
     * Test method for
     * {@link org.gvsig.compat.se.lang.StandardStringUtils#replaceAll(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public void testReplaceAll() {
        // Check the result is the same as the returned with the
        // String.replaceAll method.
        String testString = "En un lugar de la Mancha";
        String regex = " ";
        String replacement = "_";

        assertEquals("En_un_lugar_de_la_Mancha", utils
                .replaceAll(testString, regex, replacement));
        
        // Check replaceFirst
        assertEquals("En_un lugar de la Mancha", utils
        		.replaceFirst(testString, regex, replacement));
    }
}
