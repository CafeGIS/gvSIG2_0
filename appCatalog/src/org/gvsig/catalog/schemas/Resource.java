
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
import org.gvsig.catalog.querys.Coordinates;

/**
 * This class implements a geodata linked by a metadata
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Resource {
	
	/**
	 * 
	 * 
	 */
	public static final String WMS = "OGC:WMS";
	
	/**
	 * 
	 * 
	 */
	public static final String WFS = "OGC:WFS";
	
	/**
	 * 
	 * 
	 */
	public static final String WCS = "OGC:WCS";
	
	/**
	 * 
	 * 
	 */
	public static final String POSTGIS = "POSTGIS";
	
	/**
	 * 
	 * 
	 */
	public static final String ARCIMS_IMAGE = "ESRI:AIMS--HTTP-GET-IMAGE";
	
	/**
	 * 
	 * 
	 */
	public static final String ARCIMS_VECTORIAL = "ESRI:AIMS--HTTP-GET-FEATURE";
	
	/**
	 * 
	 * 
	 */
	public static final String WEBSITE = "WWW:LINK";
	
	/**
	 * 
	 * 
	 */
	public static final String DOWNLOAD = "WWW:DOWNLOAD";
	/**
	 * 
	 * 
	 */
	public static final String UNKNOWN = "UNKNOWN";
	
	/**
	 * 
	 * 
	 */
	private String linkage = null;
	
	/**
	 * 
	 * 
	 */
	private String protocol = null;
	
	/**
	 * 
	 * 
	 */
	private String name = null;
	
	/**
	 * 
	 * 
	 */
	private String description = null;
	
	/**
	 * 
	 * 
	 */
	private String function = null;
	
	/**
	 * 
	 * 
	 */
	private String srs = null;
	/**
	 * 
	 * 
	 */
	private Coordinates coordinates = null;
	/**
	 * 
	 * 
	 */
	private String Type = null;
	
	/**
	 * 
	 * 
	 * 
	 * @param linkage 
	 * @param protocol 
	 * @param name 
	 * @param description 
	 * @param function 
	 * @param srs 
	 * @param coordinates 
	 */
	public  Resource(String linkage, String protocol, String name, String description, String function, String srs, Coordinates coordinates) {        
		super();
		this.linkage = linkage;
		this.protocol = protocol;
		this.name = name;
		this.description = description;
		this.function = function;
		this.srs = srs;
		this.coordinates = coordinates;
		setResourceType();
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the description.
	 */
	public String getDescription() {        
		return description;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param description The description to set.
	 */
	public void setDescription(String description) {        
		this.description = description;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the function.
	 */
	public String getFunction() {        
		return function;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param function The function to set.
	 */
	public void setFunction(String function) {        
		this.function = function;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the linkage.
	 */
	public String getLinkage() {        
		return linkage;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param linkage The linkage to set.
	 */
	public void setLinkage(String linkage) {        
		this.linkage = linkage;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the name.
	 */
	public String getName() {        
		return name;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name) {        
		this.name = name;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the protocol.
	 */
	public String getProtocol() {        
		return protocol;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param protocol The protocol to set.
	 */
	public void setProtocol(String protocol) {        
		this.protocol = protocol;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the srs.
	 */
	public String getSrs() {        
		return srs;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param srs The srs to set.
	 */
	public void setSrs(String srs) {        
		this.srs = srs;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @return Returns the coordinates.
	 */
	public Coordinates getCoordinates() {        
		return coordinates;
	} 
	
	/**
	 * 
	 * 
	 * 
	 * @param coordinates The coordinates to set.
	 */
	public void setCoordinates(Coordinates coordinates) {        
		this.coordinates = coordinates;
	}
	/**
	 * 
	 * 
	 * 
	 * @return Returns the typr.
	 */
	public String getType() {
		return Type;
	}
	/**
	 * 
	 * 
	 * 
	 * @return Returns the type
	 */
	public void setType(String type) {
		Type = type;
	} 
	
	public void setResourceType(){
		try{
			if (getProtocol().toUpperCase().indexOf(Resource.WCS) >= 0){
				setType(Resource.WCS);
			}else if (getProtocol().toUpperCase().indexOf(Resource.WMS) >= 0){
				setType(Resource.WMS);
			}else if (getProtocol().toUpperCase().indexOf(Resource.WFS) >= 0){
				setType(Resource.WFS);
			}else if (getProtocol().toUpperCase().indexOf(Resource.POSTGIS) >= 0){
				setType(Resource.POSTGIS);
			}else if (getProtocol().toUpperCase().indexOf(Resource.WEBSITE) >= 0){
				setType(Resource.WEBSITE);
			}else if (getProtocol().toUpperCase().indexOf(Resource.DOWNLOAD) >= 0){
				setType(Resource.DOWNLOAD);
			}else if (getProtocol().toUpperCase().indexOf(Resource.ARCIMS_IMAGE) >= 0){
				setType(Resource.ARCIMS_IMAGE);
			}else if (getProtocol().toUpperCase().indexOf(Resource.ARCIMS_VECTORIAL) >= 0){
				setType(Resource.ARCIMS_VECTORIAL);
			}else{
				setType(Resource.UNKNOWN);
			}
		}catch(NullPointerException e){
			setType(Resource.UNKNOWN);
		}
	}
}
