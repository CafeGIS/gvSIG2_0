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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import org.cresques.cts.IProjection;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.CrsWkt;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.persistence.RecentCRSsPersistence;
import org.gvsig.crs.persistence.RecentTrsPersistence;
import org.gvsig.crs.persistence.TrData;

import au.com.objectix.jgridshift.GridShiftFile;
import au.com.objectix.jgridshift.SubGrid;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.XMLEntity;

/**
 * Clase para generar el panel de la tranformacin nadgrids y su
 * manejo
 * 
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @author Jos Luis Gmez Martnez (jolugomar@gmail.com)
 * @author Luisa Marina Fernndez (luisam.fernandez@uclm.es)
 *
 */
public class TransformationNadgridsPanel extends JPanel implements IWindow, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel groupRadioButton = null;
	private JLabel jLabelChooser = null;
	private JRadioButton jRadioButtonSource = null;
	private JRadioButton jRadioButtonTarget = null;
	
	private IProjection firstProj;
	private String nadFile = null;
	String[] targetAuthority;
	String targetAbrev = "";
	String sourceAbrev = "";
	
	private String cadWKT = "";
	private String dataPath = "./gvSIG/extensiones/org.gvsig.crs/data/";
	private int codeEpsg;
	
	private JPanel jPanelOpen;
	private JPanel jPanelSelectNad;
	private JLabel jLabelOpenGsb;
	private JFileChooser openFileChooser;
	private JButton jButtonOpen;
	private TreePanel treePanel = null;
	private JComboBox jComboNadFile = null;
	private JLabel jLabelSelectNad = null;
	
	
	private JTextArea jTextAreaInfo;
	
	private PluginServices ps = null;
	private XMLEntity xml = null;
	
	boolean targetNad = false;
	boolean setRadioButtons = false;

	public TransformationNadgridsPanel(boolean optional) {
		ps = PluginServices.getPluginServices("org.gvsig.crs"); 
		xml = ps.getPersistentXML();
		setRadioButtons = optional;
		initialize();
	}
		
	private void initialize(){
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("+ "+PluginServices.getText(this,"nadgrids")),
				BorderFactory.createEmptyBorder(5,5,5,5)));
		JPanel pNorth=new JPanel();
		pNorth.setBorder(new EmptyBorder(1,10,1,10));
		pNorth.setLayout(new GridLayout(4,1));
		
		pNorth.add(getJPanelOpen());
		//label+combo
		pNorth.add(getJPanelSelectNad());
		JPanel lbl=new JPanel(new FlowLayout(FlowLayout.LEFT,0,10));
		lbl.add(getJLabelChooser());
		if (setRadioButtons) {
			pNorth.add(lbl);
			//dos radio buttons
			pNorth.add(getGroupRadioButton());
		}
		this.add(pNorth,BorderLayout.NORTH);
		this.add(getTreePanel(),BorderLayout.CENTER);
		
	}
	
	private JPanel getGroupRadioButton() {
		if (groupRadioButton == null) {
			groupRadioButton = new JPanel();
			groupRadioButton.setLayout(new GridLayout(1,0));	
			groupRadioButton.add(getJRadioButtonSource());
			groupRadioButton.add(getJRadioButtonTarget());			
		}
		return groupRadioButton;
	}
	
	private JRadioButton getJRadioButtonSource() {
		if (jRadioButtonSource == null) {
			jRadioButtonSource = new JRadioButton();			
			jRadioButtonSource.setSelected(true);
			jRadioButtonSource.addActionListener(this);
		}
		jRadioButtonSource.setText(PluginServices.getText(this,"aplicar_capa") + " " + "("+getSourceAbrev()+")" );
		return jRadioButtonSource;
	}
	
	
	private JRadioButton getJRadioButtonTarget() {		
		if (jRadioButtonTarget == null) {
			jRadioButtonTarget = new JRadioButton();			
			jRadioButtonTarget.setSelected(false);
			jRadioButtonTarget.addActionListener(this);
		}
		jRadioButtonTarget.setText(PluginServices.getText(this,"aplicar_vista") + " " + "("+ getTargetAbrev()+")");
		return jRadioButtonTarget;
	}	
	
	private JLabel getJLabelChooser(){
		jLabelChooser = new JLabel();
		jLabelChooser.setText(PluginServices.getText(this, "seleccion_nadgrids")+":");
		return jLabelChooser;
	}
		
		
	private JPanel getJPanelOpen() {
		if(jPanelOpen == null) {
			jPanelOpen = new JPanel();
			jPanelOpen.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
			jPanelOpen.add(getJLabelOpenGsb(), null);
			jPanelOpen.add(getJButtonOpen(), null);			
		}
		return jPanelOpen;
	}
	
	private JLabel getJLabelOpenGsb() {
		if(jLabelOpenGsb == null) {
			jLabelOpenGsb = new JLabel();
			jLabelOpenGsb.setPreferredSize(new Dimension(130,20));
			jLabelOpenGsb.setText(PluginServices.getText(this,"importarRejilla"));
		}
		return jLabelOpenGsb;
	}
	
	private JFileChooser getOpenFileChooser(){
		if(openFileChooser == null){
			openFileChooser = new JFileChooser();			
			openFileChooser.setEnabled(false);
			openFileChooser.addChoosableFileFilter(new FiltroNadgrids());
		}
		return openFileChooser;
	}

	private JButton getJButtonOpen() {
		if(jButtonOpen == null) {
			jButtonOpen = new JButton();
			jButtonOpen.setText("");
			jButtonOpen.setPreferredSize(new Dimension(20,20));
			jButtonOpen.setText("...");
			jButtonOpen.addActionListener(this);
		}
		return jButtonOpen;
	}
	
	public ICrs getProjection() {
		ICrs crs;
		if (jRadioButtonSource.isSelected()){
			try {
				setNad(false);
				//crs = new CrsFactory().getCRS(getSourceAbrev(),	getWKT());
				crs = new CrsFactory().getCRS(getSourceAbrev());
				crs.setTransformationParams("+nadgrids="+getNadFile(),null);//nadFile);
				return crs;
			} catch (org.gvsig.crs.CrsException e) {
				e.printStackTrace();
			}
			return null;
		}
		else {	
			setNad(true);
			try {
				//crs = new CrsFactory().getCRS(getSourceAbrev(), getWKT());
				crs = new CrsFactory().getCRS(getSourceAbrev());
				crs.setTransformationParams(null,"+nadgrids="+getNadFile());//nadFile);
				//crs.setParamsInTarget(true);
				
				return crs;
			} catch (CrsException e) {				
				e.printStackTrace();
			}
			return null;
		}		
	}
	
	public void setProjection(IProjection proj) {
		firstProj = proj;
	}
	
	public void setNad(boolean nadg){
		targetNad = nadg;		
	}
	
	public boolean getNad(){
		return targetNad;		
	}
	
	public void setCode(int cod){
		codeEpsg = cod;
	}
	
	public int getCode(){
		return codeEpsg;
	}
	
	public void setWKT(String cad){
		cadWKT = cad;
		CrsWkt parser = new CrsWkt(cad);
		//setSourceAbrev(parser.getAuthority()[0], parser.getAuthority()[1]);
		//getJTextAreaInfo();
		//getJRadioButtonSource();
	}
	
	public String getWKT(){
		return cadWKT;
	}
	
	public void setTargetAuthority(String[] authority){
		targetAuthority = authority;
		setTargetAbrev(targetAuthority[0], targetAuthority[1]);
		getJRadioButtonTarget();		
	}
	
	public String[] getTargetAuthority(){
		return targetAuthority;
	}
	
	public void setTargetAbrev(String fuente, String codigo){
		targetAbrev = fuente + ":" + codigo;
	}
	
	public String getTargetAbrev() {
		return targetAbrev;
	}
	
	public void setSourceAbrev(String fuente, String codigo){
		sourceAbrev = fuente + ":" + codigo;
		getJRadioButtonSource();
	}
	
	public String getSourceAbrev(){
		return sourceAbrev;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo=new WindowInfo(WindowInfo.MODALDIALOG);
   		m_viewinfo.setTitle(PluginServices.getText(this,"nadgrids"));
		return m_viewinfo;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.getJRadioButtonSource()){
			jRadioButtonSource.setSelected(true);
			jRadioButtonTarget.setSelected(false);
		}
		
		if (e.getSource() == this.getJRadioButtonTarget()){
			jRadioButtonTarget.setSelected(true);
			jRadioButtonSource.setSelected(false);
		}
		
		if (e.getSource() == this.getJButtonOpen()){
			jPanelOpen.add(getOpenFileChooser(), null);
			//openChooser.setCurrentDirectory(f);
			int returnVal = openFileChooser.showOpenDialog(TransformationNadgridsPanel.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File inFile = openFileChooser.getSelectedFile();
	            //jTextOpen.setText(file.getAbsolutePath());

	            String fileName = inFile.getName();
	            
	            //Comprobar que el fichero existe y tiene formato valido
	    		RandomAccessFile raFile = null;
	    		try {
	    			raFile = new RandomAccessFile(inFile.getAbsolutePath(),"r");
	    		} catch (FileNotFoundException ex) {
	    			ex.printStackTrace();
	    			getJComboNadFile().setSelectedIndex(0);
	    			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_not_found"));
	    			setNadFile(null);//nadFile = null;
	    			return;
	    		}
	    		GridShiftFile gsf = new GridShiftFile();
	    		try {
	    			gsf.loadGridShiftFile(raFile);
	    		} catch (IOException ex) {
	    			ex.printStackTrace();
	    			getJComboNadFile().setSelectedIndex(0);
	    			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_bad_format"));
	    			setNadFile(null);//nadFile = null;
	    			return;
	    		}catch (java.lang.Exception ex) {
	    			ex.printStackTrace();
	    			getJComboNadFile().setSelectedIndex(0);
	    			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_bad_format"));
	    			setNadFile(null);//nadFile = null;
	    			return;
	    		}
	            
	            //Copiar el fichero seleccionado al directorio data de la extensin:
	            
	    		File outFile = new File(dataPath+fileName);
	    		InputStream in = null;
	    		OutputStream out = null;
	    		try {
	    			in = new FileInputStream(inFile);
	    			out = new FileOutputStream(outFile);
	    		} catch (FileNotFoundException ex) {
	    			ex.printStackTrace();
	    		}
	    		byte[] buf = new byte[1024];
	    		int len;
	    		try {
	    			while ((len = in.read(buf)) > 0) {
	    				out.write(buf, 0, len);
	    			}
	    			in.close();
	    			out.close();
	    		} catch (IOException ex) {
	    			ex.printStackTrace();
	    		}
	    		
	    		//Incluir el fichero importado en el combo y seleccionarlo
	    		boolean exists = false;
	    		for (int item=0; item<getJComboNadFile().getItemCount() && !exists;item++)
	    			if (getJComboNadFile().getItemAt(item).equals(fileName))
	    				exists = true;
	    		if(!exists)
		    		getJComboNadFile().addItem(fileName);
	    		getJComboNadFile().setSelectedItem(fileName);
	    		setNadFile(fileName);//nadFile = fileName;
	        }
		}
	}

	public TreePanel getTreePanel() {
		if (treePanel == null){
			treePanel = new TreePanel(PluginServices.getText(this,"grids_en")+": "+nadFile);
			treePanel.getTree().expandRow(0);
			treePanel.setPanelSize(530,150);
		}
		return treePanel;
	}
	
	public void initializeTree(){
		// leer el fichero nadgrids
		RandomAccessFile raFile = null;
		try {
			raFile = new RandomAccessFile("./gvSIG/extensiones/org.gvsig.crs/data/"+nadFile,"r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_not_found"));
			setNadFile(null);//nadFile = null;
			return;
		}
		GridShiftFile gsf = new GridShiftFile();
		try {
			gsf.loadGridShiftFile(raFile);
		} catch (IOException e) {
			e.printStackTrace();
			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_bad_format"));
			setNadFile(null);//nadFile = null;
			return;
		}catch (java.lang.Exception e) {
			e.printStackTrace();
			getTreePanel().setRoot(PluginServices.getText(this,"nadfile_bad_format"));
			setNadFile(null);//nadFile = null;
			return;
		}
		
		SubGrid subGrid[] = gsf.getSubGridTree(); 
		
		for (int i=0;i<subGrid.length;i++){
			getTreePanel().addClass(subGrid[i].getSubGridName(),i);
			getTreePanel().addEntry(PluginServices.getText(this,"long_min")+": "+String.valueOf(subGrid[i].getMaxLon()/-3600)+"",subGrid[i].getSubGridName(), "");
			getTreePanel().addEntry(PluginServices.getText(this,"lat_min")+": "+String.valueOf(subGrid[i].getMinLat()/3600)+"?",subGrid[i].getSubGridName(), "");
			getTreePanel().addEntry(PluginServices.getText(this,"long_max")+": "+String.valueOf(subGrid[i].getMinLon()/-3600+"?"),subGrid[i].getSubGridName(), "");
			getTreePanel().addEntry(PluginServices.getText(this,"lat_max")+": "+String.valueOf(subGrid[i].getMaxLat()/3600+""),subGrid[i].getSubGridName(), "");
			getTreePanel().addEntry(PluginServices.getText(this,"node_count")+": "+String.valueOf(subGrid[i].getNodeCount()),subGrid[i].getSubGridName(), "");
			getTreePanel().addEntry(PluginServices.getText(this,"detalles")+": "+String.valueOf(subGrid[i].getDetails()),subGrid[i].getSubGridName(), "");
		}
		getTreePanel().getTree().expandRow(0);
		getTreePanel().getList().setText(PluginServices.getText(this,"advertencia_nad"));
	}

	public JComboBox getJComboNadFile() {
		if (jComboNadFile == null){
			jComboNadFile = new JComboBox();
			jComboNadFile.setPreferredSize(new Dimension(200,25));
			jComboNadFile.setEditable(false);
			jComboNadFile.addItem(PluginServices.getText(this,"seleccionar")+"...");
			//Aadir los al combo los nombres de los .gsb que hay en el directorio data
			File dataDir = new File(dataPath);
			for (int i = 0; i<dataDir.list().length;i++)
				if (dataDir.list()[i].substring(dataDir.list()[i].lastIndexOf('.')+1).equals("gsb")){
					jComboNadFile.addItem(dataDir.list()[i]);
				}
			if (getNadFile() != null)//nadFile != null)
				jComboNadFile.setSelectedItem(getNadFile());//nadFile);
						
		}
		return jComboNadFile;
	}

	public JLabel getJLabelSelectNad() {
		if (jLabelSelectNad == null){
			jLabelSelectNad = new JLabel(PluginServices.getText(this,"seleccionarRejilla")+":  ");
			jLabelSelectNad.setPreferredSize(new Dimension(130,25));
		}
		return jLabelSelectNad;
	}

	public JPanel getJPanelSelectNad() {
		if (jPanelSelectNad == null){
			jPanelSelectNad = new JPanel();
			jPanelSelectNad.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));		
			jPanelSelectNad.add(getJLabelSelectNad());
			jPanelSelectNad.add(getJComboNadFile());
		}
		return jPanelSelectNad;
	}
	
	public void setNadFile(String nad){
		nadFile = nad;
	}
	
	public String getNadFile(){
		return nadFile;
	}	
	
	/**
	 * Guarda el nombre del fichero nadgrids mediante el modelo de persistencia
	 * de Andami
	 * @param name Nombre del fichero nadgrids
	 */
	public void saveNadFileName(String name){
		ps = PluginServices.getPluginServices("org.gvsig.crs"); 
		xml = ps.getPersistentXML();
		
		for (int child = 0; child<xml.getChildrenCount();child++)
			if (xml.getChild(child).getPropertyName(0).equals("nadFile"))
				xml.removeChild(child);
		
		
		XMLEntity xmlEnt = new XMLEntity();
		xmlEnt.putProperty("nadFile",name);
		xml.addChild(xmlEnt);
		ps.setPersistentXML(xml);
	}
	
	/**
	 * Obtiene el nombre del fichero nadgrids guardado mediante el
	 * modelo de persistencia de Andami.
	 * 
	 * @return nombre del fichero nadgrids o null si no ha sido guardado.
	 */
	private String restoreNadFileName(){
		String fileName;
		for (int child = 0; child<xml.getChildrenCount();child++)
			if (xml.getChild(child).getPropertyName(0).equals("nadFile")){
				fileName = xml.getChild(child).getPropertyValue(0);
				File dataDir = new File(dataPath);
				for (int i = 0; i<dataDir.list().length;i++)
					if (dataDir.list()[i].equals(fileName)){
						return fileName;
					}
			}
		
		return null;
	}
	
	/**
	 * cuando utilizamos crs+transformacion, cargamos los paneles para que
	 * el usuario pueda consultar la transformacion utilizada...
	 * @param details
	 */
	public void fillData(String details) {
		RecentTrsPersistence trPersistence = new RecentTrsPersistence();
		TrData crsTrDataArray[] = trPersistence.getArrayOfTrData();
		
		for (int iRow = crsTrDataArray.length-1; iRow >= 0; iRow--) {
			if (details.equals(crsTrDataArray[iRow].getAuthority()+":"+crsTrDataArray[iRow].getCode()+" <--> "+crsTrDataArray[iRow].getDetails()) && crsTrDataArray[iRow].getAuthority().equals(PluginServices.getText(this, "NADGR"))) {
				String data[] = crsTrDataArray[iRow].getDetails().split(" ");
				String fichero = data[0];
				String authority = data[1].substring(1,data[1].length()-1);
				for (int i = 0; i< getJComboNadFile().getItemCount(); i++) {
					if (fichero.equals((String)getJComboNadFile().getItemAt(i))) {
						getJComboNadFile().setSelectedIndex(i);
						break;
					}
				}
				if (authority.equals(getSourceAbrev())) {
					getJRadioButtonSource().setSelected(true);
					getJRadioButtonTarget().setSelected(false);
				}
				else {
					getJRadioButtonSource().setSelected(false);
					getJRadioButtonTarget().setSelected(true);
				}
				
				break;
			}
		}
	}
	
	public void resetData () {
		getJRadioButtonSource().setSelected(true);
		getJRadioButtonTarget().setSelected(false);
		getJComboNadFile().setSelectedIndex(0);		
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
	
}

class FiltroNadgrids extends FileFilter {

	final static String gsb = "gsb";
	public boolean accept(File f) {
		if (f.isDirectory()) {
           return true;
       }
       String s = f.getName();
       int i = s.lastIndexOf('.');

       if (i > 0 &&  i < s.length() - 1) {
           String extension = s.substring(i+1).toLowerCase();
           if (gsb.equals(extension)){
                   return true;
           } else {
               return false;
           }
       }
       return false;
	}

	public String getDescription() {
		 return "Archivos .gsb";
	}
	
}
