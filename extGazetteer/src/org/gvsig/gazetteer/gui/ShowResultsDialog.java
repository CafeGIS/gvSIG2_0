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
package org.gvsig.gazetteer.gui;


import javax.swing.JDialog;

import org.gvsig.gazetteer.GazetteerClient;
import org.gvsig.gazetteer.loaders.FeatureLoader;
import org.gvsig.gazetteer.querys.Feature;
import org.gvsig.gazetteer.querys.GazetteerQuery;
import org.gvsig.gazetteer.ui.showresults.ShowResultsDialogPanel;
import org.gvsig.i18n.Messages;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsDialog extends ShowResultsDialogPanel implements IWindow  {
    private SearchDialog searchDialog = null;
    private JDialog frame = null;
    /**
     * @param client
     * @param features
     * @param recordsByPage
     */
    public ShowResultsDialog(JDialog frame,GazetteerClient client, Feature[] features, int recordsByPage, GazetteerQuery query) {
        super(client, features, recordsByPage,query);
        this.frame = frame;
    }

      /* (non-Javadoc)
     * @see com.iver.andami.ui.mdiManager.View#getViewInfo()
     */
    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(Messages.getText( "gazetteer_search"));
		return m_viewinfo;
    }
    public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}
    
    public void closeButtonActionPerformed() {
	     closeJDialog();
	 }
   
    public void closeJDialog() {
        frame.setVisible(false);
	}
   
   /**
    * This method have to load the selected feature into gvSIG
    */
   public void loadButtonActionPerformed() {
       Feature feature = ppalPanel.getFeature();
       if (feature != null){
    
    	   new FeatureLoader(client.getProjection()).load(feature,query);
       }
       closeJDialog();
       //getSearchDialog().closeJDialog();
   }    
    
    /**
     * @return Returns the searchDialog.
     */
    public SearchDialog getSearchDialog() {
        return searchDialog;
    }
    /**
     * @param searchDialog The searchDialog to set.
     */
    public void setSearchDialog(SearchDialog searchDialog) {
        this.searchDialog = searchDialog;
    }
}
