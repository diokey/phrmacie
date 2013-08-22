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

public class Commande implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Commande() {
		// TODO Auto-generated constructor stub
	}

	
	//methods
	public boolean saveCommande() {
		String requete="INSERT INTO Commande(idUtilisateur,dateCommande,prixTotal,idFournisseur) " +
				"VALUES ("+this.idUtilisateur+",NOW(),"+this.prixTotal+","+this.idFournisseur+") ";
		
		return new DB().update(requete)>0;
	}
	
	public List<Commande> listAllOrders() {
		String requete="SELECT * FROM Commande order by idCommande desc";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Commande> listCommande = new ArrayList<Commande>();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Commande commande = new Commande();
				commande.setIdCommande(res.getInt("idCommande"));
				commande.setIdUtilisateur(res.getInt("idUtilisateur"));
				commande.setDateCommande(res.getDate("dateCommande"));
				commande.setPrixTotal(res.getInt("prixTotal"));
				commande.setIdFournisseur(res.getInt("idFournisseur"));
				commande.setDelivered(res.getBoolean("delivered"));
				
				listCommande.add(commande);
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
		
		return listCommande;
	}
	
	public boolean commandeFournis() {
		// TODO Auto-generated method stub
		String requete="UPDATE Commande set delivered=1 where idCommande="+this.idCommande;
		//System.out.println("Commande livree: "+requete);
		return new DB().update(requete)>0;
	}
	
	public String getFournisseurName() {
		
		if(this.getFournisseur()!=null)
			return this.fournisseur.getNomFournisseur()+" "+this.fournisseur.getPrenomFournisseur();
		return "";
		
	}
	
	public String getUserName() {
		User u = new User().find(this.idUtilisateur);
		if(u!=null) {
			return ""+u.getNom()+" "+u.getPrenom();
		}
		return "";
	}
	
	public boolean deliveryStarted() {
		String req="SELECT * FROM Fourniture WHERE idCommande = "+this.idCommande;
		boolean rep = false;
		Connection con = SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement = con.createStatement();
			
			res = statement.executeQuery(req);
			
			rep = res.next();
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
			} catch(Exception e) {
				
			}
			
		}
		
		return rep;
	}
	
	public boolean deleteOrder() {
		String req2 = "DELETE FROM MedicamentCommande where idCommande = "+this.idCommande;
		String req="DELETE FROM Commande where idCommande="+this.idCommande;
		System.out.println(req2);
		System.out.println(req);
		return new DB().update(req2)>0 && new DB().update(req)>0;
	}
	//getters and setters	
	
	private int idUtilisateur;
	public int getIdUtilisateur() {
		return idUtilisateur;
	}
	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	public int getIdCommande() {
		return idCommande;
	}
	public void setIdCommande(int idCommande) {
		this.idCommande = idCommande;
	}
	public Date getDateCommande() {
		return dateCommande;
	}
	public void setDateCommande(Date dateCommande) {
		this.dateCommande = dateCommande;
	}
	public long getPrixTotal() {
		return prixTotal;
	}
	public void setPrixTotal(long prixTotal) {
		this.prixTotal = prixTotal;
	}
	public int getIdFournisseur() {
		return idFournisseur;
	}
	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	
	public List<Medicament> getProduitsCommande() {
		if(produitsCommande==null)
			produitsCommande = Medicament.fetchOrderDetails(this.idCommande);
		return produitsCommande;
	}

	public void setProduitsCommande(List<Medicament> produitsCommande) {
		this.produitsCommande = produitsCommande;
	}
		
	public List<Medicament> getAllProduitsCommande() {
		allProduitsCommande = Medicament.fetchOrderDetails(this.idCommande);
		return allProduitsCommande;
	}

	public void setAllProduitsCommande(List<Medicament> allProduitsCommande) {
		this.allProduitsCommande = allProduitsCommande;
	}


	public int getItemCount() {
		itemCount=0;
		for(Medicament m : this.getProduitsCommande()) {
			itemCount+=m.getQuantiteAchete();
		}
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	public Fournisseur getFournisseur() {
		fournisseur = new Fournisseur().find(this.idFournisseur);
		return fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}
	
	public boolean isDelivered() {
		return delivered;
	}


	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	//attributes
	private int idCommande;
	private Date dateCommande;
	private long prixTotal;
	private int idFournisseur;
	private int itemCount;
	private boolean delivered;
	
	private List<Medicament> produitsCommande;
	private List<Medicament> allProduitsCommande;
	
	private Fournisseur fournisseur;

	
	
}
