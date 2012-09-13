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
package org.gvsig.raster.gui.wizards.projection;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.raster.gui.wizards.FileOpenRaster;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;
/**
 * Obtiene el panel con las opciones a realizar con la proyección. Nos ofrece
 * las siguientes posibilidades:
 * <UL>
 * <LI>Cambiar la proyección de la vista</LI>
 * <LI>Reproyectar el raster</LI>
 * <LI>Ignorar la proyección del raster y cargar</LI>
 * <LI>No cargar</LI>
 * </UL>
 * 
 * 07/04/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class RasterProjectionActionsPanel extends DefaultButtonsPanel {
	private static final long serialVersionUID = -3868504818382448187L;

	public static boolean selectAllFiles         = false;
	private JPanel        buttonsPanel           = null;
	private ButtonGroup   group                  = new ButtonGroup();
	private JRadioButton  changeViewProjection   = null;
	private JRadioButton  reproject              = null;
	private JRadioButton  ignoreRasterProjection = null;
	private JRadioButton  notLoad                = null;
	private JCheckBox     allfiles               = null;
	private FLyrRasterSE  lyr                    = null;
	
	/**
	 * Constructor. Llama al inicializador de componentes gráficos.
	 */
	public RasterProjectionActionsPanel(FLyrRasterSE lyr) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		this.lyr = lyr;
		init(lyr.getName());
		setSelection(FileOpenRaster.defaultActionLayer);
	}
	
	/**
	 * Inicialización de componentes gráficos.
	 */
	public void init(String lyrName) {
		BorderLayout bl = new BorderLayout();
		bl.setVgap(5);
		setLayout(bl);
		add(new JLabel("<html><b>" + lyrName + "</b><BR><BR>" + PluginServices.getText(this, "dif_proj") + "</html>"), BorderLayout.NORTH);
		add(getButtonsActionPanel(), BorderLayout.CENTER);
		add(getCheckOption(), BorderLayout.SOUTH);
	}
		
	/**
	 * Obtiene el panel con los botones se selección de opción.
	 * @return JPanel
	 */
	private JPanel getButtonsActionPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "proj_options"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

			group.add(getIgnoreRasterProjectionButton());
			group.add(getChangeViewProjectionButton());
			group.add(getReprojectButton());
			group.add(getNotLoadButton());

			buttonsPanel.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new java.awt.Insets(0, 5, 5, 0);
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			buttonsPanel.add(getIgnoreRasterProjectionButton(), gbc);
			
			gbc.gridy = 1;
			buttonsPanel.add(getReprojectButton(), gbc);
			
			gbc.gridy = 2;
			buttonsPanel.add(getChangeViewProjectionButton(), gbc);
			
			gbc.gridy = 3;
			buttonsPanel.add(getNotLoadButton(), gbc);
		}
		return buttonsPanel;
	}
	
	/**
	 * Obtiene el botón de cambio de projección de la vista
	 * @return
	 */
	private JRadioButton getChangeViewProjectionButton() {
		if(changeViewProjection == null) {
			changeViewProjection = new JRadioButton(PluginServices.getText(this, "change_view_proj"));
			
			IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
			if (activeWindow instanceof View) {		
				View activeView = (com.iver.cit.gvsig.project.documents.view.gui.View) activeWindow;
				if (activeView.getMapControl().getMapContext().getLayers().getLayersCount() >= 1)
					changeViewProjection.setEnabled(false);
			}
		}
		return changeViewProjection;
	}
	
	/**
	 * Obtiene el botón de cambio de reproyección
	 * @return
	 */
	private JRadioButton getReprojectButton() {
		if(reproject == null) {
			reproject = new JRadioButton(PluginServices.getText(this, "reproject"));
			reproject.setEnabled(lyr.isReproyectable());
		}
		return reproject;
	}
	
	/**
	 * Obtiene el botón de ignorar la proyección del raster
	 * @return
	 */
	private JRadioButton getIgnoreRasterProjectionButton() {
		if(ignoreRasterProjection == null) {
			ignoreRasterProjection = new JRadioButton(PluginServices.getText(this, "ignore_raster_proj"));
		}
		return ignoreRasterProjection;
	}
	
	/**
	 * Obtiene el botón de no cargar el raster.
	 * @return
	 */
	private JRadioButton getNotLoadButton() {
		if(notLoad == null) {
			notLoad = new JRadioButton(PluginServices.getText(this, "not_load"));
		}
		return notLoad;
	}
	
	/**
	 * Obtiene la selección del panel
	 * @return entero con la selección. Esta representada por las constantes de FileOpenRaster.
	 */
	public int getSelection() {
		if (getChangeViewProjectionButton().isSelected())
			return FileOpenRaster.CHANGE_VIEW_PROJECTION;
		if (getReprojectButton().isSelected())
			return FileOpenRaster.REPROJECT;
		if (getIgnoreRasterProjectionButton().isSelected())
			return FileOpenRaster.IGNORE;
		if (getNotLoadButton().isSelected())
			return FileOpenRaster.NOTLOAD;
		return -1;
	}
	
	/**
	 * Asigna una selección de opción
	 * @param entero con la selección. Esta representada por las constantes de FileOpenRaster.
	 */
	public void setSelection(int value) {
		if (value == FileOpenRaster.CHANGE_VIEW_PROJECTION)
			getChangeViewProjectionButton().setSelected(true);
		if (value == FileOpenRaster.REPROJECT)
			getReprojectButton().setSelected(true);
		if (value == FileOpenRaster.IGNORE)
			getIgnoreRasterProjectionButton().setSelected(true);
		if (value == FileOpenRaster.NOTLOAD)
			getNotLoadButton().setSelected(true);
	}
	
	/**
	 * Obtiene el check con la opción de aplicar a todos los ficheros
	 * @return
	 */
	public JCheckBox getCheckOption() {
		if (allfiles == null)
			allfiles = new JCheckBox(PluginServices.getText(this, "apply_all"));
		return allfiles;
	}
} 
