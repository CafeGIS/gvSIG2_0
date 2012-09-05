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
* 2009 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal.serverexplorer.filesystem.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.cresques.cts.IProjection;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;

import com.iver.andami.PluginServices;

public class DynObjectViewer extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -5277036770491043233L;
	private GridBagConstraints paramsListLabelConstraint;
	private GridBagConstraints paramsListValueConstraint;
	private GridBagConstraints paramsListFillConstraint;

	public DynObjectViewer() {
		super();
		this.intialize();
	}

	private void intialize() {
		this.setLayout(new GridBagLayout());

		paramsListLabelConstraint = new GridBagConstraints();
		paramsListValueConstraint = new GridBagConstraints();
		paramsListFillConstraint = new GridBagConstraints();

		paramsListLabelConstraint.ipadx = 3;
		paramsListLabelConstraint.ipady = 3;
		paramsListLabelConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
		paramsListLabelConstraint.gridwidth = GridBagConstraints.RELATIVE;
		paramsListLabelConstraint.fill = GridBagConstraints.HORIZONTAL;

		paramsListValueConstraint.ipadx = 3;
		paramsListValueConstraint.ipady = 3;
		paramsListValueConstraint.anchor = GridBagConstraints.FIRST_LINE_END;
		paramsListValueConstraint.gridwidth = GridBagConstraints.REMAINDER;
		paramsListValueConstraint.fill = GridBagConstraints.HORIZONTAL;
		paramsListValueConstraint.weightx = 1;

		paramsListFillConstraint.ipadx = 3;
		paramsListFillConstraint.ipady = 3;
		paramsListFillConstraint.anchor = GridBagConstraints.FIRST_LINE_END;
		paramsListFillConstraint.gridwidth = GridBagConstraints.REMAINDER;
		paramsListFillConstraint.fill = GridBagConstraints.BOTH;
		paramsListFillConstraint.weightx = 1;
		paramsListFillConstraint.weighty = 1;
	}

	private String getLocalizedText(String txt) {
		try {
			return PluginServices.getText(this, txt);
		} catch (Exception e) {
			return txt;
		}
	}

	public void load(DynObject dynObj) {
		this.removeAll();

		if (dynObj == null) {
			this.doLayout();
			return;
		}
		DynClass dynClass = dynObj.getDynClass();

		JTextField label;
		JTextField text;
		Object value;
		String strValue;

		//		label = new JLabel();
		//		label.setText(getLocalizedText("parameter"));
		//		label.setBackground(Color.LIGHT_GRAY);
		//		this.add(label, paramsListLabelConstraint);

		text = new JTextField();
		text.setText(getLocalizedText("parameter"));
		text.setEditable(false);
		text.setBackground(Color.LIGHT_GRAY);
		this.add(text, paramsListLabelConstraint);

		text = new JTextField();
		text.setText(getLocalizedText("value"));
		text.setEditable(false);
		text.setBackground(Color.LIGHT_GRAY);
		this.add(text, paramsListValueConstraint);

		for (DynField field : dynClass.getDynFields()) {
			label = new JTextField();
			label.setText(field.getDescription());
			label.setEditable(false);
			this.add(label, paramsListLabelConstraint);

			strValue = "";
			value = dynObj.getDynValue(field.getName());
			if (value != null) {
				if (value instanceof String) {
					strValue = (String) value;
				} else if (value instanceof IProjection) {
					strValue = ((IProjection) value).getAbrev();
				} else {
					strValue = value.toString();
				}

			}
			text = new JTextField();
			text.setText(strValue);
			text.setEditable(false);
			this.add(text, paramsListValueConstraint);
		}

		this.add(new JLabel(), paramsListFillConstraint);

		this.doLayout();
		this.repaint();

	}
}
