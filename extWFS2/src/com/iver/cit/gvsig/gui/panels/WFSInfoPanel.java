package com.iver.cit.gvsig.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.remoteClient.wfs.schema.XMLElement;
import org.gvsig.remoteClient.wfs.schema.type.IXMLType;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.gui.panels.model.WFSUtils;
import com.iver.cit.gvsig.project.documents.view.toc.gui.HTMLInfoPanel;

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
/* CVS MESSAGES:
 *
 * $Id: WFSInfoPanel.java 17736 2008-01-02 16:53:48Z ppiqueras $
 * $Log$
 * Revision 1.16  2006-12-26 10:25:37  ppiqueras
 * Corregidas las dependencias con las nuevas ubicaciones de clases: IXMLType, XMLElement, IXMLComplexType, etc. (en libRemoteServices)
 *
 * Revision 1.15  2006/12/26 09:23:23  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2).
 *
 * Revision 1.13  2006/10/10 12:55:06  jorpiell
 * Se ha añadido el soporte de features complejas
 *
 * Revision 1.12  2006/10/05 12:49:57  jorpiell
 * Cambiada la cadena buffer por max_features
 *
 * Revision 1.11  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.9.2.1  2006/09/19 12:28:11  jorpiell
 * Ya no se depende de geotools
 *
 * Revision 1.10  2006/09/18 12:07:31  jorpiell
 * Se ha sustituido geotools por el driver de remoteservices
 *
 * Revision 1.9  2006/07/24 07:30:33  jorpiell
 * Se han eliminado las partes duplicadas y se está usando el parser de GML de FMAP.
 *
 * Revision 1.8  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.7  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.6  2006/06/15 07:50:58  jorpiell
 * Añadida la funcionalidad de reproyectar y hechos algunos cambios en la interfaz
 *
 * Revision 1.5  2006/05/25 16:22:23  jorpiell
 * Cambio para eliminar el namespace de los atributos
 *
 * Revision 1.4  2006/05/25 16:01:43  jorpiell
 * Se ha añadido la funcionalidad para eliminar el namespace de los tipos de atributos
 *
 * Revision 1.3  2006/05/25 10:31:06  jorpiell
 * Como ha cambiado la forma de mostrar las capas (una tabla, en lugar de una lista), los paneles han tenido que ser modificados
 *
 * Revision 1.2  2006/05/23 08:09:39  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.1  2006/04/20 16:38:24  jorpiell
 * Ahora mismo ya se puede hacer un getCapabilities y un getDescribeType de la capa seleccionada para ver los atributos a dibujar. Queda implementar el panel de opciones y hacer el getFeature().
 *
 * Revision 1.1  2006/04/19 12:50:16  jorpiell
 * Primer commit de la aplicación. Se puede hacer un getCapabilities y ver el mensaje de vienvenida del servidor
 *
 *
 */

/**
 * <p>Panel that provides information about the service offered by the WFS server connected.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSInfoPanel extends HTMLInfoPanel implements IWFSPanel {
	private static final long serialVersionUID = 2605168444543321684L;

	private final String service_title       = PluginServices.getText(this, "service_info");
	private final String server              = PluginServices.getText(this, "server");
	private final String server_type         = PluginServices.getText(this, "server_type");
	private final String server_abstract     = PluginServices.getText(this, "server_abstract");
	private final String server_title        = PluginServices.getText(this, "server_title");
	private final String layers_title        = PluginServices.getText(this, "selected_layer");
	private final String layer_title         = PluginServices.getText(this, "layer_title");
	private final String layer_abstract      = PluginServices.getText(this, "layer_abstract");
	private final String options			 = PluginServices.getText(this, "properties");
	private final String layer_fields 	 	 = PluginServices.getText(this, "fields");
	private final String layer_name			 = PluginServices.getText(this, "name");
	private final String timeout 			 = PluginServices.getText(this, "timeout");
	private final String buffer				 = PluginServices.getText(this, "max_features");
	private final String layer_geometry		 = PluginServices.getText(this, "geometry");
	private final String layer_srs			 = PluginServices.getText(this, "srs");
	
	/**
	 * Creates a new instance of InfoPanel with double buffer and null layout
	 */
	public WFSInfoPanel() {
		super();
		initialize();
		this.setPreferredSize(new Dimension(10,10));
	}	
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		IPanelGroup panelGroup = getPanelGroup();
		
		if (panelGroup == null)
			return;
		
		((WFSParamsPanel)panelGroup).refreshCapabilitiesInfo();
		super.paintComponent(g);
	}

	/**
	 * Fills the text pane with a data table describing the
	 * service and the selected settings.
	 *
	 * @param WFSWizardData dataSource: connection info
	 * @param WFSLayer layer: the selected layer
	 */
	public void refresh(WFSSelectedFeature layer) {
		WFSServerExplorer serverExplorer = getServerExplorer();
		
		String server_text = serverExplorer.getUrl();
		String server_type_text = serverExplorer.getServerType();
		String server_title_text = serverExplorer.getTitle();
		String server_abstract_text = serverExplorer.getAbstract();
		
		if (server_text == null)
			server_text = "-";
		if (server_type_text == null)
			server_type_text = "-";
		if (server_title_text == null)
			server_title_text = "-";
		if (server_abstract_text == null)
			server_abstract_text = "-";

		String layers_html = "";
		if (layer!=null) {
			String layer_name_text = layer.getName();
			String layer_abstract_text = layer.getAbstract();
			String layer_title_text = layer.getTitle();
			String layer_fields_text = "-";
			String layer_geometry_text = "-";
			String layer_srs_text = "-";

			Vector<Object> fields = layer.getSelectedFields();
			layer_fields_text = "";

			for (int i=0 ; i<fields.size() ; i++){
				XMLElement field = (XMLElement)fields.get(i);
				if (!((field.getEntityType() != null) && (field.getEntityType().getType() == IXMLType.GML_GEOMETRY))){
					layer_fields_text = layer_fields_text + field.getName() +
					" (" +
					PluginServices.getText(this,WFSUtils.getFieldType(field.getEntityType())) + ")";
				}else{
					layer_fields_text = layer_fields_text + field.getName();
				}
				if (i < fields.size() -1){
					layer_fields_text = layer_fields_text + ", ";
				}
			}

			layer_geometry_text = PluginServices.getText(this,WFSUtils.getGeometry(layer));
			if (layer_geometry_text.equals("")){
				layer_geometry_text = "-";
			}

			if (layer.getSrs().size() > 0){
				layer_srs_text = (String)layer.getSrs().get(0);
			}

			if (layer_name_text ==null)
				layer_name_text = "-";
			if (layer_abstract_text==null)
				layer_abstract_text = "-";
			if (layer_title_text == null)
				layer_title_text = "-";
			StringBuffer layer_html = new StringBuffer();
			layer_html.append(setHTMLBasicProperty(layer_name, layer_name_text));
			layer_html.append(setHTMLBasicProperty(layer_title, layer_title_text));
			layer_html.append(setHTMLBasicProperty(layer_abstract, layer_abstract_text));
			layer_html.append(setHTMLBasicProperty(layer_geometry, layer_geometry_text));
			layer_html.append(setHTMLBasicProperty(layer_fields, layer_fields_text));
			layer_html.append(setHTMLBasicProperty(layer_srs, layer_srs_text));
			layers_html += layer_html.toString();
		}
		if (!layers_html.equals(""))
			layers_html = setHTMLTitleTable(layers_title, 2) + layers_html;

		String buffer_text = String.valueOf(serverExplorer.getBuffer());
		String timeout_text = String.valueOf(serverExplorer.getTimeOut());

		StringBuffer options_html = new StringBuffer();
		options_html.append(setHTMLTitleTable(options , 2));
		options_html.append(setHTMLBasicProperty(timeout, timeout_text));
		options_html.append(setHTMLBasicProperty(buffer, buffer_text));
			
		StringBuffer service_html = new StringBuffer();
		service_html.append(setHTMLTitleTable(service_title, 2));
		service_html.append(setHTMLBasicProperty(server, server_text));
		service_html.append(setHTMLBasicProperty(server_type, server_type_text));
		service_html.append(setHTMLBasicProperty(server_title, server_title_text));
		service_html.append(setHTMLBasicProperty(server_abstract, server_abstract_text));
			
		StringBuffer html = new StringBuffer();
		html.append(setHTMLTable(service_html.toString()));
		html.append(setHTMLTable(layers_html.toString()));
		html.append(setHTMLTable(options_html.toString()));	
		
		addHTMLToPanel(setHTMLBody(html.toString()));		
	}	

	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void accept() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#apply()
	 */
	public void apply() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#cancel()
	 */
	public void cancel() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#selected()
	 */
	public void selected() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * <p>Gets the information of the wizard used to add or load panels.</p>
	 * 
	 * @return the wizard data
	 */
	public WFSServerExplorer getServerExplorer(){
		return (getPanelGroup() == null)? null: ((WFSParamsPanel)getPanelGroup()).getServerExplorer();  
	}
}
