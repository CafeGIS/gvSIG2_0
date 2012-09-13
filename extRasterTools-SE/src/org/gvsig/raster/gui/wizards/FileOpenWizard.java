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
 */
package org.gvsig.raster.gui.wizards;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.fmap.geom.exception.CreateEnvelopeException;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.geom.util.UtilFunctions;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.gui.ListManagerSkin;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.FPanelLocConfig;
import com.iver.utiles.listManager.ListManagerListener;

/**
 * Pestaña donde estara la apertura de ficheros
 *
 * @version 04/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FileOpenWizard extends WizardPanel implements ListManagerListener {
	private static final GeometryManager 	geomManager 				= GeometryLocator.getGeometryManager();
	private static final Logger 			logger        				= LoggerFactory.getLogger(FileOpenWizard.class);
	private static final String 			OPEN_LAYER_FILE_CHOOSER_ID 	= "OPEN_LAYER_FILE_CHOOSER_ID";
	private static final long 				serialVersionUID 			= 335310147513197564L;
	private static String    				lastPath        			= null;
	private JPanel            				jPanel2         			= null;
	private JFileChooser      				fileChooser    				= null;
	private CRSSelectPanel    				jPanelProj      			= null;
	private ListManagerSkin  				listManagerSkin 			= null;
	private boolean           				projection      			= false;
	static private FileFilter 				lastFileFilter  			= null;
	private TitledBorder      				titledBorder    			= null;

	/**
	 * Lista de manejadores de ficheros (extensiones)
	 */
	private ArrayList<IFileOpen> listFileOpen = new ArrayList<IFileOpen>();

	/**
	 * Construye un FileOpenWizard usando la extension por defecto
	 * 'FileExtendingOpenDialog'
	 */
	public FileOpenWizard() {
		this("FileExtendingOpenDialog");
	}

	/**
	 * Construye un FileOpenWizard usando el punto de extension pasado por
	 * parametro
	 *
	 * @param nameExtension
	 *
	 */
	public FileOpenWizard(String nameExtension) {
		this(nameExtension, true);
	}
	/**
	 * Construye un FileOpenWizard usando el punto de extension pasado por
	 * parametro
	 * @param nameExtension
	 */
	public FileOpenWizard(String nameExtension, boolean proj) {
		ExtensionPointManager epManager = ToolsLocator
				.getExtensionPointManager();
		if (!epManager.has(nameExtension))
			return;

		Iterator iterator = epManager.get(nameExtension).iterator();
		Object obj = null;
		while (iterator.hasNext()) {
			try {
				obj = ((Extension) iterator.next()).create();
			} catch (Exception e) {
				e.printStackTrace(); //FIXME Excepcion
				continue;
			}
			if (obj instanceof IFileOpen)
				listFileOpen.add((IFileOpen) obj);
		}
		init(null, proj);
	}

	/**
	 * Creates a new FileOpenWizard object.
	 * @param driverClasses
	 * @param proj
	 */
	public FileOpenWizard(IFileOpen[] driverClasses, boolean proj) {
		init(driverClasses, proj);
	}

	/**
	 * Creates a new FileOpenWizard object.
	 * @param driverClasses
	 */
	public FileOpenWizard(IFileOpen[] driverClasses) {
		init(driverClasses, true);
	}

	/**
	 * Creates a new FileOpenWizard object.
	 * @param driverClasses
	 * @param proj
	 * @param title
	 */
	public FileOpenWizard(IFileOpen[] driverClasses, boolean proj, String title) {
		setTitle(title);
		init(driverClasses, proj);
	}

	/**
	 * @param driverClasses2
	 * @param b
	 */
	private void init(IFileOpen[] driverClasses, boolean projection) {
		this.projection = projection;

		if (driverClasses != null)
			for (int i = 0; i < driverClasses.length; i++)
				listFileOpen.add(driverClasses[i]);

		if (lastPath == null) {
			Preferences prefs = Preferences.userRoot().node("gvsig.foldering");
			lastPath = prefs.get("DataFolder", null);
		}

		initialize();
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#initWizard()
	 */
	public void initWizard() {
		setTabName(PluginServices.getText(this, "Fichero_Raster"));
		init(null, true);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(514, 280);
		this.setLayout(null);
		this.add(getJPanel2(), null);
	}

	public File[] getFiles() {
		MyFile[] files = (MyFile[]) getListManagerSkin().getListManager().getListModel().getObjects().toArray(new MyFile[0]);
		File[] ret = new File[files.length];
		int pos = files.length - 1;
		for (int i = 0; i < files.length; i++) {
			ret[pos] = files[i].getFile();
			pos--;
		}
		return ret;
	}

	public MyFile[] getMyFiles() {
		return (MyFile[]) getListManagerSkin().getListManager().getListModel().getObjects().toArray(new MyFile[0]);
	}

	/**
	 * This method initializes jPanel2
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);
			jPanel2.setBorder(getTitledBorder());
			jPanel2.setPreferredSize(new Dimension(380, 200));
			jPanel2.setBounds(2, 2, 506, 472);
			if (projection)
				jPanel2.add(getJPanelProj(), null);
			jPanel2.add(getListManagerSkin(), null);
		}

		return jPanel2;
	}

	private TitledBorder getTitledBorder() {
		if (titledBorder == null) {
			titledBorder = BorderFactory.createTitledBorder(null, PluginServices.getText(this, "Seleccionar_fichero"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null);
			titledBorder.setTitle(PluginServices.getText(this, PluginServices.getText(this, "Capas")));
		}
		return titledBorder;
	}

	public String[] getDriverNames() {
		MyFile[] files = (MyFile[]) getListManagerSkin().getListManager().getListModel().getObjects().toArray(new MyFile[0]);
		String[] ret = new String[files.length];
		int pos = files.length - 1;

		for (int i = 0; i < files.length; i++) {
			ret[pos] = files[i].getFilter().getDescription();
			pos--;
		}
		return ret;
	}

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private CRSSelectPanel getJPanelProj() {
		if (jPanelProj == null) {
			IProjection proj = CRSFactory.getCRS("EPSG:23030");
			if (PluginServices.getMainFrame() != null)
				proj = AddLayerDialog.getLastProjection();

			jPanelProj = CRSSelectPanel.getPanel(proj);
			jPanelProj.setTransPanelActive(true);
			jPanelProj.setBounds(11, 400, 448, 35);
			jPanelProj.setPreferredSize(new Dimension(448, 35));
			jPanelProj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (jPanelProj.isOkPressed())
						AddLayerDialog.setLastProjection(jPanelProj.getCurProj());
				}
			});

		}
		return jPanelProj;
	}


	/**
	 * This method initializes listManagerDemoSkin
	 * @return ListManagerSkin
	 */
	private ListManagerSkin getListManagerSkin() {
		if (listManagerSkin == null) {
			listManagerSkin = new ListManagerSkin(false);
			listManagerSkin.setBounds(11, 21, 491, 363);
			listManagerSkin.getListManager().setListener(this);
		}
		return listManagerSkin;
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.listManager.ListManagerListener#addObjects()
	 */
	public Object[] addObjects() {
		this.callStateChanged(true);
		fileChooser = new JFileChooser(OPEN_LAYER_FILE_CHOOSER_ID, lastPath);
		fileChooser.setMultiSelectionEnabled(true);
		fileChooser.setAcceptAllFileFilterUsed(false);

		boolean finded = false;
		FileFilter auxFilter=null;
		for (int i = 0; i < listFileOpen.size(); i++) {
			IFileOpen fileOpen = listFileOpen.get(i);
			fileOpen.pre();
			List<FileFilter> aux = fileOpen.getFileFilter();

			for (int j = 0; j < aux.size(); j++) {
				FileFilter fileFilter = aux.get(j);
				fileChooser.addChoosableFileFilter(fileFilter);
				if (lastFileFilter!=null && lastFileFilter.getDescription().equals(fileFilter.getDescription())){
					auxFilter=fileFilter;
					finded = true;
				}
			}
		}
		if (finded && (lastFileFilter != null))
			fileChooser.setFileFilter(auxFilter);

		int result = fileChooser.showOpenDialog(this);

		File[] newFiles = null;
		ArrayList<MyFile> toAdd = new ArrayList<MyFile>();
		if (result == JFileChooser.APPROVE_OPTION) {
			lastPath = fileChooser.getCurrentDirectory().getAbsolutePath();
			lastFileFilter = fileChooser.getFileFilter();
			newFiles = fileChooser.getSelectedFiles();

			IFileOpen lastFileOpen = null;
			for (int i = 0; i < listFileOpen.size(); i++) {
				IFileOpen fileOpen = listFileOpen.get(i);
				List<FileFilter> aux = fileOpen.getFileFilter();
				for (int j = 0; j < aux.size(); j++)
					if (fileChooser.getFileFilter() == aux.get(j)) {
						for (int iFile=0; iFile<newFiles.length; iFile++)
							newFiles[iFile] = fileOpen.post(newFiles[iFile]);
						lastFileOpen = fileOpen;
						break;
					}
			}

			for (int ind = 0; ind < newFiles.length; ind++) {
				if (newFiles[ind] == null)
					break;
				toAdd.add(new MyFile(newFiles[ind], (fileChooser
						.getFileFilter()), lastFileOpen));
			}

			return toAdd.toArray();
		} else
			return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.cit.gvsig.gui.WizardPanel#execute()
	 */
	public void execute() {
		if (getFiles() == null)
			return;

		MapControl mapControl = null;
		IWindow[] w = PluginServices.getMDIManager().getAllWindows();

		// Si se está cargando la capa en el localizador se obtiene el mapcontrol de este
		for (int i = 0; i < w.length; i++)
			if (w[i] instanceof FPanelLocConfig) {
				mapControl = ((FPanelLocConfig) w[i]).getMapCtrl();
				DefaultListModel lstModel = (DefaultListModel) ((FPanelLocConfig) w[i]).getJList().getModel();
				lstModel.clear();
				for (int k = 0; k < getFiles().length; k++)
					lstModel.addElement(getFiles()[k].getName());
				for (int j = mapControl.getMapContext().getLayers().getLayersCount() - 1; j >= 0; j--) {
					FLayer lyr = mapControl.getMapContext().getLayers().getLayer(j);
					lstModel.addElement(lyr.getName());
				}
			}

		// Obtiene la primera vista activa
		if (mapControl == null)
			for (int i = 0; i < w.length; i++) {
				IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
				if (w[i] instanceof BaseView && w[i].equals(activeWindow))
					mapControl = ((BaseView) w[i]).getMapControl();
			}
		// Si no hay ninguna activa obtiene la primera vista aunque no esté activa
		if (mapControl == null)
			for (int i = 0; i < w.length; i++)
				if (w[i] instanceof BaseView)
					mapControl = ((BaseView) w[i]).getMapControl();

		//		if (mapControl == null) {
		//			return;
		//		}

		Envelope[] rects = new Envelope[getFiles().length];
		boolean first = false;

		for (int i = getMyFiles().length - 1; i >= 0; i--) {
			if (mapControl != null
					&& mapControl.getMapContext().getViewPort().getExtent() == null)
				first = true;
			rects[i] = (getMyFiles()[i]).createLayer(mapControl);
		}

		if (first && (rects.length > 1)) {
			Envelope rect;
			try {
				rect = geomManager.createEnvelope(
						rects[0].getMinimum(0), rects[0].getMinimum(1), rects[0]
								.getMaximum(0), rects[0].getMaximum(1),
								SUBTYPES.GEOM2D);
//				rect.setRect(rects[0]);
				for (int i = 0; i < rects.length; i++)
					if (rects[i] != null)
						rect.add(rects[i]);
				if (mapControl != null)
					mapControl.getMapContext().getViewPort().setEnvelope(rect);
			} catch (CreateEnvelopeException e) {
				logger.error("Error creating the envelope", e);
			}
		}
		if (mapControl != null)
			mapControl.getMapContext().endAtomicEvent();// FIXME ??? y el begin
	}

	public void setTitle(String title) {
		getTitledBorder().setTitle(title);
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.utiles.listManager.ListManagerListener#getProperties(java.lang.Object)
	 */
	public Object getProperties(Object selected) {
		return null;
	}


	/**
	 * Obtiene la última ruta seleccionada al añadir ficheros.
	 * @return Ruta del último fichero seleccionado
	 */
	public static String getLastPath() {
		return lastPath;
	}

	/**
	 * Asigna la última ruta en una selección de ficheros de disco. Es necesario
	 * poder hacer esta asignación desde fuera de FileOpenWizard ya que este path debe
	 * ser común a toda la aplicación. Hay otros puntos donde se seleccionan ficheros
	 * de disco.
	 * @param lastPath Ruta del último fichero de disco seleccionado
	 */
	public static void setLastPath(String path) {
		lastPath = path;
	}

	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}