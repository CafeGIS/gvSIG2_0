package org.gvsig.gui.beans.filterPanel.filterQueryPanel.jLabelAsCell;

import java.io.Serializable;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

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


/**
 * This class reimplements the "contains()" method of "DefaultListModel" for 
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 *
 */
public class DefaultListModelForJLabelAsCell extends DefaultListModel implements Serializable {
	private static final long serialVersionUID = 4410627738805243864L;

	/**
	 * Represents the start index of the group of not loaded values (that should be after the group of loaded values) 
	 * -2                 -> if elements aren't grouped (in two lists: first for loaded, after for not loaded)
	 * -1                 -> if there isn't elements
	 *  0                 -> if there are only not loaded elements  
	 *  = (capacity() -1) -> if there are only loaded elements
	 */
	private int startIndexOfNotLoadedValues; 
	
	/**
	 * Default constructor without parameters
	 */
	public DefaultListModelForJLabelAsCell() {
		super();
		startIndexOfNotLoadedValues = -1;
	}
	
	/**
	 * Method that returns true if there is a inner JLabel that has as inner text the same text as 'patternText' 
	 * 
	 * @param patternText
	 * @return A boolean value
	 */
	public boolean containsJLabelText(String patternText) {
		for (int i = 0; i < super.getSize(); i++) {
			if (((JLabel)super.get(i)).getText().compareTo(patternText) == 0)
				return true;
		}				
			
		return false;
	}
	
	/**
	 * Returns the position of first JLabel that has as text the same text as 'patternText', or -1 if no JLabel has that text
	 * 
	 * @param patternText An String
	 * @return An integer value
	 */
	public int getIndexOfJLabelText(String patternText) {
		for (int i = 0; i < super.getSize(); i++) {
			if (((JLabel)super.get(i)).getText().compareTo(patternText) == 0)
				return i;
		}				
			
		return -1; // If not found
	}
	
	/**
	 * Replaces first element which has the text 'patternText' to a JLabelValueLoaded new one element with the same text
	 * 
	 * @param patternText Text of the component
	 */
	public void changeElementThatHasTextToJLabelLoadedValue(String patternText) {
		int index = getIndexOfJLabelText(patternText);
		
		if (index != -1)
			super.set(index, new JLabelAsCellValueLoaded(patternText));
	}
	
	/**
	 * Sets all elements to 'JLabelValueNotLoaded'
	 */
	public void setAllElementsToNotLoaded() {
		if (startIndexOfNotLoadedValues == -1) {
			for (int i = 0; i < super.getSize(); i++) {
				if (super.get(i) instanceof JLabelAsCellValueLoaded) {
					JLabelAsCellValueNotLoaded jL = new JLabelAsCellValueNotLoaded(((JLabelAsCellValueLoaded)super.get(i)).getText());
					super.remove(i);
					super.add(i, jL);///// REIMPLEMENTATION OF SOME METHODS OF 'DefaultListModel' /////
				}
			}
		}
		else {
			for (int i = 0; i < startIndexOfNotLoadedValues; i++) {
				if (super.get(i) instanceof JLabelAsCellValueLoaded) {
					JLabelAsCellValueNotLoaded jL = new JLabelAsCellValueNotLoaded(((JLabelAsCellValueLoaded)super.get(i)).getText());
					super.set(i, jL); // replacement
				}
			}
		}
		
		startIndexOfNotLoadedValues = 0;
	}
	
	///// REIMPLEMENTATION OF SOME METHODS OF 'DefaultListModel' /////
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#clear()
	 */		
	public void clear() {
		super.clear();
		startIndexOfNotLoadedValues = -1;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#insertElementAt(java.lang.Object, int)
	 */
	public void insertElementAt(Object obj, int index) {
		super.insertElementAt(obj, index);
		
		if ((obj instanceof JLabelAsCellValueNotLoaded) && (index > -1) && (index < startIndexOfNotLoadedValues))
			startIndexOfNotLoadedValues = index;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#remove(int)
	 */
	public Object remove(int index) {
		if ( (index > -1) && (index == startIndexOfNotLoadedValues) && (index == (super.capacity() -1)) ) {
			startIndexOfNotLoadedValues = -1;
		}

		return super.remove(index);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#removeAllElements()
	 */
	public void	removeAllElements() {
		super.removeAllElements();
		startIndexOfNotLoadedValues = -1;
	}
	 
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#removeElement(java.lang.Object)
	 */
	public boolean removeElement(Object obj) {
		int index = super.indexOf(obj);
		
		if ( (index > -1) && (index == startIndexOfNotLoadedValues) && (index == (super.capacity() -1)) ) {
			startIndexOfNotLoadedValues = -1;
		}
		
		return super.removeElement(obj);
	}
	 
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#removeElementAt(int)
	 */
	public void removeElementAt(int index) {
		if ( (index > -1) && (index == startIndexOfNotLoadedValues) && (index == (super.capacity() -1)) ) {
			startIndexOfNotLoadedValues = -1;
		}
		
		super.removeElementAt(index);			
	}
	 
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#removeRange(int, int)
	 */
	public void removeRange(int fromIndex, int toIndex) {			
		super.removeRange(fromIndex, toIndex);
		
		// Try to find any element of type 'JLabelNotLoadedValue'
		for (int i = 0; i < super.capacity(); i++) {
			if (super.get(i) instanceof JLabelAsCellValueNotLoaded) {
				if ( ( (i == (super.capacity() -1) || (i < (super.capacity() -1)) && (super.get(super.capacity() -1) instanceof JLabelAsCellValueNotLoaded) ) ) )
					startIndexOfNotLoadedValues = i;
				else
					startIndexOfNotLoadedValues = -1;
			}
		}
	}
	 
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#set(int, java.lang.Object)
	 */
	public Object set(int index, Object element) {
		if ((element instanceof JLabelAsCellValueNotLoaded) && (index > -1) && (index < startIndexOfNotLoadedValues))
			startIndexOfNotLoadedValues = index;

		return super.set(index, element);
	}
	
	/*
	 *  (non-Javadoc)
	 * @see javax.swing.DefaultListModel#setElementAt(java.lang.Object, int)
	 */
	public void setElementAt(Object obj, int index) {
		super.setElementAt(obj, index);

		if ((obj instanceof JLabelAsCellValueNotLoaded) && (index > -1) && (index < startIndexOfNotLoadedValues))
			startIndexOfNotLoadedValues = index;
	}

	///// END REIMPLEMENTATION OF SOME METHODS OF 'DefaultListModel' /////		
}