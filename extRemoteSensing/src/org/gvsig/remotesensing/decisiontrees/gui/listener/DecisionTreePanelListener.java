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
	 *   Av. Blasco Ibañez, 50
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
package org.gvsig.remotesensing.decisiontrees.gui.listener;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.RasterProcess;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.datastruct.ColorItem;
import org.gvsig.raster.grid.filter.FilterTypeException;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.RemoteSensingUtils;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeProcess;
import org.gvsig.remotesensing.decisiontrees.gui.ClassEditorDialog;
import org.gvsig.remotesensing.decisiontrees.gui.DecisionTreePanel;
import org.gvsig.remotesensing.decisiontrees.gui.ExpressionEditorDialog;
import org.gvsig.remotesensing.gui.beans.OptionsPanel;
import org.jgraph.graph.DefaultGraphCell;
import org.nfunk.jep.Variable;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * Listener del panel de Árboles de decisión.
 *
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTreePanelListener implements ButtonsPanelListener, ActionListener, MouseListener, IProcessActions {

	DecisionTreePanel decisionTreePanel = null;

	public DecisionTreePanelListener(DecisionTreePanel decisionTreePanel) {
		this.decisionTreePanel = decisionTreePanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==decisionTreePanel.getAddMenuItem()){
			DefaultGraphCell cell = decisionTreePanel.getSelectedCell();
			DecisionTreeNode node = (DecisionTreeNode)cell.getUserObject();
			int classId = node.getClassID();
			node.setChildren();
			node.getLeftChild().setClassID(classId);
			node.getRightChild().setClassID(decisionTreePanel.generateClassId());
			decisionTreePanel.reloadGraph();
		}

		else if (e.getSource()==decisionTreePanel.getDeleteMenuItem()){
			DefaultGraphCell cell = decisionTreePanel.getSelectedCell();
			DecisionTreeNode node = (DecisionTreeNode)cell.getUserObject();
			if(cell!=decisionTreePanel.getJGraph().getRoots()[0]){
				node.deleteChildren();
				decisionTreePanel.cleanClassColors();
				node.setClassID(decisionTreePanel.generateClassId());
				decisionTreePanel.reloadGraph();
			}
		}

		else if (e.getSource()==decisionTreePanel.getExecuteMenuItem()){
			DefaultGraphCell cell = decisionTreePanel.getSelectedCell();
			DecisionTreeNode node = (DecisionTreeNode)cell.getUserObject();
			executeTree(node);
		}

		else if (e.getSource()==decisionTreePanel.getCloseMenuItem()){
			close();
		}

		else if (e.getSource()==decisionTreePanel.getSaveMenuItem()){
			JFileChooser openFileChooser;
			openFileChooser = new JFileChooser("DECISION_TREE_PANEL_SAVE",JFileChooser.getLastPath("DECISION_TREE_PANEL_SAVE", null));
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new TreeFileFilter());
			int returnVal = openFileChooser.showSaveDialog(decisionTreePanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	String fileName = openFileChooser.getSelectedFile().toString();
	        	if (!fileName.endsWith(".tree")){
	        		fileName = fileName + ".tree";
	        	}
	        	File file = new File(fileName);
	        	if (file.exists()){
	        		JFileChooser.setLastPath("DECISION_TREE_PANEL_SAVE", file);
	        		int resp = JOptionPane.showConfirmDialog(
							(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
							PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
					if (resp != JOptionPane.YES_OPTION) {
						return;
					}
	        	}
				FileWriter writer;
				try {
					writer = new FileWriter(fileName);
					Marshaller m = new Marshaller(writer);
	    			m.setEncoding("ISO-8859-1");
	    			m.marshal(decisionTreePanel.getDecisionTree().getXMLEntity().getXmlTag());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MarshalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValidationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        }
		}

		else if (e.getSource()==decisionTreePanel.getLoadMenuItem()){
			JFileChooser openFileChooser;
			openFileChooser = new JFileChooser("DECISION_TREE_PANEL_LOAD",JFileChooser.getLastPath("DECISION_TREE_PANEL_LOAD", null));
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new TreeFileFilter());
			int returnVal = openFileChooser.showOpenDialog(decisionTreePanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File inFile = openFileChooser.getSelectedFile();
	            JFileChooser.setLastPath("DECISION_TREE_PANEL_LOAD", inFile);

	            try {
					FileReader fileReader = new FileReader(inFile);
					XmlTag xmlTag = (XmlTag) XmlTag.unmarshal(fileReader);
					XMLEntity xmlDecisionTree = new XMLEntity(xmlTag);
					decisionTreePanel.getDecisionTree().setXMLEntity(xmlDecisionTree);
					decisionTreePanel.reloadGraph();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (MarshalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValidationException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
	        }
		}

		else if (e.getSource()==decisionTreePanel.getImportMenuItem()){

		}

		else if (e.getSource()==decisionTreePanel.getExportMenuItem()){

		}

		else if(e.getSource()==decisionTreePanel.getJButtonLoadTree()){
			// Cargar arbol
			JFileChooser openFileChooser;
			openFileChooser = new JFileChooser("DECISION_TREE_PANEL_LOAD_TREE",JFileChooser.getLastPath("DECISION_TREE_PANEL_LOAD_TREE", null));

			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new TreeFileFilter());
			int returnVal = openFileChooser.showOpenDialog(decisionTreePanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File inFile = openFileChooser.getSelectedFile();
	            JFileChooser.setLastPath("DECISION_TREE_PANEL_LOAD_TREE", inFile);

	            try {
					FileReader fileReader = new FileReader(inFile);
					XmlTag xmlTag = (XmlTag) XmlTag.unmarshal(fileReader);
					XMLEntity xmlDecisionTree = new XMLEntity(xmlTag);
					decisionTreePanel.getDecisionTree().setXMLEntity(xmlDecisionTree);
					decisionTreePanel.reloadGraph();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (MarshalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValidationException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
	        }


		}

		else if(e.getSource()==decisionTreePanel.getJButtonSaveTree()){

			JFileChooser openFileChooser;
			openFileChooser = new JFileChooser("DECISION_TREE_PANEL_SAVE_TREE",JFileChooser.getLastPath("DECISION_TREE_PANEL_SAVE_TREE", null));
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new TreeFileFilter());
			int returnVal = openFileChooser.showSaveDialog(decisionTreePanel);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	String fileName = openFileChooser.getSelectedFile().toString();
	        	if (!fileName.endsWith(".tree")){
	        		fileName = fileName + ".tree";
	        	}
	        	File file = new File(fileName);
	        	if (file.exists()){
	        		 JFileChooser.setLastPath("DECISION_TREE_PANEL_SAVE_TREE", file);

	        		int resp = JOptionPane.showConfirmDialog(
							(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
							PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
					if (resp != JOptionPane.YES_OPTION) {
						return;
					}
	        	}
				FileWriter writer;
				try {
					writer = new FileWriter(fileName);
					Marshaller m = new Marshaller(writer);
	    			m.setEncoding("ISO-8859-1");
	    			m.marshal(decisionTreePanel.getDecisionTree().getXMLEntity().getXmlTag());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MarshalException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ValidationException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
	        }

		}
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
//		 Al pulsar Aceptar o Aplicar se ejecuta el aceptar del panel
		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			executeTree(decisionTreePanel.getDecisionTree().getRoot());
		}

		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CLOSE) {
			close();
		}
	}

	private void close() {
		try {
			PluginServices.getMDIManager().closeWindow(decisionTreePanel.getDecisionTreeDialog());
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}

	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() ==  MouseEvent.BUTTON3){
			showPopupMenu(e);
		}
		if (e.getClickCount() == 2) {
			editCell(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void editCell(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		DefaultGraphCell cell = (DefaultGraphCell) decisionTreePanel.getJGraph().getFirstCellForLocation(x, y);
		if (cell!=null){
			decisionTreePanel.setSelectedCell(cell);
			Object userObject = cell.getUserObject();
			if(userObject instanceof DecisionTreeNode){
				DecisionTreeNode node = (DecisionTreeNode)userObject;

				if(!node.isFinal()){
					ExpressionEditorDialog expressionEditorDialog = new ExpressionEditorDialog(600,250,decisionTreePanel);
					expressionEditorDialog.getExpressionEditorPanel().getCalculatorPanel().setPersistentVarTable(
							decisionTreePanel.getDecisionTree().getVariablesTable());
					PluginServices.getMDIManager().addWindow(expressionEditorDialog);
				}
				else{
					ClassEditorDialog classEditorDialog = new ClassEditorDialog(250,100,decisionTreePanel);
					PluginServices.getMDIManager().addWindow(classEditorDialog);
				}
				decisionTreePanel.reloadGraph();
			}
		}
	}

	private void showPopupMenu(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		DefaultGraphCell cell = (DefaultGraphCell) decisionTreePanel.getJGraph().getFirstCellForLocation(x, y);
		if (cell != null){
			if(cell.getUserObject() instanceof DecisionTreeNode){
				decisionTreePanel.getDeleteMenuItem().setVisible(true);
				decisionTreePanel.getAddMenuItem().setVisible(true);
				decisionTreePanel.getExecuteMenuItem().setVisible(true);
				if(cell == decisionTreePanel.getJGraph().getRoots()[0])
					decisionTreePanel.getDeleteMenuItem().setVisible(false);
				if(!((DecisionTreeNode)cell.getUserObject()).isFinal())
					decisionTreePanel.getAddMenuItem().setVisible(false);
				else{
					decisionTreePanel.getDeleteMenuItem().setVisible(false);
					decisionTreePanel.getExecuteMenuItem().setVisible(false);
				}
				decisionTreePanel.setSelectedCell(cell);
				decisionTreePanel.getJPopupMenu().show(e.getComponent(),  x, y);
			}
		}
	}

	/**
	 * Ejecuta el árbol desde el nodo indicado.
	 *
	 * @param root Nodo que se considera raiz en la ejecución de árbol.
	 */
	private void executeTree(DecisionTreeNode root){
		//Comprobar que todas laa variables estén asignadas:
		boolean allVarsAsigned = true;
		for (Iterator iter = decisionTreePanel.getDecisionTree().getVariablesTable().keySet().iterator(); iter.hasNext();) {
			String varName = (String) iter.next();
			String layerName = (String) decisionTreePanel.getDecisionTree().getVariablesTable().get(varName);
			if (layerName==null || layerName.equals("")){
				allVarsAsigned = false;
				break;
			}
		}

		if (allVarsAsigned){
			if (!root.hasError()){

				int nBand;
				String layerBand;
				String layerName;
				FLyrRasterSE rasterLayer;

				String path = getFileSelected();
				if (path == null)
					return;
				FLayers layers = decisionTreePanel.getView().getModel().getMapContext().getLayers();
				// Extent de salida personalizado.
				OptionsPanel outputOptionsPanel = decisionTreePanel.getOutputOptionsPanel();
				if (outputOptionsPanel.getRButtom2().isSelected()){
					try{
						outputOptionsPanel.getOutputExtent().setXRange(Double.parseDouble(outputOptionsPanel.getJTextRangoX1().getText()),Double.parseDouble(outputOptionsPanel.getJTextRangoX2().getText()));
						outputOptionsPanel.getOutputExtent().setYRange(Double.parseDouble(outputOptionsPanel.getJTextRangoY1().getText()), Double.parseDouble(outputOptionsPanel.getJTextRangoY2().getText()));
						outputOptionsPanel.getOutputExtent().setCellSizeX(Double.parseDouble(outputOptionsPanel.getJTextCellSizeX().getText()));
						outputOptionsPanel.getOutputExtent().setCellSizeY(Double.parseDouble(outputOptionsPanel.getJTextCellSizeY().getText()));
					}catch (NumberFormatException  e) {
						RasterToolsUtil.messageBoxError(PluginServices.getText(this, "invalid_number"), this);
						return;
					}
				}
				//	Extent de salida a partir de una capa.
				else if(outputOptionsPanel.getRButtom4().isSelected()){
					try {
						FLayer layer = layers.getLayer(outputOptionsPanel.getJComboCapas().getSelectedIndex());
						outputOptionsPanel.getOutputExtent().setXRange(layer.getFullEnvelope().getMinimum(0),
								layer.getFullEnvelope().getMaximum(0));
						outputOptionsPanel.getOutputExtent().setYRange(layer.getFullEnvelope().getMinimum(1),
								layer.getFullEnvelope().getMaximum(1));
						outputOptionsPanel.getOutputExtent().setCellSizeX(Double.parseDouble(outputOptionsPanel.getJTextCellSizeX().getText()));
						outputOptionsPanel.getOutputExtent().setCellSizeY(Double.parseDouble(outputOptionsPanel.getJTextCellSizeY().getText()));
						outputOptionsPanel.extentHasChanged();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				// Crear un HashMap con los rasterbuffers de entrada indexados por el nombre de variable.
				RasterBuffer valor=null;
				HashMap buffers = new HashMap();
				for (Iterator iter = decisionTreePanel.getDecisionTree().getVariablesTable().keySet().iterator(); iter.hasNext();) {
					String varName = (String) iter.next();

					layerBand =  (String)decisionTreePanel.getDecisionTree().getVariablesTable().get(varName);
					layerName = layerBand.substring(0,layerBand.indexOf("["));
					nBand = Integer.valueOf(layerBand.substring(layerBand.lastIndexOf("Band")+4,layerBand.lastIndexOf("]"))).intValue();
					rasterLayer = (FLyrRasterSE)layers.getLayer(layerName);
					BufferFactory bufferFactory= rasterLayer.getBufferFactory();

					double minX=0,minY=0,maxX=0,maxY=0;
					minX= outputOptionsPanel.getOutputExtent().getMin().getX();
					minY= outputOptionsPanel.getOutputExtent().getMin().getY();
					maxX= outputOptionsPanel.getOutputExtent().getMax().getX();
					maxY =outputOptionsPanel.getOutputExtent().getMax().getY();

					try {
							bufferFactory.setAdjustToExtent(false);
							bufferFactory.setDrawableBands(new int[]{nBand-1});
							bufferFactory.setAreaOfInterest(minX,minY,maxX,maxY,outputOptionsPanel.getOutputExtent().getNX(),
									outputOptionsPanel.getOutputExtent().getNY());
							valor=(RasterBuffer) bufferFactory.getRasterBuf();

					} catch (ArrayIndexOutOfBoundsException e) {
						RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_writer"), this, e);
					} catch (InvalidSetViewException e) {
							e.printStackTrace();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (RasterDriverException e) {
						e.printStackTrace();
					}

					buffers.put(varName,new Object[]{valor,new Integer(valor.getDataType())});
				}
				//Ejecutar el proceso:
				/*
				String viewName = PluginServices.getMDIManager().getWindowInfo(decisionTreePanel.getView()).getTitle();
				DecisionTreeProcess decisionTreeProcess = new DecisionTreeProcess(root, buffers, outputOptionsPanel.getOutputExtent(),
						viewName, path);
				IncrementableTask incrementableTask = new IncrementableTask(decisionTreeProcess);
				decisionTreeProcess.setIncrementableTask(incrementableTask);
				incrementableTask.showWindow();
				decisionTreeProcess.start();
				incrementableTask.start();
				*/

				//*********************************
				String viewName = PluginServices.getMDIManager().getWindowInfo(decisionTreePanel.getView()).getTitle();

				//Comprobar que estén todas las variables asignadas
				for (Iterator iter = root.getParser().getSymbolTable().values().iterator(); iter.hasNext();) {
					Variable variable = (Variable) iter.next();
					if (!buffers.containsKey(variable.getName())){
						RasterToolsUtil.messageBoxError(PluginServices.getText(this,"variables_sin_asignar"),this);
						return;
					}
				}

				RasterProcess decisionTreeProcess = new DecisionTreeProcess();
				decisionTreeProcess.setActions(this);
				decisionTreeProcess.addParam("tree", root);
				decisionTreeProcess.addParam("varsTable", buffers);
				decisionTreeProcess.addParam("viewName", viewName);
				decisionTreeProcess.addParam("filename", path);
				decisionTreeProcess.addParam("resultExtent", outputOptionsPanel.getOutputExtent());

				decisionTreeProcess.start();
			}else{
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "bad_expresion_in_tree"), this);
			}
		}
	}

	/**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (decisionTreePanel.getOutputOptionsPanel().getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("DECISION_TREE_PANEL_FILE_SELECTED",JFileChooser.getLastPath("DECISION_TREE_PANEL_FILE_SELECTED",null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(decisionTreePanel.getOutputOptionsPanel()) != JFileChooser.APPROVE_OPTION)
				return null;
			 JFileChooser.setLastPath("DECISION_TREE_PANEL_FILE_SELECTED", chooser.getSelectedFile());

			ExtendedFileFilter fileFilter = (ExtendedFileFilter) chooser.getFileFilter();
			path = fileFilter.getNormalizedFilename(chooser.getSelectedFile());
		} else {
			String file = decisionTreePanel.getOutputOptionsPanel().getJTextNombreCapa().getText();
			path = Utilities.createTempDirectory() + File.separator + decisionTreePanel.getOutputOptionsPanel().getJTextNombreCapa().getText() + ".tif";
			if(file.compareTo(RasterLibrary.getOnlyLayerName()) == 0)
				RasterLibrary.usesOnlyLayerName();
			decisionTreePanel.getOutputOptionsPanel().updateNewLayerText();
		}

		return path;
	}

	/**
	 * @return string con el path del fichero de salida
	 */
/*	public String getFileSelected() {
		String path = "";
		if (decisionTreePanel.getOutputOptionsPanel().getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser(FileOpenWizard.getLastPath());
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));

			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new WriterFilter(extList[i]));

			if (chooser.showOpenDialog(decisionTreePanel) != JFileChooser.APPROVE_OPTION)
				return null;

			String fName = chooser.getSelectedFile().toString();
			String ext = ((WriterFilter)chooser.getFileFilter()).getDescription();

			ext = ext.toLowerCase().substring(ext.lastIndexOf(".") + 1, ext.length());

			if ((fName != null) && !fName.equals(""))
				if (!fName.endsWith("." + ext))
					fName = fName + "." + ext;

			FileOpenWizard.setLastPath(chooser.getSelectedFile().getPath().substring(0, chooser.getSelectedFile().getPath().lastIndexOf(File.separator)));
			path = fName;
		} else {
			path = Utilities.createTempDirectory() + File.separator + decisionTreePanel.getOutputOptionsPanel().getJTextNombreCapa().getText() + ".tif";
			decisionTreePanel.getOutputOptionsPanel().updateNewLayerText();
		}
		return path;
	}*/

	public void end(Object fileName) {
		String viewName = PluginServices.getMDIManager().getWindowInfo(decisionTreePanel.getView()).getTitle();
		try {
			FLayer lyr = RasterToolsUtil.loadLayer(viewName,(String)fileName,null);
			/*
			 * Asignar la leyenda a la nueva capa:
			 */
			ArrayList colorItems = new ArrayList();
			ColorItem colorItem = null;
			HashMap classColors = decisionTreePanel.getDecisionTree().getColorTable();
			for (Iterator iter = classColors.keySet().iterator(); iter.hasNext();) {
				Integer classId = (Integer) iter.next();
				colorItem = new ColorItem();
				colorItem.setColor((Color)classColors.get(classId));
				colorItem.setNameClass("Class "+classId.toString());
				colorItem.setValue(classId.intValue());
				colorItems.add(colorItem);
			}

			RemoteSensingUtils.setLeyend(lyr, colorItems);

		} catch (RasterNotLoadException e) {
			RasterToolsUtil.messageBoxError("error_load_layer", this, e);
		} catch (FilterTypeException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_adding_leyend"), this, e);
		}
	}


	public void interrupted() {
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

/**
 * Filtro para el selector de expresiones matemáticas.
 * @author Alejandro Muñoz (alejandro.munoz@uclm.es)
 *
 */
class TreeFileFilter extends FileFilter {

	final static String exp = "tree";
	public boolean accept(File f) {
		if (f.isDirectory()) {
           return true;
       }
       String s = f.getName();
       int i = s.lastIndexOf('.');

       if (i > 0 &&  i < s.length() - 1) {
           String extension = s.substring(i+1).toLowerCase();
           if (exp.equals(extension)){
                   return true;
           } else {
               return false;
           }
       }
       return false;
	}

	public String getDescription() {
		 return "Archivos .tree";
	}

}
