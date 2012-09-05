/*
 * Created on 19-oct-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
package com.iver.utiles;

/**
 * @author FJP
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;
/*
 * ImageFilter.java
 * 
 */
public class ImageFilter extends FileFilter
{
	//Accept all directories and all gif, jpg, tiff, or png files.
	public boolean accept(File f)
	{
		if (f.isDirectory()) { 
			return true; }
		String extension = Utils.getExtension(f);
		if (extension != null) 
		{
			if (extension.equals(Utils.tiff) || extension.equals(Utils.tif) || 
					extension.equals(Utils.gif) || extension.equals(Utils.jpeg) || 
					extension.equals(Utils.jpg) || extension.equals(Utils.png))
			{
				return true;
			} 
			else
			{
				return false;
			}
		}
		return false; } //The description of this filter

	public String getDescription() { return "Just Images"; }
	} 