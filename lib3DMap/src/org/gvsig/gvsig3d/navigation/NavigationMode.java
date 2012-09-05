package org.gvsig.gvsig3d.navigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gvsig.osgvp.planets.CustomTerrainManipulator;

public class NavigationMode {

	/**
	 * Custom terrain manipulator
	 */
	private CustomTerrainManipulator CTM;

	/**
	 * List of roll manipulators
	 */
	private static List manRollList;

	/**
	 * List of zoom manipulator
	 */
	private static List manZoomList;

	/**
	 * List of azimut manipulator
	 */
	private static List manAzimutList;

	/**
	 * Contrustor method
	 * 
	 * They save the custom terrain manipulator for use in the future.
	 * 
	 * @param view3D
	 *            View3D object
	 */
	public NavigationMode(CustomTerrainManipulator CTM) {

		// Getting the custom terrain manipulator
		this.CTM = CTM;

		if (manRollList == null){
			// Gettin list of roll manipulator
			manRollList = new ArrayList();
			for (int i = 0; i < CTM.getRollButtonMaskSize(); i++) {
				// Save button and key mask
				NavigationMask navMask = new NavigationMask(
						CTM.getRollMouseMask(i), CTM.getRollKeyMask(i));
				// Adding to list
				manRollList.add(navMask);
			}
		}

		if (manZoomList == null){
			// Gettin list of zoom manipulator
			manZoomList = new ArrayList();
			for (int i = 0; i < CTM.getZoomButtonMaskSize(); i++) {
				// Save button and key mask
				NavigationMask navMask = new NavigationMask(
						CTM.getZoomMouseMask(i), CTM.getZoomKeyMask(i));
				// Adding to list
				manZoomList.add(navMask);
			}
		}

		if (manAzimutList == null){
			// Gettin list of roll manipulator
			manAzimutList = new ArrayList();
			for (int i = 0; i < CTM.getAzimButtonMaskSize(); i++) {
				// Save button and key mask
				NavigationMask navMask = new NavigationMask(
						CTM.getAzimMouseMask(i), CTM.getAzimKeyMask(i));
				// Adding to list
				manAzimutList.add(navMask);
			}
		}
	}

	/**
	 * Method to restore defaul roll mode manipulator
	 */
	public void RestoreRollMode() {

		// Setting left button for roll mode
		for (Iterator iter = manRollList.iterator(); iter.hasNext();) {
			// Get the mask element
			NavigationMask mask = (NavigationMask) iter.next();
			// Settin mode
			CTM.addRollButtonMask(mask.getBtnMask(), mask.getKeyMask());

		}

	}

	/**
	 * Method to restore default zoom mode manipulator
	 */
	public void RestoreZoomMode() {

		// Setting left button for roll mode
		for (Iterator iter = manZoomList.iterator(); iter.hasNext();) {
			// Get the mask element
			NavigationMask mask = (NavigationMask) iter.next();
			// Settin mode
			CTM.addZoomButtonMask(mask.getBtnMask(), mask.getKeyMask());

		}
	}

	/**
	 * Method to restore default azimut mode manipulator
	 */
	public void RestoreAzimutMode() {

		// Setting left button for roll mode
		for (Iterator iter = manAzimutList.iterator(); iter.hasNext();) {
			// Get the mask element
			NavigationMask mask = (NavigationMask) iter.next();
			// Settin mode
			CTM.addAzimButtonMask(mask.getBtnMask(), mask.getKeyMask());

		}

	}

	/**
	 * Method to remove navigation altogether
	 */
	public void SetStopMode() {
		// Removing all controls
		removeAllModes();
	}

	/**
	 * Method to set up roll mode in left mouse button
	 */
	public void SetRollMode() {
		// Relmoving all controls
		removeAllModes();

		CTM.addRollButtonMask(
				CustomTerrainManipulator.MouseButtonMaskType.LEFT_MOUSE_BUTTON,
				0);
	}

	/**
	 * Method to set up zoom mode in left mouse button
	 */
	public void SetZoomMode() {
		// Relmoving all controls
		removeAllModes();
		// Settin mode
		CTM.addZoomButtonMask(
				CustomTerrainManipulator.MouseButtonMaskType.LEFT_MOUSE_BUTTON,
				0);
	}

	/**
	 * Method to set up azimut mode in left mouse button
	 */
	public void SetAzimutMode() {
		// Relmoving all controls
		removeAllModes();
		CTM.addAzimButtonMask(
				CustomTerrainManipulator.MouseButtonMaskType.LEFT_MOUSE_BUTTON,
				0);
	}

	/**
	 * Method to set up default mode in left mouse button
	 */
	public void SetDefaultMode() {
		// Removing all controls
		removeAllModes();

		RestoreRollMode();
		RestoreZoomMode();
		RestoreAzimutMode();

	}
	
	/**
	 * Method to set up azimut and roll mode in left mouse button
	 */
	public void SetAzimutRollMode() {
		// Relmoving all controls
		removeAllModes();
		CTM.addAzimButtonMask(
				CustomTerrainManipulator.MouseButtonMaskType.RIGHT_MOUSE_BUTTON,
				0);
		CTM.addRollButtonMask(
				CustomTerrainManipulator.MouseButtonMaskType.LEFT_MOUSE_BUTTON,
				0);
	}

	/**
	 * Method to remove all mask button manipulator
	 */
	public void removeAllModes() {
		// For remove a button mask always remove 0 index. Why the list of
		// button mask auto-reorder her elements.

		// Removing roll modes
		for (int i = 0; i < manRollList.size(); i++) {
			CTM.removeRollButtonMask(0);
		}

		// Removing zoom modes
		for (int i = 0; i < manZoomList.size(); i++) {
			CTM.removeZoomButtonMask(0);
		}

		// Removing azimut modes
		for (int i = 0; i < manAzimutList.size(); i++) {
			CTM.removeAzimButtonMask(0);
		}
	}

	public static void removeAllNavigationModes(CustomTerrainManipulator ctm) {
		// For remove a button mask always remove 0 index. Why the list of
		// button mask auto-reorder her elements.
		if (ctm != null){

			// Removing roll modes
			for (int i = 0; i < manRollList.size(); i++) {
				ctm.removeRollButtonMask(0);
			}
			
			// Removing zoom modes
			for (int i = 0; i < manZoomList.size(); i++) {
				ctm.removeZoomButtonMask(0);
			}
			
			// Removing azimut modes
			for (int i = 0; i < manAzimutList.size(); i++) {
				ctm.removeAzimButtonMask(0);
			}
		}
	}
	
	public static void restoreAllNavigationModes(CustomTerrainManipulator ctm) {
		if (ctm !=null){
			
			for (int i = 0; i < manRollList.size(); i++) {
				// Get the mask element
				NavigationMask mask = (NavigationMask) manRollList.get(i);
				// Settin mode
				ctm.addRollButtonMask(mask.getBtnMask(), mask.getKeyMask());
			}
			for (int i = 0; i < manZoomList.size(); i++) {
				// Get the mask element
				NavigationMask mask = (NavigationMask) manZoomList.get(i);
				// Settin mode
				ctm.addZoomButtonMask(mask.getBtnMask(), mask.getKeyMask());
			}
			for (int i = 0; i < manAzimutList.size(); i++) {
				// Get the mask element
				NavigationMask mask = (NavigationMask) manAzimutList.get(i);
				// Settin mode
				ctm.addAzimButtonMask(mask.getBtnMask(), mask.getKeyMask());
			}
			
		}
		
	}

}
