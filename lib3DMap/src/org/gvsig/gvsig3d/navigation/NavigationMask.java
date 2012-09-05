package org.gvsig.gvsig3d.navigation;

/**
 * Use this class for save the estate of the Navigations mask butons and keys
 * 
 * @author julio
 *
 */
public class NavigationMask {

	private int btnMask;

	private int keyMask;

	/**
	 * Constructor 
	 * 
	 * @param btn button mask
	 * @param key key mask
	 */
	public NavigationMask(int btn, int key) {
		setBtnMask(btn);
		setKeyMask(key);
	}

	/**
	 * @return
	 */
	public int getBtnMask() {
		return btnMask;
	}

	/**
	 * @param btnMask
	 */
	public void setBtnMask(int btnMask) {
		this.btnMask = btnMask;
	}

	/**
	 * @return
	 */
	public int getKeyMask() {
		return keyMask;
	}

	/**
	 * @param keyMask
	 */
	public void setKeyMask(int keyMask) {
		this.keyMask = keyMask;
	}

}
