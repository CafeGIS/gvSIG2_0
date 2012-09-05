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
 * 2008 PRODEVELOP		Main development
 */

/**
 * 
 */
package org.gvsig.data.vectorial.filter;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;

/**
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * 
 */
public class ModuleImpl implements Expression {
    Expression exp1;
    Expression exp2;

    public ModuleImpl(Expression exp1, Expression exp2) {
	super();
	this.exp1 = exp1;
	this.exp2 = exp2;
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opengis.filter.expression.Expression#evaluate(java.lang.Object)
     */
    public Object evaluate(Object arg0) {
	
	Object value1 = exp1.evaluate(arg0);
	Object value2 = exp2.evaluate(arg0);
	
	Double doub1 = (Double) value1;
	Double doub2 = (Double) value2;
	
	return doub1%doub2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.opengis.filter.expression.Expression#evaluate(java.lang.Object,
     *      java.lang.Class)
     */
    public Object evaluate(Object arg0, Class arg1) {
	return evaluate(arg0);
    }

}
