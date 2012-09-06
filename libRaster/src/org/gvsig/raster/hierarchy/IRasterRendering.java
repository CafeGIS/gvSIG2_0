package org.gvsig.raster.hierarchy;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.render.Rendering;

/**
 * Interfaz de operaciones aplicadas sobre un raster renderizable. Las propiedades
 * ofrecidas son las aplicadas en la renderización. No quiere decir que sean las 
 * del propio raster ya que estas han podido ser modificadas.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public interface IRasterRendering {
	/**
	 * Obtiene la transparencia aplicada sobre la renderización
	 * @return Entero que representa la transparencia aplicada en la renderización. 
	 */
	public GridTransparency getRenderTransparency();
	/**
	 * Obtiene el objeto renderizador
	 * @return objeto que renderiza o null si no existe.
	 */
	public Rendering getRender();
	
	/**
	 * Obtiene las lista de filtros aplicados en la renderización
	 * @return RasterFilterList
	 */
	public RasterFilterList getRenderFilterList();
	
	/**
	 * Asigna la lista de filtros aplicados en la renderización
	 * @param filterList
	 */
	public void setRenderFilterList(RasterFilterList filterList);
	
	/**
	 * Obtiene la lista de bandas asignadas en el renderizado.
	 * @return Lista de bandas
	 */
	public int[] getRenderBands();
	
	/**
	 * Asigna la lista de bandas usadas en el renderizado
	 * @param renderBands
	 */
	public void setRenderBands(int[] renderBands);
	
	/**
	 * Obtiene el buffer aplicado en la última renderización 
	 * @return IBuffer buffer aplicado o null si no se ha aplicado ninguno
	 */
	public IBuffer getLastRenderBuffer();
	
	/**
	 * Informa de si el raster tiene tabla de color asociada o no.
	 * @return true si tiene tabla de color y false si no la tiene.
	 */
	public boolean existColorTable();
	
	/**
	 * Método que informa de si existe una banda de transparencia en el raster.
	 * @return true si existe la banda de transparencia y false si no existe
	 */
	public boolean existsAlphaBand();
	
	/**
	 * Obtiene el número de banda de transparencia o -1 si no tiene
	 * @return Posición de la banda de transparencia o -1 si no tiene
	 */
	public int getAlphaBandNumber();
	
}