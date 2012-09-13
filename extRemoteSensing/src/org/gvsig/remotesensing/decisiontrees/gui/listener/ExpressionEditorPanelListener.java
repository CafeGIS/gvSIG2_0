package org.gvsig.remotesensing.decisiontrees.gui.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.remotesensing.decisiontrees.gui.ExpressionEditorPanel;

import com.iver.andami.PluginServices;

/**
 * Listener del panel de edición de nodos.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ExpressionEditorPanelListener implements ButtonsPanelListener, ActionListener{

	private ExpressionEditorPanel expressionEditorPanel = null;

	public ExpressionEditorPanelListener(ExpressionEditorPanel expressionEditorPanel) {
		this.expressionEditorPanel  = expressionEditorPanel;
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}

		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			String oldExpression = expressionEditorPanel.getDecisionTreenode().getExpression();
			expressionEditorPanel.getDecisionTreenode().setExpression(expressionEditorPanel.getCalculatorPanel().getJTextExpression().getText());
			if (!expressionEditorPanel.getDecisionTreenode().getParser().hasError()){
				expressionEditorPanel.getCalculatorPanel().updatePersistentVarTable();
				ArrayList layersList = new ArrayList(expressionEditorPanel.getDecisionTreePanel().getDecisionTree().getVariablesTable().values());
				expressionEditorPanel.getDecisionTreePanel().getOutputOptionsPanel().setLayersList(layersList);
				close();
			}else{
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "bad_expresion"), this);
				expressionEditorPanel.getDecisionTreenode().setExpression(oldExpression);
			}
		}
		
	}

	private void close() {
		try {
			PluginServices.getMDIManager().closeWindow(expressionEditorPanel.getExpressionEditorDialog());
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}
	

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
