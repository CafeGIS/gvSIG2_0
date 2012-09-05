/*
 * Created on 20-feb-2004
 *
 */
/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.gvsig.fmap.mapcontext.rendering.symbols.IPrintable;

import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


/**
 * Interface que implementa FFrame.
 *
 * @author Vicente Caballero Navarro
 */
public interface IFFrame extends IPrintable{
	public static final int N = 1;
    public static final int NE = 2;
    public static final int E = 3;
    public static final int SE = 4;
    public static final int S = 5;
    public static final int SO = 6;
    public static final int O = 7;
    public static final int NO = 8;
    public static final int RECT = 9;
    /** FFrame no selecccionado. */
    public static final int NOSELECT = 0;

    /**
     * Devuelve el boundingBox del fframe en funci�n de la transformada af�n
     * que se pasa como par�metro. Si se pasa como par�metro null, devuelve el
     * �ltimo boundingbox que se calcul�.
     *
     * @param at Transformada af�n
     *
     * @return Rect�ngulo que representa el BoundingBox del fframe.
     */
    public Rectangle2D.Double getBoundingBox(AffineTransform at);

    /**
     * Devuelve el rect�ngulo que representa el fframe en cent�metros.
     *
     * @return Rect�ngulo en centimetros.
     */
    public Rectangle2D.Double getBoundBox();

    /**
     * Rellena con el rect�ngulo que se pasa como par�metro el boundBox(en
     * cent�metros) del fframe del cual con una transformaci�n se podr�
     * calcular el BoundingBox (en pixels).
     *
     * @param rect Rect�ngulo en cent�metros.
     */
    public void setBoundBox(Rectangle2D rect);

    /**
     * Este m�todo se implementa en cada una de las fframe, ya que cada una se
     * dibuja de una forma diferente sobre el graphics. M�todo que dibuja
     * sobre el graphics que se le pasa como par�metro, seg�n la transformada
     * afin que se debe de aplicar y el rect�ngulo que se debe de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param r rect�ngulo sobre el que hacer un clip.
     * @param imgBase Imagen para acelerar el dibujado.
     * @throws ReadDriverException TODO
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D r,
        BufferedImage imgBase);

//    /**
//     * Implementado para imprimir.
//     *
//     * @param g Graphics2D de la impresora sobre el que dibujar.
//     * @param at DOCUMENT ME!
//     */
//    public void print(Graphics2D g, AffineTransform at, FShape shape)
//        throws ReadDriverException;

    /**
     * Devuelve el nombre que representa al fframe.
     *
     * @return String nombre del FFrame.
     */
    public String getName();

    /**
     * Rellena el String que representa al nombre del fframe.
     *
     * @param n nombre del fframe.
     */
    public void setName(String n);

    /**
     * Devuelve true, si el punto que se pasa como par�metro esta contenido
     * dentro del boundingbox del fframe.
     *
     * @param p punto a comprobar.
     *
     * @return true si el punto esta dentro del boundingbox.
     */
    public boolean contains(Point2D p);

    /**
     * Dibuja los handlers sobre el boundingBox en el graphics que se pasa como
     * par�metro.
     *
     * @param g Graphics sobre el que dibujar.
     */
    public void drawHandlers(Graphics2D g);

    /**
     * Pasando como par�metro true,  se toma como que est� seleccionado el
     * fframe  y si es false como que esta sin seleccionar, de esta forma se
     * selecciona un fframe directamente sin comprobar si un punto est�
     * contenido en �l.
     *
     * @param b true si se quiere seleccionar y false si se quiere
     *        deseleccionar.
     */
    public void setSelected(Point2D b, MouseEvent e);

    /**
     * Establece que tipo de selecci�n se realiza sobre el fframe.
     *
     * @param b punto sobre el que se debe de establecer si se selecciona o no
     *        el fframe.
     */
    public void setSelected(boolean b);

    /**
     * Devuelve un entero que representa el tipo de selecci�n que se ha
     * realizado sobre el fframe.
     *
     * @return tipo de selecci�n que se ha realizado.
     */
    public int getSelected();

    /**
     * Devuelve un entero que representa donde esta contenido el punto que se
     * pasa como par�metro.
     *
     * @param p punto a comparar.
     *
     * @return entero que representa como esta contenido el punto.
     */
    public int getContains(Point2D p);

    /**
     * Devuelve el Cursor adecuado seg�n como est� contenido el punto, si es
     * para desplazamiento, o cambio de tama�o.
     *
     * @param p punto a comprobar.
     *
     * @return Cursor adecuado a la posici�n.
     */
    public Image getMapCursor(Point2D p);

    /**
     * Devuelve el rect�ngulo a partir del desplazamiento en el eje x y el
     * desplazamiento en el eje y.
     *
     * @param difx desplazamiento sobre el eje x.
     * @param dify desplazamiento sobre el eje y.
     *
     * @return rect�ngulo modificado en funci�n del desplazamiento realizado.
     */
    public Rectangle2D getMovieRect(int difx, int dify);

    /**
     * Devuelve un Objeto XMLEntity con la informaci�n los atributos necesarios
     * para poder despu�s volver a crear el objeto original.
     *
     * @return XMLEntity.
     *
     * @throws XMLException
     */
    public XMLEntity getXMLEntity() throws SaveException;

    /**
     * Dibuja un rect�ngulo de color gris,  con el nombre en el centro y
     * escalado en forma de borrador.
     *
     * @param g Graphics sobre el que se dibuja.
     */
    public void drawDraft(Graphics2D g);

    /**
     * Actualiza el BoundBox del FFrame a partir de su rect�ngulo en pixels y
     * la matriz de transformaci�n.
     *
     * @param r Rect�ngulo.
     * @param at Matriz de transformaci�n.
     */
    public void updateRect(Rectangle2D r, AffineTransform at);

    /**
     * Devuelve true si el rect�ngulo primero es null o si es distinto de null
     * e intersecta.
     *
     * @param rv Rect�ngulo
     * @param r Rect�ngulo
     *
     * @return True si intersecta o es null.
     */
    public boolean intersects(Rectangle2D rv, Rectangle2D r);

    /**
     * Introduce el n�mero de FFrame en el que de que se trata.
     *
     * @param i n�mero de FFrame
     */
    public void setNum(int i);

    /**
     * A partir del xml y de Project inicia el objeto.
     *
     * @param xml XMLEntity
     */
    public void setXMLEntity(XMLEntity xml);


    /**
     * Devuelve el nombre que representa al tipo de FFrame.
     *
     * @return nombre del elemento.
     */
    public String getNameFFrame();

    /**
     * Abre la ventana para modificar el tag.
     */
    public void openTag();

    /**
     * Rellena el tag.
     *
     * @param s valor del tag.
     */
    public void setTag(String s);

    /**
     * Devuelve el valor del tag.
     *
     * @return String del valor del tag.
     */
    public String getTag();

    /**
     * Dibuja el s�mbolo que indica que el FFrame contiene un tag.
     *
     * @param g Graphics sobre el que dibujar.
     */
    public void drawSymbolTag(Graphics2D g);

    /**
     * Rellena la rotaci�n del BoundingBox.
     *
     * @param rotation rotaci�n que se quiere aplicar.
     */
    public void setRotation(double rotation);

    /**
     * Devuelve la rotaci�n del BoundingBox.
     *
     * @return Rotaci�n del BoundingBox.
     */
    public double getRotation();

    /**
     * Devuelve el nivel al que se encuentra el FFrame en el Layout.
     *
     * @return nivel.
     */
    public int getLevel();

    /**
     * Inserta el nivel de dibujado del FFrame respecto del resto de fframes
     * del Layout.
     *
     * @param l nivel.
     */
    public void setLevel(int l);
    public IFFrame cloneFFrame(Layout layout);
	public Rectangle2D getLastMoveRect();
	 /**
     * Operaciones necesarias para finalizar correctamente el clonado de un frame, por ejemplo los listeners.
     *
     * @param frame IFFrame del cual copiar los listeners.
     */
	public void cloneActions(IFFrame frame);
	public IFFrameDialog getPropertyDialog();
	public void setLayout(Layout layout);
	public Layout getLayout();

	public void setFrameLayoutFactory(FrameFactory flf);
	public FrameFactory getFrameLayoutFactory();
}
