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
 * 2009 {Iver T.I.}   {Task}
 */

package org.gvsig.fmap.dal.store.gpe;

import java.io.File;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.OpenException;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.impl.DefaultFeature;
import org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.fmap.dal.resource.file.FileResource;
import org.gvsig.fmap.dal.resource.spi.ResourceConsumer;
import org.gvsig.fmap.dal.resource.spi.ResourceProvider;
import org.gvsig.fmap.dal.spi.DataStoreProviderServices;
import org.gvsig.fmap.dal.store.gpe.handlers.FmapContentHandler;
import org.gvsig.fmap.dal.store.gpe.handlers.FmapErrorHandler;
import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.gpe.gml.parser.GPEGmlSFP0Parser;
import org.gvsig.gpe.parser.GPEErrorHandler;
import org.gvsig.gpe.parser.GPEParser;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.dynobject.DynClass;
import org.gvsig.tools.dynobject.DynField;
import org.gvsig.tools.dynobject.DynObject;
import org.gvsig.tools.dynobject.DynObjectManager;

/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class GPEStoreProvider extends AbstractFeatureStoreProvider implements
ResourceConsumer {
	public static String NAME = "GPEStore";
	public static String DESCRIPTION = "GPE file";
	private static final String DYNCLASS_NAME = "GPEStore";
	protected static DynClass DYNCLASS = null;
	private GPEStoreParameters gpeParams;
	protected File m_Fich;
	private ResourceProvider gpeResource;
	private boolean isOpen = false;
	private GPEParser parser = null;

	public GPEStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices)
	throws InitializeException {
		this(params,storeServices,ToolsLocator.getDynObjectManager()
				.createDynObject(DYNCLASS));
	}

	protected GPEStoreProvider(DataStoreParameters params,
			DataStoreProviderServices storeServices, DynObject metadata)
			throws InitializeException {

		super(params, storeServices, metadata);

		this.setDynValue("Envelope", null);

		m_Fich = getGPEParameters().getFile();
		gpeResource = this.createResource(FileResource.NAME,
				new Object[] { m_Fich.getAbsolutePath() });
		gpeResource.addConsumer(this);
		this.initFeatureType();
	}

	protected void initFeatureType() throws InitializeException {
		try {
			this.open();
		} catch (DataException e) {
			throw new InitializeException(this.getName(), e);
		}
	}

	private GPEStoreParameters getGPEParameters() {
		return (GPEStoreParameters) this.getParameters();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.resource.spi.ResourceConsumer#closeResourceRequested(org.gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public boolean closeResourceRequested(ResourceProvider resource) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.resource.spi.ResourceConsumer#resourceChanged(org.gvsig.fmap.dal.resource.spi.ResourceProvider)
	 */
	public void resourceChanged(ResourceProvider resource) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#canWriteGeometry(int)
	 */
	public boolean canWriteGeometry(int geometryType, int geometrySubtype)
			throws DataException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createNewOID()
	 */
	public Object createNewOID() {
		return ((FmapContentHandler)parser.getContentHandler()).createNewOID();
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createSet(org.gvsig.fmap.dal.feature.FeatureQuery)
	 */
	public FeatureSetProvider createSet(FeatureQuery query)
	throws DataException {
		return new GPESetProvider(this, query);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureProviderByReference(org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices)
	 */
	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference) throws DataException {
		return this.getFeatureProviderByReference(reference, this
				.getFeatureStore()
				.getDefaultFeatureType());
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureProviderByReference(org.gvsig.fmap.dal.feature.spi.FeatureReferenceProviderServices, org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureProvider getFeatureProviderByReference(
			FeatureReferenceProviderServices reference, FeatureType featureType)
	throws DataException {
		return this.getFeatureProviderByIndex((Long) reference.getOID(),
				featureType);
	}

	FeatureProvider getFeatureProviderByIndex(Long index, FeatureType fType)
	throws DataException {
		return ((DefaultFeature)((FmapContentHandler)parser.getContentHandler())
				.getFeatureSet().get(index)).getData();
	}


	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getFeatureReferenceOIDType()
	 */
	public int getOIDType() {
		return DataTypes.LONG;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#getName()
	 */
	public String getName() {
		return NAME;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.DataStoreProvider#getSourceId()
	 */
	public Object getSourceId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.spi.DataStoreProvider#open()
	 */
	public void open() throws OpenException {
		if (isOpen) {
			return;
		}
		try {
			//parser = GPERegister.createParser(m_Fich.toURI());
			parser = new  GPEGmlSFP0Parser();
			GPEErrorHandler errorHandler = new FmapErrorHandler();
			parser.parse(new FmapContentHandler(errorHandler,
					getStoreServices(), this, m_Fich),
					errorHandler,
					m_Fich.toURI());
		} catch (Exception e) {
			throw new OpenException("Imposible create a parser",e);
		}
	}

	protected static void registerDynClass() {
		DynObjectManager dynman = ToolsLocator.getDynObjectManager();
		DynClass dynClass;
		DynField field;
		if (DYNCLASS == null) {

			dynClass = dynman.add(DYNCLASS_NAME);

			// The SHP store parameters extend the DBF store parameters
			dynClass.extend(dynman.get(FeatureStore.DYNCLASS_NAME));

			DYNCLASS = dynClass;
		}

	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.FeatureStoreProvider#createSet(org.gvsig.fmap.dal.feature.FeatureQuery, org.gvsig.fmap.dal.feature.FeatureType)
	 */
	public FeatureSetProvider createSet(FeatureQuery query,
			FeatureType featureType) throws DataException {
		return new GPESetProvider(this, query, featureType);
	}

	public long getFeatureCount() throws DataException {
		FmapContentHandler handler = (FmapContentHandler)parser.getContentHandler();
		return handler.getFeaturesCount();
	}

	/**
	 * @return the parser
	 */
	public GPEParser getParser() {
		return parser;
	}

	/* (non-Javadoc)
	 * @see org.gvsig.fmap.dal.feature.spi.AbstractFeatureStoreProvider#getEnvelope()
	 */
	public Envelope getEnvelope() throws DataException {
		this.open();
		return (Envelope) this.getDynValue("Envelope");
	}

	public void setEnvelope(Envelope envelope) {
		this.setDynValue("Envelope", envelope);
	}


}

