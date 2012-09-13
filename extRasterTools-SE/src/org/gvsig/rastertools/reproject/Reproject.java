/* gvSIG. Sistema de Informaciï¿½n Geogrï¿½fica de la Generalitat Valenciana
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
 */
package org.gvsig.rastertools.reproject;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDriverException;

import es.gva.cit.jgdal.GdalException;
import es.gva.cit.jgdal.GdalWarp;
/**
 * Clase encargada de la reproyección. Se le asigna una capa raster y la ruta de
 * destino
 * 
 * @version 30/04/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class Reproject {
	private FLyrRasterSE lyr      = null;
	private String       pathDest = null;
	private GdalWarp     warper   = null;

	/**
	 * Constructor de la clase.
	 * @param lyr
	 * @param pathDest Ruta de destino
	 */
	public Reproject(FLyrRasterSE lyr, String pathDest) {
		this.lyr = lyr;
		this.pathDest = pathDest;
	}
	
	/**
	 * Método para la transformación del raster.
	 * @param proj Proyección destino
	 */
	public void warp(IProjection proj, IProjection sourceSrs) throws ReprojectException {
		if (lyr == null)
			throw new ReprojectException("Capa no valida.");
		if (!lyr.isReproyectable())
			throw new ReprojectException("Esta capa no se puede reproyectar.");
		if (proj == null || proj.getAbrev() == null)
			throw new ReprojectException("Proyección de destino no valida.");

		String epsg = proj.getAbrev();
		String s_srs = null;
		if (sourceSrs != null) {
			s_srs = sourceSrs.getAbrev();
		}
		if (sourceSrs == null) {
			if (lyr.getProjection() != null) {
				s_srs = lyr.getProjection().getAbrev();
			} else {
				throw new ReprojectException("No se encuentra la proyeccion de origen.");
			}
		}
		String source = null;
		if (lyr.getFile() != null)
			source = lyr.getFile().getPath();

		if (source == null)
			throw new ReprojectException("Fichero fuente no valido.");
		
		if (source.endsWith(".hdr"))
			source = source.substring(0, source.lastIndexOf(".hdr"));

		File file = new File(pathDest);
		if (!file.getParentFile().canWrite())
			throw new ReprojectException("Ruta de destino no valida.");

		warper = new GdalWarp();

		try {
			warper.setSsrs(s_srs);
			
//			GTiff: GeoTIFF
//			GTiff_FORMAT = 1;     // Reproyecta .tif
//			VRT: Virtual Raster
//			VRT_FORMAT = 2;       // No reproyecta
//			NITF: National Imagery Transmission Format
//			NITF_FORMAT = 3;      // Reproyecta .nitf
//			HFA: Erdas Imagine Images (.img)
//			HFA_FORMAT = 4;       // Reproyecta .img
//			ELAS: ELAS
//			ELAS_FORMAT = 5;      // No reproyecta
//			MEM: In Memory Raster
//			MEM_FORMAT = 6;       // No usarlo, aunque reproyecta
//			BMP: MS Windows Device Independent Bitmap
//			BMP_FORMAT = 7;       // No reproyecta
//			PCIDSK: PCIDSK Database File
//			PCIDSK_FORMAT = 8;    // Reproyecta
//			ILWIS: ILWIS Raster Map
//			ILWIS_FORMAT = 9;     // No reproyecta

//			HDF4_FORMAT = 10;     // No reproyecta y se cierra gvSIG

//			PNM: Portable Pixmap Format (netpbm)
//			PNM_FORMAT = 11;      // No reproyecta
//			ENVI: ENVI .hdr Labelled
//			ENVI_FORMAT = 12;     // Reproyecta
//			EHdr: ESRI .hdr Labelled
//			EHDR_FORMAT = 13;     // Reproyecta
//			PAux: PCI .aux Labelled
//			PAUX_FORMAT = 14;     // Reproyecta
//			MFF: Vexcel MFF Raster
//			MFF_FORMAT = 15;      // No reproyecta
//			MFF2: Vexcel MFF2 (HKV) Raster
//			MFF2_FORMAT = 16;     // No reproyecta
//			BT: VTP .bt (Binary Terrain) 1.3 Format
//			BT_FORMAT = 17;       // No reproyecta
//			IDA: Image Data and Analysis
//			IDA_FORMAT = 18;      // No reproyecta
//			RMF: Raster Matrix Format
//			RMF_FORMAT = 19;      // No reproyecta
//			RST: Idrisi Raster A.1
//			RST_FORMAT = 20;      // No reproyecta

//			Leveller: Leveller heightfield
//			LEVELLER_FORMAT = 21; // No reproyecta
//			Terragen: Terragen heightfield
//			TERRAGEN_FORMAT = 22; // No reproyecta
//			ERS: ERMapper .ers Labelled
//			ERS_FORMAT = 23;      // No reproyecta
//			INGR: Intergraph Raster
//			INGR_FORMAT = 24;     // No reproyecta
//			GSAG: Golden Software ASCII Grid (.grd)
//			GSAG_FORMAT = 25;     // No reproyecta
//			GSBG: Golden Software Binary Grid (.grd)
//			GSBG_FORMAT = 26;     // No reproyecta
//			ADRG: ARC Digitized Raster Graphics
//			ADRG_FORMAT = 27;     // No reproyecta
			
//			warper.setFormat(27);
			warper.warp(epsg, source, pathDest, GeoRasterWriter.getWriter(pathDest).getDriverName());
		} catch (GdalException e) {
			throw new ReprojectException("Error al reproyectar.");
		} catch (NotSupportedExtensionException e) {
			throw new ReprojectException("Error al reproyectar.");
		} catch (RasterDriverException e) {
			throw new ReprojectException("Error al reproyectar.");
		}
	}

	/**
	 * Obtiene el porcentaje de progreso de la tarea de reproyección para
	 * mostrarlo por pantalla.
	 * @return
	 */
	public int getPercent() {
		if (warper != null)
			return warper.getPercent();
		return 0;
	}
}