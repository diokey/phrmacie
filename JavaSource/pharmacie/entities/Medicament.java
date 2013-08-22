package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class Medicament implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//methods
	
	/**
	 * Method that returns the last idStock of the medicament id
	 */
	
	public static int findLastIdStock(int idMedicament) {
		String requete="SELECT idStock FROM Stock where idMedicament="+idMedicament+" order by idStock desc limit 1";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		int rep = 0;
		
		System.out.println(requete);
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			if(res.next()) {
				rep = res.getInt("idStock");
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
	
	/*
	 * Method that find the generic litteral name depending on the selected Medicament
	 */
	public String getGeneriqueString() {
		String requete="SELECT nomMedicament,codeMedicament FROM Medicament" +
				" WHERE codeMedicament='"+this.codeGenerique+"' limit 1";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		//System.out.println(requete);
		String rep="";
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				rep+=res.getString("codeMedicament");
				rep+="-"+res.getString("nomMedicament");
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
			}catch (Exception e) {
				
			}
		}
		return rep;
	}
	
	public static int getIdMedicamentByCode(String code) {
		String requete="SELECT idMedicament FROM Medicament " +
				"where codeMedicament='"+code+"' limit 1";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		int rep = 0;
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			if(res.next()) {
				rep = res.getInt("idMedicament");
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
	
	public static int getIdMedicamentByName(String medicName) {
		String requete="SELECT idMedicament FROM Medicament " +
				"where nomMedicament='"+medicName+"' limit 1";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		int rep = 0;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				rep = res.getInt("idMedicament");
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
	
	public static Medicament getMedicamentByCode(String code) {
		String requete="SELECT idMedicament,codeMedicament,nomMedicament,type,codeGenerique FROM Medicament " +
		"where codeMedicament='"+code+"' limit 1";
		
		//System.out.println(requete);
		Medicament m = null;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				m = new Medicament();
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.nomMedicament = res.getString("nomMedicament");
				m.typeMedic = res.getString("type");
				m.codeMedicament = res.getString("codeMedicament");
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
		return m;
	}
	
	/*
	 * Method thats add the Medication into the table Medicament
	 * @param null
	 * @return returns a true if success false if failed
	 */
	public int addMedicament() {
		String requete;
		PreparedStatement pst = null;
		if(!this.isSpecial()) {
			requete="INSERT INTO Medicament(codeMedicament,nomMedicament,type)" +
				" values(?,?,?)";
			try {
				pst = SingletonConnection.getInstance().prepareStatement(requete);
				pst.setString(1, this.codeMedicament);
				pst.setString(2, this.nomMedicament);
				pst.setString(3, this.typeMedic);
				
				if(pst.executeUpdate()>0) {
					int idMedicament = CommonUtils.getLastId("Medicament", "idMedicament");
					
					return putIntoStock(idMedicament);
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
				}
			}
		}else{
			if(this.codeGenerique.equalsIgnoreCase(""))
				this.codeGenerique=null;
			requete="INSERT INTO Medicament(codeMedicament,nomMedicament,type,codeGenerique)" +
			" values(?,?,?,?)";
			try {
				pst = SingletonConnection.getInstance().prepareStatement(requete);
				pst.setString(1, this.codeMedicament);
				pst.setString(2, this.nomMedicament);
				pst.setString(3, this.typeMedic);
				pst.setString(4, this.codeGenerique);
				
				if(pst.executeUpdate()>0) {
					int idMedicament = CommonUtils.getLastId("Medicament", "idMedicament");
					
					return putIntoStock(idMedicament);
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
				}
			}
		}
		return 0;
	}
	
	/*
	 * Method that put medication into the stock
	 * @param idMedicament which is the medicament id
	 * @return boolean if success
	 */
	public int putIntoStock(int idMedicament) {
		String requete="INSERT INTO Stock(idMedicament,quantite,batch,manifactureDate,expiryDate,dateArrive)" +
				" VALUES("+idMedicament+","+this.quantiteStock+",'"+this.batch+"','"
				+CommonUtils.date2String(this.manifactureDate)+"','"
				+CommonUtils.date2String(this.expiryDate)+"','"
				+CommonUtils.date2String(this.dateArrive)+"')";
		//System.out.println(requete);
		if(new DB().update(requete)>0){
			return CommonUtils.getLastId("Stock","idStock");
		}
		return 0;
	}
	
	public boolean restoreMedicine() {
		String requete="UPDATE Stock set quantite=quantite+"+this.getQuantiteAchete()+" where IdStock="+this.getIdStock();
		//System.out.println(requete);
		
		return new DB().update(requete)>0;
	}
	
	
	/*
	 * Method that saves sold items into the shopping cart
	 * @return true if saved and store update, false if failed
	 */
	public boolean putIntoCart(int idAchat) {
		if(this.idStockRemboursable!=null && this.idStockRemboursable==0)
			this.idStockRemboursable=null;
		
		String requete="INSERT INTO Panier(idStock,quantite,prixUnitaire,prixTotal,prixTotalReduit,idAchat,specialiteRemboursable,idStockRemboursable)" +
				" values("+this.idStock+","+this.quantiteAchete+","+this.prix+","+this.prixTotal+","+this.prixTotalReduit+","+idAchat+","+this.specialiteRemboursable+","+this.idStockRemboursable+")";
		
		return new DB().update(requete)>0 && updateStock();
	}
	
	/**
	 * @param idCommande
	 * @return true if successful false otherwise
	 * Method that saves medics when ordering medics from a provider not a client
	 */
	public boolean saveMedicamentCommande(int idCommande) {
		String requete="INSERT INTO MedicamentCommande(idMedicament,quantite,prixUnitaire,prix,	idCommande) " +
				"values("+this.idMedicament+","+this.quantiteAchete+","+this.prixAchat+","+this.prixTotalAchat+","+idCommande+")";
		return new DB().update(requete)>0;
	}
	
	/**
	 * @parm idFourniture
	 * @return true is successfully saved otherwise returns false
	 * 
	 */
	public boolean saveMedicamentFournis(int idFourniture) {
		String requete="INSERT INTO MedicamentFournis(idMedicament,quantite,prixUnitaire,prix,idFourniture) " +
				"values("+this.idMedicament+","+this.quantiteFournis+","+this.prixAchat+","+this.prixTotalFourniture+","+idFourniture+")";
		return new DB().update(requete)>0;
	}
	
	
	/*
	 * Update stock depending on the medics sold
	 * @param null
	 * @return true if success and false if failed
	 */
	public boolean updateStock(){
		
		String requete="UPDATE Stock set quantite=quantite - "+this.quantiteAchete+" where idStock="+this.idStock;
		
		return new DB().update(requete)>0;
	}
	
	public boolean update() {
		
		if(this.codeGenerique!=null && !this.codeGenerique.isEmpty() && !this.codeGenerique.equalsIgnoreCase("null")) {
		
			String requete="UPDATE Medicament set codeMedicament=?,nomMedicament=?,type=?,codeGenerique=? where idMedicament = "+this.idMedicament;
			PreparedStatement pst = null;
			try {
				pst = SingletonConnection.getInstance().prepareStatement(requete);
				pst.setString(1,this.codeMedicament);
				pst.setString(2, this.nomMedicament);
				pst.setString(3, this.typeMedic);
				pst.setString(4, this.codeGenerique);
				
				return pst.executeUpdate()>0;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(Exception e) {
					
				}
			}
			
			return false;
		}else{
			
			String requete="UPDATE Medicament set codeMedicament=?,nomMedicament=?,type=? where idMedicament = "+this.idMedicament;
			PreparedStatement pst = null;	
			try {
				pst = SingletonConnection.getInstance().prepareStatement(requete);
				pst.setString(1,this.codeMedicament);
				pst.setString(2, this.nomMedicament);
				pst.setString(3, this.typeMedic);
				
				return pst.executeUpdate()>0;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(Exception e) {
					
				}
			}
			
			return false;
		}
		
	}
	
	public boolean updateStockRow() {
		
		String requete="UPDATE Stock set quantite='"+this.quantiteStock+"',batch=?,manifactureDate='"+CommonUtils.date2String(this.manifactureDate)+"',expiryDate='"+CommonUtils.date2String(this.expiryDate)+"',dateArrive='"+CommonUtils.date2String(this.dateArrive)+"' where idStock="+this.idStock;
		PreparedStatement pst = null;
		boolean rep = false;
		try {
			pst = SingletonConnection.getInstance().prepareStatement(requete);
			pst.setString(1, this.batch);
			
			rep = pst.executeUpdate()>0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception e) {
				
			}
		}
		return rep;
	}
	
	public List<Medicament> generiqueDuMedicement(String codeMedicament, int typeAchat) {
		String requete="SELECT s.idStock,m.codeGenerique,m.codeMedicament,m.type,m.idMedicament,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,expiryDate,p.prix,p.reduction,p.idMention" +
		" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " +
		"join Prix p on s.idStock=p.idStock AND p.idTypeAchat="+typeAchat+" " +
				" where s.quantite > 0 and m.codeMedicament='"+codeMedicament+"' order by m.nomMedicament";
		
		List<Medicament> medicaments = new ArrayList<Medicament>();
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;

		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.quantiteStock = res.getInt("quantite");
				m.batch = res.getString("batch");
				m.manifactureDate = res.getDate("manifactureDate");
				m.expiryDate = res.getDate("expiryDate");
				m.prix = res.getInt("prix");
				m.reduction = res.getInt("reduction");
				m.dateArrive = res.getDate("dateArrive");
				m.typeMedic = res.getString("type");
				m.typePayement = new Assureur().findAssureur(typeAchat).getNomAssureur();
				
				m.idMention = res.getInt("idMention");
				
				
				medicaments.add(m);
			}
			
			return medicaments;
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
		return medicaments;
	}
	
	/**
	 * give us the list of the medicaments corresponding to the criterias.
	 * @param int indicating the shopping profile 1: cash, 2: mutuelle, 3, jubile, etc...
	 * @return List list of matching medications and its prices
	 */
	public List<Medicament> listMedicament(int typeAchat){
		
		List<Medicament> medicaments = new ArrayList<Medicament>();
		
		String requete="SELECT s.idStock,m.codeGenerique,m.codeMedicament,m.type,m.idMedicament,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,expiryDate,p.prix,p.reduction,p.idMention" +
				" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " +
				"join Prix p on s.idStock=p.idStock AND p.idTypeAchat="+typeAchat+" " +
						" where s.quantite > 0 order by m.nomMedicament";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.quantiteStock = res.getInt("quantite");
				m.batch = res.getString("batch");
				m.manifactureDate = res.getDate("manifactureDate");
				m.expiryDate = res.getDate("expiryDate");
				m.prix = res.getInt("prix");
				m.reduction = res.getInt("reduction");
				m.dateArrive = res.getDate("dateArrive");
				m.typeMedic = res.getString("type");
				m.typePayement = new Assureur().findAssureur(typeAchat).getNomAssureur();
				
				m.idMention = res.getInt("idMention");
				m.medicamentsGenerique = generiqueDuMedicement(m.codeGenerique, typeAchat);
				
				medicaments.add(m);
			}
			
			return medicaments;
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
		return medicaments;
	}
	
	public List<Medicament> listAllMedicament(int typeAchat) {
		List<Medicament> medicaments = new ArrayList<Medicament>();
		
		String requete="SELECT s.idStock,m.type,m.idMedicament,m.codeMedicament,m.codeGenerique,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,s.expiryDate,p.prix,p.reduction,p.idMention" +
				" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " +
				"join Prix p on s.idStock=p.idStock";
						
		
		if(typeAchat!=0) {
			requete+=" AND p.idTypeAchat="+typeAchat;
		}
		
		requete+=" order by m.nomMedicament";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.quantiteStock = res.getInt("quantite");
				m.batch = res.getString("batch");
				m.manifactureDate = res.getDate("manifactureDate");
				m.expiryDate = res.getDate("expiryDate");
				m.prix = res.getInt("prix");
				m.reduction = res.getInt("reduction");
				m.dateArrive = res.getDate("dateArrive");
				m.typeMedic = res.getString("type");
				m.idMention = res.getInt("idMention");
				//System.out.println(m.nomMedicament+" idMention: "+m.idMention);
				medicaments.add(m);
			}
			
			return medicaments;
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
		return medicaments;
	}
	
	/**
	 * 
	 * @param typeAchat
	 * @param all : boolean indicates if medics with 0 quantity should be included. false to exclude 0 values
	 * @return medication list
	 */
	public List<Medicament> listAllMedicament(int typeAchat,boolean all) {
		List<Medicament> medicaments = new ArrayList<Medicament>();
		
		String requete="SELECT s.idStock,m.type,m.idMedicament,m.codeMedicament,m.codeGenerique,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,s.expiryDate,p.prix,p.reduction,p.idMention" +
				" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " +
				"join Prix p on s.idStock=p.idStock";
						
		
		if(typeAchat!=0) {
			requete+=" AND p.idTypeAchat="+typeAchat;
		}
		
		if(!all) {
			requete+=" where s.quantite > 0";
		}
		
		requete+=" order by m.nomMedicament";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.quantiteStock = res.getInt("quantite");
				m.batch = res.getString("batch");
				m.manifactureDate = res.getDate("manifactureDate");
				m.expiryDate = res.getDate("expiryDate");
				m.prix = res.getInt("prix");
				m.reduction = res.getInt("reduction");
				m.dateArrive = res.getDate("dateArrive");
				m.typeMedic = res.getString("type");
				m.idMention = res.getInt("idMention");
				//System.out.println(m.nomMedicament+" idMention: "+m.idMention);
				m.listAssureur = new Assureur().getPrixAllAsseur(m.idStock);
				medicaments.add(m);
			}
			
			return medicaments;
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
		return medicaments;
	}
	
	
	public String getMentionString() {
		for(Mention m : this.getListMention()) {
			if(m.getIdMention()==this.idMention) {
				return m.getDescription();
			}
		}
		return "";
	}
	
	public String getMentionIcon() {
		for(Mention m : this.getListMention()) {
			if(m.getIdMention()==this.idMention) {
				return m.getIcone();
			}
		}
		return "";
	}
	
	public List<Medicament> listSuggestion() {
		String requete="SELECT s.idStock,m.idMedicament,m.codeGenerique,m.codeMedicament,m.nomMedicament,m.type,s.batch" +
		" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " +
		" GROUP BY m.idMedicament";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		List<Medicament> listSuggestion = new ArrayList<Medicament>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.typeMedic = res.getString("type");
				m.batch = res.getString("batch");
				m.listAssureur = new Assureur().getPrixAllAsseur(m.idStock);
				
				
				listSuggestion.add(m);
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
		return listSuggestion;
	}
	
	public List<Medicament> nomMedicaments() {
		String requete="SELECT m.idMedicament,m.codeGenerique,m.codeMedicament,m.nomMedicament,m.type" +
		" FROM Medicament m " ;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		List<Medicament> listMedics = new ArrayList<Medicament>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.typeMedic = res.getString("type");
				
				listMedics.add(m);
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
			}catch(Exception e) {
				
			}
			
		}
		return listMedics;
	}
	
	public List<Medicament> listMedicamentsGenerique() {
		String requete="SELECT m.idMedicament,m.codeGenerique,m.codeMedicament,m.nomMedicament,m.type" +
		" FROM Medicament m where type='generique'" ;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		Statement statement = null;
		List<Medicament> listMedics = new ArrayList<Medicament>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.typeMedic = res.getString("type");
				
				listMedics.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e) {
				
			}
			
		}
		return listMedics;
	}
	
	public List<Medicament> listPrix() {
		String requete="SELECT s.idStock,m.idMedicament,m.codeGenerique,m.codeMedicament,m.nomMedicament,m.type,s.batch" +
		" FROM Stock s join Medicament m on s.idMedicament=m.idMedicament " ;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		Statement statement = null;
		List<Medicament> listSuggestion = new ArrayList<Medicament>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				m.idStock = res.getInt("idStock");
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.typeMedic = res.getString("type");
				m.batch = res.getString("batch");
				m.listAssureur = new Assureur().getPrixAllAsseur(m.idStock);
				
				
				listSuggestion.add(m);
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
		return listSuggestion;
	}
	
	/**
	 * @param void
	 * @return list of medics and quantity in the stock.
	 */
	public List<Medicament> listPrixAchat(int idFournisseur) {
		String requete="SELECT m.idMedicament,m.codeGenerique,m.codeMedicament,m.nomMedicament,m.type,sum(s.quantite) quantite FROM Medicament m LEFT JOIN Stock s on s.idMedicament=m.idMedicament group by m.idMedicament order by quantite";
		
		System.out.print(requete);
		List<Medicament> listMedics = new ArrayList<Medicament>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Medicament m = new Medicament();
				
				m.idMedicament = res.getInt("idMedicament");
				m.codeGenerique = res.getString("codeGenerique");
				m.codeMedicament = res.getString("codeMedicament");
				m.nomMedicament = res.getString("nomMedicament");
				m.quantiteStock = res.getInt("quantite");
				m.typeMedic = res.getString("type");
				m.prixAchat = new PrixAchat().getPrixAchat(m.idMedicament,idFournisseur);
				
				listMedics.add(m);
				
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
		
		return listMedics;
	}
	
	/**
	 * 
	 * @param idCommande
	 * @return List<Medicament> list of medic ordered on the idCommande
	 */
	public static List<Medicament> fetchOrderDetails(int idCommande) {
		List<Medicament> result = new ArrayList<Medicament>();
		String requete="SELECT m.idMedicament,m.codeMedicament,m.nomMedicament,m.type,m.codeGenerique,mc.quantite,mc.prixUnitaire,mc.prix " +
				"FROM MedicamentCommande mc JOIN Medicament m on mc.idMedicament=m.idMedicament where idCommande = "+idCommande;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				Medicament m = new Medicament();
				m.setIdMedicament(res.getInt("idMedicament"));
				m.setCodeMedicament(res.getString("codeMedicament"));
				m.setNomMedicament(res.getString("nomMedicament"));
				m.setTypeMedic(res.getString("type"));
				m.setCodeGenerique(res.getString("codeGenerique"));
				m.setQuantiteAchete(res.getInt("quantite"));
				m.setPrixAchat(res.getInt("prixUnitaire"));
				m.setPrixTotalAchat(res.getInt("prix"));
				
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

	public Medicament getMedicament(String codeMedicament, int typeAchat) {
		Medicament found = null;
		
		String requete="SELECT s.idStock,m.type,m.idMedicament,m.codeMedicament,m.codeGenerique,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,s.expiryDate,p.prix,p.reduction,p.idMention " +
				"FROM Stock s JOIN Medicament m ON s.idMedicament=m.idMedicament " +
				"JOIN Prix p on p.idStock=s.idStock AND p.idTypeAchat="+typeAchat+" where m.codeMedicament='"+codeMedicament+"'";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res==null) {
				return null;
			}
			if(res.next()) {
				found = new Medicament();
				found.idStock = res.getInt("idStock");
				found.idMedicament = res.getInt("idMedicament");
				found.codeGenerique = res.getString("codeGenerique");
				found.codeMedicament = res.getString("codeMedicament");
				found.nomMedicament = res.getString("nomMedicament");
				found.quantiteStock = res.getInt("quantite");
				found.batch = res.getString("batch");
				found.manifactureDate = res.getDate("manifactureDate");
				found.expiryDate = res.getDate("expiryDate");
				found.prix = res.getInt("prix");
				found.reduction = res.getInt("reduction");
				found.dateArrive = res.getDate("dateArrive");
				found.typeMedic = res.getString("type");
				found.idMention = res.getInt("idMention");
				
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
		return found;
	}
	
	public Medicament getMedicamentRemboursable(int idStock,int typeAchat) {
		Medicament found = null;
		
		String requete="SELECT s.idStock,m.type,m.idMedicament,m.codeMedicament,m.codeGenerique,m.nomMedicament,s.quantite,s.batch,s.dateArrive,s.manifactureDate,s.expiryDate,p.prix,p.reduction,p.idMention " +
				"FROM Stock s JOIN Medicament m ON s.idMedicament=m.idMedicament " +
				"JOIN Prix p on p.idStock=s.idStock AND p.idTypeAchat="+typeAchat+" where s.idStock='"+idStock+"'";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res==null) {
				return null;
			}
			
			if(res.next()) {
				found = new Medicament();
				found.idStock = res.getInt("idStock");
				found.idMedicament = res.getInt("idMedicament");
				found.codeGenerique = res.getString("codeGenerique");
				found.codeMedicament = res.getString("codeMedicament");
				found.nomMedicament = res.getString("nomMedicament");
				found.quantiteStock = res.getInt("quantite");
				found.batch = res.getString("batch");
				found.manifactureDate = res.getDate("manifactureDate");
				found.expiryDate = res.getDate("expiryDate");
				found.prix = res.getInt("prix");
				found.reduction = res.getInt("reduction");
				found.dateArrive = res.getDate("dateArrive");
				found.typeMedic = res.getString("type");
				found.idMention = res.getInt("idMention");
				
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
			}
		}
		return found;
	}
	
	public Integer findMentionId(String mention) {
		for(Mention m : this.getListMention()) {
			if(m.getDescription()!=null) {
				if(m.getDescription().equalsIgnoreCase(mention)) {
					return m.getIdMention();
				}
			}
		}
		return null;
	}
	
	/*public String toString() {
		return this.nomMedicament+":"+this.batch+":"+CommonUtils.date2String(this.manifactureDate)+":"+
		CommonUtils.date2String(this.expiryDate)+":"+this.typePayement+":"+
		this.prix+":"+this.quantiteStock+":"+this.quantiteAchete+":"+this.idStock+":"+this.prixTotal;
	}*/
	
	//constructor
	public Medicament(){
		
	}
		
	public Medicament(Medicament copy) {
		batch = copy.batch;         
		manifactureDate = copy.manifactureDate; 
		expiryDate = copy.expiryDate;      
		dateArrive = copy.dateArrive;      
		typePayement = copy.typePayement;  
		prix = copy.prix;         
		quantiteStock = copy.quantiteStock;
		quantiteAchete = copy.quantiteAchete;
		idStock = copy.idStock;      
		prixTotal = copy.prixTotal;    
		idMedicament = copy.idMedicament;     
		codeGenerique = copy.codeGenerique; 
		nomMedicament = copy.nomMedicament; 
		codeMedicament = copy.codeMedicament;
		typeMedic = copy.typeMedic;     
		nomMedicShort = copy.nomMedicament; 
		generique = copy.generique;     
		exists = copy.exists;       
		special = copy.special; 
		listMention = copy.listMention;
		listAssureur = copy.listAssureur;
		prixTotalReduit = copy.prixTotalReduit;
		reduction = copy.reduction;
		idMedicament = copy.idMention;
		specialiteRemboursable = copy.specialiteRemboursable;
		medicamentsGenerique = copy.medicamentsGenerique;
		idStockRemboursable = copy.idStockRemboursable;
	}
	
	//getters and setters
	
	
	//member variables
	
	public String getNomMedicament() {
		return nomMedicament;
	}
	public void setNomMedicament(String nomMedicament) {
		this.nomMedicament = nomMedicament;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	public String getTypePayement() {
		return typePayement;
	}
	public void setTypePayement(String typePayement) {
		this.typePayement = typePayement;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	
	public int getPrixReduit() {
		if(prixReduit==0 && this.reduction!=null)
			prixReduit = prix * (100 - this.reduction) / 100;
		return prixReduit;
	}

	public void setPrixReduit(int prixReduit) {
		this.prixReduit = prixReduit;
	}

	public int getPrixAssureur() {
		
		return prixTotal - prixTotalReduit;
	}
	public Date getManifactureDate() {
		return manifactureDate;
	}

	public void setManifactureDate(Date manifactureDate) {
		this.manifactureDate = manifactureDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getQuantiteStock() {
		return quantiteStock;
	}

	public void setQuantiteStock(Integer quantiteStock) {
		this.quantiteStock = quantiteStock;
	}

	public Integer getQuantiteAchete() {
		return quantiteAchete;
	}

	public void setQuantiteAchete(Integer quantiteAchete) {
		this.quantiteAchete = quantiteAchete;
	}

	public Integer getIdStock() {
		return idStock;
	}

	public void setIdStock(Integer idStock) {
		this.idStock = idStock;
	}

	public int getPrixUnitaireAssureur() {
		return this.getPrix() - this.getPrixReduit();
	}
	public int getPrixTotal() {
		if(this.quantiteAchete!=null)
		prixTotal=this.quantiteAchete * this.prix;
		return this.prixTotal;
	}

	public void setPrixTotal(int prixTotal) {
		this.prixTotal = prixTotal;
	}
	
	public Integer getPrixTotalReduit() {
		if(this.quantiteAchete!=null) {
			
			prixTotalReduit = this.quantiteAchete * getPrixReduit();
		}
		return prixTotalReduit;
	}

	public void setPrixTotalReduit(Integer prixTotalReduit) {
		this.prixTotalReduit = prixTotalReduit;
	}

	public String getCodeGenerique() {
		return codeGenerique;
	}

	public void setCodeGenerique(String codeGenerique) {
		this.codeGenerique = codeGenerique;
	}

	public Date getDateArrive() {
		return dateArrive;
	}
	public void setDateArrive(Date dateArrive) {
		this.dateArrive = dateArrive;
	}
	
	public String getCodeMedicament() {
		return codeMedicament;
	}
	
	public void setCodeMedicament(String codeMedicament) {
		this.codeMedicament = codeMedicament;
	}

	public String getTypeMedic() {
		return typeMedic;
	}

	public void setTypeMedic(String typeMedic) {
		this.typeMedic = typeMedic;
	}
	
	public List<Mention> getListMention() {
		if(this.listMention == null)
			this.listMention = new Mention().listMention();
		return listMention;
	}

	public void setListMention(List<Mention> listMention) {
		this.listMention = listMention;
	}

	public String getNomMedicShort() {
		if(!this.nomMedicament.isEmpty()){
			if(this.nomMedicament.length()>12)
				this.nomMedicShort = this.nomMedicament.substring(0, 9)+"...";
			else
				this.nomMedicShort = this.nomMedicament;
		}
		return nomMedicShort;
	}

	public void setNomMedicShort(String nomMedicShort) {
		this.nomMedicShort = nomMedicShort;
	}
	
	public List<Assureur> getListAssureur() {
		if(this.listAssureur == null)
			this.listAssureur = new Assureur().getPrixAllAsseur(0);
		return listAssureur;
	}

	public void setListAssureur(List<Assureur> listAssureur) {
		this.listAssureur = listAssureur;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	public int getIdMedicament() {
		return idMedicament;
	}

	public void setIdMedicament(int idMedicament) {
		this.idMedicament = idMedicament;
	}

	public boolean isSpecial() {
		special = this.typeMedic==null?false:typeMedic.equalsIgnoreCase("specialite")?true:false;
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public String getcodeGenerique() {
		return codeGenerique;
	}

	public void setcodeGenerique(String codeGenerique) {
		this.codeGenerique = codeGenerique;
	}
	public String getGenerique() {
		if(generique==null)
			generique = this.getGeneriqueString();
		return generique;
	}

	public void setGenerique(String generique) {
		this.generique = generique;
	}

	public Integer getReduction() {
		return reduction;
	}

	public void setReduction(Integer reduction) {
		this.reduction = reduction;
	}

	public int getIdMention() {
		return idMention;
	}

	public void setIdMention(int idMention) {
		this.idMention = idMention;
	}

	public boolean isWithGenerique() {
		withGenerique = this.codeGenerique!=null && !this.codeGenerique.isEmpty();
		return withGenerique;
	}

	public void setWithGenerique(boolean withGenerique) {
		this.withGenerique = withGenerique;
	}

	public boolean isRemboursable() {
		remboursable = this.codeGenerique!=null && !this.codeGenerique.isEmpty() && getMentionIcon().equalsIgnoreCase("/images/refresh16.png");
		return remboursable;
	}

	public void setRemboursable(boolean remboursable) {
		this.remboursable = remboursable;
	}
	

	public boolean isSpecialiteRemboursable() {
		return specialiteRemboursable;
	}

	public void setSpecialiteRemboursable(boolean specialiteRemboursable) {
		this.specialiteRemboursable = specialiteRemboursable;
	}
	

	public List<Medicament> getMedicamentsGenerique() {
		return medicamentsGenerique;
	}

	public void setMedicamentsGenerique(List<Medicament> medicamentsGenerique) {
		this.medicamentsGenerique = medicamentsGenerique;
	}

	public Integer getIdStockRemboursable() {
		return idStockRemboursable;
	}

	public void setIdStockRemboursable(Integer idStockRemboursable) {
		this.idStockRemboursable = idStockRemboursable;
	}
	
	public int getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(int prixAchat) {
		this.prixAchat = prixAchat;
	}
	
	public int getPrixTotalAchat() {
		prixTotalAchat = this.prixAchat * this.quantiteAchete;
		return prixTotalAchat;
	}

	public void setPrixTotalAchat(int prixTotalAchat) {
		this.prixTotalAchat = prixTotalAchat;
	}

	public int getQuantiteFournis() {
		return quantiteFournis;
	}

	public void setQuantiteFournis(int quantiteFournis) {
		this.quantiteFournis = quantiteFournis;
	}

	public Long getPrixTotalFourniture() {
		
		prixTotalFourniture = (long) (this.quantiteFournis * this.prixAchat);
		
		return prixTotalFourniture;
	}

	public void setPrixTotalFourniture(Long prixTotalFourniture) {
		this.prixTotalFourniture = prixTotalFourniture;
	}
	

	public Integer getQuantiteRestante() {
		if(this.quantiteAchete!=null && this.quantiteRestante==null)
			return quantiteRestante = this.quantiteAchete - this.quantiteFournis;
		return quantiteRestante;
	}

	public void setQuantiteRestante(Integer quantiteRestante) {
		this.quantiteRestante = quantiteRestante;
	}






	private String batch;
	private Date manifactureDate;
	private Date expiryDate;
	private Date dateArrive;
	private String typePayement;
	private int prix;
	private int prixAchat;
	private int prixTotalAchat;
	private int prixReduit;
	private Integer reduction;
	private Integer quantiteStock;
	private Integer quantiteAchete;
	private int quantiteFournis;
	private Integer quantiteRestante;
	private Integer idStock;
	private Integer idStockRemboursable;
	private int prixTotal;
	private Integer prixTotalReduit;
	private Long prixTotalFourniture;
	private int idMedicament;
	private String codeGenerique;
	private String nomMedicament;
	private String codeMedicament;
	private String typeMedic;
	private String nomMedicShort;
	private String generique;
	private boolean exists;
	private boolean special;
	private boolean withGenerique;
	private boolean remboursable;
	private int idMention;
	private boolean specialiteRemboursable;
	
	
	private List<Mention> listMention;
	private List<Assureur> listAssureur;
	private List<Medicament> medicamentsGenerique;
}
