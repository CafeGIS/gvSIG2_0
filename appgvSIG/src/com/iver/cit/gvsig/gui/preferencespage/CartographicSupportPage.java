package com.iver.cit.gvsig.gui.preferencespage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupportToolkit;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.gui.JComboBoxUnits;
import com.iver.cit.gvsig.gui.styling.JComboBoxUnitsReferenceSystem;
import com.iver.utiles.XMLEntity;

/**
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class CartographicSupportPage extends AbstractPreferencePage {
	private static final String DefaultMeasureUnitKey = "DefaultMeasureUnitKey";
	private static final String DefaultUnitReferenceSystemKey = "DefaultUnitReferenceSystemKey";
	private JComboBoxUnits cmbUnits;
	private JComboBoxUnitsReferenceSystem cmbReferenceSystem;
	
	public CartographicSupportPage() {
		super();
		initialize();
	}
	
	private void initialize() {
		addComponent(PluginServices.getText(this, "default_measure_units"),
				cmbUnits = new JComboBoxUnits(true));
		addComponent(PluginServices.getText(this, "default_measure_units_reference_system"),
				cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());
		addComponent(new JSeparator(JSeparator.HORIZONTAL));
	}

	// pending of a proposed refactor, don't erase
//	@Override
//	public void persistPreferences() throws StoreException {
//		PluginServices ps = PluginServices.getPluginServices(this);
//		XMLEntity xml = ps.getPersistentXML();
//		xml.putProperty(DefaultMeasureUnitKey, cmbUnits.getSelectedUnitIndex());
//		xml.putProperty(DefaultUnitReferenceSystemKey, cmbReferenceSystem.getSelectedIndex());
//	}

	@Override
	public void setChangesApplied() {
		setChanged(false);
	}

	public void applyValuesFromPersistence() throws StoreException {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		if (xml.contains(DefaultMeasureUnitKey))
			CartographicSupportToolkit.DefaultMeasureUnit = xml.getIntProperty(DefaultMeasureUnitKey);
		if (xml.contains(DefaultUnitReferenceSystemKey))
			CartographicSupportToolkit.DefaultReferenceSystem = xml.getIntProperty(DefaultUnitReferenceSystemKey);
	}

	public String getID() {
		return getClass().getName();
	}

	public ImageIcon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public JPanel getPanel() {
		return this;
	}

	public String getTitle() {
		return PluginServices.getText(this, "cartographic_support");
	}

	// pending of a refactoring do not delete (swap commented lines)
//	public void initializeComponents() {
	public void initializeValues() {
		cmbUnits.setSelectedUnitIndex(CartographicSupportToolkit.DefaultMeasureUnit);
		cmbReferenceSystem.setSelectedIndex(CartographicSupportToolkit.DefaultReferenceSystem);
	}

	public void initializeDefaults() {
		CartographicSupportToolkit.DefaultMeasureUnit = -1; // pixel
		CartographicSupportToolkit.DefaultReferenceSystem = CartographicSupport.WORLD;
		initializeValues();
		// pending of a refactoring do not delete (swap commented lines)
//		initializeComponents();
	}
	
	// pending of a refactoring, following method would be removed
	@Override
	public void storeValues() throws StoreException {
		// TODO Auto-generated method stub
		
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

}
