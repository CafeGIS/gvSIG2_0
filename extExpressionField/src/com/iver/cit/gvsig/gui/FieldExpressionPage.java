/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
* $Id: FieldExpressionPage.java 14822 2007-10-22 12:15:32Z jmvivo $
* $Log$
* Revision 1.1  2007-09-03 10:28:47  caballero
* prepare head
*
* Revision 1.1  2007/02/09 10:49:36  caballero
* expression field
*
* Revision 1.4  2006/09/06 15:29:59  caballero
* initialize flatness
*
* Revision 1.3  2006/08/22 12:27:53  jaume
* improved performances when saving
*
* Revision 1.2  2006/08/22 07:37:32  jaume
* *** empty log message ***
*
* Revision 1.1  2006/08/10 08:18:35  caballero
* configurar grid
*
* Revision 1.1  2006/08/04 11:41:05  caballero
* poder especificar el zoom a aplicar en las vistas
*
* Revision 1.3  2006/07/31 10:02:31  jaume
* *** empty log message ***
*
* Revision 1.2  2006/06/13 07:43:08  fjp
* Ajustes sobre los cuadros de dialogos de preferencias
*
* Revision 1.1  2006/06/12 16:04:28  caballero
* Preferencias
*
* Revision 1.11  2006/06/06 10:26:31  jaume
* *** empty log message ***
*
* Revision 1.10  2006/06/05 17:07:17  jaume
* *** empty log message ***
*
* Revision 1.9  2006/06/05 17:00:44  jaume
* *** empty log message ***
*
* Revision 1.8  2006/06/05 16:57:59  jaume
* *** empty log message ***
*
* Revision 1.7  2006/06/05 14:45:06  jaume
* *** empty log message ***
*
* Revision 1.6  2006/06/05 11:00:09  jaume
* *** empty log message ***
*
* Revision 1.5  2006/06/05 10:39:02  jaume
* *** empty log message ***
*
* Revision 1.4  2006/06/05 10:13:40  jaume
* *** empty log message ***
*
* Revision 1.3  2006/06/05 10:06:08  jaume
* *** empty log message ***
*
* Revision 1.2  2006/06/05 09:51:56  jaume
* *** empty log message ***
*
* Revision 1.1  2006/06/02 10:50:18  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

/**
 * @author Vicente Caballero Navarro
 */
public class FieldExpressionPage extends AbstractPreferencePage {
	private static Preferences prefs = Preferences.userRoot().node( "fieldExpressionOptions" );
	private JTextArea jTextArea = null;
	private JTextField txtLimit;
	private JCheckBox ckLimit=null;
	private ImageIcon icon;

	public FieldExpressionPage() {
		super();
		icon=PluginServices.getIconTheme().get("field-expression-kcalc");
		addComponent(getJTextArea());

		addComponent(PluginServices.getText(this, "limit_rows_in_memory") + ":",
			txtLimit = new JTextField("", 15));
		addComponent(ckLimit = new JCheckBox(PluginServices.getText(this, "without_limit")));
		ckLimit.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if (ckLimit.isSelected()) {
					txtLimit.setText(PluginServices.getText(this, "without_limit"));
				}else {
					if (txtLimit.getText().equals(PluginServices.getText(this, "without_limit")))
					txtLimit.setText("500000");
				}

			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
			}

		});
	}

	public void initializeValues() {
		int limit = prefs.getInt("limit_rows_in_memory",-1);
		if (limit==-1) {
			ckLimit.setSelected(true);
			txtLimit.setText(PluginServices.getText(this,"without_limit"));
		}else {
			ckLimit.setSelected(false);
			txtLimit.setText(String.valueOf(limit));
		}
	}

	public String getID() {
		return this.getClass().getName();
	}

	public String getTitle() {
		return PluginServices.getText(this, "limit_rows_in_memory");
	}

	public JPanel getPanel() {
		return this;
	}

	public void storeValues() throws StoreException {
		int limit;
		try{
			if (ckLimit.isSelected()) {
				limit=-1;
			}else {
//			if (txtLimit.getText().equals(PluginServices.getText(this,"without_limit"))) {
//				limit=-1;
//			}else {
				limit=Integer.parseInt(txtLimit.getText());
			}
		}catch (Exception e) {
			throw new StoreException(PluginServices.getText(this,"limit_rows_in_memory")+PluginServices.getText(this,"error"));
		}
		prefs.putInt("limit_rows_in_memory", limit);
	}

	public void initializeDefaults() {
		int limit=prefs.getInt("limit_rows_in_memory",-1);
		if (limit==-1) {
			ckLimit.setSelected(true);
			txtLimit.setText(PluginServices.getText(this,"without_limit"));
		}else {
			ckLimit.setSelected(false);
			txtLimit.setText(String.valueOf(limit));
		}
	}

	public ImageIcon getIcon() {
		return icon;
	}
	/**
	 * This method initializes jTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setBounds(new java.awt.Rectangle(13,7,285,57));
			jTextArea.setForeground(java.awt.Color.black);
			jTextArea.setBackground(java.awt.SystemColor.control);
			jTextArea.setRows(3);
			jTextArea.setWrapStyleWord(true);
			jTextArea.setLineWrap(true);
			jTextArea.setEditable(false);
			jTextArea.setText(PluginServices.getText(this,"specifies_the_limit_rows_in_memory_when_the_program_eval_the_expression"));
		}
		return jTextArea;
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

	public void setChangesApplied() {
		setChanged(false);
	}
}
