package com.iver.cit.gvsig.project.documents.view.legend.edition.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class PreviewIcon extends JPanel {
	private ImageIcon icon;
	private boolean isSelected;

	/**
	 *
	 */
	public PreviewIcon() {
		super();
			initialize();
	}


	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
        this.setSize(70, 52);

	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (icon == null)
			return;

		Graphics2D g2 = (Graphics2D) g;
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g.setClip(0,0,getWidth(), getHeight());
		//Rectangle r = g.getClipBounds(); //getClipRect();
		if (isSelected)
			g2.setColor(Color.blue);
		else
			g2.setColor(Color.white);

		//g2.setColor(color);
		g2.fillRect(0,0,getWidth(),getHeight());
		g2.drawImage(icon.getImage(),0,0,this);

	}
	public void setSelected(boolean b) {
		isSelected=b;
	}
	public void setIcon(ImageIcon icon) {
		this.icon=icon;
		repaint();
	}
	public ImageIcon getIcon() {
		return icon;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#isShowing()
	 */
	public boolean isShowing() {
		return true; // super.isShowing();
	}


	public boolean isSelected() {
		return isSelected;
	}


	public synchronized void addMouseListener(MouseListener arg0) {
		super.addMouseListener(arg0);

	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
