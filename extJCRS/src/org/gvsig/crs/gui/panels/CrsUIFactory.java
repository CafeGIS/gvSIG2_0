package org.gvsig.crs.gui.panels;

import java.awt.GridLayout;

import org.cresques.cts.IProjection;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.dialog.CSSelectionDialog;
import org.gvsig.crs.gui.dialog.TRSelectionDialog;

import com.iver.cit.gvsig.gui.panels.crs.ICrsUIFactory;
import com.iver.cit.gvsig.gui.panels.crs.ISelectCrsPanel;

public class CrsUIFactory implements ICrsUIFactory {

	public ISelectCrsPanel getSelectCrsPanel(IProjection projection,
			boolean isTransPanelActive) {
		if (isTransPanelActive){
			TRSelectionDialog trSelect = new TRSelectionDialog((ICrs)projection);
			trSelect.setProjection(projection);
			trSelect.setLayout(new GridLayout(0,1));
			return trSelect;
		}
		else {
			CSSelectionDialog csSelect = new CSSelectionDialog((ICrs) projection);
			csSelect.setProjection(projection);
			csSelect.initRecents((ICrs)projection);
			return csSelect;
		}

	}
}
