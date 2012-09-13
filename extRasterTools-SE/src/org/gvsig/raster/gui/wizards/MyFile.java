package org.gvsig.raster.gui.wizards;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import org.gvsig.fmap.geom.primitive.Envelope;
import org.gvsig.fmap.mapcontrol.MapControl;

import com.iver.cit.gvsig.addlayer.AddLayerDialog;

/**
 * @version 05/09/2007
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
	public class MyFile {
		private File      f;
		private FileFilter filter;
		private IFileOpen fileOpen;


	public MyFile(File f, FileFilter fileFilter, IFileOpen fileOpen) {
			this.f = f;
			this.filter = fileFilter;
			this.fileOpen = fileOpen;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return f.getName();
		}

		public FileFilter getFilter() {
			return filter;
		}

		public IFileOpen getFileOpen() {
			return fileOpen;
		}

		public Envelope createLayer(MapControl mapControl) {
			return fileOpen.createLayer(f, mapControl, filter, AddLayerDialog
				.getLastProjection());
		}
		public File getFile(){
			return f;
		}
	}
