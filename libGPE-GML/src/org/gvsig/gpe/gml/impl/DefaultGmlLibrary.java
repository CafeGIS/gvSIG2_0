/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.gpe.gml.impl;

import org.gvsig.gpe.GPELocator;
import org.gvsig.gpe.GPEManager;
import org.gvsig.gpe.exceptions.ParserNotRegisteredException;
import org.gvsig.gpe.exceptions.WriterHandlerNotRegisteredException;
import org.gvsig.gpe.gml.GmlLibrary;
import org.gvsig.gpe.gml.GmlProperties;
import org.gvsig.tools.locator.ReferenceNotRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DefaultGmlLibrary extends GmlLibrary{
	private static final Logger logger = LoggerFactory.getLogger(DefaultGmlLibrary.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#initialize()
	 */
	public void initialize() throws ReferenceNotRegisteredException {
        super.initialize();		
    }

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.tools.locator.BaseLibrary#postInitialize()
	 */
	public void postInitialize() {
		super.postInitialize();

		// Validate there is any implementation registered.
		GPEManager gpeManager = GPELocator.getGPEManager();
		if (gpeManager == null) {
			throw new ReferenceNotRegisteredException(
					GPELocator.GPE_MANAGER_NAME, GPELocator.getInstance());
		}
		
		gpeManager.setProperty(GmlProperties.DECIMAL, ".");
		gpeManager.setProperty(GmlProperties.COORDINATES_SEPARATOR, ",");
		gpeManager.setProperty(GmlProperties.TUPLES_SEPARATOR, " ");
		gpeManager.setProperty(GmlProperties.DEFAULT_FEATURECOLLECTION, "FeatureCollection");
		gpeManager.setProperty(GmlProperties.DEFAULT_FEATURE, "Feature");
		gpeManager.setProperty(GmlProperties.SRS_BASED_ON_XML, new Boolean(true));
		
		try {
			gpeManager.addGpeParser("GML", "Parser for GML", org.gvsig.gpe.gml.parser.GPEGmlSFP0Parser.class);
		} catch (ParserNotRegisteredException e) {
			logger.error("Impossible to register a GML parser");
		}
		try {			
			gpeManager.addGpeWriterHandler("GMLv2", "Writer for GML", org.gvsig.gpe.gml.writer.GPEGmlv2WriterHandlerImplementor.class);
			gpeManager.addGpeWriterHandler("GMLSFP0", "Writer for GML", org.gvsig.gpe.gml.writer.GPEGmlSFP0WriterHandlerImplementor.class);
		} catch (WriterHandlerNotRegisteredException e) {
			logger.error("Impossible to register a GML writer");

		}
	}
}

