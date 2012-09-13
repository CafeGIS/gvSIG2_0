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
package org.gvsig.rastertools.memorydrivertest;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.gvsig.fmap.dal.DataStoreParameters;
import org.gvsig.fmap.dal.coverage.dataset.io.MemoryRasterDriverParam;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.exceptions.LoadLayerException;
import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.RasterDriverException;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.gui.WizardPanel;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class TestMemoryDriverWizard extends WizardPanel implements ActionListener {
	private static final long serialVersionUID = 6919167443568023588L;
	private JButton but = null;
	private String path = "/home/flus/images-gv/03AUG23153350-M2AS-000000122423_01_P001-BROWSE.jpg";
	//private String path = "/home/nacho/images/wcs16bits.tif";
	//private String path = "/mnt/hdd/imagenes/bugRaster/Red_drenaje.asc";
	private RasterDataset f = null;
	private BufferFactory bf = null;
	private MapContext m_MapContext;

	public TestMemoryDriverWizard (){
		super();
		initialize() ;
	}

	static {
		RasterLibrary.wakeUp();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setTabName("Test Memory Driver");
		this.setLayout(new FlowLayout());
		JLabel label = new JLabel("En TestMemoryDriverWizard debe haber una ruta a una imagen.");
		but = new JButton("Test MemoryDriver");
		but.setPreferredSize(new Dimension(180,35));
		this.add(label, null);
		this.add(but, null);
		but.addActionListener(this);
	}

	public void execute() {


	}

	public FLayer getLayer() {
		return null;
	}

	public void initWizard() {


	}

	private IBuffer createBuffer(){
		try {
			f = RasterDataset.open(null, path);
		} catch (NotSupportedExtensionException e) {
			e.printStackTrace();
			return null;
		} catch (RasterDriverException e) {
			e.printStackTrace();
			return null;
		}
		RasterLibrary.cacheSize = 1;
		RasterLibrary.pageSize = 0.2;
		bf = new BufferFactory(f);
		int[] drawableBands = {0, 1, 2};
		int x = 0;//0;//406;//150;
		int y = 0;//0;//350;//150;
		int w = f.getWidth();//601;//100;
		int h = f.getHeight();//410;//100;

		bf.setDrawableBands(drawableBands);
		try {
			bf.setAreaOfInterest(x, y, w, h);
		} catch (InvalidSetViewException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RasterDriverException e) {
			e.printStackTrace();
		}
		return bf.getRasterBuf();
	}

	public void actionPerformed(ActionEvent e) {
		//Carga de una capa con datos de memoria
		createBuffer();

		BaseView theView = (BaseView) PluginServices.getMDIManager().getActiveWindow();
		this.m_MapContext = theView.getModel().getMapContext();
		FLyrRasterSE layer = null;
		/*for (int j = 0; j < raster.getHeight(); j++) {
			for (int i = 0; i < raster.getWidth(); i++) {
				byte c = raster.getElemByte(j, i, 0);
				if(c != 0)
					System.out.println(c);
			}
		}*/

		try {
			layer = FLyrRasterSE.createLayer("test-memoryDriver",
					new MemoryRasterDriverParam(bf.getRasterBuf(), bf.getDataExtent()),  m_MapContext.getProjection());
			m_MapContext.getLayers().addLayer(layer);
		} catch (LoadLayerException e1) {
			return;
		}

		//Acceso a datos de la capa de memoria
		/*if(layer != null){
			Grid g = layer.getGrid();
			int[] drawableBands = {0, 1, 2};
			g.addDrawableBands(drawableBands);
			g.setAreaOfInterest(0, 0, g.getWidth(), g.getHeight());
			RasterBuf b = g.getRasterBuf();
			for(int i = 0; i < b.getWidth(); i++){
				for(int j = 0; j < b.getHeight(); j++)
					System.out.print(b.getElemByte(j, i, 0) + " ");
				System.out.println("");
			}
		}*/

	}

	@Override
	public DataStoreParameters[] getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
