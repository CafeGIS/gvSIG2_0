package org.gvsig.gvsig3d.cacheservices;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.cresques.cts.IProjection;
import org.gvsig.cacheservice.CacheService;
import org.gvsig.fmap.geom.operation.GeometryOperationException;
import org.gvsig.fmap.geom.operation.GeometryOperationNotSupportedException;
import org.gvsig.fmap.geom.primitive.AbstractPrimitive;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.geometries3D.MultiSolid;
import org.gvsig.gvsig3d.drivers.GvsigDriverOSG;
import org.gvsig.gvsig3d.listener.EditorListener;
import org.gvsig.operations3D.Draw3DMultiSolid;
import org.gvsig.operations3D.context.Draw3DContext;

import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.PositionAttitudeTransform;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.manipulator.EditionManager;
import org.gvsig.osgvp.manipulator.ManipulatorHandler;
import org.gvsig.osgvp.manipulator.RemoveAllSelectionCommand;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.IViewerContainer;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

public class OSGCacheService extends CacheService {

	private IViewerContainer _canvas3D;

	private PlanetViewer _planetViewer;

	private Rectangle2D _lyrExtentRect;

	private FLayer _layer;

	// vector caching for libJOSG
	private Group _layerNode;

	private boolean newLayerOSG = false;

	private EditionManager editionManager;

	private ManipulatorHandler manipulatorHandler;

	private EditorListener editionListener;

	private RemoveAllSelectionCommand command;

	private String filePath;

	private static Logger logger = Logger.getLogger(OSGCacheService.class
			.getName());

	// private ISymbol _currentSymbol;

	public OSGCacheService(IViewerContainer canvas3D, Planet planet,
			String name, FLayer layer, IProjection viewProj) {
		super(planet.getPlanetName(), name);

		// getNames();

		_canvas3D = canvas3D;
		_planetViewer = (PlanetViewer) canvas3D.getOSGViewer();
		_layer = layer;
		_layerNode = new Group();
		_layerNode.setNodeName("layer3DOSG");
		try {
			editionManager = new EditionManager();
			_layerNode.addChild(editionManager);
			manipulatorHandler = new ManipulatorHandler();
			manipulatorHandler.setActive(false);

			editionListener = new EditorListener(editionManager,
					manipulatorHandler, layer, _canvas3D, _planetViewer);

			_canvas3D.getOSGViewer().addEventHandler(manipulatorHandler);

		} catch (NodeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

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

	}

	public Group getLayerNode() {

		if (!Layer3DProps.getLayer3DProps(this._layer).isEditing()) {
			try {
				return (Group) editionManager.getScene();
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {

			try {
				return editionManager.getTransformedScene();
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return _layerNode;
	}

	// uses a strategy to get all features from layer and their symbols
	// and add them to the planet

	public void AddFeaturesToPlanet() {

		boolean newLayer = Layer3DProps.getLayer3DProps(_layer).isNewLayerOSG();

		if (!newLayer) {
			GvsigDriverOSG osgD = (GvsigDriverOSG) ((FLyrVect) _layer)
					.getSource().getDriver();
			try {
				File file = osgD.getFile();
				filePath = file.getAbsolutePath();
				Node n = null;

				try {
					n = osgDB.readNodeFile(file.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MultiGeometry root = osgD.getOSGDriver().getRootFeature();
				// Insert the geometry in the root node of the graph.
				Group g = new Group(n.getCPtr());
				// buildGeometry(root, g);
				// g.addChild(n);
				for (int i = 0; i < g.getNumChildren(); i++) {

					editionManager.setScene(g.getChild(i));

				}

				// _layerNode.addChild(n);
				// startEditing();
				// _layerNode.addChild(g);
			} catch (NodeException e) {
				logger
						.error(
								"Command: "
										+ "Error loading OSG file. File must be generated with gvSIG",
								e);

			}
		}
		this._canvas3D.repaint();
	}

	public void RefreshFeaturesToPlanet() {
		this.DeleteFeaturesToPlanet();
		this.AddFeaturesToPlanet();
	}

	public void DeleteFeaturesToPlanet() {
		try {
			endEditing();
			_planetViewer.removeSpecialNode(_layerNode);
		} catch (NodeException e) {
			logger.error("Command: "
					+ "Error removing new child node to the special node.", e);
		}
	}

	public void refreshFeaturesToPlanet(boolean visible) {
		_layerNode.setNodeMask(visible ? 0xffffffff : 0x00000000);
	}

	public void AddGeometryToLayer(MultiGeometry multiGeometry, Vec3 position,
			Vec3 rotation, Vec3 scale) {
		Group group = new Group();
		group.setNodeName("GROUP-PAT");
		PositionAttitudeTransform posAttTrasn = null;
		try {
			posAttTrasn = new PositionAttitudeTransform();
			posAttTrasn.setPosition(position);
			posAttTrasn.setScale(scale);
			posAttTrasn.setNodeName("PAT");
			// posAttTrasn.setAttitude((float) rotation.x(), new Vec3(1,0,0));
			// posAttTrasn.setAttitude((float) rotation.y(), new Vec3(0,1,0));
			// posAttTrasn.setAttitude((float) rotation.z(), new Vec3(0,0,1));
			buildGeometry(multiGeometry, group);
			posAttTrasn.addChild(group);

			Layer3DProps props3D = Layer3DProps.getLayer3DProps(this._layer);
			// if (props3D.isEditing()){
			editionManager.setScene(posAttTrasn);
			// }else{
			// _layerNode.addChild(posAttTrasn);
			// }
			// _layerNode.addChild(group);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void buildGeometry(AbstractPrimitive geom, Group group)
			throws NodeException {

		int i;

		if (geom instanceof MultiGeometry) {
			MultiGeometry multiGeometry = (MultiGeometry) geom;
			for (i = 0; i < multiGeometry.getGeometries().size(); i++) {

				Group child = new Group();
				group.addChild(child);
				buildGeometry(multiGeometry.getGeometries().get(i), child);
			}
		} else if (geom instanceof MultiSolid) {
			// Getting the geometry
			MultiSolid multiSolid = (MultiSolid) geom;
			// Creating the context and adding parameters
			Draw3DContext ctx3D = new Draw3DContext();
			ctx3D.setGroup(group);
			// Creating the drawing operation
			Draw3DMultiSolid d3DMultiSolid = new Draw3DMultiSolid();

			try {
				// Invoking the operation for the multisolid
				multiSolid.invokeOperation(d3DMultiSolid.getOperationIndex(),
						ctx3D);
			} catch (GeometryOperationNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeometryOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void startEditing() {

		Layer3DProps props3D = Layer3DProps.getLayer3DProps(this._layer);
		if ((props3D.getType() == Layer3DProps.layer3DOSG)
				&& (!props3D.isEditing())) {
			props3D.setEditing(true);
			props3D.setNewLayerOSG(true);
			JOptionPane.showMessageDialog(null,
					"La capa ha sido puesta en edicion");

			// editionManager = new EditionManager();
			// editionManager.setNodeName("EDITIONMANAGER");
			// editionManager.getChild(0).setNodeName("GROUP");
			// for (int i = 0; i < _layerNode.getNumChildren(); i++) {
			// Node node = _layerNode.getChild(i);
			// editionManager.setScene(node);
			// _layerNode.removeChild(node);
			// }
			// _layerNode.addChild(editionManager);
			// _layerNode.setNodeName("LAYERNODE");
			// Group parent = _layerNode.getParent(0);
			// parent.removeChild(_layerNode);
			// parent.addChild(editionManager);

			// ManipulatorHandler manipulatorHandler = new ManipulatorHandler();
			// manipulatorHandler.setActive(true);
			// public void changeDragger(String draggerType)
			// editionManager.changeDragger(DraggerType.ROTATE_SPHERE_DRAGGER
			// );
			// editionManager.changeDragger(DraggerType.TABBOX_DRAGGER);
			// editionManager.changeDragger(DraggerType.TRANSLATE_AXIS_DRAGGER
			// );

		}
	}

	public void endEditing() {

		Layer3DProps props3D = Layer3DProps.getLayer3DProps(this._layer);
		if ((props3D.getType() == Layer3DProps.layer3DOSG)
				&& (props3D.isEditing())) {
			props3D.setEditing(false);

			if (props3D.isNewLayerOSG())
				JOptionPane
						.showMessageDialog(null,
								"La capa ha sido modificada, recuerde salvar los cambios.");

			command = new RemoveAllSelectionCommand(editionManager);
			command.execute();

			// Group scene = editionManager.getTransformedScene();

			// for (int i = 0; i < _layerNode.getNumChildren(); i++) {
			// Node node = _layerNode.getChild(i);
			// _layerNode.removeChild(node);
			//					
			// }
			// _layerNode.removeChildren();

			// for (int i =0; i < scene.getNumChildren(); i++){
			//					
			// _layerNode.addChild(scene.getChild(i));
			//					
			// }
			//				

			// Group parent = _layerNode.getParent(0);
			// parent.removeChild(_layerNode);
			// parent.addChild(scene);
			// public void changeDragger(String draggerType)
			// editionManager.changeDragger(DraggerType.ROTATE_SPHERE_DRAGGER
			// );
			// editionManager.changeDragger(DraggerType.TABBOX_DRAGGER);
			// editionManager.changeDragger(DraggerType.TRANSLATE_AXIS_DRAGGER
			// );

			manipulatorHandler.setActive(false);

		}

	}

	public EditionManager getEditionManager() {
		return editionManager;
	}

	public void setEditionManager(EditionManager editionManager) {
		this.editionManager = editionManager;
	}

	public String getLayerPath() {

		return filePath;
	}

}
