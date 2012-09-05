package org.gvsig.gpe.kml.parser.v21.kmz;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.gvsig.gpe.kml.parser.v21.kml.KMLBaseTest;

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
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera Llodr� (jorge.piera@iver.es)
 */
public abstract class KMZv21BaseTest extends KMLBaseTest{

	/* (non-Javadoc)
	 * @see org.gvsig.gpe.readers.GPEReaderBaseTest#getInputStream()
	 */
	public InputStream getInputStream() throws Exception {
		FileInputStream fis = new FileInputStream(getFile());
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry = null;
		while((entry = zis.getNextEntry()) != null){
			if (!entry.isDirectory()){
				ZipFile fz = new ZipFile(getFile());
				return fz.getInputStream(entry);
			}
		}
		throw new Exception();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.writers.GPEWriterBaseTest#getGPEParserClass()
	 */
	public Class getGPEParserClass() {
		return org.gvsig.gpe.kml.parser.GPEKmz2_1_Parser.class;
	}

}
