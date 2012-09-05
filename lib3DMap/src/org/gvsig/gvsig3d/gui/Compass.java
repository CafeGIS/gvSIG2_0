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

package org.gvsig.gvsig3d.gui;

import org.gvsig.osgvp.core.osg.Matrix;
import org.gvsig.osgvp.core.osg.MatrixTransform;
import org.gvsig.osgvp.core.osg.PositionAttitudeTransform;
import org.gvsig.osgvp.core.osg.Quat;
import org.gvsig.osgvp.core.osg.Texture2D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.Polygon;
import org.gvsig.osgvp.features.Quad;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.viewer.Camera;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;

/**
 *  @
 */

/**
 * Class to represent 3D axis (x,y,z) in the scene
 */
public class Compass extends PositionAttitudeTransform {

	private MatrixTransform _matrix;
	private Matrix _mat;
	private int _cameraType;
	private Planet _planet;

	public static class Mode {
		// default
		public static int FLAT = 0;
		public static int SPHERIC = 1;

	}

	public Compass(Planet planet) throws NodeException {

		super();
		_matrix = new MatrixTransform();
		_planet = planet;
		Polygon pol = new Polygon();
		Quad quad = new Quad();
		Texture2D tex = new Texture2D();
		_cameraType = Compass.Mode.FLAT;

		// quad.addVertex(new Vec3(-0.5,-0.5,0.0), new Vec4(0.0,1.0,1.0,0.5));
		// quad.addVertex(new Vec3(0.5,-0.5,0.0), new Vec4(1.0,0.0,1.0,0.5));
		// quad.addVertex(new Vec3(0.5,0.5,0.0), new Vec4(1.0,1.0,0.0,0.5));
		// quad.addVertex(new Vec3(-0.5,0.5,0.0), new Vec4(1.0,0.0,1.0,0.5));

		pol.addVertex(new Vec3(-0.5, -0.5, 0.0), new Vec4(1.0, 1.0, 1.0, 1));
		pol.addVertex(new Vec3(0.5, -0.5, 0.0), new Vec4(1.0, 1.0, 1.0, 1));
		pol.addVertex(new Vec3(0.5, 0.5, 0.0), new Vec4(1.0, 1.0, 1.0, 1));
		pol.addVertex(new Vec3(-0.5, 0.5, 0.0), new Vec4(1.0, 1.0, 1.0, 1));

		// StateSet ss =quad.getOrCreateStateSet();
		// Image im;
		// try {
		// im = new Image(ResourcesFactory.getResourcePath("compass.png"));
		// tex.setImage(im);
		// } catch (LoadImageException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ImageException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// quad.getOrCreateStateSet().setTextureMode(0, Node.Mode.ON |
		// Node.Mode.OVERRIDE);
		// quad.getOrCreateStateSet().setTexture2D(tex,0 , Node.Mode.ON);
		// _matrix.addChild(quad);

		pol.setTexture(ResourcesFactory.getResourcePath("compass.png"));
		pol.setEnabledBlending(true);
		_matrix.addChild(pol);
		_mat = new Matrix();
		this.addChild(_matrix);
	}

	public void setPanetType(int type) {

		_cameraType = type;

	}

	/**
	 * Update compass rotation respect a camera.
	 * 
	 * @param camera
	 */
	public void update(Camera camera) {

		Matrix auxmat = new Matrix();

		if (_cameraType == Mode.SPHERIC) {
			Vec3 o, i, n, e, z, v, iTo;

			o = camera.getEye();
			i = _planet.convertXYZToLatLongHeight(camera.getEye());
			i = _planet.convertLatLongHeightToXYZ(new Vec3(i.x(), i.y(), 0));
			iTo = o.sub(i);
			Vec3 northPole = _planet.convertLatLongHeightToXYZ(new Vec3(90, 0,
					0));
			z = i.sub(northPole);
			e = iTo.crossProduct(z);
			n = iTo.crossProduct(e);
			v = camera.getCenter().sub(camera.getEye());
			v.normalize();

			double vX, vY;

			vX = v.dotProduct(e) / e.length();
			vY = v.dotProduct(n) / n.length();

			double ang = Math.atan2(vX, vY);

			Quat quat = new Quat();
			quat.makeRotate(ang, new Vec3(0, 0, 1));
			auxmat.setRotate(quat);
			_matrix.setMatrix(auxmat);
		}

		else {

			Vec3 look = camera.getCenter().sub(camera.getEye());
			look.normalize();
			Vec3 east = new Vec3(1, 0, 0);
			Vec3 north = new Vec3(0, 1, 0);

			double vX = look.dotProduct(east);
			double vY = look.dotProduct(north);

			double angle = Math.atan2(vX, vY);

			Quat quat = new Quat();
			quat.makeRotate(angle, new Vec3(0, 0, 1));

			auxmat.setRotate(quat);
			_matrix.setMatrix(auxmat);

		}

	}

}
