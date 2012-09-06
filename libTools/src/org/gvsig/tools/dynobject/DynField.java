package org.gvsig.tools.dynobject;


/**
 * @author <a href="mailto:jjdelcerro@gvsig.org">Joaqu�n Jos� del Cerro</a>
 * @author <a href="mailto:cordin@disid.com">C�sar Ordi�ana</a>
 */
public interface DynField {

	static final int SINGLE = 1;
    static final int CHOICE = 2;
    static final int RANGE = 3;

	String getName();

	int getType();

	String getDescription();

	Object getDefaultValue();

	boolean isMandatory();

	boolean isPersistent();

	int getTheTypeOfAvailableValues(); // SINGLE, CHOICE o RANGE

	DynObjectValueItem[] getAvailableValues();

	Object getMinValue();

	Object getMaxValue();

	DynField setDescription(String description);

	DynField setType(int type);

	DynField setDefaultValue(Object defaultValue);

	DynField setMandatory(boolean mandatory);

	DynField setPersistent(boolean persistent);

	DynField setTheTypeOfAvailableValues(int type);

	DynField setAvailableValues(DynObjectValueItem[] values);

	DynField setMinValue(Object minValue);

	DynField setMaxValue(Object maxValue);
}
