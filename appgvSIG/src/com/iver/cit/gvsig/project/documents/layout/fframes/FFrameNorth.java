/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameNorthDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * Class that extends FFramePicture and implements IFFrameViewDependence to be
 * able to maintain the same rotation that the view to which associate.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameNorth extends FFramePicture implements IFFrameViewDependence {
    private FFrameView fframeview;
    private int dependenceIndex = -1;

    /**
     * Inserts FFrameView to be able to relate this with the image.
     *
     * @param fv FFrameView
     */
    public void setFFrameDependence(IFFrame fv) {
        fframeview = (FFrameView) fv;
    }
    public void refreshDependence(IFFrame fant, IFFrame fnew) {
    	if ((fframeview != null) &&
                fframeview.equals(fant)) {
            fframeview=(FFrameView)fnew;
    	}
    }
    /**
     * Returns the FFrameView.
     *
     * @return FFrameView
     */
    public IFFrame[] getFFrameDependence() {
        return new IFFrame[]{fframeview};
    }

    /**
     * Returns the rotation of de FFrameView.
     *
     * @return Rotation.
     */
    public double getRotation() {
        if (fframeview != null) {
            return fframeview.getRotation();
        }

        return super.getRotation();
    }

    /**
     * Returns a XMLEntity to save de properties.
     *
     * @return XMLEntity with the properties of FFrameNorth.
     *
     * @throws SaveException
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();

        try {
//            xml.putProperty("type", Layout.RECTANGLENORTH);

            if (fframeview != null) {
                Layout layout = fframeview.getLayout();

                if (layout != null) {
                    IFFrame[] fframes = layout.getLayoutContext().getAllFFrames();

                    for (int i = 0; i < fframes.length; i++) {
                        if ((fframeview != null) &&
                                fframeview.compare(fframes[i])) {
                            xml.putProperty("index", i);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new SaveException(e, this.getClass().getName());
        }

        return xml;
    }

    /**
     * Inserts the properties of FFrameNorth from a XMLEntity and the Layout.
     *
     * @param xml XMLEntity with the properties.
     */
    public void setXMLEntity(XMLEntity xml) {
        super.setXMLEntity(xml);

        if (xml.contains("index")) {
            dependenceIndex = xml.getIntProperty("index");
        }
    }

    /**
     * Update the dependences that have this FFrame with the other FFrame.
     *
     * @param fframes Other FFrames.
     */
    public void initDependence(IFFrame[] fframes) {
        if ((dependenceIndex != -1) &&
                fframes[dependenceIndex] instanceof FFrameView) {
            fframeview = (FFrameView) fframes[dependenceIndex];
        }
    }

	public IFFrameDialog getPropertyDialog() {
		return new FFrameNorthDialog(getLayout(),this);
	}
}
