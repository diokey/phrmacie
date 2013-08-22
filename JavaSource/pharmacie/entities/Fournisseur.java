package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;

public class Fournisseur implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Fournisseur() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean save() {
		String requete="INSERT INTO Fournisseur(nom,prenom,adresse,tel,email) values " +
				"('"+this.nomFournisseur+"','"+this.prenomFournisseur+"','"+this.adresse+"','"+this.tel+"','"+this.email+"')";
		return new DB().update(requete)>0;
	}
	
	public boolean update() {
		String requete="UPDATE Fournisseur SET nom='"+this.nomFournisseur+"', prenom='"+this.prenomFournisseur+"', adresse='"+this.adresse+"', tel='"+this.tel+"', email='"+this.email+"' " +
				" WHERE idFournisseur="+this.idFournisseur;
		//System.out.println(requete);
		return new DB().update(requete)>0;
	}
	
	public Fournisseur find(int idFournisseur) {
		String requete="SELECT * FROM Fournisseur where idFournisseur="+idFournisseur;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		Fournisseur f = new Fournisseur();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				
				f.idFournisseur = res.getInt("idFournisseur");
				f.nomFournisseur = res.getString("nom");
				f.prenomFournisseur = res.getString("prenom");
				f.tel = res.getString("tel");
				f.email = res.getString("email");
				f.adresse = res.getString("adresse");
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
		
		return f;
	}
	public List<Fournisseur> readAll() {
		String requete="SELECT * FROM Fournisseur";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Fournisseur> result = new ArrayList<Fournisseur>();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Fournisseur f = new Fournisseur();
				f.idFournisseur = res.getInt("idFournisseur");
				f.nomFournisseur = res.getString("nom");
				f.prenomFournisseur = res.getString("prenom");
				f.tel = res.getString("tel");
				f.email = res.getString("email");
				f.adresse = res.getString("adresse");
				
				result.add(f);
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
		
		return result;
	}
	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	public String getNomFournisseur() {
		return nomFournisseur;
	}
	public void setNomFournisseur(String nomFournisseur) {
		this.nomFournisseur = nomFournisseur;
	}
	public String getPrenomFournisseur() {
		return prenomFournisseur;
	}
	public void setPrenomFournisseur(String prenomFournisseur) {
		this.prenomFournisseur = prenomFournisseur;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}



	private int idFournisseur;
	private String nomFournisseur;
	private String prenomFournisseur;
	private String tel;
	private String email;
	private String adresse;

}
