package com.iver.cit.gvsig;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.impl.DefaultFeatureStoreNotification;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.layers.VectorialLayerEdited;

/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * $Id: EditionChangeManager.java 27268 2009-03-10 13:25:53Z vcaballero $
 * $Log$
 * Revision 1.16  2007-09-19 15:55:42  jaume
 * removed unnecessary imports
 *
 * Revision 1.15  2007/06/22 10:48:00  caballero
 * borrar selecci�n
 *
 * Revision 1.14  2007/03/21 12:25:41  caballero
 * zoom remove cacheDrawing
 *
 * Revision 1.13  2007/02/13 17:10:06  caballero
 * expresion
 *
 * Revision 1.12  2006/11/28 13:18:32  fjp
 * No redibujar cuando se a�ade algo.
 * Para que se dibuje con el s�mbolo por defecto del layer, habr� que tocar
 * en los CADTool
 *
 * Revision 1.11  2006/09/15 10:42:17  caballero
 * extensibilidad de documentos
 *
 * Revision 1.10  2006/08/29 07:56:33  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.9  2006/08/29 07:13:57  cesar
 * Rename class com.iver.andami.ui.mdiManager.View to com.iver.andami.ui.mdiManager.IWindow
 *
 * Revision 1.8  2006/08/08 07:19:05  caballero
 * afterRowEditEvent con IRow
 *
 * Revision 1.7  2006/07/20 11:03:27  fjp
 * *** empty log message ***
 *
 * Revision 1.6  2006/07/13 12:36:01  fjp
 * Revisar bien lo de a�adir campos y gestionar un campo gris
 *
 * Revision 1.5  2006/06/21 07:22:48  fjp
 * Posibilidad de marcar capas como "dirty" y tener una que guarde lo que se ha dibujado antes que ella. Al hacer un MapControl.rePaintDirtyLayers(), eso se tiene en cuenta en el redibujado.
 *
 * Revision 1.4  2006/05/16 07:06:02  caballero
 * Saber si se realiza una operaci�n desde la vista o desde la tabla.
 *
 * Revision 1.3  2006/05/10 06:26:24  caballero
 * comprobar si tiene capa asociada
 *
 * Revision 1.2  2006/05/09 09:26:04  caballero
 * refrescar las vistas y tablas
 *
 * Revision 1.1  2006/05/05 09:06:09  jorpiell
 * Se a a�adido la clase EditionChangeManager, que no es m�s que un listener que se ejecuta cuando se produce un evento de edici�n.
 *
 *
 */
/**
 * Cuando un tema se pone en edici�n se le debe asociar
 * un listener de este tipo, que se dispar� cuando se produzca
 * un evento de edici�n (borrado, modificaci�n,... sobre la capa.
 *
 * @author Jorge Piera Llodr� (piera_jor@gva.es)
 */
public class EditionChangeManager implements Observer{
	private FLayer fLayer = null;

	/**
	 * Constructor
	 * @param fLayer
	 * Tema que se est� editando
	 */
	public EditionChangeManager(FLayer fLayer){
		this.fLayer = fLayer;
	}
	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.fmap.edition.IEditionListener#afterRowEditEvent(com.iver.cit.gvsig.fmap.edition.AfterRowEditEvent)
	 */
//	public void afterRowEditEvent(IRow feat, AfterRowEditEvent e) {
//		IWindow[] views = PluginServices.getMDIManager().getAllWindows();
//
//		for (int i=0 ; i<views.length ; i++){
//			if (views[i] instanceof Table){
////				Table table=(Table)views[i];
//				///VCN Creo que no hace falta refrescar la tabla aqu�
////				if (table.getModel().getAssociatedTable()!=null && table.getModel().getAssociatedTable().equals(fLayer))
////					table.refresh();
//			}else if (views[i] instanceof com.iver.cit.gvsig.project.documents.view.gui.View){
//				com.iver.cit.gvsig.project.documents.view.gui.View view=(com.iver.cit.gvsig.project.documents.view.gui.View)views[i];
//
//				if (e.getChangeType() == EditionEvent.CHANGE_TYPE_ADD) {
//					// No redraw, just image paint
//					view.getMapControl().repaint();
//				}else if (e.getChangeType() == EditionEvent.CHANGE_TYPE_DELETE){
//					EditionManager em=CADExtension.getEditionManager();
//					if (em.getActiveLayerEdited()!=null){
//						VectorialLayerEdited vle=(VectorialLayerEdited)em.getActiveLayerEdited();
//						try {
//							vle.clearSelection(false);
//						} catch (ReadException e1) {
//							NotificationManager.addError(e1);
//						}
//					}
//				}else{
//					fLayer.setDirty(true);
//					view.getMapControl().rePaintDirtyLayers();
//				}
//
//				/* FLayers layers=view.getMapControl().getMapContext().getLayers();
//				for (int j=0;j<layers.getLayersCount();j++){
//					if (layers.getLayer(j).equals(fLayer)){
//						view.repaintMap();
//					}
//				} */
//			}
//		}
//
//	}
	public void update(Observable observable, Object notification) {
		DefaultFeatureStoreNotification dfsn=(DefaultFeatureStoreNotification)notification;
		String type=dfsn.getType();
		IWindow[] views = PluginServices.getMDIManager().getAllWindows();

		for (int i=0 ; i<views.length ; i++){
			if (views[i] instanceof com.iver.cit.gvsig.project.documents.view.gui.View){
				com.iver.cit.gvsig.project.documents.view.gui.View view=(com.iver.cit.gvsig.project.documents.view.gui.View)views[i];
				if (type.equals(DefaultFeatureStoreNotification.AFTER_DELETE)){
					EditionManager em=CADExtension.getEditionManager();
					if (em.getActiveLayerEdited()!=null){
						VectorialLayerEdited vle=(VectorialLayerEdited)em.getActiveLayerEdited();
						try {
							vle.clearSelection();
						} catch (DataException e1) {
							NotificationManager.addError(e1);
						}
					}
				}
				if (type.equals(DefaultFeatureStoreNotification.AFTER_INSERT)){
					view.getMapControl().repaint();
				}
				if (type.equals(DefaultFeatureStoreNotification.AFTER_UPDATE)){
					view.getMapControl().rePaintDirtyLayers();
				}
			}
		}
	}

}
