package org.gvsig.gui.beans.filterPanel.tableFilterQueryPanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel;


/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */

/**
 * This class is the graphical interface of the "FilterDialog" which was made by Fernando and is in 'appgvSIG' project.
 * It's supposed that other classes will extend from this and will add the logic as is in the "FilterDialog" class.
 *
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class TableFilterQueryJPanel extends AbstractFilterQueryJPanel implements Serializable {	
	private static final long serialVersionUID = 7215933791644769237L;

	protected JPanelML buttonsSetJPanel = null;
	protected JButtonML btnNewSet = null;
	protected JButtonML btnAddToCurrentSet = null;
	protected JButtonML btnFromSet = null;
	protected int defaultButtonsSetPanelWidth = 190; 
	protected int defaultButtonsSetPanelHeight = 100;
	protected int filterJScrollPaneHeight = defaultBottomJPanelHeight - 10;
	protected int filterJScrollPanelWidth = defaultBottomJPanelWidth - 10 - defaultButtonsSetPanelWidth;
	protected int setButtonWidth = defaultButtonsSetPanelWidth - 5;
	protected int setButtonHeight = 25;

	protected DefaultMutableTreeNode jtreeRoot;
	
	/**
	 * @see AbstractFilterQueryJPanel#AbstractFilterQueryJPanel()
	 */
	public TableFilterQueryJPanel() {
		super();
		this.initialize();
	}

	/**
	 * @see AbstractFilterQueryJPanel#AbstractFilterQueryJPanel(String)
	 */
	public TableFilterQueryJPanel(String _title) {
		super(_title);
		this.initialize();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#initialize()
	 */
	protected void initialize() {
		super.initialize();
		this.setBorder(BorderFactory.createTitledBorder(Messages.getText("filter_of_Table")));
		jtreeRoot = new DefaultMutableTreeNode(fieldsJTree.getModel().getRoot());
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getFieldsJPanel()
	 */
	protected JPanel getFieldsJPanel() {
		if (fieldsJPanel == null) {
			fieldsJPanel = new JPanelML();
			fieldsJPanel.setPreferredSize(new java.awt.Dimension(fieldsJPanelWidth, fieldsJPanelHeight));
			fieldsJPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;			
			fieldsJPanel.add(getFieldsJLabel(), gridBagConstraints);
			gridBagConstraints.anchor = GridBagConstraints.SOUTH;
			fieldsJPanel.add(getFieldsJScrollPane(), gridBagConstraints);			
		}

		return fieldsJPanel;
	}
	
	/**
	 * @see AbstractFilterQueryJPanel#getValuesJList()
	 */
	protected javax.swing.JList getValuesJList() {
		if (valuesJList == null) {
			valuesJList = new JListML(new DefaultListModel());
			valuesListModel = new DefaultListModel();
			valuesJList.setModel(valuesListModel);
			valuesJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			
//			valuesJList.addMouseListener(new MouseAdapter() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					if (e.getClickCount() == 2) {
//						int row = fieldsJTree.getRowForLocation(e.getX(), e.getY());
//	
//						if (row > -1) {
//							putSymbol(((DefaultListModel)valuesJList.getModel()).get(row).toString());
//						}
//					}
//				}
//			});
		}

		return valuesJList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getFieldsJScrollPane()
	 */
	protected javax.swing.JScrollPane getFieldsJScrollPane() {
		if (fieldsJScrollPane == null) {
			fieldsJScrollPane = new JScrollPaneML();
			fieldsJScrollPane.setPreferredSize(new java.awt.Dimension(fieldsAndValuesJScrollPaneWidth, fieldsAndValuesJScrollPaneHeight));
			fieldsJScrollPane.setViewportView(getFieldsJTree());
		}

		return fieldsJScrollPane;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getValuesJPanel()
	 */
	protected JPanel getValuesJPanel() {
		if (valuesJPanel == null) {
			valuesJPanel = new JPanelML();
			valuesJPanel.setPreferredSize(new java.awt.Dimension(valuesJPanelWidth, valuesJPanelHeight));
			valuesJPanel.setLayout(new GridBagLayout());
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			valuesJPanel.add(getValuesJLabel(), gridBagConstraints);
			gridBagConstraints.anchor = GridBagConstraints.SOUTH;
			valuesJPanel.add(getValuesJScrollPane(), gridBagConstraints);
		}

		return valuesJPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getValuesJScrollPane()
	 */
	protected javax.swing.JScrollPane getValuesJScrollPane() {
		if (valuesJScrollPane == null) {
			valuesJScrollPane = new JScrollPaneML();
			valuesJScrollPane.setPreferredSize(new Dimension(fieldsAndValuesJScrollPaneWidth, fieldsAndValuesJScrollPaneHeight));
			valuesJScrollPane.setViewportView(getValuesJList());
		}

		return valuesJScrollPane;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getFilterJScrollPane()
	 */
	protected JScrollPane getFilterJScrollPane() {
		if (filterJScrollPane == null) {
			filterJScrollPane = new JScrollPaneML();
			filterJScrollPane.setPreferredSize(new java.awt.Dimension(filterJScrollPanelWidth, filterJScrollPaneHeight));
			filterJScrollPane.setViewportView(getTxtExpression());
			filterJScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}

		return filterJScrollPane;
	}	
	
	/**
	 * This method initializes buttonsSetJPanel
	 *
	 * @return javax.swing.JPanel
	 */
	protected javax.swing.JPanel getButtonsSetJPanel() {
		if (buttonsSetJPanel == null) {
			buttonsSetJPanel = new JPanelML();
			buttonsSetJPanel.setPreferredSize(new java.awt.Dimension(defaultButtonsSetPanelWidth, defaultButtonsSetPanelHeight));
			buttonsSetJPanel.add(getBtnNewSet(), null);
			buttonsSetJPanel.add(getBtnAddToCurrentSet(), null);
			buttonsSetJPanel.add(getBtnFromSet(), null);			
		}

		return buttonsSetJPanel;
	}

	/**
	 * This method initializes btnNewSet
	 *
	 * @return javax.swing.JButton
	 */
	protected javax.swing.JButton getBtnNewSet() {
		if (btnNewSet == null) {
			btnNewSet = new JButtonML();
			btnNewSet.setPreferredSize(new Dimension(this.setButtonWidth, this.setButtonHeight));
			btnNewSet.setText(Messages.getText("Nuevo_conjunto"));
//			btnNewSet.setMargin(new java.awt.Insets(2, 2, 2, 2));
		}

		return btnNewSet;
	}

	/**
	 * This method initializes btnAddToCurrentSet
	 *
	 * @return javax.swing.JButton
	 */
	protected javax.swing.JButton getBtnAddToCurrentSet() {
		if (btnAddToCurrentSet == null) {
			btnAddToCurrentSet = new JButtonML();
			btnAddToCurrentSet.setPreferredSize(new Dimension(this.setButtonWidth, this.setButtonHeight));
			btnAddToCurrentSet.setText(Messages.getText("Anadir_al_conjunto"));
//			btnAddToCurrentSet.setMargin(new java.awt.Insets(2, 2, 2, 2));
		}

		return btnAddToCurrentSet;
	}

	/**
	 * This method initializes btnFromSet
	 *
	 * @return javax.swing.JButton
	 */
	protected javax.swing.JButton getBtnFromSet() {
		if (btnFromSet == null) {
			btnFromSet = new JButtonML();
			btnFromSet.setPreferredSize(new Dimension(this.setButtonWidth, this.setButtonHeight));
			btnFromSet.setText(Messages.getText(
					"Seleccionar_del_conjunto"));
//			btnFromSet.setMargin(new java.awt.Insets(2, 2, 2, 2));
		}

		return btnFromSet;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getTxtExpression()
	 */
	protected javax.swing.JTextArea getTxtExpression() {
		if (txtExpression == null) {
			txtExpression = new JEditableTextAreaML();
			txtExpression.setLineWrap(true);
			txtExpression.setToolTipText(Messages.getText("write_here_a_filter_expression"));
		}

		return txtExpression;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getTopJPanel()
	 */
	protected JPanel getTopJPanel() {
		if (topJPanel == null) {
			topJPanel = new JPanelML();

		    topJPanel.setPreferredSize(new Dimension(defaultTopJPanelWidth, defaultTopJPanelHeight));
		    topJPanel.setLayout(new GridBagLayout());
		    
		    GridBagConstraints gridBagConstraints = new GridBagConstraints();
		    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;	    
		    
		    gridBagConstraints.anchor = GridBagConstraints.WEST;
		    topJPanel.add(getFieldsJPanel(), gridBagConstraints);
		    
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			topJPanel.add(getFilterButtonsJPanel(), gridBagConstraints);
			
			gridBagConstraints.anchor = GridBagConstraints.EAST;
			topJPanel.add(getValuesJPanel(), gridBagConstraints);
		}
		
		return topJPanel;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#getBottomJPanel()
	 */
	protected JPanel getBottomJPanel() {
		if (bottomJPanel == null) {
			bottomJPanel = new JPanelML();
			bottomJPanel.setPreferredSize(new Dimension(defaultBottomJPanelWidth, defaultBottomJPanelHeight));
			bottomJPanel.add(getFilterJScrollPane(), null);
			bottomJPanel.add(getButtonsSetJPanel(), null);
		}
		
		return bottomJPanel;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.filterPanel.AbstractFilterQueryJPanel#resizeWidth(int)
	 */
	public void resizeWidth(int new_Width) {	
		int difference = new_Width - DefaultWidth;
		
		if (difference != 0) {
			this.setPreferredSize(new Dimension(this.getPreferredSize().width + difference, this.getPreferredSize().height));
			getTopJPanel().setPreferredSize(new Dimension(getTopJPanel().getPreferredSize().width + difference, getTopJPanel().getPreferredSize().height));
			getBottomJPanel().setPreferredSize(new Dimension(getBottomJPanel().getPreferredSize().width + difference, getBottomJPanel().getPreferredSize().height));
			int halfDifference = difference / 2;

			getFilterJScrollPane().setPreferredSize(new Dimension(getFilterJScrollPane().getPreferredSize().width + halfDifference, getFilterJScrollPane().getPreferredSize().height));
			
			getButtonsSetJPanel().setPreferredSize(new Dimension(getButtonsSetJPanel().getPreferredSize().width + halfDifference, getButtonsSetJPanel().getPreferredSize().height));
			getBtnNewSet().setPreferredSize(new Dimension(getBtnNewSet().getPreferredSize().width + halfDifference, getBtnNewSet().getPreferredSize().height));
			getBtnAddToCurrentSet().setPreferredSize(new Dimension(getBtnAddToCurrentSet().getPreferredSize().width + halfDifference, getBtnAddToCurrentSet().getPreferredSize().height));
			getBtnFromSet().setPreferredSize(new Dimension(getBtnFromSet().getPreferredSize().width + halfDifference, getBtnFromSet().getPreferredSize().height));
				
			getFieldsJLabel().setPreferredSize(new Dimension(getFieldsJLabel().getPreferredSize().width + halfDifference, getFieldsJLabel().getPreferredSize().height));
			getFieldsJPanel().setPreferredSize(new Dimension(getFieldsJPanel().getPreferredSize().width + halfDifference, getFieldsJPanel().getPreferredSize().height));
			getFieldsJScrollPane().setPreferredSize(new Dimension(getFieldsJScrollPane().getPreferredSize().width + halfDifference, getFieldsJScrollPane().getPreferredSize().height));
		
			getValuesJLabel().setPreferredSize(new Dimension(getValuesJLabel().getPreferredSize().width + halfDifference, getValuesJLabel().getPreferredSize().height));
			getValuesJPanel().setPreferredSize(new Dimension(getValuesJPanel().getPreferredSize().width + halfDifference, getValuesJPanel().getPreferredSize().height));
			getValuesJScrollPane().setPreferredSize(new Dimension(getValuesJScrollPane().getPreferredSize().width + halfDifference, getValuesJScrollPane().getPreferredSize().height));
		}
	}
}