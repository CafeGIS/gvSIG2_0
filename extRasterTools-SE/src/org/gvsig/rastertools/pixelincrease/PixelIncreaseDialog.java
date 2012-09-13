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
package org.gvsig.rastertools.pixelincrease;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.gvsig.fmap.mapcontrol.tools.CompoundBehavior;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.IView;


/**
 * Panel del zoom de la vista sobre el cursor.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class PixelIncreaseDialog extends JPanel implements IWindow, MouseListener {
    final private static long      serialVersionUID = -3370601314380922368L;
    private JPopupMenu             menu = null;
    /**
     * Ancho y alto de la ventana
     */
    private int                    width = 170;
    private int                    height = 170;
    /**
     * Ancho y alto de la ventana
     */
    private int                    w = 0;
    private int                    h = 0;
    /**
     * Posición de la ventana en X y en Y
     */
    private int                    posWindowX = 0;
    private int                    posWindowY = 0;
    /**
     * Escala del zoom
     */
    private int                    scale = 8;
    /**
     * Vista asociada al inspector de pixels
     */
    public IView                    view = null;
    /**
     * Posición en X e Y donde se comienza a dibujar dentro del inspector de pixeles
     */
    private int						posX = 0;
    private int						posY = 0;
    /**
     * Posición del pixel en X e Y en relación a las coordenadas del buffer de la vista
     */
    public int                      pixX = 0;
    public int                      pixY = 0;
    /**
     * Valores RGB del pixel seleccionado
     */    
    int                             red = 0, green = 0, blue = 0;
    private WindowInfo 				m_viewinfo = null;
    private boolean					clear = false;
    private Color					color = Color.red;
    private JCheckBoxMenuItem[] 	entry = new JCheckBoxMenuItem[6];
    
	/**
	 * Constructor de la ventana de dialogo para gvSIG.
	 */
	public PixelIncreaseDialog() {
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		setSize(width, height);
		addMouseListener(this);
		
		IWindow active = PluginServices.getMDIManager().getActiveWindow();
		if(active instanceof IView) {
			view = (IView)active;
			WindowInfo wInfo = PluginServices.getMDIManager().getWindowInfo(active);
			posWindowX = wInfo.getX() + wInfo.getWidth() - width;
			posWindowY = wInfo.getY();
		}	
		
		CompoundBehavior.setAllControlsBehavior(new PixelIncreaseBehavior(this));
		initMenu();
		
	}
	
	/**
	 * 
	 * @param clear
	 */
	public void setClear(boolean clear) {
		this.clear = clear;
	}
	
	/**
	 * Inicializa el menú contextual con las opciones de selección del 
	 * zoom.
	 */
	private void initMenu() {
		menu = new JPopupMenu();
		PopupMenuListener lis = new PopupMenuListener() {
			public void popupMenuCanceled( PopupMenuEvent evt ) {
				clear = true;
				repaint();
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			}
		};
		menu.addPopupMenuListener(lis);
				
		ActionListener al = new ActionListener() {
			public void actionPerformed( ActionEvent evt ){
				String txt = ((JMenuItem)evt.getSource()).getText();
				if(txt.compareTo("X4") == 0) {
					scale = 4;
					for (int i = 1; i <= 3; i++) 
						entry[i].setSelected(false);	
					entry[0].setSelected(true);
				}
				if(txt.compareTo("X8") == 0) {
					scale = 8;
					entry[0].setSelected(false);
					entry[1].setSelected(true);
					entry[2].setSelected(false);
					entry[3].setSelected(false);
				}
				if(txt.compareTo("X16") == 0) {
					scale = 16;
					entry[0].setSelected(false);
					entry[1].setSelected(false);
					entry[2].setSelected(true);
					entry[3].setSelected(false);
				}
				if(txt.compareTo("X32") == 0) {
					scale = 32;
					for (int i = 0; i < 3; i++) 
						entry[i].setSelected(false);	
					entry[3].setSelected(true);
				}
				if(txt.compareTo(PluginServices.getText(this, "green")) == 0) {
					color = Color.GREEN;
					entry[4].setSelected(false);
					entry[5].setSelected(true);
				}
				if(txt.compareTo(PluginServices.getText(this, "red")) == 0) {
					color = Color.RED;
					entry[4].setSelected(true);
					entry[5].setSelected(false);
				}
			}
		};
		
		entry[0] = new JCheckBoxMenuItem( "X4" );
		entry[0].addActionListener( al );
	    menu.add(entry[0]);
	    entry[1] = new JCheckBoxMenuItem( "X8" );
	    entry[1].setSelected(true);
	    entry[1].addActionListener( al );
	    menu.add(entry[1]);
	    entry[2] = new JCheckBoxMenuItem( "X16" );
	    entry[2].addActionListener( al );
	    menu.add(entry[2]);
	    entry[3] = new JCheckBoxMenuItem( "X32" );
	    entry[3].addActionListener( al );
	    menu.add(entry[3]);
	    entry[4] = new JCheckBoxMenuItem( PluginServices.getText(this, "red") );
	    entry[4].addActionListener( al );
	    entry[4].setSelected(true);
	    menu.add(entry[4]);
	    entry[5] = new JCheckBoxMenuItem( PluginServices.getText(this, "green") );
	    entry[5].addActionListener( al );
	    menu.add(entry[5]);
	}
	
	/**
	 * Obtiene el buffer de la vista activa y lo dibuja sobre el panel
	 * con los datos de escala y desplazamiento seleccionados.
	 */
	protected void paintComponent(Graphics g) {
		w = getVisibleRect().width;
		h = getVisibleRect().height;
				
		if(clear) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, w, h);
			return;
		}
		
		if(view != null) {
			int sizeCrux = 10;
			
			//Obtenemos valores RGB del Image
			BufferedImage img = view.getMapControl().getImage();
			int value = 0;
			try {
				value = img.getRGB(pixX, pixY);
			} catch (ArrayIndexOutOfBoundsException e) {
				
			}
			red = ((value & 0x00ff0000) >> 16);
			green = ((value & 0x0000ff00) >> 8);
			blue = (value & 0x000000ff);
			
			//Dibujamos el graphics con el zoom
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, w, h);
			((Graphics2D)g).scale(scale, scale);
			g.drawImage(img, posX, posY , this);
			((Graphics2D)g).setTransform(new AffineTransform());
			
			//Dibujamos la información RGB y la cruz
			//g.setXORMode(Color.WHITE);
			g.setColor(color);
			int middleW = w >> 1;
			int middleH = h >> 1;
			g.drawLine(middleW - sizeCrux, middleH, middleW + sizeCrux, middleH);
			g.drawLine(middleW, middleH - sizeCrux, middleW , middleH + sizeCrux);
			g.drawString(red + "," + green + "," + blue, w - 85, h - 3);
		}
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		m_viewinfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
    	m_viewinfo.setTitle(PluginServices.getText(this, "increase"));
    	m_viewinfo.setX(posWindowX);
    	m_viewinfo.setY(posWindowY);
		return m_viewinfo;
	}

	/**
	 * Asigna el zoom de la vista sobre el inspector de pixels
	 * @param scale Escala
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * Asigna la posición en X del control donde se empieza a dibujar
	 * @param posX posición X del Graphics donde se empieza a dibujar
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * Asigna la posición en Y del control donde se empieza a dibujar
	 * @param posY posición Y del Graphics donde se empieza a dibujar
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * Obtiene el alto del control
	 */
	public int getHeight() {
		return (h == 0) ? height : h;
	}

	/**
	 * Obtiene el ancho del control
	 */
	public int getWidth() {
		return (w == 0) ? width : w;
	}

	/**
	 * Obtiene el factor de escala
	 * @return 
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Obtiene la vista asociada al inspector de pixeles
	 * @return IView
	 */
	public IView getView() {
		return view;
	}

	/**
	 * Asigna la vista asociada al inspector de pixeles
	 * @param IView
	 */
	public void setView(IView view) {
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		clear = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		clear = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		clear = true;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {
			menu.show( e.getComponent(), e.getX(), e.getY() );
			clear = true;
			repaint();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}
