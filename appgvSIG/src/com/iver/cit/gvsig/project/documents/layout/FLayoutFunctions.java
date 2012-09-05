/*
 * Created on 16-sep-2004
 *
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
package com.iver.cit.gvsig.project.documents.layout;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.layout.commands.FrameCommandsRecord;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Funciones utilizadas desde el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FLayoutFunctions {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(FLayoutFunctions.class);
	private Layout layout = null;

	/**
	 * Crea un nuevo FLayoutFunctions.
	 *
	 * @param l Referencia al Layout.
	 */
	public FLayoutFunctions(Layout l) {
		layout = l;
	}

	/**
	 * Gestiona la herramienta de selección sobre el Mapa.
	 */
	public void setSelect() {
		IFFrame fframe = null;
		boolean isUpdate = false;
		layout.getLayoutContext().updateFFrames();
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		FrameCommandsRecord efs=layout.getLayoutContext().getFrameCommandsRecord();
		efs.startComplex("move");
		for (int i = 0; i < fframes.length; i++) {
			fframe = fframes[i];

			int difx = (layout.getLayoutControl().getLastPoint().x - layout.getLayoutControl().getFirstPoint().x);
			int dify = (layout.getLayoutControl().getLastPoint().y - layout.getLayoutControl().getFirstPoint().y);

			if (((Math.abs(difx) > 3) || (Math.abs(dify) > 3)) &&
					(fframe.getSelected() != IFFrame.NOSELECT)) {
				Rectangle2D rectangle = fframe.getLastMoveRect();
				if (rectangle==null) {
					efs.endComplex();
					return;
				}
//				if (layout.isAdjustingToGrid()) {
//					//cuadrar con la cuadrícula dibujada y después en el mouseReleased guardar ese rectángulo de la misma forma.
//					FLayoutUtilities.setRectGrid(rectangle,
//						layout.getAtributes().getUnitInPixelsX(),
//						layout.getAtributes().getUnitInPixelsY(), layout.getAT());
//				}

				if (fframe instanceof FFrameGroup) {
					((FFrameGroup) fframe).setAt(layout.getLayoutControl().getAT());
				}

				IFFrame fframeAux=fframe.cloneFFrame(layout);
				fframeAux.setBoundBox(FLayoutUtilities.toSheetRect(rectangle,
						layout.getLayoutControl().getAT()));
//				if (fframeAux instanceof IFFrameUseFMap)
//					((IFFrameUseFMap)fframeAux).refresh();
				efs.update(fframe,fframeAux);
				fframeAux.getBoundingBox(layout.getLayoutControl().getAT());

				isUpdate = true;
			}
			Rectangle rect=null;
			if (layout.getLayoutControl().getReSel()==null){
				rect=new Rectangle();
				rect.setFrameFromDiagonal(layout.getLayoutControl().getFirstPoint(), layout.getLayoutControl().getLastPoint());
			}else{
				rect=layout.getLayoutControl().getReSel();
			}
			if (layout.getLayoutControl().isReSel() &&
					(rect.contains(fframe.getBoundingBox(
							layout.getLayoutControl().getAT())))) {
				fframe.setSelected(true);
			}

			if (isUpdate) {
				layout.getLayoutControl().setStatus(LayoutControl.DESACTUALIZADO);

			} else {
				layout.getLayoutControl().setStatus(LayoutControl.ACTUALIZADO);
			}
		}
		efs.endComplex();
		layout.getLayoutContext().updateFFrames();
	}

	/**
	 * Añade un fframe al Layout del tipo preseleccionado  y abre el diálogo
	 * para configurar las características.
	 * @return TODO
	 */
//	public IFFrame addFFrame() {
//		IFFrame fframe = null;
//		boolean isadd = false;
//
//		if (layout.getTool() == Layout.RECTANGLEVIEW) {
//			fframe = new FFrameView();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLEOVERVIEW) {
//			fframe = new FFrameOverView();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLEPICTURE) {
//			fframe = new FFramePicture();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLESCALEBAR) {
//			fframe = new FFrameScaleBar();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLELEGEND) {
//			fframe = new FFrameLegend();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLETEXT) {
//			fframe = new FFrameText();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLENORTH) {
//			fframe = new FFrameNorth();
//			isadd = true;
//		} else if (layout.getTool() == Layout.RECTANGLEBOX) {
//			fframe = new FFrameTable();
//			isadd = true;
//		} else if ((layout.getTool() == Layout.LINE) ||
//				(layout.getTool() == Layout.POLYLINE) ||
//				(layout.getTool() == Layout.POLYGON) ||
//                (layout.getTool() == Layout.CIRCLE) ||
//                (layout.getTool() == Layout.RECTANGLESIMPLE) ||
//                (layout.getTool() == Layout.POINT)) {
//			fframe = new FFrameGraphics();
//			((FFrameGraphics)fframe).setGeometryAdapter(layout.getGeometryAdapter());
//			isadd = false;
//			((FFrameGraphics) fframe).update(layout.getTool(), layout.getAT());
//		} /*else if ((layout.getTool() == Layout.POINT)){
//			fframe = new FFrameGraphics();
//			isadd = true;
//			((FFrameGraphics) fframe).update(layout.getTool(), layout.getAT());
//		}*/
//		if (isadd) {
//			Rectangle2D r = new Rectangle2D.Double(); //rectOrigin.x+m_PointAnt.x,rectOrigin.y+m_PointAnt.y,m_LastPoint.x-m_PointAnt.x,m_LastPoint.y-m_PointAnt.y);
//			int tolerance=20;
//			r=getRectangle(tolerance);
//
////			if (layout.isAdjustingToGrid()) {
////				//cuadrar con la cuadrícula dibujada y después en el mouseReleased guardar ese rectángulo de la misma forma.
////				FLayoutUtilities.setRectGrid(r,
////					layout.getAtributes().getUnitInPixelsX(),
////					layout.getAtributes().getUnitInPixelsY(), layout.getAT());
////			}
//
//			fframe.setBoundBox(FLayoutUtilities.toSheetRect(r, layout.getAT()));
//			IFFrame newFrame=layout.openFFrameDialog(fframe);
//			if (newFrame!=null) {
//				if (!layout.isEditGroup()){
//					layout.addFFrame(newFrame, true,true);
//				}
//			}
//		}else{
//			if (layout.getTool() == Layout.POINT){
//				Rectangle2D r=layout.getGeometryAdapter().getBounds2D();
//				double d=0.5;//FLayoutUtilities.toSheetDistance(50,layout.getAT());
//				r=new Rectangle2D.Double(r.getX()-(d/2),r.getY()-d,d*2,d*2);
//				fframe.setBoundBox(r);
//			}else{
//				fframe.setBoundBox(layout.getGeometryAdapter().getBounds2D());
//			}
//			layout.addFFrame(fframe, true,true);
//			///fframe.setBoundBox(FLayoutUtilities.toSheetRect(r, layout.getAT()));
//		}
//		fframe.setLayout(layout);
//		return fframe;
//	}

//	private Rectangle2D getRectangle(int tolerance) {
//		Rectangle2D r=new Rectangle2D.Double();
//		if (isCorrectSize(tolerance,layout.getFirstPoint(),layout.getLastPoint())){
//			Point2D fp=layout.getFirstPoint();
//			Point2D lp=layout.getLastPoint();
//			if (fp.getX()<lp.getX()){
//				if (fp.getY()<lp.getY()){
//					r.setRect(fp.getX(),fp.getY(),lp.getX()-fp.getX(),lp.getY()-fp.getY());
//				}else{
//					r.setRect(fp.getX(),lp.getY(),lp.getX()-fp.getX(),fp.getY()-lp.getY());
//				}
//			}else{
//				if (fp.getY()>lp.getY()){
//					r.setRect(lp.getX(),lp.getY(),fp.getX()-lp.getX(),fp.getY()-lp.getY());
//				}else{
//					r.setRect(lp.getX(),fp.getY(),lp.getX()-fp.getX(),lp.getY()-fp.getY());
//				}
//			}
//		}else{
//			Point2D p1=layout.getFirstPoint();
//			p1=new Point2D.Double(p1.getX()+tolerance,p1.getY()+tolerance);
//			r.setFrameFromDiagonal(layout.getFirstPoint(),p1);
//		}
//		return r;
//	}

	/**
	 * Pan sobre la vista del FFrameView.
	 *
	 * @param p1 Punto inicial del desplazamiento.
	 * @param p2 Punto final del desplazamiento.
	 */
	public void setViewPan(Point p1, Point p2) {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];

				if (((IFFrame)fframe).getSelected() != IFFrame.NOSELECT) {
					 if (fframe.getATMap()!=null) {
						Rectangle2D.Double r = new Rectangle2D.Double();
						Rectangle2D extent = fframe.getMapContext().getViewPort()
												   .getExtent();
						if (extent != null) {
							Point2D mp1 = toMapPoint(p1, fframe.getATMap());
							Point2D mp2 = toMapPoint(p2, fframe.getATMap());
							double x = extent.getX() - (mp2.getX() - mp1.getX());
							double y = extent.getY() - (mp2.getY() - mp1.getY());
							double width = extent.getWidth();
							double height = extent.getHeight();
							try {
								fframe.getMapContext().getViewPort().setEnvelope(geomManager.createEnvelope(x,y,x+width,y+height, SUBTYPES.GEOM2D));
							} catch (CreateEnvelopeException e) {
								logger.error("Error creating the envelope", e);
							}
							fframe.refresh();
						}
					 }
				}
			}
		}
	}

	/**
	 * Devuelve un punto real a partir de un punto en pixels sobre la vista.
	 *
	 * @param pScreen Punto en pixels.
	 * @param at1 Matriz de transformación.
	 *
	 * @return Punto real.
	 */
	public static Point2D.Double toMapPoint(Point2D pScreen, AffineTransform at1) {
		Point2D.Double pWorld = new Point2D.Double();

		AffineTransform at;

		try {
			at = at1.createInverse();
			at.transform(pScreen, pWorld);
		} catch (NoninvertibleTransformException e) {
			//				 throw new RuntimeException(e);
		}

		return pWorld;
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
//	private boolean isCorrectSize(int tolerance, Point2D p1, Point2D p2) {
//		if (Math.abs(p2.getX()-p1.getX())<tolerance) {
//			return false;
//		} else if (Math.abs(p2.getY()-p1.getY())<tolerance) {
//			return false;
//		}
//		return true;
//	}
}
