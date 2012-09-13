/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 Prodevelop S.L. main developer
 */

package org.gvsig.geocoding.extension;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.Envelope2D;
import org.gvsig.fmap.geom.primitive.impl.Point2D;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.GraphicLayer;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.FGraphic;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SimpleTextSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.gui.GeocodingModel;
import org.gvsig.geocoding.gui.GeocodingPanel;
import org.gvsig.geocoding.gui.IGeocodingModel;
import org.gvsig.geocoding.gui.address.AbstractAddressPanel;
import org.gvsig.geocoding.gui.address.AddressComposedPanel;
import org.gvsig.geocoding.gui.address.AddressRangePanel;
import org.gvsig.geocoding.gui.address.AddressSimpleCentroidPanel;
import org.gvsig.geocoding.gui.newpattern.NewPatternPanel;
import org.gvsig.geocoding.gui.relation.RelatePanel;
import org.gvsig.geocoding.gui.results.ResultsPanel;
import org.gvsig.geocoding.pattern.GeocodingSettings;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.gvsig.geocoding.styles.AbstractGeocodingStyle;
import org.gvsig.geocoding.styles.impl.AbstractRange;
import org.gvsig.geocoding.styles.impl.Composed;
import org.gvsig.geocoding.styles.impl.SimpleCentroid;
import org.gvsig.geocoding.utils.GeocodingTags;
import org.gvsig.geocoding.utils.GeocodingUtils;
import org.gvsig.geocoding.utils.PatternLoaderThread;
import org.gvsig.project.document.table.FeatureTableDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.gui.ProjectWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.GenericFileFilter;
import com.iver.utiles.XMLEntity;

/**
 * Controller of the geocoding extension and all yours panels
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class GeocodingController {

	private static final Logger log = LoggerFactory
			.getLogger(GeocodingController.class);

	private volatile static GeocodingController instance;

	public static final String ROW = PluginServices.getText(null, "row")
			+ " : ";

	private IGeocodingModel gmodel = null;
	private GeocodingPanel gpanel = null;
	private RelatePanel relpanel = null;
	private ResultsPanel respanel = null;

	/**
	 * Controller Constructor
	 */
	private GeocodingController() {
		// Build geocoding model
		this.gmodel = new GeocodingModel();
		// Create all extensions panel
		this.gpanel = new GeocodingPanel(this);
		this.relpanel = new RelatePanel(this);
		this.respanel = new ResultsPanel(this);

	}

	/**
	 * 
	 * @return
	 */
	public static GeocodingController getInstance() {
		if (instance == null) {
			synchronized (GeocodingController.class) {
				if (instance == null) {
					instance = new GeocodingController();
				}
			}
		}
		return instance;
	}

	/**
	 * Run the geocoding process in background
	 */
	public void geocoding() {
		PluginServices.cancelableBackgroundExecution(new GeocodingTask(this));
	}

	/**
	 * This method loads a Pattern from XML file
	 * 
	 * @return true if the load is succesfully
	 */
	public boolean loadPatternFromXML() {
		boolean ok = true;
		File thefile = null;
		JFileChooser jfc = null;
		/* Show the FileChooser to select a pattern file */
		try {
			jfc = new JFileChooser();
			jfc.setDialogTitle(PluginServices.getText(this,
					"load_geoco_pattern"));
			String[] extensions = { "xml" };
			jfc.setCurrentDirectory(new File(""));
			jfc.addChoosableFileFilter(new GenericFileFilter(extensions,
					PluginServices.getText(this, "pattern_geoco_file")));
			int returnval = jfc
					.showOpenDialog((java.awt.Component) PluginServices
							.getMainFrame());
			if (returnval == JFileChooser.APPROVE_OPTION) {
				thefile = jfc.getSelectedFile();
				log.debug("file opened: " + thefile);
			} else {
				return false;
			}
		} catch (Exception e) {
			String mes = PluginServices.getText(this, "geocoerrorloadingfile");
			String tit = PluginServices.getText(this, "geocoding");
			JOptionPane.showMessageDialog(null, mes, tit,
					JOptionPane.ERROR_MESSAGE);
			log.error("Error loading pattern file", e);
		}
		/* Parse the xml file */
		PatternLoaderThread load = new PatternLoaderThread(thefile);
		load.run();
		if (ok) {
			ok = load.isOk();
			this.gmodel.setPatternFile(load.getFile());
			this.gmodel.setPattern(load.getPattern());
		}
		return ok;
	}

	/**
	 * This method gets the list with the address components that the user has
	 * defined in the Geocoding Preferences
	 * 
	 * @return
	 */
	public List<String> getListAddressComponents() {

		List<String> components = new ArrayList<String>();
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		String tag = GeocodingTags.GEOCODINGELEMENTS + "_" + getLanguage();
		if (xml.contains(tag)) {

			String nam = String.valueOf(xml.getStringProperty(tag));
			File persistenceFile = new File(nam);
			String str = "";

			try {
				BufferedReader br = new BufferedReader(new FileReader(
						persistenceFile));
				while ((str = br.readLine()) != null) {
					components.add(str);
				}
			} catch (IOException e) {
				log.error("Reading the geocoding elements file", e);
			}
		}
		return components;
	}

	/**
	 * This method gets the application language
	 * 
	 * @return
	 */
	private String getLanguage() {

		ArrayList<Locale> myLocs = org.gvsig.i18n.Messages
				.getPreferredLocales();
		Locale myLoc = myLocs.size() == 0 ? Locale.ENGLISH : myLocs.get(0);

		/*
		 * TODO review and test with differente language codes, included Spanish
		 * languages
		 */
		String lang = myLoc.getLanguage().toLowerCase();

		return lang;
	}

	/**
	 * This method saves the Geocoding Pattern
	 * 
	 * @return
	 */
	public boolean savePattern() {

		boolean savedOK = false;

		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(PluginServices.getText(this, "save_geoco_pattern"));
		String[] extensions = { "xml" };
		jfc.addChoosableFileFilter(new GenericFileFilter(extensions,
				PluginServices.getText(this, "pattern_geoco_file")));
		int returnval = jfc.showSaveDialog((java.awt.Component) PluginServices
				.getMainFrame());

		if (returnval == JFileChooser.APPROVE_OPTION) {

			File file = jfc.getSelectedFile();
			log.debug("file created: " + file);
			if (file != null) {
				file = GeocodingUtils.addXMLSuffix(file);
				Patterngeocoding pat = this.getGmodel().getPattern();
				pat.setPatternName(file.getName());
				try {
					pat.saveToXML(file);
					savedOK = true;
					log.debug("pattern saved: " + file);
					this.gmodel.setPatternFile(file);
				} catch (Exception e) {
					log.error("Serializing the pattern", e);
					String mes = PluginServices.getText(this,
							"geocorrorsavingfile");
					String tit = PluginServices.getText(this, "geocoding");
					JOptionPane.showMessageDialog(null, mes, tit,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		return savedOK;
	}

	/**
	 * This method launches the New Pattern Panel
	 * 
	 * @param pat
	 */
	public void launchNewPatternPanel(Patterngeocoding pat) {

		List<FLyrVect> lyrs = this.getListgvSIGVectLayers();
		if (lyrs.size() > 0) {
			/* Launch the new pattern panel */
			NewPatternPanel ppanel = new NewPatternPanel(this, pat);
			PluginServices.getMDIManager().addWindow(ppanel);
			ppanel.setVisible(true);
		} else {
			// show warning message
			String title = PluginServices.getText(null, "geocoding");

			String message = PluginServices.getText(null, "nolayers");
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * This method updates the GeocodingPanel after save
	 */
	public void updateGeocoGUI() {
		// pattern path
		gpanel.setJTextPatternPath(this.getGmodel().getPatternFile()
				.getAbsolutePath());
		// settings. Maximum results availables
		gpanel.setParamMaxresults(this.getPattern().getSettings()
				.getResultsNumber());
		// settings. Minimum score
		gpanel.setParamScore(this.getPattern().getSettings().getScore());
	}

	/**
	 * Get the pattern from the geocoding model
	 * 
	 * @return pattern
	 */
	public Patterngeocoding getPattern() {
		return this.getGmodel().getPattern();
	}

	/**
	 * Get geocoding model
	 * 
	 * @return the gmodel
	 */
	public IGeocodingModel getGmodel() {
		return gmodel;
	}

	/**
	 * Get main geocoding panel
	 * 
	 * @return the gpanel
	 */
	public GeocodingPanel getGPanel() {
		return gpanel;
	}

	/**
	 * Get relate geocoding panel
	 * 
	 * @return the gpanel
	 */
	public RelatePanel getGRelPanel() {
		return relpanel;
	}

	/**
	 * Get results geocoding panel
	 * 
	 * @return the gpanel
	 */
	public ResultsPanel getGResPanel() {
		return respanel;
	}

	/**
	 * Get a list gvSIG Project tables
	 * 
	 * @return
	 */
	public DefaultComboBoxModel getModelgvSIGTables() {
		// get tables loaded in gvSIG project
		List<FeatureTableDocument> tables = this.getListgvSIGTables();
		// Build combo model with list of tables availables
		if (tables.size() > 0) {
			DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
			for (int i = 0; i < tables.size(); i++) {
				FeatureTableDocument table = tables.get(i);
				comboModel.addElement(table);
			}
			return comboModel;
		}
		return null;
	}

	/**
	 * Get the address panel from pattern style
	 * 
	 * @return panel
	 */
	public AbstractAddressPanel getAddressPanelFromPatternStyle() {
		Patterngeocoding pat = this.getPattern();
		if (pat != null) {
			AbstractGeocodingStyle style = pat.getSource().getStyle();
			Literal lit = style.getRelationsLiteral();
			AbstractAddressPanel aPanel = null;
			if (style instanceof SimpleCentroid) {
				aPanel = new AddressSimpleCentroidPanel(this, lit);
			}
			if (style instanceof AbstractRange) {
				aPanel = new AddressRangePanel(this, lit);
			}
			if (style instanceof Composed) {
				aPanel = new AddressComposedPanel(this, lit);
			}
			return aPanel;
		}
		return null;
	}

	/**
	 * get loops number of geocoding process
	 * 
	 * @return
	 */
	public long getGeocodingProcessCount() {
		if (this.getGmodel().isSimple()) {
			return 1;
		} else {
			long n = 0;
			try {
				n = this.getGmodel().getSelectedTableStore().getFeatureSet()
						.getSize();
				return n;
			} catch (Exception e) {
				return 0;
			}
		}
	}

	/**
	 * get JTable Results
	 * 
	 * @return
	 */
	public JTable getJTableResults() {
		return this.gpanel.getJTableResults();
	}

	/**
	 * Create initial array with list of selected results elements to export
	 * 
	 * @param results
	 * @return
	 */
	public Integer[] createInitialResultsExportElements(
			List<Set<GeocodingResult>> results) {
		int cant = results.size();
		Integer[] expElems = new Integer[cant];
		int i = 0;
		for (Set<GeocodingResult> res : results) {
			if (res.size() > 0) {
				expElems[i] = 0;
			} else {
				expElems[i] = -1;
			}
			i++;
		}
		return expElems;
	}

	/**
	 * Get current view in gvSIG
	 * 
	 * @return view
	 */
	public View getCurrentView() {
		IWindow[] ws = PluginServices.getMDIManager().getOrderedWindows();
		for (int k = 0; k < ws.length; k++) {
			if (ws[k] instanceof View) {
				return (View) ws[k];
			}
		}
		return null;
	}

	/**
	 * Centers the view in a point (x,y)
	 * 
	 * @param x
	 * @param y
	 * 
	 * @throws Exception
	 */
	public void zoomToPoint(double x, double y) throws Exception {

		View view = getCurrentView();
		if (view != null) {
			MapContext mapContext = view.getModel().getMapContext();
			MapControl mapControl = new MapControl();
			mapControl.setMapContext(mapContext);

			Envelope oldExtent = mapControl.getViewPort().getAdjustedExtent();
			double oldCenterX = oldExtent.getCenter(0);
			double oldCenterY = oldExtent.getCenter(1);
			double movX = x - oldCenterX;
			double movY = y - oldCenterY;
			double minX = oldExtent.getMinimum(0) + movX;
			double minY = oldExtent.getMinimum(1) + movY;
			double width = oldExtent.getLength(0);
			double height = oldExtent.getLength(1);
			Point pto1 = new Point2D(minX, minY);
			Point pto2 = new Point2D(minX + width, minY
					+ height);
			Envelope enve = new Envelope2D(pto1, pto2);
			mapControl.getViewPort().setEnvelope(enve);
		}

	}

	/**
	 * Clear all graphics in view
	 */
	public void clearGeocodingPoint() {
		View view = this.getCurrentView();
		if (view != null) {
			MapContext mapContext = view.getModel().getMapContext();
			MapControl mapControl = new MapControl();
			mapControl.setMapContext(mapContext);
			GraphicLayer grphclyr = mapControl.getMapContext()
					.getGraphicsLayer();
			grphclyr.clearAllGraphics();
			mapControl.drawGraphics();
			view.repaint();
			view.repaintMap();
		}
	}

	/**
	 * This method gets the FLyrVect list of the View
	 * 
	 * @return
	 */
	public List<FLyrVect> getListgvSIGVectLayers() {

		List<FLyrVect> layers = new ArrayList<FLyrVect>();

		View view = this.getCurrentView();

		if (view != null) {
			/* Get the layers of the view */
			MapControl mapControl = view.getMapControl();
			FLayers lyrs = mapControl.getMapContext().getLayers();
			if (lyrs.getLayersCount() > 0) {
				/* Get the layers (FLyrVect) of the view */
				for (int i = 0; i < lyrs.getLayersCount(); i++) {
					FLayer lyr = lyrs.getLayer(i);
					if (lyr instanceof FLyrVect) {
						layers.add((FLyrVect) lyr);
					}
				}
			}
		}
		return layers;
	}

	/**
	 * Get a tables list of gvSIG project
	 * 
	 * @return
	 */
	public List<FeatureTableDocument> getListgvSIGTables() {

		List<FeatureTableDocument> tables = new ArrayList<FeatureTableDocument>();

		IWindow[] wins = PluginServices.getMDIManager().getAllWindows();
		ProjectWindow projWindow = null;
		for (int i = 0; i < wins.length; i++) {
			if (wins[i] instanceof ProjectWindow) {
				projWindow = (ProjectWindow) wins[i];
				break;
			}
		}
		if (projWindow != null) {
			Project p = (Project) projWindow.getWindowModel();
			List<ProjectDocument> documents = p.getDocuments();
			for (ProjectDocument docu : documents) {
				if (docu instanceof FeatureTableDocument) {
					tables.add((FeatureTableDocument) docu);
				}
			}
		}
		return tables;
	}

	/**
	 * Draw point in the results position with identifier label
	 * 
	 * @param result
	 * @param max
	 * @param score
	 */
	public void showResultsPositionsOnView(Set<GeocodingResult> result) {

		GeocodingSettings sett = this.getPattern().getSettings();
		int max = sett.getResultsNumber();
		double score = sett.getScore();

		View vista = this.getCurrentView();
		MapContext mapContext = vista.getModel().getMapContext();
		MapControl mapControl = new MapControl();
		mapControl.setMapContext(mapContext);
		GraphicLayer grphclyr = mapControl.getMapContext().getGraphicsLayer();
		grphclyr.clearAllGraphics();

		int idSymbol;
		int i = 1;
		for (GeocodingResult res : result) {
			if (res.getScore() >= score && i <= max) {
				ISymbol symbolPos = res.getSymbolPosition();
				Geometry geom = res.getGeometry();
				idSymbol = grphclyr.addSymbol(symbolPos);
				FGraphic theGraphic = new FGraphic(geom, idSymbol);
				grphclyr.addGraphic(theGraphic);
				SimpleTextSymbol symbolText = (SimpleTextSymbol) SymbologyFactory
						.createDefaultTextSymbol();
				symbolText.setFont(new Font("Verdana", Font.BOLD, 12));
				symbolText.setTextColor(Color.BLACK);
				symbolText.setText(Integer.toString(i));
				idSymbol = grphclyr.addSymbol(symbolText);
				FGraphic theGraphicText = new FGraphic(geom, idSymbol);
				grphclyr.addGraphic(theGraphicText);
			}
			i++;
		}
		mapControl.drawGraphics();
	}

	public void drawPositionsOnView(Geometry geom) {	

		View vista = this.getCurrentView();
		MapContext mapContext = vista.getModel().getMapContext();
		MapControl mapControl = new MapControl();
		mapControl.setMapContext(mapContext);
		GraphicLayer grphclyr = mapControl.getMapContext().getGraphicsLayer();
		grphclyr.clearAllGraphics();

		SimpleMarkerSymbol symbol = (SimpleMarkerSymbol) SymbologyFactory
				.createDefaultMarkerSymbol();
		symbol.setStyle(SimpleMarkerSymbol.CIRCLE_STYLE);
		symbol.setSize(4);
		symbol.setColor(Color.RED);		
		
		int idSymbol = grphclyr.addSymbol(symbol);
		FGraphic theGraphic = new FGraphic(geom, idSymbol);
		grphclyr.addGraphic(theGraphic);
		mapControl.drawGraphics();
	}

	/**
	 * Get selected FLyrVect in view
	 * 
	 * @return
	 */
	public FLyrVect getSelectedFLyrVect() {
		View view = this.getCurrentView();

		if (view != null) {
			/* Get the layers of the view */
			MapControl mapControl = view.getMapControl();
			FLayers lyrs = mapControl.getMapContext().getLayers();
			if (lyrs.getActives().length > 0) {
				return (FLyrVect) lyrs.getLayer(0);
			} else {
				return null;
			}
		}
		return null;
	}

}
