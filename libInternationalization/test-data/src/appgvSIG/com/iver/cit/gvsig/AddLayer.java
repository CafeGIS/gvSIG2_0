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
package com.iver.cit.gvsig;

import java.awt.Component;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.cresques.cts.gt2.CoordSys;
import org.cresques.cts.gt2.CoordTrans;
import org.opengis.layer.Layer;

import com.hardcode.driverManager.Driver;
import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.engine.instruction.FieldNotFoundException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.DriverException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.drivers.RasterDriver;
import com.iver.cit.gvsig.fmap.drivers.VectorialFileDriver;
import com.iver.cit.gvsig.fmap.drivers.WithDefaultLegend;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.iver.cit.gvsig.fmap.rendering.LegendFactory;
import com.iver.cit.gvsig.fmap.rendering.VectorialLegend;
import com.iver.cit.gvsig.gui.FOpenDialog;
import com.iver.cit.gvsig.gui.FileOpenDialog;
import com.iver.cit.gvsig.gui.View;
import com.iver.cit.gvsig.gui.WizardPanel;


/**
 * Extensión que abre un diálogo para seleccionar la capa o capas que se
 * quieren añadir a la vista.
 *
 * @author Fernando González Cortés
 */
public class AddLayer extends Extension {
	public FOpenDialog fopen = null;
	
	private static ArrayList wizardStack = null;
	
	static {
		AddLayer.wizardStack = new ArrayList();
	}
	
	public static void addWizard(Class wpClass) {
		AddLayer.wizardStack.add(wpClass);
	}
	
	public static WizardPanel getInstance(int i) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class wpClass = (Class) AddLayer.wizardStack.get(i);
		Class [] args = {};
		Object [] params = {};
		WizardPanel wp = (WizardPanel) wpClass.getConstructor(args).newInstance(params);

		wp.initWizard();
		
		return wp;
	}


	/**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
	public boolean isVisible() {
		com.iver.andami.ui.mdiManager.View f = PluginServices.getMDIManager()
															 .getActiveView();

		if (f == null) {
			return false;
		}

		return (f.getClass() == View.class);
	}

	/**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
	public void initialize() {
	}

    private void checkProjection(FLayer lyr, ViewPort viewPort)
    {
        if (lyr instanceof FLyrVect)
        {
            FLyrVect lyrVect = (FLyrVect) lyr;
            IProjection proj = lyr.getProjection();
            // Comprobar que la projección es la misma que la vista
            if (proj == null)
            {
                // SUPONEMOS que la capa está en la proyección que 
                // estamos pidiendo (que ya es mucho suponer, ya).
                lyrVect.setProjection(viewPort.getProjection());
                return;
            }
            if (proj != viewPort.getProjection()) {
                int option = JOptionPane.showConfirmDialog(null,
                        PluginServices.getText(this, "reproyectar_aviso"),
                        PluginServices.getText(this, "reproyectar_pregunta"),
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.NO_OPTION) {
                    return;
                } else {
                    ICoordTrans ct = new CoordTrans((CoordSys) proj,
                            (CoordSys) viewPort.getProjection());
                    lyrVect.setCoordTrans(ct);
                    System.err.println("coordTrans = " +
                        proj.getAbrev() + " " +
                        viewPort.getProjection().getAbrev());
                }
            }
        }                    

    }
	/**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
	public void execute(String actionCommand) {
		//Project project = ((ProjectExtension) PluginServices.getExtension(ProjectExtension.class)).getProject();
		View theView = (View) PluginServices.getMDIManager().getActiveView();
		
		this.addLayers(theView.getMapControl());
		return;

	}

	/**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}
	
	

	/**
	 * Abre dialogo para añadir capas y las añade
	 * en mapControl
	 * 
	 * Devuelve true si se han añadido capas.
	 */	
	public boolean addLayers(MapControl mapControl) {		
		fopen = new FOpenDialog();

		FileOpenDialog fileDlg = new FileOpenDialog(new Class[] {
					VectorialFileDriver.class, RasterDriver.class
				});
		fopen.addTab(PluginServices.getText(this, "Fichero"), fileDlg);
		for (int i=0; i<wizardStack.size(); i++) {
			WizardPanel wp;
			try {
				wp = AddLayer.getInstance(i);
				fopen.addWizardTab(wp.getTabName(), wp);			
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		PluginServices.getMDIManager().addView(fopen);

		if (fopen.isAccepted()) {
			FLayer lyr = null;

			if (fopen.getSelectedTab() == fileDlg) {
				if (fileDlg.getFiles() == null) {
					return false;
				}

				IProjection proj = FOpenDialog.getLastProjection();
				File[] files = fileDlg.getFiles();
				String[] driverNames = fileDlg.getDriverNames();
				Driver[] drivers = new Driver[driverNames.length];
				for (int i = 0; i < drivers.length; i++) {
					try {
						drivers[i] = LayerFactory.getDM().getDriver(driverNames[i]);
					} catch (DriverLoadException e) {
						NotificationManager.addError("No se pudo cargar el driver", e);
					}
				}
				Rectangle2D[] rects=new Rectangle2D[files.length];
				boolean first=false;
				mapControl.getMapContext()
				   .beginAtomicEvent();

				for (int iFile = 0; iFile < files.length; iFile++) {
					File fich = files[iFile];
					String layerName = fich.getName();
					//String layerPath = fich.getAbsolutePath();

					try {
						// FJP: Comento esto (if (fileDlg.accept(fich))) para resolver el bug 75. ¿Esto estaba antes
                        // o lo apuesto alguien por algo en concreto?.
						// if (fileDlg.accept(fich))
    						if (drivers[iFile] instanceof VectorialFileDriver){
    							lyr = LayerFactory.createLayer(layerName,
    									(VectorialFileDriver) drivers[iFile], fich, proj);
    						}else if (drivers[iFile] instanceof RasterDriver){
    							lyr = LayerFactory.createLayer(layerName,
    									(RasterDriver) drivers[iFile], fich, proj);
    						}

						if (lyr != null) {
							lyr.setVisible(true);
							if (mapControl.getMapContext().getViewPort().getExtent()==null){
								first=true;
							}


                            
                            try {
                                
                                // Le asignamos también una legenda por defecto acorde con
                                // el tipo de shape que tenga. Tampoco sé si es aquí el
                                // sitio adecuado, pero en fin....
                                checkProjection(lyr, mapControl.getViewPort());
                                mapControl.getMapContext().getLayers()
                                   .addLayer(lyr);
                                
                                if (lyr instanceof FLyrVect)
                                {
                                    FLyrVect lyrVect = (FLyrVect) lyr;
                                    if (drivers[iFile] instanceof WithDefaultLegend) {
                                        WithDefaultLegend aux = (WithDefaultLegend) drivers[iFile];
                                        lyrVect.setLegend((VectorialLegend) aux.getDefaultLegend());                                        
                                    } else {
                                        lyrVect.setLegend(LegendFactory.createSingleSymbolLegend(
                                                lyrVect.getShapeType()));
                                    }
                                }
                            } catch (FieldNotFoundException e) {
                                //Esta no puede saltar
                            }                            
							rects[iFile]=lyr.getFullExtent();
							
							// TODO: Poner una variable y dibujar solo cuando
							// todas las capas hayan sido cargadas.
							// TODO Se deberá de redibujar mediante la captura de los eventos, por
							//eso se comenta la parte anterior
							//							theView.getMapControl().drawMap();
							//							theView.getTOC().refresh();
						}

					} catch (DriverException e) {
						NotificationManager.addError("Error al crear la capa", e);
					}
				}

				//Esto permite que cuando se cargan varias capas de golpe y la vista esté vacia,se ponga como extent la suma de todos sus extents.
				if (rects.length > 1) {
					Rectangle2D rect = new Rectangle2D.Double();
					rect.setRect(rects[0]);

					if (first) {
						for (int i = 0; i < rects.length; i++) {
							rect.add(rects[i]);
						}

						mapControl.getMapContext().getViewPort()
							   .setExtent(rect);
					}
				}
				mapControl.getMapContext()
				   .endAtomicEvent();
				return true;

			} else if (fopen.getSelectedTab() instanceof WizardPanel) {
				WizardPanel wp = (WizardPanel) fopen.getSelectedTab();
				wp.setMapCtrl(mapControl);
				wp.execute();
				lyr = wp.getLayer();
				
				if (lyr != null) {
					lyr.setVisible(true);
					mapControl.getMapContext().beginAtomicEvent();
                    checkProjection(lyr, mapControl.getViewPort());
					mapControl.getMapContext().getLayers()
						   .addLayer(lyr);
					mapControl.getMapContext().endAtomicEvent();
					return true;
                    
				}
			} else {
				JOptionPane.showMessageDialog((Component)PluginServices.getMainFrame(),"Ninguna capa seleccionada");
			}
		}
		return false;
	}
}
