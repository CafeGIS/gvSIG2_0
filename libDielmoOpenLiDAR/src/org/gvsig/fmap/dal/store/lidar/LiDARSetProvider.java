package org.gvsig.fmap.dal.store.lidar;

import java.util.NoSuchElementException;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ReadRuntimeException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.spi.FeatureProvider;
import org.gvsig.fmap.dal.feature.spi.FeatureSetProvider;
import org.gvsig.tools.evaluator.Evaluator;
import org.gvsig.tools.evaluator.EvaluatorData;
import org.gvsig.tools.evaluator.EvaluatorException;

public class LiDARSetProvider implements FeatureSetProvider {

	private LiDARStoreProvider store;
	private FeatureQuery query;
	private FeatureType featureType;

	public LiDARSetProvider(LiDARStoreProvider liDARStoreProvider,
			FeatureQuery query, FeatureType featureType) throws DataException {
		this.store = liDARStoreProvider;
		this.query = query;
		this.featureType = featureType;
	}

	public boolean canFilter() {
		return false;
	}

	public boolean canIterateFromIndex() {
		return true;
	}

	public boolean canOrder() {
		return false;
	}

	public DisposableIterator fastIterator(long index) throws DataException {
		return new FastLiDARIterator(this.store, this.featureType,
				index);
	}

	public DisposableIterator fastIterator() throws DataException {
		return this.fastIterator(0);
	}

	public long getSize() throws DataException {
		return this.store.getFeatureCount();
	}

	public boolean isEmpty() throws DataException {
		return this.store.getFeatureCount() > 0;
	}

	public DisposableIterator iterator() throws DataException {
		return this.iterator(0);
	}

	public DisposableIterator iterator(long index) throws DataException {
		return new LiDARIteratorScale(this.store, this.query,
				this.featureType,
				index);
	}


	private class LiDARIteratorScale implements DisposableIterator {
		protected long curIndex;
		protected LiDARStoreProvider store;
		protected FeatureType featureType;
		private long count;
		private Evaluator filter;
		FeatureQuery query;
		protected boolean nextChecked;
		private FeatureProvider current;

		public LiDARIteratorScale(LiDARStoreProvider store, FeatureQuery query,FeatureType featureType,
				long index) throws DataException {

			this.store = store;
			this.featureType = featureType;
			this.curIndex = index;
			this.count = this.store.getFeatureCount();
			this.query = query;
			this.filter = query.getFilter();
			nextChecked=false;
		}

		public boolean hasNext() {

			if (nextChecked) {
				return curIndex < count;
			}
			try {
				doNext();
			} catch( DataException e) {
				NullPointerException ex = new NullPointerException();
				ex.initCause(e);
				throw ex;
			}
			return curIndex < count;
		}

		protected void doNext() throws DataException {
			nextChecked = true;

			FeatureProvider featureProvider;
			Feature feature;
			long i=this.curIndex;
			while (i<count) {
				featureProvider=this.createFeature();
				feature =store.getStoreServices().createFeature(featureProvider);

				if (this.filter==null) {
					this.current = featureProvider;
					return;
				} else{
					try {
						if(((Boolean) this.filter.evaluate((EvaluatorData)feature)).booleanValue()){
							this.current = featureProvider;
							return;
						}
					} catch (EvaluatorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++; // este indice debe indicar el salto de puntos para implemetar la estrategia
			}


			this.current = null;
		}


		public Object next() {
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			try{
				FeatureProvider data = this.createFeature();
				curIndex++;
				return data;
			} catch (DataException e){
				throw new ReadRuntimeException(this.store.getName(), e);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}

		protected FeatureProvider createFeature() throws DataException {
			return this.store.getFeatureProviderByIndex(curIndex, featureType);
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

	}



	private class LiDARIterator implements DisposableIterator {
		protected long curIndex;
		protected LiDARStoreProvider store;
		protected FeatureType featureType;
		private long count;



		public LiDARIterator(LiDARStoreProvider store, FeatureType featureType,
				long index) throws DataException {

			this.store = store;
			this.featureType = featureType;
			this.curIndex = index;
			this.count = this.store.getFeatureCount();
		}

		public boolean hasNext() {
			return curIndex < count;
		}

		public Object next() {
			if (!hasNext()){
				throw new NoSuchElementException();
			}
			try{
				FeatureProvider data = this.createFeature();
				curIndex++;
				return data;
			} catch (DataException e){
				throw new ReadRuntimeException(this.store.getName(), e);
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();

		}

		protected FeatureProvider createFeature() throws DataException {
			return this.store.getFeatureProviderByIndex(curIndex, featureType);
		}

		public void dispose() {
			// TODO Auto-generated method stub

		}

	}

	private class FastLiDARIterator extends LiDARIterator {
		public FastLiDARIterator(LiDARStoreProvider store,
				FeatureType featureType, long index) throws DataException {
			super(store, featureType, index);
			this.curData = this.store.createFeatureProvider(featureType);
		}


		private FeatureProvider curData;


		protected FeatureProvider createFeature() throws DataException {
			this.store.loadFeatureProviderByIndex(curData, curIndex,
					featureType);
			return curData;
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

}
