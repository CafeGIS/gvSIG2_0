package org.gvsig.gvsig3d.cacheservices;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.cresques.cts.IProjection;
import org.cresques.px.Extent;
import org.gvsig.cacheservice.CacheService;
import org.gvsig.cacheservice.CacheServiceException;
import org.gvsig.cacheservice.RasterCacheService;
import org.gvsig.cacheservice.TileNum;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.buffer.WriterBufferServer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.remoteClient.utils.Utilities;
import org.gvsig.remoteClient.wcs.WCSStatus;
import org.gvsig.remoteClient.wms.ICancellable;
import org.gvsig.remoteClient.wms.WMSStatus;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriver;
import com.iver.cit.gvsig.fmap.drivers.wcs.FMapWCSDriverFactory;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriver;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriverFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.utiles.FileUtils;
import com.iver.utiles.swing.threads.Cancellable;

public class FLayerCacheService extends RasterCacheService {
	private boolean _loadFailed = true;
	private Extent _extent;
	private Rectangle2D _lyrExtentRect;
	private FLayer _layer;
	private IProjection _viewProj;
	private int _dataType;

	// WMS terrain
	// TODO Un toggle this comment to use WMS and WCS extensions.
	FMapWMSDriver _wmsDriver = null;
	FMapWCSDriver _wcsDriver = null;

	WMSStatus _wmsStatus = null;
	WCSStatus _wcsStatus = null;

	public FLayerCacheService(Planet planet, String name, FLayer layer,
			IProjection viewProj) {
		super(planet.getPlanetName(), name);

		_layer = layer;
		_viewProj = viewProj;
		_loadFailed = true;

		int cacheType = CacheService.GLOBAL;
		if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC)
			cacheType += SPHERIC;
		else
			cacheType += PLANE;
		setCacheType(cacheType);

		try {
			_lyrExtentRect = _layer.getFullExtent();
		} catch (ExpansionFileReadException e) {
			e.printStackTrace();
		} catch (ReadDriverException e) {
			e.printStackTrace();
		}

		_extent = new Extent(_lyrExtentRect);

		Layer3DProps props3D = Layer3DProps.getLayer3DProps(layer);
		_dataType = props3D.getType();
		if (_dataType == Layer3DProps.layer3DImage)
			setFileExtension(".png");
		else
			setFileExtension(".tif");

		_loadFailed = false;

	}

	protected class MyCancel implements Cancellable, ICancellable {

		public boolean isCanceled() {
			return false;
		}

		public void setCanceled(boolean canceled) {

		}

		/* if you don´t put and ID the wms donwload will be fail */
		public Object getID() {
			return this;
		}

	}

	private Image getTileFromLayer(String fName, Rectangle2D tileExtent) {
		BufferedImage image;

		if (_dataType == Layer3DProps.layer3DImage) {
			int size = getTileSize();
			image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

			ViewPort vp = new ViewPort(_viewProj);
			vp.setExtent(tileExtent); // the real extent of the tile
			// NotificationManager.addInfo("Extent solicitado--> "
			// + tileExtent.getMinX() + " " + tileExtent.getMinY() + " "
			// + tileExtent.getMaxX() + " " + tileExtent.getMaxY());
			vp.setAdjustable(false);
			vp.setImageSize(new Dimension(size, size));
			// vp.setDist1pixel(1 / 150000);
			vp.setDist1pixel(0.000000001);

			Graphics2D graphics = image.createGraphics();
			Color backgColor = new Color(255, 255, 255, 0); // transparent
			// background
			graphics.setColor(backgColor);
			graphics.fillRect(0, 0, size, size);

			// CancelDraw canceldraw = new MapControl.CancelDraw();
			// canceldraw.setCanceled(false);
			// For now we'll use a trick to make sure the layer is drawn
			// even if it has scale dependency
			// In the future, a scale corresponding to the tile should be passed
			double scale = 0.5 * (_layer.getMaxScale() + _layer.getMinScale());
			int trans = 255;

			// The layers of remote services donï¿½t have transparency.
			if (_layer.getClass().equals(FLyrVect.class)) {
				// Generate layers 3D properties
				Layer3DProps props3D = Layer3DProps.getLayer3DProps(_layer);
//				// Getting layer transparency
//				trans = ((FLyrDefault) _layer).getTransparency();
//				// Casting to float
//				float transFloat = (float) ((float) (trans) / 255.0);
//				// Setting in the layers 3D props
//				props3D.setOpacity(transFloat);
				// fix opaque the layer to save in disk
				((FLyrDefault) _layer).setTransparency((int) (props3D.getOpacity()*255));
			}

			try {
				// disabling the iterator
				// if (_layer instanceof FLyrVect) {
				// FLyrVect layerVect = (FLyrVect) _layer;
				// boolean strategyUse = layerVect.isUseStrategy();
				// layerVect.setUseStrategy(true);
				// layerVect.draw(image, graphics, vp, new MyCancel(), scale);
				// layerVect.setUseStrategy(strategyUse);
				// } else
				_layer.draw(image, graphics, vp, new MyCancel(), scale);
			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
			// Restoring the real value of transparency
			if (_layer.getClass().equals(FLyrVect.class)) {
				// fix the real transparency to the layer
				((FLyrDefault) _layer).setTransparency(trans);
			}

			try {
				File file = new File(fName);
				String format = getFileExtension().substring(1);
				saveCachedFile(image, format, file);
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
			return image;
		} else { // Elevation data
			// TODO Un toggle this comment to use WCS and WMS extensions.
			if (_layer instanceof FLyrWMS) { // WMS Elevation
				try {
					FLyrWMS wmsLayer = (FLyrWMS) _layer;
					if (_wmsDriver == null) {

						URL host = wmsLayer.getHost();
						_wmsDriver = FMapWMSDriverFactory
								.getFMapDriverForURL(host);
					}
					if (_wmsStatus == null) {
						_wmsStatus = new WMSStatus();
						_wmsStatus.setFormat(wmsLayer.getFormat());
						_wmsStatus.setHeight(32);
						_wmsStatus.setWidth(32);
						_wmsStatus.setLayerNames(Utilities.createVector(
								wmsLayer.getLayerQuery(), ","));
						_wmsStatus.setSrs(wmsLayer.getSRS());

						HashMap props = wmsLayer.getProperties();
						Vector styles;
						Vector dimensions;
						styles = (Vector) (props.get("styles"));
						dimensions = (Vector) (props.get("dimensions"));
						_wmsStatus.setStyles(styles);
						_wmsStatus.setDimensions(dimensions);
						_wmsStatus.setTransparency(false);
						String getMapStr = wmsLayer.getOnlineResource("GetMap");
						_wmsStatus.setOnlineResource(getMapStr);
					}
					_wmsStatus.setExtent(tileExtent);
					File f = _wmsDriver.getMap(_wmsStatus, new MyCancel());
					if (f == null)
						return null;
					FileUtils.copy(f, new File(fName));

					String fileName = f.getPath();
					// System.out.println(fileName);

				} catch (Exception e) {
					return null;
				}
			} else if (_layer instanceof FLyrWCS) { // WCS Elevation
				try {
					FLyrWCS wcsLayer = (FLyrWCS) _layer;
					if (_wcsDriver == null) {

						URL host = wcsLayer.getHost();
						_wcsDriver = FMapWCSDriverFactory
								.getFMapDriverForURL(host);
					}
					if (_wcsStatus == null) {
						_wcsStatus = new WCSStatus();

						Hashtable props = wcsLayer.getProperties();

						_wcsStatus.setCoveraName((String) props.get("name"));
						_wcsStatus.setFormat((String) props.get("format"));
						_wcsStatus.setHeight(32);
						_wcsStatus.setWidth(32);
						_wcsStatus.setSrs((String) props.get("crs"));
						_wcsStatus.setParameters((String) props
								.get("parameter"));
						String time = (String) props.get("time");
						if (time.length() > 0)
							_wcsStatus.setTime(time);
						// _wcsStatus.setOnlineResource(wcsLayer.getOnlineResource("GetCoverage"));
					}
					_wcsStatus.setExtent(tileExtent);

					File f = _wcsDriver.getCoverage(_wcsStatus, new MyCancel());
					if (f == null)
						return null;

					FileUtils.copy(f, new File(fName));

					String fileName = f.getPath();
					// System.out.println(fileName);

				} catch (Exception e) {
					return null;
				}
			}
			// RASTER_GRID
			if (_layer instanceof FLyrRasterSE) {
				// Grid grid = null;
				FLyrRasterSE rasterLayer = (FLyrRasterSE) _layer;

				// grid = rasterLayer.getSource().getGrid();
				// if (grid == null)
				// return null;

				BufferFactory bufferFactory = new BufferFactory(rasterLayer
						.getDataSource());

				int bands[] = { 0 };
				bufferFactory.setDrawableBands(bands);
				// bufferFactory.setNoDataValue(0.0);
				// ((IBuffer) bufferFactory).setNoDataValue(0.0);
				bufferFactory.setAdjustToExtent(false);

				try {
					bufferFactory.setAreaOfInterest(tileExtent.getMinX(),
							tileExtent.getMinY(), tileExtent.getMaxX(),
							tileExtent.getMaxY(), 32, 32);
				} catch (ArrayIndexOutOfBoundsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidSetViewException e1) {
					// // TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// // TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RasterDriverException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RasterBuffer raster = (RasterBuffer) bufferFactory
						.getRasterBuf();
				raster.setNoDataValue(0.0);
				try {
					// raster.save(fName, new Extent(tileExtent));
					WriterBufferServer bufferServer = new WriterBufferServer(
							raster);
					Params p = GeoRasterWriter.getWriter(fName).getParams();
					AffineTransform at = new AffineTransform(tileExtent
							.getWidth() / 32, 0, 0,
							-(tileExtent.getHeight() / 32), tileExtent
									.getMinX(), tileExtent.getMaxY());
					GeoRasterWriter grw = GeoRasterWriter.getWriter(
							bufferServer, fName, 1, at, raster.getWidth(),
							raster.getHeight(), raster.getDataType(), p, null);
					// grw.setWkt(d.getWktProjection());
					try {
						grw.dataWrite();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					grw.writeClose();
				} catch (NotSupportedExtensionException e) {
					e.printStackTrace();
				} catch (RasterDriverException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return null; // no image
		}

	}

	public String getTileAsFName(TileNum t, Rectangle2D extent)
			throws CacheServiceException {
		if (_loadFailed) {
			throw new CacheServiceException(new Exception());
		}
		String tileId = t.numToOpTileId();
		String fName = getTileFileName(t);
		// System.out.println("CMTexture2D: requestTexture: "+tileId);
		if (!new File(fName).exists()) {
			Rectangle2D tileExtent = extent;
			getTileFromLayer(fName, tileExtent);
		}
		return fName;
	}

	public Image getTileAsImage(TileNum tileNum, Rectangle2D extent)
			throws CacheServiceException {
		if ((tileNum.getLevel() == 5)) {// && (tileNum.getX())==29 &&
			// (tileNum.getY()==9)){
			System.err.println("pillado");
		}

		if (_loadFailed) {
			throw new CacheServiceException(new Exception());
		}
		String tileId = tileNum.numToOpTileId();
		String fName = getTileFileName(tileNum);
		// System.out.println("CMTexture2D: requestTexture: "+tileId);
		Rectangle2D tileExtent = extent;
		return getTileFromLayer(fName, tileExtent);
	}

	public boolean intersectsLayer(Rectangle2D extent) {
//		Rectangle2D tileExtent = extent;
		return _lyrExtentRect.intersects(extent);
		// return Math.max(_lyrExtentRect.getMinX(),tileExtent.getMinX()) <=
		// Math.min(_lyrExtentRect.getMaxX(),tileExtent.getMaxX()) &&
		// Math.max(Math.min(_lyrExtentRect.getMinY(),_lyrExtentRect.getMaxY()),tileExtent.getMinY())
		// <=
		// Math.min(Math.max(_lyrExtentRect.getMinY(),_lyrExtentRect.getMaxY()),tileExtent.getMaxY());
	}

}
