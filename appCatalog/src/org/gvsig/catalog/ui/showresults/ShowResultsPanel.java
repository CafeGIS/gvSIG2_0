
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
package org.gvsig.catalog.ui.showresults;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import org.gvsig.catalog.CatalogClient;
import org.gvsig.catalog.metadataxml.XMLNode;
import org.gvsig.catalog.schemas.Record;
import org.gvsig.catalog.utils.ImageRetriever;
import org.gvsig.i18n.Messages;


/**
 * 
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class ShowResultsPanel extends JPanel {
	public static boolean mustShowThumbnails = true;
	private Record record = null;
	private CatalogClient client = null;
	private URL imageURL = null;
	private JPanel descriptionPanel = null;
	private JEditorPane descriptionArea = null;
	private JPanel linksPanel = null;
	private JPanel jPanel = null;
	private JButton descriptionButton = null;
	private JLabel jLabel = null;
	private JButton mapButton = null;
	private JScrollPane descriptionScroll = null;
	private JPanel imagePanel = null;
	private JLabel jLabel1 = null;
	private JLabel imageLabel = null;
	private JPanel jPanel1 = null;
	private JPanel nextLastPanel = null;
	private JButton lastButton = null;
	private JLabel textLabel = null;
	private JButton nextButton = null;
	private int numRecords; 
	private JButton closeButton = null;

	/**
	 * Constaructor
	 * 
	 * @param server Server URL
	 * 
	 * @param node Answer Node
	 * @param protocol Search protocol
	 * @param numRecords Number of records returned by the query
	 * @param serverURL 
	 * @param translator 
	 */
	public  ShowResultsPanel(CatalogClient client, int numRecords) {        
		super();
		this.client = client;		
		this.numRecords = numRecords;        
		initialize();		
	} 

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {        
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(703, 364);
		this.add(getNextLastPanel(), null);
		this.add(getJPanel1(), null);
		this.add(getDescriptionPanel(), null);
		this.add(getJPanel(), null);
		this.add(getLinksPanel(), null);
		actualizaLabel(1);
	} 

	/**
	 * Actualiza el valor de la cadena de taexto que muestra los resultados
	 * 
	 * 
	 * @param number Registro actual
	 */
	public void actualizaLabel(int number) {        
		textLabel.setText(Messages.getText("results") + ": " + String.valueOf(number) + " " + Messages.getText("of") + " " +
				String.valueOf(this.numRecords));
	} 

	/**
	 * @param descripcionBoton The descripcionBoton to set.
	 */
	public void setDescripcionBoton(JButton descripcionBoton) {        
		this.descriptionButton = descripcionBoton;
	} 

	/**
	 * @param mapButton The mapButton to set.
	 */
	public void setMapButton(JButton mapButton) {        
		this.mapButton = mapButton;
	} 

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getDescriptionPanel() {        
		if (descriptionPanel == null) {
			jLabel1 = new JLabel();
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new BoxLayout(descriptionPanel,
					BoxLayout.X_AXIS));
			descriptionPanel.setPreferredSize(new java.awt.Dimension(600, 300));
			jLabel1.setText("");
			jLabel1.setPreferredSize(new java.awt.Dimension(40, 0));
			descriptionPanel.add(getDescriptionScroll(), null);
			if (mustShowThumbnails) {
				descriptionPanel.add(jLabel1, null);
				descriptionPanel.add(getImagePanel(), null);
			} else {
				descriptionPanel.setPreferredSize(new java.awt.Dimension(400,
						300));
			}
		}
		return descriptionPanel;
	} 

	/**
	 * This method initializes jTextArea
	 * @return javax.swing.JTextArea
	 */
	public JEditorPane getDescriptionArea() {        
		if (descriptionArea == null) {
			descriptionArea = new JEditorPane();
			descriptionArea.setPreferredSize(new java.awt.Dimension(400, 300));
			descriptionArea.setContentType("text/html");			
		}
		return descriptionArea;
	} 

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getLinksPanel() {        
		if (linksPanel == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(flowLayout.RIGHT);
			linksPanel = new JPanel(flowLayout);
			linksPanel.add(getDescriptionButton(), null);
			linksPanel.add(getMapButton(), null);
			linksPanel.add(getCloseButton(), null);
		}
		return linksPanel;
	} 

	/**
	 * This method initializes jPanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {        
		if (jPanel == null) {
			jPanel = new JPanel();
		}
		return jPanel;
	} 

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getDescriptionButton() {        
		if (descriptionButton == null) {
			descriptionButton = new JButton();
			descriptionButton.setText(Messages.getText("description"));
			descriptionButton.setActionCommand("description");
			descriptionButton.setPreferredSize(new Dimension(95, 23));
		}
		return descriptionButton;
	} 

	/**
	 * This method initializes jLabel
	 * @return javax.swing.JLabel
	 */
	private JLabel getJLabel() {        
		if (jLabel == null) {
			jLabel = new JLabel();
		}
		return jLabel;
	} 

	/**
	 * This method initializes mapButton
	 * @return javax.swing.JButton
	 */
	public JButton getMapButton() {        
		if (mapButton == null) {
			mapButton = new JButton();
			mapButton.setText(Messages.getText("addLayer"));
			mapButton.setActionCommand("layer");
			mapButton.setPreferredSize(new Dimension(95, 23));
		}
		return mapButton;
	} 

	/**
	 * This method initializes descriptionScroll
	 * @return javax.swing.JScrollPane
	 */
	public JScrollPane getDescriptionScroll() {        
		if (descriptionScroll == null) {
			descriptionScroll = new JScrollPane();
			descriptionScroll.setViewportView(getDescriptionArea());
			descriptionScroll.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			descriptionScroll.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			descriptionScroll.setPreferredSize(new java.awt.Dimension(400, 250));
		}
		return descriptionScroll;
	} 

	/**
	 * @return Returns the record.
	 */
	public Record getRecord() {        
		return record;
	} 

	/**
	 * @param record The Record to set.
	 */
	public void setRecord(Record record) {        
		this.record = record;
	} 

	/**
	 * @param node 
	 * @param protocol 
	 */
	public void loadTextNewRecord(Record record) {        
		setRecord(record);
		descriptionArea.setText(this.getHtml());
		descriptionArea.setCaretPosition(0);
		if (getRecord() != null){
			if (getRecord().getImage() == null){
				imageLabel.setIcon(getImgIcon(null));	
				//DOWNLOAD the image
				new ImageThread();			
			}else{
				imageLabel.setIcon(getImgIcon(getRecord().getImage()));
			}
		}
		this.repaint();
	} 

	/**
	 * @return the HTML code to show
	 */
	private String getHtml() {        
		StringBuffer html = new StringBuffer();
		html.append("<html><body>");
		if(getRecord() == null){
			html.append("Error");
			html.append("</body></html>");
			return html.toString();
		}
		if ((getRecord().getNode() != null) && (getRecord().getNode().getName() != null)){
			if (getRecord().getNode().getName().equals(XMLNode.ISNOTXML)){
				html.append(getRecord().getNode().getText());
				html.append("</body></html>");
				return html.toString();
			}
		}
		html.append("<font COLOR=\"#0000FF\">");
		if (this.getRecord().getTitle() != null) {
			html.append(this.getRecord().getTitle());
		}
		html.append("<font COLOR=\"#000000\">");
		if ((this.getRecord().getAbstract_() != null) &&
				(!(this.getRecord().getAbstract_().equals("")))) {
			html.append("<br>");
			html.append("<br>");
			html.append("<b>" + Messages.getText("abstract") + ": </b>");
			html.append(this.getRecord().getAbstract_());
		}
		if ((this.getRecord().getPurpose() != null) &&
				(!(this.getRecord().getPurpose().equals("")))) {
			html.append("<br>");
			html.append("<br>");
			html.append("<b>" + Messages.getText("purpose") +": </b>");
			html.append(this.getRecord().getPurpose());
		}
		if (this.getRecord().getKeyWords() != null) {
			if (this.getRecord().getKeyWords()[0] != null) {
				html.append("<br>");
				html.append("<br>");
				html.append("<b>" + Messages.getText("keyWords") + ":</b>");
				html.append(this.getRecord().getKeyWords()[0]);
			}
		}
		if (this.getRecord().getKeyWords() != null) {
			for (int i = 1; i < this.getRecord().getKeyWords().length; i++)
				if (this.getRecord().getKeyWords()[i] != null) {
					html.append(", " + this.getRecord().getKeyWords()[i]);
				}
		}
		html.append("</body></html>");
		return html.toString();
	} 

	/**
	 * This method initializes imagePanel
	 * @return javax.swing.JPanel
	 */
	private JPanel getImagePanel() {        
		if (imagePanel == null) {
			imagePanel = new JPanel();
			imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			imagePanel.setMaximumSize(new java.awt.Dimension(207, 210));
			JPanel borderPanel = new JPanel();
			borderPanel.setLayout(new BorderLayout());
			borderPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createBevelBorder(BevelBorder.LOWERED),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			imagePanel.add(borderPanel, BorderLayout.CENTER);
			imageLabel = new JLabel(getImgIcon(null));
			borderPanel.add(imageLabel, BorderLayout.CENTER);
		}
		return imagePanel;
	} 

	/**
	 * @return 
	 * @param pic 
	 */
	public ImageIcon getImgIcon(BufferedImage pic) {        
		BufferedImage img = new BufferedImage(180, 180,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		if (pic != null) {
			double fw =         1.0;
			double fh =         1.0;
			if (pic.getWidth() > pic.getHeight()){
				fw = 180D / pic.getWidth();
				fh = fw; 
			}else{
				fh = 180D / pic.getHeight();
				fw = fh;
			}

			AffineTransform mat = AffineTransform.getScaleInstance(fw, fh);
			g.drawImage(pic, mat, null);
		} else {

			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setFont(new Font("Lucida Bright", Font.ITALIC, 30));
			g.rotate(-Math.PI / 4, 100D, 100D);
			String text = "NO PICTURE"; //DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
			TextLayout tl = new TextLayout(text, g.getFont(),
					g.getFontRenderContext());
			Rectangle2D bounds = tl.getBounds();
			double x = (( 180D - bounds.getWidth()) / 2) - bounds.getX();
			double y = (( 180D - bounds.getHeight()) / 2) - bounds.getY();
			Shape outline = tl.getOutline(AffineTransform.getTranslateInstance(x +
					2, y + 1));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			g.setPaint(Color.WHITE);
			g.draw(outline);
			g.setPaint(new GradientPaint(0, 0, new Color(192, 192, 255), 30,
					20, new Color(255, 255, 64), true));
			tl.draw(g, (float) x, (float) y);
		}
		g.dispose();
		return new ImageIcon(img);
	} 

	/**
	 * @return Returns the imageURL.
	 */
	public URL getImageURL() {        
		return imageURL;
	} 

	/**
	 * @param imageURL The imageURL to set.
	 */
	public void setImageURL(URL imageURL) {        
		this.imageURL = imageURL;
	} 

	/**
	 * This method initializes jPanel1
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {        
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
		}
		return jPanel1;
	} 

	/**
	 * This method initializes jPanel2
	 * @return javax.swing.JPanel
	 */
	private JPanel getNextLastPanel() {        
		if (nextLastPanel == null) {
			textLabel = new JLabel();
			nextLastPanel = new JPanel();
			textLabel.setText("JLabel");
			nextLastPanel.add(getLastButton(), null);
			nextLastPanel.add(textLabel, null);
			nextLastPanel.add(getNextButton(), null);
		}
		return nextLastPanel;
	} 

	/**
	 * This method initializes jButton
	 * 
	 * 
	 * @return javax.swing.JButton
	 */
	public JButton getLastButton() {        
		if (lastButton == null) {
			lastButton = new JButton();
			lastButton.setText(Messages.getText("last"));
			lastButton.setActionCommand("last");
			lastButton.setEnabled(false);
			lastButton.setPreferredSize(new Dimension(95, 23));
		}
		return lastButton;
	} 

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getNextButton() {        
		if (nextButton == null) {
			nextButton = new JButton();
			nextButton.setText(Messages.getText("next"));
			nextButton.setActionCommand("next");
			nextButton.setPreferredSize(new Dimension(95, 23));
			if (this.numRecords < 2) {
				nextButton.setEnabled(false);
			}
		}
		return nextButton;
	} 

	/**
	 * This method initializes jButton
	 * @return javax.swing.JButton
	 */
	public JButton getCloseButton() {        
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText(Messages.getText("close"));
			closeButton.setActionCommand("close");
			closeButton.setPreferredSize(new Dimension(80, 23));
		}
		return closeButton;
	} 

	/**
	 * This thread is used to download the image
	 * @author jorpiell
	 *
	 */
	private class ImageThread implements Runnable {
		volatile Thread myThread = null;

		public  ImageThread() {        
			myThread = new Thread(this);
			myThread.start();
		} 

		public void stop() {        
			myThread.stop();
		} 

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {        
			BufferedImage img = ImageRetriever.getImageUrl(getRecord());
			imageLabel.setIcon(getImgIcon(img));
		} 
	}

}
//@jve:decl-index=0:visual-constraint="107,10"
