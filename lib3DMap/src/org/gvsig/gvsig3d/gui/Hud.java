package org.gvsig.gvsig3d.gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.apache.log4j.Logger;
import org.gvsig.gui.beans.Messages;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.InvalidValueException;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.Text;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.util.UpdateNodeListener;
import org.gvsig.osgvp.viewer.IViewerContainer;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.andami.PluginServices;

/**
 * Use this class for draw items in HUD
 * 
 * @author julio
 * 
 */
public class Hud extends Group implements MouseMotionListener {

	private IViewerContainer m_canvas3d = null;
	
	private Planet m_planet = null;

	private String lonText;

	private String latText;

	private String lon;

	private String lat;

	private Text textoHud;

	private static String north;

	private static String south;

	private static String east;

	private static String west;
	
	private static Compass compass;
	
	private static Logger logger = Logger.getLogger(Hud.class.getName());

	static {
		north = Messages.getText("North");
		south = Messages.getText("South");
		east = Messages.getText("East");
		west = Messages.getText("West");

	}

	private int projectionType;

	/**
	 * Constructor
	 * 
	 * @param m_canvas3d
	 *            Viewer instance
	 * @param m_planet
	 *            Planet instance
	 */
	public Hud(IViewerContainer m_canvas3d, Planet m_planet) {
		super();
		this.m_canvas3d = m_canvas3d;
		this.m_planet = m_planet;
		this.projectionType = m_planet.getCoordinateSystemType();
		// Inicialize object
		init();
	}

	/**
	 * Inicilize the object params
	 */
	private void init() {

		try {
			textoHud = new Text();
			compass = new Compass(m_planet);
			if(projectionType == Planet.CoordinateSystemType.GEOCENTRIC){
				compass.setPanetType(Compass.Mode.SPHERIC);
			}
			else compass.setPanetType(Compass.Mode.FLAT);
		} catch (NodeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (getProjectionType() == Planet.CoordinateSystemType.GEOCENTRIC) {
			// Setting up longitud and latitud string
			lonText = PluginServices.getText(this, "Ext3D.longitude");
			latText = PluginServices.getText(this, "Ext3D.latitude");
		} else {
			lonText = PluginServices.getText(this, "X") + " ";
			;
			latText = PluginServices.getText(this, "Y") + " ";
			;
		}

		// Adding text to group
		try {
			this.addChild(textoHud);
			this.addChild(compass);
		} catch (NodeException e) {
			logger.error("Comand:" + "Error al a�adir nodo al hud.",e);
		}
		
		//Setting up the lighting mode to disable (rgaitan)
		try {
			getOrCreateStateSet().setLightingMode(Node.Mode.OFF | Node.Mode.PROTECTED);
		} catch (InvalidValueException e) {
			logger.error("Comand:" + "Error al inicializar las luces.",e);
		};

		// Seting up text
		textoHud.setCharacterSize(14);
		textoHud.setColor(new Vec4(1.0f, 1.0f, 1.0f, 1.0f));
		textoHud.setBackdropColor(0.0f, 0.0f, 1.0f, 1.0f);

		if (ResourcesFactory.exitsResouce("arial.ttf"))
			textoHud.setFont(ResourcesFactory.getResourcePath("arial.ttf"));
		else {
			// TODO: This freeze the execution.. disable when working. 
			textoHud.setFont("arial.ttf");
		}

		textoHud.setPosition(10, 10, 0);
		textoHud.setBackdropType(Text.BackdropType.OUTLINE);
		textoHud.setAlignment(Text.AlignmentType.LEFT_CENTER);
		
		compass.setUpdateListener(new UpdateNodeListener(){

			public void update(Node arg0) {
				compass.update(m_canvas3d.getOSGViewer().getCamera());
				
			}});
		
		//disabling compass.
		compass.setEnabledNode(false);

		// Add mouse listener to viewer
		((Component) m_canvas3d).addMouseMotionListener(this);

		// Update Hud
		updateHud();
	}

	/**
	 * This method updates information of the HUD
	 */
	public void updateHud() {

		if (m_planet.getCoordinateSystemType() == Planet.CoordinateSystemType.GEOCENTRIC) {
			// Getting longitud and latitud informacion from planet
			lon = Hud.getSexagesinal(m_planet.getLongitude(), true);
			lat = Hud.getSexagesinal(m_planet.getLatitude(), false);
			

			// Updating text information
			textoHud.setText(lonText + " " + lon + " " + latText + " " + lat);
		} else {
			// Getting longitud and latitud informacion from planet
			lon = Double.toString(m_planet.getLongitude());
			lat = Double.toString(m_planet.getLatitude());

			// Updating text information
			textoHud.setText(lonText + " " + lon + " " + latText + " " + lat);
		}
		
		compass.setScale(new Vec3(75,75,75));
		compass.setPosition(new Vec3(m_canvas3d.getWidth()-70,m_canvas3d.getHeight()-70,0));
		// Repainting view
		if (m_canvas3d != null)
			m_canvas3d.repaint();
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLatText() {
		return latText;
	}

	public void setLatText(String latText) {
		this.latText = latText;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLonText() {
		return lonText;
	}

	public void setLonText(String lonText) {
		this.lonText = lonText;
	}

	/**
	 * To transform longitud and latitud to sexagesinal format degress minuts
	 * seconds
	 * 
	 * @param num
	 *            number to transform
	 * @param lat
	 *            is tatitud or not
	 * @return sexagesinal format
	 */
	public static String getSexagesinal(double num, boolean lat) {

		String result = "";
		String ori = "";
		
		// Setting up North or South and East or West
		if (num < 0) {
			num = num * (-1);
			if (lat) {
				ori = east;//south;// Messages.getText("South");
			} else {
				ori = north;//north;// Messages.getText("North");
			}
		} else {
			if (lat) {
				ori = west;//west;// Messages.getText("West");
			} else {
				ori = south;//east;// Messages.getText("East");
			}
		}

		// transform degrees in sexagesinal format
		int grados = (int) num;
		double resG = num - grados;
		int minutos = (int) (resG * 60);
		double minutosD = (resG * 60);
		double resM = minutosD - minutos;
		int segundos = (int) (resM * 60);
		String cadG = "";
		if (grados < 10)
			cadG = cadG + "0";
		cadG = cadG + grados;

		String cadM = "";
		if (minutos < 10)
			cadM = cadM + "0";
		cadM = cadM + minutos;

		String cadS = "";
		if (segundos < 10)
			cadS = cadS + "0";
		cadS = cadS + segundos;

		// Building result string
		result = cadG + " " + cadM + " " + cadS + " " + ori;

		return result;
	}

	// MOUSE MOTION EVENTS

	public void mouseDragged(MouseEvent e) {
		// Updating Hud information+
		// If not update the hud information when mouses dragged the hud don�t change
		updateHud();

		// System.out.println("***************************************");
		// System.out.println("Longitud : " + m_planet.getLongitude());
		// System.out.println("Latitud : " + m_planet.getLatitude());
		// System.out.println("***************************************");

	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		updateHud();
	}

	public int getProjectionType() {
		return projectionType;
	}

	public void setProjectionType(int projectionType) {
		this.projectionType = projectionType;
	}

}
