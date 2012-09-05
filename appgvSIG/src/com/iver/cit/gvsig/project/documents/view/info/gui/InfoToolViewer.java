package com.iver.cit.gvsig.project.documents.view.info.gui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.gvsig.fmap.mapcontext.layers.FLayer;
import org.gvsig.fmap.mapcontext.layers.FLyrDefault;
import org.gvsig.fmap.mapcontext.layers.operations.InfoByPoint;
import org.gvsig.fmap.mapcontext.layers.operations.XMLItem;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.iver.cit.gvsig.gui.wizards.FormatListModel;
import com.iver.utiles.xmlViewer.XMLContent;
/**
 * This is the generic Feature Info Viewer
 * 
 * If the feature Info comes from a special layer which has registered
 * the way to visualize itself, adds a panel that the layer should provide
 * otherwise this viewer will add a panel to visualize HTML or a special
 * viewer to show XML.
 * 
 * @author laura
 *
 */
public class InfoToolViewer extends JPanel {
	
	public static Class XULPanelClass = null;
	
    private javax.swing.JScrollPane jScrollPane = null;
    private JList layerList = null;
    private javax.swing.JSplitPane jSplitPane1 = null;
    private javax.swing.JPanel layerListPanel = null;
	private JPanel infoViewerPanel;	
	private XMLItem[] m_layers;
	IInfoToolPanel infoPanel = null;

    /**
     * This is the default constructor
     */
    public InfoToolViewer() {
        super();
        initialize();
        this.addComponentListener(new componentListener());
    }
    
    public InfoToolViewer(XMLItem[] layers) {
    	super();
    	initialize();
    	setLayers(layers);
    }
    
    public void setLayers(XMLItem[] layers){    	
    	m_layers = layers;
    	initilizeLayerListModel();
    	updateViewer(0);
    	// layerList.setSelectedIndex(0);
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getJSplitPane1(), java.awt.BorderLayout.CENTER);
        this.setSize(600, 600);
        this.setPreferredSize(new Dimension(600, 600));
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setSize(new Dimension(600,600));
            jScrollPane.setPreferredSize( new Dimension(600,600));
            jScrollPane.setViewportView(getLayerListPanel());
        }

        return jScrollPane;
    }
    
    private void initilizeLayerListModel() {
        Vector layerNames = new Vector();
        if (m_layers != null)
        {
        	for (int i = 0; i < m_layers.length; i++)
        	{
        		layerNames.add(m_layers[i].getLayer().getName());
        	}        	
        }         
        
        FormatListModel model = new FormatListModel((String[])layerNames.toArray(new String[0])); 
        getJList().setModel(model);    	
    }

    public JList getJList(){

	  if (layerList == null) {
		  layerList = new JList();
		  initilizeLayerListModel();
	      layerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
	  }
  
      layerList.addListSelectionListener(new ListSelectionListener() { 
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				  int changedIdx = layerList.getSelectedIndex();//e.getFirstIndex();
				 
				  if (changedIdx == -1) return;
				  updateViewer(changedIdx);
	           }
          });    
      return layerList;
}
    /**
     * This method initializes jSplitPane1
     *
     * @return javax.swing.JSplitPane
     */
    private javax.swing.JSplitPane getJSplitPane1() {
        if (jSplitPane1 == null) {
            jSplitPane1 = new javax.swing.JSplitPane();
            jSplitPane1.setLeftComponent(getJScrollPane());
            jSplitPane1.setRightComponent(getInfoViewerPanel());
            jSplitPane1.setDividerSize(4);
            jSplitPane1.setDividerLocation(100);
            jSplitPane1.setSize( new Dimension(600,600));
            jSplitPane1.setPreferredSize( new Dimension(600,600));
        }
        return jSplitPane1;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getLayerListPanel() {
        if (layerListPanel == null) {
            layerListPanel = new javax.swing.JPanel();
            layerListPanel.setLayout(new java.awt.BorderLayout());
            layerListPanel.add(getJList(), java.awt.BorderLayout.CENTER);
        }

        return layerListPanel;
    }

    /**
     * This method initializes jPanel1
     *
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getInfoViewerPanel() {
        if (infoViewerPanel == null) {
            infoViewerPanel = new javax.swing.JPanel();
            infoViewerPanel.setLayout(new java.awt.BorderLayout());
        }
        validate();
        return infoViewerPanel;
    } 
    
    /**
     * updates the layer to display
     *
     */
    private void updateViewer(int changedIdx)
    {
    	if((m_layers == null) || (m_layers.length == 0)) return;    	  
		  final XMLItem item = m_layers[changedIdx];
		  FLayer layer = item.getLayer(); 
		
		  if (layer instanceof InfoByPoint){					  				
			  FLyrDefault defaultLayer = (FLyrDefault)layer;
              if ( XULPanelClass!=null && defaultLayer.getProperty("infoTool.XULFile") != null){
            	try {
					IXULInfoToolSupport infoPanel = (IXULInfoToolSupport) XULPanelClass.newInstance();
					infoPanel.setXULFile((String)defaultLayer.getProperty("infoTool.XULFile"));
					infoViewerPanel.removeAll();
					infoViewerPanel.add((JPanel)infoPanel);
					infoPanel.show(item);
					infoViewerPanel.setVisible( true ); 
					revalidate();
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	  
              }
              
              if (defaultLayer.getProperty("customPanel") != null){
				  
				Object o = (Object)defaultLayer.getProperty("customPanel");
				if (o instanceof IInfoToolPanel) {
					  infoPanel = (IInfoToolPanel)o;
				} else {
				 	try {
						Class c = (Class)o;
				 		infoPanel = (IInfoToolPanel)c.newInstance();
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
				infoViewerPanel.removeAll();
				infoViewerPanel.add((JPanel)infoPanel);
				infoPanel.show(item);
				infoViewerPanel.setVisible( true ); 
				revalidate();

			  } else {
				  
				  if (item.toString().toLowerCase().endsWith( "</html>"))//if (item.toString().toLowerCase().startsWith( "<html>"))
				  {
					  //skip the header info
					  IInfoToolPanel htmlPanel = new HTMLInfoToolPanel();
					 
					  int idx = item.toString().toLowerCase().indexOf("<html");
					  if (idx != -1){
						  htmlPanel.show(item.toString().substring(idx));					  
					  }else{
						  htmlPanel.show(item.toString());
					  }
					  infoViewerPanel.removeAll();
					  infoViewerPanel.add((JPanel)htmlPanel);
					  infoViewerPanel.setVisible( true ); 							  
					  revalidate();
				  } else {
					  FInfoDialogXML dlgXML = new FInfoDialogXML();
						try {
							dlgXML.setModel(new XMLContent() {
								private ContentHandler handler;

								public void setContentHandler(ContentHandler arg0) {
									handler = arg0;
								}

								public void parse() throws SAXException {
									handler.startDocument();
									item.parse( handler);
									handler.endDocument();
								}
							});
							dlgXML.getXmlTree().setRootVisible(false);
							DefaultTreeModel treeModel = (DefaultTreeModel) dlgXML
									.getXmlTree().getModel();
							DefaultMutableTreeNode n;
							DefaultMutableTreeNode root = (DefaultMutableTreeNode) dlgXML
									.getXmlTree().getModel().getRoot();
							n = root.getFirstLeaf();
							TreePath path = new TreePath(treeModel.getPathToRoot(n));
							dlgXML.getXmlTree().expandPath(path);
							dlgXML.getXmlTree().setSelectionPath(path);
							//dlgXML.
							
							infoViewerPanel.removeAll();
							infoViewerPanel.add(dlgXML);
							infoViewerPanel.setVisible( true ); 
							this.validate();
	    					this.doLayout();	
							
						} catch (SAXException e1) {
							e1.printStackTrace();
						}
				  }
			  }
		  }    	
    }
    
    class componentListener implements ComponentListener{

		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentResized(ComponentEvent e) {
			
			//if (e.getComponent() == )
			if (infoPanel != null){
				infoPanel.refreshSize();
			}
			
			
		}

		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
