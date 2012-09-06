/* gvSIG. Sistema de InformaciÛn Geogr·fica de la Generalitat Valenciana
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
 *   Av. Blasco Ib·Òez, 50
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

import java.util.ArrayList;
import java.util.Iterator;

import javax.units.ConversionException;
import javax.units.Unit;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultProjectedCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PrimeMeridian;

import com.iver.andami.PluginServices;

/**
 * Clase para manejar la libreria proj4
 * 
 * @author	David Hern·ndez LÛpez (david.hernandez@uclm.es)
 * @author  JosÈ Luis GÛmez MartÌnez (JoseLuis.Gomez@uclm.es)
 *
 */

public class Proj4 {	

	private ArrayList projectionNameList= new ArrayList();
	private ArrayList unitNameList= new ArrayList();
	private ArrayList projectionParameterNameList= new ArrayList();
	private ArrayList projectionParameterList= new ArrayList();
	private ArrayList projectionParameterDefaultValueList= new ArrayList();
	private ArrayList projectionParameterMaxValueList= new ArrayList();
	private ArrayList projectionParameterMinValueList= new ArrayList();
	private ArrayList projectionParameterUnitList= new ArrayList();
	private ArrayList projectionAcronymList= new ArrayList();
	private ArrayList projectionParameterAcronymList= new ArrayList();
	
	int divider=10000;
	private static double angularTolerance=1.0/3600.0;
	
	/**
     * Small tolerance factor for rounding errors.
     */
    private static final double EPS = 1E-8;
	
	public Proj4() throws CrsException
	{
		//defineProjectionNames();
		//defineProjectionParameterNameList();
		//defineProjectionParameterUnitList();
		defineUnitNameList();
		defineProjectionParameterList();
		defineProjections();
	}
	
	private void defineUnitNameList() throws CrsException
	{
		int count=0;
		
		{
			String[] unitName={"Angular"};
			unitNameList.add(count,unitName);
		}
		
		count++;
		{
			String[] unitName={"Linear"};
			unitNameList.add(count,unitName);
		}
		
		count++;
		{
			String[] unitName={"Unitless"};
			unitNameList.add(count,unitName);
			addUnitName(count,"Adimensional");
		}
		
	}
	
	private void defineProjectionParameterList() throws CrsException
	{
		int count=0;

		{ // azimuth
			String[] parameterName={"azimuth"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Azimuth of initial line");		
			addProjectionParameterName(count,"AzimuthAngle");		

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // central_meridian
			String[] parameterName={"central_meridian"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Longitude of natural origin");		
			addProjectionParameterName(count,"NatOriginLong");		
			addProjectionParameterName(count,"Longitude of projection center");
			addProjectionParameterName(count,"Longitude_of_center");
			addProjectionParameterName(count,"ProjCenterLong");
			addProjectionParameterName(count,"Longitude of false origin");		
			addProjectionParameterName(count,"FalseOriginLong");		
			addProjectionParameterName(count,"StraightVertPoleLong");		
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}
		
		count++;
		{ // false_easting
			String[] parameterName={"false_easting"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Easting at projection centre");		
			addProjectionParameterName(count,"Easting of false origin");		
			addProjectionParameterName(count,"FalseEasting");		
			addProjectionParameterName(count,"False_Easting");		
			addProjectionParameterName(count,"FalseOriginEasting");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}
		
		count++;
		{ // false_northing
			String[] parameterName={"false_northing"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Northing at projection centre");		
			addProjectionParameterName(count,"Northing of false origin");		
			addProjectionParameterName(count,"FalseNorthing");		
			addProjectionParameterName(count,"False_Northing");		
			addProjectionParameterName(count,"FalseOriginNorthing");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // latitude_of_center
			String[] parameterName={"latitude_of_center"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"CenterLat");		
			addProjectionParameterName(count,"FalseOriginLat");
			addProjectionParameterName(count,"Latitude of false origin");		
			addProjectionParameterName(count,"Latitude_of_origin");		
			addProjectionParameterName(count,"Latitude of natural origin");
			addProjectionParameterName(count,"Latitude of projection center");
			addProjectionParameterName(count,"Latitude of projection centre");
			addProjectionParameterName(count,"NatOriginLat");		
			addProjectionParameterName(count,"ProjCenterLat");
			addProjectionParameterName(count,"Spherical_latitude_of_origin");
			addProjectionParameterName(count,"Central_Parallel");

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // Latitude_Of_1st_Point
			String[] parameterName={"Latitude_Of_1st_Point"};
			projectionParameterNameList.add(count,parameterName);
			//addProjectionParameterName(count,"CenterLat");		
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // Latitude_Of_2nd_Point
			String[] parameterName={"Latitude_Of_2nd_Point"};
			projectionParameterNameList.add(count,parameterName);
			//addProjectionParameterName(count,"CenterLat");		
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // latitude_of_origin
			String[] parameterName={"latitude_of_origin"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"CenterLat");		
			addProjectionParameterName(count,"FalseOriginLat");
			addProjectionParameterName(count,"Latitude of center");		
			addProjectionParameterName(count,"Latitude of false origin");		
			addProjectionParameterName(count,"Latitude of natural origin");
			addProjectionParameterName(count,"Latitude of projection center");
			addProjectionParameterName(count,"Latitude of projection centre");
			addProjectionParameterName(count,"NatOriginLat");		
			addProjectionParameterName(count,"ProjCenterLat");
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // latitude_of_origin
			String[] parameterName={"latitude_of_standard_parallel"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"CenterLat");		
			addProjectionParameterName(count,"FalseOriginLat");
			addProjectionParameterName(count,"Latitude of center");		
			addProjectionParameterName(count,"Latitude of false origin");		
			addProjectionParameterName(count,"Latitude of natural origin");
			addProjectionParameterName(count,"Latitude of projection center");
			addProjectionParameterName(count,"Latitude of projection centre");
			addProjectionParameterName(count,"Latitude_of_standard_parallel");		
			addProjectionParameterName(count,"NatOriginLat");		
			addProjectionParameterName(count,"ProjCenterLat");
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // longitude_of_center
			String[] parameterName={"longitude_of_center"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Longitude of origin");		
			addProjectionParameterName(count,"Longitude of false origin");		
			addProjectionParameterName(count,"NatOriginLong");		
			addProjectionParameterName(count,"central_meridian");		
			addProjectionParameterName(count,"CenterLong");		
			addProjectionParameterName(count,"Spherical_latitude_of_origin");

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // Longitude_Of_1st_Point
			String[] parameterName={"Longitude_Of_1st_Point"};
			projectionParameterNameList.add(count,parameterName);
			//addProjectionParameterName(count,"CenterLat");		
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // Longitude_Of_2nd_Point
			String[] parameterName={"Longitude_Of_2nd_Point"};
			projectionParameterNameList.add(count,parameterName);
			//addProjectionParameterName(count,"CenterLat");		
			
			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // pseudo_standard_parallel_1
			String[] parameterName={"pseudo_standard_parallel_1"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Latitude of Pseudo Standard Parallel");		

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}
		
		count++;
		{ // satellite_height
			String[] parameterName={"rectified_grid_angle"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Angle from Rectified to Skew Grid");		
			addProjectionParameterName(count,"XY_Plane_Rotation");		
			addProjectionParameterName(count,"RectifiedGridAngle");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}
		
		count++;
		{ // satellite_height
			String[] parameterName={"satellite_height"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Satellite Height");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}
		
		count++;
		{ // scale_factor
			String[] parameterName={"scale_factor"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Scale factor at natural origin");		
			addProjectionParameterName(count,"ScaleAtNatOrigin");		
			addProjectionParameterName(count,"ScaleAtCenter");		

			String[] parameterUnit={"Unitless"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // standard_parallel_1
			String[] parameterName={"standard_parallel_1"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Latitude of first standard parallel");		
			addProjectionParameterName(count,"Latitude of origin");		
			addProjectionParameterName(count,"StdParallel1");		

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // standard_parallel_2
			String[] parameterName={"standard_parallel_2"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"Latitude of second standard parallel");		
			addProjectionParameterName(count,"StdParallel2");		

			String[] parameterUnit={"Angular"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // semi_major
			String[] parameterName={"semi_major"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"semi_major_axis");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // semi_minor
			String[] parameterName={"semi_minor"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"semi_minor_axis");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

		count++;
		{ // height
			String[] parameterName={"Height"};
			projectionParameterNameList.add(count,parameterName);
			addProjectionParameterName(count,"altitude");		

			String[] parameterUnit={"Linear"};
			projectionParameterUnitList.add(count,parameterUnit);
		}

	}
	
	private void defineProjections() throws CrsException
	{
		int count=0;
		
		{// Aitoff
			String[] projectionName={"Aitoff"};
			projectionNameList.add(count,projectionName);

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"aitoff"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Albers Equal-Area Conic
			String[] projectionName={"Albers_Conic_Equal_Area"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Albers Equal-Area Conic");
			addProjectionName(count,"Albers Equal Area");
			addProjectionName(count,"9822");
			
			String[] parameterName={"standard_parallel_1"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_2");
			addProjectionParameter(count,"latitude_of_center");
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_1"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"lat_0");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"aea"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Azimuthal_Equidistantt
			String[] projectionName={"Azimuthal_Equidistant"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Azimuthal Equidistant");
			addProjectionName(count,"Azimuthal-Equidistant");
			addProjectionName(count,"Postel");
			addProjectionName(count,"Zenithal Equidistant");
			addProjectionName(count,"Zenithal-Equidistant");
			addProjectionName(count,"Zenithal_Equidistant");
			
			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"aeqd"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Bonne
			String[] projectionName={"Bonne"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Bonne");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_1");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"bonne"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Cassini_Soldner
			String[] projectionName={"Cassini_Soldner"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Cassini-Soldner");
			addProjectionName(count,"Cassini");
			addProjectionName(count,"9806");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"cass"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Craster Parabolic
			String[] projectionName={"Craster_Parabolic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Craster-Parabolic");
			addProjectionName(count,"Craster Parabolic");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"craster"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Cylindrical_Equal_Area
			String[] projectionName={"Cylindrical_Equal_Area"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Cylindrical Equal Area");
			addProjectionName(count,"Normal Authalic Cylindrical (FME)");
			addProjectionName(count,"Lambert Cylindrical Equal Area");
			addProjectionName(count,"Lambert_Cylindrical_Equal_Area");
			addProjectionName(count,"Behrmann (standard parallel = 30)");
			addProjectionName(count,"Behrmann");
			addProjectionName(count,"Gall Orthographic (standard parallel = 45)");
			addProjectionName(count,"Gall Orthographic");
			addProjectionName(count,"Gall_Orthographic");
			addProjectionName(count,"Peters (approximated by Gall Orthographic)");
			addProjectionName(count,"Peters");
			addProjectionName(count,"Lambert Cylindrical Equal Area (Spherical)");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"cea"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_I
			String[] projectionName={"Eckert_I"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert I");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck1"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_II
			String[] projectionName={"Eckert_II"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert II");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck2"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_III
			String[] projectionName={"Eckert_III"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert III");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck3"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_IV
			String[] projectionName={"Eckert_IV"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert IV");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck4"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_V
			String[] projectionName={"Eckert_V"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert V");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck5"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Eckert_VI
			String[] projectionName={"Eckert_VI"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Eckert VI");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eck6"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Equidistant_Conic
			String[] projectionName={"Equidistant_Conic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Equidistant Conic");
			
			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"standard_parallel_2");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"lat_1");
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eqdc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Equirectangular
			String[] projectionName={"Equirectangular"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Plate Caree");
			addProjectionName(count,"Plate Carree");
			addProjectionName(count,"Plate_Caree");
			addProjectionName(count,"Plate_Carree");
			addProjectionName(count,"Equidistant Cylindrical");
			addProjectionName(count,"Equidistant_Cylindrical");
			addProjectionName(count,"9823");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_ts"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"eqc"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// McBryde-Thomas Flat-Polar Quartic
			String[] projectionName={"McBryde_Thomas_Flat_Polar_Quartic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"McBryde-Thomas-Flat-Polar-Quartic");
			addProjectionName(count,"McBryde Thomas Flat Polar Quartic");
			addProjectionName(count,"Flat Polar Quartic");
			addProjectionName(count,"Flat-Polar-Quartic");
			addProjectionName(count,"Flat_Polar_Quartic");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"mbtfpq"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Gall_Stereographic
			String[] projectionName={"Gall_Stereographic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Gall Stereograpic");
			addProjectionName(count,"Gall");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"gall"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// GEOS
			String[] projectionName={"GEOS"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Geostationary Satellite View");
			addProjectionName(count,"Normalized Geostationary Projection");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"satellite_height");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"h");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"35785831.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"geos"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Gnomonic
			String[] projectionName={"Gnomonic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Gnomonic");
			addProjectionName(count,"Central");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"gnom"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Goode
			String[] projectionName={"Goode"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Homolosine");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"goode"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Hammer-Aitoff
			String[] projectionName={"Hammer_Aitoff"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Hammer Aitoff");
			addProjectionName(count,"Hammer-Aitoff");
			addProjectionName(count,"Hammer");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"hammer"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Krovak
			String[] projectionName={"Krovak"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Krovak Oblique Conic Conformal");
			addProjectionName(count,"9819");

			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			//addProjectionParameter(count,"azimuth");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			//addProjectionParameterAcronymList(count,"alpha");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			//addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			//addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			//addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"krovak"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		
		count++;
		{// Laborde Madagascar
			String[] projectionName={"Laborde_Madagascar"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Laborde Madagascar");
			addProjectionName(count,"Laborde");
			addProjectionName(count,"9813");

			String[] parameterName={};
			projectionParameterList.add(count,parameterName);

			String[] parameterAcronym={};
			projectionParameterAcronymList.add(count,parameterAcronym);

			String[] parameterDefaultValue={};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);

			String[] parameterMaxValue={};
			projectionParameterMaxValueList.add(count,parameterMaxValue);

			String[] parameterMinValue={};
			projectionParameterMinValueList.add(count,parameterMinValue);

			String[] projectionAcronym={"labrd"};
			projectionAcronymList.add(count,projectionAcronym);
			
			/*String[] parameterName={"azimuth"};
			projectionParameterList.add(count,parameterName);

			String[] parameterAcronym={"azi"};
			projectionParameterAcronymList.add(count,parameterAcronym);

			String[] parameterDefaultValue={"18.9"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);

			String[] parameterMaxValue={"19"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);

			String[] parameterMinValue={"18"};
			projectionParameterMinValueList.add(count,parameterMinValue);

			String[] projectionAcronym={"labrd"};
			projectionAcronymList.add(count,projectionAcronym);*/
		}

		count++;
		{// Lambert_Azimuthal_Equal_Area
			String[] projectionName={"Lambert_Azimuthal_Equal_Area"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Azimuthal Equal Area");
			addProjectionName(count,"Lambert Azimuthal Equal Area (Spherical)");
			addProjectionName(count,"Lambert_Azimuthal_Equal_Area_(Spherical)");
			addProjectionName(count,"Lorgna");
			addProjectionName(count,"Zenithal Equal Area");
			addProjectionName(count,"Zenithal Equal-Area");
			addProjectionName(count,"Zenithal_Equal-Area");
			addProjectionName(count,"Zenithal-Equal-Area");
			addProjectionName(count,"Zenithal Eqivalent");
			addProjectionName(count,"Zenithal-Eqivalent");
			addProjectionName(count,"Zenithal_Eqivalent");
			addProjectionName(count,"9820");
			addProjectionName(count,"9821");

			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"laea"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		count++;
		{// Lambert_Conformal_Conic_1SP
			String[] projectionName={"Lambert_Conic_Near_Conformal"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Conic Near-Conformal");
			addProjectionName(count,"Lambert Conic Near Conformal");
			addProjectionName(count,"Lambert_Conic_Near-Conformal");
			addProjectionName(count,"9817");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"lcca"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Lambert_Conformal_Conic_1SP
			String[] projectionName={"Lambert_Conformal_Conic_1SP"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Conic Conformal (1SP)");
			addProjectionName(count,"9801");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"lcc"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Lambert_Conformal_Conic_2SP
			String[] projectionName={"Lambert_Conformal_Conic_2SP"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Conic Conformal (2SP)");
			addProjectionName(count,"9802");

			String[] parameterName={"standard_parallel_1"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_2");
			addProjectionParameter(count,"latitude_of_origin");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_1"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"lat_0");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"lcc"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Lambert_Conformal_Conic
			String[] projectionName={"Lambert_Conformal_Conic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Conic Conformal");
			addProjectionName(count,"9801");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"standard_parallel_2");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_1");
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"lcc"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Lambert_Conformal_Conic_2SP_Belgium
			String[] projectionName={"Lambert_Conformal_Conic_2SP_Belgium"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Lambert Conic Conformal (2SP Belgium)");
			addProjectionName(count,"9803");

			String[] parameterName={"standard_parallel_1"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_2");
			addProjectionParameter(count,"latitude_of_origin");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_1"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"lat_0");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"lcc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Loximuthal
			String[] projectionName={"Loximuthal"};
			projectionNameList.add(count,projectionName);
			//addProjectionName(count,"Winkel I");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"Central_Parallel");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_1");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"loxim"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Mercator_1SP
			String[] projectionName={"Mercator_1SP"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Mercator");
			addProjectionName(count,"Wright");
			addProjectionName(count,"9804");
			addProjectionName(count, "Mercator (1SP)");
			addProjectionName(count, "Mercator_(1SP)");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"latitude_of_origin");  // o latitude_origin
			addProjectionParameter(count,"scale_factor");  // o latitude_origin
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts"); // o lat_ts
			addProjectionParameterAcronymList(count,"k"); // o lat_ts
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0"); // o 0.0
			addProjectionParameterDefaultValue(count,"1.0"); // o 0.0
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"merc"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Mercator_2SP
			String[] projectionName={"Mercator_2SP"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Mercator");
			addProjectionName(count,"9805");
			addProjectionName(count, "Mercator (2SP)");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0"); // o 0.0
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"merc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Miller_Cylindrical
			String[] projectionName={"Miller_Cylindrical"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Miller Cylindrical");

			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"mill"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Mollweide
			String[] projectionName={"Mollweide"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Homolographic");
			addProjectionName(count,"Homalographic");
			addProjectionName(count,"Babinet");
			addProjectionName(count,"Elliptical");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"moll"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		
		count++;
		{// Near-Sided Perspective
			String[] projectionName={"Near_Sided_Perspective"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Near Sided Perspective");
			addProjectionName(count,"Near-Sided Perspective");
			addProjectionName(count,"Near-Sided-Perspective");
			addProjectionName(count,"Near-Sided_Perspective");
			addProjectionName(count,"Vertical Near Side Perspective");
			addProjectionName(count,"Vertical-Near-Side-Perspective");
			addProjectionName(count,"Vertical_Near_Side_Perspective");
			
			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"Height");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"h");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.001");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.001");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"nsper"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// New_Zealand_Map_Grid
			String[] projectionName={"New_Zealand_Map_Grid"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"New Zealand Map Grid");
			addProjectionName(count,"9811");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"nzmg"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Oblique_Mercator
			String[] projectionName={"Oblique_Mercator"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Oblique Mercator");
			addProjectionName(count,"9815");
			addProjectionName(count,"CT_ObliqueMercator");
			addProjectionName(count,"Hotine_Oblique_Mercator_Azimuth_Center");
			addProjectionName(count,"Rectified_Skew_Orthomorphic_Center");
			addProjectionName(count,"Hotine Oblique Mercator");
			addProjectionName(count,"Hotine_Oblique_Mercator");

			String[] parameterName={"latitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"longitude_of_center");
			addProjectionParameter(count,"azimuth");
			addProjectionParameter(count,"rectified_grid_angle");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lonc");
			addProjectionParameterAcronymList(count,"alpha");
			addProjectionParameterAcronymList(count,"gamma");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"omerc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Oblique_Mercator
			String[] projectionName={"Hotine_Oblique_Mercator_Two_Point_Center"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Hotine_Oblique_Mercator_Two_Point_Natural_Origin");

            /*SEMI_MAJOR,          SEMI_MINOR,
            LAT_OF_1ST_POINT,    LONG_OF_1ST_POINT,
            LAT_OF_2ND_POINT,    LONG_OF_2ND_POINT,
            	LAT_OF_CENTRE,       SCALE_FACTOR_LOCAL,
            FALSE_EASTING_LOCAL,       FALSE_NORTHING_LOCAL*/

            String[] parameterName={"Latitude_Of_1st_Point"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"Longitude_Of_1st_Point");
			addProjectionParameter(count,"Latitude_Of_2nd_Point");
			addProjectionParameter(count,"Longitude_Of_2nd_Point");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_1"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_1");
			addProjectionParameterAcronymList(count,"lat_2");
			addProjectionParameterAcronymList(count,"lon_2");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"omerc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Oblique_Stereographic
			String[] projectionName={"Oblique_Stereographic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Oblique Stereographic");
			addProjectionName(count,"9809");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"sterea"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Orthographic
			String[] projectionName={"Orthographic"};
			projectionNameList.add(count,projectionName);

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"ortho"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Polar_Stereographic
			String[] projectionName={"Polar_Stereographic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Polar Stereographic");
			addProjectionName(count,"Polar_Stereographic_(variant_A)");
			addProjectionName(count,"Polar_Stereographic_(variant_B)");
			addProjectionName(count,"9810");
			addProjectionName(count,"9829");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"latitude_of_standard_parallel");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			//lat_0=90 o lat_0=-90
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"90.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"90.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"stere"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Polyconic
			String[] projectionName={"Polyconic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"American_Polyconic");
			addProjectionName(count,"American Polyconic");
			addProjectionName(count,"9818");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"poly"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Quartic Authalic
			String[] projectionName={"Quartic_Authalic"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Quartic Authalic");
			addProjectionName(count,"Quartic-Authalic");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"qua_aut"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Robinson
			String[] projectionName={"Robinson"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Orthophanic");

			String[] parameterName={"longitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"robin"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		// 	Rosenmund Oblique Mercator - No en EPSG

		count++;
		{// Sinusoidal
			String[] projectionName={"Sinusoidal"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Sanson-Flamsteed");
			addProjectionName(count,"Sanson Flamsteed");
			addProjectionName(count,"Sanson_Flamsteed");
			addProjectionName(count,"Mercator equal area");
			addProjectionName(count,"Mercator_equal_area");

			String[] parameterName={"longitude_of_center"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"sinu"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Swiss_Oblique_Cylindrical
			String[] projectionName={"Swiss_Oblique_Cylindrical"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Swiss Oblique Cylindrical");
			addProjectionName(count,"Swiss Oblique Mercator");
			addProjectionName(count,"9814");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"somerc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Stereographic
			String[] projectionName={"Stereographic"};
			projectionNameList.add(count,projectionName);

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"latitude_of_standard_parallel");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"stere"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Stereographic North Pole
			String[] projectionName={"Stereographic_North_Pole"};
			projectionNameList.add(count,projectionName);

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"90.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"stere"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Stereographic South Pole
			String[] projectionName={"Stereographic_South_Pole"};
			projectionNameList.add(count,projectionName);

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"-90.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"stere"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Transverse_Mercator
			String[] projectionName={"Transverse_Mercator"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Transverse Mercator");
			addProjectionName(count,"Gauss-Kruger");
			addProjectionName(count,"Gauss_Kruger");
			addProjectionName(count,"Gauss Conformal");
			addProjectionName(count,"Transverse Cylindrical Orthomorphic");
			addProjectionName(count,"9807");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"scale_factor");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"k");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"1.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"10.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"0.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"tmerc"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		/*
		count++;
		{// Tunisia_Mining_Grid
			String[] projectionName={"Tunisia_Mining_Gridr"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Tunisia Mining Grid");
			addProjectionName(count,"9816");

			String[] parameterName={"latitude_of_origin"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"central_meridian");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lat_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lon_0");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"90.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"360.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-90.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-360.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"cass"};
			projectionAcronymList.add(count,projectionAcronym);
		}
		*/

		count++;
		{// VanDerGrinten
			String[] projectionName={"VanDerGrinten"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"VanDerGrinten I");
			addProjectionName(count,"VanderGrinten");
			addProjectionName(count,"Van_der_Grinten_I");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"vandg"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Winkel_I
			String[] projectionName={"Winkel_I"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Winkel I");
			addProjectionName(count,"Winkel-I");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"wink1"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Winkel_II
			String[] projectionName={"Winkel_II"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Winkel II");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"wink2"};
			projectionAcronymList.add(count,projectionAcronym);
		}

		count++;
		{// Winkel Tripel
			String[] projectionName={"Winkel_Tripel"};
			projectionNameList.add(count,projectionName);
			addProjectionName(count,"Winkel-Tripel");
			addProjectionName(count,"Winkel Tripel");

			String[] parameterName={"central_meridian"};
			projectionParameterList.add(count,parameterName);
			addProjectionParameter(count,"standard_parallel_1");
			addProjectionParameter(count,"false_easting");
			addProjectionParameter(count,"false_northing");

			String[] parameterAcronym={"lon_0"};
			projectionParameterAcronymList.add(count,parameterAcronym);
			addProjectionParameterAcronymList(count,"lat_ts");
			addProjectionParameterAcronymList(count,"x_0");
			addProjectionParameterAcronymList(count,"y_0");

			String[] parameterDefaultValue={"0.0"};
			projectionParameterDefaultValueList.add(count,parameterDefaultValue);
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");
			addProjectionParameterDefaultValue(count,"0.0");

			String[] parameterMaxValue={"360.0"};
			projectionParameterMaxValueList.add(count,parameterMaxValue);
			addProjectionParameterMaxValue(count,"90.0");
			addProjectionParameterMaxValue(count,"100000000.0");
			addProjectionParameterMaxValue(count,"100000000.0");

			String[] parameterMinValue={"-360.0"};
			projectionParameterMinValueList.add(count,parameterMinValue);
			addProjectionParameterMinValue(count,"-90.0");
			addProjectionParameterMinValue(count,"-100000000.0");
			addProjectionParameterMinValue(count,"-100000000.0");

			String[] projectionAcronym={"wintri"};
			projectionAcronymList.add(count,projectionAcronym);
		}
	}
	
	public void addProjectionName(int pos,String projectionName) throws CrsException
	{
		if(pos<0||pos>(projectionNameList.size()-1))
		{
			String strError="error_adding_projection_name";
			String strError2=projectionName;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionNames=(String[]) projectionNameList.get(pos);
		String[] newProjectionNames=new String[projectionNames.length+1];
		for(int i=0;i<projectionNames.length;i++)
		{
			newProjectionNames[i]=projectionNames[i];
		}
		newProjectionNames[projectionNames.length]=projectionName;
		projectionNameList.remove(pos);
		projectionNameList.add(pos,newProjectionNames);
	}
	
	public void addUnitName(int pos,String unitName) throws CrsException
	{
		if(pos<0||pos>(unitNameList.size()-1))
		{
			String strError="error_adding_unit_name";
			String strError2=unitName;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] unitNames=(String[]) unitNameList.get(pos);
		String[] newUnitNames=new String[unitNames.length+1];
		for(int i=0;i<unitNames.length;i++)
		{
			newUnitNames[i]=unitNames[i];
		}
		newUnitNames[unitNames.length]=unitName;
		unitNameList.remove(pos);
		unitNameList.add(pos,newUnitNames);
	}
	
	public void addProjectionParameterName(int pos,String projectionParameterName) throws CrsException
	{
		if(pos<0||pos>(projectionParameterNameList.size()-1))
		{
			String strError="error_adding_parameter_projection_name";
			String strError2=projectionParameterName;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameterNames=(String[]) projectionParameterNameList.get(pos);
		String[] newProjectionParameterNames=new String[projectionParameterNames.length+1];
		for(int i=0;i<projectionParameterNames.length;i++)
		{
			newProjectionParameterNames[i]=projectionParameterNames[i];
		}
		newProjectionParameterNames[projectionParameterNames.length]=projectionParameterName;
		projectionParameterNameList.remove(pos);
		projectionParameterNameList.add(pos,newProjectionParameterNames);
	}
	
	public void addProjectionParameter(int pos,String projectionParameter) throws CrsException
	{
		if(pos<0||pos>(projectionParameterList.size()-1))
		{
			String strError="error_adding_projection_parameter";
			String strError2=projectionParameter;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameters=(String[]) projectionParameterList.get(pos);
		String[] newProjectionParameters=new String[projectionParameters.length+1];
		for(int i=0;i<projectionParameters.length;i++)
		{
			newProjectionParameters[i]=projectionParameters[i];
		}
		newProjectionParameters[projectionParameters.length]=projectionParameter;
		projectionParameterList.remove(pos);
		projectionParameterList.add(pos,newProjectionParameters);
	}
	
	public void addProjectionParameterDefaultValue(int pos,String projectionParameterDefaultValue) throws CrsException
	{
		if(pos<0||pos>(projectionParameterDefaultValueList.size()-1))
		{
			String strError="error_adding_default_value_to_projection_parameter";
			String strError2=projectionParameterDefaultValue;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameterDefaultValues=(String[]) projectionParameterDefaultValueList.get(pos);
		String[] newProjectionParameterDefaultValues=new String[projectionParameterDefaultValues.length+1];
		for(int i=0;i<projectionParameterDefaultValues.length;i++)
		{
			newProjectionParameterDefaultValues[i]=projectionParameterDefaultValues[i];
		}
		newProjectionParameterDefaultValues[projectionParameterDefaultValues.length]=projectionParameterDefaultValue;
		projectionParameterDefaultValueList.remove(pos);
		projectionParameterDefaultValueList.add(pos,newProjectionParameterDefaultValues);
	}
	
	public void addProjectionParameterMaxValue(int pos,String projectionParameterMaxValue) throws CrsException
	{
		if(pos<0||pos>(projectionParameterMaxValueList.size()-1))
		{
			String strError="error_adding_max_value_to_projection_parameter";
			String strError2=projectionParameterMaxValue;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameterMaxValues=(String[]) projectionParameterMaxValueList.get(pos);
		String[] newProjectionParameterMaxValues=new String[projectionParameterMaxValues.length+1];
		for(int i=0;i<projectionParameterMaxValues.length;i++)
		{
			newProjectionParameterMaxValues[i]=projectionParameterMaxValues[i];
		}
		newProjectionParameterMaxValues[projectionParameterMaxValues.length]=projectionParameterMaxValue;
		projectionParameterMaxValueList.remove(pos);
		projectionParameterMaxValueList.add(pos,newProjectionParameterMaxValues);
	}
	
	public void addProjectionParameterMinValue(int pos,String projectionParameterMinValue) throws CrsException
	{
		if(pos<0||pos>(projectionParameterMinValueList.size()-1))
		{
			String strError="error_adding_min_value_to_projection_parameter";
			String strError2=projectionParameterMinValue;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameterMinValues=(String[]) projectionParameterMinValueList.get(pos);
		String[] newProjectionParameterMinValues=new String[projectionParameterMinValues.length+1];
		for(int i=0;i<projectionParameterMinValues.length;i++)
		{
			newProjectionParameterMinValues[i]=projectionParameterMinValues[i];
		}
		newProjectionParameterMinValues[projectionParameterMinValues.length]=projectionParameterMinValue;
		projectionParameterMinValueList.remove(pos);
		projectionParameterMinValueList.add(pos,newProjectionParameterMinValues);
	}
	
	public void addProjectionParameterAcronymList(int pos,String projectionParameterAcronym) throws CrsException
	{
		if(pos<0||pos>(projectionParameterAcronymList.size()-1))
		{
			String strError="error_adding_projection_acronym";
			String strError2=projectionParameterAcronym;
			String strError3="position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+": "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
		}
		String[] projectionParameterAcronyms=(String[]) projectionParameterAcronymList.get(pos);
		String[] newProjectionParameterAcronyms=new String[projectionParameterAcronyms.length+1];
		for(int i=0;i<projectionParameterAcronyms.length;i++)
		{
			newProjectionParameterAcronyms[i]=projectionParameterAcronyms[i];
		}
		newProjectionParameterAcronyms[projectionParameterAcronyms.length]=projectionParameterAcronym;
		projectionParameterAcronymList.remove(pos);
		projectionParameterAcronymList.add(pos,newProjectionParameterAcronyms);
	}
	
	public int findProjection(String projectionName)
	{
		for(int i=0;i<projectionNameList.size();i++)
		{
			String[] projectionNames=(String[]) projectionNameList.get(i);
			for(int j=0;j<projectionNames.length;j++)
			{
				if(projectionNames[j].toLowerCase().replaceAll(" ","").equals(projectionName.toLowerCase().replaceAll(" ","")))
					return i;
			}
		}
			
		return -1;
	}
	
	public int findProjectionParameter(String parameterName)
	{
		for(int i=0;i<projectionParameterNameList.size();i++)
		{
			String[] parameterNames=(String[]) projectionParameterNameList.get(i);
			for(int j=0;j<parameterNames.length;j++)
			{
				if(parameterNames[j].toLowerCase().replaceAll(" ","").equals(parameterName.toLowerCase().replaceAll(" ","")))
					return i;
			}
		}
			
		return -1;
	}
	
	public int findProjectionParameters(String parameterName1, String parameterName2)
	{
		for(int i=0;i<projectionParameterNameList.size();i++)
		{
			boolean existsParameter1=false;
			boolean existsParameter2=false;
			
			
			String[] parameterNames=(String[]) projectionParameterNameList.get(i);
			if(parameterNames[0].toLowerCase().replaceAll(" ","").equals(parameterName2.toLowerCase().replaceAll(" ","")))
			{
				existsParameter2=true;
				for(int j=0;j<parameterNames.length;j++)
				{
					if(parameterNames[j].toLowerCase().replaceAll(" ","").equals(parameterName1.toLowerCase().replaceAll(" ","")))
					{
						existsParameter1=true;
						break;
					}
				}
			}
			if(existsParameter1&&existsParameter2)
				return i;
		}
			
		return -1;
	}
	
	public int findUnit(String unitName)
	{
		for(int i=0;i<unitNameList.size();i++)
		{
			String[] unitNames=(String[]) unitNameList.get(i);
			for(int j=0;j<unitNames.length;j++)
			{
				if(unitNames[j].toLowerCase().replaceAll(" ","").equals(unitName.toLowerCase().replaceAll(" ","")))
					return i;
			}
		}
			
		return -1;
	}
	
	public String getProj4UnitName(int pos) throws CrsException 
	{
		if(pos<0||pos>(unitNameList.size()-1))
		{
			String strError="error_obtaining_unit_name";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		return ((String[]) unitNameList.get(pos))[0];
	}
	
	public String getProj4ProjectionName(int pos) throws CrsException 
	{
		if(pos<0||pos>(projectionNameList.size()-1))
		{
			String strError="error_obtaining_projection_name";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		return ((String[]) projectionNameList.get(pos))[0];
	}
	
	public String getProj4ProjectionParameterName(int pos) throws CrsException 
	{
		// Ojo decir a Jose Luis que quite los parametros del elipsoide
		if(pos<0||pos>(projectionParameterNameList.size()-1))
		{
			String strError="error_obtaining_projection_parameter_name";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		return ((String[]) projectionParameterNameList.get(pos))[0];
	}
	
	public ArrayList getProj4ProjectionParameters(int pos) throws CrsException 
	{
		
		if(pos<0||pos>(projectionParameterList.size()-1))
				throw new CrsException(new Exception());
		String[] parameterList=(String[])projectionParameterList.get(pos);
		ArrayList parameters=new ArrayList();
		for(int i=0;i<parameterList.length;i++)
		{
			String parameterName=parameterList[i];
			int posParameter=findProjectionParameter(parameterName);
			if(posParameter==-1)
			{
				String strError="the_parameter";
				String strError2=parameterName;
				String strError3="not_in_parameter_list";
				throw new CrsException(new Exception(PluginServices.getText(this, strError)+" = "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
			}
			//String parameterNameProj4=getProj4ProjectionParameterName(posParameter);
			String parameterNameProj4=parameterName;
			parameters.add(i,parameterNameProj4);
		}
		return parameters;
	}
	
	public ArrayList getProj4ProjectionParameterDefaultValues(int pos) throws CrsException 
	{
		
		if(pos<0||pos>(projectionParameterDefaultValueList.size()-1))
		{
			String strError="error_obtaining_default_value_to_projection_parameter";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		String[] parameterDefaultValueList=(String[])projectionParameterDefaultValueList.get(pos);
		ArrayList parameterDefaultValues=new ArrayList();
		for(int i=0;i<parameterDefaultValueList.length;i++)
		{
			String parameterDefaultValue=parameterDefaultValueList[i];
			parameterDefaultValues.add(i,parameterDefaultValue);
		}
		return parameterDefaultValues;
	}
	
	public ArrayList getProj4ProjectionParameterMaxValues(int pos) throws CrsException 
	{
		
		if(pos<0||pos>(projectionParameterMaxValueList.size()-1))
		{
			String strError="error_obtaining_max_value_to_projection_parameter";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		String[] parameterMaxValueList=(String[])projectionParameterMaxValueList.get(pos);
		ArrayList parameterMaxValues=new ArrayList();
		for(int i=0;i<parameterMaxValueList.length;i++)
		{
			String parameterMaxValue=parameterMaxValueList[i];
			parameterMaxValues.add(i,parameterMaxValue);
		}
		return parameterMaxValues;
	}
	
	public ArrayList getProj4ProjectionParameterMinValues(int pos) throws CrsException 
	{
		
		if(pos<0||pos>(projectionParameterMinValueList.size()-1))
		{
			String strError="error_obtaining_min_value_to_projection_parameter";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		String[] parameterMinValueList=(String[])projectionParameterMinValueList.get(pos);
		ArrayList parameterMinValues=new ArrayList();
		for(int i=0;i<parameterMinValueList.length;i++)
		{
			String parameterMinValue=parameterMinValueList[i];
			parameterMinValues.add(i,parameterMinValue);
		}
		return parameterMinValues;
	}
	
	public ArrayList getProj4ProjectionParameterAcronyms(int pos) throws CrsException 
	{
		
		if(pos<0||pos>(projectionParameterAcronymList.size()-1))
		{
			String strError="error_obtaining_projection_acronym";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		String[] parameterAcronymList=(String[])projectionParameterAcronymList.get(pos);
		ArrayList parameterAcronyms=new ArrayList();
		for(int i=0;i<parameterAcronymList.length;i++)
		{
			String parameterAcronym=parameterAcronymList[i];
			parameterAcronyms.add(i,parameterAcronym);
		}
		return parameterAcronyms;
	}
	
	public String getProjectionParameterUnitList(int pos) throws CrsException {
		if(pos<0||pos>(projectionParameterUnitList.size()-1))
		{
			String strError="error_obtaining_unit_list_of_projection_parameter";
			String strError2= "position_out_of_valid_limits";
			throw new CrsException(new Exception(PluginServices.getText(this, strError)+". "+PluginServices.getText(this, strError2)));
		}
		String [] projParamUnit = (String[]) projectionParameterUnitList.get(pos);
		return projParamUnit[0];
	}

	public String exportToProj4(Crs crs) throws CrsException
	{
		String strProj4="+proj=";
		String strDatumName="";
		String strDatumCode="";
		String strProj4ToMeter="";
		String strProj4Datum="";
		String[] primeMeridian=crs.getCrsWkt().getPrimen();
		String primeMeridianName=primeMeridian[0];
		double primeMeridianValue=Double.parseDouble(primeMeridian[1]); // -> hacer el cambio de unidades
		String[] strPrimeMeridianProj4=primeMeridianToProj4(primeMeridianName,primeMeridianValue);
		primeMeridianValue=Double.parseDouble(strPrimeMeridianProj4[1]);
		primeMeridianName=strPrimeMeridianProj4[0];
		String primeMeridianAcronym=strPrimeMeridianProj4[2]; // Puede ser cadena vacia->Analizar

		// Extracci√≥n del c√≥digo de Datum
		String codDatum="0";
		strDatumName=crs.getCrsWkt().getDatumName();
        int intCodDatum=0;//Integer.parseInt(codDatum);
        strProj4Datum=datumToProj4(strDatumName,intCodDatum);
        


		String strProj=crs.getCrsWkt().getProjcs();
		if(strProj.equals(""))
		{
			//System.out.println("Projection Name = "+"Geodetic");
			strProj4+="longlat ";
		}
		else
		{
	        // OJO ******************************************
			/*
			if(factor_to_meter!=1.0)
	        	strProj4ToMeter="+to_meter="+factor_to_meter+" ";
	        else
	        	strProj4ToMeter="+units=m ";
	        */
			
			
			//System.out.println("Projection      = "+strProj);
			String strProjName=crs.getCrsWkt().getProjection();
			//System.out.println("Projection Name = "+strProjName);
			int indexProj=findProjection(strProjName);
			if(indexProj==-1)
			{
				String strError="the_projection";
				String strError2= strProjName;
				String strError3="not_in_proj4";
				throw(new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3))));
			}
			String projectionName=(getProj4ProjectionName(indexProj)).trim();
			ArrayList parameterNames=getProj4ProjectionParameters(indexProj);
			ArrayList parameterAcronyms=getProj4ProjectionParameterAcronyms(indexProj);
			ArrayList parameterValues=getProj4ProjectionParameterDefaultValues(indexProj);
			ArrayList parameterMaxValues=getProj4ProjectionParameterMaxValues(indexProj);
			ArrayList parameterMinValues=getProj4ProjectionParameterMinValues(indexProj);
			//System.out.println("Parametros:"+parameterNames);
			String[] gtParameterValues=crs.getCrsWkt().getParam_value();
			String[] gtParameterNames=crs.getCrsWkt().getParam_name();
			for(int i=0;i<parameterNames.size();i++)
			{
				boolean existsParameter=false;
				String parameterValue="";
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					//int posGtParameter=findProjectionParameter(gtParameterName);
					int posGtParameter=findProjectionParameters(gtParameterName,(String)parameterNames.get(i));
					//int posParameter=findProjectionParameter((String)parameterNames.get(i));
					//if(((String)parameterNames.get(i)).trim().equals(gtParameterNames[j].trim()))
					if(posGtParameter!=-1)
					{
						gtParameterName=getProj4ProjectionParameterName(posGtParameter);
						gtParameterNames[j]=gtParameterName;
						existsParameter=true;
						double maxValue=Double.parseDouble((String)parameterMaxValues.get(i));
						double minValue=Double.parseDouble((String)parameterMinValues.get(i));
						//parameterValue=Double.parseDouble(gtParameterValues[j]);
						parameterValue=gtParameterValues[j]; // Ojo unidades -> analizar
						double auxValue=Double.parseDouble(parameterValue);
						if((auxValue<minValue)||(auxValue>maxValue))
						{
							String strError="the_parameter";
							String strError2=gtParameterName;
							String strError3="out_of_domain";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
						}
						else if (((String)parameterNames.get(i)).trim().equals("scale_factor") && (auxValue==minValue)) {
							String strError="the_parameter";
							String strError2=gtParameterName;
							String strError3="out_of_domain";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
						}
						break;
					}
					/*else
					{
						String strError="El parametro recibido de GeoTools";
						strError+=gtParameterName;
						strError+=" no esta contemplado en la proyeccion";
						System.out.println(strError);
						throw new CrsException(new Exception());
					}*/
				}
				if(existsParameter)
				{
					parameterValues.set(i,parameterValue);
					//System.out.println("- Parametro["+parameterNames.get(i)+"]="+parameterValue);
				}
				/*
				else
				{
					value=Double.parseDouble((String)parameterDefaultValues.get(i));
					System.out.println("El parametro "+parameterNames.get(i)+" no figura en la lista geotools");
					throw(new CrsException(new Exception()));
				}
				*/
			}
			boolean isSomerc=false;
			boolean isOmerc=false;
			double valueAlpha=0;
			double valueGamma=0;
			boolean existsAlpha=false;
			boolean existsGamma=false;
			String[] projectionAcronym=(String[])projectionAcronymList.get(indexProj);
			if(projectionName.equals("Oblique_Mercator"))
			{
				isOmerc=true;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("latitude_of_origin")
							||gtParameterName.equalsIgnoreCase("standard_parallel_1")
							||gtParameterName.equalsIgnoreCase("latitude_of_center"))
					{
						double value=Double.parseDouble(gtParameterValues[j]);
						if(Math.abs(Math.abs(value)-90.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_latitude_origin_close_to_the_poles";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
						}
					}
					if(gtParameterName.equalsIgnoreCase("azimuth"))
					{
						valueAlpha=Double.parseDouble(gtParameterValues[j]);
						if(Math.abs(valueAlpha-90.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_azimut_close_to";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" 90"));
						}
						if(Math.abs(valueAlpha-270.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_azimut_close_to";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" 270"));
						}
						existsAlpha=true;
					}
					if(gtParameterName.equalsIgnoreCase("rectified_grid_angle"))
					{
						valueGamma=Double.parseDouble(gtParameterValues[j]);
						existsGamma=true;
					}
				}
				if(existsAlpha&&existsGamma)
				{
					if(Math.abs(valueAlpha-valueGamma)>0.00000001)
					{
						String strError="in_proj4_projection";
						String strError2="Oblique_Mercator";
						String strError3="not_admit_different_azimut_and_spin_axis";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
				}
				
				// Necesito buscar un caso de ejemplo
			}
			if(projectionName.equals("Hotine_Oblique_Mercator_Two_Point_Center"))
			{
				double lat_1=0.0;
				double lat_2=0.0;
				boolean exists_Lat_1=false;
				boolean exists_Lat_2=false;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("Latitude_Of_1st_Point"))
					{
						lat_1=Double.parseDouble(gtParameterValues[j]);
						exists_Lat_1=true;
					}
					if(gtParameterName.equalsIgnoreCase("Latitude_Of_2nd_Point"))
					{
						lat_2=Double.parseDouble(gtParameterValues[j]);
						exists_Lat_2= true;
					}
				}
				if(exists_Lat_1&&exists_Lat_2)
				{
					if(Math.abs(lat_1-lat_2)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_equal_lat_1_and_lat_2";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					if(Math.abs(lat_1)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_zero_lat_1";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					if(Math.abs(Math.abs(lat_2)-90)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_values_90_or_minus_90_lat_2";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
				}
			}
			String strProjectionAcronym=projectionAcronym[0];
			String strExtraProj4="";
			// Control de casos especiales
			boolean isLaborde=false;
			boolean isLcc1sp=false;
			boolean isMerc=false;
			boolean exists_sf=false;
			boolean exists_lo=false;
			boolean isSterePolar=false;
			boolean isStereOblique=false;  // pondremos +proj=sterea
			boolean exists_stdPar=false;
			double value_stdPar=0.0;
			if(projectionAcronym[0].equals("merc")) // 1 - Mercator
			{
				isMerc=true;
				double value_sf=0.0;
				double value_lo=0.0;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("latitude_of_origin")
							||gtParameterName.equalsIgnoreCase("standard_parallel_1")
							||gtParameterName.equalsIgnoreCase("latitude_of_center"))
					{
						exists_lo=true;
						value_lo=Double.parseDouble(gtParameterValues[j]);
					}
					if(gtParameterName.equalsIgnoreCase("scale_factor"))
					{
						exists_sf=true;
						value_sf=Double.parseDouble(gtParameterValues[j]);
					}
				}
				if(exists_sf&&exists_lo)
				{
					if(value_sf!=1.0&&value_lo!=0.0)
					{
						String strError="in_proj4_projection";
						String strError2 = "Mercator";
						String strError3 = "not_admit_scale_factor_and_latitude_of_origin";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					else if(projectionName.equals("Mercator_1SP"))
						exists_lo=false;
					else if(projectionName.equals("Mercator_2SP"))
						exists_sf=false;
					/*
					else if(value_sf!=1.0)
						exists_lo=false;
					else if(value_sf==1.0)
						exists_sf=false;
					*/
				}
			}
			else if(projectionAcronym[0].equals("lcc"))
			{
				if(projectionName.equalsIgnoreCase("Lambert_Conformal_Conic_1SP"))
				{
					isLcc1sp=true;
				}
			}
			else if(projectionAcronym[0].equals("stere"))
			{
				if(projectionName.equalsIgnoreCase("Polar_Stereographic")) // lat_ts=lat_0={90 o -90)
				{
					isSterePolar=true;
					for(int j=0;j<gtParameterNames.length;j++)
					{
						String gtParameterName=gtParameterNames[j].trim();
						if(gtParameterName.equalsIgnoreCase("scale_factor"))
						{
							double value_sf=Double.parseDouble(gtParameterValues[j]);
							exists_sf=true;
							//if(Math.abs(value_sf-1.0)>0.000001)
							//	exists_sf=true;
						}
						if(gtParameterName.equalsIgnoreCase("latitude_of_standard_parallel"))
						{
							value_stdPar=Double.parseDouble(gtParameterValues[j]);
							exists_stdPar=true;
						}
					}
				}
				if(projectionName.equalsIgnoreCase("Stereographic")) // No admite lat_ts
				{
					// Comprobamos si es polar
					boolean isPolar=false;
					for(int j=0;j<gtParameterNames.length;j++)
					{
						String gtParameterAcronymn=((String)parameterAcronyms.get(j)).trim();
						if(gtParameterNames[j].equalsIgnoreCase("latitude_of_origin"))
						//if(gtParameterAcronymn.equalsIgnoreCase("lat_0"))
						{
							double gtParameterValue=Double.parseDouble(gtParameterValues[j]);
							if(Math.abs(gtParameterValue-90.0)<angularTolerance) isPolar=true;
							else if(Math.abs(gtParameterValue-(-90.0))<angularTolerance)  isPolar=true;
							break;
						}
					}
					if(isPolar)
					{
						isSterePolar=true;
						strProjectionAcronym="stere";
						for(int j=0;j<gtParameterNames.length;j++)
						{
							String gtParameterName=gtParameterNames[j].trim();
							if(gtParameterName.equalsIgnoreCase("scale_factor"))
							{
								double value_sf=Double.parseDouble(gtParameterValues[j]);
								exists_sf=true;
								//if(Math.abs(value_sf-1.0)>0.000001)
								//	exists_sf=true;
							}
							if(gtParameterName.equalsIgnoreCase("latitude_of_standard_parallel"))
							{
								value_stdPar=Double.parseDouble(gtParameterValues[j]);
								exists_stdPar=true;
							}
						}
					}
					if(!isPolar)
					{
						isStereOblique=true;
						if(exists_stdPar)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Stereographic";
							String strError3="not_admit_parameter";
							String strError4="latitude_of_standard_parallel";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" "+PluginServices.getText(this, strError4)));
						}
						strProjectionAcronym="sterea";
					}
				}
			}
			else if(projectionAcronym[0].equals("omerc"))
			{
				boolean existsLat1=false;
				boolean existsLat2=false;
				boolean existsLon1=false;
				boolean existsLon2=false;
				// Necesito buscar un caso de ejemplo
			}
			else if(projectionAcronym[0].equals("mill"))
			{
				strExtraProj4="+R_A ";
			}
			else if(projectionAcronym[0].equals("vandg"))
			{
				strExtraProj4="+R_A ";
			}
			else if(projectionAcronym[0].equals("labrd"))
			{
				isLaborde=true;
			}
			
			strProj4=strProj4+strProjectionAcronym+" ";

			for(int i=0;i<parameterNames.size();i++)
			{
				boolean control=true;
				String parameterName=((String)parameterNames.get(i)).trim();
				String parameterAcronym=((String)parameterAcronyms.get(i)).trim();
				String strParameterValue=((String)parameterValues.get(i)).trim();
				if(isMerc)
				{
					if(parameterName.equalsIgnoreCase("latitude_of_origin")
							||parameterName.equalsIgnoreCase("standard_parallel_1")
							||parameterName.equalsIgnoreCase("latitude_of_center"))
					{
						if(!exists_lo)
							control=false;
					}
					if(parameterName.equalsIgnoreCase("scale_factor"))
					{
						if(!exists_sf)
							control=false;
					}
				}
				if(isSterePolar)
				{
					String gtParameterAcronymn=((String)parameterAcronyms.get(i)).trim();
					if(gtParameterAcronymn.equalsIgnoreCase("lat_0")) // Esto resuelve el caso del EPSG:3031
					{
						if(exists_stdPar){
							double parameterValue=Double.parseDouble(strParameterValue);
							if((parameterValue>0)&&(value_stdPar<0)){
								strParameterValue="-90.0";
							}
							if((parameterValue<0)&&(value_stdPar>0)){
								strParameterValue="90.0";
							}
						}
					}
					
					if(gtParameterAcronymn.equalsIgnoreCase("lat_ts"))
					{
						if(exists_sf)
							control=false;
					}
					if(parameterName.equalsIgnoreCase("scale_factor"))
					{
						if(!exists_sf)
							control=false;
					}
				}
				if(isSomerc)
				{
					if(parameterName.equals("rectified_grid_angle"))
						control=false;
					if(parameterName.equals("azimuth"))
						control=false;
				}
				if(isOmerc)
				{
					if(parameterName.equals("rectified_grid_angle"))
					{
						if(existsAlpha)
							control=false;
						else
							parameterAcronym="alpha";
					}
				}
				if(parameterAcronym.equals("lon_0")
						||parameterAcronym.equals("lonc"))
				{
					double parameterValue=Double.parseDouble(strParameterValue);
					if(!projectionAcronym[0].equalsIgnoreCase("krovak"))
					{
						parameterValue=parameterValue-primeMeridianValue;
					}
					strParameterValue=Double.toString(parameterValue);
				}
				if(control)
					strProj4=strProj4+"+"+parameterAcronym+"="+strParameterValue+" ";
				if(isLcc1sp)
				{
					if(parameterAcronym.equals("lat_0"))
					{
						strProj4=strProj4+"+lat_1="+strParameterValue+" ";
						strProj4=strProj4+"+lat_2="+strParameterValue+" ";
					}
				}
			}
			if(isLaborde)
				strProj4+="+azi=18.9 +lat_0=-18.9 +lon_0=44.1 +k_0=0.9995 +x_0=400000 +y_0=800000 +ellps=intl ";
			strProj4+=strExtraProj4;
			//getProj4ProjectionName();
		}
		
		// Que pasa si no hay elipsoide? -> OGR por defecto pone WGS84
		double a=0;
		double inv_f=0;
		String elipName=crs.getCrsWkt().getSpheroid()[0];
		a=Double.parseDouble(crs.getCrsWkt().getSpheroid()[1]);
		inv_f=Double.parseDouble(crs.getCrsWkt().getSpheroid()[2]);
		String strEllipseAcronym=ellipseToProj4(a,inv_f);
		String strEllipse="";
		if(strEllipseAcronym.equals(""))
		{
			if (!Double.isInfinite(inv_f))
			{
				if(inv_f>0.0)
					strEllipse="+a="+a+" +rf="+inv_f+" ";
				else
					strEllipse="+R="+a+" ";
			}
			else
				strEllipse="+R="+a+" ";
		}
		else
		{
			strEllipse="+ellps="+strEllipseAcronym+" ";
		}
		strProj4+=strEllipse;
		//System.out.println("Elipsoide["+elipName+"]=("+a+","+inv_f+")");
		if(!strProj4Datum.equals(""))
		{
			strProj4+=strProj4Datum;
		}
		strProj4+=primeMeridianAcronym;
		String strWkt=crs.getWKT();
		if(!strProj4ToMeter.equals(""))
			strProj4+=strProj4ToMeter;
		
		//System.out.println("- Cadena proj4: "+strProj4);
		return strProj4;		
	}
		
	public String exportToProj4(CoordinateReferenceSystem crs) throws CrsException
	{
		String strProj4="+proj=";
		String[] primeMeridian = new String[2];
		String strProj="";
		String strProjName="";
		String strDatumName="";
		String strDatumCode="";
		String strProj4Datum="";
		String strProj4ToMeter="";
		String[] gtParameterValues=new String[1];
		String[] gtParameterNames= new String[1];
		String[] spheroid = new String[3];
		double a=0;
		double inv_f=0;
		String elipName="";
		if (crs instanceof DefaultProjectedCRS) {
			DefaultProjectedCRS crsProjected = (DefaultProjectedCRS) crs;
			primeMeridian = Primem(((DefaultGeodeticDatum) crsProjected.getDatum()).getPrimeMeridian());
			String[] proj = crsProjected.getName().toString().split(":");
			if (proj.length>1)
				strProj = proj[1];
			else
				strProj = proj[0];
			strProjName = getName(crsProjected.getConversionFromBase().getMethod().getName());
			gtParameterValues = new String[crsProjected.getConversionFromBase().getParameterValues().values().size()];
			gtParameterNames = new String[crsProjected.getConversionFromBase().getParameterValues().values().size()];
			String str;
			for (int i=0; i< crsProjected.getConversionFromBase().getParameterValues().values().size();i++) {
				str = crsProjected.getConversionFromBase().getParameterValues().values().get(i).toString();
				Unit u = crsProjected.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).getUnit();
				double value = crsProjected.getConversionFromBase().getParameterValues().parameter(str.split("=")[0]).doubleValue();
				value = convert(value, u.toString());
				gtParameterNames[i] = str.split("=")[0];
				gtParameterValues [i] = String.valueOf(value);
			}
			spheroid = Spheroid(((DefaultGeodeticDatum) crsProjected.getDatum()).getEllipsoid());
			elipName=spheroid[0];
			a=Double.parseDouble(spheroid[1]);
			inv_f=Double.parseDouble(spheroid[2]);
			Ellipsoid ellip = ((DefaultGeodeticDatum) crsProjected.getDatum()).getEllipsoid();
            Unit unit = ellip.getAxisUnit();
            String[] i_un = unit.toString().split("[*]");
	        if (i_un.length>1)
	        {
	        	try{
		        	a=a*Double.parseDouble(i_un[1].replaceAll("]", ""));
	        		//factor_to_meter=Double.parseDouble(un[1].replaceAll("]", ""));
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        }
	        else if (i_un[0].equals("m"))
	        {
	        	try{
	        		//factor_to_meter=Double.parseDouble(un[0]);
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        } else if (i_un[0].equals("ft")) {
	        	/**
	        	 * Cuando viene en otro formato, coger el valor de la unidad
	        	 * correspondiente...
	        	 */
	        	//Caso de los pies
	        	//factor_to_meter = 0.3048;
	        }
			// Extracci√≥n del c√≥digo de Datum
			String codDatum="0";
			String[] val=((DefaultProjectedCRS)crs).getDatum().getName().toString().split(":");
			if (val.length<2)
				strDatumName=val[0];
			else
				strDatumName=val[1];
	        for (Iterator iter =((DefaultProjectedCRS)crs).getDatum().getIdentifiers().iterator();iter.hasNext();) {
	            Identifier element = (Identifier) iter.next();
	            codDatum = element.getCode();          
	        }
	        int intCodDatum=Integer.parseInt(codDatum);
	        strProj4Datum=datumToProj4(strDatumName,intCodDatum);
	        double factor_to_meter=1.0;
	        Unit u = crs.getCoordinateSystem().getAxis(0).getUnit();
	        String[] un = u.toString().split("[*]");
	        if (un.length>1)
	        {
	        	try{
		        	factor_to_meter=Double.parseDouble(un[1].replaceAll("]", ""));
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        }
	        else if (un[0].equals("m"))
	        {
	        	try{
	        		factor_to_meter=Double.parseDouble(un[0]);
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        } else if (un[0].equals("ft")) {
	        	/**
	        	 * Cuando viene en otro formato, coger el valor de la unidad
	        	 * correspondiente...
	        	 */
	        	//Caso de los pies
	        	factor_to_meter = 0.3048;
	        }
	        if(factor_to_meter!=1.0)
	        	strProj4ToMeter="+to_meter="+factor_to_meter+" ";
	        else
	        	strProj4ToMeter="+units=m ";

		}
		else if (crs instanceof DefaultGeographicCRS) {
			DefaultGeographicCRS crsGeographic = (DefaultGeographicCRS) crs;
			primeMeridian = Primem(((DefaultGeodeticDatum) crsGeographic.getDatum()).getPrimeMeridian());
			spheroid = Spheroid(((DefaultGeodeticDatum) crsGeographic.getDatum()).getEllipsoid());
			elipName=spheroid[0];
			a=Double.parseDouble(spheroid[1]);
			inv_f=Double.parseDouble(spheroid[2]);
			Ellipsoid ellip = ((DefaultGeodeticDatum) crsGeographic.getDatum()).getEllipsoid();
            Unit unit = ellip.getAxisUnit();
            String[] i_un = unit.toString().split("[*]");
	        if (i_un.length>1)
	        {
	        	try{
		        	a=a*Double.parseDouble(i_un[1].replaceAll("]", ""));
	        		//factor_to_meter=Double.parseDouble(un[1].replaceAll("]", ""));
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        }
	        else if (i_un[0].equals("m"))
	        {
	        	try{
	        		//factor_to_meter=Double.parseDouble(un[0]);
	        	}
	        	catch(java.lang.NumberFormatException t){}
	        } else if (i_un[0].equals("ft")) {
	        	/**
	        	 * Cuando viene en otro formato, coger el valor de la unidad
	        	 * correspondiente...
	        	 */
	        	//Caso de los pies
	        	//factor_to_meter = 0.3048;
	        }

            // Extracci√≥n del c√≥digo de Datum
			String codDatum="0";
			String[] val=((DefaultGeographicCRS)crs).getDatum().getName().toString().split(":");
			if (val.length<2)
				strDatumName=val[0];
			else
				strDatumName=val[1];
	        for (Iterator iter =((DefaultGeographicCRS)crs).getDatum().getIdentifiers().iterator();iter.hasNext();) {
	            Identifier element = (Identifier) iter.next();
	            codDatum = element.getCode();          
	        }
	        int intCodDatum=Integer.parseInt(codDatum);
	        strProj4Datum=datumToProj4(strDatumName,intCodDatum);
		}
		else {
			throw(new CrsException(new Exception(PluginServices.getText(this, "not_geographic_nor_projected"))));
		}
		
		String primeMeridianName=primeMeridian[0];
		double primeMeridianValue = -1;
		if (primeMeridian[1] == null) {
			throw(new CrsException(new Exception(PluginServices.getText(this, "error_prime_meridiam_parameters"))));
		} else {
			primeMeridianValue=Double.parseDouble(primeMeridian[1]); // -> hacer el cambio de unidades
		}		
		String[] strPrimeMeridianProj4=primeMeridianToProj4(primeMeridianName,primeMeridianValue);
		primeMeridianValue=Double.parseDouble(strPrimeMeridianProj4[1]);
		primeMeridianName=strPrimeMeridianProj4[0];
		String primeMeridianAcronym=strPrimeMeridianProj4[2]; // Puede ser cadena vacia->Analizar

		if(strProj.equals(""))
		{
			//System.out.println("Projection Name = "+"Geodetic");
			strProj4+="longlat ";
		}
		else
		{
			int indexProj=findProjection(strProjName);
			if(indexProj==-1)
			{
				String strError="the_projection";
				String strError2=strProjName;
				String strError3="not_in_proj4";
				throw(new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3))));
			}
			String projectionName=(getProj4ProjectionName(indexProj)).trim();
			ArrayList parameterNames=getProj4ProjectionParameters(indexProj);
			ArrayList parameterAcronyms=getProj4ProjectionParameterAcronyms(indexProj);
			ArrayList parameterValues=getProj4ProjectionParameterDefaultValues(indexProj);
			ArrayList parameterMaxValues=getProj4ProjectionParameterMaxValues(indexProj);
			ArrayList parameterMinValues=getProj4ProjectionParameterMinValues(indexProj);
			for(int i=0;i<parameterNames.size();i++)
			{
				boolean existsParameter=false;
				String parameterValue="";
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					//System.out.println( gtParameterName);
					//int posGtParameter=findProjectionParameter(gtParameterName);
					int posGtParameter=findProjectionParameters(gtParameterName,(String)parameterNames.get(i));
					//int posParameter=findProjectionParameter((String)parameterNames.get(i));
					//if(((String)parameterNames.get(i)).trim().equals(gtParameterNames[j].trim()))
					if(posGtParameter!=-1)
					{
						gtParameterName=getProj4ProjectionParameterName(posGtParameter);
						gtParameterNames[j]=gtParameterName;
						existsParameter=true;
						double maxValue=Double.parseDouble((String)parameterMaxValues.get(i));
						double minValue=Double.parseDouble((String)parameterMinValues.get(i));
						//parameterValue=Double.parseDouble(gtParameterValues[j]);
						parameterValue=gtParameterValues[j]; // Ojo unidades -> analizar
						double auxValue=Double.parseDouble(parameterValue);
						if((auxValue<minValue)||(auxValue>maxValue))
						{
							String strError="the_parameter";
							String strError2=gtParameterName;
							String strError3="out_of_domain";
							throw(new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3))));
						}
						else if (((String)parameterNames.get(i)).trim().equals("scale_factor") && (auxValue==minValue)) {
							String strError="the_parameter";
							String strError2=gtParameterName;
							String strError3="out_of_domain";
							throw(new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3))));
						}
						break;
					}
					/*else
					{
						String strError="El parametro recibido de GeoTools ";
						strError+=gtParameterName;
						strError+=" no esta contemplado en la proyeccion";
						System.out.println(strError);
						throw new CrsException(new Exception());
					}*/
				}
				if(existsParameter)
				{
					parameterValues.set(i,parameterValue);
				}			
			}
			boolean isSomerc=false;
			boolean isOmerc=false;
			boolean isLcc=false;
			double valueAlpha=0;
			double valueGamma=0;
			boolean existsAlpha=false;
			boolean existsGamma=false;
			String[] projectionAcronym=(String[])projectionAcronymList.get(indexProj);
			if(projectionName.equals("Lambert_Conformal_Conic"))
			{
				for(int i=0;i<parameterNames.size();i++)
				{
					boolean control=true;
					String parameterName=((String)parameterNames.get(i)).trim();
					if(parameterName.equalsIgnoreCase("standard_parallel_2"))
						isLcc=true;
				}
			}
			if(projectionName.equals("Oblique_Mercator"))
			{
				isOmerc=true;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("latitude_of_origin")
							||gtParameterName.equalsIgnoreCase("standard_parallel_1")
							||gtParameterName.equalsIgnoreCase("latitude_of_center"))
					{
						double value=Double.parseDouble(gtParameterValues[j]);
						if(Math.abs(Math.abs(value)-90.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_latitude_origin_close_to_the_poles";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
						}
					}
					if(gtParameterName.equalsIgnoreCase("azimuth"))
					{
						valueAlpha=Double.parseDouble(gtParameterValues[j]);
						if(Math.abs(valueAlpha-90.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_azimut_close_to";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" 90"));
						}
						if(Math.abs(valueAlpha-270.0)<0.0001)
						{
							String strError="in_proj4_projection";
							String strError2="Oblique_Mercator";
							String strError3="not_admit_azimut_close_to";
							throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" 270"));
						}
						existsAlpha=true;
					}
					if(gtParameterName.equalsIgnoreCase("rectified_grid_angle"))
					{
						valueGamma=Double.parseDouble(gtParameterValues[j]);
						existsGamma=true;
					}
				}
				if(existsAlpha&&existsGamma)
				{
					if(Math.abs(valueAlpha-valueGamma)>0.00000001)
					{
						String strError="in_proj4_projection";
						String strError2="Oblique_Mercator";
						String strError3="not_admit_different_azimut_and_spin_axis";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
				}
				
				// Necesito buscar un caso de ejemplo
			}
			if(projectionName.equals("Hotine_Oblique_Mercator_Two_Point_Center"))
			{
				double lat_1=0.0;
				double lat_2=0.0;
				boolean exists_Lat_1=false;
				boolean exists_Lat_2=false;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("Latitude_Of_1st_Point"))
					{
						double value=Double.parseDouble(gtParameterValues[j]);
					}
					if(gtParameterName.equalsIgnoreCase("Latitude_Of_2nd_Point"))
					{
						double value=Double.parseDouble(gtParameterValues[j]);
					}
				}
				if(exists_Lat_1&&exists_Lat_2)
				{
					if(Math.abs(lat_1-lat_2)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_equal_lat_1_and_lat_2";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					if(Math.abs(lat_1)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_zero_lat_1";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					if(Math.abs(Math.abs(lat_2)-90)<0.0001)
					{
						String strError="in_proj4_projection";
						String strError2="Hotine-Oblique Mercator Two Points";
						String strError3="not_values_90_or_minus_90_lat_2";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
				}
			}
			String projAcronym=projectionAcronym[0];
			if(isLcc)
				projAcronym="lcc";
			String strExtraProj4="";
			// Control de casos especiales
			boolean isLaborde=false;
			boolean isLcc1sp=false;
			boolean isMerc=false;
			boolean exists_sf=false;
			boolean exists_lo=false;
			boolean isSterePolar=false;
			boolean isStereOblique=false;  // pondremos +proj=sterea
			boolean exists_stdPar=false;
			double value_stdPar=0.0;
			if(projectionAcronym[0].equals("merc")) // 1 - Mercator
			{
				isMerc=true;
				double value_sf=0.0;
				double value_lo=0.0;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterName=gtParameterNames[j].trim();
					if(gtParameterName.equalsIgnoreCase("latitude_of_origin")
							||gtParameterName.equalsIgnoreCase("standard_parallel_1")
							||gtParameterName.equalsIgnoreCase("latitude_of_center"))
					{
						exists_lo=true;
						value_lo=Double.parseDouble(gtParameterValues[j]);
					}
					if(gtParameterName.equalsIgnoreCase("scale_factor"))
					{
						exists_sf=true;
						value_sf=Double.parseDouble(gtParameterValues[j]);
					}
				}
				if(exists_sf&&exists_lo)
				{
					if(value_sf!=1.0&&value_lo!=0.0)
					{
						String strError="in_proj4_projection";
						String strError2 = "Mercator";
						String strError3 = "not_admit_scale_factor_and_latitude_of_origin";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)));
					}
					else if(projectionName.equals("Mercator_1SP"))
						exists_lo=false;
					else if(projectionName.equals("Mercator_2SP"))
						exists_sf=false;
					/*
					else if(value_sf!=1.0)
						exists_lo=false;
					else if(value_sf==1.0)
						exists_sf=false;
					*/
				}
			}
			else if(projectionAcronym[0].equals("lcc"))
			{
				if(projectionName.equalsIgnoreCase("Lambert_Conformal_Conic_1SP"))
				{
					isLcc1sp=true;
				}
			}
			else if(projectionAcronym[0].equals("stere"))
			{
				if(projectionName.equalsIgnoreCase("Polar_Stereographic")) // lat_ts=lat_0={90 o -90)
				{
					isSterePolar=true;
					for(int j=0;j<gtParameterNames.length;j++)
					{
						String gtParameterName=gtParameterNames[j].trim();
						if(gtParameterName.equalsIgnoreCase("scale_factor"))
						{
							double value_sf=Double.parseDouble(gtParameterValues[j]);
							exists_sf=true;
							//if(Math.abs(value_sf-1.0)>0.000001)
							//	exists_sf=true;
						}
						if(gtParameterName.equalsIgnoreCase("latitude_of_standard_parallel"))
						{
							value_stdPar=Double.parseDouble(gtParameterValues[j]);
							exists_stdPar=true;
						}
					}
				}
			}
			if(projectionName.equalsIgnoreCase("Stereographic")) // No admite lat_ts
			{
				// Comprobamos si es polar
				boolean isPolar=false;
				for(int j=0;j<gtParameterNames.length;j++)
				{
					String gtParameterAcronymn=((String)parameterAcronyms.get(j)).trim();
					//if(gtParameterAcronymn.equalsIgnoreCase("lat_0"))
					if(gtParameterNames[j].equalsIgnoreCase("latitude_of_origin"))
					{
						double gtParameterValue=Double.parseDouble(gtParameterValues[j]);
						if(Math.abs(gtParameterValue-90.0)<angularTolerance) isPolar=true;
						else if(Math.abs(gtParameterValue-(-90.0))<angularTolerance)  isPolar=true;
						break;
					}
				}
				if(isPolar)
				{
					isSterePolar=true;
					projAcronym="stere";
					for(int j=0;j<gtParameterNames.length;j++)
					{
						String gtParameterName=gtParameterNames[j].trim();
						if(gtParameterName.equalsIgnoreCase("scale_factor"))
						{
							double value_sf=Double.parseDouble(gtParameterValues[j]);
							exists_sf=true;
							//if(Math.abs(value_sf-1.0)>0.000001)
							//	exists_sf=true;
						}
						if(gtParameterName.equalsIgnoreCase("latitude_of_standard_parallel"))
						{
							value_stdPar=Double.parseDouble(gtParameterValues[j]);
							exists_stdPar=true;
						}
					}
				}
				if(!isPolar)
				{
					isStereOblique=true;
					projAcronym="sterea";
					if(exists_stdPar)
					{
						String strError="in_proj4_projection";
						String strError2="Oblique_Stereographic";
						String strError3="not_admit_parameter";
						String strError4="latitude_of_standard_parallel";
						throw new CrsException(new Exception(PluginServices.getText(this, strError)+" "+PluginServices.getText(this, strError2)+" "+PluginServices.getText(this, strError3)+" "+PluginServices.getText(this, strError4)));
					}
				}
			}
			else if(projectionAcronym[0].equals("mill"))
			{
				strExtraProj4="+R_A ";
			}
			else if(projectionAcronym[0].equals("vandg"))
			{
				strExtraProj4="+R_A ";
			}
			else if(projectionAcronym[0].equals("labrd"))
			{
				isLaborde=true;
			}
			strProj4=strProj4+projAcronym+" ";

			for(int i=0;i<parameterNames.size();i++)
			{
				boolean control=true;
				String parameterName=((String)parameterNames.get(i)).trim();
				String parameterAcronym=((String)parameterAcronyms.get(i)).trim();
				String strParameterValue=((String)parameterValues.get(i)).trim();
				if(isMerc)
				{
					if(parameterName.equalsIgnoreCase("latitude_of_origin")
							||parameterName.equalsIgnoreCase("standard_parallel_1")
							||parameterName.equalsIgnoreCase("latitude_of_center"))
					{
						if(!exists_lo)
							control=false;
					}
					if(parameterName.equalsIgnoreCase("scale_factor"))
					{
						if(!exists_sf)
							control=false;
					}
				}
				if(isSterePolar)
				{
					String gtParameterAcronymn=((String)parameterAcronyms.get(i)).trim();
					if(gtParameterAcronymn.equalsIgnoreCase("lat_0")) // Esto resuelve el caso del EPSG:3031
					{
						if(exists_stdPar){
							double parameterValue=Double.parseDouble(strParameterValue);
							if((parameterValue>0)&&(value_stdPar<0)){
								strParameterValue="-90.0";
							}
							if((parameterValue<0)&&(value_stdPar>0)){
								strParameterValue="90.0";
							}
						}
					}
					if(gtParameterAcronymn.equalsIgnoreCase("lat_ts"))
					{
						if(exists_sf)
							control=false;
					}
					if(parameterName.equalsIgnoreCase("scale_factor"))
					{
						if(!exists_sf)
							control=false;
					}
				}
				if(isSomerc)
				{
					if(parameterName.equals("rectified_grid_angle"))
						control=false;
					if(parameterName.equals("azimuth"))
						control=false;
				}
				if(isOmerc)
				{
					if(parameterName.equals("rectified_grid_angle"))
					{
						if(existsAlpha)
							control=false;
						else
							parameterAcronym="alpha";
					}
				}
				if(parameterAcronym.equals("lon_0")
						||parameterAcronym.equals("lonc"))
				{
					double parameterValue=Double.parseDouble(strParameterValue);
					if(!projectionAcronym[0].equalsIgnoreCase("krovak"))
					{
						parameterValue=parameterValue-primeMeridianValue;
					}
					strParameterValue=Double.toString(parameterValue);
				}
				if(control)
					strProj4=strProj4+"+"+parameterAcronym+"="+strParameterValue+" ";
				if(isLcc1sp)
				{
					if(parameterAcronym.equals("lat_0"))
					{
						strProj4=strProj4+"+lat_1="+strParameterValue+" ";
						strProj4=strProj4+"+lat_2="+strParameterValue+" ";
					}
				}
			}
			if(isLaborde)
				strProj4+="+azi=18.9 +lat_0=-18.9 +lon_0=44.1 +k_0=0.9995 +x_0=400000 +y_0=800000 +ellps=intl ";
			strProj4+=strExtraProj4;
			//getProj4ProjectionName();
		}
		
		// Que pasa si no hay elipsoide? -> OGR por defecto pone WGS84
		String strEllipseAcronym=ellipseToProj4(a,inv_f);
		String strEllipse="";
		if(strEllipseAcronym.equals(""))
		{
			if (!Double.isInfinite(inv_f))
			{
				if(inv_f>0.0)
					strEllipse="+a="+a+" +rf="+inv_f+" ";
				else
					strEllipse="+R="+a+" ";
			}
			else
				strEllipse="+R="+a+" ";
		}
		else
		{
			strEllipse="+ellps="+strEllipseAcronym+" ";
		}
		strProj4+=strEllipse;
		//System.out.println("Elipsoide["+elipName+"]=("+a+","+inv_f+")");
		strProj4+=primeMeridianAcronym;
		if(!strProj4Datum.equals(""))
			strProj4+=strProj4Datum;
		if(!strProj4ToMeter.equals(""))
			strProj4+=strProj4ToMeter;
		String strWkt=crs.toWKT();
		
		//System.out.println("- Cadena proj4: "+strProj4);
		return strProj4;		
	}

	private String[] primeMeridianToProj4(String pmName,double pmValue) throws CrsException
	{
	    String[] primeMeridian=new String[3];
		String pszPM="";
		String acronym="";
	    double dfFromGreenwich = 0.0;
	    double tolerance=0.002/3600.0;
	    int    nPMCode = -1;

	    dfFromGreenwich=-(9+7/60.0+54.862/3600.0);
        if(pmName.equalsIgnoreCase("lisbon")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="lisbon";
            nPMCode = 8902;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(2+20/60.0+14.025/3600.0); 
        if(pmName.equalsIgnoreCase("paris")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="paris";
            nPMCode = 8903;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=-(74+4/60.0+51.3/3600.0);
        if(pmName.equalsIgnoreCase("bogota")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="bogota";
            nPMCode = 8904;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=-(3+41/60.0+16.58/3600.0);  // mal en ogr los segundos pone 16.48
        if(pmName.equalsIgnoreCase("madrid")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="madrid";
            nPMCode = 8905;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(12+27/60.0+8.4/3600.0);
        if(pmName.equalsIgnoreCase("rome")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="rome";
            nPMCode = 8906;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(7+26/60.0+22.5/3600.0);
        if(pmName.equalsIgnoreCase("bern")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="bern";
            nPMCode = 8907;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(106+48/60.0+27.79/3600.0);
        if(pmName.equalsIgnoreCase("jakarta")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="jakarta";
            nPMCode = 8908;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=-(17+40/60.0+0.0/3600.0);
        if(pmName.equalsIgnoreCase("ferro")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="ferro";
            nPMCode = 8909;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(4+22/60.0+4.71/3600.0);
        if(pmName.equalsIgnoreCase("brussels")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="brussels";
            nPMCode = 8910;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(18+3/60.0+29.8/3600.0);
        if(pmName.equalsIgnoreCase("stockholm")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="stockholm";
            nPMCode = 8911;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(23+42/60.0+58.815/3600.0);
        if(pmName.equalsIgnoreCase("athens")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="athens";
            nPMCode = 8912;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(10+43/60.0+22.5/3600.0);
        if(pmName.equalsIgnoreCase("oslo")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="oslo";
            nPMCode = 8913;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
            acronym="+pm="+pszPM+" ";
        }
        
        dfFromGreenwich=(0.0);
        if(pmName.equalsIgnoreCase("Greenwich")||(Math.abs(dfFromGreenwich-pmValue)<tolerance))
        {
            pszPM="Greenwich";
            nPMCode = 0;
            if(Math.abs(dfFromGreenwich-pmValue)>tolerance)
            {
				String strError="No concuerdan el nombre del meridiano origen y su valor";
				System.out.println(strError);
				//throw new CrsException(new Exception(strError));
            }
            pmValue=dfFromGreenwich;
        }
        primeMeridian[0]=pszPM;
        primeMeridian[1]=Double.toString(pmValue);
        primeMeridian[2]=acronym;
		return primeMeridian;
	}

	private String ellipseToProj4(double dfSemiMajor, double dfInvFlattening)
	{
		double yo=Math.abs(4.5);
		String pszPROJ4Ellipse="";
	    if( Math.abs(dfSemiMajor-6378249.145) < 0.01
	        && Math.abs(dfInvFlattening-293.465) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "clrk80";     /* Clark 1880 */
	    }
	    else if( Math.abs(dfSemiMajor-6378245.0) < 0.01
	             && Math.abs(dfInvFlattening-298.3) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "krass";      /* Krassovsky */
	    }
	    else if( Math.abs(dfSemiMajor-6378388.0) < 0.01
	             && Math.abs(dfInvFlattening-297.0) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "intl";       /* International 1924 */
	    }
	    else if( Math.abs(dfSemiMajor-6378160.0) < 0.01
	             && Math.abs(dfInvFlattening-298.25) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "aust_SA";    /* Australian */
	    }
	    else if( Math.abs(dfSemiMajor-6377397.155) < 0.01
	             && Math.abs(dfInvFlattening-299.1528128) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "bessel";     /* Bessel 1841 */
	    }
	    else if( Math.abs(dfSemiMajor-6377483.865) < 0.01
	             && Math.abs(dfInvFlattening-299.1528128) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "bess_nam";   /* Bessel 1841 (Namibia / Schwarzeck)*/
	    }
	    else if( Math.abs(dfSemiMajor-6378160.0) < 0.01
	             && Math.abs(dfInvFlattening-298.247167427) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "GRS67";      /* GRS 1967 */
	    }
	    else if( Math.abs(dfSemiMajor-6378137) < 0.01
	             && Math.abs(dfInvFlattening-298.257222101) < 0.000001 )
	    {
	        pszPROJ4Ellipse = "GRS80";      /* GRS 1980 */
	    }
	    else if( Math.abs(dfSemiMajor-6378206.4) < 0.01
	             && Math.abs(dfInvFlattening-294.9786982) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "clrk66";     /* Clarke 1866 */
	    }
	    else if( Math.abs(dfSemiMajor-6378206.4) < 0.01
	             && Math.abs(dfInvFlattening-294.9786982) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "mod_airy";   /* Modified Airy */
	    }
	    else if( Math.abs(dfSemiMajor-6377563.396) < 0.01
	             && Math.abs(dfInvFlattening-299.3249646) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "airy";       /* Modified Airy */
	    }
	    else if( Math.abs(dfSemiMajor-6378200) < 0.01
	             && Math.abs(dfInvFlattening-298.3) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "helmert";    /* Helmert 1906 */
	    }
	    else if( Math.abs(dfSemiMajor-6378155) < 0.01
	             && Math.abs(dfInvFlattening-298.3) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "fschr60m";   /* Modified Fischer 1960 */
	    }
	    else if( Math.abs(dfSemiMajor-6377298.556) < 0.01
	             && Math.abs(dfInvFlattening-300.8017) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "evrstSS";    /* Everest (Sabah & Sarawak) */
	    }
	    else if( Math.abs(dfSemiMajor-6378165.0) < 0.01
	             && Math.abs(dfInvFlattening-298.3) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "WGS60";      
	    }
	    else if( Math.abs(dfSemiMajor-6378145.0) < 0.01
	             && Math.abs(dfInvFlattening-298.25) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "WGS66";      
	    }
	    else if( Math.abs(dfSemiMajor-6378135.0) < 0.01
	             && Math.abs(dfInvFlattening-298.26) < 0.0001 )
	    {
	        pszPROJ4Ellipse = "WGS72";      
	    }
	    else if( Math.abs(dfSemiMajor-6378137.0) < 0.01
	             && Math.abs(dfInvFlattening-298.257223563) < 0.000001 )
	    {
	        pszPROJ4Ellipse = "WGS84";
	    }
	    /*
	    else if( EQUAL(pszDatum,"North_American_Datum_1927") )
	    {
//	        pszPROJ4Ellipse = "clrk66:+datum=nad27"; // NAD 27 
	        pszPROJ4Ellipse = "clrk66";
	    }
	    else if( EQUAL(pszDatum,"North_American_Datum_1983") )
	    {
//	        pszPROJ4Ellipse = "GRS80:+datum=nad83";       // NAD 83 
	        pszPROJ4Ellipse = "GRS80";
	    }
	    */
	    return pszPROJ4Ellipse;
	}

	private String datumToProj4(String datumName,int epsgCode)
	{
	    String datumProj4="";
	    String SRS_DN_NAD27="North_American_Datum_1927";
	    String SRS_DN_NAD83="North_American_Datum_1983";
	    String SRS_DN_WGS72="WGS_1972";
	    String SRS_DN_WGS84="WGS_1984";
	    if(datumName.equals(""))
	    	datumProj4="";
		else if(datumName.equalsIgnoreCase(SRS_DN_NAD27) || epsgCode == 6267 )
			datumProj4 = "+datum=NAD27 ";

	    else if(datumName.equalsIgnoreCase(SRS_DN_NAD83) || epsgCode == 6269 )
	    	datumProj4 = "+datum=NAD83 ";

	    else if(datumName.equalsIgnoreCase(SRS_DN_WGS84) || epsgCode == 6326 )
	    	datumProj4 = "+datum=WGS84 ";

	    else if( epsgCode == 6314 )
	    	datumProj4 = "+datum=potsdam ";

	    else if( epsgCode == 6272 )
	    	datumProj4 = "+datum=nzgd49 ";
		return datumProj4;
	}
	// Casos especiales
	// - MERCATOR_1SP
	// - HOTINE
	// - MILLER
	// - Polar_Stereographic
	// - Polar_Stereographic
	// - VanDerGrinten
	// - Transverse Mercator
	// - Cuando el meridiano origen no es 0 se pone pm=madrid y lon_0=
	// +datum
	
	// Funciones privadas necesarias para el proceso
	
	private String getName(Identifier name) {
		String[] correctName = name.toString().split(":");
		if (correctName.length<2) 
			return correctName[0];
		
		else
			return correctName[1];
	}
	
	private String[] Spheroid (Ellipsoid ellips) {
		String[] spheroid = new String[3];
		Unit u = ellips.getAxisUnit();
		double semi_major = convert( ellips.getSemiMajorAxis(), u.toString());
		//double inv_f = convert( ellips.getInverseFlattening(), u.toString());
		double inv_f = ellips.getInverseFlattening();
		String[] val =	ellips.getName().toString().split(":");
		if (val.length<2)
			spheroid[0] = ellips.getName().toString().split(":")[0];
		else
			spheroid[0] = ellips.getName().toString().split(":")[1];
		spheroid[1] = String.valueOf(semi_major);
		spheroid[2] = String.valueOf(inv_f);
		return spheroid;
	}
	
	private String[] Primem (PrimeMeridian prim) {
		String[] primem = new String[2];
		DefaultPrimeMeridian pm = (DefaultPrimeMeridian) prim;
		Unit u = pm.getAngularUnit();
		double value = convert( pm.getGreenwichLongitude(), u.toString());
		String[] val = pm.getName().toString().split(":");
		if (val.length<2)
			primem[0] = pm.getName().toString().split(":")[0];
		else
			primem[0] = pm.getName().toString().split(":")[1];
		primem[1] = String.valueOf(value);
		return primem;
	}
	
	public double convert(double value, String measure) throws ConversionException {
		if (measure.equals("D.MS")) {		
			value *= this.divider;
	        int deg,min;
	        deg = (int) (value/10000); value -= 10000*deg;
	        min = (int) (value/  100); value -=   100*min;
	        if (min<=-60 || min>=60) {  // Accepts NaN
	            if (Math.abs(Math.abs(min) - 100) <= EPS) {
	                if (min >= 0) deg++; else deg--;
	                min = 0;
	            } else {
	                throw new ConversionException("Invalid minutes: "+min);
	            }
	        }
	        if (value<=-60 || value>=60) { // Accepts NaN
	            if (Math.abs(Math.abs(value) - 100) <= EPS) {
	                if (value >= 0) min++; else min--;
	                value = 0;
	            } else {
	                throw new ConversionException("Invalid secondes: "+value);
	            }
	        }
	        value = ((value/60) + min)/60 + deg;
	        return value;
		}
		if (measure.equals("grad") || measure.equals("grade")) 
			return ((value * 180.0) / 200.0);			
		if (measure.equals(""+(char)176)) 
			return value;		
		if (measure.equals("DMS") ) 
			return value;		
		if (measure.equals("m") || measure.startsWith("[m")) 
			return value;	
		if (measure.equals("")) 
			return value;
		if (measure.equalsIgnoreCase("ft")||measure.equalsIgnoreCase("foot")||measure.equalsIgnoreCase("feet")) 
			return (value*0.3048/1.0);
		
		throw new ConversionException("Conversion no contemplada: "+measure);
    }
	
	public ArrayList getProjectionNameList(){
		return projectionNameList;
	}
}
