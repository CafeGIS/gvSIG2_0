/*
 * Created on 21-jun-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
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
package com.iver.cit.gvsig.project.documents.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.events.ColorEvent;
import org.gvsig.fmap.mapcontext.events.ExtentEvent;
import org.gvsig.fmap.mapcontext.events.ProjectionEvent;
import org.gvsig.fmap.mapcontext.events.listeners.ViewPortListener;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.DraggerBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.RectangleBehavior;

import com.iver.cit.gvsig.project.documents.view.toolListeners.MapOverviewChangeZoomListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.MapOverviewListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.MapOverviewPanListener;


/**
 * <p>Lightweight <code>MapControl</code> that uses another <code>MapControl</code>'s <code>MapContext</code>, and updates
 *  any rectangular extent selected, to the associated <code>MapControl</code>.</p>
 *
 * <p>Both <code>MapControl</code> instances work in the same projection. And, always, the not undefined <i>adjusted extent</i>
 *  of the associated one, will be enhanced as a red-bordered grey-filled rectangle in this one. Furthermore, draws a horizontal and vertical
 *  this component's width or height, black lines centered in that rectangle.</p>
 *
 * @author FJP
 */
public class MapOverview extends MapControl implements ViewPortListener {
	/**
	 * <p>Associated <code>MapControl</code> instance that this component represents its overview.</p>
	 */
	private MapControl m_MapAssoc;

	/**
	 * <p>Determines that's the first time this component is going to be painted.</p>
	 */
	boolean first = true;

	/**
	 * <p>Tool listener used to apply a <i>zoom out</i> operation in this component graphical information.</p>
	 */
	private MapOverviewListener movl;

	/**
	 * <p>Tool listener used to allow user work with this component applying <i>pan</i> operations.</p>
	 */
	private MapOverviewPanListener movpl;

	/**
	 * <p>Tool listener used to allow user work with this component applying <i>pan</i> operations.</p>
	 */
	private MapOverviewChangeZoomListener movczl;

	/**
	 * <p>Rectangular area selected in this component, that will be the extent of the associated <code>MapControl</code> instance.</p>
	 */
	private Envelope extent;

	/**
	 * <p>Double buffer used to paint this component graphical information.</p>
	 */
	private BufferedImage image;

	/**
	 * <p>Creates a <code>MapOverview</code> instance associated to <code>mapAssoc</code>.</p>
	 *
	 * @param mapAssoc <code>MapControl</code> this component will be the overview
	 */
	public MapOverview(MapControl mapAssoc) {
		super();
		this.setName("MapOverview");
		// super.vp.setBackColor(new Color(230,230,230));
		m_MapAssoc = mapAssoc;

		// setModel((FMap) m_MapAssoc.getMapContext().clone()); // Lo inicializamos con
		// los mismos temas que tenga el grande.
		movl = new MapOverviewListener(this);
		movpl = new MapOverviewPanListener(this);
		movczl = new MapOverviewChangeZoomListener(this);
		addMapTool(
				"zoomtopoint",
				new Behavior[]{
						new PointBehavior(movl),
						new RectangleBehavior(movczl),
						new DraggerBehavior(movczl),
						new DraggerBehavior(movpl)
				}
		);

//		setCursor(movl.getCursor());

		setTool("zoomtopoint");
		getGrid().setShowGrid(false);
		getGrid().setAdjustGrid(false);
	}

	/**
	 * @see MapControl#getMapContext()
	 */
	public MapContext getAssociatedMapContext() {
		return m_MapAssoc.getMapContext();
	}

	/**
	 * <p>Gets the <code>MapControl</code> instance that wrappers.</p>
	 *
	 * @return the <code>MapControl</code> instance that wrappers
	 */
	public MapControl getAssociatedMapControl(){
		return m_MapAssoc;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
	 */
	public void extentChanged(ExtentEvent evExtent) {
		// Nos llega el nuevo extent del FMap asociado, así que dibujamos nuestro
		// rectángulo para mostrar la zona de dibujo del otro mapa.
		repaint();
	}

	/**
	 * <p>If this had no extent, calls {@link #delModel() #delModel()}, otherwise recalculates this
	 *  component's view port extent as the union of all extents of all layers of this map.</p>
	 */
	public void refreshExtent() {
		try {
		if (this.getMapContext().getFullEnvelope()!=null){
				this.getMapContext().getViewPort().setEnvelope(this.getMapContext().getFullEnvelope());
		}else{
			delModel();
		}
		} catch (ReadException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * <p>Repaints this component updating its <i>extent</i>.</p>
	 *
	 * @param r the new extent
	 */
	public void refreshOverView(Envelope r){
		extent=r;
		repaint();
	}

	/**
	 * <p>Paints this component's graphical information using a 8-bit RGBA color double buffer, drawing a red-bordered
	 *  grey-filled rectangle enhancing the extent selected, and a horizontal and vertical this component's width or
	 *  height, black lines centered in that rectangle.</p>
	 */
	protected void paintComponent(Graphics g) {
		getGrid().setShowGrid(false);
		getGrid().setAdjustGrid(false);
		super.paintComponent(g);
			if ((m_MapAssoc.getMapContext().getViewPort().getExtent() != null) &&
					(getMapContext().getViewPort().getExtent() != null)) {
				if (first) {
					first = false;
					repaint();
					return;
				}
				image = new BufferedImage(this.getWidth(), this.getHeight(),
		                    BufferedImage.TYPE_INT_ARGB);
				ViewPort vp = getMapContext().getViewPort();
				Envelope extentView=vp.getAdjustedExtent();
				ViewPort vpOrig = m_MapAssoc.getMapContext().getViewPort();
				if (extent==null) {
					extent=vpOrig.getAdjustedExtent();
				}
				// Dibujamos el extent del mapa asociado.
				Graphics2D g2 = (Graphics2D) image.getGraphics();
				g2.setTransform(vp.getAffineTransform());

				g2.setStroke(new BasicStroke((float) vp.getDist1pixel()));

				g2.setColor(Color.red);
				Rectangle2D extentToDraw=new Rectangle2D.Double(extent.getMinimum(0),extent.getMinimum(1),extent.getLength(0),extent.getLength(1));
				g2.draw(extentToDraw);
				g2.setColor(new Color(100,100,100,100));
				g2.fill(extentToDraw);
				// dibujamos las líneas vertical y horizontal
				Point2D pRightUp = vp.toMapPoint(getWidth(), 0);

				Line2D.Double linVert = new Line2D.Double(extentToDraw.getCenterX(),
						extentView.getMinimum(1), extentToDraw.getCenterX(),
						pRightUp.getY());
				Line2D.Double linHoriz = new Line2D.Double(extentView.getMinimum(0),
						extentToDraw.getCenterY(), pRightUp.getX(),
						extentToDraw.getCenterY());

				g2.setColor(Color.darkGray);
				g2.draw(linVert);
				g2.draw(linHoriz);
				g.drawImage(image,0,0,this);
				g2.setTransform(new AffineTransform());
				extent=null;
			}

	}

	/**
	 * <p>Sets a <code>MapContext</code> to this component, configuring it to manage view port events produced
	 *  in the associated <code>MapControl</code> and this one's view port.</p>
	 *
	 * <p>This <code>MapContext</code>'s projection will be the same as the associated <code>MapControl</code>'s one.</p>
	 *
	 * <p>Setting the model includes the following steps:
	 *  <ul>
	 *   <li><b>1.-</b> set <code>model</code> as the <code>MapContext</code> of <code>this</code>.</li>
	 *   <li><b>2.-</b> set as <code>model</code> projection, the associated <code>MapControl<code>'s projection.</li>
	 *   <li><b>3.-</b> set <code>this</code> as <i>view port</i> listener of the associated <code>MapControl</code>'s <i>view port</i>.</li>
	 *   <li><b>4.-</b> set <code>this</code> as <i>view port</i> listener of this <code>MapContext</code>'s <i>view port</i></li>
	 *  </ul>
	 * </p>
	 *
	 * @param model data to set
	 */
	public void setModel(MapContext model) {
		this.setMapContext(model);
		model.setProjection(m_MapAssoc.getMapContext().getProjection());
		m_MapAssoc.getMapContext().getViewPort().addViewPortListener(this);
		getMapContext().getViewPort().addViewPortListener(this);
	}

	/**
	 * <p>Removes this component as listener of the the associated <code>MapControl</code> and this one's view port. Besides,
     *  removes the extent.</p>
	 */
	private void delModel(){
		this.getMapContext().getViewPort().setEnvelope(null);
		m_MapAssoc.getMapContext().getViewPort().removeViewPortListener(this);
		getMapContext().getViewPort().removeViewPortListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
	 */
	public void backColorChanged(ColorEvent e) {
	}

	/**
	 * @see ViewPortListener#projectionChanged(ProjectionEvent)
	 *
	 * @see MapControl#setProjection(IProjection)
	 */
	public void projectionChanged(ProjectionEvent e) {
		super.setProjection(e.getNewProjection());

	}

	/**
	 * <p>Unimplemented.</p>
	 *
	 * <p>Can't change the projection, because must be the same as the one of the
	 *  associated <code>MapControl</code> instance.</p>
	 */
	public void setProjection(IProjection proj) {
		//No permitimos cambiar la proyeccion
		//ya que debe ser la misma que la del
		//MapControl asociado
		return;
	}
}
