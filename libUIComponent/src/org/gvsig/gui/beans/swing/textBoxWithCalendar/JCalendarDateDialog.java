package org.gvsig.gui.beans.swing.textBoxWithCalendar;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JDialog;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendar;
import org.gvsig.gui.beans.Messages;

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
 * Creates a Dialog that allows users to select the date they want
 * This class is a version of JCalendarDatePanel
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class JCalendarDateDialog extends JDialog implements IMethodsForGraphicalCalendarComponents, Serializable {
	private static final long serialVersionUID = -4265734997797240482L;
	private JCalendar jCalendar = null;
	private final int defaultWidth = 350;
	private final int defaultHeight = 230;
	private Dimension lastDimensionOfJDialog;
	private Dimension minDimensionOfJDialog;
	private Dimension maxDimensionOfJDialog;

	/**
	 * Default Constructor
	 */
	public JCalendarDateDialog() {
		super();
		
		this.initialize();
	}

	/**
	 * This is the default constructor with 2 parameters for set the size
	 */
	public JCalendarDateDialog(int width, int height) {
		super();

		this.initialize();			
			
		this.lastDimensionOfJDialog.width = width;
		this.lastDimensionOfJDialog.height = height;
	}
	
	/**
	 * This method initializes this 
	 */
	private void initialize() {
		this.setDefaultTitle();
				
		// Initialize the attribute 'lastDimensionOfJCalendar'
		this.lastDimensionOfJDialog = new Dimension(this.defaultWidth, this.defaultHeight);
		
		// Resize this component to its initial size
		this.setSizeResize(defaultWidth, defaultHeight);
		
		// Adds the jCalendar to this dialog
		this.getContentPane().add(this.getJCalendar());

		// By default there isn't maximun neither minimum dimensions
		this.maxDimensionOfJDialog = new Dimension(-1, -1);
		this.minDimensionOfJDialog = new Dimension(-1, -1);		
		
		// This allows authomatic revalidation while this component is been resized
		Toolkit.getDefaultToolkit().setDynamicLayout(true);

		// Listener for the redimension of this component:
		// Dimension must be between the minimum and maximum
		this.addComponentListener(new ComponentAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
			 */
			public void componentResized(ComponentEvent e) {
				boolean modified = false;
				int width = getSize().width;
				int height = getSize().height;
				int new_width = 0;
				int new_height = 0;
				
				// If there are a minimum dimension or a maximum dimension, check the current
				//   dimension and resize if it's nessecary

				// If this component hasn't been initialed yet
				if ((height != 0) && (width != 0)) {
					if (minDimensionOfJDialog.height != -1) {
						if (height < minDimensionOfJDialog.height) {
							new_width = width;
							new_height = minDimensionOfJDialog.height;
							modified = true;
						}
					}
					
					if (minDimensionOfJDialog.width != -1) {
						if (width < minDimensionOfJDialog.width) {
							new_width = minDimensionOfJDialog.width;
							new_height = height;
							modified = true;
						}
					}
	
					if (maxDimensionOfJDialog.height != -1) {
						if (height > maxDimensionOfJDialog.height) {
							new_width = width;
							new_height = maxDimensionOfJDialog.height;
							modified = true;
						}
					}
	
					if (maxDimensionOfJDialog.width != -1) {
						if (width > maxDimensionOfJDialog.width) {
							new_width = maxDimensionOfJDialog.width;
							new_height = height;
							modified = true;
						}
					}
					
					if (modified) {
						setResizable(false);
						setSize(new_width, new_height);
						setResizable(true);
						getJCalendar().revalidate();
					}
					
					lastDimensionOfJDialog = getSize();
				}
			}
		});
	}

	/**
	 * Sets the initial size of this panel
	 * 
	 * @param width (the new Width for the panel)
	 * @param height (the new Height for the panel)
	 */
	public void resizeToInitialSize()
	{
		getContentPane().setSize(this.defaultWidth, this.defaultHeight);
		getJCalendar().revalidate();
	}

	/**
	 * Sets the maximum width for this component, according a percentage of the width resolution of the screen
	 * (If the parameter isn't > 0.0 neither -1.0 neither <= 1.0, this method doesn't apply the changes)
	 * 
	 * @param max_width_screen_percentage A float number > 0, or -1 if there is no limit
	 */
	public void setMaximumWidthScreenResolutionPercentage(double max_width_screen_percentage) {
		int max_width = (int) Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().width * max_width_screen_percentage);
		
		if ((max_width > 0) || (max_width == -1)) {
			this.maxDimensionOfJDialog.width = max_width;
		}
	}
	
	/**
	 * Sets the minimum width for this component, according a percentage of the width resolution of the screen
	 * (If the parameter isn't > 0.0 neither -1.0 neither <= 1.0, this method doesn't apply the changes)
	 * 
	 * @param min_width_screen_percentage A float number > 0.0 neither -1.0 neither <= 1.0, or -1 if there is no limit
	 */
	public void setMinimumWidthScreenResolutionPercentage(double min_width_screen_percentage) {
		int min_width = (int) Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().width * min_width_screen_percentage);
		
		if ((min_width > 0) || (min_width == -1))
			this.minDimensionOfJDialog.width = min_width;
	}

	/**
	 * Sets the maximum height for this component, according a percentage of the height resolution of the screen
	 * (If the parameter isn't > 0.0 neither -1.0 neither <= 1.0, this method doesn't apply the changes)
	 * 
	 * @param max_width_screen_percentage A float number > 0.0 neither -1.0 neither <= 1.0, or -1 if there is no limit
	 */
	public void setMaximumHeightScreenResolutionPercentage(double max_height_screen_percentage) {
		if (((max_height_screen_percentage > 0.0) && (max_height_screen_percentage <= 1.0)) || (max_height_screen_percentage == -1.0)) {
			int max_height = (int) Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().height * max_height_screen_percentage);
			this.maxDimensionOfJDialog.height = max_height;
		}
	}
	
	/**
	 * Sets the minimum height for this component, according a percentage of the height resolution of the screen
	 * (If the parameter isn't > 0.0 neither -1.0 neither <= 1.0, this method doesn't apply the changes)
	 * 
	 * @param min_width_screen_percentage A float number > 0.0 neither -1.0 neither <= 1.0, or -1 if there is no limit
	 */
	public void setMinimumHeightScreenResolutionPercentage(double min_height_screen_percentage) {
		if (((min_height_screen_percentage > 0.0) && (min_height_screen_percentage <= 1.0)) || (min_height_screen_percentage == -1.0)) {
			int min_height = (int) Math.ceil(Toolkit.getDefaultToolkit().getScreenSize().height * min_height_screen_percentage);
			this.minDimensionOfJDialog.height = min_height;
		}
	}
	
	/**
	 * Sets the maximum width for this component
	 * (If the parameter isn't > 0 neither -1, this method doesn't apply the changes)
	 * 
	 * @param max_width A natural number > 0, or -1 if there is no limit
	 */
	public void setMaximumWidth(int max_width) {
		if ((max_width > 0) || (max_width == -1)) {
			this.maxDimensionOfJDialog.width = max_width;
		}
	}
	
	/**
	 * Sets the minimum width for this component
	 *  (If the parameter isn't > 0 neither -1, this method doesn't apply the changes)
	 * 
	 * @param min_width A natural number > 0, or -1 if there is no limit
	 */
	public void setMinimumWidth(int min_width) {
		if ((min_width > 0) || (min_width == -1))
			this.minDimensionOfJDialog.width = min_width;
	}

	/**
	 * Sets the maximum height for this component
	 * (If the parameter isn't > 0 neither -1, this method doesn't apply the changes)
	 * 
	 * @param max_width A natural number > 0, or -1 if there is no limit
	 */
	public void setMaximumHeight(int max_height) {
		if ((max_height > 0) || (max_height == -1))
			this.maxDimensionOfJDialog.height = max_height;
	}
	
	/**
	 * Sets the minimum height for this component
	 *  (If the parameter isn't > 0 neither -1, this method doesn't apply the changes)
	 * 
	 * @param min_width A natural number > 0, or -1 if there is no limit
	 */
	public void setMinimumHeight(int min_height) {
		if ((min_height > 0) || (min_height == -1))
			this.minDimensionOfJDialog.height = min_height;
	}

	/**
	 * Sets the size of this panel
	 * 
	 * @param width (the new Width for the panel)
	 * @param height (the new Height for the panel)
	 */
	public void setSizeResize(int width, int height) {
		this.setSize(new Dimension(width, height));
		this.lastDimensionOfJDialog = this.getSize();
		getJCalendar().revalidate();
	}
	
	/**
	 * Get the height of this panel
	 */
	public int getHeight()
	{
		return this.lastDimensionOfJDialog.height;
	}
	
	/**
	 * Get the width of this panel
	 */
	public int getWidth()
	{
		return this.lastDimensionOfJDialog.width;
	}
	
	/**
	 * This method initializes jCalendar	
	 * 	
	 * @return javax.swing.JCalendar	
	 */
	private JCalendar getJCalendar() {
		if (jCalendar == null) {
			jCalendar = new JCalendar();
			jCalendar.setToolTipText(Messages.getText("calendarSelectDate"));

			try
			{
				jCalendar.setToolTipTextToMonthDecrButton(Messages.getText("calendarBackOneMonth"));
				jCalendar.setToolTipTextToMonthIncrButton(Messages.getText("calendarForwardOneMonth"));
				jCalendar.setToolTipTextToYearDecrButton(Messages.getText("calendarBackOneYear"));
				jCalendar.setToolTipTextToYearIncrButton(Messages.getText("calendarForwardOneYear"));

				// Adds a date listener calendar
				jCalendar.addDateListener(new DateListener(){
					/*
					 *  (non-Javadoc)
					 * @see org.freixas.jcalendar.DateListener#dateChanged(org.freixas.jcalendar.DateEvent)
					 */
					public void dateChanged(DateEvent arg0) {
						setVisible(false);
					}				
				});
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return jCalendar;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getDate()
	 */	
	public Date getDate() {		
		return jCalendar.getDate();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#setDate(java.util.Date)
	 */
	public void setDate(Date date)
	{
		jCalendar.setDate(date);
	}

//	/**
//	 * Returns the date formatted
//	 * 
//	 * @param Date
//	 * @return String The formatted date
//	 */
//	private String getFormattedDate(Date d) {
//		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(d);
//	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getFormattedDate()
	 */
	public String getFormattedDate() {
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.getDate());
	}

	/**
	 * Gets the title of the JDialog with the calendar component. The title is displayed in the JDialog's border.
	 *
	 * @return Title
	 */
	public String getTitle() {
		return super.getTitle();
	}
	
	/**
	 * Sets the title of the JDialog with the calendar component. 
	 * 
	 * @param String
	 */
	public void setTitle(String title) {
		super.setTitle(title);
	}

	/**
	 * Sets the default title of the JDialog with the calendar component.
	 */
	public void setDefaultTitle() {
		this.setTitle(Messages.getText("calendarTitle"));
	}
	
	/**
	 * @see java.awt.Dialog#setModal(boolean)
	 */
	public void setModal(boolean b) {
		super.setModal(b);
	}
}
