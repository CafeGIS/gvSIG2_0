package com.iver.cit.gvsig.project.documents.layout;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.commands.FrameCommandsRecord;
import com.iver.cit.gvsig.project.documents.layout.commands.FrameManager;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameUseFMap;
import com.iver.cit.gvsig.project.documents.layout.fframes.gui.dialogs.IFFrameDialog;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.XMLException;

/**
 * Model of LayoutControl.
 *
 * @author Vicente Caballero Navarro
 */
public class LayoutContext {
	private Attributes m_attributes = null;
	private IFFrame[] fframes;
	private FrameCommandsRecord fcr;
	public static Hashtable nums = new Hashtable();
	public int numBefore = 0;
    public int numBehind = 0;
    private boolean isEditable = true;
    private Boolean adjustToGrid = null;
    private Boolean m_showRuler;
    private Boolean isGridVisible = null;
	private ArrayList layoutDrawListeners=new ArrayList();
	private AffineTransform m_MatrizTransf;
	/**
     * Create a new object of LayoutContext.
     */
	public LayoutContext() {
		m_attributes=new Attributes();
		m_MatrizTransf = new AffineTransform();
	    m_MatrizTransf.setToIdentity();
	    FrameManager fm=new FrameManager();
		fcr = new FrameCommandsRecord(fm);
	}
	/**
	 * Returns the AffineTransform that is applying in the Layout.
	 *
	 * @return AffineTransform
	 */
	public AffineTransform getAT() {
		return m_MatrizTransf;
	}
	/**
	 * Add Listener to draw Layout.
	 * @param ldl LayoutDrawListener.
	 */
	public void addLayoutDrawListener(LayoutDrawListener ldl) {
		if (!layoutDrawListeners.contains(ldl)) {
			layoutDrawListeners.add(ldl);
		}
	}
	/**
	 * Call all LayoutDrawListeners.
	 *
	 */
	public void callLayoutDrawListeners() {
		for (int i=0;i<layoutDrawListeners.size();i++) {
			((LayoutDrawListener)layoutDrawListeners.get(i)).refresh();
		}
	}
	/**
	 * It returns the print attributes of the Layout.
	 *
	 * @return Attributes.
	 */
    public Attributes getAttributes() {
        return m_attributes;
    }
    /**
     * Inserts the print attributes of Layout.
     * @param attributes Attributes.
     */
	public void setAtributes(Attributes attributes) {
		m_attributes=attributes;
	}
	/**
	 * It obtains the Array with all the FFrames that have been added al Layout.
	 *
	 * @return Array with all the FFrames that have been added al Layout.
	 */
    public IFFrame[] getFFrames() {
        return fframes;
    }
    /**
	 * It obtains the FFrame from an index.
	**/
    public IFFrame getFFrame(int i) {
        return fframes[i];
    }
    /**
	 * It orders the FFrames depending on its position specified by level.
	 *
	 */
    public void updateFFrames() {
        ArrayList frames = new ArrayList();
        IFFrame[] auxfframes = fcr.getFrameManager().getFFrames();
        for (int j = numBehind; j <= numBefore; j++) {
            for (int i = 0; i < auxfframes.length; i++) {
                if (auxfframes[i].getLevel() == j) {
                    frames.add(auxfframes[i]);
                    continue;
                }
            }
        }
        fframes = (IFFrame[]) frames.toArray(new IFFrame[0]);
    }
    /**
	 * Remove the fframes selected.
	 *
	 */
    public void delFFrameSelected() {
        fcr.startComplex(PluginServices.getText(this,"remove_elements"));
        for (int i = fcr.getFrameManager().getAllFFrames().length - 1; i >= 0; i--) {
            IFFrame fframe = fcr.getFrameManager().getFFrame(i);

            if (fframe.getSelected() != IFFrame.NOSELECT) {
                fcr.delete(fframe);
            }
        }
        fcr.endComplex();
        updateFFrames();
    }
    /**
	 * Clear the selection of FFrames.
	 *
	 */
    public void clearSelection() {
        for (int i = fcr.getFrameManager().getAllFFrames().length - 1; i >= 0; i--) {
            IFFrame fframe = fcr.getFrameManager().getFFrame(i);
            if (fframe.getSelected() != IFFrame.NOSELECT) {
                fframe.setSelected(false);
            }
        }
    }
    /**
	 * Remove the fframe of index.
	 *
	 */
    public void delFFrame(int index) {
        for (int i = 0; i < fcr.getFrameManager().getAllFFrames().length; i++) {
            IFFrame frame=getFFrame(index);
        	if (fcr.getFrameManager().getFFrame(i).equals(frame)) {
                fcr.delete(frame);
            }
        }
        updateFFrames();
    }
    /**
	 * Remove the fframe of parameter.
	 * @param frame
	 *            FFrame to remove.
	 */
    public void delFFrame(IFFrame frame) {
        for (int i = 0; i < fcr.getFrameManager().getAllFFrames().length; i++) {
            if (fcr.getFrameManager().getFFrame(i).equals(frame)) {
                fcr.delete(frame);
            }
        }
        updateFFrames();
    }
    /**
	 * Returns the EditableFeatureSource, is the control of all change in the FFrames of Layout.
	 * @return EditableFatureSource.
	 */
    public FrameCommandsRecord getFrameCommandsRecord() {
        return fcr;
    }
    /**
	 * It adds a fframe to Arraylist of FFrames .
	 *
	 * @param frame fframe to add.
	 * @param clearSelection True
	 * 			 True if clean the selection of the fframes already added and
	 * 			 false if intends to maintain the same selection.
	 * @param select
	 *            Boolean that indicates if has to remain selected the FFrame that is added or not.
	 */
    public void addFFrame(IFFrame frame, boolean clearSelection, boolean select) {
        IFFrame[] fframes = getFFrames();
        if (clearSelection) {
            for (int i = fframes.length - 1; i >= 0; i--) {
                IFFrame fframe1 = fframes[i];
                fframe1.setSelected(false);
            }
        }

        if (nums.containsKey(frame.getClass())) {
            nums.put(frame.getClass(), new Integer(Integer.parseInt(nums.get(
                    frame.getClass()).toString()) + 1));
        } else {
            nums.put(frame.getClass(), new Integer(0));
        }

        frame.setNum(Integer.parseInt(nums.get(frame.getClass()).toString()));
        fcr.insert(frame);
        frame.setSelected(select);
        frame.setLevel(getNumBefore());
        updateFFrames();
    }
    /**
	 * It adds a fframe to Arraylist of FFrames with the same properties.
	 *
	 * @param frame fframe to add.
	 */
	public void addFFrameSameProperties(IFFrame frame){
		fcr.insert(frame);
		frame.setSelected(true);
		frame.setLevel(getNumBefore());
		updateFFrames();
	}
    /**
	 * Returns other number behind the current fframes.
	 * @return new Position behind.
	 */
    public int getNumBehind() {
        return --numBehind;
    }
    /**
	 * Returns other number before the current fframes.
	 * @return new Position before.
	 */
    public int getNumBefore() {
        return ++numBefore;
    }
    /**
	 * It returns all the fframes included them erased and modified in all
	 * its previous forms.
	 * @return IFFrame[] Array of FFrames.
	 */
    public IFFrame[] getAllFFrames() {
        ArrayList all = new ArrayList();
        return (IFFrame[]) allFFrames(getFFrames(), all)
                .toArray(new IFFrame[0]);
    }
    private ArrayList allFFrames(IFFrame[] fframes, ArrayList all) {
        for (int i = 0; i < fframes.length; i++) {
            if (fframes[i] instanceof FFrameGroup) {
            	ArrayList groupFrames=allFFrames(((FFrameGroup) fframes[i]).getFFrames(), all);
            	if (!all.containsAll(groupFrames)) {
					all.addAll(groupFrames);
				}

            }else {
            	if (!all.contains(fframes[i])) {
					all.add(fframes[i]);
				}
            }
        }
        return all;
    }
    /**
	 * It returns an array with the FFrames selected.
	 *
	 * @return Array with the FFrames selected.
	 */
    public IFFrame[] getFFrameSelected() {
        ArrayList selecList = new ArrayList();
        IFFrame[] fframes=getFFrames();
        for (int i = fframes.length - 1; i >= 0; i--) {
            IFFrame fframe = fframes[i];

            if (fframe.getSelected() != IFFrame.NOSELECT) {
                selecList.add(fframe);
            }
        }

        return (IFFrame[]) selecList.toArray(new IFFrame[0]);
    }
    /**
	 * It returns if the Layout is in edition.
	 * @return True if Layout is in edition.
	 */
    public boolean isEditable() {
        return isEditable;
    }
    /**
	 * It inserts if the Layout is in edition.
	 * @param b
	 *            True if Layout is in edition.
	 */
    public void setEditable(boolean b) {
        if (!b) {
            clearSelection();
            //layoutControl.setTool("layoutzoomin");
            PluginServices.getMainFrame().setSelectedTool("ZOOM_IN");
        }
        isEditable = b;

    }
    /**
	 * It returns if has been applying in the fframes that are added to Layout the grid, or not.
	 *
	 * @return true if has been applying the grid.
	 */
    public boolean isAdjustingToGrid() {
        if (adjustToGrid == null) {
            adjustToGrid = new Boolean(Layout.getDefaultAdjustToGrid());
        }
        return adjustToGrid.booleanValue();
    }
    /**
	 * It inserts if has been applying in the fframes that are added to Layout the grid, or not.
	 *
	 * @param b
	 *            true  if has been applying the grid.
	 */
    public void setAdjustToGrid(boolean b) {
        adjustToGrid = new Boolean(b);
    }
    /**
	 * It returns an Object XMLEntity with the information the necessary attributes
	 * to be able later to create again the original object.
	 *
	 * @return XMLEntity.
	 *
	 * @throws XMLException
	 */
    public XMLEntity getXMLEntity() {
        XMLEntity xml = new XMLEntity();
        xml.putProperty("className", this.getClass().getName());
        xml.setName("layout");
        xml.putProperty("isCuadricula", isAdjustingToGrid());
//        xml.putProperty("m_name", this.getName());
        xml.putProperty("isEditable", isEditable());
        xml.putProperty("numBehind", numBehind);
        xml.putProperty("numBefore", numBefore);
        xml.addChild(getAttributes().getXMLEntity());
        IFFrame[] fframes=getFFrames();
        for (int i = 0; i < fframes.length; i++) {
            try {
                XMLEntity xmlAux = fframes[i].getXMLEntity();
                xml.addChild(xmlAux);
            } catch (SaveException e) {
                e.showError();
            }
        }
        return xml;
    }
    /**
	 * It inserts if is shown or not the rule of the Layout.
	 *
	 * @param b
	 *            True if is shown or not the rule of the Layout.
	 */
    public void setRuler(boolean b) {
        m_showRuler = new Boolean(b);
    }

    /**
	 * Returns if is shown or not the rule of the Layout.
	 *
	 * @return True si se muestra la regla.
	 */
    public boolean getRuler() {
        if (m_showRuler == null) {
            m_showRuler = new Boolean(Layout.getDefaultShowRulers());
        }
        return m_showRuler.booleanValue();
    }





    /**
	 * It returns if has been showing the grid of Layout, or not.
	 *
	 * @return true if has been showing the grid of Layout.
	 */
    public boolean isGridVisible() {
        if (isGridVisible== null) {
            isGridVisible = new Boolean(Layout.getDefaultShowGrid());
        }
        return isGridVisible.booleanValue();
    }
    /**
	 * It inserts if draws the Grid in the Layout or not.
	 *
	 * @param b
	 *            True if draws the Grid in the Layout.
	 */
    public void setGridVisible(boolean b) {
        isGridVisible = new Boolean(b);
    }
    /**
	 * The dialogs are created here each time that are needed.
	 *
	 * @param fframe
	 *            Rectangle that represents the place that occupied the element added.
	 *
	 * @return IFFrame Returns the FFrame added or null if the fframe has not been added.
	 */
	public IFFrame openFFrameDialog(IFFrame frame) {
		IFFrameDialog fframedialog = frame.getPropertyDialog();
		if (fframedialog != null) {
			fframedialog.setRectangle(frame.getBoundingBox(getAT()));
			PluginServices.getMDIManager().addWindow(fframedialog);
		}
		return fframedialog.getFFrame();
	}
	/**
	 * Refresh all FFrames of Layout.
	 */
	public void fullRefresh() {
		IFFrame[] fframes = getFFrames();
		for (int i = 0; i < fframes.length; i++) {
			if (fframes[i] instanceof IFFrameUseFMap) {
				IFFrameUseFMap fframe = (IFFrameUseFMap) fframes[i];
				fframe.refresh();
			}
		}
		callLayoutDrawListeners();
	}

}
