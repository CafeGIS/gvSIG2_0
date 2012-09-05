package com.iver.cit.gvsig.project.documents.layout.tools.behavior;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.gvsig.fmap.mapcontrol.tools.BehaviorException;

import com.iver.cit.gvsig.project.documents.layout.LayoutControl;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutToolListener;
/**
 * Layout behavior.
 *
 * @author Vicente Caballero Navarro
 */
public interface ILayoutBehavior {

	/**
	 * Devuelve el ToolListener que está seleccionado.
	 *
	 * @return ToolListener seleccionado.
	 */
	public LayoutToolListener getListener();

	/**
	 * @see org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g);

	/**
	 * Inserta el Layout.
	 *
	 * @param lc Layout a insertar.
	 */
	public void setLayoutControl(LayoutControl lc);

	/**
	 * Devuelve la imagen del cursor de la herrameinta.
	 *
	 * @return Image cursor de la herramienta.
	 */
	public Image getImageCursor();

	/**
	 * Devuelve el Layout.
	 *
	 * @return Layout.
	 */
	public LayoutControl getLayoutControl();

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) throws BehaviorException;

	/**
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) throws BehaviorException;

	public boolean isAdjustable();

}