/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
 * $Id: FFrameBoxDialog.java 26449 2009-02-10 15:29:00Z jmvivo $
 * $Log$
 * Revision 1.6  2007-03-06 16:36:19  caballero
 * Exceptions
 *
 * Revision 1.5  2007/01/23 13:10:25  caballero
 * valor no numérico
 *
 * Revision 1.4  2006/12/20 14:42:15  caballero
 * Remodelado Layout
 *
 * Revision 1.3  2006/12/11 17:40:35  caballero
 * ajustar a grid en el Layout
 *
 * Revision 1.2.2.2  2006/12/11 11:14:10  caballero
 * no clonar el fframe hasta darle aceptar
 *
 * Revision 1.2.2.1  2006/11/15 04:10:43  jjdelcerro
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/03 12:09:31  jmvivo
 * * Ajustado tamaños
 * * cambiados botones aceptar/cancelar por AcceptCancelPanel
 *
 * Revision 1.5.4.1  2006/10/03 12:07:12  jmvivo
 * * Ajustado el dialog
 * * Sustituido los botones aceptar y cancelar por panel AcceptCancelPanel
 *
 * Revision 1.5  2006/08/29 07:56:27  cesar
 * Rename the *View* family of classes to *Window* (ie: SingletonView to SingletonWindow, ViewInfo to WindowInfo, etc)
 *
 * Revision 1.4  2006/06/08 07:57:37  caballero
 * zoom a un raster sobre el Layout
 *
 * Revision 1.3  2006/04/10 06:38:06  caballero
 * FFrameTable
 *
 * Revision 1.2  2006/01/18 15:15:08  caballero
 * eliminar código no usado
 *
 * Revision 1.1  2006/01/12 12:32:12  caballero
 * box
 *
 *
 */

package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameTable;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

import de.ios.framework.swing.NumberField;

public class FFrameBoxDialog extends JPanel implements IFFrameDialog{

	private JPanel jPanel = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private NumberField txtNumColumns = null;
	private NumberField txtNumRows = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private FFrameTable fframebox;
	private Layout m_layout;
	private boolean isAcepted = false;
	private JPanel pBox = null;
	private FFrameTable thefframebox;
	private JPRotation pRotation;
	private AcceptCancelPanel acceptCancel = null;
	private FFrameTable newFFrameBox;

	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al fframe del cajetín.
	 */
	public FFrameBoxDialog(Layout layout, FFrameTable fframe) {
		super();
		fframebox = fframe;
		thefframebox=(FFrameTable)fframebox.cloneFFrame(layout);
		m_layout = layout;
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(null);
		this.setSize(380, 185);
		this.add(getJPanel(), null);
		this.add(getPRotation(),null);
		this.add(getAcceptCancel());
		getPRotation().setRotation(fframebox.getRotation());
	}

	private AcceptCancelPanel getAcceptCancel(){
		if (this.acceptCancel == null){
			this.acceptCancel = new AcceptCancelPanel(
					new java.awt.event.ActionListener() {

						public void actionPerformed(java.awt.event.ActionEvent e) {
							newFFrameBox=(FFrameTable)fframebox.cloneFFrame(m_layout);
							if (getTxtNumColumns().getText().equals("")) {
								getTxtNumColumns().setText("0");
							}
							if (getTxtNumRows().getText().equals("")) {
								getTxtNumRows().setText("0");
							}
							newFFrameBox.setNumColumns(Integer.parseInt(getTxtNumColumns().getText()));
							newFFrameBox.setNumRows(Integer.parseInt(getTxtNumRows().getText()));
							newFFrameBox.setRotation(getPRotation().getRotation());
							Rectangle2D r=fframebox.getBoundBox();
							newFFrameBox.calculateTable(r);
							PluginServices.getMDIManager().closeWindow(FFrameBoxDialog.this);
							//m_layout.refresh();
							isAcepted = true;
						}
					}
					,

					new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							newFFrameBox=null;
							PluginServices.getMDIManager().closeWindow(FFrameBoxDialog.this);
						}
					}
			);
			this.acceptCancel.setBounds(5,150,this.getWidth()-10,30);

		}
		return this.acceptCancel;
	}


	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new java.awt.Rectangle(5,32,165,19));
			jLabel1.setText(PluginServices.getText(this,"num_filas"));
			jLabel = new JLabel();
			jLabel.setBounds(new java.awt.Rectangle(5,8,165,19));
			jLabel.setText(PluginServices.getText(this,"num_columnas"));
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new java.awt.Rectangle(12,9,220,140));
			jPanel.add(jLabel, null);
			jPanel.add(jLabel1, null);
			jPanel.add(getTxtNumColumns(), null);
			jPanel.add(getTxtNumRows(), null);
			jPanel.add(getPBox(), null);

		}
		return jPanel;
	}

	/**
	 * This method initializes txtNumColumns
	 *
	 * @return javax.swing.JTextField
	 */
	private NumberField getTxtNumColumns() {
		if (txtNumColumns == null) {
			txtNumColumns = new NumberField();
			txtNumColumns.setText(String.valueOf(fframebox.getNumColumns()));
			txtNumColumns.setBounds(new java.awt.Rectangle(175,8,35,19));
			txtNumColumns.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if (txtNumColumns.getIntegerValue()== null || getTxtNumColumns().getText().equals("") || getTxtNumColumns().getText().equals("0")) {
						return;
					}
					thefframebox.setNumColumns(txtNumColumns.getIntegerValue().intValue());
					getPBox().repaint();
				}
			});
		}
		return txtNumColumns;
	}

	/**
	 * This method initializes txtNumRows
	 *
	 * @return javax.swing.JTextField
	 */
	private NumberField getTxtNumRows() {
		if (txtNumRows == null) {
			txtNumRows = new NumberField();
			txtNumRows.setText(String.valueOf(fframebox.getNumRows()));
			txtNumRows.setBounds(new java.awt.Rectangle(175,32,35,19));
			txtNumRows.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyReleased(java.awt.event.KeyEvent e) {
					if (txtNumRows.getIntegerValue() == null || getTxtNumRows().getText().equals("") || getTxtNumRows().getText().equals("0")) {
						return;
					}
						thefframebox.setNumRows(txtNumRows.getIntegerValue().intValue());
					getPBox().repaint();
				}
			});
		}
		return txtNumRows;
	}

	public void setRectangle(Rectangle2D r) {
		rect.setRect(r);
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"propiedades_cajetin"));

		return m_viewinfo;
	}

	public boolean getIsAcepted() {
		return isAcepted;
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getPBox() {
		if (pBox == null) {
			pBox = new PanelBox();
			pBox.setBackground(java.awt.Color.white);
			pBox.setSize(new java.awt.Dimension(202,75));
			pBox.setLocation(new java.awt.Point(5,55));
			pBox.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseReleased(java.awt.event.MouseEvent e) {
					///Rectangle2D r = new Rectangle2D.Double(5, 5, 155, 65);
				}
			});
		}
		return pBox;
	}
	/**
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(240, 14, 120, 120);
		}
		return pRotation;
	}
	class PanelBox extends JPanel{
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Rectangle2D r=new Rectangle2D.Double(5,5,this.getWidth()-10,this.getHeight()-10);
			thefframebox.drawBox(r,(Graphics2D)g);
		}

	}
	public IFFrame getFFrame() {
		return newFFrameBox;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
