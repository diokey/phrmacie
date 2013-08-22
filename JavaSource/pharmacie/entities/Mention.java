package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacie.connection.SingletonConnection;

public class Mention implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Mention() {
		// TODO Auto-generated constructor stub
	}
	
	//methods
	
	public List<Mention> listMention(){
		List<Mention> mentions = null;
		String requete="SELECT * FROM Mention";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		mentions = new ArrayList<Mention>();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Mention m = new Mention();
				m.idMention = res.getInt("idMention");
				m.legende = res.getString("legende");
				m.description = res.getString("description");
				m.icone = res.getString("icone");
				mentions.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		return mentions;
	}
	public static int getId(String mentionString) {
		// TODO Auto-generated method stub
		String requete="SELECT idMention FROM Mention where description='"+mentionString+"'";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		int rep = 0;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				rep = res.getInt("idMention");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rep;
	}
	//getters and setters
	public Integer getIdMention() {
		return idMention;
	}
	public void setIdMention(Integer idMention) {
		this.idMention = idMention;
	}
	public String getLegende() {
		return legende;
	}
	public void setLegende(String legende) {
		this.legende = legende;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getIcone() {
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}


	//private properties
	private Integer idMention;
	private String legende;
	private String description;
	private String icone;

	

}
