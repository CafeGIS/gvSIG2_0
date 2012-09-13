package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;
import org.gvsig.remotesensing.decisiontrees.gui.listener.ClassEditorPanelListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import com.iver.andami.PluginServices;

public class ClassEditorPanel extends DefaultButtonsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 313490261946450414L;
	
	private ClassEditorDialog 			classEditorDialog 	= null;
	private JLabel						jLabelName			= null;
	private JLabel						jLabelValue			= null;
	private JLabel						jLabelColor 		= null;
	private JTextField					jtextColor 			= null;
	private JTextField					jtextValue			= null;
	private JButton						jButtonColor		= null;
	private ClassEditorPanelListener 	listener 			= null;
	private DecisionTreePanel 			decisionTreePanel = null;
	

	public ClassEditorPanel(ClassEditorDialog classEditorDialog, DecisionTreePanel decisionTreePanel) {
		super(ButtonsPanel.BUTTONS_ACCEPTCANCEL);
		this.classEditorDialog = classEditorDialog;
		this.decisionTreePanel = decisionTreePanel;
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.anchor = GridBagConstraints.WEST;
		add(getJLabelName(),constraints);
		constraints.gridx=0;
		constraints.gridy=1;
		constraints.anchor = GridBagConstraints.WEST;
		add(getJLabelValue(),constraints);
		constraints.gridx=0;
		constraints.gridy=2;
		constraints.anchor = GridBagConstraints.WEST;
		add(getJLabelColor(),constraints);
		constraints.gridx=1;
		constraints.gridy=0;
		constraints.insets = new Insets(3,3,3,3);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		add(getJtextColor(),constraints);
		constraints.gridx=1;
		constraints.gridy=1;
		constraints.insets = new Insets(3,3,3,3);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		add(getJtextValue(),constraints);
		constraints.gridx=1;
		constraints.gridy=2;
		constraints.insets = new Insets(3,3,3,3);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		add(getJButtonColor(),constraints);
		
		listener  = new ClassEditorPanelListener(this);
		getJButtonColor().addActionListener(listener);
		addButtonPressedListener(listener);
	}

	public ClassEditorDialog getClassEditorDialog() {
		return classEditorDialog;
	}

	public JTextField getJtextColor() {
		if (jtextColor == null)
			jtextColor = new JTextField();
		return jtextColor;
	}

	public JButton getJButtonColor() {
		if (jButtonColor == null){
			jButtonColor = new JButton();
			DefaultGraphCell cell = getDecisionTreePanel().getSelectedCell();
			Color color = GraphConstants.getBackground(cell.getAttributes());
			jButtonColor.setBackground(color);
			jButtonColor.setPreferredSize(new Dimension(60, 20));
		}
		return jButtonColor;
	}

	public JLabel getJLabelColor() {
		if (jLabelColor == null)
			jLabelColor = new JLabel(PluginServices.getText(this,"class_color")+":");
		return jLabelColor;
	}

	public JLabel getJLabelName() {
		if (jLabelName == null)
			jLabelName = new JLabel(PluginServices.getText(this, "nombre")+":");
		return jLabelName;
	}

	public JLabel getJLabelValue() {
		if (jLabelValue == null)
			jLabelValue = new JLabel(PluginServices.getText(this,"valor")+":");
		return jLabelValue;
	}

	public JTextField getJtextValue() {
		if (jtextValue == null){
			jtextValue = new  JTextField();
			DefaultGraphCell cell = getDecisionTreePanel().getSelectedCell();
			DecisionTreeNode node = (DecisionTreeNode)cell.getUserObject();
			jtextValue.setText(String.valueOf(node.getClassID()));
		}
		return jtextValue;
	}

	public DecisionTreePanel getDecisionTreePanel() {
		return decisionTreePanel;
	}
}
