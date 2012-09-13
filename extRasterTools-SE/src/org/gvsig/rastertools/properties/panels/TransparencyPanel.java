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
package org.gvsig.rastertools.properties.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.gui.beans.checkslidertext.CheckSliderTextContainer;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.gui.beans.slidertext.listeners.SliderEvent;
import org.gvsig.gui.beans.slidertext.listeners.SliderListener;
import org.gvsig.gui.util.StatusComponent;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.rastertools.RasterModule;
import org.gvsig.rastertools.properties.control.TransparencyListener;

import com.iver.andami.PluginServices;
/**
 * Dialogo para asignar la transparencia por pixel y global al raster.
 *
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class TransparencyPanel extends AbstractPanel implements ActionListener, SliderListener {
	private static final long serialVersionUID = -4556920949255458471L;
	private IRasterProperties        op                   = null;

	/**
	 * Número de bandas del raster
	 */
	public int                       nBands               = 3;
	private JCheckBox                cbTransparencia      = null;
	private TranspByPixelPanel       pTranspByPixel       = null;
	private CheckSliderTextContainer pOpacity             = null;
	private TransparencyListener     transparencyListener = null;

	/**
	 * Constructor.
	 */
	public TransparencyPanel() {
		setLabel(PluginServices.getText(this, "transparencia"));
		initialize();
	}

	private TransparencyListener getTransparencyListener() {
		if (transparencyListener == null) {
			transparencyListener = new TransparencyListener(this);
		}
		return transparencyListener;
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	protected void initialize() {
		this.setLayout(new BorderLayout());
		this.add(getOpacityPanel(), BorderLayout.NORTH);
		this.add(getPTranspByPixel(), BorderLayout.CENTER);
		initControls();
		this.setPreferredSize(new Dimension(100, 80));
	}

	/**
	 * Asigna el número de bandas de la imagen
	 * @param nBands
	 */
	public void setBands(int nBands) {
		this.nBands = nBands;
	}

	/**
	 * Inicializa controles a sus valores por defecto
	 */
	public void initControls() {
		this.setActiveTransparencyControl(false);
	}

	/**
	 * This method initializes jCheckBox
	 * @return javax.swing.JCheckBox
	 */
	public JCheckBox getTransparencyCheck() {
		if (cbTransparencia == null) {
			cbTransparencia = new JCheckBox();
			cbTransparencia.setText("Activar");
			cbTransparencia.addActionListener(this);
		}

		return cbTransparencia;
	}

	/**
	 * This method initializes TranspOpacitySliderPanel
	 * @return javax.swing.JPanel
	 */
	public CheckSliderTextContainer getOpacityPanel() {
		if (pOpacity == null) {
			pOpacity = new CheckSliderTextContainer(0, 100, 100, false, PluginServices.getText(this, "activar"), false, false, true);
			pOpacity.setDecimal(false);
			pOpacity.setBorder(PluginServices.getText(this, "opacidad"));
			pOpacity.addValueChangedListener(this);
		}

		return pOpacity;
	}

	/**
	 * Activa/Desactiva los controles de transparencia
	 * @param active
	 */
	public void setActiveTransparencyControl(boolean active) {
		this.getTransparencyCheck().setSelected(active);
		TranspByPixelRGBInputPanel rgbPanel = this.getPTranspByPixel().getPRGBInput();
		rgbPanel.getTRed().setEnabled(active);

		if (op != null) {
			if (op.getBandCount() == 2) {
				rgbPanel.getTGreen().setEnabled(active);
				rgbPanel.getTBlue().setEnabled(false);
			}

			if (op.getBandCount() == 3) {
				rgbPanel.getTGreen().setEnabled(active);
				rgbPanel.getTBlue().setEnabled(active);
			}
		}
	}

	/**
	 * This method initializes jPanel2
	 * @return javax.swing.JPanel
	 */
	public TranspByPixelPanel getPTranspByPixel() {
		if (pTranspByPixel == null) {
			pTranspByPixel = new TranspByPixelPanel();
			pTranspByPixel.setBorder(BorderFactory.createTitledBorder(null, PluginServices.getText(this, "transp_by_pixel"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		}

		return pTranspByPixel;
	}

	/**
	 * Eventos sobre TextField y CheckBox. Controla eventos de checkbox de
	 * opacidad, transparencia, recorte de colas y los textfield de opacidad,
	 * valores de transparencia por banda y porcentaje de recorte.
	 */
	public void actionPerformed(ActionEvent e) {
		// Evento sobre el checkbox de transparencia
		if (e.getSource().equals(getTransparencyCheck())) {
			// Check de opacidad activado -> Activar controles de opacidad
			if (getTransparencyCheck().isSelected()) {
				getPTranspByPixel().setControlEnabled(true);
				getPTranspByPixel().getPRGBInput().setActiveBands(nBands);
			} else
				getPTranspByPixel().setControlEnabled(false);
		}
	}

	/**
	 * Obtiene el interfaz de operaciones raster
	 * @return FLyrRasterSE
	 */
	public IRasterProperties getRasterOperations() {
		return op;
	}

	/**
	 * Obtiene la capa si existe esta.
	 * @return FLayer si existe una capa o null si no existe.
	 */
	public FLayer getLayer() {
		if (op instanceof FLayer)
			return (FLayer) op;
		return null;
	}

	public void setReference(Object ref) {
		super.setReference(ref);

		if (!(ref instanceof FLayer))
			return;

		FLayer lyr = (FLayer) ref;

		if (lyr instanceof IRasterProperties) {
			op = (IRasterProperties) lyr;
			getTransparencyListener().setLayer(op);
		}

		actionEnabled();
	}

	private void actionEnabled() {
		if (op == null) {
			setVisible(false);
			return;
		}

		FLyrRasterSE fLyrRasterSE = ((FLyrRasterSE) op);

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.OPACITY))
			StatusComponent.setDisabled(getOpacityPanel());

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.TRANSPARENCY))
			StatusComponent.setDisabled(getPTranspByPixel());

		if (!fLyrRasterSE.isActionEnabled(IRasterLayerActions.TRANSPARENCY) &&
				!fLyrRasterSE.isActionEnabled(IRasterLayerActions.OPACITY))
			setVisible(false);
		else
			setVisible(true);
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.rastertools.properties.dialog.IRegistrablePanel#accept()
	 */
	public void accept() {
		getTransparencyListener().accept();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.rastertools.properties.dialog.IRegistrablePanel#apply()
	 */
	public void apply() {
		getTransparencyListener().apply();
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.rastertools.properties.dialog.IRegistrablePanel#cancel()
	 */
	public void cancel() {
		getTransparencyListener().cancel();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.slidertext.listeners.SliderListener#actionValueChanged(org.gvsig.gui.beans.slidertext.listeners.SliderEvent)
	 */
	public void actionValueChanged(SliderEvent e) {
		if (!RasterModule.autoRefreshView)
			return;
		getTransparencyListener().onlyApply();
	}

	public void actionValueDragged(SliderEvent e) {}
	public void selected() {}
}