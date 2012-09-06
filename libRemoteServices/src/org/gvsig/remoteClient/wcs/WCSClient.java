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
package org.gvsig.remoteClient.wcs;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.gvsig.remoteClient.exceptions.ServerErrorException;
import org.gvsig.remoteClient.exceptions.WCSException;
import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.wms.ICancellable;

/**
 * WCSClient managing the low-level comunication to the server. It is used
 * as a bridge between a standard WCS server and other high-level clients.
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class WCSClient extends org.gvsig.remoteClient.RasterClient{

    private WCSProtocolHandler handler;
    private Hashtable layerPool;

    /**
     * Constructor.
     * the parameter host, indicates the WCS host to connect.
     * @throws IOException
     *
     */
    public WCSClient(String host) throws ConnectException, IOException {
        setHost(host);
        try {
        	handler = WCSProtocolHandlerFactory.negotiate(host);
        	handler.setHost(host);
        } catch(ConnectException conE) {
        	throw conE;
        } catch(IOException ioE) {
        	throw ioE;
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * <p>Checks the connection to de remote WMS and requests its capabilities.</p>
     * @param override
     *
     */
    public boolean connect(boolean override, ICancellable cancel)
    {
        try {
            if (handler == null) {
                if (getHost().trim().length() > 0) {
                	handler = WCSProtocolHandlerFactory.negotiate(getHost());
                    handler.setHost(getHost());
                } else {
                    // must to specify host first!!!!
                    return false;
                }
            }
            getCapabilities(null, override, cancel);

            describeCoverage(null, override, cancel); //TODO falta posar el onlineresource del describe coverage
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends a GetCapabilities request using the properties contained by
     * the status. If status is null, then it uses the default configuration.
     * @param override
     * @param WCSStatus, containing the status properties
     */
    private void getCapabilities(WCSStatus status, boolean override, ICancellable cancel) {
        handler.getCapabilities(status, override, cancel);
    }

    /**
     * Sends a DescribeCoverage request using the properties contained by
     * the status.
     * @param override, if true the cache is ignored
     * @param WCSStatus, containing the status properties
     */
    private void describeCoverage(WCSStatus status, boolean override, ICancellable cancel) {
        handler.describeCoverage(status, override, cancel);
        // check it was response or if we need to perform a specific DescribeCoverage for each coverage
        Hashtable layers = handler.getLayers();
        Iterator it = layers.keySet().iterator();
        while (it.hasNext()) {
        	Object obj = layers.get(it.next());
        	if (obj instanceof CoverageOfferingBrief) {
        		if (status == null)
        			status = new WCSStatus();
        		status.setCoveraName( ((CoverageOfferingBrief) obj).getName());
        		handler.describeCoverage(status, override, cancel);
        	}
        }

        layerPool = handler.getLayers();
    }

    /* (non-Javadoc)
     * @see org.gvsig.remoteClient.RemoteClient#close()
     */
    public void close() {
    }

    /**
     * Returns the title of the service. The title is a human-readable string format
     * used to label the service connection.
     * @return String
     */
	public String getServiceTitle() {
		return handler.serviceInfo.title;
	}

	/**
	 * Returns the service version (1.0.0, 1.1.0, ...).
	 * @return String
	 */
	public String getVersion() {
		return handler.getVersion();
	}

	/**
	 * Returns a brief description of the service, it is a human-readable string.
	 * @return String
	 */
	public String getDescription() {
		return handler.serviceInfo.abstr;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList getFormats() {
		return handler.getFormats();
	}

	/**
	 * Returns a hash table containing the WCSCoverage's produced at parse time using
	 * the coverage names as the Hashtable's keys
	 * @return Hashtable.
	 */
	public Hashtable getCoverageList() {
		return layerPool;
	}

	/**
	 * Given a coverage name, it returns the coverage's title.
	 * @param coverageName
	 * @return String
	 */
	public String getLabel(String coverageName) {
		return ((WCSCoverage) layerPool.get(coverageName)).getTitle();
	}

	/**
	 * Given a coverage name and the CRS name, it returns the extent defined by the
	 * server in the DescribeCoverage document.
	 *
	 * @param coverageName
	 * @param crs
	 * @return Rectangle2D
	 */
	public Rectangle2D getExtent(String coverageName, String crs) {
		BoundaryBox bbox = (BoundaryBox) ((WCSCoverage) layerPool.get(coverageName)).getBbox(crs);;
        if (bbox == null) return null;
        double xmin = bbox.getXmin();
        double xmax = bbox.getXmax();
        double ymin = bbox.getYmin();
        double ymax = bbox.getYmax();
		return new Rectangle2D.Double(xmin, ymin, xmax-xmin, ymax-ymin);
	}

	/**
	 * Sends the GetCoverage request according to the settings passed in the status
	 * argument.
	 * @param status
	 * @return File
	 * @throws ServerErrorException
	 * @throws WCSException
	 */
	public File getCoverage(WCSStatus status, ICancellable cancel) throws ServerErrorException, WCSException {
		return handler.getCoverage(status, cancel);
	}
}

