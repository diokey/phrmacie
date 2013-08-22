package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.richfaces.event.DropEvent;

import pharmacie.entities.OwnerData;
import pharmacie.entities.STATUS;
import pharmacie.entities.Commande;
import pharmacie.entities.Fournisseur;
import pharmacie.entities.Medicament;
import pharmacie.entities.User;
import pharmacie.util.CommonUtils;
import pharmacie.util.RessourceBundleUtil;

@SessionScoped
@ManagedBean
public class CommandeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public CommandeBean() {
		// TODO Auto-generated constructor stub
	}
	
	//methods
	public void addToCart(DropEvent event) {
		//System.out.println("Processing...");
		Medicament m = (Medicament) event.getDragValue();
		
		processDrop(m);
	}
	
	public void addMedicToCart() {
		if(this.getMedicaments()==null) {
			return;
		}		
		
		Medicament m = this.getMedicaments().get(this.selectedMedic);
		
		System.out.println("Medicament: "+m.getNomMedicament());
		
		processDrop(m);
	}
	
	private void processDrop(Medicament m) {
		String message = RessourceBundleUtil.getUIMessages().getString("medicaddedtocart");
		if(this.listMedicamentCommande.contains(m)) {
			m = this.listMedicamentCommande.get(
					this.listMedicamentCommande.indexOf(m));
			m.setQuantiteAchete(m.getQuantiteAchete()+1);
		}else{
			m.setQuantiteAchete(1);
			this.listMedicamentCommande.add(m);
		}
		this.setUpNotification(STATUS.INFO, message);
	}
	
	public void annulerCommande() {
		String message = RessourceBundleUtil.getUIMessages().getString("cancelCommand");
		this.listMedicamentCommande.clear();
		this.setUpNotification(STATUS.INFO, message);
		//System.out.println("canceling...");
	}
	
	public void saveCommande() {
		System.out.println("Saving...");
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String successMsg = RessourceBundleUtil.getUIMessages().getString("commandeSaved");
		User user = new UserBean().getUser();
		this.commande.setIdUtilisateur(user.getUserId());
		this.commande.setPrixTotal(this.getPrixCommande());
		this.commande.setIdFournisseur(this.idFournisseur);
		
		int idCommande = 0;
		
		if(this.commande.saveCommande()) {
			idCommande = CommonUtils.getLastId("Commande", "idCommande");
		}
		if(idCommande==0) {
			System.err.println("No command passed");
			return;
		}
		for(Medicament m : this.listMedicamentCommande) {
			
			if(!m.saveMedicamentCommande(idCommande)) {
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		this.setUpNotification(STATUS.INFO, successMsg);
		System.out.println(successMsg);
		
		this.listMedicamentCommande.clear();
		this.listCommande = null;
		this.selectedMedic=0;
	}
	
	public void removeFromCart() {
		Medicament medicament = this.listMedicamentCommande.remove(this.selectedMedic);
		String successMsg = RessourceBundleUtil.getUIMessages().getString("medicRemovedFromCart");
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		if(medicament!=null) {
			this.setUpNotification(STATUS.INFO, successMsg);
		}else{
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
	public void updateSoldQuantity() {
		
		Medicament medicament = this.listMedicamentCommande.get(this.selectedMedic);
		
		if(medicament==null) {
			System.err.print("an error occured ");
			return;
		}
		medicament.setQuantiteAchete(this.newQuantity);
		this.newQuantity=0;
	}
	
	public void changeQuantity() {
		Medicament medicament = this.listMedicamentCommande.get(this.selectedMedic);
		
		if(medicament==null) {
			System.err.print("an error occured ");
			return;
		}
		
		this.newQuantity = medicament.getQuantiteAchete();
	}
	
	public void selectOrderToViewDetails() {
		this.selectedOrder = this.listCommande.get(this.selectedOrderId);
		
		if(this.selectedOrder==null) {
			System.out.println("Not selected");
			return;
		}
	}
	
	public void selectMedicamentToViewDetails() {
		this.selectedMedicament = this.medicaments.get(this.selectedMedic);
		System.out.println("Prix achat: "+this.selectedMedicament.getPrixAchat()+": "+this.selectedMedic);
		if(this.selectedMedicament==null) {
			System.err.println("Not selected");
			return;
		}
	}
	
	public void fournisseurChanged(ValueChangeEvent event) {
		this.oldIdFournisseur = Integer.parseInt(""+event.getOldValue());
		
		this.idFournisseur = Integer.parseInt(""+event.getNewValue());
	}
	
	public void confirmed() {
		System.out.println("produits: "+this.listMedicamentCommande.size());
		System.out.println("Id fournisseur "+this.oldIdFournisseur+": "+this.idFournisseur);
		
		int temp = this.idFournisseur;
		this.idFournisseur = this.oldIdFournisseur;
		
		this.saveCommande();
		
		this.idFournisseur = temp;
	}
	
	public void canceled() {
		this.listMedicamentCommande.clear();
		this.medicaments = null;
		System.out.print("Canceled");
	}
	
	public void sort() {
		
		this.medicaments=null;
		
		List<Medicament> newList = new ArrayList<Medicament>();
		if(this.keyword==null || this.keyword.isEmpty()) {
			this.medicaments=null;
		}else {
			for(Medicament m : this.getMedicaments()) {
				if(m.getNomMedicament().toLowerCase().startsWith(this.keyword.toLowerCase())){
					
					newList.add(m);
					
				}
			}
			this.medicaments.retainAll(newList);
		}
	}
	
	public void checkOrder() {
		this.selectedOrder = this.listCommande.get(this.selectedOrderId);
		if(this.selectedOrder==null) {
			System.out.println("Not selected");
			return;
		}
		
		this.commandeDisabled = this.selectedOrder.isDelivered();
		this.editable = this.commandeDisabled || this.selectedOrder.deliveryStarted();
		System.out.println("Checked: dilivered: "+this.editable);
	}
	
	private void setUpNotification(STATUS status, String message) {
		this.notificationMessage=message;
		if(status.equals(STATUS.WARNING)){
			this.notificationIcon="/images/warning.png";
			
		}else{
			if(status.equals(STATUS.INFO)) {
				this.notificationIcon="/images/info.png";
			}else{
				if(status.equals(STATUS.ERROR)) {
					this.notificationIcon="/images/error.png";
				}else{
					if(status.equals(STATUS.SUCCESS)) {
						this.notificationIcon="/images/accept-24.png";
					}
				}
			}
		}
	}
	
	public void reloadMedicament() {
		this.medicaments = null;
	}
	
	public void reloadCommande() {
		this.listCommande = null;
		System.out.println("Orders reloading...");
	}
	
	
	//getters and setters
	public List<Medicament> getMedicaments() {
		if(medicaments==null) {
			medicaments = new Medicament().listPrixAchat(this.getIdFournisseur());
		}
		return medicaments;
	}
	
	public void setMedicaments(List<Medicament> medicaments) {
		this.medicaments = medicaments;
	}
	
	public List<Medicament> getListMedicamentCommande() {
		return listMedicamentCommande;
	}

	public void setListMedicamentCommande(List<Medicament> listMedicamentCommande) {
		this.listMedicamentCommande = listMedicamentCommande;
	}

	public int getIdFournisseur() {
		if(idFournisseur==0) {
			List<Fournisseur> fournisseur = new Fournisseur().readAll();
			if(fournisseur!=null && fournisseur.size()>0) {
				idFournisseur = fournisseur.get(0).getIdFournisseur();
			}
		}
		return idFournisseur;
	}

	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	
	public long getPrixCommande() {
		prixCommande=0;
		for(Medicament m : this.listMedicamentCommande) {
			prixCommande+=m.getPrixTotalAchat();
		}
		return prixCommande;
	}

	public void setPrixCommande(long prixCommande) {
		this.prixCommande = prixCommande;
	}
	
	public int getProduitsCount() {
		produitsCount=0;
		if(this.listMedicamentCommande!=null) {
			for(Medicament m : this.listMedicamentCommande) {
				produitsCount+=m.getQuantiteAchete();
			}
		}
		return produitsCount;
	}

	public void setProduitsCount(int produitsCount) {
		this.produitsCount = produitsCount;
	}

	public String getCartIcon() {
		cartIcon = this.listMedicamentCommande.isEmpty()?"/images/cartempty.png":"/images/cartfull.png";
		return cartIcon;
	}

	public void setCartIcon(String cartIcon) {
		this.cartIcon = cartIcon;
	}
	
	public int getSelectedMedic() {
		return selectedMedic;
	}

	public void setSelectedMedic(int selectedMedic) {
		this.selectedMedic = selectedMedic;
	}
	
	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}
	
	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public String getNotificationIcon() {
		return notificationIcon;
	}

	public void setNotificationIcon(String notificationIcon) {
		this.notificationIcon = notificationIcon;
	}
	
	public int getNewQuantity() {
		return newQuantity;
	}

	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	
	public List<Commande> getListCommande() {
		
		if(listCommande==null) {
			listCommande = new Commande().listAllOrders();
		}
		return listCommande;
	}

	public void setListCommande(List<Commande> listCommande) {
		this.listCommande = listCommande;
	}
	
	public int getSelectedOrderId() {
		return selectedOrderId;
	}

	public void setSelectedOrderId(int selectedOrderId) {
		this.selectedOrderId = selectedOrderId;
	}

	public Commande getSelectedOrder() {
		return selectedOrder;
	}

	public void setSelectedOrder(Commande selectedOrder) {
		this.selectedOrder = selectedOrder;
	}
	
	public Medicament getSelectedMedicament() {
		return selectedMedicament;
	}

	public void setSelectedMedicament(Medicament selectedMedicament) {
		this.selectedMedicament = selectedMedicament;
	}
	
	public OwnerData getPharmacie() {
		return pharmacie;
	}

	public void setPharmacie(OwnerData pharmacie) {
		this.pharmacie = pharmacie;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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




	//attributes
	private List<Medicament> medicaments;
	private List<Medicament> listMedicamentCommande = new ArrayList<Medicament>();
	private List<Commande> listCommande = null;
	
	private int idFournisseur;
	private int oldIdFournisseur;
	private int produitsCount;
	private int selectedMedic;
	private int selectedOrderId;
	private int newQuantity;
	private long prixCommande;
	private boolean commandeDisabled;
	private boolean editable;
	
	private String cartIcon;
	private String notificationMessage;
	private String notificationIcon;
	private String keyword;
		
	private Commande commande = new Commande();
	private Commande selectedOrder;
	private Medicament selectedMedicament;
	private OwnerData pharmacie = new OwnerData();
	
}
