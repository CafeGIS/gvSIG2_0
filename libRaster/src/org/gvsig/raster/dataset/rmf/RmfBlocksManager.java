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
package org.gvsig.raster.dataset.rmf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.gvsig.raster.util.RasterUtilities;
/**
 * Gestor para la escritura de bloques XML en el fichero RMF. Cada cliente que quiere
 * escribir en el se registrará a traves de ClientRegister y esta clase será la encargada
 * de gestionar la lectura y escritura de bloques.
 *
 * 21-abr-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class RmfBlocksManager extends ClientRegister implements IRmfBlock {
	private final String MAIN_TAG = "RasterMetaFile";
	private String path = null;

	/**
	 * Constructor. Asigna la ruta del fichero.
	 * @param path
	 */
	public RmfBlocksManager(String path) {
		setPath(path);
	}

	/**
	 * Asigna la ruta del fichero
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Obtiene la ruta del fichero
	 * @return path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Genera una copia de seguridad del fichero del rmf
	 * @throws IOException
	 */
	public void fileBackup() throws IOException {
		RasterUtilities.copyFile(getPath(), getPath() + "~");
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#read(java.lang.String)
	 */
	public void read(String xml) throws ParsingException {
		File file = new File(getPath());
		ArrayList lines = new ArrayList();

		BufferedReader inGrf = null;
		try {
			inGrf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = inGrf.readLine();
			while (str != null) {
				for (int i = 0; i < clients.size(); i++) {
					IRmfBlock block = ((IRmfBlock) clients.get(i));
					String main = block.getMainTag();
					if (str.startsWith("<" + main)) {
						lines.clear();
						while (str.compareTo("</" + main + ">") != 0) {
							lines.add(str);
							str = inGrf.readLine();
						}
						lines.add(str);
						StringBuffer buf = new StringBuffer();
						for (int j = 0; j < lines.size(); j++)
							buf.append((String) lines.get(j));
						block.read(buf.toString());
						break;
					}
				}

				str = inGrf.readLine();
			}
			inGrf.close();
		} catch (FileNotFoundException e) {
			throw new ParsingException("File Input error: creating BufferedReader");
		} catch (IOException ex) {
			throw new ParsingException("File Input error: reading lines");
		}
	}

	/**
	 * Creación de nuevo fichero RMF. Añade la cabecera y vuelca el contenido de
	 * todos los IRmfBlock.
	 * @param file Fichero
	 * @return true si el fichero no existia y se ha creado nuevo
	 * @throws IOException
	 */
	private boolean create(File file) {
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write("<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n");
			writer.write("<RasterMetaFile>\n");
			for (int i = 0; i < clients.size(); i++) {
				IRmfBlock block = ((IRmfBlock) clients.get(i));
				writer.write(block.write());
			}
			writer.write("</RasterMetaFile>\n");
			writer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/*
	 *  (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#write()
	 */
	public String write() throws IOException {
		File file = new File(getPath());
		ArrayList lines = new ArrayList();

		// Si no existe aún el rmf se crea, se añade las cabeceras y se vuelca el
		// contenido de los bloques
		if (!file.exists())
			if (!create(file))
				return null;

		// Añadir bloques al fichero.
		BufferedReader inGrf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String str = inGrf.readLine();
		while (str != null) {
			lines.add(str);
			str = inGrf.readLine();
		}

		// Obtenemos el primer tag de un bloque. Para cada uno recorremos todo el
		// rmf actual buscando ese tag. Si existe se añade el nuevo bloque y se
		// borra el viejo. Si no existe lo añade al final antes de la etiqueta de
		// cierre </RasterMetaFile>
		for (int i = 0; i < clients.size(); i++) {
			IRmfBlock block = ((IRmfBlock) clients.get(i));
			String tag = block.getMainTag();

			for (int line = 0; line < lines.size(); line++) {
				str = (String) lines.get(line);
				if (str.compareTo("</" + getMainTag() + ">") == 0 || str.startsWith("<" + tag)) {
					String xmlBlock = block.write();
					if (line != 0)
						lines.add(line, xmlBlock);
					if (str.startsWith("<" + tag)) {
						while (((String) lines.get(line + 1)).compareTo("</" + tag + ">") != 0)
							lines.remove(line + 1);
						lines.remove(line + 1);
					}
					break;
				}
			}
		}
		inGrf.close();

		// Escribir fichero de salida.
		file.delete();
		file.createNewFile();
		FileWriter writer = new FileWriter(file);

		for (int i = 0; i < lines.size(); i++) {
			if (((String) lines.get(i)).length() == 0)
				continue;
			if (!((String) lines.get(i)).endsWith("\n"))
				writer.write((String) lines.get(i) + "\n");
			else
				writer.write((String) lines.get(i));
		}
		writer.close();

		return null;
	}

	/**
	 * Obtiene un bloque XML que representa a las propiedades del objeto a
	 * serializar. Antes de la operación hace una copia de seguridad del fichero
	 * RMF.
	 * @param rmfBackup Especifica si debe hacer la copia de seguridad.
	 * @return Texto XML que representa el objeto.
	 */
	public String write(boolean rmfBackup) throws IOException, FileNotFoundException {
		if (rmfBackup)
			RasterUtilities.copyFile(getPath(), getPath() + "~");

		return write();
	}

	/**
	 * Método que checkea si el fichero pasado es valido para ser rmf. Si existe
	 * el fichero será valido si se puede escribir en el y no está vacio y no es
	 * un directorio y contiene los caracteres de cabecera de comienzo de XML (<?xml
	 * .... >). En caso de que el fichero no exista también es valido ya que se
	 * creará de cero.
	 * @return true si es un rmf valido y false si no lo es
	 */
	public boolean checkRmf() {
		File f = new File(getPath());
		if (f.exists()) {
			if (!f.canWrite() || f.length() == 0 || f.isDirectory())
				return false;
			BufferedReader inGrf;
			try {
				inGrf = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
				String str = inGrf.readLine();
				if (!str.startsWith("<?xml"))
					return false;
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getMainTag()
	 */
	public String getMainTag() {
		return MAIN_TAG;
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.dataset.io.rmf.IRmfBlock#getResult()
	 */
	public Object getResult() {
		return null;
	}
}