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

import java.util.Collections;
import java.util.Map;

import org.gvsig.tools.exception.BaseException;

/**
 * Exception thrown when there is an error while performing an redo.
 * 
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class RedoException extends BaseException {

    private static final long serialVersionUID = 6635654940071716308L;
    
    private final static String MESSAGE_FORMAT = "The element %(undoredo) could not be redone";
    private final static String MESSAGE_KEY = "_RedoException";

    private UndoRedoInfo undoRedoInfo;

    /**
     * Creates a new exception of an error while performing a redo operation.
     * 
     * @param undoRedoInfo
     *            the information of the element that could not be redone
     */
    public RedoException(UndoRedoInfo undoRedoInfo) {
        super(MESSAGE_FORMAT, MESSAGE_KEY, serialVersionUID);
        this.undoRedoInfo = undoRedoInfo;
    }

    /**
     * Creates a new exception of an error while performing a redo operation.
     * 
     * @param undoRedoInfo
     *            the information of the element that could not be redone
     * @param cause
     *            the original cause of the exception
     */
    public RedoException(UndoRedoInfo undoRedoInfo, Throwable cause) {
        super(MESSAGE_FORMAT, cause, MESSAGE_KEY, serialVersionUID);
        this.undoRedoInfo = undoRedoInfo;
    }

    protected Map values() {
        return Collections
                .singletonMap("undoredo", undoRedoInfo
                .getDescription());
    }
}