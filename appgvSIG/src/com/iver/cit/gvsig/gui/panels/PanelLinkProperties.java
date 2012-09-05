package com.iver.cit.gvsig.gui.panels;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.fmap.mapcontext.layers.FLayer;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

//TODO comentado para que compile
public class PanelLinkProperties extends JPanel {

	FLayer layer=null;
	private JPanel jPanel1 = null;
	private JLabel jLabelCampo1 = null;
	private JComboBox jComboBoxCampo1 = null;
	private JLabel jLabelExtension1 = null;
	private JTextField jTextFieldExtension1 = null;
	private JLabel jLabelAccion1 = null;
	private JComboBox jComboBoxAccion1 = null;
	private IProjectView view;

	/**
	 * This is the default constructor
	 */
	public PanelLinkProperties(FLayer flayer, IProjectView vista) {
		super();
//		this.setLayout(new GridBagLayout());
		this.layer=flayer;
		view=vista;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED), PluginServices.getText(this, "Hiperenlace"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		this.setBounds(315, 195, 313, 154);
		this.add(getJPanel1(), null);
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabelAccion1 = new JLabel();
			jLabelAccion1.setText(PluginServices.getText(this, "Accion_Predefinida") + "  ");
			jLabelExtension1 = new JLabel();
			jLabelExtension1.setText(PluginServices.getText(this,"extension"));
			jLabelCampo1 = new JLabel();
			jLabelCampo1.setText(PluginServices.getText(this, "Campo") + "   ");
			jPanel1 = new JPanel();
		}
		jPanel1.setBounds(315, 195, 313, 154);
		jPanel1.setPreferredSize(new java.awt.Dimension(300,120));
		//jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		jPanel1.add(jLabelCampo1, null);
		jPanel1.add(getJComboBoxCampo1(), null);
		jPanel1.add(jLabelExtension1, null);
		jPanel1.add(getJTextFieldExtension1(), null);
		jPanel1.add(jLabelAccion1, null);
		jPanel1.add(getJComboBoxAccion1(), null);
		return jPanel1;
	}

	/**
	 * This method initializes jComboBoxCampo1
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxCampo1() {
		if (jComboBoxCampo1 == null) {
			jComboBoxCampo1 = new JComboBox();
			jComboBoxCampo1.setPreferredSize(new java.awt.Dimension(80,25));

			if (PluginServices.getMainFrame() != null) {
				if  (PluginServices.getMDIManager().getActiveWindow() instanceof BaseView ){
				    BaseView theView = (BaseView)PluginServices.getMDIManager().getActiveWindow();
				    view = theView.getModel();
//				    try {
//                		if (layer instanceof FLyrVect) {
//                    		FLyrVect ad = (FLyrVect) layer;
//                    		DataSource ds;
//                    		ds = ad.getRecordset();
//                    		String[] names = new String[ds.getFieldCount()];
//                    		for (int i = 0; i < ds.getFieldCount(); i++) {
//                    			names[i] = ds.getFieldName(i);
//                    		}
//                    		//jComboBox = new javax.swing.JComboBox(names);
//                    		DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(names);
//                    		jComboBoxCampo1.setModel(defaultModel);
//                    		if (view.getSelectedField() != null) {
//                    			jComboBoxCampo1.setSelectedItem(view.getSelectedField());
//                    			//layer.setField(view.getSelectedField());
//                    		}
//                			}else{
//                				jComboBoxCampo1 = new javax.swing.JComboBox();
//                			}
//				    } catch (ReadException e) {
//				    	NotificationManager.addError("No se pudo obtener la tabla", e);
//				    }
				}
			}
			}
		return jComboBoxCampo1;
	}

	/**
	 * This method initializes jTextFieldExtension1
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldExtension1() {
		if (jTextFieldExtension1 == null) {
			jTextFieldExtension1 = new JTextField();
			jTextFieldExtension1.setPreferredSize(new java.awt.Dimension(40,25));
		}
		return jTextFieldExtension1;
	}

	/**
	 * This method initializes jComboBoxAccion1
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxAccion1() {
		if (jComboBoxAccion1 == null) {
			jComboBoxAccion1 = new JComboBox();
			jComboBoxAccion1.setPreferredSize(new java.awt.Dimension(120,25));
			ComboActionModel t=new ComboActionModel();
			DefaultComboBoxModel defaultModel1 = new DefaultComboBoxModel(t.getModel());
			jComboBoxAccion1.setModel(defaultModel1);

		}
		return jComboBoxAccion1;
	}
}

	class ComboActionModel{
		private String types[] = null;

		ComboActionModel(){
			types[0]="Enlazar a ficheros de imágen";
			types[1]="Enlazar a ficheros de texto";
		}
		String toString(int ind){
			return types[ind];
		}
		public String[] getModel(){
			return types;
		}

		public int getOrder(String field){
			if (field.compareTo(types[0])==0){
				return 0;
			}
			else if (field.compareTo(types[1])==0) {
				return 1;
			}
			return 1;
		}


	}

	class ComboActionItem {
		private String text=null;
		private int cont;

		public ComboActionItem(String texto, int contador){
			text = texto;
			cont = contador;
		}

		public String toString() {
			return this.text;
		}
	}