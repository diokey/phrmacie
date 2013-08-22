package pharmacie.entities;

import pharmacie.connection.DB;

public class Facture {

	public Facture() {
		// TODO Auto-generated constructor stub
	}

	//methods
	
	public boolean save() {
		String requete="INSERT INTO Facture(idAchat,sommePaye,sommeRestante) values("+this.idAchat+","+this.sommePaye+","+this.sommeRestante+")";
		
		return new DB().update(requete)>0;
	}
	
	public boolean deleteFactureAchat(int idAchat) {
		String req="DELETE FROM Facture where idAchat="+idAchat;
		//System.out.println(req);
		return new DB().update(req)>0;
	}
	
	//getters and setters
	
	public int getIdAchat() {
		return idAchat;
	}
	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
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

	//private properties
	private int idAchat;
	private long sommePaye;
	private long sommeRestante;
}
