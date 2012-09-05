package org.gvsig.fmap.dal.spi;

import java.util.Arrays;
import java.util.Iterator;

import org.gvsig.fmap.dal.DataParameters;
import org.gvsig.fmap.dal.exception.CopyParametersException;
import org.gvsig.fmap.dal.exception.ParameterMissingException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.tools.dynobject.DelegatedDynObject;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.persistence.PersistenceException;
import org.gvsig.tools.persistence.PersistenceValueNotFoundException;
import org.gvsig.tools.persistence.PersistentState;


/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
 * AUTHORS (In addition to CIT):
 * 2008 IVER T.I. S.A.   {{Task}}
 */

/**
 *
 */
/**
 * @author jmvivo
 *
 */
public abstract class AbstractDataParameters implements DataParameters {
	protected DelegatedDynObject delegatedDynObject;

	public Object getDynValue(String name) {
		return this.delegatedDynObject.getDynValue(name);
	}

	public void setDynValue(String name, Object value) {
		this.delegatedDynObject.setDynValue(name, value);
		// return this ???
	}

	public void clear() {
		// TODO Delegar en el DynObject cuando tenga este servicio

		DynField[] fields = delegatedDynObject.getDynClass()
				.getDeclaredDynFields();

		for (int i = 0; i < fields.length; i++) {
			this.setDynValue(fields[i].getName(), fields[i].getDefaultValue());
		}
	}

	protected void copyValuesTo(AbstractDataParameters target) {
		// TODO Delegar en el DynObject cuando tenga este servicio
		DynField[] fields = delegatedDynObject.getDynClass()
				.getDynFields();


		for (int i = 0; i < fields.length; i++) {
			target.setDynValue(fields[i].getName(), this.getDynValue(fields[i]
					.getName()));
		}
	}

	public DataParameters getCopy() {
		// TODO Delegar en el DynObject cuando tenga este servicio
		AbstractDataParameters copy;
		try {
			copy = (AbstractDataParameters) this.getClass().newInstance();
		} catch (InstantiationException e) {
			throw new CopyParametersException("data parameters", e);
		} catch (IllegalAccessException e) {
			throw new CopyParametersException("data parameters", e);
		}
		this.copyValuesTo(copy);
		return copy;
	}

	public void saveToState(PersistentState state) throws PersistenceException {
		DynField[] fields = delegatedDynObject.getDynClass()
				.getDeclaredDynFields();

		for (int i = 0; i < fields.length; i++) {
			state.set(fields[i].getName(), this
					.getDynValue(fields[i].getName()));
		}
	}

	public void loadFromState(PersistentState state) throws PersistenceException {
		Iterator it = state.getNames();
		while (it.hasNext()) {
			String name = (String) it.next();
			try {
				this.setDynValue(name, state.get(name));
			} catch (PersistenceValueNotFoundException e) {
				// Ignore
			}
		}
	}

	public void delegate(DynObject dynObject) {
		this.delegatedDynObject.delegate(dynObject);

	}

	public DynClass getDynClass() {
		return this.delegatedDynObject.getDynClass();
	}

	public boolean hasDynValue(String name) {
		return this.delegatedDynObject.hasDynValue(name);
	}

	public void implement(DynClass dynClass) {
		this.delegatedDynObject.implement(dynClass);
	}

	public Object invokeDynMethod(String name, DynObject context)
			throws DynMethodException {
		return this.delegatedDynObject.invokeDynMethod(this, name, context);
	}

	public Object invokeDynMethod(int code, DynObject context)
			throws DynMethodException {
		return this.delegatedDynObject.invokeDynMethod(this, code, context);
	}

	public void validate() throws ValidateDataParametersException {
		ValidateDataParametersException exception = new ValidateDataParametersException();
		Iterator iter = Arrays.asList(this.getDynClass().getDynFields())
				.iterator();
		DynField field;
		while (iter.hasNext()) {
			field = (DynField) iter.next();
			if (field.isMandatory()) {
				if (!this.hasDynValue(field.getName())) {
					if (field.getDefaultValue() != null) {
						this.setDynValue(field.getName(), field
								.getDefaultValue());
					} else {
						exception.add(new ParameterMissingException(field
								.getName()));
					}
				}
			} else {
				if (field.getDefaultValue() != null
						&& !this.hasDynValue(field.getName())) {
					this.setDynValue(field.getName(), field.getDefaultValue());
				}
			}

		}
		if (exception.size() > 0) {
			throw exception;
		}

	}

}
