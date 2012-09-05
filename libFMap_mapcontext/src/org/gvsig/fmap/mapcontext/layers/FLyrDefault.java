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
package org.gvsig.fmap.mapcontext.layers;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ReloadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.StartEditionLayerException;
import org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.exception.BaseException;
import org.slf4j.LoggerFactory;

import com.iver.utiles.IPersistence;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * <p>Implementation of the common characteristics of all layers: visibility, activation, name, ...</p>
 *
 * <p>Represents the definition of a basic layer, implementing {@link FLayer FLayer}, and new functionality:
 * <ul>
 *  <li>Supports transparency.
 *  <li>Notification of evens produced using this layer.
 *  <li>Can have internal virtual layers.
 *  <li>Can have a text layer.
 *  <li>Supports an strategy for visit its geometries.
 *  <li>Can have an image in the <i>TOC (table of contents)</i> associated to the state of this layer.
 * </ul>
 * </p>
 *
 * <p>Each graphical layer will inherit from this class and adapt to its particular logic and model according
 *  its nature.</p>
 *
 * @see FLayer
 * @see FLayerStatus
 */
public abstract class FLyrDefault implements FLayer, LayerListener {
	/**
	 * Useful for debug the problems during the implementation.
	 */
	final static private org.slf4j.Logger logger = LoggerFactory.getLogger(FLyrDefault.class);
	private LayerChangeSupport layerChangeSupport = new LayerChangeSupport();

	/**
	 * Path to the upper layer which this layer belongs.
	 *
	 * @see #getParentLayer()
	 * @see #setParentLayer(FLayers)
	 */
	private FLayers parentLayer = null;

	/**
	 * A node in the tree of layers. Isn't used.
	 *
	 * @see #getVirtualLayers()
	 * @see #setVirtualLayers(FLayers)
	 */
	//	private FLayers virtualLayers = null;
	/**
	 * Name for this layer, this also will be a property in the XML entity that
	 * represents this layer.
	 *
	 * @see #getName()
	 * @see #setName(String)
	 *
	 * @deprecated value stored en delegatedDynObject
	 */
	private String namex;

	/**
	 * Projection for this layer.
	 *
	 * @see #getProjection()
	 * @see #setProjection(IProjection)
	 *
	 * @deprecated value stored en delegatedDynObject
	 */
	private IProjection projectionx;

	/**
	 * Transparency level of this layer in the range 0-255. By default 255.
	 * 0   --> Transparent
	 * 255 --> Opaque
	 *
	 * @see #getTransparency()
	 * @see #setTransparency(int)
	 */
	private int transparency = 255;

	/**
	 * Coordinate transformation.
	 *
	 * @see #getCoordTrans()
	 * @see #setCoordTrans(ICoordTrans)
	 */
	private ICoordTrans ct;

	/**
	 * Minimum scale, >= 0 or -1 if not defined. By default -1.
	 *
	 * @see #getMinScale()
	 * @see #setMinScale(double)
	 */
	private double minScale = -1; // -1 indica que no se usa

	/**
	 * Maximum scale, >= 0 or -1 if not defined. By default -1.
	 *
	 * @see #getMaxScale()
	 * @see #setMaxScale(double)
	 */
	private double maxScale = -1;
	//	private boolean isInTOC = true;

	/**
	 * Array list with all listeners registered to this layer.
	 *
	 * @see #getLayerListeners()
	 * @see #removeLayerListener(LayerListener)
	 * @see #callEditionChanged(LayerEvent)
	 */
	protected ArrayList layerListeners = new ArrayList();


	/**
	 * Hash table with the extended properties of this layer.
	 *
	 * @see #getProperty(Object)
	 * @see #setProperty(Object, Object)
	 * @see #getExtendedProperties()
	 */
	private Hashtable properties = new Hashtable();

	//by default, all is active, visible and avalaible
	/**
	 * Status of this layer.
	 *
	 * @see #getFLayerStatus()
	 * @see #setFLayerStatus(FLayerStatus)
	 * @see #isActive()
	 * @see #setActive(boolean)
	 * @see #isVisible()
	 * @see #setVisible(boolean)
	 * @see #visibleRequired()
	 * @see #isEditing()
	 * @see #setEditing(boolean)
	 * @see #isInTOC()
	 * @see #isCachingDrawnLayers()
	 * @see #setCachingDrawnLayers(boolean)
	 * @see #isDirty()
	 * @see #setDirty(boolean)
	 * @see #isAvailable()
	 * @see #setAvailable(boolean)
	 * @see #isOk()
	 * @see #isWritable()
	 * @see #getNumErrors()
	 * @see #getError(int)
	 * @see #getErrors()
	 * @see #addError(BaseException)
	 */
	private FLayerStatus status = new FLayerStatus();
	/**
	 * Image drawn shown in the TOC according the status of this layer.
	 *
	 * @see #getTocStatusImage()
	 * @see #setTocStatusImage(Image)
	 */
	private Image tocStatusImage;

	protected DelegatedDynObject delegatedDynObject;

	/**
	 * Draw version of the context. It's used for know when de componend has
	 * changed any visualization property
	 *
	 *  @see getDrawVersion
	 *  @see updateDrawVersion
	 */
	private long drawVersion= 0L;

	public FLyrDefault() {
		this.delegatedDynObject = (DelegatedDynObject) ToolsLocator
		.getDynObjectManager()
		.createDynObject(FLayer.DYNCLASS_NAME);
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getProperty(java.lang.Object)
	 */
	public Object getProperty(Object key) {
		return properties.get(key);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setProperty(java.lang.Object, java.lang.Object)
	 */
	public void setProperty(Object key, Object val) {
		properties.put(key, val);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getExtendedProperties()
	 */
	public Map getExtendedProperties() {
		return properties;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setActive(boolean)
	 */
	public void setActive(boolean selected) {
		status.active = selected;
		callActivationChanged(LayerEvent.createActivationChangedEvent(this,
		"active"));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isActive()
	 */
	public boolean isActive() {
		return status.active;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.delegatedDynObject.setDynValue("name", name);
		callNameChanged(LayerEvent.createNameChangedEvent(this, "name"));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getName()
	 */
	public String getName() {
		return (String) this.delegatedDynObject.getDynValue("name");
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#load()
	 */
	public void load() throws LoadLayerException {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setVisible(boolean)
	 */
	public void setVisible(boolean visibility) {
		if (status.visible != visibility){
			status.visible = visibility;
			this.updateDrawVersion();

			//			if (this.getMapContext() != null){
			//				this.getMapContext().clearAllCachingImageDrawnLayers();
			//			}
			callVisibilityChanged(LayerEvent.createVisibilityChangedEvent(this,
			"visible"));
		}
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isVisible()
	 */
	public boolean isVisible() {
		return status.visible && status.available;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getParentLayer()
	 */
	public FLayers getParentLayer() {
		return parentLayer;
	}


	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setParentLayer(com.iver.cit.gvsig.fmap.layers.FLayers)
	 */
	public void setParentLayer(FLayers root) {
		if (this.parentLayer != root){
			this.parentLayer = root;
			this.updateDrawVersion();
		}
	}

	/**
	 * <p>Inserts the projection to this layer.</p>
	 *
	 * @param proj information about the new projection
	 *
	 * @see #isReprojectable()
	 * @see #reProject(MapControl)
	 */
	public void setProjection(IProjection proj) {
		IProjection curProj = this.getProjection();
		if (curProj == proj) {
			return;
		}
		if (curProj != null && curProj.equals(proj)){
			return;
		}
		this.updateDrawVersion();
		this.delegatedDynObject.setDynValue("SRS", proj);
		// Comprobar que la proyección es la misma que la de FMap
		// Si no lo es, es una capa que está reproyectada al vuelo
		if ((proj != null) && (getMapContext() != null)) {
			if (proj != getMapContext().getProjection()) {
				ICoordTrans ct = proj.getCT(getMapContext().getProjection());
				setCoordTrans(ct);
				logger.debug("Cambio proyección: FMap con "
						+ getMapContext().getProjection().getAbrev() + " y capa "
						+ getName() + " con " + proj.getAbrev());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.cresques.geo.Projected#getProjection()
	 */
	public IProjection getProjection() {
		if (!this.delegatedDynObject.hasDynValue("SRS")) {
			return null;
		}
		return (IProjection) this.delegatedDynObject.getDynValue("SRS");
	}

	/**
	 * <p>Changes the projection of this layer.</p>
	 * <p>This method will be overloaded in each kind of layer, according its specific nature.</p>
	 *
	 * @param mapC <code>MapControl</code> instance that will reproject this layer
	 *
	 * @return <code>true<code> if the layer has been created calling {@link FLayers#addLayer(FLayer) FLayers#addLayer}. But returns <code>false</code>
	 *  if the load control logic of this layer is in the reprojection method
	 *
	 * @see #isReprojectable()
	 * @see #setProjection(IProjection)
	 */
	public void reProject(ICoordTrans arg0) {
	}

	/**
	 * Returns the transparency level of this layer, in the range 0-255 .
	 *
	 * @return the transparency level
	 *
	 * @see #setTransparency(int)
	 */
	public int getTransparency() {
		return transparency;
	}

	/**
	 * Inserts the transparency level for this layer, the range allowed is 0-255 .
	 *
	 * @param trans the transparency level
	 *
	 * @see #getTransparency()
	 */
	public void setTransparency(int trans) {
		if (this.transparency != trans){
			transparency = trans;
			this.updateDrawVersion();
		}
	}
	/**
	 * <p>Returns an entity that represents this layer.</p>
	 *
	 * <p>This XML entity has elements (properties) that represent and store information about this layer.</p>
	 *
	 * <p>There are two kinds of information: default properties of this layer, and extended properties (they added that weren't by default)</p>
	 *
	 * <p> <b>Default properties:</b>
	 *  <ul>
	 *   <li> className : name of this class
	 *   <li> active : if this layer is active or not
	 *   <li> name : name of this layer
	 *   <li> minScale : minimum scale of this layer
	 *   <li> maxScale : maximum scale of this layer
	 *   <li> visible : if this layer is visible or not
	 *   <li> proj : the projection of this layer (only if it's defined)
	 *   <li> transparency : transparency level of this layer
	 *   <li> isInTOC : if this layer is in the TOC or not
	 *  </ul>
	 * </p>
	 *
	 * <p> <b>Extended properties:</b> are stored as children of the tree-node returned. There are two kinds of information for a child,
	 *  according if it's an instance of an <code>String</code> or of an object that implements the interface <code>IPersistance</code>.
	 *
	 *  <ul>
	 *   <li> <i>Instance of <code>String</code>:</i>
	 *   <ul>
	 *    <li> className : name of the class of the object that it's the property
	 *    <li> value : value of the property
	 *    <li> layerPropertyName : name of the extended property of the layer
	 *   </ul>
	 *   <li> <i>Implements <code>IPersistance</code>:</i>
	 *   <ul>
	 *    <li> Information returned by the implementation of the method <code>getXMLEntity</code> of that object
	 *    <li> className : name of the class of the object (this information could be with the information returned by
	 *     the method <code>getXMLEntity</code> of that object
	 *    <li> layerPropertyName : name of the extended property of the layer
	 *   </ul>
	 *  <ul>
	 * </p>
	 *
	 * @return an XML entity with information to the current layer
	 * @throws XMLException
	 * @throws org.gvsig.fmap.mapcontext.layers.XMLException if there is an error obtaining the object.
	 *
	 * @see #setXMLEntity(XMLEntity)
	 * @see #setXMLEntity03(XMLEntity)
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", this.getClass().getName());

		xml.putProperty("active", status.active);
		xml.putProperty("name", this.getName());
		xml.putProperty("minScale", minScale);
		xml.putProperty("maxScale", maxScale);

		xml.putProperty("visible", status.visible);
		IProjection projection = this.getProjection();
		if (projection != null) {
			xml.putProperty("proj", projection.getFullCode());
		}
		xml.putProperty("transparency", transparency);
		xml.putProperty("isInTOC", status.inTOC);

		// persist Properties hashTable
		Set keyset = properties.keySet();
		Iterator keyitr = keyset.iterator();
		XMLEntity xmlProperties = new XMLEntity();
		xmlProperties.putProperty("tagName", "properties");
		while (keyitr.hasNext()) {
			String propName = (String)keyitr.next();
			Object obj = properties.get(propName);
			if (obj instanceof IPersistence)
			{
				IPersistence persistObj = (IPersistence)obj;
				XMLEntity xmlPropObj = persistObj.getXMLEntity();
				// make sure the node contains the class name
				if (!xmlPropObj.contains("className")) {
					try {
						String propClassName = persistObj.getClassName();
						System.out.println("PROP CLASS NAME "+propClassName);
						xmlPropObj.putProperty("className", propClassName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				xmlPropObj.putProperty("layerPropertyName", propName);
				xmlProperties.addChild(xmlPropObj);
			} else if (obj instanceof String) {
				XMLEntity xmlPropObj = new XMLEntity();
				xmlPropObj.putProperty("className", String.class.getName());
				xmlPropObj.putProperty("value",(String)obj);
				xmlPropObj.putProperty("layerPropertyName", propName);
				xmlProperties.addChild(xmlPropObj);
			}
		}
		if (xmlProperties.getChildrenCount() > 0) {
			xml.addChild(xmlProperties);
		}
		return xml;
	}

	/**
	 * <p>Inserts information to this layer.</p>
	 *
	 * <p>This XML entity has elements that represent and store information about this layer.</p>
	 *
	 * <p>The properties are the same as the described in <code>getXMLEntity()</code>. And the properties
	 *  <i>proj</i>,  <i>transparency</i>, <i>isInTOC</i> are optional.</p>
	 *
	 * <p>The property <i>numProperties</i> is optional, and only used in old projects.</p>
	 *
	 * @see FLyrDefault#getXMLEntity()
	 *
	 * @param xml an <code>XMLEntity</code> with the information
	 *
	 * @throws org.gvsig.fmap.mapcontext.layers.XMLException if there is an error setting the object.
	 *
	 * @see #getXMLEntity()
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException {
		status.active = xml.getBooleanProperty("active");
		this.setName(xml.getStringProperty("name"));
		minScale = xml.getDoubleProperty("minScale");
		maxScale = xml.getDoubleProperty("maxScale");
		status.visible = xml.getBooleanProperty("visible");
		if (xml.contains("proj")) {
			setProjection(CRSFactory.getCRS(xml.getStringProperty("proj")));
		}
		if (xml.contains("transparency")) {
			transparency = xml.getIntProperty("transparency");
		}
		if (xml.contains("isInTOC")) {
			status.inTOC = xml.getBooleanProperty("isInTOC");
		}

		// recreate Properties hashTable
		int xmlPropertiesPos = xml.firstIndexOfChild("childName", "properties");
		XMLEntity xmlProperties = null;
		if (xmlPropertiesPos > -1) {
			xmlProperties = xml.getChild(xmlPropertiesPos);
		}

		if (xmlProperties != null) {

			int numProps = xmlProperties.getChildrenCount();
			Object obj;
			String className;
			Class classProp;
			IPersistence objPersist;
			for (int iProp=0; iProp<numProps; iProp++) {
				XMLEntity xmlProp = xmlProperties.getChild(iProp);
				try {
					className = xmlProp.getStringProperty("className");
					if (className.equals(String.class.getName())) {
						obj = xmlProp.getStringProperty("value");
					} else {
						classProp = Class.forName(className);
						obj = classProp.newInstance();
						objPersist = (IPersistence)obj;
						objPersist.setXMLEntity(xmlProp);

					}
					String propName = xmlProp.getStringProperty("layerPropertyName");
					properties.put(propName, obj);
				} catch (Exception e) {
					logger.error("Layer:" + this.getName()
							+ ": Error loading properties", e);
					continue;
				}
			}
		}
		this.updateDrawVersion();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getMapContext()
	 */
	public MapContext getMapContext() {
		if (getParentLayer() != null) {
			return getParentLayer().getMapContext();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#addLayerListener(com.iver.cit.gvsig.fmap.layers.LayerListener)
	 */
	public boolean addLayerListener(LayerListener o) {
		if (layerListeners.contains(o)) {
			return false;
		}
		return layerListeners.add(o);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getLayerListeners()
	 */
	public LayerListener[] getLayerListeners() {
		return (LayerListener[])layerListeners.toArray(new LayerListener[0]);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#removeLayerListener(com.iver.cit.gvsig.fmap.layers.LayerListener)
	 */
	public boolean removeLayerListener(LayerListener o) {
		return layerListeners.remove(o);
	}
	/**
	 *
	 */
	private void callDrawValueChanged(LayerEvent e) {
		for (Iterator iter = layerListeners.iterator(); iter.hasNext();) {
			LayerListener listener = (LayerListener) iter.next();

			listener.drawValueChanged(e);
		}
	}
	/**
	 * Called by the method {@linkplain #setName(String)}. Notifies all listeners associated to this layer,
	 *  that its name has changed.
	 *
	 * @param e a layer event with the name of the property that has changed
	 *
	 * @see #setName(String)
	 */
	private void callNameChanged(LayerEvent e) {
		for (Iterator iter = layerListeners.iterator(); iter.hasNext();) {
			LayerListener listener = (LayerListener) iter.next();

			listener.nameChanged(e);
		}
	}

	/**
	 * Called by the method {@linkplain #setVisible(boolean)}. Notifies all listeners associated to this layer,
	 *  that its visibility has changed.
	 *
	 * @param e a layer event with the name of the property that has changed
	 *
	 * @see #setVisible(boolean)
	 */
	private void callVisibilityChanged(LayerEvent e) {
		for (Iterator iter = layerListeners.iterator(); iter.hasNext();) {
			LayerListener listener = (LayerListener) iter.next();

			listener.visibilityChanged(e);
		}
	}

	/**
	 * Called by the method {@linkplain #setActive(boolean)}. Notifies all listeners associated to this layer,
	 *  that its active state has changed.
	 *
	 * @param e a layer event with the name of the property that has changed
	 *
	 * @see #setActive(boolean)
	 */
	private void callActivationChanged(LayerEvent e) {
		for (Iterator iter = layerListeners.iterator(); iter.hasNext();) {
			LayerListener listener = (LayerListener) iter.next();

			listener.activationChanged(e);
		}
	}

	/**
	 * Returns the virtual layers associated to this layer.
	 *
	 * @return a node with the layers
	 *
	 * @see #setVirtualLayers(FLayers)
	 */
	//	public FLayers getVirtualLayers() {
	//		return virtualLayers;
	//	}

	/**
	 * Inserts virtual layers to this layer.
	 *
	 * @param virtualLayers a node with the layers
	 *
	 * @see #getVirtualLayers()
	 */
	//	public void setVirtualLayers(FLayers virtualLayers) {
	//		this.virtualLayers = virtualLayers;
	//	}

	/**
	 * Sets transformation coordinates for this layer.
	 *
	 * @param ct an object that implements the <code>ICoordTrans</code> interface, and with the transformation coordinates
	 *
	 * @see #getCoordTrans()
	 */
	public void setCoordTrans(ICoordTrans ct) {
		if (this.ct == ct){
			return;
		}
		if (this.ct != null && this.ct.equals(ct)){
			return;
		}
		this.ct = ct;
		this.updateDrawVersion();
	}

	/**
	 * Returns the transformation coordinates of this layer.
	 *
	 * @return an object that implements the <code>ICoordTrans</code> interface, and with the transformation coordinates
	 *
	 * @see #setCoordTrans(ICoordTrans)
	 */
	public ICoordTrans getCoordTrans() {
		return ct;
	}

	/**
	 * <p>Method called by {@link FLayers FLayers} to notify this layer that is going to be added.
	 *  This previous notification is useful for the layers that need do something before being added. For
	 *  example, the raster needs reopen a file that could have been closed recently.</p>
	 */
	public void wakeUp() throws LoadLayerException {
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getMinScale()
	 */
	public double getMinScale() {
		return minScale;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getMaxScale()
	 */
	public double getMaxScale() {
		return maxScale;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setMinScale(double)
	 */
	public void setMinScale(double minScale) {
		if (this.minScale != minScale){
			this.minScale = minScale;
			this.updateDrawVersion();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setMaxScale(double)
	 */
	public void setMaxScale(double maxScale) {
		if (this.maxScale != maxScale){
			this.maxScale = maxScale;
			this.updateDrawVersion();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isWithinScale(double)
	 */
	public boolean isWithinScale(double scale) {

		boolean bVisible = true;
		if (getMinScale() != -1) {
			if (scale < getMinScale()){
				bVisible = false;
			}
		}
		if (getMaxScale() != -1) {
			if (scale > getMaxScale()) {
				bVisible = false;
			}
		}

		return bVisible;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setEditing(boolean)
	 */
	public void setEditing(boolean b) throws StartEditionLayerException {
		status.editing = b;
	}
	/**
	 * Called by some version of the method {@linkplain #setEditing(boolean)} overwritten. Notifies
	 *  all listeners associated to this layer, that its edition state has changed.
	 *
	 * @param e a layer event with the name of the property that has changed
	 *
	 * @see #setEditing(boolean)
	 */
	protected void callEditionChanged(LayerEvent e) {
		for (Iterator iter = layerListeners.iterator(); iter.hasNext();) {
			LayerListener listener = (LayerListener) iter.next();

			listener.editionChanged(e);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isEditing()
	 */
	public boolean isEditing() {
		return status.editing;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getTocImageIcon()
	 */
	public ImageIcon getTocImageIcon() {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isInTOC()
	 */
	public boolean isInTOC() {
		return status.inTOC;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setInTOC(boolean)
	 */
	public void setInTOC(boolean b) {
		status.inTOC=b;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isAvailable()
	 */
	public boolean isAvailable() {
		return status.available;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setAvailable(boolean)
	 */
	public void setAvailable(boolean available) {
		if (status.available != available){
			status.available = available;
			this.updateDrawVersion();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#reload()
	 */
	public void reload() throws ReloadLayerException {
		this.setAvailable(true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getFLayerStatus()
	 */
	public FLayerStatus getFLayerStatus(){
		return status.cloneStatus();
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#setFLayerStatus(com.iver.cit.gvsig.fmap.layers.FLayerStatus)
	 */
	public void setFLayerStatus(FLayerStatus status){
		if (!this.status.equals(status)){
			this.status = status;
			this.updateDrawVersion();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isOk()
	 */

	public boolean isOk(){
		return status.isOk();
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getNumErrors()
	 */
	public int getNumErrors(){
		return status.getNumErrors();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getError(int)
	 */
	public BaseException getError(int i){
		return status.getError(i);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getErrors()
	 */
	public List getErrors(){
		return status.getErrors();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#addError(BaseException)
	 */
	public void addError(BaseException exception){
		status.addLayerError(exception);
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#visibleRequired()
	 */
	public boolean visibleRequired() {
		return status.visible;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getInfoString()
	 */
	public String getInfoString() {
		return null;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#isWritable()
	 */
	public boolean isWritable() {
		return status.writable;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#cloneLayer()
	 */
	public FLayer cloneLayer() throws Exception {
		return this;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getTocStatusImage()
	 */
	public Image getTocStatusImage() {
		return tocStatusImage;
	}

	/**
	 * Inserts the image icon that will be shown in the TOC next to this layer, according its status.
	 *
	 * @param tocStatusImage the image
	 *
	 * @see #getTocStatusImage()
	 */
	public void setTocStatusImage(Image tocStatusImage) {
		this.tocStatusImage = tocStatusImage;
		logger.debug("setTocStatusImage " + tocStatusImage + " sobre capa " + this.getName());
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#newComposedLayer()
	 */
	public ComposedLayer newComposedLayer() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#allowLinks()
	 */
	public boolean allowLinks()
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getLinkProperties()
	 */
	public AbstractLinkProperties getLinkProperties()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getLink(java.awt.geom.Point2D, double)
	 */
	public URI[] getLink(Point2D point, double tolerance) throws ReadException{
		return null;
	}

	/**
	 * @see LayerChangeSupport#addLayerListener(LegendListener)
	 */
	public void addLegendListener(LegendListener listener) {
		layerChangeSupport.addLayerListener(listener);
	}

	/**
	 * @see LayerChangeSupport#callLegendChanged(LegendChangedEvent)
	 */
	protected void callLegendChanged(LegendChangedEvent e) {
		layerChangeSupport.callLegendChanged(e);
		if(parentLayer != null) {
			parentLayer.callLegendChanged(e);
		}
	}

	/**
	 * @see LayerChangeSupport#removeLayerListener(LegendListener)
	 */
	public void removeLegendListener(LegendListener listener) {
		layerChangeSupport.removeLayerListener(listener);
	}
	public String getClassName() {
		return this.getClass().getName();
	}

	public void delegate(DynObject dynObject) {
		this.delegatedDynObject.delegate(dynObject);
	}

	public DynClass getDynClass() {
		return this.delegatedDynObject.getDynClass();
	}

	public Object getDynValue(String name) throws DynFieldNotFoundException {
		return this.delegatedDynObject.getDynValue(name);
	}

	public boolean hasDynValue(String name) {
		return this.delegatedDynObject.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		this.delegatedDynObject.implement(dynClass);
	}

	public Object invokeDynMethod(int code, DynObject context)
	throws DynMethodException {
		return this.delegatedDynObject.invokeDynMethod(this, code, context);
	}

	public Object invokeDynMethod(String name, DynObject context)
	throws DynMethodException {
		return this.delegatedDynObject.invokeDynMethod(this, name, context);
	}

	public void setDynValue(String name, Object value)
	throws DynFieldNotFoundException {
		this.delegatedDynObject.setDynValue(name, value);
	}

	public long getDrawVersion() {
		return this.drawVersion;
	}

	protected void updateDrawVersion(){
		this.drawVersion++;
		this.callDrawValueChanged(LayerEvent.createDrawValuesChangedEvent(this, ""));
		if (this.parentLayer != null){
			this.parentLayer.updateDrawVersion();
		}
	}

	public boolean hasChangedForDrawing(long value){
		return this.drawVersion > value;
	}

	public void activationChanged(LayerEvent e) {
	}

	public void drawValueChanged(LayerEvent e) {
		this.updateDrawVersion();
	}

	public void editionChanged(LayerEvent e) {

	}

	public void nameChanged(LayerEvent e) {

	}

	public void visibilityChanged(LayerEvent e) {

	}


}
