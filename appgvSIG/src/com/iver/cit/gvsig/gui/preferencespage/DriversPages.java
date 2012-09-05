package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;


/**
 * P�gina de preferencias de donde cuelgan todos los drivers.
 *
 * @author Vicente Caballero Navarro
 */
public class DriversPages extends AbstractPreferencePage {
    private JLabel jLabel = null;
    private ImageIcon icon;

    /**
     * This is the default constructor
     */
    public DriversPages() {
        super();
        icon=PluginServices.getIconTheme().get("layout-insert-view");
        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        jLabel = new JLabel();
        jLabel.setText(PluginServices.getText(this,
                "configurar_todos_los_drivers"));
        this.setLayout(new BorderLayout());
        this.setSize(300, 200);
        this.add(jLabel, java.awt.BorderLayout.CENTER);
    }

    public String getID() {
        return this.getClass().getName();
    }

    public String getTitle() {
        return PluginServices.getText(this, "drivers");
    }

    public JPanel getPanel() {
        return this;
    }

    public void initializeValues() {
    }

    public void storeValues() {
    }

    public void initializeDefaults() {
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public boolean isValueChanged() {
        return false; // Because it does not manage values
    }

    public void setChangesApplied() {
    }
}
