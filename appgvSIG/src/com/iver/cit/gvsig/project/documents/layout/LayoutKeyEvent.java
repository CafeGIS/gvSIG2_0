package com.iver.cit.gvsig.project.documents.layout;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import org.gvsig.tools.undo.RedoException;
import org.gvsig.tools.undo.UndoException;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;

public class LayoutKeyEvent implements KeyEventDispatcher{
	private static IFFrame[] selectFFrames=new IFFrame[0];
    private int difX;
    private int difY;

	public static boolean copy(Layout layout) {
		IFFrame[] fframes = layout.getLayoutContext().getFFrameSelected();
		if (fframes.length==0)
			return false;
		selectFFrames = new IFFrame[fframes.length];
		for (int i = 0; i < fframes.length; i++) {
			selectFFrames[i] = fframes[i].cloneFFrame(layout);
		}
		return true;
	}
	public static boolean cut(Layout layout) {
		IFFrame[] fframes = layout.getLayoutContext().getFFrameSelected();
		if (fframes.length==0)
			return false;
		selectFFrames = new IFFrame[fframes.length];
		for (int i = 0; i < fframes.length; i++) {
			selectFFrames[i] = fframes[i].cloneFFrame(layout);
		}
		layout.getLayoutContext().delFFrameSelected();
		layout.getLayoutContext().callLayoutDrawListeners();
		return true;
	}
	public static boolean paste(Layout layout) {
		IFFrame copyFFrame = null;
		layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(layout, "paste_elements"));
		for (int i = 0; i < selectFFrames.length; i++) {
			copyFFrame = selectFFrames[i].cloneFFrame(layout);
			if (i == 0)
				layout.getLayoutContext().addFFrame(copyFFrame, true, true);
			else
				layout.getLayoutContext().addFFrame(copyFFrame, false, true);
		}
		layout.getLayoutContext().getFrameCommandsRecord().endComplex();
		layout.getLayoutContext().callLayoutDrawListeners();
		return true;
	}
	public static boolean undo(Layout layout) {
		LayoutContext lcontext=layout.getLayoutContext();
		try {
			lcontext.getFrameCommandsRecord().undo();
		} catch (UndoException e) {
			e.printStackTrace();
		}
		layout.getLayoutContext().updateFFrames();
		layout.getLayoutContext().callLayoutDrawListeners();
		PluginServices.getMainFrame().enableControls();
		return true;
	}
	public static boolean redo(Layout layout) {
		LayoutContext lcontext=layout.getLayoutContext();
		try {
			lcontext.getFrameCommandsRecord().redo();
		} catch (RedoException e) {
			e.printStackTrace();
		}
		lcontext.updateFFrames();
		lcontext.callLayoutDrawListeners();
		PluginServices.getMainFrame().enableControls();
		return true;
	}

    public boolean dispatchKeyEvent(KeyEvent e) {
		IWindow view = PluginServices.getMDIManager().getActiveWindow();

		if (e.getID() == KeyEvent.KEY_PRESSED || !(view instanceof Layout) || !(e.getSource() instanceof LayoutControl))
			return false;
		Layout layout=(Layout)view;
		if (!layout.getLayoutContext().isEditable())
			return false;
		IFFrame[] fframes = layout.getLayoutContext().getFFrameSelected();
		int dif = 10;
		if (e.getModifiers() == KeyEvent.CTRL_MASK) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_C:
				copy(layout);
				break;
			case KeyEvent.VK_X:
				cut(layout);
				break;
			case KeyEvent.VK_V:
				paste(layout);
				break;
			case KeyEvent.VK_Z:
				undo(layout);
				break;
			case KeyEvent.VK_Y:
				redo(layout);
				break;
			}
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				difX = difX - dif;
				break;

			case KeyEvent.VK_RIGHT:
				difX = difX + dif;
				break;
			case KeyEvent.VK_UP:
				difY = difY - dif;
				break;
			case KeyEvent.VK_DOWN:
				difY = difY + dif;
				break;
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				layout.getLayoutContext().delFFrameSelected();
				layout.getLayoutControl().refresh();
				break;
			}
			if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_DOWN) {
				if (fframes.length==0)
					return false;
				layout.getLayoutContext().getFrameCommandsRecord().startComplex(PluginServices.getText(this, "move_elements"));
			for (int i = 0; i < fframes.length; i++) {
				IFFrame fframeAux = fframes[i].cloneFFrame(layout);
				Rectangle2D r = getRectMove(fframes[i]
						.getBoundingBox(layout.getLayoutControl().getAT()), difX, difY);
				fframeAux.setBoundBox(FLayoutUtilities.toSheetRect(r,
						layout.getLayoutControl().getAT()));
				layout.getLayoutContext().getFrameCommandsRecord().update(fframes[i], fframeAux);
			}
			layout.getLayoutContext().getFrameCommandsRecord().endComplex();
			layout.getLayoutContext().updateFFrames();
			layout.getLayoutControl().refresh();
			}
			difX = 0;
			difY = 0;

		}
		return true;
	}
	private Rectangle2D getRectMove(Rectangle2D r,int difX,int difY) {
		Rectangle2D rectMove=new Rectangle2D.Double();
		rectMove.setRect(r.getX()+difX,r.getY()+difY,r.getWidth(),r.getHeight());
		return rectMove;
	}
	public static boolean hasSelection() {
		return selectFFrames.length>0;
	}

}
