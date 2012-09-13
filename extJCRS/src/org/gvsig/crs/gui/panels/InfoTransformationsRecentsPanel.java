/* gvSIG. Sistema de Informacin Geogrfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *
 * For more information, contact:
 *
 *  Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.TableSorter;

import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Panel con la informacin de la transformacin seleccionada
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class InfoTransformationsRecentsPanel extends JPanel implements IWindow, ActionListener{

	private static final long serialVersionUID = 1L;

	private JTable jTable;
	public DefaultTableModel dtm = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel jPanelbuttons;
	private JButton jButtonOk;
	public TableSorter sorter = null;
	String[] data = null;
	
	//Ancho y alto del panel
	private int v_height=200;
	private int v_width=420;
	
	public InfoTransformationsRecentsPanel(String[] data) {
		super();
		this.data = data;
		inicializate();
	}
	
	private void inicializate() {
		setLayout(new BorderLayout());
		add(getJScrollPane1(), BorderLayout.CENTER);
		add(getJPanelButtons(), BorderLayout.SOUTH);
		
	}

	private JPanel getJPanelButtons() {
		if(jPanelbuttons == null) {
			jPanelbuttons = new JPanel();
			jPanelbuttons.setLayout(new FlowLayout(FlowLayout.RIGHT,10,10));
			jPanelbuttons.add(getJButtonOk(),null);
		}
		return jPanelbuttons;
	}
	
	private JButton getJButtonOk() {
		if(jButtonOk == null) {
			jButtonOk = new JButton();
			jButtonOk.setText(PluginServices.getText(this,"ok"));
			jButtonOk.setPreferredSize(new Dimension(100,25));
			jButtonOk.setMnemonic('O');
			jButtonOk.setToolTipText(PluginServices.getText(this,"ok"));
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
		
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new Dimension(400,150));	
			jScrollPane1.setBorder(
				    BorderFactory.createCompoundBorder(
					BorderFactory.createCompoundBorder(
							BorderFactory.createTitledBorder(PluginServices.getText(this,"info_transformations")),
							BorderFactory.createEmptyBorder(5,5,5,5)),
							jScrollPane1.getBorder()));
			jScrollPane1.setViewportView(getJTable());
		}
		return jScrollPane1;
	}
	
	private JTable getJTable() {
		if(jTable == null) {
			String[] columnNames = {PluginServices.getText(this,"nombre")
					,PluginServices.getText(this,"valor")};
			Object[][] datos = obtainData();
			dtm = new DefaultTableModel(datos, columnNames)
			 {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}		
			};
			sorter = new TableSorter(dtm);			

			jTable = new JTable(sorter);

			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		}
		return jTable;
	}
	
	/**
	 * Mtodo que recuperar la informacin a mostrar en el panel
	 * de las transformaciones recientes
	 * @return
	 */
	private String[][] obtainData(){	
		String[][] valid = null;
		String[] transformation = data[0].split(":");
		if (transformation[0].equals("EPSG")){
			valid = new String[6][2];
			valid[0][0] = PluginServices.getText(this,"source_crs");
			valid[0][1] = data[2];
			valid[1][0] = PluginServices.getText(this,"target_crs");
			valid[1][1] = data[3];
			EpsgConnection conn = new EpsgConnection();
			conn.setConnectionEPSG();
			String sentence = "SELECT area_of_use_code " +
							"FROM epsg_coordoperation " +                        
							"WHERE coord_op_code = " + transformation[1] ;
		    ResultSet result = Query.select(sentence,conn.getConnection());		   
			try {
				result.next();				
				valid[2][0] =  PluginServices.getText(this,"transformation_code");
				valid[2][1] = transformation[1];				
				sentence = "SELECT area_of_use FROM epsg_area " +
								"WHERE area_code = "+ Integer.parseInt(result.getString("area_of_use_code"));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			valid[3][0] = PluginServices.getText(this,"transformation_name");
			valid[3][1] = data[1];
			valid[4][0] = PluginServices.getText(this,"details");
			result = Query.select(sentence, conn.getConnection());
			try {
				result.next();
				valid[4][1] = result.getString("area_of_use");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (transformation[0].equals("USR")){
			valid = new String[3][2];
			valid[0][0] = PluginServices.getText(this,"source_crs");
			valid[0][1] = data[2];
			valid[1][0] = PluginServices.getText(this,"target_crs");
			valid[1][1] = data[3];
			valid[2][0] = PluginServices.getText(this,"details");
			valid[2][1] = data[4];			
		} else {
			valid = new String[4][2];
			String[] partes = data[4].split("\\(");
			String nadFile = partes[0];		
			String codigoNad = partes[1].substring(0,partes[1].length()-1);
			valid[0][0] = PluginServices.getText(this,"source_crs");
			valid[0][1] = data[2];
			valid[1][0] =PluginServices.getText(this,"target_crs");
			valid[1][1] = data[3];
			valid[2][0] = PluginServices.getText(this,"nadgrids_file");
			valid[2][1] = nadFile;	
			valid[3][0] = PluginServices.getText(this,"calculated_in");
			valid[3][1] = codigoNad;
		}
		return valid;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle("Info");//PluginServices.getText(this,proj.getCrsWkt().getName()));
   		//Define el ancho y el alto del panel
   		m_viewinfo.setHeight(v_height);
   		m_viewinfo.setWidth(v_width);
		return m_viewinfo;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonOk()){
			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
