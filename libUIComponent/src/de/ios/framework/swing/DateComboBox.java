package de.ios.framework.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxUI;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;
import com.sun.java.swing.plaf.windows.WindowsComboBoxUI;

/**
 * @version 1.0 11/02/2000
 */

//////////////////////////////////////////////////////////////

public class DateComboBox extends JComboBox {
  private static final long serialVersionUID = 1L;

static java.util.ResourceBundle resource = java.util.ResourceBundle.getBundle("resources.Traduction")/*#BundleType=List*/;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
    public void setDateFormat(SimpleDateFormat dateFormat) {
	this.dateFormat = dateFormat;
    }
    public void setSelectedItem(Object item) {
	// Could put extra logic here or in renderer when item is instanceof Date, Calendar, or String
	// Dont keep a list ... just the currently selected item
	removeAllItems(); // hides the popup if visible
	addItem(item);
	super.setSelectedItem(item);
    }

    public void updateUI() {
	ComboBoxUI cui = (ComboBoxUI) UIManager.getUI(this);
	if (cui instanceof MetalComboBoxUI) {
	    cui = new MetalDateComboBoxUI();
	} else if (cui instanceof MotifComboBoxUI) {
	    cui = new MotifDateComboBoxUI();
	} else if (cui instanceof WindowsComboBoxUI) {
	    cui = new WindowsDateComboBoxUI();
	}
        setUI(cui);
    }

    // Inner classes are used purely to keep DateComboBox component in one file
    //////////////////////////////////////////////////////////////
    // UI Inner classes -- one for each supported Look and Feel
    //////////////////////////////////////////////////////////////

    class MetalDateComboBoxUI extends MetalComboBoxUI {
	protected ComboPopup createPopup() {
	    return new DatePopup( comboBox );
	}
    }

    class WindowsDateComboBoxUI extends WindowsComboBoxUI {
	protected ComboPopup createPopup() {
	    return new DatePopup( comboBox );
	}
    }

    class MotifDateComboBoxUI extends MotifComboBoxUI {
      private static final long serialVersionUID = 5669807256653124411L;

	protected ComboPopup createPopup() {
	    return new DatePopup( comboBox );
	}
    }

    //////////////////////////////////////////////////////////////
    // DatePopup inner class
    //////////////////////////////////////////////////////////////

    class DatePopup implements ComboPopup, MouseMotionListener,
			       MouseListener, KeyListener, PopupMenuListener {

	protected JComboBox comboBox;
	protected Calendar calendar;
	protected JPopupMenu popup;
	protected JLabel monthLabel;
	protected JPanel days = null;
	protected SimpleDateFormat monthFormat = new SimpleDateFormat("MMM yyyy");

	protected Color selectedBackground;
	protected Color selectedForeground;
	protected Color background;
	protected Color foreground;

	public DatePopup(JComboBox comboBox) {
	    this.comboBox = comboBox;
	    calendar = Calendar.getInstance();
	    // check Look and Feel
	    background = UIManager.getColor("ComboBox.background");
	    foreground = UIManager.getColor("ComboBox.foreground");
	    selectedBackground = UIManager.getColor("ComboBox.selectionBackground");
	    selectedForeground = UIManager.getColor("ComboBox.selectionForeground");

	    initializePopup();
	}

	//========================================
	// begin ComboPopup method implementations
	//
        public void show() {
	    try {
		// if setSelectedItem() was called with a valid date, adjust the calendar
		calendar.setTime( dateFormat.parse( comboBox.getSelectedItem().toString() ) );
	    } catch (Exception e) {}
	    updatePopup();
	    popup.show(comboBox, 0, comboBox.getHeight());
        }

	public void hide() {
	    popup.setVisible(false);
	}

	protected JList list = new JList();
	public JList getList() {
	    return list;
	}

	public MouseListener getMouseListener() {
	    return this;
	}

	public MouseMotionListener getMouseMotionListener() {
	    return this;
	}

	public KeyListener getKeyListener() {
	    return this;
	}

	public boolean isVisible() {
	    return popup.isVisible();
	}

	public void uninstallingUI() {
	    popup.removePopupMenuListener(this);
	}

	//
	// end ComboPopup method implementations
	//======================================



	//===================================================================
	// begin Event Listeners
	//

	// MouseListener

	public void mousePressed( MouseEvent e ) {}
        public void mouseReleased( MouseEvent e ) {}
	// something else registered for MousePressed
	public void mouseClicked(MouseEvent e) {
            if ( !SwingUtilities.isLeftMouseButton(e) )
                return;
            if ( !comboBox.isEnabled() )
                return;
	    if ( comboBox.isEditable() ) {
		comboBox.getEditor().getEditorComponent().requestFocus();
	    } else {
		comboBox.requestFocus();
	    }
	    togglePopup();
	}

	protected boolean mouseInside = false;
	public void mouseEntered(MouseEvent e) {
	    mouseInside = true;
	}
	public void mouseExited(MouseEvent e) {
	    mouseInside = false;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}

	// KeyListener
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	public void keyReleased( KeyEvent e ) {
	    if ( e.getKeyCode() == KeyEvent.VK_SPACE ||
		 e.getKeyCode() == KeyEvent.VK_ENTER ) {
		togglePopup();
	    }
	}

	/**
	 * Variables hideNext and mouseInside are used to
	 * hide the popupMenu by clicking the mouse in the JComboBox
	 */
	public void popupMenuCanceled(PopupMenuEvent e) {}
	protected boolean hideNext = false;
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	    hideNext = mouseInside;
	}
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

	//
	// end Event Listeners
	//=================================================================

	//===================================================================
	// begin Utility methods
	//

	protected void togglePopup() {
	    if ( isVisible() || hideNext ) {
		hide();
	    } else {
		show();
	    }
	    hideNext = false;
	}

	//
	// end Utility methods
	//=================================================================

	// Note *** did not use JButton because Popup closes when pressed
	protected JLabel createUpdateButton(final int field, final int amount) {
	    final JLabel label = new JLabel();
	    final Border selectedBorder = new EtchedBorder();
	    final Border unselectedBorder = new EmptyBorder(selectedBorder.getBorderInsets(new JLabel()));
	    label.setBorder(unselectedBorder);
	    label.setForeground(foreground);
	    label.addMouseListener(new MouseAdapter() {
		    public void mouseReleased(MouseEvent e) {
			calendar.add(field, amount);
			updatePopup();
		    }
		    public void mouseEntered(MouseEvent e) {
			label.setBorder(selectedBorder);
		    }
		    public void mouseExited(MouseEvent e) {
			label.setBorder(unselectedBorder);
		    }
		});
	    return label;
	}


	protected void initializePopup() {
	    JPanel header = new JPanel(); // used Box, but it wasn't Opaque
	    header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
	    header.setBackground(background);
	    header.setOpaque(true);

	    JLabel label;
	    label = createUpdateButton(Calendar.YEAR, -1);
	    label.setText("<<");
	    label.setToolTipText(resource.getString("datecombobox.prevyear"));

	    header.add(Box.createHorizontalStrut(12));
	    header.add(label);
	    header.add(Box.createHorizontalStrut(12));

	    label = createUpdateButton(Calendar.MONTH, -1);
	    label.setText("<");
	    label.setToolTipText(resource.getString("datecombobox.prevmonth"));
	    header.add(label);

	    monthLabel = new JLabel("", JLabel.CENTER);
	    monthLabel.setForeground(foreground);
	    header.add(Box.createHorizontalGlue());
	    header.add(monthLabel);
	    header.add(Box.createHorizontalGlue());

	    label = createUpdateButton(Calendar.MONTH, 1);
	    label.setText(">");
	    label.setToolTipText(resource.getString("datecombobox.nextmonth"));
	    header.add(label);

	    label = createUpdateButton(Calendar.YEAR, 1);
	    label.setText(">>");
	    label.setToolTipText(resource.getString("datecombobox.nextyear"));

	    header.add(Box.createHorizontalStrut(12));
	    header.add(label);
	    header.add(Box.createHorizontalStrut(12));

	    popup = new JPopupMenu();
	    popup.setBorder(BorderFactory.createLineBorder(Color.black));
	    popup.setLayout(new BorderLayout());
	    popup.setBackground(background);
	    popup.addPopupMenuListener(this);
	    popup.add(BorderLayout.NORTH, header);
	}

	// update the Popup when either the month or the year of the calendar has been changed
	protected void updatePopup() {
	    monthLabel.setText( monthFormat.format(calendar.getTime()) );
	    if (days != null) {
		popup.remove(days);
	    }
	    days = new JPanel(new GridLayout(0, 7));
	    days.setBackground(background);
	    days.setOpaque(true);

	    Calendar setupCalendar = (Calendar) calendar.clone();
	    setupCalendar.set(Calendar.DAY_OF_WEEK, setupCalendar.getFirstDayOfWeek());
	    for (int i = 0; i < 7; i++) {
		int dayInt = setupCalendar.get(Calendar.DAY_OF_WEEK);
		JLabel label = new JLabel();
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setForeground(foreground);
		if (dayInt == Calendar.SUNDAY) {
		    label.setText(resource.getString("datecombobox.sun"));
		} else if (dayInt == Calendar.MONDAY) {
		    label.setText(resource.getString("datecombobox.mon"));
		} else if (dayInt == Calendar.TUESDAY) {
		    label.setText(resource.getString("datecombobox.tue"));
		} else if (dayInt == Calendar.WEDNESDAY) {
		    label.setText(resource.getString("datecombobox.wed"));
		} else if (dayInt == Calendar.THURSDAY) {
		    label.setText(resource.getString("datecombobox.thu"));
		} else if (dayInt == Calendar.FRIDAY) {
		    label.setText(resource.getString("datecombobox.fri"));
		} else if (dayInt == Calendar.SATURDAY){
		    label.setText(resource.getString("datecombobox.sat"));
		}
		days.add(label);
		setupCalendar.roll(Calendar.DAY_OF_WEEK, true);
	    }

	    setupCalendar = (Calendar) calendar.clone();
	    setupCalendar.set(Calendar.DAY_OF_MONTH, 1);
	    int first = setupCalendar.get(Calendar.DAY_OF_WEEK);
	    for (int i = 0; i < (first - 1); i++) {
		days.add(new JLabel(""));
	    }
	    for (int i = 1; i <= setupCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
		final int day = i;
		final JLabel label = new JLabel(String.valueOf(day));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setForeground(foreground);
		label.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {
			    label.setOpaque(false);
			    label.setBackground(background);
			    label.setForeground(foreground);
			    calendar.set(Calendar.DAY_OF_MONTH, day);
			    comboBox.setSelectedItem(dateFormat.format(calendar.getTime()));
			    // hide();
			    // hide is called with setSelectedItem() ... removeAll()
			    comboBox.requestFocus();
			}
			public void mouseEntered(MouseEvent e) {
			    label.setOpaque(true);
			    label.setBackground(selectedBackground);
			    label.setForeground(selectedForeground);
			}
			public void mouseExited(MouseEvent e) {
			    label.setOpaque(false);
			    label.setBackground(background);
			    label.setForeground(foreground);
			}
		    });

		days.add(label);
	    }

	    popup.add(BorderLayout.CENTER, days);
	    popup.pack();
	}
    }

    //////////////////////////////////////////////////////////////
    // This is only included to provide a sample GUI
    //////////////////////////////////////////////////////////////
    public static void main(String args[]) {
	JFrame f = new JFrame();
	Container c = f.getContentPane();
	c.setLayout(new FlowLayout());
	c.add(new JLabel("Date 1:"));
	c.add(new DateComboBox());
	c.add(new JLabel("Date 2:"));
	DateComboBox dcb = new DateComboBox();
	dcb.setEditable(true);
	c.add(dcb);
	f.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    });
	f.setSize(500, 200);
	f.setVisible(true);
    }

}





