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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import org.cresques.cts.IProjection;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.dialog.CSSelectionDialog;
import org.gvsig.crs.gui.dialog.TRSelectionDialog;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

/**
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class ProjChooserPanel extends CRSSelectPanel {
    
	private static final long serialVersionUID = 1L;
	
	public  ICrs curProj = null; //(IProjection) new CRSFactory().getCRS("EPSG:23030");
	
	private JLabel jLblProj = null;
	private JLabel jLblProjName = null;
	private JButton jBtnChangeProj = null;
	private boolean okPressed = false;
	private String abrev;
	boolean panel = false;
	boolean targetNad = false;
	String dataSource = "EPSG";
	
	private ActionListener actionListener = null;
	
	public ProjChooserPanel(IProjection proj) {
		super(proj);	
		IProjection pr = proj;
		IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
    	if (activeWindow instanceof BaseView) {		
    		BaseView activeView = (BaseView) activeWindow;
    		pr = activeView.getMapControl().getProjection();
    	}
    	AddLayerDialog.setLastProjection(pr);	
		setCurProj(pr);
		initialize();		
	}
	

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new GridLayout(3,1));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        setPreferredSize(new java.awt.Dimension(330,35));
        this.setSize(new java.awt.Dimension(330,23));
        this.add(getJLblProjName(), null);
        this.add(getJLblProj(), null);
        this.add(getJBtnChangeProj(), null);
       
        
        //cambiar cuando la parte de JLuis se ensamble
        if(!((ICrs)curProj).getAbrev().equals("EPSG:23030")){
        	dataSource = curProj.getCrsWkt().getAuthority()[0];
        	jLblProj.setText(dataSource+":" +String.valueOf(curProj.getCode()));
        	//jLblProj.setText(((ICrs)curProj).getCrsWkt().getName());
        }
        else
        	jLblProj.setText("EPSG:23030");        
		initBtnChangeProj();
	}

	private void initBtnChangeProj() {
		getJBtnChangeProj().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				CSSelectionDialog csSelect = null;
				TRSelectionDialog trSelect = null;
				if (!isTransPanelActive()){
					okPressed = false;
					csSelect = new CSSelectionDialog((ICrs) curProj);
					csSelect.setProjection(curProj);
					csSelect.initRecents(curProj);
					//csSelect.setLayout(new GridLayout(0,1));
					
			        PluginServices.getMDIManager().addWindow(csSelect);
			        abrev = dataSource+ ":" +String.valueOf(curProj.getCode());
			        if (csSelect.isOkPressed()) {
			        	curProj = (ICrs) csSelect.getProjection();
			        	dataSource = curProj.getCrsWkt().getAuthority()[0];
			        	//dataSource = csSelect.getDataSource();
			        	abrev = dataSource+ ":" +String.valueOf(curProj.getCode());
			        	
			        	jLblProj.setText(dataSource+ ":" +String.valueOf(curProj.getCode()));//.getAbrev());
			        	AddLayerDialog.setLastProjection(curProj);
			        	okPressed = true;
			        	if (actionListener != null) {
			        		actionListener.actionPerformed(e);
			        	}
			        }else			        	
			        	jLblProj.setText(abrev);  
				}
				else {
					okPressed = false;										
			    	trSelect = new TRSelectionDialog(curProj);
					
					trSelect.setProjection(curProj);
			        
					trSelect.setLayout(new GridLayout(0,1));
					
					PluginServices.getMDIManager().addWindow(trSelect);
					setTargetNad(trSelect.getTargetNad());
			        //abrev = curProj.getAbrev();
					
			        if (trSelect.getProjection() != curProj) {
			        	curProj = (ICrs) trSelect.getProjection();
			        	dataSource = curProj.getCrsWkt().getAuthority()[0];
			        	//dataSource = trSelect.getProjPanel().getDataSource();
			        	//abrev = trSelect.getProjectionName();
			        	jLblProj.setText(dataSource+ ":" +String.valueOf(curProj.getCode()));//abrev);
			        	AddLayerDialog.setLastProjection(curProj);			        	
			        	okPressed = true;
			        	
			        	if (actionListener != null) {
			        		actionListener.actionPerformed(e);
			        	}
			        }else
			        	jLblProj.setText(dataSource+ ":" +String.valueOf(curProj.getCode()));//abrev);
					
				}
					        	
			}
		});
	}

	public JLabel getJLblProjName() {
		if (jLblProjName == null) {
	        jLblProjName = new JLabel("Proyeccion actual");
			jLblProjName.setText(PluginServices.getText(this,"proyeccion_actual")); //$NON-NLS-1$
		}
		return jLblProjName;
	}
	
	public JLabel getJLabel() {
		return getJLblProjName();
	}

	public JLabel getJLblProj() {
		if (jLblProj == null) {
	        jLblProj = new JLabel();
			jLblProj.setText(curProj.getAbrev());
		}
		return jLblProj;
	}
	public void addBtnChangeProjActionListener(java.awt.event.ActionListener al) {
		jBtnChangeProj.addActionListener(al);
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getJBtnChangeProj() {
		if (jBtnChangeProj == null) {
			jBtnChangeProj = new JButton();			
			jBtnChangeProj.setText("..."); //$NON-NLS-1$
			jBtnChangeProj.setPreferredSize(new Dimension(50, 23));
		}
		return jBtnChangeProj;
	}
	
	public void setTargetNad(boolean tarNad){
		targetNad = tarNad;
	}
	
	public boolean getTargetNad(){
		return targetNad;
	}
	
	/**
	 * @return Returns the curProj.
	 */
	public IProjection getCurProj() {
		return curProj;
	}
	/**
	 * @param curProj The curProj to set.
	 */
	public void setCurProj(IProjection curProj) {
		this.curProj = (ICrs) curProj;
	}
	/**
	 * @return Returns the okPressed.
	 */
	public boolean isOkPressed() {
		return okPressed;
	}
	/**
	 * @param actionListener The actionListener to set.
	 */
	public void addActionListener(ActionListener actionListener) {
		this.actionListener = actionListener;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
