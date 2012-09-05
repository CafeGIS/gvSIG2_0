package com.iver.cit.gvsig.project.documents.contextMenu.actions;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.gui.ProjectWindow;

public class PasteDocumentContextMenuAction extends
		AbstractClipboardDocumentContextMenuAction {

	public int getOrder() {
		return 2;
	}

	public boolean isVisible(ProjectDocument item, ProjectDocument[] selectedItems) {
		return true;
	}

	public boolean isEnabled(ProjectDocument item, ProjectDocument[] selectedItems) {
		String sourceString = PluginServices.getFromClipboard();
		if (sourceString == null) return false;

		ProjectExtension projectExtension = (ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
		Project project = projectExtension.getProject();
		String docType = ((ProjectWindow)projectExtension.getProjectWindow()).getDocumentSelected();

		return project.isValidXMLForImport(sourceString,docType);
	}


	public void execute(ProjectDocument item, ProjectDocument[] selectedItems) {
		String sourceString = PluginServices.getFromClipboard();
		if (sourceString == null) return;

		ProjectExtension projectExtension = (ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
		Project project = projectExtension.getProject();
		String docType = ((ProjectWindow)projectExtension.getProjectWindow()).getDocumentSelected();

		try {
			project.importFromXML(sourceString,docType);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>"+PluginServices.getText(this,"No_ha_sido_posible_realizar_la_operacion")+"</html>",//Mensaje
					PluginServices.getText(this,"pegar"),//titulo
					JOptionPane.ERROR_MESSAGE
					);
		}
		project.setModified(true);
	}

	public String getText() {
		return PluginServices.getText(this, "pegar");
	}



}
