package org.gvsig.gpe.parser;
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
 * $Id: IGPEErrorHandler.java 153 2007-06-20 09:35:37Z jorpiell $
 * $Log$
 * Revision 1.4  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.3  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.2  2007/04/12 17:06:42  jorpiell
 * First GML writing tests
 *
 * Revision 1.1  2007/04/11 08:46:21  csanchez
 * Actualizacion protoripo libGPE
 *
 *
 */
/**
 * This interface defines the GPE error handler. It has methods
 * to register the errors and the warnings that can happen
 * wherear a parser is reading or writing a file. 
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public interface IGPEErrorHandler {

	/**
	 * This method is invoked when a parser has found
	 * an error that has to stop the reading or the writing 
	 * process
	 * @param e
	 * The detected exception
	 */
	public void addError(Throwable e);
	
	/**
	 * This method is invoke when a parser has detected a
	 * error, but it is able to manage it by itself
	 * @param e
	 * The detected exception
	 */
	public void addWarning(Throwable e);
	
}
