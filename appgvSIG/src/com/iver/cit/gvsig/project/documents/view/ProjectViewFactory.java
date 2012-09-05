package com.iver.cit.gvsig.project.documents.view;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CopyDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CutDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.PasteDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.legend.gui.SingleSymbol;
import com.iver.cit.gvsig.project.documents.view.legend.gui.VectorialInterval;
import com.iver.cit.gvsig.project.documents.view.legend.gui.VectorialUniqueValue;
import com.iver.utiles.XMLEntity;


/**
 * Factory of View.
 *
 * @author Vicente Caballero Navarro
 */
public class ProjectViewFactory extends ProjectDocumentFactory {
    public static String registerName = "ProjectView";


    /**
     * Returns image of button.
     *
     * @return Image button.
     */
    public ImageIcon getButtonIcon() {
		return PluginServices.getIconTheme().get("document-view-icon");
    }

    /**
     * Returns image of selected button.
     *
     * @return Image button.
     */
    public ImageIcon getSelectedButtonIcon() {
		return PluginServices.getIconTheme().get("document-view-icon-sel");
    }

    /**
     * Create a new ProjectDocument.
     *
     * @param project Opened project.
     *
     * @return ProjectDocument.
     */
    public ProjectDocument create(Project project) {
        String viewName = "";
        String aux = PluginServices.getText(this, "untitled");
        int numViews=((Integer)ProjectDocument.NUMS.get(registerName)).intValue();

        viewName = aux + " - " + numViews++;

        if (project != null) {
            // Buscamos si alguna vista ya ten�a este nombre:
            while (existName(project, viewName)) {
                viewName = aux + " - " + numViews++;
            }
        }
        ProjectDocument.NUMS.put(registerName,new Integer(numViews));
        ProjectView vista = createView(viewName);
        vista.setProject(project, 0);
        vista.setProjectDocumentFactory(this);

        return vista;
    }

    /**
     * Create a new ProjectView.
     *
     * @param baseName name
     *
     * @return ProjectView.
     */
    private static ProjectView createView(String viewName) {
    	ProjectView v = new ProjectView();
		MapContext viewMapContext = new MapContext(new ViewPort(Project
				.getDefaultProjection()));
		ViewPort vp = viewMapContext.getViewPort();
		vp.setBackColor(View.getDefaultBackColor());
		vp.setDistanceUnits(Project.getDefaultDistanceUnits());
		vp.setDistanceArea(Project.getDefaultDistanceArea());
		vp.setMapUnits(Project.getDefaultMapUnits());

		v.setMapContext(viewMapContext);
		v.setMapOverViewContext(new MapContext(null));

		/*
		 * jaume. ?no puedo definir color de fondo en localizador?
		 *
		 * v.getMapOverViewContext().setProjection(v.getMapContext().getProjection());
		 * v.getMapOverViewContext(). getViewPort(). setBackColor(
		 * Project.getDefaultMapOverViewBackColor() );
		 *
		 */
		v.setName(viewName);
		v.setCreationDate(DateFormat.getInstance().format(new Date()));

        return v;
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
     * Returns the name of ProjectDocument.
     *
     * @return Name of ProjectDocument.
     */
    public String getNameType() {
        return PluginServices.getText(this, "Vista");
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new ProjectViewFactory(),
            "com.iver.cit.gvsig.project.ProjectView");

        registerAction(registerName,"copy",new CopyDocumentContextMenuAction());
        registerAction(registerName,"cut",new CutDocumentContextMenuAction());
        registerAction(registerName,"paste",new PasteDocumentContextMenuAction());

        PluginServices.getIconTheme().registerDefault(
        		"document-view-icon",
        		ProjectView.class.getClassLoader().getResource("images/Vista.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"document-view-icon-sel",
        		ProjectView.class.getClassLoader().getResource("images/Vista_sel.png")
        	);



        PluginServices.getIconTheme().registerDefault(
        		"cursor-query-distance",
        		MapControl.class.getClassLoader().getResource("images/RulerCursor.gif")
        	);

        PluginServices.getIconTheme().registerDefault(
        		"cursor-query-information",
        		MapControl.class.getResource("images/InfoCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"cursor-hiperlink",
        		MapControl.class.getResource("images/LinkCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"cursor-zoom-in",
        		MapControl.class.getClassLoader().getResource("images/ZoomInCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"cursor-zoom-in",
        		MapControl.class.getClassLoader().getResource("images/ZoomInCursor.gif")
        	);


        PluginServices.getIconTheme().registerDefault(
           		"single-symbol",
           		SingleSymbol.class.getClassLoader().getResource("images/single-symbol.png")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"vectorial-interval",
           		VectorialInterval.class.getClassLoader().getResource("images/Intervalos.png")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"vectorial-unique-value",
           		VectorialUniqueValue.class.getClassLoader().getResource("images/ValoresUnicos.png")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"vectorial-unique-value",
           		VectorialUniqueValue.class.getClassLoader().getResource("images/ValoresUnicos.png")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"crux-cursor",
           		MapControl.class.getClassLoader().getResource("images/CruxCursor.png")
           	);


    }

    /**
     * Returns the priority of de ProjectDocument.
     *
     * @return Priority.
     */
    public int getPriority() {
        return 0;
    }

	public boolean resolveImportXMLConflicts(XMLEntity root, Project project, Hashtable conflicts) {
		Hashtable viewsConflits = (Hashtable)conflicts.get(this.getRegisterName());
		Hashtable tablesConflits = (Hashtable)conflicts.get(FeatureTableDocumentFactory.registerName);
		XMLEntity xmlTables = root.firstChild("type",FeatureTableDocumentFactory.registerName);


		if (viewsConflits != null && viewsConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>"+
						PluginServices.getText(this,"conflicto_de_nombres_de_vistas_al_pegar") + "<br>" +
						PluginServices.getText(this,"debera_introducir_nombres_para_las_vistas_a_pegar") + "<br>" +
						PluginServices.getText(this,"no_se_pegaran_las_tablas") + "<br>" +
						PluginServices.getText(this,"desea_continuar") +
					"</html>",
					PluginServices.getText(this,"pegar_vistas"),
					JOptionPane.YES_NO_OPTION
					);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			Enumeration en = viewsConflits.elements();
			while (en.hasMoreElements()) {
				XMLEntity view = (XMLEntity)en.nextElement();
				String newName = JOptionPane.showInputDialog(
						(Component)PluginServices.getMainFrame(),
						"<html>"+
							PluginServices.getText(this,"introduzca_nuevo_nombre_para_la_vista") +" "+  view.getStringProperty("name") + ":" +
						"</html>", //Mensaje
						view.getStringProperty("name") //Valor por defecto
						);
				if (newName == null) {
					JOptionPane.showMessageDialog(
							(Component)PluginServices.getMainFrame(),
							"<html>"+PluginServices.getText(this,"operacion_cancelada")+"</html>",//Mensaje
							PluginServices.getText(this,"pegar_vistas"),//titulo
							JOptionPane.ERROR_MESSAGE
							);
				} else if (newName.equalsIgnoreCase(view.getStringProperty("name")) ) {
					JOptionPane.showMessageDialog(
							(Component)PluginServices.getMainFrame(),
							"<html>"+
								PluginServices.getText(this,"operacion_cancelada") +":<br>" +
								PluginServices.getText(this,"nombre_no_valido")+
							"</html>",//Mensaje
							PluginServices.getText(this,"pegar_vistas"),//FIXME: getText
							JOptionPane.ERROR_MESSAGE
							);
					return false;
				}
				view.setName(newName);
			}
			if (xmlTables != null) xmlTables.removeAllChildren();
			tablesConflits = null;
		}

		if (tablesConflits != null && tablesConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>" +
						PluginServices.getText(this,"conflicto_de_nombres_de_tablas_al_pegar") + "<br>" +
						PluginServices.getText(this,"no_se_pegaran_las_tablas") + "<br>" +
						PluginServices.getText(this,"desea_continuar") +
					"</html>", //Mensaje
					PluginServices.getText(this,"pegar_vistas"),//FIXME: getText
					JOptionPane.YES_NO_OPTION
					);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			xmlTables.removeAllChildren();
		}

		return true;
	}
}
