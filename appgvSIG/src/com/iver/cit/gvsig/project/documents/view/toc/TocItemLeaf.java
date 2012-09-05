/*
 * Created on 03-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
package com.iver.cit.gvsig.project.documents.view.toc;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeSymbolTocMenuEntry;

/**
 * @author FJP
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TocItemLeaf implements ITocItem {

	private ISymbol symbol;
	private String description;
	private int layerType;
	private static int w_1symbol = 20;
	private static int h_1symbol = 15;

	private Dimension sz;
	BufferedImage imgLegend = null;

    final public static DataFlavor INFO_FLAVOR =
	    new DataFlavor(TocItemLeaf.class, "ItemLeaf");

	static DataFlavor flavors[] = {INFO_FLAVOR };


	public TocItemLeaf(ISymbol symbol, String description, int layerType)
	{
		this.symbol = symbol;
		this.description = description;
		this.layerType = layerType;
	}
	public TocItemLeaf()
	{

	}
	public void setImageLegend(Image imageLegend, String descrip, Dimension size)
	{
		this.description = descrip;
		this.sz = size;
		imgLegend = new BufferedImage(sz.width, sz.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = imgLegend.createGraphics();
		// Rectangle r = new Rectangle(sz.width, sz.height);
		// TODO: Calcular ratio

		double ratioImage =  (double) imageLegend.getWidth(null) / (double) imageLegend.getHeight(null);
	    double ratioToc  = (double) sz.width / (double) sz.height;

	    boolean resul = g2.drawImage(imageLegend, 0, 0, sz.width, sz.height, null);
//		if (ratioImage > ratioToc) {
//			int newHeight = (int) (sz.width / ratioImage);
//			boolean resul = g2.drawImage(imageLegend, 0, 0, sz.width, newHeight, null);
//		} else {
//			int newWidth = (int) (sz.height * ratioImage);
//			boolean resul = g2.drawImage(imageLegend, 0, 0, newWidth, sz.height, null);
//		}

	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#getLabel()
	 */
	public String getLabel() {
		return description;
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#getIcon()
	 */
	public Icon getIcon() {
		// System.out.println("Dentro de getIcon: layerType=" + layerType);
		if (imgLegend != null)
		{
//			Graphics2D g2 = imgLegend.createGraphics();
//			g2.drawString("Prueba", 10, 20);
			return new ImageIcon(imgLegend);
		}

		BufferedImage img = null;
		Graphics2D g2 = null;
		Rectangle r = null;

		switch (layerType)
		{
			case Geometry.TYPES.POINT:
			case Geometry.TYPES.CURVE:
			case Geometry.TYPES.SURFACE:
			case Geometry.TYPES.MULTICURVE:
			case Geometry.TYPES.MULTISURFACE:
			case Geometry.TYPES.MULTIPOINT:
				img = new BufferedImage(w_1symbol, h_1symbol, BufferedImage.TYPE_INT_ARGB);
				g2 = img.createGraphics();
				r = new Rectangle(w_1symbol, h_1symbol);

//				// TODO: CAMBIAR PARA QUE NO DEPENDA DE FSYMBOL
//				if (symbol instanceof FSymbol)
//					FGraphicUtilities.DrawSymbol(g2, AffineTransform.getScaleInstance(0.8,0.8), r, (FSymbol) symbol);
//				// Por ahora, los que no sean FSymbol no se renderizan en el TOC.
				break;
			case Geometry.TYPES.GEOMETRY:
				img = new BufferedImage(3*w_1symbol, h_1symbol, BufferedImage.TYPE_INT_ARGB);
				g2 = img.createGraphics();
				r = new Rectangle(3*w_1symbol, h_1symbol);
//				if (symbol instanceof FSymbol)
//					FGraphicUtilities.DrawSymbol(g2, AffineTransform.getScaleInstance(0.8,0.8), r, (FSymbol) symbol);
				break;

		}

		if (g2 == null) {
			return null;//TODO tipo de shape no soportado.
		}
		try {
			symbol.drawInsideRectangle(g2, AffineTransform.getScaleInstance(0.8,0.8), r,null);
		} catch (SymbolDrawingException e) {
			if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
				try {
					SymbologyFactory.getWarningSymbol(
							SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
							symbol.getDescription(),
							SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS
							).drawInsideRectangle(g2, g2.getTransform(), r.getBounds(),null);
				} catch (SymbolDrawingException e1) {
					// IMPOSSIBLE TO REACH THIS
				}
			} else {
				// should be unreachable code
				throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
			}
		}
		return new ImageIcon(img);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor dF) {
		return dF.equals(INFO_FLAVOR);
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor dF) throws UnsupportedFlavorException, IOException {
	    if (dF.equals(INFO_FLAVOR)) {
	        return this;
	      } else {
			throw new UnsupportedFlavorException(dF);
		}
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#getSize()
	 */
	public Dimension getSize() {
		return sz;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension sz) {
		this.sz = sz;

	}


	/**
	 * @return Returns the symbol.
	 */
	public ISymbol getSymbol() {
		return symbol;
	}
	public IContextMenuAction getDoubleClickAction() {
		return new ChangeSymbolTocMenuEntry();
	}
}
