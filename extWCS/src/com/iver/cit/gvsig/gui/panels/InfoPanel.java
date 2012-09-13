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
* $Id: InfoPanel.java 6501 2006-07-21 11:51:55Z jaume $
* $Log$
* Revision 1.4  2006-07-21 11:51:55  jaume
* improved appearance
*
* Revision 1.3  2006/05/23 10:37:39  jaume
* fixed import of Cancellable
*
* Revision 1.2  2006/03/28 09:14:04  jaume
* Para empezar a limpiar errores
*
* Revision 1.1  2006/03/21 16:36:58  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.panels;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.WCSLayer;
import com.iver.cit.gvsig.gui.wcs.WCSWizardData;

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
    private final String selected_parameters = PluginServices.getText(this, "selected_parameters");
    private final String time_title               = PluginServices.getText(this, "time");
    private final String format_title        = PluginServices.getText(this, "format");
    private final String srs_title           = "CRS";
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
     * @param WCSWizardData dataSource: connection info
     * @param WCSLayer layer: the selected layer
     * @param String time: the time value.
     * @param String format: the format value
     * @param String crs: the CRS value
     * @param String parameters: comma-separated key-value string containing the values for the parameters
     */

    public void refresh(WCSWizardData dataSource, WCSLayer layer, String time, String format, String crs, String parameters) {

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
        if (crs == null)
            crs = PluginServices.getText(this, "none_selected");

        String layers_html = "";
        if (layer!=null) {
            String layer_name_text = layer.getName();
            String time_text = (time==null) ? PluginServices.getText(this, "not_available") : time;//((FMapWMSStyle) selectedStyles.get(i)).title;
            String layer_abstract_text = layer.getDescription();
            String layer_title_text = layer.getTitle();

            if (layer_name_text ==null)
            	layer_name_text = "-";
            if (time_text==null)
            	time_text = "-";
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
                "     <td bgcolor=\"#D6D6D6\" align=\"right\"><font face=\"Arial\" size=\"3\" align=\"right\"><b>"+time_title+"</b></font></td>" +
                "     <td bgcolor="+bgColor1+"><font face=\"Arial\" size=\"3\">"+time_text+"</font></td>" +
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
        String parameter_html = "";
        if (parameters!=null) {
        	boolean swap = false;
        	String[] myParameters = parameters.split("&");
        	for (int i = 0; i < myParameters.length; i++) {
        		String color = swap ? bgColor0 : bgColor1;
        		String[] parameter = myParameters[i].split("=");
        		parameter_html +=
        			"  <tr valign=\"top\" bgcolor="+color+">" +
        			"    <td width=\"120\" height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><b>"+parameter[0]+"</b></td>" +
        			"    <td width=\"322\" height=\"18\">"+parameter[1]+"</td>" +
        			"  </tr>";
        		swap = !swap;
        	}
        	parameter_html +=
        		"  <tr>" +
        		"  </tr>";
        }
        if (!parameter_html.equals(""))
        	parameter_html =
        		"  <tr valign=\"top\">" +
        		"    <td width=\"92\" height=\"18\" bgcolor="+bgColor3+" colspan=\"2\"><font face=\""+font+"\" size=\"4\"><b>"+selected_parameters+"</font></b></td>" +
        		"  </tr>" + parameter_html;

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
            "    <td><font face=\""+font+"\" size=\"3\">"+crs+"</font></td>" +
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
            parameter_html +
            format_html +
            "</table>" +
            "</body>" +
            "</html>";

        getEditor().setContentType("text/html");
        getEditor().setText(html);
    }
}
