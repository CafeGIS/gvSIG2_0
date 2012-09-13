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
package com.iver.cit.gvsig.gui.panels;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.gvsig.gui.beans.DefaultBean;
import org.gvsig.gui.beans.Pager;
import org.gvsig.gui.beans.listeners.BeanListener;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.IFMapWMSDimension;

/**
 * This is the Dimensions tab of the WMS wizard.
 *
 * @author jaume
 *
 */
public class DimensionPanel extends DefaultBean {
	static private final int SINGLE_VALUE = 0;
	static private final int MULTIPLE_VALUE = 1;
	static private final int INTERVAL = 2;
	private final String bgColor0 = "\"#FEEDD6\""; // light salmon
	private final String bgColor2 = "\"#F2FEFF\""; // light blue
	private Pager pager = null;
	private JPanel valueEditionPanel = null;
	private JRadioButton rdBtnSingle = null;
	private JRadioButton rdBtnMultiple = null;
	private JRadioButton rdBtnInterval = null;
	private JButton btnAdd = null;
	private JTextField txt = null;
	private JScrollPane scrlDimension = null;
	private JList lstDimensions = null;
	private JPanel editionPanel = null;
	private JScrollPane scrDimInfo = null;
	private JEditorPane infoEditor = null;
	private JPanel valuePanel = null;
	private JButton btnSet = null;
	private JButton btnClear = null;
	private JLabel lblValue = null;
	private JLabel lblValueText = null;
	private int currentSelectedValue;
	private int mode;
	private boolean incomplete = true;
	private boolean userEdits = false;
	private IFMapWMSDimension[] dim;
	private IFMapWMSDimension currentDimension;
	/**
	 * <b>key:</b> FMapWMSDimension <br>
	 * <b>value:</b> value
	 */
	private Hashtable settings = new Hashtable();

	/**
	 * Will contain the settings for a getMap query which are just text. Since
	 * only one Dimension definition expression is allowed the key for this hash
	 * map is the dimension name.
	 *
	 * <b>key:</b> dimension name as string <br>
	 * <b>value:</b> dimension value as string
	 */
	private Vector indices = new Vector();

	/**
	 * This is the default constructor
	 */
	public DimensionPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(null);
		this.setSize(475, 427);
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				PluginServices.getText(this, "dimension"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.add(getValuePanel(), null);
		this.add(getEditionPanel(), null);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private Pager getJPanel() {
		if (pager == null) {
			pager = new Pager(Pager.HORIZONTAL);
			pager.setBounds(5, 20, 240, 50);
			pager.addListener(new BeanListener() {
				public void beanValueChanged(Object value) {
					currentSelectedValue = ((Integer) value).intValue();
					lblValueText.setText(currentDimension
							.valueAt(currentSelectedValue));
				}
			});
		}
		return pager;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (valueEditionPanel == null) {
			ButtonGroup group = new ButtonGroup();
			lblValueText = new JLabel();
			lblValueText.setBounds(54, 4, 190, 20);
			lblValueText.setText("");
			lblValue = new JLabel();
			lblValue.setBounds(7, 4, 45, 20);
			lblValue.setText(PluginServices.getText(this, "value") + ":");
			lblValue.setFont(new java.awt.Font("MS Sans Serif",
					java.awt.Font.BOLD, 11));
			valueEditionPanel = new JPanel();
			valueEditionPanel.setLayout(null);
			valueEditionPanel.setBounds(228, 12, 245, 214);
			valueEditionPanel.add(getRdBtnSingleValue(), null);
			valueEditionPanel.add(getRdBtnMultipleValue(), null);
			valueEditionPanel.add(getRdBtnInterval(), null);
			valueEditionPanel.add(getBtnAdd(), null);
			valueEditionPanel.add(getTxt(), null);
			valueEditionPanel.add(getBtnSet(), null);
			valueEditionPanel.add(getBtnClear(), null);
			valueEditionPanel.add(getJPanel(), null);
			valueEditionPanel.add(lblValue, null);
			valueEditionPanel.add(lblValueText, null);
			group.add(getRdBtnSingleValue());
			group.add(getRdBtnMultipleValue());
			group.add(getRdBtnInterval());
		}
		return valueEditionPanel;
	}

	/**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnSingleValue() {
		if (rdBtnSingle == null) {
			rdBtnSingle = new JRadioButton();
			rdBtnSingle.setBounds(7, 70, 180, 20);
			rdBtnSingle.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					mode = SINGLE_VALUE;
					txt.setText("");
				}
			});
			rdBtnSingle.setText(PluginServices.getText(this, "single_value"));
		}
		return rdBtnSingle;
	}

	/**
	 * This method initializes jRadioButton1
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnMultipleValue() {
		if (rdBtnMultiple == null) {
			rdBtnMultiple = new JRadioButton();
			rdBtnMultiple.setBounds(7, 89, 180, 20);
			rdBtnMultiple
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							mode = MULTIPLE_VALUE;
							txt.setText("");
						}
					});
			rdBtnMultiple.setText(PluginServices
					.getText(this, "multiple_value"));
		}
		return rdBtnMultiple;
	}

	/**
	 * This method initializes jRadioButton2
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnInterval() {
		if (rdBtnInterval == null) {
			rdBtnInterval = new JRadioButton();
			rdBtnInterval.setBounds(7, 108, 180, 20);
			rdBtnInterval
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							mode = INTERVAL;
							txt.setText("");
						}
					});
			rdBtnInterval.setText(PluginServices.getText(this, "interval"));
		}
		return rdBtnInterval;
	}

	/**
	 * This method initializes btnAdd
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAdd() {
		if (btnAdd == null) {
			btnAdd = new JButton();
			btnAdd.setBounds(7, 130, 110, 20);
			btnAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					execute("add");
				}
			});
			btnAdd.setText(PluginServices.getText(this, "add"));
		}
		return btnAdd;
	}

	private boolean execute(String actionCommand) {
		boolean b = true;
		if (actionCommand.equals("add")) {
			if (lblValueText.getText() != null && !lblValueText.equals("")) {
				Integer newIndex = new Integer(currentSelectedValue);
				switch (mode) {
				case SINGLE_VALUE:
					txt.setText(lblValueText.getText());
					indices.clear();
					indices.add(newIndex);
					break;
				case MULTIPLE_VALUE:
					String oldText = txt.getText();
					if (oldText == null || oldText.equals(""))
						txt.setText(lblValueText.getText());
					else
						txt.setText(oldText + "," + lblValueText.getText());
					indices.add(newIndex);
					break;
				case INTERVAL:
					if (txt.getText().indexOf("/") == -1) {
						txt.setText(lblValueText.getText() + "/");
						indices.add(newIndex);
						incomplete = true;
					} else {
						if (incomplete) {
							txt.setText(txt.getText() + lblValueText.getText());
							indices.set(1, newIndex);
							incomplete = false;
						}
					}
					break;
				}
			}
		} else if (actionCommand.equals("clear")) {
			txt.setText("");
			incomplete = true;
			userEdits = false;
			indices.clear();
			settings.remove(currentDimension.getName());
			callValueChanged(getDimensions());
		} else if (actionCommand.equals("set")) {

			/*
			 * Two kinds of input data is accepted: - An array of Integer
			 * representing points which are computed and obtained from the
			 * application. This ensures that it is a valid value. Depending on
			 * what is being inserted (single value, interval, or multiple
			 * values) the array will contain one, two or more Integer.
			 *  - An user custom expression. The user decides to type-in its own
			 * expression, and the application assumes that that expression is
			 * correct and just uses it.
			 */
			int type;
			Object val;
			if (userEdits) {
				type = Value.EXPR;
				val = txt.getText();
			} else {
				type = Value.INDEXES;
				val = (Integer[]) indices.toArray(new Integer[0]);
			}
			//settings.put(currentDimension, new Value(type, mode, val));
			if ((type == Value.EXPR && !((String) val).equals(""))	||
				(type == Value.INDEXES && ((Integer[]) val).length>0)){
				settings.put(currentDimension.getName(), new Value(type, mode, val, currentDimension));

			}
			callValueChanged(getDimensions());
		}
		refreshInfo();

		return b;
	}

	private void refreshInfo() {
		String font = "Arial";

		String tableRows = "";
		boolean switchColor = false;
		Iterator it = settings.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			String name = (String) it.next();
			Value val = (Value) settings.get(name);

			String dimValue;
			if (val != null) {
				if (val.type == Value.INDEXES) {
					Integer[] ints = val.getIndexes();
					String separator = val.getMode() == MULTIPLE_VALUE ? "," : "/";
					String s = "";
					for (int j = 0; j < ints.length; j++) {
						s += currentDimension.valueAt(ints[j].intValue());
						if (j < ints.length - 1)
							s += separator;
					}
					dimValue = s;
				} else
					dimValue = val.getExpr();
			} else
				dimValue = PluginServices.getText(this, "none_selected");

			IFMapWMSDimension d = val.getOwner();
			// Handle units and unit symbols
			String unitSymbol = (d !=null) ? d.getUnitSymbol() : null;
			String unit = (d !=null) ? d.getUnit() : null;
			if (unit != null && !unit.equals(""))
				unit = PluginServices.getText(this, "in") + " " + unit;
			else
				unit = "";

			if (unitSymbol != null && !unitSymbol.equals(""))
				unitSymbol = " (" + unitSymbol + ")";
			else
				unitSymbol = "";

			String color = switchColor ? bgColor0 : bgColor2;

			tableRows += "  <tr valign=\"top\" bgcolor="
					+ color
					+ ">"
					+ "    <td width=\"92\" height=\"18\" bgcolor=\"#D6D6D6\" align=\"right\"><font face=\""
					+ font + "\" size=\"3\">" + "		<b>" + name
					+ "</b>" + " " + unit + unitSymbol + "</font>" + "	 </td>"
					+ "    <td width=\"268\"><font face=\"" + font
					+ "\" size=\"3\">" + dimValue + "</font></td>" + "  </tr>";
			switchColor = !switchColor;
		}

		String html = "<html>"
				+ "<body>"
				+ "<table align=\"center\" width=\"437\" height=\"156\" border=\"0\" cellpadding=\"4\" cellspacing=\"4\">"
				+ tableRows + "</table>" + "</body>" + "</html>";
		getInfoEditor().setContentType("text/html");
		getInfoEditor().setText(html);
	}

	/**
	 * This method initializes txt
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxt() {
		if (txt == null) {
			txt = new JTextField();
			txt.setBounds(7, 162, 234, 22);
			txt.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					userEdits = true;
				}
			});
		}
		return txt;
	}

	/**
	 * This method initializes scrlDimension
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrlDimension() {
		if (scrlDimension == null) {
			scrlDimension = new JScrollPane();
			scrlDimension.setBounds(5, 17, 220, 209);
			scrlDimension.setViewportView(getLstDimensions());
		}
		return scrlDimension;
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	private JList getLstDimensions() {
		if (lstDimensions == null) {
			lstDimensions = new JList();
			lstDimensions
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstDimensions
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(javax.swing.event.ListSelectionEvent e) {
							try{
								if (currentDimension != dim[lstDimensions.getSelectedIndex()]) {
									indices.clear();
									currentDimension = dim[lstDimensions.getSelectedIndex()];
								}
							} catch (ArrayIndexOutOfBoundsException ex) {}
							refreshEditionPanel();
						}
					});
		}
		return lstDimensions;
	}

	protected void refreshEditionPanel() {
		//Value val = (Value) settings.get(currentDimension);
		Value val = (Value) settings.get(currentDimension.getName());
		if (val == null) {
			lblValueText.setText(currentDimension.valueAt(0));
			getRdBtnSingleValue().setEnabled(true);
		} else {
			int m = val.getMode();
			String separator;

			if (m == MULTIPLE_VALUE) {
				getRdBtnMultipleValue().setSelected(true);
				separator = ",";
			} else {
				separator = "/";
				if (m == SINGLE_VALUE)
					getRdBtnSingleValue().setSelected(true);
				else
					getRdBtnInterval().setSelected(true);
			}

			// text of VALUE
			if (val.type == Value.EXPR) {
				txt.setText(val.getExpr());
			} else {
				Integer[] ints = val.getIndexes();
				String s = "";
				for (int i = 0; i < ints.length; i++) {
					s += currentDimension.valueAt(ints[i].intValue());
					if (i < ints.length - 1)
						s += separator;
				}
				txt.setText(s);
			}
		}
		pager.setItemCount(currentDimension.valueCount());
		pager.setValue(0, true);
	}

	/**
	 * This method initializes editionPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getEditionPanel() {
		if (editionPanel == null) {
			editionPanel = new JPanel();
			editionPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									PluginServices.getText(this,
											"settings_editor"),
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, null));

			editionPanel.setLayout(null);
			editionPanel.setBounds(9, 150, 480, 232);
			editionPanel.add(getJPanel1(), null);
			editionPanel.add(getScrlDimension(), null);

		}
		return editionPanel;
	}

	/**
	 * This method initializes scrDimInfo
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrDimInfo() {
		if (scrDimInfo == null) {
			scrDimInfo = new JScrollPane();
			scrDimInfo.setBounds(6, 17, 466, 113);
			scrDimInfo.setViewportView(getInfoEditor());
		}
		return scrDimInfo;
	}

	/**
	 * This method initializes infoEditor
	 *
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getInfoEditor() {
		if (infoEditor == null) {
			infoEditor = new JEditorPane();
			infoEditor.setEditable(false);
		}
		return infoEditor;
	}

	/**
	 * This method initializes valuePanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getValuePanel() {
		if (valuePanel == null) {

			valuePanel = new JPanel();
			valuePanel.setLayout(null);
			valuePanel.setBounds(8, 17, 480, 137);
			valuePanel.add(getScrDimInfo(), null);
			valuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "settings"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));

		}
		return valuePanel;
	}

	/**
	 * This method initializes btnSet
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnSet() {
		if (btnSet == null) {
			btnSet = new JButton();
			btnSet.setBounds(7, 188, 110, 20);
			btnSet.setText(PluginServices.getText(this, "set"));
			btnSet.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					execute("set");
				}
			});
		}
		return btnSet;
	}

	/**
	 * This method initializes btnClear
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton();
			btnClear.setBounds(107, 130, 110, 20);
			btnClear.setText(PluginServices.getText(this, "clear"));
			btnClear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					execute("clear");
				}
			});
		}
		return btnClear;
	}

	public void addDimension(IFMapWMSDimension d) throws IllegalArgumentException {
		d.compile();

		if (dim == null) {
			dim = new IFMapWMSDimension[] { d };
		} else {

			Vector v = new Vector();
			for (int i = 0; i < dim.length; i++) {
				v.add(dim[i]);
			}
			if (v.contains(d))
				return;
			v.add(d);
			dim = (IFMapWMSDimension[]) v.toArray(new IFMapWMSDimension[0]);
		}
		String[] texts = new String[dim.length];
		for (int i = 0; i < texts.length; i++) {
			texts[i] = dim[i].getName();
		}
		getLstDimensions().setListData(texts);
		refreshInfo();
	}

	public Vector getDimensions() {
		if (settings.isEmpty())
			return null;
		Vector v = new Vector();
		Iterator it = settings.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Object key = it.next();
			Value val = ((Value) settings.get(key));

			if (val.getType() == Value.EXPR)
				v.add((String)key + "="
						+ (String) val.getValue());
			else {
				String values = "";
				String separator = (val.getMode() == MULTIPLE_VALUE) ? ","	: "/";
				Integer[] indexes = val.getIndexes();
				for (int j = 0; j < indexes.length; j++) {
					values += val.getOwner().valueAt(indexes[j].intValue());
					if (j < indexes.length - 1)
						values += separator;
				}
				v.add((String)key + "=" + values);
			}
			i++;
		}
		return v;
	}

	/**
	 * Sets the value for a Dimension given by the dimension name. The value is
	 * set as a plain text expressions and the panel will not keep track of what
	 * position represents this value.
	 *
	 * @param name
	 * @param value
	 */
	public void setDimensionValue(String name, String value) {
		for (int i = 0; dim!=null && i < dim.length; i++) {
			if (dim[i].getName().equals(name)) {
				int myMode;
				if (value.indexOf(",") != -1)
					myMode = MULTIPLE_VALUE;
				else if (value.indexOf("/") != -1)
					myMode = INTERVAL;
				else
					myMode = SINGLE_VALUE;
				Value val = new Value(Value.EXPR, myMode, value, null);
				//settings.put(dim[i], val);
				settings.put(name, val);
			}
		}
		refreshInfo();
	}

	private class Value {
		public static final int INDEXES = 0;
		public static final int EXPR = 1;
		private int type;
		private int valueMode;
		private Object value;
		private IFMapWMSDimension owner;



		public Value(int type, int mode, Object value, IFMapWMSDimension owner) {
			this.type = type;
			this.valueMode = mode;
			this.owner = owner;
			this.value = value;
		}

		/**
		 * Returns the type of the contained value.<br>
		 * Possible values are:
		 * <ol>
		 * <li> <b>Value.INDEXES</b>, which means that the value is an Integer
		 * array that should be used to compute the actual value by using the
		 * method IFMapWMSDimension.valueAt(int i). </li>
		 * <li> <b>Value.EXPR</b>. If the value type is this, then the value is
		 * a plain String that can be directly used as much it should represent
		 * a valid value for the WMS server. </li>
		 * </ol>
		 *
		 * @return int
		 */
		public int getType() {
			return type;
		}

		/**
		 * Array of indexes that compose this value expression. Use it only when
		 * type is INDEXES.
		 * @return
		 */
		public Integer[] getIndexes() {
			return (Integer[]) value;
		}

		/**
		 * Expression of this value, if its type is EXPR
		 * @return
		 */
		public String getExpr() {
			return (String) value;
		}

		/**
		 * The mode of the value.
		 * @return one of DimensionPanel.SINGLE_VALUE, DimensionPanel.MULTIPLE_VALUE
		 * 	   or DimensionPanel.INTERVAL
		 */
		public int getMode() {
			return valueMode;
		}

		public Object getValue() {
			return value;
		}
		/**
		 * Returns a reference to the IFMapDimension that owns this dimension.
		 */
		public IFMapWMSDimension getOwner() {
			return owner;
		}

	}
}