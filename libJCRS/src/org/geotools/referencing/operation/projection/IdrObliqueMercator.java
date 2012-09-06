/*
 * Geotools - OpenSource mapping toolkit
 * (C) 2005, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here.
 */
/*
** libproj -- library of cartographic projections
** Some parts Copyright (c) 2003   Gerald I. Evenden
**
** Permission is hereby granted, free of charge, to any person obtaining
** a copy of this software and associated documentation files (the
** "Software"), to deal in the Software without restriction, including
** without limitation the rights to use, copy, modify, merge, publish,
** distribute, sublicense, and/or sell copies of the Software, and to
** permit persons to whom the Software is furnished to do so, subject to
** the following conditions:
**
** The above copyright notice and this permission notice shall be
** included in all copies or substantial portions of the Software.
**
** THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
** EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
** MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
** IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
** CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
** TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
** SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.geotools.referencing.operation.projection;

// J2SE dependencies and extensions
import java.awt.geom.Point2D;
import java.util.Collection;

import javax.units.NonSI;
import javax.units.SI;
import javax.units.Unit;

import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.IdrAzimuthalEquidistant.Provider;
import org.geotools.resources.cts.ResourceKeys;
import org.geotools.resources.cts.Resources;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


/**
 * Oblique Mercator Projection. A conformal, oblique, cylindrical projection 
 * with the cylinder touching the ellipsoid (or sphere) along a great circle 
 * path (the central line). The Mercator and Transverse Mercator projections 
 * can be thought of as special cases of the oblique mercator, where the central 
 * line is along the equator or a meridian, respectively. The Oblique Mercator 
 * projection has been used in Switzerland, Hungary, Madagascar, 
 * Malaysia, Borneo and the panhandle of Alaska.
 * <br><br>
 * 
 * The Oblique Mercator projection uses a (U,V) coordinate system, with the 
 * U axis along the central line. During the forward projection, coordinates 
 * from the ellipsoid are projected conformally to a sphere of constant total 
 * curvature, called the 'aposphere', before being projected onto the plane. 
 * The projection coordinates are further convented to a (X,Y) coordinate system 
 * by rotating the calculated (u,v) coordinates to give output (x,y) coordinates. 
 * The rotation value is usually the same as the projection azimuth (the angle, 
 * east of north, of the central line), but some cases allow a separate 
 * rotation parameter. 
 * <br><br>
 * 
 * There are two forms of the oblique mercator, differing in the origin of
 * their grid coordinates. The Hotine_Oblique_Mercator (EPSG code 9812) has grid 
 * coordinates start at the intersection of the central line and the equator of the 
 * aposphere. The Oblique_Mercator (EPSG code 9815) is the same, except the grid 
 * coordinates begin at the central point (where the latitude of center and 
 * central line intersect). ESRI separates these two case by appending
 * "Natural_Origin" (for the Hotine_Oblique_Mercator) and "Center" 
 * (for the Obique_Mercator) to the projection names.
 * <br><br>
 * 
 * Two different methods are used to specify the central line for the 
 * oblique mercator: 1) a central point and an azimuth, 
 * east of north, describing the central line and 
 * 2) two points on the central line. The EPSG does not use the two point method, 
 * while ESRI separates the two cases by putting "Azimuth" and "Two_Point" in 
 * their projection names. Both cases use the point where the "latitude_of_center" 
 * parameter crosses the central line as the projection's central point. 
 * The central meridian is not a projection parameter, and is instead calculated 
 * as the intersection between the central line and the equator of the aposphere. 
 * <br><br>
 *
 * For the azimuth method, the central latitude cannot be +- 90.0 degrees
 * and the central line cannot be at a maximum or minimum latitude at the central point.
 * In the two point method, the latitude of the first and second points cannot be
 * equal. Also, the latitude of the first point and central point cannot be
 * +- 90.0 degrees. Furthermore, the latitude of the first point cannot be 0.0 and 
 * the latitude of the second point cannot be - 90.0 degrees. A change of 
 * 10^-7 radians can allow calculation at these special cases. Snyder's restriction
 * of the central latitude being 0.0 has been removed, since the equaitons appear
 * to work correctly in this case.
 * <br><br>
 *
 * Azimuth values of 0.0 and +- 90.0 degrees are allowed (and used in Hungary
 * and Switzerland), though these cases would usually use a Mercator or 
 * Transverse Mercator projection instead. Azimuth values > 90 degrees cause
 * errors in the equations.
 * <br><br>
 * 
 * The oblique mercator is also called the "Rectified Skew Orthomorphic" (RSO). 
 * It appears is that the only difference from the oblique mercator is that
 * the RSO allows the rotation from the (U,V) to (X,Y) coordinate system to be different
 * from the azimuth. This separate parameter is called "rectified_grid_angle" (or 
 * "XY_Plane_Rotation" by ESRI) and is also included in the EPSG's parameters
 * for the Oblique Mercator and Hotine Oblique Mercator. 
 * The rotation parameter is optional in all the non-two point projections and will be
 * set to the azimuth if not specified.
 * <br><br>
 * 
 * Projection cases and aliases implemented by the {@link IdrObliqueMercator} are:
 * <ul>
 * <li>Oblique_Mercator (EPSG code 9815) - grid coordinates begin at the central point, has "rectified_grid_angle" parameter.</li>
 * <li>Hotine_Oblique_Mercator_Azimuth_Center (ESRI) - grid coordinates begin at the central point.</li>
 * <li>Rectified_Skew_Orthomorphic_Center (ESRI) - grid coordinates begin at the central point, has "rectified_grid_angle" parameter.</li>
 * 
 * <li>Hotine_Oblique_Mercator (EPSG code 9812) - grid coordinates begin at the interseciton of the central line and aposphere equator, has "rectified_grid_angle" parameter.</li>
 * <li>Hotine_Oblique_Mercator_Azimuth_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 * <li>Rectified_Skew_Orthomorphic_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator, has "rectified_grid_angle" parameter.</li>
 * 
 * <li>Hotine_Oblique_Mercator_Two_Point_Center (ESRI) - grid coordinates begin at the central point.</li>
 * <li>Hotine_Oblique_Mercator_Two_Point_Natural_Origin (ESRI) - grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 * </ul>
 * 
 * <strong>References:</strong>
 * <ul>
 *   <li><code>libproj4</code> is available at
 *       <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/">libproj4 Miscellanea</A><br>
 *        Relevent files are: <code>PJ_omerc.c</code>, <code>pj_tsfn.c</code>,
 *        <code>pj_fwd.c</code>, <code>pj_inv.c</code> and <code>lib_proj.h</code></li>
 *   <li> John P. Snyder (Map Projections - A Working Manual,
 *        U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li> "Coordinate Conversions and Transformations including Formulas",
 *        EPSG Guidence Note Number 7 part 2, Version 24.</li>
 *   <li>Gerald Evenden, 2004, <a href="http://members.verizon.net/~vze2hc4d/proj4/omerc.pdf">
 *         Documentation of revised Oblique Mercator</a></li>
 * </ul>
 * 
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Oblique Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/hotine_oblique_mercator.html">hotine_oblique_mercator on Remote Sensing</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/oblique_mercator.html">oblique_mercator on Remote Sensing</A>
 *
 * @version $Id: IdrObliqueMercator.java 18139 2008-01-16 16:21:08Z jlgomez $
 * @author  Rueben Schulz
 */
public class IdrObliqueMercator extends MapProjection {

	private final double scaleFactorLocal;
	private final double falseEastingLocal;
	private final double falseNorthingLocal;
	
	/**
     * Latitude of the projection centre. This is similar to the 
     * {@link #latitudeOfOrigin}, but the latitude of origin is the
     * Earth equator on aposphere for the oblique mercator. Needed
     * for WKT.
     */
    private final double latitudeOfCentre;
    
    /**
     * Longitude of the projection centre. This is <strong>NOT</strong> equal
     * to the {@link #centralMeridian}, which is the meridian where the
     * central line intersects the Earth equator on aposphere. Needed for
     * for non-two point WKT.
     */
    private final double longitudeOfCentre;
    
    /**
     * The azimuth of the central line passing throught the centre of the
     * projection, needed for for non-two point WKT. 
     */
    private double alpha_c; 
    
    /**
     * The rectified bearing of the central line, needed for non-two point WKT. Equals
     * {@link #alpha_c} if the "rectified_grid_angle" parameter value is not set.
     */
    private double rectGridAngle;
    
    /**
     * The latitude of the 1st point used to specify the central line, needed for two point
     * WKT. 
     */
    private final double latitudeOf1stPoint;
    
    /**
     * The longitude of the 1st point used to specify the central line, needed for two point
     * WKT. 
     */
    private final double longitudeOf1stPoint;
    
    /**
     * The latitude of the 2nd point used to specify the central line, needed for two point
     * WKT. 
     */
    private final double latitudeOf2ndPoint;
    
    /**
     * The longitude of the 2nd point used to specify the central line, needed for two point
     * WKT.  
     */
    private double longitudeOf2ndPoint;
    
    /**
     * Constants used in the transformation.
     */
    private double B, A, E;  
    
    /**
     * Convenience values equal to {@link #A} / {@link #B}, 
     * {@link #A}&times;{@link #B}, and {@link #B} / {@link #A}.
     */
    private final double ArB, AB, BrA;
    
    /**
     * v values when the input latitude is a pole.
     */
    private final double v_pole_n, v_pole_s;
    
    /**
     * Sine and Cosine values for gamma0 (the angle between the meridian
     * and central line at the intersection between the central line and 
     * the Earth equator on aposphere).
     */
    private final double singamma0, cosgamma0;
    
    /**
     * Sine and Cosine values for the rotation between (U,V) and 
     * (X,Y) coordinate systems
     */
    private final double sinrot, cosrot;
    
    /**
     * u value (in (U,V) coordinate system) of the central point. Used in the 
     * oblique mercater case. The v value of the central point is 0.0.
     */
    private double u_c;
    
    /**
     * <code>true</code> if using two points on the central line to specify 
     * the azimuth.
     */
    private final boolean twoPoint;
    
    /**
     * <code>true</code> for hotine oblique mercator, or <code>false</code> 
     * for the oblique mercator case. 
     */
    private final boolean hotine;
    
    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for an {@link IdrObliqueMercator} projection.
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrObliqueMercator.java 18139 2008-01-16 16:21:08Z jlgomez $
     * @author Rueben Schulz
     */
    public static class Provider extends AbstractProvider {

        public static final ParameterDescriptor SCALE_FACTOR_LOCAL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "scale_factor"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Scale factor on initial line"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Scale factor at natural origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "ScaleAtNatOrigin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "ScaleAtCenter")
                },
                1, 0, Double.POSITIVE_INFINITY, Unit.ONE);
	
        public static final ParameterDescriptor FALSE_EASTING_LOCAL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "false_easting"),
                    new NamedIdentifier(CitationImpl.EPSG,    "False easting"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Easting at projection centre"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Easting at false origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "FalseEasting")
                },
                0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);
    	
        public static final ParameterDescriptor FALSE_NORTHING_LOCAL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,     "false_northing"),
                    new NamedIdentifier(CitationImpl.EPSG,    "False northing"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Northing at projection centre"),
                    new NamedIdentifier(CitationImpl.EPSG,    "Northing at false origin"),
                    new NamedIdentifier(CitationImpl.GEOTIFF, "FalseNorthing")
                },
                0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, SI.METER);
    	
    	
    	/**
         * The operation parameter descriptor for the {@link #latitudeOfCentre}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "latitude_of_center"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.ESRI,     "Latitude_Of_Center"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "CenterLat")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);
        
        /**
         * The operation parameter descriptor for the {@link #longitudeOfCentre}
         * parameter value. Valid values range is from -180 to 180. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "longitude_of_center"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Longitude of projection centre"),
                    new NamedIdentifier(CitationImpl.ESRI,     "Longitude_Of_Center"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "CenterLong")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);
                
        /**
         * The operation parameter descriptor for the {@link #alpha_c}
         * parameter value. Valid values range is from -360 to -270, -90 to 90, 
         * and 270 to 360 degrees. Default value is 0.
         */
        public static final ParameterDescriptor AZIMUTH = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "azimuth"),
                    new NamedIdentifier(CitationImpl.ESRI,     "Azimuth"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Azimuth of initial line"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "AzimuthAngle")
                },
                0, -360, 360, NonSI.DEGREE_ANGLE);
                
        /**
         * The operation parameter descriptor for the {@link #rectGridAngle}
         * parameter value. It is an optional parameter with valid values ranging
         * from -360 to 360. Default value is {@link #alpha_c}.
         */
        public static final ParameterDescriptor RECTIFIED_GRID_ANGLE = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "rectified_grid_angle"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Angle from Rectified to Skew Grid"),
                    new NamedIdentifier(CitationImpl.ESRI,     "XY_Plane_Rotation"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "RectifiedGridAngle")
                },
                -360, 360, NonSI.DEGREE_ANGLE);
                
        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Oblique_Mercator"),
                new NamedIdentifier(CitationImpl.EPSG,     "Oblique Mercator"),
                new NamedIdentifier(CitationImpl.EPSG,     "9815"),
                new NamedIdentifier(CitationImpl.GEOTIFF,  "CT_ObliqueMercator"),
                new NamedIdentifier(CitationImpl.ESRI,     "Hotine_Oblique_Mercator_Azimuth_Center"),
                new NamedIdentifier(CitationImpl.ESRI,     "Rectified_Skew_Orthomorphic_Center"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.OBLIQUE_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LONG_OF_CENTRE,      LAT_OF_CENTRE,
                AZIMUTH,             RECTIFIED_GRID_ANGLE,
                SCALE_FACTOR_LOCAL,
                FALSE_EASTING_LOCAL,       FALSE_NORTHING_LOCAL
            });

        /**
         * Constructs a new provider. 
         */
        public Provider() {
            super(PARAMETERS);
        }
        
        /**
         * Constructs a new provider. 
         */
        protected Provider(final ParameterDescriptorGroup params) {
            super(params);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            return new IdrObliqueMercator(parameters, descriptors, false, false);
        }
    }
    
    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for a Hotine {@link IdrObliqueMercator} projection.
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrObliqueMercator.java 18139 2008-01-16 16:21:08Z jlgomez $
     * @author Rueben Schulz
     */
    public static final class Provider_Hotine extends Provider {
        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.OGC,      "Hotine_Oblique_Mercator"),
                new NamedIdentifier(CitationImpl.EPSG,     "Hotine Oblique Mercator"),
                new NamedIdentifier(CitationImpl.EPSG,     "9812"),
                new NamedIdentifier(CitationImpl.GEOTIFF,  "CT_ObliqueMercator_Hotine"),
                new NamedIdentifier(CitationImpl.ESRI,     "Hotine_Oblique_Mercator_Azimuth_Natural_Origin"),
                new NamedIdentifier(CitationImpl.ESRI,     "Rectified_Skew_Orthomorphic_Natural_Origin"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.OBLIQUE_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LONG_OF_CENTRE,      LAT_OF_CENTRE,
                AZIMUTH,             RECTIFIED_GRID_ANGLE,
                SCALE_FACTOR_LOCAL,
                FALSE_EASTING_LOCAL,       FALSE_NORTHING_LOCAL
        });
        
        /**
         * Constructs a new provider. 
         */
        public Provider_Hotine() {
            super(PARAMETERS);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            return new IdrObliqueMercator(parameters, descriptors, true, false);
        }
    }
   
    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for a {@link IdrObliqueMercator} projection, specified with
     * two points on the central line (instead of a central point and azimuth).
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrObliqueMercator.java 18139 2008-01-16 16:21:08Z jlgomez $
     * @author Rueben Schulz
     */
    public static class Provider_TwoPoint extends Provider {
    		/**
         * The operation parameter descriptor for the {@link #latitudeOfCentre}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.OGC,      "latitude_of_center"),
                    new NamedIdentifier(CitationImpl.EPSG,     "Latitude of projection centre"),
                    new NamedIdentifier(CitationImpl.ESRI,     "Latitude_Of_Center"),
                    new NamedIdentifier(CitationImpl.GEOTIFF,  "CenterLat")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);
        
    	    /**
         * The operation parameter descriptor for the {@link #latitudeOf1stPoint}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_1ST_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.ESRI, "Latitude_Of_1st_Point")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);
        
        /**
         * The operation parameter descriptor for the {@link #longitudeOf1stPoint}
         * parameter value. Valid values range is from -180 to 180. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_1ST_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.ESRI, "Longitude_Of_1st_Point")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);
        
        /**
         * The operation parameter descriptor for the {@link #latitudeOf2ndPoint}
         * parameter value. Valid values range is from -90 to 90. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_2ND_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.ESRI, "Latitude_Of_2nd_Point")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);
        
        /**
         * The operation parameter descriptor for the {@link #longitudeOf2ndPoint}
         * parameter value. Valid values range is from -180 to 180. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_2ND_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(CitationImpl.ESRI, "Longitude_Of_2nd_Point")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);
        
        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.ESRI,     "Hotine_Oblique_Mercator_Two_Point_Center"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.OBLIQUE_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LAT_OF_1ST_POINT,    LONG_OF_1ST_POINT,
                LAT_OF_2ND_POINT,    LONG_OF_2ND_POINT,
			   LAT_OF_CENTRE,       SCALE_FACTOR_LOCAL,
                FALSE_EASTING_LOCAL,       FALSE_NORTHING_LOCAL
            });

        /**
         * Constructs a new provider. 
         */
        public Provider_TwoPoint() {
            super(PARAMETERS);
        }
        
        /**
         * Constructs a new provider. 
         */
        protected Provider_TwoPoint(final ParameterDescriptorGroup params) {
            super(params);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            return new IdrObliqueMercator(parameters, descriptors, false, true);
        }
        
    }


    /**
     * The {@link org.geotools.referencing.operation.MathTransformProvider}
     * for a Hotine {@link IdrObliqueMercator} projection, specified with
     * two points on the central line (instead of a central point and azimuth).
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     *
     * @version $Id: IdrObliqueMercator.java 18139 2008-01-16 16:21:08Z jlgomez $
     * @author Rueben Schulz
     */
    public static final class Provider_Hotine_TwoPoint extends Provider_TwoPoint {
    	/**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(CitationImpl.ESRI,     "Hotine_Oblique_Mercator_Two_Point_Natural_Origin"),
                new NamedIdentifier(CitationImpl.GEOTOOLS, Resources.formatInternational(
                                                           ResourceKeys.OBLIQUE_MERCATOR_PROJECTION)),
                new NamedIdentifier(new CitationImpl("IDR"), "IDR")
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LAT_OF_1ST_POINT,    LONG_OF_1ST_POINT,
                LAT_OF_2ND_POINT,    LONG_OF_2ND_POINT,
                	LAT_OF_CENTRE,       SCALE_FACTOR_LOCAL,
                FALSE_EASTING_LOCAL,       FALSE_NORTHING_LOCAL
            });

        /**
         * Constructs a new provider. 
         */
        public Provider_Hotine_TwoPoint() {
            super(PARAMETERS);
        }

        /**
         * Returns the operation type for this map projection.
         */
        protected Class getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection descriptors = PARAMETERS.descriptors();
            return new IdrObliqueMercator(parameters, descriptors, true, true);
        }

    }
    
    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @param  expected The expected parameter descriptors.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    IdrObliqueMercator(final ParameterValueGroup parameters, final Collection expected,
                    final boolean hotine, final boolean twoPoint) 
            throws ParameterNotFoundException 
    {
        
    	//Fetch parameters 
        super(parameters, expected);
        
        this.hotine = hotine;
        this.twoPoint = twoPoint;
        
        //NaN for safety (centralMeridian calculated below)
        latitudeOfOrigin = Double.NaN;
        centralMeridian  = Double.NaN;

        //scaleFactorLocal=1.0;
        //falseEastingLocal=0.0;
        //falseNorthingLocal=0.0;
        
        final Collection miExpected = getParameterDescriptors().descriptors();
        if (miExpected.contains(Provider.SCALE_FACTOR_LOCAL)) {
        	scaleFactorLocal = Math.abs(doubleValue(expected,
                                        Provider.SCALE_FACTOR_LOCAL, parameters));
            //ensureInRange(Provider.SCALE_FACTOR_LOCAL, scaleFactorLocal, false);
        } else {
        	scaleFactorLocal = 1.0;
        }
        if (miExpected.contains(Provider.FALSE_EASTING_LOCAL)) {
        	falseEastingLocal = Math.abs(doubleValue(expected,
                                        Provider.FALSE_EASTING_LOCAL, parameters));
            //ensureLatitudeInRange(Provider.FALSE_EASTING_LOCAL, falseEastingLocal, false);
        } else {
        	falseEastingLocal = 0.0;
        }
        if (miExpected.contains(Provider.FALSE_NORTHING_LOCAL)) {
        	falseNorthingLocal = Math.abs(doubleValue(expected,
                                        Provider.FALSE_NORTHING_LOCAL, parameters));
            //ensureLatitudeInRange(Provider.FALSE_NORTHING_LOCAL, falseNorthingLocal, false);
        } else {
        	falseNorthingLocal = 0.0;
        }

        latitudeOfCentre = doubleValue(expected, Provider.LAT_OF_CENTRE, parameters);
        //checks that latitudeOfCentre is not +- 90 degrees
        //not checking if latitudeOfCentere is 0, since equations behave correctly
        ensureLatitudeInRange(Provider.LAT_OF_CENTRE, latitudeOfCentre, false);
        
        if (twoPoint) {
            longitudeOfCentre  = Double.NaN;
            latitudeOf1stPoint = doubleValue(expected, Provider_TwoPoint.LAT_OF_1ST_POINT, parameters);
            //checks that latOf1stPoint is not +-90 degrees
            ensureLatitudeInRange(Provider_TwoPoint.LAT_OF_1ST_POINT, latitudeOf1stPoint, false);
            longitudeOf1stPoint = doubleValue(expected, Provider_TwoPoint.LONG_OF_1ST_POINT, parameters);
            ensureLongitudeInRange(Provider_TwoPoint.LONG_OF_1ST_POINT, longitudeOf1stPoint, true);
            latitudeOf2ndPoint = doubleValue(expected, Provider_TwoPoint.LAT_OF_2ND_POINT, parameters);
            ensureLatitudeInRange(Provider_TwoPoint.LAT_OF_2ND_POINT, latitudeOf2ndPoint, true);
            longitudeOf2ndPoint = doubleValue(expected, Provider_TwoPoint.LONG_OF_2ND_POINT, parameters);
            ensureLongitudeInRange(Provider_TwoPoint.LONG_OF_2ND_POINT, longitudeOf2ndPoint, true);
            
            /*
            double con = Math.abs(latitudeOf1stPoint);
            if (Math.abs(latitudeOf1stPoint - latitudeOf2ndPoint) < TOL) {
                throw new IllegalArgumentException(Resources.format(ResourceKeys.ERROR_LAT1_EQ_LAT2));
            }
            if (Math.abs(latitudeOf1stPoint) < TOL) {
                throw new IllegalArgumentException(Resources.format(ResourceKeys.ERROR_LAT1_EQ_ZERO));
            }
            if (Math.abs(latitudeOf2ndPoint + Math.PI/2.0) < TOL) {
                throw new IllegalArgumentException(Resources.format(ResourceKeys.ERROR_LAT2_EQ_NEG_90));
            }
            */
        } else {
	    latitudeOf1stPoint  = Double.NaN;
            longitudeOf1stPoint = Double.NaN;
            latitudeOf2ndPoint  = Double.NaN;
            longitudeOf2ndPoint = Double.NaN;
                       
            longitudeOfCentre = doubleValue(expected, Provider.LONG_OF_CENTRE, parameters);
            ensureLongitudeInRange(Provider.LONG_OF_CENTRE, longitudeOfCentre, true);
            
            alpha_c = doubleValue(expected, Provider.AZIMUTH, parameters);
            //already checked for +-360 deg. above. 
            if ((alpha_c > -1.5*Math.PI && alpha_c < -0.5*Math.PI) ||
                (alpha_c > 0.5*Math.PI && alpha_c < 1.5*Math.PI)) {
                    throw new IllegalArgumentException(
                        Resources.format(ResourceKeys.ERROR_VALUE_OUT_OF_BOUNDS_$3,
                        new Double(Math.toDegrees(alpha_c)), new Double(-90), new Double(90)));
            }
            
            rectGridAngle = doubleValue(expected, Provider.RECTIFIED_GRID_ANGLE, parameters);
            if (Double.isNaN(rectGridAngle)) {
                rectGridAngle = alpha_c;
            }
        }   
        /*
        double com = Math.sqrt(1.0-excentricitySquared);
        double sinphi0 = Math.sin(latitudeOfCentre);
        double cosphi0 = Math.cos(latitudeOfCentre);
        B = cosphi0 * cosphi0;
        B = Math.sqrt(1.0 + excentricitySquared * B * B / (1.0-excentricitySquared));
        double con = 1.0 - excentricitySquared * sinphi0 * sinphi0;
        A = B * com / con;
        double D = B * com / (cosphi0 * Math.sqrt(con));
        double F = D * D - 1.0;
        if (F < 0.0) {
            F = 0.0;
        } else {
            F = Math.sqrt(F);
            if (latitudeOfCentre < 0.0) {  //taking sign of latOfCentre
                F = -F;
            }
        }
        F = F += D;
        E = F* Math.pow(tsfn(latitudeOfCentre, sinphi0), B);          
        double gamma0;
        if (twoPoint) {
            double H = Math.pow(tsfn(latitudeOf1stPoint, Math.sin(latitudeOf1stPoint)), B);
            double L = Math.pow(tsfn(latitudeOf2ndPoint, Math.sin(latitudeOf2ndPoint)), B);
            double Fp = E / H;
            double P = (L - H) / (L + H);
            double J = E * E;
            J = (J - L * H) / (J + L * H);        
            double diff = longitudeOf1stPoint - longitudeOf2ndPoint;
            if (diff < -Math.PI) {
                longitudeOf2ndPoint -= 2.0* Math.PI;
            } else if (diff > Math.PI) {
                longitudeOf2ndPoint += 2.0* Math.PI;
            }
            
            centralMeridian = rollLongitude(0.5 * (longitudeOf1stPoint + longitudeOf2ndPoint) -
                              Math.atan(J * Math.tan(0.5 * B * (longitudeOf1stPoint - longitudeOf2ndPoint)) / P) / B);
            gamma0 = Math.atan(2.0 * Math.sin(B * rollLongitude(longitudeOf1stPoint - centralMeridian)) /
                     (Fp - 1.0 / Fp));
            alpha_c = Math.asin(D * Math.sin(gamma0));
            rectGridAngle = alpha_c;
        } else {
            gamma0 = Math.asin(Math.sin(alpha_c) / D);
            //check for asin(+-1.00000001)
            double temp = 0.5 * (F - 1.0 / F) * Math.tan(gamma0);
            if (Math.abs(temp) > 1.0) {
                if (Math.abs(Math.abs(temp) - 1.0) > EPS) {
                    throw new IllegalArgumentException("Tolerance condition error");
                }
                temp = (temp > 0) ? 1.0 : -1.0;
            }
            centralMeridian = longitudeOfCentre - Math.asin(temp) / B; 
        }
        singamma0 = Math.sin(gamma0);
        cosgamma0 = Math.cos(gamma0);
		sinrot = Math.sin(rectGridAngle);
		cosrot = Math.cos(rectGridAngle);
        ArB = A/B;
        AB = A*B;
        BrA = B/A;
        v_pole_n = ArB * Math.log(Math.tan(0.5 * (Math.PI/2.0 - gamma0)));
        v_pole_s = ArB * Math.log(Math.tan(0.5 * (Math.PI/2.0 + gamma0)));
        if (hotine) {
            u_c = 0.0;
        } else {
            if (Math.abs(Math.abs(alpha_c) - Math.PI/2.0) < TOL) {
                //longitudeOfCentre = NaN in twopoint, but alpha_c cannot be 90 here (lat1 != lat2)
                u_c = A * (longitudeOfCentre - centralMeridian);
            } else {
                u_c = Math.abs(ArB * Math.atan2(Math.sqrt(D * D - 1.0), Math.cos(alpha_c)));
                if (latitudeOfCentre < 0.0) {
                    u_c = -u_c;
                }
            }
        }
        */
        singamma0 = 0;
        cosgamma0 = 0;
		sinrot = 0;
		cosrot = 0;
        ArB = 0;
        AB = 0;
        BrA = 0;
        v_pole_n =0;
        v_pole_s =0;
   }
    
    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        if (hotine) {
            return (twoPoint) ? Provider_Hotine_TwoPoint.PARAMETERS : Provider_Hotine.PARAMETERS;
        } else {
            return (twoPoint) ? Provider_TwoPoint.PARAMETERS : Provider.PARAMETERS;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection expected = getParameterDescriptors().descriptors();
        if (twoPoint) {
            set(expected, Provider_TwoPoint.LAT_OF_CENTRE, values, latitudeOfCentre);
            set(expected, Provider_TwoPoint.LAT_OF_1ST_POINT, values, latitudeOf1stPoint);
            set(expected, Provider_TwoPoint.LONG_OF_1ST_POINT, values, longitudeOf1stPoint);
            set(expected, Provider_TwoPoint.LAT_OF_2ND_POINT, values, latitudeOf2ndPoint);
            set(expected, Provider_TwoPoint.LONG_OF_2ND_POINT, values, longitudeOf2ndPoint);
        } else {
            set(expected, Provider.LAT_OF_CENTRE, values, latitudeOfCentre);
            set(expected, Provider.LONG_OF_CENTRE, values, longitudeOfCentre);
            set(expected, Provider.AZIMUTH, values, alpha_c );
            set(expected, Provider.RECTIFIED_GRID_ANGLE, values, rectGridAngle);
        }
        set(expected, Provider.SCALE_FACTOR_LOCAL, values, scaleFactorLocal);
        set(expected, Provider.FALSE_NORTHING_LOCAL, values, falseNorthingLocal);
        set(expected, Provider.FALSE_EASTING_LOCAL, values, falseEastingLocal);
        return values;
    }

    /**
     * {@inheritDoc}
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException 
    {
        double u, v;
        if (Math.abs(Math.abs(y) - Math.PI/2.0) > EPS) {
            double Q = E / Math.pow(tsfn(y, Math.sin(y)), B);
            double temp = 1.0 / Q;
            double S = 0.5 * (Q - temp);
            double V = Math.sin(B * x);
            double U = (S * singamma0 - V * cosgamma0) / (0.5 * (Q + temp));
            if (Math.abs(Math.abs(U) - 1.0) < EPS) {
                throw new ProjectionException(Resources.format(ResourceKeys.ERROR_V_INFINITE));
            }
            v = 0.5 * ArB * Math.log((1.0 - U) / (1.0 + U));
            temp = Math.cos(B * x);
            if (Math.abs(temp) < TOL) {
                u = AB * x;
            } else {
                u = ArB * Math.atan2((S * cosgamma0 + V * singamma0), temp);
            }   
        } else {
            v = y > 0 ? v_pole_n : v_pole_s;
	    u = ArB * y;
        }
        
        u -= u_c;
		x = v * cosrot + u * sinrot;
		y = u * cosrot - v * sinrot;

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }
    
    /**
     * {@inheritDoc}
     */
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) 
            throws ProjectionException 
    {
	double v = x * cosrot - y * sinrot;
        double u = y * cosrot + x * sinrot + u_c;
        
        double Qp = Math.exp(-BrA * v);
        double temp = 1.0 / Qp;
        double Sp = 0.5 * (Qp - temp);
        double Vp = Math.sin(BrA * u);
        double Up = (Vp * cosgamma0 + Sp * singamma0) / (0.5 * (Qp + temp));
        if (Math.abs(Math.abs(Up) - 1.0) < EPS) {
            x = 0.0;
            y = Up < 0.0 ? -Math.PI / 2.0 : Math.PI / 2.0;
        } else {
            y = Math.pow(E / Math.sqrt((1. + Up) / (1. - Up)), 1.0 / B);  //calculate t
            y = cphi2(y);
            x = -Math.atan2((Sp * cosgamma0 - Vp * singamma0), Math.cos(BrA * u)) / B;
        }

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }    

    /**
     * Maximal error (in metres) tolerated for assertion, if enabled.
     *
     * @param  longitude The longitude in degrees.
     * @param  latitude The latitude in degrees.
     * @return The tolerance level for assertions, in meters.
     */
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        if (Math.abs(longitude - centralMeridian)/2 +
            Math.abs(latitude  - latitudeOfCentre) > 10)
        {
            // When far from the valid area, use a larger tolerance.
            return 1;
        }
        return super.getToleranceForAssertions(longitude, latitude);
    }
    
    /**
     * Returns a hash value for this projection.
     */
    public int hashCode() {
        long code =      Double.doubleToLongBits(latitudeOfCentre);
        code = code*37 + Double.doubleToLongBits(longitudeOfCentre);
        code = code*37 + Double.doubleToLongBits(alpha_c);
        code = code*37 + Double.doubleToLongBits(rectGridAngle);
        code = code*37 + Double.doubleToLongBits(latitudeOf1stPoint);
        code = code*37 + Double.doubleToLongBits(latitudeOf2ndPoint);
        return ((int)code ^ (int)(code >>> 32)) + 37*super.hashCode();
    }

    /**
     * Compares the specified object with this map projection for equality.
     */
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final IdrObliqueMercator that = (IdrObliqueMercator) object;
            return equals(this.latitudeOfCentre   , that.latitudeOfCentre   ) &&
                   equals(this.longitudeOfCentre  , that.longitudeOfCentre  ) &&
                   equals(this.alpha_c            , that.alpha_c            ) &&
                   equals(this.rectGridAngle      , that.rectGridAngle      ) &&
                   equals(this.u_c                , that.u_c                ) &&
		   equals(this.latitudeOf1stPoint , that.latitudeOf1stPoint ) &&
	           equals(this.longitudeOf1stPoint, that.longitudeOf1stPoint) &&
		   equals(this.latitudeOf2ndPoint , that.latitudeOf2ndPoint ) &&
		   equals(this.longitudeOf2ndPoint, that.longitudeOf2ndPoint) &&
                   this.twoPoint == that.twoPoint &&
                   this.hotine   == that.hotine;
        }
        return false;
    }
}
