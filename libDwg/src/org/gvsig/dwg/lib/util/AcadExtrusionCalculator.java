/* jdwglib. Java Library for reading Dwg files.
 * 
 * Author: Jose Morell Rama (jose.morell@gmail.com).
 * Port from the Pythoncad Dwg library by Art Haas.
 *
 * Copyright (C) 2005 Jose Morell, IVER TI S.A. and Generalitat Valenciana
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
 * Jose Morell (jose.morell@gmail.com)
 * 
 * or
 *
 * IVER TI S.A.
 *  C/Salamanca, 50
 *  46005 Valencia
 *  Spain
 *  +34 963163400
 *  dac@iver.es
 */
package org.gvsig.dwg.lib.util;

import java.lang.Math;

/**
 * This class allows to apply the extrusion transformation of Autocad given by an array
 * of doubles to a point given by an array of doubles too. 
 * 
 * @author azabala
 */
/*
 * The maths behind all this stuff, and the reasons to do this could be complex.
 * 
 * Some links:
 * http://www.autodesk.com/techpubs/autocad/acadr14/dxf/object_coordinate_systems_40ocs41_al_u05_c.htm
 * http://www.autodesk.com/techpubs/autocad/acadr14/dxf/arbitrary_axis_algorithm_al_u05_c.htm 
 * http://personales.unican.es/togoresr/Sco-en.html
 * 
 * In ACAD, 3D entities has their coordinates in World Coordinate System (WCS: utm, etc.)
 * but 2D entities has their coordinates in an Object Coordinate System (OCS).
 * 
 * To work with the coordinates saved in the file, we must to transform them (extrude) from OCS
 * to WCS
 * 

 * */
public class AcadExtrusionCalculator {
    
	public static Matrix4D computeTransform(double[] extrusion, double[] coord)
    {
        
        Vector3D Ax = null;
        if(extrusion[0] == 0d && extrusion[1] == 0d && extrusion[2] > 0d && coord[2] == 0d)
        	return null;
        if((extrusion[0] >= 0f ? extrusion[0]:-extrusion[0]) < 0.015625F
        		&&
            (extrusion[1] >= 0f ? extrusion[1]:-extrusion[1]) < 0.015625F){
         
        	Ax = new Vector3D(extrusion[2], 0f, -extrusion[0]);
         }else{
        	 Ax = new Vector3D(-extrusion[1],extrusion[0], 0f);
         }
        double len = Ax.length();
        Ax.scale(1d / len);
        Vector3D upward = new Vector3D(extrusion[0], extrusion[1], extrusion[2]);
        Vector3D Ay = upward.cross(Ax);
  			
        Matrix4D aaa = new Matrix4D(Ax.x, Ay.x, upward.x, coord[2] * upward.x, 
  										Ax.y, Ay.y, upward.y, coord[2] * upward.y,
  										Ax.z, Ay.z, upward.z, coord[2] * upward.z, 
  											0.0F, 0.0F, 0.0F, 1.0F);
  	
        //Creo que la transformacion comentada es para pasar de WCS a OCS, y no al reves
        
        /*
        Matrix4D aaa = new Matrix4D(Ax.x, Ax.y, Ax.z, 0f, 
									Ay.x, Ay.y, Ay.z, 0f,
									upward.x, upward.y, upward.z, 0f,
									coord[2] * upward.x, coord[2] * upward.y, coord[2] * upward.z, 1f);
        
        */
        return aaa;
    }
	
	public static double[] extrude2(double[] coord_in, double[] xtru){
		
		//antes de cambiar el plano, aplicamos una extrusion con valor
		//la altura del punto
//		coord_in[0] += (xtru[0] * coord_in[2]);
//		coord_in[1] += (xtru[1] * coord_in[2]);
//		coord_in[2] += (xtru[2] * coord_in[2]);
		
		
		
		Matrix4D transform = computeTransform(xtru, coord_in);
		if(transform == null)//xtru es el eje Z
			return coord_in;
		Point3D p = new Point3D(coord_in[0], coord_in[1], coord_in[2]);
		transform.transform(p);
		return new double[]{p.x, p.y, p.z};
		
	}
	
	
	
	/*
    
   llamamos a finalConv, y este llama a extrude internamente
   
    static DrawAble finalConv(DrawAble dl, DxfEntity entity, boolean doTransform)
    {
        if(dl == null)
            return null;
        DrawAble dr;
        if(doTransform)
        {
            if(entity.getExtrusion() != 0.0F)
                dr = dl.extrude(entity.getExtrusion());
            else
                dr = dl;
            Matrix4D mat = entity.calcArbitMat();
            if(mat != null)
                dr.transformBy(mat);
        } else
        if(entity.getExtrusion() != 0.0F)
            dr = dl.extrude(entity.getExtrusion(), entity.getUpwardVector());
        else
            dr = dl;
        return dr;
    }
    
     public DrawAble extrude(float dist, Vector3D up)
    {
        if(dist == 0.0F)
            return this;
        Vector3D ex = new Vector3D(dist * up.x, dist * up.y, dist * up.z);
        DrawSet set = new DrawSet(2 + nrPoints);
        DrawLines second = new DrawLines(nrPoints);
        set.setLayer(super.layer);
        set.setColor(super.color);
        second.setLayer(super.layer);
        second.setColor(super.color);
        set.addDrawable(this);
        for(int i = 0; i < nrPoints; i++)
        {
            second.addPoint(line[i].x + ex.x, line[i].y + ex.y, line[i].z + ex.z);
            DrawLines conn = new DrawLines(2);
            conn.setLayer(super.layer);
            conn.setColor(super.color);
            conn.addPoint(line[i]);
            conn.addPoint(second.line[i]);
            set.addDrawable(conn);
        }

        if(isClosed)
            second.close();
        set.addDrawable(second);
        return set;
    }

    
    
*/
}
