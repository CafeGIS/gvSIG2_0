/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
* $Id: ViewPage.java 28999 2009-05-26 12:59:51Z cmartinez $
* $Log$
* Revision 1.27  2007-09-17 09:22:10  jaume
* view draw frame rate now customizable
*
* Revision 1.26  2007/05/09 16:08:14  jaume
* *** empty log message ***
*
* Revision 1.25  2007/03/06 16:35:02  caballero
* Exceptions
*
* Revision 1.24  2007/02/20 15:52:32  caballero
* *** empty log message ***
*
* Revision 1.23  2007/01/25 11:04:07  caballero
* comprobar que el factor no es cero
*
* Revision 1.22  2006/12/20 14:41:12  caballero
* Remodelado Layout
*
* Revision 1.21  2006/11/08 10:57:55  jaume
* remove unecessary imports
*
* Revision 1.20  2006/10/25 08:34:06  jmvivo
* LLamado al PluginServices.getText para las unidades de medida del los combo
*
* Revision 1.19  2006/10/04 07:23:31  jaume
* refactored ambiguous methods and field names and added some more features for preference pages
*
* Revision 1.18  2006/10/03 09:52:38  jaume
* restores to meters
*
* Revision 1.17  2006/10/03 09:19:12  jaume
* *** empty log message ***
*
* Revision 1.16  2006/10/03 07:26:08  jaume
* *** empty log message ***
*
* Revision 1.15  2006/10/02 15:30:29  jaume
* *** empty log message ***
*
* Revision 1.14  2006/10/02 13:52:34  jaume
* organize impots
*
* Revision 1.13  2006/10/02 13:38:23  jaume
* *** empty log message ***
*
* Revision 1.12  2006/10/02 11:49:23  jaume
* *** empty log message ***
*
* Revision 1.11  2006/09/28 12:04:21  jaume
* default selection color now configurable
*
* Revision 1.10  2006/09/25 10:17:15  caballero
* Projection
*
* Revision 1.9  2006/09/15 10:41:30  caballero
* extensibilidad de documentos
*
* Revision 1.8  2006/09/14 15:43:48  jaume
* *** empty log message ***
*
* Revision 1.7  2006/09/14 15:42:38  jaume
* *** empty log message ***
*
* Revision 1.6  2006/09/14 06:57:18  jaume
* *** empty log message ***
*
* Revision 1.5  2006/09/12 15:56:50  jaume
* Default Projection now customizable
*
* Revision 1.4  2006/08/29 07:21:08  cesar
* Rename com.iver.cit.gvsig.fmap.Fmap class to com.iver.cit.gvsig.fmap.MapContext
*
* Revision 1.3  2006/08/22 12:30:59  jaume
* *** empty log message ***
*
* Revision 1.2  2006/08/22 07:36:04  jaume
* *** empty log message ***
*
* Revision 1.1  2006/08/04 11:41:05  caballero
* poder especificar el zoom a aplicar en las vistas
*
* Revision 1.3  2006/07/31 10:02:31  jaume
* *** empty log message ***
*
* Revision 1.2  2006/06/13 07:43:08  fjp
* Ajustes sobre los cuadros de dialogos de preferencias
*
* Revision 1.1  2006/06/12 16:04:28  caballero
* Preferencias
*
* Revision 1.11  2006/06/06 10:26:31  jaume
* *** empty log message ***
*
* Revision 1.10  2006/06/05 17:07:17  jaume
* *** empty log message ***
*
* Revision 1.9  2006/06/05 17:00:44  jaume
* *** empty log message ***
*
* Revision 1.8  2006/06/05 16:57:59  jaume
* *** empty log message ***
*
* Revision 1.7  2006/06/05 14:45:06  jaume
* *** empty log message ***
*
* Revision 1.6  2006/06/05 11:00:09  jaume
* *** empty log message ***
*
* Revision 1.5  2006/06/05 10:39:02  jaume
* *** empty log message ***
*
* Revision 1.4  2006/06/05 10:13:40  jaume
* *** empty log message ***
*
* Revision 1.3  2006/06/05 10:06:08  jaume
* *** empty log message ***
*
* Revision 1.2  2006/06/05 09:51:56  jaume
* *** empty log message ***
*
* Revision 1.1  2006/06/02 10:50:18  jaume
* *** empty log message ***
*
*
*/
package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.panels.crs.ISelectCrsPanel;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.JComboBox;
/**
 *  View document configuration page.
 *  <b><b>
 *  Here the user can establish what settings wants to use by default regarding to
 *  the document View.
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ViewPage extends AbstractPreferencePage {

	private static final String DEFAULT_PROJECTION_KEY_NAME = "DefaultProjection";
	private static final String FACTORY_DEFAULT_PROJECTION = "EPSG:23030";
	private static final String ZOOM_IN_FACTOR_KEY_NAME = "ZoomInFactor";
	private static final String ZOOM_OUT_FACTOR_KEY_NAME = "ZoomOutFactor";
	private static final String ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME = "NewLayersInInvisibleMode";
	private static final String KEEP_SCALE_ON_RESIZING_KEY_NAME = "KeepScaleOnResizing";
	private static final String DEFAULT_SELECTION_COLOR_KEY_NAME = "DefaultSelectionColor";
	private static final String DEFAULT_VIEW_BACK_COLOR_KEY_NAME = "DefaultViewBackColor";
	private static final String DEFAULT_DISTANCE_AREA_KEY_NAME = "DefaultDistanceArea";
	private static final String DEFAULT_DISTANCE_UNITS_KEY_NAME = "DefaultDistanceUnits";
	private static final String DEFAULT_MAP_UNITS_KEY_NAME = "DefaultMapUnits";
	/*
	 * Locator's background color, for when we let it be possible
	 * private static final String DEFAULT_MAP_OVERVIEW_BACK_COLOR_KEY_NAME = "DefaultMapOverViewBackColor";
	 */
	private static final double DEFAULT_ZOOM_IN_FACTOR = 2.0;
	private static final double DEFAULT_ZOOM_OUT_FACTOR = 0.5;
	private static final Color FACTORY_DEFAULT_SELECTION_COLOR = Color.YELLOW;
	private static final Color FACTORY_DEFAULT_VIEW_BACK_COLOR = Color.WHITE;
	/*
	 * Locator's background color, for when we let it be possible
	 * private static final Color FACTORY_DEFAULT_MAP_OVERVIEW_BACK_COLOR = Color.WHITE;
	 */

	private static String[] unitsNames = null;

	private static int FACTORY_DEFAULT_MAP_UNITS;
	private static int FACTORY_DEFAULT_DISTANCE_UNITS;
	private static int FACTORY_DEFAULT_DISTANCE_AREA;

	private JTextField txtZoomInFactor;
	private JTextField txtZoomOutFactor;
	protected static String id = ViewPage.class.getName();
	private ImageIcon icon;
	private JLabel lblDefaultProjection;
	private JButton btnChangeProjection;
	private String fontName;
	private JCheckBox chkInvisibleNewLayers;
	private JCheckBox chkKeepScaleOnResizing;
	private ColorChooserPanel jccDefaultSelectionColor;
	private ColorChooserPanel jccDefaultViewBackColor;
	private JComboBox jCmbMapUnits;
	private JComboBox jCmbDistanceUnits;
	private JComboBox jCmbDistanceArea;
	
	CRSSelectPanel jPanelProj = null;
	
	
	/*
	 * Locator's background color, for when we let it be possible
	 * private ColorChooserPanel jccDefaultMapOverViewBackColor;
	 */

	{
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof FontUIResource) {
				FontUIResource fur = (FontUIResource) value;
				fontName = fur.getFontName();

			}
		}

		String[] unitNames = MapContext.getDistanceNames();
		for (int i = 0; i < unitNames.length; i++) {
			if (unitNames[i].equals("Metros")) {
				FACTORY_DEFAULT_DISTANCE_UNITS = i;
				FACTORY_DEFAULT_DISTANCE_AREA = i;
				FACTORY_DEFAULT_MAP_UNITS = i;
				break;
			}
		}

	}

	/**
	 * Creates a new panel containing View preferences settings.
	 *
	 */
	public ViewPage() {
		super();

		icon = PluginServices.getIconTheme().get("vista-icono");

		// default projection
		lblDefaultProjection = new JLabel();
		lblDefaultProjection.setFont(new java.awt.Font(
			fontName, java.awt.Font.BOLD, 11));
		btnChangeProjection = new JButton(PluginServices.getText(this, "change"));
		btnChangeProjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ISelectCrsPanel panel = CRSSelectPanel.getUIFactory().getSelectCrsPanel(
						CRSFactory.getCRS(lblDefaultProjection.getText()), false);
				PluginServices.getMDIManager().addWindow(panel);
		        if (panel.isOkPressed()) {
		        	IProjection proj = panel.getProjection();
		        	lblDefaultProjection.setText(proj.getAbrev());
		        }
			}
		});

		addComponent(PluginServices.getText(this, "default_projection") + ":", lblDefaultProjection );
		
		IProjection proj = CRSFactory.getCRS("EPSG:23030");
		if (PluginServices.getMainFrame() != null) {
			proj = AddLayerDialog.getLastProjection();
		}

		jPanelProj = CRSSelectPanel.getPanel(proj);
		jPanelProj.setTransPanelActive(true);
		jPanelProj.setBounds(11, 400, 448, 35);
		jPanelProj.setPreferredSize(new Dimension(448, 35));
		jPanelProj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jPanelProj.isOkPressed()) {
					lblDefaultProjection.setText(jPanelProj.getCurProj().getAbrev());
				}
			}
		});
		
		
		JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		aux.add(btnChangeProjection);
		addComponent("", aux);

		// just a separator
		addComponent(new JLabel(" "));

		// Adding Invisible New Layers
		chkInvisibleNewLayers = new JCheckBox(
				PluginServices.getText(this, "options.view.invisible_new_layers"));

		addComponent("", chkInvisibleNewLayers);
		chkKeepScaleOnResizing = new JCheckBox(
				PluginServices.getText(this, "options.view.keep_scale_on_resizing"));
		chkKeepScaleOnResizing.setEnabled(false);
		addComponent("", chkKeepScaleOnResizing);

		// just a separator
		addComponent(new JLabel(" "));

		// zoom in factor
		addComponent(PluginServices.getText(this, "zoom_in_factor") + ":",
			txtZoomInFactor = new JTextField("", 15));
		// zoom out factor
		addComponent(PluginServices.getText(this, "zoom_out_factor") + ":",
			txtZoomOutFactor = new JTextField("", 15));

		// just a separator
		addComponent(new JLabel(" "));

		// default back color chooser
		addComponent(PluginServices.getText(this, "options.view.default_view_back_color"),
				jccDefaultViewBackColor = new ColorChooserPanel());

		/*
		 * Locator's background color, for when we let it be possible
		 * // default back color chooser
		 * addComponent(PluginServices.getText(this, "options.view.default_map_overview_back_color"), jccDefaultMapOverViewBackColor = new ColorChooserPanel());
		 */

		// default selection color chooser
		addComponent(PluginServices.getText(this, "options.view.default_selection_color"),
				jccDefaultSelectionColor = new ColorChooserPanel());

		// just a separator
		addComponent(new JLabel(" "));

		// default map units
		addComponent(PluginServices.getText(this, "map_units"),
				jCmbMapUnits = new JComboBox());

		// default distance units
		addComponent(PluginServices.getText(this, "distance_units"),
				jCmbDistanceUnits = new JComboBox());

		// default distance area
		addComponent(PluginServices.getText(this, "distance_area"),
				jCmbDistanceArea = new JComboBox());

		initializeValues();
	}

	public void initializeValues() {
		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();

		// Default Projection
		if (xml.contains(DEFAULT_PROJECTION_KEY_NAME)) {
			lblDefaultProjection.setText(
				xml.getStringProperty(DEFAULT_PROJECTION_KEY_NAME));
		} else {
			lblDefaultProjection.setText(FACTORY_DEFAULT_PROJECTION);
		}
		Project.setDefaultProjection( CRSFactory.getCRS(lblDefaultProjection.getText()));

		// Adding invisible new layers
		if (xml.contains(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME)) {
			chkInvisibleNewLayers.setSelected(
				xml.getBooleanProperty(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME));
		}

		// Keep scale on resizing
		if (xml.contains(KEEP_SCALE_ON_RESIZING_KEY_NAME)) {
			chkKeepScaleOnResizing.setSelected(
				xml.getBooleanProperty(KEEP_SCALE_ON_RESIZING_KEY_NAME));
		}

		// Zoom-in factor
		if (xml.contains(ZOOM_IN_FACTOR_KEY_NAME)) {
			double zoomInFactor = xml.getDoubleProperty(ZOOM_IN_FACTOR_KEY_NAME);
			txtZoomInFactor.setText(String.valueOf( zoomInFactor ));
		} else {
			txtZoomInFactor.setText(
				String.valueOf( MapContext.ZOOMINFACTOR ));
		}
		MapContext.ZOOMINFACTOR = Double.parseDouble(txtZoomInFactor.getText());

		// Zoom-out factor
		if (xml.contains(ZOOM_OUT_FACTOR_KEY_NAME)) {
			double zoomOutFactor = xml.getDoubleProperty(ZOOM_OUT_FACTOR_KEY_NAME);
			txtZoomOutFactor.setText(String.valueOf( zoomOutFactor ));
		} else {
			txtZoomOutFactor.setText(
				String.valueOf( MapContext.ZOOMOUTFACTOR ));
		}
		MapContext.ZOOMOUTFACTOR = Double.parseDouble(txtZoomOutFactor.getText());;

		// Default back color
		jccDefaultViewBackColor.setColor(View.getDefaultBackColor());
		jccDefaultViewBackColor.setAlpha(255);
		/*
		 * Locator's background color, for when we let it be possible
		 * // Default map overview back color
		 * jccDefaultMapOverViewBackColor.setColor(Project.getDefaultMapOverViewBackColor());
		 * jccDefaultMapOverViewBackColor.setAlpha(255);
		 */

		// Default selection color
		jccDefaultSelectionColor.setColor(Project.getDefaultSelectionColor());
		jccDefaultSelectionColor.setAlpha(255);
		String[] distanceNames=MapContext.getDistanceNames();
		// Map units
		if (Project.getDefaultMapUnits()<=distanceNames.length && distanceNames.length != jCmbMapUnits.getItemCount()){
//			 default map units
			for (int i = 0; i < distanceNames.length; i++) {
				((DefaultComboBoxModel)jCmbMapUnits.getModel()).addElement(PluginServices.getText(this,distanceNames[i]));
			}
			jCmbMapUnits.setSelectedIndex(Project.getDefaultMapUnits());
		}

		// Distance units
		if (Project.getDefaultDistanceUnits()<=distanceNames.length&& distanceNames.length != jCmbDistanceUnits.getItemCount()){
			// default distance units
			for (int i = 0; i < distanceNames.length; i++) {
				((DefaultComboBoxModel)jCmbDistanceUnits.getModel()).addElement(PluginServices.getText(this,distanceNames[i]));
			}
			jCmbDistanceUnits.setSelectedIndex(Project.getDefaultDistanceUnits());
		}
		String[] names=MapContext.getAreaNames();

		// Distance area
		if (Project.getDefaultDistanceArea()<=names.length && names.length != jCmbDistanceArea.getItemCount()){
			for (int i = 0; i < names.length; i++) {

				((DefaultComboBoxModel)jCmbDistanceArea.getModel()).addElement(PluginServices.getText(this,names[i])+MapContext.getOfLinear(i));
			}
			jCmbDistanceArea.setSelectedIndex(Project.getDefaultDistanceArea());
		}


	}

	public String getID() {
		return id;
	}

	public String getTitle() {
		return PluginServices.getText(this, "Vista");
	}

	public JPanel getPanel() {
		return this;
	}

	public void storeValues() throws StoreException {
		String projName = lblDefaultProjection.getText();
		double zif=1;
		double zof=1;
		boolean invisibleNewLayers, keepScaleOnResize;
		Color selectionColor, viewBackColor;
		/*
		 * Locator's background color, for when we let it be possible
		 * Color mapOverViewBackColor;
		 */

		try {
//			try {
				zif=Double.parseDouble(txtZoomInFactor.getText());
				zof=Double.parseDouble(txtZoomOutFactor.getText());
				if (zif==0 || zof==0) {
					throw new NumberFormatException();
				}
//			}catch (NumberFormatException e) {
//				//JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),PluginServices.getText(this,"numero_incorrecto")+" = "+ txtZoomInFactor.getText()+ ", "+txtZoomOutFactor.getText());
//				throw new StoreException(PluginServices.getText(this,"numero_incorrecto")+" = "+ txtZoomInFactor.getText()+ ", "+txtZoomOutFactor.getText());
//			}
			Project.setDefaultProjection( CRSFactory.getCRS(projName));

			selectionColor = jccDefaultSelectionColor.getColor();
			Project.setDefaultSelectionColor( selectionColor );

			viewBackColor = jccDefaultViewBackColor.getColor();
			View.setDefaultBackColor( viewBackColor );
			/*
			 * Locator's background color, for when we let it be possible
			 * mapOverViewBackColor = jccDefaultMapOverViewBackColor.getColor();
			 * View.setDefaultMapOverViewBackColor( mapOverViewBackColor );
			 */

			Project.setDefaultMapUnits(jCmbMapUnits.getSelectedIndex());
			Project.setDefaultDistanceUnits(jCmbDistanceUnits.getSelectedIndex());
			Project.setDefaultDistanceArea(jCmbDistanceArea.getSelectedIndex());
			invisibleNewLayers = chkInvisibleNewLayers.isSelected();
			keepScaleOnResize = chkKeepScaleOnResizing.isSelected();
		} catch (Exception e) {
			throw new StoreException(PluginServices.getText(this,"factor_zoom_incorrecto"));
		}
		MapContext.ZOOMINFACTOR=zif;
		MapContext.ZOOMOUTFACTOR=zof;
		// MapControl.setScalingOnResize(!keepScaleOnResize);

		PluginServices ps = PluginServices.getPluginServices(this);
		XMLEntity xml = ps.getPersistentXML();
		xml.putProperty(DEFAULT_PROJECTION_KEY_NAME, projName);
		xml.putProperty(ZOOM_IN_FACTOR_KEY_NAME, zif);
		xml.putProperty(ZOOM_OUT_FACTOR_KEY_NAME, zof);
		xml.putProperty(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME, invisibleNewLayers);
		xml.putProperty(KEEP_SCALE_ON_RESIZING_KEY_NAME, keepScaleOnResize);
		xml.putProperty(DEFAULT_VIEW_BACK_COLOR_KEY_NAME, StringUtilities.color2String(viewBackColor));
		/*
		 * Locator's background color, for when we let it be possible
		 * xml.putProperty(DEFAULT_MAP_OVERVIEW_BACK_COLOR_KEY_NAME, StringUtilities.color2String(mapOverViewBackColor));
		 */
		xml.putProperty(DEFAULT_SELECTION_COLOR_KEY_NAME, StringUtilities.color2String(selectionColor));
		xml.putProperty(DEFAULT_MAP_UNITS_KEY_NAME, jCmbMapUnits.getSelectedIndex());
		xml.putProperty(DEFAULT_DISTANCE_UNITS_KEY_NAME, jCmbDistanceUnits.getSelectedIndex());
		xml.putProperty(DEFAULT_DISTANCE_AREA_KEY_NAME, jCmbDistanceArea.getSelectedIndex());
	}


	public void initializeDefaults() {
		lblDefaultProjection.setText(FACTORY_DEFAULT_PROJECTION);
		txtZoomInFactor.setText(String.valueOf(DEFAULT_ZOOM_IN_FACTOR));
		txtZoomOutFactor.setText(String.valueOf(DEFAULT_ZOOM_OUT_FACTOR));
		chkInvisibleNewLayers.setSelected(false);
		chkKeepScaleOnResizing.setSelected(false);
		jccDefaultViewBackColor.setColor(FACTORY_DEFAULT_VIEW_BACK_COLOR);
		/*
		 * Locator's background color, for when we let it be possible
		 * jccDefaultMapOverViewBackColor.setColor(FACTORY_DEFAULT_VIEW_BACK_COLOR);
		 */
		jccDefaultSelectionColor.setColor(FACTORY_DEFAULT_SELECTION_COLOR);
		jCmbMapUnits.setSelectedIndex(FACTORY_DEFAULT_MAP_UNITS);
		jCmbDistanceUnits.setSelectedIndex(FACTORY_DEFAULT_DISTANCE_UNITS);
		jCmbDistanceArea.setSelectedIndex(FACTORY_DEFAULT_DISTANCE_AREA);

	}

	public ImageIcon getIcon() {
		return icon;
	}

	public boolean isValueChanged() {
		return super.hasChanged();
	}

	public void setChangesApplied() {
		setChanged(false);
	}


//	private String[] getUnitsNames() {
//		if (unitsNames == null) {
//			String[] names=MapContext.getDistanceNames();
//			unitsNames = new String[names.length];
//			for (int i=0;i<names.length;i++) {
//				unitsNames[i]=PluginServices.getText(this, names[i]);
//			}
//		}
//		return unitsNames;
//	}
}
