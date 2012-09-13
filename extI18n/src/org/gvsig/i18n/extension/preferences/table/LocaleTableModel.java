/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Gobernment (CIT)
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
 * 2008 {DiSiD Technologies}  {New extension for installation and update of text translations}
 */
package org.gvsig.i18n.extension.preferences.table;

import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import org.gvsig.i18n.*;

/**
 * TableModel to show the list of available Locales in gvSIG.
 *
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class LocaleTableModel extends AbstractTableModel {

    // Start from 0 to enable de locale column again
    public static final int COLUMN_LOCALE = -1;

    public static final int COLUMN_LANGUAGE = 0;

    public static final int COLUMN_COUNTRY = 1;

    public static final int COLUMN_VARIANT = 2;

    public static final int COLUMN_ACTIVE = 3;

    private static final long serialVersionUID = -3855372372788577788L;

    private final I18nManager manager;

    private Locale[] locales;

    private int currentLocaleIndex;

    private int originalLocaleIndex;

    /**
     * Creates a LocaleTableModel with a I18nManager to handle locales.
     */
    public LocaleTableModel(I18nManager manager) {
	this.manager = manager;
	loadLocales();
    }

    public int getColumnCount() {
	return 4;
    }

    public int getRowCount() {
	return locales.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
	switch (columnIndex) {
	case COLUMN_LOCALE:
	    return locales[rowIndex];
	case COLUMN_LANGUAGE:
	    return manager.getLanguageDisplayName(locales[rowIndex]);
	case COLUMN_COUNTRY:
	    return locales[rowIndex].getDisplayCountry();
	case COLUMN_VARIANT:
	    return locales[rowIndex].getDisplayVariant();
	case COLUMN_ACTIVE:
	    return rowIndex == currentLocaleIndex ? Boolean.TRUE
		    : Boolean.FALSE;
	}
	return null;
    }

    public String getColumnName(int columnIndex) {
	switch (columnIndex) {
	case COLUMN_LOCALE:
	    return Messages.getText("I18nPreferencePage.Locale_code");
	case COLUMN_LANGUAGE:
	    return Messages.getText("I18nPreferencePage.Idioma");
	case COLUMN_COUNTRY:
	    return Messages.getText("I18nPreferencePage.Pais");
	case COLUMN_VARIANT:
	    return Messages.getText("I18nPreferencePage.Variante");
	case COLUMN_ACTIVE:
	    return Messages.getText("I18nPreferencePage.Activar");
	}
	return "";
    }

    public Class getColumnClass(int columnIndex) {
	switch (columnIndex) {
	case COLUMN_LOCALE:
	case COLUMN_LANGUAGE:
	case COLUMN_COUNTRY:
	case COLUMN_VARIANT:
	    return String.class;
	case COLUMN_ACTIVE:
	    return Boolean.class;
	}
	return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return columnIndex == 3;
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
	// Only edit current locale value
	if (columnIndex == COLUMN_ACTIVE && Boolean.TRUE.equals(value)) {
	    setSelectedLocale(rowIndex, columnIndex);
	}
    }

    /**
     * Returns the {@link Locale} shown in a table row.
     *
     * @param rowIndex
     *            the table row index
     * @return the {@link Locale}
     */
    public Locale getLocale(int rowIndex) {
    	return rowIndex >= 0 ? locales[rowIndex] : null;
    }

    /**
     * Removes and uninstalls a locale.
     *
     * @param locale
     *            to remove
     * @throws I18nException
     *             if there is an error removing a Locale
     */
    public void removeLocale(Locale locale) throws I18nException {
	int rowIndex = getLocaleRowIndex(locale);
	manager.uninstallLocale(locale);
	loadLocales();
	fireTableRowsDeleted(rowIndex, rowIndex);
    }

    /**
     * Reloads the list of installed locales.
     */
    public void reloadLocales() {
	loadLocales();
	fireTableDataChanged();
    }

    public void saveSelectedLocale() {
	manager.setCurrentLocale(getSelectedLocale());
	originalLocaleIndex = currentLocaleIndex;
    }

    public void selectPreviousLocale() {
	setSelectedLocale(originalLocaleIndex, COLUMN_ACTIVE);
    }

    /**
     *
     */
    public void selectDefaultLocale() {
	Locale defLocale = manager.getDefaultSystemLocale();
	int pos = getLocaleRowIndex(defLocale);
	// Maybe the locale has country and/or variant and is not registered
	// (ex: en_US). Look for the locale with only the language (ex: en).
	if (pos == -1) {
	    defLocale = new Locale(defLocale.getLanguage());
	    pos = getLocaleRowIndex(defLocale);
	}
	// If the locale could'nt be found, use English as default
	if (pos == -1) {
	    pos = getLocaleRowIndex(I18nManager.ENGLISH);
	}
	setSelectedLocale(pos, COLUMN_ACTIVE);
    }

    public boolean isValueChanged() {
	return currentLocaleIndex != originalLocaleIndex;
    }

    public void setChangesApplied() {
	originalLocaleIndex = currentLocaleIndex;
    }

    /**
     * Sets the current selected locale.
     */
    public void setSelectedLocale(Locale locale) {
	int localeIndex = getLocaleRowIndex(locale);
	setSelectedLocale(localeIndex);
    }

    /**
     * Returns the current selected locale.
     */
    public Locale getSelectedLocale() {
	Locale selected = getLocale(currentLocaleIndex);
	return selected == null ? manager.getCurrentLocale() : selected;
    }

    private void setSelectedLocale(int rowIndex) {
	setSelectedLocale(rowIndex, 0);
    }

    private void setSelectedLocale(int rowIndex, int columnIndex) {
	int oldCurrentLocaleIndex = currentLocaleIndex;
	currentLocaleIndex = rowIndex;
	fireTableCellUpdated(oldCurrentLocaleIndex, columnIndex);
	fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Loads the locales from the I18nManager.
     */
    private void loadLocales() {
	locales = manager.getInstalledLocales();
	Locale currentLocale = manager.getCurrentLocale();
	currentLocaleIndex = getLocaleRowIndex(currentLocale);
	originalLocaleIndex = currentLocaleIndex;
    }

    /**
     * Returns the table row position for a Locale.
     */
    private int getLocaleRowIndex(Locale locale) {
	for (int i = 0; i < locales.length; i++) {
	    if (locale.equals(locales[i])) {
		return i;
	    }
	}
	return -1;
    }
}