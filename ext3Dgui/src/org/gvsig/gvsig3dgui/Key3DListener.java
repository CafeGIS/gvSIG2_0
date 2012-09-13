package org.gvsig.gvsig3dgui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.gvsig.osgvp.planets.Planet;

public class Key3DListener implements KeyListener {

	private Planet planet;

	public Key3DListener(Planet m_planet) {
		// TODO Auto-generated constructor stub
		this.planet = m_planet;
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

		if (e.getKeyCode() == KeyEvent.VK_0) {
			this.planet.setEnabledTextureLayer(0, !this.planet
					.getEnabledTextureLayer(0));
		} else if (e.getKeyCode() == KeyEvent.VK_1) {
			this.planet.setEnabledTextureLayer(1, !this.planet
					.getEnabledTextureLayer(1));
		} else if (e.getKeyCode() == KeyEvent.VK_2) {
			this.planet.setEnabledTextureLayer(2, !this.planet
					.getEnabledTextureLayer(2));
		}else if (e.getKeyCode() == KeyEvent.VK_3) {
			this.planet.setEnabledTextureLayer(3, !this.planet
					.getEnabledTextureLayer(3));
		}else if (e.getKeyCode() == KeyEvent.VK_4) {
			this.planet.setEnabledTextureLayer(4, !this.planet
					.getEnabledTextureLayer(4));
		}else if (e.getKeyCode() == KeyEvent.VK_5) {
			this.planet.setEnabledTextureLayer(5, !this.planet
					.getEnabledTextureLayer(5));
		}else if (e.getKeyCode() == KeyEvent.VK_6) {
			this.planet.setEnabledTextureLayer(6, !this.planet
					.getEnabledTextureLayer(6));
		}else if (e.getKeyCode() == KeyEvent.VK_7) {
			this.planet.setEnabledTextureLayer(7, !this.planet
					.getEnabledTextureLayer(7));
		}else if (e.getKeyCode() == KeyEvent.VK_8) {
			this.planet.setEnabledTextureLayer(8, !this.planet
					.getEnabledTextureLayer(8));
		}else if (e.getKeyCode() == KeyEvent.VK_9) {
			this.planet.setEnabledTextureLayer(9, !this.planet
					.getEnabledTextureLayer(9));
		}
		
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
