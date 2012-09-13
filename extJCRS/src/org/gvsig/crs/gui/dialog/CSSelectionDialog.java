/*
 * Created on 26-ene-2005
 */
package org.gvsig.crs.gui.dialog;

import java.awt.GridLayout;

import org.gvsig.crs.ICrs;
import org.gvsig.crs.gui.CRSSelectionDialog;


/**
 * Dilogo contenedor para la seleccin de CRS. 
 * Incluye los controles para las transformaciones
 * para el CRS de la capa)
 * 
 * @author Luis W. Sevilla (sevilla_lui@gva.es)
 */
public class CSSelectionDialog extends CRSSelectionDialog{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public CSSelectionDialog(ICrs curProj) {
		super(curProj);
		this.init();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void init() {
		this.setBounds(0, 0, 600, 400);
		this.setLayout(new GridLayout(0,1));
//		this.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
		
	}
	
	
	/*public IProjection getProjection() {
		return (IProjection) getProjPanel().getProjection();
	}
	
	public void setProjection(IProjection proj) {
		lastProj = proj;
		getProjPanel().setProjection(proj);
	}
	
	public String getProjectionAbrev(){
		return  (String) getProjPanel().getProjection().getAbrev();
	}*/	
}
