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

package org.gvsig.gvsig3dgui.layer.properties;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gvsig3d.map3d.MapContext3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;


/**
* Tab with vectorial options in properties 3D panel.
* @version 
* @author Ángel Fraile Griñán.(angel.fraile@iver.es)
*/

public class VectorialLayerPanel3D extends AbstractPanel implements ItemListener {

	private static final long serialVersionUID = -4145789119404234621L;

	/**
	 * <p>Element for the interface.</p>
	 */
	
	private JPanel panelOptionsGlob;
	private JPanel panelOptionsDetail;
	private JPanel panelOptionsRast;
	private JCheckBox rasterOption;
	private JCheckBox zOption;
	private JCheckBox detailVector;
	private JCheckBox rangeOption;
	private Layer3DProps props3D;
	private TitledBorder borderOptionsRast;
	private DataInputContainer textFieldHeigth;
	private VectorialLayerListener3D vectorialLayerListener3D;
	private DataInputContainer textFieldRangeMin;
	private DataInputContainer textFieldRangeMax;
	private TitledBorder borderOptionsDetail;
	private static Logger logger = Logger.getLogger(VectorialLayerPanel3D.class
			.getName());
	
	final int integerMaxDepth = 2147483647;	//default maximum depth value
	final int defaultElevation = 100;		//default layer elevation
	final int integerMaxRange = 2147483647;	//default maximum visibility range
	final int integerMinRange = 1;			//default minimum depth value
	

	
	/**
	 * @see AbstractPanel#AbstractPanel()
	 */
	public VectorialLayerPanel3D() {
		super();
		initialize();
	}
	
	/**
	 * @see AbstractPanel#AbstractPanel(String, String, String)
	 */
	public VectorialLayerPanel3D(String id, String label, String labelGroup) {
		super(id, label, labelGroup);
		initialize();
	}
	
	private VectorialLayerListener3D getVectorialLayerListener3D() {
		if (vectorialLayerListener3D == null) {
			vectorialLayerListener3D = new VectorialLayerListener3D(this);
		}
		return vectorialLayerListener3D;
	}

	protected void initialize() {
		setLabel(PluginServices.getText(this, PluginServices.getText(this, "Capa_Vectorial")));
		setLabelGroup(PluginServices.getText(this,PluginServices.getText(this, "Propiedades_3D")));
		setPreferredSize(new Dimension(500, 400));
		resetChangedStatus();
		panelOptionsGlob = new JPanel();
		panelOptionsGlob.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(0, 0, 0, 0);
		panelOptionsGlob.add(panelRaster(), gbc);
		
		gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1.0;
		panelOptionsGlob.add(panelDetail(), gbc);
		
		this.setLayout(new BorderLayout());
		this.add(panelOptionsGlob, BorderLayout.CENTER);
		
	}
	
	private JPanel panelRaster() {
		
		if (panelOptionsRast == null) {
			panelOptionsRast = new JPanel();
			borderOptionsRast = new TitledBorder(PluginServices.getText(this, "Opciones"));
			panelOptionsRast.setLayout(new GridBagLayout());
			panelOptionsRast.setBorder(this.borderOptionsRast);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 10, 0);
			panelOptionsRast.add(getJCheckBoxRaster(),gbc);
			
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridy = 1;
			gbc.insets = new Insets(0, 0, 10, 0);
			panelOptionsRast.add(getJCheckBoxValueZ(),gbc);
			
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridy = 2;
			panelOptionsRast.add(getLabelText(), gbc);
			
			gbc = new GridBagConstraints();
			gbc.weighty = 1.0;
			gbc.gridy = 3;
			panelOptionsRast.add(new JPanel(), gbc);
		}
		return panelOptionsRast;
	}
	
	
	
	private JPanel panelDetail() {
		
		if (panelOptionsDetail == null) {
			
			panelOptionsDetail = new JPanel();
			borderOptionsDetail = new TitledBorder(PluginServices.getText(this, "Nivel_de_detalle"));
			panelOptionsDetail.setLayout(new GridBagLayout());
			panelOptionsDetail.setBorder(this.borderOptionsDetail);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 10, 0);
			panelOptionsDetail.add(getJCheckBoxDetail(),gbc);
			
			gbc.gridy = 1;
			panelOptionsDetail.add(getJCheckBoxRange(),gbc);
			
			gbc.gridy = 2;
			panelOptionsDetail.add(getLabelMinRange(), gbc);
			
			gbc.gridy = 3;
			panelOptionsDetail.add(getLabelMaxRange(), gbc);
			
			gbc = new GridBagConstraints();
			gbc.weighty = 1.0;
			gbc.gridy = 4;
			panelOptionsDetail.add(new JPanel(), gbc);
		}
		return panelOptionsDetail;
	}
	
	/**
	 * @return JCheckbox. Return the checkbox of selection of visibility range options
	 */
	private JCheckBox getJCheckBoxRange() {
		if (rangeOption == null) {
			rangeOption = new JCheckBox(PluginServices.getText(this, "Activar_intervalo"),
					false);
			rangeOption.addItemListener(this);
			rangeOption.setEnabled(false);
		}
		return rangeOption;
	}
	
	
	
	/**
	 * @return JCheckbox. Return the checkbox of selection of image options
	 */
	private JCheckBox getJCheckBoxDetail() {
		if (detailVector == null) {
			detailVector = new JCheckBox(
					PluginServices.getText(this, "Activar_profundidad_maxima"), false);
			detailVector.addItemListener(this);
			detailVector.setEnabled(false);
		}
		return detailVector;
	}
	
	
	/*
	 * Enabled options depending the Layer3DProps, and the context MapContext
	 */
	private void controlEnabled()
	{
		if(props3D.getType() == 0) {
		 if (getLayer() instanceof FLyrWMS) {
			 	textFieldHeigth.setControlEnabled(false);
			 	rasterOption.setSelected(true);
				rasterOption.setEnabled(false);
				zOption.setEnabled(false);
			}
		 else {
			textFieldHeigth.setControlEnabled(false);
			rasterOption.setEnabled(true);
			rasterOption.setSelected(true);
			zOption.setEnabled(false);
		 }
		 
		 int order = props3D.getTocOrder();
		 int aux =  this.getMapContext().getPlanet().getMaxTextureResolution(order);
		 if(aux < integerMaxDepth ) {
			 detailVector.setEnabled(true);
			 detailVector.setSelected(true);
		 }
		 
		 int actualRangeMax =  this.getMapContext().getPlanet().getMaxTextureRange(order);
		 int actualRangeMin =  this.getMapContext().getPlanet().getMinTextureRange(order);
		 
		
		 if((actualRangeMax != integerMaxRange || actualRangeMin != integerMinRange)&& (isRasterOptionSelected() && !isDetailOptionSelected())) {
			rangeOption.setEnabled(true);
			rangeOption.setSelected(true);
			String rangeMinString = Integer.toString(actualRangeMin);
			textFieldRangeMin.setValue(rangeMinString);
			String rangeMaxString = Integer.toString(actualRangeMax);
			textFieldRangeMax.setValue(rangeMaxString);
			
		 }
		}
		else if(props3D.isZEnable()) {
			
				textFieldHeigth.setControlEnabled(false);
				rasterOption.setEnabled(false);
				zOption.setEnabled(true);
				zOption.setSelected(true);
			}
			else {						
				textFieldHeigth.setControlEnabled(true);
				rasterOption.setEnabled(true);
				zOption.setEnabled(true);
			}

	}
	/*
	 * Height layer data input container 
	 */
	
	private DataInputContainer getLabelText() {
		if(textFieldHeigth == null)
		{
			textFieldHeigth = new DataInputContainer();
			textFieldHeigth.setLabelText(PluginServices.getText(this,"Altura_en_metros"));
			textFieldHeigth.setValue(this.getMyHeight());
		}
		
		return textFieldHeigth;
	}
	
	
	
	private DataInputContainer getLabelMaxRange() {
		if(textFieldRangeMax == null)
		{
			textFieldRangeMax = new DataInputContainer();
			textFieldRangeMax.setLabelText(PluginServices.getText(this,"Rango_maximo"));
			textFieldRangeMax.setControlEnabled(false);
			//if(!this.getMyRangeMax().equals(String.valueOf(integerMaxRange)))
				textFieldRangeMax.setValue(this.getMyRangeMax());
			//else
			//	textFieldRangeMax.setValue("0");
		}	
		
		return textFieldRangeMax;
	}
	
	
	private DataInputContainer getLabelMinRange() {
		if(textFieldRangeMin == null)
		{
			textFieldRangeMin = new DataInputContainer();
			textFieldRangeMin.setLabelText(PluginServices.getText(this,"Rango_minimo"));
			textFieldRangeMin.setControlEnabled(false);
		//	if(!this.getMyRangeMin().equals(String.valueOf(integerMinRange)))
				textFieldRangeMin.setValue(this.getMyRangeMin());
		//	else
		//		textFieldRangeMin.setValue("0");
		}
		
		return textFieldRangeMin;
	}
	
	
	
	/*
	 * @return JCheckbox rasterOption
	 *
	 */
	private JCheckBox getJCheckBoxRaster() {
		if (rasterOption == null) {
			rasterOption = new JCheckBox(
					PluginServices.getText(this, "Raster_layer"), false);
			rasterOption.addItemListener(this);
		}
		return rasterOption;
	}
	
	/*
	 * @return JCheckbox zOptions
	 *
	 */
	
	private JCheckBox getJCheckBoxValueZ() {
		if (zOption == null) {
			zOption = new JCheckBox(PluginServices.getText(this, "Heigth_Z"),
					false);
			zOption.addItemListener(this);
		}
		return zOption;
	}


	public void selected() {
	
	}

	/**
	 * Logic with active or inactive options
	 */
	
	public void itemStateChanged(ItemEvent e) {
		
		int order = props3D.getTocOrder();
		int actualDepth =  this.getMapContext().getPlanet().getMaxTextureResolution(order);
//		int actualRangeMax =  this.getMapContext().getPlanet().getMaxTextureRange(order);
//		int actualRangeMin =  this.getMapContext().getPlanet().getMinTextureRange(order);
		
		zOption.setEnabled(!rasterOption.isSelected());
		rasterOption.setEnabled(!zOption.isSelected());
		textFieldHeigth.setControlEnabled(!(zOption.isSelected()) && !(rasterOption.isSelected()));
		detailVector.setEnabled(rasterOption.isSelected()&& !rangeOption.isSelected());
		rangeOption.setEnabled(rasterOption.isSelected() && !detailVector.isSelected());
		textFieldRangeMin.setControlEnabled(rangeOption.isSelected()&& !detailVector.isSelected());
		textFieldRangeMax.setControlEnabled(rangeOption.isSelected() && !detailVector.isSelected());

		
		if(detailVector.isSelected()) {
			 if(actualDepth != integerMaxDepth) {
					detailVector.setSelected(true);
			 }
		}
		else {
			detailVector.setSelected(false);
		}
		
		if(rangeOption.isSelected()) {
			 textFieldRangeMin.setValue(getMyRangeMin());
			 textFieldRangeMax.setValue(getMyRangeMax());
		
//			 if(actualRangeMax != integerMaxRange || actualRangeMin != integerMinRange) {
//				 String rangeMinString = Integer.toString(actualRangeMin);
//				 textFieldRangeMin.setValue(rangeMinString);
//				 String rangeMaxString = Integer.toString(actualRangeMax);
//				 textFieldRangeMax.setValue(rangeMaxString);
//			 }
//				else {
//					textFieldRangeMin.setValue("0");
//					textFieldRangeMax.setValue("0");
//				}
		}
		
	}
	
	
	/*
	 * Calling the listener
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void accept() {
		getVectorialLayerListener3D().accept();
	}

	/*
	 * Calling the listener
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void apply() {
		getVectorialLayerListener3D().apply();
	}
	
	/*
	 * Calling the listener
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void cancel() {
		getVectorialLayerListener3D().cancel();
	}
	
	/**
	 * Option range detail option selected
	 * @return boolean
	 */
	public boolean isRangeOptionSelected() {
		return getJCheckBoxRange().isSelected();
	}
	
	
	/**
	 * Option raster selected
	 * @return boolean
	 */
	public boolean isRasterOptionSelected() {
		return getJCheckBoxRaster().isSelected();
	}
	
	/**
	 * Option to work with Z values
	 * @return boolean
	 */
	public boolean isZValueSelected() {
		return getJCheckBoxValueZ().isSelected();
	}
	
	/**
	 * Option level of detail selected
	 * @return boolean
	 */
	public boolean isDetailOptionSelected() {
		return getJCheckBoxDetail().isSelected();
	}
	
	/**
	 * Getting the user height introduced 
	 * @return actual height
	 */
	public String getJTextFieldHeight() {
		return textFieldHeigth.getValue();
	}
	
	
	public String getJTextFieldRangeMax() {
		return textFieldRangeMax.getValue();
	}
	
	public String getJTextFieldRangeMin() {
		return textFieldRangeMin.getValue();
	}
	
	/**
	 * Getting the 3D properties
	 * @return Layer3DProps
	 */
	public Layer3DProps getLayer3DProps() {
		return props3D;
	}

	/**
	 * Getting the map context
	 * @return MapContext3D
	 */
	public MapContext3D getMapContext() {
		FLayer fl = (FLayer)this.getReference();
		MapContext3D mc = (MapContext3D)fl.getMapContext();
		return mc;
	}
	
	/**
	 * Getting the selected layer
	 * @return Flayer
	 */
	public FLayer getLayer() {
		return (FLayer)this.getReference();
	}

	/**
	 * Getting the actual layer height
	 * @return String.
	 */
	public String getMyHeight() {
		String elevation = "0";//default value
		try {
			float alt = props3D.getHeigth();
			elevation = Float.toString(alt);
		}
		catch (Exception e) {
			logger.error("Command: "
					+ "Error getting layer elevation value.", e);
		}
		return elevation;
	}
	
	/*
	 * Return actual least texture range as String
	 */
	private String getMyRangeMax() {
		String rangeMax = String.valueOf(integerMaxRange);;
		try {
			int order = props3D.getTocOrder();
			int alt =  this.getMapContext().getPlanet().getMaxTextureRange(order);
			rangeMax = Integer.toString(alt);
		}
		catch (Exception e) {
//			logger.error("Command: "
//					+ "Error getting maximun range layer elevation value.", e);
		}
		return rangeMax;
	}
	
	
	/*
	 * Return actual minim texture range as String
	 */
	private String getMyRangeMin() {
		String rangeMin = String.valueOf(integerMinRange);
		try {
			int order = props3D.getTocOrder();
			int alt =  this.getMapContext().getPlanet().getMinTextureRange(order);
			rangeMin = Integer.toString(alt);
		}
		catch (Exception e) {
//			logger.error("Command: "
//					+ "Error getting least range layer elevation value.", e);
		}
		return rangeMin;
	}
	
	/**
	 * Getting the actual height in the model
	 * @return height
	 */
	public double getHightLayer() {
		return props3D.getHeigth();
	}
	
	/**
	 * Getting the actual texture maxim range in the model
	 * @return max range
	 */
	public int getDefaultMaxRange(){
		return integerMaxRange;
	}
	
	/**
	 * Getting the actual texture minim range in the model
	 * @return height
	 */
	public int getDefaultMinRange(){
		return integerMinRange;
	}
	
	/**
	 * Getting the actual texture maxim range in the model
	 * @return height
	 */
	public int getDefaultDepth(){
		return integerMaxDepth;
	}
	
	/**
	 * Getting the actual layer elevation in the model
	 * @return elevation
	 */
	public int getDefaultElevation(){
		return defaultElevation;
	}
	
	/**
	 * Getting the objet(the selected layer). Updating the panel with the 
	 * actual properties of the model.
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object arg0) {
		super.setReference(arg0);
		props3D = Layer3DProps.getLayer3DProps((FLayer)this.getReference());
		updatePanel();
	}
	
	/*
	 * Setting data model when the panel is built
	 */
	protected void updatePanel() {
		textFieldHeigth.setValue(this.getMyHeight());
		controlEnabled();
	}
	
}