package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.gvsig.fmap.geom.Geometry;
import org.gvsig.fmap.mapcontext.MapContext;
import org.gvsig.fmap.mapcontext.rendering.symbols.ISymbol;
import org.gvsig.gui.beans.AcceptCancelPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.utils.FontChooser;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGrid;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.legend.gui.PanelEditSymbol;

public class FFrameGridDialog extends JPanel implements IFFrameDialog{
	private static final long serialVersionUID = 1L;
	private JPanel jPanel1 = null;
	private JPanel jPanel4 = null;
	private JLabel jLabel = null;
	private JTextField txtIntervalX = null;
	private JPanel jPanel5 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private JLabel jLabel1 = null;
	private JTextField txtIntervalY = null;
	private JLabel lblUnitsX = null;
	private JLabel lblUnitsY = null;
	private JPanel jPanel8 = null;
	private JPanel jPanel9 = null;
	private JRadioButton rbPoints = null;
	private JRadioButton rbLines = null;
	private JButton bSymbol = null;
	private JPanel jPanel10 = null;
	private JPanel jPanel11 = null;
	private JButton jButton = null;
	private ColorChooserPanel colorChooserPanel = null;
	private JPanel jPanel12 = null;
	private JLabel jLabel2 = null;
	private JTextField txtSize = null;

	private FFrameGrid fframegrid;
	private FFrameView fframeview;
	private AcceptCancelPanel accept;
	private FFrameGrid newFFrameGrid;
	private Rectangle2D rect;  //  @jve:decl-index=0:
	private Layout layout;
//	private Color textcolor;
	private ISymbol symbol;
	private Font m_font;
	private ButtonGroup bg=new ButtonGroup();
	private JPanel jPanel15 = null;
	/**
	 * This is the default constructor
	 */
	public FFrameGridDialog(Layout layout, FFrameGrid fframe,FFrameView view) {
		super();
		this.layout=layout;
		this.fframegrid=fframe;
		this.fframeview=view;
//		textcolor=fframegrid.getFontColor();
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(350, 265);
		this.add(getJPanel1(), java.awt.BorderLayout.CENTER);
		this.add(getAcceptCancelPanel(), java.awt.BorderLayout.SOUTH);
	}

	/**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getJPanel5(), java.awt.BorderLayout.NORTH);
			jPanel1.add(getJPanel15(), java.awt.BorderLayout.CENTER);
		}
		return jPanel1;
	}


	/**
	 * This method initializes jPanel4
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			lblUnitsX = new JLabel();
			if (fframeview.getMapContext()!=null)
				lblUnitsX.setText(MapContext.getDistanceNames()[fframeview.getMapContext().getViewPort().getMapUnits()]);
			jLabel = new JLabel();
			jLabel.setText("x");
			jLabel.setName("jLabel");
			jPanel4 = new JPanel();
			jPanel4.setLayout(new FlowLayout());
			jPanel4.add(jLabel, null);
			jPanel4.add(getTxtIntervalX(), null);
			jPanel4.add(lblUnitsX, null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes txtIntervalX
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtIntervalX() {
		if (txtIntervalX == null) {
			txtIntervalX = new JTextField();
			txtIntervalX.setText(String.valueOf(fframegrid.getIntervalX()));
			txtIntervalX.setPreferredSize(new java.awt.Dimension(60,20));
			txtIntervalX.setName("txtIntervalX");
		}
		return txtIntervalX;
	}

	/**
	 * This method initializes jPanel5
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new FlowLayout());
			jPanel5.add(getJPanel6(), null);
			jPanel5.add(getJPanel8(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jPanel6
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BoxLayout(getJPanel6(), BoxLayout.Y_AXIS));
			jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "Intervalo"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION,
					null, null));
			jPanel6.add(getJPanel4(), null);
			jPanel6.add(getJPanel7(), null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jPanel7
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			lblUnitsY = new JLabel();
			if (fframeview.getMapContext()!=null)
				lblUnitsY.setText(MapContext.getDistanceNames()[fframeview.getMapContext().getViewPort().getMapUnits()]);
			jLabel1 = new JLabel();
			jLabel1.setText("y");
			jPanel7 = new JPanel();
			jPanel7.add(jLabel1, null);
			jPanel7.add(getTxtIntervalY(), null);
			jPanel7.add(lblUnitsY, null);
		}
		return jPanel7;
	}

	/**
	 * This method initializes txtIntervalY
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtIntervalY() {
		if (txtIntervalY == null) {
			txtIntervalY = new JTextField();
			txtIntervalY.setText(String.valueOf(fframegrid.getIntervalY()));
			txtIntervalY.setPreferredSize(new java.awt.Dimension(60,20));
		}
		return txtIntervalY;
	}

	/**
	 * This method initializes jPanel8
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel8() {
		if (jPanel8 == null) {
			jPanel8 = new JPanel();
			jPanel8.setLayout(new BoxLayout(getJPanel8(), BoxLayout.Y_AXIS));
			jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
					PluginServices.getText(this, "Simbologia"),
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION,
					null, null));
			jPanel8.add(getJPanel9(), null);
			jPanel8.add(getBSymbol(), null);
		}
		return jPanel8;
	}

	/**
	 * This method initializes jPanel9
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jPanel9 = new JPanel();
			jPanel9.add(getRbPoints(), null);
			jPanel9.add(getRbLines(), null);
		}
		return jPanel9;
	}

	/**
	 * This method initializes rbPoints
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRbPoints() {
		if (rbPoints == null) {
			rbPoints = new JRadioButton();
			bg.add(rbPoints);
			rbPoints.setSelected(!fframegrid.isLine());
			rbPoints.setText(PluginServices.getText(this,"points"));
		}
		return rbPoints;
	}

	/**
	 * This method initializes rbLines
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRbLines() {
		if (rbLines == null) {
			rbLines = new JRadioButton();
			bg.add(rbLines);
			rbLines.setSelected(fframegrid.isLine());
			rbLines.setText(PluginServices.getText(this,"lines"));
		}
		return rbLines;
	}

	/**
	 * This method initializes bSymbol
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBSymbol() {
		if (bSymbol == null) {
			bSymbol = new JButton();
			bSymbol.setText(PluginServices.getText(this,"symbol"));
			bSymbol.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					PanelEditSymbol pes=new PanelEditSymbol();
					if (getRbLines().isSelected()){
						pes.setSymbol(fframegrid.getSymbolLine());
						pes.setShapeType(Geometry.TYPES.CURVE);
					} else{
						pes.setSymbol(fframegrid.getSymbolPoint());
						pes.setShapeType(Geometry.TYPES.POINT);
					}
					PluginServices.getMDIManager().addWindow(pes);
					symbol=pes.getSymbol();
//					FPanelLegendDefault pld=new FPanelLegendDefault();
//					pld.setFSymbol(fframegrid.getSymbolLine());

				}
			});
		}
		return bSymbol;
	}

	/**
	 * This method initializes jPanel10
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel10() {
		if (jPanel10 == null) {
			jPanel10 = new JPanel();
			jPanel10.setLayout(new BorderLayout());
			jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "Font"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
			jPanel10.add(getJPanel11(), java.awt.BorderLayout.NORTH);
			jPanel10.add(getJPanel12(), java.awt.BorderLayout.CENTER);
		}
		return jPanel10;
	}

	/**
	 * This method initializes jPanel11
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jPanel11 = new JPanel();
			jPanel11.add(getJButton(), null);
			jPanel11.add(getColorChooserPanel(), null);
		}
		return jPanel11;
	}

	/**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText(PluginServices.getText(this, "Font"));
			m_font=fframegrid.getFont();
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Font font = FontChooser.showDialog(PluginServices.getText(
							this, "__seleccion_de_fuente"), m_font);
					if (font != null)
						m_font=font; // fchoser=new
													// FontChooser();
													// //$NON-NLS-1$
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes colorChooserPanel
	 *
	 * @return com.iver.cit.gvsig.gui.panels.ColorChooserPanel
	 */
	private ColorChooserPanel getColorChooserPanel() {
		if (colorChooserPanel == null) {
			colorChooserPanel = new ColorChooserPanel();
			colorChooserPanel.setAlpha(255);
			colorChooserPanel.setColor(fframegrid.getFontColor());
		}
		return colorChooserPanel;
	}

	/**
	 * This method initializes jPanel12
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel12() {
		if (jPanel12 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText(PluginServices.getText(this,"size"));
			jPanel12 = new JPanel();
			jPanel12.add(jLabel2, null);
			jPanel12.add(getTxtSize(), null);
		}
		return jPanel12;
	}

	/**
	 * This method initializes txtSize
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtSize() {
		if (txtSize == null) {
			txtSize = new JTextField();
			txtSize.setText(String.valueOf(fframegrid.getFontSize()));
			txtSize.setPreferredSize(new java.awt.Dimension(40,20));
		}
		return txtSize;
	}

	public IFFrame getFFrame() {
		return newFFrameGrid;
	}
	private AcceptCancelPanel getAcceptCancelPanel() {
		if (accept == null) {
			ActionListener okAction, cancelAction;
			okAction = new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {
					newFFrameGrid = (FFrameGrid) fframegrid
							.cloneFFrame(layout);
					newFFrameGrid.setFFrameDependence(fframeview);
//					newFFrameGrid.setBoundBox();
					newFFrameGrid.setIntervalX(Double.parseDouble(getTxtIntervalX().getText().toString()));
					newFFrameGrid.setIntervalY(Double.parseDouble(getTxtIntervalY().getText().toString()));

					if (getRbLines().isSelected()){
						if (symbol!=null)
						newFFrameGrid.setSymbolLine(symbol);
					}else{
						if (symbol!=null)
						newFFrameGrid.setSymbolPoint(symbol);
					}


					newFFrameGrid.setTextColor(getColorChooserPanel().getColor());
					newFFrameGrid.setSizeFont(Integer.parseInt(getTxtSize().getText()));
					newFFrameGrid.setIsLine(getRbLines().isSelected());
					newFFrameGrid.setFont(m_font);
					newFFrameGrid.setRotation(fframeview.getRotation());
					PluginServices.getMDIManager().closeWindow(
							FFrameGridDialog.this);
				}
			};
			cancelAction = new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newFFrameGrid=null;
					PluginServices.getMDIManager().closeWindow(
							FFrameGridDialog.this);
				}
			};
			accept = new AcceptCancelPanel(okAction, cancelAction);
//			accept.setPreferredSize(new java.awt.Dimension(300, 300));
			// accept.setBounds(new java.awt.Rectangle(243,387,160,28));
			accept.setEnabled(true);
//			accept.setBounds(new java.awt.Rectangle(45, 250, 300, 32));
			accept.setVisible(true);
		}
		return accept;
	}

	public void setRectangle(Rectangle2D r) {
		rect=r;
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG|WindowInfo.RESIZABLE);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"Grid_settings"));

		return m_viewinfo;
	}

	public boolean getIsAcepted() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method initializes jPanel15
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel15() {
		if (jPanel15 == null) {
			jPanel15 = new JPanel();
			jPanel15.add(getJPanel10(), null);
		}
		return jPanel15;
	}
	public void setFFrameView(FFrameView fview){
		fframeview=fview;

	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
