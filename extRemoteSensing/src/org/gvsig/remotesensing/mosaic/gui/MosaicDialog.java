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


/**
 * Diálogo para la funcionalidad de mosaicos.
 *
 * @author aMuÑoz (alejandro.munoz@uclm.es)
 * @version 24/4/2008
 *
 * */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanel;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelEvent;
import org.gvsig.gui.beans.buttonspanel.ButtonsPanelListener;
import org.gvsig.gui.beans.defaultbuttonspanel.DefaultButtonsPanel;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.raster.IProcessActions;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.buffer.BufferFactory;
import org.gvsig.raster.buffer.RasterBuffer;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IRasterDataSource;
import org.gvsig.raster.datastruct.Histogram;
import org.gvsig.raster.hierarchy.IHistogramable;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterNotLoadException;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.histogram.HistogramProcess;
import org.gvsig.remotesensing.mosaic.process.FeatherProcessBuff;
import org.gvsig.remotesensing.mosaic.process.HistogramMatchProcess;
import org.gvsig.remotesensing.mosaic.process.MosaicProcess;

import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.IWindowListener;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class MosaicDialog extends DefaultButtonsPanel implements PropertyListener, IWindow, IWindowListener,
	ButtonsPanelListener,ActionListener,FocusListener,PropertyChangeListener,IProcessActions{

	private static final long serialVersionUID = 2847035927527203595L;
	private SelectLayersFromViewPanel northPanel = null;
	private MethodMosaicPanel southPanel = null;
	private MapControl mapControl = null;
	private View view=null;
	private JPanel 					panelTab1=null;
	private JPanel 					panelTab2=null;
	private JPanel 					nombreCapa=null;
	private JRadioButton 			rButtonMemory=null;
	private JRadioButton 			rButtonFile=null;
	private JTextField 				jTextNombreCapa=null;

	private final int HM = 0;
	private final int PREMOSAICING = 1;
	private final int MOSAICING = 2;
	private final int LOADING = 3;
	private int currentProcess = this.HM;

	private FLyrRasterSE hmMasterRaster = null;
	private int masterRasterPosition = 0 ;
	private FLyrRasterSE inputRasterLayers[];
	private FLyrRasterSE resultLayer = null;
	private boolean applyHistogramM = false;

	/**
	 * Constructor
	 * @param width Ancho del panel
	 * @param height Alto del panel
	 */
	public MosaicDialog(int width, int height, View view) {
		super();
		this.view= view;
		this.setSize(width, height);
		this.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
	 	tabbedPane.addTab(PluginServices.getText(this, "operacion"), getPanelTab1());
		tabbedPane.addTab(PluginServices.getText(this, "opciones"), getPanelTab2());
		this.add(tabbedPane, BorderLayout.CENTER);
		this.addButtonPressedListener(this);
	}


	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.RESIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this, "mosaicos"));
		m_viewinfo.setHeight(this.getHeight());
		m_viewinfo.setWidth(this.getWidth());
		return m_viewinfo;
	}


	/**
	 * Acciones a ejecutar cuando se cancela
	 */
	public void close(){
		try {
			PluginServices.getMDIManager().closeWindow(this);
		} catch (ArrayIndexOutOfBoundsException ex) {
			// Si la ventana no se puede eliminar no hacemos nada
		}
	}

	/**
	 *  @return northPanel. Panel principal
	 * */
	public SelectLayersFromViewPanel getPanelNorth() {
		if (northPanel == null){
			northPanel = new SelectLayersFromViewPanel(view);
			northPanel.getTableImages().addPropertyChangeListener(this);
		}
		return northPanel;
	}

	/**
	 *  @return northPanel. Panel principal
	 * */
	public MethodMosaicPanel getPanelSouth() {
		if (southPanel == null){
			southPanel = new MethodMosaicPanel(northPanel.getSelectedLayers(),view);
		}
		return southPanel;
	}



	public MapControl getMapControl() {
		return mapControl;
	}


	/**
	 * @return panel para el tab operacion.
	 */
	private JPanel getPanelTab1(){
		if (panelTab1==null){
			panelTab1 = new JPanel();
			panelTab1.setLayout(new GridBagLayout());
			JPanel selectionImagenPanel= new JPanel();
			JPanel aux= new JPanel();
			BorderLayout bd=new BorderLayout();
			aux.setLayout(bd);
			aux.add(getPanelNorth(),BorderLayout.NORTH);
			aux.add(getPanelSouth(),BorderLayout.SOUTH);
			selectionImagenPanel.add(aux);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 0.1;
			c.gridx = 0;
			c.gridy = 0;
			panelTab1.add(selectionImagenPanel, c);
		 }
		 return panelTab1;
	 }


	 /**
	  * @return Panel para el tab de opciones.
	  */
	private JPanel getPanelTab2(){
		if (panelTab2==null){
			panelTab2 = new JPanel();
			panelTab2.setLayout(new GridBagLayout());

			JPanel parameterPanel= new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this, "parametros"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			JPanel aux= new JPanel();
			aux.setPreferredSize(new Dimension(300,110));

			BorderLayout bd=new BorderLayout();
			aux.setLayout(bd);
			parameterPanel.add(aux);

			parameterPanel.setBorder(topBorder);
			BorderLayout bordlayout= new BorderLayout();
			parameterPanel.setLayout(bordlayout);
			parameterPanel.add(new JPanel(),BorderLayout.NORTH);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 0;
			panelTab2.add(parameterPanel, c);


			// Insercion del panel de las clases
			c.fill = GridBagConstraints.BOTH;
			c.insets=new Insets(2,2,2,2);
			c.weightx = 0.05;
			c.weighty = 0.05;
			c.gridx = 0;
			c.gridy = 1;

			panelTab2.add(getOutputOptionsPanel(), c);
		}
		return panelTab2;
	 }


	 /**
	 * @return panel con las opciones de salida.
	 */
	public JPanel getOutputOptionsPanel() {

		if (nombreCapa==null){
			nombreCapa=new JPanel();
			nombreCapa.setPreferredSize(new Dimension(400,130));
			TitledBorder topBorder = BorderFactory.createTitledBorder(PluginServices.getText(this, "salida"));
			topBorder.setTitlePosition(TitledBorder.TOP);
			nombreCapa.setBorder(topBorder);
			GridBagConstraints gridBagConstraints;
			JPanel radioPanel = new JPanel();
			radioPanel.setLayout(new GridBagLayout());
			radioPanel.setBorder(BorderFactory.createTitledBorder(""));
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(getRadioMemory());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			radioPanel.add(getRadioMemory(),gridBagConstraints);
			buttonGroup.add(getRadioFile());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 2;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			radioPanel.add(getRadioFile(),gridBagConstraints);
			//Establece la separacion entre los elementos
			nombreCapa.setLayout(new GridBagLayout());
			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(new JLabel(("Nombre:"),SwingConstants.RIGHT ),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(getJTextNombreCapa(),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			nombreCapa.add(radioPanel,gridBagConstraints);
		}
			return nombreCapa;
		}



	/**
	 * @return JRadioButton de generar en memoria
	 */
	public JRadioButton getRadioMemory(){
		if (rButtonMemory == null){
			rButtonMemory = new JRadioButton(PluginServices.getText(this,"a_memoria"),true);
			rButtonMemory.addActionListener(this);
		}
		return rButtonMemory;
	}



	/**
	* @return JRadioButton de generar fichero
	*/
	public JRadioButton getRadioFile(){
			if (rButtonFile == null){
				rButtonFile = new JRadioButton(PluginServices.getText(this,"a_fichero"));
				rButtonFile.addActionListener(this);
			}
			return rButtonFile;
	}

	/**
	 * @return javax.swing.JTextField
	 */
	public JTextField getJTextNombreCapa() {
		if (jTextNombreCapa==null) {
			jTextNombreCapa=new JTextField(10);
			jTextNombreCapa.setText(RasterLibrary.getOnlyLayerName());
			jTextNombreCapa.addFocusListener(this);
		}
			return jTextNombreCapa;
	}


	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	 public void updateNewLayerText() {
		 	getJTextNombreCapa().setText(RasterLibrary.getOnlyLayerName());
	 }



	/**
	 * Acciones ante la modificación de las capas seleccionadas en panelNorth
	 * */
	 public void propertyChange(PropertyChangeEvent evt) {
		getPanelSouth().setNameLayers(getPanelNorth().getSelectedLayers());
		getPanelSouth().updateCombo();
		getPanelSouth().updateRoisTable();
	 }


	 /**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return string con la ruta de salida
	 */
	public String getFileSelected() {
		String path = "";
		if (getRadioFile().isSelected()) {
			JFileChooser chooser = new JFileChooser("MOSAIC_DIALOG",JFileChooser.getLastPath("MOSAIC_DIALOG", null));
			chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
			//Añadimos las extensiones que hayan sido registradas en el driver
			String[] extList = GeoRasterWriter.getDriversExtensions();
			for(int i=0;i<extList.length;i++)
				chooser.addChoosableFileFilter(new ExtendedFileFilter(extList[i]));

			if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
				return null;

			JFileChooser.setLastPath("MOSAIC_DIALOG", chooser.getSelectedFile());
			ExtendedFileFilter fileFilter = (ExtendedFileFilter) chooser.getFileFilter();
			path = fileFilter.getNormalizedFilename(chooser.getSelectedFile());

		} else {
			String file = getJTextNombreCapa().getText();
			path = Utilities.createTempDirectory() + File.separator + getJTextNombreCapa().getText() + ".tif";
			if(file.compareTo(RasterLibrary.getOnlyLayerName()) == 0)
				RasterLibrary.usesOnlyLayerName();
			updateNewLayerText();
		}
		return path;
	}


	/**
	 * Acciones al presionar los botones aceptar, aplicar o cancelar
	 * Cancelar se cierra el dialogo
	 * Aceptar se comprueba las opciones seleccionadas. si la opcion de histogramMatching se encuentra seleccionada, si no se dispone del hostograma
	 * de referencia.Obtenido el histograma (con un proceso histogramProcess), se lanza el proceso de HistogramMatching para cada una de las capas seleccionadas.
	 * Una vez finalizado, se comprueba que la opción de mosaicos se encuentra activa. En caso afirmativo se lanza el proceso de mosaico con los parámetros
	 * correspondientes.
	 * */
	public void actionButtonPressed(ButtonsPanelEvent e) {
		// Al pulsar Cancelar la ventana se cierra y se refresca la vista
		if (e.getButton() == ButtonsPanel.BUTTON_CANCEL) {
			close();
		}

		if (e.getButton() == ButtonsPanel.BUTTON_ACCEPT) {
			apply();
			close();
		}

		if (e.getButton() == ButtonsPanel.BUTTON_APPLY) {
			apply();
		}
	}


	/** Método que lanza la ejecución del histogramMatchProcess */
	private void applyHistogramMatchProcess(){
		try{
			FLyrRasterSE capas[]= new FLyrRasterSE[getPanelSouth().getLayers().getLayersCount()];
			hmMasterRaster= (FLyrRasterSE) getPanelSouth().getLayers().getLayer((String)getPanelSouth().getMasterImageCombo().getSelectedItem());

			IRasterDataSource dsetCopy = null;
			dsetCopy = hmMasterRaster.getDataSource().newDataset();
			BufferFactory bufferFactory = new BufferFactory(dsetCopy);
			bufferFactory.setDrawableBands(hmMasterRaster.getRenderBands());
			if (!RasterBuffer.loadInMemory(dsetCopy))
				bufferFactory.setReadOnly(true);
			bufferFactory.setAreaOfInterest();
			Histogram histogramReference = bufferFactory.getRasterBuf().getHistogram();

			//Histogram histogramReference=hmMasterRaster.getDataSource().getHistogram();
			for(int i=0; i<getPanelSouth().getLayers().getLayersCount();i++)
				if(!hmMasterRaster.getName().equals(getPanelSouth().getLayers().getLayer(i).getName())){
					capas[i]= (FLyrRasterSE) getPanelSouth().getLayers().getLayer(i).cloneLayer();
				}
				else
					masterRasterPosition = i;

			File tempHMDir = new File(Utilities.createTempDirectory()+File.separator+"HMResults");
			tempHMDir.mkdir();

			HistogramMatchProcess proceso = new HistogramMatchProcess();
			proceso.addParam("masterHistogram",histogramReference);
			proceso.addParam("numbands",new Integer(3)); //*********************************
			proceso.addParam("inputRasterLayers",capas);
			proceso.addParam("outputPath",tempHMDir.getAbsolutePath());
			proceso.setActions(this);
			proceso.start();

		}catch (Exception e2) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "apply_histogramMatching_filter_error"), this, e2);
		}
	}



	/**
	 * Acciones que se realizan al finalizar de crear los recortes de imagen.
	 * Este método es llamado por el thread TailRasterProcess al finalizar.
	 */
	public void loadLayerInToc(String fileName) {
		if(!new File(fileName).exists())
			return;
		try {
			RasterToolsUtil.loadLayer(PluginServices.getMDIManager().getWindowInfo(view).getTitle(), fileName, null);
		} catch (RasterNotLoadException e) {
			RasterToolsUtil.messageBoxError("error_cargar_capa", this, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.rastertools.IProcessActions#end(java.lang.Object)
	 */
	public void end(Object param) {
		switch (currentProcess){
		case 0: //HM
			currentProcess = this.PREMOSAICING;
			applyHistogramMatchProcess();
			break;
		case 1: //HMS
			currentProcess = this.MOSAICING;
			FLyrRasterSE inputRasterLayers[] =  (FLyrRasterSE[]) param;
			if (applyHistogramM)
				inputRasterLayers[masterRasterPosition] = hmMasterRaster;
			if(getPanelSouth().getRButtomEdge().isSelected())
				applyFeatherPorcess((FLyrRasterSE[]) inputRasterLayers);
			else
				applyBasicProcess((FLyrRasterSE[] )inputRasterLayers);
			break;
		case 2: //MOSAICING
			currentProcess = this.LOADING;
			resultLayer = (FLyrRasterSE)param;
			loadLayer();
			//Borrar las ficheros intermedios (el resultado del H.M.)
			File tempDir = new File(Utilities.createTempDirectory()+File.separator+"HMResults");
			File filesToDelete[] = tempDir.listFiles();
			if (filesToDelete!=null)
				for (int i = 0; i<filesToDelete.length; i++)
					filesToDelete[i].delete();
			tempDir.delete();
			break;
		}
	}

	private void applyFeatherPorcess(FLyrRasterSE inputRasterLayers[]) {
		FeatherProcessBuff featherProcess= new FeatherProcessBuff();

		featherProcess.addParam("inputRasterLayers",inputRasterLayers);
		featherProcess.addParam("outputPath", getFileSelected());
		featherProcess.setActions(this);
		featherProcess.start();
	}


	private void applyBasicProcess(FLyrRasterSE inputRasterLayers[]) {
		MosaicProcess proceso= new MosaicProcess ();
		try {
			proceso.addParam("inputRasterLayers",inputRasterLayers);
			//proceso.addParam("layers",getPanelSouth().getLayers().cloneLayer());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		proceso.addParam("methodCode",new Integer(getPanelSouth().getComboOverlapFunction().getSelectedIndex()));
		proceso.addParam("outputPath",getFileSelected());
		proceso.setActions(this);
		proceso.start();
	}


	private void loadLayer() {
		view.getMapControl().getMapContext().getLayers().addLayer(resultLayer);
		view.getMapControl().getMapContext().endAtomicEvent();
		view.getMapControl().getMapContext().invalidate();
	}


	public void windowActivated() {
	}

	public void windowClosed() {
	}

	public void focusGained(FocusEvent arg0) {
	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}


	public void actionValueChanged(PropertyEvent e) {
		updateNewLayerText();
	}


	public void interrupted() {
		// TODO Auto-generated method stub

	}


	/** Acciones al aplicar */
	void apply(){
		applyHistogramM = getPanelSouth().getCheckHistogramMatching().isSelected();

		if(applyHistogramM){
			currentProcess = this.HM;
			try{
				FLyrRasterSE rasterReferencia= (FLyrRasterSE) getPanelSouth().getLayers().getLayer((String)getPanelSouth().getMasterImageCombo().getSelectedItem());
				// Si el histograma no esta calculado se calcula
				HistogramProcess histoCalculus= new HistogramProcess();
				histoCalculus.addParam("histogramable", (IHistogramable)rasterReferencia.getDataSource());
				histoCalculus.setActions(this);
				histoCalculus.start();

				// cuando el proceso termina se lanzara e HistogramMatching;

			}catch (Exception e2) {
				RasterToolsUtil.messageBoxError(PluginServices.getText(this, "error_getting_referenceHistogram"), this, e2);
			}
		}
		else{
			currentProcess = this.PREMOSAICING;
			inputRasterLayers= new FLyrRasterSE[getPanelSouth().getLayers().getLayersCount()];
			for(int i=0; i<inputRasterLayers.length;i++)
				inputRasterLayers[i]=(FLyrRasterSE) getPanelSouth().getLayers().getLayer(i);
			this.end(inputRasterLayers);
		}
	}


	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}
