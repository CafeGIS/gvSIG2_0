/*
* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
*   Av. Blasco Ibañez, 50
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

package org.gvsig.remotesensing.imagefusion.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.raster.util.RasterUtilities;
import org.gvsig.remotesensing.principalcomponents.gui.BandTableFormat;

import com.iver.andami.PluginServices;

/**
 * Componente para la seleccion de la banda de alta resolución.
 * Permite abrir el fichero en el que se encuentra la banda pancromática y seleccionar
 * la banda en cuestión.
 *
 * @version 27/02/2008
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 *
 */
public class BandHigthResolutionSelection extends JPanel implements ActionListener {

	private static final long    serialVersionUID = 1L;
	private JTextField           fileName = null;
	private JButton              bSelection = null;
	private FLyrRasterSE         layer = null;
	private BandTableFormat 	 mModeloTabla=null;
	private JScrollPane			 scrollBandas=null;
	private JTable 				 jTableBandas=null;


	public BandHigthResolutionSelection() {
		init();
	}

	/**
	 * Acciones de inicialización del panel
	 */
	public void init() {
		BorderLayout fl = new BorderLayout();
		BorderLayout fl2 = new BorderLayout();
		fl.setHgap(3);
		fl.setVgap(5);
		fl2.setHgap(3);
		fl2.setVgap(5);
		JPanel panel= new JPanel();
		panel.setPreferredSize(new Dimension(310,90));
		JPanel northPanel= new JPanel();
		panel.setLayout(fl);
		northPanel.setLayout(fl2);
		northPanel.add(getFileName(), BorderLayout.CENTER);
		northPanel.add(getSelectFileButton(),BorderLayout.EAST);
		panel.add(northPanel,BorderLayout.NORTH);
		panel.add(getScrollBands(),BorderLayout.CENTER);
		TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this,"band_high"));
		topBorder.setTitlePosition(TitledBorder.TOP);
		this.setBorder(new CompoundBorder(topBorder,new EmptyBorder(2,2,2,2)));
		add(panel,BorderLayout.NORTH);
	}

	/**
	 * Obtiene el campo con la ruta del fichero con la banda de alta resolucion
	 * @return JFormattedTextField
	 */
	private JTextField getFileName() {
		if (fileName == null) {
			fileName = new JTextField();
			fileName.setEditable(true);
		}
		return fileName;
	}

	/**
	 * Obtiene el botón de selección de fichero
	 * @return JButton
	 */
	private JButton getSelectFileButton() {
		if(bSelection == null) {
			bSelection = new JButton(RasterToolsUtil.getText(this, "select"));
			bSelection.addActionListener(this);
		}
		return bSelection;
	}


	public JScrollPane getScrollBands() {
		if (scrollBandas==null){
			scrollBandas=new JScrollPane();
		}
		getTableBands().getColumn("").setPreferredWidth(30);
		scrollBandas.setViewportView(getTableBands());
		scrollBandas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollBandas;
	}


	/**
	 * @return tabla conlas bandas de la imagen
	 */
	private JTable getTableBands(){
		if (jTableBandas==null){
			jTableBandas=new JTable(getModeloTabla());
		}
		return jTableBandas;
	}


	/**
	 * @return modelo de tabla
	 * @see BandTableFormat
	 */
	public BandTableFormat getModeloTabla() {
		if(mModeloTabla==null){
			mModeloTabla=new BandTableFormat();
			mModeloTabla.setMultipleSelection(false);
		}
		return mModeloTabla;
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == getSelectFileButton()) {
			loadRasterLayer();
		}
	}

	/**
	 * Muestra el dialogo de selección de fichero para la carga de la capa
	 * con la banda de alta resolucion
	 * @return Capa raster cargada o null si no se consigue ninguna
	 */
	private FLyrRasterSE loadRasterLayer() {
		String path = null;
		JFileChooser chooser = new JFileChooser("BAND_HIGTH_RESOLUTION_SELECTION", JFileChooser.getLastPath("BAND_HIGTH_RESOLUTION_SELECTION", null));
		chooser.setAcceptAllFileFilterUsed(false);
		String[] extensionsSupported = RasterDataset.getExtensionsSupported();
		ExtendedFileFilter allFiles = new ExtendedFileFilter();
		for (int i = 0; i < extensionsSupported.length; i++) {
			ExtendedFileFilter fileFilter = new ExtendedFileFilter();
			fileFilter.addExtension((String)extensionsSupported[i]);
			allFiles.addExtension((String)extensionsSupported[i]);
			chooser.addChoosableFileFilter(fileFilter);
		}
		allFiles.setDescription(RasterToolsUtil.getText(this, "todos_soportados"));
		chooser.addChoosableFileFilter(allFiles);
		JFileChooser.setLastPath("BAND_HIGTH_RESOLUTION_SELECTION", chooser.getSelectedFile());
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
			try {
				if (layer != null)
					layer.getDataSource().close();
				layer = FLyrRasterSE.createLayer(RasterUtilities.getLastPart(path, File.separator), path, null);
				if (layer != null)
					getFileName().setText(path);
				getBandas();
				return layer;
			} catch (LoadLayerException e) {
			}
		}
		return null;
	}


	/**
	 * Toma las Bandas de la imagen seleccionada en el combo Imagen
	 */
	public void getBandas(){

		// Antes de insertar se borra lo anterior
		getModeloTabla().LimpiarLista();

		FLyrRasterSE rasterLayer= getLayer();
		String bandas[]=new String[rasterLayer.getBandCount()];
		int []bands= new int[rasterLayer.getBandCount()];
		for (int j=0; j<rasterLayer.getBandCount(); j++){
			String s=(PluginServices.getText(this,"banda"))+(j+1);
			bandas[j]=s;
			bands[j]=j;
		}
		getModeloTabla().addRow(bandas[0],true);
		for (int i=1;i<bandas.length;i++)
			getModeloTabla().addRow(bandas[i],false);
			getTableBands().updateUI();

	}

	//-------Consulta de propiedades seleccionadas---------

	/**
	 * Obtiene la capa que ha sido abierta por el usuario
	 * @return Obtiene la capa que ha sido abierta por el usuario o null si no
	 * hay abierta ninguna.
	 */
	public FLyrRasterSE getLayer() {
		return layer;
	}

}