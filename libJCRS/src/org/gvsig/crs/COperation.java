/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Instituto de Desarrollo Regional and Generalitat Valenciana.
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
 *   Instituto de Desarrollo Regional (Universidad de Castilla La-Mancha)
 *   Campus Universitario s/n
 *   02071 Alabacete
 *   Spain
 *
 *   +34 967 599 200
 */

package org.gvsig.crs;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.crs.proj.CrsProj;
import org.gvsig.crs.proj.JNIBaseCrs;
import org.gvsig.crs.proj.OperationCrsException;

import com.iver.andami.messages.NotificationManager;

/**
 * Clase que implementa las operaciones de tranformacion de 
 * coordenadas entre dos CRSs.
 * 
 * @author Miguel García Jiménez (garciajimenez.miguel@gmail.com)
 * @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */

//public class COperation implements ICOperation {
public class COperation implements ICoordTrans {
	/**
	 * CRS Fuente
	 */
	private ICrs sourceCrs;
	/**
	 * CRS Destino
	 */
	private ICrs targetCrs;
	/**
	 * CRS con los parámetros de transformación (puede representar el
	 * CRS fuente o destino, dependiendo de <code>paramsInTarget</code>).
	 */
	private CrsProj paramsCrsProj = null;
	/**
	 * Indica si los parámetros de transformación van asociados al CRS
	 * fuente (<code>false</code>) o al destino (<code>true</code>)
	 */
	private boolean paramsInTarget;
	
	/**
	 * parámetros de transformación para el CRS fuente en el formato proj4
	 */
	private String sourceParams = null;
	
	/**
	 * parámetros de transformación para el CRS destino en el formato proj4
	 */
	private String targetParams = null;
	
	private CrsProj source = null;
	
	private CrsProj target = null;
	
	
	/**
	 * Constructor.
	 * 
	 * @param from CRS fuente.
	 * @param to CRS destino.
	 * @throws CrsException
	 */
	public COperation(ICrs from, ICrs to) throws CrsException {
		sourceCrs =  from;
		targetCrs =  to;
		source = sourceCrs.getCrsProj();
		target = targetCrs.getCrsProj();
	}
	
	/**
	 * Construcctor.
	 * 
	 * @param sourceCrs CRS fuente.
	 * @param targetCrs CRS destino.
	 * @param sourceParams Parámetros de transformación para la fuente (en formato proj4).
	 * @param targetParams Parámetros de transformación para el destino (en formato proj4).
	 * @throws CrsException
	 */
	public COperation(ICrs sourceCrs, ICrs targetCrs, String sourceParams, String targetParams) throws CrsException {
		this.sourceCrs = sourceCrs;
		this.targetCrs = targetCrs;
		this.sourceParams = sourceParams;
		this.targetParams = targetParams;
		
		if(sourceParams != null)
			source = new CrsProj(sourceCrs.getProj4String()+sourceParams);
		else
			source = sourceCrs.getCrsProj();
		if(targetParams != null)
			target = new CrsProj(targetCrs.getProj4String()+targetParams);
		else
			target = targetCrs.getCrsProj();
	}

	/**
	 * Realiza la operación de transformación sobre un punto.
	 * Si exiten parámetros de transformación (<code>paramsCrsProj</code>) se utilizan
	 * en lugar del CRS fuente o destino, según indique <code>paramsInTarget</code>.
	 * @throws OperationCrsException 
	 * 
	 */
	private Point2D operate(Point2D pt) throws CrsException, OperationCrsException {
		double x[] = {pt.getX()};
		double y[] = {pt.getY()};
		double z[] = {0D};
		int errno = 0;
		
			errno = JNIBaseCrs.operate( x , y, z,source,target);
			if (errno != -38) // "failed to load NAD27-83 correction file" (pj_strerrno.c)
				return new Point2D.Double(x[0],y[0]);
			else{
				x[0] = pt.getX();
				y[0] = pt.getY();
				z[0] = 0D;
				JNIBaseCrs.operate( x , y, z,sourceCrs.getCrsProj(), targetCrs.getCrsProj());
				return new Point2D.Double(x[0],y[0]);
			}
			
			/*
			if (paramsCrsProj != null){
				if (paramsInTarget)
					errno = JNIBaseCrs.operate( x , y, z,sourceCrs.getCrsProj(), paramsCrsProj);
				else
					errno = JNIBaseCrs.operate( x , y, z,paramsCrsProj,targetCrs.getCrsProj());
				if (errno != -38) // "failed to load NAD27-83 correction file" (pj_strerrno.c)
					return new Point2D.Double(x[0],y[0]);
			}
			
			// Si el punto estaba fuera del ámbito del nadgrid operamos sin nadgrid (convertimos)
			x[0] = pt.getX();
			y[0] = pt.getY();
			z[0] = 0D;
			JNIBaseCrs.operate( x , y, z,sourceCrs.getCrsProj(), targetCrs.getCrsProj());
			return new Point2D.Double(x[0],y[0]);*/
	//	System.out.println("x="+x[0]+"y="+y[0]);
	//	if(!targetCrs.isProjected())
	//		return new Point2D.Double(x[0]*((180)/Math.PI),y[0]*((180)/Math.PI));
	//	else
	}

	/**
	 * Realiza la operación de transformación sobre un punto.
	 * Si exiten parámetros de transformación (<code>paramsCrsProj</code>) se utilizan
	 * en lugar del CRS fuente o destino, según indique <code>paramsInTarget</code>.
	 * @throws CrsException 
	 * 
	 */
	//TODO Eliminar este método. Me vale con el operate(Point2D).
	private double [] operate(double []ptOrig) throws OperationCrsException, CrsException {
		double x[] = {ptOrig[0]};
		double y[] = {ptOrig[1]};
		double z[] = {ptOrig[2]};
		int errno = 0;

			CrsProj source;
			CrsProj target;
			if(sourceParams != null)
				source = new CrsProj(sourceCrs.getProj4String()+sourceParams);
			else
				source = sourceCrs.getCrsProj();
			if(targetParams != null)
				target = new CrsProj(targetCrs.getProj4String()+targetParams);
			else
				target = targetCrs.getCrsProj();
			
			errno = JNIBaseCrs.operate( x , y, z,source,target);
			if (errno != -38){ // "failed to load NAD27-83 correction file" (pj_strerrno.c)
				double ptDest[] = {x[0], y[0], z[0]};
				return ptDest;
			}
			
			/*if (paramsCrsProj != null){
				if (paramsInTarget)
					errno = JNIBaseCrs.operate( x , y, z,sourceCrs.getCrsProj(), paramsCrsProj);
				else
					errno = JNIBaseCrs.operate( x , y, z,paramsCrsProj,targetCrs.getCrsProj());
				if (errno != -38){ // "failed to load NAD27-83 correction file" (pj_strerrno.c)
					double ptDest[] = {x[0], y[0], z[0]};
					return ptDest;
				}
			}*/
			
			// Si el punto estaba fuera del ámbito del nadgrid operamos sin nadgrid (convertimos)
			 x[0] = ptOrig[0];
			 y[0] = ptOrig[1];
			 z[0] = ptOrig[2];
			 JNIBaseCrs.operate(x, y, z,sourceCrs.getCrsProj(), targetCrs.getCrsProj());
			 double ptDest[] = {x[0], y[0], z[0]};
			 return ptDest;
	}

	public IProjection getPOrig() {
		return (IProjection)sourceCrs;
	}

	public IProjection getPDest() {
		return (IProjection)targetCrs;
	}

	public Point2D convert(Point2D ptOrig, Point2D ptDest){
		try {
			ptDest = operate(ptOrig);
		} catch (CrsException e) {
			// TODO LWS implementar una gestión completa de los errores.
			//NotificationManager.addError(e);
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}catch (OperationCrsException e) {
			// TODO LWS implementar una gestión completa de los errores.
			//NotificationManager.addError(e);
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		return ptDest;
	}

	public Rectangle2D convert(Rectangle2D rect) {
		Point2D pt1 = new Point2D.Double(rect.getMinX(), rect.getMinY());
		Point2D pt2 = new Point2D.Double(rect.getMaxX(), rect.getMaxY());
		try {
			pt1 = operate(pt1);
			pt2 = operate(pt2);
		} catch (CrsException e) {
			// TODO LWS implementar una gestión completa de los errores.
			//NotificationManager.addError(e);
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}catch (OperationCrsException e) {
			// TODO LWS implementar una gestión completa de los errores.
			//NotificationManager.addError(e);
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage());
		}
		rect = new Rectangle2D.Double();
		rect.setFrameFromDiagonal(pt1, pt2);
		return rect;
	}

	
	public ICoordTrans getInverted() {
		COperation operation;
		try {
			operation = new COperation(targetCrs, sourceCrs,targetParams,sourceParams);
			return operation;
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
/*	
	public void setParamsCrsProj(CrsProj paramsCrs, boolean inTarget){
		this.paramsInTarget = inTarget;
		this.paramsCrsProj = paramsCrs;
	}
	*/
}
