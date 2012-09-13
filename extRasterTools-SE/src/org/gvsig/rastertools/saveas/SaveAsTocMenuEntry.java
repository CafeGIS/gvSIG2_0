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
package org.gvsig.rastertools.saveas;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponent;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterGeoOperations;
import org.gvsig.raster.hierarchy.IRasterOperations;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.process.ClippingProcess;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * <p>
 * Entrada del TOC que corresponde con la opción "Salvar Como" de raster. Esta se apoya
 * en el proceso de recorte de raster para salvar datos.
 * </p>
 * <p>
 * Cuando se abre el dialogo de "Salvar como" se cargan las extensiones soportadas en el
 * selector. Cada tipo de datos de la fuente soporta unas extensiones de escritura u otras.
 * Por ejemplo, si la capa de entrada  es FLOAT no podremos escribir a JPG2000 porque no
 * lo soporta, tendriamos que convertila primero a RGB.
 * </p>
 * <p>
 * Cambiando el tipo de extensión en el selector cambian el panel de propiedades asociado.
 * El fichero de salidad se salvará con las propiedades ajustadas.
 * </P>
 * @version 30/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class SaveAsTocMenuEntry extends AbstractTocContextMenuAction implements PropertyChangeListener, IGenericToolBarMenuItem {
	static private SaveAsTocMenuEntry singleton     = null;
	private JFileChooser              chooser       = null;
	private PropertiesComponent       panelProperty = null;

	/**
	 * Nadie puede crear una instancia a esta clase única, hay que usar el
	 * getSingleton()
	 */
	private SaveAsTocMenuEntry() {}

	/**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
	static public SaveAsTocMenuEntry getSingleton() {
		if (singleton == null)
			singleton = new SaveAsTocMenuEntry();
		return singleton;

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.contextMenu.AbstractContextMenuAction#getGroup()
	 */
	public String getGroup() {
		return "RasterExport";
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
		return PluginServices.getText(this, "saveas");
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

		return (((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.SAVEAS));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		FLayer fLayer = null;
		IWindow w = PluginServices.getMDIManager().getActiveWindow();

		if (selectedItems.length != 1)
			return;

		fLayer = selectedItems[0];

		if (!(fLayer instanceof IRasterOperations))
			return;

		chooser = new JFileChooser("SAVE_AS_TOC_MENU_ENTRY",JFileChooser.getLastPath("SAVE_AS_TOC_MENU_ENTRY", null));
		chooser.addPropertyChangeListener(this);
		chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));

		// Cargamos las extensiones en la lista
		ArrayList extList = new ArrayList();
		try {
			extList = GeoRasterWriter.getExtensionsSupported(((IRasterGeoOperations) fLayer).getDataType()[0], ((IRasterGeoOperations) fLayer).getBandCount(), false);
		} catch (RasterDriverException e2) {
			RasterToolsUtil.messageBoxError("error_extensiones_soportadas", chooser, e2);
			return;
		}

		int selected_id = extList.size() - 1;
		FileFilter selected = null;
		for (int i = 0; i < extList.size(); i++) {
			FileFilter filter = new ExtendedFileFilter((String) extList.get(i));
			if (extList.get(i).equals("tif")) {
				selected = filter;
				selected_id = i;
			}
			chooser.addChoosableFileFilter(filter);
		}
		if (selected != null)
			chooser.setFileFilter(selected);

		// Cargamos el panel de propiedades en el selector
		panelProperty = loadPanelProperties((String) extList.get(selected_id));
		chooser.setAccessory(panelProperty);
		chooser.setAcceptAllFileFilterUsed(false);

		if (w instanceof View) {
			if (chooser.showSaveDialog(((BaseView) w).getComponent(0)) == JFileChooser.APPROVE_OPTION) {
				// Creación de parámetros
				String tit = PluginServices.getMDIManager().getWindowInfo(w).getTitle();
				WriterBufferServer dataWriter = new WriterBufferServer();
				int[] dValues = new int[] { 0, (int) ((IRasterOperations) fLayer).getPxHeight(), (int) ((IRasterOperations) fLayer).getPxWidth(), 0 };
				IRasterDataSource dataset = (IRasterDataSource)((IRasterDataset) fLayer).getDataSource();
				int[] drawableBands = new int[((IRasterOperations) fLayer).getBandCount()];
				for (int i = 0; i < ((IRasterOperations) fLayer).getBandCount(); i++)
					drawableBands[i] = i;
				JFileChooser.setLastPath("SAVE_AS_TOC_MENU_ENTRY", chooser.getCurrentDirectory());
				String file =  ((ExtendedFileFilter) chooser.getFileFilter()).getNormalizedFilename(chooser.getSelectedFile());
				if (!RasterToolsUtil.canWrite(chooser.getCurrentDirectory().toString(), this))
					return;
				Params params = null;
				try {
					params = GeoRasterWriter.getWriter(file).getParams();
				} catch (NotSupportedExtensionException e1) {
					RasterToolsUtil.messageBoxError("no_driver_escritura", this, e1);
				} catch (RasterDriverException e1) {
					RasterToolsUtil.messageBoxError("no_driver_escritura", this, e1);
				}

				// Lanzamiento del proceso de guardado
				RasterProcess clippingProcess = new ClippingProcess();
				clippingProcess.setActions(new SaveAsActions());
				clippingProcess.addParam("viewname", tit);
				clippingProcess.addParam("pixelcoordinates", dValues);
				clippingProcess.addParam("filename", file);
				clippingProcess.addParam("datawriter", dataWriter);
				clippingProcess.addParam("layer", fLayer);
				clippingProcess.addParam("drawablebands", drawableBands);
				clippingProcess.addParam("colorInterpretation", dataset.getColorInterpretation());
				clippingProcess.addParam("onelayerperband", new Boolean(false));
				clippingProcess.addParam("interpolationmethod", new Integer(BufferInterpolation.INTERPOLATION_NearestNeighbour));
				clippingProcess.addParam("affinetransform", dataset.getAffineTransform(0));
				clippingProcess.addParam("resolution", new int[]{(int) ((IRasterOperations) fLayer).getPxWidth(),
																 (int) ((IRasterOperations) fLayer).getPxHeight()});

				if (params != null)
					RasterToolsUtil.loadWriterParamsFromPropertiesPanel(panelProperty, params);
				clippingProcess.addParam("driverparams", params);
				clippingProcess.start();
			}
		}
	}

	/**
	 * Evento que se produce al cambiar el driver de escritura. Esto
	 * sustituye el panel properties por el del nuevo driver seleccionado
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof ExtendedFileFilter) {
			String ext = ((ExtendedFileFilter) evt.getNewValue()).getExtensions().get(0).toString();
			panelProperty = loadPanelProperties(ext);
			chooser.setAccessory(panelProperty);
			chooser.revalidate();
		}
	}

	/**
	 * Obtiene el objeto PropertiesComponent para una extensión concreta de fichero
	 * sobre el que se va a escribir.
	 * @param file Fichero raster sobre el que se escribirá
	 * @return PropertiesComponent
	 */
	private PropertiesComponent loadPanelProperties(String file) {
		PropertiesComponent panelProperty = new PropertiesComponent();
		Params params = null;
		try {
			params = GeoRasterWriter.getWriter(file).getParams();
		} catch (NotSupportedExtensionException e1) {
			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this, "no_driver_escritura"));
			return null;
		} catch (RasterDriverException e1) {
			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this, "no_driver_escritura"));
			return null;
		}
		RasterToolsUtil.loadPropertiesFromWriterParams(panelProperty, params, null);
		return panelProperty;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.generictoolbar.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return PluginServices.getIconTheme().get("save-icon");
	}
}