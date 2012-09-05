package com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs;

import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.layout.FLayoutUtilities;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.JPRotation;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;


/**
 * Diálogo de un grupo de fframe del Layout.
 *
 * @author Vicente Caballero Navarro
 */
public class FFrameGroupDialog extends JPanel implements IFFrameDialog {
	private javax.swing.JPanel jContentPane = null;
	private javax.swing.JButton bAceptar = null;
	private javax.swing.JButton bCancelar = null;
	private Rectangle2D rect = new Rectangle2D.Double();
	private FFrameGroup fframegroup = null;
	private Layout m_layout = null;
	private boolean isAcepted = false;
	private JPRotation pRotation = null;
	private FFrameGroup newFFrameGroup;


	/**
	 * This is the default constructor
	 *
	 * @param layout Referencia al Layout.
	 * @param fframe Referencia al fframe de imagen.
	 */
	public FFrameGroupDialog(Layout layout, FFrameGroup fframe) {
		super();
		fframegroup = fframe;
		m_layout = layout;
		initialize();
	}

	/**
	 * Inserta el rectángulo que ocupará el fframe de imagen.
	 *
	 * @param r Rectángulo.
	 */
	public void setRectangle(Rectangle2D r) {
		rect.setRect(r);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(null);
		this.add(getJContentPane(), null);
		this.setSize(196, 191);
		getPRotation().setRotation(fframegroup.getRotation());
	}

	/**
	 * This method initializes jContentPane
	 *
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBAceptar(), null);
			jContentPane.add(getBCancelar(), null);
			jContentPane.setSize(192, 186);
			jContentPane.setLocation(0, 0);
			jContentPane.add(getPRotation(), null);
		}

		return jContentPane;
	}

	/**
	 * This method initializes bAceptar
	 *
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAceptar() {
		if (bAceptar == null) {
			bAceptar = new javax.swing.JButton();
			bAceptar.setSize(85, 26);
			bAceptar.setText(PluginServices.getText(this, "Aceptar"));
			bAceptar.setLocation(7, 147);
			bAceptar.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent e) {
					  	newFFrameGroup=(FFrameGroup)fframegroup.cloneFFrame(m_layout);
					  	newFFrameGroup.setBoundBox(FLayoutUtilities.toSheetRect(
								rect, m_layout.getLayoutControl().getAT()));
					  	newFFrameGroup.setRotation(getPRotation().getRotation());
						PluginServices.getMDIManager().closeWindow(FFrameGroupDialog.this);
						//m_layout.refresh();
						isAcepted = true;
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
			bCancelar.setSize(85, 26);
			bCancelar.setText(PluginServices.getText(this, "Cancelar"));
			bCancelar.setLocation(99, 147);
			bCancelar.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						PluginServices.getMDIManager().closeWindow(FFrameGroupDialog.this);
					}
				});
		}

		return bCancelar;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo(WindowInfo.MODALDIALOG);
		m_viewinfo.setTitle(PluginServices.getText(this,
				"propiedades_marco_imagenes"));

		return m_viewinfo;
	}

	/**
	 * @see com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog#getIsAcepted()
	 */
	public boolean getIsAcepted() {
		return isAcepted;
	}

	/**
	 * @see com.iver.mdiApp.ui.MDIManager.IWindow#windowActivated()
	 */
	public void viewActivated() {
	}

	/**
	 * This method initializes pRotation
	 *
	 * @return javax.swing.JPanel
	 */
	private JPRotation getPRotation() {
		if (pRotation == null) {
			pRotation = new JPRotation();
			pRotation.setBounds(30, 10, 120, 120);
		}
		return pRotation;
	}

	public IFFrame getFFrame() {
		return newFFrameGroup;
	}

	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

