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
package org.gvsig.rastertools.saveraster.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.gvsig.rastertools.saveraster.ui.panels.listener.MethodSelectorListener;

import com.iver.andami.PluginServices;

/**
 * Selección de parámetros del cuadro de salvar a raster.
 * 
 * 04/10/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class SelectionParamsPanel extends JPanel{
	private static final long serialVersionUID = -5868836652528932347L;
	private MethodSelectorPanel      selectorPanel = null;
	private InputScaleDataPanel      inputScalePanel = null;
	private InputSizePanel           inputSizePanel = null;
	private JPanel                   northPanel = null;
	private MethodSelectorListener   listener = null;
	
	/**
	 * Constructor
	 */
	public SelectionParamsPanel() {
		BorderLayout layout = new BorderLayout();
		layout.setHgap(4);
		layout.setVgap(4);
		setLayout(layout);
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getInputSizePanel(), BorderLayout.SOUTH);
		setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "metodo"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("metodo", java.awt.Font.PLAIN, 10), null));
		listener = new MethodSelectorListener(this);
	}

	/**
	 * Obtiene el listener que controla el sincronismo entre los componentes
	 * @return MethodSelectorListener
	 */
	public MethodSelectorListener getMethodSelectorListener() {
		return listener;
	}
	
	/**
	 * Obtiene el panel con el selector de método y la selección
	 * de escala
	 * @return JPanel
	 */
	public JPanel getCenterPanel() {
		if(northPanel == null) {
			northPanel = new JPanel();
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 10, 0, 5);

			northPanel.setLayout(new GridBagLayout());

			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			northPanel.add(getSelectorPanel(), gridBagConstraints);

			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			northPanel.add(getInputScalePanel(), gridBagConstraints);
		}
		return northPanel;
	}

	/**
	 * Obtiene el panel con la selección de escala y tamaño de pixel
	 * @return InputScaleDataPanel
	 */
	public InputScaleDataPanel getInputScalePanel() {
		if(inputScalePanel == null)
			inputScalePanel = new InputScaleDataPanel();
		return inputScalePanel;
	}

	/**
	 * Obtiene el panel con la selección de tamaño de salida
	 * del raster
	 * @return InputSizePanel
	 */
	public InputSizePanel getInputSizePanel() {
		if(inputSizePanel == null)
			inputSizePanel = new InputSizePanel();
		return inputSizePanel;
	}

	/**
	 * Obtiene el panel con el selector de método de selección. Por escala,
	 * tamaño de pixel o tamaño de imagen
	 * @return MethodSelectorPanel
	 */
	public MethodSelectorPanel getSelectorPanel() {
		if(selectorPanel == null)
			selectorPanel = new MethodSelectorPanel();
		return selectorPanel;
	}
}
