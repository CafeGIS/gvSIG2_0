/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
* $Id: InfoPanel.java 6500 2006-07-21 11:51:13Z jaume $
* $Log$
* Revision 1.5  2006-07-21 11:51:13  jaume
* improved appearance in wms panel and a wmc bug fixed
*
* Revision 1.4  2006/02/28 15:25:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.5  2006/02/07 08:29:39  jaume
* *** empty log message ***
*
* Revision 1.2.2.4  2006/02/06 15:19:50  jaume
* *** empty log message ***
*
* Revision 1.2.2.3  2006/01/31 16:25:24  jaume
* correcciones de bugs
*
* Revision 1.3  2006/01/26 16:07:14  jaume
* *** empty log message ***
*
* Revision 1.2.2.1  2006/01/26 12:59:32  jaume
* 0.5
*
* Revision 1.2  2006/01/26 12:50:20  jaume
* *** empty log message ***
*
* Revision 1.1  2006/01/25 09:08:53  jaume
* test save and reload project
*
*
*/
package com.iver.cit.gvsig.gui.panels;

import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.gui.wizards.WMSWizardData;

/**
 * Service Information Panel
 */

class InfoPanel extends JPanel {
    private final String bgColor0 = "\"#FEEDD6\""; // light salmon
    private final String bgColor1 = "\"#EAEAEA\""; // light grey
    private final String bgColor2 = "\"#F2FEFF\""; // light blue
    private final String bgColor3 = "\"#FBFFE1\""; // light yellow

    private final String service_title       = PluginServices.getText(this, "service_info");
    private final String server              = PluginServices.getText(this, "server");
    private final String server_type         = PluginServices.getText(this, "server_type");
    private final String server_abstract     = PluginServices.getText(this, "server_abstract");
    private final String server_title        = PluginServices.getText(this, "server_title");
    private final String layers_title        = PluginServices.getText(this, "selected_layers");
    private final String layer_title         = PluginServices.getText(this, "layer_title");
    private final String layer_abstract      = PluginServices.getText(this, "layer_abstract");
    private final String selected_dimensions = PluginServices.getText(this, "selected_dimensions");
    private final String style               = PluginServices.getText(this, "style");
    private final String format_title        = PluginServices.getText(this, "format");
    private final String srs_title           = "SRS";
    private final String properties			 = PluginServices.getText(this, "properties");
    private final String layer_name			 = PluginServices.getText(this, "name");
	private JEditorPane editor = null;


    /**
     * Creates a new instance of InfoPanel with double buffer and null layout
     *
     */
	public InfoPanel() {
		super();
        setLayout(null);
        JScrollPane src = new JScrollPane();
        src.setBounds(5, 5, 485, 375);
        add(src, null);
        src.setViewportView(getEditor());
	}

    /**
     * This method initializes tblInfo
     *
     * @return javax.swing.JTable
     */
    private JEditorPane getEditor() {
        if (editor == null) {
            editor = new JEditorPane();
            editor.setEditable(false);
        }
        return editor;
    }


    /**
     * Fills the text pane with a data table describing the service and the
     * selected settings.
     *
     * @param dataSource
     */

    public void refresh(WMSWizardData dataSource, Vector layers, Vector styles, String format, String srs, Vector dimensions) {

        String server_text = dataSource.getHost();
        String server_type_text = dataSource.getServerType();
        String server_title_text = dataSource.getTitle();
        String server_abstract_text = dataSource.getAbstract();
        String font = "Arial";

        if (server_text == null)
            server_text = "-";
        if (server_type_text == null)
            server_type_text = "-";
        if (server_title_text == null)
            server_title_text = "-";
        if (server_abstract_text == null)
            server_abstract_text = "-";

        if (format == null)
            format = PluginServices.getText(this, "none_selected");
        if (srs == null)
            srs = PluginServices.getText(this, "none_selected");

        String layers_html = "";
        for (int i = 0; i < layers.size(); i++) {
            WMSLayerNode layer = (WMSLayerNode) layers.get(i);
            String layer_name_text = layer.getName();
            String style_title = (styles==null) ? PluginServices.getText(this, "not_available") : (String) styles.get(i);//((FMapWMSStyle) selectedStyles.get(i)).title;
            String layer_abstract_text = layer.getAbstract();
            String layer_title_text = layer.getTitle();

            if (layer_name_text ==null)
            	layer_name_text = "-";
            if (style_title==null)
            	style_title = "-";
            if (layer_abstract_text==null)
            	layer_abstract_text = "-";
            if (layer_title_text == null)
            	layer_title_text = "-";
            String layer_html =
            	"  <tr valign=\"top\">" +
                "     <td bgcolor=\"#D6D6D6\" align=\"right\"><font face=\"Arial\" size=\"3\" align=\"right\"><b>"+layer_name+"</b></font></td>" +
                "     <td bgcolor="+bgColor0+"><font face=\"Arial\" size=\"3\">"+layer_name_text+"</font></td>" +
                "  </tr>" +
                "  <tr valign=\"top\">" +
                "     <td width=\"119\" height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\"Arial\" size=\"3\"><b>"+layer_title+"</b></font></td>" +
                "     <td width=\"322\" height=\"18\" bgcolor="+bgColor1+"><font face=\"Arial\" size=\"3\">"+layer_title_text+"</font></td>" +
                "  </tr>" +
                "  <tr valign=\"top\">" +
                "     <td bgcolor=\"#D6D6D6\" align=\"right\"><font face=\"Arial\" size=\"3\" align=\"right\"><b>"+layer_abstract+"</b></font></td>" +
                "     <td bgcolor="+bgColor0+"><font face=\"Arial\" size=\"3\">"+layer_abstract_text+"</font></td>" +
                "  </tr>" +
                "  <tr valign=\"top\">" +
                "     <td bgcolor=\"#D6D6D6\" align=\"right\"><font face=\"Arial\" size=\"3\" align=\"right\"><b>"+style+"</b></font></td>" +
                "     <td bgcolor="+bgColor1+"><font face=\"Arial\" size=\"3\">"+style_title+"</font></td>" +
                "  </tr>" +
                "  <tr>" +
                "  </tr>";
            layers_html += layer_html;
        }
        if (!layers_html.equals(""))
            layers_html =
                "  <tr valign=\"top\" bgcolor=\"#FFFFFF\">" +
                "    <td width=\"92\" height=\"18\" bgcolor="+bgColor3+" colspan=\"2\"><font face=\""+font+"\" size=\"4\"><b>"+layers_title+"</font></b></td>" +
                "  </tr>" + layers_html;
        String dimension_html = "";
        if (dimensions!=null) {
        	boolean swap = false;
        	for (int i = 0; i < dimensions.size(); i++) {
        		String color = swap ? bgColor0 : bgColor1;
        		String[] dimension = ((String) dimensions.get(i)).split("=");
        		dimension_html +=
        			"  <tr valign=\"top\" bgcolor="+color+">" +
        			"    <td width=\"120\" height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><b>"+dimension[0]+"</b></td>" +
        			"    <td width=\"322\" height=\"18\">"+dimension[1]+"</td>" +
        			"  </tr>";
        		swap = !swap;
        	}
        }
        if (!dimension_html.equals(""))
        	dimension_html =
        		"  <tr valign=\"top\">" +
        		"    <td width=\"92\" height=\"18\" bgcolor="+bgColor3+" colspan=\"2\"><font face=\""+font+"\" size=\"4\"><b>"+selected_dimensions+"</font></b></td>" +
        		"  </tr>" + dimension_html;

        String format_html =
        	"  <tr valign=\"top\" bgcolor=\"#FFFFFF\">" +
            "    <td width=\"92\" height=\"18\" bgcolor="+bgColor3+" colspan=\"2\"><font face=\""+font+"\" size=\"4\"><b>"+properties+"</font></b></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor0+">" +
            "    <td height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+format_title+"</b></font></td>" +
            "    <td><font face=\""+font+"\" size=\"3\"><font face=\""+font+"\" size=\"3\">"+format+"</font></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor1+">" +
            "    <td height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+srs_title+"</font></b></td>" +
            "    <td><font face=\""+font+"\" size=\"3\">"+srs+"</font></td>" +
            "  </tr>";

        String html =

            "<html>" +
            "<body>" +
            "<table align=\"center\" width=\"437\" height=\"156\" border=\"0\" cellpadding=\"4\" cellspacing=\"4\">" +
            "  <tr valign=\"top\" bgcolor=\"#FFFFFF\">" +
            "    <td width=\"92\" height=\"18\" bgcolor="+bgColor3+" colspan=\"2\"><font face=\""+font+"\" size=\"4\"><b>"+service_title+"</font></b></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor0+">" +
            "    <td width=\"92\" height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+server+"</font></b></td>" +
            "    <td width=\"268\"><font face=\""+font+"\" size=\"3\">"+server_text+"</font></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor1+">" +
            "    <td height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+server_type+"</b></font></td>" +
            "    <td><font face=\""+font+"\" size=\"3\">"+server_type_text+"</font></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor0+">" +
            "    <td height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+server_title+"</b></font></td>" +
            "    <td><font face=\""+font+"\" size=\"3\"><font face=\""+font+"\" size=\"3\">"+server_title_text+"</font></td>" +
            "  </tr>" +
            "  <tr valign=\"top\" bgcolor="+bgColor1+">" +
            "    <td height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""+font+"\" size=\"3\"><b>"+server_abstract+"</font></b></td>" +
            "    <td><font face=\""+font+"\" size=\"3\">"+server_abstract_text+"</font></td>" +
            "  </tr>" +

            "  <tr>" +
            "  </tr>" +
            layers_html +
            dimension_html +
            format_html +
            "</table>" +
            "</body>" +
            "</html>";

        getEditor().setContentType("text/html");
        getEditor().setText(html);
    }
}
