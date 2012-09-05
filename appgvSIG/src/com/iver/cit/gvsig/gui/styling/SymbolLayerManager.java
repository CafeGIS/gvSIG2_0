/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

/* CVS MESSAGES:
*
* $Id: SymbolLayerManager.java 23176 2008-09-05 10:54:21Z vcaballero $
* $Log$
* Revision 1.8  2007-09-17 09:21:45  jaume
* refactored SymboSelector (added support for multishapedsymbol)
*
* Revision 1.7  2007/08/09 10:39:04  jaume
* first round of found bugs fixed
*
* Revision 1.6  2007/06/29 13:07:33  jaume
* +PictureLineSymbol
*
* Revision 1.5  2007/05/31 09:36:22  jaume
* *** empty log message ***
*
* Revision 1.4  2007/04/20 07:54:38  jaume
* *** empty log message ***
*
* Revision 1.3  2007/03/09 11:25:00  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.4  2007/02/21 07:35:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2007/02/08 15:45:37  jaume
* *** empty log message ***
*
* Revision 1.1.2.2  2007/02/08 15:43:04  jaume
* some bug fixes in the editor and removed unnecessary imports
*
* Revision 1.1.2.1  2007/01/26 13:49:03  jaume
* *** empty log message ***
*
* Revision 1.1  2007/01/16 11:52:11  jaume
* *** empty log message ***
*
* Revision 1.7  2007/01/10 17:05:05  jaume
* moved to FMap and gvSIG
*
* Revision 1.6  2006/11/08 10:56:47  jaume
* *** empty log message ***
*
* Revision 1.5  2006/11/07 08:52:30  jaume
* *** empty log message ***
*
* Revision 1.4  2006/11/06 17:08:45  jaume
* *** empty log message ***
*
* Revision 1.3  2006/11/06 16:06:52  jaume
* *** empty log message ***
*
* Revision 1.2  2006/11/06 07:33:54  jaume
* javadoc, source style
*
* Revision 1.1  2006/11/02 17:19:28  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.rendering.symbols.IMultiLayerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerFillSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerLineSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.MultiLayerMarkerSymbol;
import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.project.documents.view.legend.gui.ISymbolSelector;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * A component linked to a SymbolEditor that shows the layers
 *
 * @author jaume
 *
 */
public class SymbolLayerManager extends JPanel implements ActionListener{
   private static final long serialVersionUID = -1939951243066481691L;
	private static final Dimension LIST_CELL_SIZE = new Dimension(150,37);
	private SymbolEditor owner;
    private JList jListLayers;
    private ISymbol activeLayer;
    private IMultiLayerSymbol symbol;
    private JButton btnAddLayer = null;
    private JPanel pnlButtons = null;
    private JButton btnRemoveLayer = null;
    private JButton btnMoveUpLayer = null;
    private JButton btnMoveDownLayer = null;
    private final Dimension btnDimension = new Dimension(24, 24);
    private JScrollPane scroll;
    private Color cellSelectedBGColor = Color.BLUE;


    public SymbolLayerManager(SymbolEditor owner) {
        this.owner = owner;
        this.symbol = (IMultiLayerSymbol) owner.getSymbol();
        if (symbol.getLayerCount()==0) {
        	int shapeType = -1;
        	if (symbol instanceof MultiLayerMarkerSymbol) {
        		shapeType = Geometry.TYPES.POINT;
        	} else if (symbol instanceof MultiLayerLineSymbol) {
        		shapeType = Geometry.TYPES.CURVE;
			} else if (symbol instanceof MultiLayerFillSymbol) {
        		shapeType = Geometry.TYPES.SURFACE;
			}

        	if (shapeType != -1)
			symbol.addLayer(SymbologyFactory.
					createDefaultSymbolByShapeType(shapeType));
   		}
        initialize();

    }

    private void initialize() {
        this.setLayout(new BorderLayout());
        this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        this.add(getPnlButtons(), java.awt.BorderLayout.SOUTH);
        getJListLayers().setSelectedIndex(0);
    }

    private JScrollPane getJScrollPane() {
        if (scroll ==  null) {
            scroll = new JScrollPane();
            scroll.setPreferredSize(new Dimension(150, 160));
            scroll.setViewportView(getJListLayers());
        }
        return scroll;
    }

    private JList getJListLayers() {
        if (jListLayers == null) {
            jListLayers = new JList();
            jListLayers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jListLayers.setLayoutOrientation(JList.HORIZONTAL_WRAP);

            jListLayers.setVisibleRowCount(-1);
            jListLayers.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    setLayer((ISymbol) jListLayers.getSelectedValue());
                }
            });
            jListLayers.setModel(new SymbolLayerListModel());
            jListLayers.setCellRenderer(new DefaultListCellRenderer() {
              	private static final long serialVersionUID = -2551357351239544039L;

				public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                	return new SymbolLayerComponent((ISymbol) value, index, isSelected);
                }
            });
            jListLayers.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
						ISymbolSelector symSel = SymbolSelector.createSymbolSelector(activeLayer, owner.getShapeType());
						PluginServices.getMDIManager().addWindow(symSel);
						updateSymbol((ISymbol) symSel.getSelectedObject());
						setLayer((ISymbol) symSel.getSelectedObject());
					}

				}
            });
        }
        return jListLayers;
    }

    private void updateSymbol(ISymbol layer) {
    	if (layer != null) {
    		int index = getJListLayers().getSelectedIndex();
    		symbol.setLayer(index, layer);
    		owner.refresh();
    		validate();
    	}
    }

    private void setLayer(ISymbol symbol) {
    	if (symbol!=null) {
    		activeLayer = symbol;
    		owner.setOptionsPageFor(symbol);
    	}
    }

    private JButton getBtnAddLayer() {
        if (btnAddLayer == null) {
        	btnAddLayer = new JButton(PluginServices.getIconTheme().get("add-layer-icono"));
            btnAddLayer.setActionCommand("ADD");
            btnAddLayer.setSize(btnDimension);
            btnAddLayer.setPreferredSize(btnDimension);
            btnAddLayer.addActionListener(this);
        }
        return btnAddLayer;
    }

    private JPanel getPnlButtons() {
        if (pnlButtons == null) {
            pnlButtons = new JPanel();
            pnlButtons.add(getBtnAddLayer(), null);
            pnlButtons.add(getBtnRemoveLayer(), null);
            pnlButtons.add(getBtnMoveUpLayer(), null);
            pnlButtons.add(getBtnMoveDown(), null);
        }
        return pnlButtons;
    }

    private JButton getBtnRemoveLayer() {
        if (btnRemoveLayer == null) {
            btnRemoveLayer = new JButton(PluginServices.getIconTheme().get("delete-icono"));
            btnRemoveLayer.setActionCommand("REMOVE");
            btnRemoveLayer.setSize(btnDimension);
            btnRemoveLayer.setPreferredSize(btnDimension);
            btnRemoveLayer.addActionListener(this);
        }
        return btnRemoveLayer;
    }

    private JButton getBtnMoveUpLayer() {
        if (btnMoveUpLayer == null) {
            btnMoveUpLayer = new JButton(PluginServices.getIconTheme().get("arrow-up-icono"));
            btnMoveUpLayer.setActionCommand("MOVE_UP");
            btnMoveUpLayer.setSize(btnDimension);
            btnMoveUpLayer.setPreferredSize(btnDimension);
            btnMoveUpLayer.addActionListener(this);
        }
        return btnMoveUpLayer;
    }

    private JButton getBtnMoveDown() {
        if (btnMoveDownLayer == null) {
            btnMoveDownLayer = new JButton(PluginServices.getIconTheme().get("arrow-down-icono"));
            btnMoveDownLayer.setActionCommand("MOVE_DOWN");
            btnMoveDownLayer.setSize(btnDimension);
            btnMoveDownLayer.setPreferredSize(btnDimension);
            btnMoveDownLayer.addActionListener(this);
        }
        return btnMoveDownLayer;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        int jlistIndex = getJListLayers().getSelectedIndex();
        int index = symbol.getLayerCount() - 1 - jlistIndex;

        if (command.equals("ADD")) {
        	symbol.addLayer(owner.getNewLayer());
            index = symbol.getLayerCount();
        } else if (command.equals("REMOVE")) {
        	int layerCount = symbol.getLayerCount();
        	if (layerCount >1 && index < layerCount)
        		symbol.removeLayer(symbol.getLayer(index));
        } else if (command.equals("MOVE_UP")) {
            if (index < symbol.getLayerCount()-1) {
            	symbol.swapLayers(index, index+1);
                jlistIndex--;
            }
        } else if (command.equals("MOVE_DOWN")) {
            if (index > 0) {
            	symbol.swapLayers(index, index-1);
                jlistIndex++;
            }
        } else if (command.equals("LOCK")) {
            try {
                XMLEntity layerXML = symbol.getLayer(index).getXMLEntity();
                boolean locked = !layerXML.contains("locked") || !layerXML.getBooleanProperty("locked");
                layerXML.putProperty("locked", locked);
            } catch (Exception ex) {
                // Index out of bounds or so, we don't care
            }
        }
        owner.refresh();
        validate();
        getJListLayers().setSelectedIndex(jlistIndex);

    }

    private class SymbolLayerListModel extends AbstractListModel {
    	private static final long serialVersionUID = 4197818146836802855L;
		private ArrayList listeners = new ArrayList();

        public int getSize() {
            return symbol.getLayerCount();
        }

        public Object getElementAt(int index) {
        	return symbol.getLayer(symbol.getLayerCount()-1-index);
        }

        public void addListDataListener(ListDataListener l) {
        	listeners.add(l);
        }

        public void removeListDataListener(ListDataListener l) {
        	listeners.remove(l);
        }

    }

	public ISymbol getSelectedLayer() {
		return (ISymbol) getJListLayers().getSelectedValue();
	}

	public int getSelectedLayerIndex() {
		return getJListLayers().getSelectedIndex();
	}

	public void validate() { // patched to force the list to be painted when it starts empty
		jListLayers = null;
		getJScrollPane().setViewportView(getJListLayers());
		super.validate();
	}

	private class SymbolLayerComponent extends JPanel {
		private static final long serialVersionUID = -3706313315505454031L;

		public SymbolLayerComponent(ISymbol sym, int index, boolean isSelected) {
			 LayoutManager layout = new FlowLayout(FlowLayout.LEFT, 3, 3);
             setLayout(layout);
             Color bgColor = (isSelected) ? cellSelectedBGColor
                      : Color.WHITE;
             setBackground(bgColor);
             JCheckBox chkVisible = new JCheckBox("", symbol.getLayer(index).isShapeVisible());
             chkVisible.setBackground(bgColor);
             add(chkVisible);
             SymbolPreviewer sp = new SymbolPreviewer();
             sp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
             sp.setAlignmentX(Component.LEFT_ALIGNMENT);
             sp.setPreferredSize(new Dimension(80, 30));
             sp.setSize(new Dimension(80, 30));
             sp.setSymbol(sym);
             sp.setBackground(Color.WHITE);
             add(sp);
             XMLEntity xml=null;
			try {
				xml = sym.getXMLEntity();
			} catch (XMLException e) {
				NotificationManager.addWarning("Symbol layer", e);
			}
             String source;
             if (xml.contains("locked"))
             	source = xml.getBooleanProperty("locked") ?
             			"images/locked.png" :
             				"images/unlocked.png";
             else
             	source = "images/unlocked.png";

             ImageIcon icon = new ImageIcon(getClass().
             		getClassLoader().getResource(source));
             JButton btnLock = new JButton(icon);
             btnLock.setSize(btnDimension);
             btnLock.setPreferredSize(btnDimension);
             btnLock.setActionCommand("LOCK");
             btnLock.setBackground(bgColor);
             btnLock.setAlignmentX(Component.CENTER_ALIGNMENT);
             add(btnLock);
             setPreferredSize(LIST_CELL_SIZE);

		}
	}
}


