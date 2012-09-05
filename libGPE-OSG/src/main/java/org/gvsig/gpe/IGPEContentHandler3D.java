/* gvSIG. Geographic Information System of the Valencian Government
*  osgVP. OSG Virtual Planets.
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
* 2008 Instituto de Automática e Informática Industrial, UPV.
*/


package org.gvsig.gpe;

import java.awt.image.BufferedImage;

import org.gvsig.gpe.parser.IGPEContentHandler;

public interface IGPEContentHandler3D extends IGPEContentHandler {

	public Object startSolid(String id, String srs);

	public void endSolid(Object solid);
	
	public Object startMultiSolid(String id, String srs);
	
	public void endMultiSolid(Object solid);

	public void addSolidToMultiSolid(Object solid, Object feature);
	
	public void startSolidVertexArray(Object solid);
	
	public void endSolidVertexArray();
	
	public void startSolidNormalArray(Object solid);

	public void endSolidNormalArray();
	
	public void startSolidColorArray(Object solid);

	public void endSolidColorArray();
	
	public void startSolidTexCoordArray(Object solid, int nTexCoords, int stage);

	public void endSolidTexCoordArray();

	public void addVertexToSolid(Object solid, double x, double y, double z);

	public void addNormalToSolid(Object solid, double x, double y, double z);
	
	public void addTextureToSolid(Object geometry, int stage, BufferedImage image);
	
	public void setNormalBindingToSolid(Object solid, int mode );
	
	public void setColorBindingToSolid(Object solid, int mode );

	public void addColorToSolid(Object solid, float r, float g, float b, float a);

	public void addTextureCoordinateToSolid(Object solid, double x, double y, int stage);
	
	public Object startPrimitiveSet(int mode, int type);
	
	public void endPrimitiveSet(Object primitiveSet);
	
	public void addPrimitiveSetToSolid(Object solid, Object primitiveSet);
	
	public void startPrimitiveSetIndexArray(Object primitiveSet, int nIndices);

	public void endPrimitiveSetIndexArray();
	
	public void addIndexToPrimitiveSet(Object primitiveSet, int i);
	
	public Object startMaterial();
	
	public void addMaterialToSolid(Object solid, Object material);
	
	public void endMaterial(Object material);
	
	public void addAmbientToMaterial(Object material, float r, float g, float b, float a);
	
	public void addDiffuseToMaterial(Object material, float r, float g, float b, float a);
	
	public void addSpecularToMaterial(Object material, float r, float g, float b, float a);
	
	public void addEmissionToMaterial(Object material, float r, float g, float b, float a);
	
	public void addShininessToMaterial(Object material, float s);
	
	public void addBlendingToSolid(Object solid, boolean blending);

}
