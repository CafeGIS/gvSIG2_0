package com.iver.cit.gvsig;

import java.math.BigDecimal;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.DisposableIterator;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureQuery;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.mapcontext.rendering.legend.NullValue;
import org.gvsig.project.document.table.gui.FeatureTableDocumentPanel;
import org.gvsig.project.document.table.gui.Statistics;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;

/**
 * @author Fernando González Cortés
 */
public class TableNumericFieldOperations extends Extension{
	private FeatureTableDocumentPanel table;


	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
		registerIcons();
	}

	private void registerIcons(){
    	PluginServices.getIconTheme().registerDefault(
				"table-statistics",
				this.getClass().getClassLoader().getResource("images/statistics.png")
			);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();

		if (v != null) {
			if (v.getClass() == FeatureTableDocumentPanel.class) {

				FeatureTableDocumentPanel table = (FeatureTableDocumentPanel) v;

				doExecute(table);
			}
		}
	}
	/**
	 * "execute" method acction
	 * @param actionCommand
	 * The acction command that executes this method
	 * @param table
	 * Table to operate
	 */

	protected void doExecute(FeatureTableDocumentPanel table){
		FeatureSet fCollection = null;
		FeatureSet fCollec = null;
		try{
			FeatureAttributeDescriptor fad=table.getTablePanel().getTable().getSelectedColumnsAttributeDescriptor()[0];
			int numRows=0;
			FeatureStore fs=null;
			fs = table.getModel().getStore();

			fCollection = (FeatureSet) fs.getSelection();

			BigDecimal suma = new BigDecimal(0);
			BigDecimal min = new BigDecimal(Double.MAX_VALUE);
			BigDecimal max = new BigDecimal(-Double.MAX_VALUE);
			DisposableIterator iterator = null;
			if (!fCollection.isEmpty()){
				try {
					iterator = fCollection.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						if (feature.get(fad.getName()) == null) {
							continue;
						}
						Number number = (Number) feature.get(fad.getName());
						double d = number.doubleValue();
						numRows++;
						suma = suma.add(new BigDecimal(d));
						if (d < min.doubleValue()) {
							min = new BigDecimal(d);
						}
						if (d > max.doubleValue()) {
							max = new BigDecimal(d);
						}
					}
				} finally {
					if (iterator != null) {
						iterator.dispose();
					}
				}
			}else{

				try {
					FeatureQuery fq = fs.createFeatureQuery();
					fq.setAttributeNames(new String[]{fad.getName()});
					fCollec = fs.getFeatureSet(fq);

					iterator = fCollec.iterator();
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						if (feature.get(fad.getName()) instanceof NullValue) {
							continue;
						}
						Number number = (Number) feature.get(fad.getName());
						double d = number.doubleValue();
						numRows++;
						suma = suma.add(new BigDecimal(d));
						if (d < min.doubleValue()) {
							min = new BigDecimal(d);
						}
						if (d > max.doubleValue()) {
							max = new BigDecimal(d);
						}
					}
				} catch (DataException e) {
					// TODO Auto-generated catch block
					NotificationManager.addError(e);
					return;
				} finally {
					if (iterator != null) {
						iterator.dispose();
					}
					if (fCollec != null) {
						fCollec.dispose();
					}
				}
			}
			Statistics st = new Statistics();
			BigDecimal media = new BigDecimal(0);
			BigDecimal varianza = new BigDecimal(0);
			double desviacion = 0;
			if (numRows==0) {
				suma = new BigDecimal(0);
				min = new BigDecimal(0);
				max = new BigDecimal(0);
			}else {
				media = suma.divide(new BigDecimal(numRows), BigDecimal.ROUND_HALF_DOWN);
				varianza = new BigDecimal(0);
				if (!fCollection.isEmpty()){
					iterator=fCollection.iterator();
				}else{
					iterator=fCollec.iterator();
				}
				try {
					while (iterator.hasNext()) {
						Feature feature = (Feature) iterator.next();
						if (feature.get(fad.getName()) instanceof NullValue) {
							continue;
						}
						Number number = (Number) feature.get(fad.getName());
						double d = number.doubleValue();

						BigDecimal dif = new BigDecimal(d).subtract(media);
						varianza = dif.multiply(dif).add(varianza);
					}
				} finally {
					if (iterator != null) {
						iterator.dispose();
					}
				}
				varianza = varianza.divide(new BigDecimal(numRows), BigDecimal.ROUND_HALF_DOWN);
				desviacion = Math.sqrt(varianza.doubleValue());
			}
			st.setStatistics(media.doubleValue(), max.doubleValue(), min.doubleValue(), varianza.doubleValue(), desviacion, numRows, new BigDecimal(max.doubleValue()).subtract(min).doubleValue(), suma.doubleValue());
			PluginServices.getMDIManager().addWindow(st);
		}catch (DataException e) {
			NotificationManager.addError(e);
		} finally{
			if (fCollection != null){
				fCollection.dispose();
			}
		}
//		}else{

//		}

//		BitSet selectedRows = (BitSet)ds.getSelection().clone();
//		try {
//		int rc = (int) ds.getRowCount();
//		//Si no hay selección se usan todos los registros
//		if (selectedRows.cardinality() == 0){
//		selectedRows.set(0, rc);
//		}else{
//		rc = selectedRows.cardinality();
//		}
//		BigDecimal suma = new BigDecimal(0);
//		BigDecimal min = new BigDecimal(Double.MAX_VALUE);
//		BigDecimal max = new BigDecimal(-Double.MAX_VALUE);
//		for(int i=selectedRows.nextSetBit(0); i>=0; i=selectedRows.nextSetBit(i+1)) {
//		if (fs.getFieldValue(i, fieldIndex) instanceof NullValue)
//		continue;
//		numRows++;
//		double d = ((NumericValue) ds.getFieldValue(i, fieldIndex)).doubleValue();
//		suma = suma.add(new BigDecimal(d));
//		if (d < min.doubleValue()) min = new BigDecimal(d);
//		if (d > max.doubleValue()) max = new BigDecimal(d);
//		}
//		Statistics st = new Statistics();
//		BigDecimal media = new BigDecimal(0);
//		BigDecimal varianza = new BigDecimal(0);
//		double desviacion = 0;
//		if (numRows==0) {
//		suma = new BigDecimal(0);
//		min = new BigDecimal(0);
//		max = new BigDecimal(0);
//		}else {
//		media = suma.divide(new BigDecimal(numRows), BigDecimal.ROUND_HALF_DOWN);
//		varianza = new BigDecimal(0);
//		for(int i=selectedRows.nextSetBit(0); i>=0; i=selectedRows.nextSetBit(i+1)) {
//		if (ds.getFieldValue(i, fieldIndex) instanceof NullValue)
//		continue;
//		double d = ((NumericValue) ds.getFieldValue(i, fieldIndex)).doubleValue();
//		BigDecimal dif = new BigDecimal(d).subtract(media);
//		varianza = dif.multiply(dif).add(varianza);
//		}
//		varianza = varianza.divide(new BigDecimal(numRows), BigDecimal.ROUND_HALF_DOWN);
//		desviacion = Math.sqrt(varianza.doubleValue());
//		}

//		st.setStatistics(media.doubleValue(), max.doubleValue(), min.doubleValue(), varianza.doubleValue(), desviacion, numRows, new BigDecimal(max.doubleValue()).subtract(min).doubleValue(), suma.doubleValue());
//		PluginServices.getMDIManager().addWindow(st);

//		} catch (ReadDriverException e) {
//		NotificationManager.addError("No se pudo acceder a los datos", e);
//		}
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return doIsEnabled(table);
	}

	protected boolean doIsEnabled(FeatureTableDocumentPanel table){
//		FIXME
		FeatureAttributeDescriptor[] fads=null;
		try {
			fads = table.getTablePanel().getTable().getSelectedColumnsAttributeDescriptor();
		} catch (DataException e) {
			e.printStackTrace();
		}

		if (fads.length == 1){
//				int index=indices.nextSetBit(0);
//				if (table.getModel().getStore().getDefaultFeatureType().size()<index+1) {
//					return false;
//				}
				int type = fads[0].getDataType();
				if ((type==DataTypes.LONG) ||
						(type== DataTypes.DOUBLE) ||
								(type==DataTypes.FLOAT) ||
										(type==DataTypes.INT)){
					return true;
				}
		}
		return false;
	}


	/**
	 * @see com.iver.andami.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		IWindow v = PluginServices.getMDIManager().getActiveWindow();
		if (v!=null && v instanceof FeatureTableDocumentPanel) {
			table=(FeatureTableDocumentPanel)v;
			return true;
		}
		return false;
	}

}
