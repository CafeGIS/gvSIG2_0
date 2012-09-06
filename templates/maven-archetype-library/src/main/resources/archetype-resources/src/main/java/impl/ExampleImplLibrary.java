#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
* 2009 {{Company}}  {{Task}}
*/
package ${package}.impl;

import java.util.Locale;

import ${package}.ExampleLocator;
import org.gvsig.i18n.Messages;
import org.gvsig.tools.locator.BaseLibrary;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;

/**
 * Initialization of the Example Library Default implementation.
 * 
 * Registers implementations of the Library main services and also
 * the translations of the i18n texts of the Library.  
 * 
 * @author <a href="mailto:devel@domain">Name Surname</a>
 */
public class ExampleImplLibrary extends BaseLibrary {

    public void initialize() {
        super.initialize();
        
        /*
         * Register the ExampleManager default implementation.
         * Replace with the registration of your own implementations.
         */
        ExampleLocator.registerExampleManager(DefaultExampleManager.class);
    }
    
    @Override
    public void postInitialize() throws ReferenceNotRegisteredException {
    	// TODO: change Messages to always have a default locale.
        if (!Messages.hasLocales()) {
            Messages.addLocale(Locale.ENGLISH);
        }

        /*
         * Add the resource bundles of your library this way.
         * The family "${package}.i18n.text" is constructed
         * as the path to the resource bundle files as a package
         * (look at the src/main/resources/org/gvsig/example/i18n folder)
         * and the base name of those .properties files, usually "text",
         * so you have in the previous folders the files:
         * 
         * - text.properties (this is the default one for Spanish language).
         * - text_en.properties (translations for English language).
         * 
         * Translate your i18n texts at least to those two languages. You 
         * can translate to more languages by adding more text.properties
         * files, following the naming scheme of the Java Locale. 
         */
    	Messages.addResourceFamily(
                "${package}.i18n.text",
                ExampleImplLibrary.class.getClassLoader(),
                ExampleImplLibrary.class.getClass().getName());
    }

}