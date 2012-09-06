/* gvSIG. Sistema de Informaci�n Geogr�fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib��ez, 50
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
 * $Id: Pager.java 13655 2007-09-12 16:28:55Z bsanchez $
 * $Log$
 * Revision 1.2  2007-09-12 16:28:23  bsanchez
 * *** empty log message ***
 *
 * Revision 1.1  2007/08/20 08:34:45  evercher
 * He fusionado LibUI con LibUIComponents
 *
 * Revision 1.1  2006/03/22 11:18:29  jaume
 * *** empty log message ***
 *
 * Revision 1.4  2006/02/28 15:25:14  jaume
 * *** empty log message ***
 *
 * Revision 1.2.2.6  2006/02/23 10:36:30  jaume
 * *** empty log message ***
 *
 * Revision 1.2.2.5  2006/02/16 10:36:41  jaume
 * *** empty log message ***
 *
 * Revision 1.1  2006/02/16 09:09:05  caballero
 * PAger de Jaume
 *
 * Revision 1.2.2.4  2006/02/10 13:23:18  jaume
 * page value can now be set externally
 *
 * Revision 1.2.2.3  2006/01/31 16:25:24  jaume
 * correcciones de bugs
 *
 * Revision 1.3  2006/01/26 16:07:14  jaume
 * *** empty log message ***
 *
 * Revision 1.2.2.1  2006/01/26 12:59:32  jaume
 * 0.5
 *
 * Revision 1.2  2006/01/24 14:36:33  jaume
 * This is the new version
 *
 * Revision 1.1.2.5  2006/01/19 16:09:30  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.4  2006/01/17 17:05:39  jaume
 * fixed crazy buttons behavior :-P
 *
 * Revision 1.1.2.3  2006/01/17 17:01:55  jaume
 * fixed crazy buttons behavior :-P
 *
 * Revision 1.1.2.2  2006/01/11 12:20:30  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2006/01/10 13:11:38  jaume
 * *** empty log message ***
 *
 * Revision 1.1.2.1  2006/01/10 11:33:31  jaume
 * Time dimension working against Jet Propulsion Laboratory's WMS server
 *
 * Revision 1.1.2.3  2006/01/09 18:10:38  jaume
 * casi con el time dimension
 *
 * Revision 1.1.2.2  2006/01/02 18:08:01  jaume
 * Tree de estilos
 *
 * Revision 1.1.2.1  2005/12/30 08:56:19  jaume
 * *** empty log message ***
 *
 *
 */
/**
 *
 */
package org.gvsig.gui.beans;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;



/**
 * Bean that is useful to browse a very large list of data. It
 * includes a set of navigation buttons to step ahead or behind
 * by one or going to the first and last element of the list as
 * well as an slider and a text field for directly focus on a
 * list item.
 *
 * @author jaume dominguez faus
 *
 */
public class Pager extends DefaultBean {
  private static final long serialVersionUID = 1997136221292929382L;
	private JPanel buttonsPanel = null;
	private JButton btnFastBackward = null;
	private JButton btnBackward = null;
	private JTextField txtItemCountDisplay = null;
	private JButton btnForward = null;
	private JButton btnFastForward = null;
	private JPanel sliderPanel = null;
	private JSlider slider = null;
	private int itemCount;
	private int lowLimit;
	private int currentValue = -1;
	private int orientation;
	private boolean refreshing = false;
	public static int HORIZONTAL=0;
	public static int VERTICAL=1;
	/**
	 * This is the default constructor. Creates a new instance of ItemBrowser with
	 * zero items.
	 */
	public Pager(int orientation){
		super();
		this.orientation=orientation;
		initialize(0, 0);
	}
	
	/**
	 * Creates a new instance of ItemBrowser defining its edges
	 * @param lowIndex, the lowest edge.
	 * @param itemCount, the highest edge.
	 */
	public Pager(int lowIndex, int itemCount,int orientation) {
		super();
		this.orientation=orientation;
		initialize(lowIndex, itemCount);
	}
	
	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize(int lowIndex, int itemCount) {
		setItemCount(itemCount);
		this.lowLimit = lowIndex;
		this.setLayout(null);
		if (orientation==VERTICAL){
			this.setSize(45,305);
			this.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.lightGray,1));
			this.setPreferredSize(new Dimension(40,300));
		}else{
			this.setSize(240, 50);
			this.setPreferredSize(new Dimension(190,50));
		}
		
		this.add(getSliderPanel(), null);
		this.add(getButtonsPanel(), null);
	}
	
	/**
	 * This method initializes buttonsPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(null);
			buttonsPanel.setName("buttonsPanel");
			buttonsPanel.setPreferredSize(new java.awt.Dimension(173,50));
			if (orientation==VERTICAL){
				buttonsPanel.setBounds(3, 182, 35,115);
			}else{
				buttonsPanel.setBounds(5, 25, 240, 25);
			}
			
			buttonsPanel.add(getBtnFastBackward(), null);
			buttonsPanel.add(getBtnBackward(), null);
			buttonsPanel.add(getTxtItemCountDisplay(), null);
			buttonsPanel.add(getBtnForward(), null);
			buttonsPanel.add(getBtnFastForward(), null);
		}
		return buttonsPanel;
	}
	
	/**
	 * This method initializes btnFastBackWard
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnFastBackward() {
		if (btnFastBackward == null) {
			btnFastBackward = new JButton();
			if (orientation==VERTICAL){
				btnFastBackward.setBounds(7, 1, 20, 24);
			}else{
				btnFastBackward.setBounds(2, 1, 20, 24);
			}
			
			btnFastBackward.setEnabled(itemCount!=0);
			btnFastBackward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentValue != lowLimit){
						setValue(lowLimit, true);
					}
				}});
			btnFastBackward.setIcon(new ImageIcon(getClass().getResource("images/fastbackward.png")));
		}
		return btnFastBackward;
	}
	
	/**
	 * This method initializes btnBackward
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnBackward() {
		if (btnBackward == null) {
			btnBackward = new JButton();
			if (orientation==VERTICAL){
				btnBackward.setBounds(7, 21, 20, 24);
			}else{
				btnBackward.setBounds(21, 1, 20, 24);
			}
			
			btnBackward.setEnabled(itemCount!=0);
			btnBackward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentValue > lowLimit ){
						setValue(currentValue-1, true);
					}
				}});
			btnBackward.setIcon(new ImageIcon(getClass().getResource("images/backward.png")));
		}
		return btnBackward;
	}
	
	/**
	 * This method initializes txtItemCountDisplay
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtItemCountDisplay() {
		if (txtItemCountDisplay == null) {
			txtItemCountDisplay = new JTextField();
			txtItemCountDisplay.setEnabled(itemCount!=0);
			txtItemCountDisplay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
			if (orientation==VERTICAL){
				txtItemCountDisplay.setBounds(2,43, 33, 23);
			} else {
				txtItemCountDisplay.setBounds(43, 2, 144, 23);
			}
			
			txtItemCountDisplay.setText(lowLimit+" / "+itemCount);
			txtItemCountDisplay.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					txtItemCountDisplay.setText(currentValue+"");
					txtItemCountDisplay.setSelectionStart(0);
					txtItemCountDisplay.setSelectionEnd(txtItemCountDisplay.getText().length());
				}
			});
			txtItemCountDisplay.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						int v = Integer.parseInt(txtItemCountDisplay.getText());
						v = (v>itemCount) ? itemCount : v;
						setValue(v, true);
					} catch (Exception ex){
						refreshText(currentValue);
					}
					txtItemCountDisplay.transferFocusDownCycle();
				}
			});
			txtItemCountDisplay.setEnabled(false);
		}
		return txtItemCountDisplay;
	}
	
	/**
	 * This, sets the bean value and triggers an event that can be captured
	 * by a listener.
	 * @param number
	 * @param fireEvent, if true then this method will fire the event. If false,
	 * then the value will be changed silently.
	 *
	 */
	public void setValue(int number, boolean fireEvent) {
		if (number < lowLimit)
			number = lowLimit;
		if (number > itemCount-1)
			number = itemCount;
		if (number != currentValue) {
			currentValue = number;
			refreshControls();
			if (fireEvent)
				callValueChanged(new Integer(currentValue));
		}
	}
	
	/**
	 * Refreshes all the mutable controls in this component.
	 */
	private void refreshControls() {
		int normalizedValue = (int) ((currentValue / (float) itemCount)*100);
		refreshSlider(normalizedValue);
		refreshText(currentValue);
	}
	
	/**
	 * Sets the slider to the correct (scaled) position.
	 * @param normalizedValue
	 */
	private void refreshSlider(int normalizedValue) {
		refreshing = true;
		getSlider().setValue(normalizedValue);
		refreshing = false;
	}
	
	/**
	 * @param string
	 */
	private void refreshText(int value) {
		String newText = (value+1) +" / "+itemCount;
		
		if (!getTxtItemCountDisplay().getText().equals(newText))
			getTxtItemCountDisplay().setText(newText);
	}
	
	/**
	 * This method initializes btnForward
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnForward() {
		if (btnForward == null) {
			btnForward = new JButton();
			if (orientation==VERTICAL){
				btnForward.setBounds(7, 67, 20, 24);
			}else{
				btnForward.setBounds(189, 1, 20, 24);
			}
			
			btnForward.setEnabled(itemCount!=0);
			btnForward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentValue < itemCount-1){
						setValue(currentValue+1, true);
					}
				}});
			btnForward.setIcon(new ImageIcon(getClass().getResource("images/forward.png")));
		}
		return btnForward;
	}
	
	/**
	 * This method initializes btnFastForward
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBtnFastForward() {
		if (btnFastForward == null) {
			btnFastForward = new JButton();
			if (orientation==VERTICAL){
				btnFastForward.setBounds(7, 91, 20, 24);
			}else{
				btnFastForward.setBounds(208, 1, 20, 24);
			}
			
			btnFastForward.setEnabled(itemCount!=0);
			btnFastForward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (currentValue < itemCount-1){
						setValue(itemCount-1, true);
					}
				}});
			btnFastForward.setIcon(new ImageIcon(getClass().getResource("images/fastforward.png")));
		}
		return btnFastForward;
	}
	
	/**
	 * This method initializes sliderPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getSliderPanel() {
		if (sliderPanel == null) {
			sliderPanel = new JPanel();
			sliderPanel.setLayout(null);
			sliderPanel.setName("sliderPanel");
			if (orientation==VERTICAL){
				sliderPanel.setBounds(3, 0, 35, 181);
			}else{
				sliderPanel.setBounds(5, 0, 300, 26);
			}
			sliderPanel.setEnabled(false);
			sliderPanel.add(getSlider(), null);
		}
		return sliderPanel;
	}
	
	/**
	 * This method initializes slider
	 *
	 * @return javax.swing.JSlider
	 */
	private JSlider getSlider() {
		if (slider == null) {
			slider = new JSlider();
			slider.setValue(0);
			if (orientation==VERTICAL){
				slider.setOrientation(JSlider.VERTICAL);
				slider.setSize(24, 230);
			}else{
				slider.setOrientation(JSlider.HORIZONTAL);
				slider.setSize(230, 24);
			}
			
			slider.setLocation(0, 1);
			slider.setEnabled(itemCount!=0);
			slider.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					int value = (int) (getSlider().getValue() * itemCount * 0.01);
					if (value >= itemCount)
						value = itemCount - 1;
					refreshText(value);
					setValue(value, false);
				}
			});
			slider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					int value = (int) (getSlider().getValue() * itemCount * 0.01);
					if (value >= itemCount)
						value = itemCount - 1;
					refreshText(value);
					if (!refreshing)
						callValueChanged(new Integer(value));
				}
			});
		}
		return slider;
	}
	
	/**
	 * Sets the amount of items that this component will handle.
	 * @param count
	 */
	public void setItemCount(int count){
		itemCount = count;
		getSlider().setEnabled(count != 0);
		getBtnFastBackward().setEnabled(count != 0);
		getBtnBackward().setEnabled(count != 0);
		getTxtItemCountDisplay().setEnabled(count != 0);
		getBtnForward().setEnabled(count != 0);
		getBtnFastForward().setEnabled(count != 0);
	}
	
	/**
	 * Sets the starting point if none is defined the pager will start from 0.
	 * @param initial position
	 */
	public void setStartingPosition(int initialPosition) {
		lowLimit = initialPosition;
	}
	
	public void setCurrentPosition(int pos) {
		setValue(pos, true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,15"
