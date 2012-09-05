package com.iver.ai2.gvsig3d.map3d.layers;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.cresques.cts.IProjection;
import org.exolab.castor.xml.Marshaller;
import org.gvsig.cacheservice.CacheService;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLyrDefault;
import org.gvsig.fmap.mapcontext.layers.operations.Classifiable;
import org.gvsig.fmap.mapcontext.layers.operations.ClassifiableVectorial;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;

import com.iver.ai2.gvsig3d.gui.VectorLayerMenu;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * This class is used to manage all the 3d properties that the layer has.
 * 
 * @author Salvador Bayarri
 * @author Julio Campos 
 *
 */
public class Layer3DProps implements IPersistence {

	// types of 3D layers
	public static final int layer3DImage = 0;
	public static final int layer3DElevation = 1;
	public static final int layer3DVector = 2;
	public static final int layer3DOSG = 3;
	public static long drawVersion = 0;

	private boolean newLayerOSG = false;
	
	// public static String m_cacheDir = "c:/data/cache"; // TEST
	// Create a .data directorio in user home for caching elements
	public static String m_cacheDir = System.getProperty("user.home")
			+ "/gvSIG/.data/cache";

	protected int m_tocOrder; // index of layer in TOC (drawing priority)
	protected int m_planetOrder; // current stage of layer in planet.
	// Can be temporarily different from the TOC order while moving groups of
	// layers

	protected float m_opacity = 1.0f; // Opacity of layer default 1.0
	protected float m_verticalEx = 10.0f; // Opacity of layer default 1.0
	protected float m_heigth = 100; // Opacity of layer default 1.0
	protected int m_type; // see type enumeration above
	protected String m_cacheName = "default";
	protected FLayer m_layer;
	protected CacheService m_cacheService;
	private boolean m_bChooseType = true;

	private boolean m_Zenable = false;

	// used by FLayers3D (3D group layer) to flag when they are actually hooked
	// to TOC or not
	private boolean m_hooked = false;

	private int option;

	private VectorLayerMenu vectorLayerMenu;
	
	private boolean isEditing = false;


	/**
	 * the constructor 
	 */
	public Layer3DProps() {
		m_tocOrder = -1; // not set
		m_planetOrder = -1;
	}

	/**
	 * This method make an instance of the 3D props of the layer that get from parameters
	 * 
	 * @param layer 
	 * @return the properties
	 */
	public static Layer3DProps getLayer3DProps(FLayer layer) {
		FLyrDefault baseLayer = (FLyrDefault) layer;
		Object propsObj = baseLayer.getProperty("3DLayerExtension");
		Layer3DProps props3D = null;
		if (propsObj != null) {
			try {
				props3D = (Layer3DProps) propsObj;
			} catch (Exception e) {
				props3D = null;
			}
		}
		if (drawVersion == 0)
			drawVersion = layer.getDrawVersion();
		return props3D;
	}

	/**
	 * Return The editing state
	 * 
	 * @return true->editing. false->not editing 
	 */
	public boolean isEditing() {
		return isEditing;
	}
	
	/**
	 * Set the editing state
	 * 
	 * @param true->editing. false->not editing
	 */
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	
	public void setChooseType(boolean bChoose) {
		m_bChooseType = bChoose;
	}

	public boolean getHooked() {
		return m_hooked;
	}

	public void setHooked(boolean hooked) {
		m_hooked = hooked;
	}

	/**
	 * Setting the layer
	 * 
	 * @param layer
	 */
	public void setLayer(FLayer layer) {

		if (m_layer == layer)
			return;

		m_layer = layer;

		// find out data type
		if (m_bChooseType) {
			m_bChooseType = false;
			m_type = layer3DImage;
			boolean bCanBeElev = false;

			// TODO Un toggle this comment to use WMS extensions

			if (layer instanceof FLyrWMS) {
				FLyrWMS wmsLayer = (FLyrWMS) layer;
				String format = wmsLayer.getFormat();
				if (format.regionMatches(true, 0, "image/geotiff", 0, 13)
						|| format.regionMatches(true, 0, "image/tiff", 0, 10))
					bCanBeElev = true;
			} else if (layer instanceof FLyrWCS) {
				FLyrWCS wcsLayer = (FLyrWCS) layer;
				String format = wcsLayer.getFileFormat();
				Hashtable props = wcsLayer.getProperties();
				String params = (String) props.get("parameter");
				if (format.compareToIgnoreCase("GEOTIFF_INT16") == 0
						&& params.length() == 0)
					bCanBeElev = true;
			}
			// FEATURES
			else /**/if (layer instanceof ClassifiableVectorial) {

				if (this.getType() != layer3DOSG) {
					vectorLayerMenu = new VectorLayerMenu(this, layer.getName());
					vectorLayerMenu.setModal(true);
					vectorLayerMenu.pack();
					vectorLayerMenu.setVisible(true);
				}

				// PluginServices.getMDIManager().addWindow(vectorLayerMenu);

				// // choose rasterization of 3D
				// option = JOptionPane.showConfirmDialog(
				// (Component) PluginServices.getMainFrame(),
				// PluginServices
				// .getText(this, "Rasterize_layer_question"),
				// PluginServices.getText(this, "Layer_options"),
				// JOptionPane.YES_NO_OPTION);
				//
				// if (option == JOptionPane.YES_OPTION)
				// m_type = layer3DImage;
				// else
				// m_type = layer3DVector;
				//
				// if (m_type == layer3DVector) {
				// String Altura = JOptionPane.showInputDialog(PluginServices
				// .getText(this, PluginServices.getText(this,
				// "Heigth_layer_question")), "1000");
				//
				// if (Altura != null) {
				// int h = Integer.parseInt(Altura);
				// if (h >= 0)
				// m_heigth = h;
				// }
				// }

			} else if (layer instanceof FLyrRasterSE) {
				FLyrRasterSE rasterLayer = (FLyrRasterSE) layer;
				if (rasterLayer.getBandCount() == 1)
					bCanBeElev = true;

			}

			if (m_type == layer3DImage && bCanBeElev) {
				option = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),
						PluginServices
								.getText(this, "Elevation_layer_question"),
						PluginServices.getText(this, "Layer_options"),
						JOptionPane.YES_NO_OPTION);

				if (option == JOptionPane.YES_OPTION)
					m_type = layer3DElevation;
			}

		}
	}

	/**
	 * Initialize the cache name for the layer. It uses the planet type the view projection and the type o coordinates 
	 * 
	 * @param planetType
	 * @param viewProj
	 * @param geocentricCoordinates
	 */
	public void initCacheName(int planetType, IProjection viewProj,
			int geocentricCoordinates) {
		// TODO: use full path of source or service, not just layer name

		String typeStr;
		if (planetType == geocentricCoordinates)
			typeStr = "Sph";
		else
			typeStr = "Pla" + viewProj.getAbrev();

		if (m_type == layer3DElevation)
			typeStr += "Elev";
		else if (m_type == layer3DVector)
			typeStr += "Vect";

		String layerInfo = m_layer.getName();
		// TODO Un toggle this comment to use WMS extension

		if (m_layer instanceof FLyrWMS) {
			FLyrWMS wmsLayer = (FLyrWMS) m_layer;

			// Getting wms layer properties
			HashMap props = wmsLayer.getProperties();
			Vector styles;
			// Getting styles
			styles = (Vector) (props.get("styles"));

			// Adding styles to cache path
			String layerStyle = "";
			if (styles != null) {
				styles.size();
				for (int i = 0; i < styles.size(); i++) {
					String ele = (String) styles.get(i);
					layerStyle += ele;
				}
			}

			layerInfo = wmsLayer.getHost().toString()
					+ wmsLayer.getLayerQuery() + "_" + layerStyle;

		} else {
			layerInfo = m_layer.getName();
		}
		/**/
		m_cacheName = typeStr + "_" + layerInfo;

		m_cacheName = m_cacheName.replace('/', '_');
		m_cacheName = m_cacheName.replace(':', '_');
		m_cacheName = m_cacheName.replace('\\', '_');
		m_cacheName = m_cacheName.replace('*', '_');
		m_cacheName = m_cacheName.replace('<', '_');
		m_cacheName = m_cacheName.replace('>', '_');
		m_cacheName = m_cacheName.replace('?', '_');
		m_cacheName = m_cacheName.replace('"', '_');
		m_cacheName = m_cacheName.replace('|', '_');

		// filter strange characters out of the cache name
		int iChar;
		for (iChar = 0; iChar < m_cacheName.length(); iChar++) {
			char c = m_cacheName.charAt(iChar);
			boolean bIsLow = (c >= 'a') && (c <= 'z');
			boolean bIsUp = (c >= 'A') && (c <= 'Z');
			boolean bIsNum = (c >= '0') && (c <= '9');
			if (!bIsLow && !bIsUp && !bIsNum && c != '_' && c != '.') {
				int newCInt = java.lang.Math.abs((int) (c) - (int) 'A');
				newCInt = (newCInt % 26) + (int) 'A';
				char newC = (char) newCInt;
				m_cacheName = m_cacheName.replace(c, newC);
			}
		}
	}

	/**
	 * Return the cacheService of this layer. It depends of the type of the layer.
	 * 
	 * @return
	 */
	public CacheService getCacheService() {
		return m_cacheService;
	}

	/**
	 * Set the cacheService of this layer. It depends of the type of the layer.
	 * 
	 * @param srv
	 */
	public void setCacheService(CacheService srv) {
		m_cacheService = srv;
	}

	/**
	 * Set the type of this layer
	 * 
	 * @param type
	 */
	public void setType(int type) {
		m_type = type;
	}

	/**
	 * Get the type of the layer
	 * 
	 * @return
	 */
	public int getType() {
		return m_type;
	}

	/**
	 * Set the toc order of this layer
	 * 
	 * @param order
	 */
	public void setTocOrder(int order) {
		m_tocOrder = order;
	}

	/**
	 * Get the toc order of this layer
	 * 
	 * @return
	 */
	public int getTocOrder() {
		return m_tocOrder;
	}

	/**
	 * Set the planet order of this layer
	 * 
	 * @param order
	 */
	public void setPlanetOrder(int order) {
		m_planetOrder = order;
	}

	/**
	 * Get the planet order of this layer
	 * 
	 * @return
	 */
	public int getPlanetOrder() {
		return m_planetOrder;
	}

	/**
	 * Get the cache name for this layer
	 * 
	 * @return
	 */
	public String getCacheName() {
		return m_cacheName;
	}

	/**
	 * Verifies that the vector layer's legend matches the one in the cache
	 * folder
	 */
	public void VerifyLegend(String planetName) {

		
		//NotificationManager.addInfo(m_cacheDir, null);
		if (m_layer instanceof Classifiable) {
			Classifiable legendLyr = (Classifiable) m_layer;
			String cacheFolder = m_cacheDir + "/" + planetName + "/"
					+ m_cacheName;
			File cacheFolderFile = new File(cacheFolder);
			if (!cacheFolderFile.exists())
				cacheFolderFile.mkdir();

			String legendFileName = cacheFolder + "/" + "legend.xml";
			File legendFile = new File(legendFileName);

			if (legendFile.exists()) { // read legend
				NotificationManager.addInfo("el fichero existe", null);
				FileReader reader = null;
				try {
					reader = new FileReader(legendFile);
				} catch (FileNotFoundException e) {
					return;
				}
				try {
					XmlTag tag = (XmlTag) XmlTag.unmarshal(reader);
					XMLEntity legendXml = new XMLEntity(tag);
					ILegend legend = LegendFactory.createFromXML(legendXml);
					if (m_layer instanceof FLyrVect) {
						((FLyrVect) m_layer).setLegend((IVectorLegend) legend);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else { // write legend
				NotificationManager.addInfo("el fichero no existe", null);
				ILegend legend = legendLyr.getLegend();
				if (legend == null)
					return;

				XMLEntity xmlLegend=null;
				try {
					xmlLegend = legend.getXMLEntity();
				} catch (XMLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					FileWriter writer = new FileWriter(legendFileName);
					Marshaller m = new Marshaller(writer);
					m.setEncoding("ISO-8859-1");
					m.marshal(xmlLegend.getXmlTag());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// IPersistance

	public String getClassName() {
		return this.getClass().getName();
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.IPersistence#getXMLEntity()
	 */
	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();

		xml.putProperty("type", m_type);
		xml.putProperty("order", m_tocOrder);
		xml.putProperty("opacity", m_opacity);
		xml.putProperty("heigth", m_heigth);
		xml.putProperty("cacheName", m_cacheName);

		return xml;
	}

	/* (non-Javadoc)
	 * @see com.iver.utiles.IPersistence#setXMLEntity(com.iver.utiles.XMLEntity)
	 */
	public void setXMLEntity(XMLEntity xml) {
		if (xml.contains("type")) {
			m_bChooseType = false;
			m_type = xml.getIntProperty("type");
		}
		if (xml.contains("order"))
			m_tocOrder = xml.getIntProperty("order");
		if (xml.contains("opacity"))
			m_opacity = Float.parseFloat(xml.getStringProperty("opacity"));
		if (xml.contains("heigth"))
			m_heigth = Float.parseFloat(xml.getStringProperty("heigth"));
		if (xml.contains("cacheName"))
			m_cacheName = xml.getStringProperty("cacheName");
	}

	/**
	 * Return the opacity of this layer
	 * 
	 * @return
	 */
	public float getOpacity() {
		return m_opacity;
	}

	/**
	 * Set the opacity of this layer
	 * 
	 * @param A value between 0 and 1 
	 */
	public void setOpacity(float m_opacity) {
		this.m_opacity = m_opacity;
	}

	/**
	 * Get the vertical exaggeration for this layer
	 * 
	 * @return
	 */
	public float getVerticalEx() {
		return m_verticalEx;
	}

	/**
	 * Get the vertical exaggeration from this layer
	 * 
	 * @param ex
	 */
	public void setVerticalEx(float ex) {
		m_verticalEx = ex;
	}

	/**
	 * Get the height that this layer has when it draws in 3D
	 * 
	 * @return
	 */
	public float getHeigth() {
		return m_heigth;
	}

	/**
	 * Set the height that this layer has when it draws in 3D
	 * 
	 * @param heigth
	 */
	public void setHeigth(float m_heigth) {
		this.m_heigth = m_heigth;
	}

	/**
	 * If the features of this layer have z value this method will return true. In the other case it will return false
	 * 
	 * @return
	 */
	public boolean isZEnable() {
		return m_Zenable;
	}

	/**
	 * Set the z enable
	 * 
	 * @param zenable
	 */
	public void setZEnable(boolean zenable) {
		m_Zenable = zenable;
	}

	/**
	 * It is to know if this layer is new. It only works with the OSG layers
	 * 
	 * @return
	 */
	public boolean isNewLayerOSG() {
		return newLayerOSG;
	}

	/**
	 * Set if this layer is new. It only works with OSG layers.
	 * 
	 * @param newLayerOSG
	 */
	public void setNewLayerOSG(boolean newLayerOSG) {
		this.newLayerOSG = newLayerOSG;
	}

}
