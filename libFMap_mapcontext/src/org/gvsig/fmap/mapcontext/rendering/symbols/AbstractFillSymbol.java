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
* $Id: AbstractFillSymbol.java 21071 2008-06-02 10:55:35Z vcaballero $
* $Log$
* Revision 1.10  2007-09-19 16:20:45  jaume
* removed unnecessary imports
*
* Revision 1.9  2007/08/09 10:39:41  jaume
* first round of found bugs fixed
*
* Revision 1.8  2007/03/28 16:48:14  jaume
* *** empty log message ***
*
* Revision 1.7  2007/03/21 11:37:00  jaume
* *** empty log message ***
*
* Revision 1.6  2007/03/13 16:58:36  jaume
* Added QuantityByCategory (Multivariable legend) and some bugfixes in symbols
*
* Revision 1.5  2007/03/09 11:20:56  jaume
* Advanced symbology (start committing)
*
* Revision 1.3.2.2  2007/02/21 16:09:02  jaume
* *** empty log message ***
*
* Revision 1.3.2.1  2007/02/16 10:54:12  jaume
* multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
*
* Revision 1.3  2007/01/24 17:58:22  jaume
* new features and architecture error fixes
*
* Revision 1.2  2007/01/11 12:17:34  jaume
* *** empty log message ***
*
* Revision 1.1  2007/01/10 16:31:36  jaume
* *** empty log message ***
*
* Revision 1.6  2006/12/04 17:13:39  fjp
* *** empty log message ***
*
* Revision 1.5  2006/11/14 11:10:27  jaume
* *** empty log message ***
*
* Revision 1.4  2006/11/09 18:39:05  jaume
* *** empty log message ***
*
* Revision 1.3  2006/11/06 07:33:54  jaume
* javadoc, source style
*
* Revision 1.2  2006/10/31 16:16:34  jaume
* *** empty log message ***
*
* Revision 1.1  2006/10/30 19:30:35  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.Color;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.ViewPort;



/**
 * Abstract class that any FILL SYMBOL should extend
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public abstract class AbstractFillSymbol extends AbstractSymbol implements IFillSymbol {
	private boolean hasFill = true;
	private boolean hasOutline = true;
	private Color color = null;
	private ILineSymbol outline;

	public boolean isSuitableFor(Geometry geom) {
		return geom.getType() == Geometry.TYPES.SURFACE;
	}

	public int getOnePointRgb() {
		int rgb = 0;
		if (outline != null) {
			rgb = outline.getOnePointRgb();
		} else if (color != null) {
			rgb = color.getRGB();
		}
		return rgb;
	}

	public void getPixExtentPlus(Geometry geom, float[] distances, ViewPort viewPort, int dpi) {
		if (getOutline() != null) {
			getOutline().getPixExtentPlus(geom, distances, viewPort, dpi);
		} else {
			distances[0] = 0;
			distances[1] = 0;
		}
	}

	public void setFillColor(Color color) {
		this.color = color;
	}

	public void setOutline(ILineSymbol outline) {
		this.outline = outline;
	}

	public Color getFillColor() {
		return color;
	}

	public ILineSymbol getOutline() {
		return outline;
	}

	public int getFillAlpha() {
		// TODO debatir si es correcto o no (por ejemplo cuando hablamos de LineFillSymbol's
		if (color == null) return 255;
		return color.getAlpha();
	}

	public void setCartographicSize(double cartographicSize, Geometry geom) {
		if (getOutline() != null) {
			getOutline().setLineWidth(cartographicSize);
		}
	}

	public double toCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		if (getOutline() != null) {
			double oldSize = getOutline().getLineWidth();
			setCartographicSize(getCartographicSize(
					viewPort,
					dpi,
					geom),
					geom);
			return oldSize;
		}
		return 0;
	}

	public boolean hasFill() {
		return hasFill;
	}

	public void setHasFill(boolean hasFill) {
		this.hasFill = hasFill;
	}


	public boolean hasOutline() {
		return hasOutline;
	}

	public void setHasOutline(boolean hasOutline) {
		this.hasOutline = hasOutline;
	}


	public double getCartographicSize(ViewPort viewPort, double dpi, Geometry geom) {
		if (getOutline() != null) {
			return CartographicSupportToolkit.
						getCartographicLength(this,
							getOutline().getLineWidth(),
							viewPort,
							dpi);
		}
		return 0;
	}

	public void setUnit(int unitIndex) {
		super.setUnit(unitIndex);
		if (getOutline() != null) {
			getOutline().setUnit(unitIndex);
		}
	}
}
