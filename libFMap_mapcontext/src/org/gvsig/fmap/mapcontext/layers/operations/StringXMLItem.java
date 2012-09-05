package org.gvsig.fmap.mapcontext.layers.operations;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.iver.utiles.xmlViewer.TextXMLContent;

public class StringXMLItem extends TextXMLContent implements XMLItem {

	FLayer layer;
	/**
	 * @param text
	 */
	public StringXMLItem(String text, FLayer lyr) {
		super(text);
		this.layer = lyr;
		
	}
	
	public FLayer getLayer(){
		return this.layer;
	}
	/**
	 * @see com.iver.cit.gvsig.gui.toolListeners.InfoListener.XMLItem#parse(org.xml.sax.ContentHandler)
	 */
	public void parse(ContentHandler handler) throws SAXException {
		setContentHandler(new FilterContentHandler(handler));
		parse();
	}

	private class FilterContentHandler implements ContentHandler {

		private ContentHandler handler;

		public FilterContentHandler(ContentHandler handler) {
			this.handler = handler;
		}

		/**
		 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
		 */
		public void setDocumentLocator(Locator arg0) {
		}

		/**
		 * @see org.xml.sax.ContentHandler#startDocument()
		 */
		public void startDocument() throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#endDocument()
		 */
		public void endDocument() throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String,
		 *      java.lang.String)
		 */
		public void startPrefixMapping(String arg0, String arg1)
				throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
		 */
		public void endPrefixMapping(String arg0) throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
		 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		public void startElement(String arg0, String arg1, String arg2,
				Attributes arg3) throws SAXException {
			handler.startElement(arg0, arg1, arg2, arg3);
		}

		/**
		 * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
		public void endElement(String arg0, String arg1, String arg2)
				throws SAXException {
			handler.endElement(arg0, arg1, arg2);
		}

		/**
		 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
		 */
		public void characters(char[] arg0, int arg1, int arg2)
				throws SAXException {
			handler.characters(arg0, arg1, arg2);
		}

		/**
		 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int,
		 *      int)
		 */
		public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
				throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String,
		 *      java.lang.String)
		 */
		public void processingInstruction(String arg0, String arg1)
				throws SAXException {
		}

		/**
		 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
		 */
		public void skippedEntity(String arg0) throws SAXException {
		}

	}
}
