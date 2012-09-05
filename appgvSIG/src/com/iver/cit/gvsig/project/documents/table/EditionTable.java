package com.iver.cit.gvsig.project.documents.table;

import java.io.IOException;

import org.gvsig.fmap.dal.exception.ReadException;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.opengis.feature.FeatureCollection;

import com.iver.cit.gvsig.project.documents.table.exceptions.CancelEditingTableException;
import com.iver.cit.gvsig.project.documents.table.exceptions.StartEditingTableException;
import com.iver.cit.gvsig.project.documents.table.exceptions.StopEditingTableException;


/**
 * DOCUMENT ME!
 *
 * @author Vicente Caballero Navarro
 */
public interface EditionTable {
    /**
     * DOCUMENT ME!
     * @throws StartEditingTableException
     * @throws EditionException
     */
    public void startEditing() throws StartEditingTableException;

    /**
     * DOCUMENT ME!
     */
    public void stopEditing() throws StopEditingTableException;

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void hideColumns(int[] index);

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setUneditableColumns(int[] index);

    /**
     * DOCUMENT ME!
     *
     * @param numColumns DOCUMENT ME!
     * @param values DOCUMENT ME!
     */
    public void setDefaultValues(int[] numColumns, Object[] values);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getDefaultValue();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    // public int[] getSelectedColumns();

    /**
     * DOCUMENT ME!
     * @throws IOException
     * @throws CancelEditingTableException
     */
    public void cancelEditing() throws CancelEditingTableException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditing();

    /**
     * DOCUMENT ME!
     */
    public void refresh();
    public void addFeatures(FeatureCollection collection) throws ReadException;
    public void copyFeature() throws ReadException;
    public void cutFeature() throws ReadException;
    public void removeFeature() throws ReadException;
    public void addColumn(FeatureAttributeDescriptor descriptor);
    public void removeColumn();
    public boolean isCopied();
    public void pasteFeature() throws ReadException;

}
