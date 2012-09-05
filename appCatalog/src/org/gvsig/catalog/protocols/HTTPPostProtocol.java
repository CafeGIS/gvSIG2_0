
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
package org.gvsig.catalog.protocols;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

import org.gvsig.catalog.metadataxml.XMLTree;
import org.gvsig.catalog.utils.Strings;


/**
 * This class implement the HTTP Post protocol.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class HTTPPostProtocol implements IProtocols {

/**
 * @return 
 * @param url 
 * @param message 
 * @param firstRecord 
 */
    public Collection doQuery(URL url, Object message, int firstRecord) {        
        String body = (String) message;
        ByteArrayInputStream output = null;
            
        try {
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            
         c.setRequestProperty("SOAPAction","post");
         c.setRequestMethod("POST");
         c.setDoOutput(true);
         c.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                 
        // Write the request.
        OutputStreamWriter w =
            new OutputStreamWriter(c.getOutputStream(), "UTF-8");
        
        w.write(body);
        w.flush();
              
        InputStream is = c.getInputStream();
        byte[] buf = new byte[1024];
        int len;
        String str = "";
        
        while ((len = is.read(buf)) > 0) {
            str = str + new String(buf, 0, len);
        }
            
        str = Strings.replace(str,
				  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
		  "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        System.out.println(str);
        output = new ByteArrayInputStream(str.getBytes());
            
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            return null;
        } 
        
        Collection col = new java.util.ArrayList();
        col.add(XMLTree.xmlToTree(output));
        return col;            
    }    

    public void doQuery(URL url, Object message, int firstRecord, String fileName) {        
    	String body = (String) message;
    	FileOutputStream output = null;

    	try {
    		HttpURLConnection c = (HttpURLConnection) url.openConnection();

    		c.setRequestProperty("SOAPAction","post");
    		c.setRequestMethod("POST");
    		c.setDoOutput(true);
    		c.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");

    		// Write the request.
    		OutputStreamWriter w =
    			new OutputStreamWriter(c.getOutputStream(), "UTF-8");

    		w.write(body);
    		w.flush();

    		InputStream is = c.getInputStream();
    		byte[] buf = new byte[1024];
    		int len;
    		String str = "";
    		while ((len = is.read(buf)) > 0) {
    			str = str + new String(buf, 0, len);
    		}
	
    		System.out.println(str);
    		output = new FileOutputStream(new File(fileName));
    		output.write(str.getBytes());
    		output.flush();
    		output.close();
    	
    		
    	}  catch (IOException e) {
    		e.printStackTrace();           
    	}           
             
    }    
 }
