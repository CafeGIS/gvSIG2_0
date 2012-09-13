package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.BorderLayout;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.remotesensing.calculator.gui.CalculatorPanel;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;
import org.gvsig.remotesensing.decisiontrees.gui.listener.ExpressionEditorPanelListener;
import org.jgraph.graph.DefaultGraphCell;

import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Panel para edición de nodos en la herramiento de árbole de decisión
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ExpressionEditorPanel extends DefaultButtonsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9078436882539529331L;
	
	private CalculatorPanel 					calculatorPanel = null;
	ExpressionEditorDialog 						expressionEditorDialog = null;
	private View 								view = null;
	private DecisionTreePanel 					decisionTreePanel = null;
	private ExpressionEditorPanelListener 		listener = null;
	private DecisionTreeNode 					decisionTreenode = null;

	public ExpressionEditorPanel(ExpressionEditorDialog expressionEditorDialog, DecisionTreePanel decisionTreePanel) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		this.expressionEditorDialog = expressionEditorDialog;
		this.decisionTreePanel = decisionTreePanel;
		this.view = decisionTreePanel.getView();
		initialize();
	}

	
	private void initialize() {
		setLayout(new BorderLayout(5,5));
		add(getCalculatorPanel(),BorderLayout.CENTER);
		listener = new ExpressionEditorPanelListener(this);
		DefaultGraphCell cell = getDecisionTreePanel().getSelectedCell();
		Object userObject = cell.getUserObject();
		cell.setUserObject("   ");
		if(userObject instanceof DecisionTreeNode){
			decisionTreenode = (DecisionTreeNode)userObject;
			getCalculatorPanel().getJTextExpression().setText(decisionTreenode.getExpression());
		}
		this.addButtonPressedListener(listener);
	}
	
	public CalculatorPanel getCalculatorPanel() {
		if (calculatorPanel == null){
			calculatorPanel = new CalculatorPanel(view, false);
		}
		return calculatorPanel;
	}


	public ExpressionEditorDialog getExpressionEditorDialog() {
		return expressionEditorDialog;
	}


	public DecisionTreePanel getDecisionTreePanel() {
		return decisionTreePanel;
	}


	public DecisionTreeNode getDecisionTreenode() {
		return decisionTreenode;
	}
}
