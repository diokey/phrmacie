package pharmacie.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import pharmacie.entities.Achat;
import pharmacie.entities.Assureur;
import pharmacie.entities.Client;
import pharmacie.entities.Credit;
import pharmacie.entities.Medicament;

@ManagedBean
@SessionScoped
public class CreditBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CreditBean() {
		// TODO Auto-generated constructor stub
	}

	//methods
	public void checkVente() {
		this.achat = null;
		final List<Achat> listAchat = new VenteBean().getListAchatDuJour();
		for(final Achat a : listAchat) {
			if(a.getIdAchat()== this.idAchat) {
				this.achat = a;
				
			}
		}
		
		if(this.achat==null) {
			
			this.achat = new Achat().findAchat(this.idAchat);
			if(this.achat == null)
				return;
			
		}
		
		this.venteCash = this.achat.getTypeAchat().equalsIgnoreCase("Cash");
		//System.out.println("achat: "+this.achat.getTypeAchat()+" "+this.venteCash+": "+this.idAchat);
	}
	
		
	public void fillFactureData() {
		final VenteBean source = new VenteBean();
		if(this.achat==null) {
			checkVente();		
		}
		
		final List<Assureur> listAssureur = source.getAssureurs();
		for(Assureur a : listAssureur) {
			if(a.getNomAssureur().equalsIgnoreCase(this.achat.getTypeAchat())) {
				this.assureur = a;
				System.out.println("Assureur : "+this.assureur.getNomAssureur());
			}
		}
		
		this.client = this.client.findClient(this.achat.getClientId());
		this.credit = this.credit.findCredit(this.idAchat);
		
		
		System.out.println("Client: "+client.getNomClient()+" id "+this.achat.getClientId());
		
	}
	
	private List<Medicament> handleRemboursement() {
		
		List<Medicament> medics = this.achat.getMedicamentsAchete();
		int length = medics.size();
		for(int i=0; i<length; i++) {
			Medicament m = medics.get(i);
			
			if(m.isSpecialiteRemboursable()) {
				
				Medicament generique = m.getMedicamentRemboursable(m.getIdStockRemboursable(), achat.getIdTypeAchat());
				
				if(generique==null)
					return medics;
				generique.setQuantiteAchete(m.getQuantiteAchete());
				generique.setPrixTotal(generique.getPrix() * generique.getQuantiteAchete());
				generique.setPrixReduit((generique.getPrix()*(100-generique.getReduction()))/100);
				generique.setPrixTotalReduit(generique.getQuantiteAchete()*generique.getPrixReduit());
								
				//System.out.println("Generique: "+generique.getPrix()+" "+generique.getPrixTotal()+" "+generique.getPrixTotalReduit());
				medics.remove(m);
				medics.add(i,generique);
				//m = generique;				
			}
		}
		return medics;
	}
	//getters and setters	

	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
		
	public Assureur getAssureur() {
		return assureur;
	}

	public void setAssureur(Assureur assureur) {
		this.assureur = assureur;
	}
	
	public int getIdAchat() {
		return idAchat;
	}

	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}

	public Achat getAchat() {
		if(achat!=null)
			achat.setMedicamentsAchete(handleRemboursement());
		return achat;
	}

	public void setAchat(Achat achat) {
		this.achat = achat;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public String getDateAchatString() {
		dateAchatString= this.achat.getDateShort();
		return dateAchatString;
	}

	public void setDateAchatString(String dateAchatString) {
		this.dateAchatString = dateAchatString;
	}

	public int getNumeroFacture() {
		numeroFacture = this.achat.getNumeroFacture(this.achat.getIdAchat(), this.assureur.getIdAssureur());
		return numeroFacture;
	}

	public void setNumeroFacture(int numeroFacture) {
		this.numeroFacture = numeroFacture;
	}

	public boolean isVenteCash() {
		return venteCash;
	}

	public void setVenteCash(boolean venteCash) {
		this.venteCash = venteCash;
	}

	
	public List<Medicament> getMedicamentsAchete() {
		medicamentsAchete = this.handleRemboursement();
		return medicamentsAchete;
	}

	public void setMedicamentsAchete(List<Medicament> medicamentsAchete) {
		this.medicamentsAchete = medicamentsAchete;
	}




	//properties
	private Client client = new Client();
	private Credit credit = new Credit();
	private Assureur assureur = new Assureur();
	private Achat achat = new Achat();
	private List<Medicament> medicamentsAchete;
	
	private int idAchat;
	private String dateAchatString;
	private int numeroFacture;
	private boolean venteCash;
	
}
