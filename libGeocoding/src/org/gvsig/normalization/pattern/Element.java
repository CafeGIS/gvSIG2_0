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
 * 2008 Prodevelop S.L. main development
 */
package org.gvsig.normalization.pattern;

import org.gvsig.tools.persistence.Persistent;

/**
 * This interface are the all elements that they make the pattern. Each element
 * defines one new field in final table. The attributes of each element are: -
 * (_fieldname) Name of the new field - (_fieldtype) Type of the new field
 * (String, Integer, Decimal or Date) - (_fieldwith) Number of position to split
 * the main string. If this value is zero the split process will be via
 * separators - (_fieldseparator) separators between fields -
 * (infieldseparators) special characters within one substring (thousand
 * character, decimal character, text characters) - (_importfield) this boolean
 * defines if this new field will be normalized
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */
public interface Element extends Persistent {	

	/**
	 * @return the value of field 'fieldname'.
	 */
	public String getFieldname();

	/**
	 * @return the value of field 'fieldseparator'.
	 */
	public Fieldseparator getFieldseparator();

	/**
	 * @return the value of field 'fieldtype'.
	 */
	public Fieldtype getFieldtype();

	/**
	 * @return the value of field 'fieldwidth'.
	 */
	public int getFieldwidth();

	/**
	 * @return the value of field 'importfield'.
	 */
	public boolean getImportfield();

	/**
	 * @return the value of field 'infieldseparators'.
	 */
	public Infieldseparators getInfieldseparators();

	/**
	 * Sets the value of field 'fieldname'.
	 * 
	 * @param fieldname
	 *            the value of field 'fieldname'.
	 */
	public void setFieldname(String fieldname);

	/**
	 * Sets the value of field 'fieldseparator'.
	 * 
	 * @param fieldseparator
	 *            the value of field 'fieldseparator'.
	 */
	public void setFieldseparator(Fieldseparator fieldseparator);

	/**
	 * Sets the value of field 'fieldtype'.
	 * 
	 * @param fieldtype
	 *            the value of field 'fieldtype'.
	 */
	public void setFieldtype(Fieldtype fieldtype);

	/**
	 * Sets the value of field 'fieldwidth'.
	 * 
	 * @param fieldwidth
	 *            the value of field 'fieldwidth'.
	 */
	public void setFieldwidth(int fieldwidth);

	/**
	 * Sets the value of field 'importfield'.
	 * 
	 * @param importfield
	 *            the value of field 'importfield'.
	 */
	public void setImportfield(boolean importfield);

	/**
	 * Sets the value of field 'infieldseparators'.
	 * 
	 * @param infieldseparators
	 *            the value of field 'infieldseparators'.
	 */
	public void setInfieldseparators(Infieldseparators infieldseparators);


	

}
