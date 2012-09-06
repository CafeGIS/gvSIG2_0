package org.gvsig.gui.beans.filterPanel.filterButtons;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolTip;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.controls.MultiLineToolTip;
import org.gvsig.gui.beans.swing.textBoxWithCalendar.JCalendarDateDialog;

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
 * This class is a panel with buttons for filter operations: AND, OR, NOT, >, <, ...
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class FilterButtonsJPanel extends JPanel implements Serializable {
	private static final long serialVersionUID = -6976915487897365666L;

	public static final int default_FilterButtonsJPanelWidth = 190;
	public static final int default_FilterButtonsJPanelHeight = 104;
	private final int buttonsGroupJPanelWidth = default_FilterButtonsJPanelWidth;
	private final int buttonsGroupJPanelHeight = default_FilterButtonsJPanelHeight;
	private final int buttonHeight = 20;
	private final int buttonWidthUnit = 40;

	private JButtonML btnEqual = null;
	private JButtonML btnDistinct = null;
	private JButtonML btnGreater = null;
	private JButtonML btnSmaller = null;
	private JButtonML btnEqualGreater = null;
	private JButtonML btnEqualSmaller = null;
	private JButtonML btnAnd = null;
	private JButtonML btnOr = null;
	private JButtonML btnNot = null;
	private JButtonML btnDate = null;
	private JButtonML btnParenthesis = null;
	private JButtonML btnDeleteText = null;
	private JPanelML buttonsJPanel = null;	

	// Last selected date
	private DateFormat dateFormat = DateFormat.getDateInstance();
	private Date lastSelectedDate = null;

	private JCalendarDateDialog jCalendarDateDialog = null;
	
	// Listener for property change support
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	// Values of the events fired when has been clicked a button
	public static final int DEFAULT = 0;
	public static final int EQUAL = 1;
	public static final int DISTINCT = 2;
	public static final int GREATER = 3;
	public static final int SMALLER = 4;
	public static final int EQUALGREATER = 5;
	public static final int EQUALSMALLER = 6;
	public static final int AND = 7;
	public static final int OR = 8;
	public static final int NOT = 9;
	public static final int DATE = 10;
	public static final int PARENTHESIS = 11;
	public static final int DELETE_TEXT = 12;
	
	// Values of the type of event fired
	public static final int BUTTON_CLICKED_ACTION_ID = 13;
	public static final String BUTTON_CLICKED_ACTION_COMMAND = "Button Clicked";
	
	// Hash map for the items
	private HashMap<String, String> map;
	
	// Action listener for notify (fire) some events that had happened to this component
	private ActionListener actionListener = null;

	/**
	 * @see JPanel#JPanel()
	 */
	public FilterButtonsJPanel() {
		super();
		initialize();
	}

	/**
	 * @see JPanel#JPanel(boolean)
	 */
	public FilterButtonsJPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		initialize();
	}

	/**
	 * @see JPanel#JPanel(java.awt.LayoutManager, boolean)
	 */
	public FilterButtonsJPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		initialize();
	}

	/**
	 * @see JPanel#JPanel(java.awt.LayoutManager)
	 */
	public FilterButtonsJPanel(LayoutManager layout) {
		super(layout);
		initialize();
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		map = new HashMap<String, String>();
		
		this.setPreferredSize(new Dimension(default_FilterButtonsJPanelWidth, default_FilterButtonsJPanelHeight));
		this.setLayout(new GridBagLayout());
		
		// Vertical center
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;

		this.add(getButtonsJPanel(), gridBagConstraints);
	}
	
	/**
	 * This method initializes buttonsJPanel
	 *
	 * @return javax.swing.JPanel
	 */	
	private javax.swing.JPanel getButtonsJPanel() {
		if (buttonsJPanel == null) {
			buttonsJPanel = new JPanelML();
			
			buttonsJPanel.setPreferredSize(new Dimension(buttonsGroupJPanelWidth, buttonsGroupJPanelHeight));
			buttonsJPanel.add(getBtnEqual());
			buttonsJPanel.add(getBtnDistinct());
			buttonsJPanel.add(getBtnDate());
			buttonsJPanel.add(getBtnSmaller());
			buttonsJPanel.add(getBtnGreater());
			buttonsJPanel.add(getBtnEqualSmaller());
			buttonsJPanel.add(getBtnEqualGreater());
			buttonsJPanel.add(getBtnAnd());
			buttonsJPanel.add(getBtnOr());
			buttonsJPanel.add(getBtnNot());
			buttonsJPanel.add(getBtnParenthesis());			
			buttonsJPanel.add(getBtnDeleteText());
		}
		
		return buttonsJPanel;
	}

	/**
	 * This method initializes btnDistinct
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnDistinct() {
		if (btnDistinct == null) {
			btnDistinct = new JButtonML();
			btnDistinct.setText("!=");
			btnDistinct.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnDistinct.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnDistinct.setToolTipText(Messages.getText("operator_distinct_explanation"));
			map.put("!=", Integer.toString(FilterButtonsJPanel.DISTINCT));
			
			btnDistinct.addActionListener(this.getActionListener());
		}
		
		return btnDistinct;
	}
	
	
	/**
	 * This method initializes btnEqua
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnEqual() {
		if (btnEqual == null) {
			btnEqual = new JButtonML();
			btnEqual.setText("=");
			btnEqual.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnEqual.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnEqual.setToolTipText(Messages.getText("operator_equal_explanation"));
			map.put("=", Integer.toString(FilterButtonsJPanel.EQUAL));

			btnEqual.addActionListener(this.getActionListener());
		}

		return btnEqual;
	}

	/**
	 * This method initializes btnGreater
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnGreater() {
		if (btnGreater == null) {
			btnGreater = new JButtonML();
			btnGreater.setText(">");
			btnGreater.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnGreater.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnGreater.setToolTipText(Messages.getText("operator_greater_explanation"));
			map.put(">", Integer.toString(FilterButtonsJPanel.GREATER));
			
			btnGreater.addActionListener(this.getActionListener());
		}

		return btnGreater;
	}

	/**
	 * This method initializes btnEqualGreater
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnEqualGreater() {
		if (btnEqualGreater == null) {
			btnEqualGreater = new JButtonML();
			btnEqualGreater.setText(">=");
			btnEqualGreater.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnEqualGreater.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnEqualGreater.setToolTipText(Messages.getText("operator_equal_greater_explanation"));
			map.put(">=", Integer.toString(FilterButtonsJPanel.EQUALGREATER));
			
			btnEqualGreater.addActionListener(this.getActionListener());
		}

		return btnEqualGreater;
	}

	/**
	 * This method initializes btnSmaller
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnSmaller() {
		if (btnSmaller == null) {
			btnSmaller = new JButtonML();
			btnSmaller.setText("<");
			btnSmaller.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnSmaller.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnSmaller.setToolTipText(Messages.getText("operator_smaller_explanation"));
			map.put("<", Integer.toString(FilterButtonsJPanel.SMALLER));
			
			btnSmaller.addActionListener(this.getActionListener());
		}

		return btnSmaller;
	}

	/**
	 * This method initializes btnEqualSmaller
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnEqualSmaller() {
		if (btnEqualSmaller == null) {
			btnEqualSmaller = new JButtonML();
			btnEqualSmaller.setText("<=");
			btnEqualSmaller.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnEqualSmaller.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnEqualSmaller.setToolTipText(Messages.getText("operator_equal_smaller_explanation"));
			map.put("<=", Integer.toString(FilterButtonsJPanel.EQUALSMALLER));
			
			btnEqualSmaller.addActionListener(this.getActionListener());
		}

		return btnEqualSmaller;
	}

	/**
	 * This method initializes btnDate
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnDate() {
		if (btnDate == null) {
			btnDate = new JButtonML();
			btnDate.setText("Date");
			btnDate.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnDate.setPreferredSize(new java.awt.Dimension(86, buttonHeight));
			btnDate.setToolTipText(Messages.getText("date_button_explanation"));
			map.put("Date", Integer.toString(FilterButtonsJPanel.DATE));
			
			btnDate.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					getCDD().setVisible(true);
				}				
			});			
		}

		return btnDate;
	}

	/**
	 * This method initializes a JCalendarDateDialog	
	 * 	
	 * @return A JCalendarDateDialog
	 */
	protected JCalendarDateDialog getCDD() {
		if (jCalendarDateDialog == null) {
			jCalendarDateDialog = new JCalendarDateDialog(350, 230);
			jCalendarDateDialog.setModal(true);
			jCalendarDateDialog.setLocationRelativeTo(btnDate);
			jCalendarDateDialog.setMinimumWidth(350);
			jCalendarDateDialog.setMinimumHeight(170);
			jCalendarDateDialog.setMaximumWidth(500);
			jCalendarDateDialog.setMaximumHeight(400);
	
			// Adds a listener for get the date when the 
			jCalendarDateDialog.addComponentListener(new ComponentAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
				 */
				public void componentHidden(ComponentEvent e) {
					lastSelectedDate = jCalendarDateDialog.getDate();
					
					actionListener.actionPerformed(new ActionEvent(btnDate, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_ID, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND));

				}			
			});
		
		}
		
		return jCalendarDateDialog;
	}

	/**
	 * This method initializes btnAnd
	 *
	 * @return javax.swing.JButton
	 */	
	private javax.swing.JButton getBtnAnd() {
		if (btnAnd == null) {
			btnAnd = new JButtonML();
			btnAnd.setText("And");
			btnAnd.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnAnd.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnAnd.setToolTipText(Messages.getText("operator_and_explanation"));
			map.put("And", Integer.toString(FilterButtonsJPanel.AND));
			
			btnAnd.addActionListener(this.getActionListener());
		}

		return btnAnd;
	}

	/**
	 * This method initializes btnNot
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnNot() {
		if (btnNot == null) {
			btnNot = new JButtonML();
			btnNot.setText("Not");
			btnNot.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnNot.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnNot.setToolTipText(Messages.getText("operator_not_explanation"));
			map.put("Not", Integer.toString(FilterButtonsJPanel.NOT));
			
			btnNot.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					new ActionEvent(this, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_ID, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND);
				}				
			});
			
			btnNot.addActionListener(this.getActionListener());

		}

		return btnNot;
	}

	/**
	 * This method initializes btnOr
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnOr() {
		if (btnOr == null) {
			btnOr = new JButtonML();
			btnOr.setText("Or");
			btnOr.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnOr.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnOr.setToolTipText(Messages.getText("operator_or_explanation"));
			map.put("Or", Integer.toString(FilterButtonsJPanel.OR));
			
			btnOr.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					new ActionEvent(this, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_ID, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND);
				}				
			});
			
			btnOr.addActionListener(this.getActionListener());
		}

		return btnOr;
	}

	/**
	 * This method initializes btnParenthesis
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnParenthesis() {
		if (btnParenthesis == null) {
			btnParenthesis = new JButtonML();
			btnParenthesis.setText("()");
			btnParenthesis.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnParenthesis.setPreferredSize(new java.awt.Dimension(buttonWidthUnit, buttonHeight));
			btnParenthesis.setToolTipText(Messages.getText("parenthesis_explanation"));
			map.put("()", Integer.toString(FilterButtonsJPanel.PARENTHESIS));
			
			btnParenthesis.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					new ActionEvent(this, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_ID, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND);
				}				
			});
			
			btnParenthesis.addActionListener(this.getActionListener());
		}

		return btnParenthesis;
	}
	
	/**
	 * This method initializes btnDeleteText
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBtnDeleteText() {
		if (btnDeleteText == null) {
			btnDeleteText = new JButtonML();
			btnDeleteText.setText(Messages.getText("deleteText"));
			btnDeleteText.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnDeleteText.setPreferredSize(new java.awt.Dimension(176, buttonHeight));
			btnDeleteText.setToolTipText(Messages.getText("deleteText_on_filter_use_explanation"));
			map.put(Messages.getText("deleteText"), Integer.toString(FilterButtonsJPanel.DELETE_TEXT));
			
			btnDeleteText.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					new ActionEvent(this, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_ID, FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND);
				}				
			});
			
			btnDeleteText.addActionListener(this.getActionListener());
		}
			
		return btnDeleteText;	
	}
	
	/**
	 * This method initializes the "actionListener" ActionListener
	 * 
	 * @return ActionListener
	 */
	private ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent event) {
				   	// Notifies that has been clicked a button
		       		changes.firePropertyChange(FilterButtonsJPanel.BUTTON_CLICKED_ACTION_COMMAND, FilterButtonsJPanel.DEFAULT, map.get( ((javax.swing.JButton)event.getSource()).getText()));
		        }
		    };
		}
		return actionListener;
	}
	
	/**
	 * Returns the las selected date, formatted
	 * 
	 * @return A formatted date
	 */
	public String getLastSelectedDate() {
		if (lastSelectedDate == null)
			return "";
		else
			return "Date(" + dateFormat.format(lastSelectedDate) + ")"; 
	}
	
	/**
	 * Returns the 'DateFormat' private attribute
	 * 
	 * @return A 'DateFormat' reference
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

    /**
     * Adds a "Property Change Listener"
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
    	changes.addPropertyChangeListener(l);
    }

    /**
     * Removes a "Property Change Listener"
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
    	changes.removePropertyChangeListener(l);
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

	/**
	 * JPanel with multi line tool tip text.
	 * 
	 * @see JPanel
	 * 
	 * @author Pablo Piqueras Bartolome (pablo.piqueras@iver.es)
	 */
    protected class JPanelML extends JPanel {
		private static final long serialVersionUID = -5282313934096892711L;

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
}
