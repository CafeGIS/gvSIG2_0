package org.gvsig.gvsig3d.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.IViewerContainer;
import org.gvsig.osgvp.viewer.Intersection;
import org.gvsig.osgvp.viewer.Intersections;
import org.gvsig.osgvp.viewer.OSGViewer;

public class canvasListener implements KeyListener, MouseListener {
	private static boolean active = false;

	private IViewerContainer m_canvas3d = null;

	private Planet planet;

	private RotatePlanet updateViewThread;

	private int frameRate = 24;

	private PlanetViewer m_planetViewer;

	private static boolean activeRot;

	public void setCanvas(IViewerContainer m_canvas3d) {
		this.m_canvas3d = m_canvas3d;
		this.m_planetViewer = (PlanetViewer) m_canvas3d.getOSGViewer();
	}

	public void keyPressed(KeyEvent kev) {
		// TODO Auto-generated method stub
		int codigo = kev.getKeyCode();
		switch (kev.getKeyChar()) {
		case 'w':
		case 'W':
			active = !active;
			if (active) {
				m_planetViewer.setPolygonMode(
						OSGViewer.PolygonModeType.GL_LINE);
			} else {
				m_planetViewer.setPolygonMode(
						OSGViewer.PolygonModeType.GL_FILL);
			}
			break;
		case 'a':
		case 'A':
			Thread thread;
			activeRot = !activeRot;
			if (activeRot) {

				updateViewThread = new RotatePlanet(1000/frameRate);

				// Create the thread supplying it with the runnable object
				thread = new Thread(updateViewThread);

				// Start the thread
				thread.start();
			} else {
				updateViewThread.end();
			}
			break;
			
		case 'e':
		case 'E':
			updateViewThread.setTime(1000/frameRate++);
			break;
		case 'd':
		case 'D':
			updateViewThread.setTime(1000/frameRate--);
			break;
		}

	}

	public void keyReleased(KeyEvent kev) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent kev) {
		// TODO Auto-generated method stub

	}

	/**
	 * Class to rotate the planet. This class implements runnable and contructor
	 * with time parameter.
	 * 
	 * @author julio
	 * 
	 */
	public class RotatePlanet implements Runnable {

		private boolean finish = false;

		private long time;

		public RotatePlanet(long time) {
			this.time = time;
		}

		double lat = 0.0;

		double longi = 0.0;

		// This method is called when the thread runs
		public void run() {
			while (true) {
				try {
					Thread.sleep(time);
					synchronized (this) {
						if (finish) {
							break;
						}
					}
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				// Rotate planet
				longi += 1.0;
				longi = longi > 360 ? 0 : longi;
				Camera cam = m_planetViewer.getCamera();
				Vec3 camPos = planet.convertLatLongHeightToXYZ(new Vec3(lat,
						longi, cam.getEye().z()));

				cam.setViewByLookAt(camPos.x(), camPos.y(), 500000 * 16.6,
						0, 0, 0, 0, 0, 1);
				m_planetViewer.setCamera(cam);

				// Repainting canvas
				m_canvas3d.repaint();
			}
		}

		public synchronized void end() {
			finish = true;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

	}

	public Planet getPlanet() {
		return planet;
	}

	public void setPlanet(Planet planet) {
		this.planet = planet;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Intersections hits = m_planetViewer.rayPick(this.planet, e.getX(), e.getY());
			if(hits.containsIntersections()) {
				Intersection hit = hits.getFirstIntersection();
				System.err.println("Point:" + hit.getIntersectionPoint().toString());
				Vec3 point = planet.convertXYZToLatLongHeight(hit
						.getIntersectionPoint());
				System.err.println("LatLonHeight: " + point.toString());
			}
		}
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
