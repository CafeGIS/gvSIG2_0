package org.gvsig.remotesensing.imagefusion.gui.parameter.panels;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;

public class ATROUSParameterPanel extends MethodFusionParameterPanel{

	private JPanel parameterPanel = null;
	private JComboBox comboLevel= null;
	private JPanel panelLevel = null;
	
	public ATROUSParameterPanel(){
		parameterPanel = new JPanel();
		parameterPanel.setLayout(new BorderLayout());
		idPanel = PluginServices.getText(this,"atrous");	
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"parametros"));
		topBorder.setTitlePosition(TitledBorder.TOP);
		parameterPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(0,2,2,2)));
		parameterPanel.add(getPanelLevelPac(), BorderLayout.CENTER);
	}
	
	
	public JPanel getParameterPanel() {
		return parameterPanel;
	}


	public HashMap getParams(){
		return params;
	}

	
	
	public String getIDPanel() {
		return idPanel;
	}


	public JComboBox getLevelCombo() {
		if(comboLevel==null){
			comboLevel= new JComboBox();
			comboLevel.addItem(new String("1"));
			comboLevel.addItem(new String("2"));
			comboLevel.addItem(new String("3"));
		}
		
		return comboLevel;
	}




	public JPanel getPanelLevelPac() {
		if(panelLevel==null){
			panelLevel= new JPanel();
			BorderLayout bd = new  BorderLayout(); 
			BorderLayout bd2= new BorderLayout();
			bd.setHgap(3);
			panelLevel.setLayout(bd);
			JPanel aux= new JPanel();
			bd2.setHgap(5);
			aux.setLayout(bd2);
			aux.add(new JLabel("level"),BorderLayout.WEST);
			aux.add(getLevelCombo(),BorderLayout.CENTER);
			panelLevel.add(aux,BorderLayout.NORTH);
		
		}
		return panelLevel;
	}

	
	public JPanel getPanelLevelPanc() {
		return panelLevel;
	}
	
	
	public void setParams(){
		params.put("nivel", new Integer(getLevelCombo().getSelectedIndex()+1));
	}

}
