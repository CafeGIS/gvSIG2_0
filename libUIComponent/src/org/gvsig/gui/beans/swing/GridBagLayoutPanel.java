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
* $Id: GridBagLayoutPanel.java 13655 2007-09-12 16:28:55Z bsanchez $
* $Log$
* Revision 1.2  2007-09-12 16:28:23  bsanchez
* *** empty log message ***
*
* Revision 1.1  2007/08/20 08:34:46  evercher
* He fusionado LibUI con LibUIComponents
*
* Revision 1.11  2006/10/23 09:53:07  jaume
* *** empty log message ***
*
* Revision 1.10  2006/10/18 07:29:21  jaume
* *** empty log message ***
*
* Revision 1.9  2006/08/23 07:33:32  jaume
* *** empty log message ***
*
* Revision 1.8  2006/08/22 07:38:58  jaume
* added support for detecting changes keyboard and mouse changes over the componenets added via addComponent methods
*
* Revision 1.7  2006/08/11 16:36:15  azabala
* *** empty log message ***
*
* Revision 1.6  2006/08/07 14:45:14  azabala
* added a convenience method to specify insets and gridbagconstraints
*
* Revision 1.5  2006/07/28 15:17:43  azabala
* added the posibility to customize insets of the panel which has each row
*
* Revision 1.4  2006/07/12 11:23:56  jaume
* *** empty log message ***
*
* Revision 1.3  2006/07/04 16:56:10  azabala
* new method to add three componentes
*
* Revision 1.2  2006/07/03 09:29:09  jaume
* javadoc
*
* Revision 1.1  2006/06/29 06:27:10  jaume
* *** empty log message ***
*
*
*/
package org.gvsig.gui.beans.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A panel designed for make the production of forms easier and faster.<br><br>
 *
 * It is a JPanel with a GridBagLayout that allows easily adding new components
 * in rows that are automatically added and aligned by using the
 * addComponent(...) methods.
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 *
 */
public class GridBagLayoutPanel extends JPanel{
  private static final long serialVersionUID = -5583978286938513624L;
	private GridBagLayout gridBag;
	private boolean changed;
	/**
	 * The number of components already added to the layout manager.
	 */
	protected int y;
	private MyValueChangeListener lst;


	public GridBagLayoutPanel() {
		setLayout(gridBag = new GridBagLayout());
		lst = new MyValueChangeListener();
	}

	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param label The label
	 * @param comp The component
	 */
	public void addComponent(String label, Component comp)
	{
		JLabel l = newLabel(label, comp);
		l.setBorder(new EmptyBorder(0,0,0,12));
		addComponent(l,comp,GridBagConstraints.BOTH);
	}

	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param label The label
	 * @param comp The component
	 * @param fill Fill parameter to GridBagConstraints for the right
	 * component
	 */
	public void addComponent(String label, Component comp, int fill)
	{
		JLabel l = newLabel(label, comp);
		l.setBorder(new EmptyBorder(0,0,0,12));
		addComponent(l,comp,fill);
	}


	public void addComponent(String label, Component comp, Insets insets)
	{
		addComponent(label, comp, GridBagConstraints.BOTH, insets);
	}

	public void addComponent(String label, Component comp, int fill, Insets insets){
		JLabel l = newLabel(label, comp);
		l.setBorder(new EmptyBorder(0,0,0,12));
		addComponent(l,comp, fill, insets);
	}


	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param comp1 The label
	 * @param comp2 The component
	 *
	 * @since jEdit 4.1pre3
	 */
	public void addComponent(Component comp1, Component comp2)
	{
		addComponent(comp1,comp2,GridBagConstraints.BOTH);
	}

	/**
	 * Adds two components in a single line using the default inset (borders of margin)
	 * @param comp1
	 * @param comp2
	 * @param fill
	 */
	public void addComponent(Component comp1, Component comp2, int fill){
		addComponent(comp1, comp2, fill, new Insets(1,0,1,0));
	}
	/**
	 * Adds a labeled component to the option pane. Components are
	 * added in a vertical fashion, one per row. The label is
	 * displayed to the left of the component.
	 * @param comp1 The label
	 * @param comp2 The component
	 * @param fill Fill parameter to GridBagConstraints for the right
	 * component
	 *
	 * @since jEdit 4.1pre3
	 */
	public void addComponent(Component comp1, Component comp2, int fill, Insets insets)
	{
		copyToolTips(comp1, comp2);
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = 1;
		cons.weightx = 0.0f;
		cons.insets = insets;
		cons.fill = GridBagConstraints.BOTH;

		gridBag.setConstraints(comp1,cons);
		add(comp1);

		cons.fill = fill;
		cons.gridx = 1;
		cons.weightx = 1.0f;
		gridBag.setConstraints(comp2,cons);
		add(comp2);
		comp1.addKeyListener(lst);
		comp1.addMouseListener(lst);
		comp2.addKeyListener(lst);
		comp2.addMouseListener(lst);
	}

	/**
	 * Adds three components in a line using the default insets
	 * @param comp1
	 * @param comp2
	 * @param comp3
	 * @param fill
	 */
	public void addComponent(Component comp1,
			Component comp2,
			Component comp3,
			int fill){
		addComponent(comp1, comp2, comp3, fill, new Insets(1, 0, 1, 0));
	}
	/**
	 * Adds three components (azabala)
	 *
	 * @param comp1
	 * @param comp2
	 * @param comp3
	 * @param fill
	 */
	public void addComponent(Component comp1,
			Component comp2,
			Component comp3,
			int fill,
			Insets insets)
	{
		copyToolTips(comp1, comp2);
		copyToolTips(comp1, comp3);

		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = 1;
		cons.weightx = 0.0f;
		cons.insets = insets;
		cons.fill = GridBagConstraints.BOTH;

		gridBag.setConstraints(comp1,cons);
		add(comp1);

		cons.gridx = 1;
		cons.weightx = 1.0f;
		gridBag.setConstraints(comp2,cons);
		add(comp2);

		//FIXME. REVISAR ESTO QUE SEGURAMENTE ESTE MAL (AZABALA)
		cons.fill = GridBagConstraints.NONE;
		cons.gridx = 2;
		cons.weightx = 1.0f;
		gridBag.setConstraints(comp3, cons);
		add(comp3);
		comp1.addKeyListener(lst);
		comp1.addMouseListener(lst);
		comp2.addKeyListener(lst);
		comp2.addMouseListener(lst);
	}

	/**
	 * Adds three components in a single line using the default BOTH fill constraint
	 * @param comp1
	 * @param comp2
	 * @param comp3
	 */
	public void addComponent(Component comp1,
			Component comp2,
			Component comp3) {
		addComponent(comp1, comp2, comp3, GridBagConstraints.BOTH);
	}



	public void addComponent(Component comp){
		addComponent(comp, new Insets(1, 0, 1, 0));
	}
	/**
	 * Adds a component to the option pane. Components are
	 * added in a vertical fashion, one per row.
	 * @param comp The component
	 */
	public void addComponent(Component comp, Insets insets)
	{
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = GridBagConstraints.REMAINDER;
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = insets;

		gridBag.setConstraints(comp,cons);
		add(comp);
		comp.addKeyListener(lst);
		comp.addMouseListener(lst);

	}

	/**
	 * (azabala)
	 * Adds a component which is going to fill many rows of the grid
	 * (useful to add scrollpanes with list, etc.)
	 *
	 * */
	public void addComponent(Component comp, Insets insets, int numRows){
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = numRows;
		//REVISAR
		y += numRows;
		cons.gridwidth = GridBagConstraints.REMAINDER;
		cons.fill = GridBagConstraints.HORIZONTAL;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = insets;
		cons.weighty = 0.0f;

		gridBag.setConstraints(comp,cons);
		add(comp);
		comp.addKeyListener(lst);
		comp.addMouseListener(lst);

	}


	///////ADDING SEPARATORS











	public void addComponent(Component comp, int fill){
		addComponent(comp, fill, new Insets(1, 0, 1, 0));
	}

	/**
	 * Adds a component to the option pane. Components are
	 * added in a vertical fashion, one per row.
	 * @param comp The component
	 * @param fill Fill parameter to GridBagConstraints
	 */
	public void addComponent(Component comp, int fill, Insets insets)
	{
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridy = y++;
		cons.gridheight = 1;
		cons.gridwidth = GridBagConstraints.REMAINDER;
		cons.fill = fill;
		cons.anchor = GridBagConstraints.WEST;
		cons.weightx = 1.0f;
		cons.insets = insets;

		gridBag.setConstraints(comp,cons);
		add(comp);
		comp.addKeyListener(lst);
		comp.addMouseListener(lst);
	}

	private void copyToolTips (Component c1, Component c2) {
		int tooltips = 0;
		int jc=0;
		String text = null;
		JComponent jc1 = null, jc2 = null;
		try {
			jc1 = (JComponent) c1;
			text = jc1.getToolTipText();
			++jc;
			if (text != null && text.length() > 0) tooltips++;
		}
		catch (Exception e) {}
		try {
			jc2 = (JComponent) c2;
			String text2 = jc2.getToolTipText();
			++jc;
			if (text2 != null && text2.length() > 0) {
				text = text2;
				tooltips++;
			}
		}
		catch (Exception e) {}
		if (tooltips == 1 && jc == 2) {
			jc1.setToolTipText(text);
			jc2.setToolTipText(text);
		}
	}

	/**
	 *	@return a label which has the same tooltiptext as the Component
	 *    that it is a label for. This is used to create labels from inside
	 *    AbstractPreferencePage.
	 */
	public JLabel newLabel(String label, Component comp)
	{
		JLabel retval = new JLabel(label);
		try /* to get the tooltip of the component */
		{
			JComponent jc = (JComponent) comp;
			String tttext = jc.getToolTipText();
			retval.setToolTipText(tttext);
		}
		catch (Exception e)
		{
			/* There probably wasn't a tooltip,
			 * or it wasn't a JComponent.
			   We don't care. */
		}
		return retval;
	}

	/**
	 * Adds an empty row to the form. It can be used as a separator to
	 * improve panel appearance and comprehension.
	 */
	public void addBlank() {
		addComponent(new JBlank(1,1));
	}

	public boolean hasChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	private class MyValueChangeListener implements KeyListener, MouseListener {
		public void keyPressed(KeyEvent e)      { changed = true; }
		public void keyReleased(KeyEvent e)     { changed = true; }
		public void keyTyped(KeyEvent e)        { changed = true; }
		public void mouseClicked(MouseEvent e)  { changed = true; }
		public void mouseEntered(MouseEvent e)  { changed = true; }
		public void mouseExited(MouseEvent e)   { changed = true; }
		public void mousePressed(MouseEvent e)  { changed = true; }
		public void mouseReleased(MouseEvent e) { changed = true; }
	}
}
