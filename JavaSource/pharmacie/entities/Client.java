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
import pharmacie.util.CommonUtils;

public class Client implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	public Client(Client copy) {
		idClient = copy.idClient;          
		nomClient = copy.nomClient;      
		prenomClient = copy.prenomClient;   
		numeroMatricule = copy.numeroMatricule;
		beneficiare = copy.beneficiare;    
		idService = copy.idService;         
		nomService = copy.nomService;     
		idAssureur = copy.idAssureur;        
		nomAssureur = copy.nomAssureur;    
		exists = copy.exists;        
	}

	
	//methods
	public int save() {
		String requete="INSERT INTO Client(nom,prenom,matricule,idService,idAssureur) values " +
				"('"+this.nomClient+"','"+this.prenomClient+"','"+this.numeroMatricule+"',"+this.idService+","+this.idAssureur+")";
		if(new DB().update(requete)>0) {
			return CommonUtils.getLastId("Client", "idClient");
		}
		return -1;
	}
	
	public boolean update() {
		String req="UPDATE Client SET nom='"+this.nomClient+"',prenom='"+this.prenomClient+"',matricule='"+this.numeroMatricule+"',idService='"+this.idService+"'," +
				"idAssureur='"+this.idAssureur+"' where idClient="+this.idClient;
		System.out.println(req);
		
		return new DB().update(req)>0;
	}
	
public Client findClient(int idClient) {
		
		String requete="SELECT c.idClient,c.nom,c.prenom,c.matricule,c.idService,s.nomService,c.idAssureur,t.nomTypeAchat " +
		"FROM Client c JOIN Service s on c.idService=s.idService JOIN TypeAchat t on t.idTypeAchat=c.idAssureur where c.idClient="+idClient;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		Client c = new Client();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				c.idClient = res.getInt("idClient");
				c.nomClient = res.getString("nom");
				c.prenomClient = res.getString("prenom");
				c.numeroMatricule = res.getString("matricule");
				c.idService = res.getInt("idService");
				c.nomService = res.getString("nomService");
				c.idAssureur = res.getInt("idAssureur");
				c.nomAssureur = res.getString("nomTypeAchat");
				
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
		
		return c;
	}
	
	public List<Client> clientList() {
		String requete="SELECT c.idClient,c.nom,c.prenom,c.matricule,c.idService,s.nomService,c.idAssureur,t.nomTypeAchat " +
				"FROM Client c JOIN Service s on c.idService=s.idService JOIN TypeAchat t on t.idTypeAchat=c.idAssureur";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		//System.out.println(requete);
		
		List<Client> clients = new ArrayList<Client>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Client c = new Client();
				c.idClient = res.getInt("idClient");
				c.nomClient = res.getString("nom");
				c.prenomClient = res.getString("prenom");
				c.numeroMatricule = res.getString("matricule");
				c.idService = res.getInt("idService");
				c.nomService = res.getString("nomService");
				c.idAssureur = res.getInt("idAssureur");
				c.nomAssureur = res.getString("nomTypeAchat");
								
				clients.add(c);
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
		
		return clients;
	}
	
	//getters and setters
	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getPrenomClient() {
		return prenomClient;
	}

	public void setPrenomClient(String prenomClient) {
		this.prenomClient = prenomClient;
	}

	public String getNumeroMatricule() {
		return numeroMatricule;
	}

	public void setNumeroMatricule(String numeroMatricule) {
		this.numeroMatricule = numeroMatricule;
	}

	public String getBeneficiare() {
		return beneficiare;
	}

	public void setBeneficiare(String beneficiare) {
		this.beneficiare = beneficiare;
	}

	
	public int getIdService() {
		return idService;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}

	public int getIdAssureur() {
		return idAssureur;
	}

	public void setIdAssureur(int idAssureur) {
		this.idAssureur = idAssureur;
	}

	public String getNomService() {
		return nomService;
	}

	public void setNomService(String nomService) {
		this.nomService = nomService;
	}

	public String getNomAssureur() {
		return nomAssureur;
	}

	public void setNomAssureur(String nomAssureur) {
		this.nomAssureur = nomAssureur;
	}

	public List<String> getListBeneficiaire() {
		if(listBeneficiaire==null) {
			this.listBeneficiaire = new ArrayList<String>();
			this.listBeneficiaire.add("Lui-meme");
			this.listBeneficiaire.add("Enfant");
			this.listBeneficiaire.add("Conjoint");
		}
		return listBeneficiaire;
	}

	public void setListBeneficiaire(List<String> listBeneficiaire) {
		this.listBeneficiaire = listBeneficiaire;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	public long getSommeAchat() {
		return sommeAchat;
	}

	public void setSommeAchat(long sommeAchat) {
		this.sommeAchat = sommeAchat;
	}




	private int idClient;
	private String nomClient;
	private String prenomClient;
	private String numeroMatricule;
	private String beneficiare;
	private int idService;
	private String nomService;
	private int idAssureur;
	private String nomAssureur;
	private boolean exists;
	
	private long sommeAchat;
	
	private List<String> listBeneficiaire;
}
