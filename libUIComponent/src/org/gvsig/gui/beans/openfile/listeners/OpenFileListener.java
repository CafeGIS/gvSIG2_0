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
package org.gvsig.gui.beans.openfile.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import org.gvsig.gui.beans.openfile.OpenFileContainer;

public class OpenFileListener implements ActionListener{

	private JButton button = null;
	private String fName = null;
	private OpenFileContainer controlPanel= null;
	
	public OpenFileListener(OpenFileContainer panel){
		this.controlPanel = panel;
	}
	
	public void setButton(JButton but){
		this.button = but;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.button){
			JFileChooser file = new JFileChooser();
			file.setDialogTitle("Select File");
			file.addChoosableFileFilter(new ReadFilter(file, "raw"));
			int returnVal = file.showOpenDialog(controlPanel);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				fName = file.getSelectedFile().toString();
				controlPanel.getTOpen().setText(fName);
			}
		}
		
	}

	public String getFileName(){
		return fName;
	}
	
}

class ReadFilter extends javax.swing.filechooser.FileFilter{

	JFileChooser chooser = null;
	String filter = null;
	
	public ReadFilter(JFileChooser cho,String fil){
		this.chooser = cho;
		this.filter = fil;
	}
	
	public boolean accept(File f) {
		 return f.isDirectory() || f.getName().toLowerCase().endsWith("."+filter);
	}

	public String getDescription() {
		return "."+filter;
	}
	
}