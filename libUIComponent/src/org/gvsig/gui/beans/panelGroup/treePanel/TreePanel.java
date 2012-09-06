/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
 */

package org.gvsig.gui.beans.panelGroup.treePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.gvsig.gui.beans.panelGroup.AbstractPanelGroup;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;


/**
 * <p>Graphical interface that's a {@link JPanel JPanel} with an inner {@link JSplitPane JSplitPane} that
 *  contains the {@link IPanel IPanel}'s grouped in a {@link JTree JTree}.</p>
 * <p>When user selects one of them, at the tree, that's displayed at the right pane of the split pane, and
 *  user can work with it.</p>
 *
 * @see AbstractPanelGroup
 *
 * @version 15/10/2007
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 */
public class TreePanel extends AbstractPanelGroup implements Serializable {
	private static final long serialVersionUID = 2350636078676872309L;

	/**
	 * <p>Graphical interface where the tree and the selected panel will be drawn.</p>
	 *
	 * @see #getJSplitPane()
	 */
	private JSplitPane jSplitPane = null;

	/**
	 * <p>Graphical interface that represents a tree where panels will be listed and grouped together.</p>
	 *
	 * @see #getJTree()
	 */
	private JTree jTree = null;

	/**
	 * <p>Graphical interface where is allocated the tree.</p>
	 *
	 * @see #getJSplitPane()
	 */
	private JScrollPane treeJScrollPane = null;

	/**
	 * <p>Reference to the root node of the tree.</p>
	 *
	 * #see #getRootNode()
	 */
	private DefaultMutableTreeNode rootNode = null;

	/**
	 * <p>Reference to the tree model.</p>
	 *
	 * @see #getTreeModel()
	 */
	private DefaultTreeModel treeModel = null;

	/**
	 * <p>A panel used in the initialization of the split pane.</p>
	 *
	 * @see #getDefaultPanel()
	 */
	private AbstractPanel defaultPanel = null;

	/**
	 * <p>Name of the tree root node.</p>
	 */
	private final String rootNodeName = "";

	/**
	 * <p>Default (horizontal) divider width.</p>
	 */
	private final short DEFAULT_DIVIDER_WIDTH = 5;

	/**
	 * <p>Reference to the last node inserted or removed.</p>
	 */
	private DefaultMutableTreeNode lastNode = null;

	/**
	 * <p>Attribute used to hold the position of the divider.</p>
	 */
	private int currentDividerLocation;

	/**
	 * <p>Number of the registered panels of this group that are in the GUI.</p>
	 */
	private int panelsInGUICount;

	static {
		// Registers this class as a "PanelGroup" type
		PanelGroupManager.getManager().registerPanelGroup(TabbedPanel.class);
	}

	/**
	 * <p>Default constructor.</p>
	 *
	 * @param reference object that is ''semantically' or 'contextually' related to the group of panels
	 */
	public TreePanel(Object reference) {
		super(reference);

		initialize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#initialize()
	 */
	protected void initialize() {
		super.initialize();

		currentDividerLocation = -1;

		panelsInGUICount = 0;
		this.setLayout(new BorderLayout());
		this.add(getJSplitPane(), BorderLayout.CENTER);
	}

	/**
	 * <p>This method initializes <code>jSplitPane</code>.</p>
	 *
	 * @return JSplitPane
	 */
	protected JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			jSplitPane.setDividerSize(DEFAULT_DIVIDER_WIDTH);
			jSplitPane.setLeftComponent(getTreeJScrollPane());
			jSplitPane.setRightComponent(getDefaultPanel());
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setDividerLocation(180);
		}

		return jSplitPane;
	}

	/**
	 * <p>This method initializes <code>defaultPanel</code>.</p>
	 *
	 * @return AbstractPanel inner panel that is stored as a right panel of the <code>JSplitPane</code> object
	 *  when this object is created
	 */
	protected AbstractPanel getDefaultPanel() {
		if (defaultPanel == null) {
			defaultPanel = new DefaultPanel();
		}

		return defaultPanel;
	}

	/**
	 * <p>This method initializes <code>jTree</code>.</p>
	 *
	 * @return JTree
	 */
	protected JTree getJTree() {
		if (jTree == null) {
			jTree = new JTree(getTreeModel());

			// Single Selection:
			jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

			// Remove icons:
			DefaultTreeCellRenderer defaultTreeCellRenderer = new DefaultTreeCellRenderer();
			defaultTreeCellRenderer.setOpenIcon(null);
			defaultTreeCellRenderer.setClosedIcon(null);
			defaultTreeCellRenderer.setLeafIcon(null);

			// Add new icons for the node-branches that can be expanded or collapsed
			ImageIcon treeCellRendererIcon = new ImageIcon(TreePanel.class.getResource("images/treenodecollapsibleicon.png"), null);
			defaultTreeCellRenderer.setOpenIcon(treeCellRendererIcon);
			treeCellRendererIcon = new ImageIcon(TreePanel.class.getResource("images/treenodeexpandableicon.png"), null);
			defaultTreeCellRenderer.setClosedIcon(treeCellRendererIcon);

			// Root not visible
			jTree.setRootVisible(false);
			jTree.setCellRenderer(defaultTreeCellRenderer);

			// Expand only the root node
			// Adds support for notification that a node has been adder or removed using a ''ContainerEvent''
			//  which is used in other AbstractPanelGruop implementations:
			jTree.getModel().addTreeModelListener(new TreeModelListener() {
				/*
				 * (non-Javadoc)
				 * @see org.apache.log4j.lf5.viewer.categoryexplorer.TreeModelAdapter#treeNodesInserted(javax.swing.event.TreeModelEvent)
				 */
				public void treeNodesInserted(TreeModelEvent e) {
					// Expand only the root node
					TreePath rootNodePath = new TreePath(getRootNode());

					if (jTree.isCollapsed(rootNodePath))
						jTree.expandPath(rootNodePath);

					// Only notify as ContainerEvent the insertion of a IPanel
					if (((DefaultMutableTreeNode)lastNode).getUserObject() instanceof IPanel)
						dispatchEvent(new ContainerEvent(jTree, ContainerEvent.COMPONENT_ADDED, (Component)((DefaultMutableTreeNode)lastNode).getUserObject()));

					lastNode = null;
				}

				/*
				 * (non-Javadoc)
				 * @see org.apache.log4j.lf5.viewer.categoryexplorer.TreeModelAdapter#treeNodesRemoved(javax.swing.event.TreeModelEvent)
				 */
				public void treeNodesRemoved(TreeModelEvent e) {
					// Expand only the root node
					TreePath rootNodePath = new TreePath(getRootNode());

					if (jTree.isCollapsed(rootNodePath))
						jTree.expandPath(rootNodePath);

					// Only notify as ContainerEvent the insertion of a IPanel
					if (((DefaultMutableTreeNode)lastNode).getUserObject() instanceof IPanel)
						dispatchEvent(new ContainerEvent(jTree, ContainerEvent.COMPONENT_REMOVED, (Component)((DefaultMutableTreeNode)lastNode).getUserObject()));

					lastNode = null;
				}

				public void treeNodesChanged(TreeModelEvent e) {
					// TODO Auto-generated method stub

				}

				public void treeStructureChanged(TreeModelEvent e) {
					// TODO Auto-generated method stub

				}
			});

			// Set to the right panel of the JSplitPane, the AbstractPanel selected
			// Adds support for notification that a node has been selected -> set the panel associated to
			//  the right panel of the jSplitPane
			jTree.addTreeSelectionListener(new TreeSelectionListener() {
				/*
				 * (non-Javadoc)
				 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
				 */
			    public void valueChanged(TreeSelectionEvent e) {
			        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();

			        /* If nothing is selected */
			        if (node == null)
			        	return;

			        /* Retrieve the node that was selected and set it to the right panel of the JSPlitPane */
			        Object object = node.getUserObject();

		        	if ((object != null) && (object instanceof AbstractPanel)) {
		        		getJSplitPane().setRightComponent((Component) object);

		        		// Notifies the new panel selected
		        		stateChanged(new ChangeEvent(object));

		    			getJSplitPane().setDividerLocation(currentDividerLocation);
			        }
			    }
			});
		}

		return jTree;
	}

	/**
	 * <p>This method initializes <code>treeModel</code>.</p>
	 *
	 * @return DefaultTreeModel
	 */
	protected DefaultTreeModel getTreeModel() {
		if (treeModel == null) {
			treeModel = new DefaultTreeModel(getRootNode());
		}

		return treeModel;
	}

	/**
	 * <p>This method initializes <code>rootNode</code>.</p>
	 *
	 * @return DefaultMutableTreeNode
	 */
	protected DefaultMutableTreeNode getRootNode() {
		if (rootNode == null)
			rootNode = new DefaultMutableTreeNode(rootNodeName);

		return rootNode;
	}

	/**
	 * <p>This method initialiazes <code>jScrollPane</code>.</p>
	 *
	 * @return JScrollPane
	 */
	protected JScrollPane getTreeJScrollPane() {
		if (treeJScrollPane == null) {
			treeJScrollPane = new JScrollPane();
			treeJScrollPane.setViewportView(getJTree());
		}

		return treeJScrollPane;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#loadPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	protected void loadPanel(IPanel panel) {
		super.loadPanel(panel);

		AbstractPanel panel_obj = (AbstractPanel)panel;

		if (panel_obj.isVisible()) {
			addPanelImpl(panel);

			// Recalculates preferred sizes (of the right component of the JSPlitPane), and then resizes also this one if it's necessary
			Dimension panelPreferredSize = panel_obj.getPreferredSize();
			Dimension rightPanelPreferredSize = getJSplitPane().getRightComponent().getPreferredSize();

			if ((panelPreferredSize.width > rightPanelPreferredSize.width) || (panelPreferredSize.height > rightPanelPreferredSize.height)) {
				getJSplitPane().getRightComponent().setPreferredSize(new Dimension(Math.max(panelPreferredSize.width, rightPanelPreferredSize.width), Math.max(panelPreferredSize.height, rightPanelPreferredSize.height)));
			}
		}
	}

	/**
	 * <p>Has the algorithms that allows adding a panel to the JTree component.</p>
	 *
	 * @param panel the panel to add
	 *
	 * @see #addPanel(IPanel)
	 */
	protected void addPanelImpl(IPanel panel) {
		if (belongsThisGroup(panel)) {

			// Don't add the panel if hasn't defined the 'label' attribute
			if (panel.getLabel() == null)
				return;

			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(panel);
			lastNode = new_node;

			// Case 1 -> the attribute 'labelGroup' is undefined
			if (panel.getLabelGroup() == null) {
				getTreeModel().insertNodeInto(new_node, getRootNode(), getRootNode().getChildCount());
				panelsInGUICount ++;
				return;
			}

			// Case 2 -> Check if already exists a node with the same 'labelGroup' attribute than the panel
			DefaultMutableTreeNode node;

			for (short i = 0; i < getRootNode().getChildCount(); i ++) {
				node = (DefaultMutableTreeNode) getRootNode().getChildAt(i);

				if (node.getUserObject().toString().equals(panel.getLabelGroup())) {
					getTreeModel().insertNodeInto(new_node, node, node.getChildCount());
					panelsInGUICount ++;
					return;
				}
			}

			// Case 3 -> Normal insertion
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(panel.getLabelGroup());
			lastNode = parent;
			getTreeModel().insertNodeInto(parent, getRootNode(), getRootNode().getChildCount());

			lastNode = new_node;
			getTreeModel().insertNodeInto(new_node, parent, 0);
			panelsInGUICount ++;
		}
	}

	/**
	 * <p>Has the algorithms that allows adding a panel to the JTree component, considering the position
	 *  of that panel in the group, and the group position in the JTree.</p>
	 *
	 * @param panel the panel to add
	 *
	 * @see #addPanel(IPanel)
	 */
	protected void addPanelSortOrdered(IPanel panel) {
		if (belongsThisGroup(panel)) {

			// Don't add the panel if hasn't defined the 'label' attribute
			if (panel.getLabel() == null)
				return;

			short p_index = (short)registeredPanels.indexOf(panel);

			DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(panel);
			lastNode = new_node;

//			String label;
			short l_index = 0;
			short i;

			// Case 1 -> the attribute 'labelGroup' is undefined (null)
			if (panel.getLabelGroup() == null) {
				Enumeration labelsAndGroups = rootNode.children();

				for (i = 0; i < p_index; i++) {
					if (registeredPanels.get(i).getLabelGroup() == null) {
						if (labelsAndGroups.hasMoreElements()) {
							if (((DefaultMutableTreeNode)labelsAndGroups.nextElement()).getUserObject().equals(registeredPanels.get(i)))
								l_index ++;
						}
						else {
							break;
						}
					}
					else {
						if (labelsAndGroups.hasMoreElements()) {
							if (((DefaultMutableTreeNode)labelsAndGroups.nextElement()).getUserObject().equals(registeredPanels.get(i).getLabelGroup()))
								l_index ++;
						}
						else {
							break;
						}
					}
				}

				// Insert the node
				getTreeModel().insertNodeInto(new_node, getRootNode(), l_index);
				panelsInGUICount ++;
				return;
			}

			// Case 2 -> Check if already exists a node with the same 'labelGroup' attribute than the panel
			DefaultMutableTreeNode node = null;
			Enumeration groups = rootNode.children();
			Enumeration labels;

			// Find the node with the label group
			while (groups.hasMoreElements()) {
				node = (DefaultMutableTreeNode) groups.nextElement();

				if ((node.getUserObject()).equals(panel.getLabelGroup())) {
					break;
				}
				else {
					node = null;
				}
			}

			if (node != null) {
				labels = node.children();
				l_index = 0;

				// Avoid adding two times the same node
				while (labels.hasMoreElements()) {
					if (((DefaultMutableTreeNode)labels.nextElement()).getUserObject().equals(panel))
						return;
				}

				labels = node.children();
				Object leaf_panel = ((DefaultMutableTreeNode) labels.nextElement()).getUserObject();

				for (i = 0; i < p_index; i++) {
					if (registeredPanels.get(i).getLabelGroup() == panel.getLabelGroup()) {
						if (registeredPanels.get(i).equals(leaf_panel)) {
							l_index ++;

							if (! labels.hasMoreElements())
								break;

							leaf_panel = ((DefaultMutableTreeNode) labels.nextElement()).getUserObject();
						}
					}
				}

				// Insert the node
				getTreeModel().insertNodeInto(new_node, node, l_index);
				panelsInGUICount ++;
				return;
			}

			// Case 3 -> Normal insertion

			// Check the position of the 'labelGroup' node:
			short g_index = 0;

			groups = rootNode.children();
			String c_labelGroup = (String)((DefaultMutableTreeNode)groups.nextElement()).getUserObject();
			String labelGroup;

			for (i = 0; i < p_index; i++) {
				labelGroup = registeredPanels.get(i).getLabelGroup();

				if (c_labelGroup == null) {
					if (labelGroup == null) {
						c_labelGroup = (String)((DefaultMutableTreeNode)groups.nextElement()).getUserObject();
						g_index ++;
					}
				}
				else {
					if (c_labelGroup.equals(labelGroup))
					{
						DefaultMutableTreeNode dmtnode = ((DefaultMutableTreeNode)groups.nextElement());
						c_labelGroup = (String)dmtnode.getUserObject().toString();
						g_index ++;
					}
				}
			}

			// Insert the node
			DefaultMutableTreeNode parent = new DefaultMutableTreeNode(panel.getLabelGroup());
			lastNode = parent;
			getTreeModel().insertNodeInto(parent, getRootNode(), g_index);

			lastNode = new_node;
			getTreeModel().insertNodeInto(new_node, parent, 0);
			panelsInGUICount ++;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#unLoadPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	protected void unLoadPanel(IPanel panel) {
		super.unLoadPanel(panel);

		removePanelImpl(panel);
	}

	/**
	 * <p>Has the algorithms that allows removing a panel from the JTree component.</p>
	 *
	 * @param panel the panel to remove
	 *
	 * @see #removePanel(IPanel)
	 */
	protected void removePanelImpl(IPanel panel) {
		if (belongsThisGroup(panel)) {

			// Don't add the panel if hasn't defined the 'label' attribute
			if (panel.getLabel() == null)
				return;

			DefaultMutableTreeNode node;

			// Case 1 -> the attribute 'labelGroup' is undefined
			if (panel.getLabelGroup() == null) {
				for (short i = 0; i < getRootNode().getChildCount(); i ++) {
					node = (DefaultMutableTreeNode) getRootNode().getChildAt(i);

					if ((node.getUserObject().toString().equals(panel.getLabel())) && (node.getChildCount() == 0)) {
						lastNode = node;
						getTreeModel().removeNodeFromParent(node);
						panelsInGUICount --;
						return;
					}
				}

				return;
			}

			DefaultMutableTreeNode parent;

			// Case 2 -> Check if already exists a node with the same 'labelGroup' and 'label' attributes than the panel
			for (short i = 0; i < getRootNode().getChildCount(); i ++) {
				parent = (DefaultMutableTreeNode) getRootNode().getChildAt(i);

				if ((parent.getChildCount() > 0) && (parent.getUserObject().toString().equals(panel.getLabelGroup()))) {
					for (short j = 0; j < parent.getChildCount(); j ++) {
						node = (DefaultMutableTreeNode)parent.getChildAt(j);

						if (node.getUserObject().toString().equals(panel.getLabel())) {
							if (parent.getChildCount() == 1) { // Case 3 -> If there is only one panel in that 'labelGroup'
								lastNode = parent;
								getTreeModel().removeNodeFromParent(parent);
								panelsInGUICount --;
							}
							else {
								lastNode = node;
								getTreeModel().removeNodeFromParent(node);
								panelsInGUICount --;
							}
						}
					}
					return;
				}
			}
		}
	}

	/**
	 * @see JTree#getSelectionPath()
	 */
	public TreePath getSelectionPath() {
		return getJTree().getSelectionPath();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getActivePanel()
	 */
	public IPanel getActivePanel() {
		if (registeredPanels.size() == 0)
			return null;

		Object node = ((DefaultMutableTreeNode)getJTree().getLastSelectedPathComponent()).getUserObject();

		if (node instanceof IPanel)
			return (IPanel)node;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		if (registeredPanels.size() == 0)
			return;

		Object object = e.getSource();

		if ((object != null) && (object instanceof IPanel)) {
			((IPanel) object).selected();

			// Get the current divider location to hold it after the selected panel will be set to the interface of the inner JTreePanel
			currentDividerLocation = getJSplitPane().getDividerLocation();
    	}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#updatePanelVisibility(org.gvsig.gui.beans.panelGroup.panels.IPanel, boolean)
	 */
	public synchronized void setPanelInGUI(IPanel panel, boolean b) {
		if (registeredPanels.size() == 0)
			return;

		if (registeredPanels.indexOf(panel) == -1)
			return;

		if (b == true) {
			addPanelSortOrdered((AbstractPanel)panel);
		}
		else {
			removePanelImpl((AbstractPanel)panel);
		}

		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#isPanelInGUI(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	public synchronized boolean isPanelInGUI(IPanel panel) {
		if (!registeredPanels.contains(panel))
			return false;

		DefaultMutableTreeNode node;

		for (int i = 0; i < getRootNode().getChildCount(); i++) {
			node = (DefaultMutableTreeNode) getRootNode().getChildAt(i);

			if (node.getChildCount() == 0) {
				if (node.getUserObject().equals(panel))
					return true;
			}
			else {
				if (isPanelAtBranch(panel, node))
					return true;
			}
		}

		return false;
	}

	/**
	 * <p>Returns <code>true</code> if the panel is a child of the tree node as parameter.</p>
	 */
	protected boolean isPanelAtBranch(IPanel panel, DefaultMutableTreeNode node) {
		DefaultMutableTreeNode child;

		for (int i = 0; i < node.getChildCount(); i++) {
			child = (DefaultMutableTreeNode) node.getChildAt(i);

			if (child.getUserObject().equals(panel))
				return true;
		}

		return false;
	}

    /**
     * @see JTree#addTreeSelectionListener(TreeSelectionListener)
     */
    public void addTreeSelectionListener(TreeSelectionListener tsl) {
    	getJTree().addTreeSelectionListener(tsl);
    }

    /**
     * @see JTree#removeTreeSelectionListener(TreeSelectionListener)
     */
    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
    	getJTree().removeTreeSelectionListener(tsl);
    }

    /**
     * @see JTree#getTreeSelectionListeners()
     */
    public TreeSelectionListener[] getTreeSelectionListeners() {
    	return getJTree().getTreeSelectionListeners();
    }

     /*
     * (non-Javadoc)
     * @see org.gvsig.gui.beans.panelGroup.IPanelGroup#getPanelInGUICount()
     */
	public int getPanelInGUICount() {
		return panelsInGUICount;
	}

	/**
	 * @see JSplitPane#getDividerLocation()
	 */
	public int getDividerLocation() {
		return getJSplitPane().getDividerLocation();
	}

	/**
	 * @see JSplitPane#setDividerLocation(int)
	 */
	public void setDividerLocation(int location) {
		getJSplitPane().setDividerLocation(location);
	}

	/**
	 * @see JSplitPane#getDividerSize()
	 */
	public int getDividerSize() {
		return getJSplitPane().getDividerSize();
	}

	/**
	 * @see JSplitPane#setDividerSize()
	 */
	public void setDividerSize(int newSize) {
		getJSplitPane().setDividerSize(newSize);
	}

	/**
	 * <p>A trivial implementation of {@link AbstractPanel AbstractPanel} used in the
	 *  initialization of the <code>jSplitPane</code> attribute.</p>
	 *
	 * @version 15/10/2007
	 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
	 */
	protected class DefaultPanel extends AbstractPanel {
		/**
		 * @see AbstractPanel#AbstractPanel()
		 */
		public DefaultPanel() {
			super();
		}

		/**
		 * @see AbstractPanel#AbstractPanel(String, String, String)
		 */
		public DefaultPanel(String id, String label, String labelGroup) {
			super(id, label, labelGroup);
		}

		/*
		 * (non-Javadoc)
		 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#initialize()
		 */
		protected void initialize() {
		}

		/*
		 * (non-Javadoc)
		 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
		 */
		public void accept() {
		}

		/*
		 * (non-Javadoc)
		 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#apply()
		 */
		public void apply() {
		}

		public void cancel() {
		}

		public void selected() {
		}
	}
}