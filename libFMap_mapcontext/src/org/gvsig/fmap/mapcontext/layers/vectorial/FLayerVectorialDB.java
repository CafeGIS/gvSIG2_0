/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package org.gvsig.fmap.mapcontext.layers.vectorial;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DataStore;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.mapcontext.exceptions.DriverLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LegendLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.NameLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ProjectionLayerException;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.AttrInTableLabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.ILabelingStrategy;
import org.gvsig.tools.dynobject.exception.DynFieldNotFoundException;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.dynobject.exception.DynMethodNotSupportedException;
import org.gvsig.tools.exception.BaseException;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;


public class FLayerVectorialDB extends FLyrVect {
    private boolean loaded = false;

    /* Esto deberia ir en el FLyrDefault */
    public void setProjectionByName(String projectionName)
        throws Exception {
        IProjection proj = CRSFactory.getCRS(projectionName);

        if (proj == null) {
            throw new Exception("No se ha encontrado la proyeccion: " +
                projectionName);
        }

        this.setProjection(proj);
    }

    /* FIXME: esto tendria que tener declarado un throws de algo*/
	public void wakeUp() throws LoadLayerException {
		if (!loaded) {
			this.load();
		}

	}


    public void load() throws LoadLayerException {
		if (this.getName() == null || this.getName().length() == 0) {
			this.setAvailable(false);
			throw new NameLayerException(this.getName(),null);
		}
		try {
			if (this.getFeatureStore() == null) {
				this.setAvailable(false);
				//TODO: traducir???
				throw new DriverLayerException(this.getName(),null);
			}
		} catch (ReadException e1) {
			throw new LoadLayerException(getName(),e1);
		}
		if (this.getProjection() == null) {
			this.setAvailable(false);
			//TODO: traducir???
			throw new ProjectionLayerException(this.getName(),null);
		}

//		try {
//			try {
//				getFeatureStore().load();
//			} catch (ReadDriverException e1) {
//				throw new LoadLayerException(this.getName(),e1);
//			}
//		} catch (Exception e) {
//			this.setAvailable(false);
//			throw new DriverIOException(e);
//		}

//		try {
//			VectorialDBAdapter dbAdapter = new VectorialDBAdapter();
//			dbAdapter.setDriver(this.dbDriver);
//			this.setSource(dbAdapter);

//		} catch (Exception e) {
//			this.setAvailable(false);
//			throw new DriverIOException(e);
//		}

//		try {
			try {
				this.putLoadSelection();
				this.putLoadLegend();
				this.initializeLegendDefault();
			} catch (LegendLayerException e) {
				this.setAvailable(false);
				throw new LegendLayerException(this.getName(),e);
			} catch (ReadException e) {
				this.setAvailable(false);
				throw new LoadLayerException(this.getName(),e);
			} catch (XMLException e) {
				this.setAvailable(false);
				throw new LoadLayerException(this.getName(),e);
			}
//		} catch (Exception e) {
//			this.setAvailable(false);
//			//TODO: traducir???
//			throw new DriverIOException(e);
//		}
		this.cleanLoadOptions();
	}

    /* Esto deberia ir en FLyrVect */
	private void initializeLegendDefault() throws ReadException, LegendLayerException {
		if (this.getLegend() == null) {
//			this.getFeatureStore().getMetadata().
			Object obj=null;
			try {
//				// FIXME arraglar esto
				obj = this.getFeatureStore().getDynValue(
						"WithDefaultLegend");
			} catch (BaseException e1) {
				throw new ReadException(getName(), e1);
			}
            if (obj!=null && ((Boolean)obj).booleanValue()) {
            	ILegend legend = null;
					try {
						legend = (ILegend) getFeatureStore().invokeDynMethod("defaultLegend", null);
					} catch (DynFieldNotFoundException e) {
						throw new LegendLayerException(getName(),e);
					} catch (DynMethodException e) {
						throw new LegendLayerException(getName(),e);
					}

//            	WithDefaultLegend aux = (WithDefaultLegend) this.getRecordset().getDriver();
                this.setLegend((IVectorLegend)legend);



            } else {
                this.setLegend(LegendFactory.createSingleSymbolLegend(
                        this.getShapeType()));
            }
            obj = null;
            try {
				// FIXME Arreglar esto
				obj = this.getFeatureStore().getDynValue("LabelingStrategy");
			} catch (BaseException e) {
				throw new ReadException(getName(), e);
			}
            if (obj!=null && ((Boolean)obj).booleanValue()){
            ILabelingStrategy labeler=null;
				try {
					labeler = (ILabelingStrategy) getFeatureStore()
							.invokeDynMethod("labelingStrategy", null);
				} catch (DynMethodNotSupportedException e) {
					throw new LegendLayerException(getName(),e);
				} catch (DynMethodException e) {
					throw new LegendLayerException(getName(),e);
				}
		    if (labeler instanceof AttrInTableLabelingStrategy) {
            	((AttrInTableLabelingStrategy) labeler).setLayer(this);
            }

            this.setLabelingStrategy(labeler);
            }

		}
	}


    public void setXMLEntity(XMLEntity xml) throws XMLException {
//        IProjection proj = null;
//
//        if (xml.contains("proj")) {
//            proj = CRSFactory.getCRS(xml.getStringProperty("proj"));
//        }
//        else {
//            proj = this.getMapContext().getViewPort().getProjection();
//        }
//
//        this.setName(xml.getName());
//        this.setProjection(proj);
//
//        String driverName = xml.getStringProperty("db");
//        IVectorialDatabaseDriver driver;
//
//        try {
//            driver = (IVectorialDatabaseDriver) LayerFactory.getDM()
//                                                           .getDriver(driverName);
//
//            //Hay que separar la carga de los datos del XMLEntity del load.
//            driver.setXMLEntity(xml.getChild(2));
//            this.setDriver(driver);
//        }
//        catch (Exception e) {
//            throw new XMLException(e);
//        }
//
//        super.setXMLEntityNew(xml);
    }

	public void setDataStore(DataStore dataStore) throws LoadLayerException {
		super.setDataStore(dataStore);
	}
}
