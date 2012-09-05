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
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.gui.beans.AcceptCancelPanel;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.utils.FontChooser;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameLegend;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.ListViewModel;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Dialogo para añadir la leyenda de alguna vista al Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameLegendDialog extends JPanel implements IFFrameDialog {
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JLabel lMarcoVista = null;
	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JLabel lVisualizacion = null;
	private javax.swing.JComboBox cbVisualizacion = null;
	private javax.swing.JLabel lCalidad = null;
	private javax.swing.JComboBox cbCalidad = null;
	private javax.swing.JList liVistas = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private Layout m_layout = null; //  @jve:visual-info  decl-index=0 visual-constraint="393,10"
	private FFrameLegend fframelegend = null; //new FFrameLegend();
	private ArrayList nameLayers=new ArrayList();
	private ArrayList areVisible=new ArrayList();
	private boolean isAcepted = false;
	private JButton bFuente = null;
	private FFrameView fframeview = null;
	private JPRotation pRotation = null;
	//private JLabel lblNumColum = null;
	//private JTextField txtNumColum = null;
	//private ColumPanel pNumColum = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel jPanel = null;
	private AcceptCancelPanel accept;
	private IFFrame fframeDependence;
	private Font font;
	private FFrameLegend newFFrameLegend;


	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al FFrameLegend.
	 */
	public FFrameLegendDialog(Layout layout, FFrameLegend fframe) {
		super();
		m_layout = layout;
		fframelegend = fframe;
		font=fframelegend.getFont();
		fframeDependence=fframelegend.getFFrameDependence()[0];
		initialize();
	}

	/**
	 * Inserta el rectángulo que ocupará el fframe de leyenda.
	 *
	 * @param r extent.
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
		this.setSize(616, 199);
		this.setSize(new java.awt.Dimension(616,189));
		getPRotation().setRotation(fframelegend.getRotation());
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
			jContentPane.setBounds(new java.awt.Rectangle(3,1,609,183));
			jContentPane.add(getLMarcoVista(), null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getLVisualizacion(), null);
			jContentPane.add(getCbVisualizacion(), null);
			jContentPane.add(getLCalidad(), null);
			jContentPane.add(getCbCalidad(), null);
			//jContentPane.add(getBAceptar(), null);
			//jContentPane.add(getBCancelar(), null);
			jContentPane.add(getAcceptCancelPanel(),null);
			jContentPane.add(getBFuente(), null);
			jContentPane.setBounds(3, 1, 609, 241);
			jContentPane.add(getPRotation(), null);
			//jContentPane.add(lblNumColum, null);
			jContentPane.add(getJScrollPane1(), null);
		}

		return jContentPane;
	}

	/**
	 * This method initializes lMarcoVista
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLMarcoVista() {
		if (lMarcoVista == null) {
			lMarcoVista = new javax.swing.JLabel();
			lMarcoVista.setSize(115, 63);
			lMarcoVista.setText(PluginServices.getText(this, "marco_vista"));
			lMarcoVista.setLocation(5, 10);
		}

		return lMarcoVista;
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
			jScrollPane.setSize(179, 65);
			jScrollPane.setPreferredSize(new java.awt.Dimension(70, 60));
			jScrollPane.setLocation(129, 10);
		}

		return jScrollPane;
	}

	/**
	 * This method initializes lVisualizacion
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLVisualizacion() {
		if (lVisualizacion == null) {
			lVisualizacion = new javax.swing.JLabel();
			lVisualizacion.setSize(115, 16);
			lVisualizacion.setText(PluginServices.getText(this, "visualizacion"));
			lVisualizacion.setLocation(6, 83);
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
			cbVisualizacion.addItem(PluginServices.getText(this, "cuando_activo"));
			cbVisualizacion.addItem(PluginServices.getText(this, "siempre"));
			cbVisualizacion.setSelectedIndex(fframelegend.getViewing());
			cbVisualizacion.setSize(179, 20);
			cbVisualizacion.setPreferredSize(new java.awt.Dimension(200, 20));
			cbVisualizacion.setLocation(129, 83);
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
			lCalidad.setSize(115, 20);
			lCalidad.setText(PluginServices.getText(this, "calidad"));
			lCalidad.setLocation(6, 103);
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
			cbCalidad.addItem(PluginServices.getText(this, "presentacion"));
			cbCalidad.addItem(PluginServices.getText(this, "borrador"));
			cbCalidad.setSelectedIndex(fframelegend.getQuality());
			cbCalidad.setSize(179, 20);
			cbCalidad.setPreferredSize(new java.awt.Dimension(200, 20));
			cbCalidad.setLocation(129, 103);
		}

		return cbCalidad;
	}
	private AcceptCancelPanel getAcceptCancelPanel() {
		if (accept == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
//					IFFrame[] fframes=m_layout.getFFrames();
//					for (int i = 0; i < fframes.length;
//							i++) {
//						IFFrame f = fframes[i];
//
//						if (f instanceof FFrameView &&
//								(fframeview != null)) {
//							fframelegend.setName("Leyenda de " +
//								((FFrameView) f).toString());
//
//
//						}
//					}
					newFFrameLegend=(FFrameLegend)fframelegend.cloneFFrame(m_layout);
					newFFrameLegend.setFFrameDependence(fframeDependence);
					newFFrameLegend.setFont(font);

					/*if (m_projectView!=null){
					   fframelegend.setLayers(m_projectView.getMapContext().getLayers());
					   }
					 */
					newFFrameLegend.setNameLayers(nameLayers);
					newFFrameLegend.setAreVisible(areVisible);
					newFFrameLegend.setViewing(cbVisualizacion.getSelectedIndex());
					newFFrameLegend.setQuality(cbCalidad.getSelectedIndex());
					newFFrameLegend.setBoundBox(FLayoutUtilities.toSheetRect(
							rect, m_layout.getLayoutControl().getAT()));
					newFFrameLegend.setRotation(getPRotation().getRotation());

					PluginServices.getMDIManager().closeWindow(FFrameLegendDialog.this);
					//m_layout.refresh();
					isAcepted = true;
				}
				};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newFFrameLegend=null;
					PluginServices.getMDIManager().closeWindow(FFrameLegendDialog.this);
				}
			};
			accept = new AcceptCancelPanel(okAction, cancelAction);
			accept.setPreferredSize(new java.awt.Dimension(300,34));
			accept.setBounds(new java.awt.Rectangle(5,140,200,30));
			//accept.setLocation(5, 200);
		}
		return accept;
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

					if (fframeview == fframelegend.getFFrameDependence()[0]) {
						liVistas.setSelectedIndex(i);
						nameLayers=fframelegend.getNameLayers();
						areVisible=fframelegend.getAreVisible();
						refreshLayerPanel();
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
											refreshLayerPanel();
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
	private void refreshLayerPanel() {
		getJPanel().removeAll();
		if (fframeview==null || fframeview.getMapContext()==null) {
			return;
		}
		FLayers layers=fframeview.getMapContext().getLayers();
		refreshLayers(layers,true);
	}
	private void refreshLayers(FLayers layers,boolean first) {
		boolean clear=false;
		////Comprobar si ha variado
		if (layers.getLayersCount()!=nameLayers.size()) {
			clear=true;
		}else {
			int j=0;
			for (int i=layers.getLayersCount()-1;i>=0;i--) {
				String name=(String)nameLayers.get(j);
				if (!(nameLayers.size()>j && nameLayers.get(j).equals(name))) {
					clear=true;
					break;
				}
				j++;
			}
		}
		////
		if (clear && first) {
			nameLayers.clear();
			areVisible.clear();
		}
		int j=0;
		for (int i=layers.getLayersCount()-1;i>=0;i--) {
			FLayer layer=layers.getLayer(i);
			String name=layer.getName();

			/*if (layer instanceof FLayers) {
				//refreshLayers((FLayers)layer,false);
			}else*/
			{

			if (nameLayers.size()>j && nameLayers.get(j).equals(name)) {
				boolean b=((Boolean)areVisible.get(j)).booleanValue();
				 addLayer(name,j,b);
				 nameLayers.set(j,name);
				 areVisible.set(j,new Boolean(b));
			}else {
				addLayer(name,j,layer.isVisible());
				nameLayers.add(name);
				areVisible.add(new Boolean(layers.getLayer(i).isVisible()));

			}
			}
			j++;

		}
		getJPanel().setSize(new Dimension(100,100));
	}
	private void addLayer(String name,int i,boolean b) {
		JCheckBox chbox=new JCheckBox(name,b);
//		if (nameLayers.size()>i) {
//			nameLayers.set(i,name);
//			areVisible.set(i,new Boolean(b));
//		}else {
//			nameLayers.add(name);
//			areVisible.add(new Boolean(b));
//		}

		chbox.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent arg0) {
				JCheckBox ckb=(JCheckBox)arg0.getSource();
				Component[] components=getJPanel().getComponents();
				int j=0;
				for (int i=components.length-1;i>=0;i--) {
					if (components[i].equals(ckb)) {
						areVisible.set(i,new Boolean(ckb.isSelected()));
					}
					j++;
				}
				//hashVisible.put(ckb.getLabel(),new Boolean(ckb.isSelected()));
			}

		});
		getJPanel().add(name,chbox);
	}
	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"propiedades_marco_leyenda"));

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
	private JButton getBFuente() {
		if (bFuente == null) {
			bFuente = new JButton();
			bFuente.setSize(90, 23);
			bFuente.setText(PluginServices.getText(this, "fuente"));
			bFuente.setLocation(217, 146);
			bFuente.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (font != null) {
							font=FontChooser.showDialog(
									PluginServices.getText(this,
										"seleccion_fuente"),
									font); // fchoser=new FontChooser();
						} else {
							font=FontChooser.showDialog(
									PluginServices.getText(this,
										"seleccion_fuente"), getFont());
						}
					}
				});
		}

		return bFuente;
	}

	/**
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(470, 19, 120, 120);
		}
		return pRotation;
	}

	/**
	 * This method initializes jScrollPane1
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(new java.awt.Rectangle(314,10,137,158));
			jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jScrollPane1.setViewportView(getJPanel());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
		}
		return jPanel;
	}

	public IFFrame getFFrame() {
		return newFFrameLegend;
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"