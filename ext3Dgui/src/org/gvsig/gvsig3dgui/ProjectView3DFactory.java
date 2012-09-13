package org.gvsig.gvsig3dgui;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.gvsig3d.map3d.ViewPort3D;
import org.gvsig.gvsig3d.map3d.layers.FLayers3D;
import org.gvsig.gvsig3dgui.view.ViewProperties3D;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.table.ProjectTableFactory;
import com.iver.utiles.XMLEntity;

/**
 * Factory of View3D.
 * 
 * @author Vicente Caballero Navarro
 */
public class ProjectView3DFactory extends ProjectDocumentFactory {
	public static String registerName = "ProjectView3D";

	private boolean createFromGUI = false;

	/**
	 * Returns image of button.
	 * 
	 * @return Image button.
	 */
	public ImageIcon getButtonIcon() {
		return new ImageIcon(this.getClass().getClassLoader().getResource(
				"images/ProjectView3D.png"));
	}

	/**
	 * Returns image of selected button.
	 * 
	 * @return Image button.
	 */
	public ImageIcon getSelectedButtonIcon() {
		return new ImageIcon(this.getClass().getClassLoader().getResource(
				"images/ProjectView3DSel.png"));
	}

	public ProjectDocument createFromGUI(Project project) {
		createFromGUI = true;
		ProjectDocument doc = create(project);
		createFromGUI = false;
		return doc;
	}

	/**
	 * Create a new ProjectDocument.
	 * 
	 * @param project
	 *            Opened project.
	 * 
	 * @return ProjectDocument.
	 */
	public ProjectDocument create(Project project) {
		String viewName = "";
        String aux = PluginServices.getText(this, "untitled");
        int numViews=((Integer)ProjectDocument.NUMS.get(registerName)).intValue();

        viewName = aux + " - " + numViews++;

        if (project != null) {
            // Buscamos si alguna vista ya tenía este nombre:
            while (existName(project, viewName)) {
                viewName = aux + " - " + numViews++;
            }
        }

        ProjectDocument.NUMS.put(registerName,new Integer(numViews));
		ProjectView3D vista = createView(viewName);
		
		if (vista != null)
			vista.setProject(project, 0);

		
		return vista;
	}

	/**
	 * Create a new ProjectView.
	 * 
	 * @param baseName
	 *            name
	 * 
	 * @return ProjectView.
	 */
	private ProjectView3D createView(String viewName) {
		ProjectView3D v = new ProjectView3D();

		// in the case of 3D layers, the map context is created by the FLayers'
		// constructor
		ViewPort vp = new ViewPort3D(Project.getDefaultProjection());
		FLayers layers = new FLayers3D(null, null, vp);
		v.setMapContext(layers.getMapContext());
		v.setMapOverViewContext(new MapContext(null));
		v.getMapOverViewContext().setProjection(
				v.getMapContext().getProjection());
		v.setName(viewName);
		v.setCreationDate(DateFormat.getInstance().format(new Date()));

		if (createFromGUI)
			setView3DProperties(v);

		return v;
	}

	private void setView3DProperties(ProjectView3D prView) {

		ViewProperties3D viewProperties = new ViewProperties3D(prView, true);
		PluginServices.getMDIManager().addWindow(viewProperties);

		// // choose spherical or flat view
		// int option = JOptionPane.showConfirmDialog(
		// (Component)PluginServices.getMainFrame(),
		// /*"<html>"+
		// PluginServices.getText(this,"desea_una_vista_esférica_(Si)") + "<br>"
		// +
		// PluginServices.getText(this,"o_plana_(No)") + "<br>" +
		// "</html>",
		// PluginServices.getText(this,"tipo_de_vista"),*/
		// "¿Desea una vista esférica (Si) o plana (No)?",
		// "Tipo de vista 3D",
		// JOptionPane.YES_NO_OPTION
		// );
		// int planetType;
		// if (option == JOptionPane.YES_OPTION)
		// planetType = PlanetType.SPHERICAL_MODE;
		// else
		// planetType = PlanetType.PLANE_MODE;
		// prView.setPlanetType(planetType);
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
		return PluginServices.getText(this, "Vista3D");
	}

	/**
	 * Registers in the points of extension the Factory with alias.
	 * 
	 */
	public static void register() {
		register(registerName, new ProjectView3DFactory());
	}

	/**
	 * Returns the priority of de ProjectDocument.
	 * 
	 * @return Priority.
	 */
	public int getPriority() {
		return 10;
	}

	/**
	 * Returns the priority of the Document in the ProjectWindow list.
	 * 
	 * @return Priority.
	 */
	public int getListPriority() {
		return 10; // so the 3D View appears before other document types
	}

	public boolean resolveImportXMLConflicts(XMLEntity root, Project project,
			Hashtable conflicts) {
		Hashtable viewsConflits = (Hashtable) conflicts.get(this
				.getRegisterName());
		Hashtable tablesConflits = (Hashtable) conflicts
				.get(ProjectTableFactory.registerName);
		XMLEntity xmlTables = root.firstChild("type",
				ProjectTableFactory.registerName);

		if (viewsConflits != null && viewsConflits.size() > 0) {
			int option = JOptionPane
					.showConfirmDialog(
							(Component) PluginServices.getMainFrame(),
							"<html>"
									+ PluginServices
											.getText(this,
													"conflicto_de_nombres_de_vistas_al_pegar")
									+ "<br>"
									+ PluginServices
											.getText(this,
													"debera_introducir_nombres_para_las_vistas_a_pegar")
									+ "<br>"
									+ PluginServices.getText(this,
											"no_se_pegaran_las_tablas")
									+ "<br>"
									+ PluginServices.getText(this,
											"desea_continuar") + "</html>",
							PluginServices.getText(this, "pegar_vistas"),
							JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			Enumeration en = viewsConflits.elements();
			while (en.hasMoreElements()) {
				XMLEntity view = (XMLEntity) en.nextElement();
				String newName = JOptionPane
						.showInputDialog(
								(Component) PluginServices.getMainFrame(),
								"<html>"
										+ PluginServices
												.getText(this,
														"introduzca_nuevo_nombre_para_la_vista")
										+ " " + view.getStringProperty("name")
										+ ":" + "</html>", // Mensaje
								view.getStringProperty("name") // Valor por
																// defecto
						);
				if (newName == null) {
					JOptionPane.showMessageDialog((Component) PluginServices
							.getMainFrame(), "<html>"
							+ PluginServices.getText(this,
									"operacion_cancelada") + "</html>",// Mensaje
							PluginServices.getText(this, "pegar_vistas"),// titulo
							JOptionPane.ERROR_MESSAGE);
				} else if (newName.equalsIgnoreCase(view
						.getStringProperty("name"))) {
					JOptionPane.showMessageDialog((Component) PluginServices
							.getMainFrame(), "<html>"
							+ PluginServices.getText(this,
									"operacion_cancelada") + ":<br>"
							+ PluginServices.getText(this, "nombre_no_valido")
							+ "</html>",// Mensaje
							PluginServices.getText(this, "pegar_vistas"),// FIXME:
																			// getText
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
				view.setName(newName);
			}
			if (xmlTables != null)
				xmlTables.removeAllChildren();
			tablesConflits = null;
		}

		if (tablesConflits != null && tablesConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component) PluginServices.getMainFrame(), "<html>"
							+ PluginServices.getText(this,
									"conflicto_de_nombres_de_tablas_al_pegar")
							+ "<br>"
							+ PluginServices.getText(this,
									"no_se_pegaran_las_tablas") + "<br>"
							+ PluginServices.getText(this, "desea_continuar")
							+ "</html>", // Mensaje
					PluginServices.getText(this, "pegar_vistas"),// FIXME:
																	// getText
					JOptionPane.YES_NO_OPTION);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			xmlTables.removeAllChildren();
		}

		return true;
	}
}
