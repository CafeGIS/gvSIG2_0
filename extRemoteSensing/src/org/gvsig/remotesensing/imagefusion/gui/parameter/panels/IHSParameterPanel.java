package org.gvsig.remotesensing.imagefusion.gui.parameter.panels;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;

import com.iver.andami.PluginServices;

/** Panel de parámetros para el método de IHS */


public class IHSParameterPanel extends MethodFusionParameterPanel implements ActionListener, SliderListener, ChangeListener {
	
	private static final long serialVersionUID = 1L;
	private JPanel 				  parameterPanel 	= null;
	private JTextField            jTextField        = null;
	private JSlider               slIHS           	= null;
	private double 				  coef 				= 0.0;
	
	public IHSParameterPanel(){
		parameterPanel = new JPanel();
		idPanel = PluginServices.getText(this,"brovey");
		parameterPanel.setLayout(new FlowLayout());
		getJSlider1().setValue(0);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(getJSlider1());
		panel.add(getJTextField());
		TitledBorder topBorder0 = BorderFactory.createTitledBorder(PluginServices.getText(this,""));
		topBorder0.setTitlePosition(TitledBorder.TOP);
		panel.setBorder(new CompoundBorder(topBorder0,new EmptyBorder(5,1,1,1)));
		parameterPanel.add(panel);
		
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"parametros"));
		topBorder.setTitlePosition(TitledBorder.TOP);
		parameterPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(0,2,2,2)));
	}
	
	public JPanel getParameterPanel() {
		return parameterPanel;
	}

	
	/**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setPreferredSize(new java.awt.Dimension(50,19));
			jTextField.setText("0.0");
		}
		return jTextField;
	}
	
	public JSlider getJSlider1() {
		if (slIHS == null) {
			slIHS = new JSlider();
			slIHS.setMaximum(100);
			slIHS.setPreferredSize(new java.awt.Dimension(180,16));
			slIHS.addChangeListener(this);
		}
		return slIHS;
	}

	public void actionPerformed(ActionEvent e) {
	}

	public void actionValueChanged(SliderEvent e) {
		
	}

	public void actionValueDragged(SliderEvent e) {
		
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(getJSlider1())) {
			coef= (double)getJSlider1().getValue()/200;
			getJTextField().setText(String.valueOf((coef)));
		}
	}

	public HashMap getParams(){
		return params;
	}
	
	public void setParams(){
		params.put("coef", new Double(coef));
	}

	public String getIDPanel() {
		return idPanel;
	}
}