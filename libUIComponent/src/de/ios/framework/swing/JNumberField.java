/*
 * $Id: JNumberField.java 13136 2007-08-20 08:38:34Z evercher $
 *
 * (c)1999 IoS Gesellschaft fr innovative Softwareentwicklung mbH
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

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;


/**
 * Input-Field for Numbers, supported Objects: BigInteger, Long, Integer, (long)
 * Implementation of several Listener-Interfaces by the Basic-Class only for internal use!
 * For further description
 * @see JIoSTextField
 * @version $Id: JNumberField.java 13136 2007-08-20 08:38:34Z evercher $
 */
public class JNumberField extends JIoSTextField {
	private static final long serialVersionUID = 6157661330265735100L;

	/** Input-Field-Size in Columns. */
	public final static int DEFAULT_LENGTH = 10;

	/** Limited Character-Set of this Field. */
	public static final String NUMBER_CHARSET = "-0123456789";

	/** The Decimal-Format. */
	protected DecimalFormat decimalFormat = null;


	/**
	 * Default Constructor
	 */
	public JNumberField() {
		this( DEFAULT_LENGTH );
		//super(NUMBER_CHARSET);
	}


	/**
	 * Constructor
	 * @param cols columns of textfield
	 */
	public JNumberField( int cols ) {
		super( cols, NUMBER_CHARSET );
	}


	/**
	 * Constructor
	 * @param _autoFormat if true use the special character set, ignore invalid characters
	 */
	public JNumberField( boolean _autoFormat ) {
		this( DEFAULT_LENGTH, _autoFormat );
	}


	/**
	 * Constructor
	 * @param cols columns of textfield
	 * @param _autoFormat
	 */
	public JNumberField( int cols, boolean _autoFormat ) {
		this( cols );
		setAutoFormat( _autoFormat );
	}


	/**
	 * Constructor defining the auto-formating and illegal-value-focus-keeping.
	 */
	public JNumberField( boolean _autoFormat, boolean _keepFocus ) {
		this( DEFAULT_LENGTH, _autoFormat, _keepFocus );
	}


	/**
	 * Constructor defining the auto-formating and illegal-value-focus-keeping.
	 */
	public JNumberField( int cols, boolean _autoFormat, boolean _keepFocus ) {
		super( cols, NUMBER_CHARSET, _autoFormat, _keepFocus );
	}


	/**
	 * Set the value
	 * @param value The value to set.
	 */
	public void setValue( BigInteger value ) {
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
		setText( (decimalFormat == null) ?
				String.valueOf( value ) :
					decimalFormat.format( value ) );
	}


	/**
	 * Set the format-pattern for display (WARNING: Can't be used with BigInteger!).
	 * @param pattern New pattern or null.
	 * @see java.text.DecimalFormat
	 */
	public void setDecimalFormat( DecimalFormat pattern )
	throws IllegalArgumentException {
		decimalFormat = (pattern == null) ? null : (DecimalFormat)pattern.clone();
	}


	/**
	 * Get the current value as BigInteger.
	 * @return The current value.
	 */
	public BigInteger getValue() {
		String bi = getFormatedText();
		try {
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
			if (bi != null)
				if ( bi.trim().length() == 0 )
					setText( null );
				else
					setValue( (decimalFormat == null) ?
							new BigInteger(bi) :
								BigInteger.valueOf( decimalFormat.parse( bi ).longValue() ) );
			return true;
		} catch ( ParseException p ) {  // If this happens, the formatValue()-Method is buggy!
			return false;
		} catch ( NumberFormatException e ) {  // If this happens, the formatValue()-Method is buggy!
			return false;
		}
	}
}


