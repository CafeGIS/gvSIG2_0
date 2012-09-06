package org.gvsig.tools.dynobject.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynMethod;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;
import org.gvsig.tools.dynobject.exception.DynMethodException;

public class DefaultDynClass implements DynClass {

	public class FieldAndIndex {
		DefaultDynField field;
		int index;

		FieldAndIndex(DynField field, int index) {
			this.field = (DefaultDynField) field;
			this.index = index;
		}
		public int getIndex() {
			return this.index;
		}
		public DefaultDynField getDynField() {
			return this.field;
		}
	}

	public class MethodAndIndex {
		DynMethod method;
		int index;

		MethodAndIndex(DynMethod method, int index) {
			this.method = method;
			this.index = index;
		}
		public int getIndex() {
			return this.index;
		}
		public DynMethod getDynMethod() {
			return this.method;
		}
	}

    DynObjectManager manager;

    private String name;
    private String description;

    private boolean isAnonymous;

    private Map classes;
    private Map declaredFieldsMap;
    private Map declaredMethodsMap;

    // This attributes are calculated by consolide method
    private DefaultDynClass[] superClasses;
	private Map superClassesMap;
    private DynField[] declaredFields;
	private Map fieldsMap;
	private DynField[] fields;
    private DynMethod[] declaredMethods;
	private Map methodsMap;
	private DynMethod[] methods;


    public DefaultDynClass(DynObjectManager manager, String name, String description) {
    	this.isAnonymous = false;
    	this.classes = new LinkedHashMap();
    	this.declaredFieldsMap = new HashMap();
    	this.declaredMethodsMap = new HashMap();

    	this.forceConsolide();

    	this.manager = manager;
    	this.name = name;
    	this.description = description;
    }

    public DefaultDynClass(DynObjectManager manager, String name, String description, DynClass[] superClases) {
    	this(manager,name,description);
    	for( int i=0; i<superClases.length; i++ ) {
    		DynClass dynClass = superClases[i];
    		if( ! this.classes.containsKey(dynClass.getName()) ) {
    			this.classes.put(dynClass.getName(), dynClass);
    		}
    	}
    }

   	public DynObjectManager getManager() {
    	return this.manager;
    }

    public void consolide() {
    	// Use classes and decalredFieldsMap to update all.

    	// Actualize superClasses array
    	this.superClasses = (DefaultDynClass[]) buildSuperDynClassSet()
				.toArray(new DefaultDynClass[] {});

    	// Actualize declaredFields array
    	this.declaredFields = (DynField[]) this.declaredFieldsMap.values()
		.toArray(new DynField[] {});

    	// Actualize declaredMethods array
    	this.declaredMethods = (DynMethod[]) this.declaredMethodsMap.values()
		.toArray(new DynMethod[] {});

    	// Actualize fieldsMap
    	this.fieldsMap = new LinkedHashMap();
    	int index = 0;
    	for( int i=0; i<this.declaredFields.length; i++) {
    		this.fieldsMap.put(this.declaredFields[i].getName(), new FieldAndIndex(this.declaredFields[i],index++));
    	}
    	for( int i=0; i<this.superClasses.length ; i++ ) {
        	Iterator it = this.superClasses[i].declaredFieldsMap.values().iterator();
        	while( it.hasNext() ) {
        		DynField field = (DynField) it.next();
	    		if( !this.fieldsMap.containsKey(field.getName()) ) {
	    			this.fieldsMap.put(field.getName(), new FieldAndIndex(field,index++));
	    		}
        	}
    	}

    	// Actualize methodsMap
    	this.methodsMap = new LinkedHashMap();
    	index = 0;
    	for( int i=0; i<this.declaredMethods.length; i++) {
    		this.methodsMap.put(this.declaredMethods[i].getName(), new MethodAndIndex(this.declaredMethods[i],index++));
    	}
    	for( int i=0; i<this.superClasses.length ; i++ ) {
        	Iterator it = this.superClasses[i].declaredMethodsMap.values().iterator();
        	while( it.hasNext() ) {
        		DynMethod method = (DynMethod) it.next();
	    		if( !this.methodsMap.containsKey(method.getName()) ) {
	    			this.methodsMap.put(method.getName(), new MethodAndIndex(method,index++));
	    		}
        	}
    	}


		// Actualize fields array
		this.fields = new DynField[ this.fieldsMap.size() ];
		int i=0;
		Iterator it = this.fieldsMap.values().iterator();
		while( it.hasNext() ) {
		    FieldAndIndex findex = (FieldAndIndex) it.next();
            DynField field = findex.getDynField();
			fields[i++] = field;
		}

		// Actualize methods array
		this.methods = new DynMethod[ this.methodsMap.size() ];
		i=0;
		it = this.methodsMap.values().iterator();
		while( it.hasNext() ) {
		    MethodAndIndex mindex = (MethodAndIndex) it.next();
            DynMethod method = mindex.getDynMethod();
			methods[i++] = method;
		}

		// Actualize superClassesMap
		this.superClassesMap = new HashMap();
		for( i=0 ; i<this.superClasses.length ; i++) {
			this.superClassesMap.put(this.superClasses[i].getName(), this.superClasses[i]);
		}
    }

    private void forceConsolide() {
        this.superClasses = null;
        this.superClassesMap = null;
        this.declaredFields = null;
        this.fieldsMap = null;
        this.fields = null;
        this.declaredMethods = null;
        this.methodsMap = null;
        this.methods = null;
    }

    private Set buildSuperDynClassSet() {
        Set dynClassParents = new LinkedHashSet();
        buildSuperDynClassList(this, dynClassParents);
        return dynClassParents;
    }

    private void buildSuperDynClassList(DefaultDynClass dynClass, Set allParents) {
    	Collection values = dynClass.classes.values();
        Iterator it = values.iterator();
        while( it.hasNext() ) {
        	DynClass dc = (DynClass) it.next();
        	allParents.add(dc);
        }
        it = values.iterator();
        while( it.hasNext() ) {
        	DefaultDynClass dc = (DefaultDynClass) it.next();
        	buildSuperDynClassList(dc, allParents);
        }
    }

    public Object[] createValues(Object[] oldvalues) {
    	if( this.fields == null ) {
    		consolide();
    	}
    	if (oldvalues != null && oldvalues.length >= this.fields.length) {
    		return oldvalues;
    	}
    	Object[] extended = new Object[ this.fields.length ];
    	if( oldvalues != null ) {
    		System.arraycopy(oldvalues, 0, extended, 0, oldvalues.length);
    	}
    	return extended;
    }

	public void extend(DynClass dynClass) {
		if( this.classes.containsKey(dynClass.getName()) ) {
			return;
		}
		this.classes.put(dynClass.getName(), dynClass);
		this.forceConsolide();
	}

	public void extend(String dynClassName) {
		DynClass dynClass = manager.get(dynClassName);
		extend(dynClass);
	}

    public int getFieldIndex(String name) {
    	if( this.fieldsMap == null ) {
    		consolide();
    	}
    	FieldAndIndex f = (FieldAndIndex) this.fieldsMap.get(name);
    	if( f == null ) {
    		return -1;
    	}
    	return f.index;
    }

	public DynField getDeclaredDynField(String name) {
		return (DynField)this.declaredFieldsMap.get(name);
	}

	public DynField[] getDeclaredDynFields() {
		return this.declaredFields;
	}

	public String getDescription() {
		return this.description;
	}

	public DynField getDynField(String name) {
    	if( this.fieldsMap == null ) {
    		consolide();
    	}
    	FieldAndIndex findex = (FieldAndIndex) fieldsMap.get(name);
        return findex == null ? null : findex.getDynField();
	}

	public FieldAndIndex getDynFieldAndIndex(String name) {
    	if( this.fieldsMap == null ) {
    		consolide();
    	}
		return (FieldAndIndex) this.fieldsMap.get(name);
	}

	public DynField[] getDynFields() {
    	if( this.fields == null ) {
    		consolide();
    	}
		return this.fields;
	}

	public String getName() {
		return this.name;
	}

	public DynField addDynField(String name) {
		DynField field = new DefaultDynField(name);
		declaredFieldsMap.put(name, field);
		this.forceConsolide();
		return field;
	}

	public DynClass[] getSuperDynClasses() {
    	if( this.superClasses == null ) {
    		consolide();
    	}
		return this.superClasses;
	}

	public DynObject newInstance() {
		return this.manager.createDynObject(this);
	}

	public boolean isInstance(DynObject dynObject) {
    	if( this.superClassesMap == null ) {
    		consolide();
    	}
		DefaultDynClass objClass = (DefaultDynClass) dynObject.getDynClass();
		if( this.superClassesMap.containsKey(objClass.getName()) ) {
			return true;
		}
		if(objClass.isAnonymous ) {
			Iterator it = objClass.classes.values().iterator();
			while( it.hasNext() ) {
				DynClass dc = (DynClass) it.next();
				if( this.superClassesMap.containsKey(dc.getName()) ) {
					return true;
				}
			}
		}
		return false;
	}

	public void removeDynField(String name) {
		this.declaredFieldsMap.remove(name);
		this.forceConsolide();
	}

	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}

	public boolean isAnonymous() {
		return isAnonymous;
	}

	public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DynClass) {
            return name.equals(((DynClass) obj).getName());
        }
        return false;
    }

	public void addDynMethod(DynMethod dynMethod) {
		this.manager.registerDynMethod(this, dynMethod);
	}

	void addMethod(DynMethod dynMethod) {
		declaredMethodsMap.put(dynMethod.getName(), dynMethod);
		this.forceConsolide();
	}

	public DynMethod getDeclaredDynMethod(String name) {
		return (DynMethod)this.declaredMethodsMap.get(name);
	}

	public DynMethod[] getDeclaredDynMethods() {
    	if (this.declaredMethods == null) {
			consolide();
		}
		return this.declaredMethods;
	}

	public DynMethod getDynMethod(String name) throws DynMethodException {
    	if( this.methodsMap == null ) {
    		consolide();
    	}
    	MethodAndIndex mindex = (MethodAndIndex) methodsMap.get(name);
        return mindex == null ? null : mindex.getDynMethod();
	}

	public DynMethod getDynMethod(int code) throws DynMethodException {
		return this.manager.getDynMethod(code);
	}

	public DynMethod[] getDynMethods() {
    	if( this.methods == null ) {
    		consolide();
    	}
		return this.methods;
	}

	public void removeDynMethod(String name) {
		// TODO Auto-generated method stub

	}

}
