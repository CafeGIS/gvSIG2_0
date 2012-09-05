package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public interface IFFrameGroupSelectable {

public Rectangle2D getMovieRectGroup(int difX,int difY);
public int getContainsGroup(Point2D p);
public void drawHandlersGroup(Graphics2D g);
public boolean containsGroup(Point2D p);
public void setSelectedGroup(Point2D p,MouseEvent e);
public int getSelectedGroup();
public void selectFFrame(boolean b);
public IFFrame joinFFrame();
public Image getMapCursorGroup(Point2D p);
}
