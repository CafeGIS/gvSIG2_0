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
package org.gvsig.rastertools.histogram.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
/**
 * <code>HistogramDialog</code>. Creación de la ventana de histograma para gvSIG.
 *
 * @version 20/03/2007
 * @author Nacho Brodin (brodin_ign@gva.es)
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public class HistogramDialog extends JPanel implements IWindow, ButtonsPanelListener {
	private static final long serialVersionUID = 7362459094802955247L;
	private HistogramPanel histogramPanel = null;
	private String         layerName      = null;

	/**
	 * Crea la ventana del histograma con un ancho y alto.
	 * @param width Ancho de la ventana
	 * @param height Alto de la ventana
	 */
	public HistogramDialog(int width, int height){
		this.setSize(width, height);
		this.setLayout(new BorderLayout(5, 5));
		this.add(getHistogramPanel(), java.awt.BorderLayout.CENTER);
	}
	
	/**
	 * Asigna la capa para obtener las fuentes de datos tanto del 
	 * datasource como de la visualización.
	 * @param lyr Capa
	 */
	public void setLayer(FLyrRasterSE lyr) throws Exception {
		layerName = lyr.getName();
		getHistogramPanel().setDataType(((FLyrRasterSE) lyr).getDataType()[0]);
		getHistogramPanel().setLayer(lyr);
	}

	/**
	 * Obtiene el panel con el histograma
	 * @return HistogramPanel
	 */
	public HistogramPanel getHistogramPanel(){
		if (histogramPanel == null) {
			histogramPanel = new HistogramPanel();
			histogramPanel.addButtonPressedListener(this);
		}
		return histogramPanel;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE);
		if (getLayerName() != null)
			m_viewinfo.setAdditionalInfo(getLayerName());
		m_viewinfo.setTitle(PluginServices.getText(this, "histograma"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}

	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	private void close() {
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			close();
		}
	}

	/**
	 * Obtiene el nombre de la capa. Esto no es necesario para la funcionalidad de histograma.
	 * Solo se usa para destruir el dialogo si está abierto cuando se destruye la capa.
	 * @return Nombre de la capa
	 */
	public String getLayerName() {
		return layerName;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}