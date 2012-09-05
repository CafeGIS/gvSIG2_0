package com.iver.cit.gvsig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Properties;

import org.cresques.ProjectionLibrary;
import org.cresques.impl.CresquesCtsLibrary;
import org.gvsig.AppGvSigLibrary;
import org.gvsig.AppGvSigLocator;
import org.gvsig.compat.CompatLibrary;
import org.gvsig.compat.se.SECompatLibrary;
import org.gvsig.fmap.dal.DALFileLibrary;
import org.gvsig.fmap.dal.DALLibrary;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.index.spatial.gt2.Gt2IndexLibrary;
import org.gvsig.fmap.dal.index.spatial.jsi.JSIIndexLibrary;
import org.gvsig.fmap.dal.index.spatial.jts.JTSIndexLibrary;
import org.gvsig.fmap.dal.resource.ResourceManager;
import org.gvsig.fmap.dal.resource.exception.DisposeResorceManagerException;
import org.gvsig.fmap.dal.serverexplorer.filesystem.swing.FilesystemExplorerTableWizardPanel;
import org.gvsig.fmap.dal.store.dbf.DBFLibrary;
import org.gvsig.fmap.dal.store.dgn.DGNLibrary;
import org.gvsig.fmap.dal.store.dgn.legend.DGNLegendLibrary;
import org.gvsig.fmap.dal.store.dxf.DXFLibrary;
import org.gvsig.fmap.dal.store.dxf.legend.DXFLegendLibrary;
import org.gvsig.fmap.dal.store.shp.SHPLibrary;
import org.gvsig.fmap.geom.GeometryLibrary;
import org.gvsig.fmap.geom.impl.DefaultGeometryLibrary;
import org.gvsig.fmap.geom.operation.MapContextGeomOperationsLibrary;
import org.gvsig.fmap.mapcontext.MapContextLibrary;
import org.gvsig.tools.ToolsLibrary;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.evaluator.sqljep.SQLJEPEvaluator;
import org.gvsig.tools.evaluator.sqljep.SQLJEPLibrary;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.persistence.xmlentity.XMLEntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.Launcher;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;

public class InitializeApplicationExtension extends Extension {
    private static final Logger logger = LoggerFactory
            .getLogger(InitializeApplicationExtension.class);
	private Observer dbPasswordResorceObserver;


	private AppGvSigLibrary appGvSigLibrary;

	private DALFileLibrary dalFileLibrary;

	private DXFLibrary dxf;

	private DXFLegendLibrary dxfLegend;

	private DGNLibrary dgn;

	private DGNLegendLibrary dgnLegend;

	private DBFLibrary dbf;

	private SHPLibrary shp;

	private MapContextLibrary mapContextLibrary;
	private MapContextGeomOperationsLibrary mapContextGeomOperationLibrary;


	private JTSIndexLibrary jtsIndex;
	private JSIIndexLibrary jsiIndex;
	private Gt2IndexLibrary gt2Index;
	private ToolsLibrary toolLibrary;
	private DALLibrary dalLibrary;
	private GeometryLibrary geometryLibrary;
	private SQLJEPLibrary sQLJEPLibrary;
	private DefaultGeometryLibrary defaultGeometryLibrary;
	private CompatLibrary compatLibrary;
	private ProjectionLibrary projectionLibrary;
	private CresquesCtsLibrary cresquesCtsLibrary;



	public void initialize() {

		// Basic libraries
		toolLibrary = new ToolsLibrary();
		toolLibrary.initialize();

		// Register persistence manager
		ToolsLocator.registerDefaultPersistenceManager(XMLEntityManager.class);

		projectionLibrary = new ProjectionLibrary();
		projectionLibrary.initialize();

		cresquesCtsLibrary = new CresquesCtsLibrary();
		cresquesCtsLibrary.initialize();

		// geometry api
		geometryLibrary = new GeometryLibrary();
		geometryLibrary.initialize();

		// geometry implementation
		defaultGeometryLibrary = new DefaultGeometryLibrary();
		defaultGeometryLibrary.initialize();

		// Data Access Library
		dalLibrary = new DALLibrary();
		dalLibrary.initialize();

		// SQLJEP expression parser for DAL
		sQLJEPLibrary = new SQLJEPLibrary();
		sQLJEPLibrary.initialize();


		// Register default expression parser
		DALLocator.getDataManager().registerDefaultEvaluator(
				SQLJEPEvaluator.class);

		// DAL file store support library
		dalFileLibrary = new DALFileLibrary();
		dalFileLibrary.initialize();

		// DAL DXF provider
		dxf = new DXFLibrary();
		dxf.initialize();
		dxfLegend = new DXFLegendLibrary();
		dxfLegend.initialize();

		// DAL DGN provider
		dgn = new DGNLibrary();
		dgn.initialize();
		dgnLegend = new DGNLegendLibrary();
		dgnLegend.initialize();

		// DAL DBF provider
		dbf = new DBFLibrary();
		dbf.initialize();

		// DAL SHP provider
		shp = new SHPLibrary();
		shp.initialize();


		// DAL geom Index JTS provider
		jtsIndex = new JTSIndexLibrary();
		jtsIndex.initialize();

		// DAL geom Index JSI provider
		jsiIndex = new JSIIndexLibrary();
		jsiIndex.initialize();

		// DAL geom Index GT2 provider
		gt2Index = new Gt2IndexLibrary();
		gt2Index.initialize();

		// MapContext library
		mapContextLibrary = new MapContextLibrary();
		mapContextLibrary.initialize();

		// MapContext Geomerty operations library
		mapContextGeomOperationLibrary = new MapContextGeomOperationsLibrary();
		mapContextGeomOperationLibrary.initialize();

		// appGvSIG library
		appGvSigLibrary = new AppGvSigLibrary();
		appGvSigLibrary.initialize();

		// libCompat library
		// Register default expression parser
		compatLibrary = new CompatLibrary();
		compatLibrary.initialize();
		SECompatLibrary compatLibraryImpl=new SECompatLibrary();
		compatLibraryImpl.initialize();

		AppGvSigLocator.getAppGvSigManager().registerAddTableWizard("File",
				"File Table", FilesystemExplorerTableWizardPanel.class);
		addToLogInfo();
		registerIcons();

	}
	public void postInitialize(){

		toolLibrary.postInitialize();

		projectionLibrary.postInitialize();
		cresquesCtsLibrary.postInitialize();

		geometryLibrary.postInitialize();
		defaultGeometryLibrary.postInitialize();

		dalLibrary.postInitialize();

		sQLJEPLibrary.postInitialize();

		dalFileLibrary.postInitialize();

		dxf.postInitialize();
		dxfLegend.postInitialize();

		dgn.postInitialize();
		dgnLegend.postInitialize();

		dbf.postInitialize();
		shp.postInitialize();

		jtsIndex.postInitialize();
		jsiIndex.postInitialize();
		gt2Index.postInitialize();

		mapContextLibrary.postInitialize();
		mapContextGeomOperationLibrary.postInitialize();

		appGvSigLibrary.postInitialize();

		compatLibrary.postInitialize();

		registerObservers();

		DALLocator.getResourceManager().startResourceCollector(
				3 * (60 * 1000), // minutes --> miliseconds
				null);

	}
	private void registerObservers() {
//		FIXME
//		ResourceManager resMan = DALLocator.getResourceManager();//.getResource(PostgresqlStore.DATASTORE_NAME);
//		dbPasswordResorceObserver = new DBResourceManager();
//		resMan.addObserver(dbPasswordResorceObserver);
	}

	//Registro en esta extension los iconos que no se donde registrarlos.
	private void registerIcons(){

		PluginServices.getIconTheme().registerDefault(
				"view-add-event-layer",
				this.getClass().getClassLoader().getResource("images/addeventtheme.png")
			);
		PluginServices.getIconTheme().registerDefault(
				"gvsig-logo-icon",
				this.getClass().getClassLoader().getResource("images/icon_gvsig.png")
			);



		PluginServices.getIconTheme().registerDefault(
				"mapa-icono",
				this.getClass().getClassLoader().getResource("images/mapas.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"layout-insert-view",
				this.getClass().getClassLoader().getResource("images/MapaVista.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"vista-icono",
				this.getClass().getClassLoader().getResource("images/Vista.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"hand-icono",
				this.getClass().getClassLoader().getResource("images/Hand.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"add-layer-icono",
				this.getClass().getClassLoader().getResource("images/add-layer.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"delete-icono",
				this.getClass().getClassLoader().getResource("images/delete.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"arrow-up-icono",
				this.getClass().getClassLoader().getResource("images/up-arrow.png")
			);

		PluginServices.getIconTheme().registerDefault(
				"arrow-down-icono",
				this.getClass().getClassLoader().getResource("images/down-arrow.png")
			);
//		PluginServices.getIconTheme().register(
//				"arrow-down-icono",
//				PrintPropertiesPage.class.getClassLoader().getResource("images/prepare-page.png")
//			);
	}

	public void execute(String actionCommand) {

	}

	public boolean isEnabled() {
		return false;
	}

	public boolean isVisible() {
		return false;
	}

	private void addToLogInfo() {
		String info[] = this.getStringInfo().split("\n");
		for (int i=0;i< info.length;i++) {
			logger.info(info[i]);
		}
	}

	public String getStringInfo() {
		StringWriter writer = new StringWriter();
		String andamiPath;
		String extensionsPath;
		//		String jaiVersion;

		Properties props = System.getProperties();

		try {
			try {
				andamiPath = (new File(Launcher.class.getResource(".").getFile() + File.separator + ".." + File.separator + ".." + File.separator +"..")).getCanonicalPath();
			} catch (IOException e) {
				andamiPath = (new File(Launcher.class.getResource(".").getFile() + File.separator + ".." + File.separator + ".." + File.separator +"..")).getAbsolutePath();
			}
		} catch (Exception e1) {
			andamiPath = (String)props.get("user.dir");
		}
		try {
			try {
				extensionsPath = (new File(Launcher.getAndamiConfig().getPluginsDirectory())).getCanonicalPath();
			} catch (IOException e) {
				extensionsPath = (new File(Launcher.getAndamiConfig().getPluginsDirectory())).getAbsolutePath();
			}
		} catch (Exception e1) {
			extensionsPath = "???";
		}



		writer.write("gvSIG version: " + Version.longFormat() + "\n");
		writer.write("    gvSIG app exec path: " + andamiPath + "\n");
		writer.write("    gvSIG user app home: " + Launcher.getAppHomeDir() + "\n");
		writer.write("    gvSIG extension path: " + extensionsPath + "\n");
		writer.write("    gvSIG locale language: " + Launcher.getAndamiConfig().getLocaleLanguage() + "\n");
		String osName = props.getProperty("os.name");
		writer.write("OS name: " + osName + "\n");
		writer.write("    arch:" + props.get("os.arch") + "\n");
		writer.write("    version:"+ props.get("os.version") + "\n");
		if (osName.startsWith("Linux")) {
			try {
				String[] command = {"lsb_release", "-a"};
				Process p = Runtime.getRuntime().exec(command);
				InputStream is = p.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				while ( (line = reader.readLine()) != null) {
					writer.write("    "+line+"\n");
				}
			}
			catch (Exception ex) {

			}
		}
		writer.write("JAVA vendor: " + props.get("java.vendor") + "\n");
		writer.write("    version:" +props.get("java.version")+ "\n");
		writer.write("    home: " + props.get("java.home") + "\n");
		return writer.toString();
	}

	public void terminate() {
		// XXX: Need a TerminateApplicationExtension ???
		ResourceManager resMan = DALLocator.getResourceManager();
		resMan.stopResourceCollector();
		try {
			resMan.dispose();
		} catch (DisposeResorceManagerException e) {
			logger.error("Exceptions at dispose Resource Manager", e);
		}

		super.terminate();
//		try {
//			LayerFactory.getDataSourceFactory().finalizeThis();
//		} catch (Exception e) {
//			//e.printStackTrace();
//		}

	}
}
