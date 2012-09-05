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

import java.awt.geom.Rectangle2D;

import javax.swing.JList;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameOverView;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.ListViewModel;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo para añadir una localizador al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameOverViewDialog extends JPanel implements IFFrameDialog {
	private javax.swing.JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="37,10"
	private javax.swing.JLabel lVistas = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JList liVistas = null; //  @jve:visual-info  decl-index=0 visual-constraint="98,-3"
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private javax.swing.JLabel lCalidad = null;
	private javax.swing.JComboBox cbCalidad = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private FFrameOverView fframeoverview = null; //new FFrameView();
	private Layout m_layout = null;
	private boolean isAcepted = false;
	private JPRotation rotation = null;
	private FFrameOverView newFFrameView;
	private FFrameView fframeDependence;
	private FFrameView fframeview;


	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al fframe vista.
	 */
	public FFrameOverViewDialog(Layout layout, FFrameOverView fframe) {
		super();
		fframeoverview = fframe;
		fframeDependence=(FFrameView)fframeoverview.getFFrameDependence()[0];
		m_layout = layout;
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);
		this.add(getJContentPane(), null);
		this.setSize(463, 222);

		getPRotation().setRotation(fframeoverview.getRotation());
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
			jContentPane.add(getLCalidad(), null);
			jContentPane.add(getCbCalidad(), null);
			jContentPane.add(getbAceptar(), null);
			jContentPane.add(getBCancelar(), null);
			jContentPane.setSize(462, 184);
			jContentPane.setPreferredSize(new java.awt.Dimension(60, 60));
			jContentPane.setLocation(0, 0);
			jContentPane.add(getPRotation(), null);
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

			//listmodel.addViews();
			listmodel.addViews(m_layout);

			///ArrayList list = listmodel.getViews();
			liVistas = new javax.swing.JList();
			liVistas.setModel(listmodel);

			for (int i = 0; i < liVistas.getModel().getSize(); i++) {
				if (fframeDependence != null) {
					fframeview = (FFrameView) liVistas.getModel().getElementAt(i);

					if (fframeview == fframeoverview.getFFrameDependence()[0]) {
						liVistas.setSelectedIndex(i);
					}

					/*m_projectView=(ProjectView)liVistas.getModel().getElementAt(i);
					   if (m_projectView.getMapContext()==fframelegend.getFMap()){
					           liVistas.setSelectedIndex(i);
					   }
					 */
				}
			}

			liVistas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					private int selectIndex=-1;
					public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							IFFrame[] fframes=m_layout.getLayoutContext().getFFrames();
							int selectInt =((JList) e.getSource())
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
											fframeDependence=fframeview;
//											fframelegend
//													.setFFrameDependence(fframeview);
										}
									}
								}

								// fframelegend.setFFrameView(fframeview);
							}
						}

					});
		}

		return liVistas;
	}
//	/**
//	 * This method initializes liVistas
//	 *
//	 * @return javax.swing.JList
//	 */
//	private javax.swing.JList getLiVistas() {
//		if (liVistas == null) {
//			ListViewModel listmodel = new ListViewModel();
//			listmodel.addViews();
//			liVistas = new javax.swing.JList();
//			liVistas.setModel(listmodel);
//			for (int i = 0; i < liVistas.getModel().getSize(); i++) {
//				if (fframeoverview.getMapContext() != null) {
//
//					ProjectView pvaux= (ProjectView) liVistas.getModel()
//														  .getElementAt(i);
//
//					if (pvaux.getMapContext().equals(fframeoverview.getMapContext())) {
//						liVistas.setSelectedIndex(i);
//						m_projectView= (ProjectView) liVistas.getModel()
//						  .getElementAt(i);
//					}
//				}
//			}
//
//			liVistas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
//					private int selectIndex=-1;
//					public void valueChanged(
//						javax.swing.event.ListSelectionEvent e) {
//						int selectInt = ((JList)e.getSource()).getSelectedIndex();
//						if (selectInt!=selectIndex){
//							selectIndex=selectInt;
//							if (selectIndex==-1)
//								return;
//						m_projectView = (ProjectView) liVistas.getModel()
//															  .getElementAt(selectInt);
//
////						fframeview.setBoundBox(FLayoutUtilities.toSheetRect(
////								rect, m_layout.getAT()));
////						fframeview.setView(m_projectView);
////
////						fframeview.setMapUnits(m_projectView.getMapContext()
////															.getViewPort()
////															.getMapUnits());
//					}
//					}
//				});
//		}
//
//		return liVistas;
//	}

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
			bAceptar.setLocation(59, 136);
			bAceptar.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent e) {
						newFFrameView=(FFrameOverView)fframeoverview.cloneFFrame(m_layout);
						newFFrameView.setBoundBox(FLayoutUtilities.toSheetRect(
								rect, m_layout.getLayoutControl().getAT()));
						newFFrameView.setRotation(getPRotation().getRotation());
						if (fframeDependence!=null) {
							newFFrameView.setName(fframeDependence.getView().getName());

							newFFrameView.setFFrameDependence(fframeDependence);
							newFFrameView.setView(fframeDependence.getView());

							newFFrameView.setQuality(getCbCalidad()
													  .getSelectedIndex());
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
						PluginServices.getMDIManager().closeWindow(FFrameOverViewDialog.this);
						fframeoverview.refresh();
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
			bCancelar.setSize(85, 26);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.setLocation(199, 136);
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						newFFrameView=null;
						PluginServices.getMDIManager().closeWindow(FFrameOverViewDialog.this);
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
			lCalidad.setLocation(14, 112);
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
			cbCalidad.setSelectedIndex(fframeoverview.getQuality());
			cbCalidad.setPreferredSize(new java.awt.Dimension(200, 20));
			cbCalidad.setLocation(104, 112);
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
				"propiedades_marco_localizador"));

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
			rotation.setBounds(330, 30, 120, 130);
		}
		return rotation;
	}

	public IFFrame getFFrame() {
		return newFFrameView;
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
