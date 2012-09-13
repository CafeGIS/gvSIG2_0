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
package com.iver.cit.gvsig.fmap.drivers.wcs;

import com.iver.andami.PluginServices;

/**
 * Excepci�n provocada por el WCS.
 *
 * @author Vicente Caballero Navarro
 */
public class WCSExceptionOld extends Exception {
	String message;
	
	public String getMessage() {
		return PluginServices.getText(this, "wcs_server_error")+"\n"+format(message, 200);
	}

	/**
	 *
	 */
	public WCSExceptionOld() {
		super();
	}

	/**
	 * Crea WCSException.
	 *
	 * @param message
	 */
	public WCSExceptionOld(String message) {
        super();
        this.message = message;
	}

	/**
	 * Crea WCSException.
	 *
	 * @param message
	 * @param cause
	 */
	public WCSExceptionOld(String message, Throwable cause) {
		super(format(message, 200), cause);
	}

	/**
	  * Crea WCSException.
	 *
	 * @param cause
	 */
	public WCSExceptionOld(Throwable cause) {
		super(cause);
	}
    
    /**
     * Cuts the message text to force its lines to be shorter or equal to 
     * lineLength.
     * @param message, the message.
     * @param lineLength, the max line length in number of characters.
     * @return the formated message.
     */
    private static String format(String message, int lineLength){
        if (message.length() <= lineLength) return message;
        String[] lines = message.split("\n");
        String theMessage = "";
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length()<lineLength)
                theMessage += line+"\n";
            else {
                String[] chunks = line.split(" ");
                String newLine = "";
                for (int j = 0; j < chunks.length; j++) {
                    int currentLength = newLine.length();
                    chunks[j] = chunks[j].trim();
                    if (chunks[j].length()==0)
                        continue;
                    if ((currentLength + chunks[j].length() + " ".length()) <= lineLength)
                        newLine += chunks[j] + " ";
                    else {
                        newLine += "\n"+chunks[j]+" ";
                        theMessage += newLine;
                        newLine = "";
                    }
                }
                
            }
        }
        return theMessage;
    }
}
