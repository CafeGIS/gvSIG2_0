package org.gvsig.gvsig3dgui.preferences;

import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.gvsig.gui.beans.swing.GridBagLayoutPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

public class View3DPreferences extends AbstractPreferencePage {

	private String id;
	private ImageIcon icon;
	private GridBagLayoutPanel panel;
	private ButtonGroup radioButtonGroup;
	private boolean compatibilitySwing = false;
	private JRadioButton activeAWT;
	private JRadioButton activeGL;
	private static Preferences prefs = Preferences.userRoot().node(
			"gvsig.configuration.3D");

	public View3DPreferences() {
		super();
		// TODO Auto-generated constructor stub
		id = this.getClass().getName();
		icon = PluginServices.getIconTheme().get("gnome-settings-theme");
	}

	public void setChangesApplied() {
		// System.out.println("ESTOY LLAMANDO A setChangesApplied()");
		if (compatibilitySwing)
			activeAWT.setSelected(true);
		else
			activeGL.setSelected(true);
	}

	public void storeValues() throws StoreException {
		// TODO Auto-generated method stub
		// System.out.println("ESTOY LLAMANDO A storeValues()");
		prefs.putBoolean("compatibilitySwing", compatibilitySwing);
	}

	public String getID() {
		return id;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	private ButtonGroup getRadioButonGroup() {

		if (radioButtonGroup == null) {

			// Create an action for each radio button
			Action AWTaction = new AbstractAction("AWT") {

				// This method is called whenever the radio button is pressed,
				// even if it is already selected; this method is not called
				// if the radio button was selected programmatically
				public void actionPerformed(ActionEvent evt) {
					// ACTION THAT ACTIVATE GL PANEL MODE
					compatibilitySwing = true;
				}
			};

			Action GLaction = new AbstractAction("GL") {
				// See above
				public void actionPerformed(ActionEvent evt) {
					// ACTION THAT ACTIVATE CANVAS MODE
					compatibilitySwing = false;
				}
			};

			

			activeAWT.setAction(AWTaction);
			activeGL.setAction(GLaction);
			// Inicialize the button properties
			activeAWT.setName("Si");
			activeAWT.setLabel(PluginServices.getText(this, "AWT"));
			activeGL.setName("No");
			activeGL.setLabel(PluginServices.getText(this, "GL"));

			if (compatibilitySwing)
				activeAWT.setSelected(true);
			else
				activeGL.setSelected(true);

			// Associate the two buttons with a button group
			radioButtonGroup = new ButtonGroup();
			radioButtonGroup.add(activeAWT);
			radioButtonGroup.add(activeGL);
		}

		return radioButtonGroup;
	}

	public JPanel getPanel() {

		if (radioButtonGroup == null) {
			radioButtonGroup = getRadioButonGroup();

			addComponent(new JLabel(PluginServices.getText(this, "Swing_Compability")));
			for (Enumeration e = radioButtonGroup.getElements(); e
					.hasMoreElements();) {
				JRadioButton b = (JRadioButton) e.nextElement();
				addComponent(b);
			}
			addComponent(new JLabel(PluginServices.getText(this, "Swing_Compability_Note")));
		}
		return this;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return "3D";
	}

	public void initializeDefaults() {
		// TODO Auto-generated method stub
		// System.out.println("inicialize Defaults");
		this.compatibilitySwing = true;

	}

	public void initializeValues() {
		// TODO Auto-generated method stub
		// System.out.println("inicialize values");
		compatibilitySwing = prefs.getBoolean("compatibilitySwing",
				compatibilitySwing);
		
		// Create the radio buttons using the actions
		activeAWT = new JRadioButton();
		activeGL = new JRadioButton();

	}

	public boolean isValueChanged() {
		// TODO Auto-generated method stub
		// System.out.println("is value changed");
		return true;
	}

}
