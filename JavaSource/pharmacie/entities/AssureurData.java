package pharmacie.entities;

public class AssureurData {

	public AssureurData() {
		// TODO Auto-generated constructor stub
	}
	
	public long getPrix() {
		return prix;
	}
	public void setPrix(long prix) {
		this.prix = prix;
	}
	public Integer getReduction() {
		return reduction;
	}
	public void setReduction(Integer reduction) {
		this.reduction = reduction;
	}
	public String getMention() {
		return mention;
	}
	public void setMention(String mention) {
		this.mention = mention;
	}

	public String getNomAssureur() {
		return nomAssureur;
	}

	public void setNomAssureur(String nomAssureur) {
		this.nomAssureur = nomAssureur;
	}



	private long prix;
	private Integer reduction;
	private String mention;
	private String nomAssureur;
	
}
