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

package org.gvsig.gvsig3dgui.layer.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.gui.beans.panelGroup.panels.AbstractPanel;
import org.gvsig.raster.dataset.BandAccessException;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.dataset.properties.DatasetMetadata;
import org.gvsig.raster.datastruct.Extent;
import org.gvsig.raster.hierarchy.IRasterProperties;
import org.gvsig.raster.util.RasterUtilities;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.utiles.swing.JComboBox;
/**
 * Panel de información sobre raster. La información que aparece en este panel
 * es la siguiente:
 * <P>
 * Información básica sobre el dataset:
 * <UL>
 * <LI>Fichero/s que forman el dataset</LI>
 * <LI>Tamaño por fichero</LI>
 * <LI>Ancho y alto</LI>
 * <LI>Formato</LI>
 * <LI>Si tiene georreferenciación o no</LI>
 * <LI>Número de bandas</LI>
 * <LI>Tipo de dato.</LI>
 * </UL>
 * </P>
 * <P>
 * Datos de georreferenciación:
 * <UL>
 * <LI>Proyección</LI>
 * <LI>Coordenadas UTM o geográficas</LI>
 * <LI>Tamaño de pixel</LI>
 * </UL>
 * </P>
 * <P>
 * Bandas:
 * <UL>
 * <LI>Bandas de todos los datasets asociados con el tipo de dato de cada uno y
 * banda de visualización asignada (R, G o B)</LI>
 * <LI>Coordenadas UTM o geográficas</LI>
 * <LI>Tamaño de pixel</LI>
 * </UL>
 * </P>
 * <P>
 * Metadatos
 * </P>
 *
 * @author 
 *
 */
public class InfoPanel3D extends AbstractPanel {
	private static final long serialVersionUID = -3764465947289974528L;
	private final String      bgColor0        = "\"#FEEDD6\""; // light salmon
	private final String      bgColor1        = "\"#EAEAEA\""; // light grey
	private final String      bgColor3        = "\"#FBFFE1\""; // light yellow
	private final String      bgColor4        = "\"#D6D6D6\""; // Gris
	private final String      bgColorBody     = "\"#FFFFFF\""; // white

	private JScrollPane       jScrollPane     = null;
	public JEditorPane        jEditorPane     = null;
	private JComboBox         jComboBox       = null;
	private int               selectedDataSet = 0;

	private boolean           jComboBoxEvent  = false;

	/**
	 * Cabecera de las columnas del dialogo
	 */
	public Object[]           columnNames     = { "Propiedad", "Valor" };

	private IRasterProperties op              = null;

	/**
	 * Booleano que está a true cuando la fila a dibujar es par y a false cuando
	 * es impar.
	 */
	private boolean           rowColor        = true;
	
	
	private Layer3DProps props3D;
	private FLayer lyr;
	private FLyrRasterSE lyr2;

	/**
	 * This is the default constructor
	 */
	public InfoPanel3D() {
		super();
		
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	protected void initialize() {
		setLabel(PluginServices.getText(this, "info"));
		this.setLayout(new BorderLayout(5, 5));
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(getJScrollPane(), BorderLayout.CENTER);
		this.add(getJComboBox(), BorderLayout.SOUTH);
		this.getJEditorPane().repaint();

		getJComboBox().addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				refresh();
			}
		});
		this.setPreferredSize(new Dimension(100, 80));
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.raster.gui.properties.dialog.IRegistrablePanel#initializeUI()
	 */
	public void initializeUI() {
	}

	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getJEditorPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new JComboBox();
		}
		return jComboBox;
	}

	/**
	 * This method initializes jEditorPane
	 *
	 * @return javax.swing.JEditorPane
	 */
	private JEditorPane getJEditorPane() {
		if (jEditorPane == null) {
			jEditorPane = new JEditorPane();
			jEditorPane.setEditable(false);
			jEditorPane.setContentType("text/html");
		}
		return jEditorPane;
	}

	public void resetTable() {
		this.jEditorPane = null;
	}

	/**
	 * Controla la alternatividad de colores en la tabla.
	 *
	 * @return Cadena con el color de la fila siguiente.
	 */
	private String getColor() {
		String color = (rowColor ? bgColor0 : bgColor1);
		rowColor = !rowColor;
		return color;
	}

	/**
	 * Obtiene una entrada de la tabla en formato HTML a partir de una propiedad,
	 * un valor y un color.
	 *
	 * @param prop
	 *          Nombre de la propiedad
	 * @param value
	 *          Valor
	 * @param color
	 *          Color
	 *
	 * @return Entrada HTML de la tabla
	 */
	private String setHTMLBasicProperty(String prop, String value) {
		String content = "<tr valign=\"top\">";
		if (prop != null)
			content += "<td bgcolor=" + bgColor4 + "align=\"right\" width=\"140\"><font face=\"Arial\" size=\"3\">" + prop + ":&nbsp;</font></td>";
		content += "<td bgcolor=" + getColor() + "align=\"left\"><font face=\"Arial\" size=\"3\">" + value + "</font></td>";
		content += "</tr>";

		return content;
	}

	/**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param title
	 *          Nombre del titulo
	 * @param colspan
	 *          Numero de celdas que ocupara el titulo
	 *
	 * @return Entrada HTML del titulo
	 */
	private String setHTMLTitleTable(String title, int colspan) {
		return
			"<tr valign=\"middle\" >" +
			"<td bgcolor=" + bgColor3 + " align=\"center\" colspan=\"" + colspan + "\"><font face=\"Arial\" size=\"3\"><b> " + title + "</b></font></td>" +
			"</tr>";
	}

	/**
	 * Obtiene una cabecera de tabla en formato HTML a partir de un titulo.
	 *
	 * @param content
	 *          Codigo HTML de las filas que componen la tabla.
	 *
	 * @return Entrada HTML de la tabla completa
	 */
	private String setHTMLTable(String content) {
		return "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\">" + content + "</table>";
	}

	/**
	 * Genera el HTML para todo el contenido.
	 *
	 * @param content
	 *          Codigo HTML que ira en el <body>...</body>.
	 *
	 * @return HTML completo
	 */
	private String setHTMLBody(String content) {
		String html = "<html>";
		html += "<body bgcolor=" + bgColorBody + " topmargin=\"0\" marginheight=\"0\">";
		html += content;
		html += "</body>";
		html += "</html>";
		return html;
	}

	/**
	 * Método que crea el código HTML para la tabla de información general dentro
	 * del panel de información de raster
	 *
	 * @return String con el cófigo HTML que corresponde con la tabla de
	 * información general
	 */
	public String tablaInfo() {
		rowColor = true;
		String cabInfo = PluginServices.getText(this, "general_info");
		String propiedades = "";

		String cabecera = setHTMLTitleTable(cabInfo, 2);
		
		if(op!=null) {
			
			op.getFileFormat();
			
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "archivo"), op.getFileName()[selectedDataSet]);
			long fileSize = op.getFileSize()[selectedDataSet];
			if (op instanceof FLyrRasterSE) {
				FLyrRasterSE rasterSE = (FLyrRasterSE) op;
				fileSize = rasterSE.getDataSource().getDataset(selectedDataSet)[0].getFileSize();
			}
			
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "size"), RasterUtilities.formatFileSize(fileSize) + " ");
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "ancho_alto"), op.getPxWidth() + " X " + op.getPxHeight());
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "formato"), op.getFileFormat());
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "georref"), op.isGeoreferenced() ? PluginServices.getText(this, "si") : PluginServices.getText(this, "no"));
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "nbandas"), new Integer(op.getBandCountFromDataset()[selectedDataSet]).toString());
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "tipo_dato"), RasterUtilities.typesToString(op.getDataType()[0]));
		}
		else {
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "archivo"), lyr.getName());
			if(lyr.getInfoString() != null)
				propiedades += setHTMLBasicProperty(PluginServices.getText(this, "Informacion"), lyr.getInfoString());
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "Valor_minimo_de_escala"),String.valueOf(lyr.getMinScale()));
			propiedades += setHTMLBasicProperty(PluginServices.getText(this, "Valor_maximo_de_escala"),String.valueOf(lyr.getMaxScale()));
		}
		
		return setHTMLTable(cabecera + propiedades);
	}

	/**
	 * Método que crea el código HTML para la tabla de Coordenadas Geográficas
	 * dentro del panel de información de raster
	 *
	 * @return String con el código HTML que corresponde con la tabla de
	 *         Coordenadas Geográficas
	 */
	public String tablaCoord() {
		rowColor = true;
		String cabCoord = PluginServices.getText(this, "coor_geograficas");
		String propiedades = "";

		String cabecera = setHTMLTitleTable(cabCoord, 2);

		Extent ext = op.getFullRasterExtent();
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "ul"), ext.getULX() + ", " + ext.getULY());
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "lr"), ext.getLRX() + ", " + ext.getLRY());
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "ur"), ext.getURX() + ", " + ext.getURY());
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "ll"), ext.getLLX() + ", " + ext.getLLY());

		double pixelSizeX = op.getAffineTransform().getScaleX();
		double pixelSizeY = op.getAffineTransform().getScaleY();
		double rotX = op.getAffineTransform().getShearX();
		double rotY = op.getAffineTransform().getShearY();

		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "tamPixX"), String.valueOf(pixelSizeX));
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "tamPixY"), String.valueOf(pixelSizeY));
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "rotX"), String.valueOf(rotX));
		propiedades += setHTMLBasicProperty(PluginServices.getText(this, "rotY"), String.valueOf(rotY));

		return "<br>" + setHTMLTable(cabecera + propiedades);
	}

	/**
	 * Método que crea la tabla de origen de datos dentro del panel
	 * de Información.
	 *
	 * @return String con el código HTML de la tabla.
	 */
	public String tablaOrigen() {
		rowColor = true;
		String propiedades = "";
		String bandType = "";

		String cabOrig = PluginServices.getText(this, "origin");

		String cabecera = setHTMLTitleTable(cabOrig, 2);

		for(int j = 0; j < op.getBandCountFromDataset()[selectedDataSet] ; j++) {
			bandType = RasterUtilities.typesToString(op.getDataType()[j]);

			String data = "";
			data += "Type=" + bandType;
			data += ", ";
			data += "ColorInterp=" + op.getColorInterpretation(j, selectedDataSet);
			data += ", ";
			data += "NoData=" + ((FLyrRasterSE) op).getNoDataValue();

			propiedades += setHTMLBasicProperty("Band " + (j + 1), data);
			
			if (op instanceof FLyrRasterSE) {
				FLyrRasterSE rasterSE = (FLyrRasterSE) op;
				data = "";
				try {
					int count = rasterSE.getDataSource().getDataset(selectedDataSet)[0].getOverviewCount(j);
					if (count > 0) {
						for (int i = 0; i < count; i++) {
							if (!data.equals(""))
								data += ", ";
					//		data += rasterSE.getDataSource().getDataset(selectedDataSet)[0].getOverviewWidth(j, i);
							data += "x";
					//		data += rasterSE.getDataSource().getDataset(selectedDataSet)[0].getOverviewHeight(j, i);
						}
						propiedades += setHTMLBasicProperty("Overviews", data);
					}
				} catch (BandAccessException e) {
					// No se obtiene el contador de overviews
				} catch (RasterDriverException e) {
					// No se obtiene el contador de overviews
				}
			}
		}

		return "<br>" + setHTMLTable(cabecera + propiedades);
	}

	/**
	 * Método para crear la tabla de proyección del raster en el
	 * panel de información de propiedades de raster.
	 *
	 * @return String con el código HTML que genera la tabla de proyección.
	 */
	public String tablaProjection() {
		rowColor = true;
		String propiedades = "";
		String projection = null;

		boolean datos = false;

		String cabProjection = PluginServices.getText(this, "projection");

		String cabecera = setHTMLTitleTable(cabProjection, 1);

		String wktProj = null;
		try {
			wktProj = op.getWktProjection();
		} catch (RasterDriverException e) {
			//No se obtiene la proyección pero no hacemos nada al respecto
		}

		if(wktProj != null) {
			projection = RasterUtilities.parserGdalProj(wktProj);
			if (projection != null){
				datos = true;
				propiedades += setHTMLBasicProperty(null, projection);
			}
		}

		if (datos == false) return "";

		return "<br>" + setHTMLTable(cabecera + propiedades);
	//	return null;
	}

	/**
	 * Método para crear la tabla de información de 3D en el
	 * panel de información de propiedades de raster.
	 *
	 * @param Vector
	 *          con los georasterfiles cargados en la capa.
	 * @return String con el código HTML que genera la tabla.
	 */
	public String tablaMetadatos() {
		rowColor = true;
		String propiedades = "";
		String[] metadatos = null;

		DatasetMetadata meta = null;
		boolean datos = false;

		String cabMeta = PluginServices.getText(this, "metadata");

		String cabecera = setHTMLTitleTable(cabMeta, 2);

		meta = op.getMetadata()[selectedDataSet];
		if (meta != null){
			metadatos = meta.getMetadataString();
			for(int j = 0 ; j<metadatos.length ; j++){
				datos = true;
				int index = metadatos[j].indexOf("=");

				propiedades += setHTMLBasicProperty(metadatos[j].substring(0,index), metadatos[j].substring(index+1));
			}
		}

		if(datos == false)
			return "";

		return "<br>" + setHTMLTable(cabecera + propiedades);
	}

	/**
	 * Método que dibuja las tablas HTML del panel de información dentro de las
	 * propiedades de ráster. Se llama cada vez que se actualiza algún dato de las
	 * tablas.
	 *
	 */
	public void refresh() {
		boolean refresh = false;
		
		if (!jComboBoxEvent) {
			// Set flag to ensure that an infinite loop is not created
			jComboBoxEvent = true;

			selectedDataSet = getJComboBox().getSelectedIndex();
			if (selectedDataSet < 0) selectedDataSet = 0;

			String nameFile = "";
			getJComboBox().removeAllItems();
			if(op!=null) {
				for (int i = 0; i < op.getFileCount(); i++) {
					nameFile = op.getFileName()[i];
					nameFile = nameFile.substring(nameFile.lastIndexOf(File.separator) + 1);
					getJComboBox().addItem(nameFile);
				}
				try {
					// Select previous item
					getJComboBox().setSelectedIndex(selectedDataSet);
				} catch (IllegalArgumentException iae) {
					selectedDataSet = 0;
				}
	
				jComboBoxEvent = false;
				refresh = true;
			}
		}

		String html = "";
		if ((refresh) && (op.getFileCount() >= 1)) {
			html = setHTMLBody(tablaInfo() + tablaCoord() + tablaOrigen() + tablaProjection() + tablaMetadatos());
		}
		else
			html = setHTMLBody(tablaInfo());

		this.getJEditorPane().setContentType("text/html");
		this.getJEditorPane().setText(html);
		this.getJEditorPane().setCaretPosition(0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.AbstractPanel#setReference(java.lang.Object)
	 */
	public void setReference(Object ref) {
		super.setReference(ref);

		if (!(ref instanceof FLayer))
			return;


		lyr = (FLayer) ref;
		if (lyr instanceof IRasterProperties) {
			op = (IRasterProperties) lyr;
			refresh();
		}
			
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#selected()
	 */
	public void selected() {
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#accept()
	 */
	public void accept() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#apply()
	 */
	public void apply() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.gvsig.gui.beans.panelGroup.panels.IPanel#cancel()
	 */
	public void cancel() {
	}
	
}
