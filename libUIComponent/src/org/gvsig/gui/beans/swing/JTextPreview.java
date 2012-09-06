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
* $Id: JTextPreview.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.2  2006/11/28 09:30:12  ppiqueras
* Cambiado PluginServices.getText(..., ...) por Messages.getText(...)
*
* Revision 1.1  2006/09/18 08:00:34  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.swing;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.gvsig.gui.beans.Messages;

public class JTextPreview extends JEditorPane {
  private static final long serialVersionUID = -3848260639539532634L;
	private String text = null;
	private Font font = null;
	private final String template=
	"{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang3082{\\fonttbl{\\f0\\fswiss\\fprq2\\fcharset0 Arial;}{\\f1\\fswiss\\fcharset0 #FONT#;}}\r\n" +
	"{\\*\\generator Msftedit 5.41.15.1507;}\\viewkind4\\uc1\\pard#BOLD##ITALIC##UNDERLINED#f0#FONT_SIZE# #TEXT##END_UNDERLINED##NONE##END_BOLD##END_ITALIC#\\f1\\par\r\n" +
	"}";


	public JTextPreview() {
		super();
		setEditable(false);
	}

	public void setText(String text) {
		if (text == null)
			text = Messages.getText("text_preview_text");
		setContentType("text/rtf");
		this.text = text;
		if (font == null) {
			Enumeration keys = UIManager.getDefaults().keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object value = UIManager.get (key);
				if (value instanceof FontUIResource) {
					FontUIResource fur = (FontUIResource) value;
					String fontName = fur.getFontName();
					font = new Font(fontName, Font.PLAIN, 10);
					break;
				}
			}
		}

		String theText =
			template.replaceAll("#FONT#", font.getName()).
					 replaceAll("#BOLD#", (font.isBold())? "\\b": "").
					 replaceAll("#END_BOLD#", (font.isBold())? "\\b0": "").
					 replaceAll("#ITALIC#", (font.isItalic())? "\\i": "").
					 replaceAll("#END_ITALIC#", (font.isItalic())? "\\i0": "").
					 replaceAll("#UNDERLINED#", (false/*font.isUnderlined()*/)? "\\ul": "").
					 replaceAll("#END_UNDERLINED#", (false/*font.isUnderlined()*/)? "\\ul": "").
					 replaceAll("#FONT_SIZE#", "\\\\fs"+font.getSize()*2).
					 replaceAll("#NONE#", ((font.isBold() || font.isItalic() /*||font.isUnderlined()*/)? "none":"")).
					 replaceAll("#TEXT#", text);
		super.setText(theText);
	}

	public void setFont(Font font) {
		this.font = font;
		setText(text);
	}

	public void setEditable(boolean b) {
		// avoided
	}
}
