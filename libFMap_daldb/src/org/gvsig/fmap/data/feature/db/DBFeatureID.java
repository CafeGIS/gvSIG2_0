package org.gvsig.fmap.data.feature.db;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.dal.feature.FeatureType;

public class DBFeatureID implements FeatureReference{
	protected Object[] featureKey;
	protected DBStore store;

	protected DBFeatureID(DBStore store, Object[] featureKey) {
		this.featureKey=featureKey;
		this.store=store;
	}
	public Object[] getKey(){
		return featureKey;
	}
	public Feature getFeature(FeatureType featureType) throws DataException{
		return store.getFeatureByID(featureType, featureKey);
	}
	public boolean equals(Object obj) {
		if (obj instanceof DBFeatureID){
			DBFeatureID other = (DBFeatureID)obj;
			if (this.store != other.store) {
				return false;
			}
			Comparable obj1,obj2;
			for (int i=0;i<featureKey.length;i++){
				obj1 = (Comparable)this.featureKey[i];
				obj2 = (Comparable)other.featureKey[i];
				if (obj1 == null || obj2 == null){
					if (!(obj1 == null && obj2 == null)){
						return false;
					}
				} else{
					if (obj1.compareTo(obj2) != 0){
						return false;
					}
				}

			}
			return true;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		StringBuffer strBuffer= new StringBuffer();
		strBuffer.append(this.store.hashCode());
		strBuffer.append("[$");
		for (int i=0;i<featureKey.length;i++){
			strBuffer.append(featureKey[i]);
			strBuffer.append("$,$");
		}
		strBuffer.append("]");
		return strBuffer.toString().hashCode();
	}



}