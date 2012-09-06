package org.gvsig.gui.beans.buttonspanel;

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

/**
 * <p>Interface for adding support to enable/disable the necessary buttons of a {@link ButtonsPanel ButtonsPanel} component.</p>
 * <p>For example, if our panel has only the <i>accept</i> and <i>cancel</i> buttons, 
 *  will only need to have implemented the methods related with them. And, even, only one (the set...) of them,
 *   if, for instance, we don't need to know if a particular button is or isn't enabled.</p>    
 * 
 * @version 09/05/2008
 *
 * @author BorSanZa - Borja Sanchez Zamorano (borja.sanchez@iver.es)
 */
public interface IButtonsPanel {
	/**
	 * <p>Enables (or disables) the <i>accept button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>accept button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledAcceptButton()
	 */
	public void setEnabledAcceptButton(boolean b);

	/**
	 * <p>Determines whether the <i>accept button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>accept button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledAcceptButton(boolean)
	 */
	public boolean isEnabledAcceptButton();

	/**
	 * <p>Enables (or disables) the <i>cancel button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>cancel button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledCancelButton()
	 */
	public void setEnabledCancelButton(boolean b);

	/**
	 * <p>Determines whether the <i>cancel button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>cancel button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledCancelButton(boolean)
	 */
	public boolean isEnabledCancelButton();

	/**
	 * <p>Enables (or disables) the <i>apply button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>apply button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledApplyButton()
	 */
	public void setEnabledApplyButton(boolean b);

	/**
	 * <p>Determines whether the <i>apply button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>apply button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledApplyButton(boolean)
	 */
	public boolean isEnabledApplyButton();

	/**
	 * <p>Enables (or disables) the <i>yes button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>yes button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledYesButton()
	 */
	public void setEnabledYesButton(boolean b);

	/**
	 * <p>Determines whether the <i>yes button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>yes button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledYesButton(boolean)
	 */
	public boolean isEnabledYesButton();

	/**
	 * <p>Enables (or disables) the <i>no button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>no button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledNoButton()
	 */
	public void setEnabledNoButton(boolean b);

	/**
	 * <p>Determines whether the <i>no button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>no button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledNoButton(boolean)
	 */
	public boolean isEnabledNoButton();

	/**
	 * <p>Enables (or disables) the <i>close button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>close button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledCloseButton()
	 */
	public void setEnabledCloseButton(boolean b);

	/**
	 * <p>Determines whether the <i>close button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>close button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledCloseButton(boolean)
	 */
	public boolean isEnabledCloseButton();

	/**
	 * <p>Enables (or disables) the <i>exit button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>exit button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledExitButton()
	 */
	public void setEnabledExitButton(boolean b);

	/**
	 * <p>Determines whether the <i>exit button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>exit button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledExitButton(boolean)
	 */
	public boolean isEnabledExitButton();

	/**
	 * <p>Enables (or disables) the <i>see details button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>see details button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledSeeDetailsButton()
	 */
	public void setEnabledSeeDetailsButton(boolean b);

	/**
	 * <p>Determines whether the <i>see details button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>see details button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledSeeDetailsButton(boolean)
	 */
	public boolean isEnabledSeeDetailsButton();

	/**
	 * <p>Enables (or disables) the <i>hide details button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>hide details button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledHideDetailsButton()
	 */
	public void setEnabledHideDetailsButton(boolean b);

	/**
	 * <p>Determines whether the <i>hide details button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>hide details button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledHideDetailsButton(boolean)
	 */
	public boolean isEnabledHideDetailsButton();

	/**
	 * <p>Enables (or disables) the <i>pause button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>pause button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledPauseButton()
	 */
	public void setEnabledPauseButton(boolean b);

	/**
	 * <p>Determines whether the <i>pause button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>pause button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledPauseButton(boolean)
	 */
	public boolean isEnabledPauseButton();

	/**
	 * <p>Enables (or disables) the <i>restart button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>restart button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledRestartButton()
	 */
	public void setEnabledRestartButton(boolean b);

	/**
	 * <p>Determines whether the <i>restart button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>restart button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledRestartButton(boolean)
	 */
	public boolean isEnabledRestartButton();

	/**
	 * <p>Enables (or disables) the <i>save button</i>.</p>
	 * 
	 * @param b <code>true</code> to enable the <i>save button</i>, otherwise <code>false</code>
	 * 
	 * @see #isEnabledSaveButton()
	 */
	public void setEnabledSaveButton(boolean b);

	/**
	 * <p>Determines whether the <i>save button</i> is enabled. An enabled component can respond
	 *  to user input and generate events.</p>
	 * 
	 * @return <code>true</code> if the <i>save button</i> is enabled, <code>false</code> otherwise
	 * 
	 * @see #setEnabledSaveButton(boolean)
	 */
	public boolean isEnabledSaveButton();
}
