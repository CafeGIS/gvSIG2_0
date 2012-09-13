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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponentListener;
import org.gvsig.remotesensing.imagefusion.process.ATROUSFusionProcess;
import org.gvsig.remotesensing.imagefusion.process.BroveyFusionProcess;
import org.gvsig.remotesensing.imagefusion.process.IHSFusionProcess;
import org.gvsig.remotesensing.imagefusion.process.PCAFusionProcess;

/**
 * <code>FusionListener</code> es la clase donde se procesará gran parte del
 * código que controla el panel para el manejo de un layer en la aplicación de
 * fusión de imagenes.
 *
 * @author aMuÑoz - (alejandro.munoz@uclm.es)
 */
public class FusionListener implements ActionListener, PropertiesComponentListener,ChangeListener {

	private FusionPanel fusionDialog  = null;
	
	/**
	 * Construye un FilterListener especificando el FilterPanel asociado
	 * @param filterDialog
	 */
	public FusionListener(FusionPanel filterDialog) {
		this.fusionDialog = filterDialog;
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==fusionDialog.getMainPanel().getJComboMethods())
		{
			if(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==0 || fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==1){
				fusionDialog.getNewLayerPanel().getRadioAllBand().setEnabled(false);
				fusionDialog.getNewLayerPanel().getRadioAsignedBand().setSelected(true);
				fusionDialog.getNewLayerPanel().getRadioAllBand().setSelected(false);
			}
			
			else
			{
				fusionDialog.getNewLayerPanel().getRadioAllBand().setEnabled(true);
			}
			updatePanel();
			fusionDialog.getMainPanel().updateUI();
		}
	}
	
	void updatePanel(){
		
		fusionDialog.getMainPanel().getPanel(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex());
		fusionDialog.updateUI();
	}
	
	public void accept(){
		fusionDialog.getMainPanel().getParameterPanel().setParams();
		HashMap params= fusionDialog.getMainPanel().getParameterPanel().getParams();
		
		if(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==0)
			{
				BroveyFusionProcess proceso = new BroveyFusionProcess();
				proceso.addParam("coef",params.get("coef"));
				proceso.addParam("layer",fusionDialog.getLayer());
				proceso.addParam("high_band",fusionDialog.getMainPanel().getBandPanel().getLayer());
				proceso.addParam("band",new Integer(fusionDialog.getMainPanel().getBandPanel().getModeloTabla().getFirstSelected()));
				proceso.addParam("filename", fusionDialog.getNewLayerPanel().getFileSelected());
				proceso.start();
			}
		else if(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==1){
			
				IHSFusionProcess proceso = new IHSFusionProcess();
				proceso.addParam("coef",params.get("coef"));
				proceso.addParam("layer",fusionDialog.getLayer());
				proceso.addParam("high_band",fusionDialog.getMainPanel().getBandPanel().getLayer());
				proceso.addParam("band",new Integer(fusionDialog.getMainPanel().getBandPanel().getModeloTabla().getFirstSelected()));
				proceso.addParam("filename", fusionDialog.getNewLayerPanel().getFileSelected());
				proceso.start();
		}
		
		else if(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==2)//.getIDPanel()==PluginServices.getText(this,"pc")){
		{
				PCAFusionProcess proceso = new PCAFusionProcess();
				proceso.addParam("layer",fusionDialog.getLayer());
				proceso.addParam("high_band",fusionDialog.getMainPanel().getBandPanel().getLayer());
				proceso.addParam("band",new Integer(fusionDialog.getMainPanel().getBandPanel().getModeloTabla().getFirstSelected()));
				proceso.addParam("filename", fusionDialog.getNewLayerPanel().getFileSelected());
				proceso.start();
		}
		else if(fusionDialog.getMainPanel().getJComboMethods().getSelectedIndex()==3)//.getIDPanel()==PluginServices.getText(this,"pc")){
		{
				ATROUSFusionProcess proceso = new ATROUSFusionProcess();
				proceso.addParam("layer",fusionDialog.getLayer());
				proceso.addParam("high_band",fusionDialog.getMainPanel().getBandPanel().getLayer());
				proceso.addParam("band",new Integer(fusionDialog.getMainPanel().getBandPanel().getModeloTabla().getFirstSelected()));
				proceso.addParam("nivel", params.get("nivel"));
				
				// Opcion de todas las bandas o solo las asignadas a visualizacion.
				int bands[]=null;
				if(fusionDialog.getNewLayerPanel().getRadioAllBand().isSelected()){
					bands= new int [((FLyrRasterSE)fusionDialog.getLayer()).getBandCount()];
					for(int i=0; i<((FLyrRasterSE)fusionDialog.getLayer()).getBandCount();i++)
						bands[i]=i;
				}
				else{
					bands= ((FLyrRasterSE)fusionDialog.getLayer()).getRenderBands();
				}	
				proceso.addParam("bands",bands);
				proceso.addParam("filename", fusionDialog.getNewLayerPanel().getFileSelected());
				proceso.start();
		}
	}
	
	public void interrupted() {
		// TODO Auto-generated method stub
	}


	public void actionChangeProperties(EventObject e) {
	}

	
	public void stateChanged(ChangeEvent e) {
	
		
	}
}