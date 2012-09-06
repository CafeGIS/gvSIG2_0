package org.gvsig.gui.beans.swing.textBoxWithCalendar.jCalendarUsingSamples;


//**********************************************************************
// Import list
//**********************************************************************

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 * This example shows various instances of the JCalendarCombo class.
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

class Example2 extends JFrame {
	private static final long serialVersionUID = 1804829593564929253L;

	// **********************************************************************
	// main
	// **********************************************************************

	public static void main(String[] args) {
		new Example2();
	}

// **********************************************************************
// Constructors
//**********************************************************************

/**
 * Create various instances of a JCalendarCombo.
 */

public
Example2()
{
    // Set up the frame

    setTitle("Example2");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    Container contentPane = getContentPane();
    contentPane.setLayout(new GridLayout(2, 2, 5, 5));

    // Create a date listener to be used for all calendars

    MyDateListener listener = new MyDateListener();

    // Display date and time using the default calendar and locale.
    // Display today's date at the bottom. Allow editing

    JCalendarCombo calendar1 =
	new JCalendarCombo(
	    JCalendarCombo.DISPLAY_DATE | JCalendarCombo.DISPLAY_TIME,
	    true);
    calendar1.setEditable(true);
    calendar1.addDateListener(listener);

    // Set fonts rather than using defaults

    calendar1.setFont(new Font("DialogInput", Font.PLAIN, 16));

    calendar1.setTitleFont(new Font("Serif", Font.BOLD|Font.ITALIC, 24));
    calendar1.setDayOfWeekFont(new Font("SansSerif", Font.ITALIC, 12));
    calendar1.setDayFont(new Font("SansSerif", Font.BOLD, 16));
    calendar1.setTimeFont(new Font("DialogInput", Font.PLAIN, 10));
    calendar1.setTodayFont(new Font("Dialog", Font.PLAIN, 14));

    // Display date only

    JCalendarCombo calendar2 =
	new JCalendarCombo(JCalendarCombo.DISPLAY_DATE, false);
    calendar2.addDateListener(listener);

    // Display time only and set the time pattern to use as a duration
    // from 00:00 to 23:59

    JCalendarCombo calendar3 =
	new JCalendarCombo(
	    Calendar.getInstance(),
	    Locale.getDefault(),
	    JCalendarCombo.DISPLAY_TIME,
	    false,
	    "HH:mm:ss");
    calendar3.setDateFormat(new SimpleDateFormat("HH:mm:ss"));
    calendar3.addDateListener(listener);

    // Display a French calendar

    JCalendarCombo calendar4 =
	new JCalendarCombo(
	    Calendar.getInstance(Locale.FRENCH),
	    Locale.FRENCH,
	    JCalendarCombo.DISPLAY_DATE | JCalendarCombo.DISPLAY_TIME,
	    false);
    calendar4.addDateListener(listener);

    // Add all the calendars to the content pane

    JPanel panel1 = new JPanel(new BorderLayout());
    panel1.add(calendar1, BorderLayout.NORTH);
    contentPane.add(panel1);

    JPanel panel2 = new JPanel(new BorderLayout());
    panel2.add(calendar2, BorderLayout.NORTH);
    contentPane.add(panel2);

    JPanel panel3 = new JPanel(new BorderLayout());
    panel3.add(calendar3, BorderLayout.NORTH);
    contentPane.add(panel3);

    JPanel panel4 = new JPanel(new BorderLayout());
    panel4.add(calendar4, BorderLayout.NORTH);
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
