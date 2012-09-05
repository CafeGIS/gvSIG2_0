package org.gvsig.gpe.kml.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.gvsig.gpe.kml.exceptions.KmlException;
import org.gvsig.gpe.kml.parser.profiles.Kml2_1_BindingProfile;

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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEKml2_1_Parser extends GPEDeafultKmlParser{

	public GPEKml2_1_Parser() {
		super();
		setProfile(new Kml2_1_BindingProfile());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getDescription()
	 */
	public String getDescription() {
		return "This parser parses KML 2.1";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getName()
	 */
	public String getName() {
		return "KML v2.1";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.parser.GPEParser#getFormat()
	 */
	public String getFormat() {
		return "text/xml; subtype=kml/2.1";		
	}
	
	/**
	 * It creates an InputStream. The Kml file can have the
	 * KML or the KMZ extension
	 * @return
	 * @throws KmlException
	 */
	protected InputStream createInputStream(File file){
		//KML
		try{
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			getErrorHandler().addError(e);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.GPEParser#accept(java.io.File)
	 */
	public boolean accept(URI uri) {
		if (uri.getPath().toUpperCase().endsWith("KML")){				
			return true;
		}
		return false;
	}		


}
