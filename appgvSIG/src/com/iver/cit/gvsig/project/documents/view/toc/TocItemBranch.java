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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.actions.FLyrVectEditPropertiesTocMenuEntry;

/**
 * @author FJP
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TocItemBranch implements ITocItem {

	private ImageIcon icolayer = null;

	private ImageIcon finalIcon = null;

	private BufferedImage unavailableImg = null;

	private final String defaultIcon = "images/icolayer.PNG";

	private final String unavailableImgPath = "images/unavailable.png";

	private boolean isAvailable = true;

	private FLayer lyr;

	private Dimension sz;

    final public static DataFlavor INFO_FLAVOR =
	    new DataFlavor(TocItemBranch.class, "ItemBranch");
    static DataFlavor flavors[] = {INFO_FLAVOR };

	public TocItemBranch(FLayer lyr)
	{
		this.lyr = lyr;
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#getLabel()
	 */
	public String getLabel() {
		/*if (lyr instanceof FLyrVect && ((FLyrVect)lyr).isBroken())
		{
			return lyr.getName() + "(broken)";
		}
		else*/
			return lyr.getName();
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.toc.ITocItem#getIcon()
	 */
	public Icon getIcon() {
		// TODO Pedirle el icono a la capa. Por defecto habrá un icono
		// para vectoriales y otro para raster, pero se podrán cambiar.

		if (finalIcon == null) {
			this.setIcon(this.lyr.getTocImageIcon());
		}
		updateStateIcon();

		return finalIcon;
	}

	private void setIcon(String path) {
		File f = new File(path);
		if (f.exists()) {
			icolayer = new ImageIcon(f.getAbsolutePath());
		} else {
			URL url =PluginServices.getPluginServices("com.iver.cit.gvsig").getClassLoader().getResource(path);
			if (url!=null) {
				icolayer = new ImageIcon(url);
				return;
			}
		}
		updateStateIcon();

	}
	private void updateStateIcon() {
		if (icolayer == null) return;
		if (lyr.getTocStatusImage() != null)
		{
			BufferedImage newImage = new BufferedImage(icolayer.getIconWidth(),icolayer.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
			Graphics2D grp = newImage.createGraphics();
			grp.drawImage(icolayer.getImage(),0,0,null);
			Image img = lyr.getTocStatusImage();
			grp.drawImage(
					img,
					0,
					icolayer.getIconHeight() -img.getHeight(null),
					null
			);
			this.finalIcon = new ImageIcon(newImage);
		}
		if (this.lyr.isAvailable() != this.isAvailable || finalIcon==null) {
			if (!this.lyr.isAvailable()) {
				BufferedImage newImage = new BufferedImage(icolayer.getIconWidth(),icolayer.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
				Graphics2D grp = newImage.createGraphics();
				grp.drawImage(icolayer.getImage(),0,0,null);
				/*grp.setComposite(AlphaComposite.getInstance(
				 AlphaComposite.SRC_OVER, (float) 1));*/
				BufferedImage img = this.getUnavailableImage();
				grp.drawImage(
						img,
						0,
						icolayer.getIconHeight() -img.getHeight(),
						null
				);
				this.finalIcon = new ImageIcon(newImage);

			} else {
				this.finalIcon = new ImageIcon(icolayer.getImage());
			}
			this.isAvailable =(this.lyr.isAvailable());
		}

	}

	private void setIcon(ImageIcon icon) {
		if (icon!=null) {
			icolayer = icon;
		} else {
			this.setIcon(defaultIcon);

		}
		updateStateIcon();

	}

	public FLayer getLayer() {
		return lyr;
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
	      }
	      else throw new UnsupportedFlavorException(dF);
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

	private BufferedImage getUnavailableImage() {
		if (this.unavailableImg == null) {
			URL url =PluginServices.getPluginServices("com.iver.cit.gvsig").getClassLoader().getResource(this.unavailableImgPath);
			if (url!=null) {
				ImageIcon uIcon = new ImageIcon(url);
				this.unavailableImg = new BufferedImage(uIcon.getIconWidth(),uIcon.getIconHeight(),BufferedImage.TYPE_INT_RGB);
				this.unavailableImg.getGraphics().drawImage(uIcon.getImage(),0,0,null);
			}

		}
		return this.unavailableImg;
	}

	public IContextMenuAction getDoubleClickAction() {
		// TODO
		// THIS IS A PATCH; IT WILL BE REMOVED WHEN ALL THE PROPERTIES
		// FOR ALL LAYERS WILL BE MERGED IN THE SAME DIALOG
		if (!(getLayer() instanceof FLyrVect)) {
			return null;
		}
		return new FLyrVectEditPropertiesTocMenuEntry();
	}
}
