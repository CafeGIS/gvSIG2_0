package org.gvsig.gpe.writer;

import org.gvsig.gpe.parser.ICoordinateIterator;

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
 * $Id$
 * $Log$
 *
 */
/**
 * A sequence to iterate over the different parts of the
 * current geometry. 
 * <p>It is using on the writing process. The consumer application
 * has to implement this interface and send it to the writer
 * using the {@link GPEWriterHandler} interface. It is composed of
 * one {@link ICoordinatesIterator) and the {@link #getSize()} method
 * that is used in some writers to have a better performance
 * </p>
 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
 */
public interface ICoordinateSequence {
		
	/**
	 * Returns the size of the coordinates that the writer has to 
	 * write.
	 */
	public int getSize();
	 
	/**
	 * Returns the iterator necessary to retrieve all the coordinates
	 * that compose the geometry 
	 */
	public ICoordinateIterator iterator();
}
