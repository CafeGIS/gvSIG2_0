package com.iver.cit.gvsig.project.documents;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.ImageIcon;

import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionBuilder;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.contextMenu.AbstractDocumentContextMenuAction;
import com.iver.utiles.XMLEntity;


/**
 * Factory of ProjectDocument.
 *
 * @author Vicente Caballero Navarro
 */
public abstract class ProjectDocumentFactory implements ExtensionBuilder {
    /**
     * Returns the load priority of the ProjectDocument.
     *
     * @return Priority.
     */
    public int getPriority() {
        return 10;
    }

    /**
     * Returns the priority of the ProjectDocument in the project window list.
     *
     * @return Priority.
     */
    public int getListPriority() {
        return 10;
    }

    /**
     * Returns image of button.
     *
     * @return Image button.
     */
    public abstract ImageIcon getButtonIcon();

    /**
     * Returns image of selected button.
     *
     * @return Image button.
     */
    public abstract ImageIcon getSelectedButtonIcon();

    /**
     * Returns the name of ProjectDocument.
     *
     * @return Name of ProjectDocument.
     */
    public String getNameType() {
        return PluginServices.getText(this, "documento");
    }

    /**
     * Create a new ProjectDocument.
     *
     * @param project Opened project.
     *
     * @return ProjectDocument.
     */
    public abstract ProjectDocument create(Project project);

    /**
     * Introdece a gui to be able from the characteristics that we want a ProjectDocument
     *
     * @param project present Project.
     *
     * @return new ProjectDocument.
     */
    public ProjectDocument createFromGUI(Project project) {
        return create(project);
    }

    /**
     * Returns the name of registration in the point of extension.
     *
     * @return Name of registration
     */
    public abstract String getRegisterName();

    /**
     * Create a ProjectDocumentFactory.
     *
     * @return ProjectDocumentFactory.
     */
    public Object create() {
        return this;
    }

    /**
     * Create a ProjectDocumentFactory.
     *
     * @param args
     *
     * @return ProjectDocumentFactory.
     */
    public Object create(Object[] args) {
        return this;
    }

    /**
     * Create a ProjectDocumentFactory.
     *
     * @param args
     *
     * @return ProjectDocumentFactory.
     */
    public Object create(Map args) {
        return this;
    }

	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            DocumentFactory of register.
	 * @param alias
	 *            Alias.
	 */
    public static void register(String registerName,
			ProjectDocumentFactory obj,
			String alias) {
    	ToolsLocator.getExtensionPointManager().add("Documents").append(
				registerName, alias, obj);
        ProjectDocument.NUMS.put(registerName,new Integer(0));
    }

	/**
	 * Registers in the points of extension the Factory
	 * 
	 * @param registerName
	 *            Register name.
	 * @param obj
	 *            DocumentFactory of register.
	 */
    public static void register(String registerName, ProjectDocumentFactory obj) {
    	register(registerName, obj, "");
    }

    /**
     * Register an action for the document.
     *
     * This actions will be appears in the context menu of
     * the project document list.
     *
     *
     * @param documentRegisterName
     * @param actionName
     * @param action
     */
    public static void registerAction(String documentRegisterName, String actionName, AbstractDocumentContextMenuAction action) {
    	ExtensionPointManager epManager = ToolsLocator.getExtensionPointManager();
    	String epName ="DocumentActions_"+documentRegisterName;
    	epManager.add(epName, "Actions for document " + documentRegisterName)
				.append(actionName, "", action);
    }
    /**
     * Try to resolve the documents conflicts before perform an import action
     *
     * @param root the XML document
     * @param project the project
     * @param conflicts Hashtable
     * 			- keys: documents register name.
     * 			- values: Hashtable
     * 			(keys = index of the child in the xml group of the XML,
     * 			values = XMLEntity in conflict)
     *
     * @return true if all the conflicts are resolved , else false
     */
    public abstract boolean resolveImportXMLConflicts(XMLEntity root,Project project, Hashtable conflicts);
    /**
    * Return true if the name exists to another document.
    *
    * @param project
    * @param documentName
    *
    * @return True if the name exists.
    */
   public boolean existName(Project project, String documentName) {
       ArrayList documentList = project.getDocumentsByType(getRegisterName());

       for (int i = 0; i < documentList.size(); i++) {
           ProjectDocument pd = (ProjectDocument) documentList.get(i);
           String title = pd.getName();

           if (title.compareTo(documentName) == 0) {
               return true;
           }
       }

       return false;
   }
}
