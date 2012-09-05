package com.iver.utiles.swing.objectSelection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.utiles.swing.JComboBox;


/**
 * Control consistente en un texto y un combo autocompletable
 *
 * @author Fernando González Cortés
 */
public class ObjectSelection extends JPanel {
	private JLabel jLabel = null;
	private JComboBox combo = null;
	private ObjectSelectionModel model;
	private DefaultComboBoxModel cmbModel = new DefaultComboBoxModel();
	private Object selected;
	/**
	 * This is the default constructor
	 */
	public ObjectSelection() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		jLabel = new JLabel();
		this.setSize(504, 265);
		this.add(jLabel, null);
		this.add(getCombo(), null);
	}

	/**
	 * Obtiene una referencia al combo
	 *
	 * @return com.iver.utiles.swing.JComboBox
	 */
	protected JComboBox getCombo() {
		if (combo == null) {
			combo = new JComboBox();
			combo.setModel(cmbModel);
			combo.setEditable(true);
			combo.addItemListener(new java.awt.event.ItemListener() {
					public void itemStateChanged(java.awt.event.ItemEvent e) {
						seleccion();
					}
				});
		}

		return combo;
	}

	/**
	 * Método invocado cuando cambia la selección del combo
	 */
	protected void seleccion() {
	}

	/**
	 * Establece el modelo del control
	 *
	 * @param model The model to set.
	 *
	 * @throws SelectionException Si hay algún error leyendo los
	 * datos del modelo
	 */
	public void setModel(ObjectSelectionModel model) throws SelectionException {
		this.model = model;

		Object[] objects = model.getObjects();
		cmbModel.removeAllElements();

		for (int i = 0; i < objects.length; i++) {
			cmbModel.addElement(objects[i]);
		}
		
		getCombo().setSelectedIndex(-1);

		jLabel.setText(model.getMsg());
	}

	/**
	 * Obtiene el elemento actualmente seleccionado
	 *
	 * @return Returns the selected item.
	 */
	public Object getSelected() {
		return cmbModel.getSelectedItem();
	}
} //  @jve:decl-index=0:visual-constraint="10,10"
