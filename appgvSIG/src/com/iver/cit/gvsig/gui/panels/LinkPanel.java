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
package com.iver.cit.gvsig.gui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.StaticRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.gui.PDFViewerWindow;
import com.iver.cit.gvsig.project.documents.view.toolListeners.LinkListener;
import com.iver.utiles.BrowserControl;
import com.sun.jimi.core.Jimi;


/**
 * This class extends JPanel. This class implements a Panel to show the content of the URI
 * that the constructor of the class receives. The URI is interpreted according to the type
 * of the HyperLink. The allowed types are Text, WWW, image (jpg,gif,tiff,bmp,ico), PDF
 * documents and SVG images. Implements listener to resize the component that contains the
 * text when resize the window.
 *
 * @author Vicente Caballero Navarro
 *
 */
public class LinkPanel extends JPanel implements IWindow, ComponentListener {
    private static final Logger logger = LoggerFactory
            .getLogger(LinkPanel.class);
    
	private JPanel jPane = null;
	private JScrollPane jScrollPane = null;
//	private JEditorPane jEditorPane1 = null;
	private JPanel jPanel = null; //  @jve:decl-index=0:visual-constraint="220,10"
	private URI uri=null;
	private int type=LinkListener.TYPELINKIMAGE;
	private JPanel jPanel1Izq = null;
	private JPanel jPanel1Der = null;
	private JLabel jLabelUri = null;
	private JButton jButtonCerrar = null;
	private JScrollPane scroll=null;
	private int alto;
	private int ancho;
	private WindowInfo m_ViewInfo;
	private JTextPane textPane;
	private Element elt;
    private GVTBuilder gvtBuilder = new GVTBuilder();
    private GraphicsNode gvtRoot = null;
    private BridgeContext ctx = null;
    private StaticRenderer renderer = new StaticRenderer();
    protected static RenderingHints defaultRenderingHints;
    static {
        defaultRenderingHints = new RenderingHints(null);
        defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }
	/**
	 * This is the default constructor. Needs the URI with the information of the HyperLink
	 * and the type of the HyperLink
	 */
	public LinkPanel(URI Uri, int t){
		super();
		type=t;
		uri=Uri;
		addComponentListener(this);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(600, 450);
		alto=this.getHeight()-100;
		ancho=this.getWidth()-100;
		this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		this.add(getJPanel(), java.awt.BorderLayout.SOUTH);
	}


	/**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new java.awt.Dimension(300, 400));
			jScrollPane.setViewportView(getJPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jEditorPane1. Check the type of the HyperLink and analize the
	 * URI. According to the type, adds to the panel the necessary components to show the
	 * content of the URI. If the type is "0" (text/WWW) adds a ScrollPane with the text.
	 * If the type "1" (smages) adds a ImageIcon with a label. If the type is "2" uses the
	 * library "JPedal" to open PDF documents and if the type is "3" implements some methods
	 * to open SVG images
	 *
	 * @return javax.swing.JEditorPane
	 */
	private JPanel getJPane() {

		if (jPane == null) {
			jPane=new JPanel();
			System.out.println(type);

			//Enlazar a documento de texto o WWW
			if (type==1){
				textPane = new JTextPane();

				scroll = new JScrollPane(textPane);
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		        scroll.setPreferredSize(new Dimension(ancho-100,alto-100));
		        scroll.setMinimumSize(new Dimension(ancho-100,alto-100));
		        textPane.setEditable(false);

				if(uri==null) {
					System.out.println("NULO");
				}
				if (uri != null) {
					URL url=null;
					try {
						url=uri.normalize().toURL();
					} catch (MalformedURLException e1) {
						NotificationManager.addError(e1);
					}
					try {

						textPane.setPage(url);
						textPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
							public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
								if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
									System.out.println("hyperlinkUpdate()");
									BrowserControl.displayURL(e.getURL().toString());

									}
								}
							});
					} catch (IOException e) {
						System.err.println("Attempted to read a bad URL: " +
						uri);
					}
					jPane.add(scroll);
				} else {
					System.err.println("Couldn't find file.");
				}
			}
			//Enlazar a archivo de Imágen
			else if (type==0){

				ImageIcon image = null;
				String iString=uri.toString();
				iString=iString.toLowerCase();
				if (uri == null){
					System.out.println("Uri creada incorrectamente");
					return null;}

				if (iString.endsWith("jpg") || iString.endsWith("jpeg") ||
		                iString.endsWith("gif")) {
		            try {
						image = new ImageIcon(Jimi.getImage(uri.toURL()));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
		        } else
		        	if (iString.endsWith("png") || iString.endsWith("tiff") ||
		                iString.endsWith("ico") || iString.endsWith("xpm")) {
		            try {
						image = new ImageIcon(Jimi.getImage(uri.toURL()));
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

		        }else {
		        	try {
						image=new ImageIcon(uri.toURL());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
		        }
				if (image==null) {
					return null;
				}

				JLabel label = new JLabel(image);
				jPane.add(label);

				jPane.setVisible(true);
			}

			//Tipo 2: Enlazar a documentos PDF
			else if (type==2){

				String aux=null;
				try {
					aux = uri.normalize().toURL().toString();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				aux=aux.replaceFirst("file:/","");
				System.out.println(aux);
				PDFViewerWindow pdf =new PDFViewerWindow(aux);
				jPane.add(pdf);
				jPane.setVisible(true);
			}

			//Tipo 3: Enlazar a imágenes SDV
			else if (type==3){
				String iString=uri.toString();
				iString=iString.toLowerCase();
				if (uri == null){
					System.out.println("Uri creada incorrectamente");
					return null;}
				File file=new File(uri);
				iString=uri.toString().replaceFirst("file:/","");
				if (iString.endsWith("svg")){
					paintSVGToPanel(file);
				}
			}
		}
		return jPane;
	 }

	/**
	 * Allows paint SVG images in the panel.
	 *
	 * @param file, this file has been extracted from the URI
	 */
	private void paintSVGToPanel(File file) {
		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = image.createGraphics();
		Rectangle2D rect = new Rectangle2D.Double();
		rect.setFrame(0,0,this.getWidth(),this.getHeight());
		obtainStaticRenderer(file);
		drawSVG(g,rect, null);
		jPane.setVisible(true);
		ImageIcon icon=new ImageIcon(image);
		JLabel label = new JLabel(icon);
		jPane.add(label);
	}

	/**
	 * Render the image to add to the panel.
	 * @param file, this file has been extracted from the URI
	 */
	private void obtainStaticRenderer(File file) {
        try {
            UserAgentAdapter userAgent = new UserAgentAdapter();
            DocumentLoader loader = new DocumentLoader(userAgent);
            ctx = new BridgeContext(userAgent, loader);
            Document svgDoc = loader.loadDocument(file.toURI().toString());
            gvtRoot = gvtBuilder.build(ctx, svgDoc);
            renderer.setTree(gvtRoot);
            elt = ((SVGDocument) svgDoc).getRootElement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	 /**
	 * Draw SVG in the Graphics that receives like parameter.
     *
     * @param g Graphics
     * @param rect Rectangle that fills the Graphic.
     * @param rv Rectangle. This forms the visible part in the Layout
     */
    private void drawSVG(Graphics2D g, Rectangle2D rect, Rectangle2D rv) {
        if ((rv == null) || rv.contains(rect)) {
            AffineTransform ataux = new AffineTransform();

            ataux.translate(rect.getX(), rect.getY());
            try {
                ataux.concatenate(ViewBox.getViewTransform(null, elt,
                        (float) rect.getWidth(), (float) rect.getHeight(), ctx));
                gvtRoot.setTransform(ataux);

            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            AffineTransform ataux = new AffineTransform();

            ataux.translate(rect.getX(), rect.getY());
            ataux.concatenate(ViewBox.getViewTransform(null, elt,
                    (float) rect.getWidth(), (float) rect.getHeight(), ctx));

            gvtRoot.setTransform(ataux);
        }

        RenderingHints renderingHints = defaultRenderingHints;
        g.setRenderingHints(renderingHints);

        if (gvtRoot != null) {
            gvtRoot.paint(g);
        }
    }

	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			//JLabel name = new JLabel();
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(1);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
			jPanel.setPreferredSize(new java.awt.Dimension(10, 50));
			jPanel.setSize(411, 50);
			jPanel.add(getJPanel1Izq(), null);
			jPanel.add(getJPanel1Der(), null);
			String auxext="";
		}

		return jPanel;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#getViewInfo()
	 */
	public WindowInfo getWindowInfo() {
		m_ViewInfo = new WindowInfo(WindowInfo.RESIZABLE | WindowInfo.MAXIMIZABLE | WindowInfo.ICONIFIABLE);
		m_ViewInfo.setTitle(PluginServices.getText(this,"Hiperenlace"));

		return m_ViewInfo;
	}

	/* (non-Javadoc)
	 * @see com.iver.mdiApp.ui.MDIManager.View#viewActivated()
	 */
	public void viewActivated() {
		// TODO Auto-generated method stub
	}

	/**
	 * This method initializes jPanel1Izq
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1Izq() {
		if (jPanel1Izq == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			jLabelUri = new JLabel();
			jLabelUri.setText(uri.toString());
			jLabelUri.setPreferredSize(new java.awt.Dimension(172,17));
			jPanel1Izq = new JPanel();
			jPanel1Izq.setLayout(new GridBagLayout());
			jPanel1Izq.setPreferredSize(new java.awt.Dimension(10,10));
			jPanel1Izq.setAlignmentX(0.5F);
			jPanel1Izq.add(jLabelUri, gridBagConstraints);
		}
		return jPanel1Izq;
	}

	/**
	 * This method initializes jPanel1Der
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1Der() {
		if (jPanel1Der == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jPanel1Der = new JPanel();
			jPanel1Der.setLayout(new GridBagLayout());
			jPanel1Der.add(getJButtonCerrar(), gridBagConstraints1);
		}
		return jPanel1Der;
	}

	/**
	 * Initializes jButtonCerrar, and implements the Listener to close the window
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonCerrar() {
		if (jButtonCerrar == null) {
			jButtonCerrar = new JButton();
			jButtonCerrar.setPreferredSize(new java.awt.Dimension(94,25));
			jButtonCerrar.setText(PluginServices.getText(this,"Cerrar"));
			jButtonCerrar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()

					if (PluginServices.getMainFrame() != null) {
						PluginServices.getMDIManager().closeWindow(LinkPanel.this);
					} else {
						((JDialog) (getParent().getParent().getParent()
										.getParent())).dispose();
					}
				}
			});

		}
		return jButtonCerrar;
	}
	/**
	 * Resizes the scroll that contains the text when the window changes its own size
	 */
	private void redimensionarScroll(){
		Dimension currentSize = getSize();
		scroll.setPreferredSize(new Dimension(currentSize.width-100,currentSize.height-100));
        scroll.setMinimumSize(new Dimension(currentSize.width-100,currentSize.height-100));
		scroll.updateUI();
		System.out.println(scroll.getSize().toString());
	}

	/**
	 * Invoked when the frame´s size changes
	 */
	public void componentResized(ComponentEvent e) {
		if(type==1) {
			redimensionarScroll();
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
	 */
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 *  (non-Javadoc)
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
	 */
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub

	}

	public Object getWindowProfile() {
		return WindowInfo.TOOL_PROFILE;
	}
}
