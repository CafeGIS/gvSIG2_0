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
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.tools.BehaviorException;
import org.gvsig.fmap.mapcontrol.tools.Listeners.RectangleListener;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.util.RasterToolsUtil;


/**
 * Comportamiento que se aplica a la herramienta de shear. El cursor del ratón cambiará cuando
 * se encuentre en la zona exterior al raster, permitiendo el deformar la imagen al pinchar y
 * arrastrar en los ejes X e Y.
 *
 * Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class ShearBehavior extends TransformationBehavior {
	//Número de pixeles de ancho del borde donde el cursor se activará. Son pixeles del canvas de la vista.
	//De esta forma da igual la escala a la que esté la imagen siempre tiene la misma precisión
	private int 							PX_SELEC_BASE = 12;
	private int 							PX_SELEC = PX_SELEC_BASE;

	private RectangleListener listener;

	private final Image shearYImg = new ImageIcon(getClass().getClassLoader().getResource(
		"images/y.gif")).getImage();
	private final Image shearXImg = new ImageIcon(getClass().getClassLoader().getResource(
		"images/x.gif")).getImage();

	/**
	 * Puntos de inicio y final para el arrastre de la imagen.
	 */
	private Point2D 						beforePoint = null;
	private Point2D 						nextPoint = null;

	private AffineTransform                 boxShear = null;

	/**
	 * Lista de flags de redimensionado para cada lado del raster
	 * [0]-esquina superior derecha
	 * [1]-esquina superior izquierda
	 * [2]-esquina inferior derecha
	 * [3]-esquina inferior izquierda
	 */
	private boolean[]						cornerActive = {false, false, false, false};
	private boolean 						init = false;
	/**
	 * Controla si se ha activado el shear o no
	 */
	private boolean                         isShearing = false;
	/**
	 * Contiene el valor del incremento
	 */
	private double							incrShear = 0.01;
	/**
	 * Control del valor del shearing en X
	 */
	private double                          shearX = 0;
	/**
	 * Control del valor del shearing en Y
	 */
	private double                          shearY = 0;


	/**
	 * Crea un nuevo RectangleBehavior.
	 *
	 * @param zili listener.
	 */
	public ShearBehavior(GeoRasterBehavior grb, Image curImage, ITransformIO windowIO) {
		grBehavior = grb;
		defaultImage = curImage;
		this.trIO = windowIO;
	}

	/**
	 * Inicialización. Calcula la longitud de la esquina y el ancho en píxeles
	 * de la zona de detección del puntero de ratón. Inicialmente esto está calculado en
	 * unidades pixel de la vista pero la detección de puntero, debido a la rotación posible
	 * del raster ha de hacerse en unidades del raster en disco por ello ha de calcularse
	 * una escala entre ambas unidades cuando se carga la aplicación.
	 *
	 */
	private void init() {
		ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
		lyr = grBehavior.getLayer();
		if(lyr == null)
			return ;

		Extent ext = lyr.getDataSource().getExtent();
		AffineTransform atImg = lyr.getAffineTransform();

		//Establecer una escala entre las coordenadas de la vista y las coordenadas pixel
		Point2D ul = new Point2D.Double(ext.getULX(), ext.getULY());
		Point2D lr = new Point2D.Double(ext.getLRX(), ext.getLRY());
		Point2D ulPx = new Point2D.Double();
		Point2D lrPx = new Point2D.Double();
		Point2D ulVp = new Point2D.Double();
		Point2D lrVp = new Point2D.Double();
		double esc = 1;
		try {
			atImg.inverseTransform(ul, ulPx);
			atImg.inverseTransform(lr, lrPx);
			ulVp = vp.fromMapPoint(ul);
			lrVp = vp.fromMapPoint(lr);
			esc = Math.abs(lrPx.getX() - ulPx.getX()) / Math.abs(lrVp.getX() - ulVp.getX());
		} catch (NoninvertibleTransformException e1) {
			RasterToolsUtil.messageBoxError("error_transformacion1", this, e1);
			return;
		}

		//Escalar PX_SELEC y LONG_CORNER para tenerlo en coordenadas pixel

		PX_SELEC = (int)(PX_SELEC_BASE * esc);
		init = true;
	}

	/**
	 * Cuando se produce un evento de pintado dibujamos el marco de la imagen para
	 * que el usuario pueda seleccionar y redimensionar.
	 */
	public void paintComponent(Graphics g) {
		if(lyr == null)
			lyr = grBehavior.getLayer();

		if(isShearing && lyr != null) {
			try {
				ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
				AffineTransform at = new AffineTransform(lyr.getAffineTransform());
				Extent ext = lyr.getFullRasterExtent();
				Point2D center = new Point2D.Double(lyr.getPxWidth() / 2, lyr.getPxHeight()/ 2);
				at.transform(center, center);

				Point2D ul = new Point2D.Double(ext.getULX(), ext.getULY());
				Point2D lr = new Point2D.Double(ext.getLRX(), ext.getLRY());

				AffineTransform T1 = new AffineTransform(1, 0, 0, 1, -center.getX(), -center.getY());
				AffineTransform R1 = new AffineTransform();
				R1.setToShear(shearX, shearY);
				AffineTransform T2 = new AffineTransform(1, 0, 0, 1, center.getX(), center.getY());
				T2.concatenate(R1);
				T2.concatenate(T1);

				T2.transform(ul, ul);
				T2.transform(lr, lr);

				at.preConcatenate(T2);
				boxShear = new AffineTransform(at);
				at.preConcatenate(vp.getAffineTransform());

				vp.getAffineTransform().transform(ul, ul);
				at.inverseTransform(ul, ul);

				vp.getAffineTransform().transform(lr, lr);
				at.inverseTransform(lr, lr);

				((Graphics2D)g).transform(at);
				g.setColor(rectangleColor);
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
				g.fillRect((int)ul.getX(), (int)ul.getY(), (int)lr.getX(), (int)lr.getY());
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				g.drawRect((int)ul.getX(), (int)ul.getY(), (int)lr.getX(), (int)lr.getY());
				((Graphics2D)g).transform(at.createInverse());
			} catch (NoninvertibleTransformException e1) {
				RasterToolsUtil.messageBoxError("error_transformacion1", this, e1);
			}
		}
	}

	/**
	 * Reimplementación del método mousePressed de Behavior. Si no está
	 * activo el cursor por defecto capturamos el punto seleccionado en
	 * coordenadas del mundo real.
	 *
	 * @param e MouseEvent
	 */
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
			beforePoint = vp.toMapPoint(e.getPoint());
			if(lyr == null)
				lyr = grBehavior.getLayer();
			isShearing = true;
		}
	}

	/**
	 * Reimplementación del método mouseReleased de Behavior. Desactivamos
	 * los flags de redimensionado y a partir de la selección del usuario
	 * creamos un nuevo extent para la imagen. Con este extent creamos una nueva
	 * capa que sustituirá a la anterior.
	 *
	 * @param e MouseEvent
	 *
	 * @throws BehaviorException Excepción lanzada cuando el Behavior.
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		if(e.getButton() == MouseEvent.BUTTON1) {
			lyr.setAffineTransform(boxShear);
			shearX = shearY = 0;
			grBehavior.getMapControl().getMapContext().invalidate();
			isShearing = false;
			super.mouseReleased(e);
		}
	}

	/**
	 * Cuando arrastramos con el ratón pulsado significa que estamos rotando la imagen. Para realizar la rotación
	 * medimos la distancia al punto inicial (desde el pto que estamos al pto donde se picho la primera vez). Esta distancia
	 * nos da la velocidad de giro ya que si es grande es que lo habremos movido deprisa y si es pequeña habremos arrastrado
	 * el ratón despacio. Creamos una matriz de transformación con la identidad y la rotación que hemos medido aplicada. Al
	 * final repintamos para que se pueda usar esta rotación calculada en el repintado.
	 */
	public void mouseDragged(MouseEvent e) {
		ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
		nextPoint = vp.toMapPoint(e.getPoint());

		if( (cornerActive[0] && (beforePoint.getY() >= nextPoint.getY())) ||
			(cornerActive[1] && (beforePoint.getY() < nextPoint.getY())) )
			shearY += incrShear;
		if( (cornerActive[0] && (beforePoint.getY() < nextPoint.getY())) ||
			(cornerActive[1] && (beforePoint.getY() >= nextPoint.getY())) )
				shearY -= incrShear;
		if( (cornerActive[2] && (beforePoint.getX() >= nextPoint.getX())) ||
			(cornerActive[3] && (beforePoint.getX() < nextPoint.getX())) )
				shearX += incrShear;
		if( (cornerActive[2] && (beforePoint.getX() < nextPoint.getX())) ||
			(cornerActive[3] && (beforePoint.getX() >= nextPoint.getX())) )
				shearX -= incrShear;

		beforePoint = vp.toMapPoint(e.getPoint());
		if(boxShear != null)
			trIO.loadTransform(boxShear);
		grBehavior.getMapControl().repaint();
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#setListener(org.gvsig.georeferencing.fmap.tools.ToolListener)
	 */
	public void setListener(ToolListener listener) {
		this.listener = (RectangleListener) listener;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return listener;
	}

	/**
	 * Cuando movemos el ratón detecta si estamos en el marco de la
	 * imagen y pone el icono del cursor del ratón adecuado.
	 */
	public boolean mouseMoved(MouseEvent ev) throws BehaviorException {
		if(!init)
			init();

		ViewPort vp = grBehavior.getMapControl().getMapContext().getViewPort();
		resetBorderSelected();

		lyr = grBehavior.getLayer();
		if(lyr == null) {
			setActiveTool(false);
			return false;
		}

		AffineTransform atImg = lyr.getAffineTransform();

		//Pasar coordenadas del punto a coordenadas reales y luego a coordenadas pixel
		Point2D e = vp.toMapPoint(ev.getX(), ev.getY());
		try {
			atImg.inverseTransform(e, e);
		} catch (NoninvertibleTransformException e1) {
			return false;
		}

		//Comprobar si está dentro del raster
		Point2D p1 = new Point2D.Double(0, 0);
		Point2D p2 = new Point2D.Double(lyr.getDataSource().getWidth(), lyr.getDataSource().getHeight());

		//System.out.println("--->" + e.getX() + " , " + e.getY());

		//lateral derecho
		if (e.getY() > p1.getY() && e.getY() < p2.getY() && e.getX() > p2.getX() && e.getX() < (p2.getX() + PX_SELEC)) {
			setCursor(shearYImg);
			setActiveTool(true);
			cornerActive[1] = true;
			return true;
		}

		//lateral izquierdo
		if (e.getY() > p1.getY() && e.getY() < p2.getY() && e.getX() < p1.getX() && e.getX() > (p1.getX() - PX_SELEC)) {
			setCursor(shearYImg);
			setActiveTool(true);
			cornerActive[0] = true;
			return true;
		}

		//lateral superior
		if (e.getX() > p1.getX() && e.getX() < p2.getX() && e.getY() < p1.getY() && e.getY() > (p1.getX() - PX_SELEC)) {
			setCursor(shearXImg);
			setActiveTool(true);
			cornerActive[3] = true;
			return true;
		}

		//lateral inferior
		if (e.getX() > p1.getX() && e.getX() < p2.getX() && e.getY() > p2.getY() && e.getY() < (p2.getY() + PX_SELEC)) {
			setCursor(shearXImg);
			setActiveTool(true);
			cornerActive[2] = true;
			return true;
		}

		grBehavior.getMapControl().repaint();
//		grBehavior.getMapControl().setCursor(defaultCursor);
		return false;
	}

	/**
	 * Pone a false todos los elementos del array sideActive. Esto es equivalente
	 * a eliminar cualquier selección de borde.
	 */
	private void resetBorderSelected() {
		for (int i = 0; i < cornerActive.length; i++)
			cornerActive[i] = false;
	}

}
