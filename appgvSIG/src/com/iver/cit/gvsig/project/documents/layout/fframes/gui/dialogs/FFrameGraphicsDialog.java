/*
 * Created on 02-ago-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.styling.SymbolSelector;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphics;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo de las propiedades de los gráficos.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGraphicsDialog extends SymbolSelector
    implements IFFrameDialog {
    private Rectangle2D rect = new Rectangle2D.Double();
    private Layout m_layout = null; //  @jve:visual-info  decl-index=0 visual-constraint="393,10"
    private boolean isAcepted = false;
    private FFrameGraphics fframegraphics = null;
//    private javax.swing.JButton bAceptar = null;
//    private javax.swing.JButton bCancelar = null;
    private JPRotation pRotation = null;
    private FFrameGraphics newFFrameGraphics;

    public FFrameGraphicsDialog(Layout layout, FFrameGraphics fframe){
        super(fframe.getFSymbol(), fframe.getShapeType(), null,true);
        m_layout = layout;
        fframegraphics = fframe;
        initialize();
    }

   /**
     * This method initializes this
     */
    private void initialize() {
//		this.add(getBAceptar(), null);
//		this.add(getBCancelar(), null);
        this.setSize(650, 700);
        getPRotation().setRotation(fframegraphics.getRotation());
        ((GridBagLayoutPanel)getJPanelOptions()).addComponent(getPRotation());

        ActionListener okAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newFFrameGraphics=(FFrameGraphics)fframegraphics.cloneFFrame(m_layout);
                newFFrameGraphics.setBoundBox(fframegraphics.getBoundBox());
                newFFrameGraphics.setSymbol((ISymbol)getSelectedObject());
                newFFrameGraphics.setRotation(getPRotation().getRotation());
                m_layout.getLayoutContext().updateFFrames();
                m_layout.getLayoutControl().refresh();
                isAcepted = true;
            }
        };
        okCancelPanel.addOkButtonActionListener(okAction);
     }

    /**
     * @see com.iver.mdiApp.ui.MDIManager.SingletonWindow#getWindowModel()
     */
    public Object getViewModel() {
        return "FPanelLegendDefault";
    }

    /**
     * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
     */
    public WindowInfo getWindowInfo() {
        WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewinfo.setTitle(PluginServices.getText(this, "propiedades_grafico"));

        return m_viewinfo;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog#setRectangle(java.awt.geom.Rectangle2D)
     */
    public void setRectangle(Rectangle2D r) {
        rect.setRect(r);
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog#getIsAcepted()
     */
    public boolean getIsAcepted() {
        return isAcepted;
    }

    /**
     * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
     */
    public void viewActivated() {
    }

    /**
     * This method initializes pRotation
     *
     * @return javax.swing.JPanel
     */
    private JPRotation getPRotation() {
        if (pRotation == null) {
            pRotation = new JPRotation();
            pRotation.setPreferredSize(new Dimension(120, 120));
        }
        return pRotation;
    }

    public IFFrame getFFrame() {
        return newFFrameGraphics;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
