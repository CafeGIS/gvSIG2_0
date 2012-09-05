package com.iver.cit.gvsig.project.documents.layout;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.project.document.table.FeatureTableDocumentFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.AddLayer;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CopyDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.CutDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.contextMenu.actions.PasteDocumentContextMenuAction;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.utiles.XMLEntity;


/**
 * Factory of maps.
 *
 * @author Vicente Caballero Navarro
 */
public class ProjectMapFactory extends ProjectDocumentFactory {
    public static String registerName = "ProjectMap";

    /**
     * Returns image of button.
     *
     * @return Image button.
     */
    public ImageIcon getButtonIcon() {
//        return new ImageIcon(ProjectMap.class.getClassLoader().getResource("images/mapas.png"));
        return PluginServices.getIconTheme().get("document-map-icon");
    }

    /**
     * Returns image of selected button.
     *
     * @return Image button.
     */
    public ImageIcon getSelectedButtonIcon() {
//        return new ImageIcon(ProjectMap.class.getClassLoader().getResource("images/mapas_sel.png"));
        return PluginServices.getIconTheme().get("document-map-icon-sel");
    }

    /**
     * Create a new ProjectDocument.
     *
     * @param project Opened project.
     *
     * @return ProjectDocument.
     */
//    public ProjectDocument create(Project project) {
//        String layoutName = "";
//        String aux = PluginServices.getText(this, "untitled");
//        layoutName = aux + " - " + ProjectMap.numMaps++;
//
//        if (project != null) {
//            // Buscamos si alguna vista ya tenía este nombre:
//            while (existLayoutName(project, layoutName)) {
//                layoutName = aux + " - " + ProjectView.numViews++;
//            }
//        }
//
//        ProjectMap map = createMap(layoutName);
//        map.setModel(new Layout());
//        map.getModel().setProjectMap(map);
//        map.setProject(project, 0);
//        map.setProjectDocumentFactory(this);
//
//        return map;
//    }
    public ProjectDocument create(Project project) {
    	String mapName = "";
    	String aux = PluginServices.getText(this, "untitled");
    	int numMaps=((Integer)ProjectDocument.NUMS.get(registerName)).intValue();
    	mapName = aux + " - " + numMaps++;
    	if (project != null) {
            while (existName(project, mapName)) {
                mapName = aux + " - " + numMaps++;
            }
        }
    	ProjectDocument.NUMS.put(registerName,new Integer(numMaps));
        ProjectMap map = createMap(mapName);
        map.setModel(new Layout());
        map.getModel().setProjectMap(map);
        map.setProject(project, 0);
        map.setProjectDocumentFactory(this);

        return map;
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
        return PluginServices.getText(this, "Mapa");
    }

    /**
     * Create a ne ProjectMap
     *
     * @param baseName name
     *
     * @return ProjectMap.
     */
    private static ProjectMap createMap(String baseName) {
        ProjectMap m = new ProjectMap();
        m.setName(baseName);
        m.setCreationDate(DateFormat.getInstance().format(new Date()));
        return m;
    }

    /**
     * Registers in the points of extension the Factory with alias.
     *
     */
    public static void register() {
        register(registerName, new ProjectMapFactory(),
            "com.iver.cit.gvsig.project.ProjectMap");

        registerAction(registerName,"copy",new CopyDocumentContextMenuAction());
        registerAction(registerName,"cut",new CutDocumentContextMenuAction());
        registerAction(registerName,"paste",new PasteDocumentContextMenuAction());

        PluginServices.getIconTheme().registerDefault(
        		"document-map-icon",
        		AddLayer.class.getClassLoader().getResource("images/mapas.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"document-map-icon-sel",
        		AddLayer.class.getClassLoader().getResource("images/mapas_sel.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"neresize-icon",
        		AddLayer.class.getClassLoader().getResource("images/NEResize.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"eresize-icon",
        		AddLayer.class.getClassLoader().getResource("images/EResize.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"nresize-icon",
        		AddLayer.class.getClassLoader().getResource("images/NResize.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"move-icon",
        		AddLayer.class.getClassLoader().getResource("images/Move.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"sereresize-icon",
        		AddLayer.class.getClassLoader().getResource("images/SEResize.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"symboltag-icon",
        		AddLayer.class.getClassLoader().getResource("images/symbolTag.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"move-icon",
        		AddLayer.class.getClassLoader().getResource("images/Move.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"numero-icon",
        		AddLayer.class.getClassLoader().getResource("images/numero.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"barra1-icon",
        		AddLayer.class.getClassLoader().getResource("images/barra1.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"barra2-icon",
        		AddLayer.class.getClassLoader().getResource("images/barra2.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"barra3-icon",
        		AddLayer.class.getClassLoader().getResource("images/barra3.png")
        	);
        PluginServices.getIconTheme().registerDefault(
          		"text-left-icon",
           		AddLayer.class.getClassLoader().getResource("images/left.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
        		"text-center-v-icon",
        		AddLayer.class.getClassLoader().getResource("images/centerV.PNG")
        	);
        PluginServices.getIconTheme().registerDefault(
           		"text-right-icon",
           		AddLayer.class.getClassLoader().getResource("images/right.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"left-rotation-icon",
           		AddLayer.class.getClassLoader().getResource("images/leftrotation.png")
           	);
        PluginServices.getIconTheme().registerDefault(
           		"text-up-icon",
           		AddLayer.class.getClassLoader().getResource("images/up.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
        		"text-center-h-icon",
        		AddLayer.class.getClassLoader().getResource("images/centerH.PNG")
        	);
        PluginServices.getIconTheme().registerDefault(
          		"text-down-icon",
           		AddLayer.class.getClassLoader().getResource("images/down.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distup-icon",
           		AddLayer.class.getClassLoader().getResource("images/distUp.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distcenterh-icon",
           		AddLayer.class.getClassLoader().getResource("images/distCenterH.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distdown-icon",
           		AddLayer.class.getClassLoader().getResource("images/distDown.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distleft-icon",
           		AddLayer.class.getClassLoader().getResource("images/distLeft.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distcenterv-icon",
           		AddLayer.class.getClassLoader().getResource("images/distCenterV.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-distright-icon",
           		AddLayer.class.getClassLoader().getResource("images/distRight.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-size-width-icon",
           		AddLayer.class.getClassLoader().getResource("images/sizeWidth.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-size-height-icon",
           		AddLayer.class.getClassLoader().getResource("images/sizeHeight.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-size-other-icon",
           		AddLayer.class.getClassLoader().getResource("images/sizeOther.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
          		"text-space-right-icon",
           		AddLayer.class.getClassLoader().getResource("images/spaceRight.PNG")
           	);
        PluginServices.getIconTheme().registerDefault(
        		"text-inlayout-icon",
        		AddLayer.class.getClassLoader().getResource("images/inLayout.PNG")
        	);

        //Tools
        PluginServices.getIconTheme().registerDefault(
        		"rect-select-cursor",
        		MapControl.class.getResource("images/RectSelectCursor.gif")
        	);

        PluginServices.getIconTheme().registerDefault(
        		"circle-cursor",
        		AddLayer.class.getClassLoader().getResource("images/CircleCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"line-cursor",
        		AddLayer.class.getClassLoader().getResource("images/LineCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"point-cursor",
        		AddLayer.class.getClassLoader().getResource("images/PointCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"poligon-cursor",
        		AddLayer.class.getClassLoader().getResource("images/PoligonCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"rectangle-cursor",
        		AddLayer.class.getClassLoader().getResource("images/RectangleCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"crux-cursor",
        		AddLayer.class.getClassLoader().getResource("images/CruxCursor.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"layout-hand-icon",
        		AddLayer.class.getClassLoader().getResource("images/LayoutHand.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"zoom-in-cursor",
        		AddLayer.class.getClassLoader().getResource("images/ZoomInCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"hand-icon",
        		AddLayer.class.getClassLoader().getResource("images/Hand.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"zoom-out-cursor",
        		AddLayer.class.getClassLoader().getResource("images/ZoomOutCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"layout-zoom-in-cursor",
        		AddLayer.class.getClassLoader().getResource("images/LayoutZoomInCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"layout-zoom-out-cursor",
        		AddLayer.class.getClassLoader().getResource("images/LayoutZoomOutCursor.gif")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"right-rotation-icon",
        		AddLayer.class.getClassLoader().getResource("images/rightrotation.png")
        	);
        PluginServices.getIconTheme().registerDefault(
        		"point-select-cursor",
        		MapControl.class.getResource("images/PointSelectCursor.gif")
        	);


    }

    /**
     * Returns the priority of de ProjectDocument.
     *
     * @return Priority.
     */
    public int getPriority() {
        return 2;
    }

	public boolean resolveImportXMLConflicts(XMLEntity root, Project project, Hashtable conflicts) {
		XMLEntity xmlMaps = root.firstChild("type",this.getRegisterName());
		XMLEntity xmlViews = root.firstChild("type",ProjectViewFactory.registerName);
		XMLEntity xmlTables = root.firstChild("type",FeatureTableDocumentFactory.registerName);

		Hashtable mapsConflits = (Hashtable)conflicts.get(ProjectMapFactory.registerName);

		Hashtable viewsConflits = (Hashtable)conflicts.get(ProjectViewFactory.registerName);

		Hashtable tablesConflits = (Hashtable)conflicts.get(FeatureTableDocumentFactory.registerName);


		if (mapsConflits != null && mapsConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>"+
						PluginServices.getText(this,"conflicto_de_nombres_de_mapas_al_pegar") + "<br>" +
						PluginServices.getText(this,"debera_introducir_nombres_para_los_mapas_a_pegar") + "<br>" +
					"</html>", //Mensaje
					PluginServices.getText(this,"pegar_mapas"),//titulo
					JOptionPane.YES_NO_OPTION
					);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			Enumeration en = mapsConflits.elements();
			while (en.hasMoreElements()) {
				XMLEntity map = (XMLEntity)en.nextElement();
				String newName = JOptionPane.showInputDialog(
						(Component)PluginServices.getMainFrame(),
						"<html>"+
							PluginServices.getText(this,"nuevo_nombre_para_el_mapa") +" "+  map.getStringProperty("name") + ":" +
					    "</html>", //Mensaje
						map.getStringProperty("name") //Valor por defecto
						);
				if (newName == null) {
					JOptionPane.showMessageDialog(
							(Component)PluginServices.getMainFrame(),
							"<html>"+PluginServices.getText(this,"operacion_cancelada")+"</html>",//Mensaje
							PluginServices.getText(this,"pegar_mapas"),//titulo
							JOptionPane.ERROR_MESSAGE
							);
				} else if (newName.equalsIgnoreCase(map.getStringProperty("name")) ) {
					JOptionPane.showMessageDialog(
							(Component)PluginServices.getMainFrame(),
							"<html>"+
								PluginServices.getText(this,"operacion_cancelada") +":<br>" +
								PluginServices.getText(this,"nombre_no_valido")+
							"</html>",//Mensaje
							PluginServices.getText(this,"pegar_mapas"),//titulo
							JOptionPane.ERROR_MESSAGE
							);
					return false;
				}
				map.setName(newName);
			}
		}

		if (viewsConflits != null && viewsConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>"+
						PluginServices.getText(this,"conflicto_de_nombres_de_vistas_al_pegar") + "<br>" +
						PluginServices.getText(this,"no_se_pegaran_las_vistas_del_conflicto") + "<br>" +
						PluginServices.getText(this,"desea_continuar") +
					"</html>",
					PluginServices.getText(this,"pegar_mapas"),//titulo
					JOptionPane.YES_NO_OPTION
					);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			// Eliminamos las vistas del xml que no vamos a importar

			// Esto me devuelve los indices en orden inverso
			int[] indexes = this.getIndexOfConflict(viewsConflits);
			for (int i=0;i < indexes.length;i++) {
				xmlViews.removeChild(indexes[i]);
			}
			viewsConflits = null;

		}


		if (tablesConflits != null && tablesConflits.size() > 0) {
			int option = JOptionPane.showConfirmDialog(
					(Component)PluginServices.getMainFrame(),
					"<html>" +
						PluginServices.getText(this,"conflito_de_nombres_de_tablas_al_pegar") + "<br>" +
						PluginServices.getText(this,"no_se_pegaran_las_tablas") + "<br>" +
						PluginServices.getText(this,"desea_continuar") +
					"</html>", //Mensaje
					PluginServices.getText(this,"pegar_mapas"),
					JOptionPane.YES_NO_OPTION
					);
			if (option != JOptionPane.YES_OPTION) {
				return false;
			}
			xmlTables.removeAllChildren();
		}


		return true;
	}

	/**
	 * Devuelve las claves de conflits ordenados
	 * en orden inverso. Las claves se esperan que
	 * sean instancias de Integer
	 */
	private int[] getIndexOfConflict(Hashtable conflits) {
		Object[] tmpArray = conflits.keySet().toArray();
		Arrays.sort(tmpArray,new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Integer)o2).intValue() - ((Integer)o1).intValue();
			}
		}
		);
		int[] indexes = new int[] {tmpArray.length};
		for (int i = 0;i< tmpArray.length;i++) {
			indexes[i] = ((Integer)tmpArray[i]).intValue();
		}
		return indexes;


	}
}
