package org.gvsig.gui.beans.editionpopupmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

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
 * <p>This class is a JPopupMenu that can be used with another component.</p>
 * <p>The items showed in this menu are for editing: UNDO, REDO, CUT, COPY, PASTE, DELETE and SELECT ALL.</p>
 * <p>When the user clicks on a item, this Component fires a property change event that its value allows identify the item clicked.</p>
 * <p>(The icons used are from the open-source 'Tango Icon Library' project: (http://tango.freedesktop.org/Tango_Icon_Library)).</p>
 *  
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 */
public class JOptionsEditionByMousePopupMenu extends JPopupMenu implements Serializable{
	private static final long serialVersionUID = -142360699557710223L;

	// Items of this JPopupMenu
	private JMenuItem jMenuItemUndo = null;
	private JMenuItem jMenuItemRedo = null;
	private JMenuItem jMenuItemCut = null;
	private JMenuItem jMenuItemCopy = null;
	private JMenuItem jMenuItemPaste = null;
	private JMenuItem jMenuItemDelete = null;
	private JMenuItem jMenuItemSelectAll = null;
	
	// Values of the events fired when has been clicked an item
	public static final int DEFAULT = 0;
	public static final int UNDO = 1;
	public static final int REDO = 2;
	public static final int CUT = 3;
	public static final int COPY = 4;
	public static final int PASTE = 5;
	public static final int DELETE = 6;
	public static final int SELECT_ALL = 7;
	
	// Values of the type of event fired
	public static final int VISIBILITY = 8;
	public static final String SELECTEDOPTION = "SelectedOption";
	public static final String VISIBILITYCHANGED = "Visibility";
	
	// Hash map for the items
	private HashMap<String, String> map;

	// Action listener for notify (fire) some events that had happened to this component
	private ActionListener menuListener = null;

	// Listener for property change support
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

 	/**
	 * Default constructor 
	 */
	public JOptionsEditionByMousePopupMenu() {
		super();
		initialize();
	}

	/**
	 * This method initializes this component 
	 */
	private void initialize() {
        map = new HashMap<String, String>();
        this.add(getJMenuItemUndo());
        this.add(getJMenuItemRedo());
        this.addSeparator();
        this.add(getJMenuItemCut());
        this.add(getJMenuItemCopy());
        this.add(getJMenuItemPaste());
        this.add(getJMenuItemDelete());
        this.addSeparator();
        this.add(getJMenuItemSelectAll());
        this.setLabel("Edition");
        this.setBorder(new BevelBorder(BevelBorder.RAISED));
	}
	
	/**
	 * This method initializes jMenuItemUndo	
	 * 	
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getJMenuItemUndo() {
		if (jMenuItemUndo == null) {
			ImageIcon undoIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-undo.png"), Messages.getText("edit_undo"));
			jMenuItemUndo = new JMenuItem(Messages.getText("edit_undo"), undoIcon);
			jMenuItemUndo.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemUndo.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_undo"), Integer.toString(JOptionsEditionByMousePopupMenu.UNDO));
		}
		return jMenuItemUndo;
	}

	/**
	 * This method initializes jMenuItemRedo	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemRedo() {
		if (jMenuItemRedo == null) {
			ImageIcon redoIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-redo.png"), Messages.getText("edit_redo"));
			jMenuItemRedo = new JMenuItem(Messages.getText("edit_redo"), redoIcon);
			jMenuItemRedo.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemRedo.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_redo"), Integer.toString(JOptionsEditionByMousePopupMenu.REDO));
		}
		return jMenuItemRedo;
	}

	/**
	 * This method initializes jMenuItemCut	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemCut() {
		if (jMenuItemCut == null) {
			ImageIcon cutIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-cut.png"), Messages.getText("edit_cut"));
			jMenuItemCut = new JMenuItem(Messages.getText("edit_cut"), cutIcon);
			jMenuItemCut.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemCut.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_cut"), Integer.toString(JOptionsEditionByMousePopupMenu.CUT));
		}
		return jMenuItemCut;
	}
	
	/**
	 * This method initializes jMenuItemCopy	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemCopy() {
		if (jMenuItemCopy == null) {
			ImageIcon copyIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-copy.png"), Messages.getText("edit_copy"));
			jMenuItemCopy = new JMenuItem(Messages.getText("edit_copy"), copyIcon);
			jMenuItemCopy.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemCopy.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_copy"), Integer.toString(JOptionsEditionByMousePopupMenu.COPY));
		}
		return jMenuItemCopy;
	}

	/**
	 * This method initializes jMenuItemPaste	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemPaste() {
		if (jMenuItemPaste == null) {
			ImageIcon pasteIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-paste.png"), Messages.getText("edit_paste"));
			jMenuItemPaste = new JMenuItem(Messages.getText("edit_paste"), pasteIcon);
			jMenuItemPaste.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemPaste.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_paste"), Integer.toString(JOptionsEditionByMousePopupMenu.PASTE));
		}
		return jMenuItemPaste;
	}

	/**
	 * This method initializes jMenuItemDelete	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemDelete() {
		if (jMenuItemDelete == null) {
			ImageIcon deleteIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-delete.png"), Messages.getText("edit_delete"));
			jMenuItemDelete = new JMenuItem(Messages.getText("edit_delete"), deleteIcon);
			jMenuItemDelete.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemDelete.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_delete"), Integer.toString(JOptionsEditionByMousePopupMenu.DELETE));
		}
		return jMenuItemDelete;
	}	

	/**
	 * This method initializes jMenuItemSelectAll	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemSelectAll() {
		if (jMenuItemSelectAll == null) {
			ImageIcon selectAllIcon = new ImageIcon(JOptionsEditionByMousePopupMenu.class.getResource("images/edit-select-all.png"), Messages.getText("edit_select_all"));
			jMenuItemSelectAll = new JMenuItem(Messages.getText("edit_select_all"), selectAllIcon);
			jMenuItemSelectAll.setHorizontalTextPosition(JMenuItem.RIGHT);
			jMenuItemSelectAll.addActionListener(this.getMenuListener());
			map.put(Messages.getText("edit_select_all"), Integer.toString(JOptionsEditionByMousePopupMenu.SELECT_ALL));
		}
		return jMenuItemSelectAll;
	}
	
	/**
	 * This method initializes the "menuListener" ActionListener
	 * 
	 * @return ActionListener
	 */
	private ActionListener getMenuListener() {
		if (menuListener == null) {
		    menuListener = new ActionListener() {
		    	/*
		    	 * (non-Javadoc)
		    	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		    	 */
		        public void actionPerformed(ActionEvent event) {
		        	// Notifies that the Visibility of this component has changed
		        	if (event.getActionCommand().equals(JOptionsEditionByMousePopupMenu.VISIBILITYCHANGED))
		           		changes.firePropertyChange(JOptionsEditionByMousePopupMenu.VISIBILITYCHANGED, JOptionsEditionByMousePopupMenu.DEFAULT, JOptionsEditionByMousePopupMenu.VISIBILITY);
		         	else // Notifies that has been clicked on an item
		           		changes.firePropertyChange(JOptionsEditionByMousePopupMenu.SELECTEDOPTION, JOptionsEditionByMousePopupMenu.DEFAULT, map.get(event.getActionCommand()));
		        }
		    };
		}
		return menuListener;
	} 

	/**
	 * Enables or disables the undo option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledUndoOption(boolean b) {
		jMenuItemUndo.setEnabled(b);
	}
	
	/**
	 * Returns if the undo option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledUndoOption() {
		return jMenuItemUndo.isEnabled();
	}
	
	/**
	 * Enables or disables the redo option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledRedoOption(boolean b) {
		jMenuItemRedo.setEnabled(b);
	}
	
	/**
	 * Returns if the redo option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledRedoOption() {
		return jMenuItemRedo.isEnabled();
	}
	
	/**
	 * Enables or disables the cut option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledCutOption(boolean b) {
		jMenuItemCut.setEnabled(b);
	}
	
	/**
	 * Returns if the cut option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledCutOption() {
		return jMenuItemCut.isEnabled();
	}
	
	/**
	 * Enables or disables the copy option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledCopyOption(boolean b) {
		jMenuItemCopy.setEnabled(b);
	}
	
	/**
	 * Returns if the copy option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledCopyOption() {
		return jMenuItemCopy.isEnabled();
	}
	
	/**
	 * Enables or disables the paste option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledPasteOption(boolean b) {
		jMenuItemPaste.setEnabled(b);
	}
	
	/**
	 * Returns if the paste option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledPasteOption() {
		return jMenuItemPaste.isEnabled();
	}
	
	/**
	 * Enables or disables the delete option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledDeleteOption(boolean b) {
		jMenuItemDelete.setEnabled(b);
	}
	
	/**
	 * Returns if the delete option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledDeleteOption() {
		return jMenuItemDelete.isEnabled();
	}
	
	/**
	 * Enables or disables the select all option item
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledSelectAllOption(boolean b) {
		jMenuItemSelectAll.setEnabled(b);
	}
	
	/**
	 * Returns if the select all option item is enabled or not
	 * 
	 * @return Value true or false
	 */
	public boolean isEnabledelectAllOption() {
		return jMenuItemSelectAll.isEnabled();
	}	

	/**
	 * Enables or disables all option items
	 * 
	 * @param b Value true or false
	 */
	public void setEnabledAllOptions(boolean b) {
		this.setEnabledUndoOption(b);
		this.setEnabledRedoOption(b);
		this.setEnabledCutOption(b);
		this.setEnabledCopyOption(b);
		this.setEnabledPasteOption(b);
		this.setEnabledDeleteOption(b);
		this.setEnabledSelectAllOption(b);
	}	
	
	/* 
	 * (non-Javadoc)
	 * @see javax.swing.JPopupMenu#setVisible(boolean)
	 */
	public void setVisible(boolean b) {
		// Notifies that has changed the visibility of this component
		super.setVisible(b);
		this.getMenuListener().actionPerformed(new ActionEvent(this, JOptionsEditionByMousePopupMenu.VISIBILITY, JOptionsEditionByMousePopupMenu.VISIBILITYCHANGED));
	}
	   
    /**
     * Adds a "Property Change Listener"
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
    	changes.addPropertyChangeListener(l);
    }

    /**
     * Removes a "Property Change Listener"
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
    	changes.removePropertyChangeListener(l);
    }    
}
