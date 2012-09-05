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
 * This interface has the characters that they define in one number the thousand
 * separator, the decimal separator and the text separator
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a> * 
 */
public interface Infieldseparators extends Persistent {

	/**
	 * @return the value of field 'decimalseparator'.
	 */
	public String getDecimalseparator();

	/** 
	 * @return the value of field 'textseparator'.
	 */
	public String getTextseparator();

	/** 
	 * @return the value of field 'thousandseparator'.
	 */
	public String getThousandseparator();

	/**
	 * Sets the value of field 'decimalseparator'.
	 * 
	 * @param decimalseparator
	 *            the value of field 'decimalseparator'
	 */
	public void setDecimalseparator(String decimalseparator);

	/**
	 * Sets the value of field 'textseparator'.
	 * 
	 * @param textseparator
	 *            the value of field 'textseparator'.
	 */
	public void setTextseparator(String textseparator);

	/**
	 * Sets the value of field 'thousandseparator'.
	 * 
	 * @param thousandseparator
	 *            the value of field 'thousandseparator'.
	 */
	public void setThousandseparator(String thousandseparator);


}
