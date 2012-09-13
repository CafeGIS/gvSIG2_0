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
 * 2008 Prodevelop S.L. main development
 */

package org.gvsig.geocoding.extension;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;


import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.geocoding.Geocoder;
import org.gvsig.geocoding.GeocodingLocator;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.gui.TableResultsModel;
import org.gvsig.geocoding.impl.DataGeocoderImpl;
import org.gvsig.geocoding.pattern.Patterngeocoding;
import org.gvsig.geocoding.result.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.threads.AbstractMonitorableTask;

/**
 * Geocoding task in background
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public class GeocodingTask extends AbstractMonitorableTask {

	private static final Logger log = LoggerFactory
			.getLogger(GeocodingTask.class);
	private GeocodingController control;
	private long finalStep;
	private Geocoder geoco = null;

	/**
	 * Constructor task
	 * 
	 * @param control
	 */
	public GeocodingTask(GeocodingController control) {

		this.control = control;
		// configure task
		setInitialStep(0);
		setStatusMessage(PluginServices.getText(this, "geocodingrun"));
		setDeterminatedProcess(true);
		finalStep = control.getGeocodingProcessCount();
		setFinalStep((int) finalStep);
		//
		GeocodingLocator loc = GeocodingLocator.getInstance();
		geoco = loc.getGeocoder();
	}

	/**
	 * run process
	 */
	public void run() throws Exception {

		String logtask = "";
		Address address = null;

		// Remove old results of the model list
		control.getGmodel().clearResults();

		// SIMPLE GEOCODING
		if (finalStep == 1) {
			logtask = PluginServices.getText(this, "addressgeocoded");

			if (!isCanceled()) {
				setCurrentStep(1);
				address = getSimpleAddress();
				Set<GeocodingResult> result = geocoding(control.getPattern(),
						address);
				control.getGmodel().addResult(result);
				setNote(1 + " " + logtask);
			} else {
				return;
			}
		}

		// TABLE GEOCODING (MASSIVE)
		else {
			logtask = PluginServices.getText(this, "addressesgeocoded");
			for (int i = 1; i < finalStep + 1; i++) {
				if (!isCanceled()) {
					setCurrentStep(i);
					address = getTableAddress(i - 1);
					Set<GeocodingResult> result = geocoding(control
							.getPattern(), address);
					control.getGmodel().addResult(result);
					setNote(i + " " + logtask);
				} else {
					return;
				}
			}
		}

		List<Set<GeocodingResult>> results = control.getGmodel()
				.getAllResults();
		// get first result to show in the table
		Set<GeocodingResult> result = results.get(0);
		// save in the model number of result showed
		control.getGmodel().setNumResultShowed(0);
		// build array with results selected and save in the model
		Integer[] expElems = control
				.createInitialResultsExportElements(results);
		control.getGmodel().setExportElements(expElems);

		if (result.size() > 0) {
			int max = control.getPattern().getSettings().getResultsNumber();

			JTable jTableResults = control.getJTableResults();
			TableResultsModel model = new TableResultsModel(control, address);
			model.setResultSet(result, max, control.getPattern().getSettings()
					.getScore());

			jTableResults.setModel(model);
			jTableResults.validate();
			jTableResults.repaint();

			// Show results position in the gvSIG view
			control.showResultsPositionsOnView(result);

			// select first result
			jTableResults.setRowSelectionInterval(0, 0);

			// zoom position to first result in the view
			Point pto = model.getGeometry(0,2,3);
			control.zoomToPoint(pto.getX(), pto.getY());

			if (results.size() > 1) {
				control.getGPanel().activeTableGUIFeatures(true);
				control.getGPanel().setLabRow(control.ROW + 1);
				control.getGmodel().setNumResultShowed(0);
			}
		}
	}

	/**
	 * get simple address
	 * 
	 * @return
	 */
	private Address getSimpleAddress() {
		return control.getGPanel().getAddressPanel().getSimpleAddress();
	}

	/**
	 * get table address from row
	 * 
	 * @param row
	 * @return
	 */
	private Address getTableAddress(int row) {
		return control.getGPanel().getAddressPanel().getTableAddress(row);
	}

	/**
	 * geocoding process
	 * 
	 * @param pat
	 * @param address
	 * @return set with sorted results
	 */
	private Set<GeocodingResult> geocoding(Patterngeocoding pat, Address address) {

		Set<GeocodingResult> results = new TreeSet<GeocodingResult>();
		try {
			((DataGeocoderImpl)geoco).setPattern(pat);
			results = geoco.geocode(address);
		} catch (Exception e) {
			log.error("ERROR in geocoding process", e);
		}
		return results;
	}

}
