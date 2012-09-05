package org.gvsig.gvsig3d.cacheservices;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;
import org.cresques.px.Extent;
import org.gvsig.cacheservice.CacheService;
import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.gvsig3d.labels3D.ILabel3D;
import org.gvsig.gvsig3d.labels3D.SimpleLabel3D;
import org.gvsig.gvsig3d.simbology3D.Line3DFeature;
import org.gvsig.gvsig3d.simbology3D.Point3DFeature;
import org.gvsig.gvsig3d.simbology3D.Polygon3DFeature;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.PixelPoint;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.IViewerContainer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.NumericValue;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ILabelable;
import com.iver.cit.gvsig.fmap.core.symbols.ISymbol;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ISpatialDB;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.fmap.rendering.IClassifiedVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.IVectorLegend;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.ILabelingStrategy;

public class VectorCacheService extends CacheService {

	private IViewerContainer _canvas3D;

	private PlanetViewer _planetViewer;

	private Planet _planet;

	private Extent _extent;

	private Rectangle2D _lyrExtentRect;

	private FLayer _layer;

	private IProjection _viewProj;

	// vector caching for libJOSG
	private Group _layerNode;

	private Node _currentNode;

	private int _currentGeomType;

	private List<ILabel3D> m_labels;

	private float heigth = 5000;

	private boolean inPixels = true;

	private int fontSize = 14;

	private int n = 0;

	// Create a hash table

	private int option;

	private boolean primera = true;

	private static Logger logger = Logger.getLogger(VectorCacheService.class
			.getName());

	// private ISymbol _currentSymbol;

	public VectorCacheService(IViewerContainer canvas3D, Planet planet,
			String name, FLayer layer, IProjection viewProj) {
		super(planet.getPlanetName(), name);

		// getNames();

		_canvas3D = canvas3D;
		_planetViewer = (PlanetViewer) canvas3D.getOSGViewer();
		_planet = planet;
		_layer = layer;
		_viewProj = viewProj;
		_layerNode = new Group();
		_layerNode.setNodeName("layer3DVector");
		try {
			_planetViewer.addSpecialNode(_layerNode);
		} catch (NodeException e1) {
			logger.error("Command: "
					+ "Error adding new child node to the special node.", e1);
		}

		int cacheType = CacheService.GLOBAL;
		if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC)
			cacheType += SPHERIC;
		else
			cacheType += PLANE;
		setCacheType(cacheType);

		try {
			_lyrExtentRect = _layer.getFullExtent();
		} catch (ExpansionFileReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_extent = new Extent(_lyrExtentRect);

	}

	// uses a strategy to get all features from layer and their symbols
	// and add them to the planet

	public void AddFeaturesToPlanet() {

		try {
			newDraw();
			getNames();
			_canvas3D.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void RefreshFeaturesToPlanet() {
		this.DeleteFeaturesToPlanet();
		_layerNode = new Group();
		try {
			_planetViewer.addSpecialNode(_layerNode);
		} catch (NodeException e1) {
			logger.error("Command: "
					+ "Error adding new child node to the special node.", e1);
		}
		_currentGeomType = -1;

		try {
			newDraw();
			getNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DeleteFeaturesToPlanet() {
//		_layerNode.dispose();
//		_layerNode = null;
//		System.gc();
		try {
			_planetViewer.removeSpecialNode(_layerNode);
		} catch (NodeException e) {
			logger.error("Command: "
					+ "Error removing new child node to the special node.", e);
		}
	}

	public void refreshFeaturesToPlanet(boolean visible) {
		// _canvas3D.removeSpecialNode(_layerNode);
		//_layerNode.setEnabledNode(visible);
		_layerNode.setNodeMask(visible?0xffffffff:0x00000000);
	}

	// FeatureVisitor methods

	private void startGeometry(int geomType, ISymbol symb) {

		// TODO: create new node if some properties change in symbol (like point
		// size)

		// if ((geomType != _currentGeomType) || ((geomType & FShape.POINT) ==
		// FShape.POINT)) {
		if (geomType != _currentGeomType) {
			_currentGeomType = geomType;

			if ((geomType & FShape.Z) == FShape.Z)
				geomType = geomType - FShape.Z;

			if (((geomType & FShape.POINT) == FShape.POINT)) {
				Group g = new Group();
				PixelPoint point = (PixelPoint) FeatureFactory.insertPointS(
						new Vec3(0.0, 0.0, 0.0), new Vec4(1.0f, 1.0f, 1.0f,
								1.0f), 100);
				_currentNode = point;
				try {
					_layerNode.addChild(g);
				} catch (NodeException e) {
					logger.error("Command: " + "Error adding new child node.",
							e);
				}
				try {
					g.addChild(point);
				} catch (NodeException e) {
					logger.error("Command: " + "Error adding new child node.",
							e);
				}
			} else if (((geomType & FShape.LINE) == FShape.LINE)) {
				;
			} else if (((geomType & FShape.POLYGON) == FShape.POLYGON)) {
				;
			}
			// switch (geomType) {
			//
			// case FShape.POINT:
			// // Point point = (Point) VectorTest.insertPointS(new Vec3(0.0,
			// // 0.0, 0.0), new Vec4(1.0f, 1.0f, 1.0f, 1.0f), 100);
			// Group g = new Group();
			// _currentNode = g;
			// _layerNode.addChild(g);
			// break;
			// case FShape.LINE:
			// // No set up
			// break;
			// case FShape.POLYGON:
			// // No set up
			// break;
			//
			// }

		}
	}

	private void getNames() {

		createLabels(_layer);
		if ((m_labels != null) && (m_labels.size() > 0))
			try {
				_layerNode.addChild(FeatureFactory.insertLabels(this.m_labels,
						this._planet));
			} catch (NodeException e) {
				logger.error("Command: " + "Error adding new child node.", e);
			}
		
		
		

		// if (_layer instanceof FLyrVect) {
		// FLyrVect flyrVect = (FLyrVect) _layer;
		// // REVISAR COMO SE GENERAN LOS LABELS CON LOS ITERADORES
		// flyrVect.isLabeled();
		// if (m_labels != null)
		// if (((IVectorLegend) flyrVect.getLegend()).getLabelField() !=
		// null)
		//						
		// } catch (DrivierException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void createLabels(FLayer lyrVect) {

		// If this layer had enable the labels, we would need to get its.
		if (lyrVect instanceof ILabelable && ((FLyrVect) lyrVect).isLabeled()) {
			// Cleaning the m_label list
			m_labels = new ArrayList<ILabel3D>();
			// Casting the layer to the interface
			ILabelable lyrLable = (ILabelable) lyrVect;
			// Getting the strategy
			AttrInTableLabelingStrategy strategy = (AttrInTableLabelingStrategy) lyrLable
					.getLabelingStrategy();
			// Getting the fields and Id to get after the values using the
			// recorset.
			try {
				// Getting fields
				// Text
				int textFieldId = strategy.getTextFieldId();
				// String textField = strategy.getTextField();

				// Height
				String heightField = strategy.getHeightField();
				int heightFieldId = strategy.getHeightFieldId();
				
				

				// Rotation
				String rotationField = strategy.getRotationField();
				int rotationFieldId = strategy.getRotationFieldId();

				// Unit
				int unit = strategy.getUnit();

				// Getting the class and the symbol to get all his properties.
//				ILabelingMethod method = strategy.getLabelingMethod();
//				LabelClass lClass = method.getLabelClasses()[0];
//				SimpleTextSymbol sym = (SimpleTextSymbol) lClass
//						.getTextSymbol();

				// Getting the recordset
				ReadableVectorial source = ((FLyrVect) lyrVect).getSource();
				SelectableDataSource recordSet = source.getRecordset();

				
				
				// Iterate over all the rows.
				for (int i = 0; i < recordSet.getRowCount(); i++) {

					// Getting the geometry from the source
					IGeometry geom = source.getShape(i);

					// Getting the row with all the fields relactionship.
					Value[] vv = recordSet.getRow(i);

					Value texto = vv[textFieldId];
					double size = 0 ;
					if (strategy.usesFixedSize()){
						size = strategy.getFixedSize();
					}else{
						NumericValue altura = (NumericValue) vv[heightFieldId];
						size = altura.doubleValue();
					}

					SimpleLabel3D simpleLabel3DAux = new SimpleLabel3D(geom
							.createLabels(0, true)[0]);

					Vec3 position = simpleLabel3DAux.getPosition();
					Vec3 newPosition = new Vec3(position.x(), position.y(),
							heigth + 50);
					simpleLabel3DAux.setPosition(newPosition);

					simpleLabel3DAux.setTextField(texto.toString());
					
					simpleLabel3DAux.setHeightField(size);
					
					simpleLabel3DAux.setStrategy(strategy);

					Value rotacion;
					if (rotationField != null)
						rotacion = vv[rotationFieldId];

					simpleLabel3DAux.setUnits(unit);

					m_labels.add(simpleLabel3DAux);
				}

			} catch (ReadDriverException e) {
				e.printStackTrace();
			}
		}

	}

	public void newDraw() {
		FLyrVect fLyrVect = (FLyrVect) _layer;
		try {

			// String[] aux;
			// ArrayList fieldList = new ArrayList();
			// if (fLyrVect.isLabeled()) {
			// aux = fLyrVect.getLabelingStrategy().getUsedFields();
			// aux =
			// fLyrVect.getLabelingStrategy().getPlacementConstraints().
			// getLocationsFor(geom,
			// labelShape, exclusionZone);
			// for (int i = 0; i < aux.length; i++) {
			// fieldList.add(aux[i]);
			// }
			// }
			IVectorLegend legend = (IVectorLegend) fLyrVect.getLegend();
			ArrayList<String> fieldList = new ArrayList<String>();

			// fields from legendn
			String[] aux = null;

			if (legend instanceof IClassifiedVectorLegend) {
				aux = ((IClassifiedVectorLegend) legend)
						.getClassifyingFieldNames();
				for (int i = 0; i < aux.length; i++) {
					fieldList.add(aux[i]);
				}

			}

			FLyrVect lyrVect = null;
			if (_layer.getClass().equals(FLyrVect.class)) {
				lyrVect = (FLyrVect) _layer;
			}
			if (lyrVect != null) {
				if (lyrVect.isLabeled()) {
					aux = lyrVect.getLabelingStrategy().getUsedFields();
					for (int i = 0; i < aux.length; i++) {
						fieldList.add(aux[i]);
					}
				}
			}
			ILabelingStrategy labelStrategy = fLyrVect.getLabelingStrategy();
			if (labelStrategy != null) {
				String[] usetFields = labelStrategy.getUsedFields();
			}

			// IFeatureIterator it = fLyrVect.getSource().getFeatureIterator(
			// fieldList.toArray(new String[0]), _viewProj);
			ReadableVectorial source = fLyrVect.getSource();
			if (source != null) {
				IFeatureIterator it = fLyrVect.getSource().getFeatureIterator(
						fieldList.toArray(new String[fieldList.size()]),
						_viewProj);

				// Get the iterator over the visible features
				// IFeatureIterator ite =
				// fLyrVect.getSource().getFeatureIterator(
				// viewPort.getAdjustedExtent(),
				// fieldList.toArray(new String[fieldList.size()]),
				// viewPort.getProjection(), true);
				try {
					while (it.hasNext()) {
						IFeature feat = it.next();
						ISymbol sym = legend.getSymbolByFeature(feat);
						if (sym == null)
							sym = legend.getDefaultSymbol();
						IGeometry geom = feat.getGeometry();

						ReadableVectorial rv = fLyrVect.getSource();
						int selectionIndex = -1;
						if (rv instanceof ISpatialDB) {
							selectionIndex = ((ISpatialDB) rv)
									.getRowIndexByFID(feat);
						} else {
							selectionIndex = Integer.parseInt(feat.getID());
						}
						if (selectionIndex != -1) {
							if (fLyrVect.getRecordset().getSelectionSupport()
									.isSelected(selectionIndex)) {
								sym = sym.getSymbolForSelection();
							}
						}

						int geomType = geom.getGeometryType();
						// Line Type
						Layer3DProps props3D = Layer3DProps
								.getLayer3DProps(_layer);
						if ((geomType & FShape.LINE) == FShape.LINE) {
							Line3DFeature l3D = new Line3DFeature(sym, geom);
							l3D.setPlanet(this._planet);
							l3D.setHeigth(props3D.getHeigth());
							this._layerNode = l3D.Draw(this._layerNode);
						}
						// Polygon type
						if ((geomType & FShape.POLYGON) == FShape.POLYGON) {
							Polygon3DFeature poly3D = new Polygon3DFeature(sym,
									geom);
							poly3D.setPlanet(this._planet);
							poly3D.setHeigth(props3D.getHeigth());
							// StateSet s= new StateSet();
							// s.setPolygonOffset(1.0f,1.0f);
							// _layerNode.setStateSet(s);
							this._layerNode = poly3D.Draw(_layerNode);
						}
						// Point type
						if ((geomType & FShape.POINT) == FShape.POINT) {

							Point3DFeature point3D = new Point3DFeature(sym,
									geom);
							point3D.setPlanet(this._planet);
							point3D.setHeigth(props3D.getHeigth());
							this._layerNode = point3D.Draw(_layerNode);
						}

					}
				} catch (ExpansionFileReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		} catch (ReadDriverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

	}

	// public void p(){
	// Matrix posMat = Matrix.translate(point2);
	// Quat quat = new Quat();
	// point2.normalize();
	// quat.makeRotate(new Vec3(0, 0, 1), point2);
	// Matrix rotMat = Matrix.rotate(quat);
	// Matrix scaleMat = Matrix.scale(1000000, 1000000, 1000000);
	// MatrixTransform matNode = new MatrixTransform();
	// matNode.setMatrix(rotMat.prod(scaleMat.prod(posMat)));
	// matNode.addChild(_flagNode);
	// ((PlanetViewer) _canvas3d.getOSGViewer()).addSpecialNode(matNode);
	// }

}
