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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.filechooser.FileFilter;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.gui.wizards.projection.RasterProjectionActionsDialog;
import org.gvsig.raster.gui.wizards.projection.RasterProjectionActionsPanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.geolocation.ui.GeoLocationOpeningRasterDialog;
import org.gvsig.rastertools.raw.ui.main.OpenRawFileDefaultView;
import org.gvsig.rastertools.reproject.ui.LayerReprojectPanel;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
/**
 * Clase que indicará que ficheros puede tratar al panel de apertura de
 * ficheros
 *
 * @version 04/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class FileOpenRaster extends AbstractFileOpen {
	public static int                     CHANGE_VIEW_PROJECTION = 0;
	public static int                     REPROJECT              = 1;
	public static int                     IGNORE                 = 2;
	public static int                     NOTLOAD                = 3;
	/**
	 * Acción a realizar con la capa. Por defecto ignora la proyección y la carga
	 * en la vista pero esta opción puede ser cambiada por el usuario desde el
	 * dialogo.
	 */
	public static int                     defaultActionLayer     = IGNORE;
	/**
	 * Lista de acciones. Una por capa a añadir.
	 */
	private ArrayList                     actionList             = new ArrayList(); ;

	private ArrayList                     lyrsRaster             = new ArrayList();
	private RasterProjectionActionsDialog dialog                 = null;
	private MapControl                    mapControl             = null;

	/**
	 * Constructor de FileOpenRaster
	 */
	public FileOpenRaster() {
		getFileFilter().add(new DriverFileFilter());
		RasterProjectionActionsPanel.selectAllFiles = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.wizards.IFileOpen#post(java.io.File[])
	 */
	public File post(File file) {

		//Si el fichero es raw lanzamos el dialogo de parámetros de raw
		if (RasterUtilities.getExtensionFromFileName(file.getAbsolutePath()).equals("raw")) {
			OpenRawFileDefaultView view = new OpenRawFileDefaultView(file.getAbsolutePath());
 			file = view.getImageFile();
		}

		if(file == null || file.getAbsoluteFile() == null) {
			return null;
		}

		//Si el fichero es vrt chequeamos que sea correcto
		if (RasterUtilities.getExtensionFromFileName(file.getAbsolutePath()).equals("vrt")) {
			try {
				checkFileVRT(file);
			} catch (FileOpenVRTException e) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_abrir_fichero") + " " + file.getName() + "\n\n" + PluginServices.getText(this, "informacion_adicional") + ":\n\n  " + e.getMessage(), this, e);
				return null;
			}
		}

		try {
			FLyrRasterSE lyrRaster = null;
			lyrRaster = FLyrRasterSE.createLayer(file.getName(), file, null);

			// Si hay que generar las overviews por el panel de preferencias
//			if (Configuration.getValue("overviews_ask_before_loading", Boolean.FALSE).booleanValue() == true) {
//				try {
//					boolean generate = false;
//					for (int i = 0; i < lyrRaster.getFileCount(); i++) {
//						if (lyrRaster.getDataSource().getDataset(i)[0].getOverviewCount(0) == 0) {
//							generate = true;
//							break;
//						}
//					}
//					if (generate) {
//						if (firstTaskOverview) {
//							execOverview = RasterToolsUtil.messageBoxYesOrNot("generar_overviews", this);
//							firstTaskOverview = false;
//						}
//
//						if (execOverview) {
//							RasterProcess process = new OverviewsProcess();
//							process.setCancelable(false);
//							process.addParam("layer", (FLyrRasterSE) lyrRaster);
//							UniqueProcessQueue.getSingleton().add(process);
//						}
//					}
//				} catch (Exception e) {
//					// Si no se puede generar la overview no hacemos nada
//				}
//			}

			//Mostramos el cuadro que pide la georreferenciación si la capa no tiene y si la opción está activa en preferencias
			if(RasterModule.askCoordinates) {
				if(	lyrRaster.getFullEnvelope().getMinimum(0) == 0 &&
						lyrRaster.getFullEnvelope().getMinimum(1) == 0 &&
						lyrRaster.getFullEnvelope().getMaximum(0) == (lyrRaster).getPxWidth() &&
						lyrRaster.getFullEnvelope().getMaximum(1) == (lyrRaster).getPxHeight()) {
					if(RasterToolsUtil.messageBoxYesOrNot(lyrRaster.getName() + "\n" + PluginServices.getText(this, "layer_without_georref"), null)) {
						GeoLocationOpeningRasterDialog gld = new GeoLocationOpeningRasterDialog(lyrRaster);
						PluginServices.getMDIManager().addWindow(gld);
					}
				}
			}

			//Opciones de proyección
			boolean compareProj = Configuration.getValue("general_ask_projection", Boolean.valueOf(false)).booleanValue();
			if(compareProj) {
				compareProjections(lyrRaster);
			} else {
				actionList.add(new Integer(defaultActionLayer));
			}

			lyrsRaster.add(lyrRaster);
		} catch (LoadLayerException e) {
			RasterToolsUtil.messageBoxError("error_carga_capa", this, e);
		}

		return super.post(file);
	}


	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.wizards.IFileOpen#execute(java.io.File[])
	 */
	public Envelope createLayer(File file, MapControl mapControl,
			FileFilter driverName, IProjection proj) {
		this.mapControl = mapControl;
		FLyrRasterSE lyr = null;
		String layerName = file.getName();
		int nLayer = -1;

		// Si hay capas en la lista la buscamos allí
		if (lyrsRaster.size() != 0) {
			for (int i = 0; i < lyrsRaster.size(); i++) {
				if (((FLyrRasterSE) lyrsRaster.get(i)).getName().equals(layerName)) {
					lyr = (FLyrRasterSE) lyrsRaster.get(i);
					lyr.setProjection(proj);
					nLayer = i;
				}
			}
		}

		// Si no hay capa la cargamos
		if (lyr == null) {
			try {
				lyr = FLyrRasterSE.createLayer(layerName, file, proj);
				layerActions(defaultActionLayer, lyr);
			} catch (LoadLayerException e) {
				RasterToolsUtil.messageBoxError("error_carga_capa", this, e);
			}
		}

		if (lyr != null) {
			boolean actionsAdded = false;
			if ((nLayer >= 0) && (nLayer < actionList.size())) {
				Object obj = actionList.get(nLayer);
				if (obj != null && obj instanceof Integer) {
					actionsAdded = true;
					layerActions(((Integer) obj).intValue(), lyr);
				}
			}
			if (!actionsAdded) {
				layerActions(defaultActionLayer, lyr);
			}
			return lyr.getFullEnvelope();
		}

		return null;
	}

	/**
	 * Compara las proyecciones de la vista y la capa. En caso de ser diferentes
	 * pregunta por las opciones a realizar.
	 * @param lyr
	 * @param mapControl
	 */
	private void compareProjections(FLyrRasterSE lyr) {
		IProjection viewProj = getMapControl().getProjection();
		IProjection lyrProj = lyr.readProjection();
		if (lyrProj == null) {
			lyr.setProjection(viewProj);
			actionList.add(new Integer(defaultActionLayer));
			return;
		}
		if(viewProj == null) {
			getMapControl().setProjection(lyrProj);
			actionList.add(new Integer(defaultActionLayer));
			return;
		}

		/*
		 * Si las proyecciones de vista y raster son distintas se lanza el dialogo de selección de
		 * opciones. Este dialogo solo se lanza en caso de que el checkbox de aplicar a todos los
		 * ficheros no haya sido marcado. En este caso para el resto de ficheros de esa selección
		 * se hará la misma acción que se hizo para el primero.
		 */
		if (!viewProj.getAbrev().endsWith(lyrProj.getAbrev())) {
			boolean showDialog = false;
			if (!RasterProjectionActionsPanel.selectAllFiles) {
				showDialog = true;
			}

			if (showDialog) {
				dialog = new RasterProjectionActionsDialog(lyr);
			} else {
				if (defaultActionLayer == FileOpenRaster.REPROJECT && !lyr.isReproyectable()) {
					dialog = new RasterProjectionActionsDialog(lyr);
				}
			}
			int select = defaultActionLayer;
			if (dialog != null) {
				select = dialog.getRasterProjectionActionsPanel().getSelection();
			}

			actionList.add(new Integer(select));
			return;
		}
		actionList.add(new Integer(defaultActionLayer));
	}

	/**
	 * Acciones posibles cuando la proyección de la capa es distinta de la de
	 * la vista.
	 * @param action Valor de la acción. Está representado en RasterProjectionActionsPanel
	 */
	private void layerActions(int action, FLyrRasterSE lyr) {
		// Cambia la proyección de la vista y carga la capa
		if (action == CHANGE_VIEW_PROJECTION) {
			if (lyr != null) {
				getMapControl().setProjection(lyr.readProjection());
				lyr.setVisible(true);
				getMapControl().getMapContext().getLayers().addLayer(lyr);
			}
		}

		// Ignora la proyección de la capa y la carga
		if (action == IGNORE) {
			if (lyr != null) {
				lyr.setVisible(true);
				getMapControl().getMapContext().getLayers().addLayer(lyr);
			}
		}

		// Reproyectando
		if (action == REPROJECT) {
			LayerReprojectPanel reprojectPanel = new LayerReprojectPanel(lyr, Boolean.FALSE);
			RasterToolsUtil.addWindow(reprojectPanel);
		}

		// No carga
		if (action == NOTLOAD) {
		}
	}

	/**
	 * Comprueba si un fichero VRT esta en correcto estado, en caso contrario
	 * lanza una excepcion indicando el tipo de error en la apertura.
	 *
	 * @param file
	 * @throws FileOpenVRTException
	 */
	private void checkFileVRT(File file) throws FileOpenVRTException {
		KXmlParser parser = new KXmlParser();

		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			parser.setInput(fileReader);

			parser.nextTag();

			parser.require(XmlPullParser.START_TAG, null, "VRTDataset");

			while (parser.nextTag () != XmlPullParser.END_TAG) {
				parser.require(XmlPullParser.START_TAG, null, "VRTRasterBand");

				String name;
				while (parser.nextTag() != XmlPullParser.END_TAG) {
					parser.require(XmlPullParser.START_TAG, null, null);
					boolean relativePath = false;
					for (int i=0; i<parser.getAttributeCount(); i++) {
						if (parser.getAttributeName(i).equals("relativetoVRT") &&
								parser.getAttributeValue(i).equals("1")) {
							relativePath = true;
						}
					}
					name = parser.getName();
					String nameFile = parser.nextText();
					if (name.equals("SourceFilename")) {
						if (relativePath) {
							nameFile = file.getParent() + File.separator + nameFile;
						}
						File tryFile = new File(nameFile);
						if (!tryFile.exists()) {
							throw new FileOpenVRTException(PluginServices.getText(this, "no_existe_fichero") + " " + nameFile);
						}
					}
					parser.require(XmlPullParser.END_TAG, null, name);
				}

				parser.require(XmlPullParser.END_TAG, null, "VRTRasterBand");
			}
			parser.require(XmlPullParser.END_TAG, null, "VRTDataset");
			parser.next();
			parser.require(XmlPullParser.END_DOCUMENT, null, null);
		} catch (XmlPullParserException e) {
			throw new FileOpenVRTException(PluginServices.getText(this, "el_fichero")+ " " + file.getName().toString() + " " + PluginServices.getText(this, "esta_formato_desconocido"));
		} catch (IOException e) {
			throw new FileOpenVRTException(PluginServices.getText(this, "no_puede_abrir_fichero") + " " + file.getName().toString());
		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Obtiene el mapControl. La llamada createLayer recibe el MapControl por parámetro pero
	 * ni pre() ni post() pueden tener acceso al MapControl a no ser que lo busquen ellos.
	 * @return MapControl
	 */
	public MapControl getMapControl() {
		if (mapControl != null) {
			return mapControl;
		}

		IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
		if ((activeWindow != null) && (activeWindow instanceof View)) {
			return ((View) activeWindow).getMapControl();
		}

		IWindow[] w = PluginServices.getMDIManager().getAllWindows();
		// Obtiene la primera vista activa
		for (int i = 0; i < w.length; i++) {
			if (w[i] instanceof BaseView && w[i].equals(PluginServices.getMDIManager().getActiveWindow())) {
				return ((BaseView) w[i]).getMapControl();
			}
		}

		// Si no hay ninguna activa obtiene la primera vista aunque no esté activa
		for (int i = 0; i < w.length; i++) {
			if (w[i] instanceof BaseView) {
				return ((BaseView) w[i]).getMapControl();
			}
		}

		return null;
	}

	public void pre() {}

}