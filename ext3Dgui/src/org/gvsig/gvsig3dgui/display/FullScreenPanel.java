package org.gvsig.gvsig3dgui.display;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.core.osg.Group;
import org.gvsig.osgvp.core.osg.Matrix;
import org.gvsig.osgvp.core.osg.Node;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.core.osg.Vec4;
import org.gvsig.osgvp.exceptions.node.ChildIndexOutOfBoundsExceptions;
import org.gvsig.osgvp.exceptions.node.NodeException;
import org.gvsig.osgvp.features.Polygon;
import org.gvsig.osgvp.planets.CustomTerrainManipulator;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.planets.exceptions.PlanetExtentException;
import org.gvsig.osgvp.stereoconfig.StereoConfig;
import org.gvsig.osgvp.util.CameraHUD;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.DisplaySettings;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class FullScreenPanel extends GridBagLayoutPanel implements IWindow,
		ChangeListener, IWindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7918162328474597496L;

	private WindowInfo m_viewinfo;

	private int width = 700;

	private int height = 330;

	private View3D view3D;

	private JPanel jPanel1;

	private JButton jButton2;

	private JButton jButton1;

	private JLabel jLabel2;

	private JTextField jTextField1;

	private JButton jButton3;

	private JCheckBox jCheckBox1;

	private PlanetViewer _fullScreenViewer;

	private DisplaySettings _ds;

	private JCheckBox jCheckBox2;

	private JCheckBox jCheckBox3;

	private javax.swing.JSpinner jSpinner1;
	private javax.swing.JSpinner jSpinner2;
	private javax.swing.JSpinner jSpinner3;
	private javax.swing.JSpinner jSpinner4;
	private javax.swing.JSpinner jSpinner5;

	private JLabel jLabel4;
	private JLabel jLabel3;

	public FullScreenPanel() {

		if (PluginServices.getMDIManager().getActiveWindow() instanceof View3D) {
			view3D = (View3D) PluginServices.getMDIManager().getActiveWindow();
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
		jButton2 = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton3 = new javax.swing.JButton();
		jCheckBox1 = new javax.swing.JCheckBox();
		jCheckBox2 = new javax.swing.JCheckBox();
		jCheckBox3 = new javax.swing.JCheckBox();
		jSpinner1 = new javax.swing.JSpinner();
		jSpinner2 = new javax.swing.JSpinner();
		jSpinner3 = new javax.swing.JSpinner();
		jSpinner4 = new javax.swing.JSpinner();
		jSpinner5 = new javax.swing.JSpinner();

		setLayout(new java.awt.GridBagLayout());

		jPanel1.setLayout(new java.awt.GridBagLayout());

		jButton2.setText("Cancelar");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton2, gridBagConstraints);

		jButton1.setText("Aceptar");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
		jPanel1.add(jButton1, gridBagConstraints);

		jLabel2.setText("Archivo de Configuracion");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 21);
		jPanel1.add(jLabel2, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		jPanel1.add(jTextField1, gridBagConstraints);

		jButton3.setText("Browse");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton3, gridBagConstraints);

		jCheckBox1.setText("Sincronizar camaras");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
		jPanel1.add(jCheckBox1, gridBagConstraints);
		jCheckBox1.setSelected(false);

		jCheckBox2.setText("Elegir Pantalla");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
		jPanel1.add(jCheckBox2, gridBagConstraints);

		jCheckBox2.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				jCheckBox1ItemSelected(e);
			}

		});

		jCheckBox3.setText("Modo Ventana");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 11, 0);
		jPanel1.add(jCheckBox3, gridBagConstraints);

		jCheckBox3.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				jCheckBox3ItemSelected(e);
			}

		});

		int min = 0;
		int max = 20;
		int step = 1;
		int initValue = 0;
		SpinnerNumberModel model = new SpinnerNumberModel(initValue, min, max,
				step);

		jSpinner1 = new JSpinner(model);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) jSpinner1
				.getEditor()).getTextField();
		tf.setEditable(false);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jSpinner1.setEnabled(false);
		jPanel1.add(jSpinner1, gridBagConstraints);

		min = 0;
		max = 10000;
		step = 1;
		initValue = 0;
		SpinnerNumberModel model2 = new SpinnerNumberModel(initValue, min, max,
				step);

		jLabel3.setText("Posicion Origen");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		jPanel1.add(jLabel3, gridBagConstraints);

		jSpinner2 = new JSpinner(model2);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jSpinner2.setEnabled(false);
		jPanel1.add(jSpinner2, gridBagConstraints);

		min = 0;
		max = 10000;
		step = 1;
		initValue = 0;
		SpinnerNumberModel model3 = new SpinnerNumberModel(initValue, min, max,
				step);

		jSpinner3 = new JSpinner(model3);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jSpinner3.setEnabled(false);
		jPanel1.add(jSpinner3, gridBagConstraints);

		jLabel4.setText("Resolucion");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		jPanel1.add(jLabel4, gridBagConstraints);

		min = 0;
		max = 10000;
		step = 1;
		initValue = 0;
		SpinnerNumberModel model4 = new SpinnerNumberModel(initValue, min, max,
				step);

		jSpinner4 = new JSpinner(model4);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jSpinner4.setEnabled(false);
		jSpinner4.setValue((int) 800);
		jPanel1.add(jSpinner4, gridBagConstraints);

		min = 0;
		max = 10000;
		step = 1;
		initValue = 0;
		SpinnerNumberModel model5 = new SpinnerNumberModel(initValue, min, max,
				step);

		jSpinner5 = new JSpinner(model5);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
		jSpinner5.setEnabled(false);
		jSpinner5.setValue((int) 600);
		jPanel1.add(jSpinner5, gridBagConstraints);

		add(jPanel1, new java.awt.GridBagConstraints());

	}

	protected void jCheckBox3ItemSelected(ItemEvent e) {

		if (e.getStateChange() == ItemEvent.SELECTED) {
			jSpinner2.setEnabled(true);
			jSpinner3.setEnabled(true);
			jSpinner4.setEnabled(true);
			jSpinner5.setEnabled(true);
		} else {
			jSpinner2.setEnabled(false);
			jSpinner3.setEnabled(false);
			jSpinner4.setEnabled(false);
			jSpinner5.setEnabled(false);
		}

	}

	protected void jCheckBox1ItemSelected(ItemEvent e) {

		if (e.getStateChange() == ItemEvent.SELECTED)
			jSpinner1.setEnabled(true);
		else
			jSpinner1.setEnabled(false);

	}

	protected void jButton3ActionPerformed(ActionEvent evt) {
		JFileChooser jFileChooser1 = new JFileChooser();
		int returnVal = jFileChooser1.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
			jTextField1.setText(jFileChooser1.getSelectedFile()
					.getAbsolutePath());

	}

	protected void jButton1ActionPerformed(ActionEvent evt) {

		view3D = (View3D) PluginServices.getMDIManager()
		.getActiveWindow();
		PluginServices.getMDIManager().closeWindow(this);
		new Thread(new Runnable() {

			public void run() {
				_ds = view3D.getDisplaySettings();	
				//Polygon p = null;
				boolean synchronize = jCheckBox1.isSelected();
				boolean changedManipulator = false;
				Planet planet = null;

				if (!jTextField1.getText().equals("")) {

					runMStereo();
					return;

				}

				try {
					Planet planetView3D = ((PlanetViewer) view3D.getCanvas3d().getOSGViewer())
					.getPlanet(0);
					planet = new Planet();
					planet.setWaitElevationData(true);
					planet.setCoordinateSystemType(planetView3D.getCoordinateSystemType());
					planet.setCoordinateSystemName(planetView3D.getCoordinateSystemName());
					planet.setCoordinateSystemFormat(planetView3D.getCoordinateSystemFormat());
					double extent[] = new double[4];
					extent[0] = planetView3D.getExtent().getMinX();
					extent[1] = planetView3D.getExtent().getMinY();
					extent[2] = planetView3D.getExtent().getMaxX();
					extent[3] = planetView3D.getExtent().getMaxY();
					planet.setExtent(extent);
					planet.setWaitElevationData(true);
					planet.setPlanetName(planetView3D.getPlanetName());
					
					MapContext3D mc = (MapContext3D)view3D.getMapControl().getMapContext();
					planet.init();
					mc.addCurrentLayersToPlanet(planet);
					planet.setRequestLayerListener(mc);
					
					_fullScreenViewer = new PlanetViewer();
					_fullScreenViewer.addPlanet(planet);
					_fullScreenViewer.addSpecialNode((((PlanetViewer) view3D
							.getCanvas3d().getOSGViewer()).getSpecialNodes()));

				} catch (ChildIndexOutOfBoundsExceptions e1) {

					e1.printStackTrace();
				} catch (NodeException e1) {

					e1.printStackTrace();
				} catch (PlanetExtentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Vec4 color = ((PlanetViewer) view3D.getCanvas3d()
						.getOSGViewer()).getClearColor();
				_fullScreenViewer.setClearColor(color.x(), color.y(),
						color.z(), color.w());
				_fullScreenViewer.setDisplaySettings(_ds);

				if (view3D.getCanvas3d().getOSGViewer().getCameraManipulator() == null) {
					((PlanetViewer) (view3D.getCanvas3d().getOSGViewer()))
							.restoreCustomTerrainManipulator();
					changedManipulator = true;
				}
				Matrix refView = view3D.getCanvas3d().getOSGViewer()
						.getViewMatrix();
				System.out.println("refCam view " + refView.toString());
				Camera viewCam = new Camera();
				viewCam.setViewMatrix(refView);
				_fullScreenViewer.setCamera(viewCam);

				if (synchronize)
					_fullScreenViewer.setCameraManipulator(null);

				if (!jCheckBox3.isSelected()) {

					if (jCheckBox2.isSelected()) {
						_fullScreenViewer
								.setUpViewOnSingleScreen(((SpinnerNumberModel) jSpinner1
										.getModel()).getNumber().intValue());
					}
				}

				else {

					if (jCheckBox2.isSelected()) {
						_fullScreenViewer.setUpViewInWindow(
								((SpinnerNumberModel) jSpinner2.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner3.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner4.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner5.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner1.getModel())
										.getNumber().intValue());
					} else
						_fullScreenViewer.setUpViewInWindow(
								((SpinnerNumberModel) jSpinner2.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner3.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner4.getModel())
										.getNumber().intValue(),
								((SpinnerNumberModel) jSpinner5.getModel())
										.getNumber().intValue(), 0);

				}

				while (!_fullScreenViewer.done()) {

					if (synchronize) {
						_fullScreenViewer.setFusionDistance((float) planet
								.getZoom());
						refView = view3D.getCanvas3d().getOSGViewer()
								.getViewMatrix();
						viewCam.setViewMatrix(refView);
						_fullScreenViewer.setCamera(viewCam);

					}
					_fullScreenViewer.frame();

				}
				_fullScreenViewer.releaseGLContext();
				_fullScreenViewer.dispose();
				_fullScreenViewer = null;

				if (changedManipulator) {
					((PlanetViewer) (view3D.getCanvas3d().getOSGViewer()))
							.setCameraManipulator(null);
				}
				System.gc();

			}
		}).start();

	}

	private boolean runMStereo() {
		Group g = new Group();
		PlanetViewer viewer;
		CameraHUD ch = null;
		Polygon p = null;

		try {
			Vec4 color = new Vec4(1, 1, 1, 1);
			p = new Polygon();
			p.addVertex(new Vec3(0.0, 0.0, 0.0), color);
			p.addVertex(new Vec3(392.0, 0.0, 0.0), color);
			p.addVertex(new Vec3(392.0, 82.0, 0.0), color);
			p.addVertex(new Vec3(0.0, 82.0, 0.0), color);
			p.setTexture(this.getClass().getClassLoader().getResource(
					"images/okode-stereotoolbox.png").getPath());
			p.setEnabledBlending(true);

			ch = new CameraHUD();
			ch.addChild(p);

		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		boolean synchronize = jCheckBox1.isSelected();
		boolean changedManipulator = false;
		Planet planet = null;
		StereoConfig sc = null;
		try {
			planet = ((PlanetViewer) view3D.getCanvas3d().getOSGViewer())
					.getPlanet(0);
			planet.setWaitElevationData(true);
			g.addChild(planet);
			g.addChild(((PlanetViewer) view3D.getCanvas3d().getOSGViewer())
					.getSpecialNodes());
			g.addChild(ch);

			viewer = new PlanetViewer();

			File config = new File(jTextField1.getText());
			sc = new StereoConfig();
			Node n = sc.pipes2screens1texture(g, viewer, config
					.getAbsolutePath());
			viewer.setSceneData(n);

			Vec4 color = ((PlanetViewer) view3D.getCanvas3d().getOSGViewer())
					.getClearColor();
			viewer.setClearColor(color.x(), color.y(), color.z(), color.w());
			viewer.setDisplaySettings(_ds);

			if (view3D.getCanvas3d().getOSGViewer().getCameraManipulator() == null) {
				((PlanetViewer) (view3D.getCanvas3d().getOSGViewer()))
						.restoreCustomTerrainManipulator();
				changedManipulator = true;
			}

		} catch (Exception e1) {

			e1.printStackTrace();
			return false;
		}
		Matrix refView = view3D.getCanvas3d().getOSGViewer().getViewMatrix();
		System.out.println("refCam view " + refView.toString());
		Camera viewCam = new Camera();
		viewCam.setViewMatrix(refView);
		viewer.setCamera(viewCam);

		if (synchronize) {
			viewer.setCameraManipulator(null);
		} else
			viewer.setCameraManipulator(new CustomTerrainManipulator());
		if (!jCheckBox3.isSelected()) {

			viewer.setUpViewOnSingleScreen(((SpinnerNumberModel) jSpinner1
					.getModel()).getNumber().intValue());
		}

		else {

			viewer.setUpViewInWindow(0, 0, 800, 600,
					((SpinnerNumberModel) jSpinner1.getModel()).getNumber()
							.intValue());

		}

		while (!viewer.done()) {
			if (synchronize) {
				viewer.setFusionDistance((float) planet.getZoom());
				refView = view3D.getCanvas3d().getOSGViewer().getViewMatrix();
				viewCam.setViewMatrix(refView);
				viewer.setCamera(viewCam);

			}
			sc.updateCameras(viewer);
			viewer.frame();
		}
		viewer.releaseGLContext();
		viewer.dispose();
		viewer = null;

		planet.setWaitElevationData(false);
		if (changedManipulator) {
			((PlanetViewer) (view3D.getCanvas3d().getOSGViewer()))
					.setCameraManipulator(null);
		}

		System.gc();
		return true;

	}

	protected void jButton2ActionPerformed(ActionEvent evt) {
		PluginServices.getMDIManager().closeWindow(this);

	}

	public WindowInfo getWindowInfo() {
		m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this, "FullScreen"));
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

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return WindowInfo.DIALOG_PROFILE;
	}
}
