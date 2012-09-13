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
 *   Av. Blasco Ibáñez, 50
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

package org.gvsig.remotesensing.gridmath.gui;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.remotesensing.calculator.gui.CalculatorPanel;
import org.gvsig.remotesensing.calculator.gui.TableFormat;
import org.gvsig.remotesensing.gridmath.GridMathProcess;
import org.gvsig.remotesensing.gridmath.gui.listener.GridMathPanelListener;
import org.nfunk.jep.JEP;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Diálogo para la calculadora de bandas.
 *
 * @author Alejandro Muñoz Sanchez	(alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 19/10/2007
 */

public class GridMathPanel extends DefaultButtonsPanel{

	private static final long serialVersionUID = 1L;


	//-------PANEL---------//

	CalculatorPanel calculatorPanel = null;
	OutputOptionsPanel optionsPanel = null;

	//-------JTEXTAREA---------//
	private JTextArea jTextExpresion;

	//----------------------------------------//
	private GridMathDialog calculatorDialog = null;
	private TableFormat jTableVariables;
	private MapContext m_MapContext = null;
	private View view = null;
	private HashMap qWindowsHash = null;
	private GridExtent outputExtent;
	GridMathProcess gridMath = null;

	private JEP parser = null;
	private GridMathPanelListener listener = null;

	private boolean optionsTabVisible = true;

	/**
	 * @param calculatorDialog
	 * @param view	vista de la aplicacion
	 */
	public GridMathPanel(GridMathDialog calculatorDialog, View view) {
		super();
		this.view = view;
		this.calculatorDialog = calculatorDialog;
		if (view != null)
			m_MapContext = view.getModel().getMapContext();

		listener = new GridMathPanelListener(this);
		Inicializar();
	}


	/**
	 * Inicializar los elementos del Panel CalculatorPanel
	 */
	private void Inicializar(){

	 	JTabbedPane tabbedPane = new JTabbedPane();
	 	tabbedPane.addTab(PluginServices.getText(this, "operacion"), getCalculatorPanel());
	 	if (optionsTabVisible)
	 		tabbedPane.addTab(PluginServices.getText(this, "opciones"), getOptionsPanel());
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

	 	this.addButtonPressedListener(listener);
	 	this.getCalculatorPanel().getJTableVariables().getTableFormat().getModel().addTableModelListener(listener);
	}


	/**
	 * @return extent de salida
	 */
	public GridExtent getOutputExtent() {
		if (outputExtent==null)
			outputExtent= new GridExtent();
		return outputExtent;
	}


	/**
	 * @return proceso de calculo
	 */
	public GridMathProcess getGridMath() {
		if (gridMath==null)
			gridMath = new GridMathProcess();
		return gridMath;
	}

	/**
	 * @return calculatorDialog
	 */
	public IWindow getCalculatorDialog() {
		return calculatorDialog;
	}

	/**
	 * @return vista actual de la aplicacion
	 */
	public View getView() {
		return view;
	}

	/**
	 * @return panel de configuracion de salida
	 */
	public OutputOptionsPanel getOptionsPanel() {
		if (optionsPanel==null)
			optionsPanel = new OutputOptionsPanel(getView(),this);
		return optionsPanel;
	}

	public CalculatorPanel getCalculatorPanel() {
		if (calculatorPanel == null){
			calculatorPanel = new  CalculatorPanel(view);
		}
		return calculatorPanel;
	}
}


