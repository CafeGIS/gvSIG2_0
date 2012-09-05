/*
 * Created on 27-jul-2004
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
package com.iver.cit.gvsig.project.documents.layout;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroupFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameLegend;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.layout.gui.dialogs.FAlignDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.dialogs.FBorderDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.dialogs.FPositionDialog;


/**
 * Operaciones realizadas sobre el conjunto de FFrames.
 *
 * @author Vicente Caballero Navarro
 */
public class FLayoutGraphics {
	private Layout layout;
	private FAlignDialog m_alignLayout = null;
	private FBorderDialog borderdialog = null;
	private FPositionDialog positiondialog = null;

	/**
	 * Crea un nuevo FLayoutGraphics.
	 *
	 * @param l Referencia al Layout.
	 */
	public FLayoutGraphics(Layout l) {
		layout = l;
	}

	/**
	 * Transforma un FFrameLegend a FFrames de tipo FFrameSymbol y FFrameText.
	 */
	public void simplify() {
		layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"simplify"));
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = fframes.length - 1; i >= 0; i--) {
			IFFrame fframe = fframes[i];

			if (fframe instanceof FFrameLegend) {
				if (fframe.getSelected() != IFFrame.NOSELECT) {
					((FFrameLegend) fframe).toFFrames(layout.getLayoutContext());
				}
			}
		}
		layout.getLayoutContext().getFrameCommandsRecord().endComplex();
		layout.getLayoutControl().refresh();
	}

	/**
	 * Agrupar en un FFrameGroup todos los FFrames seleccionados.
	 */
	public void grouping() {
		//		Se debe controlar de alguna forma si hay varios seleccionados.
		FFrameGroup fframegroup =(FFrameGroup)FrameFactory.createFrameFromName(FFrameGroupFactory.registerName);

		fframegroup.setLayout(layout);
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		if (fframes.length > 1) {
			ArrayList selecList = new ArrayList();

			for (int i = fframes.length - 1; i >= 0; i--) {
				IFFrame fframe = fframes[i];

				if (fframe.getSelected() != IFFrame.NOSELECT) {
					selecList.add(fframe);
					layout.getLayoutContext().delFFrame(fframe);
				}
			}

			for (int i = selecList.size() - 1; i >= 0; i--) {
				fframegroup.addFFrame((IFFrame) selecList.get(i));
			}

			fframegroup.setAt(layout.getLayoutControl().getAT());

			Rectangle2D.Double rd = fframegroup.getRectangle(layout.getLayoutControl().getAT());

			Rectangle2D.Double rd1 = FLayoutUtilities.toSheetRect(rd,
					layout.getLayoutControl().getAT());

			fframegroup.setBoundBox(rd1);
			fframegroup.setSelected(true);
			layout.getLayoutContext().addFFrame(fframegroup, true,true);
			layout.getLayoutControl().refresh();
		}
	}

	/**
	 * Desagrupar los FFrames que estan contenidos dentro del FFrameGroup en
	 * FFrames individuales.
	 */
	public void ungrouping() {
		layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"ungroup"));
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = fframes.length - 1; i >= 0; i--) {
			IFFrame fframe = fframes[i];

			if (fframe.getSelected() != IFFrame.NOSELECT) {
				if (fframe instanceof FFrameGroup) {
					FFrameGroup fframegroup = (FFrameGroup) fframe;
					ArrayList selecList = new ArrayList();

					for (int j = fframegroup.getFFrames().length - 1; j >= 0;
							j--) {
						selecList.add(fframegroup.getFFrames()[j]);
					}

					for (int j = selecList.size() - 1; j >= 0; j--) {
						IFFrame frame=(IFFrame) selecList.get(j);
						frame.setRotation(frame.getRotation()+fframe.getRotation());
						layout.getLayoutContext().addFFrameSameProperties(frame);
					}
					layout.getLayoutContext().delFFrame(fframegroup);
				}
			}
		}
		layout.getLayoutContext().getFrameCommandsRecord().endComplex();
		layout.getLayoutControl().refresh();
	}

	/**
	 * Abre el diálogo para alinear los FFrames.
	 */
	public void aligning() {
		m_alignLayout = new FAlignDialog(layout);
		PluginServices.getMDIManager().addWindow(m_alignLayout);
	}
	/**
	 * Posiciona los FFrames seleccionados delante de los no seleccionados.
	 */
	public void before() {
		layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"change_before"));
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = fframes.length - 1; i >= 0; i--) {
			IFFrame fframe = fframes[i];
			if (fframe.getSelected() != IFFrame.NOSELECT) {
				if (fframe instanceof FFrameGroup) {
					((FFrameGroup) fframe).setAt(layout.getLayoutControl().getAT());
				}

				IFFrame fframeAux=fframe.cloneFFrame(layout);
				fframeAux.setLevel(layout.getLayoutContext().getNumBefore());
				layout.getLayoutContext().getFrameCommandsRecord().update(fframe,fframeAux);
				fframeAux.getBoundingBox(layout.getLayoutControl().getAT());
			}
		}
		layout.getLayoutContext().getFrameCommandsRecord().endComplex();
		layout.getLayoutContext().updateFFrames();
		layout.getLayoutControl().refresh();
	}

	/**
	 * Posiciona los FFrames seleccionados detrás de los FFrames no
	 * seleccionados.
	 */
	public void behind() {
		layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"change_behind"));
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = fframes.length - 1; i >= 0; i--) {
			IFFrame fframe = fframes[i];
			if (fframe.getSelected() != IFFrame.NOSELECT) {
				if (fframe instanceof FFrameGroup) {
					((FFrameGroup) fframe).setAt(layout.getLayoutControl().getAT());
				}

				IFFrame fframeAux=fframe.cloneFFrame(layout);
				fframeAux.setLevel(layout.getLayoutContext().getNumBehind());
				layout.getLayoutContext().getFrameCommandsRecord().update(fframe,fframeAux);
				fframeAux.getBoundingBox(layout.getLayoutControl().getAT());
			}
		}
		layout.getLayoutContext().getFrameCommandsRecord().endComplex();
		layout.getLayoutContext().updateFFrames();
		layout.getLayoutControl().refresh();
	}

	/**
	 * Abre el diálogo adecuadao a las propiedades del FFrame seleccionado,
	 * ahora mismo solo se abre cuando hay un solo FFrame seleccionado.
	 */
	public boolean openFFrameDialog() {
		IFFrame[] selecList = layout.getLayoutContext().getFFrameSelected();

		if (selecList.length == 1) {
			IFFrame frame=selecList[0];
//			int toolaux = layout.getTool();
//			layout.setTool(getType(frame));
//			IFFrame fframeAux=frame.cloneFFrame(layout);
			IFFrame fframeAux=layout.openFFrameDialog(frame);
			if (fframeAux!=null) {
				if (fframeAux instanceof IFFrameUseFMap)
					((IFFrameUseFMap)fframeAux).refresh();
				layout.getLayoutContext().getFrameCommandsRecord().update(frame,fframeAux);
				fframeAux.getBoundingBox(layout.getLayoutControl().getAT());
				layout.getLayoutContext().updateFFrames();
				layout.getLayoutControl().setIsReSel(true);
				layout.getLayoutControl().refresh();
			}
//			layout.setTool(toolaux);
			if (fframeAux!=null)
				return true;
		}
		return false;
	}

	/**
	 * Selección de todos los FFrames del Layout.
	 */
	public void selecAll() {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		for (int i = fframes.length - 1; i >= 0; i--) {
			IFFrame fframe =fframes[i];
			fframe.setSelected(true);
		}
		layout.getLayoutControl().refresh();
	}

	/**
	 * Abre el diálogo para añadir un borde a los fframes.
	 */
	public boolean border() {
		borderdialog = new FBorderDialog(layout);
		PluginServices.getMDIManager().addWindow(borderdialog);
		return borderdialog.isAccepted();
	}

	/**
	 * Abre el diálogo de cambio de posición y tamaño del FFrame.
	 */
	public void position() {

		IFFrame[] fframes=layout.getLayoutContext().getFFrameSelected();
		if (fframes.length!=0){
			for (int i=0;i<fframes.length;i++){
				positiondialog = new FPositionDialog(layout,fframes[i]);
				PluginServices.getMDIManager().addWindow(positiondialog);
			}
		}
	}
}
