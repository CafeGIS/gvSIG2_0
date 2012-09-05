package com.iver.cit.gvsig.project.documents.contextMenu.actions;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;

public class CutDocumentContextMenuAction extends
		AbstractClipboardDocumentContextMenuAction {
	public int getOrder() {
		return 1;
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
					PluginServices.getText(this,"cortar"),//titulo
					JOptionPane.ERROR_MESSAGE
					);
			return;
		}
		PluginServices.putInClipboard(data);

		for (int i =0;i<selectedItems.length;i++) {
			if (selectedItems[i].isLocked()) {
				JOptionPane.showMessageDialog(
						(Component)PluginServices.getMainFrame(),
						PluginServices.getText(this, "locked_element_it_cannot_be_deleted") + ": " +selectedItems[i].getName()
				);
				return;

			}
		}


    	int option=JOptionPane.showConfirmDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"desea_borrar_el_documento"));
    	if (option!=JOptionPane.OK_OPTION) {
    		return;
    	}

		this.removeDocuments(selectedItems,project);
		project.setModified(true);

	}

	public String getText() {
		return PluginServices.getText(this, "cortar");
	}

	private boolean removeDocuments(ProjectDocument[] selectedItems,Project project) {
		ProjectDocument element;
		int index;
		for (int i=selectedItems.length-1;i>=0;i--) {

			element = selectedItems[i];


			if (element.isLocked()) {
				JOptionPane.showMessageDialog(
						(Component)PluginServices.getMainFrame(),
						PluginServices.getText(this, "locked_element_it_cannot_be_deleted") + ": " +element.getName()
				);
				//return false;
			} else {
				PluginServices.getMDIManager().closeSingletonWindow(element);
				project.delDocument(element);
			}
		}
		return true;
	}


}
