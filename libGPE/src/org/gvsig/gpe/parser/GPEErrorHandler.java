package org.gvsig.gpe.parser;

import java.util.ArrayList;

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
 * $Id: GPEErrorHandler.java 153 2007-06-20 09:35:37Z jorpiell $
 * $Log$
 * Revision 1.6  2007/06/20 09:35:37  jorpiell
 * Add the javadoc comments
 *
 * Revision 1.5  2007/05/16 09:27:24  jorpiell
 * Added two arrays to manage exceptions
 *
 * Revision 1.4  2007/04/19 07:23:20  jorpiell
 * Add the add methods to teh contenhandler and change the register mode
 *
 * Revision 1.3  2007/04/17 07:53:55  jorpiell
 * Before to start a new parsing process, the initialize method of the content handlers is throwed
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
 * This class is a common implementation for all
 * the application error handlers.
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (sanchez_carper@gva.es)
 */
public abstract class GPEErrorHandler implements IGPEErrorHandler {
	private ArrayList errors = null;
	private ArrayList warnings = null;
		
	public GPEErrorHandler() {
		super();
		errors = new ArrayList();
		warnings = new ArrayList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEErrorHandler#addError(java.lang.Throwable)
	 */
	public void addError(Throwable e) {
		errors.add(e);			
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gpe.IGPEErrorHandler#addWarning(java.lang.Throwable)
	 */
	public void addWarning(Throwable e) {
		warnings.add(e);		
	}

	/**
	 * @return the errors size
	 */
	public int getErrorsSize() {
		return errors.size();
	}

	/**
	 * @return the warnings size
	 */
	public int getWarningsSize() {
		return warnings.size();
	}
	
	/**
	 * Get a error
	 * @param i
	 * Error position
	 * @return
	 * The exception
	 */
	public Throwable getErrorAt(int i) {
		return (Throwable)errors.get(i);
	}

	/**
	 * Get a warning
	 * @param i
	 * Warning position
	 * @return
	 * The warning exception
	 */
	public Throwable getWarningAt(int i) {
		return (Throwable) warnings.get(i);
	}	

}
