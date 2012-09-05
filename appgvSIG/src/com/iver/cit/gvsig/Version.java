package com.iver.cit.gvsig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import com.iver.cit.gvsig.gui.panels.FPanelAbout;

public class Version {
	public final static int MAJOR_VERSION_NUMBER = 2;
	public final static int MINOR_VERSION_NUMBER = 0;
	public final static int RELEASE_NUMBER = 0;

	private static String BUILD = null;

	public static String format() {

		if (RELEASE_NUMBER < 1) {
			return MAJOR_VERSION_NUMBER +"."+MINOR_VERSION_NUMBER;
		} else {
			return MAJOR_VERSION_NUMBER +"."+MINOR_VERSION_NUMBER+"."+RELEASE_NUMBER;
		}
	}

	public static String longFormat() {

		if (RELEASE_NUMBER < 1) {
			return MAJOR_VERSION_NUMBER +"."+MINOR_VERSION_NUMBER + " (Build " + getBuild() + ")";
		} else {
			return MAJOR_VERSION_NUMBER +"."+MINOR_VERSION_NUMBER+"."+RELEASE_NUMBER + " (Build " + getBuild() + ")";
		}
	}
	public static String getBuild() {
		if (BUILD == null) {
			try {
				// Leemos el nº de build
				BufferedReader fich = new BufferedReader(
						new FileReader(FPanelAbout.class.getResource("/build.number").getFile()));
				fich.readLine();
				fich.readLine();
				String strVer = fich.readLine();
				StringTokenizer strTokenizer = new StringTokenizer(strVer);
				String strToken = strTokenizer.nextToken("=");
				strToken = strTokenizer.nextToken();
				BUILD = strToken;
				fich.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return BUILD;
	}
}
