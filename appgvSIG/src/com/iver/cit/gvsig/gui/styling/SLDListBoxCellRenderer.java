/*
 * Created on 26-abr-2005
 *
 * gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
package com.iver.cit.gvsig.gui.styling;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.StaticRenderer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

/**
*
* @author jaume dominguez faus - jaume.dominguez@iver.es
*
*/
public class SLDListBoxCellRenderer extends JPanel implements ListCellRenderer {

    private String str;
    private GVTBuilder gvtBuilder = new GVTBuilder();
    private StaticRenderer renderer = new StaticRenderer();
    private Element elt;
    private GraphicsNode gvtRoot;

    DocumentLoader loader;
    private UserAgentAdapter userAgent;
    BridgeContext  ctx;
    BufferedImage image;
    protected static RenderingHints defaultRenderingHints;
    static {
        defaultRenderingHints = new RenderingHints(null);
        defaultRenderingHints.put(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

        defaultRenderingHints.put(RenderingHints.KEY_INTERPOLATION,
                                  RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }
    /**
     * Constructor method
     *
     */

    public SLDListBoxCellRenderer() {
        setOpaque(true);
        setLayout(new BorderLayout(3,3));

        userAgent = new UserAgentAdapter();
        loader    = new DocumentLoader(userAgent);
        ctx       = new BridgeContext(userAgent, loader);

        renderer.setDoubleBuffered(true);
        /* canvas = new JSVGCanvas();
        // canvas.setMySize(getWidth()-3, getHeight()-3);
        canvas.setMySize(new Dimension(getWidth()-3, getHeight()-3));
        // add(canvas,BorderLayout.CENTER);
        canvas.setVisible(true); */

        /* setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER); */
    }
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
//      Get the selected index. (The index param isn't
//      always valid, so just use the value.)
      String selectedSymbol = ((String)value).toString();
      str = selectedSymbol;

      if (isSelected) {
          setBackground(list.getSelectionBackground());
          setForeground(list.getSelectionForeground());
      } else {
          setBackground(list.getBackground());
          setForeground(list.getForeground());
      }

      /* try {
          canvas.setURI( new File("D:/java/eclipse30/eclipse/workspace/FMap 03/docs/fill1.svg").toURL().toString() );
      } catch (MalformedURLException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
      } */
      gvtRoot = null;
      try
      {
          File f = new File(str);
          Document svgDoc = loader.loadDocument(f.toURI().toString());
          gvtRoot = gvtBuilder.build(ctx, svgDoc);
          renderer.setTree(gvtRoot);
          elt = ((SVGDocument)svgDoc).getRootElement();

      } catch (Exception ex) {
          ex.printStackTrace();
      }


//      Set the icon and text.  If icon was null, say so.
      // ImageIcon icon = images[selectedIndex];
      String pet = selectedSymbol; // petStrings[selectedIndex];
      /* setIcon(icon);
      if (icon != null) { */
          // setText(pet);
          setFont(list.getFont());
          repaint();
      /* } else {
          // setUhOhText(pet + " (no image available)", list.getFont());
      } */

      return this;
    }
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /* renderer.setTransform
        (ViewBox.getViewTransform
         (null, elt, getWidth()-1, getHeight()-1));

        renderer.updateOffScreen(getWidth()-1,
                                 getHeight()-1);

        Rectangle r = new Rectangle(0, 0,
                                    getWidth()-1,
                                    getHeight()-1);
        renderer.repaint(r);

        image = renderer.getOffScreen();

        g.drawImage(image,0,0, null); */
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints renderingHints = defaultRenderingHints;
        g2.setRenderingHints(renderingHints);
        try {
        	gvtRoot.setTransform((ViewBox.getViewTransform(null, elt, getWidth()-1, getHeight()-1, ctx)));
        	gvtRoot.paint(g2);
        } catch (Exception e) {
        	Rectangle bounds = getBounds();
        	JLabel errLbl = new JLabel("<html><b>"+"Cannot render north"+/*PluginServices.getText(this, "cannot_render_north")+*/"</b></html>");
        	errLbl.setPreferredSize(bounds.getSize());
        	errLbl.setSize(bounds.getSize());
        	errLbl.setBounds(bounds);
        	errLbl.paint(g);
        }
        // g2.setTransform(oldTx);

    }

}
