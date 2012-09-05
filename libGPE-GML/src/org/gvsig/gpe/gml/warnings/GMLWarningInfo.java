package org.gvsig.gpe.gml.warnings;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.exception.ListBaseException;
import org.gvsig.gpe.gml.exceptions.GMLExceptionList;

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
 * $Id: GMLWarningInfo.java 6 2007-02-28 11:49:30Z csanchez $
 * $Log$
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/01/15 13:08:06  csanchez
 * Sistema de Warnings y Excepciones adaptado a BasicException
 *
 * Revision 1.1  2006/12/22 11:25:04  csanchez
 * Nuevo parser GML 2.x para gml's sin esquema
 *
 *
 */
/************************************************************************
 * Class < GMLFileParseInfo >											*
 * 																		*
 * This Class gets all the warnings from the GML Parser, the GML parser	*
 * always tries parse the GML file even though it isn't a standard but	*
 * it has to info the user. 											*
 * 																		*
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)				*
 ************************************************************************/	
public class GMLWarningInfo {
	
	private ListBaseException warning;
		
	public GMLWarningInfo(){
		warning=new GMLExceptionList("", "", 0);
	}
	
	/**
	 * This method return true if there aren't warnings else returns false 
	 * @return boolean
	 */
	public boolean areWarnings(){
		if (warning.isEmpty()){
			return false;						
		}
		return true;
	}
	/**
	 * This method return the list of warnings parsing the GML 
	 * @return ArrayList of warnings
	 */
	public ListBaseException getGMLWarningList(){
		return warning;
	}
	
	/**
	 * This method inserts a new warning into the list 
	 * @args int new warning
	 */
	public void setElement(BaseException war){
		//Only we add a warning when it is new
		if (warning.contains(war)==false)
		{
			warning.add((war));
		}
	}
	/**
	 * This method returns a warning from the list 
	 * @args int index
	 * @return int warning
	 */
	public BaseException getElement(int index, ListBaseException war){
		//it returns the int value of a warning or 0 if it doesn't exists
		if (war.isEmpty()==false)
		{
			if (index < war.size()){
				return (BaseException)war.get(index);
			}
		}
		return null;
	}
	
	/**
	 * This method inserts a new warning list 
	 * @args int new warning
	 */
	public void setGMLWarningList(ListBaseException war){
		int i=0;
		BaseException info;
		while(i < war.size()){
			info = this.getElement(i,war);
			if (info != null)
			{
				this.setElement(info);
			}
			i++;
		}
	}
}
