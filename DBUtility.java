package com.inventory.macwarehouse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.kenss.utilities.Constants;

public class DBUtility {
	
	private Connection mySQLConnection = null;
	private PreparedStatement ps = null;

	public DBUtility() {
		// TODO Auto-generated constructor stub
	}

	public Connection getConnection() throws SQLException {

	    Properties connectionProps = new Properties();
	    connectionProps.put("user", Constants.DBUSER);
	    connectionProps.put("password", Constants.DBPASSWORD);

	    mySQLConnection = DriverManager.getConnection(
                   "jdbc:mysql://" +
                   Constants.DBHOST +
                   ":" + Constants.DBPORT + "/",
                   connectionProps);
	    return mySQLConnection;
	}

	
	public long getPONumber(MacWarehouseProduct receivedProduct) throws SQLException {
		
		if (ps == null) {
			if (mySQLConnection != null){
				try {
					//Get all the POs for that have this SKU as a line item and the status is OPEN, PARTIALLY FULFILLED, ENTERED. 
					ps = mySQLConnection.prepareStatement("select po.id,  from macwarehouse.po inner join macwarehouse.poitem on po.id=poitem.poid" + 
															" inner join macwarehouse.poitemstatus on poitem.statusid = poitemstatus.id" +
															" where poitem.partnum = ?" +
															" and poitemstatus.name not in ('Closed', 'Void', 'adsfasfs')");
				} catch (SQLException e) {
					System.out.println("Error trying to retrieve PO number: " + e.getMessage());
				}
			}
		}
		
		try {
			ps.setString(0, receivedProduct.getSKU());
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				
			}
		}
		catch (Exception e) {
			System.out.println("Error trying to retrieve PO number: " + e.getMessage());
		}
		
		
		
		return 0L;
		
		
	}
}
