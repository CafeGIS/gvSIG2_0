/*
 * Created on 23-jun-2004
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

import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.MapContext;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.Attributes;
import com.iver.cit.gvsig.project.documents.layout.Size;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Clase para configurar el Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FConfigLayoutDialog extends JPanel implements IWindow {
	private javax.swing.JLabel lTamPag = null;
	private javax.swing.JComboBox cbTipoFolio = null;
	private javax.swing.JLabel lDistancia = null;
	private javax.swing.JComboBox cbUnidades = null;
	private javax.swing.JLabel lAnchura = null;
	private javax.swing.JLabel lAltura = null;
	private javax.swing.JTextField tAncho = null;
	private javax.swing.JTextField tAlto = null;
	private javax.swing.JLabel lOrientacion = null;
	private javax.swing.JLabel lMargenes = null;
	private javax.swing.JCheckBox chbMargenes = null;
	private javax.swing.JLabel lSuperior = null;
	private javax.swing.JTextField tSuperior = null;
	private javax.swing.JLabel lIzquierdo = null;
	private javax.swing.JTextField tIzquierdo = null;
	private javax.swing.JLabel lInferior = null;
	private javax.swing.JTextField tInferior = null;
	private javax.swing.JLabel lDerecho = null;
	private javax.swing.JTextField tDerecho = null;
	private javax.swing.JLabel lResolucion = null;
	private javax.swing.JComboBox cbResolucion = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private Layout m_layout = null;
	private javax.swing.JCheckBox chbHorizontal = null;
	private javax.swing.JCheckBox chbVertical = null;
	private int unit = 2;
	private int type = 0;
	private boolean isLand = true;
	private boolean margin = false;
	private int resolution = 1;
	private double der = 2.54;
	private double izq = 2.54;
	private double sup = 2.54;
	private double inf = 2.54;
	private NumberFormat nf = NumberFormat.getInstance();

	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 */
	public FConfigLayoutDialog(Layout layout) {
		super();
		m_layout = layout;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		nf.setMaximumFractionDigits(2);
		this.setLayout(null);
		type = m_layout.getLayoutContext().getAttributes().getType();
		unit = m_layout.getLayoutContext().getAttributes().getSelTypeUnit();
		isLand = m_layout.getLayoutContext().getAttributes().isLandSpace();
		sup = m_layout.getLayoutContext().getAttributes().m_area[0];
		inf = m_layout.getLayoutContext().getAttributes().m_area[1];
		izq = m_layout.getLayoutContext().getAttributes().m_area[2];
		der = m_layout.getLayoutContext().getAttributes().m_area[3];
		this.add(getLTamPag(), null);
		this.add(getCbTipoFolio(), null);
		this.add(getLDistancia(), null);
		this.add(getCbUnidades(), null);
		this.add(getLAnchura(), null);
		this.add(getLAltura(), null);
		this.add(getTAncho(), null);
		this.add(getTAlto(), null);
		this.add(getLOrientacion(), null);
		this.add(getLMargenes(), null);
		this.add(getChbMargenes(), null);
		this.add(getLSuperior(), null);
		this.add(getTSuperior(), null);
		this.add(getLIzquierdo(), null);
		this.add(getTIzquierdo(), null);
		this.add(getLInferior(), null);
		this.add(getTInferior(), null);
		this.add(getLDerecho(), null);
		this.add(getTDerecho(), null);
		this.add(getLResolucion(), null);
		this.add(getCbResolucion(), null);
		this.add(getBAceptar(), null);
		this.add(getBCancelar(), null);
		this.add(getChbHorizontal(), null);
		this.add(getChbVertical(), null);
		this.setPreferredSize(new Dimension(371, 300));
	}

	/**
	 * This method initializes lTamPag
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLTamPag() {
		if (lTamPag == null) {
			lTamPag = new javax.swing.JLabel();
			lTamPag.setSize(134, 20);
			lTamPag.setText(PluginServices.getText(this, "tamano_pagina") +
				":");
			lTamPag.setLocation(25, 15);
		}

		return lTamPag;
	}

	/**
	 * This method initializes cbTipoFolio
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbTipoFolio() {
		if (cbTipoFolio == null) {
			cbTipoFolio = new javax.swing.JComboBox();
			cbTipoFolio.setSize(175, 20);
			cbTipoFolio.setPreferredSize(new java.awt.Dimension(130, 20));
			cbTipoFolio.setLocation(175, 15);
			cbTipoFolio.addItem(PluginServices.getText(this,
					"Igual_que_la_impresora"));
			cbTipoFolio.addItem(PluginServices.getText(this, "A4"));
			cbTipoFolio.addItem(PluginServices.getText(this, "A3"));
			cbTipoFolio.addItem(PluginServices.getText(this, "A2"));
			cbTipoFolio.addItem(PluginServices.getText(this, "A1"));
			cbTipoFolio.addItem(PluginServices.getText(this, "A0"));
			cbTipoFolio.addItem(PluginServices.getText(this, "Personalizado"));

			cbTipoFolio.setSelectedIndex(m_layout.getLayoutContext().getAttributes().getType());
			cbTipoFolio.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						type = cbTipoFolio.getSelectedIndex();

						Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand,
								type);
						getTAlto().setText(String.valueOf(nf.format(
									size.getAlto())));
						getTAncho().setText(String.valueOf(nf.format(
									size.getAncho())));
						setMargin(margin);
						if (cbTipoFolio.getSelectedItem().equals(PluginServices.getText(this,"Personalizado"))) {
							getTAlto().setEnabled(true);
							getTAncho().setEnabled(true);
							getChbVertical().setSelected(true);
							isLand=false;
							getChbHorizontal().setSelected(false);
							getChbVertical().setEnabled(false);
							getChbHorizontal().setEnabled(false);
						}else {
							getTAlto().setEnabled(false);
							getTAncho().setEnabled(false);
							getChbVertical().setEnabled(true);
							getChbHorizontal().setEnabled(true);
						}
					}
				});
		}

		return cbTipoFolio;
	}

	/**
	 * This method initializes lDistancia
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDistancia() {
		if (lDistancia == null) {
			lDistancia = new javax.swing.JLabel();
			lDistancia.setSize(137, 19);
			lDistancia.setText(PluginServices.getText(this, "distance_units"));
			lDistancia.setLocation(25, 40);
		}

		return lDistancia;
	}

	/**
	 * This method initializes cbUnidades
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbUnidades() {
		if (cbUnidades == null) {
			cbUnidades = new javax.swing.JComboBox();
			String[] names= MapContext.getDistanceNames();
			for (int i=0;i<names.length;i++) {
				cbUnidades.addItem(PluginServices.getText(this,names[i]));
			}
			cbUnidades.setSize(175, 20);
			cbUnidades.setLocation(175, 40);
			cbUnidades.setSelectedIndex(m_layout.getLayoutContext().getAttributes().getSelTypeUnit());
			cbUnidades.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						unit = cbUnidades.getSelectedIndex();
						m_layout.getLayoutContext().getAttributes().setUnit(unit);

						Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand,
								type);
						getTAlto().setText(String.valueOf(nf.format(
									size.getAncho())));
						getTAncho().setText(String.valueOf(nf.format(
									size.getAlto())));
						setMargin(margin);
					}
				});
		}

		return cbUnidades;
	}

	/**
	 * This method initializes lAnchura
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLAnchura() {
		if (lAnchura == null) {
			lAnchura = new javax.swing.JLabel();
			lAnchura.setSize(81, 21);
			lAnchura.setText(PluginServices.getText(this, "anchura"));
			lAnchura.setLocation(25, 65);
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
			lAltura.setSize(90, 21);
			lAltura.setText(PluginServices.getText(this, "altura"));
			lAltura.setLocation(180, 65);
		}

		return lAltura;
	}

	/**
	 * This method initializes tAncho
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTAncho() {
		if (tAncho == null) {
			tAncho = new javax.swing.JTextField();
			tAncho.setSize(60, 20);
			tAncho.setLocation(110, 65);

			Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand, type);
			String s = String.valueOf(nf.format(size.getAncho()));
			tAncho.setText(s);
			if (getCbTipoFolio().getSelectedItem().equals(PluginServices.getText(this,"Personalizado"))) {
				getTAncho().setEnabled(true);
			}else {
				getTAncho().setEnabled(false);
			}
		}

		return tAncho;
	}

	/**
	 * This method initializes tAlto
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTAlto() {
		if (tAlto == null) {
			tAlto = new javax.swing.JTextField();
			tAlto.setSize(60, 20);
			tAlto.setLocation(280, 65);

			Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand, type);
			String s = String.valueOf(nf.format(size.getAlto()));
			tAlto.setText(s);
			if (getCbTipoFolio().getSelectedItem().equals(PluginServices.getText(this,"Personalizado"))) {
				getTAlto().setEnabled(true);
			}else {
				getTAlto().setEnabled(false);
			}
		}

		return tAlto;
	}

	/**
	 * This method initializes lOrientacion
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLOrientacion() {
		if (lOrientacion == null) {
			lOrientacion = new javax.swing.JLabel();
			lOrientacion.setSize(102, 38);
			lOrientacion.setText(PluginServices.getText(this, "orientacion"));
			lOrientacion.setLocation(25, 100);
		}

		return lOrientacion;
	}

	/**
	 * This method initializes lMargenes
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLMargenes() {
		if (lMargenes == null) {
			lMargenes = new javax.swing.JLabel();
			lMargenes.setSize(95, 20);
			lMargenes.setText(PluginServices.getText(this, "margenes"));
			lMargenes.setLocation(25, 145);
		}

		return lMargenes;
	}

	/**
	 * Inserta si se dibuja los margenes sobre el Layout o no.
	 *
	 * @param b True si se tiene que dibujar los margenes.
	 */
	private void setMargin(boolean b) {
		margin = b;

		if (b) {
			getTSuperior().setText(String.valueOf(nf.format(
						m_layout.getLayoutContext().getAttributes().toUnits(sup))));
			getTIzquierdo().setText(String.valueOf(nf.format(
						m_layout.getLayoutContext().getAttributes().toUnits(izq))));
			getTInferior().setText(String.valueOf(nf.format(
						m_layout.getLayoutContext().getAttributes().toUnits(inf))));
			getTDerecho().setText(String.valueOf(nf.format(
						m_layout.getLayoutContext().getAttributes().toUnits(der))));
			getTSuperior().setEnabled(true);
			getTIzquierdo().setEnabled(true);
			getTInferior().setEnabled(true);
			getTDerecho().setEnabled(true);
		} else {
			getTSuperior().setText("");
			getTIzquierdo().setText("");
			getTInferior().setText("");
			getTDerecho().setText("");
			getTSuperior().setEnabled(false);
			getTIzquierdo().setEnabled(false);
			getTInferior().setEnabled(false);
			getTDerecho().setEnabled(false);
		}
	}

	/**
	 * This method initializes chbMargenes
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbMargenes() {
		if (chbMargenes == null) {
			chbMargenes = new javax.swing.JCheckBox();
			chbMargenes.setSize(230, 21);
			chbMargenes.setText(PluginServices.getText(this,"personalizar_margenes"));
			chbMargenes.setLocation(125, 145);
			chbMargenes.setSelected(m_layout.getLayoutContext().getAttributes().isMargin());
			setMargin(m_layout.getLayoutContext().getAttributes().isMargin());
			chbMargenes.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (chbMargenes.isSelected()) {
							margin = true;
						} else {
							margin = false;
						}

						setMargin(margin);
					}
				});
		}

		return chbMargenes;
	}

	/**
	 * This method initializes lSuperior
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLSuperior() {
		if (lSuperior == null) {
			lSuperior = new javax.swing.JLabel();
			lSuperior.setSize(81, 20);
			lSuperior.setText(PluginServices.getText(this, "Superior"));
			lSuperior.setLocation(25, 167);
		}

		return lSuperior;
	}

	/**
	 * This method initializes tSuperior
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTSuperior() {
		if (tSuperior == null) {
			tSuperior = new javax.swing.JTextField();
			tSuperior.setSize(60, 20);
			tSuperior.setLocation(110, 167);

			if (m_layout.getLayoutContext().getAttributes().isMargin()) {
				tSuperior.setText(String.valueOf(nf.format(
							m_layout.getLayoutContext().getAttributes().toUnits(sup))));
			}
		}

		return tSuperior;
	}

	/**
	 * This method initializes lIzquierdo
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLIzquierdo() {
		if (lIzquierdo == null) {
			lIzquierdo = new javax.swing.JLabel();
			lIzquierdo.setSize(88, 20);
			lIzquierdo.setText(PluginServices.getText(this, "Izquierdo"));
			lIzquierdo.setLocation(180, 167);
		}

		return lIzquierdo;
	}

	/**
	 * This method initializes tIzquierdo
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTIzquierdo() {
		if (tIzquierdo == null) {
			tIzquierdo = new javax.swing.JTextField();
			tIzquierdo.setSize(60, 20);
			tIzquierdo.setLocation(280, 167);

			if (m_layout.getLayoutContext().getAttributes().isMargin()) {
				tIzquierdo.setText(String.valueOf(nf.format(
							m_layout.getLayoutContext().getAttributes().toUnits(izq))));
			}
		}

		return tIzquierdo;
	}

	/**
	 * This method initializes lInferior
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLInferior() {
		if (lInferior == null) {
			lInferior = new javax.swing.JLabel();
			lInferior.setSize(81, 21);
			lInferior.setText(PluginServices.getText(this, "Inferior"));
			lInferior.setLocation(25, 190);
		}

		return lInferior;
	}

	/**
	 * This method initializes tInferior
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTInferior() {
		if (tInferior == null) {
			tInferior = new javax.swing.JTextField();
			tInferior.setSize(60, 20);
			tInferior.setLocation(110, 190);

			if (m_layout.getLayoutContext().getAttributes().isMargin()) {
				tInferior.setText(String.valueOf(nf.format(
							m_layout.getLayoutContext().getAttributes().toUnits(inf))));
			}
		}

		return tInferior;
	}

	/**
	 * This method initializes lDerecho
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDerecho() {
		if (lDerecho == null) {
			lDerecho = new javax.swing.JLabel();
			lDerecho.setSize(87, 21);
			lDerecho.setText(PluginServices.getText(this, "Derecho"));
			lDerecho.setLocation(180, 190);
		}

		return lDerecho;
	}

	/**
	 * This method initializes tDerecho
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTDerecho() {
		if (tDerecho == null) {
			tDerecho = new javax.swing.JTextField();
			tDerecho.setSize(60, 20);
			tDerecho.setLocation(280, 190);

			if (m_layout.getLayoutContext().getAttributes().isMargin()) {
				tDerecho.setText(String.valueOf(nf.format(
							m_layout.getLayoutContext().getAttributes().toUnits(der))));
			}
		}

		return tDerecho;
	}

	/**
	 * This method initializes lResolucion
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLResolucion() {
		if (lResolucion == null) {
			lResolucion = new javax.swing.JLabel();
			lResolucion.setSize(164, 21);
			lResolucion.setText(PluginServices.getText(this,
					"resolucion_resultado"));
			lResolucion.setLocation(25, 215);
		}

		return lResolucion;
	}

	/**
	 * This method initializes cbResolucion
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbResolucion() {
		if (cbResolucion == null) {
			cbResolucion = new javax.swing.JComboBox();
			cbResolucion.setSize(150, 20);
			cbResolucion.setLocation(195, 215);
			cbResolucion.addItem(PluginServices.getText(this, "alta"));
			cbResolucion.addItem(PluginServices.getText(this, "normal"));
			cbResolucion.addItem(PluginServices.getText(this, "baja"));
			cbResolucion.setSelectedIndex(m_layout.getLayoutContext().getAttributes().getResolution());
			cbResolucion.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						resolution = cbResolucion.getSelectedIndex();
					}
				});
		}

		return cbResolucion;
	}

	/**
	 * This method initializes bAceptar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setSize(84, 23);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.setLocation(130, 245);
			bAceptar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						m_layout.getLayoutContext().getAttributes().setUnit(unit);

						if (isLand) {
							m_layout.getLayoutContext().getAttributes().setIsLandScape(true);
						} else {
							m_layout.getLayoutContext().getAttributes().setIsLandScape(false);
						}

						m_layout.getLayoutContext().getAttributes().setType(type);
						m_layout.getLayoutContext().getAttributes().setSizeinUnits(isLand);
						obtainArea();

						double[] area = { sup, inf, izq, der };

						if (type == Attributes.CUSTOM) {
							String width=getTAncho().getText().replace(',','.');
							String height=getTAlto().getText().replace(',','.');
							Attributes.CUSTOM_PAPER_SIZE = new Size(Double.valueOf(
										width).doubleValue(),
									Double.valueOf(height)
										  .doubleValue());
							m_layout.getLayoutContext().getAttributes().m_sizePaper=Attributes.CUSTOM_PAPER_SIZE;
						}
						m_layout.getLayoutContext().getAttributes().setType(type);
						m_layout.obtainRect(false);
						m_layout.getLayoutContext().getAttributes().setSelectedOptions(type, unit,
							isLand, margin, resolution, area);
						PluginServices.getMDIManager().closeWindow(FConfigLayoutDialog.this);
						m_layout.getLayoutControl().fullRect();
						//m_layout.refresh();
					}
				});
		}

		return bAceptar;
	}

	/**
	 * This method initializes bCancelar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBCancelar() {
		if (bCancelar == null) {
			bCancelar = new javax.swing.JButton();
			bCancelar.setSize(85, 23);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.setLocation(245, 245);
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						//setVisible(false);
						PluginServices.getMDIManager().closeWindow(FConfigLayoutDialog.this);
					}
				});
		}

		return bCancelar;
	}

	/**
	 * This method initializes chbHorizontal
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbHorizontal() {
		if (chbHorizontal == null) {
			chbHorizontal = new javax.swing.JCheckBox();
			chbHorizontal.setSize(99, 21);
			chbHorizontal.setText(PluginServices.getText(this, "horizontal"));
			chbHorizontal.setLocation(130, 110);
			chbHorizontal.setSelected(isLand);
			if (getCbTipoFolio().getSelectedItem().equals(PluginServices.getText(this,"Personalizado"))) {
				chbHorizontal.setEnabled(false);
				chbHorizontal.setSelected(false);
				isLand=false;
			}
			chbHorizontal.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						isLand = true;
						getChbVertical().setSelected(false);

						if (chbHorizontal.isSelected()) {
							double iz = izq;
							izq = inf;
							inf = der;
							der = sup;
							sup = iz;

							setMargin(margin);

							//}else{
							if (type == Attributes.CUSTOM) {
								Attributes.CUSTOM_PAPER_SIZE = new Size(Double.valueOf(
											getTAlto().getText()).doubleValue(),
										Double.valueOf(getTAncho().getText())
											  .doubleValue());
							}

							Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand,
									type);
							getTAncho().setText(String.valueOf(nf.format(
										size.getAncho())));
							getTAlto().setText(String.valueOf(nf.format(
										size.getAlto())));
						}

						chbHorizontal.setSelected(true);
					}
				});
		}

		return chbHorizontal;
	}

	/**
	 * This method initializes chbVertical
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbVertical() {
		if (chbVertical == null) {
			chbVertical = new javax.swing.JCheckBox();
			chbVertical.setSize(94, 23);
			chbVertical.setText(PluginServices.getText(this, "vertical"));
			chbVertical.setLocation(245, 110);
			chbVertical.setSelected(!isLand);
			if (getCbTipoFolio().getSelectedItem().equals(PluginServices.getText(this,"Personalizado"))) {
				chbVertical.setEnabled(false);
				chbVertical.setSelected(true);
				isLand=false;
			}
			chbVertical.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						isLand = false;
						getChbHorizontal().setSelected(false);

						if (chbVertical.isSelected()) {
							double in = inf;
							inf = izq;
							izq = sup;
							sup = der;
							der = in;

							setMargin(margin);

							//}else{
							if (type == Attributes.CUSTOM) {
								Attributes.CUSTOM_PAPER_SIZE = new Size(Double.valueOf(
											getTAncho().getText()).doubleValue(),
										Double.valueOf(getTAlto().getText())
											  .doubleValue());
							}

							Size size = m_layout.getLayoutContext().getAttributes().getSizeinUnits(isLand,
									type);
							getTAncho().setText(String.valueOf(nf.format(
										size.getAncho())));
							getTAlto().setText(String.valueOf(nf.format(
										size.getAlto())));
						}

						chbVertical.setSelected(true);
					}
				});
		}

		return chbVertical;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setWidth(371);
		m_viewinfo.setHeight(300);
		m_viewinfo.setTitle(PluginServices.getText(this, "Preparar_pagina"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * Obtiene el área de los JtextField.
	 */
	private void obtainArea() {
		String s = (getTSuperior().getText());
		s = s.replace(',', '.');

		if (s.length() == 0) {
			s = "0";
		}

		sup = m_layout.getLayoutContext().getAttributes().fromUnits(Double.valueOf(s).doubleValue());
		s = (getTIzquierdo().getText());
		s = s.replace(',', '.');

		if (s.length() == 0) {
			s = "0";
		}

		izq = m_layout.getLayoutContext().getAttributes().fromUnits(Double.valueOf(s).doubleValue());
		s = (getTInferior().getText());
		s = s.replace(',', '.');

		if (s.length() == 0) {
			s = "0";
		}

		inf = m_layout.getLayoutContext().getAttributes().fromUnits(Double.valueOf(s).doubleValue());
		s = (getTDerecho().getText());
		s = s.replace(',', '.');

		if (s.length() == 0) {
			s = "0";
		}

		der = m_layout.getLayoutContext().getAttributes().fromUnits(Double.valueOf(s).doubleValue());
	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
