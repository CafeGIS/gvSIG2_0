/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
package com.iver.cit.gvsig.gui.filter;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 21743 $
 */
public interface ExpressionDataSource {
	/**
	 * DOCUMENT ME!
	 *
	 * @param row DOCUMENT ME!
	 * @param idField DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws FilterException DOCUMENT ME!
	 */
	public Object getFieldValue(int row, int idField) throws FilterException;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws FilterException DOCUMENT ME!
	 */
	public int getFieldCount() throws FilterException;

	/**
	 * DOCUMENT ME!
	 *
	 * @param idField DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws FilterException DOCUMENT ME!
	 */
	public String getFieldName(int idField) throws FilterException;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws FilterException DOCUMENT ME!
	 */
	public int getRowCount() throws FilterException;

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getDataSourceName();

//    public void start() throws ReadDriverException;
//    public void stop() throws ReadDriverException;

}
