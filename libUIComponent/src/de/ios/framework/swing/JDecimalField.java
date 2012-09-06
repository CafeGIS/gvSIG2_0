/*
 * $Id: JDecimalField.java 13136 2007-08-20 08:38:34Z evercher $
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

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.swing.event.DocumentEvent;

import de.ios.framework.basic.BigDecimalFormat;

/**
 * Input Field for decimal Numbers, supported Objects: BigDecimal.
 * Implementation of several Listener-Interfaces by the Basic-Class only for internal use!
 * For further description
 * @see JIoSTextField
 * @version $Id: JDecimalField.java 13136 2007-08-20 08:38:34Z evercher $
 */
public class JDecimalField extends JIoSTextField {

	private static final long serialVersionUID = -600056966056029618L;

	/** Input-Field-Size in Columns. */
	public static final int    DEFAULT_LENGTH = 10;

	/** Limited Character-Set of this Field. */
	private static final String DECIMAL_CHARSET = "0123456789TMtm"; // additional read from DecimalFormatSymbols

	/** The Parse-Formater. */
	protected BigDecimalFormat parseFormater = null;

	/** Scale for the value. */
	protected int minscale = 2;
	protected int maxscale = 2;

	/** Rounding-modes for BigDecimal.setScale-calls. */
	protected int roundMode = BigDecimal.ROUND_HALF_UP;

	/** first-init-flag. */
	protected boolean firstInit = true;

	/** auto-down-scaling? */
	protected boolean doAutoDownScaling = true;


	/**
	 * Default Constructor.
	 */
	public JDecimalField() {
		this( DEFAULT_LENGTH );
		//super(DECIMAL_CHARSET);
	}


	/**
	 * Constructor.
	 * @param cols number of columns of input field
	 */
	public JDecimalField(int cols) {
		super( cols, DECIMAL_CHARSET );
	}


	/**
	 * Constructor
	 * @param cols length of the field
	 * @param _scale precision of this field. _scale == -1 means no scaling!
	 */
	public JDecimalField(int cols, int _scale) {
		this( cols );
		setScale( _scale );
	}


	/**
	 * Constructor defining the auto-formating and illegal-value-focus-keeping.
	 */
	public JDecimalField( boolean _autoFormat, boolean _keepFocus ) {
		this( DEFAULT_LENGTH, _autoFormat, _keepFocus );
	}


	/**
	 * Constructor defining the auto-formating and illegal-value-focus-keeping.
	 */
	public JDecimalField( int cols, boolean _autoFormat, boolean _keepFocus ) {
		super( cols, DECIMAL_CHARSET, _autoFormat, _keepFocus );
	}


	/**
	 * Internal Method for setting the DecimalFormat.
	 */
	protected void defineDecimalFormat() {
		String s1 = getScaleFormat( minscale );
		String s2 = getScaleFormat( maxscale );
		if (s1.length() < s2.length())
			s1 += s2.substring( s1.length() );
		parseFormater = new BigDecimalFormat( s1 );
		if (firstInit) {
			setCharSet( getCharSet()+getFormatChars() );
			firstInit = false;
		}
	}

	/**
	 * Sets the specific Format
	 *
	 * @param formater the Formater for the decimal values
	 */
	public void setBigDecimalFormat(BigDecimalFormat format) {
		parseFormater = format;
	}

	protected String getFormatChars() {
		DecimalFormatSymbols decFormSym = parseFormater.getDecimalFormatSymbols();
		return
		""+
		decFormSym.getDecimalSeparator()+
		decFormSym.getGroupingSeparator()+
		decFormSym.getMinusSign();
	}


	/**
	 * Get a Scale-Format-String (up to Scale 20).
	 */
	protected String getScaleFormat( int scale ) {
		return ",##0." + ((scale < 0)
				? "########################################"
						: ("0000000000000000000000000000000000000000".substring(0, scale)));
	}


	/**
	 * Detect the proper scale to set.
	 */
	protected int detectScale( BigDecimal bd ) {
		if (bd == null)
			return -1;
		int ns, ms, s = bd.scale();
		if      ((minscale != -1) && (s < minscale))
			ns = minscale;
		else if ((maxscale != -1) && (s > maxscale))
			ns = maxscale;
		else
			ns = s;
		if (doAutoDownScaling) {
			ms = (minscale == -1) ? 0 : minscale;
			if (ms < ns) {
				String bs = ((s == ns) ? bd : bd.setScale(ns, roundMode)).toString();
				int c;
				for (c = 1; (c <= (ns-ms)) && (bs.charAt(bs.length()-c) == '0'); c++)
					;
				ns = ns-c+1;
			}
		}
		return (ns == s) ? -1 : ns;
	}


	/**
	 * Get a formater for a scale.
	 */
	protected BigDecimalFormat getFormater( int scale ) {
		return new BigDecimalFormat( getScaleFormat( scale ) );
	}

	/**
	 * Sets the scale of the field.
	 */
	public JDecimalField setScale( int _scale ) {
		return setScale( _scale, _scale );
	}


	/**
	 * Sets the min. and max. scale of the field.
	 */
	public JDecimalField setScale( int _minscale, int _maxscale ) {
		return setScale( _minscale, _maxscale, doAutoDownScaling );
	}


	/**
	 * Sets the min. and max. scale of the field.
	 */
	public JDecimalField setScale( int _minscale, int _maxscale, boolean _autoDownScale ) {
		minscale          = _minscale;
		maxscale          = _maxscale;
		doAutoDownScaling = _autoDownScale;
		return this;
	}


	/**
	 * Gets the (min.) scale of the field.
	 */
	public int getScale() {
		return minscale;
	}


	/**
	 * Gets the min. scale of the field.
	 */
	public int getMinScale() {
		return minscale;
	}


	/**
	 * Gets the max. scale of the field.
	 */
	public int getMaxScale() {
		return maxscale;
	}


	/**
	 * Set the rounding-mode.
	 * @see java.math.BigDecimal
	 */
	public JDecimalField setRoundMode( int _mode ) {
		roundMode = _mode;
		return this;
	}


	/**
	 * Get the rounding-mode.
	 * @see java.math.BigDecimal
	 */
	public int getRoundMode() {
		return roundMode;
	}

	/**
	 * Set the Value.
	 */
	public void setValue( BigDecimal value ) {
		defineDecimalFormat();
		if (value == null)
			setText( null );
		else {
			int sc = detectScale( value );
			setText( getFormater(sc).formatBD( (sc <0) ? value : value.setScale( sc, roundMode ) ) );
		}
	}


	/**
	 * Get the Value.
	 */
	public BigDecimal getValue() {
		String dc = getFormatedText();
		try {
			return createBigDecimal( dc );
		} catch (NumberFormatException e) { // If this happens, the formatValue()-Method is buggy!
			e.printStackTrace();
		} catch (ParseException e) { // If this happens, the formatValue()-Method is buggy!
			e.printStackTrace();
		}
		return null;

	}


	/**
	 * Format the Value (automatically called on lostFocus, manually called on getValue/Date()).
	 * @return false, if formating fails due to an illegal Value
	 * (results in keeping the Focus, if requested by setKeepFocusOnIllegalValue).
	 */
	public boolean formatValue() {
		String dc = getNullText();
		try {
			setValue( createBigDecimal( dc ) );
			return true;
		} catch (NumberFormatException e) {
			return false;
		} catch (ParseException e) {
			return false;
		}
	}


	/**
	 * Create a BigDecimal from a formated Text.
	 */
	protected BigDecimal createBigDecimal( String s )
	throws NumberFormatException, ParseException {
		BigDecimal bd;

		defineDecimalFormat();
		if (s == null)
			return null;
		s = s.trim();
		while ( s.endsWith("-") )
			s = s.substring( 0, s.length()-1 ).trim();
		if ( s.length() == 0 )
			return null;
		else {
			s = s.toUpperCase();
			if (s.endsWith("T"))
				s = s.substring(0, s.length()-1)+"000";
			else  if (s.endsWith("M"))
				s = s.substring(0, s.length()-1)+"000000";
			bd = (BigDecimal)parseFormater.parse( s );
		}
		int sc = detectScale( bd );
		return ( (sc < 0)
				? bd
						: bd.setScale( sc, roundMode ) );
	}


	/**
	 * Handle a document-event.
	 */
	protected void handleDocumentEvent( DocumentEvent evt ) {
		defineDecimalFormat();
		super.handleDocumentEvent( evt );
	}

}

