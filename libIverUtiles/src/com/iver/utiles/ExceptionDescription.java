/*
 * Created on 01-sep-2006
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * $Id: ExceptionDescription.java 20098 2008-04-14 08:42:53Z jmvivo $
 * $Log$
 * Revision 1.2  2006-09-22 08:08:04  ldiaz
 * layerName y driverName gestionados/almacenados desde esta clase
 *
 * Revision 1.1  2006/09/21 17:04:52  azabala
 * First version in cvs
 *
 *
 */
package com.iver.utiles;

/**
 * <p>Most exceptions produced in a layer, has common information (layer name, driver name, error description, error code).</p>
 * 
 * <p>An <code>ExceptionDescription</code> can provide this extra information describing the cause of the
 *  error, and more useful information to give to the user that works with a layer.<p>
 * 
 * @author azabala
 */
public abstract class ExceptionDescription {
	/**
	 * <p>Code which identifies the kind of error which is the cause of this exception.</p>
	 */
	private int errorCode;

	/**
	 * <p>A description about the error which produced this exception.</p>
	 */
	private String errorDescription;

	/**
	 * <p>If this exception was produced using a driver, this attribute describes that driver.</p>
	 */
	private String driverName;

	/**
	 * <p>If this exception was produced using a layer, this attribute describes that layer.</p> 
	 */
	private String layerName;
	
	/**
	 * <p>Gets the name of the driver (if a driver was using) which this exception was produced.</p>
	 * 
	 * @return the name of the driver (if a driver was using) which this exception was produced
	 */	
	public String getDriverName() {
		return driverName;
	}

	/**
	 * <p>Sets the name of the driver (if a driver was using) which this exception was produced.</p>
	 * 
	 * @param driverName the name of the driver (if a driver was using) which this exception was produced
	 */
	public void setDriverName(String driverName){
		this.driverName = driverName;
	}	

	/**
	 * <p>Gets the name of the layer (if a layer was using) with that this exception was produced.</p>
	 * 
	 * @return the name of the layer (if a layer was using) with that this exception was produced
	 */
	public String getLayerName() {
		return driverName;
	}

	/**
	 * <p>Sets the name of the layer (if a layer was using) with that this exception was produced.</p>
	 * 
	 * @param layerName the name of the layer (if a layer was using) with that this exception was produced
	 */
	public void setLayerName(String layerName){
		this.layerName = layerName;
	}	

	/**
	 * <p>Sets the name of the layer (if a layer was using) with that this exception was produced.</p>
	 * 
	 * @param layerName the name of the layer (if a layer was using) with that this exception was produced
	 */
	public ExceptionDescription() {
	}

	/**
	 * <p>Creates a new <code>ExceptionDescription</code> with the useful values initialized.</p>
	 * 
	 * @param errorCode code which identifies the kind of error which is the cause of this exception
	 * @param errorDescription description about the error which produced this exception
	 */
	public ExceptionDescription(int errorCode, String errorDescription) {
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}

	/**
	 * <p>Sets the code which identifies the kind of error which is the cause of this exception.</p>
	 * 
	 * @param errorCode code which identifies the kind of error which is the cause of this exception
	 */
	public void setCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * <p>Sets the description about the error which produced this exception.</p>
	 * 
	 * @param description description about the error which produced this exception
	 */
	public void setDescription(String description) {
		this.errorDescription = description;
	}

	/**
	 * <p>Gets the code which identifies the kind of error which is the cause of this exception.</p>
	 * 
	 * @return code which identifies the kind of error which is the cause of this exception
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * <p>Gets the description about the error which produced this exception.</p>
	 * 
	 * @return description about the error which produced this exception
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * <p>Returns a message that describes the error which produced this exception, formatted in HTML code.</p>
	 * 
	 * @return message that describes the error which produced this exception, formatted in HTML code
	 */
	public abstract String getHtmlErrorMessage();
}