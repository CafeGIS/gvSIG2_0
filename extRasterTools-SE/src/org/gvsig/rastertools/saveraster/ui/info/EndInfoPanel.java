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
package org.gvsig.rastertools.saveraster.ui.info;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.raster.util.RasterUtilities;

import com.iver.andami.PluginServices;
import com.iver.utiles.swing.JComboBox;
/**
 * Panel principal del dialogo de finalización del salvado a raster. En el se
 * muestra la información de nombre de fichero, tamaño de este, tiempo de la
 * operación, etc...
 *
 * @version 18/04/2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class EndInfoPanel extends DefaultButtonsPanel implements ActionListener {
	private static final long	serialVersionUID	= -2280318605043767336L;
	private JPanel contentPane = null;
	private JComboBox comboBox = null;

	private String labelFilename = "File:";
	private String labelPath = "Path:";
	private String labelTime = "Time:";
	private String labelSize = "Size:";
	private String labelCompression = "Compression:";

	/**
	 * This is the default constructor
	 */
	protected EndInfoPanel() {
		super(ButtonsPanel.BUTTONS_ACCEPT);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		initialize();
		setTranslation();
	}

	/**
	 * Asigna los textos a los paneles en el idioma seleccionado
	 */
	private void setTranslation() {
		setLabelFilename(PluginServices.getText(this, "file") + ":");
		setLabelPath(PluginServices.getText(this, "path") + ":");
		setLabelSize(PluginServices.getText(this, "size") + ":");
		setLabelTime(PluginServices.getText(this, "time") + ":");
		setLabelCompression(PluginServices.getText(this, "compress") + ":");
	}

	protected void addFile(String fileName, long time) {
		File f = new File(fileName);
		String size = RasterUtilities.formatFileSize(f.length());

		String compression;
		if (fileName.endsWith("ecw") || fileName.endsWith("jp2") ||
				fileName.endsWith("jpg") || fileName.endsWith("jpeg")) {
			compression = "Yes";
		} else {
			compression = "No";
		}
		
		JPanel pContent = new JPanel();
		pContent.setLayout(new GridBagLayout());

		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(5, 5, 2, 5);
		pContent.add(newPanelFile(fileName), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 5, 2, 5);
		pContent.add(newPanelTimeSize(RasterUtilities.formatTime(time), size), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 5, 5, 5);
		pContent.add(newPanelCompression(compression), gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		JPanel emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(0, 0));
		pContent.add(emptyPanel, gridBagConstraints);

		pContent.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "Estadisticas",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));

		File file = new File(fileName);
		
		contentPane.add(pContent, file.getName());
		if (comboBox.getItemCount() == 0)
			comboBox.setVisible(false);
		else
			comboBox.setVisible(true);
		comboBox.addItem(file.getName());
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		setLayout(new BorderLayout(5, 5));

		contentPane = new JPanel();
		contentPane.setLayout(new CardLayout());
		
		comboBox = new JComboBox();
		comboBox.setVisible(false);
		comboBox.addActionListener(this);
		
		this.add(contentPane, BorderLayout.CENTER);
		this.add(comboBox, BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel newPanelFile(String file) {
		JPanel pFile = new JPanel();

		JLabel lFileNameTag = new JLabel(labelFilename);
		JLabel lPathTag = new JLabel(labelPath);

		File fileorig = new File(file);

		JTextField tFile = new JTextField(fileorig.getName());
		tFile.setBackground(pFile.getBackground());
		tFile.setEditable(false);
		tFile.setHorizontalAlignment(JTextField.LEFT);
		tFile.setBorder(null);
		
		JTextField pathFile = new JTextField(fileorig.getParent() + File.separator);
		pathFile.setBackground(pFile.getBackground());
		pathFile.setEditable(false);
		pathFile.setHorizontalAlignment(JTextField.LEFT);
		pathFile.setBorder(null);
		
		pFile.setLayout(new GridBagLayout());
		
		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		pFile.add(lFileNameTag, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		pFile.add(tFile, gridBagConstraints);
		
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		pFile.add(lPathTag, gridBagConstraints);

		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		pFile.add(pathFile, gridBagConstraints);

		
		return pFile;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel newPanelTimeSize(String time, String size) {
		JPanel pTime = new JPanel();
		pTime.setLayout(new GridBagLayout());

		JLabel lTimeTag = new JLabel(labelTime);
		GridBagConstraints gridBagConstraints;
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 5, 2, 2);
		pTime.add(lTimeTag, gridBagConstraints);

		JLabel lTime = new JLabel(time);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 11);
		pTime.add(lTime, gridBagConstraints);

		JLabel lSizeTag = new JLabel(labelSize);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.insets = new Insets(2, 2, 2, 11);
		pTime.add(lSizeTag, gridBagConstraints);

		JLabel lSize = new JLabel(size);
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(2, 2, 2, 5);
		pTime.add(lSize, gridBagConstraints);

		return pTime;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel newPanelCompression(String compression) {
		JPanel pCompression = new JPanel();

		JLabel lCompressTag = new JLabel(labelCompression);
		JLabel lCompress = new JLabel(compression);

		pCompression.setLayout(new FlowLayout(FlowLayout.LEFT));
		pCompression.add(lCompressTag);
		pCompression.add(lCompress);

		return pCompression;
	}

	/**
	 * @param labelFilename the labelFilename to set
	 */
	private void setLabelFilename(String labelFilename) {
		this.labelFilename = labelFilename;
	}

	/**
	 * @param labelFilename the labelFilename to set
	 */
	private void setLabelPath(String labelPath) {
		this.labelPath = labelPath;
	}

	/**
	 * @param labelTime the labelTime to set
	 */
	private void setLabelTime(String labelTime) {
		this.labelTime = labelTime;
	}

	/**
	 * @param labelSize the labelSize to set
	 */
	private void setLabelSize(String labelSize) {
		this.labelSize = labelSize;
	}

	/**
	 * @param labelCompression the labelCompression to set
	 */
	private void setLabelCompression(String labelCompression) {
		this.labelCompression = labelCompression;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == comboBox)
			((CardLayout) contentPane.getLayout()).show(contentPane, (String) comboBox.getSelectedItem());
	}
}