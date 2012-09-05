/*
 * Created on 13-may-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
package com.iver.cit.gvsig.project.documents.view.toc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.events.AtomicEvent;
import org.gvsig.fmap.mapcontext.events.listeners.AtomicEventListener;
import org.gvsig.fmap.mapcontext.layers.CancelationException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionEvent;
import org.gvsig.fmap.mapcontext.layers.LayerCollectionListener;
import org.gvsig.fmap.mapcontext.layers.LayerPositionEvent;
import org.gvsig.fmap.mapcontext.layers.operations.Classifiable;
import org.gvsig.fmap.mapcontext.layers.operations.IHasImageLegend;
import org.gvsig.fmap.mapcontext.layers.operations.LayerCollection;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.fmap.mapcontext.rendering.legend.IClassifiedLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.SingleSymbolLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.events.LegendChangedEvent;
import org.gvsig.fmap.mapcontext.rendering.legend.events.listeners.LegendListener;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.IContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.DnDJTree;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toc.ITocOrderListener;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemBranch;
import com.iver.cit.gvsig.project.documents.view.toc.TocItemLeaf;

/**
 * DOCUMENT ME!
 *
 * @author fjp To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TOC extends JComponent implements ITocOrderListener,
		LegendListener, LayerCollectionListener, TreeExpansionListener,
		ComponentListener {
	private MapContext mapContext;

	private DnDJTree m_Tree;

	private DefaultTreeModel m_TreeModel;

	private DefaultMutableTreeNode m_Root;

	private TOCRenderer m_TocRenderer;

	private JScrollPane m_Scroller;

	// private ArrayList m_Listeners;
	private HashMap m_ItemsExpanded = new HashMap();

	private NodeSelectionListener nodeSelectionListener = null;

	/**
	 * Crea un nuevo TOC.
	 */
	public TOC() {
		this.setName("TOC");
		this.setLayout(new BorderLayout());
		this.setMinimumSize(new Dimension(100, 80));
		this.setPreferredSize(new Dimension(100, 80));

		// Construct the tree.
		m_Root = new DefaultMutableTreeNode(java.lang.Object.class);
		m_TreeModel = new DefaultTreeModel(m_Root);
		m_Tree = new DnDJTree(m_TreeModel);

		m_TocRenderer = new TOCRenderer();
		m_Tree.setCellRenderer(m_TocRenderer);

		m_Tree.setRootVisible(false);

		// m_Tree.setExpandsSelectedPaths(true);
		// m_Tree.setAutoscrolls(true);
		m_Tree.setShowsRootHandles(true);

		// Posibilidad de seleccionar de forma aleatoria nodos de la leyenda.
		m_Tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		nodeSelectionListener = new NodeSelectionListener(m_Tree);
		m_Tree.addMouseListener(nodeSelectionListener);
		m_Tree.setBackground(UIManager.getColor("Button.background"));
		m_Tree.setBorder(BorderFactory.createEtchedBorder());

		this.addComponentListener(this);

		m_Tree.addTreeExpansionListener(this);

		m_Tree.addOrderListener(this);

		m_Tree.setRowHeight(0); // Para que lo determine el renderer

		m_Scroller = new JScrollPane(m_Tree);
		m_Scroller.setBorder(BorderFactory.createEmptyBorder());

		// scrollPane.setPreferredSize(new Dimension(80,80));
		// Add everything to this panel.
		/*
		 * GridBagLayout gridbag = new GridBagLayout(); GridBagConstraints c =
		 * new GridBagConstraints(); setLayout(gridbag); c.fill =
		 * GridBagConstraints.BOTH; c.weightx = 1.0;
		 * gridbag.setConstraints(check,c);
		 */
		add(m_Scroller); // , BorderLayout.WEST);

		// refresh();
	}

	/**
	 * Elimina los listeners que actuan sobre el TOC, lo único que deja hacer es
	 * desplegar la leyenda de las capas.
	 */
	public void removeListeners() {
		m_Tree.removeMouseListener(nodeSelectionListener);
		m_Tree.invalidateListeners();
	}

	/**
	 * Inserta el FMap.
	 *
	 * @param mc
	 *            FMap.
	 */
	public void setMapContext(MapContext mc) {
		mapContext = mc;
		mapContext.addAtomicEventListener(new AtomicEventListener() {
			/**
			 * @see org.gvsig.fmap.mapcontext.events.listeners.AtomicEventListener#atomicEvent(org.gvsig.fmap.mapcontext.events.AtomicEvent)
			 */
			public void atomicEvent(AtomicEvent e) {
				if ((e.getLayerCollectionEvents().length > 0)
						|| (e.getLegendEvents().length > 0)) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refresh();
						}
					});
				}

				if (e.getLayerEvents().length > 0) {
					repaint();
				}

				if (e.getExtentEvents().length > 0) {
					repaint();
				}
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				refresh();
			}
		});
	}

	/**
	 * DOCUMENT ME!
	 */
	private void setExpandedNodes(DefaultMutableTreeNode node) {
		// int i = 0;
		// Las claves sobrantes de m_ItemsExpanded (provocadas
		// por layerRemove, se quitan en el evento layerRemoved
		// de este TOC
		DefaultMutableTreeNode n;
		Enumeration enumeration = node.children();

		while (enumeration.hasMoreElements()) {
			n = (DefaultMutableTreeNode) enumeration.nextElement();
			if (n.getChildCount() > 0) {
				setExpandedNodes(n);
			}
			TreePath path = new TreePath(m_TreeModel.getPathToRoot(n));
			ITocItem item = (ITocItem) n.getUserObject();
			Boolean b = (Boolean) m_ItemsExpanded.get(item.getLabel());

			if (b == null) // No estaba en el hash todavía: valor por defecto
			{
				// System.out.println("Primera expansión de " +
				// item.getLabel());
				m_Tree.expandPath(path);

				return;
			}

			if (b.booleanValue()) {
				// System.out.println("Expansión de " + item.getLabel());
				m_Tree.expandPath(path);
			} else {
				// System.out.println("Colapso de " + item.getLabel());
				m_Tree.collapsePath(path);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.opensig.gui.IToc#refresh()
	 */
	public void refresh() {
		LayerCollection theLayers = mapContext.getLayers();
		m_Root.removeAllChildren();
		m_Root.setAllowsChildren(true);
		// System.out.println("Refresh del toc");
		doRefresh(theLayers, m_Root);

		m_TreeModel.reload();

		setExpandedNodes(m_Root);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param theLayers
	 *            DOCUMENT ME!
	 * @param parentNode
	 *            DOCUMENT ME!
	 */
	private void doRefresh(LayerCollection theLayers,
			DefaultMutableTreeNode parentNode) {
		Dimension sizeLeaf = new Dimension(m_Tree.getWidth(), 15);
		Dimension sizeBranch = new Dimension(m_Tree.getWidth(), 25);

		for (int i = theLayers.getLayersCount() - 1; i >= 0; i--) {
			FLayer lyr = theLayers.getLayer(i);
			if (!lyr.isInTOC()) {
				continue;
			}
			TocItemBranch elTema = new TocItemBranch(lyr);
			elTema.setSize(sizeBranch);

			DefaultMutableTreeNode nodeLayer = new DefaultMutableTreeNode(
					elTema);

			m_TreeModel.insertNodeInto(nodeLayer, parentNode, parentNode
					.getChildCount());

			// TreePath path = new
			// TreePath(m_TreeModel.getPathToRoot(nodeLayer));
			// m_Tree.makeVisible(path);
			if (lyr instanceof LayerCollection) {
				LayerCollection group = (LayerCollection) lyr;
				doRefresh(group, nodeLayer);
			} else {
				if (lyr instanceof Classifiable) {// && !(lyr instanceof
													// FLyrAnnotation)) {

					Classifiable aux = (Classifiable) lyr;
					ILegend legendInfo = aux.getLegend();

					try {
						if (legendInfo instanceof IClassifiedLegend) {
							IClassifiedLegend cl = (IClassifiedLegend) legendInfo;
							String[] descriptions = cl.getDescriptions();
							ISymbol[] symbols = cl.getSymbols();

							for (int j = 0; j < descriptions.length; j++) {
								TocItemLeaf itemLeaf;
								itemLeaf = new TocItemLeaf(symbols[j],
										descriptions[j], aux.getShapeType());
								itemLeaf.setSize(sizeLeaf);

								DefaultMutableTreeNode nodeValue = new DefaultMutableTreeNode(
										itemLeaf);
								m_TreeModel.insertNodeInto(nodeValue,
										nodeLayer, nodeLayer.getChildCount());

								// TreePath pathSymbol = new
								// TreePath(m_TreeModel.getPathToRoot(
								// nodeValue));
								// m_Tree.makeVisible(pathSymbol);
							}
						}

						if (legendInfo instanceof SingleSymbolLegend
								&& (legendInfo.getDefaultSymbol() != null)) {
							TocItemLeaf itemLeaf;
							itemLeaf = new TocItemLeaf(legendInfo
									.getDefaultSymbol(), legendInfo
									.getDefaultSymbol().getDescription(), aux
									.getShapeType());
							itemLeaf.setSize(sizeLeaf);

							DefaultMutableTreeNode nodeValue = new DefaultMutableTreeNode(
									itemLeaf);
							m_TreeModel.insertNodeInto(nodeValue, nodeLayer,
									nodeLayer.getChildCount());

							// TreePath pathSymbol = new
							// TreePath(m_TreeModel.getPathToRoot(
							// nodeValue));
							// m_Tree.makeVisible(pathSymbol);
						}
					} catch (ReadException e) {
						e.printStackTrace();
					}
				} else if (lyr instanceof IHasImageLegend) {
					TocItemLeaf itemLeaf;
					IHasImageLegend aux = (IHasImageLegend) lyr;
					Image image = aux.getImageLegend();

					if (image != null) {
						itemLeaf = new TocItemLeaf();
						itemLeaf.setImageLegend(image, "", new Dimension(image
								.getWidth(null), image.getHeight(null)));// new
																			// Dimension(150,200));

						DefaultMutableTreeNode nodeValue = new DefaultMutableTreeNode(
								itemLeaf);
						m_TreeModel.insertNodeInto(nodeValue, nodeLayer,
								nodeLayer.getChildCount());
					}
				}
			} // if instanceof layers
		}
	}

	/**
	 * @see com.iver.cit.opensig.gui.toc.ITocOrderListener#orderChanged(int,
	 *      int)
	 */
	public void orderChanged(int oldPos, int newPos, FLayers lpd) {
		// LayerCollection layers = mapContext.getLayers();
		// El orden es el contrario, hay que traducir.
		// El orden es el contrario, hay que traducir.
		// /oldPos = layers.getLayersCount() - 1 - oldPos;
		// /newPos = layers.getLayersCount() - 1 - newPos;
		try {
			lpd.moveTo(oldPos, newPos);
		} catch (CancelationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// No hace falta un refresh, lo hace mediante eventos.
		// refresh();
		mapContext.invalidate();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param lpo
	 *            DOCUMENT ME!
	 * @param lpd
	 *            DOCUMENT ME!
	 * @param ls
	 *            DOCUMENT ME!
	 */
	public void parentChanged(FLayers lpo, FLayers lpd, FLayer ls) {
		lpo.removeLayer(ls);
		lpd.addLayer(ls);

		/*
		 * if (lpo.getLayersCount()==0){ lpo.getParentLayer().removeLayer(lpo); }
		 */
		mapContext.invalidate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		System.out.println("Cambiando tamaño.");

		int i = 0;
		DefaultMutableTreeNode n;
		Enumeration enumeration = m_Root.children();

		while (enumeration.hasMoreElements()) {
			n = (DefaultMutableTreeNode) enumeration.nextElement();

			if (n.getUserObject() instanceof TocItemBranch) {
				ITocItem item = (ITocItem) n.getUserObject();
				Dimension szAnt = item.getSize();
				item.setSize(new Dimension(this.getWidth() - 40, szAnt.height));
			}

		}

		// m_Tree.setSize(this.getSize());
		System.out.println("Ancho del tree=" + m_Tree.getWidth() + " "
				+ m_Tree.getComponentCount());
		System.out.println("Ancho del TOC=" + this.getWidth());

		// m_Tree.repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerListener#legendChanged(com.iver.cit.gvsig.fmap.rendering.LegendChangedEvent)
	 */
	public void legendChanged(LegendChangedEvent e) {
		System.out.println("Refrescando TOC");
		refresh();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdded(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerAdded(LayerCollectionEvent e) {
		refresh();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoved(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
	 */
	public void layerMoved(LayerPositionEvent e) {
		refresh();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoved(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerRemoved(LayerCollectionEvent e) {
		m_ItemsExpanded.remove(e.getAffectedLayer().getName());
		refresh();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerAdding(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerAdding(LayerCollectionEvent e) throws CancelationException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerMoving(com.iver.cit.gvsig.fmap.layers.LayerPositionEvent)
	 */
	public void layerMoving(LayerPositionEvent e) throws CancelationException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#layerRemoving(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void layerRemoving(LayerCollectionEvent e)
			throws CancelationException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#activationChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void activationChanged(LayerCollectionEvent e)
			throws CancelationException {
		repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.iver.cit.gvsig.fmap.layers.LayerCollectionListener#visibilityChanged(com.iver.cit.gvsig.fmap.layers.LayerCollectionEvent)
	 */
	public void visibilityChanged(LayerCollectionEvent e)
			throws CancelationException {
		repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
	 */
	public void treeCollapsed(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		if (n.getUserObject() instanceof ITocItem) {
			ITocItem item = (ITocItem) n.getUserObject();
			Boolean b = Boolean.FALSE;

			// System.out.println("Collapsed: " + item.getLabel());
			m_ItemsExpanded.put(item.getLabel(), b);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
	 */
	public void treeExpanded(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		DefaultMutableTreeNode n = (DefaultMutableTreeNode) path
				.getLastPathComponent();

		if (n.getUserObject() instanceof ITocItem) {
			ITocItem item = (ITocItem) n.getUserObject();
			Boolean b = Boolean.TRUE;

			// System.out.println("Expanded: " + item.getLabel());
			m_ItemsExpanded.put(item.getLabel(), b);
		}
	}

	/**
	 * Obtiene el JScrollPane que contiene el TOC
	 *
	 * @return JScrollPane que contiene el TOC
	 */
	public JScrollPane getJScrollPane() {
		return this.m_Scroller;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public DnDJTree getTree() {
		return m_Tree;
	}

	/**
	 * Clase Listener que reacciona al pulsar sobre el checkbox de un nodo y
	 * crea un popupmenu al pulsar el botón derecho.
	 */
	class NodeSelectionListener extends MouseAdapter implements ActionListener {
		JTree tree;

		JDialog dlg;

		JColorChooser colorChooser;

		FPopupMenu popmenu = null;

		DefaultMutableTreeNode node;

		/**
		 * Crea un nuevo NodeSelectionListener.
		 *
		 * @param tree
		 *            DOCUMENT ME!
		 */
		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param e
		 *            DOCUMENT ME!
		 */
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);
			LayerCollection layers = mapContext.getLayers();

			// System.out.println(e.getSource());
			if (path != null) {
				if (e.getClickCount() == 1) {
					// this fixes a bug when double-clicking. JTree by default
					// expands the tree when double-clicking, so we capture a
					// different node in the second click than in the first
					node = (DefaultMutableTreeNode) path.getLastPathComponent();
				}

				// System.out.println("Evento de ratón originado por " +
				// e.getSource().getClass().toString());
				if (node != null
						&& node.getUserObject() instanceof TocItemBranch) {
					// double click with left button ON A BRANCH/NODE (layer)
					if (e.getClickCount() >= 2
							&& e.getButton() == MouseEvent.BUTTON1) {
						e.consume();
						PluginServices.getMDIManager().setWaitCursor();
						try {
							TocItemBranch leaf = (TocItemBranch) node
									.getUserObject();

							IContextMenuAction action = leaf
									.getDoubleClickAction();
							if (action != null) {
								/*
								 * if there is an action associated with the
								 * double-clicked element it will be fired for
								 * it and FOR ALL OTHER COMPATIBLES THAT HAVE
								 * BEEN ACTIVATED.
								 */
								ArrayList<FLayer> targetLayers = new ArrayList<FLayer>();

								TocItemBranch owner = (TocItemBranch) node
										.getUserObject();

								FLayer masterLayer = owner.getLayer();
								targetLayers.add(masterLayer);
								FLayer[] actives = mapContext.getLayers()
										.getActives();
								for (int i = 0; i < actives.length; i++) {
									if (actives[i].getClass().equals(
											masterLayer.getClass())) {
										if (actives[i] instanceof FLyrVect) {
											FLyrVect vectorLayer = (FLyrVect) actives[i];
											FLyrVect vectorMaster = (FLyrVect) masterLayer;
											if (vectorLayer.getShapeType() == vectorMaster
													.getShapeType()) {
												targetLayers.add(vectorLayer);
											} else {
												vectorLayer.setActive(false);
											}
										}
										// TODO for the rest of layer types
										// (i.e. FLyrRaster)
									} else {
										actives[i].setActive(false);
									}
								}
								action.execute(leaf, targetLayers
										.toArray(new FLayer[0]));
							}
						} catch (Exception ex) {
							NotificationManager.addError(ex);
						} finally {
							PluginServices.getMDIManager().restoreCursor();
						}
						return;
					}

					TocItemBranch elTema = (TocItemBranch) node.getUserObject();
					FLayer lyr = elTema.getLayer();
					lyr.getMapContext().beginAtomicEvent();

					if (((e.getModifiers() & InputEvent.SHIFT_MASK) != 0)
							&& (e.getButton() == MouseEvent.BUTTON1)) {
						FLayer[] activeLayers = layers.getActives();
						if (activeLayers.length > 0) {
							selectInterval(layers, lyr);
						} else {
							updateActive(lyr, !lyr.isActive());
						}

					} else {
						if (!((e.getModifiers() & InputEvent.CTRL_MASK) != 0)
								&& (e.getButton() == MouseEvent.BUTTON1)) {
							layers.setAllActives(false);
						}
						if (e.getButton() == MouseEvent.BUTTON1) {
							// lyr.setActive(true);
							updateActive(lyr, !lyr.isActive());
						}
					}
					// Si pertenece a un grupo, lo ponemos activo también.
					// FLayer parentLayer = lyr.getParentLayer();

					/*
					 * if (parentLayer != null) { parentLayer.setActive(true); }
					 */
					Point layerNodeLocation = tree.getUI().getPathBounds(tree,
							path).getLocation();

					// Rectángulo que representa el checkbox
					Rectangle checkBoxBounds = m_TocRenderer
							.getCheckBoxBounds();
					checkBoxBounds.translate((int) layerNodeLocation.getX(),
							(int) layerNodeLocation.getY());

					if (checkBoxBounds.contains(e.getPoint())) {
						updateVisible(lyr);
					}

					// }
					if (e.getButton() == MouseEvent.BUTTON3) {
						// Boton derecho sobre un nodo del arbol
						// if ((e.getModifiers() & InputEvent.META_MASK) != 0) {
						popmenu = new FPopupMenu(mapContext, node);
						tree.add(popmenu);

						popmenu.show(e.getComponent(), e.getX(), e.getY());

						// }
					}

					lyr.getMapContext().endAtomicEvent();
				}

				if (node != null && node.getUserObject() instanceof TocItemLeaf) {
					// double click with left button ON A LEAF (ISymbol)
					if (e.getClickCount() >= 2
							&& e.getButton() == MouseEvent.BUTTON1) {
						e.consume();

						PluginServices.getMDIManager().setWaitCursor();
						try {
							TocItemLeaf leaf = (TocItemLeaf) node
									.getUserObject();
							IContextMenuAction action = leaf
									.getDoubleClickAction();
							if (action != null) {
								/*
								 * if there is an action associated with the
								 * double-clicked element it will be fired for
								 * it and FOR ALL OTHER COMPATIBLES THAT HAVE
								 * BEEN ACTIVATED.
								 */
								ArrayList<FLayer> targetLayers = new ArrayList<FLayer>();

								TocItemBranch owner = (TocItemBranch) ((DefaultMutableTreeNode) node
										.getParent()).getUserObject();

								FLayer masterLayer = owner.getLayer();
								targetLayers.add(masterLayer);
								FLayer[] actives = mapContext.getLayers()
										.getActives();
								for (int i = 0; i < actives.length; i++) {
									if (actives[i].getClass().equals(
											masterLayer.getClass())) {
										if (actives[i] instanceof FLyrVect) {
											FLyrVect vectorLayer = (FLyrVect) actives[i];
											FLyrVect vectorMaster = (FLyrVect) masterLayer;
											int masterLayerShapetypeOF_THE_LEGEND = ((IVectorLegend) vectorMaster
													.getLegend())
													.getShapeType();
											int anotherVectorLayerShapetypeOF_THE_LEGEND = ((IVectorLegend) vectorLayer
													.getLegend())
													.getShapeType();
											if (masterLayerShapetypeOF_THE_LEGEND == anotherVectorLayerShapetypeOF_THE_LEGEND) {
												targetLayers.add(vectorLayer);
											} else {
												vectorLayer.setActive(false);
											}
										}
										// TODO for the rest of layer types
										// (i.e. FLyrRaster)
									} else {
										actives[i].setActive(false);
									}
								}
								action.execute(leaf, targetLayers
										.toArray(new FLayer[0]));
							}
						} catch (Exception ex) {
							NotificationManager.addError(ex);
						} finally {
							PluginServices.getMDIManager().restoreCursor();
						}
						return;
					}

					// Boton derecho sobre un Simbolo
					// TocItemLeaf auxLeaf = (TocItemLeaf) node.getUserObject();
					// FSymbol theSym = auxLeaf.getSymbol();
					if ((e.getModifiers() & InputEvent.META_MASK) != 0) {
						popmenu = new FPopupMenu(mapContext, node);
						tree.add(popmenu);
						popmenu.show(e.getComponent(), e.getX(), e.getY());
					}
				}

				((DefaultTreeModel) tree.getModel()).nodeChanged(node);

				if (row == 0) {
					tree.revalidate();
					tree.repaint();
				}

				if (PluginServices.getMainFrame() != null) {
					PluginServices.getMainFrame().enableControls();
				}
			} else {
				if (e.getButton() == MouseEvent.BUTTON3) {
					popmenu = new FPopupMenu(mapContext, null);
					tree.add(popmenu);
					popmenu.show(e.getComponent(), e.getX(), e.getY());
				}

			}
		}

		private void selectInterval(LayerCollection layers, FLayer lyr) {
			FLayer[] activeLayers = layers.getActives();
			// if (activeLayers[0].getParentLayer() instanceof FLayers &&
			// activeLayers[0].getParentLayer().getParentLayer()!=null) {
			// selectInterval((LayerCollection)activeLayers[0].getParentLayer(),lyr);
			// }
			for (int j = 0; j < layers.getLayersCount(); j++) {
				FLayer layerAux = layers.getLayer(j);
				// Si se cumple esta condición es porque la primera capa que nos
				// encontramos en el TOC es la que estaba activa
				if (activeLayers[0].equals(layerAux)) {
					boolean isSelected = false;
					for (int i = 0; i < layers.getLayersCount(); i++) {
						FLayer layer = layers.getLayer(i);
						if (!isSelected) {
							isSelected = layer.isActive();
						} else {
							updateActive(layer, true);
							if (lyr.equals(layer)) {
								isSelected = false;
							}
						}
					}
					break;
				} else
				// Si se cumple esta condición es porque la primera capa que nos
				// encontramos en el TOC es la que acabamos de seleccionar
				if (lyr.equals(layerAux)) {
					boolean isSelected = false;
					for (int i = layers.getLayersCount() - 1; i >= 0; i--) {
						FLayer layer = layers.getLayer(i);
						if (!isSelected) {
							isSelected = layer.isActive();
						} else {
							updateActive(layer, true);
							if (lyr.equals(layer)) {
								isSelected = false;
							}
						}
					}
					break;
				}
			}

		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param lyr
		 *            DOCUMENT ME!
		 * @param active
		 *            DOCUMENT ME!
		 */
		private void updateActive(FLayer lyr, boolean active) {
			lyr.setActive(active);
			updateActiveChild(lyr);
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param lyr
		 *            DOCUMENT ME!
		 */
		private void updateActiveChild(FLayer lyr) {
			if (lyr instanceof FLayers) { // Es la raiz de una rama o
				// cualquier nodo intermedio.

				FLayers layergroup = (FLayers) lyr;

				for (int i = 0; i < layergroup.getLayersCount(); i++) {
					layergroup.getLayer(i).setActive(lyr.isActive());
					updateActiveChild(layergroup.getLayer(i));
				}
			}
		}

		/**
		 * Actualiza la visibilidad de la capas.
		 *
		 * @param lyr
		 *            Capa sobre la que se está clickando.
		 */
		private void updateVisible(FLayer lyr) {
			if (lyr.isAvailable()) {
				lyr.setVisible(!lyr.visibleRequired());
				updateVisibleChild(lyr);
				updateVisibleParent(lyr);
			}
		}

		/**
		 * Actualiza de forma recursiva la visibilidad de los hijos de la capa
		 * que se pasa como parámetro.
		 *
		 * @param lyr
		 *            Capa a actualizar.
		 */
		private void updateVisibleChild(FLayer lyr) {
			if (lyr instanceof FLayers) { // Es la raiz de una rama o
											// cualquier nodo intermedio.

				FLayers layergroup = (FLayers) lyr;

				for (int i = 0; i < layergroup.getLayersCount(); i++) {
					layergroup.getLayer(i).setVisible(lyr.visibleRequired());
					updateVisibleChild(layergroup.getLayer(i));
				}
			}
		}

		/**
		 * Actualiza de forma recursiva la visibilidad del padre de la capa que
		 * se pasa como parámetro.
		 *
		 * @param lyr
		 *            Capa a actualizar.
		 */
		private void updateVisibleParent(FLayer lyr) {
			FLayers parent = lyr.getParentLayer();

			if (parent != null) {
				boolean parentVisible = false;

				for (int i = 0; i < parent.getLayersCount(); i++) {
					if (parent.getLayer(i).visibleRequired()) {
						parentVisible = true;
					}
				}

				parent.setVisible(parentVisible);
				updateVisibleParent(parent);
			}
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param arg0
		 *            DOCUMENT ME!
		 */
		public void actionPerformed(ActionEvent arg0) {
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param arg0
		 *            DOCUMENT ME!
		 */
		public void mouseReleased(MouseEvent arg0) {
			super.mouseReleased(arg0);
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param arg0
		 *            DOCUMENT ME!
		 */
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			super.mouseEntered(arg0);

			// FJP: COMENTO ESTO.
			// LO CORRECTO CREO QUE ES IMPLEMENTAR CORRECTAMENTE
			// LOS METODOS DE DRAG AND DROP

			/*
			 * if (m_Root.getChildCount()==0){ m_Tree.dropRoot(m_Root); }
			 */
		}
	}

}
