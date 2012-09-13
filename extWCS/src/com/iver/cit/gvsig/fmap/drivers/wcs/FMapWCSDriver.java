/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*
* For more information, contact:
*
*  Generalitat Valenciana
*   Conselleria d'Infraestructures i Transport
*   Av. Blasco Ibáñez, 50
*   46010 VALENCIA
*   SPAIN
*
*      +34 963862235
*   gvsig@gva.es
*      www.gvsig.gva.es
*
*    or
*
*   IVER T.I. S.A
*   Salamanca 50
*   46005 Valencia
*   Spain
*
*   +34 963163400
*   dac@iver.es
*/
package com.iver.cit.gvsig.fmap.drivers.wcs;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.remoteClient.exceptions.ServerErrorException;
import org.gvsig.remoteClient.exceptions.WCSException;
import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.wcs.WCSClient;
import org.gvsig.remoteClient.wcs.WCSCoverage;
import org.gvsig.remoteClient.wcs.WCSStatus;
import org.gvsig.remoteClient.wcs.WCSCoverage.AxisDescription;
import org.gvsig.remoteClient.wms.ICancellable;

import com.iver.cit.gvsig.fmap.layers.FMapWCSParameter;
import com.iver.cit.gvsig.fmap.layers.WCSLayer;



/**
 * Driver between the FMap and WCSClient
 *
 * Controlador entre FMap y WCSClient
 *
 * @author jaume
 *
 */
public class FMapWCSDriver {
	private WCSClient client = null;
	private Hashtable coverages;
	private WCSLayer[] layerList;



	/**
	 * Obtains the host name.
	 *
	 * Devuelve el nombre del host.
	 */
	public String getHost() {
		return client.getHost();
	}

	private FMapWCSDriver() {}

	protected FMapWCSDriver(URL url) throws ConnectException, IOException {
		client = new WCSClient(url.toString());
	}

	/**
	 * Returns the string "WCSDriver", which is the driver's name.
	 *
	 * Devuelve "WCSDriver", el nombre del driver.
	 * @return String
	 */
	public String getName() { return "WCSDriver"; }

	/**
	 * Sets the server that we want to connect to.
	 *
	 * Establece el servidor al que se quiere conectar.
	 *
	 * @param host
	 * @throws IOException
	 */
	public void setHost(String host) throws IOException{
		client = new WCSClient(host);
	}


	/**
	 * Returns a human-readable string containing the server's name.
	 *
	 * Devuelve el nombre legible del servidor devuelto por éste.
	 *
	 * @return String
	 */
	public String getLabel() {
		return client.getServiceTitle();
	}

	/**
	 * Returns a string containing the server's WCS version number.
	 *
	 * Devuelve el número de versión WCS del servidor
	 *
	 * @return String
	 */
	public String getVersion(){
		return client.getVersion();
	}

	/**
	 * <p>
	 * Returns name and description of the server. It is supposed to be used
	 * as the source of the abstract field in your application's interface.
	 * </p>
	 * <p>
	 * Devuelve nombre y descripción (abstract) del servidor.
	 * </p>
	 * @return String
	 */
	public String getDescription(){
		return client.getDescription();
	}

	/**
	 * Returns the layer descriptor for a given coverage name.
	 * @param layerName
	 * @return WCSLayer
	 */
	public WCSLayer getLayer(String layerName) {
		getLayerList();
		return (WCSLayer) coverages.get(layerName);
	}

	/**
	 * Returns an array of WCSLayer's with the descriptors of all coverages
	 * @return WCSLayer[]
	 */
	public WCSLayer[] getLayerList(){
		if (coverages == null || coverages.isEmpty()) {
			// the WCSLayer collection will be built
			coverages = new Hashtable();
			Hashtable wcsCoverages  = client.getCoverageList();
			int sz = wcsCoverages.size();

			// Create an array with the WCSCoverages
			WCSCoverage[] coverageList = new WCSCoverage[sz];
			Iterator it = wcsCoverages.keySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				coverageList[i] = (WCSCoverage) wcsCoverages.get(it.next());
				i++;
			}

			// Create a WCSLayer array from the previous WCSCoverage array
			layerList = new WCSLayer[sz];
			for (int j = 0; j < layerList.length; j++) {
				WCSLayer lyr = new WCSLayer();
				WCSCoverage cov = coverageList[j];
				// name
				lyr.setName(cov.getName());

				// title
				lyr.setTitle(cov.getTitle());

				// description
				lyr.setDescription(cov.getAbstract());

				// srs
				lyr.addAllSrs(cov.getAllSrs());

				// native srs
				lyr.setNativeSRS(cov.getNativeSRS());

				// extents
				Set k = cov.getBBoxes().keySet();
				if (!k.isEmpty()) {
					it = k.iterator();
					while (it.hasNext()) {
						String srs = (String) it.next();
						BoundaryBox bBox = cov.getBbox(srs);
						Rectangle2D r = new Rectangle2D.Double(
												bBox.getXmin(),
												bBox.getYmin(),
												bBox.getXmax()-bBox.getXmin(),
												bBox.getYmax()-bBox.getYmin()
												);
						lyr.addExtent(srs, r);
						}
				}

				// formats
				lyr.setFormats(cov.getFormats());

				// time positions
				lyr.setTimePositions(cov.getTimePositions());

				// max res
				lyr.setMaxRes(new Point2D.Double(cov.getResX(), cov.getResY()));

				// interpolations
				lyr.setInterpolationMethods(cov.getInterpolationMethods());

				// parameters
				k = cov.axisPool.keySet();
				if (!k.isEmpty()) {
					it = k.iterator();
					while (it.hasNext()) {
						AxisDescription ad = (AxisDescription) cov.axisPool.get(it.next());
						FMapWCSParameter p = new FMapWCSParameter();
						p.setName(ad.getName());
						p.setLabel(ad.getLabel());
						p.setType(ad.getInterval()==null? FMapWCSParameter.VALUE_LIST : FMapWCSParameter.INTERVAL);
						if (p.getType()==FMapWCSParameter.VALUE_LIST) {
							p.setValueList(ad.getSingleValues());
						} else {
							p.setInterval(ad.getInterval());
						}
						lyr.addParameter(p);
					}
				}
				layerList[j] = lyr;
				coverages.put(lyr.getName(), lyr);
			}
		}
		return layerList;
	}

	/**
	 * Establishes the connection to the WCS server. Connecting to a WCS is
	 * an abstraction.<br>
	 * <p>
	 * Actually, it sends a GetCapabilities and a general DescribeCoverage
	 * request (not a coverage-specific DescribeCoverage request) to read the
	 * necessary data for building further GetCoverage requests.
	 * </p>
	 * @param override
	 * @throws IOException, DriverException.
	 */
	public boolean connect(boolean override, ICancellable cancel)
			throws IOException, ReadException {
		coverages = null;
		setHost(client.getHost());
		return client.connect(override, cancel);
	}

	/**
	 * No close operation is needed since WCS service it is a non-session based
	 * protocol. So, this does nothing and you can omit it.<br>
	 */
	public void close() {
//		connected = false;
	}

	/**
	 * Returns the label of an specific coverage given by the coverage name
	 * @param coverage name (string)
	 * @return string
	 */
	public String getLabel(String coverageName) {
		return client.getLabel(coverageName);
	}

	/**
	 * Returns the coverage's MAX extent from the server.
	 * @return Rectangle2D
	 * @throws DriverException
	 * @throws IOException
	 */
	public Rectangle2D getFullExtent(String coverageName, String srs)
			throws IOException, ReadException {
		return client.getExtent(coverageName, srs);
	}

	/**
	 * Returns the max resolution of a specific coverage given by the coverage's name.
	 * @param coverage name (string)
	 * @return double
	 */
	public Point2D getMaxResolution(String coverageName) {
		if (coverages.containsKey(coverageName)) {
			return ((WCSLayer) coverages.get(coverageName)).getMaxRes();
		}
		return null;
	}


	/**
	 * Returns an ArrayList containing a set of Strings with the coverage's SRSs.
	 * @param coverage name (string)
	 * @return ArrayList
	 */
	public ArrayList getSRSs(String coverageName) {
		if (coverages.containsKey(coverageName)) {
			return ((WCSLayer) coverages.get(coverageName)).getSRSs();
		}
		return null;
	}

	/**
	 * Returns a String containing a description of an specific coverage.
	 * @param coverage name (string)
	 * @return string
	 */
	public String getCoverageDescription(String coverageName) {
		if (coverages.containsKey(coverageName)) {
			return ((WCSLayer) coverages.get(coverageName)).getDescription();
		}
		return null;
	}

	/**
	 * Returns an ArrayList containing strings for the time positions of an
	 * specific coverage given by the coverage's name.
	 * @param coverage name (string)
	 * @return ArrayList
	 */
	public ArrayList getTimes(String coverageName) {
		if (coverages.containsKey(coverageName)) {
			return ((WCSLayer) coverages.get(coverageName)).getTimePositions();
		}
		return null;
	}

	/**
	 * Sends a GetCoverage request to the client.
	 * @param status
	 * @return
	 * @throws WCSException
	 */
	public File getCoverage(WCSStatus status, ICancellable cancel) throws WCSDriverException {
		try {
			return client.getCoverage(status, cancel);
		} catch (ServerErrorException e) {
			throw new WCSDriverException(getName(),e);
		} catch (org.gvsig.remoteClient.exceptions.WCSException e) {
			throw new WCSDriverException(getName(),e);
		}
	}

}
