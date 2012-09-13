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
* 2008 Geographic Information research group: http://www.geoinfo.uji.es
* Departamento de Lenguajes y Sistemas Informáticos (LSI)
* Universitat Jaume I   
* {{Task}}
*/

package org.gvsig.metadata.extended.exchanger;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.gvsig.metadata.Metadata;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;


public class XSLTExchanger implements MDExchanger {
	
	public XSLTExchanger() {}

	public String exportMD(Metadata md, String format) {
		return "";
	}

	public Metadata importMD(Metadata md, String file) {
		return null;
	}
	
	/**
	 * Transforms a metadata String to a format passed as a parameter
	 * @param md		a file containing metadata
	 * @param xsltPath	path of the xslt
	 * @return			a metadata String encoded in 'format' 
	 */
	public String transformMD(File mdPath, String xsltPath) {
		String xml = null;
		//URL xsl = this.getClass().getClassLoader().getResource(/* MDExchangeConstants.MDML_TO_ISO_XSL */);
		File xsl = new File(xsltPath);
		Source src = new StreamSource(mdPath);
		//Source xsltSource = new StreamSource(new File(xsl.getPath()));
		Source xsltSource = new StreamSource(xsl);
		StreamResult result = new StreamResult(new StringWriter());
		TransformerFactory factory = new TransformerFactoryImpl();
		Transformer transformer = null;
		try {
			transformer = factory.newTransformer(xsltSource);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(src, result);
			xml = result.getWriter().toString();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xml;
	} 
	
	private File getFile(String str) {
		String fullPath = null;	
		/*final */File f = null;
		/*final */FileWriter fw = null;
		File f_temp = new File(/*Launcher.getAppHomeDir()+"temp"*/"xslt" + File.separator + "f.xml");
		if ( ! f_temp.exists() ) 
			try {
				f_temp.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		try {
			fullPath = f_temp.getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		f = new File(fullPath);
		try {
			fw = new FileWriter(f);
			//fw.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
			fw.write(str);
			fw.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	private static String getText(File f) {
		String str = null;
		int c = 0;
		final StringBuffer sb = new StringBuffer();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			while ((c = fis.read()) != -1)
				sb.append((char) c);
			str = new String(sb);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return str;
	}


	public static void main(String[] args) {
		String opcion = null;
		String xsltPath = null;
		String strTrans = null;
		XSLTExchanger ex = new XSLTExchanger();
		// Fichero con MD en formato ISO19115 NEM
		File mdPath = new File("xslt" + File.separator + "md_ESIGNMAPASRELIEVESERIE200701180000_es.xml");
		String md = ex.getText(mdPath);
		System.out.println("+++++++++++++++++++++++++++++++\nContenido del fichero md_ESIGNMAPASRELIEVESERIE200701180000_es.xml con MD ISO19115 NEM:\n+++++++++++++++++++++++++++++++" + md);
		
		
		
		do {
			System.out.println("Los metadatos originales están en formato ISO19115.\nElige el formato/codificación para la demo de transformación entre las siguientes opciones:\n1- ISO19139\n2- Dublin Core\n3- FGDC\n4- Salir");
			System.out.print("Introduce el número: ");
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				opcion = br.readLine();
			} catch(Exception e){ e.printStackTrace();}
			switch (Integer.parseInt(opcion)) {
			case 1:
				// XSLT ISO19115 a ISO19139
				xsltPath = "xslt" + File.separator + "ISO19115_ISO19139.xsl";
				strTrans = ex.transformMD(mdPath, xsltPath);
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de ISO19115 NEM a ISO19139:\n+++++++++++++++++++++++++++++++\n" + strTrans);
				// XSLT ISO19139 a ISO19115
				xsltPath = "xslt" + File.separator + "ISO19139_ISO19115.xsl";
				mdPath = ex.getFile(strTrans);
				strTrans = ex.transformMD(mdPath, xsltPath); 
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de ISO19139 a ISO19115:\n+++++++++++++++++++++++++++++++\n" + strTrans);
				break;
				
			case 2:
				// ISO19115 a Dublin Core (qualifieddc)
				xsltPath = "xslt" + File.separator + "ISO_DC.xsl";
				strTrans = ex.transformMD(mdPath, xsltPath);
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de ISO19115 a Dublin Core (qualifieddc):\n+++++++++++++++++++++++++++++++\n" + strTrans);
				// Dublin Core (qualifieddc) to ISO19115
				xsltPath = "xslt" + File.separator + "DC_ISO.xsl";
				mdPath = ex.getFile(strTrans);
				strTrans = ex.transformMD(mdPath, xsltPath);
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de Dublin Core (qualifieddc) a ISO19115:\n+++++++++++++++++++++++++++++++\n" + strTrans);
				break;
				
			case 3:
				// ISO19115 to FGDC
				xsltPath = "xslt" + File.separator + "ISO_FGDC.xsl";
				strTrans = ex.transformMD(mdPath, xsltPath);
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de ISO19115 a FGDC:\n+++++++++++++++++++++++++++++++\n" + strTrans);
				// FGDC to ISO19115
				xsltPath = "xslt" + File.separator + "FGDC_ISO.xsl";
				mdPath = ex.getFile(strTrans);
				strTrans = ex.transformMD(mdPath, xsltPath);
				System.out.println("+++++++++++++++++++++++++++++++\nTransformación de FGDC a ISO19115:\n+++++++++++++++++++++++++++++++\n" + strTrans);
				break;
				
			case 4:
				opcion = "4";
				break;
				
			default:
				System.out.println("Opción incorrecta! Elige un número entre 1 y 4.\n");
				break;
			}
			mdPath = new File("xslt" + File.separator + "md_ESIGNMAPASRELIEVESERIE200701180000_es.xml");
		}
		while (opcion != "4");
	}
}

