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

package org.gvsig.remotesensing.gridmath.gui;

import info.clearthought.layout.TableLayout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterToolsUtil;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Panel de configuracion de las opciones de salida.
 * Puede ajustarse el extent de salida y las opciones habituales de salvado de la
 * capa.
 *
 * @author Alejandro Muñoz Sánchez (alejandro.munoz@uclm.es)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 * @version 22/10/2007
 */
public class OutputOptionsPanel extends JPanel implements ActionListener,FocusListener,KeyListener,PropertyListener {

	private static final long serialVersionUID = 1L;
	private JPanel namePanel = null;
	private JPanel panelExtension = null;
	private JPanel paramPanel = null;

	private JTextField jTextNombreCapa = null;
	private JTextField  jTextRangoX1 = null;
	private JTextField  jTextRangoX2 = null;
	private JTextField jTextRangoY1 = null;
	private JTextField jTextRangoY2 = null;
	private JTextField jTextCellSizeX = null;
	private JTextField jTextCellSizeY = null;
	private JTextField  jTextNumFiCol1 = null;
	private JTextField jTextNumFiCol2 = null;
	private FLayers layers = null;

	private JRadioButton rButtom1 = null;
	private JRadioButton rButtom2 = null;
	private JRadioButton rButtom4 = null;
	private JRadioButton rButtonFile = null;
	private JRadioButton rButtonMemory = null;
	private JComboBox jComboCapas = null;
	private MapContext mapContext = null;
	private GridMathPanel cp = null;


	/**
	* Constructor
	* @param vista vista de la aplicación
	* @param cp calculatorPanel desde el que se invoca
	*/
	public OutputOptionsPanel(View vista, GridMathPanel cp) {
		super();
		if (vista!=null){
			mapContext = vista.getModel().getMapContext();
			layers = mapContext.getLayers();
		}
		this.cp=cp;
		Inicializar();
	}

	/**
	 * Inicialización del panel
	 */
	public void  Inicializar(){
		BorderLayout bd=new BorderLayout();
		this.setLayout(bd);
		this.setBorder( new EmptyBorder(2, 2, 2, 2));
		this.add(getNamePanel(),BorderLayout.NORTH);
		this.add(getPanelExtension(),BorderLayout.WEST);
		this.add(getParameterPanel(),BorderLayout.CENTER);
		getRadioMemory().setSelected(true);
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
	 * @return JRadioButton de generar en memoria
	 */
	public JRadioButton getRadioMemory(){
		if (rButtonMemory == null){
			rButtonMemory = new JRadioButton(PluginServices.getText(this,"a_memoria"));
			rButtonMemory.addActionListener(this);
		}
		return rButtonMemory;
	}


	/**
	 * @return panel que incluye el nombre de la capa y las opciones de almacenamieto de la capa de salida
	 */
	public JPanel getNamePanel() {

		if (namePanel==null){
			namePanel=new JPanel();
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

			//Establece la separacin entre los elementos
			namePanel.setLayout(new GridBagLayout());

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			namePanel.add(new JLabel(PluginServices.getText(this,"nombre_capa")),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			namePanel.add(getJTextNombreCapa(),gridBagConstraints);

			gridBagConstraints = new java.awt.GridBagConstraints();
			gridBagConstraints.gridx = 2;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
			namePanel.add(radioPanel,gridBagConstraints);
		}
		return namePanel;
	}


	 /**
	  * @return javax.swing.JTextField
	  */
	 public JTextField getJTextNombreCapa() {
		if (jTextNombreCapa==null) {
			jTextNombreCapa=new JTextField(10);
			jTextNombreCapa.setText(RasterLibrary.getOnlyLayerName());
			jTextNombreCapa.addFocusListener(this);
			RasterLibrary.addOnlyLayerNameListener(this);
		}
		return jTextNombreCapa;
	}




	/**
	* @return panel con las opciones de configuración de extent,
	* los radioButton y el comboCapas
	*/
	public JPanel getPanelExtension() {

		if (panelExtension==null){
			panelExtension=new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"extension_from")));
		    topBorder.setTitlePosition(TitledBorder.TOP);
		    panelExtension.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,6,5)));

		    JPanel p=new JPanel();
		    //p.setPreferredSize(new Dimension(200,130));
				TableLayout thisLayout = new TableLayout(new double[][] {
						{200},
						{TableLayout.PREFERRED,TableLayout.PREFERRED,TableLayout.PREFERRED, TableLayout.PREFERRED}});
						//Establece la separacin entre los elementos
						thisLayout.setHGap(5);
						thisLayout.setVGap(5);
						panelExtension.setLayout(thisLayout);
						p.setLayout(thisLayout);
						ButtonGroup buttonGroup = new ButtonGroup();
						buttonGroup.add(getRButtom1());
						buttonGroup.add(getRButtom2());
						buttonGroup.add(getRButtom4());
						panelExtension.add(getRButtom1(),"0,0");
						panelExtension.add(getRButtom2(),"0,1");
						panelExtension.add(getRButtom4(),"0,2");
						panelExtension.add(getJComboCapas(),"0,3");

		}

		return panelExtension;
	}


	/**
	 * @return panel con los parametros de configuración de extensión de la salida raster.
	 */
	public JPanel getParameterPanel() {


		if (paramPanel==null){
			paramPanel=new JPanel();
			TitledBorder topBorder = BorderFactory.createTitledBorder((PluginServices.getText(this,"parametros")));
		    topBorder.setTitlePosition(TitledBorder.TOP);
		    paramPanel.setBorder(new CompoundBorder(topBorder,new EmptyBorder(5,5,6,5)));


		    JPanel p=new JPanel();
		   // p.setPreferredSize(new Dimension(320,130));
			TableLayout thisLayout = new TableLayout(new double[][] {
				 {150,75, 75},
				{20,20,20,20}});
				//Establece la separacin entre los elementos
				thisLayout.setHGap(3);
				thisLayout.setVGap(3);
				p.setLayout(thisLayout);

				//Aado los diferentes elementos
				p.add(new JLabel((PluginServices.getText(this,"rangox"))),"0,0");
				p.add(new JLabel((PluginServices.getText(this,"rangoy"))),"0,1");
				p.add(new JLabel((PluginServices.getText(this,"tamanio_celda"))),"0,2");
				p.add(new JLabel((PluginServices.getText(this,"num_filas_columnas"))),"0,3");
				p.add(getJTextCellSizeX(),"1,2");
				p.add(getJTextCellSizeY(),"2,2");
				p.add(getJTextNumFiCol1(),"1,3");
				p.add(getJTextNumFiCol2(),"2,3");
				p.add(getJTextRangoX1(),"1,0");
				p.add(getJTextRangoX2(),"2,0");
				p.add(getJTextRangoY1(),"1,1");
				p.add(getJTextRangoY2(),"2,1");

				paramPanel.add(p);
		}
		return paramPanel;
	}



	/**
	 * @return JRadioButton  ajustar a datos de entrada
	 */
	public JRadioButton getRButtom1() {
		if(rButtom1==null){
			rButtom1=new JRadioButton((PluginServices.getText(this,"ajustar_entrada")), true);
			rButtom1.addActionListener(this);
			}
		return rButtom1;
	}



	/**
	 * @return JRadioButton extension definida por el  usuario
	 */
	public JRadioButton getRButtom2() {
		if(rButtom2==null){
			rButtom2=new JRadioButton((PluginServices.getText(this,"definida_usuario")), false);
			rButtom2.addActionListener(this);
		}
		return rButtom2;
	}



	/**
	 * @return JRadioButton extension ajustada a otra capa
	 */
	public JRadioButton getRButtom4() {
		if(rButtom4==null){
			rButtom4=new JRadioButton((PluginServices.getText(this,"extension_capa")), false);
			rButtom4.addActionListener(this);
		}
		return rButtom4;
	}


	/**
	 * @return JTextField tamaño de celda en X
	 */
	public JTextField getJTextCellSizeX() {
		if (jTextCellSizeX==null){
			jTextCellSizeX=new JTextField(15);;
			jTextCellSizeX.setEditable(false);
			jTextCellSizeX.addKeyListener(this);
		}
		return jTextCellSizeX;
	}

	/**
	 * @return JTextField tamaño de celda en Y
	 */
	public JTextField getJTextCellSizeY() {
		if (jTextCellSizeY==null){
			jTextCellSizeY=new JTextField(15);;
			jTextCellSizeY.setEditable(false);
			jTextCellSizeY.addKeyListener(this);
		}
		return jTextCellSizeY;
	}



	/**
	 * @return JTextField numero de filas
	 */
	public JTextField getJTextNumFiCol1() {
		if (jTextNumFiCol1==null){
			jTextNumFiCol1=new JTextField ();
			jTextNumFiCol1.setEditable(false);
		}
		return jTextNumFiCol1;
	}



	/**
	 * @return JTextField coordenada x mínima
	 */
	public JTextField  getJTextRangoX1() {
		if (jTextRangoX1==null){
			jTextRangoX1 =new JTextField ();
			jTextRangoX1.setEditable(false);
			jTextRangoX1.addKeyListener(this);

		}
		return jTextRangoX1;
	}



	/**
	 * @return JTextField coordenada x máxima
	 */
	public JTextField getJTextRangoX2() {
		if (jTextRangoX2==null){
			jTextRangoX2=new JTextField ();
			jTextRangoX2.setEditable(false);
			jTextRangoX2.addKeyListener(this);

		}
		return jTextRangoX2;
	}




	/**
	 * @return JTextField coordenada y mímnima
	 */
	public JTextField  getJTextRangoY1() {
		if (jTextRangoY1==null){
			jTextRangoY1=new JTextField ();
			jTextRangoY1.setEditable(false);
			jTextRangoY1.addKeyListener(this);
			}
		return jTextRangoY1;
	}



	/**
	 * @return JTextField coordenada y máxima
	 */
	public JTextField  getJTextRangoY2() {
		if (jTextRangoY2==null){
			jTextRangoY2=new JTextField ();
			jTextRangoY2.setEditable(false);
			jTextRangoY2.addKeyListener(this);
		}
		return jTextRangoY2;
	}




	/**
	 * @return JTextField numero de columnas
	 */
	public JTextField getJTextNumFiCol2() {
		if (jTextNumFiCol2==null){
			jTextNumFiCol2=new JTextField ();
			jTextNumFiCol2.setEditable(false);
		}
		return jTextNumFiCol2;
	}



	/**
	 * @return JCombo con las capas raster cargadas en la vista
	 */
	public JComboBox getJComboCapas() {
		if (jComboCapas==null){
			ComboBoxModel jComboBoxLayersModel = new DefaultComboBoxModel(getLayerNames());
			jComboCapas = new JComboBox();
			jComboCapas.setModel(jComboBoxLayersModel);
			jComboCapas.setEnabled(false);
			jComboCapas.addActionListener(this);
		}
		return jComboCapas;
	}

	/**
	 * @return array con el nombre de los ficheros cargados en la vista
	 */
	private String[] getLayerNames() {
		String[] sNames = {};
		if (layers!=null){
			sNames = new String[layers.getLayersCount()];
			for (int i = 0; i < layers.getLayersCount(); i++) {
				sNames[i] = (layers.getLayer(i)).getName();
			}
		}
		return sNames;
	}


	/**
	 * Establece la opción por defecto cuando se crea en cuadro de dialogo
	 */
	public void InicializarOpcion(){

		DesabilitarTodo();
		rButtom1.setSelected(true);
		// Comprobar que todas las variables estan asignadas
		boolean allAssigned = false;
		for (int i=0; i<cp.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount(); i++){
			allAssigned = true;
			if (cp.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(i,1).toString().equals(""))
			{
				allAssigned=false;
				break;
			}
		}
		if(allAssigned){
		// Establecemos la opcion por defecto.
		setAjustInDataExtent();
		}
	}


	/**
	* Deshabilita todos los componetes variables de la interfaz
	*/
	public void DesabilitarTodo(){

		jComboCapas.setEnabled(false);
		jTextRangoX1.setEditable(false);
		jTextRangoX1.setEnabled(false);
		jTextRangoX2.setEditable(false);
		jTextRangoX2.setEnabled(false);
		jTextRangoY1.setEditable(false);
		jTextRangoY1.setEnabled(false);
		jTextRangoY2.setEditable(false);
		jTextRangoY2.setEnabled(false);
		jTextNumFiCol1.setEditable(false);
		jTextCellSizeX.setEditable(false);
		jTextCellSizeX.setEnabled(false);
		jTextCellSizeY.setEditable(false);
		jTextCellSizeY.setEnabled(false);
		jTextNumFiCol1.setEnabled(false);
		jTextNumFiCol2.setEditable(false);
		jTextNumFiCol2.setEnabled(false);
		jComboCapas.updateUI();

	}


	/**
	 * Reestablece el numero de filas y columnas ante cuanquier variación de la interfaz
	 */
	public void extentHasChanged(){

		double dRangeX;
		double dRangeY;
		double dCellSizeX;
		double dCellSizeY;
		int iRows;
		int iCols;
		// Se actualiza la X
		try {
			dRangeX = Math.abs(Double.parseDouble(getJTextRangoX2().getText())
								- Double.parseDouble(getJTextRangoX1().getText()));
			dCellSizeX = Double.parseDouble(getJTextCellSizeX().getText());
			iCols = (int) Math.floor(dRangeX / dCellSizeX);
			getJTextNumFiCol2().setText(Integer.toString(iCols));

			// Se actualiza la Y
			dRangeY = Math.abs(Double.parseDouble(getJTextRangoY2().getText())
					- Double.parseDouble(getJTextRangoY1().getText()));
			dCellSizeY = Double.parseDouble(getJTextCellSizeY().getText());
			iRows = (int) Math.floor(dRangeY / dCellSizeY);
			getJTextNumFiCol1().setText(Integer.toString(iRows));


		} catch (NumberFormatException e) {
			RasterToolsUtil.messageBoxError(PluginServices.getText(this, "invalid_number"), this);
		}
}

	private void validateKeyTyping(KeyEvent event) {
		jComboCapas.updateUI();
		switch (event.getKeyChar()) {

			case KeyEvent.VK_ENTER:
				extentHasChanged();
				jComboCapas.updateUI();
				break;

			case KeyEvent.VK_BACK_SPACE:
				extentHasChanged();
				break;
			case KeyEvent.VK_0 :
				extentHasChanged();
				break;
			case KeyEvent.VK_1 :
				extentHasChanged();
				break;
			case KeyEvent.VK_2 :
				extentHasChanged();
				break;
			case KeyEvent.VK_3:
				extentHasChanged();
				break;
			case KeyEvent.VK_4:
				extentHasChanged();
				break;
			case KeyEvent.VK_5:
				extentHasChanged();
				break;
			case KeyEvent.VK_6:
				extentHasChanged();
				break;
			case KeyEvent.VK_7:
				extentHasChanged();
				break;
			case KeyEvent.VK_8:
				extentHasChanged();
				break;
			case KeyEvent.VK_9 :
				extentHasChanged();
				break;
		}
	}



	/**
	* Fija las acciones que se realizan cuando se produce un evento
	* @param e Objeto que genera el evento
	*/
	public void actionPerformed(ActionEvent e) {

		//Radiobutton1
		if (e.getSource()==getRButtom1()){
			DesabilitarTodo();
			setAjustInDataExtent();
			updateParams();
		}

		// RadioButtom2
		else  if (e.getSource()==getRButtom2()){

			DesabilitarTodo();
			rButtom2.setSelected(true);
			getJTextRangoX1().setEnabled(true);
			getJTextRangoX1().setEditable(true);
			getJTextRangoX2().setEnabled(true);
			getJTextRangoX2().setEditable(true);
			getJTextRangoY1().setEnabled(true);
			getJTextRangoY1().setEditable(true);
			getJTextRangoY2().setEditable(true);
			getJTextRangoY2().setEnabled(true);
			getJTextCellSizeX().setEditable(true);
			getJTextCellSizeX().setEnabled(true);
			getJTextCellSizeY().setEditable(true);
			getJTextCellSizeY().setEnabled(true);
			updateParams();
		}


		// RadioButtom4
		else  if (e.getSource()==getRButtom4()){

			DesabilitarTodo();
			rButtom4.setSelected(true);
			jComboCapas.setEnabled(true);
			jTextCellSizeX.setEditable(true);
			jTextCellSizeX.setEnabled(true);
			jTextCellSizeY.setEditable(true);
			jTextCellSizeY.setEnabled(true);
			getJComboCapas().updateUI();
			updateParams();
		}

		else  if (e.getSource()==getRadioFile()){
			rButtonFile.setSelected(true);
		}

		else  if (e.getSource()==getRadioMemory()){
			rButtonMemory.setSelected(true);
		}

		// ComboCapas
		else if (e.getSource()==getJComboCapas()){
			updateParams();

		}
	}

	/**
	 *  Actualizacion de los parametros ajustandolos a los de la capa seleccionada
	 */
	private void updateParams(){
		double dCoord;
		if (getRButtom4().isSelected()){
			try {
				FLyrRasterSE rasterLayer = (FLyrRasterSE)mapContext.getLayers().getLayer((String)getJComboCapas().getSelectedItem());
				dCoord = rasterLayer.getFullEnvelope().getMinimum(0);
				getJTextRangoX1().setText(new Double(dCoord).toString());
				dCoord = rasterLayer.getFullEnvelope().getMaximum(0);
				getJTextRangoX2().setText(new Double(dCoord).toString());
				dCoord = rasterLayer.getFullEnvelope().getMinimum(1);
				getJTextRangoY1().setText(new Double(dCoord).toString());
				dCoord = rasterLayer.getFullEnvelope().getMaximum(1);
				getJTextRangoY2().setText(new Double(dCoord).toString());
				if (rasterLayer instanceof FLyrRasterSE){
					getJTextCellSizeX().setText(new Double(
							Math.abs(((FLyrRasterSE) rasterLayer).getAffineTransform().getScaleX()))
							.toString());
					getJTextCellSizeY().setText(new Double(
							Math.abs(((FLyrRasterSE) rasterLayer).getAffineTransform().getScaleY()))
							.toString());
				}
				extentHasChanged();
			} catch (Exception ex) {}
		}

	}



	/**
	 * Establece el extent de salida ajustandolo a los datos de entrada.
	 */
	private void setAjustInDataExtent(){
		// Si no hay variables asignadas no hace nada. No dejara lanzar el calculo
		if( cp.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount()>0 && cp.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(0,1).toString()!=""){

			String layerName = cp.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(0,1).toString();
			layerName = layerName.substring(0,layerName.indexOf("["));
			FLyrRasterSE rasterLayer = (FLyrRasterSE)mapContext.getLayers().getLayer(layerName);

			double xMin =rasterLayer.getFullRasterExtent().minX();
			double xMax =rasterLayer.getFullRasterExtent().maxX();
			double yMin =rasterLayer.getFullRasterExtent().minY();
			double yMax =rasterLayer.getFullRasterExtent().maxY();
			double cellSizeX = Math.abs(rasterLayer.getAffineTransform().getScaleX());
			double cellSizeY = Math.abs(rasterLayer.getAffineTransform().getScaleY());

			for (int i=0;i<cp.getCalculatorPanel().getJTableVariables().getTableFormat().getRowCount();i++){
				layerName = cp.getCalculatorPanel().getJTableVariables().getTableFormat().getValueAt(i,1).toString();
				layerName = layerName.substring(0,layerName.indexOf("["));
				rasterLayer = (FLyrRasterSE)mapContext.getLayers().getLayer(layerName);

				xMin = Math.min(xMin,rasterLayer.getFullRasterExtent().minX());
				xMax = Math.max(rasterLayer.getFullRasterExtent().maxX(),xMax);
				yMin = Math.min(yMin,rasterLayer.getFullRasterExtent().minY());
				yMax = Math.max(yMax,rasterLayer.getFullRasterExtent().maxY());
				cellSizeX = Math.min(cellSizeX,Math.abs(rasterLayer.getAffineTransform().getScaleX()));
				cellSizeY = Math.min(cellSizeY,Math.abs(rasterLayer.getAffineTransform().getScaleY()));
			}

			cp.getOutputExtent().setXRange(xMin,xMax);
			cp.getOutputExtent().setYRange(yMin,yMax);

			getJTextRangoX1().setText(String.valueOf(cp.getOutputExtent().minX()));
			getJTextRangoX2().setText(String.valueOf(cp.getOutputExtent().maxX()));
			getJTextRangoY1().setText(String.valueOf(cp.getOutputExtent().minY()));
			getJTextRangoY2().setText(String.valueOf(cp.getOutputExtent().maxY()));
			getJTextCellSizeX().setText(String.valueOf(cellSizeX));
			getJTextCellSizeY().setText(String.valueOf(cellSizeY));
			cp.getOutputExtent().setCellSizeX(cellSizeX);
			cp.getOutputExtent().setCellSizeY(cellSizeY);
			extentHasChanged();
		}
	}

	/**
	 * Eventos del teclado
	 */
	public void keyReleased(KeyEvent e) {

		if(e.getSource()==getJTextRangoX1()){
			validateKeyTyping(e);
		}

		if(e.getSource()==getJTextRangoX2()){
			validateKeyTyping(e);
		}

		if(e.getSource()==getJTextRangoY1()){
			validateKeyTyping(e);
		}

		if(e.getSource()==getJTextRangoY2()){
			validateKeyTyping(e);
		}

		if (e.getSource()==getJTextCellSizeX()){

			validateKeyTyping(e);
		}

		if (e.getSource()==getJTextCellSizeY()){

			validateKeyTyping(e);
		}
	}

	/**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignándo
	 * en cada llamada un nombre consecutivo.
	 */
	 public void updateNewLayerText() {
		 	getJTextNombreCapa().setText(RasterLibrary.getOnlyLayerName());
	 }




	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}



	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}



	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void actionValueChanged(PropertyEvent e) {
		updateNewLayerText();
	}
}