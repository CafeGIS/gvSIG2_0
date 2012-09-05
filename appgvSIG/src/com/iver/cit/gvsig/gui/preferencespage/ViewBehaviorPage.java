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
package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.swing.JBlank;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.XMLEntity;

/**
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ViewBehaviorPage extends AbstractPreferencePage{
	private static final int FACTORY_DEFAULT_MAPCONTEXT_FRAME_RATE = 3;
	protected String id = ViewBehaviorPage.class.getName();
	private ScreenRefreshRatePanel refreshRate;
	private static final String MAPCONTROL_ENABLE_ANIMATION_KEY_NAME = "MapControlEnableAnimation";
	private static final String MAPCONTROL_REFRESH_RATE_KEY_NAME = "MapControlRefreshRate";

	public ViewBehaviorPage() {
		super();
		setParentID(ViewPage.id);
		addComponent(new JBlank(1,1));
		refreshRate = new ScreenRefreshRatePanel();
		addComponent(refreshRate);
	}

	@Override
	public void setChangesApplied() {
		setChanged(false);
	}

	@Override
	public void storeValues() throws StoreException {
		final int frameRate = refreshRate.getFrameRate();
		final boolean bAnimationEnabled = frameRate>0;
		if (frameRate > 0) {
			MapContext.setDrawFrameRate(frameRate);
		}
		MapControl.setDrawAnimationEnabled(bAnimationEnabled);

		// Apply on-the-fly to all already created views
		IWindow[] windows = PluginServices.getMDIManager().getAllWindows();
		for (int i = 0; i < windows.length; i++) {
			if (windows[i] instanceof BaseView) {
				BaseView view = (BaseView) windows[i];
				view.getMapControl().applyFrameRate();
			}
		}

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml.putProperty(
				MAPCONTROL_REFRESH_RATE_KEY_NAME,
				MapContext.getDrawFrameRate());
		xml.putProperty(
				MAPCONTROL_ENABLE_ANIMATION_KEY_NAME,
				MapControl.isDrawAnimationEnabled());

	}

	public String getID() {
		return id;
	}

	public ImageIcon getIcon() {
		return null;
	}

	public JPanel getPanel() {
		return this;
	}

	public String getTitle() {
		return PluginServices.getText(this, "behavior");
	}

	public void initializeDefaults() {
		refreshRate.setFrameRate(FACTORY_DEFAULT_MAPCONTEXT_FRAME_RATE);
	}

	public void initializeValues() {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		// View refresh rate
		if (xml.contains(MAPCONTROL_REFRESH_RATE_KEY_NAME)) {
			MapContext.setDrawFrameRate(xml.getIntProperty(MAPCONTROL_REFRESH_RATE_KEY_NAME));
		}

		// View animation enabled
		if (xml.contains(MAPCONTROL_ENABLE_ANIMATION_KEY_NAME)) {
			MapControl.setDrawAnimationEnabled(xml.getBooleanProperty(MAPCONTROL_ENABLE_ANIMATION_KEY_NAME));
		}


		int frameRate = MapContext.getDrawFrameRate();
		if (!MapControl.isDrawAnimationEnabled())
			frameRate = 0;
		refreshRate.setFrameRate(frameRate);
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

	private class ScreenRefreshRatePanel extends JPanel {
		private static final long serialVersionUID = -68376902499946576L;
		private JSlider sldRefreshRate;

		public ScreenRefreshRatePanel() {
    		setPreferredSize(new Dimension(500, 200));
            Border border=BorderFactory.createTitledBorder(
            		PluginServices.getText(this, "options.view.behavior.screen_refreshrate"));
            setBorder(border);
    		setLayout(new BorderLayout(15, 15));

    		JPanel aux = new JPanel(new BorderLayout());
    		aux.add(new JLabel(
    				PluginServices.getText(
    						this,
    						"options.view.behavior.screen_refreshrate.none")),
    				BorderLayout.WEST);
    		JPanel aux2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    		aux2.add(new JLabel(
    				PluginServices.getText(
    						this,
    						"options.view.behavior.screen_refreshrate.half")),
    				BorderLayout.CENTER);
    		aux.add(aux2);
    		aux.add(new JLabel(
    				PluginServices.getText(
    						this,
    						"options.view.behavior.screen_refreshrate.full")),
    				BorderLayout.EAST);

    		add(aux, BorderLayout.NORTH);
    		aux = new JPanel(new BorderLayout());
    		aux.add(getSldRefreshRate(), BorderLayout.NORTH);
    		aux.add(new JBlank(10, 20), BorderLayout.SOUTH);
    		add(aux, BorderLayout.CENTER);
    		add(new JLabel(PluginServices.
    				getText(this, "options.view.behavior.screen_refresh_rate.help")),
    				BorderLayout.SOUTH);
    	}

		private JSlider getSldRefreshRate() {
			if (sldRefreshRate == null) {
				sldRefreshRate = new JSlider(0, 30);

			}

			return sldRefreshRate;
		}

		public int getFrameRate() {
			return getSldRefreshRate().getValue();
		}

		public void setFrameRate(int sampleRate) {
			if (sampleRate<=30)
				getSldRefreshRate().setValue(sampleRate);
		}

    }

	@Override
	public String getParentID() {
		if (super.getParentID()==null) {
			setParentID(ViewPage.id);
		}
		return super.getParentID();
	}
}
