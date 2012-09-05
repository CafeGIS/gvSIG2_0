package org.gvsig.gvsig3d.map3d;

import java.awt.geom.Point2D;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.gui.FeatureFactory;
import org.gvsig.osgvp.core.osg.AutoTransform;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.PositionAttitudeTransform;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.core.osgdb.osgDB;
import org.gvsig.osgvp.exceptions.InvalidValueException;
import org.gvsig.osgvp.exceptions.node.ChildIndexOutOfBoundsExceptions;
import org.gvsig.osgvp.exceptions.node.LoadNodeException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.Text;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.cit.gvsig.fmap.core.v02.FLabel;
import com.iver.cit.gvsig.fmap.layers.GraphicLayer;
import com.iver.cit.gvsig.fmap.rendering.FGraphic;
import com.iver.cit.gvsig.fmap.rendering.FGraphicLabel;

public class GraphicLayer3D extends GraphicLayer {

	private Group graphicNode;

	private Planet planet;

	private PlanetViewer planetViewer;

	private static Logger logger = Logger.getLogger(GraphicLayer3D.class
			.getName());

	public GraphicLayer3D(PlanetViewer planetViewer, Planet planet) {
		super();
		this.planetViewer = planetViewer;
		this.graphicNode = new Group();
		this.graphicNode.setNodeName("graphicLayer");
		try {
			this.planetViewer.addSpecialNode(this.graphicNode);
//			this.graphicNode = planetViewer.getSpecialNodes()(0);
		} catch (ChildIndexOutOfBoundsExceptions e) {
			logger.error("Command: " + "Error Child index out of bound.", e);
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.planet = planet;
	}

	public void clearAllGraphics() {
		// codigo de borrado de busquedas
		Group grupo = null;
//		try {
//			grupo = (Group) planetViewer.getSpecialNode(0);
//		} catch (ChildIndexOutOfBoundsExceptions e) {
//			logger.error("Command: " + "Error child index out of bound.", e);
//		}
		grupo = this.graphicNode;

		while (grupo.getNumChildren() > 0) {
			Node node;
			try {
				node = grupo.getChild(0);
				grupo.removeChild(node);
			} catch (ChildIndexOutOfBoundsExceptions e) {
				logger
						.error("Command: " + "Error Child index out of bound.",
								e);
			} catch (NodeException e) {
				logger.error("Command: " + "Error removing Child from group.",
						e);
			}
		}
	}

	public void addGraphic(FGraphic g) {
		// a�adir la etiqueta aki

		FGraphicLabel label = (FGraphicLabel) g;
		int scale = 15;
		FLabel lab = label.getLabel();
		Point2D p = lab.getOrig();

		System.out.println("etiqueta ::: " + label.getLabel().getString());

		// Variable declaration
		Vec3 posI = null, posE = null, posT = null;
		Vec3 posIni = null, posEnd = null, posText = null, pos1 = null, pos2 = null;

		// search for Gazetteer
		// There are two gazetteer modes SPHERICAL and PLANE mode
		if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC) {
			// For spheriacl mode

			// Geodesical coordinates
			posI = new Vec3(p.getY(), p.getX(), 0);
			posE = new Vec3(p.getY(), p.getX(), 10000);
			posT = new Vec3(p.getY(), p.getX(), scale); // Text position

			// To convert coordinate to cartesian
			posIni = planet.convertLatLongHeightToXYZ(posI);
			posEnd = planet.convertLatLongHeightToXYZ(posE);

			posText = planet.convertLatLongHeightToXYZ(posT);

		} else if (planet.getCoordinateSystemType() == Planet.CoordinateSystemType.PROJECTED) {

			// For plane mode
			posIni = new Vec3(p.getX(), p.getY(), 0);
			posText = new Vec3(p.getX(), p.getY(), scale); // Text position
		}
		// To display text in gazetteer
		Text text = (Text) FeatureFactory.insertTextoS(label.getLabel()
				.getString(), posText, new Vec4(1.0, 0.0, 0.0, 1.0), 30, true);

		Group grupo = null;
//		try {
//			grupo = (Group) planetViewer.getSpecialNode(0);
//		} catch (ChildIndexOutOfBoundsExceptions e) {
//			logger.error("Command: " + "Error Child index out of bound.", e);
//		}
		grupo = this.graphicNode;
		String name = ResourcesFactory.getResourcePath("flag.ive");
		System.out.println(name);
		String path = ResourcesFactory.getResourcesPath();
		System.out.println(path);
		Node flag = null;
		try {
			flag = osgDB.readNodeFile(name);
		} catch (LoadNodeException e) {
			logger.error("Command: " + "Error reading file from system.", e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Con autotransform
		AutoTransform trans = null;
		PositionAttitudeTransform pat = null;
		try {
			trans = new AutoTransform();
			pat = new PositionAttitudeTransform();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			trans.addChild(pat);
			pat.addChild(flag);
		} catch (NodeException e) {
			logger.error("Command: " + "Error adding new child.", e);
		}
//		int scale = (int) ((20 / 4.5) * 1000);
		
//		trans.setScale(new Vec3(scale, scale, scale));
		pat.setScale(new Vec3(scale, scale, scale));
		trans.setPosition(posIni);
		try {
			trans
					.setAutoRotateMode(AutoTransform.AutoRotateMode.ROTATE_TO_CAMERA);
			trans.setAutoScaleToScreen(true);
		} catch (InvalidValueException e) {
			logger.error("Command: " + "Error setting new camera property.", e);
		}

		// Add nodes to graph
		// grupo.setNodeName("gazetteer");
		try {
			grupo.addChild(trans);
			grupo.addChild(text);
		} catch (NodeException e) {
			logger.error("Command: " + "Error adding new child.", e);
		}

	}

//	public Node getEspecialNode() {
//		return graphicNode;
//	}
//
//	public void setEspecialNode(Node especialNode) {
//		this.graphicNode = especialNode;
//	}

}
