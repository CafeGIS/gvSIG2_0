package org.gvsig.catalog.utils;

import java.awt.Dimension;

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
 * $Id: CatalogConstants.java 602 2007-09-24 11:55:02 +0000 (Mon, 24 Sep 2007) jpiera $
 * $Log$
 * Revision 1.1.2.4  2007/07/24 09:45:52  jorpiell
 * Fix some interface bugs
 *
 * Revision 1.1.2.3  2007/07/23 07:14:25  jorpiell
 * Catalog refactoring
 *
 * Revision 1.1.2.2  2007/07/11 13:01:50  jorpiell
 * Catalog UI updated
 *
 * Revision 1.1.2.1  2007/07/10 11:18:04  jorpiell
 * Added the registers
 *
 *
 */
/**
 * This class has some constants for the catalog client
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class CatalogConstants {
	public static final int BUTTON_LENGHT = 80;
	public static final int BUTTON_WIDTH = 23;
	public static final Dimension BUTTON_SIZE = new Dimension(BUTTON_LENGHT,BUTTON_WIDTH);
	public static final int RESIZE_WINDOW_BUTTON_SIZE = 30;
	
	//Action commands
	public static final String CLOSE_BUTTON_ACTION_COMMAND = "close";
	public static final String NEXT_BUTTON_ACTION_COMMAND = "next";
	public static final String LAST_BUTTON_ACTION_COMMAND = "last";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancel";
	public static final String SEARCH_BUTTON_ACTION_COMMAND = "search";
	public static final String PROTOCOL_COMBO_ACTION_COMMAND = "protocolCombo";
	public static final String SERVER_COMBO_ACTION_COMMAND = "serverCombo";
	public static final String RESIZE_BUTTON_ACTION_COMMAND = "resize";
	public static final String CONNECT_BUTTON_ACTION_COMMAND = "connect";
	public static final String SERVERPROPERTIES_BUTTON_ACTION_COMMAND = "serverProperties";

	//conordancia
	public static final String EXACT_WORDS = "E";
	public static final String ANY_WORD = "Y";
	public static final String ALL_WORDS = "A";
	public static final String OR = "Or";
	public static final String AND = "And";
	
	//Connection 
	public static final int GET = 0;
	public static final int POST = 1;
	public static final int SOAP = 2;
	public static final int Z3950 = 4;
	
	//XML
	public static final String XML_HEADER_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String XML_NS = "xmlns";

}
