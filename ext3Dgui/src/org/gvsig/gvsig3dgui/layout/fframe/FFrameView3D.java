/*
 * Created on 20-feb-2004
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
package org.gvsig.gvsig3dgui.layout.fframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.print.attribute.PrintRequestAttributeSet;

import org.cresques.cts.IProjection;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3d.map3d.ViewPort3D;
import org.gvsig.gvsig3dgui.ProjectView3D;
import org.gvsig.gvsig3dgui.ProjectView3DFactory;
import org.gvsig.gvsig3dgui.layout.fframe.gui.dialog.FFrameView3DDialog;
import org.gvsig.gvsig3dgui.view.View3D;
import org.gvsig.osgvp.viewer.ISlaveComponent;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.ColorEvent;
import com.iver.cit.gvsig.fmap.ExtentEvent;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ProjectionEvent;
import com.iver.cit.gvsig.fmap.ViewPortListener;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.layers.LegendChangedEvent;
import com.iver.cit.gvsig.fmap.layers.XMLException;
import com.iver.cit.gvsig.fmap.rendering.LegendListener;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.LayoutControl;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseProject;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameViewDependence;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectViewBase;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.utiles.XMLEntity;

/**
 * FFrame para introducir una vista en el Layout.
 * 
 * @author Vicente Caballero Navarro
 */
public class FFrameView3D extends FFrame implements ViewPortListener,
		LegendListener, IFFrameUseProject, IFFrameUseFMap, ISlaveComponent {
	// private static Logger logger =
	// Logger.getLogger(FFrameView.class.getName());
	public static final int PRESENTACION = 0;
	public static final int BORRADOR = 1;
	protected int m_Mode;
	protected int m_typeScale = AUTOMATICO;
	protected int m_extension = 0;
	protected int m_quality = PRESENTACION;
	protected int m_viewing = 0;
	protected boolean m_bLinked = true;
	protected ProjectViewBase view = null;
	protected MapContext3D m_fmap = null;
	protected double m_Scale = 0;
	protected int m_mapUnits = 1; // Metros.

	private BufferedImage m_image = null;
	private AffineTransform at = null;
	private Project project = null;
	private double scaleAnt;
	private boolean refresh = false;
	private Point origin;
	private Point2D p1;
	private Point2D p2;
	private String titleWindow;
	private String className = this.getClass().getName();

	/**
	 * Creates a new FFrameView object.
	 */
	public FFrameView3D() {
		num++;
	}

	public String getClassName() {
		return className;
	}
	
	/**
	 * Devuelve una descripción del FFrameView.
	 * 
	 * @return Descripción.
	 */
	public String toString() {
		if (getView() == null) {
			return "FFrameView " + num + ": " + "Vacio";
		}

		return "FFrameView " + num + ": " + getView().getName();
	}

	/**
	 * Rellena la escala de la vista que contiene el fframe.
	 * 
	 * @param d
	 *            escala de la vista.
	 */
	public void setScale(double d) {
		m_Scale = d;
	}

	/**
	 * Inserta el nuevo extent a la FFrameView.
	 * 
	 * @param r
	 *            Rectángulo a ocupar por el FFrameView.
	 */
	public void setNewExtent(Rectangle2D r) {
		getMapContext().getViewPort().setExtent(r);
		refresh = true;
		m_Scale = FLayoutUtilities.getScaleView(getMapContext().getViewPort(),
				getBoundBox().width, getBoundingBox(null).width
						/ getBoundBox().width);
	}

	/**
	 * Devuelve el FMap de la vista o una clonación de este si se utiliza una
	 * escala fija.
	 * 
	 * @return FMap.
	 */
	public MapContext3D getMapContext() {
		return m_fmap;
	}

	/**
	 * Rellena la calidad que se quiere aplicar.
	 * 
	 * @param q
	 *            entero que representa la calidad a aplicar.
	 */
	public void setQuality(int q) {
		m_quality = q;
	}

	/**
	 * Devuelve un entero que representa la calidad que está seleccionada.
	 * 
	 * @return tipo de calidad selccionada.
	 */
	public int getQuality() {
		return m_quality;
	}

	/**
	 * Devuelve un entero que representa la forma en que se actualiza la vista.
	 * 
	 * @return forma que se actualiza la vista.
	 */
	public int getViewing() {
		return m_viewing;
	}

	/**
	 * Rellena la forma de actualizar la vista(cuando activo o siempre). De
	 * momento esta opción esta deshabilitada.
	 * 
	 * @param v
	 *            entero que representa la forma de actualizar la vista.
	 */
	public void setViewing(int v) {
		m_viewing = v;
	}

	/**
	 * Inserta el ProjectView de donde obtener las propiedades de la vista a
	 * mostrar.
	 * 
	 * @param v
	 *            Modelo de la vista.
	 */
	public void setView(ProjectViewBase v) {
		view = v;
		m_fmap = (MapContext3D) v.getMapContext();
		if(m_fmap.getCanvas3d() != null)
			m_fmap.getCanvas3d().addSlaveComponent(this);
	
	}
	
	
	

	/**
	 * Devuelve el modelo de la vista.
	 * 
	 * @return Modelo de la vista.
	 */
	public ProjectViewBase getView() {
		return view;
	}

	/**
	 * Devuelve un Rectángulo que representa el extent de la vista que se
	 * requiere a partir de una escala.
	 * 
	 * @param scale
	 *            Escala a mostrar.
	 * 
	 * @return Rectángulo.
	 */
	protected Rectangle2D getNewExtent(long scale) {
		double hview = getBoundBox().getHeight();
		double wview = getBoundBox().getWidth();
		double hextent = (scale * hview) / 100.0;
		double wextent = (scale * wview) / 100.0;

		if (m_fmap.getViewPort().getExtent() == null)
			return new Rectangle2D.Double();
		double newx = m_fmap.getViewPort().getExtent().getCenterX()
				- (wextent / 2.0);
		double newy = m_fmap.getViewPort().getExtent().getCenterY()
				- (hextent / 2.0);
		IProjection proj = m_fmap.getViewPort().getProjection();
		Rectangle2D r = new Rectangle2D.Double(newx, newy, wextent, hextent);
		if (!proj.isProjected()) {
			r = m_fmap.getViewPort().getProjection().getExtent(r, scale, wview,
					hview, 1, 100, 2.54);
		}
		return r;
	}

	/**
	 * Método que dibuja sobre el graphics que se le pasa como parámetro, según
	 * la transformada afin que se debe de aplicar y el rectángulo que se debe
	 * de dibujar.
	 * 
	 * @param g
	 *            Graphics2D
	 * @param at
	 *            Transformada afín.
	 * @param rv
	 *            rectángulo sobre el que hacer un clip.
	 * @param imgBase
	 *            Imagen para acelerar el dibujado.
	 */
	public void draw(Graphics2D g, AffineTransform at, Rectangle2D rv,
			BufferedImage imgBase) throws ReadDriverException {

		Rectangle2D.Double r = getBoundingBox(at);
		if (getRotation() != 0)
			g.rotate(Math.toRadians(getRotation()), r.x + (r.width / 2), r.y
					+ (r.height / 2));
		if (intersects(rv, r)) {
			if (getMapContext() == null) {
				drawEmpty(g);
			} else {
				if (rv != null) {
					// Dibujamos en pantalla
					Rectangle rclip = (Rectangle) g.getClipBounds().clone();
					g.clipRect((int) r.getMinX(), (int) r.getMinY(), (int) r
							.getWidth(), (int) r.getHeight());

					if (getQuality() == PRESENTACION) {
						if (rv.intersects(r)) {
							ViewPort3D viewPort = (ViewPort3D) this
									.getMapContext().getViewPort();
							Color theBackColor = viewPort.getBackColor();

							if (origin != null
									&& origin
											.equals(getLayout()
													.getLayoutControl()
													.getRectOrigin())
									&& getLayout() != null
									&& getLayout().getLayoutControl().getAT()
											.getScaleX() == scaleAnt
									&& m_image != null
									&& !refresh
									&& !(r.getWidth() > getLayout().getWidth() || r
											.getHeight() > getLayout()
											.getHeight())) {
								if (theBackColor != null) {
									g.setColor(theBackColor);
									g.fillRect((int) r.x, (int) r.y, viewPort
											.getImageWidth(), viewPort
											.getImageHeight());
								}
								g.translate(r.getX(), r.getY());
								g.drawImage(m_image, 0, 0, getLayout()
										.getLayoutControl());
								g.translate(-r.getX(), -r.getY());
								scaleAnt = getLayout().getLayoutControl()
										.getAT().getScaleX();
								origin = (Point) getLayout().getLayoutControl()
										.getRectOrigin().clone();
							} else {

								// System.err.println("r : " + r);
								if (r.getWidth() > getLayout().getWidth()
										|| r.getHeight() > getLayout()
												.getHeight()) {
									getMapContext().getViewPort().setOffset(
											new Point2D.Double(r.getX(), r
													.getY()));
									getMapContext().getViewPort().setImageSize(
											new Dimension((int) r.getWidth(),
													(int) r.getHeight()));
//									MapContext3D fmap = (MapContext3D) getMapContext()
//											.cloneToDraw();
									ViewPort3D viewp =(ViewPort3D)this.getMapContext().getViewPort();//.cloneViewPort();
									viewp.setImageSize(new Dimension(
											getLayout().getWidth(), getLayout()
													.getHeight()));
									Rectangle2D r1 = calculateExtent();
									double width = getLayout()
											.getLayoutContext().getAtributes()
											.getSizeInUnits().getAncho();
									double scale = FLayoutUtilities
											.getScaleView(viewp, width, r1
													.getWidth()
													/ width);
									viewp.setExtent(r1);

									getMapContext().setViewPort(viewp);
									g.translate(-r.getX(), -r.getY());
									if (theBackColor != null) {
										g.setColor(theBackColor);
										g.fillRect((int) r.x, (int) r.y, viewp
												.getImageWidth(), viewp
												.getImageHeight());
									}
									getMapContext().draw(imgBase, g, scale);
									g.translate(r.getX(), r.getY());

								} else {
									getMapContext().getViewPort().setOffset(
											new Point2D.Double(r.x, r.y));
									getMapContext().getViewPort().setImageSize(
											new Dimension((int) r.width,
													(int) r.height));
									m_image = new BufferedImage((int) r
											.getWidth(), (int) r.getHeight(),
											BufferedImage.TYPE_INT_ARGB);
									Graphics2D gimg = (Graphics2D) m_image
											.getGraphics();
									gimg.translate(-((int) r.getX()), -((int) r
											.getY()));
									getMapContext().draw(m_image, gimg,
											getScale());
									gimg.translate(((int) r.getX()), ((int) r
											.getY()));
									if (theBackColor != null) {
										g.setColor(theBackColor);
										g.fillRect((int) r.x, (int) r.y,
												viewPort.getImageWidth(),
												viewPort.getImageHeight());
									}
									g.drawImage(m_image, (int) r.getX(),
											(int) r.getY(), getLayout());
								}
								scaleAnt = getLayout().getLayoutControl()
										.getAT().getScaleX();
								origin = (Point) getLayout().getLayoutControl()
										.getRectOrigin().clone();
								refresh = false;
							}
						}
					} else {
						drawDraft(g);
					}
					if (rclip != null) {
						g.setClip(rclip.x, rclip.y, rclip.width, rclip.height);
					}
				} else {
					printX(g, at);
				}
			}
		}
		if (getRotation() != 0)
			g.rotate(Math.toRadians(-getRotation()), r.x + (r.width / 2), r.y
					+ (r.height / 2));

		if (getMapContext() != null) {
			setATMap(getMapContext().getViewPort().getAffineTransform());

		}
	}

	private Rectangle2D calculateExtent() {
		Rectangle2D.Double r = new Rectangle2D.Double();
		if (p1 == null || p2 == null)
			return r;
		r.setFrameFromDiagonal(p1, p2);
		return r;
	}

	public void print(Graphics2D g, AffineTransform at, FShape shape,
			PrintRequestAttributeSet prroperties) throws ReadDriverException {
		draw(g, at, null, null);
	}

	private void printX(Graphics2D g, AffineTransform at) {
		Rectangle2D.Double r = getBoundingBox(at);

		// Dibujamos en impresora
		Rectangle rclip = g.getClipBounds();
		g.clipRect((int) r.getMinX(), (int) r.getMinY(), (int) r.getWidth(),
				(int) r.getHeight());
		this.getMapContext().getViewPort().setOffset(
				new Point2D.Double(r.x, r.y));
		this.getMapContext().getViewPort().setImageSize(
				new Dimension((int) r.width, (int) r.height));

		Dimension imageSize = this.getMapContext().getViewPort().getImageSize();

		try {
			this.getMapContext().print(g, getScale(), null);
		} catch (ReadDriverException e) {
			NotificationManager.addError(e.getMessage(), e);
		}

		g.setClip(rclip.x, rclip.y, rclip.width, rclip.height);

		// this.getMapContext().draw(
		// g,
		// (new Rectangle((int) r.getMinX(), (int) r.getMinY(), (int) r
		// .getWidth(), (int) r.getHeight())));
		// this.getMapContext().draw(image ,g,null,0);

	}

	/**
	 * Rellena la unidad de medida en la que está la vista.
	 * 
	 * @param i
	 *            entero que representa la unidad de medida de la vista.
	 */
	public void setMapUnits(int i) {
		m_mapUnits = i;
	}

	/**
	 * Obtiene la unidad de medida en la que está la vista.
	 * 
	 * @return Unidad de medida.
	 */
	public int getMapUnits() {
		return m_mapUnits;
	}

	/**
	 * Devuelve la escala según el tipo de escala que se haya seleccionado al
	 * añadida la vista.
	 * 
	 * @return escala.
	 */
	public long getScale() {
		/*
		 * if (m_bLinked){ return getScaleView1(METROS); }
		 */
		if (getMapContext() == null)
			return 0;
		if (getTypeScale() == AUTOMATICO) {
			return FLayoutUtilities.getScaleView(getMapContext().getViewPort(),
					getBoundBox().width, getBoundingBox(null).width);
		} else if (getTypeScale() == CONSTANTE) {
			return (long) m_Scale;
		} else if (getTypeScale() == MANUAL) {
			return (long) m_Scale;
		}

		return (long) m_Scale;
	}

	/**
	 * Seleccionar si la vista esta relacionada o no con la original.
	 * 
	 * @param b
	 *            true si está ligada y false si no lo está.
	 */
	public void setLinked(boolean b) {
		m_bLinked = b;
	}

	/**
	 * Devuelve si está ligada o no el FFrameView con la vista.
	 * 
	 * @return True si la vista está ligada.
	 */
	public boolean getLinked() {
		return m_bLinked;
	}

	/**
	 * Devuelve la opción seleccionada:Rellenar marco de la vista o recorte a la
	 * vista.
	 * 
	 * @return entero que representa la opción elegida.
	 */
	public int getExtension() {
		return m_extension;
	}

	/**
	 * Devuelve el tipo de escala que está seleccionada AUTOMATICO,CONSTANTE o
	 * MANUAL.
	 * 
	 * @return entero que representa el tipo seleccionado.
	 */
	public int getTypeScale() {
		return m_typeScale;
	}

	/**
	 * Rellenar si se quiere:Rellenar marco de la vista o recorte a la vista.
	 * 
	 * @param i
	 *            entero que representa la opción elegida.
	 */
	public void setExtension(int i) {
		m_extension = i;
	}

	/**
	 * Rellenar el tipo de escala que se desea.
	 * 
	 * @param i
	 *            entero que representa el tipo de escala.
	 */
	public void setTypeScale(int i) {
		m_typeScale = i;
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

		try {
			// xml.putProperty("type", Layout.RECTANGLEVIEW);
		//	xml.putProperty("className", this.getClassName());
		
			xml.putProperty("m_Mode", m_Mode);
			xml.putProperty("m_typeScale", m_typeScale);
			xml.putProperty("m_extension", m_extension);
			xml.putProperty("m_quality", m_quality);
			xml.putProperty("m_viewing", m_viewing);
			xml.putProperty("m_bLinked", m_bLinked);
			xml.putProperty("m_mapUnits", m_mapUnits);
			xml.putProperty("m_Scale", m_Scale);

			ProjectExtension pe = (ProjectExtension) PluginServices
					.getExtension(ProjectExtension.class);
			ArrayList views = pe.getProject().getDocumentsByType(
					ProjectView3DFactory.registerName);

			boolean hasIndex = false;

			if (view != null) {
				xml.putProperty("viewName", view.getName());
				for (int i = 0; i < views.size(); i++) {
					if (view.getName().equals(
							((ProjectViewBase) views.get(i)).getName())) {
						xml.putProperty("indice", i);
						hasIndex = true;
						break;
					}
				}
				
			}
			
//			titleWindow = ((ProjectView3D) view).getView().getWindowInfo().getTitle();
//			xml.putProperty("titleWindow", titleWindow);

			if (!hasIndex) {
				xml.putProperty("indice", -1);
			}

			if (getMapContext() != null
					&& getMapContext().getViewPort().getExtent() != null) {
				xml.putProperty("extentX", getMapContext().getViewPort()
						.getExtent().getX());
				xml.putProperty("extentY", getMapContext().getViewPort()
						.getExtent().getY());
				xml.putProperty("extentW", getMapContext().getViewPort()
						.getExtent().getWidth());
				xml.putProperty("extentH", getMapContext().getViewPort()
						.getExtent().getHeight());

				xml.addChild(getMapContext().getXMLEntity());
			}
		} catch (Exception e) {
			throw new SaveException(e, this.getClass().getName());
		}

		return xml;
	}

	/**
	 * Inserta la imagen para repintar el FFrameView.
	 * 
	 * @param bi
	 *            Imagen para repintar.
	 */
	public void setBufferedImage(BufferedImage bi) {
		m_image = bi;
	}

	/**
	 * Devuelve la imagen para repintar.
	 * 
	 * @return Imagen para repintar.
	 */
	public BufferedImage getBufferedImage() {
		return m_image;
	}

	/**
	 * Devuelve la MAtriz de transformación utilizada por la FFrameView.
	 * 
	 * @return MAtriz de transformación.
	 */
	public AffineTransform getATMap() {
		return at;
	}

	/**
	 * Inserta la matriz de transformación.
	 * 
	 * @param transform
	 *            Matriz de transformación.
	 */
	public void setATMap(AffineTransform transform) {
		at = transform;
	}

	/**
	 * Inserta el proyecto.
	 * 
	 * @param p
	 *            Proyecto.
	 */
	public void setProject(Project p) {
		project = p;
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#setXMLEntity(com.iver.utiles.XMLEntity,
	 *      com.iver.cit.gvsig.project.Project)
	 */
	public void setXMLEntity03(XMLEntity xml, Layout l) {
		if (xml.getIntProperty("m_Selected") != 0) {
			this.setSelected(true);
		} else {
			this.setSelected(false);
		}

		this.setName(xml.getStringProperty("m_name"));
		this.setBoundBox(new Rectangle2D.Double(xml.getDoubleProperty("x"), xml
				.getDoubleProperty("y"), xml.getDoubleProperty("w"), xml
				.getDoubleProperty("h")));

		this.m_Mode = xml.getIntProperty("m_Mode");
		this.m_typeScale = xml.getIntProperty("m_typeScale");
		this.m_extension = xml.getIntProperty("m_extension");
		this.m_quality = xml.getIntProperty("m_quality");
		this.m_viewing = xml.getIntProperty("m_viewing");
		this.m_bLinked = xml.getBooleanProperty("m_bLinked");
		this.m_mapUnits = xml.getIntProperty("m_mapUnits");

		// ProjectExtension pe = (ProjectExtension)
		// PluginServices.getExtension(ProjectExtension.class);
		this.m_Scale = xml.getDoubleProperty("m_Scale");

		int indice = xml.getIntProperty("indice");

		if (indice != -1) {
			ArrayList views = project
					.getDocumentsByType(ProjectView3DFactory.registerName);

			ProjectViewBase view = (ProjectViewBase) views.get(indice);
			this.m_fmap = (MapContext3D) view.getMapContext();
			this.setView(view);

			try {
				if (m_bLinked) {
					this.getMapContext().getViewPort().setExtent(
							new Rectangle2D.Double(xml
									.getDoubleProperty("extentX"), xml
									.getDoubleProperty("extentY"), xml
									.getDoubleProperty("extentW"), xml
									.getDoubleProperty("extentH")));
				} else if (!m_bLinked) {
					this.m_fmap = (MapContext3D) MapContext.createFromXML03(xml
							.getChild(0));
				}
			} catch (XMLException e) {
				NotificationManager.addError(
						"Pasando las propiedades del XMLEntity al objeto", e);
			}
		}
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame#getNameFFrame()
	 */
	public String getNameFFrame() {
		return PluginServices.getText(this, "Vista") + num;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.ExtentListener#extentChanged(com.iver.cit.gvsig.fmap.ExtentEvent)
	 */
	public void extentChanged(ExtentEvent e) {
		if (getTypeScale() == AUTOMATICO) {
			m_fmap.getViewPort().setExtent(e.getNewExtent());
			if (getLayout() != null) {
				getLayout().getLayoutControl().setStatus(
						LayoutControl.DESACTUALIZADO);
			}
		}
		refresh = true;
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#backColorChanged(com.iver.cit.gvsig.fmap.ColorEvent)
	 */
	public void backColorChanged(ColorEvent e) {
		if (getLinked()) {
			m_fmap.getViewPort().setBackColor(e.getNewColor());
			getLayout().getLayoutControl().setStatus(
					LayoutControl.DESACTUALIZADO);

			// setBufferedImage(null);
		}
	}

	/**
	 * @see com.iver.cit.gvsig.fmap.ViewPortListener#projectionChanged(com.iver.cit.gvsig.fmap.ProjectionEvent)
	 */
	public void projectionChanged(ProjectionEvent e) {
		if (getTypeScale() == AUTOMATICO) {
			m_fmap.getViewPort().setProjection(e.getNewProjection());

			if (getLayout() != null) {
				getLayout().getLayoutControl().setStatus(
						LayoutControl.DESACTUALIZADO);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void legendChanged(LegendChangedEvent e) {
		if (getLinked()) {
			getLayout().getLayoutControl().setStatus(
					LayoutControl.DESACTUALIZADO);
			refresh = true;
			// setBufferedImage(null);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param xml
	 *            DOCUMENT ME!
	 */
	public void setXMLEntity(XMLEntity xml) {
		
//		
		if (xml.getIntProperty("m_Selected") != 0) {
			this.setSelected(true);
		} else {
			this.setSelected(false);
		}

		this.setName(xml.getStringProperty("m_name"));
		this.setBoundBox(new Rectangle2D.Double(xml.getDoubleProperty("x"), xml
				.getDoubleProperty("y"), xml.getDoubleProperty("w"), xml
				.getDoubleProperty("h")));

		this.m_Mode = xml.getIntProperty("m_Mode");
		this.m_typeScale = xml.getIntProperty("m_typeScale");
		this.m_extension = xml.getIntProperty("m_extension");
		this.m_quality = xml.getIntProperty("m_quality");
		this.m_viewing = xml.getIntProperty("m_viewing");
		this.m_bLinked = xml.getBooleanProperty("m_bLinked");
		this.m_mapUnits = xml.getIntProperty("m_mapUnits");
		setRotation(xml.getDoubleProperty("m_rotation"));

		this.m_Scale = xml.getDoubleProperty("m_Scale");

		int indice = xml.getIntProperty("indice");

		ProjectViewBase view = null;

		if (xml.contains("viewName")) {
			view = (ProjectViewBase) project.getProjectDocumentByName(xml
					.getStringProperty("viewName"),
					ProjectView3DFactory.registerName);
			
	//		if (xml.contains("className"))
//				this.className=	xml.getStringProperty("className");
			
		} else {
			if (indice != -1) {
				try {
					ArrayList views = project
							.getDocumentsByType(ProjectView3DFactory.registerName);

					view = (ProjectViewBase) views.get(indice);
				} catch (IndexOutOfBoundsException e) {
					NotificationManager.addError(
							"No se ha encontrado la vista de indice " + indice,
							e);
				}
			}
		}

//		if (xml.contains("titleWindow"))
//			this.titleWindow = xml.getStringProperty("titleWindow");
		
		if (view != null) {
			this.setView(view);

			try {
				if (xml.contains("extentX")) {
					if (m_bLinked) {
						this.getMapContext().getViewPort().setExtent(
								new Rectangle2D.Double(xml
										.getDoubleProperty("extentX"), xml
										.getDoubleProperty("extentY"), xml
										.getDoubleProperty("extentW"), xml
										.getDoubleProperty("extentH")));
					} else if (!m_bLinked) {
						this.m_fmap = (MapContext3D) MapContext
								.createFromXML(xml.getChild(0));
					}
				}
			} catch (XMLException e) {
				NotificationManager.addError(
						"Pasando las propiedades del XMLEntity al objeto", e);
			}
		} else if (!m_bLinked) {
			try {
				this.m_fmap = (MapContext3D) MapContext.createFromXML(xml
						.getChild(0));
			} catch (XMLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param arg0
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean compare(Object arg0) {
		if (!(arg0 instanceof FFrameView3D)) {
			return false;
		}

		if (!this.getName().equals(((FFrameView3D) arg0).getName())) {
			return false;
		}

		if (Math.abs(this.getBoundBox().getWidth()
				- (((FFrameView3D) arg0).getBoundBox().getWidth())) > 0.05) {
			return false;
		}
		if (Math.abs(this.getBoundBox().getHeight()
				- (((FFrameView3D) arg0).getBoundBox().getHeight())) > 0.05) {
			return false;
		}

		if (!this.toString().equals(((FFrameView3D) arg0).toString())) {
			return false;
		}

		if (this.getMapContext() != null
				&& !this.getMapContext().equals(
						((FFrameView3D) arg0).getMapContext())) {
			return false;
		}

		if (this.getRotation() != ((FFrameView3D) arg0).getRotation()) {
			return false;
		}
		return true;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param arg0
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object arg0) {
		return this.compare(arg0);
	}

	public void refresh() {
		if (view != null
				&& (getTypeScale() == MANUAL || getTypeScale() == CONSTANTE))
			getMapContext().getViewPort().setExtent(getNewExtent(getScale()));
		refresh = true;
	}

	public void fullExtent() throws ReadDriverException {
		setNewExtent(getMapContext().getFullExtent());
	}

	public void setPointsToZoom(Point2D px1, Point2D px2) {
		p1 = px1;
		p2 = px2;
	}

	public void movePoints(Point2D px1, Point2D px2) {
		double difX = -px2.getX() + px1.getX();
		double difY = -px2.getY() + px1.getY();
		if (p1 != null) {
			p1.setLocation(p1.getX() + difX, p1.getY() + difY);
			p2.setLocation(p2.getX() + difX, p2.getY() + difY);
		}
	}

	public void cloneActions(IFFrame frame) {
		if (view == null || view.getMapContext() == null)
			return;
		if (m_bLinked) {
			if (getTypeScale() == AUTOMATICO) {
				view.getMapContext().getViewPort().addViewPortListener(this);
				view.getMapContext().addLayerListener(this);
			} else if (getTypeScale() == CONSTANTE) {
				view.getMapContext().getViewPort().removeViewPortListener(this);
				view.getMapContext().addLayerListener(this);
			} else if (getTypeScale() == MANUAL) {
				view.getMapContext().getViewPort().removeViewPortListener(this);
				view.getMapContext().addLayerListener(this);
			}
		} else if (!m_bLinked) {
			if (getTypeScale() == AUTOMATICO) {
				view.getMapContext().getViewPort().addViewPortListener(this);
			} else if (getTypeScale() == CONSTANTE) {
				view.getMapContext().getViewPort().removeViewPortListener(this);
			} else if (getTypeScale() == MANUAL) {
				view.getMapContext().getViewPort().removeViewPortListener(this);
			}
		}
		((FFrameView3D) frame).view.getMapContext().removeLayerListener(
				(FFrameView3D) frame);
		((FFrameView3D) frame).view.getMapContext().getViewPort()
				.removeViewPortListener((FFrameView3D) frame);

	}

	public IFFrame cloneFFrame(Layout layout) {
		FFrameView3D frame = (FFrameView3D) FrameFactory
				.createFrameFromName(FFrameView3DFractory.registerName);
		frame.setLevel(this.getLevel());
		frame.setNum(this.num);
		frame.setName(this.getName());
		frame.setBoundBox(this.getBoundBox());
		frame.setTag(this.getTag());
		frame.m_Mode = this.m_Mode;
		frame.m_typeScale = this.m_typeScale;
		frame.m_extension = this.m_extension;
		frame.m_quality = this.m_quality;
		frame.m_viewing = this.m_viewing;
		frame.m_bLinked = this.m_bLinked;
		frame.m_mapUnits = this.m_mapUnits;
		frame.setRotation(this.getRotation());

		frame.m_Scale = this.m_Scale;
		frame.view = this.getView();
		frame.m_fmap = this.getMapContext();
		frame.setSelected(this.getSelected() != IFFrame.NOSELECT);
		frame.setLayout(layout);
		if ((frame.view != null) && (frame.m_fmap != null))
			frame.m_fmap.getCanvas3d().addSlaveComponent(frame);

		if (frame instanceof IFFrameViewDependence) {
			((IFFrameViewDependence) frame).initDependence(layout
					.getLayoutContext().getAllFFrames());
			frame.getMapContext().setRenewCanvasOff(true);
		}
		cloneActions(frame);
		return frame;
	}

	public IFFrameDialog getPropertyDialog() {
		return new FFrameView3DDialog(getLayout(), this);
	}

	public void repaint() {
		refresh = true;
		this.getLayout().getLayoutControl().refresh();
	}
}
