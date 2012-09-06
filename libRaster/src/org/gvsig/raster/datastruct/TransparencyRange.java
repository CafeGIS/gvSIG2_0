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
package org.gvsig.raster.datastruct;

import java.io.IOException;
import java.util.Arrays;

import org.gvsig.raster.dataset.RasterDataset;
/**
 * Clase que representa a un conjunto de pixeles con transparencia. Incluye los
 * rangos de transparencia para cada banda, la cadena que va en la lista y la
 * operación que se realiza entre ellos. Un And significa que serán
 * transparentes todos los pixeles que cumplan con R con G y con B. Un Or
 * significará que tendrán transparencia todos los pixeles que cumplan con R con
 * G o con B.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TransparencyRange implements Cloneable {
	private String  strEntry = null;
	private int[]   red      = null;
	private int[]   green    = null;
	private int[]   blue     = null;
	private boolean and      = true;
	private int     alpha    = 0;

	/**
	 * Constructor
	 */
	public TransparencyRange() {
	}

	/**
	 * Crea un objeto TransparencyRange a partir de una cadena bien formada
	 * @param value cadena bien formada que representa un intervalo
	 */
	public TransparencyRange(String value) {
		try {
			red = new int[2];
			green = new int[2];
			blue = new int[2];
			and = TransparencyRange.stringToInterval(value, red, green, blue);
			strEntry = value;
		} catch (IOException e) {
			// Cadena mal formada
		}
	}

	
	/**
	 * Obtiene la operación  utilizada
	 * @param and Si es true significa que se usa un AND y false implica
	 * que se usa un OR
	 */
	public boolean isAnd() {
		return and;
	}

	/**
	 * Asigna la operación AND como la utilizada
	 * @param and booleano que si está a true significa que el la operación
	 * AND es la utilizada como operación.
	 */
	public void setAnd(boolean and) {
		this.and = and;
	}

	/**
	 * Obtiene el intervalo de valores correspondiente a la banda del azul
	 * @return Array bidimensional de enteros correspondiente a la banda del azul
	 */
	public int[] getBlue() {
		return blue;
	}

	/**
	 * Asigna los intervalos de valores correspondientes a las bandas del
	 * rojo, azul y verde
	 * @param red Array bidimensional de enteros correspondiente a la banda del rojo
	 * @param green Array bidimensional de enteros correspondiente a la banda del verde
	 * @param blue Array bidimensional de enteros correspondiente a la banda del azul
	 */
	public void setRGB(int[] red, int[] green, int[] blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	/**
	 * Asigna el intervalo de valores correspondiente a la banda del azul
	 * @param blue Array bidimensional de enteros correspondiente a la banda del azul
	 */
	public void setBlue(int[] blue) {
		this.blue = blue;
	}

	/**
	 * Obtiene el intervalo de valores correspondiente a la banda del verde
	 * @return Array bidimensional de enteros correspondiente a la banda del verde
	 */
	public int[] getGreen() {
		return green;
	}

	/**
	 * Asigna el intervalo de valores correspondiente a la banda del verde
	 * @param green Array bidimensional de enteros correspondiente a la banda del verde
	 */
	public void setGreen(int[] green) {
		this.green = green;
	}

	/**
	 * Obtiene el intervalo de valores correspondiente a la banda del rojo
	 * @return Array bidimensional de enteros correspondiente a la banda del rojo
	 */
	public int[] getRed() {
		return red;
	}

	/**
	 * Asigna el intervalo de valores correspondiente a la banda del rojo
	 * @param red Array bidimensional de enteros correspondiente a la banda del rojo
	 */
	public void setRed(int[] red) {
		this.red = red;
	}

	/**
	 * Obtiene la cadena que representa una entrada en la tabla.
	 * @return Cadena que representa una entrada en la tabla
	 */
	public String getStrEntry() {
		return strEntry;
	}

	/**
	 * Asigna la cadena que representa una entrada en la tabla.
	 * @param strEntry Cadena que representa una entrada en la tabla.
	 */
	public void setStrEntry(String strEntry) {
		this.strEntry = strEntry;
	}


	/**
	 * Esta función valida la cadena de entrada por medio de una máquina de estados. Valida las
	 * siguientes posibilidades en la cadena de entrada:
	 * <LI>
	 * <UL>(valor_rojo) & (Valor_verde) & (Valor_azul)</UL>
	 * <UL>(valor_rojo) | (Valor_verde) | (Valor_azul)</UL>
	 * </LI>
	 * Despues de la validación parsea el contenido y carga los parámetros r,g,b con los
	 * intervalos de valores. Estos parámetros deben ser pasados como arrays de enteros de
	 * dos elementos.
	 * @param values
	 * @param r	Intervalo de rojo
	 * @param g Intervalo de verde
	 * @param b Intervalo de azul
	 * @return Devuelve true si la operación usada en los intervalos es un AND y false si es un OR
	 */
	public static boolean stringToInterval(String values, int[] r, int[] g, int[] b) throws IOException {
		int status = 0;
		int countAnd = 0, countOr = 0;
		boolean and = true;
		for (int i = 0; i < values.length(); i++) {
			char c = values.charAt(i);
			switch (status) {
				case 0:
					if (c == ' ')
						status = 0;
					else if ((c >= 48 && c <= 57) || c == '*')
						status = 1;
					else
						status = 4;
					break;
				case 1:
					if ((c >= 48 && c <= 57) || (c == ' '))
						status = 1;
					else if (c == ':')
						status = 2;
					else if (c == '&') {
						status = 0;
						countAnd++;
					} else if (c == '|') {
						status = 0;
						countOr++;
					} else
						status = 4;
					break;
				case 2:
					if (c >= 48 && c <= 57)
						status = 3;
					else
						status = 4;
					break;
				case 3:
					if ((c >= 48 && c <= 57) || (c == ' '))
						status = 3;
					else if (c == '&') {
						status = 0;
						countAnd++;
					} else if (c == '|') {
						status = 0;
						countOr++;
					} else
						status = 4;
					break;
				case 4:
					throw new IOException("Error en la cadena de entrada " + status);
			}
		}

		// Si el analisis es correcto parseamos
		if ((status == 1 || status == 3) && ((countAnd == 2 && countOr == 0) || (countAnd == 0 && countOr == 2))) {
			String[] s = values.split(" & ");
			if (s.length < 3) {
				s = values.split(" \\| ");
				and = false;
			}
			;
			String[] val = s[0].split(":");
			try {
				r[0] = Integer.parseInt(val[0]);
				if (val.length == 2)
					r[1] = Integer.parseInt(val[1]);
				else
					r[1] = Integer.parseInt(val[0]);
			} catch (NumberFormatException e) {
				r[0] = -1;
				r[1] = -1;
			}

			val = s[1].split(":");
			try {
				g[0] = Integer.parseInt(val[0]);
				if (val.length == 2)
					g[1] = Integer.parseInt(val[1]);
				else
					g[1] = Integer.parseInt(val[0]);
			} catch (NumberFormatException e) {
				g[0] = -1;
				g[1] = -1;
			}

			val = s[2].split(":");
			try {
				b[0] = Integer.parseInt(val[0]);
				if (val.length == 2)
					b[1] = Integer.parseInt(val[1]);
				else
					b[1] = Integer.parseInt(val[0]);
			} catch (NumberFormatException e) {
				b[0] = -1;
				b[1] = -1;
			}
		} else
			throw new IOException("Error en la cadena de entrada ");

		return and;
	}

	/**
	 * Carga la cadena StrEntry leyendo los valores en los vectores que representa los intervalos.
	 */
	public void loadStrEntryFromValues(){
		String separator = " | ";
		if (and)
			separator = " & ";
		strEntry = String.valueOf(red[0] + separator+ green[0] + separator + blue[0]);
	}

	/**
	 * Obtiene el alpha asociado al rango. Por defecto es de 0, es decir
	 * totalmente transparente pero puede asociarsele un valor distinto
	 * @return Alpha asociado
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * Asigna el alpha asociado al rango. Por defecto es de 0, es decir
	 * totalmente transparente pero puede asociarsele un valor distinto
	 * @param alpha asociado
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	/**
	 * Realiza la unión entre el intervalo actual y el pasado por parámetro
	 * @param interval intervalo pasado
	 * @param rgb
	 * @return union de intervalos
	 */
	public int[] union(int[] interval, int rgb) {
		switch (rgb) {
			case RasterDataset.RED_BAND:
				if ((red[1] >= interval[0] && red[0] <= interval[0]) || (red[0] <= interval[1] && red[0] >= interval[0]))
					return new int[] { Math.min(red[0], interval[0]), Math.max(red[1], interval[1]) };
			case RasterDataset.GREEN_BAND:
				if ((green[1] >= interval[0] && green[0] <= interval[0]) || (green[0] <= interval[1] && green[0] >= interval[0]))
					return new int[] { Math.min(green[0], interval[0]), Math.max(green[1], interval[1]) };
			case RasterDataset.BLUE_BAND:
				if ((blue[1] >= interval[0] && blue[0] <= interval[0]) || (blue[0] <= interval[1] && blue[0] >= interval[0]))
					return new int[] { Math.min(blue[0], interval[0]), Math.max(blue[1], interval[1]) };
		}
		return null;
	}

	/**
	 * Muestra los datos del objeto para depuración.
	 */
	public void show(){
		if (getRed() != null)
			System.out.println(getRed()[0] + " " + getRed()[1]);
		if (getGreen() != null)
			System.out.println(getGreen()[0] + " " + getGreen()[1]);
		if (getBlue() != null)
			System.out.println(getBlue()[0] + " " + getBlue()[1]);
		System.out.println(isAnd());
		System.out.println(getStrEntry());
		System.out.println("--------------------");
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		TransparencyRange clone = null;
		try {
			clone = (TransparencyRange) super.clone();
		} catch (CloneNotSupportedException e) {
		}

		if (strEntry != null)
			clone.strEntry = new String(strEntry);

		if (red != null)
			clone.red = (int[]) red.clone();

		if (green != null)
			clone.green = (int[]) green.clone();

		if (blue != null)
			clone.blue = (int[]) blue.clone();

		return clone;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + alpha;
		result = PRIME * result + (and ? 1231 : 1237);
		result = PRIME * result + TransparencyRange.hashCode(blue);
		result = PRIME * result + TransparencyRange.hashCode(green);
		result = PRIME * result + TransparencyRange.hashCode(red);
		result = PRIME * result + ((strEntry == null) ? 0 : strEntry.hashCode());
		return result;
	}
	
	private static int hashCode(int[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + array[index];
		}
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TransparencyRange other = (TransparencyRange) obj;
		if (alpha != other.alpha)
			return false;
		if (and != other.and)
			return false;
		if (!Arrays.equals(blue, other.blue))
			return false;
		if (!Arrays.equals(green, other.green))
			return false;
		if (!Arrays.equals(red, other.red))
			return false;
		if (strEntry == null) {
			if (other.strEntry != null)
				return false;
		} else if (!strEntry.equals(other.strEntry))
			return false;
		return true;
	}
}