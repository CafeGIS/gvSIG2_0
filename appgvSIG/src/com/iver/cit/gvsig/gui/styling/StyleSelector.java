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
* $Id: StyleSelector.java 28081 2009-04-21 13:33:58Z fdiaz $
* $Log$
* Revision 1.11  2007-08-21 09:32:53  jvidal
* javadoc
*
* Revision 1.10  2007/05/31 09:36:22  jaume
* *** empty log message ***
*
* Revision 1.9  2007/05/10 09:47:50  jaume
* *** empty log message ***
*
* Revision 1.8  2007/05/08 15:44:07  jaume
* *** empty log message ***
*
* Revision 1.7  2007/04/27 12:10:17  jaume
* *** empty log message ***
*
* Revision 1.6  2007/04/11 16:02:43  jaume
* file filter
*
* Revision 1.5  2007/04/05 16:08:34  jaume
* Styled labeling stuff
*
* Revision 1.4  2007/04/04 16:01:14  jaume
* *** empty log message ***
*
* Revision 1.2  2007/03/09 11:25:00  jaume
* Advanced symbology (start committing)
*
* Revision 1.1.2.4  2007/02/21 07:35:14  jaume
* *** empty log message ***
*
* Revision 1.1.2.3  2007/02/08 15:43:04  jaume
* some bug fixes in the editor and removed unnecessary imports
*
* Revision 1.1.2.2  2007/01/30 18:10:10  jaume
* start commiting labeling stuff
*
* Revision 1.1.2.1  2007/01/26 13:49:03  jaume
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.exolab.castor.xml.Marshaller;
import org.gvsig.fmap.mapcontext.rendering.symbols.CartographicSupport;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.ILabelStyle;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IStyle;
import org.gvsig.gui.beans.AcceptCancelPanel;
import org.gvsig.gui.beans.controls.dnd.JDnDList;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.JComboBoxUnits;
import com.iver.cit.gvsig.gui.panels.ImageSizePanel;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Creates a panel where the user can select a style for an object that allows
 * to manage this property.This panel will be similar to the symbol selector panel
 * and, on it, the user will have a previsualization of the style of objects
 * stored and posibilities to modify an existing one, to create a new one
 * and so on.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class StyleSelector extends SymbolSelector {
	private static final long serialVersionUID = -7476555713446755512L;
	private ImageSizePanel isp;

	public StyleSelector(IStyle style, int shapeType) {
		this(style, shapeType, new SelectorFilter() {
			public boolean accepts(Object obj) {
				return obj instanceof IStyle;
			}
		});
	}
	/**
	 * Constructor method
	 *
	 * @param style
	 * @param shapeType
	 * @param filter
	 */
	public StyleSelector(IStyle style, int shapeType, SelectorFilter filter) {
		super(null, shapeType, filter, false);

    	Preferences prefs = Preferences.userRoot().node( "gvsig.foldering" );
		rootDir = new File(prefs.get("SymbolStylesFolder", System.getProperty("user.home")+"/gvSIG/Styles"));
		if (!rootDir.exists())
			rootDir.mkdir();

		try {
			initialize(style);
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
		lblTitle.setText(PluginServices.getText(this, "label_styles"));
		treeRootName = PluginServices.getText(this, "style_library");

	}

	@Override
	protected void initialize(Object currentElement) throws ClassNotFoundException {
    	library = new StyleLibrary(rootDir);

    	this.setLayout(new BorderLayout());
    	this.setSize(400, 221);

    	this.add(getJNorthPanel(), BorderLayout.NORTH);
    	this.add(getJSplitPane(), BorderLayout.CENTER);
    	this.add(getJEastPanel(), BorderLayout.EAST);
    	ActionListener okAction = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			accepted = true;
    			PluginServices.getMDIManager().closeWindow(StyleSelector.this);
    		}
    	}, cancelAction = new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			setSymbol(null);
    			PluginServices.getMDIManager().closeWindow(StyleSelector.this);
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

	@Override
	public void setSymbol(Object style) {
		((StylePreviewer) jPanelPreview).setStyle((IStyle) style);
		updateOptionsPanel();
	}

	@Override
	public Object getSelectedObject() {
		if (!accepted) return null;
		Object mySelectedElement = ((StylePreviewer) jPanelPreview).getStyle();

		if (mySelectedElement instanceof CartographicSupport) {
			CartographicSupport csSym = (CartographicSupport) mySelectedElement;
			csSym.setUnit(cmbUnits.getSelectedUnitIndex());
			csSym.setReferenceSystem(cmbReferenceSystem.getSelectedIndex());
		}

		return mySelectedElement;
	}

	protected SymbolSelectorListModel newListModel() {
		StyleSelectorListModel listModel = new StyleSelectorListModel(
				dir,
//				selectedElement,
				sFilter,
				StyleSelectorListModel.STYLE_FILE_EXTENSION);
		return listModel;

	}

	protected JPanel getJPanelOptions() {
		if (jPanelOptions == null) {
			jPanelOptions = new GridBagLayoutPanel();
			jPanelOptions.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "options")));
			jPanelOptions.addComponent(getImageSizePanel());
			jPanelOptions.addComponent(PluginServices.getText(this, "units"),
					cmbUnits = new JComboBoxUnits(true));
			jPanelOptions.addComponent("",
					cmbReferenceSystem = new JComboBoxUnitsReferenceSystem());
		}
    	return jPanelOptions;
    }

	/**
	 *
     * This method initializes ImageSizePanel
     *
     * @return isp ImageSizePanel
     */
	 private ImageSizePanel getImageSizePanel() {
		 if (isp == null) {
			isp = new ImageSizePanel();
			isp.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ILabelStyle st = (ILabelStyle) getSelectedObject();
					if (st != null) {
						double[] sz = isp.getImageDimension();
						st.setSize(sz[0], sz[1]);
					}
				}
			});
		}

		return isp;
	}

	protected void updateOptionsPanel() {
		IStyle s = ((StylePreviewer) jPanelPreview).getStyle();
	    if (s instanceof ILabelStyle) {
			ILabelStyle lab = (ILabelStyle) s;
			Dimension sz = lab.getSize();
			getImageSizePanel().setImageSize(sz);
		}
	 }


    /**
     * This method initializes jList
     *
     * @return javax.swing.JList
     */
    protected JList getJListSymbols() {
    	if (jListSymbols == null) {
    		jListSymbols = new JDnDList();
    		jListSymbols.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jListSymbols.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            jListSymbols.setVisibleRowCount(-1);
            jListSymbols.addListSelectionListener(new ListSelectionListener() {
            	public void valueChanged(ListSelectionEvent e) {
            		setStyle(jListSymbols.getSelectedValue());
            		updateOptionsPanel();
            	}
            });
            ListCellRenderer renderer = new ListCellRenderer() {
        		private Color mySelectedBGColor = new Color(255,145,100,255);
    			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
    				IStyle sty = (IStyle) value;
    				JPanel pnl = new JPanel();
    				BoxLayout layout = new BoxLayout(pnl, BoxLayout.Y_AXIS);
    				pnl.setLayout(layout);
    				Color bgColor = (isSelected) ? mySelectedBGColor
    							 : getJListSymbols().getBackground();

    				pnl.setBackground(bgColor);
    				StylePreviewer sp = new StylePreviewer();
    				sp.setShowOutline(false);
    				sp.setAlignmentX(Component.CENTER_ALIGNMENT);
    				sp.setPreferredSize(new Dimension(50, 50));
    				sp.setStyle(sty);
    				sp.setBackground(bgColor);
    				pnl.add(sp);
    				JLabel lbl = new JLabel(sty.getDescription());
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
     * Modify the previsualization showed in the panel with the style of the
     * new object selected.
     *
     * @param selectedValue
     */
   protected void setStyle(Object selectedValue) {
		//selectedElement = selectedValue;
		((StylePreviewer) jPanelPreview).setStyle((IStyle) selectedValue);
		doLayout();
		repaint();
	}

	protected void propertiesPressed() {
		StyleEditor se = new StyleEditor((IStyle) getSelectedObject());
		PluginServices.getMDIManager().addWindow(se);
		setStyle(se.getStyle());
	}

	protected void savePressed() {
		if (getSelectedObject() ==null)
			return;

		JFileChooser jfc = new JFileChooser(rootDir);
		javax.swing.filechooser.FileFilter ff = new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				return f.getAbsolutePath().
				toLowerCase().
				endsWith(StyleSelectorListModel.STYLE_FILE_EXTENSION);
			}

			public String getDescription() {
				return PluginServices.getText(
						this, "gvSIG_style_definition_file")+ " ("+StyleSelectorListModel.STYLE_FILE_EXTENSION+")";
			}
		};
		jfc.setFileFilter(ff);
		JPanel accessory = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 5));
		accessory.add(new JLabel(PluginServices.getText(this, "enter_description")));
		JTextField txtDesc = new JTextField(25);
		txtDesc.setText(((IStyle) getSelectedObject()).getDescription());
		accessory.add(txtDesc);
		jfc.setAccessory(accessory);
		if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File targetFile = jfc.getSelectedFile();

			String fExtension = StyleSelectorListModel.STYLE_FILE_EXTENSION;

			// apply description
			String desc;
			if (txtDesc.getText()==null || txtDesc.getText().trim().equals("")) {
				// default to file name
				String s = targetFile.getAbsolutePath();
				desc = s.substring(s.lastIndexOf(File.separator)+1).replaceAll(fExtension, "");
			} else {
				desc = txtDesc.getText().trim();
			}
			IStyle s = (IStyle) getSelectedObject();
			s.setDescription(desc);

			// save it
			XMLEntity xml=null;
			try {
				xml = s.getXMLEntity();
			} catch (XMLException e) {
				NotificationManager.addWarning("Symbol layer", e);
			}
			if (!targetFile.
					getAbsolutePath().
					toLowerCase().
					endsWith(fExtension))
				targetFile = new File(targetFile.getAbsolutePath() + fExtension);
			if(targetFile.exists()){
				int resp = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),
						PluginServices.getText(this,
						"fichero_ya_existe_seguro_desea_guardarlo"),
						PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION) {
					return;
				}
			}
			FileWriter writer;
			try {
				writer = new FileWriter(targetFile.getAbsolutePath());
				Marshaller m = new Marshaller(writer);
				m.setEncoding("ISO-8859-1");
				m.marshal(xml.getXmlTag());

			} catch (Exception ex) {
				NotificationManager.addError(
						PluginServices.getText(this, "save_error"), ex);
			}
			getJListSymbols().setModel(newListModel());
		}
	}

    /**
     * This method initializes jPanelPreview
     *
     * @return javax.swing.JComponent
     */
    protected JComponent getJPanelPreview() {
    	if (jPanelPreview == null) {
    		jPanelPreview = new StylePreviewer();
    		jPanelPreview.setPreferredSize(new java.awt.Dimension(100,100));
    		jPanelPreview.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
    		((StylePreviewer) jPanelPreview).setShowOutline(true);
    	}
    	return jPanelPreview;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	if (!act) return;
		Object selectedElement = ((StylePreviewer) jPanelPreview).getStyle();//getSelectedObject();
		performActionOn(selectedElement, e);
		StyleSelector.this.repaint();
    }

    public int getUnit() {
    	return cmbUnits.getSelectedUnitIndex();
    }

    public int getReferenceSystem() {
    	return cmbReferenceSystem.getSelectedIndex();
    }

	public void setUnit(int unit) {
		cmbUnits.setSelectedUnitIndex(unit);
	}

	public void setReferenceSystem(int referenceSystem) {
		cmbReferenceSystem.setSelectedIndex(referenceSystem);
	}
}
