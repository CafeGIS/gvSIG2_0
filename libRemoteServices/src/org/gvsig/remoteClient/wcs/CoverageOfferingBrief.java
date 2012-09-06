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

/* CVS MESSAGES:
*
* $Id: CoverageOfferingBrief.java 7010 2006-09-04 16:12:33Z jaume $
* $Log$
* Revision 1.1  2006-09-04 16:12:17  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.remoteClient.wcs;

import java.io.IOException;

import org.gvsig.remoteClient.utils.BoundaryBox;
import org.gvsig.remoteClient.utils.CapabilitiesTags;
import org.gvsig.remoteClient.utils.DescribeCoverageTags;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public class CoverageOfferingBrief {
	private String name;
	private String label;
	private BoundaryBox		lonLatBbox;

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public BoundaryBox getLonLatBbox() {
		return lonLatBbox;
	}
	public void setLonLatBbox(BoundaryBox lonLatBbox) {
		this.lonLatBbox = lonLatBbox;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void parse(KXmlParser parser) throws XmlPullParserException, IOException {
		int currentTag;
		boolean end = false;

		parser.require(KXmlParser.START_TAG, null, "wcs:"+CapabilitiesTags.WCS_COVERAGEOFFERINGBRIEF);
		currentTag = parser.next();

		while (!end)
		{
			switch(currentTag)
			{
			case KXmlParser.START_TAG:

				if (parser.getName().toLowerCase().compareTo(CapabilitiesTags.NAME.toLowerCase())==0) {
					this.name = parser.nextText();
				} else if (parser.getName().compareTo(CapabilitiesTags.WCS_LABEL)==0) {
					this.label = parser.nextText();
				} else if (parser.getName().compareTo(DescribeCoverageTags.LONLAT_ENVELOPE)==0) {
					parser.nextTag();
					this.lonLatBbox = new BoundaryBox();
					this.lonLatBbox.setSrs(DescribeCoverageTags.WGS84);
					parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GML_POS);
					String[][] coord = new String[2][2];
					coord[0] = parser.nextText().split(WCSCoverage.SEPARATOR);
					parser.nextTag();
					parser.require(KXmlParser.START_TAG, null, DescribeCoverageTags.GML_POS);
					coord[1] = parser.nextText().split(WCSCoverage.SEPARATOR);
					this.lonLatBbox.setXmin(Double.parseDouble(coord[0][0]));
					this.lonLatBbox.setYmin(Double.parseDouble(coord[0][1]));
					this.lonLatBbox.setXmax(Double.parseDouble(coord[1][0]));
					this.lonLatBbox.setYmax(Double.parseDouble(coord[1][1]));
				}

			case KXmlParser.END_TAG:
				if (parser.getName().compareTo("wcs:"+CapabilitiesTags.WCS_COVERAGEOFFERINGBRIEF) == 0)
					end = true;
				break;
			case KXmlParser.TEXT:
				break;
			}
			currentTag = parser.next();
		}

	}
}
