/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
*/


package org.gvsig.project.document.table;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.ValidateDataParametersException;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.project.document.table.gui.CSVSeparatorOptionsPanel;
import org.gvsig.project.document.table.gui.Statistics.MyObjectStatistics;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;

/**
 * Class to create dbf and csv files at disk with the statistics group generated from a table.
 *
 * dbf -> Data Base File
 * csv -> Comma Separated Value
 *
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 *
 */


public class ExportStatisticsFile {
    private static final Logger logger = LoggerFactory
            .getLogger(ExportStatisticsFile.class);

	private String lastPath = null;
	private Hashtable<String, MyFileFilter> dbfExtensionsSupported; // Supported extensions.
	private Hashtable<String, MyFileFilter> csvExtensionsSupported;

	public ExportStatisticsFile(List<MyObjectStatistics> valores) {

        	JFileChooser jfc = new JFileChooser(lastPath);
			jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());

			// Adding required extensions (dbf, csv)
			dbfExtensionsSupported = new Hashtable<String, MyFileFilter>();
			csvExtensionsSupported = new Hashtable<String, MyFileFilter>();
			dbfExtensionsSupported.put("dbf", new MyFileFilter("dbf",PluginServices.getText(this, "Ficheros_dbf"), "dbf"));
			csvExtensionsSupported.put("csv", new MyFileFilter("csv",PluginServices.getText(this, "Ficheros_csv"), "csv"));

			Iterator<MyFileFilter> iter = csvExtensionsSupported.values().iterator();
			while (iter.hasNext()) {
				jfc.addChoosableFileFilter(iter.next());
			}

			iter = dbfExtensionsSupported.values().iterator();
			while (iter.hasNext()) {
				jfc.addChoosableFileFilter(iter.next());
			}

			// Opening a JFileCooser
			if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
					File endFile = jfc.getSelectedFile();
						if (endFile.exists()){// File exists in the directory.
							int resp = JOptionPane.showConfirmDialog(
									(Component) PluginServices.getMainFrame(),
									PluginServices.getText(this,
											"fichero_ya_existe_seguro_desea_guardarlo")+"\n"+endFile.getAbsolutePath(),
									PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);// Informing the user
							if (resp != JOptionPane.YES_OPTION) {//cancel pressed.
								return;
							}
						}//end if exits.
						MyFileFilter filter = (MyFileFilter)jfc.getFileFilter();// dbf, csv
						endFile = filter.normalizeExtension(endFile);//"name" + "." + "dbf", "name" + "." + "csv"

						if(filter.getExtensionOfAFile(endFile).toLowerCase().compareTo("csv") == 0) { // csv file
							exportToCSVFile(valores,endFile); // export to csv format
						}
						else if(filter.getExtensionOfAFile(endFile).toLowerCase().compareTo("dbf") == 0) {// dbf file
							try {
								exportToDBFFile(valores,endFile);
							} catch (Exception e) {
								NotificationManager.addError(e);
							} // export to dbf format
						}
			}//end if aprove option.
	}

	/**
	 * Creating  cvs format file with the statistics.
	 * Option to select the two columns separator.
	 *
	 * Example with semicolon: Name;data\n
	 * 					   Name2;data2\n
	 *
	 * @param valores
	 * 			- Pairs: String name (key) + Double value (
	 * @param endFile
	 * 			- File to write the information
	 */

	private void exportToCSVFile(List<MyObjectStatistics> valores, File endFile) {

		try {
			CSVSeparatorOptionsPanel csvSeparatorOptions = new CSVSeparatorOptionsPanel();
			PluginServices.getMDIManager().addWindow(csvSeparatorOptions);

			String separator = csvSeparatorOptions.getSeparator();

			if(separator != null) {

				FileWriter fileCSV = new FileWriter(endFile);

				fileCSV.write(PluginServices.getText(this, "Nombre") + separator + PluginServices.getText(this, "Valor")+ "\n");

				Iterator<MyObjectStatistics> iterador = valores.listIterator();

				while (iterador.hasNext()) {// Writing value,value\n
					 MyObjectStatistics data= iterador.next();
					 fileCSV.write(data.getKey() + separator + (data.getValue())+ "\n");
				}
				fileCSV.close();
				JOptionPane.showMessageDialog(null, PluginServices.getText(this, "fichero_creado_en") + " " + endFile.getAbsolutePath(),
						PluginServices.getText(this, "fichero_creado_en_formato")+ " csv "+
						PluginServices.getText(this, "mediante_el_separador")+
						" \""+ separator + "\"", JOptionPane.INFORMATION_MESSAGE);// Informing the user
			} else {
				return;
			}

		} catch (IOException e) {// Informing the user
			logger.error("Error exportando a formato csv");
			JOptionPane.showMessageDialog(null, PluginServices.getText(this, "Error_exportando_las_estadisticas") + " " + endFile.getAbsolutePath(),
					PluginServices.getText(this, "Error"), JOptionPane.ERROR_MESSAGE);
		}

	}

	public void exportToDBFFile(List<MyObjectStatistics> valores, File endFile)
			throws DataException, ValidateDataParametersException {
		DataManager datamanager=DALLocator.getDataManager();
		FilesystemServerExplorerParameters explorerParams=(FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);
		explorerParams.setRoot(endFile.getParent());
		FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);
		NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(endFile);
		try {
			EditableFeatureType type = newParams.getDefaultFeatureType()
					.getEditable();
			EditableFeatureAttributeDescriptor efad1 = type.add(PluginServices.getText(this, "Nombre"), DataTypes.STRING, 50);
			EditableFeatureAttributeDescriptor efad2 = type.add(PluginServices.getText(this, "Valor"), DataTypes.DOUBLE, 100);
			efad2.setPrecision(25);
			newParams.setDefaultFeatureType(type);
			explorer.add(newParams, true);
			DataManager manager = DALLocator.getDataManager();
			FeatureStore target = (FeatureStore) manager
			.createStore(newParams);
			FeatureType targetType = target.getDefaultFeatureType();
			target.edit(FeatureStore.MODE_APPEND);
			Iterator<MyObjectStatistics> iterador = valores.listIterator();
			while (iterador.hasNext()) {
				MyObjectStatistics data= iterador.next();
				EditableFeature ef=target.createNewFeature().getEditable();//new DefaultFeature(target).getEditable();//EditableFeature();
				ef.set(PluginServices.getText(this, "Nombre"), data.getKey());
				ef.set(PluginServices.getText(this, "Valor"), data.getValue());
				target.insert(ef);
			}
			target.finishEditing();
			target.dispose();
			JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(), PluginServices.getText(this, "fichero_creado_en") + " " + endFile.getAbsolutePath(),
					PluginServices.getText(this, "fichero_creado_en_formato")+ " dbf", JOptionPane.INFORMATION_MESSAGE);// Informing the user
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 *
 * Class to work with the file extensions.
 */
class MyFileFilter extends FileFilter {

	private String[] extensiones = new String[1];
	private String description;
	private boolean dirs = true;
	private String info = null;

	public MyFileFilter(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}

	public MyFileFilter(String[] ext, String desc, String info) {
		extensiones = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	public MyFileFilter(String ext, String desc, String info) {
		extensiones[0] = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	public MyFileFilter(String ext, String desc, boolean dirs, String info) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
		this.info = info;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		for (int i = 0; i < extensiones.length; i++) {
			if (extensiones[i].equals("")) {
				continue;
			}
			if (getExtensionOfAFile(f).equalsIgnoreCase(extensiones[i])) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensiones;
	}

	public boolean isDirectory() {
		return dirs;
	}

	public String getExtensionOfAFile(File file) {
		String name;
		int dotPos;
		name = file.getName();
		dotPos = name.lastIndexOf(".");
		if (dotPos < 1) {
			return "";
		}
		return name.substring(dotPos + 1);
	}

	public File normalizeExtension(File file) {
		String ext = getExtensionOfAFile(file);
		if (ext.equals("") || !(this.accept(file))) {
			return new File(file.getAbsolutePath() + "." + extensiones[0]);
		}
		return file;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}