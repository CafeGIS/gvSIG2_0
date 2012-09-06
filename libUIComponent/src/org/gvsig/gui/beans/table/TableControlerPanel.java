/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.gui.beans.table;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.Messages;
import org.gvsig.gui.beans.table.listeners.TableListener;
import org.gvsig.gui.util.StatusComponent;

/**
 * Control para el manejo de tablas. No contiene eventos, estos deben ser
 * manejados desde la clase que lo llame.
 * @author Nacho Brodin (brodin_ign@gva.es)
 */
public class TableControlerPanel extends JPanel {
	private static final long serialVersionUID = -6442685244347917638L;
	private int               HEIGHT_BUTTONS   = 19;                   // 16
																																			// estaria
																																			// bien
	// **********************Vars**********************************
	private JButton           bPrev            = null;
	private JComboBox         cPoint           = null;
	private JButton           bNext            = null;
	private JButton           bFirst           = null;
	private JButton           bLast            = null;
	private JLabel            lPoints          = null;
	private JLabel            lNumberOfPoints  = null;
	private JButton           bNew             = null;
	private JButton           bDelPoint        = null;
	private JButton           bClear           = null;

	private String            pathToImages     = "images/";            // "/com/iver/cit/gvsig/gui/panels/images/";

	/**
	 * Objeto para controlar el estado de los componentes visuales
	 */
	private StatusComponent   statusComponent  = null;

	// **********************End Vars******************************

	// **********************Methods*******************************
	/**
	 * This is the default constructor
	 */
	public TableControlerPanel(TableListener tableListener) {
		initialize(tableListener);
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize(TableListener tableListener) {
		statusComponent = new StatusComponent(this);

		this.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 0));
		this.setAlignmentX(0);

		lPoints = new JLabel();
		lPoints.setText(Messages.getText("registro") + " ");
		this.add(lPoints);

		this.add(getBFirst());
		this.add(getBPrev());
		this.add(getCPoint());
		this.add(getBNext());
		this.add(getBLast());
		this.add(getBNew());
		this.add(getLNumberOfPoints());
		this.add(getBDelPoint());
		this.add(getBClear());

		getBFirst().addActionListener(tableListener);
		getBPrev().addActionListener(tableListener);
		getCPoint().addActionListener(tableListener);
		getBNext().addActionListener(tableListener);
		getBLast().addActionListener(tableListener);
		getBNew().addActionListener(tableListener);
		getBDelPoint().addActionListener(tableListener);
		getBClear().addActionListener(tableListener);
	}

	/**
	 * Esta función deshabilita todos los controles y guarda sus valores de
	 * habilitado o deshabilitado para que cuando se ejecute restoreControlsValue
	 * se vuelvan a quedar como estaba
	 */
	public void disableAllControls() {
		statusComponent.setEnabled(false);
	}

	/**
	 * Esta función deja los controles como estaban al ejecutar la función
	 * disableAllControls
	 */
	public void restoreControlsValue() {
		statusComponent.setEnabled(true);
	}

	// **********************Methods*******************************
	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getBFirst() {
		if (bFirst == null) {
			bFirst = new JButton();
			bFirst.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bFirst.setEnabled(false);
			bFirst.setIcon(new ImageIcon(getClass().getResource(pathToImages + "first.png")));
			bFirst.setToolTipText(Messages.getText("primero"));
		}
		return bFirst;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getBLast() {
		if (bLast == null) {
			bLast = new JButton();
			bLast.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bLast.setEnabled(false);
			bLast.setIcon(new ImageIcon(getClass().getResource(pathToImages + "last.png")));
			bLast.setToolTipText(Messages.getText("ultimo"));
		}
		return bLast;
	}

	/**
	 * This method initializes bBefore
	 * @return javax.swing.JButton
	 */
	public JButton getBPrev() {
		if (bPrev == null) {
			bPrev = new JButton();
			bPrev.setText("");
			bPrev.setEnabled(false);
			bPrev.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bPrev.setIcon(new ImageIcon(getClass().getResource(pathToImages + "prev.png")));
			bPrev.setActionCommand("");
			bPrev.setToolTipText(Messages.getText("anterior"));
		}
		return bPrev;
	}

	/**
	 * This method initializes bNext
	 * @return javax.swing.JButton
	 */
	public JButton getBNext() {
		if (bNext == null) {
			bNext = new JButton("");
			bNext.setEnabled(false);
			bNext.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bNext.setIcon(new ImageIcon(getClass().getResource(pathToImages + "next.png")));
			bNext.setActionCommand("");
			bNext.setToolTipText(Messages.getText("siguiente"));
		}
		return bNext;
	}

	/**
	 * Este método inicializa el combo que contiene el número de puntos.
	 * @return javax.swing.JComboBox
	 */
	public JComboBox getCPoint() {
		if (cPoint == null) {
			cPoint = new JComboBox();
			cPoint.setPreferredSize(new Dimension(64, HEIGHT_BUTTONS));
		}
		return cPoint;
	}

	/**
	 * @return Returns the lNumberOfPoints.
	 */
	public JLabel getLNumberOfPoints() {
		if (lNumberOfPoints == null) {
			lNumberOfPoints = new JLabel();
			lNumberOfPoints.setText(Messages.getText("de") + " 0");
			// lNumberOfPoints.setPreferredSize(new java.awt.Dimension(35,15));
		}
		return lNumberOfPoints;
	}

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getBNew() {
		if (bNew == null) {
			bNew = new JButton();
			bNew.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bNew.setIcon(new ImageIcon(getClass().getResource(pathToImages + "newpoint.png")));
			bNew.setToolTipText(Messages.getText("nuevo"));
		}
		return bNew;
	}

	/**
	 * Este método inicializa el botón del eliminar punto que hará que se elimine
	 * el punto seleccionado.
	 * @return javax.swing.JButton
	 */
	public JButton getBDelPoint() {
		if (bDelPoint == null) {
			bDelPoint = new JButton();
			bDelPoint.setText("");
			bDelPoint.setEnabled(false);
			bDelPoint.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bDelPoint.setIcon(new ImageIcon(getClass().getResource(pathToImages + "delone.png")));
			bDelPoint.setToolTipText(Messages.getText("borrar_uno"));
		}
		return bDelPoint;
	}

	/**
	 * Este método inicializa el botón del clear que hará que se eliminen todos
	 * los puntos seleccionados.
	 * @return javax.swing.JButton
	 */
	public JButton getBClear() {
		if (bClear == null) {
			bClear = new JButton();
			bClear.setText("");
			bClear.setPreferredSize(new Dimension(22, HEIGHT_BUTTONS));
			bClear.setEnabled(false);
			bClear.setIcon(new ImageIcon(getClass().getResource(pathToImages + "delall.png")));
			bClear.setActionCommand("");
			bClear.setToolTipText(Messages.getText("borrar_todos"));
		}
		return bClear;
	}

	/**
	 * Resetea el control al estado inicial. Limpia el combo, pone el número de
	 * elementos a 0 y desactiva las flechas.
	 */
	public void resetControls() {
		getCPoint().removeAllItems();
		getLNumberOfPoints().setText(Messages.getText("de 0"));
		checkArrows();
	}

	/**
	 * Elimina del control un elemento de una posición.
	 * <UL>
	 * <LI>Actualiza el combo</LI>
	 * <LI>Actualiza el texto que dice el número de elementos</LI>
	 * <LI>Actualiza las flechas</LI>
	 * </UL>
	 * @param pos Posición del elemento a eliminar.
	 */
	public void setNItems(int n) {
		// if(n < 1)
		// return;
		getCPoint().removeAllItems();
		for (int i = 1; i <= n; i++) {
			try {
				getCPoint().addItem(String.valueOf(i));
			} catch (ArrayIndexOutOfBoundsException ex) {
				// No añadimos nada
			}
		}
		getLNumberOfPoints().setText(Messages.getText("de") + " " + n);
		checkArrows();
	}

	/**
	 * Selecciona un elemento del control
	 * @param index
	 */
	public void setSelectedIndex(int index) {
		try {
			getCPoint().setSelectedIndex(index);
		} catch (IllegalArgumentException ex) {

		}
		checkArrows();
	}

	/**
	 * Devuelve el punto seleccionado
	 * @return Punto seleccionado.
	 */
	public int getSelectedIndex() {
		return getCPoint().getSelectedIndex();
	}

	/**
	 * Obtiene el número de elementos en la lista.
	 * @return Número de elementos
	 */
	public int getItemCount() {
		return getCPoint().getItemCount();
	}

	/**
	 * Obtiene el Objeto seleccionado como cadena de texto.
	 * @return Número seleccionado
	 */
	public String getSelectedItem() {
		return getCPoint().getSelectedItem().toString();
	}

	/**
	 * Comprueba la posición del combo para ver si tiene que habilitar o
	 * deshabilitar las flechas de delante y detrás.
	 */
	public void checkArrows() {
		if (!statusComponent.isEnabled())
			return;

		if (getCPoint().getItemCount() <= 0) {
			getBClear().setEnabled(false);
			getBDelPoint().setEnabled(false);
		} else {
			getBClear().setEnabled(true);
			getBDelPoint().setEnabled(true);
		}

		if (getCPoint().getSelectedIndex() == -1) {
			getBPrev().setEnabled(false);
			getBNext().setEnabled(false);
			getBLast().setEnabled(false);
			getBFirst().setEnabled(false);

			return;
		}
		if (getCPoint().getSelectedIndex() == 0) {
			getBPrev().setEnabled(false);
			getBFirst().setEnabled(false);
		} else {
			getBPrev().setEnabled(true);
			getBFirst().setEnabled(true);
		}

		if (getCPoint().getSelectedIndex() == (getCPoint().getItemCount() - 1)) {
			getBNext().setEnabled(false);
			getBLast().setEnabled(false);
		} else {
			getBNext().setEnabled(true);
			getBLast().setEnabled(true);
		}
	}

	/**
	 * Añade un punto al combo y checkea los controles colocandolos en los valores
	 * correctos.
	 * @param countPoints Número de punto a añadir al final del combo
	 */
	public void addPointToTable(int countPoints) {
		getCPoint().addItem("" + countPoints);
		getLNumberOfPoints().setText(Messages.getText("de") + " " + countPoints);
		setSelectedIndex(getItemCount() - 1);
		checkArrows();
	}

	/**
	 * Asigna la ruta donde están las imagenes
	 * @param pathToImages
	 */
	public void setPathToImages(String pathToImages) {
		this.pathToImages = pathToImages;
	}

	/**
	 * Activa o desactiva este panel y todos los que lo componen
	 * @param enabled variable booleana para la activación y/o desactivación
	 */
	public void setEnabled(boolean enabled) {
		this.getBDelPoint().setEnabled(enabled);
		this.getBClear().setEnabled(enabled);
	}

	/**
	 * Dice si los controles están deshabilitados o no
	 * @return true si está habilitado y false si no lo está
	 */
	public boolean isDisableAllControls() {
		return !statusComponent.isEnabled();
	}
}