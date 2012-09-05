package org.gvsig.project.document.table;

import java.util.ArrayList;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSelection;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.tools.observer.Observable;
import org.gvsig.tools.observer.Observer;

import com.iver.andami.messages.NotificationManager;

public class LinkSelectionObserver implements Observer{
	private int field1;
	private int field2;
	private FeatureStore fs1;
	private FeatureStore fs2;
	public LinkSelectionObserver(FeatureStore modelo, FeatureStore modelo2, String field1, String field2) {
		this.fs1=modelo;
		this.fs2=modelo2;
		try {
			this.field1=fs1.getDefaultFeatureType().getIndex(field1);
			this.field2=fs2.getDefaultFeatureType().getIndex(field2);
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void update(Observable arg0, Object arg1) {
		try{
			FeatureSet fCollection1 = (FeatureSet)fs1.getSelection();
			FeatureSelection fCollection2 = (FeatureSelection)fs2.createSelection();
			ArrayList idx = new ArrayList();

			//		Construimos el índice
			DisposableIterator iterator1 = null;
			try {
				iterator1 = fCollection1.iterator();
				while (iterator1.hasNext()) {
					Feature feature = (Feature) iterator1.next();
					Object obj = feature.get(field1);
					if (!idx.contains(obj)) {
						idx.add(obj);
					}
				}
			} finally {
				if (iterator1 != null) {
					iterator1.dispose();
				}
			}
//			fCollection1.dispose();
//			for (int i = bs1.nextSetBit(0); i >= 0;
//			i = bs1.nextSetBit(i + 1)) {
//			Value v = modelo1.getFieldValue((long) i, index1);

//			if (idx.get(v) == null) {
//			idx.put(v, new Integer(i));
//			}
//			}
			FeatureSet set = null;
			DisposableIterator iterator2 = null;

			try {
				set = fs2.getFeatureSet();
				iterator2 = set.iterator();
				while (iterator2.hasNext()) {
					Feature feature = (Feature) iterator2.next();
					Object obj=feature.get(field2);
					if (idx.contains(obj)){
						fCollection2.select(feature);
					}
				}
//				Integer pi = (Integer) idx.get(obj);
//				if (pi != null) {
//				bs2.set(i);
//				}

//				for (int i = 0; i < modelo2.getRowCount(); i++) {
//				Value v = modelo2.getFieldValue(i, index2);
//				Integer pi = (Integer) idx.get(v);

//				if (pi != null) {
//				bs2.set(i);
//				}
//				}
			} catch (DataException e1) {
				NotificationManager.addError(e1);
				return;
			} finally {
				if (iterator2 != null) {
					iterator2.dispose();
				}
				if (set != null) {
					set.dispose();
				}
			}

			// this applies the selection to the linked table
			if (fs1!=fs2) {
//				fs2.beginComplexNotification();
				fs2.setSelection(fCollection2);
//				fs2.endComplexNotification();
				//modelo2.fireSelectionEvents();
			}
		} catch (DataException e2) {
			NotificationManager.addError(e2);
			return;
		}
	}

}
