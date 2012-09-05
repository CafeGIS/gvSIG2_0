package org.gvsig.gvsig3d.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.gvsig.geometries3D.PrimitiveSet.Mode;
import org.gvsig.gvsig3d.labels3D.ILabel3D;
import org.gvsig.gvsig3d.labels3D.SimpleLabel3D;
import org.gvsig.gvsig3d.utils.Punto3D;
import org.gvsig.osgvp.core.osg.AutoTransform;
import org.gvsig.osgvp.core.osg.Geode;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.core.osgutil.Optimizer;
import org.gvsig.osgvp.core.osgutil.Optimizer.OptimizationOptions;
import org.gvsig.osgvp.exceptions.InvalidValueException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.PixelPoint;
import org.gvsig.osgvp.features.PointExtruder;
import org.gvsig.osgvp.features.Polygon;
import org.gvsig.osgvp.features.PolygonExtruder;
import org.gvsig.osgvp.features.Polyline;
import org.gvsig.osgvp.features.PolylineExtruder;
import org.gvsig.osgvp.features.QuadPoint;
import org.gvsig.osgvp.features.TessellablePolygon;
import org.gvsig.osgvp.features.Text;
import org.gvsig.osgvp.planets.Planet;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.cit.gvsig.fmap.core.v02.FLabel;
import com.iver.cit.gvsig.fmap.rendering.styling.labeling.AttrInTableLabelingStrategy;

public class FeatureFactory extends Group {

	private static double radius;

	static Planet planet;
	
	private static Logger logger = Logger.getLogger(Hud.class.getName());

	public FeatureFactory(double ra, Planet m_planet) {
		super();
		radius = ra;
		// this.setNodeName("VECTOR");
		planet = m_planet;
		init();
	}

	private void init() {

		// this.addChild(insertTexto("Polo norte", poloNorte));
		// this.addChild(insertTexto("MADRID", valenciaCar));

		// Vec3 posIni = new Vec3(0, 0, 0);
		// Vec3 posEnd = new Vec3(0, 0, 100000);

		// Polyline line = (Polyline) VectorTest.insertLineS(posIni, posEnd,
		// new Vec4(1.0, 1.0, 1.0, 1.0), 0);
		// if (planet.getType() == PlanetType.PLANE_MODE)
		// this.addChild(line);

	}

	// public Node insertTexto(String texto, Vec3 posicion) {
	// // A�adiendo texto
	// Text text = new Text();
	// // text.setNodeName(texto);
	// text.setText(texto);
	// text.setCharacterSize(32);
	//
	// text.setAxisAlignment(Text.AxisAlignment.SCREEN);
	// text.setAlignment(Text.AlignmentType.CENTER_CENTER);
	// text.setCharacterSizeMode(Text.CharacterSizeMode.SCREEN_COORDS);
	// text.setPosition((float) posicion.x(), (float) posicion.y(),
	// (float) posicion.z());
	//
	// if (ResourcesFactory.exitsResouce("arial.ttf"))
	// text.setFont(ResourcesFactory.getResourcePath("arial.ttf"));
	// else
	// text.setFont("arial.ttf");
	// text.setAutoRotateToScreen(true);
	//		
	//		
	// Optimizer opt = new Optimizer();
	// opt.optimizeOptions(text, OptimizationOptions.SPATIALIZE_GROUPS);
	//		
	// return text;
	// }
	// /**
	// * this method create a new 3D text. With a value and position. And insert
	// a
	// * point under 3D text
	// *
	// * @param texto
	// * text that is visible in 3D
	// * @param position
	// * Position of 3D text in cartesian coordinates
	// * @return New node
	// */
	// public void insertText(String texto, Vec3 posicion) {
	// posicion.setZ(posicion.z() + radius);
	// Vec3 e = planet.convertLatLongHeightToXYZ(posicion);
	//
	// this.addChild(insertPoint(e, new Vec4(1.0, 0, 0, 1.0)));
	//
	// this.addChild(insertTexto(texto, e));
	// }
	//
	// /**
	// * this method create a new 3D point. With a color and position
	// *
	// * @param color
	// * Color of the point
	// * @param position
	// * Position of 3D point in cartesian coordinates
	// * @return
	// */
	// public Node insertPoint(Vec3 position, Vec4 color) {
	// // Creating point
	// Point p = new Point();
	//
	// // Adding position and color
	// for (int i = 0; i < 1; i++) {
	// p.setPointSize(100);
	// p.addPoint(position, color);
	// }
	//
	// return p;
	// }

	/**
	 * Method to create a new 3D text. With a value and position. Text aligment
	 * is LEFT CENTER, and his font is "Arial.ttf"
	 * 
	 * @param texto
	 *            Text that is visible in 3D
	 * @param position
	 *            Position of 3D text in cartesian coordinates
	 * @return New node
	 */
	public static Node insertTextoS(String texto, Vec3 position, Vec4 color,
			int tam, boolean inPixels) {

		// Creating text node
		Text text = null;
		try {
			text = new Text();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Set up text properties
		text.setText(texto);
		text.setCharacterSize(tam);
		// Select that the text will be in pixels or screen coordinates
		if (inPixels) {
			text.setCharacterSizeMode(Text.CharacterSizeMode.SCREEN_COORDS);
		} else {
			text.setCharacterSizeMode(Text.CharacterSizeMode.OBJECT_COORDS);
		}
		text.setColor(color);
		text.setAxisAlignment(Text.AxisAlignment.SCREEN);
		text.setAlignment(Text.AlignmentType.LEFT_BOTTOM);
		text.setPosition((float) position.x(), (float) position.y(),
				(float) position.z());

		// Search font in resources directori
		if (ResourcesFactory.exitsResouce("arial.ttf"))
			text.setFont(ResourcesFactory.getResourcePath("arial.ttf"));
		else
			text.setFont("arial.ttf");
		text.setAutoRotateToScreen(true);

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(text, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return node
		return text;

	}

	/**
	 * Method to create a new 3D point. With a color, heigth and position.
	 * 
	 * @param position
	 *            Position of 3D text in cartesian coordinates
	 * @param color
	 *            color of the 3D point
	 * @param tam
	 *            the heigth of the point
	 * @return New text node
	 */
	public static Node insertPointS(Vec3 position, Vec4 color, int tam) {
		AutoTransform au = null;
		try {
			au = new AutoTransform();
		} catch (NodeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Creating point
		PixelPoint p = null;
		try {
			p = new PixelPoint();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// p.setDistanceAttenuation(new Vec3(0.0,0.0,0.0000001));
		p.setPointSize(tam);
		p.addPoint(position, color);

		try {
			au.addChild(p);
		} catch (NodeException e) {
			logger.error("command:" + "Error adding new child.",e);
		}
		// return au;
		return p;
	}

	/**
	 * Method to create a new 3D point. With a color, heigth and position.
	 * 
	 * @param hash
	 *            Hash Table that contains "Puntos3D" elements
	 * @return
	 */
	public static Node insertPointS(HashMap hash) {

		Group g = new Group();

		// Iterate over the keys in the map
		Iterator it = hash.keySet().iterator();
		while (it.hasNext()) {
			// Get key
			Double key = (Double) it.next();
			List list = (List) hash.get(key);

			PixelPoint p = null;
			try {
				p = new PixelPoint();
			} catch (NodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			ShapePoint p = new ShapePoint();
//			es.upv.ai2.osgvp.features.Sphere s = new Sphere();
//			p.setShape(s);
//			s.setRadius(100);
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				// Getting PixelPoint
				Punto3D element = (Punto3D) iter.next();
//				Vec4 color = element.getColor();
				Vec4 color = new Vec4(1,0,0,1);
				
				// Blending only is active if the alpha component are diferent
				// that 1
				if (color.w() != 1) {
					p.setEnabledBlending(true);
				}
				// Enabling antialiasing
				p.setEnabledSmoothing(true);

				// Set up size, position and color
//				p.setPointSize(element.getZize());
				p.setPointSize(20);
				try {
					p.getOrCreateStateSet().setLightingMode(Node.Mode.OFF | Node.Mode.PROTECTED);
				} catch (InvalidValueException e) {
					logger.error("command:" + "Error setting new lighthing mode.",e);
				}
				p.addPoint(element.getPosition(), color);
//				System.err.println("posicion del punto " +element.getPosition());
			}
			// Adding points to group
			try {
				g.addChild(p);
			} catch (NodeException e) {
				logger.error("command:" + "Error adding new child node.",e);
			}
		}
//		System.out.println("nuevo grupo");

		// Optimizer generated nodes
//		Optimizer opt = new Optimizer();
//		opt.optimize(g, OptimizationOptions.SPATIALIZE_GROUPS);

		return g;
	}

	/**
	 * Method to add 3D points to specific point type. With a color, heigth and
	 * position.
	 * 
	 * @param point
	 *            Node to add new 3D point
	 * @param position
	 *            Position of 3D text in cartesian coordinates
	 * @param color
	 *            color of the 3D point
	 * @param tam
	 *            the heigth of the point
	 * @return New text node
	 */
	public static void addPointToNode(Node point, Vec3 position, Vec4 color,
			int tam) {

		PixelPoint p = null;
		// Getting point
		if (point != null)
			p = (PixelPoint) point;

		// If not exits create new
		if (p == null)
			try {
				p = new PixelPoint();
			} catch (NodeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		// Blending only is active if the alpha component are diferent that 1
		if (color.w() != 1) {
			p.setEnabledBlending(true);
		}

		// Set up size, position and color
		p.setPointSize(tam);
		p.addPoint(position, color);

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("command:" + "Error Inicializing value.",e);
		} catch (NodeException e) {
			logger.error("command:" + "Error node exception.",e);
		}

	}

	/**
	 * Method to create a new 3D line. With a color, heigth and position.
	 * 
	 * @param ini
	 *            Initial position of line
	 * @param end
	 *            End positon of line.
	 * @param color
	 *            color of the 3D point
	 * @param tam
	 *            the heigth of the point
	 * @return New line node
	 */

	public static Node insertLineS(Vec3 ini, Vec3 end, Vec4 color, int tam) {
		// Creating a polyline
		Polyline p = null;
		try {
			p = new Polyline(ini, end);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameter
		p.setWidth(2f);
		p.setPattern((short) 0xFFFF);
		p.setFactor(2);
		p.setColor(color);

		// Optimize the node
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p);
		} catch (NodeException e) {
			logger.error("command:" + "Error setting the optimizer.",e);
		}

		// Return the node
		return p;
	}

	/**
	 * Method to add new points to specific line type. With a color, heigth and
	 * position.
	 * 
	 * @param line
	 *            Node to add new point
	 * @param position
	 *            Position of 3D point in cartesian coordinates
	 * @param color
	 *            color of the 3D point
	 * @param tam
	 *            the heigth of the point
	 */
	public static void addNodeToLine(Node line, Vec3 position, Vec4 color,
			int tam) {
		// Getting polyline
		Polyline p = (Polyline) line;

		// If not exits, create a new
		if (p == null)
			try {
				p = new Polyline(position, position);
			} catch (NodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// Add a point and color
		p.addVertex(position, color);

	}

	/**
	 * Create a new point node with a color and size. Using a position of 3D
	 * points
	 * 
	 * @param position
	 *            Position of 3D point
	 * @param color
	 *            Color of the 3D point
	 * @param tam
	 *            Size of the point (in pixels)
	 * @return
	 */
	public static Node insertPointS(Vec3[] position, Vec4 color, int tam) {
		// Creating point node
		PixelPoint p = null;
		try {
			p = new PixelPoint();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters and insert position
		for (int i = 0; i < position.length; i++) {
			p.setPointSize(tam);
			p.addPoint(position[i], color);
		}

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("command:" + "Error inicializing value for the optimizer.",e);
		} catch (NodeException e) {
			logger.error("command:" + "Error adding the optimizer.",e);
		}

		// Return node
		return p;
	}
	
	public static Node insertPointExtruded(List points, Vec4 color,String texture,double heigth) {
		// Creating a polygon
		PixelPoint p = null;
		try {
			p = new PixelPoint();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Adding points
		for (int i = 0; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			p.addVertex(element, color);
			//p.addPoint3D(element);
		}
		PointExtruder pe = new PointExtruder();
		pe.extrude(p,new Vec3 (0.0,0.0,1.0), heigth);	
		

		Group group = new Group();
		Geode geode = new Geode();
		
		geode.getOrCreateStateSet().setTwoSidedLighting(true, Node.Mode.OVERRIDE|Node.Mode.ON);
		try {
			geode.addDrawable(pe.getGeometry());
			group.addChild(geode);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding drawable object.",e);
		}
		
		return group;
	}
	
	
	
	/**
	 * Create a new quad point node with a color, size and texture. Using a position of 3D
	 * points
	 * 
	 * @param position
	 *            Position of 3D point
	 * @param color
	 *            Color of the 3D point
	 * @param tam
	 *            Size of the point (in pixels)
	 * @return
	 */
	public static Node insertQuadPoints(Vec3[] position, Vec4 color, float tam, String texture) {
		// Creating point node
		QuadPoint p = null;
		try {
			p = new QuadPoint(tam);
		} catch (NodeException e) {
			logger.error("command:" + "Error creating quad point.",e);
		}

		// Set up parameters and insert position
		for (int i = 0; i < position.length; i++) {
			p.setPointSize(tam);
			p.addPoint(position[i], color);
		}
	
//		if (texture!=null)
//			p.setTexture(texture);
		
		try {
			p.setBillboardingEnabled(true);
		} catch (InvalidValueException e) {
			logger.error("command:" + "Error setting billboarding value.",e);
		}
		
		
		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("command:" + "Error inciliazicing optimize values.",e);
		} catch (NodeException e) {
			logger.error("command:" + "Error setting optimizer.",e);
		}

		// Return node
		return p;
	}
	/**
	 * Create a new tesselation poligon with a color using an array of 3D points
	 * 
	 * @param points
	 *            Array of 3D points
	 * @param color
	 *            Color of the poligon
	 * @return Poligon node
	 */
	public static Node generateQuadPoligon(Vec3 position, Vec4 color,String texture,float tam) {
		// Creating a polygon
		TessellablePolygon p = null;
		try {
			p = new TessellablePolygon();
		} catch (NodeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

//		PositionAttitudeTransform pt = new PositionAttitudeTransform();
		AutoTransform pt = null;
		try {
			pt = new AutoTransform();
		} catch (NodeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			pt.addChild(p);
		} catch (NodeException e1) {
			logger.error("Command:" + "Error adding new child.",e1);
		}
		pt.setPosition(position);
		try {
			pt.setAutoRotateMode(AutoTransform.AutoRotateMode.ROTATE_TO_CAMERA);
		} catch (InvalidValueException e1) {
			logger.error("command:" + "Error setting autorotate value.",e1);
		}
		// Set up parameters
		// p.setType(Polygon.PolygonType.FILLED_POLYGON);
		p.setWidth(2f);
		// p.setPattern((short) 0xFFFF);
		// p.setFactor(2);
		p.setType(Polygon.PolygonType.FILLED_POLYGON);
		// p.setType(Polygon.PolygonType.EMPTY_POLYGON);
		// p.setType(Polygon.PolygonType.PATTERN_POLYGON);
		
		Vec3 bl = new Vec3(-tam/2,-tam/2,0.0);
		Vec3 br = new Vec3(tam/2,-tam/2,0.0);
		Vec3 tr = new Vec3(tam/2,tam/2,0.0);
		Vec3 tl = new Vec3(-tam/2,tam/2,0.0);
		p.setEnabledBlending(true);
//		Vector<Vec3> normal = new Vector<Vec3>();
//		normal.add(new Vec3(0,0,1));
//		p.setNormalArray(normal);
//		p.setNormalBinding(Geometry.AttributeBinding.BIND_OVERALL);
		
		try {
			p.getOrCreateStateSet().setLightingMode(Mode.OFF | Mode.PROTECTED);
		} catch (InvalidValueException e) {
			logger.error("command:" + "Error ",e);
		}
		p.addVertex(bl, color);
		p.addVertex(tl, color); 
		p.addVertex(tr, color);
		p.addVertex(br, color);
		
//		p.setTwoSidedLighting(true);
		
		// if there was any texture to put we would include it
		if (texture != null)
			p.setTexture(texture);
		// Tesselation of this poligon
		p.tesselate();

		// Optimizer generated nodes
//		Optimizer opt = new Optimizer();
//		opt.optimize(pt, OptimizationOptions.SPATIALIZE_GROUPS);

		// Return node
		return pt;
	}

	/**
	 * Create a new tesselation poligon with a color using an array of 3D points
	 * 
	 * @param points
	 *            Array of 3D points
	 * @param color
	 *            Color of the poligon
	 * @return Poligon node
	 */
	public static Node insertPolygon(Vec3[] points, Vec4 color,String texture) {
		// Creating a polygon
		TessellablePolygon p = null;
		try {
			p = new TessellablePolygon();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters
		// p.setType(Polygon.PolygonType.FILLED_POLYGON);
		p.setWidth(2f);
		// p.setPattern((short) 0xFFFF);
		// p.setFactor(2);
		p.setType(Polygon.PolygonType.FILLED_POLYGON);
		// p.setType(Polygon.PolygonType.EMPTY_POLYGON);
		// p.setType(Polygon.PolygonType.PATTERN_POLYGON);

		// Adding points
		for (int i = 0; i < points.length - 1; i++) {
			p.addVertex(points[i], color);
			// System.out.println("Position: " + inv[i].x() + " " + inv[i].y() +
			// " " + inv[i].z());
		}
		// if there was any texture to put we would include it
		if (texture != null)
			p.setTexture(texture);
		// Tesselation of this poligon
		p.tesselate();

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("Command:" + "Error setting optimize value.",e);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		// Return node
		return p;
	}
	
	

	/**
	 * Create a new tesselation poligon with a color using an List of 3D points
	 * 
	 * @param points
	 *            List of 3D points
	 * @param color
	 *            Color of the poligon
	 * @return Poligon node
	 */
	public static Node insertPolygon(List points, Vec4 color,String texture) {
		// Creating a polygon
		TessellablePolygon p = null;
		try {
			p = new TessellablePolygon();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters
		// p.setType(Polygon.PolygonType.FILLED_POLYGON);
		p.setWidth(2f);
		// p.setPattern((short) 0xFFFF);
		// p.setFactor(2);
		p.setType(Polygon.PolygonType.FILLED_POLYGON);
		// p.setType(Polygon.PolygonType.EMPTY_POLYGON);
		// p.setType(Polygon.PolygonType.PATTERN_POLYGON);

		// Blending only is active if the alpha component are diferent that 1
		if (color.w() != 1) {
			p.setEnabledBlending(true);
		}
		// Adding points
		for (int i = 0; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			p.addVertex(element, color);
		}
	

		// p.setTexture("C:/modelos3d/Images/land_shallow_topo_2048.jpg");
		// if there was any texture to put we would include it
		if (texture != null)
			p.setTexture(texture);
		// Tesselation of this poligon
		p.tesselate();

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.ALL_OPTIMIZATIONS);
		} catch (InvalidValueException e) {
			logger.error("Command:" + "Error setting optimizer value.",e);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		// Return node
		return p;
	}
	public static Node insertPolygonExtruded(List points, Vec4 color,String texture,double heigth) {
		// Creating a polygon
		Polygon p = null;
		try {
			p = new Polygon();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters
		p.setWidth(2f);
		p.setType(Polygon.PolygonType.FILLED_POLYGON);
		// Blending only is active if the alpha component are diferent that 1
		if (color.w() != 1) {
			p.setEnabledBlending(true);
		}
		List positionTapa = new ArrayList();
		// Adding points
		for (int i = 0; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			Vec3 elementTapa = new Vec3(element.x(),element.y(),element.z()+heigth);
			positionTapa.add(elementTapa);
			p.addVertex(element, color);
		}
		
		PolygonExtruder pe = new PolygonExtruder();
		pe.extrude(p,new Vec3 (0.0,0.0,1.0), heigth);		
		

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		Node node = null;
		try {
			opt.optimize(p, OptimizationOptions.ALL_OPTIMIZATIONS);
			node = FeatureFactory.insertPolygon(positionTapa, color, null);
			opt.optimize(node, OptimizationOptions.ALL_OPTIMIZATIONS);
			
		} catch (InvalidValueException e) {
			logger.error("Command:" + "Error setting optimizer value.",e);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}
		Group group = new Group();
		Geode geode = new Geode();
		
		geode.getOrCreateStateSet().setTwoSidedLighting(true, Node.Mode.ON| Node.Mode.OVERRIDE);
		try {
			geode.addDrawable(pe.getGeometry());
			group.addChild(geode);
			group.addChild(node);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding drawable object.",e);
		}
		
		return group;
	}
	/**
	 * Method to create a new 3D line. With a List of 3D points and a color
	 * 
	 * @param points
	 *            List of 3D points
	 * @param color
	 *            Color of the line
	 * @return
	 * 
	 */
	public static Node insertLine(List points, Vec4 color, float size) {
		Vec3 ori = null, fin = null;

		// Getting first points
		if (points.size() > 1) {
			ori = (Vec3) points.get(0);
			fin = (Vec3) points.get(1);
		}

		// If the line do not have more than 2 points return null
		if ((ori == null) || (fin == null))
			return null;

		// Create new polyline
		Polyline p = null;
		try {
			p = new Polyline(ori, fin);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		p.setVertexColor(0, color);
		p.setVertexColor(1, color);

		// Set up parameters
		p.setWidth(size);
		p.setPattern((short) 0xFFFF);
		p.setFactor(2);

		// Blending only is active if the alpha component are diferent that 1
		if (color.w() != 1) {
			p.setEnabledBlending(true);
		}

		// Enabling antialiasing
		p.setEnabledSmoothing(true);

		p.setColor(color);

		// Adding points
		for (int i = 2; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			p.addVertex(element, color);
		}

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		// Return node
		return p;

	}
	
	public static Node insertLineExtruded(List points, Vec4 color,String texture,double heigth) {
		// Creating a polygon
		Polyline p = null;
		try {
			p = new Polyline();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters
		p.setWidth(2f);

		// Blending only is active if the alpha component are diferent that 1
		if (color.w() != 1) {
			p.setEnabledBlending(true);
		}
		// List positionTapa = new ArrayList();
		// Adding points
		for (int i = 0; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			p.addVertex(element, color);
		}
		PolylineExtruder pe = new PolylineExtruder();
		pe.extrude(p,new Vec3 (0.0,0.0,1.0), heigth);		
		

		Group group = new Group();
		Geode geode = new Geode();
		
		geode.getOrCreateStateSet().setTwoSidedLighting(true, Node.Mode.ON| Node.Mode.OVERRIDE);
		try {
			geode.addDrawable(pe.getGeometry());
			group.addChild(geode);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding drawable object.",e);
		}
		
		return group;
	}
	
	
	
	

	/**
	 * Create a new group of 3D points with a color and size. Using a List of 3D
	 * points
	 * 
	 * @param points
	 *            List of 3D points
	 * @param color
	 *            Color of the line
	 * @param size
	 *            Size of the point (in pixels)
	 * @return
	 */
	public static Node insertPointL(List points, Vec4 color, float size) {
		// Creating point
		PixelPoint p = null;
		try {
			p = new PixelPoint();
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Set up parameters
		p.setPointSize(size);

		// Adding points
		for (int i = 0; i < points.size(); i++) {
			Vec3 element = (Vec3) points.get(i);
			p.addPoint(element, color);
		}

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		// Return node
		return p;

	}

	/**
	 * Create a new group of 3D labels with a size and heigth. Using a List of
	 * 3D points
	 * 
	 * @param labels
	 *            List of Flabels
	 * 
	 * @param size
	 *            Size of the labels
	 * @param heigth
	 *            Heigth of labels
	 * @param inPixels
	 *            True indicates that size is in pixel and false indicates real
	 *            size;
	 * @param type
	 * @return Node group
	 */
	public static Node insertLabels(List labels, int size, float heigth,
			boolean inPixels, int planetType) {
		// Creating a group node
		Group p = new Group();

		// Getting total size
		int total = labels.size();

		// this is for control a max of labels because the system not suport
		// more than 10000
		int max = 3000;
		if (total > max)
			total = max;

		if (labels != null) {
			for (int i = 0; i < total; i++) {
				FLabel f = (FLabel) labels.get(i);

				// Set up parameters

				// Getting color
				// Color co = f.getColor();
				Color co = new Color(255, 255, 255, 255);
				float r, g, b, a;
				r = ((float) co.getRed()) / 255.0f;
				g = ((float) co.getGreen()) / 255.0f;
				b = ((float) co.getBlue()) / 255.0f;
				a = ((float) co.getAlpha()) / 255.0f;
				// Set color
				Vec4 textColor = new Vec4(r, g, b, a);

				// Get label name
				String label = f.getString();
				// double heigth = h;
				Vec3 pos = null;
				if (planetType == Planet.CoordinateSystemType.PROJECTED) {
					pos = new Vec3(f.getOrig().getX(), f.getOrig().getY(),
							heigth);
				} else {
					// Transform geodesical coordinates to cartesian
					Vec3 posGeo = new Vec3(f.getOrig().getY(), f.getOrig()
							.getX(), heigth);

					pos = planet.convertLatLongHeightToXYZ(posGeo);
				}

				// Adding new child text nodes
				try {
					p.addChild(insertTextoS(label, pos, textColor, size, inPixels));
				} catch (NodeException e) {
					logger.error("Command:" + "Error adding new child text.",e);
				}
			}
		}

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("Command:" + "Error setting optimizer value.",e);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		return p;

	}
	
	/**
	 * Create a new group of 3D labels with a size and heigth. Using a List of
	 * 3D points
	 * 
	 * @param labels
	 *            List of Flabels
	 * 
	 * @param size
	 *            Size of the labels
	 * @param heigth
	 *            Heigth of labels
	 * @param inPixels
	 *            True indicates that size is in pixel and false indicates real
	 *            size;
	 * @param type
	 * @return Node group
	 */
	public static Node insertLabels(List<ILabel3D> labels,Planet planet) {
		
		int height;
		AttrInTableLabelingStrategy strategy;
		
		// Creating a group node
		Group p = new Group();

		// Getting total size
		int total = labels.size();

		// this is for control a max of labels because the system not suport
		// more than 10000
		int max = 3000;
		if (total > max)
			total = max;

		if (labels != null) {
			for (int i = 0; i < total; i++) {
				
				
				ILabel3D label3D = (ILabel3D) labels.get(i);
				FLabel f = label3D.getFlabel();

				// Set up parameters

				// Getting color
//				 Color co = f.getColor();
				Color co = new Color(255, 0, 0, 255);
				if (label3D instanceof SimpleLabel3D) {
					strategy = ((SimpleLabel3D) label3D).getStrategy();
					co = strategy.getFixedColor();
					height = strategy.getFont().getSize();
				}
				float r, g, b, a;
				r = ((float) co.getRed()) / 255.0f;
				g = ((float) co.getGreen()) / 255.0f;
				b = ((float) co.getBlue()) / 255.0f;
				a = ((float) co.getAlpha()) / 255.0f;
				// Set color
				Vec4 textColor = new Vec4(r, g, b, a);

				// Get label name
				String label = label3D.getTextField();
				// double heigth = h;
				Vec3 pos = null;
				Vec3 originalPos = null;
				originalPos = label3D.getPosition();
				if (planet.getCoordinateSystemType()== Planet.CoordinateSystemType.PROJECTED) {
					pos = new Vec3(originalPos.x(),originalPos.y(),originalPos.z());
				} else {
					// Transform geodesical coordinates to cartesian
					Vec3 posGeo = new Vec3(originalPos.y(),originalPos.x(),originalPos.z());

					pos = planet.convertLatLongHeightToXYZ(posGeo);
				}
				
				boolean inPixels = (label3D.getUnits() <= -1);
//				height = (int)f.getHeight()/255;
				height = (int) label3D.getHeightField();;
				// Adding new child text nodes
				try {
					p.addChild(insertTextoS(label, pos,textColor,height , inPixels));
//					p.addChild(insertTextoS(label, pos,textColor,height , true));
				} catch (NodeException e) {
					logger.error("Command:" + "Error adding new child text.",e);
				}
			}
		}

		// Optimizer generated nodes
		Optimizer opt = new Optimizer();
		try {
			opt.optimize(p, OptimizationOptions.SPATIALIZE_GROUPS);
		} catch (InvalidValueException e) {
			logger.error("Command:" + "Error setting optimizer value.",e);
		} catch (NodeException e) {
			logger.error("Command:" + "Error adding optimizer.",e);
		}

		return p;

	}

}
