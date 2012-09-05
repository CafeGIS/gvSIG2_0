/*
 * Created on 17-feb-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
package com.iver.cit.gvsig;

import java.awt.Component;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.mapcontext.layers.FLayers;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.IDataWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiException;


/**
 * Extensión para exportar en algunos formatos raster la vista actual.
 *
 * @author Fernando González Cortés
 */
public class Export extends Extension {
	private String lastPath = null;
	private Hashtable<String, MyFileFilter> cmsExtensionsSupported = null;
	private Hashtable<String, MyFileFilter> jimiExtensionsSupported = null;

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		View f = (View) PluginServices.getMDIManager().getActiveWindow();

		if (f == null) {
			return false;
		}

		FLayers layers = f.getModel().getMapContext().getLayers();
		for (int i=0;i< layers.getLayersCount();i++) {
			return layers.getLayer(i).isAvailable();
		}
		return false;
	}

	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.IWindow f = (com.iver.andami.ui.mdiManager.IWindow) PluginServices.getMDIManager()
																								  .getActiveWindow();

		if (f == null) {
			return false;
		}

		return (f instanceof View);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

	/* (non-Javadoc)
	 * @see com.iver.andami.plugins.Extension#postInitialize()
	 */
	public void postInitialize() {
		cmsExtensionsSupported = new Hashtable<String, MyFileFilter>();
		jimiExtensionsSupported = new Hashtable<String, MyFileFilter>();
		cmsExtensionsSupported.put("jpg", new MyFileFilter("jpg",
				PluginServices.getText(this, "jpg"), "cms"));
		jimiExtensionsSupported.put("png",new MyFileFilter("png",
				PluginServices.getText(this, "png"), "jimi"));
		jimiExtensionsSupported.put("bmp",new MyFileFilter("bmp",
				PluginServices.getText(this, "bmp"), "jimi"));
	}

	public static boolean saveImageCMS(File fileDst,BufferedImage srcImage) throws IOException, NotSupportedExtensionException, RasterDriverException {
		RasterLibrary.wakeUp();
		RasterizerImage data = new RasterizerImage(srcImage);
		GeoRasterWriter writer = null;
		writer = GeoRasterWriter.getWriter(
				data,
				fileDst.getAbsolutePath(),
				3,
				new AffineTransform(),
				srcImage.getWidth(),
				srcImage.getHeight(),
				IBuffer.TYPE_IMAGE,
				GeoRasterWriter.getWriter(fileDst.getAbsolutePath()).getParams(),
				null,
				false
		);
		if (writer == null){
			PluginServices.getLogger().error("No supported Format: " + fileDst.getAbsolutePath());
			return false;
		}
		try {
			writer.dataWrite();
		} catch (InterruptedException e) {
		}
		writer.writeClose();
		return true;
	}



	public static boolean saveImageJimi(File fileDst,BufferedImage srcImage) throws Exception{
		try {

			Jimi.putImage(srcImage, fileDst.getAbsolutePath());

		} catch (JimiException e) {
			throw new Exception(fileDst.getAbsolutePath(),e);
		}
		return true;

	}

	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		JFileChooser jfc = new JFileChooser(lastPath);

		jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());

		Iterator<MyFileFilter> iter = cmsExtensionsSupported.values().iterator();
		while (iter.hasNext()){
			jfc.addChoosableFileFilter((FileFilter)iter.next());
		}

		iter = jimiExtensionsSupported.values().iterator();
		while (iter.hasNext()){
			jfc.addChoosableFileFilter((FileFilter)iter.next());
		}

		jfc.setFileFilter((FileFilter)jimiExtensionsSupported.get("png"));
		if (jfc.showSaveDialog(
				(Component) PluginServices.getMainFrame()
				) == JFileChooser.APPROVE_OPTION) {

			BufferedImage tempImage;

			tempImage = ((View) PluginServices.getMDIManager().getActiveWindow()).getImage();

			File f = jfc.getSelectedFile();

			lastPath = f.getParent();

			MyFileFilter filter = (MyFileFilter)jfc.getFileFilter();
			f = filter.normalizeExtension(f);


			if (f.exists()){
				int resp = JOptionPane.showConfirmDialog(
						(Component) PluginServices.getMainFrame(),
						PluginServices.getText(this,
								"fichero_ya_existe_seguro_desea_guardarlo")+
								"\n"+
								f.getAbsolutePath(),
						PluginServices.getText(this,"guardar"), JOptionPane.YES_NO_OPTION);
				if (resp != JOptionPane.YES_OPTION) {
					return;
				}

			}

			if (filter.getInfo().equalsIgnoreCase("cms")){

				try {
					saveImageCMS(f, tempImage);
				} catch (IOException e) {
					NotificationManager.addError("Error exportando la imagen", e);
				} catch (NotSupportedExtensionException e) {
					NotificationManager.addError("Error exportando la imagen: formato no soportado", e);
				} catch (RasterDriverException e) {
					NotificationManager.addError("Error exportando la imagen", e);
				}
			}else if (filter.getInfo().equalsIgnoreCase("jimi")) {
				try {
					saveImageJimi(f, tempImage);
				} catch (Exception e) {
					NotificationManager.addError("Error exportando la imagen", e);
				}

			}


		}
	}
}

/**
 * Servidor de datos desde un BufferedImage. Cada petición es de un tamaño
 * de bloque indicado por el escritor en los parámetros de la llamada por lo 
 * que en la siguiente petición habrá que escribir el bloque siguiente.
 */
class RasterizerImage implements IDataWriter {
	private BufferedImage source  = null;
	private int[]         data    = null;
	private int           y       = 0;

	public RasterizerImage(BufferedImage source) {
		this.source = source;
	}

	/**
	 * Solo necesita implementar el método RGB porque solo se
	 * utiliza para exportar la vista.
	 */
	public int[] readARGBData(int sX, int sY, int nBand){
		return readData( sX, sY, nBand);
	}

	public int[] readData(int sizeX, int sizeY, int nBand) {
		if(nBand == 0) { //Con nBand==0 se devuelven las 3 bandas
			this.data = this.source.getRGB(0, y, sizeX, sizeY, this.data, 0, sizeX);
			y += sizeY;
			return this.data;
		}
		return null;
	}

	public byte[][] readByteData(int sizeX, int sizeY) {
		return null;
	}

	public double[][] readDoubleData(int sizeX, int sizeY) {
		return null;
	}

	public float[][] readFloatData(int sizeX, int sizeY) {
		return null;
	}

	public int[][] readIntData(int sizeX, int sizeY) {
		return null;
	}

	public short[][] readShortData(int sizeX, int sizeY) {
		return null;
	}

}

class MyFileFilter extends FileFilter{

	private String[] extensiones=new String[1];
	private String description;
	private boolean dirs = true;
	private String info= null;

	public MyFileFilter(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}

	public MyFileFilter(String[] ext, String desc,String info) {
		extensiones = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	public MyFileFilter(String ext, String desc,String info) {
		extensiones[0] = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	public MyFileFilter(String ext, String desc, boolean dirs,String info) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
		this.info = info;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		for (int i=0;i<extensiones.length;i++){
			if (extensiones[i].equals("")){
				continue;
			}
			if (getExtensionOfAFile(f).equalsIgnoreCase(extensiones[i])){
				return true;
			}
		}

		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensiones;
	}

	public boolean isDirectory(){
		return dirs;
	}

	private String getExtensionOfAFile(File file){
		String name;
		int dotPos;
		name = file.getName();
		dotPos = name.lastIndexOf(".");
		if (dotPos < 1){
			return "";
		}
		return name.substring(dotPos+1);
	}

	public File normalizeExtension(File file){
		String ext = getExtensionOfAFile(file);
		if (ext.equals("") || !(this.accept(file))){
			return new File(file.getAbsolutePath() + "." + extensiones[0]);
		}
		return file;
	}

	public String getInfo(){
		return this.info;
	}

	public void setInfo(String info){
		this.info = info;
	}

}