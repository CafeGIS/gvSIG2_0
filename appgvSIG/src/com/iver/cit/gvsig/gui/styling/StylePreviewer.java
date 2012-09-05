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
* $Id: StylePreviewer.java 28370 2009-05-04 16:14:34Z vcaballero $
* $Log$
* Revision 1.6  2007-08-16 06:54:35  jvidal
* javadoc updated
*
* Revision 1.5  2007/08/13 11:33:03  jvidal
* javadoc
*
* Revision 1.4  2007/07/30 12:56:04  jaume
* organize imports, java 5 code downgraded to 1.4 and added PictureFillSymbol
*
* Revision 1.3  2007/05/08 15:44:07  jaume
* *** empty log message ***
*
* Revision 1.2  2007/04/04 16:01:14  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:25:00  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.4  2007/02/21 07:35:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2007/02/08 15:43:04  jaume
* some bug fixes in the editor and removed unnecessary imports
*
* Revision 1.1.2.2  2007/01/30 18:10:10  jaume
* start commiting labeling stuff
*
* Revision 1.1.2.1  2007/01/26 13:49:03  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.styling;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IStyle;

import com.iver.andami.PluginServices;

/**
* Defines the properties of the symbols that are showed in the
* SymbolPreviewer panel.Also the user has methods to set this attributes.
*
* @author jaume dominguez faus - jaume.dominguez@iver.es
*
*/
public class StylePreviewer extends SymbolPreviewer{
	private int hGap = 5, vGap = 5;
	private IStyle style;
	private boolean showOutline;

	/**
	 * Constructor method
	 *
	 */
	public StylePreviewer() {
		super();
		setBackground(Color.WHITE);
	}
	/**
	 * Obtains the style of the symbol showed in the SymbolPreviewer panel
	 * @return style,IStyle
	 */
	public IStyle getStyle() {
		return style;
	}

	public ISymbol getSymbol() {
		throw new Error(PluginServices.getText(this, "undefined_for_StylePreviewer_use")
				+" getStyle() "+
				PluginServices.getText(this, "instead") );

	}

	public void setSymbol(ISymbol symbol) {
		throw new Error(PluginServices.getText(this, "undefined_for_StylePreviewer_use")
				+" setStyle(IStyle) "+
				PluginServices.getText(this, "instead") );

	}
	/**
	 * Defines the style of the symbol showed in the SymbolPreviewer panel
	 * @param style,IStyle
	 */

	public void setStyle(IStyle style) {
		this.style = style;
//		repaint();

	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Rectangle r = getBounds();
		g2.translate(hGap, vGap);
		r = new Rectangle(0, 0, (int) (r.getWidth()-(hGap*2)), (int) (r.getHeight()-(vGap*2)));

		if (style != null) {
			try {
				if (showOutline) {
					// this is a trick for the editor
					style.drawOutline(g2, r);

				} else {
					style.drawInsideRectangle(g2, r);
				}
			} catch (SymbolDrawingException e) {
				if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
					try {
						SymbologyFactory.getWarningSymbol(
								SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
								"",
								SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS).
								drawInsideRectangle(g2, null, r,null);
					} catch (SymbolDrawingException e1) {
						// IMPOSSIBLE TO REACH THIS
					}
				} else {
					// should be unreachable code
					throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
				}
			}
		} else {
			String noneSelected = "["+PluginServices.getText(this, "preview_not_available")+"]";
			FontMetrics fm = g2.getFontMetrics();
			int lineWidth = fm.stringWidth(noneSelected);
			float scale = (float) r.getWidth() / lineWidth;
			Font f = g2.getFont();
			float fontSize = f.getSize()*scale;
			g2.setFont(	f.deriveFont( fontSize ) );

			g2.drawString(noneSelected,	 (r.x*scale) - (hGap/2), r.height/2+vGap*scale);
		}
	}

	/**
	 * Allows to choose between paint styles Outline or the style itself.
	 * @param b
	 */
	public void setShowOutline(boolean b) {
		this.showOutline = b;
	}
}
