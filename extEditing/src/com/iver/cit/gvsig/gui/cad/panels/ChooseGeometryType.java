package com.iver.cit.gvsig.gui.cad.panels;


import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.geom.Geometry;

import com.iver.andami.PluginServices;

/**
 * @author fjp
 *
 * Panel para que el usuario seleccione el driver que va a utilizar para
 * crear un tema desde cero.
 *
 */
public class ChooseGeometryType extends JWizardPanel {

	private JLabel lblSelecGeometryType = null;
	private JPanel jPanelGeometryTypeOptions = null;
	private JRadioButton jRadioButtonPoint = null;
	private JRadioButton jRadioButtonLine = null;
	private JRadioButton jRadioButtonPolygon = null;
	private JRadioButton jRadioButtonMulti = null;
	private JRadioButton jRadioButtonMultiPoint = null;
	private JTextField jTextFieldLayerName = null;
	private JLabel jLabelLayerName = null;
	private String driverName;

	private class MyInputEventListener implements CaretListener
	{
		public void caretUpdate(CaretEvent arg0) {
			if (jTextFieldLayerName.getText().length() > 0)
				setNextButtonEnabled(true);
			else
				setNextButtonEnabled(false);

		}

	}

	public ChooseGeometryType(JWizardComponents wizardComponents) {
		super(wizardComponents);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
        jLabelLayerName = new JLabel();
        jLabelLayerName.setBounds(new java.awt.Rectangle(14,9,321,22));
        jLabelLayerName.setText(PluginServices.getText(this,"enter_layer_name"));
        lblSelecGeometryType = new JLabel();
        lblSelecGeometryType.setText(PluginServices.getText(this,"select_geometry_type"));
        lblSelecGeometryType.setBounds(new java.awt.Rectangle(13,63,329,15));
        this.setLayout(null);
        this.setSize(new java.awt.Dimension(358,263));
        this.add(lblSelecGeometryType, null);
        this.add(getJPanelGeometryTypeOptions(), null);
        this.add(getJTextFieldLayerName(), null);
        this.add(jLabelLayerName, null);

	}

	/**
	 * This method initializes jPanelGeometryTypeOptions
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelGeometryTypeOptions() {
		if (jPanelGeometryTypeOptions == null) {
			jPanelGeometryTypeOptions = new JPanel();
			jPanelGeometryTypeOptions.setLayout(null);
			jPanelGeometryTypeOptions.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this,"Geometry_types"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanelGeometryTypeOptions.setPreferredSize(new java.awt.Dimension(300,400));
			jPanelGeometryTypeOptions.setBounds(new java.awt.Rectangle(14,82,326,172));
			jPanelGeometryTypeOptions.add(getJRadioButtonPoint(), null);
			jPanelGeometryTypeOptions.add(getJRadioButtonLine(), null);
			jPanelGeometryTypeOptions.add(getJRadioButtonPolygon(), null);
			jPanelGeometryTypeOptions.add(getJRadioButtonMulti(), null);
			jPanelGeometryTypeOptions.add(getJRadioButtonMultiPoint(), null);

		    //Group the radio buttons.
		    ButtonGroup group = new ButtonGroup();
		    group.add(getJRadioButtonPoint());
		    group.add(getJRadioButtonLine());
		    group.add(getJRadioButtonPolygon());
		    group.add(getJRadioButtonMulti());
		    group.add(getJRadioButtonMultiPoint());

		}
		return jPanelGeometryTypeOptions;
	}

	/**
	 * This method initializes jRadioButtonPoint
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonPoint() {
		if (jRadioButtonPoint == null) {
			jRadioButtonPoint = new JRadioButton();
			jRadioButtonPoint.setText(PluginServices.getText(this,"point_type"));
			jRadioButtonPoint.setBounds(new java.awt.Rectangle(19,31,297,23));
		}
		return jRadioButtonPoint;
	}

	/**
	 * This method initializes jRadioButtonLine
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonLine() {
		if (jRadioButtonLine == null) {
			jRadioButtonLine = new JRadioButton();
			jRadioButtonLine.setText(PluginServices.getText(this,"line_type"));
			jRadioButtonLine.setSelected(true);
			jRadioButtonLine.setBounds(new java.awt.Rectangle(19,81,297,23));
		}
		return jRadioButtonLine;
	}

	/**
	 * This method initializes jRadioButtonPolygon
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonPolygon() {
		if (jRadioButtonPolygon == null) {
			jRadioButtonPolygon = new JRadioButton();
			jRadioButtonPolygon.setText(PluginServices.getText(this,"polygon_type"));
			jRadioButtonPolygon.setBounds(new java.awt.Rectangle(19,106,297,23));
		}
		return jRadioButtonPolygon;
	}

	/**
	 * This method initializes jRadioButtonMulti
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonMulti() {
		if (jRadioButtonMulti == null) {
			jRadioButtonMulti = new JRadioButton();
			jRadioButtonMulti.setText(PluginServices.getText(this,"multi_type"));
			jRadioButtonMulti.setBounds(new java.awt.Rectangle(19,131,297,23));
		}
		return jRadioButtonMulti;
	}

	/**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getJRadioButtonMultiPoint() {
		if (jRadioButtonMultiPoint == null) {
			jRadioButtonMultiPoint = new JRadioButton();
			jRadioButtonMultiPoint.setText(PluginServices.getText(this,"multipoint_type"));
			jRadioButtonMultiPoint.setBounds(new java.awt.Rectangle(19,56,297,23));
		}
		return jRadioButtonMultiPoint;
	}

	/**
	 * En función de qué tipo de driver sea, habilitamos o deshabilitamos
	 * las opciones. Por ejemplo, si es de tipo shp, deshabilitamos
	 * multi_type
	 * @param writer
	 */
	public void setFeatureStore(FeatureStore store) {
		// En función de qué tipo de driver sea, habilitamos o deshabilitamos
		// las opciones. Por ejemplo, si es de tipo shp, deshabilitamos
		// multi_type
//		IWriteable aux = (IWriteable) store.;
//		ISpatialWriter writer = (ISpatialWriter) aux.getWriter();
//		System.out.println("Writer seleccionado:" + writer.getName());
//		driverName = store.getName();
		try {
			getJRadioButtonPoint().setEnabled(store.canWriteGeometry(Geometry.TYPES.POINT));
			getJRadioButtonMultiPoint().setEnabled(store.canWriteGeometry(Geometry.TYPES.MULTIPOINT));
			getJRadioButtonLine().setEnabled(store.canWriteGeometry(Geometry.TYPES.CURVE));
			getJRadioButtonPolygon().setEnabled(store.canWriteGeometry(Geometry.TYPES.SURFACE));
			getJRadioButtonMulti().setEnabled(store.canWriteGeometry(Geometry.TYPES.GEOMETRY));
		} catch (DataException e) {
			e.printStackTrace();
		}

	}

	public int getSelectedGeometryType()
	{
		if (getJRadioButtonPoint().isSelected())
			return Geometry.TYPES.POINT;
		if (getJRadioButtonMultiPoint().isSelected())
			return Geometry.TYPES.MULTIPOINT;
		if (getJRadioButtonLine().isSelected())
			return Geometry.TYPES.CURVE;
		if (getJRadioButtonPolygon().isSelected())
			return Geometry.TYPES.SURFACE;
		if (getJRadioButtonMulti().isSelected())
			return Geometry.TYPES.GEOMETRY;

		return -1;
	}

	/**
	 * This method initializes jTextLayerName
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldLayerName() {
		if (jTextFieldLayerName == null) {
			jTextFieldLayerName = new JTextField();
			jTextFieldLayerName.setBounds(new java.awt.Rectangle(13,36,323,20));
			jTextFieldLayerName.setText("layer1"); //PluginServices.getText(this,"new_layer"));
			jTextFieldLayerName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
			jTextFieldLayerName.addCaretListener(new MyInputEventListener());
		}
		return jTextFieldLayerName;
	}

	public String getLayerName() {
		return getJTextFieldLayerName().getText();
	}

	public String getSelectedDriver() {
		return driverName;
	}



}  //  @jve:decl-index=0:visual-constraint="10,10"
