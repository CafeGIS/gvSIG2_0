package com.iver.cit.gvsig.gui.panels;

import java.awt.Container;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.ProviderNotRegisteredException;
import org.gvsig.fmap.dal.serverexplorer.wfs.WFSServerExplorer;
import org.gvsig.fmap.dal.store.wfs.WFSStoreParameters;
import org.gvsig.fmap.dal.store.wfs.WFSStoreProvider;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.LayerFactory;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.gui.beans.panelGroup.PanelGroupManager;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupException;
import org.gvsig.gui.beans.panelGroup.exceptions.EmptyPanelGroupGUIException;
import org.gvsig.gui.beans.panelGroup.exceptions.ListCouldntAddPanelException;
import org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.panelGroup.panels.IPanel;
import org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel;
import org.gvsig.remoteClient.wfs.WFSStatus;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.ILayerPanel;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeature;
import com.iver.cit.gvsig.gui.panels.model.WFSSelectedFeatureManager;
import com.iver.cit.gvsig.gui.wizards.WFSWizard;

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
/* CVS MESSAGES:
 *
 * $Id: WFSParamsPanel.java 17736 2008-01-02 16:53:48Z ppiqueras $
 * $Log$
 * Revision 1.42  2007-09-19 16:14:50  jaume
 * removed unnecessary imports
 *
 * Revision 1.41  2007/06/26 09:33:54  jorpiell
 * Information tab refreshed
 *
 * Revision 1.40  2007/04/11 12:08:55  ppiqueras
 * Varios cambios:
 * - Corregida etiqueta "Campo"
 * - Cambiada actualización
 * - Corregido bug: actualizar área visible después de pulsar "Aplicar" en la interfaz gráfica.
 *
 * Revision 1.39  2007/03/15 13:33:44  ppiqueras
 * Corregido bug de excepción que se lanzaba cuando se filtraba y no se podía cargar la capa.
 *
 * Revision 1.38  2007/03/05 13:49:42  ppiqueras
 * Si una capa WFS no tiene campos (y por tanto no tiene un campo obligatorio de tipo geometría), que avise al usuario y no permita que se intente cargar dicha capa.
 *
 * Revision 1.37  2007/03/01 13:12:09  ppiqueras
 * Mejorado el filtrado por área.
 *
 * Revision 1.36  2007/02/22 12:30:59  ppiqueras
 * - Eliminado método que sobraba.
 * - Mejorada la relación con el panel del área.
 * - Añadido JOptionPanel de aviso que no se aplicará área porque es incorrecta.
 *
 * Revision 1.35  2007/02/20 11:31:11  ppiqueras
 * Eliminados comentarios que sobraban.
 *
 * Revision 1.34  2007/02/19 11:44:42  jorpiell
 * AÃ±adidos los filtros por Ã¡rea
 *
 * Revision 1.33  2007/02/16 13:36:53  ppiqueras
 * Que el área seleccionada en el panel WFSArea sea accesible una vez se va a aplicar.
 *
 * Revision 1.32  2007/02/12 11:37:00  ppiqueras
 * Añadidos comentarios y método para refrescar el MapControl de la pestaña del área.
 *
 * Revision 1.31  2007/02/09 14:12:39  jorpiell
 * Soporte para WFS 1.1 y WFS-T
 *
 * Revision 1.30  2007/02/02 12:22:22  ppiqueras
 * Corregido algún bug.
 *
 * Revision 1.29  2007/01/08 09:37:39  ppiqueras
 * Eliminado comentario que sobraba
 *
 * Revision 1.28  2006/12/29 09:27:02  ppiqueras
 * Corregidos varios bugs:
 *
 * - Se ejecuta 2 veces "Aplicar" o "Aceptar" en los filtros WFS.
 * - Cuando se carga por primera vez una capa WFS, si se pone un
 * filtro incorrecto, se ignora el filtro (cargando toda la informaciÃ³n posible de la capa, y borrando el texto de filtrado).
 *
 * Revision 1.27  2006/12/26 09:19:40  ppiqueras
 * Cambiado "atttibutes" en todas las aparaciones en atributos, mÃ©todos, clases, paquetes o comentarios por "fields". (SÃ³lo a aquellas que afectan a clases dentro del proyecto extWFS2).
 *
 * Revision 1.26  2006/12/20 14:22:06  ppiqueras
 * AÃ±adido comentario
 *
 * Revision 1.25  2006/12/15 13:59:36  ppiqueras
 * -Permite que se almacenen y/o recojan todos los campos y valores conocidos de la capa actual.
 *
 * -AdemÃ¡s, control frentre a consultas de filtro errÃ³neas, (esto a medias aÃºn).
 *
 * - AlgÃºn comentario mÃ¡s.
 *
 * Revision 1.24  2006/12/12 10:24:45  ppiqueras
 * Nueva funcionalidad: Pulsando doble 'click' sobre una capa de un servidor, se carga (igual que antes), pero ademÃ¡s se avanza a la siguiente pestaÃ±a sin tener que pulsar el botÃ³n 'Siguiente'.
 *
 * Revision 1.23  2006/12/11 11:02:24  ppiqueras
 * Corregido bug -> que se mantenga la frase de filtrado
 *
 * Revision 1.22  2006/12/04 08:59:47  ppiqueras
 * Algunos bugs corregidos. A cambio hay 2 bugs relacionados que todavÃ­a no han sido corregidos (ver PHPCollab) (los tiene asignados Jorge).
 *
 * Revision 1.21  2006/11/28 08:05:13  jorpiell
 * Se escribe la query en la pestaña del filtro
 *
 * Revision 1.20  2006/11/14 13:45:49  ppiqueras
 * AÃ±adida pequeÃ±a funcionalidad:
 * Cuando se pulsa el botÃ³n "Aplicar", (al seleccionar un nuevo filtro), si el Ã¡rbol de campos posee alguno seleccionado, actualiza los valores (con los nuevos), asociados a Ã©ste campo seleccionado.
 *
 * Revision 1.19  2006/10/27 12:10:02  ppiqueras
 * Nueva funcionalidad
 *
 * Revision 1.16  2006/10/23 07:37:04  jorpiell
 * Ya funciona el filterEncoding
 *
 * Revision 1.14  2006/10/13 13:05:32  ppiqueras
 * Permite el refrescado de datos del panel de filtrado sobre capa WFS.
 *
 * Revision 1.13  2006/10/10 12:55:06  jorpiell
 * Se ha añadido el soporte de features complejas
 *
 * Revision 1.12  2006/10/02 09:17:48  jorpiell
 * Añadido el setCRS a la capa
 *
 * Revision 1.11  2006/09/29 13:02:38  ppiqueras
 * Filtro para WFS. De momento sÃ³lo interfaz grÃ¡fica.
 *
 * Revision 1.10  2006/07/21 11:50:31  jaume
 * improved appearance
 *
 * Revision 1.9  2006/06/21 12:35:45  jorpiell
 * Se ha añadido la ventana de propiedades. Esto implica añadir listeners por todos los paneles. Además no se muestra la geomatría en la lista de atributos y se muestran únicamnete los que se van a descargar
 *
 * Revision 1.8  2006/06/15 07:50:58  jorpiell
 * Añadida la funcionalidad de reproyectar y hechos algunos cambios en la interfaz
 *
 * Revision 1.7  2006/05/25 16:22:47  jorpiell
 * Se limpia el panel cada vez que se conecta con un servidor distinto
 *
 * Revision 1.6  2006/05/25 16:01:43  jorpiell
 * Se ha añadido la funcionalidad para eliminar el namespace de los tipos de atributos
 *
 * Revision 1.5  2006/05/25 10:31:06  jorpiell
 * Como ha cambiado la forma de mostrar las capas (una tabla, en lugar de una lista), los paneles han tenido que ser modificados
 *
 * Revision 1.4  2006/05/23 08:09:39  jorpiell
 * Se ha cambiado la forma en la que se leian los valores seleccionados en los paneles y se ha cambiado el comportamiento de los botones
 *
 * Revision 1.3  2006/05/19 12:57:08  jorpiell
 * Modificados algunos paneles
 *
 * Revision 1.2  2006/04/20 16:38:24  jorpiell
 * Ahora mismo ya se puede hacer un getCapabilities y un getDescribeType de la capa seleccionada para ver los atributos a dibujar. Queda implementar el panel de opciones y hacer el getFeature().
 *
 * Revision 1.1  2006/04/19 12:50:16  jorpiell
 * Primer commit de la aplicación. Se puede hacer un getCapabilities y ver el mensaje de vienvenida del servidor
 *
 */

/**
 * <p>Container of the WFS panels, that works as a {@linkplain TabbedPanel TabbedPanel}.</p>
 *
 * @author Pablo Piqueras Bartolomé (pablo.piqueras@iver.es)
 * @author Jorge Piera Llodrá (piera_jor@gva.es)
 */
public class WFSParamsPanel extends TabbedPanel implements ILayerPanel {
	private static final long serialVersionUID = 150328995058481516L;

	private WFSServerExplorer serverExplorer = null;
	private WFSSelectedFeatureManager selectedFeatureManager = null;

	// Tab positions
	private int infoTabPosition = -1;
	private int featureTabPosition = -1;
	private int fieldsTabPosition = -1;
	private int optionsTabPosition = -1;
	private int filterTabPosition = -1;
	private int areaTabPosition = -1;

	// Tabs
	private WFSInfoPanel infoPanel = null;
	private WFSSelectFeaturePanel featurePanel = null;
	private WFSSelectFieldsPanel fieldsPanel = null;
	private WFSOptionsPanel optionsPanel = null;
	private WFSFilterPanel filterPanel = null;
	private WFSAreaPanel areaPanel = null;

	static {
		// Registers this class as a "PanelGroup" type
		PanelGroupManager.getManager().registerPanelGroup(WFSParamsPanel.class);

		// Sets this class as the default "PanelGroup" type
		PanelGroupManager.getManager().setDefaultType(WFSParamsPanel.class);
	}

	/**
	 * This method initializes jTabbedPane
	 *
	 * @return javax.swing.JTabbedPane
	 */
	public WFSParamsPanel(Object reference) {
		super(reference);
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel#initialize()
	 */
	protected void initialize() {
		super.initialize();

		this.setVisible(false);
	}

	/**
	 * Sets the focus to the next tab of the current one.
	 */
	public void goToNextTab() {
		int tabIndex = getSelectedIndex();

		if ((tabIndex - 1) < getPanelInGUICount()) {
			int nextIndex = nextPageEnabled();

			if (nextIndex > -1)
				setSelectedIndex(nextIndex);
		}
	}

	/**
	 * Sets the focus to the tab previous to the current one.
	 */
	public void goToPreviousTab(){
		setSelectedIndex(previousEnabledPage());
	}

	/**
	 * @see JTabbedPane#getSelectedIndex()
	 */
	public int getSelectedIndex(){
		return this.getJTabbedPane().getSelectedIndex();
	}

	/**
	 * @see JTabbedPane#setSelectedIndex(int)
	 */
	public void setSelectedIndex(int index) {
		this.getJTabbedPane().setSelectedIndex(index);
	}

	/**
	 * This method initializes infoPanel
	 * 
	 * @return the information panel
	 */
	public WFSInfoPanel getInfoPanel() {
		if (infoPanel != null)
			return infoPanel;

		if (infoTabPosition == -1)
			return null;

		return (infoPanel = (WFSInfoPanel) values().toArray()[infoTabPosition]);
	}

	/**
	 * This method initializes featurePanel
	 *
	 * @return javax.swing.JPanel
	 */
	public WFSSelectFeaturePanel getFeaturesPanel() {
		if (featurePanel != null)
			return featurePanel;

		if (featureTabPosition == -1)
			return null;

		return (featurePanel = (WFSSelectFeaturePanel) values().toArray()[featureTabPosition]);
	}

	/**
	 * This method initializes fieldsPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public WFSSelectFieldsPanel getFieldsPanel() {
		if (fieldsPanel != null)
			return fieldsPanel;

		if (fieldsTabPosition == -1)
			return null;

		return (fieldsPanel = (WFSSelectFieldsPanel) values().toArray()[fieldsTabPosition]);
	}

	/**
	 * This method initializes optionsPanel
	 *
	 * @return javax.swing.JPanel
	 */
	public WFSOptionsPanel getOptionsPanel() {
		if (optionsPanel != null)
			return optionsPanel;

		if (optionsTabPosition == -1)
			return null;

		return (optionsPanel = (WFSOptionsPanel) values().toArray()[optionsTabPosition]);
	}

	/**
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public WFSFilterPanel getFilterPanel(){
		if (filterPanel != null)
			return filterPanel;

		if (filterTabPosition == -1)
			return null;

		return (filterPanel = (WFSFilterPanel) values().toArray()[filterTabPosition]);
	}

	/**
	 * This method initializes areaPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	public WFSAreaPanel getAreaPanel() {
		if (areaPanel != null)
			return areaPanel;

		if (areaTabPosition == -1)
			return null;

		return (areaPanel = (WFSAreaPanel) values().toArray()[areaTabPosition]);
	}

	/**
	 * Verifies that the selected parameters are enough to request the coverage to the server.
	 * 
	 * @return boolean <code>true</code> if its correctly configured; otherwise returns <code>false</code>
	 */
	public boolean isCorretlyConfigured() {
		if (featureTabPosition == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Enable or disable the default tabs
	 */
	public void enableDefaultTabs(boolean isEnabled) {
		if (fieldsTabPosition != -1)
			setEnabledAt(fieldsTabPosition, isEnabled);

		if (filterTabPosition != -1)
			setEnabledAt(filterTabPosition, isEnabled);

		if (areaTabPosition != -1)
			setEnabledAt(areaTabPosition, isEnabled);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.tabbedPanel.TabbedPanel#loadPanel(org.gvsig.gui.beans.panelGroup.panels.IPanel)
	 */
	protected void loadPanel(IPanel panel) {
		super.loadPanel(panel);

		if (((AbstractPanel)panel).getClass() ==  WFSInfoPanel.class) {
			infoTabPosition = getPanelInGUICount() - 1;
			return;
		}

		if (((AbstractPanel)panel).getClass() ==  WFSSelectFeaturePanel.class) {
			featureTabPosition = getPanelInGUICount() - 1;
			return;
		}

		if (((AbstractPanel)panel).getClass() ==  WFSSelectFieldsPanel.class) {
			fieldsTabPosition = getPanelInGUICount() - 1;
			return;
		}

		if (((AbstractPanel)panel).getClass() ==  WFSOptionsPanel.class) {
			optionsTabPosition = getPanelInGUICount() - 1;
			return;
		}

		if (((AbstractPanel)panel).getClass() ==  WFSFilterPanel.class) {
			filterTabPosition = getPanelInGUICount() - 1;
			return;
		}

		if (((AbstractPanel)panel).getClass() ==  WFSAreaPanel.class) {
			areaTabPosition = getPanelInGUICount() - 1;
			return;
		}
	}

	/**
	 * Refresh all the panels with the WFS capabilities information.
	 */
	public void refreshCapabilitiesInfo(){
		WFSSelectedFeature selectedFeature = getFeaturesPanel().getSelectedFeature();
		if (selectedFeature != null) {
			selectedFeature.setSelectedFields(getFieldsPanel().getSelectedFields());
		} else {
			getFeaturesPanel().refresh(null);
		}
		serverExplorer.setUserName(getOptionsPanel().getUserName());
		serverExplorer.setBuffer(getOptionsPanel().getBuffer());
		serverExplorer.setTimeOut(getOptionsPanel().getTimeout());

		getInfoPanel().refresh(selectedFeature);
	}

	/**
	 * Gets the information used to add or load a WFS layer.
	 * 
	 * @return information used to add or load a WFS layer
	 */
	public WFSServerExplorer getServerExplorer() {
		return serverExplorer;
	}

	/**
	 * Sets the information used to add or load a WFS layer.
	 *
	 * @param wizardData information used to add or load a WFS layer
	 */
	public void setServerExplorer(WFSServerExplorer serverExplorer) {
		this.serverExplorer = serverExplorer;
		this.selectedFeatureManager = 
			WFSSelectedFeatureManager.getInstance(serverExplorer);
	}

	/**
	 * Returns the next enabled tab's index, or -1 if there isn't any.
	 *
	 * @return The index or -1 if there is no one.
	 */
	public int nextPageEnabled() {
		int currentPage = getSelectedIndex();
		int nPages = getPanelInGUICount();

		if (currentPage == nPages)
			return -1;

		for (int i = currentPage + 1; i < nPages; i++){
			if (getJTabbedPane().isEnabledAt(i)){
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns the index of the current tab.
	 *
	 * @return index of the current tab
	 */
	public int currentPage() {
		return getSelectedIndex();
	}

	/**
	 * Returns the index of the previous enabled tab.
	 *
	 * @return The index, or -1 if there is no one.
	 */
	public int previousEnabledPage() {
		int currentPage = getSelectedIndex();

		if (currentPage == 0)
			return -1;

		for (int i = currentPage - 1; i > -1; i--) {
			if (isEnabledAt(i)) {
				return i;
			}
		}

		return -1;
	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#initWizard()
	 */
	public void initWizard() {

	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#execute()
	 */
	public void execute() {

	}

	/*
	 *  (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.ILayerPanel#getLayer()
	 */
	public FLayer getLayer() {
		try{
			return LayerFactory.getInstance().createLayer(
					getFeaturesPanel().getLayerName(),
					getDataStoreParameters());	
		} catch (InitializeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProviderNotRegisteredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (LoadLayerException e1){
			e1.printStackTrace();
		}
		return null;
	}

	public DataStoreParameters getDataStoreParameters() throws InitializeException, ProviderNotRegisteredException{
		DataManager dataManager = DALLocator.getDataManager();
		WFSStoreParameters parameters = (WFSStoreParameters) dataManager
		.createStoreParameters(WFSStoreProvider.NAME);
		refreshDataStoreParameters(parameters);
		return parameters;
	}

	public void refreshDataStoreParameters(WFSStoreParameters parameters){
		WFSSelectedFeature selectedFeature = getSelectedFeature();
		parameters.setUrl(serverExplorer.getUrl());
		parameters.setVersion(serverExplorer.getVersion());
		parameters.setFeatureType(selectedFeature.getNameSpace(),
				selectedFeature.getName());
		parameters.setFields(getFieldsPanel().getSelectedFieldsAsString());
		parameters.setUser(getOptionsPanel().getUserName());
		parameters.setPassword(getOptionsPanel().getPassword());
		parameters.setMaxFeatures(getOptionsPanel().getBuffer());
		parameters.setTimeOut(getOptionsPanel().getTimeout());
//		parameters.setFilterByArea(getAreaPanel().getArea(),
//				"the_geom",
//				getOptionsPanel().getSRS(),
//				AFilter.GEOMETRIC_OPERATOR_INTERSECT);		
//		parameters.setFilterByAttribute(getFilterPanel().getFilterExpressionFromInterface());
	}	

	/**
	 * @see WFSWizard#doClickOnNextButton()
	 */
	public void doClickOnNextButton() {
		Object obj = this.getParent();

		if (obj == null)
			return;

		// When we start to connect to a server -> the interface is the dialog WFSWizard
		if (obj instanceof WFSWizard)
			((WFSWizard)this.getParent()).doClickOnNextButton();
		else {
			// When we are modifying a loaded layer -> the interface is the dialog WFSPropsDialog
			this.advanceCurrentTab();
		}
	}

	/**
	 * Selects the next tab
	 */
	public void advanceCurrentTab() {
		int currentTabIndex = this.getJTabbedPane().getSelectedIndex();

		// Ensure we can select next tab
		if ((currentTabIndex != -1) && (currentTabIndex < (values().size() -1)))
			setSelectedIndex(currentTabIndex + 1);
	}

	/**
	 * Refreshes the data of the inner <code>WFSSelectFeaturePanel</code>.
	 */
	public void refreshWFSSelectFeaturePanel() {
		WFSSelectFeaturePanel panel = getFeaturesPanel();

		if (panel != null) {
			panel.refresh(null);
		}
	}

	/**
	 * Gets the node of the layer selected as a feature, with the selected fields and the filter defined.
	 * 
	 * @return gets the node of the layer selected as a feature, with the selected fields and the filter defined
	 */
	private WFSSelectedFeature getSelectedFeature(){
		WFSSelectedFeature selectedFeature = getFeaturesPanel().getSelectedFeature();
		selectedFeature.setSelectedFields(getFieldsPanel().getSelectedFields());

		// If the query is incorrect -> no query (no filter)
		String query = getFilterPanel().getQuery();
		if (query == null) {
			selectedFeature.setFilter("");

			// Removes filter expression from the JTextArea
			getFilterPanel().removeFilterExpression();

			// Notify to user that no filter will be applied
			JOptionPane.showMessageDialog(null, PluginServices.getText(null, "no_filter_will_be_applied_in_the_load_of_the_layer"), PluginServices.getText(null, "warning"), JOptionPane.WARNING_MESSAGE);
		}
		else {
			selectedFeature.setFilter(query);
		}

		return selectedFeature;
	}
	
	/**
	 * @return the selected projection
	 */
	public IProjection getSelectedFeatureProjection(){
		WFSSelectedFeature selectedFeature = getSelectedFeature();
		if (selectedFeature.getSrs().size() > 0){
			String crs = (String)selectedFeature.getSrs().get(0);
			crs = getSRS(crs);
			IProjection projection = CRSFactory.getCRS(crs);
			if (projection != null){
				return projection;
			}
		}
		return null;
	}
	
	/**
	 * Removing the URN prefix
	 * @param srs
	 * @return
	 */
	private String getSRS(String srs){
		if (srs == null){
			return null;
		}
		if (srs.startsWith("urn:x-ogc:def:crs:")){
			String newString = srs.substring(srs.lastIndexOf(":") + 1, srs.length());
			if (srs.indexOf("EPSG") > 0){
				if (newString.indexOf("EPSG") < 0){
					newString = "EPSG:" + newString;
				}
			}
			return newString;			
		}
		if (srs.toLowerCase().startsWith("crs:")){
			return srs.substring(4, srs.length());
		}
		return srs;
	}

	/**
	 * Refresh all the panels every time that a different layer is selected. The info panel is refreshed every time the user 
	 * makes a click on it.
	 * 
	 * @param selectedFeature the selected layer
	 */
	public void refresh(WFSSelectedFeature selectedFeature){
		boolean hasFields = false;

		if (selectedFeature!=null){
			//Update the layer information
			selectedFeature = selectedFeatureManager.getFeatureInfo(selectedFeature.getNameSpace(),
					selectedFeature.getName());

			// If there is no fields -> disable not necessary tabs
			if (selectedFeature.getFields().size() == 0) {
				setApplicable(false);
				hasFields = false;
			}else{	
				getFieldsPanel().refresh(selectedFeature);			
				getOptionsPanel().refresh(selectedFeature);
				getFilterPanel().refresh(selectedFeature);
				getAreaPanel().refresh(selectedFeature);
				hasFields = true;
			}
		}

		enableDefaultTabs(hasFields);
	}

	/**
	 * Changes the <i>enable</i> status of the "<i>apply</i>" button
	 * 
	 * @param isApplicable the <i>enable</i> status of the "<i>apply</i>" button
	 */
	public void setApplicable(boolean isApplicable){
		setEnabledApplyButton(isApplicable);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.buttonspanel.IButtonsPanel#setEnabledApplyButton(boolean)
	 */
	public void setEnabledApplyButton(boolean b) {
		super.setEnabledApplyButton(b);

		Container parent = getParent();

		if ((parent != null) && (parent instanceof WizardPanel))
			((WizardPanel)parent).callStateChanged(b);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.AbstractPanelGroup#loadPanels(org.gvsig.gui.beans.panelGroup.loaders.IPanelGroupLoader)
	 */
	public void loadPanels(IPanelGroupLoader loader) throws ListCouldntAddPanelException, EmptyPanelGroupException, EmptyPanelGroupGUIException {
		// This code will be executed when is creating the dialog of properties
		if (getReference() != null) {
			FLyrVect lyr = (FLyrVect) getReference();

			DataManager dataManager = DALLocator.getDataManager();
			//Create the datastore parameters and fill them
			WFSStoreParameters parameters;
			try {
				parameters = (WFSStoreParameters) dataManager
				.createStoreParameters(WFSStoreProvider.NAME);
				parameters.setUrl(serverExplorer.getUrl());
				parameters.setVersion(serverExplorer.getVersion());
				//parameters.setFeatureType(namespacePrefix, namespace, featureType);

				//dataSource.setDriver(lyr.getWfsDriver());

				//setServerExplorer(dataSource);

				super.loadPanels(loader);

				//				HashMap<String, Object> info = lyr.getProperties();
				//				setSelectedFeature((WFSLayerNode)info.get("wfsLayerNode"));
				//				setStatus((WFSStatus)info.get("status"));
				//				setLayerName(lyr.getName());
				//				setVisible(true);
				//				refreshInfo();
			} catch (InitializeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProviderNotRegisteredException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return;
		}

		// This will executed when is adding a new WFS layer:
		super.loadPanels(loader);
	}

	///// BEGIN: METHODS FOR INITIALIZE THIS PANEL AS A COMPONENT OF A PROPERTIES DIALOG /////

	/**
	 * Sets the selected feature.
	 * 
	 * @param selectedFeature node of the selected feature
	 */
	public void setSelectedFeature(WFSSelectedFeature selectedFeature){
		getFeaturesPanel().setSelectedFeature(selectedFeature);
		getFieldsPanel().setSelectedFields(selectedFeature);
		getFilterPanel().refresh(selectedFeature);
	}

	/**
	 * Sets the status of the options and filter panels.
	 * 
	 * @param status the status of the options and filter panels
	 */
	public void setStatus(WFSStatus status) {
		getOptionsPanel().setStatus(status);
		getFilterPanel().setFilterExpressionIntoInterface(status.getFilterByAttribute());
	}

	/**
	 * @see WFSSelectFeaturePanel#setLayerName(String)
	 */
	public void setLayerName(String name) {
		getFeaturesPanel().setLayerName(name);
	}

	/**
	 * Updates the information stored at the wizard's data and <i>info</i> panel.
	 */
	public void refreshInfo(){
		WFSSelectedFeature selectedFeature = getFeaturesPanel().getSelectedFeature();

		if (selectedFeature != null) {
			selectedFeature.setSelectedFields(getFieldsPanel().getSelectedFields());
		}

		serverExplorer.setUserName(getOptionsPanel().getUserName());
		serverExplorer.setBuffer(getOptionsPanel().getBuffer());
		serverExplorer.setTimeOut(getOptionsPanel().getTimeout());
		getInfoPanel().refresh(selectedFeature);
	}
	
	/**
	 * @return the selectedFeatureManager
	 */
	public WFSSelectedFeatureManager getSelectedFeatureManager() {
		return selectedFeatureManager;
	}	
}
