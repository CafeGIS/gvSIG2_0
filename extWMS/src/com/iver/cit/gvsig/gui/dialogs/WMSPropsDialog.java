/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
package com.iver.cit.gvsig.gui.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.gvsig.fmap.mapcontext.exceptions.ConnectionErrorLayerException;
import org.gvsig.fmap.mapcontext.exceptions.DriverLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LegendLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.NameLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ProjectionLayerException;
import org.gvsig.fmap.mapcontext.exceptions.TypeLayerException;
import org.gvsig.fmap.mapcontext.exceptions.URLLayerException;
import org.gvsig.fmap.mapcontext.exceptions.UnsupportedVersionLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.fmap.layers.WMSLayerNode;
import com.iver.cit.gvsig.gui.panels.WMSParamsPanel;
import com.iver.cit.gvsig.gui.wizards.LayerTreeModel;
import com.iver.cit.gvsig.gui.wizards.WMSWizardData;
import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;

/**
 * The TOC WMS properties panel container.
 *
 * @author jaume - jaume.dominguez@iver.es
 *
 */
public class WMSPropsDialog extends JPanel implements IWindow {
	JDialog dlg = null;  //  @jve:decl-index=0:visual-constraint="10,20"
	private JPanel buttonsPanel = null;
	//private FLyrDefault fLayer = null;
	private FLayer fLayer = null;
	boolean applied = false;;
	private WindowInfo m_ViewInfo = null;

	private JButton btnApply = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private WMSParamsPanel wmsParamsTabbedPane;
    private CommandListener m_actionListener;

    public WMSPropsDialog(FLayer layer) {
		super();
		initialize(layer);
	}


	private void initialize(FLayer layer) {
		setLayout(null);

        setFLayer(layer);
        wmsParamsTabbedPane = getParamsPanel(((FLyrWMS) layer).getProperties());
        wmsParamsTabbedPane.addWizardListener(new WizardListener(){
			public void wizardStateChanged(boolean finishable) {
				getBtnOk().setEnabled(finishable);
				getBtnApply().setEnabled(finishable);
			}

			public void error(Exception e) {
			}

        })  ;
        wmsParamsTabbedPane.disableDisagregatedLayers();
        this.add(wmsParamsTabbedPane);
        this.add(getButtonsPanel(), null);

	}


	public void setFLayer(FLayer f) {
		fLayer = f;
	}

	/**
	 * With getParamsPanel we have access to the map config TabbedPane.
	 * If this panel doesn't exist yet (which can occur when an existing project is
	 * recovered) it is been automatically constructed by connecting to the server,
	 * reloading the necessary data, filling up the content and setting the selected
	 * values that were selected when the projet was saved.
	 *
	 *
	 * Since a connection to the server is needed when rebuiliding the panel, this
	 * causes a delay for the panel's showing up or a nullPointer error if there is
	 * no path to the server.
	 *
	 *
	 * Con getParamsPanel tenemos acceso a juego de pestaï¿½as de configuraciï¿½n
	 * del mapa. Si este panel todavï¿½a no existe (como puede ser cuando
	 * recuperamos un proyecto guardado) ï¿½ste se crea automï¿½ticamente conectando
	 * al servidor, recuperando los datos necesarios, rellenando el contenido y
	 * dejando seleccionados los valores que estaban seleccionados cuando se
	 * guardï¿½ el proyecto.
	 *
	 *
	 * Como para reconstruirse requiere una conexiï¿½n con el servidor esto causa
	 * un retardo en la apariciï¿½n en el toc o un error de nullPointer si no
	 * hay conexiï¿½n hasta el servidor.
	 *
	 *
	 * @return WMSParamsPanel
	 */
	public WMSParamsPanel getParamsPanel(HashMap info) {
	    if (info!=null){
	        try {

	        	// host
                URL host = (URL) info.get("host");
                WMSWizardData dataSource = new WMSWizardData();
                dataSource.setHost(host, false);

                // name
	            WMSParamsPanel toc = new WMSParamsPanel();
	            toc.setLayerName((String)info.get("name"));
	            toc.setWizardData(dataSource);

	            toc.setVisible(true);
	            toc.setListenerSupport(new WizardListenerSupport());

                WMSLayerNode[] selectedLayers = (WMSLayerNode[]) ((Vector) info.get("selectedLayers")).toArray(new WMSLayerNode[0]);
                //boolean any = false;
                // selected layers
                //((JDnDListModel) toc.getLstSelectedLayers().getModel()).clear();
	            for (int i = 0; i < selectedLayers.length; i++) {
	            	toc.getJustTreeLayer().clearSelection();
	            	//  Se añade a la lista de layers seleccionados
	            	TreePath path = new TreePath(((LayerTreeModel)toc.getJustTreeLayer()
	            						.getModel()).getNodeByName(selectedLayers[i].getName()));
	            	toc.getJustTreeLayer().addSelectionPath(path);
	            	toc.addLayer();
	            	//This block is deleted since this is implemented in the addLayer method.
//                    JDnDListModel modelo = (JDnDListModel) toc.getLstSelectedLayers().getModel();
//                    if (modelo.addElement(0, selectedLayers[i])) {
//                        any = true;
//                    }
                }
//                if (any) {
//                    toc.refreshData();
//                }

                // srs
                String srs = (String) info.get("srs");
                int index = toc.getSRSIndex( srs );
	            if (index != -1) {
					toc.getLstSRSs().setSelectedIndex(index);
				}

                // format
	            String format = (String) info.get("format");
	            index = toc.getFormatIndex(format);
	            if (index != -1) {
					toc.getLstFormats().setSelectedIndex(index);
				}

	            // transparency
                toc.setTransparent(((Boolean) info.get("wmsTransparency")).booleanValue());

                // styles
                Vector v = (Vector) info.get("styles");
                if (v != null) {
					toc.setStyleSelections(v);
				}

                // dimensions
                v = (Vector) info.get("dimensions");
                if (v != null) {
					toc.setDimensions(v);
				}

                // fixed sizes
                Dimension sz = (Dimension) info.get("fixedSize");
                if (sz != null) {
					toc.setFixedSize(sz);
				}
                toc.refreshInfo();
                applied = false;
	            return toc;
	        } catch (ConnectionErrorLayerException ioe){
	            JOptionPane.showMessageDialog(null, "error_comunicacion_servidor", "Error", JOptionPane.ERROR_MESSAGE);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return null;
	}


	public JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			m_actionListener = new CommandListener(this);
			buttonsPanel = new JPanel();
	        buttonsPanel.setBounds(5, wmsParamsTabbedPane.getHeight(), 471, 40);
			buttonsPanel.setLayout(null);
			buttonsPanel.setName("buttonPanel");

	        buttonsPanel.add(getBtnOk(), null);
	        buttonsPanel.add(getBtnApply(), null);
	        buttonsPanel.add(getBtnCancel(), null);
		}
		return buttonsPanel;
	}

	public JButton getBtnOk() {
		if (btnOk == null) {
	        btnOk = new JButton("ok");
	        btnOk.setText(PluginServices.getText(this,"ok"));
	        btnOk.setActionCommand("OK");
	        btnOk.addActionListener(m_actionListener);
	        btnOk.setBounds(367, 9, 90, 25);
		}
		return btnOk;
	}

	public JButton getBtnApply() {
		if (btnApply == null) {
	        btnApply = new JButton("apply");
	        btnApply.setText(PluginServices.getText(this,"apply"));
	        btnApply.setEnabled(false);
	        btnApply.setActionCommand("APPLY");
	        btnApply.addActionListener(m_actionListener);
	        btnApply.setBounds(267, 9, 90, 25);
		}
		return btnApply;
	}

	public JButton getBtnCancel() {
		if (btnCancel == null) {
	        btnCancel = new JButton("cancel");
	        btnCancel.setText(PluginServices.getText(this,"cancel"));
	        btnCancel.setActionCommand("CANCEL");
	        btnCancel.addActionListener(m_actionListener);
	        btnCancel.setBounds(137, 9, 90, 25);
		}
		return btnCancel;
	}

    private class CommandListener implements ActionListener {

        /**
         * Creates a new ComandosListener object.
         *
         * @param lg DOCUMENT ME!
         */
        public CommandListener(WMSPropsDialog tp) {
            //m_tp = tp;
        }

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == "CANCEL") {
				close();
			} else{
            	try {
					((FLyrWMS) fLayer).setFullExtent(wmsParamsTabbedPane.getLayersRectangle());
				} catch (ProjectionLayerException ex) {
					ex.printStackTrace();
			        JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), ex.getMessage());
			        return;
				}
				((FLyrWMS) fLayer).setFormat(wmsParamsTabbedPane.getFormat());
                try {
                if (!wmsParamsTabbedPane.getDisagregatedLayers()) {
					((FLyrWMS) fLayer).setLayerQuery(wmsParamsTabbedPane.getLayersQuery());
				}
                ((FLyrWMS) fLayer).setInfoLayerQuery(wmsParamsTabbedPane.getQueryableLayerQuery());
                ((FLyrWMS) fLayer).setSRS(wmsParamsTabbedPane.getSRS());
                ((FLyrWMS) fLayer).setName(wmsParamsTabbedPane.getLayerName());
                ((FLyrWMS) fLayer).setWmsTransparency(wmsParamsTabbedPane.getTransparency());
                ((FLyrWMS) fLayer).setStyles(wmsParamsTabbedPane.getStyles());
                ((FLyrWMS) fLayer).setDimensions(wmsParamsTabbedPane.getDimensions());
                ((FLyrWMS) fLayer).setFixedSize(wmsParamsTabbedPane.getFixedSize());

            	//fLayer = wmsParamsTabbedPane.getLayer();
            	FLayers parent = fLayer.getParentLayer();

                if (e.getActionCommand() == "APPLY"){

                	com.iver.cit.gvsig.project.documents.view.gui.View vista = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices.getMDIManager().getActiveWindow();
                    MapControl mapCtrl = vista.getMapControl();

                    if (parent != null){
                    	if ( wmsParamsTabbedPane.getLayer() instanceof FLayers){

								mapCtrl.getMapContext().getLayers().
								replaceLayer(parent.getName(),mergeFLayers(parent,(FLayers)wmsParamsTabbedPane.getLayer()));

                    	}
                    }
                    else {
                    	mapCtrl.getMapContext().getLayers().replaceLayer(fLayer.getName(), wmsParamsTabbedPane.getLayer());
                    }
                    //mapCtrl.getMapContext().getLayers().addLayer( fLayer );
                    mapCtrl.getMapContext().invalidate();
                    applied = true;
                    getBtnApply().setEnabled(!applied);
                }
                if (e.getActionCommand() == "OK") {
                    if (!applied) {

                        com.iver.cit.gvsig.project.documents.view.gui.View vista = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices.getMDIManager().getActiveWindow();
                        MapControl mapCtrl = vista.getMapControl();

                        if (parent != null){
                        	if ( wmsParamsTabbedPane.getLayer() instanceof FLayers){
                        		mapCtrl.getMapContext().getLayers().
                        		replaceLayer(parent.getName(),mergeFLayers(parent,(FLayers)wmsParamsTabbedPane.getLayer()));
                        	}
                        }
                        else {
                        	mapCtrl.getMapContext().getLayers().replaceLayer(fLayer.getName(), wmsParamsTabbedPane.getLayer());
                        }                        mapCtrl.getMapContext().invalidate();
                    }
                    close();
                }
                } catch (ConnectionErrorLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (DriverLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (LegendLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (NameLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (ProjectionLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (TypeLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (UnsupportedVersionLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (URLLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			} catch (LoadLayerException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
            }
		}
    }
    /**
     * Merge two FLayers in one
     * @param group1
     * @param group2
     * @return
     */
    private FLayers mergeFLayers(FLayers group1, FLayers group2)
    {
    	//FLayers agrupation = new FLayers(group1.getFMap(), group1.getParentLayer());
    	FLayer layer;
    	for(int i = 0; i < group2.getLayersCount(); i++)
    	{
    		layer = group2.getLayer( i );
    		if(group1.getLayer( layer.getName()) == null ){
    			group1.addLayer( layer );
    		}
    	}

    	return group1;
    }



	public WindowInfo getWindowInfo() {
		if (m_ViewInfo==null){
			m_ViewInfo=new WindowInfo(WindowInfo.MODALDIALOG);
			m_ViewInfo.setTitle(PluginServices.getText(this,"fit_WMS_layer"));
	        m_ViewInfo.setWidth(wmsParamsTabbedPane.getWidth()+10);
	        m_ViewInfo.setHeight(wmsParamsTabbedPane.getHeight()+40);

		}
		return m_ViewInfo;
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
