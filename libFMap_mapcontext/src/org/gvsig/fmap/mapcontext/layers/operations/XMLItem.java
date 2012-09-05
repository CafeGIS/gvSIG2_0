package org.gvsig.fmap.mapcontext.layers.operations;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public interface XMLItem {
		public void parse(ContentHandler handler) throws SAXException;
		public FLayer getLayer();
}

