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
package com.iver.cit.gvsig.fmap.layers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ImageIcon;

import org.cresques.cts.ICoordTrans;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.ConnectionErrorLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.UnsupportedVersionLayerException;
import org.gvsig.fmap.mapcontext.exceptions.XMLLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.Tiling;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.fmap.raster.layers.IStatusRaster;
import org.gvsig.fmap.raster.layers.StatusLayerRaster;
import org.gvsig.raster.dataset.CompositeDataset;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MosaicNotValidException;
import org.gvsig.raster.dataset.MultiRasterDataset;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorTable;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.RasterFilterListManager;
import org.gvsig.remoteClient.wcs.WCSStatus;
import org.gvsig.remoteClient.wms.ICancellable;
import org.gvsig.tools.task.Cancellable;

import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriver;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriverFactory;
import com.iver.cit.gvsig.fmap.drivers.wcs.WCSDriverException;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


/**
 * Class for the WCS layer.
 *
 * Capa para el WCS.
 *
 * Las capas WCS son tileadas para descargarlas del servidor. Esto quiere decir que
 * están formadas por multiples ficheros raster. Por esto la fuente de datos raster (IRasterDatasource)
 * de la capa FLyrWCS es un objeto de tipo CompositeDataset. Este objeto está compuesto por un array
 * bidimensional de MultiRasterDataset. Cada uno de los MultiRasterDataset corresponde con un tile
 * salvado en disco. Estos MultiRasterDataset se crean cada vez que se repinta ya que en WCS a cada
 * zoom varian los ficheros fuente. La secuencia de creación de un CompositeDataset sería la siguiente:
 * <UL>
 * <LI>Se hace una petición de dibujado por parte del usuario llamando al método draw de FLyrWCS</LI>
 * <LI>Se tilea la petición</LI>
 * <LI>Cada tile se dibuja abriendo una FLyrRaster para ese tile</LI>
 * <LI>Si es el primer dibujado se guarda una referencia en la capa WMS a las propiedades de renderizado, orden de bandas,
 * transparencia, filtros aplicados, ...</LI>
 * <LI>Si no es el primer dibujado se asignan las propiedades de renderizado cuya referencia se guarda en la capa WMS</LI>
 * <LI>Se guarda el MultiRasterDataset de cada tile</LI>
 * <LI>Al acabar todos los tiles creamos un CompositeDataset con los MultiRasterDataset de todos los tiles</LI>
 * <LI>Asignamos a la capa la referencia de las propiedades de renderizado que tenemos almacenadas. De esta forma si hay
 * alguna modificación desde el cuadro de propiedades será efectiva sobre los tiles que se dibujan.</LI>
 * </UL>
 *
 * @author jaume - jaume.dominguez@iver.es
 */
public class FLyrWCS extends FLyrRasterSE {
	private FMapWCSDriver wcs = null;

	private URL 						host;
	private String						coverageName;
	private Envelope					fullEnvelope;
	private String						format;
	private String						srs;
	private String						time;
	private String						parameter;
	private Point2D						maxRes;
	private Hashtable 					onlineResources = new Hashtable();

	private WCSStatus					wcsStatus = new WCSStatus();

	private int 						posX = 0, posY = 0;
	private double 						posXWC = 0, posYWC = 0;
	private int 						r = 0, g = 0, b = 0;
	private boolean 					firstLoad = false;
	private VisualStatus				visualStatus = new VisualStatus();

	private boolean 					mustTileDraw = false;
	private int 						maxTileDrawWidth  = 1023;
	private int							maxTileDrawHeight = 1023;
	//private int 						maxTilePrintWidth  = 250;
	//private int							maxTilePrintHeight = 250;
	/**
	 * Lista de filtros aplicada en la renderización
	 */
	private RasterFilterList            filterList = null;
	private GridTransparency			transparency = null;
	private int[]                       renderBands = null;
	private FLyrRasterSE				layerRaster = null;
	private ArrayList                   filterArguments = null;

	private class MyCancellable implements ICancellable
	{

		private Cancellable original;
		public MyCancellable(Cancellable cancelOriginal)
		{
			this.original = cancelOriginal;
		}
		public boolean isCanceled() {
			return original.isCanceled();
		}
		public Object getID() {
			return this;
		}

	}

	public FLyrWCS(){
		super();
	}

	public FLyrWCS(Map args) throws ReadException {
		FMapWCSDriver drv = null;
		String host = (String)args.get("HOST");
		String sCoverage = (String) args.get("COVERAGE");

		try {
			this.setHost(new URL(host));
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			throw new ReadException("Malformed host URL, '" + host + "' ("
					+ e.toString() + ").", e);
		}
		try {
			drv = this.getDriver();
		} catch (Exception e) {
			// e.printStackTrace();
			throw new ReadException("Can't get driver to host '" + host + "' ("
					+ e.toString() + ").", e);
		}

		try{
			if (!drv.connect(false, null)){
				throw new ReadException(
						"Can't connect to host '" + host + "'.", null);
			}
		}catch(Exception e){
			throw new ReadException("Can't connect to host '" + host + "'.", e);
		}

		WCSLayer wcsNode = drv.getLayer(sCoverage);

		if (wcsNode == null){
			throw new ReadException("The server '" + host
					+ "' doesn't has the coverage '" + sCoverage + "'.", null);
		}

		try{
			this.setFullExtent(drv.getFullExtent(sCoverage,
					(String) args.get("CRS")));
			this.setFormat((String) args.get("FORMAT"));
			this.setParameter("BANDS=" + (String) args.get("BANDS"));
			this.setSRS((String) args.get("CRS"));
			this.setName(sCoverage);
			this.setCoverageName(sCoverage);
		}catch (Exception e){
			throw new ReadException("The server '" + host
					+ "' is not able to load the coverage '"
							+ sCoverage + "'.", e);
		}

	}

	/**
	 * Clase que contiene los datos de visualización de WCS. Tiene datos que representan al
	 * raster en la vista. Este raster puede estar compuesto por tiles por lo que valores
	 * como el ancho total o el mínimo o máximo deben ser calculados a partir de todos los
	 * tiles visualizados.
	 * @author Nacho Brodin (brodin_ign@gva.es)
	 */
	private class VisualStatus {
		/**
		 * Ancho y alto de la imagen o del conjunto de tiles si los tiene. Coincide con
		 * el ancho y alto del viewPort
		 */
		private	int							width = 0, height = 0;
		private double						minX = 0D, minY = 0D, maxX = 0D, maxY = 0D;
		private int 						bandCount = 0;
		private int							dataType = DataBuffer.TYPE_UNDEFINED;

		/**
		 * Ancho y alto total del raster que será la suma de todos los tiles.
		 */
		private	int							rasterWidth = 0, rasterHeight = 0;
		private	double						rasterMinX = Double.MAX_VALUE, rasterMinY = Double.MAX_VALUE;
		private	double						rasterMaxX = 0, rasterMaxY = 0;
		/**
		 * Lista de nombre de fichero que componen toda la visualización.
		 */
		private String[]					fileNames = null;
	}

	/**
	 * @deprecated
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.InfoByPoint#getInfo
	 */
	public String queryByPoint(Point p) {
		String data = "<file:"+getName().replaceAll("[^a-zA-Z0-9]","")+">\n";
		ArrayList attr = this.getAttributes();
		data += "  <raster\n";
		data += "    File=\""+getName()+"\"\n";
		for (int i=0; i<attr.size(); i++) {
			Object [] a = (Object []) attr.get(i);

			data += "    "+a[0].toString()+"=";
			if (a[1].toString() instanceof String) {
				data += "\""+a[1].toString()+"\"\n";
			} else {
				data += a[1].toString()+"\n";
			}
		}
		data += "    Point=\""+posX+" , "+posY+"\"\n";
		data += "    Point_WC=\""+posXWC+" , "+posYWC+"\"\n";
		data += "    RGB=\""+r+", "+g+", "+b+"\"\n";
		data += "  />\n";

		data += "</file:"+getName().replaceAll("[^a-zA-Z0-9]","")+">\n";
		System.out.println(data);
		return data;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.InfoByPoint#getInfo
	 */
	public XMLItem[] getInfo(Point point, double tolerance, Cancellable cancel)
			throws ReadException {
		return super.getInfo(point, tolerance, cancel);
	}

	/**
	 * Devuelve el envelope en la proyeccion de la vista
	 *
	 */
	public Envelope getFullEnvelope() {
		Envelope rAux;
		rAux = this.fullEnvelope;
		GeometryManager geoMan = GeometryLocator.getGeometryManager();

		try {
			rAux = geoMan.createEnvelope(SUBTYPES.GEOM2D);

			// Esto es para cuando se crea una capa nueva con el fullExtent de
			// ancho
			// y alto 0.
			if (rAux == null || rAux.getMaximum(0) - rAux.getMinimum(0) == 0
					&& rAux.getMaximum(1) - rAux.getMinimum(1) == 0) {
				rAux = geoMan.createEnvelope(SUBTYPES.GEOM2D);
				org.gvsig.fmap.geom.primitive.Point pl = (org.gvsig.fmap.geom.primitive.Point) geoMan
						.create(TYPES.POINT, SUBTYPES.GEOM2D);
				org.gvsig.fmap.geom.primitive.Point pu = (org.gvsig.fmap.geom.primitive.Point) geoMan
						.create(TYPES.POINT, SUBTYPES.GEOM2D);
				pl.setX(0);
				pl.setY(0);
				pu.setX(100);
				pu.setY(100);
				rAux.setLowerCorner(pl);
				rAux.setUpperCorner(pu);
			}
			// Si existe reproyección, reproyectar el extent
			ICoordTrans ct = getCoordTrans();
			if (ct != null) {
				Point2D pt1 = new Point2D.Double(rAux.getMinimum(0), rAux
						.getMinimum(1));
				Point2D pt2 = new Point2D.Double(rAux.getMaximum(0), rAux
						.getMaximum(1));
				pt1 = ct.convert(pt1, null);
				pt2 = ct.convert(pt2, null);
				org.gvsig.fmap.geom.primitive.Point pt1_geom = (org.gvsig.fmap.geom.primitive.Point) geoMan
						.create(TYPES.POINT, SUBTYPES.GEOM2D);
				pt1_geom.setX(pt1.getX());
				pt1_geom.setY(pt1.getY());
				org.gvsig.fmap.geom.primitive.Point pt2_geom = (org.gvsig.fmap.geom.primitive.Point) geoMan
						.create(TYPES.POINT, SUBTYPES.GEOM2D);
				pt2_geom.setX(pt2.getX());
				pt2_geom.setY(pt2.getY());

				rAux.setLowerCorner(pt1_geom);
				rAux.setUpperCorner(pt2_geom);
			}
		} catch (CreateEnvelopeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.setAvailable(false);
			return null;
		} catch (CreateGeometryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.setAvailable(false);
			return null;
		}
		return rAux;

	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#draw(java.awt.image.BufferedImage, java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.cit.gvsig.fmap.operations.Cancellable, double)
	 */
	public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale) throws ReadException {
		enableStopped();
		// callLegendChanged(null);

		if (isWithinScale(scale)) {
			Point2D p = viewPort.getOffset();
			// p will be (0, 0) when drawing a view or other when painting onto
			// the Layout.
			visualStatus.width = viewPort.getImageWidth();
			visualStatus.height = viewPort.getImageHeight();
			Envelope env = viewPort.getAdjustedExtent();
			visualStatus.minX = env.getMinimum(0);
			visualStatus.minY = env.getMinimum(1);
			visualStatus.maxX = env.getMaximum(0);
			visualStatus.maxY = env.getMaximum(1);
			visualStatus.rasterWidth = 0;
			visualStatus.rasterHeight = 0;
			visualStatus.rasterMinX = Double.MAX_VALUE;
			visualStatus.rasterMinY = Double.MAX_VALUE;
			visualStatus.rasterMaxX = 0;
			visualStatus.rasterMaxY = 0;
			visualStatus.fileNames = new String[1];

			try {
				if (true) {
					if (viewPort.getImageWidth() <= maxTileDrawWidth && viewPort.getImageHeight() <= maxTileDrawHeight) {
						drawTile(g, viewPort, cancel, 0, scale);
						if (layerRaster == null) {
							return;
						}
						dataset = layerRaster.getDataSource();
						getRender().setLastRenderBuffer(layerRaster.getRender().getLastRenderBuffer());
						initializeRasterLayer(null, new IBuffer[][] { { layerRaster.getRender().getLastRenderBuffer() } });
					} else {
						Rectangle r = new Rectangle((int) p.getX(), (int) p.getY(), viewPort.getImageWidth(), viewPort.getImageHeight());
						Tiling tiles = new Tiling(maxTileDrawWidth, maxTileDrawHeight, r);
						tiles.setAffineTransform((AffineTransform) viewPort.getAffineTransform().clone());
						MultiRasterDataset[][] datasets = new MultiRasterDataset[tiles.getNumRows()][tiles.getNumCols()];
						IBuffer[][] buf = new IBuffer[tiles.getNumRows()][tiles.getNumCols()];
						visualStatus.fileNames = new String[tiles.getNumTiles()];
						for (int tileNr = 0; tileNr < tiles.getNumTiles(); tileNr++) {
							// drawing part
							try {
								ViewPort vp = tiles.getTileViewPort(viewPort, tileNr);
								boolean painted = drawTile(g, vp, cancel, tileNr, scale);
								if (layerRaster != null && painted) {
									datasets[(tileNr / tiles.getNumCols())][tileNr % tiles.getNumCols()] = (MultiRasterDataset) layerRaster.getDataSource().newDataset();
									buf[(tileNr / tiles.getNumCols())][tileNr % tiles.getNumCols()] = layerRaster.getRender().getLastRenderBuffer();
								}
							} catch (NoninvertibleTransformException e) {
								e.printStackTrace();
							}
						}
						try {
							if (datasets != null && datasets[0][0] != null) {
								dataset = new CompositeDataset(datasets);
								initializeRasterLayer(datasets, buf);
							}
						} catch (MosaicNotValidException e) {
							throw new ReadException(
									"No hay continuidad en el mosaico.", e);
						} catch (LoadLayerException e) {
							throw new ReadException(
									"Error inicializando la capa.", e);
						}
					}
				} else {
					drawTile(g, viewPort, cancel, 0, scale);
					if (layerRaster == null) {
						return;
					}
					dataset = layerRaster.getDataSource();
					getRender().setLastRenderBuffer(layerRaster.getRender().getLastRenderBuffer());
					initializeRasterLayer(null, new IBuffer[][] { { layerRaster.getRender().getLastRenderBuffer() } });
				}
			} catch (ConnectionErrorLayerException e) {
				e.printStackTrace();
			} catch (UnsupportedVersionLayerException e) {
				e.printStackTrace();
			} catch (LoadLayerException e) {
				e.printStackTrace();
			}
		}
		disableStopped();
		// callLegendChanged(null);
		Runtime r = Runtime.getRuntime();
		long mem = r.totalMemory() - r.freeMemory();
		System.err.println("Memoria total: " + (mem / 1024) +"KB");
	}

	/**
	 * Acciones que se realizan después de asignar la fuente de datos a
	 * la capa raster.
	 *
	 * @throws LoadLayerException
	 */
	private void initializeRasterLayer(MultiRasterDataset[][] datasets, IBuffer[][] buf) throws LoadLayerException {
		if(this.filterList != null) {
			getRender().setFilterList(filterList);
		}

/*
		if(this.transparency != null)
			getRender().setLastTransparency(transparency);
*/
		if(this.renderBands != null) {
			getRender().setRenderBands(renderBands);
		}
		if(datasets != null) {
			String[][] names = new String[datasets.length][datasets[0].length];
			for (int i = 0; i < datasets.length; i++) {
				for (int j = 0; j < datasets[i].length; j++) {
					if(datasets[i][j] != null) {
						names[i][j] = datasets[i][j].getDataset(0)[0].getFName();
					}
				}
			}
			super.setLoadParams(names);
		}
		super.init();
		if(buf != null) {
			int drawablesBandCount = layerRaster.getDataSource().getBands().getDrawableBandsCount();
			IBuffer buff = null;
			if(dataset instanceof CompositeDataset) {
				buff = ((CompositeDataset)dataset).generateBuffer(buf, drawablesBandCount);
			} else {
				buff = buf[0][0];
			}
			getRender().setLastRenderBuffer(buff);
		}

		if (transparency == null) {
			transparency = new GridTransparency(getDataSource().getTransparencyFilesStatus());
		}

		getRender().setLastTransparency(transparency);
	}

	/**
	 * This is the method used to draw a tile in a WCS mosaic layer.
	 *
	 * @param tile
	 *            Tile number to draw
	 * @throws ReadException
	 * @return true when a tile has been painted
	 */
	private boolean drawTile(Graphics2D g, ViewPort vp, Cancellable cancel,
			int tile, double scale) throws LoadLayerException, ReadException {

		// Compute the query geometry
		// 1. Check if it is within borders
		Envelope envelope = getFullEnvelope();
		Envelope vpEnvelope = vp.getAdjustedExtent();
		if (!vpEnvelope.intersects(envelope)) {
			return false;
		}

		// 2. Compute extent to be requested.
		Rectangle2D bBox = new Rectangle2D.Double();
		Rectangle2D.intersect(getRectable2DFromEnvelope(vpEnvelope),
				getRectable2DFromEnvelope(envelope), bBox);

		// 3. Compute size in pixels
		double scalex = vp.getAffineTransform().getScaleX();
		double scaley = vp.getAffineTransform().getScaleY();
		int wImg = (int) Math.ceil(Math.abs(bBox.getWidth() * scalex) + 1);
		int hImg = (int) Math.ceil(Math.abs(bBox.getHeight() * scaley) + 1);
		Dimension sz = new Dimension(wImg, hImg);

		if ((wImg <= 0) || (hImg <= 0)) {
			return false;
		}

		try {
			sz = new Dimension(wImg, hImg);

			wcsStatus.setCoveraName( coverageName );
			wcsStatus.setExtent( bBox );
			wcsStatus.setFormat( format );
			wcsStatus.setHeight( hImg );
			wcsStatus.setWidth( wImg );
			wcsStatus.setSrs(srs);
			wcsStatus.setParameters( parameter );
			wcsStatus.setTime( time );
			wcsStatus.setOnlineResource((String) onlineResources.get("GetCoverage"));

			File f = getDriver().getCoverage(wcsStatus, new MyCancellable(cancel));
			if (f == null) {
				return false;
			}
			String nameWordFile = f.getPath() + getExtensionWorldFile();
			com.iver.andami.Utilities.createTemp(nameWordFile, this.getDataWorldFile(bBox, sz));

			IStatusRaster status = super.getStatus();
			if(status!=null && firstLoad){
				try {
					status.applyStatus(this);
				} catch (NotSupportedExtensionException e) {
					throw new ReadException("", e);
				} catch (RasterDriverException e) {
					throw new ReadException("", e);
				} catch (FilterTypeException e) {
					throw new ReadException("", e);
				}
				firstLoad = false;
			}
			ViewPortData vpData = new ViewPortData(
				vp.getProjection(), new Extent(bBox), sz );
			vpData.setMat(vp.getAffineTransform());

			String filePath = f.getAbsolutePath();
			visualStatus.fileNames[tile] = filePath;

			try {
				rasterProcess(filePath, g, vp, scale, cancel);
			} catch (FilterTypeException e) {
			}

//			this.getRender().draw(g, vpData);
//			rasterProcess(g, vpData, f);

//		} catch (ValidationException e) {
//			UnknownResponseFormatExceptionType type =
//				new UnknownResponseFormatExceptionType();
//			type.setLayerName(getName());
//			try {
//				type.setDriverName(getDriver().getName());
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//			type.setFormat(format);
//			type.setHost(host);
//			type.setProtocol("WCS");
			// ReadException exception = new
			// ReadException("unknown_response_format",type);
//			throw exception;
//	azabala		throw new DriverException(PluginServices.getText(this, "unknown_response_format"), e);
//		}
//		catch (UnsupportedVersionLayerException e) {
//			UnsuportedProtocolVersionExceptionType type =
//				new UnsuportedProtocolVersionExceptionType();
//			type.setLayerName(getName());
//			try {
//				type.setDriverName(getDriver().getName());
//			} catch (Exception ex){
//			}
//			type.setUrl(host);
			// throw new ReadException(PluginServices.getText(this,
			// "version_conflict"), e, type);

//	azabala		throw new DriverException(PluginServices.getText(this, "version_conflict"), e);
		} catch (IOException e) {
//			ConnectionErrorExceptionType type = new ConnectionErrorExceptionType();
//			type.setLayerName(getName());
//			try {
//				type.setDriverName(getDriver().getName());
//			} catch (Exception e1) {
//			}
//			type.setHost(host);
			throw new ConnectionErrorLayerException(getName(),e);
		}
//		catch (WCSLayerException e) {
////azabala: la capturamos y la convertimos en DriverException
//			WCSDriverExceptionType type = new WCSDriverExceptionType();
//			type.setLayerName(getName());
//			try {
//				type.setDriverName(getDriver().getName());
//			} catch (Exception e1) {
//			}
//			type.setWcsStatus(wcsStatus);
//			this.setVisible(false);
//			throw new WDriverException("Error WCS", e,  type);
//
////            JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), e.getMessage());
//
//        }//
		catch (WCSDriverException e) {
			throw new LoadLayerException(getName(),e);
		} catch (IllegalStateException e) {
			throw new LoadLayerException(getName(),e);
		}
		return true;
	}

	/**
	 * Devuelve el FMapWMSDriver.
	 *
	 * @return FMapWMSDriver
	 *
	 * @throws IllegalStateException
	 * @throws ValidationException
	 * @throws UnsupportedVersionLayerException
	 * @throws IOException
	 */
	private FMapWCSDriver getDriver() throws IllegalStateException, IOException {
		if (wcs == null) {
			wcs = FMapWCSDriverFactory.getFMapDriverForURL(host);
		}
		return wcs;
	}

	/**
	 * Calcula el contenido del fichero de georreferenciación de una imagen.
	 * @param bBox Tamaño y posición de la imagen (en coordenadas de usuario)
	 * @param sz Tamaño de la imagen en pixeles.
	 * @return el 'WorldFile', como String.
	 * @throws IOException
	 */
	public String getDataWorldFile(Rectangle2D bBox, Dimension sz) throws IOException {
		StringBuffer data = new StringBuffer();
		data.append((bBox.getMaxX() - bBox.getMinX())/(sz.getWidth() - 1)+"\n");
		data.append("0.0\n");
		data.append("0.0\n");
		data.append("-"+(bBox.getMaxY() - bBox.getMinY())/(sz.getHeight() - 1)+"\n");
		data.append(""+bBox.getMinX()+"\n");
		data.append(""+bBox.getMaxY()+"\n");
		return data.toString();
	}

	/**
	 * Carga y dibuja el raster usando la librería
	 *
	 * @param filePath
	 *            Ruta al fichero en disco
	 * @param g
	 *            Graphics2D
	 * @param vp
	 *            ViewPort
	 * @param scale
	 *            Escala para el draw
	 * @param cancel
	 *            Cancelación para el draw
	 * @throws ReadException
	 * @throws LoadLayerException
	 */
	private void rasterProcess(String filePath, Graphics2D g, ViewPort vp,
			double scale, Cancellable cancel) throws ReadException,
			LoadLayerException, FilterTypeException {
		//Cerramos el dataset asociado a la capa si está abierto.
		if(layerRaster != null) {
			layerRaster.setRemoveRasterFlag(true);
			layerRaster.getDataSource().close();
		}

		//Cargamos el dataset con el raster de disco.
		layerRaster = FLyrRasterSE.createLayer("", filePath, vp.getProjection());
		layerRaster.getRender().setBufferFactory(layerRaster.getBufferFactory());

		if(visualStatus.dataType == IBuffer.TYPE_UNDEFINED && layerRaster.getDataType() != null) {
			visualStatus.dataType = layerRaster.getDataType()[0];
		}
		if(visualStatus.bandCount == 0 && layerRaster.getBandCount() != 0) {
			visualStatus.bandCount = layerRaster.getBandCount();
		}

		if (getLegend() == null) {
			lastLegend = layerRaster.getLegend();
		}

		//En caso de cargar un proyecto con XMLEntity se crean los filtros
		if(filterArguments != null) {
			RasterFilterList fl = new RasterFilterList();
			fl.addEnvParam("IStatistics", layerRaster.getDataSource().getStatistics());
			fl.addEnvParam("MultiRasterDataset", layerRaster.getDataSource());
			fl.setInitDataType(layerRaster.getDataType()[0]);
			RasterFilterListManager filterListManager = new RasterFilterListManager(fl);
			filterListManager.createFilterListFromStrings(filterArguments);
			StatusLayerRaster.enhancedCompV10(filterArguments, layerRaster, filterListManager);
			filterArguments = null;
			filterList = fl;
		}

		//Como el raster se carga a cada zoom el render se crea nuevamente y la lista de
		//filtros siempre estará vacia a cada visualización. Para evitarlo tenemos que
		//guardar la lista de filtro aplicada en la visualización anterior.
		if (filterList != null) {
			layerRaster.getRender().setFilterList(filterList);
		}
		if (transparency == null) {
			transparency = layerRaster.getRender().getLastTransparency();
		}
		if (transparency != null) {
			layerRaster.getRender().setLastTransparency(transparency);
		}
		if (renderBands != null) {
			layerRaster.getRender().setRenderBands(renderBands);
		}

		//Dibujamos
		layerRaster.draw(null, g, vp, cancel, scale);

		//La primera vez asignamos la lista de filtros asociada al renderizador. Guardamos una referencia
		//en esta clase para que a cada zoom no se pierda.
		if (filterList == null) {
			filterList = layerRaster.getRender().getFilterList();
		}
		if (renderBands == null) {
			renderBands = layerRaster.getRender().getRenderBands();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#cloneLayer()
	 */
	public FLayer cloneLayer() throws Exception {
		Object par = null;
		if (dataset instanceof CompositeDataset) {
			par = ((CompositeDataset) dataset).getFileNames();
		} else
			if (layerRaster != null) {
				par = layerRaster.getLoadParams();
			}

		FLyrRasterSE newLayer = FLyrRasterSE.createLayer(this.getName(), par, this.getProjection());

		ArrayList filters = getRender().getFilterList().getStatusCloned();
		newLayer.getRender().getFilterList().setStatus(filters);

		return newLayer;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterRendering#getRenderFilterList()
	 */
	public RasterFilterList getRenderFilterList(){
		return (filterList != null) ? filterList : getRender().getFilterList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#setRenderFilterList(org.gvsig.raster.grid.filter.RasterFilterList)
	 */
	public void setRenderFilterList(RasterFilterList filterList) {
		this.filterList = filterList;
		super.getRender().setFilterList(filterList);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.IRasterRendering#getRenderTransparency()
	 */
	public GridTransparency getRenderTransparency() {
		return getRender().getLastTransparency();
//		return (transparency != null) ? transparency : getRender().getLastTransparency();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#getRenderBands()
	 */
	public int[] getRenderBands() {
		return (renderBands != null) ? renderBands : getRender().getRenderBands();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.hierarchy.IRasterRendering#setRenderBands(int[])
	 */
	public void setRenderBands(int[] renderBands) {
		this.renderBands = renderBands;
		getRender().setRenderBands(renderBands);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.FLyrRasterSE#print(java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.utiles.swing.threads.Cancellable, double, javax.print.attribute.PrintRequestAttributeSet)
	 */
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale, PrintRequestAttributeSet properties)
			throws ReadException {
		if (isVisible() && isWithinScale(scale)){
			draw(null, g, viewPort, cancel, scale);
		}
	}

	/**
	 * Returns the XMLEntity containing the necessary info for reproduce
	 * the layer.
	 *
	 * Devuelve el XMLEntity con la información necesaria para reproducir
	 * la capa.
	 *
	 * @return XMLEntity.
	 * @throws XMLException
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = super.getXMLEntity();

		xml.putProperty("wcs.host", getHost());
		xml.putProperty("wcs.fullExtent", StringUtilities
				.rect2String(getRectable2DFromEnvelope(this.fullEnvelope)));
		xml.putProperty("wcs.layerQuery", coverageName );
		xml.putProperty("wcs.format", format );
		xml.putProperty("wcs.srs", srs );
		xml.putProperty("wcs.time", time );
		xml.putProperty("wcs.parameter", parameter );
		xml.putProperty("wcs.coverageName", coverageName );
		xml.putProperty("wcs.maxResX", maxRes.getX());
		xml.putProperty("wcs.maxResY", maxRes.getY());

		Iterator it = onlineResources.keySet().iterator();
		String strOnlines = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) onlineResources.get(key);
			strOnlines += key+"~##SEP2##~"+value;
			if (it.hasNext()) {
				strOnlines += "~##SEP1##~";
			}
		}
		xml.putProperty("onlineResources", strOnlines);

		IStatusRaster status = super.getStatus();
		if (status!=null) {
			status.getXMLEntity(xml, true, this);
		} else{
			status = new StatusLayerRaster();
			status.getXMLEntity(xml, true, this);
		}
		return xml;
	}

	/**
	 * Reproduces the layer from an XMLEntity.
	 *
	 * A partir del XMLEntity reproduce la capa.
	 *
	 * @param xml
	 *            XMLEntity
	 *
	 * @throws XMLException
	 * @throws DriverException
	 * @throws ReadException
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException {
		super.setXMLEntity(xml);

		// host
		try {
			host = new URL(xml.getStringProperty("wcs.host"));
		} catch (MalformedURLException e) {
			throw new XMLLayerException("", e);
		}

		// full extent
		fullEnvelope = getEnvelopeFromRectable2D(StringUtilities
				.string2Rect(xml
				.getStringProperty("wcs.fullExtent")));

		// coverageQuery
		coverageName = xml.getStringProperty("wcs.layerQuery");

		// format
		format = xml.getStringProperty("wcs.format");

		// srs
		srs = xml.getStringProperty("wcs.srs");

		// time
		time = xml.getStringProperty("wcs.time");

		// parameter
		parameter = xml.getStringProperty("wcs.parameter");

		// coverage name
		coverageName = xml.getStringProperty("wcs.coverageName");

		// max resolution
		if (xml.contains("wcs.maxRes")) {
			maxRes = new Point2D.Double(xml.getDoubleProperty("wcs.maxRes"), xml.getDoubleProperty("wcs.maxRes"));
		} else if (xml.contains("wcs.maxResX") && xml.contains("wcs.maxResY")) {
			maxRes = new Point2D.Double(xml.getDoubleProperty("wcs.maxResX"), xml.getDoubleProperty("wcs.maxResY"));
		}

		// OnlineResources
				if (xml.contains("onlineResources")) {
					String[] operations = xml.getStringProperty("onlineResources").split("~##SEP1##~");
					for (int i = 0; i < operations.length; i++) {
				String[] resources = operations[i].split("~##SEP2##~");
				if (resources.length==2 && resources[1]!="") {
					onlineResources.put(resources[0], resources[1]);
				}
			}
				}
		String claseStr = null;
		if (xml.contains("raster.class")) {
			claseStr = xml.getStringProperty("raster.class");
		}

		IStatusRaster status = super.getStatus();
		if (status!=null) {
			status.setXMLEntity(xml, this);
		} else {
			//Cuando cargamos un proyecto

			if(claseStr!=null && !claseStr.equals("")){
				try{
					// Class clase =
					// LayerFactory.getLayerClassForLayerClassName(claseStr);
					Class clase = FLyrRasterSE.class;
					Constructor constr = clase.getConstructor(null);
					status = (IStatusRaster)constr.newInstance(null);
					if(status != null) {
						((StatusLayerRaster)status).setNameClass(claseStr);
						status.setXMLEntity(xml, this);
						filterArguments = status.getFilterArguments();
//						transparency = status.getTransparency();
						renderBands = status.getRenderBands();
						ColorTable ct = status.getColorTable();
						if(ct != null) {
							setLastLegend(ct);
						}
					}
					// } catch(ClassNotFoundException exc) {
					// exc.printStackTrace();
				} catch(InstantiationException exc) {
					exc.printStackTrace();
				} catch(IllegalAccessException exc) {
					exc.printStackTrace();
				} catch(NoSuchMethodException exc) {
					exc.printStackTrace();
				} catch(InvocationTargetException exc) {
					exc.printStackTrace();
				} catch (FilterTypeException exc) {
					exc.printStackTrace();
				}
			}
		}
		firstLoad = true;
	}

	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}

	public void setParameter(String parametersString) {
		if (this.parameter == parametersString){
			return;
		}
		if (this.parameter != null && this.parameter.equals(parametersString)){
			return;
		}
		this.parameter = parametersString;
		this.updateDrawVersion();
	}

	public void setTime(String time) {
		if (this.time == time){
			return;
		}
		if (this.time != null && this.time.equals(time)){
			return;
		}
		this.time = time;
		this.updateDrawVersion();
	}

	public void setSRS(String srs) {
		if (this.srs == srs){
			return;
		}
		if (this.srs != null && this.srs.equals(srs)){
			return;
		}
		this.srs = srs;
		this.updateDrawVersion();
		setProjection(CRSFactory.getCRS(srs));
	}

	public void setFormat(String format) {
		if (this.format == format){
			return;
		}
		if (this.format != null && this.format.equals(format)){
			return;
		}
		this.format = format;
		this.updateDrawVersion();
	}


	/**
	 * Inserta el URL.
	 *
	 * @param host String.
	 * @throws MalformedURLException
	 */
	public void setHost(String host) {
		try {
			setHost(new URL(host));
		} catch (MalformedURLException e) {

		}
	}

	/**
	 * Inserta el URL.
	 *
	 * @param host URL.
	 */
	public void setHost(URL host) {
		if (this.host == host){
			return;
		}
		if (this.host != null && this.host.equals(host)){
			return;
		}
		this.host = host;
		this.updateDrawVersion();
	}


	/**
	 * Devuelve el URL.
	 *
	 * @return URL.
	 */
	public URL getHost() {
		return host;
	}

	/**
	 * Remote source layers have a bunch of properties that are required for get them from
	 * the servers. This method supplies a hash table containing any needed field. This hash
	 * table may be used to let the client to connect to a server and restore a previously saved
	 * layer. So, the layer itself may not be saved to the disk since the actual saved
	 * info is just its properties.
	 *
	 * @return Returns a hash table containing all the required information for
	 * set up a wms layer
	 */
	public Hashtable getProperties(){
		Hashtable info = new Hashtable();
		info.put(   "name", coverageName);
		info.put(   "host", getHost());
		info.put(    "crs", srs);
		info.put( "format", format);
		String str = time;
		if (str==null) {
			str = "";
		}
		info.put(   "time", str);
		str = parameter;
		if (str==null) {
			str = "";
		}
		info.put("parameter", str);

		return info;
	}

	/**
	 * Obtiene la extensión del fichero de georreferenciación
	 * @return String con la extensiï¿½n del fichero de georreferenciaciï¿½n dependiendo
	 * del valor del formato obtenido del servidor. Por defecto asignaremos un .wld
	 */
	private String getExtensionWorldFile(){
		String extWorldFile = ".wld";
			if (format.equals("image/tif") || format.equals("image/tiff")) {
				extWorldFile = ".tfw";
			}
			if (format.equals("image/jpeg")) {
				extWorldFile = ".jpgw";
			}
			return extWorldFile;
	}

	public void setMaxResolution(Point2D maxResolution) {
		if (this.maxRes == maxResolution){
			return;
		}
		if (this.maxRes != null && this.maxRes.equals(maxResolution)){
			return;
		}
		this.maxRes = maxResolution;
		this.updateDrawVersion();
	}

	/**
	 * <p>
	 * Gets the max resolution allowed by the coverage. Requesting a higher resolution
	 * than this value does not cause any error, but the info responsed is just an
	 * interpolation. <br>
	 * </p>
	 *
	 * <p>
	 * In exchange for obtaining a greater file and without additional information,
	 * we can easily fit it into the View. <br>
	 * </p>
	 *
	 * <p>
	 * Obtiene la resolución máxima soportada por la cobertura. La petición
	 * de una resolución superior a la soportada no provoca ningún error, aunque
	 * la información obtenida sólo es una mera interpolación de información. <br>
	 * </p>
	 *
	 * <p>
	 * A cambio de obtener un archivo mayor y sin información adicional, podemos
	 * fácilmente acoplarlo a la vista. <br>
	 * </p>
	 *
	 * @return double
	 */
	public Point2D getMaxResolution() {
		if (maxRes==null) {
			maxRes = wcs.getMaxResolution(coverageName);
		}
		return maxRes;
	}


	public void setDriver(FMapWCSDriver driver) {
		if (driver == this.wcs){
			return;
		}
		this.wcs = driver;
		this.updateDrawVersion();
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
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getTocImageIcon()
	 */
	public ImageIcon getTocImageIcon() {
		return new ImageIcon(getClass().getResource("image/icoLayer.png"));
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.RasterOperations#isTiled()
	 */
	public boolean isTiled() {
		return mustTileDraw;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.FLyrRasterSE#isActionEnabled(int)
	 */
	public boolean isActionEnabled(int action) {
		switch (action) {
			case IRasterLayerActions.ZOOM_PIXEL_RESOLUTION:
			case IRasterLayerActions.FLYRASTER_BAR_TOOLS:
			case IRasterLayerActions.BANDS_FILE_LIST:
			case IRasterLayerActions.GEOLOCATION:
			case IRasterLayerActions.PANSHARPENING:
			case IRasterLayerActions.SAVE_COLORINTERP:
				return false;
			case IRasterLayerActions.BANDS_RGB:
				return true;
		}

		return super.isActionEnabled(action);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.fmap.raster.layers.FLyrRasterSE#overviewsSupport()
	 */
	public boolean overviewsSupport() {
		return false;
	}

	/**
	 * Inserta la extensiï¿½n total de la capa.
	 *
	 * @param fullExtent
	 *            Rectï¿½ngulo.
	 * @deprecated
	 */
	public void setFullExtent(Rectangle2D fullExtent) {
		this.setFullEnvelope(this.getEnvelopeFromRectable2D(fullExtent));
	}

	/**
	 * Inserta la extensiï¿½n total de la capa en la proyeccion original.
	 *
	 * @param fullExtent
	 *            Rectï¿½ngulo.
	 */
	public void setFullEnvelope(Envelope envelope) {
		Envelope cur = this.getFullEnvelope();
		if (cur == envelope) {
			return;
		}
		if (cur != null && cur.equals(envelope)) {
			return;
		}

		this.fullEnvelope = envelope;
		this.updateDrawVersion();
	}

	private Rectangle2D.Double getRectable2DFromEnvelope(Envelope env) {
		return new Rectangle2D.Double(env.getMinimum(0), env.getMinimum(1), env
				.getLength(0), env.getLength(1));
	}

	private Envelope getEnvelopeFromRectable2D(Rectangle2D rect) {

		GeometryManager geoMan = GeometryLocator.getGeometryManager();
		Envelope rAux = null;
		try {
			rAux = geoMan.createEnvelope(rect.getMinX(), rect.getMinY(), rect
					.getMaxX(), rect.getMaxY(), SUBTYPES.GEOM2D);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


		return rAux;
	}

}