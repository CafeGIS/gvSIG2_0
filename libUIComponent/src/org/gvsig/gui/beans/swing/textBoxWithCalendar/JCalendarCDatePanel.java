package org.gvsig.gui.beans.swing.textBoxWithCalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JPanel;
import javax.swing.JToolTip;

import org.freixas.jcalendar.JCalendarCombo;
import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.controls.MultiLineToolTip;

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
 * Creates a Panel for include in other panels -> this panel allows users to set the date they want
 * The difference from this class to the JCalendarDatePanel is that in this class the user can move
 *    the calendar with the mouse
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class JCalendarCDatePanel extends JPanel implements IMethodsForGraphicalCalendarComponents, java.io.Serializable {
	private static final long serialVersionUID = -1698696825218186886L;

	private final int defaultWidth = 120;
	private final int defaultHeight = 19;
	private JCalendarCombo calendar = null;
	
	/**
	 * This is the default constructor: creates the panel with a JCalendar object
	 */
	public JCalendarCDatePanel() {
			
		try
		{
			// Set properties to the current panel
			this.setPreferredSize(new Dimension(defaultWidth, defaultHeight));
			this.setLayout(new BorderLayout());
			
		    // Adds the JCalendar to the current panel
		    this.add(this.getJCalendarCombo());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the default constructor with 2 parameters: creates the panel with a JCalendar object
	 */
	public JCalendarCDatePanel(int width, int height) {
			
		try {
			
			// Set properties to the current panel
			this.setPreferredSize(new Dimension(width, height));
			this.setLayout(new BorderLayout());			
		    
		    // Adds the JCalendar to the current panel
		    this.add(this.getJCalendarCombo());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns a reference to the JCalendarCombo private attribute
	 * 
	 * @return A reference to the calendar
	 */
	private JCalendarCombo getJCalendarCombo() {
		if (this.calendar == null) {
			// Create a JCalendar
			calendar = new JCalendarCombo(Calendar.getInstance(), Locale.getDefault(), JCalendarCombo.DISPLAY_DATE, true);
			calendar.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		    calendar.setEditable(false);		    
		    calendar.setBackground(Color.WHITE);
		    calendar.setToolTipText(Messages.getText("calendarSelectDate"));
		    calendar.setToolTipTextToMonthDecrButton(Messages.getText("calendarBackOneMonth"));
		    calendar.setToolTipTextToMonthIncrButton(Messages.getText("calendarForwardOneMonth"));
		    calendar.setToolTipTextToYearDecrButton(Messages.getText("calendarBackOneYear"));
		    calendar.setToolTipTextToYearIncrButton(Messages.getText("calendarForwardOneYear"));
		}
		
		return this.calendar;
	}
	
	/**
	 * Sets the size of the CalendarDatePanel for resize the JCalendarCommbo and this panel
	 * 
	 * @param width (the new Width for the panel)
	 * @param height (the new Height for the panel)
	 */
	public void setPreferredSizeResize(int width, int height)
	{
		this.setPreferredSize(new Dimension(width, height));
		calendar.setPreferredSize(new Dimension(width, height));		   
		calendar.setSize(new Dimension(width, height));
		this.revalidate();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#setDate(java.util.Date)
	 */
	public void setDate(Date date)
	{
		calendar.setDate(date);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getDate()
	 */
	public Date getDate()
	{
		return calendar.getDate();
	}
	
	/**
	 * Allows select the date
	 */
	public void enableCalendar()
	{
		calendar.setEnabled(true);
	}
	
	/**
	 * Don't allow select the date
	 */
	public void disableCalendar()
	{
		calendar.setEnabled(false);
	}	
	
	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.gui.beans.swing.textBoxWithCalendar.IMethodsForGraphicalCalendarComponents#getFormattedDate()
	 */
	public String getFormattedDate() {
		return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(this.getDate());
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
