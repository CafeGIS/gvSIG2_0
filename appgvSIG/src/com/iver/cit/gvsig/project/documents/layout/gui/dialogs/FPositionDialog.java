/*
 * Created on 31-ago-2004
 *
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.project.documents.layout.gui.dialogs;

import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.LayoutControl;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo que ofrece la posibilidad de posicionar el fframe en un punto en
 * concreto del Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FPositionDialog extends JPanel implements IWindow {
	private javax.swing.JLabel lDesdeIzquierda = null;
	private javax.swing.JLabel lDesdeArriba = null;
	private javax.swing.JLabel lAnchura = null;
	private javax.swing.JLabel lAltura = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private javax.swing.JTextField tDesdeIzquierda = null;
	private javax.swing.JTextField tDesdeDerecha = null;
	private javax.swing.JTextField tAnchura = null;
	private javax.swing.JTextField tAltura = null;
	private javax.swing.JLabel lNomUnidades = null;
	private javax.swing.JLabel lUnidades = null;
	private Layout layout = null;
	private String m_NameUnit = null;
	//private ArrayList selecList = new ArrayList();
	NumberFormat nf = NumberFormat.getInstance();
	private javax.swing.JLabel lAnchoUnidades = null;
	private javax.swing.JLabel lAlto = null;
	private javax.swing.JLabel lAltoUnidades = null;
	private javax.swing.JLabel lAncho = null;
	private javax.swing.JPanel pFolio = null;
	private javax.swing.JLabel lSeparador = null;
	private IFFrame fframe;
	/**
	 * This is the default constructor
	 *
	 * @param l Referencia al Layout.
	 */
	public FPositionDialog(Layout l,IFFrame fframe) {
		super();
		layout = l;
		this.fframe=fframe;

		/*for (int i = layout.getFFrames().size() - 1; i >= 0; i--) {
			IFFrame fframe = (IFFrame) layout.getFFrames().get(i);

			if (fframe.getSelected() != FFrame.NOSELECT) {
				selecList.add(fframe);
			}
		}
		*/
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);

		if (layout.getLayoutContext().isAdjustingToGrid()) {
			nf.setMaximumFractionDigits(1);
		} else {
			nf.setMaximumFractionDigits(2);
		}

		m_NameUnit = layout.getLayoutContext().getAttributes().getNameUnit();
		this.add(getLDesdeIzquierda(), null);
		this.add(getLDesdeArriba(), null);
		this.add(getLAnchura(), null);
		this.add(getLAltura(), null);
		this.add(getBAceptar(), null);
		this.add(getBCancelar(), null);
		this.add(getTDesdeIzquierda(), null);
		this.add(getTDesdeDerecha(), null);
		this.add(getTAnchura(), null);
		this.add(getTAltura(), null);
		this.add(getLNomUnidades(), null);
		this.add(getLUnidades(), null);
		this.add(getPFolio(), null);
		this.setSize(203, 215);
	}

	/**
	 * This method initializes lDesdeIzquierda
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDesdeIzquierda() {
		if (lDesdeIzquierda == null) {
			lDesdeIzquierda = new javax.swing.JLabel();
			lDesdeIzquierda.setBounds(9, 85, 116, 20);
			lDesdeIzquierda.setText(PluginServices.getText(this,
					"desde_izquierda"));
		}

		return lDesdeIzquierda;
	}

	/**
	 * This method initializes lDesdeArriba
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDesdeArriba() {
		if (lDesdeArriba == null) {
			lDesdeArriba = new javax.swing.JLabel();
			lDesdeArriba.setBounds(9, 110, 116, 20);
			lDesdeArriba.setText(PluginServices.getText(this, "desde_arriba"));
		}

		return lDesdeArriba;
	}

	/**
	 * This method initializes lAnchura
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAnchura() {
		if (lAnchura == null) {
			lAnchura = new javax.swing.JLabel();
			lAnchura.setBounds(9, 135, 116, 20);
			lAnchura.setText(PluginServices.getText(this, "anchura"));
		}

		return lAnchura;
	}

	/**
	 * This method initializes lAltura
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAltura() {
		if (lAltura == null) {
			lAltura = new javax.swing.JLabel();
			lAltura.setBounds(9, 160, 116, 20);
			lAltura.setText(PluginServices.getText(this, "altura"));
		}

		return lAltura;
	}

	/**
	 * This method initializes bAceptar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setBounds(12, 185, 85, 23);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						//if (selecList.size() == 1) {
							Rectangle2D.Double r = new Rectangle2D.Double();

							if (getTDesdeIzquierda().getText().equals("")) {
								getTDesdeIzquierda().setText("0");
							}

							if (getTDesdeDerecha().getText().equals("")) {
								getTDesdeDerecha().setText("0");
							}

							if (getTAnchura().getText().equals("")) {
								getTAnchura().setText("0");
							}

							if (getTAltura().getText().equals("")) {
								getTAltura().setText("0");
							}

							r.x = stringToDouble(getTDesdeIzquierda().getText()
													 .toString());
							r.y = stringToDouble(getTDesdeDerecha().getText()
													 .toString());
							r.width = stringToDouble(getTAnchura().getText()
														 .toString());
							r.height = stringToDouble(getTAltura().getText()
														  .toString());

							//((IFFrame) selecList.get(0)).setBoundBox(r);
							IFFrame fframeAux=fframe.cloneFFrame(layout);
							fframeAux.setBoundBox(r);
							layout.getLayoutContext().getFrameCommandsRecord().update(fframe,fframeAux);
							layout.getLayoutContext().updateFFrames();
						//}

						PluginServices.getMDIManager().closeWindow(FPositionDialog.this);
						layout.getLayoutControl().setStatus(LayoutControl.DESACTUALIZADO);
						layout.repaint();
					}
				});
		}

		return bAceptar;
	}

	/**
	 * Paso de String a double.
	 *
	 * @param s String.
	 *
	 * @return double obtenido.
	 */
	private double stringToDouble(String s) {
		String snew = s.replace(',', '.');

		return layout.getLayoutContext().getAttributes().fromUnits(Double.parseDouble(snew));
	}

	/**
	 * This method initializes bCancelar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBCancelar() {
		if (bCancelar == null) {
			bCancelar = new javax.swing.JButton();
			bCancelar.setBounds(107, 185, 87, 23);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						PluginServices.getMDIManager().closeWindow(FPositionDialog.this);
					}
				});
		}

		return bCancelar;
	}

	/**
	 * This method initializes tDesdeIzquierda
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTDesdeIzquierda() {
		if (tDesdeIzquierda == null) {
			tDesdeIzquierda = new javax.swing.JTextField();
			tDesdeIzquierda.setBounds(132, 85, 53, 20);
			tDesdeIzquierda.setText(String.valueOf(nf.format(
						layout.getLayoutContext().getAttributes().toUnits(fframe.getBoundBox().x))));
		}

		return tDesdeIzquierda;
	}

	/**
	 * This method initializes tDesdeDerecha
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTDesdeDerecha() {
		if (tDesdeDerecha == null) {
			tDesdeDerecha = new javax.swing.JTextField();
			tDesdeDerecha.setBounds(132, 110, 53, 20);
			tDesdeDerecha.setText(String.valueOf(nf.format(
						layout.getLayoutContext().getAttributes().toUnits(fframe.getBoundBox().y))));
		}

		return tDesdeDerecha;
	}

	/**
	 * This method initializes tAnchura
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTAnchura() {
		if (tAnchura == null) {
			tAnchura = new javax.swing.JTextField();
			tAnchura.setBounds(132, 135, 53, 20);
			tAnchura.setText(String.valueOf(nf.format(
						layout.getLayoutContext().getAttributes().toUnits(fframe.getBoundBox().width))));
		}

		return tAnchura;
	}

	/**
	 * This method initializes tAltura
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTAltura() {
		if (tAltura == null) {
			tAltura = new javax.swing.JTextField();
			tAltura.setBounds(132, 160, 53, 20);
			tAltura.setText(String.valueOf(nf.format(layout.getLayoutContext().getAttributes()
														   .toUnits(fframe.getBoundBox().height))));
		}

		return tAltura;
	}

	/**
	 * This method initializes lNomUnidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLNomUnidades() {
		if (lNomUnidades == null) {
			lNomUnidades = new javax.swing.JLabel();
			lNomUnidades.setBounds(91, 7, 91, 20);
			lNomUnidades.setText(PluginServices.getText(this,m_NameUnit));
		}

		return lNomUnidades;
	}

	/**
	 * This method initializes lUnidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLUnidades() {
		if (lUnidades == null) {
			lUnidades = new javax.swing.JLabel();
			lUnidades.setBounds(7, 7, 72, 20);
			lUnidades.setText(PluginServices.getText(this, "unidades"));
		}

		return lUnidades;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);

		//vi.setResizable(false);
		m_viewinfo.setTitle(PluginServices.getText(this, "tamano_posicion"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * This method initializes lAnchoUnidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAnchoUnidades() {
		if (lAnchoUnidades == null) {
			lAnchoUnidades = new javax.swing.JLabel();

			if (layout.getLayoutContext().getAttributes().isLandSpace()) {
				lAnchoUnidades.setText(String.valueOf(nf.format(
							layout.getLayoutContext().getAttributes().getSizeInUnits().getAlto())));
			} else {
				lAnchoUnidades.setText(String.valueOf(nf.format(
							layout.getLayoutContext().getAttributes().getSizeInUnits().getAncho())));
			}
		}

		return lAnchoUnidades;
	}

	/**
	 * This method initializes lAlto
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAlto() {
		if (lAlto == null) {
			lAlto = new javax.swing.JLabel();
			lAlto.setText(PluginServices.getText(this, "alto"));
		}

		return lAlto;
	}

	/**
	 * This method initializes lAltoUnidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAltoUnidades() {
		if (lAltoUnidades == null) {
			lAltoUnidades = new javax.swing.JLabel();

			if (layout.getLayoutContext().getAttributes().isLandSpace()) {
				lAltoUnidades.setText(String.valueOf(nf.format(
							layout.getLayoutContext().getAttributes().getSizeInUnits().getAncho())));
			} else {
				lAltoUnidades.setText(String.valueOf(nf.format(
							layout.getLayoutContext().getAttributes().getSizeInUnits().getAlto())));
			}
		}

		return lAltoUnidades;
	}

	/**
	 * This method initializes lAncho
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAncho() {
		if (lAncho == null) {
			lAncho = new javax.swing.JLabel();
			lAncho.setText(PluginServices.getText(this, "ancho"));
		}

		return lAncho;
	}

	/**
	 * This method initializes pFolio
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getPFolio() {
		if (pFolio == null) {
			pFolio = new javax.swing.JPanel();
			pFolio.add(getLAltoUnidades(), null);
			pFolio.add(getLAlto(), null);
			pFolio.add(getLSeparador(), null);
			pFolio.add(getLAnchoUnidades(), null);
			pFolio.add(getLAncho(), null);
			pFolio.setBounds(9, 31, 180, 43);
			pFolio.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "tamanyo_pagina"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}

		return pFolio;
	}

	/**
	 * This method initializes lSeparador
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLSeparador() {
		if (lSeparador == null) {
			lSeparador = new javax.swing.JLabel();
			lSeparador.setText("/");
		}

		return lSeparador;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
