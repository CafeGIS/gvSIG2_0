/*
 * Created on 22-jul-2004
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
package com.iver.cit.gvsig.project.documents.layout.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.TreeMap;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.commands.FrameCommandsRecord;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Clase que hace de Listener de FAlignDialog.
 *
 * @author Vicente Caballero Navarro
 */
public class EventsFAlign implements ActionListener {
    private Layout m_layout;
    private boolean inLayout = false;

    /**
     * Crea un nuevo FAlign.
     *
     * @param layout Referencia al Layout.
     */
    public EventsFAlign(Layout layout) {
        m_layout = layout;
    }

    /**
     * Desplaza los fframes seleccionados a la izquierda del fframe más
     * occidental.
     */
    private void alignLeft() {
        double xmin = Double.MAX_VALUE;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (xmin > fframe.getBoundBox().getMinX()) {
                xmin = fframe.getBoundBox().getMinX();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_left"));

        for (int i = fframes.length - 1; i >= 0; i--) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmin;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la izquierda del Layout.
     */
    private void alignLeftL() {
        double xmin = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this,"align_to_layout_left"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmin;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados al centro del fframe mas ancho de
     * forma horizontal.
     */
    private void alignCenterV() {
        double xcenter = 0;
        double w = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (w < fframe.getBoundBox().getWidth()) {
                w = fframe.getBoundBox().getWidth();
                xcenter = fframe.getBoundBox().getCenterX();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_center"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xcenter - (fframe.getBoundBox().width / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados al centro del Layout horizontalmente.
     */
    private void alignCenterVL() {
        double xcenter = 0;
        xcenter = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho() / 2;

        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this,"align_to_layout_center"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xcenter - (fframe.getBoundBox().width / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte derecha del fframe más
     * oriental.
     */
    private void alignRight() {
        double xmax = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (xmax < fframe.getBoundBox().getMaxX()) {
                xmax = fframe.getBoundBox().getMaxX();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_right"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmax - fframes[i].getBoundBox().width;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte derecha del Layout.
     */
    private void alignRightL() {
        double xmax = 0;
        xmax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho();

        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_to_layout_right"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmax - fframes[i].getBoundBox().width;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte inferior del fframe más
     * hacia abajo.
     */
    private void alignDown() {
        double ymax = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (ymax < fframe.getBoundBox().getMaxY()) {
                ymax = fframe.getBoundBox().getMaxY();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_down"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);

            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymax - fframe.getBoundBox().height;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte inferior del Layout.
     */
    private void alignDownL() {
        double ymax = 0;
        ymax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto();

        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_to_layout_down"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymax - fframe.getBoundBox().height;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte superior del fframe que
     * más arriba este colocado.
     */
    private void alignUp() {
        double ymin = Double.MAX_VALUE;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (ymin > fframe.getBoundBox().getMinY()) {
                ymin = fframe.getBoundBox().getMinY();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_up"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymin;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados a la parte superior del Layout.
     */
    private void alignUpL() {
        double ymin = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_to_layout_up"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymin;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados al centro del fframe más alto
     * verticalmente.
     */
    private void alignCenterH() {
        double ycenter = 0;
        double h = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (h < fframe.getBoundBox().getHeight()) {
                h = fframe.getBoundBox().getHeight();
                ycenter = fframe.getBoundBox().getCenterY();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_vertical_center"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ycenter - (fframe.getBoundBox().height / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Desplaza los fframes seleccionados en el Layout al centro verticalmente.
     */
    private void alignCenterHL() {
        double ycenter = 0;
        ycenter = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto() / 2;

        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "align_to_layout_vertical_center"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ycenter - (fframe.getBoundBox().height / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y vertical,
     * de izquierda a derecha.
     */
    private void distLeft() {
        double xmin = Double.MAX_VALUE;
        double xmax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (xmin > fframe.getBoundBox().getMinX()) {
                xmin = fframe.getBoundBox().getMinX();
            }

            if (xmax < fframe.getBoundBox().getMaxX()) {
                xmax = fframe.getBoundBox().getMaxX();
            }
        }

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_left"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmin + (dist * i);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y vertical, de izquierda a derecha.
     */
    private void distLeftL() {
        double xmin = 0;
        double xmax = 0;
        xmax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_left"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmin + (dist * i);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y vertical,
     * de derecha a izquierda.
     */
    private void distRight() {
        double xmin = Double.MAX_VALUE;
        double xmax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (xmin > fframe.getBoundBox().getMinX()) {
                xmin = fframe.getBoundBox().getMinX();
            }

            if (xmax < fframe.getBoundBox().getMaxX()) {
                xmax = fframe.getBoundBox().getMaxX();
            }
        }

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_right"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmax - (dist * i) - fframe.getBoundBox().width;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y vertical, de derecha a izquierda.
     */
    private void distRightL() {
        double xmin = 0;
        double xmax = 0;
        xmax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_right"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = xmax - (dist * i) - fframe.getBoundBox().width;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y vertical,
     * desde arriba hacia abajo.
     */
    private void distUp() {
        double ymin = Double.MAX_VALUE;
        double ymax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (ymin > fframe.getBoundBox().getMinY()) {
                ymin = fframe.getBoundBox().getMinY();
            }

            if (ymax < fframe.getBoundBox().getMaxY()) {
                ymax = fframe.getBoundBox().getMaxY();
            }
        }

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_up"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymin + (dist * i);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y vertical, desde arriba hacia abajo.
     */
    private void distUpL() {
        double ymin = 0;
        double ymax = 0;
        ymax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        num = fframes.length;

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_up"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymin + (dist * i);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y vertical,
     * desde bajo hacia arriba.
     */
    private void distDown() {
        double ymin = Double.MAX_VALUE;
        double ymax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (ymin > fframe.getBoundBox().getMinY()) {
                ymin = fframe.getBoundBox().getMinY();
            }

            if (ymax < fframe.getBoundBox().getMaxY()) {
                ymax = fframe.getBoundBox().getMaxY();
            }
        }

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_down"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymax - (dist * i) - fframe.getBoundBox().height;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y vertical, desde bajo hacia arriba.
     */
    private void distDownL() {
        double ymin = 0;
        double ymax = 0;
        ymax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        num = fframes.length;

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_down"));

        for (int i = fframes.length - 1; i >= 0; i--) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = ymax - (dist * i) - fframe.getBoundBox().height;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y vertical.
     */
    private void distCenterH() {
        double xmin = Double.MAX_VALUE;
        double xmax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (xmin > fframe.getBoundBox().getMinX()) {
                xmin = fframe.getBoundBox().getMinX();
            }

            if (xmax < fframe.getBoundBox().getMaxX()) {
                xmax = fframe.getBoundBox().getMaxX();
            }
        }

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_vertical"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = (xmin + (((dist) * (i + 1)) - (dist / 2))) -
                (fframe.getBoundBox().width / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y vertical.
     */
    private void distCenterHL() {
        double xmin = 0;
        double xmax = 0;
        xmax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        double dif = xmax - xmin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_vertical"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.x = (xmin + (((dist) * (i + 1)) - (dist / 2))) -
                (fframe.getBoundBox().width / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados de forma equidistante y horizontal.
     */
    private void distCenterV() {
        double ymin = Double.MAX_VALUE;
        double ymax = Double.NEGATIVE_INFINITY;
        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];

            if (ymin > fframe.getBoundBox().getMinY()) {
                ymin = fframe.getBoundBox().getMinY();
            }

            if (ymax < fframe.getBoundBox().getMaxY()) {
                ymax = fframe.getBoundBox().getMaxY();
            }
        }

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_horizontal"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = (ymin + (((dist) * (i + 1)) - (dist / 2))) -
                (fframe.getBoundBox().height / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye los fframes seleccionados en el Layout de forma equidistante
     * y horizontal.
     */
    private void distCenterVL() {
        double ymin = 0;
        double ymax = 0;
        ymax = m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto();

        int num = 0;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        double dif = ymax - ymin;
        double dist = dif / num;
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "distributes_to_layout_horizontal"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.y = (ymin + (((dist) * (i + 1)) - (dist / 2))) -
                (fframe.getBoundBox().height / 2);
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Cambia la anchura de los fframes seleccionados por la anchura del más
     * ancho.
     */
    private void sizeWidth() {
        double wmax = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (wmax < fframe.getBoundBox().getWidth()) {
                wmax = fframe.getBoundBox().getWidth();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "change_width"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.width = wmax;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Cambia la altura de los fframes seleccionados por la altura del mas
     * alto.
     */
    private void sizeHeight() {
        double hmax = Double.NEGATIVE_INFINITY;
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i];

            if (hmax < fframe.getBoundBox().getHeight()) {
                hmax = fframe.getBoundBox().getHeight();
            }
        }

        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "change_height"));

        for (int i = 0; i < fframes.length; i++) {
            IFFrame fframe = fframes[i].cloneFFrame(m_layout);
            Rectangle2D.Double r = (Rectangle2D.Double) fframe.getBoundBox()
                                                              .clone();
            r.height = hmax;
            fframe.setBoundBox(r);
            efs.update(fframes[i], fframe);
        }

        efs.endComplex();
    }

    /**
     * Distribuye horizontalmente los fframes dejando como espacio entre ellos
     * la media de todos los espacios.
     */
    private void spaceRight() {
        double total = 0;
        double num = 0;
        double xmin = Double.MAX_VALUE;
        double xmax = Double.NEGATIVE_INFINITY;

        TreeMap treemap = new TreeMap();
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];
            treemap.put(new Double(fframe.getBoundBox().getMinX()), fframe);
        }

        Iterator j = treemap.keySet().iterator();

        if (j.hasNext()) {
            IFFrame fframeA = (IFFrame) treemap.get(j.next());
            IFFrame fframeS = null;
            xmin = fframeA.getBoundBox().x;

            while (j.hasNext()) {
                fframeS = (IFFrame) treemap.get(j.next());
                total += (fframeS.getBoundBox().getMinX() -
                fframeA.getBoundBox().getMaxX());
                fframeA = fframeS;
            }

            if (fframeS != null) {
                xmax = fframeS.getBoundBox().getMaxX();
            }
        }

        if (inLayout) {
            total += (xmin +
            (m_layout.getLayoutContext().getAttributes().m_sizePaper.getAncho() - xmax));
            num += 2;
        }

        double dist = total / (num - 1);
        Iterator k = treemap.keySet().iterator();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "horizontal_space"));

        if (k.hasNext()) {
            IFFrame fframe = (IFFrame) treemap.get(k.next());
            IFFrame fframeA = fframe.cloneFFrame(m_layout);

            if (inLayout) {
                Rectangle2D.Double r = (Rectangle2D.Double) fframeA.getBoundBox()
                                                                   .clone();
                r.x = dist;
                fframeA.setBoundBox(r);
                efs.update(fframe, fframeA);
            }

            while (k.hasNext()) {
                IFFrame fframeAux = (IFFrame) treemap.get(k.next());
                IFFrame fframeS = fframeAux.cloneFFrame(m_layout);
                Rectangle2D.Double r = (Rectangle2D.Double) fframeS.getBoundBox()
                                                                   .clone();
                r.x = fframeA.getBoundBox().getMaxX() + dist;
                fframeS.setBoundBox(r);
                efs.update(fframeAux, fframeS);
                fframeA = fframeS;
            }
        }

        efs.endComplex();
    }

    /**
     * Distribuye verticalmente los fframes dejando como espacio entre ellos la
     * media de todos los espacios.
     */
    private void spaceDown() {
        double total = 0;
        double num = 0;
        double ymin = Double.MAX_VALUE;
        double ymax = Double.NEGATIVE_INFINITY;

        TreeMap treemap = new TreeMap();
        IFFrame[] fframes = m_layout.getLayoutContext().getFFrameSelected();
        num = fframes.length;

        for (int i = 0; i < num; i++) {
            IFFrame fframe = fframes[i];
            treemap.put(new Double(fframe.getBoundBox().getMinY()), fframe);
        }

        Iterator j = treemap.keySet().iterator();

        if (j.hasNext()) {
            IFFrame fframeA = (IFFrame) treemap.get(j.next());
            ymin = fframeA.getBoundBox().y;

            IFFrame fframeS = null;

            while (j.hasNext()) {
                fframeS = (IFFrame) treemap.get(j.next());
                total += (fframeS.getBoundBox().getMinY() -
                fframeA.getBoundBox().getMaxY());
                fframeA = fframeS;
            }

            if (fframeS != null) {
                ymax = fframeS.getBoundBox().getMaxY();
            }
        }

        if (inLayout) {
            total += (ymin +
            (m_layout.getLayoutContext().getAttributes().m_sizePaper.getAlto() - ymax));
            num += 2;
        }

        double dist = total / (num - 1);
        Iterator k = treemap.keySet().iterator();
        FrameCommandsRecord efs = m_layout.getLayoutContext().getFrameCommandsRecord();
        efs.startComplex(PluginServices.getText(this, "vertical_space"));

        if (k.hasNext()) {
            IFFrame fframe = (IFFrame) treemap.get(k.next());
            IFFrame fframeA = fframe.cloneFFrame(m_layout);

            if (inLayout) {
                Rectangle2D.Double r = (Rectangle2D.Double) fframeA.getBoundBox()
                                                                   .clone();
                r.y = dist;
                fframeA.setBoundBox(r);
                efs.update(fframe, fframeA);
            }

            while (k.hasNext()) {
                IFFrame fframeAux = (IFFrame) treemap.get(k.next());
                IFFrame fframeS = fframeAux.cloneFFrame(m_layout);
                Rectangle2D.Double r = (Rectangle2D.Double) fframeS.getBoundBox()
                                                                   .clone();
                r.y = fframeA.getBoundBox().getMaxY() + dist;
                fframeS.setBoundBox(r);
                efs.update(fframeAux, fframeS);
                fframeA = fframeS;
            }
        }

        efs.endComplex();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        m_layout.getLayoutContext().updateFFrames();

        if (!inLayout) {
            if (e.getActionCommand() == "LEFT") {
                alignLeft();
            } else if (e.getActionCommand() == "CENTERV") {
                alignCenterV();
            } else if (e.getActionCommand() == "RIGHT") {
                alignRight();
            } else if (e.getActionCommand() == "UP") {
                alignUp();
            } else if (e.getActionCommand() == "CENTERH") {
                alignCenterH();
            } else if (e.getActionCommand() == "DOWN") {
                alignDown();
            } else if (e.getActionCommand() == "DISTUP") {
                distUp();
            } else if (e.getActionCommand() == "DISTCENTERV") {
                distCenterV();
            } else if (e.getActionCommand() == "DISTDOWN") {
                distDown();
            } else if (e.getActionCommand() == "DISTLEFT") {
                distLeft();
            } else if (e.getActionCommand() == "DISTCENTERH") {
                distCenterH();
            } else if (e.getActionCommand() == "DISTRIGHT") {
                distRight();
            } else if (e.getActionCommand() == "SIZECENTERV") {
                sizeWidth();
            } else if (e.getActionCommand() == "SIZECENTERH") {
                sizeHeight();
            } else if (e.getActionCommand() == "SIZEOTHER") {
                sizeWidth();
                m_layout.getLayoutContext().updateFFrames();
                sizeHeight();
            } else if (e.getActionCommand() == "SPACERIGHT") {
                spaceRight();
            } else if (e.getActionCommand() == "SPACEDOWN") {
                spaceDown();
            } else if (e.getActionCommand() == "INLAYOUT") {
                inLayout = true;
            }
        } else {
            if (e.getActionCommand() == "LEFT") {
                alignLeftL();
            } else if (e.getActionCommand() == "CENTERV") {
                alignCenterVL();
            } else if (e.getActionCommand() == "RIGHT") {
                alignRightL();
            } else if (e.getActionCommand() == "UP") {
                alignUpL();
            } else if (e.getActionCommand() == "CENTERH") {
                alignCenterHL();
            } else if (e.getActionCommand() == "DOWN") {
                alignDownL();
            } else if (e.getActionCommand() == "DISTUP") {
                distUpL();
            } else if (e.getActionCommand() == "DISTCENTERV") {
                distCenterVL();
            } else if (e.getActionCommand() == "DISTDOWN") {
                distDownL();
            } else if (e.getActionCommand() == "DISTLEFT") {
                distLeftL();
            } else if (e.getActionCommand() == "DISTCENTERH") {
                distCenterHL();
            } else if (e.getActionCommand() == "DISTRIGHT") {
                distRightL();
            } else if (e.getActionCommand() == "SIZECENTERV") {
                sizeWidth();
            } else if (e.getActionCommand() == "SIZECENTERH") {
                sizeHeight();
            } else if (e.getActionCommand() == "SIZEOTHER") {
                sizeWidth();
                m_layout.getLayoutContext().updateFFrames();
                sizeHeight();
            } else if (e.getActionCommand() == "SPACERIGHT") {
                spaceRight();
            } else if (e.getActionCommand() == "SPACEDOWN") {
                spaceDown();
            } else if (e.getActionCommand() == "INLAYOUT") {
                inLayout = false;
            }
        }

        m_layout.getLayoutContext().updateFFrames();
        m_layout.getLayoutContext().callLayoutDrawListeners();
    }
}
