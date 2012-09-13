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
package org.gvsig.i18n.extension.preferences;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;

import org.gvsig.gui.beans.swing.JButton;
import org.gvsig.i18n.I18nException;
import org.gvsig.i18n.I18nManager;
import org.gvsig.i18n.Messages;
import org.gvsig.i18n.extension.preferences.table.LocaleTableModel;
import org.gvsig.i18n.extension.preferences.table.RadioButtonCellEditor;
import org.gvsig.i18n.extension.preferences.table.RadioButtonCellRenderer;
import org.gvsig.i18n.impl.I18nManagerImpl;

import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;

/**
 * Prefence page to manage gvSIG locales.
 * 
 * @author <a href="mailto:dcervera@disid.com">David Cervera</a>
 */
public class I18nPreferencePage extends AbstractPreferencePage implements
	ActionListener {

    private static final long serialVersionUID = 7164183052397200888L;

    private static final String COMMAND_UNINSTALL = "UNINSTALL";

    private static final String COMMAND_EXPORT = "EXPORT";

    private static final String COMMAND_EXPORT_NEW = "EXPORT_NEW";

    private static final String COMMAND_INSTALL = "INSTALL";

    private static final String EXPORT_JAR_FILE_EXTENSION = ".jar";

    private static final String EXPORT_ZIP_FILE_EXTENSION = ".zip";

    private ImageIcon icon;

    private I18nManager manager = I18nManagerImpl.getInstance();

    private JTable localesTable;

    private LocaleTableModel tableModel;

    private JFileChooser fileChooser;

    /**
     * Creates a new I18n preferences page.
     */
    public I18nPreferencePage() {
	setParentID("com.iver.core.preferences.general.GeneralPage");
	icon = new ImageIcon(this.getClass().getClassLoader().getResource(
		"images/babel.png"));

	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	add(getLocalesPanel());
	add(Box.createRigidArea(new Dimension(0, 5)));
	add(getActiveLocaleLabel());
	add(Box.createRigidArea(new Dimension(0, 5)));
	add(getButtonsPanel());
	add(Box.createRigidArea(new Dimension(0, 5)));
	add(getCollaborationLabel());
    }

    public String getID() {
	return getClass().getName();
    }

    public String getTitle() {
	return Messages.getText("idioma");
    }

    public ImageIcon getIcon() {
	return icon;
    }

    public JPanel getPanel() {
	return this;
    }

    public void setChangesApplied() {
	tableModel.setChangesApplied();
    }

    public boolean isValueChanged() {
	return tableModel.isValueChanged();
    }

    public void storeValues() throws StoreException {
	tableModel.saveSelectedLocale();
    }

    public void initializeDefaults() {
	tableModel.selectDefaultLocale();
    }

    public void initializeValues() {
	tableModel.selectPreviousLocale();
    }

    public void actionPerformed(ActionEvent event) {
	if (COMMAND_INSTALL.equals(event.getActionCommand())) {
	    installLocale();
	}
	else if (COMMAND_EXPORT.equals(event.getActionCommand())) {
	    exportLocaleForUpdate();
	}
	else if (COMMAND_EXPORT_NEW.equals(event.getActionCommand())) {
	    exportLocaleForTranslation();
	}
	else if (COMMAND_UNINSTALL.equals(event.getActionCommand())) {
	    uninstallSelectedLocale();
	}
    }

    /**
     * Installs a new Locale translation or updates an already existing one.
     */
    private void installLocale() {
	JFileChooser fileChooser = getJarFileChooser();

	int returnVal = fileChooser.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File importFile = fileChooser.getSelectedFile();
	    try {
		Locale[] installedLocales = manager.installLocales(importFile);
		if (installedLocales == null || installedLocales.length == 0) {
		    JOptionPane
			    .showMessageDialog(
				    this,
				    Messages
					    .getText("I18nPreferencePage.idiomas_no_encontrados_para_instalar"),
				    Messages
					    .getText("I18nPreferencePage.error_instalar_idiomas"),
				    JOptionPane.ERROR_MESSAGE);
		}
		else {
		    StringBuffer msg = new StringBuffer(Messages
			    .getText("I18nPreferencePage.idiomas_instalados"));

		    for (int i = 0; i < installedLocales.length; i++) {
			msg.append(manager.getDisplayName(installedLocales[i]));
			if (i < installedLocales.length - 1) {
			    msg.append(", ");
			}
		    }
		    tableModel.reloadLocales();
		    JOptionPane
			    .showMessageDialog(
				    this,
				    msg.toString(),
				    Messages
					    .getText("I18nPreferencePage.instalar_idiomas"),
				    JOptionPane.INFORMATION_MESSAGE);
		}
	    } catch (I18nException ex) {
		ex.showError();
	    }
	}
    }

    /**
     * Updates the translation of a locale
     */
    private void exportLocaleForUpdate() {
	Locale[] locales = getSelectedLocales();

	if (locales == null) {
	    JOptionPane.showMessageDialog(this, Messages
		    .getText("I18nPreferencePage.seleccione_idioma"), Messages
		    .getText("I18nPreferencePage.error_actualizar_idioma"),
		    JOptionPane.ERROR_MESSAGE);
	}
	else {
	    // Select the reference language
	    LocaleItem[] items = getLocaleItemsForSelection(manager
		    .getInstalledLocales(), locales);
	    LocaleItem selected = null;
	    if (items != null && items.length > 0) {
		// Select by default the current locale, or the first one
		// if the current locale is one of the ones to be updated
		for (int i = 0; i < locales.length && selected == null; i++) {
		    if (locales[i].equals(manager.getCurrentLocale())) {
			selected = items[0];
		    }
		}
		if (selected == null) {
		    selected = new LocaleItem(manager.getCurrentLocale(),
			    manager);
		}
		selected = (LocaleItem) JOptionPane
			.showInputDialog(
				this,
				Messages
					.getText("I18nPreferencePage.seleccione_idioma_referencia"),
				Messages
					.getText("I18nPreferencePage.exportar_idioma"),
				JOptionPane.QUESTION_MESSAGE, null, items,
				selected);

		if (selected == null) {
		    return;
		}
	    }
	    // Select the file to export to
	    JFileChooser fileChooser = getJarFileChooser();
	    fileChooser.setSelectedFile(new File(
		    getLocaleJarFileName(locales[0])));

	    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
		File saveFile = fileChooser.getSelectedFile();
		try {
		    Locale fileNameLocale = selected == null ? manager
			    .getCurrentLocale() : selected.getLocale();
		    manager.exportLocalesForUpdate(locales, fileNameLocale,
			    saveFile);
		    
		    JOptionPane
			    .showMessageDialog(
				    this,
				    Messages
					    .getText("I18nPreferencePage.exportado_textos_idioma")
					    + " "
					    + saveFile,
				    Messages
					    .getText("I18nPreferencePage.exportar_idioma"),
				    JOptionPane.INFORMATION_MESSAGE);
		} catch (I18nException ex) {
		    ex.showError();
		}
	    }
	}
    }

    /**
     * Prepares a locale for translation.
     */
    private void exportLocaleForTranslation() {
	// Get the selected locale as the ones for reference
	Locale[] refLocales = getSelectedLocales();

	if (refLocales == null) {
	    JOptionPane
		    .showMessageDialog(
			    this,
			    Messages
				    .getText("I18nPreferencePage.seleccione_idioma_actualizar"),
			    Messages
				    .getText("I18nPreferencePage.error_actualizar_idioma"),
			    JOptionPane.ERROR_MESSAGE);
	}
	else {

	    // Select the locale to translate
	    LocaleItem[] items = getLocaleItemsForSelection(Locale
		    .getAvailableLocales(), manager.getInstalledLocales());
	    LocaleItem selected = (LocaleItem) JOptionPane
		    .showInputDialog(
			    this,
			    Messages
				    .getText("I18nPreferencePage.seleccione_idioma_traducir"),
			    Messages
				    .getText("I18nPreferencePage.exportar_idioma"),
			    JOptionPane.QUESTION_MESSAGE, null, items, items[0]);

	    if (selected == null) {
		return;
	    }

	    // Select the file to export to
	    JFileChooser fileChooser = getJarFileChooser();
	    fileChooser.setSelectedFile(new File(getLocaleJarFileName(selected
		    .getLocale())));

	    if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
		File saveFile = fileChooser.getSelectedFile();
		try {
		    manager.exportLocaleForTranslation(selected.getLocale(),
			    refLocales, saveFile);

		    JOptionPane
			    .showMessageDialog(
				    this,
				    Messages
					    .getText("I18nPreferencePage.idioma_preparado_traducir")
					    + manager.getDisplayName(selected
						    .getLocale())
					    + Messages
						    .getText("I18nPreferencePage.en_archivo")
					    + saveFile,
				    Messages
					    .getText("I18nPreferencePage.exportar_idioma"),
				    JOptionPane.INFORMATION_MESSAGE);
		} catch (I18nException ex) {
		    ex.showError();
		}
	    }
	}

    }

    private LocaleItem[] getLocaleItemsForSelection(Locale[] locales,
	    Locale[] exceptions) {
	List items = new ArrayList();
	Set exceptionsSet = new HashSet(exceptions.length);
	for (int i = 0; i < exceptions.length; i++) {
	    exceptionsSet.add(exceptions[i]);
	}

	int j = 0;
	for (int i = 0; i < locales.length; i++) {
	    // Only add locales not included in the exceptions list
	    if (!exceptionsSet.contains(locales[i])) {
		items.add(new LocaleItem(locales[i], manager));
		j++;
	    }
	}
	return (LocaleItem[]) items.toArray(new LocaleItem[items.size()]);
    }

    /**
     * Returns a name for the jar file to export a locale.
     */
    private String getLocaleJarFileName(Locale locale) {
	return manager.getDisplayName(locale, I18nManager.ENGLISH).replace(' ',
		'_').concat(EXPORT_ZIP_FILE_EXTENSION);
    }

    /**
     * Creates a new JFileChooser to import or export a locale jar file.
     */
    private JFileChooser getJarFileChooser() {
	if (fileChooser == null) {
	    fileChooser = new JFileChooser();
	    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fileChooser.setMultiSelectionEnabled(false);
	    fileChooser.setFileFilter(new FileFilter() {

		public boolean accept(File file) {
		    return (file.isDirectory()
			    || file.getName().endsWith(
				    EXPORT_JAR_FILE_EXTENSION) || file
			    .getName().endsWith(EXPORT_ZIP_FILE_EXTENSION));
		}

		public String getDescription() {
		    return Messages.getText("I18nPreferencePage.archivos_jar");
		}

	    });
	}
	return fileChooser;
    }

    /**
     * Removes some installed locales from gvSIG.
     */
    private void uninstallSelectedLocale() {
	Locale[] locales = getSelectedLocales();

	if (locales == null) {
	    JOptionPane
		    .showMessageDialog(
			    this,
			    Messages
				    .getText("I18nPreferencePage.seleccione_idioma_desinstalar"),
			    Messages
				    .getText("I18nPreferencePage.error_desinstalar_idioma"),
			    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	
	for (int i = 0; i < locales.length; i++) {

	    if (locales[i].equals(manager.getCurrentLocale())) {
		JOptionPane
			.showMessageDialog(
				this,
				Messages
					.getText("I18nPreferencePage.idioma_actual_no_puede_desinstalar"),
				Messages
					.getText("I18nPreferencePage.error_desinstalar_idioma"),
				JOptionPane.ERROR_MESSAGE);
	    } else {
		int option = JOptionPane
			.showConfirmDialog(
				this,
				Messages
					.getText("I18nPreferencePage.seguro_desea_desinstalar_idioma")
					+ " "
					+ manager.getDisplayName(locales[i])
					+ "?",
				Messages
					.getText("I18nPreferencePage.confirmar_desinstalar_idioma"),
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
		    try {
			tableModel.removeLocale(locales[i]);
		    } catch (I18nException ex) {
			ex.showError();
		    }
		}
	    }
	}
    }

    /**
     * Returns the Locales selected in the table of available locales.
     */
    private Locale[] getSelectedLocales() {
	int[] rowIndexes = localesTable.getSelectedRows();
	if (rowIndexes != null && rowIndexes.length > 0) {
	    Locale[] locales = new Locale[rowIndexes.length];
	    for (int i = 0; i < locales.length; i++) {
		locales[i] = tableModel.getLocale(rowIndexes[i]);
	    }
	    return locales;
	}
	else {
	    return null;
	}
    }

    /**
     * Creates the Panel with the table of Locales.
     */
    private Component getLocalesPanel() {
	tableModel = new LocaleTableModel(manager);
	localesTable = new JTable(tableModel);

	TableColumn activeColumn = localesTable.getColumnModel().getColumn(
		LocaleTableModel.COLUMN_ACTIVE);
	activeColumn.setCellEditor(new RadioButtonCellEditor());
	activeColumn.setCellRenderer(new RadioButtonCellRenderer());

	localesTable
		.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	localesTable.getSelectionModel().setSelectionInterval(0, 0);
	JScrollPane scrollPane = new JScrollPane(localesTable);

	// Container panel
	JPanel localesPanel = new JPanel();
	localesPanel.setLayout(new BoxLayout(localesPanel, BoxLayout.Y_AXIS));
	localesPanel.add(scrollPane);
	localesPanel.setAlignmentX(CENTER_ALIGNMENT);
	localesPanel.setPreferredSize(new Dimension(236, 230));
	localesPanel.setMaximumSize(new Dimension(500, 230));

	return localesPanel;
    }

    /**
     * Creates the panel with the buttons to perform the related actions.
     */
    private Component getButtonsPanel() {
	JPanel buttonPanel = new JPanel(new GridBagLayout());
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.fill = GridBagConstraints.NONE;
	constraints.anchor = GridBagConstraints.LINE_START;
	Insets btInsets = new Insets(2, 0, 2, 4);
	Insets lbInsets = new Insets(2, 2, 2, 0);

	/* ROW 0 */
	constraints.gridy = 0;

	constraints.gridx = 0;
	constraints.insets = btInsets;
	JButton newLocaleButton = new JButton(Messages
		.getText("I18nPreferencePage.Instalar"));
	newLocaleButton.setActionCommand(COMMAND_INSTALL);
	newLocaleButton.addActionListener(this);
	newLocaleButton.setToolTipText(Messages
		.getText("I18nPreferencePage.Instalar_idioma_tooltip"));
	buttonPanel.add(newLocaleButton, constraints);

	constraints.gridx = 1;
	constraints.insets = lbInsets;
	buttonPanel.add(new JLabel(Messages
		.getText("I18nPreferencePage.Instalar_idioma_tooltip")),
		constraints);

	/* ROW 1 */
	constraints.gridy = 1;

	constraints.gridx = 0;
	constraints.insets = btInsets;
	JButton removeLocaleButton = new JButton(Messages
		.getText("I18nPreferencePage.Desinstalar"));
	removeLocaleButton.setActionCommand(COMMAND_UNINSTALL);
	removeLocaleButton.addActionListener(this);
	removeLocaleButton.setToolTipText(Messages
		.getText("I18nPreferencePage.Desinstalar_idioma_tooltip"));
	buttonPanel.add(removeLocaleButton, constraints);

	constraints.gridx = 1;
	constraints.insets = lbInsets;
	buttonPanel.add(new JLabel(Messages
		.getText("I18nPreferencePage.Desinstalar_idioma_tooltip")),
		constraints);

	/* ROW 2 */
	constraints.gridy = 2;

	constraints.gridx = 0;
	constraints.insets = btInsets;
	JButton exportLocaleButton = new JButton(Messages
		.getText("I18nPreferencePage.exportar_actualizar"));
	exportLocaleButton.setActionCommand(COMMAND_EXPORT);
	exportLocaleButton.addActionListener(this);
	exportLocaleButton.setToolTipText(Messages
		.getText("I18nPreferencePage.exportar_actualizar_tooltip"));
	buttonPanel.add(exportLocaleButton, constraints);

	constraints.gridx = 1;
	constraints.insets = lbInsets;
	buttonPanel.add(new JLabel(Messages
		.getText("I18nPreferencePage.exportar_actualizar_tooltip")),
		constraints);

	/* ROW 3 */
	constraints.gridy = 3;

	constraints.gridx = 0;
	constraints.insets = btInsets;
	JButton exportNewLocaleButton = new JButton(Messages
		.getText("I18nPreferencePage.exportar_traducir"));
	exportNewLocaleButton.setActionCommand(COMMAND_EXPORT_NEW);
	exportNewLocaleButton.addActionListener(this);
	exportNewLocaleButton.setToolTipText(Messages
		.getText("I18nPreferencePage.exportar_traducir_tooltip"));
	buttonPanel.add(exportNewLocaleButton, constraints);

	constraints.gridx = 1;
	constraints.insets = lbInsets;
	buttonPanel.add(new JLabel(Messages
		.getText("I18nPreferencePage.exportar_traducir_tooltip")),
		constraints);

	buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
	return buttonPanel;
    }

    /**
     * Creates the JLabel to show information about the preference page.
     */
    private Component getActiveLocaleLabel() {
	JTextArea textArea = new JTextArea(Messages
		.getText("I18nPreferencePage.ayuda"));
	textArea.setEditable(false);
	textArea.setAutoscrolls(true);
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
	return textArea;
    }

    /**
     * Creates the JLabel to show information about gvSIG translation
     * collaboration.
     */
    private Component getCollaborationLabel() {
	JTextArea textArea = new JTextArea(Messages
		.getText("I18nPreferencePage.colaboracion"));
	textArea.setEditable(false);
	textArea.setAutoscrolls(true);
	textArea.setLineWrap(true);
	textArea.setWrapStyleWord(true);
	return textArea;
    }

    private class LocaleItem {
	private final Locale locale;
	private final I18nManager manager;

	public LocaleItem(Locale locale, I18nManager manager) {
	    this.locale = locale;
	    this.manager = manager;
	}

	public Locale getLocale() {
	    return locale;
	}

	public String toString() {
	    return manager.getDisplayName(locale);
	}

	public boolean equals(Object obj) {
	    if (obj == null || !(obj instanceof LocaleItem)) {
		return false;
	    }

	    LocaleItem item = (LocaleItem) obj;
	    return locale.equals(item.getLocale());
	}
    }
}