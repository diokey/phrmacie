package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;

public class OwnerData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OwnerData() {
		// TODO Auto-generated constructor stub
		loadData();
	}
	
	private void loadData() {
		String requete="SELECT * FROM OwnerInfo";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				this.nomOwner = res.getString("nomPharmacie");
				this.tel = res.getString("tel");
				this.bp = res.getString("bp");
				this.id = res.getInt("id");
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
	}
	
	public boolean updateNom(String nouveauNom) {
		String requete="UPDATE OwnerInfo SET nomPharmacie='"+nouveauNom+"'";
		return new DB().update(requete)>0;
	}
	
	public boolean updateTel(String nouveauTel) {
		String requete="UPDATE OwnerInfo SET tel='"+nouveauTel+"'";
		return new DB().update(requete)>0;
	}
	
	public boolean updateBP(String nouveauBP) {
		String requete="UPDATE OwnerInfo SET Bp='"+nouveauBP+"'";
		return new DB().update(requete)>0;
	}
	
	public String getNomOwner() {
		return nomOwner;
	}
	public void setNomOwner(String nomOwner) {
		this.nomOwner = nomOwner;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getBp() {
		return bp;
	}
	public void setBp(String bp) {
		this.bp = bp;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	private int id;
	private String nomOwner;
	private String tel;
	private String bp;
}
