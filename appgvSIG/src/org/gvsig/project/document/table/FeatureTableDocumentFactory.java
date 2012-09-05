package org.gvsig.project.document.table;

import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import org.gvsig.AppGvSigLocator;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.serverexplorer.filesystem.swing.FilesystemExplorerWizardPanel;
import org.gvsig.tools.ToolsLocator;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CopyDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CutDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.PasteDocumentContextMenuAction;
import com.iver.utiles.XMLEntity;


/**
 * Factory of Table.
 *
 * @author Vicente Caballero Navarro
 */
public class FeatureTableDocumentFactory extends ProjectDocumentFactory {
    public static String registerName = "ProjectTable";

    static {
  		// A�adimos nuestra extension para el tratamiento de la apertura de ficheros
  		// dentro de gvSIG
		//  		ToolsLocator.getExtensionPointManager().add("FileTableOpenDialog", "")
		//				.append("FileOpenTable", "", TableFileOpen.class);
  		ToolsLocator.getExtensionPointManager().add("FileTableOpenDialog", "")
		.append("FileOpenTable", "",
						FilesystemExplorerWizardPanel.class);

    }

    /**
     * Returns image of button.
     *
     * @return Image button.
     */
    public ImageIcon getButtonIcon() {
        return PluginServices.getIconTheme().get("document-table-icon");
    }

    /**
     * Returns image of selected button.
     *
     * @return Image button.
     */
    public ImageIcon getSelectedButtonIcon() {
        return PluginServices.getIconTheme().get("document-table-icon-sel");
    }

    /**
     * Introduce a gui to be able from the characteristics that we want a ProjectDocument
     *
     * @param project present Project.
     *
     * @return new ProjectDocument.
     */
    public ProjectDocument createFromGUI(Project project) {
        try {
            AddLayerDialog fopen = new AddLayerDialog(PluginServices.getText(this,
                        "Nueva_tabla"));


			//            FileOpenWizard fod = new FileOpenWizard("FileTableOpenDialog", false);
			//            fod.setTitle(PluginServices.getText(this, "Tablas"));

			// FilesystemExplorerTableWizardPanel fod = new
			// FilesystemExplorerTableWizardPanel();
			// fod.setTabName(PluginServices.getText(this, "Tablas"));


//            DataBaseOpenDialog dbod = new DataBaseOpenDialog();
			//            dbod.setClasses(new Class[] { DBStoreParameters.class });
			List<WizardPanel> wizards = AppGvSigLocator.getAppGvSigManager()
					.getWizardPanels();
			WizardPanel panel;
			Iterator<WizardPanel> iter = wizards.iterator();
			while (iter.hasNext()) {
				panel = iter.next();
				fopen.addWizardTab(panel.getTabName(), panel);
				panel.initWizard();
			}
//            fopen.addTab(PluginServices.getText(this, "base_datos"), dbod);
//            WizardVectorialDB wp=new WizardVectorialDB();
//            wp.initWizard();
//            fopen.addWizardTab(wp.getTabName(), wp);
            PluginServices.getMDIManager().addWindow(fopen);
//            wp.setMapCtrl(null);
//    		wp.execute();
    		DataManager dm = DALLocator.getDataManager();
            if (fopen.isAccepted()) {
            	panel = (WizardPanel) fopen.getSelectedTab();

                panel.execute();

            }
		} catch (Exception e) {
			NotificationManager.addError(e);
		}

        return null;
    }

    /**
     * Create a new ProjectTable
     *
     * @param baseName name
     *
     * @return ProjectTable.
     */
    public static FeatureTableDocument createTable(String name, FeatureStore fs) {
        FeatureTableDocument t = new FeatureTableDocument(fs);

//        if (fs != null) {
//            t.setModel(fs);
//
////            try {
////                t.createAlias();
////            } catch (ReadException e) {
////				e.printStackTrace();
////			}
//        }

        t.setName(name);
        t.setCreationDate(DateFormat.getInstance().format(new Date()));
        int numTables=(ProjectDocument.NUMS.get(registerName)).intValue();
        ProjectDocument.NUMS.put(registerName,new Integer(numTables++));

        return t;
    }

    /**
     * Returns the name of registration in the point of extension.
     *
     * @return Name of registration
     */
    public String getRegisterName() {
        return registerName;
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new FeatureTableDocumentFactory(),
            "com.iver.cit.gvsig.project.ProjectTable");

        registerAction(registerName,"copy",new CopyDocumentContextMenuAction());
        registerAction(registerName,"cut",new CutDocumentContextMenuAction());
        registerAction(registerName,"paste",new PasteDocumentContextMenuAction());

        PluginServices.getIconTheme().registerDefault(
        		"document-table-icon",
        		AddLayer.class.getClassLoader().getResource("images/tablas.png")
        	);//
        PluginServices.getIconTheme().registerDefault(
        		"document-table-icon-sel",
        		AddLayer.class.getClassLoader().getResource("images/tablas_sel.png")
        	);

        PluginServices.getIconTheme().registerDefault(
        		"edit-copy",
        		AddLayer.class.getClassLoader().getResource("images/editcopy.png")
        	);//
        PluginServices.getIconTheme().registerDefault(
        		"edit-cut",
        		AddLayer.class.getClassLoader().getResource("images/editcut.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"edit-paste",
        		AddLayer.class.getClassLoader().getResource("images/editpaste.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"edit-delete",
        		AddLayer.class.getClassLoader().getResource("images/editdelete.png")
        	);
    }

    /**
     * Returns the name of ProjectDocument.
     *
     * @return Name of ProjectDocument.
     */
    public String getNameType() {
        return PluginServices.getText(this, "Tabla");
    }


    /**
     * Create a new ProjectDocument.
     *
     * @param project Opened project.
     *
     * @return ProjectDocument.
     */
    public ProjectDocument create(Project project) {
        FeatureTableDocument pt = null;

        pt = FeatureTableDocumentFactory.createTable("", null);
        pt.setProject(project,0);

        pt.setProjectDocumentFactory(this);

        return pt;
    }

    /**
     * Returns the priority of de ProjectDocument.
     *
     * @return Priority.
     */
    public int getPriority() {
        return 1;
    }

	public boolean resolveImportXMLConflicts(XMLEntity root, Project project, Hashtable conflicts) {
		return true;
	}
}
