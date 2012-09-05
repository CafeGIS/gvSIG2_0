/*
 * Created on 01-jun-2004
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

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.ViewPort;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGrid;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGridFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.ListViewModel;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectView;


/**
 * Diálogo para añadir una vista al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameViewDialog extends JPanel implements IFFrameDialog {
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel lVistas = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JList liVistas = null; //  @jve:visual-info  decl-index=0 visual-constraint="98,-3"
	private javax.swing.JCheckBox chbEnlaceVivo = null;
	private javax.swing.JLabel lEscala = null;
	private javax.swing.JComboBox cbEscala = null;
	private javax.swing.JLabel lEscalaNumerica = null;
	private javax.swing.JTextField tEscalaNumerica = null;
	private javax.swing.JLabel lExtension = null;
	private javax.swing.JComboBox cbExtension = null;
	private javax.swing.JLabel lVisualizacion = null;
	private javax.swing.JComboBox cbVisualizacion = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private javax.swing.JLabel lCalidad = null;
	private javax.swing.JComboBox cbCalidad = null;
	private ProjectView m_projectView = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private FFrameView fframeview = null; //new FFrameView();
	private Layout m_layout = null;
	private boolean isAcepted = false;
	private JPRotation rotation = null;
	private FFrameView newFFrameView=null;
	private javax.swing.JButton bGrid = null;
	private javax.swing.JCheckBox chbShowGrid = null;

	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al fframe vista.
	 */
	public FFrameViewDialog(Layout layout, FFrameView fframe) {
		super();
		fframeview = fframe;
		m_layout = layout;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);
		this.add(getJContentPane(), null);
		this.setSize(478, 269);

		if (fframeview.getTypeScale() == 0 || fframeview.getTypeScale() == 1) {
				getTEscalaNumerica().setEnabled(false);
			}else{
				getTEscalaNumerica().setEnabled(true);
			}
		getPRotation().setRotation(fframeview.getRotation());
	}

	/**
	 * Inserta el rectángulo que ocupará el fframe vista.
	 *
	 * @param r Rectángulo.
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
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getLVistas(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getChbEnlaceVivo(), null);
			jContentPane.add(getLEscala(), null);
			jContentPane.add(getCbEscala(), null);
			jContentPane.add(getLEscalaNumerica(), null);
			jContentPane.add(getTEscalaNumerica(), null);
			jContentPane.add(getLExtension(), null);
			jContentPane.add(getCbExtension(), null);
			jContentPane.add(getLVisualizacion(), null);
			jContentPane.add(getCbVisualizacion(), null);
			jContentPane.add(getLCalidad(), null);
			jContentPane.add(getCbCalidad(), null);
			jContentPane.add(getbAceptar(), null);
			jContentPane.add(getBCancelar(), null);
			jContentPane.setSize(475, 264);
			jContentPane.setPreferredSize(new java.awt.Dimension(60, 60));
			jContentPane.setLocation(0, 0);
			jContentPane.add(getPRotation(), null);
			jContentPane.add(getBGrid(), null);
			jContentPane.add(getChbShowGrid(), null);
		}

		return jContentPane;
	}

	/**
	 * This method initializes lVistas
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLVistas() {
		if (lVistas == null) {
			lVistas = new javax.swing.JLabel();
			lVistas.setSize(85, 19);
			lVistas.setText(PluginServices.getText(this, "Vista"));
			lVistas.setLocation(10, 10);
		}

		return lVistas;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getLiVistas());
			jScrollPane.setBounds(99, 8, 224, 64);
			jScrollPane.setPreferredSize(new java.awt.Dimension(250, 60));
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
			listmodel.addViews();
			liVistas = new javax.swing.JList();
			liVistas.setModel(listmodel);
			for (int i = 0; i < liVistas.getModel().getSize(); i++) {
				if (fframeview.getMapContext() != null) {

					ProjectView pvaux= (ProjectView) liVistas.getModel()
														  .getElementAt(i);

					if (pvaux.getMapContext().equals(fframeview.getMapContext())) {
						liVistas.setSelectedIndex(i);
						m_projectView= (ProjectView) liVistas.getModel()
						  .getElementAt(i);
					}
				}
			}

			liVistas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					private int selectIndex=-1;
					private Rectangle2D boundBox;
					public void valueChanged(
						javax.swing.event.ListSelectionEvent e) {
						int selectInt = ((JList)e.getSource()).getSelectedIndex();
						if (selectInt!=selectIndex){
							selectIndex=selectInt;
							if (selectIndex==-1) {
								return;
							}
						m_projectView = (ProjectView) liVistas.getModel()
															  .getElementAt(selectInt);

						getCbEscala().setSelectedItem(PluginServices.getText(this, "automatico"));
						boundBox=FLayoutUtilities.toSheetRect(rect, m_layout.getLayoutControl().getAT());
//						fframeview.setBoundBox(FLayoutUtilities.toSheetRect(
//								rect, m_layout.getAT()));

//						fframeview.setView(m_projectView);
//
//						fframeview.setMapUnits(m_projectView.getMapContext()
//															.getViewPort()
//															.getMapUnits());
						ViewPort vp=m_projectView.getMapContext().getViewPort().cloneViewPort();
						vp.setImageSize(new Dimension((int)rect.getWidth(),(int)rect.getHeight()));
						getTEscalaNumerica().setText(String.valueOf(FLayoutUtilities.getScaleView(vp,
				                boundBox.getWidth(), FLayoutUtilities.fromSheetDistance(boundBox.getWidth(),m_layout.getLayoutControl().getAT()))));
//						getTEscalaNumerica().setText(String.valueOf(
//								fframeview.getScale()));
					}
					}
				});
		}

		return liVistas;
	}

	/**
	 * This method initializes chbEnlaceVivo
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbEnlaceVivo() {
		if (chbEnlaceVivo == null) {
			chbEnlaceVivo = new javax.swing.JCheckBox();
			chbEnlaceVivo.setSize(88, 24);
			chbEnlaceVivo.setText(PluginServices.getText(this, "enlace_vivo"));
			chbEnlaceVivo.setSelected(fframeview.getLinked());
			chbEnlaceVivo.setLocation(10, 75);
			chbEnlaceVivo.setEnabled(true);
			chbEnlaceVivo.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					}
				});
		}

		return chbEnlaceVivo;
	}

	/**
	 * This method initializes lEscala
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLEscala() {
		if (lEscala == null) {
			lEscala = new javax.swing.JLabel();
			lEscala.setSize(86, 16);
			lEscala.setText(PluginServices.getText(this, "escala"));
			lEscala.setLocation(10, 105);
		}

		return lEscala;
	}

	/**
	 * This method initializes cbEscala
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbEscala() {
		if (cbEscala == null) {
			//String[] s={"Automático","Conservar escala de visualización","Escala especificada por el usuario"};
			cbEscala = new javax.swing.JComboBox();
			cbEscala.setSize(220, 20);
			cbEscala.addItem(PluginServices.getText(this, "automatico"));
			cbEscala.addItem(PluginServices.getText(this,
					"conservar_escala_visualizacion"));
			cbEscala.addItem(PluginServices.getText(this, "escala_usuario"));
			cbEscala.setSelectedIndex(fframeview.getTypeScale());

			if (fframeview.getTypeScale() == 0 || fframeview.getTypeScale() == 1) {
				getTEscalaNumerica().setEnabled(false);
			}else{
				getTEscalaNumerica().setEnabled(true);
			}

			cbEscala.setPreferredSize(new java.awt.Dimension(200, 20));
			cbEscala.setLocation(100, 105);
			cbEscala.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (cbEscala.getSelectedIndex() == 2) {
							getTEscalaNumerica().setEnabled(true);
						} else {
							getTEscalaNumerica().setEnabled(false);
						}
					}
				});
		}

		return cbEscala;
	}

	/**
	 * This method initializes lEscalaNumerica
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLEscalaNumerica() {
		if (lEscalaNumerica == null) {
			lEscalaNumerica = new javax.swing.JLabel();
			lEscalaNumerica.setSize(86, 16);
			lEscalaNumerica.setText("1:");
			lEscalaNumerica.setLocation(10, 130);
		}

		return lEscalaNumerica;
	}

	/**
	 * This method initializes tEscalaNumerica
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTEscalaNumerica() {
		if (tEscalaNumerica == null) {
			tEscalaNumerica = new javax.swing.JTextField();
			tEscalaNumerica.setSize(220, 20);
			tEscalaNumerica.setPreferredSize(new java.awt.Dimension(200, 20));

			if (m_projectView != null) {
				tEscalaNumerica.setText(String.valueOf(fframeview.getScale()));
			}
			tEscalaNumerica.setLocation(100, 130);
			tEscalaNumerica.setEnabled(false);
		}

		return tEscalaNumerica;
	}

	/**
	 * This method initializes lExtension
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLExtension() {
		if (lExtension == null) {
			lExtension = new javax.swing.JLabel();
			lExtension.setSize(86, 16);
			lExtension.setText(PluginServices.getText(this, "extension"));
			lExtension.setLocation(10, 155);
			lExtension.setVisible(false);
		}

		return lExtension;
	}

	/**
	 * This method initializes cbExtension
	 *
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getCbExtension() {
		if (cbExtension == null) {
			//String[] s={"Rellenar marco de la vista","Recorte a vista"};
			cbExtension = new javax.swing.JComboBox();
			cbExtension.setSize(220, 20);
			cbExtension.addItem(PluginServices.getText(this,
					"rellenar_marco_vista"));
			cbExtension.addItem(PluginServices.getText(this, "recorte_vista"));

			cbExtension.setPreferredSize(new java.awt.Dimension(200, 20));
			cbExtension.setLocation(100, 155);
			cbExtension.setEnabled(false);
			cbExtension.setVisible(false);
		}

		return cbExtension;
	}

	/**
	 * This method initializes jLabel4
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLVisualizacion() {
		if (lVisualizacion == null) {
			lVisualizacion = new javax.swing.JLabel();
			lVisualizacion.setSize(86, 16);
			lVisualizacion.setText(PluginServices.getText(this, "visualizacion"));
			lVisualizacion.setLocation(10, 180);
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
			//String[] s={"Cuando activo","Siempre"};
			cbVisualizacion = new javax.swing.JComboBox();
			cbVisualizacion.setSize(220, 20);
			cbVisualizacion.addItem(PluginServices.getText(this, "cuando_activo"));
			cbVisualizacion.addItem(PluginServices.getText(this, "siempre"));
			cbVisualizacion.setSelectedIndex(fframeview.getViewing());
			cbVisualizacion.setPreferredSize(new java.awt.Dimension(200, 20));
			cbVisualizacion.setLocation(100, 180);
			cbVisualizacion.setEnabled(false);
			cbVisualizacion.setVisible(false);
		}

		return cbVisualizacion;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getbAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setSize(85, 26);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.setBounds(new java.awt.Rectangle(109,223,85,26));
			bAceptar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						newFFrameView=(FFrameView)fframeview.cloneFFrame(m_layout);
						newFFrameView.setBoundBox(FLayoutUtilities.toSheetRect(
								rect, m_layout.getLayoutControl().getAT()));
						newFFrameView.setRotation(getPRotation().getRotation());
						if (m_projectView != null) {
							newFFrameView.setLinked(getChbEnlaceVivo().isSelected());
							newFFrameView.setName(m_projectView.getName());

							newFFrameView.setTypeScale(getCbEscala()
														.getSelectedIndex());
							newFFrameView.setScale(Double.parseDouble(
									getTEscalaNumerica().getText()));
							newFFrameView.setView(m_projectView);

							//fframeview.setFMap(m_projectView.getMapContext());
							newFFrameView.setExtension(getCbExtension()
														.getSelectedIndex());
							newFFrameView.setViewing(getCbVisualizacion()
													  .getSelectedIndex());
							newFFrameView.setQuality(getCbCalidad()
													  .getSelectedIndex());
							if (fframeview.getGrid()!=null){
								((FFrameGrid)fframeview.getGrid()).setLayout(m_layout);
								((FFrameGrid)fframeview.getGrid()).setFFrameDependence(newFFrameView);
//								((FFrameGrid)grid).setBoundBox();
								newFFrameView.setGrid(fframeview.getGrid());
							}
							newFFrameView.showGrid(getChbShowGrid().isSelected());
							newFFrameView.setBoundBox(FLayoutUtilities.toSheetRect(
									rect, m_layout.getLayoutControl().getAT()));

							/*        for (int i=0;i<m_layout.getFFrames().size();i++){
							   IFFrame fframe=(IFFrame)m_layout.getFFrames().get(i);
							   if (fframe instanceof FFrameScaleBar){
							           if (((FFrameScaleBar)fframe).getFFrameView()==fframeview){
							           if (getJComboBox().getSelectedIndex()!=0){
							           ((FFrameScaleBar)fframe).setIsFixed(true);
							           ((FFrameScaleBar)fframe).setScaleView(Long.parseLong(getJTextField().getText()));
							           }else{
							                   ((FFrameScaleBar)fframe).setIsFixed(false);
							           }

							           }
							   }


							   }
							 */
						}

						isAcepted = true;

						//}else{
						//	isAcepted=false;
						//}
						PluginServices.getMDIManager().closeWindow(FFrameViewDialog.this);
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
			bCancelar.setBounds(new java.awt.Rectangle(207,223,85,26));
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						newFFrameView=null;
						PluginServices.getMDIManager().closeWindow(FFrameViewDialog.this);
					}
				});
		}

		return bCancelar;
	}

	/**
	 * This method initializes lCalidad
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLCalidad() {
		if (lCalidad == null) {
			lCalidad = new javax.swing.JLabel();
			lCalidad.setSize(86, 16);
			lCalidad.setText(PluginServices.getText(this, "calidad"));
			lCalidad.setLocation(10, 155);
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
			//String[] s={"Presentación","Borrador"};
			cbCalidad = new javax.swing.JComboBox();
			cbCalidad.setSize(220, 20);
			cbCalidad.addItem(PluginServices.getText(this, "presentacion"));
			cbCalidad.addItem(PluginServices.getText(this, "borrador"));
			cbCalidad.setSelectedIndex(fframeview.getQuality());
			cbCalidad.setPreferredSize(new java.awt.Dimension(200, 20));
			cbCalidad.setLocation(100, 155);
		}

		return cbCalidad;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);

		//vi.setResizable(false);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"propiedades_marco_vista"));

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
	 * This method initializes rotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (rotation == null) {
			rotation = new JPRotation();
			rotation.setBounds(330, 90, 120, 120);
		}
		return rotation;
	}

	public IFFrame getFFrame() {
		return newFFrameView;
	}
	/**
	 * This method initializes bGrid
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBGrid() {
		if (bGrid == null) {
			bGrid = new JButton();
			bGrid.setBounds(new java.awt.Rectangle(207,186,85,26));
			bGrid.setText(PluginServices.getText(this,"configurar"));
			bGrid.addActionListener(new java.awt.event.ActionListener() {


				public void actionPerformed(java.awt.event.ActionEvent e) {
					FFrameGridFactory gridFactory=new FFrameGridFactory();
					FFrameGrid fframe = null;

					if (fframeview.getGrid() != null){
						fframe = (FFrameGrid)fframeview.getGrid();
					}else{
						fframe = (FFrameGrid)gridFactory.createFrame();
						fframe.setLayout(m_layout);
						fframe.setFFrameDependence(fframeview);
					}


					FFrameGridDialog fframedialog = new FFrameGridDialog(m_layout, fframe,fframeview);
//					fframedialog.setFFrameView(fframeview);
					if (fframedialog != null) {
				            PluginServices.getMDIManager().addWindow(fframedialog);
				        }
				    IFFrame newFrame= fframedialog.getFFrame();
				    if (newFrame!=null) {
				    	fframeview.setGrid(newFrame);
					}
				}
			});
		}
		return bGrid;
	}

	/**
	 * This method initializes chbShowGrid
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbShowGrid() {
		if (chbShowGrid == null) {
			chbShowGrid = new JCheckBox();
			chbShowGrid.setBounds(new java.awt.Rectangle(13,191,181,21));
			chbShowGrid.setSelected(fframeview.isShowGrid());
			chbShowGrid.setText(PluginServices.getText(this,"Show_grid"));
		}
		return chbShowGrid;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"