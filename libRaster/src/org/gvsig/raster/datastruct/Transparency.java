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

import java.util.ArrayList;

import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
/**
 * <p>
 * Esta clase contiene información de transparencia de un objeto. Los objetos
 * pueden ser dataset, grid u otros. Cuando un usuario quiere añadir nueva
 * información de transparencia crea un objeto de este tipo que debe ser
 * mezclado (merge) con otros objetos de este tipo que existan para la misma
 * fuente de datos.
 * </p>
 * <p>
 * Un multirasterdatset obtiene los distintos objetos transparency de todos los
 * ficheros que los componen. Para realizar un solo objeto transparency se hará
 * un merge de todos ellos.
 * </p>
 * <p>
 * Finalmente y antes de renderizar se necesita un objeto GridTransparency. Este
 * estará compuesto con toda la información de transparencia aplicar, es decir,
 * todos los objetos Transparency mezclados. GridTransparency con tiene el
 * método de procesado de un pixel. Se le proporciona un pixel y devuelve este
 * mismo pixel con la transparencia aplicada.
 * </p>
 * <p>
 * Una transparencia que se aplica a un buffer puede tener cuatro procedencias distintas:
 * <UL>
 * <LI>Mascara: Un buffer de NxN aplicado sobre la zona a renderizar como una máscara de 
 * transparencia. Las bandas alpha de las imagenes se comportan de esta forma.</LI>
 * <LI>Opacidad global: Un valor de opacidad se aplicado a cada pixel a renderizar. Este es
 * igual para todos los píxeles.</LI>
 * <LI>Rangos: Una lista de rangos de valores RGB. Cada pixel cuyo valor esté dentro de alguno
 * de los rangos se aplica como transparente.</LI>
 * <LI>Dato: Todos los valores del buffer que coincidan con ese dato son puestos como transparente.
 * Para que esto sea posible tenemos que disponer del buffer de datos originales sin ningún proceso.</LI>
 * </UL>
 * </p>
 *
 * @version 07/06/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Transparency {
	public static int     MAX_OPACITY        = 255;
	protected int         alphaBandNumber    = -1;
	/**
	 * Buffer con la banda alpha correspondiente a la zona a renderizar
	 */
	private IBuffer       mask               = null;
	/**
	 * Buffer con los datos originales (sin filtrar) correspondiente a la zona a renderizar.
	 * Esto es util para aplicar el valor NoData ya que hay que consultar el valor original del
	 * dato. Después de hacer un process es recomendable hacer free para poner a null los buffers.
	 */
	protected IBuffer     originalData       = null;
	/**
	 * Valor de dato transparente. Todos los píxeles de originalData que correspondan con este
	 * valor se pondrán 100% transparentes. 
	 */
	protected double      noData             = RasterLibrary.defaultNoDataValue;
	/**
	 * Flag que indica que el uso de noData para transparencia está activo
	 */
	protected boolean     noDataActive       = false;
	/**
	 * Rangos de transparencia aplicados. Lista de TransparencyRange
	 */
	protected ArrayList   transparencyRanges = new ArrayList();
	/**
	 * Grado de opacidad de todo el raster
	 */
	protected int         opacity            = 0xff;
	
	/**
	 * Array de listeners que serán informados cuando cambia la propiedad de transparencia
	 */
	private ArrayList     transparencyPropertyListener = new ArrayList();
	
	/**
	 * Constructor
	 */
	public Transparency() {
	}

	/**
	 * Constructor de copia
	 */
	public Transparency(Transparency t) {
		//TODO: FUNCIONALIDAD: Falta asignar lo necesario para la transparencia por selección
		this.transparencyRanges = (ArrayList) t.getTransparencyRange().clone();
		this.mask = t.getAlphaBand();
		this.opacity = t.getOpacity();
		this.alphaBandNumber = t.alphaBandNumber;
		this.noData = t.getNoData();
		this.noDataActive = t.isNoDataActive();
		this.originalData = t.originalData;
	}
	
	/**
	 * Asigna un listener a la lista que será informado cuando cambie una
	 * propiedad visual en la renderización. 
	 * @param listener VisualPropertyListener
	 */
	public void addPropertyListener(PropertyListener listener) {
		transparencyPropertyListener.add(listener);
	}
	
	/**
	 * Método llamado cuando hay un cambio en una propiedad de transparencia
	 */
	private void callPropertyChanged(Object obj) {
		for (int i = 0; i < transparencyPropertyListener.size(); i++) {
			PropertyEvent ev = new PropertyEvent(this, "transparency", null, null);
			((PropertyListener)transparencyPropertyListener.get(i)).actionValueChanged(ev);
		}
	}

	/**
	 * Obtiene la máscara asociada
	 * @return Máscara de transparencia
	 */
	public IBuffer getAlphaBand() {
		return mask;
	}

	/**
	 * Asigna el buffer con la máscara
	 * @param b
	 */
	public void setAlphaBand(IBuffer b) {
		if(b == null && mask != null) {
			mask = b;
			callPropertyChanged(this);
			return;
		} else {		
			mask = b;
			if(b != null)
				callPropertyChanged(this);
		}
	}
	
	/**
	 * Obtiene la información de si existe o no banda de transparencia cuando este
	 * objeto va asociado a un dataset. Si tiene este tipo de banda en cada
	 * dibujado se cargará la información de máscara de transparencia en el la
	 * variable mask.
	 * @return true si existe banda de transparencia y false si no lo es.
	 */
	public boolean existAlphaBand() {
		return (mask != null);
	}

	
	/**
	 * Obtiene el área de datos
	 * @return Máscara de transparencia
	 */
	public IBuffer getDataBuffer() {
		return originalData;
	}

	/**
	 * Asigna el área de datos
	 * @param b
	 */
	public void setDataBuffer(IBuffer b) {
		originalData = b;
	}

	/**
	 * Obtiene la información de si existe o no la posibilidad de aplicar valore no data
	 * como transparentes. Para ello tiene que estar activo su uso y el buffer debe contener datos
	 * @return true si puede aplicarse noData y false si no se puede
	 */
	public boolean isNoDataActive() {
		return noDataActive;
	}
	
	/**
	 * Obtiene el valor noData 
	 * @return
	 */
	public double getNoData() {
		return noData;
	}
	
	/**
	 * Asigna el valor noData
	 * @param noData
	 */
	public void setNoData(double noData) {
		this.noDataActive = true;
		if(this.noData != noData)
			callPropertyChanged(this);
		this.noData = noData;
	}
	
	/**
	 * Activa o desactiva el uso de noData como transparencia
	 * @param active
	 */
	public void activeNoData(boolean active) {
		this.noDataActive = active;
	}

	/**
	 * Obtiene los rangos de pixels que son transparentes en el raster.
	 * @return Rangos de transparencias a aplicar
	 */
	public ArrayList getTransparencyRange() {
		return transparencyRanges;
	}

	/**
	 * Asigna un rango de pixels que son transparentes en el raster.
	 * @param range
	 */
	public void setTransparencyRange(TransparencyRange range) {
		this.transparencyRanges.add(range);
		callPropertyChanged(this);
	}

	/**
	 * Asigna la lista de rangos de transparencia
	 * @param ranges
	 */
	public void setTransparencyRangeList(ArrayList ranges) {
		this.transparencyRanges = ranges;
		callPropertyChanged(this);
	}

	/**
	 * Inicializa la lista de rangos de transparencia.
	 */
	public void clearListOfTransparencyRange() {
		transparencyRanges.clear();
		callPropertyChanged(this);
	}

	/**
	 * Obtiene el grado de opacidad de todo el raster
	 * @return valor del grado de opacidad.
	 */
	public int getOpacity() {
		return opacity;
	}

	/**
	 * Asigna el grado de opacidad de todo el raster
	 * @param opacity valor del grado de opacidad.
	 */
	public void setOpacity(int opacity) {
		if(opacity != this.opacity)
			callPropertyChanged(this);
		this.opacity = opacity;
	}

	/**
	 * Asigna la transparencia a partir de un objeto con los metadatos del raster.
	 * @param metadata
	 */
	public void setTransparencyByPixelFromMetadata(DatasetMetadata metadata){
		if (metadata != null) {
			TransparencyRange[] noData = metadata.parserNodataInMetadata();
			if (noData != null) {
				for (int i = 0; i < noData.length; i++)
					getTransparencyRange().add(noData[i]);
			}
			TransparencyRange noDataValue = metadata.parserNodataByBand();
			if (noData == null && noDataValue != null)
				getTransparencyRange().add(noDataValue);
		}
	}

	/**
	 * Mezcla el alpha actual con el que nos pasan por parametro y se asigna
	 * directamente a destino
	 * @param buffer
	 * @param dst
	 */
	public void mergeBuffer(IBuffer buffer, IBuffer dst) {
		for (int y = 0; y < mask.getHeight(); y++) {
			for (int x = 0; x < mask.getWidth(); x++) {
				// ((a / 255) * (b / 255)) * 255
				// Es lo mismo que:
				// (a * b) / 255
				dst.setElem(y, x, 0,
					(byte) (((mask.getElemByte(y, x, 0) & 0xff) * (buffer.getElemByte(y, x, 0) & 0xff)) / 255D));
			}
		}
	}

	/**
	 * Mezcla dos buffers de transparencia en uno solo. 
	 * @param dst Buffer destino de la mezcla
	 * @param buf Buffer a mezclar sobre el destino
	 * @return Buffer destino
	 */
	public static IBuffer merge(IBuffer dst, IBuffer buf) {
		if(buf == null && dst != null)
			return dst;
		if(dst == null && buf != null)
			return buf;
		if(buf != null && dst != null) {
			if(dst.getWidth() != buf.getWidth() || dst.getHeight() != buf.getHeight())
				return null;
			for (int y = 0; y < buf.getHeight(); y++) {
				for (int x = 0; x < buf.getWidth(); x++) {
					dst.setElem(y, x, 0,
							(byte) (((dst.getElemByte(y, x, 0) & 0xff) * (buf.getElemByte(y, x, 0) & 0xff)) / 255D));
				}
			}
			return dst;
		}
		return null;
	}
	
	/**
	 * Mezcla un objeto Transparency con el actual
	 * @param ts objeto TransparencyStatus
	 */
	public Transparency merge(Transparency transp) {
		Transparency t = new Transparency();
		// Mezclamos la opacidad
		double op1 = (double) opacity / (double) MAX_OPACITY;
		double op2 = (double) transp.getOpacity() / (double) MAX_OPACITY;
		t.setOpacity((int) (op1 * op2 * MAX_OPACITY));

		// Mezclamos los rangos de transparencia
		ArrayList tr = transp.getTransparencyRange();
		for (int i = 0; i < tr.size(); i++)
			transparencyRanges.add(tr.get(i));

		// TODO: FUNCIONALIDAD Mezclamos la máscara
		if (mask != null && transp.getAlphaBand() != null) {
			IBuffer newMask = RasterBuffer.getBuffer(IBuffer.TYPE_BYTE, mask.getWidth(), mask.getHeight(), 1, true);
			// Mezclamos alphaBand con el que nos pasan en transp y lo asignamos al nuevo buffer
			mergeBuffer(transp.getAlphaBand(), newMask);

			t.setAlphaBand(newMask);
		} else {
			if (mask != null) {
				t.setAlphaBand(mask);
				t.alphaBandNumber = alphaBandNumber;
			} else {
				t.setAlphaBand(transp.getAlphaBand());
				t.alphaBandNumber = transp.alphaBandNumber;
			}
		}

		// TODO: FUNCIONALIDAD Mezclamos las áreas

		// TODO: FUNCIONALIDAD Mezclamos las mascaras
		return t;
	}

	/**
	 * Obtiene la banda de transpareci si existe o -1 si no existe.
	 * @return número de banda de transparencia o -1 si no existe.
	 */
	public int getAlphaBandNumber() {
		return alphaBandNumber;
	}

	/**
	 * Asigna la información de si existe o no banda de transparencia cuando este
	 * objeto va asociado a un dataset. Si tiene este tipo de banda en cada
	 * dibujado se cargará la información de máscara de transparencia en el la
	 * variable mask.
	 * @param true si existe banda de transparencia y false si no lo es.
	 */
	public void setTransparencyBand(int alphaBandNumber) {
		this.alphaBandNumber = alphaBandNumber;
	}
	
	/**
	 * Consulta si el valor de la posición (line, col) del buffer es considerado
	 * NoData o no.
	 * @param line Linea del buffer
	 * @param col Columna del buffer
	 * @return
	 */
	protected boolean isNoData(int line, int col) {
		switch (originalData.getDataType()) {
		case IBuffer.TYPE_BYTE:
			if(((double)originalData.getElemByte(line, col, 0)) == noData)
				return true;
			break;
		case IBuffer.TYPE_SHORT:
			if(((double)originalData.getElemShort(line, col, 0)) == noData)
				return true;
			break;
		case IBuffer.TYPE_INT:
			if(((double)originalData.getElemInt(line, col, 0)) == noData)
				return true;
			break;
		case IBuffer.TYPE_FLOAT:
			if(((double)originalData.getElemFloat(line, col, 0)) == noData)
				return true;
			break;
		case IBuffer.TYPE_DOUBLE:
			if(((double)originalData.getElemDouble(line, col, 0)) == noData)
				return true;
			break;
		}
		return false;
	}
	
	/**
	 * Pone a null los buffers de datos y pasa el garbage collector para liberar la memoria.
	 * Después de renderizar es conveniente hacer esto porque el objeto Transparency asociado a
	 * un dataset no se destruye hasta que no se cierra este. Esto hace que esta memoria este
	 * siempre ocupada. 
	 */
	public void free() {
		mask = null;
		originalData = null;
		System.gc();
	}
}