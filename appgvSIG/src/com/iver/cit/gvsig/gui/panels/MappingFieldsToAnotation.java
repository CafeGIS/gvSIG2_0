package com.iver.cit.gvsig.gui.panels;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.gvsig.fmap.mapcontext.layers.MappingAnnotation;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

//TODO comentado para que compile
public class MappingFieldsToAnotation extends JPanel implements IWindow{

	private JComboBox cmbText = null;
	private JComboBox cmbAngle = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JComboBox cmbColor = null;
	private JLabel jLabel3 = null;
	private JComboBox cmbHeight = null;
	private JLabel jLabel4 = null;
	private JComboBox cmbFont = null;
	private JButton bOk = null;
	private JButton bCancel = null;
//	private SelectableDataSource sds=null;
//	private FLyrAnnotation la;
	private int[] mapText;
	private int[] mapAngle;
	private int[] mapColor;
	private int[] mapHeight;
	private int[] mapFont;
	private JPanel jPanel = null;
	private JLabel jLabel5 = null;
	private JTextField txtName = null;
	//private JCheckBox chbPixels = null;
	private JRadioButton rbMapUnits = null;
	private JRadioButton rbPixels = null;
	private boolean isOk=false;
	/**
	 * This is the default constructor
	 */
//	public MappingFieldsToAnotation(FLyrAnnotation la) {
//		super();
//		this.la=la;
//		try {
//			this.sds=la.getRecordset();
//			initialize();
//		} catch (ReadDriverException e) {
//			e.printStackTrace();
//		}
//
//	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		jLabel4 = new JLabel();
		jLabel4.setBounds(new java.awt.Rectangle(15,259,166,20));
		jLabel4.setText("fuente");
		jLabel3 = new JLabel();
		jLabel3.setBounds(new java.awt.Rectangle(15,197,166,20));
		jLabel3.setText("alto");
		jLabel2 = new JLabel();
		jLabel2.setBounds(new java.awt.Rectangle(15,135,166,20));
		jLabel2.setText("color");
		jLabel1 = new JLabel();
		jLabel1.setBounds(new java.awt.Rectangle(15,73,166,20));
		jLabel1.setText("angulo");
		jLabel = new JLabel();
		jLabel.setBounds(new java.awt.Rectangle(15,11,166,20));
		jLabel.setText("texto");
		this.setLayout(null);
		this.setSize(544, 368);
		this.add(getCmbText(), null);
		this.add(getCmbAngle(), null);
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(jLabel2, null);
		this.add(getCmbColor(), null);
		this.add(jLabel3, null);
		this.add(getCmbHeight(), null);
		this.add(jLabel4, null);
		this.add(getCmbFont(), null);
		this.add(getBOk(), null);
		this.add(getBCancel(), null);
		this.add(getJPanel(), null);
	}

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"indexar_campos"));

		return m_viewinfo;
	}

	/**
	 * This method initializes cmbOrigin
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbText() {
		if (cmbText == null) {
			cmbText = new JComboBox();
//			try {
//				mapText=new int[sds.getFieldCount()];
//				int num=-1;
//				for (int i = 0; i < sds.getFieldCount(); i++) {
//					cmbText.addItem(sds.getFieldName(i));
//					num++;
//					mapText[i]=num;
//				}
//				//cmbText.addItem("- Defaulf -");
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
			cmbText.setBounds(new java.awt.Rectangle(15,42,166,20));

		}
		return cmbText;
	}

	/**
	 * This method initializes cmbEnd
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbAngle() {
		if (cmbAngle == null) {
			cmbAngle = new JComboBox();
//			try {
//				mapAngle=new int[sds.getFieldCount()];
//				int num=-1;
//			for (int i = 0; i < sds.getFieldCount(); i++) {
//				if (sds.getFieldType(i) == Types.DOUBLE){
//					cmbAngle.addItem(sds.getFieldName(i));
//					num++;
//					mapAngle[i]=num;
//				}
//			}
//			cmbAngle.addItem("- Default -");
//			cmbAngle.setSelectedItem("- Default -");
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
			cmbAngle.setBounds(new java.awt.Rectangle(15,104,166,20));
		}
		return cmbAngle;
	}

	/**
	 * This method initializes cmbColor
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbColor() {
		if (cmbColor == null) {
			cmbColor = new JComboBox();
			cmbColor.setBounds(new java.awt.Rectangle(15,166,166,20));
//			try {
//				mapColor=new int[sds.getFieldCount()];
//				int num=-1;
//				for (int i = 0; i < sds.getFieldCount(); i++) {
//					if (sds.getFieldType(i) == Types.DOUBLE){
//						cmbColor.addItem(sds.getFieldName(i));
//						num++;
//						mapColor[i]=num;
//					}
//				}
//				cmbColor.addItem("- Default -");
//				cmbColor.setSelectedItem("- Default -");
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
		}
		return cmbColor;
	}

	/**
	 * This method initializes cmbHeight
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbHeight() {
		if (cmbHeight == null) {
			cmbHeight = new JComboBox();
//			try {
//				mapHeight=new int[sds.getFieldCount()];
//				int num=-1;
//				for (int i = 0; i < sds.getFieldCount(); i++) {
//					if (sds.getFieldType(i) == Types.DOUBLE){
//						cmbHeight.addItem(sds.getFieldName(i));
//						num++;
//						mapHeight[i]=num;
//					}
//				}
//				cmbHeight.addItem("- Default -");
//				cmbHeight.setSelectedItem("- Default -");
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
			cmbHeight.setBounds(new java.awt.Rectangle(15,228,166,20));
		}
		return cmbHeight;
	}

	/**
	 * This method initializes cmbFont
	 *
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCmbFont() {
		if (cmbFont == null) {
			cmbFont = new JComboBox();
//			try {
//				mapFont=new int[sds.getFieldCount()];
//				int num=-1;
//				for (int i = 0; i < sds.getFieldCount(); i++) {
//					if (sds.getFieldType(i) == Types.VARCHAR){
//						cmbFont.addItem(sds.getFieldName(i));
//						num++;
//						mapFont[i]=num;
//					}
//				}
//				cmbFont.addItem("- Default -");
//				cmbFont.setSelectedItem("- Default -");
//			} catch (ReadDriverException e) {
//				e.printStackTrace();
//			}
			cmbFont.setBounds(new java.awt.Rectangle(15,290,166,20));
		}
		return cmbFont;
	}

	/**
	 * This method initializes bOk
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBOk() {
		if (bOk == null) {
			bOk = new JButton();
			bOk.setBounds(new java.awt.Rectangle(119,326,92,24));
			bOk.setText("Aceptar");
			bOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MappingAnnotation mapping=new MappingAnnotation();
					if (getCmbText().getSelectedItem().equals("")) {
						mapping.setColumnText(-1);
					} else {
						mapping.setColumnText(mapText[getCmbText().getSelectedIndex()]);
					}
					if (!getCmbAngle().getSelectedItem().equals("- Default -")) {
						mapping.setColumnRotate(mapAngle[getCmbAngle().getSelectedIndex()]);
					}
					if (!getCmbColor().getSelectedItem().equals("- Default -")) {
						mapping.setColumnColor(mapColor[getCmbColor().getSelectedIndex()]);
					}
					if (!getCmbHeight().getSelectedItem().equals("- Default -")) {
						mapping.setColumnHeight(mapHeight[getCmbHeight().getSelectedIndex()]);
					}
					if (!getCmbFont().getSelectedItem().equals("- Default -")) {
						mapping.setColumnTypeFont(mapFont[getCmbFont().getSelectedIndex()]);
					}
					//Falta el estilo, si es necesario.
//					la.setInPixels(rbPixels.isSelected());
//					la.setMapping(mapping);
//					la.setName(getTxtName().getText());
					isOk=true;
					PluginServices.getMDIManager().closeWindow(MappingFieldsToAnotation.this);
				}
			});
		}
		return bOk;
	}

	/**
	 * This method initializes bCancel
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getBCancel() {
		if (bCancel == null) {
			bCancel = new JButton();
			bCancel.setBounds(new java.awt.Rectangle(330,325,93,25));
			bCancel.setText("cancelar");
			bCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					isOk=false;
					PluginServices.getMDIManager().closeWindow(MappingFieldsToAnotation.this);
				}
			});
		}
		return bCancel;
	}

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel5 = new JLabel();
			jLabel5.setBounds(new java.awt.Rectangle(15,10,228,22));
			jLabel5.setText("nombre");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new java.awt.Rectangle(234,14,251,141));
			jPanel.add(jLabel5, null);
			jPanel.add(getTxtName(), null);
			//jPanel.add(getChbPixels(), null);
			jPanel.add(getRbMapUnits(), null);
			jPanel.add(getRbPixels(), null);
			ButtonGroup bg=new ButtonGroup();
			bg.add(getRbMapUnits());
			bg.add(getRbPixels());
		}
		return jPanel;
	}

	/**
	 * This method initializes txtName
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtName() {
		if (txtName == null) {
			txtName = new JTextField();
			//txtName.setText(la.get.getName());
			txtName.setBounds(new java.awt.Rectangle(15,42,227,22));
		}
		return txtName;
	}

	/**
	 * This method initializes chbPixels
	 *
	 * @return javax.swing.JCheckBox
	 */
	/*private JCheckBox getChbPixels() {
		if (chbPixels == null) {
			chbPixels = new JCheckBox();
			chbPixels.setBounds(new java.awt.Rectangle(15,138,226,25));
			chbPixels.setText("pixels");
		}
		return chbPixels;
	}
*/
	/**
	 * This method initializes rbMapUnits
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRbMapUnits() {
		if (rbMapUnits == null) {
			rbMapUnits = new JRadioButton();
			rbMapUnits.setBounds(new java.awt.Rectangle(15,74,167,22));
			rbMapUnits.setText("unidades_mapa");
		}
		return rbMapUnits;
	}

	/**
	 * This method initializes rbPixels
	 *
	 * @return javax.swing.JRadioButton
	 */
	private JRadioButton getRbPixels() {
		if (rbPixels == null) {
			rbPixels = new JRadioButton();
			rbPixels.setBounds(new java.awt.Rectangle(15,106,167,22));
			rbPixels.setText("pixels");
			rbPixels.setSelected(true);
		}
		return rbPixels;
	}

	public boolean isOk() {
		return isOk;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
}  //  @jve:decl-index=0:visual-constraint="-150,10"
