/*
 * Created on 26-abr-2005
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.IFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ILineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMultiLayerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ITextSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.AcceptCancelPanel;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.gui.beans.swing.JComboBoxFontSizes;
import org.gvsig.gui.beans.swing.JComboBoxFonts;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.JComboBoxUnits;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;
import com.iver.utiles.XMLException;

/**
 * Creates the panel where the user has the options to select a symbol.
 * Apart from the option to select one, the user will have a previsualization
 * of all the symbols stored and posibilities to modify an existing one, to create
 * a new symbol and so on.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class SymbolSelector extends JPanel implements ISymbolSelector, ActionListener {
	private static final long serialVersionUID = -6405660392303659551L;
	private static final GeometryManager geomManager = GeometryLocator.getGeometryManager();
	private static final Logger logger = LoggerFactory.getLogger(SymbolSelector.class);
	private JPanel jPanel = null;
	protected JScrollPane jScrollPane = null;
	private JScrollPane jScrollPane1 = null;
	private WindowInfo wi;
	private JSplitPane jSplitPane = null;
	protected AcceptCancelPanel okCancelPanel;
	private JPanel northPanel;
	private ColorChooserPanel jcc1;
	private ColorChooserPanel jcc2;
	private JIncrementalNumberField txtSize;
	private JIncrementalNumberField txtAngle;
	private JPanel jPanelButtons;
	private JButton btnProperties;
	private int shapeType;
	private JButton btnSaveSymbol;
	private JButton btnResetSymbol;
	private JButton btnNewSymbol;
	private JComboBoxFonts cmbFonts;
	private JToggleButton btnBold;
	private JToggleButton btnItalic;
	private JToggleButton btnUnderlined;
	protected JLabel lblTitle;
	protected File dir;
	protected File rootDir;
	protected JComponent jPanelPreview = null;
	protected GridBagLayoutPanel jPanelOptions = null;
	protected JList jListSymbols = null;
	protected String treeRootName;
	protected ILibraryModel library;
	private JIncrementalNumberField txtWidth;
	protected boolean act = true;
	boolean accepted = true;


	protected SelectorFilter sFilter = new SelectorFilter() {
		private Geometry dummyPointGeom = null;
		private Geometry dummyLineGeom = null;
		private Geometry dummyPolygonGeom = null;
		private boolean isInitialized = false;

		public boolean accepts(Object obj) {
			if (!isInitialized){
				try {
					dummyPointGeom = geomManager.createPoint(0, 0, SUBTYPES.GEOM2D);
					dummyLineGeom = geomManager.createSurface(new GeneralPathX(), SUBTYPES.GEOM2D);
					dummyPolygonGeom = geomManager.createSurface(new GeneralPathX(), SUBTYPES.GEOM2D);
				} catch (CreateGeometryException e) {
					logger.error("Error creating a geometry", e);
				}
				isInitialized = true;
			}

			if (obj instanceof ISymbol) {
				ISymbol sym = (ISymbol) obj;

				Geometry compareGeometry = null;
				switch (SymbolSelector.this.shapeType) {
				case Geometry.TYPES.TEXT:
					return sym instanceof ITextSymbol;
				case Geometry.TYPES.POINT:
					compareGeometry = dummyPointGeom;
					break;
				case Geometry.TYPES.CURVE:
					compareGeometry = dummyLineGeom;
					break;
				case Geometry.TYPES.SURFACE:
					compareGeometry = dummyPolygonGeom;
					break;
				}
				return sym.isSuitableFor(compareGeometry);
			}
			return false;
		}
	};
	protected JComboBoxUnits cmbUnits;
	protected JComboBoxUnitsReferenceSystem cmbReferenceSystem;
	private JComboBoxFontSizes cmbFontSize;
	protected LibraryBrowser libraryBrowser;
	/**
	 * Constructor method
	 *
	 * @param currentElement
	 * @param shapeType
	 */
	private SymbolSelector(Object currentElement, int shapeType, boolean initialize) {
		super();

		// TODO  09/08/07 check the currentElement type is suitable for the shapeType specified
		if (currentElement != null && currentElement instanceof ISymbol) {
			ISymbol sym = (ISymbol) currentElement;
			try {
				currentElement = SymbologyFactory.createSymbolFromXML(
						sym.getXMLEntity(), "");
			} catch (XMLException e) {
				NotificationManager.addWarning("Symbol layer", e);
			}
			String desc = sym.getDescription();
			//    		desc += " ("+PluginServices.getText(this, "current")+")";
			//    		((ISymbol)currentElement).setDescription(desc);
		}

		//    	 for symbols MULTIPOINT is the same than POINT
		if (shapeType == Geometry.TYPES.MULTIPOINT) {
			shapeType = Geometry.TYPES.POINT;
		}

		this.shapeType = shapeType;
		//    	Preferences prefs = Preferences.userRoot().node( "gvsig.foldering" );
		rootDir = new File(SymbologyFactory.SymbolLibraryPath);
		//				prefs.get("SymbolLibraryFolder", System.getProperty("user.home")+"/gvSIG/Symbols"));
		if (!rootDir.exists()) {
			rootDir.mkdir();
		}
		treeRootName = PluginServices.getText(this, "symbol_library");
		try {
			if (initialize) {
				initialize(currentElement);
			}
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}


	}

	/**
	 * Constructor method, it is <b>protected</b> by convenience to let StyleSelector
	 * to invoke it, but rigorously it should be <b>private</b>.
	 *
	 * @param symbol
	 * @param shapeType
	 * @param filter
	 */
	protected SymbolSelector(Object symbol, int shapeType, SelectorFilter filter, boolean initialize) {
		this(symbol, shapeType, initialize);
		sFilter = filter;
	}

	/**
	 * This method initializes this
	 * @param currentElement
	 * @throws ClassNotFoundException
	 *
	 */
	protected void initialize(Object currentElement) throws ClassNotFoundException {
		library = new SymbolLibrary(rootDir);

		this.setLayout(new BorderLayout());
		this.setSize(400, 221);

		this.add(getJNorthPanel(), BorderLayout.NORTH);
		this.add(getJSplitPane(), BorderLayout.CENTER);
		this.add(getJEastPanel(), BorderLayout.EAST);
		ActionListener okAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PluginServices.getMDIManager().closeWindow(SymbolSelector.this);
			}
		}, cancelAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accepted = false;

				setSymbol(null);
				PluginServices.getMDIManager().closeWindow(SymbolSelector.this);
			}
		};

		okCancelPanel = new AcceptCancelPanel();
		okCancelPanel.setOkButtonActionListener(okAction);
		okCancelPanel.setCancelButtonActionListener(cancelAction);

		this.add(okCancelPanel, BorderLayout.SOUTH);
		libraryBrowser.setSelectionRow(0);

		SillyDragNDropAction dndAction = new SillyDragNDropAction();
		libraryBrowser.addMouseListener(dndAction);
		libraryBrowser.addMouseMotionListener(dndAction);
		getJListSymbols().addMouseListener(dndAction);
		getJListSymbols().addMouseMotionListener(dndAction);
		setSymbol(currentElement);
	}

	/**
	 * Creates a new symbol selector list model in order to allow the user
	 * to select an existing symbol previously created.
	 *
	 * @return listModel SymbolSelectorListModel
	 */
	protected SymbolSelectorListModel newListModel() {
		SymbolSelectorListModel listModel = new SymbolSelectorListModel(
				dir,
				sFilter,
				SymbolLibrary.SYMBOL_FILE_EXTENSION);
		return listModel;
	}
	/**
	 * Initializes tha JNorthPanel.
	 *
	 * @return northPanel JPanel
	 * @throws IllegalArgumentException
	 */
	protected JPanel getJNorthPanel() throws IllegalArgumentException {
		if (northPanel == null) {
			String text = "";
			switch (shapeType) {
			case Geometry.TYPES.POINT:
				text = PluginServices.getText(this, "point_symbols");
				break;
			case Geometry.TYPES.CURVE:
				text = PluginServices.getText(this, "line_symbols");
				break;
			case Geometry.TYPES.SURFACE:
				text = PluginServices.getText(this, "polygon_symbols");
				break;
			case Geometry.TYPES.TEXT:
				text = PluginServices.getText(this, "text_symbols");
				break;
			default:
				throw new IllegalArgumentException(PluginServices.getText(this, "shape_type_not_yet_supported"));
			}
			northPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
			lblTitle = new JLabel(text);
			lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD));
			northPanel.add(lblTitle);
		}
		return northPanel;
	}

	/**
	 * This method initializes jList
	 *
	 * @return javax.swing.JList
	 */
	protected JList getJListSymbols() {
		if (jListSymbols == null) {
			jListSymbols = new JList() ;
			jListSymbols.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			jListSymbols.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			jListSymbols.setVisibleRowCount(-1);
			jListSymbols.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (jListSymbols.getSelectedValue()!=null) {
						ISymbol selSym=null;
						try {
							selSym = SymbologyFactory.createSymbolFromXML(
									((ISymbol) jListSymbols.getSelectedValue()).getXMLEntity(), null);
						} catch (XMLException e1) {
							NotificationManager.addWarning("Symbol layer", e1);
						}
						setSymbol(selSym);
						updateOptionsPanel();
					}
				}
			});
			ListCellRenderer renderer = new ListCellRenderer() {
				private Color mySelectedBGColor = new Color(255,145,100,255);
				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
					ISymbol sym = (ISymbol) value;
					JPanel pnl = new JPanel();
					BoxLayout layout = new BoxLayout(pnl, BoxLayout.Y_AXIS);
					pnl.setLayout(layout);
					Color bgColor = (isSelected) ? mySelectedBGColor
							: getJListSymbols().getBackground();

					pnl.setBackground(bgColor);
					SymbolPreviewer sp = new SymbolPreviewer();
					sp.setAlignmentX(Component.CENTER_ALIGNMENT);
					sp.setPreferredSize(new Dimension(50, 50));
					sp.setSymbol(sym);
					sp.setBackground(bgColor);
					pnl.add(sp);
					String desc = sym.getDescription();
					if (desc == null) {
						desc = "["+PluginServices.getText(this, "no_desc")+"]";
					}
					JLabel lbl = new JLabel(desc);
					lbl.setBackground(bgColor);
					lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
					pnl.add(lbl);

					return pnl;
				}

			};
			jListSymbols.setCellRenderer(renderer);
		}
		return jListSymbols;
	}
	/**
	 * Updates the options panel depending on the type of symbol that the user
	 * is controlling or using to show specific options for each one.
	 *
	 */
	protected void updateOptionsPanel() throws IllegalArgumentException {
		Object mySelectedElement = ((SymbolPreviewer) jPanelPreview).getSymbol();

//		if (mySelectedElement == null) {
//			return;
//		}
		act = false; // disable events

		if (mySelectedElement instanceof CartographicSupport) {
			CartographicSupport cs = (CartographicSupport) mySelectedElement;
			cmbUnits.setSelectedUnitIndex(cs.getUnit());
			cmbReferenceSystem.setSelectedIndex(cs.getReferenceSystem());
		}

		if (mySelectedElement instanceof IMultiLayerSymbol){
			if (((IMultiLayerSymbol)mySelectedElement).getLayerCount() == 1) {
				mySelectedElement = ((IMultiLayerSymbol)mySelectedElement).getLayer(0);
			}
		}

		try {

			jcc1.setEnabled(mySelectedElement!=null);
			jcc2.setEnabled(mySelectedElement!=null);

			if(mySelectedElement instanceof IMultiLayerSymbol){
				jcc1.setColor(Color.WHITE);
				jcc2.setColor(Color.WHITE);
				jcc1.setEnabled(false);
				jcc2.setEnabled(false);
			}

			if (shapeType == Geometry.TYPES.POINT) {
				IMarkerSymbol m = (IMarkerSymbol) mySelectedElement;
				txtSize.setEnabled(m!=null);
				txtAngle.setEnabled(m!=null);
				if(m!=null){
					jcc1.setColor(m.getColor());
					txtSize.setDouble(m.getSize());
					txtAngle.setDouble(Math.toDegrees(m.getRotation()));
				}
			}

			if (shapeType == Geometry.TYPES.CURVE) {
				ILineSymbol l = (ILineSymbol) mySelectedElement;
				txtSize.setEnabled(l!=null);
				if(l!=null){
					jcc1.setColor(l.getColor());
					jcc1.setAlpha(l.getAlpha());
					txtSize.setDouble(l.getLineWidth());
				}
			}

			if (shapeType == Geometry.TYPES.SURFACE) {
				IFillSymbol f = (IFillSymbol) mySelectedElement;

				txtWidth.setEnabled(f!=null);
				cmbReferenceSystem.setEnabled(f!=null);
				cmbUnits.setEnabled(f!=null);

				if (f!=null){
					jcc1.setUseColorIsSelected(f.hasFill());
					jcc1.setColor(f.getFillColor());
					jcc1.setAlpha(f.getFillAlpha());
					jcc2.setUseColorIsSelected(f.hasOutline());
					ILineSymbol outline = f.getOutline();
					if (outline != null) {
						jcc2.setColor(outline.getColor());
						txtWidth.setDouble(outline.getLineWidth());
					}

					if(f instanceof MultiLayerFillSymbol){
						txtWidth.setEnabled(false);
						cmbReferenceSystem.setEnabled(false);
						cmbUnits.setEnabled(false);
					}
				}
			}


			if (shapeType == Geometry.TYPES.TEXT) {
				ITextSymbol t = (ITextSymbol) mySelectedElement;
				cmbFontSize.setEnabled(t!=null);
				if(t!=null){
					jcc1.setColor(t.getTextColor());
					Double s = new Double(t.getFont().getSize());
					cmbFontSize.setSelectedItem(s);
					int i = cmbFontSize.getSelectedIndex();
					if (i == -1) {
						cmbFontSize.addItem(s);
						cmbFontSize.setSelectedItem(s);
					}
				}
			}
		} catch (NullPointerException npEx) {
			throw new IllegalArgumentException(npEx);
		} catch (ClassCastException ccEx) {
			throw new IllegalArgumentException(ccEx);
		}

		act = true;  // enable events
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	protected JPanel getJEastPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getJPanelOptions(), BorderLayout.CENTER);
			JPanel aux = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			aux.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "preview")));
			aux.add(getJPanelPreview());
			jPanel.add(aux, BorderLayout.NORTH);

			jPanel.add(getJPanelOptions());
			aux = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			aux.add(getJPanelButtons());
			jPanel.add(aux, BorderLayout.SOUTH);
		}
		return jPanel;
	}

	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridLayout layout = new GridLayout();
			layout.setColumns(1);
			layout.setVgap(5);
			jPanelButtons.add(getBtnNewSymbol());
			jPanelButtons.add(getBtnSaveSymbol());
			jPanelButtons.add(getBtnResetSymbol());
			jPanelButtons.add(getBtnProperties());

			// do not add components bellow this line!
			layout.setRows(jPanelButtons.getComponentCount());
			jPanelButtons.setLayout(layout);
		}
		return jPanelButtons;
	}

	private JButton getBtnNewSymbol() {
		if (btnNewSymbol == null) {
			btnNewSymbol = new JButton();
			btnNewSymbol.setName("btnNewSymbol");
			btnNewSymbol.setText(PluginServices.getText(this, "new"));
			btnNewSymbol.addActionListener(this);
		}
		return btnNewSymbol;
	}

	private JButton getBtnResetSymbol() {
		if (btnResetSymbol == null) {
			btnResetSymbol = new JButton();
			btnResetSymbol.setName("btnResetSymbol");
			btnResetSymbol.setText(PluginServices.getText(this, "reset"));
			btnResetSymbol.addActionListener(this);
		}
		return btnResetSymbol;
	}

	private JButton getBtnSaveSymbol() {
		if (btnSaveSymbol == null) {
			btnSaveSymbol = new JButton();
			btnSaveSymbol.setName("btnSaveSymbol");
			btnSaveSymbol.setText(PluginServices.getText(this, "save"));
			btnSaveSymbol.addActionListener(this);
		}
		return btnSaveSymbol;
	}

	private JButton getBtnProperties() {
		if (btnProperties == null) {
			btnProperties = new JButton();
			btnProperties.setName("btnProperties");
			btnProperties.setText(PluginServices.getText(this, "properties"));
			btnProperties.addActionListener(this);
		}
		return btnProperties;
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 * @throws ClassNotFoundException
	 */
	protected JScrollPane getLeftJScrollPane() throws ClassNotFoundException {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(80,130));
			jScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			libraryBrowser = new LibraryBrowser(library);
			libraryBrowser.addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
					dir = (File) ((DefaultMutableTreeNode)
							libraryBrowser.getLastSelectedPathComponent()).getUserObject();

					if (dir == null) {
						return;
					}

					jListSymbols.setModel(newListModel());
					//					jListSymbols.setSelectedValue(selectedElement, true);
				}
			});
			jScrollPane.setViewportView(libraryBrowser);
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJListSymbols());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jPanelPreview
	 *
	 * @return javax.swing.JComponent
	 */
	protected JComponent getJPanelPreview() {
		if (jPanelPreview == null) {
			jPanelPreview = new SymbolPreviewer();
			jPanelPreview.setPreferredSize(new java.awt.Dimension(100,100));
			jPanelPreview.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		}
		return jPanelPreview;
	}
	/**
	 * This method initializes jPanelOptions
	 *
	 * @return javax.swing.JPanel
	 */
	protected JPanel getJPanelOptions() {
		if (jPanelOptions == null) {
			jPanelOptions = new GridBagLayoutPanel();
			jPanelOptions.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "options")));
			jcc2 = new ColorChooserPanel(true,true);
			jcc2.setAlpha(255);
			if (shapeType == Geometry.TYPES.POINT) {
				jcc1 = new ColorChooserPanel(true);

				jPanelOptions.addComponent(
						PluginServices.getText(this, "color")+":", jcc1);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "size")+":",
						txtSize = new JIncrementalNumberField(String.valueOf(3), 3, 0, Double.MAX_VALUE, 1));
				jPanelOptions.addComponent(PluginServices.getText(this, "units")+":",
						cmbUnits = new JComboBoxUnits());
				jPanelOptions.addComponent("",
						cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());
				jPanelOptions.addComponent(
						PluginServices.getText(this, "angle")+ " (" +PluginServices.getText(this, "degree")+"):",
						txtAngle = new JIncrementalNumberField());


			} else if (shapeType == Geometry.TYPES.CURVE) {
				jcc1 = new ColorChooserPanel(true);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "color")+":", jcc1);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "width")+":",
						txtSize = new JIncrementalNumberField(String.valueOf(3), 3, 0, Double.MAX_VALUE, 1));
				jPanelOptions.addComponent(PluginServices.getText(this, "units")+":",
						cmbUnits = new JComboBoxUnits());
				jPanelOptions.addComponent("",
						cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());

			} else if (shapeType == Geometry.TYPES.SURFACE) {
				jcc1 = new ColorChooserPanel(true, true);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "fill_color")+":", jcc1);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "outline_color")+":", jcc2);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "outline_width"),
						txtWidth = new JIncrementalNumberField(String.valueOf(3), 3, 0, Double.MAX_VALUE, 1));
				jPanelOptions.addComponent(PluginServices.getText(this, "units")+":",
						cmbUnits = new JComboBoxUnits());
				jPanelOptions.addComponent("",
						cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());

			} else if (shapeType == Geometry.TYPES.TEXT) {
				jcc1 = new ColorChooserPanel(true);
				jPanelOptions.addComponent(
						PluginServices.getText(this, "font")+":", getCmbFonts());

				jPanelOptions.addComponent(
						PluginServices.getText(this, "color")+":", jcc1);
				jPanelOptions.addComponent(PluginServices.getText(this, "size")+":",
						cmbFontSize = new JComboBoxFontSizes());
				jPanelOptions.addComponent(PluginServices.getText(this, "units")+":",
						cmbUnits = new JComboBoxUnits());
				jPanelOptions.addComponent("",
						cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());

				JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEADING,0,1));
				aux.add(getBtnBold());
				aux.add(getBtnItalic());
				aux.add(getBtnUnderlined());
				jPanelOptions.addComponent(
						PluginServices.getText(this, "style")+":", aux);

			}

			jcc1.setAlpha(255);

			if (txtSize != null) {
				txtSize.addActionListener(this);
			}
			if (cmbUnits != null) {
				cmbUnits.addActionListener(this);
			}
			if (cmbReferenceSystem != null) {
				cmbReferenceSystem.addActionListener(this);
			}
			if (jcc1 != null) {
				jcc1.addActionListener(this);
			}
			if (jcc2 != null) {
				jcc2.addActionListener(this);
			}
			if (txtWidth != null) {
				txtWidth.addActionListener(this);
			}
			if (cmbFontSize != null) {
				cmbFontSize.addActionListener(this);
			}
			if (txtAngle != null) {
				txtAngle.addActionListener(this);
			}
		}
		return jPanelOptions;
	}

	private JToggleButton getBtnUnderlined() {
		if (btnUnderlined == null) {
			btnUnderlined = new JToggleButton(PluginServices.getIconTheme().
					get("underline-icon"));
		}
		return btnUnderlined;
	}

	private JToggleButton getBtnItalic() {
		if (btnItalic == null) {
			btnItalic = new JToggleButton(PluginServices.getIconTheme().
					get("italic-icon"));
		}
		return btnItalic;
	}

	private JToggleButton getBtnBold() {
		if (btnBold == null) {
			btnBold = new JToggleButton(PluginServices.getIconTheme().
					get("bold-icon"));
		}
		return btnBold;
	}


	private JComboBoxFonts getCmbFonts() {
		if (cmbFonts == null) {
			cmbFonts = new JComboBoxFonts();
		}
		return cmbFonts;
	}

	public WindowInfo getWindowInfo() {
		if (wi == null) {
			wi = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
			wi.setWidth(706);
			wi.setHeight(500);
			wi.setTitle(PluginServices.getText(this, "symbol_selector"));
		}
		return wi;
	}

	protected JSplitPane getJSplitPane() throws ClassNotFoundException {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(200);
			jSplitPane.setResizeWeight(0.4);
			jSplitPane.setLeftComponent(getLeftJScrollPane());
			jSplitPane.setRightComponent(getJScrollPane1());
		}
		return jSplitPane;
	}

	public Object getSelectedObject() {
		if (!accepted) {
			return null;
		}
		Object mySelectedElement = ((SymbolPreviewer) jPanelPreview).getSymbol();

		// if this symbol only has one layer, then no multilayer is needed
		if (mySelectedElement instanceof IMultiLayerSymbol) {
			if (((IMultiLayerSymbol) mySelectedElement).getLayerCount()==1) {
				return ((IMultiLayerSymbol) mySelectedElement).getLayer(0);
			}
		}

		if (mySelectedElement instanceof CartographicSupport) {
			CartographicSupport csSym = (CartographicSupport) mySelectedElement;
			csSym.setUnit(cmbUnits.getSelectedUnitIndex());
			csSym.setReferenceSystem(cmbReferenceSystem.getSelectedIndex());
		}

		return mySelectedElement;
	}

	public void setSymbol(Object symbol) {
		((SymbolPreviewer) jPanelPreview).setSymbol((ISymbol) symbol);
		updateOptionsPanel();
	}

	/**
	 * Invoked when the PROPERTIES button is pressed
	 */
	protected void propertiesPressed() {
		ISymbol mySelectedElement = ((SymbolPreviewer) jPanelPreview).getSymbol();
		if (mySelectedElement ==null) {
			return;
		}

		ISymbol clonedSymbol=null;
		try {
			clonedSymbol = SymbologyFactory.createSymbolFromXML(
					mySelectedElement.getXMLEntity(), null);
		} catch (XMLException e) {
			NotificationManager.addWarning("Symbol layer", e);
		}
		SymbolEditor se = new SymbolEditor(clonedSymbol, shapeType);
		PluginServices.getMDIManager().addWindow(se);

		ISymbol symbol = se.getSymbol();
		if (symbol instanceof IMultiLayerSymbol) {
			IMultiLayerSymbol mSym = (IMultiLayerSymbol) symbol;
			if (mSym.getLayerCount() == 1) {
				symbol =  mSym.getLayer(0);
			}
		}
		setSymbol(symbol);

	}

	/**
	 * Invoked when the NEW button is pressed
	 */
	protected void newPressed() {
		SymbolEditor se = new SymbolEditor(null, shapeType);
		PluginServices.getMDIManager().addWindow(se);
		setSymbol(se.getSymbol());
	}

	/**
	 * Invoked when the RESET button is pressed
	 */
	protected void resetPressed() {
		setSymbol(null);
	}

	/**
	 * Invoked when the SAVE button is pressed
	 */
	protected void savePressed() {
		if (getSelectedObject() ==null) {
			return;
		}


		JFileChooser jfc = new JFileChooser("SYMBOL_SELECTOR_FILECHOOSER", rootDir);
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.getAbsolutePath().
				toLowerCase().
				endsWith(SymbolLibrary.SYMBOL_FILE_EXTENSION) || f.isDirectory();
			}

			public String getDescription() {
				return PluginServices.getText(
						this, "gvSIG_symbol_definition_file")+ " (*.sym)";
			}
		};
		jfc.setFileFilter(ff);
		JPanel accessory = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		accessory.add(new JLabel(PluginServices.getText(this, "enter_description")));
		JTextField txtDesc = new JTextField(25);
		accessory.add(txtDesc);
		jfc.setAccessory(accessory);
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File targetFile = jfc.getSelectedFile();

			// apply description
			String desc;
			if (txtDesc.getText()==null || txtDesc.getText().trim().equals("")) {
				// default to file name
				String s = targetFile.getAbsolutePath();
				desc = s.substring(s.lastIndexOf(File.separator)+1).
				replaceAll(SymbolLibrary.SYMBOL_FILE_EXTENSION, "");
			} else {
				desc = txtDesc.getText().trim();
			}
			ISymbol s = (ISymbol) getSelectedObject();
			s.setDescription(desc);


			String symbolFileName = targetFile.getAbsolutePath().substring(
					targetFile.getAbsolutePath().lastIndexOf(File.separator)+1,
					targetFile.getAbsolutePath().length());
			File targetDir = new File(targetFile.getAbsolutePath().substring(
					0,
					targetFile.getAbsolutePath().lastIndexOf(File.separator)));
			library.addElement(s, symbolFileName , targetDir);
			getJListSymbols().setModel(newListModel());
		}
	}


	public void actionPerformed(ActionEvent e) {
		if (!act) {
			return;
		}
		Object selectedElement = ((SymbolPreviewer) jPanelPreview).getSymbol();//getSelectedObject();
		performActionOn(selectedElement, e);
		SymbolSelector.this.repaint();
	}

	protected void performActionOn(Object selectedElement, ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();

		if ( comp.equals(getBtnProperties()) ) {
			// properties pressed
			propertiesPressed();
		} else if ( comp.equals(getBtnNewSymbol()) ) {
			// new pressed
			newPressed();
		} else if ( comp.equals(getBtnResetSymbol()) ) {
			// reset pressed
			resetPressed();
		} else if ( comp.equals(getBtnSaveSymbol()) ) {
			// save pressed
			savePressed();
		} else if (comp.equals(jcc1)) {
			if (selectedElement == null) {
				return;
			}

			Color c = jcc1.getColor();

			if (selectedElement instanceof IMarkerSymbol) {
				IMarkerSymbol m = (IMarkerSymbol) selectedElement;
				if (m instanceof MultiLayerMarkerSymbol) {
					MultiLayerMarkerSymbol mm = (MultiLayerMarkerSymbol) m;
					mm.setAlpha(jcc1.getAlpha());
				} else {
					m.setColor(c);
				}
			}

			if (selectedElement instanceof ILineSymbol) {
				ILineSymbol l = (ILineSymbol) selectedElement;
				if (l instanceof MultiLayerLineSymbol) {
					MultiLayerLineSymbol ml = (MultiLayerLineSymbol) l;
					ml.setAlpha(jcc1.getAlpha());
				} else {
					l.setLineColor(c);
				}
			}

			if (selectedElement instanceof IFillSymbol) {
				IFillSymbol f = (IFillSymbol) selectedElement;

				f.setHasFill(jcc1.getUseColorisSelected());
				f.setFillColor(c);
			}

			if (selectedElement instanceof ITextSymbol) {
				ITextSymbol t = (ITextSymbol) selectedElement;
				t.setTextColor(c);
			}

		} else if (comp.equals(jcc2)) {
			if (selectedElement == null) {
				return;
			}
			Color c = jcc2.getColor();


			if (selectedElement instanceof IFillSymbol) {
				IFillSymbol f = (IFillSymbol) selectedElement;
				ILineSymbol outline = f.getOutline();
				f.setHasOutline(jcc2.getUseColorisSelected());

				if (outline!=null) {
					ILineSymbol l = outline;
					if (l instanceof MultiLayerLineSymbol && c != null) {
						MultiLayerLineSymbol ml = (MultiLayerLineSymbol) l;
						ml.setAlpha(c.getAlpha());
					} else {
						l.setLineColor(c);
					}
				}

			}
		} else if (comp.equals(txtSize)) {
			double s = txtSize.getDouble();

			if (selectedElement instanceof IMarkerSymbol) {
				IMarkerSymbol m = (IMarkerSymbol) selectedElement;
				m.setSize(s);
			}

			if (selectedElement instanceof ILineSymbol) {
				ILineSymbol l = (ILineSymbol) selectedElement;
				l.setLineWidth(s);
			}
		} else if (comp.equals(cmbUnits)) {
			if (selectedElement instanceof CartographicSupport) {
				CartographicSupport cs = (CartographicSupport) selectedElement;
				cs.setUnit(cmbUnits.getSelectedUnitIndex());
			}
		} else if (comp.equals(cmbReferenceSystem)) {
			if (selectedElement instanceof CartographicSupport) {
				CartographicSupport cs = (CartographicSupport) selectedElement;
				cs.setUnit(cmbReferenceSystem.getSelectedIndex());
			}
		} else if (comp.equals(txtWidth)) {
			double w = txtWidth.getDouble();
			if (selectedElement instanceof IFillSymbol) {
				IFillSymbol f = (IFillSymbol) selectedElement;
				ILineSymbol outline = f.getOutline();
				if (outline!=null) {
					outline.setLineWidth(w);
				}
			}
		} else if (comp.equals(cmbFontSize)) {
			double s = ((Integer) cmbFontSize.getSelectedItem()).doubleValue();
			if (selectedElement instanceof ITextSymbol) {
				ITextSymbol t = (ITextSymbol) selectedElement;
				t.setFontSize(s);
			}
		} else if (comp.equals(txtAngle)) {
			double a = Math.toRadians(txtAngle.getDouble());
			if (selectedElement instanceof IMarkerSymbol) {
				IMarkerSymbol m = (IMarkerSymbol) selectedElement;
				m.setRotation(a);
			}
		}
	}

	public static ISymbolSelector createSymbolSelector(Object currSymbol, int shapeType) {
		return createSymbolSelector(currSymbol, shapeType, null);
	}

	public static ISymbolSelector createSymbolSelector(Object currSymbol, int shapeType, SelectorFilter filter) {
		ISymbolSelector selector = null;

		// patch for backwards compatibility
		//		if (currSymbol instanceof FSymbol) {
		//			currSymbol = SymbologyFactory.deriveFSymbol((FSymbol) currSymbol);
		//		}

		if (filter==null) {
			selector = (shapeType == Geometry.TYPES.GEOMETRY) ?
					new MultiShapeSymbolSelector(currSymbol) :
						new SymbolSelector(currSymbol, shapeType, true);
		} else {
			selector = (shapeType == Geometry.TYPES.GEOMETRY) ?
					new MultiShapeSymbolSelector(currSymbol) :
						new SymbolSelector(currSymbol, shapeType, filter, true);
		}
		return selector;
	}

	class SillyDragNDropAction implements MouseListener, MouseMotionListener {
		private boolean doDrop = false;
		private Object selected;
		private File sourceFolder;

		public void mouseClicked(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }

		public void mousePressed(MouseEvent e) {
			if (e.getSource().equals(getJListSymbols())) {
				selected = getJListSymbols().getSelectedValue();
				doDrop = selected!=null;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) libraryBrowser.getLastSelectedPathComponent();
				if (node.getUserObject() instanceof File) {
					sourceFolder = (File) node.getUserObject();
				}
			}
			e.consume();
		}

		public void mouseReleased(MouseEvent e) {
			if (doDrop && e.getSource().equals(getJListSymbols())) {
				Point p = new Point(getJListSymbols().getLocation().x-e.getPoint().x, getJListSymbols().getLocation().y+e.getPoint().y);
				if (libraryBrowser.getBounds().contains(p)) {
					File destFolder = libraryBrowser.getElementBellow(p);
					if (destFolder != null) {
						ISymbol sym = (ISymbol) selected;
						int move = InputEvent.SHIFT_DOWN_MASK | InputEvent.BUTTON1_DOWN_MASK;
						//					    int copy = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK;

						library.addElement(sym, sym.getDescription(), destFolder);
						if ((e.getModifiers() & (move)) !=0) {
							library.removeElement(sym, sourceFolder);
						}

					}
					libraryBrowser.refresh();
				}

			}
			doDrop = false;
		}

		public void mouseDragged(MouseEvent e) {
			if (e.getSource().equals(getJListSymbols())) {

				Point p = new Point(getJListSymbols().getLocation().x-e.getPoint().x, getJListSymbols().getLocation().y+e.getPoint().y);
				if (libraryBrowser.getBounds().contains(p)) {
					libraryBrowser.setSelectedElementBellow(p);
				}
			}
		}

		public void mouseMoved(MouseEvent e) {

		}

	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}