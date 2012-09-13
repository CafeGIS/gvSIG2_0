/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2007 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.georeferencing.main;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.gvsig.georeferencing.ui.table.GCPTablePanel;
import org.gvsig.georeferencing.ui.zoom.layers.GPGraphic;
import org.gvsig.gui.beans.table.exceptions.NotInitializeException;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.dataset.serializer.RmfSerializerException;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;


/**
 * Operaciones de lectura y escritura en disco de puntos de control.
 * 
 * 26/12/2007
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GeoPointsPersistence {
	
	/**
	 * Salva a rmf la lista de puntos
	 * @param pointList
	 * @param parent
	 * @param dataset
	 */
	public void saveToRMF(ArrayList pointList, IRasterDataSource dataset) {
		if (!RasterToolsUtil.messageBoxYesOrNot("sobreescribir_puntos", null))
			return;

		GeoPointList geoPointList = new GeoPointList();
		for (int i = 0; i < pointList.size(); i++)
			geoPointList.add(((GPGraphic) pointList.get(i)).getGeoPoint());
		
		try {
			for (int i = 0; i < dataset.getDatasetCount(); i++)
				dataset.getDataset(i)[0].saveObjectToRmf(GeoPointList.class, geoPointList);
		} catch (RmfSerializerException e) {
			RasterToolsUtil.messageBoxError("error_salvando_rmf", null, e);
		}
	}
	
	/**
	 * Carga desde el rmf asociado los puntos de control guardados si los tiene
	 * @param pointList
	 * @param parent
	 * @param dataset
	 */
	public void loadFromRMF(IRasterDataSource dataset, LayersPointManager layerPointsManager, GCPTablePanel tablePanel) {
		try {
			if (!RasterToolsUtil.messageBoxYesOrNot("eliminar_puntos", null))
				return;

			// Eliminamos los puntos existentes
			layerPointsManager.removeAllPoints();
			tablePanel.removeAllPoints();

			// Cargamos los puntos leídos del RMF
			GeoPointList pointList = (GeoPointList) dataset.getDataset(0)[0].loadObjectFromRmf(GeoPointList.class, null);
			for (int i = 0; i < pointList.size(); i++) {
				GeoPoint point = pointList.get(i);
				long id = layerPointsManager.addPoint(point.mapPoint, point.pixelPoint);
				tablePanel.addRow(true, point.mapPoint, point.pixelPoint, 0, 0, 0, 0, id);
			}
			layerPointsManager.calcPointsNumeration(tablePanel);
			tablePanel.updateErrors();
		} catch (RmfSerializerException e) {
			RasterToolsUtil.messageBoxError("error_leyendo_rmf", null, e);
		} catch (NotInitializeException e) {
			RasterToolsUtil.messageBoxError("table_not_initialize", tablePanel);
		}
	}
	
	/**
	 * Función que se ejecuta al pulsar el botón de export a ascii
	 */
	public void exportToCSV(ArrayList pointList, boolean error) {
		for (int i = 0; i < pointList.size(); i++) {
			if(!(pointList.get(i) instanceof GPGraphic))
				return;
		}

		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(RasterToolsUtil.getText(this, "seleccionar_fichero"));

		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			String fName = chooser.getSelectedFile().toString();
			if(!fName.endsWith(".csv"))
				fName = fName + ".csv";
			saveCSVPointList(fName, pointList, error);
		}
	}

	/**
	 * Función que se ejecuta al pulsar el botón de importar desde CSV
	 */
	public void importFromCSV(LayersPointManager layerPointsManager, GCPTablePanel tablePanel) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(RasterToolsUtil.getText(this, "seleccionar_fichero"));
		ExtendedFileFilter fileFilter = null;

		fileFilter = new ExtendedFileFilter();
		fileFilter.addExtension("csv");
		fileFilter.setDescription("CSV File");
			
		chooser.addChoosableFileFilter(fileFilter);

		if (fileFilter != null)
			chooser.setFileFilter(fileFilter);

		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			if(!f.exists()) {
				RasterToolsUtil.messageBoxError("error_file_not_found", null);
				return;
			}
			if(!RasterToolsUtil.messageBoxYesOrNot("eliminar_puntos", null))
				return;
			loadCSVPointList(f.getAbsolutePath(), layerPointsManager, tablePanel);
			tablePanel.updateErrors();
		}
	}
	
	/**
	 * Crea el fichero Ascii con la lista de puntos y la salva a disco
	 * @param file
	 */
	public void saveCSVPointList(String file, ArrayList pointList, boolean error){
		try{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			if(error)
				out.write("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\",\"ErrX\",\"ErrY\",\"RMS\"\n");
			else
				out.write("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\"\n");

			for(int i = 0; i < pointList.size(); i++) {
				GeoPoint geoPoint =  (GeoPoint)((GPGraphic)pointList.get(i)).getGeoPoint();
				String point = i + "," +
								geoPoint.pixelPoint.getX() + "," +
								geoPoint.pixelPoint.getY() + "," +
								geoPoint.mapPoint.getX() + "," +
								geoPoint.mapPoint.getY();
				String errorTxt = "";
				try{
					errorTxt = geoPoint.getErrorX() + "," + geoPoint.getErrorY() + "," + geoPoint.getRms();
					if(error)
						out.write(point + "," + errorTxt + "\n");
					else
						out.write(point + "\n");
				}catch(ArrayIndexOutOfBoundsException ex){
					out.write(point + "\n");
				}
			}
			out.close();
		}catch(FileNotFoundException ex){
			//No salvamos el csv
		}catch(IOException ex){
			//No salvamos el csv
		}
	}
	
	/**
	 * Crea la lista de puntos a partir de un fichero CSV
	 * @param file
	 */
	private void loadCSVPointList(String file, LayersPointManager layerPointsManager, GCPTablePanel tablePanel){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = in.readLine();
			int nPoint = 0;
			while(line != null) {
				if(nPoint == 0) {
					if(!line.equals("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\",\"ErrX\",\"ErrY\",\"RMS\"") && 
							!line.equals("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\"") && 
							!line.equals("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\",\"Z\",\"ErrX\",\"ErrY\",\"RMS\"") &&
							!line.equals("\"Pt\",\"X\",\"Y\",\"X'\",\"Y'\",\"Z\"")) {
						RasterToolsUtil.messageBoxError("error_point_file", null);
						return;
					} else {
						layerPointsManager.removeAllPoints();
						tablePanel.removeAllPoints();
					}
				} else {
					double x = 0D, y = 0D, xx = 0D, yy = 0D;
					String[] tokens = line.split(",");
						for(int tok = 0; tok < tokens.length; tok++){
							try{
								if(tok == 1)
									x = Double.parseDouble(tokens[tok]);
								if(tok == 2)
									y = Double.parseDouble(tokens[tok]);
								if(tok == 3)
									xx = Double.parseDouble(tokens[tok]);
								if(tok == 4)
									yy = Double.parseDouble(tokens[tok]);
							}catch(NumberFormatException ex){
								break;
							}
						}
					
					if(x == 0 && y == 0 && xx == 0 && yy == 0){
						line = in.readLine();
						continue;
					}
	
						Point2D p = new Point2D.Double();
					p.setLocation(x, y);
					Point2D m = new Point2D.Double();
					m.setLocation(xx, yy);
					long id = layerPointsManager.addPoint(m, p);
					tablePanel.addRow(true, m, p, 0, 0, 0, 0, id);
				}
				nPoint++;
				line = in.readLine();
			}
			in.close();
			layerPointsManager.calcPointsNumeration(tablePanel);
		} catch(FileNotFoundException ex){
			//No salvamos el csv
		} catch(IOException ex){
			//No salvamos el csv
		} catch(NotInitializeException ex){
			RasterToolsUtil.messageBoxError("table_not_initialize", tablePanel);
		}
	}
}
