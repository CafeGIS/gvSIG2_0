/*
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
 */

package de.ios.framework.swing;

import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;


/**
 * Input-Field for Numbers, supported Objects: BigInteger, Long, Integer, (long)
 * Implementation of several Listener-Interfaces by the Basic-Class only for internal use!
 * For further description
 * @see IoSTextField
 * @version $Id: NumberField.java 13655 2007-09-12 16:28:55Z bsanchez $
 */
public class NumberField extends IoSTextField {
  private static final long serialVersionUID = -2597892768297671667L;

	/** Input-Field-Size in Columns. */
  public final static int DEFAULT_LENGTH = 10;

  /** Limited Character-Set of this Field. */
  private static final String NUMBER_CHARSET = "-0123456789TMtm";

  /** The Decimal-Format. */
  protected DecimalFormat decimalFormat = null;

  /** The Decimal-Format-Pattern. */
  protected String dformat = "0";


  /**
   * Default Constructor
   */
  public NumberField() {
    this( DEFAULT_LENGTH );
  }


  /**
   * Constructor
   * @param cols columns of textfield
   */
  public NumberField( int cols ) {
    super( cols, NUMBER_CHARSET );
  }


  /**
   * Constructor
   * @param _autoFormat if true use the special character set, ignore invalid characters
   */
  public NumberField( boolean _autoFormat ) {
    this( DEFAULT_LENGTH, _autoFormat );
  }


  /**
   * Constructor
   * @param cols columns of textfield
   * @param _autoFormat
   */
  public NumberField( int cols, boolean _autoFormat ) {
    this( cols );
    setAutoFormat( _autoFormat );
  }


  /**
   * Constructor defining the auto-formating and illegal-value-focus-keeping.
   */
  public NumberField( boolean _autoFormat, boolean _keepFocus ) {
    this( DEFAULT_LENGTH, _autoFormat, _keepFocus );
  }


  /**
   * Constructor defining the auto-formating and illegal-value-focus-keeping.
   */
  public NumberField( int cols, boolean _autoFormat, boolean _keepFocus ) {
    super( cols, NUMBER_CHARSET, _autoFormat, _keepFocus );
  }


  /**
   * Set the value
   * @param value The value to set.
   */
  public void setValue( BigInteger value ) {
    defineDecimalFormat();
    setText( (value == null) ?
	     null :
	     ( (decimalFormat == null) ?
	       value.toString() :
	       decimalFormat.format( value.longValue() ) ) );
  }


  /**
   * Set the value
   * @param value The value to set.
   */
  public void setValue( Integer value ) {
    defineDecimalFormat();
    setText( (value == null) ?
	     null :
	     ( (decimalFormat == null) ?
	       value.toString() :
	       decimalFormat.format( value.longValue() ) ) );
  }


  /**
   * Set the value
   * @param value The value to set.
   */
  public void setValue( Long value ) {
    defineDecimalFormat();
    setText( (value == null) ?
	     null :
	     ( (decimalFormat == null) ?
	       value.toString() :
	       decimalFormat.format( value.longValue() ) ) );
  }


  /**
   * Set the value
   * @param value The value to set.
   */
  public void setValue( long value ) {
    defineDecimalFormat();
    setText( (decimalFormat == null) ?
	     String.valueOf( value ) :
	     decimalFormat.format( value ) );
  }


  /**
   * Set the format for display (WARNING: Can't be used with BigInteger!).
   * @param formater New formater or null.
   * @see java.text.DecimalFormat
   */
  public void setDecimalFormat( DecimalFormat formater )
    throws IllegalArgumentException {
      decimalFormat = (formater == null) ? null : (DecimalFormat)formater.clone();
      dformat = null;
  }


  /**
   * Set the format-pattern for display (WARNING: Can't be used with BigInteger!).
   * @param pattern New pattern or null.
   * @see java.text.DecimalFormat
   */
  public NumberField showTPoints( boolean b ) {
    dformat = (b ? ",##0" : "0");
    return this;
  }


  /**
   * Internal Method for setting the DecimalFormat.
   */
  protected void defineDecimalFormat() {
    DecimalFormatSymbols decFormSym;

    if (dformat != null) {
      try {
	decimalFormat = new DecimalFormat( dformat );
	decFormSym    = decimalFormat.getDecimalFormatSymbols();
	setCharSet( getCharSet()+
		    decFormSym.getGroupingSeparator() );
      } catch (IllegalArgumentException e) {
    	  e.printStackTrace();
      }
      dformat = null;
    }
  }


  /**
   * Get the current value as BigInteger.
   * @return The current value.
   */
  public BigInteger getValue() {
    String bi = getFormatedText();
    try {
      defineDecimalFormat();
      return ( (bi == null) ?
	       null :
	       ( (decimalFormat == null) ?
		 new BigInteger(bi) :
		 BigInteger.valueOf( decimalFormat.parse( bi ).longValue() ) ) );
    } catch ( ParseException p ) {  // If this happens, the formatValue()-Method is buggy!
    	 p.printStackTrace();
      return null;
    } catch ( NumberFormatException e ) {  // If this happens, the formatValue()-Method is buggy!
    	 e.printStackTrace();
      return null;
    }
  }


  /**
   * Get the current value as Long.
   * @return The current value.
   */
  public Long getLongValue() {
    BigInteger bi = getValue();
    return (bi == null) ? null : new Long( bi.longValue() );
  }


  /**
   * Get the current value as Integer.
   * @return The current value.
   */
  public Integer getIntegerValue() {
    BigInteger bi = getValue();
    return (bi == null) ? null : new Integer( bi.intValue() );
  }


  /**
   * Format the Value (automatically called on lostFocus, manually called on getValue/Date()).
   * @return false, if formating fails due to an illegal Value
   * (results in keeping the Focus, if requested by setKeepFocusOnIllegalValue).
   */
  public boolean formatValue() {
    String bi = getNullText();
    try {
      defineDecimalFormat();
      if (bi != null)
	if ( bi.trim().length() == 0 )
	  setText( null );
	else {
	  bi = bi.toUpperCase();
          if (bi.endsWith("T"))
            bi = bi.substring(0, bi.length()-1)+"000";
          else  if (bi.endsWith("M"))
            bi = bi.substring(0, bi.length()-1)+"000000";
	  setValue( (decimalFormat == null) ?
		    new BigInteger(bi) :
		    BigInteger.valueOf( decimalFormat.parse( bi ).longValue() ) );
        }
      return true;
    } catch ( ParseException p ) {  // If this happens, the formatValue()-Method is buggy!
      return false;
    } catch ( NumberFormatException e ) {  // If this happens, the formatValue()-Method is buggy!
      return false;
    }
  }


  /**
   * Implements KeyListener.
   * Invoked when a key has been typed. Replaces manualy handled Characters.
   */
  public synchronized void keyTyped(KeyEvent evt ) {
    defineDecimalFormat();
    super.keyTyped( evt );
  }

}

/*
 * $Log$
 * Revision 1.3  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.2  2007/08/21 08:37:09  bsanchez
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
 * Revision 1.1.1.1  2001/02/07 15:23:12  rtfm
 * initial
 *
 * Revision 1.17  1999/12/15 09:08:45  fw
 * allowed also small 't/m'
 *
 * Revision 1.16  1999/12/03 10:00:52  js
 * comment fix
 *
 * Revision 1.15  1999/04/22 11:47:33  fw
 * added xxxT & xxxM Support
 *
 * Revision 1.14  1999/04/15 15:50:09  fw
 * using Formaters
 *
 * Revision 1.13  1999/04/09 15:36:27  fw
 * extended Formater-Support
 *
 * Revision 1.12  1998/12/08 18:26:10  fw
 * *Field: cleanup; autoFormat-, keepFocus-, limited-Charset-Support for all Fields (enabled by default)
 * ViewLayout: Litle change due to changes at the *Fields
 * DBListRow: toString()-Method for BigDecimals exchanging the '.' by ','
 *
 * Revision 1.11  1998/12/02 11:27:43  js
 * Modifications for changes in IoSText (special character sets).
 *
 * Revision 1.10  1998/11/30 12:01:26  bw
 * Support for DecimalFormat added (automatic formating by java.text.DecimalFormat).
 * Not tested!!!!
 *
 * Revision 1.9  1998/06/30 12:18:30  bw
 * Bugfix (keyTyped now set Caret-Position and can handle empty fields).
 *
 * Revision 1.8  1998/05/13 11:55:38  fw
 * Added new basic TextField-Component IoSTextField (ExtendedTextField
 * with JDK 1.1 Event-Handling), replaced the Base-Class ExtendedTextField
 * of all other 'Fields' by IoSTextField
 * ExtendedTextField uses now the JDK 1.0 Event-Handling again (due to massive
 * Problems with blocked 1.0-Event-Handling at older Projects) - from now
 * on the complete Class is DEPRECATED.
 *
 * Revision 1.7  1998/04/28 13:21:41  js
 * bugfix in constructor
 *
 * Revision 1.6  1998/04/28 12:56:26  bw
 * Automativ supression of non-digits added.
 *
 * Revision 1.5  1998/04/08 11:58:49  fw
 * bugfix / extension
 *
 * Revision 1.4  1998/04/08 09:01:22  fw
 * bugfix: Standard-Default-Value is null
 *
 * Revision 1.3  1998/04/07 11:44:48  fw
 * *** empty log message ***
 *
 * Revision 1.2  1998/03/18 01:15:24  fw
 * added some Methods
 *
 * Revision 1.1  1998/03/15 15:40:09  mh
 * added new fields DateField, DecimalField and NumberField
 *
 */

