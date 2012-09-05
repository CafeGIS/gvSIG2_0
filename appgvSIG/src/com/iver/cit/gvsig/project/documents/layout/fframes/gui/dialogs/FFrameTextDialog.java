/*
 * Created on 22-jun-2004
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.utils.FontChooser;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameText;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

/**
 * Dialogo para a�adir texto al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameTextDialog extends JPanel implements IFFrameDialog {
	public static final ImageIcon ileft = PluginServices.getIconTheme()
		.get("text-left-icon");

	public static final ImageIcon icenterV = PluginServices.getIconTheme()
			.get("text-center-v-icon");

	public static final ImageIcon iright = PluginServices.getIconTheme()
			.get("text-right-icon");

	private javax.swing.JPanel jContentPane = null; // @jve:visual-info
													// decl-index=0
													// visual-constraint="593,10"

	private javax.swing.JScrollPane jScrollPane = null;

	private javax.swing.JTextArea taTexto = null;

	private JToggleButton bIzquierda = null;

	private JToggleButton bCentro = null;

	private JToggleButton bDerecha = null;

	private Layout m_layout = null;

	private Rectangle2D rect = new Rectangle2D.Double();

	private FFrameText fframetext = null; // new FFrameText();

	private boolean isAcepted = false;

	private javax.swing.JButton bFuente = null;

	private Color textcolor = null;

	private Color frameColor = null;

	private Color titleColor = null;

	private JCheckBox chkSurroundingRectangle = null;

	private JTextField txtCellPadding = null;

	private JLabel jLabel = null;

	private JCheckBox chkFontSize = null;

	private JTextField txtFontSize = null;

	private JCheckBox chkTitle = null;

	private JTextField txtTitle = null;

	private JPanel jPanel = null;

	private JTextField txtTitleSize = null;

	private JLabel jLabel3 = null;

	private JLabel jLabel4;

	private JPanel jPanel1 = null;

	private JTextField txtFrameSize = null;

	private JLabel jLabel5 = null;

	private JLabel jLabel6 = null;

	private JLabel jLabel7 = null;

	private JPRotation pRotation = null;

	private ColorChooserPanel m_colorChooser;

	private JPanel jPanel21 = null;

	private ColorChooserPanel m_colorFont; // @jve:decl-index=0:visual-constraint="474,139"

	private ColorChooserPanel m_colorTitle;

	private AcceptCancelPanel accept;

	private int pos = FFrameText.LEFT;

	private FFrameText newFFrameText = null;

	private Font m_font;

	/**
	 * This is the default constructor
	 *
	 * @param layout
	 *            Referencia al Layout.
	 * @param fframe
	 *            Referencia al fframe de texto.
	 */
	public FFrameTextDialog(Layout layout, FFrameText fframe) {
		super();
		fframetext = fframe;
		m_layout = layout;
		textcolor = fframe.getTextColor();
		frameColor = fframe.getFrameColor();
		titleColor = fframe.getTitleColor();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(419, 444);
		this.add(getJContentPane(), java.awt.BorderLayout.CENTER);
		getPRotation().setRotation(fframetext.getRotation());
	}

	/**
	 * Inserta el rect�ngulo a ocupar por el fframe de texto.
	 *
	 * @param r
	 *            Rect�ngulo.
	 */
	public void setRectangle(Rectangle2D r) {
		rect.setRect(r);
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel = new JLabel();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJScrollPane(), null);
			// jContentPane.add(getJComboBox(), null);
			// jContentPane.add(getJLabel2(), null);
			jContentPane.add(getBFuente(), null);
			jContentPane.add(getColorFont(), null);
			jLabel.setText(PluginServices.getText(this, "Milimetros"));
			jLabel.setSize(110, 16);
			jLabel.setLocation(244, 74);
			jContentPane.add(getJPanel1(), null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getChkFontSize(), null);
			jContentPane.add(getTxtFontSize(), null);
			jContentPane.add(getPRotation(), null);
			jContentPane.add(getJPanel21(), null);
			// jContentPane.add(getAcceptCancelPanel(), null);
			jContentPane.add(getAcceptCancelPanel(), null);
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
			jScrollPane.setViewportView(getTaTexto());
			jScrollPane.setSize(261, 83);
			jScrollPane
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setLocation(5, 5);
		}

		return jScrollPane;
	}

	/**
	 * This method initializes taTexto
	 *
	 * @return javax.swing.JTextArea
	 */
	private javax.swing.JTextArea getTaTexto() {
		if (taTexto == null) {
			taTexto = new javax.swing.JTextArea();

			for (int i = 0; i < fframetext.getText().size(); i++) {
				if (!((String) fframetext.getText().get(i)).equals("")) { //$NON-NLS-1$
					taTexto.append((String) fframetext.getText().get(i));
				}
			}
		}

		return taTexto;
	}

	/**
	 * This method initializes bIzquierda
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getBIzquierda() {
		if (bIzquierda == null) {
			bIzquierda = new JToggleButton();
			bIzquierda.setPreferredSize(new java.awt.Dimension(28, 20));
			if (fframetext.getPos() == FFrameText.LEFT) {
				bIzquierda.setSelected(true);
				pos=FFrameText.LEFT;
			}

			bIzquierda.setIcon(ileft);
			bIzquierda.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					pos = FFrameText.LEFT;
					// fframetext.setPos(FFrameText.LEFT);
					getBIzquierda().setSelected(true);
					getBCentro().setSelected(false);
					getBDerecha().setSelected(false);
				}
			});
		}

		return bIzquierda;
	}

	/**
	 * This method initializes bCentro
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getBCentro() {
		if (bCentro == null) {
			bCentro = new JToggleButton();
			bCentro.setPreferredSize(new java.awt.Dimension(28, 20));
			if (fframetext.getPos() == FFrameText.CENTER) {
				bCentro.setSelected(true);
				pos=FFrameText.CENTER;
			}

			bCentro.setIcon(icenterV);
			bCentro.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					pos = FFrameText.CENTER;
					// fframetext.setPos(FFrameText.CENTER);
					getBIzquierda().setSelected(false);
					getBCentro().setSelected(true);
					getBDerecha().setSelected(false);
				}
			});
		}

		return bCentro;
	}

	/**
	 * This method initializes bDerecha
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getBDerecha() {
		if (bDerecha == null) {
			bDerecha = new JToggleButton();
			bDerecha.setPreferredSize(new java.awt.Dimension(28, 20));
			if (fframetext.getPos() == FFrameText.RIGTH) {
				bDerecha.setSelected(true);
				pos=FFrameText.RIGTH;
			}

			bDerecha.setIcon(iright);
			bDerecha.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					pos = FFrameText.RIGTH;
					// fframetext.setPos(FFrameText.RIGTH);
					getBIzquierda().setSelected(false);
					getBCentro().setSelected(false);
					getBDerecha().setSelected(true);
				}
			});
		}

		return bDerecha;
	}

	private AcceptCancelPanel getAcceptCancelPanel() {
		if (accept == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newFFrameText = (FFrameText) fframetext
							.cloneFFrame(m_layout);
					newFFrameText.setPos(pos);
					newFFrameText.setBoundBox(FLayoutUtilities.toSheetRect(
							rect, m_layout.getLayoutControl().getAT()));
					newFFrameText.getText().clear();

					for (int i = 0; i < getTaTexto().getLineCount(); i++) {
						try {
							String s = getTaTexto().getText(
									getTaTexto().getLineStartOffset(i),
									getTaTexto().getLineEndOffset(i)
											- getTaTexto()
													.getLineStartOffset(i));

							if (!s.equals("")) { //$NON-NLS-1$
								newFFrameText.addText(s);
							}
						} catch (Exception ex) {
						}
					}
					newFFrameText.setTextColor(textcolor);
					// fframetext.setSizeFixed(!getChkEscalaVista().isSelected());

					// jaume
					boolean b = getChkSurroundingRectangle().isSelected();
					newFFrameText.setSurrounded(getChkSurroundingRectangle()
							.isSelected());

					if (b) {
						// Hay rect�ngulo
						newFFrameText
								.setCellPadding(Double
										.parseDouble(getTxtCellPadding()
												.getText()) / 100);
						double i;
						try {
							i = Double.parseDouble(getTxtFrameSize().getText());
						} catch (Exception ex) {
							i = 0;
						}
						if (i == 0) {
							i = 1;
						}
						newFFrameText.setFrameBorderSize(i / 100);
						newFFrameText.setFrameColor(frameColor);
					}
					b = getChkFontSize().isSelected();
					newFFrameText.setFixedFontSize(b);

					if (b) {
						// El tama�o de la fuente es fijo
						int i;
						try {
							i = Integer.parseInt(getTxtFontSize().getText());
						} catch (Exception ex) {
							i = 12;
						}
						newFFrameText.setFontSize(i);
					}
					newFFrameText.setFont(m_font);
					b = getChkTitle().isSelected();
					newFFrameText.setHasTitle(b);
					if (b) {
						// Hay t�tulo
						newFFrameText.setTitle(getTxtTitle().getText());
						int i;
						try {
							i = Integer.parseInt(getTxtTitleSize().getText());
						} catch (Exception ex) {
							i = 12;
						}
						newFFrameText.setTitleSize(i);

						newFFrameText.setTitleColor(titleColor);
					}

					newFFrameText.setRotation(getPRotation().getRotation());
					PluginServices.getMDIManager().closeWindow(
							FFrameTextDialog.this);
					// m_layout.refresh();
					isAcepted = true;
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newFFrameText=null;
					PluginServices.getMDIManager().closeWindow(
							FFrameTextDialog.this);
				}
			};
			accept = new AcceptCancelPanel(okAction, cancelAction);
			accept.setPreferredSize(new java.awt.Dimension(300, 300));
			// accept.setBounds(new java.awt.Rectangle(243,387,160,28));
			accept.setEnabled(true);
			accept.setBounds(new java.awt.Rectangle(71, 410, 300, 32));
			accept.setVisible(true);
		}
		return accept;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);

		// vi.setResizable(false);
		m_viewinfo.setTitle(PluginServices.getText(this, "Propiedades_texto")); //$NON-NLS-1$

		return m_viewinfo;
	}

	/**
	 * @see com.iver.cit.gvsig.gui.layout.fframes.dialogs.IFFrameDialog#getIsAcepted()
	 */
	public boolean getIsAcepted() {
		return isAcepted;
	}

	/**
	 * This method initializes bFuente
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBFuente() {
		if (bFuente == null) {
			bFuente = new javax.swing.JButton();
			bFuente.setBounds(158, 98, 78, 24);
			bFuente.setText(PluginServices.getText(this, "fuente")); //$NON-NLS-1$
			m_font=fframetext.getFont();
			bFuente.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Font font = FontChooser.showDialog(PluginServices.getText(
							this, "__seleccion_de_fuente"), m_font);
					if (font != null) {
						m_font=font; // fchoser=new
													// FontChooser();
													// //$NON-NLS-1$
					}
				}
			});
		}

		return bFuente;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * This method initializes chbSurroundingRectangle
	 *
	 * @return javax.swing.JCheckBox
	 * @author jaume
	 */
	private JCheckBox getChkSurroundingRectangle() {
		if (chkSurroundingRectangle == null) {
			chkSurroundingRectangle = new JCheckBox();
			chkSurroundingRectangle.setText(PluginServices.getText(this,
					"usar_marco"));
			chkSurroundingRectangle.setSelected(fframetext.isSurrounded());
			chkSurroundingRectangle.setBounds(34, 20, 122, 19);
			chkSurroundingRectangle
					.addItemListener(new java.awt.event.ItemListener() {
						public void itemStateChanged(java.awt.event.ItemEvent e) {
							getTxtCellPadding().setEnabled(
									getChkSurroundingRectangle().isSelected());
							getTxtFrameSize().setEnabled(
									getChkSurroundingRectangle().isSelected());
						}
					});
		}
		return chkSurroundingRectangle;
	}

	/**
	 * This method initializes txtCellPadding
	 *
	 * @author jaume
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtCellPadding() {
		if (txtCellPadding == null) {
			txtCellPadding = new JTextField();
			txtCellPadding.setEnabled(fframetext.isSurrounded());
			txtCellPadding.setText(fframetext.getCellPadding() * 100 + "");
			txtCellPadding.setSize(60, 20);
			txtCellPadding.setLocation(149, 74);
			txtCellPadding.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					ensureInteger(txtCellPadding);
				}
			});

		}
		return txtCellPadding;
	}

	/**
	 * This method initializes chkFontSize
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChkFontSize() {
		if (chkFontSize == null) {
			chkFontSize = new JCheckBox();
			chkFontSize.setBounds(7, 160, 143, 19);
			chkFontSize.setText(PluginServices.getText(this, "tamanyo_fuente"));
			chkFontSize.setSelected(fframetext.isFontSizeFixed());
			chkFontSize.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					getTxtFontSize().setEnabled(getChkFontSize().isSelected());
				}
			});
		}
		return chkFontSize;
	}

	/**
	 * This method initializes txtFontSize
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtFontSize() {
		if (txtFontSize == null) {
			txtFontSize = new JTextField();
			txtFontSize.setBounds(158, 162, 60, 17);
			txtFontSize.setEnabled(fframetext.isFontSizeFixed());
			if (txtFontSize.isEnabled()) {
				txtFontSize.setText(fframetext.getFontSize() + "");
			}
			txtFontSize.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					ensureInteger(txtFontSize);
				}
			});
		}
		return txtFontSize;
	}

	/**
	 * This method initializes chkTitle2
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChkTitle() {
		if (chkTitle == null) {
			chkTitle = new JCheckBox();
			chkTitle.setText(PluginServices.getText(this, "usar_titulo"));
			chkTitle.setSelected(fframetext.hasTitle());
			chkTitle.setBounds(36, 17, 118, 19);
			chkTitle.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					getTxtTitle().setEnabled(chkTitle.isSelected());
					getTxtTitleSize().setEnabled(chkTitle.isSelected());
					jLabel3.setEnabled(chkTitle.isSelected());
				}
			});
		}
		return chkTitle;
	}

	/**
	 * This method initializes txtTitle
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtTitle() {
		if (txtTitle == null) {
			txtTitle = new JTextField();
			txtTitle.setEnabled(fframetext.hasTitle());
			if (fframetext.hasTitle()) {
				txtTitle.setText(fframetext.getTitle());
			}
			txtTitle.setBounds(35, 39, 254, 20);
		}
		return txtTitle;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jLabel3 = new JLabel();
			jPanel.setLayout(null);
			jPanel.setBounds(4, 294, 403, 89);
			jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "titulo_fframetext"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jLabel3.setBounds(35, 64, 170, 20);
			jLabel3.setText(PluginServices.getText(this, "tamanyo_fuente"));
			jLabel3.setEnabled(fframetext.hasTitle());
			jPanel.add(getTxtTitleSize(), null);
			jPanel.add(jLabel3, null);
			jPanel.add(getChkTitle(), null);
			jPanel.add(getTxtTitle(), null);
			jLabel4 = new JLabel();
			jLabel4.setBounds(245, 64, 96, 20);
			jPanel.add(jLabel4, null);
			jLabel4.setText(PluginServices.getText(this, "pixeles"));
			jPanel.add(getColorTitle(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes txtTitleSize
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtTitleSize() {
		if (txtTitleSize == null) {
			txtTitleSize = new JTextField();
			txtTitleSize.setBounds(146, 65, 60, 20);
			txtTitleSize.setEnabled(fframetext.hasTitle());
			int i = fframetext.getTitleSize();
			if (i == 0 && !txtTitleSize.isEnabled()) {
				i = 5;
			}
			txtTitleSize.setText(i + "");
			txtTitleSize.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					ensureInteger(txtTitleSize);
				}
			});
		}
		return txtTitleSize;
	}

	/**
	 * Asegura cutremente que no se meten valores que no sean. El funcionamiento
	 * consiste en si el �ltimo car�cter escrito no vale para formar un int
	 * entonces se elimina.
	 *
	 * enteros.
	 *
	 * @param tf
	 */
	private void ensureInteger(JTextField tf) {
		String s = tf.getText();
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			if (s.length() != 0) {
				tf.setText(s.substring(0, s.length() - 1));
			} else {
				tf.setText("");
			}
		}
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jLabel5 = new JLabel();
			jLabel6 = new JLabel();
			jLabel7 = new JLabel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(5, 185, 403, 106);
			jPanel1.add(getTxtCellPadding(), null);
			jPanel1.add(getChkSurroundingRectangle(), null);
			jPanel1.add(jLabel, null);
			jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "marco"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jLabel5.setText(PluginServices.getText(this, "Milimetros"));
			jLabel5.setLocation(244, 46);
			jLabel5.setSize(110, 16);
			jLabel5.setPreferredSize(new java.awt.Dimension(40, 16));
			jLabel6.setBounds(34, 47, 204, 20);
			jLabel6.setText(PluginServices.getText(this, "tamanyo_borde"));
			jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			jLabel7.setBounds(34, 73, 202, 20);
			jLabel7.setText(PluginServices.getText(this, "margenes"));
			jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			jPanel1.add(getTxtFrameSize(), null);
			jPanel1.add(jLabel6, null);
			jPanel1.add(jLabel7, null);
			jPanel1.add(jLabel5, null);
			// jPanel1.add(getColorFrame(), null);
			jPanel1.add(getColorFrame(), null);
			jPanel1.add(getColorFrame(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes txtFrameSize
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtFrameSize() {
		if (txtFrameSize == null) {
			txtFrameSize = new JTextField();
			txtFrameSize.setEnabled(fframetext.isSurrounded());
			// if (txtFrameSize.isEnabled()){
			double i = fframetext.getFrameBorderSize() * 100;
			if (i == 0) {
				i = 1;
			}
			txtFrameSize.setText(i + "");
			// }
			txtFrameSize.setLocation(149, 47);
			txtFrameSize.setSize(60, 20);
		}
		return txtFrameSize;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private ColorChooserPanel getColorFrame() {
		if (m_colorChooser == null) {
			m_colorChooser = new ColorChooserPanel();
			m_colorChooser.setAlpha(255);
			m_colorChooser.setBounds(new java.awt.Rectangle(155, 15, 80, 25));
			m_colorChooser
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(ActionEvent e) {
							frameColor = m_colorChooser.getColor();
						}
					});
		}
		return m_colorChooser;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private ColorChooserPanel getColorFont() {
		if (m_colorFont == null) {
			m_colorFont = new ColorChooserPanel();
			m_colorFont.setAlpha(255);
			m_colorFont.setLocation(new java.awt.Point(158, 129));
			m_colorFont.setSize(new java.awt.Dimension(80, 25));
			m_colorFont.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textcolor = m_colorFont.getColor();
				}
			});
		}
		return m_colorFont;
	}

	/**
	 * This method initializes jButton1
	 *
	 * @return javax.swing.JButton
	 */
	private ColorChooserPanel getColorTitle() {
		if (m_colorTitle == null) {
			m_colorTitle = new ColorChooserPanel();
			m_colorTitle.setAlpha(255);
			m_colorTitle.setBounds(new java.awt.Rectangle(155, 11, 80, 25));
			m_colorTitle.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					titleColor = m_colorTitle.getColor();
				}
			});
		}
		return m_colorTitle;
	}

	/**
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(281, 0, 125, 131);
		}
		return pRotation;
	}

	/**
	 * This method initializes jPanel21
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel21() {
		if (jPanel21 == null) {
			jPanel21 = new JPanel();
			jPanel21.setBounds(new java.awt.Rectangle(6, 95, 126, 60));
			jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "alinear"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					null));
			jPanel21.add(getBIzquierda(), null);
			jPanel21.add(getBCentro(), null);
			jPanel21.add(getBDerecha(), null);
		}
		return jPanel21;
	}

	public IFFrame getFFrame() {
		return newFFrameText;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
