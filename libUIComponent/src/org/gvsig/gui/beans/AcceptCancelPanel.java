/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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

/* CVS MESSAGES:
*
* $Id: AcceptCancelPanel.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:45  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.5  2006/11/06 07:29:04  jaume
* *** empty log message ***
*
* Revision 1.4  2006/09/14 08:31:58  cesar
* Replace PluginServices.getText by the new Messages bridge class from libUI
*
* Revision 1.3.2.1  2006/09/13 13:13:19  cesar
* Replace PluginServices.getText by the new Messages bridge class from libUI
*
* Revision 1.3  2006/07/20 11:12:25  jaume
* *** empty log message ***
*
* Revision 1.2  2006/07/20 10:42:37  jaume
* *** empty log message ***
*
* Revision 1.1  2006/07/03 07:12:28  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.gvsig.gui.beans.swing.JButton;

/**
 * A JPanel representing a normative sized and aligned Ok and Cancel buttons according
 * on the gvSIG's style sheet. The buttons size is automatically handled as far as the
 * panel is BorderLayout-ed.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class AcceptCancelPanel extends JPanel {
  private static final long serialVersionUID = -1834568346328338410L;

	private JButton btnOk = null;
	private JButton btnCancel = null;

	/**
	 * Creates a new instance of the panel with the buttons aligned to the right
	 * and with the respective action handlers.
	 * @param okAction, the handler for the ok button clicking.
	 * @param cancelAction, the handler for the cancel button clicking.
	 *
	 */
	public AcceptCancelPanel(ActionListener okAction, ActionListener cancelAction) {
		super();
		initialize(okAction, cancelAction);
	}
	/**
	 * Creates a new instance of the panel with the buttons aligned to the right
	 * with no listeners set.
	 *
	 */
	public AcceptCancelPanel() {
		super();
		initialize(null, null);
	}
	/**
	 * This method initializes this
	 *
	 */
	private void initialize(ActionListener okAction, ActionListener cancelAction) {
        this.setLayout(new BorderLayout());
        JPanel aux = new JPanel();
        aux.add(getBtnOk(okAction), java.awt.BorderLayout.EAST);
        aux.add(getCancelButton(cancelAction), java.awt.BorderLayout.EAST);
        this.add(aux, java.awt.BorderLayout.EAST);
	}

	/**
	 * This method initializes btnOk
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOk(ActionListener okAction) {
		if (btnOk == null) {
			btnOk = new JButton();
			btnOk.setText(Messages.getText("ok" ));
			btnOk.setActionCommand("OK");
			if (okAction != null)
				btnOk.addActionListener(okAction);
		}
		return btnOk;
	}

	/**
	 * This method initializes btnCancel
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton(ActionListener cancelAction) {
		if (btnCancel == null) {
			btnCancel = new JButton();
			btnCancel.setText(Messages.getText("cancel" ));
			btnCancel.setActionCommand("CANCEL");
			if (cancelAction != null)
				btnCancel.addActionListener(cancelAction);
		}
		return btnCancel;
	}

	/**
	 * Adds an ActionListener to the <b>cancel</b> button.
	 * @param l
	 */
	public void addCancelButtonActionListener(ActionListener l) {
		btnCancel.addActionListener(l);
	}

	/**
	 * Sets the ActionListener to the <b>OK</b> button removing any other previous one.
	 * @param l
	 */
	public void setOkButtonActionListener(ActionListener l) {
		ActionListener[] listeners = btnOk.getActionListeners();
		for (int i = 0; i < listeners.length; i++) {
			btnOk.removeActionListener(listeners[i]);
		}
		btnOk.addActionListener(l);
	}

	/**
	 * Sets the ActionListener to the <b>cancel</b> button removing any other previous one.
	 * @param l
	 */
	public void setCancelButtonActionListener(ActionListener l) {
		ActionListener[] listeners = btnCancel.getActionListeners();
		for (int i = 0; i < listeners.length; i++) {
			btnCancel.removeActionListener(listeners[i]);
		}
		btnCancel.addActionListener(l);
	}

	/**
	 * Adds an ActionListener to the <b>OK</b> button.
	 * @param l
	 */
	public void addOkButtonActionListener(ActionListener l) {
		btnOk.addActionListener(l);
	}

	/**
	 * Returns the ok button contained by this panel since resizing issues should be
	 * automatically handled by the layout manager. The use of this method is discouraged,
	 * it is keeped only for compatibility issues. Try using specific button properties
	 * access methods contained by this class instead.
	 * @return the Ok button
	 * @deprecated
	 */
	public JButton getOkButton() {
		return btnOk;
	}

	public boolean isOkButtonEnabled() {
		return btnOk.isEnabled();
	}

	public boolean isCancelButtonEnabled() {
		return btnCancel.isEnabled();
	}

	public void setOkButtonEnabled(boolean b) {
		btnOk.setEnabled(b);
	}

	public void setCancelButtonEnabled(boolean b) {
		btnCancel.setEnabled(b);
	}
}
