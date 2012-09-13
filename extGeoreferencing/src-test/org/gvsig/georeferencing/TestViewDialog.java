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
package org.gvsig.georeferencing;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.georeferencing.ui.zoom.InvalidRequestException;
import org.gvsig.georeferencing.ui.zoom.ViewRasterRequestManager;
import org.gvsig.georeferencing.ui.zoom.tools.ToolEvent;
import org.gvsig.georeferencing.ui.zoom.tools.ToolListener;
import org.gvsig.georeferencing.view.ViewDialog;

import com.iver.utiles.swing.threads.Cancellable;

/**
 * Test para la vista de georreferenciación con peticiones a la capa.
 *
 * @version 30/07/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TestViewDialog implements ToolListener {
	private JFrame 	   frame = new JFrame();
	private String     path = "/home/nacho/images/500k_2.ecw";
	//private String     path = "/home/nacho/images/orto5mv30f290126cor03.sid";
	//private String     path = "/home/nacho/images/wcs16bits.tif";
	//private String     path = "/home/nacho/images/03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	private int        w = 500, h = 500;

	public TestViewDialog() {
		super();
		initialize();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		new TestViewDialog();
	}

	private void initialize() {
		FLyrRasterSE layer = null;
		try {
			layer = FLyrRasterSE.createLayer("mylyr", path, null);
		} catch (LoadLayerException e) {
			e.printStackTrace();
		}

		//Creamos el componente
		ViewDialog view = new ViewDialog(0, 0, w, h, this);
		view.setShowInfo(true);
		//Gestor de peticiones a la capa (IExtensionRequest)
		ViewRasterRequestManager viewRequestManager = new ViewRasterRequestManager(view, layer);
		//Asignamos al componente cual será su gestor de peticiones. Cada vez que se pulse un zoom el componente hará
		//una llamada request a su gestor de peticiones
		view.setExtensionRequest(viewRequestManager);
		view.setCursorSize(w / 2, h / 2);

		frame.setContentPane(view);

		frame.setSize(new java.awt.Dimension(w, h));
		frame.setResizable(true);
		frame.setTitle("zoom");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		try {
			Envelope env=layer.getFullEnvelope();
			viewRequestManager.initRequest(env);
		} catch (InvalidRequestException e1) {
			System.out.println("Error asignando la vista");
		}
	}

	class CancellableClass implements Cancellable{
		private boolean cancel = false;
		public void setCanceled(boolean canceled) {
			this.cancel = canceled;
		}

		public boolean isCanceled() {
			return this.cancel;
		}
	}

	public void endAction(ToolEvent ev) {
	}

	public void offTool(ToolEvent ev) {
	}

	public void onTool(ToolEvent ev) {
	}

}