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
package org.gvsig.gui.beans.editabletextcomponent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ComboBoxEditor;
import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditEvent;
import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener;
import org.gvsig.gui.beans.editionpopupmenu.JOptionsEditionByMousePopupMenu;

/**
 * <p>Extra functionality that allows a {@link JTextComponent JTextComponent} having graphical edition options,
 * which allows user to select any: <i>COPY, CUT, PASTE, SELECT ALL, REMOVE, UNDO, REDO</i> in a popup menu.</p>
 * 
 * <p>All options will be about the edition of the text in the component.</p>
 *  
 * <p><b><i>How select an edition option using the mouse:</i></b> press the right button of the mouse on the text area, and a popup with the options will be displayed. Select and option.<br>
 * <b><i>Default fast-access keys:</i></b> 
 * <ul>
 *  <li><b>COPY:</b> </li> CTRL + C. Copies from the text field to the clipboard.
 *  <li><b>CUT:</b> </li> CTRL + X. Cuts from the text field to the clipboad.
 *  <li><b>PASTE:</b> </li> CTRL + V. Copies from the clipboard to the text field.
 *  <li><b>REMOVE:</b> </li> SUPR (in spanish keyboard, and the equivalent in others). Removes the selected text.
 *  <li><b>SELECT ALL:</b> </li> CTRL + A. Selects all the text.
 *  <li><b>UNDO:</b> </li> CTRL + Z. Undoes.
 *  <li><b>REDO:</b> </li> SHIFT + CTRL + Z. Redoes.
 * </ul></p>
 * 
 * <p>This component by default stores 100 undo/redo actions. This value can be modified.</p>
 * 
 * <p>A developer would have to re-implement {@linkplain #defineEditorKeyListener() #defineEditorKeyListener()}, creating
 *  a new <code>editorKeyListener</code> for changing any fast-access key.</p>
 * 
 * @version 27/02/2008
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class EditableTextDecorator implements IEditableText {
	// CONSTANTS
	public static final int DEFAULT_UNDO_REDO_LIMIT_ACTIONS = 100;
	// END CONSTANTS

	// A POPUPMENU FOR EDITION OPTIONS
	private JOptionsEditionByMousePopupMenu optionsEditionByMouse;
    // END A POPUPMENU FOR EDITION OPTIONS
	
	// UNDO-REDO
	private UndoManager undoManager;
	private int undoRedoLimitActions;
	// END UNDO-REDO
	
	// LISTENERS
	protected KeyListener editorKeyListener;
	private MouseAdapter editorMouseListener;
	private PropertyChangeListener editionMenuListener;
	private EventListenerList undoRedoEditListeners;
	// END LISTENERS
	
	// REFERENCE TO THE JTEXTCOMPONENT
	private JTextComponent jTextComponent;
	// END REFERENCE TO THE JTEXTCOMPONENT
	
	/**
	 * Default constructor
	 */
	public EditableTextDecorator(JTextComponent jTextComponent) {
		super();

		// Reference to the JTextComponent
		this.jTextComponent = jTextComponent;
		
		initialize();
		
		// Other configuration tasks
		this.createDefaultListeners();
		this.configure();
	}
	
	/**
	 * This method sets the start values of inner attributes and creates the necessary inner objects
	 */
	private void initialize() {
		undoRedoEditListeners = new EventListenerList();
		
        // Allows user to edit
		jTextComponent.setEditable(true);
		
		// Text options edition popupmenu initialization
		optionsEditionByMouse = new JOptionsEditionByMousePopupMenu();
		
		// Undo-Redo initialization
		undoManager = new UndoManager();
		undoRedoLimitActions = DEFAULT_UNDO_REDO_LIMIT_ACTIONS; // By default is 1
		undoManager.setLimit(undoRedoLimitActions);
	}
	
	/**
	 * Creation of default listeners that will use the component
	 */
	private void createDefaultListeners() {
		// Defines a listener for the PopupMenu of text edition options: when a change is produced in that component, it fires an event
		//   and this listener captures it
		defineEditionMenuPropertyChangeListener();

        // Defines a key listener for the editor of this component
        defineEditorKeyListener();

        // Defines a mouse listener for the editor of this component
        defineEditorMouseListener();
	}
	
	/**
	 * Defines a listener for the PopupMenu of text edition options: when a change is produced in that component, it fires an event
	 *    and this listener captures and treats it
	 */
	private void defineEditionMenuPropertyChangeListener() {
		editionMenuListener = new PropertyChangeListener() {
			/*
			 * (non-Javadoc)
			 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
			 */
			public void propertyChange(PropertyChangeEvent arg0) {
				if (arg0.getPropertyName().equals(JOptionsEditionByMousePopupMenu.SELECTEDOPTION)) {
	            						
			          switch(Integer.parseInt(arg0.getNewValue().toString()))
			          {
				          case JOptionsEditionByMousePopupMenu.UNDO:
				        	  undoOperationLogic();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.REDO:
				        	  redoOperationLogic();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.CUT:
				        	  jTextComponent.cut();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.COPY:
				        	  jTextComponent.copy();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.PASTE:
				        	  jTextComponent.paste();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.DELETE:
				        	  deleteTextLogic();
				        	  break;
				          case JOptionsEditionByMousePopupMenu.SELECT_ALL:
				        	  jTextComponent.selectAll();
				        	  break;
				          default: // do anything
			          }
				}
/*
				else
				{
					if (arg0.getPropertyName().equals(JOptionsEditionByMousePopupMenu.VISIBILITYCHANGED))
					{
						// First True, after False (when false -> optionsEditorWasVisible = true)
						if (!optionsEditionByMouse.isVisible())
							optionsEditorWasVisible = true;
						else
							optionsEditorWasVisible = false;

					}
				}
*/
			}			
		};
	}

	/**
	 * Defines a key listener for the editor of the text component.
	 */
	protected void defineEditorKeyListener() {
		editorKeyListener = new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			public void keyPressed(KeyEvent ke)  // Executed on the Start view state or Search view state
			{
				// COPY, CUT, PASTE, SELECT ALL, UNDO AND REDO WITH THE KEYBOARD (Combination of keys; REMOVE is implemented after: KV_DELETE (supr spanish key))
				if (ke.isControlDown())
				{
					// COPY
				    if (ke.getKeyCode() == KeyEvent.VK_C) {
				    	jTextComponent.copy();
				    	ke.consume();
				    	return;
				    }
				    
				    // CUT
				    if (ke.getKeyCode() == KeyEvent.VK_X) {
				    	jTextComponent.cut();
				    	ke.consume();
				    	return;
				    }
				    
				    // PASTE
				    if (ke.getKeyCode() == KeyEvent.VK_V) {
				    	jTextComponent.paste();
				    	ke.consume();
				    	return;
				    }
				    
				    // SELECT ALL
				    if (ke.getKeyCode() == KeyEvent.VK_A) {
				    	jTextComponent.selectAll();
				    	ke.consume();
				    	return;
				    }

				    // UNDO OR REDO
				    if (ke.getKeyCode() == KeyEvent.VK_Z) {
				    	if (ke.isShiftDown()) // REDO
				    		redoOperationLogic();					    		
				    	else //UNDO
				    		undoOperationLogic();
				    	
				    	ke.consume();
				    	return;
			 	    }
				}
				
				// According the key pressed, do some actions or others
				switch (ke.getKeyCode())
				{
					case KeyEvent.VK_DELETE : // 'supr' key in spanish keyboard
						deleteTextLogic();
						ke.consume();
					break;
				}
			}
		};
	}

	/**
	 * Defines a mouse listener for the editor of the text component.
	 */
	private void defineEditorMouseListener() {
		editorMouseListener = new MouseAdapter() {
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			public void mouseClicked(MouseEvent e) {
	
				if (e.getButton() == MouseEvent.BUTTON3) {	            	
	            	// By default disable all options
	            	optionsEditionByMouse.setEnabledAllOptions(false);
	            	
	            	// Enable the "Undo" option if there is any previous state to restore
	            	if (undoManager.canUndo())
	            		optionsEditionByMouse.setEnabledUndoOption(true);
	            	
	            	// Enable the "Redo" option if there is any later state to restore
	            	if (undoManager.canRedo())
	            		optionsEditionByMouse.setEnabledRedoOption(true);
	            	
	            	// Enable the "Copy", "Cut" and "Delete" options if there is text selected
	            	if (jTextComponent.getCaretPosition() != jTextComponent.getCaret().getMark())
	            	{
	            		optionsEditionByMouse.setEnabledCopyOption(true);
	            		optionsEditionByMouse.setEnabledCutOption(true);
	            		optionsEditionByMouse.setEnabledDeleteOption(true);
	            	}
	            	
	            	// Enable the "Paste" option if there is text on the system's clipboard
	            	if ( jTextComponent.getToolkit().getSystemClipboard().getContents(this).toString().length() > 0 )
	            		optionsEditionByMouse.setEnabledPasteOption(true);//
	            	
	            	// Enable the "Select-All" option (by default it's always enabled)
	            	optionsEditionByMouse.setEnabledSelectAllOption(true);
	            	
					optionsEditionByMouse.setLocation((int)jTextComponent.getLocationOnScreen().getX() + e.getX(), (int)jTextComponent.getLocationOnScreen().getY() + e.getY());
	            	optionsEditionByMouse.setInvoker(jTextComponent);
	            	optionsEditionByMouse.setVisible(true);
	            }
	        }
		};
	}
	
	/**
	 * Configures the component and some of its parts
	 */
	private void configure() {
		// Configures the document of the editor of this component
		PlainDocument document = this.configureDocument();

		// Configures the editor (ComboBoxEditor) of this component
		configureEditor(document);
		
		// Configures the text-edition-options popupmenu
		configureOptionsEditionByMouse();
	}

	/**
	 * Configures the document of the editor of the text component
	 */
	private PlainDocument configureDocument() {
		// Creates the document of the editor of this component
		PlainDocument document = new PlainDocument();
        
		// Configures the document
		configureUndoManager(document);
		
		return document;
	}
	
	/**
	 * Configures the editor {@link ComboBoxEditor ComboBoxEditor} of the text component.
	 * 
	 * @param document the document to display/edit
	 */
	private void configureEditor(PlainDocument document) {
        if (jTextComponent != null) {
           	// Removes some previous listeners and adds some new others:     
        	
        	// Adds the new Key Listener (tries to remove it if it existed before)
        	jTextComponent.removeKeyListener(this.editorKeyListener);
        	jTextComponent.addKeyListener(this.editorKeyListener);
        	
           	// Adds the new Mouse Listener (tries to remove it if it existed before)
        	jTextComponent.removeMouseListener(this.editorMouseListener);
        	jTextComponent.addMouseListener(this.editorMouseListener);
        	
        	// Adds the new document (tries to remove it if it existed before)
        	jTextComponent.setDocument(document);
        }
	}

	/** 
	 * Configures the text-edition-options popup menu.
	 */
	private void configureOptionsEditionByMouse() {
		this.optionsEditionByMouse.addPropertyChangeListener(editionMenuListener);
	}
	
	/**
	 * Configures the UndoManager for Undo-Redo operations.
	 */
	private void configureUndoManager(PlainDocument document) {
        // Listen for undo and redo events
        document.addUndoableEditListener(new UndoableEditListener() {
        	/*
        	 * (non-Javadoc)
        	 * @see javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing.event.UndoableEditEvent)
        	 */
            public void undoableEditHappened(UndoableEditEvent evt) {
            	undoManager.addEdit(evt.getEdit());
            }
        });
	}
	
	/**
	 * This method has the logic for a "undo" operation.
	 */
	private void undoOperationLogic() {
		try {
            if (undoManager.canUndo()) {
            	String previousText = jTextComponent.getText();
            	undoManager.undo();
         	   	String newText = jTextComponent.getText();
         	   	fireUndoRedoEditUpdate(new UndoRedoEditEvent(jTextComponent, UndoRedoEditEvent.UNDO, previousText, newText));
            }
		} catch (CannotUndoException e) {
        	e.printStackTrace();
        }
	}
	
	/**
	 * This method has the logic for a "redo" operation.
	 */
	private void redoOperationLogic() {
        try {
            if (undoManager.canRedo()) {
            	String previousText = jTextComponent.getText();
         	   	undoManager.redo();
         	   	String newText = jTextComponent.getText();
         	   	fireUndoRedoEditUpdate(new UndoRedoEditEvent(jTextComponent, UndoRedoEditEvent.REDO, previousText, newText));
            }
	    } catch (CannotRedoException e) {
	    	e.printStackTrace();
	    }
	}

	////// OTHER METHODS //////

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#setUndoRedoLimitActions(int)
	 */
	public void setUndoRedoLimitActions(int limit) {
    	this.undoRedoLimitActions = limit;
    	undoManager.setLimit(undoRedoLimitActions);
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#getUndoRedoLimitActions()
	 */
	public int getUndoRedoLimitActions() {
    	return this.undoRedoLimitActions;
    }
     
	/**
	 * This method is invoked when some text has to be removed: With the 'Delete' option of the text-edition-popupmenu or with the 'delete' ('supr'
	 *    in spanish keyboard) key.
	 */
	private void deleteTextLogic() {
		//		 Get the new text:		
	  	try {
	  		PlainDocument document = (PlainDocument)jTextComponent.getDocument();
	  		int caretPosition = jTextComponent.getCaretPosition();
	  		int markPosition = jTextComponent.getCaret().getMark();
	  		  
	  		int min_index = Math.min(caretPosition, markPosition);
	  		int length = Math.abs(caretPosition - markPosition);
	  		  
	  		document.remove(min_index, length);
	  	} catch (BadLocationException e) {
	  		e.printStackTrace();
	  	}
	}
	
   /*
    * (non-Javadoc)
    * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#addUndoRedoEditListener(org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener)
    */
    public void addUndoRedoEditListener(UndoRedoEditListener listener) {
    	undoRedoEditListeners.add(UndoRedoEditListener.class, listener);
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#removeCaretListener(org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener)
     */
    public void removeUndoRedoEditListener(UndoRedoEditListener listener) {
    	undoRedoEditListeners.remove(UndoRedoEditListener.class, listener);
    }

    /*
     * (non-Javadoc)
     * @see org.gvsig.gui.beans.editabletextcomponent.IEditableText#getUndoRedoEditListeners()
     */
    public UndoRedoEditListener[] getUndoRedoEditListeners() {
        return (UndoRedoEditListener[])undoRedoEditListeners.getListeners(UndoRedoEditListener.class);
    }
    
    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.  The listener list is processed in a
     * last-to-first manner.
     *
     * @param e the event
     * @see EventListenerList
     */
    protected void fireUndoRedoEditUpdate(UndoRedoEditEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = undoRedoEditListeners.getListenerList();
        
    	if (listeners != null) {
    	    for (int i = 1; i < listeners.length; i+=2) {
    	    	if (listeners[i] instanceof UndoRedoEditListener) {
                  ((UndoRedoEditListener)listeners[i]).operationExecuted(e);
              }
    	    }
    	}
    }
    ////// END OTHER METHODS //////
}
