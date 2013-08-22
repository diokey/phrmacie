package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class PrixAchat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PrixAchat() {
		// TODO Auto-generated constructor stub
	}
	
	public PrixAchat(String codeMedicament, String nomMedicament,String typeMedicament,int prix) {
		this.codeMedicament = codeMedicament;
		this.nomMedicament = nomMedicament;
		this.typeMedicament = typeMedicament;
		this.prix = prix;
	}
	
	public PrixAchat(int idMedicament,String codeMedicament, String nomMedicament,String typeMedicament) {
		this.idMedicament = idMedicament;
		this.codeMedicament = codeMedicament;
		this.nomMedicament = nomMedicament;
		this.typeMedicament = typeMedicament;
	}
	
	public boolean save() {
		String requete="INSERT INTO PrixAchat(idMedicament,idFournisseur,prixAchat) " +
				"values("+this.idMedicament+","+this.idFournisseur+","+this.prix+")";
		//System.out.println(requete);
		return new DB().update(requete)>0;
	}
	
	public boolean update() {
		String requete="UPDATE PrixAchat SET prixAchat="+this.prix+" where idPrixAchat="+this.idPrixAchat;
		//System.out.println(requete);
		
		return new DB().update(requete)>0;
	}
	
	public int saveMedicament() {
		
		String requete="INSERT INTO Medicament(codeMedicament,nomMedicament,type)" +
		" values(?,?,?)";
		PreparedStatement pst = null;
		
		try {
			pst = SingletonConnection.getInstance().prepareStatement(requete);
			pst.setString(1, this.codeMedicament);
			pst.setString(2, this.nomMedicament);
			pst.setString(3, this.typeMedicament);
			
			if(pst.executeUpdate()>0) {
				int idMedicament = CommonUtils.getLastId("Medicament", "idMedicament");
				
				return idMedicament;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e) {
				
			}
		}
		
		return -1;
	}
	
	public List<PrixAchat> getListSansPrix() {
		List<PrixAchat> listSansPrix = new ArrayList<PrixAchat>();
		String requete="SELECT * FROM Medicament";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				PrixAchat p = new PrixAchat(res.getInt("idMedicament"),res.getString("codeMedicament"),res.getString("nomMedicament"),res.getString("type"));
				listSansPrix.add(p);
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
		return listSansPrix;
	}

	/**
	 * 
	 * @param idMedicament
	 * @return list des prix de chaque assureurs
	 */
	public List<PrixAchat> prixMedicament(int idMedicament) {
		String requete="SELECT * FROM PrixAchat where idMedicament="+idMedicament;
		List<PrixAchat> listPrix = new ArrayList<PrixAchat>();
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				PrixAchat p = new PrixAchat();
				p.idPrixAchat = res.getInt("idPrixAchat");
				p.idMedicament = res.getInt("idMedicament");
				p.codeMedicament = res.getString("codeMedicament");
				p.nomMedicament = res.getString("nomMedicament");
				p.idFournisseur = res.getInt("idFournisseur");
				p.typeMedicament = res.getString("type");
				p.prix = res.getInt("prixAchat");
				p.fournisseur = new Fournisseur().find(p.idFournisseur);
				
				listPrix.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		
		return listPrix;
	}
	
	public int getPrixAchat(int idMedicament, int idFournisseur) {
		String requete="SELECT prixAchat FROM PrixAchat where idMedicament="+idMedicament+" and idFournisseur="+idFournisseur;
		int prix = 0;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				prix = res.getInt("prixAchat");
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
		return prix;
	}
	
	public List<PrixAchat> getListAvecPrix(int idFournisseur) {
		List<PrixAchat> listAvecPrix = new ArrayList<PrixAchat>();
		
		String requete="SELECT p.idPrixAchat,m.idMedicament,p.idFournisseur,p.prixAchat,m.nomMedicament,m.codeMedicament,m.type" +
				" FROM PrixAchat p join Medicament m on p.idMedicament=m.idMedicament where p.idFournisseur="+idFournisseur;
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				PrixAchat p = new PrixAchat();
				p.idPrixAchat = res.getInt("idPrixAchat");
				p.idMedicament = res.getInt("idMedicament");
				p.codeMedicament = res.getString("codeMedicament");
				p.nomMedicament = res.getString("nomMedicament");
				p.idFournisseur = res.getInt("idFournisseur");
				p.typeMedicament = res.getString("type");
				p.prix = res.getInt("prixAchat");
				
				listAvecPrix.add(p);
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
		return listAvecPrix;
	}
	
	public boolean isNew() {
		return this.idPrixAchat==0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codeMedicament == null) ? 0 : codeMedicament.hashCode());
		result = prime * result
				+ ((nomMedicament == null) ? 0 : nomMedicament.hashCode());
		result = prime * result
				+ ((typeMedicament == null) ? 0 : typeMedicament.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrixAchat other = (PrixAchat) obj;
		if (codeMedicament == null) {
			if (other.codeMedicament != null)
				return false;
		} else if (!codeMedicament.equals(other.codeMedicament))
			return false;
		if (nomMedicament == null) {
			if (other.nomMedicament != null)
				return false;
		} else if (!nomMedicament.equals(other.nomMedicament))
			return false;
		if (typeMedicament == null) {
			if (other.typeMedicament != null)
				return false;
		} else if (!typeMedicament.equals(other.typeMedicament))
			return false;
		return true;
	}

	


	@Override
	public String toString() {
		return  codeMedicament
				+ ":" + nomMedicament + ":"
				+ typeMedicament;
	}


	public int getIdPrixAchat() {
		return idPrixAchat;
	}
	public void setIdPrixAchat(int idPrixAchat) {
		this.idPrixAchat = idPrixAchat;
	}
	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	public String getCodeMedicament() {
		return codeMedicament;
	}
	public void setCodeMedicament(String codeMedicament) {
		this.codeMedicament = codeMedicament;
	}
	public String getNomMedicament() {
		return nomMedicament;
	}
	public void setNomMedicament(String nomMedicament) {
		this.nomMedicament = nomMedicament;
	}
	public String getTypeMedicament() {
		return typeMedicament;
	}
	public void setTypeMedicament(String typeMedicament) {
		this.typeMedicament = typeMedicament;
	}
	
	public Integer getPrix() {
		return prix;
	}

	public void setPrix(Integer prix) {
		this.prix = prix;
	}
	
	public int getIdMedicament() {
		return idMedicament;
	}

	public void setIdMedicament(int idMedicament) {
		this.idMedicament = idMedicament;
	}
	
	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public Fournisseur getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}

	private int idPrixAchat;
	private int idFournisseur;
	private int idMedicament;
	private String codeMedicament;
	private String nomMedicament;
	private String typeMedicament;
	private Integer prix;
	private boolean modified;
	private Fournisseur fournisseur;
	

}
