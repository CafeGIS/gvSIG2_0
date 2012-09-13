/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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

package org.gvsig.remotesensing.mosaic.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.remotesensing.principalcomponents.gui.BandTableFormat;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Panel que gestiona la selección de imagenes de la vista.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 24/4/2008
 *
 * */
public class SelectLayersFromViewPanel extends JPanel{

	private static final long 	serialVersionUID = 1L;
	private View 				view = null;
	private JPanel 				imageViewPanel = null;
	private JTable 				tableImagesView = null;
	private BandTableFormat 	mModeloTable =null;
	private JScrollPane			scrollImages=null;
	private FLayer[] layers = null;;

	public SelectLayersFromViewPanel (View view){
		this.view= view;
		if(this.view!=null)
			layers= new FLayer[view.getMapControl().getMapContext().getLayers().getLayersCount()];
		int k=view.getMapControl().getMapContext().getLayers().getLayersCount()-1;
		for(int i=0;i<view.getMapControl().getMapContext().getLayers().getLayersCount();i++){
			 	layers[i]=((FLyrRasterSE)view.getMapControl().getMapContext().getLayers().getLayer(k));
			 	k--;
		}
		initUI();
		loadLayers();
	}

	/**
	 * Panel con la tabla que contiene las imagenes cargadas en la vista
	 * */
	public JPanel getImageViewPanel() {
		if(imageViewPanel==null){
			imageViewPanel = new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder("seleccion_imagenes");
			topBorder.setTitlePosition(TitledBorder.TOP);
			imageViewPanel.setBorder(topBorder);
			imageViewPanel.add(getScrollImages(),BorderLayout.CENTER);
		}
		return imageViewPanel;
	}


	/**
	 * @return array de string con los nombre de las capas raster
	 */
	private String[] getLayerNames() {
		int i, capasraster=0;
		String[] sNames= null;

		for (i = 0; i < layers.length; i++) {
			if (layers[i]instanceof  FLyrRasterSE)
				capasraster++;
		}
		// Se se toman las capas raster.
		sNames = new String[capasraster];
		capasraster=0;

		for (i = 0; i < layers.length; i++) {
			if (layers[i]instanceof FLyrRasterSE){
				sNames[capasraster] =layers[i].getName();
				capasraster++;
			}
		}
		return sNames;
	}


	/**
	 * @return scroll con bandas del ráster
	 */
	public JScrollPane getScrollImages() {
		if (scrollImages==null){
			scrollImages=new JScrollPane();
		}
		getTableImages().getColumn("").setPreferredWidth(20);
		scrollImages.setPreferredSize(new Dimension(300,100));
		scrollImages.setViewportView(getTableImages());
		scrollImages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollImages;
	}

	/**
	 * @return modelo de tabla
	 * @see BandTableFormat
	 */
	public BandTableFormat getModeloTabla() {
		if(mModeloTable==null){
			mModeloTable=new BandTableFormat();
			mModeloTable.setColumnName(PluginServices.getText(this,"imagen"));

		}
		return mModeloTable;
	}

	/**
	 * @return tabla conlas bandas de la imagen
	 */
	public JTable getTableImages(){
		if (tableImagesView==null){
			tableImagesView=new JTable(getModeloTabla());
		}
		return tableImagesView;
	}


	/**
	 *  Método que inicializa la interfaz añadiendo los paneles correspondientes
	 * */
	private void initUI(){
		setLayout(new BorderLayout());
		add(getImageViewPanel(), BorderLayout.CENTER);
	}


	/**
	 *  Método que carga en la tabla tableImagenView las capas raster de la vista
	 *  Por defeto todas se encuentran activadas
	 * */
	private void loadLayers(){
		String[] layersNames =getLayerNames();
		for (int i=0;i<layersNames.length;i++)
			getModeloTabla().addRow(layersNames[i],true);
		getTableImages().updateUI();

	}

	/**
	 * Medodo que devuelve un array de Strings con en nombre de las capas seleccionadas
	 * */
	public String[] getSelectedLayers(){

		String[] layersSelectedNames =new String[getModeloTabla().getNumSelected()];
		boolean []selected= mModeloTable.getSeleccionadas();
		String [] sImages = getLayerNames();
		int k=0;
		for(int i=0; i< sImages.length; i++)
			if(selected[i]){
				layersSelectedNames[k]=sImages[i];
				k++;
			}
		return layersSelectedNames;
	}


	public void actionPerformed(ActionEvent e) {
	}

}
