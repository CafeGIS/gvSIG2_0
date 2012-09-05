package org.gvsig.fmap.dal.resource.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataParameters;
import org.gvsig.fmap.dal.exception.CopyParametersException;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.resource.Resource;
import org.gvsig.fmap.dal.resource.ResourceParameters;
import org.gvsig.fmap.dal.resource.exception.AccessResourceException;
import org.gvsig.fmap.dal.resource.exception.DisposeResorceManagerException;
import org.gvsig.fmap.dal.resource.exception.ResourceException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotClosedOnDisposeManagerException;
import org.gvsig.fmap.dal.resource.exception.ResourceNotRegisteredException;
import org.gvsig.fmap.dal.resource.spi.AbstractResource;
import org.gvsig.fmap.dal.resource.spi.ResourceManagerProviderServices;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.observer.Observer;
import org.gvsig.tools.observer.impl.DelegateWeakReferencingObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultResourceManager implements ResourceManagerProviderServices {

	final static private String DATA_MANAGER_RESOURCE = "Data.manager.resources";
	final static private String DATA_MANAGER_RESOURCE_PARAMS = "Data.manager.resources.params";

	// FIXME: rellenar las cadenas
	private static final String DATA_MANAGER_RESOURCE_DESCRIPTION = "DAL Resources types";
	private static final String DATA_MANAGER_RESOURCE_PARAMS_DESCRIPTION = "DAL Resources types Parameters";

	private Map resources = new HashMap();

	private DelegateWeakReferencingObservable delegateObservable = new DelegateWeakReferencingObservable(this);

	private Timer timer = null;
	private Logger logger;

	private long mlsecondsToBeIdle = 0;

	public DefaultResourceManager() {
		/*
		 * Create te extensions point in te registry.
		 */
		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_RESOURCE,
				DATA_MANAGER_RESOURCE_DESCRIPTION);

		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_RESOURCE_PARAMS,
				DATA_MANAGER_RESOURCE_PARAMS_DESCRIPTION);
	}

	public Logger getLogger() {
		if (this.logger == null) {
			this.logger = LoggerFactory.getLogger(this.getClass());
		}
		return this.logger;
	}

	public synchronized void remove(Resource resource)
			throws DataException {
		remove(resource.getName());
	}

	public synchronized void remove(String name)
			throws DataException {
		ResourceProvider res = (ResourceProvider) this.resources.get(name);
		if (res == null){
			throw new IllegalArgumentException("Resource not register:" + name);
		}
		if (res.getConsumersCount() < 1) {
			this.resources.remove(name);
			res.notifyDispose();
		}
		res = null;
	}

	public Resource getResource(String key){
		return (Resource)this.resources.get(key);
	}

	public Iterator iterator(){
		return this.resources.values().iterator();
	}

	public void addObserver(Observer o) {
		// TODO añadir el observador a los recursos que ya existiesen
		this.delegateObservable.addObserver(o);
	}

	public void deleteObserver(Observer o) {
		this.delegateObservable.deleteObserver(o);
	}

	public void deleteObservers() {
		this.delegateObservable.deleteObservers();
	}

	public synchronized void collectResources() throws
			DataException {
		ResourceProvider res;
		Iterator iter = this.resources.keySet().iterator();
		String key;
		while (iter.hasNext()) {
			key = (String) iter.next();
			res = (ResourceProvider) this.resources.get(key);
			if (res.getConsumersCount() < 1) {
				res.closeRequest();
				res.notifyDispose();
				iter.remove();
				continue;
			}
			if (mlsecondsToBeIdle > 0
					&& System.currentTimeMillis()
							- res.getLastTimeUsed().getTime() > mlsecondsToBeIdle) {

			}

		}
	}

	private AbstractResource findResource(ResourceParameters params) {
		AbstractResource res;
		Iterator iter = this.resources.values().iterator();
		while (iter.hasNext()) {
			res = (AbstractResource) iter.next();
			try {
				if (res.isThis(params)) {
					return res;
				}
			} catch (ResourceException e) {
				getLogger()
						.warn(
								"Se ha producido un error comprobando si se puede reutilizar el recurso.",
								e);
			} catch (CopyParametersException e) {
				getLogger()
						.warn(
								"Se ha producido un error comprobando si se puede reutilizar el recurso.",
								e);
			}
		}
		return null;
	}

	private AbstractResource addResource(AbstractResource resource)
			throws AccessResourceException {
		resources.put(resource.getName(), resource);
		resource.addObservers(this.delegateObservable);
		return resource;
	}

	public void startResourceCollector(long milis, Observer observer) {
		if (this.timer == null){
			this.timer = new Timer();
		} else{
			this.timer.cancel();
		}
		// TODO observer
		this.timer.scheduleAtFixedRate(new TimerTask() {


			public void run() {
				try {
					DALLocator.getResourceManager().collectResources();
				} catch (DataException e) {
					// TODO Notificar con el observer
				}
			}

		}, milis, milis);
	}

	public void stopResourceCollector() {
		if (this.timer != null) {
			this.timer.cancel();
		}

	}

	public DataParameters createParameters(String type, Object[] args)
			throws InitializeException {
		try {
			DataParameters parameters;
			if (args == null) {
				parameters = (DataParameters) ToolsLocator.getExtensionPointManager()
						.get(
						DATA_MANAGER_RESOURCE_PARAMS).create(type);
			} else {

				parameters = (DataParameters) ToolsLocator.getExtensionPointManager()
						.get(DATA_MANAGER_RESOURCE_PARAMS).create(type, args);
			}
			if (parameters == null){
				throw new ResourceNotRegisteredException(type);
			}
			return parameters;
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		} catch (NoSuchMethodException e) {
			throw new InitializeException(e);
		} catch (InvocationTargetException e) {
			throw new InitializeException(e);
		}
	}

	public DataParameters createParameters(String type)
			throws InitializeException {
		return createParameters(type, null);
	}

	public ResourceProvider createResource(String type, Object[] params)
			throws InitializeException {
		return createResource((ResourceParameters) createParameters(type,
				params));
	}

	public ResourceProvider createResource(ResourceParameters params)
			throws InitializeException {

		AbstractResource resource = this.findResource(params);
		if (resource != null) {
			return resource;
		}

		try {

			resource = (AbstractResource) ToolsLocator
					.getExtensionPointManager().get(DATA_MANAGER_RESOURCE)
					.create(
							params.getTypeName(), new Object[] { params }
				);
			if (resource == null) {
				throw new ResourceNotRegisteredException(params.getTypeName());
			}
		} catch (InstantiationException e) {
			throw new InitializeException(e);
		} catch (IllegalAccessException e) {
			throw new InitializeException(e);
		} catch (SecurityException e) {
			throw new InitializeException(e);
		} catch (IllegalArgumentException e) {
			throw new InitializeException(e);
		} catch (NoSuchMethodException e) {
			throw new InitializeException(e);
		} catch (InvocationTargetException e) {
			throw new InitializeException(e);
		}

		try {
			return addResource(resource);
		} catch (AccessResourceException e) {
			throw new InitializeException(e);
		}
	}

	public boolean register(String type, String description,
			Class resourceHandler, Class resourceParams) {


		ToolsLocator.getExtensionPointManager().add(DATA_MANAGER_RESOURCE,
				DATA_MANAGER_RESOURCE_DESCRIPTION).append(type, description,
						resourceHandler);

		ToolsLocator.getExtensionPointManager().add(
				DATA_MANAGER_RESOURCE_PARAMS,
				DATA_MANAGER_RESOURCE_PARAMS_DESCRIPTION).append(type,
				description, resourceParams);

		return true;
	}

	public List getResourceProviders() {
		return ToolsLocator.getExtensionPointManager().get(
				DATA_MANAGER_RESOURCE).getNames();
	}

	public void closeResources() throws DataException {
		this.collectResources();
		Resource res;
		Iterator iter = this.resources.values().iterator();
		while (iter.hasNext()) {
			res = (Resource) iter.next();
			res.closeRequest();
		}
	}

	public void dispose() throws DisposeResorceManagerException {
		DisposeResorceManagerException exception = new DisposeResorceManagerException();

		try {
			this.collectResources();
		} catch (DataException e) {
			exception.add(e);
		}
		Resource res;
		Iterator iter = this.resources.values().iterator();
		while (iter.hasNext()) {
			res = (Resource) iter.next();
			try {
				res.closeRequest();
				if (res.openCount() > 0) {
					exception
							.add(new ResourceNotClosedOnDisposeManagerException(
									res));
				}
				iter.remove();
			} catch (ResourceException e) {
				exception.add(e);
			}
		}

		this.resources = null;
		this.delegateObservable.deleteObservers();
		this.delegateObservable = null;

		if (!exception.isEmpty()) {
			throw exception;
		}
	}

	public int getTimeToBeIdle() {
		if (mlsecondsToBeIdle == 0) {
			return 0;
		}
		return (int) (mlsecondsToBeIdle / 1000);
	}

	public void setTimeToBeIdle(int seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("seconds must be >= 0");
		}
		mlsecondsToBeIdle = seconds * 1000;
	}

}
