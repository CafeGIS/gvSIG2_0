package com.iver.cit.gvsig.project.documents.contextMenu.actions;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;

public class CopyDocumentContextMenuAction extends
		AbstractClipboardDocumentContextMenuAction {

	public int getOrder() {
		return 0;
	}

	public boolean isVisible(ProjectDocument item,
			ProjectDocument[] selectedItems) {
		return true;
	}

	public boolean isEnabled(ProjectDocument item,
			ProjectDocument[] selectedItems) {
		return selectedItems.length > 0;
	}

	public void execute(ProjectDocument item, ProjectDocument[] selectedItems) {
		ProjectExtension projectExtension = (ProjectExtension)PluginServices.getExtension(ProjectExtension.class);
		Project project = projectExtension.getProject();
		String data;
		try {
			data = project.exportToXML(selectedItems);
		} catch (SaveException e) {
			JOptionPane.showMessageDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>"+PluginServices.getText(this,"No_ha_sido_posible_realizar_la_operacion")+"</html>",//Mensaje
					PluginServices.getText(this,"copiar"),//titulo
					JOptionPane.ERROR_MESSAGE
					);
			return;
		}
		PluginServices.putInClipboard(data);
		project.setModified(true);
	}

	public String getText() {
		return PluginServices.getText(this, "copiar");
	}

}
