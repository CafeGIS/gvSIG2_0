/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * 2009 Prodevelop S.L  vsanjaime   programador
 */

package org.gvsig.geocoding.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureSet;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.geom.primitive.Point;
import org.gvsig.fmap.geom.primitive.impl.Point2D;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.geocoding.address.Address;
import org.gvsig.geocoding.address.AddressComponent;
import org.gvsig.geocoding.address.ComposedAddress;
import org.gvsig.geocoding.address.Literal;
import org.gvsig.geocoding.address.NumberAddress;
import org.gvsig.geocoding.extension.GeocodingController;
import org.gvsig.geocoding.result.GeocodingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * table model of the results tables
 * 
 * @author <a href="mailto:jsanz@prodevelop.es"> Jorge Gaspar Sanz Salinas</a>
 * @author <a href="mailto:vsanjaime@prodevelop.es"> Vicente Sanjaime Calvet</a>
 */

public class TableResultsModel extends AbstractTableModel {

	private static final Logger log = LoggerFactory
			.getLogger(TableResultsModel.class);

	private static final long serialVersionUID = 1L;
	private PluginServices ps = PluginServices.getPluginServices(this);
	private String[] columnNames;
	private Object[][] data;
	private int columnsNumber;

	private GeocodingController control = null;

	/**
	 * Constructor with control
	 * 
	 * @param control
	 */
	public TableResultsModel(GeocodingController control) {

		this.control = control;
		columnNames = new String[4];
		columnsNumber = 4;
		this.getMainColumnNames();
		data = new Object[0][4];
	}

	/**
	 * Constructor with control and address
	 * 
	 * @param control
	 * @param address
	 */
	public TableResultsModel(GeocodingController control, Address address) {

		this.control = control;

		// Number address
		if (address instanceof NumberAddress) {
			NumberAddress adr = (NumberAddress) address;
			Literal mainLit = adr.getMainLiteral();
			int litsize = mainLit.size();
			// main columns+literal+numbers
			columnsNumber = 4 + litsize + 1;
			columnNames = new String[columnsNumber];
			getMainColumnNames();
			for (int i = 0; i < litsize; i++) {
				columnNames[4 + i] = ((AddressComponent) mainLit.get(i))
						.getKeyElement();
			}
			columnNames[4 + litsize] = ps.getText("fnumber");
		}

		// Composed address
		else if (address instanceof ComposedAddress) {
			ComposedAddress adr = (ComposedAddress) address;
			int intersize = adr.getIntersectionLiterals().size();
			Literal mainLit = adr.getMainLiteral();
			int litsize = mainLit.size();
			// cross
			if (intersize == 1) {
				columnsNumber = 4 + litsize + litsize;
				columnNames = new String[columnsNumber];
			}
			// between
			else {
				columnsNumber = 4 + litsize + litsize + litsize;
				columnNames = new String[columnsNumber];
			}
			getMainColumnNames();
			for (int i = 0; i < litsize; i++) {
				columnNames[4 + i] = ((AddressComponent) mainLit.get(i))
						.getKeyElement()
						+ "_1";
			}
			Literal secLit = adr.getIntersectionLiterals().get(0);
			for (int i = 0; i < litsize; i++) {
				columnNames[4 + litsize + i] = ((AddressComponent) secLit
						.get(i)).getKeyElement()
						+ "_2";
			}
			if (intersize == 2) {
				Literal thiLit = adr.getIntersectionLiterals().get(1);
				for (int i = 0; i < litsize; i++) {
					columnNames[4 + litsize + litsize + i] = ((AddressComponent) thiLit
							.get(i)).getKeyElement()
							+ "_3";
				}
			}
		}
		// address
		else {
			Literal mainLit = address.getMainLiteral();
			int litsize = mainLit.size();
			columnsNumber = 4 + litsize;
			columnNames = new String[columnsNumber];
			getMainColumnNames();
			for (int i = 0; i < litsize; i++) {
				columnNames[4 + i] = ((AddressComponent) mainLit.get(i))
						.getKeyElement();
			}
		}
	}

	public TableResultsModel(FeatureType type, FeatureSet set) {

		int siz = 0;
		try {
			siz = (int) set.getSize();

			columnsNumber = type.size();
			columnNames = new String[columnsNumber];
			for (int i = 0; i < columnsNumber; i++) {
				columnNames[i] = type.getAttributeDescriptor(i).getName();
			}
			data = new Object[siz][columnsNumber];
			Iterator<Feature> it = set.iterator();
			int i = 0;
			while (it.hasNext()) {
				Feature feat = it.next();
				for (int j = 0; j < columnsNumber; j++) {
					String fieldname = type.getAttributeDescriptor(j).getName();
					data[i][j] = feat.get(fieldname);
				}
				i++;
			}

		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Get number of column in the table model
	 * 
	 * @return
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Get rows number
	 * 
	 * @return
	 */
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Get selected column name
	 * 
	 * @param col
	 * @return
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Get registry value
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public Object getValueAt(int row, int col) {
		if (row < 0 || col < 0) {
			log.debug("Fila. " + row);
			log.debug("Columna. " + col);
			return null;
		}
		Object obj = data[row][col];
		return obj;
	}

	/**
	 * JTable uses this method to determine the default renderer/ editor for
	 * each cell. If we didn't implement this method, then the last column would
	 * contain text ("true"/"false"), rather than a check box.
	 * 
	 * @param c
	 * @return
	 */
	public Class getColumnClass(int c) {
		Object obj = getValueAt(0, c);
		if (obj == null) {
			return String.class;
		}
		return obj.getClass();
	}

	/**
	 * Are cells editables
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * Set value in selected cell
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	/**
	 * fill the model with results set
	 * 
	 * @param result
	 * @param maxResults
	 * @param score
	 */
	public void setResultSet(Set<GeocodingResult> result, int maxResults,
			double score) {

		data = new Object[0][columnsNumber];
		fireTableDataChanged();

		int num = result.size();
		if (result.size() > maxResults) {
			num = maxResults;
			// count score ok
			int numscore = 0;
			for (GeocodingResult res : result) {
				double sco = res.getScore();
				if (sco >= score) {
					numscore++;
				}
			}
			if (num > numscore) {
				num = numscore;
			}
		} else {
			// count score ok
			int numscore = 0;
			for (GeocodingResult res : result) {
				double sco = res.getScore();
				if (sco >= score) {
					numscore++;
				}
			}
			if (num > numscore) {
				num = numscore;
			}
		}

		data = new Object[num][columnsNumber];
		int i = 0;
		log.debug("Results: " + result.size());
		Iterator<GeocodingResult> it = result.iterator();
		while (it.hasNext()) {
			if (i < num) {
				GeocodingResult res = it.next();

				int id = i + 1;
				Double score1 = new Double(res.getScore());

				data[i][0] = id;
				data[i][1] = score1.intValue();
				if (res.getGeometry() != null) {
					Double x = new Double(((Point) res.getGeometry()).getX());
					Double y = new Double(((Point) res.getGeometry()).getY());
					data[i][2] = x;
					data[i][3] = y;
				} else {
					data[i][2] = 0.0;
					data[i][3] = 0.0;
				}

				// range
				if (res.getAddress() instanceof NumberAddress) {
					// get number address
					NumberAddress address = (NumberAddress) res.getAddress();
					Literal mainLit = address.getMainLiteral();
					int litsize = mainLit.size();
					// fill literal address values
					for (int j = 0; j < litsize; j++) {
						int cont = 4 + j;
						AddressComponent comp = (AddressComponent) mainLit
								.get(j);
						String value = comp.getValue();
						data[i][cont] = value;
					}
					// fill address number
					data[i][4 + litsize] = address.getNumber();
				}

				// Composed address
				else if (res.getAddress() instanceof ComposedAddress) {
					// get compose address
					ComposedAddress address = (ComposedAddress) res
							.getAddress();
					// main literal
					Literal mainLit = address.getMainLiteral();
					int litsize = mainLit.size();
					// fill main literal address values
					for (int j = 0; j < litsize; j++) {
						int cont = 4 + j;
						AddressComponent comp = (AddressComponent) mainLit
								.get(j);
						String value = comp.getValue();
						data[i][cont] = value;
					}
					// fill second literal address values
					List<Literal> inter = address.getIntersectionLiterals();
					Literal secLit = inter.get(0);
					for (int j = 0; j < litsize; j++) {
						int cont = 4 + litsize + j;
						AddressComponent comp = (AddressComponent) secLit
								.get(j);
						String value = comp.getValue();
						data[i][cont] = value;
					}
					// third literal (between)
					int intersize = address.getIntersectionLiterals().size();
					if (intersize == 2) {
						Literal thiLit = inter.get(1);
						for (int j = 0; j < litsize; j++) {
							int cont = 4 + litsize + litsize + j;
							AddressComponent comp = (AddressComponent) thiLit
									.get(j);
							String value = comp.getValue();
							data[i][cont] = value;
						}
					}
				}
				// address
				else {
					Literal mainLit = res.getAddress().getMainLiteral();
					int litsize = mainLit.size();
					for (int j = 0; j < litsize; j++) {
						int cont = 4 + j;
						AddressComponent comp = (AddressComponent) mainLit
								.get(j);
						String value = comp.getValue();
						data[i][cont] = value;
					}
				}

			} else {
				break;
			}
			i++;
		}
		fireTableDataChanged();
	}

	/**
	 * Get the main columns names
	 */
	private void getMainColumnNames() {

		if (ps != null) {
			columnNames[0] = ps.getText("fid");
			columnNames[1] = ps.getText("fscore");
			View view = control.getCurrentView();
			if (view != null) {
				MapContext context = view.getModel().getMapContext();
				MapControl mControl = new MapControl();
				mControl.setMapContext(context);
				IProjection proj = mControl.getProjection();
				if (proj.isProjected()) {
					columnNames[2] = ps.getText("fx");
					columnNames[3] = ps.getText("fy");
				} else {
					columnNames[2] = ps.getText("flong");
					columnNames[3] = ps.getText("flat");
				}
			} else {
				columnNames[2] = ps.getText("fx");
				columnNames[3] = ps.getText("fy");
			}
		}
	}

	/**
	 * get the geometry (Point2D)
	 * 
	 * @param row
	 * @return
	 */
	public Point getGeometry(int row, int xColumn, int yColumn) {
		Double x = (Double) getValueAt(row, xColumn);
		Double y = (Double) getValueAt(row, yColumn);
		Point pto = new Point2D();
		pto.setX(x);
		pto.setY(y);
		return pto;
	}

}
