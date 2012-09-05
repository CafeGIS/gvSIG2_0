/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
* $Id: AbstractSymbol.java 20989 2008-05-28 11:05:57Z jmvivo $
* $Log$
* Revision 1.8  2007-09-17 09:33:47  jaume
* some multishapedsymbol bugs fixed
*
* Revision 1.7  2007/08/13 11:36:50  jvidal
* javadoc
*
* Revision 1.6  2007/07/18 06:54:34  jaume
* continuing with cartographic support
*
* Revision 1.5  2007/05/22 10:05:31  jaume
* *** empty log message ***
*
* Revision 1.4  2007/03/26 14:24:13  jaume
* implemented Print
*
* Revision 1.3  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.2.2.2  2007/02/15 16:23:44  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2007/02/09 07:47:04  jaume
* Isymbol moved
*
* Revision 1.2  2007/01/24 17:58:22  jaume
* new features and architecture error fixes
*
* Revision 1.1  2007/01/10 16:31:36  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;


/**
 * Abstract class that implements the interface the interface for symbols.It is
 * considered as the father of all XXXSymbols and will implement all the methods that
 * these classes had not developed (and correspond with one of the methods of AbstractSymbol class)
 * @author  jaume dominguez faus - jaume.dominguez@iver.es
 */
public abstract class AbstractSymbol implements ISymbol, CartographicSupport{
	private String desc;
	private int unit = CartographicSupportToolkit.DefaultMeasureUnit;
	private int referenceSystem = CartographicSupportToolkit.DefaultReferenceSystem;

	private boolean isShapeVisible = true;

	public final void setDescription(String desc) {
		this.desc = desc;
	}

	public final String getDescription() {
		return desc;
	}

	/**
	 * @return
	 * @uml.property  name="isShapeVisible"
	 */
	public final boolean isShapeVisible() {
		return isShapeVisible;
	}

	/**
	 * Sets this symbol to visible
	 * @param isShapeVisible
	 */
	public final void setIsShapeVisible(boolean isShapeVisible) {
		this.isShapeVisible = isShapeVisible;
	}

	public void setUnit(int unitIndex) {
		this.unit = unitIndex;
	}

	public int getUnit() {
		return this.unit;
	}

	public int getReferenceSystem() {
		return this.referenceSystem;
	}

	public void setReferenceSystem(int system) {
		this.referenceSystem = system;

	}
	public boolean equals(Object obj){
		if (!obj.getClass().equals(getClass())){
			return false;
		}
		if (((ISymbol)obj).getOnePointRgb()!=getOnePointRgb()){
			return false;
		}
		if (getDescription()!=null && ((ISymbol)obj).getDescription()!=null){
			if (!((ISymbol)obj).getDescription().equals(getDescription())){
				return false;
			}
		}
		return true;
	}

}
