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
package org.gvsig.fmap.mapcontext.layers.vectorial;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.Set;
import java.util.TreeSet;

import org.cresques.cts.ICoordTrans;
import org.gvsig.compat.CompatLocator;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.ConcurrentDataModificationException;
import org.gvsig.fmap.dal.feature.exception.CreateGeometryException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.Geometry.TYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.operation.DrawInts;
import org.gvsig.fmap.geom.operation.DrawOperationContext;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.Circle;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LegendLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ReprojectLayerException;
import org.gvsig.fmap.mapcontext.exceptions.StartEditionLayerException;
import org.gvsig.fmap.mapcontext.exceptions.XMLLayerException;
import org.gvsig.fmap.mapcontext.layers.AbstractLinkProperties;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLyrDefault;
import org.gvsig.fmap.mapcontext.layers.LayerEvent;
import org.gvsig.fmap.mapcontext.layers.SpatialCache;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;
import org.gvsig.fmap.mapcontext.layers.operations.ILabelable;
import org.gvsig.fmap.mapcontext.layers.operations.InfoByPoint;
import org.gvsig.fmap.mapcontext.layers.operations.SingleLayer;
import org.gvsig.fmap.mapcontext.layers.operations.VectorialXMLItem;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.gvsig.fmap.mapcontext.rendering.legend.IClassifiedVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.SingleSymbolLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.SymbolLegendEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.XMLLegendException;
import org.gvsig.fmap.mapcontext.rendering.legend.ZSort;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendClearEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendContentsChangedListener;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.AttrInTableLabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.ILabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.LabelingFactory;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.FSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMultiLayerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.locator.LocatorException;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceManager;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;
import org.gvsig.tools.persistence.xmlentity.XMLEntityState;
import org.gvsig.tools.task.Cancellable;
import org.slf4j.LoggerFactory;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Capa básica Vectorial.
 *
 * @author Fernando González Cortés
 */

// TODO Cuando no sea para pruebas debe no ser public
public class FLyrVect extends FLyrDefault implements ILabelable, InfoByPoint,
ClassifiableVectorial, SingleLayer, LegendContentsChangedListener,
Observer {
	final static private org.slf4j.Logger logger = LoggerFactory.getLogger(FLyrVect.class);
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	/** Leyenda de la capa vectorial */
	private IVectorLegend legend;
	private int typeShape = -1;
	private FeatureStore featureStore=null;
	private SpatialCache spatialCache = new SpatialCache();
	private boolean spatialCacheEnabled = false;

	/**
	 * An implementation of gvSIG spatial index
	 */
	//    protected ISpatialIndex spatialIndex = null;
	private boolean bHasJoin = false;
	private XMLEntity orgXMLEntity = null;
	private XMLEntity loadSelection = null;
	private IVectorLegend loadLegend = null;

	//Lo añado. Características de HyperEnlace (LINK)
	private FLyrVectLinkProperties linkProperties=new FLyrVectLinkProperties();

	/**
	 * Devuelve el VectorialAdapater de la capa.
	 *
	 * @return VectorialAdapter.
	 */
	public DataStore getDataStore() {
		if (!this.isAvailable()) {
			return null;
		}
		return featureStore;
	}

	/**
	 * If we use a persistent spatial index associated with this layer, and the
	 * index is not intrisic to the layer (for example spatial databases) this
	 * method looks for existent spatial index, and loads it.
	 *
	 */
	//    private void loadSpatialIndex() {
	//        //FIXME: Al abrir el indice en fichero...
	//        //¿Cómo lo liberamos? un metodo Layer.shutdown()
	//
	//
	//        ReadableVectorial source = getSource();
	//        //REVISAR QUE PASA CON LOS DRIVERS DXF, DGN, etc.
	//        //PUES SON VECTORIALFILEADAPTER
	//        if (!(source instanceof VectorialFileAdapter)) {
	//            // we are not interested in db adapters
	//            return;
	//        }
	//        VectorialDriver driver = source.getDriver();
	//        if (!(driver instanceof BoundedShapes)) {
	//            // we dont spatially index layers that are not bounded
	//            return;
	//        }
	//        File file = ((VectorialFileAdapter) source).getFile();
	//        String fileName = file.getAbsolutePath();
	//        File sptFile = new File(fileName + ".qix");
	//        if (!sptFile.exists() || (!(sptFile.length() > 0))) {
	//            // before to exit, look for it in temp path
	//            String tempPath = System.getProperty("java.io.tmpdir");
	//            fileName = tempPath + File.separator + sptFile.getName();
	//            sptFile = new File(fileName);
	//            // it doesnt exists, must to create
	//            if (!sptFile.exists() || (!(sptFile.length() > 0))) {
	//                return;
	//            }// if
	//        }// if
	//
	//        try {
	//            source.start();
	//            spatialIndex = new QuadtreeGt2(FileUtils.getFileWithoutExtension(sptFile),
	//                    "NM", source.getFullExtent(), source.getShapeCount(), false);
	//            source.setSpatialIndex(spatialIndex);
	//        } catch (SpatialIndexException e) {
	//            spatialIndex = null;
	//            e.printStackTrace();
	//            return;
	//        } catch (ReadDriverException e) {
	//            spatialIndex = null;
	//            e.printStackTrace();
	//            return;
	//        }
	//
	//    }

	/**
	 * Checks if it has associated an external spatial index
	 * (an spatial index file).
	 *
	 * It looks for it in main file path, or in temp system path.
	 * If main file is rivers.shp, it looks for a file called
	 * rivers.shp.qix.

	 * @return
	 */
	//    public boolean isExternallySpatiallyIndexed() {
	//        /*
	//         * FIXME (AZABALA): Independizar del tipo de fichero de índice
	//          * con el que se trabaje (ahora mismo considera la extension .qix,
	//         * pero esto dependerá del tipo de índice)
	//         * */
	//        ReadableVectorial source = getSource();
	//        if (!(source instanceof VectorialFileAdapter)) {
	//            // we are not interested in db adapters.
	//            // think in non spatial dbs, like HSQLDB
	//            return false;
	//        }
	//        File file = ((VectorialFileAdapter) source).getFile();
	//        String fileName = file.getAbsolutePath();
	//        File sptFile = new File(fileName + ".qix");
	//        if (!sptFile.exists() || (!(sptFile.length() > 0))) {
	//            // before to exit, look for it in temp path
	//            // it doesnt exists, must to create
	//            String tempPath = System.getProperty("java.io.tmpdir");
	//            fileName = tempPath + File.separator + sptFile.getName();
	//            sptFile = new File(fileName);
	//            if (!sptFile.exists() || (!(sptFile.length() > 0))) {
	//                return false;
	//            }// if
	//        }// if
	//        return true;
	//    }
	/**
	 * Inserta el VectorialAdapter a la capa.
	 *
	 * @param va
	 *            VectorialAdapter.
	 *
	 * @deprecated esto debería se ser protected
	 */
  	public void setDataStore(DataStore dataStore) throws LoadLayerException {
		if (this.featureStore != null && this.featureStore != dataStore){
			this.featureStore.deleteObserver(this);
		}

		featureStore = (FeatureStore)dataStore;

		ILegend legend = null;
		try {
			legend = (ILegend) dataStore.invokeDynMethod(
					"getLegend", null);

		} catch (DynMethodNotSupportedException e1) {
			try {
				legend = LegendFactory.createSingleSymbolLegend(this
						.getShapeType());
			} catch (ReadException e) {
				throw new LoadLayerException(this.getName(), e);
			}

		} catch (DynMethodException e1) {
			throw new LoadLayerException(this.getName(), e1);
		}
		this.setLegend((IVectorLegend) legend);


		ILabelingStrategy labeler = null;
		try {
			labeler = (ILabelingStrategy) dataStore.invokeDynMethod(
					"getLabeling", null);
		} catch (DynMethodNotSupportedException e1) {
			labeler = null;
		} catch (DynMethodException e1) {
			throw new LoadLayerException(this.getName(), e1);
		}

		if (labeler != null) {
			try {
				labeler.setLayer(this);
			} catch (ReadException e) {
				throw new LoadLayerException(this.getName(), e);
			}
			this.setLabelingStrategy(labeler);
			this.setIsLabeled(true); // TODO: ací no s'hauria de detectar si té etiquetes?????
		}

		this.delegate(dataStore);

		dataStore.addObserver(this);

		// azabala: we check if this layer could have a file spatial index
		// and load it if it exists
		//        loadSpatialIndex();
	}

	public Envelope getFullEnvelope() throws ReadException {
		Envelope rAux;
		try {
			rAux = getFeatureStore().getEnvelope();
		} catch (BaseException e) {
			throw new ReadException(getName(),e);
		}

		//Esto es para cuando se crea una capa nueva con el fullExtent de ancho y alto 0.
		if (rAux == null || rAux.getMaximum(0)-rAux.getMinimum(0)==0 && rAux.getMaximum(1)-rAux.getMinimum(1)==0) {
			try {
				rAux= geomManager.createEnvelope(0,0,100,100, SUBTYPES.GEOM2D);
			} catch (CreateEnvelopeException e) {
				logger.error("Error creating the envelope", e);
				e.printStackTrace();
			}
		}
		// Si existe reproyección, reproyectar el extent
		ICoordTrans ct = getCoordTrans();
		try{
			if (ct != null) {
				Point2D pt1 = new Point2D.Double(rAux.getMinimum(0), rAux.getMinimum(1));
				Point2D pt2 = new Point2D.Double(rAux.getMaximum(0), rAux.getMaximum(1));
				pt1 = ct.convert(pt1, null);
				pt2 = ct.convert(pt2, null);
				try {
					rAux = geomManager.createEnvelope(pt1.getX(),pt1.getY(),pt2.getX(),pt2.getY(), SUBTYPES.GEOM2D);
				} catch (CreateEnvelopeException e) {
					logger.error("Error creating the envelope", e);
					e.printStackTrace();
				}//new Rectangle2D.Double();
			}
		}catch (IllegalStateException e) {
			this.setAvailable(false);
			this.addError(new ReprojectLayerException(getName(), e));
		}
		return rAux;

	}

	/**
	 * Draws using IFeatureIterator. This method will replace the old draw(...) one.
	 * @autor jaume dominguez faus - jaume.dominguez@iver.es
	 * @param image
	 * @param g
	 * @param viewPort
	 * @param cancel
	 * @param scale
	 * @throws ReadDriverException
	 */
	public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale) throws ReadException {
		if (!this.isWithinScale(scale)) {
			return;
		}
		if (cancel.isCanceled()) {
			return;
		}
		boolean containsAll = false;
		Envelope viewPortEnvelope =viewPort.getAdjustedExtent();
		Envelope viewPortEnvelopeInMyProj = viewPortEnvelope;
		// FIXME
		if (this.getCoordTrans() != null) {
			viewPortEnvelopeInMyProj = viewPortEnvelope
					.convert(this
					.getCoordTrans().getInverted());

		}


		Envelope myEnvelope;
		try {
			myEnvelope = this.getFullEnvelope();
		} catch (ConcurrentDataModificationException e) {
			cancel.setCanceled(true);
			return;
		}
		if (!viewPortEnvelope.intersects(myEnvelope)){
			return;
		}
		containsAll = viewPortEnvelope.contains(myEnvelope);
		double dpi = MapContext.getScreenDPI();
		DrawOperationContext doc=new DrawOperationContext();
		doc.setViewPort(viewPort);
		doc.setScale(scale);
		doc.setCancellable(cancel);
		doc.setDPI(dpi);
		boolean bDrawShapes = true;
		if (legend instanceof SingleSymbolLegend) {
			bDrawShapes = legend.getDefaultSymbol().isShapeVisible();
		}
		Point2D offset = viewPort.getOffset();

		if (bDrawShapes) {
			if (cancel.isCanceled()) {
				return;
			}
			boolean cacheFeatures = isSpatialCacheEnabled();
			SpatialCache cache = null;
			if (cacheFeatures) {
				getSpatialCache().clearAll();
				cache = getSpatialCache();
			}

			try {

				FeatureSelection selection = this.featureStore
				.getFeatureSelection();

				FeatureStore featureStore=getFeatureStore();
				String[] fieldNames=null;
				if (legend instanceof IClassifiedVectorLegend){
					String[] classified=((IClassifiedVectorLegend)legend).getClassifyingFieldNames();
					fieldNames=new String[classified.length+1];
					fieldNames[0]=featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName();
					for (int i = 1; i < fieldNames.length; i++) {
						fieldNames[i]=classified[i-1];
					}

				}else{
					fieldNames=new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()};
				}
				FeatureSet featureSet=null;
				FeatureQuery featureQuery=featureStore.createFeatureQuery();
				featureQuery.setScale(scale);
				featureQuery.setAttributeNames(fieldNames);
				if (!containsAll) {
					IntersectsEnvelopeEvaluator iee = new IntersectsEnvelopeEvaluator(
							viewPortEnvelopeInMyProj, getProjection(),
							featureStore.getDefaultFeatureType(),
							featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
					featureQuery.setFilter(iee);

				}
				featureSet = featureStore.getFeatureSet(featureQuery);
				DisposableIterator it = featureSet.fastIterator();

				ZSort zSort = ((IVectorLegend) getLegend()).getZSort();

				boolean bSymbolLevelError = false;

				// if layer has map levels it will use a ZSort
				boolean useZSort = zSort != null && zSort.isUsingZSort();

				// -- visual FX stuff
				long time = System.currentTimeMillis();
				BufferedImage virtualBim;
				Graphics2D virtualGraphics;

				if (cancel.isCanceled()) {
					return;
				}

				// render temporary map each screenRefreshRate milliseconds;
				int screenRefreshDelay = (int) ((1D/MapContext.getDrawFrameRate())*3*1000);
				BufferedImage[] imageLevels = null;
				Graphics2D[] graphics = null;
				if (useZSort) {
					imageLevels = new BufferedImage[zSort.getLevelCount()];
					graphics = new Graphics2D[imageLevels.length];
					for (int i = 0; !cancel.isCanceled() && i < imageLevels.length; i++) {
						
						imageLevels[i] =
							CompatLocator.getGraphicsUtils().createBufferedImage(
									image.getWidth(), image.getHeight(), image.getType());
						
						graphics[i] = imageLevels[i].createGraphics();
						graphics[i].setTransform(g.getTransform());
						graphics[i].setRenderingHints(g.getRenderingHints());
					}
				}
				// -- end visual FX stuff

				// FIXME geometry reproject temporaly patch
				// FeatureAttributeDescriptor attrGeom =
				// featureStore.getDefaultFeatureType().getAttributeDescriptor(featureStore.getDefaultFeatureType().getDefaultGeometryAttributeIndex());
				// ICoordTrans myct = null;
				// if (!viewPort.getProjection().equals(attrGeom.getSRS())) {
				// myct = viewPort.getProjection().getCT(attrGeom.getSRS());
				// }


				try {
					// Iteration over each feature
					while (it.hasNext()) {
						if (cancel.isCanceled()) {
							return;
						}
						Feature feat = (Feature) it.next();

						Geometry geom = feat.getDefaultGeometry();

						if (geom.getType() == Geometry.TYPES.NULL) {
							continue;
						}

						if (this.getCoordTrans() != null) {
							geom = geom.cloneGeometry();
							geom.reProject(this.getCoordTrans());
						}

						if (cacheFeatures) {
							if (cache.getMaxFeatures() >= cache.size()) {
								// already reprojected
								cache.insert(geom.getEnvelope(), geom);
							}
						}

						// retrieve the symbol associated to such feature
						ISymbol sym = legend.getSymbolByFeature(feat);
						if (selection.isSelected(feat)) {
							sym = sym.getSymbolForSelection();
						}
						if (sym == null) {
							continue;
						}

						// Check if this symbol is sized with
						// CartographicSupport
						CartographicSupport csSym = null;
						int symbolType = sym.getSymbolType();
						boolean bDrawCartographicSupport = false;

						if (symbolType == Geometry.TYPES.POINT
								|| symbolType == Geometry.TYPES.CURVE
								|| sym instanceof CartographicSupport) {

							// patch
							if (!sym.getClass().equals(FSymbol.class)) {
								csSym = (CartographicSupport) sym;
								bDrawCartographicSupport = (csSym.getUnit() != -1);
							}
						}

						int x = -1;
						int y = -1;
						int[] xyCoords = new int[2];

						// Check if size is a pixel
						boolean onePoint = bDrawCartographicSupport ? isOnePoint(
								g.getTransform(), viewPort, MapContext
										.getScreenDPI(), csSym, geom, xyCoords)
								: isOnePoint(g.getTransform(), viewPort, geom,
										xyCoords);

								// Avoid out of bounds exceptions
						if (onePoint) {
							x = xyCoords[0];
							y = xyCoords[1];
							if (x < 0 || y < 0 || x >= viewPort.getImageWidth()
									|| y >= viewPort.getImageHeight()) {
								continue;
							}
						}

								if (cancel.isCanceled()) {
							return;
								}

								if (useZSort) {
							// Check if this symbol is a multilayer
							int[] symLevels = zSort.getLevels(sym);
							if (sym instanceof IMultiLayerSymbol) {
								// if so, treat each of its layers as a single
								// symbol
								// in its corresponding map level
								IMultiLayerSymbol mlSym = (IMultiLayerSymbol) sym;
								for (int i = 0; !cancel.isCanceled()
										&& i < mlSym.getLayerCount(); i++) {
									ISymbol mySym = mlSym.getLayer(i);
									int symbolLevel = 0;
									if (symLevels != null) {
										symbolLevel = symLevels[i];
									} else {
										/*
										 * an error occured when managing symbol
										 * levels some of the legend changed
										 * events regarding the symbols did not
										 * finish satisfactory and the legend is
										 * now inconsistent. For this drawing,
										 * it will finish as it was at the
										 * bottom (level 0) but, when done, the
										 * ZSort will be reset to avoid app
										 * crashes. This is a bug that has to be
										 * fixed.
										 */
										bSymbolLevelError = true;
									}

											if (onePoint) {
										if (x < 0
												|| y < 0
												|| x >= imageLevels[symbolLevel]
														.getWidth()
												|| y >= imageLevels[symbolLevel]
														.getHeight()) {
											continue;
										}
										imageLevels[symbolLevel].setRGB(x, y,
												mySym.getOnePointRgb());
									} else {
										if (!bDrawCartographicSupport) {
											doc
													.setGraphics(graphics[symbolLevel]);
											doc.setSymbol(mySym);
											geom.invokeOperation(DrawInts.CODE,
													doc);
										} else {
											doc
													.setGraphics(graphics[symbolLevel]);
											doc.setSymbol(mySym);
											geom.invokeOperation(DrawInts.CODE,
													doc);
										}
									}
								}
							} else {
								// else, just draw the symbol in its level
										int symbolLevel = 0;
										if (symLevels != null) {
											symbolLevel = symLevels[0];
								}
								if (!bDrawCartographicSupport) {
									doc.setGraphics(graphics[symbolLevel]);
									doc.setSymbol(sym);
									geom.invokeOperation(DrawInts.CODE, doc);
										} else {
											doc.setGraphics(graphics[symbolLevel]);
									doc.setSymbol((ISymbol) csSym);
									geom.invokeOperation(DrawInts.CODE, doc);
										}
									}

									// -- visual FX stuff
							// Cuando el offset!=0 se está dibujando sobre el
							// Layout y por tanto no tiene que ejecutar el
							// siguiente código.
							if (offset.getX() == 0 && offset.getY() == 0) {
								if ((System.currentTimeMillis() - time) > screenRefreshDelay) {
									
									virtualBim = CompatLocator.getGraphicsUtils().createBufferedImage(
											image.getWidth(), image.getHeight(),
											BufferedImage.TYPE_INT_ARGB);
									
									virtualGraphics = virtualBim
											.createGraphics();
									virtualGraphics
											.drawImage(image, 0, 0, null);
									for (int i = 0; !cancel.isCanceled()
											&& i < imageLevels.length; i++) {
										virtualGraphics.drawImage(
												imageLevels[i], 0, 0, null);
											}
											g.clearRect(0, 0, image.getWidth(), image
											.getHeight());
									g.drawImage(virtualBim, 0, 0, null);
									time = System.currentTimeMillis();
										}
										// -- end visual FX stuff
									}

								} else {
									// no ZSort, so there is only a map level, symbols
							// are
							// just drawn.
							if (onePoint) {
								if (x < 0 || y < 0 || x >= image.getWidth()
										|| y >= image.getHeight()) {
									continue;
								}
								image.setRGB(x, y, sym.getOnePointRgb());
									} else {

										if (!bDrawCartographicSupport) {
									doc.setGraphics(g);
									doc.setSymbol(sym);
									geom.invokeOperation(DrawInts.CODE, doc);
								} else {
									doc.setGraphics(g);
									doc.setSymbol((ISymbol) csSym);
									geom.invokeOperation(DrawInts.CODE, doc);
										}
									}
								}
					}
				} catch (ConcurrentDataModificationException e) {
					cancel.setCanceled(true);
					return;
				}

				if (useZSort) {
					g.drawImage(image, 0, 0, null);
					
					CompatLocator.getGraphicsUtils().translate(g, offset.getX(), offset.getY());

					for (int i = 0; !cancel.isCanceled() && i < imageLevels.length; i++) {
						g.drawImage(imageLevels[i],0,0, null);
						imageLevels[i] = null;
						graphics[i] = null;
					}
					
					CompatLocator.getGraphicsUtils().translate(g, -offset.getX(), -offset.getY());
					
					imageLevels = null;
					graphics = null;
				}
				it.dispose();
				featureSet.dispose();

				if (bSymbolLevelError) {
					((IVectorLegend) getLegend()).setZSort(null);
				}

			} catch (ReadException e) {
				this.setVisible(false);
				this.setActive(false);
				throw e;
			} catch (GeometryOperationNotSupportedException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			} catch (GeometryOperationException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			} catch (BaseException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			}


		}
	}
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale, PrintAttributes properties) throws ReadException {
		// TEST METHOD
		boolean bDrawShapes = true;
		if (legend instanceof SingleSymbolLegend) {
			bDrawShapes = legend.getDefaultSymbol().isShapeVisible();
		}
		if (bDrawShapes) {
			try {
				double dpi = 72;

				int resolution = properties.getPrintQuality();
				
				if (resolution == PrintAttributes.PRINT_QUALITY_NORMAL){
					dpi = 300;
				} else if (resolution == PrintAttributes.PRINT_QUALITY_HIGH){
					dpi = 600;
				} else if (resolution == PrintAttributes.PRINT_QUALITY_DRAFT){
					dpi = 72;
				}
				ZSort zSort = ((IVectorLegend) getLegend()).getZSort();

				// if layer has map levels it will use a ZSort
				boolean useZSort = zSort != null && zSort.isUsingZSort();


				int mapLevelCount = (useZSort) ? zSort.getLevelCount() : 1;
				for (int mapPass = 0; mapPass < mapLevelCount; mapPass++) {
					// Get the iterator over the visible features
					FeatureStore featureStore=getFeatureStore();
					// Get the iterator over the visible features
					//        			String featureFilter = null;
					//
					//        			if (!viewPort.getAdjustedExtent().contains((Envelope)featureStore.getMetadata().get("extent"))) {
					//    					featureFilter=this.getDataStoreFilterForGeomerty(
					//    						viewPort.getAdjustedExtent().getGeometry(),
					//    						featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName(),
					//    						null);
					//    				}
					String[] fieldNames=null;
					if (legend instanceof IClassifiedVectorLegend){
						String[] classified=((IClassifiedVectorLegend)legend).getClassifyingFieldNames();
						fieldNames=new String[classified.length+1];
						fieldNames[0]=featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName();
						for (int i = 1; i < fieldNames.length; i++) {
							fieldNames[i]=classified[i-1];
						}

					}else{
						fieldNames=new String[]{featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName()};
					}


					FeatureSet featureSet=null;
					FeatureQuery featureQuery=featureStore.createFeatureQuery();
					featureQuery.setAttributeNames(fieldNames);
					featureQuery.setScale(scale);
					//        			ññSQLJEPEvaluator evaluator=new SQLJEPEvaluator(featureFilter);
					ContainsEnvelopeEvaluator iee=new ContainsEnvelopeEvaluator(viewPort.getAdjustedExtent(),viewPort.getProjection(),featureStore.getDefaultFeatureType(),featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName());
					featureQuery.setFilter(iee);
					featureSet = featureStore.getFeatureSet(featureQuery);
					DisposableIterator it = featureSet.fastIterator();

					// Iteration over each feature
					while ( !cancel.isCanceled() && it.hasNext()) {
						Feature feat = (Feature)it.next();
						Geometry geom = feat.getDefaultGeometry();

						// retreive the symbol associated to such feature
						ISymbol sym = legend.getSymbolByFeature(feat);
						if (sym == null) {
							continue;
						}
						if (useZSort) {
							int[] symLevels = zSort.getLevels(sym);
							if(symLevels != null){

								// Check if this symbol is a multilayer
								if (sym instanceof IMultiLayerSymbol) {
									// if so, get the layer corresponding to the current
									// level. If none, continue to next iteration
									IMultiLayerSymbol mlSym = (IMultiLayerSymbol) sym;
									for (int i = 0; i < mlSym.getLayerCount(); i++) {
										ISymbol mySym = mlSym.getLayer(i);
										if (symLevels[i] == mapPass) {
											sym = mySym;
											break;
										}
										System.out.println("avoided layer "+i+"of symbol '"+mlSym.getDescription()+"' (pass "+mapPass+")");
									}

								} else {
									// else, just draw the symbol in its level
									if (symLevels[0] != mapPass) {
										System.out.println("avoided single layer symbol '"+sym.getDescription()+"' (pass "+mapPass+")");
										continue;
									}
								}
							}
						}

						// Check if this symbol is sized with CartographicSupport
						CartographicSupport csSym = null;
						int symbolType = sym.getSymbolType();

						if (   symbolType == Geometry.TYPES.POINT
								|| symbolType == Geometry.TYPES.CURVE
								|| sym instanceof CartographicSupport) {

							csSym = (CartographicSupport) sym;
						}

//						System.err.println("passada "+mapPass+" pinte símboll "+sym.getDescription());

//						if (!bDrawCartographicSupport) {
//							DrawOperationContext doc=new DrawOperationContext();
//							doc.setGraphics(g);
//							doc.setViewPort(viewPort);
//							doc.setSymbol(sym);
//							doc.setCancellable(cancel);
//							geom.invokeOperation(DrawInts.CODE,doc);
//						} else {
							DrawOperationContext doc=new DrawOperationContext();
							doc.setGraphics(g);
							doc.setViewPort(viewPort);
							if (csSym==null){
								doc.setSymbol(sym);
							} else {
								doc.setDPI(dpi);
								doc.setCancellable(cancel);
								doc.setSymbol((ISymbol)csSym);
							}
							geom.invokeOperation(DrawInts.CODE,doc);
//						}
					}
					it.dispose();
					it=null;
					featureSet.dispose();
				}
			} catch (ReadException e) {
				this.setVisible(false);
				this.setActive(false);
				throw e;
			} catch (GeometryOperationNotSupportedException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			} catch (GeometryOperationException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			} catch (BaseException e) {
				this.setVisible(false);
				this.setActive(false);
				throw new ReadException(getName(),e);
			}
		}
	}

	/**
	 * <p>
	 * Creates an spatial index associated to this layer.
	 * The spatial index will used
	 * the native projection of the layer, so if the layer is reprojected, it will
	 * be ignored.
	 * </p>
	 * @param cancelMonitor instance of CancellableMonitorable that allows
	 * to monitor progress of spatial index creation, and cancel the process
	 */
	//    public void createSpatialIndex(CancellableMonitorable cancelMonitor){
	//         // FJP: ESTO HABRÁ QUE CAMBIARLO. PARA LAS CAPAS SECUENCIALES, TENDREMOS
	//        // QUE ACCEDER CON UN WHILE NEXT. (O mejorar lo de los FeatureVisitor
	//        // para que acepten recorrer sin geometria, solo con rectangulos.
	//
	//        //If this vectorial layer is based in a spatial database, the spatial
	//        //index is already implicit. We only will index file drivers
	//        ReadableVectorial va = getSource();
	//        //We must think in non spatial databases, like HSQLDB
	//        if(!(va instanceof VectorialFileAdapter)){
	//            return;
	//        }
	//        if (!(va.getDriver() instanceof BoundedShapes)) {
	//            return;
	//        }
	//        File file = ((VectorialFileAdapter) va).getFile();
	//        String fileName = file.getAbsolutePath();
	//        ISpatialIndex localCopy = null;
	//        try {
	//            va.start();
	//            localCopy = new QuadtreeGt2(fileName, "NM", va.getFullExtent(),
	//                    va.getShapeCount(), true);
	//
	//        } catch (SpatialIndexException e1) {
	//            // Probably we dont have writing permissions
	//            String directoryName = System.getProperty("java.io.tmpdir");
	//            File newFile = new File(directoryName +
	//                    File.separator +
	//                    file.getName());
	//            String newFileName = newFile.getName();
	//            try {
	//                localCopy = new QuadtreeGt2(newFileName, "NM", va.getFullExtent(),
	//                        va.getShapeCount(), true);
	//            } catch (SpatialIndexException e) {
	//                // if we cant build a file based spatial index, we'll build
	//                // a pure memory spatial index
	//                localCopy = new QuadtreeJts();
	//            } catch (ReadException e) {
	//                localCopy = new QuadtreeJts();
	//            }
	//
	//        } catch(Exception e){
	//            e.printStackTrace();
	//        }//try
	//        BoundedShapes shapeBounds = (BoundedShapes) va.getDriver();
	//        try {
	//            for (int i=0; i < va.getShapeCount(); i++)
	//            {
	//                if(cancelMonitor != null){
	//                    if(cancelMonitor.isCanceled())
	//                        return;
	//                    cancelMonitor.reportStep();
	//                }
	//                Rectangle2D r = shapeBounds.getShapeBounds(i);
	//                if(r != null)
	//                    localCopy.insert(r, i);
	//            } // for
	//            va.stop();
	//            if(localCopy instanceof IPersistentSpatialIndex)
	//                ((IPersistentSpatialIndex) localCopy).flush();
	//            spatialIndex = localCopy;
	//            //vectorial adapter needs a reference to the spatial index, to solve
	//            //request for feature iteration based in spatial queries
	//            source.setSpatialIndex(spatialIndex);
	//        } catch (ReadException e) {
	//            // TODO Auto-generated catch block
	//            e.printStackTrace();
	//        }
	//    }

	//    public void createSpatialIndex() {
	//        createSpatialIndex(null);
	//    }


	public void setLegend(IVectorLegend r) throws LegendLayerException {
		if (this.legend == r){
			return;
		}
		if (this.legend != null && this.legend.equals(r)){
			return;
		}
		IVectorLegend oldLegend = legend;
		legend = r;
		try {
			legend.setFeatureStore(getFeatureStore());
		} catch (ReadException e1) {
			throw new LegendLayerException(getName(),e1);
		} catch (DataException e) {
			throw new LegendLayerException(getName(),e);
		} finally{
			this.updateDrawVersion();
		}

		if (oldLegend != null) {
			oldLegend.removeLegendListener(this);
		}
		if (legend != null) {
			legend.addLegendListener(this);
		}

		LegendChangedEvent e = LegendChangedEvent.createLegendChangedEvent(
				oldLegend, legend);
		e.setLayer(this);
		callLegendChanged(e);
	}

	/**
	 * Devuelve la Leyenda de la capa.
	 *
	 * @return Leyenda.
	 */
	public ILegend getLegend() {
		return legend;
	}

	/**
	 * Devuelve el tipo de shape que contiene la capa.
	 *
	 * @return tipo de shape.
	 *
	 * @throws ReadException
	 */
	public int getShapeType() throws ReadException {
		if (typeShape == -1) {
			FeatureType featureType;
			try {
				featureType = (((FeatureStore)getDataStore()).getDefaultFeatureType());
			} catch (DataException e) {
				throw new ReadException(getName(),e);
			}
			int indexGeom=featureType.getDefaultGeometryAttributeIndex();
			typeShape=featureType.getAttributeDescriptor(indexGeom).getGeometryType();
		}
		return typeShape;
	}

	public XMLEntity getXMLEntity() throws XMLException {

		if (!this.isAvailable() && this.orgXMLEntity != null) {
			return this.orgXMLEntity;
		}
		XMLEntity xml = super.getXMLEntity();
		if (getLegend()!=null){
			XMLEntity xmlLegend=getLegend().getXMLEntity();
			xmlLegend.putProperty("tagName","legend");
			xml.addChild(xmlLegend);
		}
		try {
			PersistenceManager manager = ToolsLocator.getPersistenceManager();
			PersistentState stateFeatureStore=manager.getState(getFeatureStore());
			stateFeatureStore.set("tagName","featureStore");
			xml.addChild(((XMLEntityState)stateFeatureStore).getXMLEntity());
		} catch (ReadException e) {
			throw new XMLLayerException(getName(),e);
		} catch (PersistenceException e) {
			throw new XMLLayerException(getName(),e);
		}
		// properties from ILabelable
		xml.putProperty("isLabeled", isLabeled);
		if (strategy != null) {
			XMLEntity strategyXML = strategy.getXMLEntity();
			strategyXML.putProperty("tagName", "labelingStrategy");
			xml.addChild(strategy.getXMLEntity());
		}
		xml.addChild(getLinkProperties().getXMLEntity());
		return xml;
	}
	/*
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException {
		try {
			super.setXMLEntity(xml);
			XMLEntity legendXML = xml.firstChild("tagName","legend");
			IVectorLegend leg = LegendFactory.createFromXML(legendXML);

			//    	PersistentState persistentState=new XMLEntityState(new XMLEntityManager());
			XMLEntity xmlStore=xml.firstChild("tagName","featureStore");
			XMLEntityManager xmlManger = new XMLEntityManager();
			PersistentState state = xmlManger.createState(xmlStore);
			DataStore store = (DataStore) ToolsLocator.getPersistenceManager().create(state);
			//    	persistentState.createState(xmlStore);

			//    	DataManager dm=DALLocator.getDataManager();

			this.setDataStore(store);
			/* end patch */
			try {
				setLegend(leg);
			} catch (LegendLayerException e) {
				throw new XMLLegendException(e);
			}
			// set properties for ILabelable

			if (xml.contains("isLabeled")
					&& xml.getBooleanProperty(("isLabeled"))) {
				XMLEntity labelingXML = xml.firstChild("tagName", "labelingStrategy");
				if (labelingXML != null){

					isLabeled = true;
					try {
						this.strategy = LabelingFactory.createStrategyFromXML(labelingXML, this);
					} catch (NotExistInXMLEntity neXMLEX) {
						// no strategy was set, just continue;
						logger.warn("Reached what should be unreachable code");
					}
				} else {
					isLabeled = false;
				}
			} else if (legendXML.contains("labelFieldName")|| legendXML.contains("labelfield")) {
    			/* (jaume) begin patch;
        		 * for backward compatibility purposes. Since gvSIG v1.1 labeling is
        		 * no longer managed by the Legend but by the ILabelingStrategy. The
        		 * following allows restoring older projects' labelings.
        		 */
				String labelTextField =	null;
    			if (legendXML.contains("labelFieldName")){
    				labelTextField = legendXML.getStringProperty("labelFieldName");
    				if (labelTextField != null) {
    					AttrInTableLabelingStrategy labeling = new AttrInTableLabelingStrategy();
    					labeling.setLayer(this);
    					labeling.setUsesFixedSize(true);
    					labeling.setFixedSize(10);
    					labeling.setTextField(labelTextField);
    					labeling.setHeightField(legendXML.getStringProperty("labelHeightFieldName"));
    					labeling.setRotationField(legendXML.getStringProperty("labelRotationFieldName"));
    					this.setLabelingStrategy(labeling);
    					this.setIsLabeled(true);
    				}
    			}else{
    				labelTextField = legendXML.getStringProperty("labelfield");
    				if (labelTextField != null) {
    					AttrInTableLabelingStrategy labeling = new AttrInTableLabelingStrategy();
    					labeling.setLayer(this);
    					labeling.setUsesFixedSize(true);
    					labeling.setFixedSize(10);
    					labeling.setTextField(labelTextField);
    					labeling.setHeightField(legendXML.getStringProperty("labelFieldHeight"));
    					labeling.setRotationField(legendXML.getStringProperty("labelFieldRotation"));
    					this.setLabelingStrategy(labeling);
    					this.setIsLabeled(true);
    				}
    			}
        	}else{
				isLabeled = false;
			}
			XMLEntity xmlLinkProperties=xml.firstChild("typeChild","linkProperties");
			if (xmlLinkProperties != null){
				getLinkProperties().setXMLEntity(xmlLinkProperties);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setAvailable(false);
			this.orgXMLEntity = xml;

		}
		//

	}

	public void setXMLEntityNew(XMLEntity xml) throws XMLException {
		//        try {
		//            super.setXMLEntity(xml);
		//
		//            XMLEntity legendXML = xml.getChild(0);
		//            IVectorLegend leg = LegendFactory.createFromXML(legendXML);
		//            /* (jaume) begin patch;
		//             * for backward compatibility purposes. Since gvSIG v1.1 labeling is
		//             * no longer managed by the Legend but by the ILabelingStrategy. The
		//             * following allows restoring older projects' labelings.
		//             */
		//            if (legendXML.contains("labelFieldHeight")) {
		//                AttrInTableLabelingStrategy labeling = new AttrInTableLabelingStrategy();
		//                labeling.setLayer(this);
		//                labeling.setTextField(legendXML.getStringProperty("labelFieldHeight"));
		//                labeling.setRotationField(legendXML.getStringProperty("labelFieldRotation"));
		//                this.setLabelingStrategy(labeling);
		//                this.setIsLabeled(true);
		//              }
		//            /* end patch */
		//            try {
		//                getRecordset().getSelectionSupport().setXMLEntity(xml.getChild(1));
		//
		//                this.setLoadSelection(xml.getChild(1));
		//            } catch (ReadException e1) {
		//                this.setAvailable(false);
		//                throw new XMLException(e1);
		//            }
		//            // Si tiene una unión, lo marcamos para que no se cree la leyenda hasta
		//            // el final
		//            // de la lectura del proyecto
		//            if (xml.contains("hasJoin")) {
		//                setIsJoined(true);
		//                PostProcessSupport.addToPostProcess(this, "setLegend", leg, 1);
		//            } else {
		//                this.setLoadLegend(leg);
		//            }
		//
		//        } catch (XMLException e) {
		//            this.setAvailable(false);
		//            this.orgXMLEntity = xml;
		//        } catch (Exception e) {
		//            this.setAvailable(false);
		//            this.orgXMLEntity = xml;
		//        }


	}


	/**
	 * Sobreimplementación del método toString para que las bases de datos
	 * identifiquen la capa.
	 *
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		/*
		 * Se usa internamente para que la parte de datos identifique de forma
		 * unívoca las tablas
		 */
		String ret = super.toString();

		return "layer" + ret.substring(ret.indexOf('@') + 1);
	}

	public boolean isJoined() {
		return bHasJoin;
	}

	/**
	 * Returns if a layer is spatially indexed
	 *
	 * @return if this layer has the ability to proces spatial queries without
	 *         secuential scans.
	 */
	//    public boolean isSpatiallyIndexed() {
	//        ReadableVectorial source = getSource();
	//        if (source instanceof ISpatialDB)
	//            return true;
	//
	////FIXME azabala
	///*
	// * Esto es muy dudoso, y puede cambiar.
	// * Estoy diciendo que las que no son fichero o no son
	// * BoundedShapes estan indexadas. Esto es mentira, pero
	// * así quien pregunte no querrá generar el indice.
	// * Esta por ver si interesa generar el indice para capas
	// * HSQLDB, WFS, etc.
	// */
	//        if(!(source instanceof VectorialFileAdapter)){
	//            return true;
	//        }
	//        if (!(source.getDriver() instanceof BoundedShapes)) {
	//            return true;
	//        }
	//
	//        if (getISpatialIndex() != null)
	//            return true;
	//        return false;
	//    }

	public void setIsJoined(boolean hasJoin) {
		bHasJoin = hasJoin;
	}

	//    /**
	//     * @return Returns the spatialIndex.
	//     */
	//    public ISpatialIndex getISpatialIndex() {
	//        return spatialIndex;
	//    }
	//    /**
	//     * Sets the spatial index. This could be useful if, for some
	//     * reasons, you want to work with a distinct spatial index
	//     * (for example, a spatial index which could makes nearest
	//     * neighbour querys)
	//     * @param spatialIndex
	//     */
	//    public void setISpatialIndex(ISpatialIndex spatialIndex){
	//        this.spatialIndex = spatialIndex;
	//    }

	public void setEditing(boolean b) throws StartEditionLayerException {
		super.setEditing(b);
		if (b){
			try {
				getFeatureStore().edit();
			} catch (ReadException e) {
				throw new StartEditionLayerException(getName(),e);
			} catch (DataException e) {
				throw new StartEditionLayerException(getName(),e);
			}
		}
		setSpatialCacheEnabled(b);
		callEditionChanged(LayerEvent
				.createEditionChangedEvent(this, "edition"));

	}

	public void clearSpatialCache()
	{
		spatialCache.clearAll();
	}

	public boolean isSpatialCacheEnabled() {
		return spatialCacheEnabled;
	}

	public void setSpatialCacheEnabled(boolean spatialCacheEnabled) {
		this.spatialCacheEnabled = spatialCacheEnabled;
	}

	public SpatialCache getSpatialCache() {
		return spatialCache;
	}

	/**
	 * Siempre es un numero mayor de 1000
	 * @param maxFeatures
	 */
	public void setMaxFeaturesInEditionCache(int maxFeatures) {
		if (maxFeatures > spatialCache.getMaxFeatures()) {
			spatialCache.setMaxFeatures(maxFeatures);
		}

	}

	/**
	 * This method returns a boolean that is used by the FPopMenu
	 * to make visible the properties menu or not. It is visible by
	 * default, and if a later don't have to show this menu only
	 * has to override this method.
	 * @return
	 * If the properties menu is visible (or not)
	 */
	public boolean isPropertiesMenuVisible(){
		return true;
	}

	public void reload() throws ReloadLayerException {
		super.reload();
		try {
			DataManager dataManager=DALLocator.getDataManager();
			DataStoreParameters storeParameters;

			storeParameters = getFeatureStore().getParameters();

			DataStore dataStore=dataManager.createStore(storeParameters);
			setDataStore(dataStore);
			getFeatureStore().refresh();
		} catch (Exception e) {
			throw new ReloadLayerException(getName(),e);
		}
		//        try {
		//            this.source.getDriver().reload();
		//            if (this.getLegend() == null) {
		//                if (this.getRecordset().getDriver() instanceof WithDefaultLegend) {
		//                    WithDefaultLegend aux = (WithDefaultLegend) this.getRecordset().getDriver();
		//                    this.setLegend((IVectorLegend) aux.getDefaultLegend());
		//                    this.setLabelingStrategy(aux.getDefaultLabelingStrategy());
		//                } else {
		//                    this.setLegend(LegendFactory.createSingleSymbolLegend(
		//                            this.getShapeType()));
		//                }
		//            }
		//
		//        } catch (LegendLayerException e) {
		//            this.setAvailable(false);
		//            throw new ReloadLayerException(getName(),e);
		//        } catch (ReadException e) {
		//            this.setAvailable(false);
		//            throw new ReloadLayerException(getName(),e);
		//        }


	}

	protected void setLoadSelection(XMLEntity xml) {
		this.loadSelection = xml;
	}

	protected void setLoadLegend(IVectorLegend legend) {
		this.loadLegend = legend;
	}

	protected void putLoadSelection() throws XMLException {
		//        if (this.loadSelection == null) return;
		//        try {
		//            this.getRecordset().getSelectionSupport().setXMLEntity(this.loadSelection);
		//        } catch (ReadDriverException e) {
		//            throw new XMLException(e);
		//        }
		//        this.loadSelection = null;

	}
	protected void putLoadLegend() throws LegendLayerException {
		if (this.loadLegend == null) {
			return;
		}
		this.setLegend(this.loadLegend);
		this.loadLegend = null;
	}

	protected void cleanLoadOptions() {
		this.loadLegend = null;
		this.loadSelection = null;
	}

	public boolean isWritable() {
		try {
			return getFeatureStore().allowWrite();
		} catch (ReadException e) {
			e.printStackTrace();
		}
		return false;
	}

	public FLayer cloneLayer() throws Exception {
		FLyrVect clonedLayer = new FLyrVect();
		clonedLayer.setDataStore(getDataStore());
		if (isJoined()) {
			clonedLayer.setIsJoined(true);
		}
		clonedLayer.setVisible(isVisible());
		//        clonedLayer.setISpatialIndex(getISpatialIndex());
		clonedLayer.setName(getName());
		clonedLayer.setCoordTrans(getCoordTrans());

		clonedLayer.setLegend((IVectorLegend)getLegend().cloneLegend());

		clonedLayer.setIsLabeled(isLabeled());
		ILabelingStrategy labelingStrategy=getLabelingStrategy();
        if (labelingStrategy!=null) {
			clonedLayer.setLabelingStrategy(labelingStrategy);
		}

		return clonedLayer;
	}


	private boolean isOnePoint(AffineTransform graphicsTransform, ViewPort viewPort, double dpi, CartographicSupport csSym, Geometry geom, int[] xyCoords) {
		return isOnePoint(graphicsTransform, viewPort, geom, xyCoords) && csSym.getCartographicSize(viewPort, dpi, geom) <= 1;
	}

	private boolean isOnePoint(AffineTransform graphicsTransform, ViewPort viewPort, Geometry geom, int[] xyCoords) {
		boolean onePoint = false;
		int type=geom.getType();
		if (type == Geometry.TYPES.NULL) {
			return false;
		}
		if (type!=Geometry.TYPES.POINT && type!=Geometry.TYPES.MULTIPOINT) {

			Envelope geomBounds = geom.getEnvelope();

			ICoordTrans ct = getCoordTrans();

			// Se supone que la geometria ya esta reproyectada
			// if (ct!=null) {
			// // geomBounds = ct.getInverted().convert(geomBounds);
			// geomBounds = geomBounds.convert(ct);
			// }

			double dist1Pixel = viewPort.getDist1pixel();

			onePoint = (geomBounds.getLength(0)  <= dist1Pixel
					&& geomBounds.getLength(1) <= dist1Pixel);

			if (onePoint) {
				// avoid out of range exceptions
				org.gvsig.fmap.geom.primitive.Point p;
				try {
					p = geomManager.createPoint(geomBounds.getMinimum(0), geomBounds.getMinimum(1), SUBTYPES.GEOM2D);
					p.transform(viewPort.getAffineTransform());
					p.transform(graphicsTransform);
					xyCoords[0] = (int) p.getX();
					xyCoords[1] = (int) p.getY();
				} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
					logger.error("Error creating a point", e);
				}

			}

		}
		return onePoint;
	}
	/*
	 * jaume. Stuff from ILabeled.
	 */
	private boolean isLabeled;
	protected ILabelingStrategy strategy;

	public boolean isLabeled() {
		return isLabeled;
	}

	public void setIsLabeled(boolean isLabeled) {
		this.isLabeled = isLabeled;
	}

	public ILabelingStrategy getLabelingStrategy() {
		return strategy;
	}

	public void setLabelingStrategy(ILabelingStrategy strategy) {
		this.strategy = strategy;
		try {
			strategy.setLayer(this);
		} catch (ReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void drawLabels(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale, double dpi) throws ReadException {
		if (strategy!=null && isWithinScale(scale)) {
			strategy.draw(image, g, viewPort, cancel, dpi);
		}
	}

	public void printLabels(Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale,
			PrintAttributes properties) throws ReadException {
		if (strategy != null) {
			strategy.print(g, viewPort, cancel, properties);
		}
	}
	//Métodos para el uso de HyperLinks en capas FLyerVect

	/**
	 * Return true, because a Vectorial Layer supports HyperLink
	 */
	public boolean allowLinks()
	{
		return true;
	}

	/**
	 * Returns an instance of AbstractLinkProperties that contains the information
	 * of the HyperLink
	 * @return Abstra
	 */
	public AbstractLinkProperties getLinkProperties()
	{
		return linkProperties;
	}

	/**
	 * Provides an array with URIs. Returns one URI by geometry that includes the point
	 * in its own geometry limits with a allowed tolerance.
	 * @param layer, the layer
	 * @param point, the point to check that is contained or not in the geometries in the layer
	 * @param tolerance, the tolerance allowed. Allowed margin of error to detect if the  point
	 * 		is contained in some geometries of the layer
	 * @return
	 * @throws ReadException
	 * @throws BehaviorException
	 */
	public URI[] getLink(Point2D point, double tolerance) throws ReadException
	{
		//return linkProperties.getLink(this)
		return linkProperties.getLink(this,point,tolerance);
	}

	public void load() throws LoadLayerException {
		super.load();
	}

	public FeatureStore getFeatureStore() throws ReadException {
		return (FeatureStore)getDataStore();
	}

	public FeatureSet queryByPoint(Point2D mapPoint, double tol, FeatureType featureType) throws DataException {
		GeometryManager manager = GeometryLocator.getGeometryManager();
		org.gvsig.fmap.geom.primitive.Point center;
		try {
			center = (org.gvsig.fmap.geom.primitive.Point)manager.create(TYPES.POINT, SUBTYPES.GEOM2D);
			center.setX(mapPoint.getX());
			center.setY(mapPoint.getY());
			Circle circle = (Circle)manager.create(TYPES.CIRCLE, SUBTYPES.GEOM2D);
			circle.setPoints(center, tol);
			return queryByGeometry(circle, featureType);
		} catch (org.gvsig.fmap.geom.exception.CreateGeometryException e) {
			throw new CreateGeometryException(TYPES.CIRCLE, SUBTYPES.GEOM2D, e);
		}
	}


	public FeatureSet queryByGeometry(Geometry geom, FeatureType featureType) throws DataException {
		FeatureQuery featureQuery=featureStore.createFeatureQuery();
		String geomName=featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName();
		featureQuery.setFeatureType(featureType);
		IntersectsGeometryEvaluator iee=new IntersectsGeometryEvaluator(geom,getMapContext().getViewPort().getProjection(),featureStore.getDefaultFeatureType(),geomName);
		featureQuery.setFilter(iee);
		return getFeatureStore().getFeatureSet(featureQuery);

	}

	public FeatureSet queryByEnvelope(Envelope envelope, FeatureType featureType)
	throws DataException {
		return queryByEnvelope(envelope, featureType, null);
	}

	public FeatureSet queryByEnvelope(Envelope envelope, FeatureType featureType, String[] names)
	throws DataException {
		FeatureQuery featureQuery=featureStore.createFeatureQuery();
		if (names==null){
			featureQuery.setFeatureType(featureType);
		}else{
			featureQuery.setAttributeNames(names);
			featureQuery.setFeatureTypeId(featureType.getId());
		}
		String geomName=featureStore.getDefaultFeatureType().getDefaultGeometryAttributeName();
		ContainsEnvelopeEvaluator iee=new ContainsEnvelopeEvaluator(envelope,getMapContext().getViewPort().getProjection(),featureStore.getDefaultFeatureType(),geomName);
		featureQuery.setFilter(iee);
		return getFeatureStore().getFeatureSet(featureQuery);

	}
	public XMLItem[] getInfo(Point p, double tolerance, Cancellable cancel) throws LoadLayerException, DataException {
		Point2D pReal = this.getMapContext().getViewPort().toMapPoint(p);
		FeatureSet featureCollection=null;
		try {
			featureCollection = queryByPoint(pReal, tolerance, getFeatureStore().getDefaultFeatureType());
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VectorialXMLItem[] item = new VectorialXMLItem[1];
		item[0] = new VectorialXMLItem(featureCollection, this);

		return item;
	}

	public void legendCleared(LegendClearEvent event) {
		// this.updateDrawVersion(); TODO
		LegendChangedEvent e = LegendChangedEvent.createLegendChangedEvent(
				legend, legend);
		this.callLegendChanged(e);
	}

	public boolean symbolChanged(SymbolLegendEvent e) {
		LegendChangedEvent ev = LegendChangedEvent.createLegendChangedEvent(
				legend, legend);
		this.callLegendChanged(ev);
		return true;
	}

	public void update(Observable observable, Object notification) {
		if (observable.equals(this.featureStore)) {
			if (notification instanceof FeatureStoreNotification) {
				FeatureStoreNotification event = (FeatureStoreNotification) notification;
				if (event.getType() == FeatureStoreNotification.AFTER_CANCELEDITING
						|| event.getType() == FeatureStoreNotification.AFTER_DELETE
						|| event.getType() == FeatureStoreNotification.AFTER_UNDO
						|| event.getType() == FeatureStoreNotification.AFTER_REDO
						|| event.getType() == FeatureStoreNotification.AFTER_REFRESH
						|| event.getType() == FeatureStoreNotification.AFTER_UPDATE
						|| event.getType() == FeatureStoreNotification.AFTER_UPDATE_TYPE
						|| event.getType() == FeatureStoreNotification.SELECTION_CHANGE
						|| event.getType() == FeatureStoreNotification.AFTER_INSERT) {
					this.updateDrawVersion();

				} else if (event.getType() == FeatureStoreNotification.AFTER_FINISHEDITING
						|| event.getType() == FeatureStoreNotification.TRANSFORM_CHANGE
						|| event.getType() == FeatureStoreNotification.RESOURCE_CHANGED) {
					this.setAvailable(false);

					//					try {
					//						reload();
					//					} catch (ReloadLayerException e) {
					//						this.setAvailable(false);
					//					}
				}

			}

		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
	 */
	public Set getMetadataChildren() {
		Set ret = new TreeSet();
		ret.add(this.featureStore);
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataID()
	 */
	public Object getMetadataID() {
		return "Layer(" + this.getName() + "):"
		+ this.featureStore.getMetadataID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataName()
	 */
	public String getMetadataName() {
		return "Layer '" + this.getName() + "':"
		+ this.featureStore.getMetadataName();
	}

	public GeometryType getTypeVectorLayer() throws DataException, LocatorException, GeometryTypeNotSupportedException, GeometryTypeNotValidException {
		// FIXME Esto deberia de pedirse a FType!!!!
		FeatureStore fs = this.getFeatureStore();
		FeatureType fType = fs.getDefaultFeatureType();
		FeatureAttributeDescriptor attr = fType.getAttributeDescriptor(fType
				.getDefaultGeometryAttributeIndex());
		GeometryType geomType = GeometryLocator.getGeometryManager()
				.getGeometryType(attr.getGeometryType(),
						attr.getGeometrySubType());
		return geomType;
	}

}