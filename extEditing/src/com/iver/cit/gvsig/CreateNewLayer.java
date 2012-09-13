package com.iver.cit.gvsig;

import javax.swing.ImageIcon;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.wizard.WizardAndami;
import com.iver.cit.gvsig.gui.cad.CADToolAdapter;
import com.iver.cit.gvsig.gui.cad.MyFinishAction;
import com.iver.cit.gvsig.gui.cad.panels.ChooseGeometryType;
import com.iver.cit.gvsig.gui.cad.panels.FileBasedPanel;
import com.iver.cit.gvsig.gui.cad.panels.JPanelFieldDefinition;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class CreateNewLayer extends Extension {
	static ImageIcon LOGO;

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
public void execute(String actionCommand) {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		if (f instanceof View) {
			try {
				View vista = (View) f;

				LOGO = new javax.swing.ImageIcon(this.getClass()
						.getClassLoader().getResource(
								"images/package_graphics.png"));
				CADToolAdapter cta = CADExtension.getCADToolAdapter();
				MapControl mapControl = vista.getMapControl();
				cta.setMapControl(mapControl);
				/*
				 * SimpleLogoJWizardFrame wizardFrame = new
				 * SimpleLogoJWizardFrame( LOGO);
				 * wizardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				 *
				 * SwingUtilities.updateComponentTreeUI(wizardFrame);
				 *
				 * wizardFrame.setTitle("Creación de un nuevo Tema");
				 */
				WizardAndami wizard = new WizardAndami(LOGO);

//				DriverManager writerManager = LayerFactory.getDM();
//				ArrayList spatialDrivers = new ArrayList();
//				String[] writerNames = writerManager.getDriverNames();
//				for (int i = 0; i < writerNames.length; i++) {
//					Driver drv = writerManager.getDriver(writerNames[i]);
//					if (drv instanceof ISpatialWriter)
//						spatialDrivers.add(drv.getName());
//				}

				ChooseGeometryType panelChoose = new ChooseGeometryType(wizard
						.getWizardComponents());
				JPanelFieldDefinition panelFields = new JPanelFieldDefinition(
						wizard.getWizardComponents());
				DataManager dm=DALLocator.getDataManager();
				if (actionCommand.equals("SHP")) {
					wizard.getWizardComponents().addWizardPanel(panelChoose);
					wizard.getWizardComponents().addWizardPanel(panelFields);


//					DataStoreParameters parameters = dm.createStoreParameters("SHPStore");
//					FeatureStore store=(FeatureStore)dm.createStore(parameters);
//					panelChoose.setFeatureStore(store);
					FileBasedPanel filePanel = new FileBasedPanel(wizard
							.getWizardComponents());
					filePanel.setFileExtension("shp");
					wizard.getWizardComponents().addWizardPanel(filePanel);

					wizard.getWizardComponents().setFinishAction(
							new MyFinishAction(wizard.getWizardComponents(),
									vista, actionCommand));
				}
				if (actionCommand.equals("DXF")) {
					FeatureStore store = (FeatureStore)dm.createStoreParameters("DXFStore");

					panelChoose.setFeatureStore(store);
					FileBasedPanel filePanel = new FileBasedPanel(wizard
							.getWizardComponents());
					filePanel.setFileExtension("dxf");
					wizard.getWizardComponents().addWizardPanel(filePanel);
					wizard.getWizardComponents().getBackButton().setEnabled(
							false);
					wizard.getWizardComponents().getNextButton().setEnabled(
							false);

					wizard.getWizardComponents().setFinishAction(
							new MyFinishAction(wizard.getWizardComponents(),
									vista, actionCommand));
				}
//				if (actionCommand.equals("POSTGIS")) {
//					wizard.getWizardComponents().addWizardPanel(panelChoose);
//					wizard.getWizardComponents().addWizardPanel(panelFields);
//					Driver driver = writerManager
//							.getDriver("PostGIS JDBC Driver");
//					panelChoose.setFeatureStore(driver);
//					panelFields.setWriter(((IWriteable) driver).getWriter());
//					wizard.getWizardComponents().addWizardPanel(
//							new PostGISpanel(wizard.getWizardComponents()));
//
//					wizard.getWizardComponents().setFinishAction(
//							new MyFinishAction(wizard.getWizardComponents(),
//									vista, actionCommand));
//				}

				wizard.getWizardComponents().getFinishButton()
						.setEnabled(false);
				wizard.getWindowInfo().setWidth(640);
				wizard.getWindowInfo().setHeight(350);
				wizard.getWindowInfo().setTitle(
						PluginServices.getText(this, "new_layer"));
				// Utilities.centerComponentOnScreen(wizard);
				// wizardFrame.show();
				PluginServices.getMDIManager().addWindow(wizard);
				// System.out.println("Salgo con " + panelChoose.getLayerName());
			} catch (InitializeException e) {
				NotificationManager.addError(e.getMessage(),e);
			} catch (ProviderNotRegisteredException e) {
				NotificationManager.addError(e.getMessage(),e);
			}
		}
	}
	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		View f = (View) PluginServices.getMDIManager().getActiveWindow();

		if (f == null)
			return false;
		return true;
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager()
				.getActiveWindow();

		if (f == null) {
			return false;
		}

		if (f instanceof View)
			return true;
		return false;
	}
}
