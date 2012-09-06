package org.gvsig.gui.beans.propertiespanel;

import java.util.EventListener;
import java.util.EventObject;

public interface PropertiesComponentListener extends EventListener {
	public void actionChangeProperties(EventObject e);
}