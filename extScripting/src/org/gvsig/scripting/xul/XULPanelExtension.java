package org.gvsig.scripting.xul;

import java.io.File;

import com.iver.andami.plugins.Extension;

public class XULPanelExtension extends Extension {

	public void initialize() {
		// Do nothing.
	}
	
	public void execute(String actionCommand) {
		try {
			File f = new File(actionCommand);
			XULScriptableJPanel window = new XULScriptableJPanel();
			window.setXULFile(f.getAbsolutePath());
			window.showWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public boolean isEnabled() {
		return true;
	}
		

	public boolean isVisible() {
		return true;
	}

}
