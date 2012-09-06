/*
 * EquidistantCylindricalMapProjection.java
 *
 * Copyright (c) 2002, 2003, Raben Systems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *    Neither the name of Raben Systems, Inc. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on July 2, 2002, 3:45 PM
 */
package org.cresques.impl.geo;


//package com.raben.projection.map;
//import java.awt.geom.Point2D;  
//import java.awt.geom.GeneralPath;

/***
 * Implementation ProjectionInterface for Equidistant Cylindrical map projection
 * @author  Vern Raben
 * @version $Revision$ $Date$
 */
public class EquidistantCylindrical { //extends AbstractMapProjection {

    /*47
    48      /*** Creates new CylindricalEquidistantMapProjection * /
    49      public EquidistantCylindricalMapProjection() {
    50          setCenterCoordinate(new Point2D.Double(Math.PI, 0.0));
    51      }
    52
    53      /***
    54       * Get coordinate for a given location in radians
    55       * @param loc Point2D Screen location
    56       * @return Point2D Coordinate of the point in radians
    57       * /
    58      public Point2D getCoordinateForLocation(Point2D loc) {
    59          Point2D.Double coordinate = new Point2D.Double(Double.NaN,
    60              Double.NaN);
    61          Point2D centerCoordinate = getCenterCoordinate();
    62          Point2D centerPoint = getCenterPoint();
    63          double radius = getRadius();
    64
    65          if (!Double.isNaN(loc.getX())) {
    66              double x = loc.getX() - centerPoint.getX();
    67              double y = loc.getY() - centerPoint.getY();
    68              double latitude = y / radius;
    69              double longitude = centerCoordinate.getX() + (x / radius);
    70              coordinate.setLocation(longitude, latitude);
    71              normalizeCoordinate(coordinate);
    72          }
    73
    74          return coordinate;
    75      }
    76
    77      /***
    78       * Get location for specified coordinate in radians
    79       * @param coordinate Point2D longitude and latitude of coordinate in radians
    80       * @return Point2D Screen location of the coordinate
    81       * /
    82      public Point2D getLocationForCoordinate(Point2D coordinate) {
    83          Point2D.Double location = new Point2D.Double(Double.NaN, Double.NaN);
    84          Point2D centerPoint = getCenterPoint();
    85          Point2D centerCoordinate = getCenterCoordinate();
    86          double radius = getRadius();
    87
    88          if (!Double.isNaN(coordinate.getX())) {
    89              double lonDiff = normalizeLongitude(coordinate.getX()
    90                  - centerCoordinate.getX());
    91              double x = centerPoint.getX() + (radius * lonDiff);
    92              double y = radius * coordinate.getY();
    93              location.setLocation(x, y);
    94          }
    95
    96          return location;
    97      }
    98
    99      /***
    100      * Get overlay grid for map
    101      * @return mapOverlay GeneneralPath
    102      * /
    103     public GeneralPath getOverlayGridPath() {
    104         GeneralPath overlayGridPath = new GeneralPath();
    105         double radius = getRadius();
    106         Point2D centerCoordinate = getCenterCoordinate();
    107         Point2D centerPoint = getCenterPoint();
    108         float mark = (float) (radius / 360.0);
    109         double latLim = getLatitudeRange() / 2.0;
    110         double overlayGridLatitudeIncrement = getOverlayGridLatitudeIncrement();
    111         double overlayGridIncrement = getOverlayGridIncrement();
    112         double overlayGridLongitudeIncrement
    113             = getOverlayGridLongitudeIncrement();
    114
    115         // Create latitude lines
    116         for (double lat = -latLim; lat <= latLim;
    117                 lat += overlayGridLatitudeIncrement) {
    118             float y = (float) (radius * lat);
    119
    120             for (double lon = -Math.PI; lon <= Math.PI;
    121                     lon += overlayGridIncrement) {
    122                 double lonDiff = normalizeLongitude(lon
    123                     - centerCoordinate.getX());
    124                 float x = (float) (centerPoint.getX() + (radius * lonDiff));
    125                 overlayGridPath.moveTo(x - mark, y);
    126                 overlayGridPath.lineTo(x + mark, y);
    127                 overlayGridPath.moveTo(x, y - mark);
    128                 overlayGridPath.lineTo(x, y + mark);
    129             }
    130         }
    131
    132         // Create longitude lines
    133         for (double lon = -Math.PI; lon <= Math.PI;
    134                 lon += overlayGridLongitudeIncrement) {
    135             double lonDiff = normalizeLongitude(lon - centerCoordinate.getX());
    136             float x = (float) (centerPoint.getX() + (radius * lonDiff));
    137
    138             for (double lat = -latLim; lat <= latLim;
    139                     lat += overlayGridIncrement) {
    140                 float y = (float) (radius * lat);
    141                 overlayGridPath.moveTo(x - mark, y);
    142                 overlayGridPath.lineTo(x + mark, y);
    143                 overlayGridPath.moveTo(x, y - mark);
    144                 overlayGridPath.lineTo(x, y + mark);
    145             }
    146         }
    147
    148         return overlayGridPath;
    149     }
    150
    151     /***
    152      * Get name of the projection
    153      * @return ProjectionName
    154      * /
    155     public ProjectionName getProjectionName() {
    156         return ProjectionName.EQUIDISTANT_CYLINDRICAL;
    157     }
    */
}
