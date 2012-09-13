/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibï¿½ï¿½ez, 50
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
package org.gvsig.gvsig3dgui.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gvsig3d.map3d.layers.FLayers3D;
import org.gvsig.gvsig3dgui.ProjectView3D;
import org.gvsig.osgvp.planets.Planet;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.SingleLayerIterator;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * Dialogo donde se muestran las propiedades de una vista
 * 
 * @author 
 */
public class ViewProperties3D extends GridBagLayoutPanel implements
		SingletonWindow {

	private ProjectView3D view3D;

	private JTextField txtName;

	private JTextField txtDate;

	private JTextField txtOwner;

	private CRSSelectPanel jPanelProj;

	private JPanel jPanelSelectProjection;

	private ButtonGroup radioButtonGroupTypePlanet;
	
	private ButtonGroup radioButtonGroupProjection;

	private int width = 700;

	private int height = 600;

	private JButton btnColor;

	private JLabel lblColor;

	private GridBagLayoutPanel panelColor;

	private JScrollPane jScrollPane;

	private JTextArea txtComments;
	
	private Color backColor;

	private GridBagLayoutPanel okCancelPanel;
	
	private boolean editable;

	private JButton okButton;

	private JButton cancelButton;

	private JTextField verEx;

	private JTabbedPane pestanya;

	private JPanel jPanelSelectProjectionView;
	
	private IProjection  proj;
	
	private boolean projectEditable = false;

	/**
	 * This is the default constructor
	 * 
	 * @param f
	 *            Frame padre del dialogo
	 * @param v
	 *            Vista que se representa
	 */
	public ViewProperties3D(IProjectView v, boolean edit) {
		projectEditable = edit;
		view3D = (ProjectView3D) v;
		proj = view3D.getProjection();
		setEditable(edit);
		initialize();
	}

	private void addComponentGridBag(String label, Component component, int position) {
		if (label == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = position;
	        gridBagConstraints.anchor = GridBagConstraints.EAST;
	        gridBagConstraints.gridwidth = 2;
	        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
	        this.add(component, gridBagConstraints);
		} else {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = position;
	        gridBagConstraints.anchor = GridBagConstraints.WEST;
	        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
	        this.add(new JLabel(label), gridBagConstraints);
	        
			gridBagConstraints = new GridBagConstraints();
	        gridBagConstraints.gridx = 1;
	        gridBagConstraints.gridy = position;
	        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
	        this.add(component, gridBagConstraints);
		}
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new GridBagLayout());
		
		// Inicialize component
		setName(PluginServices.getText(this, "view_3D_properties"));
		// Introducing the margin 
		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		// Dimension of the panel
//		setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		
		// ADDING COMPONENTS
		
		// Name Component
		addComponentGridBag(PluginServices.getText(this, "Name"), getTxtName(), 0);

		// Date component
		addComponentGridBag(PluginServices.getText(this, "Creation_Date"), getTxtDate(), 1);

		// Owner component
		
		this.addComponentGridBag(PluginServices.getText(this, "Owner"), getTxtOwner(), 2);
		// Projection selector (Radio buton group)
		this.addComponentGridBag(PluginServices.getText(this, "Proyection_Select"), getJPanelSelectProjection(), 3);
//		this.addComponent(PluginServices.getText(this, "Proyection_Select"),
//				getJPanelSelectProjectionView(), new Insets(2,10,2,10));
		this.addComponentGridBag(null, getJPanelProj(), 4);
		
		// Description component
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        this.add(new JLabel(PluginServices.getText(this, "Commentaries")), gridBagConstraints);
        
		gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 5, 2, 5);
        this.add(getJScrollPane(), gridBagConstraints);

		// Vertical exaggeration
		this.addComponentGridBag(PluginServices.getText(this, "Vertical_Exageration"), getVerEx(), 6);
		
		// Color Panel
		panelColor = new GridBagLayoutPanel();
		panelColor.add(getLblColor());
		panelColor.add(getBtnColor());
		this.addComponentGridBag(PluginServices.getText(this, "Background_Color"), panelColor, 7);
		
		// Accept buton
		this.addComponentGridBag(null, getOkCancelPanel(), 8);
		
		// Inicialicing default values
		this.txtName.setText(PluginServices.getText(this, "Creation_Date"));
		txtName.setText(view3D.getName());
		txtDate.setText(view3D.getCreationDate());
		txtOwner.setText(view3D.getOwner());
		txtComments.setText(view3D.getComment());
		verEx.setText(Float.toString(view3D.getVerticalExaggeration()));
		backColor = view3D.getBackGroundColor();
		
	}
	
	private javax.swing.JTextField getVerEx() {
		if (verEx == null) {
			verEx = new javax.swing.JTextField();
			verEx.setHorizontalAlignment(JTextField.RIGHT);
			verEx.setColumns(2);
			verEx.setPreferredSize(new java.awt.Dimension(10, 23));
		}

		return verEx;
	}
	
	
	
	private javax.swing.JTextField getTxtName() {
		if (txtName == null) {
			txtName = new javax.swing.JTextField();
			txtName.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return txtName;
	}

	private javax.swing.JTextField getTxtDate() {
		if (txtDate == null) {
			txtDate = new javax.swing.JTextField();
			txtDate.setPreferredSize(new java.awt.Dimension(150, 23));
			txtDate.setEditable(false);
			txtDate.setBackground(java.awt.Color.white);
		}

		return txtDate;
	}

	private javax.swing.JTextField getTxtOwner() {
		if (txtOwner == null) {
			txtOwner = new javax.swing.JTextField();
			txtOwner.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return txtOwner;
	}

	private JPanel getJPanelSelectProjection() {
		if (jPanelSelectProjection == null) {
			// Create JPanel
			jPanelSelectProjection = new GridBagLayoutPanel();
			
			if (radioButtonGroupTypePlanet == null) {
				radioButtonGroupTypePlanet = getRadioButonGroupPlanetType();
			}
			for (Enumeration e = radioButtonGroupTypePlanet.getElements(); e
					.hasMoreElements();) {
				JRadioButton b = (JRadioButton) e.nextElement();
				b.setEnabled(isEditable());
				jPanelSelectProjection.add(b, null);
			}

		}

		return jPanelSelectProjection;
	}
	
	private JPanel getJPanelSelectProjectionView() {
		if (jPanelSelectProjectionView == null) {
			// Create JPanel
			jPanelSelectProjectionView = new GridBagLayoutPanel();
			
			if (radioButtonGroupProjection == null) {
				radioButtonGroupProjection = getRadioButonGroupProjection();
			}
			for (Enumeration e = radioButtonGroupProjection.getElements(); e
					.hasMoreElements();) {
				JRadioButton b = (JRadioButton) e.nextElement();
				b.setEnabled(isEditable());
				jPanelSelectProjectionView.add(b, null);
			}

		}

		return jPanelSelectProjectionView;
	}

	/**
	 * Method to inicialize the radio button group
	 * 
	 * @return the radio button group
	 */
	private ButtonGroup getRadioButonGroupPlanetType() {
		if (radioButtonGroupTypePlanet == null) {

			// Create an action for each radio button
			Action sphericalAction = new AbstractAction("spherical") {
				// This method is called whenever the radio button is pressed,
				// even if it is already selected; this method is not called
				// if the radio button was selected programmatically
				public void actionPerformed(ActionEvent evt) {
					view3D.setPlanetType(Planet.CoordinateSystemType.GEOCENTRIC);
					proj= CRSFactory.getCRS("EPSG:4326");
					setComponentProjection(proj);
					
				}
			};
			Action flatAction = new AbstractAction("flat") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					view3D.setPlanetType(Planet.CoordinateSystemType.PROJECTED);
					proj = view3D.getProjection();
					setComponentProjection(proj);
				}
			};
			Action orthoAction = new AbstractAction("ortho") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					view3D.getCanvas3d().getOSGViewer().setProjectionMatrixAsOrtho(-10, 10, -10, 10, 1, 1000);
//					view3D.setPlanetType(Planet.CoordinateSystemType.PROJECTED);
				}
			};

			// Create the radio buttons using the actions
			JRadioButton spherical = new JRadioButton(sphericalAction);
			JRadioButton flat = new JRadioButton(flatAction);
			JRadioButton ortho = new JRadioButton(orthoAction);

			// Inicialize the button properties
			spherical.setName("spherical");
			spherical.setText(PluginServices.getText(this, "Spherical"));
			flat.setName("flat");
			flat.setText(PluginServices.getText(this, "Flat"));
			ortho.setName("ortho");
			ortho.setText(PluginServices.getText(this, "Ortho"));

			if (view3D.getPlanetType()== Planet.CoordinateSystemType.GEOCENTRIC) {
				spherical.setSelected(true);
				//jPanelProj.setEnabled(false);
			}
			else
				flat.setSelected(true);

			// Associate the two buttons with a button group
			radioButtonGroupTypePlanet = new ButtonGroup();
			radioButtonGroupTypePlanet.add(spherical);
			radioButtonGroupTypePlanet.add(flat);
			
			
	//		}
//			radioButtonGroupTypePlanet.add(ortho);
		}

		return radioButtonGroupTypePlanet;
	}
	

	/**
	 * Method to inicialize the radio button group
	 * 
	 * @return the radio button group
	 */
	private ButtonGroup getRadioButonGroupProjection() {
		if (radioButtonGroupProjection == null) {

			
			Action prespectiveAction = new AbstractAction("perspective") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					// Active prespective mode into View 3D
//					view3D.getCanvas3d().getOSGViewer().setProjectionMatrixAsPerspective(arg0, arg1, arg2, arg3);
					if (!(view3D.isPrespectiveModeActive())){
						view3D.setActivePrespectiveMode(true);
					}	
				}
			};
			Action orthoAction = new AbstractAction("ortho") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					// Active ortho mode into View 3D
					if (view3D.isPrespectiveModeActive()){
						view3D.setActivePrespectiveMode(false);
					}
//					view3D.getCanvas3d().getOSGViewer().setProjectionMatrixAsOrtho(-10, 10, -10, 10, 1, 1000);
				}
			};

			// Create the radio buttons using the actions
			JRadioButton ortho = new JRadioButton(orthoAction);
			JRadioButton prespective = new JRadioButton(prespectiveAction);
			
			// Inicialize the button properties
			ortho.setName("ortho");
			ortho.setText(PluginServices.getText(this, "Ortho"));
			ortho.setSelected(false);
			prespective.setName("prespective");
			prespective.setText(PluginServices.getText(this, "Prespective"));
			prespective.setSelected(true);


			// Associate the two buttons with a button group
			radioButtonGroupProjection = new ButtonGroup();
			radioButtonGroupProjection.add(prespective);
			radioButtonGroupProjection.add(ortho);
		}

		return radioButtonGroupProjection;
	}

	private javax.swing.JLabel getLblColor() {
		if (lblColor == null) {
			lblColor = new javax.swing.JLabel();
			lblColor.setText("");
			lblColor.setPreferredSize(new java.awt.Dimension(30, 16));
			 Color theColor = view3D.getBackGroundColor();
			
			if (theColor == null)
				theColor = Color.WHITE;
			lblColor.setBackground(theColor);
			lblColor.setOpaque(true);
		}

		return lblColor;
	}

	private JButton getBtnColor() {
		if (btnColor == null) {
			btnColor = new JButton();

			btnColor.setText("...");

			btnColor.addActionListener(new java.awt.event.ActionListener() {
				

				public void actionPerformed(java.awt.event.ActionEvent e) {
					Color ret = JColorChooser.showDialog(ViewProperties3D.this,
							PluginServices.getText(this, "Select_Color"), lblColor.getBackground());

					if (ret != null) {
						lblColor.setBackground(ret);
						backColor = ret;
					} else
						lblColor.setBackground(Color.WHITE);
				}
			});
		}

		return btnColor;
	}

	
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getTxtComments());
			jScrollPane.setPreferredSize(new java.awt.Dimension(340, 70));
		}

		return jScrollPane;
	}
	
	private javax.swing.JTextArea getTxtComments() {
		if (txtComments == null) {
			txtComments = new javax.swing.JTextArea();
			txtComments.setRows(1);
			txtComments.setColumns(28);
		}

		return txtComments;
	}
	
	
	private GridBagLayoutPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					view3D.setName(txtName.getText());
					view3D.setCreationDate(txtDate.getText());
					view3D.setOwner(txtOwner.getText());
					view3D.setComment(txtComments.getText());
					view3D.setBackGroundColor(backColor);
					// TODO: poner la exageracion vertical
//					view3D.set)
//					setVerticalExaggeration();
					
					proj = jPanelProj.getCurProj();
					setVerticalEx(Float.parseFloat(verEx.getText()));
					view3D.setVerticalExaggeration(Float.parseFloat(verEx.getText()));
					view3D.setProjection(proj);
					PluginServices.getMDIManager().closeWindow(ViewProperties3D.this);
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PluginServices.getMDIManager().closeWindow(ViewProperties3D.this);
				}
			};
			
			okCancelPanel = new GridBagLayoutPanel();
			okCancelPanel.setAlignmentX(GridBagLayoutPanel.RIGHT_ALIGNMENT);
			okButton = new JButton();
			okButton.setText(PluginServices.getText(this, "Accept"));
			okButton.addActionListener(okAction);
			cancelButton = new JButton();
			cancelButton.setEnabled(!isEditable());
			cancelButton.setText(PluginServices.getText(this, "Cancel"));
			cancelButton.addActionListener(cancelAction);
			
			okCancelPanel.addComponent(okButton,cancelButton);
		}
		return okCancelPanel;
	}
	

	public Object getWindowModel() {
		return view3D;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		m_viewinfo.setTitle(PluginServices.getText(this, "view_3D_properties"));
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public void setVerticalEx(float exa){
		
		SingleLayerIterator lyrIterator = new SingleLayerIterator((FLayers3D)view3D.getMapContext().getLayers());
		while (lyrIterator.hasNext()) {
			FLayer lyr = lyrIterator.next();
			
			Layer3DProps props = Layer3DProps.getLayer3DProps(lyr);
			int type = props.getType();
			if (exa != props.getVerticalEx()){
				if (type == Layer3DProps.layer3DElevation){
					int order = props.getPlanetOrder();
					props.setVerticalEx(exa);
					view3D.getPlanet().setVerticalExaggerationLayer(order, props.getVerticalEx());
				} else if (type == Layer3DProps.layer3DVector){
					int order = props.getPlanetOrder();
					props.setVerticalEx(exa);
				}
			}
			
			
		}
	}
	
	private CRSSelectPanel getJPanelProj() {
 		
		IProjection projectionAux = null;
		IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
		if (activeWindow instanceof View3D) {
			BaseView activeView = (View3D)activeWindow;
			projectionAux = activeView.getProjection();
			activeView.setProjection(proj);
		}

		jPanelProj = CRSSelectPanel.getPanel(proj);

		if (activeWindow instanceof View3D) {
			BaseView activeView = (View3D)activeWindow;
			activeView.setProjection(projectionAux);
		}
		
		
		jPanelProj.setPreferredSize(new java.awt.Dimension(330,35));
		
		
		jPanelProj.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (jPanelProj.isOkPressed()) {
//					IProjection projectionSelected = jPanelProj.getCurProj();
//					System.out.println("Proyección seleccionada.: " + projectionSelected.getAbrev());
//						if (!jPanelProj.getCurProj().isProjected()) {
////							if (getCmbMapUnits().getItemCount()==MapContext.NAMES.length) {
////								getCmbMapUnits().addItem(PluginServices.getText(this, Attributes.DEGREES));
////							}
//							getCmbMapUnits().setSelectedItem(PluginServices.getText(this, Attributes.DEGREES));
//							getCmbMapUnits().setEnabled(false);
//						}else {
//							if (getCmbMapUnits().getSelectedItem().equals(PluginServices.getText(this, Attributes.DEGREES))) {
//								getCmbMapUnits().setSelectedIndex(1);
//								getCmbMapUnits().setEnabled(true);
//							}
////							if (!(getCmbMapUnits().getItemCount()==MapContext.NAMES.length)) {
////								getCmbMapUnits().removeItem(PluginServices.getText(this, Attributes.DEGREES));
////							}
//						}
					
						//New projection selected.
						view3D.setProjection(jPanelProj.getCurProj());
						
						
				}
			}
		});
		
		Component[] cs=jPanelProj.getComponents();
	//	 Projection options disabled in spherical view.
		if(view3D.getPlanetType() == Planet.CoordinateSystemType.GEOCENTRIC){
			proj= CRSFactory.getCRS("EPSG:4326");
			for (int i = 0; i < cs.length; i++) {
				cs[i].setEnabled(false);
			}
		}
		// Projection options disabled in flat view.
		else{
			if(!projectEditable) {
				proj = view3D.getProjection();
				for (int i = 0; i < cs.length; i++) {
						cs[i].setEnabled(false);
					}
				}
			}
		
		
		return jPanelProj;
	}
	
	private void setComponentProjection(IProjection projection) {
		proj = projection;
		this.remove(jPanelProj);
		jPanelProj = null;
		
		this.addComponentGridBag(null, getJPanelProj(), 4);
		this.validate();
		
	}
	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}
}