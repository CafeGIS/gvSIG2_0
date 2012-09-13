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
package org.gvsig.fmap.raster.layers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.ImageIcon;

import org.cresques.cts.IProjection;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.coverage.dataset.io.DefaultRasterIOLibrary;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.raster.CoverageStore;
import org.gvsig.fmap.dal.store.raster.RasterStoreParameters;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.XMLLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLyrDefault;
import org.gvsig.fmap.mapcontext.layers.LayerChangeSupport;
import org.gvsig.fmap.mapcontext.layers.LayerListener;
import org.gvsig.fmap.mapcontext.layers.Tiling;
import org.gvsig.fmap.mapcontext.layers.operations.Classifiable;
import org.gvsig.fmap.mapcontext.layers.operations.InfoByPoint;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.gvsig.fmap.mapcontext.layers.operations.StringXMLItem;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;
import org.gvsig.fmap.raster.legend.ColorTableLegend;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.CompositeDataset;
import org.gvsig.raster.dataset.FileNotOpenException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.MosaicNotValidException;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.datastruct.persistence.ColorTableLibraryPersistence;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridPalette;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.raster.grid.filter.bands.ColorTableListManager;
import org.gvsig.raster.grid.filter.enhancement.EnhancementStretchListManager;
import org.gvsig.raster.grid.filter.enhancement.LinearStretchParams;
import org.gvsig.raster.grid.render.Rendering;
import org.gvsig.raster.grid.render.VisualPropertyEvent;
import org.gvsig.raster.grid.render.VisualPropertyListener;
import org.gvsig.raster.grid.roi.ROI;
import org.gvsig.raster.hierarchy.IRasterDataset;
import org.gvsig.raster.hierarchy.IRasterOperations;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.hierarchy.IStatistics;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.projection.CRS;
import org.gvsig.raster.util.ColorConversion;
import org.gvsig.raster.util.Historical;
import org.gvsig.raster.util.MathUtils;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Capa raster
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class FLyrRasterSE extends FLyrDefault implements IRasterProperties,
		IRasterDataset, InfoByPoint, Classifiable, IRasterOperations,
		IRasterLayerActions, ILayerState, VisualPropertyListener, SingleLayer {
	private boolean                mustTileDraw        = false;
	private boolean                mustTilePrint       = true;
	private int                    maxTileDrawWidth    = 200;
	private int                    maxTileDrawHeight   = 200;
	private int                    maxTilePrintWidth   = 1500;
	private int                    maxTilePrintHeight  = 1500;
	protected IStatusRaster        status              = null;
	private boolean                firstLoad           = false;
	private boolean                removeRasterFlag    = true;
	private Object                 params              = null;
	protected IRasterDataSource    dataset             = null;
	protected Rendering            render              = null;
	protected BufferFactory        bufferFactory       = null;
	private int                    posX                = 0;
	private int                    posY                = 0;
	private double                 posXWC              = 0;
	private int                    posYWC              = 0;
	private int                    r                   = 0;
	private int                    g                   = 0;
	private int                    b                   = 0;
	private LayerChangeSupport     layerChangeSupport  = new LayerChangeSupport();
	private FLyrState              state               = new FLyrState();
	private ArrayList              filterArguments     = null;
	protected ILegend              lastLegend          = null;
	protected ColorTable           loadedFromProject   = null;
	private ArrayList              rois                = null;
	private RasterDrawStrategy     strategy            = null;
	static private IConfiguration  configuration       = new DefaultLayerConfiguration();

	private BufferedImage          image               = null;
	private static GeometryManager geomManager   	   = GeometryLocator.getGeometryManager();
	private static final Logger    logger              = LoggerFactory.getLogger(FLyrRasterSE.class);

	/**
	 * Tipo de valor no data asociado a la capa.
	 * Sirve para diferenciar los estados seleccionados por el usuario. Siendo
	 * estos 'Sin Valor NoData', 'NoData de Capa'(Por defecto) y 'Personalizado'
	 */
	private int                    noDataType          = RasterLibrary.NODATATYPE_LAYER;

	/**
	 * Lista de transformaciones afines que son aplicadas. Esta lista es
	 * simplemente un historico que no se utiliza. Es posible utilizarlo para
	 * recuperar transformaciones anteriores.
	 */
	private Historical             affineTransformList = new Historical();
	private CoverageStore          store               = null;

	static {
		 RasterLibrary.wakeUp();
		 try {
			 new DefaultRasterIOLibrary().initialize();
		 } catch (NoClassDefFoundError e) {
			 RasterToolsUtil.debug("There was not possible to load drivers", e, null);
		 }
	}

	/**
	 * Crea una capa Raster a partir del nombre driver, fichero y proyección.
	 *
	 * @param layerName
	 *            Nombre de la capa..
	 * @param params
	 *            Parámetros de carga del formato. El caso más simple es la ruta
	 *            de la capa en disco.
	 * @param d
	 *            RasterDriver.
	 * @param f
	 *            Fichero.
	 * @param proj
	 *            Proyección.
	 * @return Nueva capa de tipo raster.
	 * @throws DriverIOException
	 *
	 * @deprecated
	 */
	public static FLyrRasterSE createLayer(String layerName, Object params,
			IProjection proj) throws LoadLayerException {
		FLyrRasterSE capa = new FLyrRasterSE();
		capa.setLoadParams(params);
		capa.setName(layerName);
		capa.setProjection(proj);
		capa.load();
		return capa;
	}

	/**
	 * Asigna los parámetros para la carga de la capa
	 *
	 * @param param
	 *            Parámetros.
	 *
	 * @deprecated
	 */
	public void setLoadParams(Object param){
		this.params = param;

		//Si la capa tiene nombre acivamos el estado awake
		if(params != null && getName() != null)
			try {
				enableAwake();
			} catch (NotAvailableStateException e) {
				RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Active=" + isOpen(), this, e);
			}
	}

	/**
	 * Obtiene los parámetros para la carga de la capa
	 *
	 * @return param Parámetros.
	 *
	 * @deprecated
	 */
	public Object getLoadParams() {
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setName(java.lang.String)
	 */
	public void setName(String name) {
		super.setName(name);

		//Si la capa tiene nombre acivamos el estado awake
		if(getLoadParams() != null && name != null)
			try {
				if(isClosed())
					enableAwake();
			} catch (NotAvailableStateException e) {
				RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Active=" + isOpen(), this, e);
			}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#wakeUp()
	 */
	public void wakeUp(){
		if (bufferFactory == null)
			try {
				reload();
			} catch (ReloadLayerException e) {
				// No se ha podido recuperar la capa con exito
			}
	}

	/**
	 * Asignar el estado del raster
	 * @param status
	 */
	public void setStatus(IStatusRaster status){
		this.status = status;
	}

	/**
	 * Obtiene el estado del raster
	 * @return
	 */
	public IStatusRaster getStatus(){
		return this.status;
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#load()
	 */
	public void load() throws LoadLayerException {
		if (isStopped())
			return;

		enableStopped(); // Paramos la capa mientras se hace un load

		String fName = null;
		int test = -1;
		if (params != null && params instanceof File) {
			fName = ((File) params).getAbsolutePath();
			test = fName.indexOf("ecwp:");
		}

		if (test != -1) {
			String urlECW = fName.substring(test + 6);
			fName = "ecwp://" + urlECW;
			System.err.println(test + " " + fName);
		}

		try {
			if (params instanceof String[][]) {
				String[][] files = (String[][]) params;
				MultiRasterDataset[][] dt = new MultiRasterDataset[files.length][files[0].length];
				for (int i = 0; i < files.length; i++)
					for (int j = 0; j < files[i].length; j++)
						dt[i][j] = MultiRasterDataset.open(getProjection(), files[i][j]);
				dataset = new CompositeDataset(dt);
			} else
				if (params == null || params instanceof File) {
					if (fName != null)
						dataset = MultiRasterDataset.open(getProjection(), fName);
				} else
					dataset = MultiRasterDataset.open(getProjection(), params);
		} catch (NotSupportedExtensionException e) {
			throw new LoadLayerException("Formato no valido", e);
		} catch (MosaicNotValidException e) {
			throw new LoadLayerException("Error en el mosaico", e);
		} catch (Exception e) {
			throw new LoadLayerException("No existe la capa.", e);
		}
		if (dataset != null)
			this.init();
	}

	/**
	 * Acciones de inicialización después de que la fuente de datos
	 * de la capa está asignada. El tipo de fuente de datos es variable
	 * puede ser MultiRasterDataset, CompositeDataset u otras que existan e
	 * implementen IRasterDatasource.
	 */
	public void init() throws LoadLayerException {
		if (dataset == null)
			throw new LoadLayerException("Formato no valido", new IOException());

		bufferFactory = new BufferFactory(dataset);
		render = new Rendering(bufferFactory);
		render.addVisualPropertyListener(this);
		initFilters();

		//Inicialización del historico de transformaciones
		affineTransformList.clear();
		affineTransformList.add(this.getAffineTransform());

		try {
			enableOpen();
		} catch (NotAvailableStateException e) {
			RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Awake=" + isAwake(), this, e);
		}
	}

	/**
	 * Obtiene la proyección del fichero.
	 * @return IProjection
	 */
	public IProjection readProjection() {
		try {
			CRS.setCRSFactory(CRSFactory.cp);
			if( dataset == null )
				return null;
			return CRS.convertWktToIProjection(dataset.getWktProjection());
		} catch (RasterDriverException e) {
			RasterToolsUtil.messageBoxError("Problemas accediendo a getWktProjection. Driver no inicializado", this, e);
		}
		return null;
	}

	/**
	 * Crea el objeto renderizador de raster
	 * @return Rendering
	 */
	public Rendering getRender() {
		if (render == null) {
			render = new Rendering(bufferFactory);
			render.addVisualPropertyListener(this);
		}
		return render;
	}

	/**
	 * Aplica los filtros noData al layer
	 * @param rasterSE
	 * @param filterManager
	 */
	public void applyNoData() {
		Boolean noDataEnabled = configuration.getValueBoolean("nodata_transparency_enabled", Boolean.FALSE);
		if (noDataEnabled.booleanValue() && getDataSource().isNoDataEnabled()) {
			noDataType = RasterLibrary.NODATATYPE_LAYER;
			Double noDataValue = Double.valueOf(getNoDataValue());
			getDataSource().getTransparencyFilesStatus().setNoData(noDataValue.doubleValue());
		} else {
			getDataSource().getTransparencyFilesStatus().activeNoData(false);
			noDataType = RasterLibrary.NODATATYPE_DISABLED;
		}
	}

	/**
	 * Filtros añadidos por defecto en la pila para visualización.
	 */
	private void initFilters() {
		RasterFilterList filterList = new RasterFilterList();
		filterList.addEnvParam("IStatistics", getDataSource().getStatistics());
		filterList.addEnvParam("MultiRasterDataset", getDataSource());

		getDataSource().resetNoDataValue();
		applyNoData();
		GridTransparency gridTransparency = new GridTransparency(getDataSource().getTransparencyFilesStatus());

		filterList.setInitDataType(getDataType()[0]);
		RasterFilterListManager filterManager = new RasterFilterListManager(filterList);

		// Quitamos la leyenda
		lastLegend = null;

		try {
			//Si en la carga del proyecto se cargó una tabla de color asignamos esta
			if(loadedFromProject != null) {
				GridPalette p = new GridPalette(loadedFromProject);
				setLastLegend(p);
				ColorTableListManager ctm = new ColorTableListManager(filterManager);
				ctm.addColorTableFilter(p);
			} else
				//sino ponemos la tabla asociada al raster
				if (this.getDataSource().getColorTables()[0] != null) {
				GridPalette p = new GridPalette(getDataSource().getColorTables()[0]);
				setLastLegend(p);
				ColorTableListManager ctm = new ColorTableListManager(filterManager);
				ctm.addColorTableFilter(p);
			} else //sino hace lo que dice en las preferencias
				if (getDataType()[0] != IBuffer.TYPE_BYTE)
					loadEnhancedOrColorTable(filterManager);
			loadedFromProject = null;

			getRender().setFilterList(filterList);
			// Inicializo la transparencia para el render
			getRender().setLastTransparency(gridTransparency);
		} catch (FilterTypeException e) {
			//Ha habido un error en la asignación de filtros por los que no se añade ninguno.
			RasterToolsUtil.debug("Error añadiendo filtros en la inicialización de capa " + this.getName() + " Datatype=" + this.getDataType(), null, e);
		}
	}

	/**
	 * Mira la configuracion para saber si debe cargar un realce o una tabla
	 * de color por defecto
	 * @param filterManager
	 * @throws FilterTypeException
	 */
	private void loadEnhancedOrColorTable(RasterFilterListManager filterManager) throws FilterTypeException {
		String colorTableName = configuration.getValueString("loadlayer_usecolortable", (String) null);

		String palettesPath = System.getProperty("user.home") +
			File.separator +
			"gvSIG" + // PluginServices.getArguments()[0] +
			File.separator + "colortable";

		IStatistics stats = getDataSource().getStatistics();

		if (colorTableName != null)
			try {
				stats.calcFullStatistics();
				if (getBandCount() == 1) {
					ArrayList fileList = ColorTableLibraryPersistence.getPaletteFileList(palettesPath);
					for (int i = 0; i < fileList.size(); i++) {
						ArrayList paletteItems = new ArrayList();
						String paletteName = ColorTableLibraryPersistence.loadPalette(palettesPath, (String) fileList.get(i), paletteItems);
						if (paletteName.equals(colorTableName)) {
							if (paletteItems.size() <= 0)
								continue;

							ColorTable colorTable = new ColorTable();
							colorTable.setName(paletteName);
							colorTable.createPaletteFromColorItems(paletteItems, true);
							colorTable.setInterpolated(true);

							colorTable.createColorTableInRange(stats.getMinimun(), stats.getMaximun(), true);

							GridPalette p = new GridPalette(colorTable);
							setLastLegend(p);

							ColorTableListManager ctm = new ColorTableListManager(filterManager);
							ctm.addColorTableFilter(p);
							return;
						}
					}
				}
			} catch (FileNotOpenException e) {
				// No podemos aplicar el filtro
			} catch (RasterDriverException e) {
				// No podemos aplicar el filtro
			} catch (InterruptedException e) {
				// El usuario ha cancelado el proceso
			}

		/*EnhancementListManager elm = new EnhancementListManager(filterManager);
		elm.addEnhancedFilter(false, stats, 0.0, getRender().getRenderBands());*/

		EnhancementStretchListManager elm = new EnhancementStretchListManager(filterManager);
		try {
			elm.addEnhancedStretchFilter(LinearStretchParams.createStandardParam(getRenderBands(), 0.0, stats, false),
										stats,
										getRender().getRenderBands(),
										false);
		} catch (FileNotOpenException e) {
			//No podemos aplicar el filtro
		} catch (RasterDriverException e) {
			//No podemos aplicar el filtro
		}
	}

	/**
	 * Devuelve si es reproyectable o no la capa
	 * @return
	 */
	public boolean isReproyectable() {
		if (dataset == null)
			return false;

		int nFiles = dataset.getDatasetCount();
		for (int i = 0; i < nFiles; i++)
			if (!dataset.getDataset(i)[0].isReproyectable())
				return false;
		return true;
	}

	/**
	 * @throws ReadException
	 * @throws ReadDriverException
	 * @see com.iver.cit.gvsig.fmap.layers.LayerOperations#draw(java.awt.image.BufferedImage,
	 * 		java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort,
	 * 		com.iver.utiles.swing.threads.Cancellable)
	 */
	public void draw(BufferedImage image, Graphics2D g, ViewPort vp, Cancellable cancel, double scale) throws ReadException {
		this.image = image;
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		task.setEvent(null);

		try {
			if (!isOpen())
				return;

			enableStopped();
			// callLegendChanged(null);

			strategy = new RasterDrawStrategy(getMapContext(), this);
			strategy.stackStrategy();
			HashMap tStr = strategy.getStrategy();
			if (tStr != null &&
				tStr.get(this) != null &&
				((Boolean) (tStr.get(this))).booleanValue() == false) {
				disableStopped();
				return;
			}

			if (isWithinScale(scale)) {
				if (status != null && firstLoad) {
					if (mustTileDraw) {
						Point2D p = vp.getOffset();
						Rectangle r = new Rectangle((int) p.getX(), (int) p.getY(), vp.getImageWidth(), vp.getImageHeight());
						Tiling tiles = new Tiling(maxTileDrawWidth, maxTileDrawHeight, r);
						tiles.setAffineTransform((AffineTransform) vp.getAffineTransform().clone());
						for (int tileNr = 0; tileNr < tiles.getNumTiles(); tileNr++)
							// drawing part
							try {
								ViewPort vport = tiles.getTileViewPort(vp, tileNr);
//								g.setClip(tiles.getClip(tileNr).x, tiles.getClip(tileNr).y, tiles.getClip(tileNr).width - 5, tiles.getClip(tileNr).height);
								draw(image, g, vport, cancel);
							} catch (InterruptedException e) {
								System.out.println("Se ha cancelado el pintado");
							} catch (InvalidSetViewException e) {
								throw new ReadException("Error reading file.", e);
							} catch (RasterDriverException e) {
								throw new ReadException("Error reading file.", e);
							}  catch (NoninvertibleTransformException e) {
								throw new ReadException("Error in the transformation.", e);
							}
					} else
						try {
							draw(image, g, vp, cancel);
						} catch (InterruptedException e) {
							System.out.println("Se ha cancelado el pintado");
						} catch (InvalidSetViewException e) {
							throw new ReadException("Error reading file.", e);
						} catch (RasterDriverException e) {
							throw new ReadException("Error reading file.", e);
						}
					try {
						status.applyStatus(this);
					} catch (NotSupportedExtensionException e) {
						throw new ReadException("Error in input file", e);
					} catch (FilterTypeException e) {
						throw new ReadException("Error setting filters from a project.", e);
					} catch (RasterDriverException e) {
						throw new ReadException("Error reading file.", e);
					}
					firstLoad = false;
				}

				if (mustTileDraw) {
					Point2D p = vp.getOffset();
					Rectangle r = new Rectangle((int) p.getX(), (int) p.getY(), vp.getImageWidth(), vp.getImageHeight());
					Tiling tiles = new Tiling(maxTileDrawWidth, maxTileDrawHeight, r);
					tiles.setAffineTransform((AffineTransform) vp.getAffineTransform().clone());
					for (int tileNr = 0; tileNr < tiles.getNumTiles(); tileNr++)
						// drawing part
						try {
							ViewPort vport = tiles.getTileViewPort(vp, tileNr);
							draw(image, g, vport, cancel);
						} catch (InterruptedException e) {
							System.out.println("Se ha cancelado el pintado");
						} catch (InvalidSetViewException e) {
							throw new ReadException("Error reading file.", e);
						} catch (RasterDriverException e) {
							throw new ReadException("Error reading file.", e);
						}  catch (NoninvertibleTransformException e) {
							throw new ReadException("Error in the transformation.", e);
						}
				} else
					try {
						draw(image, g, vp, cancel);
					} catch (InterruptedException e) {
						System.out.println("Se ha cancelado el pintado");
					} catch (InvalidSetViewException e) {
						throw new ReadException("Error reading file.", e);
					} catch (RasterDriverException e) {
						throw new ReadException("Error reading file.", e);
					}

			}
			//callLegendChanged(null);
		} finally {
			disableStopped();
			task.setEvent(null);
		}
	}

	private void draw(BufferedImage image, Graphics2D g, ViewPort vp, Cancellable cancel) throws RasterDriverException, InvalidSetViewException, InterruptedException {
		Envelope adjustedExtent = vp.getAdjustedExtent();
		if (adjustedExtent == null)
			return;
		Extent e = new Extent(adjustedExtent.getLowerCorner().getX(),
				adjustedExtent.getUpperCorner().getY(), adjustedExtent
						.getUpperCorner().getX(),
				adjustedExtent
						.getLowerCorner().getY());
		Dimension imgSz = vp.getImageSize();
		ViewPortData vp2 = new ViewPortData(vp.getProjection(), e, imgSz );
		vp2.setMat(vp.getAffineTransform());
		getRender().draw(g, vp2);
	}

	/**
	 * Inserta la proyección.
	 *
	 * @param proj Proyección.
	 */
	public void setProjection(IProjection proj) {
		super.setProjection(proj);
	}

	/*
	 * @see com.iver.cit.gvsig.fmap.layers.LayerOperations#getFullExtent()
	 */
	public Envelope getFullEnvelope() {
		//TODO:DEPURACION Comentamos !isOpen porque getFullExtent de FLayers da una excepción ya que siempre espera
		//un extent aunque la capa no esté abierta
		if(/*!isOpen() || */dataset == null || dataset.getExtent() == null)
			return null;

		Rectangle2D e = dataset.getExtent().toRectangle2D();
		try {
			return geomManager.createEnvelope(e.getX(), e.getY(), e.getMaxX(), e
					.getMaxY(), SUBTYPES.GEOM2D);
		} catch (CreateEnvelopeException e1) {
			logger.error("Error creating the envelope", e);
			return null;
		}
	}

	/**
	 * Obtiene el valor del pixel del Image en la posición x,y
	 * @param x Posición x
	 * @param y Posición y
	 * @return valor de pixel
	 */
	public int[] getPixel(int pxx, int pxy) {
		int[] argb = { -1, -1, -1, -1 };
		if (!isOpen() || (image == null))
			return argb;
		if (pxx >= 0 && pxx < image.getWidth() && pxy >= 0 && pxy < image.getHeight()) {
			int value = image.getRGB(pxx, pxy);
			argb[0] = ((value & 0xff000000) >> 24);
			argb[1] = ((value & 0x00ff0000) >> 16);
			argb[2] = ((value & 0x0000ff00) >> 8);
			argb[3] = (value & 0x000000ff);
		}
		return argb;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getMaxX()
	 */
	public double getMaxX() {
		if(getFullEnvelope() != null)
			return getFullEnvelope().getMaximum(0);
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getMaxY()
	 */
	public double getMaxY() {
		if(getFullEnvelope() != null)
			return this.getFullEnvelope().getMaximum(1);
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getMinX()
	 */
	public double getMinX() {
		if(getFullEnvelope() != null)
			return getFullEnvelope().getMinimum(0);
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getMinY()
	 */
	public double getMinY() {
		if(getFullEnvelope() != null)
			return getFullEnvelope().getMinimum(1);
		return -1;
	}

	/* (non-Javadoc)
	 * @deprecated. See String getInfo(Point p) throws DriverException
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.InfoByPoint#queryByPoint(java.awt.Point)
	 */
	public String queryByPoint(Point p) {
		if (!isOpen())
			return null;
		ColorConversion conv = new ColorConversion();

		String data = "<file:" + normalizeAsXMLTag(getName()) + ">\n";

		ArrayList attr = getAttributes();
		data += "  <raster\n";
		data += "    File=\"" + getFile() + "\"\n";
		for (int i = 0; i < attr.size(); i++) {
			Object[] a = (Object[]) attr.get(i);

			data += "    " + a[0].toString() + "=";
			if (a[1].toString() instanceof String)
				data += "\"" + a[1].toString() + "\"\n";
			else
				data += a[1].toString() + "\n";
		}
		data += "    Point=\"" + posX + " , " + posY + "\"\n";
		data += "    Point_WC=\"" + MathUtils.format(posXWC, 3) + " , " + MathUtils.format(posYWC, 3) + "\"\n";
		data += "    RGB=\"" + r + ", " + g + ", " + b + "\"\n";
		double[] cmyk = conv.RGBtoCMYK(r & 0xff, g & 0xff, b & 0xff, 1D);
		data += "    CMYK=\"" + MathUtils.format(cmyk[0], 4) + ", " + MathUtils.format(cmyk[1], 4) + ", " + MathUtils.format(cmyk[2], 4) + "," + MathUtils.format(cmyk[3], 4) + "\"\n";
		double[] hsl = conv.RGBtoHSL(r & 0xff, g & 0xff, b & 0xff);
		hsl[0] = (int)(255.0 * hsl[0] / 360.0 + 0.5);
		hsl[2] = (int) (hsl[2] * 255. + 0.5);
		hsl[1] = (int) (hsl[1] * 255. + 0.5);
		data += "    HSL=\"" + MathUtils.format(hsl[0], 4) + ", " + MathUtils.format(hsl[1], 4) + ", " + MathUtils.format(hsl[2], 4) + "\"\n";
		data += "  />\n";

		data += "</file:" + normalizeAsXMLTag(getName()) + ">\n";
		return data;
	}

	/**
	 * Transforma un punto real a coordenadas pixel indicando la banda que es usada para la
	 * transformación. Hay que tener en cuenta que es posible que todas las transformaciones no
	 * sean iguales en todas la bandas porque puede haber bandas de distinta resolución.
	 *
	 * @param numberBand
	 * @param pReal
	 * @return
	 * @throws ReadDriverException
	 */
	private Point2D transformPoint(int numberBand, Point2D pReal) throws NoninvertibleTransformException {
		AffineTransform at = this.getDataSource().getAffineTransform(numberBand);
		Point2D px = new Point2D.Double();
		//px = new Point2D.Double(pReal.getX(), pReal.getY());
		at.inverseTransform(pReal, px);
		return px;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.InfoByPoint#getInfo(java.awt.Point, double, com.iver.utiles.swing.threads.Cancellable)
	 */
	public XMLItem[] getInfo(Point p, double tolerance, Cancellable cancel)
			throws ReadException {
		if (!isOpen()) {
			StringXMLItem[] item = new StringXMLItem[1];
			String data = "<file:" + normalizeAsXMLTag(getName()) + ">\n";
			data += "  <raster\n" + "  Layer=\" Not available\"\n" + "  />\n";
			data += "</file:" + normalizeAsXMLTag(getName()) + ">\n";
			item[0] = new StringXMLItem(data, this);
			return item;
		}

		Point2D pReal = getMapContext().getViewPort().toMapPoint(p);
		Point2D px = new Point2D.Double();
		if(	pReal.getX() > this.getMinX() &&
			pReal.getX() < this.getMaxX() &&
			pReal.getY() > this.getMinY() &&
			pReal.getY() < this.getMaxY())
			try {
				px = transformPoint(0, pReal);
			} catch (NoninvertibleTransformException e) {
				throw new ReadException("Error in the transformation", e);
			}
		int[] rgb = getPixel((int) p.getX(), (int) p.getY());
		ColorConversion conv = new ColorConversion();

		StringXMLItem[] item = new StringXMLItem[1];
		String data = "<file:" + normalizeAsXMLTag(getName()) + ">\n";

		data += "  <raster\n";
		data += "    View_Point=\"" + p.getX() + " , " + p.getY() + "\"\n";
		data += "    World_Point=\"" + MathUtils.format(pReal.getX(), 3) + " , " + MathUtils.format(pReal.getY(), 3) + "\"\n";
		if (px == null)
			data += "    Pixel_Point=\"Out\"\n";
		else
			data += "    Pixel_Point=\"" + (int) px.getX() + " , " + (int) px.getY() + "\"\n";
		data += "    RGB=\"" + rgb[1] + "  " + rgb[2] + "  " + rgb[3] + "\"\n";
		double[] cmyk = conv.RGBtoCMYK(rgb[1] & 0xff, rgb[2] & 0xff, rgb[3] & 0xff, 1D);
		data += "    CMYK=\"" + MathUtils.format(cmyk[0], 4) + ", " + MathUtils.format(cmyk[1], 4) + ", " + MathUtils.format(cmyk[2], 4) + "," + MathUtils.format(cmyk[3], 4) + "\"\n";
		double[] hsl = conv.RGBtoHSL(rgb[1] & 0xff, rgb[2] & 0xff, rgb[3] & 0xff);
		hsl[0] = (int)(255.0 * hsl[0] / 360.0 + 0.5);
		hsl[2] = (int) (hsl[2] * 255. + 0.5);
		hsl[1] = (int) (hsl[1] * 255. + 0.5);
		data += "    HSL=\"" + MathUtils.format(hsl[0], 4) + ", " + MathUtils.format(hsl[1], 4) + ", " + MathUtils.format(hsl[2], 4) + "\"\n";
		data += "    Band_Value=\"";
		try {
			if (px != null) {
				if(getDataType()[0] >= 0 && getDataType()[0] <= 3)
					for(int i = 0; i < getBandCount(); i++)
						if(getDataSource().isInside(pReal)) {
							Point2D pxAux = transformPoint(i, pReal);
							data += ((Integer)getDataSource().getData((int)pxAux.getX(), (int)pxAux.getY(), i)).intValue() + "  ";
						}
				if(getDataType()[0] == 4)
					for(int i = 0; i < getBandCount(); i++)
						if(getDataSource().isInside(pReal)) {
							Point2D pxAux = transformPoint(i, pReal);
							data += ((Float)getDataSource().getData((int)pxAux.getX(), (int)pxAux.getY(), i)).floatValue() + "  ";
						}
				if(getDataType()[0] == 5)
					for(int i = 0; i < getBandCount(); i++)
						if(getDataSource().isInside(pReal)) {
							Point2D pxAux = transformPoint(i, pReal);
							data += ((Double)getDataSource().getData((int)pxAux.getX(), (int)pxAux.getY(), i)).doubleValue() + "  ";
						}
			}
		} catch (RasterDriverException ex) {
			throw new ReadException("Error en el acceso al dataset", ex);
		} catch (InvalidSetViewException ex) {
			throw new ReadException(
					"Error en la asignación de la vista en getData", ex);
		} catch (FileNotOpenException ex) {
			throw new ReadException("Fichero no abierto en el dataset", ex);
		} catch (NoninvertibleTransformException ex) {
			throw new ReadException("Error in the transformation", ex);
		}
		data += "\"\n";
		data += "  />\n";
		data += "</file:" + normalizeAsXMLTag(getName()) + ">\n";

		item[0] = new StringXMLItem(data, this);
		return item;
	}

	/**
	 * Filters a string for being suitable as XML Tag, erasing
	 * all not alphabetic or numeric characters.
	 * @param s
	 * @return string normalized
	 */
	private String normalizeAsXMLTag(String s) {
		return s.replaceAll("[^a-zA-Z0-9]", "");
	}

	/**
	 * Obtiene atributos a partir de un georasterfile
	 * @return
	 */
	public ArrayList getAttributes() {
		ArrayList attr = new ArrayList();
		if(!isOpen())
			return attr;
		Object [][] a = {
			{"Filename", dataset.getDataset(0)[0].getFName()},
			{"Filesize", new Long(dataset.getFileSize())},
			{"Width", new Integer((int)dataset.getWidth())},
			{"Height", new Integer((int)dataset.getHeight())},
			{"Bands", new Integer(dataset.getBandCount())}
		};
		for (int i = 0; i < a.length; i++)
			attr.add(a[i]);
		return attr;
	}

	/**
	 * Escribe en el proyecto la capa actual
	 * @throws XMLException
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getProperties()
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		if(isClosed() || isAwake())
			return null;

		XMLEntity xml = super.getXMLEntity();
		if(getFile() != null)
			xml.putProperty("file", getFile());
		xml.putProperty("driverName", "gvSIG Raster Driver");

		// Si no hay ningín Status aplicamos el StatusLayerRaster que se usa por defecto
		if (status == null)
			status = new StatusLayerRaster();
		status.getXMLEntity(xml, true, this);

		return xml;
	}

	/**
	 * Recupera de disco los datos de la capa.
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException {
		for (int i = 0; i < xml.getPropertyCount(); i++) {
			String key = xml.getPropertyName(i);
			if(key.startsWith("raster.file")) {
				if(xml.getPropertyValue(i).startsWith(RasterLibrary.getTemporalPath()))
					throw new XMLLayerException("Trying to load temporary layer", null);
			}
		}
		super.setXMLEntity(xml);

		try {
			params = new File(xml.getStringProperty("file"));

			if(params != null && getName() != null && getName().compareTo("") != 0)
				try {
					enableAwake();
				} catch (NotAvailableStateException e) {
					RasterToolsUtil.messageBoxError("Fallo el estado de open. Closed=" + isClosed() + " Active=" + isOpen(), this, e);
				}
			if(!super.getFLayerStatus().visible)
				enableStopped();

			// Para notificar al adapter-driver cual es la proyección.
			setProjection(super.getProjection());

			//Inicializamos la clase a la que se usa por defecto para
			//compatibilidad con proyectos antiguos
			String claseStr = StatusLayerRaster.defaultClass;
			if (xml.contains("raster.class"))
				claseStr = xml.getStringProperty("raster.class");

			if (status != null)
				status.setXMLEntity(xml, this);
			else if (claseStr != null && !claseStr.equals(""))
				try {
					// Class clase =
					// LayerFactory.getLayerClassForLayerClassName(claseStr);
					Class clase = this.getClass();
					Constructor constr = clase.getConstructor(null);
					status = (IStatusRaster) constr.newInstance(null);
					if (status != null) {
						((StatusLayerRaster)status).setNameClass(claseStr);
						status.setXMLEntity(xml, this);
						filterArguments = status.getFilterArguments();

						//Creamos la tabla de color
						ArrayList color = (ArrayList) filterArguments.clone();
						loadedFromProject = ColorTableListManager.createColorTableFromArray(color);
					}
					// } catch (ClassNotFoundException exc) {
					// throw new XMLLayerException("", exc);
				} catch (InstantiationException exc) {
					throw new XMLLayerException("", exc);
				} catch (IllegalAccessException exc) {
					throw new XMLLayerException("", exc);
				} catch (NoSuchMethodException exc) {
					throw new XMLLayerException("", exc);
				} catch (InvocationTargetException exc) {
					throw new XMLLayerException("", exc);
				}
			firstLoad = true;
		} catch (NotExistInXMLEntity e) {

		}
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#print(java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.cit.gvsig.fmap.operations.Cancellable)
	 */
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale, PrintAttributes propeties) throws ReadException {

		if (!isOpen() || !isVisible() || !isWithinScale(scale))
			return;

		if (!mustTilePrint)
			draw(null, g, viewPort, cancel,scale);
		else {
			// Para no pedir imagenes demasiado grandes, vamos
			// a hacer lo mismo que hace EcwFile: chunkear.
			// Llamamos a drawView con cuadraditos más pequeños
			// del BufferedImage ni caso, cuando se imprime viene con null
			Tiling tiles = new Tiling(maxTilePrintWidth, maxTilePrintHeight, g.getClipBounds());
			tiles.setAffineTransform((AffineTransform) viewPort.getAffineTransform().clone());

			//Si es la primera lectura salvamos los valores de máximo y mínimo para la aplicación
			//de realce si la imagen es de 16 bits.

			//RasterStats stats = getSource().getFilterStack().getStats();
			//if(stats != null)
			//stats.history.add(stats.new History(getName(), stats.minBandValue, stats.maxBandValue, stats.secondMinBandValue, stats.secondMaxBandValue));


			for (int tileNr = 0; tileNr < tiles.getNumTiles(); tileNr++)
				// Parte que dibuja
				try {
					ViewPort vp = tiles.getTileViewPort(viewPort, tileNr);
					draw(null, g, vp, cancel, scale);
				} catch (NoninvertibleTransformException e) {
					throw new ReadException("Error en la transformación.", e);
				}
		}
	}

	public void _print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale) throws ReadException {
		if(!isOpen())
			return;

		// Para no pedir imagenes demasiado grandes, vamos
		// a hacer lo mismo que hace EcwFile: chunkear.
		// Llamamos a drawView con cuadraditos más pequeños
		// del BufferedImage ni caso, cuando se imprime viene con null

		int numW, numH;
		int stepX, stepY;
		int xProv, yProv;
		int A = 1500;
		int H = 1500;
		int altoAux, anchoAux;

		AffineTransform mat = (AffineTransform) viewPort.getAffineTransform().clone();

		// Vamos a hacerlo en trozos de AxH
		Rectangle r = g.getClipBounds();
		numW = (r.width) / A;
		numH = (r.height) / H;

		double[] srcPts = new double[8];
		double[] dstPts = new double[8];

		yProv = r.y;
		for (stepY = 0; stepY < numH + 1; stepY++) {
			if ((yProv + H) > r.getMaxY())
				altoAux = (int) r.getMaxY() - yProv;
			else
				altoAux = H;

			xProv = r.x;
			for (stepX = 0; stepX < numW + 1; stepX++) {
				if ((xProv + A) > r.getMaxX())
					anchoAux = (int) r.getMaxX() - xProv;
				else
					anchoAux = A;

				//Rectangle newRect = new Rectangle(xProv, yProv, anchoAux, altoAux);

				// Parte que dibuja
				srcPts[0] = xProv;
				srcPts[1] = yProv;
				srcPts[2] = xProv + anchoAux + 1;
				srcPts[3] = yProv;
				srcPts[4] = xProv + anchoAux + 1;
				srcPts[5] = yProv + altoAux + 1;
				srcPts[6] = xProv;
				srcPts[7] = yProv + altoAux + 1;

				try {
					mat.inverseTransform(srcPts, 0, dstPts, 0, 4);
					Rectangle2D.Double rectCuadricula = new Rectangle2D.Double(dstPts[0], dstPts[1], dstPts[2] - dstPts[0], dstPts[5] - dstPts[3]);
					// Extent extent = new Extent(rectCuadricula);

					Dimension tam = new Dimension(anchoAux + 1, altoAux + 1);
					ViewPort vp = viewPort.cloneViewPort();
					vp.setImageSize(tam);
					Envelope env = geomManager.createEnvelope(rectCuadricula
							.getMinX(), rectCuadricula.getMinY(),
							rectCuadricula.getMaxX(), rectCuadricula.getMaxY(),
							SUBTYPES.GEOM2D);
					vp.setEnvelope(env);
					vp.setAffineTransform(mat);
					draw(null, g, vp, cancel, scale);

				} catch (NoninvertibleTransformException e) {
					//throw new ReadDriverException("Error en la transformación.", e);
				} catch (ReadException e) {
					//throw new ReadDriverException("Error en la transformación.", e);
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
				}
				// Fin parte que dibuja
				xProv = xProv + A;
			}
			yProv = yProv + H;
		}
	}

	/**
	 * Borra de la lista de listeners el que se pasa como parámetro.
	 *
	 * @param o LayerListener a borrar.
	 *
	 * @return True si ha sido correcto el borrado del Listener.
	 */
	public boolean removeLayerListener(LayerListener o) {
		// Salva a RMF
		if (this.getDataSource() != null)
			// Guardamos la GeoReferenciacion de cada dataset
			try {
				for (int i = 0; i < getDataSource().getDatasetCount(); i++)
					getDataSource().saveObjectToRmf(i, RasterDataset.class, getDataSource().getDataset(i)[0]);
			} catch (RmfSerializerException e) {
				RasterToolsUtil.messageBoxError("error_salvando_rmf", this, e);
			}

		if (this.isRemoveRasterFlag()) {
			String[] files = getFileName().clone();
			if (dataset != null)
				dataset.close();
			if (bufferFactory != null)
				bufferFactory.free();
			bufferFactory = null;
			dataset = null;
			render = null;
			try {
				enableClosed();
			} catch (NotAvailableStateException e1) {
				// No se ha podido cambiar el estado de la capa a cerrado
			}

			// System.gc();
			this.setRemoveRasterFlag(true);

			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i]);
				File dirTemp = RasterLibrary.getTemporalFile();
				if (dirTemp.compareTo(file.getParentFile()) == 0) {
					file.delete();

					// Borramos todos los ficheros que puedan tener relacion con el fichero actual
					String basefile = file.getName();
					File basepath = file.getParentFile();
					int last = basefile.lastIndexOf(".");
					if (last != -1)
						basefile = basefile.substring(0, last + 1);
					File[] list = basepath.listFiles();
					for (int j = 0; j < list.length; j++)
						if (list[j].getName().startsWith(basefile))
							list[j].delete();
				}
			}
		}
		updateDrawVersion();
		return super.layerListeners.remove(o);
	}

	/**
	 * @return Returns the removeRasterFlag.
	 */
	public boolean isRemoveRasterFlag() {
		return removeRasterFlag;
	}

	/**
	 * Asigna el valor del flag que dice si destruimos la memoria del raster
	 * al eliminarlo del TOC o  no.
	 * @param removeRasterFlag The removeRasterFlag to set.
	 */
	public void setRemoveRasterFlag(boolean removeRasterFlag) {
		this.removeRasterFlag = removeRasterFlag;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getTocImageIcon()
	 */
	public ImageIcon getTocImageIcon() {
		return new ImageIcon(getClass().getResource("images/map_ico_ok.gif"));
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.RasterOperations#getTileSize()
	 */
	public int[] getTileSize() {
		int[] size = {maxTileDrawWidth, maxTileDrawHeight};
		return size;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.RasterOperations#isTiled()
	 */
	public boolean isTiled() {
		return mustTileDraw;
	}

	/**
	 * Obtiene el flag que dice si la imagen está o no georreferenciada
	 * @return true si está georreferenciada y false si no lo está.
	 */
	public boolean isGeoreferenced() {
		return dataset.isGeoreferenced();
	}

	/**
	 * Get datasource object
	 * @return
	 */
	public BufferFactory getBufferFactory(){
		return bufferFactory;
	}

	/**
	 * Obtiene el valor NoData asociado al raster.
	 * @return double
	 */
	public double getNoDataValue() {
		if (dataset == null)
			return RasterLibrary.defaultNoDataValue;
		return dataset.getNoDataValue();
	}

	/**
	 * Asigna el valor no data asociado a la capa
	 * @param nd
	 */
	public void setNoDataValue(double nd) {
		if (bufferFactory != null)
			bufferFactory.setNoDataToFill(nd);
		if (dataset != null)
			dataset.setNoDataValue(nd);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterOperations#getPXHeight()
	 */
	public double getPxHeight() {
		return dataset.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterOperations#getPxWidth()
	 */
	public double getPxWidth() {
		return dataset.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IGeoDimension#getWCHeight()
	 */
	public double getWCHeight() {
		return getFullEnvelope().getMaximum(1);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IGeoDimension#getWCWidth()
	 */
	public double getWCWidth() {
		return getFullEnvelope().getMaximum(0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterFile#getFileSize()
	 */
	public long[] getFileSize(){
		int nFiles = dataset.getDatasetCount();
		long[] s = new long[nFiles];
		for (int i = 0; i < nFiles; i++)
			s[i] = dataset.getDataset(i)[0].getFileSize();
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterFile#getFileName()
	 */
	public String[] getFileName(){
		int nFiles = 0;
		if (dataset != null)
			nFiles = dataset.getDatasetCount();
		String[] s = new String[nFiles];
		for (int i = 0; i < nFiles; i++)
			s[i] = dataset.getDataset(i)[0].getFName();
		return s;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterFile#getFileCount()
	 */
	public int getFileCount() {
		return (dataset != null) ? dataset.getDatasetCount() : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterFile#getFileFormat()
	 */
	public String getFileFormat() {
		String fName = dataset.getDataset(0)[0].getFName();
		int index = fName.lastIndexOf(".") + 1;
		String ext = null;
		if (index > 0)
			ext = fName.substring(fName.lastIndexOf(".") + 1, fName.length());
		return ext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterOperations#getBandCount()
	 */
	public int getBandCount() {
		return (dataset != null) ? dataset.getBandCount() : 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterOperations#getDatatype()
	 */
	public int[] getDataType() {
		return dataset.getDataType();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterRendering#getRenderTransparency()
	 */
	public GridTransparency getRenderTransparency() {
		return getRender().getLastTransparency();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterRendering#getRenderFilterList()
	 */
	public RasterFilterList getRenderFilterList() {
		return getRender().getFilterList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#getRenderBands()
	 */
	public int[] getRenderBands() {
		return getRender().getRenderBands();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#setRenderBands(int[])
	 */
	public void setRenderBands(int[] renderBands) {
		getRender().setRenderBands(renderBands);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#setRenderFilterList(org.gvsig.raster.grid.filter.RasterFilterList)
	 */
	public void setRenderFilterList(RasterFilterList filterList) {
		getRender().setFilterList(filterList);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterDataset#getDataSource()
	 */
	public IRasterDataSource getDataSource() {
		return dataset;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterDataset#addFile(java.lang.String)
	 */
	public void addFile(String fileName) throws NotSupportedExtensionException, RasterDriverException {
		if (getRender() != null)
			bufferFactory.addFile(RasterDataset.open(getProjection(), fileName));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterDataset#delFile(java.lang.String)
	 */
	public void delFile(String fileName) {
		if (getRender() != null)
			bufferFactory.removeFile(fileName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterDataset#getInfo(java.lang.String)
	 */
	public Object getInfo(String key) {
		if (key.equals("DriverName"))
			return "gvSIG Raster Driver";
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterOperations#getMetadata()
	 */
	public DatasetMetadata[] getMetadata() {
		int count = dataset.getDatasetCount();
		DatasetMetadata[] metadata = new DatasetMetadata[count];
		for (int i = 0; i < count; i++)
			metadata[i] = dataset.getDataset(i)[0].getMetadata();
		return metadata;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterOperations#getBandCountFromDataset()
	 */
	public int[] getBandCountFromDataset() {
		int count = dataset.getDatasetCount();
		int[] bands = new int[count];
		for (int i = 0; i < count; i++)
			bands[i] = dataset.getDataset(i)[0].getBandCount();
		return bands;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterOperations#getColourInterpretation(int, int)
	 */
	public String getColorInterpretation(int band, int dataset) {
		if (this.dataset.getDataset(dataset)[0].getColorInterpretation().get(band) == null)
			return "Undefined";
		return this.dataset.getDataset(dataset)[0].getColorInterpretation().get(band);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getStringProjection()
	 */
	public String getWktProjection() throws RasterDriverException {
		return dataset.getWktProjection();
	}

	/**
	 * Metodo para consultar si una capa puede ser un RGB. Suponemos que es un RGB
	 * si el tipo de datos es de tipo byte y su interpretacion de color tiene
	 * asignada los tres colores.
	 * @return boolean
	 */
	public boolean isRGB() {
		if ((dataset == null) || (render == null))
			return false;

// Quitado pq no necesariamente tiene pq tener 3 bandas para ser RGB
//		if (dataset.getBandCount() < 3)
//			return false;

		if (dataset.getDataType()[0] != IBuffer.TYPE_BYTE)
			return false;

		boolean R = false;
		boolean G = false;
		boolean B = false;

		int[] renderBands = render.getRenderBands();
		for (int i = 0; i < renderBands.length; i++)
			if (renderBands[i] >= 0)
				switch (i) {
					case 0:
						R = true;
						break;
					case 1:
						G = true;
						break;
					case 2:
						B = true;
						break;
				}

		if (R && G && B)
			return true;

		return false;
	}

	/**
	 * Obtiene el grid de la capa completa. Hay que tener cuidado porque cuando se hace esta
	 * petición se carga un buffer con todos los datos de la capa. Este buffer puede ser
	 * cacheado o no dependiendo del tamaño de esta.
	 * @param interpolated true si se solicita un grid interpolado y false si se solicita sin interpolar.
	 * @return Grid.
	 * @throws InterruptedException
	 */
	public Grid getFullGrid(boolean interpolated) throws GridException, InterruptedException {
		BufferFactory bf = getBufferFactory();
		bf.clearDrawableBand();
		bf.setAllDrawableBands();
		try {
			bf.setAreaOfInterest();
		} catch (RasterDriverException e) {
			throw new GridException("Error reading buffer");
		}
		return new Grid(bf, interpolated);
	}

	/**
	 * Obtiene el grid de la capa completa. Esta llamada devuelve un buffer de solo lectura
	 * @param interpolated true si se solicita un grid interpolado y false si se solicita sin interpolar.
	 * @return Grid.
	 * @throws InterruptedException
	 */
	public Grid getReadOnlyFullGrid(boolean interpolated) throws GridException, InterruptedException {
		BufferFactory bf = getBufferFactory();
		bf.setReadOnly(true);
		bf.clearDrawableBand();
		bf.setAllDrawableBands();
		try {
			bf.setAreaOfInterest();
		} catch (RasterDriverException e) {
			throw new GridException("Error reading buffer");
		}
		return new Grid(bf, interpolated);
	}

	/**
	 * Obtiene el tamaño de celda de la fuente de datos
	 * @return double con el tamaño de celda
	 */
	public double getCellSize() {
		return (getDataSource() != null) ? getDataSource().getCellSize() : 1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterGeoOperations#getFullRasterExtent()
	 */
	public Extent getFullRasterExtent() {
		return this.getDataSource().getExtent();
	}


	/**
	 * Devuelve el fichero asociado a la capa o null si no tiene.
	 * @return Fichero.
	 */
	public File getFile() {
		return (params instanceof File) ? ((File)params) : null;
	}

	/**
	 * Consulta si un fichero es aceptado o no para este tipo de capa.
	 * @param file Fichero a consultar
	 * @return true si es aceptado y false si no lo es.
	 */
	public static boolean isFileAccepted(File file) {
		return RasterDataset.fileIsSupported(file.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.shared.IRasterRendering#existColorTable()
	 */
	public boolean existColorTable() {
		return getRender().existColorTable();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#existsAlphaBand()
	 */
	public boolean existsAlphaBand() {
		if(getDataSource().getColorInterpretation() != null)
			return getDataSource().getColorInterpretation().isAlphaBand();
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#getAlphaBandNumber()
	 */
	public int getAlphaBandNumber() {
		if(getDataSource().getColorInterpretation() != null)
			return getDataSource().getColorInterpretation().getBand(DatasetColorInterpretation.ALPHA_BAND);
		return -1;
	}

	/**
	 * Define la ultima leyenda valida de la capa o se pone a null para que la
	 * capa busque una leyenda valida.
	 * @param ct
	 */
	public void setLastLegend(ColorTable ct) {
		lastLegend = ColorTableLegend.createLegend(ct);
	}

	/**
	 * Devuelve la Leyenda de la capa.
	 * @return Leyenda.
	 */
	public ILegend getLegend() {
		if (lastLegend != null)
			return lastLegend;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#addLegendListener(com.iver.cit.gvsig.fmap.layers.LegendListener)
	 */
	public void addLegendListener(LegendListener listener) {
		layerChangeSupport.addLayerListener(listener);
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#getShapeType()
	 */
	public int getShapeType() {
		return TYPES.SURFACE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.Classifiable#removeLegendListener(com.iver.cit.gvsig.fmap.layers.LegendListener)
	 */
	public void removeLegendListener(LegendListener listener) {
		layerChangeSupport.removeLayerListener(listener);
	}

	/**
	 * Metodo que obtiene si un punto cae dentro de los límites de la capa
	 * o fuera de ellos.
	 * @param p Punto a calcular
	 * @return true si está dentro de los límites y false si está fuera
	 */
	public boolean isInside(Point2D p) {
		 return getDataSource().isInside(p);
	}

	/**
	 * Recupera del raster la matriz de transformación que lo situa en cualquier parte de la vista
	 * @return AffineTransform
	 */
	public AffineTransform getAffineTransform(int band) {
		return getDataSource().getAffineTransform(band);
	}

	/**
	 * Recupera del raster la matriz de transformación que lo situa en cualquier parte de la vista
	 * @return AffineTransform
	 */
	public AffineTransform getAffineTransform() {
		return getDataSource().getAffineTransform(0);
	}

	/**
	 * Asigna al raster la matriz de transformación para situarlo en cualquier parte de la vista
	 * @param transf
	 */
	public void setAffineTransform(AffineTransform transf) {
		if(transf == null)
			return;
		affineTransformList.add(transf);
		getDataSource().setAffineTransform(transf);
		updateDrawVersion();
	}

	/**
	 * Asigna al raster la matriz de transformación para situarlo en cualquier parte de la vista.
	 * Esta versión no guarda en el historico.
	 * @param transf
	 */
	public void setAT(AffineTransform transf) {
		getDataSource().setAffineTransform(transf);
		updateDrawVersion();
	}

	/**
	 * Obtiene la lista de transformaciones que se han ido aplicando al raster.
	 * @return Historical. Lista de AffineTransform
	 */
	public Historical getAffineTransformHistorical() {
		return this.affineTransformList;
	}

	/**
	 * Salva la georreferenciación a fichero rmf.
	 * @param fName
	 * @throws RmfSerializerException
	 */
	public void saveGeoToRmf() throws RmfSerializerException {
		if (!isOpen())
			return;

		// Guardamos la GeoReferenciacion de cada dataset
		for (int i = 0; i < getDataSource().getDatasetCount(); i++)
			getDataSource().saveObjectToRmf(i, RasterDataset.class, getDataSource().getDataset(i)[0]);

		affineTransformList.clear();
		affineTransformList.add(this.getAffineTransform());
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.IRasterLayerActions#isActionEnabled(int)
	 */
	public boolean isActionEnabled(int action) {
		switch (action) {
			case IRasterLayerActions.BANDS_FILE_LIST:
				if (existColorTable())
					return false;
				break;
			case IRasterLayerActions.BANDS_RGB:
				if (existColorTable())
					return false;
				break;
			case IRasterLayerActions.REPROJECT:
				if (!isReproyectable())
					return false;
				break;
			case IRasterLayerActions.CREATEOVERVIEWS:
				return overviewsSupport();
			case IRasterLayerActions.OPACITY:
			case IRasterLayerActions.TRANSPARENCY:
			case IRasterLayerActions.BRIGHTNESSCONTRAST:
			case IRasterLayerActions.ENHANCED:
			case IRasterLayerActions.PANSHARPENING:
			case IRasterLayerActions.SELECT_LAYER:
			case IRasterLayerActions.SAVE_COLORINTERP:
				return true;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setVisible(boolean)
	 */
	public void setVisible(boolean visibility) {
		if(visibility)
			state.disableStopped();
		else
			enableStopped();

		if(isAwake() || isClosed())
			try {
				this.load();
			} catch (LoadLayerException e) {
				e.printStackTrace();
			}

		/*
		 * Cuando se modifica la visibilidad de una capa raster se hace un updateDrawVersion de todas las
		 * capas raster de ese MapContext. Esto es porque la estrategia utilizada por RasterDrawStrategy hace
		 * que se cacheen en blanco las capas raster que están ocultas debajo de otras. Al hacer invisibles las
		 * de arriba la cache que estaba en blanco hace que no se pinte nada. Para evitar esto las marcamos todas
		 * como que han sido modificadas para que se vuelvan a leer.
		 */
		if(getMapContext() != null) {
			ArrayList listLayers = new ArrayList();
			listLayers = RasterDrawStrategy.getLayerList(getMapContext().getLayers(), listLayers);
			for (int i = 0; i < listLayers.size(); i++)
				if(listLayers.get(i) instanceof FLyrRasterSE)
					((FLyrRasterSE)listLayers.get(i)).updateDrawVersion();
		}

		super.setVisible(visibility);
	}

	/**
	 * Consulta la transparencia asignada en la última renderización de la capa
	 * @return valor de transparencia
	 */
	public int getTransparency() {
		try {
			return getRenderTransparency().getOpacity();
		} catch (NullPointerException e) {
			return super.getTransparency();
		}
	}

	/**
	 * Consulta si tiene aplicada alguna transparencia en la última renderización
	 * o no.
	 * @return true si se aplicó alguna transparencia en la última renderización.
	 */
	public boolean isTransparent() {
		return getRenderTransparency().isTransparencyActive();
	}

	/**
	 * Asigna la transparencia de la siguiente renderización
	 * @param valor de transparencia
	 */
	public void setTransparency(int trans) {
		super.setTransparency(trans);
		try {
			getRenderTransparency().setOpacity(trans);
			getRenderTransparency().activeTransparency();
		} catch (NullPointerException e) {
			//Solo asigna la transparencia a la clase padre y no a la renderización
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#getLastRenderBuffer()
	 */
	public IBuffer getLastRenderBuffer() {
		return getRender().getLastRenderBuffer();
	}

	/**
	 *
	 * @return ROIs asociadas a la capa raster.
	 */
	public ArrayList<ROI> getRois() {
		return rois;
	}

	/**
	 * Establece las ROI asociadas a la capa raster.
	 *
	 * @param rois ArrayList de ROIs a asociar a la capa raster.
	 */
	public void setRois(ArrayList<ROI> rois) {
		this.rois = rois;
	}

	/**
	 * Si ya tiene una estrategia de dibujado de raster calculada la devuelve, sino
	 * devolverá null.
	 * @return TreeMap con la lista de capas a dibujar
	 */
	public HashMap getRasterStrategy() {
		if(strategy != null)
			return strategy.getStrategy();
		return null;
	}

	/**
	 * Devuelve el tipo de valor de NoData asociado a la capa.
	 * Sirve para diferenciar los estados seleccionados por el usuario. Siendo
	 * estos '0: Sin Valor NoData', '1: NoData de Capa'(Por defecto) y '2: Personalizado'
	 */
	/**
	 * @return the noDataType
	 */
	public int getNoDataType() {
		return noDataType;
	}

	/**
	 * @param noDataType the noDataType to set
	 */
	public void setNoDataType(int noDataType) {
		this.noDataType = noDataType;
		if (dataset != null)
			dataset.setNoDataEnabled(noDataType != RasterLibrary.NODATATYPE_DISABLED);
	}

	/**
	 * @return the configuration
	 */
	static public IConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * @param configuration the configuration to set
	 */
	static public void setConfiguration(IConfiguration configuration) {
		FLyrRasterSE.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#reload()
	 */
	public void reload() throws ReloadLayerException {
		try {
			super.reload();
			if (getMapContext() == null)
				return;
			if (isStopped())
				disableStopped();
			load();
			getMapContext().invalidate();
		} catch (LoadLayerException e) {
			setAvailable(false);
			throw new ReloadLayerException(getName(), e);
		}
	}

	/**
	 * Devuelve si la capa tiene soporte para poder generar overviews
	 * @return
	 */
	public boolean overviewsSupport() {
		if ((getDataSource() != null) && (getDataSource().overviewsSupport()))
			return true;

		return false;
	}

	/**
	 * Devuelve si la asignacion de las bandas a renderizar representa una capa
	 * en escala de grises
	 * @return
	 */
	public boolean isRenderingAsGray() {
		int[] renderBands = getRenderBands();
		if ((renderBands != null) && (renderBands.length == 3) && (renderBands[0] >= 0) &&
				(renderBands[0] == renderBands[1]) && (renderBands[1] == renderBands[2]))
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.render.VisualPropertyListener#actionValueChanged(org.gvsig.raster.grid.render.VisualPropertyEvent)
	 */
	public void visualPropertyValueChanged(VisualPropertyEvent e) {
		updateDrawVersion();
	}

	/*****************************************************/
	//Utils

	/**
	 * Ajusta las coordenadas especificadas en el parámetro al área máxima
	 * del raster en píxeles.
	 * @param req Punto a ajustar dentro del extener del raster
	 */
	public Point2D adjustWorldRequest(Point2D req) {
		Envelope ext = null;

		ext = getFullEnvelope();
		req.setLocation(Math.max(ext.getMinimum(0), req.getX()), Math.max(ext.getMinimum(1), req.getY()));
		req.setLocation(Math.min(ext.getMaximum(0), req.getX()), Math.min(ext.getMaximum(1), req.getY()));
		return req;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#cloneLayer()
	 */
	public FLayer cloneLayer() throws Exception {
		FLyrRasterSE newLayer = FLyrRasterSE.createLayer(this.getName(), params, this.getProjection());
		for (int i = 0; i < dataset.getDatasetCount(); i++) {
			String name = dataset.getDataset(i)[0].getFName();
			if (!(dataset instanceof CompositeDataset) && !name.equals(this.getName()))
				newLayer.addFile(name);
		}
		ArrayList filters = getRender().getFilterList().getStatusCloned();

		//Hacemos una copia de las bandas a renderizar
		if(getRenderBands() != null) {
			int[] rb = new int[getRenderBands().length];
			for (int i = 0; i < rb.length; i++)
				rb[i] = getRenderBands()[i];
			newLayer.setRenderBands(rb);
		}

		//Asignamos el entorno
		newLayer.getRender().getFilterList().setEnv(getRender().getFilterList().getEnv());
		newLayer.getRender().getFilterList().setStatus(filters);

		// Asignamos los valores noData del original
		newLayer.setNoDataValue(getNoDataValue());
		newLayer.setNoDataType(getNoDataType());
		newLayer.applyNoData();

		return newLayer;
	}

	/*****************************************************/

	public void disableStopped() {state.disableStopped();}

	public void enableAwake() throws NotAvailableStateException {state.enableAwake();}

	public void enableClosed() throws NotAvailableStateException {state.enableClosed();}

	public void enableOpen() throws NotAvailableStateException {state.enableOpen();}

	public void enableStopped() {state.enableStopped();}

	public boolean isAwake() {return state.isAwake();}

	public boolean isClosed() {return state.isClosed();}

	public boolean isOpen() {return state.isOpen();}

	public boolean isStopped() {return state.isStopped();}


	public Set getMetadataChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getMetadataID() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMetadataName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontext.layers.operations.SingleLayer#getDataStore()
	 */
	public DataStore getDataStore() {
		return this.store;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.mapcontext.layers.operations.SingleLayer#setDataStore(org.gvsig.fmap.dal.DataStore)
	 */
	public void setDataStore(DataStore dataStore) throws LoadLayerException {
		this.store = (CoverageStore) dataStore;
		// TODO temporal
		RasterStoreParameters params = (RasterStoreParameters)store.getParameters();
		this.setLoadParams(params.getFile());
		if (params.getSRS() != null)
			this.setProjection(params.getSRS());
		this.load();
	}

}