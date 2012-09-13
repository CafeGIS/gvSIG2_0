/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 * funciones comunes como incremento de la tarea, gesti�n de eventos a la tarea, 
 * par�metros de la tarea, etc ...
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
	 * Proceso de carga de par�metros. Puede no hacerse una carga de par�metros 
	 * explicita y obtenerlos directamente de la tabla Hash cuando vayan a usarse. 
	 * Esto queda a elecci�n del programador. En caso de hacerse una carga de par�metros
	 * explicita esta llamada deber�a hacerse justo despues del constructor de la clase
	 * que contendr� el proceso.
	 */
	public abstract void init();
	
	/**
	 * Proceso
	 * @throws InterruptedException
	 */
	public abstract void process() throws InterruptedException;
	
	/**
	 * Obtenci�n de un objeto de resultado. Este objeto deber� ser el mismo que el que se
	 * pasa por par�metro en el "end" de IProcessActions. Este m�todo es util cuando la tarea no se
	 * lanza en un Thread (ejecutamos process directamente) y queremos recoger el resultado
	 * sin registrarnos al evento de finalizaci�n de tarea.
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
	 * M�todo donde se ejecutar� el Thread. Este har� las acciones globales para 
	 * cualquier tarea y llamar� al m�todo execute especifico de una tarea.
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
	 * Obtiene el tiempo que tard� en ejecutarse la tarea 
	 * la �ltima vez que se proces�
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
	 * Obtiene el objeto para ejecutar acciones de la cola de procesos de ejecuci�n exclusiva.
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
	 * Inserta una nueva l�nea en el log del cuadro de incremento de tarea
	 * @param line
	 */
	protected void insertLineLog(String line) {
		lastLine = line;
		log = log + line + "\n";
	}
	
	/**
	 * Obtiene la �ltima l�nea introducida en el log del cuadro de incremento.
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
	 * ello se crear� un objeto CancelEvent y se asignar� a la tarea en ejecuci�n. Esta lo
	 * procesar� cuando pueda e interrumpir� el proceso.
	 */
	public void actionCanceled(IncrementableEvent e) {
		rasterTask.setEvent(new CancelEvent(this));
	}
	
	/**
	 * A�ade un par�metro a la tarea
	 * @param name Clave del par�metro
	 * @param param Objeto pasado como par�metro
	 */
	public void addParam(String name, Object param) {
		if (param != null)
			taskParams.put(name, param);
		else
			taskParams.remove(name);
	}

	/**
	 * Elimina un par�metro de la tarea
	 * @param name Clave del par�metro a eliminar
	 */
	public void removeParam(String name) {
		taskParams.remove(name);
	}

	/**
	 * Obtiene un par�metro a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public Object getParam(String name) {
		return taskParams.get(name);
	}
	
	/**
	 * Obtiene un par�metro String a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public String getStringParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof String) ? (String)value : null;
	}
	
	/**
	 * Obtiene un par�metro byte a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public byte getByteParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Byte) ? ((Byte)value).byteValue() : 0;
	}
	
	/**
	 * Obtiene un par�metro float a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public float getFloatParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Float) ? ((Float)value).floatValue() : 0F;
	}
	
	/**
	 * Obtiene un par�metro double a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public double getDoubleParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Double) ? ((Double)value).doubleValue() : 0D;
	}
	
	/**
	 * Obtiene un par�metro entero a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public int getIntParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Integer) ? ((Integer)value).intValue() : 0;
	}
	
	/**
	 * Obtiene un par�metro boolean a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public boolean getBooleanParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof Boolean) ? ((Boolean)value).booleanValue() : false;
	}
	
	/**
	 * Obtiene un par�metro int[] a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public int[] getIntArrayParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof int[]) ? ((int[])value) : null;
	}
	
	/**
	 * Obtiene un par�metro double[] a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public double[] getDoubleArrayParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof double[]) ? ((double[])value) : null;
	}
	
	/**
	 * Obtiene un par�metro capa raster a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
	 */
	public FLyrRasterSE getLayerParam(String name) {
		Object value = taskParams.get(name);
		return (value != null && value instanceof FLyrRasterSE) ? ((FLyrRasterSE)value) : null;
	}
	
	/**
	 * Obtiene un par�metro extent a partir de la clave
	 * @param name Par�metro
	 * @return Par�metro
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