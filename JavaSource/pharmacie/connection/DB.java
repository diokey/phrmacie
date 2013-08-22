package pharmacie.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

	public DB() {
		// TODO Auto-generated constructor stub
	}
	
	public ResultSet read(String request) {
		//con=HouseLoanConnexion.getInstance();
		Connection con=SingletonConnection.getInstance();
		
		ResultSet res=null;
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(request);
			return res;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				statement = null;
			}catch(NullPointerException e) {
				e.printStackTrace();
			}
			
		}
		return res;	
	}
	
	public int update(String requete) {
		Connection con=SingletonConnection.getInstance();
		int res=-1;
		Statement statement = null;
		try {
			statement=con.createStatement();
			res = statement.executeUpdate(requete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e) {
				
			}
		}
		
		return res;
	}
	
	
	public static void main(String args[]){
		//String requete="INSERT INTO User(username,password,role) values('olivier','"+HashUtil.hash("diokey")+"','admin')";
		
		//System.out.println(update(requete));
	}

}
