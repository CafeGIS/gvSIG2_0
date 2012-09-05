/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibï¿½ï¿½ez, 50
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
package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.gvsig.fmap.mapcontext.rendering.legend.ZSort;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMultiLayerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbolDrawingException;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.AcceptCancelPanel;
import org.gvsig.gui.beans.swing.JBlank;
import org.gvsig.gui.beans.swing.celleditors.BooleanTableCellEditor;
import org.gvsig.gui.beans.swing.celleditors.IntegerTableCellEditor;
import org.gvsig.gui.beans.swing.cellrenderers.BooleanTableCellRenderer;
import org.gvsig.gui.beans.swing.cellrenderers.NumberTableCellRenderer;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.gui.TableSymbolCellRenderer;

/**
 * Creates a panel to specify an order for the symbols of a map. This order
 * is important when the map is going to be painted because, apart from that
 * the waste of time can be less, the final representation of the map will
 * depend on this order.
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SymbolLevelsWindow extends JPanel implements IWindow, ActionListener {
	private static final long serialVersionUID = 3241898997869313055L;
	private static final int DESCRIPTION_COLUMN_INDEX = 1;
	private static final int SYMBOL_COLUMN_INDEX = 0;
	private static final int MERGE_COLUMN_INDEX = 3;
	private static final int JOIN_COLUMN_INDEX = 2;
	private static final int FIRST_LEVEL_COLUMN_INDEX = 2;
	private static final String[] defaultHeaders = new String[] {
		PluginServices.getText(SymbolLevelsWindow.class, "symbol"),
		PluginServices.getText(SymbolLevelsWindow.class, "description"),
		PluginServices.getText(SymbolLevelsWindow.class, "join"),
		PluginServices.getText(SymbolLevelsWindow.class, "merge"),
	};
	private static final int DEFAULT_VIEW = 0;
	private static final int ADVANCED_VIEW = 1;
	private static int viewMode = ADVANCED_VIEW;
	private JCheckBox chkSpecifyDrawOrder = null;
	private JPanel pnlCenter = null;
	private JScrollPane srclLevels = null;
	private JTable tblLevels = null;
	private JButton btnUp = null;
	private JButton btnDown;
	private JPanel pnlSouth = null;
	private String[] advancedHeaders;
	private JButton btnSwitchView;
	private ZSort zSort;
	private SymbolSummary summary = new SymbolSummary();

	private ActionListener action = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			if ("OK".equals(actionCommand)) {
				tblLevels.editingStopped(new ChangeEvent(tblLevels));
				applyValues();
			}

			PluginServices.getMDIManager().closeWindow(SymbolLevelsWindow.this);

		}
	};

	public WindowInfo getWindowInfo() {
		WindowInfo wi = new WindowInfo(WindowInfo.RESIZABLE | WindowInfo.MODALDIALOG);
		wi.setTitle(PluginServices.getText(this, "symbol_levels"));
		wi.setWidth(getWidth()+10);
		wi.setHeight(getHeight());
		return wi;
	}
	/**
	 * Completes the main table of the panel with the symbols contained in the
	 * legend of the map.
	 *
	 */
	private void applyValues() {

		// update symbol order
		TableModel model = tblLevels.getModel();



		Hashtable<ISymbol, int[] > aTable = new Hashtable<ISymbol, int[]>();
		ISymbol[] symbols = new ISymbol[model.getRowCount()];
		for (int i = 0; i < symbols.length; i++) {
			symbols[i] = (ISymbol) model.getValueAt(i, SYMBOL_COLUMN_INDEX);
			int length = 1;
			if (symbols[i] instanceof IMultiLayerSymbol) {
				IMultiLayerSymbol mlSym = (IMultiLayerSymbol) symbols[i];
				length = mlSym.getLayerCount();
			}

			int[] symbolLevels = new int[length];
			if (viewMode == DEFAULT_VIEW) {
				// default view (JOIN and MERGE)
				if (symbols[i] instanceof IMultiLayerSymbol) {
					boolean join = ((Boolean) model.getValueAt(i, JOIN_COLUMN_INDEX)).booleanValue();
					boolean merge= ((Boolean) model.getValueAt(i, MERGE_COLUMN_INDEX)).booleanValue();
					boolean needToJoin = true;
					if (merge && i>0) {
						int j=0;
						try {
							int[] prevSymbolLevels = aTable.get(symbols[i-1]);
							for (j = 0; j < symbolLevels.length; j++) {
								symbolLevels[j] = prevSymbolLevels[j];
							}
						} catch (IndexOutOfBoundsException ex) {
							/* perfect, no problem
							 * the previous symbol has different amount of layers
							 * that is ok because we just have to replicate
							 * the values of each cell
							 */
							for (; j < symbolLevels.length; j++) {
								symbolLevels[j] = symbolLevels[j-1]+1;
							}
						}
						needToJoin = false;
					}
					if (join && needToJoin) {
						for (int j = 0; j < symbolLevels.length; j++) {
							symbolLevels[j] = zSort.getLevelCount()+j+1;
						}
					}
					if (!join && !merge) {
						for (int j = 0; j < symbolLevels.length; j++) {
							symbolLevels[j] = zSort.getLevelCount();
						}
					}
				} else {
					symbolLevels[0] = zSort.getLevelCount();
				}
			} else {
				// ADVANCED VIEW (user may set map levels manually)
				for (int j = 0; j < symbolLevels.length; j++) {
					symbolLevels[j] = ((Integer) model.getValueAt(i, j+FIRST_LEVEL_COLUMN_INDEX)).intValue();
				}
			}

			aTable.put(symbols[i], symbolLevels);
		}

		Iterator<ISymbol> it = aTable.keySet().iterator();
		while (it.hasNext()) {
			ISymbol sym = it.next();
			zSort.setLevels(sym, aTable.get(sym));
		}

		zSort.setUsingZSort(getChkSpecifyDrawOrder().isSelected());
	}

	public SymbolLevelsWindow(ZSort zSort) {
		super();
		initialize();
		setModel(zSort);
		quitaEsteMetodo();
		tblLevels.setRowHeight(23);
	}
	private void quitaEsteMetodo() {
		getBtnSwitchView().setEnabled(false);
		getBtnDown().setEnabled(false);
		getBtnUp().setEnabled(false);
	}
	/**
	 * Sets the model
	 * @param plan ZSort
	 */

	public void setModel(ZSort plan) {
		advancedHeaders = new String[FIRST_LEVEL_COLUMN_INDEX
		                             +plan.getTopLevelIndexAllowed() ];
		advancedHeaders[SYMBOL_COLUMN_INDEX] = defaultHeaders[SYMBOL_COLUMN_INDEX];
		advancedHeaders[DESCRIPTION_COLUMN_INDEX] = defaultHeaders[DESCRIPTION_COLUMN_INDEX];
		for (int i = 2; i < advancedHeaders.length; i++) {
			advancedHeaders[i] = String.valueOf(i-1);
		}
		this.zSort = plan;
		getChkSpecifyDrawOrder().setSelected(plan.isUsingZSort());
		initTableContents(getTblLevels(), plan, viewMode);
	}
	/**
	 * Initializes the table that it is showed in the panel where the user can
	 * see the different symbols of the legend and has options to specify the
	 * level for each one, merge and so on.
	 *
	 * @param table
	 * @param zSort
	 * @param mode
	 */
	private void initTableContents(JTable table, ZSort zSort, int mode) {
		DefaultTableModel model = new DefaultTableModel();
		Object[][] dataVector = null;
		ISymbol[] syms = zSort.getSymbols();
		String[] labels = zSort.getDescriptions();

		if (mode == DEFAULT_VIEW) {
			// default view (JOIN and MERGE)
			dataVector = new Object[syms.length][syms.length];
			for (int i = 0; i < syms.length; i++) {
				dataVector[i] = new Object[defaultHeaders.length];
				dataVector[i][SYMBOL_COLUMN_INDEX] = syms[i];
				dataVector[i][DESCRIPTION_COLUMN_INDEX] = labels[i];
				if (syms[i] instanceof IMultiLayerSymbol) {
					boolean joined = true;
					int[] levels = zSort.getLevels(syms[i]);
					if(levels != null){
						for (int j = 0; j < levels.length; j++) {
							if (joined) {
								joined = levels[j] != levels[j+1];
							}
						}
					}


					boolean merged = true;
					if (i<syms.length-1) {
						for (int j = 0; joined && j < levels.length; j++) {
							// must be joined to be merged
							ISymbol nextSymbol = syms[i+1];
							int[] nextLevels = zSort.getLevels(nextSymbol);
							if(nextLevels != null){
								if (nextSymbol instanceof IMultiLayerSymbol) {
									if (j<nextLevels.length) {
										merged = levels[j] == nextLevels[j];
									}
								} else {
									merged = levels[0] == nextLevels[0];
								}
							}
						}
						if (!merged) {
							break;
						}
					}
					if (!joined) {
						merged = false;
					}
					dataVector[i][JOIN_COLUMN_INDEX] = new Boolean(joined);
					dataVector[i][MERGE_COLUMN_INDEX] = new Boolean(merged);
				}
			}

			model.setDataVector(dataVector, defaultHeaders);
			table.setModel(model);
			TableColumn col = table.getColumnModel().getColumn(JOIN_COLUMN_INDEX);
			col.setCellRenderer(new BooleanTableCellRenderer(true));
			col.setCellEditor(new BooleanTableCellEditor(table));
			col = table.getColumnModel().getColumn(MERGE_COLUMN_INDEX);
			col.setCellRenderer(new BooleanTableCellRenderer(true));
			col.setCellEditor(new BooleanTableCellEditor(table));
		} else {
			// advanced view (user may input the map level manually)
			dataVector = new Object[syms.length][
			                                     FIRST_LEVEL_COLUMN_INDEX + /* this is the first column that
			                                      * contains a level for the symbol
			                                      */

			                                     zSort.getTopLevelIndexAllowed() + /* according to the set of
			                                      * symbols this will get the
			                                      * max level reachable
			                                      */
			                                     1 /* plus 1 to get a count instead of an index */];
			for (int i = 0; i < syms.length; i++) {
				dataVector[i][SYMBOL_COLUMN_INDEX] = syms[i];
				dataVector[i][DESCRIPTION_COLUMN_INDEX] = labels[i];
				if (syms[i] instanceof IMultiLayerSymbol) {
					int[] levels = zSort.getLevels(syms[i]);

					for (int j = 0; j < levels.length; j++) {
						dataVector[i][j+FIRST_LEVEL_COLUMN_INDEX] = levels[j];
					}
				} else {
					dataVector[i][FIRST_LEVEL_COLUMN_INDEX] = new Integer(zSort.getLevels(syms[i])[0]);
				}
			}

			model.setDataVector(dataVector, advancedHeaders);
			table.setModel(model);
			for (int j = FIRST_LEVEL_COLUMN_INDEX; j < model.getColumnCount(); j++) {

				table.getColumnModel().getColumn(j).setCellRenderer(new NumberTableCellRenderer(true, false));
				table.getColumnModel().getColumn(j).setCellEditor(new IntegerTableCellEditor());
			}
		}

		TableSymbolCellRenderer symbolCellRenderer = new TableSymbolCellRenderer(true) {
			private static final long serialVersionUID = 5603529641148869112L;

			{ // Object static initialize block

				preview = new SymbolPreviewer() {
					private static final long serialVersionUID = 7262380340075167043L;
					private Icon downIcon = new Icon(){
						public int getIconHeight() { return 7; }
						public int getIconWidth() { return 7; }
						public void paintIcon(Component c, Graphics g, int x, int y) {
							Graphics2D g2 = (Graphics2D) g;
							g2.setColor(Color.GRAY);
							g2.translate(x + c.getWidth()-getIconWidth()*2, y + c.getHeight()-getIconHeight()*2);
							GeneralPath gp = new GeneralPath();
							gp.moveTo(0, 0);
							gp.lineTo(getIconWidth()/2, getIconHeight()-1);
							gp.lineTo(getIconWidth()-1, 0);
							g2.fill(gp);
							g2.translate(-(x + c.getWidth()-getIconWidth()*2), -(y + c.getHeight()-getIconHeight()*2));
						}
					};
					@Override
					public void paint(Graphics g) {
						super.paint(g);
						if (getSymbol() instanceof IMultiLayerSymbol) {
							downIcon.paintIcon(this, g, 0, 0);
						}
					}
				};

			} // Object static initialize block
		};
		TableColumn col = table.getColumnModel().getColumn(SYMBOL_COLUMN_INDEX);
		col.setCellRenderer(symbolCellRenderer);
	}

	private void initialize() {
		this.setLayout(new BorderLayout(15, 15));
		this.setSize(564, 344);

		this.add(getChkSpecifyDrawOrder(), BorderLayout.NORTH);
		this.add(new JBlank(20, 20));
		this.add(getPnlCenter(), BorderLayout.CENTER);
		this.add(getPnlSouth(), BorderLayout.SOUTH);
		tblLevels.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) { }
			public void mouseClicked(MouseEvent e)  { }
			public void mouseEntered(MouseEvent e)  { }
			public void mouseExited(MouseEvent e)  {
				summary.sym = null;
				repaint();
	}

			public void mousePressed(MouseEvent e) {
				Point where = e.getPoint();
				int whereX = where.x;
				int whereY = where.y;
				Rectangle bounds = tblLevels.getBounds();
				/*
				 * calculate the right border x-position of the symbol
				 * column
	 			*/
				int rightEdge = 0;
				for (int i = 0; i <= SYMBOL_COLUMN_INDEX; i++) {
					rightEdge += tblLevels.getColumnModel().getColumn(i).getWidth();
				}
				if (whereX >= bounds.x &&
					whereX <= rightEdge + bounds.x &&
					whereY >= bounds.y &&
					whereY <= bounds.height + bounds.y) {
					int rowHeight = tblLevels.getRowHeight();
					int rowClicked = (whereY - bounds.y) / rowHeight;
					ISymbol sym = (ISymbol) tblLevels.
											getModel().
											getValueAt(
												rowClicked,
												SYMBOL_COLUMN_INDEX);
					if (sym instanceof IMultiLayerSymbol) {
						summary.sym = (IMultiLayerSymbol) sym;
						summary.rowIndex = rowClicked;
					} else {
						summary.sym = null;
					}
					repaint();
				}
			}

		});

	}

	private JCheckBox getChkSpecifyDrawOrder() {
		if (chkSpecifyDrawOrder == null) {
			chkSpecifyDrawOrder = new JCheckBox("<html><b>"+
					PluginServices.getText(this, "draw_symbols_in_specified_order")
					+"</b></html>");
			chkSpecifyDrawOrder.addActionListener(this);
		}
		return chkSpecifyDrawOrder;
	}


	private JPanel getPnlCenter() {
		if (pnlCenter == null) {
			pnlCenter = new JPanel();
			pnlCenter.setLayout(new BorderLayout(0, 15));
			pnlCenter.add(getSrclLevels(), BorderLayout.CENTER);
			pnlCenter.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			JPanel aux = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
			JPanel pnlButtons = new JPanel();
			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.Y_AXIS));
			pnlButtons.add(new JBlank(1, 70));
			pnlButtons.add(getBtnUp());
			pnlButtons.add(new JBlank(1, 10));
			pnlButtons.add(getBtnDown());
			pnlButtons.setVisible(false);
			aux.add(pnlButtons);
			pnlCenter.add(aux, BorderLayout.EAST);

		}
		pnlCenter.setEnabled(getChkSpecifyDrawOrder().isSelected());
		return pnlCenter;
	}

	private JScrollPane getSrclLevels() {
		if (srclLevels == null) {
			srclLevels = new JScrollPane();
			srclLevels.setViewportView(getTblLevels());
		}
		srclLevels.setEnabled(getChkSpecifyDrawOrder().isSelected());
		return srclLevels;
	}


	private JTable getTblLevels() {
		if (tblLevels == null) {
			tblLevels = new JTable() {
				private static final long serialVersionUID = -1545710722048183232L;

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					summary.paint((Graphics2D) g);
		}
			};
			summary.rowHeight = tblLevels.getRowHeight();
		}
		tblLevels.setEnabled(getChkSpecifyDrawOrder().isSelected());
		return tblLevels;
	}


	private JButton getBtnUp() {
		if (btnUp == null) {
			btnUp = new JButton(PluginServices.getIconTheme().get("arrow-up-icono"));
			btnUp.setActionCommand("MOVE_UP");
		}
		return btnUp;
	}

	private JButton getBtnDown() {
		if (btnDown == null) {
			btnDown = new JButton(PluginServices.getIconTheme().get("arrow-down-icono"));
			btnDown.setActionCommand("MOVE_DOWN");
		}
		return btnDown;
	}


	private JPanel getPnlSouth() {
		if (pnlSouth == null) {
			pnlSouth = new JPanel(new BorderLayout());
			JPanel aux = new JPanel();
			aux.setLayout(new FlowLayout(FlowLayout.RIGHT));
			aux.add(getBtnSwitchView());
			pnlSouth.add(aux, BorderLayout.NORTH);

			aux = new JPanel();
			aux.setLayout(new FlowLayout(FlowLayout.RIGHT));
			pnlSouth.add(new AcceptCancelPanel(action, action), BorderLayout.SOUTH);

		}
		return pnlSouth;
	}

	private JButton getBtnSwitchView() {
		if (btnSwitchView == null) {
			btnSwitchView = new JButton(
					(viewMode != DEFAULT_VIEW) ?
					PluginServices.getText(this, "default_view"):
					PluginServices.getText(this, "advanced_view")
					);
			btnSwitchView.addActionListener(this);
			btnSwitchView.setVisible(false);
		}

		return btnSwitchView;
	}

	public void actionPerformed(ActionEvent e) {
		JComponent c = (JComponent) e.getSource();
		if (c.equals(getChkSpecifyDrawOrder())) {
			getPnlCenter().setEnabled(getChkSpecifyDrawOrder().isSelected());
			getSrclLevels().setEnabled(getChkSpecifyDrawOrder().isSelected());
			TableCellEditor tce = getTblLevels().getCellEditor();
			if (tce != null){
				tce.stopCellEditing();
			}
			getTblLevels().setEnabled(getChkSpecifyDrawOrder().isSelected());
		} else if (c.equals(getBtnSwitchView())) {
			viewMode = (viewMode == ADVANCED_VIEW) ? DEFAULT_VIEW : ADVANCED_VIEW;
			initTableContents(getTblLevels(), zSort, viewMode);
			btnSwitchView.setText((viewMode != DEFAULT_VIEW) ?
					PluginServices.getText(this, "default_view"):
					PluginServices.getText(this, "advanced_view"));
		}
	}
	/**
	 * Gets the ZSort value
	 *
	 * @return zSort ZSort
	 */
	public ZSort getZSort() {
		return zSort;
	}

	private class SymbolSummary {
		int witdh;
		int rowHeight = 10;

		int rowIndex;

		IMultiLayerSymbol sym;

		void paint(Graphics2D g){

			if (sym != null) {
				int whereY = (rowHeight*(rowIndex-1) + (int) (rowHeight/0.6));
				int whereX = 0;
				for (int i = 0; i <= SYMBOL_COLUMN_INDEX; i++) {
					whereX += tblLevels.getColumnModel().getColumn(i).getWidth();
				}
				whereX -= 40;
				int width = 150;
				int height = Math.max(rowHeight*sym.getLayerCount(), rowHeight);
				Rectangle bounds = new Rectangle(whereX, whereY, width, height);
				g.setColor(new Color(255, 255, 220));
				g.fill(bounds);

				g.setColor(new Color(255, 230, 20));
				g.draw(bounds);

				g.setFont(new Font("Arial", Font.BOLD, 10));

				for (int i = 0; i < sym.getLayerCount(); i++) {
					g.setColor(Color.black);
					g.drawString(i+1+":", whereX+5, height + whereY - ( (i*rowHeight) + 5 ));
					Rectangle rect = new Rectangle(whereX + 20,
							height + whereY - ((i+1)*rowHeight) + 3,
							width - 20,
							rowHeight - 6);
					try {
						sym.getLayer(i).drawInsideRectangle(g, null, rect,null);
					} catch (SymbolDrawingException e) {
						if (e.getType() == SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS) {
							try {
								SymbologyFactory.getWarningSymbol(
										SymbolDrawingException.STR_UNSUPPORTED_SET_OF_SETTINGS,
										"",
										SymbolDrawingException.UNSUPPORTED_SET_OF_SETTINGS).drawInsideRectangle(g, null, rect,null);
							} catch (SymbolDrawingException e1) {
								// IMPOSSIBLE TO REACH THIS
							}
						} else {
							// should be unreachable code
							throw new Error(PluginServices.getText(this, "symbol_shapetype_mismatch"));
						}
					}
				}

			}
		};
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
