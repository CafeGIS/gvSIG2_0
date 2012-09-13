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
package com.iver.cit.gvsig;

import org.gvsig.AppGvSigLocator;
import org.gvsig.fmap.dal.DALDbLibrary;
import org.gvsig.fmap.dal.store.db.DBStoreLibrary;
import org.gvsig.fmap.dal.store.jdbc.JDBCLibrary;
import org.gvsig.fmap.dal.store.mysql.MySQLLibrary;
import org.gvsig.fmap.dal.store.postgresql.PostgreSQLLibrary;

import com.iver.andami.plugins.Extension;
import com.prodevelop.cit.gvsig.vectorialdb.wizard.WizardDB;
import com.prodevelop.cit.gvsig.vectorialdb.wizard.WizardVectorialDB;


/**
 * This extension adds the export-to-oracle button.
 *
 * @author jldominguez
 *
 */
public class ExtDB_Spatial extends Extension {

    private DALDbLibrary dbLibrary;
    private DBStoreLibrary dbStoreLibrary;
	private JDBCLibrary jdbcLibrary;
	private PostgreSQLLibrary posgresqlLibrary;
	private MySQLLibrary mysqlLibrary;

	public void initialize() {
    	dbLibrary = new DALDbLibrary();
    	dbStoreLibrary = new DBStoreLibrary();
		jdbcLibrary = new JDBCLibrary();
		posgresqlLibrary = new PostgreSQLLibrary();
		mysqlLibrary = new MySQLLibrary();


		dbLibrary.initialize();
		dbStoreLibrary.initialize();
		jdbcLibrary.initialize();
		posgresqlLibrary.initialize();
		mysqlLibrary.initialize();


    }

    public void execute(String actionCommand) {
    }

    public boolean isEnabled() {
        return isVisible();

        // return true;
    }

    /**
     * Is visible when there is one vector layer selected
     */
    public boolean isVisible() {
        return false;
    }

	@Override
	public void postInitialize() {
		super.postInitialize();

		dbLibrary.postInitialize();
		dbStoreLibrary.postInitialize();
		jdbcLibrary.postInitialize();
		posgresqlLibrary.postInitialize();
		mysqlLibrary.postInitialize();

		AppGvSigLocator.getAppGvSigManager().registerAddTableWizard("DB",
				"DB Tables", WizardDB.class);
    	AddLayer.addWizard(WizardVectorialDB.class);
	}
}
