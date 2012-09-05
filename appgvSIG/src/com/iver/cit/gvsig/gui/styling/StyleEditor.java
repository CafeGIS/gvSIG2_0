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

package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gvsig.fmap.mapcontext.rendering.symbols.SymbologyFactory;
import org.gvsig.fmap.mapcontext.rendering.symbols.styles.IStyle;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.XMLException;

/**
 * Implements the panel which is composed by a previsualization of the style for the label
 * and the different tools that the user has to modify that style in order to improve the
 * representation of the labelling in the layer. The different available tools and the preview
 * of the style change according with the geometry of the layer.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class StyleEditor extends JPanel implements IWindow, ActionListener {
	private static final long serialVersionUID = -4002620456610864510L;
	private JPanel pnlNorth = null;
	private JPanel jPanel = null;
	private JPanel pnlCenter = null;
	private AcceptCancelPanel pnlButtons = null;
	private JLabel lblTitle;
	private StylePreviewer preview;
	private JPanel pnlTools;
	private JTextField txtDesc;
	private Hashtable<AbstractButton, EditorTool> tools = new Hashtable<AbstractButton, EditorTool>();
	private static ArrayList<Class> installedTools = new ArrayList<Class>();
	private EditorTool prevTool;

	private ActionListener okAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			PluginServices.getMDIManager().closeWindow(StyleEditor.this);
		}
	};

	private ActionListener cancelAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			getStylePreviewer().setStyle(null);
			PluginServices.getMDIManager().closeWindow(StyleEditor.this);
		};
	};
	/**
	 * Constructor method
	 *
	 * @param style, an specific style for the labelling of the layer.This style depends on the
	 * geometry of the layer.
	 *
	 */
	public StyleEditor(IStyle style) {
		if (style!=null) {
			IStyle sty=null;
			try {
				sty = SymbologyFactory.createStyleFromXML(style.getXMLEntity(), style.getDescription());
			} catch (XMLException e) {
				NotificationManager.addWarning("Symbol layer", e);
			}
			getStylePreviewer().setStyle(sty);
			String desc = sty.getDescription();
			getTxtDesc().setText(desc);
		} else {
			getTxtDesc().setText(PluginServices.getText(this, "new_style"));
		}
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 */
	private void initialize() {
        this.setLayout(new BorderLayout());
        this.setSize(417,284);
        this.add(getPnlNorth(), java.awt.BorderLayout.NORTH);
        this.add(getPnlCenter(), java.awt.BorderLayout.CENTER);
        this.add(getPnlButtons(), java.awt.BorderLayout.SOUTH);

	}
	/**
	 * Sets the previewer of the style in the panel
	 *
	 * @param sty, style to be previewed
	 *
	 */
	public void setStyle(IStyle sty) {
		preview.setStyle(sty);
	}

	/**
	 * This method initializes the north JPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlNorth() {
		if (pnlNorth == null) {
			pnlNorth = new JPanel(new FlowLayout(FlowLayout.LEADING));
			lblTitle = new JLabel(PluginServices.getText(this, "editing"));
			pnlNorth.add(lblTitle);
			pnlNorth.add(getTxtDesc());
		}
		return pnlNorth;
	}

	/**
	 * This method initializes the textDesc JTextfield
	 *
	 * @return javax.swing.JTextfield
	 */
	private JTextField getTxtDesc() {
		if (txtDesc == null) {
			txtDesc = new JTextField(30);
			txtDesc.addActionListener(this);

		}

		return txtDesc;
	}

	/**
	 * This method initializes the center JPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlCenter() {
		if (pnlCenter == null) {
			pnlCenter = new JPanel(new BorderLayout());
			JPanel aux = new JPanel();
			aux.add(getStylePreviewer());
			pnlCenter.add(getStylePreviewer(), BorderLayout.CENTER);
			pnlCenter.add(getPnlTools(), BorderLayout.EAST);
		}
		return pnlCenter;
	}
	/**
	 * This method initializes the StylePreviewer
	 *
	 * @return StylePreviewer for the label
	 */
	private StylePreviewer getStylePreviewer() {
		if (preview == null) {
			preview = new StylePreviewer();
			preview.setShowOutline(true);
		}

		return preview;
	}

	/**
	 * This method initializes pnlButtons JPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlButtons() {
		if (pnlButtons == null) {
			pnlButtons = new AcceptCancelPanel(okAction, cancelAction);
		}
		return pnlButtons;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo wi = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
		wi.setTitle(PluginServices.getText(this, "edit_style"));
		wi.setWidth(getWidth()+10);
		wi.setHeight(getHeight());
		return wi;
	}

	/**
	 * Obtains the style for the label
	 *
	 * @return IStyle
	 */
	public IStyle getStyle() {
		IStyle style = getStylePreviewer().getStyle();
		if (style != null) {
			style.setDescription(txtDesc.getText());
		}
		return style;
	}

	public void actionPerformed(ActionEvent e) {
		AbstractButton srcButton = (AbstractButton) e.getSource();
		EditorTool currTool = tools.get(srcButton);
		if (currTool != null) {
			prevTool = preview.setEditorTool(currTool);
		}
	}
	/**
	 * This method initializes pnlTools JPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlTools() {
		if (pnlTools == null) {
			pnlTools = new JPanel();
			pnlTools.setBorder(BorderFactory.createTitledBorder(PluginServices.getText(this, "tools")));
			pnlTools.setLayout(new BoxLayout(pnlTools, BoxLayout.Y_AXIS));

			ArrayList<EditorTool> availableTools = new ArrayList<EditorTool>();
			IStyle sty = preview.getStyle();
			Class<?> editorClazz = null;
			Class[] constrLocator = new Class[] {JComponent.class};
			Object[] constrInitargs = new Object[] { this };
			try {
				for (Iterator<Class> iterator = installedTools.iterator(); iterator
				.hasNext();) {
					editorClazz = iterator.next();
					Constructor<EditorTool> constructor = (Constructor<EditorTool>) editorClazz.getConstructor(constrLocator);
					EditorTool editorTool = constructor.newInstance(constrInitargs);
					if (editorTool.isSuitableFor(sty)) {
						editorTool.setModel(sty);
						availableTools.add(editorTool);
					}

				}

				ButtonGroup group = new ButtonGroup();
				for (Iterator<EditorTool> iterator = availableTools.iterator();
				iterator.hasNext();) {
					EditorTool editorTool = iterator.next();
					AbstractButton button = editorTool.getButton();
					button.addActionListener(this);
					pnlTools.add(button);
					group.add(button);

					tools.put(button, editorTool);

				}
			} catch (Exception e) {
				NotificationManager.addWarning(PluginServices.getText(this, "could_not_initialize_editor_")+"'"+editorClazz+"'"+
						" ["+new Date(System.currentTimeMillis()).toString()+"]");
			}
		}
		return pnlTools;
	}
	/**
	 * Sets the tool to be used with the previous one
	 *
	 */
	public void restorePreviousTool() {
		preview.setEditorTool(prevTool);
	}
	/**
	 * Adds a new tool for the style
	 *
	 * @param styleEditorClass, the new tool to be added
	 */
	public static void addEditorTool(Class styleEditorClass) {
		installedTools.add(styleEditorClass);
	}
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
