/* gvSIG. Geographic Information System of the Valencian Government
 *
 * Copyright (C) 2007-2008 Infrastructures and Transports Department
 * of the Valencian Government (CIT)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 */

/*
* AUTHORS (In addition to CIT):
* 2008 IVER T.I. S.A.   {{Task}}
*/

package org.gvsig.fmap.dal.store.dbf;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

import org.gvsig.fmap.dal.exception.DataException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureType;
import org.gvsig.fmap.dal.feature.exception.AttributeFeatureTypeNotSuportedException;
import org.gvsig.fmap.dal.store.dbf.utils.DbaseFileHeader;
import org.gvsig.fmap.dal.store.dbf.utils.DbaseFileWriter;

public class DBFFeatureWriter {
	protected FeatureType featuretype;
	private DbaseFileWriter dbfWriter = null;
	protected String name;
	private File dbfFile;
	private DbaseFileHeader myHeader;
	private FileChannel dbfChannel;

	protected DBFFeatureWriter(String name) {
		this.name= name;
	}

	public void begin(DBFStoreParameters storeParameters,
			FeatureType featureType, long numRows) throws DataException {

		// TODO if is new set the langID
		try {
			myHeader = DbaseFileHeader.createDbaseHeader(featureType);
		} catch (AttributeFeatureTypeNotSuportedException e1) {
			throw new WriteException(this.name, e1);
		}

		dbfFile = storeParameters.getDBFFile();

		dbfChannel = null;

		try {
			dbfChannel = (FileChannel) getWriteChannel(dbfFile
					.getAbsolutePath());
		} catch (IOException e) {
			throw new WriteException(this.name, e);
		}
		try{
			this.dbfWriter = new DbaseFileWriter(myHeader, dbfChannel, true);


			this.dbfWriter.setCharset(Charset.forName("ISO-8859-1"));
			} catch (InitializeException e) {
				throw new WriteException(this.name, e);
			}


		this.featuretype=featureType;
		dbfFile = storeParameters.getDBFFile();

	}

	public void append(Feature feature) throws DataException {
		// TODO use FeatureProvider
		dbfWriter.append(feature);
	}

	public void end() throws DataException {
		dbfWriter.close();
	}

	public void dispose() {
		dbfWriter = null;

	}

	protected static WritableByteChannel getWriteChannel(String path)
	throws IOException {
		WritableByteChannel channel;

		File f = new File(path);

		if (!f.exists()) {
			//			System.out.println("Creando fichero " + f.getAbsolutePath());

			if (!f.createNewFile()) {
				System.err.print("Error al crear el fichero "
						+ f.getAbsolutePath());
				throw new IOException("Cannot create file " + f);
			}
		}

		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		channel = raf.getChannel();

		return channel;
	}

	/**
	 * @return
	 */
	protected int getRowCount() {
		return this.myHeader.getNumRecords();
	}


}
