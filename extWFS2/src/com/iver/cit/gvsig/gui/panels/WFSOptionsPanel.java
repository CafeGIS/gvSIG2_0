package com.iver.cit.gvsig.gui.panels;

import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.remoteClient.wfs.WFSStatus;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.utiles.StringUtilities;

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
/* CVS MESSAGES:
 *
 * $Id: WFSOptionsPanel.java 17736 2008-01-02 16:53:48Z ppiqueras $
 * $Log$
 * Revision 1.12  2007-02-22 12:25:39  ppiqueras
 * Añadidas tool tip text y etiqueta para de milisegundos.
 *
 * Revision 1.11  2006/10/05 12:49:57  jorpiell
 * Cambiada la cadena buffer por max_features
 *
 * Revision 1.10  2006/10/02 09:09:45  jorpiell
 * Cambios del 10 copiados al head
 *
 * Revision 1.9.2.1  2006/09/26 07:36:24  jorpiell
 * El WFS no reproyectaba porque no se le asignaba a la capa un sistema de referencia. Ahora ya se hace.
 *
 * Revision 1.9  2006/09/05 14:25:05  jorpiell
 * Eliminado el nombre de usuario y la contraseña del panel de opciones
 *
 * Revision 1.8  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.7  2006/06/22 06:54:14  jorpiell
 * Se ha desabilitado el cuadro de diálogo del sistema de referencia
 *
 * Revision 1.6  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.5  2006/06/15 07:50:58  jorpiell
 * Añadida la funcionalidad de reproyectar y hechos algunos cambios en la interfaz
 *
 * Revision 1.4  2006/06/01 15:40:50  jorpiell
 * Se ha capturado una excepción que saltaba al ejecutar la extensión en iglés provocada por el signo "," que aparece en lo números (en castellano ".").
 *
 * Revision 1.3  2006/05/23 08:09:39  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.2  2006/05/19 12:57:08  jorpiell
 * Modificados algunos paneles
 *
 * Revision 1.1  2006/04/20 16:38:24  jorpiell
 * Ahora mismo ya se puede hacer un getCapabilities y un getDescribeType de la capa seleccionada para ver los atributos a dibujar. Queda implementar el panel de opciones y hacer el getFeature().
 *
 *
 */

/**
 * <p>Panel with options about the timeout, maximum number of features, the projection reference system, and
 *  if it's implemented, user and password.</p>
 * 
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 * @author Pablo Piqueras Bartolomé
 */
public class WFSOptionsPanel extends AbstractWFSPanel {
	private static final long serialVersionUID = -769956995699452173L;

	private JPanel credentialsPanel = null;
	private JLabel usernameLabel = null;
	private JTextField usernameText = null;
	private JLabel passwordLabel = null;
	private JPasswordField passwordText = null;
	private JPanel connectionParamsPanel = null;
	private JLabel timeoutLabel = null;
	private JFormattedTextField bufferText = null;
	private JLabel bufferLabel = null;
	private JFormattedTextField timeOutText = null;
	private JPanel formatPanel = null;
	private JLabel srsLabel = null;
	private JTextField srsText = null;
	private JLabel jLabelMiliSeconds = null;
	private JPanel bufferPanel = null;
	private JPanel timeOutPanel = null;
	private JPanel northPanel = null;

	/**
	 * Creates a new WFS options panel.
	 */
	public WFSOptionsPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes usernameText
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getUsernameText() {
		if (usernameText == null) {
			usernameText = new JTextField();
			usernameText.setBounds(new java.awt.Rectangle(100,25,200,20));
			usernameText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			});
		}
		return usernameText;
	}

	/**
	 * This method initializes passwordText
	 *
	 * @return javax.swing.JTextField
	 */
	private JPasswordField getPasswordText() {
		if (passwordText == null) {
			passwordText = new JPasswordField();
			passwordText.setBounds(new java.awt.Rectangle(100,50,200,20));
			passwordText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			});
		}
		return passwordText;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getConnectionParamsPanel() {
		if (connectionParamsPanel == null) {
			java.awt.GridBagConstraints gridBagConstraints;

			bufferPanel = new JPanel();
			bufferLabel = new JLabel();
			bufferLabel.setText(PluginServices.getText(this, "max_features"));
			bufferPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			bufferPanel.add(bufferLabel);
			bufferPanel.add(getBufferText());

			timeOutPanel = new JPanel();
			timeoutLabel = new JLabel();
			timeoutLabel.setText(PluginServices.getText(this, "timeout"));
			timeOutPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			timeOutPanel.add(timeoutLabel);
			timeOutPanel.add(getTimeOutText());
			timeOutPanel.add(getJLabelMiliSeconds());

			connectionParamsPanel = new JPanel();
			connectionParamsPanel.setLayout(new java.awt.GridBagLayout());
			connectionParamsPanel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "conection"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			connectionParamsPanel.add(bufferPanel, gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;
			connectionParamsPanel.add(timeOutPanel, gridBagConstraints);		
		}

		return connectionParamsPanel;
	}

	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getBufferText() {
		if (bufferText == null) {
			bufferText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			bufferText.setValue(new Integer(1000));
			bufferText.setBounds(new Rectangle(100, 25, 70, 20));
			bufferText.setToolTipText(PluginServices.getText(bufferText, "set_Max_Number_Of_Features_Will_Be_Loaded"));
			bufferText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			});
		}

		return bufferText;
	}

	/**
	 * This method initializes jTextField1
	 *
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getTimeOutText() {
		if (timeOutText == null) {
			timeOutText = new JFormattedTextField(NumberFormat.getIntegerInstance());
			timeOutText.setValue(new Integer(10000));
			timeOutText.setBounds(new Rectangle(100, 50, 70, 20));
			timeOutText.setToolTipText(PluginServices.getText(timeOutText, "set_TimeOut_Explanation"));
			timeOutText.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			});
		}

		return timeOutText;
	}

	/**
	 * Gets the name of the user.
	 * 
	 * @return name of the user
	 */
	public String getUserName(){
		return getUsernameText().getText();
	}

	/**
	 * Gets the password of the user.
	 * 
	 * @return password of the user
	 */
	public String getPassword(){
		return getPasswordText().getText();
	}

	/**
	 * Gets the maximum number of values that can load.
	 *
	 * @return the maximum number of values that can load
	 */
	public int getBuffer(){
		try {
			String buffer = StringUtilities.replace(getBufferText().getText(),".","");
			buffer = StringUtilities.replace(buffer,",","");
			return Integer.parseInt(buffer);
		} catch (NumberFormatException e) {
			return 10000;
		}
	}

	/**
	 * Gets the timeout used during the loading process.
	 *
	 * @return the timeout used during the loading process
	 */
	public int getTimeout(){
		try {
			String timeOut = StringUtilities.replace(getTimeOutText().getText(),".","");
			timeOut = StringUtilities.replace(timeOut,",","");
			return Integer.parseInt(timeOut);
		} catch (NumberFormatException e) {
			return 10000;
		}
	}

	/**
	 * Gets the reference system of the layer.
	 *
	 * @return the reference system of the layer
	 */
	public String getSRS(){
		return getSrsText().getText();
	}

	/**
	 * This method initializes formatPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getFormatPanel() {
		if (formatPanel == null) {
			formatPanel = new JPanel();
			formatPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			formatPanel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "srs"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
			formatPanel.add(getSRSLabel());
			formatPanel.add(getSrsText());
		}
		return formatPanel;
	}

	/**
	 * This method initializes srsLabel
	 *
	 * @return javax.swing.JLabel
	 */
	private JLabel getSRSLabel() {
		if (srsLabel == null) {
			srsLabel = new JLabel();
			srsLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
			srsLabel.setText(PluginServices.getText(this, "srs"));
			srsLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			srsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
			srsLabel.setBounds(new java.awt.Rectangle(10, 25, 85, 16));
		}

		return srsLabel;
	}

	/**
	 * This method initializes srsText
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getSrsText() {
		if (srsText == null) {
			srsText = new JTextField();
			srsText.setBounds(new java.awt.Rectangle(98, 25, 100, 20));
			srsText.setEnabled(false);
			srsText.setEditable(false);
			srsText.setToolTipText(PluginServices.getText(srsText, "select_Feature_Reference_System"));
		}
		return srsText;
	}

	/**
	 * Refresh the panel
	 * 
	 * @param feature
	 */
	public void refresh(WFSSelectedFeature layer) {
		Vector srs = layer.getSrs();

		if (srs.size() > 0) {
			getSrsText().setText((String)srs.get(0));
		}
	}

	/**
	 * Updates the status of this panel.
	 * 
	 * @param status status of the WFS graphical interface.
	 */
	public void setStatus(WFSStatus status) {
		getBufferText().setText(String.valueOf(status.getBuffer()));
		getTimeOutText().setText(String.valueOf(status.getTimeout()));
		getUsernameText().setText(status.getUserName());
		getPasswordText().setText(status.getPassword());
	}

	/**
	 * This method initializes jLabelMiliSeconds
	 *
	 * @return javax.swing.JLabel
	 */
	public JLabel getJLabelMiliSeconds() {
		if (jLabelMiliSeconds == null) {
			jLabelMiliSeconds = new JLabel();
			jLabelMiliSeconds.setText(PluginServices.getText(jLabelMiliSeconds, "miliSeconds"));
			jLabelMiliSeconds.setBounds(new Rectangle(175, 50, 40, 16));
		}

		return jLabelMiliSeconds;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.AbstractWFSPanel#initialize()
	 */
	protected void initialize() {
		setLabel(PluginServices.getText(this, "options"));
		setLabelGroup(PluginServices.getText(this, "wfs"));		
		this.setLayout(new java.awt.BorderLayout());
		java.awt.GridBagConstraints gridBagConstraints;

		northPanel = new JPanel();
		northPanel.setLayout(new java.awt.GridBagLayout());
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;	
		northPanel.add(getConnectionParamsPanel(), gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		northPanel.add(getFormatPanel(), gridBagConstraints);
		
		add(northPanel, java.awt.BorderLayout.NORTH);
	}
}
