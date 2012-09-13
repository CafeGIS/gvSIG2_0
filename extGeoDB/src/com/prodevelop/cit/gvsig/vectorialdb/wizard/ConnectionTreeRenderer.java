package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.iver.andami.PluginServices;

public class ConnectionTreeRenderer extends DefaultTreeCellRenderer {

//	private ImageIcon connImage = new ImageIcon(createResourceUrl("images/conn.png"));
	private ImageIcon connImage = PluginServices.getIconTheme().get("conn-image");
//	private ImageIcon disconnImage = new ImageIcon(createResourceUrl("images/disconn.png"));
	private ImageIcon disconnImage = PluginServices.getIconTheme().get("disconn-image");

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		if (value instanceof VectorialDBConnectionTreeLeaf) {
			VectorialDBConnectionTreeLeaf jdbc = (VectorialDBConnectionTreeLeaf) value;
			if (jdbc.getConnectionWithParams()!=null) {
				setLeafIcon(connImage);
			} else {
				setLeafIcon(disconnImage);
			}
		}

		return super.getTreeCellRendererComponent(
					tree,
					value,
					selected,
					expanded,
					leaf,
					row,
					hasFocus);
	}

    private java.net.URL createResourceUrl(String path) {
        return getClass().getClassLoader().getResource(path);
    }

}
