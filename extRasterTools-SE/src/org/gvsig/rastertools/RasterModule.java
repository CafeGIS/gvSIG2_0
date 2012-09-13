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
package org.gvsig.rastertools;

import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.gvsig.AppGvSigLocator;
import org.gvsig.AppGvSigManager;
import org.gvsig.fmap.dal.coverage.dataset.io.DefaultRasterIOLibrary;
import org.gvsig.fmap.dal.raster.CoverageStore;
import org.gvsig.fmap.dal.store.raster.RasterStoreLibrary;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.ConfigurationEvent;
import org.gvsig.raster.ConfigurationListener;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.filter.grayscale.GrayScaleManager;
import org.gvsig.raster.filter.mask.MaskListManager;
import org.gvsig.raster.filter.regionalpha.RegionAlphaListManager;
import org.gvsig.raster.gui.preferences.RasterPreferences;
import org.gvsig.raster.gui.wizards.PrepareDataStoreParametersRaw;
import org.gvsig.raster.gui.wizards.PrepareDataStoreParametersVTR;
import org.gvsig.raster.gui.wizards.PrepareLayerAskCoordinates;
import org.gvsig.raster.gui.wizards.PrepareLayerAskProjection;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.properties.RasterPropertiesTocMenuEntry;
import org.gvsig.rastertools.properties.panels.BandSelectorPanel;
import org.gvsig.rastertools.properties.panels.EnhancedPanel;
import org.gvsig.rastertools.properties.panels.GeneralPanel;
import org.gvsig.rastertools.properties.panels.InfoPanel;
import org.gvsig.rastertools.properties.panels.TransparencyPanel;
import org.gvsig.rastertools.rasterresolution.ZoomPixelCursorTocMenuEntry;
import org.gvsig.rastertools.saveas.SaveAsTocMenuEntry;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

/**
 * Extensión que adapta a FMap y gvSIG la nueva implementación de raster. Para
 * el cuadro de Propiedades de visualización de raster contiene el contenedor
 * base se registran la entrada del menú del TOC y los paneles en el cuadro.
 * Para la parte de FMap contiene una nueva capa raster y los drivers
 * necesarios.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterModule extends Extension implements ConfigurationListener {

	/**
	 * Indica si en el panel de preferencias se refresca automaticamente la vista
	 * para mostrar los cambios
	 */
	public static boolean       autoRefreshView = true;

	/**
	 * Indica si se debe preguntar las coordenadas al cargar una capa que no las
	 * tenga
	 */
	public static boolean       askCoordinates = false;

	private RasterStoreLibrary storeLibrary;

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		// Asignamos la configuracion global a FlyrRasterSE
		FLyrRasterSE.setConfiguration(Configuration.getSingleton());

		Configuration.addValueChangedListener(this);
		loadConfigurationValues();

		registerIcons();
		RasterLibrary.wakeUp();
		DefaultRasterIOLibrary drivers = null;
		try {
			drivers = new DefaultRasterIOLibrary();
			drivers.initialize();
		} catch (NoClassDefFoundError e) {
			RasterToolsUtil.debug("There was not possible to load drivers", e, null);
		}

		// DiagSignalHandler.install("ALRM");

		Preferences prefs = Preferences.userRoot().node("gvsig.foldering");
		prefs.put("DataFolder", System.getProperty("user.home"));
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();

		// Creación del punto de extensión para registrar paneles en el cuadro de propiedades.
		if (!extensionPoints.has("RasterSEPropertiesDialog")) {
			ExtensionPoint point=extensionPoints.add("RasterSEPropertiesDialog");

			point.setDescription("Raster Properties registrable panels (register instances of javax.swing.JPanel)");
			//extensionPoints.put(new ExtensionPoint("RasterSEPropertiesDialog", "Raster Properties registrable panels (register instances of javax.swing.JPanel)"));
		}

		// Añadimos la configuracion de Raster a gvSIG

		ExtensionPoint point=extensionPoints.add("AplicationPreferences");
		point.append("RasterPreferences", "", RasterPreferences.class);

		// Añadimos paneles al cuadro de propiedades.
		point = extensionPoints.add("RasterSEPropertiesDialog");
		point.append("info", "", InfoPanel.class);
		point.append("bands_panel", "", BandSelectorPanel.class);
		point.append("transparencia", "", TransparencyPanel.class);
		point.append("realce", "", EnhancedPanel.class);
		//extensionPoints.add("RasterSEPropertiesDialog", "pansharp", PanSharpeningPanel.class);
		point.append("general_panel", "", GeneralPanel.class);

		// Añadimos las entradas del menú  del toc de raster
		point = extensionPoints.add("View_TocActions");
		point.append("RasterSEProperties", "", RasterPropertiesTocMenuEntry.getSingleton());
		point.append("View_TocActions", "SaveAs", SaveAsTocMenuEntry.getSingleton());
		point.append("View_TocActions", "ZoomPixelCursor", new ZoomPixelCursorTocMenuEntry());

		// Añadimos nuestra extension para el tratamiento de la apertura de ficheros mdentro de gvSIG
		// AddLayer.addWizard(FileOpenWizard.class);
		// ToolsLocator.getExtensionPointManager().add("FileExtendingOpenDialog",
		// 			"").append("FileOpenRaster", "", FileOpenRaster.class);

		AppGvSigManager appGvSigMan = AppGvSigLocator.getAppGvSigManager();

		appGvSigMan.registerPrepareOpenDataStoreParameters(new PrepareDataStoreParametersRaw());
		appGvSigMan.registerPrepareOpenDataStoreParameters(new PrepareDataStoreParametersVTR());

		appGvSigMan.registerPrepareOpenLayer(new PrepareLayerAskCoordinates());
		appGvSigMan.registerPrepareOpenLayer(new PrepareLayerAskProjection());

		//Alias
		LayerFactory lFactory=LayerFactory.getInstance();
//		lFactory.registerLayerToUseForStore("com.iver.cit.gvsig.fmap.layers.FLyrRaster", FLyrRasterSE.class);
//		lFactory.registerLayerToUseForStore("com.iver.cit.gvsig.fmap.layers.StatusLayerRaster", StatusLayerRaster.class);
		lFactory.registerLayerToUseForStore(CoverageStore.class,
				FLyrRasterSE.class);

		storeLibrary = new RasterStoreLibrary();
		storeLibrary.initialize();

		// Registrar filtros
		RegionAlphaListManager.register();
		MaskListManager.register();
		GrayScaleManager.register();
	}

	/**
	 * Registra los iconos a utilizar en andami.
	 */
	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"forward-icon",
				this.getClass().getClassLoader().getResource("images/forward.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"backward-icon",
				this.getClass().getClassLoader().getResource("images/backward.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"map-ok-ico",
				MapControl.class.getResource("images/map_ico_ok.gif")
			);
//		PluginServices.getIconTheme().registerDefault(
//				"crux-cursor",
//				MapControl.class.getResource("images/CruxCursor.png")
//			);
		PluginServices.getIconTheme().registerDefault(
				"blank-icon",
				getClass().getClassLoader().getResource("images/blank.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"addlayer-icon",
				this.getClass().getClassLoader().getResource("images/addlayer.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"delall-icon",
				this.getClass().getClassLoader().getResource("images/delall.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"rotar-icon",
				this.getClass().getClassLoader().getResource("images/Rotar.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"hor-arrow-icon",
				this.getClass().getClassLoader().getResource("images/FlechaHorizCursor.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"ver-arrow-icon",
				this.getClass().getClassLoader().getResource("images/FlechaVertCursor.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"inclder-arrow-icon",
				this.getClass().getClassLoader().getResource("images/FlechaInclDerCursor.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"inclizq-arrow-icon",
				this.getClass().getClassLoader().getResource("images/FlechaInclIzqCursor.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"shear-y-icon",
				this.getClass().getClassLoader().getResource("images/y.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"shear-x-icon",
				this.getClass().getClassLoader().getResource("images/x.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"hand-icon",
				this.getClass().getClassLoader().getResource("images/Hand.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"back-icon",
				this.getClass().getClassLoader().getResource("images/back.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"next-icon",
				this.getClass().getClassLoader().getResource("images/next.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"undo-icon",
				this.getClass().getClassLoader().getResource("images/undo.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"zoom-pixel-cursor",
				this.getClass().getClassLoader().getResource("images/ZoomPixelCursor.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"save-raster",
				this.getClass().getClassLoader().getResource("images/Rectangle.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"pixel-increase",
				this.getClass().getClassLoader().getResource("images/increase.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"select-raster",
				this.getClass().getClassLoader().getResource("images/stock_toggle-info.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"select-raster",
				this.getClass().getClassLoader().getResource("images/stock_toggle-info.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"export-icon",
				this.getClass().getClassLoader().getResource("images/export.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"pref-raster-icon",
				this.getClass().getClassLoader().getResource("images/raster-pref.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"import-icon",
				this.getClass().getClassLoader().getResource("images/import.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"save-icon",
				this.getClass().getClassLoader().getResource("images/save.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"reset-icon",
				this.getClass().getClassLoader().getResource("images/reset.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"tfwload-icon",
				this.getClass().getClassLoader().getResource("images/load.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"centerraster-icon",
				this.getClass().getClassLoader().getResource("images/center_image.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"increase-icon",
				this.getClass().getClassLoader().getResource("images/aumentar.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"decrease-icon",
				this.getClass().getClassLoader().getResource("images/disminuir.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"selectzoomarea-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-to-seleccion.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"prevzoom-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-back.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"fullview-icon",
				this.getClass().getClassLoader().getResource("images/view-zoom-map-contents.png")
			);
		PluginServices.getIconTheme().register(
				"geolocalization-icon",
				this.getClass().getClassLoader().getResource("images/georef.png")
			);
		PluginServices.getIconTheme().register(
				"exporttoascii-icon",
				this.getClass().getClassLoader().getResource("images/exportToAscii.png")
			);
		PluginServices.getIconTheme().register(
				"exporttocsv-icon",
				this.getClass().getClassLoader().getResource("images/exportToCSV.png")
			);
		PluginServices.getIconTheme().register(
				"importfromcsv-icon",
				this.getClass().getClassLoader().getResource("images/importFromCSV.png")
			);
		PluginServices.getIconTheme().register(
				"exit-icon",
				this.getClass().getClassLoader().getResource("images/ico_exit.gif")
			);
		PluginServices.getIconTheme().register(
				"options-icon",
				this.getClass().getClassLoader().getResource("images/ico_options.gif")
			);
		PluginServices.getIconTheme().register(
				"add-icon",
				this.getClass().getClassLoader().getResource("images/add-ico.gif")
			);
		PluginServices.getIconTheme().register(
				"centerpoint-icon",
				this.getClass().getClassLoader().getResource("images/icon_centerpoint.gif")
			);
		PluginServices.getIconTheme().register(
				"hand-icon",
				this.getClass().getClassLoader().getResource("images/Hand.gif")
			);
		PluginServices.getIconTheme().register(
				"analisis-icon",
				this.getClass().getClassLoader().getResource("images/prismaticos.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"clipping-icon",
				getClass().getClassLoader().getResource("images/clipping.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"filter-icon",
				getClass().getClassLoader().getResource("images/raster-filter.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"histogram-icon",
				getClass().getClassLoader().getResource("images/histogram.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"overviews-icon",
				getClass().getClassLoader().getResource("images/overviews.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"properties-icon",
				getClass().getClassLoader().getResource("images/properties.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"colortable-icon",
				getClass().getClassLoader().getResource("images/colortable.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"rois-icon",
				getClass().getClassLoader().getResource("images/rois.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"brush-icon",
				getClass().getClassLoader().getResource("images/brush.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"selectrgb-icon",
				getClass().getClassLoader().getResource("images/select_rgb.gif")
			);
		PluginServices.getIconTheme().registerDefault(
				"reproj-icon",
				getClass().getClassLoader().getResource("images/reproject.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"vectorization-icon",
				getClass().getClassLoader().getResource("images/vectorT.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"focus-icon",
				getClass().getClassLoader().getResource("images/focus.png")
			);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null)
			return false;
		if (f.getClass() == View.class) {
			BaseView vista = (BaseView) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			FLayers layers = mapa.getLayers();
			for (int i = 0; i < layers.getLayersCount(); i++)
				if (layers.getLayer(i) instanceof FLyrRasterSE)
					return true;
		}
		return false;
	}

	/**
	 * Mostramos el control si hay alguna capa cargada.
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null)
			return false;

		if (f instanceof View) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			if (mapa.getLayers().getLayersCount() > 0)
				return true;
		}

		return false;
	}

	/**
	 * Carga los valores de configuracion iniciales
	 */
	private void loadConfigurationValues() {
		autoRefreshView = Configuration.getValue("general_auto_preview", Boolean.TRUE).booleanValue();
		askCoordinates = Configuration.getValue("general_ask_coordinates", Boolean.FALSE).booleanValue();
		RasterLibrary.defaultNumberOfClasses = Configuration.getValue("general_defaultNumberOfClasses", Integer.valueOf(RasterLibrary.defaultNumberOfClasses)).intValue();
		RasterLibrary.cacheSize = Configuration.getValue("cache_size", Long.valueOf(RasterLibrary.cacheSize)).longValue();
		RasterLibrary.pageSize = Configuration.getValue("cache_pagesize", Double.valueOf(RasterLibrary.pageSize)).doubleValue();
		RasterLibrary.pagsPerGroup = Configuration.getValue("cache_pagspergroup", Integer.valueOf(RasterLibrary.pagsPerGroup)).intValue();
		RasterLibrary.blockHeight = Configuration.getValue("cache_blockheight", Integer.valueOf(RasterLibrary.blockHeight)).intValue();
		RasterLibrary.defaultNoDataValue = Configuration.getValue("nodata_value", Double.valueOf(RasterLibrary.defaultNoDataValue)).doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.ConfigurationListener#actionConfigurationChanged(org.gvsig.raster.util.ConfigurationEvent)
	 */
	public void actionConfigurationChanged(ConfigurationEvent e) {
		if (e.getKey().equals("nodata_transparency_enabled")) {
			boolean noDataTransparent = ((Boolean) e.getValue()).booleanValue();
			IWindow[] f = PluginServices.getMDIManager().getAllWindows();
			if (f == null)
				return;
			for (int i = 0; i < f.length; i++)
				if (f[i] instanceof View) {
					View vista = (View) f[i];
					IProjectView model = vista.getModel();
					MapContext map = model.getMapContext();
					FLayers lyrs = map.getLayers();

					ArrayList list = RasterToolsUtil.getRasterLayerList(lyrs, null);
					for (int j = 0; j < list.size(); j++)
						if(list.get(j) instanceof FLyrRasterSE)
							((FLyrRasterSE)list.get(j)).getDataSource().getTransparencyFilesStatus().activeNoData(noDataTransparent);
				}

			return;
		}

		if (e.getKey().equals("general_auto_preview")) {
			if(e.getValue() instanceof String)
				autoRefreshView = new Boolean((String) e.getValue()).booleanValue();
			if(e.getValue() instanceof Boolean)
				autoRefreshView = ((Boolean) e.getValue()).booleanValue();
			return;
		}

		if (e.getKey().equals("general_ask_coordinates")) {
			if(e.getValue() instanceof String)
				askCoordinates = new Boolean((String) e.getValue()).booleanValue();
			if(e.getValue() instanceof Boolean)
				askCoordinates = ((Boolean) e.getValue()).booleanValue();
			return;
		}

		if (e.getKey().equals("general_defaultNumberOfClasses")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.defaultNumberOfClasses = new Integer(
							(String) e.getValue()).intValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en el número de clases
				}

			if(e.getValue() instanceof Integer)
				RasterLibrary.defaultNumberOfClasses = ((Integer) e.getValue()).intValue();
			return;
		}

		if (e.getKey().equals("cache_size")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.cacheSize = new Long((String) e.getValue()).longValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en la cache
				}
			if(e.getValue() instanceof Long)
				RasterLibrary.cacheSize = ((Long) e.getValue()).longValue();
			return;
		}

		if (e.getKey().equals("cache_pagesize")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.pageSize = new Double((String) e.getValue())
							.doubleValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en la cache
				}

			if(e.getValue() instanceof Double)
				RasterLibrary.pageSize = ((Double) e.getValue()).doubleValue();
			return;
		}

		if (e.getKey().equals("cache_pagspergroup")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.pagsPerGroup = new Integer((String) e
							.getValue()).intValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en la cache
				}

			if(e.getValue() instanceof Integer)
				RasterLibrary.pagsPerGroup = ((Integer) e.getValue()).intValue();
			return;
		}

		if (e.getKey().equals("cache_blockheight")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.blockHeight = new Integer((String) e
							.getValue()).intValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en la cache
				}
			if(e.getValue() instanceof Integer)
				RasterLibrary.blockHeight = ((Integer) e.getValue()).intValue();
			return;
		}

		if (e.getKey().equals("nodata_value")) {
			if(e.getValue() instanceof String)
				try {
					RasterLibrary.defaultNoDataValue = new Double((String) e
							.getValue()).doubleValue();
				} catch (NumberFormatException exc) {
					//Valor por defecto en valor nodata
				}

			if(e.getValue() instanceof Double)
				RasterLibrary.defaultNoDataValue = ((Double) e.getValue()).doubleValue();
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		this.execute("SELECTIMAGE");
	}

	public void postInitialize() {
		super.postInitialize();
		storeLibrary.postInitialize();
	}
}