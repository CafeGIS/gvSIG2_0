package org.gvsig.gvsig3d.map3d.layers;

import org.gvsig.gvsig3d.map3d.MapContext3D;

import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent;
import com.iver.cit.gvsig.fmap.layers.LayerPositionEvent;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.utiles.XMLEntity;

public class FLayers3D extends FLayers  {

	// protected static Logger logger =
	// Logger.getLogger(FLayers.class.getName());

	/**
	 * Constructor that can be used to create a MapContext containing the
	 * FLayers3D
	 * 
	 * @param fmap
	 *            MapContext (can be null)
	 * @param parent
	 *            FLayers (can be null)
	 */
	public FLayers3D(MapContext fmap, FLayers parent, ViewPort vp) {

		super();
		this.setMapContext(fmap);
		this.setParentLayer(parent);
//		super(fmap, parent);
		
		// add 3D properties, mainly to keep track of hooked state
		Layer3DProps props3D = new Layer3DProps();
		props3D.setLayer(this);
		//props3D.initCacheName(m_planet.getType(), m_viewProjection);
		setProperty("3DLayerExtension", props3D);
		
		if (fmap == null) {
			MapContext newMap = new MapContext3D(this, vp);
			this.fmap = newMap;
			props3D.setHooked(true);
		} else {
			fmap.setViewPort(vp);
		}
	}

	// overrides for layer collection events

	protected void callLayerAdded(LayerCollectionEvent event) {
		if (isHooked()) {
		    FLayer layer = event.getAffectedLayer();
		    ((MapContext3D)fmap).layerAdded(this, layer);
		}
		super.callLayerAdded(event);
	}

	protected void callLayerRemoved(LayerCollectionEvent event) {
		if (isHooked()) {
			FLayer layer = event.getAffectedLayer();
			((MapContext3D)fmap).layerRemoved(this, layer);
		}
		super.callLayerRemoved(event);
	}

	protected void callLayerMoved(LayerPositionEvent event) {
		super.callLayerMoved(event);
		if (isHooked()) 
		    ((MapContext3D)fmap).layerMoved(this, event.getAffectedLayer(),
		    		                        event.getOldPos(), event.getNewPos());
	}

	public boolean isHooked() {
		Layer3DProps props3D = Layer3DProps.getLayer3DProps(this);
		if (((MapContext3D)fmap).getLayers()==this) // this is the root FLayers3D
			props3D.setHooked(true);

		return props3D.getHooked();
	}
	
	// Object persistence

	/**
	 * Devuelve el XMLEntity a partir del objeto.
	 * 
	 * @return XMLEntity.
	 * @throws XMLException
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = super.getXMLEntity();

		// Overwrite class name with FLayers' name to avoid problems when opening projects
		// This means FLayers3D will be persisted as FLayers but it will instanced as
		// FLayers3D by getNewGroupLayer if added to a MapContext3D
		xml.putProperty("className", FLayers.class.getName());

		return xml;
	}

	/**
	 * Inserta los valores de los atributos del XMLEntity al objeto.
	 * 
	 * @param xml
	 *            XMLEntity.
	 * 
	 * @throws XMLException
	 * @throws DriverException
	 * @throws DriverIOException
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException {
		if (((MapContext3D)fmap).getLayers()==this) // this is the root FLayers3D
		    ((MapContext3D)fmap).setLoading(true);
		
		super.setXMLEntity(xml);		
		
		if (((MapContext3D)fmap).getLayers()==this) // this is the root FLayers3D
			((MapContext3D)fmap).setLoading(false);
	}

}
