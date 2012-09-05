/*
 * Created on 04-may-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package org.gvsig.fmap.mapcontext.rendering.symbols;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.compat.CompatLocator;



/**
 * @author fjp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
/**
 * @author fjp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FSymbolFactory {
	static int w, h;

	// Podríamos pasarle también un color de fondo, y usarlo como background del
	// graphics2D.
	static private Graphics2D createG2(Color cRef, BufferedImage bi)
	{
		   Graphics2D big = bi.createGraphics();
		   Color color=new Color(0,0,0,0);
		   big.setBackground(color);
		   big.clearRect(0, 0, w, h);
		   big.setColor(new Color(cRef.getRed(),cRef.getGreen(), cRef.getBlue(),cRef.getAlpha()));
		   big.setStroke(new BasicStroke());

		   return big;

	}


	static public Paint createPatternFill(int style, Color cRef)
	{
		/* public final static int SYMBOL_STYLE_FILL_SOLID = 1;
		public final static int SYMBOL_STYLE_FILL_TRANSPARENT = 2;
		public final static int SYMBOL_STYLE_FILL_HORIZONTAL = 3;
		public final static int SYMBOL_STYLE_FILL_VERTICAL = 4;
		public final static int SYMBOL_STYLE_FILL_CROSS = 5;
		public final static int SYMBOL_STYLE_FILL_UPWARD_DIAGONAL = 6;
		public final static int SYMBOL_STYLE_FILL_DOWNWARD_DIAGONAL = 7;
		public final static int SYMBOL_STYLE_FILL_CROSS_DIAGONAL = 8;
		public final static int SYMBOL_STYLE_FILL_GRAYFILL = 9;
		public final static int SYMBOL_STYLE_FILL_LIGHTGRAYFILL = 10;
		public final static int SYMBOL_STYLE_FILL_DARKGRAYFILL = 11; */


		w=7; h=7;

		BufferedImage bi = null;
		Graphics2D big = null;

		Rectangle2D rProv = new Rectangle();
		rProv.setFrame(0, 0,w,h);

		Paint resulPatternFill = null;
		bi= CompatLocator.getGraphicsUtils().createBufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		big = createG2(cRef, bi);

		switch (style)
		{
			case FSymbol.SYMBOL_STYLE_FILL_SOLID:
			    return null;
			case FSymbol.SYMBOL_STYLE_FILL_TRANSPARENT:
			    return null;
			case FSymbol.SYMBOL_STYLE_FILL_UPWARD_DIAGONAL:
			    big.drawLine(0,0,w,h);
				break;
			case FSymbol.SYMBOL_STYLE_FILL_CROSS:
				big.drawLine(w/2,0,w/2,h);
				big.drawLine(0,h/2,w,h/2);
				break;
			case FSymbol.SYMBOL_STYLE_FILL_CROSS_DIAGONAL:
				big.drawLine(0,0,w,h);
				big.drawLine(0,h,w,0);
				break;
			case FSymbol.SYMBOL_STYLE_FILL_VERTICAL:
				big.drawLine(w/2,0,w/2,h);
				break;
			case FSymbol.SYMBOL_STYLE_FILL_HORIZONTAL:
				big.drawLine(0,h/2,w,h/2);
				break;
			case FSymbol.SYMBOL_STYLE_FILL_DOWNWARD_DIAGONAL:
				// DOWNWARD_DIAGONAL más ancho
				w=15;h=15;
				rProv.setFrame(0, 0,w,h);
				bi= CompatLocator.getGraphicsUtils().createBufferedImage(
						w, h, BufferedImage.TYPE_INT_ARGB);
				big = createG2(cRef, bi);
				big.drawLine(-1,h,w,-1);
				break;

		}

		resulPatternFill = new TexturePaint(bi,rProv);

		return resulPatternFill;

	}

	/**
	 * Al crear esto lo mejor sería mirar en un directorio y cargar todas las
	 * imagenes que tengamos prediseñadas.
	 *
	 * @param cRef
	 * @return
	 */
//	static public Paint[] createPatternFills(Color cRef)
//	{
//
//		int aux = 3;
//
//		w=7; h=7;
//
//		BufferedImage bi = null;
//		Graphics2D big = null;
//
//		Rectangle2D rProv = new Rectangle();
//		rProv.setFrame(0, 0,w,h);
//
//		Paint[] patternFills = new Paint[8];
//
//		// UPWARD_DIAGONAL
//		bi= new Bu-f-f-eredImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		big.drawLine(0,0,w,h);
//		patternFills[FStyle2D.SYMBOL_STYLE_FILL_UPWARD_DIAGONAL-aux] =
//				new TexturePaint(bi,rProv);
//
//		// CROSS
//		bi= new Bu-f-f-eredImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		big.drawLine(w/2,0,w/2,h);
//		big.drawLine(0,h/2,w,h/2);
//		patternFills[FConstant.SYMBOL_STYLE_FILL_CROSS-aux] =
//				new TexturePaint(bi,rProv);
//
//		// CROSS
//		bi= new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		big.drawLine(0,0,w,h);
//		big.drawLine(0,h,w,0);
//		patternFills[FConstant.SYMBOL_STYLE_FILL_CROSS_DIAGONAL-aux] =
//				new TexturePaint(bi,rProv);
//
//
//		// VERTICAL
//		bi= new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		big.drawLine(w/2,0,w/2,h);
//		patternFills[FConstant.SYMBOL_STYLE_FILL_VERTICAL-aux] =
//				new TexturePaint(bi,rProv);
//
//		// HORIZONTAL
//		bi= new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		big.drawLine(0,h/2,w,h/2);
//		patternFills[FConstant.SYMBOL_STYLE_FILL_HORIZONTAL-aux] =
//				new TexturePaint(bi,rProv);
//
//		// DOWNWARD_DIAGONAL más ancho
//		w=15;h=15;
//		rProv.setFrame(0, 0,w,h);
//		bi= new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//		big = createG2(cRef, bi);
//		// big.setColor(Color.BLUE);
//		big.drawLine(-1,h,w,-1);
//		// big.setColor(Color.RED);
//		// big.drawLine(0,0,w,h);
//		patternFills[FConstant.SYMBOL_STYLE_FILL_DOWNWARD_DIAGONAL-aux] =
//				new TexturePaint(bi,rProv);
//		int trans=((TexturePaint)patternFills[FConstant.SYMBOL_STYLE_FILL_DOWNWARD_DIAGONAL-aux]).getTransparency();
//
//		return patternFills;
//	}

}
