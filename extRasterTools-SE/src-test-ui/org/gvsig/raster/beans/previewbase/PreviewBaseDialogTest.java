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
*/
package org.gvsig.raster.beans.previewbase;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.hierarchy.IRasterRendering;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * Dialogo para el test del PreviewBasePanel
 * 
 * 19/02/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class PreviewBaseDialogTest extends JPanel implements IWindow, IWindowListener {	
	private static final long serialVersionUID = -5374834293534046986L;
	private String baseDir = "./test-images/";
	private String path = baseDir + "03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";

	private PreviewBasePanel panel = null;
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
		} catch( Exception e ) {
			System.err.println( "No se puede cambiar al LookAndFeel");
		}
		PreviewBaseDialogTest preview = new PreviewBaseDialogTest(590, 410);
		frame.setSize(new java.awt.Dimension(590, 410));
		frame.setContentPane(preview);
		frame.setResizable(true);
		frame.setTitle("Panel de recorte");
		frame.setVisible(true);
	}
	
	class NoProcessClass implements IPreviewRenderProcess {
		public void process(IRasterRendering rendering) throws FilterTypeException {
			//No se procesa
		}

		public boolean isShowPreview() {
			return false;
		}

		public void setShowPreview(boolean showPreview) {
		}
	}
	
	class EmptyPanel extends JPanel implements IUserPanelInterface {
		private static final long serialVersionUID = 1L;
		private int i;
		public EmptyPanel(int i) {
			this.i = i;
			add(new JLabel("Texto"));
		}
		public JPanel getPanel() {
			return this;
		}
		public String getTitle() {
			return "Panel " + i;
		}
	}
	
	/**
	 * Constructor
	 * @param width Ancho
	 * @param height Alto
	 */
	public PreviewBaseDialogTest(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(new BorderLayout(5, 5));
		this.add(getPreviewPanel(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	public PreviewBasePanel getPreviewPanel() {
		if (panel == null) {
			ArrayList list = new ArrayList();
			list.add(new EmptyPanel(0));
			list.add(new EmptyPanel(1));
			FLyrRasterSE lyr;
			try {
				lyr = FLyrRasterSE.createLayer("mylyr", path, null);
				panel = new PreviewBasePanel(list, new EmptyPanel(1), new EmptyPanel(1), new NoProcessClass(), lyr);
			} catch (LoadLayerException e) {
				e.printStackTrace();
			}
		}
		return panel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if(getPreviewPanel().getLayer() != null)
			m_viewinfo.setAdditionalInfo(getPreviewPanel().getLayer().getName());
		m_viewinfo.setTitle(PluginServices.getText(this, "tablas_color"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {

	}

	public void windowActivated() {}

	public Object getWindowProfile() {
		return null;
	}
}