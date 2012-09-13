/*
* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
*   Av. Blasco Ibañez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
*   Campus Universitario s/n
*   02071 Alabacete
*   Spain
*
*   +34 967 599 200
*/

package org.gvsig.remotesensing.imagefusion.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.remotesensing.imagefusion.gui.components.BandHigthResolutionSelection;
import org.gvsig.remotesensing.imagefusion.gui.parameter.panels.ATROUSParameterPanel;
import org.gvsig.remotesensing.imagefusion.gui.parameter.panels.BroveyParameterPanel;
import org.gvsig.remotesensing.imagefusion.gui.parameter.panels.IHSParameterPanel;
import org.gvsig.remotesensing.imagefusion.gui.parameter.panels.MethodFusionParameterPanel;
import org.gvsig.remotesensing.imagefusion.gui.parameter.panels.PCAParameterPanel;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;

/**
 * Es el panel central del panel de fusion. Se compone de un panel de seleccion de
 * la banda de alta resolución, de una parte central en la que se selecciona el método 
 * de fusion y los parámetros propisos del método y el fichero de salida.
 * 
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 */

public class FusionMainPanel extends JPanel implements IUserPanelInterface{
	
	private static final long 				serialVersionUID = -406089078173595028L;
	private JPanel            				centralPanel     = null;
	private JPanel 			  				methodPanel		 = null;	
	private FusionListener   				fusionListener   = null;
	private BandHigthResolutionSelection 	bandPanel		 = null;
	private JComboBox 		 				jComboMethods 	 = null;  
	private BroveyParameterPanel 			broveyPanel		 = null;
	private IHSParameterPanel 				ihsPanel		 = null;
	private PCAParameterPanel				pcaPanel 		 = null;
	private ATROUSParameterPanel			atrousPanel 	 = null;
	private MethodFusionParameterPanel 		panelParam		 = null;
	private boolean first = true;
	
	
	public FusionMainPanel(FusionListener fusionListener) {
		this.fusionListener = fusionListener;
		initialize();
	}
	
	
	private void initialize() {
		setLayout(new BorderLayout(5, 5));
		add(getBandPanel(),BorderLayout.NORTH);
		add(getCentralPanel(), BorderLayout.CENTER);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}

	
	/**
	 * Obtener y generar el JPanel central que contendrá las propiedades
	 * @return
	 */
	public JPanel getCentralPanel() {
		if (centralPanel == null) {
			centralPanel = new JPanel();
			centralPanel.setLayout(new BorderLayout());
			centralPanel.add(getMethodPanel(), BorderLayout.NORTH);
			centralPanel.add(getPanel(0).getParameterPanel(),BorderLayout.CENTER);
			first=false;
			centralPanel.updateUI();
		}
		return centralPanel;
	}
	

	public String getTitle() {
		return  PluginServices.getText(this,"fusion");
	}

	
	/** Panel de seleccion de banda pancromática */
	public BandHigthResolutionSelection getBandPanel() {
		if(bandPanel==null){
			bandPanel= new BandHigthResolutionSelection();
		}
		return bandPanel;
	}
	
	
	public JPanel getMethodPanel(){
		if(methodPanel==null){
			methodPanel = new JPanel();
			methodPanel.setLayout(new BorderLayout());
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"method"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			methodPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(7,7,7,7)));
			methodPanel.add(new JLabel("method"));
			methodPanel.add(getJComboMethods());
		}
		return methodPanel;
	}

	public JComboBox getJComboMethods() {
		if(jComboMethods==null){
			jComboMethods= new JComboBox();
			jComboMethods.addItem(PluginServices.getText(this,"brovey"));
			jComboMethods.addItem(PluginServices.getText(this,"ihs"));
			jComboMethods.addItem(PluginServices.getText(this,"pc"));
			jComboMethods.addItem(PluginServices.getText(this,"wavelets_atrous"));
			jComboMethods.setSelectedIndex(0);
			jComboMethods.addActionListener(fusionListener);

		}
		return jComboMethods;
	}
	
	
	
	/** @return  BroveyParameterPanel para la selección de parámetros para el 
	 * 	método de Brovey
	 * */
	public BroveyParameterPanel getPanelBrovey(){
		if(broveyPanel==null){
			broveyPanel= new BroveyParameterPanel();
		}
		return broveyPanel;
	}
	
	
	/** @return  BroveyParameterPanel para la selección de parámetros para el 
	 * 	método de IHS
	 * */
	public IHSParameterPanel getPanelIHS(){
		if(ihsPanel==null){
			ihsPanel= new IHSParameterPanel();
		}
		return ihsPanel;
	}
	
	
	/** @return  PCAPara,eterPanel para la selección de parámetros para el 
	 * 	método de fusion PCA
	 * */
	public PCAParameterPanel getPanelPCA(){
		if(pcaPanel==null){
			pcaPanel= new PCAParameterPanel();
		}
		return pcaPanel;
	}
	
	
	/** @return  BroveyParameterPanel para la selección de parámetros para el 
	 * 	método de Brovey
	 * */
	public ATROUSParameterPanel getPanelAtrous(){
		if(atrousPanel==null){
			atrousPanel= new ATROUSParameterPanel();
		}
		return atrousPanel;
	}
	
	
	/** @return panelParam*/
	public MethodFusionParameterPanel getParameterPanel(){
		return panelParam;
	}
	
	
	public MethodFusionParameterPanel getPanel(int panel){	
		switch (panel) {
		case 0:
			 getPanelBrovey().getParameterPanel().setVisible(true);
			 getPanelIHS().getParameterPanel().setVisible(false);
			 getPanelPCA().getParameterPanel().setVisible(false);
			 getPanelAtrous().getParameterPanel().setVisible(false);
			 panelParam =  getPanelBrovey();
			 break;
		case 1:
			 getPanelBrovey().getParameterPanel().setVisible(false);
			 getPanelIHS().getParameterPanel().setVisible(true);
			 getPanelPCA().getParameterPanel().setVisible(false);
			 getPanelAtrous().getParameterPanel().setVisible(false);
			 panelParam= getPanelIHS();
			 break;
		case 2:
			 getPanelBrovey().getParameterPanel().setVisible(false);
			 getPanelIHS().getParameterPanel().setVisible(false);
			 getPanelPCA().getParameterPanel().setVisible(true);
			 getPanelAtrous().getParameterPanel().setVisible(false);
			 panelParam= getPanelPCA();
			 break;
			 
		case 3:
			 getPanelBrovey().getParameterPanel().setVisible(false);
			 getPanelIHS().getParameterPanel().setVisible(false);
			 getPanelPCA().getParameterPanel().setVisible(false);
			 getPanelAtrous().getParameterPanel().setVisible(true);
			 panelParam= getPanelAtrous();
			 break;
		}
		panelParam.getParameterPanel().updateUI();
		if(!first)
			centralPanel.remove(panelParam.getParameterPanel());
		    
		centralPanel.add(panelParam.getParameterPanel(),BorderLayout.CENTER);
		return panelParam;
	}
	
	
	
	
}