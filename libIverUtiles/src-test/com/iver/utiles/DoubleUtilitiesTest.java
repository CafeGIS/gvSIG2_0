package com.iver.utiles;

import junit.framework.TestCase;
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
 * $Id: DoubleUtilitiesTest.java 8515 2006-11-06 07:30:00Z jaume $
 * $Log$
 * Revision 1.2  2006-11-06 07:29:59  jaume
 * organize imports
 *
 * Revision 1.1  2006/05/16 13:02:41  jorpiell
 * Se ha añadido la clase DoubleUtiles, donde ha un metodo para limitar el tamaño de los decimales de un double y para quitar los "puntos" de la parte entera
 *
 *
 */
/**
 * Para probar los double utilities
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class DoubleUtilitiesTest extends TestCase {
	public void testDouble(){
		double numero = 1.36645345645;
		numero = DoubleUtilities.format(numero,".".charAt(0),4);
		assertEquals(numero,1.3665,0.00000000001);
	}
}
