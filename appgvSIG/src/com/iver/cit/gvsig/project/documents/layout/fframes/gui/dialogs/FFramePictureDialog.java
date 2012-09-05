/*
 * Created on 02-jun-2004
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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFramePicture;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.GenericFileFilter;


/**
 * Dialogo para añadir una imagen al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFramePictureDialog extends JPanel implements IFFrameDialog {
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel lFichero = null;
	private javax.swing.JTextField tFichero = null;
	private javax.swing.JButton bExaminar = null;
	private javax.swing.JLabel lVisualizacion = null;
	private javax.swing.JComboBox cbVisualizacion = null;
	private javax.swing.JLabel lCalidad = null;
	private javax.swing.JComboBox cbCalidad = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private FFramePicture fframepicture = null; // = new FFramePicture();
	private String namefile = null;
	private Layout m_layout = null;
	private boolean isAcepted = false;
	private JPRotation pRotation = null;
	private String path;
	private FFramePicture newFFramePicture=null;


	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al fframe de imagen.
	 */
	public FFramePictureDialog(Layout layout, FFramePicture fframe) {
		super();
		fframepicture = fframe;
		m_layout = layout;
		initialize();
	}

	/**
	 * Inserta el rectángulo que ocupará el fframe de imagen.
	 *
	 * @param r Rectángulo.
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
		this.setSize(505, 142);
		getPRotation().setRotation(fframepicture.getRotation());
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
			jContentPane.add(getLFichero(), null);
			jContentPane.add(getLCalidad(), null);
			jContentPane.add(getCbCalidad(), null);
			jContentPane.add(getTFichero(), null);
			jContentPane.add(getBExaminar(), null);
			jContentPane.add(getLVisualizacion(), null);
			jContentPane.add(getCbVisualizacion(), null);
			jContentPane.add(getBAceptar(), null);
			jContentPane.add(getBCancelar(), null);
			jContentPane.setSize(499, 139);
			jContentPane.setLocation(3, 0);
			jContentPane.add(getPRotation(), null);
		}

		return jContentPane;
	}

	/**
	 * This method initializes lFichero
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLFichero() {
		if (lFichero == null) {
			lFichero = new javax.swing.JLabel();
			lFichero.setSize(42, 20);
			lFichero.setText(PluginServices.getText(this, "Fichero"));
			lFichero.setLocation(10, 9);
		}

		return lFichero;
	}

	/**
	 * This method initializes tFichero
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTFichero() {
		if (tFichero == null) {
			tFichero = new javax.swing.JTextField();
			tFichero.setBounds(62, 8, 200, 20);
			tFichero.setPreferredSize(new java.awt.Dimension(200, 20));
		}

		if (fframepicture.getPath() != null) {
			tFichero.setText(fframepicture.getPath());
		}

		return tFichero;
	}

	/**
	 * This method initializes bExaminar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBExaminar() {
		if (bExaminar == null) {
			bExaminar = new javax.swing.JButton();
			bExaminar.setSize(88, 23);
			bExaminar.setText(PluginServices.getText(this, "Examinar"));
			bExaminar.setLocation(275, 6);
			bExaminar.setPreferredSize(new java.awt.Dimension(88, 23));
			bExaminar.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent e) {
						String[] extensions = {
								"jpeg", "jpg", "gif", "png", "bmp", "svg"
							};
						GenericFileFilter filter = new GenericFileFilter(extensions,
								"Image files (jpeg, jpg, gif, png, bmp, svg)");

						JFileChooser jfc = new JFileChooser();
						jfc.addChoosableFileFilter(filter);
						if (jfc.showOpenDialog(
									(Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
							File file = jfc.getSelectedFile();
							namefile = file.getName();
							getTFichero().setText(file.getAbsolutePath());
							path=file.getAbsolutePath();
//							fframepicture.setPath(file.getAbsolutePath());
						}
					}
				});
		}

		return bExaminar;
	}

	/**
	 * Inserta el path al TFichero.
	 *
	 * @param val path del fichero.
	 */
	public void setText(String val) {
		getTFichero().setText(val);
	}

	/**
	 * This method initializes lVisualizacion
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLVisualizacion() {
		if (lVisualizacion == null) {
			lVisualizacion = new javax.swing.JLabel();
			lVisualizacion.setBounds(9, 62, 105, 20);
			lVisualizacion.setText(PluginServices.getText(this, "visualizacion"));
			lVisualizacion.setVisible(false);
		}

		return lVisualizacion;
	}

	/**
	 * This method initializes cbVisualizacion
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbVisualizacion() {
		if (cbVisualizacion == null) {
			cbVisualizacion = new javax.swing.JComboBox();
			cbVisualizacion.setSize(245, 20);
			cbVisualizacion.addItem(PluginServices.getText(this, "cuando_activo"));
			cbVisualizacion.addItem(PluginServices.getText(this, "siempre"));
			cbVisualizacion.setSelectedIndex(fframepicture.getViewing());
			cbVisualizacion.setPreferredSize(new java.awt.Dimension(200, 20));
			cbVisualizacion.setLocation(118, 62);
			cbVisualizacion.setEnabled(false);
			cbVisualizacion.setVisible(false);
		}

		return cbVisualizacion;
	}

	/**
	 * This method initializes lCalidad
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLCalidad() {
		if (lCalidad == null) {
			lCalidad = new javax.swing.JLabel();
			lCalidad.setSize(103, 20);
			lCalidad.setText(PluginServices.getText(this, "calidad"));
			lCalidad.setPreferredSize(new java.awt.Dimension(73,16));
			lCalidad.setLocation(10, 35);
		}

		return lCalidad;
	}

	/**
	 * This method initializes cbCalidad
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbCalidad() {
		if (cbCalidad == null) {
			cbCalidad = new javax.swing.JComboBox();
			cbCalidad.setSize(245, 20);
			cbCalidad.addItem(PluginServices.getText(this, "presentacion"));
			cbCalidad.addItem(PluginServices.getText(this, "borrador"));
			cbCalidad.setSelectedIndex(fframepicture.getQuality());
			cbCalidad.setPreferredSize(new java.awt.Dimension(200, 20));
			cbCalidad.setLocation(118, 35);
		}

		return cbCalidad;
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
			bAceptar.setLocation(70, 89);
			bAceptar.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent e) {
					    Dimension dimension = null;
						try {
						    // ImageIcon imageIcon = new ImageIcon(getTFichero().getText());
						    dimension = fframepicture.getBound(getTFichero().getText());
						} catch (Exception ex) {
							NotificationManager.addError("Excepción :", ex);
						}
						newFFramePicture=(FFramePicture)fframepicture.cloneFFrame(m_layout);
						if (path==null) {
							path=getTFichero().getText();
						}
						newFFramePicture.setPath(path);
						newFFramePicture.setViewing(getCbVisualizacion()
													 .getSelectedIndex());
						newFFramePicture.setQuality(getCbCalidad()
													 .getSelectedIndex());

						if (namefile != null) {
							newFFramePicture.setName(namefile);
						}
						// Ajustamos la relación de aspecto a la altura.
						double ratio = (float) (dimension.getWidth()) / (float) (dimension.getHeight());

						double newWidth = rect.getHeight() * ratio;
						Rectangle2D.Double rAdjust = new Rectangle2D.Double(
						        rect.getMinX(), rect.getMinY(),
						        newWidth, rect.getHeight());
				        rect = rAdjust;

						newFFramePicture.setBoundBox(FLayoutUtilities.toSheetRect(
								rect, m_layout.getLayoutControl().getAT()));
						newFFramePicture.setRotation(getPRotation().getRotation());
						PluginServices.getMDIManager().closeWindow(FFramePictureDialog.this);
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
			bCancelar.setLocation(219, 89);
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						newFFramePicture=null;
						PluginServices.getMDIManager().closeWindow(FFramePictureDialog.this);
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
				"propiedades_marco_imagenes"));

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
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(373, 14, 120, 120);
		}
		return pRotation;
	}

	public IFFrame getFFrame() {
		return newFFramePicture;
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
