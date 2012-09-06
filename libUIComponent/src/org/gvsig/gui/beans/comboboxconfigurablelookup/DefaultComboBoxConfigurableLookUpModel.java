package org.gvsig.gui.beans.comboboxconfigurablelookup;
/* DefaultComboBoxModel.java --
   Copyright (C) 2002, 2004, 2005, 2006, Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

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

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.MutableComboBoxModel;

import org.gvsig.gui.beans.comboboxconfigurablelookup.agents.StartsWithLookUpAgent;

/**
 * <p>Re-implementation of {@link DefaultComboBoxModel} adapted for the behavior needed to look up items.</p>
 * 
 * <p>This model has configurable behaviour using flags.</p>
 * 
 * <p><code>DefaultComboBoxConfigurableLookUpModel</code> represents the model used by a {@link JComboBoxLookUpConfigurable JComboBoxLookUpConfigurable},
 *  according the MVC (Model-View-Controller) pattern.</p>
 * 
 * <p>Flags:
 * <ul>
 *  <li><b>ItemsOrder:</b> order in which items will be showed: maintain position (according the items were introduced); alphabetical order (according the local rules); or disordered.<br>
 *   + <b><i>MAINTAIN_POSITION</i></b>: in the same position as they are stored.<br>
 *   + <b><i>ALPHABETICAL_ORDERED</i></b>: in alphabetical order (according a local language rules).<br>
 *   + <b><i>MAINTAIN_AGENT_POSITIONS</i></b>: in the same position as the are returned by the look up agent.
 *  </li>   
 *  <li><b>ShowAllItemsInListBox:</b> show all items in list box always, or only items that match with the text written according the <code>ILookUp</code> agent.<br>
 *   + <b><i>SHOW_ALL_ITEMS</i></b>: shows all items of the model always.<br>
 *   + <b><i>SHOW_ONLY_MATCHES</i></b>: shows only the items , or only items that match with the text written according the <code>ILookUp</code> agent.
 *  </li>
 *  <li><b>LanguageRules:</b> language rules (es_ES, en_US, fr_FR, ...)</li>  
 *  <li><b>CaseSensitive:</b> consider or ignore the case sensitive looking up the items with the <code>ILookUp</code> agent.<br>
 *   + <b><i>CASE_SENSITIVE</i></b>: discriminates between lower and upper cases.<br>
 *   + <b><i>CASE_INSENSITIVE</i></b>: doesn't discriminate between lower and upper cases.
 *  </li>  
 * </ul>
 * </p>
 * 
 * <p>Default flag values are:
 *  <ul>
 *   <li><b>ItemsOrder:</b> <i>MAINTAIN_AGENT_POSITIONS</i>.</li>
 *   <li><b>ShowAllItemsInListBox:</b> <i>false (that's equal to ''SHOW_ONLY_MATCHES'').</li>
 *   <li><b>LanguageRules:</b> <i>en_EN (English from England)</i>.</li>
 *   <li><b>CaseSensitive:</b> <i>true (that's equal to ''CASE_SENSITIVE'')</i>.</li>
 *  </ul>
 * </p>
 * 
 * @see javax.swing.DefaultComboBoxModel
 * @see JComboBoxConfigurableLookUp
 *  
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @version 07/02/2008
 */
public class DefaultComboBoxConfigurableLookUpModel extends AbstractListModel implements ComboBoxModel, MutableComboBoxModel, Serializable, IComboBoxLookUp {
	private static final long serialVersionUID = 3514664640086791882L;

	/**
     * <p>Storage for the elements in the model's objects.</p>
     * 
     * <p>All items will be stored according they are introduced.</p>
     */
    private Vector<Object> objects;
 
    /**
     * <p>Stores the elements of 'objects' in alphabetical order.</p>
     * 
     * <p>This attribute will be used in the searches.</p>
     */
    private Vector<Object> alphabeticallyOrdered;
  
    /**
     * <p>The selected item (<code>null</code> indicates no selection).</p>
     */
    private Object selectedObject = null;
    
    ///// NEW ATTRIBUTES /////

	/**
	 * <p>Items that will be visible (displayed).</p>
	 */
	private List<Object> visibleList;

	/**
	 * <p>Text written.</p>
	 */
	private String textWritten;
	
	/**
	 * <p>Agent with the logic for looking up the items of this model, considering the text written.</p>
	 */
	private ILookUp lookUpAgent;
	
	/**
	 * <p>Object with utilities for managing vectors.</p>
	 */
	private VectorUtilities vectorUtilities;

	/**
	 * <p>Determines if will notify all events produced.</p>
	 */
	private boolean eventNotificationEnabled = true; // by default -> true

    ///// END NEW ATTRIBUTES /////
	
	// CONSTANTS FOR CONFIGURE THE BEHAVIOR
	public static final int MAINTAIN_POSITION = 0;
	public static final int ALPHABETICAL_ORDERED = 1;
	public static final int MAINTAIN_AGENT_POSITIONS = 2;
	public static final boolean SHOW_ALL_ITEMS = true;
	public static final boolean SHOW_ONLY_MATCHES = false;
	public static final boolean CASE_SENSITIVE = true;
	public static final boolean CASE_INSENSITIVE = false;
	
	public static final int DEFAULT_ITEMS_ORDER_CONFIGURATION = MAINTAIN_AGENT_POSITIONS;
	public static final boolean DEFAULT_SHOW_ALL_ITEMS_IN_LIST_BOX_CONFIGURATION = SHOW_ONLY_MATCHES;
	
	public static final String DEFAULT_LANGUAGE_RULES_CONFIGURATION = "en_EN";
	public static final boolean DEFAULT_CASE_SENSITIVE_CONFIGURATION = CASE_SENSITIVE;
	// END CONSTANTS FOR CONFIGURE THE BEHAVIOR
	
	// CONFIGURATION FLAGS
	/**
	 * <p>Flag: order in which items will be showed: maintain position (according the items were introduced); alphabetical order (according the local rules); or maintain the elements in
	 *  the positions the look up agent returned.</p>
	 */
	private int itemsOrder;
	
	/**
	 * <p>Flag: show all items in list box always, or only items that match with the text written according the <code>ILookUp</code> agent.</p>
	 */
	private boolean showAllItemsInListBox;

	/**
	 * <p>Flag: locale rules (es_ES, en_US, fr_FR, ...).</p>
	 */
	private String localeRules;

	/**
	 * <p>Object that allows compare strings. Stores a flag:</p>
	 * 
	 * <p>Flag: consider or ignore the case sensitive looking up the items that match with the text written according the <code>ILookUp</code> agent.</p>
	 */
	private StringComparator stringComparator;
	// END FLAGS

    /**
     * @see javax.swing.DefaultComboBoxModel#DefaultComboBoxModel()
     */
    public DefaultComboBoxConfigurableLookUpModel()
    {
    	objects = new Vector<Object>();
      
    	initialize();
    }
  
	/**
     * @see javax.swing.DefaultComboBoxModel#DefaultComboBoxModel(java.lang.Object[])
     */
    public DefaultComboBoxConfigurableLookUpModel(final Object items[])
    {
    	objects = new Vector<Object>(Arrays.asList(items));

    	initialize();
    }
  
	/**
     * @see javax.swing.DefaultComboBoxModel#DefaultComboBoxModel(java.util.Vector)
     */
    public DefaultComboBoxConfigurableLookUpModel(Vector<Object> vector)
    {
    	this.objects = new Vector<Object>(vector);
 
    	initialize();
    }
	
	///// REIMPLEMENTATION OF METHODS /////
 
    /**
     * @see javax.swing.DefaultComboBoxModel#addElement(java.lang.Object)
     */
	public void addElement(Object object)
	{
		objects.addElement(object);
		vectorUtilities.addAlphabeticallyOrdered(alphabeticallyOrdered, object, stringComparator);
     
		int index = objects.size() - 1;
		
		updateVisibleList();
 		
		if (eventNotificationEnabled)
			fireIntervalAdded(this, index, index);
 	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#removeElementAt(int)
	 */
	public void removeElementAt(int index)
	{
		if ( getElementAt( index ) == selectedObject )
		{
			if ( index == 0 ) {
				setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1) );
			}
			else {
				setSelectedItem( getElementAt( index - 1 ) );
			}
		}
		
		Object obj = objects.get(index);
		
		alphabeticallyOrdered.remove(obj);
		objects.removeElementAt(index);
		
		updateVisibleList();

		if (eventNotificationEnabled)
			fireIntervalRemoved(this, index, index);
	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#insertElementAt(java.lang.Object, int)
	 */
	public void insertElementAt(Object object, int index)
	{
		objects.insertElementAt(object, index);
		vectorUtilities.addAlphabeticallyOrdered(alphabeticallyOrdered, object, stringComparator);
		
		updateVisibleList();

		if (eventNotificationEnabled)
			fireIntervalAdded(this, index, index);
	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#removeElement(java.lang.Object)
	 */
	public void removeElement(Object object)
	{
		int index = getIndexOf(object);
		
		if (index != -1) {
			removeElementAt(index);
		}
	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#removeAllElements()
	 */
	public void removeAllElements()
	{
		selectedObject = null;
		int size = objects.size();

		if (size > 0)
		{
			objects.removeAllElements();
			alphabeticallyOrdered.removeAllElements();
			
			updateVisibleList();
	 		
			if (eventNotificationEnabled)
				fireIntervalRemoved(this, 0, size - 1);
		}
	}

	/**
	 * @see javax.swing.DefaultComboBoxModel#getSize()
	 */
	public int getSize()
	{		
		if (visibleList == null)
			return 0;
		else
			return visibleList.size();
	}
	
	/**
	 * @see javax.swing.DefaultComboBoxModel#setSelectedItem(java.lang.Object)
	 */
	public void setSelectedItem(Object anObject)
	{
		// No item is selected and object is null, so no change required.
		if (selectedObject == null && anObject == null)
			return;
		
		if ( ((selectedObject != null) && (!selectedObject.equals(anObject)))
				|| ((selectedObject == null) && (anObject != null)) ) {
		
			// Here we know that object is either an item in the objects or null.
	 
			// Handle the three change cases: selectedObject is null, object is
			// non-null; selectedObject is non-null, object is null;
			// selectedObject is non-null, object is non-null and they're not
			// equal.
			selectedObject = anObject;
			
			if (anObject == null)
				textWritten = null;
			else
				textWritten = anObject.toString();
			
			updateVisibleList();
			
			if (eventNotificationEnabled)
				fireContentsChanged(this, -1, -1);
		}
	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#getSelectedItem()
	 */
	public Object getSelectedItem()
	{
		return selectedObject;
	}
 
	/**
	 * @see javax.swing.DefaultComboBoxModel#getElementAt(int)
	 */
	public Object getElementAt(int index)
	{		
		if (visibleList == null)
			return null;
		
		if (index >= 0 && index < visibleList.size())
			return visibleList.get(index);
		else
			return null;
	}
	
	/**
	 * @see javax.swing.DefaultComboBoxModel#getIndexOf(java.lang.Object)
	 */
	public int getIndexOf(Object object)
	{
		if (visibleList == null)
			return -1;
    	
		return visibleList.indexOf(object);
	}

	///// END REIMPLEMENTATION OF METHODS /////    
    
	///// METHODS FOR THE BEHAVIOR FLAGS  /////
	/**
	 * <p>Returns the 'itemsOrder' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li>MAINTAIN_POSITION: all items will be shown in the order programmer did.</li>
	 *   <li>ALPHABETICAL_ORDERED: all items will be shown in alphabetical order.</li>
	 *   <li>MAINTAIN_AGENT_POSITIONS: all items will be shown in the order the look up agent did.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @return 'itemsOrder' configuration
	 */
	public int getItemsOrder() {
		return itemsOrder;
	}

	/**
	 * <p>Sets the 'itemsOrder' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li>MAINTAIN_POSITION: all items will be shown in the order programmer did.</li>
	 *   <li>ALPHABETICAL_ORDERED: all items will be shown in alphabetical order.</li>
	 *   <li>MAINTAIN_AGENT_POSITIONS: all items will be shown in the order the look up agent did.</li>
	 *  </ul>
	 * </p>
	 * 
	 * @param An integer value for 'itemsOrder' configuration flag
	 */
	public void setItemsOrder(int itemsOrder) {
		this.itemsOrder = itemsOrder;
	}
	
	/**
	 * <p>Returns the 'showAllItemsInListBox' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li>SHOW_ALL_ITEMS: shows all items in the list box.</li>
	 *   <li>SHOW_ONLY_MATCHES: shows only the items that match with the text written according the <code>ILookUp</code> agent.</li>
	 *  </ul> 
	 * </p>
	 * 
	 * @return 'showAllItemsInListBox' configuration
	 */
	public boolean isShowAllItemsInListBox() {
		return showAllItemsInListBox;
	}
	
	/**
	 * <p>Sets the 'showAllItemsInListBox' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li>SHOW_ALL_ITEMS: shows all items in the list box.</li>
	 *   <li>SHOW_ONLY_MATCHES: shows only the items that match with the text written according the <code>ILookUp</code> agent.</li>
	 *  </ul> 
	 * </p>
	 * 
	 * @param A boolean value for 'showAllItemsInListBox' configuration flag
	 */
	public void setShowAllItemsInListBox(boolean itemsShownInListBox) {
		this.showAllItemsInListBox = itemsShownInListBox;
	}
	
	/**
	 * <p>Returns the 'localeRules' configuration value of this component. The language code values could be consulted at: <br>
	 *  <i><a href="http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt">http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt</a></i><br>
	 *  <i><a href="http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html">http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html</a></i>
	 * </p>
	 * 
	 * @see {@Locale}
	 * 
	 * @return 'localeRules' configuration
	 */
	public String getLocaleRules() {
		return localeRules;
	}
	
	/**
	 * <p>Sets the 'localeRules' configuration value for this component. Configuration values are: <br>
	 *  <i><a href="http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt">http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt</a></i><br>
	 *  <i><a href="http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html">http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html</a></i>
	 * </p>
	 * 
	 * @see {@Locale}
	 * 
	 * @param An String value for 'localeRules' configuration flag
	 */
	public void setLocaleRules(String localeRules) {
		this.localeRules = localeRules;
		
		stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, Collator.getInstance(new Locale(this.localeRules))));
		Collections.sort(alphabeticallyOrdered.subList(0, alphabeticallyOrdered.size()), stringComparator); 
		
		updateVisibleList();
	}
	
	/**
	 * <p>Returns the 'caseSensitive' configuration value of this component. Configuration values are:
	 *  <ul>
	 *   <li>CASE_SENSITIVE: discriminates between small letters and capital letters.</li>
	 *   <li>CASE_INSENSITIVE: doesn't discriminates between small letters and capital letters.</li>
	 *  </ul> 
	 * </p>
	 *  
	 * @return 'caseSensitive' configuration
	 */
	public boolean isCaseSensitive() {
		return stringComparator.isCaseSensitive();
	}
	
	/**
	 * <p>Sets the 'caseSensitive' configuration value for this component. Configuration values are:
	 *  <ul>
	 *   <li>CASE_SENSITIVE: discriminates between small letters and capital letters.</li>
	 *   <li>CASE_INSENSITIVE: doesn't discriminates between small letters and capital letters.</li>
	 *  </ul> 
	 * </p>
	 *  
	 * @param A boolean value for 'caseSensitive' configuration flag
	 */
	public void setCaseSensitive(boolean caseSensitive) {
		stringComparator.setCaseSensitive(caseSensitive);
		Collections.sort(alphabeticallyOrdered.subList(0, alphabeticallyOrdered.size()), stringComparator); 

		updateVisibleList();
	}
	///// END METHODS FOR THE BEHAVIOR FLAGS  /////
    
   ///// NEW METHODS /////
   
	/**
	 * <p>Initializes some attributes.</p>
	 */
	protected void initialize() {
		textWritten = "";
		
		// Creates an object for doing ordering operations with vectors
		vectorUtilities = new VectorUtilities();
		
		// Creates the agent that will execute the look up on the items of this model
		lookUpAgent = new StartsWithLookUpAgent();
		
		// Initializes the StringComparator -> use spanish alphabetical rules, and case sensitive
		stringComparator = new StringComparator();
		stringComparator.setCaseSensitive(DEFAULT_CASE_SENSITIVE_CONFIGURATION);
		stringComparator.setLocaleRules(stringComparator.new LocaleRules(true, Collator.getInstance(new Locale(DEFAULT_LANGUAGE_RULES_CONFIGURATION))));
		
		// Set default flags configuration
		this.setDefaultBehaviorFlagsConfiguration();		
		
		alphabeticallyOrdered = new Vector<Object>(objects.subList(0, objects.size()));
		Collections.sort(alphabeticallyOrdered.subList(0, alphabeticallyOrdered.size()), stringComparator); // Use an algorithm of comparision which implements the rules of the language.
		
		visibleList = new ArrayList<Object>();
		updateVisibleList();
	}
	
	/**
	 * <p>Sets the default values of the flags.</p>
	 */
	protected void setDefaultBehaviorFlagsConfiguration() {
		itemsOrder = DEFAULT_ITEMS_ORDER_CONFIGURATION;
		showAllItemsInListBox = DEFAULT_SHOW_ALL_ITEMS_IN_LIST_BOX_CONFIGURATION;
		localeRules = DEFAULT_LANGUAGE_RULES_CONFIGURATION;
		stringComparator.setCaseSensitive(DEFAULT_CASE_SENSITIVE_CONFIGURATION);
	}

	/**
	 * <p>Returns all elements stored in this model, according the order they were added.</p>
	 * 
	 * @return data stored in this model.
	 */
	public Vector<Object> getData() {
		return objects;
	}

	/**
	 * <p>Gets the data stored in this model according the criterion of the <i>itemsOrder</i> parameter.</p>
	 * <p>Always returns all items stored.</p>
	 * 
	 * @return data stored in this model according the criterion of the <i>itemsOrder</i> parameter
	 */
	public Vector<Object> getDataAccordingItemsOrder() {
		switch(itemsOrder) {
			case MAINTAIN_POSITION:
				return objects;
	     	case ALPHABETICAL_ORDERED:
	     		// Do nothing because items are already alphabetically ordered
	     		return alphabeticallyOrdered;
	     	case MAINTAIN_AGENT_POSITIONS:
	     		List<Object> list;
	     		
	     		// "" forces to return all items stored
	    		if (stringComparator.isCaseSensitive()) {
	    			list = lookUpAgent.doLookUpConsideringCaseSensitive("", alphabeticallyOrdered, stringComparator);
	    		}
	    		else {
	    			list = lookUpAgent.doLookUpIgnoringCaseSensitive("", alphabeticallyOrdered, stringComparator);
	    		}
	    		
	    		return new Vector<Object>(list.subList(0, list.size()));
	     	default:
	     		// Default case: ALPHABETICAL_ORDERED
	     		// Do nothing because items are already alphabetically ordered
	     		return alphabeticallyOrdered;
		}
	}
	
	/**
	 * <p>Updates the list with the results of the look up by the agent, and its configuration.</p>
	 */
	protected void updateVisibleList() {
		if (!eventNotificationEnabled)
			return;
		
		if (objects.size() == 0) {
			visibleList = alphabeticallyOrdered.subList(0, 0);
			return;
		}
		
		if (showAllItemsInListBox) {
			switch (itemsOrder) {
				case MAINTAIN_POSITION:
					visibleList = objects.subList(0, objects.size());
		     		break;
		     	case ALPHABETICAL_ORDERED:
		     		visibleList = alphabeticallyOrdered.subList(0, alphabeticallyOrdered.size());
		     	    break;
		     	case MAINTAIN_AGENT_POSITIONS:
		     		// "" forces to return all items stored
		    		if (stringComparator.isCaseSensitive()) {
		    			visibleList = lookUpAgent.doLookUpConsideringCaseSensitive("", alphabeticallyOrdered, stringComparator);
		    		}
		    		else {
		    			visibleList = lookUpAgent.doLookUpIgnoringCaseSensitive("", alphabeticallyOrdered, stringComparator);
		    		}
		     		break;
		     	default:
		     		// Default case: ALPHABETICAL_ORDERED
		     		visibleList = alphabeticallyOrdered.subList(0, alphabeticallyOrdered.size());
			}

			if ((visibleList != null) && (visibleList.size() > 0)) {
				List<Object> list = lookUp();

				if ((list != null) && (list.size() > 0)) {
					selectedObject = list.get(0);
					textWritten = selectedObject.toString(); // ?
				}
				else {
					selectedObject = null;
				}
			}
		}
		else {
			visibleList = lookUp();
			
			switch (itemsOrder) {
				case MAINTAIN_POSITION:
					if (visibleList != null) {
						Vector<Object> aux = new Vector<Object>(objects.subList(0, objects.size()));
						int size = aux.size();
						Object obj = new Object();

						for (int i = (size - 1); i >= 0; i--) {
							obj = aux.get(i);

							if (visibleList.contains(obj)) {
								visibleList.remove(obj);
							}
							else {
								aux.remove(i);
							}
						}

						visibleList = aux.subList(0, aux.size());
					}
		     		break;
		     	case ALPHABETICAL_ORDERED:
		     		if (visibleList != null)
		     			Collections.sort(visibleList, stringComparator);
		     		break;
		     	case MAINTAIN_AGENT_POSITIONS:
		     		// Do nothing because items are already ordered
		     		break;
		     	default:
		     		// Default case: ALPHABETICAL_ORDERED
		     		// Do nothing because items are already alphabetically ordered
			}
		}
	}

	/**
	 * <p>Invokes the agent to execute its search on all items alphabetically sort ordered,
	 *  considering the <i>text written</i> and the <i>case sensitive</i> configuration. The agent will use a <p>Comparator</p> that
	 *  includes the <i>locale language rules</i> and the <i>case sensitive</i> in the rule that uses to compare.</p>.
	 * 
	 * @return java.util.List
	 */
	protected List<Object> lookUp() {
		if (stringComparator.isCaseSensitive()) {
			return lookUpAgent.doLookUpConsideringCaseSensitive(textWritten, alphabeticallyOrdered, stringComparator);
		}
		else {
			return lookUpAgent.doLookUpIgnoringCaseSensitive(textWritten, alphabeticallyOrdered, stringComparator);
		}
	}

	/**
	 * <p>Gets the agent used with the logic for looking up the items of this model, considering
	 *  the text written.</p>
	 *  
	 * @return the agent used with the logic for looking up the items of this model
	 */
	public ILookUp getLookUpAgent() {
		return lookUpAgent;
	}

	/**
	 * <p>Sets the agent with the logic for looking up the items of this model, considering
	 *  the text written.</p>
	 *  
	 * @param agent the agent used with the logic for looking up the items of this model
	 */
	public void setLookUpAgent(ILookUp agent) {
		lookUpAgent = agent;
		
		// Updates the list using the new agent
		updateVisibleList();
		
	}

	/**
	 * <p>Sets enabled or disabled the notification of events in this model.</p>
	 * 
	 * @param b <code>true</code> to enable, <code>false</code> to disable
	 */
	public void setEventNotificationEnabled(boolean b) {
		eventNotificationEnabled = b;
	}

	/**
	 * <p>Gets if the notification of events in this model is enabled.</p>
	 * 
	 * @return <code>true</code> is its enabled; <code>false</code> otherwise
	 */
	public boolean getEventNotificationEnabled() {
		return eventNotificationEnabled;
	}
	
	///// END NEW METHODS /////

	///// REIMPLEMENTATION OF METHODS OF THE INTERFACE IComboBoxLookUp /////

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.comboboxconfigurablelookup.IComboBoxLookUp#setTextWritten(java.lang.String)
	 */
	public void setTextWritten(String text) {
		textWritten = text;
		updateVisibleList();
	}

	///// END REIMPLEMENTATION OF METHODS OF THE INTERFACE IComboBoxLookUp /////
	
	/**
	 * <p>New functionality to work with vectors (of elements).</p>
	 *
	 * <p>This class is a copy of a class with the same name located in <i>libIverUtiles</i>.
	 * 
	 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
	 */
	public class VectorUtilities {
		/**
		 * <p>Creates a new instance of the class <code>VectorUtilities</code>.</p>
		 */
		public VectorUtilities() {
		}
		
		/**
		 * <p>Adds an item in alphabetical order.</p>
		 * 
		 * @param v java.util.Vector in alphabetical order.
		 * @param obj java.lang.Object
		 */
		public synchronized void addAlphabeticallyOrdered(Vector<Object> v, Object obj) {
			int size = v.size();
			int currentIteration = 0;
			int index, aux_index;
			int lowIndex = 0;
			int highIndex = size -1;
			int maxNumberOfIterations = (int) log2(size);
			
			// If there are no items
			if (size == 0) {
				v.add(obj);
				return;
			}
			
			while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
				index = ( lowIndex + highIndex ) / 2;

				// If the item in the index position has the same value as obj 
				if (v.get(index).toString().compareTo(obj.toString()) == 0) {
					v.add(index, obj);
					return;
				}

				// If the item in the index position has a lower value than the obj
				if (v.get(index).toString().compareTo(obj.toString()) < 0) {
					aux_index = index + 1;
					
					if ((aux_index) >= size) {
						v.add(v.size() , obj);
						return;
					}

					if (v.get(aux_index).toString().compareTo(obj.toString()) > 0) {
						v.add(aux_index, obj);
						return;
					}

					lowIndex = aux_index;
				}
				else {
					// If the item in the index position has a higher value than the obj
					if (v.get(index).toString().compareTo(obj.toString()) > 0) {
						aux_index = index - 1;
						
						if ((aux_index) < 0) {
							v.add(0 , obj);
							return;
						}
						
						if (v.get(aux_index).toString().compareTo(obj.toString()) < 0) {
							v.add(index, obj);
							return;
						}
						
						highIndex = aux_index;
					}
				}
				
				currentIteration ++;
			}		 
		}
		
		/**
		 * <p>Adds an item in alphabetic order using a comparator for compare the objects. The objects must be alhabetically ordered.</p>
		 *
		 * @param v java.util.Vector in alphabetical order.
		 * @param obj java.lang.Object
		 * @param comp java.util.Comparator
		 */
		public synchronized void addAlphabeticallyOrdered(Vector<Object> v, Object obj, Comparator<Object> comp) {
			int size = v.size();
			int currentIteration = 0;
			int index, aux_index;
			int lowIndex = 0;
			int highIndex = size -1;
			int maxNumberOfIterations = (int) log2(size);
			
			// If there are no items
			if (size == 0) {
				v.add(obj);
				return;
			}
			
			while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
				index = ( lowIndex + highIndex ) / 2;
				
				// If the item in the index position has the same value as obj 
				if (comp.compare(v.get(index), obj) == 0) {
					v.add(index, obj);
					return;
				}
				
				// If the item in the index position has a lower value than the obj
				if (comp.compare(v.get(index), obj) < 0) {
					aux_index = index + 1;
					
					if ((aux_index) >= size) {
						v.add(v.size() , obj);
						return;
					}
					
					if (comp.compare(v.get(aux_index), obj) > 0) {
						v.add(aux_index, obj);
						return;
					}
					
					lowIndex = aux_index;
				}
				else {
					// If the item in the index position has a higher value than the obj
					if (comp.compare(v.get(index), obj) > 0) {
						aux_index = index - 1;
						
						if ((aux_index) < 0) {
							v.add(0 , obj);
							return;
						}
						
						if (comp.compare(v.get(aux_index), obj) < 0) {
							v.add(index, obj);
							return;
						}
						
						highIndex = aux_index;
					}
				}
				
				currentIteration ++;
			}
		}

		///// MATHEMATICAL OPERATIONS /////
		/**
		 * <p>2-base logarithm of a</p>
		 * 
		 * @param a The value to do the calculations
		 * @return Logarithm in 2 base of 'a'
		 */
		public double log2(double a) {
			return (Math.log(a) / Math.log(2));
		}
		
		/**
		 * <p>2-base logarithm of a, but the result doesn't have decimals (approximated to the integer number most near by below)</p>
		 * 
		 * @param a The value to do the calculations
		 * @return Logarithm in 2 base of 'a', as an integer
		 */
		public int log2Integer(double a) {
			return (int) Math.floor(log2(a));
		}
		///// END MATHEMATICAL OPERATIONS /////
	}
}