

/*
 * $Id: IoSTextField.java 13184 2007-08-21 08:38:21Z bsanchez $
 *
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

import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;


/**
 * Textfield, ABSTRACT basic class for any Singleline-Input-Fields.
 * Includes support for key-substitution (to solve problems with i.e.
 * the '@'), limited Charactersets, auto-formating on lost focus,
 * automatic selection on gained focus.
 * Implementation of several Listener-Interfaces only for internal use!
 * @version $Id: IoSTextField.java 13184 2007-08-21 08:38:21Z bsanchez $
 */
public abstract class IoSTextField extends JTextField
implements KeyListener, FocusListener, MouseListener {


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
  protected boolean keepFocus = true;

  /** Disable-Lock for setEnabled(). */
  protected boolean lockDisable = false;


  /**
   * Constructor.
   */
  public IoSTextField() {
    super();
    initListener();
  }


  /**
   * Constructor.
   */
  public IoSTextField( int cols ) {
    super(cols);
    initListener();
  }


  /**
   * Constructor.
   * This constructor is temporary of type 'package', cause it's of the same
   * type like a deprecated constructor, but with changed behaviour.
   */
  IoSTextField( String _charSet ) {
    super();
    setCharSet( _charSet );
    initListener();
  }


  /**
   * Constructor defining also the Charset.
   */
  public IoSTextField( int cols, String _charSet ) {
    super(cols);
    setCharSet( _charSet );
    initListener();
  }


  /**
   * Constructor defining the charset, auto-formating and illegal-value-focus-keeping.
   */
  public IoSTextField( int cols, String _charSet, boolean _autoFormat, boolean _keepFocus ) {
    super(cols);
    setCharSet( _charSet );
    setAutoFormat( _autoFormat );
    setKeepFocusOnIllegalValue( _keepFocus );
    initListener();
  }


  /**
   * Constructor.
   */
  /* deprecated
  public IoSTextField( String text, int cols ) {
    super(cols);
    setText( text )
    initListener();
  }
  */


  /**
   * Add an ActionListener, set a predefined color for 'Action'-Fields.
   */
  public void addActionListener(ActionListener l) {
    super.addActionListener( l );
    //String c = Parameters.getParameter( "textfield.action.color" );
    //if ( c != null )
    //  setBackground( new Color( Integer.parseInt( c, 16 ) ) );
  }


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
   * Sets the text. Watches the maximal size of the field,
   * so that the leading part of the text is always visible,
   * handles null-Values.
   * (the default-implementation show the trailing part of the text, because
   * always the caret is set to the end of the textfield.).
   */
  public void setText( String text ) {
    if (text == null)
      text = "";
    if ( fastSetText ? (text.compareTo( syncGetText() )!=0) : true )
      super.setText( text );
    if ( text != null )
      if ( text.length() >= getColumns())
        if (fastSetText ? (getCaretPosition() != 0) : true)
	  setCaretPosition( 0 );
    textValueChanged();
  }

  /**
   * Overloaded, calls syncGetText().
   * @see syncGetText()
   */
  public String getText() {
    return syncGetText();
  }

  /**
   * To solve problem with blocking peers (Bug in Motif/JDK?).
   */
  public synchronized String syncGetText() {
    return super.getText();
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
  public synchronized IoSTextField manuallyHandle(String _keys, String _replace) {
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
    addKeyListener  ( this );
    addFocusListener( this );
    addMouseListener( this );
    //addTextListener ( this );
  }


  // Events //

  public static boolean restoreCaretTested = false;
  public static boolean restoreCaretPosition;


  /**
   * Implements FocusListener
   * Invoked when the component gains the keyboard focus.
   * Marks the complet input (but leaves the caret-position).
   */
  public void focusGained( FocusEvent  evt ) {
    if ( ((autoSelection == null) ? defaultAutoSelection : autoSelection.booleanValue())
	 && isEditable() && !lastFTemp) {
	int l = getText().length();
	if (l>0) {
	    int p = 0;
	    if (!restoreCaretTested) {
		p = getCaretPosition();
		selectAll();
		setCaretPosition( p );
		restoreCaretPosition = (getSelectedText().length()==l);
	    }
	    if (restoreCaretPosition)
		p = getCaretPosition();
	    selectAll();
	    if (restoreCaretPosition)
		setCaretPosition( p );
	}
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
	  else
	    setText( null );
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
   * Implements KeyListener.
   * Invoked when a key has been pressed.
   */
  public synchronized void keyPressed(KeyEvent evt ) {
  }


  /**
   * Implements KeyListener.
   * Invoked when a key has been released.
   */
  public synchronized void keyReleased(KeyEvent evt ) {
  }


  /**
   * Implements KeyListener.
   * Invoked when a key has been typed. Replaces manualy handled Characters.
   */
  public synchronized void keyTyped(KeyEvent evt ) {
    int pos=keys.indexOf(evt.getKeyChar());
    if (pos!=-1) {
      String rtext;
      String text = getNullText();
      int    cpos = getCaretPosition();
      if ( text != null ) {
	rtext = text.substring(0,cpos-1)+(char)replace.charAt(pos);
	if ( text.length()>cpos)
	  rtext += text.substring(cpos);
	setText( rtext );
	setCaretPosition( cpos );
      }
    }
  }


  /**
   * Implements Text-Listener.
   */
  public void textValueChanged() {
    String  val = getNullText();
    int     op  = getCaretPosition();
    int     p   = op;
    int     i;
    boolean changed = false;

    if ((charSet != null) && (val != null)) {
      for ( i=val.length()-1; i>=0; i-- ) {
	if (charSet.indexOf(val.charAt(i)) < 0) {
	  if (p>i)
	    p--;
	  val = val.substring(0,i)+val.substring(i+1);
	  changed = true;
	}
      }
      if (changed) {
	setText( val );
	if (p != op)
	  setCaretPosition( p );
	getToolkit().beep();
      }
    }
  }

}

/*
 * $Log$
 * Revision 1.2  2007-08-21 08:36:33  bsanchez
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
 * Revision 1.1.1.1  2001/02/07 15:24:00  rtfm
 * initial
 *
 * Revision 1.17  1999/12/16 15:21:55  fw
 * TextField: optional coloring of Fields with ActionListeners (property: textfield.action.color=RRGGBB (hex))
 * TextArea: no change
 *
 * Revision 1.16  1999/12/05 22:19:15  bw
 * Automatische makrierung gefixed
 *
 * Revision 1.15  1999/11/26 14:16:30  fw
 * added lock-disable-methods
 *
 * Revision 1.14  1999/11/25 12:07:27  js
 * added syncGetText() to solve problem with blocking peers (Bug in Motif/JDK?).
 *
 * Revision 1.13  1999/10/27 10:57:58  fw
 * bugfix due to changed event-order on jdk1.1.7 on illegal characters
 *
 * Revision 1.12  1999/08/02 09:03:20  js
 * Fix for blocking texfields when ENTER was typed (JDK >= 1.1.7).
 *
 * Revision 1.11  1999/05/20 11:12:15  fw
 * removed special handling of @, seems not to be neccessary at current jdk's anymore, caused problems at windows-jdk's
 *
 * Revision 1.10  1999/01/07 12:29:23  fw
 * Speed-Up for setText()-Methods (reduces repaints of Fields, fastSetText-Mode, default: on/true)
 *
 * Revision 1.9  1998/12/08 18:26:10  fw
 * *Field: cleanup; autoFormat-, keepFocus-, limited-Charset-Support for all Fields (enabled by default)
 * ViewLayout: Litle change due to changes at the *Fields
 * DBListRow: toString()-Method for BigDecimals exchanging the '.' by ','
 *
 * Revision 1.8  1998/12/02 11:26:54  js
 * Support for special character sets added.
 *
 * Revision 1.7  1998/12/02 08:29:41  js
 * doc fixes
 *
 * Revision 1.6  1998/09/07 11:50:04  bw
 * Automatic caret repositioning on "setText", so that always
 * the leading part of the text is shown.
 *
 * Revision 1.5  1998/08/20 15:25:13  bw
 * Bugfix (lastFTemp was not set correctly)
 *
 * Revision 1.4  1998/08/13 09:46:05  bw
 * Default for auto-Selection set to false.
 *
 * Revision 1.3  1998/06/17 19:03:46  bw
 * Automativ Info-texts on right-mouse-click.
 *
 * Revision 1.2  1998/05/29 10:05:10  bw
 * Support for automatic selection on focus-gain.
 *
 * Revision 1.1  1998/05/13 11:55:38  fw
 * Added new basic TextField-Component IoSTextField (ExtendedTextField
 * with JDK 1.1 Event-Handling), replaced the Base-Class ExtendedTextField
 * of all other 'Fields' by IoSTextField
 * ExtendedTextField uses now the JDK 1.0 Event-Handling again (due to massive
 * Problems with blocked 1.0-Event-Handling at older Projects) - from now
 * on the complete Class is DEPRECATED.
 *
 * Revision 1.4  1998/05/10 05:46:39  bw
 * Automativ replacement convertzed to jdk 1.1 norm (using KeyListener).
 *
 * Revision 1.3  1998/02/06 14:03:10  fw
 * ExtendedTextField: removed some old, unused Code
 * PrintCanvas: turned debugging off
 *
 * Revision 1.2  1997/12/10 19:38:41  bb
 * Copyright eingefgt.
 *
 * Revision 1.1  1997/09/16 09:19:37  bw
 * Package-nderung von "ios-online" nach "ios".
 *
 * Revision 1.1  1997/09/09 08:42:00  fw
 * moved all framework-stuff of wrede.customer to the new de.ios.framework
 *
 * Revision 1.2  1997/07/29 11:54:10  fw
 * Added the missing CVS-Id and CVS-Log entries...
 *
 */







