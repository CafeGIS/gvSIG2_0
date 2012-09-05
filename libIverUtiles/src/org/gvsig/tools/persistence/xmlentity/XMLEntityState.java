package org.gvsig.tools.persistence.xmlentity;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceValueNotFoundException;
import org.gvsig.tools.persistence.Persistent;
import org.gvsig.tools.persistence.PersistentState;
import org.gvsig.tools.persistence.impl.AbstractPersistentState;

import com.iver.utiles.NotExistInXMLEntity;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xmlEntity.generate.XmlTag;

public class XMLEntityState extends AbstractPersistentState {
	final private static int DATA_POS = 0;
	
	final private static String KEY_CLASSNAME = "_classname_";
	final private static String KEY_DATA = "_item_data_";
	final private static String VALUE = "_value_";
	
	final private static String KEY_CHILD = "_childName_";
	final private static String ITEM = "_iterator_item_";
	
	public static String PROJECTENCODING = "UTF-8";

	protected XMLEntity xmlEntity;
	protected XMLEntityManager manager;
	protected XMLEntity data;

	public XMLEntityState(XMLEntityManager manager) {
		this(manager, new XMLEntity());
	}

	public XMLEntityState(XMLEntityManager manager, XMLEntity xmlEntity) {
		this.manager = manager;
		this.xmlEntity = xmlEntity;
		this.data = xmlEntity.firstChild(KEY_DATA);
	}

	protected void init() {
		this.data = new XMLEntity();
		this.data.getXmlTag().setName(KEY_DATA);
		this.xmlEntity.addChild(this.data);
	}

	public XMLEntity getXMLEntity() {
		return this.xmlEntity;
	}

	public void setTheClass(Object obj) {
		this.xmlEntity.putProperty(KEY_CLASSNAME, obj.getClass().getName());
	}

	public void setTheClass(Class theClass) {
		this.xmlEntity.putProperty(KEY_CLASSNAME, theClass.getName());
	}

	public void setTheClass(String name) {
		this.xmlEntity.putProperty(KEY_CLASSNAME, name);
	}

	public String getTheClassName() {
		return this.xmlEntity.getStringProperty(KEY_CLASSNAME);
	}

	protected XMLEntity getData() {
		if (this.data==null) {
			init();
		}
		return this.data;
	}

	protected void setName(String name) {
		this.xmlEntity.getXmlTag().setName(name);
	}

	protected String getName() {
		return this.xmlEntity.getXmlTag().getName();
	}

	public Object get(String name) throws PersistenceException {
		XMLEntity value = this.getData().firstChild(name);
		if( value == null ) {
			try {
				return this.getData().getStringProperty(name);
			} catch (NotExistInXMLEntity e) {
				throw new PersistenceValueNotFoundException(name, e);
			}
		}
		return manager.create(manager.createState(value));
	}

	public boolean getBoolean(String name)
			throws PersistenceValueNotFoundException {
		try {
			return this.getData().getBooleanProperty(name);
		} catch (NotExistInXMLEntity e) {
			throw new PersistenceValueNotFoundException(name, e);
		}
	}

	public double getDouble(String name)
			throws PersistenceValueNotFoundException {
		try {
			return this.getData().getDoubleProperty(name);
		} catch (NotExistInXMLEntity e) {
			throw new PersistenceValueNotFoundException(name, e);
		}
	}

	public float getFloat(String name) throws PersistenceValueNotFoundException {
		try {
			return this.getData().getFloatProperty(name);
		} catch (NotExistInXMLEntity e) {
			throw new PersistenceValueNotFoundException(name, e);
		}
	}

	public int getInt(String name) throws PersistenceValueNotFoundException {
		try {
			return this.getData().getIntProperty(name);
		} catch (NotExistInXMLEntity e) {
			throw new PersistenceValueNotFoundException(name, e);
		}
	}

	public long getLong(String name) throws PersistenceValueNotFoundException {
		try {
			return this.getData().getLongProperty(name);
		} catch (NotExistInXMLEntity e) {
			throw new PersistenceValueNotFoundException(name, e);
		}
	}

	public String getString(String name)
	throws PersistenceValueNotFoundException {
		return this.getData().getStringProperty(name);
	}

	public void set(String name, String value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, int value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, long value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, double value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, float value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, boolean value) {
		this.getData().putProperty(name, value);
	}

	public void set(String name, Persistent obj)
			throws PersistenceException {
		XMLEntityState state = (XMLEntityState) manager.getState(obj);
		state.setName(name);
		this.getData().addChild(state.xmlEntity);
	}

	public static class PersistentIterator implements Iterator, Persistent {
		private XMLEntityState state = null;
		int current = 0;

		public PersistentIterator() {
			
		}

		public boolean hasNext() {
			return (state!=null && current< state.getData().getChildrenCount());
		}
		public Object next() {
			Object value = null;
			XMLEntity xmlEntity = (XMLEntity) state.getData().getChild(current++);
			String className = xmlEntity.getStringProperty(KEY_CLASSNAME);
			if (className.equals(String.class.getName())) {
				value = xmlEntity.getObjectProperty(VALUE);
			} else if (className.equals(Integer.class.getName())) {
				value = xmlEntity.getIntProperty(VALUE);
			} else if (className.equals(Long.class.getName())) {
				value = xmlEntity.getLongProperty(VALUE);
			} else if (className.equals(Double.class.getName())) {
				value = xmlEntity.getDoubleProperty(VALUE);
			} else if (className.equals(Float.class.getName())) {
				value = xmlEntity.getFloatProperty(VALUE);
			} else if (className.equals(Boolean.class.getName())) {
				value = xmlEntity.getBooleanProperty(VALUE);
			}
			else if (className.equals(PersistentState.class)) {
				if (xmlEntity.getChildrenCount()>0) {
					try {
						value = state.manager.create(state.manager.createState(xmlEntity));
					} catch (PersistenceException e) {
						// FIXME
						throw new RuntimeException(e);
					}
				}
			}
			else { // suppose it is a Persistent object
				try {
					value = state.manager.create(state.manager.createState(xmlEntity));
				} catch (PersistenceException e) {
					// FIXME
					throw new RuntimeException(e);
				}
			}
			return value;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
		public void loadFromState(PersistentState state) throws PersistenceException {
			if (state instanceof XMLEntityState) {
				this.state = (XMLEntityState) state;
				current = 0;
			}
			else {
				throw new PersistenceException("setState(PersistentState): Not supported yet");
			}
		}

		public void saveToState(PersistentState state)
				throws PersistenceException {
			throw new UnsupportedOperationException();
		}
	}
	
	public Iterator getIterator(String name)
		throws PersistenceException, PersistenceValueNotFoundException
	{
		XMLEntity value = this.getData().firstChild(name);
		if (value == null) {
			throw new PersistenceValueNotFoundException(name);
		}
		return (Iterator) manager.create(manager.createState(value));
	}

	public void set(String name, Iterator it)
			throws PersistenceException {
		XMLEntityState stateList = manager.createStateInstance();
		stateList.setName(name);
		stateList.setTheClass(PersistentIterator.class);
		while (it.hasNext()) {
			Object value = it.next();
			if (value instanceof Persistent) {
				XMLEntityState persistentState = 
					(XMLEntityState) manager.getState((Persistent)value);
				persistentState.setName(ITEM);
				stateList.getData().addChild(persistentState.xmlEntity);
			}
			else if (value instanceof XMLEntityState) {
				XMLEntityState persistentState = 
					(XMLEntityState) value;
				persistentState.setName(ITEM);
				stateList.getData().addChild(persistentState.xmlEntity);
			}
			else if (value instanceof Integer
					|| value instanceof Long || value instanceof Double
					|| value instanceof Float || value instanceof String
					|| value instanceof Boolean) {
				XMLEntityState stateItem = (XMLEntityState) manager.createStateInstance();
				stateItem.setName(ITEM);
				stateItem.xmlEntity.putProperty(KEY_CLASSNAME, value.getClass().getName());
				stateItem.xmlEntity.putProperty(VALUE, value);
				stateList.getData().addChild(stateItem.xmlEntity);
				
			} else {
				//TODO add a meaningful message and maybe use a more specific exception
				throw new PersistenceException(new RuntimeException());
			}
		}
//		stateList.xmlEntity.putProperty(KEY_CHILD, name);
		this.getData().addChild(stateList.xmlEntity);
	}
	
	

	private class NamesIterator implements Iterator {

		private String name;
		private XMLEntity xmlEntity;
		private int index;
		private int propertiyCount;

		public NamesIterator(XMLEntity xmlEntity) {
			this.xmlEntity = xmlEntity;
			this.name = null;
			this.index = 0;
			this.propertiyCount = xmlEntity.getPropertyCount();
		}

		private void testNext() {
			if (this.name != null) {
				return;
			}
			String tmpName;
			while (this.index < this.propertiyCount) {
				tmpName = this.xmlEntity.getPropertyName(index);
				index++;
				if (!tmpName.equals(KEY_CHILD) || tmpName.equals(KEY_CLASSNAME)) {
					continue;
				} else {
					this.name = tmpName;
					break;
				}
			}

		}

		public boolean hasNext() {
			this.testNext();
			return this.index < this.propertiyCount;
		}

		public Object next() {
			this.testNext();
			if (this.name == null) {
				throw new NoSuchElementException();
			}
			String tmpName = this.name;
			this.name = null;
			return tmpName;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public Iterator getNames() {
		return new NamesIterator(this.xmlEntity);
	}

	public void set(String name, Object value)
			throws PersistenceException {
		if (value instanceof Persistent) {
			set(name, (Persistent)value);
		} else if (value instanceof Integer || value instanceof Long
				|| value instanceof Double || value instanceof Float
				|| value instanceof String || value instanceof Boolean) {
			this.getData().putProperty(name, value);
		} else if (value instanceof Iterator) {
			set(name, (Iterator)value);
		}
		else {
			throw new IllegalArgumentException(name);
		}
	}

	public void load(Reader reader) throws PersistenceException {
		try {
			Object obj = XmlTag.unmarshal(reader);
			xmlEntity = new XMLEntity((XmlTag)obj);
			data = xmlEntity.firstChild(KEY_DATA);
		} catch (MarshalException e) {
			throw new PersistenceException(e);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}
	}

	public void save(Writer writer) throws PersistenceException {
		try {
			Marshaller m = new Marshaller(writer);
			m.setEncoding(PROJECTENCODING);
			m.marshal(xmlEntity.getXmlTag());
		} catch (IOException e) {
			throw new PersistenceException(e);
		} catch (MarshalException e) {
			throw new PersistenceException(e);
		} catch (ValidationException e) {
			throw new PersistenceException(e);
		}
		
	}
}
