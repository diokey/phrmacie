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

public class Payement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Payement() {
		
	}
	
	
	public List<Payement> getListPayement() {
		List<Payement> result = new ArrayList<Payement>();
		
		String req = "SELECT * FROM PayementFournisseur where idFourniture = "+this.idFourniture;
		
		//System.out.println(req);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
	
		Payement p = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			while(res.next()) {
				p = new Payement();
				p.idPayement = res.getInt("idPayementFournisseur");
				p.idFourniture = res.getInt("idFourniture");
				p.sommePaye = res.getLong("sommePaye");
				p.sommeRestante = res.getLong("sommeRestante");
				p.datePayement = res.getDate("datePayement");
				
				result.add(p);
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
	
	
	public int getIdPayement() {
		return idPayement;
	}
	public void setIdPayement(int idPayement) {
		this.idPayement = idPayement;
	}
	public int getIdFourniture() {
		return idFourniture;
	}
	public void setIdFourniture(int idFourniture) {
		this.idFourniture = idFourniture;
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
	public Date getDatePayement() {
		return datePayement;
	}
	public void setDatePayement(Date datePayement) {
		this.datePayement = datePayement;
	}


	private int idPayement;
	private int idFourniture;
	private long sommePaye;
	private long sommeRestante;
	private Date datePayement;
}
