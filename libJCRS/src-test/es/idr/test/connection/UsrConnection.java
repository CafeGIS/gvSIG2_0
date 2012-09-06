package es.idr.test.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UsrConnection {

	Connection connect;
	
	public UsrConnection() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {			
			connect =  DriverManager.getConnection("jdbc:hsqldb:file:/home/jlgomez/gvSIGF2/gt2-usr-hsql/src/org/geotools/referencing/factory/usr/usr", "sa", "");			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return connect;
	}
	
	public void shutdown() throws SQLException {

        Statement st = connect.createStatement();

        // db writes out to files and performs clean shuts down
        // otherwise there will be an unclean shutdown
        // when program ends
        st.execute("SHUTDOWN");
        connect.close();    // if there are no other open connection
    }
	
	public synchronized void update(String expression) throws SQLException {

        Statement st = null;

        st = connect.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    }    
}
