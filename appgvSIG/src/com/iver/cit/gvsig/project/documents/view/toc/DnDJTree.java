/*
 * Created on 02-jun-2004
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
package com.iver.cit.gvsig.project.documents.view.toc;
/*
**  This is version II of DnDJTree. The first version allowed for what I
**  thought was a JDK oversight. However, we can set the cursor appropriately,
**  relative to whether the current cursor location is a valid drop target.
**
**  If this is your first time reading the source code. Just ignore the above
**  comment and ignore the "CHANGED" comments below. Otherwise, the
**  "CHANGED" comments will show where the code has changed.
**
**  Credit for finding this shortcoming in my code goes Laurent Hubert.
**  Thanks Laurent.
**
**  Rob. [ rkenworthy@hotmail.com ]
*/


import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class DnDJTree extends JTree
                  implements TreeSelectionListener,
                  DragGestureListener, DropTargetListener,
                  DragSourceListener {

  protected ArrayList m_Listeners = new ArrayList();

  private static DnDJTree oDnDtocOrigin = null;
  private static DnDJTree oDnDtocDestination = null;
  /** Stores the parent Frame of the component */
  // private Frame Parent = null;

  /** Stores the selected node info */
  protected TreePath SelectedTreePath = null;
  protected DefaultMutableTreeNode SelectedNode = null;

  /** Variables needed for DnD */
  private DragSource dragSource = null;
  //private DragSourceContext dragSourceContext = null;
  private DropTarget dropTarget;
  //private ArrayList treeListeners=new ArrayList();
  //private ArrayList dropListeners=new ArrayList();
 // private TreeModel model1;
  /** Constructor
  @param root The root node of the tree
  @param parent Parent JFrame of the JTree */
  public DnDJTree(TreeModel treeModel) {
    super(treeModel);
    // Parent = parent;

    //addTreeSelectionListener(this);

		/* ********************** CHANGED ********************** */
    dragSource = DragSource.getDefaultDragSource() ;
		/* ****************** END OF CHANGE ******************** */

    DragGestureRecognizer dgr =
      dragSource.createDefaultDragGestureRecognizer(
        this,                             //DragSource
        DnDConstants.ACTION_COPY_OR_MOVE, //specifies valid actions
        this                              //DragGestureListener
      );


    /* Eliminates right mouse clicks as valid actions - useful especially
     * if you implement a JPopupMenu for the JTree
     */
    dgr.setSourceActions(dgr.getSourceActions() & ~InputEvent.BUTTON3_MASK);

    /* First argument:  Component to associate the target with
     * Second argument: DropTargetListener
    */
    //DropTarget dropTarget = new DropTarget(this, this);
    setDropTarget();
  }
  public void invalidateListeners(){
	  removeDropListener();
	  removeTreeListener();
  }
  private void addDropListener(){
	  dropTarget= new DropTarget(this, this);
  }
  private void removeDropListener(){
	  dropTarget=null;
  }
  private void addTreeListener(){
	  addTreeSelectionListener(this);
  }
  private void removeTreeListener(){
	  removeTreeSelectionListener(this);
  }
  public void setDropTarget(){
      // TODO: COMENTADO POR FJP
	  /* com.iver.andami.ui.mdiManager.View[] views=PluginServices.getMDIManager().getAllViews();
	  for(int i=0;i<views.length;i++){
		  if (views[i] instanceof View){
			 // model1=((View)views[i]).getTOC().getTree().getModel();
			  ((View)views[i]).getTOC().getTree().removeTreeListener();
			  ((View)views[i]).getTOC().getTree().addTreeListener();
			  ((View)views[i]).getTOC().getTree().removeDropListener();
			  ((View)views[i]).getTOC().getTree().addDropListener();
		  } */
		  addTreeListener();
		  addDropListener();
	  // }
	 ////////// new DropTarget(this, this);
  }
  /** Returns The selected node */
  public DefaultMutableTreeNode getSelectedNode() {
    return SelectedNode;
  }

  ///////////////////////// Interface stuff ////////////////////


  /** DragGestureListener interface method */
  public void dragGestureRecognized(DragGestureEvent e) {
	  if (((MouseEvent)((MouseDragGestureRecognizer)e.getSource()).getTriggerEvent()).getButton()==MouseEvent.BUTTON3){
		  return;
	  }
    //Get the selected node
    DefaultMutableTreeNode dragNode = getSelectedNode();
    if (dragNode != null) {


      if (!(dragNode.getUserObject() instanceof Transferable)) {
		return;
	}

//    Get the Transferable Object
      Transferable transferable = (Transferable) dragNode.getUserObject();

			/* ********************** CHANGED ********************** */

      //Select the appropriate cursor;
      // Cursor cursor = DragSource.DefaultCopyNoDrop;
      int action = e.getDragAction();
      /* if (action == DnDConstants.ACTION_MOVE)
        cursor = DragSource.DefaultMoveDrop; */


      //In fact the cursor is set to NoDrop because once an action is rejected
      // by a dropTarget, the dragSourceListener are no more invoked.
      // Setting the cursor to no drop by default is so more logical, because
      // when the drop is accepted by a component, then the cursor is changed by the
      // dropActionChanged of the default DragSource.
			/* ****************** END OF CHANGE ******************** */

      //begin the drag
      oDnDtocOrigin = this;
      dragSource.startDrag(e, null, transferable, this);
    }
  }

  /** DragSourceListener interface method */
  public void dragDropEnd(DragSourceDropEvent dsde) {
  }

  /** DragSourceListener interface method */
  public void dragEnter(DragSourceDragEvent dsde) {
		/* ********************** CHANGED ********************** */
      // System.err.println("dragOver" + dsde.getDragSourceContext().getComponent());

		/* ****************** END OF CHANGE ******************** */
  }

  /** DragSourceListener interface method */
  public void dragOver(DragSourceDragEvent dsde) {
		/* ********************** CHANGED ********************** */
      // System.err.println("dragOver" + dsde.getDragSourceContext().getComponent());
		/* ****************** END OF CHANGE ******************** */
  }

  /** DragSourceListener interface method */
  public void dropActionChanged(DragSourceDragEvent dsde) {
  }

  /** DragSourceListener interface method */
  public void dragExit(DragSourceEvent dsde) {
  }

  /** DropTargetListener interface method - What we do when drag is released */
  public void drop(DropTargetDropEvent e) {
    try {
      Transferable tr = e.getTransferable();
      //flavor not supported, reject drop
      if (!tr.isDataFlavorSupported( TocItemBranch.INFO_FLAVOR)) {
		e.rejectDrop();
	}
      //cast into appropriate data type
      TocItemBranch childInfo =
        (TocItemBranch) tr.getTransferData( TocItemBranch.INFO_FLAVOR );
      //get new parent node
      Point loc = e.getLocation();
      TreePath destinationPath = getPathForLocation(loc.x, loc.y);

      final String msg = testDropTarget(destinationPath, SelectedTreePath);
      if (msg != null) {
          /* if (testSameComponent())
          { */
              e.rejectDrop();
              SwingUtilities.invokeLater(new Runnable() {
                  public void run() {
                      System.err.println(msg);
                  }
              });
              return;
          /* }
          else
          {
              // TODO: Por ahora solo dejamos mover, no copiar
              // TODO: ¿Y qué pasa si la vista a la que vamos
              // no tiene la misma proyección? MEJOR DESHABILITO ESTO POR AHORA
              if (e.getDropAction() == DnDConstants.ACTION_MOVE)
                  dropRoot(SelectedNode);
              else
                  e.rejectDrop();
              return;
          } */
      }
      if (!testSameComponent(e))
      {
          e.rejectDrop();
          return;
      }
      int oldPos,newPos;
	  //boolean isContainer=false;

	  DefaultMutableTreeNode nodoTocado =
        (DefaultMutableTreeNode) destinationPath.getLastPathComponent();
	  //	get old parent node
      DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode) getSelectedNode().getParent();
      if (nodoTocado.getParent().equals(getSelectedNode())){
    	  return;
      }
      //oldParent.setUserObject(new TocItemBranch(((TocItemBranch)getSelectedNode().getUserObject()).getLayer().getParentLayer()));
	  if (oldParent==null) {
		return;
	}
      oldPos = oldParent.getIndex(getSelectedNode());
	  // Para no tener en cuenta los nodos de símbolos:
      if (!(nodoTocado.getUserObject() instanceof TocItemBranch)){
      		nodoTocado = (DefaultMutableTreeNode) nodoTocado.getParent();
      }

      ///posActual = oldParent.getIndex(getSelectedNode());
      //Destino
	  DefaultMutableTreeNode destParent=null;

	  if (((TocItemBranch)nodoTocado.getUserObject()).getLayer() instanceof FLayers){
		  //isContainer=true;
		  newPos=0;
		  destParent=nodoTocado;
      }else{//Si donde se deja la capa seleccionada no es un contenedor de capas.
		destParent= (DefaultMutableTreeNode)nodoTocado.getParent();
        newPos=destParent.getIndex(nodoTocado);
      }




      int action = e.getDropAction();
      boolean copyAction = (action == DnDConstants.ACTION_COPY);

      //make new child node
      DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(childInfo);
	  if (getSelectedNode().getAllowsChildren()){
		  int childs=getSelectedNode().getChildCount();

		  for (int i=0;i<childs;i++){
			  newChild.add((MutableTreeNode)getSelectedNode().getChildAt(0));
		  }
	  }

	  try {
        if (!copyAction){
        	oldParent.remove(getSelectedNode());
            destParent.insert(newChild,newPos);
            // newParent.add(newChild);
        }
        if (copyAction) {
			e.acceptDrop (DnDConstants.ACTION_COPY);
		} else {
			e.acceptDrop (DnDConstants.ACTION_MOVE);
		}
      }
      catch (java.lang.IllegalStateException ils) {
        e.rejectDrop();
      }

      e.getDropTargetContext().dropComplete(true);

      //expand nodes appropriately - this probably isnt the best way...

      // TODO: COMENTADO POR FJP
      /* com.iver.andami.ui.mdiManager.View[] views=PluginServices.getMDIManager().getAllViews();
	  for(int i=0;i<views.length;i++){
		  if (views[i] instanceof View){
			  ((DefaultTreeModel)((View)views[i]).getTOC().getTree().getModel()).reload(oldParent);
		  }
	  } */



     // ((DefaultTreeModel)model1).reload(oldParent);
      DefaultTreeModel model = (DefaultTreeModel) getModel();

	  model.reload(getSelectedNode().getRoot());
	  FLayers lpo=null;
	  FLayers lpd=null;

	  if (oldParent.getUserObject() instanceof TocItemBranch){
	  lpo=(FLayers)((TocItemBranch)oldParent.getUserObject()).getLayer();
	  //lpd=(FLayers)((TocItemBranch)destParent.getUserObject()).getLayer();
	  }/*else if (((TocItemBranch)nodoTocado.getUserObject()).getLayer().getParentLayer()!=null){
		  lpo=((TocItemBranch)nodoTocado.getUserObject()).getLayer().getParentLayer();
	  }*/else{
		  lpo=((TocItemBranch)getSelectedNode().getUserObject()).getLayer().getParentLayer();
	  }
		  if (destParent.getUserObject() instanceof TocItemBranch){
		  lpd=(FLayers)((TocItemBranch)destParent.getUserObject()).getLayer();
		  }else{
		  lpd=((TocItemBranch)nodoTocado.getUserObject()).getLayer().getParentLayer();
		  }


	  if (destParent.equals(oldParent)){
		  callListeners(oldPos,newPos,lpd);
	  }else{
		  callListeners(lpo,lpd,((TocItemBranch)newChild.getUserObject()).getLayer());
	  }
	  }

    catch (IOException io) { e.rejectDrop(); }
    catch (UnsupportedFlavorException ufe) {e.rejectDrop();}
  } //end of method
public void dropRoot(TreeNode tn){
	int oldPos,newPos;
	DefaultMutableTreeNode nodoTocado =
	        (DefaultMutableTreeNode) tn;
		  //	get old parent node
	    if (getSelectedNode()==null) {
			return;
		}
		DefaultMutableTreeNode oldParent = (DefaultMutableTreeNode) getSelectedNode().getParent();
		  if (oldParent!=null){
	      oldPos = oldParent.getIndex(getSelectedNode());
	      //Destino
		  DefaultMutableTreeNode destParent=null;
		  newPos=0;
		  destParent=nodoTocado;

	      //make new child node
	      DefaultMutableTreeNode newChild = (DefaultMutableTreeNode)getSelectedNode().clone();
	      oldParent.remove(getSelectedNode());
	      destParent.insert(newChild,newPos);

	      com.iver.andami.ui.mdiManager.IWindow[] views=PluginServices.getMDIManager().getAllWindows();
		  for(int i=0;i<views.length;i++){
			  if (views[i] instanceof View){
				  ((DefaultTreeModel)((View)views[i]).getTOC().getTree().getModel()).reload(oldParent);
			  }
		  }
	     // ((DefaultTreeModel)model1).reload(oldParent);
	      DefaultTreeModel model = (DefaultTreeModel) getModel();
		  model.reload(destParent);
		  FLayers lpo=null;
		  FLayers lpd=null;

		  lpo=((TocItemBranch)getSelectedNode().getUserObject()).getLayer().getParentLayer();
		  for(int i=0;i<views.length;i++){
			  if (views[i] instanceof View){
				  if (((View)views[i]).getTOC().getTree().equals(this)){
					  lpd= ((View)views[i]).getMapControl().getMapContext().getLayers();
				  }
			  }
		  }
		  if (destParent.equals(oldParent)){
			  callListeners(oldPos,newPos,lpd);
		  }else{
			  callListeners(lpo,lpd,((TocItemBranch)newChild.getUserObject()).getLayer());
		  }
		  }
	}

  /** DropTaregetListener interface method */
  public void dragEnter(DropTargetDragEvent e) {
  }

  /** DropTaregetListener interface method */
  public void dragExit(DropTargetEvent e) {
  }

  /** DropTaregetListener interface method */
  public void dragOver(DropTargetDragEvent e) {
		/* ********************** CHANGED ********************** */
    //set cursor location. Needed in setCursor method
    Point cursorLocationBis = e.getLocation();
        TreePath destinationPath =
      getPathForLocation(cursorLocationBis.x, cursorLocationBis.y);


    // if destination path is okay accept drop...

    if (testSameComponent(e))
    {
        String msg = testDropTarget(destinationPath, SelectedTreePath);
        if ( msg == null) {
            e.acceptDrag(DnDConstants.ACTION_MOVE) ;
            return;
        }

    }
    // ...otherwise reject drop
    // else {
        // System.err.println(e.getDropTargetContext().getComponent());

        // if (testSameComponent(e))
            e.rejectDrag() ;
        /* else
            e.acceptDrag(DnDConstants.ACTION_MOVE); */
    // }
		/* ****************** END OF CHANGE ******************** */
  }

  /** DropTaregetListener interface method */
  public void dropActionChanged(DropTargetDragEvent e) {
  }
  private void setSelectedNode(DefaultMutableTreeNode smtn){
	  if (smtn!=null) {
		SelectedNode=smtn;
	}
  }

  /** TreeSelectionListener - sets selected node */
  public void valueChanged(TreeSelectionEvent evt) {
    SelectedTreePath = evt.getNewLeadSelectionPath();
    /* com.iver.andami.ui.mdiManager.View[] views=PluginServices.getMDIManager().getAllViews();
	  for(int i=0;i<views.length;i++){
		  if (views[i] instanceof View){
			  if (SelectedTreePath == null) {
				((View)views[i]).getTOC().getTree().setSelectedNode(null);
			  }else{
			   	((View)views[i]).getTOC().getTree().setSelectedNode((DefaultMutableTreeNode)SelectedTreePath.getLastPathComponent());
			  }
		  }
	  } */
	  if (SelectedTreePath == null){
		  setSelectedNode(null);
	  }else{
		  setSelectedNode((DefaultMutableTreeNode)SelectedTreePath.getLastPathComponent());
	  }
  }

  /** Convenience method to test whether drop location is valid
  @param destination The destination path
  @param dropper The path for the node to be dropped
  @return null if no problems, otherwise an explanation
  */
  private String testDropTarget(TreePath destination, TreePath dropper) {
    //Typical Tests for dropping

    //Test 1.
    boolean destinationPathIsNull = destination == null;
    if (destinationPathIsNull) {
		return "Invalid drop location.";
	}

    //Test 2.
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) destination.getLastPathComponent();
    if ( !node.getAllowsChildren() ) {
		return "This node does not allow children";
	}

    if (destination.equals(dropper)) {
		return "Destination cannot be same as source";
	}

    //Test 3.
    /* if ( dropper.isDescendant(destination))
       return "Destination node cannot be a descendant.";

    //Test 4.
    if ( dropper.getParentPath().equals(destination))
       return "Destination node cannot be a parent."; */

    return null;
  }

  private boolean testSameComponent(DropTargetEvent e)
  {
      oDnDtocDestination = this;
      return (oDnDtocOrigin == oDnDtocDestination);
  }
	/**
	 * @param arg0
	 * @return
	 */
	public boolean addOrderListener(ITocOrderListener arg0) {
		return m_Listeners.add(arg0);
	}
	/**
	 * @param arg0
	 * @return
	 */
	public boolean removeOrderListener(ITocOrderListener arg0) {
		return m_Listeners.remove(arg0);
	}
	private void callListeners(int oldPos, int newPos,FLayers lpd)
	{
		for (int i=0; i < m_Listeners.size(); i++)
		{
			ITocOrderListener listener = (ITocOrderListener) m_Listeners.get(i);
			listener.orderChanged(oldPos, newPos,lpd);
		}
	}

  private void callListeners(FLayers lpo,FLayers lpd,FLayer ls){
	  for (int i=0; i < m_Listeners.size(); i++)
		{
			ITocOrderListener listener = (ITocOrderListener) m_Listeners.get(i);
			listener.parentChanged(lpo,lpd,ls);
		}
  }

} //end of DnDJTree
