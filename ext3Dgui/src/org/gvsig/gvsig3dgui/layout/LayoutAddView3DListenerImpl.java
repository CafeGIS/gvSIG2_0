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
package org.gvsig.gvsig3dgui.layout;

import java.awt.Image;
import java.awt.geom.Rectangle2D;

import org.gvsig.gvsig3dgui.layout.fframe.FFrameView3D;
import org.gvsig.gvsig3dgui.layout.fframe.FFrameView3DFractory;
import org.gvsig.gvsig3dgui.layout.fframe.gui.dialog.FFrameView3DDialog;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.tools.listener.LayoutAddRectangleListener;


/**
 * Implementación de la interfaz LayoutRectangleListener como herramienta para
 * realizar una inserción por rectángulo.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutAddView3DListenerImpl extends LayoutAddRectangleListener {
//	private final Image img = new ImageIcon(MapControl.class.getResource(
//				"images/RectSelectCursor.gif")).getImage();
	private final Image img = PluginServices.getIconTheme().get("rect-select-cursor").getImage();

	/**
	 * Crea un nuevo LayoutAddRectangleListener.
	 *
	 * @param l Layout.
	 */
	public LayoutAddView3DListenerImpl(Layout l) {
		super(l);
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.RectangleListener#rectangle(com.iver.cit.gvsig.fmap.tools.Events.RectangleEvent)
	 */
	public void rectangle(RectangleEvent event) throws BehaviorException {
		FFrameView3D fframe =(FFrameView3D)FrameFactory.createFrameFromName(FFrameView3DFractory.registerName);

		fframe.setLayout(layout);
		Rectangle2D r = new Rectangle2D.Double(); //rectOrigin.x+m_PointAnt.x,rectOrigin.y+m_PointAnt.y,m_LastPoint.x-m_PointAnt.x,m_LastPoint.y-m_PointAnt.y);

		r=getRectangle(TOLERANCE);
		fframe.setBoundBox(FLayoutUtilities.toSheetRect(r, layout.getLayoutControl().getAT()));
		FFrameView3DDialog fframedialog = new FFrameView3DDialog(layout, fframe);
		 if (fframedialog != null) {
	            fframedialog.setRectangle(fframe.getBoundingBox(layout.getLayoutControl().getAT()));
	            PluginServices.getMDIManager().addWindow(fframedialog);
	        }

	    IFFrame newFrame= fframedialog.getFFrame();
		if (newFrame!=null) {
			layout.getLayoutContext().addFFrame(newFrame, true,true);
		}
        PluginServices.getMainFrame().enableControls();
        layout.getLayoutControl().refresh();
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#getCursor()
	 */
	public Image getImageCursor() {
		return img;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.tools.Listeners.ToolListener#cancelDrawing()
	 */
	public boolean cancelDrawing() {
		return false;
	}
}
