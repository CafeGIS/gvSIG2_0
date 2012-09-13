/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
	 *
	 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
	 *   Av. Blasco Ibañez, 50
	 *   46010 VALENCIA
	 *   SPAIN
	 *
	 *      +34 963862235
	 *   gvsig@gva.es
	 *      www.gvsig.gva.es
	 *
	 *    or
	 *
	 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
	 *   Campus Universitario s/n
	 *   02071 Alabacete
	 *   Spain
	 *
	 *   +34 967 599 200
	 */
package org.gvsig.remotesensing.decisiontrees.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.remotesensing.decisiontrees.DecisionTree;
import org.gvsig.remotesensing.decisiontrees.DecisionTreeNode;
import org.gvsig.remotesensing.decisiontrees.gui.listener.DecisionTreePanelListener;
import org.gvsig.remotesensing.gui.beans.OptionsPanel;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphModel;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Panel para la herramienta de árboles de decisión.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class DecisionTreePanel extends DefaultButtonsPanel {

	/**
	 * 
	 */
	private static final long 		serialVersionUID 	= 2800193252856199039L;
	private static final Color[]	colors  = new Color[] {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN,
		Color.ORANGE, Color.PINK, Color.WHITE, Color.BLACK};
	public  static final int 		POSITION_LEFT 		= 0;
	public  static final int 		POSITION_RIGHT 		= 1;
	
	private JGraph 			  			jGraph     	   		= null;
	private DecisionTreeDialog  		decisionTreeDialog 	= null;
	private JScrollPane					scrollPane			= null;
	private JPanel						menuBarPanel		= null;
	private JMenuBar 					menuBar			 	= null;
	private JMenuItem 					closeMenuItem 		= null;
	private JMenuItem 					saveMenuItem 		= null;
	private JMenuItem 					loadMenuItem 		= null;
	private JMenuItem 					exportMenuItem 		= null;
	private JMenuItem 					helpMenuItem 		= null;
	private JMenuItem 					importMenuItem		= null;
	private JPopupMenu					jPopupMenu			= null;
	private JMenuItem 					addMenuItem			= null;
	private JMenuItem					deleteMenuItem		= null;
	private JMenuItem					executeMenuItem		= null;
	private DecisionTree				decisionTree		= null;
	private DecisionTreePanelListener 	listener 			= null;
	private DefaultGraphCell 			selectedCell 		= null;
	private View						view				= null;
	private JTabbedPane					tabbedPanel 		= null; 
	private OptionsPanel				outputOptionsPanel  = null;
	private JPanel 						treePanel 			= null;
	private JButton						jButtonSaveTree		= null;
	private JButton						jButtonLoadTree		= null;

	public DecisionTreePanel(DecisionTreeDialog decisionTreeDialog, View view) {
		super(ButtonsPanel.BUTTONS_APPLYCLOSE);
		this.view = view;
		this.decisionTreeDialog = decisionTreeDialog;
		initialize();
	}
	
	private void initialize() {
		initTree();
		reloadGraph();
		setLayout(new BorderLayout(5,5));
		setBorder(new EmptyBorder(2,2,10,2));		
		add(getMenuBarPanel(),BorderLayout.NORTH);
		add(getTabbedPanel());
		listener = new DecisionTreePanelListener(this);
		getAddMenuItem().addActionListener(listener);
		getDeleteMenuItem().addActionListener(listener);
		getExecuteMenuItem().addActionListener(listener);
		getCloseMenuItem().addActionListener(listener);
		getLoadMenuItem().addActionListener(listener);
		getSaveMenuItem().addActionListener(listener);
		getImportMenuItem().addActionListener(listener);
		getExportMenuItem().addActionListener(listener);
		getJGraph().addMouseListener(listener);
		getJButtonLoadTree().addActionListener(listener);
		getJButtonSaveTree().addActionListener(listener);
		this.addButtonPressedListener(listener);
	}

	private Component getTabbedPanel() {
		if (tabbedPanel==null){
			tabbedPanel = new JTabbedPane();
			tabbedPanel.setBorder(new EmptyBorder(0,10,0,10));	
			tabbedPanel.addTab(PluginServices.getText(this, "arbol"), getTreePanel());
			tabbedPanel.addTab(PluginServices.getText(this,"output_options"), getOutputOptionsPanel());
		}
		return tabbedPanel;
	}

	/**
	 * Crea el árbol de decisión inicial
	 *
	 */
	private void initTree() {
		getDecisionTree().getRoot().setChildren();
		getDecisionTree().getRoot().setExpression("");
		getDecisionTree().getRoot().getLeftChild().setClassID(generateClassId());
		getDecisionTree().getRoot().getRightChild().setClassID(generateClassId());
	}
	
	public void initJGraph(){
		jGraph = null;
	}
	
	/**
	 * Reconstruye el gráfico a partir del Árbol (root).
	 *
	 */
	public void reloadGraph(){
		/*
		 * Limpiar el gráfico.
		 */
		getJGraph().getGraphLayoutCache().remove(getJGraph().getGraphLayoutCache().getCells(false, true, false, true),true,true);
		getJGraph().getGraphLayoutCache().reload();
		/*
		 * Construir el gráfico a partir el árbol.
		 */
		insertCells(getDecisionTree().getRoot(),null,-1);
	}

	/**
	 * Inserta recursivamente en el gráfico los nodos y arcos el árbol que radica en <code>node</node>
	 * en el gráfico.
	 * 
	 * @param node nodo raiz del subárbol.
	 * @param parent nodo antecesor del subárbol a insertar
	 * @param position indica si <code>node</node> es el hijo izquierdo (0) o derecho (1) de <code>parent</code>
	 */
	public void insertCells(DecisionTreeNode node, DefaultGraphCell parent, int position) {
		int x = 0;
		int y = 0;
		int w = 90;
		int h = 40;
		if(parent==null){
			y = 10;
			//x = getScrollPane().getPreferredSize().width/2 - 20;
			//x = node.getLevelsCount() * w -w +10;
			x = getXFactor(node,null) * w - w + 10;
		}
		else{
			int leftLevels = ((DecisionTreeNode)parent.getUserObject()).getLeftChild().getLevelsCount();
			int rightLevels = ((DecisionTreeNode)parent.getUserObject()).getRightChild().getLevelsCount();
			int minLevels = Math.min(leftLevels, rightLevels);
			y = ((int)GraphConstants.getBounds(parent.getAttributes()).getMaxY())+50;
			if(position==POSITION_LEFT)
				x = ((int)GraphConstants.getBounds(parent.getAttributes()).getMinX())-w*minLevels;
			else
				x = ((int)GraphConstants.getBounds(parent.getAttributes()).getMinX())+(w)*minLevels;
		}
		DefaultGraphCell nodeVertex = createVertex(node, x, y, w, h, null, false);
		
		/*
		 * Si es un nodo final:
		 */
		if (node.isFinal()){
			Color color = (Color)getDecisionTree().getColorTable().get(new Integer(node.getClassID()));
			GraphConstants.setBackground(nodeVertex.getAttributes(), color);
			GraphConstants.setOpaque(nodeVertex.getAttributes(), true);
		}
		
		getJGraph().getGraphLayoutCache().insert(nodeVertex);
		if (parent!=null){
			String label = PluginServices.getText(this, "si");
			if(position==POSITION_LEFT)
				label = PluginServices.getText(this, "no");
			DefaultEdge edge = createEdge(nodeVertex, parent, label);
			getJGraph().getGraphLayoutCache().insert(edge);
		}
			
		if (node.getLeftChild()!=null){
			insertCells(node.getLeftChild(), nodeVertex, POSITION_LEFT);
		}
		if (node.getRightChild()!=null){
			insertCells(node.getRightChild(), nodeVertex, POSITION_RIGHT);
		}
	}
	
	private int getXFactor(DecisionTreeNode node, DecisionTreeNode parent){
		if (parent == null){
			return 1+getXFactor(node.getLeftChild(),node);
		}
		int leftLevels = parent.getLeftChild().getLevelsCount();
		int rightLevels = parent.getRightChild().getLevelsCount();
		int minLevels = Math.min(leftLevels, rightLevels);
		
		if (node.getLeftChild()!=null)
			return minLevels+getXFactor(node.getLeftChild(),node);
		
		return 1+minLevels;
	}

	private DefaultGraphCell createVertex(Object name, double x,
			double y, double w, double h, Color bg, boolean raised) {

			// Create vertex with the given name
			DefaultGraphCell cell = new DefaultGraphCell(name);

			// Set bounds
			GraphConstants.setBounds(cell.getAttributes(),
					new Rectangle2D.Double(x, y, w, h));

			// Set fill color
			if (bg != null) {
				GraphConstants.setGradientColor(
					cell.getAttributes(), bg);
				GraphConstants.setOpaque(
					cell.getAttributes(), true);
			}

			// Set raised border
			if (raised)
				GraphConstants.setBorder(
					cell.getAttributes(), 
					BorderFactory.createRaisedBevelBorder());
			else
				// Set black border
				GraphConstants.setBorderColor(
					cell.getAttributes(), Color.black);

			// Add a Port
			DefaultPort port = new DefaultPort();
			cell.add(port);
			port.setParent(cell);
			
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory.createEtchedBorder());
	    	GraphConstants.setAutoSize(cell.getAttributes(),false);
	    	GraphConstants.setResize(cell.getAttributes(),false);
	    	GraphConstants.setInset(cell.getAttributes(), 10);
	    	GraphConstants.setMoveable(cell.getAttributes(), true);
	    	GraphConstants.setSizeable(cell.getAttributes(),true);

			return cell;
		}
	
	private DefaultEdge createEdge(DefaultGraphCell cell, DefaultGraphCell parentCell, String label){
		DefaultEdge edge = new DefaultEdge(label);
		edge.setSource(parentCell);
		edge.setTarget(cell);
		GraphConstants.setLineEnd(edge.getAttributes(), GraphConstants.ARROW_SIMPLE);
		GraphConstants.setEndFill(edge.getAttributes(), false);
		GraphConstants.setAutoSize(edge.getAttributes(), true);
		getJGraph().getGraphLayoutCache().insert(edge);
		return edge;
	}

	public JScrollPane getScrollPane() {
		if (scrollPane == null){
			scrollPane = new JScrollPane(getJGraph());
			scrollPane.setPreferredSize(new Dimension(500,400));
		}
		return scrollPane;
	}

	
	public JPanel getTreePanel(){
		
		if(treePanel ==null){
			treePanel = new JPanel();
			BorderLayout bd= new BorderLayout();
			treePanel.setLayout(bd);
			treePanel.add(getScrollPane(), BorderLayout.CENTER);
			
			JPanel paux= new JPanel();
			paux.add(getJButtonLoadTree());
			paux.add(getJButtonSaveTree());
			
			JPanel p= new JPanel();
			p.setLayout(new BorderLayout());
			p.add(paux, BorderLayout.WEST);
			treePanel.add(p,BorderLayout.SOUTH);
		}
		
		return treePanel;
	}
	
	public JGraph getJGraph() {
		if (jGraph == null){
			GraphModel model = new DefaultGraphModel();
			jGraph = new JGraph(model);
			
			jGraph.setCloneable(false);
			jGraph.setEditable(false);
	        jGraph.setInvokesStopCellEditing(false);
	        jGraph.setJumpToDefaultPort(true);
	        jGraph.setConnectable(false);
	        jGraph.setDisconnectable(false);
		}
		return jGraph;
	}
	
	public JMenuBar getMenuBar() {
		if (menuBar==null){
			menuBar = new JMenuBar();
			menuBar.setBorder(new EmptyBorder(0,0,0,0));
			JMenu fileMenu = new JMenu(PluginServices.getText(this, "archivo"));
			JMenu optionsMenu = new JMenu(PluginServices.getText(this, "opciones"));
			JMenu helpMenu = new JMenu(PluginServices.getText(this, "help"));
			
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getLoadMenuItem());
			fileMenu.add(getImportMenuItem());
			fileMenu.add(getExportMenuItem());
			fileMenu.add(getImportMenuItem());
			fileMenu.add(getCloseMenuItem());
			helpMenu.add(getHelpMenuItem());
			menuBar.add(fileMenu);
			menuBar.add(optionsMenu);
			menuBar.add(helpMenu);
		}
		return menuBar;
	}
	
	public JMenuItem getExportMenuItem() {
		if (helpMenuItem == null){
			exportMenuItem = new JMenuItem(PluginServices.getText(this, "exportar"));
		}
		return exportMenuItem;
	}
	
	public JMenuItem getImportMenuItem() {
		if (importMenuItem == null){
			importMenuItem = new JMenuItem(PluginServices.getText(this, "importar"));
		}
		return importMenuItem;
	}

	public JMenuItem getHelpMenuItem() {
		if (helpMenuItem == null){
			helpMenuItem = new JMenuItem(PluginServices.getText(this, "help"));
		}
		return helpMenuItem;
	}

	public JMenuItem getLoadMenuItem() {
		if (loadMenuItem == null){
			loadMenuItem = new JMenuItem(PluginServices.getText(this, "cargar"));
		}
		return loadMenuItem;
	}

	public JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null){
			saveMenuItem = new JMenuItem(PluginServices.getText(this, "salvar"));
		}
		return saveMenuItem;
	}
	
	public JMenuItem getCloseMenuItem() {
		if (closeMenuItem == null){
			closeMenuItem = new JMenuItem(PluginServices.getText(this, "cerrar"));
		}
		return closeMenuItem;
	}

	public JPopupMenu getJPopupMenu() {
		if (jPopupMenu==null){
			jPopupMenu = new JPopupMenu("");
			jPopupMenu.add(getAddMenuItem());
			jPopupMenu.add(getDeleteMenuItem());
			jPopupMenu.add(getExecuteMenuItem());
		}
		return jPopupMenu;
	}

	public JMenuItem getAddMenuItem() {
		if (addMenuItem==null){
			addMenuItem = new JMenuItem(PluginServices.getText(this, "addChild"));
		}
		return addMenuItem;
	}

	public JMenuItem getDeleteMenuItem() {
		if (deleteMenuItem==null){
			deleteMenuItem = new JMenuItem(PluginServices.getText(this, "deleteChild"));
		}
		return deleteMenuItem;
	}

	public JMenuItem getExecuteMenuItem() {
		if (executeMenuItem == null)
			executeMenuItem = new JMenuItem(PluginServices.getText(this, "ejecutar"));
		return executeMenuItem;
	}
	
	public void setSelectedCell(DefaultGraphCell cell) {
		selectedCell = cell;
	}

	public DefaultGraphCell getSelectedCell() {
		return selectedCell;
	}
	

	public View getView() {
		return view;
	}

	public DecisionTreeDialog getDecisionTreeDialog() {
		return decisionTreeDialog;
	}

	public OptionsPanel getOutputOptionsPanel() {
		if (outputOptionsPanel == null){
			outputOptionsPanel = new OptionsPanel(view);
		}
		return outputOptionsPanel;
	}
	
	/**
	 * Devuelve un color para asignar a un nuevo nodo final
	 * 
	 * @return Color
	 */
	private Color generateColor(){
		return null;
	}
	
	/**
	 * Devuelve un ID para asignar a un nuevo nodo final
	 * 
	 * @return int
	 */
	public int generateClassId(){
		Color color = null;
		int classColorsCount = getDecisionTree().getColorTable().size();
		for (int i=0; i<classColorsCount;i++)
			if(!getDecisionTree().getColorTable().containsKey(new Integer(i))){
				if (i<colors.length)
					color = colors[i];
				else
					color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
				getDecisionTree().getColorTable().put(new Integer(i), color);
				return i;
			}
		if (classColorsCount<colors.length)
			color = colors[classColorsCount];
		else
			color = new Color((float)Math.random(),(float)Math.random(),(float)Math.random());
		getDecisionTree().getColorTable().put(new Integer(classColorsCount), color);
		return classColorsCount;
	}

	/**
	 * Elimina las entradas de <code>classColors</code> que se están
	 * usando en el árbol.
	 *
	 */
	public void cleanClassColors() {
		ArrayList ids = new ArrayList();
		getDecisionTree().getRoot().getFinals(ids);
		Object keys[] = getDecisionTree().getColorTable().keySet().toArray();
		for (int i=0; i < keys.length; i++){
			if (!ids.contains(keys[i]))
				getDecisionTree().getColorTable().remove(keys[i]);
		}
	}

	public JPanel getMenuBarPanel() {
		if (menuBarPanel == null){
			menuBarPanel = new JPanel();
			menuBarPanel.setLayout(new BorderLayout());
		//	menuBarPanel.add(getMenuBar(),BorderLayout.WEST);
		}
		return menuBarPanel;
	}

	/**
	 * 
	 * @return Arbol de decisión asociado al panel.
	 */
	public DecisionTree getDecisionTree() {
		if (decisionTree == null)
			decisionTree = new DecisionTree();
		return decisionTree;
	}

	public JButton getJButtonLoadTree() {
		
		if(jButtonLoadTree==null){
			jButtonLoadTree= new JButton(PluginServices.getText(this,"loadtree"));
		}
		return jButtonLoadTree;
	}

	public JButton getJButtonSaveTree() {
		if(jButtonSaveTree==null){
			jButtonSaveTree= new JButton(PluginServices.getText(this,"savetree"));
		}
		return jButtonSaveTree;
	}
	
	

}
