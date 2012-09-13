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
package org.gvsig.rastertools.reproject.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.util.StatusComponent;
import org.gvsig.raster.beans.createlayer.NewLayerPanel;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
/**
 * Panel para la reproyección de capas cargadas.
 * 
 * 28/04/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class LayerReprojectPanel extends DefaultButtonsPanel implements IWindow {
	private static final long serialVersionUID = -1011688195806336071L;
	private NewLayerPanel          newLayerPanel         = null;
	private JPanel                 nameFile              = null;
	private CRSSelectPanel         projectionSrcSelector = null;
	private CRSSelectPanel         projectionDstSelector = null;
	private IProjection            projSrc               = null;
	private IProjection            projDst               = null;
	private JPanel                 filePanel             = null;
	private LayerReprojectListener reprojectListener     = null;
	private FLyrRasterSE           lyr                   = null;
	private String                 viewName              = null;
	private Boolean                isInTOC               = Boolean.TRUE;

	/**
	 * Constructor
	 */
	public LayerReprojectPanel(FLyrRasterSE lyr, Boolean isInTOC) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		this.isInTOC = isInTOC;
		setLayer(lyr);
		init();
		projSrc = getProjectionSrcSelector().getCurProj();
		projDst = getProjectionDstSelector().getCurProj();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo windowInfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
		windowInfo.setTitle(PluginServices.getText(this, "capa_a_reproyectar"));
		windowInfo.setWidth(320);
		windowInfo.setHeight(320);
		return windowInfo;
	}
	
	private void setLayer(FLyrRasterSE lyr) {
		this.lyr = lyr;

		if (this.lyr == null)
			return;

		
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		viewName = PluginServices.getMDIManager().getWindowInfo(view).getTitle();
		
		projSrc = this.lyr.readProjection();
		if (projSrc == null)
			projSrc = CRSFactory.getCRS("EPSG:23030");

		projDst = CRSFactory.getCRS("EPSG:23030");

		getLayerReprojectListener().setIsInTOC(isInTOC);
		getLayerReprojectListener().setLayer(this.lyr);
	}
	
	private LayerReprojectListener getLayerReprojectListener() {
		if (reprojectListener == null) {
			reprojectListener = new LayerReprojectListener(this);
			addButtonPressedListener(reprojectListener);
		}
		return reprojectListener;
	}
	
	/**
	 * Inicialización de los componentes gráficos.
	 */
	private void init() {
		GridBagConstraints gridBagConstraints;

		setLayout(new GridBagLayout());
		
		int posy = 0;
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		JPanel panelFile = new JPanel();
		panelFile.setLayout(new BorderLayout());
		panelFile.setBorder(BorderFactory.createTitledBorder(RasterToolsUtil.getText(this, "origen")));
		JLabel label = new JLabel("<html><b>" + lyr.getName() + "</b></html>");
		panelFile.add(label, BorderLayout.CENTER);
		add(panelFile, gridBagConstraints);
		
		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		add(getProjectionSrcSelector(), gridBagConstraints);

		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		add(getProjectionDstSelector(), gridBagConstraints);
		
		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		add(getNewLayerPanel().getJPanel(), gridBagConstraints);
		
		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(2, 5, 0, 5);
		add(getFilePanel(), gridBagConstraints);

		// Insertamos un panel vacio
		posy++;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = posy;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new Insets(0, 0, 0, 0);
		JPanel emptyPanel = new JPanel();
		add(emptyPanel, gridBagConstraints);
	}
	
	/**
	 * Obtiene el panel de destino de fichero y nombre de capa
	 * @return JPanel
	 */
	private JPanel getFilePanel() {
		if (filePanel == null) {
			filePanel = new JPanel();
			filePanel.setBorder(BorderFactory.createTitledBorder(RasterToolsUtil.getText(this, "dest_file")));
			filePanel.setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1D;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(0, 0, 0, 5);
			filePanel.add(getNameFilePanel(), gbc);
		}
		return filePanel;
	}

	/**
	 * Obtiene el desplegable con la lista de capas
	 * @return JComboBox
	 */
	protected NewLayerPanel getNewLayerPanel() {
		if (newLayerPanel == null) {
			newLayerPanel = new NewLayerPanel(lyr);
			newLayerPanel.setOnlyReprojectables(true);
			newLayerPanel.getJPanel().setBorder(BorderFactory.createTitledBorder(RasterToolsUtil.getText(this, "capa")));
			newLayerPanel.getRadioOpenMemory().setEnabled(!isInTOC.booleanValue());
			if (isInTOC.booleanValue())
				newLayerPanel.getRadioFileGenerate().setSelected(true);
		}
		return newLayerPanel;
	}
	
	/**
	 * Obtiene el nombre de la vista
	 * @return
	 */
	public String getViewName() {
		return viewName;
	}
	
	/**
	 * Obtiene el desplegable con la lista de capas
	 * @return JComboBox
	 */
	private JPanel getNameFilePanel() {
		if(nameFile == null) {
			nameFile = getNewLayerPanel().getFileNamePanel();
		}
		return nameFile;
	}

	/**
	 * Obtiene el botón que lanza el panel de selección de proyecciones.
	 * @return
	 */
	private CRSSelectPanel getProjectionSrcSelector() {
		if (projectionSrcSelector == null) {
			
			IProjection projectionAux = null;
			IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
			if (activeWindow instanceof View) {		
				View activeView = (com.iver.cit.gvsig.project.documents.view.gui.View) activeWindow;
				projectionAux = activeView.getProjection();
				activeView.setProjection(projSrc);
			}
			
			projectionSrcSelector = CRSSelectPanel.getPanel(projSrc);
			
			if (activeWindow instanceof View) {		
				View activeView = (com.iver.cit.gvsig.project.documents.view.gui.View) activeWindow;
				activeView.setProjection(projectionAux);
			}
			
			projectionSrcSelector.setTransPanelActive(true);
			projectionSrcSelector.setPreferredSize(null);
			projectionSrcSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (projectionSrcSelector.isOkPressed()) {
						projSrc = projectionSrcSelector.getCurProj();
					}
				}
			});
			projectionSrcSelector.setBorder(BorderFactory.createTitledBorder(RasterToolsUtil.getText(this, "src_proj")));
			
			if (isInTOC.booleanValue())
				StatusComponent.setDisabled(projectionSrcSelector);
		}
		return projectionSrcSelector;
	}
	
	/**
	 * Obtiene el botón que lanza el panel de selección de proyecciones.
	 * @return
	 */
	private CRSSelectPanel getProjectionDstSelector() {
		if (projectionDstSelector == null) {
			projectionDstSelector = CRSSelectPanel.getPanel(projDst);
			projectionDstSelector.setTransPanelActive(true);
			projectionDstSelector.setPreferredSize(null);
			projectionDstSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (projectionDstSelector.isOkPressed()) {
						projDst = projectionDstSelector.getCurProj();
					}
				}
			});
			projectionDstSelector.setBorder(BorderFactory.createTitledBorder(RasterToolsUtil.getText(this, "dest_proj")));
			
			if (!isInTOC.booleanValue())
				StatusComponent.setDisabled(projectionDstSelector);
		}
		return projectionDstSelector;
	}

	/**
	 * @return the projSrc
	 */
	public IProjection getProjectionSrc() {
		return projSrc;
	}

	/**
	 * @return the projDst
	 */
	public IProjection getProjectionDst() {
		return projDst;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}