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
package org.gvsig.raster;

import java.util.Hashtable;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.incrementabletask.IIncrementable;
import org.gvsig.gui.beans.incrementabletask.IncrementableEvent;
import org.gvsig.gui.beans.incrementabletask.IncrementableListener;
import org.gvsig.gui.beans.incrementabletask.IncrementableTask;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.process.CancelEvent;
import org.gvsig.raster.process.RasterTask;
import org.gvsig.raster.process.RasterTaskQueue;
import org.gvsig.raster.util.RasterToolsUtil;
/**
 * Clase base de todos los procesos raster. En ella se genstionan todas las
 * funciones comunes como incremento de la tarea, gestión de eventos a la tarea, 
 * parámetros de la tarea, etc ...
 * 
 * 18/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public abstract class RasterProcess implements IIncrementable, IncrementableListener, Runnable {
	protected IncrementableTask incrementableTask = null;
	protected volatile Thread   blinker           = new Thread(this);
	protected RasterTask        rasterTask        = null;
	protected IProcessActions   externalActions   = null;
	protected Hashtable         taskParams        = new Hashtable();

	private String              log               = "";
	private String              lastLine          = "";
	private long                time              = 0;
	private boolean             progressActive    = true;
	private IProcessActions     queueActions      = null;

	/**
	 * Crea la ventana de IncrementableTask
	 */
	private IncrementableTask getIncrementableTask() {
		if (incrementableTask == null) {
			incrementableTask = new IncrementableTask(this);
			incrementableTask.addIncrementableListener(this);
		}
		return incrementableTask;
	}
	
	/**
	 * Define si se puede cancelar el proceso. Por defecto es true.
	 * @param enabled
	 */
	public void setCancelable(boolean enabled) {
		getIncrementableTask().getButtonsPanel().setEnabled(ButtonsPanel.BUTTON_CANCEL, enabled);
	}
	
	/**
	 * Muestra la ventana de IncrementableTask
	 */
	private void showIncrementableWindow() {
		if (progressActive) {
			getIncrementableTask().showWindow();
			getIncrementableTask().start();
		}
	}

	/**
	 * Arranca el proceso de recorte de un layer
	 */
	public void start() {
		showIncrementableWindow();
		blinker.start();
	}
		
	/**
	 * Proceso de carga de parámetros. Puede no hacerse una carga de parámetros 
	 * explicita y obtenerlos directamente de la tabla Hash cuando vayan a usarse. 
	 * Esto queda a elección del programador. En caso de hacerse una carga de parámetros
	 * explicita esta llamada debería hacerse justo despues del constructor de la clase
	 * que contendrá el proceso.
	 */
	public abstract void init();
	
	/**
	 * Proceso
	 * @throws InterruptedException
	 */
	public abstract void process() throws InterruptedException;
	
	/**
	 * Obtención de un objeto de resultado. Este objeto deberá ser el mismo que el que se
	 * pasa por parámetro en el "end" de IProcessActions. Este método es util cuando la tarea no se
	 * lanza en un Thread (ejecutamos process directamente) y queremos recoger el resultado
	 * sin registrarnos al evento de finalización de tarea.
	 * @return objeto resultado de la tarea
	 */
	public Object getResult() {
		return null;
	}
	
	/**
	 * Proceso
	 */
	public void execute() throws InterruptedException {
		init();
		process();
	}
	
	/**
	 * Método donde se ejecutará el Thread. Este hará las acciones globales para 
	 * cualquier tarea y llamará al método execute especifico de una tarea.
	 */
	public void run() {
		long t1 = new java.util.Date().getTime();

		try {
			rasterTask = new RasterTask(this);
			RasterTaskQueue.register(rasterTask);
			execute();
		} catch (InterruptedException e) {
			if (externalActions != null)
				externalActions.interrupted();
			Thread.currentThread().interrupt();
		} catch (Exception e) {
			if (progressActive) {
				if (incrementableTask != null) {
					getIncrementableTask().processFinalize();
					incrementableTask = null;
				}
			}
			RasterToolsUtil.messageBoxError("error_processing", this, e);
			queueActions = null;
		} finally {
			RasterTaskQueue.remove(rasterTask);
			if (progressActive) {
				if (incrementableTask != null)
					getIncrementableTask().processFinalize();
			}
			if (queueActions != null)
				queueActions.end(this);
			time = new java.util.Date().getTime() - t1;
		}
	}
	
	/**
	 * Activa o desactiva el interfaz de progreso en el lanzamiento de la tarea como un thread.
	 * @param active true para activarlo o false para desactivarlo
	 */
	public void setProgressActive(boolean active){
		this.progressActive = active;
	}
	
	/**
	 * Obtiene el tiempo que tardó en ejecutarse la tarea 
	 * la última vez que se procesó
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Obtiene el objeto para ejecutar acciones externar.
	 * @param IProcessActions
	 */
	public IProcessActions getActions() {
		return externalActions;
	}

	/**
	 * Asigna el objeto para ejecutar acciones externar.
	 * @param IProcessActions
	 */
	public void setActions(IProcessActions actions) {
		this.externalActions = actions;
	}
	
	/**
	 * Obtiene el objeto para ejecutar acciones de la cola de procesos de ejecución exclusiva.
	 * @param IProcessActions
	 */
	public IProcessActions getUniqueProcessActions() {
		return queueActions;
	}

	/**
	 * Asigna el objeto para ejecutar acciones externar.
	 * @param IProcessActions
	 */
	public void setUniqueProcessActions(IProcessActions actions) {
		this.queueActions = actions;
	}
	
	/**
	 * Inserta una nueva línea en el log del cuadro de incremento de tarea
	 * @param line
	 */
	protected void insertLineLog(String line) {
		lastLine = line;
		log = log + line + "\n";
	}
	
	/**
	 * Obtiene la última línea introducida en el log del cuadro de incremento.
	 */
	public String getLabel() {
		return lastLine;
	}
	
	/**
	 * Obtiene el texto de log del cuadro de incremento completo.
	 */
	public String getLog() {
		return log;
	}
	
	/**
	 * Un evento de cancelado es enviado a la tarea cuando actionCanceled es activado. Para
	 * ello se creará un objeto CancelEvent y se asignará a la tarea en ejecución. Esta lo
	 * procesará cuando pueda e interrumpirá el proceso.
	 */
	public void actionCanceled(IncrementableEvent e) {
		rasterTask.setEvent(new CancelEvent(this));
	}
	
	/**
	 * Añade un parámetro a la tarea
	 * @param name Clave del parámetro
	 * @param param Objeto pasado como parámetro
	 */
	public void addParam(String name, Object param) {
		if (param != null)
			taskParams.put(name, param);
		else
			taskParams.remove(name);
	}

	/**
	 * Elimina un parámetro de la tarea
	 * @param name Clave del parámetro a eliminar
	 */
	public void removeParam(String name) {
		taskParams.remove(name);
	}

	/**
	 * Obtiene un parámetro a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public Object getParam(String name) {
		return taskParams.get(name);
	}
	
	/**
	 * Obtiene un parámetro String a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public String getStringParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof String) ? (String)value : null;
	}
	
	/**
	 * Obtiene un parámetro byte a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public byte getByteParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Byte) ? ((Byte)value).byteValue() : 0;
	}
	
	/**
	 * Obtiene un parámetro float a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public float getFloatParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Float) ? ((Float)value).floatValue() : 0F;
	}
	
	/**
	 * Obtiene un parámetro double a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public double getDoubleParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Double) ? ((Double)value).doubleValue() : 0D;
	}
	
	/**
	 * Obtiene un parámetro entero a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public int getIntParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Integer) ? ((Integer)value).intValue() : 0;
	}
	
	/**
	 * Obtiene un parámetro boolean a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public boolean getBooleanParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Boolean) ? ((Boolean)value).booleanValue() : false;
	}
	
	/**
	 * Obtiene un parámetro int[] a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public int[] getIntArrayParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof int[]) ? ((int[])value) : null;
	}
	
	/**
	 * Obtiene un parámetro double[] a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public double[] getDoubleArrayParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof double[]) ? ((double[])value) : null;
	}
	
	/**
	 * Obtiene un parámetro capa raster a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public FLyrRasterSE getLayerParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof FLyrRasterSE) ? ((FLyrRasterSE)value) : null;
	}
	
	/**
	 * Obtiene un parámetro extent a partir de la clave
	 * @param name Parámetro
	 * @return Parámetro
	 */
	public Extent getExtentParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Extent) ? ((Extent)value) : null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IncrementableListener#actionResumed(org.gvsig.gui.beans.incrementabletask.IncrementableEvent)
	 */
	public void actionResumed(IncrementableEvent e) {	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IncrementableListener#actionSuspended(org.gvsig.gui.beans.incrementabletask.IncrementableEvent)
	 */
	public void actionSuspended(IncrementableEvent e) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#isCancelable()
	 */
	public boolean isCancelable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.incrementabletask.IIncrementable#isPausable()
	 */
	public boolean isPausable() {
		return false;
	}
}