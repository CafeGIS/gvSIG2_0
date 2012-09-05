package com.iver.cit.gvsig.project.documents.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.PageRanges;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jpedal.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.objects.PrinterOptions;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class PDFViewerWindow extends JPanel implements IWindow,
		PropertyChangeListener {

	private WindowInfo m_viewInfo;
	private PdfDecoder pf;

	//private String viewerTitle="Visor PDF";
	private String currentFile=null;
	private int currentPage=1;
	private final JLabel pageCounter1=new JLabel(" "+PluginServices.getText(this,"Pagina")+" ");
	private JTextField pageCounter2=new JTextField(4);
	private JLabel pageCounter3=new JLabel(PluginServices.getText(this,"de"));

	public PDFViewerWindow(String currentFile){
		super();
		initialize(currentFile);
	}

	private void initialize(String currentFile){

		pf = new PdfDecoder();
		this.currentFile = currentFile;
		//currentFile = "C:\\Cartografia\\prueba.pdf";

		try{
			//this opens the PDF and reads its internal details
			pf.openPdfFile(currentFile);

			//these 2 lines opens page 1 at 100% scaling
			pf.decodePage(currentPage);
			float scaling =(float) 1.5;
			pf.setPageParameters(scaling,1); //values scaling (1=100%). page number
			pf.setDisplayView(Display.SINGLE_PAGE,Display.DISPLAY_CENTERED);
		}catch(Exception e){
			e.printStackTrace();
		}

		//setup our GUI display
		initializeViewer();

		//set page number display
		pageCounter2.setText(currentPage+"");
		pageCounter3.setText(PluginServices.getText(this,"de")+" "+pf.getPageCount()+" ");
	}


	public void setCurrentFile(String currentFile) {
		this.currentFile = currentFile;
	}

	private void initializeViewer() {

		Container cPane = this;
		cPane.setLayout(new BorderLayout());

//		JButton open = initOpenBut();//setup open button
		Component[] itemsToAdd = initChangerPanel();//setup page display and changer

		JPanel topBar = new JPanel();
		topBar.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
//		topBar.add(pageChanger);
		for(int i=0;i<itemsToAdd.length;i++){
            topBar.add(itemsToAdd[i]);
        }

		cPane.add(topBar,BorderLayout.NORTH);

		JScrollPane display = getJPaneViewer();//setup scrollpane with pdf display inside
		cPane.add(display,BorderLayout.CENTER);

		//pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width/2,screen.height/2);
		//<start-13>
		//setLocationRelativeTo(null);//centre on screen
		//<end-13>
		setVisible(true);
	}

private Component[] initChangerPanel(){

        Component[] list = new Component[12];

		/**back to page 1*/
		JButton start = new JButton();
		start.setBorderPainted(false);
		URL startImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/start.gif");
		start.setIcon(new ImageIcon(startImage));
		start.setToolTipText(PluginServices.getText(this,"primera_pagina"));
//		currentBar1.add(start);
        list[0] = start;
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    if(currentFile!=null && currentPage!=1){
			    	currentPage = 1;
			    	try {
						pf.decodePage(currentPage);
						pf.invalidate();
						repaint();
					} catch (Exception e1) {
						System.err.println("back to page 1");
						e1.printStackTrace();
					}

			    	//set page number display
					pageCounter2.setText(currentPage+"");
			    }
			}
		});

		/**back 10 icon*/
		JButton fback = new JButton();
		fback.setBorderPainted(false);
		URL fbackImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/fback.gif");
		fback.setIcon(new ImageIcon(fbackImage));
		fback.setToolTipText(PluginServices.getText(this,"diez_paginas_atras"));
//		currentBar1.add(fback);
        list[1] = fback;
		fback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentFile!=null && currentPage>10){
					currentPage -= 10;
			    	try {
						pf.decodePage(currentPage);
						pf.invalidate();
						repaint();
					} catch (Exception e1) {
						System.err.println("back 10 pages");
						e1.printStackTrace();
					}

//			    	set page number display
					pageCounter2.setText(currentPage+"");
				}
			}
		});

		/**back icon*/
		JButton back = new JButton();
		back.setBorderPainted(false);
		URL backImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/back.gif");
		back.setIcon(new ImageIcon(backImage));
		back.setToolTipText(PluginServices.getText(this,"pagina_atras"));
//		currentBar1.add(back);
        list[2] = back;
		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(currentFile!=null && currentPage>1){
				currentPage -= 1;
		    	try {
					pf.decodePage(currentPage);
					pf.invalidate();
					repaint();
				} catch (Exception e1) {
					System.err.println("back 1 page");
					e1.printStackTrace();
				}

//		    	set page number display
				pageCounter2.setText(currentPage+"");
			}
			}
		});

		pageCounter2.setEditable(true);
		pageCounter2.addActionListener(new ActionListener(){

		    public void actionPerformed(ActionEvent a) {

		        String value=pageCounter2.getText().trim();
		        int newPage;

		        //allow for bum values
		        try{
		            newPage=Integer.parseInt(value);

		            if((newPage>pf.getPageCount())|(newPage<1)){
		            	return;
		            }

		            currentPage=newPage;
		            try{
		            	pf.decodePage(currentPage);
		            	pf.invalidate();
						repaint();
		            }catch(Exception e){
		            	System.err.println("page number entered");
		            	e.printStackTrace();
		            }

		        }catch(Exception e){
		            JOptionPane.showMessageDialog(null,">"+value+ "< "+PluginServices.getText(this,"valor_incorrecto")+pf.getPageCount());
				}

		    }

		});

		/**put page count in middle of forward and back*/
//		currentBar1.add(pageCounter1);
//		currentBar1.add(new JPanel());//add gap
//		currentBar1.add(pageCounter2);
//		currentBar1.add(new JPanel());//add gap
//		currentBar1.add(pageCounter3);
        list[3] = pageCounter1;
        list[4] = new JPanel();
        list[5] = pageCounter2;
        list[6] = new JPanel();
        list[7] = pageCounter3;

		/**forward icon*/
		JButton forward = new JButton();
		forward.setBorderPainted(false);
		URL fowardImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/forward.gif");
		forward.setIcon(new ImageIcon(fowardImage));
		forward.setToolTipText(PluginServices.getText(this, "pagina_delante"));
//		currentBar1.add(forward);
        list[8] = forward;
		forward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(currentFile!=null && currentPage<pf.getPageCount()){
				currentPage += 1;
				try {
					pf.decodePage(currentPage);
					pf.invalidate();
					repaint();
				} catch (Exception e1) {
					System.err.println("forward 1 page");
					e1.printStackTrace();
				}

//				set page number display
				pageCounter2.setText(currentPage+"");
			}
			}
		});

		/**fast forward icon*/
		JButton fforward = new JButton();
		fforward.setBorderPainted(false);
		URL ffowardImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/fforward.gif");
		fforward.setIcon(new ImageIcon(ffowardImage));
		fforward.setToolTipText(PluginServices.getText(this,"10_paginas_delante"));
//		currentBar1.add(fforward);
        list[9] = fforward;
		fforward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(currentFile!=null && currentPage<pf.getPageCount()-9){
				currentPage += 10;
				try {
					pf.decodePage(currentPage);
					pf.invalidate();
					repaint();
				} catch (Exception e1) {
					System.err.println("forward 10 pages");
					e1.printStackTrace();
				}

//				set page number display
				pageCounter2.setText(currentPage+"");
			}
			}
		});

		/**goto last page*/
		JButton end = new JButton();
		end.setBorderPainted(false);
		URL endImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/end.gif");
		end.setIcon(new ImageIcon(endImage));
		end.setToolTipText(PluginServices.getText(this,"ultima_pagina"));
//		currentBar1.add(end);
        list[10] = end;
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			if(currentFile!=null && currentPage<pf.getPageCount()){
				currentPage = pf.getPageCount();
				try {
					pf.decodePage(currentPage);
					pf.invalidate();
					repaint();
				} catch (Exception e1) {
					System.err.println("forward to last page");
					e1.printStackTrace();
				}

//				set page number display
				pageCounter2.setText(currentPage+"");
			}
			}
		});

		/**Print*/
		JButton print = new JButton();
		print.setBorderPainted(false);
		URL printImage =getClass().getResource("/org/jpedal/examples/simpleviewer/res/print.gif");
		print.setIcon(new ImageIcon(printImage));
		print.setToolTipText(PluginServices.getText(this,"imprimir"));
//		currentBar1.add(end);
        list[11] = print;
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printPDF();
			}
		});
		return list;
	}

	public void printPDF(){
		PrinterJob printJob = PrinterJob.getPrinterJob();

		printJob.setPageable(pf);
		//decode_pdf.setPageFormat(pf);

		/**
		 * this additional functionality is available under printable interface
		 */
		//setup default values to add into JPS
		PrintRequestAttributeSet aset=new HashPrintRequestAttributeSet();
		aset.add(new PageRanges(1,pf.getPageCount()));

		boolean printFile=printJob.printDialog(aset);

		//set page range
		PageRanges r=(PageRanges) aset.get(PageRanges.class);
		if(r!=null) {
			try {
				PageFormat pformat = printJob.defaultPage();

				Paper paper = new Paper();
				paper.setSize(595, 842); //default A4
				paper.setImageableArea(43, 43, 545, 792); //actual print 'zone'

				pformat.setPaper(paper);

				//pageable provides a method getPageFormat(int p) - this method allows it to be set by JPedal
				pf.setPageFormat(pformat);
				pf.setPagePrintRange(r);
			} catch (PdfException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * generic call to both Pageable and printable
		 */
		if (printFile) {
			try {
				pf.setPrintPageScalingMode(PrinterOptions.PAGE_SCALING_FIT_TO_PRINTER_MARGINS);
				printJob.print();
			} catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



	private JScrollPane getJPaneViewer(){
		JScrollPane currentScroll = new JScrollPane();
		currentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		currentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		currentScroll.setViewportView(pf);
	return currentScroll;
	}

	public WindowInfo getWindowInfo() {
		if (m_viewInfo==null) {
			m_viewInfo = new WindowInfo(WindowInfo.PALETTE);
			m_viewInfo.setMaximizable(true);
			m_viewInfo.setWidth(this.getWidth());
			m_viewInfo.setHeight(this.getHeight());
			m_viewInfo.setTitle(PluginServices.getText(this, "pdf_viewer"));
		}
		return m_viewInfo;
	}

	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return WindowInfo.TOOL_PROFILE;
	}

}
