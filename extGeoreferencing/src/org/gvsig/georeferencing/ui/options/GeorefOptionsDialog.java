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
package org.gvsig.georeferencing.ui.options;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.datainput.DataInputContainer;

import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Dialogo de opciones de georreferenciación
 * 
 * 10/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GeorefOptionsDialog extends JPanel implements IWindow, IWindowListener {
	private static final long            serialVersionUID = 7362459094802955247L;
	private GeorefOptionsPanel           geoOptionsPanel  = null;
	
	private int                          polynomialDegree;

	/**
	 * Tamaño de la ventana
	 */
	private int                          widthWindow      = 440;
	private int                          heightWindow     = 460;
		
	/**
	 * Constructor
	 * @param viewList Lista de nombres de las vistas disponibles
	 * @param degreeList grado máximo para la georreferenciación polinomial
	 */
	public GeorefOptionsDialog(int polynomialDegree, ButtonsPanelListener listener) {
		this.polynomialDegree = polynomialDegree;
		
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		this.add(getOptionsPanel(listener), BorderLayout.CENTER);
	}
		
	/**
	 * Obtiene el panel general del lanzador  de la georreferenciación
	 * @return GeorefLauncherPanel
	 */
	public GeorefOptionsPanel getOptionsPanel(ButtonsPanelListener listener) {
		if (geoOptionsPanel == null) 
			geoOptionsPanel = new GeorefOptionsPanel(polynomialDegree, listener);
		
		return geoOptionsPanel;
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
	 * Obtiene el tipo de algoritmo seleccionado
	 * @return entero con el tipo de algoritmo. Es una constante definida 
	 * en la clase georreferencing. 
	 */
	public int getAlgorithm() {
		return geoOptionsPanel.getAlgorithmSelectionPanel().getAlgorithm();
	}
	
	/**
	 * Obtiene el grado del algoritmo si este es polinomial
	 * @return entero con el grado del algoritmo. 
	 */
	public int getDegree() {
		return geoOptionsPanel.getAlgorithmSelectionPanel().getSelectedDegree();
	}
	
	/**
	 * Obtiene el método de interpolación del algoritmo si este es polinomial
	 * @return entero con el método de interpolación 
	 */
	public int getInterpolationMethod() {
		return geoOptionsPanel.getAlgorithmSelectionPanel().getSelectedInterpolationMethod();
	}
	
	/**
	 * Obtiene el tamaño de celda en X.
	 * @return double con el tamaño de celda. 
	 */
	public double getXCellSize() {
		return geoOptionsPanel.getCellSizePanel().getXCellSizeValue();
	}
	
	/**
	 * Asigna el tamaño de celda en X.
	 * @param cellSize
	 */
	public void setXCellSize(double cellSize) {
		geoOptionsPanel.getCellSizePanel().setXCellSize(cellSize);
	}
	
	/**
	 * Obtiene el tamaño de celda en Y.
	 * @return double con el tamaño de celda. 
	 */
	public double getYCellSize() {
		return geoOptionsPanel.getCellSizePanel().getYCellSizeValue();
	}
	
	/**
	 * Asigna el tamaño de celda en Y.
	 * @param cellSize
	 */
	public void setYCellSize(double cellSize) {
		geoOptionsPanel.getCellSizePanel().setYCellSize(cellSize);
	}
	
	/**
	 * Asigna el método de interpolación
	 * @param interpolationMethod
	 */
	public void setInterpolationMethod(int interpolationMethod) {
		geoOptionsPanel.getAlgorithmSelectionPanel().setInterpolationMethod(interpolationMethod);
	}
	
	/**
	 * Asigna el nombre del fichero de salida
	 * @param interpolationMethod
	 */
	public void setOutFile(String out) {
		geoOptionsPanel.getOutFileSelectionPanel().setOutFile(out);
	}
	
	/**
	 * Asigna el grado del algoritmo cuando es polinomial
	 * @param optiondegree
	 */
	public void setDegree(int optiondegree) {
		geoOptionsPanel.getAlgorithmSelectionPanel().setDegree(optiondegree);
	}
	
	/**
	 * Asigna el algoritmo
	 * @param alg
	 */
	public void setAlgorithm(int alg) {
		geoOptionsPanel.getAlgorithmSelectionPanel().setAlgorithm(alg);
	}
	
	/**
	 * Asigna el color de fonfo
	 * @param c
	 */
	public void setBackGroundColor(Color c) {
		geoOptionsPanel.getCheckOptionsPanel().getBackGroundColorSelector().setColor(c);
	}
	
	/**
	 * Obtiene el color de fondo
	 * @return
	 */
	public Color getBackGroundColor() {
		return geoOptionsPanel.getCheckOptionsPanel().getBackGroundColorSelector().getColor();
	}
	
	/**
	 * Asigna el color del texto
	 * @param c
	 */
	public void setTextColor(Color c) {
		geoOptionsPanel.getCheckOptionsPanel().getTextSelector().setColor(c);
	}
	
	/**
	 * Obtiene el color del texto
	 * @return
	 */
	public Color getTextColor() {
		return geoOptionsPanel.getCheckOptionsPanel().getTextSelector().getColor();
	}
	
	/**
	 * Obtiene el control para selección de umbral de error
	 * @return JButton
	 */
	public DataInputContainer getThresholdError() {
		return geoOptionsPanel.getCheckOptionsPanel().getThresholdError();
	}
	
	/**
	 * Asigna el control para selección de umbral de error
	 * @return JButton
	 */
	public void setThresholdError(double threshold) {
		geoOptionsPanel.getCheckOptionsPanel().getThresholdError().setValue(new Double(threshold).toString());
	}
}

