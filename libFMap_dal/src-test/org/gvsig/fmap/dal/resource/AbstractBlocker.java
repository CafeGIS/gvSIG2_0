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
* 2009 IVER T.I. S.A.   {{Task}}
*/

/**
 *
 */
package org.gvsig.fmap.dal.resource;
/**
 * @author jmvivo
 *
 */
public abstract class AbstractBlocker {
	protected int idConsumer = -1;
	protected int consumerData = -1;
	protected boolean inUse = false;

	public abstract void begin(int idConsumer, int consumerData);

	public abstract void end();

	public final boolean inUse() {
		return inUse;
	}

	public int getConsumerData() {
		return this.consumerData;
	}

	public int getConsumerId() {
		return this.idConsumer;
	}

	protected void setUsed(int idConsumer, int consumerData) {
		System.out.println("*** Block " + idConsumer + " " + consumerData);
		inUse = true;
		this.idConsumer = idConsumer;
		this.consumerData = consumerData;
	}

	protected void endUsed() {
		System.out.println("*** Free " + idConsumer + " " + consumerData);
		inUse = false;
		this.idConsumer = -1;
		this.consumerData = -1;
	}


}
