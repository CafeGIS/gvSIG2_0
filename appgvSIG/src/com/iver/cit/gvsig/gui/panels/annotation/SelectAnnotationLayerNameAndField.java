package com.iver.cit.gvsig.gui.panels.annotation;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import com.iver.andami.PluginServices;

//TODO comentado para que compile
public class SelectAnnotationLayerNameAndField extends JWizardPanel {
//	private FLyrAnnotation layer;

	public SelectAnnotationLayerNameAndField(JWizardComponents wizardComponents) {
		super(wizardComponents);
		// TODO Auto-generated constructor stub
	}


	private JLabel lblDescription = null;
	private JLabel lblStep1 = null;
	private JLabel lblNewLayerName = null;
	private JLabel lblStep2 = null;
	private JLabel lblField = null;

	private JTextField txtNewLayerName = null;
	private JComboBox cmbField = null;


	private static final Rectangle lblDescriptionPosition = new Rectangle(4,4,355,75);

	private static final Rectangle lblStep1Position = new Rectangle(4,90,15,15);
	private static final Rectangle lblNewLayerNamePosition = new Rectangle(30,90,355,15);
	private static final Rectangle txtNewLayerNamePosition = new Rectangle(30,109,250,18);


	private static final Rectangle lblStep2Position = new Rectangle(4,135,15,12);
	private static final Rectangle lblFieldPosition = new Rectangle(30,135,355,30);
	private static final Rectangle cmbFieldPosition = new Rectangle(30,169,170,18);




	private class EventsListener implements CaretListener,ItemListener
	{
		public void caretUpdate(CaretEvent arg0) {
			updateButtonsState();
		}

		public void itemStateChanged(ItemEvent e) {
			updateButtonsState();
		}

	}


	private void updateButtonsState() {
		setBackButtonEnabled(false);
		boolean enabled =checkIsOkPanelData();
		setNextButtonEnabled(enabled);
		setFinishButtonEnabled(enabled);
	}


	private EventsListener eventsListener = new EventsListener();


	protected boolean checkIsOkPanelData() {
		if (txtNewLayerName.getText().trim().length() < 1) {
			return false;
		}
		if (((String)cmbField.getSelectedItem()).trim().length() < 1) {
			return false;
		}
		return true;
	}


//	public SelectAnnotationLayerNameAndField(JWizardComponents arg0,FLyrAnnotation layer) {
//		super(arg0);
//		this.layer =layer;
//		this.initialize();
//	}

	protected void initialize() {
		this.setLayout(null);
		this.setSize(new Dimension(358,263));
		this.addLabels();

		this.add(getTxtNewLayerName(),null);
		this.add(getCmbField(),null);

		checkIsOkPanelData();
	}

	private JTextField getTxtNewLayerName() {
		if (this.txtNewLayerName == null) {
			this.txtNewLayerName = new JTextField();
			this.txtNewLayerName.setBounds(txtNewLayerNamePosition);
			this.txtNewLayerName.setText("NuevaCapa");
			this.txtNewLayerName.addCaretListener(eventsListener);
		}
		return this.txtNewLayerName;
	}

	public String getNewLayerName() {
		return this.getTxtNewLayerName().getText();
	}


	private JComboBox getCmbField() {
		if (this.cmbField == null) {
			this.cmbField = new JComboBox();
			this.cmbField.setEditable(false);
			this.cmbField.setBounds(cmbFieldPosition);
			this.cmbField.addItemListener(this.eventsListener);
			this.cmbField.addItem("");


//			try {
//				SelectableDataSource dataSource = this.layer.getRecordset();
//
//				String[] fieldsNames = dataSource.getFieldNames();
//
//				for (int i=0; i < fieldsNames.length; i++) {
//					this.cmbField.addItem(fieldsNames[i]);
//				}
//
//			} catch (ReadDriverException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}
		return this.cmbField;
	}

	public String getField() {
		return (String)this.getCmbField().getSelectedItem();
	}


	protected void addLabels() {
		this.lblDescription = new JLabel();
		this.lblStep1 = new JLabel();
		this.lblNewLayerName = new JLabel();
		this.lblStep2 = new JLabel();
		this.lblField= new JLabel();

		this.lblDescription.setText(PluginServices.getText(this,"descripcion_de_crear_capa_de_anotaciones"));
		this.lblStep1.setText("1.");
		this.lblNewLayerName.setText(PluginServices.getText(this,"introduzca_el_nombre_de_la_nueva_capa_de_anotaciones"));
		this.lblStep2.setText("2.");
		this.lblField.setText(PluginServices.getText(this,"seleccione_el_campo_de_texto_que_desea_que_se_utilize_para_mostrar_la_nueva_capa_virtual"));

		this.lblDescription.setBounds(lblDescriptionPosition);
		this.lblStep1.setBounds(lblStep1Position);
		this.lblNewLayerName.setBounds(lblNewLayerNamePosition);
		this.lblStep2.setBounds(lblStep2Position);
		this.lblField.setBounds(lblFieldPosition);


		this.add(lblDescription,null);
		this.add(lblStep1,null);
		this.add(lblNewLayerName,null);
		this.add(lblStep2,null);
		this.add(lblField,null);

	}



}
