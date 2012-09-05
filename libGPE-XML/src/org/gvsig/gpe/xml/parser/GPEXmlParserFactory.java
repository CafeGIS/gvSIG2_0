package org.gvsig.gpe.xml.parser;

import java.io.InputStream;
import java.io.OutputStream;

import org.gvsig.gpe.xml.stream.IXmlStreamReader;
import org.gvsig.gpe.xml.stream.IXmlStreamReaderFactory;
import org.gvsig.gpe.xml.stream.IXmlStreamWriter;
import org.gvsig.gpe.xml.stream.XmlStreamException;
import org.gvsig.gpe.xml.stream.kxml.KxmlXmlParserFactory;
import org.gvsig.gpe.xml.stream.stax.StaxXmlStreamWriter;


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
 * $Id: GPEXmlParserFactory.java 144 2007-06-07 14:53:59Z jorpiell $
 * $Log$
 * Revision 1.3  2007/06/07 14:53:30  jorpiell
 * Add the schema support
 *
 * Revision 1.2  2007/04/12 11:47:15  jorpiell
 * Add a getParser method
 *
 * Revision 1.1  2007/04/12 10:23:41  jorpiell
 * Add some writers and the GPEXml parser
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GPEXmlParserFactory {
	//private static ClassLoader classLoader = null;
	private static IXmlStreamReaderFactory factory;
	private static String parserName;
	/**
	 * Create a new parser from a name
	 * @param name
	 * GPEParser name
	 * @param contenHandler
	 * Application contenHandler usett to throw the parsing events
	 * @param errorHandler
	 * Application errror handler used to put errors and warnings
	 * @throws GPEParserCreationException  
	 */
	public static IXmlStreamReader getParser(final String mimeType, final InputStream in) throws XmlStreamException {
		return new KxmlXmlParserFactory().createParser(mimeType, in);
	}

	public static IXmlStreamWriter getWriter(String format,	OutputStream outputStream) throws XmlStreamException {
		// TODO Auto-generated method stub
		return new StaxXmlStreamWriter(outputStream);
	} 	
	
//	public static void setClassLoader(ClassLoader classLoader) {
//		GPEXmlParserFactory.classLoader = classLoader;
//	}
//    
//	private static Iterator availableParserFactories() {
//        if (classLoader != null){
//        	return sun.misc.Service.providers(IXmlStreamReaderFactory.class, classLoader);
//        }else{
//        	return sun.misc.Service.providers(IXmlStreamReaderFactory.class);
//        }       
//        
//    }
//    
//    /**
//     * @return the parser
//     * @throws XMLStreamException
//     */
//    public static IXmlStreamReader getParser(final String mimeType, final InputStream in) throws XmlStreamException {
//        Iterator parserFactories = availableParserFactories();
//        IXmlStreamReaderFactory factory;
//        while(parserFactories.hasNext()){
//            factory = (IXmlStreamReaderFactory) parserFactories.next();
//            if(factory.canParse(mimeType)){
//                return factory.createParser(mimeType, in);
//            }
//        }
//        //TODO update the GVSIG classpath to use SPI!!!
//        return new KxmlXmlParserFactory().createParser(mimeType, in);
//        //throw new XmlStreamException("ERROR: no xml parser factory found able to parse content type: " + mimeType);
//    }    
//    
//    private static Iterator availableWriterFactories() {
//        Iterator providers = sun.misc.Service.providers(IXmlStreamWriterFactory.class);
//        return providers;
//    }
//    
//    public static IXmlStreamWriter getWriter(final String mimeType, final OutputStream os) throws XmlStreamException, IllegalArgumentException
//    {
//    	Iterator writerFactories = availableWriterFactories();
//    	IXmlStreamWriterFactory factory;
//        while(writerFactories.hasNext()){
//            factory = (IXmlStreamWriterFactory) writerFactories.next();
//            if(factory.canWrite(mimeType)){
//                return factory.createWriter(mimeType, os);
//            }
//        }
//        //TODO update the GVSIG classpath to use SPI!!!
//        return new StaxXmlStreamWriter(os);
//        //throw new XmlStreamException("ERROR: no xml writer factory found able to write content type: " + mimeType);
//    }

}
