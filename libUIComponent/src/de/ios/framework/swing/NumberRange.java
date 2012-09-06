/*
 * $Id: NumberRange.java 13655 2007-09-12 16:28:55Z bsanchez $
 * (c)1997 IoS Gesellschaft fr innovative Softwareentwicklung mbH
 * http://www.IoS-Online.de    mailto:info@IoS-Online.de
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package de.ios.framework.swing;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays a Textfield with two small buttons in an own panel.
 */
public class NumberRange extends Panel {
  private static final long serialVersionUID = 6977082046201638066L;

	/** Output debugging mssages, if true */
  public static final boolean debug = false;

  /** TextField to hold the current value */
  protected NumberField value  = null;
  /** Button to increment the current value */
  protected Button      plusB  = new Button("+");;
  /** Button to decrement the current value */
  protected Button      minusB = new Button("-");;

  // Default values
  /** Size of NumberField (digits) */
  protected int digits   = 3;
  /** The smallest allowed value */
  protected int minValue = 0;
  /** The biggest allowed value */
  protected int maxValue = 999;

  /**
   * Default Constructor
   */
  public NumberRange() {
    createDialog();
    value.setValue( new Integer( minValue ) );

  }

  /**
   * Constructor to set an initial value.
   *
   * @param v Initial value
   */
  public NumberRange(int v) {
    createDialog();
    value.setValue( new Integer(v) );
  }

  /**
   * Constructor to set an initial, minimal and maximal value.
   *
   * @param v Initial value
   * @param min Min. value
   * @param max Max. value
   */
  public NumberRange(int v, int min, int max) {
    minValue = min;
    maxValue = max;
    createDialog();
    value.setValue( new Integer(v) );
  }

  /**
   * Constructor to set the field size (digits) and
   * an initial, minimal and maximal value.
   *
   * @param dig Field size (digits)
   * @param v Initial value
   * @param min Min. value
   * @param max Max. value
   */
  public NumberRange(int dig, int v, int min, int max) {
    digits = dig;
    minValue = min;
    maxValue = max;
    createDialog();
    value.setValue( new Integer(v) );
  }

  /**
   * Arranges the GUI components
   */
  protected void createDialog() {
    value  = new NumberField( digits );
    value.setEditable( false );
    setLayout( new FlowLayout( FlowLayout.LEFT, 1, 1) );
    add( value  );
    add( plusB  );
    add( minusB );

    // Button events
    plusB.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	incValue();
      }
    });
    minusB.addActionListener( new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	decValue();
      }
    });
  }

  /**
   * Increment the current value.
   */
  protected void incValue() {
    int v = this.getValue();
    if (v < maxValue) {
      value.setValue( new Integer(v+1) );
    }
  }

  /**
   * Decrement the current value.
   */
  protected void decValue() {
    int v = this.getValue();
    if (v > minValue) {
      value.setValue( new Integer(v-1) );
    }
  }

  /**
   * Returns the current value.
   */
  public int getValue() {
    return value.getIntegerValue().intValue();
  }

  /**
   * Sets the current value
   */
  public void setValue(int v) {
    value.setValue( new Integer(v) );
  }

}

/*
 * $Log$
 * Revision 1.3  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 08:37:22  bsanchez
 * - Quitados warnings en imports innecesarios
 *
 * Revision 1.1  2007/08/20 08:34:46  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.2  2007/01/23 13:11:06  caballero
 * valor no numérico
 *
 * Revision 1.1.2.1  2007/01/19 13:42:54  caballero
 * NumericField
 *
 * Revision 1.1.1.1  2001/02/07 15:24:01  rtfm
 * initial
 *
 * Revision 1.4  1998/05/15 08:45:04  js
 * added setValue
 *
 * Revision 1.3  1998/04/21 15:39:25  js
 * More comments.
 *
 * Revision 1.2  1998/04/17 12:49:13  js
 * Changes for layout of CardViews (missing right line...)
 *
 * Revision 1.1  1998/04/17 07:58:20  js
 * A textfield (NumberField) with two buttons to increment/decrement the
 * numerical value.
 *
 */

