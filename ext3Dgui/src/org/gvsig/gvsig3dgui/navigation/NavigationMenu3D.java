package org.gvsig.gvsig3dgui.navigation;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3d.navigation.NavigationMode;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.planets.CustomTerrainManipulator;
import org.gvsig.osgvp.planets.Planet;
import org.gvsig.osgvp.planets.PlanetViewer;
import org.gvsig.osgvp.viewer.Camera;
import org.gvsig.osgvp.viewer.OSGViewer;

import com.iver.ai2.gvsig3d.resources.ResourcesFactory;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiFrame.JMenuItem;
import com.iver.andami.ui.mdiFrame.JToolBarButton;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.gui.FPanelLocConfig;

public class NavigationMenu3D extends Extension {

	private boolean activa = true;

	private NavigationMode navMode;

	private boolean wireAct = false;

	JMenuItem myMenu = null;

	Icon iconVis = null;

	Icon iconNoVis = null;

	private ImageIcon iconButtonVis;

	private ImageIcon iconButtonNoVis;

	// cursors
	private Cursor navCursor;

	private Cursor zoomCursor;

	private Cursor panCursor;

	private Cursor azimCursor;

	private String buttonPath;

	private String imagesPath;

	public void execute(String actionCommand) {
		// System.out.println("EXECUTE");

		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();
		if (!(view instanceof View3D))
			return;
		// Casting to View3D
		View3D vista3D = (View3D) view;
		Component viewer = (Component) vista3D.getCanvas3d();

		// remove active tool in MapControl
		boolean resetCursor = true;

		navMode = vista3D.getNavMode();

		// Action for ZOOM_SELECT
		if (actionCommand.equals("PAN_SELECT")) {
			if (navMode != null) {
				viewer.setCursor(panCursor);
				navMode.SetRollMode();
			}
		} else if (actionCommand.equals("ZOOM_SELECT")) {
			if (navMode != null) {
				viewer.setCursor(zoomCursor);
				navMode.SetZoomMode();
			}
		} else if (actionCommand.equals("AZIMUT_SELECT")) {
			if (navMode != null) {
				viewer.setCursor(azimCursor);
				navMode.SetAzimutMode();
			}
		} else if (actionCommand.equals("DEFAULT_SELECT")) {
			if (navMode != null) {
				viewer.setCursor(navCursor);
				navMode.SetDefaultMode();
			}
		} else if (actionCommand.equals("WIRE_FRAME")) {
			resetCursor = false;
			wireAct = !wireAct;
			if (wireAct)
				vista3D.getCanvas3d().getOSGViewer().setPolygonMode(
						OSGViewer.PolygonModeType.GL_LINE);
			else
				vista3D.getCanvas3d().getOSGViewer().setPolygonMode(
						OSGViewer.PolygonModeType.GL_FILL);
			vista3D.getCanvas3d().repaint();
		} else if (actionCommand.equals("RESET_VIEW")) {
			resetCursor = false;
			MapContext3D map3D = (MapContext3D) vista3D.getMapControl()
					.getMapContext();
			FLayers layers = map3D.getLayers();
			Rectangle2D ext = layers.getFullExtent();
			if (ext == null) {
				// ext = new Rectangle2D.Double(-90,-180,90,180);
				Camera cam;
				cam = vista3D.getCamera();

				if (vista3D.getPlanet().getCoordinateSystemType() != Planet.CoordinateSystemType.PROJECTED) {
					cam.setViewByLookAt(vista3D.getPlanet()
							.getRadiusEquatorial() * 8.0, 0, 0, 0, 0, 0, 0, 0,
							1);

				} else {
					cam.setViewByLookAt(0, 0, 5000000 * 4.6, 0, 0, 0, 0, 1, 0);
				}
				vista3D.getCanvas3d().getOSGViewer().setCamera(cam);
			} else {
				map3D.zoomToExtent(ext);
			}
			vista3D.repaint();


			// UtilCoord.imprimeCamara(vista3D.getCamera());
			// System.out.println("CAMBIANDO CAMARAAAAAAAAA!!!!!!!!!");s
		} else if (actionCommand.equals("ACTIVE")) {
			resetCursor = false;
			if (vista3D.getPlanet().getCoordinateSystemType() != Planet.CoordinateSystemType.PROJECTED) {

				CustomTerrainManipulator ctm = ((PlanetViewer) vista3D
						.getCanvas3d().getOSGViewer())
						.getCustomTerrainManipulator();

				JToolBarButton b = (JToolBarButton) PluginServices
						.getMainFrame().getComponentByName("NORTH");
				if ((!ctm.getNorthOrientation()) && (myMenu != null)) {
					myMenu.setIcon(iconVis);

					if (b != null) {
						b.setIcon(iconButtonVis);
						b.setToolTip(PluginServices.getText(this, "Des_north")
								+ " "
								+ PluginServices.getText(this, "Active_north"));
					}
				} else {
					myMenu.setIcon(iconNoVis);

					if (b != null) {
						b.setIcon(iconButtonNoVis);
						b.setToolTip(PluginServices.getText(this, "Ac_north")
								+ " "
								+ PluginServices.getText(this, "Active_north"));
					}
				}

				ctm.setEnabledNorthOrientation(!ctm.getNorthOrientation());

				// System.out.println("orientacion " +
				// ctm.getNorthOrientation());

				vista3D.getCanvas3d().repaint();
			} else {
				JOptionPane.showMessageDialog(null, PluginServices.getText(
						this, "North_plane"));
			}

		}
		if (actionCommand.equals("CONFIG_LOCATOR")) {
			// Set up the map overview
			FPanelLocConfig m_panelLoc = new FPanelLocConfig(vista3D
					.getMapOverview());
			PluginServices.getMDIManager().addWindow(m_panelLoc);
			m_panelLoc.setPreferredSize(m_panelLoc.getSize());
		}

		// OJOOOOOOOOO CON ESTO Q ESTA COMENTADO PARA Q COMPILE HAY Q MIRAR COMO
		// ARREGLARLO
		if (resetCursor)
			vista3D.getMapControl().setCurrentMapTool(null);
	}

	public void initialize() {

		// Register new icons
		// Default manipulator
		
		PluginServices.getIconTheme().registerDefault(
				"default-manipulator-icon",
				this.getClass().getClassLoader().getResource(
				"images/DefaultManipulator.png"));
		// Roll manipulator
		PluginServices.getIconTheme().registerDefault(
				"roll-manipulator-icon",
				this.getClass().getClassLoader().getResource(
				"images/RollManipulator.png"));
		// Zoom manipulator
		PluginServices.getIconTheme()
		.registerDefault(
				"zoom-manipulator-icon",
				this.getClass().getClassLoader().getResource(
				"images/zoom.png"));
		// Azimut manipulator
		PluginServices.getIconTheme().registerDefault(
				"azimut-manipulator-icon",
				this.getClass().getClassLoader().getResource(
				"images/AzimutManipulator.png"));
		// North disable
		PluginServices.getIconTheme().registerDefault(
				"north-disable-icon",
				this.getClass().getClassLoader().getResource(
				"images/NorthDes.png"));
		// North disable
		PluginServices.getIconTheme().registerDefault(
				"global-zoom-icon",
				this.getClass().getClassLoader().getResource(
				"images/Global.png"));
		
		
		this.setActiva(true);

		String oldPath = ResourcesFactory.getExtPath();// Save the path.
		ResourcesFactory
				.setExtPath("/gvSIG/extensiones/com.iver.ai2.gvsig3dgui/images/");// my
		// new
		// path
		buttonPath = ResourcesFactory.getResourcesPath();
		ResourcesFactory.setExtPath(oldPath);// Restore the old path.
		System.out.println(oldPath);
		System.out.println(buttonPath);
		
		URL path;
		path = this.getClass().getClassLoader().getResource("images/");
		buttonPath = path.getPath(); 

		Image cursorImage = new ImageIcon(buttonPath + "/NavigationCursor.gif")
				.getImage();

		navCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
				new Point(16, 16), "");
		cursorImage = new ImageIcon(buttonPath + "/ZoomCursor.gif").getImage();
		zoomCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImage, new Point(16, 16), "");
		cursorImage = new ImageIcon(buttonPath + "/PanCursor.gif").getImage();
		panCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
				new Point(16, 16), "");
		cursorImage = new ImageIcon(buttonPath + "/AzimuthCursor.gif")
				.getImage();
		azimCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImage, new Point(16, 16), "");

		// Getting view3
		com.iver.andami.ui.mdiManager.IWindow view = PluginServices
				.getMDIManager().getActiveWindow();

		// JMenuBar jMenuBar = PluginServices.getMainFrame().getJMenuBar();

		// Iterate over menu bar to get children menus
		// for (int i = 0; i < jMenuBar.getMenuCount(); i++) {
		// // Getting children
		// JMenu menu = jMenuBar.getMenu(i);
		// if (menu.getText().equalsIgnoreCase(aux.getText("Vista"))) {
		// // Getting children components
		// Component[] subMenus = menu.getMenuComponents();
		// // Iterate over children menu bar to get children menus
		// for (int t = 0; t < subMenus.length; t++) {
		// // Getting all children components
		// Component component = subMenus[t];
		// // Only get instance of JMenu
		// if (component instanceof JMenu) {
		// JMenu subMenuItem = (JMenu) component;
		// // Is "navegacion" menu
		// if (subMenuItem.getText().equalsIgnoreCase(
		// aux.getText("navegacion"))) {
		// // Search north option
		// for (int j = 0; j < subMenuItem.getItemCount(); j++) {
		// if (subMenuItem.getItem(j).getText()
		// .equalsIgnoreCase(
		// PluginServices.getText(this,
		// "Active_north"))) {
		// myMenu = (JMenuItem) subMenuItem.getItem(j);
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// This condition are always true.
		if (!(view instanceof View3D))
			return;
		// // Casting to View3D
		// View3D vista3D = (View3D) view;
		// //vista3D.getMapControl().setCurrentMapTool(null);
		// Component viewer = (Component)vista3D.getCanvas3d();
		// viewer.setCursor(navCursor);
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices
				.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		// Only isVisible = true, where the view3D have layers
		if (f instanceof View3D) {
			/*******************************************************************
			 * This code don`t work because andami activate all buttons when the
			 * return value is true
			 ******************************************************************/
			View3D vista3D = (View3D) f;
			JToolBarButton b = (JToolBarButton) PluginServices.getMainFrame()
					.getComponentByName("NORTH");
			if ((vista3D.getPlanet().getCoordinateSystemType() != Planet.CoordinateSystemType.GEOCENTRIC)
					&& (b != null)) {
				b.setEnabled(false);
			}
			/* **************************************************************** */
			return true;
		}
		return false;
	}

	public void terminate() {
		super.terminate();
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public void postInitialize() {
		// Getting Main menu bar
		
		PluginServices aux = PluginServices
		.getPluginServices("com.iver.cit.gvsig");
		
		String[] menuPath = new String[3];
		menuPath[0] = new String("Vista");
		menuPath[1] = new String("navegacion");
		menuPath[2] = new String("Active_north");
		this.myMenu = (JMenuItem) PluginServices.getMainFrame().getMenuEntry(
				menuPath);

		if (myMenu != null) {

			File file = new File(this.getClass().getClassLoader().getResource(
					"images").getFile());
			String path1 = file.getPath() + "/mini_check_2.png";
			String path2 = file.getPath() + "/mini_no_check_2.png";

			iconVis = new ImageIcon(path1);
			iconNoVis = new ImageIcon(path2);

			myMenu.setIcon(iconNoVis);

			String path1B = file.getPath() + "/norte.png";
			String path2B = file.getPath() + "/NorthDes.png";
			iconButtonVis = new ImageIcon(path1B);
			iconButtonNoVis = new ImageIcon(path2B);

			JToolBarButton b = (JToolBarButton) PluginServices.getMainFrame()
					.getComponentByName("NORTH");
			if (b != null) {
				b.setToolTip(PluginServices.getText(this, "Ac_north") + " "
						+ PluginServices.getText(this, "Active_north"));

				// if (vista3D.getPlanet().getType() !=
				// PlanetType.SPHERICAL_MODE) {
				// b.setEnabled(false);
				// }
			}

		}

	}
}
