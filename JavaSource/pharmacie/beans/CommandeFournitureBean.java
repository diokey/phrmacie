package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.richfaces.component.html.HtmlInputNumberSpinner;

import pharmacie.entities.Assureur;
import pharmacie.entities.Commande;
import pharmacie.entities.Fourniture;
import pharmacie.entities.Medicament;
import pharmacie.entities.Payement;
import pharmacie.entities.STATUS;
import pharmacie.util.CommonUtils;
import pharmacie.util.RessourceBundleUtil;

@ViewScoped
@ManagedBean
public class CommandeFournitureBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandeFournitureBean() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void postInit() {
		this.listCommande = null;
		this.selectedCommande = null;
		this.listFourniture = null;
		this.selectedFourniture = null;
		//System.out.println("Before show");
	}
	
	//methods
	
	public void validatePrix(FacesContext fx, UIComponent comp, Object ref) throws ValidatorException{
		if(fx==null || comp==null || ref==null)
			
			return;
		
		long prix = Long.parseLong(""+ref);
		
		if(prix <= 0 || prix > this.prixFourniture) {
			String errorMsg = RessourceBundleUtil.getUIMessages().getString("invalidSum");
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,errorMsg);
			throw new ValidatorException(message);
		}
			
	}
	
	public void payerFournisseur() {
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		if(!called) {
			saveFourniture();
			System.out.println("Called from payer fournisseur");
			called = true;
		}
		
		this.fourniture.setSommePaye(this.prixPayer);
		this.fourniture.setSommeRestante(this.getPrixFourniture() - this.prixPayer);
		
		if(this.fourniture.getSommePaye()<0) {
			if(!this.fourniture.payerFourniture()) {
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		
		this.fourniture = new Fourniture();		
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("payementFournisseursuccess")+" "+this.prixPayer+" FBU");
		
		
		if(this.selectedCommande.getProduitsCommande()==null || this.selectedCommande.getProduitsCommande().isEmpty()) {
			if(!this.selectedCommande.commandeFournis()) {
				System.out.println("Commande non livre. Verifier l'erreur");
			}
		}
		
	}
	
	
	
	private void saveFourniture() {
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		fourniture.setIdCommande(this.selectedCommande.getIdCommande());
		fourniture.setIdFournisseur(this.selectedCommande.getIdFournisseur());
		
		fourniture.setSommeTotal(this.getPrixFourniture());
		
		int idFourniture = 0;
		if(fourniture.saveFourniture()) {
			idFourniture = CommonUtils.getLastId("Fourniture", "idFourniture");
		}
		fourniture.setIdFourniture(idFourniture);
		
		for(Medicament m : this.produitsFournis) {
			if(!m.saveMedicamentFournis(idFourniture)){
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
	}
	
	public void receiveFourniture() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		if(!called) {
			saveFourniture();
			System.out.println("Called from recieve fourniture");
			called = true;
		}
		System.out.println("Called receive"+this.produitsFournis.size());
		for(Medicament m : this.produitsFournis) {
			m.setQuantiteStock(m.getQuantiteFournis());
			m.setDateArrive(new Date());
			int idStock = m.putIntoStock(m.getIdMedicament());
			
			List<Assureur> listAssureur = m.getListAssureur();
			System.out.println("saving...");
			for(Assureur a : listAssureur) {
				String mention = a.getPrix().getMentionString()==null?"":a.getPrix().getMentionString();
				//System.out.println("Assureur: "+a.getNomAssureur());
				Integer idMention = m.findMentionId(mention);
				
				a.getPrix().setIdMention(idMention);
				//int idPrix = a.getPrix().getIdPrix();
				
				if(!a.saveTarifAssureur(idStock)){
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
				
			}
		}
		
		this.selectedFourniture = null;
		for(Medicament m : this.selectedCommande.getProduitsCommande()){
			m.setQuantiteFournis(0);
		}
		this.listFourniture = null;
		this.listCommande = null;
		this.produitsFournis.clear();
		this.selectedCommande = null;
		this.prixPayer = 0;
		called = false;
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("fournitureAddedmedic"));
		
	}
	
	public void cancelReceptionFourniture() {
		//this.selectedCommande.getProduitsCommande().removeAll(this.produitsFournis);
		this.selectedCommande.getProduitsCommande().addAll(this.produitsFournis);
		this.produitsFournis = null;
	}
	
	public void selectCommande() {
		if(this.getListCommande()==null || this.getListCommande().size()<=0) {
			return;
		}
		this.selectedCommande = this.getListCommande().get(this.selectedOrder);		
		this.selectedFourniture = null;
		this.listFourniture = null;
		this.removeDelivered();
		if(this.selectedCommande==null) {
			System.err.println("No commande selected for index "+this.selectedOrder);
			return;
		}
	}
	
	public void selectFourniture() {
		
		if(this.getListFourniture()==null || this.getListFourniture().size()<=0) {
			System.out.println("No selected : "+this.selectedFournitureRow);
			return;
		}
		this.selectedFourniture = this.getListFourniture().get(this.selectedFournitureRow);
		
		if(this.selectedFourniture==null) {
			System.err.println("No Fourniture selected for index "+this.selectedFournitureRow);
		}
	}
	
	public void onQuantiteChanged(ValueChangeEvent event) {
		//System.out.println("Component: "+);
		HtmlInputNumberSpinner spinner = (HtmlInputNumberSpinner) event.getComponent();
		int index = Integer.parseInt(spinner.getLabel());
		int newQteFourni = Integer.parseInt(""+event.getNewValue());
		int oldValue = Integer.parseInt(""+event.getOldValue());
		Medicament m = this.selectedCommande.getProduitsCommande().get(index);
		
		int realValue = newQteFourni- oldValue;
		
		m.setQuantiteRestante(m.getQuantiteRestante()-realValue);
		
	}
	
	public void goToNext() {
		List<Medicament> medicaments = this.selectedCommande.getProduitsCommande();
		List<Medicament> tobeRemoved = new ArrayList<Medicament>();
		this.produitsFournis = new ArrayList<Medicament>();
		int size = medicaments.size();
		for(int i=0; i<size; i++) {
			Medicament m = medicaments.get(i);
			
			if(m.getQuantiteFournis()!=0) {
				m.setBatch("");
				m.setManifactureDate(null);
				m.setExpiryDate(null);
				this.produitsFournis.add(m);
				if(m.getQuantiteRestante()==0) {
					tobeRemoved.add(m);
				}
			}
		}
		
		medicaments.removeAll(tobeRemoved);
		for(Medicament m : this.produitsFournis) {
			
			m.setListAssureur(guessPrices(m.getIdMedicament()));
		}
	}
	
	private List<Assureur> guessPrices(int idMedicament) {
		
		int idStock = Medicament.findLastIdStock(idMedicament);
		
		System.out.println("id Stock: "+idStock);
		List<Assureur> allAssureurPrices = new Assureur().getPrixAllAsseur(idStock);
		
		return allAssureurPrices;
	}
	
	public void selectOrderToViewDetails() {
		System.out.println("Selected order : "+this.selectedOrder);
		this.selectedCommande = this.listCommande.get(this.selectedOrder);
		
		if(this.selectedCommande==null) {
			System.out.println("Not selected");
			return;
		}
	}
	
	public void checkOrder() {
		this.selectedCommande = this.listCommande.get(this.selectedOrder);
		if(this.selectedCommande==null) {
			System.out.println("Not selected");
			return;
		}
		
		this.commandeDisabled = this.selectedCommande.isDelivered();
		this.editable = this.commandeDisabled || this.selectedCommande.deliveryStarted();
		System.out.println("Checked: dilivered: "+this.editable);
	}
	
	public void removeOrder() {
		
		if(this.listCommande.size()<=this.selectedOrder) {
		
			System.err.println("Error : NO such index");
			return;
		}
		
		Commande removed = this.getListCommande().remove(this.selectedOrder);
		
		removed.deleteOrder();
		this.selectedOrder = 0;
		
		this.postInit();
		
		this.selectCommande();
			
		
	}
	
		
	private void removeDelivered() {
		
		if(this.selectedCommande==null)
			return;
		
		List<Medicament> all = this.selectedCommande.getProduitsCommande();
		if(all==null)
			return;
		List<Medicament> fournis = new ArrayList<Medicament>();
		List<Medicament> toBeRemoved = new ArrayList<Medicament>();
		
		for(Fourniture f : this.getListFourniture() ){
			
			fournis.addAll(f.getMedicamentsFournis());
			//System.out.println("fournis "+fournis.size());
		}
		
		for(Medicament m : all) {
			for(Medicament f : fournis) {
				if(m.getCodeMedicament().equals(f.getCodeMedicament())) {
					m.setQuantiteRestante(m.getQuantiteRestante() - f.getQuantiteFournis());
					
					if(m.getQuantiteRestante()<=0) {
						toBeRemoved.add(m);
					}
				}
			}
		}
		
		all.removeAll(toBeRemoved);
		
	}
	
	public void payementLivraison() {
		if(this.getListFourniture()==null || this.getListFourniture().isEmpty()) {
			return;
		}
		this.payementFourniture = this.getListFourniture().get(this.selectedFournitureRow);
		
		if(this.payementFourniture ==null) {
			//System.err.println("No fourniture found... exit");
			return;
		}
		
		Payement p = new Payement();
		p.setIdFourniture(this.payementFourniture.getIdFourniture());
		this.listPayementFounisseur = p.getListPayement();
		this.prixTotalPaye = 0;
		for(Payement pay : this.listPayementFounisseur) {
			this.prixTotalPaye += pay.getSommePaye();
		}
		
		this.prixRestante = this.payementFourniture.getSommeTotal() - this.prixTotalPaye;
		
	}
	
	public void savePayement() {
		System.out.println("Newly paid = "+this.newlyPaid);
		
		this.payementFourniture.setSommePaye(this.newlyPaid);
		this.payementFourniture.setSommeRestante(this.payementFourniture.getSommeTotal()-(this.prixTotalPaye+this.newlyPaid));
		
		System.out.println("Somme paye "+this.payementFourniture.getSommePaye());
		System.out.println("Somme restante "+this.payementFourniture.getSommeRestante());
		
		if(this.payementFourniture.payerFourniture()) {
			String msg = RessourceBundleUtil.getUIMessages().getString("sumPaid");
			this.setUpNotification(STATUS.SUCCESS, msg+" "+this.payementFourniture.getSommePaye()+" FBU");
		}else{
			String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
		
		this.listFourniture = null;
		this.payementLivraison();
	}
	
	private void setUpNotification(STATUS status, String message) {
		this.message=message;
		if(status.equals(STATUS.WARNING)){
			this.messageIcon="/images/warning.png";
			
		}else{
			if(status.equals(STATUS.INFO)) {
				this.messageIcon="/images/info.png";
			}else{
				if(status.equals(STATUS.ERROR)) {
					this.messageIcon="/images/error.png";
				}else{
					if(status.equals(STATUS.SUCCESS)) {
						this.messageIcon="/images/accept-24.png";
					}
				}
			}
		}
	}
	
	
	//getters and setters
	public List<Commande> getListCommande() {
		
			listCommande = new Commande().listAllOrders();
	
		
		return listCommande;
	}

	public void setListCommande(List<Commande> listCommande) {
		this.listCommande = listCommande;
	}
		
	public Commande getSelectedCommande() {
		if(selectedCommande == null) {
			if(this.getListCommande()!=null && !this.listCommande.isEmpty()){
				this.selectedCommande = this.listCommande.get(0);
				this.selectedCommande.setProduitsCommande(null);
			}
			removeDelivered();
		}
		
		return selectedCommande;
	}

	public void setSelectedCommande(Commande selectedCommande) {
		this.selectedCommande = selectedCommande;
	}
	
	public List<Medicament> getProduitsFournis() {
		return produitsFournis;
	}

	public void setProduitsFournis(List<Medicament> produitsFournis) {
		this.produitsFournis = produitsFournis;
	}

	public int getSelectedOrder() {
		return selectedOrder;
	}

	public void setSelectedOrder(int selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	

	public boolean isModified() {
		
		if(this.getSelectedCommande()==null)
			return modified = false;
		if(this.selectedCommande==null)
			return modified = false;
		
		List<Medicament> medicaments = this.selectedCommande.getProduitsCommande();
		if(medicaments==null)
			return modified = false;
		for(Medicament m : medicaments) {
			
			if( m.getQuantiteFournis()!=0) {
				
				return modified = true;
			}
		}
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public int getSelectedMedicament() {
		return selectedMedicament;
	}

	public void setSelectedMedicament(int selectedMedicament) {
		this.selectedMedicament = selectedMedicament;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageIcon() {
		return messageIcon;
	}

	public void setMessageIcon(String messageIcon) {
		this.messageIcon = messageIcon;
	}

	
	public long getPrixFourniture() {
		prixFourniture = 0;
		if(this.produitsFournis!=null) {
			for(Medicament m : this.produitsFournis) {
				prixFourniture+=m.getPrixTotalFourniture();
			}
		}
		return prixFourniture;
	}

	public void setPrixFourniture(long prixFourniture) {
		this.prixFourniture = prixFourniture;
	}
	
	public long getPrixPayer() {
		return prixPayer;
	}

	public void setPrixPayer(long prixPayer) {
		this.prixPayer = prixPayer;
	}
	
	public Fourniture getFourniture() {
		return fourniture;
	}

	public void setFourniture(Fourniture fourniture) {
		this.fourniture = fourniture;
	}
	
	public List<Fourniture> getListFourniture() {
		if(this.getSelectedCommande()==null)
			return listFourniture = new ArrayList<Fourniture>();
		
		if(listFourniture==null) {
			listFourniture = this.fourniture.founitureCommande(this.selectedCommande.getIdCommande());
		}
		return listFourniture;
	}

	public void setListFourniture(List<Fourniture> listFourniture) {
		this.listFourniture = listFourniture;
	}
	
	public Fourniture getSelectedFourniture() {
		if(this.selectedFourniture==null && this.getListFourniture()!=null) {
			if(this.listFourniture.size()>0)
				this.selectedFourniture = this.getListFourniture().get(0);
		}
		return selectedFourniture;
	}

	public void setSelectedFourniture(Fourniture selectedFourniture) {
		this.selectedFourniture = selectedFourniture;
	}
	
	public int getSelectedFournitureRow() {
		return selectedFournitureRow;
	}

	public void setSelectedFournitureRow(int selectedFournitureRow) {
		this.selectedFournitureRow = selectedFournitureRow;
	}
	
	public boolean isCommandeDisabled() {
		return commandeDisabled;
	}

	public void setCommandeDisabled(boolean commandeDisabled) {
		this.commandeDisabled = commandeDisabled;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public Fourniture getPayementFourniture() {
		return payementFourniture;
	}

	public void setPayementFourniture(Fourniture payementFourniture) {
		this.payementFourniture = payementFourniture;
	}
	
	public List<Payement> getListPayementFounisseur() {
		return listPayementFounisseur;
	}

	public void setListPayementFounisseur(List<Payement> listPayementFounisseur) {
		this.listPayementFounisseur = listPayementFounisseur;
	}
	
	
	public long getPrixTotalPaye() {
		return prixTotalPaye;
	}

	public void setPrixTotalPaye(long prixTotalPaye) {
		this.prixTotalPaye = prixTotalPaye;
	}

	public long getPrixRestante() {
		return prixRestante;
	}

	public void setPrixRestante(long prixRestante) {
		this.prixRestante = prixRestante;
	}
	
	public long getNewlyPaid() {
		return newlyPaid;
	}

	public void setNewlyPaid(long newlyPaid) {
		this.newlyPaid = newlyPaid;
	}






	//private members
	private List<Commande> listCommande = null;
	private Commande selectedCommande = null;
	private Fourniture fourniture = new Fourniture();
	private Fourniture selectedFourniture = null;
	private Fourniture payementFourniture = null;
	private List<Medicament> produitsFournis = new ArrayList<Medicament>();
	private List<Fourniture> listFourniture = null;
	private List<Payement> listPayementFounisseur;
	
	private int selectedOrder;
	private int selectedFournitureRow;
	private int selectedMedicament;
	private long prixFourniture;
	private long prixPayer;
	private long prixTotalPaye;
	private long prixRestante;
	private long newlyPaid;
	private boolean commandeDisabled;
	private boolean editable;
	
	private boolean modified;
	
	private String message;
	private String messageIcon;
	private boolean called = false;
	
}
