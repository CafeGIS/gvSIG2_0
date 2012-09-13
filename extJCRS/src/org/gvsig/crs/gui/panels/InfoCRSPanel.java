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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;

import org.cresques.ui.DefaultDialogPanel;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import com.iver.andami.PluginServices;
import com.iver.andami.persistence.generate.PluginsStatusDescriptor;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Clase que genera el panel de informacin del CRS seleccionado
 * en el caso del repositorio de la EPSG
 * @author Jose Luis Gomez Martinez (joseluis.gomez@uclm.es)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class InfoCRSPanel extends DefaultDialogPanel implements IWindow, ActionListener{
	
	private static final long serialVersionUID = 1L;

	private JPanel panelLabels;
	
	private JTable jTable;
	public DefaultTableModel dtm = null;
	private JScrollPane jScrollPane1 = null;
	private JPanel jPanelbuttons;
	private JButton jButtonOk;
	
	private ICrs proj;
	
	private JLabel jLabelProjcs;
	private JLabel jLabelGeogcs;
	private JLabel jLabelDatum;
	private JLabel jLabelSpheroid;
	private JLabel jLabelPrimem;
	private JLabel jLabelProjection;
	private JLabel jLabelUnits;
	private JLabel jLabelUnits_p;
	private JLabel jLabelProjcsdinamic;
	private JLabel jLabelGeogcsdinamic;
	private JLabel jLabelDatumdinamic;
	private JLabel jLabelSpheroiddinamic;
	private JLabel jLabelPrimemdinamic;
	private JLabel jLabelProjectiondinamic;
	private JLabel jLabelUnitsdinamic;
	private JLabel jLabelUnits_pdinamic;
	
	private JEditorPane jEditorPane = null;
	private JScrollPane jScrollPane = null;
	private String page = "";
	private boolean isTransformation = false;
	private String details = null;
	private String transformationAuthority = null;
	private int transformationCode = -1;
	private String sourceCrs = null;
	private String targetCrs = null;
	private String transformationName = null;
	//private String
	
	
	public InfoCRSPanel(String fuente, int codigo) {
		super(false);		
		try {
			proj = new CrsFactory().getCRS(fuente+":"+codigo);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		inicializate();
	}
	
	public InfoCRSPanel(String fuente, int codigo, String trans) {
		super(false);		
		try {
			proj = new CrsFactory().getCRS(fuente+":"+codigo);
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if (!trans.equals(PluginServices.getText(this, "sin_transformacion"))) {
			isTransformation = true;
			details = trans;
			RecentTrsPersistence trPersistence = new RecentTrsPersistence();
			TrData crsTrDataArray[] = trPersistence.getArrayOfTrData();
			
			for (int iRow = crsTrDataArray.length-1; iRow >= 0; iRow--) {
				if (details.equals(crsTrDataArray[iRow].getAuthority()+":"+crsTrDataArray[iRow].getCode()+" <--> "+crsTrDataArray[iRow].getDetails())) {
					//Ya tenemos todos los datos de la transformacion...
					transformationAuthority = crsTrDataArray[iRow].getAuthority();
					transformationCode = crsTrDataArray[iRow].getCode();
					sourceCrs = crsTrDataArray[iRow].getCrsSource();
					targetCrs = crsTrDataArray[iRow].getCrsTarget();
					transformationName = crsTrDataArray[iRow].getName();
					details = crsTrDataArray[iRow].getDetails();					
				}
			}
			
		} 
		inicializate();
	}
	
	private void inicializate() {
		this.setLayout(new BorderLayout());
		JPanel p=new JPanel(new BorderLayout());
		p.setBorder(new EmptyBorder(0,10,0,10));
		//p.add(getPanel(),BorderLayout.NORTH);
		//p.add(getJScrollPane1(),BorderLayout.CENTER);
		createPage();
		
		p.add(getJScrollPane());
		
		this.add(p, BorderLayout.CENTER);
		this.add(getJPanelButtons(),BorderLayout.SOUTH);
	}

	private JPanel getPanel() {
		if(panelLabels == null) {
			panelLabels = new JPanel();
			panelLabels.setLayout(new GridLayout(4,2));
			JPanel p00=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p00.add(getJLabelProjcs());
			p00.add(getJLabelProjcsDinamic());
			JPanel p01=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p01.add(getJLabelGeogcs());
			p01.add(getJLabelGeogcsDinamic());
			JPanel p10=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p10.add(getJLabelDatum());
			p10.add(getJLabelDatumDinamic());
			JPanel p11=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p11.add(getJLabelSpheroid());
			p11.add(getJLabelSpheroidDinamic());
			JPanel p20=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p20.add(getJLabelPrimen());
			p20.add(getJLabelPrimenDinamic());
			JPanel p21=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p21.add(getJLabelUnits());
			p21.add(getJLabelUnitsDinamic());
			JPanel p30=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p30.add(getJLabelProjection());
			p30.add(getJLabelProjectionDinamic());
			JPanel p31=new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
			p31.add(getJLabelUnits_p());
			p31.add(getJLabelUnits_pDinamic());
			
			panelLabels.add(p00);
			panelLabels.add(p01);
			panelLabels.add(p10);
			panelLabels.add(p11);
			panelLabels.add(p20);
			panelLabels.add(p21);
			panelLabels.add(p30);
			panelLabels.add(p31);
			
		}
		return panelLabels;
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
			jButtonOk.setToolTipText("Accept");
			jButtonOk.addActionListener(this);
		}
		return jButtonOk;
	}
	
	private JLabel getJLabelProjcs() {
		if(jLabelProjcs == null) {
			jLabelProjcs = new JLabel();
			jLabelProjcs.setFont(new Font("Projcs:",Font.BOLD,10));
			jLabelProjcs.setText(PluginServices.getText(this,"projcs")+":");
		}
		return jLabelProjcs;
	}
	
	private JLabel getJLabelProjcsDinamic() {
		if(jLabelProjcsdinamic == null) {
			jLabelProjcsdinamic = new JLabel();
			jLabelProjcsdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelProjcsdinamic.setText(proj.getCrsWkt().getProjcs());
		}
		return jLabelProjcsdinamic;
	}
	
	private JLabel getJLabelGeogcs() {
		if(jLabelGeogcs == null) {
			jLabelGeogcs = new JLabel();
			jLabelGeogcs.setFont(new Font("Geogcs:",Font.BOLD,10));
			jLabelGeogcs.setText(PluginServices.getText(this,"geogcs")+ ":");
		}
		return jLabelGeogcs;
	}
	
	private JLabel getJLabelGeogcsDinamic() {
		if(jLabelGeogcsdinamic == null) {
			jLabelGeogcsdinamic = new JLabel();
			jLabelGeogcsdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelGeogcsdinamic.setText(proj.getCrsWkt().getGeogcs());
		}
		return jLabelGeogcsdinamic;
	}
	
	private JLabel getJLabelDatum() {
		if(jLabelDatum == null) {
			jLabelDatum = new JLabel();
			jLabelDatum.setFont(new Font("Datum:",Font.BOLD,10));
			jLabelDatum.setText(PluginServices.getText(this,"datum")+":");
		}
		return jLabelDatum;
	}
	
	private JLabel getJLabelDatumDinamic() {
		if(jLabelDatumdinamic == null) {
			jLabelDatumdinamic = new JLabel();
			jLabelDatumdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelDatumdinamic.setText(proj.getCrsWkt().getDatumName());
		}
		return jLabelDatumdinamic;
	}
	
	private JLabel getJLabelSpheroid() {
		if(jLabelSpheroid == null) {
			jLabelSpheroid = new JLabel();
			jLabelSpheroid.setFont(new Font("Spheroid",Font.BOLD,10));
			jLabelSpheroid.setText(PluginServices.getText(this,"spheriod")+":");
		}
		return jLabelSpheroid;
	}
	
	private JLabel getJLabelSpheroidDinamic() {
		if(jLabelSpheroiddinamic == null) {
			String[] sphe = proj.getCrsWkt().getSpheroid();
			jLabelSpheroiddinamic = new JLabel();
			jLabelSpheroiddinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelSpheroiddinamic.setText(sphe[0]+" , "+sphe[1]+" , "+sphe[2]);
		}
		return jLabelSpheroiddinamic;
	}
	
	private JLabel getJLabelPrimen() {
		if(jLabelPrimem == null) {
			jLabelPrimem = new JLabel();
			jLabelPrimem.setFont(new Font("Primen:",Font.BOLD,10));
			jLabelPrimem.setText(PluginServices.getText(this,"primem")+":");
		}
		return jLabelPrimem;
	}
	
	private JLabel getJLabelPrimenDinamic() {
		if(jLabelPrimemdinamic == null) {
			String pri[] = proj.getCrsWkt().getPrimen();
			jLabelPrimemdinamic = new JLabel();
			jLabelPrimemdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelPrimemdinamic.setText(pri[0]+" , "+pri[1]);
		}
		return jLabelPrimemdinamic;
	}
	
	private JLabel getJLabelProjection() {
		if(jLabelProjection == null) {
			jLabelProjection = new JLabel();
			jLabelProjection.setFont(new Font("Projection",Font.BOLD,10));
			jLabelProjection.setText(PluginServices.getText(this,"projection")+":");
		}
		return jLabelProjection;
	}
	
	private JLabel getJLabelProjectionDinamic() {
		if(jLabelProjectiondinamic == null) {
			jLabelProjectiondinamic = new JLabel();
			jLabelProjectiondinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelProjectiondinamic.setText(proj.getCrsWkt().getProjection());
		}
		return jLabelProjectiondinamic;
	}
	
	private JLabel getJLabelUnits() {
		if(jLabelUnits == null) {
			jLabelUnits = new JLabel();
			jLabelUnits.setFont(new Font("Units",Font.BOLD,10));
			jLabelUnits.setText(PluginServices.getText(this,"units")+":");
		}
		return jLabelUnits;
	}
	
	private JLabel getJLabelUnitsDinamic() {
		if(jLabelUnitsdinamic == null) {
			String[] units = proj.getCrsWkt().getUnit();
			jLabelUnitsdinamic = new JLabel();
			jLabelUnitsdinamic.setPreferredSize(new Dimension(200,20));
			jLabelUnitsdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelUnitsdinamic.setText(units[0]+" , "+units[1]);
			System.out.println("Unidades:  "+ units[0]+" , "+units[1]);

		}
		return jLabelUnitsdinamic;
	}
	
	private JLabel getJLabelUnits_p() {
		if(jLabelUnits_p == null) {
			jLabelUnits_p = new JLabel();
			jLabelUnits_p.setFont(new Font("",Font.BOLD,10));
			jLabelUnits_p.setText(PluginServices.getText(this,"units_p")+":");
		}
		return jLabelUnits_p;
	}
	
	private JLabel getJLabelUnits_pDinamic() {
		if(jLabelUnits_pdinamic == null) {
			String[] uni_p = proj.getCrsWkt().getUnit_p();
			jLabelUnits_pdinamic = new JLabel();
			jLabelUnits_pdinamic.setFont(new Font("",Font.ROMAN_BASELINE,10));
			jLabelUnits_pdinamic.setText(uni_p[0]+" , "+uni_p[1]);
			
		}
		return jLabelUnits_pdinamic;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setPreferredSize(new Dimension(400,150));
			jScrollPane1.setViewportView(getJTable());
		}
		return jScrollPane1;
	}
	
	private JTable getJTable() {
		if(jTable == null) {
			String[] columnNames = {PluginServices.getText(this,"nombre")
					,PluginServices.getText(this,"valor")};
			String[] param_n = proj.getCrsWkt().getParam_name();
			String[] param_v = proj.getCrsWkt().getParam_value();
			Object[][]data;
			if(param_v != null) {
				data = new Object[param_v.length][2];
				for(int i = 0 ; i < 2 ; i++ )
					for(int j = 0 ; j < param_n.length ; j++) {
						if(i == 0)
							data[j][i] = param_n[j];
						else
							data[j][i] = param_v[j];
					}
			} else 
				data = new Object[1][1];
			dtm = new DefaultTableModel(data, columnNames);
			jTable = new JTable(dtm);
			jTable.setCellSelectionEnabled(false);
			jTable.setRowSelectionAllowed(true);
			jTable.setColumnSelectionAllowed(false);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
		}
		return jTable;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this,proj.getCrsWkt().getName()));
   		m_viewinfo.setWidth(600);
   		m_viewinfo.setHeight(500);
		return m_viewinfo;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getJButtonOk()){
			PluginServices.getMDIManager().closeWindow(this);
		}
	}
	
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */    
	private JScrollPane getJScrollPane() {
		
		// if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(300,450));
			jScrollPane.setViewportView(getJEditorPane());
		// }
		return jScrollPane;
	}
	/**
	 * This method initializes jEditorPane1	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */    
	private JEditorPane getJEditorPane() {
		// if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			
			jEditorPane.setEditable(false);
			jEditorPane.setContentType("text/html");
			jEditorPane.setPreferredSize(new java.awt.Dimension(300,450));
			
			
			jEditorPane.setText(page);//Page(aboutURL);
			  
			
		// }
		return jEditorPane;
	}
	
	private void createPage() {
		String title = PluginServices.getText(this, "infoCRS");
		
		page += "<html>  <head>  <title>"+title+"</title> <meta content=\"\"> <style></style> </head> <body>";
		page += "<p align=\"center\"><font size=5><b>"+title.toUpperCase()+"</font></b></p>";
		//Primera fila, codigo y repositorio
		page += "<br><table width=\"100%\" border=\"0\">  " +
				"<tr> " +
					"<td width=\"50%\">"+PluginServices.getText(this, "codeSelectedCrs")+": "+proj.getCode()+"</td> " +
					"<td width=\"50%\"><div align=\"center\">"+PluginServices.getText(this, "repository")+": "+proj.getCrsWkt().getAuthority()[0]+"</div></td> " +
				"</tr>";
		
		String projected;
		if (proj.getCrsWkt().getProjcs().equals(""))
			projected = PluginServices.getText(this, "no");
		else
			projected = PluginServices.getText(this, "si");		
		
		//Segunda fila, nombre y proyeccion		
		page += "<tr> " +
			"<td width=\"50%\">"+PluginServices.getText(this, "nameSelectedCrs")+": "+proj.getCrsWkt().getName()+"</td> " +
			"<td width=\"50%\"><div align=\"center\">"+PluginServices.getText(this, "projected")+": "+projected+"</div></td> " +
		"</tr>";
		
		//Nombre del datum
		page += "<tr> " +
			"<td width=\"60%\">"+PluginServices.getText(this, "nameDatum")+": "+proj.getCrsWkt().getDatumName()+"</td> " +
			"<td width=\"40%\"><div align=\"center\">"+"</div></td> " +
		"</tr>";
		
		page +="</table>";
		
		//Datos del elipsoide
		page += "<br><table width=\"100%\" border=\"2\">  " +
				"<tr> " +
					"<td align=\"center\"><b>"+PluginServices.getText(this, "ellipsoid")+": "+proj.getCrsWkt().getSpheroid()[0]+"</b></td> " +
				"</tr>";
		
		page += "<tr> " +
					"<td>"+PluginServices.getText(this, "semiMajorAxis")+"</td> " +
					"<td><div align=\"center\">"+proj.getCrsWkt().getSpheroid()[1]+"</div></td> " +
				"</tr>";
		page += "<tr> " +
					"<td>"+PluginServices.getText(this, "invFlattening")+"</td> " +
					"<td><div align=\"center\">"+proj.getCrsWkt().getSpheroid()[2]+"</div></td> " +
				"</tr>";
		page += "</table>";
		
		//Datos del prime meridian
		page += "<br><table width=\"100%\" border=\"2\">  " +
		"<tr> " +
			"<td align=\"center\"><b>"+PluginServices.getText(this, "primeMeridian")+": "+proj.getCrsWkt().getPrimen()[0]+"</b></td> " +
		"</tr>";

		page += "<tr> " +
			"<td>"+PluginServices.getText(this, "greenwichLongitude")+"</td> " +
			"<td><div align=\"center\">"+proj.getCrsWkt().getPrimen()[1]+"</div></td> " +
		"</tr>";
		
		page += "</table>";
		
		//Si es proyectado procedemos a concatenar la proyección y sus parámetros
		if (projected.equals(PluginServices.getText(this, "si"))) {
						
			//proyeccion
			page += "<p><font size=4>"+PluginServices.getText(this, "projection")+": <b>"+proj.getCrsWkt().getProjection()+"</b></font></p>";
			
			//Parametros
			page += "<p align=\"center\">"+PluginServices.getText(this, "parameters")+"<table width=\"100%\" border=\"2\">  ";
			for (int i = 0; i < proj.getCrsWkt().getParam_name().length; i++) {
				
				page += "<tr> " +
					"<td align=\"center\">"+PluginServices.getText(this, proj.getCrsWkt().getParam_name()[i])+"</td> " +
					"<td align=\"center\">"+proj.getCrsWkt().getParam_value()[i]+"</td> " +
				"</tr>";
			}
			page += "</table>";
		}
		
		try {
			page += "<p>"+PluginServices.getText(this, "proj4Chain")+": "+ proj.getProj4String() +"</p>";
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (isTransformation) {
			
			//Titulo de la transformacion
			page += "<p align=\"center\"><font size=5><b>"+PluginServices.getText(this, "transformation")+"</b></font></p>";
			
			//Tipo de transformacion y codigo si es EPSG
			page += "<p>"+PluginServices.getText(this, "type_transformation")+": ";
			
			if (transformationAuthority.equals(PluginServices.getText(this, "USR"))) {
				page += PluginServices.getText(this, "user")+"</p>";
			}
			else if (transformationAuthority.equals(PluginServices.getText(this, "EPSG"))) {
				page += PluginServices.getText(this, "EPSG")+":"+transformationCode+"</p>";
				//nombre de la transformacion
				page += "<p>"+PluginServices.getText(this, "name")+": "+transformationName+"</p>";
			}
			else if (transformationAuthority.equals(PluginServices.getText(this, "NADGR"))) {
				page += PluginServices.getText(this, "nadgrids")+"</p>";
			}
			else if (transformationAuthority.equals(PluginServices.getText(this, "COMP"))) {
				page += PluginServices.getText(this, "compound")+"</p>";
			}
			
			//Fuente y destino de la transformacion
			page += "<br><table width=\"100%\" border=\"0\">  " +
			"<tr> " +
				"<td align=\"center\">"+PluginServices.getText(this, "source_crs")+": "+sourceCrs+"</td> " +
				"<td align=\"center\">"+PluginServices.getText(this, "target_crs")+": "+targetCrs+"</td> " +
			"</tr></table>";
			
			page += "<p>"+PluginServices.getText(this, "detailsTransformation")+": "+details+"</p>";	
			
		}
		
		page += "</body> </html>";
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}
