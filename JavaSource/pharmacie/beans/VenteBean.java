package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.richfaces.event.DropEvent;

import pharmacie.entities.Achat;
import pharmacie.entities.Assureur;
import pharmacie.entities.Client;
import pharmacie.entities.Credit;
import pharmacie.entities.Facture;
import pharmacie.entities.Medicament;
import pharmacie.entities.Mention;
import pharmacie.entities.Message;
import pharmacie.entities.STATUS;
import pharmacie.entities.Service;
import pharmacie.entities.User;
import pharmacie.util.CommonUtils;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@SessionScoped
public class VenteBean implements Serializable{

	/**
	 * 
	 */
	
	
	public VenteBean() {
		// TODO Auto-generated constructor stub
		
		this.notificationMessage="";
	}
	
	public void detailSelected() {
		
	}
	
	public void selectListGenerique() {
		
		int index = this.rightClicked;
		Medicament m = this.getListMedicaments().get(index);
		
		if(m==null) {
			System.out.println("No medics found on the index "+index);
			return;
		}
		
	}
	
	public void onMatriculeSelect() {
		if(this.clientList==null)
			this.clientList = new Client().clientList();
		
		for(Client c : this.clientList) {
			if(c.getNumeroMatricule().equalsIgnoreCase(this.client.getNumeroMatricule())) {
				this.client = new Client(c);
				this.client.setExists(true);
				this.clientSelected = true;
				checkService();
				return;
			}
		}
		String oldMatricule = this.client.getNumeroMatricule();
		
		this.client = new Client();
		this.client.setNumeroMatricule(oldMatricule);
		this.clientSelected = false;
		
	}
	
	public void validateService(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException {
		//System.out.println("Called");
		if(fc == null || comp == null || ref == null)
			return;
		
		final String nomRef = (String) ref;
		this.client.setNomService(nomRef);
		Service s = checkService();
		
		if(s == null && ! getServiceName(this.typeAchat).equalsIgnoreCase("Mutuelle")) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_WARN,"Nouveau Service.<br /> Veuillez Completer la reduction s'elle existe",
																			"Nouveau Service.<br /> Veuillez Completer la reduction s'elle existe");
			//throw new ValidatorException(message);
			FacesUtil.addMessage("formulaireMutuelle:fonction", message);
		}
	}
	
	private String getServiceName(int idAssureur) {
		for(Assureur a : this.getAssureurs()) {
			if(a.getIdAssureur() == idAssureur) {
				return a.getNomAssureur();
			}
		}
		return "";
	}
	
	public Service checkService() {
				
		String nomService =this.client.getNomService();
		Service service = null;
		for(Service s : this.getServiceList()) {
			if(s.getNomService().equalsIgnoreCase(nomService)){
				service = new Service(s);
				break;
			}
		}
		
		if(service==null) {
			System.out.println("Service non existant");
		}else{
			Integer reduction = service.getReduction();
			if(reduction!=null && reduction!=0) {
				
				for(Medicament m : this.listMedicaments) {
					m.setReduction(reduction);
					
				}
				
				System.out.println("Modified");
			}
		}
		return service;
	}
	
	public void saveCredit() {
		System.out.println("Called...saving credit");
		this.clientInfocompleted = false;
		Service s = new Service();
		
		if(!this.client.isExists()) {
			s.setNomService(this.client.getNomService());
			s.setIdAssureur(this.typeAchat);
			s.setIdService(s.getServiceId());
			
			this.client.setIdService(s.getIdService());
			this.client.setIdAssureur(this.typeAchat);
			this.client.setIdClient(this.client.save());
			
			//we add the new client in the list if it doesn't exist
			this.clientList.add(this.client);
			//we add the new service in the list if it doesn't exist
			if(!isServiceInList(s)) {
				this.serviceList.add(s);
			}
		}
		//System.out.println("Called");
		this.credit.setIdClient(this.client.getIdClient());	
		this.clientInfocompleted = true;
		
		//System.out.println("Called"+this.credit.getBeneficiaire()+" "+this.credit.getCodeMedecin());
	}
	
	private boolean isServiceInList(Service service) {
		
		for(Service s : this.serviceList) {
			if(s.getIdService()==service.getIdService() && s.getNomService().equalsIgnoreCase(service.getNomService())) {
				return true;
			}
		}
		return false;
	}
	
	public List<Client> suggestClient(String prefix) {
		
		if(clientList==null)
			clientList = client.clientList();
		
		List<Client> result = new ArrayList<Client>();
		
		for(Client c : clientList) {
			if(c.getNumeroMatricule().toLowerCase().startsWith(prefix.toLowerCase()) && c.getIdAssureur()==this.typeAchat) {
				
				result.add(c);
			}
		}
		return result;
	}
	
	public List<Service> suggestService(String prefix) {
		
		List<Service> result = new ArrayList<Service>();
		
		for(Service s : this.getServiceList()) {
			if(s.getNomService().toLowerCase().startsWith(prefix.toLowerCase()) && s.getIdAssureur()==this.typeAchat) {
				result.add(s);
			}
		}
		
		return result;
	}
	
	public void reloadIfNeeded() {
		
		if(CommonUtils.medicsNeedReload()) {
			//CommonUtils.cancelReload();
			reloadMedicament();
			System.out.println("need reloading...");
		}else{
			System.out.println("No need reloading...");
		}
	}
	
	public void reloadMedicament() {
		this.listMedicaments = null;
		System.out.println("Reset...");
	}
	
	public void selectOrderToViewDetails() {
		if(this.orderId==null || this.orderId.isEmpty()) {
			System.out.println("Order not set");
			return;
		}
		System.out.println("Called "+this.orderId);
		int order = Integer.parseInt(this.orderId);
		
		this.selectedAchat.setIdAchat(order);
		
		String typeAchat = this.selectedAchat.findTypeAchat(order);
		this.selectedAchat.setTypeAchat(typeAchat.equalsIgnoreCase("")?"Cash":typeAchat);
		this.selectedAchat.setVenteCash(this.selectedAchat.getTypeAchat().equalsIgnoreCase("Cash"));
		
	}
	
	public void selectFromId() {
		if(this.tempId==null || this.tempId.isEmpty()) {
			System.out.println("NOt set");
			return;
		}
		int id= Integer.parseInt(this.tempId);
		this.selected = this.listMedicaments.get(id);
	}
	
	public void sort(AjaxBehaviorEvent event) {
		//System.out.println("Keyword: "+this.keyWord);
		this.listMedicaments = null;
		
		if(this.listMedicaments==null)
			this.listMedicaments = new Medicament().listMedicament(1);
		
		List<Medicament> newList = new ArrayList<Medicament>();
		if(this.keyWord==null || this.keyWord.isEmpty()) {
			this.listMedicaments=null;
		}else {
			for(Medicament m : this.getListMedicaments()) {
				if(m.getNomMedicament().toLowerCase().startsWith(this.keyWord.toLowerCase())){
					
					newList.add(m);
					
				}
			}
			this.listMedicaments.retainAll(newList);
		}
	}
	
	private boolean sendMessage (User sender, User receiver, String messageText, String messageTitle,String priority) {
		Message message = new Message();
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setMessage(messageText);
		message.setMessageTitle(messageTitle);
		message.setPriority(priority);
		
		return message.send();
	}
	
	public void saveAchat() {
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String successMessage = RessourceBundleUtil.getUIMessages().getString("venteSuccess");
		
		Achat achat = new Achat();
		achat.setIdTypeAchat(this.typeAchat);
		achat.setClientId(0);
		achat.setClientId(this.credit.getIdClient());
		achat.setUserId(this.getUser().getUserId());
		achat.setSommeTotal(this.getSommeTotal());
		achat.setSommeTotalReduit(this.getSommeTotalReduit());
		
		int lastId = achat.save();
		achat.setIdAchat(lastId);
		
		if(lastId!=0) {
			//list des admin et patron
			List<User> admins = this.user.userInRole("admin,patron");
			
			for(Medicament m : this.listMedicamentsAchete) {
				if(!m.putIntoCart(lastId)){
					
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
				
				if(m.getQuantiteStock()<=0) {
					//envoi du message d'avertissement quant le stock atteint 10 produits
					String messageTitle = "Le médicament "+m.getNomMedicament()+" Vient d' écouler";
				
					for(User admin : admins) {
						
						String messageText = "<div>Bonjour, "+admin.getUsername()+" <br />le système a detecté que le Medicament  "+m.getNomMedicament()+" " +
						" vient d' écouler. Il n y a plus de quantité disponible dans le stocker<br /> Veuillez effectuer une nouvelle Commande</div>";					
									
						
						if(!sendMessage(null, admin, messageText, messageTitle, "/images/error.png")) {
							System.out.println("Impossible d'envoyer le message a "+admin.getUsername());
						}
					}
				}else{
					if(m.getQuantiteStock()<=10) {
						
						//envoi du message d'avertissement quant le stock atteint 10 produits
						String messageTitle = "Avertissement du Niveau du Stock";
						
						
						for(User admin : admins) {
							
							String messageText = "<div>Bonjour, "+admin.getUsername()+" <br />le système a detecté qu' il ne reste plus qu' une Quantité de "+m.getQuantiteStock()+" " +
							" du Medicament "+m.getNomMedicament()+". <br /> Penser à le remplacer</div>";					
										
							
							if(!sendMessage(null, admin, messageText, messageTitle, "/images/warning.png")) {
								System.out.println("Impossible d'envoyer le message a "+admin.getUsername());
							}
						}
					}
				}
			}
			
						
			//enregistrement de la facture
			if(achat.getSommeTotalReduit()!=0) {
				Facture f = new Facture();
				f.setIdAchat(achat.getIdAchat());
				f.setSommePaye(achat.getSommeTotalReduit());
				f.setSommeRestante(achat.getSommeTotalReduit());
				if(!f.save()) {
					
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
			}
			
			if(achat.getClientId()!=0) {
				//credit = new Credit();
				credit.setIdAchat(achat.getIdAchat());
				credit.setIdClient(achat.getClientId());
				//c.setTypeAchat(this.typeAchat);
				
				if(!credit.save()) {
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
			}
			
			//reinitialisation
			this.annulerCommande();
			this.annulerCredit();
			
			this.listAchatDuJour.clear();
			
			
			this.setUpNotification(STATUS.INFO, successMessage);
			
			CommonUtils.triggerReload();
			
		}else{
			System.out.println("There was an error!");
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
	public void annulerCommande() {
		annulerCredit();
		this.listMedicaments=null;
		this.listMedicamentsAchete.clear();
		this.toBeRemoved="0";
		String message = RessourceBundleUtil.getUIMessages().getString("sellsCanceled");
		this.setUpNotification(STATUS.INFO, message);
		
	}
	
	public void demanderAnnulationAchat() {
		System.out.println("Id Achat "+this.idAchat);
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		this.selectedAchat.setIdAchat(this.idAchat);
		if(this.selectedAchat.demanderAnnulation()) {
			String part1 = RessourceBundleUtil.getUIMessages().getString("cancelSells1");
			String part2 = RessourceBundleUtil.getUIMessages().getString("cancelSells2");
			
			this.setUpNotification(STATUS.INFO, part1+" "+this.idAchat+" "+part2);
			this.listAchatDuJour = null;
		}else{
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
	public void viderPanier() {
		this.listMedicaments=null;
		this.listMedicamentsAchete.clear();
		this.toBeRemoved="0";
		String message = RessourceBundleUtil.getUIMessages().getString("emptyCart");
		this.setUpNotification(STATUS.INFO, message);
	}
	
	public void annulerCredit() {
		this.listMedicaments = null;
		this.listMedicamentsAchete.clear();
		this.credit = new Credit();
		this.client = new Client();
		this.typeAchat = 1;
		this.clientSelected = false;
		this.clientInfocompleted = false;
		
	}
	
	public void updateSoldQuantity() {
		
		int index = Integer.parseInt(this.toBeRemoved);
		Medicament m = this.listMedicamentsAchete.get(index);
		int quantity = this.newQuantity - m.getQuantiteAchete();
		int indexAll = this.listMedicaments.indexOf(m);
		if(indexAll!=-1) {
			Medicament temp = this.listMedicaments.get(indexAll);
			temp.setQuantiteStock(temp.getQuantiteStock()-quantity);
			if(temp.getQuantiteStock()<0) {
				temp.setQuantiteStock(0);
			}
		}else{
			for(Medicament med : this.listMedicaments) {
				if(med.getIdStock()==m.getIdStock()) {
					med.setQuantiteStock(med.getQuantiteStock()-quantity);
					if(med.getQuantiteStock()<0) {
						med.setQuantiteStock(0);
					}
				}
			}
		}
		
		m.setQuantiteAchete(this.newQuantity);
		//this.panelNeeded=false;
		System.out.println(""+this.newQuantity);
	}
	
	public void changeQuantite() {
		if(this.toBeRemoved==null || this.toBeRemoved.isEmpty())
			return;
		int index = Integer.parseInt(this.toBeRemoved);
		Medicament m = this.listMedicamentsAchete.get(index);
		this.newQuantity = m.getQuantiteAchete();
		
	}
	
	public void removeFromCart() {
		//System.out.println("to be removed!"+this.toBeRemoved);
		int index = Integer.parseInt(this.toBeRemoved);
		
		Medicament m = this.listMedicamentsAchete.remove(index);
		int qteAchete = m.getQuantiteAchete();
		//System.out.println("Index all: "+);
		int indexAll = this.listMedicaments.indexOf(m);
		if(indexAll!=-1) {
			Medicament tempMed= this.listMedicaments.get(indexAll);
			tempMed.setQuantiteStock(tempMed.getQuantiteStock()+qteAchete);
		}else{
			for(Medicament med : this.listMedicaments) {
				if(med.getIdStock()==m.getIdStock()) {
					med.setQuantiteStock(med.getQuantiteStock()+qteAchete);
				}
			}
		}
		String enlever = RessourceBundleUtil.getUIMessages().getString("medicRemovedFromCart");
		this.setUpNotification(STATUS.INFO, enlever);
	}
	
	private void processDrop(Medicament selected) {

		if(this.listMedicamentsAchete.contains(selected)) {
			int index = this.listMedicamentsAchete.indexOf(selected);
			if(index==-1) {
				System.out.println("Not added");
				return;
			}
			Medicament m = this.listMedicamentsAchete.get(index);
			//System.out.println("called: increment quantite");
			if(m.getQuantiteStock()==0) {
				String noMedics = RessourceBundleUtil.getUIMessages().getString("noMedicInStore");
				this.setUpNotification(STATUS.ERROR, noMedics);
				return;
			}
			m.setQuantiteAchete(m.getQuantiteAchete()+1);
			//m.setQuantiteStock(m.getQuantiteStock()-1);
			this.listMedicamentsAchete.set(index, m);
			
		}else{
			//System.out.println("New");
			selected.setQuantiteAchete(1);
			this.listMedicamentsAchete.add(selected);
		}
		
		if(this.listMedicaments.contains(selected)) {
			int index = this.listMedicaments.indexOf(selected);
			if(index==-1){
				//System.out.println("Not found");
				return;
			}
			
			this.listMedicaments.get(index).setQuantiteStock(this.listMedicaments.get(index).getQuantiteStock()-1);
		}
		String added = RessourceBundleUtil.getUIMessages().getString("medicaddedtocart");
		this.setUpNotification(STATUS.INFO, added);
	}
	
	public void addToCart(DropEvent event) {
		System.out.print("processing...");
		Medicament selected = (Medicament) event.getDragValue();
		if(selected == null) {
			System.out.println("Empty drag value! Exiting...");
			return;
		}
		processDrop(selected);		
	}
	
	public void addProductToCart(){
		
		int index = Integer.parseInt(this.selectedMedicament);
		Medicament selected = this.listMedicaments.get(index);
		if(selected == null) {
			System.out.println("Medicament Not found in list! Exiting...");
			return;
		}
		processDrop(selected);
	}
	
	public void addGeneriqueToCart() {
		int idStock = Integer.parseInt(this.selectedMedicament);
		System.err.println("Id stock : "+idStock);
		for(Medicament med : this.listMedicaments) {
			if(med.getIdStock()==idStock) {
				selected = med;
				break;
			}
		}
		
		
		/*
		String codeGenerique = selected.getcodeGenerique();
		if(codeGenerique==null || codeGenerique.isEmpty()) {
			System.out.println("No code Generique");
			return;
		}*/
		
		//selected = findMedicamentByCode(codeGenerique);
		if(selected == null) {
			System.out.println("Medicament Not found in list! Exiting...");
			return;
		}
		processDrop(selected);
		
	}
	
	public Medicament findMedicamentById(int idMedic) {
		for(Medicament m : this.listMedicaments) {
			//System.out.println("Code generique "+codeGenerique+" code Medicament: "+m.getCodeMedicament());
			if(idMedic == m.getIdStock()) {
				
				return m;
			}
		}
		return null;
	}
	
	public void addSpecialiteRemboursable() {
		
		//on recupere la specialite desiree
		int index = this.rightClicked;
		Medicament specialite = this.listMedicaments.get(index);
		if(specialite == null) {
			System.out.println("Aucun medicament  selectionnee");
			return;
		}
		
		if(specialite.isWithGenerique()) {	
			
			int idMedicament = Integer.parseInt(this.selectedMedicament);
			//on recupere le generique correspondant
			Medicament avendre = new Medicament(specialite);
			Medicament generique = findMedicamentById(idMedicament);
			System.out.println("Id medicament: "+idMedicament);
			//maintenant on le facture sur le prix de specialite moins le prix du generique
			//qui sera paye par la mutuelle
			if(avendre.getQuantiteStock()==0) {
				String noMedic = RessourceBundleUtil.getUIMessages().getString("noMedicInStore");
				this.setUpNotification(STATUS.ERROR, noMedic);
				return;
			}
			int prixSpecialite = avendre.getPrix();
			System.out.println("Prix Specialite "+prixSpecialite+" prix generique: "+generique.getPrix());
			
			int prixAffilieGenerique = generique.getPrix() - generique.getPrixReduit();
			int prixFinal = prixSpecialite - prixAffilieGenerique;
			if(prixFinal<0) {
				
				prixFinal=0;
				//avendre.setReduction(100);
				avendre.setPrix(0);
			}
			avendre.setPrixReduit(prixFinal);
			
			System.out.println("Prix reduit generique "+generique.getPrixReduit()+" prix mutuelle generique: "+prixAffilieGenerique);
			System.out.println("Prix final specialite "+avendre.getPrixReduit());
			avendre.setSpecialiteRemboursable(true);
			avendre.setIdStockRemboursable(generique.getIdStock());
			//processDrop(specialite);
			boolean found=false;
			for(Medicament m : this.listMedicamentsAchete) {
				//on regarde si c'est le meme specalite ayant le meme generique.
				if(m.getCodeMedicament().equalsIgnoreCase(avendre.getCodeMedicament()) ) {
					
					if(previous==null) {
						continue;
					}
					
					System.err.println(previous.getIdStock()+": "+generique.getIdStock());
					if(previous.getIdStock()==generique.getIdStock()) {
						
						avendre.setQuantiteStock(avendre.getQuantiteStock()-1);
						m.setQuantiteAchete(m.getQuantiteAchete()+1);
						
						System.out.println("Found");
						found=true;
						break;
					}
				}
				
				
			}
			
			if(!found) {
				avendre.setQuantiteAchete(1);
				avendre.setQuantiteStock(avendre.getQuantiteStock()-1);
				this.listMedicamentsAchete.add(avendre);
				
			}
			previous = generique;
			specialite.setQuantiteStock(specialite.getQuantiteStock()-1);
			
		}else{
			System.out.println("Medicament non generique. exiting...");
			return;
		}
		
	}
	
	public Medicament findMedicamentByCode(String codeGenerique) {
		for(Medicament m : this.listMedicaments) {
			//System.out.println("Code generique "+codeGenerique+" code Medicament: "+m.getCodeMedicament());
			if(codeGenerique.equalsIgnoreCase(m.getCodeMedicament())) {
				
				return m;
			}
		}
		return null;
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
	
	public void changeTypeAchat() {		
		//System.out.println("type achat: "+typeAchat);
		this.client = new Client();
		this.client.setIdAssureur(this.typeAchat);
		this.toBeRemoved="0";
		this.listMedicamentsAchete.clear();
		this.listMedicaments = null;
	}
	
	
	private void typeAchatSelectItems() {
				
		this.typesAchat = new ArrayList<SelectItem>();
		
		for(Assureur ass : getAssureurs()) {
			this.typesAchat.add(new SelectItem(ass.getIdAssureur(),ass.getNomAssureur()));
		}
	}
	
	private void beneficiaireSelectItems() {
		List<String> beneficiaires = this.client.getListBeneficiaire();
		this.beneficiareSI = new ArrayList<SelectItem>();
		for(String beneficiaire : beneficiaires) {
			this.beneficiareSI.add(new SelectItem(beneficiaire,getBeneficiaireLabel(beneficiaire)));
		}
	}
	
	private String getBeneficiaireLabel(String keyword) {
		String rep = RessourceBundleUtil.getUIMessages().getString(keyword);
		System.out.println("For key "+keyword+" value "+rep);
		return rep;
	}
	
	public String getAssureurName() {
		for(Assureur s : this.getAssureurs()) {
			if(s.getIdAssureur()==this.typeAchat){
				return s.getNomAssureur();
			}
		}
		return "";
	}
	
	public List<Medicament> getListMedicaments() {
		if(this.listMedicaments==null)
			this.listMedicaments = new Medicament().listMedicament(this.typeAchat);
		return listMedicaments;
	}

	public void setListMedicaments(List<Medicament> listMedicaments) {
		
		this.listMedicaments = listMedicaments;
	}
	
	public List<Achat> getListAchatDuJour() {
		if(this.listAchatDuJour == null || this.listAchatDuJour.isEmpty()) {
			if(this.getUser()==null)
				return null;
			this.listAchatDuJour = new Achat().listAchatParJour(this.getUser().getUserId());
		}
		return listAchatDuJour;
	}

	public void setListAchatDuJour(List<Achat> listAchatDuJour) {
		this.listAchatDuJour = listAchatDuJour;
	}

	public List<Medicament> getListMedicamentsAchete() {
		return listMedicamentsAchete;
	}

	public void setListMedicamentsAchete(List<Medicament> listMedicamentsAchete) {
		this.listMedicamentsAchete = listMedicamentsAchete;
	}

	public String getCartIcon() {
		cartIcon = this.listMedicamentsAchete.isEmpty()?"/images/cartempty.png":"/images/cartfull.png";
		return cartIcon;
	}

	public void setCartIcon(String cartIcon) {
		this.cartIcon = cartIcon;
	}
	
	public String getSelectedMedicament() {
		return selectedMedicament;
	}


	public void setSelectedMedicament(String selectedMedicament) {
		this.selectedMedicament = selectedMedicament;
	}

	
	public int getSommeTotal() {
		sommeTotal=0;
		for(Medicament m :this.listMedicamentsAchete) {
			sommeTotal+=m.getPrixTotal();
		}
		return sommeTotal;
	}

	public void setSommeTotal(int sommeTotal) {
		this.sommeTotal = sommeTotal;
	}
	
	public int getSommeTotalReduit() {
		sommeTotalReduit=0;
		for(Medicament m :this.listMedicamentsAchete) {
			sommeTotalReduit+=m.getPrixTotalReduit();
		}
		
		return sommeTotalReduit;
	}

	public void setSommeTotalReduit(int sommeTotalReduit) {
		this.sommeTotalReduit = sommeTotalReduit;
	}

	public Integer getProduitsCount() {
		produitsCount=0;
		for(Medicament m :this.listMedicamentsAchete) {
			produitsCount+=m.getQuantiteAchete();
		}
		return produitsCount;
	}

	public void setProduitsCount(Integer produitsCount) {
		this.produitsCount = produitsCount;
	}

	public String getToBeRemoved() {
		return toBeRemoved;
	}

	public void setToBeRemoved(String toBeRemoved) {
		this.toBeRemoved = toBeRemoved;
	}


	public String getNotificationIcon() {
		return notificationIcon;
	}

	public void setNotificationIcon(String notificationIcon) {
		this.notificationIcon = notificationIcon;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	public int getNewQuantity() {
		return newQuantity;
	}

	public void setNewQuantity(int newQuantity) {
		this.newQuantity = newQuantity;
	}
	
	public int getMaxQuantity() {
		if(this.toBeRemoved==null || this.toBeRemoved.isEmpty())
			return 0;
		int index = Integer.parseInt(this.toBeRemoved);
		Medicament m=null;
		if(!this.listMedicamentsAchete.isEmpty() && this.listMedicamentsAchete.size()>index)
			m = this.listMedicamentsAchete.get(index);
		if(m!=null)
			maxQuantity = m.getQuantiteStock()+m.getQuantiteAchete();
		return maxQuantity;
	}

	public void setMaxQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public Medicament getSelected() {
		return selected;
	}

	public void setSelected(Medicament selected) {
		this.selected = selected;
	}
	
	public long getTotalEnCaisse() {
		totalEnCaisse=0;
		for(Achat a : this.listAchatDuJour) {
			totalEnCaisse+=a.getSommeTotalReduit();
		}
		return totalEnCaisse;
	}

	public void setTotalEnCaisse(long totalEnCaisse) {
		this.totalEnCaisse = totalEnCaisse;
	}
	
	public User getUser() {
		if(user==null)
			user = new UserBean().getUser();
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Achat getSelectedAchat() {
		return selectedAchat;
	}

	public void setSelectedAchat(Achat selectedAchat) {
		this.selectedAchat = selectedAchat;
	}
	

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public List<SelectItem> getTypesAchat() {
			if(typesAchat==null)
				typeAchatSelectItems();
		return typesAchat;
	}

	public void setTypesAchat(List<SelectItem> typesAchat) {
		this.typesAchat = typesAchat;
	}

	public List<SelectItem> getBeneficiareSI() {
		
			beneficiaireSelectItems();
		return beneficiareSI;
	}

	public void setBeneficiareSI(List<SelectItem> beneficiareSI) {
		this.beneficiareSI = beneficiareSI;
	}

	public int getTypeAchat() {
		return typeAchat;
	}

	public void setTypeAchat(int typeAchat) {
		this.typeAchat = typeAchat;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	public List<Service> getServiceList() {
		if(serviceList==null)
			serviceList = new Service().serviceList();
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}

	public boolean isClientInfocompleted() {
		return clientInfocompleted;
	}

	public void setClientInfocompleted(boolean clientInfocompleted) {
		this.clientInfocompleted = clientInfocompleted;
	}

	public boolean isClientSelected() {
		return clientSelected;
	}

	public void setClientSelected(boolean clientSelected) {
		this.clientSelected = clientSelected;
	}

	public List<Assureur> getAssureurs() {
		if(assureurs == null) {
			assureurs = new Assureur().listAssureur();
		}
		return assureurs;
	}

	public void setAssureurs(List<Assureur> assureurs) {
		this.assureurs = assureurs;
	}

	public void setProduitsCount(int produitsCount) {
		this.produitsCount = produitsCount;
	}

	public List<Mention> getListMentions() {
		if(listMentions == null) {
			listMentions = new Medicament().getListMention();
		}
		return listMentions;
	}

	public void setListMentions(List<Mention> listMentions) {
		this.listMentions = listMentions;
	}
	
	public List<Medicament> getMedicamentsGenerique() {
		return medicamentsGenerique;
	}

	public void setMedicamentsGenerique(List<Medicament> medicamentsGenerique) {
		this.medicamentsGenerique = medicamentsGenerique;
	}

	public int getRightClicked() {
		return rightClicked;
	}

	public void setRightClicked(int rightClicked) {
		this.rightClicked = rightClicked;
	}

	public int getIdAchat() {
		return idAchat;
	}

	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}



	private static final long serialVersionUID = 1L;
	private List<SelectItem> typesAchat;
	private List<SelectItem> beneficiareSI;
	private List<Medicament> listMedicaments = null;
	private List<Medicament> listMedicamentsAchete = new ArrayList<Medicament>();
	private List<Medicament> medicamentsGenerique;
	private List<Achat> listAchatDuJour = new ArrayList<Achat>();
	private String cartIcon;
	private String selectedMedicament;
	private int sommeTotal;
	private int sommeTotalReduit;
	private int produitsCount;
	private String toBeRemoved;
	private String notificationIcon;
	private String notificationMessage;
	private int newQuantity;
	private int maxQuantity;
	private Medicament selected;
	private Medicament previous;
	private long totalEnCaisse;
	private User user;
	private Achat selectedAchat = new Achat();
	private String keyWord;
	private String tempId;
	private String orderId;
	private int typeAchat=1;
	private int rightClicked;
	private boolean clientInfocompleted;
	private boolean clientSelected;
	
	private Client client = new Client();
	private Credit credit = new Credit();
	private List<Client> clientList;
	private List<Service> serviceList;
	private List<Assureur> assureurs;
	private List<Mention> listMentions;
	
	//used to cancel achat
	private int idAchat;
	
}
