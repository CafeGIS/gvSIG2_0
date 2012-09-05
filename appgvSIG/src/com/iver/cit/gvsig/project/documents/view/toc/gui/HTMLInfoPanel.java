/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public abstract class HTMLInfoPanel extends AbstractPanel{
	private final String      bgColor0        = "\"#FEEDD6\""; // light salmon
	private final String      bgColor1        = "\"#EAEAEA\""; // light grey
	private final String      bgColor3        = "\"#FBFFE1\""; // light yellow
	private final String      bgColor4        = "\"#D6D6D6\""; // Gris
	private final String      bgColorBody     = "\"#FFFFFF\""; // white
	
	private JScrollPane       jScrollPane     = null;
	public JEditorPane        jEditorPane     = null;
	private JComboBox         jComboBox       = null;
		
	/**
	 * 
	 */
	public HTMLInfoPanel() {
		super();
		setLabel(PluginServices.getText(this, "info"));
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	protected void initialize() {
		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getJScrollPane(), BorderLayout.CENTER);		
		this.getJEditorPane().repaint();		
	}	
	
	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJEditorPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jEditorPane
	 *
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			jEditorPane.setEditable(false);
			jEditorPane.setContentType("text/html");
		}
		return jEditorPane;
	}
	
	/**
	 * Booleano que está a true cuando la fila a dibujar es par y a false cuando
	 * es impar.
	 */
	private boolean           rowColor        = true;
	
	/**
	 * Controla la alternatividad de colores en la tabla.
	 *
	 * @return Cadena con el color de la fila siguiente.
	 */
	private String getColor() {
		String color = (rowColor ? bgColor0 : bgColor1);
		rowColor = !rowColor;
		return color;
	}

	/**
	 * Obtiene una entrada de la tabla en formato HTML a partir de una propiedad,
	 * un valor y un color.
	 *
	 * @param prop
	 *          Nombre de la propiedad
	 * @param value
	 *          Valor
	 * @param color
	 *          Color
	 *
	 * @return Entrada HTML de la tabla
	 */
	protected String setHTMLBasicProperty(String prop, String value) {
		String content = "<tr valign=\"top\">";
		if (prop != null)
			content += "<td bgcolor=" + bgColor4 + "align=\"right\" width=\"33%\"><font face=\"Arial\" size=\"3\">" + prop + ":&nbsp;</font></td>";
		content += "<td bgcolor=" + getColor() + "align=\"left\" width=\"67%\"><font face=\"Arial\" size=\"3\">" + value + "</font></td>";
		content += "</tr>";

		return content;
	}

	/**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param title
	 *          Nombre del titulo
	 * @param colspan
	 *          Numero de celdas que ocupara el titulo
	 *
	 * @return Entrada HTML del titulo
	 */
	protected String setHTMLTitleTable(String title, int colspan) {
		return
			"<tr valign=\"middle\" >" +
			"<td bgcolor=" + bgColor3 + " align=\"center\" colspan=\"" + colspan + "\"><font face=\"Arial\" size=\"3\"><b> " + title + "</b></font></td>" +
			"</tr>";
	}

	/**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param content
	 *          Codigo HTML de las filas que componen la tabla.
	 *
	 * @return Entrada HTML de la tabla completa
	 */
	protected String setHTMLTable(String content) {
		return "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\">" + content + "</table>";
	}

	/**
	 * Genera el HTML para todo el contenido.
	 *
	 * @param content
	 *          Codigo HTML que ira en el <body>...</body>.
	 *
	 * @return HTML completo
	 */
	protected String setHTMLBody(String content) {
		String html = "<html>";
		html += "<body bgcolor=" + bgColorBody + " topmargin=\"0\" marginheight=\"0\">";
		html += content;
		html += "</body>";
		html += "</html>";
		return html;
	}
	
	/**
	 * Add the HTML code t the form
	 * @param content
	 */
	protected void addHTMLToPanel(String content){
		this.getJEditorPane().setContentType("text/html");
		this.getJEditorPane().setText(content);
		this.getJEditorPane().setCaretPosition(0);
	}
}

