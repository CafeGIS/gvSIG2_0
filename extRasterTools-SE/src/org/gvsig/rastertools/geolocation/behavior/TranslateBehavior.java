/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
 */
package org.gvsig.rastertools.geolocation.behavior;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
/**
 * Comportamiento que se aplica a la herramienta de translate. El cursor del ratón cambiará cuando
 * se encuentre en la zona interior al raster, permitiendo el deformar la imagen al pinchar y
 * arrastrar.
 *
 * Nacho Brodin (nachobrodin@gmail.com)
 */
public class TranslateBehavior extends TransformationBehavior {

	private final Image handCursor =PluginServices.getIconTheme().get("hand-icon").getImage();

//	private Cursor                         cur = null;
	private boolean                        isMoveable = false;

	/**
	 * Variable booleana que está a true si el cursor por defecto está
	 * activo y a false si hay otro.
	 */
	private boolean 						defaultCursorActive = true;

	/**
	 * Puntos de inicio y final para el arrastre de la imagen.
	 */
	private Point2D 						ptoIni = null;
	private Point2D 						ptoFin = null;

	/**
	 * Crea un nuevo RectangleBehavior.
	 *
	 * @param zili listener.
	 */
	public TranslateBehavior(GeoRasterBehavior grb, Image curImage, ITransformIO windowIO) {
		grBehavior = grb;
		defaultImage = curImage;
		this.trIO = windowIO;
	}

	/**
	 * Coloca el cursor del ratón con el icono adecuado cuando entra dentro de la
	 * imagen.
	 */
	public boolean mouseMoved(MouseEvent e) throws BehaviorException {

		ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();

		try {
			lyr = grBehavior.getLayer();
			if (lyr == null) {
				setActiveTool(false);
				return false;
			}

			Point2D pto = vp.toMapPoint(e.getX(), e.getY());

			try {
				if (lyr.isInside(pto)) {
					grBehavior.getMapControl().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(handCursor,
							new Point(16, 16), ""));
					defaultCursorActive = false;
					setActiveTool(true);
					return true;
				} else {
					if (!defaultCursorActive) {
//						grBehavior.getMapControl().setCursor(defaultCursor);
						defaultCursorActive = true;
						setActiveTool(false);
						return false;
					}
				}
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (IndexOutOfBoundsException e1) {
				e1.printStackTrace();
			}
		} catch(ClassCastException exc){
			RasterToolsUtil.messageBoxError("error_capa_puntos", this, exc);
		}
		setActiveTool(false);
		return false;
	}

	/**
	 * Si no está activo el cursor por defecto capturamos el punto
	 * seleccionado en coordenadas del mundo real.
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		if (e.getButton() == MouseEvent.BUTTON1 && !defaultCursorActive) {
			setActiveTool(true);
			lyr = grBehavior.getLayer();

			if (lyr == null)
				return;

			ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
			ptoIni = vp.toMapPoint(e.getPoint());

			isMoveable = true;
		}
	}

	/**
	 *  Cuando soltamos el botón del ratón desplazamos la imagen a la posición
	 * de destino calculando el extent nuevamente.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		if (!isActiveTool())
			return;
		if (e.getButton() == MouseEvent.BUTTON1 && isMoveable){
			FLyrRasterSE lyr = grBehavior.getLayer();
			if (lyr == null)
				return;

			ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
			ptoFin = vp.toMapPoint(e.getPoint());

			//Asignamos la nueva matriz de transformación a la capa
			AffineTransform atOld = lyr.getAffineTransform();
			AffineTransform atNew = null;

			double distX = ptoFin.getX() - ptoIni.getX();
			double distY = ptoFin.getY() - ptoIni.getY();

			//La nueva matriz de transformación es la vieja más la distancia desplazada
			atNew = new AffineTransform(atOld.getScaleX(), atOld.getShearY(),
										atOld.getShearX(), atOld.getScaleY(),
										atOld.getTranslateX() + distX,
										atOld.getTranslateY() + distY);
			lyr.setAffineTransform(atNew);

			grBehavior.getMapControl().getMapContext().invalidate();
			isMoveable = false;
			super.mouseReleased(e);
		}

	}

	/**
	 * <P>
	 * Función de pintado del canvas. Pintamos un marco a la imagen para saber
	 * donde la movemos.
	 * </P>
	 * <P>
	 * Para dibujar el marco alrededor del raster hacemos lo mismo que para pintar el raster
	 * rotado. En realidad dibujamos un cuadrado y luego le aplicamos las transformaciones necesarias
	 * para que se vea con la misma forma del raster al que representa.
	 * </P>
	 */
	public void paintComponent(Graphics g) {
		if (isMoveable && lyr != null && ptoFin != null && ptoIni != null) {
			try {
				ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
				AffineTransform at = (AffineTransform)lyr.getAffineTransform().clone();
				at.preConcatenate(vp.getAffineTransform());
				Extent ext = lyr.getFullRasterExtent();

				Point2D pt = new Point2D.Double(ext.getULX(), ext.getULY());
				vp.getAffineTransform().transform(pt, pt);
				at.inverseTransform(pt, pt);

				Point2D size = new Point2D.Double(ext.getLRX(), ext.getLRY());
				vp.getAffineTransform().transform(size, size);
				at.inverseTransform(size, size);

				double distX = ptoFin.getX() - ptoIni.getX();
				double distY = ptoFin.getY() - ptoIni.getY();
				Point2D d = new Point2D.Double(ext.getULX() + distX, ext.getULY() + distY);
				vp.getAffineTransform().transform(d, d);
				at.inverseTransform(d, d);

				//Giramos el graphics se dibuja y se vuelve a dejar como estaba
				((Graphics2D)g).transform(at);
				g.setColor(rectangleColor);
				AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f);
				((Graphics2D)g).setComposite(alpha);
				g.fillRect((int)pt.getX() + (int)d.getX(), (int)pt.getY() + (int)d.getY(), (int)size.getX(), (int)size.getY());
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				g.drawRect((int)pt.getX() + (int)d.getX(), (int)pt.getY() + (int)d.getY(), (int)size.getX(), (int)size.getY());
				((Graphics2D)g).transform(at.createInverse());
			} catch (NoninvertibleTransformException e1) {
				RasterToolsUtil.messageBoxError("error_transformacion1", this, e1);
			}
		}
	}

	/**
	 * Esta función repinta el canvas si se está arrasrtando la imagen para
	 * poder ver el marco de arrastre.
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		if (!isActiveTool())
			return;
		if (isMoveable) {
			ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
			ptoFin = vp.toMapPoint(e.getPoint());

			//Asignación de las coordenadas temporales al dialogo
			assignTransformToDialog();

			grBehavior.getMapControl().repaint();
		}
	}

	/**
	 * Asigna las coordenadas temporales en el dialogo.
	 */
	public void assignTransformToDialog() {
		AffineTransform atNew = null;
		double distX = ptoFin.getX() - ptoIni.getX();
		double distY = ptoFin.getY() - ptoIni.getY();
		AffineTransform atOld = grBehavior.getLayer().getAffineTransform();
		//La nueva matriz de transformación es la vieja más la distancia desplazada
		atNew = new AffineTransform(atOld.getScaleX(), atOld.getShearY(),
									atOld.getShearX(), atOld.getScaleY(),
									atOld.getTranslateX() + distX,
									atOld.getTranslateY() + distY);
		trIO.loadTransform(atNew);
	}

	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return null;
	}
}