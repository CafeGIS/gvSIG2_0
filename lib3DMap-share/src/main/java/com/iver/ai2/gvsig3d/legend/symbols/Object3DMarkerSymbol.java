/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
 *
 * $Id: PictureMarkerSymbol.java 15593 2007-10-29 13:01:13Z jdominguez $
 * $Log$
 * Revision 1.17  2007-09-21 12:25:32  jaume
 * cancellation support extended down to the IGeometry and ISymbol level
 *
 * Revision 1.16  2007/09/19 16:22:04  jaume
 * removed unnecessary imports
 *
 * Revision 1.15  2007/09/11 07:46:55  jaume
 * *** empty log message ***
 *
 * Revision 1.14  2007/08/16 06:55:19  jvidal
 * javadoc updated
 *
 * Revision 1.13  2007/08/09 06:42:24  jvidal
 * javadoc
 *
 * Revision 1.12  2007/08/08 12:05:17  jvidal
 * javadoc
 *
 * Revision 1.11  2007/07/18 06:54:35  jaume
 * continuing with cartographic support
 *
 * Revision 1.10  2007/07/03 10:58:29  jaume
 * first refactor on CartographicSupport
 *
 * Revision 1.9  2007/06/29 13:07:01  jaume
 * +PictureLineSymbol
 *
 * Revision 1.8  2007/06/11 12:25:48  jaume
 * ISymbol drawing integration tests (markers and lines)
 *
 * Revision 1.7  2007/06/07 06:50:40  jaume
 * *** empty log message ***
 *
 * Revision 1.6  2007/05/29 15:46:37  jaume
 * *** empty log message ***
 *
 * Revision 1.5  2007/05/08 08:47:40  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2007/03/21 17:36:22  jaume
 * *** empty log message ***
 *
 * Revision 1.3  2007/03/09 11:20:57  jaume
 * Advanced symbology (start committing)
 *
 * Revision 1.1.2.4  2007/02/21 07:34:09  jaume
 * labeling starts working
 *
 * Revision 1.1.2.3  2007/02/16 10:54:12  jaume
 * multilayer splitted to multilayerline, multilayermarker,and  multilayerfill
 *
 * Revision 1.1.2.2  2007/02/15 16:23:44  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2007/02/09 07:47:05  jaume
 * Isymbol moved
 *
 * Revision 1.1  2007/01/24 17:58:22  jaume
 * new features and architecture error fixes
 *
 *
 */
package com.iver.ai2.gvsig3d.legend.symbols;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.FShape;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.osgvp.core.osg.Vec3;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.Messages;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.threads.Cancellable;

public class Object3DMarkerSymbol extends org.gvsig.fmap.mapcontext.rendering.symbols.AbstractMarkerSymbol {
	private static final float SELECTION_OPACITY_FACTOR = .3F;
	// transient private Image img;
	private String object3DPath;
	private boolean selected;
	private Vec3 scale;
	private Vec3 rotation;
	private boolean autoRotate = false;
	private String tempScreenshotimage;

	// transient private Image selImg;

	public boolean isAutoRotate() {
		return autoRotate;
	}

	public void setAutoRotate(boolean autoRotate) {
		this.autoRotate = autoRotate;
	}

	/**
	 * Constructor method
	 */
	public Object3DMarkerSymbol() {
		super();
	}

	/**
	 * Constructor method
	 * 
	 * @param imageURL
	 *            , URL of the normal image
	 * @param selImageURL
	 *            , URL of the image when it is selected in the map
	 * @throws IOException
	 */
	public Object3DMarkerSymbol(URL object3DURL) throws IOException {
		setObject3DPath(object3DURL);
	}

	public Object3DMarkerSymbol(String object3DPath) throws IOException {
		this.object3DPath = object3DPath;
	}

	/**
	 * Sets the file for the image to be used as a marker symbol
	 * 
	 * @param imageFile
	 *            , File
	 * @throws IOException
	 */
	public void setObject3DPath(URL Object3DUrl) throws IOException {
		object3DPath = Object3DUrl.toString();
	}

	public ISymbol getSymbolForSelection() {
		return null;
	}

	public void draw(Graphics2D g, AffineTransform affineTransform, FShape shp,
			Cancellable cancel) {
		// FPoint2D p = (FPoint2D) shp;
		// double x, y;
		// int size = (int) Math.round(getSize());
		// double halfSize = getSize()/2;
		// x = p.getX() - halfSize;
		// y = p.getY() - halfSize;
		// int xOffset = (int) getOffset().getX();
		// int yOffset = (int) getOffset().getY();
		//
		// if (size > 0) {
		// BackgroundFileStyle bg = (!selected) ? bgImage : bgSelImage ;
		// Rectangle rect = new Rectangle( size, size );
		// g.translate(x+xOffset, y+yOffset);
		// g.rotate(getRotation(), halfSize, halfSize);
		// try {
		// bg.drawInsideRectangle(g, rect);
		// } catch (SymbolDrawingException e) {
		// Logger.getLogger(getClass()).warn(Messages.getString(
		// "label_style_could_not_be_painted"), e);
		// }
		// g.rotate(-getRotation(), halfSize, halfSize);
		// g.translate(-(x+xOffset), -(y+yOffset));
		//
		// }

	}


	public void drawInsideRectangle(Graphics2D g,
			AffineTransform scaleInstance, Rectangle r)
			throws SymbolDrawingException {
		// TODO Auto-generated method stub
		// super.drawInsideRectangle(g, scaleInstance, r);

		ImageIcon image;
		// Setting the size symbol
		int size = 30;

		// Generating the image
//		image = new ImageIcon(
//				"c:/Documents and Settings/Julio.IVER/Escritorio/img.png");
		image = new ImageIcon(tempScreenshotimage);
		int x = 0;
		int y = 0;
		if (g.getClipBounds() != null) {
			x = (int) (g.getClipBounds().getCenterX() - (size / 2));
			y = (int) (g.getClipBounds().getCenterY() - (size / 2));
		}
		g.drawImage(image.getImage(), x, y, size, size, null, null);

	}

	public XMLEntity getXMLEntity() {
		XMLEntity xml = new XMLEntity();
		xml.putProperty("className", getClassName());
		xml.putProperty("isShapeVisible", isShapeVisible());
		xml.putProperty("desc", getDescription());
		xml.putProperty("imagePath", object3DPath);
		xml.putProperty("tempScreenshotimage",tempScreenshotimage);

		xml.putProperty("scalex", scale.x());
		xml.putProperty("scaley", scale.y());
		xml.putProperty("scalez", scale.z());
		
		xml.putProperty("rotationx", rotation.x());
		xml.putProperty("rotationy", rotation.y());
		xml.putProperty("rotationz", rotation.z());
		
		xml.putProperty("autoRotate", isAutoRotate());
		

		return xml;
	}


	public boolean isSuitableFor(Geometry geom) {
		boolean suitable = false;
		com.iver.andami.ui.mdiManager.IWindow[] f = PluginServices
				.getMDIManager().getAllWindows();
		if (f == null) {
			return false;
		}
		for (int i = 0; i < f.length; i++) {
			if (f[i] instanceof BaseView) {
				BaseView view = (BaseView) f[i];
				MapContext mapContext = view.getMapControl().getMapContext();
				FLayers layer = mapContext.getLayers();
				FLayer[] actives = layer.getActives();
				if (actives.length == 1) {
					Layer3DProps props3D = Layer3DProps
							.getLayer3DProps(actives[0]);
					if ((props3D != null)
							&& (props3D.getType() == Layer3DProps.layer3DVector)) {
						suitable = true;
					} else {
						suitable = false;
					}
				}
			}

		}
		System.out.println("is suitable " + suitable);
		return suitable;
	}

	public String getClassName() {
		return getClass().getName();
	}

	public void setXMLEntity(XMLEntity xml) {
		setDescription(xml.getStringProperty("desc"));
		setIsShapeVisible(xml.getBooleanProperty("isShapeVisible"));
		object3DPath = xml.getStringProperty("imagePath");
		tempScreenshotimage = xml.getStringProperty("tempScreenshotimage");
//		setRotation(xml.getDoubleProperty("rotation"));
		
		this.scale = new Vec3();
		scale.setX(xml.getDoubleProperty("scalex"));
		scale.setY(xml.getDoubleProperty("scaley"));
		scale.setZ(xml.getDoubleProperty("scalez"));

		this.rotation = new Vec3();
		rotation.setX(xml.getDoubleProperty("rotationx"));
		rotation.setY(xml.getDoubleProperty("rotationy"));
		rotation.setZ(xml.getDoubleProperty("rotationz"));
		
		setAutoRotate(xml.getBooleanProperty("autoRotate"));
		

		try {
			setObject3DPath(new URL(object3DPath));
		} catch (MalformedURLException e) {
			Logger.getLogger(getClass()).error(
					Messages.getString("invalid_url"));
		} catch (IOException e) {
			Logger.getLogger(getClass()).error(
					Messages.getString("invalid_url"));

		}

	}

	public void print(Graphics2D g, AffineTransform at, FShape shape)
			throws ReadException {
		// TODO Implement it
		throw new Error("Not yet implemented!");

	}

	/**
	 * Returns the path of the image that is used as a marker symbol
	 * 
	 * @return imagePath,String
	 */
	public String getObject3DPath() {
		return object3DPath;
	}

	public void setScale(Vec3 scale) {
		this.scale = scale;
		
	}

	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
		
	}

	public Vec3 getScale() {
		return this.scale;
	}

	public Vec3 getRotationObject() {
		return this.rotation;
	
	}

	public void setSnapshot(String tempScreenshotimage) {
		this.tempScreenshotimage = tempScreenshotimage;
		
	}

	public void draw(Graphics2D g, AffineTransform affineTransform,
			Geometry geom, org.gvsig.tools.task.Cancellable cancel) {
		// TODO Auto-generated method stub
		
	}

}