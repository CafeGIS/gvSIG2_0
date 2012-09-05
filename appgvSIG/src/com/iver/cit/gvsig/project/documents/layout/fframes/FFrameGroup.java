/*
 * Created on 15-jul-2004
 *
 */
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
package com.iver.cit.gvsig.project.documents.layout.fframes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.gvsig.compat.print.PrintAttributes;
import org.gvsig.fmap.geom.Geometry;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameGroupDialog;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;


/**
 * FFrame que contiene a su vez un ArrayList de FFrames de cualquier tipo
 * incluso de si mismo.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGroup extends FFrame implements IFFrameUseProject, IFFrameViewDependence{
    private ArrayList<IFFrame> m_fframes = new ArrayList<IFFrame>();
    private Rectangle2D.Double rg = null;
    private AffineTransform m_at;
    private Project project;

    /**
     * Crea un nuevo FFrameGroup.
     */
    public FFrameGroup() {
    }

    /**
     * Añade al Arraylist un nuevo FFrame para formar parte del grupo.
     *
     * @param fframe FFrame a añadir.
     */
    public void addFFrame(IFFrame fframe) {
        m_fframes.add(fframe);
    }

    /**
     * Devuelve una ArrayList que contiene todos los FFrames que forman parte
     * del grupo.
     *
     * @return Arraylist con los fframes.
     */
    public IFFrame[] getFFrames() {
        return m_fframes.toArray(new IFFrame[0]);
    }

    /**
     * Devuelve el rectángulo que contiene a todos los fframes seleccionados.
     *
     * @param at Matriz de transformación
     *
     * @return Rectángulo.
     */
    public Rectangle2D.Double getRectangle(AffineTransform at) {
        boolean first = true;
        Rectangle2D.Double rec = new Rectangle2D.Double();
        IFFrame[] fframes=getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            Rectangle2D.Double rs = fframes[i].getBoundingBox(at);

            if (first) {
                rec.setRect(rs);
                first = false;
            }

            rec.add(rs);
        }

        rg = new Rectangle2D.Double();
        rg.setRect(FLayoutUtilities.toSheetRect(rec, m_at));

        return rec;
    }

    /**
     * Método que dibuja sobre el graphics que se le pasa como parámetro, según
     * la transformada afin que se debe de aplicar y el rectángulo que se debe
     * de dibujar.
     *
     * @param g Graphics
     * @param at Transformada afin.
     * @param rv rectángulo sobre el que hacer un clip.
     * @param imgBase Imagen utilizada para acelerar el dibujado.
     */
    public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
        BufferedImage imgBase) {
        Rectangle2D.Double r = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
        m_at = at;

        for (int i = 0; i < m_fframes.size(); i++) {
            m_fframes.get(i).draw(g, at, rv, imgBase);
        }

        g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
    }

    /**
     * Rellena la transformada que se esta utilizando en el Layout.
     *
     * @param at Matriz de transformación.
     */
    public void setAt(AffineTransform at) {
        m_at = at;
    }

    /**
     * Reimplementación del método papa poder modificar los BoundBox  de cada
     * uno de los FFrames que contiene dentro este FFrameGroup.
     *
     * @param r Rectángulo.
     */
    public void setBoundBox(Rectangle2D r) {
        getBoundBox().setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());

        double dx = 1;
        double dy = 1;
        double dw = 1;
        double dh = 1;

        if (rg != null) {
            Rectangle2D.Double raux1 = new Rectangle2D.Double(rg.x, rg.y,
                    rg.width, rg.height);
            dx = r.getX() - raux1.x;
            dy = r.getY() - raux1.y;
            dw = r.getWidth() / raux1.width;
            dh = r.getHeight() / raux1.height;
            IFFrame[] fframes=getFFrames();
            for (int i = 0; i < fframes.length; i++) {
                IFFrame fframe = fframes[i];
                Rectangle2D.Double raux = new Rectangle2D.Double();
                raux.setRect(fframe.getBoundBox());

                AffineTransform escalado = new AffineTransform();

                escalado.setToScale(dw, dh);
                escalado.translate(dx - r.getX(), dy - r.getY());

                Point2D.Double pd = new Point2D.Double();
                escalado.transform(new Point2D.Double(raux.x, raux.y), pd);

                raux.x = pd.x + r.getX();
                raux.y = pd.y + r.getY();
                raux.width = raux.width * dw;
                raux.height = raux.height * dh;

                fframe.setBoundBox(raux);
            }
        } else {
            rg = new Rectangle2D.Double();
            rg.setRect(r);
        }

        rg.setRect(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SaveException
     *
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getXMLEntity()
     */
    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();
//        xml.putProperty("type", Layout.RECTANGLEGROUP);
        IFFrame[] fframes=getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            xml.addChild(fframes[i].getXMLEntity());
        }

        return xml;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity)
     */
    public void setXMLEntity(XMLEntity xml) {
        if (xml.getIntProperty("m_Selected") != 0) {
            this.setSelected(true);
        } else {
            this.setSelected(false);
        }

        setRotation(xml.getDoubleProperty("m_rotation"));

        for (int i = 0; i < xml.getChildrenCount(); i++) {
            try {
                IFFrame frame = FFrame.createFromXML(xml.getChild(i), project,getLayout());
                this.addFFrame(frame);
            } catch (OpenException e) {
                e.showError();
            }
        }
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
     */
    public String getNameFFrame() {
        return PluginServices.getText(this, "grupo")+ num;
    }

    /**
     * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#print(java.awt.Graphics2D,
     *      java.awt.geom.AffineTransform)
     */
    public void print(Graphics2D g, AffineTransform at, Geometry geom,
			PrintAttributes printingProperties) {
        Rectangle2D.Double r = getBoundingBox(at);
        g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
        IFFrame[] fframes=m_fframes.toArray(new IFFrame[0]);
        for (int i = 0; i < fframes.length; i++) {
//            fframes[i].setPrintingProperties(printingProperties);
        	fframes[i].print(g, at, geom, printingProperties);
//        	fframes[i].setPrintingProperties(null);
        }

        g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2),
            r.y + (r.height / 2));
    }

    /**
     * Inserta una referencia al proyecto nesecario.
     *
     * @param project DOCUMENT ME!
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layout DOCUMENT ME!
     */
    public void setLayout(Layout layout) {
        super.setLayout(layout);
    	IFFrame[] fsoriginal= layout.getLayoutContext().getAllFFrames();
        IFFrame[] fs = getFFrames();

        for (int i = 0; i < fs.length; i++) {
        	fs[i].setLayout(layout);

            if (fs[i] instanceof IFFrameViewDependence) {
                ((IFFrameViewDependence) fs[i]).initDependence(fsoriginal);
            }
        }
    }

	public void initialize() {
		// TODO Auto-generated method stub

	}
	public void clearFFrames(){
		m_fframes.clear();
	}
	public IFFrame removeFFrame(int i){
		return m_fframes.remove(i);
	}
	public void removeFFrame(IFFrame fframe){
		m_fframes.remove(fframe);
	}
	public void cloneActions(IFFrame frame) {
		// TODO Auto-generated method stub
	}
	public IFFrame cloneFFrame(Layout layout) {
		FFrameGroup frame =(FFrameGroup)FrameFactory.createFrameFromName(FFrameGroupFactory.registerName);
		frame.setSelected(this.getSelected()!=IFFrame.NOSELECT);
		frame.setLevel(this.getLevel());
	    frame.setNum(this.num);
	    frame.setName(this.getName());
	    frame.setBoundBox(this.getBoundBox());
	    frame.setTag(this.getTag());
	    frame.setRotation(this.getRotation());
	    frame.setLayout(layout);
	    frame.m_at=m_at;
	    for(int i=0;i<m_fframes.size();i++) {
	    	frame.addFFrame(m_fframes.get(i).cloneFFrame(layout));
	    }
	    return frame;
	}
	public IFFrameDialog getPropertyDialog() {
		return new FFrameGroupDialog(getLayout(),this);
	}

	public void setFFrameDependence(IFFrame f) {
		IFFrame[] frames=getFFrames();
		for (int i =0;i<frames.length;i++){
			if (frames[i] instanceof IFFrameViewDependence){
				((IFFrameViewDependence)frames[i]).setFFrameDependence(f);
			}
		}

	}

	public IFFrame[] getFFrameDependence() {
		IFFrame[] frames=getFFrames();
		ArrayList<IFFrame> dependences=new ArrayList<IFFrame>();
		for (int i =0;i<frames.length;i++){
			if (frames[i] instanceof IFFrameViewDependence){
				IFFrame[] framesAux=((IFFrameViewDependence)frames[i]).getFFrameDependence();
					for (int j =0;j<framesAux.length;j++){
						dependences.add(framesAux[i]);
					}
			}
		}
		return dependences.toArray(new IFFrame[0]);
	}

	public void initDependence(IFFrame[] fframes) {
		IFFrame[] frames=getFFrames();
		for (int i =0;i<frames.length;i++){
			if (frames[i] instanceof IFFrameViewDependence){
				((IFFrameViewDependence)frames[i]).initDependence(fframes);
			}
		}
	}

	public void refreshDependence(IFFrame fant, IFFrame fnew) {
		IFFrame[] frames=getFFrames();
		for (int i =0;i<frames.length;i++){

			if (fnew instanceof FFrameGroup){
				IFFrame[] framesGroupNew=((FFrameGroup)fnew).getFFrames();
				for (int j=0;j<framesGroupNew.length;j++){
					if (fant instanceof FFrameGroup){
						IFFrame[] framesGroupAnt=((FFrameGroup)fant).getFFrames();
						for (int k=0;k<framesGroupAnt.length;k++){
							if (framesGroupAnt[k] instanceof IFFrameViewDependence){
								refreshDependence(framesGroupAnt[k],framesGroupNew[j]);
							}
						}
					}
				}
			}else if (frames[i] instanceof IFFrameViewDependence){
				((IFFrameViewDependence)frames[i]).refreshDependence(fant,fnew);
			}
		}

	}
}
