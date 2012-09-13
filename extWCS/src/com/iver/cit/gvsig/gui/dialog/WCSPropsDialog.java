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
package com.iver.cit.gvsig.gui.dialog;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLyrWCS;
import com.iver.cit.gvsig.gui.panels.WCSParamsPanel;
import com.iver.cit.gvsig.gui.wcs.WCSWizardData;
import com.iver.cit.gvsig.gui.wizards.WizardListener;
import com.iver.cit.gvsig.gui.wizards.WizardListenerSupport;

/**
 *  The TOC WCS properties panel container.
 *
 * @author jaume - jaume.dominguez@iver.es
 *
 */
public class WCSPropsDialog extends JPanel implements IWindow {
	JDialog dlg = null;  //  @jve:decl-index=0:visual-constraint="10,20"
	private JPanel buttonsPanel = null;
	//private FLyrDefault fLayer = null;
	private FLayer fLayer = null;
	boolean applied;
	private WindowInfo m_ViewInfo = null;

	private JButton btnApply = null;
	private JButton btnOk = null;
	private JButton btnCancel = null;
	private WCSParamsPanel wcsParamsTabbedPane;
    private ComandosListener m_actionListener;

    /**
     * Creates a new instance of WCSPropsDialog
     * @param layer,
     */
    public WCSPropsDialog(FLyrWCS layer) {
		super();
		initialize(layer);
	}


	private void initialize(FLyrWCS layer) {
		setBounds(0,0,500,333);
        setLayout(null);

        setFLayer(layer);
        wcsParamsTabbedPane = getParamsPanel((layer).getProperties());
        wcsParamsTabbedPane.addWizardListener(new WizardListener(){

			public void wizardStateChanged(boolean finishable) {
				getBtnOk().setEnabled(finishable);
				getBtnApply().setEnabled(finishable);
			}

			public void error(Exception e) {
			}

        })  ;

        this.add(wcsParamsTabbedPane);
		this.add(getButtonsPanel(), null);
	}


	public void setFLayer(FLayer f) {
		fLayer = f;
	}

	/**
	 * With getParamsPanel we have access to the coverage config TabbedPane.
	 * If this panel doesn't exist yet (which can occur when an existing project is
	 * recovered) it is been automatically constructed by connecting to the server,
	 * reloading the necessary data, filling up the content and setting the selected
	 * values that were selected when the projet was saved.
	 *
	 * Since a connection to the server is needed when rebuiliding the panel, this
	 * causes a delay for the panel's showing up or a nullPointer error if there is
	 * no path to the server.
	 *
	 *
	 * Con getParamsPanel tenemos acceso a juego de pestañas de configuración
	 * de la cobertura. Si este panel todavía no existe (como puede ser cuando
	 * recuperamos un proyecto guardado) éste se crea automáticamente conectando
	 * al servidor, recuperando los datos necesarios, rellenando el contenido y
	 * dejando seleccionados los valores que estaban seleccionados cuando se
	 * guardó el proyecto.
	 *
	 * Como para reconstruirse requiere una conexión con el servidor esto causa
	 * un retardo en la aparición en el toc o un error de nullPointer si no
	 * hay conexión hasta el servidor.
	 *
	 * @return WCSParamsPanel
	 */
	public WCSParamsPanel getParamsPanel(Hashtable info) {
		try {

			URL host = (URL) info.get("host");
			WCSWizardData dataSource = new WCSWizardData();
			dataSource.setHost(host, false);

			WCSParamsPanel toc = new WCSParamsPanel();
			toc.setVisible(true);
			toc.setListenerSupport(new WizardListenerSupport());

			toc.setDataSource(dataSource);

			toc.getLstCoverages().setListData(dataSource.getCoverageList());

			String coverageName = (String) info.get("name");
			int index = toc.getCoverageIndex( coverageName );
			if (index != -1) {
				toc.getLstCoverages().setSelectedIndex(index);
			}

			toc.refreshData();

			index = toc.getSRSIndex((String) info.get("crs") );
			if (index != -1) {
				toc.getLstCRSs().setSelectedIndex(index);
			}
			index = toc.getFormatIndex((String) info.get("format"));
			if (index != -1) {
				toc.getLstFormats().setSelectedIndex(index);
			}
			String time = (String) info.get("time");
			if (!time.equals("")) {
				toc.getLstSelectedTimes().setListData(time.split(","));
			}
			String parameter = (String) info.get("parameter");
			if (!parameter.equals("")){
				String[] s = parameter.split("=");
				String pName = s[0];

				String regexDouble = "-?[0-9]+(\\.[0-9]+)?";
				String regexInterval = regexDouble+"/"+regexDouble;
				String regexIntervalList = regexInterval+"(,"+regexInterval+")*";
				if (s[1].matches(regexInterval)){
					// Single Interval
					toc.getJScrollPane5().setVisible(false);
					toc.getSingleParamValuesList().setEnabled(false);


				} else if (s[1].matches(regexIntervalList)){
					// Multiple Interval

				} else {
					// Single values
					toc.getJScrollPane5().setVisible(true);
					toc.getSingleParamValuesList().setEnabled(true);

					String[] pVals = s[1].split(",");
					index = toc.getParamIndex(pName);
					toc.getCmbParam().setSelectedIndex(index);
					//toc.refreshParamValues( coverageName );
					int[] indexes = new int[pVals.length];
					for (int i = 0; i < pVals.length; i++) {
						indexes[i] = toc.getValueIndex(pVals[i]);
					}

					toc.getSingleParamValuesList().setSelectedIndices(indexes);
				}
			}
			toc.refreshInfo();
			return toc;
		} catch (ReadException soe) {
			JOptionPane.showMessageDialog(null, "servidor_wcs_no_responde", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			m_actionListener = new ComandosListener(this);
			buttonsPanel = new JPanel();
	        buttonsPanel.setBounds(5, wcsParamsTabbedPane.getHeight(), 471, 40);
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
	        btnOk = new JButton("Ok");
	        btnOk.setText(PluginServices.getText(this,"Ok"));
	        btnOk.setActionCommand("OK");
	        btnOk.addActionListener(m_actionListener);
	        btnOk.setBounds(367, 9, 90, 25);
		}
		return btnOk;
	}

	public JButton getBtnApply() {
		if (btnApply == null) {
	        btnApply = new JButton("Apply");
	        btnApply.setText(PluginServices.getText(this,"Apply"));
	        btnApply.setEnabled(false);
	        btnApply.setActionCommand("APPLY");
	        btnApply.addActionListener(m_actionListener);
	        btnApply.setBounds(267, 9, 90, 25);
		}
		return btnApply;
	}

	public JButton getBtnCancel() {
		if (btnCancel == null) {
	        btnCancel = new JButton("Cancel");
	        btnCancel.setText(PluginServices.getText(this,"Cancel"));
	        btnCancel.setActionCommand("CANCEL");
	        btnCancel.addActionListener(m_actionListener);
	        btnCancel.setBounds(137, 9, 90, 25);
		}
		return btnCancel;
	}

    private class ComandosListener implements ActionListener {
        private WCSPropsDialog m_tp;

        /**
         * Creates a new ComandosListener object.
         *
         * @param lg DOCUMENT ME!
         */
        public ComandosListener(WCSPropsDialog tp) {
            m_tp = tp;
        }

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "CANCEL") {
				close();
			} else {
				applied = false;

            	((FLyrWCS) fLayer).setCoverageName(wcsParamsTabbedPane.getCurrentCoverageName());
            	((FLyrWCS) fLayer).setName(wcsParamsTabbedPane.getCoverageName());
            	((FLyrWCS) fLayer).setSRS(wcsParamsTabbedPane.getSRS());
            	((FLyrWCS) fLayer).setFormat(wcsParamsTabbedPane.getFormat());
            	((FLyrWCS) fLayer).setFullExtent(wcsParamsTabbedPane.getExtent());
            	((FLyrWCS) fLayer).setTime(wcsParamsTabbedPane.getTime());
            	((FLyrWCS) fLayer).setParameter(wcsParamsTabbedPane.getParameterString());

            	if (e.getActionCommand() == "APPLY") {
            		com.iver.cit.gvsig.project.documents.view.gui.View vista = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices.getMDIManager().getActiveWindow();
            		MapControl mapCtrl = vista.getMapControl();
            		mapCtrl.getMapContext().invalidate();
            		applied = true;
            		getBtnApply().setEnabled(!applied);
            	}

            	if (e.getActionCommand() == "OK") {
            		if (!applied) {
            			com.iver.cit.gvsig.project.documents.view.gui.View vista = (com.iver.cit.gvsig.project.documents.view.gui.View) PluginServices.getMDIManager().getActiveWindow();
                        MapControl mapCtrl = vista.getMapControl();
                        mapCtrl.getMapContext().invalidate();
            		}
                    close();
            	}
             }
		}
    }

	public WindowInfo getWindowInfo() {
		if (m_ViewInfo==null){
			m_ViewInfo=new WindowInfo(WindowInfo.MODALDIALOG);
			m_ViewInfo.setTitle(PluginServices.getText(this,"fit_WCS_layer"));
			m_ViewInfo.setWidth(wcsParamsTabbedPane.getWidth()+10);
	        m_ViewInfo.setHeight(wcsParamsTabbedPane.getHeight()+40);
		}
		return m_ViewInfo;
	}

	public void viewActivated() {
	}

	public void openWCSPropertiesDialog() {
		if (PluginServices.getMainFrame() == null) {
			dlg = new JDialog();
			Rectangle bnds = getBounds();
			bnds.width += 10;
			bnds.height += 33;
			dlg.setBounds(bnds);
			dlg.setSize(getSize());
			dlg.getContentPane().add(this);
			dlg.setModal(true);
			dlg.pack();
			dlg.show();
		} else {
			PluginServices.getMDIManager().addWindow(this);
		}
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}


	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
