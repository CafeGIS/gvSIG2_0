/*
 * Cresques Mapping Suite. Graphic Library for constructing mapping applications.
 *
 * Copyright (C) 2004-5.
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
 * cresques@gmail.com
 */
package org.cresques.px.dxf;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Point3D;
import org.cresques.geo.Projected;
import org.cresques.geo.ViewPortData;
import org.cresques.io.DxfFile;
import org.cresques.io.DxfGroup;
import org.cresques.io.DxfGroupVector;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;
import org.cresques.px.gml.Feature;
import org.cresques.px.gml.FeatureCollection;
import org.cresques.px.gml.InsPoint3D;
import org.cresques.px.gml.LineString;
import org.cresques.px.gml.LineString3D;
import org.cresques.px.gml.Polygon;
import org.cresques.px.gml.Polygon3D;


/**
 * La clase DxfFeatureMaker facilita la creación de entidades en un modelo de datos
 * GIS. La creación se realiza partiendo de las entidades obtenidas de un fichero DXF.
 * @author jmorell
 */
public class DxfFeatureMaker implements DxfFile.EntityFactory, Projected {
    IProjection proj = null;

    //Feature lastFeature = null;
    Feature lastFeaBordes = null;
    Feature lastFeaFondos = null;
    boolean isDoubleFeatured = false;
    FeatureCollection features = null;
    double bulge = 0.0;
    double xtruX = 0.0;
    double xtruY = 0.0;
    double xtruZ = 1.0;
    int polylineFlag = 0;
    Point3D firstPt = new Point3D();
    Point3D ptAnterior = null;
    boolean addingToBlock = false;
    int iterator = 0;
    FeatureCollection blk = null;
    Vector blkList = null;
    DxfTable layers = null;
    private Vector faces = null;
    private boolean hasFaces = false;
    private int facesIterador = 1;
    private Point2D facesFirstPoint = null;
    private Vector attributes = null;
    private boolean constantPolylineElevation;
    private double lastVertexElevation;
    private boolean dxf3DFile;

    /**
     * Constructor de DxfFeatureMaker.
     * @param proj, proyección cartográfica en la que se encontrarán las entidades
     * que creemos.
     */
    public DxfFeatureMaker(IProjection proj) {
        this.proj = proj;
        layers = new DxfTable();
        features = new FeatureCollection(proj);
        blkList = new Vector();
        attributes = new Vector();
        dxf3DFile = false;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#setAddingToBlock(boolean)
     */
    public void setAddingToBlock(boolean a) {
        addingToBlock = a;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createLayer(org.cresques.io.DxfGroupVector)
     */
    public void createLayer(DxfGroupVector v) throws Exception {
        int color = v.getDataAsInt(62);
        DxfLayer layer = new DxfLayer(v.getDataAsString(2),
                                      Math.abs(v.getDataAsInt(62)));

        if (color < 0) {
            layer.isOff = true;
        }

        layer.lType = v.getDataAsString(6);
        layer.setFlags(v.getDataAsInt(70));

        // compruebo flags
        if ((layer.flags & 0x01) == 0x01) {
            layer.frozen = true;
        }

        if ((layer.flags & 0x02) == 0x02) {
            layer.frozen = true;
        }

        System.out.println("LAYER color=" + layer.getColor());

        layers.add(layer);
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createPolyline(org.cresques.io.DxfGroupVector)
     */
    public void createPolyline(DxfGroupVector grp) throws Exception {
        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();

        //Feature feature= new Feature();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        int flags = 0;
        constantPolylineElevation = true;

        // 041122: Cada polyline tiene asociado un objeto faces distinto.
        faces = new Vector();

        //feature.setProp("dxfEntity", "Polyline");
        feaBordes.setProp("dxfEntity", "Polyline");
        feaFondos.setProp("dxfEntity", "Polyline");

        if (grp.hasCode(8)) {
            //feature.setProp("layer", grp.getDataAsString(8));
            feaBordes.setProp("layer", grp.getDataAsString(8));
            feaFondos.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();

            //feature.setProp("thickness", string);
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);

            //feature.setProp("thickness", doub.toString());
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(10)) {
            x = grp.getDataAsDouble(10);
        }

        if (grp.hasCode(20)) {
            y = grp.getDataAsDouble(20);
        }

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);

            if (z != 0.0) {
                dxf3DFile = true;
            }

            Double doub = new Double(z);
            String string = doub.toString();

            //feature.setProp("elevation", string);
            feaBordes.setProp("elevation", string);
            feaFondos.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);

            //feature.setProp("elevation", doub.toString());
            feaBordes.setProp("elevation", doub.toString());
            feaFondos.setProp("elevation", doub.toString());
        }

        if (grp.hasCode(70)) {
            flags = grp.getDataAsInt(70);
        }

        if (grp.hasCode(210)) {
            xtruX = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            xtruY = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            xtruZ = grp.getDataAsDouble(230);
        }

        if (((flags & 0x01) == 0x01) || ((flags & 0x40) == 0x40)) {
            feaBordes.setGeometry(lineString3D);
            feaFondos.setGeometry(polygon3D);
            lastFeaBordes = feaBordes;
            lastFeaFondos = feaFondos;
            isDoubleFeatured = true;
        } else if ((flags & 0x01) == 0x00) {
            feaBordes.setGeometry(lineString3D);
            lastFeaBordes = feaBordes;
            isDoubleFeatured = false;
        } else {
            System.out.println("Detectada una Polyline Flag que no corresponde");
            System.out.println("a una Polyline corriente, ni a una Closed Polyline");
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#endSeq(org.cresques.io.DxfGroupVector)
     */
    public void endSeq() throws Exception {
        if (isDoubleFeatured) {
            if (lastFeaBordes.getGeometry() instanceof LineString3D) {
                Feature feaBordes = lastFeaBordes;
                Feature feaFondos = lastFeaFondos;
                LineString3D lineString3D = (LineString3D) feaBordes.getGeometry();
                Polygon3D polygon3D = (Polygon3D) feaFondos.getGeometry();
                lineString3D.add(firstPt);

                if ((bulge != 0) &&
                        !((lineString3D.get(lineString3D.pointNr() - 2).getX() == lineString3D.get(lineString3D.pointNr() -
                                                                                                       1)
                                                                                                  .getX()) &&
                        (lineString3D.get(lineString3D.pointNr() - 2).getY() == lineString3D.get(lineString3D.pointNr() -
                                                                                                     1)
                                                                                                .getY()))) {
                    // 041122: Corrección del bug en los bulges de FIXT3.DXF
                    Vector arc = createArc(new Point2D.Double(lineString3D.get(lineString3D.pointNr() -
                                                                               2)
                                                                          .getX(),
                                                              lineString3D.get(lineString3D.pointNr() -
                                                                               2)
                                                                          .getY()),
                                           new Point2D.Double(lineString3D.get(lineString3D.pointNr() -
                                                                               1)
                                                                          .getX(),
                                                              lineString3D.get(lineString3D.pointNr() -
                                                                               1)
                                                                          .getY()),
                                           bulge);
                    lineString3D.remove(lineString3D.pointNr() - 1);
                    lineString3D.remove(lineString3D.pointNr() - 1);
                    polygon3D.remove(lineString3D.pointNr() - 1);
                    polygon3D.remove(lineString3D.pointNr() - 1);

                    if (bulge > 0) {
                        for (int i = 0; i < arc.size(); i++) {
                            Point2D ptAux = new Point2D.Double();
                            double z = ((Point3D) lineString3D.get(lineString3D.pointNr() -
                                                                   2)).getZ();
                            ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                     ((Point2D) arc.get(i)).getY());

                            Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                          ptAux.getY(), z);
                            lineString3D.add(ptAux3D);
                            polygon3D.add(ptAux3D);

                            if (lineString3D.pointNr() == 1) {
                                firstPt = ptAux3D;
                            }
                        }
                    } else {
                        for (int i = arc.size() - 1; i >= 0; i--) {
                            Point2D ptAux = new Point2D.Double();
                            double z = ((Point3D) lineString3D.get(lineString3D.pointNr() -
                                                                   2)).getZ();
                            ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                     ((Point2D) arc.get(i)).getY());

                            Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                          ptAux.getY(), z);
                            lineString3D.add(ptAux3D);
                            polygon3D.add(ptAux3D);

                            if ((lineString3D.pointNr() == 1) ||
                                    (polygon3D.pointNr() == 1)) {
                                firstPt = ptAux3D;
                            }
                        }
                    }

                    // 041122
                    bulge = 0.0;
                }

                if (hasFaces) {
                    //System.out.println("Sabe que es un poligono.");
                    //System.out.println("POLYLINE: caras=" +faces.size()+", puntos="+ lineString.pointNr());
                    LineString ls1 = new LineString();
                    Polygon pl1 = new Polygon();
                    LineString ls2 = new LineString();
                    Polygon pl2 = new Polygon();
                    LineString ls = new LineString();
                    Polygon pl = new Polygon();
                    int[] face;
                    int i0;
                    int i1;
                    Iterator iter = faces.iterator();

                    while (iter.hasNext()) {
                        face = (int[]) iter.next();
                        i0 = face[3];

                        for (int i = 0; i < 4; i++) {
                            i1 = face[i];

                            if (i0 > 0) {
                                if ((facesIterador % 2) != 0) {
                                    ls1.add(lineString3D.get(i0 - 1));
                                    pl1.add(polygon3D.get(i0 - 1));
                                } else {
                                    ls2.add(lineString3D.get(i0 - 1));
                                    pl2.add(polygon3D.get(i0 - 1));
                                }

                                facesIterador = facesIterador + 1;
                            }

                            i0 = i1;
                        }
                    }

                    facesFirstPoint = new Point2D.Double(ls1.get(0).getX(),
                                                         ls1.get(0).getY());

                    for (int i = 0; i < ls1.pointNr(); i++) {
                        ls.add(ls1.get(i));
                        pl.add(pl1.get(i));
                    }

                    for (int i = ls2.pointNr() - 1; i > 0; i--) {
                        ls.add(ls2.get(i));
                        pl.add(pl2.get(i));
                    }

                    ls.add(facesFirstPoint);
                    pl.add(facesFirstPoint);
                    lastFeaBordes.setGeometry(ls);
                    lastFeaFondos.setGeometry(pl);
                } else {
                    lastFeaBordes.setGeometry(lineString3D);
                    lastFeaFondos.setGeometry(polygon3D);
                }

                // 041130: Rellena las props con los atributos.
                completeAttributes(lastFeaBordes);
                completeAttributes(lastFeaFondos);

                setPolylineElevation(lastFeaBordes, lastFeaFondos);

                if (addingToBlock == false) {
                    //features.add(lastFeaBordes);
                    features.add(lastFeaFondos);
                } else {
                    //blk.add(lastFeaBordes);
                    blk.add(lastFeaFondos);
                }

                lastFeaBordes = null;
                lastFeaFondos = null;
            } else if (lastFeaBordes.getGeometry() instanceof InsPoint3D) {
                // Se trata de un SEQEND despues de un ATTRIB
                copyAttributes(lastFeaBordes);

                gestionaInsert(lastFeaBordes);

                if (addingToBlock == false) {
                    features.add(lastFeaFondos);
                } else {
                    blk.add(lastFeaBordes);
                }

                lastFeaBordes = null;
                lastFeaFondos = null;
            } else {
                // Caso no contemplado.
            }
        } else {
            if (lastFeaBordes.getGeometry() instanceof LineString3D) {
                Feature feaBordes = lastFeaBordes;
                LineString3D lineString3D = (LineString3D) feaBordes.getGeometry();

                if ((bulge != 0) &&
                        !((lineString3D.get(lineString3D.pointNr() - 2).getX() == lineString3D.get(lineString3D.pointNr() -
                                                                                                       1)
                                                                                                  .getX()) &&
                        (lineString3D.get(lineString3D.pointNr() - 2).getY() == lineString3D.get(lineString3D.pointNr() -
                                                                                                     1)
                                                                                                .getY()))) {
                    // 041122: Corrección del bug en los bulges de FIXT3.DXF
                    Vector arc = createArc(new Point2D.Double(lineString3D.get(lineString3D.pointNr() -
                                                                               2)
                                                                          .getX(),
                                                              lineString3D.get(lineString3D.pointNr() -
                                                                               2)
                                                                          .getY()),
                                           new Point2D.Double(lineString3D.get(lineString3D.pointNr() -
                                                                               1)
                                                                          .getX(),
                                                              lineString3D.get(lineString3D.pointNr() -
                                                                               1)
                                                                          .getY()),
                                           bulge);
                    lineString3D.remove(lineString3D.pointNr() - 1);
                    lineString3D.remove(lineString3D.pointNr() - 1);

                    if (bulge > 0) {
                        for (int i = 0; i < arc.size(); i++) {
                            Point2D ptAux = new Point2D.Double();
                            double z = ((Point3D) lineString3D.get(lineString3D.pointNr() -
                                                                   2)).getZ();
                            ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                     ((Point2D) arc.get(i)).getY());

                            Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                          ptAux.getY(), z);
                            lineString3D.add(ptAux3D);

                            if (lineString3D.pointNr() == 1) {
                                firstPt = ptAux3D;
                            }
                        }
                    } else {
                        for (int i = arc.size() - 1; i >= 0; i--) {
                            Point2D ptAux = new Point2D.Double();
                            double z = ((Point3D) lineString3D.get(lineString3D.pointNr() -
                                                                   2)).getZ();
                            ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                     ((Point2D) arc.get(i)).getY());

                            Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                          ptAux.getY(), z);
                            lineString3D.add(ptAux3D);

                            if (lineString3D.pointNr() == 1) {
                                firstPt = ptAux3D;
                            }
                        }
                    }

                    // 041122
                    bulge = 0.0;
                }

                if (hasFaces) {
                    //System.out.println("POLYLINE: caras=" +faces.size()+", puntos="+ lineString.pointNr());
                    LineString ls1 = new LineString();
                    LineString ls2 = new LineString();
                    LineString ls = new LineString();
                    int[] face;
                    int i0;
                    int i1;
                    Iterator iter = faces.iterator();

                    while (iter.hasNext()) {
                        face = (int[]) iter.next();
                        i0 = face[3];

                        for (int i = 0; i < 4; i++) {
                            i1 = face[i];

                            if (i0 > 0) {
                                if ((facesIterador % 2) != 0) {
                                    ls1.add(lineString3D.get(i0 - 1));

                                    //ls.add((Point2D)lineString.get(Math.abs(i1)-1));
                                } else {
                                    ls2.add(lineString3D.get(i0 - 1));

                                    //ls.add((Point2D)lineString.get(Math.abs(i1)-1));
                                }

                                facesIterador = facesIterador + 1;
                            }

                            i0 = i1;
                        }
                    }

                    facesFirstPoint = new Point2D.Double(ls1.get(0).getX(),
                                                         ls1.get(0).getY());

                    for (int i = 0; i < ls1.pointNr(); i++) {
                        ls.add(ls1.get(i));
                    }

                    for (int i = ls2.pointNr() - 1; i > 0; i--) {
                        ls.add(ls2.get(i));
                    }

                    ls.add(facesFirstPoint);
                    lastFeaBordes.setGeometry(ls);
                } else {
                    lastFeaBordes.setGeometry(lineString3D);
                }

                // 041130: Rellena las props con los atributos.
                completeAttributes(lastFeaBordes);

                setPolylineElevation(lastFeaBordes);

                if (addingToBlock == false) {
                    features.add(lastFeaBordes);
                } else {
                    blk.add(lastFeaBordes);
                }

                lastFeaBordes = null;
            } else {
                // Se trata de un SEQEND despues de un ATTRIB
            }
        }

        xtruX = 0.0;
        xtruY = 0.0;
        xtruZ = 1.0;
        bulge = 0.0;
        isDoubleFeatured = false;
        hasFaces = false;
        facesIterador = 1;
    }

    /**
     * Establece un valor constante para la elevación de una polilínea.
     * @param feaBordes, la polilínea.
     */
    private void setPolylineElevation(Feature feaBordes) {
        if (constantPolylineElevation) {
            Double doub = new Double(lastVertexElevation);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);
        }
    }

    /**
     * Establece un valor constante para la elevación de un polígono.
     * @param feaBordes, borde del polígono.
     * @param feaFondos, fondo del polígono.
     */
    private void setPolylineElevation(Feature feaBordes, Feature feaFondos) {
        if (constantPolylineElevation) {
            Double doub = new Double(lastVertexElevation);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);
            feaFondos.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);
            feaFondos.setProp("elevation", string);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#addVertex(org.cresques.io.DxfGroupVector)
     */
    public void addVertex(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        int vFlags = 0;

        if (isDoubleFeatured) {
            Feature feaBordes = lastFeaBordes;
            Feature feaFondos = lastFeaFondos;
            LineString3D lineString3D = (LineString3D) feaBordes.getGeometry();
            Polygon3D polygon3D = (Polygon3D) feaFondos.getGeometry();

            if (grp.hasCode(8)) {
                feaBordes.setProp("layer", grp.getDataAsString(8));
                feaFondos.setProp("layer", grp.getDataAsString(8));
            }

            if (grp.hasCode(70)) {
                vFlags = grp.getDataAsInt(70);
            }

            x = grp.getDataAsDouble(10);
            y = grp.getDataAsDouble(20);
            z = grp.getDataAsDouble(30);

            Point3D point_in = new Point3D(x, y, z);
            Point3D xtru = new Point3D(xtruX, xtruY, xtruZ);
            Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
            x = point_out.getX();
            y = point_out.getY();
            z = point_out.getZ();

            if (z != 0.0) {
                dxf3DFile = true;
            }

            if ((z != lastVertexElevation) && (lineString3D.pointNr() > 0)) {
                constantPolylineElevation = false;
            }

            lastVertexElevation = z;

            //System.out.println("addVertex(): vFlags = " + vFlags);
            if (((vFlags & 0x80) == 0x80) && ((vFlags & 0x40) == 0)) {
                int[] face = { 0, 0, 0, 0 };
                face[0] = grp.getDataAsInt(71);
                face[1] = grp.getDataAsInt(72);
                face[2] = grp.getDataAsInt(73);
                face[3] = grp.getDataAsInt(74);
                addFace(face);
            } else if ((vFlags & 0x10) == 0x10) {
                // Son vertices que se trataran cuando se implementen
                // los splines. En principio no se hace nada con ellos.
            } else {
            	Point2D ptaux = proj.createPoint(x, y);
                Point3D pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
                lineString3D.add(pt);
                polygon3D.add(pt);

                if (lineString3D.pointNr() == 1) {
                    firstPt = pt;
                }

                if (bulge == 0.0) {
                    if (grp.hasCode(42)) {
                        bulge = grp.getDataAsDouble(42);
                    } else {
                        bulge = 0.0;
                    }
                } else {
                    double bulge_aux = 0.0;

                    if (grp.hasCode(42)) {
                        bulge_aux = grp.getDataAsDouble(42);
                    } else {
                        bulge_aux = 0.0;
                    }

                    //int cnt = lineString.pointNr();
                    if ((ptAnterior.getX() == pt.getX()) &&
                            (ptAnterior.getY() == pt.getY())) {
                        // no se construye el arco
                    } else {
                        lineString3D.remove(lineString3D.pointNr() - 1);
                        lineString3D.remove(lineString3D.pointNr() - 1);
                        polygon3D.remove(polygon3D.pointNr() - 1);
                        polygon3D.remove(polygon3D.pointNr() - 1);

                        Vector arc = createArc(ptAnterior, pt, bulge);

                        if (bulge > 0) {
                            for (int i = 0; i < arc.size(); i++) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                         ((Point2D) arc.get(i)).getY());

                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), z);
                                lineString3D.add(ptAux3D);
                                polygon3D.add(ptAux3D);

                                if (lineString3D.pointNr() == 1) {
                                    firstPt = ptAux3D;
                                }
                            }
                        } else {
                            for (int i = arc.size() - 1; i >= 0; i--) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                         ((Point2D) arc.get(i)).getY());

                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), z);
                                lineString3D.add(ptAux3D);
                                polygon3D.add(ptAux3D);

                                if ((lineString3D.pointNr() == 1) ||
                                        (polygon3D.pointNr() == 1)) {
                                    firstPt = ptAux3D;
                                }
                            }
                        }
                    }

                    bulge = bulge_aux;
                }

                ptAnterior = pt;
            }
        } else {
            Feature feaBordes = lastFeaBordes;
            LineString3D lineString3D = (LineString3D) feaBordes.getGeometry();

            if (grp.hasCode(8)) {
                feaBordes.setProp("layer", grp.getDataAsString(8));
            }

            if (grp.hasCode(70)) {
                vFlags = grp.getDataAsInt(70);
            }

            x = grp.getDataAsDouble(10);
            y = grp.getDataAsDouble(20);
            if (grp.hasCode(30)){
            	z = grp.getDataAsDouble(30);
            }

            Point3D point_in = new Point3D(x, y, z);
            Point3D xtru = new Point3D(xtruX, xtruY, xtruZ);
            Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
            x = point_out.getX();
            y = point_out.getY();
            z = point_out.getZ();

            if (z != 0.0) {
                dxf3DFile = true;
            }

            if ((z != lastVertexElevation) && (lineString3D.pointNr() > 0)) {
                constantPolylineElevation = false;
            }

            lastVertexElevation = z;

            if (((vFlags & 0x80) == 0x80) && ((vFlags & 0x40) == 0)) {
                int[] face = { 0, 0, 0, 0 };
                face[0] = grp.getDataAsInt(71);
                face[1] = grp.getDataAsInt(72);
                face[2] = grp.getDataAsInt(73);
                face[3] = grp.getDataAsInt(74);
                addFace(face);
            } else if ((vFlags & 16) == 16) {
                // no se hace nada.
            } else {
            	Point2D ptaux = proj.createPoint(x, y);
                Point3D pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
                lineString3D.add(pt);

                //System.out.println("addVertex: pt = " + pt);
                if (lineString3D.pointNr() == 1) {
                    firstPt = pt;

                    //System.out.println("addVertex(Primer pto de la lineString, firstPt=pt): firstPt = " + firstPt);
                }

                if (bulge == 0.0) {
                    if (grp.hasCode(42)) {
                        bulge = grp.getDataAsDouble(42);
                    } else {
                        bulge = 0.0;
                    }

                    //System.out.println("addVertex: El vertice anterior tenia bulge=0.0");
                } else {
                    //System.out.println("addVertex: El vertice anterior tiene bulge = " + bulge);
                    double bulge_aux = 0.0;

                    if (grp.hasCode(42)) {
                        bulge_aux = grp.getDataAsDouble(42);
                    } else {
                        bulge_aux = 0.0;
                    }

                    if ((ptAnterior.getX() == pt.getX()) &&
                            (ptAnterior.getY() == pt.getY())) {
                        // no se construye el arco
                    } else {
                        // Borro los puntos inicio y final del arco.
                        lineString3D.remove(lineString3D.pointNr() - 1);
                        lineString3D.remove(lineString3D.pointNr() - 1);

                        Vector arc = createArc(ptAnterior, pt, bulge);

                        if (bulge > 0) {
                            for (int i = 0; i < arc.size(); i++) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                         ((Point2D) arc.get(i)).getY());

                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), z);
                                lineString3D.add(ptAux3D);

                                if (lineString3D.pointNr() == 1) {
                                    firstPt = ptAux3D;
                                }
                            }
                        } else {
                            for (int i = arc.size() - 1; i >= 0; i--) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                         ((Point2D) arc.get(i)).getY());

                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), z);
                                lineString3D.add(ptAux3D);

                                if (lineString3D.pointNr() == 1) {
                                    firstPt = ptAux3D;
                                }
                            }
                        }
                    }

                    bulge = bulge_aux;
                }

                ptAnterior = pt;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createLwPolyline(org.cresques.io.DxfGroupVector)
     */
    public void createLwPolyline(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double elev = 0.0;
        DxfGroup g = null;
        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();

        //Geometry geometria;
        //Feature feature= new Feature();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();
        int flags = 0;
        //int NumberOfVertices = 0;
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        //feature.setProp("dxfEntity", "LwPolyline");
        feaBordes.setProp("dxfEntity", "LwPolyline");
        feaFondos.setProp("dxfEntity", "LwPolyline");

        //if (grp.hasCode(8)) {
            //feature.setProp("layer", grp.getDataAsString(8));
        //    feaBordes.setProp("layer", grp.getDataAsString(8));
        //}

        feaBordes.setProp("layer", grp.getDataAsString(8));
        feaFondos.setProp("layer", grp.getDataAsString(8));

        if (grp.hasCode(38)) {
            elev = grp.getDataAsDouble(38);

            if (elev != 0.0) {
                dxf3DFile = true;
            }

            Double doub = new Double(elev);
            String string = doub.toString();

            //feature.setProp("elevation", string);
            feaBordes.setProp("elevation", string);
            feaFondos.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);

            //feature.setProp("elevation", doub.toString());
            feaBordes.setProp("elevation", doub.toString());
            feaFondos.setProp("elevation", doub.toString());
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();

            //feature.setProp("thickness", string);
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);

            //feature.setProp("thickness", doub.toString());
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(70)) {
            flags = grp.getDataAsInt(70);
        }

        if ((flags & 0x01) == 0x01) {
            //geometria = new Polygon();
            feaBordes.setGeometry(lineString3D);
            feaFondos.setGeometry(polygon3D);
            isDoubleFeatured = true;
        } else {
            //geometria = new LineString();
            feaBordes.setGeometry(lineString3D);
            isDoubleFeatured = false;
        }

        //if (grp.hasCode(90)) {
        //    NumberOfVertices = grp.getDataAsInt(90);
        //}

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        int j = 0;
        double firstX = 0.0;
        double firstY = 0.0;
        boolean hasBulge = false;
        double bulgeLwp = 0.0;

        for (int i = 0; i < grp.size(); i++) {
            g = (DxfGroup) grp.get(i);

            if (g.getCode() == 10) {
                j++;
                x = ((Double) g.getData()).doubleValue();
            } else if (g.getCode() == 20) {
                y = ((Double) g.getData()).doubleValue();

                // Añadiendo extrusion a LwPolyline ...
                Point3D point_in1 = new Point3D(x, y, elev);
                Point3D xtru = new Point3D(extx, exty, extz);
                Point3D point_out1 = DxfCalXtru.CalculateXtru(point_in1, xtru);
                x = point_out1.getX();
                y = point_out1.getY();
                elev = point_out1.getZ();

                //
                if (hasBulge) {
                    Point2D finalPoint = new Point2D.Double(x, y);

                    if (((lineString3D).get(lineString3D.pointNr() -
                                                               1).getX() == finalPoint.getX()) &&
                            ((lineString3D).get(lineString3D.pointNr() -
                                                                   1).getY() == finalPoint.getY())) {
                        // no se construye el arco
                    } else {
                        Vector arc = createArc((lineString3D).get(lineString3D.pointNr() -
                                                                                 1),
                                               finalPoint, bulgeLwp);
                        lineString3D.remove(lineString3D.pointNr() - 1);

                        if (isDoubleFeatured) {
                            polygon3D.remove(polygon3D.pointNr() - 1);
                        }

                        if (bulgeLwp > 0) {
                            for (int k = 0; k < arc.size(); k++) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(k)).getX(),
                                                         ((Point2D) arc.get(k)).getY());

                                //System.out.println("createLwPolyline: ptAux = " + ptAux);
                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), elev);
                                lineString3D.add(ptAux3D);

                                //if (lineString.pointNr() == 1) firstPt = ptAux;
                                if (isDoubleFeatured) {
                                    polygon3D.add(ptAux3D);
                                }

                                if ((lineString3D.pointNr() == 1) ||
                                        (polygon3D.pointNr() == 1)) {
                                    firstPt = ptAux3D;
                                }
                            }
                        } else {
                            for (int k = arc.size() - 1; k >= 0; k--) {
                                Point2D ptAux = new Point2D.Double();
                                ptAux = proj.createPoint(((Point2D) arc.get(k)).getX(),
                                                         ((Point2D) arc.get(k)).getY());

                                Point3D ptAux3D = new Point3D(ptAux.getX(),
                                                              ptAux.getY(), elev);
                                lineString3D.add(ptAux3D);

                                if (isDoubleFeatured) {
                                    polygon3D.add(ptAux3D);
                                }

                                if ((lineString3D.pointNr() == 1) ||
                                        (polygon3D.pointNr() == 1)) {
                                    firstPt = ptAux3D;
                                }
                            }
                        }
                    }

                    hasBulge = false;
                    bulgeLwp = 0.0;
                } else {
                    //System.out.println("createLwPolyline: hasBulge siempre es false");
                	Point2D ptAux2D = proj.createPoint(x, y);
					Point3D ptAux3D = new Point3D(ptAux2D.getX(), ptAux2D
							.getY(), elev);
                    lineString3D.add(ptAux3D);

                    if (isDoubleFeatured) {
                        polygon3D.add(ptAux3D);
                    }
                }

                if (j == 1) {
                    firstX = x;
                    firstY = y;
                }

                x = 0.0;
                y = 0.0;
            } else if (g.getCode() == 42 && ((Double)g.getData()).doubleValue()!=0.0) { // Cuando los bulges eran 0.000 se provocaban errores
                //System.out.println("createLwPolyline: Lee el bulgeLwp");
                hasBulge = true;
                bulgeLwp = ((Double) g.getData()).doubleValue();
            }
        }

        if (isDoubleFeatured) {
            //geometria.add(proj.createPoint(firstX, firstY));
        	Point2D ptAux2D = proj.createPoint(firstX, firstY);
			Point3D ptAux3D = new Point3D(ptAux2D.getX(), ptAux2D.getY(),
                                          elev);
            lineString3D.add(ptAux3D);
            polygon3D.add(ptAux3D);
        }

        lastFeaBordes = feaBordes;

        if (isDoubleFeatured) {
            lastFeaFondos = feaFondos;
        }

        // 041130: Rellena las props con los atributos.
        completeAttributes(lastFeaBordes);
        completeAttributes(lastFeaFondos);

        //features.add(feature);
        if (addingToBlock == false) {
            if (isDoubleFeatured) {
                features.add(feaFondos);
            } else {
                features.add(feaBordes);
            }
        } else {
            //System.out.println("createLwPolyline(): Añadimos una lwpolilinea al bloque " + iterator);
            if (isDoubleFeatured) {
                blk.add(feaFondos);
            } else {
                blk.add(feaBordes);
            }
        }

        isDoubleFeatured = false;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createLine(org.cresques.io.DxfGroupVector)
     */
    public void createLine(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z1 = 0.0;
        double z2 = 0.0;
        double elev = 0.0;
        DxfGroup g = null;
        Point2D pt1 = null;
        Point2D pt2 = null;
        LineString3D lineString3D = new LineString3D();
        Feature feature = new Feature();
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        feature.setProp("dxfEntity", "Line");

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z1 = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)) {
            z1 = grp.getDataAsDouble(30);
            elev = z1;
            Double doub = new Double(elev);
            String string = doub.toString();
            //feature.setProp("elevation", string);
        }*/
        pt1 = proj.createPoint(x, y);
        x = grp.getDataAsDouble(11);
        y = grp.getDataAsDouble(21);
        z2 = grp.getDataAsDouble(31);

        /*if (grp.hasCode(31)) {
            z2 = grp.getDataAsDouble(31);
        } else {
            // Cuando no se especifican z para las lineas se asume que la
            // z es cero.
            Double doub = new Double(0.0);
            //feature.setProp("elevation", doub.toString());
        }*/
        pt2 = proj.createPoint(x, y);

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in1 = new Point3D(pt1.getX(), pt1.getY(), z1);
        Point3D point_in2 = new Point3D(pt2.getX(), pt2.getY(), z2);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out1 = DxfCalXtru.CalculateXtru(point_in1, xtru);
        Point3D point_out2 = DxfCalXtru.CalculateXtru(point_in2, xtru);

        if (point_out1.getZ() != 0.0) {
            dxf3DFile = true;
        }

        if (point_out2.getZ() != 0.0) {
            dxf3DFile = true;
        }

        if (point_out1.getZ() == point_out2.getZ()) {
            elev = z1;

            Double doub = new Double(elev);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            elev = 0.0;

            Double doub = new Double(elev);
            String string = doub.toString();
            feature.setProp("elevation", string);
        }

        //pt1.setLocation(point_out1);
        //pt2.setLocation(point_out2);
        lineString3D.add(point_out1);
        lineString3D.add(point_out2);

        feature.setGeometry(lineString3D);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        //features.add(feature);
        if (addingToBlock == false) {
            //System.out.println("createLine(): Añadimos una linea a la lista de entidades");
            features.add(feature);
        } else {
            //System.out.println("createLine(): Añadimos una linea al bloque " + iterator);
            blk.add(feature);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createText(org.cresques.io.DxfGroupVector)
     */
    public void createText(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double h = 0.0;
        double rot = 0.0;
        DxfGroup g = null;

        //Point3D pt1 = null, pt2 = null;
        Point3D pt = null;
        org.cresques.px.gml.Point3D point = new org.cresques.px.gml.Point3D();
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        point.setTextPoint(true);

        Feature feature = new Feature();

        feature.setProp("dxfEntity", "Text");

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(1)) {
            String strAux1 = grp.getDataAsString(1);
            strAux1 = DxfConvTexts.ConvertText(strAux1);
            feature.setProp("text", strAux1);
        } else {
            feature.setProp("text", "No Text Code");
        }

        if (grp.hasCode(40)) {
            Double heightD = new Double(grp.getDataAsDouble(40));
            String heightS = heightD.toString();
            feature.setProp("textHeight", heightS);
        } else {
            feature.setProp("textHeight", "20.0");
        }

        if (grp.hasCode(50)) {
            Double rotD = new Double(grp.getDataAsDouble(50));
            String rotS = rotD.toString();
            feature.setProp("textRotation", rotS);

            //System.out.println("rotS = " + rotS);
        } else {
            feature.setProp("textRotation", "0.0");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        if (grp.hasCode(30)) {
        	z = grp.getDataAsDouble(30);
        }
        /*if (grp.hasCode(30)){
            Double doub = new Double(z);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feature.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        point.add(new Point3D(x, y, z));
        feature.setGeometry(point);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        //features.add(feature);
        if (addingToBlock == false) {
            features.add(feature);
        } else {
            //System.out.println("createText(): Añadimos un text al bloque " + iterator);
            blk.add(feature);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createMText(org.cresques.io.DxfGroupVector)
     */
    public void createMText(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double h = 0.0;
        double rot = 0.0;
        DxfGroup g = null;

        //Point2D pt1 = null, pt2 = null;
        Point3D pt = null;
        org.cresques.px.gml.Point3D point = new org.cresques.px.gml.Point3D();
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        point.setTextPoint(true);

        Feature feature = new Feature();

        feature.setProp("dxfEntity", "Text");

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(1)) {
            String strAux1 = grp.getDataAsString(1);
            strAux1 = DxfConvTexts.ConvertText(strAux1);
            feature.setProp("text", strAux1);
        } else {
            feature.setProp("text", "No Text Code");
        }

        if (grp.hasCode(40)) {
            Double heightD = new Double(grp.getDataAsDouble(40));
            String heightS = heightD.toString();
            feature.setProp("textHeight", heightS);
        } else {
            feature.setProp("textHeight", "20.0");
        }

        if (grp.hasCode(50)) {
            Double rotD = new Double(grp.getDataAsDouble(50));
            String rotS = rotD.toString();
            feature.setProp("textRotation", rotS);

            //System.out.println("rotS = " + rotS);
        } else {
            feature.setProp("textRotation", "0.0");
        }

        if (grp.hasCode(71)) {
            int attachPoint = grp.getDataAsInt(71);

            if (attachPoint == 1) {
            } else if (attachPoint == 2) {
            } else if (attachPoint == 3) {
            } else if (attachPoint == 4) {
            } else if (attachPoint == 5) {
            } else if (attachPoint == 6) {
            } else if (attachPoint == 7) {
            } else if (attachPoint == 8) {
            } else if (attachPoint == 9) {
            }
        }

        if (grp.hasCode(72)) {
            int drawDirection = grp.getDataAsInt(71);

            if (drawDirection == 1) {
            } else if (drawDirection == 3) {
            } else if (drawDirection == 5) {
            }
        }

        if (grp.hasCode(73)) {
            int spacingStyle = grp.getDataAsInt(71);

            if (spacingStyle == 1) {
            } else if (spacingStyle == 2) {
            }
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)){
            z = grp.getDataAsDouble(30);
            Double doub = new Double(z);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feature.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        point.add(new Point3D(x, y, z));
        feature.setGeometry(point);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        //features.add(feature);
        if (addingToBlock == false) {
            features.add(feature);
        } else {
            //System.out.println("createText(): Añadimos un text al bloque " + iterator);
            blk.add(feature);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createPoint(org.cresques.io.DxfGroupVector)
     */
    public void createPoint(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        DxfGroup g = null;
        Point3D pt = null;
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;
        org.cresques.px.gml.Point3D point = new org.cresques.px.gml.Point3D();
        Feature feature = new Feature();

        feature.setProp("dxfEntity", "Point");

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
            Double doub = new Double(z);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feature.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        point.add(new Point3D(x, y, z));
        feature.setGeometry(point);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        /*for (int i=0;i<attributes.size();i++) {
            String[] att = new String[2];
            att = (String[])attributes.get(i);
            feature.setProp(att[0],att[1]);
        }*/

        //features.add(feature);
        if (addingToBlock == false) {
            features.add(feature);
        } else {
            //System.out.println("createPoint(): Añadimos un punto al bloque " + iterator);
            blk.add(feature);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createCircle(org.cresques.io.DxfGroupVector)
     */
    public void createCircle(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double r = 0.0;
        Point3D firstPt = new Point3D();
        DxfGroup g = null;
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;
        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();

        feaBordes.setProp("dxfEntity", "Circle");
        feaFondos.setProp("dxfEntity", "Circle");

        if (grp.hasCode(8)) {
            feaBordes.setProp("layer", grp.getDataAsString(8));
        }

        feaFondos.setProp("layer", grp.getDataAsString(8));

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }


        /*if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
            Double doub = new Double(z);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);
            feaFondos.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feaBordes.setProp("elevation", doub.toString());
            feaFondos.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(40)) {
            r = grp.getDataAsDouble(40);
        }

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feaBordes.setProp("elevation", doub.toString());
        feaFondos.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        Point2D c = proj.createPoint(x, y);
        Point3D center = new Point3D(c.getX(), c.getY(), z);
        Point3D[] pts = new Point3D[360];
        int angulo = 0;

        for (angulo = 0; angulo < 360; angulo++) {
            pts[angulo] = new Point3D(center.getX(), center.getY(),
                                      center.getZ());
            pts[angulo] = new Point3D(pts[angulo].getX() +
                                      (r * Math.sin((angulo * Math.PI) / 180.0)),
                                      pts[angulo].getY() +
                                      (r * Math.cos((angulo * Math.PI) / 180.0)),
                                      center.getZ());

            if (pts.length == 1) {
                firstPt = pts[angulo];
            }
        }

        for (int i = 0; i < pts.length; i++) {
            lineString3D.add(pts[i]);
            polygon3D.add(pts[i]);
        }

        feaBordes.setGeometry(lineString3D);
        feaFondos.setGeometry(polygon3D);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feaBordes);
        completeAttributes(feaFondos);

        //features.add(feature);
        if (addingToBlock == false) {
            //System.out.println("createCircle(): Añade un circulo a la lista de entidades");
            //features.add(feaBordes);
            features.add(feaFondos);
        } else {
            //System.out.println("createCircle(): Añadimos un circulo al bloque " + iterator);
            //blk.add(feaBordes);
            blk.add(feaFondos);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createArc(org.cresques.io.DxfGroupVector)
     */
    public void createArc(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double r = 0.0;
        double empieza = 0.0;
        double acaba = 0.0;
        DxfGroup g = null;
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;
        LineString3D lineString3D = new LineString3D();
        Feature feature = new Feature();

        feature.setProp("dxfEntity", "Arc");

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        if (grp.hasCode(30))
        {
        	z = grp.getDataAsDouble(30);
        }

        /*if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
            Double doub = new Double(z);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(40)) {
            r = grp.getDataAsDouble(40);
        }

        if (grp.hasCode(50)) {
            empieza = grp.getDataAsDouble(50);
        }

        if (grp.hasCode(51)) {
            acaba = grp.getDataAsDouble(51);
        }

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feature.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        Point2D c = proj.createPoint(x, y);
        Point3D center = new Point3D(c.getX(), c.getY(), z);

        //System.out.println("empieza = " + empieza + ", acaba = " + acaba);
        int iempieza = (int) empieza;
        int iacaba = (int) acaba;

        //System.out.println("iempieza = " + iempieza + ", iacaba = " + iacaba);
        double angulo = 0;
        Point3D[] pts = null;

        if (empieza <= acaba) {
            pts = new Point3D[(iacaba - iempieza) + 2];
            angulo = empieza;
            pts[0] = new Point3D(center.getX() +
                                 (r * Math.cos((angulo * Math.PI) / 180.0)),
                                 center.getY() +
                                 (r * Math.sin((angulo * Math.PI) / 180.0)),
                                 center.getZ());

            for (int i = 1; i <= ((iacaba - iempieza) + 1); i++) {
                angulo = (iempieza + i);
                pts[i] = new Point3D(center.getX() +
                                     (r * Math.cos((angulo * Math.PI) / 180.0)),
                                     center.getY() +
                                     (r * Math.sin((angulo * Math.PI) / 180.0)),
                                     center.getZ());
            }

            angulo = acaba;
            pts[(iacaba - iempieza) + 1] = new Point3D(center.getX() +
                                                       (r * Math.cos((angulo * Math.PI) / 180.0)),
                                                       center.getY() +
                                                       (r * Math.sin((angulo * Math.PI) / 180.0)),
                                                       center.getZ());
        } else {
            pts = new Point3D[(360 - iempieza) + iacaba + 2];
            angulo = empieza;

            //System.out.println("pts[0] = " + pts[0] + ", center = " + center + ", angulo = " + angulo);
            pts[0] = new Point3D(center.getX() +
                                 (r * Math.cos((angulo * Math.PI) / 180.0)),
                                 center.getY() +
                                 (r * Math.sin((angulo * Math.PI) / 180.0)),
                                 center.getZ());

            for (int i = 1; i <= (360 - iempieza); i++) {
                angulo = (iempieza + i);
                pts[i] = new Point3D(center.getX() +
                                     (r * Math.cos((angulo * Math.PI) / 180.0)),
                                     center.getY() +
                                     (r * Math.sin((angulo * Math.PI) / 180.0)),
                                     center.getZ());
            }

            for (int i = (360 - iempieza) + 1;
                     i <= ((360 - iempieza) + iacaba); i++) {
                angulo = (i - (360 - iempieza));
                pts[i] = new Point3D(center.getX() +
                                     (r * Math.cos((angulo * Math.PI) / 180.0)),
                                     center.getY() +
                                     (r * Math.sin((angulo * Math.PI) / 180.0)),
                                     center.getZ());
            }

            angulo = acaba;
            pts[(360 - iempieza) + iacaba + 1] = new Point3D(center.getX() +
                                                             (r * Math.cos((angulo * Math.PI) / 180.0)),
                                                             center.getY() +
                                                             (r * Math.sin((angulo * Math.PI) / 180.0)),
                                                             center.getZ());
        }

        for (int i = 0; i < pts.length; i++) {
            lineString3D.add(pts[i]);
        }

        feature.setGeometry(lineString3D);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        //features.add(feature);
        if (addingToBlock == false) {
            features.add(feature);
        } else {
            //System.out.println("createArc(): Añadimos un arco al bloque " + iterator);
            blk.add(feature);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createInsert(org.cresques.io.DxfGroupVector)
     */
    public void createInsert(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        DxfGroup g = null;
        Point3D pt = new Point3D(0.0, 0.0, 0.0);
        Point3D scaleFactor = new Point3D(1.0, 1.0, 1.0);
        double rotAngle = 0.0;
        String blockName = "";
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        InsPoint3D insert = new InsPoint3D();
        Feature feature = new Feature();
        org.cresques.px.gml.Point3D secondGeom = new org.cresques.px.gml.Point3D();
        Feature secondFeat = new Feature();
        int attributesFollowFlag = 0;

        feature.setProp("dxfEntity", "Insert");
        secondFeat.setProp("dxfEntity", "Insert");

        if (grp.hasCode(2)) {
            blockName = grp.getDataAsString(2);

            //feature.setProp("blockName", blockName);
            insert.setBlockName(blockName);
        }

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
            secondFeat.setProp("layer", grp.getDataAsString(8));
        }

        Double doub = new Double(0.0);
        secondFeat.setProp("thickness", doub.toString());

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            secondFeat.setProp("color", string);
            feature.setProp("colorByLayer", "false");
            secondFeat.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            secondFeat.setProp("color", string);
            feature.setProp("colorByLayer", "true");
            secondFeat.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(66)) {
            attributesFollowFlag = grp.getDataAsInt(66);
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
            Double doubz = new Double(z);
            String string = doubz.toString();
            feature.setProp("elevation", string);
            secondFeat.setProp("elevation", string);
        } else {
            Double elev = new Double(z);
            //feature.setProp("elevation", doub.toString());
            feature.setProp("elevation", elev.toString());
            secondFeat.setProp("elevation", elev.toString());
        }*/
        if (grp.hasCode(41)) {
            scaleFactor.setLocation(grp.getDataAsDouble(41), scaleFactor.getY());
            insert.setScaleFactor(scaleFactor);
        } else {
            insert.setScaleFactor(scaleFactor);
        }

        if (grp.hasCode(42)) {
            scaleFactor.setLocation(scaleFactor.getX(), grp.getDataAsDouble(42));
            insert.setScaleFactor(scaleFactor);
        } else {
            insert.setScaleFactor(scaleFactor);
        }

        if (grp.hasCode(43)) {
            scaleFactor = new Point3D(scaleFactor.getX(), scaleFactor.getY(),
                                      grp.getDataAsDouble(43));
            insert.setScaleFactor(scaleFactor);
        } else {
            insert.setScaleFactor(scaleFactor);
        }

        if (grp.hasCode(50)) {
            rotAngle = grp.getDataAsDouble(50);
            insert.setRotAngle(rotAngle);
        }

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doubz = new Double(z);
        feature.setProp("elevation", doubz.toString());
        secondFeat.setProp("elevation", doubz.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        insert.setBlkList(blkList);

        insert.encuentraBloque(blockName);

        insert.add(new Point3D(x, y, z));
        secondGeom.add(new Point3D(x, y, z));

        feature.setGeometry(insert);
        secondFeat.setGeometry(secondGeom);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);
        completeAttributes(secondFeat);

        /*for (int i=0;i<attributes.size();i++) {
            String[] att = new String[2];
            att = (String[])attributes.get(i);
            secondFeat.setProp(att[0],att[1]);
        }*/
        if ((insert.getBlockFound() == true) && (attributesFollowFlag != 1)) {
            gestionaInsert(feature);
        }

        //if (addingToBlock == false) {
        //features.add(secondFeat);
        //}
        //if (addingToBlock == true/* && insert.getBlockFound() == true*/) {
        //System.out.println("createInsert(): Añadimos un insert al bloque " + iterator);
        //blk.add(feature);
        //}
        // 041129: Añadido para implementar los ATTRIBS.
        if (attributesFollowFlag == 1) {
            isDoubleFeatured = true;
            lastFeaBordes = feature;
            lastFeaFondos = secondFeat;
        } else {
            if (addingToBlock == false) {
                features.add(secondFeat);
            }

            if (addingToBlock == true /* && insert.getBlockFound() == true*/) {
                //System.out.println("createInsert(): Añadimos un insert al bloque " + iterator);
                blk.add(feature);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createSolid(org.cresques.io.DxfGroupVector)
     */
    public void createSolid(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z1 = 0.0;
        double z2 = 0.0;
        double z3 = 0.0;
        double z4 = 0.0;
        DxfGroup g = null;

        //Point2D pt1 = null, pt2 = null, pt3 = null, pt4 = null;
        Point3D[] pts = new Point3D[4];

        //DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();

        //double elev = 0;
        feaBordes.setProp("dxfEntity", "Solid");
        feaFondos.setProp("dxfEntity", "Solid");

        if (grp.hasCode(8)) {
            feaBordes.setProp("layer", grp.getDataAsString(8));
        }

        feaFondos.setProp("layer", grp.getDataAsString(8));
        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z1 = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)) {
            z1 = grp.getDataAsDouble(30);
        } else {
            Double doub = new Double(0.0);
            feaBordes.setProp("elevation", doub.toString());
            feaFondos.setProp("elevation", doub.toString());
        }*/
        Point2D pto = proj.createPoint(x, y);
        Point3D pto3D = new Point3D(pto.getX(), pto.getY(), z1);
        pts[0] = pto3D;
        x = grp.getDataAsDouble(11);
        y = grp.getDataAsDouble(21);
        z2 = grp.getDataAsDouble(31);
        pto = proj.createPoint(x, y);
        pto3D = new Point3D(pto.getX(), pto.getY(), z2);
        pts[1] = pto3D;
        x = grp.getDataAsDouble(12);
        y = grp.getDataAsDouble(22);
        z3 = grp.getDataAsDouble(32);
        pto = proj.createPoint(x, y);
        pto3D = new Point3D(pto.getX(), pto.getY(), z3);
        pts[2] = pto3D;

        if (grp.hasCode(13)) {
            x = grp.getDataAsDouble(13);
        }

        if (grp.hasCode(23)) {
            y = grp.getDataAsDouble(23);
        }

        if (grp.hasCode(33)) {
            z4 = grp.getDataAsDouble(33);
        }

        pto = proj.createPoint(x, y);
        pto3D = new Point3D(pto.getX(), pto.getY(), z4);
        pts[3] = pto3D;

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in1 = new Point3D(pts[0].getX(), pts[0].getY(), z1);
        Point3D point_in2 = new Point3D(pts[1].getX(), pts[1].getY(), z2);
        Point3D point_in3 = new Point3D(pts[2].getX(), pts[2].getY(), z3);
        Point3D point_in4 = new Point3D(pts[3].getX(), pts[3].getY(), z4);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out1 = DxfCalXtru.CalculateXtru(point_in1, xtru);
        Point3D point_out2 = DxfCalXtru.CalculateXtru(point_in2, xtru);
        Point3D point_out3 = DxfCalXtru.CalculateXtru(point_in3, xtru);
        Point3D point_out4 = DxfCalXtru.CalculateXtru(point_in4, xtru);
        pts[0] = new Point3D(point_out1);
        pts[1] = new Point3D(point_out2);
        pts[2] = new Point3D(point_out3);
        pts[3] = new Point3D(point_out4);

        if ((pts[0].getZ() != 0.0) || (pts[1].getZ() != 0.0) ||
                (pts[2].getZ() != 0.0) || (pts[3].getZ() != 0.0)) {
            dxf3DFile = true;
        }

        Point3D aux = new Point3D(pts[2]);
        pts[2] = new Point3D(pts[3]);
        pts[3] = aux;

        Double doub = new Double(0.0);
        if ((pts[0].getZ() == pts[1].getZ()) &&
                (pts[1].getZ() == pts[2].getZ()) &&
                (pts[2].getZ() == pts[3].getZ())) {
            doub = new Double(pts[0].getZ());
        }
        String string = doub.toString();
        feaBordes.setProp("elevation", string);
        feaFondos.setProp("elevation", string);

        for (int i = 0; i < pts.length; i++) {
            lineString3D.add(pts[i]);
            polygon3D.add(pts[i]);
        }

        // Para cerrarlos.
        lineString3D.add(pts[0]);
        polygon3D.add(pts[0]);

        feaBordes.setGeometry(lineString3D);
        feaFondos.setGeometry(polygon3D);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feaBordes);
        completeAttributes(feaFondos);

        //features.add(feature);
        if (addingToBlock == false) {
            //System.out.println("createSolid(): Añade un solid a la lista de entidades");
            //features.add(feaBordes);
            features.add(feaFondos);
        } else {
            //System.out.println("createSolid(): Añadimos un circulo al bloque " + iterator);
            //blk.add(feaBordes);
            blk.add(feaFondos);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createSpline(org.cresques.io.DxfGroupVector)
     */
    public void createSpline(DxfGroupVector grp) throws Exception {
        double x = 0.0; //elev = 0.0;
        double y = 0.0; //elev = 0.0;
        double z = 0.0; //elev = 0.0;

        //double elev = 0.0;
        DxfGroup g = null;
        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();

        //Geometry geometria;
        //Feature feature= new Feature();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();
        int flags = 0;

        //feature.setProp("dxfEntity", "LwPolyline");
        feaBordes.setProp("dxfEntity", "Spline");
        feaFondos.setProp("dxfEntity", "Spline");

        if (grp.hasCode(8)) {
            //feature.setProp("layer", grp.getDataAsString(8));
            feaBordes.setProp("layer", grp.getDataAsString(8));
            feaFondos.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();

            //feature.setProp("thickness", string);
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);

            //feature.setProp("thickness", doub.toString());
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();

            //feature.setProp("color", string);
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(70)) {
            flags = grp.getDataAsInt(70);
        }

        if ((flags & 0x01) == 0x01) {
            //geometria = new Polygon();
            feaBordes.setGeometry(lineString3D);
            feaFondos.setGeometry(polygon3D);
            isDoubleFeatured = true;
        } else {
            //geometria = new LineString();
            feaBordes.setGeometry(lineString3D);
            isDoubleFeatured = false;
        }

        int j = 0;
        double firstX = 0.0;
        double firstY = 0.0;
        double firstZ = 0.0;

        for (int i = 0; i < grp.size(); i++) {
            g = (DxfGroup) grp.get(i);

            if (g.getCode() == 10) {
                j++;
                x = ((Double) g.getData()).doubleValue();
            } else if (g.getCode() == 20) {
                y = ((Double) g.getData()).doubleValue();
            } else if (g.getCode() == 30) {
                z = ((Double) g.getData()).doubleValue();

                Point2D p = proj.createPoint(x, y);
                Point3D p3d = new Point3D(p.getX(), p.getY(), z);
                lineString3D.add(p3d);

                if (isDoubleFeatured) {
                    polygon3D.add(p3d);
                }

                if (j == 1) {
                    firstX = x;
                    firstY = y;
                    firstZ = z;
                }

                //elev = z;
                x = 0.0;
                y = 0.0;
                z = 0.0;
            }
        }

        //Double doub = new Double(elev);
        //String string = doub.toString();
        //feature.setProp("elevation", string);
        //feaBordes.setProp("elevation", string);
        //feaFondos.setProp("elevation", string);
        if (isDoubleFeatured) {
            Point2D p = proj.createPoint(firstX, firstY);
            Point3D p3d = new Point3D(p.getX(), p.getY(), z);
            lineString3D.add(p3d);
            polygon3D.add(p3d);
        }

        double zprev = 0;
        boolean constSplineElev = true;

        for (int i = 0; i < lineString3D.pointNr(); i++) {
            z = lineString3D.getPoint3D(i).getZ();

            if (z != 0.0) {
                dxf3DFile = true;
            }

            if ((i > 0) && (z != zprev)) {
                constSplineElev = false;
            }

            zprev = z;
        }

        if (constSplineElev) {
            Double doub = new Double(lineString3D.getPoint3D(0).getZ());
            String string = doub.toString();
            feaBordes.setProp("elevation", string);

            if (isDoubleFeatured) {
                feaFondos.setProp("elevation", string);
            }
        } else {
            Double doub = new Double(0.0);
            String string = doub.toString();
            feaBordes.setProp("elevation", string);

            if (isDoubleFeatured) {
                feaFondos.setProp("elevation", string);
            }
        }

        lastFeaBordes = feaBordes;

        if (isDoubleFeatured) {
            lastFeaFondos = feaFondos;
        }

        // 041130: Rellena las props con los atributos.
        completeAttributes(feaBordes);
        completeAttributes(feaFondos);

        //features.add(feature);
        if (addingToBlock == false) {
            if (isDoubleFeatured) {
                features.add(feaFondos);
            } else {
                features.add(feaBordes);
            }
        } else {
            //System.out.println("createLwPolyline(): Añadimos una lwpolilinea al bloque " + iterator);
            if (isDoubleFeatured) {
                blk.add(feaFondos);
            } else {
                blk.add(feaBordes);
            }
        }

        isDoubleFeatured = false;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createAttdef(org.cresques.io.DxfGroupVector)
     */
    public void createAttdef(DxfGroupVector grp) throws Exception {
        DxfGroup g = null;

        String defaultValue = "";
        String tagString = "";
        String textStyleName = "";
        String[] attribute = new String[2];
        boolean tagDefined = false;
        boolean defValDefined = false;

        if (grp.hasCode(1)) {
            defaultValue = grp.getDataAsString(1);
            attribute[1] = DxfConvTexts.ConvertText(defaultValue);
            defValDefined = true;

            if (tagDefined) {
                attributes.add(attribute);
            }
        }

        if (grp.hasCode(2)) {
            tagString = grp.getDataAsString(2);
            attribute[0] = DxfConvTexts.ConvertText(tagString);
            tagDefined = true;

            if (defValDefined) {
                attributes.add(attribute);
            }
        }

        if (grp.hasCode(7)) {
            textStyleName = grp.getDataAsString(7);
            textStyleName = DxfConvTexts.ConvertText(textStyleName);
        }

        setNewAttributes();
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createAttrib(org.cresques.io.DxfGroupVector)
     */
    public void createAttrib(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double h = 0.0;
        double rot = 0.0;
        DxfGroup g = null;

        //Point2D pt1 = null, pt2 = null;
        Point3D pt = null;
        org.cresques.px.gml.Point3D point = new org.cresques.px.gml.Point3D();

        point.setTextPoint(true);

        String defaultValue = "";
        String tagString = "";
        String textStyleName = "";
        String[] att = new String[2];
        boolean tagDefined = false;
        boolean defValDefined = false;
        int attributeFlags = 0;

        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        Feature insFea = lastFeaBordes;
        Feature ptFea = lastFeaFondos;

        Feature feature = new Feature();

        feature.setProp("dxfEntity", "Attrib"); // <-- Es un INSERT con attributes.

        if (grp.hasCode(8)) {
            feature.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feature.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feature.setProp("color", string);
            feature.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(1)) {
            String strAux1 = grp.getDataAsString(1);
            strAux1 = DxfConvTexts.ConvertText(strAux1);
            defaultValue = strAux1;
            att[1] = DxfConvTexts.ConvertText(defaultValue);
            defValDefined = true;

            if (tagDefined) {
                insFea.setProp(att[0], att[1]);
                ptFea.setProp(att[0], att[1]);
            }

            feature.setProp("text", strAux1);
        }

        if (grp.hasCode(2)) {
            String strAux2 = grp.getDataAsString(2);
            strAux2 = DxfConvTexts.ConvertText(strAux2);
            tagString = strAux2;
            att[0] = DxfConvTexts.ConvertText(tagString);
            tagDefined = true;

            if (defValDefined) {
                insFea.setProp(att[0], att[1]);
                ptFea.setProp(att[0], att[1]);
            }
        }

        if (grp.hasCode(7)) {
            textStyleName = grp.getDataAsString(7);
            textStyleName = DxfConvTexts.ConvertText(textStyleName);
        }

        if (grp.hasCode(70)) {
            attributeFlags = grp.getDataAsInt(70);
        }

        if (grp.hasCode(40)) {
            Double heightD = new Double(grp.getDataAsDouble(40));
            String heightS = heightD.toString();
            feature.setProp("textHeight", heightS);
        } else {
            feature.setProp("textHeight", "20.0");
        }

        if (grp.hasCode(50)) {
            Double rotD = new Double(grp.getDataAsDouble(50));
            String rotS = rotD.toString();
            feature.setProp("textRotation", rotS);
        } else {
            feature.setProp("textRotation", "0.0");
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

        /*if (grp.hasCode(30)){
            z = grp.getDataAsDouble(30);
            Double doub = new Double(z);
            String string = doub.toString();
            feature.setProp("elevation", string);
        } else {
            Double doub = new Double(0.0);
            feature.setProp("elevation", doub.toString());
        }*/
        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        z = point_out.getZ();

        Double doub = new Double(z);
        feature.setProp("elevation", doub.toString());

        if (z != 0.0) {
            dxf3DFile = true;
        }

        point.add(new Point3D(x, y, z));
        feature.setGeometry(point);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feature);

        if (attributeFlags == 8) {
            if (addingToBlock == false) {
                features.add(feature);
            } else {
                blk.add(feature);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createBlock(org.cresques.io.DxfGroupVector)
     */
    public void createBlock(DxfGroupVector grp) throws Exception {
        //DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        blk = new FeatureCollection(proj);

        Point3D basePoint = new Point3D();
        String blockName = "";

        //System.out.println("createBlock(): Creamos nuevo bloque, el bloque " + iterator);
        addingToBlock = true;

        //System.out.println("createBlock(): Añadimos el bloque " + iterator + " a la lista de bloques");
        blkList.add(iterator, blk);

        //System.out.println("createBlock(): Rellenamos la informacion del bloque " + iterator);
        if (grp.hasCode(8)) {
            blk.setProp("layer", grp.getDataAsString(8));
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            blk.setProp("color", string);
            blk.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            blk.setProp("color", string);
            blk.setProp("colorByLayer", "true");
        }

        if (grp.hasCode(1)) {
            blockName = grp.getDataAsString(1);

            //blk.setBlkName(blockName);
            blk.setProp("blockName", blockName);
        }

        if (grp.hasCode(2)) {
            blockName = grp.getDataAsString(2);

            //blk.setBlkName(blockName);
            blk.setProp("blockName", blockName);
        }

        if (grp.hasCode(10)) {
            Double basePointX = new Double(grp.getDataAsDouble(10));
            basePoint.X = grp.getDataAsDouble(10);
            blk.setProp("basePointX", basePointX.toString());
        }

        if (grp.hasCode(20)) {
            Double basePointY = new Double(grp.getDataAsDouble(20));
            basePoint.Y = grp.getDataAsDouble(20);
            blk.setProp("basePointY", basePointY.toString());
        }

        if (grp.hasCode(30)) {
            Double basePointZ = new Double(grp.getDataAsDouble(30));
            basePoint.Z = grp.getDataAsDouble(30);

            if (basePoint.getZ() != 0.0) {
                dxf3DFile = true;
            }

            blk.setProp("basePointZ", basePointZ.toString());
        }

        if (grp.hasCode(70)) {
            //blk.flags = grp.getDataAsInt(70);
            Integer blockFlags = new Integer(grp.getDataAsInt(70));
            blk.setProp("blockFlags", blockFlags.toString());

            // 041103: Hoy por hoy esto no lo utilizamos.
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#endBlk(org.cresques.io.DxfGroupVector)
     */
    public void endBlk(DxfGroupVector grp) throws Exception {
        //DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        setAddingToBlock(false);
        iterator = iterator + 1;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#testBlocks()
     */
    public void testBlocks() {
        //System.out.println("getBlkList() = " + getBlkList());
        Vector blkList = getBlkList();
        FeatureCollection block = null;
        Feature feature = null;
        InsPoint3D insert = null;

        for (int i = 0; i < blkList.size(); i++) {
            //System.out.println("compruebaBloques(): Bloque " + i +" de " + blkList.size());
            block = (FeatureCollection) blkList.get(i);

            int aux = block.size();

            for (int j = 0; j < aux; j++) {
                feature = (Feature) block.get(j);

                //if (feature.getGeometry() instanceof InsPoint && feature.getProp("isAnInsert") == "true") {
                if (feature.getGeometry() instanceof InsPoint3D) {
                    insert = (InsPoint3D) feature.getGeometry();

                    String nomBlock = insert.getBlockName();

                    if (insert.getBlockFound() == false) {
                        //System.out.println("compruebaBloques(): Ahora se ocupa del DxfInsert " + nomBlock);
                        insert.encuentraBloque(nomBlock);

                        //gestionaInsert(feature);
                        //block.add(feature);
                    }
                }
            }
        }
    }

    /**
     * Método que permite incluir en la lista general de objetos los objetos que se
     * encuentran dentro del bloque referenciado por cada InsPoint3D.
     * @param feature, el punto de inserción.
     */
    private void gestionaInsert(Feature feature) {
        Feature feature2 = null;
        org.cresques.px.gml.Point3D point = null;
        LineString3D lineString3D = null;
        Polygon3D polygon3D = null;
        InsPoint3D insert = new InsPoint3D();
        insert = (InsPoint3D) feature.getGeometry();

        double bPointX = 0.0;
        double bPointY = 0.0;
        double bPointZ = 0.0;

        if (insert.getBlockFound() != true)
        {
        	System.err.println("BLOQUE NO ENCONTRADO !!!" + insert.getBlockName() + " " + insert.toString());
        	return;
        }

        bPointX = Double.parseDouble(insert.getBlock().getProp("basePointX"));
        bPointY = Double.parseDouble(insert.getBlock().getProp("basePointY"));
        bPointZ = Double.parseDouble(insert.getBlock().getProp("basePointZ"));

        //}
        double sFactorX = insert.getScaleFactor().getX();
        double sFactorY = insert.getScaleFactor().getY();
        double sFactorZ = insert.getScaleFactor().getZ();
        double rAngleGra = insert.getRotAngle();
        double rAngleRad = (rAngleGra * Math.PI) / 180.0;
        InsPoint3D insert2 = null;

        for (int i = 0; i < insert.getBlock().size(); i++) {
            //System.out.println("gestionaInserts: insert.getBlock().features.size() = " + insert.getBlock().size());
            feature2 = (Feature) insert.getBlock().get(i);

            // Para que los elementos dentro del bloque tengan la misma layer
            // y color que el insert al que corresponden.
            // Segun la especificacion del formato DXF de Autodesk, la layer
            // de las entities dentro de un bloque es la del bloque. La
            // layer especifica para estos elementos en la defincion del
            // bloque se ignora.
            //System.out.println("gestionaInsert(): layer = " + feature2.getProp("layer"));
            if ((feature2.getProp("colorByLayer").equals("false") ||
                    feature2.getProp("layer").equals("0")) &&
                    !feature.getProp("layer").equals("0")) {
                feature2.setProp("color", feature.getProp("color"));
            }

            feature2.setProp("layer", feature.getProp("layer"));

            Point3D point1 = new Point3D();
            Point3D point11 = new Point3D();
            Point3D pointAux = null;

            if (feature2.getGeometry() instanceof InsPoint3D) {
                //System.out.println("gestionaInsert(): Encuentra bloques dentro de bloques");
                insert2 = (InsPoint3D) feature2.getGeometry();
                point1 = insert2.getPoint3D(0);

                pointAux = new Point3D(point1.getX() - bPointX,
                                       point1.getY() - bPointY,
                                       point1.getZ() - bPointZ);

                double laX = insert.getPoint3D(0).getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = insert.getPoint3D(0).getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                double laZ = insert.getPoint3D(0).getZ() +
                             (pointAux.getZ() * sFactorZ);
                point11 = new Point3D(laX, laY, laZ);

                InsPoint3D insert3 = new InsPoint3D();

                insert3.add(point11);

                insert3.setBlkList(insert2.getBlkList());
                insert3.setBlock(insert2.getBlock());
                insert3.setBlockName(insert2.getBlockName());
                insert3.setRotAngle(insert2.getRotAngle());

                Point3D newScale = new Point3D(insert2.getScaleFactor().getX() * sFactorX,
                                               insert2.getScaleFactor().getY() * sFactorY,
                                               insert2.getScaleFactor().getZ() * sFactorZ);
                insert3.setScaleFactor(newScale);

                Feature feature3 = new Feature();
                feature3.setProp("layer", feature2.getProp("layer"));
                feature3.setProp("color", feature2.getProp("color"));
                feature3.setProp("dxfEntity", feature2.getProp("dxfEntity"));
                feature3.setProp("elevation", feature2.getProp("elevation"));

                //041130
                for (int j = 0; j < attributes.size(); j++) {
                    String[] att = new String[2];
                    att = (String[]) attributes.get(j);
                    feature3.setProp(att[0], feature2.getProp(att[0]));
                }

                feature3.setGeometry(insert3);

                gestionaInsert(feature3);
            } else if (feature2.getGeometry() instanceof LineString3D) {
                lineString3D = (LineString3D) feature2.getGeometry();

                LineString3D lineString2 = new LineString3D();
                Point3D[] points = new Point3D[lineString3D.pointNr()];
                Point3D[] pointss = new Point3D[lineString3D.pointNr()];

                for (int j = 0; j < lineString3D.pointNr(); j++) {
                    points[j] = (Point3D) lineString3D.get(j);
                    pointss[j] = new Point3D();

                    pointAux = new Point3D(points[j].getX() - bPointX,
                                           points[j].getY() - bPointY,
                                           points[j].getZ() - bPointZ);

                    double laX = insert.getPoint3D(0).getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = insert.getPoint3D(0).getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    double laZ = insert.getPoint3D(0).getZ() +
                                 (pointAux.getZ() * sFactorZ);
                    pointss[j] = new Point3D(laX, laY, laZ);
                    lineString2.add(pointss[j]);
                }

                Feature feature3 = new Feature();
                feature3.setProp("layer", feature2.getProp("layer"));
                feature3.setProp("color", feature2.getProp("color"));
                feature3.setProp("dxfEntity", feature2.getProp("dxfEntity"));

                if (feature2.getProp("elevation") != null) {
                    feature3.setProp("elevation", feature2.getProp("elevation"));
                }

                feature3.setProp("thickness", feature2.getProp("thickness"));

                //041130
                for (int j = 0; j < attributes.size(); j++) {
                    String[] att = new String[2];
                    att = (String[]) attributes.get(j);

                    String str = att[0];
                    feature3.setProp(str, feature2.getProp(str));
                }

                feature3.setGeometry(lineString2);

                if (addingToBlock == false) {
                    features.add(feature3);
                }
            } else if (feature2.getGeometry() instanceof Polygon3D) {
                polygon3D = (Polygon3D) feature2.getGeometry();

                Polygon3D polygon2 = new Polygon3D();
                Point3D[] points = new Point3D[polygon3D.pointNr()];
                Point3D[] pointss = new Point3D[polygon3D.pointNr()];

                for (int j = 0; j < polygon3D.pointNr(); j++) {
                    points[j] = (Point3D) polygon3D.get(j);
                    pointss[j] = new Point3D();

                    pointAux = new Point3D(points[j].getX() - bPointX,
                                           points[j].getY() - bPointY,
                                           points[j].getZ() - bPointZ);

                    double laX = insert.getPoint3D(0).getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = insert.getPoint3D(0).getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    double laZ = insert.getPoint3D(0).getZ() +
                                 (pointAux.getZ() * sFactorZ);
                    pointss[j] = new Point3D(laX, laY, laZ);
                    polygon2.add(pointss[j]);
                }

                Feature feature3 = new Feature();
                feature3.setProp("layer", feature2.getProp("layer"));
                feature3.setProp("color", feature2.getProp("color"));
                feature3.setProp("dxfEntity", feature2.getProp("dxfEntity"));

                if (feature2.getProp("elevation") != null) {
                    feature3.setProp("elevation", feature2.getProp("elevation"));
                }

                feature3.setProp("thickness", feature2.getProp("thickness"));

                //041130
                for (int j = 0; j < attributes.size(); j++) {
                    String[] att = new String[2];
                    att = (String[]) attributes.get(j);
                    feature3.setProp(att[0], feature2.getProp(att[0]));
                }

                feature3.setGeometry(polygon2);

                if (addingToBlock == false) {
                    features.add(feature3);
                }
            } else if (feature2.getGeometry() instanceof org.cresques.px.gml.Point3D) {
                point = (org.cresques.px.gml.Point3D) feature2.getGeometry();
                point1 = point.getPoint3D(0);

                pointAux = new Point3D(point1.getX() - bPointX,
                                       point1.getY() - bPointY,
                                       point1.getZ() - bPointZ);

                double laX = insert.getPoint3D(0).getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = insert.getPoint3D(0).getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                double laZ = insert.getPoint3D(0).getZ() +
                             (pointAux.getZ() * sFactorZ);
                point11 = new Point3D(laX, laY, laZ);

                org.cresques.px.gml.Point3D pointt = new org.cresques.px.gml.Point3D();
                pointt.add(point11);

                Feature feature3 = new Feature();
                feature3.setProp("layer", feature2.getProp("layer"));
                feature3.setProp("color", feature2.getProp("color"));
                feature3.setProp("dxfEntity", feature2.getProp("dxfEntity"));
                feature3.setProp("elevation", feature2.getProp("elevation"));
                feature3.setProp("thickness", feature2.getProp("thickness"));

                if (point.isTextPoint()) {
                    feature3.setProp("text", feature2.getProp("text"));
                    feature3.setProp("textHeight",
                                     feature2.getProp("textHeight"));

                    double auxR = Double.parseDouble(feature2.getProp("textRotation"));

                    //System.out.println("auxR = " + auxR);
                    auxR = auxR + rAngleGra;
                    feature3.setProp("textRotation", Double.toString(auxR));

                    //System.out.println("gestionaInsert(): feature3.getProp(textRotation) = " + feature3.getProp("textRotation"));
                    pointt.setTextPoint(true);
                }

                //041130
                for (int j = 0; j < attributes.size(); j++) {
                    String[] att = new String[2];
                    att = (String[]) attributes.get(j);
                    feature3.setProp(att[0], feature2.getProp(att[0]));
                }

                feature3.setGeometry(pointt);

                //if (addingToBlock == false) {
                features.add(feature3);

                //}
            } else {
                System.out.println("gestionaInsert(): Encontrado elemento desconocido");
            }
        }
    }

    /**
     * Gestiona la característica faces en las polilíneas.
     * @param face
     */
    private void addFace(int[] face) {
        hasFaces = true;

        if (faces == null) {
            faces = new Vector();
        }

        faces.add(face);
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#depureAttributes()
     */
    public void depureAttributes() {
        //String[] att = null;

        /*Set conjunto = new HashSet();
        for (int i=0;i<attributes.size();i++) {
            att = (String[])attributes.get(i);
            String str = att[0];
            conjunto.add(str);
        }
        //conjunto.addAll(attributes);
        Vector atts = new Vector();
        Vector atts2 = new Vector();
        atts.addAll(conjunto);
        for (int i=0;i<atts.size();i++) {
            if (((String[])(attributes.get(i)))[0] == atts.get(i)) {
                atts2.add(i, attributes.get(i));
            }
        }*/
        String[] lastAtt = new String[2];

        for (int i = 0; i < attributes.size(); i++) {
            String[] att = (String[]) attributes.get(i);

            for (int j = i + 1; j < attributes.size(); j++) {
                //System.out.println("depureAttributes(): j = " + j);
                String[] st = (String[]) attributes.get(j);
                String st1 = att[0];
                String st2 = st[0];

                //System.out.println("depureAttributes(): st1 = " + st1);
                //System.out.println("depureAttributes(): st2 = " + st2);
                if (st2.equals(st1)) {
                    //System.out.println("depureAttributes(): Borra el st2");
                    attributes.remove(j);
                }

                if (i == (attributes.size() - 1)) {
                    lastAtt = att;
                }
            }
        }

        for (int i = attributes.size() - 2; i >= 0; i--) {
            String[] att = (String[]) attributes.get(i);
            String st1 = lastAtt[0];
            String st2 = att[0];

            if (st2.equals(st1)) {
                attributes.remove(i);
            }
        }

        /*String[] attStrs = new String[attributes.size()];
        Vector attribs = new Vector();
        for (int i=0;i<attributes.size();i++) {
            att = (String[])attributes.get(i);
            attStrs[i] = att[0];
        }
        Set attStrsNR = new HashSet();
        for (int i=0;i<attStrs.length;i++) {
            attStrsNR.add(attStrs[i]);
        }
        String[] attStrsNRA = new String[attStrsNR.size()];
        attStrsNR.toArray(attStrsNRA);
        for (int i=0;i<attStrsNR.size();i++) {
            att[0] = attStrsNRA[i];
            attribs.add(att);
        }
        for (int i=0;i<attributes.size();i++) {
            att = (String[])attributes.get(i);
            String[] att2 = new String[2];
            for (int j=0;j<attribs.size();j++) {
                att2 = (String[])attribs.get(j);
                if (att[0].equals(att2[0])) {
                    att2[1] = att[1];
                }
                attribs.set(j, att2);
            }
        }
        attributes = attribs;*/
    }

    /**
     * Hace los setProp para los atributos extra de las entidades.
     * @param feature
     */
    private void completeAttributes(Feature feature) {
        // 041130: Rellena las props con los atributos.
        for (int i = 0; i < attributes.size(); i++) {
            String[] att = new String[2];
            att = (String[]) attributes.get(i);
            feature.setProp(att[0], att[1]);
        }
    }

    /**
     * Copia los atributos extra cargados en InsPoint y ya perfectamente definidos a los
     * elementos del bloque al que referencia el punto de insercion.
     * @param feaInsert, punto de inserción.
     */
    private void copyAttributes(Feature feaInsert) {
        Feature feature = null;
        InsPoint3D insert = new InsPoint3D();
        insert = (InsPoint3D) feaInsert.getGeometry();

        for (int i = 0; i < insert.getBlock().size(); i++) {
            feature = (Feature) insert.getBlock().get(i);

            for (int j = 0; j < attributes.size(); j++) {
                String[] att = new String[2];
                att = (String[]) attributes.get(j);
                feature.setProp(att[0], feaInsert.getProp(att[0]));
            }
        }
    }

    /**
     * Se ejecuta cuando se declaran nuevos atributos, es decir, tras cada ATTDEF.
     * Añade estos atributos a las features existentes.
     */
    private void setNewAttributes() {
        for (int i = 0; i < features.size(); i++) {
            Feature fea = new Feature();
            fea = (Feature) features.get(i);
            completeAttributes(fea);
        }

        for (int i = 0; i < blkList.size(); i++) {
            FeatureCollection bloque = new FeatureCollection(proj);
            bloque = (FeatureCollection) blkList.get(i);

            for (int j = 0; j < bloque.size(); j++) {
                Feature fea = new Feature();
                fea = (Feature) bloque.get(j);
                completeAttributes(fea);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getAttributes()
     */
    public Vector getAttributes() {
        return attributes;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getExtent()
     */
    public Extent getExtent() {
        Feature feature = new Feature();
        Extent extent = new Extent();
        Iterator iter = features.iterator();

        while (iter.hasNext()) {
            feature = (Feature) iter.next();
            extent.add(feature.getExtent());
        }

        return extent;
    }

    /**
     * Establece la proyección cartográfica en la que se van a crear las features.
     * @param p, Proyección cartográfica.
     */
    public void setProjection(IProjection p) {
        proj = p;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#reProject(org.cresques.geo.ReProjection)
     */
    public void reProject(ICoordTrans rp) {
        Feature feature = new Feature();
        Extent extent = new Extent();
        Iterator iter = features.iterator();

        while (iter.hasNext()) {
            feature = (Feature) iter.next();
            ((Projected) feature).reProject(rp);
            extent.add(feature.getExtent());
        }

        setProjection(rp.getPDest());
    }

    /* (non-Javadoc)
     * @see org.cresques.geo.Projected#getProjection()
     */
    public IProjection getProjection() {
        return proj;
    }

    /**
     * Devuelve las features creadas.
     * @return IObjList
     */
    public IObjList getObjects() {
        return features;
    }

    /**
     * Permite dibujar las features creadas.
     */
    public void draw(Graphics2D g, ViewPortData vp) {
        Iterator iter = features.iterator();
        Extent extent;

        while (iter.hasNext()) {
            Feature feature = new Feature();
            feature = (Feature) iter.next();
            extent = feature.getExtent();

            if (vp.getExtent().minX() > extent.maxX()) {
                continue;
            }

            if (vp.getExtent().minY() > extent.maxY()) {
                continue;
            }

            if (vp.getExtent().maxX() < extent.minX()) {
                continue;
            }

            if (vp.getExtent().maxY() < extent.minY()) {
                continue;
            }

            //if (!feature.layer.frozen)
            feature.draw(g, vp);
        }
    }

    /**
     * Invoca el método de creación de arcos para polilíneas con parámetros de
     * curvatura.
     * @param coord1, punto inicial del arco.
     * @param coord2, punto final del arco.
     * @param bulge, parámetro de curvatura.
     * @return Vector con los puntos del arco.
     */
    public static Vector createArc(Point2D coord1, Point2D coord2, double bulge) {
        return new DxfCalArcs(coord1, coord2, bulge).getPoints(1);
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getBlkList()
     */
    public Vector getBlkList() {
        return blkList;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getDxfEntityList()
     */
    public DxfEntityList getDxfEntityList() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getBlk()
     */
    public DxfBlock getBlk() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createEllipse(org.cresques.io.DxfGroupVector)
     */
    public void createEllipse(DxfGroupVector grp) throws Exception {
        double cx = 0.0;
        double cy = 0.0;
        double cz = 0.0;

        double x_end_point_major_axis = 0.0;
        double y_end_point_major_axis = 0.0;
        double z_end_point_major_axis = 0.0;

        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;
        double ratio_minor_to_major_axis = 1.0;
        double start = 0.0;
        double end = 2* Math.PI;
        LineString3D lineString3D = new LineString3D();
        Polygon3D polygon3D = new Polygon3D();
        Feature feaBordes = new Feature();
        Feature feaFondos = new Feature();

        feaBordes.setProp("dxfEntity", "Ellipse");
        feaFondos.setProp("dxfEntity", "Ellipse");

        if (grp.hasCode(8)) {
            feaBordes.setProp("layer", grp.getDataAsString(8));
        }

        feaFondos.setProp("layer", grp.getDataAsString(8));

        if (grp.hasCode(39)) {
            Double doub = new Double(grp.getDataAsDouble(39));
            String string = doub.toString();
            feaBordes.setProp("thickness", string);
            feaFondos.setProp("thickness", string);
        } else {
            Double doub = new Double(0.0);
            feaBordes.setProp("thickness", doub.toString());
            feaFondos.setProp("thickness", doub.toString());
        }

        if (grp.hasCode(62)) {
            Integer integer = new Integer(grp.getDataAsInt(62));
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "false");
            feaFondos.setProp("colorByLayer", "false");
        } else {
            DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
            int clr = layer.colorNumber;
            Integer integer = new Integer(clr);
            String string = integer.toString();
            feaBordes.setProp("color", string);
            feaFondos.setProp("color", string);
            feaBordes.setProp("colorByLayer", "true");
            feaFondos.setProp("colorByLayer", "true");
        }

        // Center point
        cx = grp.getDataAsDouble(10);
        cy = grp.getDataAsDouble(20);
        if (grp.hasCode(30)) {
            cz = grp.getDataAsDouble(30);
        }

        // end_point_major_axis  (RELATIVE to the center)
        x_end_point_major_axis = grp.getDataAsDouble(11);
        y_end_point_major_axis = grp.getDataAsDouble(21);
        if (grp.hasCode(31)) {
            z_end_point_major_axis = grp.getDataAsDouble(31);
        }

        // ratio minor axis to major axis
        if (grp.hasCode(40)) {
        	ratio_minor_to_major_axis = grp.getDataAsDouble(40);
        }
        Point2D c = proj.createPoint(cx, cy);
        // Point2D end_major = proj.createPoint(x_end_point_major_axis, y_end_point_major_axis);
        // double r_major_axis_2D = c.distance(end_major)/2.0;
        double r_major_axis_2D = Math.sqrt(x_end_point_major_axis*
        		x_end_point_major_axis +
        		y_end_point_major_axis * y_end_point_major_axis);
        double r_minor_axis_2D = r_major_axis_2D * ratio_minor_to_major_axis;
        double rotation_angle = Math.atan2(y_end_point_major_axis , x_end_point_major_axis);

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsDouble(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsDouble(230);
        }

        Point3D point_in = new Point3D(cx, cy, cz);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        cx = point_out.getX();
        cy = point_out.getY();
        cz = point_out.getZ();

        Double doub = new Double(cz);
        feaBordes.setProp("elevation", doub.toString());
        feaFondos.setProp("elevation", doub.toString());

        if (cz != 0.0) {
            dxf3DFile = true;
        }


        Point3D center = new Point3D(c.getX(), c.getY(), cz);
        Point3D[] pts = new Point3D[360];
        int angulo = 0;

        for (angulo = 0; angulo < 360; angulo++) {
            pts[angulo] = new Point3D(center.getX() +
                                      r_major_axis_2D * Math.cos(Math.toRadians(angulo)),
                                      center.getY() +
                                      r_minor_axis_2D * Math.sin(Math.toRadians(angulo)),
                                      center.getZ());

            if (pts.length == 1) {
                firstPt = pts[angulo];
            }
        }

        AffineTransform at = new AffineTransform();
        at.rotate(rotation_angle, cx, cy);
        for (int i = 0; i < pts.length; i++) {
        	Point2D pAux = pts[i];
        	Point2D pRot = at.transform(pAux, null);
            lineString3D.add(pRot);
            polygon3D.add(pRot);
        }

        feaBordes.setGeometry(lineString3D);
        feaFondos.setGeometry(polygon3D);

        // 041130: Rellena las props con los atributos.
        completeAttributes(feaBordes);
        completeAttributes(feaFondos);

        //features.add(feature);
        if (addingToBlock == false) {
            //System.out.println("createCircle(): Añade un circulo a la lista de entidades");
            //features.add(feaBordes);
            features.add(feaFondos);
        } else {
            //System.out.println("createCircle(): Añadimos un circulo al bloque " + iterator);
            //blk.add(feaBordes);
            blk.add(feaFondos);
        }

    }

    /**
     * @return Returns the dxf3DFile.
     */
    public boolean isDxf3DFile() {
        return dxf3DFile;
    }

    /**
     * @param dxf3DFile The dxf3DFile to set.
     */
    public void setDxf3DFile(boolean dxf3DFile) {
        this.dxf3DFile = dxf3DFile;
    }
}
