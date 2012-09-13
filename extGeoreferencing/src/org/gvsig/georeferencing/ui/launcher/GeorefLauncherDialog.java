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
package org.gvsig.georeferencing.ui.launcher;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo con el cuadro de parámetros iniciales de la funcionalidad de georreferenciación.
 * 
 * 10/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GeorefLauncherDialog extends JPanel implements IWindow, IWindowListener {
	private static final long            serialVersionUID = 7362459094802955247L;
	private GeorefLauncherPanel          geoLauncherPanel = null;
	
	private String[]                     viewNameList = null;
	private int                          polynomialDegree;

	/**
	 * Tamaño de la ventana
	 */
	private int                          widthWindow      = 400;
	private int                          heightWindow     = 420;
		
	/**
	 * Constructor
	 * @param viewList Lista de nombres de las vistas disponibles
	 * @param degreeList grado máximo para la georreferenciación polinomial
	 */
	public GeorefLauncherDialog(String[] viewList, int polynomialDegree, ButtonsPanelListener listener) {
		this.viewNameList = viewList;
		this.polynomialDegree = polynomialDegree;
		
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		this.add(getGeorefLauncherPanel(listener), BorderLayout.CENTER);
	}
		
	/**
	 * Obtiene el panel general del lanzador  de la georreferenciación
	 * @return GeorefLauncherPanel
	 */
	public GeorefLauncherPanel getGeorefLauncherPanel(ButtonsPanelListener listener){
		if (geoLauncherPanel == null) 
			geoLauncherPanel = new GeorefLauncherPanel(viewNameList, polynomialDegree, listener);
		
		return geoLauncherPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
		m_viewinfo.setHeight(heightWindow);
		m_viewinfo.setWidth(widthWindow);
		return m_viewinfo;
	}
	
	public Object getWindowProfile(){
		return WindowInfo.DIALOG_PROFILE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowActivated()
	 */
	public void windowActivated() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.iver.andami.ui.mdiManager.IWindowListener#windowClosed()
	 */
	public void windowClosed() {
	}
	
	//-------Consulta de propiedades seleccionadas---------
	
	/**
	 * Obtiene la capa que ha sido abierta por el usuario 
	 * @return Obtiene la capa que ha sido abierta por el usuario o null si no 
	 * hay abierta ninguna.
	 */
	public FLyrRasterSE getLayer() {
		return geoLauncherPanel.getFileSelectionPanel().getLayer();
	}
	
	/**
	 * Obtiene la vista seleccionada
	 * @return
	 */
	public String getSelectedView() {
		return geoLauncherPanel.getTypeSelectionPanel().getSelectedView();
	}
	
	/**
	 * Obtiene el tipo de georreferenciación seleccionada
	 * @return entero con el tipo de georreferenciación. Es una constante contenida en la 
	 * clase georreferencing.
	 */
	public int getType() {
		return geoLauncherPanel.getTypeSelectionPanel().getType();
	}
	
	/**
	 * Obtiene el tipo de algoritmo seleccionado
	 * @return Cte definida en Georeferencing
	 */
	public int getAlgorithm() {
		return geoLauncherPanel.getAlgorithmSelectionPanel().getAlgorithm();
	}
	
	/**
	 * Obtiene el nombre del fichero de salida
	 * @return Fichero de salida
	 */
	public String getOutFile() {
		return geoLauncherPanel.getOutFileSelectionPanel().getOutFile();
	}
	
	/**
	 * Obtiene el método de interpolación
	 * @return Metodo de interpolación. El valor coincide con las constantes de GridInterpolation
	 */
	public int getInterpolationMethod() {
		return geoLauncherPanel.getAlgorithmSelectionPanel().getSelectedInterpolationMethod();
	}
	
	/**
	 * Obtiene el grado del algoritmo
	 * @return 
	 */
	public int getDegree() {
		String degree = ((String)geoLauncherPanel.getAlgorithmSelectionPanel().getDegreeList().getSelectedItem());
		String[] l = degree.split(" ");
		degree = l[l.length - 1];
		try {
			return Integer.valueOf(degree).intValue();
		}catch (NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en X
	 * @return DataInputContainer
	 */
	public double getXCellSizeValue() {
		return geoLauncherPanel.getCellSizePanel().getXCellSizeValue();
	}
	
	/**
	 * Obtiene el control para selección de tamaño de pixel en Y
	 * @return DataInputContainer
	 */
	public double getYCellSizeValue() {
		return geoLauncherPanel.getCellSizePanel().getYCellSizeValue();
	}

}

