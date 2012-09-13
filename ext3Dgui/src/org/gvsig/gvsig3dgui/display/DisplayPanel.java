package org.gvsig.gvsig3dgui.display;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.viewer.DisplaySettings;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class DisplayPanel extends GridBagLayoutPanel implements IWindow,
		ChangeListener, IWindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7937904493499301555L;

	private WindowInfo m_viewinfo;

	private int width = 480;

	private int height = 250;

	private View3D view3D;

	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JComboBox jComboBox3;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JSpinner jSpinner1;
	private javax.swing.JSpinner jSpinner2;
	private javax.swing.JSpinner jSpinner3;

	private DisplaySettings ds;

	public DisplayPanel() {

		if (PluginServices.getMDIManager().getActiveWindow() instanceof View3D) {
			view3D = (View3D) PluginServices.getMDIManager().getActiveWindow();
			ds = view3D.getDisplaySettings();
			initialize();
		}

		else
			PluginServices.getMDIManager().closeWindow(this);

	}

	public Object getWindowModel() {
		return view3D;
	}

	public void initialize() {

		java.awt.GridBagConstraints gridBagConstraints;

		jPanel1 = new javax.swing.JPanel();
		jComboBox1 = new javax.swing.JComboBox();
		jComboBox2 = new javax.swing.JComboBox();
		jButton2 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jComboBox3 = new javax.swing.JComboBox();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();

		float min = (float) 0.01;
		float max = (float) 0.1;
		float step = (float) 0.005;
		float initValue = (float) 0.065;
		SpinnerNumberModel model = new SpinnerNumberModel(initValue, min, max, step);

		jSpinner1 = new JSpinner(model);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) jSpinner1
				.getEditor()).getTextField();
		tf.setEditable(false);

		min = (float) 0.1;
		max = (float) 1.0;
		step = (float) 0.1;
		initValue = (float) 0.5;
		SpinnerNumberModel model2 = new SpinnerNumberModel(initValue, min, max, step);

		jSpinner2 = new JSpinner(model2);
		JFormattedTextField tf2 = ((JSpinner.DefaultEditor) jSpinner2
				.getEditor()).getTextField();
		tf2.setEditable(false);

		min = (float) -100.0;
		max = (float) 100.0;
		step = (float) 1.0;
		initValue = (float) 0.0;
		SpinnerNumberModel model3 = new SpinnerNumberModel(initValue, min, max, step);

		jSpinner3 = new JSpinner(model3);
		JFormattedTextField tf3 = ((JSpinner.DefaultEditor) jSpinner3
				.getEditor()).getTextField();
		tf3.setEditable(false);

		setLayout(new java.awt.GridBagLayout());

		jPanel1.setLayout(new java.awt.GridBagLayout());

		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				PluginServices.getText(this, "None"),
				PluginServices.getText(this, "Anaglyphic"),
				PluginServices.getText(this, "HorizontalInterlace"),
				PluginServices.getText(this, "VerticalInterlace"),
				PluginServices.getText(this, "HorizontalSplit"),
				PluginServices.getText(this, "LeftEye"),
				PluginServices.getText(this, "RightEye"),
				PluginServices.getText(this, "QuadBuffer"), }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jPanel1.add(jComboBox1, gridBagConstraints);

		jComboBox1.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				jComboBox1ItemSelected(e);
			}

		});

		jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				PluginServices.getText(this, "None"),
				PluginServices.getText(this, "2X"),
				PluginServices.getText(this, "4X") }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jPanel1.add(jComboBox2, gridBagConstraints);

		jComboBox2.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				jComboBox2ItemSelected(e);
			}

		});

		jButton2.setText("Cancelar");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		jPanel1.add(jButton2, gridBagConstraints);

		jButton1.setText("Aceptar");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
		jPanel1.add(jButton1, gridBagConstraints);

		jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				PluginServices.getText(this, "Monitor"),
				PluginServices.getText(this, "HeadMounted"),
				PluginServices.getText(this, "PowerWall"),
				PluginServices.getText(this, "RealityCenter") }));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		jPanel1.add(jComboBox3, gridBagConstraints);
		jComboBox3.setEnabled(false);

		jComboBox3.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				jComboBox3ItemSelected(e);
			}

		});

		jLabel1.setText("Tipo de Pantalla :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		jPanel1.add(jLabel1, gridBagConstraints);

		jLabel2.setText("Antialiasing :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jPanel1.add(jLabel2, gridBagConstraints);

		jLabel3.setText("Visualizacion Estereo :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 22);
		jPanel1.add(jLabel3, gridBagConstraints);

		jLabel4.setText("Distancia interocular :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
		jPanel1.add(jLabel4, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.ipadx = 30;
		// gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 22);
		jSpinner1.setEnabled(false);
		jPanel1.add(jSpinner1, gridBagConstraints);

		jLabel5.setText("Distancia a la pantalla :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		// gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
		jPanel1.add(jLabel5, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		gridBagConstraints.ipadx = 30;
		jSpinner2.setEnabled(false);
		jPanel1.add(jSpinner2, gridBagConstraints);

		jLabel6.setText("Increm. de distancia de fusión :");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		// gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 10);
		jPanel1.add(jLabel6, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		gridBagConstraints.ipadx = 30;
		jSpinner3.setEnabled(false);
		jPanel1.add(jSpinner3, gridBagConstraints);
		// jSpinner1.addChangeListener(new ChangeListener() {
		//		
		// public void stateChanged(ChangeEvent e) {
		// JSpinner spinner = (JSpinner) e.getSource();
		//		
		// double value = (Double) spinner.getValue();
		// ds.setEyeSeparation((float) value);
		// view3D.getCanvas3d().getOSGViewer().setDisplaySettings(ds);
		//		
		// }
		//		
		// });

		add(jPanel1, new java.awt.GridBagConstraints());

		if (ds != null)
			setDisplaySettings();

	}

	protected void jButton2ActionPerformed(ActionEvent evt) {
		PluginServices.getMDIManager().closeWindow(this);

	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

		int stereo, antialias, device;

		view3D = (View3D) PluginServices.getMDIManager().getActiveWindow();
		stereo = jComboBox1.getSelectedIndex();
		antialias = jComboBox2.getSelectedIndex();
		device = jComboBox3.getSelectedIndex();
		// ds = new DisplaySettings();
		switch (stereo) {

		case 0:
			ds.setStereo(false);
			break;

		case 1:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.ANAGLYPHIC);
			break;

		case 2:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.HORIZONTAL_INTERLACE);
			break;

		case 3:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.VERTICAL_INTERLACE);
			break;

		case 4:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.HORIZONTAL_SPLIT);
			break;

		case 5:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.LEFT_EYE);
			break;

		case 6:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.RIGHT_EYE);
			break;

		case 7:
			ds.setStereo(true);
			ds.setStereoMode(DisplaySettings.StereoMode.QUAD_BUFFER);
			break;

		}
		switch (antialias) {

		case 0:
			break;

		case 1:
			ds.setNumMultiSamples(2);
			break;

		case 2:
			ds.setNumMultiSamples(4);
			break;

		}

		switch (device) {

		case 0:
			ds.setDisplayType(DisplaySettings.DisplayType.MONITOR);
			break;

		case 1:
			ds.setDisplayType(DisplaySettings.DisplayType.HEAD_MOUNTED_DISPLAY);
			break;

		case 2:
			ds.setDisplayType(DisplaySettings.DisplayType.POWERWALL);
			break;

		case 3:
			ds.setDisplayType(DisplaySettings.DisplayType.REALITY_CENTER);
			break;

		}

		if (jSpinner1.isEnabled()) {
			
			ds.setEyeSeparation(((SpinnerNumberModel) jSpinner1.getModel()).getNumber().floatValue());
			ds.setScreenDistance(((SpinnerNumberModel) jSpinner2.getModel()).getNumber().floatValue());
			
		}
		view3D.getCanvas3d().getOSGViewer().setDisplaySettings(ds);

		PluginServices.getMDIManager().closeWindow(this);

	}

	private void jComboBox3ItemSelected(ItemEvent e) {

	}

	private void jComboBox2ItemSelected(ItemEvent e) {

	}

	private void jComboBox1ItemSelected(ItemEvent e) {
		if (e.getItem().equals(PluginServices.getText(this, "None"))) {
			jComboBox3.setEnabled(false);
			jComboBox3.setSelectedIndex(0);
			jSpinner1.setEnabled(false);
			jSpinner2.setEnabled(false);
			jSpinner3.setEnabled(false);
		} else {
			jComboBox3.setEnabled(true);
			jSpinner1.setEnabled(true);
			jSpinner2.setEnabled(true);
			jSpinner3.setEnabled(true);
		}

	}

	public WindowInfo getWindowInfo() {
		m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this, "Display"));
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}

	public void stateChanged(ChangeEvent e) {

	}

	public void windowActivated() {
		view3D = (View3D) PluginServices.getMDIManager().getActiveWindow();
		System.out.println(view3D.getName());
	}

	public void windowClosed() {

	}

	public void setDisplaySettings() {

		int stereo, antialias, device;
		
		 

		System.out.println(ds);
		if (ds.getStereo()) {

			stereo = ds.getStereoMode();
			jSpinner1.setValue(ds.getEyeSeparation());
			jSpinner2.setValue((ds.getScreenDistance()));

		}

		else
			stereo = -1;
		antialias = ds.getNumMultiSamples();
		device = ds.getDisplayType();

		switch (stereo) {

		case 0:
			jComboBox1.setSelectedIndex(7);
			break;

		case 1:
			jComboBox1.setSelectedIndex(1);
			break;

		case 2:
			jComboBox1.setSelectedIndex(4);
			break;

		case 3:
			jComboBox1.setSelectedIndex(5);
			break;

		case 4:
			jComboBox1.setSelectedIndex(6);
			break;

		case 5:
			jComboBox1.setSelectedIndex(2);
			break;

		case 6:
			jComboBox1.setSelectedIndex(3);
			break;

		default:
			jComboBox1.setSelectedIndex(0);
			break;

		}

		if (!ds.getStereo())
			jComboBox1.setSelectedIndex(0);

		switch (antialias) {

		case 0:
			jComboBox2.setSelectedIndex(0);
			break;

		case 2:
			jComboBox2.setSelectedIndex(1);
			break;

		case 4:
			jComboBox2.setSelectedIndex(2);
			break;

		}

		switch (device) {

		case 0:
			jComboBox3.setSelectedIndex(0);
			break;

		case 1:
			jComboBox3.setSelectedIndex(2);
			break;

		case 2:
			jComboBox3.setSelectedIndex(3);
			break;

		case 3:
			jComboBox3.setSelectedIndex(1);
			break;

		}

	}

	/*
	 * public DisplaySettings getDisplaySettings() {
	 * 
	 * return ds; }
	 */
	public Object getWindowProfile() {
		return null;
	}
}
