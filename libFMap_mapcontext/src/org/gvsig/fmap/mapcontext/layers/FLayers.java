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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.Messages;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.XMLLayerException;
import org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer;
import org.gvsig.fmap.mapcontext.layers.operations.ILabelable;
import org.gvsig.fmap.mapcontext.layers.operations.InfoByPoint;
import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;
import org.gvsig.fmap.mapcontext.layers.operations.LayersVisitable;
import org.gvsig.fmap.mapcontext.layers.operations.LayersVisitor;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.gvsig.tools.exception.BaseException;
import org.gvsig.tools.task.Cancellable;
import org.gvsig.tools.visitor.NotSupportedOperationException;
import org.gvsig.tools.visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * <p>Represents a generic collection of layers, that can be represented as a node in a tree of nodes of layers.</p>
 *
 * <p>Adapts the basic functionality implemented for a layer in the abstract class <code>FLyrDefault</code>, to
 *  a collection of layers, implementing, as well, specific methods for this kind of object, defined in the
 *  interfaces <code>VectorialData</code>, <code>LayerCollection</code>, and <code>InfoByPoint</code>.</p>
 *
 * @see FLyrDefault
 */
public class FLayers extends FLyrDefault implements LayerCollection,
InfoByPoint {
	/**
	 * List with all listeners registered for this kind of node.
	 *
	 * @see #addLayerCollectionListener(LayerCollectionListener)
	 * @see #removeLayerCollectionListener(LayerCollectionListener)
	 * @see #callLayerAdded(LayerCollectionEvent)
	 * @see #callLayerAdding(LayerCollectionEvent)
	 * @see #callLayerMoved(LayerPositionEvent)
	 * @see #callLayerMoving(LayerPositionEvent)
	 * @see #callLayerRemoved(LayerCollectionEvent)
	 * @see #callLayerRemoving(LayerCollectionEvent)
	 */
	protected ArrayList layerCollectionListeners = new ArrayList();

	/**
	 * A synchronized list with the layers.
	 *
	 * @see #setAllVisibles(boolean)
	 * @see #addLayer(FLayer)
	 * @see #addLayer(int, FLayer)
	 * @see #moveTo(int, int)
	 * @see #removeLayer(FLayer)
	 * @see #removeLayer(int)
	 * @see #removeLayer(String)
	 * @see #replaceLayer(String, FLayer)
	 * @see #getVisibles()
	 * @see #getLayer(int)
	 * @see #getLayer(String)
	 * @see #getLayersCount()
	 * @see #getFullEnvelope()
	 */
	protected List layers = Collections.synchronizedList(new ArrayList());

	/**
	 * The model of the layer.
	 *
	 * @see #getMapContext()
	 */
	protected MapContext fmap;

	/**
	 * Useful for debug the problems during the implementation.
	 */
	final static private Logger logger = LoggerFactory.getLogger(FLayers.class);

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#addLayerCollectionListener(com.iver.cit.gvsig.fmap.layers.LayerCollectionListener)
	 */
	public void addLayerCollectionListener(LayerCollectionListener listener) {
		if (!layerCollectionListeners.contains(listener)) {
			layerCollectionListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#setAllVisibles(boolean)
	 */
	public void setAllVisibles(boolean visible) {
		FLayer lyr;

		for (int i = 0; i < layers.size(); i++) {
			lyr = ((FLayer) layers.get(i));
			lyr.setVisible(visible);

			if (lyr instanceof LayerCollection) {
				((LayerCollection) lyr).setAllVisibles(visible);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#removeLayerCollectionListener(com.iver.cit.gvsig.fmap.layers.LayerCollectionListener)
	 */
	public void removeLayerCollectionListener(LayerCollectionListener listener) {
		layerCollectionListeners.remove(listener);
	}

	/**
	 * Adds a layer on an specified position in this node.
	 *
	 * @param pos position in the inner list where the layer will be added
	 * @param layer a layer
	 */
	private void doAddLayer(int pos,FLayer layer) {
		layers.add(pos,layer);
		layer.setParentLayer(this);
		IProjection layerProj = layer.getProjection();
		if(layerProj != null && fmap != null) {
			IProjection mapContextProj = fmap.getProjection();
			// TODO REVISAR ESTO !!!!
			// Esta condición puede que no fuese exacta para todos los casos
			if (!layerProj.getAbrev().equals(mapContextProj.getAbrev())) {
				ICoordTrans ct = layerProj.getCT(mapContextProj);
				layer.setCoordTrans(ct);
			} else {
				layer.setCoordTrans(null);
			}
		}
		this.updateDrawVersion();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#addLayer(com.iver.cit.gvsig.fmap.layers.FLayer)
	 */
	public void addLayer(FLayer layer) {
		addLayer(layers.size(),layer);
	}

	/**
	 * Adds a layer in an specified position in this node.
	 *
	 * @param layer a layer
	 */
	public void addLayer(int pos,FLayer layer) {
		try {
			//Notificamos a la capa que va a ser añadida
			if (layer instanceof FLyrDefault) {
				((FLyrDefault)layer).wakeUp();
			}

			if (layer instanceof FLayers){
				FLayers layers=(FLayers)layer;
				fmap.addAsCollectionListener(layers);
			}
			callLayerAdding(LayerCollectionEvent.createLayerAddingEvent(layer));

			doAddLayer(pos,layer);

			callLayerAdded(LayerCollectionEvent.createLayerAddedEvent(layer));
		} catch (CancelationException e) {
			logger.warn(e.getMessage());
		} catch (LoadLayerException e) {
			layer.setAvailable(false);
			layer.addError(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#moveTo(int, int)
	 */
	public void moveTo(int from, int to) throws CancelationException {
		int newfrom=layers.size()-from-1;
		int newto=layers.size()-to-1;
		if ( newfrom < 0 || newfrom >=layers.size() || newto < 0 || newto >= layers.size()) {
			return;
		}
		FLayer aux = (FLayer) layers.get(newfrom);
		callLayerMoving(LayerPositionEvent.createLayerMovingEvent(aux, newfrom, newto));
		layers.remove(newfrom);
		layers.add(newto, aux);
		this.updateDrawVersion();
		callLayerMoved(LayerPositionEvent.createLayerMovedEvent(aux, newfrom, newto));
	}

	/**
	 * Removes an inner layer.
	 *
	 * @param lyr a layer
	 */
	private void doRemoveLayer(FLayer lyr) {
		layers.remove(lyr);
		this.updateDrawVersion();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#removeLayer(com.iver.cit.gvsig.fmap.layers.FLayer)
	 */
	public void removeLayer(FLayer lyr) throws CancelationException {
		callLayerRemoving(LayerCollectionEvent.createLayerRemovingEvent(lyr));
		doRemoveLayer(lyr);
		callLayerRemoved(LayerCollectionEvent.createLayerRemovedEvent(lyr));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#removeLayer(int)
	 */
	public void removeLayer(int idLayer) {
		FLayer lyr = (FLayer) layers.get(idLayer);
		callLayerRemoving(LayerCollectionEvent.createLayerRemovingEvent(lyr));
		layers.remove(idLayer);
		this.updateDrawVersion();
		callLayerRemoved(LayerCollectionEvent.createLayerRemovedEvent(lyr));
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#removeLayer(java.lang.String)
	 */
	public void removeLayer(String layerName) {
		FLayer lyr;

		for (int i = 0; i < layers.size(); i++) {
			lyr = ((FLayer) layers.get(i));

			if (lyr.getName().compareToIgnoreCase(layerName) == 0) {
				removeLayer(i);

				break;
			}
		}
	}

	/**
	 * Replace a layer identified by its name, by another.
	 *
	 * @param layerName the name of the layer to be replaced
	 * @param layer the new layer
	 */
	public void replaceLayer(String layerName, FLayer layer) throws LoadLayerException
	{
		FLayer lyr;
		FLayer parent;
		for (int i = 0; i < layers.size(); i++) {
			lyr = ((FLayer) layers.get(i));

			if (lyr.getName().compareToIgnoreCase(layerName) == 0) {
				parent = lyr.getParentLayer();
				removeLayer(i);
				if (parent != null) {
					//Notificamos a la capa que va a ser añadida
					if (layer instanceof FLyrDefault) {
						((FLyrDefault)layer).wakeUp();
					}
				}

				if (layer instanceof FLayers){
					FLayers layers=(FLayers)layer;
					fmap.addAsCollectionListener(layers);
				}
				callLayerAdding(LayerCollectionEvent.createLayerAddingEvent(layer));

				layers.add(i,layer);
				layer.setParentLayer(this);

				callLayerAdded(LayerCollectionEvent.createLayerAddedEvent(layer));
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#getVisibles()
	 */
	public FLayer[] getVisibles() {
		ArrayList array = new ArrayList();
		LayersIterator iter = new LayersIterator(this) {
			public boolean evaluate(FLayer layer) {
				return layer.isVisible();
			}

		};

		while (iter.hasNext()) {
			array.add(iter.nextLayer());
		}

		return (FLayer[]) array.toArray(new FLayer[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#getLayer(int)
	 */
	public FLayer getLayer(int index) {
		return (FLayer) layers.get(index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#getLayer(java.lang.String)
	 */
	public FLayer getLayer(String layerName) {
		FLayer lyr;
		FLayer lyr2;
		ArrayList layerList;

		for (int i = 0; i < layers.size(); i++) {
			lyr = ((FLayer) layers.get(i));

			if (lyr.getName().compareToIgnoreCase(layerName) == 0) {
				return lyr;
			}

			layerList = new ArrayList();
			splitLayerGroup(lyr,layerList);
			for(int j = 0; j<layerList.size(); j++ )
			{
				lyr2 = ((FLayer)layerList.get(j));
				if (lyr2.getName().compareToIgnoreCase(layerName) == 0) {
					return lyr2;
				}
			}
		}

		return null;
	}

	/**
	 * <p> Splits up a layer group in order to get a layer by name when there are layer groups</p>
	 *
	 * <p>In <code>result</code> always will be at least one layer.</p>
	 *
	 * @param layer the layer we are looking for
	 * @param result an array list that will have the results of the search
	 */
	private void splitLayerGroup(FLayer layer, ArrayList result)
	{
		int i;
		FLayers layerGroup;
		if (layer instanceof FLayers)
		{
			layerGroup = (FLayers)layer;
			for (i=0; i < layerGroup.getLayersCount(); i++ )
			{
				splitLayerGroup(layerGroup.getLayer(i),result);
			}
		}
		else
		{
			result.add(layer);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#getLayersCount()
	 */
	public int getLayersCount() {
		return layers.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#draw(java.awt.image.BufferedImage, java.awt.Graphics2D, com.iver.cit.gvsig.fmap.ViewPort, com.iver.utiles.swing.threads.Cancellable, double)
	 */
	public void draw(BufferedImage image, Graphics2D g, ViewPort viewPort,
			Cancellable cancel, double scale) throws ReadException {
		// FIXME Arreglar este error
		throw new RuntimeException("Esto no deberia de llamarse");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#print(java.awt.Graphics2D,
	 * com.iver.cit.gvsig.fmap.ViewPort,
	 * com.iver.utiles.swing.threads.Cancellable, double,
	 * javax.print.attribute.PrintAttributes)
	 */
	public void print(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale, PrintAttributes properties)
	throws ReadException {
		throw new RuntimeException("No deberia pasar por aqui");
	}

	public void print_old(Graphics2D g, ViewPort viewPort, Cancellable cancel,
			double scale, PrintAttributes properties)
	throws ReadException {
		this.print_old(g, viewPort, cancel, scale, properties, null);
	}

	/**
	 * <p>Checks all layers (each one as a sub-node of this node <i>collection of layers</i>) of this collection and draws their requested properties. If a node is
	 *  a group of layers (<code>ComposedLayer</code>), executes it's drawn.</p>
	 *
	 * <p>All nodes which could group with the composed layer <code>group</code>, will be drawn together. And once the <code>
	 * group</code> is drawn, will be set to <code>null</code> if hasn't a parent layer.</p>
	 *
	 * <p>The particular implementation depends on the kind of each layer and composed layer. And this process can be cancelled at any
	 *  time by the shared object <code>cancel</code>.</p>
	 *
	 * <p>According the print quality, labels will be printed in different resolution:
	 *  <ul>
	 *   <li><b>PrintQuality.DRAFT</b>: 72 dpi (dots per inch).</li>
	 *   <li><b>PrintQuality.NORMAL</b>: 300 dpi (dots per inch).</li>
	 *   <li><b>PrintQuality.HIGH</b>: 600 dpi (dots per inch).</li>
	 *  </ul>
	 * </p>
	 *
	 * @param g for rendering 2-dimensional shapes, text and images on the Java(tm) platform
	 * @param viewPort the information for drawing the layers
	 * @param cancel shared object that determines if this layer can continue being drawn
	 * @param scale the scale of the view. Must be between {@linkplain FLayer#getMinScale()} and {@linkplain FLayer#getMaxScale()}.
	 * @param properties properties that will be print
	 * @param group a composed layer pending to paint; if this parameter is <code>null</code>, the composed layer
	 *
	 * @return <code>null</code> if the layers in <code>group</code> had been drawn or were <code>null</code>; otherwise, the <code>group</code>
	 *
	 * @see FLayer#print(Graphics2D, ViewPort, Cancellable, double, PrintAttributes)
	 *
	 * @throws ReadDriverException if fails the driver reading the data.
	 */
	public ComposedLayer print_old(Graphics2D g, ViewPort viewPort, Cancellable cancel, double scale, PrintAttributes properties, ComposedLayer group)
	throws ReadException {
		double dpi = 72;

		int resolution = properties.getPrintQuality();
		if (resolution == PrintAttributes.PRINT_QUALITY_NORMAL) {
			dpi = 300;
		} else if (resolution == PrintAttributes.PRINT_QUALITY_HIGH){
			dpi = 600;
		} else if (resolution == PrintAttributes.PRINT_QUALITY_DRAFT){
			dpi = 72;
		}

		// TODO: A la hora de imprimir, isWithinScale falla, porque está
		// calculando la escala en pantalla, no para el layout.
		// Revisar esto.

		// TODO: We have to check when we have to call the drawLabels method when exists a ComposedLayer group.
		for (int i=0; i < layers.size(); i++) {
			FLayer lyr = (FLayer) layers.get(i);
			if (!lyr.isVisible() || !lyr.isWithinScale(scale)) {
				continue;
			}

			try{

				///// CHEMA ComposedLayer
				// Checks for draw group (ComposedLayer)
				if (group != null) {
					if (lyr instanceof FLayers){
						group = ((FLayers)lyr).print_old(g, viewPort, cancel,scale,properties,group);
					} else {
						// If layer can be added to the group, does it
						if (lyr instanceof ILabelable
								&& ((ILabelable) lyr).isLabeled()
								&& ((ILabelable) lyr).getLabelingStrategy() != null
								&& ((ILabelable) lyr).getLabelingStrategy().shouldDrawLabels(scale)) {
							group.add(lyr);
						} else {
							// draw the 'pending to draw' layer group
							group.print(g,viewPort,cancel,scale,properties);

							// gets a new group instance
							if (lyr instanceof ILabelable
									&& ((ILabelable) lyr).isLabeled()
									&& ((ILabelable) lyr).getLabelingStrategy() != null
									&& ((ILabelable) lyr).getLabelingStrategy().shouldDrawLabels(scale)) {
								group = lyr.newComposedLayer();
							} else {
								group = null;
							}
							// if layer hasn't group, draws it inmediately
							if (group == null) {
								if (lyr instanceof FLayers){
									group = ((FLayers)lyr).print_old(g, viewPort, cancel,scale,properties,group);
								} else {
									lyr.print(g, viewPort, cancel,scale,properties);
									if (lyr instanceof ILabelable
											&& ((ILabelable) lyr).isLabeled()
											&& ((ILabelable) lyr).getLabelingStrategy() != null
											&& ((ILabelable) lyr).getLabelingStrategy().shouldDrawLabels(scale)) {
										ILabelable lLayer = (ILabelable) lyr;
										lLayer.drawLabels(null, g, viewPort, cancel, scale, dpi);
									}
								}
							} else {
								// add the layer to the group
								group.setMapContext(fmap);
								group.add(lyr);

							}

						}
					}
				} else {
					// gets a new group instance
					group = lyr.newComposedLayer();
					// if layer hasn't group, draws it inmediately
					if (group == null) {
						if (lyr instanceof FLayers){
							group = ((FLayers)lyr).print_old(g, viewPort, cancel,scale,properties,group);
						} else {
							lyr.print(g, viewPort, cancel,scale,properties);
							if (lyr instanceof ILabelable && ((ILabelable) lyr).isLabeled()) {
								ILabelable lLayer = (ILabelable) lyr;

								lLayer.drawLabels(null, g, viewPort, cancel, scale, dpi);
							}
						}
					} else {
						// add the layer to the group
						group.setMapContext(fmap);
						group.add(lyr);

					}
				}
				///// CHEMA ComposedLayer

			} catch (Exception e){
				String mesg = Messages.getString("error_printing_layer")+" "+ lyr.getName() + ": " + e.getMessage();
				fmap.addLayerError(mesg);
				logger.error(mesg, e);
			}

		}

		///// CHEMA ComposedLayer
		if (group != null && this.getParentLayer() == null) {
			//si tenemos un grupo pendiente de pintar, pintamos
			group.print(g, viewPort, cancel,scale,properties);
			group = null;

		}
		///// CHEMA ComposedLayer

		//		if (getVirtualLayers() != null) {
		//			getVirtualLayers().print( g, viewPort, cancel, scale, properties);
		//		}

		///// CHEMA ComposedLayer
		return group;
		///// CHEMA ComposedLayer
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLayer#getFullExtent()
	 */
	public Envelope getFullEnvelope() {
		Envelope rAux = null;
		boolean first = true;


		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			FLayer capa = (FLayer) iter.next();
			try{
				if (first) {
					rAux = capa.getFullEnvelope();
					first=false;
				} else {
					rAux.add(capa.getFullEnvelope());
				}
			}catch (Exception e) {
				e.printStackTrace();//TODO hay que revisar para determinar el comportamiento que espera el usuario.
			}
		}

		return rAux;
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that another layer is going to be added or replaced in the internal
	 *  list of layers.
	 *
	 * @param e a layer collection event with the new layer
	 */
	protected void callLayerAdding(LayerCollectionEvent event)
	throws CancelationException {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerAdding(event);
		}
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that a layer is going to be removed from the internal list of layers.
	 *
	 * @param event a layer collection event with the layer being removed
	 *
	 * @throws CancelationException any exception produced during the cancellation of the driver.
	 */
	protected void callLayerRemoving(LayerCollectionEvent event)
	throws CancelationException {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerRemoving(event);
		}
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that a layer is going to be moved in the internal list of layers.
	 *
	 * @param event a layer collection event with the layer being moved, and the initial and final positions
	 *
	 * @throws CancelationException any exception produced during the cancellation of the driver.
	 */
	protected void callLayerMoving(LayerPositionEvent event)
	throws CancelationException {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerMoving(event);
		}
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that another layer has been added or replaced in the internal
	 *  list of layers.
	 *
	 * @param e a layer collection event with the new layer
	 */
	protected void callLayerAdded(LayerCollectionEvent event) {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerAdded(event);
		}
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that another layer has been removed from the internal list of layers.
	 *
	 * @param e a layer collection event with the layer removed
	 */
	protected void callLayerRemoved(LayerCollectionEvent event) {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerRemoved(event);
		}
	}

	/**
	 * Notifies all listeners associated to this collection of layers,
	 *  that another layer has been moved in the internal list of layers.
	 *
	 * @param e a layer collection event with the layer moved, and the initial and final positions
	 */
	protected void callLayerMoved(LayerPositionEvent event) {
		ArrayList aux = (ArrayList) layerCollectionListeners.clone();
		for (Iterator iter = aux.iterator(); iter.hasNext();) {
			((LayerCollectionListener) iter.next()).layerMoved(event);
		}
	}

	/**
	 * <p>Returns an entity that represents this collection of layers stored as a tree-node with children that are also layers.</p>
	 *
	 * <p>The root node has the same properties that <code>FlyrDefault#getXMLEntity()</code> returns, and adds:
	 *  <ul>
	 * 	 <li> <i>numLayers</i> : number of layers of this collection (direct children of this node)
	 *   <li> <i>LayerNames</i> : an array list with the name of the layers of this collection (direct children of this node)
	 *    <code>FLayer.getXMLEntity()</code>
	 *  </ul>
	 * </p>
	 *
	 * <p>All XML elements returned represent the information about this layer.</p>
	 *
	 * @return an XML entity with information to this collection of layers
	 * @throws XMLException if there is any error creating the XML from the layers.
	 */
	public XMLEntity getXMLEntity() throws XMLException {
		XMLEntity xml = super.getXMLEntity();
		xml.putProperty("numLayers", layers.size());

		String[] s = new String[layers.size()];

		for (int i = 0; i < layers.size(); i++) {
			s[i] = ((FLayer) layers.get(i)).getName();
		}

		xml.putProperty("LayerNames", s);

		for (int i = 0; i < layers.size(); i++) {
			try {
				XMLEntity xmlLayer=((FLayer) layers.get(i)).getXMLEntity();
				xmlLayer.putProperty("tagName", "layer");
				xml.addChild(xmlLayer);
			}catch (XMLException e) {
				e.printStackTrace();
			}
		}

		return xml;
	}

	/**
	 * <p>Inserts layers and properties to this collection of layers.</p>
	 *
	 * <p>This root node has the same properties that return <code>FlyrDefault#getXMLEntity()</code> adding:
	 *  <ul>
	 * 	 <li> <i>numLayers</i> : number of first-level layers of this collection
	 *   <li> <i>LayerNames</i> : an array list with the name of the first-level layers of this collection
	 *    <code>FLayer.getXMLEntity()</code>
	 *  </ul>
	 * </p>
	 *
	 * @see FLyrDefault#setXMLEntity()
	 * @see FLyrDefault#getXMLEntity()
	 * @see CopyOfFLayers#addLayerFromXML(XMLEntity, String)
	 *
	 * @param xml an <code>XMLEntity</code> with the information
	 *
	 * @throws XMLException if there is an error setting the object.
	 */
	public void setXMLEntity(XMLEntity xml) throws XMLException{
		super.setXMLEntity(xml);
		//LoadLayerException loadLayerException=new LoadLayerException();

		String[] s = xml.getStringArrayProperty("LayerNames");
		// try {
		fmap.clearErrors();
		int numLayers=xml.getIntProperty("numLayers");
		Iterator iter = xml.findChildren("tagName","layer");
		XMLEntity xmlLayer;
		while (iter.hasNext()) {
			xmlLayer=(XMLEntity) iter.next();
			try {
				this.addLayerFromXML(xmlLayer, null);
			} catch (LoadLayerException e) {
				throw new XMLLayerException(getName(),e);
			}
		}
		if (numLayers != this.layers.size()) {
			logger.warn(this.getName() + ": layer count no match");
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getMapContext()
	 */
	public MapContext getMapContext() {
		return fmap;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setCoordTrans(org.cresques.cts.ICoordTrans)
	 */
	public void setCoordTrans(ICoordTrans ct) {
		super.setCoordTrans(ct);

		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			FLayer layer = (FLayer) iter.next();
			layer.setCoordTrans(ct);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#setAllActives(boolean)
	 */
	public void setAllActives(boolean active) {
		FLayer lyr;

		for (int i = 0; i < layers.size(); i++) {
			lyr = ((FLayer) layers.get(i));
			lyr.setActive(active);

			if (lyr instanceof LayerCollection) {
				((LayerCollection) lyr).setAllActives(active);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection#getActives()
	 */
	public FLayer[] getActives() {
		ArrayList ret = new ArrayList();
		LayersIterator it = new LayersIterator(this) {

			public boolean evaluate(FLayer layer) {
				return layer.isActive();
			}

		};

		while (it.hasNext())
		{
			ret.add(it.next());
		}
		return (FLayer[]) ret.toArray(new FLayer[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getMinScale()
	 */
	public double getMinScale() {
		return -1; // La visibilidad o no la controla cada capa
		// dentro de una colección
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getMaxScale()
	 */
	public double getMaxScale() {
		return -1;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setMinScale(double)
	 */
	public void setMinScale(double minScale)
	{
		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			FLayer lyr = (FLayer) iter.next();
			lyr.setMinScale(minScale);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setMaxScale(double)
	 */
	public void setMaxScale(double maxScale)
	{
		for (Iterator iter = layers.iterator(); iter.hasNext();) {
			FLayer lyr = (FLayer) iter.next();
			lyr.setMinScale(maxScale);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#setActive(boolean)
	 */
	public void setActive(boolean b){
		super.setActive(b);
		for (int i=0;i<layers.size();i++){
			((FLayer)layers.get(i)).setActive(b);
		}
	}
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#addLayerListener(com.iver.cit.gvsig.fmap.layers.LayerListener)
	 */
	public boolean addLayerListener(LayerListener o) {
		for (int i = 0; i < layers.size(); i++) {
			((FLayer) layers.get(i)).addLayerListener(o);
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.layerOperations.InfoByPoint#getInfo(java.awt.Point, double, com.iver.utiles.swing.threads.Cancellable)
	 */
	public XMLItem[] getInfo(Point p, double tolerance, Cancellable cancel) throws LoadLayerException, DataException {
		int i;
		Vector items = new Vector();
		FLayer layer;
		XMLItem[] aux;
		for (i = 0; i < this.layers.size(); i++){
			layer = (FLayer)layers.get(i);
			if (layer instanceof InfoByPoint){
				InfoByPoint queryable_layer = (InfoByPoint) layer;
				aux = queryable_layer.getInfo(p, tolerance, null);
				if (!(queryable_layer instanceof FLayers)){
					for(int j = 0; j < aux.length; j++){
						items.add(aux[j]);
					}
				}
			}
		}
		return (XMLItem[])items.toArray(new XMLItem[0]);

	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.layers.FLyrDefault#getTocImageIcon()
	 */
	public ImageIcon getTocImageIcon() {
		return new ImageIcon(MapContext.class.getResource("images/layerGroup.png"));
	}

	/**
	 * <p>Adds the layer with the information in an XML entity and the specified name, to this collection of layers. And
	 *  returns <code>true</code> if there hasn't been errors.</p>
	 *
	 * @see CopyOfFLayers#addLayerFromXML(XMLEntity, String)
	 *
	 * @param xml tree-node structure with information about layers
	 * @param name name of the layer to add
	 * @return <code>true</code> if there were no errors adding the layer, <code>false</code> otherwise
	 *
	 * @throws LoadLayerException if fails loading this layer.
	 */
	//	public boolean addLayerFromXMLEntity(XMLEntity xml, String name) throws LoadLayerException {
	//		fmap.clearErrors();
	//		this.addLayerFromXML(xml,name);
	//
	//		return (fmap.getLayersError().size() == 0);
	//
	//	}

	/**
	 * <p>Adds the layer with the information in an XML entity and the specified name, to this collection of layers.</p>
	 *
	 * <p>This method really executes the addition, considering the kind of layer (<code>FLyrVect</code>,
	 *  <code>FLyrAnnotation</code>, <code>FLyrRaster</code>, a collection of layers (<code>FLayers</code>),
	 *  or another kind of layer (<code>FLayer</code>)), and the driver in the layer.</p>
	 *
	 * @param xml tree-node structure with information about layers
	 * @param name name of the layer to add
	 *
	 * @throws LoadLayerException if fails loading this layer.
	 */
	private void addLayerFromXML(XMLEntity xml, String name) throws LoadLayerException {
		FLayer layer = null;

		try {
			if (name == null) {
				name = xml.getName();
			}


			String className = xml.getStringProperty("className");
			Class clase = Class.forName(className);
			layer = (FLayer) clase.newInstance();
			if (FLayers.class.isAssignableFrom(clase)) {
				((FLayers)layer).setMapContext(getMapContext());
				((FLayers)layer).setParentLayer(this);
				//			layer = new FLayers(getMapContext(),this);
				layer.setXMLEntity(xml);
			} else {
				// Capas Nuevas (externas)
				layer.setName(name);
				layer.setXMLEntity(xml);
				layer.load();
			}

			//		//TODO VCN FLyrAnnotation es un parche para no tener que duplicar todo el código de aqí y de los diferentes métodos de LayerFactory,
			//		//ya que los drivers de una FLyrAnnotation no sabemos cual es puede ser cualquier Driver Vectorial.
			//		if (className.equals(FLyrVect.class.getName())){// || className.equals(FLyrAnnotation.class.getName())) {
			//			String type = xml.getStringProperty("type");
			//			if ("vectorial".equals(type)){
			//				//String recordsetName = xml.getChild(i).getStringProperty("recordset-name");
			//				IProjection proj = null;
			//				if (xml.contains("proj")) {
			//					proj = CRSFactory.getCRS(xml.getStringProperty("proj"));
			//				}
			//				else
			//				{
			//					proj = this.getMapContext().getViewPort().getProjection();
			//				}
			//				if (xml.contains("file")) {
			//					Driver d;
			//					try {
			//						d = LayerFactory.getDM().getDriver(xml.getStringProperty("driverName"));
			//					} catch (DriverLoadException e1) {
			//						throw new DriverLayerException(name,e1);
			//					}
			//					layer = LayerFactory.createLayer(name, (VectorialFileDriver) d,
			//							new File(xml.getStringProperty("file")),
			//							proj);
			//
			//
			//				}
			//				if (xml.contains("db")) {
			//
			//					String driverName = xml.getStringProperty("db");
			//					IVectorialDatabaseDriver driver;
			//					try {
			//						driver = (IVectorialDatabaseDriver) LayerFactory.getDM().getDriver(driverName);
			//						//Hay que separar la carga de los datos del XMLEntity del load.
			//						driver.setXMLEntity(xml.getChild(2));
			//
			//						boolean loadOk = false;
			//						((DefaultJDBCDriver)driver).load();
			//						if (((DefaultJDBCDriver)driver).getConnection() != null) {
			//							loadOk = true;
			//						}
			//						layer = LayerFactory.createDBLayer(driver, name, proj);
			//						if (!loadOk) {
			//							layer.setAvailable(false);
			//						}
			//
			//					} catch (DriverLoadException e) {
			//						throw new DriverLayerException(name,e);
			//					} catch (XMLException e) {
			//						throw new DriverLayerException(name,e);
			//					} catch (ReadException e) {
			//						throw new DriverLayerException(name,e);
			//					}
			//
			//				}
			//				// Clases con algun driver genérico creado por otro
			//				// programador
			//				if (xml.contains("other")) {
			//
			//					String driverName = xml.getStringProperty("other");
			//					VectorialDriver driver = null;
			//					try {
			//						driver = (VectorialDriver) LayerFactory.getDM().getDriver(driverName);
			//					} catch (DriverLoadException e) {
			//						// Si no existe ese driver, no pasa nada.
			//						// Puede que el desarrollador no quiera que
			//						// aparezca en el cuadro de diálogo y ha metido
			//						// el jar con sus clases en nuestro directorio lib.
			//						// Intentamos cargar esa clase "a pelo".
			//						if (xml.getChild(2).contains("className"))
			//						{
			//							String className2 = xml.getChild(2).getStringProperty("className");
			//							try {
			//								driver = (VectorialDriver) Class.forName(className2).newInstance();
			//							} catch (Exception e1) {
			//								throw new DriverLayerException(name,e);
			//							}
			//						}
			//					} catch (NullPointerException npe) {
			//						// Si no existe ese driver, no pasa nada.
			//						// Puede que el desarrollador no quiera que
			//						// aparezca en el cuadro de diálogo y ha metido
			//						// el jar con sus clases en nuestro directorio lib.
			//						// Intentamos cargar esa clase "a pelo".
			//						if (xml.getChild(2).contains("className"))
			//						{
			//							String className2 = xml.getChild(2).getStringProperty("className");
			//							try {
			//								driver = (VectorialDriver) Class.forName(className2).newInstance();
			//							} catch (Exception e1) {
			//								throw new DriverLayerException(name,e1);
			//							}
			//						}
			//					}
			//					if (driver instanceof IPersistence)
			//					{
			//						IPersistence persist = (IPersistence) driver;
			//						persist.setXMLEntity(xml.getChild(2));
			//					}
			//					layer = LayerFactory.createLayer(name, driver, proj);
			//				}
			//
			//			}
			//
			//			//TODO VCN FLyrAnnotation es un parche para no tener que duplicar todo el código de aqí y de los diferentes métodos de LayerFactory,
			//			//ya que los drivers de una FLyrAnnotation no sabemos cual es puede ser cualquier Driver Vectorial.
			//			if (className.equals(FLyrAnnotation.class.getName())){
			//				layer=FLyrAnnotation.createLayerFromVect((FLyrVect)layer);
			//			}
			//
			//
			//			layer.setXMLEntity(xml);
			//
			//		} else {
			//			Class clase = LayerFactory.getLayerClassForLayerClassName(className);
			//			layer = (FLayer) clase.newInstance();
			//			if (clase.isAssignableFrom(FLayers.class)) {
			//				((FLayers)layer).setMapContext(getMapContext());
			//				((FLayers)layer).setParentLayer(this);
			////				layer = new FLayers(getMapContext(),this);
			//				layer.setXMLEntity(xml);
			//			} else {
			//				// Capas Nuevas (externas)
			//				layer.setName(name);
			//				layer.setXMLEntity(xml);
			//				layer.load();
			//			}
			//		}
			this.addLayer(layer);
			logger.debug("layer: "+ layer.getName() +" loaded");
			// Comprobar que la proyección es la misma que la de FMap
			// Si no lo es, es una capa que está reproyectada al vuelo
			IProjection proj = layer.getProjection();
			if ((proj != null)) {
				if (!proj.getFullCode().equals(getMapContext().getProjection().getFullCode()))
				{
					ICoordTrans ct = proj.getCT(getMapContext().getProjection());
					// TODO: REVISAR CON LUIS
					// Se lo fijamos a todas, luego cada una que se reproyecte
					// si puede, o que no haga nada

					layer.setCoordTrans(ct);
				}
			}
		} catch (XMLException e) {
			fmap.addLayerError(xml.getStringProperty("name"));
			throw new LoadLayerException(name,e);
		} catch (ClassNotFoundException e) {
			fmap.addLayerError(xml.getStringProperty("name"));
			throw new LoadLayerException(name,e);
		} catch (InstantiationException e) {
			fmap.addLayerError(xml.getStringProperty("name"));
			throw new LoadLayerException(name,e);
		} catch (IllegalAccessException e) {
			fmap.addLayerError(xml.getStringProperty("name"));
			throw new LoadLayerException(name,e);
		} catch (LoadLayerException e){
			fmap.addLayerError(xml.getStringProperty("name"));
			throw e;
		}
	}

	/**
	 * <p>Sets the <code>MapContext</code> that contains this layer node.</p>
	 *
	 * @param mapContext the <code>MapContext</code> that contains this layer node
	 */
	public void setMapContext(MapContext mapContext) {
		this.fmap = mapContext;
	}

	/**
	 * <p>Creates a new layer of the same class as the property <i>className</i> of the XML, after, adds the XML entity to that layer
	 *  and loads the layer. Then, adds the layer to this collection of layers, and if there is a projection defined,
	 *  inserts the transformation coordinates to the layer.</p>
	 *
	 * <p>If the new layer is an instance of <code>FLyrVect</code>, and has a label field, creates a label layer on the layer.</p>
	 *
	 * @param xml tree-node structure with information about layers
	 * @param name name of the layer to add
	 */
	private void addLayerFromXMLNew(XMLEntity xml, String name) {
		//		FLayer layer = null;
		//
		//
		//		try {
		//			String className = xml.getStringProperty("className");
		//			Class clazz = Class.forName(className);
		//			if (clazz.isAssignableFrom(FLayers.class)) {
		//				layer = (FLayer) clazz.newInstance();
		//				((FLayers)layer).setMapContext(getMapContext());
		//				((FLayers)layer).setParentLayer(this);
		//	//		if (className.equals((FLayers.class.getName()))){
		//	//			layer = new FLayers(getMapContext(),this);
		//			} else {
		//	//			Por compatibilidad
		//				if (className.equals(FLyrVect.class.getName())) {
		//					if (xml.contains("file")) {
		//						layer = new FLayerFileVectorial();
		//					} else if (xml.contains("db")) {
		//						try {
		//							layer = (FLayer)((ExtensionPoint)ExtensionPointsSingleton.getInstance().get("Layers")).create("com.iver.cit.gvsig.fmap.layers.FLayerJDBCVectorial");
		//						} catch (Exception e) {
		//							throw new XMLException(new Exception("No se tiene registrada la capa de tipo JDBC"));
		//						}
		//						//className = FLayerJDBCVectorial.class.getName();
		//					} else if (xml.contains("other")){
		//						layer = new FLayerGenericVectorial();
		//					} else {
		//						throw new XMLException(new Exception("Capa vectorial de tipo no reconocido"));
		//					}
		//	//				Fin por compatibilidad
		//				} else {
		//					try {
		//						layer = (FLayer)(((ExtensionPoint)ExtensionPointsSingleton.getInstance().get("Layers")).create(className));
		//					} catch (Exception e) {
		//						//puende que no este registrada como punto de extension
		//						Class clase = Class.forName(className);
		//						layer = (FLayer) clase.newInstance();
		//						// FIXME: Hacemos algo aqui o dejamos que suba el error?
		//					}
		//				}
		//
		//			}
		//			layer.setXMLEntity(xml);
		//			if (name != null) layer.setName(name);
		//			layer.load();
		//
		//			this.addLayer(layer);
		//			logger.debug("layer: "+ layer.getName() +" loaded");
		//			// Comprobar que la proyección es la misma que la de FMap
		//			// Si no lo es, es una capa que está reproyectada al vuelo
		//			IProjection proj = layer.getProjection();
		//			if ((proj != null))
		//				if (proj != getMapContext().getProjection())
		//				{
		//					ICoordTrans ct = proj.getCT(getMapContext().getProjection());
		//					// TODO: REVISAR CON LUIS
		//					// Se lo fijamos a todas, luego cada una que se reproyecte
		//					// si puede, o que no haga nada
		//					layer.setCoordTrans(ct);
		//
		//				}
		//		}catch (Exception e) {
		//			fmap.addLayerError(xml.getStringProperty("name"));
		//			logger.debug(Messages.getString("could_not_load_layer")+": "+xml.getStringProperty("name") + ".\n"
		//					+Messages.getString("reason")+":", e);
		//		}
	}

	public void accept(Visitor visitor) throws BaseException {
		throw new NotSupportedOperationException(visitor, this);
	}

	public void accept(LayersVisitor visitor) throws BaseException {
		for (int i = 0; i < this.getLayersCount(); i++) {
			FLayer layer = this.getLayer(i);
			if (layer instanceof LayersVisitable) {
				((LayersVisitable) layer).accept(visitor);
			} else {
				visitor.visit(layer);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataID()
	 */
	public Object getMetadataID() {
		StringBuffer strb = new StringBuffer();
		strb.append("Layers(");
		strb.append(this.getName());
		strb.append("):{");
		Iterator iter = this.layers.iterator();
		while (iter.hasNext()) {
			strb.append(((FLayer) iter.next()).getMetadataID());
			strb.append(",");
		}
		strb.append("}");
		return strb.toString();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataChildren()
	 */
	public Set getMetadataChildren() {
		Set ret = new TreeSet();
		Iterator iter = this.layers.iterator();
		while (iter.hasNext()) {
			ret.add(iter.next());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.metadata.Metadata#getMetadataName()
	 */
	public String getMetadataName() {
		StringBuffer strb = new StringBuffer();
		strb.append("Layer Group '");
		strb.append(this.getName());
		strb.append("': {");
		Iterator iter = this.layers.iterator();
		while (iter.hasNext()) {
			strb.append(((FLayer) iter.next()).getMetadataName());
			strb.append(",");
		}
		strb.append("}");
		return strb.toString();
	}


	public void beginDraw(Graphics2D g, ViewPort viewPort) {
		LayerDrawEvent beforeEvent = new LayerDrawEvent(this, g, viewPort, LayerDrawEvent.LAYER_BEFORE_DRAW);
		fmap.fireLayerDrawingEvent(beforeEvent);
	}

	public void endDraw(Graphics2D g, ViewPort viewPort) {
		LayerDrawEvent afterEvent = new LayerDrawEvent(this, g, viewPort, LayerDrawEvent.LAYER_AFTER_DRAW);
		fmap.fireLayerDrawingEvent(afterEvent);
	}

}
