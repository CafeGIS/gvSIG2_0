/*
 * Created on 02-jun-2004
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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.gvsig.fmap.mapcontext.MapContext;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.utils.FontChooser;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameScaleBar;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.ListViewModel;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Dialogo para a�adir una barra de escala al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameScaleBarDialog extends JPanel implements IFFrameDialog {
	private static final ImageIcon inumero = PluginServices.getIconTheme()
		.get("numero-icon");

	private static final ImageIcon ibarra1 = PluginServices.getIconTheme()
		.get("barra1-icon");

	private static final ImageIcon ibarra2 = PluginServices.getIconTheme()
		.get("barra2-icon");

	private static final ImageIcon ibarra3 = PluginServices.getIconTheme()
		.get("barra3-icon");

	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JList liVistas = null;
	private javax.swing.JCheckBox chbMantenerIntervalo = null;
	private javax.swing.JComboBox cbEscala = null;
	private javax.swing.JComboBox cbUnidades = null;
	private javax.swing.JLabel lIntervalo = null;
	private javax.swing.JTextField tIntervalo = null;
	private javax.swing.JLabel lNumIntervalos = null;
	private javax.swing.JTextField tNumIntervalos = null;
	private javax.swing.JLabel lDivIzquierda = null;
	private javax.swing.JTextField tDivIzquierda = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;

	//private ProjectView m_projectView=null;
	private FFrameView fframeview = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private Layout m_layout = null;
	private FFrameScaleBar fframescalebar = null; //new FFrameScaleBar();
	private boolean isAcepted = false;
	private ImageIcon[] images = new ImageIcon[4];
	private javax.swing.JButton bFuente = null;
	private JPanel pMarcoVista = null;
	private JPanel pDescripcion = null;
	private JTextField tfDescripcion = null;
	private JPanel pUnidades = null;
	private JCheckBox chbSobreDescripcion = null;
	private JCheckBox chbMostrarUnidades = null;
	private JCheckBox chbSobreUnidades = null;
	private JPanel pEtiquetas = null;
	private JCheckBox chbEtiquetas = null;
	private JPanel pBarra = null;
	private JPanel jPanel5 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private ColorChooserPanel bUnidadesColor = null;
	private ColorChooserPanel bBarraColor = null;
	private Color barcolor = null;
	private Color textcolor = null;

	//private JPanel jPanel = null;
	//private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JCheckBox jCheckBox = null;
	private JPRotation pRotation = null;
	private JLabel lblBarColor = null;
	private JTextField txtNumDec = null;
	private JLabel lblNumDec = null;

	/**
	 * This is the default constructor
	 *
	 * @param layout DOCUMENT ME!
	 * @param fframe DOCUMENT ME!
	 */
	public FFrameScaleBarDialog(Layout layout, FFrameScaleBar fframe) {
		super();
		fframescalebar = (FFrameScaleBar)fframe.cloneFFrame(layout);
		barcolor = fframescalebar.getBarColor();
		textcolor = fframescalebar.getTextColor();

		if (fframescalebar.getFFrameDependence() != null && fframescalebar.getDescription().equals("")) {
			getTfNumberScale().setText("1:" +
				String.valueOf(((FFrameView)fframescalebar.getFFrameDependence()[0]).getScale()));
		}

		m_layout = layout;
		initialize();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param r DOCUMENT ME!
	 */
	public void setRectangle(Rectangle2D r) {
		rect.setRect(r);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);
		this.add(getJContentPane(), null);
		this.setSize(621, 400);

		if (fframescalebar.getStyle() == 0) {
			getChbMantenerIntervalo().setEnabled(false);

			///getLUnidades().setEnabled(false);
			getCbUnidades().setEnabled(false);
			getChbEtiquetas().setEnabled(false);
			getBBarraColor().setEnabled(false);
			getChbMostrarUnidades().setEnabled(false);
			getChbSobreUnidades().setEnabled(false);
			getLIntervalo().setEnabled(false);
			getTIntervalo().setEnabled(false);
			getLNumIntervalos().setEnabled(false);
			getTNumIntervalos().setEnabled(false);
			getLDivIzquierda().setEnabled(false);
			getTDivIzquierda().setEnabled(false);
			getJCheckBox().setEnabled(false);
			getChbSobreDescripcion().setEnabled(false);
		}
		getPRotation().setRotation(fframescalebar.getRotation());
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBAceptar(), null);
			jContentPane.add(getBCancelar(), null);
			jContentPane.add(getPRotation(), null);
			jContentPane.setSize(617, 386);
			jContentPane.setPreferredSize(new java.awt.Dimension(50, 50));
			jContentPane.setLocation(0, 6);
			jContentPane.add(getPMarcoVista(), null);
			jContentPane.add(getPDescripcion(), null);
			jContentPane.add(getPUnidades(), null);
			jContentPane.add(getPBarra(), null);
			jContentPane.add(getPEtiquetas(), null);
		}

		return jContentPane;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(250,55));
			jScrollPane.setViewportView(getLiVistas());
		}

		return jScrollPane;
	}

	/**
	 * This method initializes liVistas
	 *
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getLiVistas() {
		if (liVistas == null) {
			ListViewModel listmodel = new ListViewModel();
			listmodel.addViews(m_layout);

			///ArrayList list = listmodel.getViews();

			liVistas = new javax.swing.JList();

			liVistas.setSize(new java.awt.Dimension(250,52));
			liVistas.setModel(listmodel);

			for (int i = 0; i < liVistas.getModel().getSize(); i++) {
				if (fframescalebar.getFFrameDependence() != null) {
					FFrameView fframeviewAux = (FFrameView) liVistas.getModel().getElementAt(i);

					if (fframeviewAux == fframescalebar.getFFrameDependence()[0]) {
						liVistas.setSelectedIndex(i);
						fframeview=fframeviewAux;
					}
				}
			}

			liVistas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					private int selectIndex=-1;
					public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
						IFFrame[] fframes=m_layout.getLayoutContext().getFFrames();
							int selectInt = ((JList) e.getSource())
									.getSelectedIndex();
							if (selectInt != selectIndex) {
								selectIndex = selectInt;
								if (selectIndex == -1) {
									return;
								}
								fframeview = (FFrameView) liVistas.getModel()
										.getElementAt(selectInt);

								for (int i = 0; i < fframes.length; i++) {
									IFFrame f = fframes[i];

									if (f instanceof FFrameView) {
										if (((FFrameView) f).getView() == fframeview
												.getView()) {
											fframescalebar
													.setFFrameDependence(fframeview);
										}
									}
								}

								getTNumIntervalos().setText(
										String.valueOf(fframescalebar
												.getNumInterval()));
								getTDivIzquierda().setText(
										String.valueOf(fframescalebar
												.getNumLeft()));
								getTIntervalo().setText(
										fframescalebar.obtainInterval());
								getTfNumberScale().setText(
										fframescalebar.getDescription());
							}
						}
					});
		}

		return liVistas;
	}

	/**
	 * This method initializes chbMantenerIntervalo
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbMantenerIntervalo() {
		if (chbMantenerIntervalo == null) {
			chbMantenerIntervalo = new javax.swing.JCheckBox();
			chbMantenerIntervalo.setSelected(fframescalebar.isbIntervalSet());
			chbMantenerIntervalo.setText(PluginServices.getText(this,
					"mantener_intervalo"));
			chbMantenerIntervalo.setPreferredSize(new java.awt.Dimension(200, 20));
			chbMantenerIntervalo.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fframescalebar.setIntervalSet(getChbMantenerIntervalo()
														  .isSelected());
					}
				});
		}

		return chbMantenerIntervalo;
	}

	/**
	 * This method initializes cbEscala
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbEscala() {
		if (cbEscala == null) {
			//String[] s={"n�merico","barra1","barra2","barra3","barra4"};
			cbEscala = new javax.swing.JComboBox();
			images[0] = inumero;
			images[1] = ibarra1;
			images[2] = ibarra2;
			images[3] = ibarra3;

			/*  Image img=img = new BufferedImage(100, 25,
			   BufferedImage.TYPE_INT_ARGB);
			   img.getGraphics().drawImage(images[3].getImage(),0,0,null);
			   img.getGraphics().setXORMode(Color.yellow);
			    images[3]=new ImageIcon(img);
			 */
			ComboBoxRenderer renderer = new ComboBoxRenderer();
			renderer.setPreferredSize(new Dimension(100, 25));
			cbEscala.setRenderer(renderer);
			cbEscala.setMaximumRowCount(4);

			cbEscala.addItem("0");
			cbEscala.addItem("1");
			cbEscala.addItem("2");
			cbEscala.addItem("3");
			cbEscala.setSelectedIndex(fframescalebar.getStyle());
			cbEscala.setPreferredSize(new java.awt.Dimension(200, 20));
			cbEscala.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fframescalebar.setStyle(getCbEscala().getSelectedIndex());

						if (cbEscala.getSelectedIndex() == 0) {
							getChbMantenerIntervalo().setEnabled(false);
							getChbMostrarUnidades().setEnabled(false);
							getChbSobreUnidades().setEnabled(false);
							getCbUnidades().setEnabled(false);
							getChbEtiquetas().setEnabled(false);
							getBBarraColor().setEnabled(false);
							getLIntervalo().setEnabled(false);
							getTIntervalo().setEnabled(false);
							getLNumIntervalos().setEnabled(false);
							getTNumIntervalos().setEnabled(false);
							getLDivIzquierda().setEnabled(false);
							getTDivIzquierda().setEnabled(false);
							getJCheckBox().setEnabled(false);
							getChbSobreDescripcion().setEnabled(false);
						} else {
							getChbMantenerIntervalo().setEnabled(true);
							getChbMostrarUnidades().setEnabled(true);
							getChbSobreUnidades().setEnabled(true);
							getCbUnidades().setEnabled(true);
							getChbEtiquetas().setEnabled(true);
							getBBarraColor().setEnabled(true);
							getLIntervalo().setEnabled(true);
							getTIntervalo().setEnabled(true);
							getLNumIntervalos().setEnabled(true);
							getTNumIntervalos().setEnabled(true);
							getLDivIzquierda().setEnabled(true);
							getTDivIzquierda().setEnabled(true);
							getJCheckBox().setEnabled(true);
							getChbSobreDescripcion().setEnabled(true);
						}
					}
				});
		}

		return cbEscala;
	}

	/**
	 * This method initializes cbUnidades
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbUnidades() {
		if (cbUnidades == null) {
			//String[] s={"Kil�metros","metros","cent�metros","mil�metros","millas","yardas","pies","pulgadas"};
			String[] names = MapContext.getDistanceNames();
			for (int i = 0; i < names.length; i++) {
				names[i]=PluginServices.getText(this,names[i]);
			}
			cbUnidades = new javax.swing.JComboBox(names);
			cbUnidades.setSelectedIndex(fframescalebar.getUnits());
			cbUnidades.setPreferredSize(new java.awt.Dimension(150, 20));
			cbUnidades.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fframescalebar.setUnits(getCbUnidades()
													.getSelectedIndex());
						getTIntervalo().setText(fframescalebar.obtainInterval());
					}
				});
		}

		return cbUnidades;
	}

	/**
	 * This method initializes lIntervalo
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLIntervalo() {
		if (lIntervalo == null) {
			lIntervalo = new javax.swing.JLabel();
			lIntervalo.setText(PluginServices.getText(this, "Intervalo"));
			lIntervalo.setPreferredSize(new java.awt.Dimension(155,20));
		}

		return lIntervalo;
	}

	/**
	 * This method initializes tIntervalo
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTIntervalo() {
		if (tIntervalo == null) {
			tIntervalo = new javax.swing.JTextField();
			tIntervalo.setPreferredSize(new java.awt.Dimension(80,20));
			tIntervalo.setText(fframescalebar.getInterval());
			tIntervalo.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyReleased(java.awt.event.KeyEvent e) {
						if (!tIntervalo.getText().toString().equals("")) {
							String s=tIntervalo.getText().toString();
							try {
								fframescalebar.setInterval(FFrameScaleBar.numberFormat.parse(s).doubleValue());
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
							getTNumIntervalos().setText(String.valueOf(
									fframescalebar.getNumInterval()));
						}
					}
				});
		}

		return tIntervalo;
	}

	/**
	 * This method initializes lNumIntervalos
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLNumIntervalos() {
		if (lNumIntervalos == null) {
			lNumIntervalos = new javax.swing.JLabel();
			lNumIntervalos.setText(PluginServices.getText(this, "Num_intervalos"));
			lNumIntervalos.setPreferredSize(new java.awt.Dimension(155,20));
		}

		return lNumIntervalos;
	}

	/**
	 * This method initializes tNumIntervalos
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTNumIntervalos() {
		if (tNumIntervalos == null) {
			tNumIntervalos = new javax.swing.JTextField();
			tNumIntervalos.setPreferredSize(new java.awt.Dimension(80,20));
			tNumIntervalos.setText(Integer.toString(
					fframescalebar.getNumInterval()));
			tNumIntervalos.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyReleased(java.awt.event.KeyEvent e) {
						if (!tNumIntervalos.getText().toString().equals("")) {
							fframescalebar.setNumInterval(Integer.parseInt(
									tNumIntervalos.getText().toString()));
							getTIntervalo().setText(fframescalebar.obtainInterval());
						}
					}
				});
		}

		return tNumIntervalos;
	}

	/**
	 * This method initializes lDivIzquierda
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDivIzquierda() {
		if (lDivIzquierda == null) {
			lDivIzquierda = new javax.swing.JLabel();
			lDivIzquierda.setText(PluginServices.getText(this,
					"divisiones_izquierda"));
			lDivIzquierda.setPreferredSize(new java.awt.Dimension(155,20));
		}

		return lDivIzquierda;
	}

	/**
	 * This method initializes tDivIzquierda
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTDivIzquierda() {
		if (tDivIzquierda == null) {
			tDivIzquierda = new javax.swing.JTextField();
			tDivIzquierda.setPreferredSize(new java.awt.Dimension(80,20));
			tDivIzquierda.setText(Integer.toString(fframescalebar.getNumLeft()));
			tDivIzquierda.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyReleased(java.awt.event.KeyEvent e) {
						if (tDivIzquierda.getText().toString().equals("")) {
							tDivIzquierda.setText("0");
						} else {
							fframescalebar.setNumLeft(Integer.parseInt(
									tDivIzquierda.getText()));
						}
					}
				});
		}

		return tDivIzquierda;
	}

	/**
	 * This method initializes bAceptar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setSize(85, 26);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.setLocation(106, 347);
			bAceptar.setPreferredSize(new java.awt.Dimension(79, 23));
			bAceptar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fframescalebar.setUnits(getCbUnidades()
													.getSelectedIndex());
						fframescalebar.setBoundBox(FLayoutUtilities.toSheetRect(
								rect, m_layout.getLayoutControl().getAT()));
						fframescalebar.setFFrameDependence(fframeview);
						fframescalebar.setBarColor(barcolor);
						fframescalebar.setTextColor(textcolor);
						fframescalebar.setShowNameUnits(getChbMostrarUnidades()
															.isSelected());
						fframescalebar.setShowDescription(getJCheckBox()
															  .isSelected());
						fframescalebar.setAboveDescription(getChbSobreDescripcion()
															   .isSelected());
						fframescalebar.setAboveIntervals(getChbEtiquetas()
															 .isSelected());
						fframescalebar.setAboveName(getChbSobreUnidades()
														.isSelected());
						fframescalebar.setRotation(getPRotation().getRotation());
						fframescalebar.setNumDec(Integer.parseInt(getNumDec().getText()));
						fframescalebar.setNumLeft(Integer.parseInt(getTDivIzquierda().getText()));
						PluginServices.getMDIManager().closeWindow(FFrameScaleBarDialog.this);
						//m_layout.refresh();
						isAcepted = true;
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
			bCancelar.setSize(85, 26);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.setLocation(297, 347);
			bCancelar.setPreferredSize(new java.awt.Dimension(85, 23));
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fframescalebar=null;
						PluginServices.getMDIManager().closeWindow(FFrameScaleBarDialog.this);
					}
				});
		}

		return bCancelar;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"Propiedades_escala_grafica"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog#getIsAcepted()
	 */
	public boolean getIsAcepted() {
		return isAcepted;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * This method initializes bFuente
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBFuente() {
		if (bFuente == null) {
			bFuente = new javax.swing.JButton();
			bFuente.setText(PluginServices.getText(this, "fuente"));
			bFuente.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						Font font=FontChooser.showDialog(
								PluginServices.getText(this, "seleccion_fuente"),
								fframescalebar.getFont());
						if (font != null){
							fframescalebar.setFont(font); // fchoser=new FontChooser();
						}
					}
				});
		}

		return bFuente;
	}

	/**
	 * This method initializes pMarcoVista
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPMarcoVista() {
		if (pMarcoVista == null) {
			pMarcoVista = new JPanel();
			pMarcoVista.setBounds(7, 9, 263, 86);
			pMarcoVista.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this,PluginServices.getText(this,"marco_vista")),javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.black));
			pMarcoVista.add(getJScrollPane(), null);
		}

		return pMarcoVista;
	}

	/**
	 * This method initializes pDescripcion
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPDescripcion() {
		if (pDescripcion == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			pDescripcion = new JPanel();
			pDescripcion.setLayout(flowLayout1);
			pDescripcion.setBounds(275, 9, 204, 110);
			pDescripcion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this,"Escala"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.black));
			pDescripcion.add(getTfNumberScale(), null);
			pDescripcion.add(getJCheckBox(), null);
			pDescripcion.add(getChbSobreDescripcion(), null);
		}

		return pDescripcion;
	}

	/**
	 * This method initializes tfDescripcion
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTfNumberScale() {
		if (tfDescripcion == null) {
			tfDescripcion = new JTextField();
			tfDescripcion.setPreferredSize(new java.awt.Dimension(180,20));

			tfDescripcion.setEditable(false);
			/*if (fframeview!=null){
			   getTfDescripcion().setText("escala 1:"+String.valueOf(fframeview.getScale()));
			   }
			 */
			tfDescripcion.setText(fframescalebar.getDescription());
		}

		return tfDescripcion;
	}

	/**
	 * This method initializes pUnidades
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPUnidades() {
		if (pUnidades == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			pUnidades = new JPanel();
			pUnidades.setLayout(flowLayout);
			pUnidades.setLocation(274, 122);
			pUnidades.setSize(204, 110);
			pUnidades.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "unidades"),javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.black));
			pUnidades.add(getCbUnidades(), null);
			pUnidades.add(getChbMostrarUnidades(), null);
			pUnidades.add(getChbSobreUnidades(), null);
		}

		return pUnidades;
	}

	/**
	 * This method initializes chbSobreDescripcion
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbSobreDescripcion() {
		if (chbSobreDescripcion == null) {
			chbSobreDescripcion = new JCheckBox();
			chbSobreDescripcion.setSelected(fframescalebar.isAboveDescription());
			chbSobreDescripcion.setPreferredSize(new java.awt.Dimension(180,24));
			chbSobreDescripcion.setText(PluginServices.getText(this,"sobre_la_barra"));
		}

		return chbSobreDescripcion;
	}

	/**
	 * This method initializes chbMostrarUnidades
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbMostrarUnidades() {
		if (chbMostrarUnidades == null) {
			chbMostrarUnidades = new JCheckBox();
			chbMostrarUnidades.setText(PluginServices.getText(this,"mostrar_unidades"));
			chbMostrarUnidades.setSelected(fframescalebar.isShowNameUnits());
			chbMostrarUnidades.setPreferredSize(new java.awt.Dimension(150, 24));
		}

		return chbMostrarUnidades;
	}

	/**
	 * This method initializes chbSobreUnidades
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbSobreUnidades() {
		if (chbSobreUnidades == null) {
			chbSobreUnidades = new JCheckBox();
			chbSobreUnidades.setSelected(fframescalebar.isAboveName());
			chbSobreUnidades.setText(PluginServices.getText(this,"sobre_la_barra"));
			chbSobreUnidades.setPreferredSize(new java.awt.Dimension(150, 24));
		}

		return chbSobreUnidades;
	}

	/**
	 * This method initializes pEtiquetas
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPEtiquetas() {
		if (pEtiquetas == null) {
			FlowLayout flowLayout2 = new FlowLayout();
			flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
			pEtiquetas = new JPanel();
			pEtiquetas.setLayout(flowLayout2);
			pEtiquetas.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "etiquetas"),javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.black));
			pEtiquetas.setBounds(276, 235, 204, 104);
			pEtiquetas.add(getChbEtiquetas(), null);
			pEtiquetas.add(getJPanel2(), null);
		}

		return pEtiquetas;
	}

	/**
	 * This method initializes chbEtiquetas
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbEtiquetas() {
		if (chbEtiquetas == null) {
			chbEtiquetas = new JCheckBox();
			chbEtiquetas.setSelected(fframescalebar.isAboveIntervals());
			chbEtiquetas.setText(PluginServices.getText(this,"sobre_la_barra"));
		}

		return chbEtiquetas;
	}

	/**
	 * This method initializes pBarra
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPBarra() {
		if (pBarra == null) {
			FlowLayout flowLayout3 = new FlowLayout();
			flowLayout3.setAlignment(java.awt.FlowLayout.LEFT);
			lblNumDec = new JLabel();
			lblNumDec.setText(PluginServices.getText(this,"numero_decimales_mostrar"));
			lblBarColor = new JLabel();
			lblBarColor.setText(PluginServices.getText(this,"color"));
			pBarra = new JPanel();
			pBarra.setLayout(flowLayout3);
			pBarra.setBounds(8, 98, 263, 241);
			pBarra.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this,"barra"),javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12), java.awt.Color.black));
			pBarra.add(getChbMantenerIntervalo(), null);
			pBarra.add(getCbEscala(), null);
			pBarra.add(lblNumDec, null);
			pBarra.add(getNumDec(), null);
			pBarra.add(getJPanel5(), null);
			pBarra.add(getJPanel7(), null);
			pBarra.add(getJPanel6(), null);
			pBarra.add(lblBarColor, null);
			pBarra.add(getBBarraColor(), null);
		}

		return pBarra;
	}

	/**
	 * This method initializes jPanel5
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setPreferredSize(new java.awt.Dimension(245,30));
			jPanel5.add(getLIntervalo(), null);
			jPanel5.add(getTIntervalo(), null);
		}

		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setPreferredSize(new java.awt.Dimension(245,30));
			jPanel6.add(getLDivIzquierda(), null);
			jPanel6.add(getTDivIzquierda(), null);
		}

		return jPanel6;
	}

	/**
	 * This method initializes jPanel7
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jPanel7 = new JPanel();
			jPanel7.setPreferredSize(new java.awt.Dimension(245,30));
			jPanel7.add(getLNumIntervalos(), null);
			jPanel7.add(getTNumIntervalos(), null);
		}

		return jPanel7;
	}

	/**
	 * This method initializes bUnidadesColor
	 *
	 * @return javax.swing.JButton
	 */
	private ColorChooserPanel getBUnidadesColor() {
		if (bUnidadesColor == null) {
			bUnidadesColor = new ColorChooserPanel();
			bUnidadesColor.setAlpha(255);
			bUnidadesColor.setColor(textcolor);
			bUnidadesColor.setPreferredSize(new java.awt.Dimension(100,25));
			bUnidadesColor.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						textcolor = getBUnidadesColor().getColor();
					}
				});
		}

		return bUnidadesColor;
	}

	/**
	 * This method initializes bBarraColor
	 *
	 * @return javax.swing.JButton
	 */
	private ColorChooserPanel getBBarraColor() {
		if (bBarraColor == null) {
			bBarraColor = new ColorChooserPanel();
			bBarraColor.setPreferredSize(new java.awt.Dimension(100,25));
			bBarraColor.setAlpha(255);
			bBarraColor.setColor(barcolor);
			bBarraColor.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
//						JDialog dlg;
//						JColorChooser colorChooser;
//						colorChooser = new JColorChooser();
//						dlg = JColorChooser.createDialog((JFrame) null,
//								PluginServices.getText(this, "Elegir_Color"),
//								true, colorChooser, null, null);
//						dlg.show(true);
//
						barcolor = getBBarraColor().getColor();

//						bBarraColor.setBackground(barcolor);
//						bBarraColor.setForeground(barcolor);
					}
				});
		}

		return bBarraColor;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */

	/*private JPanel getJPanel() {
	   if (jPanel == null) {
	           jPanel = new JPanel();
	           jPanel.setPreferredSize(new java.awt.Dimension(44,24));
	           jPanel.setForeground(barcolor);
	           jPanel.setBackground(barcolor);
	   }
	   return jPanel;
	   }
	 */

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */

	/*        private JPanel getJPanel1() {
	   if (jPanel1 == null) {
	           jPanel1 = new JPanel();
	           jPanel1.setBackground(textcolor);
	           jPanel1.setPreferredSize(new java.awt.Dimension(44,24));
	   }
	   return jPanel1;
	   }
	 */

	/**
	 * This method initializes jPanel2
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.add(getBFuente(), null);
			jPanel2.add(getBUnidadesColor(), null);

			//jPanel2.add(getJPanel1(), null);
		}

		return jPanel2;
	}

	/**
	 * This method initializes jCheckBox
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBox() {
		if (jCheckBox == null) {
			jCheckBox = new JCheckBox();
			jCheckBox.setSelected(fframescalebar.isShowDescription());
			jCheckBox.setPreferredSize(new java.awt.Dimension(180,24));
			jCheckBox.setText(PluginServices.getText(this,"mostrar_escala_numerica"));
		}

		return jCheckBox;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author Vicente Caballero Navarro
	 */
	class ComboBoxRenderer extends JLabel implements ListCellRenderer {
		/**
		 * Crea un nuevo ComboBoxRenderer.
		 */
		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		/*
		 * This method finds the image and text corresponding
		 * to the selected value and returns the label, set up
		 * to display the text and image.
		 */
		public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)
			///int selectedIndex = ((Integer)value).intValue();
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			ImageIcon icon = images[Integer.parseInt((String) value)];
			setIcon(icon);

			return this;
		}
	}

	/**
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(480, 10, 120, 120);
		}
		return pRotation;
	}

	/**
	 * This method initializes numDec
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getNumDec() {
		if (txtNumDec == null) {
			txtNumDec = new JTextField();
			txtNumDec.setPreferredSize(new java.awt.Dimension(30,20));
			txtNumDec.setText(String.valueOf(fframescalebar.getNumDec()));
			txtNumDec.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(KeyEvent arg0) {
					super.keyReleased(arg0);
					if (!getNumDec().getText().toString().equals("")) {
						fframescalebar.setNumDec(Integer.parseInt(
								getNumDec().getText().toString()));
						getTIntervalo().setText(fframescalebar.obtainInterval());
					}
				}
			});
		}
		return txtNumDec;
	}

	public IFFrame getFFrame() {
		return fframescalebar;
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

} //  @jve:decl-index=0:visual-constraint="17,10"
//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
