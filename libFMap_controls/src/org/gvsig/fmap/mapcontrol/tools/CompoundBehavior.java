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
package org.gvsig.fmap.mapcontrol.tools;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Listeners.ToolListener;



/**
 * <p>Allows having multiple behaviors when user works with the associated
 *  <code>MapControl</code>. Each one with its associated tool listener.</p>
 *
 * @author Fernando González Cortés
 * @author Pablo Piqueras Bartolomé
 */
public class CompoundBehavior extends Behavior {
	/**
	 * List of all behaviors that compound this one.
	 */
	private ArrayList behaviors = new ArrayList(); //<Behavior>

	/**
	 * List that determines which behaviors of this one will be real-time painted.
	 */
	private ArrayList draws = new ArrayList(); //<Boolean>

	/**
	 * Static <code>behavior</code> that will be used for any <code>MapControl</code>.</p>
	 */
	private static Behavior behavior = null;

	/**
 	 * <p>Creates a new behavior as a composition of others.</p>
 	 *
	 * @param tools atomic behaviors that will compound this one</code>
	 */
	public CompoundBehavior(Behavior[] behaviors){
		for (int i = 0; i < behaviors.length; i++) {
			this.behaviors.add(behaviors[i]);

			if (i == 0)
				draws.add(Boolean.TRUE);
			else
				draws.add(Boolean.FALSE);
		}
	}

	/**
	 * <p>Adds a new behavior, setting if will be real-time (when user is working with it) drawn or not.</p>
	 *
	 * <p>When user works with a compound behavior, he/she will see on real-time the graphical changes produced at
	 *  the associated <code>MapControl</code>, only by those which have their associated <i>draw</i> flag to <code>true</code>.</p>
	 *
	 * @param mt the new behavior
	 * @param draw flag determining if will be real-time drawn or no
	 */
	public void addMapBehavior(Behavior mt, boolean draw){
		behaviors.add(mt);
		draws.add(new Boolean(draw));
	}

	/**
	 * <p>Removes a <code>Behavior</code> that composes this one.</p>
	 *
	 * @param mt the <code>Behavior</code> to be removed
	 */
	public void removeMapBehavior(Behavior mt){
		int index = behaviors.indexOf(mt);

		if (index >= 0) {
			behaviors.remove(index);
			draws.remove(index);
		}
	}
	/**
	 * <p>Searches for <code>mt</code>, returning <code>true</code> if is contained.</p>
	 *
	 * @param mt the behavior to search
	 *
	 * @return <code>true</code> if is contained; otherwise <code>false</code>
	 */
	public boolean containsBehavior(Behavior mt) {
		return behaviors.indexOf(mt) > -1;
	}

	/**
	 * <p>Returns the first-level {@link Behavior Behavior} at the specified position.</p>
	 *
	 * @param index index of element to return
	 *
	 * @return the element at the specified position.
	 */
	public Behavior getBehavior(int index) {
		return (Behavior)behaviors.get(index);
	}

	/**
	 * <p>Returns if it's invoked the method <code>public void paintComponent(Graphics g)</code> of the
	 *  first-level {@link Behavior Behavior} at the specified position, each time is painted this component.</p>
	 *
	 * @param index index of element
	 *
	 * @return <code>true</code>  if it's invoked the method <code>public void paintComponent(Graphics g)</code> of the
	 *   first-level {@link Behavior Behavior} at the specified position, each time is painted this component, otherwise
	 *   <code>false</code>.
	 */
	public boolean isDrawnBehavior(int index) {
		return ((Boolean)draws.get(index)).booleanValue();
	}

	/**
	 * <p>Sets if will be invoked the method <code>public void paintComponent(Graphics g)</code> of the
	 *  first-level {@link Behavior Behavior} at the specified position, each time is painted this component.</p>
	 *
	 * @param index index of element
	 * @param <code>true</code> if will be invoked the method <code>public void paintComponent(Graphics g)</code> of the
	 *   first-level {@link Behavior Behavior} at the specified position, each time is painted this component, otherwise
	 *   <code>false</code>.
	 */
	public void setDrawnBehavior(int index, boolean draw) {
		draws.set(index, new Boolean(draw));
	}

	/**
	 * <p>Returns the number of first-level {@link Behavior Behavior}s in this <code>CompoundBehavior</code>.</p>
	 *
	 * @return the number of first-level {@link Behavior Behavior}s in this <code>CompoundBehavior</code>
	 */
	public int size() {
		return behaviors.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getCursor()
	 */
	public Image getImageCursor() {
		if (behaviors.size() > 0) {
			return ((Behavior) behaviors.get(0)).getImageCursor();
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseClicked(e);
		}

		if(behavior != null)
			behavior.mouseClicked(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseDragged(e);
		}

		if(behavior != null)
			behavior.mouseDragged(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseEntered(e);
		}

		if(behavior != null)
			behavior.mouseEntered(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseExited(e);
		}

		if(behavior != null)
			behavior.mouseExited(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseMoved(e);
		}

		if(behavior != null)
			behavior.mouseMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mousePressed(e);
		}

		if(behavior != null)
			behavior.mousePressed(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseReleased(e);
		}

		if(behavior != null)
			behavior.mouseReleased(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent e) throws BehaviorException {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].mouseWheelMoved(e);
		}

		if(behavior != null)
			behavior.mouseWheelMoved(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		for (int i = 0; i < behaviors.size(); i++) {
			Behavior mapTool = (Behavior) behaviors.get(i);

			if (((Boolean) draws.get(i)).booleanValue())
				mapTool.paintComponent(g);
		}
	}

	/**
	 * Sets a tool listener to work with a <code>MapControl</code> instance using these behaviors.
	 *
	 * @param listener a <code>RectangleListener</code> object for this behavior
	 */
	public void setListener(ToolListener listener) {
		if (listener != null) {
			throw new RuntimeException(
				"CompoundBehavior does not have listeners");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#getListener()
	 */
	public ToolListener getListener() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.tools.Behavior.Behavior#setMapControl(com.iver.cit.gvsig.fmap.MapControl)
	 */
	public void setMapControl(MapControl mc) {
		Behavior[] behaviors=(Behavior[])this.behaviors.toArray(new Behavior[0]);
		for (int i=0;i<behaviors.length;i++ ) {
			behaviors[i].setMapControl(mc);
		}

		super.setMapControl(mc);
	}

	/**
	 * <p>Sets the <code>Behavior</code> that will be used for any <code>MapControl</code>.</p>
	 *
	 * @param behavior the multi-view <code>Behavior</code>
	 */
	public static void setAllControlsBehavior(Behavior behavior) {
		CompoundBehavior.behavior = behavior;
	}

	/**
	 * <p>Gets the <code>Behavior</code> that will be used for any <code>MapControl</code>.</p>
	 *
	 * @return the multi-view <code>Behavior</code>
	 */
	public static Behavior getAllControlsBehavior() {
		return CompoundBehavior.behavior;
	}
}

