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
package org.gvsig.gvsig3dgui.skin;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JDialog;

import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.core.mdiManager.DialogStackSupport;


/**
 *
 */
public class JDialogStackSupport extends DialogStackSupport{

	/** log */
    private static Logger logger = Logger.getLogger(DialogStackSupport.class.getName());

    /** Pila de diálogos modales mostrados */
    private ArrayList dialogStack = new ArrayList(0);
    
    private ArrayList dialogCursors = new ArrayList(0);

    public JDialogStackSupport(MDIFrame frame) {
    	super(frame);
    }
    
    public void pushDialog(JDialog dlg){
    	dialogStack.add(dlg);
    }
    
    public JDialog popDialog(){
    	return (JDialog) dialogStack.remove(dialogStack.size() - 1);
    }

	/**
	 * @return
	 */
	public void setWaitCursor() {
        dialogCursors.clear();
		for (Iterator iter = dialogStack.iterator(); iter.hasNext();) {
			JDialog dlg = (JDialog) iter.next();
            dialogCursors.add(dlg.getCursor());
			dlg.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 	        dlg.getGlassPane().setVisible(true);
 	   }
	}

	/**
	 * 
	 */
	public void restoreCursor() {
		for (Iterator iter = dialogStack.iterator(); iter.hasNext();) {
			JDialog dlg = (JDialog) iter.next();
            
            // TODO: RECUPERAR EL CURSOR
            dlg.setCursor(null);
		    dlg.getGlassPane().setVisible(false);
		}
	}
	
}
