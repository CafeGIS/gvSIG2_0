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

import java.awt.geom.Point2D;
import java.util.Vector;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.geo.Point3D;
import org.cresques.geo.Projected;
import org.cresques.io.DxfFile;
import org.cresques.io.DxfGroup;
import org.cresques.io.DxfGroupVector;
import org.cresques.px.Extent;
import org.cresques.px.IObjList;


/**
 * La clase DxfEntityMaker facilita la creación de entidades en un modelo de datos
 * CAD. La creación se realiza partiendo de las entidades obtenidas de un fichero DXF.
 * @author jmorell
 */
public class DxfEntityMaker implements DxfFile.EntityFactory, Projected {
    IProjection proj = null;
    DxfEntity lastEntity = null;
    DxfEntityList entities = null;
    Vector blkList = null;
    DxfBlock blk = null;
    DxfTable layers = null;
    double bulge = 0.0;
    double xtruX = 0.0;
    double xtruY = 0.0;
    double xtruZ = 1.0;
    int polylineFlag = 0;
    Point2D firstPt = new Point2D.Double();
    boolean addingToBlock = false;
    int iterator = 0;

    // jmorell, 050406: implementación inicial de los ATTRIBS para el piloto ...
    private Vector attributes = null;
    
    /**
     * Constructor de DxfEntityMaker.
     * @param proj, proyección cartográfica en la que se encontrarán las entidades
     * que creemos.
     */
    public DxfEntityMaker(IProjection proj) {
        this.proj = proj;
        layers = new DxfTable();
        entities = new DxfEntityList(proj);
        blkList = new Vector();

        // jmorell, 050406: implementación inicial de los ATTRIBS para el piloto ...
        attributes = new Vector();
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getBlkList()
     */
    public Vector getBlkList() {
        return blkList;
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getObjects()
     */
    public IObjList getObjects() {
        return entities;
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#getExtent()
     */
    public Extent getExtent() {
        return entities.getExtent();
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
    public void createLayer(DxfGroupVector grp) throws Exception {
        int color = grp.getDataAsInt(62);
        DxfLayer layer = new DxfLayer(grp.getDataAsString(2),
                                      Math.abs(grp.getDataAsInt(62)));

        if (color < 0) {
            layer.isOff = true;
        }

        layer.lType = grp.getDataAsString(6);
        layer.setFlags(grp.getDataAsInt(70));

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
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        DxfPolyline entity = new DxfPolyline(proj, layer);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        if (grp.hasCode(100)) {
            entity.setSubclassMarker(grp.getDataAsString(100));
        }

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double thickness = 0;

        if (grp.hasCode(10)) {
            x = grp.getDataAsDouble(10);
        }

        if (grp.hasCode(20)) {
            y = grp.getDataAsDouble(20);
        }

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
            entity.setElevation(z);
        }

        /*if (grp.hasCode(39))
                System.out.println("Leer el thickness provoca un error");
                thickness = grp.getDataAsDouble(39);*/
        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(66)) {
            entity.entitiesFollow = grp.getDataAsInt(66);
        }

        if (grp.hasCode(70)) {
            entity.flags = grp.getDataAsInt(70);
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

        if ((entity.flags & 0x01) == 0x01) {
            entity.closed = true;
        }

        lastEntity = entity;
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#endSeq(org.cresques.io.DxfGroupVector)
     */
    public void endSeq() throws Exception {
        if (lastEntity instanceof DxfPolyline) {
            DxfPolyline polyline = (DxfPolyline) lastEntity;

            if (polyline.closed) {
                ((DxfPolyline) lastEntity).add(firstPt);

                if (bulge > 0) {
                    int cnt = ((DxfPolyline) lastEntity).pts.size();

                    if ((((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                            2))).getX() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                              1))).getX()) &&
                            (((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                2))).getY() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                                  1))).getY())) {
                        // no se construye el arco
                    } else {
                        Vector arc = DxfPolyline.createArc((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                         2)),
                                                           (Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                         1)),
                                                           bulge);
                        ((DxfPolyline) lastEntity).pts.remove(cnt - 1);

                        for (int i = 0; i < arc.size(); i++) {
                            Point2D pt = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                          ((Point2D) arc.get(i)).getY());
                            ((DxfPolyline) lastEntity).add(pt);

                            if (((DxfPolyline) lastEntity).pts.size() == 1) {
                                firstPt = pt;
                            }
                        }
                    }

                    bulge = 0.0;
                } else if (bulge < 0) {
                    int cnt = ((DxfPolyline) lastEntity).pts.size();

                    if ((((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                            2))).getX() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                              1))).getX()) &&
                            (((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                2))).getY() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                                  1))).getY())) {
                        // no se construye el arco
                    } else {
                        Vector arc = DxfPolyline.createArc((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                         2)),
                                                           (Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                         1)),
                                                           bulge);
                        ((DxfPolyline) lastEntity).pts.remove(cnt - 1);

                        for (int i = arc.size() - 1; i >= 0; i--) {
                            Point2D pt = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                                          ((Point2D) arc.get(i)).getY());
                            ((DxfPolyline) lastEntity).add(pt);

                            if (((DxfPolyline) lastEntity).pts.size() == 1) {
                                firstPt = pt;
                            }
                        }
                    }

                    bulge = 0.0;
                }
            }

            // 050315, jmorell: Para leer Polylines estas tb deben tener vector de bulges.
            for (int i = 0; i < ((DxfPolyline) lastEntity).pts.size(); i++) {
                // jmorell, 050405: intentando leer DxfPolylines con bulges para el
                // piloto ...
                //polyline.addBulge(new Double(0));
                ((DxfPolyline) lastEntity).addBulge(new Double(0));
            }

            //System.out.println("DxfEntityMaker.endSeq(): ((DxfPolyline)lastEntity).getBulges().size() = " + ((DxfPolyline)lastEntity).getBulges().size());
            //((DxfPolyline)lastEntity).addBulge(new Double(bulge));
            //lastEntity.setHandle(entities.size()+40);
            if (addingToBlock == false) {
                //System.out.println("createPolyline: Añadimos una polilinea a la lista de entidades");
                entities.add(lastEntity);
            } else {
                //System.out.println("createPolyline: Añadimos una polilinea al bloque " + iterator);
                blk.add(lastEntity);

                //System.out.println("PLINE color="+polyline.getColor());
            }

            lastEntity = null;
        } else if (lastEntity instanceof DxfInsert) {
            // Se trata de un SEQEND despues de un ATTRIB
            gestionaInsert((DxfInsert) lastEntity, lastEntity.getLayer());

            if (addingToBlock == false) {
                entities.add(lastEntity);
            } else {
                blk.add(lastEntity);
            }

            lastEntity = null;
        } else {
            // Caso no contemplado
        }

        xtruX = 0.0;
        xtruY = 0.0;
        xtruZ = 1.0;
        bulge = 0.0;
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#addVertex(org.cresques.io.DxfGroupVector)
     */
    public void addVertex(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        int flags = 0;
        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }

        if (grp.hasCode(70)) {
            flags = grp.getDataAsInt(70);
        }

        //bulge = 0.0;
        if (bulge == 0.0) {
            if (grp.hasCode(42)) {
                bulge = grp.getDataAsDouble(42);

                //bulge = 0.0;
            } else {
                bulge = 0.0;
            }

            Point3D point_in = new Point3D(x, y, z);
            Point3D xtru = new Point3D(xtruX, xtruY, xtruZ);
            Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);

            if (((flags & 0x80) == 0x80) && ((flags & 0x40) == 0)) {
                int[] face = { 0, 0, 0, 0 };
                face[0] = grp.getDataAsInt(71);
                face[1] = grp.getDataAsInt(72);
                face[2] = grp.getDataAsInt(73);
                face[3] = grp.getDataAsInt(74);
                ((DxfPolyline) lastEntity).addFace(face);
            } else {
                x = point_out.getX();
                y = point_out.getY();
                z = point_out.getZ();

                Point2D ptaux = proj.createPoint(x, y);
                Point3D pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
                ((DxfPolyline) lastEntity).add(pt);

                if (((DxfPolyline) lastEntity).pts.size() == 1) {
                    firstPt = pt;
                }
            }
        } else if (bulge > 0.0) {
            double bulge_aux = 0.0;

            if (grp.hasCode(42)) {
                bulge_aux = grp.getDataAsDouble(42);
            } else {
                bulge_aux = 0.0;
            }

            Point3D point_in = new Point3D(x, y, z);
            Point3D xtru = new Point3D(xtruX, xtruY, xtruZ);
            Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
            x = point_out.getX();
            y = point_out.getY();
            z = point_out.getZ();

            Point2D ptaux = proj.createPoint(x, y);
            Point3D pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
            ((DxfPolyline) lastEntity).add(pt);

            if (((DxfPolyline) lastEntity).pts.size() == 1) {
                firstPt = pt;
            }

            int cnt = ((DxfPolyline) lastEntity).pts.size();

            if ((((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt - 2))).getX() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                        1))).getX()) &&
                    (((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt - 2))).getY() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                            1))).getY())) {
                // no se construye el arco
            } else {
                Vector arc = DxfPolyline.createArc((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                 2)),
                                                   (Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                 1)),
                                                   bulge);
                ((DxfPolyline) lastEntity).pts.remove(cnt - 1);

                for (int i = 0; i < arc.size(); i++) {
                    ptaux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                          ((Point2D) arc.get(i)).getY());
                    pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
                    ((DxfPolyline) lastEntity).add(pt);

                    if (((DxfPolyline) lastEntity).pts.size() == 1) {
                        firstPt = pt;
                    }
                }
            }

            bulge = bulge_aux;
        } else { //si el bulge es menor que cero.

            double bulge_aux = 0.0;

            if (grp.hasCode(42)) {
                bulge_aux = grp.getDataAsDouble(42); // * (-1.0);
            } else {
                bulge_aux = 0.0;
            }

            Point3D point_in = new Point3D(x, y, z);
            Point3D xtru = new Point3D(xtruX, xtruY, xtruZ);
            Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
            x = point_out.getX();
            y = point_out.getY();
            z = point_out.getZ();

            Point2D ptaux = proj.createPoint(x, y);
            Point3D pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
            ((DxfPolyline) lastEntity).add(pt);

            if (((DxfPolyline) lastEntity).pts.size() == 1) {
                firstPt = pt;
            }

            int cnt = ((DxfPolyline) lastEntity).pts.size();

            if ((((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt - 2))).getX() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                        1))).getX()) &&
                    (((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt - 2))).getY() == ((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                                                            1))).getY())) {
                // no se construye el arco
            } else {
                Vector arc = DxfPolyline.createArc((Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                 2)),
                                                   (Point2D) (((DxfPolyline) lastEntity).pts.get(cnt -
                                                                                                 1)),
                                                   bulge);
                ((DxfPolyline) lastEntity).pts.remove(cnt - 1);

                for (int i = arc.size() - 1; i >= 0; i--) {
                    ptaux = proj.createPoint(((Point2D) arc.get(i)).getX(),
                                          ((Point2D) arc.get(i)).getY());
                    pt = new Point3D(ptaux.getX(), ptaux.getY(), z);
                    ((DxfPolyline) lastEntity).add(pt);

                    if (((DxfPolyline) lastEntity).pts.size() == 1) {
                        firstPt = pt;
                    }
                }
            }

            bulge = bulge_aux;
        }

        /*if (grp.hasCode(5)) {
                String hexS = grp.getDataAsString(5);
                Integer hexI = Integer.decode("0x" + hexS);
                int hexi = hexI.intValue();
                lastEntity.setHandle(hexi);
        } else {
                lastEntity.setHandle(entities.size()+40);
        }*/
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createLwPolyline(org.cresques.io.DxfGroupVector)
     */
    public void createLwPolyline(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double elev = 0.0;
        DxfGroup g = null;
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        DxfLwPolyline entity = new DxfLwPolyline(proj, layer);
        
        if (grp.hasCode(38)) {
            entity.setElevation(grp.getDataAsDouble(38));
        }

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
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

        double bulge = 0;
        boolean isNewCoord = false;

        for (int i = 0; i < grp.size(); i++) {
            bulge = 0;
            isNewCoord = false;
            g = (DxfGroup) grp.get(i);

            if (g.getCode() == 10) {
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
                //if (y <= 1.0) throw new Exception("Y == "+y);
                entity.add(proj.createPoint(x, y));
                entity.addBulge(new Double(0));
                x = 0.0;
                y = 0.0;
                isNewCoord = true;
            } else if (g.getCode() == 42) {
                //entity.addBulge((Double)g.getData());
                entity.getBulges().remove(entity.getBulges().size() - 1);
                entity.getBulges().add((Double) g.getData());
                bulge = ((Double) g.getData()).doubleValue();
            }

            /*if (bulge == 0 && isNewCoord) {
                    entity.addBulge(new Double(0));
            }*/
        }

        //System.out.println("entity.getPts().size() = " + entity.getPts().size());
        //System.out.println("entity.getBulges().size() = " + entity.getBulges().size());
        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(70)) {
            entity.flags = grp.getDataAsInt(70);

            //System.out.println("entity.flags = " + entity.flags);
        }

        if ((entity.flags & 0x01) == 0x01) {
            entity.closed = true;
        }

        if (addingToBlock == false) {
            entities.add(entity);
        } else {
            //System.out.println("createLwPolyline(): Añadimos una lwpolilinea al bloque " + iterator);
            blk.add(entity);
        }
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createLine(org.cresques.io.DxfGroupVector)
     */
    public void createLine(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double z1 = 0.0;
        double z2 = 0.0;
        DxfGroup g = null;
        Point2D pt1 = null;
        Point2D pt2 = null;
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z1 = grp.getDataAsDouble(30);
        }

        pt1 = proj.createPoint(x, y);
        x = grp.getDataAsDouble(11);
        y = grp.getDataAsDouble(21);

        if (grp.hasCode(31)) {
            z2 = grp.getDataAsDouble(31);
        }

        pt2 = proj.createPoint(x, y);

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsInt(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsInt(230);
        }

        Point3D point_in1 = new Point3D(pt1.getX(), pt1.getY(), z1);
        Point3D point_in2 = new Point3D(pt2.getX(), pt2.getY(), z2);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point2D point_out1 = DxfCalXtru.CalculateXtru(point_in1, xtru);
        Point2D point_out2 = DxfCalXtru.CalculateXtru(point_in2, xtru);
        pt1.setLocation(point_out1);
        pt2.setLocation(point_out2);

        DxfLine entity = new DxfLine(proj, layer, pt1, pt2);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (addingToBlock == false) {
            //System.out.println("createLine(): Añadimos una linea a la lista de entidades");
            entities.add(entity);
        } else {
            //System.out.println("createLine(): Añadimos una linea al bloque " + iterator);
            blk.add(entity);
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

        //OJO! El segundo punto es opcional ...
        Point2D pt1 = null;

        //OJO! El segundo punto es opcional ...
        Point2D pt2 = null;
        String txt = null;
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));

        txt = grp.getDataAsString(1);

        DxfText entity = new DxfText(proj, layer, txt);
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

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

        entity.setPt(proj.createPoint(x, y));

        //entity.setPt1(proj.createPoint(x, y));
        if (grp.hasCode(11)) {
            entity.setTwoPointsFlag(true);
            entity.setPt1(proj.createPoint(entity.getPt().getX(),
                                           entity.getPt().getY()));
            x = grp.getDataAsDouble(11);
            y = grp.getDataAsDouble(21);
            z = grp.getDataAsDouble(31);

            point_in = new Point3D(x, y, z);
            point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
            x = point_out.getX();
            y = point_out.getY();
            z = point_out.getZ();

            entity.setPt2(proj.createPoint(x, y));
        }

        entity.setHeight(grp.getDataAsDouble(40));

        if (grp.hasCode(50)) {
            entity.setRotation(grp.getDataAsDouble(50));

            //System.out.println("AAAAAA: entity.getRotation = " + entity.getRotation());
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(72)) {
            entity.align = grp.getDataAsInt(72);
        }

        if (addingToBlock == false) {
            entities.add(entity);
        } else {
            //System.out.println("createText(): Añadimos un text al bloque " + iterator);
            blk.add(entity);
        }
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createMText(org.cresques.io.DxfGroupVector)
     */
    public void createMText(DxfGroupVector v) throws Exception {
        // TODO Auto-generated method stub
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createPoint(org.cresques.io.DxfGroupVector)
     */
    public void createPoint(DxfGroupVector grp) throws Exception {
        double x = 0.0; //, h= 0.0, rot= 0.0;
        double y = 0.0; //, h= 0.0, rot= 0.0;
        double z = 0.0; //, h= 0.0, rot= 0.0;
        DxfGroup g = null;
        Point2D pt = null;
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        DxfPoint entity = new DxfPoint(proj, layer);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(210)) {
            extx = grp.getDataAsDouble(210);
        }

        if (grp.hasCode(220)) {
            exty = grp.getDataAsInt(220);
        }

        if (grp.hasCode(230)) {
            extz = grp.getDataAsInt(230);
        }

        Point3D point_in = new Point3D(x, y, z);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point2D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();
        entity.setPt(proj.createPoint(x, y));

        if (addingToBlock == false) {
            entities.add(entity);
        } else {
            //System.out.println("createPoint(): Añadimos un punto al bloque " + iterator);
            blk.add(entity);
        }
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createCircle(org.cresques.io.DxfGroupVector)
     */
    public void createCircle(DxfGroupVector grp) throws Exception {
        //System.out.println("Encontramos un Circle.");
        double x = 0.0;

        //System.out.println("Encontramos un Circle.");
        double y = 0.0;

        //System.out.println("Encontramos un Circle.");
        double z = 0.0;
        double r = 0.0;
        DxfGroup g = null;
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }

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
        Point2D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();

        Point2D center = proj.createPoint(x, y);
        Point2D[] pts = new Point2D[360];
        int angulo = 0;

        for (angulo = 0; angulo < 360; angulo++) {
            pts[angulo] = new Point2D.Double(center.getX(), center.getY());
            pts[angulo].setLocation(pts[angulo].getX() +
                                    (r * Math.sin((angulo * Math.PI) / (double) 180.0)),
                                    pts[angulo].getY() +
                                    (r * Math.cos((angulo * Math.PI) / (double) 180.0)));

            if (pts.length == 1) {
                firstPt = pts[angulo];
            }
        }

        DxfCircle entity = new DxfCircle(proj, layer, pts);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        entity.setCenter(new Point2D.Double(x, y));

        //System.out.println("Y le metemos el centro.");
        entity.setRadius(r);

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (addingToBlock == false) {
            //System.out.println("createCircle(): Añade un circulo a la lista de entidades");
            entities.add(entity);
        } else {
            //System.out.println("createCircle(): Añadimos un circulo al bloque " + iterator);
            blk.add(entity);
        }
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createEllipse(org.cresques.io.DxfGroupVector)
     */
    public void createEllipse(DxfGroupVector grp) throws Exception {
        double incX = 0.0;
        double incY = 0.0;
        double incZ = 0.0;
        double xc = 0.0;
        double yc = 0.0;
        double zc = 0.0;
        double mMAxisRatio = 0.0;
        DxfGroup g = null;
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        xc = grp.getDataAsDouble(10);
        yc = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            zc = grp.getDataAsDouble(30);
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

        Point3D point_in = new Point3D(xc, yc, zc);
        Point3D xtru = new Point3D(extx, exty, extz);
        Point3D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        xc = point_out.getX();
        yc = point_out.getY();
        zc = point_out.getZ();

        incX = grp.getDataAsDouble(11);
        incY = grp.getDataAsDouble(21);

        if (grp.hasCode(31)) {
            incZ = grp.getDataAsDouble(31);
        }

        if (grp.hasCode(40)) {
            mMAxisRatio = grp.getDataAsDouble(40);
        }

        Point2D pt2 = new Point2D.Double(xc + incX, yc + incY);
        Point2D pt1 = new Point2D.Double(xc - incX, yc - incY);
        double majorAxisLength = pt1.distance(pt2);
        double minorAxisLength = majorAxisLength * mMAxisRatio;

        DxfEllipse entity = new DxfEllipse(proj, layer, pt1, pt2,
                                           minorAxisLength);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (addingToBlock == false) {
            //System.out.println("createCircle(): Añade un circulo a la lista de entidades");
            entities.add(entity);
        } else {
            //System.out.println("createCircle(): Añadimos un circulo al bloque " + iterator);
            blk.add(entity);
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
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }

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
        Point2D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();

        Point2D center = proj.createPoint(x, y);

        //System.out.println("empieza = " + empieza + ", acaba = " + acaba);
        int iempieza = (int) empieza;
        int iacaba = (int) acaba;

        //System.out.println("iempieza = " + iempieza + ", iacaba = " + iacaba);
        double angulo = 0;
        Point2D[] pts = null;

        if (empieza <= acaba) {
            pts = new Point2D[(iacaba - iempieza) + 2];
            angulo = empieza;
            pts[0] = new Point2D.Double(center.getX() +
                                        (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                        center.getY() +
                                        (r * Math.sin((angulo * Math.PI) / (double) 180.0)));

            for (int i = 1; i <= ((iacaba - iempieza) + 1); i++) {
                angulo = (double) (iempieza + i);
                pts[i] = new Point2D.Double(center.getX() +
                                            (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                            center.getY() +
                                            (r * Math.sin((angulo * Math.PI) / (double) 180.0)));
            }

            angulo = acaba;
            pts[(iacaba - iempieza) + 1] = new Point2D.Double(center.getX() +
                                                              (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                                              center.getY() +
                                                              (r * Math.sin((angulo * Math.PI) / (double) 180.0)));
        } else {
            pts = new Point2D[(360 - iempieza) + iacaba + 2];
            angulo = empieza;

            //System.out.println("pts[0] = " + pts[0] + ", center = " + center + ", angulo = " + angulo);
            pts[0] = new Point2D.Double(center.getX() +
                                        (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                        center.getY() +
                                        (r * Math.sin((angulo * Math.PI) / (double) 180.0)));

            for (int i = 1; i <= (360 - iempieza); i++) {
                angulo = (double) (iempieza + i);
                pts[i] = new Point2D.Double(center.getX() +
                                            (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                            center.getY() +
                                            (r * Math.sin((angulo * Math.PI) / (double) 180.0)));
            }

            for (int i = (360 - iempieza) + 1;
                     i <= ((360 - iempieza) + iacaba); i++) {
                angulo = (double) (i - (360 - iempieza));
                pts[i] = new Point2D.Double(center.getX() +
                                            (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                            center.getY() +
                                            (r * Math.sin((angulo * Math.PI) / (double) 180.0)));
            }

            angulo = acaba;
            pts[(360 - iempieza) + iacaba + 1] = new Point2D.Double(center.getX() +
                                                                    (r * Math.cos((angulo * Math.PI) / (double) 180.0)),
                                                                    center.getY() +
                                                                    (r * Math.sin((angulo * Math.PI) / (double) 180.0)));
        }

        DxfArc entity = new DxfArc(proj, layer, pts);

        if (grp.hasCode(5)) {
            String hexS = grp.getDataAsString(5);
            Integer hexI = Integer.decode("0x" + hexS);
            int hexi = hexI.intValue();
            entity.setHandle(hexi);
        } else {
            entity.setHandle(entities.size() + 40);
        }

        // 050223, jmorell: Establecimiento de los parámetros del arco.
        //System.out.println("DxfEntityMaker.createArc(): pts.length = " + pts.length);
        entity.setCentralPoint(pts[(pts.length) / 2]);

        //System.out.println("DxfEntityMaker.createArc(): (pts.length)/2 = " + (pts.length/2));
        entity.setInit(pts[0]);
        entity.setEnd(pts[pts.length - 1]);

        //System.out.println("DxfEntityMaker.createArc(): (pts.length)-1 = " + (pts.length-1));
        entity.setCenter(center);
        entity.setRadius(r);
        entity.setInitAngle(empieza);

        //System.out.println("DxfEntityMaker.createArc(): empieza = " + empieza);
        entity.setEndAngle(acaba);

        //System.out.println("DxfEntityMaker.createArc(): acaba = " + acaba);
        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        //System.out.println("createArc(): Añadimos un arco al bloque");
        if (addingToBlock == false) {
            entities.add(entity);
        } else {
            //System.out.println("createArc(): Añadimos un arco al bloque " + iterator);
            blk.add(entity);
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
        Point2D pt = new Point2D.Double(0.0, 0.0);
        Point2D scaleFactor = new Point2D.Double(1.0, 1.0);
        double rotAngle = 0.0;
        String blockName = "";
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));

        DxfInsert entity = new DxfInsert(proj, layer);
        DxfPoint secondEntity = new DxfPoint(proj, layer);

        // jmorell, 050406: implementación inicial de los ATTRIBS para el piloto ...
        int attributesFollowFlag = 0;

        if (grp.hasCode(2)) {
            blockName = grp.getDataAsString(2);
            entity.setBlockName(blockName);
        }

        if (grp.hasCode(10)) {
            x = grp.getDataAsDouble(10);
        }

        if (grp.hasCode(20)) {
            y = grp.getDataAsDouble(20);
        }

        if (grp.hasCode(30)) {
            z = grp.getDataAsDouble(30);
        }

        if (grp.hasCode(41)) {
            scaleFactor.setLocation(grp.getDataAsDouble(41), scaleFactor.getY());
            entity.setScaleFactor(scaleFactor);
        } else {
            entity.setScaleFactor(scaleFactor);
        }

        if (grp.hasCode(42)) {
            scaleFactor.setLocation(scaleFactor.getX(), grp.getDataAsDouble(42));
            entity.setScaleFactor(scaleFactor);
        } else {
            entity.setScaleFactor(scaleFactor);
        }

        if (grp.hasCode(43)) {
            // TODO La coordenada z
        }

        if (grp.hasCode(50)) {
            rotAngle = grp.getDataAsDouble(50);
            entity.setRotAngle(rotAngle);
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(66)) {
            attributesFollowFlag = grp.getDataAsInt(66);

            //System.out.println("createInsert: attributesFollowFlag = " + attributesFollowFlag);
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
        Point2D point_out = DxfCalXtru.CalculateXtru(point_in, xtru);
        x = point_out.getX();
        y = point_out.getY();

        entity.setBlkList(blkList);

        entity.encuentraBloque(blockName);

        entity.setPt(proj.createPoint(x, y));
        secondEntity.setPt(proj.createPoint(x, y));

        if ((entity.getBlockFound() == true) && (attributesFollowFlag != 1)) {
            gestionaInsert(entity, layer);
        }

        //System.out.println("createInsert: entity.getBlockName = " + entity.getBlockName());
        //System.out.println("createInsert: entity.getRotAngle = " + entity.getRotAngle());
        if (attributesFollowFlag == 1) {
            //System.out.println("createInsert: Coloca como lastEntity el insert");
            lastEntity = entity;
        } else {
            if (addingToBlock == false) {
                entities.add(secondEntity);
            } else if ((addingToBlock == true) && (entity.blockFound == true)) {
                //System.out.println("createArc(): Añadimos un insert al bloque " + iterator);
                blk.add(entity);
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
        Point2D[] pts = new Point2D[4];
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        double extx = 0.0;
        double exty = 0.0;
        double extz = 1.0;

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);

        if (grp.hasCode(30)) {
            z1 = grp.getDataAsDouble(30);
        }

        pts[0] = proj.createPoint(x, y);
        x = grp.getDataAsDouble(11);
        y = grp.getDataAsDouble(21);

        if (grp.hasCode(31)) {
            z2 = grp.getDataAsDouble(31);
        }

        pts[1] = proj.createPoint(x, y);
        x = grp.getDataAsDouble(12);
        y = grp.getDataAsDouble(22);

        if (grp.hasCode(32)) {
            z3 = grp.getDataAsDouble(32);
        }

        pts[2] = proj.createPoint(x, y);
        x = grp.getDataAsDouble(13);
        y = grp.getDataAsDouble(23);

        if (grp.hasCode(33)) {
            z2 = grp.getDataAsDouble(33);
        }

        pts[3] = proj.createPoint(x, y);

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
        Point2D point_out1 = DxfCalXtru.CalculateXtru(point_in1, xtru);
        Point2D point_out2 = DxfCalXtru.CalculateXtru(point_in2, xtru);
        Point2D point_out3 = DxfCalXtru.CalculateXtru(point_in3, xtru);
        Point2D point_out4 = DxfCalXtru.CalculateXtru(point_in4, xtru);
        pts[0].setLocation(point_out1);
        pts[1].setLocation(point_out2);
        pts[2].setLocation(point_out3);
        pts[3].setLocation(point_out4);

        DxfSolid entity = new DxfSolid(proj, layer, pts);

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (addingToBlock == false) {
            //System.out.println("createLine(): Añadimos una linea a la lista de entidades");
            entities.add(entity);
        } else {
            //System.out.println("createLine(): Añadimos una linea al bloque " + iterator);
            blk.add(entity);
        }
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createSpline(org.cresques.io.DxfGroupVector)
     */
    /**
     * Los Splines estan implementados como LwPolylines. Se pintan las lineas
     * entre los vertices pero no se aplica la curvatura Spline.
     * TODO: Contemplar la curvatura spline para Splines.
     */
    public void createSpline(DxfGroupVector grp) throws Exception {
        double x = 0.0;
        double y = 0.0;
        double elev = 0.0;
        DxfGroup g = null;

        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        DxfLwPolyline entity = new DxfLwPolyline(proj, layer);

        for (int i = 0; i < grp.size(); i++) {
            g = (DxfGroup) grp.get(i);

            if (g.getCode() == 10) {
                x = ((Double) g.getData()).doubleValue();
            } else if (g.getCode() == 20) {
                y = ((Double) g.getData()).doubleValue();

                //if (y <= 1.0) throw new Exception("Y == "+y);
                entity.add(proj.createPoint(x, y));
                x = 0.0;
                y = 0.0;
            }
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(70)) {
            entity.flags = grp.getDataAsInt(70);
        }

        if ((entity.flags & 0x01) == 0x01) {
            entity.closed = true;
        }

        if (addingToBlock == false) {
            entities.add(entity);
        } else {
            //System.out.println("createLwPolyline(): Añadimos una lwpolilinea al bloque " + iterator);
            blk.add(entity);
        }
    }
    
    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#createBlock(org.cresques.io.DxfGroupVector)
     */
    public void createBlock(DxfGroupVector grp) throws Exception {
        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        blk = new DxfBlock(proj);

        Point2D basePoint = new Point2D.Double();
        String blockName = "";

        //System.out.println("createBlock(): Creamos nuevo bloque, el bloque " + iterator);
        addingToBlock = true;

        //System.out.println("createBlock(): Añadimos el bloque " + iterator + " a la lista de bloques");
        blkList.add(iterator, blk);

        //System.out.println("createBlock(): Rellenamos la informacion del bloque " + iterator);
        if (grp.hasCode(1)) {
            blockName = grp.getDataAsString(1);
            blk.setBlkName(blockName);
        }

        if (grp.hasCode(2)) {
            blockName = grp.getDataAsString(2);
            blk.setBlkName(blockName);
        }

        if (grp.hasCode(3)) {
            blockName = grp.getDataAsString(3);
            blk.setBlkName(blockName);
        }

        if (grp.hasCode(10)) {
            basePoint = new Point2D.Double(grp.getDataAsDouble(10),
                                           basePoint.getY());
            blk.setBPoint(basePoint);
        }

        if (grp.hasCode(20)) {
            basePoint = new Point2D.Double(basePoint.getX(),
                                           grp.getDataAsDouble(20));
            blk.setBPoint(basePoint);
        }

        if (grp.hasCode(30)) {
            // TODO Contemplar la coordenada z
        }

        if (grp.hasCode(70)) {
            blk.flags = grp.getDataAsInt(70);
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
        Vector blkList = getBlkList();
        DxfBlock dxfBlock = null;
        DxfEntity dxfEntity = null;
        DxfLine dxfLine = null;
        DxfInsert dxfInsert = null;
        Point2D point1 = new Point2D.Double();
        Point2D point2 = new Point2D.Double();

        for (int i = 0; i < blkList.size(); i++) {
            dxfBlock = (DxfBlock) blkList.get(i);

            int aux = dxfBlock.getBlkElements().size();

            for (int j = 0; j < aux; j++) {
                dxfEntity = (DxfEntity) dxfBlock.getBlkElements().get(j);

                if (dxfEntity instanceof DxfLine) {
                    dxfLine = (DxfLine) dxfEntity;
                    point1 = dxfLine.getPts()[0];
                    point2 = dxfLine.getPts()[1];

                    //System.out.println("compruebaBloques(): Bloque = " + i + ", elemento = " + j + ", vertice1 = " + point1 + ", vertice2 = " + point2);
                } else if (dxfEntity instanceof DxfInsert) {
                    dxfInsert = (DxfInsert) dxfEntity;

                    String nomBlock = dxfInsert.getBlockName();

                    //System.out.println("compruebaBloques(): Bloque = " + i + ", elemento = " + j + ", inserta el bloque = " + nomBlock);
                    //System.out.println("compruebaBloques(): dxfInsert.pt = " + dxfInsert.getPt());
                    //System.out.println("compruebaBloques(): dxfInsert.rotAngle = " + dxfInsert.getRotAngle());
                    //System.out.println("compruebaBloques(): dxfInsert.scaleFactor = " + dxfInsert.getScaleFactor());
                    if (dxfInsert.getBlockFound() == false) {
                        //System.out.println("compruebaBloques(): Ahora se ocupa del DxfInsert " + nomBlock);
                        boolean aux_bool = dxfInsert.encuentraBloque(nomBlock);
                        gestionaInsert(dxfInsert, dxfInsert.getDxfLayer());
                        dxfBlock.add(dxfInsert);
                    }
                }
            }
        }
    }
    
    /**
     * Establece la proyección cartográfica en la que se van a crear las entidades.
     * @param p, Proyección cartográfica.
     */
    public void setProjection(IProjection proj) {
        this.proj = proj;
    }
    
    /**
     * Devuelve la proyección cartográfica en la que se encuentran las entidades.
     * @return IProjection, proyección cartográfica.
     */
    public IProjection getProjection() {
        return proj;
    }
    
    /**
     * Permite reproyectar las entidades creadas dado un conjunto de coordenadas de
     * transformación.
     * @param rp, coordenadas de transformación.
     */
    public void reProject(ICoordTrans rp) {
        entities.reProject(rp);
        setProjection(rp.getPDest());
    }
    
    /**
     * Devuelve las entidades creadas.
     * @return DxfEntityList
     */
    public DxfEntityList getEntities() {
        return entities;
    }
    
    /**
     * Devuelve las capas del DXF en forma de DxfTable.
     * @return DxfTable
     */
    public DxfTable getLayers() {
        return layers;
    }
    
    /**
     * Devuelve el bloque activo.
     * @return DxfBlock
     */
    public DxfBlock getBlk() {
        return blk;
    }
    
    /**
     * Método que permite incluir en la lista general de objetos los objetos que se
     * encuentran dentro del bloque referenciado por cada DxfInsert.
     * @param entity, el punto de inserción.
     * @param layer, la capa en la que se encuentra.
     */
    public void gestionaInsert(DxfInsert entity, DxfLayer layer) {
        DxfEntity dxfEntity = null;
        DxfLine dxfLine = null;
        DxfInsert dxfInsert = null;
        DxfPolyline dxfPolyline = null;
        DxfArc dxfArc = null;
        DxfCircle dxfCircle = null;
        DxfLwPolyline dxfLwPolyline = null;
        DxfPoint dxfPoint = null;
        DxfText dxfText = null;
        DxfSolid dxfSolid = null;

        // jmorell, 050406: intentando corregir cosas que salen fuera de sitio ...
        double bPointX = 0.0;
        double bPointY = 0.0;

        //if (entity.getBlockFound() == true) {
        bPointX = entity.block.bPoint.getX();
        bPointY = entity.block.bPoint.getY();

        //}
        double sFactorX = entity.getScaleFactor().getX();
        double sFactorY = entity.getScaleFactor().getY();
        double rAngleGra = entity.getRotAngle();
        double rAngleRad = (rAngleGra * Math.PI) / 180.0;

        for (int i = 0; i < entity.block.size(); i++) {
            //System.out.println("gestionaInserts: entity.block.blkElements.size() = " + entity.block.blkElements.size());
            dxfEntity = (DxfEntity) entity.block.get(i);

            Point2D point1 = new Point2D.Double();
            Point2D point2 = new Point2D.Double();
            Point2D point11 = new Point2D.Double();
            Point2D point22 = new Point2D.Double();
            Point2D pointAux = null;

            if (dxfEntity instanceof DxfLine) {
                dxfLine = (DxfLine) dxfEntity;
                point1 = dxfLine.getPts()[0];

                //double laX = entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX());
                //double laY = entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY());
                pointAux = new Point2D.Double(point1.getX() - bPointX,
                                              point1.getY() - bPointY);

                double laX = entity.pt.getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = entity.pt.getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                point11.setLocation(laX, laY);
                point2 = dxfLine.getPts()[1];
                pointAux = new Point2D.Double(point2.getX() - bPointX,
                                              point2.getY() - bPointY);
                laX = entity.pt.getX() +
                      (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                laY = entity.pt.getY() +
                      (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                point22.setLocation(laX, laY);

                DxfLine dxfLinee = new DxfLine(proj, layer, point11, point22);

                if (addingToBlock == false) {
                    entities.add(dxfLinee);
                }
            } else if (dxfEntity instanceof DxfInsert) {
                dxfInsert = (DxfInsert) dxfEntity;
                point1 = dxfInsert.pt;
                pointAux = new Point2D.Double(point1.getX() - bPointX,
                                              point1.getY() - bPointY);

                //point11.setLocation(entity.pt.getX() - entity.block.bPoint.getX() + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - entity.block.bPoint.getY() + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                //point11.setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                double laX = entity.pt.getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = entity.pt.getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                point11.setLocation(laX, laY);

                DxfInsert dxfInsertt = new DxfInsert(proj, layer);

                dxfInsertt.pt = point11;

                dxfInsertt.blkList = dxfInsert.blkList;
                dxfInsertt.block = dxfInsert.block;
                dxfInsertt.blockName = dxfInsert.blockName;
                dxfInsertt.rotAngle = dxfInsert.rotAngle;
                dxfInsertt.layer = dxfInsert.layer;
                dxfInsertt.proj = dxfInsert.proj;

                Point2D newScale = new Point2D.Double(dxfInsert.getScaleFactor()
                                                               .getX() * sFactorX,
                                                      dxfInsert.getScaleFactor()
                                                               .getY() * sFactorY);
                dxfInsertt.setScaleFactor(newScale);

                //dxfInsertt.scaleFactor = new Point2D.Double(dxfInsert.scaleFactor.getX() * entity.scaleFactor.getX(), dxfInsert.scaleFactor.getY() * entity.scaleFactor.getY());
                gestionaInsert(dxfInsertt, layer);
            } else if (dxfEntity instanceof DxfPolyline) {
                dxfPolyline = (DxfPolyline) dxfEntity;

                DxfPolyline dxfPolylinee = new DxfPolyline(proj, layer);

                if (dxfPolyline.closed) {
                    dxfPolylinee.closed = true;
                }

                Point2D[] points = new Point2D[dxfPolyline.pts.size()];
                Point2D[] pointss = new Point2D[dxfPolyline.pts.size()];

                for (int j = 0; j < dxfPolyline.pts.size(); j++) {
                    points[j] = (Point2D) dxfPolyline.pts.get(j);
                    pointss[j] = new Point2D.Double();

                    //pointss[j].setLocation(entity.pt.getX() - entity.block.bPoint.getX() + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - entity.block.bPoint.getY() + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    //pointss[j].setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    pointAux = new Point2D.Double(points[j].getX() - bPointX,
                                                  points[j].getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    pointss[j].setLocation(laX, laY);

                    //pointss[j].setLocation(entity.pt.getX() + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    dxfPolylinee.add(pointss[j]);

                    // jmorell, 050405: Bulges en Dxfpolyline para el piloto
                    dxfPolylinee.addBulge((Double) dxfPolyline.getBulges().get(j));
                }

                if (addingToBlock == false) {
                    entities.add(dxfPolylinee);
                }
            } else if (dxfEntity instanceof DxfArc) {
                dxfArc = (DxfArc) dxfEntity;

                Point2D[] points = new Point2D[dxfArc.pts.length];
                Point2D[] pointss = new Point2D[dxfArc.pts.length];

                for (int j = 0; j < dxfArc.pts.length; j++) {
                    points[j] = (Point2D) dxfArc.pts[j];
                    pointss[j] = new Point2D.Double();
                    pointAux = new Point2D.Double(points[j].getX() - bPointX,
                                                  points[j].getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    pointss[j].setLocation(laX, laY);

                    //pointss[j].setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                }

                DxfArc dxfArcc = new DxfArc(proj, layer, pointss);

                // 050315, jmorell: Para que no se pierdan las propiedades en el
                //					caso de objetos dentro de bloques.
                pointAux = new Point2D.Double(dxfArc.getCentralPoint().getX() -
                                              bPointX,
                                              dxfArc.getCentralPoint().getY() -
                                              bPointY);

                double laX = entity.pt.getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = entity.pt.getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                pointAux.setLocation(laX, laY);
                dxfArcc.setCentralPoint(pointAux);
                pointAux = new Point2D.Double(dxfArc.getInit().getX() -
                                              bPointX,
                                              dxfArc.getInit().getY() -
                                              bPointY);
                laX = entity.pt.getX() +
                      (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                laY = entity.pt.getY() +
                      (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                pointAux.setLocation(laX, laY);
                dxfArcc.setInit(pointAux);
                pointAux = new Point2D.Double(dxfArc.getEnd().getX() - bPointX,
                                              dxfArc.getEnd().getY() - bPointY);
                laX = entity.pt.getX() +
                      (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                laY = entity.pt.getY() +
                      (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                pointAux.setLocation(laX, laY);
                dxfArcc.setEnd(pointAux);
                pointAux = new Point2D.Double(dxfArc.getCenter().getX() -
                                              bPointX,
                                              dxfArc.getCenter().getY() -
                                              bPointY);
                laX = entity.pt.getX() +
                      (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                laY = entity.pt.getY() +
                      (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                      ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                pointAux.setLocation(laX, laY);
                dxfArcc.setCenter(pointAux);
                dxfArcc.setRadius(dxfArc.getRadius() * sFactorX);

                // TODO ¿Como afectan las rotaciones del insert al init y el end angle?
                dxfArcc.setInitAngle(dxfArc.getInitAngle());
                dxfArcc.setEndAngle(dxfArc.getEndAngle());

                if (addingToBlock == false) {
                    entities.add(dxfArcc);
                }
            } else if (dxfEntity instanceof DxfCircle) {
                dxfCircle = (DxfCircle) dxfEntity;

                Point2D[] points = new Point2D[dxfCircle.pts.length];
                Point2D[] pointss = new Point2D[dxfCircle.pts.length];

                for (int j = 0; j < dxfCircle.pts.length; j++) {
                    points[j] = (Point2D) dxfCircle.pts[j];
                    pointss[j] = new Point2D.Double();
                    pointAux = new Point2D.Double(points[j].getX() - bPointX,
                                                  points[j].getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    pointss[j].setLocation(laX, laY);

                    //pointss[j].setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                }

                DxfCircle dxfCirclee = new DxfCircle(proj, layer, pointss);

                // 050315, jmorell: Para que no se pierdan las propiedades en el
                //					caso de objetos dentro de bloques.
                pointAux = new Point2D.Double(dxfCircle.getCenter().getX() -
                                              bPointX,
                                              dxfCircle.getCenter().getY() -
                                              bPointY);

                double laX = entity.pt.getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = entity.pt.getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                pointAux.setLocation(laX, laY);
                dxfCirclee.setCenter(pointAux);

                // Escala en X = escala en Y ...
                dxfCirclee.setRadius(dxfCircle.getRadius() * sFactorX);

                if (addingToBlock == false) {
                    entities.add(dxfCirclee);
                }
            } else if (dxfEntity instanceof DxfLwPolyline) {
                dxfLwPolyline = (DxfLwPolyline) dxfEntity;

                DxfLwPolyline dxfLwPolylinee = new DxfLwPolyline(proj, layer);
                Point2D[] points = new Point2D[dxfLwPolyline.pts.size()];
                Point2D[] pointss = new Point2D[dxfLwPolyline.pts.size()];

                for (int j = 0; j < dxfLwPolyline.pts.size(); j++) {
                    points[j] = (Point2D) dxfLwPolyline.pts.get(j);
                    pointss[j] = new Point2D.Double();
                    pointAux = new Point2D.Double(points[j].getX() - bPointX,
                                                  points[j].getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    pointss[j].setLocation(laX, laY);

                    //pointss[j].setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    dxfLwPolylinee.add(pointss[j]);

                    // jmorell, 050405: Bulges en Dxfpolyline para el piloto
                    dxfLwPolylinee.addBulge((Double) dxfPolyline.getBulges()
                                                                .get(j));
                }

                if (addingToBlock == false) {
                    entities.add(dxfLwPolylinee);
                }
            } else if (dxfEntity instanceof DxfPoint) {
                dxfPoint = (DxfPoint) dxfEntity;
                point1 = dxfPoint.getPt();
                pointAux = new Point2D.Double(point1.getX() - bPointX,
                                              point1.getY() - bPointY);

                double laX = entity.pt.getX() +
                             (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                double laY = entity.pt.getY() +
                             (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                             ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                point11.setLocation(laX, laY);

                //point11.setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                DxfPoint dxfPointt = new DxfPoint(proj, layer);

                //dxfPointt.pt = point11;
                dxfPointt.setPt(point11);

                if (addingToBlock == false) {
                    entities.add(dxfPointt);
                }
            } else if (dxfEntity instanceof DxfText) {
                dxfText = (DxfText) dxfEntity;

                if (dxfText.getTwoPointsFlag()) {
                    point1 = dxfText.pts[0];
                    pointAux = new Point2D.Double(point1.getX() - bPointX,
                                                  point1.getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    point11.setLocation(laX, laY);

                    //point11.setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    point2 = dxfText.pts[1];
                    pointAux = new Point2D.Double(point2.getX() - bPointX,
                                                  point2.getY() - bPointY);
                    laX = entity.pt.getX() +
                          (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                          ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    laY = entity.pt.getY() +
                          (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                          ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    point22.setLocation(laX, laY);

                    //point22.setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point2.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point2.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point2.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point2.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    DxfText dxfTextt = new DxfText(proj, layer,
                                                   dxfText.getText());
                    dxfTextt.pts[0] = point11;
                    dxfTextt.pts[1] = point22;

                    if (addingToBlock == false) {
                        entities.add(dxfTextt);
                    }
                } else {
                    point1 = dxfText.getPt();
                    pointAux = new Point2D.Double(point1.getX() - bPointX,
                                                  point1.getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    point11.setLocation(laX, laY);

                    //point11.setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((point1.getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + point1.getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((point1.getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + point1.getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                    DxfText dxfTextt = new DxfText(proj, layer,
                                                   dxfText.getText());
                    dxfTextt.setPt(point11);

                    if (addingToBlock == false) {
                        entities.add(dxfTextt);
                    }
                }
            } else if (dxfEntity instanceof DxfSolid) {
                dxfSolid = (DxfSolid) dxfEntity;

                Point2D[] points = new Point2D[dxfSolid.pts.length];
                Point2D[] pointss = new Point2D[dxfSolid.pts.length];

                for (int j = 0; j < dxfSolid.pts.length; j++) {
                    points[j] = (Point2D) dxfSolid.pts[j];
                    pointss[j] = new Point2D.Double();
                    pointAux = new Point2D.Double(points[j].getX() - bPointX,
                                                  points[j].getY() - bPointY);

                    double laX = entity.pt.getX() +
                                 (((pointAux.getX() * sFactorX) * Math.cos(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * (-1) * Math.sin(rAngleRad)));
                    double laY = entity.pt.getY() +
                                 (((pointAux.getX() * sFactorX) * Math.sin(rAngleRad)) +
                                 ((pointAux.getY() * sFactorY) * Math.cos(rAngleRad)));
                    pointss[j].setLocation(laX, laY);

                    //pointss[j].setLocation(entity.pt.getX() - (entity.block.bPoint.getX() * entity.getScaleFactor().getX()) + ((points[j].getX()*Math.cos((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*(-1)*Math.sin((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getX()), entity.pt.getY() - (entity.block.bPoint.getY() * entity.getScaleFactor().getY()) + ((points[j].getX()*Math.sin((entity.rotAngle*Math.PI)/180.0) + points[j].getY()*Math.cos((entity.rotAngle*Math.PI)/180.0)) * entity.scaleFactor.getY()));
                }

                DxfSolid dxfSolidd = new DxfSolid(proj, layer, pointss);
                Point2D aux = dxfSolidd.pts[2];
                dxfSolidd.pts[2] = dxfSolidd.pts[3];
                dxfSolidd.pts[3] = aux;

                if (addingToBlock == false) {
                    entities.add(dxfSolidd);
                }
            } else {
                System.out.println("gestionaInserts: Encontrado elemento desconocido");
            }
        }
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

        // TODO setNewAttributes();
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
        Point2D pt = null;

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

        DxfLayer layer = (DxfLayer) layers.getByName(grp.getDataAsString(8));
        DxfAttrib entity = new DxfAttrib(proj, layer);

        if (grp.hasCode(1)) {
            String strAux1 = grp.getDataAsString(1);
            strAux1 = DxfConvTexts.ConvertText(strAux1);
            defaultValue = strAux1;
            att[1] = DxfConvTexts.ConvertText(defaultValue);
            defValDefined = true;

            /*if (tagDefined) {
                    insFea.setProp(att[0], att[1]);
                    ptFea.setProp(att[0], att[1]);
            }
            feature.setProp("text", strAux1);*/
        }

        if (grp.hasCode(2)) {
            String strAux2 = grp.getDataAsString(2);
            strAux2 = DxfConvTexts.ConvertText(strAux2);
            tagString = strAux2;
            att[0] = DxfConvTexts.ConvertText(tagString);
            tagDefined = true;

            /*if (defValDefined) {
                    insFea.setProp(att[0], att[1]);
                    ptFea.setProp(att[0], att[1]);
            }*/
        }

        if (grp.hasCode(7)) {
            textStyleName = grp.getDataAsString(7);
            textStyleName = DxfConvTexts.ConvertText(textStyleName);
        }

        x = grp.getDataAsDouble(10);
        y = grp.getDataAsDouble(20);
        z = grp.getDataAsDouble(30);

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
        entity.setPt(proj.createPoint(x, y));

        if (grp.hasCode(40)) {
            Double heightD = new Double(grp.getDataAsDouble(40));
            String heightS = heightD.toString();

            //feature.setProp("textHeight", heightS);
        } else {
            //feature.setProp("textHeight", "20.0");
        }

        if (grp.hasCode(50)) {
            Double rotD = new Double(grp.getDataAsDouble(50));
            String rotS = rotD.toString();

            //feature.setProp("textRotation", rotS);
        } else {
            //feature.setProp("textRotation", "0.0");
        }

        if (grp.hasCode(62)) {
            entity.dxfColor = grp.getDataAsInt(62);
        } else {
            //entity.dxfColor = 0;
        }

        if (grp.hasCode(70)) {
            attributeFlags = grp.getDataAsInt(70);
        }

        if (attributeFlags == 8) {
            if (addingToBlock == false) {
                entities.add(entity);
            } else {
                blk.add(entity);
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
     * @see org.cresques.io.DxfFile.EntityFactory#depureAttributes()
     */
    public void depureAttributes() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.cresques.io.DxfFile.EntityFactory#isDxf3DFile()
     */
    public boolean isDxf3DFile() {
        // TODO Auto-generated method stub
        return false;
    }
}
