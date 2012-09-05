/*
 * Created on 31-ene-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemStoreParameters;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.type.GeometryType;
import org.gvsig.fmap.geom.type.GeometryTypeNotSupportedException;
import org.gvsig.fmap.geom.type.GeometryTypeNotValidException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLyrDefault;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;
import org.gvsig.tools.locator.LocatorException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.legend.CreateSpatialIndexMonitorableTask;
import com.iver.utiles.swing.threads.IMonitorableTask;



/**
 * This class implements an useful and intuitive graphic interface to change some
 * properties of the layer. This class extends AbstractThemeManager. The properties
 * that allow modified are the name of the layer, the scale, if the user want to use
 * Spatial index or not and the HyperLink. Also shows a scroll with a little resume
 * with the properties of the layer.
 * @author jmorell
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class General extends AbstractThemeManagerPage {

	private static final long serialVersionUID = 1L;
    private FLayer layer;
	private IProjectView view;
	private NumberFormat nf = NumberFormat.getInstance();
	private JPanel pnlLayerName = null;
	private GridBagLayoutPanel pnlScale = null;
	private JPanel pnlProperties = null;
	private JLabel lblLayerName = null;
	private JTextField txtLayerName = null;
	private JTextField txtMaxScale = null;
	private JTextArea propertiesTextArea = null;
	private JRadioButton rdBtnShowAlways = null;
	private JRadioButton rdBtnDoNotShow = null;
	private JTextField txtMinScale = null;
	private JComboBox cmbLinkField = null;
    private JTextField txtLinkExtension = null;
    private JComboBox cmbLinkType = null;
    private JCheckBox jCheckBoxSpatialIndex = null;
	private JPanel pnlHyperLink;
	private JLabel lblLinkExtension;
	private JLabel lblLinkField;
	private JLabel lblDefaultAction;
	private JPanel pnlFieldAndExtension;
	private JPanel pnlHyperLinkAction;
	private JScrollPane scrlProperties;

    /**
	 * This is the default constructor.
	 */
	public General() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private  void initialize() {
		this.setLayout(new BorderLayout());
		GridBagLayoutPanel aux = new GridBagLayoutPanel();
		aux.addComponent(getPnlLayerName());
		aux.addComponent(new JBlank(10, 10));
		aux.addComponent(getJCheckBoxSpatialIndex());
		aux.addComponent("", getPnlScale());
		JPanel aux2 = new JPanel(new GridLayout(1,2));
		aux2.add(getPnlProperties());
		aux2.add(getPnlHyperLink());
		aux.addComponent(aux2);



		aux.setPreferredSize(getPreferredSize());
		this.add(aux, BorderLayout.CENTER);
		this.add(new JBlank(5, 10), BorderLayout.WEST);
		this.add(new JBlank(5, 10), BorderLayout.EAST);

	}


	/**
	 * Sets the necessary properties in the panel. This properties are
	 * extracted from the layer. With this properties fills the TextFields,
	 * ComboBoxes and the rest of GUI components.
	 * @param FLayer layer,
	 */
	public void setModel(FLayer layer) {
		this.layer = layer;

        if (layer instanceof FLyrVect) {
            FLyrVect lyrVect = (FLyrVect) layer;


            //TODO
//            if(lyrVect.getISpatialIndex() == null) {
//                getJCheckBoxSpatialIndex().setSelected(false);
//            } else {
//                getJCheckBoxSpatialIndex().setSelected(true);
//            }
        }
		if (layer.getMinScale() != -1) {
			getTxtMaxScale().setText(nf.format(layer.getMinScale()));
		}
		if (layer.getMaxScale() != -1) {
			getTxtMinScale().setText(nf.format(layer.getMaxScale()));
		}
		if (layer.getMinScale() == -1 && layer.getMaxScale() == -1) {
			getRdBtnShowAlways().setSelected(true);
			txtMaxScale.setEnabled(false);
			txtMinScale.setEnabled(false);

		} else {
			getRdBtnDoNotShowWhen().setSelected(true);
			txtMaxScale.setEnabled(true);
			txtMinScale.setEnabled(true);
		}
		txtLayerName.setText(layer.getName());
		showLayerInfo();


		if (PluginServices.getMainFrame() != null) {
			if  (PluginServices.getMDIManager().getActiveWindow() instanceof BaseView ){
			    BaseView theView = (BaseView)PluginServices.getMDIManager().getActiveWindow();
			    view = theView.getModel();
			    try {
			        if (layer instanceof FLyrVect) {
			            FLyrVect ad = (FLyrVect) layer;
			            FeatureStore fs;
			            fs = ad.getFeatureStore();
			            FeatureType ftype = fs.getDefaultFeatureType();
			            String[] names = new String[ftype.size()];
			            Iterator iter = ftype.iterator();
			            int i=0;
			            while (iter.hasNext()){
			                names[i] = ((FeatureAttributeDescriptor)iter.next()).getName();
			                i++;
			            }
			            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(names);
			            cmbLinkField.setModel(defaultModel);

			            if (layer.getLinkProperties().getField()!=null) {
							cmbLinkField.setSelectedItem(layer.getLinkProperties().getField());
						} else {
							cmbLinkField.setSelectedItem(view.getSelectedField());
						}
			        } else {
			            cmbLinkField = new JComboBox();
			        }
			     } catch (DataException e) {
			        NotificationManager.addError("No se pudo obtener la tabla", e);
			    }


			    if(layer.getLinkProperties().getExt()!=null) {
					getTxtLinkExtension().setText(layer.getLinkProperties().getExt());
				}

			    getCmbLinkType().addItem(PluginServices.getText(this,
			       	"Enlazar_a_ficheros_de_imagen"));
			    getCmbLinkType().addItem(PluginServices.getText(this,
			    	"Enlazar_a_fichero_de_texto"));

			    getCmbLinkType().addItem(PluginServices.getText(this,
			    	"Enlazar_a_documento_PDF"));
			    getCmbLinkType().addItem(PluginServices.getText(this,
					"Enlazar_a_imagen_SVG"));

			    if (layer.getLinkProperties().getType()!=-1) {
					getCmbLinkType().setSelectedIndex(layer.getLinkProperties().getType());
				}

			}
		}

	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlLayerName() {
		if (pnlLayerName == null) {
			lblLayerName = new JLabel();
			pnlLayerName = new JPanel();
			lblLayerName.setText(PluginServices.getText(this,"Nombre") + ":");
			lblLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
			pnlLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
			pnlLayerName.add(lblLayerName, null);
			pnlLayerName.add(getTxtLayerName(), null);
		}
		return pnlLayerName;
	}
	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private GridBagLayoutPanel getPnlScale() {
		if (pnlScale == null) {
			pnlScale = new GridBagLayoutPanel();
			pnlScale.setBorder(BorderFactory.createTitledBorder(
    				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					PluginServices.getText(this, "rango_de_escalas"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null)
				);
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(getRdBtnShowAlways());
			buttonGroup.add(getRdBtnDoNotShowWhen());
			pnlScale.addComponent(getRdBtnShowAlways());
			pnlScale.addComponent(getRdBtnDoNotShowWhen());
			JPanel aux;


			aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
			aux.add(getTxtMaxScale());
			aux.add(new JLabel("(" + PluginServices.getText(this,"escala_maxima") + ")"));

			GridBagLayoutPanel aux2;
			aux2 = new GridBagLayoutPanel();
			aux2.addComponent(PluginServices.getText(
					this,"este_por_encima_de")+" 1:",
					aux);
			aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
			aux.add(getTxtMinScale());
			aux.add(new JLabel("(" + PluginServices.getText(this,"escala_minima") + ")"));

			aux2.addComponent(PluginServices.getText(
					this,"este_por_debajo_de_")+" 1:",
					aux);

			pnlScale.addComponent(new JBlank(20, 1), aux2);

			pnlScale.addComponent(new JBlank(20, 1), aux2);

		}
		return pnlScale;
	}
	/**
	 * This method initializes jPanel2, this contains the ScrollPane with the
	 * properies.
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlProperties() {
		if (pnlProperties == null) {
			pnlProperties = new JPanel();
			pnlProperties.setBorder(BorderFactory.createTitledBorder(
    				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					PluginServices.getText(this, "propiedades"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, null)
				);
			pnlProperties.add(getScrlProperties(), null);
		}
		return pnlProperties;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtLayerName() {
		if (txtLayerName == null) {
			txtLayerName = new JTextField(25);
			txtLayerName.setEditable(true);
		}
		return txtLayerName;
	}

	/**
	 * This method initializes TxtMaxScale
	 * @return jTextField1
	 */
	private JTextField getTxtMaxScale() {
		if (txtMaxScale == null) {
			txtMaxScale = new JTextField(15);
			txtMaxScale.setEnabled(false);
		}
		return txtMaxScale;
	}
	/**
	 * This method initilizes TxtArea, in this TextArea sets the text with
	 * the properties of the layer
	 * @return
	 */
	private JTextArea getPropertiesTextArea() {
		if (propertiesTextArea == null) {
			propertiesTextArea = new JTextArea();
			propertiesTextArea.setEditable(false);
			propertiesTextArea.setBackground(SystemColor.control);

		}
		return propertiesTextArea;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrlProperties() {
		if (scrlProperties == null) {
			scrlProperties = new JScrollPane();
			scrlProperties.setViewportView(getPropertiesTextArea());
			scrlProperties.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			scrlProperties.setPreferredSize(new Dimension(350, 200));
		}
		return scrlProperties;
	}
	/**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnShowAlways() {
		if (rdBtnShowAlways == null) {
			rdBtnShowAlways = new JRadioButton();
			rdBtnShowAlways.setText(PluginServices.getText(this,"Mostrar_siempre"));
			rdBtnShowAlways.setSelected(true);
			rdBtnShowAlways.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtMaxScale.setEnabled(false);
					txtMinScale.setEnabled(false);
				}
			});
		}
		return rdBtnShowAlways;
	}
	/**
	 * This method initializes jRadioButton1
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRdBtnDoNotShowWhen() {
		if (rdBtnDoNotShow == null) {
			rdBtnDoNotShow = new JRadioButton();
			rdBtnDoNotShow.setText(PluginServices.getText(this,"No_mostrar_la_capa_cuando_la_escala"));
			rdBtnDoNotShow.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					txtMaxScale.setEnabled(true);
					txtMinScale.setEnabled(true);
				}
			});
		}
		return rdBtnDoNotShow;
	}
	/**
	 * This method initializes jTextField2
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtMinScale() {
		if (txtMinScale == null) {
			txtMinScale = new JTextField(15);
			txtMinScale.setEnabled(false);
		}
		return txtMinScale;
	}

	private String getLayerName(){
		return txtLayerName.getText().toString();
	}
    /**
     * This method initializes jPanel3, this panel contains the components of the
     * HyperLink
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlHyperLink() {
    	if (pnlHyperLink == null) {
    		pnlHyperLink = new JPanel();
    		pnlHyperLink.setBorder(BorderFactory.createTitledBorder(
    				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
    					PluginServices.getText(this, "Hiperenlace"),
    					TitledBorder.DEFAULT_JUSTIFICATION,
    					TitledBorder.DEFAULT_POSITION, null, null)
    				);
    		JPanel aux = new JPanel(new BorderLayout());
    		pnlHyperLink.setLayout(new BorderLayout());
    		aux.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    		aux.add(getPnlFieldAndExtension(), BorderLayout.NORTH);
    		aux.add(getPnlHyperLinkAction(), BorderLayout.CENTER);
    		pnlHyperLink.add(aux);
    	}
    	return pnlHyperLink;
    }


    private JPanel getPnlFieldAndExtension() {
    	if (pnlFieldAndExtension == null) {
    		lblLinkExtension = new JLabel();
    		lblLinkExtension.setText(" \t \t"+PluginServices.getText(this,"extension"));
    		lblLinkField = new JLabel();
    		lblLinkField.setText(PluginServices.getText(this, "Campo"));
    		pnlFieldAndExtension = new JPanel();
    		pnlFieldAndExtension.add(lblLinkField, null);
    		pnlFieldAndExtension.add(getCmbLinkField(), null);
    		pnlFieldAndExtension.add(lblLinkExtension, null);
    		pnlFieldAndExtension.add(getTxtLinkExtension(), null);
    	}
    	return pnlFieldAndExtension;
    }
    /**
     * This method initializes jPanel8. This panel contains the ComboBox to select
     * the action, (type of HyperLink)
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlHyperLinkAction() {
    	if (pnlHyperLinkAction == null) {
    		lblDefaultAction = new JLabel();
    		lblDefaultAction.setText(PluginServices.getText(this, "Accion_Predefinida") + "  ");
    		pnlHyperLinkAction = new JPanel();
    		pnlHyperLinkAction.add(lblDefaultAction, null);
    		pnlHyperLinkAction.add(getCmbLinkType(), null);
    	}
    	return pnlHyperLinkAction;
    }
    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getCmbLinkField() {
    	if (cmbLinkField == null) {
            cmbLinkField = new JComboBox();
    	}
    	return cmbLinkField;
    }
    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtLinkExtension() {
    	if (txtLinkExtension == null) {
    		txtLinkExtension = new JTextField();
            txtLinkExtension.setPreferredSize(new Dimension(40,20));
    	}
    	return txtLinkExtension;
    }
    /**
     * This method initializes jComboBox1
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getCmbLinkType() {
    	if (cmbLinkType == null) {
    		cmbLinkType = new JComboBox();
    	}
    	return cmbLinkType;
    }
    /**
     * @return Returns the view.
     */
    private IProjectView getView() {
        return view;
    }
    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBoxSpatialIndex() {
    	if (jCheckBoxSpatialIndex == null) {
    		jCheckBoxSpatialIndex = new JCheckBox();
    		jCheckBoxSpatialIndex.setBounds(2, 33, 242, 23);
    		jCheckBoxSpatialIndex.setText(PluginServices.getText(this,"Usar_indice_espacial"));
    	}
    	return jCheckBoxSpatialIndex;
    }

    /**
     * Returns the selected Type in the ComboBox
     */
    private int getLinkType() {
        return getCmbLinkType().getSelectedIndex();

    }

    /**
     * Returns the Selected Field in the ComboBox
     */
    private String getSelectedLinkField() {
        return (String) getCmbLinkField().getSelectedItem();
    }

    /**
     * Returns the Extension in the TextField
     */
    private String getExtensionLink() {
        return getTxtLinkExtension().getText();
    }

    /**
     * Returns true or false if the CheckBox is marked or not
     */
    private boolean isSpatialIndexSelected() {
        return getJCheckBoxSpatialIndex().isSelected();
    }

    /**
     * Add the information of the layer to the textArea
     */
    private void showLayerInfo() {
		try {
			String info = ((FLyrDefault)layer).getInfoString();
			if (info == null) {
				StringBuffer buff= new StringBuffer();
				Envelope fullExtentViewPort = layer.getFullEnvelope();
				IProjection viewPortProj = layer.getMapContext().getProjection();
				buff.append(PluginServices.getText(this,"Extent"));
				buff.append(" ");
				buff.append(viewPortProj.getAbrev());
				buff.append(" (" + PluginServices.getText(this, "view_projection") + "):\n\t");
				buff.append(PluginServices.getText(this,"Superior") + ":\t" + fullExtentViewPort.getMaximum(1) + "\n\t");
				buff.append(PluginServices.getText(this,"Inferior") + ":\t" + fullExtentViewPort.getMinimum(1) + "\n\t");
				buff.append(PluginServices.getText(this,"Izquierda") + ":\t" + fullExtentViewPort.getMinimum(0) + "\n\t");
				buff.append(PluginServices.getText(this,"Derecha") + ":\t" + fullExtentViewPort.getMaximum(0) + "\n\n");
				// show layer native projection
				if (layer.getProjection() != null
						&& !layer.getProjection().getAbrev().equals(
								viewPortProj.getAbrev())) {
					IProjection nativeLayerProj = layer.getProjection();
					ICoordTrans ct = viewPortProj.getCT(nativeLayerProj);
//					Rectangle2D r=new Rectangle2D.Double(fullExtentViewPort.getMinimum(0),fullExtentViewPort.getMinimum(1),fullExtentViewPort.getLength(0),fullExtentViewPort.getLength(1));
//					Rectangle2D nativeLayerExtent = ct.convert(r);
					Envelope nativeLayerExtent = fullExtentViewPort.convert(ct);
					buff.append(PluginServices.getText(this,"Extent") + " ");
					buff.append(nativeLayerProj.getAbrev());
					buff.append(" (" + PluginServices.getText(this, "layer_native_projection") + "):\n\t");
					buff.append(PluginServices.getText(this, "Superior")
							+ ":\t" + nativeLayerExtent.getMaximum(1) + "\n\t");
					buff.append(PluginServices.getText(this, "Inferior")
							+ ":\t" + nativeLayerExtent.getMinimum(1) + "\n\t");
					buff.append(PluginServices.getText(this, "Izquierda")
							+ ":\t" + nativeLayerExtent.getMinimum(0) + "\n\t");
					buff.append(PluginServices.getText(this, "Derecha") + ":\t"
							+ nativeLayerExtent.getMaximum(0) + "\n\n");

				}
				if (layer instanceof FLyrVect) {
					FeatureStore fStore=((FLyrVect)layer).getFeatureStore();
//					if (rv instanceof VectorialEditableAdapter) {
//						rv=((VectorialEditableAdapter) ((FLyrVect)layer).getSource()).getOriginalAdapter();
//					}

					buff.append(PluginServices.getText(this,"Origen_de_datos") + ": ");
					buff.append(fStore.getName());
					info=buff.toString();

					DataStoreParameters parameters=fStore.getParameters();
					if (parameters instanceof FilesystemStoreParameters) {
						info = info  + "\n" +
						PluginServices.getText(this, "fichero")
								+ ": "
								+ ((FilesystemStoreParameters) parameters)
										.getFile();
//					FIXME
//					}else if (parameters instanceof DBParameters){
//						info = info + "\n" + fStore.getName() + "\n";
//
//						try {
//							info = info +
//							PluginServices.getText(this,"url") +": " + ((DBParameters)parameters).getHost() + "\n";
//						} catch (Exception e) {
//							//TODO: Que hacer aqui?
//							e.printStackTrace();
//						}
//
//						info = info +
//						PluginServices.getText(this, "Tabla")
//								+ ": "
//								+ ((DBStoreParameters) parameters)
//										.getTableName() + "\n";
					}else{
						info = info + "\n" + fStore.getName() + "\n";
					}
//					if (rv instanceof VectorialFileAdapter) {
//						info = info + "\n" + rv.getDriver().getName() + "\n" +
//						PluginServices.getText(this,"fichero") +": " + ((VectorialFileAdapter)rv).getFile();
//
//					} else if (rv instanceof VectorialDBAdapter) {
//						DBLayerDefinition dbdef = ((VectorialDBAdapter) rv).getLyrDef();
//						info = info + "\n" + rv.getDriver().getName() + "\n";
//
//						try {
//							info = info +
//							PluginServices.getText(this,"url") +": " + dbdef.getConnection().getURL() + "\n";
//						} catch (Exception e) {
//							//TODO: Que hacer aqui?
//							e.printStackTrace();
//						}
//
//						info = info +
//						PluginServices.getText(this,"Tabla") +": " + dbdef.getTableName() + "\n";
//					} else if (rv instanceof VectorialAdapter){
//						info = info + "\n" + rv.getDriver().getName() + "\n";
//					}
					String sGeomType= "Unknow";

					try {
						GeometryType geomType = ((FLyrVect)layer).getTypeVectorLayer();
						sGeomType = geomType.getName();
					} catch (LocatorException e) {
						NotificationManager.addError(e);
					} catch (GeometryTypeNotSupportedException e) {
						NotificationManager.showMessageWarning("Not supported GeometryType", e);
					} catch (GeometryTypeNotValidException e) {
						NotificationManager.showMessageWarning("Not valid GeometryType", e);
					}

					info += "\n" + PluginServices.getText(this,"type")+ ": "+sGeomType + "\n";

				} else {
					info=buff.toString();
					info = info + PluginServices.getText(this,"Origen_de_datos") + ": " + layer.getName();
				}

			}
			getPropertiesTextArea().setText(info);

		} catch (ReadException e) {
			NotificationManager.addError(e.getMessage(), e);
		} catch (DataException e) {
			NotificationManager.addError(e.getMessage(), e);
		}

    }

    /**
     * Returns true or false if the scale is activa
     */
	private boolean isScaleActive() {
		return getRdBtnDoNotShowWhen().isSelected();
	}

    /**
     * Instanciates a ITask (@see com.iver.utiles.swing.threads.ITask)
     * to create a spatial index for the selected layer in background.
     * This task also allow to monitor process evolution, and to cancel
     * this process.
     * @throws DriverException
     * @throws DriverIOException
     */
    private IMonitorableTask getCreateSpatialIndexTask() throws DataException {
    	// FIXME REVISAR ESTO (Quizas lanzar TaskException)
    	return new CreateSpatialIndexMonitorableTask((FLyrVect)layer);
    }

	public void acceptAction() {
	     // Implementación para hacer funcionar el hyperlink ...
        //com.iver.cit.gvsig.gui.View theView = (com.iver.cit.gvsig.gui.View)PluginServices.getMDIManager().getActiveView();
        //ProjectView view = theView.getModel();
    	if (PluginServices.getMainFrame() != null)
    	{
    		IProjectView view = getView();
    		view.setTypeLink(getLinkType());
    		if (getSelectedLinkField()!=null)
    		{
    			view.setSelectedField(getSelectedLinkField().toString().trim());
    			view.setExtLink(getExtensionLink());

    		}
    	}


	}

	public void cancelAction() {
		// does nothing
	}

	/**
	 * When we press the apply button, sets the new properties of the layer thar the
	 * user modified using the UI components
	 */
	public void applyAction() {
		if (isScaleActive()) {
			try	{
				layer.setMinScale((nf.parse(getTxtMaxScale().getText())).doubleValue());
			} catch (ParseException ex)	{
			    if (getTxtMaxScale().getText().compareTo("") == 0) {
					layer.setMinScale(-1);
				} else {
					System.err.print(ex.getLocalizedMessage());
				}
			}

			try	{
			    layer.setMaxScale((nf.parse(getTxtMinScale().getText())).doubleValue());
			} catch (ParseException ex)	{
			    if (getTxtMinScale().getText().compareTo("") == 0) {
					layer.setMaxScale(-1);
				} else {
					System.err.print(ex.getLocalizedMessage());
				}
			}

		} else {
	        layer.setMinScale(-1);
	        layer.setMaxScale(-1);
		}

		if (!getLayerName().equals(layer.getName())){
			layer.setName(getLayerName());
		}

        if (layer instanceof FLyrVect){
            FLyrVect lyrVect = (FLyrVect) layer;
            if (isSpatialIndexSelected()) {
        		// TODO

//            	if(lyrVect.getISpatialIndex() == null) {
//                	//AZABALA
//                	try {
//						PluginServices.
//							cancelableBackgroundExecution(getCreateSpatialIndexTask());
//					} catch (ReadDriverException e) {
//						// TODO Auto-generated catch block
//						NotificationManager.addError(e.getMessage(), e);
//					}
//                }
            }
            //AZABALA
            /*
             * If we unselect the spatial index checkbox...Must we delete
             * spatial index file, or we only have to put Spatial Index
             * reference to null?. I have followed the second choice
             */
            else{
//                lyrVect.deleteSpatialIndex();
            }


        }
        //Codigo relacionado
        if (layer instanceof FLyrVect ){
			FLyrVect vectlyr=(FLyrVect) layer;
			if (getSelectedLinkField()!=null){
				vectlyr.getLinkProperties().setExt(getExtensionLink());
				vectlyr.getLinkProperties().setField(getSelectedLinkField().toString().trim());
				vectlyr.getLinkProperties().setType(getLinkType());

				System.out.println("Link Properties");
				System.out.println("Extensión: " + vectlyr.getLinkProperties().getExt());
				System.out.println("Campo: " + vectlyr.getLinkProperties().getField());
				System.out.println("Tipo: " + vectlyr.getLinkProperties().getType());
			}
        }
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return PluginServices.getText(this,"General");
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

