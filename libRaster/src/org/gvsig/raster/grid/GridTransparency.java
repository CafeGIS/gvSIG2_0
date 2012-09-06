/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid;

import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.datastruct.TransparencyRange;

//TODO: FUNCIONALIDAD: Convertir la transparencia a String y viceversa para salvar proyecto
/**
 * <P>
 * Representa el estado de transparencia de un grid. 
 * </P>
 * <P>
 * El estado de transparencia de un raster puede verse modificado desde los siguientes sitios:
 * <UL>
 * <LI>Color con transparencia definido en los metadatos de la imagen. Caracteristica de PNG.</LI>
 * <LI>Una banda del raster definida como banda de transparencia. PNGs, IMGs o TIFFs 
 * pueden tener bandas de transparencia</LI>
 * <LI>Tablas de color con valores de transparencia. GIF y PNG pueden tener tablas 
 * que definan entradas de color con transparencia</LI>
 * <LI>Información de transparencia en la cabecera raster. Tipicamente valores NO_DATA suelen 
 * ser interpretados como transparentes en la visualización.</LI>
 * <LI>Modificación hecha por el usuario. En la visualización un usuario puede necesitar
 * asignar valores o rangos de valores como transparentes.</LI>
 * </UL> 
 * </P>
 * <P>
 * Estas transparencias quedan representadas por cinco tipo básicos:
 * <UL>
 * <LI><b>Transparencia por rangos. </b>Esta se aplicará antes que ninguna y siempre sobre los valores
 * base del raster.</LI>
 * <LI><b>Banda de transparencia en un raster. </b></LI>
 * <LI>Máscara de transparencia</LI>
 * <LI>Opacidad</LI>
 * <LI>Zona de recorte</LI>
 * </UL>
 * </P>
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class GridTransparency extends Transparency {
	/**
	 * Flag de activación de la transparencia cuando se visualiza o exporta.
	 */
	private boolean 		transparencyActive           = false;
			
	/**
	 * Constructor. Crea un objeto con las propiedades de la transparencia de un grid. 
	 * Las variables conservan sus valores por defecto
	 */
	public GridTransparency(){
		super();
		activeTransparency();
	};
	
	/**
	 * Constructor. Crea un objeto con las propiedades de la transparencia de fichero
	 * pasado como parámetro.
	 * @param fileStatus Transparencia.
	 */
	public GridTransparency(Transparency transp) {
		super(transp);
		activeTransparency();
	}
	
	/**
	 * Comprueba si hay criterios para la aplicación de transparencia y la activa si es
	 * así sino se desactiva. Los criterios son:
	 * <P>
	 * Que haya una mascara de transparencia.
	 * </P><P>
	 * Que exista un buffer de datos para la aplicación de nodata
	 * </P><P>
	 * Que hayan reglas sobre la aplicación de transparencia.
	 * </P><P>
	 * Que la opacidad sea distita de 255 (completamente opaco). 
	 * </P>
	 */
	public void activeTransparency() {
		if(	existAlphaBand() || isNoDataActive() ||
			(transparencyRanges.size() > 0) ||
			(opacity != 0xff))
			transparencyActive = true;
		else
			transparencyActive = false;
	}
	
	/**
	 * Aplica las reglas de transparencia a un pixel RGB y devuelve el valor de ese
	 * mismo pixel con la transparencia aplicada. Primero procesará los rangos. Si
	 * el pixel está en alguno directamente lo pone transparente y lo devuelve, sino
	 * comprueba si existe banda de transparencia y si es así la aplica.
	 * @param rgb
	 * @param line
	 * @param col
	 * @return
	 */
	public int processRGB(int r, int g, int b, int line, int col) {
		// Si el valor es noData se pone totalmente transparente y ya no se tiene en
		// cuenta nada más.
		if (originalData != null && isNoDataActive()) {
			if (isNoData(line, col))
				// El alpha no se pone para dejarlo a 0, totalmente transparente
				return (r << 16 | g << 8 | b);
		}

		/**
		 * Para sacar el valor del nuevo alpha, lo que hacemos es convertir a
		 * porcentajes todos los alphas posibles (en porcentajes) y multiplicarlos
		 * entre si. Con esto conseguimos un nuevo porcentaje de alpha entre 0 y 1.
		 * Una vez hecho esto se multiplica por 255 para tenerlo dentro del rango
		 * deseado. Como queremos evitar operaciones de más, podemos quitar una
		 * division de 255 y así nos ahorramos luego multiplicarlo por 255 otra vez.
		 */

		// Quitada la division para optimizar
		//double a = opacity / 255D;
		double a = opacity;

		int alphaRange = processRange(r, g, b);
		if (alphaRange != 255)
			a *= (alphaRange / 255D);

		if (existAlphaBand() && getAlphaBand() != null)
			a *= (getAlphaBand().getElemByte(line, col, 0) & 0xff) / 255D;

		// Quitada la multiplicacion para optimizar
		// a = (int)(a * 255D);

		return ((int) a << 24) | r << 16 | g << 8 | b;
	}
	
	/**
	 * Aplica la transparecia por rangos al pixel pasado por parámetro. El valor
	 * en la posición cero es el alpha por lo tanto será esta posición la que se
	 * modificará.
	 * @param r
	 * @param g
	 * @param b
	 * @return Un valor de 0 a 255. Donde 255 es totalmente opaco
	 */
	private int processRange(int r, int g, int b) {
		for (int i = 0; i < transparencyRanges.size(); i++) {
			TransparencyRange tr = ((TransparencyRange) transparencyRanges.get(i));
			if (tr == null) continue;
			if (tr.isAnd()) {
				if (tr.getRed() == null ||
						tr.getGreen() == null ||
						tr.getBlue() == null ||
						r < tr.getRed()[0]   || r > tr.getRed()[1] ||
						g < tr.getGreen()[0] || g > tr.getGreen()[1] ||
						b < tr.getBlue()[0]  || b > tr.getBlue()[1])
					continue;

				return tr.getAlpha();
			} else {
				if (tr.getRed() != null) {
					if (r >= tr.getRed()[0] && r <= tr.getRed()[1]) {
						return tr.getAlpha();
					}
				}
				if (tr.getGreen() != null) {
					if (g >= tr.getGreen()[0] && g <= tr.getGreen()[1]) {
						return tr.getAlpha();
					}
				}
				if (tr.getBlue() != null) {
					if (b >= tr.getBlue()[0] && b <= tr.getBlue()[1]) {
						return tr.getAlpha();
					}
				}
			}
		}
		return 255;
	}
		
	/**
	 * Obtiene el flag de transparencia activa o desactivada.
	 * @return true la transparencia está activa y false desactiva
	 */
	public boolean isTransparencyActive() {
		return transparencyActive;
	}
	
	/**
	 * Asigna el flag de transparencia activa o desactivada.
	 * @param transparencyActive true activa la transparencia false la desactiva
	 */
	public void setTransparencyActive(boolean transparencyActive) {
		this.transparencyActive = transparencyActive;
	}
			
}