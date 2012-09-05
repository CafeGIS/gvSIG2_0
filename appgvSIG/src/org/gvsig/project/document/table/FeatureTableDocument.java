/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  {TableDocument implementation based on the gvSIG DAL API}
 */
package org.gvsig.project.document.table;

import java.util.ArrayList;
import java.util.Iterator;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureQueryOrder;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureStoreNotification;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.gvsig.project.document.table.gui.TableProperties;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * @author <a href="mailto:cordin@disid.com">Cèsar Ordiñana</a>
 */
public class FeatureTableDocument extends ProjectDocument implements Observer {

    private static final long serialVersionUID = -1842181135614158881L;

    private FeatureStore store;

	private String featureTypeId;

	private String[] attributeNames;

	private String linkTable;

	private String field1;

	private String field2;

	private LinkSelectionObserver linkSelectionObserver;

	private FLyrVect associatedTable;

	private FeatureQuery query;

	private Evaluator baseFilter;

	private FeatureQueryOrder baseOrder;



    public FeatureTableDocument(FeatureStore store) {
        this(store, store.createFeatureQuery());
    }

    public FeatureTableDocument(FeatureStore store, FeatureQuery query) {
        this.store = store;
        this.query = null;
        store.addObserver(this);
        this.featureTypeId = query.getFeatureTypeId();
        this.baseFilter = query.getFilter();
		this.baseOrder = query.getOrder();


    }

    public FeatureQuery getQuery(){
    	if (this.query == null){
    		try {
    			FeatureType fType = null;
				this.query = this.store.createFeatureQuery();
				if (this.featureTypeId != null) {
					Iterator<FeatureType> iter;
					iter = this.store.getFeatureTypes().iterator();
					while (iter.hasNext()) {
						fType = iter.next();
						if (this.featureTypeId.equals(fType.getId())) {
							this.query.setFeatureType(fType);
							break;
						}
					}
	    			if (fType == null) {
						// TODO
	    				throw new RuntimeException("FeatureType '"
								+ this.featureTypeId + "'not found");
					}

				} else {
					fType = store.getDefaultFeatureType();
				}

				if (this.attributeNames != null) {
					ArrayList<String> newNames = new ArrayList<String>();
					for (String name : this.attributeNames) {
						if (fType.getIndex(name) > -1) {
							newNames.add(name);
						}
					}
					if (newNames.size() > 0) {
						this.query.setAttributeNames(newNames
								.toArray(this.attributeNames));
					}
				}

				this.query.setFilter(this.baseFilter); // TODO check is valid
				this.query.setOrder(this.baseOrder);


    		} catch (DataException e) {
				// TODO Auto-generated catch block
				NotificationManager.addError(e);
				return null;
			}

    	}
    	return this.query;
    }

    @Override
    public void afterAdd() {
        // Nothing to do
    }

    @Override
    public void afterRemove() {
        // Nothing to do
    }

    @Override
    public IWindow createWindow() {
        try {
        	FeatureTableDocumentPanel tablePanel = new FeatureTableDocumentPanel(
					this);
        	callCreateWindow(tablePanel);
			return tablePanel;
        } catch (DataException e) {
            // TODO ¿QUé SE HACE EN ESTOS CASOS?
            e.printStackTrace();
            NotificationManager.addError(e);
        }

        return null;
    }

    @Override
    public void exportToXML(XMLEntity root, Project project)
            throws SaveException {
        // TODO Auto-generated method stub

    }

    @Override
    public IWindow getProperties() {
        return new TableProperties(this);
    }

    /**
     * @return the store
     */
    public FeatureStore getStore() {
        return store;
    }

	@Override
	public void importFromXML(XMLEntity root, XMLEntity typeRoot,
			int elementIndex, Project project, boolean removeDocumentsFromRoot)
			throws XMLException, OpenException,
			org.gvsig.fmap.dal.exception.ReadException {
		// TODO Auto-generated method stub

	}

	/**
	 * Devuelve el identificador de la tabla que contiene el link.
	 *
	 * @return identificador único de la tabla.
	 */
	public String getLinkTable() {
		return linkTable;
	}

	/**
	 * Devuelve el nombre del campo de la tabla a enlazar.
	 *
	 * @return Nombre del campo de la tabla a enlazar.
	 */
	public String getField1() {
		return field1;
	}

	/**
	 * Devuelve el nombre del campo de la tabla enlazada.
	 *
	 * @return Nombre del campo de la tabla enlazada.
	 */
	public String getField2() {
		return field2;
	}

	/**
	 * Inserta el identificador de la tabla, el campo de la primera tabla y el
	 * campo de la segunda tabla.
	 *
	 * @param lt
	 *            identificado de la tabla.
	 * @param f1
	 *            nombre del campo de la primera tabla.
	 * @param f2
	 *            nombre del campo de la segunda tabla.
	 */
	public void setLinkTable(String lt, String f1, String f2) {
		linkTable = lt;
		field1 = f1;
		field2 = f2;
	}

	/**
	 * Borra el identificador de la tabla y elimina del array de listener los
	 * listener que sean del tipo: LinkSelectionObserver
	 */
	public void removeLinkTable() {
		linkTable = null;
//		try {
			getStore().deleteObserver(linkSelectionObserver);//removeLinksSelectionListener();
//		} catch (ReadException e) {
//			e.printStackTrace();
//		}
	}
	public void addLinkSelectionObserver(LinkSelectionObserver lso) {
		linkSelectionObserver=lso;

	}

	public FLyrVect getAssociatedLayer() {
		return associatedTable;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param associatedTable
	 *            DOCUMENT ME!
	 */
	public void setAssociatedLayer(FLyrVect associatedTable) {
		this.associatedTable = associatedTable;
	}

	public void setStore(FeatureStore fs) {
		this.store=fs;
	}

	public void update(Observable arg0, Object arg1) {
		if (this.store.equals(arg0)) {
			if (arg1 instanceof FeatureStoreNotification) {
				FeatureStoreNotification event = (FeatureStoreNotification) arg1;
				if (event.getType() == FeatureStoreNotification.TRANSFORM_CHANGE
						|| event.getType() == FeatureStoreNotification.RESOURCE_CHANGED) {
					this.query = null;
				}
			}

		}

	}

}