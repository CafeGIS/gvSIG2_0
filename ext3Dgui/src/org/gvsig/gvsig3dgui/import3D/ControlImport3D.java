/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
*
* Copyright (C) 2005 IVER T.I. and Generalitat Valenciana.
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
*/



package org.gvsig.gvsig3dgui.import3D;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.gvsig.driver.OSGDriver;
import org.gvsig.geometries3D.MultiGeometry;
import org.gvsig.gpe.osg.OSGParser;
import org.gvsig.gui.beans.datainput.DataInputField;
import org.gvsig.gvsig3d.cacheservices.OSGCacheService;
import org.gvsig.gvsig3d.map3d.MapContext3D;
import org.gvsig.gvsig3dgui.view.View3D;

import org.gvsig.osgvp.core.osg.Vec3;
import org.gvsig.osgvp.viewer.Intersections;

import com.iver.ai2.gvsig3d.map3d.layers.Layer3DProps;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.IProjectView;

/**
 * @author Ángel Fraile Griñán  e-mail: angel.fraile@iver.es
 * @since 1.1
 * 
 */
public class ControlImport3D extends JPanel implements IWindow, ActionListener,
		MouseListener {

	private static final long serialVersionUID = 8557746972654979029L;
	private JPanel panelOpcionesObjeto;
	private TitledBorder borderOpcionesObjeto;
	private JLabel labelLatitud;
	private DataInputField textLatitud;
	private JLabel labelLongitud;
	private DataInputField textLongitud;
	private JLabel labelEscaladoX;
	private JLabel labelEscaladoY;
	private JLabel labelEscaladoZ;
	private DataInputField textEscaladoX;
	private DataInputField textEscaladoY;
	private DataInputField textEscaladoZ;
	private JLabel labelRotacionX;
	private JLabel labelRotacionY;
	private JLabel labelRotacionZ;
	private DataInputField textRotacionX;
	private DataInputField textRotacionY;
	private DataInputField textRotacionZ;
	private JPanel panelImportar;
	private TitledBorder borderImportar;
	private JButton buttonImportar;
	private int tipoObjectImport;
	private JTextField textFile;
	private TitledBorder border;
	private JPanel panelPath;
	private Hashtable iveExtensionsSupported = null;
	private Hashtable osgExtensionsSupported = null;
	private String lastPath = null;
	private JLabel labelAltura;
	private DataInputField textAltura;
	private JPanel panelButtons;
	private JButton buttonAccept;
	private JButton buttonCancel;
	private JPanel panelAux;
	private JLabel labelX;
	private DataInputField textX;
	private JLabel labelY;
	private DataInputField textY;

	private View3D vista;
	private File fileOSG = null;
	private FileInputStream fileInput = null;
	private Vec3 position = new Vec3(0,0,0);

	
	/**
	 * Building the panel options
	 * 
	 * @param tipoObjeto
	 * @param vista3D: actual 3D view selected
	 */
	public ControlImport3D(int tipoObjeto, View3D vista3D) {

		tipoObjectImport = tipoObjeto;
		vista = vista3D;

		this.panelOpcionesObjeto = new JPanel();
		this.borderOpcionesObjeto = new TitledBorder(PluginServices.getText(
				this, "Opciones de objeto"));
		this.panelOpcionesObjeto.setBorder(this.borderOpcionesObjeto);
		this.panelOpcionesObjeto.setLayout(new GridLayout(5, 6, 5, 5));

		this.textLatitud = new DataInputField();
		this.labelLatitud = new JLabel(
				PluginServices.getText(this, "Latitude"), JLabel.RIGHT);

		this.textLongitud = new DataInputField();
		this.labelLongitud = new JLabel(PluginServices.getText(this,
				"Longitude"), JLabel.RIGHT);

		this.textEscaladoX = new DataInputField();
		this.textEscaladoX.setValue("1");
		this.textEscaladoY = new DataInputField();
		this.textEscaladoY.setValue("1");
		this.textEscaladoZ = new DataInputField();
		this.textEscaladoZ.setValue("1");
		this.labelEscaladoX = new JLabel(PluginServices
				.getText(this, "EscaladoX"), JLabel.RIGHT);
		this.labelEscaladoY = new JLabel(PluginServices
				.getText(this, "EscaladoY"), JLabel.RIGHT);
		this.labelEscaladoZ = new JLabel(PluginServices
				.getText(this, "EscaladoZ"), JLabel.RIGHT);

		this.textRotacionX = new DataInputField();
		this.textRotacionY = new DataInputField();
		this.textRotacionZ = new DataInputField();
		this.labelRotacionX = new JLabel(PluginServices.getText(this,
				"Rotation_degreesX"), JLabel.RIGHT);
		this.labelRotacionY = new JLabel(PluginServices.getText(this,
				"Rotation_degreesY"), JLabel.RIGHT);
		this.labelRotacionZ = new JLabel(PluginServices.getText(this,
				"Rotation_degreesZ"), JLabel.RIGHT);

		this.textAltura = new DataInputField();
		this.labelAltura = new JLabel(PluginServices.getText(this, "Hight"),
				JLabel.RIGHT);

		this.textX = new DataInputField();
		this.labelX = new JLabel(PluginServices.getText(this, "Coordenada X:"),
				JLabel.RIGHT);

		this.textY = new DataInputField();
		this.labelY = new JLabel(PluginServices.getText(this, "Coordenada Y:"),
				JLabel.RIGHT);

		this.panelOpcionesObjeto.add(this.labelLatitud);
		this.panelOpcionesObjeto.add(this.textLatitud);
		this.panelOpcionesObjeto.add(this.labelX);
		this.panelOpcionesObjeto.add(this.textX);
		this.panelOpcionesObjeto.add(new JPanel());
		this.panelOpcionesObjeto.add(new JPanel());

		this.panelOpcionesObjeto.add(this.labelLongitud);
		this.panelOpcionesObjeto.add(this.textLongitud);
		this.panelOpcionesObjeto.add(this.labelY);
		this.panelOpcionesObjeto.add(this.textY);
		this.panelOpcionesObjeto.add(new JPanel());
		this.panelOpcionesObjeto.add(new JPanel());

		this.panelOpcionesObjeto.add(this.labelAltura);
		this.panelOpcionesObjeto.add(this.textAltura);
		this.panelOpcionesObjeto.add(new JPanel());
		this.panelOpcionesObjeto.add(new JPanel());
		this.panelOpcionesObjeto.add(new JPanel());
		this.panelOpcionesObjeto.add(new JPanel());

		this.panelOpcionesObjeto.add(this.labelEscaladoX);
		this.panelOpcionesObjeto.add(this.textEscaladoX);
		this.panelOpcionesObjeto.add(this.labelEscaladoY);
		this.panelOpcionesObjeto.add(this.textEscaladoY);
		this.panelOpcionesObjeto.add(this.labelEscaladoZ);
		this.panelOpcionesObjeto.add(this.textEscaladoZ);

		this.panelOpcionesObjeto.add(this.labelRotacionX);
		this.panelOpcionesObjeto.add(this.textRotacionX);
		this.panelOpcionesObjeto.add(this.labelRotacionY);
		this.panelOpcionesObjeto.add(this.textRotacionY);
		this.panelOpcionesObjeto.add(this.labelRotacionZ);
		this.panelOpcionesObjeto.add(this.textRotacionZ);

		this.panelImportar = new JPanel();
		this.borderImportar = new TitledBorder(PluginServices.getText(this,
				"File"));
		this.panelImportar.setBorder(this.borderImportar);
		this.panelImportar.setLayout(new GridLayout(1, 1, 5, 5));
		this.buttonImportar = new JButton(PluginServices.getText(this,
				"Examine"));
		this.panelImportar.add(this.buttonImportar);
		this.buttonImportar.addActionListener(this);

		this.panelAux = new JPanel();
		this.panelAux.setLayout(new BorderLayout(5, 5));
		this.panelAux.add(this.panelOpcionesObjeto, BorderLayout.CENTER);
		this.panelAux.add(this.panelImportar, BorderLayout.SOUTH);

		this.panelPath = new JPanel();
		this.border = new TitledBorder(PluginServices.getText(this,
				"Ruta_manual"));
		this.panelPath.setBorder(this.border);
		this.panelPath.setLayout(new GridLayout(1, 1, 5, 5));
		this.textFile = new JTextField();
		this.panelPath.add(this.textFile);

		this.panelButtons = new JPanel();
		this.panelButtons.setLayout(new GridLayout(1, 3, 5, 5));
		this.buttonAccept = new JButton(PluginServices.getText(this, "Accept"));
		this.buttonAccept.addActionListener(this);
		this.buttonCancel = new JButton(PluginServices.getText(this, "Cancel"));
		this.buttonCancel.addActionListener(this);
		this.panelButtons.add(new JPanel());
		this.panelButtons.add(this.buttonAccept);
		this.panelButtons.add(this.buttonCancel);

		this.setLayout(new BorderLayout(5, 5));
		this.add(this.panelAux, BorderLayout.NORTH);
		this.add(this.panelPath, BorderLayout.CENTER);
		this.add(this.panelButtons, BorderLayout.SOUTH);

		vista.getCanvas3d().addMouseListener(this);

	}

	/**
	 * Window properties
	 * 
	 * @see com.iver.andami.ui.mdiManager.IWindow#getWindowInfo()
	 */

	public WindowInfo getWindowInfo() {
		WindowInfo m_viewinfo = new WindowInfo();
		String cad = null;
		// java.awt.Dimension minSize = new java.awt.Dimension();
		// minSize.setSize(290, 400);

		if (tipoObjectImport == 0)
			cad = "Importar_Objeto_3D";
		else if (tipoObjectImport == 1)
			cad = "Importar_Objeto_3D_Vectorial";

		m_viewinfo.setTitle(PluginServices.getText(this, cad));
		m_viewinfo.setHeight(290);
		m_viewinfo.setWidth(610);
		return m_viewinfo;
	}

	/**
	 * Buttons events listener
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.buttonImportar) {
			actionPerformedImport();

		}
		if (event.getSource() == this.buttonAccept) {
			actionPerformedAccept();

		}
		if (event.getSource() == this.buttonCancel) {
			PluginServices.getMDIManager().closeWindow(this);
		}
	}

	/**
	 * Accept button listener
	 */
	private void actionPerformedAccept() {


		if (fileOSG == null) {// of the user write the file
			// String fichManual = getJTextFieldFichero();
			if(getJTextFieldFichero().equals("")) {
				JOptionPane.showMessageDialog(null, PluginServices.getText(this,"root_in"),
						PluginServices.getText(this,"no_file"), JOptionPane.WARNING_MESSAGE);
				return;
			}
			else {
					fileOSG = new File(getJTextFieldFichero());
				if(!fileOSG.exists()){
					JOptionPane.showMessageDialog(null,PluginServices.getText(this,"no_root"),
							PluginServices.getText(this,"no_file2"), JOptionPane.WARNING_MESSAGE);
					fileOSG = null;
					this.setTextFieldFichero();
					return;
				}
			}

		} else {
			try {
				if(!fileOSG.exists()){
					JOptionPane.showMessageDialog(null, PluginServices.getText(this,"root_in"),
							PluginServices.getText(this,"no_file"), JOptionPane.WARNING_MESSAGE);
					return;
				}
				fileInput = new FileInputStream(fileOSG);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		OSGDriver _osgDriver = new OSGDriver();
		OSGParser parser = new OSGParser("OSG", "OSG File Formats Parser");
		parser.parse(_osgDriver, null, fileOSG.toURI());
		MultiGeometry root = _osgDriver.getRootFeature();

		IProjectView model = vista.getModel();
		MapContext mapContext = model.getMapContext();
		FLayers layers = mapContext.getLayers();
		FLayer[] actives = layers.getActives();
		if (actives.length == 1
				&& Layer3DProps.getLayer3DProps(actives[0]).getType() == Layer3DProps.layer3DOSG) {
			OSGCacheService osgCache = (OSGCacheService) Layer3DProps.getLayer3DProps(actives[0]).getCacheService();
			Vec3 scale = new Vec3(Double.parseDouble(textEscaladoX.getValue()), Double.parseDouble(textEscaladoY.getValue()), Double.parseDouble(textEscaladoZ.getValue()));
			Vec3 rotation = new Vec3(Double.parseDouble(textRotacionX.getValue()), Double.parseDouble(textRotacionY.getValue()), Double.parseDouble(textRotacionZ.getValue()));
			osgCache.AddGeometryToLayer(root, position,rotation,scale);
		}
		else {
			JOptionPane.showMessageDialog(null, PluginServices.getText(this,"editable_layer_select_info"),
					PluginServices.getText(this,"editable_layer_select"), JOptionPane.WARNING_MESSAGE);
			return;
		}
		PluginServices.getMDIManager().closeWindow(this);
	}

	/**
	 * Import file button listener
	 */
	private void actionPerformedImport() {
		JFileChooser jfc = new JFileChooser(lastPath);
		jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
		iveExtensionsSupported = new Hashtable();
		osgExtensionsSupported = new Hashtable();
		osgExtensionsSupported.put("osg", new MyFileFilter("osg",
				PluginServices.getText(this, "Ficheros *.osg"), "osg"));
		iveExtensionsSupported.put("ive", new MyFileFilter("ive",
				PluginServices.getText(this, "Ficheros *.ive"), "ive"));
		iveExtensionsSupported.put("obj", new MyFileFilter("obj",
				PluginServices.getText(this, "Ficheros *.obj"), "obj"));
		iveExtensionsSupported.put("dae", new MyFileFilter("dae",
				PluginServices.getText(this, "Ficheros *.dae"), "dae"));
		iveExtensionsSupported.put("3ds", new MyFileFilter("3ds",
				PluginServices.getText(this, "Ficheros *.3ds"), "3ds"));
		iveExtensionsSupported.put("*", new MyFileFilter("*",
				PluginServices.getText(this, "Ficheros *.*"), "*.*"));
		
		Iterator iter = osgExtensionsSupported.values().iterator();
		
		while (iter.hasNext()) {
			jfc.addChoosableFileFilter((FileFilter) iter.next());
		}

		iter = iveExtensionsSupported.values().iterator();
		while (iter.hasNext()) {
			jfc.addChoosableFileFilter((FileFilter) iter.next());
		}

		if (jfc.showOpenDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {

			fileOSG = jfc.getSelectedFile();
			if (fileOSG.exists()) {// file don't exists in the directory.

				MyFileFilter filter = (MyFileFilter) jfc.getFileFilter();
				fileOSG = filter.normalizeExtension(fileOSG);
				textFile.setText(fileOSG.getAbsolutePath());
			} else {
				JOptionPane.showMessageDialog(null,PluginServices.getText(this,"no_root"),
						PluginServices.getText(this,"no_file2"), JOptionPane.WARNING_MESSAGE);
				return;
			}
		}// If aprove option.
	}

	/**
	 * Getters and Setters of all TextFields values
	 */
	public String getJTextFieldFichero() {
		return textFile.getText();
	}
	
	public void setTextFieldFichero() {
		textFile.setText(null);
	}
	/*
	 * All MouseListener methods
	 * 
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseReleased(MouseEvent e) {

		Point pScreen = e.getPoint();

		if (e.getButton() == MouseEvent.BUTTON1) {
			Vec3 intersection = null;

			// System.err.println("Coordenadas de pantalla " + pScreen.getX() +
			// ","+
			// pScreen.getY());

			textX.setValue(Double.toString(pScreen.getX()));
			textY.setValue(Double.toString(pScreen.getY()));

			IProjectView model = vista.getModel();

			MapContext3D mcontext = (MapContext3D) model.getMapContext();
			mcontext.getCanvas3d();

			Intersections hits = mcontext.getCanvas3d().getOSGViewer().rayPick(
					(int) pScreen.getX(), (int) pScreen.getY());
			Point2D pWorld = new Point2D.Double();
			if (hits.containsIntersections()) {
				Vec3 hit = hits.getFirstIntersection().getIntersectionPoint();
				// convert to geo coordinates
				position = hit;
				// System.err.println("Interseccion de osg " + hit.x() + ","+
				// hit.y());
				if (mcontext.getProjection().getAbrev().compareToIgnoreCase(
						"EPSG:4326") == 0) {
					Vec3 geoPt = mcontext.getPlanet()
							.convertXYZToLatLongHeight(hit);
					// Swap the coordinates X and Y, because they are invert.
					intersection = new Vec3(geoPt.y(), geoPt.x(), geoPt.z());
				} else {
					intersection = hit;
				}
			} else {
				if (mcontext.getProjection().getAbrev().compareToIgnoreCase(
						"EPSG:4326") == 0) {
					pWorld.setLocation(360, 120);
					intersection = new Vec3(360, 120, 0);
				} else {
					intersection = new Vec3(1e100, 1e100, 0);
					// pWorld.setLocation(1e100, 1e100);
				}
			}

			textLatitud.setValue(Double.toString(intersection.x()));
			textLongitud.setValue(Double.toString(intersection.y()));

			textAltura.setValue(Double.toString(intersection.z()));

		}// if e.getButton()
	}// if hits

	public Object getWindowProfile() {
		return WindowInfo.PROPERTIES_PROFILE;
	}

}


/**
 * 
 * @version 14/08/2007
 * @author Borja Sánchez Zamorano (borja.sanchez@iver.es)
 * 
 */
class MyFileFilter extends FileFilter {

	private String[] extensiones = new String[1];
	private String description;
	private boolean dirs = true;
	private String info = null;

	public MyFileFilter(String[] ext, String desc) {
		extensiones = ext;
		description = desc;
	}

	public MyFileFilter(String[] ext, String desc, String info) {
		extensiones = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc) {
		extensiones[0] = ext;
		description = desc;
	}

	public MyFileFilter(String ext, String desc, String info) {
		extensiones[0] = ext;
		description = desc;
		this.info = info;
	}

	public MyFileFilter(String ext, String desc, boolean dirs) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
	}

	public MyFileFilter(String ext, String desc, boolean dirs, String info) {
		extensiones[0] = ext;
		description = desc;
		this.dirs = dirs;
		this.info = info;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			if (dirs) {
				return true;
			} else {
				return false;
			}
		}
		for (int i = 0; i < extensiones.length; i++) {
			if (extensiones[i].equals("")) {
				continue;
			}
			if (getExtensionOfAFile(f).equalsIgnoreCase(extensiones[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	public String[] getExtensions() {
		return extensiones;
	}

	public boolean isDirectory() {
		return dirs;
	}

	private String getExtensionOfAFile(File file) {
		String name;
		int dotPos;
		name = file.getName();
		dotPos = name.lastIndexOf(".");
		if (dotPos < 1) {
			return "";
		}
		return name.substring(dotPos + 1);
	}

	public File normalizeExtension(File file) {
		String ext = getExtensionOfAFile(file);
		if (ext.equals("") || !(this.accept(file))) {
			return new File(file.getAbsolutePath() + "." + extensiones[0]);
		}
		return file;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
