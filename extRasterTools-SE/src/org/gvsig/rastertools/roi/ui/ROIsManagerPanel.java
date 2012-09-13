package org.gvsig.rastertools.roi.ui;

import java.awt.BorderLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.EmptyBorder;

import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.rastertools.roi.ui.listener.ROIManagerPanelListener;

import com.iver.andami.PluginServices;

/**
 * Panel para el gestor de ROIs.
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class ROIsManagerPanel extends DefaultButtonsPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7269833929085630360L;

	private ROIsTablePanel				tablePanel					= null;
	private ROIManagerDialog 			roiManagerDialog 			= null;
	private ROIManagerPanelListener     listener					= null;
	private JMenuBar 					menuBar			 			= null;
	private JMenuItem 					closeMenuItem 				= null;
	private JMenuItem 					saveMenuItem 				= null;
	private JMenuItem 					loadMenuItem 				= null;
	private JMenuItem 					exportMenuItem 				= null;
	private JMenuItem 					helpMenuItem 				= null;
	private JMenuItem 					preferencesMenuItem			= null;
	
	public ROIsManagerPanel(ROIManagerDialog roiManagerDialog) {
		super();
		this.roiManagerDialog = roiManagerDialog;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(5,5));
		setBorder(new EmptyBorder(11,15,11,15));
		//setBorder(new EmptyBorder(0,15,11,15));		
		//add(getMenuBar(),BorderLayout.NORTH);
		add(getTablePanel(),BorderLayout.CENTER);
		
		listener = new ROIManagerPanelListener(this);
		getCloseMenuItem().addActionListener(listener);
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
			fileMenu.add(getExportMenuItem());
			fileMenu.add(getCloseMenuItem());
			helpMenu.add(getHelpMenuItem());
			optionsMenu.add(getPreferencesMenuItem());
			menuBar.add(fileMenu);
			menuBar.add(optionsMenu);
			menuBar.add(helpMenu);
		}
		return menuBar;
	}
	
	public JMenuItem getExportMenuItem() {
		if (exportMenuItem == null){
			exportMenuItem = new JMenuItem(PluginServices.getText(this, "exportar"));
		}
		return exportMenuItem;
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

	public JMenuItem getPreferencesMenuItem() {
		if (preferencesMenuItem == null){
			preferencesMenuItem = new JMenuItem(PluginServices.getText(this, "preferencias"));
		}
		return preferencesMenuItem;
	}
	
	public JMenuItem getCloseMenuItem() {
		if (closeMenuItem == null){
			closeMenuItem = new JMenuItem(PluginServices.getText(this, "cerrar"));
		}
		return closeMenuItem;
	}
	
	public ROIManagerDialog getRoiManagerDialog() {
		return roiManagerDialog;
	}

	public ROIsTablePanel getTablePanel() {
		if (tablePanel==null)
			tablePanel = new ROIsTablePanel(this);
		return tablePanel;
	}
}
