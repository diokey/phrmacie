package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;

public class Prix implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Prix() {
		// TODO Auto-generated constructor stub
		this.mention = new Mention();
	}
	
	public boolean savePrix() {
		if(this.idMention!=null && this.idMention==0)
			this.idMention = null;
		if(this.reduction==null) 
			this.reduction=0;
		
		String requete="INSERT INTO Prix(idStock,idTypeAchat,prix,reduction,idMention)" +
				" values("+this.idStock+","+this.idTypeAchat+","+this.prix+","+this.reduction+","+this.idMention+")";
		//System.out.println(requete);
		if(this.prix>0)
			return new DB().update(requete)>0;
		return true;
	}
	
	public boolean updatePrix() {
		if(this.idMention!=null && this.idMention==0)
			this.idMention = null;
		String requete="UPDATE Prix SET idTypeAchat="+this.idTypeAchat+",prix="+this.prix+",reduction="+this.reduction+",idMention="+this.idMention+" " +
				"WHERE idPrix="+this.idPrix;
		//System.err.println(requete);
		if(this.prix>0)
			return new DB().update(requete)>0;
		return true;
	}
	
	
	public Prix getPrixMedicament(int idStock, int typeAchat) {
		
		String requete="SELECT p.idStock,p.idTypeAchat,p.idPrix,p.prix,p.reduction,p.idMention,m.description FROM Prix p LEFT JOIN Mention m on p.idMention=m.idMention where idStock="+idStock+" and idTypeAchat="+typeAchat;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		Prix p = new Prix();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				
				p.idStock = res.getInt("idStock");
				p.idPrix = res.getInt("idPrix");
				p.idTypeAchat = res.getInt("idTypeAchat");
				p.prix = res.getLong("prix");
				p.reduction = res.getInt("reduction");
				p.idMention = res.getInt("idMention");
				p.mentionString = res.getString("description");
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
		
		return p;
	}
	

	public long getPrix() {
		return prix;
	}
	public void setPrix(long prix) {
		this.prix = prix;
	}
	public int getIdStock() {
		return idStock;
	}
	public void setIdStock(int idStock) {
		this.idStock = idStock;
	}
	public int getIdTypeAchat() {
		return idTypeAchat;
	}
	public void setIdTypeAchat(int idTypeAchat) {
		this.idTypeAchat = idTypeAchat;
	}
	public Integer getReduction() {
		return reduction;
	}
	public void setReduction(Integer reduction) {
		this.reduction = reduction;
	}
	
	public String getMentionString() {
		return mentionString;
	}

	public void setMentionString(String mentionString) {
		this.mentionString = mentionString;
	}

	public Mention getMention() {
		return mention;
	}

	public void setMention(Mention mention) {
		this.mention = mention;
	}

	public String getNomAssureur() {
		return nomAssureur;
	}

	public void setNomAssureur(String nomAssureur) {
		this.nomAssureur = nomAssureur;
	}
	
	public Integer getIdMention() {
		return idMention;
	}

	public void setIdMention(Integer idMention) {
		this.idMention = idMention;
	}
	
	public int getIdPrix() {
		return idPrix;
	}

	public void setIdPrix(int idPrix) {
		this.idPrix = idPrix;
	}




	private long prix;
	private int idPrix;
	private int idStock;
	private int idTypeAchat;
	private Integer reduction;
	private Integer idMention;
	private String mentionString;
	private String nomAssureur;
	private Mention mention;
}
