/*
 * Created on 28-jul-2004
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
package com.iver.cit.gvsig.project.documents.layout.gui.dialogs;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.FLayoutGraphics;
import com.iver.cit.gvsig.project.documents.layout.LayoutControl;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphics;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGraphicsFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.FrameFactory;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.FFrameGraphicsDialog;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.GeometryAdapter;
import com.iver.cit.gvsig.project.documents.layout.geometryadapters.RectangleAdapter;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo con todas las opciones para crear un borde a los fframes.
 *
 * @author Vicente Caballero Navarro
 */
public class FBorderDialog extends JPanel implements IWindow {
	private javax.swing.JRadioButton rbSeleccionados = null;
	private javax.swing.JRadioButton rbTodos = null;
	private javax.swing.JRadioButton rbMargen = null;
	private javax.swing.JCheckBox chbAgrupar = null;
	private javax.swing.JPanel pPosicion = null;
	private javax.swing.JCheckBox chbIgualLados = null;
	private javax.swing.JLabel lunidades = null;
	private javax.swing.JTextField tTodosLados = null;
	private javax.swing.JLabel lSuperior = null;
	private javax.swing.JLabel lInferior = null;
	private javax.swing.JTextField tSuperior = null;
	private javax.swing.JTextField tInferior = null;
	private javax.swing.JLabel lIzquierda = null;
	private javax.swing.JLabel lDerecha = null;
	private javax.swing.JTextField tIzquierda = null;
	private javax.swing.JTextField tDerecha = null;
	private javax.swing.JLabel lUnidades = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private Layout layout = null;
	private IFFrame[] selecList;// = new ArrayList();
	private FFrameGraphics fframegraphics = null;
	private FLayoutGraphics flg = null;
	private String m_NameUnit = null;
	private javax.swing.JButton bConfigurar = null;
	GeometryAdapter geometry=new RectangleAdapter();
	private boolean isAccepted=false;
	private ISymbol symbol;

	/**
	 * This is the default constructor
	 *
	 * @param l Referencia al Layout.
	 */
	public FBorderDialog(Layout l) {
		super();
		layout = l;
		flg = new FLayoutGraphics(layout);
		fframegraphics =(FFrameGraphics)FrameFactory.createFrameFromName(FFrameGraphicsFactory.registerName);

		fframegraphics.setLayout(layout);

		fframegraphics.setColor(Color.black);
		fframegraphics.update(FFrameGraphics.RECTANGLE, layout.getLayoutControl().getAT());


		initialize();
		startFFrameGraphic();
	}
	private void startFFrameGraphic() {
		IFFrame[] fframes=layout.getLayoutContext().getFFrames();
		double x = Double.parseDouble(getTIzquierda().getText()
				  .toString());
		double y = Double.parseDouble(getTSuperior().getText()
				  .toString());
		double w = Double.parseDouble(getTDerecha().getText()
				  .toString());
		double h = Double.parseDouble(getTInferior().getText()
				  .toString());

		Rectangle2D re = new Rectangle2D.Double();

		if (getRbSeleccionados().isSelected()) {
			if (selecList.length > 0) {
				re=getRectangle(selecList[0]);

			}

			for (int i = 1; i < selecList.length; i++) {
				Rectangle2D rectaux = getRectangle(selecList[i]);
				re.add(rectaux);
			}

			//						crear un Rectángulo alrededor de los fframes seleccionados.
		} else if (getRbTodos().isSelected()) {
			if (fframes.length > 0) {
				re=getRectangle(fframes[0]);
			}

			for (int i = 1; i < fframes.length;
					i++) {
				Rectangle2D rectaux = getRectangle(fframes[i]);
				re.add(rectaux);
			}

			//						creaer un Rectángulo que incluya a todos los fframes.
		} else if (getRbMargen().isSelected()) {
			//						creaer un Rectángulo en los márgenes del Layout.
			re.setRect(layout.getLayoutContext().getAttributes().getArea());
		}

		re.setRect(re.getX() - x, re.getY() - y, re.getWidth() + x + w,
			re.getHeight() + y + h);

		geometry=new RectangleAdapter();
		geometry.addPoint(new Point2D.Double(re.getX(),re.getY()));
		geometry.addPoint(new Point2D.Double(re.getMaxX(),re.getMaxY()));
		geometry.end();
		fframegraphics.setGeometryAdapter(geometry);
	}
	/**
	 * This method initializes this
	 */
	private void initialize() {
		selecList=layout.getLayoutContext().getFFrameSelected();
		this.setLayout(null);
		m_NameUnit = layout.getLayoutContext().getAttributes().getNameUnit();
		this.add(getRbSeleccionados(), null);
		this.add(getRbTodos(), null);
		this.add(getRbMargen(), null);
		this.add(getChbAgrupar(), null);
		this.add(getPPosicion(), null);
		this.add(getBAceptar(), null);
		this.add(getBCancelar(), null);
		this.add(getBConfigurar(), null);
		this.setSize(371, 223);

		ButtonGroup group = new ButtonGroup();
		group.add(getRbSeleccionados());
		group.add(getRbTodos());
		group.add(getRbMargen());
	}

	/**
	 * This method initializes rbSeleccionados
	 *
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getRbSeleccionados() {
		if (rbSeleccionados == null) {
			rbSeleccionados = new javax.swing.JRadioButton();
			rbSeleccionados.setBounds(10, 8, 416, 20);
			rbSeleccionados.setText(PluginServices.getText(this,
					"colocar_alrededor_seleccionados"));

			if (selecList.length == 0) {
				rbSeleccionados.setEnabled(false);
				getRbTodos().setSelected(true);
			} else {
				rbSeleccionados.setSelected(true);
			}
		}

		return rbSeleccionados;
	}

	/**
	 * This method initializes rbTodos
	 *
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getRbTodos() {
		if (rbTodos == null) {
			rbTodos = new javax.swing.JRadioButton();
			rbTodos.setBounds(10, 29, 332, 20);
			rbTodos.setText(PluginServices.getText(this,
					"colocar_alrededor_todos"));
		}

		return rbTodos;
	}

	/**
	 * This method initializes rbMargen
	 *
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getRbMargen() {
		if (rbMargen == null) {
			rbMargen = new javax.swing.JRadioButton();
			rbMargen.setBounds(10, 51, 286, 20);
			rbMargen.setText(PluginServices.getText(this, "colocar_a_margenes"));
		}

		return rbMargen;
	}

	/**
	 * This method initializes chbAgrupar
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbAgrupar() {
		if (chbAgrupar == null) {
			chbAgrupar = new javax.swing.JCheckBox();
			chbAgrupar.setBounds(10, 70, 280, 20);
			chbAgrupar.setText(PluginServices.getText(this, "agrupar_linea"));
		}

		return chbAgrupar;
	}

	/**
	 * This method initializes pPosicion
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getPPosicion() {
		if (pPosicion == null) {
			pPosicion = new javax.swing.JPanel();
			pPosicion.setLayout(null);
			pPosicion.add(getChbIgualLados(), null);
			pPosicion.add(getLunidades(), null);
			pPosicion.add(getTTodosLados(), null);
			pPosicion.add(getLSuperior(), null);
			pPosicion.add(getLInferior(), null);
			pPosicion.add(getTSuperior(), null);
			pPosicion.add(getTInferior(), null);
			pPosicion.add(getLIzquierda(), null);
			pPosicion.add(getLDerecha(), null);
			pPosicion.add(getTIzquierda(), null);
			pPosicion.add(getTDerecha(), null);
			pPosicion.add(getLUnidades(), null);
			pPosicion.setBounds(12, 92, 350, 97);
			pPosicion.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "posicion_linea"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}

		return pPosicion;
	}

	/**
	 * This method initializes chbIgualLados
	 *
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getChbIgualLados() {
		if (chbIgualLados == null) {
			chbIgualLados = new javax.swing.JCheckBox();
			chbIgualLados.setBounds(10, 16, 243, 21);
			chbIgualLados.setText(PluginServices.getText(this,
					"igual_todos_lados"));
			chbIgualLados.setSelected(true);
			chbIgualLados.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (!chbIgualLados.isSelected()) {
							getTTodosLados().setEnabled(false);
							getTSuperior().setEnabled(true);
							getTInferior().setEnabled(true);
							getTIzquierda().setEnabled(true);
							getTDerecha().setEnabled(true);
						} else {
							getTTodosLados().setEnabled(true);
							getTSuperior().setEnabled(false);
							getTInferior().setEnabled(false);
							getTIzquierda().setEnabled(false);
							getTDerecha().setEnabled(false);
							getTSuperior().setText(tTodosLados.getText());
							getTInferior().setText(tTodosLados.getText());
							getTIzquierda().setText(tTodosLados.getText());
							getTDerecha().setText(tTodosLados.getText());
						}
					}
				});
		}

		return chbIgualLados;
	}

	/**
	 * This method initializes lunidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLunidades() {
		if (lunidades == null) {
			lunidades = new javax.swing.JLabel();
			lunidades.setBounds(264, 64, 78, 18);
			lunidades.setText(PluginServices.getText(this,m_NameUnit));
		}

		return lunidades;
	}

	/**
	 * This method initializes tTodosLados
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTTodosLados() {
		if (tTodosLados == null) {
			tTodosLados = new javax.swing.JTextField();
			tTodosLados.setBounds(267, 19, 48, 16);
			tTodosLados.setText("0.5");
			tTodosLados.addCaretListener(new javax.swing.event.CaretListener() {
					public void caretUpdate(javax.swing.event.CaretEvent e) {
						if (getChbIgualLados().isSelected()) {
							getTSuperior().setText(tTodosLados.getText());
							getTInferior().setText(tTodosLados.getText());
							getTIzquierda().setText(tTodosLados.getText());
							getTDerecha().setText(tTodosLados.getText());
						}
					}
				});
		}

		return tTodosLados;
	}

	/**
	 * This method initializes lSuperior
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLSuperior() {
		if (lSuperior == null) {
			lSuperior = new javax.swing.JLabel();
			lSuperior.setBounds(10, 55, 62, 16);
			lSuperior.setText(PluginServices.getText(this, "Superior"));
		}

		return lSuperior;
	}

	/**
	 * This method initializes lInferior
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLInferior() {
		if (lInferior == null) {
			lInferior = new javax.swing.JLabel();
			lInferior.setBounds(10, 72, 62, 16);
			lInferior.setText(PluginServices.getText(this, "Inferior"));
		}

		return lInferior;
	}

	/**
	 * This method initializes tSuperior
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTSuperior() {
		if (tSuperior == null) {
			tSuperior = new javax.swing.JTextField();
			tSuperior.setBounds(78, 55, 48, 16);
			tSuperior.setText("0.5");
			tSuperior.setEnabled(false);
		}

		return tSuperior;
	}

	/**
	 * This method initializes tInferior
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTInferior() {
		if (tInferior == null) {
			tInferior = new javax.swing.JTextField();
			tInferior.setBounds(78, 72, 48, 16);
			tInferior.setText("0.5");
			tInferior.setEnabled(false);
		}

		return tInferior;
	}

	/**
	 * This method initializes lIzquierda
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLIzquierda() {
		if (lIzquierda == null) {
			lIzquierda = new javax.swing.JLabel();
			lIzquierda.setBounds(140, 55, 62, 16);
			lIzquierda.setText(PluginServices.getText(this, "Izquierda"));
		}

		return lIzquierda;
	}

	/**
	 * This method initializes lDerecha
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLDerecha() {
		if (lDerecha == null) {
			lDerecha = new javax.swing.JLabel();
			lDerecha.setBounds(140, 72, 62, 16);
			lDerecha.setText(PluginServices.getText(this, "Derecha"));
		}

		return lDerecha;
	}

	/**
	 * This method initializes tIzquierda
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTIzquierda() {
		if (tIzquierda == null) {
			tIzquierda = new javax.swing.JTextField();
			tIzquierda.setBounds(207, 55, 48, 16);
			tIzquierda.setText("0.5");
			tIzquierda.setEnabled(false);
		}

		return tIzquierda;
	}

	/**
	 * This method initializes tDerecha
	 *
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTDerecha() {
		if (tDerecha == null) {
			tDerecha = new javax.swing.JTextField();
			tDerecha.setBounds(207, 72, 48, 16);
			tDerecha.setText("0.5");
			tDerecha.setEnabled(false);
		}

		return tDerecha;
	}

	/**
	 * This method initializes lUnidades
	 *
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLUnidades() {
		if (lUnidades == null) {
			lUnidades = new javax.swing.JLabel();
			lUnidades.setBounds(207, 36, 134, 18);
			lUnidades.setText(PluginServices.getText(this, "map_units"));
		}

		return lUnidades;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#getWindowInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this, "ajustes_linea_grafica"));

		return m_viewinfo;
	}

	/**
	 * This method initializes bAceptar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setBounds(134, 193, 100, 20);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						IFFrame[] fframes=layout.getLayoutContext().getFFrames();
						double x = Double.parseDouble(getTIzquierda().getText()
														  .toString());
						double y = Double.parseDouble(getTSuperior().getText()
														  .toString());
						double w = Double.parseDouble(getTDerecha().getText()
														  .toString());
						double h = Double.parseDouble(getTInferior().getText()
														  .toString());

						if (!getChbAgrupar().isSelected()) {
							Rectangle2D re = new Rectangle2D.Double();

							if (getRbSeleccionados().isSelected()) {
								if (selecList.length > 0) {
									re=getRectangle(selecList[0]);

								}

								for (int i = 1; i < selecList.length; i++) {
									Rectangle2D rectaux = getRectangle(selecList[i]);
									re.add(rectaux);
								}

								//						crear un Rectángulo alrededor de los fframes seleccionados.
							} else if (getRbTodos().isSelected()) {
								if (fframes.length > 0) {
									re=getRectangle(fframes[0]);
								}

								for (int i = 1; i < fframes.length;
										i++) {
									Rectangle2D rectaux = getRectangle(fframes[i]);
									re.add(rectaux);
								}

								//						creaer un Rectángulo que incluya a todos los fframes.
							} else if (getRbMargen().isSelected()) {
								//						creaer un Rectángulo en los márgenes del Layout.
								re.setRect(layout.getLayoutContext().getAttributes().getArea());
							}

							re.setRect(re.getX() - x, re.getY() - y, re.getWidth() + x + w,
								re.getHeight() + y + h);

							geometry=new RectangleAdapter();
							geometry.addPoint(new Point2D.Double(re.getX(),re.getY()));
							geometry.addPoint(new Point2D.Double(re.getMaxX(),re.getMaxY()));
							geometry.end();
							fframegraphics.setGeometryAdapter(geometry);
							fframegraphics.setBoundBox(re);
							if (symbol!=null) {
								fframegraphics.setSymbol(symbol);
							}
							layout.getLayoutContext().addFFrame(fframegraphics, true,true);
						} else { //Agrupar

							Rectangle2D re = new Rectangle2D.Double();
							if (getRbSeleccionados().isSelected()) {
								if (selecList.length > 0) {
									re=getRectangle(selecList[0]);
								}

								for (int i = 1; i < selecList.length; i++) {
									Rectangle2D rectaux = getRectangle(selecList[i]);
									re.add(rectaux);
								}

								//						crear un Rectángulo alrededor de los fframes seleccionados.
							} else if (getRbTodos().isSelected()) {
								if (fframes.length > 0) {
									re=getRectangle(fframes[0]);
								}

								for (int i = 1; i < fframes.length;
										i++) {
									Rectangle2D rectaux = getRectangle(fframes[i]);
									fframes[i].setSelected(true);
									re.add(rectaux);
								}

								//						creaer un Rectángulo que incluya a todos los fframes.
							} else if (getRbMargen().isSelected()) {
								//						creaer un Rectángulo en los márgenes del Layout.
								re.setRect(layout.getLayoutContext().getAttributes().getArea());
							}

							re.setRect(re.getX() - x, re.getY() - y, re.getWidth() + x + w,
								re.getHeight() + y + h);

							geometry=new RectangleAdapter();
							geometry.addPoint(new Point2D.Double(re.getX(),re.getY()));
							geometry.addPoint(new Point2D.Double(re.getMaxX(),re.getMaxY()));
							geometry.end();
							fframegraphics.setGeometryAdapter(geometry);
							fframegraphics.setBoundBox(re);
							if (symbol!=null) {
								fframegraphics.setSymbol(symbol);
							}
							layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this,"group_graphic_line"));
							layout.getLayoutContext().addFFrame(fframegraphics, false,true);
							flg.grouping();
							layout.getLayoutContext().getFrameCommandsRecord().endComplex();

						}

						PluginServices.getMDIManager().closeWindow(FBorderDialog.this);
						layout.getLayoutControl().setStatus(LayoutControl.DESACTUALIZADO);
						layout.getLayoutControl().repaint();
						isAccepted=true;
					}
				});
		}

		return bAceptar;
	}

	/**
	 * This method initializes bCancelar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBCancelar() {
		if (bCancelar == null) {
			bCancelar = new javax.swing.JButton();
			bCancelar.setBounds(251, 193, 100, 20);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						PluginServices.getMDIManager().closeWindow(FBorderDialog.this);
						isAccepted=false;
					}
				});
		}

		return bCancelar;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * This method initializes bConfigurar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBConfigurar() {
		if (bConfigurar == null) {
			bConfigurar = new javax.swing.JButton();
			bConfigurar.setBounds(17, 193, 100, 20);
			bConfigurar.setText(PluginServices.getText(this, "configurar"));
			bConfigurar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						FFrameGraphicsDialog graphicsdialog = new FFrameGraphicsDialog(layout,
								fframegraphics);
						PluginServices.getMDIManager().addWindow(graphicsdialog);
						symbol=(ISymbol)graphicsdialog.getSelectedObject();
					}
				});
		}

		return bConfigurar;
	}
	private Rectangle2D getRectangle(IFFrame f){
		Rectangle2D.Double re= new Rectangle2D.Double();
		re.setRect(f.getBoundBox());
		Point2D p1=new Point2D.Double();
		Point2D p2=new Point2D.Double();
		Point2D p3=new Point2D.Double();
		Point2D p4=new Point2D.Double();
		double rotation=(f).getRotation();
		AffineTransform at=new AffineTransform();
		at.rotate(Math.toRadians(rotation), re.x + (re.width / 2),
				re.y + (re.height / 2));
		at.transform(new Point2D.Double(re.getX(),re.getY()),p1);
		at.transform(new Point2D.Double(re.getMaxX(),re.getY()),p2);
		at.transform(new Point2D.Double(re.getMaxX(),re.getMaxY()),p3);
		at.transform(new Point2D.Double(re.getX(),re.getMaxY()),p4);
		if (p1.getX()<p4.getX()){
			re.x=p1.getX();
		}else{
			re.x=p4.getX();
		}
		if (p1.getY()<p2.getY()){
			re.y=p1.getY();
		}else{
			re.y=p2.getY();
		}
		if (p2.getX()>p3.getX()){
			re.width=p2.getX()-re.x;
		}else{
			re.width=p3.getX()-re.x;
		}
		if (p4.getY()>p3.getY()){
			re.height=p4.getY()-re.y;
		}else{
			re.height=p3.getY()-re.y;
		}
		return re;
	}
	public boolean isAccepted() {
		return isAccepted;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}
