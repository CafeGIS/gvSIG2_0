package com.iver.cit.gvsig.gui.panels.wfstclock;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.layers.vectorial.FLyrVect;
import org.gvsig.gui.beans.controls.IControl;
import org.gvsig.gui.beans.swing.JButton;

import com.iver.andami.messages.NotificationManager;

/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *      +34 963862235
 *   gvsig@gva.es
 *      www.gvsig.gva.es
 *
 *    or
 *
 *   IVER T.I. S.A
 *   Salamanca 50
 *   46005 Valencia
 *   Spain
 *
 *   +34 963163400
 *   dac@iver.es
 */
/* CVS MESSAGES:
 *
 * $Id$
 * $Log$
 *
 */
/**
 * @author Jorge Piera LLodrá (jorge.piera@iver.es)
 */
public class ClockPanel extends JPanel implements IControl{
	public static final String LOCKFEATURE_ACTIONCOMMAND = "lwac";
	private JButton wfstButton = null;
	private ClockText wfstText = null;
	private FLyrVect layer = null;
	boolean isClosed = false;

	public ClockPanel(FLyrVect layer){
		super();
		initialize();
		this.layer = layer;
		//this.setExpiryTime(layer.getWfstExpiryTime());		
		addActionListener(new ClockWindowListener(this));
	}

	/**
	 * Initializes the panel
	 */
	private void initialize(){
		setLayout(new java.awt.BorderLayout());
		add(getWfstButton(), java.awt.BorderLayout.WEST);
		add(getWfstTextField(), java.awt.BorderLayout.CENTER);
	}
	
	/**
	 * @return the value of the expiry text
	 */
	public int getExpiryTime(){
		try {
			return Integer.parseInt(getWfstTextField().getText());
		} catch (NumberFormatException nfe){
			return 0;
		}
	}
	
	/**
	 * Sets the expiry time
	 * @param i
	 */
	public void setExpiryTime(int i){
		getWfstTextField().setExpiryTime(i);
	}

	/**
	 * @return the WFST text field
	 */
	private ClockText getWfstTextField(){
		if (wfstText == null){
			wfstText = new ClockText();	
			wfstText.setEditable(false);
		}
		return wfstText;
	}

	/**
	 * @return the WFST button
	 */
	private JButton getWfstButton(){
		if (wfstButton == null){
			wfstButton = new JButton();
			wfstButton.setPreferredSize(new Dimension(40,40));
			wfstButton.setActionCommand(LOCKFEATURE_ACTIONCOMMAND);
			wfstButton.setIcon(new ImageIcon(this.getClass().getClassLoader().getResource("images/clock.png")));
		}
		return wfstButton;
	}
	
	/**
	 * Start the time
	 */
	public void startTime(){
		new TimeCounter(getExpiryTime()).start();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#addActionListener(java.awt.event.ActionListener)
	 */
	public void addActionListener(ActionListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#removeActionListener(java.awt.event.ActionListener)
	 */
	public void removeActionListener(ActionListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.controls.IControl#setValue(java.lang.Object)
	 */
	public Object setValue(Object value) {
		return value;
	}

	/**
	 * @return the layer
	 */
	public FLyrVect getLayer() {
		return layer;
	}
	
	/**
	 * Closes the thread
	 */
	public void setClosed() {
		this.isClosed = true;
	}
		
	/**
	 * It closes the window
	 */
	protected void closeWindow(){
		
	}
	
	/**
	 * To control the expiry time
	 * @author Jorge Piera Llodrá (jorge.piera@iver.es)
	 */
	private class TimeCounter extends Thread{
		int milis = 0;
		
		public TimeCounter(int minutes){
			milis = minutes * 1000 * 60;
		}
	
		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			try {
				printTime();
				while(!isClosed){
					sleep(1000);
					milis = milis - 1000;				
					printTime();
				}				
			} catch (InterruptedException e) {
				NotificationManager.addError(e);
			}
		}
		
		/**
		 * Print the time into the text field
		 */
		private void printTime(){
//			try{
//				Time time = new Time(milis);
//				if (milis < 3600000 ){
//					time.setHours(0);
//				}
//				if(milis < 60000){
//					getWfstTextField().setWarning();
//				}
//				if (milis == 0){
//					JOptionPane.showMessageDialog(
//							(Component) PluginServices.getMDIManager().getActiveWindow(),
//							PluginServices.getText(this, "wfst_layer_cant_be_saved_message"),
//							PluginServices.getText(this, "wfst_layer_cant_be_saved_window"),
//							JOptionPane.WARNING_MESSAGE);
//					layer.setWfstEditing(false);
//					closeWindow();					
//				}
//				getWfstTextField().setText(time.toString());
//			}catch(Exception e){
//				e.printStackTrace();
//			}
		}
	}
}
