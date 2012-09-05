/*
 *    Geotools - OpenSource mapping toolkit
 *    (C) 2002, Centre for Computational Geography
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *    This file is based on an origional contained in the GISToolkit project:
 *    http://gistoolkit.sourceforge.net/
 *
 */
/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2004 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.fmap.dal.store.dbf.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.gvsig.fmap.dal.DataTypes;
import org.gvsig.fmap.dal.exception.CloseException;
import org.gvsig.fmap.dal.exception.InitializeException;
import org.gvsig.fmap.dal.exception.UnsupportedEncodingException;
import org.gvsig.fmap.dal.exception.WriteException;
import org.gvsig.fmap.dal.feature.Feature;
import org.gvsig.fmap.dal.feature.FeatureAttributeDescriptor;
import org.gvsig.fmap.dal.feature.FeatureType;

/**
 * A DbaseFileReader is used to read a dbase III format file. The general use of
 * this class is: <CODE><PRE>
 * DbaseFileHeader header = ...
 * WritableFileChannel out = new FileOutputStream("thefile.dbf").getChannel();
 * DbaseFileWriter w = new DbaseFileWriter(header,out);
 * while ( moreRecords ) {
 *   w.write( getMyRecord() );
 * }
 * w.close();
 * </PRE></CODE> You must supply the <CODE>moreRecords</CODE> and
 * <CODE>getMyRecord()</CODE> logic...
 *
 * @author Ian Schneider
 */
public class DbaseFileWriter {

	private DbaseFileHeader header;
	private DbaseFileWriter.FieldFormatter formatter = new DbaseFileWriter.FieldFormatter();
	FileChannel channel;
	private ByteBuffer buffer;
//	private final Number NULL_NUMBER = new Integer(0);
	private final String NULL_STRING = "";
	private final String NULL_DATE = "        ";
	private boolean headDrity = false;

	// TODO: READ HEADER AND STABLIST THE RIGHT CHARSET
	private Charset charset = Charset.forName("ISO-8859-1");

	/**
	 * Create a DbaseFileWriter using the specified header and writing to the
	 * given channel.
	 *
	 * @param header
	 *            The DbaseFileHeader to write.
	 * @param out
	 *            The Channel to write to.
	 *
	 *
	 * @throws InitializeWriterException
	 * @throws IOException
	 *             If errors occur while initializing.
	 */
	public DbaseFileWriter(DbaseFileHeader header, FileChannel out,
			boolean isNew)
			throws InitializeException {
		this.header = header;
		this.channel = out;
		this.headDrity = isNew;

		init();
	}

	private void init() throws InitializeException {
		try {
			if (this.channel.size() < this.header.getHeaderLength()) {
				this.writeHeader();
			}
			buffer = ByteBuffer.allocateDirect(header.getRecordLength());
		} catch (Exception e) {
			throw new InitializeException("DBF Writer", e);
		}
	}

	private void write() throws WriteException {
		buffer.position(0);
		int r = buffer.remaining();
		try {
			while ((r -= channel.write(buffer)) > 0) {
				; // do nothing
			}
		} catch (IOException e) {
			throw new WriteException("DBF Writer", e);
		}
	}

	private void writeHeader() throws WriteException {
		try {
			channel.position(0);
			header.writeHeader(channel);
		} catch (IOException e) {
			throw new WriteException("DBF Writer", e);
		}
	}

	/**
	 * Write a single dbase record.
	 *
	 * @param record
	 *            The entries to write.
	 * @throws UnsupportedEncodingException
	 * @throws WriteException
	 */
	public void append(Feature feature) throws WriteException,
			UnsupportedEncodingException {
		this.fillBuffer(feature);
		try {
			this.moveToEOF();
		} catch (IOException e) {
			throw new WriteException("DbaseFileWriter", e);
		}
		this.header.setNumRecords(this.header.getNumRecords() + 1);
		write();

		this.headDrity = true;
	}

	private void fillBuffer(Feature feature)
	throws UnsupportedEncodingException {
		FeatureType featureType = feature.getType();
		try{
			buffer.position(0);

			// put the 'not-deleted' marker
			buffer.put((byte) ' ');

			Iterator iterator = featureType.iterator();
			while (iterator.hasNext()) {
				FeatureAttributeDescriptor fad = (FeatureAttributeDescriptor) iterator
				.next();
				int type = fad.getDataType();
				if (type == DataTypes.GEOMETRY) {
					continue;
				}
				String fieldString = fieldString(fad, feature);
				if (fieldString == null) {
					if (type == DataTypes.STRING) {
						fieldString = NULL_STRING;
					} else if (type == DataTypes.DATE) {
						fieldString = NULL_DATE;
					} else {
						fieldString = "0";
					}
				}
				try {
					buffer.put(fieldString.getBytes(charset.name()));
				} catch (java.io.UnsupportedEncodingException e) {
					throw new UnsupportedEncodingException(e);
				}
			}
		}catch (Exception e) {
			System.err.println("error");
		}
	}

	private void moveToEOF() throws IOException {
		this.moveTo(this.header.getNumRecords());
	}

	private void moveTo(long numReg) throws IOException {
		//		if (!(channel instanceof FileChannel)) {
		//			throw new IOException(
		//							"DbaseFileWriterNIO: channel is not a FileChannel. Cannot position properly");
		//		}

		long newPos = header.getHeaderLength() + numReg
				* header.getRecordLength();
		if (this.channel.position() != newPos) {
			this.channel.position(newPos);
		}
	}

	/**
	 * Write a single dbase record. Useful to update a dbf.
	 *
	 * @param record
	 *            The entries to write.
	 * @throws WriteException
	 * @throws UnsupportedEncodingException
	 */
	public void update(Feature feature, long numReg) throws WriteException,
			UnsupportedEncodingException {
		this.fillBuffer(feature);

		try {
			this.moveTo(numReg);
		} catch (IOException e) {
			throw new WriteException("DbaseFileWriter", e);
		}

		write();
	}

	private String fieldString(FeatureAttributeDescriptor attr, Feature feature) {
		// FIXME Habria que revisar este metodo, ya que hay caracteristicas
		// 		del campo que se sacan del FType y otroas de la cabecera del DBF.
		// 		Esto tendria que ser uniforme, de un sitio o de otro, pero no
		// 		mezclado.
		int type = attr.getDataType();
		int dbfFieldIndex = this.header.getFieldIndex(attr.getName());
		final int fieldLen = header.getFieldLength(dbfFieldIndex);
		String fieldString = "";
		if (DataTypes.BOOLEAN == type) {
			boolean b = feature.getBoolean(attr.getIndex());
			if (b) {
				fieldString = "T";
			} else {
				fieldString = "F";
			}
		} else if (DataTypes.BYTE == type) {
			fieldString = String.valueOf(feature.getByte(attr.getIndex()));
		} else if (DataTypes.DATE == type) {
			Date date = feature.getDate(attr.getIndex());
			fieldString = formatter.getFieldString(date);
		} else if (DataTypes.DOUBLE == type) {
			double d = feature.getDouble(attr.getIndex());
			fieldString = formatter.getFieldString(fieldLen, header
					.getFieldDecimalCount(attr.getIndex()), d);
		} else if (DataTypes.FLOAT == type) {
			float f = feature.getFloat(attr.getIndex());
			fieldString = formatter.getFieldString(fieldLen, header
					.getFieldDecimalCount(attr.getIndex()), f);
		} else if (DataTypes.INT == type) {
			int integer = feature.getInt(attr.getIndex());
			fieldString = formatter.getFieldString(fieldLen, header
					.getFieldDecimalCount(attr.getIndex()), integer);
		} else if (DataTypes.LONG == type) {
			long l = feature.getLong(attr.getIndex());
			fieldString = formatter.getFieldString(fieldLen, header
					.getFieldDecimalCount(dbfFieldIndex), l);
		} else if (DataTypes.STRING == type) {
			String s = feature.getString(attr.getIndex());
			fieldString = formatter.getFieldString(fieldLen, s);
		}
		return fieldString;

	}

	//  private String fieldString(Object obj,final int col) {
	//    String o;
	//    final int fieldLen = header.getFieldLength(col);
	//    switch (header.getFieldType(col)) {
	//      case 'C':
	//      case 'c':
	//        o = formatter.getFieldString(
	//          fieldLen,
	//          (obj instanceof NullValue)? NULL_STRING : ((StringValue) obj).getValue()
	//        );
	//        break;
	//      case 'L':
	//      case 'l':
	//        o = (obj instanceof NullValue) ? "F" : ((BooleanValue)obj).getValue() == true ? "T" : "F";
	//        break;
	//      case 'M':
	//      case 'G':
	//        o = formatter.getFieldString(
	//          fieldLen,
	//          (obj instanceof NullValue) ? NULL_STRING : ((StringValue) obj).getValue()
	//        );
	//        break;
	//     /* case 'N':
	//      case 'n':
	//        // int?
	//        if (header.getFieldDecimalCount(col) == 0) {
	//
	//          o = formatter.getFieldString(
	//            fieldLen, 0, (Number) (obj == null ? NULL_NUMBER : Double.valueOf(obj.toString()))
	//          );
	//          break;
	//
	//         }
	//      */
	//      case 'N':
	//      case 'n':
	//      case 'F':
	//      case 'f':
	//    	Number number = null;
	//    	if(obj instanceof NullValue){
	//    		number = NULL_NUMBER;
	//    	}else{
	//    		NumericValue gVal = (NumericValue) obj;
	//    		number = new Double(gVal.doubleValue());
	//    	}
	//        o = formatter.getFieldString(fieldLen,
	//        		header.getFieldDecimalCount(col),
	//        			number);
	//        break;
	//      case 'D':
	//      case 'd':
	//          if (obj instanceof NullValue)
	//              o = NULL_DATE;
	//          else
	//              o = formatter.getFieldString(((DateValue)obj).getValue());
	//        break;
	//      default:
	//        throw new RuntimeException("Unknown type " + header.getFieldType(col));
	//    }
	//
	//    return o;
	//  }

	/**
	 * Release resources associated with this writer. <B>Highly recommended</B>
	 *
	 * @throws CloseException
	 * @throws IOException
	 *             If errors occur.
	 */
	public void close() throws CloseException {
		// IANS - GEOT 193, bogus 0x00 written. According to dbf spec, optional
		// eof 0x1a marker is, well, optional. Since the original code wrote a
		// 0x00 (which is wrong anyway) lets just do away with this :)
		// - produced dbf works in OpenOffice and ArcExplorer java, so it must
		// be okay.
		//    buffer.position(0);
		//    buffer.put((byte) 0).position(0).limit(1);
		//    write();

		if (headDrity) {
			try {
				this.writeHeader();
			} catch (WriteException e) {
				throw new CloseException("DbaseFileWriter", e);
			}
		}

		try {
			channel.close();
		} catch (IOException e) {
			throw new CloseException("DBF Writer", e);
		}
		if (buffer instanceof MappedByteBuffer) {
			// NIOUtilities.clean(buffer);
		}

		buffer = null;
		channel = null;
		formatter = null;
	}

	/** Utility for formatting Dbase fields. */
	public static class FieldFormatter {
		private StringBuffer buffer = new StringBuffer(255);
		private NumberFormat numFormat = NumberFormat
				.getNumberInstance(Locale.US);
		private Calendar calendar = Calendar.getInstance(Locale.US);
		private String emtpyString;
		private static final int MAXCHARS = 255;

		public FieldFormatter() {
			// Avoid grouping on number format
			numFormat.setGroupingUsed(false);

			// build a 255 white spaces string
			StringBuffer sb = new StringBuffer(MAXCHARS);
			sb.setLength(MAXCHARS);
			for (int i = 0; i < MAXCHARS; i++) {
				sb.setCharAt(i, ' ');
			}

			emtpyString = sb.toString();
		}

		public String getFieldString(int size, String s) {
			buffer.replace(0, size, emtpyString);
			buffer.setLength(size);

			if (s != null) {
				buffer.replace(0, size, s);
				if (s.length() <= size) {
					for (int i = s.length(); i < size; i++) {
						buffer.append(' ');
					}
				}
			}

			buffer.setLength(size);
			return buffer.toString();
		}

		public String getFieldString(Date d) {

			if (d != null) {
				buffer.delete(0, buffer.length());

				calendar.setTime(d);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1; // returns 0 based month?
				int day = calendar.get(Calendar.DAY_OF_MONTH);

				if (year < 1000) {
					if (year >= 100) {
						buffer.append("0");
					} else if (year >= 10) {
						buffer.append("00");
					} else {
						buffer.append("000");
					}
				}
				buffer.append(year);

				if (month < 10) {
					buffer.append("0");
				}
				buffer.append(month);

				if (day < 10) {
					buffer.append("0");
				}
				buffer.append(day);
			} else {
				buffer.setLength(8);
				buffer.replace(0, 8, emtpyString);
			}

			buffer.setLength(8);
			return buffer.toString();
		}

		public String getFieldString(int size, int decimalPlaces, double n) {
			buffer.delete(0, buffer.length());

			//      if (n != null) {
			numFormat.setMaximumFractionDigits(decimalPlaces);
			numFormat.setMinimumFractionDigits(decimalPlaces);
			numFormat.format(n, buffer, new FieldPosition(
					NumberFormat.INTEGER_FIELD));
			//      }

			int diff = size - buffer.length();
			if (diff >= 0) {
				while (diff-- > 0) {
					buffer.insert(0, ' ');
				}
			} else {
				buffer.setLength(size);
			}
			return buffer.toString();
		}
	}

	public void setCharset(Charset charset) {
		this.charset = charset;

	}

}
