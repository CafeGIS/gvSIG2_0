/*
 * OrthographicMapProjection.java
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
 * Created on June 7, 2002, 4:31 PM
*/
package org.cresques.impl.geo;

import java.awt.geom.GeneralPath;

//package com.raben.projection.map;
import java.awt.geom.Point2D;


/***
 * Orthographic Map projection computation
 * @author  Vern Raben
 * @version $Revision$ $Date$
 * Copyright (c) Raben Systems, Inc., 2002
 * All rights reserved
 */
public final class Orthographic { //extends AbstractMapProjection { 

    /*** Creates new OrthographicMapProjection */
    public Orthographic() {
    }

    /***
     * Get Screen location for specified coordinate in radians
     * @param coordinate Point2D Longitude and latitude of coordinate in radians
     * @return Point2D Screen location of the coordinate
     */
    public Point2D getLocationForCoordinate(Point2D coordinate) {
        Point2D.Double loc = new Point2D.Double(Double.NaN, Double.NaN);

        /*                Point2D centerPoint = getCenterPoint();
                        Point2D centerCoordinate = getCenterCoordinate();
                        double radius = getRadius();
                        double cosLatCenter = getCosLatCenter();
                        double sinLatCenter = getSinLatCenter();

                        if ((!Double.isNaN(coordinate.getX()))
                                && (!Double.isNaN(coordinate.getY()))) {
                                double latitude = coordinate.getY();
                                double longitude = coordinate.getX();

                                double sinLat = Math.sin(normalizeLatitude(latitude));
                                double cosLat = Math.cos(latitude);
                                double lonDiff = normalizeLongitude(longitude
                                        - centerCoordinate.getX());
                                double cosLonDiff = Math.cos(lonDiff);
                                double cosC = (sinLatCenter * sinLat)
                                        + (cosLatCenter * cosLat * cosLonDiff);

                                if (cosC >= 0.0) {
                                        double sinLonDiff = Math.sin(lonDiff);
                                        double x = (radius * cosLat * sinLonDiff) + centerPoint.getX();
                                        double y = (radius * ((cosLatCenter * sinLat)
                                                - (sinLatCenter * cosLat * cosLonDiff)))
                                                + centerPoint.getY();
                                        loc.setLocation(x, y);
                                }
                        }
        */
        return loc;
    }

    /***
     * Get coordinate for a given point on the screen
     * @param loc Point2D Screen location of the point
     * @return Point2D Coordinate of the point in radians
     */
    public Point2D getCoordinateForLocation(Point2D loc) {
        Point2D.Double coordinate = new Point2D.Double(Double.NaN, Double.NaN);

        /*                Point2D centerPoint = getCenterPoint();
                        Point2D centerCoordinate = getCenterCoordinate();
                        double sinLatCenter = getSinLatCenter();
                        double cosLatCenter = getCosLatCenter();
                        double radius = getRadius();

                        if ((!Double.isNaN(loc.getX()))
                                && (!Double.isNaN(loc.getY()))) {
                                double x = loc.getX() - centerPoint.getX();
                                double y = loc.getY() - centerPoint.getY();
                                double rho = Math.sqrt((x * x) + (y * y));

                                if ((rho > 0.0) & (rho <= radius)) {
                                        double sinC = rho / radius;
                                        double cosC = Math.sqrt(1.0 - (sinC * sinC));
                                        double latitude = Math.asin(cosC * sinLatCenter)
                                                + (y * sinC * cosLatCenter / rho);
                                        double longitude = Double.NaN;

                                        if (centerCoordinate.getY()
                                                        == MapProjectionConstants.PI_OVER_2) {
                                                longitude = centerCoordinate.getX()
                                                        + Math.atan2(x, -y);
                                        } else if (centerCoordinate.getY()
                                                        == -MapProjectionConstants.PI_OVER_2) {
                                                longitude = centerCoordinate.getX() + Math.atan2(x, y);
                                        } else {
                                                longitude = centerCoordinate.getX()
                                                        + Math.atan2((x * sinC), (rho * cosLatCenter * cosC)
                                                        - (y * sinLatCenter * sinC));
                                        }

                                        longitude = normalizeLongitude(longitude);
                                        latitude = normalizeLatitude(latitude);
                                        coordinate.setLocation(longitude, latitude);
                                } else if (rho == 0.0) {
                                        coordinate.setLocation(centerCoordinate.getX(),
                                                centerCoordinate.getY());
                                }

                        }
        */
        return coordinate;
    }

    /***
     * Get overlay grid for map as a path
     * @return GeneralPath to draw mapOverlay.
     */
    public GeneralPath getOverlayGridPath() {
        GeneralPath overlayGridPath = new GeneralPath();

        /*                double sinLat = 0.0;
                        double cosLat = 0.0;
                        double cosLonDiff = 0.0;
                        double sinLonDiff = 0.0;
                        double lonDiff = 0.0;
                        double cosC = 0.0;
                        float x, y;
                        float mark = (float) getRadius() / 360.0F;
                        Point2D centerPoint = getCenterPoint();
                        Point2D centerCoordinate = getCenterCoordinate();
                        double radius = getRadius();
                        double cosLatCenter = getCosLatCenter();
                        double sinLatCenter = getSinLatCenter();
                        double overlayGridIncrement = getOverlayGridIncrement();
                        double overlayGridLongitudeIncrement
                                = getOverlayGridLongitudeIncrement();
                        double overlayGridLatitudeIncrement = getOverlayGridLatitudeIncrement();

                        // Create latitude lines
                        for (double lat = -MapProjectionConstants.PI_OVER_2;
                                        lat <= MapProjectionConstants.PI_OVER_2;
                                        lat += overlayGridLatitudeIncrement) {
                                sinLat = Math.sin(lat);
                                cosLat = Math.cos(lat);


                                for (double lon = -Math.PI; lon <= Math.PI;
                                                lon += overlayGridIncrement) {
                                        lonDiff = lon - centerCoordinate.getX();
                                        cosLonDiff = Math.cos(lonDiff);
                                        cosC = (sinLatCenter * sinLat)
                                                + (cosLatCenter * cosLat * cosLonDiff);

                                        if (cosC >= 0.0) {
                                                sinLonDiff = Math.sin(lonDiff);
                                                x = (float) ((radius * cosLat * sinLonDiff)
                                                        + centerPoint.getX());
                                                y = (float) ((radius * ((cosLatCenter * sinLat)
                                                        - (sinLatCenter * cosLat * cosLonDiff)))
                                                        + centerPoint.getY());
                                                overlayGridPath.moveTo(x - mark, y);
                                                overlayGridPath.lineTo(x + mark, y);
                                                overlayGridPath.moveTo(x, y - mark);
                                                overlayGridPath.lineTo(x, y + mark);
                                        }
                                }
                        }

                        // Create longitude lines
                        for (double lon = -Math.PI; lon <= Math.PI;
                                        lon += overlayGridLongitudeIncrement) {
                                lonDiff = lon - centerCoordinate.getX();
                                cosLonDiff = Math.cos(lonDiff);

                                for (double lat = -MapProjectionConstants.PI_OVER_2;
                                                lat <= MapProjectionConstants.PI_OVER_2;
                                                lat += overlayGridIncrement) {
                                        sinLat = Math.sin(lat);
                                        cosLat = Math.cos(lat);
                                        cosC = (sinLatCenter * sinLat)
                                                + (cosLatCenter * cosLat * cosLonDiff);

                                        if (cosC >= 0.0) {
                                                sinLonDiff = Math.sin(lonDiff);
                                                x = (float) ((radius * cosLat * sinLonDiff)
                                                        + centerPoint.getX());
                                                y = (float) ((radius * ((cosLatCenter * sinLat)
                                                        - (sinLatCenter * cosLat * cosLonDiff)))
                                                        + centerPoint.getY());
                                                overlayGridPath.moveTo(x - mark, y);
                                                overlayGridPath.lineTo(x + mark, y);
                                                overlayGridPath.moveTo(x, y - mark);
                                                overlayGridPath.lineTo(x, y + mark);
                                        }
                                }
                        }
        */
        return overlayGridPath;
    }

    /***
     * Get projection name
     * @return ProjectionName
     */

    //public ProjectionName getProjectionName() {
    //	return ProjectionName.ORTHOGRAPHIC;
    //}
}
