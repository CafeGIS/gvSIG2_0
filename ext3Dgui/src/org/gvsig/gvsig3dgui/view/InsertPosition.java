/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.gvsig3dgui.view;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gvsig3dgui.ProjectView3D;
import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.planets.Planet;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

/**
 * Dialogo donde se muestran las propiedades de una vista
 * 
 * @author Fernando Gonz�lez Cort�s
 */
public class InsertPosition extends GridBagLayoutPanel implements
		SingletonWindow {

	private ProjectView3D view3D;

	private JTextField txtName;

	private JPanel jPanelSelectProjection;

	private ButtonGroup radioButtonGroup;

	private int width = 375;

	private int height = 300;

	private GridBagLayoutPanel okCancelPanel;

	private JButton okButton;

	private JButton cancelButton;

	private GridBagLayoutPanel sphericalPanel;

	private GridBagLayoutPanel cartesianasPanel;

	private GridBagLayoutPanel coordPanel;

	private JTextField coordX;

	private JTextField coordY;

	private JTextField coordZ;

	private JTextField latG;

	private JTextField lonG;

	private JTextField altura;

	private JTextField latH;

	private JTextField lonH;

	private JTextField latM;

	private JTextField lonM;

	private GridBagLayoutPanel cartesianasLatitudPanel;

	private GridBagLayoutPanel cartesianasLongitudPanel;

	/**
	 * This is the default constructor
	 * 
	 * @param f
	 *            Frame padre del dialogo
	 * @param v
	 *            Vista que se representa
	 */
	public InsertPosition(IProjectView v) {
		view3D = (ProjectView3D) v;

		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		// Inicialize component
		setName("Insercion de punto 3D");
		// Introducing the margin
		setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		// Dimension of the panel
		// setSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));

		// ADDING COMPONENTS

		// Name Component
		this.addComponent("Nombre:", getTxtName(), new Insets(0, 10, 2, 10));

		// Projection selector (Radio buton group)
//		this.addComponent("Seleccione proyecci�n:",
//				getJPanelSelectProjection(), new Insets(2, 10, 2, 10));

//		this.addComponent(getSphericalPanel());
		this.addComponent(getCoordPanel(), new Insets(2, 10, 2, 10));

		// Accept buton
		this.addComponent(getOkCancelPanel(), new Insets(2, 150, 2, 0));

		// Inicialicing default values
		coordX.setEnabled(true);
		coordY.setEnabled(true);
		coordZ.setEnabled(true);
		
		latG.setText("0");
		lonG.setText("0");
		latH.setText("0");
		lonH.setText("0");
		latM.setText("0");
		lonM.setText("0");
		latG.setEnabled(true);
		lonG.setEnabled(true);
		latH.setEnabled(true);
		lonH.setEnabled(true);
		latM.setEnabled(true);
		lonM.setEnabled(true);
		altura.setText("0");
		altura.setEnabled(true);
	}

	private javax.swing.JTextField getTxtName() {
		if (txtName == null) {
			txtName = new javax.swing.JTextField();
			txtName.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return txtName;
	}

	private JPanel getJPanelSelectProjection() {
		if (jPanelSelectProjection == null) {
			// Create JPanel
			jPanelSelectProjection = new GridBagLayoutPanel();

			if (radioButtonGroup == null) {
				radioButtonGroup = getRadioButonGroup();
			}
			for (Enumeration e = radioButtonGroup.getElements(); e
					.hasMoreElements();) {
				JRadioButton b = (JRadioButton) e.nextElement();
				jPanelSelectProjection.add(b, null);
			}

		}

		return jPanelSelectProjection;
	}

	/**
	 * Method to inicialize the radio button group
	 * 
	 * @return the radio button group
	 */
	private ButtonGroup getRadioButonGroup() {
		if (radioButtonGroup == null) {

			// Create an action for each radio button
			Action sphericalAction = new AbstractAction("spherical") {
				// This method is called whenever the radio button is pressed,
				// even if it is already selected; this method is not called
				// if the radio button was selected programmatically
				public void actionPerformed(ActionEvent evt) {
					sphericalPanel.setEnabled(true);
					cartesianasPanel.setEnabled(false);
					coordX.setEnabled(true);
					coordY.setEnabled(true);
					coordZ.setEnabled(true);
					latG.setEnabled(false);
					lonG.setEnabled(false);
					altura.setEnabled(false);

				}
			};
			Action flatAction = new AbstractAction("flat") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					sphericalPanel.setEnabled(false);
					cartesianasPanel.setEnabled(true);
					coordX.setEnabled(false);
					coordY.setEnabled(false);
					coordZ.setEnabled(false);
					latG.setEnabled(true);
					lonG.setEnabled(true);
					altura.setEnabled(true);
				}
			};

			// Create the radio buttons using the actions
			JRadioButton spherical = new JRadioButton(sphericalAction);
			JRadioButton flat = new JRadioButton(flatAction);

			// Inicialize the button properties
			spherical.setName("spherical");
			spherical.setLabel("Esferica");
			flat.setName("flat");
			flat.setLabel("Plana");

			if (view3D.getPlanetType() == Planet.CoordinateSystemType.GEOCENTRIC)
				spherical.setSelected(true);
			else
				flat.setSelected(true);

			// Associate the two buttons with a button group
			radioButtonGroup = new ButtonGroup();
			radioButtonGroup.add(spherical);
			radioButtonGroup.add(flat);
		}

		return radioButtonGroup;
	}

	private GridBagLayoutPanel getCoordPanel() {
		if (coordPanel == null) {
			coordPanel = new GridBagLayoutPanel();
			// coordPanel.addComponent(getSphericalPanel());
			coordPanel.addComponent(getCartesianasPanel());
			getSphericalPanel();
		}

		return coordPanel;
	}

	private GridBagLayoutPanel getSphericalPanel() {
		if (sphericalPanel == null) {
			sphericalPanel = new GridBagLayoutPanel();
			sphericalPanel.setSize(new java.awt.Dimension(200, 100));
			sphericalPanel.setPreferredSize(new java.awt.Dimension(200, 100));
			sphericalPanel.addComponent("X ", getCoordX());
			sphericalPanel.addComponent("Y ", getCoordY());
			sphericalPanel.addComponent("Z ", getCoordZ());
		}

		return sphericalPanel;
	}

	private javax.swing.JTextField getCoordX() {
		if (coordX == null) {
			coordX = new javax.swing.JTextField();
			coordX.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return coordX;
	}

	private javax.swing.JTextField getCoordY() {
		if (coordY == null) {
			coordY = new javax.swing.JTextField();
			coordY.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return coordY;
	}

	private javax.swing.JTextField getCoordZ() {
		if (coordZ == null) {
			coordZ = new javax.swing.JTextField();
			coordZ.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return coordZ;
	}

	private GridBagLayoutPanel getCartesianasPanel() {
		if (cartesianasPanel == null) {
			cartesianasPanel = new GridBagLayoutPanel();
			latG = new JTextField();
			latG.setPreferredSize(new java.awt.Dimension(40, 23));
			latG.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			latG.setMargin(new Insets(2, 5, 2, 5));
			lonG = new JTextField();
			lonG.setPreferredSize(new java.awt.Dimension(40, 23));
			lonG.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			lonG.setMargin(new Insets(2, 5, 2, 5));
			latH = new JTextField();
			latH.setPreferredSize(new java.awt.Dimension(40, 23));
			latH.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			lonH = new JTextField();
			lonH.setPreferredSize(new java.awt.Dimension(40, 23));
			lonH.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			latM = new JTextField();
			latM.setPreferredSize(new java.awt.Dimension(40, 23));
			latM.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			lonM = new JTextField();
			lonM.setPreferredSize(new java.awt.Dimension(40, 23));
			lonM.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			altura = new JTextField();
			altura.setPreferredSize(new java.awt.Dimension(40, 23));
			altura.setAlignmentX(GridBagLayoutPanel.LEFT_ALIGNMENT);
			cartesianasPanel.addComponent("Longitud",getCartesianasLongitudPanel());
			cartesianasPanel.addComponent("Latitud ", getCartesianasLatitudPanel());
			cartesianasPanel.addComponent("Altura  ", getHeithg());
		}

		return cartesianasPanel;
	}
	
	private GridBagLayoutPanel getCartesianasLatitudPanel() {
		if (cartesianasLatitudPanel == null) {
			cartesianasLatitudPanel = new GridBagLayoutPanel();
//			latG = new JTextField();
//			latG.setPreferredSize(new java.awt.Dimension(50, 23));
//			lonG = new JTextField();
//			lonG.setPreferredSize(new java.awt.Dimension(50, 23));
//			latH = new JTextField();
//			latH.setPreferredSize(new java.awt.Dimension(50, 23));
			cartesianasLatitudPanel.addComponent(latG,latH,latM);
		}

		return cartesianasLatitudPanel;
	}
	
	private GridBagLayoutPanel getCartesianasLongitudPanel() {
		if (cartesianasLongitudPanel == null) {
			cartesianasLongitudPanel = new GridBagLayoutPanel();
//			lonG = new JTextField();
//			lonG.setPreferredSize(new java.awt.Dimension(50, 23));
//			lonH = new JTextField();
//			lonH.setPreferredSize(new java.awt.Dimension(50, 23));
//			lonM = new JTextField();
//			lonM.setPreferredSize(new java.awt.Dimension(50, 23));
			cartesianasLongitudPanel.addComponent(lonG,lonH,lonM);
		}

		return cartesianasLongitudPanel;
	}
	


	// private javax.swing.JTextField getLat() {
	// if (latG == null) {
	// latG = new javax.swing.JTextField();
	// latG.setPreferredSize(new java.awt.Dimension(150, 23));
	// }
	//
	// return latG;
	// }
	//
	// private javax.swing.JTextField getLon() {
	// if (lonG == null) {
	// lonG = new javax.swing.JTextField();
	// lonG.setPreferredSize(new java.awt.Dimension(150, 23));
	// }
	//
	// return lonG;
	//	}

	private javax.swing.JTextField getHeithg() {
		if (altura == null) {
			altura = new javax.swing.JTextField();
			altura.setPreferredSize(new java.awt.Dimension(150, 23));
		}

		return altura;
	}

	private GridBagLayoutPanel getOkCancelPanel() {
		if (okCancelPanel == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//Pulsacion del boton de Aceptar
					
					if (latG.isEnabled()){
						double h = Double.valueOf(altura.getText()).doubleValue();
						
						double lati = Double.valueOf(latG.getText()).doubleValue();
						double longi = Double.valueOf(lonG.getText()).doubleValue();
						lati = lati + (Double.valueOf(latG.getText()).doubleValue()/60);
						longi = longi + (Double.valueOf(lonG.getText()).doubleValue()/60);
						lati = lati + (Double.valueOf(latG.getText()).doubleValue()/3600);
						longi = longi + (Double.valueOf(lonG.getText()).doubleValue()/3600);
						
//						Vec3 posicion = new Vec3(h,lati,longi);
						Vec3 posicion = new Vec3(lati,longi,h);
						view3D.insertaText(txtName.getText(),posicion);
						
					}
					PluginServices.getMDIManager().closeWindow(
							InsertPosition.this);
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PluginServices.getMDIManager().closeWindow(
							InsertPosition.this);
				}
			};

			okCancelPanel = new GridBagLayoutPanel();
			okCancelPanel.setAlignmentX(GridBagLayoutPanel.RIGHT_ALIGNMENT);
			okButton = new JButton();
			okButton.setLabel("Aceptar");
			okButton.addActionListener(okAction);
			cancelButton = new JButton();
			cancelButton.setLabel("Cancelar");
			cancelButton.addActionListener(cancelAction);

			okCancelPanel.addComponent(okButton, cancelButton);
		}
		return okCancelPanel;
	}

	public Object getWindowModel() {
		// TODO Auto-generated method stub
		return view3D;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		m_viewinfo.setTitle("Insercion_de_punto_3D");
		m_viewinfo.setHeight(height);
		m_viewinfo.setWidth(width);
		return m_viewinfo;
	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}