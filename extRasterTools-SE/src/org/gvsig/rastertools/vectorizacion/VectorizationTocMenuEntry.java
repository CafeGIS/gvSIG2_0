/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.rastertools.vectorizacion;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.statistics.StatisticsProcess;
import org.gvsig.rastertools.vectorizacion.clip.ClipData;
import org.gvsig.rastertools.vectorizacion.clip.ClipListener;
import org.gvsig.rastertools.vectorizacion.clip.ui.ClipPanel;
import org.gvsig.rastertools.vectorizacion.filter.GrayConversionData;
import org.gvsig.rastertools.vectorizacion.filter.GrayConversionListener;
import org.gvsig.rastertools.vectorizacion.filter.ui.GrayConversionPanel;
import org.gvsig.rastertools.vectorizacion.stretch.StretchData;
import org.gvsig.rastertools.vectorizacion.stretch.StretchListener;
import org.gvsig.rastertools.vectorizacion.stretch.ui.StretchPanel;
import org.gvsig.rastertools.vectorizacion.vector.VectorData;
import org.gvsig.rastertools.vectorizacion.vector.VectorListener;
import org.gvsig.rastertools.vectorizacion.vector.ui.VectorPanel;

import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * <code>VectorizationTocMenuEntry</code> es el punto de entrada del menu de vectorización.
 *
 * @version 12/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class VectorizationTocMenuEntry extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem, IProcessActions {
	static private VectorizationTocMenuEntry   singleton              = null;
	private boolean                            grayScaleConversion    = true;
	private static final int                   SIZE_MAX               = 20; 
	

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private VectorizationTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public VectorizationTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new VectorizationTocMenuEntry();
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "RasterProcess";
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 50;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getOrder()
	 */
	public int getOrder() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.IContextMenuAction#getText()
	 */
	public String getText() {
		return RasterToolsUtil.getText(this, "vectorization");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof ILayerState))
			return false;

		if (!((ILayerState) selectedItems[0]).isOpen())
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;
		
		return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.FILTER);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayer fLayer = null;

		if (selectedItems.length != 1)
			return;

		fLayer = selectedItems[0];

		if (!(fLayer instanceof FLyrRasterSE))
			return;

		FLyrRasterSE lyr = (FLyrRasterSE)fLayer;
		grayScaleConversion = true;
		
		long size = RasterUtilities.getBytesFromRasterBufType(lyr.getDataType()[0]);
		
		if((lyr.getBandCount() * lyr.getPxWidth() * lyr.getPxHeight() * size) > (SIZE_MAX * 1000000)) {
			if(!RasterToolsUtil.messageBoxYesOrNot("source_too_big", this)) {
				return;
			}
		}
		
		if(lyr.getBandCount() == 1) {
			if(RasterToolsUtil.messageBoxYesOrNot("datatype_not_byte", this)) {
				grayScaleConversion = false;
			}
		}
		StatisticsProcess.launcher(lyr, this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return RasterToolsUtil.getIcon("vectorization-icon");
	}

	public void end(Object param) {
		FLyrRasterSE lyr = null;
		if(param instanceof FLyrRasterSE)
			lyr = (FLyrRasterSE)param;
		else return;
		
		//1-Creamos los modelos de datos
		ClipData clipData = new ClipData();
		GrayConversionData grayConvData = new GrayConversionData();
		StretchData stretchData = new StretchData();
		VectorData vectorData = new VectorData();
		vectorData.setProjLayer(lyr.getMapContext().getProjection());

		//2-Creamos los paneles
		ClipPanel clipPanel = new ClipPanel();
		GrayConversionPanel grayConvPanel = new GrayConversionPanel();
		StretchPanel stretchPanel = new StretchPanel();
		VectorPanel vectorPanel = new VectorPanel();
		
		//3-Creamos los listener
		ClipListener clipList = new ClipListener(lyr, clipPanel, clipData);
		GrayConversionListener grayConvList = new GrayConversionListener(lyr, grayConvPanel, grayConvData);
		StretchListener stretchListener = new StretchListener(lyr, stretchPanel, stretchData);
		VectorListener vectorList = new VectorListener(lyr, vectorPanel, vectorData); 
		
		//4-Asignamos los observadores
		clipData.addObserver(clipPanel);
		grayConvData.addObserver(grayConvPanel);
		vectorData.addObserver(vectorPanel);
		stretchData.addObserver(stretchPanel);
		
		//5-Panel general: creamos el modelo de datos, panel y listener
		MainPanel mainPanel = null;
		if(grayScaleConversion) {
			mainPanel = new MainPanel(lyr, grayConvList.getPreviewRender());
			mainPanel.setPanel(clipPanel);
			mainPanel.setPanel(grayConvPanel);
		} else {
			mainPanel = new MainPanel(lyr, stretchListener.getPreviewRender());
			mainPanel.setPanel(clipPanel);
			mainPanel.setPanel(stretchPanel);
		}
		mainPanel.setPanel(vectorPanel);
		mainPanel.initialize();
		
		MainListener vectListener = new MainListener(lyr, mainPanel, grayConvList, clipList, vectorList, stretchListener);
		if(grayScaleConversion) {
			vectListener.setPreviewRender(grayConvList.getPreviewRender());
			grayConvList.setPreviewPanel(mainPanel.getPreviewBasePanel());
		} else {
			vectListener.setPreviewRender(stretchListener.getPreviewRender());
			stretchListener.setPreviewPanel(mainPanel.getPreviewBasePanel());
		}
			
		//6-Actualizamos los datos
		clipData.updateObservers();
		grayConvData.updateObservers();
		vectorData.updateObservers();
		stretchData.updateObservers();
		
		//7-Creamos el dialogo 
		MainDialog dialog = new MainDialog(620, 485, lyr.getName(), mainPanel);
		RasterToolsUtil.addWindow(dialog);
	}

	public void interrupted() {
	}
}