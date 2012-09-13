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
 */
package org.gvsig.rastertools.reproject;

import java.io.File;

import org.cresques.cts.IProjection;
import org.gvsig.fmap.crs.CRSFactory;
import org.gvsig.rastertools.BaseTest;
/**
 * Test para comprobar que funciona correctamente la reproyeccion
 * 
 * @version 20/05/2008
 * @author BorSanZa - Borja Sánchez Zamorano (borja.sanchez@iver.es)
 */
public class ReprojectProcessTest extends BaseTest {
	public static void main(String[] args) {
		System.err.println("***************************************");
		System.err.println("*** ReprojectProcessTest running... ***");
		System.err.println("***************************************");
		ReprojectProcessTest t = new ReprojectProcessTest();
		try {
			t.test();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Proceso de recorte de la imagen en secuencial.
	 * @param drawBands
	 * @param onePerBand
	 * @param interp
	 */
	protected ReprojectProcess reprojectingProcess(String path, IProjection projection, IProjection srcprojection) {
		ReprojectProcess reprojectProcess = new ReprojectProcess();
		reprojectProcess.addParam("layer", lyr);
		reprojectProcess.addParam("path", path);
		reprojectProcess.addParam("projection", projection);
		reprojectProcess.addParam("srcprojection", srcprojection);
		return reprojectProcess;
	}
	
	private IProjection getProjection(String code) {
		try {
			return CRSFactory.getCRS(code);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	public void test() throws InterruptedException {
		openLayer(new File(baseDir + "p198r033_7t20010601_z31_nn10_puerto.tif"));
		ReprojectProcess rp1 = reprojectingProcess(getFileTemp() + ".tif", getProjection("23030"), getProjection("32631"));
		rp1.execute();
		System.out.println(out);
		
		openLayer(new File(out + ".tif"));
		System.out.println(out + ".tif");
//		System.out.println("Proyeccion destino: " + lyr.getProjection().getAbrev());

		System.err.println("***************************************");
		System.err.println("*** ReprojectProcessTest ending...  ***");
		System.err.println("***************************************");
	}
}