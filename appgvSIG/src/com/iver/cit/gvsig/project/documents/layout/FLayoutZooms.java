/*
 * Created on 17-sep-2004
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
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.prefs.Preferences;

import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Clase encargada de realizar los zooms al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FLayoutZooms {
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(FLayoutZooms.class);
	private Layout layout = null;

	public FLayoutZooms(Layout l) {
		layout = l;
	}

	/**
	 * Realiza un zoom por rectángulo o por punto con un escalado por defecto
	 * sobre el Layout que se le pasa como parámetro.
	 *
	 * @param p1 punto de inicio del rectángulo.
	 * @param p2 punto final del recángulo.
	 */
	public void setZoomIn(Point p1, Point p2) {
		if (java.lang.Math.abs(layout.getLayoutControl().getFirstPoint().x - p2.x) < 4) {
			double difw = 2;
			setZoom(difw, p2);
		} else {
			if (p1.getX() > p2.getX()) {
				int aux = p2.x;
				p2.x = p1.x;
				p1.x = aux;
			}

			if (p1.getY() > p2.getY()) {
				int aux = p2.y;
				p2.y = p1.y;
				p1.y = aux;
			}

			Point2D.Double pSheet1 = FLayoutUtilities.toSheetPoint(
					new Point2D.Double(p1.getX(), p1.getY()), layout.getLayoutControl().getAT());
			Point2D.Double pSheet2 = FLayoutUtilities.toSheetPoint(
					new Point2D.Double(p2.getX(), p2.getY()), layout.getLayoutControl().getAT());

			double xmin;
			double xmax;
			double ymin;
			double ymax = 0;

			if (pSheet1.x > pSheet2.x) {
				xmin = pSheet2.x;
				xmax = pSheet1.x;
			} else {
				xmin = pSheet1.x;
				xmax = pSheet2.x;
			}

			if (pSheet1.y > pSheet2.y) {
				ymin = pSheet2.y;
				ymax = pSheet1.y;
			} else {
				ymin = pSheet1.y;
				ymax = pSheet2.y;
			}

			Rectangle2D.Double rScreen = new Rectangle2D.Double();
			Rectangle2D.Double rSheet = new Rectangle2D.Double();
			double x = FLayoutUtilities.toSheetDistance(
					layout.getLayoutControl().getRect().getX(), layout.getLayoutControl().getAT());
			double y = FLayoutUtilities.toSheetDistance(
					layout.getLayoutControl().getRect().getY(), layout.getLayoutControl().getAT());
			double w = FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect()
					.getWidth(), layout.getLayoutControl().getAT());
			double h = FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect()
					.getHeight(), layout.getLayoutControl().getAT());

			double wv = FLayoutUtilities.toSheetDistance(layout
					.getVisibleRect().getWidth(), layout.getLayoutControl().getAT());
			double hv = FLayoutUtilities.toSheetDistance(layout
					.getVisibleRect().getHeight(), layout.getLayoutControl().getAT());
			double mw = xmax - xmin;
			double mh = ymax - ymin;
			double difw = wv / mw;
			double difh = hv / mh;

			if (difw < difh) {
				rSheet.x = (-xmin * difw)
				- x
				+ ((wv - ((pSheet2.getX() - pSheet1.getX()) * difw)) / 2);
				rSheet.y = (-ymin * difw)
				- y
				+ ((hv - ((pSheet2.getY() - pSheet1.getY()) * difw)) / 2);

				rSheet.width = w * difw;
				rSheet.height = h * difw;
			} else {
				rSheet.x = (-xmin * difh)
				- x
				+ ((wv - ((pSheet2.getX() - pSheet1.getX()) * difh)) / 2);
				rSheet.y = (-ymin * difh)
				- y
				+ ((hv - ((pSheet2.getY() - pSheet1.getY()) * difh)) / 2);

				rSheet.width = w * difh;
				rSheet.height = h * difh;
			}
			setPointsToZoom(p1, p2);
			rScreen.setRect(FLayoutUtilities.fromSheetRect(rSheet, layout.getLayoutControl()
					.getAT()));
			if (FLayoutUtilities.isPosible(rScreen)) {
				layout.getLayoutControl().getRect().setRect(rScreen);
			}
		}
	}

	/**
	 * Realiza un zoom out sobre el Layout que se le pasa como parámetro.
	 *
	 * @param p2 punto central del rectángulo.
	 */
	public void setZoomOut(Point p2) {
		double difw = 0.5;
		setZoom(difw,p2);
	}

	/**
	 * Realiza un zoom out sobre el Layout que se le pasa como parámetro.
	 *
	 * @param dif factor.
	 * @param p2 punto final del recángulo.
	 */
	public void setZoom(double dif, Point p2) {
		Point2D.Double pSheet2 = FLayoutUtilities.toSheetPoint(new Point2D.Double(
				p2.getX(), p2.getY()), layout.getLayoutControl().getAT());
		Rectangle2D.Double rScreen = new Rectangle2D.Double();
		Rectangle2D.Double rSheet = new Rectangle2D.Double();

		double difw = dif;

		rSheet.x = (-pSheet2.getX() * difw) -
		FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect().getX(),
				layout.getLayoutControl().getAT()) +
				FLayoutUtilities.toSheetDistance(layout.getWidth() / 2,
						layout.getLayoutControl().getAT());
		rSheet.y = (-pSheet2.getY() * difw) -
		FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect().getY(),
				layout.getLayoutControl().getAT()) +
				FLayoutUtilities.toSheetDistance(layout.getHeight() / 2,
						layout.getLayoutControl().getAT());

		rSheet.width = FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect()
				.getWidth(),
				layout.getLayoutControl().getAT()) * difw;
		rSheet.height = FLayoutUtilities.toSheetDistance(layout.getLayoutControl().getRect()
				.getHeight(),
				layout.getLayoutControl().getAT()) * difw;

		rScreen.setRect(FLayoutUtilities.fromSheetRect(rSheet, layout.getLayoutControl().getAT()));

		if (FLayoutUtilities.isPosible(rScreen)) {
			layout.getLayoutControl().getRect().setRect(rScreen);
		}

		//		Para realizar el zoom a partir de un punto.
		Point p1 = new Point((int) (p2.getX() -
				(layout.getWidth() / (difw * 2))),
				(int) (p2.getY() - (layout.getHeight() / (difw * 2))));
		p2 = new Point((int) (p2.getX() + (layout.getWidth() / (difw * 2))),
				(int) (p2.getY() + (layout.getHeight() / (difw * 2))));
		setPointsToZoom(p1, p2);
	}
	/**
	 * Introduce los puntos de control para controlar el zoom del Layout.
	 */
	private void setPointsToZoom(Point p1, Point p2) {
		IFFrame[] fframes = layout.getLayoutContext().getFFrames();

		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];
				if (fframe.getATMap()!=null) {
					Point2D px1 = FLayoutFunctions.toMapPoint(p1, fframe.getATMap());
					Point2D px2 = FLayoutFunctions.toMapPoint(p2, fframe.getATMap());
					fframe.setPointsToZoom(px1, px2);
				}
			}
		}
	}

	/**
	 * Aplica el zoom real teniendo en cuenta la resolución de pantalla.
	 */
	public void realZoom() {
		Preferences prefsResolution = Preferences.userRoot().node( "gvsig.configuration.screen" );
		double cm = layout.getLayoutContext().getAttributes().getPixXCm(layout.getLayoutControl().getRect());
		Toolkit kit = Toolkit.getDefaultToolkit();
		double dpi = prefsResolution.getInt("dpi",kit.getScreenResolution());
		double dif = (cm * Attributes.PULGADA) / dpi;
		setZoom(1 / dif,
				new Point(layout.getWidth() / 2, layout.getHeight() / 2));
		layout.getLayoutControl().refresh();
	}

	/**
	 * Realiza un zoom in a partir del zoom actual de la vista.
	 */
	public void zoomIn() {
		setZoom(2, new Point(layout.getWidth() / 2, layout.getHeight() / 2));
		layout.getLayoutControl().refresh();
	}

	/**
	 * Realiza un zoom out a partir del zoom actual de la vista.
	 */
	public void zoomOut() {
		setZoom(0.5, new Point(layout.getWidth() / 2, layout.getHeight() / 2));
		layout.getLayoutControl().refresh();
	}

	/**
	 * Realiza un zoom a los elementos que esten seleccionados, si no hay
	 * ningún elemento seleccionado no realiza ningún zoom
	 */
	public void zoomSelect() {
		Rectangle2D.Double recaux = null;
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i].getSelected() != IFFrame.NOSELECT) {
				if (recaux == null) {
					recaux = fframes[i].getBoundingBox(layout.getLayoutControl().getAT());
				} else {
					recaux.add(fframes[i].getBoundingBox(
							layout.getLayoutControl().getAT()));
				}
			}
		}

		if (recaux != null) {
			Point p1 = new Point((int) recaux.x, (int) recaux.y);
			Point p2 = new Point((int) recaux.getMaxX(), (int) recaux.getMaxY());
			setZoomIn(p1, p2);
			layout.getLayoutControl().refresh();
		}
	}
	/**
	 * Realiza un zoom a todos los elementos del layout.
	 */
	public void zoomAllFrames() {
		Rectangle2D.Double recaux = null;
		IFFrame[] fframes=layout.getLayoutControl().getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (recaux == null) {
				recaux = fframes[i].getBoundingBox(layout.getLayoutControl().getAT());
			} else {
				recaux.add(fframes[i].getBoundingBox(
						layout.getLayoutControl().getAT()));
			}
		}

		if (recaux != null) {
			Point p1 = new Point((int) recaux.x, (int) recaux.y);
			Point p2 = new Point((int) recaux.getMaxX(), (int) recaux.getMaxY());
			setZoomIn(p1, p2);
			layout.getLayoutControl().refresh();
		}
	}

	/**
	 * Realiza un zoom in a las vista añadidas al Layout que esten seleccionadas
	 *
	 * @param p1 Punto inicial del rectángulo
	 * @param p2 Punto final del rectángulo
	 */
	public void setViewZoomIn(Point2D p1, Point2D p2) {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];

				if (((IFFrame) fframe).getSelected() != IFFrame.NOSELECT) {
					//IFFrameUseFMap fframeAux=(IFFrameUseFMap)((IFFrame)fframe).cloneFFrame(layout);
					p1 = FLayoutFunctions.toMapPoint(p1, fframe.getATMap());
					p2 = FLayoutFunctions.toMapPoint(p2, fframe.getATMap());


					// Borramos el anterior
					layout.getLayoutControl().setStatus(LayoutControl.DESACTUALIZADO);
					Rectangle2D.Double r = new Rectangle2D.Double();

					if (java.lang.Math.abs(p1.getX() - p2.getX()) <= 3) {
						double nuevoX;
						double nuevoY;
						double cX;
						double cY;

						cX = p2.getX();
						cY = p2.getY();

						double factor = 1/MapContext.ZOOMINFACTOR;

						Rectangle2D extent=fframe.getMapContext().getViewPort().getExtent();
						if (extent!=null){
							nuevoX = cX -
							((extent.getWidth() * factor) / 2.0);
							nuevoY = cY -
							((extent.getHeight() * factor) / 2.0);
							r.x = nuevoX;
							r.y = nuevoY;
							r.width = extent.getWidth() * factor;
							r.height = extent.getHeight() * factor;
						}

						//fframeAux.setNewExtent(r);
					} else {
						//	Fijamos el nuevo extent

						r.setFrameFromDiagonal(p1, p2);

						//fframeAux.setNewExtent(r);
					}

					/*if (fframe.getTypeScale()!=IFFrameUseFMap.AUTOMATICO) {
                    	fframeAux.setNewExtent(r);
                    	fframeAux.refresh();
                    	layout.getEFS().modifyFFrame((IFFrame)fframe,(IFFrame)fframeAux);
                    	((IFFrame)fframeAux).getBoundingBox(layout.getAT());
                    	layout.updateFFrames();
                    	layout.setIsReSel(true);
                    }else {*/
					try {
						fframe.setNewEnvelope(geomManager.createEnvelope(r.getX(),r.getY(),r.getMaxX(),r.getMaxY(), SUBTYPES.GEOM2D));
					} catch (CreateEnvelopeException e) {
						logger.error("Error creating the envelope", e);
					}
					fframe.refresh();
					///}
					// Fin del else
					//layout.repaint();
				}
			}
		}
	}

	/**
	 * Realiza un zoom out a las vistas añadidas al Layout y que estén seleccionadas
	 *
	 * @param p2 Punto central
	 */
	public void setViewZoomOut(Point p2) {
		Point2D.Double pWorld;
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];

				if (((IFFrame) fframe).getSelected() != IFFrame.NOSELECT) {
					//IFFrameUseFMap fframeAux=(IFFrameUseFMap)((IFFrame)fframe).cloneFFrame(layout);
					double nuevoX;
					double nuevoY;
					double cX;
					double cY;
					Point pScreen = new Point((int) p2.getX(), (int) p2.getY());
					pWorld = FLayoutFunctions.toMapPoint(pScreen,
							fframe.getATMap());

					cX = pWorld.getX();
					cY = pWorld.getY();

					double factor = 1/MapContext.ZOOMOUTFACTOR;
					Rectangle2D extent = fframe.getMapContext()
					.getViewPort().getExtent();
					if (extent != null) {
						nuevoX = cX - ((extent.getWidth() * factor) / 2.0);
						nuevoY = cY - ((extent.getHeight() * factor) / 2.0);
						double x = nuevoX;
						double y = nuevoY;
						double width = extent.getWidth() * factor;
						double height = extent.getHeight() * factor;
						try {
							fframe.setNewEnvelope(geomManager.createEnvelope(x,y,x+width,y+height, SUBTYPES.GEOM2D));
						} catch (CreateEnvelopeException e) {
							logger.error("Error creating the envelope", e);
						}
						fframe.refresh();
					}
				}
			}
		}
	}

	/**
	 * Modifica los puntos de control para generar el zoom del Layout
	 *
	 * @param p1 Punto inicial
	 * @param p2 Punto final
	 */
	public void setPan(Point p1, Point p2) {
		IFFrame[] fframes = layout.getLayoutContext().getFFrames();

		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];
				AffineTransform at=fframe.getATMap();
				if (at!=null) {
					Point2D px1 = FLayoutFunctions.toMapPoint(p1, at);
					Point2D px2 = FLayoutFunctions.toMapPoint(p2, at);
					fframe.movePoints(px1, px2);
				}
			}
		}
	}
}
