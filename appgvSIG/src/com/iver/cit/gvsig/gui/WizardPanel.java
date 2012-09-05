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
package com.iver.cit.gvsig.gui;

import javax.swing.JPanel;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;

public abstract class WizardPanel extends JPanel{
	private String tabName = "TabName";
    private MapControl mapCtrl = null;
	private WizardListenerSupport listenerSupport = new WizardListenerSupport();

	public void addWizardListener(WizardListener listener) {
		listenerSupport.addWizardListener(listener);
	}
	public void callError(Exception descripcion) {
		listenerSupport.callError(descripcion);
	}
	public void removeWizardListener(WizardListener listener) {
		listenerSupport.removeWizardListener(listener);
	}
	public void callStateChanged(boolean finishable) {
		listenerSupport.callStateChanged(finishable);
	}
	protected void setTabName(String name) { tabName = name; }
	public String getTabName() { return tabName; }
	abstract public void initWizard();
	abstract public void execute();
	/* (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.ILayerPanel#getLayer()
	 */
//	abstract public FLayer getLayer();
	abstract public DataStoreParameters[] getParameters();
    /**
     * You can use it to extract information from
     * the mapControl that will receive the new layer.
     * For example, projection to use, or visible extent.
     * @return Returns the mapCtrl.
     */
    public MapControl getMapCtrl() {
        return mapCtrl;
    }
    /**
     * @param mapCtrl The mapCtrl to set.
     */
    public void setMapCtrl(MapControl mapCtrl) {
        this.mapCtrl = mapCtrl;
    }
}
