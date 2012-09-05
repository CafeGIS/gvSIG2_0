/*
 * Created on 05-may-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
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
package com.iver.cit.gvsig;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.exolab.castor.xml.Marshaller;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.preferences.IPreference;
import com.iver.andami.preferences.IPreferenceExtension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.gui.preferencespage.LayoutPage;
import com.iver.cit.gvsig.project.documents.layout.FLayoutZooms;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameBasicFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphicsFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGridFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroupFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameLegendFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameNorthFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameOverViewFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFramePictureFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameScaleBarFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameSymbolFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameTableFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameTextFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameViewFactory;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.GenericFileFilter;
import com.iver.utiles.XMLEntity;


/**
 * Extensión para controlar las operaciones basicas sobre el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutExtension extends Extension implements IPreferenceExtension {
    private static final Logger logger = LoggerFactory
            .getLogger(LayoutExtension.class);

	private Layout layout = null;
	private static LayoutPage layoutPropertiesPage=new LayoutPage();

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String s) {
		layout = (Layout) PluginServices.getMDIManager().getActiveWindow();

		FLayoutZooms zooms = new FLayoutZooms(layout);
		logger.debug("Comand : " + s);

		if (s.equals("LAYOUT_PAN")) {
			layout.getLayoutControl().setTool("layoutpan");
		} else if (s.equals("LAYOUT_ZOOM_IN")) {
			layout.getLayoutControl().setTool("layoutzoomin");
		} else if (s.equals("LAYOUT_ZOOM_OUT")) {
			layout.getLayoutControl().setTool("layoutzoomout");
		} else if (s.equals("LAYOUT_FULL")) {
			layout.getLayoutControl().fullRect();
		} else if (s.equals("REALZOOM")) {
			zooms.realZoom();
		} else if (s.equals("LAYOUT_ZOOMOUT")) {
			zooms.zoomOut();
		} else if (s.equals("LAYOUT_ZOOMIN")) {
			zooms.zoomIn();
		} else if (s.equals("ZOOMSELECT")) {
			zooms.zoomSelect();
		} else if (s.equals("SAVETEMPLATE")){
			saveLayout();
		}
	}
	private void saveLayout() {
    	layout = (Layout) PluginServices.getMDIManager().getActiveWindow();
		JFileChooser jfc = new JFileChooser();
		jfc.addChoosableFileFilter(new GenericFileFilter("gvt",
				PluginServices.getText(this, "plantilla")));

		if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
			File file=jfc.getSelectedFile();
			if (!(file.getPath().endsWith(".gvt") || file.getPath().endsWith(".GVT"))){
				file=new File(file.getPath()+".gvt");
			}
			if (file.exists()) {
				int resp = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),PluginServices.getText(this,"fichero_ya_existe_seguro_desea_guardarlo"),
						PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION) {
					return;
				}
			}

			try {
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
	            OutputStreamWriter writer = new OutputStreamWriter(fos, ProjectExtension.PROJECTENCODING);
				Marshaller m = new Marshaller(writer);
				m.setEncoding(ProjectExtension.PROJECTENCODING);
				XMLEntity xml=layout.getXMLEntity();
				xml.putProperty("followHeaderEncoding", true);
				m.marshal(xml.getXmlTag());
			} catch (Exception e) {
				NotificationManager.addError(PluginServices.getText(this, "Error_guardando_la_plantilla"), e);
			}
		}
	}
	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow f = PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof Layout) {
			return true; //layout.m_Display.getMapControl().getMapContext().getLayers().layerCount() > 0;
		}
		return false;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerFFrames();
		registerIcons();
	}
	private void registerFFrames() {
		FFrameBasicFactory.register();
		FFrameGraphicsFactory.register();
		FFrameGroupFactory.register();
		FFrameLegendFactory.register();
		FFrameNorthFactory.register();
		FFrameOverViewFactory.register();
		FFramePictureFactory.register();
		FFrameScaleBarFactory.register();
		FFrameSymbolFactory.register();
		FFrameTableFactory.register();
		FFrameTextFactory.register();
		FFrameViewFactory.register();
		FFrameGridFactory.register();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layout-zoom-in",
				this.getClass().getClassLoader().getResource("images/LayoutZoomIn.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-zoom-out",
				this.getClass().getClassLoader().getResource("images/LayoutZoomOut.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"view-zoom-center-in",
				this.getClass().getClassLoader().getResource("images/zoommas.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"view-zoom-center-out",
				this.getClass().getClassLoader().getResource("images/zoommenos.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-zoom-fit",
				this.getClass().getClassLoader().getResource("images/mundo.gif")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-zoom-real",
				this.getClass().getClassLoader().getResource("images/zoomreal.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-zoom-selected",
				this.getClass().getClassLoader().getResource("images/zoomselect.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-pan",
				this.getClass().getClassLoader().getResource("images/LayoutPan.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"save-icon",
				this.getClass().getClassLoader().getResource("images/save.png")
			);
	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Devuelve el Layout sobre el que se opera.
	 *
	 * @return Layout.
	 */
	public Layout getLayout() {
		return layout;
	}

	public IPreference[] getPreferencesPages() {
		IPreference[] preferences=new IPreference[1];
		preferences[0]=layoutPropertiesPage;
		return preferences;
	}
}
