package com.iver.ai2.gvsig3d.resources;

import java.io.File;

/**
 * @author Julio Campos 
 * 
 * This class is a factory to find resources in 3D extensions
 * 
 */
public class ResourcesFactory {

	private static String textPath;

	private static String extPath;

	static {
		extPath = "/gvSIG/extensiones/org.gvsig.ext3Dgui/resources/";

		textPath = System.getProperty("user.dir") + extPath;
	}

	/**
	 * Method to get Path resources directory
	 * 
	 * @return
	 */
	public static String getResourcesPath() {
		return textPath;
	}

	/**
	 * Method to get path to specific resource
	 * 
	 * @param name
	 *            Name of resource
	 * @return All path resource
	 */
	public static String getResourcePath(String name) {
		return textPath + name;
	}

	/**
	 * Method to get a list of all resources
	 * 
	 * @return String array with all resources
	 */
	public static String[] getResources() {
		String[] resourcesList;

		File dir = new File(ResourcesFactory.getResourcesPath());

		resourcesList = dir.list();
		if (resourcesList == null) {
			// Either dir does not exist or is not a directory
		} else {
			for (int i = 0; i < resourcesList.length; i++) {
				// Get filename of file or directory
				String filename = resourcesList[i];
			}
		}

		return resourcesList;
	}

	/**
	 * Method to verify if this resource exits
	 * 
	 * @param name
	 *            Name of resource
	 * @return True or false
	 */
	public static boolean exitsResouce(String name) {
		boolean exit = false;
		String[] resourcesList = ResourcesFactory.getResources();
		if (resourcesList != null) {

			for (int i = 0; i < resourcesList.length; i++) {
				// Get filename of file or directory
				String filename = resourcesList[i];
				if (filename.equals(name))
					return true;
			}
		}

		return exit;
	}

	/**
	 * Method to set the extension path
	 * 
	 * @param extPath
	 */
	public static void setExtPath(String extPath) {
		ResourcesFactory.extPath = extPath;
		textPath = System.getProperty("user.dir") + extPath;
	}

	/**
	 * Method to get the extension path
	 * 
	 * @param extPath
	 */
	public static String getExtPath() {
		return ResourcesFactory.extPath;
	}
}
