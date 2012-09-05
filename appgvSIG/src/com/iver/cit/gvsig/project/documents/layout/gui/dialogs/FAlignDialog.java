/*
 * Created on 19-jul-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.gui.dialogs;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.SingletonWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Contenedor de los botones necesarios para alinear, desplazar y cambiar el
 * tama�o de los fframes.
 *
 * @author Vicente Caballero Navarro
 */
public class FAlignDialog extends JPanel implements SingletonWindow {
	private static final ImageIcon ileft = PluginServices.getIconTheme()
		.get("text-left-icon");
	private static final ImageIcon icenterV = PluginServices.getIconTheme()
		.get("text-center-v-icon");

	private static final ImageIcon iright = PluginServices.getIconTheme()
		.get("text-right-icon");
	private static final ImageIcon iup = PluginServices.getIconTheme()
		.get("text-up-icon");
	private static final ImageIcon icenterH= PluginServices.getIconTheme()
		.get("text-center-h-icon");
	private static final ImageIcon idown = PluginServices.getIconTheme()
		.get("text-down-icon");
	private static final ImageIcon idistUp = PluginServices.getIconTheme()
		.get("text-distup-icon");
	private static final ImageIcon idistCenterH = PluginServices.getIconTheme()
		.get("text-distcenterh-icon");
	private static final ImageIcon idistDown = PluginServices.getIconTheme()
		.get("text-distdown-icon");
	private static final ImageIcon idistLeft = PluginServices.getIconTheme()
		.get("text-distleft-icon");
	private static final ImageIcon idistCenterV = PluginServices.getIconTheme()
		.get("text-distcenterv-icon");
	private static final ImageIcon idistRight = PluginServices.getIconTheme()
		.get("text-distright-icon");
	private static final ImageIcon isizeWidth = PluginServices.getIconTheme()
		.get("text-size-width-icon");
	private static final ImageIcon isizeHeight = PluginServices.getIconTheme()
		.get("text-size-height-icon");
	private static final ImageIcon isizeOther = PluginServices.getIconTheme()
		.get("text-size-other-icon");
	private static final ImageIcon ispaceRight = PluginServices.getIconTheme()
		.get("text-space-right-icon");
	private static final ImageIcon ispaceDown =PluginServices.getIconTheme()
		.get("text-space-down-icon");
	private static final ImageIcon iinLayout = PluginServices.getIconTheme()
		.get("text-inlayout-icon");

	private javax.swing.JButton bleft = null;
	private javax.swing.JButton bcenterV = null;
	private javax.swing.JButton bright = null;
	private javax.swing.JButton bup = null;
	private javax.swing.JButton bcenterH = null;
	private javax.swing.JButton bdown = null;
	private javax.swing.JButton bdistUp = null;
	private Layout m_layout = null;
	private javax.swing.JLabel lAlineamiento = null;
	private javax.swing.JLabel lDistribuir = null;
	private javax.swing.JButton bdistcenterV = null;
	private javax.swing.JButton bdistDown = null;
	private javax.swing.JButton bdistLeft = null;
	private javax.swing.JButton bdistcenterH = null;
	private javax.swing.JButton bdistRight = null;
	private javax.swing.JLabel lTamano = null;
	private javax.swing.JButton bsizecenterV = null;
	private javax.swing.JButton bsizecenterH = null;
	private javax.swing.JButton bsizeother = null;
	private javax.swing.JButton bspaceRight = null;
	private javax.swing.JButton bspaceDown = null;
	private javax.swing.JLabel lEspacio = null;
	private javax.swing.JLabel lEnElMapa = null;
	private JToggleButton binLayout = null;

	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 */
	public FAlignDialog(Layout layout) {
		super();
		m_layout = layout;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);

		EventsFAlign listener = new EventsFAlign(m_layout);
		this.add(getBleft(), null);
		this.add(getBcenterV(), null);
		this.add(getBright(), null);
		this.add(getBup(), null);
		this.add(getBcenterH(), null);
		this.add(getBdown(), null);
		this.add(getBdistUp(), null);
		this.add(getLAlineamiento(), null);
		this.add(getLDistribuir(), null);
		this.add(getBdistcenterV(), null);
		this.add(getBdistDown(), null);
		this.add(getBdistLeft(), null);
		this.add(getBdistcenterH(), null);
		this.add(getBdistRight(), null);
		this.add(getLTamano(), null);
		this.add(getBsizecenterV(), null);
		this.add(getBsizecenterH(), null);
		this.add(getBsizeother(), null);
		this.add(getBspaceRight(), null);
		this.add(getBspaceDown(), null);
		this.add(getLEspacio(), null);
		this.add(getLEnElMapa(), null);
		this.add(getBinLayout(), null);
		this.setSize(310, 190);
		getBleft().addActionListener(listener);
		getBleft().setActionCommand("LEFT");
		getBcenterV().addActionListener(listener);
		getBcenterV().setActionCommand("CENTERV");
		getBright().addActionListener(listener);
		getBright().setActionCommand("RIGHT");
		getBup().addActionListener(listener);
		getBup().setActionCommand("UP");
		getBcenterH().addActionListener(listener);
		getBcenterH().setActionCommand("CENTERH");
		getBdown().addActionListener(listener);
		getBdown().setActionCommand("DOWN");
		getBdistUp().addActionListener(listener);
		getBdistUp().setActionCommand("DISTUP");
		getBdistcenterV().addActionListener(listener);
		getBdistcenterV().setActionCommand("DISTCENTERV");
		getBdistDown().addActionListener(listener);
		getBdistDown().setActionCommand("DISTDOWN");
		getBdistLeft().addActionListener(listener);
		getBdistLeft().setActionCommand("DISTLEFT");
		getBdistcenterH().addActionListener(listener);
		getBdistcenterH().setActionCommand("DISTCENTERH");
		getBdistRight().addActionListener(listener);
		getBdistRight().setActionCommand("DISTRIGHT");
		getBsizecenterV().addActionListener(listener);
		getBsizecenterV().setActionCommand("SIZECENTERV");
		getBsizecenterH().addActionListener(listener);
		getBsizecenterH().setActionCommand("SIZECENTERH");
		getBsizeother().addActionListener(listener);
		getBsizeother().setActionCommand("SIZEOTHER");
		getBspaceRight().addActionListener(listener);
		getBspaceRight().setActionCommand("SPACERIGHT");
		getBspaceDown().addActionListener(listener);
		getBspaceDown().setActionCommand("SPACEDOWN");
		getBinLayout().addActionListener(listener);
		getBinLayout().setActionCommand("INLAYOUT");
	}

	/**
	 * This method initializes bleft
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBleft() {
		if (bleft == null) {
			bleft = new javax.swing.JButton();
			bleft.setSize(24, 24);
			bleft.setIcon(ileft);
			bleft.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_a_la_izquierda"));
			bleft.setLocation(10, 30);
			bleft.setPreferredSize(new java.awt.Dimension(30, 30));
		}

		return bleft;
	}

	/**
	 * This method initializes bcenterV
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBcenterV() {
		if (bcenterV == null) {
			bcenterV = new javax.swing.JButton();
			bcenterV.setSize(24, 24);
			bcenterV.setIcon(icenterV);
			bcenterV.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_al_centro_por_el_eje_x"));
			bcenterV.setLocation(35, 30);
			bcenterV.setPreferredSize(new java.awt.Dimension(39, 20));
		}

		return bcenterV;
	}

	/**
	 * This method initializes bright
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBright() {
		if (bright == null) {
			bright = new javax.swing.JButton();
			bright.setSize(24, 24);
			bright.setIcon(iright);
			bright.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_a_la_derecha"));
			bright.setLocation(60, 30);
		}

		return bright;
	}

	/**
	 * This method initializes bup
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBup() {
		if (bup == null) {
			bup = new javax.swing.JButton();
			bup.setSize(24, 24);
			bup.setIcon(iup);
			bup.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_hacia_arriba"));
			bup.setLocation(100, 30);
		}

		return bup;
	}

	/**
	 * This method initializes bcenterH
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBcenterH() {
		if (bcenterH == null) {
			bcenterH = new javax.swing.JButton();
			bcenterH.setSize(24, 24);
			bcenterH.setIcon(icenterH);
			bcenterH.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_al_centro_por_el_eje_y"));
			bcenterH.setLocation(125, 30);
		}

		return bcenterH;
	}

	/**
	 * This method initializes bdown
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdown() {
		if (bdown == null) {
			bdown = new javax.swing.JButton();
			bdown.setSize(24, 24);
			bdown.setIcon(idown);
			bdown.setToolTipText(PluginServices.getText(this,"desplaza_los_elementos_seleccionados_hacia_abajo"));
			bdown.setLocation(150, 30);
		}

		return bdown;
	}

	/**
	 * This method initializes bdistUp
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistUp() {
		if (bdistUp == null) {
			bdistUp = new javax.swing.JButton();
			bdistUp.setSize(24, 24);
			bdistUp.setIcon(idistUp);
			bdistUp.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_de_arriba_hacia_abajo"));
			bdistUp.setLocation(100, 73);
		}

		return bdistUp;
	}

	/**
	 * This method initializes lAlineamiento
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAlineamiento() {
		if (lAlineamiento == null) {
			lAlineamiento = new javax.swing.JLabel();
			lAlineamiento.setSize(107, 13);
			lAlineamiento.setText(PluginServices.getText(this, "alineamiento"));
			lAlineamiento.setLocation(10, 10);
		}

		return lAlineamiento;
	}

	/**
	 * This method initializes lDistribuir
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDistribuir() {
		if (lDistribuir == null) {
			lDistribuir = new javax.swing.JLabel();
			lDistribuir.setBounds(10, 57, 107, 13);
			lDistribuir.setText(PluginServices.getText(this, "distribuir"));
		}

		return lDistribuir;
	}

	/**
	 * This method initializes bdistcenterV
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistcenterV() {
		if (bdistcenterV == null) {
			bdistcenterV = new javax.swing.JButton();
			bdistcenterV.setSize(24, 24);
			bdistcenterV.setIcon(idistCenterH);
			bdistcenterV.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_y_vertical"));
			bdistcenterV.setLocation(125, 73);
		}

		return bdistcenterV;
	}

	/**
	 * This method initializes bdistDown
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistDown() {
		if (bdistDown == null) {
			bdistDown = new javax.swing.JButton();
			bdistDown.setSize(24, 24);
			bdistDown.setIcon(idistDown);
			bdistDown.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_de_abajo_hacia_arriba"));
			bdistDown.setLocation(150, 73);
		}

		return bdistDown;
	}

	/**
	 * This method initializes bdistLeft
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistLeft() {
		if (bdistLeft == null) {
			bdistLeft = new javax.swing.JButton();
			bdistLeft.setSize(24, 24);
			bdistLeft.setIcon(idistLeft);
			bdistLeft.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_de_izquierda_a_derecha"));
			bdistLeft.setLocation(10, 73);
		}

		return bdistLeft;
	}

	/**
	 * This method initializes bdistcenterH
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistcenterH() {
		if (bdistcenterH == null) {
			bdistcenterH = new javax.swing.JButton();
			bdistcenterH.setSize(24, 24);
			bdistcenterH.setIcon(idistCenterV);
			bdistcenterH.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_y_horizontal"));
			bdistcenterH.setLocation(35, 73);
		}

		return bdistcenterH;
	}

	/**
	 * This method initializes bdistRight
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBdistRight() {
		if (bdistRight == null) {
			bdistRight = new javax.swing.JButton();
			bdistRight.setIcon(idistRight);
			bdistRight.setToolTipText(PluginServices.getText(this,"distribuye_los_elementos_seleccionados_de_forma_equidistante_de_derecha_a_izquierda"));
			bdistRight.setBounds(60, 73, 24, 24);
		}

		return bdistRight;
	}

	/**
	 * This method initializes lTamano
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLTamano() {
		if (lTamano == null) {
			lTamano = new javax.swing.JLabel();
			lTamano.setBounds(10, 101, 107, 13);
			lTamano.setText(PluginServices.getText(this, "coincidir_tamanyo"));
		}

		return lTamano;
	}

	/**
	 * This method initializes bsizecenterV
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBsizecenterV() {
		if (bsizecenterV == null) {
			bsizecenterV = new javax.swing.JButton();
			bsizecenterV.setIcon(isizeWidth);
			bsizecenterV.setToolTipText(PluginServices.getText(this,"cambiar_el_ancho_de_los_elementos_seleccionados_hasta_coincidir_con_el_mas_ancho"));
			bsizecenterV.setBounds(10, 115, 24, 24);
		}

		return bsizecenterV;
	}

	/**
	 * This method initializes bsizecenterH
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBsizecenterH() {
		if (bsizecenterH == null) {
			bsizecenterH = new javax.swing.JButton();
			bsizecenterH.setIcon(isizeHeight);
			bsizecenterH.setToolTipText(PluginServices.getText(this,"cambiar_el_alto_de_los_elementos_seleccionados_hasta_coincidir_con_el_mas_alto"));
			bsizecenterH.setBounds(35, 115, 24, 24);
		}

		return bsizecenterH;
	}

	/**
	 * This method initializes bsizeother
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBsizeother() {
		if (bsizeother == null) {
			bsizeother = new javax.swing.JButton();
			bsizeother.setIcon(isizeOther);
			bsizeother.setToolTipText(PluginServices.getText(this,"cambiar_el_tamano_de_los_elementos_seleccionados_hasta_coincidir_con_el_mas_grande"));
			bsizeother.setBounds(60, 115, 24, 24);
		}

		return bsizeother;
	}

	/**
	 * This method initializes bspaceRight
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBspaceRight() {
		if (bspaceRight == null) {
			bspaceRight = new javax.swing.JButton();
			bspaceRight.setIcon(ispaceRight);
			bspaceRight.setToolTipText(PluginServices.getText(this,"distribuye_el_espacio_entre_los_elementos_en_horizontal"));
			bspaceRight.setBounds(125, 115, 24, 24);
		}

		return bspaceRight;
	}

	/**
	 * This method initializes bspaceDown
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBspaceDown() {
		if (bspaceDown == null) {
			bspaceDown = new javax.swing.JButton();
			bspaceDown.setIcon(ispaceDown);
			bspaceDown.setToolTipText(PluginServices.getText(this,"distribuye_el_espacio_entre_los_elementos_en_vertical"));
			bspaceDown.setBounds(150, 115, 24, 24);
		}

		return bspaceDown;
	}

	/**
	 * This method initializes lEspacio
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLEspacio() {
		if (lEspacio == null) {
			lEspacio = new javax.swing.JLabel();
			lEspacio.setBounds(125, 100, 57, 15);
			lEspacio.setText(PluginServices.getText(this, "espacio"));
		}

		return lEspacio;
	}

	/**
	 * This method initializes lEnElMapa
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLEnElMapa() {
		if (lEnElMapa == null) {
			lEnElMapa = new javax.swing.JLabel();
			lEnElMapa.setBounds(187, 33, 77, 17);
			lEnElMapa.setText(PluginServices.getText(this, "en_el_mapa"));
		}

		return lEnElMapa;
	}

	/**
	 * This method initializes binLayout
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getBinLayout() {
		if (binLayout == null) {
			binLayout = new JToggleButton();
			binLayout.setIcon(iinLayout);
			binLayout.setToolTipText(PluginServices.getText(this,"distribuir_elementos_sobre_todo_el_layout"));
			binLayout.setBounds(199, 58, 51, 41);
		}

		return binLayout;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.SingletonWindow#getWindowModel()
	 */
	public Object getWindowModel() {
		return "AlignDialog";
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG|WindowInfo.PALETTE);
		m_viewinfo.setTitle(PluginServices.getText(this, "alinear"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
