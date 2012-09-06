/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 IVER T.I. and Generalitat Valenciana.
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
package org.gvsig.raster.grid;

/**
 * 
 * @author Victor Olaya (volaya@ya.com)
 */
public class GridCell implements Comparable{

	private int m_iX, m_iY;
	private double m_dValue;
	
	public GridCell(int iX, int iY, double dValue){
		
		m_iX = iX;
		m_iY = iY;
		m_dValue = dValue;
		
	}
	
	public double getValue() {
		
		return m_dValue;
		
	}
	public void setValue(double dValue) {
		
		m_dValue = dValue;
		
	}
	
	public int getX() {
		
		return m_iX;
		
	}
	
	public void setX(int iX) {
		
		m_iX = iX;
		
	}
	
	public int getY() {
		
		return m_iY;
		
	}
	
	public void setY(int iY) {
		
		m_iY = iY;
		
	}
	
	 public int compareTo(Object cell) throws ClassCastException {
		 
		 if (!(cell instanceof GridCell)){
			 throw new ClassCastException();
		 }
		 
		 double dValue = ((GridCell) cell).getValue();
		 double dDif = this.m_dValue - dValue;   
		 
		 if (dDif > 0.0){
			 return 1;
		 }
		 else if (dDif < 0.0){
			 return -1;
		 }
		 else{
			 return 0;
		 }
	 
	 }
	 	 
	 public boolean equals(Object obj){
		 
		 if (obj instanceof GridCell){
			 GridCell cell = (GridCell) obj; 
			 return (m_iX == cell.getX() &&
					 m_iY == cell.getY() &&
					 m_dValue == cell.getValue()); 
		 }
		 else{
			 return false;
		 }
		 
	 }
	

}
