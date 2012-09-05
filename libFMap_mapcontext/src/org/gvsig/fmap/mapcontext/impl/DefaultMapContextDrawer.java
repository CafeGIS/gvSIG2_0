package org.gvsig.fmap.mapcontext.impl;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.gvsig.compat.CompatLocator;
import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.MapContextDrawer;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayersIterator;
import org.gvsig.fmap.mapcontext.layers.operations.ComposedLayer;
import org.gvsig.fmap.mapcontext.layers.operations.ILabelable;
import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;
import org.gvsig.tools.task.Cancellable;



public class DefaultMapContextDrawer implements MapContextDrawer {
	private MapContext mapContext=null;
	private ViewPort viewPort=null;
	private CachedImage cachedImage=null;
	private DrawList previousDrawList = null;



	public DefaultMapContextDrawer(){

	}

	protected void checkIntilalized(){
		if (mapContext == null || viewPort == null){
			throw new IllegalStateException("MapContext and ViewPort must be set");
		}
	}

	protected boolean isLayerCacheable(FLayer layer){
		return layer.isActive();
	}

	public void draw(FLayers root, BufferedImage image, Graphics2D g, Cancellable cancel,
			double scale) throws ReadException {

		this.checkIntilalized();

		boolean needToPaintAll = false;

		DrawList drawList = this.createDrawList(root, cancel,scale);
		if (cancel.isCanceled()){
			return;
		}

		if (cachedImage == null){
			needToPaintAll=true;
		} else if (!cachedImage.isValid(mapContext, viewPort, drawList.getFirstLayerToDraw())){
			needToPaintAll=true;
		}


		int firstLayer= 0;
		if (!needToPaintAll){
			firstLayer = cachedImage.getListPosition()+1;
//			System.out.println("=======Pintando a partir de la pos "+ firstLayer+"============");
			g.drawImage(cachedImage.getImage(), 0, 0, null);
//			System.out.println("=======Pintando imagen ============");

		} else{
			this.cachedImage = null;
//			System.out.println("=======Pintando todo============");
		}


		boolean cached=false;
		ComposedLayer composed = null;
		int pos;
		int layerPos= -1;
		FLayer layer;
		Object obj;
		LayersGroupEvent event;
		for (pos=0; pos < drawList.size(); pos++){
			if (cancel.isCanceled()){
				return;
			}

			obj = drawList.get(pos);

			// *** Gestion de eventos de pintado de grupos ***
			if (obj instanceof LayersGroupEvent){
				event = (LayersGroupEvent) obj;
				if (event.type == LayersGroupEvent.IN_Event){
//					System.out.println("=======Empiza a pintar grupo de capas "+ ((FLayers)event.group).getName() +"============");
					event.group.beginDraw(g, viewPort);
				} else{
					event.group.endDraw(g, viewPort);
//					System.out.println("=======Fin a pintar grupo de capas "+ ((FLayers)event.group).getName() +"============");

				}
				continue;
			}
			layerPos++;
			if (layerPos < firstLayer){
				continue;
			}

			layer = (FLayer) obj;
			if (cancel.isCanceled()){
				return;
			}

			// *** Gestion de cache ***
			if ((!cached) && this.isLayerCacheable(layer)){
				if (layerPos > 0){ //La primera capa es la activa, no cacheamos
					if (cachedImage==null || cachedImage.getListPosition() < layerPos-1){

						if (composed != null){
							//si tenemos una composicion de capas, necesitamos pintarlas
							//antes de guardarnos la imagen
//							System.out.println("=======Pintando composicion de pintado "+ (layerPos-1)+" (antes de cache)============");
							this.draw(composed, image, g, cancel, scale);
//							composed.draw(image, g, viewPort, cancel, scale);
							composed = null;

						}
						CachedImage  newCached = new CachedImage();
						newCached.setImage(image, mapContext, viewPort, layerPos-1);
						this.cachedImage = newCached;
//						System.out.println("=======Guardando imagen de la pos "+ (layerPos-1)+" ("+ newCached.getListPosition() +")============");
					}
				}
				cached = true;
			}
			if (cancel.isCanceled()){
				return;
			}

			if (composed == null){
				composed = layer.newComposedLayer();
				if (composed != null){
					try {
//						System.out.println("=== añadiendo a composicion de pintado "+ layerPos+ " "+layer.getName());
						composed.add(layer);
						continue;
					} catch (Exception e) {
						throw new ReadException("DefalutMapContexDrawer exception",e);
					}
				}
			}else{
				if (composed.canAdd(layer)){
					try {
//						System.out.println("=== añadiendo a composicion de pintado "+ layerPos+ " "+layer.getName());
						composed.add(layer);
						continue;
					} catch (Exception e) {
						throw new ReadException("DefalutMapContexDrawer exception",e);
					}
				} else {
//					System.out.println("=======Pintando composicion de pintado "+ (layerPos-1)+" (cambio composicion)============");
					this.draw(composed, image, g, cancel, scale);
//					composed.draw(image, g, viewPort, cancel, scale);
					composed = layer.newComposedLayer();
					if (composed != null){
						try {
//							System.out.println("=== añadiendo a composicion de pintado "+ layerPos+ " "+layer.getName());
							composed.add(layer);
							continue;
						} catch (Exception e) {
							throw new ReadException("DefalutMapContexDrawer exception",e);
						}					}
				}
			}
//			System.out.println("=== pintando "+ layerPos+ " "+layer.getName());
			this.draw(layer, image, g, cancel, scale);
//			layer.draw(image, g, viewPort, cancel, scale);

		}
		if (composed != null){
			// si la composicion no se ha pintado la pintamos
//			System.out.println("=======Pintando composicion de pintado "+ (layerPos-1)+" (ultimo)============");
			this.draw(composed, image, g, cancel, scale);
//			composed.draw(image, g, viewPort, cancel, scale);
		}
		if (cancel.isCanceled()){
			return;
		}

		this.previousDrawList = drawList;

	}

	private void draw(Object layerOrComposed,BufferedImage image, Graphics2D g, Cancellable cancel,
			double scale) throws ReadException{
		ILabelable labelable= null;
		ILabelable tmp= null;
		if (layerOrComposed instanceof ILabelable){

			tmp =(ILabelable) layerOrComposed;

			if (tmp.isLabeled()
				&& tmp.getLabelingStrategy() != null
				&& tmp.getLabelingStrategy().shouldDrawLabels(scale)) {
				labelable =tmp;
			}
		}
		if (layerOrComposed instanceof FLayer){
			FLayer layer = (FLayer) layerOrComposed;
			layer.draw(image, g, viewPort, cancel, scale);
		} else{
			ComposedLayer composed = (ComposedLayer) layerOrComposed;
			composed.draw(image, g, viewPort, cancel, scale);
		}
		if (labelable != null){
			labelable.drawLabels(image, g, viewPort, cancel, scale, MapContext.getScreenDPI());
		}

	}

	private void print(Object layerOrComposed,Graphics2D g, Cancellable cancel,
			double scale, PrintAttributes properties) throws ReadException{
		ILabelable labelable= null;
		ILabelable tmp= null;
		if (layerOrComposed instanceof ILabelable){

			tmp =(ILabelable) layerOrComposed;

			if (tmp.isLabeled()
				&& tmp.getLabelingStrategy() != null
				&& tmp.getLabelingStrategy().shouldDrawLabels(scale)) {
				labelable =tmp;
			}
		}

		if (layerOrComposed instanceof FLayer){
			FLayer layer = (FLayer) layerOrComposed;
			layer.print(g, viewPort, cancel, scale, properties);
		} else{
			ComposedLayer composed = (ComposedLayer) layerOrComposed;
			composed.print(g, viewPort, cancel, scale, properties);
		}
		if (labelable != null){
			labelable.printLabels(g, viewPort, cancel, scale, properties);
		}

	}

	public void setMapContext(MapContext mapContext) {
		if (this.mapContext == mapContext){
			return;
		}
		this.clean();
		this.mapContext = mapContext;

	}

	public void setViewPort(ViewPort viewPort) {
		if (this.viewPort == viewPort){
			return;
		}
		this.clean();
		this.viewPort = viewPort;

	}

	protected void clean(){
		this.cachedImage = null;
	}

	public class CachedImage{
		private BufferedImage image;
		private long mapContextVersion;
		private long viewPortVersion;
		private int listPosition;

		public void setImage(BufferedImage img,MapContext mapContext, ViewPort viewPort,int pos) {
			
			image = CompatLocator.getGraphicsUtils().copyBufferedImage(img);
			this.mapContextVersion = mapContext.getDrawVersion();
			this.viewPortVersion = viewPort.getDrawVersion();
			this.listPosition = pos;
		}

		public BufferedImage getImage() {
			return image;
		}

		public long getMapContextVersion() {
			return mapContextVersion;
		}

		public long getViewPortVersion() {
			return viewPortVersion;
		}

		public int getListPosition(){
			return this.listPosition;
		}

		public boolean isValid(MapContext context,ViewPort viewPort,int firstLayerToDraw){
			if (viewPort.getDrawVersion() > this.viewPortVersion){
				return false;
			}
			if (firstLayerToDraw <= this.listPosition){
				return false;
			}
			return true;
		}
	}

	public class DrawList{
		private List layers = new ArrayList();
		private List all = new ArrayList();
		private List versions = new ArrayList();
		private DrawList previosList = null;
		private int firstLayerToDraw =-1;

		public DrawList(){
		}

		public DrawList(DrawList previousList){
			if (previousList != null){
				this.firstLayerToDraw =previousList.size();
				this.previosList=previousList;
			}
		}

		public int getLayerCount(){
			return this.layers.size();
		}

		private boolean hasChanged(FLayer layer, int pos){
			FLayer previous =(FLayer) this.previosList.layers.get(pos);
			String previousName = previous.getName();
			String layerName = layer.getName();
			if (previous != layer){
				return true;
			}
			long previousVersion = ((Long) this.previosList.versions.get(pos)).longValue();
			long layerVersion = layer.getDrawVersion();

			return previousVersion != layerVersion;
		}

		public void add(Object obj){
			if (obj instanceof FLayer){
				FLayer layer = (FLayer) obj;
				int curIndex = this.layers.size();
				if (this.firstLayerToDraw >= curIndex){
					if (this.previosList.getLayerCount() > curIndex){
						if (this.hasChanged(layer, curIndex)){
							this.firstLayerToDraw = curIndex;
						}
					} else if (this.previosList.getLayerCount() == curIndex){
						this.firstLayerToDraw = curIndex;
					}
				}
				this.layers.add(layer);
				this.versions.add(new Long(layer.getDrawVersion()));
			} else if (!(obj instanceof LayersGroupEvent)){
				throw new UnsupportedOperationException();
			}

			this.all.add(obj);
		}

		public int size(){
			return this.all.size();
		}

		public int getFirstLayerToDraw(){
			return this.firstLayerToDraw;
		}

		public FLayer getLayer(int pos){
			return (FLayer) this.layers.get(pos);
		}

		public Object get(int pos){
			return this.all.get(pos);
		}

	}

	private class SimpleLayerIterator extends LayersIterator{

		public SimpleLayerIterator(FLayer layer) {
			this.appendLayer(layer);
		}

		public boolean evaluate(FLayer layer) {
			if (layer instanceof FLayers) {
				return false;
			}
			return layer.isAvailable() && layer.isVisible();
		}

	}

	public void dispose() {
		this.mapContext=null;
		this.viewPort=null;
		this.cachedImage=null;
		this.previousDrawList = null;
	}


	public void print(FLayers root, Graphics2D g, Cancellable cancel,
			double scale, PrintAttributes properties)
			throws ReadException {
		this.checkIntilalized();

		List printList = this.createPrintList(root, cancel);
		if (cancel.isCanceled()){
			return;
		}


		ComposedLayer composed = null;
		int pos;
		FLayer layer;
		int layerPos= -1;
		Object obj;
		LayersGroupEvent event;
		for (pos=0; pos < printList.size(); pos++){
			if (cancel.isCanceled()){
				return;
			}

			obj = printList.get(pos);
			if (obj instanceof LayersGroupEvent){
				event = (LayersGroupEvent) obj;
				if (event.type == LayersGroupEvent.IN_Event){
//					System.out.println("=======Empiza a pintar grupo de capas "+ ((FLayers)event.group).getName() +"============");
					event.group.beginDraw(g, viewPort);
				} else{
					event.group.endDraw(g, viewPort);
//					System.out.println("=======Fin a pintar grupo de capas "+ ((FLayers)event.group).getName() +"============");

				}
				continue;
			}
			layerPos++;

			layer = (FLayer) obj;


			// *** Pintado de capa/composicion de capa ***
			if (composed == null){
				composed = layer.newComposedLayer();
				if (composed != null){
					try {
						composed.add(layer);
//						System.out.println("=======Imprimiendo composicion de pintado "+ (layerPos-1)+" ============");
						continue;
					} catch (Exception e) {
						throw new ReadException("DefalutMapContexDrawer exception",e);
					}
				}
			}else{
				if (composed.canAdd(layer)){
					try {
						composed.add(layer);
//						System.out.println("=== añadiendo a composicion de pintado "+ layerPos+ " "+layer.getName());
						continue;
					} catch (Exception e) {
						throw new ReadException("DefalutMapContexDrawer exception",e);
					}
				} else {
//					System.out.println("=======Imprimiendo composicion de pintado "+ (layerPos-1)+" ============");
					this.print(composed, g, cancel, scale, properties);
//					composed.print( g, viewPort, cancel, scale,properties);
					composed = layer.newComposedLayer();
					if (composed != null){
						try {
							composed.add(layer);
//							System.out.println("=== añadiendo a composicion de pintado "+ layerPos+ " "+layer.getName());
							continue;
						} catch (Exception e) {
							throw new ReadException("DefalutMapContexDrawer exception",e);
						}
					}
				}
			}
//			System.out.println("=== imprimiendo "+ layerPos+ " "+layer.getName());
			this.print(layer, g, cancel, scale, properties);
//			layer.print(g, viewPort, cancel, scale,properties);
			// *** Pintado de capa/composicion de capa ***
			if (composed != null){
				// si la composicion no se ha pintado la pintamos
//				System.out.println("=======Imprimiendo composicion de pintado "+ (layerPos-1)+" (ultimo) ============");
				this.print(composed, g, cancel, scale, properties);
//				composed.print(g, viewPort, cancel, scale, properties);
				composed = null;
			}
		}


	}

	private DrawList createDrawList(FLayers root,Cancellable cancel,double scale){
		DrawList result = new DrawList(this.previousDrawList);
		Iterator iter = new MyLayerIterator((FLayer)root,scale);
		while (iter.hasNext()){
			if (cancel.isCanceled()){
				return null;
			}
			result.add(iter.next());

		}

		return result;
	}

	private List createPrintList(FLayers root,Cancellable cancel){
		List result = new ArrayList();
		Iterator iter = new SimpleLayerIterator((FLayer)root);
		while (iter.hasNext()){
			if (cancel.isCanceled()){
				return null;
			}
			result.add(iter.next());
		}
		return result;
	}

	private class MyLayerIterator implements Iterator{
		ArrayList layersList  =new ArrayList();
		int index = 0;
		double scale=0;


		public MyLayerIterator(FLayer layer, double scale) {
			this.appendLayer(layer);
			this.scale=scale;
		}

		protected void appendLayer(FLayer layer) {
			if (this.evaluate(layer)) {
				layersList.add(layer);
			}
			if (layer instanceof LayerCollection) {
				appendLayers((LayerCollection)layer);
			}
		}

		private void appendLayers(LayerCollection layers) {
			int i;
			layersList.add(new LayersGroupEvent(layers,LayersGroupEvent.IN_Event));
			for (i=0;i< layers.getLayersCount();i++) {
				appendLayer(layers.getLayer(i));
			}
			layersList.add(new LayersGroupEvent(layers,LayersGroupEvent.OUT_Event));
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			return  index < layersList.size();
		}

		public Object next() {
			if (!this.hasNext()) {
				throw new NoSuchElementException();
			}
			Object aux = layersList.get(index);
			index++;
			return aux;
		}

		public boolean evaluate(FLayer layer) {
			if (layer instanceof FLayers) {
				return false;
			}
			return layer.isAvailable() && layer.isVisible() && layer.isWithinScale(this.scale);
		}

	}

	private class LayersGroupEvent {
		public static final String IN_Event = "in";
		public static final String OUT_Event = "Out";

		private LayerCollection group=null;
		private String type = IN_Event;

		public LayersGroupEvent(LayerCollection group,String type){
			this.group =group;
			this.type =type;
		}


		public String getType() {
			return type;
		}

		public LayerCollection getGroup() {
			return group;
		}
	}

}
