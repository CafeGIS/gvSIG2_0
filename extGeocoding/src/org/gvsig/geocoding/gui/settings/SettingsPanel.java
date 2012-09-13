/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsPanel.java
 *
 * Created on 16-mar-2009, 12:41:50
 */

package org.gvsig.geocoding.gui.settings;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.gvsig.geocoding.extension.GeocodingController;

import com.iver.andami.PluginServices;

/**
 * 
 * @author vsanjaime
 */
public class SettingsPanel extends JPanel implements KeyListener {

	private static final int MAXRESULTS = 10;

	private static final long serialVersionUID = 1L;

	private GeocodingController control = null;

	private JLabel jLabMaxResults;
	private JLabel jLabScore;
	private JLabel jLabScorePercent;
	private JPanel jPanMaxResults;
	private JPanel jPanScore;
	private JSlider jSliderScore;
	private JTextField jTextMaxResults;

	/**
	 * Constructor
	 */
	public SettingsPanel(GeocodingController control) {
		initComponents();
		this.control = control;

		setMesages();
	}

	/**
	 * Initialize components
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jPanMaxResults = new javax.swing.JPanel();
		jLabMaxResults = new javax.swing.JLabel();
		jTextMaxResults = new javax.swing.JTextField();
		jPanScore = new javax.swing.JPanel();
		jLabScore = new javax.swing.JLabel();
		jSliderScore = new javax.swing.JSlider();
		jLabScorePercent = new javax.swing.JLabel();

		setLayout(new java.awt.GridBagLayout());

		jPanMaxResults.setLayout(new java.awt.GridBagLayout());

		jLabMaxResults.setText("max");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanMaxResults.add(jLabMaxResults, gridBagConstraints);

		jTextMaxResults.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		jTextMaxResults.setMinimumSize(new java.awt.Dimension(30, 20));
		jTextMaxResults.setPreferredSize(new java.awt.Dimension(60, 20));
		jTextMaxResults.addKeyListener(this);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		jPanMaxResults.add(jTextMaxResults, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
		add(jPanMaxResults, gridBagConstraints);

		jPanScore.setLayout(new java.awt.GridBagLayout());

		jLabScore.setText("score");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
		jPanScore.add(jLabScore, gridBagConstraints);

		jSliderScore.setMajorTickSpacing(10);
		jSliderScore.setMinorTickSpacing(10);
		jSliderScore.setPaintTicks(false);
		jSliderScore.setSnapToTicks(true);
		jSliderScore.setValue(80);
		jSliderScore.setPreferredSize(new java.awt.Dimension(120, 25));
		jSliderScore.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				evChangeScore(evt);
			}
		});
		jPanScore.add(jSliderScore, new java.awt.GridBagConstraints());

		jLabScorePercent.setText("80");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		jPanScore.add(jLabScorePercent, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(jPanScore, gridBagConstraints);
	}

	/**
	 * change score value event
	 * 
	 * @param evt
	 */
	private void evChangeScore(javax.swing.event.ChangeEvent evt) {
		double score = (double) jSliderScore.getValue();
		jLabScorePercent.setText(Integer.toString((int) score));
		if (control.getPattern() != null) {
			control.getPattern().getSettings().setScore(score);
		}
	}

	/**
	 * get internationalizated strings
	 */
	private void setMesages() {
		PluginServices ps = PluginServices.getPluginServices(this);
		if (ps != null) {
			this.jLabMaxResults.setText(ps.getText("maxresults"));
			this.jLabScore.setText(ps.getText("score"));
			jTextMaxResults.setToolTipText(ps.getText("maxresultstip"));
			jSliderScore.setToolTipText(ps.getText("scoretip"));
		}
	}

	/**
	 * Set maximum results in the panel
	 * 
	 * @param result
	 */
	public void setMaxResults(int result) {
		jTextMaxResults.setText(Integer.toString(result));
	}

	/**
	 * Set threshold score
	 * 
	 * @param result
	 */
	public void setScore(double score) {
		jSliderScore.setValue((int) score);
		jLabScorePercent.setText(Integer.toString((int) score));
	}

	/**
	 * swich on and off components
	 * 
	 * @param active
	 */
	public void activateComponents(boolean active) {
		jLabMaxResults.setEnabled(active);
		jLabScore.setEnabled(active);
		jLabScorePercent.setEnabled(active);
		jSliderScore.setEnabled(active);
		jTextMaxResults.setEnabled(active);
	}

	/**
	 * get new pattern maximum results
	 * 
	 * @return
	 */
	public int getMaxResults() {
		return Integer.parseInt(jTextMaxResults.getText());
	}

	/**
	 * get new pattern minimum score
	 * 
	 * @return
	 */
	public double getScore() {
		return (double) jSliderScore.getValue();
	}

	/**
	 * key pressed event
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		// nothing to do

	}

	/**
	 * key released event
	 * 
	 * @param e
	 */
	public void keyReleased(KeyEvent e) {
		String max = jTextMaxResults.getText().trim();
		int maxresults;
		try {
			maxresults = Integer.parseInt(max);
			if (maxresults < 1) {
				maxresults = 1;
				jTextMaxResults.setText(Integer.toString(maxresults));
				control.getPattern().getSettings().setResultsNumber(maxresults);
			} else {
				control.getPattern().getSettings().setResultsNumber(maxresults);
			}
		} catch (Exception ex) {
			jTextMaxResults.setText(Integer.toString(MAXRESULTS));
			jTextMaxResults.requestFocus();
		}

	}

	/**
	 * key typed event
	 * 
	 * @param e
	 */
	public void keyTyped(KeyEvent e) {
		// TODO nothing to do

	}

}
