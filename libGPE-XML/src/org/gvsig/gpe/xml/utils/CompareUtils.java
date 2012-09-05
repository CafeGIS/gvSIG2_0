package org.gvsig.gpe.xml.utils;

import javax.xml.namespace.QName;

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
 * $Id: CompareUtils.java 101 2007-05-14 09:31:51Z jorpiell $
 * $Log$
 * Revision 1.1  2007/05/14 09:31:51  jorpiell
 * Class to compare tags
 *
 *
 */
/**
 * This class contains methods to compare XML tags
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 * @author Carlos Sánchez Periñán (csanchez@prodevelop.es)
 */
public class CompareUtils {
	
	/**
	 * Compare two XML tags
	 * @param value
	 * Original XML value with namespace
	 * @param tagName
	 * Tag name to compare without namespace
	 */
	public static boolean compareWithOutNamespace(QName value, QName tagName){		
		//THE TAG MUST BE DIFFERENT FROM NULL :P
		if (value!=null){
			return (value.getLocalPart().equals(tagName.getLocalPart()));			
		}
		return false;
	}
	
	/**
	 * Compare two XML tags
	 * @param value
	 * Original XML value with namespace
	 * @param tagName
	 * Tag name to compare namespace
	 */
	public static boolean compareWithNamespace(QName value, QName tagName){	
		if (value != null){
			if(value.equals(tagName)) {
				return true;
			}
		}
		return false;
//			else{
//				//if we want to compare two tags with match case. We need Collator PRIMARY  
//				Collator comparador = Collator.getInstance();
//				comparador.setStrength(Collator.PRIMARY);
//				// To skip a type error but the tag is equal. 
//				if(comparador.compare(value, tagName) == 0)
//					//WARNING HERE WITH AN ADVICE THAT THERE ARE A MISSPRINT
//					return true;
//				else 
//					return false;
//			}
//		}
//		else
//			return false;
	}
}

