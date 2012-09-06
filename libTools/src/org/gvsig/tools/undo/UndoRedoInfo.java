/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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

/*
 * AUTHORS (In addition to CIT):
 * 2008 {DiSiD Technologies}  {{Task}}
 */
package org.gvsig.tools.undo;

import java.util.Date;

/**
 * Information about a undoable or redoable element.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public interface UndoRedoInfo {
    
    public final static int DELETE = 0;
    public final static int INSERT = 1;
    public final static int UPDATE = 2;

    /**
     * Returns this command description
     * 
     * @return a String with this command description
     */
    public String getDescription();

    /**
     * Returns a String with the date in which this command was created or
     * redone.
     * 
     * @return a String with the date in which this command was created or
     *         redone
     */
    public Date getDate();
    
    /**
     * Returns the type of action related to data: insertion, deletion or
     * update.
     * 
     * @return the type of action related to data: insertion, deletion or update
     */
    public int getType();
}