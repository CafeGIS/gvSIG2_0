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

package org.gvsig.remotesensing.gridmath.gui.listener;

import java.io.File;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.gridmath.ErrorSintaxisException;
import org.gvsig.remotesensing.gridmath.GridMathProcess;
import org.gvsig.remotesensing.gridmath.NoAssignedVarsException;
import org.gvsig.remotesensing.gridmath.NoVarsException;
import org.gvsig.remotesensing.gridmath.gui.GridMathPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;


/**
 * Listener para el panel de la calculadora de bandas
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Alejandro Muñoz Sánchez	(alejandro.munoz@uclm.es)
 * @version 19/10/2007
 */
public class GridMathPanelListener implements ButtonsPanelListener, TableModelListener {

	private GridMathPanel gridMathPanel = null;
	private boolean canClose = false;


	/**
	 * Constructor
	 * @param calculatorPanel
	 */
	public GridMathPanelListener(GridMathPanel calculatorPanel) {
		this.gridMathPanel = calculatorPanel;

	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener#actionButtonPressed(org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent)
	 */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Botón de Aceptar

			if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
				aplicar();
				if(canClose)
					close();
			}
			// Botón de Aplicar
			if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
				aplicar();
			}


		// Botón de Cerrar
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}
	}


	 /**
	 *	Acciones al aplicar. Comprobar que la lista de variables no es vacia, que todas las variables
	 *  estan asignadas, y que la expresión es correcta. Si se cumplen los requisitos se lanza el calculo.
	 */
	private void aplicar(){

		try{

		canClose = false;
		if (gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount()>0){
			boolean allAsigned=true;

			// Comprobar que todas las variables estan asignadas
			for (int i=0; i<gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount(); i++){
				if (gridMathPanel.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(i,1).toString().equals(""))
				{ allAsigned=false; break;}
			}

			if (allAsigned){
				gridMathPanel.getCalculatorPanel().getParser().parseExpression(
						gridMathPanel.getCalculatorPanel().getJTextExpression().getText());
				if(!gridMathPanel.getCalculatorPanel().getParser().hasError()){
					canClose  = true;
					calculate();
				}
				else{
					throw new ErrorSintaxisException();
				}
			}else{
				throw new NoAssignedVarsException();
			}

		}else {
			throw new NoVarsException();
			}
		} catch (ErrorSintaxisException exc) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "bad_expresion"), this);
		}catch (NoAssignedVarsException exc){
			RasterToolsUtil.messageBoxError(PluginServices.getText(this,"variables_sin_asignar"),this);
		}catch (NoVarsException exc){
			RasterToolsUtil.messageBoxError(PluginServices.getText(this,"no_variables"),this);
		}
	}


	/**
	 * Método que construye el HasMap con los params y lanza el proceso para el
	 * calculo del raster resultante.
	 *
	 */
	private void calculate(){

		int nBand;
		String layerBand;
		String layerName;
		FLyrRasterSE rasterLayer;

		String path = getFileSelected();
		if (path == null)
			return;
		FLayers layers = gridMathPanel.getView().getModel().getMapContext().getLayers();

		// Extent de salida personalizado.
		if (gridMathPanel.getOptionsPanel().getRButtom2().isSelected()){
			try{
			gridMathPanel.getOutputExtent().setXRange(Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextRangoX1().getText()),Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextRangoX2().getText()));
			gridMathPanel.getOutputExtent().setYRange(Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextRangoY1().getText()), Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextRangoY2().getText()));
			gridMathPanel.getOutputExtent().setCellSizeX(Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextCellSizeX().getText()));
			gridMathPanel.getOutputExtent().setCellSizeY(Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextCellSizeY().getText()));
			}catch (NumberFormatException  e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "invalid_number"), this);
				return;
			}
		}
		//	Extent de salida a partir de una capa.
		else if(gridMathPanel.getOptionsPanel().getRButtom4().isSelected()){
			try {
				FLayer layer = layers.getLayer(gridMathPanel.getOptionsPanel().getJComboCapas().getSelectedIndex());
				gridMathPanel.getOutputExtent().setXRange(layer.getFullEnvelope().getMinimum(0),
						layer.getFullEnvelope().getMaximum(0));
				gridMathPanel.getOutputExtent().setYRange(layer.getFullEnvelope().getMinimum(1),
						layer.getFullEnvelope().getMaximum(1));
				gridMathPanel.getOutputExtent().setCellSize(Double.parseDouble(gridMathPanel.getOptionsPanel().getJTextCellSizeX().getText()));
				gridMathPanel.getOptionsPanel().extentHasChanged();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		gridMathPanel.getGridMath().setResultExtent(gridMathPanel.getOutputExtent());

		 //Rellenar el HasMap con el buffer corespondiente a cada variable



		/*
		Comprobar que todas las variables que intervienen en la expresion
		estan en el HasMap de parametros.
		 */


		 // Lanzar el proceso de cálculo.
		GridMathProcess process = new GridMathProcess();
		process.addParam("panel",gridMathPanel);
		process.addParam("expresion",gridMathPanel.getCalculatorPanel().getJTextExpression().getText());
		process.addParam("params",gridMathPanel.getGridMath().getParams());
		process.addParam("extent",gridMathPanel.getOutputExtent());
		process.addParam("mapcontext",gridMathPanel.getView().getModel().getMapContext());
		process.addParam("filepath", path);
	    process.start();

	}


	/**
	 * Acciones al cerrar
	 */
	private void close() {
		PluginServices.getMDIManager().closeWindow(gridMathPanel.getCalculatorDialog());
	}


	public void tableChanged(TableModelEvent e) {
		gridMathPanel.getOptionsPanel().InicializarOpcion();
	}

	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (gridMathPanel.getOptionsPanel().getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("GRID_MATH_PANEL_LISTENER",JFileChooser.getLastPath("GRID_MATH_PANEL_LISTENER", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(gridMathPanel.getOptionsPanel()) != JFileChooser.APPROVE_OPTION)
				return null;

			JFileChooser.setLastPath("GRID_MATH_PANEL_LISTENER", chooser.getSelectedFile());
			ExtendedFileFilter fileFilter = (ExtendedFileFilter) chooser.getFileFilter();
			path = fileFilter.getNormalizedFilename(chooser.getSelectedFile());
		} else {
			String file = gridMathPanel.getOptionsPanel().getJTextNombreCapa().getText();
			path = Utilities.createTempDirectory() + File.separator + gridMathPanel.getOptionsPanel().getJTextNombreCapa().getText() + ".tif";
			if(file.compareTo(RasterLibrary.getOnlyLayerName()) == 0)
				RasterLibrary.usesOnlyLayerName();
			gridMathPanel.getOptionsPanel().updateNewLayerText();
		}

		return path;
	}

}

/**
 * @author Nacho Brodin <brodin_ign@gva.es>
 * Filtro para el selector de formatos de escritura
 */
class WriterFilter extends javax.swing.filechooser.FileFilter {
	private String				filter;

	public WriterFilter(String fil) {
		this.filter = fil;
	}

	public boolean accept(File f) {
		return f.isDirectory() || f.getName().toLowerCase().endsWith("." + filter);
	}

	public String getDescription() {
		return "." + filter;
	}
}
