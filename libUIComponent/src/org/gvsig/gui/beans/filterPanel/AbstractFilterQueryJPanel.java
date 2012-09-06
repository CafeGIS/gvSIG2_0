package org.gvsig.gui.beans.filterPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.text.Document;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.controls.MultiLineToolTip;
import org.gvsig.gui.beans.editabletextcomponent.JEditableTextArea;
import org.gvsig.gui.beans.filterPanel.filterButtons.FilterButtonsJPanel;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;

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
 * This abstract class represents the common components of the FilterQuery panels
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public abstract class AbstractFilterQueryJPanel extends AbstractPanel implements Serializable {
	public static final int DefaultHeight = 280;
	public static final int DefaultWidth = 490; 
	
	protected final int fieldsJPanelHeight = 145;
	protected final int fieldsJPanelWidth = 145;
	protected final int valuesJPanelHeight = fieldsJPanelHeight;
	protected final int valuesJPanelWidth = fieldsJPanelWidth;
	protected final int defaultBottomJPanelWidth = 480;
	protected final int defaultBottomJPanelHeight = 110;
	protected int filterJScrollPaneHeight;
	protected int filterJScrollPanelWidth;
	protected final int filterButtonsPanelHeight = FilterButtonsJPanel.default_FilterButtonsJPanelHeight;
	protected final int filterButtonsPanelWidth = FilterButtonsJPanel.default_FilterButtonsJPanelWidth;
	protected final int defaultTopJPanelWidth = defaultBottomJPanelWidth;
	protected final int defaultTopJPanelHeight = 145;
	protected int fieldsAndValuesJScrollPaneHeight = 110;
	protected int fieldsAndValuesJScrollPaneWidth = fieldsJPanelWidth;
	
	protected JLabelML fieldsJLabel = null;
	protected JLabelML valuesJLabel = null;
	protected JPanelML fieldsJPanel = null;
	protected JPanelML valuesJPanel = null;
	protected FilterButtonsJPanel filterButtonsJPanel = null;
	protected JScrollPaneML filterJScrollPane = null;
	protected JPanelML topJPanel = null;
	protected JPanelML bottomJPanel = null;
	protected JEditableTextAreaML txtExpression = null;
	protected JTreeML fieldsJTree = null;
	protected JListML valuesJList = null;
	protected JScrollPaneML fieldsJScrollPane = null;
	protected JScrollPaneML valuesJScrollPane = null;		

	protected String title;
	
	protected DefaultTreeModel defaultTreeModel;
	protected DefaultListModel valuesListModel;
		
	// A set with all simbols or operator names used
	private Set<String> operatorSymbols;
	
	
	/**
	 * This is the default constructor
	 */
	public AbstractFilterQueryJPanel(String _title) {		
		super();
		title = _title;
	}
	/**
	 * This is the default constructor
	 */
	public AbstractFilterQueryJPanel() {		
		super();
	}
	
	/**
	 * This method initializes this
	 */
	protected void initialize() {
		operatorSymbols = new HashSet<String>();
			
		operatorSymbols.add("and");
//		operatorSymbols.add("Date");
		operatorSymbols.add("<>"); // In SQL this is the formal operator
		operatorSymbols.add("!="); // This operator is also supported
		operatorSymbols.add("=");
		operatorSymbols.add(">=");
		operatorSymbols.add("<=");
		operatorSymbols.add(">");
		operatorSymbols.add("not");
		operatorSymbols.add("or");
		operatorSymbols.add("(");
		operatorSymbols.add(")");
		operatorSymbols.add("<");

		this.setPreferredSize(new Dimension(DefaultWidth, DefaultHeight));
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		this.add(getTopJPanel(), gridBagConstraints);

		gridBagConstraints.anchor = GridBagConstraints.SOUTH;
		this.add(getBottomJPanel(), gridBagConstraints);
	}
	
	/**
	 * This method initializes topJPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected abstract javax.swing.JPanel getTopJPanel();
	
	/**
	 * This method initializes bottomJPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected abstract JPanel getBottomJPanel();	
	
	/**
	 * This method initializes fieldsJLabel
	 *
	 * @return javax.swing.JLabel
	 */
	protected JLabel getFieldsJLabel() {
		if (fieldsJLabel == null) {
			fieldsJLabel = new JLabelML();
			fieldsJLabel.setText(Messages.getText("fields_uppercase_first") + ":");
		}

		return fieldsJLabel;
	}	

	/**
	 * This method initializes valuesJList
	 *
	 * @return javax.swing.JList
	 */
	protected abstract javax.swing.JList getValuesJList();
	
	/**
	 * This method initializes fieldsJPanel
	 *
	 * @return javax.swing.JPanel
	 */
	protected abstract JPanel getFieldsJPanel();

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	protected abstract javax.swing.JScrollPane getFieldsJScrollPane();

	/**
	 * This method initializes valuesJLabel
	 *
	 * @return javax.swing.JLabel
	 */
	protected javax.swing.JLabel getValuesJLabel() {
		if (valuesJLabel == null) {
			valuesJLabel = new JLabelML();
			valuesJLabel.setText(Messages.getText("known_values") + ":");
		}

		return valuesJLabel;
	}
	
	/**
	 * This method initializes valuesJPanel
	 *
	 * @return javax.swing.JPanel
	 */
	protected abstract JPanel getValuesJPanel();
	
	/**
	 * This method initializes jScrollPane1
	 *
	 * @return javax.swing.JScrollPane
	 */
	protected abstract javax.swing.JScrollPane getValuesJScrollPane();
	
	/**
	 * This method initializes filterJScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	protected abstract javax.swing.JScrollPane getFilterJScrollPane();

	/**
	 * This method initializes txtExpression
	 *
	 * @return javax.swing.JTextArea
	 */
	protected abstract javax.swing.JTextArea getTxtExpression();
	
	/**
	 * Adds a symbol to the filter expression.
	 *
	 * @param symbol symbol to add
	 */
	protected void putSymbol(String symbol) {
		int position = getTxtExpression().getCaretPosition();
		
		getTxtExpression().setText(insert(getTxtExpression().getText(), position, symbol));

		if (symbol.equals(" () ")) {
			position = position + 2;
		} else {
			position = position + symbol.length();
		}

		getTxtExpression().setCaretPosition(position);
	}
	
	/**
	 * This method initializes fieldsJTree
	 *
	 * @return org.gvsig.gui.beans.swing.jTree
	 */
	protected javax.swing.JTree getFieldsJTree() {
		if (fieldsJTree == null) {
			fieldsJTree = new JTreeML(new Vector(0,1));
			
			// Remove icons:
			DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
			defaultTreeCellRenderer.setOpenIcon(null);
			defaultTreeCellRenderer.setClosedIcon(null);
			defaultTreeCellRenderer.setLeafIcon(null);
			
			// Root not visible
			fieldsJTree.setRootVisible(false);
			fieldsJTree.setCellRenderer(defaultTreeCellRenderer);
		}

		return fieldsJTree;
	}
	
    /**
     * Inserts an <i>string</i> at a position of another one.
     *
     * @param base original <i>string</i> where will be inserted
     * @param position position at the <i>string</i> where will be inserted
     * @param graft <i>string</i> to insert
     *
     * @return the new <i>string</i> with the graft inserted in
     */
    protected static String insert(String base, int position, String graft) {
        return base.substring(0, position) + graft + base.substring(position);
    }

	/**
	 * This method initializes filterButtonsJPanel
	 *
	 * @return javax.swing.JPanel
	 */
	protected JPanel getFilterButtonsJPanel() {
		if (filterButtonsJPanel == null) {
			filterButtonsJPanel = new FilterButtonsJPanel();
			filterButtonsJPanel.setPreferredSize(new Dimension(filterButtonsPanelWidth, filterButtonsPanelHeight));
			filterButtonsJPanel.addPropertyChangeListener(new PropertyChangeListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
				 */
				public void propertyChange(PropertyChangeEvent arg0) {
					if (arg0.getPropertyName().equals(FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND)) {
		            	
						switch(Integer.parseInt(arg0.getNewValue().toString()))	{
							case FilterButtonsJPanel.AND:
								putSymbol(" and ");
								break;
							case FilterButtonsJPanel.DATE:
								putSymbol( ((FilterButtonsJPanel)arg0.getSource()).getLastSelectedDate() );
								break;
							case FilterButtonsJPanel.DISTINCT:
								putSymbol(" <> ");
								break;
							case FilterButtonsJPanel.EQUAL:
								putSymbol(" = ");
								break;
							case FilterButtonsJPanel.EQUALGREATER:
								putSymbol(" >= ");
								break;
							case FilterButtonsJPanel.EQUALSMALLER:
								putSymbol(" <= ");
								break;
							case FilterButtonsJPanel.GREATER:
								putSymbol(" > ");
								break;
							case FilterButtonsJPanel.NOT:
								putSymbol(" not ");
								break;
							case FilterButtonsJPanel.OR:
								putSymbol(" or ");
								break;
							case FilterButtonsJPanel.PARENTHESIS:
								putSymbol(" () ");
								break;
							case FilterButtonsJPanel.SMALLER:
								putSymbol(" < ");
								break;
							case FilterButtonsJPanel.DELETE_TEXT:
								txtExpression.setText("");
								break;
							default: // do anything
				        }
					}
				}			
			});
		}
		
		return filterButtonsJPanel;
	}

	/**
	 * Returns a set with all symbols used as operators
	 * 
	 * @return A set
	 */
	protected Set<String> getAllOperatorSymbols() {
		return operatorSymbols;		
	}

	/**
	 * Sets new height to the 'topJPanel', (new Height must be bigger than default, else do nothing)
	 * 
	 * @param new_Height New height
	 */
	public void resizeHeight(int new_Height) {
		int difference = new_Height - DefaultHeight;
		
		if (difference > 0) {
			this.setPreferredSize(new Dimension(this.getPreferredSize().width, this.getPreferredSize().height + difference));
			getTopJPanel().setPreferredSize(new Dimension(getTopJPanel().getPreferredSize().width, getTopJPanel().getPreferredSize().height + difference));
			
			getFieldsJPanel().setPreferredSize(new Dimension(getFieldsJPanel().getPreferredSize().width, getFieldsJPanel().getPreferredSize().height + difference));
			getFieldsJScrollPane().setPreferredSize(new Dimension(getFieldsJScrollPane().getPreferredSize().width, getFieldsJScrollPane().getPreferredSize().height + difference));
		
			getValuesJPanel().setPreferredSize(new Dimension(getValuesJPanel().getPreferredSize().width, getValuesJPanel().getPreferredSize().height + difference));
			getValuesJScrollPane().setPreferredSize(new Dimension(getValuesJScrollPane().getPreferredSize().width, getValuesJScrollPane().getPreferredSize().height + difference));
		}
	}

	/**
	 * Sets the width to this JPanel
	 * 
	 * @param new_Width New width
	 */
	public abstract void resizeWidth(int new_Width);

	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#resize(int, int)
	 */
	public void resize(int width, int height) {
		int difference = height - DefaultHeight;

		if (difference != 0)
			this.resizeHeight(height);
		
		this.resizeWidth(width);
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#resize(java.awt.Dimension)
	 */
	public void resize(Dimension d) {
		int difference = d.height - DefaultHeight;

		if (difference != 0)
			this.resizeHeight(d.height);
		
		this.resizeWidth(d.width);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#accept()
	 */
	public void accept() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#apply()
	 */
	public void apply() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#cancel()
	 */
	public void cancel() {
	}	

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#selected()
	 */
	public void selected() {		
	}

	/**
	 * JCrollPane with multi line tool tip text.
	 * 
	 * @see JScrollPane
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JScrollPaneML extends JScrollPane {
		private static final long serialVersionUID = 5222187234181725243L;

		/**
		 * @see JScrollPane#JScrollPane()
		 */
		public JScrollPaneML() {
			super();
		}

		/**
		 * @see JScrollPane#JScrollPane(Component, int, int)
		 */
		public JScrollPaneML(Component view, int vsbPolicy, int hsbPolicy) {
			super(view, vsbPolicy, hsbPolicy);
		}

		/**
		 * @see JScrollPane#JScrollPane(Component)
		 */
		public JScrollPaneML(Component view) {
			super(view);
		}

		/**
		 * @see JScrollPane#JScrollPane(int, int)
		 */
		public JScrollPaneML(int vsbPolicy, int hsbPolicy) {
			super(vsbPolicy, hsbPolicy);
		}
		
		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }
	}

	/**
	 * JLabel with multi line tool tip text.
	 * 
	 * @see JLabel
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JLabelML extends JLabel {
		private static final long serialVersionUID = 525349503578470487L;

		/**
		 * @see JLabel#JLabel()
		 */
		public JLabelML() {
			super();
		}

		/**
		 * @see JLabel#JLabel(Icon, int)
		 */
		public JLabelML(Icon image, int horizontalAlignment) {
			super(image, horizontalAlignment);
		}

		/**
		 * @see JLabel#JLabel(Icon)
		 */
		public JLabelML(Icon image) {
			super(image);
		}

		/**
		 * @see JLabel#JLabel(String, Icon, int)
		 */
		public JLabelML(String text, Icon icon, int horizontalAlignment) {
			super(text, icon, horizontalAlignment);
		}

		/**
		 * @see JLabel#JLabel(String, int)
		 */
		public JLabelML(String text, int horizontalAlignment) {
			super(text, horizontalAlignment);
		}

		/**
		 * @see JLabel#JLabel(String)
		 */
		public JLabelML(String text) {
			super(text);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }
	}
	
	/**
	 * JPanel with multi line tool tip text.
	 * 
	 * @see JPanel
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JPanelML extends JPanel {
		private static final long serialVersionUID = 5951860640473906815L;

		/**
		 * @see JPanel#JPanel()
		 */
		public JPanelML() {
			super();
		}

		/**
		 * @see JPanel#JPanel(boolean)
		 */
		public JPanelML(boolean isDoubleBuffered) {
			super(isDoubleBuffered);
		}

		/**
		 * @see JPanel#JPanel(LayoutManager, boolean)
		 */
		public JPanelML(LayoutManager layout, boolean isDoubleBuffered) {
			super(layout, isDoubleBuffered);
		}

		/**
		 * @see JPanel#JPanel(LayoutManager)
		 */
		public JPanelML(LayoutManager layout) {
			super(layout);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }
	}

	/**
	 * Editable text area with multi line tool tip text.
	 * 
	 * @see JEditableTextArea
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JEditableTextAreaML extends JEditableTextArea {
		private static final long serialVersionUID = -6963953475368014077L;

		/**
		 * @see JTextArea#JTextArea()
		 */
		public JEditableTextAreaML() {
			super();
		}

		/**
		 * @see JTextArea#JTextArea(Document, String, int, int)
		 */
		public JEditableTextAreaML(Document doc, String text, int rows, int columns) {
			super(doc, text, rows, columns);
		}

		/**
		 * @see JTextArea#JTextArea(Document)
		 */
		public JEditableTextAreaML(Document doc) {
			super(doc);
		}

		/**
		 * @see JTextArea#JTextArea(int, int)
		 */
		public JEditableTextAreaML(int rows, int columns) {
			super(rows, columns);
		}

		/**
		 * @see JTextArea#JTextArea(String, int, int)
		 */
		public JEditableTextAreaML(String text, int rows, int columns) {
			super(text, rows, columns);
		}

		/**
		 * @see JTextArea#JTextArea(String)
		 */
		public JEditableTextAreaML(String text) {
			super(text);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }	
	}

	/**
	 * JTree with multi line tool tip text.
	 * 
	 * @see JTree
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JTreeML extends JTree {
		private static final long serialVersionUID = -8619256256346496435L;

		/**
		 * @see JTree#JTree()
		 */
		public JTreeML() {
			super();
		}

		/**
		 * @see JTree#JTree(Hashtable)
		 */
		public JTreeML(Hashtable<?, ?> value) {
			super(value);
		}

		/**
		 * @see JTree#JTree(Object[])
		 */
		public JTreeML(Object[] value) {
			super(value);
		}

		/**
		 * @see JTree#JTree(TreeModel)
		 */
		public JTreeML(TreeModel newModel) {
			super(newModel);
		}

		/**
		 * @see JTree#JTree(TreeNode, boolean)
		 */
		public JTreeML(TreeNode root, boolean asksAllowsChildren) {
			super(root, asksAllowsChildren);
		}

		/**
		 * @see JTree#JTree(TreeNode)
		 */
		public JTreeML(TreeNode root) {
			super(root);
		}

		/**
		 * @see JTree#JTree(Vector)
		 */
		public JTreeML(Vector<?> value) {
			super(value);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }		
	}

	/**
	 * JList with multi line tool tip text.
	 * 
	 * @see JList
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JListML extends JList {
		private static final long serialVersionUID = 5316332987144988365L;

		/**
		 * @see JList#JList()
		 */
		public JListML() {
			super();
		}

		/**
		 * @see JList#JList(ListModel)
		 */
		public JListML(ListModel dataModel) {
			super(dataModel);
		}

		/**
		 * @see JList#JList(Object[])
		 */
		public JListML(Object[] listData) {
			super(listData);
		}

		/**
		 * @see JTree#JTree(Vector)
		 */
		public JListML(Vector<?> listData) {
			super(listData);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }		
	}

	/**
	 * JCheckBox with multi line tool tip text.
	 * 
	 * @see JCheckBox
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
	protected class JCheckBoxML extends JCheckBox {
		private static final long serialVersionUID = 2610142188236840664L;

		/**
		 * @see JCheckBox#JCheckBox()
		 */
		public JCheckBoxML() {
			super();
		}

		/**
		 * @see JCheckBox#JCheckBox(Action)
		 */
		public JCheckBoxML(Action a) {
			super(a);
		}

		/**
		 * @see JCheckBox#JCheckBox(Icon, boolean)
		 */
		public JCheckBoxML(Icon icon, boolean selected) {
			super(icon, selected);
		}

		/**
		 * @see JCheckBox#JCheckBox(Icon)
		 */
		public JCheckBoxML(Icon icon) {
			super(icon);
		}

		/**
		 * @see JCheckBox#JCheckBox(String, boolean)
		 */
		public JCheckBoxML(String text, boolean selected) {
			super(text, selected);
		}

		/**
		 * @see JCheckBox#JCheckBox(String, Icon, boolean)
		 */
		public JCheckBoxML(String text, Icon icon, boolean selected) {
			super(text, icon, selected);
		}

		/**
		 *@see JCheckBox#JCheckBox(String, Icon)
		 */
		public JCheckBoxML(String text, Icon icon) {
			super(text, icon);
		}

		/**
		 * @see JCheckBox#JCheckBox(String)
		 */
		public JCheckBoxML(String text) {
			super(text);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#createToolTip()
		 */
	    public JToolTip createToolTip() {
	    	// Multiline support
	    	MultiLineToolTip tip = new MultiLineToolTip();
	    	tip.setComponent(this);
	    	return tip;
	    }		
	}

	/**
	 * JButton with multi line tool tip text.
	 * 
	 * @see JButton
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
    protected class JButtonML extends JButton {
		private static final long serialVersionUID = -6052122756677251026L;

		/**
		 * @see JButton#JButton()
		 */
		public JButtonML() {
			super();
		}

		/**
		 * @see JButton#JButton(Action)
		 */
		public JButtonML(Action a) {
			super(a);
		}

		/**
		 * @see JButton#JButton(Icon)
		 */
		public JButtonML(Icon icon) {
			super(icon);
		}

		/**
		 * @see JButton#JButton(String, Icon)
		 */
		public JButtonML(String text, Icon icon) {
			super(text, icon);
		}

		/**
		 * @see JButton#JButton(String)
		 */
		public JButtonML(String text) {
			super(text);
		}

		/*
    	 * (non-Javadoc)
    	 * @see javax.swing.JComponent#createToolTip()
    	 */
        public JToolTip createToolTip() {
        	// Multiline support
        	MultiLineToolTip tip = new MultiLineToolTip();
        	tip.setComponent(this);
        	return tip;
        }		
    }
}
