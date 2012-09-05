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
package com.iver.cit.gvsig.project.documents.layout.tools.listener;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphics;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphicsFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Implementación de la interfaz LayoutRectangleListener como herramienta para
 * realizar una inserción por rectángulo.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutAddRectangleListener implements LayoutRectangleListener {
//	private final Image img = new ImageIcon(MapControl.class.getResource(
//				"images/RectSelectCursor.gif")).getImage();
	private final Image img = PluginServices.getIconTheme().get("rect-select-cursor").getImage();
	protected Layout layout;
	protected static int TOLERANCE=15;

	/**
	 * Crea un nuevo LayoutAddRectangleListener.
	 *
	 * @param l Layout.
	 */
	public LayoutAddRectangleListener(Layout l) {
		this.layout = l;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener#rectangle(org.gvsig.fmap.mapcontrol.tools.Events.EnvelopeEvent)
	 */
	public void rectangle(EnvelopeEvent event) throws BehaviorException {
		FFrameGraphics fframe =(FFrameGraphics)FrameFactory.createFrameFromName(FFrameGraphicsFactory.registerName);

		fframe.setLayout(layout);
		fframe.setGeometryAdapter(layout.getLayoutControl().getGeometryAdapter());
		fframe.update(FFrameGraphics.RECTANGLE, layout.getLayoutControl().getAT());
		fframe.setBoundBox(layout.getLayoutControl().getGeometryAdapter().getBounds2D());
		layout.getLayoutContext().addFFrame(fframe, true,true);
		PluginServices.getMainFrame().enableControls();
        layout.getLayoutControl().refresh();
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}
	protected Rectangle2D getRectangle(int tolerance) {
		Rectangle2D r=new Rectangle2D.Double();
		if (isCorrectSize(tolerance,layout.getLayoutControl().getFirstPoint(),layout.getLayoutControl().getLastPoint())){
			Point2D fp=layout.getLayoutControl().getFirstPoint();
			Point2D lp=layout.getLayoutControl().getLastPoint();
			if (fp.getX()<lp.getX()){
				if (fp.getY()<lp.getY()){
					r.setRect(fp.getX(),fp.getY(),lp.getX()-fp.getX(),lp.getY()-fp.getY());
				}else{
					r.setRect(fp.getX(),lp.getY(),lp.getX()-fp.getX(),fp.getY()-lp.getY());
				}
			}else{
				if (fp.getY()>lp.getY()){
					r.setRect(lp.getX(),lp.getY(),fp.getX()-lp.getX(),fp.getY()-lp.getY());
				}else{
					r.setRect(lp.getX(),fp.getY(),lp.getX()-fp.getX(),lp.getY()-fp.getY());
				}
			}
		}else{
			Point2D p1=layout.getLayoutControl().getFirstPoint();
			p1=new Point2D.Double(p1.getX()+tolerance,p1.getY()+tolerance);
			r.setFrameFromDiagonal(layout.getLayoutControl().getFirstPoint(),p1);
		}
		return r;
	}
	/**
	 * Devuelve true si el rectangulo formado por los dos puntos que se pasan
	 * como parámetro es superior a la tolerancia.
	 *
	 * @param tolerance Tolerancia
	 * @param p1 Punto inicial del rectángulo.
	 * @param p2 Punto final del rectángulo.
	 *
	 * @return True si el tamaño es correcto.
	 */
	private boolean isCorrectSize(int tolerance, Point2D p1, Point2D p2) {
		if (Math.abs(p2.getX()-p1.getX())<tolerance) {
			return false;
		} else if (Math.abs(p2.getY()-p1.getY())<tolerance) {
			return false;
		}
		return true;
	}
}
