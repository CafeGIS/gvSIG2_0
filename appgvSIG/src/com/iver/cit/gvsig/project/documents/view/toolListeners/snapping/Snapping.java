package com.iver.cit.gvsig.project.documents.view.toolListeners.snapping;


import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;

import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.CentralPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.FinalPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.InsertPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.IntersectionPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.MediumPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.NearestPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.PerpendicularPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.PixelSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.QuadrantPointSnapper;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.snappers.TangentPointSnapper;

/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public class Snapping {
    /**
     * DOCUMENT ME!
     */
    public static void register() {
    	ExtensionPoint extPoint = ToolsLocator.getExtensionPointManager().add(
				"Snapper", "");
    	extPoint.append("FinalPointSnapper","", FinalPointSnapper.class);
    	extPoint.append("NearestPointSnapper","", NearestPointSnapper.class);
    	extPoint.append("PixelSnapper","", PixelSnapper.class);
    	extPoint.append("CentralPointSnapper","", CentralPointSnapper.class);
    	extPoint.append("QuadrantPointSnapper", "", QuadrantPointSnapper.class);
		extPoint.append("InsertPointSnapper", "", InsertPointSnapper.class);
    	extPoint.append("IntersectionPointSnapper","", IntersectionPointSnapper.class);
    	extPoint.append("MediumPointSnapper","", MediumPointSnapper.class);
    	extPoint.append("PerpendicularPointSnapper","", PerpendicularPointSnapper.class);
    	extPoint.append("TangentPointSnapper","", TangentPointSnapper.class);
    }
}
