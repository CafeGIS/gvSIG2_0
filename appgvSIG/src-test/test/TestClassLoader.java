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
* $Id: TestClassLoader.java 8590 2006-11-08 10:57:55Z jaume $
* $Log$
* Revision 1.1  2006-11-08 10:57:55  jaume
* remove unecessary imports
*
*
*/
package test;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.jar.JarException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TestClassLoader extends ClassLoader{
	private Hashtable jarz = new Hashtable();
	private Hashtable classes = new Hashtable();
	private String baseDir;

	public TestClassLoader(String baseDir) throws IOException {
		this.baseDir = baseDir;

		File dir = new File(baseDir);


		URL[] jars = (URL[]) getJarURLs(dir).toArray(new URL[0]);

		if (jars == null) {
			throw new IllegalArgumentException("jars cannot be null"); //$NON-NLS-1$
		}

		//Se itera por las URLS que deben de ser jar's
		ZipFile[] jarFiles = new ZipFile[jars.length];

		for (int i = 0; i < jars.length; i++) {
			jarFiles[i] = new ZipFile(jars[i].getPath());
			Enumeration files = jarFiles[i].entries();
			while (files.hasMoreElements()) {
				//Se obtiene la entrada
				ZipEntry file = (ZipEntry) files.nextElement();
				String fileName = file.getName();

				//Se obtiene el nombre de la clase
				if (!fileName.toLowerCase().endsWith(".class")) { //$NON-NLS-1$
					continue;
				}

				fileName = fileName.substring(0, fileName.length() - 6).replace('/',
				'.');

				//Se cromprueba si ya había una clase con dicho nombre
				if (jarz.get(fileName) != null) {
					throw new JarException(
							"two or more classes with the same name in the jars: " +
							fileName);
				}

				//Se registra la clase
				jarz.put(fileName, jarFiles[i]);
				try {
					loadClass(fileName, true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private ArrayList getJarURLs(File file) {
		ArrayList jars = new ArrayList();
		if (file.isDirectory()) {
			String[] fileNames = file.list();
			for (int i = 0; i < fileNames.length; i++) {
				File file1 = new File(file+ File.separator + fileNames[i]);
				if (file1.isDirectory()) {
					jars.addAll(getJarURLs(file1));
				} else {
					if (file1.getAbsolutePath().endsWith(".jar")) {
						try {
							jars.add(file1.toURL());
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} else {
			if (file.getAbsolutePath().endsWith(".jar")) {
				try {
					jars.add(new URL(file.getAbsolutePath()));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		return jars;
	}

	protected synchronized Class loadClass(String className, boolean resolve) throws ClassNotFoundException {
		Class c;
		System.out.print("Loading class ["+className+"]...");
		try {
			c = super.loadClass(className, resolve);
		} catch (ClassNotFoundException e) {
			ZipFile file = (ZipFile) jarz.get(className);
			ZipEntry classFile = file.getEntry(className);
			byte[] classBytes;
			try {
				classBytes = loadClassBytes(classFile, file.getInputStream(classFile));
			} catch (IOException e1) {
				throw new ClassNotFoundException(className);
			}
			c = defineClass(className, classBytes, 0, classBytes.length);
		}
		System.out.println(" Ok!");

		return c;
	}

	protected Class findClass(String className) throws ClassNotFoundException {
		return loadClass(className);
	}

	private byte[] loadClassBytes(ZipEntry classFile, InputStream is)
	throws IOException {
		// Get size of class file
		int size = (int) classFile.getSize();

		// Reserve space to read
		byte[] buff = new byte[size];

		// Get stream to read from
		DataInputStream dis = new DataInputStream(is);

		// Read in data
		dis.readFully(buff);

		// close stream
		dis.close();

		// return data
		return buff;
	}

	public static void main(String args[]) {
		try {
			TestClassLoader cl = new TestClassLoader("lib-test/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
