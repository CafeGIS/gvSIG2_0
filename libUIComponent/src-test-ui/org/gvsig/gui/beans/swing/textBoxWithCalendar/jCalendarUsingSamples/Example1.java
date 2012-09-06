//**********************************************************************
// Package
//**********************************************************************


//**********************************************************************
// Import list
//**********************************************************************
package org.gvsig.gui.beans.swing.textBoxWithCalendar.jCalendarUsingSamples;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendar;

/**
 * This example shows various instances of the JCalendar class.
 * <hr>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Artistic License. You should have
 * received a copy of the Artistic License along with this program. If
 * not, a copy is available at
 * <a href="http://opensource.org/licenses/artistic-license.php">
 * opensource.org</a>.
 *
 * @author Antonio Freixas
 */

// Copyright © 2004 Antonio Freixas
// All Rights Reserved.

public class Example1 extends JFrame {
	private static final long serialVersionUID = -2655722904436980134L;

	// **********************************************************************
	// main
	// **********************************************************************

	public static void main(String[] args) {
		new Example1();
	}

// **********************************************************************
// Constructors
//**********************************************************************

/**
 * Create various instances of a JCalendar.
 */

public
Example1()
{
    // Set up the frame

    setTitle("Example1");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(2, 2, 5, 5));

    // Create a border for all calendars

    Border etchedBorder =
	BorderFactory.createEtchedBorder();
    Border emptyBorder =
	BorderFactory.createEmptyBorder(10, 10, 10, 10);
    Border compoundBorder =
	BorderFactory.createCompoundBorder(etchedBorder, emptyBorder);

    // Create a date listener to be used for all calendars

    MyDateListener listener = new MyDateListener();

    // Display date and time using the default calendar and locale.
    // Display today's date at the bottom.

    JCalendar calendar1 =
	new JCalendar(
	    JCalendar.DISPLAY_DATE | JCalendar.DISPLAY_TIME,
	    true);
    calendar1.addDateListener(listener);
    calendar1.setBorder(compoundBorder);

    // Set fonts rather than using defaults

    calendar1.setTitleFont(new Font("Serif", Font.BOLD|Font.ITALIC, 24));
    calendar1.setDayOfWeekFont(new Font("SansSerif", Font.ITALIC, 12));
    calendar1.setDayFont(new Font("SansSerif", Font.BOLD, 16));
    calendar1.setTimeFont(new Font("DialogInput", Font.PLAIN, 10));
    calendar1.setTodayFont(new Font("Dialog", Font.PLAIN, 14));

    // Display date only

    JCalendar calendar2 = new JCalendar(JCalendar.DISPLAY_DATE, false);
    calendar2.addDateListener(listener);
    calendar2.setBorder(compoundBorder);

    // Display time only and set the time pattern to use as a duration
    // from 00:00 to 23:59

    JCalendar calendar3 =
	new JCalendar(
	    Calendar.getInstance(),
	    Locale.getDefault(),
	    JCalendar.DISPLAY_TIME,
	    false,
	    "HH:mm");
    calendar3.addDateListener(listener);
    calendar3.setBorder(compoundBorder);

    // Display a French calendar

    JCalendar calendar4 =
	new JCalendar(
	    Calendar.getInstance(Locale.FRENCH),
	    Locale.FRENCH,
	    JCalendar.DISPLAY_DATE | JCalendar.DISPLAY_TIME,
	    false);
    calendar4.addDateListener(listener);
    calendar4.setBorder(compoundBorder);

    // Add all the calendars to the content pane

    JPanel panel1 = new JPanel(new FlowLayout());
    panel1.add(calendar1);
    contentPane.add(panel1);

    JPanel panel2 = new JPanel(new FlowLayout());
    panel2.add(calendar2);
    contentPane.add(panel2);

    JPanel panel3 = new JPanel(new FlowLayout());
    panel3.add(calendar3);
    contentPane.add(panel3);

    JPanel panel4 = new JPanel(new FlowLayout());
    panel4.add(calendar4);
    contentPane.add(panel4);

    // Make the window visible

    pack();
    setVisible(true);
}

//**********************************************************************
// Inner Classes
//**********************************************************************

private class MyDateListener
      implements DateListener
{

public void
dateChanged(
    DateEvent e)
{
    Calendar c = e.getSelectedDate();
    if (c != null) {
	System.out.println(c.getTime());
    }
    else {
	System.out.println("No time selected.");
    }
}

}

//**********************************************************************
// End Inner Classes
//**********************************************************************

}