/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ibez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */
package org.gvsig.remotesensing;

import javax.swing.Icon;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.remotesensing.principalcomponents.gui.PrincipalComponentPanel;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
/**
 * Extensión para el cálculo de Componentes Principales.
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 */
public class PrincipalComponentsExtension extends Extension implements IGenericToolBarMenuItem {
	private static final double NODATA = -99999999999999.;

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();
		ExtensionPoint point=extensionPoints.add("GenericToolBarMenu");
		point.append("PrincipalComponents", "", this.getClass());
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		if (actionCommand.equals("principal_components")){
			com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();

			//si la ventana activa es de tipo Vista
			if (activeWindow instanceof View) {
				PrincipalComponentPanel pcPanel = new PrincipalComponentPanel ((View)activeWindow);
				PluginServices.getMDIManager().addWindow(pcPanel);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f.getClass() == View.class) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			FLayers layers = mapa.getLayers();
			for (int i = 0; i < layers.getLayersCount(); i++)
				if (layers.getLayer(i) instanceof FLyrRasterSE)
					return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
		if (f == null) {
			return false;
		}
		if (f instanceof View) {
			View vista = (View) f;
			IProjectView model = vista.getModel();
			MapContext mapa = model.getMapContext();
			return mapa.getLayers().getLayersCount() > 0;
		} else {
			return false;
		}
	}

	private static double covariance(double[] xx, double x, double[] yy, double y) {
		double dSum = 0;
		int iValues = 0;

		for (int i = 0; i < yy.length; i++) {
			if (xx[i] != NODATA && yy[i] != NODATA) {
				dSum += (xx[i] - x) * (yy[i] - y);
				iValues++;
			}
		}
		if (iValues > 1) {
			return dSum / (double) (iValues - 1);
		} else {
			return NODATA;
		}
	}

	private static double covariance(byte[][] xx, double x, byte[][] yy, double y) {
		double dSum = 0;
		int iValues = 0;

		for (int i = 0; i < yy.length; i++) {
			for (int j = 0; j < yy[0].length; j++) {
				if (xx[i][j] != NODATA && yy[i][j] != NODATA) {
					dSum += (xx[i][j] - x) * (yy[i][j] - y);
					iValues++;
				}
			}
		}
		if (iValues > 1) {
			return dSum / (double) (iValues - 1);
		} else {
			return NODATA;
		}
	}

	/**
	 * Covariance of two 1D arrays of doubles, xx and yy
	 * @param xx
	 * @param yy
	 * @return
	 */
	public static double covariance(byte[][] xx, byte[][] yy){
		int nx = xx.length;
		int ny = xx[0].length;
		if (nx != yy.length)
			throw new IllegalArgumentException("length of x variable array, " + nx + " and length of y array, " + yy.length + " are different");

		double sumx = 0.0D, meanx = 0.0D;
		double sumy = 0.0D, meany = 0.0D;
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++) {
				sumx += xx[i][j];
				sumy += yy[i][j];
			}
		}
		meanx = sumx / ((double) nx * (double) ny);
		meany = sumy / ((double) nx * (double) ny);
		double sum = 0.0D;
		for (int i = 0; i < nx; i++) {
			for (int j = 0; j < ny; j++)
				sum += (xx[i][j] - meanx) * (yy[i][j] - meany);
		}
		return sum / ((double) (nx * ny - 1));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#execute(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public void execute(ITocItem item, FLayer[] selectedItems) {
		this.execute("principal_components");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroup()
	 */
	public String getGroup() {
		return "RasterProcess";
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getIcon()
	 */
	public Icon getIcon() {
		return PluginServices.getIconTheme().get("blank-icon");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getOrder()
	 */
	public int getOrder() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getText()
	 */
	public String getText() {
		return PluginServices.getText(this, "principal_components");
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isEnabled(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof ILayerState))
			return false;

		if (!((ILayerState) selectedItems[0]).isOpen())
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#isVisible(com.iver.cit.gvsig.project.documents.view.toc.ITocItem, com.iver.cit.gvsig.fmap.layers.FLayer[])
	 */
	public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
		if ((selectedItems == null) || (selectedItems.length != 1))
			return false;

		if (!(selectedItems[0] instanceof FLyrRasterSE))
			return false;

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.IGenericToolBarMenuItem#getGroupOrder()
	 */
	public int getGroupOrder() {
		return 0;
	}
}