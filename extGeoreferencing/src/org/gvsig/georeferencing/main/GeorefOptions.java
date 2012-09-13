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
package org.gvsig.georeferencing.main;

import java.awt.Color;

import org.gvsig.raster.grid.GridInterpolated;

/**
 * Clase para el almacenamiento de opciones. Contiene propiedades y métodos para
 * su asignación y recuperación.
 * 
 * 31/01/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GeorefOptions {
	//	Opciones
	private int                           algorithm                = Georeferencing.AFFINE;
	private int                           degree                   = Georeferencing.DEFAULT_DEGREE;
	private Color                         backgroundColor          = Color.BLACK;
	private Color                         textColor                = Color.RED;
	private boolean                       showNumber               = true;
	private boolean                       addErrorsCSV             = false;
	private boolean                       centerView               = false;
	private double                        threshold                = 2.0;	
	private int                           interpolationMethod      = GridInterpolated.INTERPOLATION_NearestNeighbour;
	private String                        outFile                  = null;
	private int                           type                     = Georeferencing.WITH_MAP;
	private double                        xCellSize                = 0;
	private double                        yCellSize                = 0;
	
	/**
	 * Obtiene el grado del polinómio
	 * @return entero con el valor que representa el grado del polinómio
	 */
	public int getDegree() {
		return degree;
	}

	/**
	 * Asigna el grado del polinómio
	 * @param entero con el valor que representa el grado del polinómio
	 */
	public void setDegree(int optiondegree) {
		degree = optiondegree;
	}

	/**
	 * Obtiene el color de los gráficos
	 * @return Color
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * Asigna el color de los gráficos
	 * @param optionTextColor
	 */
	public void setTextColor(Color optionTextColor) {
		textColor = optionTextColor;
	}
	
	/**
	 * Obtiene al algoritmo a utilizar (Transformación afín, polinómico, ...)
	 * Las constantes están definidas en la clase Georreferencing
	 * @return
	 */
	public int getAlgorithm() {
		return algorithm;
	}

	/**
	 * Asigna el algoritmo a utilizar (Transformación afín, polinómico, ...)
	 * Las constantes están definidas en la clase Georreferencing
	 * @param
	 */
	public void setAlgorithm(int optionAlgorithm) {
		algorithm = optionAlgorithm;
	}

	/**
	 * Obtiene el color de fondo de las vistas
	 * @return Color
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * Asigna el color de fondo de las vistas
	 * @param optionBackgroundColor
	 */
	public void setBackgroundColor(Color optionBackgroundColor) {
		backgroundColor = optionBackgroundColor;
	}

	/**
	 * Consulta si los errores se escriben en el fichero CSV
	 * @return true si se escriben en el fichero y false si no se hace
	 */
	public boolean isAddErrorsCSV() {
		return addErrorsCSV;
	}

	/**
	 * Asigna el flag que dice si los errores se escriben en el fichero CSV
	 * @param true para escribirlos en el fichero y false para no hacerlo
	 */
	public void setAddErrorsCSV(boolean addErrorsCSV) {
		this.addErrorsCSV = addErrorsCSV;
	}

	/**
	 * Consulta si está activo el flag de centrar las vistas automáticamente sobre
	 * el punto que está seleccionado en la tabla.
	 * @return true para centrar automáticamente y false para no hacerlo
	 */
	public boolean isCenterView() {
		return centerView;
	}

	/**
	 * Asigna el flag que dice si se centran las vistas automáticamente sobre
	 * el punto que está seleccionado en la tabla.
	 * @return true para centrar automáticamente y false para no hacerlo
	 */
	public void setCenterView(boolean centerView) {
		this.centerView = centerView;
	}

	/**
	 * Consulta el flag que informa si se muestra el número del punto en la vista
	 * @return true si se muestra y false si no
	 */
	public boolean isShowNumber() {
		return showNumber;
	}

	/**
	 * Asigna el flag que dice si se muestra el número del punto en la vista
	 * @param true para mostrarlo y false para no hacerlo
	 */
	public void setShowNumber(boolean showNumber) {
		this.showNumber = showNumber;
	}

	/**
	 * Obtiene el umbral de error a partir del cual se iluminan en rojo en la tabla
	 * @return double con el valor del umbral
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * Asigna el umbral de error a partir del cual se iluminan en rojo en la tabla
	 * @param double con el valor del umbral
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Obtiene el método de interpolación del método polinomial
	 * @return Entero contenido como constante en la clas GridInterpolation
	 */
	public int getInterpolationMethod() {
		return interpolationMethod;
	}

	/**
	 * Asigna el método de interpolación del método polinomial
	 * @param Entero contenido como constante en la clas GridInterpolation
	 */
	public void setInterpolationMethod(int interpolationMethod) {
		this.interpolationMethod = interpolationMethod;
	}
	
	/**
	 * Obtiene el fichero de salida
	 * @return 
	 */
	public String getOutFile() {
		return outFile;
	}

	/**
	 * Asigna el fichero de salida
	 * @param outputFile
	 */
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	/**
	 * Obtiene el tipo de georreferenciación especificado en las constantes de la clase
	 * georreferencing.
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Asigna el tipo de georreferenciación especificado en las constantes de la clase
	 * georreferencing.
	 * @param
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Obtiene el tamaño de celda en X para la georreferenciación con remuestreo
	 * asignada por el usuario.
	 * @return
	 */
	public double getXCellSize() {
		return xCellSize;
	}

	/**
	 * Asigna el tamaño de celda en X para la georreferenciación con remuestreo 
	 * asignada por el usuario.
	 * @param
	 */
	public void setXCellSize(double cellSize) {
		this.xCellSize = cellSize;
	}
	
	/**
	 * Obtiene el tamaño de celda en Y para la georreferenciación con remuestreo
	 * asignada por el usuario.
	 * @return
	 */
	public double getYCellSize() {
		return yCellSize;
	}

	/**
	 * Asigna el tamaño de celda en Y para la georreferenciación con remuestreo 
	 * asignada por el usuario.
	 * @param
	 */
	public void setYCellSize(double cellSize) {
		this.yCellSize = cellSize;
	}
}

