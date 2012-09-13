/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.imagenavigator;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;

import javax.swing.JFrame;

import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.gui.beans.imagenavigator.IClientImageNavigator;
import org.gvsig.gui.beans.imagenavigator.ImageNavigator;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.render.Rendering;
/**
 * Este test comprueba el funcionamiento de ImageNavigator con capas reales
 *
 * @version 09/05/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class TestImageNavigator implements IClientImageNavigator {
	private String baseDir = "./test-images/";
	private String path = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private RasterDataset f = null;
	private BufferFactory bf = null;
	private Rendering rendering = null;
	private Extent extent2 = null;
	private Grid grid = null;

	private JFrame jFrame = null;
	private ImageNavigator imageNavigator = null;

	static {
		RasterLibrary.wakeUp();
	}

	public TestImageNavigator() {
		int[] drawableBands = {0, 1, 2};
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return;
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return;
		}
		bf = new BufferFactory(f);
		bf.setDrawableBands(drawableBands);
		try {
			grid = new Grid(bf);
		} catch (RasterBufferInvalidException e) {
			e.printStackTrace();
			return;
		}

		//ds.setAreaOfInterest(645817.0, 4923851.0, 645853.0, 4923815.0, 100, 100, true);
		rendering = new Rendering(bf);
		extent2 = f.getExtent();
//		a.draw(g, vp)
		initialize();
	}

	private void initialize() {
		jFrame = new JFrame();
		jFrame.setSize(new Dimension(600, 600));
		jFrame.setContentPane(getImageNavigator());
		jFrame.setVisible(true);

		getImageNavigator().setViewDimensions(extent2.getMin().getX(), extent2.getMax().getY(), extent2.getMax().getX(), extent2.getMin().getY());
		getImageNavigator().setZoom(1.0 / grid.getCellSize());
		getImageNavigator().updateBuffer();
		jFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	}

	private ImageNavigator getImageNavigator() {
		if (imageNavigator == null) {
			imageNavigator = new ImageNavigator(this);
		}
		return imageNavigator;
	}

	public static void main(String[] args) {
		new TestImageNavigator();
	}

	public void drawImage(Graphics2D g, double x1, double y1, double x2, double y2, double zoom, int width, int height) {
		if ((rendering == null) || ((x2 - x1) == 0.0) || ((y2 - y1) == 0.0))
			return;
		ViewPort vp = new ViewPort(null);

		Dimension2D dimension = new Dimension(width, height);
		Extent extent = new Extent(x1, y1, x2, y2);

		ViewPortData vp2 = new ViewPortData(vp.getProjection(), extent, dimension);
		vp2.setMat(new AffineTransform(zoom, 0.0, 0.0, zoom, -x1*zoom, -y1*zoom));

		AffineTransform trans = g.getTransform();
		// Calcular cuanto sobresale la imagen y rectificar ese desplazamiento
		if (y1 > extent2.maxY())
			g.translate(0.0, (-(extent2.maxY() - y1) * zoom) * 2.0);

		try {
			rendering.draw(g, vp2);
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		g.setTransform(trans);
	}
}