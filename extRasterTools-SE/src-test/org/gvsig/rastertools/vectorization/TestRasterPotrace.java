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
package org.gvsig.rastertools.vectorization;

import java.io.File;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.fmap.dal.DALLocator;
import org.gvsig.fmap.dal.DataManager;
import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.feature.EditableFeature;
import org.gvsig.fmap.dal.feature.EditableFeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.EditableFeatureType;
import org.gvsig.fmap.dal.feature.FeatureStore;
import org.gvsig.fmap.dal.feature.NewFeatureStoreParameters;
import org.gvsig.fmap.dal.feature.impl.DefaultEditableFeatureType;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorer;
import org.gvsig.fmap.dal.serverexplorer.filesystem.FilesystemServerExplorerParameters;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.geom.GeometryLocator;
import org.gvsig.fmap.geom.GeometryManager;
import org.gvsig.fmap.geom.Geometry.SUBTYPES;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;
import org.gvsig.raster.vectorization.VectorizationBinding;
import org.gvsig.rastertools.vectorizacion.fmap.BezierPathX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
*
* @version 05/08/2008
* @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
*/
public class TestRasterPotrace {
	private static final GeometryManager	geomManager	= GeometryLocator.getGeometryManager();
	private static final Logger 			logger 		= LoggerFactory.getLogger(TestRasterPotrace.class);
	private String        					baseDir     = "./test-images/";
	private String        					fileName    = "wheel";
//	private String        					fileName    = "letters";
//	private String        					fileName    = "potrace";

	private String        					fileIn      = baseDir + fileName + ".bmp";
//	private String       					fileIn      = baseDir + "NewLayer_6.tif";

	private String        					fileOutBase = "/tmp/" + fileName;

	private Object        					values[];
	private FeatureStore  					fsWriter;
	private int          					m_iGeometry = 0;

	public void generatePotrace(String fileIn, String fileOutBase, int trozos) {
		NumberFormat formatter = new DecimalFormat("000");
		String s = formatter.format(trozos);

		String fileOutShape = fileOutBase + s + ".shp";
		String fieldName = fileName + s;
		try {
			VectorizationBinding binding = new VectorizationBinding(fileIn);
//			binding.setCornerThreshold(-1);
			double geometrias[] = binding.VectorizeBuffer();

			EditableFeatureType featureType = getFeatureType(fieldName);

			EditableFeatureAttributeDescriptor efad=featureType.add("GEOMETRY", DataTypes.GEOMETRY);
			efad.setGeometryType(Geometry.TYPES.CURVE);
			efad.setSRS(CRSFactory.getCRS("EPSG:23030"));
			featureType.setDefaultGeometryAttributeName("GEOMETRY");


			DataManager datamanager=DALLocator.getDataManager();
			FilesystemServerExplorerParameters explorerParams;

			explorerParams = (FilesystemServerExplorerParameters) datamanager.createServerExplorerParameters(FilesystemServerExplorer.NAME);

			explorerParams.setRoot(new File(fileName).getParent());
			FilesystemServerExplorer explorer=(FilesystemServerExplorer) datamanager.createServerExplorer(explorerParams);
			NewFeatureStoreParameters newParams = (NewFeatureStoreParameters) explorer.getAddParameters(new File(fileName));

			newParams.setDefaultFeatureType(featureType);
			explorer.add(newParams, true);
			DataManager manager = DALLocator.getDataManager();
			fsWriter=(FeatureStore)manager.createStore(newParams);

			if(fileOutShape.endsWith(".dxf")) {
			} else if(fileOutShape.endsWith(".shp")) {
			}
			fsWriter.edit(FeatureStore.MODE_APPEND);
			values = new Object[2];
			values[0] = new Double(0);
			values[1] = new Double(0);

			showPotrace(geometrias, trozos, fieldName);

			fsWriter.finishEditing();
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TestRasterPotrace() {
		for (int i = 2; i <= 15; i++)
			generatePotrace(fileIn, fileOutBase, i);
		generatePotrace(fileIn, fileOutBase, 100);
	}


	public void addGeometry(Geometry geom, Object[] value, String fieldName) throws Exception {
		m_iGeometry++;
		EditableFeature ef=fsWriter.createNewFeature();
		ef.set("ID", value[0]);
		ef.set(fieldName,value[1]);
		ef.setGeometry("GEOMETRY", geom);
		fsWriter.insert(ef);
	}

	private void showPotrace(double[] potraceX, int trozos, String fieldName) throws Exception {
		BezierPathX pathX = new BezierPathX(trozos);

		int cont = 1;
		while (true) {
			if ((cont >= potraceX.length) || (cont >= potraceX[0]))
				return;
			switch ((int) potraceX[cont]) {
				case 0: // MoveTo
					pathX.moveTo(potraceX[cont + 1], potraceX[cont + 2]);
					cont += 3;
					break;
				case 1: // LineTo
					pathX.lineTo(potraceX[cont + 1], potraceX[cont + 2]);
					cont += 3;
					break;
				case 2: // CurveTo
					pathX.curveTo(potraceX[cont + 1], potraceX[cont + 2], potraceX[cont + 3], potraceX[cont + 4], potraceX[cont + 5], potraceX[cont + 6]);
					cont += 7;
					break;
				case 3: // closePath
				Geometry line = geomManager.createCurve(pathX, SUBTYPES.GEOM2D);
					addGeometry(line, values, fieldName);
					pathX = new BezierPathX(trozos);
					cont ++;
					break;
			}
		}
	}
	private EditableFeatureType getFeatureType(String fileName){
		EditableFeatureType eft=new DefaultEditableFeatureType();

		EditableFeatureAttributeDescriptor efad1=eft.add("ID", DataTypes.DOUBLE);
		efad1.setPrecision(5);
		EditableFeatureAttributeDescriptor efad2=eft.add(fileName, DataTypes.DOUBLE);
		efad2.setPrecision(5);

		return eft;
	}
	/**
	 * Returns the length of field
	 * @param dataType
	 * @return length of field
	 */
	public int getDataTypeLength(int dataType) {
		switch (dataType) {
		case Types.NUMERIC:
		case Types.DOUBLE:
		case Types.REAL:
		case Types.FLOAT:
		case Types.BIGINT:
		case Types.INTEGER:
		case Types.DECIMAL:
			return 20;
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			return 254;
		case Types.DATE:
			return 8;
		case Types.BOOLEAN:
		case Types.BIT:
			return 1;
		}
		return 0;
	}

	public static void main(String[] args){
		new TestRasterPotrace();
	}
}
