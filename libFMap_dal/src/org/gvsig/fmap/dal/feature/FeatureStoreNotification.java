package org.gvsig.fmap.dal.feature;

import org.gvsig.fmap.dal.DataSet;
import org.gvsig.fmap.dal.DataStoreNotification;
import org.gvsig.tools.undo.command.Command;

public interface FeatureStoreNotification extends DataStoreNotification {



	public static final String BEFORE_UNDO = "before_Undo_DataStore";
	public static final String BEFORE_REDO = "before_Redo_DataStore";

	public static final String AFTER_UNDO = "after_Undo_DataStore";
	public static final String AFTER_REDO = "after_Redo_DataStore";

	public static final String BEFORE_REFRESH = "before_refresh";
	public static final String AFTER_REFRESH = "before_refresh";

	public static final String LOCKS_CHANGE = "after_LockedChange_DataStore";

	public static final String BEFORE_STARTEDITING = "before_StartEditing_DataStore";
	public static final String AFTER_STARTEDITING = "after_StartEditing_DataStore";

	public static final String BEFORE_CANCELEDITING = "before_CancelEditing_DataStore";
	public static final String AFTER_CANCELEDITING = "after_CancelEditing_DataStore";

	public static final String BEFORE_FINISHEDITING = "before_FinishEditing_DataStore";
	public static final String AFTER_FINISHEDITING = "after_FinishEditing_DataStore";


	public static final String BEFORE_UPDATE_TYPE = "before_Update_Type";
	public static final String AFTER_UPDATE_TYPE = "after_Update_Type";

	public static final String BEFORE_UPDATE = "before_Update_Feature";
	public static final String AFTER_UPDATE = "after_Update_Feature";

	public static final String BEFORE_DELETE = "before_Delete_Feature";
	public static final String AFTER_DELETE = "after_Delete_Feature";

	public static final String BEFORE_INSERT = "before_Insert_Feature";
	public static final String AFTER_INSERT = "after_Insert_Feature";

	public static final String LOAD_FINISHED = "Load_Finished";
	public static final String TRANSFORM_CHANGE = "Transform_Change";

	public Feature getFeature();

	public DataSet getCollectionResult();

	public boolean loadSucefully();

	public Exception getExceptionLoading();

	public Command getCommand();

	public EditableFeatureType getFeatureType();
}
