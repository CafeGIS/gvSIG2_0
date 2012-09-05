package org.gvsig.fmap.mapcontext.layers.vectorial;

import java.io.File;
import java.io.FileNotFoundException;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.exceptions.DriverLayerException;
import org.gvsig.fmap.mapcontext.exceptions.FileLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LegendLayerException;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.exceptions.NameLayerException;
import org.gvsig.fmap.mapcontext.exceptions.ProjectionLayerException;
import org.gvsig.fmap.mapcontext.exceptions.XMLLayerException;
import org.gvsig.fmap.mapcontext.rendering.legend.ILegend;
import org.gvsig.fmap.mapcontext.rendering.legend.IVectorLegend;
import org.gvsig.fmap.mapcontext.rendering.legend.LegendFactory;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.AttrInTableLabelingStrategy;
import org.gvsig.fmap.mapcontext.rendering.legend.styling.ILabelingStrategy;
import org.gvsig.tools.dynobject.exception.DynMethodException;
import org.gvsig.tools.exception.BaseException;

import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

public class FLayerFileVectorial extends FLyrVect{
	private boolean loaded = false;
	private File dataFile = null;
//	private FeatureStore featureStore = null;

	public FLayerFileVectorial() {
		super();
	}

	public FLayerFileVectorial(String name, String fileName,String driverName,String projectionName) throws Exception {
		super();

		this.setName(name);

		this.setFileName(fileName);

//		this.setDriverByName(driverName);

		this.setProjectionByName(projectionName);
	}

	/* Esto deberia ir en el FLyrDefault */
	public void setProjectionByName(String projectionName) throws Exception{
		IProjection proj = CRSFactory.getCRS(projectionName);
		if (proj == null) {
			throw new Exception("No se ha encontrado la proyeccion: "+ projectionName);
		}
		this.setProjection(proj);

	}

	public void setFileName(String filePath) throws FileNotFoundException{
		if (dataFile != null) {
			//TODO: que excepcion lanzar???
			return;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new FileNotFoundException(filePath);
		}
		this.dataFile = file;
	}

	public void setFile(File file) throws FileNotFoundException {
		if (dataFile != null) {
			//TODO: que excepcion lanzar???
			return;
		}
		if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
		this.dataFile = new File(file.getAbsolutePath());
	}

	public String getFileName() {
		if (this.dataFile == null) {
			return null;
		}
		return this.dataFile.getAbsolutePath();
	}


	public void setFeatureStore(FeatureStore fs) throws LoadLayerException {
		this.setDataStore(fs);
	}

//	public void setDriverByName(String driverName) throws DriverLoadException {
//		this.setDriver(
//		  (VectorialFileDriver)LayerFactory.getDM().getDriver(driverName)
//		);
//	}

//	public VectorialFileDriver getDriver() {
//		return this.fileDriver;
//	}

	/* FIXME: esto tendria que tener declarado un throws de algo*/
	public void wakeUp() throws LoadLayerException {
		if (!loaded) {
			this.load();
		}

	}


	public void load() throws LoadLayerException {
		if (this.dataFile == null) {
			this.setAvailable(false);
			throw new FileLayerException(getName(),null);
		}
		if (this.getName() == null || this.getName().length() == 0) {
			this.setAvailable(false);
			throw new NameLayerException(getName(),null);
		}
		try {
			if (this.getFeatureStore() == null) {
				this.setAvailable(false);
				throw new DriverLayerException(getName(),null);
			}
		} catch (ReadException e2) {
			throw new LoadLayerException(getName(),e2);
		} catch (DriverLayerException e2) {
			throw new LoadLayerException(getName(),e2);
		}
		if (this.getProjection() == null) {
			this.setAvailable(false);
			throw new ProjectionLayerException(getName(),null);
		}
		try {
			this.setDataStore(DALLocator.getDataManager().createStore(getFeatureStore().getParameters()));
		} catch (Exception e) {
			throw new LoadLayerException(getName(),e);
		}

			try {
				this.putLoadSelection();
				this.putLoadLegend();
				this.initializeLegendDefault();
			} catch (Exception e) {
				this.setAvailable(false);
				throw new LegendLayerException(getName(),e);
			}
		this.cleanLoadOptions();
		this.loaded = true;
	}

	/* Esto deberia ir en FLyrVect */
	private void initializeLegendDefault() throws LegendLayerException, ReadException {
		if (this.getLegend() == null) {
//			this.getFeatureStore().getMetadata().
			Object obj=null;
			try {
				// FIXME arreglar esto
				obj = this.getFeatureStore().getDynValue(
						"WithDefaultLegend");
			} catch (BaseException e1) {
				throw new ReadException(getName(), e1);
			}
            if (obj!=null && ((Boolean)obj).booleanValue()) {
            	ILegend legend = null;
					try {
						legend = (ILegend) getFeatureStore().invokeDynMethod(
							"defaultLegend", null);
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
//            try {
//            	// FIXME Arregla esto
//				obj = this.getFeatureStore().getMetadata().getDynValue(
//						"LabelingStrategy");
//			} catch (BaseException e) {
//				throw new ReadException(getName(),e);
//			}
            if (obj!=null && ((Boolean)obj).booleanValue()){
            ILabelingStrategy labeler=null;
			try {
				labeler = (ILabelingStrategy) getFeatureStore()
							.invokeDynMethod("labelingStrategy", null);
			} catch (DynMethodException e) {
				throw new LegendLayerException(getName(),e);
			}
			if (labeler instanceof AttrInTableLabelingStrategy) {
            	((AttrInTableLabelingStrategy) labeler).setLayer(this);
            }

            this.setLabelingStrategy(labeler);
            }

		}




//		if (this.getLegend() == null) {
//            if (this.getRecordset().getDriver() instanceof WithDefaultLegend) {
//                WithDefaultLegend aux = (WithDefaultLegend) this.getRecordset().getDriver();
//                this.setLegend((IVectorLegend) aux.getDefaultLegend());
//
//                ILabelingStrategy labeler = aux.getDefaultLabelingStrategy();
//                if (labeler instanceof AttrInTableLabelingStrategy) {
//                	((AttrInTableLabelingStrategy) labeler).setLayer(this);
//                }
//
//                this.setLabelingStrategy(labeler);
//            } else {
//                this.setLegend(LegendFactory.createSingleSymbolLegend(
//                        this.getShapeType()));
//            }
//		}
	}

	public void setXMLEntity(XMLEntity xml) throws XMLException {
        IProjection proj = null;
        if (xml.contains("proj")) {
            proj = CRSFactory.getCRS(xml.getStringProperty("proj"));
        }
        else
        {
            proj = this.getMapContext().getViewPort().getProjection();
        }
		this.setName(xml.getName());
		this.setProjection(proj);
//		try {
//			this.setDriver(
//				(VectorialFileDriver)LayerFactory.getDM().getDriver(
//					xml.getStringProperty("driverName")
//				)
//			);
//		} catch (DriverLoadException e) {
//			throw new XMLException(e);
//		} catch (ClassCastException e) {
//			throw new XMLException(e);
//		}
		try {
			this.setFileName(xml.getStringProperty("file"));
		} catch (FileNotFoundException e) {
			throw new XMLLayerException(getClassName(),e);
		}

		super.setXMLEntityNew(xml);
	}
}
