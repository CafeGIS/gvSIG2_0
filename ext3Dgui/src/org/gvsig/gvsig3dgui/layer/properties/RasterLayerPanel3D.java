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

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.datainput.DataInputContainer;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gvsig3d.map3d.MapContext3D;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;


/**
* 
* Tab with raster options in properties 3D panel.
* @version 
* @author Ángel Fraile Griñán.
*/
public class RasterLayerPanel3D extends AbstractPanel implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2459859018671566919L;
	/**
	 * <p>Element for the interface.</p>
	 */
	
	
	private JPanel panelOptionsRast;
	private JPanel panelOptionsOff;
	private JPanel panelOptionsGlob;
	private TitledBorder borderOptionsRast;
	private TitledBorder borderOptionsOff;
	
	private JCheckBox imageButton;
	private JCheckBox elevationButton;
	private Layer3DProps props3D;
	private DataInputContainer textFieldOffset;
	private DataInputContainer textFieldFactor;
	private RasterLayerListener3D rasterLayerListener3D;
	private JPanel panelOptionsDetail;
	private JCheckBox detailVector;
	private TitledBorder borderOptionsDetail;
	private JCheckBox rangeOption;
	private DataInputContainer textFieldRangeMin;
	private DataInputContainer textFieldRangeMax;
	
	final int integerMaxRange = 2147483647;	//default maximum visibility range
	final int integerMinRange = 1;			//default minimum depth value
	
	/**
	 * @see AbstractPanel#AbstractPanel()
	 */
	public RasterLayerPanel3D() {
		super();
		initialize();
	}
	
	/**
	 * @see AbstractPanel#AbstractPanel(String, String, String)
	 */
	public RasterLayerPanel3D(String id, String label, String labelGroup) {
		super(id, label, labelGroup);
		initialize();
	}
	
	private RasterLayerListener3D getRasterLayerListener3D() {
		if (rasterLayerListener3D == null) {
			rasterLayerListener3D = new RasterLayerListener3D(this);
		}
		return rasterLayerListener3D;
	}
	
	/**
	 * Creating raster 3D properties
	 */
	
	protected void initialize() {

		setLabel(PluginServices.getText(this, PluginServices.getText(this, "Capa_Raster")));
		setLabelGroup(PluginServices.getText(this, PluginServices.getText(this, "Propiedades_3D")));
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
		panelOptionsGlob.add(panelOffset(), gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		panelOptionsGlob.add(panelDetail(), gbc);

		this.setLayout(new BorderLayout());
		this.add(panelOptionsGlob, BorderLayout.CENTER);
		
	}
	
	
	/**
	 * Creating subpanel with representation raster option
	 * @return JPanel panelOptionRast
	 */
	
	private JPanel panelRaster() {
		if (panelOptionsRast == null) {
			
			panelOptionsRast = new JPanel();
			borderOptionsRast = new TitledBorder(PluginServices.getText(this, "Opciones_de_imagen"));
			panelOptionsRast.setLayout(new GridBagLayout());
			panelOptionsRast.setBorder(this.borderOptionsRast);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			gbc.insets = new Insets(0, 0, 0, 0);
			panelOptionsRast.add(getJCheckBoxTerrain(),gbc);
			
		}
		return panelOptionsRast;
	}
	
	/**
	 * Creating subpanel with raster option
	 * @return JPanel panelOptionRast
	 */
	
	private JPanel panelOffset() {
		
		if (panelOptionsOff == null) {
			
			panelOptionsOff = new JPanel();
			borderOptionsOff = new TitledBorder(PluginServices.getText(this, "Opciones_de_elevación"));
			panelOptionsOff.setLayout(new GridBagLayout());
			panelOptionsOff.setBorder(this.borderOptionsOff);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			gbc.insets = new Insets(0, 0, 10, 0);
			panelOptionsOff.add(getJCheckBoxElevation(),gbc);
			
			textFieldOffset = new DataInputContainer();
			textFieldOffset.setLabelText("Offset");
			textFieldOffset.setControlEnabled(false);
			gbc.gridy = 1;
			panelOptionsOff.add(textFieldOffset, gbc);
			
			textFieldFactor = new DataInputContainer();
			textFieldFactor.setLabelText("Factor");
			textFieldFactor.setControlEnabled(false);
			gbc.gridy = 2;
			panelOptionsOff.add(textFieldFactor, gbc);
		}
		return panelOptionsOff;
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
		//	panelOptionsDetail.add(getJCheckBoxDetail(),gbc);
			
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
	
	private JCheckBox getJCheckBoxRange() {
		if (rangeOption == null) {
			rangeOption = new JCheckBox(PluginServices.getText(this, "Activar_intervalo"),
					false);
			rangeOption.addItemListener(this);
			rangeOption.setEnabled(true);
		}
		return rangeOption;
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
	 * @return JCheckbox. Return the checkbox of selection of image options
	 */
	private JCheckBox getJCheckBoxDetail() {
		if (detailVector == null) {
			detailVector = new JCheckBox(
					PluginServices.getText(this, "Activar"), false);
			detailVector.addItemListener(this);
		}
		return detailVector;
	}
	
	/**
	 * @return JCheckbox. Return the checkbox of selection of image options
	 */
	private JCheckBox getJCheckBoxTerrain() {
		if (imageButton == null) {
			imageButton = new JCheckBox(
					PluginServices.getText(this, "Pegado_sobre_el_terreno"), false);
			imageButton.addItemListener(this);
		}
		return imageButton;
	}
	
	/**
	 * @return JCheckbox. Return the checkbox of selection of elevation options
	 *
	 */
	private JCheckBox getJCheckBoxElevation() {
		if (elevationButton == null) {
			elevationButton = new JCheckBox(PluginServices.getText(this, "Elevacion"),
					false);
			elevationButton.addItemListener(this);
		}
		return elevationButton;
	}
		
	/*
	 * Calling the listener
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void accept() {
		getRasterLayerListener3D().accept();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#apply()
	 */
	public void apply() {
		getRasterLayerListener3D().apply();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#cancel()
	 */
	public void cancel() {
		getRasterLayerListener3D().cancel();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#selected()
	 */
	public void selected() {
		
	}

	/**
	 * Ekevation option selected.
	 * @return boolean
	 */
	public boolean isElevationOptionSelected() {
		return getJCheckBoxElevation().isSelected();
	}

	public boolean isTerrainOptionSelected() {
		return getJCheckBoxTerrain().isSelected();
	}
	
	/**
	 * Logic with active or innactive options.
	 */
	public void itemStateChanged(ItemEvent e) {
		
		elevationButton.setEnabled(!imageButton.isSelected());
		imageButton.setEnabled(!elevationButton.isSelected());
		textFieldFactor.setControlEnabled(elevationButton.isSelected());
		textFieldOffset.setControlEnabled(elevationButton.isSelected());
		textFieldRangeMin.setControlEnabled(rangeOption.isSelected());
		textFieldRangeMax.setControlEnabled(rangeOption.isSelected());
		
	}
	
	public String getJTextFieldOffset() {
		return textFieldOffset.getValue();
	}
	
	public String getJTextFieldFactor() {
		return textFieldFactor.getValue();
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
	 * Option level of detail selected
	 * @return boolean
	 */
	public boolean isDetailOptionSelected() {
		return getJCheckBoxDetail().isSelected();
	}
	
	/**
	 * Getting the objet(the selected layer). Updating the panel with the 
	 * actual properties of the model.
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object arg0) {
		super.setReference(arg0);
		props3D = Layer3DProps.getLayer3DProps((FLayer)this.getReference());
	//	updatePanel();
		
	}
	private void updatePanel() {
		if(props3D.getType() == Layer3DProps.layer3DImage) {
			FLyrRasterSE rasterLayer = (FLyrRasterSE)this.getReference();
			if (rasterLayer.getBandCount() == 1) {
				elevationButton.setEnabled(true);
			}
			else
				elevationButton.setEnabled(false);
		}
	}
}