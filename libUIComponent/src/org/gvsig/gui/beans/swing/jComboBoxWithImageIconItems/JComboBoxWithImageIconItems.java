package org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.gvsig.gui.beans.Messages;

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
 * A JComboBox which items are ImageIcons.<br>
 * 
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class JComboBoxWithImageIconItems extends JComboBox implements Serializable {
	private static final long serialVersionUID = -429261182461238958L;

	public static final int DEFAULT_ROW_COUNT = 3;

	private JComboBoxWithImageIconItems referenceToThisJComboBoxWithImageIconItems;

    /**
     * Default constructor
     */
    public JComboBoxWithImageIconItems() {
    	super();
    	initialize();
    }

    /**
     * Default constructor with a 'ComboBoxImageIconModel' as parameter
     * 
     * @param aModel org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems.JComboBoxWithImageIconItems.ComboBoxImageIconModel
     */
    public JComboBoxWithImageIconItems(ComboBoxImageIconModel aModel) {
    	super();
    	initialize();
    }

    /**
     * Default constructor with an array of 'ImageIconItemInfo' as parameters
     * 
     * @param items Each item must be an 'ImageIconItemInfo' object
     */
    public JComboBoxWithImageIconItems(Object[] items) {
    	super();
    	initialize();
    }

    /**
     * Default constructor with a Vector of 'ImageIconItemInfo' as parameter
     * 
     * @param items Each item must be an 'ImageIconItemInfo' object
     */
    public JComboBoxWithImageIconItems(Vector items) {
    	super();
    	initialize();
    }

    /**
     * Initialize this component
     */
    private void initialize() {
    	this.referenceToThisJComboBoxWithImageIconItems = this;
    	this.setModel(new ComboBoxImageIconModel());
		this.setRenderer(new ComboBoxImageIconRenderer());
		this.setMaximumRowCount(DEFAULT_ROW_COUNT);
    }

    /**
     * Adds an image icon URL path, image icon loaded (or null if not), tool tip text (tool tip text could be "") and if it's neccessary (it's optional)
     *   another associated value to this item (if there is no associated value, the 'item_Value' param should be null) 
     * 
     * @param imageIconPath A path
     * @param imageIcon An image icon loaded or null if haven't been loaded
     * @param imageIconToolTipText Text
     * @param item_Value Another extra value associated to the item added (null if no extra value)
     */
    public void addImageIconItem(String imageIconPath, ImageIcon imageIcon, String imageIconToolTipText, Object item_Value) {
    	// Load the image and stor it into the model of the JComboBox
    	this.addItem(new ImageIconItemInfo(imageIconPath, imageIcon, imageIconToolTipText, item_Value));
    }
    
    /**
     * Adds an image icon URL path, image icon loaded (or null if not), and tool tip text (tool tip text could be "")
     * 
     * @param imageIconItem Information for load an image icon and set its tool tip text
     */
    public void addImageIconItem(ImageIconItemInfo imageIconItem) {
    	// Load the image and stor it into the model of the JComboBox
    	this.addItem(imageIconItem);
    }
    
    /**
     * Adds an image icon URL path, tool tip text (tool tip text could be "") and the item value
     * If no item matches, does nothing
     * (The image icon parameter is ignored)
     * 
     * @param imageIconPath A path
     * @param imageIconToolTipText Text
     * @param item_Value An extra value
     */
    public void removeImageIconItem(String imageIconPath, String imageIconToolTipText, String item_Value) {
    	int i = 0;
    	ImageIconItemInfo iiii1;

    	while (i < this.getItemCount()) {
    		iiii1 = (ImageIconItemInfo)this.getItemAt(i);
    		if ((iiii1.getPath().compareTo(imageIconPath) == 0) &&
    				(iiii1.getToolTipText().compareTo(imageIconToolTipText) == 0) && 
    				(((iiii1.getItemValue() == null) && (item_Value == null)) || (iiii1.getItemValue().equals(item_Value)))) {
    			this.removeItemAt(i);
    			return; // Only remove the first match
    		}
    		else {
    			i ++;
    		}
    	}
    }

    /**
     * Removes an image icon URL path, tool tip text (tool tip text could be "") and the item value
     * If no item matches, does nothing
     * (The image icon parameter is ignored)
     * 
     * @param imageIconPath Information for remove the item
     */
    public void removeImageIconItem(ImageIconItemInfo imageIconItem) {
    	int i = 0;
    	ImageIconItemInfo iiii1;
    	
    	while (i < this.getItemCount()) {
    		iiii1 = (ImageIconItemInfo)this.getItemAt(i);
    		if ((iiii1.getPath().compareTo(imageIconItem.getPath()) == 0) &&
    				(iiii1.getToolTipText().compareTo(imageIconItem.getToolTipText()) == 0) && 
    				(((iiii1.getItemValue() == null) && (imageIconItem.getItemValue() == null)) || (iiii1.getItemValue().equals(imageIconItem.getItemValue())))) {
    			this.removeItemAt(i);
    			return; // Only remove the first match
    		}
    		else {
    			i ++;
    		}
    	}
    }

    /**
     * Removes all image icons
     */
    public void removeAllImageIconItems() {
    	this.removeAllItems();
    }

    /**
     * Sets some image icons that their information to load is stored in an array of ImageIconItemInfo
     * 
     * @param imagePaths An array of ImageIconItemInfo with the URL path, image icon loaded (or null if not), and tool tip text to each image
     */
    public void addImageIconItems(ImageIconItemInfo[] imageIconItems) {    	
    	// Load the images and store them into the model of the JComboBox
    	for (int i = 0; i < imageIconItems.length; i++) {
    		this.addItem(imageIconItems[i]);
    	}
    }

    /**
     * Sets some image icons that their information to load is stored in a Vector of ImageIconItemInfo
     * 
     * @param imagePaths A Vector of ImageIconItemInfo with the URL path, image icon loaded (or null if not), and tool tip text to each image
     */    
    public void addImageIconItems(Vector imageIconItems) {
    	// Load the images and store them into the model of the JComboBox
    	for (int i = 0; i < imageIconItems.size(); i++) {
    		this.addItem(imageIconItems.get(i));
    	}
    }

    /**
     * Removes some image icons that their path, tool tip text and item value are stored in an array
     * If no item matches, does nothing
     * (The image icon parameter is ignored)
     * 
     * @param imageIconPaths An array with the ImageIconItemInfo
     */
    public void removeImageIconItems(ImageIconItemInfo[] imageIconItems) {
       	int i, j;
       	ImageIconItemInfo iiii1, iiii2;

       	// For each item to remove
       	for (i = 0; i < imageIconItems.length; i++) {
       		j = 0;
       		iiii1 = (ImageIconItemInfo)imageIconItems[i];
       		// Finds first item that it's patch matches and removes it
	    	while (j < this.getItemCount()) {
	    		iiii2 = (ImageIconItemInfo)this.getItemAt(j);
	    		if ((iiii1.getPath().compareTo(iiii2.getPath()) == 0) &&
	    				(iiii1.getToolTipText().compareTo(iiii2.getToolTipText()) == 0) && 
	    				(((iiii1.getItemValue() == null) && (iiii2.getItemValue() == null)) || (iiii1.getItemValue().equals(iiii2.getItemValue())))) {
	    			this.removeItemAt(j);
	    			break; // Only remove the first match
	    		}
	    		else {
	    			j ++;
	    		}
	    	}
       	}
    }

    /**
     * Removes some image icons that their paths, tool tip text and item value are stored in a Vector
     * (The image icon parameter is ignored)
     * 
     * @param imageIconPaths A Vector with the ImageIconItemInfo
     */
    public void removeImageIconItems(Vector imageIconItems) {
       	int i, j;
       	ImageIconItemInfo iiii1, iiii2;

       	// For each item to remove
       	for (i = 0; i < imageIconItems.size(); i++) {
       		j = 0;
       		iiii1 = (ImageIconItemInfo)imageIconItems.get(i);
       		// Finds first item that it's patch matches and removes it
	    	while (j < this.getItemCount()) {
	    		iiii2 = (ImageIconItemInfo)this.getItemAt(j);
	    		if ((iiii1.getPath().compareTo(iiii2.getPath()) == 0) &&
	    				(iiii1.getToolTipText().compareTo(iiii2.getToolTipText()) == 0) && 
	    				(((iiii1.getItemValue() == null) && (iiii2.getItemValue() == null)) || (iiii1.getItemValue().equals(iiii2.getItemValue())))) {
	    			this.removeItemAt(j);
	    			break; // Only remove the first match
	    		}
	    		else {
	    			j ++;
	    		}
	    	}
       	}    	
    }
    
	/**
	 * @see ComboBoxImageIconModel#setShowImageIconAndTextProperty(boolean)
	 */
	public void setShowImageIconAndTextProperty(boolean b) {
		((ComboBoxImageIconModel)this.getModel()).setShowImageIconAndTextProperty(b);
	}
	
	/**
	 * @see ComboBoxImageIconModel#getShowImageIconAndTextProperty()
	 */
	public boolean getShowImageIconAndTextProperty() {
		return ((ComboBoxImageIconModel)this.getModel()).getShowImageIconAndTextProperty();
	}
    
	/**
	 * @see ComboBoxRenderer#setPreferredSize(Dimension)
	 */
	public void setJComboBoxRendererPreferredSize (Dimension d) {
		((ComboBoxImageIconRenderer)this.getRenderer()).setPreferredSize(d);
	}
	
	/**
	 * @see ComboBoxRenderer#getPreferredSize()
	 */
	public Dimension getJComboBoxRendererPreferredSize () {
		return ((ComboBoxImageIconRenderer)this.getRenderer()).getPreferredSize();
	}

    /**
     * This code has been extracted and modified from the Java Sun Microsystems's example: 'CustomComboBoxDemo.java'
     *   in http://java.sun.com/docs/books/tutorial/uiswing/components/combobox.html
     * 
     * @author Sun Microsystems
     * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
     */
    private class ComboBoxImageIconRenderer extends JLabel implements ListCellRenderer, Serializable {
		private static final long serialVersionUID = -7247546606502171953L;

		private Font uhOhFont;

    	/**
    	 * Default constructor
    	 */
    	public ComboBoxImageIconRenderer() {
    		setOpaque(true);
    		setHorizontalAlignment(CENTER);
    		setVerticalAlignment(CENTER);
    	}

    	/**
    	 * This method finds the image and text corresponding
    	 * to the selected value and returns the label, set up
    	 * to display the text and image.
    	 * 
    	 * @see ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
    	 */
    	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    		if (value != null) {
	    		if (isSelected) {
	    			setBackground(list.getSelectionBackground());
	    			setForeground(list.getSelectionForeground());
	    		} else {
	    			setBackground(list.getBackground());
	    			setForeground(list.getForeground());
	    		}
	    		
	    		ImageIcon icon = ((ImageIconItemInfo)value).getImageIcon();
	    		String item = ((ImageIconItemInfo)value).getPath();
	    		String toolTipText = ((ImageIconItemInfo)value).getToolTipText();
	    		
	    		setIcon(icon);
	    		
	    		if (toolTipText != null) {
	    			setToolTipText(toolTipText);
	    			referenceToThisJComboBoxWithImageIconItems.setToolTipText(toolTipText);
	    		}
	
	    		if (getShowImageIconAndTextProperty()) {
		    		if (icon != null) {
		    			setText(item);
		    			setFont(list.getFont());
		    		} else {
		    			setUhOhText(item + " (" + Messages.getText("no_image_available") + ")", list.getFont());
		    		}
	    		}
	    		else {
	    			 /* In this way we force to show anything in the Popup list 
		   			      in order to user knew that no item has been loaded */
		    		setText(" ");
	    		}
    		}
    		else {
    			// Empty previous information
    			setIcon(null);
    			setToolTipText("");
    			referenceToThisJComboBoxWithImageIconItems.setToolTipText("");
    			setText("");
    		}
    		
    		return this;
    	}

    	/**
    	 * Set the font and text when no image was found.
    	 * 
    	 * @param uhOhText Text
    	 * @param normalFont Font type
    	 */
    	private void setUhOhText(String uhOhText, Font normalFont) {
    		if (uhOhFont == null) { //lazily create this font
    			uhOhFont = normalFont.deriveFont(Font.ITALIC);
    		}

    		setFont(uhOhFont);
    		setText(uhOhText);
    	}
    }
}