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
package org.gvsig.raster.grid.render;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetColorInterpretation;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.datastruct.Transparency;
import org.gvsig.raster.datastruct.ViewPortData;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.grid.filter.FilterListChangeEvent;
import org.gvsig.raster.grid.filter.FilterListChangeListener;
import org.gvsig.raster.grid.filter.RasterFilterList;
import org.gvsig.raster.grid.filter.bands.ColorTableFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterUtilities;
/**
 * Esta clase se encarga de la gestión del dibujado de datos leídos desde la capa
 * "dataaccess" sobre objetos java. Para ello necesita una fuente de datos que tipicamente
 * es un buffer (RasterBuffer) y un objeto que realice la función de escritura de datos a
 * partir de un estado inicial.
 * Esta capa del renderizado gestiona Extents, rotaciones, tamaños de vista pero la escritura
 * de datos desde el buffer al objeto image es llevada a cabo por ImageDrawer.
 *
 * Parámetros de control de la visualización:
 * <UL>
 * <LI>renderBands: Orden de visualizado de las bands.</LI>
 * <LI>replicateBands: Para visualización de raster de una banda. Dice si se replica sobre las otras dos bandas
 * de visualización o se ponen a 0.</LI>
 * <LI>enhanced: aplicación de filtro de realce</LI>
 * <LI>removeEnds: Eliminar extremos en el filtro de realce. Uso del segundo máximo y mínimo</LI>
 * <LI>tailTrim: Aplicacion de recorte de colas en el realce. Es un valor decimal que representa el porcentaje del recorte entre 100.
 * Es decir, 0.1 significa que el recorte es de un 10%</LI>
 * </UL>
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class Rendering implements PropertyListener, FilterListChangeListener {

	/**
	 * Grid para la gestión del buffer
	 */
	private Grid             grid                     = null;
	/**
	 * Fuente de datos para el renderizado
	 */
	private BufferFactory    bufferFactory            = null;
	/**
	 * Objeto sobre el cual se hace el renderizado
	 */
	private Image            geoImage                 = null;
	/**
	 * Número de bandas a renderizar y en el orden que se hará. Esto es asignado
	 * por el usuario de la renderización.
	 */
	private int[]            renderBands              = { 0, 1, 2 };
	/**
	 * Tiene el comportamiento cuando se tiene un raster con una. Dice si en las
	 * otras bandas a renderizar se replica la banda existente o se ponen a 0.
	 */
	private boolean          replicateBand            = false;

	private ImageDrawer      drawer                   = null;
	/**
	 * Ultima transparencia aplicada en la visualización que es obtenida desde el
	 * grid
	 */
	private GridTransparency lastTransparency         = null;
	/**
	 * Lista de filtros aplicada en la renderización
	 */
	private RasterFilterList filterList               = null;

	private IBuffer          lastRenderBuffer         = null;

	/**
	 * Ancho y alto del objeto Image en una petición de dibujado a un raster
	 * raster
	 */
	private double           widthImage, heightImage;

	private Point2D          ulPxRequest, lrPxRequest;

	/**
	 * Array de listeners que serán informados cuando cambia una propiedad en la visualización
	 */
	private ArrayList        visualPropertyListener   = new ArrayList();

	/**
	 * Constructor
	 */
	public Rendering() {
		init();
	}

	/**
	 * Constructor
	 * @param grid
	 */
	public Rendering(Grid grid) {
		this.grid = grid;
		init();
	}

	/**
	 * Constructor
	 * @param grid
	 */
	public Rendering(BufferFactory ds) {
		this.bufferFactory = ds;
		init();
	}

	private void init() {
		drawer = new ImageDrawer(this);

		if (bufferFactory == null) {
			setRenderBands(new int[] { 0, 1, 2 });
			return;
		}

		//Bandas que se dibujan por defecto si la interpretación de color no tiene valores
		switch (bufferFactory.getDataSource().getBandCount()) {
			case 1:
				setRenderBands(new int[] { 0, 0, 0 });
				break;
			case 2:
				setRenderBands(new int[] { 0, 1, 1 });
				break;
			default:
				setRenderBands(new int[] { 0, 1, 2 });
				break;
		}

		//---------------------------------------------------
		//INICIALIZACIÓN DE LA INTERPRETACIÓN DE COLOR

		//Inicialización de la asignación de bandas en el renderizado
		//Leemos el objeto metadata para obtener la interpretación de color asociada al raster
		if (bufferFactory.getDataSource().getDatasetCount() == 1) {
			DatasetColorInterpretation colorInterpr = bufferFactory.getDataSource().getColorInterpretation();
			if (colorInterpr != null)
				if (colorInterpr.getBand(DatasetColorInterpretation.PAL_BAND) == -1) {
					if (colorInterpr.isUndefined())
						return;
					int[] result = new int[] { -1, -1, -1 };
					int gray = colorInterpr.getBand(DatasetColorInterpretation.GRAY_BAND);
					if (gray != -1)
						result[0] = result[1] = result[2] = gray;
					else {
						int r = colorInterpr.getBand(DatasetColorInterpretation.RED_BAND);
						if (r != -1)
							result[0] = r;
						int g = colorInterpr.getBand(DatasetColorInterpretation.GREEN_BAND);
						if (g != -1)
							result[1] = g;
						int b = colorInterpr.getBand(DatasetColorInterpretation.BLUE_BAND);
						if (b != -1)
							result[2] = b;
					}
					setRenderBands(result);
				}
		}
	}

	/**
	 * Asigna un listener a la lista que será informado cuando cambie una
	 * propiedad visual en la renderización.
	 * @param listener VisualPropertyListener
	 */
	public void addVisualPropertyListener(VisualPropertyListener listener) {
		visualPropertyListener.add(listener);
	}

	/**
	 * Método llamado cuando hay un cambio en una propiedad de visualización
	 */
	private void callVisualPropertyChanged(Object obj) {
		for (int i = 0; i < visualPropertyListener.size(); i++) {
			VisualPropertyEvent ev = new VisualPropertyEvent(obj);
			((VisualPropertyListener)visualPropertyListener.get(i)).visualPropertyValueChanged(ev);
		}
	}

	/**
	 * Dibuja el raster sobre el Graphics. Para ello debemos de pasar el viewPort que corresponde a la
	 * vista. Este viewPort es ajustado a los tamaños máximos y mínimos de la imagen por la función
	 * calculateNewView. Esta función también asignará la vista a los drivers. Posteriormente se calcula
	 * el alto y ancho de la imagen a dibujar (wImg, hImg), así como el punto donde se va a pintar dentro
	 * del graphics (pt). Finalmente se llama a updateImage del driver para que pinte y una vez dibujado
	 * se pasa a través de la función renderizeRaster que es la encargada de aplicar la pila de filtros
	 * sobre el Image que ha devuelto el driver.
	 *
	 * Para calcular en que coordenada pixel (pt) se empezará a pintar el BufferedImage con el raster leído
	 * se aplica sobre la esquina superior izquierda de esta la matriz de transformación del ViewPortData
	 * pasado vp.mat.transform(pt, pt). Si el raster no está rotado este punto es el resultante de la
	 * función calculateNewView que devuelve la petición ajustada al extent de la imagen (sin rotar). Si
	 * el raster está rotado necesitaremos para la transformación el resultado de la función coordULRotateRaster.
	 * Lo que hace esta última es colocar la petición que ha sido puesta en coordenadas de la imagen sin rotar
	 * (para pedir al driver de forma correcta) otra vez en coordenadas de la imagen rotada (para calcular su
	 * posición de dibujado).
	 *
	 * Para dibujar sobre el Graphics2D el raster rotado aplicaremos la matriz de transformación con los
	 * parámetros de Shear sobre este Graphics de forma inversa. Como hemos movido el fondo tendremos que
	 * recalcular ahora el punto donde se comienza a dibujar aplicandole la transformación sobre este
	 * at.inverseTransform(pt, pt);. Finalmente volcamos el BufferedImage sobre el Graphics volviendo a dejar
	 * el Graphics en su posición original al acabar.
	 *
	 * @param g Graphics sobre el que se pinta
	 * @param vp ViewPort de la extensión a dibujar
	 * @throws InvalidSetViewException
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public synchronized Image draw(Graphics2D g, ViewPortData vp)
		throws RasterDriverException, InvalidSetViewException, InterruptedException {
		geoImage = null;
		IRasterDataSource dataset = bufferFactory.getDataSource();
		AffineTransform transf = dataset.getAffineTransform(0);

		if(RasterUtilities.isOutside(vp.getExtent(), dataset.getExtent()))
			return null;

		Extent adjustedRotedRequest = request(vp, dataset);

		if ((widthImage <= 0) || (heightImage <= 0))
			return null;

		double[] step = null;

		if (bufferFactory != null) {
			if (lastTransparency == null) {
				lastTransparency = new GridTransparency(bufferFactory.getDataSource().getTransparencyFilesStatus());
				lastTransparency.addPropertyListener(this);
			}
			// Asignamos la banda de transparencia si existe esta
			if (bufferFactory.getDataSource().getTransparencyFilesStatus().getAlphaBandNumber() != -1) {
				bufferFactory.setSupersamplingLoadingBuffer(false); // Desactivamos el supersampleo en la carga del buffer.
				bufferFactory.setDrawableBands(new int[] { lastTransparency.getAlphaBandNumber(), -1, -1 });
				bufferFactory.setAreaOfInterest(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY(), adjustedRotedRequest.getLRX(), adjustedRotedRequest.getLRY(), (int)Math.round(widthImage), (int)Math.round(heightImage));
				bufferFactory.setSupersamplingLoadingBuffer(true);
				lastTransparency.setAlphaBand(bufferFactory.getRasterBuf());
			}
			bufferFactory.setSupersamplingLoadingBuffer(false); // Desactivamos el supersampleo en la carga del buffer.
			// En el renderizado será ImageDrawer el que se encargue de esta función
			bufferFactory.setDrawableBands(getRenderBands());
			step = bufferFactory.setAreaOfInterest(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY(), adjustedRotedRequest.getLRX(), adjustedRotedRequest.getLRY(), (int)Math.round(widthImage), (int)Math.round(heightImage));
			bufferFactory.setSupersamplingLoadingBuffer(true);

			//Asignamos los datos al objeto transparencia antes de aplicar la pila de filtros para que el valor NoData sea efectivo
			if (bufferFactory.getDataSource().getTransparencyFilesStatus().isNoDataActive())
				lastTransparency.setDataBuffer(bufferFactory.getRasterBuf());
			else
				lastTransparency.setDataBuffer(null);
			lastTransparency.activeTransparency();

		} else
			return null;

		//Aplicamos los filtros
		grid = new Grid(bufferFactory, true);
		filterList.addEnvParam("Transparency", lastTransparency);
		grid.setFilterList(filterList);
		grid.applyFilters();

		//Si la lista de filtros genera bandas de transparencia se mezclan con la actual
		if(grid.getFilterList().getAlphaBand() != null) {
			IBuffer t = grid.getFilterList().getAlphaBand();
			if(lastTransparency.getAlphaBand() != null)
				t = Transparency.merge(t, lastTransparency.getAlphaBand());
			lastTransparency.setAlphaBand(t);
			lastTransparency.activeTransparency();
		}

		//Buffer filtrado para renderizar
		lastRenderBuffer = grid.getRasterBuf();
		drawer.setBuffer(lastRenderBuffer); // Buffer de datos a renderizar
		drawer.setStep(step); // Desplazamiento para supersampleo
		drawer.setBufferSize((int)Math.round(widthImage), (int)Math.round(heightImage)); // Ancho y alto del buffer
		geoImage = drawer.drawBufferOverImageObject(replicateBand, getRenderBands()); // Acción de renderizado

		// Borramos el buffer de transparencia para que siempre se tenga que regenerar.
		lastTransparency.setAlphaBand(null);

		//En el caso de no tenga rotación y el tamaño de pixel sea positivo en X y negativo en Y no aplicamos ninguna
		//transformación. Esto no es necesario hacerlo, sin ello se visualiza igual. Unicamente se hace porque de esta
		//forma el raster resultante mejora un poco en calidad en ciertos niveles de zoom ya que al aplicar transformaciones
		//sobre el Graphics parece que pierde algo de calidad.
		if(transf.getScaleX() > 0 && transf.getScaleY() < 0 && transf.getShearX() == 0 && transf.getShearY() == 0) {
			Point2D lastGraphicOffset = new Point2D.Double(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY());
			vp.mat.transform(lastGraphicOffset, lastGraphicOffset);
			g.drawImage(geoImage, (int) Math.round(lastGraphicOffset.getX()), (int) Math.round(lastGraphicOffset.getY()), null);
			return geoImage;
		}

		/*
		 * Tenemos una matriz con la transformación de la coordenadas de la vista a coordenadas reales vp.mat, además tenemos
		 * la transformación de coordenadas reales a coordenadas pixel (transf). Con ambas podemos obtener una matriz de trasformacion
		 * entre coordenadas de la vista a coordenadas pixel (transf X vp.mat). Así obtenemos la transformación entre coordenadas
		 * de la vista y coordenadas pixel del raster. El problemas es que cada zoom la escala de la petición del raster varia
		 * por lo que habrá que calcular una matriz con la escala (escale). escale X transf X vp.mat
		 */
		double sX = Math.abs(ulPxRequest.getX() - lrPxRequest.getX()) / widthImage;
		double sY = Math.abs(ulPxRequest.getY() - lrPxRequest.getY()) / heightImage;
		AffineTransform escale = new AffineTransform(sX, 0, 0, sY, 0, 0);

		try {
			AffineTransform at = (AffineTransform)escale.clone();
			at.preConcatenate(transf);
			at.preConcatenate(vp.getMat());
			g.transform(at);
			Point2D.Double pt = null;
			//El punto sobre el que rota la imagen depende del signo de los tamaños del pixel
			if(transf.getScaleX() < 0 && transf.getScaleY() < 0)
				pt = new Point2D.Double(adjustedRotedRequest.maxX(), adjustedRotedRequest.maxY());
			else if(transf.getScaleX() > 0 && transf.getScaleY() > 0)
				pt = new Point2D.Double(adjustedRotedRequest.minX(), adjustedRotedRequest.minY());
			else if(transf.getScaleX() < 0 && transf.getScaleY() > 0)
				pt = new Point2D.Double(adjustedRotedRequest.maxX(), adjustedRotedRequest.minY());
			else
				pt = new Point2D.Double(adjustedRotedRequest.getULX(), adjustedRotedRequest.getULY());
			vp.getMat().transform(pt, pt);
			at.inverseTransform(pt, pt);
			g.drawImage(geoImage, (int) Math.round(pt.getX()), (int) Math.round(pt.getY()), null);
			g.transform(at.createInverse());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		return geoImage;
		// long t2 = new Date().getTime();
		// System.out.println("Renderizando Raster: " + ((t2 - t1) / 1000D) + ", secs.");
	}

	/**
	 * Calculamos la petición en coordenadas del mundo real con la transformación del raster. Esto
	 * permite obtener las coordenadas de la petición con la rotación, si la tiene.
	 * @param vp
	 * @param dataset
	 * @return
	 */
	private Extent request(ViewPortData vp, IRasterDataSource dataset) {
		if (dataset.isRotated()) {
			//Obtenemos las cuatro esquinas de la selección que hemos hecho en la vista
			Point2D ul = new Point2D.Double(vp.getExtent().minX(), vp.getExtent().maxY());
			Point2D ur = new Point2D.Double(vp.getExtent().maxX(), vp.getExtent().maxY());
			Point2D ll = new Point2D.Double(vp.getExtent().minX(), vp.getExtent().minY());
			Point2D lr = new Point2D.Double(vp.getExtent().maxX(), vp.getExtent().minY());

			//Las pasamos a coordenadas pixel del raster
			ul = dataset.worldToRaster(ul);
			ur = dataset.worldToRaster(ur);
			ll = dataset.worldToRaster(ll);
			lr = dataset.worldToRaster(lr);

			//Obtenemos los valores pixel máximos y mínimos para X e Y
			double pxMaxX = Math.max(Math.max(ul.getX(), ur.getX()), Math.max(ll.getX(), lr.getX()));
			double pxMaxY = Math.max(Math.max(ul.getY(), ur.getY()), Math.max(ll.getY(), lr.getY()));
			double pxMinX = Math.min(Math.min(ul.getX(), ur.getX()), Math.min(ll.getX(), lr.getX()));
			double pxMinY = Math.min(Math.min(ul.getY(), ur.getY()), Math.min(ll.getY(), lr.getY()));

			//Ajustamos las coordenadas pixel al área máxima del raster
			pxMinX = Math.max(pxMinX, 0);
			pxMinY = Math.max(pxMinY, 0);
			pxMaxX = Math.min(pxMaxX, dataset.getWidth());
			pxMaxY = Math.min(pxMaxY, dataset.getHeight());

			//Petición en coordenadas pixel
			ulPxRequest = new Point2D.Double(pxMinX, pxMinY);
			lrPxRequest = new Point2D.Double(pxMaxX, pxMaxY);

			//Calculamos el ancho y alto del buffer sobre el que se escribe la petición
			widthImage = ((Math.abs(lrPxRequest.getX() - ulPxRequest.getX()) * vp
					.getWidth()) / Math.abs(pxMaxX - pxMinX));
			heightImage = ((Math.abs(lrPxRequest.getY() - ulPxRequest.getY()) * vp
					.getHeight()) / Math.abs(pxMaxY - pxMinY));

			//Convertimos la petición en coordenadas pixel a petición en coordenadas reales.
			Point2D ulWC = dataset.rasterToWorld(ulPxRequest);
			Point2D lrWC = dataset.rasterToWorld(lrPxRequest);

			//Ajustamos la petición a los limites del raster, teniendo en cuenta la rotación de este.
			return new Extent(ulWC, lrWC);
		}
		Extent adjustedRotedExtent = RasterUtilities.calculateAdjustedView(vp.getExtent(), dataset.getAffineTransform(0), new Dimension((int)dataset.getWidth(), (int)dataset.getHeight()));
		widthImage = (int)Math.round(Math.abs(adjustedRotedExtent.width() * vp.getMat().getScaleX()));
		heightImage = (int)Math.round(Math.abs(adjustedRotedExtent.height() * vp.getMat().getScaleY()));
		Point2D ul = new Point2D.Double(adjustedRotedExtent.getULX(), adjustedRotedExtent.getULY());
		Point2D lr = new Point2D.Double(adjustedRotedExtent.getLRX(), adjustedRotedExtent.getLRY());
		ul = dataset.worldToRaster(ul);
		lr = dataset.worldToRaster(lr);
		ulPxRequest = new Point2D.Double(ul.getX(), ul.getY());
		lrPxRequest = new Point2D.Double(lr.getX(), lr.getY());
		return adjustedRotedExtent;
	}

	/**
	 * Obtiene el número de bandas y el orden de renderizado. Cada posición del
	 * vector es una banda del buffer y el contenido de esa posición es la banda
	 * de la imagen que se dibujará sobre ese buffer. A la hora de renderizar hay
	 * que tener en cuenta que solo se renderizan las tres primeras bandas del
	 * buffer por lo que solo se tienen en cuenta los tres primeros elementos. Por
	 * ejemplo, el array {1, 0, 3} dibujará sobre el Graphics las bandas 1,0 y 3
	 * de un raster de al menos 4 bandas. La notación con -1 en alguna posición
	 * del vector solo tiene sentido en la visualización pero no se puede asígnar
	 * una banda del buffer a null. Algunos ejemplos:
	 * <P>
	 * <UL>
	 * <LI> {-1, 0, -1} Dibuja la banda 0 del raster en la G de la visualización. Si
	 * replicateBand es true R = G = B sino R = B = 0 </LI>
	 * <LI> {1, 0, 3} La R = banda 1 del raster, G = 0 y B = 3 </LI>
	 * <LI> {0} La R = banda 0 del raster. Si replicateBand es true R = G = B
	 * sino G = B = 0</LI>
	 * </UL>
	 * </P>
	 *
	 * @return bandas y su posición
	 */
	public int[] getRenderBands() {
		return renderBands;
	}

	/**
		 * Asigna el número de bandas y el orden de renderizado. Cada posición del vector es una banda
	 * del buffer y el contenido de esa posición es la banda de la imagen que se dibujará
	 * sobre ese buffer. A la hora de renderizar hay que tener en cuenta que solo se renderizan las
	 * tres primeras bandas del buffer por lo que solo se tienen en cuenta los tres primeros
	 * elementos. Por ejemplo, el array {1, 0, 3} dibujará sobre el Graphics las bandas 1,0 y 3 de un
	 * raster que tiene al menos 4 bandas. La notación con -1 en alguna posición del vector solo tiene sentido
	 * en la visualización pero no se puede asígnar una banda del buffer a null.
	 * Algunos ejemplos:
	 * <P>
	 * {-1, 0, -1} Dibuja la banda 0 del raster en la G de la visualización.
	 * Si replicateBand es true R = G = B sino R = B = 0
	 * {1, 0, 3} La R = banda 1 del raster, G = 0 y B = 3
	 * {0} La R = banda 0 del raster. Si replicateBand es true R = G = B sino G = B = 0
	 * </P>
	 *
	 *
		 * @param renderBands: bandas y su posición
		 */
	public void setRenderBands(int[] renderBands) {
		if(	renderBands[0] != this.renderBands[0] ||
			renderBands[1] != this.renderBands[1] ||
			renderBands[2] != this.renderBands[2])
			callVisualPropertyChanged(renderBands);
		this.renderBands = renderBands;
		if (filterList != null)
			for (int i = 0; i < filterList.lenght(); i++)
				(filterList.get(i)).addParam("renderBands", renderBands);
	}

	/**
	 * Dado que la notación de bandas para renderizado admite posiciones con -1 y la notación del
	 * buffer no ya que no tendria sentido. Esta función adapta la primera notación a la segunda
	 * para realizar la petición setAreaOfInterest y cargar el buffer.
	 * @param b Array que indica la posición de bandas para el renderizado
	 * @return Array que indica la posición de bandas para la petición
	 */
	public int[] formatArrayRenderBand(int[] b) {
		int cont = 0;
		for(int i = 0; i < b.length; i++)
			if(b[i] >= 0)
				cont ++;
		if(cont <= 0)
			return null;
		int[] out = new int[cont];
		int pos = 0;
		for(int i = 0; i < cont; i++) {
			while(b[pos] == -1)
				pos ++;
			out[i] = b[pos];
			pos ++;
		}
		return out;
	}

	/**
	 * Obtiene el último objeto transparencia aplicado en la renderización
	 * @return GridTransparency
	 */
	public GridTransparency getLastTransparency() {
		return lastTransparency;
	}

	/**
	 * Asigna el último estado de transparencia de la renderización.
	 * @param lastTransparency
	 */
	public void setLastTransparency(GridTransparency lastTransparency) {
		this.lastTransparency = lastTransparency;
		this.lastTransparency.addPropertyListener(this);
		if (getFilterList() != null)
			getFilterList().addEnvParam("Transparency", lastTransparency);
	}

	/**
	 * Obtiene las lista de filtros aplicados en la renderización
	 * @return RasterFilterList
	 */
	public RasterFilterList getFilterList() {
		return filterList;
	}

	/**
	 * Obtiene el último buffer renderizado.
	 * @return IBuffer
	 */
	public IBuffer getLastRenderBuffer() {
		return this.lastRenderBuffer;
	}

	/**
	 * Asigna el último renderizado.
	 * @param buf
	 */
	public void setLastRenderBuffer(IBuffer buf) {
		this.lastRenderBuffer = buf;
	}

	/**
	 * Asigna la lista de filtros que se usará en el renderizado
	 * @param RasterFilterList
	 */
	public void setFilterList(RasterFilterList filterList) {
		this.filterList = filterList;
		this.filterList.addFilterListListener(this);
	}

	/**
	 * Informa de si el raster tiene tabla de color asociada o no.
	 * @return true si tiene tabla de color y false si no la tiene.
	 */
	public boolean existColorTable() {
			return (filterList.getFilterByBaseClass(ColorTableFilter.class) != null);
	}

	/**
	 * Obtiene el grid asociado al render
	 * @return
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * Asigna la factoria de buffer del renderizador
	 * @param bf
	 */
	public void setBufferFactory(BufferFactory bf) {
		this.bufferFactory = bf;
	}

	/**
	 * Evento activado cuando cambia una propiedad de transparencia.
	 */
	public void actionValueChanged(PropertyEvent e) {
		callVisualPropertyChanged(new VisualPropertyEvent(e.getSource()));
	}

	/**
	 * Evento activado cuando cambia la lista de filtros.
	 */
	public void filterListChanged(FilterListChangeEvent e) {
		callVisualPropertyChanged(new VisualPropertyEvent(e.getSource()));
	}
}