package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class Achat implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//methods
	public Achat() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return idAchat used
	 */
	public int save() {
		
		String requete="";
		if(this.clientId==0) {
			requete="INSERT INTO Achat(userId,dateAchat,sommeTotal,sommeTotalReduit,idTypeAchat) values("+this.userId+",NOW(),"+this.sommeTotal+","+this.sommeTotalReduit+","+this.idTypeAchat+")";
		}else{
			requete="INSERT INTO Achat(userId,idClient,dateAchat,sommeTotal,sommeTotalReduit,idTypeAchat) values("+this.userId+","+this.clientId+",NOW(),"+this.sommeTotal+","+this.sommeTotalReduit+","+this.idTypeAchat+")";
		}
		
		if(new DB().update(requete)>0) {
			return CommonUtils.getLastId("Achat", "idAchat");
		}
		
		return -1;
	}
	
	public boolean deleteOrderedItems(int idAchat) {
		String req="DELETE FROM Panier where idAchat="+idAchat;
		//System.out.println(req);
		return new DB().update(req)>0;
	}
	
	public boolean delete() {
		String req="DELETE FROM Achat where idAchat = "+this.idAchat;
		//System.out.println(req);
		return new DB().update(req)>0;
	}
	
	public boolean deleteAchat() {
		
		deleteOrderedItems(this.idAchat); 
		new Facture().deleteFactureAchat(this.idAchat);
		new Credit().deleteCreditAchat(this.idAchat);
		return delete();
		
	}
	
	public Achat findAchat(int idAchat) {
		String requete="SELECT a.*,t.nomTypeAchat FROM Achat a JOIN TypeAchat t on a.idTypeAchat=t.idTypeAchat where idAchat="+idAchat;
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		Achat achat = null;
		Statement statement = null;
		
		try {		
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
		
			if(res.next()) {
				achat = new Achat();
				achat.idAchat=res.getInt("idAchat");
				achat.userId = res.getInt("userId");
				achat.clientId = res.getInt("idClient");
				achat.dateAchat = res.getString("dateAchat");
				Date d = res.getDate("dateAchat");
				achat.dateShort = CommonUtils.date2String(d, "dd/MM/yy");
				achat.sommeTotal = res.getLong("sommeTotal");
				achat.sommeTotalReduit = res.getLong("sommeTotalReduit");
				achat.idTypeAchat = res.getInt("idTypeAchat");
				achat.typeAchat = res.getString("nomTypeAchat");
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
			}catch(NullPointerException e){
				
			}
		}
		return achat;
	}
	
	public List<Achat> listAchatParJour(int userId) {
		String startDate = CommonUtils.date2String(new Date(),"yyyy-MM-dd")+" 00:00:00";
		String endDate = CommonUtils.date2String(new Date(),"yyyy-MM-dd")+" 23:59:59";
		String requete="SELECT * FROM Achat WHERE dateAchat BETWEEN '"+startDate+"' AND '"+endDate+"' and userId="+userId+" order by dateAchat desc";
		//System.out.println(requete);
		List<Achat> listAchat = new ArrayList<Achat>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Achat temp = new Achat();
				temp.idAchat=res.getInt("idAchat");
				temp.userId = res.getInt("userId");
				temp.clientId = res.getInt("idClient");
				temp.dateAchat = res.getString("dateAchat");
				Date d = res.getDate("dateAchat");
				temp.dateShort = CommonUtils.date2String(d, "dd/MM/yy");
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = this.typeAchatString(temp.idTypeAchat);
				temp.canceled = res.getBoolean("annuler");
				
				listAchat.add(temp);
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
			}catch(NullPointerException e) {
				
			}
		}
		return listAchat;
	}
	
	public int getNumeroFacture(int idAchat, int typeAchat) {
		String requete="SELECT count( idAchat ) nbFacture FROM Achat a JOIN Client c ON a.idClient = c.idClient WHERE c.idAssureur ="+typeAchat+" AND a.idAchat <="+idAchat;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			if(res.next()) {
				return res.getInt("nbFacture");
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
			}catch(NullPointerException e){
				
			}
		}
		return -1;
	}
	
	public List<Achat> achatAnnule() {
		
		String req = "SELECT * FROM Achat where annuler = 1 order by dateAchat desc";
		//System.out.println(req);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Achat> listAchat = new ArrayList<Achat>();
		
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			
			while(res.next()) {
				Achat temp = new Achat();
				temp.idAchat=res.getInt("idAchat");
				temp.userId = res.getInt("userId");
				temp.clientId = res.getInt("idClient");
				temp.dateAchat = res.getString("dateAchat");
				Date d = res.getDate("dateAchat");
				temp.dateShort = CommonUtils.date2String(d, "dd/MM/yy");
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = this.typeAchatString(temp.idTypeAchat);
				temp.canceled = res.getBoolean("annuler");
				
				listAchat.add(temp);
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
			} catch(NullPointerException e) {
				
			}
		}
		
		return listAchat;
	}
	
	public List<Achat> achatDuJour(int userId, Date jour) {
		if(jour==null)
			jour = new Date();
		String startDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 00:00:00";
		String endDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 23:59:59";
		String requete="SELECT * FROM Achat WHERE dateAchat BETWEEN '"+startDate+"' AND '"+endDate+"' and userId="+userId+" order by dateAchat desc";
		//System.out.println(requete);
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Achat> listAchat = new ArrayList<Achat>();
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Achat temp = new Achat();
				temp.idAchat=res.getInt("idAchat");
				temp.userId = res.getInt("userId");
				temp.clientId = res.getInt("idClient");
				temp.dateAchat = res.getString("dateAchat");
				Date d = res.getDate("dateAchat");
				temp.dateShort = CommonUtils.date2String(d, "dd/MM/yy");
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = this.typeAchatString(temp.idTypeAchat);
				temp.canceled = res.getBoolean("annuler");
				
				listAchat.add(temp);
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
			} catch(NullPointerException e) {
				
			}
		}
		return listAchat;
	}
	
	public List<Achat> achatDuJour(int userId, Date jour,int typeAchat) {
		if(jour==null)
			jour = new Date();
		String startDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 00:00:00";
		String endDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 23:59:59";
		String requete="SELECT a.*,t.nomTypeAchat FROM Achat a JOIN TypeAchat t on t.idTypeAchat=a.idTypeAchat WHERE dateAchat BETWEEN '"+startDate+"' AND '"+endDate+"'";
		if(userId!=0) {
			requete+="  and userId="+userId;
		}
		
		if(typeAchat!=0) {
			requete+=" AND a.idTypeAchat="+typeAchat;
		}
		
		requete+=" order by dateAchat desc";
		//System.out.println(requete);
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Achat> listAchat = new ArrayList<Achat>();
		
		try {
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Achat temp = new Achat();
				temp.idAchat=res.getInt("idAchat");
				temp.userId = res.getInt("userId");
				temp.clientId = res.getInt("idClient");
				temp.dateAchat = res.getString("dateAchat");
				Date d = res.getDate("dateAchat");
				temp.dateShort = CommonUtils.date2String(d, "dd/MM/yy");
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = res.getString("nomTypeAchat");
				temp.canceled = res.getBoolean("annuler");
				
				listAchat.add(temp);
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
			} catch(NullPointerException e) {
				
			}
		}
		return listAchat;
	}
	
	public List<Achat> sommeAchatDuJour(int userId, Date jour,int typeAchat) {
		if(jour==null)
			jour = new Date();
		String startDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 00:00:00";
		String endDate = CommonUtils.date2String(jour,"yyyy-MM-dd")+" 23:59:59";
		String requete="SELECT sum(a.sommeTotal) sommeTotal,sum(a.sommeTotalReduit) sommeTotalReduit,a.idTypeAchat, t.nomTypeAchat FROM Achat a JOIN TypeAchat t on t.idTypeAchat=a.idTypeAchat WHERE dateAchat BETWEEN '"+startDate+"' AND '"+endDate+"'";
		if(userId!=0) {
			requete+="  and userId="+userId;
		}
		
		if(typeAchat!=0) {
			requete+=" AND a.idTypeAchat="+typeAchat;
		}
		requete+=" group by idTypeAchat ";
		
		requete+=" order by dateAchat,idTypeAchat desc";
		//System.out.println(requete);
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Achat> listAchat = new ArrayList<Achat>();
		
		
		try {
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Achat temp = new Achat();
				
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = res.getString("nomTypeAchat");
				
				listAchat.add(temp);
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
			} catch(NullPointerException e) {
				
			}
		}
		return listAchat;
	}
	
	public List<Achat> sommeAchatDuMois(int userId, Calendar jour,int typeAchat) {
		if(jour==null)
			jour = Calendar.getInstance();
		
		int year = jour.get(Calendar.YEAR);
		int month = jour.get(Calendar.MONTH) + 1;
		int startDay = 1;
		int endDay = jour.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String startDate = year+"-"+month+"-"+startDay+" 00:00:00";
		String endDate = year+"-"+month+"-"+endDay+" 23:59:59";
		String requete="SELECT sum(a.sommeTotal) sommeTotal,sum(a.sommeTotalReduit) sommeTotalReduit,a.idTypeAchat, t.nomTypeAchat FROM Achat a JOIN TypeAchat t on t.idTypeAchat=a.idTypeAchat WHERE dateAchat BETWEEN '"+startDate+"' AND '"+endDate+"'";
		if(userId!=0) {
			requete+="  and userId="+userId;
		}
		
		if(typeAchat!=0) {
			requete+=" AND a.idTypeAchat="+typeAchat;
		}
		requete+=" group by idTypeAchat ";
		
		requete+=" order by dateAchat,idTypeAchat desc";
		//System.out.println(requete);
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		List<Achat> listAchat = new ArrayList<Achat>();
		
		try {
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Achat temp = new Achat();
				
				temp.sommeTotal = res.getLong("sommeTotal");
				temp.sommeTotalReduit = res.getLong("sommeTotalReduit");
				temp.idTypeAchat = res.getInt("idTypeAchat");
				temp.typeAchat = res.getString("nomTypeAchat");
				
				listAchat.add(temp);
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
			} catch (NullPointerException e) {
				
			}
		}
		return listAchat;
	}
	
	public String findTypeAchat(int idAchat) {
		String requete="SELECT t.nomTypeAchat FROM Credit c join Client cl " +
				"on c.idClient=cl.idClient JOIN TypeAchat t on t.idTypeAchat = cl.idAssureur where idAchat="+idAchat;
		
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				return res.getString("nomTypeAchat");
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
			}catch(NullPointerException e) {
				
			}
		}
		return "";
	}
	
	private String typeAchatString(int typeAchat) {
		for(Assureur s : this.getListAssureurs()) {
			if(s.getIdAssureur() == typeAchat)
				return s.getNomAssureur();
		}
		return "";
	}
	
	private void fetchMedicament() {
		String requete="SELECT p.idPanier,p.idStock,p.idAchat,p.quantite as quantiteAchete,p.prixUnitaire,p.prixTotal,p.prixTotalReduit,p.specialiteRemboursable,p.idStockRemboursable,p.idStockRemboursable,s.batch,m.codeMedicament,m.codeGenerique,m.nomMedicament,m.type FROM Panier p JOIN " +
				" Stock s on s.idStock=p.idStock JOIN Medicament m on m.idMedicament=s.idMedicament WHERE idAchat="+this.idAchat;
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		if(this.medicamentsAchete == null) {
			this.medicamentsAchete = new ArrayList<Medicament>();
		}
		this.medicamentsAchete.clear();
		try {
			
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.setCodeMedicament(res.getString("codeMedicament"));
				m.setcodeGenerique(res.getString("codeGenerique"));
				m.setIdStock(res.getInt("idStock"));
				m.setNomMedicament(res.getString("nomMedicament"));
				m.setQuantiteAchete(res.getInt("quantiteAchete"));
				m.setSpecialiteRemboursable(res.getBoolean("specialiteRemboursable"));
				m.setIdStockRemboursable(res.getInt("idStockRemboursable"));
				int prixReduit = 0;
				if(res.getInt("quantiteAchete")!=0) //avoid division by zero
					prixReduit = res.getInt("prixTotalReduit") / res.getInt("quantiteAchete");
				m.setBatch(res.getString("batch"));
				m.setPrixReduit(prixReduit);
				m.setPrixTotalReduit(res.getInt("prixTotalReduit"));
				m.setPrixTotal(res.getInt("prixTotal"));
				m.setPrix(res.getInt("prixUnitaire"));
				m.setTypeMedic(res.getString("type"));				
								
				String typePayement=this.findTypeAchat(res.getInt("idAchat"));
				
				m.setTypePayement(typePayement.equals("")?"Cash":typePayement);
				
				
				this.medicamentsAchete.add(m);
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
	
	
	private long computeSoldAmount() {
		long sum=0;
		if(this.medicamentsAchete==null)
			return 0;
		for(Medicament m : this.medicamentsAchete) {
			sum+=m.getPrixTotal();
		}
		return sum;
	}
	
	private long computeSoldAmountReduit() {
		long sum=0;
		if(this.medicamentsAchete==null)
			return 0;
		for(Medicament m : this.medicamentsAchete) {
			sum+=m.getPrixTotalReduit();
		}
		return sum;
	}
	
	public boolean demanderAnnulation() {
		String req="UPDATE Achat set annuler = 1 where idAchat="+this.idAchat;
		
		return new DB().update(req)>0;
	}
	
	public boolean refuserAnnulation() {
		String req="update Achat set annuler = 0 where idAchat = "+this.idAchat;
		
		return new DB().update(req)>0;
	}
	
	//getters and setters
	public String getDateAchatString() {
		if(this.dateAchat==null)
			return null;
		return this.dateAchat.substring(this.dateAchat.indexOf(" "));
	}
	public int getUserId() {
		return userId;
	}
	
	public List<Medicament> getMedicamentsAchete() {
		
			this.fetchMedicament();		
		return medicamentsAchete;
	}

	public void setMedicamentsAchete(List<Medicament> medicamentAchete) {
		this.medicamentsAchete = medicamentAchete;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(String dateAchat) {
		this.dateAchat = dateAchat;
	}
	public long getSommeTotal() {
		return sommeTotal;
	}
	public void setSommeTotal(long sommeTotal) {
		this.sommeTotal = sommeTotal;
	}
	
	public int getIdAchat() {
		return idAchat;
	}

	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}

	public String getTypeAchat() {
		if(typeAchat==null) {
			typeAchat = this.typeAchatString(this.idTypeAchat);
		}
		return typeAchat;
	}

	public void setTypeAchat(String typeAchat) {
		this.typeAchat = typeAchat;
	}	

	public long getSommeVente() {
		if(this.medicamentsAchete!=null)
			sommeVente=computeSoldAmount();
		return sommeVente;
	}
	
	public long getSommeVenteReduit() {
		if(this.medicamentsAchete!=null)
			sommeVenteReduit=computeSoldAmountReduit();
		return sommeVenteReduit;
	}

	public void setSommeVenteReduit(long sommeVenteReduit) {
		this.sommeVenteReduit = sommeVenteReduit;
	}

	public long getSommeVenteAssureur() {
		sommeVenteAssureur = getSommeVente() - getSommeVenteReduit();
		return sommeVenteAssureur;
	}

	public void setSommeVenteAssureur(long sommeVenteAssureur) {
		this.sommeVenteAssureur = sommeVenteAssureur;
	}

	public void setSommeVente(long sommeVente) {
		this.sommeVente = sommeVente;
	}
	
	public long getSommeTotalReduit() {
		return sommeTotalReduit;
	}

	public void setSommeTotalReduit(long sommeTotalReduit) {
		this.sommeTotalReduit = sommeTotalReduit;
	}
	
	public boolean isVenteCash() {
		venteCash = this.getTypeAchat().equalsIgnoreCase("Cash");
		
		return venteCash;
	}

	public void setVenteCash(boolean venteCash) {
		this.venteCash = venteCash;
	}

	public String getDateShort() {
		return dateShort;
	}

	public void setDateShort(String dateShort) {
		this.dateShort = dateShort;
	}
	
	public List<Assureur> getListAssureurs() {
		if(listAssureurs == null) {
			listAssureurs = new Assureur().listAssureur();
		}
		return listAssureurs;
	}

	public void setListAssureurs(List<Assureur> listAssureurs) {
		this.listAssureurs = listAssureurs;
	}
	
	public int getIdTypeAchat() {
		return idTypeAchat;
	}

	public void setIdTypeAchat(int idTypeAchat) {
		this.idTypeAchat = idTypeAchat;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}





	//member variables
	private int idAchat;
	private int userId;
	private int clientId;
	private String dateAchat;
	private String dateShort;
	private long sommeTotal;
	private long sommeTotalReduit;
	private boolean venteCash;
	private long sommeVente;
	private long sommeVenteReduit;
	private long sommeVenteAssureur;
	private String typeAchat;
	private int idTypeAchat;
	private boolean canceled;
	private List<Medicament> medicamentsAchete;
	private List<Assureur> listAssureurs;

}
