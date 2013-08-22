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

public class Assureur implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Assureur() {
		this.prix = new Prix();
	}
	
	//methods
	public boolean save() {
		String requete="INSERT INTO TypeAchat (nomTypeAchat,nomLong,BP,Tel) values " +
				"('"+this.nomAssureur+"','"+this.nomAssureurLong+"','"+this.bp+"','"+this.tel+"')";
		return new DB().update(requete)>0;
	}
	
	public boolean update() {
		String requete="UPDATE TypeAchat set nomTypeAchat='"+this.nomAssureur+"', nomLong='"+this.nomAssureurLong+"', BP='"+this.bp+"', Tel='"+this.tel+"' " +
				" WHERE idTypeAchat="+this.idAssureur;
		//System.out.println(requete);
		return new DB().update(requete)>0;		
	}
	
	public boolean saveTarifAssureur(int idStock) {
		
		this.prix.setIdTypeAchat(this.idAssureur);
		this.prix.setIdStock(idStock);
		return this.prix.savePrix();
		
	}
		
	public boolean updateTarifAssureur() {
		this.prix.setIdTypeAchat(this.idAssureur);
		
		return this.prix.updatePrix();
	}
	
	public Assureur findAssureur(int id) {
		String requete="SELECT * FROM TypeAchat WHERE idTypeAchat="+id;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		Assureur assureur = new Assureur();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				
				assureur.idAssureur = res.getInt("idTypeAchat");
				assureur.nomAssureur = res.getString("nomTypeAchat");
				assureur.nomAssureurLong = res.getString("nomLong");
				assureur.tel = res.getString("Tel");
				assureur.bp = res.getString("BP");
				
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
		
		return assureur;
	}
	
	public List<Assureur> listAssureur() {
		String requete="SELECT * FROM TypeAchat order by idTypeAchat";
		List<Assureur> assureurs = new ArrayList<Assureur>();
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Assureur assureur = new Assureur();
				assureur.idAssureur = res.getInt("idTypeAchat");
				assureur.nomAssureur = res.getString("nomTypeAchat");
				assureur.nomAssureurLong = res.getString("nomLong");
				assureur.tel = res.getString("Tel");
				assureur.bp = res.getString("BP");
				Prix p = new Prix();
				
				p.setNomAssureur(assureur.getNomAssureur());
				assureur.prix = p;
				
				assureurs.add(assureur);
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
		
		return assureurs;
	}
	
	public List<Assureur> getPrixAllAsseur(int idStock){
		String requete="SELECT * FROM TypeAchat";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		List<Assureur> assureurs = new ArrayList<Assureur>();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Assureur assureur = new Assureur();
				assureur.idAssureur = res.getInt("idTypeAchat");
				assureur.nomAssureur = res.getString("nomTypeAchat");
				assureur.nomAssureurLong = res.getString("nomLong");
				assureur.tel = res.getString("Tel");
				assureur.bp = res.getString("BP");
				Prix p = new Prix();
				
				p.setNomAssureur(assureur.nomAssureur);
				assureur.prix = p.getPrixMedicament(idStock, assureur.idAssureur);
				
				assureurs.add(assureur);
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
		
		return assureurs;
	}
	
	
	@Override
	public String toString() {
		return "Assureur [idAssureur=" + idAssureur + ", nomAssureur="
				+ nomAssureur + ", nomAssureurLong=" + nomAssureurLong
				+ ", tel=" + tel + ", bp=" + bp + "]";
	}

	//getters and setters
	public int getIdAssureur() {
		return idAssureur;
	}
	public void setIdAssureur(int idAssureur) {
		this.idAssureur = idAssureur;
	}
	public String getNomAssureur() {
		return nomAssureur;
	}
	public void setNomAssureur(String nomAssureur) {
		this.nomAssureur = nomAssureur;
	}

	public Prix getPrix() {
		return prix;
	}

	public void setPrix(Prix prix) {
		this.prix = prix;
	}
	
	public String getNomAssureurLong() {
		return nomAssureurLong;
	}

	public void setNomAssureurLong(String nomAssureurLong) {
		this.nomAssureurLong = nomAssureurLong;
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



	//private Members
	private int idAssureur;
	private String nomAssureur;
	private String nomAssureurLong;
	private String tel;
	private String bp;
	private Prix prix;

}
