package org.gvsig.remotesensing.decisiontrees.gui.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;
import org.gvsig.remotesensing.decisiontrees.gui.ClassEditorPanel;
import org.jgraph.graph.DefaultGraphCell;

import com.iver.andami.PluginServices;

public class ClassEditorPanelListener implements ActionListener, ButtonsPanelListener {
	
	private ClassEditorPanel 	classEditorPanel 		= null;
	JColorChooser 				colorChooser 			= null;
	JDialog 					dialog 					= null;

	public ClassEditorPanelListener(ClassEditorPanel classEditorPanel) {
		this.classEditorPanel = classEditorPanel;
		colorChooser = new JColorChooser();
		dialog = JColorChooser.createDialog(classEditorPanel.getJButtonColor(), Messages.getText("select_color"), true, colorChooser, this, null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == classEditorPanel.getJButtonColor()){
			colorChooser.setColor(Color.RED);
			dialog.setVisible(true);
			classEditorPanel.getJButtonColor().setBackground(colorChooser.getColor());
		}
	}

	public void actionButtonPressed(ButtonsPanelEvent e) {
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL || e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			close();
		}

		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			DefaultGraphCell cell = classEditorPanel.getDecisionTreePanel().getSelectedCell();
			DecisionTreeNode node = (DecisionTreeNode)cell.getUserObject();
			node.setClassID(Integer.valueOf(classEditorPanel.getJtextValue().getText()).intValue());
			classEditorPanel.getDecisionTreePanel().getDecisionTree().getColorTable().put(new Integer(node.getClassID()), 
					classEditorPanel.getJButtonColor().getBackground());
		}
	}
	
	private void close() {
		try {
			PluginServices.getMDIManager().closeWindow(classEditorPanel.getClassEditorDialog());
		} catch (ArrayIndexOutOfBoundsException e) {
			//Si la ventana no se puede eliminar no hacemos nada
		}
	}

}
