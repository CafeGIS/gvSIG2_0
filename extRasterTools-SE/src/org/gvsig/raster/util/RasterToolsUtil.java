/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.propertiespanel.PropertiesComponent;
import org.gvsig.gui.beans.propertiespanel.PropertyStruct;
import org.gvsig.project.document.table.ExportStatisticsFile;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.dataset.Params.Param;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.View;
/**
 * Herramientas de uso general y que son dependientes de gvSIG, FMap o de
 * libUIComponents. En caso de no serlo existe una clase independiente de
 * cualquier proyecto dentro de libRaster para este tipo de funciones.
 *
 * @version 31/05/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RasterToolsUtil {

	/**
	 * Obtiene la lista de capas del TOC y devuelve las raster. Si hay capas agrupadas lo tiene en cuenta y mira
	 * dentro de la agrupación.
	 * @param srcLyrs FLayers de la vista
	 * @param destLyrs Lista de capas 
	 * @return ArrayList con la lista de capas raster agrupadas o no. El orden en que aparecen
	 * en la lista es de abajo a arriba las que aparecen en el TOC.
	 */
	public static ArrayList getRasterLayerList(FLayers srcLyrs, ArrayList destLyrs) {
		if(destLyrs == null)
			destLyrs = new ArrayList();
		for (int i = 0; i < srcLyrs.getLayersCount(); i++) {
			if(srcLyrs.getLayer(i) instanceof FLyrRasterSE)
				destLyrs.add(srcLyrs.getLayer(i));
			if(srcLyrs.getLayer(i) instanceof FLayers)
				destLyrs = getLayerList((FLayers)srcLyrs.getLayer(i), destLyrs);
		}
		return destLyrs;
	}
	
	/**
	 * Obtiene la lista de capas del TOC y devuelve las raster. Si hay capas agrupadas lo tiene en cuenta y mira
	 * dentro de la agrupación.
	 * @param srcLyrs FLayers de la vista
	 * @param destLyrs Lista de capas 
	 * @return ArrayList con la lista de capas raster agrupadas o no. El orden en que aparecen
	 * en la lista es de abajo a arriba las que aparecen en el TOC.
	 */
	public static ArrayList getLayerList(FLayers srcLyrs, ArrayList destLyrs) {
		if(destLyrs == null)
			destLyrs = new ArrayList();
		for (int i = 0; i < srcLyrs.getLayersCount(); i++) {
			if(srcLyrs.getLayer(i) instanceof FLayers)
				destLyrs = getLayerList((FLayers)srcLyrs.getLayer(i), destLyrs);
			else 
				destLyrs.add(srcLyrs.getLayer(i));
		}
		return destLyrs;
	}
	
	/**
	 * Obtiene el nombre de la vista donde está cargada la capa que se pasa por parámetro
	 * @param layer Capa cargada en una vista
	 * @return Nombre de la vista donde está cargada la capa.
	 */
	public static String getView(FLayer layer) {
		Project p = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
		return p.getView(layer);	
	}
	
	/**
	 * Devuelve la traducción de un texto. En caso de no existir la traducción al idioma seleccionado
	 * devolverá la cadena de entrada.
	 * @param parent Ventana padre que contiene el objeto con la traducción
	 * @param text Texto a traducir
	 * @return Texto traducido o cadena de entrada en caso de no encontrar una traducción
	 */
	public static String getText(Object parent, String text) {
		return PluginServices.getText(parent, text);
	}
	
	/**
	 * Obtiene un icono definido por la etiqueta que se especifica en el 
	 * parámetro
	 * @param ico Etiqueta del icono
	 * @return Icono
	 */
	public static ImageIcon getIcon(String ico) {
		return PluginServices.getIconTheme().get(ico);	
	}
	
	/**
	 * Añade una ventana al gestor de ventanas
	 * @param window
	 */
	public static void addWindow(IWindow window) {
		PluginServices.getMDIManager().addWindow(window);
	}
	
	/**
	 * Elimina una ventana al gestor de ventanas
	 * @param window
	 */
	public static void closeWindow(IWindow window) {
		PluginServices.getMDIManager().closeWindow(window);
	}
	
	/**
	 * Selecciona los controles del panel de propiedades a partir de los parámtros
	 * obtenidos del driver. Este método realiza una transformación entre Params
	 * obtenido del driver de escritura y los parámetros del panel de propiedades.
	 * @param panel Panel de propiedades
	 * @param params Parámetros del driver
	 * @param notTakeIntoAccount Nombre de parámetros que no hay que tener en cuenta. Si es null se tienen en cuenta todos.
	 */
	public static void loadPropertiesFromWriterParams(PropertiesComponent pComp, Params params, String[] notTakeIntoAccount) {
		for (int i = 0; i < params.getNumParams(); i++) {
			Param p = params.getParam(i);
			String name = getText(null, p.id);
			String key = p.id;

			//Miramos si el parámetro coincide con  alguno en la lista de parametros que no hay que
			//tener en cuenta. Si es así no lo añadimos
			if(notTakeIntoAccount != null && notTakeIntoAccount.length > 0) {
				boolean jump = false;
				for (int j = 0; j < notTakeIntoAccount.length; j++) {
					if (key.equals(notTakeIntoAccount[j]))
						jump = true;
				}
				if(jump)
					continue;
			}

			Object[] types = null;
			int selectedValue = 0;

			switch (p.type) {
				case Params.CHECK:
					pComp.addValue(name, key, p.defaultValue, types);
					break;
				case Params.CHOICE:
					ArrayList list = new ArrayList();
					for (int j = 0; j < p.list.length; j++) {
						list.add(p.list[j]);
						if (p.defaultValue instanceof Integer)
							if (((Integer) p.defaultValue).intValue() == j)
								selectedValue = j;
					}
					types = new Object[] { new Integer(PropertiesComponent.TYPE_COMBO), list };
					pComp.addValue(name, key, new Integer(selectedValue), types);
					break;
				case Params.SLIDER:
					types = new Object[] { new Integer(PropertiesComponent.TYPE_SLIDER), new Integer(p.list[0]), new Integer(p.list[1]) };
					pComp.addValue(name, key, p.defaultValue, types);
					break;
				default:
					pComp.addValue(getText(null, params.getParam(i).id), params.getParam(i).id, params.getParam(i).defaultValue, null);
					break;
			}
		}
	}

	/**
	 * Carga los parámetros del escritor WriterParams con los valores obtenidos
	 * de la ventana de propiedades.
	 */
	public static void loadWriterParamsFromPropertiesPanel(PropertiesComponent pComp, Params params) {
		ArrayList values = pComp.getValues();
		for (int iParam = 0; iParam < params.getNumParams(); iParam++) {
			Param p = (Param) params.getParam(iParam);
			for (int iValue = 0; iValue < values.size(); iValue++) {
				PropertyStruct prop = ((PropertyStruct) values.get(iValue));
				if (p.id.compareTo(prop.getKey()) == 0) {
					switch (p.type) {
						case Params.CHECK:
							p.defaultValue = (Boolean) prop.getNewValue();
							break;
						case Params.CHOICE:
							p.defaultValue = ((Integer) prop.getNewValue());//p.list[((Integer) prop.getNewValue()).intValue()];
							break;
						case Params.SLIDER:
							try {
								p.defaultValue = (Integer)prop.getNewValue();
							} catch (NumberFormatException e) {}
					}
					break;
				}
			}
		}
	}

	/**
	 * Función que devuelve true si se tiene permiso de escritura en la ruta
	 * indicada en el parámetro path y false si no los tiene.
	 * @param path Ruta a comprobar los permisosv
	 * @param pluginObject si es distinto de null se obtiene un mensaje de
	 *          advertencia y sirve como parámetro para getText de la traducción.
	 *          Si es null no se mostrará ventana de advertencia
	 * @return true si se tiene permiso de escritura en la ruta indicada en el
	 *         parámetro path y false si no los tiene.
	 */
	public static boolean canWrite(String path, Object pluginObject) {
		File f = new File(path);
		if(f.exists() && f.canWrite())
			return true;
		else {
			if(pluginObject != null)
				JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),
						PluginServices.getText(pluginObject, "error_escritura"));
			return false;
		}
	}

	/**
	 * Muestra un dialogo con un texto y un botón Si o No.
	 * @param msg Mensaje a mostrar en el dialogo.
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 * @return El botón seleccionado por el usuario. true si ha seleccionado "si"
	 *         y false si ha seleccionado "no"
	 */
	public static boolean messageBoxYesOrNot(String msg, Object parentWindow){
		String string1 = PluginServices.getText(parentWindow, "yes");
		String string2 = PluginServices.getText(parentWindow, "no");
		Object[] options = {string1, string2};
		int n = JOptionPane.showOptionDialog((Component)PluginServices.getMainFrame(),
					"<html>" + PluginServices.getText(parentWindow, msg).replaceAll("\n", "<br>") + "</html>",
					PluginServices.getText(parentWindow, "confirmacion"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					string1);
		if (n == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	/**
	 * Muestra un dialogo de error con un texto y un botón de aceptar.
	 * @param msg Mensaje a mostrar en el dialogo.
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 */
	public static void messageBoxError(String msg, Object parentWindow){
		String string = PluginServices.getText(parentWindow, "accept");
		Object[] options = {string};
		JOptionPane.showOptionDialog((Component)PluginServices.getMainFrame(),
					"<html>" + PluginServices.getText(parentWindow, msg).replaceAll("\n", "<br>") + "</html>",
					PluginServices.getText(parentWindow, "confirmacion"),
					JOptionPane.OK_OPTION,
					JOptionPane.ERROR_MESSAGE,
					null,
					options,
					string);
	}

	/**
	 * Muestra un dialogo de información con un texto y un botón de aceptar.
	 * @param msg Mensaje a mostrar en el dialogo. Identificador de la cadena a traducir
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 */
	public static void messageBoxInfo(String msg, Object parentWindow){
		String string = PluginServices.getText(parentWindow, "accept");
		Object[] options = {string};
		JOptionPane.showOptionDialog((Component)PluginServices.getMainFrame(),
					"<html>" + PluginServices.getText(parentWindow, msg).replaceAll("\n", "<br>") + "</html>",
					PluginServices.getText(parentWindow, "confirmacion"),
					JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null,
					options,
					string);
	}

	/**
	 * Registra un mensaje de error en el log de gvSIG
	 * @param msg Mensaje a guardar en el log
	 * @param parent Objeto que hizo disparar el mensaje
	 * @param exception Excepcion que ha sido recogida
	 */
	public static void debug(String msg, Object parent, Exception exception) {
		if(parent != null)
		    LoggerFactory
            .getLogger(parent.getClass()).debug(PluginServices.getText(parent, msg), exception);
	}

	/**
	 * Muestra un dialogo de error con un texto y un botón de aceptar. El error es
	 * registrado en el log de gvSIG con la excepcion que se le pase por parametro
	 * @param msg Mensaje a mostrar en el dialogo.
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 * @param exception Excepcion que ha sido recogida
	 */
	public static void messageBoxError(String msg, Object parentWindow, Exception exception) {
		debug(msg, parentWindow, exception);
		messageBoxError(msg, parentWindow);
	}
	
	/**
	 * Muestra un dialogo de error con un texto y un botón de aceptar. Se le pasa como último parámetros
	 * una lista de excepciones que serán guardadas en el log
	 * @param msg Mensaje a mostrar en el dialogo.
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 * @param exception Excepcion que ha sido recogida
	 */
	public static void messageBoxError(String msg, Object parentWindow, ArrayList exception) {
		for (int i = 0; i < exception.size(); i++) {
			if(exception.get(i) instanceof Exception)
				debug(msg, parentWindow, (Exception)exception.get(i));
		}
		messageBoxError(msg, parentWindow);
	}

	/**
	 * Muestra un dialogo de información con un texto y un botón de aceptar. El
	 * mensaje informativo es registrado en el log de gvSIG con la excepcion que
	 * se le pase por parametro.
	 * @param msg Mensaje a mostrar en el dialogo. Identificador de la cadena a
	 *          traducir
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 * @param exception Excepcion que ha sido recogida
	 */
	public static void messageBoxInfo(String msg, Object parentWindow, Exception exception) {
		debug(msg, parentWindow, exception);
		messageBoxInfo(msg, parentWindow);
	}

	/**
	 * Muestra un dialogo con un texto y un botón Si o No. El mensaje es
	 * registrado en el log de gvSIG con la excepcion que se le pase por
	 * parametro.
	 * @param msg Mensaje a mostrar en el dialogo.
	 * @param parentWindow Ventana desde la que se lanza el dialogo
	 * @return El botón seleccionado por el usuario. true si ha seleccionado "si"
	 *         y false si ha seleccionado "no"
	 */
	public static boolean messageBoxYesOrNot(String msg, Object parentWindow, Exception exception) {
		debug(msg, parentWindow, exception);
		return messageBoxYesOrNot(msg, parentWindow);
	}

	/**
	 * Carga una capa raster en una vista de gvSIG.
	 * @param viewName Nombre de la vista donde ha de cargarse. Si vale null se cargará en la
	 * primera vista que encuentre que esté activa. Si no hay ninguna saltará una excepción.
	 * @param fileName Nombre del fichero a cargar. No debe ser nulo nunca.
	 * @param layerName Nombre de la capa. Si es null se asignará el nombre del
	 * fichero sin extensión.
	 * @throws RasterNotLoadException Excepción que se lanza cuando no se ha podido cargar la capa
	 * por algún motivo.
	 */
	public static FLayer loadLayer(String viewName, String fileName, String layerName) throws RasterNotLoadException {
		if(fileName ==  null)
			return null;

		//Seleccionamos la vista de gvSIG
		View theView = null;
		try {
			IWindow[] allViews = PluginServices.getMDIManager().getAllWindows();
			if(viewName != null) {
				for (int i = 0; i < allViews.length; i++) {
					if (allViews[i] instanceof View
						&& PluginServices.getMDIManager().getWindowInfo((View) allViews[i]).getTitle().equals(viewName))
						theView = (View) allViews[i];
				}
			} else {
				IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
				for (int i = 0; i < allViews.length; i++) {
					if (allViews[i] instanceof View && ((View)allViews[i]) == activeWindow) //En la primera vista activa
						theView = (View) allViews[i];
				}
				if(theView == null) {
					for (int i = 0; i < allViews.length; i++) {
						if (allViews[i] instanceof View) //En la primera vista
							theView = (View) allViews[i];
					}
				}
			}

			if (theView == null)
				throw new RasterNotLoadException("Imposible cargar la capa.");
		} catch (ClassCastException ex) {
			throw new RasterNotLoadException("No se puede hacer un casting de esa IWindow a View.");
		}

		theView.getMapControl().getMapContext().beginAtomicEvent();

		FLayer lyr = null;
		try {
			if(layerName == null) {
				int endIndex = fileName.lastIndexOf(".");
				if (endIndex < 0)
					endIndex = fileName.length();
				lyr = FLyrRasterSE.createLayer(
						fileName.substring(fileName.lastIndexOf(File.separator) + 1, endIndex),
						new File(fileName),
						theView.getMapControl().getProjection());
			} else {
				lyr = FLyrRasterSE.createLayer(
						layerName,
						new File(fileName),
						theView.getMapControl().getProjection());
			}

		} catch (LoadLayerException e) {
			throw new RasterNotLoadException("Error al cargar la capa.");
		}
		theView.getMapControl().getMapContext().getLayers().addLayer(lyr);
		theView.getMapControl().getMapContext().invalidate();
		theView.getMapControl().getMapContext().endAtomicEvent();
		return lyr;
	}
	
	/**
	 * Carga una capa raster en una vista de gvSIG.
	 * @param viewName Nombre de la vista donde ha de cargarse. Si vale null se cargará en la
	 * primera vista que encuentre que esté activa. Si no hay ninguna saltará una excepción.
	 * @param fileName Nombre del fichero a cargar. No debe ser nulo nunca.
	 * @param layerName Nombre de la capa. Si es null se asignará el nombre del
	 * fichero sin extensión.
	 * @throws RasterNotLoadException Excepción que se lanza cuando no se ha podido cargar la capa
	 * por algún motivo.
	 */
	public static FLayer loadLayer(String viewName, FLayer lyr) throws RasterNotLoadException {
		if(lyr ==  null)
			return null;

		//Seleccionamos la vista de gvSIG
		View theView = null;
		try {
			IWindow[] allViews = PluginServices.getMDIManager().getAllWindows();
			if(viewName != null) {
				for (int i = 0; i < allViews.length; i++) {
					if (allViews[i] instanceof View
						&& PluginServices.getMDIManager().getWindowInfo((View) allViews[i]).getTitle().equals(viewName))
						theView = (View) allViews[i];
				}
			} else {
				IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
				for (int i = 0; i < allViews.length; i++) {
					if (allViews[i] instanceof View && ((View)allViews[i]) == activeWindow) //En la primera vista activa
						theView = (View) allViews[i];
				}
				if(theView == null) {
					for (int i = 0; i < allViews.length; i++) {
						if (allViews[i] instanceof View) //En la primera vista
							theView = (View) allViews[i];
					}
				}
			}

			if (theView == null)
				throw new RasterNotLoadException("Imposible cargar la capa.");
		} catch (ClassCastException ex) {
			throw new RasterNotLoadException("No se puede hacer un casting de esa IWindow a View.");
		}

		theView.getMapControl().getMapContext().beginAtomicEvent();
		theView.getMapControl().getMapContext().getLayers().addLayer(lyr);
		theView.getMapControl().getMapContext().invalidate();
		theView.getMapControl().getMapContext().endAtomicEvent();
		return lyr;
	}
	
	/**
	 * Calculo de las coordenadas de una ventana IWindow para que quede centrada sobre el 
	 * MainFrame. Estas coordenadas solo valen para un IWindow ya que andami mete las ventanas
	 * con coordenadas relativas a su ventanta principal.
	 * @param widthWindow Ancho de la ventana a añadir
	 * @param heightWindow Alto de la ventana a añadir
	 * @return Array con el ancho y el alto 
	 */
	public static Point iwindowPosition(int widthWindow, int heightWindow) {
		int posWindowX = 0;
		int posWindowY = 0;
		Dimension dim = null;
		Point pos = null;
		if(PluginServices.getMainFrame() instanceof Component) {
			dim = ((Component)PluginServices.getMainFrame()).getSize();
			pos = ((Component)PluginServices.getMainFrame()).getLocation();
			if(dim != null && pos != null) {
				posWindowX = ((dim.width >> 1) - (widthWindow >> 1));
				posWindowY = ((dim.height >> 1) - (heightWindow >> 1) - 70);
				return new Point(posWindowX, posWindowY);
			}
		}
		return null;
	}
}