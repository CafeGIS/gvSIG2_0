package org.gvsig.gui.beans.comboboxconfigurablelookup;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.accessibility.Accessible;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import org.gvsig.gui.beans.editabletextcomponent.IEditableText;
import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditEvent;
import org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener;


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


/** VERSION: BETA
 * <p>A configurable combo box component that lists items that carry out with the criterions of a <code>ILookUp</code> agent,
 *  considering the text in its inner editor.</p>
 * 
 * <p>Key features:
 *  <ul>
 *   <li>Allows configure it's behaviour.</li>
 * 	 <li>Inherits from JComboBox.</li>
 * 	 <li>Has 6 flags to configure its behaviour.</li>
 * 	 <li>It's made according the MVC (Model-View-Controller) pattern.</li>
 * 	 <li>Uses a {@link DefaultComboBoxConfigurableLookUpModel DefaultComboBoxConfigurableLookUpModel} as a dataModel, which it's also configurable.</li>
 *  </ul>
 * </p>
 * 
 * <p>Configuration flags:
 *  <ul>
 *   <li><b>OnlyOneColorOnText:</b> uses red if there is no item or only items that matches with the text written according the <code>ILookUp</code> agent, or uses always black color.</li>
 *   <li><b>BeepEnabled:</b> rings a beep sound if there is no item that matches with the text written according the <code>ILookUp</code> agent, or not.</li>
 *   <li><b>HidePopupIfThereAreNoItems:</b> hides popup (list box) if there is no item that matches with the text written according the <code>ILookUp</code> agent, or uses the default (JComboBox and more) behaviour.</li>
 *   <li><b>ToForceSelectAnItem:</b> forces to select an item (if the component looses the focus or user presses the Enter keyboard key), or uses the default (JComboBox and more) behaviour.</li>
 *   <li><b>CompleteArrowKeySelection:</b> writes in the editor the value of the element selected by the arrow keys (up or down) if its enabled, otherwise holds the previous text.</li>
 *   <li><b>DisplayAllItemsWithArrowButton:</b> <i>true</i>: displays all this component's dataModel items if its <i>popup</i> it's hided, and this component's dataModel listed only the matches
 *    <i>(flag: <code>DefaultComboBoxConfigurableLookUpModel.showAllItemsInListBox == false</code>)</i>; otherwise, if this flag isn't enabled,
 *    won't display all items in that situation.</li>
 *  </ul>
 * </p>
 * 
 * <p>Default flag values are:
 *  <ul>
 *   <li><b>OnlyOneColorOnText:</b> <i>false</i>.</li>
 *   <li><b>BeepEnabled:</b> <i>false</i>.</li>
 *   <li><b>HidePopupIfThereAreNoItems:</b> <i>true</i>.</li>
 *   <li><b>ToForceSelectAnItem:</b> <i>true</i>.</li>
 *   <li><b>CompleteArrowKeySelection:</b> <i>false</i>.</li>
 *   <li><b>DisplayAllItemsWithArrowButton:</b> <i>true</i>.</li>
 *  </ul>
 * </p>
 * 
 * <p><i>More information about the behaviour of it's dataModel in {@link DefaultComboBoxConfigurableLookUpModel DefaultComboBoxConfigurableLookUpModel} .</i></p>
 * 
 * @see JComboBox
 * @see DefaultComboBoxConfigurableLookUpModel
 * 
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 07/02/2008
 */
public class JComboBoxConfigurableLookUp extends JComboBox implements java.io.Serializable {


	// CONSTANTS FOR CONFIGURE THE BEHAVIOR
	public static final boolean DEFAULT_ONLY_ONE_COLOR_ON_TEXT_CONFIGURATION = false;
	public static final boolean DEFAULT_BEEP_ENABLED_CONFIGURATION = false;
	public static final boolean DEFAULT_HIDE_POPUP_IF_THERE_ARE_NO_ITEMS_CONFIGURATION = true;
	public static final boolean DEFAULT_TO_FORCE_SELECT_AN_ITEM_CONFIGURATION = true;
	public static final boolean DEFAULT_COMPLETE_ARROW_KEY_SELECTION_CONFIGURATION = false;
	public static final boolean DEFAULT_DISPLAY_ALL_ITEMS_WITH_ARROW_BUTTON_CONFIGURATION = true;
	// END CONSTANTS FOR CONFIGURE THE BEHAVIOR
	
	// EDITOR DOCUMENT REFERENCE
	/**
	 * <p>A reference to the document of this component.</p> 
	 */
	private PlainDocumentTextFormatter document;
	// END EDITOR DOCUMENT REFERENCE
	
	// NEW ATTRIBUTES 
	/**
	 * <p>A reference to the dataModel of this component (according to the MVC <i>(Model-View-Controller)</i> pattern).</p>
	 */
	private DefaultComboBoxConfigurableLookUpModel model;

	/**
	 * <p>Has or not to hide the popup on focus loss.</p>
	 */
	private boolean hidePopupOnFocusLoss;

	/**
	 * <p>Has an item of the popup been selected or not by the user.</p>
	 */
	private boolean popupItemSelected;
	
	/**
	 * <p>Determines if an arrow key (meanwhile down or up) has been pressed.</p>
	 */
	private boolean arrowKeyPressed;

	/**
	 * <p>Determines if the arrow button of the GUI has been clicked.</p>
	 */
	private boolean arrowButtonClicked;

	/**
	 * <p>Determines if is showing all items temporally in the GUI .</p>
	 */
	private boolean showingAllItemsTemporally;

	/**
	 * <p>Flag to ensure the we don't get multiple ActionEvents on item selection.</p>
	 */
	private boolean selectingItem = false;
	
	/**
	 * <p>Last item selected.</p>
	 */
	private Object previousSelected;

	/**
	 * <p>Determines if the UI of this component has been updated.</p>
	 * <p>This parameter is used by the listener that shows all items when user presses the <i>arrow button</i>
	 *  and its enabled the flag <code>displayAllItemsWithArrowButton</code>.</p>
	 */
	private boolean updatedUI = true;
	
	private boolean blockPopupHided = false;
	// END NEW ATTRIBUTES
	
	// LISTENERS
	/**
	 * <p>Listener for the editor key.</p> 
	 */
	private KeyListener editorKeyListener;
	
	/**
	 * <p>Listener for the editor focus.</p>
	 */
	private FocusListener editorFocusListener;
	
	/**
	 * <p>Listener for the popup menu.</p> 
	 */
	private PopupMenuListener popupMenuListener;
	
	/**
	 * <p>Listener for the arrow button.</p>
	 */
	private MouseListener arrowMouseListener;
	// END LISTENERS
	
	// CONFIGURATION FLAGS
	/**
	 * <p>Flag: use red if there is no item that matches with the text written according the <code>ILookUp</code> agent, or uses always black colour.</p>
	 */
	private boolean onlyOneColorOnText;
	
	/**
	 * <p>Flag: rings a beep sound if there is no item that matches with the text written according the <code>ILookUp</code> agent, otherwise no.</p>
	 */
	private boolean beepEnabled;
	
	/**
	 * <p>Flag: hide popup (list box) if there is no item that matches with the text written according the <code>ILookUp</code> agent, or uses the default (JComboBox and more) behaviour.</p>
	 */
	private boolean hidePopupIfThereAreNoItems;
	
	/**
	 * <p>Flag: forces to select an item (if the component looses the focus or user presses the Enter keyboard key), or uses the default (JComboBox and more) behaviour.</p>
	 */
	private boolean toForceSelectAnItem;

	/**
	 * <p>Flag: determines if has to write in the editor the value of the element selected by the 
	 * arrow keys (up or down) if its enabled, or hold the previous text if isn't.</p>
	 */
	private boolean completeArrowKeySelection;
	
	/**
	 * <p>Flag: displays all this component's dataModel items if its <i>popup</i> it's hided, this component's dataModel displayed only the matches, and this flag enabled; if this flag isn't enabled,
	 *  won't display all items in that situation.</p>
	 */
	private boolean displayAllItemsWithArrowButton;
	// END FLAGS
	
	/**
	 * <p>Default constructor without parameters.</p>
	 */
	public JComboBoxConfigurableLookUp() {
		super();
		initialize();
	}

	/**
	 * <p>Default constructor with a {@link DefaultComboBoxConfigurableLookUpModel} as parameter.</p>
	 * 
	 * @param aModel javax.swing.ComboBoxModel
	 */
	public JComboBoxConfigurableLookUp(DefaultComboBoxConfigurableLookUpModel aModel) {
		super(aModel);

		initialize();
	}	

	/**
	 * <p>Default constructor with an array of objects as parameter.</p>
	 * 
	 * @param items An array of objects. All them must implement a <i>'String toStrin()'</i> method
	 */
	public JComboBoxConfigurableLookUp(Object[] items) {
		super(items);

		initialize();
	}

	/**
	 * <p>Default constructor with a Vector of objects as parameter.</p>
	 * 
	 * @param items A {@link Vector} of objects. All them must implement a <i>'String toStrin()'</i> method
	 */
	public JComboBoxConfigurableLookUp(Vector<Object> items) {
		super(items);

		initialize();
	}
	
	///// NEW METHODS /////
	
	/**
	 * <p>This method sets the start values of inner attributes and creates the necessary inner objects.</p>
	 * 
	 * @param dataModel dataModel used by this combo box instance
	 */
	protected void initialize() {
		// By default user hasn't selected an item of the popup
		popupItemSelected = false;
		
		// By default no arrow key has been pressed
		arrowKeyPressed = false;
		
		// By default the arrow button of the GUI hasn't been clicked
		arrowButtonClicked = false;
		
		// By default isn't showing all items temporally in the GUI
		showingAllItemsTemporally = false;
		
		// By default no item has been selected
		previousSelected = null;

		// Set default flags configuration
		this.setDefaultBehaviorFlagsConfiguration();

		// Allows user to edit on the combobox
		super.setEditable(true);
				
		// Other configuration tasks
		configure();

		// If there are items -> select the first
		if ((toForceSelectAnItem) && (model.getData().size() > 0)) {
			model.setSelectedItem(model.getData().elementAt(0));
		}

		// Resets the UI property to a value from the current look and feel
		updateUI();
	}
	
	/**
	 * <p>Configures the component and some of its elements.</p>
	 */
	protected void configure() {
		// Defines a key listener for the editor of this component
		defineEditorKeyListener(this);

		// Defines a focus listener for the editor of this component
		defineEditorFocusListener(this);
				
		// Configures the document of the editor of this component
		configureDocument();
		
		// Configures the editor (ComboBoxEditor) of this component
		configureEditor(this.getEditor());
		
		// Configures the popup of this component
		configurePopUp(this);
	}
	
	/**
	 * <p>Configures the editor ( {@link ComboBoxEditor} ) of this component.</p>
	 * 
	 * @param newEditor The new editor to configure
	 */
	protected void configureEditor(ComboBoxEditor newEditor) {
		if (newEditor != null) {
			JTextComponent jTextComponentOfEditor = (JTextComponent) newEditor.getEditorComponent();

			// Adds the new document (tries to remove it if it existed before)
			jTextComponentOfEditor.setDocument(this.document);

			// Adds the new Key Listener (tries to remove it if it existed before)
			jTextComponentOfEditor.removeKeyListener(this.editorKeyListener);
			jTextComponentOfEditor.addKeyListener(this.editorKeyListener);

			// Adds the new Focus Listener (tries to remove it if it existed before)
			jTextComponentOfEditor.removeFocusListener(this.editorFocusListener);
			jTextComponentOfEditor.addFocusListener(this.editorFocusListener);
		}
	}

	/**
	 * <p>Configures the document of the editor of this component.</p>
	 */
	protected void configureDocument() {
		// Creates the document of the editor of this component
		document = new PlainDocumentTextFormatter();

		// Set reference to the container component
		document.setJComboBoxReference(this);
	}

	/**
	 * <p>Configures the popup of this component.</p>
	 *
	 * @param comboBox A reference of this component
	 */
	protected void configurePopUp(JComboBoxConfigurableLookUp comboBox) {
		final JComboBoxConfigurableLookUp comboBoxReference = comboBox;
		this.addPopupMenuListener(this.popupMenuListener);
				
		BasicComboBoxUI comboBoxUi = (BasicComboBoxUI)comboBoxReference.getUI();
		Accessible aC = comboBoxUi.getAccessibleChild(comboBoxReference, 0);

		if (aC instanceof ComboPopup) {
			JList jlist = ((ComboPopup)aC).getList();

			jlist.addMouseListener(new MouseAdapter() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
				 */
				public void mousePressed(MouseEvent e) {
					// User selects an item of the popup using the mouse
					popupItemSelected = true;
					System.out.println("Pulsado");
				}
			});
				
			if (aC instanceof BasicComboPopup) {
				((BasicComboPopup)aC).addPopupMenuListener(new PopupMenuListener() {
					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuCanceled(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuCanceled(PopupMenuEvent e) {
						if (arrowButtonClicked) {
							arrowButtonClicked = false;
						}
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
						if (arrowButtonClicked) {
							arrowButtonClicked = false;
						}
					}

					/*
					 * (non-Javadoc)
					 * @see javax.swing.event.PopupMenuListener#popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent)
					 */
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
						if (arrowButtonClicked) {
							arrowButtonClicked = false;
						}
					}
				});
			}
		}
	}

	/**
	 * <p>Returns the listener invoked when user presses the arrow button of this <code>JComboBox</code>.</p>
	 * 
	 * @return java.awt.event.MouseListener
	 */
	public MouseListener getArrowMouseListener() {
		if (arrowMouseListener == null) {
			// New mouse listener
			arrowMouseListener = new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
				 */
				public void mousePressed(MouseEvent e) {
					arrowButtonClicked = true;
					
					// Avoids a bug: when the component is created an shown, first time the arrow button is pressed, the method
					// #isPopupVisible() returns 'true' instead of returning 'false'
					boolean popupVisible;
					
					if (updatedUI) {
						popupVisible = false;
					}
					else {
						popupVisible = isPopupVisible();
					}
		
					// Shows all items temporally in the GUI
					if ((! model.isShowAllItemsInListBox()) && (displayAllItemsWithArrowButton) && (! popupVisible)) {
						showingAllItemsTemporally = true;
						
						// Stores the configuration of the flags	    	    	
						int itemsOrderFlag = model.getItemsOrder();
						String languageRules = model.getLocaleRules();
						boolean caseSensitive = model.isCaseSensitive();
						ILookUp lookUpAgent = model.getLookUpAgent();
						
						// Must replace the dataModel for update the data in the popup correctly
						setModel(new DefaultComboBoxConfigurableLookUpModel(model.getData()));
						
						// Restores the configuration of the flags
						model.setItemsOrder(itemsOrderFlag);
						model.setShowAllItemsInListBox(true);
						model.setLocaleRules(languageRules);
						model.setCaseSensitive(caseSensitive);
						model.setLookUpAgent(lookUpAgent);
		
						model.setTextWritten(document.textWritten);
						
						if (updatedUI) {
							updatedUI = false;
							showPopup();
						}
					}
				}
			};
		}
		
		return arrowMouseListener;
	}
	
	/**
	 * <p>Sets the default values of the flags.</p>
	 */
	protected void setDefaultBehaviorFlagsConfiguration() {
		onlyOneColorOnText = DEFAULT_ONLY_ONE_COLOR_ON_TEXT_CONFIGURATION;
		beepEnabled = DEFAULT_BEEP_ENABLED_CONFIGURATION;
		hidePopupIfThereAreNoItems = DEFAULT_HIDE_POPUP_IF_THERE_ARE_NO_ITEMS_CONFIGURATION;
		toForceSelectAnItem = DEFAULT_TO_FORCE_SELECT_AN_ITEM_CONFIGURATION;
		completeArrowKeySelection = DEFAULT_COMPLETE_ARROW_KEY_SELECTION_CONFIGURATION;
		displayAllItemsWithArrowButton = DEFAULT_DISPLAY_ALL_ITEMS_WITH_ARROW_BUTTON_CONFIGURATION;
	}
	
	/**
	 * <p>Defines a key listener for the editor of this component.</p>
	 * 
	 * <p><b>Warning:</b><i>This method is another important difference from the JComboBox; and could work badly with some key-maps.</i></p>
	 * 
	 * @param comboBox A reference of this component
	 */
	private void defineEditorKeyListener(JComboBoxConfigurableLookUp comboBox) {
		final JComboBoxConfigurableLookUp comboBoxReference = comboBox;
		
		editorKeyListener = new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			public void keyPressed(KeyEvent ke)  // Executed on the Start view state or Search view state
			{
				// Restore the status if was showing all items temporally
				if (showingAllItemsTemporally) {
					showingAllItemsTemporally = false;
					
					// Stores the configuration of the flags	    	    	
					int itemsOrderFlag = model.getItemsOrder();
					String languageRules = model.getLocaleRules();
					boolean caseSensitive = model.isCaseSensitive();
					ILookUp lookUpAgent = model.getLookUpAgent();
					
					// Must replace the dataModel for update the data in the popup correctly
					comboBoxReference.setModel(new DefaultComboBoxConfigurableLookUpModel(model.getData()));
					
					// Restores the configuration of the flags
					model.setItemsOrder(itemsOrderFlag);
					model.setShowAllItemsInListBox(false);
					model.setLocaleRules(languageRules);
					model.setCaseSensitive(caseSensitive);
					model.setLookUpAgent(lookUpAgent);
					model.setTextWritten(document.textWritten);
				}
				
				// According the key pressed, do some actions or others
				switch (ke.getKeyCode())
				{
					case KeyEvent.VK_ENTER :
						// Don't allow execute the default instructions because they have a bug (first time we remove some characters, no item will be displayed in the popup)
						ke.consume();

						// Sets the caret position of the text in the document to the end:
						((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setCaretPosition(document.getLength());

						final DefaultComboBoxConfigurableLookUpModel c_model = (DefaultComboBoxConfigurableLookUpModel)comboBoxReference.getModel();
						
						if (toForceSelectAnItem) {
							if (c_model.isShowAllItemsInListBox()) {
								// Select now the first item or the previous selected
								if (c_model.getSelectedItem() == null) {
									if (c_model.getData().size() > 0) {
										if (previousSelected == null)
											previousSelected = c_model.getDataAccordingItemsOrder().elementAt(0);
										else {
											((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
										}
									}
									else {
										previousSelected = null;
									}
								}
								else {								
									previousSelected = c_model.getSelectedItem();									
									comboBoxReference.setSelectedItem(previousSelected);
									
									((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
								}
							}
							else {
								// Select now the first item or the previous selected
								switch (comboBoxReference.getModel().getSize()) {
									case 0:
										if (previousSelected == null) {
											if (c_model.getData().size() > 0) {
												previousSelected = c_model.getDataAccordingItemsOrder().elementAt(0);
											}
											else {
												previousSelected = null;
											}
										}
										
										((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
										break;
									case 1:
										// If there is only one item -> select it
										previousSelected = comboBoxReference.getItemAt(0); // Select the first
										comboBoxReference.setSelectedIndex(0);
										break;
									default:
										if (previousSelected == null) {
											previousSelected = comboBoxReference.getItemAt(0); // Select the first
											comboBoxReference.setSelectedIndex(0);
										}
										else
											((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
								}
							}
						}						
						
						// Hide the popup
						comboBoxReference.hidePopup();
						
						// Force select one item
						if (completeArrowKeySelection) {
							model.setTextWritten(document.textWritten);
						}
						break;
						
					case KeyEvent.VK_UP: case KeyEvent.VK_DOWN:
						// User selects an item of the popup using the mouse
						popupItemSelected = true;
						arrowKeyPressed = true;
						break;
				}
			}
		};
	}
	
	/**
	 * <p>Defines a focus listener for the editor of this component.</p>
	 * 
	 * @param comboBox A reference of this component
	 */
	private void defineEditorFocusListener(JComboBoxConfigurableLookUp comboBox) {
		final JComboBoxConfigurableLookUp comboBoxReference = comboBox;
		
		// Bug 5100422 on Java 1.5: Editable JComboBox won't hide popup when tabbing out		
		hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
		
		// Highlight whole text when gaining focus
		editorFocusListener = new FocusAdapter() {
			/*
			 *  (non-Javadoc)
			 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				// Restore the status if was showing all items temporally
				if (showingAllItemsTemporally) {
					showingAllItemsTemporally = false;
					
					// Stores the configuration of the flags	    	    	
					int itemsOrderFlag = model.getItemsOrder();
					String languageRules = model.getLocaleRules();
					boolean caseSensitive = model.isCaseSensitive();
					ILookUp lookUpAgent = model.getLookUpAgent();
					
					// Must replace the dataModel for update the data in the popup correctly
					comboBoxReference.setModel(new DefaultComboBoxConfigurableLookUpModel(model.getData()));
					
					// Restores the configuration of the flags
					model.setItemsOrder(itemsOrderFlag);
					model.setShowAllItemsInListBox(false);
					model.setLocaleRules(languageRules);
					model.setCaseSensitive(caseSensitive);
					model.setLookUpAgent(lookUpAgent);
					model.setTextWritten(document.textWritten);
				}
				
				
				// Sets the caret position of the text in the document to the end:
				((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setCaretPosition(document.getLength());

				final DefaultComboBoxConfigurableLookUpModel c_model = (DefaultComboBoxConfigurableLookUpModel)comboBoxReference.getModel();

				if (toForceSelectAnItem) {
					if (c_model.isShowAllItemsInListBox()) {
						// Select now the first item or the previous selected
						if (c_model.getSelectedItem() == null) {
							if (c_model.getData().size() > 0) {
								if (previousSelected == null)
									previousSelected = c_model.getDataAccordingItemsOrder().elementAt(0);
								else {
									((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
								}
							}
							else {
								previousSelected = null;
							}
						}
						else {								
							previousSelected = c_model.getSelectedItem();									
							comboBoxReference.setSelectedItem(previousSelected);
						}

						((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
					}
					else {
						// Select now the first item or the previous selected
						switch (model.getData().size()) {
							case 0:
								if (previousSelected == null) {
									if (c_model.getData().size() > 0) {
										previousSelected = c_model.getDataAccordingItemsOrder().elementAt(0);

										((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
									}
									else {
										previousSelected = null;
									}
								}
								break;
							default:
								if (previousSelected == null) {
									previousSelected = comboBoxReference.getItemAt(0); // Select the first
									comboBoxReference.setSelectedIndex(0);
								}
								else
									((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setText(previousSelected.toString());
						}
					}
				}

				comboBoxReference.hidePopup();

				// Workaround for Bug 5100422 - Hide Popup on focus loss
				if (hidePopupOnFocusLoss)
					comboBoxReference.setPopupVisible(false);
			}			
		};
	}
	
	///// END NEW METHODS /////
	
	///// REIMPLEMENTATION OF METHODS OF 'JComboBox' /////
	
	/**
	 * <p>Sets the editor used to paint and edit the selected item in the 
     * <code>JComboBox</code> field.  The editor is used only if the
     * receiving <code>JComboBox</code> is editable. If not editable,
     * the combo box uses the renderer to paint the selected item.</p>
     * 
     * <p>The editor must be a <code>JTextComponent</code>, and its document a <code>PlainDocument</code> object.</p>
     *  
     * @param anEditor  the <code>ComboBoxEditor</code> that
     *			displays the selected item
     * @see #setRenderer
     * @beaninfo
     *     bound: true
     *    expert: true
     *  description: The editor that combo box uses to edit the current value
	 */
	public void setEditor(ComboBoxEditor anEditor) {
		if (anEditor == null) {
			super.setEditor(anEditor);
			return;
		}
		
		JTextComponent jTextComponentOfEditor = (JTextComponent) anEditor.getEditorComponent();

		// Adds the new ''PlainDocumentTextFormatter'', adding the listeners of the new editor's document
		PlainDocument old_document = ((PlainDocument)jTextComponentOfEditor.getDocument());
		
		DocumentListener[] documentListeners = old_document.getDocumentListeners();
		UndoableEditListener[] undoableEditListeners = old_document.getUndoableEditListeners();
		
		// Defines a key listener for the editor of this component
		defineEditorKeyListener(this);
		jTextComponentOfEditor.addKeyListener(editorKeyListener);
		
		// Defines a focus listener for the editor of this component
		defineEditorFocusListener(this);
		jTextComponentOfEditor.addFocusListener(editorFocusListener);
		
		document = new PlainDocumentTextFormatter();
				
		// Set reference to the container component
		document.setJComboBoxReference(this);
			
		// Removes all previous 'document' and 'undoableEdit' listeners
		DocumentListener[] removableDocumentListeners = document.getDocumentListeners();
		for (DocumentListener dL : removableDocumentListeners)
			document.removeDocumentListener(dL);

		UndoableEditListener[] removableUndoableEditListeners = document.getUndoableEditListeners();
		for (UndoableEditListener dUEL : removableUndoableEditListeners)
			document.removeUndoableEditListener(dUEL);

		for (DocumentListener rdL : documentListeners)
			document.addDocumentListener(rdL);
		
		// The undo-redo listeners fail
		for (UndoableEditListener uEL : undoableEditListeners) 
			document.addUndoableEditListener(uEL);
		
		if (undoableEditListeners.length > 0) {
			if (jTextComponentOfEditor instanceof IEditableText) {
				((IEditableText)jTextComponentOfEditor).addUndoRedoEditListener(new UndoRedoEditListener() {
					/*
					 * (non-Javadoc)
					 * @see org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditListener#operationExecuted(org.gvsig.gui.beans.editabletextcomponent.event.UndoRedoEditEvent)
					 */
					public void operationExecuted(UndoRedoEditEvent e) {
						model.setTextWritten(e.getNewText());
						document.updateOnlyTextColor();
					}
				});
			}
		}

		jTextComponentOfEditor.setDocument(document);
		
		super.setEditor(anEditor);
	}

	/*
	 *  (non-Javadoc)
	 * @see javax.swing.JComboBox#setModel(javax.swing.ComboBoxModel)
	 */
	public void setModel(ComboBoxModel aModel) {
		// If model isn't DefaultComboBoxConfigurableLookUpModel, sets as model a new DefaultComboBoxConfigurableLookUpModel, with the data
		//  of ''aModel'':
		if (! (aModel instanceof DefaultComboBoxConfigurableLookUpModel) ) {
			Vector<Object> data = new Vector<Object>(aModel.getSize());
			
			for (int i = 0; i < aModel.getSize(); i++) {
				data.add(aModel.getElementAt(i));
			}
			
			aModel = new DefaultComboBoxConfigurableLookUpModel(data);
		}
		
		super.setModel(aModel);
		
		model = (DefaultComboBoxConfigurableLookUpModel)super.getModel();
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.JComboBox#setSelectedIndex(int)
	 */
	public void setSelectedIndex(int anIndex) {
		if (anIndex < -1 || anIndex >= model.getSize())
			// Fails because index is out of bounds.
			throw new IllegalArgumentException("setSelectedIndex: " + anIndex + " out of bounds");
		else
			// Selects the item at the given index or clears the selection if the
			// index value is -1.
			setSelectedItem((anIndex == -1) ? null : model.getElementAt(anIndex));
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.JComboBox#setSelectedItem(java.lang.Object)
	 */
	public void setSelectedItem(Object anObject) {		
		if ((selectedItemReminder == null) || (!selectedItemReminder.equals(anObject))) {
			if (arrowKeyPressed) {
				
				selectingItem = true;
				model.setSelectedItem(anObject);
				selectingItem = false;
				
				if (model.getSize() > 0)
					previousSelected = anObject;
			}
			else {
				selectingItem = true;
				model.setSelectedItem(anObject);
				selectingItem = false;
				
				editor.setItem(anObject);
				
				if (model.getSize() > 0)
					previousSelected = anObject;
			}
			
			if (selectedItemReminder != model.getSelectedItem()) {
				// In case a users implementation of ComboBoxModel
				// doesn't fire a ListDataEvent when the selection
				// changes.
				selectedItemChanged();
			}
		}			
		fireActionEvent();
		document.updateOnlyTextColor();
	}

	/*
	 * <p>Unsupported operation. A <code>JComboBoxConfigurableLookUp</code> is always editable.</p>
	 *
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#setEditable(boolean)
	 */
	public void setEditable(boolean b) {
		throw new UnsupportedOperationException();
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#removeAllItems()
	 */
	public void removeAllItems() {
		if ( !(model instanceof MutableComboBoxModel) )
			throw new RuntimeException("Cannot use this method with a non-Mutable data dataModel.");

		//if ( model instanceof DefaultComboBoxConfigurableLookUpModel ) {
		model.setTextWritten("");
		((DefaultComboBoxConfigurableLookUpModel)model).removeAllElements();

		selectedItemReminder = null;

		if (isEditable()) {
			editor.setItem(null);
		}
		
		document.updateTextColorAndRingBeep();
	}
	
	public void addItem(Object anObject) {
		super.addItem(anObject);

		if ((toForceSelectAnItem) && (getSelectedIndex() == -1))
			setSelectedIndex(0);
		
		document.updateTextColorAndRingBeep();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#removeItem(java.lang.Object)
	 */
	public void removeItem(Object anObject) {
		super.removeItem(anObject);
		
		document.updateTextColorAndRingBeep();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#removeItemAt(int)
	 */
	public void removeItemAt(int anIndex) {
		super.removeItemAt(anIndex);
		
		document.updateTextColorAndRingBeep();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#setUI(javax.swing.plaf.ComponentUI)
	 */
	protected void setUI(ComponentUI newUI) {
		super.setUI(newUI);
		ComponentUI oldUI = newUI;
		
		// Remove the default key selection manager
		super.setKeySelectionManager(null);
		
        firePropertyChange("UI", oldUI, newUI);
        revalidate();
        repaint();
	}

	/*
	 * <p>Unsupported operation.</p>
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#selectWithKeyChar(char)
	 */
	public boolean selectWithKeyChar(char keyChar) {
		throw new UnsupportedOperationException();
	}

	/*
	 * <p>Unsupported operation.</p>
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#processKeyEvent(java.awt.event.KeyEvent)
	 */
	public void processKeyEvent(KeyEvent e) {
		throw new UnsupportedOperationException();
	}

	/*
	 * <p>Unsupported operation.</p>
	 * 
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#createDefaultKeySelectionManager()
	 */
	protected KeySelectionManager createDefaultKeySelectionManager() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#paramString()
	 */
	protected String paramString() {
		return super.paramString() +
		  "onlyOneColorOnText" + onlyOneColorOnText +
		  "beepEnabled" + beepEnabled +
		  "hidePopupIfThereAreNoItems" + hidePopupIfThereAreNoItems +
		  "toForceSelectAnItem" + toForceSelectAnItem +
		  "completeArrowKeySelection" + completeArrowKeySelection +
		  "displayAllItemsWithArrowButton" + displayAllItemsWithArrowButton +
		  "itemsOrder" + model.getItemsOrder() +
		  "showAllItemsInListBox" + model.isShowAllItemsInListBox() +
		  "localeRules" + model.getLocaleRules() +
		  "caseSensitive" + model.isCaseSensitive();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#updateUI()
	 */
	public void updateUI() {
		super.updateUI();
		
		// Force to hide the popup
		updatedUI = true;

		for (int i = 0; i < getComponentCount(); i++) {
			if (getComponent(i) instanceof JButton) {
				((JButton)getComponent(i)).addMouseListener(getArrowMouseListener());
				return;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComboBox#showPopup()
	 */
	public void showPopup() {
		if (this.isShowing() && (!blockPopupHided))
			setPopupVisible(true);
	}

	/**
	 * <p>Sets if the popup of this component will always remain hided, or can be shown when is invoked.</p>
	 * 
	 * @param b <code>true</code> if will remain blocked; <code>false</code> if can be shown 
	 */
	public void setBlockPopupHided(boolean b) {
		blockPopupHided = b;
	}
	

	/**
	 * <p>Determines if the popup of this component will always remain hided.</p>
	 * 
	 * @param b <code>true</code> if will remain blocked; <code>false</code> if can be shown 
	 */
	public boolean isBlockPopupHided() {
		return blockPopupHided;
	}
	///// END REIMPLEMENTATION OF METHODS OF 'JComboBox' /////    
	
	////// METHODS FOR THE BEHAVIOR FLAGS  //////
	/**
	 * <p>Returns the 'only_One_Color_On_Text' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: always uses black colour on text.</li>
	 *   <li><i>false</i>: by default uses black colour on text, but if text written by user doesn't match with any item according the <code>ILookUp</code> agent, text will be on red colour.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'only_One_Color_On_Text' configuration
	 */
	public boolean isOnlyOneColorOnText() {
		return onlyOneColorOnText;
	}
	
	/**
	 * <p>Sets the 'only_One_Color_On_Text' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: always uses black colour on text.</li>
	 *   <li><i>false</i>: by default uses black colour on text, but if text written by user doesn't match with any item according the <code>ILookUp</code> agent, text will be on red colour.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param b value for 'only_One_Color_On_Text' configuration flag
	 */
	public void setOnlyOneColorOnText(boolean b) {
		onlyOneColorOnText = b;
	}

	/**
	 * <p>Returns the 'beep_Enabled' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: a beep-sound is listened when no item matches with the text written according the <code>ILookUp</code> agent.</li>
	 *   <li><i>false</i>: no sound is listened.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'beep_Enabled' configuration
	 */
	public boolean isBeepEnabled() {
		return beepEnabled;
	}
	
	/**
	 * <p>Sets the 'beep_Enabled' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: a beep-sound is listened when no item matches with the text written according the <code>ILookUp</code> agent.</li>
	 *   <li><i>false</i>: no sound is listened.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param b value for 'beep_Enabled' configuration flag
	 */
	public void setBeepEnabled(boolean b) {
		beepEnabled = b;
	}	

	/**
	 * <p>Returns the 'hidePopupIfThereAreNoItems' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: hides the popup if there are no items when you write.</li>
	 *   <li><i>false</i>: default behaviour (if there are no items when you write, popup remains visible) .</li>
	 *  <ul>
	 * </p>
	 * 
	 * @return 'hidePopupIfThereAreNoItems' configuration
	 */
	public boolean isHidePopupIfThereAreNoItems() {
		return hidePopupIfThereAreNoItems;
	}
	
	/**
	 * <p>Sets the 'hidePopupIfThereAreNoItems' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: hides the popup if there are no items when you write.</li>
	 *   <li><i>false</i>: default behaviour (if there are no items when you write, popup remains visible) .</li>
	 *  <ul>
	 * </p>
	 * 
	 * @param b value for 'hidePopupIfThereAreNoItems' configuration flag
	 */
	public void setHidePopupIfThereAreNoItems(boolean b) {
		hidePopupIfThereAreNoItems = b;
	}

	/**
	 * <p>Returns the 'toForceSelectAnItem' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: selects the previous selected item or the first of the list when user presses the Enter key or when the component loses the focus .</li>
	 *   <li><i>false</i>: default behaviour .</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'toForceSelectAnItem' configuration
	 */
	public boolean isToForceSelectAnItem() {
		return toForceSelectAnItem;
	}
	
	/**
	 * <p>Sets the 'toForceSelectAnItem' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: selects the previous selected item or the first of the list when user presses the Enter key or when the component loses the focus .</li>
	 *   <li><i>false</i>: default behaviour .</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param value for 'toForceSelectAnItem' configuration flag
	 */
	public void setToForceSelectAnItem(boolean b) {
		toForceSelectAnItem = b;
	}

	/**
	 * <p>Returns the 'completeArrowKeySelection' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: writes in the editor the value of the element selected by the arrow keys (up or down).</li>
	 *   <li><i>false</i>: holds the previous text written .</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'completeArrowKeySelection' configuration
	 */
	public boolean isCompleteArrowKeySelection() {
		return completeArrowKeySelection;
	}

	/**
	 * <p>Sets the 'completeArrowKeySelection' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: writes in the editor the value of the element selected by the arrow keys (up or down).</li>
	 *   <li><i>false</i>: holds the previous text written .</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param b value for 'completeArrowKeySelection' configuration flag
	 */
	public void setCompleteArrowKeySelection(boolean b) {
		completeArrowKeySelection = b;
	}

	/**
	 * <p>Returns the 'displayAllItemsWithArrowButton' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: displays all this component's dataModel items if its <i>popup</i> it's hided, and this component's dataModel
	 *    listed only the matches <i>(flag: <code>DefaultComboBoxConfigurableLookUpModel.showAllItemsInListBox == false</code>)</i>.</li>
	 *   <li><i>false</i>: normal behaviour according the other <i>flags</i>.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'displayAllItemsWithArrowButton' configuration
	 */
	public boolean isDisplayAllItemsWithArrowButton() {
		return displayAllItemsWithArrowButton;
	}
	
	/**
	 * <p>Sets the 'toForceSelectAnItem' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li><i>true</i>: displays all this component's dataModel items if its <i>popup</i> it's hided, and this component's dataModel
	 *    listed only the matches <i>(flag: <code>DefaultComboBoxConfigurableLookUpModel.showAllItemsInListBox == false</code>)</i>.</li>
	 *   <li><i>false</i>: normal behaviour according the other <i>flags</i>.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param value for 'displayAllItemsWithArrowButton' configuration flag
	 */
	public void setDisplayAllItemsWithArrowButton(boolean b) {
		displayAllItemsWithArrowButton = b;
	}

	////// END METHODS FOR THE BEHAVIOR FLAGS  //////


	/**
	 * <p>Inner class that inherits of the class PlainDocument, and is used for manipulate the textWritten that has the document of the editor of this component.</p>
	 * 
	 * <p>This class is also optimized for items look up process.</p>
	 * 
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	public class PlainDocumentTextFormatter extends PlainDocument {
		private static final long serialVersionUID = -1858441733547527816L;
		private JComboBoxConfigurableLookUp comboBoxReference;
		private String textWritten;
		private boolean updatedModel;
		private String textOfReplacement;
		private boolean textInRedColor;
		private int old_caretPosition;
		private String completeArrowKeySelectionValue;

		/**
		 * <p>Default Constructor.</p>
		 */
		public PlainDocumentTextFormatter() {
			super();
			this.initialize();
		}
		
		/**
		 * <p>This method makes some initialize operations.</p> 
		 */
		protected void initialize() {
			textWritten = "";
			textOfReplacement = "";
			textInRedColor = false;
			completeArrowKeySelectionValue = null;
		}

		/**
		 * <p>Sets a reference of this component.</p>
		 * 
		 * @param comboBox A reference to the class that contains this.
		 */
		protected void setJComboBoxReference(JComboBoxConfigurableLookUp comboBox) {
			comboBoxReference = comboBox;
		}

		/*
		 *  (non-Javadoc)
		 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
		 */
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if (updatedModel) {
				model.setTextWritten(textWritten);
				super.insertString(offs, textWritten, a);
				
				return;
			}

			hidePopup();
			
			boolean caretToEnd = true;
			
			if (textWritten.length() > 0) {
				if (offs < textWritten.length()) {
					String old_textWritten = textWritten;
					
					// End of the text
					textWritten = textWritten.substring(offs, textWritten.length());
					
					// Beginning of the text
					if (offs > 0) {
						// Reset (without text written)
						textWritten = old_textWritten.substring(0, offs) + str + textWritten;
					}
					else {
						
						if (offs == 0) {
							caretToEnd = false;
						}
						
						textWritten = str + textWritten;
					}
				}
				else {
					textWritten = textWritten.substring(0, offs) + str;
				}
			}
			else {
				textWritten =  str;
			}
				
			if (completeArrowKeySelectionValue == null) {
				model.setTextWritten(textWritten);
			}
			else {
				model.setTextWritten(completeArrowKeySelectionValue);
			}
			
			super.insertString(offs, str, a);

			// Update the color of the text
			updateTextColorAndRingBeep();
		
			if (model.getSize() > 0) {
				if (isShowing()) {
					showPopup();
				}
			}
			else {
				if ((textWritten.compareTo("") != 0) && (!hidePopupIfThereAreNoItems)) {
					showPopup();	
				}
			}
			
			if (caretToEnd) {
				// Update the caret position -> at the same place it was or at the end
				updateCaretPosition(textWritten.length());
			}
			else {
				// Update the caret position -> at the same place it was or at the end
				updateCaretPosition(str.length());
			}
		}
		
		/*
		 *  (non-Javadoc)
		 * @see javax.swing.text.AbstractDocument#replace(int, int, java.lang.String, javax.swing.text.AttributeSet)
		 */
		public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			String old_textWritten;

			if (arrowKeyPressed) {
				if (!model.isShowAllItemsInListBox()) {
					if (completeArrowKeySelection) {
						String currentText = ((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).getText();
						
						old_textWritten = textWritten;
						old_caretPosition = ((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).getCaretPosition() + text.length();
								
						textWritten = "";

						super.remove(0, old_textWritten.length());
		
						if ((completeArrowKeySelectionValue == null) || (!currentText.equals(old_textWritten))) 
							completeArrowKeySelectionValue = old_textWritten;

						insertString(0, text, null);
					}
					else {
						insertString(0, "", null);
					}
				}

				previousSelected = text;
				arrowKeyPressed = false;

				return;
			}

			completeArrowKeySelectionValue = null;
			
			// Avoid two replaces for the same operation
			if ((text.compareTo("") == 0) && (length == textWritten.length()))
				return;
			
			// Only remove if there is text to be replaced (text selected)
			if (length > 0) {

				textOfReplacement = text;
				remove(offset, length);
			}
			else {
				if ((offset > 0) && (offset != textWritten.length())) {
					// Must replace the dataModel for update the data in the popup correctly
						
					// There is a bug that when text has been removed, the elements in the popup aren seen.
					// The solution to this bug is add them as a new dataModel
					hidePopup();

					updatedModel = true;

					old_textWritten = textWritten;
					old_caretPosition = ((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).getCaretPosition() + text.length();
					
					textWritten = "";
					model.setTextWritten(textWritten);

					super.remove(0, old_textWritten.length());
				
					// Stores the configuration of the flags	    	    	
					int itemsOrderFlag = model.getItemsOrder();
					boolean showAllItemsInListBox = model.isShowAllItemsInListBox();
					String languageRules = model.getLocaleRules();
					boolean caseSensitive = model.isCaseSensitive();
					ILookUp lookUpAgent = model.getLookUpAgent();
					
					// Must replace the dataModel for update the data in the popup correctly
					comboBoxReference.setModel(new DefaultComboBoxConfigurableLookUpModel(model.getData()));
					
					// Restores the configuration of the flags
					model.setItemsOrder(itemsOrderFlag);
					model.setShowAllItemsInListBox(showAllItemsInListBox);
					model.setLocaleRules(languageRules);
					model.setCaseSensitive(caseSensitive);
					model.setLookUpAgent(lookUpAgent);
					
					// Reset (without text written)
					insertString(0, textWritten, null);
					
					// Insert the new text
					textWritten = old_textWritten.substring(0, offset) + text + old_textWritten.substring(offset, old_textWritten.length());
					
					insertString(0, textWritten, null);

					updatedModel = false;

					// Update the color of the text
					updateTextColorAndRingBeep();
					
					// Update the caret position -> at the same place it was or at the end
					updateCaretPosition(old_caretPosition);
					
					// Only show the popup if there are items to show
					if (model.getSize() > 0) {
						 showPopup();
					}
					else {
						if ((textWritten.compareTo("") != 0) && (!hidePopupIfThereAreNoItems)) {
							showPopup();	
						}
					}
				}
				else {
					// Default replacement
					super.replace(offset, length, text, attrs);
				}
			}
		}
			
		/*
		 *  (non-Javadoc)
		 * @see javax.swing.text.Document#remove(int, int)
		 */
		public void remove(int offs, int len) throws BadLocationException {
			String old_textWritten;
				
			// There is a bug that when text has been removed, the elements in the popup aren seen.
			// The solution to this bug is add them as a new dataModel
			hidePopup();

			updatedModel = true;

			old_textWritten = textWritten;
			
			old_caretPosition = Math.min(((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).getCaretPosition(), offs);
			
			textWritten = "";
			model.setTextWritten(textWritten);

			super.remove(0, old_textWritten.length());
			
			int itemsOrderFlag = model.getItemsOrder();
			boolean showAllItemsInListBox = model.isShowAllItemsInListBox();
			String languageRules = model.getLocaleRules();
			boolean caseSensitive = model.isCaseSensitive();
			ILookUp lookUpAgent = model.getLookUpAgent();
		
			// Must replace the dataModel for update the data in the popup correctly
			comboBoxReference.setModel(new DefaultComboBoxConfigurableLookUpModel(model.getData()));
			
			// Restores the configuration of the flags
			model.setItemsOrder(itemsOrderFlag);
			model.setShowAllItemsInListBox(showAllItemsInListBox);
			model.setLocaleRules(languageRules);
			model.setCaseSensitive(caseSensitive);
			model.setLookUpAgent(lookUpAgent);

			// Reset (without text written)
			insertString(0, textWritten, null);
			
			// Insert the new text
			textWritten = old_textWritten.substring(0, offs) + textOfReplacement + old_textWritten.substring(offs + len, old_textWritten.length());

			insertString(0, textWritten, null);
			
			updatedModel = false;
			
			// Update the color of the text
			updateTextColorAndRingBeep();
			
			// Only show the popup if there are items to show
			if (model.getSize() > 0) {
				 showPopup();
			}
			else {
				if ((textWritten.compareTo("") != 0) && (!hidePopupIfThereAreNoItems)) {
					showPopup();	
				}
			}
	
//			if (old_caretPosition == 0) {
//				// Set the caret position to the end of the text
//				updateCaretPosition(textWritten.length());
//			}
//			else {
				// Update the caret position -> at the same place it was or at the end
				updateCaretPosition(old_caretPosition + textOfReplacement.length());
//			}
			
			textOfReplacement = "";
		}

		/**
		 * <p>Updates the color of the text in the {@link ComboBoxEditor} of this component, and
		 *  rings a beep if there is no item.</p>
		 * 
		 * <ul>
		 *  <li><i>Text in <u>red color</u></i>: if no item matches with the text written according the <code>ILookUp</code> agent.</li>
		 *  <li><i>Text in <u>black color</u></i>: if there is at least one item matches with the text written according the <code>ILookUp</code> agent.</li>
		 * </ul>
		 */
		public void updateTextColorAndRingBeep() {
			if (! comboBoxReference.isOnlyOneColorOnText()) {
				if (model.isShowAllItemsInListBox()) {
					// Set text into red color if no items matches with the text written, or in black color if it was in red
					//   color and now there is almost one element that starts with the characteres written by the user
					if (textWritten.compareTo("") != 0) {
						if (model.getSelectedItem() == null) {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.RED);
							textInRedColor = true;
								 
								 // Rings a beep if allowed
						if (beepEnabled)
							comboBoxReference.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
						}
						else {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.BLACK);
							textInRedColor = false;
						}
					}
					else {
						if (textInRedColor) {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.BLACK);
							textInRedColor = false;
						}
					}
				}
				else {
					// Set text into red color if no items matches with the text written, or in black color if it was in red
					//   color and now there is almost one element that starts with the characteres written by the user
					if (textWritten.compareTo("") != 0) {
						if (model.getSize() == 0) {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.RED);
							textInRedColor = true;
								 
								 // Rings a beep if allowed
						if (beepEnabled)
							comboBoxReference.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
						}
						else {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.BLACK);
							textInRedColor = false;
						}
					}
					else {
						if (textInRedColor) {
							comboBoxReference.getEditor().getEditorComponent().setForeground(Color.BLACK);
							textInRedColor = false;
						}
					}
				}
			}
			else {
				// Rings a beep if no there is no item, and if it's allowed
				if ((textWritten.compareTo("") != 0) && (model.getSize() == 0)) {
					if (beepEnabled)
						comboBoxReference.getToolkit().beep(); // when available use: UIManager.getLookAndFeel().provideErrorFeedback(comboBox);
				}
			}
		}
		
		/**
		 * <p>Updates the position of the caret in the {@link ComboBoxEditor} of this component.</p>
		 * 
		 * @param position the new position of the caret.
		 */
		public void updateCaretPosition(int position) {
			// If user has selected an item of the popup (using the mouse) -> set the caret position to the end of the text
			if (popupItemSelected) {
				((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setCaretPosition(textWritten.length());
				popupItemSelected = false;
				return;
			}
			
			if (position > textWritten.length())
				((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setCaretPosition(textWritten.length());
			else {
				((JTextComponent) comboBoxReference.getEditor().getEditorComponent()).setCaretPosition(position);
			}
		}

		/**
		 * <p>Updates the color of the text in this document.</p>
		 * 
		 * <p>Sets text to red color if there is no item in the visible list that the <i>look up</i> agent
		 *  returned with that text and the items of the dataModel, and, being enabled the second color in
		 *  the configuration the the <code>JcomboBoxConfigurableLookUp</code> agent. Otherwise, the foreground color
		 *  of the text will be black.</p>
		 * 
		 * <p>This method disables the <i>beep</i> during the process of updating the color of the text, and
		 *  restores it after.</p>
		 */
		public void updateOnlyTextColor() {
			boolean isBeepEnabled = isBeepEnabled();
			setBeepEnabled(false);
			
			updateTextColorAndRingBeep();
				
			setBeepEnabled(isBeepEnabled);
		}
	}
}