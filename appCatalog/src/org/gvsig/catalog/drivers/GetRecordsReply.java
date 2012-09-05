package org.gvsig.catalog.drivers;

import java.net.URI;
import java.util.ArrayList;

import org.gvsig.catalog.CatalogLocator;
import org.gvsig.catalog.CatalogManager;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.schemas.Record;

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
/* CVS MESSAGES:
 *
 * $Id: GetRecordsReply.java 600 2007-09-19 11:30:05 +0000 (Wed, 19 Sep 2007) jpiera $
 * $Log$
 * Revision 1.1.2.1  2007/07/23 07:14:25  jorpiell
 * Catalog refactoring
 *
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class GetRecordsReply {
	private static final CatalogManager catalogManager = CatalogLocator.getCatalogManager();
	private ArrayList records = null;
	private int numRecords = 0;
	
	public GetRecordsReply(int numRecords){
		this.numRecords = numRecords;
		records = new ArrayList();
	}
	
	/**
	 * Add a new record
	 * @param record
	 * rRecord to add
	 */
	public void addRecord(Record record){
		records.add(record);
	}
	
	/**
	 * Add a new record
	 * @param uri
	 * Server uri
	 * @param node
	 * XML node
	 */
	public void addRecord(URI uri, XMLNode node){
		Record record = catalogManager.createRecord(uri, node);
		records.add(record);		
	}
	
	/**
	 * Gets a record that it is in a concrete position
	 * @param index
	 * Record position
	 * @return
	 * A record
	 */
	public Record getRecordAt(int index){
		if (index >= records.size()){
			return null;
		}
		return (Record)records.get(index);
	}

	/**
	 * @return the numRecords
	 */
	public int getRecordsNumber() {
		return numRecords;
	}
	
	/**
	 * @return the numRecords
	 */
	public int getRetrievedRecordsNumber() {
		return records.size();
	}
	
	/**
	 * @param numRecords the numRecords to set
	 */
	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
	
}
