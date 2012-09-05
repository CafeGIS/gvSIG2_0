package com.iver.utiles.swing.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * Clase wizard con la gestión de los botones y los paneles añadidos al mismo.
 * Al avanzar o retrasar un paso el asistente se desactivan todos los botones
 * menos el de cancelar, que está activo siempre por defecto
 *
 * @author Fernando González Cortés
 */
public class Wizard extends JPanel implements WizardControl {
	private JPanel jPanel = null;
	private JButton btnBack = null;
	private JButton btnNext = null;
	private JButton btnFinish = null;
	private JButton btnCancel = null;
	private ArrayList steps = new ArrayList();
	private int currentStep = 0;
	private JPanel pnlSteps = null;
	private ArrayList listeners = new ArrayList();

	/**
	 * This is the default constructor
	 */
	public Wizard(String backText, String nextText, String finishText, String cancelText) {
		super();
		initialize(backText, nextText, finishText, cancelText);
	}

	/**
	 * Añade un lístener de eventos del wizard
	 *
	 * @param listener
	 */
	public void addWizardListener(WizardListener listener) {
		listeners.add(listener);
	}

	/**
	 * Elimina un listener de eventos del wizard
	 *
	 * @param listener
	 */
	public void removeWizardListener(WizardListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Invoca en los listeners el evento cancel
	 */
	private void callCancelListener() {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			WizardListener element = (WizardListener) iter.next();
			element.cancel(new WizardEvent(this, currentStep));
		}
	}

	/**
	 * Invoca en los listeners el evento finish
	 */
	private void callFinishListener() {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			WizardListener element = (WizardListener) iter.next();
			element.finished(new WizardEvent(this, currentStep));
		}
	}

	/**
	 * Invoca en los listeners el evento next
	 */
	private void callNextListener() {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			WizardListener element = (WizardListener) iter.next();
			element.next(new WizardEvent(this, currentStep));
		}
	}

	/**
	 * Invoca en los listeners el evento back
	 */
	private void callBackListener() {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			WizardListener element = (WizardListener) iter.next();
			element.back(new WizardEvent(this, currentStep));
		}
	}

	/**
	 * This method initializes this
	 */
	private void initialize(String backText, String nextText, String finishText, String cancelText) {
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(getJPanel(backText, nextText, finishText, cancelText), java.awt.BorderLayout.SOUTH);
		this.add(getPnlSteps(), java.awt.BorderLayout.CENTER);
	}

	/**
	 * Añade un paso al asistente. Inicializa el paso
	 *
	 * @param s Paso a añadir
	 *
	 * @throws RuntimeException DOCUMENT ME!
	 */
	public void addStep(Step s) {
		if (!(s instanceof JComponent)) {
			throw new RuntimeException(
				"Step must be a visual component (descend from JComponent)");
		}

		getPnlSteps().add((JComponent) s, BorderLayout.CENTER);
		steps.add(s);
		disableButtons();
		s.init(this);
	}

	/**
	 * Habilita o deshabilita los botones en función del paso en el que se
	 * encuentre el asistente
	 */
	private void disableButtons() {
		btnNext.setEnabled(false);
		btnBack.setEnabled(false);
		btnFinish.setEnabled(false);
	}

	/**
	 * Activa el paso al siguiente paso del asistente
	 */
	public void enableNext(boolean enabled) {
		if (currentStep == (steps.size() - 1)) {
			btnFinish.setEnabled(enabled);
		} else {
			btnNext.setEnabled(enabled);
		}
	}

	/**
	 * Activa el paso al paso anterior del asistente
	 */
	public void enableBack(boolean enabled) {
		if (currentStep != 0) {
			btnBack.setEnabled(enabled);
		}
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel(String backText, String nextText, String finishText, String cancelText) {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnBack(backText), null);
			jPanel.add(getBtnNext(nextText), null);
			jPanel.add(getBtnFinish(finishText), null);
			jPanel.add(getBtnCancel(cancelText), null);
		}

		return jPanel;
	}

	private JButton newJButton(String text){
		return new JButton() {
            protected void paintComponent(Graphics g) {
    			TextLayout tl = new TextLayout(getText(), getFont(), ((Graphics2D )getGraphics()).getFontRenderContext());
    			setPreferredSize(new java.awt.Dimension((int) tl.getBounds().getWidth(), 18));
                super.paintComponent(g);
            }
        };
	    
	}
	
	/**
	 * Obtiene una referencia al botón de dar un paso atrás
	 * @param text
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getBtnBack(String text) {
		if (btnBack == null) {
			btnBack = newJButton(text);
			btnBack.setText(text);
			btnBack.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnBack.setEnabled(false);
			btnBack.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						backStep();
					}
				});
		}

		return btnBack;
	}

	/**
	 * Obtiene una referencia al botón de dar un paso adelante
	 * @param text
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getBtnNext(String text) {
		if (btnNext == null) {
			btnNext = newJButton(text);
			btnNext.setText(text);
			btnNext.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnNext.setEnabled(false);
			btnNext.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						nextStep();
					}
				});
		}

		return btnNext;
	}

	/**
	 * Obtiene una referencia al botón de finalizar
	 * @param finishText
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getBtnFinish(String text) {
		if (btnFinish == null) {
			btnFinish = newJButton(text);
			btnFinish.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnFinish.setText(text);
			btnFinish.setEnabled(false);
			btnFinish.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						callFinishListener();
					}
				});
		}

		return btnFinish;
	}

	/**
	 * Obtiene una referencia al botón de cancelar
	 * @param cancelText
	 *
	 * @return javax.swing.JButton
	 */
	public JButton getBtnCancel(String text) {
		if (btnCancel == null) {
			btnCancel = newJButton(text);
			btnCancel.setMargin(new java.awt.Insets(2, 2, 2, 2));
			btnCancel.setText(text);
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						callCancelListener();
					}
				});
		}

		return btnCancel;
	}

	/**
	 * This method initializes pnlSteps
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlSteps() {
		if (pnlSteps == null) {
			pnlSteps = new JPanel();
			pnlSteps.setLayout(new CardLayout());
		}

		return pnlSteps;
	}

	/**
	 * Muestra el panel del siguiente paso del asistente
	 */
	public void nextStep() {
		currentStep++;
		((CardLayout) getPnlSteps().getLayout()).next(getPnlSteps());
		disableButtons();
		callNextListener();
	}

	/**
	 * Muestra el panel del paso anterior del asistente
	 */
	public void backStep() {
		currentStep--;
		((CardLayout) getPnlSteps().getLayout()).previous(getPnlSteps());
		disableButtons();
		callBackListener();
	}

	/**
	 * Se cancela el asistente. Esta operación no tiene ningún efecto, salvo
	 * que se disparará el evento de cancelado. El resultado de esto dependerá
	 * de las implementaciones que haya escuchando el evento. Generalmente
	 * deberá haber un objeto que al escuchar este evento cerrará el
	 * asistente.
	 */
	public void cancel() {
		callCancelListener();
	}

	/**
	 * Se finaliza el asistente. Esta operación no tiene ningún efecto, salvo
	 * que se disparará el evento de finalización. El resultado de esto
	 * dependerá de las implementaciones que haya escuchando el evento.
	 * Generalmente deberá haber un objeto que al escuchar este evento cerrará
	 * el asistente.
	 */
	public void finish() {
		callFinishListener();
	}

	/**
	 * Obtiene un array con los pasos del asistente
	 *
	 * @return array de pasos
	 */
	public Step[] getSteps() {
		return (Step[]) steps.toArray(new Step[0]);
	}

	/**
	 * Obtiene el paso actual del asistente
	 *
	 * @return Paso actual del asistente
	 */
	public Step getCurrentStep() {
		return getSteps()[currentStep];
	}
}
