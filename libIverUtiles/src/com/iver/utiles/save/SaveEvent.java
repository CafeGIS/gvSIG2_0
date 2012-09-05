package com.iver.utiles.save;

import java.io.File;
import java.util.EventObject;

/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/**
 * Event which indicates that a file is going to be saved, or has been saved.
 *
 * @see BeforeSavingListener
 * @see AfterSavingListener
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class SaveEvent extends EventObject {
	private static final long serialVersionUID = -4477418408438214208L;

	/**
     * Determines that's going to save a file.
     */
    public static final short BEFORE_SAVING = 0;

    /**
     * Determines that has saved a file.
     */
    public static final short AFTER_SAVING = 1;

    /**
     * Identifies the type of this event.
     */
    private short id;

    /**
     * File to be saved or saved.
     */
    private File file;

    /**
     * <p>Creates a new <code>SaveEvent</code> instance.</p>
     * 
     * @param source 
     * @param id identifies this event
     * @param file path of the associated file
     */
    public SaveEvent(Object source, short id, File file) {
		super(source);
		this.id = id;
		this.file = file;
	}

    /**
     * Returns a String representation of this EventObject.
     *
     * @return  A a String representation of this EventObject.
     */
    public String toString() {
        return getClass().getName() + " [" + paramString() + "] on " + (source != null? source.getClass().toString() : "");
    }

    /**
     * Returns a string representing the kind of this <code>Event</code>.
     * 
     * @return  a string representation of this event
     */
    protected String paramString() {
    	String s_id = null;
    	
    	switch(id) {
    		case BEFORE_SAVING:
    			s_id = "BEFORE SAVE";
    			break;
    		case AFTER_SAVING:
    			s_id = "AFTER SAVE";
    			break;
    	}

        return "ID: " + s_id + " File Absolute Path: " + file.getAbsolutePath();
    }

    /**
     * Returns the event type.
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the file saved or going to be saved.
     * 
     * @return the referenced file
     */
    public File getFile() {
    	return file;
    }
}
