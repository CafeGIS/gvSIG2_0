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
package org.gvsig.raster.grid.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.TreeMap;

import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.Params;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.grid.GridTransparency;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
/**
 * Filtro para raster. Ancestro de todos los filtros.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public abstract class RasterFilter implements IRasterFilter, Cloneable {
	protected IBuffer   raster       = null;
	protected IBuffer   rasterResult = null;
	protected int       height       = 0;
	protected int       width        = 0;
	protected Hashtable params       = new Hashtable();
	protected TreeMap   environment  = new TreeMap();
	protected Extent    extent       = null;
	private int         percent      = 0;
	private String      fName        = "";
	/**
	 * Variable que control la aplicación o no del filtro. Si está a false aunque esté en
	 * la pila el filtro no se ejecutará.
	 */
	protected boolean 		exec = true;

	/**
	 * Clase que representa un kernel de NxN píxeles para realizar operaciones sobre
	 * un pixel.
	 * @author Nacho Brodin (nachobrodin@gmail.com)
	 */
	public static class Kernel {
		public double[][]	kernel	= null;
		protected double	divisor = 0;

		/**
		 * Constructor. Crea la matriz de datos para el kernel.
		 * @param k datos del kernel
		 */
		public Kernel(double[][] k) {
			this.kernel = k;

			for (int i = 0; i < kernel.length; i++)
				for (int j = 0; j < kernel[0].length; j++)
					divisor = divisor + kernel[i][j];
		}

		/**
		 * Constructor. Crea la matriz de datos para el kernel.
		 * @param k datos del kernel
		 */
		public Kernel(double[][] k, double divisor) {
			this.kernel = k;
			this.divisor = divisor;
		}

		public double kernelOperation(Kernel k) {
			double res = 0;
			for (int i = 0; i < kernel.length; i++)
				for (int j = 0; j < kernel[0].length; j++)
					res += kernel[i][j] * k.kernel[i][j];
			return res;
		}

		/**
		 * Aplica la operación de convolución del kernel con otro kernel
		 * pasado por parámetro
		 * @param k
		 * @return
		 */
		public double convolution(Kernel k) {
			double res = this.kernelOperation(k);
			if (this.divisor != 0)
				res = res / divisor;
			//return Math.abs(res);
			return res;
		}

		public double getDivisor() {
			return divisor;
		}

		public void setDivisor(double divisor) {
			this.divisor = divisor;
		}

		/**
		 * Obtiene el tamaño del kernel que viene dado por
		 * el número de pixeles de su lado.
		 * @return
		 */
		public int getLado() {
			return kernel.length;
		}

		/**
		 * Aplica ls operación 0xff para todos los elementos del
		 * kernel. Presupone que este es de tipo byte y no hace ninguna
		 * comprobación al respecto. Se deja en manos del usuario aplicar esta
		 * operación solo cuando los elementos del kernel sean de este tipo de dato.
		 */
		public void rgbNormalization() {
			for (int i = 0; i < kernel.length; i++)
				for (int j = 0; j < kernel[0].length; j++)
					kernel[i][j] = ((byte)kernel[i][j]) & 0xff;
		}
	}

	/**
	 * Constructor
	 */
	public RasterFilter() {
	}
	
	/**
	 * Instancia un filtro a partir de su nombre
	 * @param strPackage Paquete 
	 * @return Filtro instanciado
	 * @throws FilterTypeException
	 */
	static public RasterFilter createFilter(String strPackage) throws FilterTypeException {
		Class filterClass = null;
		try {
			filterClass = Class.forName(strPackage.trim());
		} catch (ClassNotFoundException e) {
			throw new FilterTypeException("No puedo instanciar " + strPackage.trim());
		}
		
		Constructor con = null;
		try {
			con = filterClass.getConstructor(null);
		} catch (SecurityException e) {
			throw new FilterTypeException("");
		} catch (NoSuchMethodException e) {
			throw new FilterTypeException("");
		}
		
		RasterFilter newFilter = null;
		try {
			newFilter = (RasterFilter) con.newInstance(null);
		} catch (IllegalArgumentException e) {
			throw new FilterTypeException("");
		} catch (InstantiationException e) {
			throw new FilterTypeException("");
		} catch (IllegalAccessException e) {
			throw new FilterTypeException("");
		} catch (InvocationTargetException e) {
			throw new FilterTypeException("");
		}
		return newFilter;
	}

	/**
	 * Aplica el filtro sobre el raster pasado pixel a pixel
	 * @throws InterruptedException
	 */
	public void execute() throws InterruptedException {
		RasterTask task = RasterTaskQueue.get(Thread.currentThread().toString());
		pre();
		if (raster != null && raster.getDataType() != this.getInRasterDataType())
			exec = false;
		percent = 0;
		if (exec) {
			for (int row = 0; row < height; row ++) {
				for (int col = 0; col < width; col ++)
					process(col, row);

				if (task.getEvent() != null)
					task.manageEvent(task.getEvent());

				percent = (row * 100) / height;
			}
		}
		percent = 100;
		post();
	}

	/**
	 * Añade un parámetro al filtro
	 *
	 * @param name Clave del parámetro
	 * @param param Objeto pasado como parámetro
	 */
	public void addParam(String name, Object param) {
		if (param != null)
			params.put(name, param);
		else
			params.remove(name);
	}

	/**
	 * Elimina un parámetro del filtro
	 * @param name Clave del parámetro a eliminar
	 */
	public void removeParam(String name) {
		params.remove(name);
	}

	/**
	 * Obtiene un parámetro a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public Object getParam(String name) {
		return params.get(name);
	}

		/**
	 * @param extent The extent to set.
	 */
	public void setExtent(Extent extent) {
		this.extent = extent;
	}

	/**
	 * Obtiene true si el filtro va a ser ejecutado o false si no va a serlo
	 * @return
	 */
	public boolean isExec() {
		return exec;
	}

	/**
	 * Asigna el valor a la variable exec. Esta estará a true si el filtro se ejecutará la próxima
	 * vez que se repinte o false si no se ejecuta.
	 * @param exec
	 */
	public void setExec(boolean exec) {
		this.exec = exec;
	}

	/**
	 * Pone a cero el contador del porcentaje del proceso de filtrado
	 * @return
	 */
	public void resetPercent() {
		percent = 0;
	}

	/**
	 * Obtiene el porcentaje recorrido del proceso de filtrado
	 * @return
	 */
	public int getPercent() {
		return percent;
	}

	/**
	 * Función que contiene el código a ejecutar antes de aplicar el filtro
	 */
	abstract public void pre();

	/**
	 * Función que contiene el código a ejecutar despues de aplicar el filtro
	 */
	abstract public void post();

	/**
	 * Ejecución del filtro para un pixel de la imagen
	 */
	abstract public void process(int x, int y);

	/**
	 * Obtiene el tipo de datos del raster de entrada
	 */
	abstract public int getInRasterDataType();

	/**
	 * Obtiene el tipo de datos del raster de salida
	 */
	abstract public int getOutRasterDataType();

	/**
	 * Obtiene el resultado del filtro despues de su ejecución a través de una clave
	 * @param name        clave para obtener un objeto resultado del filtro.
	 */
	abstract public Object getResult(String name);

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.grid.filter.IRasterFilter#getGroup()
	 */
	abstract public String getGroup();

	/**
	 * Obtener que datos puede tratar una interfaz con sus valores
	 * @param nameFilter. Cada tipo de filtro puede tener parametros distintos
	 * @return
	 */
	abstract public Params getUIParams(String nameFilter);

	/**
	 * Obtener que las entradas que puede aperecer un filtro en el interfaz
	 * @return
	 */
	abstract public String[] getNames();

	/**
	 * Devolverá un booleano indicando si es visible o no en el panel de filtros.
	 * @return
	 */
	public boolean isVisible() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Devuelve el nombre interno del filtro
	 * @return the fName
	 */
	public String getName() {
		return fName;
	}

	/**
	 * @param name the fName to set
	 */
	public void setName(String name) {
		fName = name;
	}

	/**
	 * Obtiene el TreeMap con los parámetros del entorno
	 * @return TreeMap
	 */
	public TreeMap getEnv() {
		return environment;
	}

	/**
	 * Asigna el TreeMap con los parámetros del entorno
	 * @param env
	 */
	public void setEnv(TreeMap env) {
		this.environment = env;
	}

	protected void mergeBufferTransparency(IBuffer rasterAlpha) {
		GridTransparency transparency = (GridTransparency) environment.get("Transparency");

		if (transparency != null) {
			if (transparency.getAlphaBand() != null)
				transparency.mergeBuffer(rasterAlpha, transparency.getAlphaBand());
			else
				transparency.setAlphaBand(rasterAlpha);

			transparency.activeTransparency();
		}
	}
}