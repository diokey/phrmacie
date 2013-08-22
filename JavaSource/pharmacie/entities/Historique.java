package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pharmacie.connection.SingletonConnection;

public class Historique implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Historique() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Historique> getHistoriqueVente(int idMedicament) {
		String requete="SELECT m.IdMedicament,m.nomMedicament,a.IdAchat,a.dateAchat," +
				"sum(p.quantite) quantite,p.prixUnitaire,sum(p.prixTotal) prixTotal," +
				"pri.reduction,sum(p.prixTotalReduit) prixTotalReduit," +
				"t.nomTypeAchat,concat(c.nom,' ',c.prenom) client " +
				"FROM Panier p join Stock s on p.IdStock=s.IdStock join Medicament m on s.IdMedicament=m.IdMedicament " +
				"JOIN Achat a on p.IdAchat=a.IdAchat " +
				"JOIN Prix pri on pri.IdStock=s.IdStock and pri.IdTypeAchat=a.IdTypeAchat " +
				"LEFT JOIN TypeAchat t on a.IdTypeAchat = t.IdTypeAchat " +
				"left JOIN Client c on a.IdClient=c.IdClient " +
				"where m.idMedicament = " +idMedicament+" "+
				"group by dateAchat,IdMedicament";
		
		System.out.println(requete);
		List<Historique> res = new ArrayList<Historique>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet result=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			result=statement.executeQuery(requete);
			while(result.next()) {
				Historique h = new Historique();
				h.dateVente = result.getDate("dateAchat");
				h.idMedicament = result.getInt("IdMedicament");
				h.quantite = result.getInt("quantite");
				h.prixUnitaire = result.getLong("prixUnitaire");
				h.prixTotal = result.getLong("prixTotal");
				h.client = result.getString("client");
				h.reduction = result.getInt("reduction");
				h.prixTotalReduit = result.getLong("prixTotalReduit");
				h.prixUnitaireReduit = h.prixTotalReduit / h.quantite;
				h.typeAchat = result.getString("nomTypeAchat");
				if(h.client==null || h.client.equalsIgnoreCase("null")) {
					h.client="";
				}
				
				res.add(h);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				result.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		
		return res;
	}
	public List<Historique> getHistoriqueAchat(int idMedicament) {
		String requete="SELECT f.dateFourniture, mf.idMedicament,sum(mf.quantite) quantite,mf.prixUnitaire,sum(mf.prix) prixTotal ,concat(fou.nom,' ' ,fou.prenom) fournisseur FROM `MedicamentFournis` mf " +
				"JOIN Medicament m on m.idMedicament=mf.idMedicament " +
				"JOIN Fourniture f on f.idFourniture=mf.idFourniture " +
				"join Fournisseur fou on fou.idFournisseur=f.idFournisseur " +
				"where mf.idMedicament = " + idMedicament +
				" group by dateFourniture,idMedicament";
		
		System.out.println(requete);
		List<Historique> res = new ArrayList<Historique>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet result=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			result=statement.executeQuery(requete);
			
			while(result.next()) {
				
				Historique h = new Historique();
				h.dateFourniture = result.getDate("dateFourniture");
				h.idMedicament = result.getInt("idMedicament");
				h.quantite = result.getInt("quantite");
				h.prixUnitaire = result.getLong("prixUnitaire");
				h.prixTotal = result.getLong("prixTotal");
				h.fournisseur = result.getString("fournisseur");
				
				res.add(h);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				result.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		
		return res;
	}
	
	
	
	public Date getDateFourniture() {
		return dateFourniture;
	}

	public void setDateFourniture(Date dateFourniture) {
		this.dateFourniture = dateFourniture;
	}

	public int getIdMedicament() {
		return idMedicament;
	}

	public void setIdMedicament(int idMedicament) {
		this.idMedicament = idMedicament;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public long getPrixUnitaire() {
		return prixUnitaire;
	}

	public void setPrixUnitaire(long prixUnitaire) {
		this.prixUnitaire = prixUnitaire;
	}

	public long getPrixTotal() {
		return prixTotal;
	}

	public void setPrixTotal(long prixTotal) {
		this.prixTotal = prixTotal;
	}

	public String getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}

	public Date getDateVente() {
		return dateVente;
	}

	public void setDateVente(Date dateVente) {
		this.dateVente = dateVente;
	}

	public int getReduction() {
		return reduction;
	}

	public void setReduction(int reduction) {
		this.reduction = reduction;
	}

	public long getPrixTotalReduit() {
		return prixTotalReduit;
	}

	public void setPrixTotalReduit(long prixTotalReduit) {
		this.prixTotalReduit = prixTotalReduit;
	}

	public String getTypeAchat() {
		return typeAchat;
	}

	public void setTypeAchat(String typeAchat) {
		this.typeAchat = typeAchat;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
		
	public long getPrixUnitaireReduit() {
		return prixUnitaireReduit;
	}

	public void setPrixUnitaireReduit(long prixUnitaireReduit) {
		this.prixUnitaireReduit = prixUnitaireReduit;
	}



	private Date dateFourniture;
	private Date dateVente;
	private int reduction;
	private long prixUnitaireReduit;
	private long prixTotalReduit;
	private String typeAchat;
	private String client; 	
	private int idMedicament;
	private int quantite;
	private long prixUnitaire;
	private long prixTotal;
	private String fournisseur;

}
