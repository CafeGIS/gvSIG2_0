package org.gvsig.remotesensing.imagefusion.gui.parameter.panels;

import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.iver.andami.PluginServices;

public class PCAParameterPanel  extends MethodFusionParameterPanel {

	private JPanel parameterPanel = null;
	
	
	
	public PCAParameterPanel(){
		parameterPanel = new JPanel();
		idPanel = PluginServices.getText(this,"pc");	
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"parametros"));
		topBorder.setTitlePosition(TitledBorder.TOP);
		parameterPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(0,2,2,2)));
	}
	
	
	public JPanel getParameterPanel() {
		return parameterPanel;
	}


	public HashMap getParams() {
		return null;
	}

	public void setParams() {
		
	}

	
	public String getIDPanel() {
		return idPanel;
	}

}
