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
* 2008 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.serverexplorer.filesystem.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectValueItem;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;

/**
 * @author jmvivo
 *
 */
public class DynObjectEditor extends JPanel implements IWindow,
		ActionListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 23898787077741411L;
	public static final int SHOW_ALL = 0;
	public static final int SHOW_ONLY_THIS_PARAMS = 1;
	public static final int HIDDE_THIS_PARAMS = 2;

	private static boolean debug_mode = false;

	private String title;

	private DynObject parameters;
	private List<DynField> fields;
	private JButton botAcept;
	private JButton botCancel;
	private JButton botRestoreDefaults;
	private boolean showButtons;
	private JPanel panButtons;
	private JPanel panParameters;
	private Map<DynField, Component> componentField;

	private Color mandatoryLabelColor = Color.red;
	private boolean modal;
	private JScrollPane parametersScroll;

	protected Map<String, Object> tempValue = new HashMap<String, Object>();
	public boolean srsShowTransformPanel;


	public DynObjectEditor(DynObject parameters) {
		this(parameters, SHOW_ALL, null);
	}


	public DynObjectEditor(DynObject parameters, int mode,
			List<String> paramsNames) {
		this(parameters, mode, paramsNames, true, false);

	}

	public DynObjectEditor(DynObject parameters, int mode,
			List<String> paramsNames, boolean showButtons,
			boolean srsShowTransformPanel) {

		super();
		this.parameters = parameters;
		this.fields = Arrays.asList(parameters.getDynClass().getDynFields());
		this.showButtons = showButtons;
		this.srsShowTransformPanel = srsShowTransformPanel;
		if (mode == SHOW_ONLY_THIS_PARAMS || mode == HIDDE_THIS_PARAMS) {
			if (paramsNames == null) {
				throw new IllegalArgumentException("must expecify a name list");
			}
			ArrayList<DynField> toShow = new ArrayList<DynField>();
			List<DynField> fields = Arrays.asList(parameters.getDynClass()
					.getDynFields());
			Iterator<DynField> iter = fields.iterator();
			DynField field;
			while (iter.hasNext()) {
				field = iter.next();

				if (mode == SHOW_ONLY_THIS_PARAMS
						&& paramsNames.contains(field.getName())) {
					toShow.add(field);
				} else if (mode == HIDDE_THIS_PARAMS
						&& !(paramsNames.contains(field.getName()))) {
					toShow.add(field);
				}
			}
			this.fields = toShow;
		} else if (mode == SHOW_ALL) {

		} else{
			throw new IllegalArgumentException();
		}
		this.initialize();


	}

	private void initialize() {
		this.setLayout(new BorderLayout(6, 6));
		this.add(this.getParametersScroll(), BorderLayout.CENTER);

		if (this.showButtons) {
			this.add(this.getButtonsPanel(), BorderLayout.SOUTH);
		}
		this.fromParamsToUI();
	}

	private JPanel getButtonsPanel() {
		if (this.panButtons == null) {
			this.panButtons = new JPanel();
			this.panButtons.setLayout(new GridBagLayout());
			GridBagConstraints constr = new GridBagConstraints();
			constr.anchor = GridBagConstraints.LAST_LINE_END;
			constr.fill = GridBagConstraints.HORIZONTAL;
			constr.weightx = 1;
			constr.weighty = 0;
			this.panButtons.add(new JLabel(), constr);

			constr = this.getDefaultParametersConstraints();
			constr.fill = GridBagConstraints.NONE;
			constr.weightx = 0;
			constr.weighty = 0;

			this.panButtons.add(this.getAcceptButton(), constr);
			this.panButtons.add(this.getCancelButton(), constr);
			this.panButtons.add(this.getRestoreDefaults(), constr);
		}
		return this.panButtons;
	}

	private JButton getRestoreDefaults() {
		if (this.botRestoreDefaults == null) {
			this.botRestoreDefaults = new JButton();
			this.botRestoreDefaults.addActionListener(this);
			this.botRestoreDefaults
					.setText(PluginServices.getText(this, "restoreDefaults"));
		}
		return this.botRestoreDefaults;
	}

	private JButton getCancelButton() {
		if (this.botCancel == null) {
			this.botCancel = new JButton();
			this.botCancel.addActionListener(this);
			this.botCancel.setText(PluginServices.getText(this, "cancel"));
		}
		return this.botCancel;
	}

	private JButton getAcceptButton() {
		if (this.botAcept == null){
			this.botAcept = new JButton();
			this.botAcept.addActionListener(this);
			this.botAcept.setText(PluginServices.getText(this, "accept"));
		}
		return this.botAcept;
	}

	private GridBagConstraints getDefaultParametersConstraints() {
		GridBagConstraints constr = new GridBagConstraints();
		constr.insets = new Insets(2, 2, 2, 2);
		constr.ipadx = 2;
		constr.ipady = 2;
		constr.anchor = GridBagConstraints.PAGE_START;
		return constr;

	}

	private Component getParametersScroll() {
		if (parametersScroll == null) {
			parametersScroll = new JScrollPane();
			parametersScroll.setViewportView(getParametersPanel());
			parametersScroll.setBorder(null);
			parametersScroll
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			parametersScroll
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		}
		return parametersScroll;
	}


	private JPanel getParametersPanel() {
		if (this.panParameters == null) {
			this.panParameters = new JPanel();
			this.panParameters.setLayout(new GridBagLayout());
//			this.panParameters.setBorder(BorderFactory
//					.createTitledBorder(this
//					.getLocalizedText("values")));

			Iterator<DynField> iter = this.fields.iterator();
			DynField field;
			while (iter.hasNext()) {
				field = iter.next();
				switch (field.getTheTypeOfAvailableValues()) {
				case DynField.SINGLE:
					this.addFieldSingle(this.panParameters, field);
					break;
				case DynField.CHOICE:
					this.addFieldChoice(this.panParameters, field);
					break;
				case DynField.RANGE:
					this.addFieldRange(this.panParameters, field);
					break;

				default:
					//FIXME lanzar un warning???
					this.addFieldSingle(this.panParameters, field);
					break;
				}

			}
			GridBagConstraints constr = new GridBagConstraints();
			constr.fill = GridBagConstraints.BOTH;
			constr.weightx = 1;
			constr.weighty = 1;
			constr.gridheight = GridBagConstraints.REMAINDER;
			constr.gridwidth = GridBagConstraints.REMAINDER;
			this.panParameters.add(new JLabel(), constr);
		}
		return this.panParameters;
	}

	private JLabel createFieldLabel(DynField field) {
		JLabel label = new JLabel();
		label.setText(PluginServices.getText(this, field.getDescription()));

		if (field.isMandatory()) {
			label.setForeground(this.mandatoryLabelColor);
		}
		return label;
	}

	private void addFieldRange(JPanel panel, DynField field) {
		GridBagConstraints constr = this.getDefaultParametersConstraints();
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.weighty = 0;
		constr.weightx = 0;

		JLabel label = this.createFieldLabel(field);
		constr.gridwidth = GridBagConstraints.RELATIVE;
		panel.add(label, constr);


		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weightx = 1;

		Object max, min;
		double dmax, dmin,step;
		max = field.getMaxValue();
		min = field.getMinValue();
		boolean decimal;

		if (!(max instanceof Number)) {
			max = null;
		}
		if (!(min instanceof Number)) {
			min = null;
		}
		dmax = 0;
		dmin = 0;
		step = 1;

		switch (field.getType()) {
		case DataTypes.INT:
			decimal = false;
			if (max == null) {
				max = Integer.MAX_VALUE;
			} else {
				dmax = ((Integer) max).doubleValue();
			}
			if (min == null) {
				dmin = Integer.MIN_VALUE;
			} else {
				dmin = ((Integer) min).doubleValue();
			}
			break;

		case DataTypes.LONG:
			decimal = false;
			if (max == null) {
				dmax = Long.MAX_VALUE;
			} else {
				dmax = ((Long) max).doubleValue();
			}
			if (min == null) {
				dmin = Long.MIN_VALUE;
			} else {
				dmin = ((Long) min).doubleValue();
			}
			break;

		case DataTypes.BYTE:
			decimal = false;
			if (max == null) {
				dmax = Byte.MAX_VALUE;
			} else {
				dmax = ((Byte) max).doubleValue();
			}
			if (min == null) {
				dmin = Byte.MIN_VALUE;
			} else {
				dmin = ((Byte) min).doubleValue();
			}
			break;

		case DataTypes.FLOAT:
			decimal = true;
			if (max == null) {
				dmax = Float.MAX_VALUE;
			} else {
				dmax = ((Float) max).doubleValue();
			}
			if (min == null) {
				dmin = Float.MIN_VALUE;
			} else {
				dmin = ((Float) min).doubleValue();
			}
			break;

		case DataTypes.DOUBLE:
			decimal = true;
			if (max == null) {
				dmax = Double.MAX_VALUE;
			} else {
				dmax = ((Double) max).doubleValue();
			}
			if (min == null) {
				dmin = Double.MIN_VALUE;
			} else {
				dmin = ((Double) min).doubleValue();
			}
			break;

		default:
			JTextField text = new JTextField();
			text.setEnabled(false);
			panel.add(text, constr);
			this.getComponentField().put(field, text);
			return;
		}
		if ((dmax -dmin) != 0){
			step = (dmax - dmin) / 20;
		}


		SpinnerModel spinnerModel;
		if (decimal){
			spinnerModel = new SpinnerNumberModel(dmin, dmin, dmax, step);
		} else{
			spinnerModel = new SpinnerNumberModel((int) dmin, (int) dmin,
					(int) dmax, (int) step);
		}
		JSpinner spinner = new JSpinner(spinnerModel);
		spinner.setName(field.getName());
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weightx = 1;
		panel.add(spinner, constr);
		this.getComponentField().put(field, spinner);
	}

	private void addFieldChoice(JPanel panel, DynField field) {
		GridBagConstraints constr = this.getDefaultParametersConstraints();
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.weighty = 0;
		constr.weightx = 0;

		JLabel label = this.createFieldLabel(field);
		constr.gridwidth = GridBagConstraints.RELATIVE;
		panel.add(label, constr);

		JComboBox combo = new JComboBox(field.getAvailableValues());
		combo.setName(field.getName());
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weightx = 1;
		panel.add(combo, constr);
		this.getComponentField().put(field, combo);

	}

	private void addFieldSingle(JPanel panel,DynField field) {
		GridBagConstraints constr = this.getDefaultParametersConstraints();
		constr.fill = GridBagConstraints.HORIZONTAL;
		constr.weighty = 0;
		constr.weightx = 0;

		JLabel label = this.createFieldLabel(field);
		constr.gridwidth = GridBagConstraints.RELATIVE;
		panel.add(label, constr);


//		JTextField text = new JTextField();
		Component input = getComponentFor(field);
		input.setName(field.getName());
		constr.gridwidth = GridBagConstraints.REMAINDER;
		constr.weightx = 1;
		panel.add(input, constr);
		this.getComponentField().put(field, input);

	}

	private Component getComponentFor(DynField field) {
		int dataType = field.getType();
		Component result = null;

		switch (dataType) {
		case DataTypes.SRS:
			result = new MyProjectionSelector();
			break;

		default:
			result = new JTextField();
			break;
		}


		return result;
	}

	class MyProjectionSelector extends JPanel {
		/**
		 *
		 */
		private static final long serialVersionUID = -6331922386501903468L;

		private CRSSelectPanel crsSelector = null;
		private IProjection srs = null;

		MyProjectionSelector(){
			super();
			init();
		}

		private void init() {
			this.setValue(null);
		}

		private void setCurrentProj(IProjection proj) {
			if (proj == null){
				srs = null;
			} else {
				srs = proj;
			}
		}

		public void setValue(IProjection proj) {
			if (crsSelector != null) {
				this.remove(crsSelector);
			}

			crsSelector = CRSSelectPanel.getPanel(proj);
			crsSelector
					.setTransPanelActive(DynObjectEditor.this.srsShowTransformPanel);
			crsSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setCurrentProj(crsSelector.getCurProj());
				}
			});

			this.add(crsSelector);
			this.doLayout();
		}

		public IProjection getValue() {
			return srs;
		}



	}

	private void fromParamsToUISingle(DynField field, Component component) {

		Object value = parameters.getDynValue(field.getName());
		JTextField text;
		switch (field.getType()) {
		case DataTypes.STRING:
			text = (JTextField) component;
			if (value == null) {
				text.setText("");
				return;
			}

			text.setText((String) value);
			break;

		case DataTypes.SRS:
			((MyProjectionSelector) component).setValue((IProjection) value);
			break;

		default:
			text = (JTextField) component;
			if (value == null) {
				text.setText("");
				return;
			}

			text.setText(value.toString());
			break;
		}


	}

	private void fromParamsToUIRange(DynField field, Component component) {
		JSpinner spinner = (JSpinner) component;

		Object value = parameters.getDynValue(field.getName());
		double dValue = 0;
		if (value == null) {
			value = field.getMinValue();
		}
		if (value instanceof Double) {
			dValue = ((Double) value).doubleValue();
		} else if (value instanceof String) {
			dValue = Double.parseDouble((String) value);
		} else {
			// FIXME ????
			dValue = 0;
		}
		spinner.setValue(dValue);

	}

	private void fromParamsToUIChoice(DynField field, Component component) {
		JComboBox combo = (JComboBox) component;
		Object value = parameters.getDynValue(field.getName());

		DynObjectValueItem item = null;
		for (int i = 0; i < field.getAvailableValues().length; i++) {
			if (value == null) {
				if (field.getAvailableValues()[i].getValue() == null) {
					item = field.getAvailableValues()[i];
					break;
				}
			} else {
				if (field.getAvailableValues()[i].getValue().equals(value)) {
					item = field.getAvailableValues()[i];
					break;
				}

			}
		}

		combo.getModel().setSelectedItem(item);

	}


	private void fromDefaultsToUISingle(DynField field, Component component) {
		JTextField text = (JTextField) component;

		if (field.getDefaultValue() == null) {
			text.setText("");
		} else if (field.getType() == DataTypes.STRING) {
			text.setText((String) field.getDefaultValue());
		} else {
			text.setText(field.getDefaultValue().toString());
		}

	}

	private void fromDefaultsToUIChoice(DynField field, Component component) {
		JComboBox combo = (JComboBox) component;
		Object value = field.getDefaultValue();

		DynObjectValueItem item = null;
		for (int i = 0; i < field.getAvailableValues().length; i++) {
			if (value == null) {
				if (field.getAvailableValues()[i].getValue() == null) {
					item = field.getAvailableValues()[i];
					break;
				}
			} else {
				if (field.getAvailableValues()[i].getValue().equals(value)) {
					item = field.getAvailableValues()[i];
					break;
				}

			}
		}

		combo.getModel().setSelectedItem(item);
	}

	private void fromDefaultsToUIRange(DynField field, Component component) {
		JSpinner spinner = (JSpinner) component;
		Object value = field.getDefaultValue();
		double dValue = 0;
		if (value == null) {
			value = field.getMinValue();
		}


		if (value instanceof Double) {
			dValue = ((Double) value).doubleValue();
		} else if (value instanceof String) {
			dValue = Double.parseDouble((String) value);
		} else {
			// FIXME ????
			dValue = 0;
		}

		spinner.setValue(dValue);
	}

	public void fromParamsToUI() {
		Iterator<Entry<DynField, Component>> iter = this.getComponentField().entrySet().iterator();
		Entry<DynField, Component> entry;
		while (iter.hasNext()) {
			entry = iter.next();

			switch (entry.getKey().getTheTypeOfAvailableValues()) {
			case DynField.SINGLE:
				this.fromParamsToUISingle(entry.getKey(),entry.getValue());
				break;
			case DynField.CHOICE:
				this.fromParamsToUIChoice(entry.getKey(), entry.getValue());
				break;
			case DynField.RANGE:
				this.fromParamsToUIRange(entry.getKey(), entry.getValue());
				break;

			default:
				// FIXME warning??
				this.fromParamsToUISingle(entry.getKey(),entry.getValue());
				break;
			}

		}
	}

	public void fromDefaultsToUI() {
		Iterator<Entry<DynField, Component>> iter = this.getComponentField()
				.entrySet().iterator();
		Entry<DynField, Component> entry;
		while (iter.hasNext()) {
			entry = iter.next();

			switch (entry.getKey().getTheTypeOfAvailableValues()) {
			case DynField.SINGLE:
				this.fromDefaultsToUISingle(entry.getKey(), entry.getValue());
				break;
			case DynField.CHOICE:
				this.fromDefaultsToUIChoice(entry.getKey(), entry.getValue());
				break;
			case DynField.RANGE:
				this.fromDefaultsToUIRange(entry.getKey(), entry.getValue());
				break;

			default:
				// FIXME warning??
				this.fromParamsToUISingle(entry.getKey(), entry.getValue());
				break;
			}

		}
	}


	public void fromUIToParams() {
		Iterator<Entry<DynField, Component>> iter = this.getComponentField()
				.entrySet().iterator();
		Entry<DynField, Component> entry;
		while (iter.hasNext()) {
			entry = iter.next();

			switch (entry.getKey().getTheTypeOfAvailableValues()) {
			case DynField.SINGLE:
				this.fromUIToParamsSingle(entry.getKey(), entry.getValue());
				break;
			case DynField.CHOICE:
				this.fromUIToParamsChoice(entry.getKey(), entry.getValue());
				break;
			case DynField.RANGE:
				this.fromUIToParamsRange(entry.getKey(), entry.getValue());
				break;

			default:
				// FIXME warning??
				this.fromUIToParamsSingle(entry.getKey(), entry.getValue());
				break;
			}

		}

	}

	private void fromUIToParamsSingle(DynField field, Component component) {
		JTextField text;

		switch (field.getType()) {
		case DataTypes.STRING:
			text = (JTextField) component;
			if (text.getText().length() == 0){
				String curValue = (String) parameters.getDynValue(field
						.getName());
				if (curValue == null || curValue.length() == 0) {
					return;
				}
			}
			parameters.setDynValue(field.getName(), text.getText());
			break;

		case DataTypes.INT:
			text = (JTextField) component;
			if (text.getText().length() == 0) {
				Integer curValue = (Integer) parameters.getDynValue(field
						.getName());
				if (curValue == null) {
					return;
				}
			}

			try{
				parameters.setDynValue(field.getName(), new Integer(Integer
						.parseInt(text.getText())));
			} catch (NumberFormatException e) {
				NotificationManager.addError(e);
			}
			break;

		case DataTypes.BOOLEAN:
			text = (JTextField) component;

			if (text.getText().length() == 0) {
				Boolean curValue = (Boolean) parameters.getDynValue(field
						.getName());
				if (curValue == null) {
					return;
				}
			}


			parameters.setDynValue(field.getName(), new Boolean(Boolean
						.parseBoolean(text.getText())));

			break;

		case DataTypes.SRS:
			parameters.setDynValue(field.getName(),
					((MyProjectionSelector) component).getValue());

			break;

		case DataTypes.OBJECT:
			//TODO


			break;
		default:
			//FIXME Warning !!!
			text = (JTextField) component;
			if (text.getText().length() == 0) {
				Object curValue = parameters.getDynValue(field.getName());
				if (curValue == null) {
					return;
				}
			}

			parameters.setDynValue(field.getName(), text.getText());
			break;
		}
	}


	private void fromUIToParamsChoice(DynField field, Component component) {
		JComboBox combo = (JComboBox) component;
		parameters.setDynValue(field.getName(), ((DynObjectValueItem) combo
				.getSelectedItem()).getValue());
	}

	private void fromUIToParamsRange(DynField field, Component component) {
		JSpinner spinner = (JSpinner) component;
		parameters.setDynValue(field.getName(), spinner.getValue());

	}


	private Map<DynField, Component> getComponentField() {
		if (this.componentField == null) {
			this.componentField = new HashMap<DynField, Component>();
		}
		return componentField;

	}


	public void actionPerformed(ActionEvent e) {
		Component source = (Component) e.getSource();
		if (source == this.botAcept) {
			this.fromUIToParams();
			this.closeWindow();

		} else if (source == this.botCancel) {
			//TODO Close windows
			this.closeWindow();
		} else if (source == this.botRestoreDefaults) {
			this.fromDefaultsToUI();

		}
		if (!this.getComponentField().containsValue(e.getSource())) {
			return;
		}
		System.out.println("Action perform:" + source);

	}

	protected void closeWindow() {
		if (debug_mode) {
			System.out.println("Resut:");
			Iterator<DynField> iter = Arrays.asList(
					this.parameters.getDynClass().getDynFields()).iterator();
			DynField field;
			while (iter.hasNext()) {
				field = iter.next();
				System.out.println("\t"
						+ field.getName()
						+ " = "
						+ this.parameters.getDynValue(field.getName())
								.toString());
			}

		}
		if (PluginServices.getMainFrame() == null) {
			((JFrame) (getParent().getParent().getParent().getParent()))
					.dispose();
		} else {
			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo;
		if (this.modal) {
			m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG
					| WindowInfo.RESIZABLE);
		} else {
			m_viewinfo = new WindowInfo(WindowInfo.RESIZABLE);
		}
		m_viewinfo.setTitle(this.title);
		m_viewinfo.setHeight(500);
		m_viewinfo.setWidth(520);
		return m_viewinfo;
	}

	/**
	 * @param mandatoryLabelColor
	 *            the mandatoryLabelColor to set
	 */
	public void setMandatoryLabelColor(Color mandatoryLabelColor) {
		this.mandatoryLabelColor = mandatoryLabelColor;
	}

	/**
	 * @return the mandatoryLabelColor
	 */
	public Color getMandatoryLabelColor() {
		return mandatoryLabelColor;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ToolsLibrary toolsLibrary = new ToolsLibrary();

			toolsLibrary.initialize();
			toolsLibrary.postInitialize();

			//			DALLibrary dalLibrary = new DALLibrary();
			//			dalLibrary.initialize();
			//			dalLibrary.postInitialize();
			//
			//			DALFileLibrary dalFileLibrary = new DALFileLibrary();
			//			dalFileLibrary.initialize();
			//			dalFileLibrary.postInitialize();
			//
			//			DBFLibrary dbfLibrary = new DBFLibrary();
			//			dbfLibrary.initialize();
			//			dbfLibrary.postInitialize();
			//
			//			SHPLibrary shpLibrary = new SHPLibrary();
			//			shpLibrary.initialize();
			//			shpLibrary.postInitialize();
			//
			//			JFrame frame = new JFrame();
			//
			//			DataParameters params;
			////
			//			//			params = DALLocator.getDataManager().createStoreParameters(
			//			//					SHPStoreProvider.NAME);
			//			//			((SHPStoreParameters) params).setSHPFileName("test.shp");
			//
			//			params = DALLocator.getDataManager()
			//					.createServerExplorerParameters(
			//							FilesystemServerExplorer.NAME);
			//
			//						params.setDynValue("root", "/");
			//			params.setDynValue("initialpath", "/home");
			//
			//			FilesystemServerExplorer fileExp = (FilesystemServerExplorer) DALLocator
			//					.getDataManager()
			//					.createServerExplorer((DataServerExplorerParameters) params);
			//
			//			params = fileExp.getAddParameters(new File("x.shp"));
			//
//
			//			DynObjectEditor editor = new DynObjectEditor(params);

			JFrame frame = new JFrame();

			DynClass dClass = ToolsLocator.getDynObjectManager()
					.createDynClass("DynObjectEditorTestClass",
							"DynClas to test DynObjectEditor");

			DynField field = dClass.addDynField("field1");
			field.setDefaultValue("field1");
			field.setDescription("field1 (simple) string");
			field.setType(DataTypes.STRING);
			field.setMandatory(true);

			field = dClass.addDynField("field2");
			field.setDefaultValue("field2");
			field.setDescription("field2 (choice) string");
			field.setType(DataTypes.STRING);
			field.setTheTypeOfAvailableValues(DynField.CHOICE);
			field.setAvailableValues(new DynObjectValueItem[] {
					new DynObjectValueItem("v1", "Value1 (v1)"),
					new DynObjectValueItem("v2", "Value2 (v2)"),
					new DynObjectValueItem("v3", "Value3 (v3)"),
					new DynObjectValueItem("v4", "Value4 (v4)"), });

			field.setDefaultValue("v3");

			field = dClass.addDynField("field3");
			field.setDescription("field3 (range) integer(-5,28)");
			field.setDefaultValue(new Integer(10));
			field.setType(DataTypes.INT);
			field.setTheTypeOfAvailableValues(DynField.RANGE);
			field.setMinValue(new Integer(-5));
			field.setMaxValue(new Integer(28));

			field = dClass.addDynField("field4");
			field.setDescription("field3 (range) float(-5.2,28.55)");
			field.setDefaultValue(new Float(10));
			field.setType(DataTypes.FLOAT);
			field.setTheTypeOfAvailableValues(DynField.RANGE);
			field.setMinValue(new Float(-5.2));
			field.setMaxValue(new Float(28.55));


			DynObject dynObj = ToolsLocator.getDynObjectManager()
					.createDynObject(dClass);

			DynObjectEditor editor = new DynObjectEditor(dynObj);
			DynObjectEditor.debug_mode = true;

			frame.setLayout(new BorderLayout());
			frame.add(editor);

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(10, 10, 400, 400);
			frame.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}

	}

	public void editObject(boolean modal) {
		this.modal = modal;
		try {
			PluginServices.getMDIManager().addWindow(this);
		} catch (Exception e) {
			JFrame frame = new JFrame();
			frame.setLayout(new BorderLayout());
			frame.add(this);
			frame.setBounds(10, 10, 400, 400);
			frame.setVisible(true);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

	public boolean isSrsShowTransformPanel() {
		return srsShowTransformPanel;
	}

	public void setSrsShowTransformPanel(boolean srsShowTransformPanel) {
		this.srsShowTransformPanel = srsShowTransformPanel;
	}

}
