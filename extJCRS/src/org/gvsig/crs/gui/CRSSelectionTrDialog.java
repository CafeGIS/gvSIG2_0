/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Av. Blasco Ib��ez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui;

import javax.swing.JPanel;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.CrsData;
import org.gvsig.crs.persistence.RecentCRSsPersistence;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * Di�logo contenedor del panel de la selecci�n de CRS y transformaci�n
 * de la capa a a�adir
 * 
 * @author Jos� Luis G�mez Mart�nez (JoseLuis.Gomez@uclm.es)
 *
 */

public class CRSSelectionTrDialog extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	JPanel contentPane = null;
	ICrs curProj = null;
	private IProjection lastProj = null;
	private boolean okPressed = false;
	
	public CRSSelectionTrDialog(ICrs proj) {
		super(false);
		curProj = proj;
		if (curProj.getSourceTransformationParams() == null && curProj.getTargetTransformationParams() == null) {
			CrsData crsData = new CrsData(proj.getCrsWkt().getAuthority()[0], proj.getCode(),proj.getCrsWkt().getName());
			RecentCRSsPersistence persistence = new RecentCRSsPersistence();
			persistence.addCrsData(crsData);
		}
		inicializate();
	}
	public void inicializate(){
		this.add(getContentPanel(), null);        
	}
			
    public CRSMainTrPanel getProjPanel() {
        return (CRSMainTrPanel) getContentPanel();
    }

	/**
	 * @return
	 */
	public IProjection getProjection() {
		return (IProjection) getProjPanel().getProjection();
	}
	/**
	 * @param proj
	 */
	public void setProjection(IProjection proj) {
		lastProj = proj;
		getProjPanel().setProjection(proj);
	}

	public JPanel getContentPanel() {
	    if (contentPane == null) {	    	
	    	IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
	    	BaseView activeView = (com.iver.cit.gvsig.project.documents.view.gui.BaseView) activeWindow;
	    	int target = ((ICrs) activeView.getMapControl().getProjection()).getCode();
	    	CrsWkt crsWktTarget = ((ICrs) activeView.getMapControl().getProjection()).getCrsWkt();
	    	if (crsWktTarget.getDatumName().equals("")){	    		
	    	}
        	contentPane = new CRSMainTrPanel(target, crsWktTarget, curProj);        	
	    	      	
       }
	   return contentPane;
    }
	
	public boolean isOkPressed() { return okPressed; }	
}
