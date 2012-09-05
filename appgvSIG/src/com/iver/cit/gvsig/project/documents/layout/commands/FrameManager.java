package com.iver.cit.gvsig.project.documents.layout.commands;

import java.util.ArrayList;
import java.util.BitSet;

import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameGroup;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrameViewDependence;

public class FrameManager {
	private ArrayList<IFFrame> fframes = new ArrayList<IFFrame>();
    private BitSet invalidates = new BitSet();
    private int numAdd = 0;
    /**
     * Returns from an index the FFrame.
     *
     * @param index
     *
     * @return FFrame.
     */
    public IFFrame getFFrame(int index) {
        return fframes.get(index);
    }

    /**
     * Returns the number of FFrame.
     *
     * @return Number of FFrames
     */
    public int getFFrameCount() {
        return fframes.size();
    }

    /**
     * Add a FFrame in the mechanism of control creating a command.
     *
     * @param f FFrame to add
     */
//    public void addFFrame(IFFrame f) {
//        int virtualIndex = doAddFFrame(f);
//        AbstractCommand command=new InsertFrameCommand(this, f, virtualIndex);
//        command.setDescription(f.getNameFFrame());
//        if (complex) {
//            commands.add(command);
//        } else {
//            cr.add(command);
//            PluginServices.getMainFrame().enableControls();
//        }
//    }
    /**
     * Remove the FFrame by the index.
     *
     * @param index
     */
//    public void removeFFrame(int index) {
//        if (invalidates.get(index)) {
//            return;
//        }
//        IFFrame frame=getFFrame(index);
//        doRemoveFFrame(index);
//        AbstractCommand command=new DeleteFrameCommand(this, index);
//        command.setDescription(frame.getNameFFrame());
//        if (complex) {
//            commands.add(command);
//        } else {
//            cr.add(command);
//            PluginServices.getMainFrame().enableControls();
//        }
//    }

    /**
     * Modify a fframe to another fframe new.
     *
     * @param fant Previous Fframe.
     * @param fnew New FFrame.
     */
//    public boolean modifyFFrame(IFFrame fant, IFFrame fnew) {
//        int posAnt = -1;
//
//        for (int i = 0; i < fframes.size(); i++) {
//            if (fframes.get(i).equals(fant) && !invalidates.get(i)) {
//            	posAnt = i;
//            }
//        }
//        if (posAnt==-1)
//        	return false;
//        int pos = doModifyFFrame(posAnt, fnew);
//        AbstractCommand command=new UpdateFrameCommand(this, fnew, posAnt, pos);
//        command.setDescription(fant.getNameFFrame());
//        if (complex) {
//            commands.add(command);
//        } else {
//            cr.add(command);
//            PluginServices.getMainFrame().enableControls();
//        }
//
//        refreshDependences(fant, fnew);
//        return true;
//    }
    /**
     * Undo add FFrame from index.
     *
     * @param index
     */
    public void undoAddFFrame(int index) {
        doRemoveFFrame(index);
        numAdd--;
    }

    /**
     * Add FFrame.
     *
     * @param frame
     *
     * @return index of new fframe.
     */
    public int doAddFFrame(IFFrame frame) {
        fframes.add(frame);
        numAdd++;

        return fframes.size() - 1;
    }

    /**
     * Add FFrame from index.
     *
     * @param frame New FFrame.
     * @param index Index of new FFrame.
     */
    public void doAddFFrame(IFFrame frame, int index) {
        invalidates.set(index, false);
        fframes.set(index, frame);
        numAdd++;
    }

    /**
     * Undo modify an FFrame modified.
     *
     * @param fant Previous fframe.
     * @param fnew New FFrame.
     * @param indexAnt Actual index.
     * @param indexLast Previous index.
     */
    public void undoModifyFFrame(int index,
        int newIndex) {
    	undoRemoveFFrame(index);
		undoAddFFrame(newIndex);
		IFFrame fant=fframes.get(newIndex);
		IFFrame fnew=fframes.get(index);
		refreshDependences(fant,fnew);
    }

    /**
     * Modify FFrame from index and new FFrame.
     *
     * @param indexAnt Actual index.
     * @param frameNext New FFrame.
     *
     * @return New index of FFrame.
     */
    public int doModifyFFrame(int indexAnt, IFFrame frameNext) {
        doRemoveFFrame(indexAnt);

        return doAddFFrame(frameNext);
    }

    /**
     * Undo Remove FFrame from index.
     *
     * @param index Actual index of FFrame.
     */
    public void undoRemoveFFrame(int index) {
        invalidates.set(index, false);
    }

    /**
     * Remove FFrame from actual index.
     *
     * @param index Actual index.
     */
    public void doRemoveFFrame(int index) {
        invalidates.set(index, true);
    }
    /**
     * Returns all the fframes that are not removed.
     *
     * @return Vector with fframes.
     */
    public IFFrame[] getFFrames() {
        ArrayList<IFFrame> frames = new ArrayList<IFFrame>();

        for (int i = 0; i < getFFrameCount(); i++) {
            if (!invalidates.get(i)) {
                frames.add(fframes.get(i));
            }
        }

        IFFrame[] fframesV = frames.toArray(new IFFrame[0]);

        return fframesV;
    }

    /**
     * Returns all the fframes, remove and done not remove.
     *
     * @return All FFrames.
     */
    public IFFrame[] getAllFFrames() {
        return fframes.toArray(new IFFrame[0]);
    }

    /**
     * Returns all the fframes, remove and done not remove and the ones that are in the FFrameGroups
     *
     * @return All FFrames.
     */
    public IFFrame[] getAllFFrameToDepends() {
        IFFrame[] fs = getAllFFrames();

        return getFFrameToDepends(fs);
    }

    private IFFrame[] getFFrameToDepends(IFFrame[] fs) {
        ArrayList<IFFrame> result = new ArrayList<IFFrame>();

        for (int i = 0; i < fs.length; i++) {
            result.add(fs[i]);

            if (fs[i] instanceof FFrameGroup) {
                IFFrame[] ffs = getFFrameToDepends(((FFrameGroup) fs[i]).getFFrames());

                for (int j = 0; j < ffs.length; j++) {
                    result.add(ffs[j]);
                }
            }
        }

        return result.toArray(new IFFrame[0]);
    }

    public void refreshDependences(IFFrame fant, IFFrame fnew) {
        for (int i = 0; i < fframes.size(); i++) {
            if (fframes.get(i) instanceof IFFrameViewDependence) {
                IFFrameViewDependence f = (IFFrameViewDependence) fframes.get(i);
                f.refreshDependence(fant, fnew);
            }
        }
    }
    public void redoModifyFFrame(int index, int newIndex, IFFrame frame) {
		doRemoveFFrame(index);
		doAddFFrame(frame,newIndex);
		IFFrame fant=fframes.get(newIndex);
		refreshDependences(fant,frame);
	}
    public BitSet invalidates(){
    	return invalidates;
    }

	public void clear() {
		// TODO Auto-generated method stub

	}
}
