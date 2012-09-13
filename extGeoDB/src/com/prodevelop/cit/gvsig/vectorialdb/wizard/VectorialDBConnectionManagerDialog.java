/* gvSIG. Sistema de Información Geográfica de la Generalitat Valenciana
 *
 * Copyright (C) 2006 Prodevelop and Generalitat Valenciana.
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
 *   Generalitat Valenciana
 *   Conselleria d'Infraestructures i Transport
 *   Av. Blasco Ibáñez, 50
 *   46010 VALENCIA
 *   SPAIN
 *
 *   +34 963862235
 *   gvsig@gva.es
 *   www.gvsig.gva.es
 *
 *    or
 *
 *   Prodevelop Integración de Tecnologías SL
 *   Conde Salvatierra de Álava , 34-10
 *   46004 Valencia
 *   Spain
 *
 *   +34 963 510 612
 *   +34 963 510 968
 *   gis@prodevelop.es
 *   http://www.prodevelop.es
 */
package com.prodevelop.cit.gvsig.vectorialdb.wizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.apache.log4j.Logger;
import org.gvsig.fmap.dal.DataServerExplorerParameters;
import org.gvsig.fmap.dal.serverexplorer.db.DBServerExplorerParameters;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.utiles.swing.JPasswordDlg;


/**
 * Single connection manager main dialog. Lists available connections. Open connections
 * are marked "[C]" before their names.
 *
 * @author jldominguez
 *
 */
public class VectorialDBConnectionManagerDialog extends JPanel implements IWindow,
    ActionListener {
    private static Logger logger = Logger.getLogger(VectorialDBConnectionManagerDialog.class.getName());
    private JScrollPane connectionsScrollPane = null;
    private JButton closeButton = null;
    private JButton removeButton = null;
    private JButton newButton = null;
    private JTree connectionsTree = null;
    private WindowInfo winfo = new WindowInfo(8); // all false except MODAL
    private JButton connectButton = null;
    private JButton disconnButton = null;
    private JButton editButton = null;
	private TreeCellRenderer connTreeRenderer;

    /**
     * This method initializes
     *
     */
    public VectorialDBConnectionManagerDialog() {
        super();
        initialize();
    }

    public void showDialog() {
        PluginServices.getMDIManager().addWindow(this);
    }

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        winfo.setHeight(422 - 42);
        winfo.setWidth(347 + 6);
        winfo.setTitle(PluginServices.getText(this, "gestor_db"));

        this.setSize(new java.awt.Dimension(347, 420));
        this.setLayout(null);
        this.add(getConnectionsScrollPane(), null);
        this.add(getCloseButton(), null);
        this.add(getRemoveButton(), null);
        this.add(getNewButton(), null);
        this.add(getConnectButton(), null);
        this.add(getDisconnButton(), null);
        this.add(getEditButton(), null);

        connTreeRenderer = new ConnectionTreeRenderer();
        refreshTree();
    }

    public WindowInfo getWindowInfo() {
        return winfo;
    }

    /**
     * This method initializes connectionsScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getConnectionsScrollPane() {
        if (connectionsScrollPane == null) {
            connectionsScrollPane = new JScrollPane();
            connectionsScrollPane.setBounds(new java.awt.Rectangle(5, 5, 336,
                    336));
            connectionsScrollPane.setViewportView(getConnectionsTree());
        }

        return connectionsScrollPane;
    }

    /**
     * This method initializes closeButton
     *
     * @return javax.swing.JButton
     */
    private JButton getCloseButton() {
        if (closeButton == null) {
            closeButton = new JButton();
            closeButton.addActionListener(this);
            closeButton.setBounds(new java.awt.Rectangle(230, 380, 106, 26));
            closeButton.setText(PluginServices.getText(this, "aceptar"));
        }

        return closeButton;
    }

    /**
     * This method initializes removeButton
     *
     * @return javax.swing.JButton
     */
    private JButton getRemoveButton() {
        if (removeButton == null) {
            removeButton = new JButton();
            removeButton.addActionListener(this);
            removeButton.setBounds(new java.awt.Rectangle(120, 350, 106, 26));
            removeButton.setText(PluginServices.getText(this, "remove"));
        }

        return removeButton;
    }

    /**
     * This method initializes newButton
     *
     * @return javax.swing.JButton
     */
    private JButton getNewButton() {
        if (newButton == null) {
            newButton = new JButton();
            newButton.addActionListener(this);
            newButton.setBounds(new java.awt.Rectangle(10, 350, 106, 26));
            newButton.setText(PluginServices.getText(this, "add"));
        }

        return newButton;
    }

    /**
     * This method initializes connectionsTree
     *
     * @return javax.swing.JTree
     */
    private JTree getConnectionsTree() {

    	JTree resp = new JTree(getTreeRoot());
    	resp.setRowHeight(20);
    	resp.setCellRenderer(connTreeRenderer);
        return resp;

    }

    private DefaultMutableTreeNode getTreeRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.setUserObject(new String(PluginServices.getText(this,
                    "geodb_connections")));

        HashMap aux_nodes = new HashMap();

//        ConnectionWithParams[] conns = SingleVectorialDBConnectionManager.instance()
//                                                                  .getAllConnections();

//        if (conns == null) {
//            return root;
//        }

//        DataManager dm=DataManager.getManager();
//        String[] registers=dm.getRegistersExplorers();
//        for (int i = 0; i < registers.length; i++) {
//            String key = registers[i];
//
//            if (!aux_nodes.containsKey(key)) {
//                aux_nodes.put(key, new ArrayList());
//            }
//
//            ArrayList aux = (ArrayList) aux_nodes.get(key);
//            aux.add(registers);
//        }
//
//        Iterator iter = aux_nodes.keySet().iterator();
//        while (iter.hasNext()) {
//            String k = (String) iter.next();
//            VectorialDBConnectionTreeNode node = new VectorialDBConnectionTreeNode(k);
//            ArrayList cc = (ArrayList) aux_nodes.get(k);
//
//            for (int i = 0; i < cc.size(); i++) {
//                DataServerExplorerParameters conwp=null;
//				for (int j=0;j<((String[])cc.get(i)).length;j++){
//					try {
//						conwp = (DataServerExplorerParameters) dm.createDataExplorerParameters(((String[])cc.get(i))[j]);
//						 VectorialDBConnectionTreeLeaf leaf = new VectorialDBConnectionTreeLeaf(conwp);
//			                node.add(leaf);
//					} catch (InitializeException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//
//            }
//
//            root.add(node);
//        }

        return root;
    }

    private void addConnection(String _drvName, String _port, String _host,
        String _dbName, String _user, String _pw, String _conn_usr_name,
        boolean is_conn) {
//        SingleVectorialDBConnectionManager.instance()
//                                   .getConnection(_drvName, _user, _pw,
//            _conn_usr_name, _host, _port, _dbName, is_conn);
        refreshTree();
    }

    private void refreshTree() {
        connectionsTree = getConnectionsTree();

        for (int i = 0; i < connectionsTree.getRowCount(); i++) {
            connectionsTree.expandRow(i);
        }

        connectionsScrollPane.setViewportView(connectionsTree);
    }

    public void actionPerformed(ActionEvent arg0) {
        Object src = arg0.getSource();

        if (src == disconnButton) {
            if (connectionsTree.getSelectionCount() != 1) {
                return;
            }

            Object obj = connectionsTree.getSelectionPath()
                                        .getLastPathComponent();

            if (!(obj instanceof VectorialDBConnectionTreeLeaf)) {
                return;
            }

            VectorialDBConnectionTreeLeaf leaf = (VectorialDBConnectionTreeLeaf) obj;
            DataServerExplorerParameters _cwp = leaf.getConnectionWithParams();

//            if (_cwp.isConnected()) {
//                _cwp.disconnect();
//                refreshTree();
//            }
        }

        if (src == connectButton) {
            if (connectionsTree.getSelectionCount() != 1) {
                return;
            }

            Object obj = connectionsTree.getSelectionPath()
                                        .getLastPathComponent();

            if (!(obj instanceof VectorialDBConnectionTreeLeaf)) {
                return;
            }

            VectorialDBConnectionTreeLeaf leaf = (VectorialDBConnectionTreeLeaf) obj;
            DBServerExplorerParameters _cwp = (DBServerExplorerParameters) leaf
					.getConnectionWithParams();

            JPasswordDlg dlg = new JPasswordDlg();
            dlg.setLocationRelativeTo((Component)PluginServices.getMainFrame());
            String strMessage = PluginServices.getText(this, "conectar_db");
            String strPassword = PluginServices.getText(this, "password");
            dlg.setMessage(strMessage + " [" + //_cwp.getTableName() + ", " +
					_cwp.getHost() + ", " + _cwp.getPort() + ", "
					+ _cwp.getDBName() +
                ", " + _cwp.getUser() + "]. " + strPassword + "?");
            dlg.setVisible(true);

            String clave = dlg.getPassword();

            if (clave == null) {
                return;
            }

//            try {
//                _cwp.connect(clave);
                refreshTree();
//            }
//            catch (DBException e) {
//                showConnectionErrorMessage(e.getMessage(), false);
//            }
        }

        if (src == closeButton) {
            PluginServices.getMDIManager().closeWindow(this);
        }

        if (src == newButton) {
            VectorialDBConnectionParamsDialog newco = new VectorialDBConnectionParamsDialog();
            newco.showDialog();

            if (newco.isOkPressed()) {
                String _drvname = newco.getConnectionDriverName();
                String _host = newco.getConnectionServerUrl();
                String _port = newco.getConnectionPort();
                String _dbname = newco.getConnectionDBName();
                String _user = newco.getConnectionUser();
                String _pw = newco.getConnectionPassword();
                String _conn_usr_name = newco.getConnectionName();

                boolean hasToBeCon = newco.hasToBeConnected();

//                try {
                    addConnection(_drvname, _port, _host, _dbname, _user, _pw,
                        _conn_usr_name, hasToBeCon);
//                }
//                catch (DBException e) {
//                    showConnectionErrorMessage(e.getMessage(), false);
//                }
            }
        }

        if (src == removeButton) {
            if (connectionsTree.getSelectionCount() != 1) {
                return;
            }

            Object obj = connectionsTree.getSelectionPath()
                                        .getLastPathComponent();

            if (!(obj instanceof VectorialDBConnectionTreeLeaf)) {
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    PluginServices.getText(this, "confirm_remove"),
                    PluginServices.getText(this, "Remove"),
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            VectorialDBConnectionTreeLeaf leaf = (VectorialDBConnectionTreeLeaf) obj;
            DataServerExplorerParameters _cwp = leaf.getConnectionWithParams();
//            SingleVectorialDBConnectionManager.instance().closeAndRemove(_cwp);
            refreshTree();
        }

        if (src == editButton) {
            if (connectionsTree.getSelectionCount() != 1) {
                return;
            }

            Object obj = connectionsTree.getSelectionPath()
                                        .getLastPathComponent();

            if (!(obj instanceof VectorialDBConnectionTreeLeaf)) {
                return;
            }

            VectorialDBConnectionTreeLeaf leaf = (VectorialDBConnectionTreeLeaf) obj;
            DataServerExplorerParameters old_cwp = leaf.getConnectionWithParams();

            VectorialDBConnectionParamsDialog modifyco = new VectorialDBConnectionParamsDialog();
            modifyco.loadValues((DBServerExplorerParameters) old_cwp);
            modifyco.showDialog();

            if (modifyco.isOkPressed()) {
                String _drvname = modifyco.getConnectionDriverName();
                String _host = modifyco.getConnectionServerUrl();
                String _port = modifyco.getConnectionPort();
                String _dbname = modifyco.getConnectionDBName();
                String _user = modifyco.getConnectionUser();
                String _pw = modifyco.getConnectionPassword();
                String _conn_usr_name = modifyco.getConnectionName();

                boolean hasToBeCon = modifyco.hasToBeConnected();

                boolean old_was_open = true;

//                try {
//                    old_was_open = SingleVectorialDBConnectionManager.instance()
//                                                              .closeAndRemove(old_cwp);

                    addConnection(_drvname, _port, _host, _dbname, _user, _pw,
                        _conn_usr_name, hasToBeCon);
//                }
//                catch (DBException e) {
//                    showConnectionErrorMessage(e.getMessage(), false);
//
//                    try {
//                        addConnection(old_cwp.getDrvName(), old_cwp.getPort(),
//                            old_cwp.getHost(), old_cwp.getDb(),
//                            old_cwp.getUser(), old_cwp.getPw(),
//                            old_cwp.getName(), old_was_open);
//                    }
//                    catch (DBException e1) {
//                        showConnectionErrorMessage(e.getMessage(), true);
//                    }
//                }
            }
        }
    }

    private void showConnectionErrorMessage(String _msg, boolean reconnect) {
        String msg = (_msg.length() > 300) ? "" : (": " + _msg);
        String title = "";

        if (reconnect) {
            title = PluginServices.getText(this, "reconnection_error");
        }
        else {
            title = PluginServices.getText(this, "connection_error");
        }

        JOptionPane.showMessageDialog(this, title + msg, title,
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method initializes connectButton
     *
     * @return javax.swing.JButton
     */
    private JButton getConnectButton() {
        if (connectButton == null) {
            connectButton = new JButton();
            connectButton.addActionListener(this);
            connectButton.setBounds(new java.awt.Rectangle(10, 380, 106, 26));
            connectButton.setText(PluginServices.getText(this, "connect"));
        }

        return connectButton;
    }

    /**
     * This method initializes disconnButton
     *
     * @return javax.swing.JButton
     */
    private JButton getDisconnButton() {
        if (disconnButton == null) {
            disconnButton = new JButton();
            disconnButton.addActionListener(this);
            disconnButton.setBounds(new java.awt.Rectangle(120, 380, 106, 26));
            disconnButton.setText(PluginServices.getText(this, "disconnect"));
        }

        return disconnButton;
    }

    /**
     * This method initializes editButton
     *
     * @return javax.swing.JButton
     */
    private JButton getEditButton() {
        if (editButton == null) {
            editButton = new JButton();
            editButton.addActionListener(this);
            editButton.setBounds(new java.awt.Rectangle(230, 350, 106, 26));
            editButton.setText(PluginServices.getText(this, "edit_settings"));
        }

        return editButton;
    }
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}

} //  @jve:decl-index=0:visual-constraint="10,10"
