package com.iver.cit.gvsig.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateGeometryException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.primitive.GeneralPathX;
import org.gvsig.fmap.geom.primitive.Surface;
import org.gvsig.fmap.mapcontext.ExtentHistory;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.ViewPort;
import org.gvsig.fmap.mapcontext.events.ColorEvent;
import org.gvsig.fmap.mapcontext.events.ExtentEvent;
import org.gvsig.fmap.mapcontext.events.ProjectionEvent;
import org.gvsig.fmap.mapcontext.events.listeners.ViewPortListener;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.ZoomOutRightButtonListener;
import org.gvsig.fmap.mapcontrol.tools.Behavior.Behavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MouseMovementBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.MoveBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PointBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PolygonBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.PolylineBehavior;
import org.gvsig.fmap.mapcontrol.tools.Behavior.RectangleBehavior;
import org.gvsig.gui.beans.panelGroup.IPanelGroup;
import org.gvsig.gui.beans.specificcaretposition.JTextFieldWithSCP;
import org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems.ImageIconItemInfo;
import org.gvsig.gui.beans.swing.jComboBoxWithImageIconItems.JComboBoxWithImageIconItems;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toolListeners.AreaListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.MeasureListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.PanListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.ZoomInListener;
import com.iver.cit.gvsig.project.documents.view.toolListeners.ZoomOutListener;
import com.iver.utiles.stringNumberUtilities.StringNumberUtilities;

/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
/* CVS MESSAGES:
 *
 * $Id: WFSAreaPanel.java 24625 2008-11-03 08:21:18Z vcaballero $
 * $Log$
 * Revision 1.29  2007-05-28 07:00:05  jcampos
 * Fixed changes to 3D extension
 *
 * Revision 1.28  2007/04/11 12:04:58  ppiqueras
 * Bug corregido: al pulsar "Aplicar" en el wizard de WFS, despuï¿½s de haber seleccionado un ï¿½rea distinta, se debe actualizar el ï¿½rea visible del wizard con la de la vista activa.
 *
 * Revision 1.27  2007/03/15 13:32:44  ppiqueras
 * -Corregido bug de excepciï¿½n que se lanzaba cuando se filtraba y no se podï¿½a cargar la capa.
 * - Mejorada la interfaz
 *
 * Revision 1.26  2007/03/06 18:01:03  caballero
 * Exceptions
 *
 * Revision 1.25  2007/03/05 13:47:42  ppiqueras
 * Eliminadas lï¿½neas que sobraban.
 *
 * Revision 1.24  2007/03/01 13:13:58  ppiqueras
 * Aï¿½adido mï¿½todo para que si no se ha modificado el ï¿½rea que no se aplique filtro por ï¿½rea. Y mejorado el mï¿½todo getExtent() (obtenciï¿½n del Rectangle2D que define el ï¿½rea: si el usuario no ha seleccionado un ï¿½rea distinta, devolverï¿½ null).
 *
 * Revision 1.23  2007/02/26 13:40:28  ppiqueras
 * - Eliminado botï¿½n zoom siguiente
 * - En caso de que no haya vista cargada, que los botones de escalar y desplazar estï¿½n deshabilitados
 *
 * Revision 1.22  2007/02/26 12:58:09  ppiqueras
 * En caso de escribir una coordenada con formato incorrecto, avisa al usuario y restaura el valor anterior.
 *
 * Revision 1.21  2007/02/22 13:17:57  ppiqueras
 * Mejor documentado el cï¿½digo.
 *
 * Revision 1.20  2007/02/22 12:23:32  ppiqueras
 * Varias mejoras:
 * - Aviso de error en caso de intentar cargar capa con ï¿½rea mal indicada.
 * - Obtenciï¿½n del ï¿½rea de las coordenadas.
 * - Algï¿½n bug corregido.
 *
 * Revision 1.19  2007/02/20 13:13:26  ppiqueras
 * Se habilita el botï¿½n aplicar del panel padre en caso de algï¿½n cambio de coordendas, y mejora en la obtenciï¿½n del ï¿½rea del usuario.
 *
 * Revision 1.18  2007/02/20 11:31:11  ppiqueras
 * Eliminados comentarios que sobraban.
 *
 * Revision 1.17  2007/02/16 13:36:53  ppiqueras
 * Que el ï¿½rea seleccionada en el panel WFSArea sea accesible una vez se va a aplicar.
 *
 * Revision 1.16  2007/02/16 13:14:21  ppiqueras
 * Mejorada la selecciï¿½n por coordenadas absolutas  y corregido algï¿½n bug.
 *
 * Revision 1.15  2007/02/15 11:36:57  ppiqueras
 * Mejorada la interfaz: nuevas herramientas, mejoradas las de coordenadas absolutas. Y corregido algï¿½n bug de interacciï¿½n con otros paneles.
 *
 * Revision 1.14  2007/02/12 11:29:37  ppiqueras
 * Mejoras de la interfaz y uso de las herramientas
 *
 * Revision 1.13  2007/02/09 14:22:07  ppiqueras
 * Mejoras de funcionalidad
 *
 * Revision 1.12  2007/02/09 13:56:56  ppiqueras
 * Mejorada la funcionalidad de la interfaz: ya hay varias herramientas que se pueden usar.
 *
 * Revision 1.11  2007/02/08 14:18:57  ppiqueras
 * Carga de la vista actual.
 *
 * Revision 1.10  2007/02/05 13:49:26  ppiqueras
 * Mejorada la interfaz y aï¿½adidos componentes de tipo lista desplegable con iconos como ï¿½tems.
 *
 * Revision 1.9  2007/02/02 12:22:03  ppiqueras
 * Mejorada la interfaz.
 *
 * Revision 1.8  2007/01/26 14:23:56  ppiqueras
 * Mejorada la interfaz
 *
 * Revision 1.7  2007/01/25 14:37:16  ppiqueras
 * Utiliza JTextFieldWithSpecificCaretPosition
 *
 * Revision 1.6  2007/01/25 09:31:09  ppiqueras
 * Mejorada la interfaz.
 *
 * Revision 1.5  2007/01/19 14:24:30  ppiqueras
 * + Avanzada.
 *
 * Revision 1.4  2007/01/12 13:09:41  jorpiell
 * added searches by area
 *
 * Revision 1.3  2007/01/10 09:01:25  jorpiell
 * The coordinates panel is opened with the view coordinates
 *
 * Revision 1.2  2006/12/22 11:45:53  jorpiell
 * Actualizado el driver del WFS
 *
 * Revision 1.1  2006/10/10 12:55:06  jorpiell
 * Se ha aï¿½adido el soporte de features complejas
 *
 *
 */


/**
 * VERSIÓN PROVISIONAL ESTABLE
 *
 * This panel allows user to select the area he/she wants to get in the view.
 * There are two options to do this:
 *  - Indicating the coordinates of the top-left and down-right corners
 *  - Selecting the area with some visual tool
 *
 * @author Pablo Piqueras Bartolomé (p_queras@hotmail.com)
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSAreaPanel extends AbstractWFSPanel {
	private final Rectangle DEFAULT_BOUNDS = new Rectangle(10, 5, 490, 380);
	private final Rectangle DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE = new Rectangle(8, 20, 481, 60);
	private final Rectangle DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE = new Rectangle(8, 115, 481, 265);
	private final Rectangle DEFAULT_UNDO_ICON_BUTTON_RECTANGLE = new Rectangle (250, 85, 25, 25);
//	private final Rectangle DEFAULT_REDO_ICON_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
//	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
//	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (331, 85, 25, 25);
	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
	private final Rectangle DEFAULT_PAN_ICON_BUTTON_RECTANGLE = new Rectangle(250, 85, 25, 25);
	private final Rectangle DEFAULT_TOOL_JCOMBOBOX_RECTANGLE = new Rectangle(10, 87, 230, 21);
	private final Rectangle DEFAULT_ENABLING_CHECKBOX_RECTANGLE = new Rectangle(10, 87, 150, 21);
	private final Rectangle DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE = new Rectangle(277, 85, 47, 25);
	private final Rectangle DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE = new Rectangle(326, 85, 25, 25);
	private final Rectangle DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE = new Rectangle(353, 85, 25, 25);
	private final Rectangle DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE = new Rectangle(380, 85, 47, 25);
	private final Rectangle DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE = new Rectangle(429, 85, 47, 25);

	private static GeometryManager geometryManager = GeometryLocator.getGeometryManager();
	private final int DISABLED_OPERATION = 0;
	private final int SELECTION_BY_COORDINATES_OPERATION = 1;
	private final int SELECTION_BY_AREA_OPERATION = 2;
	private boolean hasUserDefinedAnArea = false;
//	private Rectangle2D lastExtentValid = null;
	private ExtentHistory previousExtentValids = null;

	private WFSServerExplorer serverExplorer = null;
	private AreaCoordinatesPanel coordinatesPanel = null;
	private SelectableMapControlAreaPanel selectableMapAreaPanel = null;
	private JComboBox jComboBoxToolSelection = null;
	private JButton jButtonUndo = null;
//	private JButton jButtonRedo = null;
	private JToggleButton jToggleButtonMove = null;
	private JToggleButton jToggleButtonScaling = null;
	private JButton jButtonPan = null;
	private JButton jButtonZoomPrevious = null;
	private JButton jButtonZoomComplete = null;
	private JComboBoxWithImageIconItems jComboBoxZoomInAndOut = null;
	private JComboBoxWithImageIconItems jComboBoxZoomInAndOutViewCentered = null;
	private JComboBoxWithImageIconItems jComboBoxOtherTools = null;
	private JCheckBox jEnablingCheckBox = null;

	/* Tool identifier constants */
	private final String PAN_TOOL = "HAND";
	private final String ZOOM_IN_TOOL = "zoomIn"; // This constant must be 'zoomIn' for a correct operation of the tools 'Zoom In' and 'Zoom In Map Centered'
	private final String ZOOM_OUT_TOOL = "zoomOut"; // This constant must be 'zoomOut' for a correct operation of the tools 'Zoom Out' and 'Zoom Out Map Centered'
	private final String ZOOM_IN_VIEW_CENTERED_TOOL = "ZOOM_IN_VIEW_CENTERED";
	private final String ZOOM_OUT_VIEW_CENTERED_TOOL = "ZOOM_OUT_VIEW_CENTERED";
	private final String MEASURE_DISTANCES_TOOL = "MEASURE_DISTANCES";
	private final String MEASURE_AREA_TOOL = "MEASURE_AREA";
	/* End tool identifier constants */
	
	private JPanel northPanel = null;


	/**
	 * This method initializes
	 */
	public WFSAreaPanel() {
		super();
		initialize();
	}

	/**
	 * Write the view coordinates into the coordinates panel
	 */
	private void initCoordinates(){
		BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		// We will use the adjusted extent because increases the usability
		Envelope envelope = activeView.getMapControl().getViewPort().getAdjustedExtent();
//		Rectangle2D r2d = activeView.getMapControl().getViewPort().getExtent();

		if (envelope != null){
			previousExtentValids.put(new Rectangle2D.Double(envelope.getMinimum(0),
					envelope.getMinimum(1),
					envelope.getMaximum(0)-envelope.getMaximum(0),
					envelope.getMaximum(1)-envelope.getMinimum(1)));

			setCoordinates(envelope.getMaximum(0), envelope.getMaximum(1),
					envelope.getMinimum(0), envelope.getMinimum(1));
//			System.out.println("Extent de la vista activa: " + r2d);
//			getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(r2d.getMinX() + r2d.getWidth()));
//			getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(r2d.getMaxX()));
////			getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(r2d.getMinY() + r2d.getHeight()));
//			getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(r2d.getMaxY()));
//			getCoordinatesPanel().getJTextFieldVertex2X().setText(Double.toString(r2d.getMinX()));
//			getCoordinatesPanel().getJTextFieldVertex2Y().setText(Double.toString(r2d.getMinY()));
		}
	}

	/**
	 * Removes all registered extents
	 */
	public void clearCoordinates() {
		this.hasUserDefinedAnArea = false;

		while (previousExtentValids.hasPrevious()) {
			previousExtentValids.removePrev();
		}
	}

	/**
	 * <p>Sets the coordinates of all the text fields. It's supposed that all coordinates are valid.</p>
	 *
	 * @param v1x value of the x coordinate of the right-up corner of the rectangle
	 * @param v1y value of the y coordinate of the right-up corner of the rectangle
	 * @param v2x value of the x coordinate of the left-up corner of the rectangle
	 * @param v2y value of the y coordinate of the left-up corner of the rectangle
	 */
	private void setCoordinates(double v1x, double v1y, double v2x, double v2y) {
		getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(v1x));
		getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(v1y));
		getCoordinatesPanel().getJTextFieldVertex2X().setText(Double.toString(v2x));
		getCoordinatesPanel().getJTextFieldVertex2Y().setText(Double.toString(v2y));
	}

	/**
	 * <p>Sets the coordinates of all the text fields. It's supposed that all coordinates are valid.</p>
	 *
	 * @param bbox the bounding box that represents a rectangle with the information of all coordinates
	 */
	private void setCoordinates(Rectangle2D bbox) {
		getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(bbox.getMaxX()));
		getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(bbox.getMaxY()));
		getCoordinatesPanel().getJTextFieldVertex2X().setText(Double.toString(bbox.getMinX()));
		getCoordinatesPanel().getJTextFieldVertex2Y().setText(Double.toString(bbox.getMinY()));
	}

	/**
	 * This method initializes coordinatesPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private AreaCoordinatesPanel getCoordinatesPanel() {
		if (coordinatesPanel == null) {
			coordinatesPanel = new AreaCoordinatesPanel();
		}
		return coordinatesPanel;
	}

	/**
	 * This method initializes jEnablingCheckBox
	 *
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEnablingJCheckBox() {
		if (jEnablingCheckBox == null) {
			jEnablingCheckBox = new JCheckBox(PluginServices.getText(this, "enabled"));
			jEnablingCheckBox.setBounds(DEFAULT_ENABLING_CHECKBOX_RECTANGLE);
			jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "enable_filter_by_area"));
			jEnablingCheckBox.setSelected(false);
			jEnablingCheckBox.addItemListener(new ItemListener() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
				 */
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "disable_filter_by_area"));
				 		enableCoorderatesRegionOperation();
					}
					else {
						jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "enable_filter_by_area"));
				 		disableCoorderatesRegionOperation();
					}

					IPanelGroup panelGroup = getPanelGroup();

					if (panelGroup == null)
						return;

					((WFSParamsPanel)panelGroup).setApplicable(true);
				}
			});

			// By default, the tool is disabled
	 		disableCoorderatesRegionOperation();
		}
		return jEnablingCheckBox;
	}

	/**
	 * This method initializes jComboBoxToolSelection
	 *
	 * @return  javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxToolSelection() {
		if (jComboBoxToolSelection == null) {
			jComboBoxToolSelection = new JComboBox();
			jComboBoxToolSelection.setBounds(DEFAULT_TOOL_JCOMBOBOX_RECTANGLE);
			jComboBoxToolSelection.setToolTipText(PluginServices.getText(this, "select_a_tool"));
			jComboBoxToolSelection.setEditable(false);
			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "disabled"), this.DISABLED_OPERATION));
			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_absolute_coordinates"), this.SELECTION_BY_COORDINATES_OPERATION));
			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_coordinates_using_view"), this.SELECTION_BY_AREA_OPERATION));

			jComboBoxToolSelection.addActionListener(new ActionListener() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox)e.getSource();
					switch(((ItemOperation)cb.getSelectedItem()).getOperation()) {
						case DISABLED_OPERATION:
					 		disableCoorderatesRegionOperation();
					 		disableAreaRegionOperation();
					 		break;
					 	case SELECTION_BY_COORDINATES_OPERATION:
					 		disableAreaRegionOperation();
					 		enableCoorderatesRegionOperation();
					 		break;
					 	case SELECTION_BY_AREA_OPERATION:
					 		// Only enable the area operation if there is data loaded in this MapControl
					 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
					 			disableCoorderatesRegionOperation();
					 			enableAreaRegionOperation();
					 		}
					 		else {
					 			JOptionPane.showMessageDialog(jComboBoxToolSelection, PluginServices.getText(null, "there_is_no_layer_loaded_in_the_active_view"), PluginServices.getText(null, "information"), JOptionPane.INFORMATION_MESSAGE);

					 			// Select: no tools
					 			jComboBoxToolSelection.setSelectedIndex(0);
					 			disableCoorderatesRegionOperation();
					 		}

					 		break;
					}
				}
			});
		}
		return jComboBoxToolSelection;
	}

	/**
	 * Enables the components associated to the selection of area by coordinates
	 */
	private void enableCoorderatesRegionOperation() {
//		 Adds associated icons
//		this.add(getJButtonUndo(), null);
//		this.add(getJButtonRedo(), null);
//		this.add(getJToggleButtonMove(), null);
//		this.add(getJToggleButtonScaling(), null);

		getCoordinatesPanel().setAllTextFieldsEnabled(true);

		// If the current active view has layers -> set the area in the fields:
		// Adds the extent of the viewport of the current active view
		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		ViewPort vP = view.getMapControl().getMapContext().getViewPort();

		// Update extent (we will use the adjusted extent because increases the usability)
		Envelope envelope = vP.getAdjustedExtent();
//		Rectangle2D r2D = vP.getExtent();

		if (envelope != null) {
//			getJToggleButtonMove().setEnabled(false);
// 			getJToggleButtonScaling().setEnabled(false);
//		}
//		else {
			getCoordinatesPanel().updateCoordinates(envelope);
//			getJToggleButtonMove().setEnabled(true);
//			getJToggleButtonScaling().setEnabled(true);
//			getCoordinatesPanel().getJButtonPreviewArea().setEnabled(true);
		}

		getCoordinatesPanel().getJButtonValidateArea().setEnabled(true);

		this.repaint();
	}

	/**
	 * Enables the components associated to the selection of area by view
	 */
	private void enableAreaRegionOperation() {
		// Adds associated icons
		this.add(getJButtonPan(), null);
		this.add(getJComboBoxZoomInAndOut(), null);
		getJComboBoxZoomInAndOut().revalidate(); // Force update this component
		this.add(getJButtonZoomPrevious(), null);
		this.add(getJButtonZoomComplete(), null);
		this.add(getJComboBoxZoomInAndOutViewCentered(), null);
		getJComboBoxZoomInAndOutViewCentered().revalidate(); // Force update this component
		this.add(getJComboBoxOtherTools(), null);
		getJComboBoxOtherTools().revalidate(); // Force update this component

		// Enables the MapControl area
		getSelectableMapAreaPanel().setEnabled(true);
	}

	/**
	 * Disables the components associated to the selection of area by coordinates
	 */
	private void disableCoorderatesRegionOperation() {
		// Removes associated icons
//		this.remove(getJButtonUndo());
//		this.remove(getJButtonRedo());
//		this.remove(getJToggleButtonMove());
//		this.remove(getJToggleButtonScaling());

		getCoordinatesPanel().setAllTextFieldsEnabled(false);
		getCoordinatesPanel().getJButtonValidateArea().setEnabled(false);

		this.repaint();
	}

	/**
	 * Disables the components associated to the selection of area by view
	 */
	private void disableAreaRegionOperation() {
		// Removes associated icons
		this.remove(getJButtonPan());
		this.remove(getJComboBoxZoomInAndOut());
		this.remove(getJButtonZoomPrevious());
		this.remove(getJButtonZoomComplete());
		this.remove(getJComboBoxZoomInAndOutViewCentered());
		this.remove(getJComboBoxOtherTools());

		// Disables the MapControl area
		getSelectableMapAreaPanel().setEnabled(false);
	}

	/**
	 * This method initializes areaMapControlPanel
	 *
	 * @return A reference to an object of SelectableMapControlAreaPanel
	 */
	private SelectableMapControlAreaPanel getSelectableMapAreaPanel() {
		if (selectableMapAreaPanel == null) {
			selectableMapAreaPanel = new SelectableMapControlAreaPanel();
		}

		return selectableMapAreaPanel;
	}

	/**
	 * This method initializes jButtonUndo
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonUndo() {
		if (jButtonUndo == null) {
			jButtonUndo = new JButton();
			jButtonUndo.setBounds(DEFAULT_UNDO_ICON_BUTTON_RECTANGLE);
			jButtonUndo.setIcon(PluginServices.getIconTheme().get("edit-undo"));
			jButtonUndo.setToolTipText(PluginServices.getText(this, "previous_area"));
			jButtonUndo.addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					goToPreviousZoom();
				}
			});
		}

		return jButtonUndo;
	}

//	/**
//	 * This method initializes jButtonUndo
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonRedo() {
//		if (jButtonRedo == null) {
//			jButtonRedo = new JButton();
//			jButtonRedo.setBounds(DEFAULT_REDO_ICON_BUTTON_RECTANGLE);
//			jButtonRedo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/edit-redo.png")));
//			jButtonRedo.setToolTipText(PluginServices.getText(this, "following_area"));
//		}
//
//		return jButtonRedo;
//	}

	/**
	 * This method initializes jToggleButtonMove
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getJToggleButtonMove() {
		if (jToggleButtonMove == null) {
			jToggleButtonMove = new JToggleButton();
			jToggleButtonMove.setBounds(DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE);
			jToggleButtonMove.setIcon(PluginServices.getIconTheme().get("WFS-move"));
			jToggleButtonMove.setToolTipText(PluginServices.getText(this, "move") + ": " + PluginServices.getText(this, "area_move_explanation"));
			jToggleButtonMove.addItemListener(new ItemListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
				 */
				public void itemStateChanged(ItemEvent e) {
					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
					if (jToggleButtonMove.isSelected())
						jToggleButtonScaling.setSelected(false);
				}
			});
		}

		return jToggleButtonMove;
	}

	/**
	 * This method initializes jToggleButtonScaling
	 *
	 * @return javax.swing.JButton
	 */
	private JToggleButton getJToggleButtonScaling() {
		if (jToggleButtonScaling == null) {
			jToggleButtonScaling = new JToggleButton();
			jToggleButtonScaling.setBounds(DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE);
			jToggleButtonScaling.setIcon(PluginServices.getIconTheme().get("WFS-scaling"));
			jToggleButtonScaling.setToolTipText(PluginServices.getText(this, "scaling") + ": " + PluginServices.getText(this, "area_scaling_explanation"));
			jToggleButtonScaling.addItemListener(new ItemListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
				 */
				public void itemStateChanged(ItemEvent e) {
					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
					if (jToggleButtonScaling.isSelected())
						jToggleButtonMove.setSelected(false);
				}
			});
		}

		return jToggleButtonScaling;
	}
	/**
	 * This method initializes jButtonPan
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonPan() {
		if (jButtonPan == null) {
			jButtonPan = new JButton();
			jButtonPan.setBounds(DEFAULT_PAN_ICON_BUTTON_RECTANGLE);
			jButtonPan.setIcon(PluginServices.getIconTheme().get("view-pan"));
			jButtonPan.setToolTipText(PluginServices.getText(this, "Desplazamiento"));
			jButtonPan.addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					getSelectableMapAreaPanel().setTool(PAN_TOOL);
				}
			});
		}
		return jButtonPan;
	}

	/**
	 * This method initializes jComboBoxZoomInAndOut
	 *
	 * @return A JComboBoxWithImageIconItems object reference
	 */
	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOut() {
		if (jComboBoxZoomInAndOut == null) {
			jComboBoxZoomInAndOut = new JComboBoxWithImageIconItems();
			jComboBoxZoomInAndOut.setBounds(DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE);
			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomIn.png",PluginServices.getIconTheme().get("view-zoom-in"),PluginServices.getText(this, "Zoom_Mas"), ZOOM_IN_TOOL));
			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-out"), PluginServices.getText(this, "Zoom_Menos"), ZOOM_OUT_TOOL));
			jComboBoxZoomInAndOut.addActionListener(new ActionListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
				}
	        });
		}

		return jComboBoxZoomInAndOut;
	}

	/**
	 * This method initializes jButtonZoomPrevious
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomPrevious() {
		if (jButtonZoomPrevious == null) {
			jButtonZoomPrevious = new JButton();
			jButtonZoomPrevious.setBounds(DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE);
			jButtonZoomPrevious.setIcon(PluginServices.getIconTheme().get("view-zoom-back"));
			jButtonZoomPrevious.setToolTipText(PluginServices.getText(this, "Zoom_Previo"));
			jButtonZoomPrevious.addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					goToPreviousZoom();
				}
			});
		}
		return jButtonZoomPrevious;
	}

	/**
	 * This method initializes jButtonZoomComplete
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonZoomComplete() {
		if (jButtonZoomComplete == null) {
			jButtonZoomComplete = new JButton();
			jButtonZoomComplete.setBounds(DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE);
			jButtonZoomComplete.setIcon(PluginServices.getIconTheme().get("view-zoom-map-contents"));
			jButtonZoomComplete.setToolTipText(PluginServices.getText(this, "Zoom_Completo"));
			jButtonZoomComplete.addMouseListener(new MouseAdapter() {
				/*
				 * (non-Javadoc)
				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
				 */
				public void mouseClicked(MouseEvent e) {
					getSelectableMapAreaPanel().getViewPort().setEnvelope(getSelectableMapAreaPanel().getMapContext().getLayers().getFullEnvelope());
					BaseView view =((BaseView)PluginServices.getMDIManager().getActiveWindow());
					view.repaintMap();
				}
			});
		}
		return jButtonZoomComplete;
	}

	/**
	 * This method initializes jComboBoxZoomInAndOut
	 *
	 * @return A JComboBoxWithImageIconItems object reference
	 */
	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOutViewCentered() {
		if (jComboBoxZoomInAndOutViewCentered == null) {
			jComboBoxZoomInAndOutViewCentered = new JComboBoxWithImageIconItems();
			jComboBoxZoomInAndOutViewCentered.setBounds(DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE);
			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/zoommas.png",PluginServices.getIconTheme().get("view-zoom-mas"), PluginServices.getText(this, "Zoom_Acercar"), ZOOM_IN_VIEW_CENTERED_TOOL));
			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-menos"), PluginServices.getText(this, "Zoom_Alejar"), ZOOM_OUT_VIEW_CENTERED_TOOL));
			jComboBoxZoomInAndOutViewCentered.addActionListener(new ActionListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();

					if (((String)iiii.getItemValue()).compareTo(ZOOM_IN_VIEW_CENTERED_TOOL) == 0) {
						getSelectableMapAreaPanel().zoomIn();
					}
					else {
						getSelectableMapAreaPanel().zoomOut();
					}
				}
	        });
		}
		return jComboBoxZoomInAndOutViewCentered;
	}

	/**
	 * This method initializes jComboBoxOtherTools
	 *
	 * @return A JComboBoxWithImageIconItems object reference
	 */
	private JComboBoxWithImageIconItems getJComboBoxOtherTools() {
		if (jComboBoxOtherTools == null) {
			jComboBoxOtherTools = new JComboBoxWithImageIconItems();
			jComboBoxOtherTools.setBounds(DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE);
			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Distancia.png",PluginServices.getIconTheme().get("view-query-distance"),PluginServices.getText(this, "medir_distancias"), MEASURE_DISTANCES_TOOL));
			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Poligono16.png",PluginServices.getIconTheme().get("view-query-area"), PluginServices.getText(this, "medir_area"), MEASURE_AREA_TOOL));
			jComboBoxOtherTools.addActionListener(new ActionListener() {
				/*
				 *  (non-Javadoc)
				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
				 */
				public void actionPerformed(ActionEvent e) {
					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
				}
	        });
		}

		return jComboBoxOtherTools;
	}

	/**
	 * Goes to previous zoom
	 */
	private void goToPreviousZoom() {
		// Gets the view port and sets its previous extent
		ViewPort vp = getSelectableMapAreaPanel().getMapContext().getViewPort();

		if (vp.getExtents().hasPrevious()) {
			vp.setPreviousExtent();

			// We will use the adjusted extent because increases the usability
			getCoordinatesPanel().updateCoordinates(vp.getAdjustedExtent());
//			getCoordinatesPanel().updateCoordinates(vp.getExtent());
		}
	}

	/**
	 * Sets the extent
	 *
	 * @param java.awt.geom.Rectangle2D
	 */
	public void setEnvelope(Envelope envelope) {
		if (envelope != null) {
			// Update coordinates in text fields
			getCoordinatesPanel().updateCoordinates(envelope);

			// If there is any layer loaded in the MapControl -> set the extent
			if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
				getSelectableMapAreaPanel().getMapContext().getViewPort().setEnvelope(envelope);
			}
		}
	}

	/**
	 * Gets the extent: a 'Rectangle2D.Double' object reference or 'null' if there is no data or if the data is invalid or user hasn't defined an area
	 *
	 * @return java.awt.geom.Rectangle2D or null if the data is invalid
	 */
	public Geometry getArea() {
		if (!getEnablingJCheckBox().isSelected())
			return null;

		if (!hasUserDefinedAnArea)
			return null;

		if (getCoordinatesPanel().areAllCoordinatesUndefined()) {
			return null; // Return null if there is no coordinate
		}
		else {
			if (getCoordinatesPanel().validVertexes()) {
				Rectangle2D r2d = getCoordinatesPanel().getExtent();
				GeneralPathX generalPath = new GeneralPathX();
				generalPath.moveTo(r2d.getMinX(), r2d.getMinY());
				generalPath.lineTo(r2d.getMaxX(), r2d.getMinY());
				generalPath.lineTo(r2d.getMaxX(), r2d.getMaxY());
				generalPath.lineTo(r2d.getMinX(), r2d.getMaxY());
				generalPath.lineTo(r2d.getMinX(), r2d.getMinY());
				try {
					return geometryManager.createSurface(generalPath, SUBTYPES.GEOM2D);
				} catch (CreateGeometryException e) {
					logger.error("Creating the area", e);
					return null;
				}
			}
			else {
				return null; // Returns null if coordinates aren't valid
			}
		}
	}

	/**
	 * Set parent's 'Applicable' button enable or disable according the value of the parameter
	 *
	 * @param b A boolean value
	 */
	private void setApplicable(boolean b) {
		IPanelGroup panelGroup = getPanelGroup();

		if (panelGroup == null)
			return;

		if (! ((WFSParamsPanel)panelGroup).getFilterPanel().getWFSFilterPanelIsAsTabForWFSLayersLoad())
			((WFSParamsPanel)panelGroup).setApplicable(b);
//			parent.isApplicable(true);
	}

	/**
	 * If there is some coordinate text field that has text, returns true; else returns false
	 *
	 * @return A boolean value
	 */
	public boolean areThereSomeCoordinatesWritten() {
		return getCoordinatesPanel().areThereSomeCoordinatesWritten();
	}

	/**
	 * If there is some coordinate text field that isn't undefined, returns true; else returns false
	 *
	 * @return A boolean value
	 */
	public boolean areThereSomeCoordinatesUndefined() {
		return getCoordinatesPanel().isThereAnyCoordinateUndefined();
	}

	/**
	 * If user has or hasn't defined an area
	 *
	 * @return A boolean value
	 */
	public boolean hasUserDefinedAnArea() {
		return hasUserDefinedAnArea;
	}

	/**
	 * Restores the inner attribute 'hasUserDefinedAnArea' to its default value (false)
	 */
	public void setUserHasntDefineAnArea() {
		hasUserDefinedAnArea = false;
	}

	/**
	 * Updates the current area information with the area of the active view
	 */
	public void updateWFSArea() {
		this.getSelectableMapAreaPanel().disableAllMouseListeners();

		// To prevent that events that take place (are produced) could be a nuisance to the load of the layers
		this.getSelectableMapAreaPanel().getMapContext().beginAtomicEvent();

		try {
			MapContext mapContext = this.getSelectableMapAreaPanel().getMapContext();

			// Removes all layers in the view area
			int numberOfLayers = mapContext.getLayers().getLayersCount();
			int i;

			for (i = (numberOfLayers-1); i >= 0; i--) {
				mapContext.getLayers().removeLayer(i);
			}
			PluginServices.getMainFrame().enableControls();
			// Adds the extent of the viewport of the current active view
			BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
			ViewPort vP = view.getMapControl().getMapContext().getViewPort();

			// Update extent (we will use the adjusted extent because increases the usability)
			Envelope envelope = vP.getAdjustedExtent();
//			Rectangle2D r2D = vP.getExtent();

			if (envelope == null) {
				// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
				this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();

				// No tools enabled
				getJComboBoxToolSelection().setSelectedIndex(0);

				return;
			}

			getCoordinatesPanel().updateCoordinates(envelope);
			mapContext.getViewPort().setEnvelope(envelope);

			// Adds the active layers of the current active view
			MapContext mC = view.getMapControl().getMapContext();

			numberOfLayers = mC.getLayers().getLayersCount();

			for (i = (numberOfLayers-1); i >= 0; i--) {
				mapContext.getLayers().addLayer(mC.getLayers().getLayer(i).cloneLayer());
			}

			// If has to enable all listeners of all tools on the view area
			if (((ItemOperation)this.getJComboBoxToolSelection().getSelectedItem()).getOperation() == SELECTION_BY_AREA_OPERATION)
				this.getSelectableMapAreaPanel().enableAllMouseListeners();

			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();

			// Refresh the view
			this.getSelectableMapAreaPanel().getViewPort().refreshExtent();
		} catch (Exception e) {
			NotificationManager.addError(e);

			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();

			// No tools enabled
			getJComboBoxToolSelection().setSelectedIndex(0);
			return;
		}
	}

	/**
	 * Represents an object that stores the necessary information for know each operation of the 'jComboBoxToolSelection'
	 *
	 * @author Pablo Piqueras Bartolomï¿½ (p_queras@hotmail.com)
	 */
	private class ItemOperation {
		String _name;
		int _operation;

		/**
		 * Default constructor with two parameters
		 *
		 * @param name Name of the operation
		 * @param operation A code that identifies the operation
		 */
		public ItemOperation(String name, int operation) {
			_name = new String(name);
			_operation = operation;
		}

		/**
		 * Returns the name of the operation
		 *
		 * @return An String
		 */
		public String getName() {
			return _name;
		}

		/**
		 * Returns the code that identifies the operation
		 *
		 * @return An integer value
		 */
		public int getOperation() {
			return _operation;
		}

		/**
		 * The name of the operation that will use JComboBox
		 */
		public String toString() {
			return _name;
		}
	}

	/**
	 * This class is a panel width four fields for indicate the coordinates of two points:
	 *   One for the ritgh-up point and another for the left-down point of a rectangle area
	 *
	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
	 * @author Jorge Piera Llodrï¿½ (piera_jor@gva.es)
	 */
	private class AreaCoordinatesPanel extends JPanel {
		private final Rectangle DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE = new Rectangle(412, 5, 62, 51);  //  @jve:decl-index=0:
		private final int DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT = 20;
		private final int DEFAULT_COORDIANTE_LABEL_WIDTH = 20;
		private final int DEFAULT_TEXT_FIELDS_WIDTH = 132;
		private final int DEFAULT_X_LOCATION = 8;
		private final int DEFAULT_Y_LEFT_UP_CORNER_LOCATION = 6;
		private final int DEFAULT_COORDINATE_WIDTH = DEFAULT_COORDIANTE_LABEL_WIDTH + DEFAULT_TEXT_FIELDS_WIDTH + 10;
		private final int DEFAULT_COORDINATE_HEIGHT = 25;
		private final Dimension DEFAULT_JLABEL_VERTEX_DIMENSION = new Dimension(60, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT);  //  @jve:decl-index=0:
		private final int DEFAULT_XY_COORDINATES_WIDTH = 2 * DEFAULT_COORDINATE_WIDTH + DEFAULT_JLABEL_VERTEX_DIMENSION.width + 10 ;

		private final String JTEXTFIELD_V1X_NAME = "V1X";
		private final String JTEXTFIELD_V1Y_NAME = "V1Y";
		private final String JTEXTFIELD_V2X_NAME = "V2X";
		private final String JTEXTFIELD_V2Y_NAME = "V2Y";

		private final short JTEXTFIELD_V1X = 1;
		private final short JTEXTFIELD_V1Y = 2;
		private final short JTEXTFIELD_V2X = 3;
		private final short JTEXTFIELD_V2Y = 4;

		private JPanel jPanelVertex1 = null;
		private JPanel jPanelVertex2 = null;
		private JPanel jPanelVertex1X = null;
		private JPanel jPanelVertex1Y = null;
		private JPanel jPanelVertex2X = null;
		private JPanel jPanelVertex2Y = null;

		private JLabel jLabelVertex1 = null;
		private JLabel jLabelVertex2 = null;
		private JLabel jLabelVertex1X = null;
		private JLabel jLabelVertex1Y = null;
		private JLabel jLabelVertex2X = null;
		private JLabel jLabelVertex2Y = null;
		private JTextFieldWithSCP jTextFieldVertex1X = null;
		private JTextFieldWithSCP jTextFieldVertex1Y = null;
		private JTextFieldWithSCP jTextFieldVertex2X = null;
		private JTextFieldWithSCP jTextFieldVertex2Y = null;
		private JButton jButtonPreviewArea = null;
		private String last_Coordinates[];
		private boolean hasChanged_v1X;
		private boolean hasChanged_v1Y;
		private boolean hasChanged_v2X;
		private boolean hasChanged_v2Y;
		private String previous_Coordinate_Value[];

		private short current_coordinate_with_focus;

		private FocusListener focusListenerForCoordinateValidation = null;
		private KeyListener keyListenerForCoordinateValidation = null;

		/**
		 * This is the default constructor
		 */
		public AreaCoordinatesPanel() {
			super();
			initialize();
		}

		/**
		 * This method initializes this
		 *
		 * @return void
		 */
		private void initialize() {
			this.setLayout(new GridBagLayout());
			this.setBounds(DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE);
			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));
			this.setToolTipText(PluginServices.getText(this, "select_by_vertexes_coordinates"));

			this.last_Coordinates = new String[4];
			this.last_Coordinates[0] = "";
			this.last_Coordinates[1] = "";
			this.last_Coordinates[2] = "";
			this.last_Coordinates[3] = "";
			this.previous_Coordinate_Value = new String[4];

			// By default no coordinates have changed
			this.resetCoordinatesFlags();
			
			GridBagConstraints gridBagConstraints = null;
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;				
			this.add(getJPanelVertex1(), gridBagConstraints);
			
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;	
			this.add(getJPanelVertex2(), gridBagConstraints);
			
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0;	
			this.add(getJButtonValidateArea(), gridBagConstraints);
		}

		/**
		 * Returns the default width of this component
		 *
		 * @return The default width
		 */
		public int getDefaultWidth() {
			return DEFAULT_COORDINATE_WIDTH * 2;
		}

		/**
		 * Returns the default height of this component
		 *
		 * @return The default height
		 */
		public int getDefaultHeight() {
			return DEFAULT_COORDINATE_HEIGHT * 2;
		}

		/**
		 * Sets all flags about if a coordinate has change to false (haven't changed)
		 */
		private void resetCoordinatesFlags() {
			hasChanged_v1X = false;
			hasChanged_v1Y = false;
			hasChanged_v2X = false;
			hasChanged_v2Y = false;
		}

		/**last_Coordinated_that_Changed
		 * This method initializes jPanelVertex1
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex1() {
			if (jPanelVertex1 == null) {
				jPanelVertex1 = new JPanel();
				jPanelVertex1.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
				jPanelVertex1.setLayout(new GridBagLayout());
				
				GridBagConstraints gridBagConstraints = null;
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;			
				jPanelVertex1.add(getJLabelVertex1(), gridBagConstraints);
				
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 1;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;
				jPanelVertex1.add(getJPanelVertex1X(), gridBagConstraints);
				
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 2;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;
				jPanelVertex1.add(getJPanelVertex1Y(), gridBagConstraints);
			}

			return jPanelVertex1;
		}

		/**
		 * This method initializes jPanelVertex2
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex2() {
			if (jPanelVertex2 == null) {
				jPanelVertex2 = new JPanel();
				jPanelVertex2.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION + DEFAULT_COORDINATE_HEIGHT, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
				jPanelVertex2.setLayout(new GridBagLayout());
				
				GridBagConstraints gridBagConstraints = null;
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;			
				jPanelVertex2.add(getJLabelVertex2(), gridBagConstraints);
				
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 1;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;						
				jPanelVertex2.add(getJPanelVertex2X(), gridBagConstraints);
			
				gridBagConstraints = new java.awt.GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 2;
				gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
				gridBagConstraints.weightx = 1.0;			
				jPanelVertex2.add(getJPanelVertex2Y(), gridBagConstraints);
			}

			return jPanelVertex2;
		}

		/**
		 * This method initializes jLabelVertex1
		 *
		 * @return javax.swing.JPanel
		 */
		private JLabel getJLabelVertex1() {
			if (jLabelVertex1 == null) {
				jLabelVertex1 = new JLabel();
				jLabelVertex1.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
				jLabelVertex1.setToolTipText(PluginServices.getText(this, "coordinates_from_area_right_up_vertex"));
				jLabelVertex1.setText(PluginServices.getText(this, "vertex") + " 1:");
				jLabelVertex1.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelVertex1.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex1.setForeground(new Color(0, 0, 255)); // Blue color for text
			}
			return jLabelVertex1;
		}

		/**
		 * This method initializes jLabelVertex2
		 *
		 * @return javax.swing.JPanel
		 */
		private JLabel getJLabelVertex2() {
			if (jLabelVertex2 == null) {
				jLabelVertex2 = new JLabel();
				jLabelVertex2.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
				jLabelVertex2.setToolTipText(PluginServices.getText(this, "coordinates_from_area_left_bottom_vertex"));
				jLabelVertex2.setText(PluginServices.getText(this, "vertex") + " 2:");
				jLabelVertex2.setHorizontalAlignment(SwingConstants.LEFT);
				jLabelVertex2.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex2.setForeground(new Color(128, 64, 0)); // Brown color for text
			}
			return jLabelVertex2;
		}

		/**
		 * This method initializes jPanelVertex1X
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex1X() {
			if (jPanelVertex1X == null) {
				jPanelVertex1X = new JPanel();
				jPanelVertex1X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
				jPanelVertex1X.setLayout(new GridBagLayout());
				jPanelVertex1X.add(getJLabelVertex1X(), null);
				jPanelVertex1X.add(getJTextFieldVertex1X(), null);
			}

			return jPanelVertex1X;
		}

		/**
		 * This method initializes jPanelVertex1Y
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex1Y() {
			if (jPanelVertex1Y == null) {
				jPanelVertex1Y = new JPanel();
				jPanelVertex1Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
				jPanelVertex1Y.setLayout(new GridBagLayout());
				jPanelVertex1Y.add(getJLabelVertex1Y(), null);
				jPanelVertex1Y.add(getJTextFieldVertex1Y(), null);
			}

			return jPanelVertex1Y;
		}

		/**
		 * This method initializes jPanelVertex2X
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex2X() {
			if (jPanelVertex2X == null) {
				jPanelVertex2X = new JPanel();
				jPanelVertex2X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
				jPanelVertex2X.setLayout(new GridBagLayout());
				jPanelVertex2X.add(getJLabelVertex2X(), null);
				jPanelVertex2X.add(getJTextFieldVertex2X(), null);
			}

			return jPanelVertex2X;
		}

		/**
		 * This method initializes jPanelVertex2Y
		 *
		 * @return javax.swing.JPanel
		 */
		private JPanel getJPanelVertex2Y() {
			if (jPanelVertex2Y == null) {
				jPanelVertex2Y = new JPanel();
				jPanelVertex2Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
				jPanelVertex2Y.setLayout(new GridBagLayout());
				jPanelVertex2Y.add(getJLabelVertex2Y(), null);
				jPanelVertex2Y.add(getJTextFieldVertex2Y(), null);
			}

			return jPanelVertex2Y;
		}

		/**
		 * This method initializes jLabelVertex1X
		 *
		 * @return  javax.swing.JLabel
		 */
		private JLabel getJLabelVertex1X() {
			if (jLabelVertex1X == null) {
				jLabelVertex1X = new JLabel();
				jLabelVertex1X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jLabelVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
				jLabelVertex1X.setText(PluginServices.getText(this, "x") + ":");
				jLabelVertex1X.setHorizontalAlignment(SwingConstants.CENTER);
				jLabelVertex1X.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex1X.setForeground(new Color(0, 0, 255)); // Blue color for text
			}

			return jLabelVertex1X;
		}

		/**
		 * This method initializes jLabelVertex2X
		 *
		 * @return  javax.swing.JLabel
		 */
		private JLabel getJLabelVertex2X() {
			if (jLabelVertex2X == null) {
				jLabelVertex2X = new JLabel();
				jLabelVertex2X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jLabelVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
				jLabelVertex2X.setText(PluginServices.getText(this, "x") + ":");
				jLabelVertex2X.setHorizontalAlignment(SwingConstants.CENTER);
				jLabelVertex2X.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex2X.setForeground(new Color(128, 64, 0)); // Brown color for text
			}

			return jLabelVertex2X;
		}

		/**
		 * This method initializes jLabelVertex1Y
		 *
		 * @return  javax.swing.JLabel
		 */
		private JLabel getJLabelVertex1Y() {
			if (jLabelVertex1Y == null) {
				jLabelVertex1Y = new JLabel();
				jLabelVertex1Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jLabelVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
				jLabelVertex1Y.setText(PluginServices.getText(this, "y") + ":");
				jLabelVertex1Y.setHorizontalAlignment(SwingConstants.CENTER);
				jLabelVertex1Y.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex1Y.setForeground(new Color(0, 0, 255)); // Blue color for text
			}

			return jLabelVertex1Y;
		}

		/**
		 * This method initializes jLabelVertex2Y
		 *
		 * @return  javax.swing.JLabel
		 */
		private JLabel getJLabelVertex2Y() {
			if (jLabelVertex2Y == null) {
				jLabelVertex2Y = new JLabel();
				jLabelVertex2Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jLabelVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
				jLabelVertex2Y.setText(PluginServices.getText(this, "y") + ":");
				jLabelVertex2Y.setHorizontalAlignment(SwingConstants.CENTER);
				jLabelVertex2Y.setVerticalAlignment(SwingConstants.CENTER);
				jLabelVertex2Y.setForeground(new Color(128, 64, 0)); // Brown color for text
			}

			return jLabelVertex2Y;
		}

		/**
		 * Returns a focus listener for validate the text of a JTextField when that component loses its focus
		 *
		 * @return java.awt.event.FocusListener
		 */
		private FocusListener getFocusListenerForCoordinateValidation() {
			if (focusListenerForCoordinateValidation == null) {
				focusListenerForCoordinateValidation = new FocusListener() {

					/*
					 *  (non-Javadoc)
					 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
					 */
					public void focusGained(FocusEvent e) {
						JTextField jTF = (JTextField)e.getSource();

						// Stores which coordinate has gotten the focus
						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {
							current_coordinate_with_focus = JTEXTFIELD_V1X;

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
							current_coordinate_with_focus = JTEXTFIELD_V1Y;

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
							current_coordinate_with_focus = JTEXTFIELD_V2X;

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
							current_coordinate_with_focus = JTEXTFIELD_V2Y;

							return;
						}

					}

					/*
					 *  (non-Javadoc)
					 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
					 */
					public void focusLost(FocusEvent e) {
						if (!getEnablingJCheckBox().isSelected())
							return;

						JTextField jTF = (JTextField)e.getSource();

						// If a coordinate has lost focus, is valid and has changed -> store the new value and set its related 'hasChanged' flag to true
						String text = jTF.getText();

						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {

							if (text.compareTo(last_Coordinates[0]) != 0) {
								// If the text written isn't a number, restores the previous value of that coordinate
//								if (! StringNumberUtilities.isNumber(text)) {
//									jTF.setText(last_Coordinates[0]);
//									return;
//								}

								hasChanged_v1X = true;

//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
									hasUserDefinedAnArea = true;
//								}
							}

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
							if (text.compareTo(last_Coordinates[1]) != 0) {
								// If the text written isn't a number, restores the previous value of that coordinate
//								if (! StringNumberUtilities.isNumber(text)) {
//									jTF.setText(last_Coordinates[1]);
//									return;
//								}

								hasChanged_v1Y = true;

//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
									hasUserDefinedAnArea = true;
//								}
							}

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
							if (text.compareTo(last_Coordinates[2]) != 0) {
								// If the text written isn't a number, restores the previous value of that coordinate
//								if (! StringNumberUtilities.isNumber(text)) {
//									jTF.setText(last_Coordinates[2]);
//									return;
//								}

								hasChanged_v2X = true;

//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
									hasUserDefinedAnArea = true;
//								}
							}

							return;
						}

						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
							if (text.compareTo(last_Coordinates[3]) != 0) {
								// If the text written isn't a number, restores the previous value of that coordinate
//								if (! StringNumberUtilities.isNumber(text)) {
//									jTF.setText(last_Coordinates[3]);
//									return;
//								}

								hasChanged_v2Y = true;

//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
									hasUserDefinedAnArea = true;
//								}
							}

							return;
						}
					}
				};
			}

			return focusListenerForCoordinateValidation;
		}

		private KeyListener getKeyListenerForCoordinateValidation() {
			if (keyListenerForCoordinateValidation == null) {
				keyListenerForCoordinateValidation = new KeyListener() {

					public void keyPressed(KeyEvent e) {
					}

					public void keyReleased(KeyEvent e) {
						validateCoordinate(current_coordinate_with_focus);
					}

					public void keyTyped(KeyEvent e) {
					}
				};
			}

			return keyListenerForCoordinateValidation;
		}

		/**
		 * This method initializes jTextFieldVertex1X
		 *
		 * @return JTextFieldWithSCP
		 */
		private JTextFieldWithSCP getJTextFieldVertex1X() {
			if (jTextFieldVertex1X == null) {
				jTextFieldVertex1X = new JTextFieldWithSCP("");
				jTextFieldVertex1X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jTextFieldVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
				jTextFieldVertex1X.addFocusListener(getFocusListenerForCoordinateValidation());
				jTextFieldVertex1X.addKeyListener(getKeyListenerForCoordinateValidation());
				jTextFieldVertex1X.setName(JTEXTFIELD_V1X_NAME);
			}
			return jTextFieldVertex1X;
		}

		/**
		 * This method initializes jTextFieldVertex1Y
		 *
		 * @return JTextFieldWithSCP
		 */
		private JTextFieldWithSCP getJTextFieldVertex1Y() {
			if (jTextFieldVertex1Y == null) {
				jTextFieldVertex1Y = new JTextFieldWithSCP("");
				jTextFieldVertex1Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jTextFieldVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
				jTextFieldVertex1Y.addFocusListener(getFocusListenerForCoordinateValidation());
				jTextFieldVertex1Y.addKeyListener(getKeyListenerForCoordinateValidation());
				jTextFieldVertex1Y.setName(JTEXTFIELD_V1Y_NAME);
			}
			return jTextFieldVertex1Y;
		}

		/**
		 * This method initializes jTextFieldVertex2X
		 *
		 * @return JTextFieldWithSCP
		 */
		private JTextFieldWithSCP getJTextFieldVertex2X() {
			if (jTextFieldVertex2X == null) {
				jTextFieldVertex2X = new JTextFieldWithSCP("");
				jTextFieldVertex2X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jTextFieldVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
				jTextFieldVertex2X.addFocusListener(getFocusListenerForCoordinateValidation());
				jTextFieldVertex2X.addKeyListener(getKeyListenerForCoordinateValidation());
				jTextFieldVertex2X.setName(JTEXTFIELD_V2X_NAME);
			}
			return jTextFieldVertex2X;
		}

		/**
		 * This method initializes jTextFieldVertex2Y
		 *
		 * @return JTextFieldWithSCP
		 */
		private JTextFieldWithSCP getJTextFieldVertex2Y() {
			if (jTextFieldVertex2Y == null) {
				jTextFieldVertex2Y = new JTextFieldWithSCP("");
				jTextFieldVertex2Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
				jTextFieldVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
				jTextFieldVertex2Y.addFocusListener(getFocusListenerForCoordinateValidation());
				jTextFieldVertex2Y.addKeyListener(getKeyListenerForCoordinateValidation());
				jTextFieldVertex2Y.setName(JTEXTFIELD_V2Y_NAME);
			}
			return jTextFieldVertex2Y;
		}

		/**
		 * Returns the double value of the text field V1X
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfJTextFieldV1X() {
			return Double.parseDouble(getJTextFieldVertex1X().getText());
		}

		/**
		 * Returns the double value of the text field V1Y
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfJTextFieldV1Y() {
			return Double.parseDouble(getJTextFieldVertex1Y().getText());
		}

		/**
		 * Returns the double value of the text field V2X
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfJTextFieldV2X() {
			return Double.parseDouble(getJTextFieldVertex2X().getText());
		}

		/**
		 * Returns the double value of the text field V2Y
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfJTextFieldV2Y() {
			return Double.parseDouble(getJTextFieldVertex2Y().getText());
		}

		/**
		 * Retuns the double value of the last text field V1X value
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfLastJTextFieldV1XValue() {
			return Double.parseDouble(last_Coordinates[0]);
		}

		/**
		 * Retuns the double value of the last text field V1Y value
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfLastJTextFieldV1YValue() {
			return Double.parseDouble(last_Coordinates[1]);
		}

		/**
		 * Retuns the double value of the last text field V2X value
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfLastJTextFieldV2XValue() {
			return Double.parseDouble(last_Coordinates[2]);
		}

		/**
		 * Retuns the double value of the last text field V2Y value
		 *
		 * @return A double number type
		 */
		public double getDoubleValueOfLastJTextFieldV2YValue() {
			return Double.parseDouble(last_Coordinates[3]);
		}

		/**
		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
		 *
		 * @param coordinate javax.swing.JTextField
		 * @return A boolean value
		 */
		private boolean validateCoordinate(JTextField coordinate) {
			if (coordinate != null) {
				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
				if ((coordinate.getText().compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(coordinate.getText()))) {
//					JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//					switch (last_Coordinate_that_Loose_the_Focus) {
//						case 1:
//							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
//							break;
//						case 2:
//							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
//							break;
//						case 3:
//							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
//							break;
//						case 4:
//							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
//							break;
//						default:
//							// Do nothing
//					}

					return false;
				}
				else {
					return true;
				}
			}

			return false; // If coordinate is null
		}

		/**
		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
		 *
		 * @param coordinate javax.swing.JTextField
		 * @return A boolean value
		 */
		private boolean validateCoordinate(int coordinate_ID) {
			JTextField coordinate = null;
			String text;

			switch (coordinate_ID) {
				case JTEXTFIELD_V1X:
					coordinate = getJTextFieldVertex1X();
					text = coordinate.getText();

					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
						coordinate.setText(previous_Coordinate_Value[0]); // Set last value
						return false;
					}
					else {
						previous_Coordinate_Value[0] = text;
						return true;
					}

//					break;
				case JTEXTFIELD_V1Y:
					coordinate = getJTextFieldVertex1Y();
					text = coordinate.getText();

					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
						coordinate.setText(previous_Coordinate_Value[1]); // Set last value
						return false;
					}
					else {
						previous_Coordinate_Value[1] = text;
						return true;
					}

//					break;
				case JTEXTFIELD_V2X:
					coordinate = getJTextFieldVertex2X();
					text = coordinate.getText();

					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
						coordinate.setText(previous_Coordinate_Value[2]); // Set last value
						return false;
					}
					else {
						previous_Coordinate_Value[2] = text;
						return true;
					}

//					break;
				case JTEXTFIELD_V2Y:
					coordinate = getJTextFieldVertex2Y();
					text = coordinate.getText();

					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
						coordinate.setText(previous_Coordinate_Value[3]); // Set last value
						return false;
					}
					else {
						previous_Coordinate_Value[3] = text;
						return true;
					}

//					break;
				default:
					return true; // Avoid problems
			}

//			if (coordinate != null) {
//				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
//					switch (coordinate_ID) {
//						case 1:
//							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
//							break;
//						case 2:
//							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
//							break;
//						case 3:
//							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
//							break;
//						case 4:
//							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
//							break;
//						default:
//							// Do nothing
//					}

//					return false;
//				}
//				else
//					return true;
////			}
//
//			return false; // If coordinate is null
		}

		/**
		 * This method initializes jButtonPreviewArea
		 *
		 * @return javax.swing.JButton
		 */
		private JButton getJButtonValidateArea() {
			if (jButtonPreviewArea == null) {
				jButtonPreviewArea = new JButton();
				jButtonPreviewArea.setBounds(DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE);
//				jButtonPreviewArea.setToolTipText(PluginServices.getText(this, "to_previsualize"));
				jButtonPreviewArea.setToolTipText(PluginServices.getText(this, "to_validate_coodinates"));
				jButtonPreviewArea.setVerticalTextPosition(AbstractButton.CENTER);
				jButtonPreviewArea.setHorizontalTextPosition(AbstractButton.CENTER);
				jButtonPreviewArea.setIcon(PluginServices.getIconTheme().get("validate-area"));
				jButtonPreviewArea.addMouseListener(new MouseAdapter() {
					/*
					 *  (non-Javadoc)
					 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
					 */
					public void mouseClicked(MouseEvent e) {
						validVertexes();
					}

//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//					 */
//					public void mouseClicked(MouseEvent e) {
//				 		// Only enable the area operation if there is data loaded in this MapControl
//
//						// Do nothing if there is no layer in the map control
//				 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() == 0) {
//				 			return;
//				 		}
//
//						if ((validateCoordinate(getJTextFieldVertex1X())) && (validateCoordinate(getJTextFieldVertex1Y())) && (validateCoordinate(getJTextFieldVertex2X())) && (validateCoordinate(getJTextFieldVertex2Y()))) {
//							ViewPort vP = getSelectableMapAreaPanel().getViewPort();
//							if (vP != null) {
//								// If has to scale the area
//								if (getJToggleButtonScaling().isSelected()) {
//									// Scale Vertex 1
//									if ((hasChanged_v1X) || (hasChanged_v1Y)) {
//										double cx = getDoubleValueOfJTextFieldV1X();
//										double cy = getDoubleValueOfJTextFieldV1Y();
//										double sx, sy, aux;
//
//										if (hasChanged_v1X) {
//											aux = getDoubleValueOfLastJTextFieldV1XValue();
//											if (aux == 0.0)
//												sx = 1.0; // Don't scale if new coordenate is zero
//											else
//												sx = cx / aux;
//										}
//										else
//											sx = 1.0;
//
//										if (hasChanged_v1Y) {
//											aux = getDoubleValueOfLastJTextFieldV1YValue();
//											if (aux == 0.0)
//												sy = 1.0; // Don't scale if new coordenate is zero
//											else
//												sy = cy / aux;
//										}
//										else
//											sy = 1.0;
//
//										if (sx == 1.0) {
//											// It's supposed that sy != 1.0
//											cx *= sy;
//
//											if (cx < getDoubleValueOfJTextFieldV2X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												restoreAllModifiedCoordinates(); // restores the coordinates
//												return;
//											}
//
//											getJTextFieldVertex1X().setText(Double.toString(cx));
//										}
//										else {
//											if (sy == 1.0) {
//												// It's supposed that sx != 1.0
//												cy *= sx;
//
//												if (cy < getDoubleValueOfJTextFieldV2Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1Y().setText(Double.toString(cy));
//											}
//											else {
//												// If there has been an error -> can't move different distances in X the two vertexes
//												if (sx != sy) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//											}
//										}
//									}
//
//									// Scale Vertex2
//									if ((hasChanged_v2X) || (hasChanged_v2Y)) {
//										double cx = getDoubleValueOfJTextFieldV2X();
//										double cy = getDoubleValueOfJTextFieldV2Y();
//										double sx, sy, aux;
//
//										if (hasChanged_v2X) {
//											aux = getDoubleValueOfLastJTextFieldV2XValue();
//											if (aux == 0.0)
//												sx = 1.0; // Don't scale if new coordenate is zero
//											else
//												sx = cx / aux;
//										}
//										else
//											sx = 1.0;
//
//										if (hasChanged_v2Y) {
//											aux = getDoubleValueOfLastJTextFieldV2YValue();
//											if (aux == 0.0)
//												sy = 1.0; // Don't scale if new coordenate is zero
//											else
//												sy = cy / aux;
//										}
//										else
//											sy = 1.0;
//
//										if (sx == 1.0) {
//											// It's supposed that sy != 1.0
//											cx *= sy;
//
//											if (cx > getDoubleValueOfJTextFieldV1X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												restoreAllModifiedCoordinates(); // restores the coordinates
//												return;
//											}
//
//											getJTextFieldVertex2X().setText(Double.toString(cx));
//										}
//										else {
//											if (sy == 1.0) {
//												// It's supposed that sx != 1.0
//												cy *= sx;
//
//												if (cy > getDoubleValueOfJTextFieldV1Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex2Y().setText(Double.toString(cy));
//											}
//											else {
//												// If there has been an error -> can't move different distances in X the two vertexes
//												if (sx != sy) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//											}
//										}
//									}
//								}
//								else {
//									// If has to move the area
//									if (getJToggleButtonMove().isSelected()) {
//										// Move in X
//										if ((hasChanged_v1X) || (hasChanged_v2X)) {
//											double c1 = getDoubleValueOfJTextFieldV1X();
//											double c2 = getDoubleValueOfJTextFieldV2X();
//											double d1, d2;
//
//											if (hasChanged_v1X)
//												d1 = c1 - getDoubleValueOfLastJTextFieldV1XValue();
//											else
//												d1 = 0.0;
//
//											if (hasChanged_v2X)
//												d2 = c2 - getDoubleValueOfLastJTextFieldV2XValue();
//											else
//												d2 = 0.0;
//
//											if (d1 == 0.0) {
//												// It's supposed that d2 != 0
//												c1 += d2;
//
//												if (c1 < getDoubleValueOfJTextFieldV2X()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1X().setText(Double.toString(c1));
//											}
//											else {
//												if (d2 == 0.0) {
//													// It's supposed that d1 != 0
//													c2 += d1;
//
//													if (c2 > getDoubleValueOfJTextFieldV1X()) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//
//													getJTextFieldVertex2X().setText(Double.toString(c2));
//												}
//												else {
//													// If there has been an error -> can't move different distances in X the two vertexes
//													if (d1 != d2) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_X"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//												}
//											}
//										}
//
//										// Move in Y
//										if ((hasChanged_v1Y) || (hasChanged_v2Y)) {
//											double c1 = getDoubleValueOfJTextFieldV1Y();
//											double c2 = getDoubleValueOfJTextFieldV2Y();
//											double d1, d2;
//
//											if (hasChanged_v1Y)
//												d1 = c1 - getDoubleValueOfLastJTextFieldV1YValue();
//											else
//												d1 = 0.0;
//
//											if (hasChanged_v2Y)
//												d2 = c2 - getDoubleValueOfLastJTextFieldV2YValue();
//											else
//												d2 = 0.0;
//
//											if (d1 == 0.0) {
//												// It's supposed that d2 != 0
//												c1 += d2;
//
//												if (c1 < getDoubleValueOfJTextFieldV2Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1Y().setText(Double.toString(c1));
//											}
//											else {
//												if (d2 == 0.0) {
//													// It's supposed that d1 != 0
//													c2 += d1;
//
//													if (c2 > getDoubleValueOfJTextFieldV1Y()) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//
//													getJTextFieldVertex2Y().setText(Double.toString(c2));
//												}
//												else {
//													// If there has been an error -> can't move different distances in Y the two vertexes
//													if (d1 != d2) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_Y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//												}
//											}
//										}
//									}
//									else {
//										boolean canSetExtent = true;
//
//										// Check if there is a impossible coordinate
//										if (hasChanged_v1X) {
//											if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v1Y) {
//											if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v2X) {
//											if (getDoubleValueOfJTextFieldV2X() > getDoubleValueOfJTextFieldV1X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v2Y) {
//											if (getDoubleValueOfJTextFieldV2Y() > getDoubleValueOfJTextFieldV1Y()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										// If can set the new Extent -> finish
//										if (!canSetExtent) {
//											restoreAllModifiedCoordinates(); // restores the coordinates
//											return;
//										}
//									}
//								}
//
//								//Rectangle2D r2d = vP.getExtent();
//								if (vP.getExtent() != null){
//									vP.setExtent(getNewRectangleByCoordinates());
//									vP.refreshExtent();
//								}
//							}
//						}
//						else {
//							JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "at_least_one_incorrect_coordinate"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//						}
//					}
				});
			}
			return jButtonPreviewArea;
		}

		/**
		 * Calculates the new rectangle using the coordinates of the text fields
		 *
		 * @return java.awt.geom.Rectangle2D
		 */
		private Rectangle2D getNewRectangleByCoordinates() {
			return new Rectangle2D.Double(getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV2Y(), getDoubleValueOfJTextFieldV1X() - getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV1Y() - getDoubleValueOfJTextFieldV2Y());
		}

		/**
		 * Restores the value of all text fields with coordinates modified to their last value
		 */
		private void restoreAllModifiedCoordinates() {
			if (hasChanged_v1X) {
				getJTextFieldVertex1X().setText(last_Coordinates[0]);
				hasChanged_v1X = false;
			}

			if (hasChanged_v1Y) {
				getJTextFieldVertex1Y().setText(last_Coordinates[1]);
				hasChanged_v1Y = false;
			}

			if (hasChanged_v2X) {
				getJTextFieldVertex2X().setText(last_Coordinates[2]);
				hasChanged_v2X = false;
			}

			if (hasChanged_v2Y) {
				getJTextFieldVertex2Y().setText(last_Coordinates[3]);
				hasChanged_v2Y = false;
			}
		}

		/**
		 * Enables or disables all inner JTextField
		 *
		 * @param b A boolean value
		 */
		public void setAllTextFieldsEnabled(boolean b) {
			getJTextFieldVertex1X().setEnabled(b);
			getJTextFieldVertex1Y().setEnabled(b);
			getJTextFieldVertex2X().setEnabled(b);
			getJTextFieldVertex2Y().setEnabled(b);
		}

		/**
		 * Sets the extent into the text fields
		 *
		 * @param envelope java.awt.geom.Rectangle2D
		 */
		public void updateCoordinates(Envelope envelope){
			getJTextFieldVertex1X().setText(String.valueOf(envelope.getMaximum(0)));
			last_Coordinates[0] = getJTextFieldVertex1X().getText();
			previous_Coordinate_Value[0] = last_Coordinates[0];

			getJTextFieldVertex1Y().setText(String.valueOf(envelope.getMaximum(1)));
			last_Coordinates[1] = getJTextFieldVertex1Y().getText();
			previous_Coordinate_Value[1] = last_Coordinates[1];

			getJTextFieldVertex2X().setText(String.valueOf(envelope.getMinimum(0)));
			last_Coordinates[2] = getJTextFieldVertex2X().getText();
			previous_Coordinate_Value[2] = last_Coordinates[2];

			getJTextFieldVertex2Y().setText(String.valueOf(envelope.getMinimum(0)));
			last_Coordinates[3] = getJTextFieldVertex2Y().getText();
			previous_Coordinate_Value[3] = last_Coordinates[3];

			resetCoordinatesFlags();

			// Enable the 'Applicate' button
//			setApplicable(true);
		}

		/**
		 * Changes the color of all coordinates text fields to <code>c</code>.
		 *
		 * @param c the new background color
		 */
		private void setBackGroundColorToCoordinatesFields(Color c) {
			getJTextFieldVertex1X().setBackground(c);
			getJTextFieldVertex1Y().setBackground(c);
			getJTextFieldVertex2X().setBackground(c);
			getJTextFieldVertex2Y().setBackground(c);
		}

		/**
		 * Validates all coordinates of both vertexes separately
		 *
		 * @return A ValidationInfo object reference
		 */
		private ValidationInfo validVertexesCoordinates() {
			String infoPrefix = null, infoSufix = null;

			if (! validateCoordinate(getJTextFieldVertex1X())) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "invalid_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v1x");
				}

				getJTextFieldVertex1X().setBackground(Color.RED);

//				else {
//					infoSufix += ", " + PluginServices.getText(this, "v1x");
//				}
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1x"));
			}

			if (! validateCoordinate(getJTextFieldVertex1Y())) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "invalid_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v1y");
				}
				else {
					infoPrefix = PluginServices.getText(this, "invalid_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v1y");
				}

				getJTextFieldVertex1Y().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1y"));
			}

			if (! validateCoordinate(getJTextFieldVertex2X())) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "invalid_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v2x");
				}
				else {
					infoPrefix = PluginServices.getText(this, "invalid_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v2x");
				}

				getJTextFieldVertex2X().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2x"));
			}

			if (! validateCoordinate(getJTextFieldVertex2Y())) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "invalid_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v2y");
				}
				else {
					infoPrefix = PluginServices.getText(this, "invalid_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v2y");
				}

				getJTextFieldVertex2Y().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2y"));
			}

			if (infoPrefix != null)
				return new ValidationInfo(false, infoPrefix + infoSufix);

			// If arrives here, all coordinates are valid
			setBackGroundColorToCoordinatesFields(Color.GREEN);
			return new ValidationInfo(true, "");
		}

		/**
		 * Validates that Vertex1 is in the right-up corner and the Vertex2 in the left-down corner of the area/rectangle <br>
		 * (It's supposed that coordinates of the vertexes are valid) <br>
		 * (It's supposed that going to right or up, the coordinate value increases)
		 *
		 * @return A ValidationInfo object reference
		 */
		private ValidationInfo validVertexesPositions() {
			String infoPrefix = null, infoSufix = null;

			// Check if no coordinate it's defined
			if (getJTextFieldVertex1X().getText().compareTo("") == 0) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "undefined_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v1x");
				}

				getJTextFieldVertex1X().setBackground(Color.RED);

//				else {
//					infoSufix += ", " + PluginServices.getText(this, "v1x");
//				}
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1x"));
			}

			if (getJTextFieldVertex1Y().getText().compareTo("") == 0) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "undefined_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v1y");
				}
				else {
					infoPrefix = PluginServices.getText(this, "undefined_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v1y");
				}

				getJTextFieldVertex1Y().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1y"));
			}

			if (getJTextFieldVertex2X().getText().compareTo("") == 0) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "undefined_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v2x");
				}
				else {
					infoPrefix = PluginServices.getText(this, "undefined_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v2x");
				}

				getJTextFieldVertex2X().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2x"));
			}

			if (getJTextFieldVertex2Y().getText().compareTo("") == 0) {
				if (infoSufix == null) {
					infoPrefix = PluginServices.getText(this, "undefined_coordinate") + ": ";
					infoSufix = PluginServices.getText(this, "v2y");
				}
				else {
					infoPrefix = PluginServices.getText(this, "undefined_coordinates") + ": ";
					infoSufix += ", " + PluginServices.getText(this, "v2y");
				}

				getJTextFieldVertex2Y().setBackground(Color.RED);
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2y"));
			}

			if (infoPrefix != null)
				return new ValidationInfo(false, infoPrefix + infoSufix);

			// Check if the vertex V1 is in the right-up corner and the vertex V2 is in the left-down corner
			if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
				getJTextFieldVertex1X().setBackground(Color.RED);
				getJTextFieldVertex2X().setBackground(Color.RED);
				return new ValidationInfo(false, PluginServices.getText(this, "v1x") + " < " + PluginServices.getText(this, "v2x"));
			}

			if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
				getJTextFieldVertex1Y().setBackground(Color.RED);
				getJTextFieldVertex2Y().setBackground(Color.RED);
				return new ValidationInfo(false, PluginServices.getText(this, "v1y") + " < " + PluginServices.getText(this, "v2y"));
			}

			// If arrives here -> vertexes positions are valid
			setBackGroundColorToCoordinatesFields(Color.GREEN);
			return new ValidationInfo(true, "");
		}

//		/** OLD VERSION
//		 * Validates all coordinates of both vertexes separately
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesCoordinates() {
//			if (! validateCoordinate(getJTextFieldVertex1X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex1Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here, all coordinates are valid
//			return new ValidationInfo(true, "");
//		}
//
//		/** OLD VERSION
//		 * Validates that Vertex1 is in the right-up corner and the Vertex2 in the left-down corner of the area/rectangle <br>
//		 * (It's supposed that coordinates of the vertexes are valid) <br>
//		 * (It's supposed that going to right or up, the coordinate value increases)
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesPositions() {
//			// Check if no coordinate it's defined
//			if (getJTextFieldVertex1X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (getJTextFieldVertex2X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getJTextFieldVertex1Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (getJTextFieldVertex2Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// Check if the vertex V1 is in the right-up corner and the vertex V2 is in the left-down corner
//			if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1x") + " < " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1y") + " < " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here -> vertexes positions are valid
//			return new ValidationInfo(true, "");
//		}

		/**
		 * Validate each coordinate of each vertex and the position of the vertexes
		 *
		 * @return A boolean value
		 */
		public boolean validVertexes() {
			ValidationInfo v1 = validVertexesCoordinates();

			if (!v1.isValid()) {

//				setApplicable(false);

				// Notify to user that no filter area will be applied
				JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v1.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);

				// Restore the previous valid extent
				if (previousExtentValids.hasPrevious()) {
					Rectangle2D previousExtent = previousExtentValids.removePrev();
					setCoordinates(previousExtent);

					if (!previousExtentValids.hasPrevious()) {
						hasUserDefinedAnArea = false;
						setApplicable(false);
					}

					previousExtentValids.put(previousExtent);
				}

				return false;
			}
			else {
				ValidationInfo v2 = validVertexesPositions();
				if (!v2.isValid()) {
//					setApplicable(false);

					// Notify to user that no filter area will be applied
					JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v2.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);

					// Restore the previous valid extent
					if (previousExtentValids.hasPrevious()) {
						Rectangle2D previousExtent = previousExtentValids.removePrev();
						setCoordinates(previousExtent);

						if (!previousExtentValids.hasPrevious()) {
							hasUserDefinedAnArea = false;
							setApplicable(false);
						}

						previousExtentValids.put(previousExtent);
					}

					return false;
				}
				else {
					// Once has been validated -> allow apply with the new extent, and save it
					if (previousExtentValids.hasPrevious()) {
						Rectangle2D previousExtent = previousExtentValids.removePrev();
						if (! areEqual(previousExtent, getExtent())) {
							previousExtentValids.put(previousExtent);
							previousExtentValids.put(getExtent());
							setApplicable(true);
						}
						else {
							previousExtentValids.put(previousExtent);
						}
					}
					else {
						previousExtentValids.put(getExtent());
						setApplicable(true);
					}

					return true;
				}
			}
		}

		/**
		 * <p>Compares two bi-dimensional rectangles, and returns <code>true</code> if both are equals.</p>
		 *
		 * @param rect1 first rectangle 2D to compare
		 * @param rect2 second rectangle 2D to compare
		 *
		 * @return <code>true</code> if both are equals; otherwise <code>false</code>
		 */
		private boolean areEqual(Rectangle2D rect1, Rectangle2D rect2) {
			return ((rect1.getX() == rect2.getX()) && (rect1.getY() == rect2.getY()) &&
					(rect1.getWidth() == rect2.getWidth()) && (rect1.getHeight() == rect2.getHeight()));
		}

		/**
		 * Gets the extent (rectangle) represented by the values of the coordinate text fields
		 *
		 * @return java.awt.geom.Rectangle2D
		 */
		public Rectangle2D getExtent() {
			double v1x = getDoubleValueOfJTextFieldV1X();
			double v1y = getDoubleValueOfJTextFieldV1Y();
			double v2x = getDoubleValueOfJTextFieldV2X();
			double v2y = getDoubleValueOfJTextFieldV2Y();

			return new Rectangle2D.Double(v2x, v2y, (v1x - v2x), (v1y  - v2y));
		}

		/**
		 * Returns true if there is some coordinate text field with data; else returns false
		 *
		 * @return A boolean value
		 */
		public boolean areThereSomeCoordinatesWritten() {
			return ((getJTextFieldVertex1X().getText().compareTo("") != 0) | (getJTextFieldVertex1Y().getText().compareTo("") != 0) | (getJTextFieldVertex2X().getText().compareTo("") != 0) | (getJTextFieldVertex2Y().getText().compareTo("") != 0));
		}

		/**
		 * Returns true if all coordinate text fields are undefined (without values)
		 *
		 * @return A boolean value
		 */
		public boolean areAllCoordinatesUndefined() {
			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) & (getJTextFieldVertex1Y().getText().compareTo("") == 0) & (getJTextFieldVertex2X().getText().compareTo("") == 0) & (getJTextFieldVertex2Y().getText().compareTo("") == 0));
		}


		/**
		 * Returns true if there is some coordinate undefined (without values)
		 *
		 * @return A boolean value
		 */
		public boolean isThereAnyCoordinateUndefined() {
			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) | (getJTextFieldVertex1Y().getText().compareTo("") == 0) | (getJTextFieldVertex2X().getText().compareTo("") == 0) | (getJTextFieldVertex2Y().getText().compareTo("") == 0));
		}
	}

	/**
	 * This class is a MapControl JComponent that has visual information and allows user interact with some tools and view the results
	 *
	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
	 */
	private class SelectableMapControlAreaPanel extends MapControl {
		private MouseListener[] mouseListeners;
		private MouseWheelListener[] mouseWheelListeners;
		private MouseMotionListener[] mouseMotionListeners;

		/**
		 * Default constructor
		 */
		public SelectableMapControlAreaPanel() {
			super();
			initialize();
		}

		/**
		 * This method initializes this component
		 */
		public void initialize() {
			/* Sets Bounds of this graphical component */
			this.setBounds(DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE);

			/* Sets border to this graphical component */
			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));

			MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();

			this.setMapContext(mp.cloneFMap());
			ViewPort vP = this.getViewPort();

			// We will use the adjusted extent because increases the usability
			Envelope envelope = vP.getAdjustedExtent();
//				Rectangle2D r2D = vP.getExtent();

			if (envelope != null) {
				vP.refreshExtent();
				getCoordinatesPanel().updateCoordinates(envelope);
			}

			/* Adds listeners to this MapControl */
			this.addToolsListeners();

			/* Sets default tool */
			this.setTool(PAN_TOOL);
		}

		/**
		 * Refresh the active view
		 */
		public void refreshWithTheActiveView() {
			MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();
			this.setMapContext(mp.cloneFMap());
			ViewPort vP = this.getViewPort();

			// We will use the adjusted extent because increases the usability
			Envelope envelope = vP.getAdjustedExtent();
//				Rectangle2D r2D = vP.getExtent();

			if (envelope != null) {
				vP.refreshExtent();
				getCoordinatesPanel().updateCoordinates(envelope);
			}
		}

		/**
		 * Adds listeners that allows user interact with it's mouse over this component and that represents different tools
		 */
		private void addToolsListeners() {
			// MOVEMENT EVENTS LISTENER: sets mouse coordinates to the status bar
	        StatusBarListener sbl = new StatusBarListener(this);

	        // ZOOM OUT (Presses on the map and it will be centered showing a bigger area)
	        // Only pressing, not selecting a rectangle area
	        ZoomOutListener zol = new ZoomOutListener(this);
	        this.addMapTool(ZOOM_OUT_TOOL, new Behavior[]{new PointBehavior(zol), new MouseMovementBehavior(sbl)});

	        // ZOOM IN (Using a Rectangle or a simple mouse click)
	        ZoomOutRightButtonListener zoil = new ZoomOutRightButtonListener(this);
	        ZoomInListener zil = new ZoomInListener(this);
	        this.addMapTool(ZOOM_IN_TOOL, new Behavior[]{new RectangleBehavior(zil),
	        				new PointBehavior(zoil), new MouseMovementBehavior(sbl)});

	        // PAN
	        PanListener pl = new PanListener(this);
	        this.addMapTool(PAN_TOOL, new Behavior[]{new MoveBehavior(pl), new MouseMovementBehavior(sbl)});

	        // MEASURE DISTANCES
	        MeasureListener mli = new MeasureListener(this);
	        this.addMapTool(MEASURE_DISTANCES_TOOL, new Behavior[]{new PolylineBehavior(mli), new MouseMovementBehavior(sbl)});

	        // MEASURE AREA
	        AreaListener ali = new AreaListener(this);
	        this.addMapTool(MEASURE_AREA_TOOL, new Behavior[]{new PolygonBehavior(ali), new MouseMovementBehavior(sbl)});

	        this.getViewPort().addViewPortListener(new ViewPortListener() {
	        	/*
	        	 *  (non-Javadoc)
	        	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
	        	 */
				public void extentChanged(ExtentEvent e) {
					if (!getEnablingJCheckBox().isSelected())
						return;

					if (getMapContext().getViewPort().getExtents().hasPrevious()) {
						// We will use the adjusted extent because increases the usability
						Envelope envelope = getViewPort().getAdjustedExtent();
//						Rectangle2D r2d = getViewPort().getExtent();

						if (envelope != null) {
							getCoordinatesPanel().updateCoordinates(envelope);

//							if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
								hasUserDefinedAnArea = true;
//							}
						}

						getJButtonZoomPrevious().setEnabled(true);
						getJButtonUndo().setEnabled(true);
					}
					else {
						getJButtonZoomPrevious().setEnabled(false);
						getJButtonUndo().setEnabled(false);
					}
				}

				/*
				 *  (non-Javadoc)
				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
				 */
				public void backColorChanged(ColorEvent e) {
				}

				/*
				 *  (non-Javadoc)
				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#projectionChanged(com.iver.cit.gvsig.fmap.ProjectionEvent)
				 */
				public void projectionChanged(ProjectionEvent e) {
				}

	        });
		}

		/*
		 *  (non-Javadoc)
		 * @see java.awt.Component#setEnabled(boolean)
		 */
		public void setEnabled(boolean b) {
			super.setEnabled(b);

			if (b)
				enableAllMouseListeners();
			else
				disableAllMouseListeners();
		}

		/**
		 * Disables all mouse listeners
		 */
		public void disableAllMouseListeners() {
			int i;

			// Only disable listeners if there are listeners to remove (it's supposed that there are always the same number of mouse listeners, and this listeners
			//   are referenciated by this component or by the private attribute 'mouseListeners')

			// Mouse Button Listeners
			if (mouseListeners == null) {
				mouseListeners = this.getMouseListeners();

				for (i = 0; i < mouseListeners.length; i++) {
					removeMouseListener(mouseListeners[i]);
				}
			}

			// Mouse Wheel Listeners
			if (mouseWheelListeners == null) {
				mouseWheelListeners = this.getMouseWheelListeners();

				for (i = 0; i < mouseWheelListeners.length; i++) {
					removeMouseWheelListener(mouseWheelListeners[i]);
				}
			}

			// Mouse motion listeners
			if (mouseMotionListeners == null) {
				mouseMotionListeners = this.getMouseMotionListeners();

				for (i = 0; i < mouseMotionListeners.length; i++) {
					removeMouseMotionListener(mouseMotionListeners[i]);
				}
			}
		}

		/**
		 * Enables all mouse listeners
		 */
		public void enableAllMouseListeners() {
			int i;

			// Mouse Button Listeners
			for (i = 0; i < mouseListeners.length; i++) {
				addMouseListener(mouseListeners[i]);
			}

			mouseListeners = null;

			// Mouse Wheel Listeners
			for (i = 0; i < mouseWheelListeners.length; i++) {
				addMouseWheelListener(mouseWheelListeners[i]);
			}

			mouseWheelListeners = null;

			// Mouse motion listeners
			for (i = 0; i < mouseMotionListeners.length; i++) {
				addMouseMotionListener(mouseMotionListeners[i]);
			}

			mouseMotionListeners = null;
		}
	}

	/**
	 * This class has information about a validation: <br>
	 *   - A boolean value -> if has been or not validated <br>
	 *   - An String -> a message about the invalid
	 *
	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
	 */
	private class ValidationInfo {
		private boolean _valid;
		private String _message;

		/**
		 * Default constructor with two parameters
		 *
		 * @param valid If was valid or not
		 * @param message A message associate to the validation (If has been valid, this attribute should be "", in the other case should have a message with
		 *                  an explanation about the invalidation)
		 */
		public ValidationInfo(boolean valid, String message) {
			_valid = valid;
			_message = message;
		}

		/**
		 * Returns the value of the inner attribute '_valid'
		 *
		 * @return A boolean value
		 */
		public boolean isValid() {
			return _valid;
		}

		/**
		 * Returns the value of the inner attribute '_message'
		 *
		 * @return java.lang.String
		 */
		public String getMessage() {
			return _message;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#initialize()
	 */
	protected void initialize() {
		setLabel(PluginServices.getText(this, "area"));
		setLabelGroup(PluginServices.getText(this, "wfs"));
		this.setLayout(new java.awt.BorderLayout());
		java.awt.GridBagConstraints gridBagConstraints;

		northPanel = new JPanel();
		northPanel.setLayout(new java.awt.GridBagLayout());		
	
		northPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, PluginServices.getText(this, "select_by_area"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));

		// By default, user hasn't defined an area
		this.hasUserDefinedAnArea = false;

		// Adds JPanel with the coordinates
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;	
		northPanel.add(getCoordinatesPanel(), gridBagConstraints);
	
		// Adds a check box to enable or enable this kind of filtering
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		northPanel.add(getEnablingJCheckBox(), gridBagConstraints);
		
		// Adds JComboBox to select a part of the graphical interface
//		this.add(getJComboBoxToolSelection(), null);
//		this.getJComboBoxToolSelection().setSelectedIndex(0); // By default select first element

		// Adds JPanel with the view
//		this.add(getSelectableMapAreaPanel(), null);

		previousExtentValids = new ExtentHistory();
		initCoordinates();
		
		add(northPanel, java.awt.BorderLayout.NORTH);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.panels.IWFSPanel#refresh(com.iver.cit.gvsig.fmap.layers.WFSLayerNode)
	 */
	public void refresh(WFSSelectedFeature layer) {
	}
}



///**
// * VERSIÓN PROVISIONAL
// *
// * This panel allows user to select the area he/she wants to get in the view.
// * There are two options to do this:
// *  - Indicating the coordinates of the top-left and down-right corners
// *  - Selecting the area with some visual tool
// *
// * @author Pablo Piqueras Bartolomï¿½ (p_queras@hotmail.com)
// * @author Jorge Piera Llodrï¿½ (piera_jor@gva.es)
// */
//public class WFSAreaPanel extends JPanel {
//	private final Rectangle DEFAULT_BOUNDS = new Rectangle(10, 5, 490, 380);
//	private final Rectangle DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE = new Rectangle(8, 20, 481, 60);
//	private final Rectangle DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE = new Rectangle(8, 115, 481, 265);
//	private final Rectangle DEFAULT_UNDO_ICON_BUTTON_RECTANGLE = new Rectangle (250, 85, 25, 25);
////	private final Rectangle DEFAULT_REDO_ICON_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
////	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
////	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (331, 85, 25, 25);
//	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
//	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
//	private final Rectangle DEFAULT_PAN_ICON_BUTTON_RECTANGLE = new Rectangle(250, 85, 25, 25);
//	private final Rectangle DEFAULT_TOOL_JCOMBOBOX_RECTANGLE = new Rectangle(10, 87, 230, 21);
//	private final Rectangle DEFAULT_ENABLING_CHECKBOX_RECTANGLE = new Rectangle(10, 87, 150, 21);
//	private final Rectangle DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE = new Rectangle(277, 85, 47, 25);
//	private final Rectangle DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE = new Rectangle(326, 85, 25, 25);
//	private final Rectangle DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE = new Rectangle(353, 85, 25, 25);
//	private final Rectangle DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE = new Rectangle(380, 85, 47, 25);
//	private final Rectangle DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE = new Rectangle(429, 85, 47, 25);
//
//	private final int DISABLED_OPERATION = 0;
//	private final int SELECTION_BY_COORDINATES_OPERATION = 1;
//	private final int SELECTION_BY_AREA_OPERATION = 2;
//	private boolean hasUserDefinedAnArea;
//
//	private WFSWizardData data = null;
//	private WFSParamsPanel parent = null;
//	private AreaCoordinatesPanel coordinatesPanel = null;
//	private SelectableMapControlAreaPanel selectableMapAreaPanel = null;
//	private JComboBox jComboBoxToolSelection = null;
//	private JButton jButtonUndo = null;
////	private JButton jButtonRedo = null;
//	private JToggleButton jToggleButtonMove = null;
//	private JToggleButton jToggleButtonScaling = null;
//	private JButton jButtonPan = null;
//	private JButton jButtonZoomPrevious = null;
//	private JButton jButtonZoomComplete = null;
//	private JComboBoxWithImageIconItems jComboBoxZoomInAndOut = null;
//	private JComboBoxWithImageIconItems jComboBoxZoomInAndOutViewCentered = null;
//	private JComboBoxWithImageIconItems jComboBoxOtherTools = null;
//	private JCheckBox jEnablingCheckBox = null;
//
//	/* Tool identifier constants */
//	private final String PAN_TOOL = "HAND";
//	private final String ZOOM_IN_TOOL = "zoomIn"; // This constant must be 'zoomIn' for a correct operation of the tools 'Zoom In' and 'Zoom In Map Centered'
//	private final String ZOOM_OUT_TOOL = "zoomOut"; // This constant must be 'zoomOut' for a correct operation of the tools 'Zoom Out' and 'Zoom Out Map Centered'
//	private final String ZOOM_IN_VIEW_CENTERED_TOOL = "ZOOM_IN_VIEW_CENTERED";
//	private final String ZOOM_OUT_VIEW_CENTERED_TOOL = "ZOOM_OUT_VIEW_CENTERED";
//	private final String MEASURE_DISTANCES_TOOL = "MEASURE_DISTANCES";
//	private final String MEASURE_AREA_TOOL = "MEASURE_AREA";
//	/* End tool identifier constants */
//
//
//	/**
//	 * This method initializes
//	 */
//	public WFSAreaPanel(WFSParamsPanel parent) {
//		super();
//		this.parent = parent;
//		initialize();
//	}
//
//	/**
//	 * This method initializes this
//	 */
//	private void initialize() {
//		this.setLayout(null);
//		this.setBounds(DEFAULT_BOUNDS);
//		this.setBorder(javax.swing.BorderFactory.createTitledBorder(
//				null, PluginServices.getText(this, "select_by_area"),
//				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
//
//		// By default, user hasn't defined an area
//		this.hasUserDefinedAnArea = false;
//
//		// Adds JPanel with the coordinates
//		this.add(getCoordinatesPanel(), null);
//
//		// Adds a check box to enable or enable this kind of filtering
//		this.add(getEnablingJCheckBox());
//
//		// Adds JComboBox to select a part of the graphical interface
////		this.add(getJComboBoxToolSelection(), null);
////		this.getJComboBoxToolSelection().setSelectedIndex(0); // By default select first element
//
//		// Adds JPanel with the view
////		this.add(getSelectableMapAreaPanel(), null);
//
//		initCoordinates();
//	}
//
//	/**
//	 * Write the view coordinates into the coordinates panel
//	 */
//	private void initCoordinates(){
//		BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//		Rectangle2D r2d = activeView.getMapControl().getViewPort().getExtent();
//		if (r2d != null){
//			System.out.println("Extent de la vista activa: " + r2d);
////			getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(r2d.getMinX() + r2d.getWidth()));
//			getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(r2d.getMaxX()));
////			getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(r2d.getMinY() + r2d.getHeight()));
//			getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(r2d.getMaxY()));
//			getCoordinatesPanel().getJTextFieldVertex2X().setText(Double.toString(r2d.getMinX()));
//			getCoordinatesPanel().getJTextFieldVertex2Y().setText(Double.toString(r2d.getMinY()));
//		}
//	}
//
//	/**
//	 * This method initializes coordinatesPanel
//	 *
//	 * @return javax.swing.JPanel
//	 */
//	private AreaCoordinatesPanel getCoordinatesPanel() {
//		if (coordinatesPanel == null) {
//			coordinatesPanel = new AreaCoordinatesPanel();
//		}
//		return coordinatesPanel;
//	}
//
//	private JCheckBox getEnablingJCheckBox() {
//		if (jEnablingCheckBox == null) {
//			jEnablingCheckBox = new JCheckBox(PluginServices.getText(this, "enabled"));
//			jEnablingCheckBox.setBounds(DEFAULT_ENABLING_CHECKBOX_RECTANGLE);
//			jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "enable_filter_by_area"));
//			jEnablingCheckBox.setSelected(false);
//			jEnablingCheckBox.addItemListener(new ItemListener() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
//				 */
//				public void itemStateChanged(ItemEvent e) {
//					if (e.getStateChange() == ItemEvent.SELECTED) {
//						jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "disable_filter_by_area"));
//				 		enableCoorderatesRegionOperation();
//					}
//					else {
//						jEnablingCheckBox.setToolTipText(PluginServices.getText(this, "enable_filter_by_area"));
//				 		disableCoorderatesRegionOperation();
//					}
//				}
//			});
//
//			// By default, the tool is disabled
//	 		disableCoorderatesRegionOperation();
//		}
//		return jEnablingCheckBox;
//	}
//
//	/**
//	 * This method initializes jComboBoxToolSelection
//	 *
//	 * @return  javax.swing.JComboBox
//	 */
//	private JComboBox getJComboBoxToolSelection() {
//		if (jComboBoxToolSelection == null) {
//			jComboBoxToolSelection = new JComboBox();
//			jComboBoxToolSelection.setBounds(DEFAULT_TOOL_JCOMBOBOX_RECTANGLE);
//			jComboBoxToolSelection.setToolTipText(PluginServices.getText(this, "select_a_tool"));
//			jComboBoxToolSelection.setEditable(false);
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "disabled"), this.DISABLED_OPERATION));
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_absolute_coordinates"), this.SELECTION_BY_COORDINATES_OPERATION));
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_coordinates_using_view"), this.SELECTION_BY_AREA_OPERATION));
//
//			jComboBoxToolSelection.addActionListener(new ActionListener() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBox cb = (JComboBox)e.getSource();
//					switch(((ItemOperation)cb.getSelectedItem()).getOperation()) {
//						case DISABLED_OPERATION:
//					 		disableCoorderatesRegionOperation();
//					 		disableAreaRegionOperation();
//					 		break;
//					 	case SELECTION_BY_COORDINATES_OPERATION:
//					 		disableAreaRegionOperation();
//					 		enableCoorderatesRegionOperation();
//					 		break;
//					 	case SELECTION_BY_AREA_OPERATION:
//					 		// Only enable the area operation if there is data loaded in this MapControl
//					 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
//					 			disableCoorderatesRegionOperation();
//					 			enableAreaRegionOperation();
//					 		}
//					 		else {
//					 			JOptionPane.showMessageDialog(jComboBoxToolSelection, PluginServices.getText(null, "there_is_no_layer_loaded_in_the_active_view"), PluginServices.getText(null, "information"), JOptionPane.INFORMATION_MESSAGE);
//
//					 			// Select: no tools
//					 			jComboBoxToolSelection.setSelectedIndex(0);
//					 			disableCoorderatesRegionOperation();
//					 		}
//
//					 		break;
//					}
//				}
//			});
//		}
//		return jComboBoxToolSelection;
//	}
//
//	/**
//	 * Enables the components associated to the selection of area by coordinates
//	 */
//	private void enableCoorderatesRegionOperation() {
////		 Adds associated icons
//		this.add(getJButtonUndo(), null);
////		this.add(getJButtonRedo(), null);
////		this.add(getJToggleButtonMove(), null);
////		this.add(getJToggleButtonScaling(), null);
//
//		getCoordinatesPanel().setAllTextFieldsEnabled(true);
//
//		// If the current active view has layers -> set the area in the fields:
//		// Adds the extent of the viewport of the current active view
//		BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//		ViewPort vP = view.getMapControl().getMapContext().getViewPort();
//
//		// Update extent
//		Rectangle2D r2D = vP.getExtent();
//
//		if (r2D != null) {
////			getJToggleButtonMove().setEnabled(false);
//// 			getJToggleButtonScaling().setEnabled(false);
////		}
////		else {
//			getCoordinatesPanel().updateCoordinates(r2D);
////			getJToggleButtonMove().setEnabled(true);
////			getJToggleButtonScaling().setEnabled(true);
////			getCoordinatesPanel().getJButtonPreviewArea().setEnabled(true);
//		}
//
//		getCoordinatesPanel().getJButtonPreviewArea().setEnabled(true);
//
//		this.repaint();
//	}
//
//	/**
//	 * Enables the components associated to the selection of area by view
//	 */
//	private void enableAreaRegionOperation() {
//		// Adds associated icons
//		this.add(getJButtonPan(), null);
//		this.add(getJComboBoxZoomInAndOut(), null);
//		getJComboBoxZoomInAndOut().revalidate(); // Force update this component
//		this.add(getJButtonZoomPrevious(), null);
//		this.add(getJButtonZoomComplete(), null);
//		this.add(getJComboBoxZoomInAndOutViewCentered(), null);
//		getJComboBoxZoomInAndOutViewCentered().revalidate(); // Force update this component
//		this.add(getJComboBoxOtherTools(), null);
//		getJComboBoxOtherTools().revalidate(); // Force update this component
//
//		// Enables the MapControl area
//		getSelectableMapAreaPanel().setEnabled(true);
//	}
//
//	/**
//	 * Disables the components associated to the selection of area by coordinates
//	 */
//	private void disableCoorderatesRegionOperation() {
//		// Removes associated icons
//		this.remove(getJButtonUndo());
////		this.remove(getJButtonRedo());
////		this.remove(getJToggleButtonMove());
////		this.remove(getJToggleButtonScaling());
//
//		getCoordinatesPanel().setAllTextFieldsEnabled(false);
//		getCoordinatesPanel().getJButtonPreviewArea().setEnabled(false);
//
//		this.repaint();
//	}
//
//	/**
//	 * Disables the components associated to the selection of area by view
//	 */
//	private void disableAreaRegionOperation() {
//		// Removes associated icons
//		this.remove(getJButtonPan());
//		this.remove(getJComboBoxZoomInAndOut());
//		this.remove(getJButtonZoomPrevious());
//		this.remove(getJButtonZoomComplete());
//		this.remove(getJComboBoxZoomInAndOutViewCentered());
//		this.remove(getJComboBoxOtherTools());
//
//		// Disables the MapControl area
//		getSelectableMapAreaPanel().setEnabled(false);
//	}
//
//	/**
//	 * This method initializes areaMapControlPanel
//	 *
//	 * @return A reference to an object of SelectableMapControlAreaPanel
//	 */
//	private SelectableMapControlAreaPanel getSelectableMapAreaPanel() {
//		if (selectableMapAreaPanel == null) {
//			selectableMapAreaPanel = new SelectableMapControlAreaPanel();
//		}
//
//		return selectableMapAreaPanel;
//	}
//
//	/**
//	 * This method initializes jButtonUndo
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonUndo() {
//		if (jButtonUndo == null) {
//			jButtonUndo = new JButton();
//			jButtonUndo.setBounds(DEFAULT_UNDO_ICON_BUTTON_RECTANGLE);
//			jButtonUndo.setIcon(PluginServices.getIconTheme().get("edit-undo"));
//			jButtonUndo.setToolTipText(PluginServices.getText(this, "previous_area"));
//			jButtonUndo.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					goToPreviousZoom();
//				}
//			});
//		}
//
//		return jButtonUndo;
//	}
//
////	/**
////	 * This method initializes jButtonUndo
////	 *
////	 * @return javax.swing.JButton
////	 */
////	private JButton getJButtonRedo() {
////		if (jButtonRedo == null) {
////			jButtonRedo = new JButton();
////			jButtonRedo.setBounds(DEFAULT_REDO_ICON_BUTTON_RECTANGLE);
////			jButtonRedo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/edit-redo.png")));
////			jButtonRedo.setToolTipText(PluginServices.getText(this, "following_area"));
////		}
////
////		return jButtonRedo;
////	}
//
//	/**
//	 * This method initializes jToggleButtonMove
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JToggleButton getJToggleButtonMove() {
//		if (jToggleButtonMove == null) {
//			jToggleButtonMove = new JToggleButton();
//			jToggleButtonMove.setBounds(DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE);
//			jToggleButtonMove.setIcon(PluginServices.getIconTheme().get("WFS-move"));
//			jToggleButtonMove.setToolTipText(PluginServices.getText(this, "move") + ": " + PluginServices.getText(this, "area_move_explanation"));
//			jToggleButtonMove.addItemListener(new ItemListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
//				 */
//				public void itemStateChanged(ItemEvent e) {
//					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
//					if (jToggleButtonMove.isSelected())
//						jToggleButtonScaling.setSelected(false);
//				}
//			});
//		}
//
//		return jToggleButtonMove;
//	}
//
//	/**
//	 * This method initializes jToggleButtonScaling
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JToggleButton getJToggleButtonScaling() {
//		if (jToggleButtonScaling == null) {
//			jToggleButtonScaling = new JToggleButton();
//			jToggleButtonScaling.setBounds(DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE);
//			jToggleButtonScaling.setIcon(PluginServices.getIconTheme().get("WFS-scaling"));
//			jToggleButtonScaling.setToolTipText(PluginServices.getText(this, "scaling") + ": " + PluginServices.getText(this, "area_scaling_explanation"));
//			jToggleButtonScaling.addItemListener(new ItemListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
//				 */
//				public void itemStateChanged(ItemEvent e) {
//					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
//					if (jToggleButtonScaling.isSelected())
//						jToggleButtonMove.setSelected(false);
//				}
//			});
//		}
//
//		return jToggleButtonScaling;
//	}
//	/**
//	 * This method initializes jButtonPan
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonPan() {
//		if (jButtonPan == null) {
//			jButtonPan = new JButton();
//			jButtonPan.setBounds(DEFAULT_PAN_ICON_BUTTON_RECTANGLE);
//			jButtonPan.setIcon(PluginServices.getIconTheme().get("view-pan"));
//			jButtonPan.setToolTipText(PluginServices.getText(this, "Desplazamiento"));
//			jButtonPan.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					getSelectableMapAreaPanel().setTool(PAN_TOOL);
//				}
//			});
//		}
//		return jButtonPan;
//	}
//
//	/**
//	 * This method initializes jComboBoxZoomInAndOut
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOut() {
//		if (jComboBoxZoomInAndOut == null) {
//			jComboBoxZoomInAndOut = new JComboBoxWithImageIconItems();
//			jComboBoxZoomInAndOut.setBounds(DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE);
//			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomIn.png",PluginServices.getIconTheme().get("view-zoom-in"),PluginServices.getText(this, "Zoom_Mas"), ZOOM_IN_TOOL));
//			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-out"), PluginServices.getText(this, "Zoom_Menos"), ZOOM_OUT_TOOL));
//			jComboBoxZoomInAndOut.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
//				}
//	        });
//		}
//
//		return jComboBoxZoomInAndOut;
//	}
//
//	/**
//	 * This method initializes jButtonZoomPrevious
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonZoomPrevious() {
//		if (jButtonZoomPrevious == null) {
//			jButtonZoomPrevious = new JButton();
//			jButtonZoomPrevious.setBounds(DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE);
//			jButtonZoomPrevious.setIcon(PluginServices.getIconTheme().get("view-zoom-back"));
//			jButtonZoomPrevious.setToolTipText(PluginServices.getText(this, "Zoom_Previo"));
//			jButtonZoomPrevious.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					goToPreviousZoom();
//				}
//			});
//		}
//		return jButtonZoomPrevious;
//	}
//
//	/**
//	 * This method initializes jButtonZoomComplete
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonZoomComplete() {
//		if (jButtonZoomComplete == null) {
//			jButtonZoomComplete = new JButton();
//			jButtonZoomComplete.setBounds(DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE);
//			jButtonZoomComplete.setIcon(PluginServices.getIconTheme().get("view-zoom-map-contents"));
//			jButtonZoomComplete.setToolTipText(PluginServices.getText(this, "Zoom_Completo"));
//			jButtonZoomComplete.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					getSelectableMapAreaPanel().getViewPort().setExtent(getSelectableMapAreaPanel().getMapContext().getLayers().getFullExtent());
//					BaseView view =((BaseView)PluginServices.getMDIManager().getActiveWindow());
//					view.repaintMap();
//				}
//			});
//		}
//		return jButtonZoomComplete;
//	}
//
//	/**
//	 * This method initializes jComboBoxZoomInAndOut
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOutViewCentered() {
//		if (jComboBoxZoomInAndOutViewCentered == null) {
//			jComboBoxZoomInAndOutViewCentered = new JComboBoxWithImageIconItems();
//			jComboBoxZoomInAndOutViewCentered.setBounds(DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE);
//			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/zoommas.png",PluginServices.getIconTheme().get("view-zoom-mas"), PluginServices.getText(this, "Zoom_Acercar"), ZOOM_IN_VIEW_CENTERED_TOOL));
//			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-menos"), PluginServices.getText(this, "Zoom_Alejar"), ZOOM_OUT_VIEW_CENTERED_TOOL));
//			jComboBoxZoomInAndOutViewCentered.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//
//					if (((String)iiii.getItemValue()).compareTo(ZOOM_IN_VIEW_CENTERED_TOOL) == 0) {
//						getSelectableMapAreaPanel().zoomIn();
//					}
//					else {
//						getSelectableMapAreaPanel().zoomOut();
//					}
//				}
//	        });
//		}
//		return jComboBoxZoomInAndOutViewCentered;
//	}
//
//	/**
//	 * This method initializes jComboBoxOtherTools
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxOtherTools() {
//		if (jComboBoxOtherTools == null) {
//			jComboBoxOtherTools = new JComboBoxWithImageIconItems();
//			jComboBoxOtherTools.setBounds(DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE);
//			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Distancia.png",PluginServices.getIconTheme().get("view-query-distance"),PluginServices.getText(this, "medir_distancias"), MEASURE_DISTANCES_TOOL));
//			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Poligono16.png",PluginServices.getIconTheme().get("view-query-area"), PluginServices.getText(this, "medir_area"), MEASURE_AREA_TOOL));
//			jComboBoxOtherTools.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
//				}
//	        });
//		}
//
//		return jComboBoxOtherTools;
//	}
//
//	/**
//	 * Goes to previous zoom
//	 */
//	private void goToPreviousZoom() {
//		// Gets the view port and sets its previous extent
//		ViewPort vp = getSelectableMapAreaPanel().getMapContext().getViewPort();
//
//		if (vp.getExtents().hasPrevious()) {
//			vp.setPreviousExtent();
//			getCoordinatesPanel().updateCoordinates(vp.getExtent());
//		}
//	}
//
//	/**
//	 * Sets the extent
//	 *
//	 * @param java.awt.geom.Rectangle2D
//	 */
//	public void setExtent(Rectangle2D extent) {
//		if (extent != null) {
//			// Update coordinates in text fields
//			getCoordinatesPanel().updateCoordinates(extent);
//
//			// If there is any layer loaded in the MapControl -> set the extent
//			if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
//				getSelectableMapAreaPanel().getMapContext().getViewPort().setExtent(extent);
//			}
//		}
//	}
//
//	/**
//	 * Gets the extent: a 'Rectangle2D.Double' object reference or 'null' if there is no data or if the data is invalid or user hasn't defined an area
//	 *
//	 * @return java.awt.geom.Rectangle2D or null if the data is invalid
//	 */
//	public Rectangle2D getExtent() {
//		if (!getEnablingJCheckBox().isSelected())
//			return null;
//
////		if (!hasUserDefinedAnArea)
////			return null;
//
//		if (getCoordinatesPanel().areAllCoordinatesUndefined()) {
//			return null; // Return null if there is no coordinate
//		}
//		else {
//			if (getCoordinatesPanel().validVertexes()) {
//				System.out.println("Extent que devuelve: " + getCoordinatesPanel().getExtent());
//				return getCoordinatesPanel().getExtent();
//			}
//			else {
//				return null; // Returns null if coordinates aren't valid
//			}
//		}
//	}
//
//	/**
//	 * Set parent's 'Applicable' button enable or disable according the value of the parameter
//	 *
//	 * @param b A boolean value
//	 */
//	private void setApplicable(boolean b) {
//		if (!parent.getWFSFilterPanelIsAsTabForWFSLayersLoad())
//			parent.isApplicable(true);
//	}
//
//	/**
//	 * If there is some coordinate text field that has text, returns true; else returns false
//	 *
//	 * @return A boolean value
//	 */
//	public boolean areThereSomeCoordinatesWritten() {
//		return getCoordinatesPanel().areThereSomeCoordinatesWritten();
//	}
//
//	/**
//	 * If there is some coordinate text field that isn't undefined, returns true; else returns false
//	 *
//	 * @return A boolean value
//	 */
//	public boolean areThereSomeCoordinatesUndefined() {
//		return getCoordinatesPanel().isThereAnyCoordinateUndefined();
//	}
//
//	/**
//	 * If user has or hasn't defined an area
//	 *
//	 * @return A boolean value
//	 */
//	public boolean hasUserDefinedAnArea() {
//		return hasUserDefinedAnArea;
//	}
//
//	/**
//	 * Restores the inner attribute 'hasUserDefinedAnArea' to its default value (false)
//	 */
//	public void setUserHasntDefineAnArea() {
//		hasUserDefinedAnArea = false;
//	}
//
//	/**
//	 * Updates the current area information with the area of the active view
//	 */
//	public void updateWFSArea() {
//		this.getSelectableMapAreaPanel().disableAllMouseListeners();
//
//		// To prevent that events that take place (are produced) could be a nuisance to the load of the layers
//		this.getSelectableMapAreaPanel().getMapContext().beginAtomicEvent();
//
//		try {
//			MapContext mapContext = this.getSelectableMapAreaPanel().getMapContext();
//
//			// Removes all layers in the view area
//			int numberOfLayers = mapContext.getLayers().getLayersCount();
//			int i;
//
//			for (i = (numberOfLayers-1); i >= 0; i--) {
//				mapContext.getLayers().removeLayer(i);
//			}
//
//			// Adds the extent of the viewport of the current active view
//			BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//			ViewPort vP = view.getMapControl().getMapContext().getViewPort();
//
//			// Update extent
//			Rectangle2D r2D = vP.getExtent();
//
//			if (r2D == null) {
//				// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//				this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//				// No tools enabled
//				getJComboBoxToolSelection().setSelectedIndex(0);
//
//				return;
//			}
//
//			getCoordinatesPanel().updateCoordinates(r2D);
//			mapContext.getViewPort().setExtent(r2D);
//
//			// Adds the active layers of the current active view
//			MapContext mC = view.getMapControl().getMapContext();
//
//			numberOfLayers = mC.getLayers().getLayersCount();
//
//			for (i = (numberOfLayers-1); i >= 0; i--) {
//				mapContext.getLayers().addLayer(mC.getLayers().getLayer(i).cloneLayer());
//			}
//
//			// If has to enable all listeners of all tools on the view area
//			if (((ItemOperation)this.getJComboBoxToolSelection().getSelectedItem()).getOperation() == SELECTION_BY_AREA_OPERATION)
//				this.getSelectableMapAreaPanel().enableAllMouseListeners();
//
//			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//			// Refresh the view
//			this.getSelectableMapAreaPanel().getViewPort().refreshExtent();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//			// No tools enabled
//			getJComboBoxToolSelection().setSelectedIndex(0);
//			return;
//		}
//	}
//
//	/**
//	 * Represents an object that stores the necessary information for know each operation of the 'jComboBoxToolSelection'
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras@hotmail.com)
//	 */
//	private class ItemOperation {
//		String _name;
//		int _operation;
//
//		/**
//		 * Default constructor with two parameters
//		 *
//		 * @param name Name of the operation
//		 * @param operation A code that identifies the operation
//		 */
//		public ItemOperation(String name, int operation) {
//			_name = new String(name);
//			_operation = operation;
//		}
//
//		/**
//		 * Returns the name of the operation
//		 *
//		 * @return An String
//		 */
//		public String getName() {
//			return _name;
//		}
//
//		/**
//		 * Returns the code that identifies the operation
//		 *
//		 * @return An integer value
//		 */
//		public int getOperation() {
//			return _operation;
//		}
//
//		/**
//		 * The name of the operation that will use JComboBox
//		 */
//		public String toString() {
//			return _name;
//		}
//	}
//
//	/**
//	 * This class is a panel width four fields for indicate the coordinates of two points:
//	 *   One for the ritgh-up point and another for the left-down point of a rectangle area
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 * @author Jorge Piera Llodrï¿½ (piera_jor@gva.es)
//	 */
//	private class AreaCoordinatesPanel extends JPanel {
//		private final Rectangle DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE = new Rectangle(412, 5, 62, 51);  //  @jve:decl-index=0:
//		private final int DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT = 20;
//		private final int DEFAULT_COORDIANTE_LABEL_WIDTH = 20;
//		private final int DEFAULT_TEXT_FIELDS_WIDTH = 132;
//		private final int DEFAULT_X_LOCATION = 8;
//		private final int DEFAULT_Y_LEFT_UP_CORNER_LOCATION = 6;
//		private final int DEFAULT_COORDINATE_WIDTH = DEFAULT_COORDIANTE_LABEL_WIDTH + DEFAULT_TEXT_FIELDS_WIDTH + 10;
//		private final int DEFAULT_COORDINATE_HEIGHT = 25;
//		private final Dimension DEFAULT_JLABEL_VERTEX_DIMENSION = new Dimension(60, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT);  //  @jve:decl-index=0:
//		private final int DEFAULT_XY_COORDINATES_WIDTH = 2 * DEFAULT_COORDINATE_WIDTH + DEFAULT_JLABEL_VERTEX_DIMENSION.width + 10 ;
//
//		private final String JTEXTFIELD_V1X_NAME = "V1X";
//		private final String JTEXTFIELD_V1Y_NAME = "V1Y";
//		private final String JTEXTFIELD_V2X_NAME = "V2X";
//		private final String JTEXTFIELD_V2Y_NAME = "V2Y";
//
//		private final short JTEXTFIELD_V1X = 1;
//		private final short JTEXTFIELD_V1Y = 2;
//		private final short JTEXTFIELD_V2X = 3;
//		private final short JTEXTFIELD_V2Y = 4;
//
//		private JPanel jPanelVertex1 = null;
//		private JPanel jPanelVertex2 = null;
//		private JPanel jPanelVertex1X = null;
//		private JPanel jPanelVertex1Y = null;
//		private JPanel jPanelVertex2X = null;
//		private JPanel jPanelVertex2Y = null;
//
//		private JLabel jLabelVertex1 = null;
//		private JLabel jLabelVertex2 = null;
//		private JLabel jLabelVertex1X = null;
//		private JLabel jLabelVertex1Y = null;
//		private JLabel jLabelVertex2X = null;
//		private JLabel jLabelVertex2Y = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex1X = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex1Y = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex2X = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex2Y = null;
//		private JButton jButtonPreviewArea = null;
//		private String last_Coordinates[];
//		private boolean hasChanged_v1X;
//		private boolean hasChanged_v1Y;
//		private boolean hasChanged_v2X;
//		private boolean hasChanged_v2Y;
//		private String previous_Coordinate_Value[];
//
//		private short current_coordinate_with_focus;
//
//		private FocusListener focusListenerForCoordinateValidation = null;
//		private KeyListener keyListenerForCoordinateValidation = null;
//
//		/**
//		 * This is the default constructor
//		 */
//		public AreaCoordinatesPanel() {
//			super();
//			initialize();
//		}
//
//		/**
//		 * This method initializes this
//		 *
//		 * @return void
//		 */
//		private void initialize() {
//			this.setLayout(null);
//			this.setBounds(DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE);
//			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));
//			this.setToolTipText(PluginServices.getText(this, "select_by_vertexes_coordinates"));
//
//			this.last_Coordinates = new String[4];
//			this.last_Coordinates[0] = "";
//			this.last_Coordinates[1] = "";
//			this.last_Coordinates[2] = "";
//			this.last_Coordinates[3] = "";
//			this.previous_Coordinate_Value = new String[4];
//
//			// By default no coordinates have changed
//			this.resetCoordinatesFlags();
//
//			this.add(getJPanelVertex1());
//			this.add(getJPanelVertex2());
//			this.add(getJButtonPreviewArea());
//		}
//
//		/**
//		 * Returns the default width of this component
//		 *
//		 * @return The default width
//		 */
//		public int getDefaultWidth() {
//			return DEFAULT_COORDINATE_WIDTH * 2;
//		}
//
//		/**
//		 * Returns the default height of this component
//		 *
//		 * @return The default height
//		 */
//		public int getDefaultHeight() {
//			return DEFAULT_COORDINATE_HEIGHT * 2;
//		}
//
//		/**
//		 * Sets all flags about if a coordinate has change to false (haven't changed)
//		 */
//		private void resetCoordinatesFlags() {
//			hasChanged_v1X = false;
//			hasChanged_v1Y = false;
//			hasChanged_v2X = false;
//			hasChanged_v2Y = false;
//		}
//
//		/**last_Coordinated_that_Changed
//		 * This method initializes jPanelVertex1
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1() {
//			if (jPanelVertex1 == null) {
//				jPanelVertex1 = new JPanel();
//				jPanelVertex1.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
//				jPanelVertex1.setLayout(new GridBagLayout());
//				jPanelVertex1.add(getJLabelVertex1(), null);
//				jPanelVertex1.add(getJPanelVertex1X(), null);
//				jPanelVertex1.add(getJPanelVertex1Y(), null);
//			}
//
//			return jPanelVertex1;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2() {
//			if (jPanelVertex2 == null) {
//				jPanelVertex2 = new JPanel();
//				jPanelVertex2.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION + DEFAULT_COORDINATE_HEIGHT, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
//				jPanelVertex2.setLayout(new GridBagLayout());
//				jPanelVertex2.add(getJLabelVertex2(), null);
//				jPanelVertex2.add(getJPanelVertex2X(), null);
//				jPanelVertex2.add(getJPanelVertex2Y(), null);
//			}
//
//			return jPanelVertex2;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JLabel getJLabelVertex1() {
//			if (jLabelVertex1 == null) {
//				jLabelVertex1 = new JLabel();
//				jLabelVertex1.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
//				jLabelVertex1.setToolTipText(PluginServices.getText(this, "coordinates_from_area_right_up_vertex"));
//				jLabelVertex1.setText(PluginServices.getText(this, "vertex") + " 1:");
//				jLabelVertex1.setHorizontalAlignment(SwingConstants.LEFT);
//				jLabelVertex1.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//			return jLabelVertex1;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JLabel getJLabelVertex2() {
//			if (jLabelVertex2 == null) {
//				jLabelVertex2 = new JLabel();
//				jLabelVertex2.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
//				jLabelVertex2.setToolTipText(PluginServices.getText(this, "coordinates_from_area_left_bottom_vertex"));
//				jLabelVertex2.setText(PluginServices.getText(this, "vertex") + " 2:");
//				jLabelVertex2.setHorizontalAlignment(SwingConstants.LEFT);
//				jLabelVertex2.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//			return jLabelVertex2;
//		}
//
//		/**
//		 * This method initializes jPanelVertex1X
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1X() {
//			if (jPanelVertex1X == null) {
//				jPanelVertex1X = new JPanel();
//				jPanelVertex1X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex1X.setLayout(new GridBagLayout());
//				jPanelVertex1X.add(getJLabelVertex1X(), null);
//				jPanelVertex1X.add(getJTextFieldVertex1X(), null);
//			}
//
//			return jPanelVertex1X;
//		}
//
//		/**
//		 * This method initializes jPanelVertex1Y
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1Y() {
//			if (jPanelVertex1Y == null) {
//				jPanelVertex1Y = new JPanel();
//				jPanelVertex1Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex1Y.setLayout(new GridBagLayout());
//				jPanelVertex1Y.add(getJLabelVertex1Y(), null);
//				jPanelVertex1Y.add(getJTextFieldVertex1Y(), null);
//			}
//
//			return jPanelVertex1Y;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2X
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2X() {
//			if (jPanelVertex2X == null) {
//				jPanelVertex2X = new JPanel();
//				jPanelVertex2X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex2X.setLayout(new GridBagLayout());
//				jPanelVertex2X.add(getJLabelVertex2X(), null);
//				jPanelVertex2X.add(getJTextFieldVertex2X(), null);
//			}
//
//			return jPanelVertex2X;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2Y
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2Y() {
//			if (jPanelVertex2Y == null) {
//				jPanelVertex2Y = new JPanel();
//				jPanelVertex2Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex2Y.setLayout(new GridBagLayout());
//				jPanelVertex2Y.add(getJLabelVertex2Y(), null);
//				jPanelVertex2Y.add(getJTextFieldVertex2Y(), null);
//			}
//
//			return jPanelVertex2Y;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1X
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex1X() {
//			if (jLabelVertex1X == null) {
//				jLabelVertex1X = new JLabel();
//				jLabelVertex1X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
//				jLabelVertex1X.setText(PluginServices.getText(this, "x") + ":");
//				jLabelVertex1X.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex1X.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1X.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//
//			return jLabelVertex1X;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2X
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex2X() {
//			if (jLabelVertex2X == null) {
//				jLabelVertex2X = new JLabel();
//				jLabelVertex2X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
//				jLabelVertex2X.setText(PluginServices.getText(this, "x") + ":");
//				jLabelVertex2X.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex2X.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2X.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//
//			return jLabelVertex2X;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1Y
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex1Y() {
//			if (jLabelVertex1Y == null) {
//				jLabelVertex1Y = new JLabel();
//				jLabelVertex1Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
//				jLabelVertex1Y.setText(PluginServices.getText(this, "y") + ":");
//				jLabelVertex1Y.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex1Y.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1Y.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//
//			return jLabelVertex1Y;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2Y
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex2Y() {
//			if (jLabelVertex2Y == null) {
//				jLabelVertex2Y = new JLabel();
//				jLabelVertex2Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
//				jLabelVertex2Y.setText(PluginServices.getText(this, "y") + ":");
//				jLabelVertex2Y.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex2Y.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2Y.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//
//			return jLabelVertex2Y;
//		}
//
//		/**
//		 * Returns a focus listener for validate the text of a JTextField when that component loses its focus
//		 *
//		 * @return java.awt.event.FocusListener
//		 */
//		private FocusListener getFocusListenerForCoordinateValidation() {
//			if (focusListenerForCoordinateValidation == null) {
//				focusListenerForCoordinateValidation = new FocusListener() {
//
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
//					 */
//					public void focusGained(FocusEvent e) {
//						JTextField jTF = (JTextField)e.getSource();
//
//						// Stores which coordinate has gotten the focus
//						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V1X;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V1Y;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V2X;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V2Y;
//
//							return;
//						}
//
//					}
//
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
//					 */
//					public void focusLost(FocusEvent e) {
//						JTextField jTF = (JTextField)e.getSource();
//
//						// If a coordinate has lost focus, is valid and has changed -> store the new value and set its related 'hasChanged' flag to true
//						String text = jTF.getText();
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {
//
//							if (text.compareTo(last_Coordinates[0]) != 0) {
//								hasChanged_v1X = true;
//
////								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
////								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[1]) != 0) {
//								hasChanged_v1Y = true;
//
////								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
////								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[2]) != 0) {
//								hasChanged_v2X = true;
//
////								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
////								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[3]) != 0) {
//								hasChanged_v2Y = true;
//
////								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
////								}
//							}
//
//							return;
//						}
//					}
//				};
//			}
//
//			return focusListenerForCoordinateValidation;
//		}
//
//		private KeyListener getKeyListenerForCoordinateValidation() {
//			if (keyListenerForCoordinateValidation == null) {
//				keyListenerForCoordinateValidation = new KeyListener() {
//
//					public void keyPressed(KeyEvent e) {
//					}
//
//					public void keyReleased(KeyEvent e) {
//						validateCoordinate(current_coordinate_with_focus);
//					}
//
//					public void keyTyped(KeyEvent e) {
//					}
//				};
//			}
//
//			return keyListenerForCoordinateValidation;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex1X
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex1X() {
//			if (jTextFieldVertex1X == null) {
//				jTextFieldVertex1X = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex1X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
//				jTextFieldVertex1X.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex1X.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex1X.setName(JTEXTFIELD_V1X_NAME);
//			}
//			return jTextFieldVertex1X;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex1Y
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex1Y() {
//			if (jTextFieldVertex1Y == null) {
//				jTextFieldVertex1Y = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex1Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
//				jTextFieldVertex1Y.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex1Y.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex1Y.setName(JTEXTFIELD_V1Y_NAME);
//			}
//			return jTextFieldVertex1Y;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex2X
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex2X() {
//			if (jTextFieldVertex2X == null) {
//				jTextFieldVertex2X = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex2X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
//				jTextFieldVertex2X.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex2X.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex2X.setName(JTEXTFIELD_V2X_NAME);
//			}
//			return jTextFieldVertex2X;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex2Y
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex2Y() {
//			if (jTextFieldVertex2Y == null) {
//				jTextFieldVertex2Y = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex2Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
//				jTextFieldVertex2Y.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex2Y.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex2Y.setName(JTEXTFIELD_V2Y_NAME);
//			}
//			return jTextFieldVertex2Y;
//		}
//
//		/**
//		 * Retuns the double value of the text field V1X
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV1X() {
//			return Double.parseDouble(getJTextFieldVertex1X().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V1Y
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV1Y() {
//			return Double.parseDouble(getJTextFieldVertex1Y().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V2X
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV2X() {
//			return Double.parseDouble(getJTextFieldVertex2X().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V2Y
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV2Y() {
//			return Double.parseDouble(getJTextFieldVertex2Y().getText());
//		}
//
//		/**
//		 * Retuns the double value of the last text field V1X value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV1XValue() {
//			return Double.parseDouble(last_Coordinates[0]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V1Y value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV1YValue() {
//			return Double.parseDouble(last_Coordinates[1]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V2X value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV2XValue() {
//			return Double.parseDouble(last_Coordinates[2]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V2Y value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV2YValue() {
//			return Double.parseDouble(last_Coordinates[3]);
//		}
//
//		/**
//		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
//		 *
//		 * @param coordinate javax.swing.JTextField
//		 * @return A boolean value
//		 */
//		private boolean validateCoordinate(JTextField coordinate) {
//			if (coordinate != null) {
//				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
//				if ((coordinate.getText().compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(coordinate.getText()))) {
////					JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
////					switch (last_Coordinate_that_Loose_the_Focus) {
////						case 1:
////							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
////							break;
////						case 2:
////							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
////							break;
////						case 3:
////							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
////							break;
////						case 4:
////							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
////							break;
////						default:
////							// Do nothing
////					}
//
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
//
//			return false; // If coordinate is null
//		}
//
//		/**
//		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
//		 *
//		 * @param coordinate javax.swing.JTextField
//		 * @return A boolean value
//		 */
//		private boolean validateCoordinate(int coordinate_ID) {
//			JTextField coordinate = null;
//			String text;
//
//			switch (coordinate_ID) {
//				case JTEXTFIELD_V1X:
//					coordinate = getJTextFieldVertex1X();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[0]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[0] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V1Y:
//					coordinate = getJTextFieldVertex1Y();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[1]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[1] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V2X:
//					coordinate = getJTextFieldVertex2X();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[2]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[2] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V2Y:
//					coordinate = getJTextFieldVertex2Y();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[3]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[3] = text;
//						return true;
//					}
//
////					break;
//				default:
//					return true; // Avoid problems
//			}
//
////			if (coordinate != null) {
////				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
////					switch (coordinate_ID) {
////						case 1:
////							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
////							break;
////						case 2:
////							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
////							break;
////						case 3:
////							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
////							break;
////						case 4:
////							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
////							break;
////						default:
////							// Do nothing
////					}
//
////					return false;
////				}
////				else
////					return true;
//////			}
////
////			return false; // If coordinate is null
//		}
//
//		/**
//		 * This method initializes jButtonPreviewArea
//		 *
//		 * @return javax.swing.JButton
//		 */
//		private JButton getJButtonPreviewArea() {
//			if (jButtonPreviewArea == null) {
//				jButtonPreviewArea = new JButton();
//				jButtonPreviewArea.setBounds(DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE);
////				jButtonPreviewArea.setToolTipText(PluginServices.getText(this, "to_previsualize"));
//				jButtonPreviewArea.setToolTipText(PluginServices.getText(this, "to_validate_coodinates"));
//				jButtonPreviewArea.setVerticalTextPosition(AbstractButton.CENTER);
//				jButtonPreviewArea.setHorizontalTextPosition(AbstractButton.CENTER);
//				jButtonPreviewArea.setIcon(PluginServices.getIconTheme().get("view-previsualize-area"));
//				jButtonPreviewArea.addMouseListener(new MouseAdapter() {
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//					 */
//					public void mouseClicked(MouseEvent e) {
//						validVertexes();
//					}
//
////					/*
////					 *  (non-Javadoc)
////					 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
////					 */
////					public void mouseClicked(MouseEvent e) {
////				 		// Only enable the area operation if there is data loaded in this MapControl
////
////						// Do nothing if there is no layer in the map control
////				 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() == 0) {
////				 			return;
////				 		}
////
////						if ((validateCoordinate(getJTextFieldVertex1X())) && (validateCoordinate(getJTextFieldVertex1Y())) && (validateCoordinate(getJTextFieldVertex2X())) && (validateCoordinate(getJTextFieldVertex2Y()))) {
////							ViewPort vP = getSelectableMapAreaPanel().getViewPort();
////							if (vP != null) {
////								// If has to scale the area
////								if (getJToggleButtonScaling().isSelected()) {
////									// Scale Vertex 1
////									if ((hasChanged_v1X) || (hasChanged_v1Y)) {
////										double cx = getDoubleValueOfJTextFieldV1X();
////										double cy = getDoubleValueOfJTextFieldV1Y();
////										double sx, sy, aux;
////
////										if (hasChanged_v1X) {
////											aux = getDoubleValueOfLastJTextFieldV1XValue();
////											if (aux == 0.0)
////												sx = 1.0; // Don't scale if new coordenate is zero
////											else
////												sx = cx / aux;
////										}
////										else
////											sx = 1.0;
////
////										if (hasChanged_v1Y) {
////											aux = getDoubleValueOfLastJTextFieldV1YValue();
////											if (aux == 0.0)
////												sy = 1.0; // Don't scale if new coordenate is zero
////											else
////												sy = cy / aux;
////										}
////										else
////											sy = 1.0;
////
////										if (sx == 1.0) {
////											// It's supposed that sy != 1.0
////											cx *= sy;
////
////											if (cx < getDoubleValueOfJTextFieldV2X()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												restoreAllModifiedCoordinates(); // restores the coordinates
////												return;
////											}
////
////											getJTextFieldVertex1X().setText(Double.toString(cx));
////										}
////										else {
////											if (sy == 1.0) {
////												// It's supposed that sx != 1.0
////												cy *= sx;
////
////												if (cy < getDoubleValueOfJTextFieldV2Y()) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////
////												getJTextFieldVertex1Y().setText(Double.toString(cy));
////											}
////											else {
////												// If there has been an error -> can't move different distances in X the two vertexes
////												if (sx != sy) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////											}
////										}
////									}
////
////									// Scale Vertex2
////									if ((hasChanged_v2X) || (hasChanged_v2Y)) {
////										double cx = getDoubleValueOfJTextFieldV2X();
////										double cy = getDoubleValueOfJTextFieldV2Y();
////										double sx, sy, aux;
////
////										if (hasChanged_v2X) {
////											aux = getDoubleValueOfLastJTextFieldV2XValue();
////											if (aux == 0.0)
////												sx = 1.0; // Don't scale if new coordenate is zero
////											else
////												sx = cx / aux;
////										}
////										else
////											sx = 1.0;
////
////										if (hasChanged_v2Y) {
////											aux = getDoubleValueOfLastJTextFieldV2YValue();
////											if (aux == 0.0)
////												sy = 1.0; // Don't scale if new coordenate is zero
////											else
////												sy = cy / aux;
////										}
////										else
////											sy = 1.0;
////
////										if (sx == 1.0) {
////											// It's supposed that sy != 1.0
////											cx *= sy;
////
////											if (cx > getDoubleValueOfJTextFieldV1X()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												restoreAllModifiedCoordinates(); // restores the coordinates
////												return;
////											}
////
////											getJTextFieldVertex2X().setText(Double.toString(cx));
////										}
////										else {
////											if (sy == 1.0) {
////												// It's supposed that sx != 1.0
////												cy *= sx;
////
////												if (cy > getDoubleValueOfJTextFieldV1Y()) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////
////												getJTextFieldVertex2Y().setText(Double.toString(cy));
////											}
////											else {
////												// If there has been an error -> can't move different distances in X the two vertexes
////												if (sx != sy) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////											}
////										}
////									}
////								}
////								else {
////									// If has to move the area
////									if (getJToggleButtonMove().isSelected()) {
////										// Move in X
////										if ((hasChanged_v1X) || (hasChanged_v2X)) {
////											double c1 = getDoubleValueOfJTextFieldV1X();
////											double c2 = getDoubleValueOfJTextFieldV2X();
////											double d1, d2;
////
////											if (hasChanged_v1X)
////												d1 = c1 - getDoubleValueOfLastJTextFieldV1XValue();
////											else
////												d1 = 0.0;
////
////											if (hasChanged_v2X)
////												d2 = c2 - getDoubleValueOfLastJTextFieldV2XValue();
////											else
////												d2 = 0.0;
////
////											if (d1 == 0.0) {
////												// It's supposed that d2 != 0
////												c1 += d2;
////
////												if (c1 < getDoubleValueOfJTextFieldV2X()) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////
////												getJTextFieldVertex1X().setText(Double.toString(c1));
////											}
////											else {
////												if (d2 == 0.0) {
////													// It's supposed that d1 != 0
////													c2 += d1;
////
////													if (c2 > getDoubleValueOfJTextFieldV1X()) {
////														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////														restoreAllModifiedCoordinates(); // restores the coordinates
////														return;
////													}
////
////													getJTextFieldVertex2X().setText(Double.toString(c2));
////												}
////												else {
////													// If there has been an error -> can't move different distances in X the two vertexes
////													if (d1 != d2) {
////														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_X"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////														restoreAllModifiedCoordinates(); // restores the coordinates
////														return;
////													}
////												}
////											}
////										}
////
////										// Move in Y
////										if ((hasChanged_v1Y) || (hasChanged_v2Y)) {
////											double c1 = getDoubleValueOfJTextFieldV1Y();
////											double c2 = getDoubleValueOfJTextFieldV2Y();
////											double d1, d2;
////
////											if (hasChanged_v1Y)
////												d1 = c1 - getDoubleValueOfLastJTextFieldV1YValue();
////											else
////												d1 = 0.0;
////
////											if (hasChanged_v2Y)
////												d2 = c2 - getDoubleValueOfLastJTextFieldV2YValue();
////											else
////												d2 = 0.0;
////
////											if (d1 == 0.0) {
////												// It's supposed that d2 != 0
////												c1 += d2;
////
////												if (c1 < getDoubleValueOfJTextFieldV2Y()) {
////													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////													restoreAllModifiedCoordinates(); // restores the coordinates
////													return;
////												}
////
////												getJTextFieldVertex1Y().setText(Double.toString(c1));
////											}
////											else {
////												if (d2 == 0.0) {
////													// It's supposed that d1 != 0
////													c2 += d1;
////
////													if (c2 > getDoubleValueOfJTextFieldV1Y()) {
////														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////														restoreAllModifiedCoordinates(); // restores the coordinates
////														return;
////													}
////
////													getJTextFieldVertex2Y().setText(Double.toString(c2));
////												}
////												else {
////													// If there has been an error -> can't move different distances in Y the two vertexes
////													if (d1 != d2) {
////														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_Y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////														restoreAllModifiedCoordinates(); // restores the coordinates
////														return;
////													}
////												}
////											}
////										}
////									}
////									else {
////										boolean canSetExtent = true;
////
////										// Check if there is a impossible coordinate
////										if (hasChanged_v1X) {
////											if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												canSetExtent = false;
////											}
////										}
////
////										if (hasChanged_v1Y) {
////											if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												canSetExtent = false;
////											}
////										}
////
////										if (hasChanged_v2X) {
////											if (getDoubleValueOfJTextFieldV2X() > getDoubleValueOfJTextFieldV1X()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												canSetExtent = false;
////											}
////										}
////
////										if (hasChanged_v2Y) {
////											if (getDoubleValueOfJTextFieldV2Y() > getDoubleValueOfJTextFieldV1Y()) {
////												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////												canSetExtent = false;
////											}
////										}
////
////										// If can set the new Extent -> finish
////										if (!canSetExtent) {
////											restoreAllModifiedCoordinates(); // restores the coordinates
////											return;
////										}
////									}
////								}
////
////								//Rectangle2D r2d = vP.getExtent();
////								if (vP.getExtent() != null){
////									vP.setExtent(getNewRectangleByCoordinates());
////									vP.refreshExtent();
////								}
////							}
////						}
////						else {
////							JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "at_least_one_incorrect_coordinate"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
////						}
////					}
//				});
//			}
//			return jButtonPreviewArea;
//		}
//
//		/**
//		 * Calculates the new rectangle using the coordinates of the text fields
//		 *
//		 * @return java.awt.geom.Rectangle2D
//		 */
//		private Rectangle2D getNewRectangleByCoordinates() {
//			return new Rectangle2D.Double(getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV2Y(), getDoubleValueOfJTextFieldV1X() - getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV1Y() - getDoubleValueOfJTextFieldV2Y());
//		}
//
//		/**
//		 * Restores the value of all text fields with coordinates modified to their last value
//		 */
//		private void restoreAllModifiedCoordinates() {
//			if (hasChanged_v1X) {
//				getJTextFieldVertex1X().setText(last_Coordinates[0]);
//				hasChanged_v1X = false;
//			}
//
//			if (hasChanged_v1Y) {
//				getJTextFieldVertex1Y().setText(last_Coordinates[1]);
//				hasChanged_v1Y = false;
//			}
//
//			if (hasChanged_v2X) {
//				getJTextFieldVertex2X().setText(last_Coordinates[2]);
//				hasChanged_v2X = false;
//			}
//
//			if (hasChanged_v2Y) {
//				getJTextFieldVertex2Y().setText(last_Coordinates[3]);
//				hasChanged_v2Y = false;
//			}
//		}
//
//		/**
//		 * Enables or disables all inner JTextField
//		 *
//		 * @param b A boolean value
//		 */
//		public void setAllTextFieldsEnabled(boolean b) {
//			getJTextFieldVertex1X().setEnabled(b);
//			getJTextFieldVertex1Y().setEnabled(b);
//			getJTextFieldVertex2X().setEnabled(b);
//			getJTextFieldVertex2Y().setEnabled(b);
//		}
//
//		/**
//		 * Sets the extent into the text fields
//		 *
//		 * @param extent java.awt.geom.Rectangle2D
//		 */
//		public void updateCoordinates(Rectangle2D extent){
//			getJTextFieldVertex1X().setText(String.valueOf(extent.getMaxX()));
//			last_Coordinates[0] = getJTextFieldVertex1X().getText();
//			previous_Coordinate_Value[0] = last_Coordinates[0];
//
//			getJTextFieldVertex1Y().setText(String.valueOf(extent.getMaxY()));
//			last_Coordinates[1] = getJTextFieldVertex1Y().getText();
//			previous_Coordinate_Value[1] = last_Coordinates[1];
//
//			getJTextFieldVertex2X().setText(String.valueOf(extent.getMinX()));
//			last_Coordinates[2] = getJTextFieldVertex2X().getText();
//			previous_Coordinate_Value[2] = last_Coordinates[2];
//
//			getJTextFieldVertex2Y().setText(String.valueOf(extent.getMinY()));
//			last_Coordinates[3] = getJTextFieldVertex2Y().getText();
//			previous_Coordinate_Value[3] = last_Coordinates[3];
//
//			resetCoordinatesFlags();
//
//			// Enable the 'Applicate' button
//			setApplicable(true);
//		}
//
//		/**
//		 * Validates all coordinates of both vertexes separately
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesCoordinates() {
//			if (! validateCoordinate(getJTextFieldVertex1X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex1Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here, all coordinates are valid
//			return new ValidationInfo(true, "");
//		}
//
//		/**
//		 * Validates that Vertex1 is in the right-up corner and the Vertex2 in the left-down corner of the area/rectangle <br>
//		 * (It's supposed that coordinates of the vertexes are valid) <br>
//		 * (It's supposed that going to right or up, the coordinate value increases)
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesPositions() {
//			// Check if no coordinate it's defined
//			if (getJTextFieldVertex1X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (getJTextFieldVertex2X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getJTextFieldVertex1Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (getJTextFieldVertex2Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// Check if the vertex V1 is in the right-up corner and the vertex V2 is in the left-down corner
//			if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1x") + " < " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1y") + " < " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here -> vertexes positions are valid
//			return new ValidationInfo(true, "");
//		}
//
//		/**
//		 * Validate each coordinate of each vertex and the position of the vertexes
//		 *
//		 * @return A boolean value
//		 */
//		public boolean validVertexes() {
//			ValidationInfo v1 = validVertexesCoordinates();
//
//			if (!v1.isValid()) {
//				// Notify to user that no filter area will be applied
//				JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v1.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);
//
//				return false;
//			}
//			else {
//				ValidationInfo v2 = validVertexesPositions();
//				if (!v2.isValid()) {
//					// Notify to user that no filter area will be applied
//					JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v2.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);
//
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
//		}
//
//		/**
//		 * Gets the extent (rectangle) represented by the values of the coordinate text fields
//		 *
//		 * @return java.awt.geom.Rectangle2D
//		 */
//		public Rectangle2D getExtent() {
//			double v1x = getDoubleValueOfJTextFieldV1X();
//			double v1y = getDoubleValueOfJTextFieldV1Y();
//			double v2x = getDoubleValueOfJTextFieldV2X();
//			double v2y = getDoubleValueOfJTextFieldV2Y();
//
//			return new Rectangle2D.Double(v2x, v2y, (v1x - v2x), (v1y  - v2y));
//		}
//
//		/**
//		 * Returns true if there is some coordinate text field with data; else returns false
//		 *
//		 * @return A boolean value
//		 */
//		public boolean areThereSomeCoordinatesWritten() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") != 0) | (getJTextFieldVertex1Y().getText().compareTo("") != 0) | (getJTextFieldVertex2X().getText().compareTo("") != 0) | (getJTextFieldVertex2Y().getText().compareTo("") != 0));
//		}
//
//		/**
//		 * Returns true if all coordinate text fields are undefined (without values)
//		 *
//		 * @return A boolean value
//		 */
//		public boolean areAllCoordinatesUndefined() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) & (getJTextFieldVertex1Y().getText().compareTo("") == 0) & (getJTextFieldVertex2X().getText().compareTo("") == 0) & (getJTextFieldVertex2Y().getText().compareTo("") == 0));
//		}
//
//
//		/**
//		 * Returns true if there is some coordinate undefined (without values)
//		 *
//		 * @return A boolean value
//		 */
//		public boolean isThereAnyCoordinateUndefined() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) | (getJTextFieldVertex1Y().getText().compareTo("") == 0) | (getJTextFieldVertex2X().getText().compareTo("") == 0) | (getJTextFieldVertex2Y().getText().compareTo("") == 0));
//		}
//	}
//
//	/**
//	 * This class is a MapControl JComponent that has visual information and allows user interact with some tools and view the results
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 */
//	private class SelectableMapControlAreaPanel extends MapControl {
//		private MouseListener[] mouseListeners;
//		private MouseWheelListener[] mouseWheelListeners;
//		private MouseMotionListener[] mouseMotionListeners;
//
//		/**
//		 * Default constructor
//		 */
//		public SelectableMapControlAreaPanel() {
//			super();
//			initialize();
//		}
//
//		/**
//		 * This method initializes this component
//		 */
//		public void initialize() {
//			/* Sets Bounds of this graphical component */
//			this.setBounds(DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE);
//
//			/* Sets border to this graphical component */
//			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));
//
//			/* Sets a clone of the current active view map context to this MapControl */
//			try {
//				MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();
//
//				this.setMapContext(mp.cloneFMap());
//				ViewPort vP = this.getViewPort();
//
//				Rectangle2D r2D = vP.getExtent();
//
//				if (r2D != null) {
//					vP.refreshExtent();
//					getCoordinatesPanel().updateCoordinates(r2D);
//				}
//			} catch (XMLException e) {
//				e.printStackTrace();
//			}
//
//			/* Adds listeners to this MapControl */
//			this.addToolsListeners();
//
//			/* Sets default tool */
//			this.setTool(PAN_TOOL);
//		}
//
//		/**
//		 * Refresh the active view
//		 */
//		public void refreshWithTheActiveView() {
//			try {
//				MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();
//				this.setMapContext(mp.cloneFMap());
//				ViewPort vP = this.getViewPort();
//
//				Rectangle2D r2D = vP.getExtent();
//
//				if (r2D != null) {
//					vP.refreshExtent();
//					getCoordinatesPanel().updateCoordinates(r2D);
//				}
//			} catch (XMLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		/**
//		 * Adds listeners that allows user interact with it's mouse over this component and that represents different tools
//		 */
//		private void addToolsListeners() {
//			// MOVEMENT EVENTS LISTENER: sets mouse coordinates to the status bar
//	        StatusBarListener sbl = new StatusBarListener(this);
//
//	        // ZOOM OUT (Presses on the map and it will be centered showing a bigger area)
//	        // Only pressing, not selecting a rectangle area
//	        ZoomOutListener zol = new ZoomOutListener(this);
//	        this.addMapTool(ZOOM_OUT_TOOL, new Behavior[]{new PointBehavior(zol), new MouseMovementBehavior(sbl)});
//
//	        // ZOOM IN (Using a Rectangle or a simple mouse click)
//	        ZoomOutRightButtonListener zoil = new ZoomOutRightButtonListener(this);
//	        ZoomInListener zil = new ZoomInListener(this);
//	        this.addMapTool(ZOOM_IN_TOOL, new Behavior[]{new RectangleBehavior(zil),
//	        				new PointBehavior(zoil), new MouseMovementBehavior(sbl)});
//
//	        // PAN
//	        PanListener pl = new PanListener(this);
//	        this.addMapTool(PAN_TOOL, new Behavior[]{new MoveBehavior(pl), new MouseMovementBehavior(sbl)});
//
//	        // MEASURE DISTANCES
//	        MeasureListener mli = new MeasureListener(this);
//	        this.addMapTool(MEASURE_DISTANCES_TOOL, new Behavior[]{new PolylineBehavior(mli), new MouseMovementBehavior(sbl)});
//
//	        // MEASURE AREA
//	        AreaListener ali = new AreaListener(this);
//	        this.addMapTool(MEASURE_AREA_TOOL, new Behavior[]{new PolygonBehavior(ali), new MouseMovementBehavior(sbl)});
//
//	        this.getViewPort().addViewPortListener(new ViewPortListener() {
//	        	/*
//	        	 *  (non-Javadoc)
//	        	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
//	        	 */
//				public void extentChanged(ExtentEvent e) {
//					if (getMapContext().getViewPort().getExtents().hasPrevious()) {
//						Rectangle2D r2d = getViewPort().getExtent();
//
//						if (r2d != null) {
//							getCoordinatesPanel().updateCoordinates(r2d);
//
////							if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//								hasUserDefinedAnArea = true;
////							}
//						}
//
//						getJButtonZoomPrevious().setEnabled(true);
//						getJButtonUndo().setEnabled(true);
//					}
//					else {
//						getJButtonZoomPrevious().setEnabled(false);
//						getJButtonUndo().setEnabled(false);
//					}
//				}
//
//				/*
//				 *  (non-Javadoc)
//				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
//				 */
//				public void backColorChanged(ColorEvent e) {
//				}
//
//				/*
//				 *  (non-Javadoc)
//				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#projectionChanged(com.iver.cit.gvsig.fmap.ProjectionEvent)
//				 */
//				public void projectionChanged(ProjectionEvent e) {
//				}
//
//	        });
//		}
//
//		/*
//		 *  (non-Javadoc)
//		 * @see java.awt.Component#setEnabled(boolean)
//		 */
//		public void setEnabled(boolean b) {
//			super.setEnabled(b);
//
//			if (b)
//				enableAllMouseListeners();
//			else
//				disableAllMouseListeners();
//		}
//
//		/**
//		 * Disables all mouse listeners
//		 */
//		public void disableAllMouseListeners() {
//			int i;
//
//			// Only disable listeners if there are listeners to remove (it's supposed that there are always the same number of mouse listeners, and this listeners
//			//   are referenciated by this component or by the private attribute 'mouseListeners')
//
//			// Mouse Button Listeners
//			if (mouseListeners == null) {
//				mouseListeners = this.getMouseListeners();
//
//				for (i = 0; i < mouseListeners.length; i++) {
//					removeMouseListener(mouseListeners[i]);
//				}
//			}
//
//			// Mouse Wheel Listeners
//			if (mouseWheelListeners == null) {
//				mouseWheelListeners = this.getMouseWheelListeners();
//
//				for (i = 0; i < mouseWheelListeners.length; i++) {
//					removeMouseWheelListener(mouseWheelListeners[i]);
//				}
//			}
//
//			// Mouse motion listeners
//			if (mouseMotionListeners == null) {
//				mouseMotionListeners = this.getMouseMotionListeners();
//
//				for (i = 0; i < mouseMotionListeners.length; i++) {
//					removeMouseMotionListener(mouseMotionListeners[i]);
//				}
//			}
//		}
//
//		/**
//		 * Enables all mouse listeners
//		 */
//		public void enableAllMouseListeners() {
//			int i;
//
//			// Mouse Button Listeners
//			for (i = 0; i < mouseListeners.length; i++) {
//				addMouseListener(mouseListeners[i]);
//			}
//
//			mouseListeners = null;
//
//			// Mouse Wheel Listeners
//			for (i = 0; i < mouseWheelListeners.length; i++) {
//				addMouseWheelListener(mouseWheelListeners[i]);
//			}
//
//			mouseWheelListeners = null;
//
//			// Mouse motion listeners
//			for (i = 0; i < mouseMotionListeners.length; i++) {
//				addMouseMotionListener(mouseMotionListeners[i]);
//			}
//
//			mouseMotionListeners = null;
//		}
//	}
//
//	/**
//	 * This class has information about a validation: <br>
//	 *   - A boolean value -> if has been or not validated <br>
//	 *   - An String -> a message about the invalid
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 */
//	private class ValidationInfo {
//		private boolean _valid;
//		private String _message;
//
//		/**
//		 * Default constructor with two parameters
//		 *
//		 * @param valid If was valid or not
//		 * @param message A message associate to the validation (If has been valid, this attribute should be "", in the other case should have a message with
//		 *                  an explanation about the invalidation)
//		 */
//		public ValidationInfo(boolean valid, String message) {
//			_valid = valid;
//			_message = message;
//		}
//
//		/**
//		 * Returns the value of the inner attribute '_valid'
//		 *
//		 * @return A boolean value
//		 */
//		public boolean isValid() {
//			return _valid;
//		}
//
//		/**
//		 * Returns the value of the inner attribute '_message'
//		 *
//		 * @return java.lang.String
//		 */
//		public String getMessage() {
//			return _message;
//		}
//	}
//}


//
//  VERSIÓN PREVIA -> ESTA ES LA VERSIÓN BUENA, PERO HAY QUE EVITAR QUE CLONE MAPCONTROL
//
//
///**
// * This panel allows user to select the area he/she wants to get in the view.
// * There are two options to do this:
// *  - Indicating the coordinates of the top-left and down-right corners
// *  - Selecting the area with some visual tool
// *
// * @author Pablo Piqueras Bartolomï¿½ (p_queras@hotmail.com)
// * @author Jorge Piera Llodrï¿½ (piera_jor@gva.es)
// */
//public class WFSAreaPanel extends JPanel {
//	private final Rectangle DEFAULT_BOUNDS = new Rectangle(10, 5, 490, 380);
//	private final Rectangle DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE = new Rectangle(8, 20, 481, 60);
//	private final Rectangle DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE = new Rectangle(8, 115, 481, 265);
//	private final Rectangle DEFAULT_UNDO_ICON_BUTTON_RECTANGLE = new Rectangle (250, 85, 25, 25);
////	private final Rectangle DEFAULT_REDO_ICON_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
////	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
////	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (331, 85, 25, 25);
//	private final Rectangle DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (277, 85, 25, 25);
//	private final Rectangle DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE = new Rectangle (304, 85, 25, 25);
//	private final Rectangle DEFAULT_PAN_ICON_BUTTON_RECTANGLE = new Rectangle(250, 85, 25, 25);
//	private final Rectangle DEFAULT_TOOL_JCOMBOBOX_RECTANGLE = new Rectangle(10, 87, 230, 21);
//	private final Rectangle DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE = new Rectangle(277, 85, 47, 25);
//	private final Rectangle DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE = new Rectangle(326, 85, 25, 25);
//	private final Rectangle DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE = new Rectangle(353, 85, 25, 25);
//	private final Rectangle DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE = new Rectangle(380, 85, 47, 25);
//	private final Rectangle DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE = new Rectangle(429, 85, 47, 25);
//
//	private final int DISABLED_OPERATION = 0;
//	private final int SELECTION_BY_COORDINATES_OPERATION = 1;
//	private final int SELECTION_BY_AREA_OPERATION = 2;
//	private boolean hasUserDefinedAnArea;
//
//	private WFSWizardData data = null;
//	private WFSParamsPanel parent = null;
//	private AreaCoordinatesPanel coordinatesPanel = null;
//	private SelectableMapControlAreaPanel selectableMapAreaPanel = null;
//	private JComboBox jComboBoxToolSelection = null;
//	private JButton jButtonUndo = null;
////	private JButton jButtonRedo = null;
//	private JToggleButton jToggleButtonMove = null;
//	private JToggleButton jToggleButtonScaling = null;
//	private JButton jButtonPan = null;
//	private JButton jButtonZoomPrevious = null;
//	private JButton jButtonZoomComplete = null;
//	private JComboBoxWithImageIconItems jComboBoxZoomInAndOut = null;
//	private JComboBoxWithImageIconItems jComboBoxZoomInAndOutViewCentered = null;
//	private JComboBoxWithImageIconItems jComboBoxOtherTools = null;
//
//	/* Tool identifier constants */
//	private final String PAN_TOOL = "HAND";
//	private final String ZOOM_IN_TOOL = "zoomIn"; // This constant must be 'zoomIn' for a correct operation of the tools 'Zoom In' and 'Zoom In Map Centered'
//	private final String ZOOM_OUT_TOOL = "zoomOut"; // This constant must be 'zoomOut' for a correct operation of the tools 'Zoom Out' and 'Zoom Out Map Centered'
//	private final String ZOOM_IN_VIEW_CENTERED_TOOL = "ZOOM_IN_VIEW_CENTERED";
//	private final String ZOOM_OUT_VIEW_CENTERED_TOOL = "ZOOM_OUT_VIEW_CENTERED";
//	private final String MEASURE_DISTANCES_TOOL = "MEASURE_DISTANCES";
//	private final String MEASURE_AREA_TOOL = "MEASURE_AREA";
//	/* End tool identifier constants */
//
//
//	/**
//	 * This method initializes
//	 */
//	public WFSAreaPanel(WFSParamsPanel parent) {
//		super();
//		this.parent = parent;
//		initialize();
//	}
//
//	/**
//	 * This method initializes this
//	 */
//	private void initialize() {
//		this.setLayout(null);
//		this.setBounds(DEFAULT_BOUNDS);
//		this.setBorder(javax.swing.BorderFactory.createTitledBorder(
//				null, PluginServices.getText(this, "select_by_area"),
//				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
//
//		// By default, user hasn't defined an area
//		this.hasUserDefinedAnArea = false;
//
//		// Adds JPanel with the coordinates
//		this.add(getCoordinatesPanel(), null);
//
//		// Adds JComboBox to select a part of the graphical interface
//		this.add(getJComboBoxToolSelection(), null);
//		this.getJComboBoxToolSelection().setSelectedIndex(0); // By default select first element
//
//		// Adds JPanel with the view
//		this.add(getSelectableMapAreaPanel(), null);
//
//		initCoordinates();
//	}
//
//	/**
//	 * Write the view coordinates into the coordinates panel
//	 */
//	private void initCoordinates(){
//		BaseView activeView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//		Rectangle2D r2d = activeView.getMapControl().getViewPort().getExtent();
//		if (r2d != null){
//			getCoordinatesPanel().getJTextFieldVertex1X().setText(Double.toString(r2d.getMaxX()));
//			getCoordinatesPanel().getJTextFieldVertex1Y().setText(Double.toString(r2d.getMaxY()));
//			getCoordinatesPanel().getJTextFieldVertex2X().setText(Double.toString(r2d.getMinX()));
//			getCoordinatesPanel().getJTextFieldVertex2Y().setText(Double.toString(r2d.getMinY()));
//		}
//	}
//
//	/**
//	 * This method initializes coordinatesPanel
//	 *
//	 * @return javax.swing.JPanel
//	 */
//	private AreaCoordinatesPanel getCoordinatesPanel() {
//		if (coordinatesPanel == null) {
//			coordinatesPanel = new AreaCoordinatesPanel();
//		}
//		return coordinatesPanel;
//	}
//
//	/**
//	 * This method initializes jComboBoxToolSelection
//	 *
//	 * @return  javax.swing.JComboBox
//	 */
//	private JComboBox getJComboBoxToolSelection() {
//		if (jComboBoxToolSelection == null) {
//			jComboBoxToolSelection = new JComboBox();
//			jComboBoxToolSelection.setBounds(DEFAULT_TOOL_JCOMBOBOX_RECTANGLE);
//			jComboBoxToolSelection.setToolTipText(PluginServices.getText(this, "select_a_tool"));
//			jComboBoxToolSelection.setEditable(false);
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "disabled"), this.DISABLED_OPERATION));
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_absolute_coordinates"), this.SELECTION_BY_COORDINATES_OPERATION));
//			jComboBoxToolSelection.addItem(new ItemOperation(PluginServices.getText(this, "define_coordinates_using_view"), this.SELECTION_BY_AREA_OPERATION));
//
//			jComboBoxToolSelection.addActionListener(new ActionListener() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBox cb = (JComboBox)e.getSource();
//					switch(((ItemOperation)cb.getSelectedItem()).getOperation()) {
//						case DISABLED_OPERATION:
//					 		disableCoorderatesRegionOperation();
//					 		disableAreaRegionOperation();
//					 		break;
//					 	case SELECTION_BY_COORDINATES_OPERATION:
//					 		disableAreaRegionOperation();
//					 		enableCoorderatesRegionOperation();
//					 		break;
//					 	case SELECTION_BY_AREA_OPERATION:
//					 		// Only enable the area operation if there is data loaded in this MapControl
//					 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
//					 			disableCoorderatesRegionOperation();
//					 			enableAreaRegionOperation();
//					 		}
//					 		else {
//					 			JOptionPane.showMessageDialog(jComboBoxToolSelection, PluginServices.getText(null, "there_is_no_layer_loaded_in_the_active_view"), PluginServices.getText(null, "information"), JOptionPane.INFORMATION_MESSAGE);
//
//					 			// Select: no tools
//					 			jComboBoxToolSelection.setSelectedIndex(0);
//					 			disableCoorderatesRegionOperation();
//					 		}
//
//					 		break;
//					}
//				}
//			});
//		}
//		return jComboBoxToolSelection;
//	}
//
//	/**
//	 * Enables the components associated to the selection of area by coordinates
//	 */
//	private void enableCoorderatesRegionOperation() {
//		// Adds associated icons
//		this.add(getJButtonUndo(), null);
////		this.add(getJButtonRedo(), null);
//		this.add(getJToggleButtonMove(), null);
//		this.add(getJToggleButtonScaling(), null);
//
//		getCoordinatesPanel().setAllTextFieldsEnabled(true);
//
// 		// Only enable the area operation if there is data loaded in this MapControl
// 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
// 			getJToggleButtonMove().setEnabled(true);
// 			getJToggleButtonScaling().setEnabled(true);
// 			getCoordinatesPanel().getJButtonPreviewArea().setEnabled(true);
// 		}
// 		else {
// 			getJToggleButtonMove().setEnabled(false);
// 			getJToggleButtonScaling().setEnabled(false);
// 		}
//	}
//
//	/**
//	 * Enables the components associated to the selection of area by view
//	 */
//	private void enableAreaRegionOperation() {
//		// Adds associated icons
//		this.add(getJButtonPan(), null);
//		this.add(getJComboBoxZoomInAndOut(), null);
//		getJComboBoxZoomInAndOut().revalidate(); // Force update this component
//		this.add(getJButtonZoomPrevious(), null);
//		this.add(getJButtonZoomComplete(), null);
//		this.add(getJComboBoxZoomInAndOutViewCentered(), null);
//		getJComboBoxZoomInAndOutViewCentered().revalidate(); // Force update this component
//		this.add(getJComboBoxOtherTools(), null);
//		getJComboBoxOtherTools().revalidate(); // Force update this component
//
//		// Enables the MapControl area
//		getSelectableMapAreaPanel().setEnabled(true);
//	}
//
//	/**
//	 * Disables the components associated to the selection of area by coordinates
//	 */
//	private void disableCoorderatesRegionOperation() {
//		// Removes associated icons
//		this.remove(getJButtonUndo());
////		this.remove(getJButtonRedo());
//		this.remove(getJToggleButtonMove());
//		this.remove(getJToggleButtonScaling());
//
//		getCoordinatesPanel().setAllTextFieldsEnabled(false);
//		getCoordinatesPanel().getJButtonPreviewArea().setEnabled(false);
//	}
//
//	/**
//	 * Disables the components associated to the selection of area by view
//	 */
//	private void disableAreaRegionOperation() {
//		// Removes associated icons
//		this.remove(getJButtonPan());
//		this.remove(getJComboBoxZoomInAndOut());
//		this.remove(getJButtonZoomPrevious());
//		this.remove(getJButtonZoomComplete());
//		this.remove(getJComboBoxZoomInAndOutViewCentered());
//		this.remove(getJComboBoxOtherTools());
//
//		// Disables the MapControl area
//		getSelectableMapAreaPanel().setEnabled(false);
//	}
//
//	/**
//	 * This method initializes areaMapControlPanel
//	 *
//	 * @return A reference to an object of SelectableMapControlAreaPanel
//	 */
//	private SelectableMapControlAreaPanel getSelectableMapAreaPanel() {
//		if (selectableMapAreaPanel == null) {
//			selectableMapAreaPanel = new SelectableMapControlAreaPanel();
//		}
//
//		return selectableMapAreaPanel;
//	}
//
//	/**
//	 * This method initializes jButtonUndo
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonUndo() {
//		if (jButtonUndo == null) {
//			jButtonUndo = new JButton();
//			jButtonUndo.setBounds(DEFAULT_UNDO_ICON_BUTTON_RECTANGLE);
//			jButtonUndo.setIcon(PluginServices.getIconTheme().get("edit-undo"));
//			jButtonUndo.setToolTipText(PluginServices.getText(this, "previous_area"));
//			jButtonUndo.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					goToPreviousZoom();
//				}
//			});
//		}
//
//		return jButtonUndo;
//	}
//
////	/**
////	 * This method initializes jButtonUndo
////	 *
////	 * @return javax.swing.JButton
////	 */
////	private JButton getJButtonRedo() {
////		if (jButtonRedo == null) {
////			jButtonRedo = new JButton();
////			jButtonRedo.setBounds(DEFAULT_REDO_ICON_BUTTON_RECTANGLE);
////			jButtonRedo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/edit-redo.png")));
////			jButtonRedo.setToolTipText(PluginServices.getText(this, "following_area"));
////		}
////
////		return jButtonRedo;
////	}
//
//	/**
//	 * This method initializes jToggleButtonMove
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JToggleButton getJToggleButtonMove() {
//		if (jToggleButtonMove == null) {
//			jToggleButtonMove = new JToggleButton();
//			jToggleButtonMove.setBounds(DEFAULT_MOVE_ICON_TOGGLE_BUTTON_RECTANGLE);
//			jToggleButtonMove.setIcon(PluginServices.getIconTheme().get("WFS-move"));
//			jToggleButtonMove.setToolTipText(PluginServices.getText(this, "move") + ": " + PluginServices.getText(this, "area_move_explanation"));
//			jToggleButtonMove.addItemListener(new ItemListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
//				 */
//				public void itemStateChanged(ItemEvent e) {
//					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
//					if (jToggleButtonMove.isSelected())
//						jToggleButtonScaling.setSelected(false);
//				}
//			});
//		}
//
//		return jToggleButtonMove;
//	}
//
//	/**
//	 * This method initializes jToggleButtonScaling
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JToggleButton getJToggleButtonScaling() {
//		if (jToggleButtonScaling == null) {
//			jToggleButtonScaling = new JToggleButton();
//			jToggleButtonScaling.setBounds(DEFAULT_SCALING_ICON_TOGGLE_BUTTON_RECTANGLE);
//			jToggleButtonScaling.setIcon(PluginServices.getIconTheme().get("WFS-scaling"));
//			jToggleButtonScaling.setToolTipText(PluginServices.getText(this, "scaling") + ": " + PluginServices.getText(this, "area_scaling_explanation"));
//			jToggleButtonScaling.addItemListener(new ItemListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
//				 */
//				public void itemStateChanged(ItemEvent e) {
//					// Isn't allowed that the two JToggleButton would be selected (can't move and scale)
//					if (jToggleButtonScaling.isSelected())
//						jToggleButtonMove.setSelected(false);
//				}
//			});
//		}
//
//		return jToggleButtonScaling;
//	}
//	/**
//	 * This method initializes jButtonPan
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonPan() {
//		if (jButtonPan == null) {
//			jButtonPan = new JButton();
//			jButtonPan.setBounds(DEFAULT_PAN_ICON_BUTTON_RECTANGLE);
//			jButtonPan.setIcon(PluginServices.getIconTheme().get("view-pan"));
//			jButtonPan.setToolTipText(PluginServices.getText(this, "Desplazamiento"));
//			jButtonPan.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					getSelectableMapAreaPanel().setTool(PAN_TOOL);
//				}
//			});
//		}
//		return jButtonPan;
//	}
//
//	/**
//	 * This method initializes jComboBoxZoomInAndOut
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOut() {
//		if (jComboBoxZoomInAndOut == null) {
//			jComboBoxZoomInAndOut = new JComboBoxWithImageIconItems();
//			jComboBoxZoomInAndOut.setBounds(DEFAULT_ZOOMINANDOUT_JCOMBOBOX_RECTANGLE);
//			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomIn.png",PluginServices.getIconTheme().get("view-zoom-in"),PluginServices.getText(this, "Zoom_Mas"), ZOOM_IN_TOOL));
//			jComboBoxZoomInAndOut.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-out"), PluginServices.getText(this, "Zoom_Menos"), ZOOM_OUT_TOOL));
//			jComboBoxZoomInAndOut.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
//				}
//	        });
//		}
//
//		return jComboBoxZoomInAndOut;
//	}
//
//	/**
//	 * This method initializes jButtonZoomPrevious
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonZoomPrevious() {
//		if (jButtonZoomPrevious == null) {
//			jButtonZoomPrevious = new JButton();
//			jButtonZoomPrevious.setBounds(DEFAULT_ZOOMPREVIOUS_ICON_BUTTON_RECTANGLE);
//			jButtonZoomPrevious.setIcon(PluginServices.getIconTheme().get("view-zoom-back"));
//			jButtonZoomPrevious.setToolTipText(PluginServices.getText(this, "Zoom_Previo"));
//			jButtonZoomPrevious.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					goToPreviousZoom();
//				}
//			});
//		}
//		return jButtonZoomPrevious;
//	}
//
//	/**
//	 * This method initializes jButtonZoomComplete
//	 *
//	 * @return javax.swing.JButton
//	 */
//	private JButton getJButtonZoomComplete() {
//		if (jButtonZoomComplete == null) {
//			jButtonZoomComplete = new JButton();
//			jButtonZoomComplete.setBounds(DEFAULT_ZOOMCOMPLETE_ICON_BUTTON_RECTANGLE);
//			jButtonZoomComplete.setIcon(PluginServices.getIconTheme().get("view-zoom-map-contents"));
//			jButtonZoomComplete.setToolTipText(PluginServices.getText(this, "Zoom_Completo"));
//			jButtonZoomComplete.addMouseListener(new MouseAdapter() {
//				/*
//				 * (non-Javadoc)
//				 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//				 */
//				public void mouseClicked(MouseEvent e) {
//					getSelectableMapAreaPanel().getViewPort().setExtent(getSelectableMapAreaPanel().getMapContext().getLayers().getFullExtent());
//					BaseView view =((BaseView)PluginServices.getMDIManager().getActiveWindow());
//					view.repaintMap();
//				}
//			});
//		}
//		return jButtonZoomComplete;
//	}
//
//	/**
//	 * This method initializes jComboBoxZoomInAndOut
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxZoomInAndOutViewCentered() {
//		if (jComboBoxZoomInAndOutViewCentered == null) {
//			jComboBoxZoomInAndOutViewCentered = new JComboBoxWithImageIconItems();
//			jComboBoxZoomInAndOutViewCentered.setBounds(DEFAULT_ZOOMINANDOUTVIEWCENTERED_JCOMBOBOX_RECTANGLE);
//			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/zoommas.png",PluginServices.getIconTheme().get("view-zoom-mas"), PluginServices.getText(this, "Zoom_Acercar"), ZOOM_IN_VIEW_CENTERED_TOOL));
//			jComboBoxZoomInAndOutViewCentered.addImageIconItem(new ImageIconItemInfo("images/ZoomOut.png",PluginServices.getIconTheme().get("view-zoom-menos"), PluginServices.getText(this, "Zoom_Alejar"), ZOOM_OUT_VIEW_CENTERED_TOOL));
//			jComboBoxZoomInAndOutViewCentered.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//
//					if (((String)iiii.getItemValue()).compareTo(ZOOM_IN_VIEW_CENTERED_TOOL) == 0) {
//						getSelectableMapAreaPanel().zoomIn();
//					}
//					else {
//						getSelectableMapAreaPanel().zoomOut();
//					}
//				}
//	        });
//		}
//		return jComboBoxZoomInAndOutViewCentered;
//	}
//
//	/**
//	 * This method initializes jComboBoxOtherTools
//	 *
//	 * @return A JComboBoxWithImageIconItems object reference
//	 */
//	private JComboBoxWithImageIconItems getJComboBoxOtherTools() {
//		if (jComboBoxOtherTools == null) {
//			jComboBoxOtherTools = new JComboBoxWithImageIconItems();
//			jComboBoxOtherTools.setBounds(DEFAULT_OTHER_TOOLS_JCOMBOBOX_RECTANGLE);
//			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Distancia.png",PluginServices.getIconTheme().get("view-query-distance"),PluginServices.getText(this, "medir_distancias"), MEASURE_DISTANCES_TOOL));
//			jComboBoxOtherTools.addImageIconItem(new ImageIconItemInfo("images/Poligono16.png",PluginServices.getIconTheme().get("view-query-area"), PluginServices.getText(this, "medir_area"), MEASURE_AREA_TOOL));
//			jComboBoxOtherTools.addActionListener(new ActionListener() {
//				/*
//				 *  (non-Javadoc)
//				 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
//				 */
//				public void actionPerformed(ActionEvent e) {
//					JComboBoxWithImageIconItems cb = (JComboBoxWithImageIconItems)e.getSource();
//					ImageIconItemInfo iiii = (ImageIconItemInfo)cb.getSelectedItem();
//					getSelectableMapAreaPanel().setTool((String)iiii.getItemValue());
//				}
//	        });
//		}
//
//		return jComboBoxOtherTools;
//	}
//
//	/**
//	 * Goes to previous zoom
//	 */
//	private void goToPreviousZoom() {
//		// Gets the view port and sets its previous extent
//		ViewPort vp = getSelectableMapAreaPanel().getMapContext().getViewPort();
//
//		if (vp.getExtents().hasPrevious()) {
//			vp.setPreviousExtent();
//			getCoordinatesPanel().updateCoordinates(vp.getExtent());
//		}
//	}
//
//	/**
//	 * Sets the extent
//	 *
//	 * @param java.awt.geom.Rectangle2D
//	 */
//	public void setExtent(Rectangle2D extent) {
//		if (extent != null) {
//			// Update coordinates in text fields
//			getCoordinatesPanel().updateCoordinates(extent);
//
//			// If there is any layer loaded in the MapControl -> set the extent
//			if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() > 0) {
//				getSelectableMapAreaPanel().getMapContext().getViewPort().setExtent(extent);
//			}
//		}
//	}
//
//	/**
//	 * Gets the extent: a 'Rectangle2D.Double' object reference or 'null' if there is no data or if the data is invalid or user hasn't defined an area
//	 *
//	 * @return java.awt.geom.Rectangle2D or null if the data is invalid
//	 */
//	public Rectangle2D getExtent() {
//		if (!hasUserDefinedAnArea)
//			return null;
//
//		if (getCoordinatesPanel().areAllCoordinatesUndefined()) {
//			return null; // Return null if there is no coordinate
//		}
//		else {
//			if (getCoordinatesPanel().validVertexes()) {
//				return getCoordinatesPanel().getExtent();
//			}
//			else {
//				return null; // Returns null if coordinates aren't valid
//			}
//		}
//	}
//
//	/**
//	 * Set parent's 'Applicable' button enable or disable according the value of the parameter
//	 *
//	 * @param b A boolean value
//	 */
//	private void setApplicable(boolean b) {
//		if (!parent.getWFSFilterPanelIsAsTabForWFSLayersLoad())
//			parent.isApplicable(true);
//	}
//
//	/**
//	 * If there is some coordinate text field that has text, returns true; else returns false
//	 *
//	 * @return A boolean value
//	 */
//	public boolean areThereSomeCoordinatesWritten() {
//		return getCoordinatesPanel().areThereSomeCoordinatesWritten();
//	}
//
//	/**
//	 * If there is some coordinate text field that isn't undefined, returns true; else returns false
//	 *
//	 * @return A boolean value
//	 */
//	public boolean areThereSomeCoordinatesUndefined() {
//		return getCoordinatesPanel().isThereAnyCoordinateUndefined();
//	}
//
//	/**
//	 * If user has or hasn't defined an area
//	 *
//	 * @return A boolean value
//	 */
//	public boolean hasUserDefinedAnArea() {
//		return hasUserDefinedAnArea;
//	}
//
//	/**
//	 * Restores the inner attribute 'hasUserDefinedAnArea' to its default value (false)
//	 */
//	public void setUserHasntDefineAnArea() {
//		hasUserDefinedAnArea = false;
//	}
//
//	/**
//	 * Updates the current area information with the area of the active view
//	 */
//	public void updateWFSArea() {
//		this.getSelectableMapAreaPanel().disableAllMouseListeners();
//
//		// To prevent that events that take place (are produced) could be a nuisance to the load of the layers
//		this.getSelectableMapAreaPanel().getMapContext().beginAtomicEvent();
//
//		try {
//			MapContext mapContext = this.getSelectableMapAreaPanel().getMapContext();
//
//			// Removes all layers in the view area
//			int numberOfLayers = mapContext.getLayers().getLayersCount();
//			int i;
//
//			for (i = (numberOfLayers-1); i >= 0; i--) {
//				mapContext.getLayers().removeLayer(i);
//			}
//
//			// Adds the extent of the viewport of the current active view
//			BaseView view = (BaseView) PluginServices.getMDIManager().getActiveWindow();
//			ViewPort vP = view.getMapControl().getMapContext().getViewPort();
//
//			// Update extent
//			Rectangle2D r2D = vP.getExtent();
//
//			if (r2D == null) {
//				// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//				this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//				// No tools enabled
//				getJComboBoxToolSelection().setSelectedIndex(0);
//
//				return;
//			}
//
//			getCoordinatesPanel().updateCoordinates(r2D);
//			mapContext.getViewPort().setExtent(r2D);
//
//			// Adds the active layers of the current active view
//			MapContext mC = view.getMapControl().getMapContext();
//
//			numberOfLayers = mC.getLayers().getLayersCount();
//
//			for (i = (numberOfLayers-1); i >= 0; i--) {
//				mapContext.getLayers().addLayer(mC.getLayers().getLayer(i).cloneLayer());
//			}
//
//			// If has to enable all listeners of all tools on the view area
//			if (((ItemOperation)this.getJComboBoxToolSelection().getSelectedItem()).getOperation() == SELECTION_BY_AREA_OPERATION)
//				this.getSelectableMapAreaPanel().enableAllMouseListeners();
//
//			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//			// Refresh the view
//			this.getSelectableMapAreaPanel().getViewPort().refreshExtent();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//			// End To prevent that events that take place(are produced) could be a nuisance to the load of the layers
//			this.getSelectableMapAreaPanel().getMapContext().endAtomicEvent();
//
//			// No tools enabled
//			getJComboBoxToolSelection().setSelectedIndex(0);
//			return;
//		}
//	}
//
//	/**
//	 * Represents an object that stores the necessary information for know each operation of the 'jComboBoxToolSelection'
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras@hotmail.com)
//	 */
//	private class ItemOperation {
//		String _name;
//		int _operation;
//
//		/**
//		 * Default constructor with two parameters
//		 *
//		 * @param name Name of the operation
//		 * @param operation A code that identifies the operation
//		 */
//		public ItemOperation(String name, int operation) {
//			_name = new String(name);
//			_operation = operation;
//		}
//
//		/**
//		 * Returns the name of the operation
//		 *
//		 * @return An String
//		 */
//		public String getName() {
//			return _name;
//		}
//
//		/**
//		 * Returns the code that identifies the operation
//		 *
//		 * @return An integer value
//		 */
//		public int getOperation() {
//			return _operation;
//		}
//
//		/**
//		 * The name of the operation that will use JComboBox
//		 */
//		public String toString() {
//			return _name;
//		}
//	}
//
//	/**
//	 * This class is a panel width four fields for indicate the coordinates of two points:
//	 *   One for the ritgh-up point and another for the left-down point of a rectangle area
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 * @author Jorge Piera Llodrï¿½ (piera_jor@gva.es)
//	 */
//	private class AreaCoordinatesPanel extends JPanel {
//		private final Rectangle DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE = new Rectangle(412, 5, 62, 51);  //  @jve:decl-index=0:
//		private final int DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT = 20;
//		private final int DEFAULT_COORDIANTE_LABEL_WIDTH = 20;
//		private final int DEFAULT_TEXT_FIELDS_WIDTH = 132;
//		private final int DEFAULT_X_LOCATION = 8;
//		private final int DEFAULT_Y_LEFT_UP_CORNER_LOCATION = 6;
//		private final int DEFAULT_COORDINATE_WIDTH = DEFAULT_COORDIANTE_LABEL_WIDTH + DEFAULT_TEXT_FIELDS_WIDTH + 10;
//		private final int DEFAULT_COORDINATE_HEIGHT = 25;
//		private final Dimension DEFAULT_JLABEL_VERTEX_DIMENSION = new Dimension(60, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT);  //  @jve:decl-index=0:
//		private final int DEFAULT_XY_COORDINATES_WIDTH = 2 * DEFAULT_COORDINATE_WIDTH + DEFAULT_JLABEL_VERTEX_DIMENSION.width + 10 ;
//
//		private final String JTEXTFIELD_V1X_NAME = "V1X";
//		private final String JTEXTFIELD_V1Y_NAME = "V1Y";
//		private final String JTEXTFIELD_V2X_NAME = "V2X";
//		private final String JTEXTFIELD_V2Y_NAME = "V2Y";
//
//		private final short JTEXTFIELD_V1X = 1;
//		private final short JTEXTFIELD_V1Y = 2;
//		private final short JTEXTFIELD_V2X = 3;
//		private final short JTEXTFIELD_V2Y = 4;
//
//		private JPanel jPanelVertex1 = null;
//		private JPanel jPanelVertex2 = null;
//		private JPanel jPanelVertex1X = null;
//		private JPanel jPanelVertex1Y = null;
//		private JPanel jPanelVertex2X = null;
//		private JPanel jPanelVertex2Y = null;
//
//		private JLabel jLabelVertex1 = null;
//		private JLabel jLabelVertex2 = null;
//		private JLabel jLabelVertex1X = null;
//		private JLabel jLabelVertex1Y = null;
//		private JLabel jLabelVertex2X = null;
//		private JLabel jLabelVertex2Y = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex1X = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex1Y = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex2X = null;
//		private JTextFieldWithSpecificCaretPosition jTextFieldVertex2Y = null;
//		private JButton jButtonPreviewArea = null;
//		private String last_Coordinates[];
//		private boolean hasChanged_v1X;
//		private boolean hasChanged_v1Y;
//		private boolean hasChanged_v2X;
//		private boolean hasChanged_v2Y;
//		private String previous_Coordinate_Value[];
//
//		private short current_coordinate_with_focus;
//
//		private FocusListener focusListenerForCoordinateValidation = null;
//		private KeyListener keyListenerForCoordinateValidation = null;
//
//		/**
//		 * This is the default constructor
//		 */
//		public AreaCoordinatesPanel() {
//			super();
//			initialize();
//		}
//
//		/**
//		 * This method initializes this
//		 *
//		 * @return void
//		 */
//		private void initialize() {
//			this.setLayout(null);
//			this.setBounds(DEFAULT_AREA_COORDINATES_PANEL_RECTANGLE);
//			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));
//			this.setToolTipText(PluginServices.getText(this, "select_by_vertexes_coordinates"));
//
//			this.last_Coordinates = new String[4];
//			this.last_Coordinates[0] = "";
//			this.last_Coordinates[1] = "";
//			this.last_Coordinates[2] = "";
//			this.last_Coordinates[3] = "";
//			this.previous_Coordinate_Value = new String[4];
//
//			// By default no coordinates have changed
//			this.resetCoordinatesFlags();
//
//			this.add(getJPanelVertex1());
//			this.add(getJPanelVertex2());
//			this.add(getJButtonPreviewArea());
//		}
//
//		/**
//		 * Returns the default width of this component
//		 *
//		 * @return The default width
//		 */
//		public int getDefaultWidth() {
//			return DEFAULT_COORDINATE_WIDTH * 2;
//		}
//
//		/**
//		 * Returns the default height of this component
//		 *
//		 * @return The default height
//		 */
//		public int getDefaultHeight() {
//			return DEFAULT_COORDINATE_HEIGHT * 2;
//		}
//
//		/**
//		 * Sets all flags about if a coordinate has change to false (haven't changed)
//		 */
//		private void resetCoordinatesFlags() {
//			hasChanged_v1X = false;
//			hasChanged_v1Y = false;
//			hasChanged_v2X = false;
//			hasChanged_v2Y = false;
//		}
//
//		/**last_Coordinated_that_Changed
//		 * This method initializes jPanelVertex1
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1() {
//			if (jPanelVertex1 == null) {
//				jPanelVertex1 = new JPanel();
//				jPanelVertex1.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
//				jPanelVertex1.setLayout(new GridBagLayout());
//				jPanelVertex1.add(getJLabelVertex1(), null);
//				jPanelVertex1.add(getJPanelVertex1X(), null);
//				jPanelVertex1.add(getJPanelVertex1Y(), null);
//			}
//
//			return jPanelVertex1;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2() {
//			if (jPanelVertex2 == null) {
//				jPanelVertex2 = new JPanel();
//				jPanelVertex2.setBounds(DEFAULT_X_LOCATION, DEFAULT_Y_LEFT_UP_CORNER_LOCATION + DEFAULT_COORDINATE_HEIGHT, DEFAULT_XY_COORDINATES_WIDTH, DEFAULT_COORDINATE_HEIGHT);
//				jPanelVertex2.setLayout(new GridBagLayout());
//				jPanelVertex2.add(getJLabelVertex2(), null);
//				jPanelVertex2.add(getJPanelVertex2X(), null);
//				jPanelVertex2.add(getJPanelVertex2Y(), null);
//			}
//
//			return jPanelVertex2;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JLabel getJLabelVertex1() {
//			if (jLabelVertex1 == null) {
//				jLabelVertex1 = new JLabel();
//				jLabelVertex1.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
//				jLabelVertex1.setToolTipText(PluginServices.getText(this, "coordinates_from_area_right_up_vertex"));
//				jLabelVertex1.setText(PluginServices.getText(this, "vertex") + " 1:");
//				jLabelVertex1.setHorizontalAlignment(SwingConstants.LEFT);
//				jLabelVertex1.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//			return jLabelVertex1;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JLabel getJLabelVertex2() {
//			if (jLabelVertex2 == null) {
//				jLabelVertex2 = new JLabel();
//				jLabelVertex2.setPreferredSize(DEFAULT_JLABEL_VERTEX_DIMENSION);
//				jLabelVertex2.setToolTipText(PluginServices.getText(this, "coordinates_from_area_left_bottom_vertex"));
//				jLabelVertex2.setText(PluginServices.getText(this, "vertex") + " 2:");
//				jLabelVertex2.setHorizontalAlignment(SwingConstants.LEFT);
//				jLabelVertex2.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//			return jLabelVertex2;
//		}
//
//		/**
//		 * This method initializes jPanelVertex1X
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1X() {
//			if (jPanelVertex1X == null) {
//				jPanelVertex1X = new JPanel();
//				jPanelVertex1X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex1X.setLayout(new GridBagLayout());
//				jPanelVertex1X.add(getJLabelVertex1X(), null);
//				jPanelVertex1X.add(getJTextFieldVertex1X(), null);
//			}
//
//			return jPanelVertex1X;
//		}
//
//		/**
//		 * This method initializes jPanelVertex1Y
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex1Y() {
//			if (jPanelVertex1Y == null) {
//				jPanelVertex1Y = new JPanel();
//				jPanelVertex1Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex1Y.setLayout(new GridBagLayout());
//				jPanelVertex1Y.add(getJLabelVertex1Y(), null);
//				jPanelVertex1Y.add(getJTextFieldVertex1Y(), null);
//			}
//
//			return jPanelVertex1Y;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2X
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2X() {
//			if (jPanelVertex2X == null) {
//				jPanelVertex2X = new JPanel();
//				jPanelVertex2X.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex2X.setLayout(new GridBagLayout());
//				jPanelVertex2X.add(getJLabelVertex2X(), null);
//				jPanelVertex2X.add(getJTextFieldVertex2X(), null);
//			}
//
//			return jPanelVertex2X;
//		}
//
//		/**
//		 * This method initializes jPanelVertex2Y
//		 *
//		 * @return javax.swing.JPanel
//		 */
//		private JPanel getJPanelVertex2Y() {
//			if (jPanelVertex2Y == null) {
//				jPanelVertex2Y = new JPanel();
//				jPanelVertex2Y.setPreferredSize(new Dimension(new Dimension(DEFAULT_COORDINATE_WIDTH, DEFAULT_COORDINATE_HEIGHT)));
//				jPanelVertex2Y.setLayout(new GridBagLayout());
//				jPanelVertex2Y.add(getJLabelVertex2Y(), null);
//				jPanelVertex2Y.add(getJTextFieldVertex2Y(), null);
//			}
//
//			return jPanelVertex2Y;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1X
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex1X() {
//			if (jLabelVertex1X == null) {
//				jLabelVertex1X = new JLabel();
//				jLabelVertex1X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
//				jLabelVertex1X.setText(PluginServices.getText(this, "x") + ":");
//				jLabelVertex1X.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex1X.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1X.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//
//			return jLabelVertex1X;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2X
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex2X() {
//			if (jLabelVertex2X == null) {
//				jLabelVertex2X = new JLabel();
//				jLabelVertex2X.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
//				jLabelVertex2X.setText(PluginServices.getText(this, "x") + ":");
//				jLabelVertex2X.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex2X.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2X.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//
//			return jLabelVertex2X;
//		}
//
//		/**
//		 * This method initializes jLabelVertex1Y
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex1Y() {
//			if (jLabelVertex1Y == null) {
//				jLabelVertex1Y = new JLabel();
//				jLabelVertex1Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
//				jLabelVertex1Y.setText(PluginServices.getText(this, "y") + ":");
//				jLabelVertex1Y.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex1Y.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex1Y.setForeground(new Color(0, 0, 255)); // Blue color for text
//			}
//
//			return jLabelVertex1Y;
//		}
//
//		/**
//		 * This method initializes jLabelVertex2Y
//		 *
//		 * @return  javax.swing.JLabel
//		 */
//		private JLabel getJLabelVertex2Y() {
//			if (jLabelVertex2Y == null) {
//				jLabelVertex2Y = new JLabel();
//				jLabelVertex2Y.setPreferredSize(new java.awt.Dimension(DEFAULT_COORDIANTE_LABEL_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jLabelVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
//				jLabelVertex2Y.setText(PluginServices.getText(this, "y") + ":");
//				jLabelVertex2Y.setHorizontalAlignment(SwingConstants.CENTER);
//				jLabelVertex2Y.setVerticalAlignment(SwingConstants.CENTER);
//				jLabelVertex2Y.setForeground(new Color(128, 64, 0)); // Brown color for text
//			}
//
//			return jLabelVertex2Y;
//		}
//
//		/**
//		 * Returns a focus listener for validate the text of a JTextField when that component loses its focus
//		 *
//		 * @return java.awt.event.FocusListener
//		 */
//		private FocusListener getFocusListenerForCoordinateValidation() {
//			if (focusListenerForCoordinateValidation == null) {
//				focusListenerForCoordinateValidation = new FocusListener() {
//
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
//					 */
//					public void focusGained(FocusEvent e) {
//						JTextField jTF = (JTextField)e.getSource();
//
//						// Stores which coordinate has gotten the focus
//						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V1X;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V1Y;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V2X;
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
//							current_coordinate_with_focus = JTEXTFIELD_V2Y;
//
//							return;
//						}
//
//					}
//
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
//					 */
//					public void focusLost(FocusEvent e) {
//						JTextField jTF = (JTextField)e.getSource();
//
//						// If a coordinate has lost focus, is valid and has changed -> store the new value and set its related 'hasChanged' flag to true
//						String text = jTF.getText();
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1X_NAME) == 0) {
//
//							if (text.compareTo(last_Coordinates[0]) != 0) {
//								hasChanged_v1X = true;
//
//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
//								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V1Y_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[1]) != 0) {
//								hasChanged_v1Y = true;
//
//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
//								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2X_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[2]) != 0) {
//								hasChanged_v2X = true;
//
//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
//								}
//							}
//
//							return;
//						}
//
//						if (jTF.getName().compareTo(JTEXTFIELD_V2Y_NAME) == 0) {
//							if (text.compareTo(last_Coordinates[3]) != 0) {
//								hasChanged_v2Y = true;
//
//								if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//									hasUserDefinedAnArea = true;
//								}
//							}
//
//							return;
//						}
//					}
//				};
//			}
//
//			return focusListenerForCoordinateValidation;
//		}
//
//		private KeyListener getKeyListenerForCoordinateValidation() {
//			if (keyListenerForCoordinateValidation == null) {
//				keyListenerForCoordinateValidation = new KeyListener() {
//
//					public void keyPressed(KeyEvent e) {
//					}
//
//					public void keyReleased(KeyEvent e) {
//						validateCoordinate(current_coordinate_with_focus);
//					}
//
//					public void keyTyped(KeyEvent e) {
//					}
//				};
//			}
//
//			return keyListenerForCoordinateValidation;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex1X
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex1X() {
//			if (jTextFieldVertex1X == null) {
//				jTextFieldVertex1X = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex1X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex1X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_right_up_vertex"));
//				jTextFieldVertex1X.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex1X.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex1X.setName(JTEXTFIELD_V1X_NAME);
//			}
//			return jTextFieldVertex1X;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex1Y
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex1Y() {
//			if (jTextFieldVertex1Y == null) {
//				jTextFieldVertex1Y = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex1Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex1Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_right_up_vertex"));
//				jTextFieldVertex1Y.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex1Y.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex1Y.setName(JTEXTFIELD_V1Y_NAME);
//			}
//			return jTextFieldVertex1Y;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex2X
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex2X() {
//			if (jTextFieldVertex2X == null) {
//				jTextFieldVertex2X = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex2X.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex2X.setToolTipText(PluginServices.getText(this, "x_coordinate_from_area_left_bottom_vertex"));
//				jTextFieldVertex2X.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex2X.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex2X.setName(JTEXTFIELD_V2X_NAME);
//			}
//			return jTextFieldVertex2X;
//		}
//
//		/**
//		 * This method initializes jTextFieldVertex2Y
//		 *
//		 * @return JTextFieldWithSpecificCaretPosition
//		 */
//		private JTextFieldWithSpecificCaretPosition getJTextFieldVertex2Y() {
//			if (jTextFieldVertex2Y == null) {
//				jTextFieldVertex2Y = new JTextFieldWithSpecificCaretPosition("");
//				jTextFieldVertex2Y.setPreferredSize(new Dimension(DEFAULT_TEXT_FIELDS_WIDTH, DEFAULT_LABELS_AND_TEXT_FIELDS_HEIGHT));
//				jTextFieldVertex2Y.setToolTipText(PluginServices.getText(this, "y_coordinate_from_area_left_bottom_vertex"));
//				jTextFieldVertex2Y.addFocusListener(getFocusListenerForCoordinateValidation());
//				jTextFieldVertex2Y.addKeyListener(getKeyListenerForCoordinateValidation());
//				jTextFieldVertex2Y.setName(JTEXTFIELD_V2Y_NAME);
//			}
//			return jTextFieldVertex2Y;
//		}
//
//		/**
//		 * Retuns the double value of the text field V1X
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV1X() {
//			return Double.parseDouble(getJTextFieldVertex1X().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V1Y
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV1Y() {
//			return Double.parseDouble(getJTextFieldVertex1Y().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V2X
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV2X() {
//			return Double.parseDouble(getJTextFieldVertex2X().getText());
//		}
//
//		/**
//		 * Retuns the double value of the text field V2Y
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfJTextFieldV2Y() {
//			return Double.parseDouble(getJTextFieldVertex2Y().getText());
//		}
//
//		/**
//		 * Retuns the double value of the last text field V1X value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV1XValue() {
//			return Double.parseDouble(last_Coordinates[0]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V1Y value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV1YValue() {
//			return Double.parseDouble(last_Coordinates[1]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V2X value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV2XValue() {
//			return Double.parseDouble(last_Coordinates[2]);
//		}
//
//		/**
//		 * Retuns the double value of the last text field V2Y value
//		 *
//		 * @return A double number type
//		 */
//		public double getDoubleValueOfLastJTextFieldV2YValue() {
//			return Double.parseDouble(last_Coordinates[3]);
//		}
//
//		/**
//		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
//		 *
//		 * @param coordinate javax.swing.JTextField
//		 * @return A boolean value
//		 */
//		private boolean validateCoordinate(JTextField coordinate) {
//			if (coordinate != null) {
//				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
//				if ((coordinate.getText().compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(coordinate.getText()))) {
////					JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
////					switch (last_Coordinate_that_Loose_the_Focus) {
////						case 1:
////							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
////							break;
////						case 2:
////							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
////							break;
////						case 3:
////							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
////							break;
////						case 4:
////							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
////							break;
////						default:
////							// Do nothing
////					}
//
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
//
//			return false; // If coordinate is null
//		}
//
//		/**
//		 * Validates the coordinate of a JTextField: returns true if it's valid; else returns false
//		 *
//		 * @param coordinate javax.swing.JTextField
//		 * @return A boolean value
//		 */
//		private boolean validateCoordinate(int coordinate_ID) {
//			JTextField coordinate = null;
//			String text;
//
//			switch (coordinate_ID) {
//				case JTEXTFIELD_V1X:
//					coordinate = getJTextFieldVertex1X();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[0]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[0] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V1Y:
//					coordinate = getJTextFieldVertex1Y();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[1]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[1] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V2X:
//					coordinate = getJTextFieldVertex2X();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[2]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[2] = text;
//						return true;
//					}
//
////					break;
//				case JTEXTFIELD_V2Y:
//					coordinate = getJTextFieldVertex2Y();
//					text = coordinate.getText();
//
//					if ((text.compareTo("") != 0) && (! StringNumberUtilities.isRealNumberWithIntegerExponent(text))) {
//						JOptionPane.showMessageDialog(this, PluginServices.getText(null, "incorrect_coordinate_format"), PluginServices.getText(null, "error"), JOptionPane.ERROR_MESSAGE);
//						coordinate.setText(previous_Coordinate_Value[3]); // Set last value
//						return false;
//					}
//					else {
//						previous_Coordinate_Value[3] = text;
//						return true;
//					}
//
////					break;
//				default:
//					return true; // Avoid problems
//			}
//
////			if (coordinate != null) {
////				// If the format of the coordinate is incorrect, shows a message warning the user that problem and resets the coordinate text
////					switch (coordinate_ID) {
////						case 1:
////							coordinate.setText(previous_Coordinate_Value[0]); // Set last value
////							break;
////						case 2:
////							coordinate.setText(previous_Coordinate_Value[1]); // Set last value
////							break;
////						case 3:
////							coordinate.setText(previous_Coordinate_Value[2]); // Set last value
////							break;
////						case 4:
////							coordinate.setText(previous_Coordinate_Value[3]); // Set last value
////							break;
////						default:
////							// Do nothing
////					}
//
////					return false;
////				}
////				else
////					return true;
//////			}
////
////			return false; // If coordinate is null
//		}
//
//		/**
//		 * This method initializes jButtonPreviewArea
//		 *
//		 * @return javax.swing.JButton
//		 */
//		private JButton getJButtonPreviewArea() {
//			if (jButtonPreviewArea == null) {
//				jButtonPreviewArea = new JButton();
//				jButtonPreviewArea.setBounds(DEFAULT_JBUTTON_GO_TO_AREA_RECTANGLE);
//				jButtonPreviewArea.setToolTipText(PluginServices.getText(this, "to_previsualize"));
//				jButtonPreviewArea.setVerticalTextPosition(AbstractButton.CENTER);
//				jButtonPreviewArea.setHorizontalTextPosition(AbstractButton.CENTER);
//				jButtonPreviewArea.setIcon(PluginServices.getIconTheme().get("view-previsualize-area"));
//				jButtonPreviewArea.addMouseListener(new MouseAdapter() {
//					/*
//					 *  (non-Javadoc)
//					 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
//					 */
//					public void mouseClicked(MouseEvent e) {
//				 		// Only enable the area operation if there is data loaded in this MapControl
//
//						// Do nothing if there is no layer in the map control
//				 		if (getSelectableMapAreaPanel().getMapContext().getLayers().getLayersCount() == 0) {
//				 			return;
//				 		}
//
//						if ((validateCoordinate(getJTextFieldVertex1X())) && (validateCoordinate(getJTextFieldVertex1Y())) && (validateCoordinate(getJTextFieldVertex2X())) && (validateCoordinate(getJTextFieldVertex2Y()))) {
//							ViewPort vP = getSelectableMapAreaPanel().getViewPort();
//							if (vP != null) {
//								// If has to scale the area
//								if (getJToggleButtonScaling().isSelected()) {
//									// Scale Vertex 1
//									if ((hasChanged_v1X) || (hasChanged_v1Y)) {
//										double cx = getDoubleValueOfJTextFieldV1X();
//										double cy = getDoubleValueOfJTextFieldV1Y();
//										double sx, sy, aux;
//
//										if (hasChanged_v1X) {
//											aux = getDoubleValueOfLastJTextFieldV1XValue();
//											if (aux == 0.0)
//												sx = 1.0; // Don't scale if new coordenate is zero
//											else
//												sx = cx / aux;
//										}
//										else
//											sx = 1.0;
//
//										if (hasChanged_v1Y) {
//											aux = getDoubleValueOfLastJTextFieldV1YValue();
//											if (aux == 0.0)
//												sy = 1.0; // Don't scale if new coordenate is zero
//											else
//												sy = cy / aux;
//										}
//										else
//											sy = 1.0;
//
//										if (sx == 1.0) {
//											// It's supposed that sy != 1.0
//											cx *= sy;
//
//											if (cx < getDoubleValueOfJTextFieldV2X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												restoreAllModifiedCoordinates(); // restores the coordinates
//												return;
//											}
//
//											getJTextFieldVertex1X().setText(Double.toString(cx));
//										}
//										else {
//											if (sy == 1.0) {
//												// It's supposed that sx != 1.0
//												cy *= sx;
//
//												if (cy < getDoubleValueOfJTextFieldV2Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1Y().setText(Double.toString(cy));
//											}
//											else {
//												// If there has been an error -> can't move different distances in X the two vertexes
//												if (sx != sy) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//											}
//										}
//									}
//
//									// Scale Vertex2
//									if ((hasChanged_v2X) || (hasChanged_v2Y)) {
//										double cx = getDoubleValueOfJTextFieldV2X();
//										double cy = getDoubleValueOfJTextFieldV2Y();
//										double sx, sy, aux;
//
//										if (hasChanged_v2X) {
//											aux = getDoubleValueOfLastJTextFieldV2XValue();
//											if (aux == 0.0)
//												sx = 1.0; // Don't scale if new coordenate is zero
//											else
//												sx = cx / aux;
//										}
//										else
//											sx = 1.0;
//
//										if (hasChanged_v2Y) {
//											aux = getDoubleValueOfLastJTextFieldV2YValue();
//											if (aux == 0.0)
//												sy = 1.0; // Don't scale if new coordenate is zero
//											else
//												sy = cy / aux;
//										}
//										else
//											sy = 1.0;
//
//										if (sx == 1.0) {
//											// It's supposed that sy != 1.0
//											cx *= sy;
//
//											if (cx > getDoubleValueOfJTextFieldV1X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												restoreAllModifiedCoordinates(); // restores the coordinates
//												return;
//											}
//
//											getJTextFieldVertex2X().setText(Double.toString(cx));
//										}
//										else {
//											if (sy == 1.0) {
//												// It's supposed that sx != 1.0
//												cy *= sx;
//
//												if (cy > getDoubleValueOfJTextFieldV1Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex2Y().setText(Double.toString(cy));
//											}
//											else {
//												// If there has been an error -> can't move different distances in X the two vertexes
//												if (sx != sy) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "different_scale_factors"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//											}
//										}
//									}
//								}
//								else {
//									// If has to move the area
//									if (getJToggleButtonMove().isSelected()) {
//										// Move in X
//										if ((hasChanged_v1X) || (hasChanged_v2X)) {
//											double c1 = getDoubleValueOfJTextFieldV1X();
//											double c2 = getDoubleValueOfJTextFieldV2X();
//											double d1, d2;
//
//											if (hasChanged_v1X)
//												d1 = c1 - getDoubleValueOfLastJTextFieldV1XValue();
//											else
//												d1 = 0.0;
//
//											if (hasChanged_v2X)
//												d2 = c2 - getDoubleValueOfLastJTextFieldV2XValue();
//											else
//												d2 = 0.0;
//
//											if (d1 == 0.0) {
//												// It's supposed that d2 != 0
//												c1 += d2;
//
//												if (c1 < getDoubleValueOfJTextFieldV2X()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1X().setText(Double.toString(c1));
//											}
//											else {
//												if (d2 == 0.0) {
//													// It's supposed that d1 != 0
//													c2 += d1;
//
//													if (c2 > getDoubleValueOfJTextFieldV1X()) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//
//													getJTextFieldVertex2X().setText(Double.toString(c2));
//												}
//												else {
//													// If there has been an error -> can't move different distances in X the two vertexes
//													if (d1 != d2) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_X"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//												}
//											}
//										}
//
//										// Move in Y
//										if ((hasChanged_v1Y) || (hasChanged_v2Y)) {
//											double c1 = getDoubleValueOfJTextFieldV1Y();
//											double c2 = getDoubleValueOfJTextFieldV2Y();
//											double d1, d2;
//
//											if (hasChanged_v1Y)
//												d1 = c1 - getDoubleValueOfLastJTextFieldV1YValue();
//											else
//												d1 = 0.0;
//
//											if (hasChanged_v2Y)
//												d2 = c2 - getDoubleValueOfLastJTextFieldV2YValue();
//											else
//												d2 = 0.0;
//
//											if (d1 == 0.0) {
//												// It's supposed that d2 != 0
//												c1 += d2;
//
//												if (c1 < getDoubleValueOfJTextFieldV2Y()) {
//													JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//													restoreAllModifiedCoordinates(); // restores the coordinates
//													return;
//												}
//
//												getJTextFieldVertex1Y().setText(Double.toString(c1));
//											}
//											else {
//												if (d2 == 0.0) {
//													// It's supposed that d1 != 0
//													c2 += d1;
//
//													if (c2 > getDoubleValueOfJTextFieldV1Y()) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "problem_with_the_new_calculated_area") + ": " + PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//
//													getJTextFieldVertex2Y().setText(Double.toString(c2));
//												}
//												else {
//													// If there has been an error -> can't move different distances in Y the two vertexes
//													if (d1 != d2) {
//														JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "different_distances_in_Y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//														restoreAllModifiedCoordinates(); // restores the coordinates
//														return;
//													}
//												}
//											}
//										}
//									}
//									else {
//										boolean canSetExtent = true;
//
//										// Check if there is a impossible coordinate
//										if (hasChanged_v1X) {
//											if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1x") + " < " + PluginServices.getText(jButtonPreviewArea, "v2x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v1Y) {
//											if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v1y") + " < " + PluginServices.getText(jButtonPreviewArea, "v2y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v2X) {
//											if (getDoubleValueOfJTextFieldV2X() > getDoubleValueOfJTextFieldV1X()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2x") + " > " + PluginServices.getText(jButtonPreviewArea, "v1x"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										if (hasChanged_v2Y) {
//											if (getDoubleValueOfJTextFieldV2Y() > getDoubleValueOfJTextFieldV1Y()) {
//												JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "v2y") + " > " + PluginServices.getText(jButtonPreviewArea, "v1y"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//												canSetExtent = false;
//											}
//										}
//
//										// If can set the new Extent -> finish
//										if (!canSetExtent) {
//											restoreAllModifiedCoordinates(); // restores the coordinates
//											return;
//										}
//									}
//								}
//
//								//Rectangle2D r2d = vP.getExtent();
//								if (vP.getExtent() != null){
//									vP.setExtent(getNewRectangleByCoordinates());
//									vP.refreshExtent();
//								}
//							}
//						}
//						else {
//							JOptionPane.showMessageDialog(jButtonPreviewArea, PluginServices.getText(jButtonPreviewArea, "at_least_one_incorrect_coordinate"), PluginServices.getText(jButtonPreviewArea, "error"), JOptionPane.ERROR_MESSAGE);
//						}
//					}
//				});
//			}
//			return jButtonPreviewArea;
//		}
//
//		/**
//		 * Calculates the new rectangle using the coordinates of the text fields
//		 *
//		 * @return java.awt.geom.Rectangle2D
//		 */
//		private Rectangle2D getNewRectangleByCoordinates() {
//			return new Rectangle2D.Double(getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV2Y(), getDoubleValueOfJTextFieldV1X() - getDoubleValueOfJTextFieldV2X(), getDoubleValueOfJTextFieldV1Y() - getDoubleValueOfJTextFieldV2Y());
//		}
//
//		/**
//		 * Restores the value of all text fields with coordinates modified to their last value
//		 */
//		private void restoreAllModifiedCoordinates() {
//			if (hasChanged_v1X) {
//				getJTextFieldVertex1X().setText(last_Coordinates[0]);
//				hasChanged_v1X = false;
//			}
//
//			if (hasChanged_v1Y) {
//				getJTextFieldVertex1Y().setText(last_Coordinates[1]);
//				hasChanged_v1Y = false;
//			}
//
//			if (hasChanged_v2X) {
//				getJTextFieldVertex2X().setText(last_Coordinates[2]);
//				hasChanged_v2X = false;
//			}
//
//			if (hasChanged_v2Y) {
//				getJTextFieldVertex2Y().setText(last_Coordinates[3]);
//				hasChanged_v2Y = false;
//			}
//		}
//
//		/**
//		 * Enables or disables all inner JTextField
//		 *
//		 * @param b A boolean value
//		 */
//		public void setAllTextFieldsEnabled(boolean b) {
//			getJTextFieldVertex1X().setEnabled(b);
//			getJTextFieldVertex1Y().setEnabled(b);
//			getJTextFieldVertex2X().setEnabled(b);
//			getJTextFieldVertex2Y().setEnabled(b);
//		}
//
//		/**
//		 * Sets the extent into the text fields
//		 *
//		 * @param extent java.awt.geom.Rectangle2D
//		 */
//		public void updateCoordinates(Rectangle2D extent){
//			getJTextFieldVertex1X().setText(String.valueOf(extent.getMaxX()));
//			last_Coordinates[0] = getJTextFieldVertex1X().getText();
//			previous_Coordinate_Value[0] = last_Coordinates[0];
//
//			getJTextFieldVertex1Y().setText(String.valueOf(extent.getMaxY()));
//			last_Coordinates[1] = getJTextFieldVertex1Y().getText();
//			previous_Coordinate_Value[1] = last_Coordinates[1];
//
//			getJTextFieldVertex2X().setText(String.valueOf(extent.getMinX()));
//			last_Coordinates[2] = getJTextFieldVertex2X().getText();
//			previous_Coordinate_Value[2] = last_Coordinates[2];
//
//			getJTextFieldVertex2Y().setText(String.valueOf(extent.getMinY()));
//			last_Coordinates[3] = getJTextFieldVertex2Y().getText();
//			previous_Coordinate_Value[3] = last_Coordinates[3];
//
//			resetCoordinatesFlags();
//
//			// Enable the 'Applicate' button
//			setApplicable(true);
//		}
//
//		/**
//		 * Validates all coordinates of both vertexes separately
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesCoordinates() {
//			if (! validateCoordinate(getJTextFieldVertex1X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex1Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2X())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (! validateCoordinate(getJTextFieldVertex2Y())) {
//				return new ValidationInfo(false, PluginServices.getText(this, "invalid_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here, all coordinates are valid
//			return new ValidationInfo(true, "");
//		}
//
//		/**
//		 * Validates that Vertex1 is in the right-up corner and the Vertex2 in the left-down corner of the area/rectangle <br>
//		 * (It's supposed that coordinates of the vertexes are valid) <br>
//		 * (It's supposed that going to right or up, the coordinate value increases)
//		 *
//		 * @return A ValidationInfo object reference
//		 */
//		private ValidationInfo validVertexesPositions() {
//			// Check if no coordinate it's defined
//			if (getJTextFieldVertex1X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1x"));
//			}
//
//			if (getJTextFieldVertex2X().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getJTextFieldVertex1Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v1y"));
//			}
//
//			if (getJTextFieldVertex2Y().getText().compareTo("") == 0) {
//				return new ValidationInfo(false, PluginServices.getText(this, "undefined_coordinate") + ": " + PluginServices.getText(this, "v2y"));
//			}
//
//			// Check if the vertex V1 is in the right-up corner and the vertex V2 is in the left-down corner
//			if (getDoubleValueOfJTextFieldV1X() < getDoubleValueOfJTextFieldV2X()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1x") + " < " + PluginServices.getText(this, "v2x"));
//			}
//
//			if (getDoubleValueOfJTextFieldV1Y() < getDoubleValueOfJTextFieldV2Y()) {
//				return new ValidationInfo(false, PluginServices.getText(this, "v1y") + " < " + PluginServices.getText(this, "v2y"));
//			}
//
//			// If arrives here -> vertexes positions are valid
//			return new ValidationInfo(true, "");
//		}
//
//		/**
//		 * Validate each coordinate of each vertex and the position of the vertexes
//		 *
//		 * @return A boolean value
//		 */
//		public boolean validVertexes() {
//			ValidationInfo v1 = validVertexesCoordinates();
//
//			if (!v1.isValid()) {
//				// Notify to user that no filter area will be applied
//				JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v1.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);
//
//				return false;
//			}
//			else {
//				ValidationInfo v2 = validVertexesPositions();
//				if (!v2.isValid()) {
//					// Notify to user that no filter area will be applied
//					JOptionPane.showMessageDialog(null, PluginServices.getText(null, PluginServices.getText(this, "invalid_coordinates") + ": " + v2.getMessage()), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);
//
//					return false;
//				}
//				else {
//					return true;
//				}
//			}
//		}
//
//		/**
//		 * Gets the extent (rectangle) represented by the values of the coordinate text fields
//		 *
//		 * @return java.awt.geom.Rectangle2D
//		 */
//		public Rectangle2D getExtent() {
//			double v1x = getDoubleValueOfJTextFieldV1X();
//			double v1y = getDoubleValueOfJTextFieldV1Y();
//			double v2x = getDoubleValueOfJTextFieldV2X();
//			double v2y = getDoubleValueOfJTextFieldV2Y();
//
//			return new Rectangle2D.Double(v2x, v1y, (v1x - v2x), (v1y  - v2y));
//		}
//
//		/**
//		 * Returns true if there is some coordinate text field with data; else returns false
//		 *
//		 * @return A boolean value
//		 */
//		public boolean areThereSomeCoordinatesWritten() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") != 0) | (getJTextFieldVertex1Y().getText().compareTo("") != 0) | (getJTextFieldVertex2X().getText().compareTo("") != 0) | (getJTextFieldVertex2Y().getText().compareTo("") != 0));
//		}
//
//		/**
//		 * Returns true if all coordinate text fields are undefined (without values)
//		 *
//		 * @return A boolean value
//		 */
//		public boolean areAllCoordinatesUndefined() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) & (getJTextFieldVertex1Y().getText().compareTo("") == 0) & (getJTextFieldVertex2X().getText().compareTo("") == 0) & (getJTextFieldVertex2Y().getText().compareTo("") == 0));
//		}
//
//
//		/**
//		 * Returns true if there is some coordinate undefined (without values)
//		 *
//		 * @return A boolean value
//		 */
//		public boolean isThereAnyCoordinateUndefined() {
//			return ((getJTextFieldVertex1X().getText().compareTo("") == 0) | (getJTextFieldVertex1Y().getText().compareTo("") == 0) | (getJTextFieldVertex2X().getText().compareTo("") == 0) | (getJTextFieldVertex2Y().getText().compareTo("") == 0));
//		}
//	}
//
//	/**
//	 * This class is a MapControl JComponent that has visual information and allows user interact with some tools and view the results
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 */
//	private class SelectableMapControlAreaPanel extends MapControl {
//		private MouseListener[] mouseListeners;
//		private MouseWheelListener[] mouseWheelListeners;
//		private MouseMotionListener[] mouseMotionListeners;
//
//		/**
//		 * Default constructor
//		 */
//		public SelectableMapControlAreaPanel() {
//			super();
//			initialize();
//		}
//
//		/**
//		 * This method initializes this component
//		 */
//		public void initialize() {
//			/* Sets Bounds of this graphical component */
//			this.setBounds(DEFAULT_AREA_MAPCONTROL_PANEL_RECTANGLE);
//
//			/* Sets border to this graphical component */
//			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.gray, 1));
//
//			/* Sets a clone of the current active view map context to this MapControl */
//			try {
//				MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();
//
//				this.setMapContext(mp.cloneFMap());
//				ViewPort vP = this.getViewPort();
//
//				Rectangle2D r2D = vP.getExtent();
//
//				if (r2D != null) {
//					vP.refreshExtent();
//					getCoordinatesPanel().updateCoordinates(r2D);
//				}
//			} catch (XMLException e) {
//				e.printStackTrace();
//			}
//
//			/* Adds listeners to this MapControl */
//			this.addToolsListeners();
//
//			/* Sets default tool */
//			this.setTool(PAN_TOOL);
//		}
//
//		/**
//		 * Refresh the active view
//		 */
//		public void refreshWithTheActiveView() {
//			try {
//				MapContext mp = ((BaseView) PluginServices.getMDIManager().getActiveWindow()).getMapControl().getMapContext();
//				this.setMapContext(mp.cloneFMap());
//				ViewPort vP = this.getViewPort();
//
//				Rectangle2D r2D = vP.getExtent();
//
//				if (r2D != null) {
//					vP.refreshExtent();
//					getCoordinatesPanel().updateCoordinates(r2D);
//				}
//			} catch (XMLException e) {
//				e.printStackTrace();
//			}
//		}
//
//		/**
//		 * Adds listeners that allows user interact with it's mouse over this component and that represents different tools
//		 */
//		private void addToolsListeners() {
//			// MOVEMENT EVENTS LISTENER: sets mouse coordinates to the status bar
//	        StatusBarListener sbl = new StatusBarListener(this);
//
//	        // ZOOM OUT (Presses on the map and it will be centered showing a bigger area)
//	        // Only pressing, not selecting a rectangle area
//	        ZoomOutListener zol = new ZoomOutListener(this);
//	        this.addMapTool(ZOOM_OUT_TOOL, new Behavior[]{new PointBehavior(zol), new MouseMovementBehavior(sbl)});
//
//	        // ZOOM IN (Using a Rectangle or a simple mouse click)
//	        ZoomOutRightButtonListener zoil = new ZoomOutRightButtonListener(this);
//	        ZoomInListener zil = new ZoomInListener(this);
//	        this.addMapTool(ZOOM_IN_TOOL, new Behavior[]{new RectangleBehavior(zil),
//	        				new PointBehavior(zoil), new MouseMovementBehavior(sbl)});
//
//	        // PAN
//	        PanListener pl = new PanListener(this);
//	        this.addMapTool(PAN_TOOL, new Behavior[]{new MoveBehavior(pl), new MouseMovementBehavior(sbl)});
//
//	        // MEASURE DISTANCES
//	        MeasureListener mli = new MeasureListener(this);
//	        this.addMapTool(MEASURE_DISTANCES_TOOL, new Behavior[]{new PolylineBehavior(mli), new MouseMovementBehavior(sbl)});
//
//	        // MEASURE AREA
//	        AreaListener ali = new AreaListener(this);
//	        this.addMapTool(MEASURE_AREA_TOOL, new Behavior[]{new PolygonBehavior(ali), new MouseMovementBehavior(sbl)});
//
//	        this.getViewPort().addViewPortListener(new ViewPortListener() {
//	        	/*
//	        	 *  (non-Javadoc)
//	        	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
//	        	 */
//				public void extentChanged(ExtentEvent e) {
//					if (getMapContext().getViewPort().getExtents().hasPrevious()) {
//						Rectangle2D r2d = getViewPort().getExtent();
//
//						if (r2d != null) {
//							getCoordinatesPanel().updateCoordinates(r2d);
//
//							if (getJComboBoxToolSelection().getSelectedIndex() != 0) {
//								hasUserDefinedAnArea = true;
//							}
//						}
//
//						getJButtonZoomPrevious().setEnabled(true);
//						getJButtonUndo().setEnabled(true);
//					}
//					else {
//						getJButtonZoomPrevious().setEnabled(false);
//						getJButtonUndo().setEnabled(false);
//					}
//				}
//
//				/*
//				 *  (non-Javadoc)
//				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
//				 */
//				public void backColorChanged(ColorEvent e) {
//				}
//
//				/*
//				 *  (non-Javadoc)
//				 * @see com.iver.cit.gvsig.fmap.ViewPortListener#projectionChanged(com.iver.cit.gvsig.fmap.ProjectionEvent)
//				 */
//				public void projectionChanged(ProjectionEvent e) {
//				}
//
//	        });
//		}
//
//		/*
//		 *  (non-Javadoc)
//		 * @see java.awt.Component#setEnabled(boolean)
//		 */
//		public void setEnabled(boolean b) {
//			super.setEnabled(b);
//
//			if (b)
//				enableAllMouseListeners();
//			else
//				disableAllMouseListeners();
//		}
//
//		/**
//		 * Disables all mouse listeners
//		 */
//		public void disableAllMouseListeners() {
//			int i;
//
//			// Only disable listeners if there are listeners to remove (it's supposed that there are always the same number of mouse listeners, and this listeners
//			//   are referenciated by this component or by the private attribute 'mouseListeners')
//
//			// Mouse Button Listeners
//			if (mouseListeners == null) {
//				mouseListeners = this.getMouseListeners();
//
//				for (i = 0; i < mouseListeners.length; i++) {
//					removeMouseListener(mouseListeners[i]);
//				}
//			}
//
//			// Mouse Wheel Listeners
//			if (mouseWheelListeners == null) {
//				mouseWheelListeners = this.getMouseWheelListeners();
//
//				for (i = 0; i < mouseWheelListeners.length; i++) {
//					removeMouseWheelListener(mouseWheelListeners[i]);
//				}
//			}
//
//			// Mouse motion listeners
//			if (mouseMotionListeners == null) {
//				mouseMotionListeners = this.getMouseMotionListeners();
//
//				for (i = 0; i < mouseMotionListeners.length; i++) {
//					removeMouseMotionListener(mouseMotionListeners[i]);
//				}
//			}
//		}
//
//		/**
//		 * Enables all mouse listeners
//		 */
//		public void enableAllMouseListeners() {
//			int i;
//
//			// Mouse Button Listeners
//			for (i = 0; i < mouseListeners.length; i++) {
//				addMouseListener(mouseListeners[i]);
//			}
//
//			mouseListeners = null;
//
//			// Mouse Wheel Listeners
//			for (i = 0; i < mouseWheelListeners.length; i++) {
//				addMouseWheelListener(mouseWheelListeners[i]);
//			}
//
//			mouseWheelListeners = null;
//
//			// Mouse motion listeners
//			for (i = 0; i < mouseMotionListeners.length; i++) {
//				addMouseMotionListener(mouseMotionListeners[i]);
//			}
//
//			mouseMotionListeners = null;
//		}
//	}
//
//	/**
//	 * This class has information about a validation: <br>
//	 *   - A boolean value -> if has been or not validated <br>
//	 *   - An String -> a message about the invalid
//	 *
//	 * @author Pablo Piqueras Bartolomï¿½ (p_queras_pab@hotmail.com)
//	 */
//	private class ValidationInfo {
//		private boolean _valid;
//		private String _message;
//
//		/**
//		 * Default constructor with two parameters
//		 *
//		 * @param valid If was valid or not
//		 * @param message A message associate to the validation (If has been valid, this attribute should be "", in the other case should have a message with
//		 *                  an explanation about the invalidation)
//		 */
//		public ValidationInfo(boolean valid, String message) {
//			_valid = valid;
//			_message = message;
//		}
//
//		/**
//		 * Returns the value of the inner attribute '_valid'
//		 *
//		 * @return A boolean value
//		 */
//		public boolean isValid() {
//			return _valid;
//		}
//
//		/**
//		 * Returns the value of the inner attribute '_message'
//		 *
//		 * @return java.lang.String
//		 */
//		public String getMessage() {
//			return _message;
//		}
//	}
//}