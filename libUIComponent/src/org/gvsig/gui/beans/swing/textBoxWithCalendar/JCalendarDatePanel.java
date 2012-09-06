package org.gvsig.gui.beans.swing.textBoxWithCalendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.controls.MultiLineToolTip;

/**
 * Creates a Panel for include in other panels -> this panel allows users to set the date they want
 * The difference from this class to the JCalendarDatePanel is that in this class the user can move
 *    the calendar with the mouse
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class JCalendarDatePanel extends JPanel implements IMethodsForGraphicalCalendarComponents, Serializable{
	private static final long serialVersionUID = -5439270916098423256L;

	private JButton jButton = null;
	private JCalendarDateDialog jDialogCalendar = null;
	private final int defaultWidth = 120;
	private final int defaultHeight = 19;
	private MouseListener mouseListener;	

	/**
	 * Default Constructor
	 */
	public JCalendarDatePanel() {
	
		try
		{
			this.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
			initialize();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This is the default constructor with 2 parameters for set the size of the inner button
	 */
	public JCalendarDatePanel(int width, int height) {
			
		try
		{			
			this.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
			initialize();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes this 
	 */
	private void initialize() {
		// Set properties to the current panel
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 0;		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		
		// Add components to this panel:
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.gridx = 0;

		// Add the JButton
		this.add(getJButton(), gridBagConstraints1);
		
		// Defines the mouseLIstener
		this.createMouseListener();
		
		// Adds the mouseListener to the jTextField and the jButton
		jButton.addMouseListener(mouseListener);
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
			jButton.setHorizontalTextPosition(SwingConstants.LEFT);
			jButton.setBackground(Color.WHITE);
			jButton.setText(this.getFormattedDate(this.getJCalendarDateDialog().getDate()));
		}
		return jButton;
	}
	
	/**
	 * This method initializes jDialogCalendar	
	 * 	
	 * @return javax.swing.JDialog	
	 */
	public JCalendarDateDialog getJCalendarDateDialog() {
		if (jDialogCalendar == null) {
			jDialogCalendar = new JCalendarDateDialog();
			jDialogCalendar.addComponentListener(new ComponentAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
				 */
				public void componentHidden(ComponentEvent e) {
					jButton.setText(getFormattedDate(jDialogCalendar.getDate()));
				}
			});
		}
		return jDialogCalendar;
	}
	
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getDate()
	 */
	public Date getDate() {		
		return this.getJCalendarDateDialog().getDate();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#setDate(java.util.Date)
	 */
	public void setDate(Date date)
	{
		this.getJCalendarDateDialog().setDate(date);
	}
	
	/**
	 * Allows select the date
	 */
	public void enableCalendar() {
		jButton.setEnabled(true);
	}
	
	/**
	 * Don't allow select the date
	 */
	public void disableCalendar() {
		jButton.setEnabled(false);
	}

	/**
	 * Returns the date formatted
	 * 
	 * @param Date
	 * @return String The formatted date
	 */
	private String getFormattedDate(Date d) {
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getFormattedDate()
	 */
	public String getFormattedDate() {
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.getDate());
	}
	
	/**
	 * Gets the title of the JDialog with the calendar component. The title is displayed in the JDialog's border.
	 * @return Title
	 */
	public String getTitle() {
		return this.getJCalendarDateDialog().getTitle();
	}
		
	/**
	 * Sets the title of the JDialog with the calendar component. 
	 * 
	 * @param String
	 */
	public void setTitle(String title) {
		this.getJCalendarDateDialog().setTitle(title);
	}

	/**
	 * Sets the default title of the JDialog with the calendar component.
	 */
	public void setDefaultTitle() {
		this.getJCalendarDateDialog().setTitle(Messages.getText("calendarTitle"));
	}	
	
	/**
	 * @see java.awt.Dialog#setModal(boolean)
	 */
	public void setModal(boolean b) {
		this.getJCalendarDateDialog().setModal(b);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.JComponent#setPreferredSize(java.awt.Dimension)
	 */
	public void setPreferredSize(Dimension d) {
		super.setPreferredSize(d);
		getJButton().setPreferredSize(d);
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		super.setSize(width, height);
		getJButton().setSize(width, height);		
	}
	
	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension d) {
		super.setSize(d);
		getJButton().setSize(d);		
	}
	
	/**
	 * Adds a component listener for the inner JDialog that cointins the calendar interface
	 * 
	 * @param componentListener
	 */
	public void addComponentListenerForJDialogCalendar(ComponentListener componentListener) {
		getJCalendarDateDialog().addComponentListener(componentListener);
	}	
	
	/**
	 * This method creates a Mouse Listener object
	 */
	private void createMouseListener() {
		
		mouseListener = new MouseAdapter() {

			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
				// Show the JDialog
				if (getJButton().isEnabled())
				{
					getJCalendarDateDialog().setLocationRelativeTo(jButton);
					getJCalendarDateDialog().setVisible(true);
				}				
			}	
		};
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
