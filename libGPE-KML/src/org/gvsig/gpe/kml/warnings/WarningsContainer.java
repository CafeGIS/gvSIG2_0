package org.gvsig.gpe.kml.warnings;

import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.exception.ListBaseException;


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
 * $Id: WarningsContainer.java 38 2007-04-12 10:21:52Z jorpiell $
 * $Log$
 * Revision 1.1  2007/04/12 10:21:52  jorpiell
 * Add the writers
 *
 * Revision 1.1  2007/03/07 08:19:10  jorpiell
 * Pasadas las clases de KML de libGPE-GML a libGPE-KML
 *
 * Revision 1.1  2007/02/28 11:48:31  csanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/02/20 10:53:20  jorpiell
 * AÃ±adidos los proyectos de kml y gml antiguos
 *
 * Revision 1.1  2007/02/12 13:49:18  jorpiell
 * Añadido el driver de KML
 *
 *
 */
/**
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WarningsContainer implements IWarningsContainer{
	private ListBaseException warning = null;
	
	public WarningsContainer(){
		warning = new WarningList("","",0);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.kml.warnings.IWarningsContainer#areWarnings()
	 */
	public boolean areWarnings() {
		if (warning.isEmpty()){
			return false;						
		}
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.kml.warnings.IWarningsContainer#getWarningList()
	 */
	public ListBaseException getWarningList() {
		return warning;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.kml.warnings.IWarningsContainer#setElement(org.gvsig.tools.exception.BaseException)
	 */
	public void setElement(BaseException war) {
//		Only we add a warning when it is new
		if (warning.contains(war)==false)
		{
			warning.add((war));
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.kml.warnings.IWarningsContainer#getElement(int, org.gvsig.exceptions.ListBaseException)
	 */
	public BaseException getElement(int index, ListBaseException war) {
//		it returns the int value of a warning or 0 if it doesn't exists
		if (war.isEmpty()==false)
		{
			if (index < war.size()){
				return (BaseException)war.get(index);
			}
		}
		return null;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.remoteClient.kml.warnings.IWarningsContainer#setWarningList(org.gvsig.exceptions.ListBaseException)
	 */
	public void setWarningList(ListBaseException war) {
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
