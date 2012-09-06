/**********************************************************************
 * $Id: GdalBuffer.java 7765 2006-10-03 07:05:18Z nacho $
 *
 * Name:     GdalBuffer.java
 * Project:  JGDAL. Interface java to gdal (Frank Warmerdam).
 * Purpose:  Buffer to store image data. 
 * Author:   Nacho Brodin, brodin_ign@gva.es
 *
 **********************************************************************/
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

package es.gva.cit.jgdal;


/**  
 * Buffer para el almacenamiento de líneas de la imágén. Esta clase es instanciada desde C para que se pueda acceder a sus datos desde el cliente java.
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */

public class GdalBuffer{
	
    public byte[] buffByte;			//8 bits
    public short[] buffShort;		//16 bits
    public int[] buffInt;			//32 bits
    public float[] buffFloat;		//32 bits
    public double[] buffDouble;		//64 bits
    
    public byte[] buffAPalette;
    public byte[] buffRPalette;
    public byte[] buffGPalette;
    public byte[] buffBPalette;
    
    public void reservaByte(int r){
    	buffByte = new byte[r];
    }
    
    public void reservaShort(int r){
    	buffShort = new short[r];
    }
    
    public void reservaInt(int r){
    	buffInt = new int[r];
    }
    
    public void reservaFloat(int r){
    	buffFloat = new float[r];
    }
    
    public void reservaDouble(int r){
    	buffDouble = new double[r];
    }
    
    public void reservaPalette(int r){
    	buffAPalette = new byte[r];
    	buffRPalette = new byte[r];
        buffGPalette = new byte[r];
        buffBPalette = new byte[r];
    }
    
    public int getSize(){
      for(int i=0;i<5;i++){
      	if(buffByte!=null)return buffByte.length;
    	if(buffShort!=null)return buffShort.length;
    	if(buffInt!=null)return buffInt.length;
    	if(buffFloat!=null)return buffFloat.length;
    	if(buffDouble!=null)return buffDouble.length;
    	if(buffRPalette!=null)return buffRPalette.length;
      }
      return 0;
    }
  
}