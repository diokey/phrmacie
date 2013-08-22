package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;

public class Fourniture implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Fourniture() {
		// TODO Auto-generated constructor stub
	}

	public boolean payerFourniture() {
		String requete="INSERT INTO PayementFournisseur(idFourniture,sommePaye,sommeRestante,datePayement) values("+this.idFourniture+","+this.sommePaye+","+this.sommeRestante+",NOW())";
		//System.out.println(requete);
		return new DB().update(requete)>0;
	}
	
	public long prixCommande() {
		String req="SELECT prixTotal FROM Fourniture f join Commande c on f.idCommande = c.idCommande where f.idFourniture = "+this.idFourniture+" limit 1";
		
		long rep = 0;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				rep = res.getLong("prixTotal");
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
		
		return rep;
	}
	
	public boolean saveFourniture() {
		String requete="INSERT INTO Fourniture(idFournisseur,idCommande,dateFourniture,sommeTotal) " +
				"values("+this.idFournisseur+","+this.idCommande+",NOW(),"+this.sommeTotal+")";
		return new DB().update(requete)>0;
	}
	
	public List<Fourniture> founitureCommande(int idCommande) {
		String requete="SELECT * FROM Fourniture WHERE idCommande="+idCommande;
		List<Fourniture> result = new ArrayList<Fourniture>();
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Fourniture f = new Fourniture();
				f.setIdFourniture(res.getInt("idFourniture"));
				f.setIdFournisseur(res.getInt("idFournisseur"));
				f.setFournisseur(new Fournisseur().find(f.getIdFournisseur()));
				f.setIdCommande(res.getInt("idCommande"));
				f.setDateFourniture(res.getDate("dateFourniture"));
				f.setSommeTotal(res.getLong("sommeTotal"));
				f.setSommePaye(this.readSommePaye(f.getIdFourniture()));
				f.setSommeRestante(f.getSommeTotal()-f.getSommePaye());
				
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
	
	private List<Medicament> medicamentsFournis(int idFourniture) {
		String requete="SELECT mf.idMedicament,m.nomMedicament,m.codeMedicament,m.type,mf.quantite,mf.prixUnitaire,mf.prix,mf.idFourniture" +
				" FROM MedicamentFournis mf join Medicament m on mf.idMedicament=m.idMedicament where mf.idFourniture="+this.idFourniture;
		
		List<Medicament> result = new ArrayList<Medicament>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
	
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.setIdMedicament(res.getInt("idMedicament"));
				m.setNomMedicament(res.getString("nomMedicament"));
				m.setCodeMedicament(res.getString("codeMedicament"));
				m.setTypeMedic(res.getString("type"));
				m.setQuantiteFournis(res.getInt("quantite"));
				m.setPrixAchat(res.getInt("prixUnitaire"));
				
				m.setPrixTotalFourniture(res.getLong("prix"));
				
				result.add(m);
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
	
	public int readSommePaye(int idFourniture) {
		int reponse=0;
		
		String requete="SELECT sum(sommePaye) sommePaye From PayementFournisseur where idFourniture="+idFourniture+" limit 1";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				reponse = res.getInt("sommePaye");
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
		
		return reponse;
	}
	
	//getters and setter
	public int getIdFourniture() {
		return idFourniture;
	}
	public void setIdFourniture(int idFourniture) {
		this.idFourniture = idFourniture;
	}
	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	public int getIdCommande() {
		return idCommande;
	}
	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}
	public long getSommeTotal() {
		return sommeTotal;
	}
	public void setSommeTotal(long sommeTotal) {
		this.sommeTotal = sommeTotal;
	}
	public Date getDateFourniture() {
		return dateFourniture;
	}
	public void setDateFourniture(Date dateFourniture) {
		this.dateFourniture = dateFourniture;
	}
	
	public long getSommePaye() {
		return sommePaye;
	}

	public void setSommePaye(long sommePaye) {
		this.sommePaye = sommePaye;
	}

	public long getSommeRestante() {
		return sommeRestante;
	}

	public void setSommeRestante(long sommeRestante) {
		this.sommeRestante = sommeRestante;
	}
	
	public List<Medicament> getMedicamentsFournis() {
		if(medicamentsFournis==null) {
			medicamentsFournis = medicamentsFournis(this.idFournisseur);
		}
		return medicamentsFournis;
	}

	public void setMedicamentsFournis(List<Medicament> medicamentsFournis) {
		this.medicamentsFournis = medicamentsFournis;
	}

	public long getPrixCommande() {
		prixCommande = this.prixCommande();
		return prixCommande;
	}

	public void setPrixCommande(long prixCommande) {
		this.prixCommande = prixCommande;
	}
	
	public Fournisseur getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}






	//members attributes
	private int idFourniture;
	private int idFournisseur;
	private int idCommande;
	private long sommeTotal;
	private long sommePaye;
	private long sommeRestante;
	private long prixCommande;
	private Date dateFourniture;
	private Fournisseur fournisseur = new Fournisseur();
	
	
	private List<Medicament> medicamentsFournis;
}
