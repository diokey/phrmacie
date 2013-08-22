package pharmacie.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pharmacie.util.Constantes;

public class SingletonConnection {

	private static volatile Connection connection; 
	
	private SingletonConnection() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection=DriverManager.getConnection(Constantes.DB_URL, Constantes.DB_USERNAME, Constantes.DB_PASSWORD);
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getInstance() {
		if(connection==null) {
			synchronized (Connection.class) {
				if(connection==null) {
					new SingletonConnection();
				}
			}
		}
		return connection;
	}
	
	public static void  main(String args[]) {
		System.out.println(SingletonConnection.getInstance());
	}

}
