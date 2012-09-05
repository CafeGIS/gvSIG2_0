/*
 * Created on 02-mar-2004
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


import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;


/**
 * Extensión que abre las tablas asociadas a las vistas.
 *
 * @author Vicente Caballero Navarro
 */
public class ShowTable extends Extension {
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		BaseView f = (BaseView) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		FLayer[] selected = f.getModel().getMapContext().getLayers().getActives();

		boolean algunaTabla = false;

		for (int i = 0; i < selected.length; i++) {
			if (selected[i].isAvailable() && selected[i] instanceof FLyrVect) {
				FLyrVect co = (FLyrVect) selected[i];

				try {
					if (co.getFeatureStore() != null) {
						algunaTabla = true;
					}
				} catch (ReadException e) {
					return false;
				}catch(NullPointerException e){
					return false;
				}
			}
		}

		return algunaTabla;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
															 .getActiveWindow();

		if (f == null) {
			return false;
		}

		return (f instanceof BaseView);
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#updateUI(java.lang.String)
	 */
	public void execute(String s) {
		BaseView vista = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		FLayer[] actives = vista.getModel().getMapContext().getLayers()
							.getActives();

		try {
			for (int i = 0; i < actives.length; i++) {
				if (actives[i] instanceof FLyrVect) {
					FLyrVect co = (FLyrVect) actives[i];

					//SelectableDataSource dataSource;
					//dataSource = co.getRecordset();

					ProjectExtension ext = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);

					FeatureTableDocument projectTable = ext.getProject().getTable(co);
					FeatureStore fs=((FLyrVect)actives[i]).getFeatureStore();
//					ReadableVectorial rv=((FLyrVect)actives[i]).getSource();
//					if (rv instanceof VectorialEditableAdapter){
//						ea=(EditableAdapter)((FLyrVect)actives[i]).getSource();
//					}else{
//						ea=new EditableAdapter();
//						SelectableDataSource sds=((FLyrVect)actives[i]).getRecordset();
//						ea.setOriginalDataSource(sds);
//					}

					if (projectTable == null) {
						projectTable = ProjectFactory.createTable(PluginServices.getText(this, "Tabla_de_Atributos") + ": " + actives[i].getName(),
								fs);
						projectTable.setProjectDocumentFactory(new FeatureTableDocumentFactory());
						projectTable.setAssociatedLayer(co);
						ext.getProject().addDocument(projectTable);
					}
					projectTable.setStore(fs);
					FeatureTableDocumentPanel t = new FeatureTableDocumentPanel(projectTable);
					t.getModel().setModified(true);
					PluginServices.getMDIManager().addWindow(t);
				}
			}
		} catch (DataException e) {
            NotificationManager.addError(PluginServices.getText(this,"No_se_pudo_obtener_la_tabla_de_la_capa"), e);
        }
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
		PluginServices.getIconTheme().registerDefault(
				"layer-show-attribute-table",
				this.getClass().getClassLoader().getResource("images/ResultConsulta.png")
			);
	}
}
