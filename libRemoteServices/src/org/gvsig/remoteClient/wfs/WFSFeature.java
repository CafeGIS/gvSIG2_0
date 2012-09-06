package org.gvsig.remoteClient.wfs;

import java.util.Hashtable;
import java.util.Vector;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.wfs.schema.XMLNameSpace;

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
 * $Id: WFSFeature.java 27504 2009-03-24 08:55:21Z jpiera $
 * $Log$
 * Revision 1.7  2007-09-20 09:30:12  jaume
 * removed unnecessary imports
 *
 * Revision 1.6  2007/02/09 14:11:01  jorpiell
 * Primer piloto del soporte para WFS 1.1 y para WFS-T
 *
 * Revision 1.5  2006/06/15 09:05:06  jorpiell
 * Actualizados los WFS
 *
 * Revision 1.4  2006/05/23 13:23:22  jorpiell
 * Se ha cambiado el final del bucle de parseado y se tiene en cuenta el online resource
 *
 * Revision 1.2  2006/04/20 16:39:16  jorpiell
 * Añadida la operacion de describeFeatureType y el parser correspondiente.
 *
 * Revision 1.1  2006/04/19 12:51:35  jorpiell
 * Añadidas algunas de las clases del servicio WFS
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSFeature {
	 private String	name = null;
	 private String	title = null;
	 private String	_abstract = null;
	 private Vector srs = new Vector();
	 private BoundaryBox latLonBbox = null;
	 private Hashtable bBoxes = new Hashtable();
	 private Vector fields = new Vector();
	 private Vector keywords = new Vector();
	 private XMLNameSpace namespace = null;
	 
	 public WFSFeature(){
		
	 }
	 
	 public WFSFeature(String name){
		 this.name = name;
	 }
	 
	/**
	 * @return Returns the fields.
	 */
	public Vector getFields() {
		return fields;
	}
	/**
	 * @param fields The fields to set.
	 */
	public void setFields(Vector fields) {
		this.fields = fields;
	}
	/**
	 * @return Returns the _abstract.
	 */
	public String getAbstract() {
		return _abstract;
	}
	/**
	 * @param _abstract The _abstract to set.
	 */
	public void setAbstract(String _abstract) {
		this._abstract = _abstract;
	}	
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		if ((title == null) ||
		(title.equals(""))){
			return name;
		}
		return title;
	}
	
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return Returns the latLonBbox.
	 */
	public BoundaryBox getLatLonBbox() {
		return latLonBbox;
	}
	/**
	 * @param latLonBbox The latLonBbox to set.
	 */
	public void setLatLonBbox(BoundaryBox latLonBbox) {
		this.latLonBbox = latLonBbox;
	}
	/**
	 * @return Returns the keywords.
	 */
	public Vector getKeywords() {
		return keywords;
	}
	
	public void addKeyword(String keyword){
		keywords.add(keyword);
	}
	/**
	 * @return Returns the srs.
	 */
	public Vector getSrs() {
		return srs;
	}
	
	public void addSRS(String key){
		if (!this.srs.contains(key))
    		srs.add(key);
	}
	
	 /**
     * <p>Adds a bbox to the Bboxes vector</p>
     * @param bbox
     */
    public void addBBox(BoundaryBox bbox) {
        bBoxes.put(bbox.getSrs(), bbox);
    } 
    
    /**
     * <p>returns the bbox with that id in the Bboxes vector</p> 
     * @param id 
     */
    public BoundaryBox getBbox(String id) {
    	return (BoundaryBox)bBoxes.get(id);
    }
	/**
	 * @return Returns the namespace.
	 */
	public XMLNameSpace getNamespace() {
		return namespace;
	}
	/**
	 * @param namespace The namespace to set.
	 */
	public void setNamespace(XMLNameSpace namespace) {
		this.namespace = namespace;
	} 
	
}    
