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
package com.iver.cit.gvsig.project.documents.layout.gui;


import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.ProjectMap;
/**
 * Dialogo con la información de los mapas
 *
 * @author Fernando González Cortés
 */
public class MapProperties extends JPanel implements IWindow {
    private javax.swing.JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="14,17"
    private javax.swing.JLabel jLabel = null;
    private javax.swing.JTextField txtName = null;
    private javax.swing.JLabel jLabel1 = null;
    private javax.swing.JTextField txtDate = null;
    private javax.swing.JLabel jLabel2 = null;
    private javax.swing.JTextField txtOwner = null;
    private javax.swing.JLabel jLabel3 = null;
    private javax.swing.JScrollPane jScrollPane = null;
    private javax.swing.JTextArea txtComments = null;
    private javax.swing.JLabel jLabel4 = null;
    private javax.swing.JTextField txtHorizontal = null;
    private javax.swing.JLabel jLabel5 = null;
    private javax.swing.JTextField txtVertical = null;
    private javax.swing.JCheckBox chkActiveGrid = null;
    private ProjectMap map;
    private javax.swing.JPanel jPanel = null;
    private javax.swing.JPanel jPanel1 = null;
    private javax.swing.JPanel jPanel2 = null;
    private javax.swing.JPanel jPanel3 = null;
    private javax.swing.JPanel jPanel4 = null;
	private javax.swing.JLabel jLabel7 = null;
	private javax.swing.JLabel jLabel8 = null;
	private javax.swing.JCheckBox activeRuler = null;
	private javax.swing.JPanel jPanel5 = null;
	private JCheckBox showGrid = null;
	private JCheckBox chbEditable = null;
	private AcceptCancelPanel accept;
	private boolean isAccepted=false;
	private JPanel jPanel6 = null;
	private JLabel jLabel6 = null;
	private JLabel jLabel9 = null;
	/**
	 * This method initializes
	 *
	 */
	public MapProperties() {
		super();
		initialize();
	}
    /**
     * This is the default constructor
     *
     * @param f frame padre del dialogo
     * @param m mapa cuya información se presenta
     */
    public MapProperties(ProjectMap m) {
        map = m;
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(425, 428);
        this.setPreferredSize(new java.awt.Dimension(410,400));
        this.add(getJContentPane(), null);
		getTxtName().setText(map.getName());
        getTxtDate().setText(map.getCreationDate());
        getTxtOwner().setText(map.getOwner());
        getTxtComments().setText(map.getComment());

        jLabel9.setText(map.getModel().getLayoutContext().getAttributes().getNameUnit());
        jLabel6.setText(map.getModel().getLayoutContext().getAttributes().getNameUnit());

        getTxtHorizontal().setText(Double.toString(map.getModel().getLayoutContext().getAttributes().getNumUnitsX()));
        getTxtVertical().setText(Double.toString(map.getModel().getLayoutContext().getAttributes().getNumUnitsY()));

        getChkActiveGrid().setSelected(map.getModel().getLayoutContext().isAdjustingToGrid());
   		//map.getModel().setProjectMap(map);
	}

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();

            java.awt.FlowLayout layFlowLayout10 = new java.awt.FlowLayout();
            layFlowLayout10.setHgap(0);
            jContentPane.setLayout(layFlowLayout10);
            jContentPane.add(getJPanel(), null);
            jContentPane.add(getJPanel1(), null);
            jContentPane.add(getJPanel2(), null);
            jContentPane.add(getJPanel3(), null);
            jContentPane.setPreferredSize(new java.awt.Dimension(400,410));
            jContentPane.setSize(new java.awt.Dimension(308,375));
            jContentPane.add(getJPanel6(), null);
            jContentPane.add(getJPanel5(), null);
            jContentPane.add(getJPanel4(), null);
            jContentPane.add(getAcceptCancelPanel(), null);
        }

        return jContentPane;
    }

    /**
     * This method initializes jLabel
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setText(PluginServices.getText(this, "nombre") + ":");
            jLabel.setPreferredSize(new java.awt.Dimension(180,20));
            jLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }

        return jLabel;
    }

    /**
     * This method initializes txtName
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtName() {
        if (txtName == null) {
            txtName = new javax.swing.JTextField();
            txtName.setPreferredSize(new java.awt.Dimension(190, 20));
        }

        return txtName;
    }

    /**
     * This method initializes jLabel1
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel1() {
        if (jLabel1 == null) {
            jLabel1 = new javax.swing.JLabel();
            jLabel1.setText(PluginServices.getText(this, "creation_date") + ":");
            jLabel1.setPreferredSize(new java.awt.Dimension(180,20));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }

        return jLabel1;
    }

    /**
     * This method initializes txtDate
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtDate() {
        if (txtDate == null) {
            txtDate = new javax.swing.JTextField();
            txtDate.setPreferredSize(new java.awt.Dimension(190, 20));
            txtDate.setEditable(false);
            txtDate.setBackground(java.awt.Color.white);
        }

        return txtDate;
    }

    /**
     * This method initializes jLabel2
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel2() {
        if (jLabel2 == null) {
            jLabel2 = new javax.swing.JLabel();
            jLabel2.setText(PluginServices.getText(this, "owner") + ":");
            jLabel2.setPreferredSize(new java.awt.Dimension(180,20));
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }

        return jLabel2;
    }

    /**
     * This method initializes txtOwner
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtOwner() {
        if (txtOwner == null) {
            txtOwner = new javax.swing.JTextField();
            txtOwner.setPreferredSize(new java.awt.Dimension(190, 20));
        }

        return txtOwner;
    }

    /**
     * This method initializes jLabel3
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel3() {
        if (jLabel3 == null) {
            jLabel3 = new javax.swing.JLabel();
            jLabel3.setText(PluginServices.getText(this, "comentarios") + ":");
            jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
            jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jLabel3.setPreferredSize(new java.awt.Dimension(180,20));
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }

        return jLabel3;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setPreferredSize(new java.awt.Dimension(190,50));
            jScrollPane.setViewportView(getTxtComments());
        }

        return jScrollPane;
    }

    /**
     * This method initializes txtComments
     *
     * @return javax.swing.JTextArea
     */
    private javax.swing.JTextArea getTxtComments() {
        if (txtComments == null) {
            txtComments = new javax.swing.JTextArea();
        }

        return txtComments;
    }

    /**
     * This method initializes jLabel4
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel4() {
        if (jLabel4 == null) {
            jLabel4 = new javax.swing.JLabel();
            jLabel4.setText(PluginServices.getText(this, "espaciado_horizontal") +
                ":");
            jLabel4.setPreferredSize(new java.awt.Dimension(130,16));
        }

        return jLabel4;
    }

    /**
     * This method initializes txtHorizontal
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtHorizontal() {
        if (txtHorizontal == null) {
            txtHorizontal = new javax.swing.JTextField();
            txtHorizontal.setPreferredSize(new java.awt.Dimension(30,20));
            txtHorizontal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        }

        return txtHorizontal;
    }

    /**
     * This method initializes jLabel5
     *
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel5() {
        if (jLabel5 == null) {
            jLabel5 = new javax.swing.JLabel();
            jLabel5.setText(PluginServices.getText(this, "espaciado_vertical") +
                ":");
            jLabel5.setPreferredSize(new java.awt.Dimension(130,16));
        }

        return jLabel5;
    }

    /**
     * This method initializes txtVertical
     *
     * @return javax.swing.JTextField
     */
    private javax.swing.JTextField getTxtVertical() {
        if (txtVertical == null) {
            txtVertical = new javax.swing.JTextField();
            txtVertical.setPreferredSize(new java.awt.Dimension(30,20));
            txtVertical.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        }

        return txtVertical;
    }

    /**
     * This method initializes chkMalla
     *
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getChkActiveGrid() {
        if (chkActiveGrid == null) {
            chkActiveGrid = new javax.swing.JCheckBox();
            chkActiveGrid.setText(PluginServices.getText(this, "malla_activada"));
            chkActiveGrid.setSelected(true);
            chkActiveGrid.setPreferredSize(new java.awt.Dimension(190,24));
            chkActiveGrid.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
            chkActiveGrid.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            chkActiveGrid.addActionListener(new java.awt.event.ActionListener() {
            	public void actionPerformed(java.awt.event.ActionEvent e) {
            		///map.getModel().setIsCuadricula(chkMalla.isSelected());
            	}
            });
        }

        return chkActiveGrid;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            FlowLayout flowLayout2 = new FlowLayout();
            flowLayout2.setHgap(4);
            flowLayout2.setAlignment(java.awt.FlowLayout.LEFT);
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(4);
            jPanel = new javax.swing.JPanel();

            jPanel.setLayout(flowLayout2);
            jPanel.setPreferredSize(new java.awt.Dimension(190,150));
            jPanel.add(getJLabel(), null);
            jPanel.add(getJLabel1(), null);
            jPanel.add(getJLabel2(), null);
            jPanel.add(getJLabel3(), null);
        }

        return jPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new javax.swing.JPanel();

            java.awt.FlowLayout layFlowLayout12 = new java.awt.FlowLayout();
            layFlowLayout12.setAlignment(java.awt.FlowLayout.LEFT);
            jPanel1.setLayout(layFlowLayout12);
            jPanel1.add(getTxtName(), null);
            jPanel1.add(getTxtDate(), null);
            jPanel1.add(getTxtOwner(), null);
            jPanel1.setPreferredSize(new java.awt.Dimension(200,150));
            jPanel1.add(getJScrollPane(), null);
        }

        return jPanel1;
    }

    /**
     * This method initializes jPanel2
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridLayout gridLayout4 = new GridLayout();
            gridLayout4.setRows(2);
            jPanel2 = new javax.swing.JPanel();

            jPanel2.setLayout(gridLayout4);
            jPanel2.setPreferredSize(new java.awt.Dimension(220,50));
            jPanel2.add(getJLabel4(), null);
            jPanel2.add(getJLabel5(), null);
        }

        return jPanel2;
    }

    /**
     * This method initializes jPanel3
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel3() {
        if (jPanel3 == null) {
            GridLayout gridLayout3 = new GridLayout();
            gridLayout3.setRows(2);
            GridLayout gridLayout2 = new GridLayout();
            gridLayout2.setRows(2);
            GridLayout gridLayout1 = new GridLayout();
            gridLayout1.setRows(2);
            jPanel3 = new javax.swing.JPanel();

            jPanel3.setLayout(gridLayout3);
            jPanel3.setPreferredSize(new java.awt.Dimension(40,50));
            jPanel3.add(getTxtHorizontal(), null);
            jPanel3.add(getTxtVertical(), null);
        }

        return jPanel3;
    }

    /**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this, "propiedades_mapa"));
		return m_viewinfo;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}
	/**
	 * This method initializes jPanel4
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel4() {
		if(jPanel4 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel4 = new javax.swing.JPanel();
			jPanel4.setLayout(flowLayout);
			jPanel4.setPreferredSize(new java.awt.Dimension(390,24));
			jPanel4.add(getJLabel7(), null);
			jPanel4.add(getJLabel8(), null);
		}
		return jPanel4;
	}
	/**
	 * This method initializes jLabel7
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel7() {
		if(jLabel7 == null) {
			jLabel7 = new javax.swing.JLabel();
			jLabel7.setText(PluginServices.getText(this, "map_units")+ ": ");
		}
		return jLabel7;
	}
	/**
	 * This method initializes jLabel8
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel8() {
		if(jLabel8 == null) {
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setText(PluginServices.getText(this,map.getModel().getLayoutContext().getAttributes().getNameUnit()));
		}
		return jLabel8;
	}
	/**
	 * This method initializes jCheckBox
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbActiveRuler() {
		if(activeRuler == null) {
			activeRuler = new javax.swing.JCheckBox();
			activeRuler.setText(PluginServices.getText(this,"activar_regla"));
			activeRuler.setPreferredSize(new java.awt.Dimension(190,24));
			activeRuler.setSelected(map.getModel().getLayoutContext().getRuler());
			activeRuler.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//map.getModel().setRuler(jCheckBox.isSelected());
				}
			});
		}
		return activeRuler;
	}
	/**
	 * This method initializes jPanel5
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel5() {
		if(jPanel5 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
			jPanel5 = new javax.swing.JPanel();
			jPanel5.setLayout(flowLayout1);
			jPanel5.setPreferredSize(new java.awt.Dimension(390,120));
			jPanel5.add(getChkActiveGrid(), null);
			jPanel5.add(getChbActiveRuler(), null);
			jPanel5.add(getChbShowGrid(), null);
			jPanel5.add(getChbEditable(), null);
		}
		return jPanel5;
	}
	/**
	 * This method initializes jCheckBox1
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbShowGrid() {
		if (showGrid == null) {
			showGrid = new JCheckBox();
			showGrid.setSelected(map.getModel().getLayoutContext().isGridVisible());
			showGrid.setPreferredSize(new java.awt.Dimension(190,24));
			showGrid.setText(PluginServices.getText(this,"visualizar_cuadricula"));
			showGrid.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

				}
			});
		}
		return showGrid;
	}
    /**
     * @see com.iver.andami.ui.mdiManager.SingletonWindow#getWindowModel()
     */
    public Object getViewModel() {
        return map;
    }
	/**
	 * This method initializes chbEditable
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getChbEditable() {
		if (chbEditable == null) {
			chbEditable = new JCheckBox();
			chbEditable.setSelected(map.getModel().getLayoutContext().isEditable());
			chbEditable.setPreferredSize(new java.awt.Dimension(190,24));
			chbEditable.setText(PluginServices.getText(this,"editable"));
		}
		return chbEditable;
	}
	private AcceptCancelPanel getAcceptCancelPanel() {
		if (accept == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					map.setName(txtName.getText());
                    map.setOwner(txtOwner.getText());
                    map.setComment(txtComments.getText());
                    map.getModel().getLayoutContext().setAdjustToGrid(chkActiveGrid.isSelected());
                    map.getModel().getLayoutContext().setGridVisible(showGrid.isSelected());
                 	map.getModel().getLayoutContext().setRuler(activeRuler.isSelected());
                    map.getModel().getLayoutContext().setEditable(chbEditable.isSelected());
                 	try {
                     	String sh=txtHorizontal.getText();
                     	sh=sh.replace(',','.');
                     	if (sh.length()!=0){
                         	double f = new Double(sh).doubleValue();
								map.getModel().getLayoutContext().getAttributes().setNumUnitsX(f);
                     	}

							String sv=txtVertical.getText();
							sv=sv.replace(',','.');
							if (sv.length()!=0){
								double f = new Double(sv).doubleValue();
								map.getModel().getLayoutContext().getAttributes().setNumUnitsY(f);
							}

							PluginServices.getMDIManager().closeWindow(MapProperties.this);
							map.getModel().getLayoutContext().setGridVisible(getChbShowGrid().isSelected());
							IWindow win=PluginServices.getMDIManager().getActiveWindow();
							if (win instanceof Layout) {
								((Layout)win).getLayoutControl().refresh();
							}
                     } catch (NumberFormatException ex) {
                         NotificationManager.addInfo("no es un double", ex);
                     }
                     isAccepted=true;
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					isAccepted=false;
					PluginServices.getMDIManager().closeWindow(MapProperties.this);
				}
			};
			accept = new AcceptCancelPanel(okAction, cancelAction);
			accept.setPreferredSize(new java.awt.Dimension(300,34));
		}
		return accept;
	}
	public boolean isAccepted() {
		return isAccepted;
	}
	/**
	 * This method initializes jPanel6
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jLabel9 = new JLabel();
			jLabel9.setPreferredSize(new java.awt.Dimension(130,20));
			jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
			jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel9.setText("");
			GridLayout gridLayout5 = new GridLayout();
			gridLayout5.setRows(2);
			gridLayout5.setColumns(1);
			jLabel6 = new JLabel();
			jLabel6.setPreferredSize(new java.awt.Dimension(130,20));
			jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel6.setText("");
			jPanel6 = new JPanel();
			jPanel6.setPreferredSize(new java.awt.Dimension(130,50));
			jPanel6.setLayout(gridLayout5);
			jPanel6.add(jLabel9, null);
			jPanel6.add(jLabel6, null);
		}
		return jPanel6;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

  }  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"


//  @jve:visual-info  decl-index=0 visual-constraint="10,10"
