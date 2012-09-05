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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.gui.styling.SLDListBoxCellRenderer;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameNorth;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.ListViewModel;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo para añadir un gráfico para señalizar el norte.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameNorthDialog extends JPanel implements IFFrameDialog {
    private JScrollPane scrollImages = null;
    private JPanel pImages = null;
    private Layout layout;
    private FFrameNorth fframenorth;
    private JList listNorth = null;
    private File m_file = null;
    private Rectangle2D rect = new Rectangle2D.Double();
    private boolean isAcepted = false;
    private JScrollPane scrollViews = null;
    private JList listViews = null;
    private FFrameView fframeview = null;
    private JLabel lblView = null;
    private AcceptCancelPanel acceptCancel = null;
    private FFrameView fframeDependence;
    private FFrameNorth newFFrameNorth;
    private String path;



    /**
     * This is the default constructor
     *
     * @param l DOCUMENT ME!
     * @param f DOCUMENT ME!
     */
    public FFrameNorthDialog(Layout l, FFrameNorth f) {
        layout = l;
        fframenorth = f;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        lblView = new JLabel();
        lblView.setBounds(new java.awt.Rectangle(5, 193, 325, 18));
        lblView.setText(PluginServices.getText(this, "marco_vista"));
        this.setLayout(null);
        this.setSize(370, 335);
        this.add(getScrollImages(), null);

        this.add(getAcceptCancel());

        this.add(getScrollViews(), null);
        this.add(lblView, null);
    }

    private AcceptCancelPanel getAcceptCancel(){
    	if (this.acceptCancel == null ){
    		this.acceptCancel = new AcceptCancelPanel(
    				new java.awt.event.ActionListener() { //Accept

						public void actionPerformed(java.awt.event.ActionEvent e) {
							newFFrameNorth=(FFrameNorth)fframenorth.cloneFFrame(layout);
							if (m_file != null) {

    							Dimension dimension = null;

    							try {
    								dimension = fframenorth.getBound(m_file.getAbsolutePath());
    							} catch (Exception ex) {
    								NotificationManager.addError("Excepción :", ex);
    							}
    							newFFrameNorth.setFFrameDependence(fframeDependence);
    							newFFrameNorth.setPath(path);
    							newFFrameNorth.setViewing(0);
    							newFFrameNorth.setQuality(0);
    							newFFrameNorth.setName(m_file.getName());
    							newFFrameNorth.load(path);

    							// Ajustamos la relación de aspecto a la altura.
    							double ratio = (float) (dimension.getWidth()) / (float) (dimension.getHeight());

    							double newWidth = rect.getHeight() * ratio;
    							Rectangle2D.Double rAdjust = new Rectangle2D.Double(rect.getMinX(),
    									rect.getMinY(), newWidth, rect.getHeight());
    							rect = rAdjust;
    						}
    						newFFrameNorth.setBoundBox(FLayoutUtilities.toSheetRect(
    								rect, layout.getLayoutControl().getAT()));
    						newFFrameNorth.setRotation(0);
    						PluginServices.getMDIManager().closeWindow(FFrameNorthDialog.this);
    						//layout.refresh();
    						isAcepted = true;
    					}
    				},
    				new java.awt.event.ActionListener() { //Cancel
    					public void actionPerformed(java.awt.event.ActionEvent e) {
    						PluginServices.getMDIManager().closeWindow(FFrameNorthDialog.this);
    					}
    				}

    		);
    		this.acceptCancel.setBounds(3, 290, 357, 30);

    	}
    	return this.acceptCancel;
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewinfo.setTitle(PluginServices.getText(this,
                "propiedades_marco_imagenes"));

        return m_viewinfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getIsAcepted() {
        return isAcepted;
    }

    /**
     * This method initializes scrollImages
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollImages() {
        if (scrollImages == null) {
            scrollImages = new JScrollPane();
            scrollImages.setBounds(new java.awt.Rectangle(3, 3, 357, 174));
            scrollImages.setBackground(java.awt.Color.white);
            scrollImages.setViewportView(getPImages());
        }

        return scrollImages;
    }

    /**
     * This method initializes pImages
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPImages() {
        if (pImages == null) {
            pImages = new JPanel();
            pImages.setBackground(java.awt.Color.white);
            pImages.add(getListNorth(), null);
        }

        return pImages;
    }

    /**
     * This method initializes listNorth
     *
     * @return javax.swing.JList
     */
    private JList getListNorth() {
        if (listNorth == null) {
            listNorth = new JList();

            listNorth.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            listNorth.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            listNorth.setVisibleRowCount(-1);

            File file = new File(AddLayer.class.getClassLoader()
                                               .getResource("northimages")
                                               .getFile());
            listNorth.setSize(new java.awt.Dimension(300, 242));
            listNorth.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);

            File[] files = file.listFiles();
            DefaultListModel listModel = new DefaultListModel();

            for (int i = 0; i < files.length; i++) {
                listModel.addElement(files[i].getAbsolutePath());
            }

            listNorth.setModel(listModel);

            SLDListBoxCellRenderer renderer = new SLDListBoxCellRenderer();
            renderer.setPreferredSize(new Dimension(60, 50));
            listNorth.setCellRenderer(renderer);
            if (fframenorth.getName()!=null){
            	for (int i=0;i<listNorth.getModel().getSize();i++){
            		if (((String)listNorth.getModel().getElementAt(i)).endsWith(fframenorth.getName())){
            			listNorth.setSelectedIndex(i);
            			m_file = new File((String) listNorth.getSelectedValue());
            		}
            	}

            }

            listNorth.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

					public void valueChanged(
                        javax.swing.event.ListSelectionEvent e) {
                        m_file = new File((String) ((JList) e.getSource()).getSelectedValue());
                        path=m_file.getAbsolutePath();
                    }
                });
        }

        return listNorth;
    }

    /**
     * This method initializes scrollViews
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getScrollViews() {
        if (scrollViews == null) {
            scrollViews = new JScrollPane();
            scrollViews.setBackground(Color.white);
            scrollViews.setBounds(new java.awt.Rectangle(3, 212, 357, 65));
            scrollViews.setViewportView(getListViews());
        }

        return scrollViews;
    }

    /**
     * This method initializes listViews
     *
     * @return javax.swing.JList
     */
    private JList getListViews() {
        if (listViews == null) {
            ListViewModel listmodel = new ListViewModel();
            listmodel.addViews(layout);
            listViews = new javax.swing.JList();
            listViews.setModel(listmodel);

            for (int i = 0; i < listViews.getModel().getSize(); i++) {
                if (fframenorth.getFFrameDependence() != null) {
                    fframeview = (FFrameView) listViews.getModel().getElementAt(i);

                    if (fframeview == fframenorth.getFFrameDependence()[0]) {
                        listViews.setSelectedIndex(i);
                    }
                }
            }

            listViews.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    private int selectIndex=-1;
					public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
            			IFFrame[] fframes=layout.getLayoutContext().getFFrames();
							int selectInt = ((JList) e.getSource())
									.getSelectedIndex();
							if (selectInt != selectIndex) {
								selectIndex = selectInt;
								if (selectIndex == -1) {
									return;
								}
								fframeview = (FFrameView) listViews.getModel()
										.getElementAt(selectInt);

								for (int i = 0; i < fframes.length; i++) {
									IFFrame f =fframes[i];

									if (f instanceof FFrameView) {
										if (((FFrameView) f).getView() == fframeview
												.getView()) {
											fframeDependence=fframeview;
//											fframenorth
//													.setFFrameDependence(fframeview);
										}
									}
								}
							}
						}
					});
		}

        return listViews;
    }

	public IFFrame getFFrame() {
		return newFFrameNorth;
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
