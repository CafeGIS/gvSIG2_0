/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
*
* $Id: JIoSTextField.java 13187 2007-08-21 08:39:49Z bsanchez $
* $Log$
* Revision 1.2  2007-08-21 08:36:56  bsanchez
* - Variable no usada
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.2  2006/10/26 17:59:33  jaume
* *** empty log message ***
*
* Revision 1.1  2006/10/25 15:53:25  jaume
* new components (text fields that only accepts numbers and text fields that only accepts decimal numbers)
*
*
*/
package de.ios.framework.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Textfield, ABSTRACT basic class for any Singleline-Input-Fields.
 * Includes support for key-substitution (to solve problems with i.e.
 * the '@'), limited Charactersets, auto-formating on lost focus,
 * automatic selection on gained focus.
 * Implementation of several Listener-Interfaces only for internal use!
 * @version $Id: JIoSTextField.java 13187 2007-08-21 08:39:49Z bsanchez $
 */
public abstract class JIoSTextField extends JTextField
implements DocumentListener, FocusListener, MouseListener {

	// FIXME: NOT SUPPORTED !!!!!!
	public void setEchoChar( char c ) {
		new Exception( "Depricated call to setEchoChar,. Use JPasswordField instead." ).
		printStackTrace();
	}

	/** STATIC: Faster setText-Handling - if true, the Text is only set, if it differs from the current value. */
	public static boolean fastSetText = true;

	/** Default for auto-selection-handling. */
	public static boolean defaultAutoSelection = false;

	/** Lokal auto-selektion-mode. */
	protected Boolean autoSelection = null;

	/** Focus-Handling. Stores "temporary"-flag of the last lostFocus. */
	protected boolean lastFTemp = false;

	/** Keys to replace on input. STILL REQUIRED??? Don't know... */
	protected String keys        = ""; // "@";

	/** Replacements for keys on input. STILL REQUIRED??? Don't know... */
	protected String replace     = ""; // "@";

	/** A String with valid input characters. If null, all characters are valid. */
	protected String charSet = null;

	/** Info-Text for this field. */
	protected String infoText = null;

	/** Auto-Format-Flag. */
	protected boolean autoFormat = true;

	/** Keep-Focus-On-Illegal-Value-Flag. */
	protected boolean keepFocus = false;   // FIXME: should be:   true;

	/** Disable-Lock for setEnabled(). */
	protected boolean lockDisable = false;


	/**
	 * Constructor.
	 */
	public JIoSTextField() {
		super();
		initListener();
	}


	/**
	 * Constructor.
	 */
	public JIoSTextField( int cols ) {
		super(cols);
		initListener();
	}


	/**
	 * Constructor.
	 * This constructor is temporary of type 'package', cause it's of the same
	 * type like a deprecated constructor, but with changed behaviour.
	 */
	JIoSTextField( String _charSet ) {
		super();
		setCharSet( _charSet );
		initListener();
	}


	/**
	 * Constructor defining also the Charset.
	 */
	public JIoSTextField( int cols, String _charSet ) {
		super(cols);
		setCharSet( _charSet );
		initListener();
	}


	/**
	 * Constructor defining the charset, auto-formating and illegal-value-focus-keeping.
	 */
	public JIoSTextField( int cols, String _charSet, boolean _autoFormat, boolean _keepFocus ) {
		super(cols);
		setCharSet( _charSet );
		setAutoFormat( _autoFormat );
		setKeepFocusOnIllegalValue( _keepFocus );
		initListener();
	}


//	/**
//	* Add an ActionListener, set a predefined color for 'Action'-Fields.
//	*/
//	public void addActionListener(ActionListener l) {
//	super.addActionListener( l );
//	String c = Parameters.getParameter( "jtextfield.action.color" );
//	if ( c != null )
//	setBackground( new Color( Integer.parseInt( c, 16 ) ) );
//	}


	/**
	 * Disable the field and lock it against any setEnable().
	 */
	public void lockDisable( boolean disable ) {
		if (!isDisableLocked())
			super.setEnabled( false );
		lockDisable |= disable;
	}


	/**
	 * Unlock a the lock on the disable of the field (does not directly enable the field, but realows enabling).
	 */
	public void unlockDisabled() {
		lockDisable = false;
	}


	/**
	 * Check, if the field is locked disabled.
	 */
	public boolean isDisableLocked() {
		return lockDisable;
	}


	/**
	 * setEnabled() protected with the disable-lock.
	 */
	public void setEnabled( boolean en ) {
		if (!isDisableLocked())
			super.setEnabled( en );
	}


	/**
	 * Set the displayed text withoput any furhter checks.
	 */
	protected void setRawText( String text ) {
		if (text == null)
			text = "";
		if ( fastSetText ? (text.compareTo( super.getText() )!=0) : true )
			super.setText( text );
	}


	/**
	 * Sets the text. Watches the maximal size of the field,
	 * so that the leading part of the text is always visible,
	 * handles null-Values.
	 * (the default-implementation show the trailing part of the text, because
	 * always the caret is set to the end of the textfield.).
	 */
	public void setText( String text ) {
		setRawText( text );
		if ( text != null )
			if ( text.length() >= getColumns())
				if (fastSetText ? (getCaretPosition() != 0) : true)
					setCaretPosition( 0 );
	}


	/**
	 * Gets the text.
	 * We couldn't give that functionality to getText(),
	 * cause it's internally used by Super-Classes not expecting null-Values.
	 */
	public String getNullText() {
		String text = getText();
		return (text.length() == 0) ? null : text;
	}


	/**
	 * Get the text after formating (primary for internal use at inheritances).
	 * Does (and MUST) not request the focus.
	 */
	public String getFormatedText() {
		if (!formatValue())
			setText( null );
		return getNullText();
	}


	/**
	 * Request-Method for formating the Data (only for redefinition by inheritances).
	 * Default-Implementation does nothing.
	 * @return false, if formating fails due to an illegal Value
	 * (results in keeping the Focus, if requested by setKeepFocusOnIllegalValue).
	 */
	public boolean formatValue() {
		return true;
	}


	/**
	 * Set a "special" character set. All other characters are ignored on input.
	 * @see #keyTyped()
	 * @param s A string with valid characters.
	 */
	public void setCharSet(String s) {
		charSet = s;
	}


	/**
	 * Get the "special" character set.
	 * @return The "special" character set.
	 */
	public String getCharSet() {
		return charSet;
	}


//	/**
//	* Implements InfoTopic.
//	*/
//	public void setInfoText( String text ) {
//	infoText = text;
//	InfoTopicViewer.watchInfoTopic( this );
//	}


	/**
	 * Implements InfoTopic.
	 */
	public String getInfoText( ) {
		return infoText;
	}


	/**
	 * Enable/disable the Auto-Selection-Mode.
	 */
	public void setAutoSelection( boolean _autoSelection ) {
		autoSelection = new Boolean( _autoSelection );
	}


	/**
	 * Set the autoformat-mode of this field.
	 * If autoformat is activated the field is automaticaly formated if the focus
	 * is lost permanently.
	 */
	public void setAutoFormat( boolean _autoFormat ) {
		autoFormat = _autoFormat;
	}


	/**
	 * Defines, if to keep the Focus on illegal Values (only on active AutoFormat).
	 */
	public void setKeepFocusOnIllegalValue( boolean _keepFocus ) {
		keepFocus = _keepFocus;
	}


	/**
	 * Define the Keys to be handled manually
	 */
	public synchronized JIoSTextField manuallyHandle(String _keys, String _replace) {
		keys=_keys;
		replace=_replace;
		if ( keys == null )
			keys="";
		if ( replace == null )
			replace="";
		return this;
	}


	/////// Internal Stuff ///////


	/**
	 * Init the listeners.
	 */
	protected void initListener() {
		getDocument().addDocumentListener( this );
		addFocusListener( this );
		addMouseListener( this );
	}


	// Events //


	/**
	 * Implements FocusListener
	 * Invoked when the component gains the keyboard focus.
	 * Marks the complet input (but leaves the caret-position).
	 */
	public void focusGained( FocusEvent  evt ) {
		if ( ((autoSelection == null) ? defaultAutoSelection : autoSelection.booleanValue())
				&& isEditable() && !lastFTemp) {
			int p = getCaretPosition();
			selectAll();
			setCaretPosition( p );
		}
	}


	/**
	 * Implements FocusListener
	 * Invoked when a component loses the keyboard focus.
	 * Stores the temporary-flag of the event and
	 * resets selection if autoselection is enabled.
	 */
	public void focusLost( FocusEvent evt ) {
		lastFTemp = evt.isTemporary();
		if (!lastFTemp) {
			if ( autoFormat && isEditable() )
				if ( !formatValue() ) {
					if ( keepFocus )
						requestFocus();
					//else
					//  setText( null );
					getToolkit().beep();
				}
			if ( (autoSelection == null) ? defaultAutoSelection : autoSelection.booleanValue() ) {
				int p = getCaretPosition();
				select(0,0);
				setCaretPosition( p );
			}
		}
	}


	/**
	 * Implements MouseListener
	 * Invoked when the mouse has been clicked on a component.
	 * Defaultimplementation, does nothing
	 */
	public void mouseClicked(MouseEvent evt) {
	}


	/**
	 * Implements MouseListener
	 * Invoked when the mouse enters a component.
	 * Defaultimplementation, does nothing
	 */
	public void mouseEntered(MouseEvent evt) {
	}


	/**
	 * Implements MouseListener
	 * Invoked when the mouse exits a component.
	 * Defaultimplementation, does nothing
	 */
	public void mouseExited(MouseEvent evt) {
	}


	/**
	 * Implements MouseListener
	 * Invoked when a mouse button has been pressed on a component.
	 * Subpress autoselection for the following focus-event.
	 */
	public void mousePressed(MouseEvent evt ) {
		lastFTemp = true;
	}


	/**
	 * Implements MouseListener
	 * Invoked when a mouse button has been released on a component.
	 * Defaultimplementation, does nothing
	 */
	public void mouseReleased(MouseEvent evt ) {
	}

	/**
	 * Implements DocumentListener.
	 * @see #handleDocumentEvent
	 */
	public void changedUpdate( DocumentEvent e ) {
		handleDocumentEvent( e );
	}

	/**
	 * Implements DocumentListener.
	 * @see #handleDocumentEvent
	 */
	public void insertUpdate( DocumentEvent e ) {
		handleDocumentEvent( e );
	}

	/**
	 * Implements DocumentListener.
	 * @see #handleDocumentEvent
	 */
	public void removeUpdate( DocumentEvent e ) {
		handleDocumentEvent( e );
	}

	/**
	 * Flag that indicated that a text-check is already involed at the end of the actual event-processing.
	 */
	boolean checkInvoked = false;

	/**
	 * Handle a document-event.
	 */
	protected void handleDocumentEvent( final DocumentEvent evt ) {
		if (checkInvoked)
			return;
		checkInvoked = true;
		SwingUtilities.invokeLater( new Runnable() {

			public void run() {
				try {

					String text = getNullText();

					if (text == null)
						return;

					int  idx;
					char c;

					int firstRemovePos = -1;

					int n = text.length();
					StringBuffer sb = new StringBuffer( n );

					// Check actual input.
					for (int i=0 ; i<n ; i++) {
						c = text.charAt( i );
						idx = keys.indexOf( c );
						if ( idx >= 0 )
							c = replace.charAt(idx);
						if ( (charSet == null) || (charSet.indexOf(c) >= 0))
							sb.append( c );
						else {
							if (firstRemovePos<0)
								firstRemovePos = idx;
						}
					}

					String newText = sb.toString();
					int newN = newText.length();
					if ((newN != n) || (newText.compareTo( text )!=0) ) {
						int cpos = getCaretPosition();
						setRawText( newText );

						if ((cpos > firstRemovePos) && (n>newN))
							cpos -= (n-newN);
						if (cpos < 0)
							cpos = 0;
						if (cpos > newN)
							cpos = newN;
						setCaretPosition( cpos );
						getToolkit().beep();
					}
				} finally {
					checkInvoked = false;
				}
			}
		} );
	}
}