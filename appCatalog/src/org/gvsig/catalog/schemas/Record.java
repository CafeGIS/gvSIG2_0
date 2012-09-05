
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
package org.gvsig.catalog.schemas;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Collection;

import org.gvsig.catalog.metadataxml.XMLNode;


/**
 * All classes that implement a tag-parser must to implement this
 * class. It only has some fields to show in the application form.
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public abstract class Record {
	private XMLNode node;
	private String title;
	private String abstract_;
	private String purpose;
	private String[] keyWords;
	private BufferedImage image;
	private String imageURL;
	private String fileID;
	private URI uri;
	private Collection resources = new java.util.ArrayList();

	public Record(URI uri, XMLNode node){
		this.uri = uri;
		this.node = node;
	}
	
	public Record(){
		
	}
	
	/**
	 * Return true is the XML node is from a type that can be
	 * parsed
	 * @param uri
	 * Server uri
	 * @param node
	 * XML node
	 * @return
	 * true if the file can be managed or false
	 */	
	public abstract boolean accept(URI uri, XMLNode node);
	
	/**
	 * @return Returns the fileID.
	 */
	public String getFileID() {        
		return fileID;
	} 
	

	/**
	 * @param fileID The fileID to set.
	 */
	public void setFileID(String fileID) {        
		this.fileID = fileID;
	} 

	/**
	 * @return Returns the image.
	 */
	public BufferedImage getImage() {        
		return image;
	} 

	/**
	 * @param image The image to set
	 */
	public void setImage(BufferedImage image){
		this.image = image;
	}	
	
	/**
	 * @param image The image address to set.
	 */
	public void setImageURL(String imageURL) {        
		this.imageURL = imageURL;
	} 

	/**
	 * @return Returns the abstract_.
	 */
	public String getAbstract_() {        
		return abstract_;
	} 

	/**
	 * @param abstract_ The abstract_ to set.
	 */
	public void setAbstract_(String abstract_) {        
		this.abstract_ = abstract_;
	} 

	/**
	 * @return Returns the keyWords.
	 */
	public String[] getKeyWords() {        
		return keyWords;
	} 

	/**
	 * @param keyWords The keyWords to set.
	 */
	public void setKeyWords(String[] keyWords) {        
		this.keyWords = keyWords;
	} 

	/**
	 * @return Returns the purpose.
	 */
	public String getPurpose() {        
		return purpose;
	} 

	/**
	 * @param purpose The purpose to set.
	 */
	public void setPurpose(String purpose) {        
		this.purpose = purpose;
	} 

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {        
		return title;
	} 

	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {        
		this.title = title;
	} 

	/**
	 * @return Returns the node.
	 */
	public XMLNode getNode() {        
		return node;
	} 

	/**
	 * @param node The node to set.
	 */
	public void setNode(XMLNode node) {        
		this.node = node;
	} 

	/**
	 * @return Returns the resources.
	 */
	public Resource[] getResources() {        
		Resource[] aux = new Resource[resources.size()];
		for (int i=0 ; i<aux.length ; i++){
			aux[i] = (Resource)resources.toArray()[i];
		}
		return aux;
	} 

	/**
	 * @param resources The resources to set.
	 */
	public void setResources(Resource[] resources) {        
		this.resources = new java.util.ArrayList();
		if (resources == null){
			return;
		}
		for (int i=0 ; i<resources.length ; i++){
			this.resources.add(resources[i]);
		}        
	} 

	/**
	 * @return Returns the serverURL.
	 */
	public URI getURI() {        
		return uri;
	} 

	/**
	 * 
	 * 
	 * 
	 * @param serverURI The serverURL to set.
	 */
	public void setURI(URI uri) {        
		this.uri = uri;
	}

	public String getImageURL() {
		return imageURL;
	} 
	
}
