package com.iver.cit.gvsig.gui.preferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.gvsig.fmap.mapcontrol.MapControl;
import org.gvsig.fmap.mapcontrol.tools.snapping.snappers.ISnapper;
import org.gvsig.tools.ToolsLocator;
import org.gvsig.tools.extensionpoint.ExtensionPoint;
import org.gvsig.tools.extensionpoint.ExtensionPointManager;
import org.gvsig.tools.extensionpoint.ExtensionPoint.Extension;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toolListeners.snapping.gui.SnapConfig;
import com.iver.utiles.extensionPointsOld.ExtensionPoints;
import com.iver.utiles.extensionPointsOld.ExtensionPointsSingleton;


/**
 * Preferencias de snapping.
 *
 * @author Vicente Caballero Navarro
 */
public class SnapConfigPage extends AbstractPreferencePage {
    private static Preferences prefs = Preferences.userRoot().node("snappers");
    private ImageIcon icon;
    private SnapConfig snapConfig;
    private ArrayList<ISnapper> snappers = new ArrayList<ISnapper>();
	private static boolean applySnappers=true;
    @SuppressWarnings("unchecked")
//	private static HashMap<ISnapper,Boolean> selected = new HashMap<ISnapper,Boolean>();
    private MapControl mc=null;

//    static {
//    	new SnapConfigPage().initializeValues();
//    }


    public SnapConfigPage() {
        super();
        icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/Snapper.png"));
        IWindow window=PluginServices.getMDIManager().getActiveWindow();
        if (window instanceof View){
        	mc=((View)window).getMapControl();
        	 snapConfig = new SnapConfig(mc);
             snappers = getSnappers();
             snapConfig.setSnappers(snappers);
             addComponent(snapConfig);
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static ArrayList<ISnapper> getSnappers() {
        ArrayList<ISnapper> snappers = new ArrayList<ISnapper>();
        ExtensionPointManager extensionPoints =ToolsLocator.getExtensionPointManager();

        ExtensionPoint ep = extensionPoints.get("Snapper");

//        ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();

//        ExtensionPoint extensionPoint = (ExtensionPoint) extensionPoints.get(
//                "Snapper");
        Iterator iterator = ep.iterator();

        while (iterator.hasNext()) {
            try {
            	Extension obj= (Extension)iterator.next();
                ISnapper snapper = (ISnapper) ep.create(obj.getName());
                snappers.add(snapper);
            } catch (InstantiationException e) {
            	NotificationManager.addError(e.getMessage(),e);
            } catch (IllegalAccessException e) {
            	NotificationManager.addError(e.getMessage(),e);
            }
        }

        return snappers;
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList<ISnapper> getActivesSnappers() {
       if (!applySnappers)
    	   return new ArrayList<ISnapper>();
       return new ArrayList<ISnapper>(mc.getSelectedSnapppers().keySet());
    }

    /**
     * DOCUMENT ME!
     *
     * @throws StoreException DOCUMENT ME!
     */
    public void storeValues() throws StoreException {
    	mc.getSelectedSnapppers().clear();
    	for (int n = 0; n < snappers.size(); n++) {
            Boolean b = (Boolean) snapConfig.getTableModel().getValueAt(n, 0);
            ISnapper snp = (ISnapper) snappers.get(n);
            String nameClass=snp.getClass().getName();
            nameClass=nameClass.substring(nameClass.lastIndexOf('.'));
            prefs.putBoolean("snapper_activated" + nameClass, b.booleanValue());
            if (b.booleanValue())
            	mc.getSelectedSnapppers().put(snp, b);
            prefs.putInt("snapper_priority"+ nameClass,snp.getPriority());
        }
        boolean b=snapConfig.applySnappers();
        prefs.putBoolean("apply-snappers",b);
        applySnappers=b;
    }

    /**
     * DOCUMENT ME!
     */
    public void setChangesApplied() {
    	setChanged(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getID() {
        return this.getClass().getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return PluginServices.getText(this, "Snapping");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPanel getPanel() {
        return this;
    }

    /**
     * DOCUMENT ME!
     */
    public void initializeValues() {
    	if (prefs.get("apply-snappers", null) == null){
    		initializeDefaults();
    	}
    	for (int n = 0; n < snappers.size(); n++) {
            ISnapper snp = (ISnapper) snappers.get(n);
            String nameClass=snp.getClass().getName();
            nameClass=nameClass.substring(nameClass.lastIndexOf('.'));
            boolean select = prefs.getBoolean("snapper_activated" + nameClass, false);
            if (select)
            	mc.getSelectedSnapppers().put(snp, new Boolean(select));
            int priority = prefs.getInt("snapper_priority" + nameClass,3);
            snp.setPriority(priority);
        }
        applySnappers = prefs.getBoolean("apply-snappers",true);
        snapConfig.setApplySnappers(applySnappers);
        snapConfig.selectSnappers(mc.getSelectedSnapppers());
        mc.getMapContext().setSnappers(new ArrayList(mc.getSelectedSnapppers().keySet()));

    }

    /**
     * DOCUMENT ME!
     */
    public void initializeDefaults() {
    	for (int n = 0; n < snappers.size(); n++) {
            ISnapper snp = (ISnapper) snappers.get(n);
            String nameClass=snp.getClass().getName();
            nameClass=nameClass.substring(nameClass.lastIndexOf('.'));
            if (nameClass.equals(".FinalPointSnapper")){
            	int priority = 1;
            	prefs.putInt("snapper_priority" + nameClass, priority);
            	mc.getSelectedSnapppers().put(snp, new Boolean(true));
            	snp.setPriority(priority);
            }else if (nameClass.equals(".NearestPointSnapper")){
            	int priority = 2;
            	prefs.putInt("snapper_priority" + nameClass, priority);
            	mc.getSelectedSnapppers().put(snp, new Boolean(true));
            	snp.setPriority(priority);
            }else{
            	int priority = 3;
            	prefs.putInt("snapper_priority" + nameClass, priority);
            	mc.getSelectedSnapppers().put(snp, new Boolean(false));
            	snp.setPriority(priority);
            }
        }
        applySnappers = true;
        snapConfig.setApplySnappers(applySnappers);
        snapConfig.selectSnappers(mc.getSelectedSnapppers());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isValueChanged() {
    	return super.hasChanged();
    }
}
