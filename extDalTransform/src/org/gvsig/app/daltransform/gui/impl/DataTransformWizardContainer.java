/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
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
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2009 {Iver T.I.}   {Task}
*/
 
package org.gvsig.app.daltransform.gui.impl;

import java.awt.BorderLayout;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import org.gvsig.app.daltransform.gui.DataTransformWizardPanel;


/**
 * @author <a href="mailto:jpiera@gvsig.org">Jorge Piera</a>
 */
public class DataTransformWizardContainer extends JWizardPanel {
	private static final long serialVersionUID = -3410258554443619626L;
	private DataTransformWizardPanel wizard = null;
	
	/**
	 * @param wizardComponents
	 */
	public DataTransformWizardContainer(JWizardComponents wizardComponents, DataTransformWizardPanel wizard) {
		super(wizardComponents);	
		this.wizard = wizard;
		setLayout(new BorderLayout());
		setBorder(javax.swing.BorderFactory.createTitledBorder(wizard.getPanelTitle()));
		add(wizard.getJPanel(), BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see jwizardcomponent.WizardPanel#back()
	 */
	@Override
	public void back() {
		wizard.lastPanel();
		super.back();
	}

	/* (non-Javadoc)
	 * @see jwizardcomponent.WizardPanel#next()
	 */
	@Override
	public void next() {
		wizard.nextPanel();
		super.next();
	}

	/* (non-Javadoc)
	 * @see jwizardcomponent.JWizardPanel#update()
	 */
	@Override
	public void update() {
		wizard.updatePanel();
		super.update();
	}
}

