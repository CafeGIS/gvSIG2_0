/*
 * Created on 02-mar-2004
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeNameTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ChangeSymbolTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.CopyLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.CutLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.EliminarCapaTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.FLyrVectEditPropertiesTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.FirstLayerTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.LayersGroupTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.LayersUngroupTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.OldTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.actions.PasteLayersTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ReloadLayerTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ShowLayerErrorsTocMenuEntry;
import com.iver.cit.gvsig.project.documents.view.toc.actions.ZoomAlTemaTocMenuEntry;


/**
 * Menu de botón derecho para el TOC.
 * Se pueden añadir entradas facilmente desde una extensión,
 * creando una clase derivando de TocMenuEntry, y añadiendola en
 * estático (o en tiempo de carga de la extensión) a FPopupMenu.
 * (Las entradas actuales están hechas de esa manera).
 *
 * @author vcn To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
 *         Comments
 */

public class FPopupMenu extends JPopupMenu {
	//private static ArrayList menuEntrys = new ArrayList();
    private DefaultMutableTreeNode nodo;
    protected MapContext mapContext;
    private ExtensionPoint extensionPoint;
    private FLayer[] selecteds;
    //private JMenuItem capa;
    // Lo de fijar la fuente es porque en linux se veía mal si no se fija.
    // TODO: Esto no funcionará para idiomas como el chino. Hay que cambiarlo.
    public final static Font theFont = new Font("SansSerif", Font.PLAIN, 10);

    public static void registerExtensionPoint() {
    	ExtensionPoint exPoint = ToolsLocator.getExtensionPointManager().add(
				"View_TocActions", "");
		exPoint
				.append("FSymbolChangeColor", "",
				new ChangeSymbolTocMenuEntry());
		exPoint.append("ChangeName", "",
				new ChangeNameTocMenuEntry());
		exPoint.append("FLyrVectEditProperties", "",
				new FLyrVectEditPropertiesTocMenuEntry());
		exPoint.append("ZoomAlTema", "",
				new ZoomAlTemaTocMenuEntry());
    	//extensionPoints.add("View_TocActions","ZoomPixelCursor",new ZoomPixelCursorTocMenuEntry());
    	exPoint.append("EliminarCapa", "",
				new EliminarCapaTocMenuEntry());
		exPoint.append("VerErroresCapa", "",
				new ShowLayerErrorsTocMenuEntry());
		exPoint.append("ReloadLayer", "",
				new ReloadLayerTocMenuEntry());
		exPoint.append("LayersGroup", "",
				new LayersGroupTocMenuEntry());
		exPoint.append("LayersUngroup", "",
				new LayersUngroupTocMenuEntry());
		exPoint.append("FirstLayer", "",
				new FirstLayerTocMenuEntry());

    	exPoint.append("Copy", "", new CopyLayersTocMenuEntry());
		exPoint.append("Cut", "", new CutLayersTocMenuEntry());
		exPoint.append("Paste", "",
				new PasteLayersTocMenuEntry());
       	//extensionPoints.add("View_TocActions","RasterProperties",new FLyrRasterAdjustPropertiesTocMenuEntry());
       	//extensionPoints.add("View_TocActions","RasterProperties",new RasterPropertiesTocMenuEntry());


    }
    static {
    	/* Cambiados
    	FPopupMenu.addEntry(new FSymbolChangeColorTocMenuEntry());
    	FPopupMenu.addEntry(new ChangeNameTocMenuEntry());
    	FPopupMenu.addEntry(new FLyrVectEditPropertiesTocMenuEntry());


    	FPopupMenu.addEntry(new ZoomAlTemaTocMenuEntry());
    	FPopupMenu.addEntry(new ZoomPixelCursorTocMenuEntry());


    	FPopupMenu.addEntry(new EliminarCapaTocMenuEntry());
    	FPopupMenu.addEntry(new ReloadLayerTocMenuEntry());

    	FPopupMenu.addEntry(new LayersGroupTocMenuEntry());
        FPopupMenu.addEntry(new LayersUngroupTocMenuEntry());


    	FPopupMenu.addEntry(new FirstLayerTocMenuEntry());
    	    	    		    	*/
    	//FPopupMenu.addEntry(new FLyrRasterAdjustPropertiesTocMenuEntry());
    }


    /**
     * Creates a new FPopupMenu object.
     *
     * @param nodo DOCUMENT ME!
     * @param vista DOCUMENT ME!
     */
    public FPopupMenu(MapContext mc, DefaultMutableTreeNode node) {
        super();
        this.initialize(mc,node);
    }

    private void initialize(MapContext mc, DefaultMutableTreeNode node) {
        this.mapContext = mc;
        this.nodo = node;

        //salir = new MenuItem("Salir");
		this.extensionPoint = ToolsLocator.getExtensionPointManager().get(
				"View_TocActions");
		this.selecteds = this.mapContext.getLayers().getActives();

		IContextMenuAction[] actions = this.getActionList();
		if (actions == null){
			return;
		}
		this.createMenuElements(actions);

		//		this.loadOldStileOptions();


    }

    public MapContext getMapContext() { return mapContext; }

	public ITocItem getNodeUserObject() {
		if (nodo == null) {
			return null;
		}
		return (ITocItem)nodo.getUserObject();
	}

	public DefaultMutableTreeNode getNode() {
		return this.nodo;
	}

	private IContextMenuAction[] getActionList() {
		ArrayList<IContextMenuAction> actionArrayList = new ArrayList<IContextMenuAction>();
		Iterator<Extension> iter = this.extensionPoint.iterator();
		AbstractTocContextMenuAction action;
		boolean contains=false;
		ITocItem tocItem=this.getNodeUserObject();
		if (tocItem instanceof TocItemBranch){
			for (int i=0;i<this.selecteds.length;i++){
				if (this.selecteds[i].equals(((TocItemBranch) tocItem)
						.getLayer())) {
					contains=true;
					break;
				}
			}
		}else{
			contains=true;
		}
		if (contains){
			while (iter.hasNext()) {
				action = null;
				try {
					action = (AbstractTocContextMenuAction) iter.next().create();
				} catch (Exception e) {
					NotificationManager.addError(e);
				}
				if (action != null && !(action instanceof OldTocContextMenuAction)) {
					action.setMapContext(this.mapContext);
					if (action.isVisible(this.getNodeUserObject(),this.selecteds)) {
						actionArrayList.add(action);
					}
				}

			}
			IContextMenuAction[] result = (IContextMenuAction[])Array.newInstance(IContextMenuAction.class,actionArrayList.size());
			System.arraycopy(actionArrayList.toArray(),0,result,0,actionArrayList.size());
			Arrays.sort(result,new CompareAction());

			return result;
		}
		return null;
	}

	public class CompareAction implements Comparator<IContextMenuAction> {

		public int compare(IContextMenuAction o1, IContextMenuAction o2) {
			//FIXME: flata formatear los enteros!!!!
			NumberFormat formater = NumberFormat.getInstance();
			formater.setMinimumIntegerDigits(3);
			String key1= ""+formater.format(o1.getGroupOrder())+o1.getGroup()+formater.format(o1.getOrder());
			String key2= ""+formater.format(o2.getGroupOrder())+o2.getGroup()+formater.format(o2.getOrder());
			return key1.compareTo(key2);
		}
	}

	private void createMenuElements(IContextMenuAction[] actions) {
		String group = null;
		for (int i=0;i < actions.length;i++) {
			IContextMenuAction action = actions[i];
			MenuItem item = new MenuItem(action.getText(),action);
			item.setFont(theFont);
			item.setEnabled(action.isEnabled(this.getNodeUserObject(),this.selecteds));
			if (!action.getGroup().equals(group)) {
				if (group != null) {
					this.addSeparator();
				}
				group = action.getGroup();
			}
			this.add(item);
		}

	}


	public class MenuItem extends JMenuItem implements ActionListener{
		/**
		 *
		 */
		private static final long serialVersionUID = 6858724610206253411L;
		private IContextMenuAction action;
		public MenuItem(String text,IContextMenuAction documentAction) {
			super(text);
			this.action = documentAction;
			String tip = this.action.getDescription();
			if (tip != null && tip.length() > 0) {
				this.setToolTipText(tip);
			}
			this.addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			this.action.execute(FPopupMenu.this.getNodeUserObject(), FPopupMenu.this.selecteds);
		}
	}

	//	private void loadOldStileOptions() {
	//		boolean first = true;
	//		Iterator iter = this.extensionPoint.keySet().iterator();
	//		AbstractTocContextMenuAction action;
	//		while (iter.hasNext()) {
	//			action = null;
	//			try {
	//				action = (AbstractTocContextMenuAction)this.extensionPoint.create((String)iter.next());
	//			} catch (InstantiationException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			} catch (IllegalAccessException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//			if (action != null && (action instanceof OldTocContextMenuAction)) {
	//				if (first) {
	//					this.addSeparator();
	//					first = false;
	//				}
	//				action.setMapContext(this.mapContext);
	//				((OldTocContextMenuAction)action).initializeElement(this);
	//			}
	//		}
	//		//comprobamos si el ultimo elemento es un seprardor
	//		if (this.getComponentCount()>0 && this.getComponent(this.getComponentCount()-1) instanceof Separator) {
	//			//Si lo es lo eliminamos
	//			this.remove(this.getComponentCount()-1);
	//		}
	//
	//
	//	}


}
