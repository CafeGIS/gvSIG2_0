/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
package org.gvsig.rastertools.vectorizacion.filter.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.raster.beans.previewbase.IUserPanelInterface;
import org.gvsig.raster.filter.grayscale.GrayScaleFilter;
import org.gvsig.raster.util.BasePanel;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.vectorizacion.filter.GrayConversionData;

import com.iver.utiles.swing.JComboBox;

/**
 * Panel para la conversi�n a escala de grises. 
 * 
 * 09/06/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class GrayConversionPanel extends BasePanel implements IUserPanelInterface, Observer {
	private static final long      serialVersionUID   = -1;
	
	private NoisePanel             noisePanel         = null;
	//private HighPassPanel          highPassPanel      = null;
	private PosterizationPanel     posterizationPanel = null;
	private ModePanel              modePanel          = null;
	
	private JComboBox              comboBands         = null;
	private JPanel                 levelsPanel        = null;
		
	/**
	 *Inicializa componentes gr�ficos y traduce
	 */
	public GrayConversionPanel() {
		init();
		translate();
	}
	
	/**
	 * Inicializa los componentes gr�ficos
	 */
	protected void init() {
		setBorder(BorderFactory.createTitledBorder(null, RasterToolsUtil.getText(this, "grayescaleconversion"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 2, 2, 2);
		
		gbc.gridy = 0;
		add(getLevelPanel(), gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.gridy = 1;
		add(getPosterizationPanel(), gbc);
		
		/*gbc.gridy = 2;
		add(getHighPassPanel(), gbc);*/
		
		gbc.gridy = 3;
		add(getModePanel(), gbc);
		
		gbc.gridy = 4;
		add(getNoisePanel(), gbc);
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.util.BasePanel#translate()
	 */
	protected void translate() {
		
	}
	
	/**
	 * Obtiene el panel con los niveles
	 * @return
	 */
	public JPanel getLevelPanel() {
		if(levelsPanel == null) {
			levelsPanel = new JPanel();
			levelsPanel.setLayout(new BorderLayout());
			levelsPanel.add(new JLabel(RasterToolsUtil.getText(this, "bands")), BorderLayout.WEST);
			levelsPanel.add(getComboBands(), BorderLayout.CENTER);
		}
		return levelsPanel;
	} 
	
	/**
	 * Obtiene el panel con el panel de posterizaci�n
	 * @return PosterizationPanel
	 */
	public PosterizationPanel getPosterizationPanel() {
		if(posterizationPanel == null) {
			posterizationPanel = new PosterizationPanel();
		}
		return posterizationPanel;
	} 
	
	/**
	 * Obtiene el panel con el fitro de paso alto
	 * @return HighPassPanel
	 */
	/*public HighPassPanel getHighPassPanel() {
		if(highPassPanel == null) {
			highPassPanel = new HighPassPanel();
		}
		return highPassPanel;
	}*/
	
	/**
	 * Obtiene el panel con el fitro de moda
	 * @return HighPassPanel
	 */
	public ModePanel getModePanel() {
		if(modePanel == null) {
			modePanel = new ModePanel();
		}
		return modePanel;
	} 
	
	/**
	 * Obtiene el panel con el fitro de ruido
	 * @return HighPassPanel
	 */
	public NoisePanel getNoisePanel() {
		if(noisePanel == null) {
			noisePanel = new NoisePanel();
		}
		return noisePanel;
	}
	
	/**
	 * Obtiene el combo con los niveles
	 * @return
	 */
	public JComboBox getComboBands() {
		if(comboBands == null) {
			comboBands = new JComboBox();
		}
		return comboBands;
	} 

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getTitle()
	 */
	public String getTitle() {
		return getText(this, "grayscaleconversion");
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if(!(o instanceof GrayConversionData))
			return;
		GrayConversionData data = (GrayConversionData)o;

		setEnableValueChangedEvent(false);
				
		String[] bands = data.getBands();
		getComboBands().removeAllItems();
		for (int i = 0; i < bands.length; i++) 
			getComboBands().addItem(bands[i]);
		
		switch (data.getBandType()) {
		case GrayScaleFilter.R:
		case GrayScaleFilter.GRAY:	getComboBands().setSelectedIndex(0);
									break;
		case GrayScaleFilter.G:	getComboBands().setSelectedIndex(1);
								break;
		case GrayScaleFilter.B:	getComboBands().setSelectedIndex(2);
								break;
		case GrayScaleFilter.RGB:	getComboBands().setSelectedIndex(3);
									break;
		}
		
		getPosterizationPanel().getLevels().setValue(data.getPosterizationLevels() + "");
		getNoisePanel().getThreshold().setValue(data.getNoiseThreshold());	
		getModePanel().getThreshold().setValue(data.getModeThreshold());	
		
		setEnableValueChangedEvent(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.beans.previewbase.IUserPanelInterface#getPanel()
	 */
	public JPanel getPanel() {
		return this;
	}
}
