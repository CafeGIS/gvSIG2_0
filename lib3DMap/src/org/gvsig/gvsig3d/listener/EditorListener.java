package org.gvsig.gvsig3d.listener;

/* gvSIG. Geographic Information System of the Valencian Government
 *  osgVP. OSG Virtual Planets.
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */
/*
 * AUTHORS (In addition to CIT):
 * 2008 Instituto de Automática e Informática Industrial, UPV.
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.manipulator.AddSelectionCommand;
import org.gvsig.osgvp.manipulator.EditionManager;
import org.gvsig.osgvp.manipulator.Manipulator;
import org.gvsig.osgvp.manipulator.ManipulatorHandler;
import org.gvsig.osgvp.manipulator.RemoveAllSelectionCommand;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.util.ActionCommand;
import org.gvsig.osgvp.util.Util;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.Intersection;
import org.gvsig.osgvp.viewer.Intersections;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.cit.gvsig.fmap.layers.FLayer;

public class EditorListener implements MouseListener, KeyListener {

	private boolean _lockPick = false;
	private EditionManager _manager;
	private ManipulatorHandler _handler;
	private IViewerContainer viewer;
	private FLayer _layer;
	IViewerContainer _canvas3d;
	private Camera _cam;
	private static Logger logger = Logger.getLogger(EditorListener.class
			.getName());

	private PlanetViewer planetViewer;

	public EditorListener(EditionManager manager, ManipulatorHandler handler,
			FLayer layer, IViewerContainer canvas3d, PlanetViewer planetViewer) {

		_manager = manager;
		_handler = handler;
		_layer = layer;
		_canvas3d = canvas3d;
		viewer = canvas3d;
		viewer.addMouseListener(this);
		viewer.addKeyListener(this);
		this.planetViewer = planetViewer;

	}

	public void mouseDoubleClick(MouseEvent e) {

	}

	public void mouseDown(MouseEvent e) {

	}

	public void mouseUp(MouseEvent e) {

	}

	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

			Util.logger.log(Level.INFO, "Control Pressed");
			if (Layer3DProps.getLayer3DProps(this._layer).isEditing()) {
				_lockPick = true;
				_handler.setActive(true);
				/*
				 * _cam = _canvas3d.getOSGViewer().getCamera();
				 * _canvas3d.getOSGViewer().setCameraManipulator(null);
				 * _canvas3d.getOSGViewer().setCamera(_cam);
				 */
				NavigationMode.removeAllNavigationModes(this.planetViewer
						.getCustomTerrainManipulator());

			}

		}

		else if (e.getKeyCode() == KeyEvent.VK_G) {

			_manager.group();

		}

		else if (e.getKeyCode() == KeyEvent.VK_U) {
			_manager.ungroup();

		}

	}

	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

			if (Layer3DProps.getLayer3DProps(this._layer).isEditing()) {
				Util.logger.log(Level.INFO, "Control Released");
				_lockPick = false;
				_handler.setActive(false);
				// NavigationMode.restoreAllNavigationModes();
				/*
				 * ((PlanetViewer) _canvas3d.getOSGViewer())
				 * .restoreCustomTerrainManipulator();
				 * _canvas3d.getOSGViewer().setCamera(_cam); try {
				 * ((PlanetViewer) _canvas3d.getOSGViewer()) .activePlanet(0);
				 * ((PlanetViewer)
				 * _canvas3d.getOSGViewer()).computeActiveCoordinateSystemNodePath(); }
				 * catch (ChildIndexOutOfBoundsExceptions e1) {
				 * logger.fatal("Planet doesn't exist"); }
				 */

				NavigationMode.restoreAllNavigationModes(planetViewer
						.getCustomTerrainManipulator());

			}

		}

		if (e.getKeyCode() == KeyEvent.VK_DELETE) {

			if (Layer3DProps.getLayer3DProps(this._layer).isEditing()) {
				_manager.deleteSelectedNodes();
			}

		}

	}

	public void mouseClicked(MouseEvent e) {

		ActionCommand command;
		if (_lockPick) {

			try {
				if (e.getButton() == MouseEvent.BUTTON1) {
					Intersections polytopeHits;
					polytopeHits = viewer.getOSGViewer().rayPick(
							this._manager.getScene(), e.getX(), e.getY(),
							Manipulator.NEG_MANIPULATOR_NODEMASK);

					if (polytopeHits.containsIntersections()) {

						Intersection hit = polytopeHits.getFirstIntersection();
						Node nodeHit = (Node) hit.getNodePath().get(1);
						System.out.println("Node name: "
								+ nodeHit.getNodeName());
						Group parent;
						parent = new Group(nodeHit.getParent(0).getCPtr());
						int k;
						k = parent.getChildIndex(nodeHit);
						command = new AddSelectionCommand(k, _manager);
						command.execute();

					}

					else {

						command = new RemoveAllSelectionCommand(_manager);
						command.execute();

					}
				}
			} catch (NodeException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
// } else if (e.getButton() == MouseEvent.BUTTON2) {
//
// command = new UngroupCommand(_manager);
// command.execute();
//
// }

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
