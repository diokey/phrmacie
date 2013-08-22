package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;

public class Credit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Credit() {
		// TODO Auto-generated constructor stub
		this.setBeneficiaire(new Client().getListBeneficiaire().get(0));
	}

	//methods
	
	
	public boolean save() {
		String requete="INSERT INTO Credit(idAchat,idClient,beneficiaire,numeroPharmaceutique,codeAgent,codeMedecin,poste) " +
				"values("+this.idAchat+","+this.idClient+",'"+this.beneficiaire+"','"+this.numeroPharmaceutique+"','"+this.codeAgent+"','"+this.codeMedecin+"','"+this.poste+"')";
		
		return new DB().update(requete)>0;		
	}
	
	public boolean deleteCreditAchat(int idAchat) {
		String req="DELETE FROM Credit where idAchat="+idAchat;
		
		return new DB().update(req)>0;
	}
	
	public Credit findCredit(int idAchat) {
		String requete="SELECT * FROM Credit where idAchat="+idAchat+" limit 1";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		Credit credit = new Credit();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			if(res.next()) {
				credit.idAchat = res.getInt("idAchat");
				credit.idClient = res.getInt("idClient");
				credit.beneficiaire = res.getString("beneficiaire");        
				credit.numeroPharmaceutique = res.getString("numeroPharmaceutique");
				credit.codeAgent = res.getString("codeAgent");           
				credit.codeMedecin = res.getString("codeMedecin");         
				credit.poste = res.getString("poste");               
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
		
		return credit;
	}
	//getters and setters
	public int getIdAchat() {
		return idAchat;
	}
	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}
		
	public String getBeneficiaire() {
		return beneficiaire;
	}

	public int getIdClient() {
		return idClient;
	}

	public void setIdClient(int idClient) {
		this.idClient = idClient;
	}

	public void setBeneficiaire(String beneficiaire) {
		this.beneficiaire = beneficiaire;
	}

	public String getCodeAgent() {
		return codeAgent;
	}

	public void setCodeAgent(String codeAgent) {
		this.codeAgent = codeAgent;
	}

	public String getCodeMedecin() {
		return codeMedecin;
	}

	public void setCodeMedecin(String codeMedecin) {
		this.codeMedecin = codeMedecin;
	}

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public String getNumeroPharmaceutique() {
		return numeroPharmaceutique;
	}

	public void setNumeroPharmaceutique(String numeroPharmaceutique) {
		this.numeroPharmaceutique = numeroPharmaceutique;
	}
	
	//properties
	



	private int idAchat;	
	private int idClient;
	private String beneficiaire;
	private String numeroPharmaceutique;
	private String codeAgent;
	private String codeMedecin;
	private String poste;
	
}
