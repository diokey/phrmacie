package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import pharmacie.entities.Assureur;
import pharmacie.entities.Client;
import pharmacie.entities.Fournisseur;
import pharmacie.entities.Medicament;
import pharmacie.entities.Mention;
import pharmacie.entities.OwnerData;
import pharmacie.entities.PrixAchat;
import pharmacie.entities.STATUS;
import pharmacie.entities.Service;
import pharmacie.entities.ToogleButtonData;
import pharmacie.util.CommonUtils;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@ViewScoped
public class SettingsBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettingsBean() {
		// TODO Auto-generated constructor stub
	}

	//methods
	/**
	 * Page assure
	 */
	public void annulerAssure() {
		System.out.println("Canceling...");
		this.service= new Service();
	}
	
	public void annulerAllAssure() {
		this.listAssures = null;
	}
	
	public void saveAssure() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		this.service.setIdAssureur(this.idAssureur);
		if(this.service.save()>0) {
			this.setUpNotification(STATUS.SUCCESS,RessourceBundleUtil.getUIMessages().getString("assuredadded"));
			this.annulerAssure();
		}else{
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
	public void updateAssure() {
		//System.out.println("Updating...");
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		for(Service s : this.listAssures) {
			if(!s.update()) {
				
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("saveAssure"));
	}
	
	/**
	 * Info Pharmacie
	 */
	public void updateNomPharmacie(ValueChangeEvent event) {
		//System.out.println("Called "+event.getNewValue()+" "+event.getOldValue());
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String nouveauNom = (String) event.getNewValue();
		if(!nouveauNom.equalsIgnoreCase((String)event.getOldValue())) {
			if(!this.owner.updateNom(nouveauNom)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,
						errorMsg);
				FacesUtil.addMessage("settings-form:nomPharmacie", message);
			}
		}
	}
	
	public void updateTel(ValueChangeEvent event) {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		//System.out.println("Called "+event.getNewValue()+" "+event.getOldValue());
		String nouveauTel = (String) event.getNewValue();
		if(!nouveauTel.equalsIgnoreCase((String)event.getOldValue())) {
			if(!this.owner.updateTel(nouveauTel)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,
						errorMsg);
				FacesUtil.addMessage("settings-form:telPharmacie", message);
			}
		}
	}
	
	public void updateBp(ValueChangeEvent event) {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		//System.out.println("Called "+event.getNewValue()+" "+event.getOldValue());
		String nouveauBp = (String) event.getNewValue();
		if(!nouveauBp.equalsIgnoreCase((String)event.getOldValue())) {
			if(!this.owner.updateBP(nouveauBp)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,
						errorMsg);
				FacesUtil.addMessage("settings-form:bpPharmacie", message);
			}
		}
	}
	
	/**
	 * Page Assureurs
	 */
	public void saveAssureur() {
		System.out.println(this.assureur);
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		for(Assureur s : this.getAssureurs()) {
			if(s.getNomAssureur().equalsIgnoreCase(this.assureur.getNomAssureur())) {
				String existMsg = RessourceBundleUtil.getUIMessages().getString("assureurExists");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,existMsg,existMsg);
				FacesUtil.addMessage("settings-form:nomCourt", message);
				return;
			}
		}
		
		if(! this.assureur.save()) {
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}else{
			this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("addAssureurSuccess"));
		}
		this.assureur = new Assureur();
		this.listAllAssureurs = null;
		this.listMedicaments = null;
		this.assureurs = null;
		this.assureurSI = null;
	}
	
	public void resetAssureur() {
		System.out.println("Reset...");
		this.assureur = new Assureur();
	}
	
	public void resetAllAssureur() {
		this.assureurs = null;
	}
	
	public void fillNomCourt(ValueChangeEvent event) {
		
		String ass[] = ((String)event.getNewValue()).split(" ");
		
		if(ass!=null)
			this.assureur.setNomAssureur(ass[0]);
		
	}
	
	public void validateNomCourt(FacesContext ctx, UIComponent ref, Object value) 
	throws ValidatorException {
		
		String validatorMsg = RessourceBundleUtil.getUIMessages().getString("assureurExist");
		
		if(ctx==null || ref==null || value==null)
			return;
		String nom = (String) value;
		
		for(Assureur ass : this.getAssureurs()) {
			
			if(ass.getNomAssureur().equalsIgnoreCase(nom)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,validatorMsg,validatorMsg);
				throw new ValidatorException(message);
			}
		}
	}
	
	public void validateNomLong(FacesContext ctx, UIComponent ref, Object value) 
	throws ValidatorException {
		
		String validatorMsg = RessourceBundleUtil.getUIMessages().getString("assureurExist");
		
		if(ctx==null || ref==null || value==null)
			return;
		String nom = (String) value;
		for(Assureur ass : this.getAssureurs()) {
			
			if(ass.getNomAssureurLong().equalsIgnoreCase(nom)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,validatorMsg,validatorMsg);
				throw new ValidatorException(message);
			}
		}
	}
	
	public void validateNomMedicament(FacesContext ctx, UIComponent ref, Object value) 
	throws ValidatorException {
		
		String validatorMsg = RessourceBundleUtil.getUIMessages().getString("medicExist");
		
		if(ctx==null || ref==null || value==null)
			return;
		String nom = (String) value;
		for(Medicament m : this.getMedicList()) {
			
			if(m.getNomMedicament().equalsIgnoreCase(nom)) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,validatorMsg,validatorMsg);
				throw new ValidatorException(message);
			}
		}
	}
		
	public void updateAllAssureur() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		for(Assureur s : this.assureurs) {
			System.out.println(s);
			if(!s.update()) {
				this.setUpNotification(STATUS.ERROR, errorMsg);
			}
			
		}
		
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("allAssureurSaved"));
		this.listMedicaments = null;
		this.listAllAssureurs = null;
		this.assureurSI = null;
	}
	
	/**
	 * Common
	 */
	public void loadAssureur() {
		assureurs = new Assureur().listAssureur();
		assureurs.remove(0);
	}
	
	public void reset() {
		System.out.println("reseting...");
		this.message="";
		this.messageIcon="";
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
	
	/**
	 * Page medicaments
	 */
	
	public void updateMedicament() {
		System.out.println(this.selectedMedicament.getNomMedicament());
		if(this.selectedMedicament.update()) {
			String msg= RessourceBundleUtil.getUIMessages().getString("changeSaved");
			//this.medicList = null;
			this.listMedicaments = null;
			this.setUpNotification(STATUS.SUCCESS, msg);
		}else{
			String error = RessourceBundleUtil.getUIMessages().getString("error");
			this.setUpNotification(STATUS.ERROR, error);
		}
	}
	
	public void cancelupdateMedicament() {
		String message = RessourceBundleUtil.getUIMessages().getString("canceled");
		this.medicList = null;
		this.setUpNotification(STATUS.INFO, message);
	}
	
	/**
	 * 
	 * Page prix des assureurs
	 */
	
	public void updatePrices() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		for(Medicament m : this.listMedicaments) {
			List<Assureur> listAssureur = m.getListAssureur();
			
			
			for(Assureur a : listAssureur) {
				String mention = a.getPrix().getMentionString()==null?"":a.getPrix().getMentionString();
				//System.out.println("Assureur: "+a.getNomAssureur());
				Integer idMention = m.findMentionId(mention);
				
				a.getPrix().setIdMention(idMention);
				int idPrix = a.getPrix().getIdPrix();
				if(idPrix==0) {
					if(!a.saveTarifAssureur(m.getIdStock())){
						this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}else{
					if(!a.updateTarifAssureur()) {
						this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}
			}
		}
		
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("assureurPa"));
		CommonUtils.triggerReload();
	}
	
	/**
	 * Page Fournisseur	
	 * @return
	 */
	
	public void annulerFournisseur() {
		this.fournisseur = new Fournisseur();
	}
	
	public void annulerAllFournisseur() {
		this.listFournisseurs = null;
	}
	
	public void saveFournisseur() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String successMsg = RessourceBundleUtil.getUIMessages().getString("fournisseurAdded");
		if(this.fournisseur.save()) {
			
			this.setUpNotification(STATUS.SUCCESS, successMsg+" "+this.fournisseur.getNomFournisseur()+" "
					+this.fournisseur.getPrenomFournisseur()+" ");
			this.idFournisseur = CommonUtils.getLastId("Fournisseur", "idFournisseur");
			this.fournisseur = new Fournisseur();			
			this.listFournisseurs = null;
			
		}else{
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
		
	public void updateAllFournisseur() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		for(Fournisseur f : this.getListFournisseurs()) {
			//System.out.println("Called"+f);
			if(!f.update()) {
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		this.fournisseursSI = null;
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("fournisseurSaveAll"));
	}
	
	public void buildMedicamentSI() {
		List<PrixAchat> listAll = this.prixAchat.getListSansPrix();
		this.medicamentsSI = new ArrayList<SelectItem>();
		
		for(PrixAchat p : listAll) {
			if(!this.getListPrixAchat().contains(p)) {
				this.medicamentsSI.add(new SelectItem(p.getIdMedicament(),p.getNomMedicament()));
			}
			
		}
	}
	
	//permet de remplir les prix d'achat du nouveau fournisseur
	public void goToPrixAchat() {
		System.out.println("going to prix achat...");
		this.fournisseursSI = null;		
		
		this.annulerMedicPrices();
	}
	
	/**
	 * page Prix d'achat des Medicament
	 * @param event
	 */
	public void medicChanged(ValueChangeEvent event) {
		
		int idMedicament = Integer.parseInt(""+event.getNewValue());
		
		for(PrixAchat p : this.prixAchat.getListSansPrix()) {
			
			if(p.getIdMedicament()==idMedicament) {
				this.prixAchat = p;
				
				break;
			}
		}
		
	}
	
	public void validateCodeMedicament(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException{
		
		if(fc==null || comp==null || ref==null) {
			return;
		}
		String codeMedicament = ""+ref;
		
		int idMedicament = Medicament.getIdMedicamentByCode(codeMedicament);
		if(idMedicament!=0) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,RessourceBundleUtil.getUIMessages().getString("codeExist"),RessourceBundleUtil.getUIMessages().getString("codeExist"));
			throw new ValidatorException(message);
		}
	}
	
	public void resetPrixAchat() {
		this.prixAchat = new PrixAchat();
		
	}
	
	public void addNewMedicPrice() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		//System.out.println(this.prixAchat.getNomMedicament()+" "+this.prixAchat.getPrix());
		this.prixAchat.setModified(true);
		this.savePrices = true;
		int idMedic = this.prixAchat.saveMedicament();
		if(idMedic<0) {
			this.setUpNotification(STATUS.ERROR, errorMsg);
			return;
		}
		this.prixAchat.setIdMedicament(idMedic);
		
		this.listPrixAchat.add(this.prixAchat);
		this.medicamentsSI = null;
		this.prixAchat = new PrixAchat();
	}
	
	public void addPriceMedicaments() {
		this.prixAchat.setModified(true);
		this.savePrices = true;
		this.listPrixAchat.add(this.prixAchat);
		this.medicamentsSI = null;
		this.prixAchat = new PrixAchat();
	}
	
	public void saveMedicPrices() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		for(PrixAchat p : this.getListPrixAchat()) {
			System.out.println("called: "+p.isModified());
			
			if(p.isNew()) {
				p.setIdFournisseur(this.idFournisseur);
				if(!p.save()) {
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
			}else{
				if(p.isModified()) {
					if(!p.update()) {
						this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}
			}
		}
		this.listPrixAchat = null;
		this.medicamentsSI = null;
		this.savePrices = false;
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("prixachatSaved"));
	}
	
	public void pricesconfirmed() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		@SuppressWarnings("unchecked")
		List<PrixAchat> pricesAchat = (List<PrixAchat>) FacesUtil.getSessionAttribute("tobeSaved");
		
		if(pricesAchat==null) {
			System.out.println("No data to save.exiting...");
			return;
		}
		for(PrixAchat p : pricesAchat) {
			System.out.println(p.isModified()+"new: "+p.isNew());
			if(p.isNew()) {
				
				if(!p.save()) {
					this.setUpNotification(STATUS.ERROR, errorMsg);
					return;
				}
			}else{
				if(p.isModified()) {
					if(!p.update()) {
						this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}
			}
		}
		this.savePrices = false;
		this.annulerMedicPrices();
		
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("prixachatSaved"));
	}
	
		
	public void annulerMedicPrices() {
		System.err.println("Annulation...");
		this.listPrixAchat = null;
		this.medicamentsSI = null;
		this.prixAchat = new PrixAchat();
		this.savePrices = false;
		FacesUtil.setSessionAttribute("tobeSaved", null);
	}
	
	public void changeMedicStatus() {
		PrixAchat pa = this.getListPrixAchat().get(this.priceMedicIndex);
		if(pa==null) {
			return;
		}
		pa.setModified(true);
		this.savePrices = true;
	}
	
	public void fournisseurChanged(ValueChangeEvent event) {
		for(PrixAchat pa : this.getListPrixAchat()) {
			pa.setIdFournisseur(Integer.parseInt(""+event.getOldValue()));
		}
		FacesUtil.setSessionAttribute("tobeSaved",this.listPrixAchat);
		this.idFournisseur = Integer.parseInt(""+event.getNewValue());
		System.out.println(this.savePrices+"changed"+this.idFournisseur);
		
	}
	
	/**
	 * Page client
	 * @return
	 */
	
	public List<Service> suggestServices(String prefix) {
		List<Service> returnServices = new ArrayList<Service>();
		
		for(Service s : this.getServices()) {
			
			if(s.getNomService().toLowerCase().startsWith(prefix)) {
				returnServices.add(s);
			}
		}
		
		return returnServices;
	}
	
	public void serviceChanged(ValueChangeEvent event) {
		String newValue = (String) event.getNewValue();
		
		for(Service s : this.getServices()) {
			if(s.getNomService().equalsIgnoreCase(newValue)) {
				this.client.setIdService(s.getIdService());
				return;
			}
		}
	}
	
	public void validateMatricule(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException {
		if(fc==null || comp == null || ref==null)
			return;
		String matricule = ""+ref;
		
		for(Client c : this.getListClients()) {
			if(c.getNumeroMatricule().equalsIgnoreCase(matricule)) {
				FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,RessourceBundleUtil.getUIMessages().getString("matriculeExists"),RessourceBundleUtil.getUIMessages().getString("matriculeExists"));
				throw new ValidatorException(message);
			}
		}
	}
	
	public void annulerClient() {
		this.client = new Client();
		this.listClients = null;
	}
	
	public void saveClient() {
		Service s = new Service();
		s.setNomService(this.client.getNomService());
		s.setIdAssureur(this.client.getIdAssureur());
		
		if(this.client.getIdService()==0) {
		
			this.client.setIdService(s.save());
		}else{
			if(!s.exists()) {
				this.client.setIdService(s.save());
			}
		}
		
		
		if(this.client.save()>0) {
			this.listClients = null;
			this.client = new Client();
			this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("customerSave"));
		}else{
			String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
			this.setUpNotification(STATUS.ERROR,errorMsg );
		}
	}
	
	public void updateClients() {
		for(Client c : this.getListClients()) {
			Service s = new Service();
			s.setNomService(c.getNomService());
			s.setIdAssureur(c.getIdAssureur());
			
			if(!s.exists()) {
				c.setIdService(s.save());
			}else{
				c.setIdService(s.getServiceId());
			}
			
			if(!c.update()){
				String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		String successMsg = RessourceBundleUtil.getUIMessages().getString("changeSaved");
		this.setUpNotification(STATUS.SUCCESS, successMsg);
	}
	
	//getters and setters	
	public OwnerData getOwner() {
		return owner;
	}

	public void setOwner(OwnerData owner) {
		this.owner = owner;
	}
	
	public List<Assureur> getAssureurs() {
		if(assureurs == null)
			loadAssureur();
		return assureurs;
	}

	public void setAssureurs(List<Assureur> assureurs) {
		this.assureurs = assureurs;
	}
	
	public Assureur getAssureur() {
		return assureur;
	}

	public void setAssureur(Assureur assureur) {
		this.assureur = assureur;
	}

	public int getIdAssureur() {
		return idAssureur;
	}

	public void setIdAssureur(int idAssureur) {
		this.idAssureur = idAssureur;
	}

	public List<ToogleButtonData> getButtons() {
		if(buttons == null)
			buttons = new StockBean().getButtons();
		return buttons;
	}

	public void setButtons(List<ToogleButtonData> buttons) {
		this.buttons = buttons;
	}

	
	public List<Medicament> getListMedicaments() {
		if(listMedicaments == null)
			listMedicaments = new Medicament().listPrix();
		return listMedicaments;
	}

	public void setListMedicaments(List<Medicament> listMedicaments) {
		this.listMedicaments = listMedicaments;
	}
	
	public List<SelectItem> getMentions() {
		if(mentions == null) {
			mentions = new ArrayList<SelectItem>();
			List<Mention> listMention = new Mention().listMention();
			mentions.add(new SelectItem(""));
			for(Mention m : listMention){
				mentions.add(new SelectItem(m.getDescription()));
			}
		}
		return mentions;
	}
	
	public void setMentions(List<SelectItem> mentions) {
		this.mentions = mentions;
	}

	public List<Assureur> getListAllAssureurs() {
		if(listAllAssureurs==null)
			listAllAssureurs = new Assureur().listAssureur();
		return listAllAssureurs;
	}

	public void setListAllAssureurs(List<Assureur> listAllAssureurs) {
		this.listAllAssureurs = listAllAssureurs;
	}

	
	public List<SelectItem> getAssureurSI() {
		if(assureurSI==null) {
			assureurSI = new ArrayList<SelectItem>();
			for(Assureur a : this.getAssureurs()) {
				
				assureurSI.add(new SelectItem(a.getIdAssureur(),a.getNomAssureur()));
			}
		}
		return assureurSI;
	}

	public void setAssureurSI(List<SelectItem> assureurSI) {
		this.assureurSI = assureurSI;
	}

	
	public String getNomAssureur() {
		for(Assureur a : this.listAllAssureurs) {
			if(a.getIdAssureur()==this.idAssureur){
				return a.getNomAssureur();
			}
		}
		return nomAssureur;
	}

	public void setNomAssureur(String nomAssureur) {
		this.nomAssureur = nomAssureur;
	}
	
	public List<Service> getListAssures() {
		
		listAssures = new Service().assureurServiceList(idAssureur);
		
		return listAssures;
	}

	public void setListAssures(List<Service> listAssures) {
		this.listAssures = listAssures;
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
	
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
	public Fournisseur getFournisseur() {
		return fournisseur;
	}

	public void setFournisseur(Fournisseur fournisseur) {
		this.fournisseur = fournisseur;
	}
	
	public List<Fournisseur> getListFournisseurs() {
		if(listFournisseurs == null)
			this.listFournisseurs = this.fournisseur.readAll();
		return listFournisseurs;
	}

	public void setListFournisseurs(List<Fournisseur> listFournisseurs) {
		this.listFournisseurs = listFournisseurs;
	}
	

	public int getIdFournisseur() {
		if(idFournisseur == 0 && this.getListFournisseurs()!=null && this.getListFournisseurs().size()>0)
			idFournisseur = this.getListFournisseurs().get(0).getIdFournisseur();
		return idFournisseur;
	}

	public void setIdFournisseur(int idFournisseur) {
		this.idFournisseur = idFournisseur;
	}
	
	public List<SelectItem> getFournisseursSI() {
		if(this.fournisseursSI==null) {
			this.fournisseursSI = new ArrayList<SelectItem>();
			for(Fournisseur f : this.getListFournisseurs()) {
				this.fournisseursSI.add(new SelectItem(f.getIdFournisseur(),f.getNomFournisseur()+" "+f.getPrenomFournisseur()));
			}
		}
		return fournisseursSI;
	}

	public void setFournisseursSI(List<SelectItem> fournisseursSI) {
		this.fournisseursSI = fournisseursSI;
	}
	
	public List<PrixAchat> getListPrixAchat() {
		if(listPrixAchat == null)
			this.listPrixAchat = this.prixAchat.getListAvecPrix(this.getIdFournisseur());
		return listPrixAchat;
	}

	public void setListPrixAchat(List<PrixAchat> listPrixAchat) {
		this.listPrixAchat = listPrixAchat;
	}
	

	public PrixAchat getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(PrixAchat prixAchat) {
		this.prixAchat = prixAchat;
	}
	
	public List<SelectItem> getMedicamentsSI() {
		if(medicamentsSI==null) {
			this.buildMedicamentSI();
		}
		return medicamentsSI;
	}

	public void setMedicamentsSI(List<SelectItem> medicamentsSI) {
		this.medicamentsSI = medicamentsSI;
	}
	
	public boolean isSavePrices() {
		
		return savePrices;
	}

	public void setSavePrices(boolean savePrices) {
		this.savePrices = savePrices;
	}
	
	public int getPriceMedicIndex() {
		return priceMedicIndex;
	}

	public void setPriceMedicIndex(int priceMedicIndex) {
		this.priceMedicIndex = priceMedicIndex;
	}
	
	public int getOldFournisseur() {
		return oldFournisseur;
	}

	public void setOldFournisseur(int oldFournisseur) {
		this.oldFournisseur = oldFournisseur;
	}
	
	public List<Medicament> getMedicList() {
		if(medicList==null)
			medicList = new Medicament().nomMedicaments();
		
		return medicList;
	}

	public void setMedicList(List<Medicament> medicList) {
		this.medicList = medicList;
	}
	
	public List<SelectItem> getGeneriqueMedics() {
		if(generiqueMedics==null) {
			generiqueMedics = new ArrayList<SelectItem>();
			generiqueMedics.add(new SelectItem(0, ""));
			
			for(Medicament m : this.getMedicList()) {
				if(m.getTypeMedic().equalsIgnoreCase("generique")) {
					generiqueMedics.add(new SelectItem(m.getCodeMedicament(),m.getNomMedicament()));
				}
			}
		}
		return generiqueMedics;
	}
	
	public void setGeneriqueMedics(List<SelectItem> generiqueMedics) {
		this.generiqueMedics = generiqueMedics;
	}

	public Medicament getSelectedMedicament() {
		return selectedMedicament;
	}

	public void setSelectedMedicament(Medicament selectedMedicament) {
		this.selectedMedicament = selectedMedicament;
	}
	
	public List<Client> getListClients() {
		if(listClients==null) {
			listClients = new Client().clientList();
		}
		return listClients;
	}

	public void setListClients(List<Client> listClients) {
		this.listClients = listClients;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public List<Service> getServices() {
		if(services==null) {
			services = new Service().serviceList();
		}
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}



	//properties
	private OwnerData owner = new OwnerData();
	private Assureur assureur = new Assureur();
	private Fournisseur fournisseur = new Fournisseur();
	private Service service = new Service();
	private PrixAchat prixAchat = new PrixAchat();
	private Client client = new Client();
	private Medicament selectedMedicament;
	private List<Assureur> assureurs;
	private List<Assureur> listAllAssureurs;
	private List<Fournisseur> listFournisseurs;
	private List<SelectItem> fournisseursSI;
	
	private int idAssureur = 2;//mutuelle
	private String nomAssureur;
	private List<ToogleButtonData> buttons;
	private List<Medicament> listMedicaments;
	private List<Medicament> medicList;
	private List<SelectItem> mentions;
	private List<SelectItem> assureurSI;
	private List<SelectItem> generiqueMedics;
	private List<Service> listAssures;
	private List<Service> services;
	private List<Client> listClients;
	
	private List<PrixAchat> listPrixAchat;
	private List<SelectItem> medicamentsSI;
	
	
	private int idFournisseur;
	private int oldFournisseur;
	
	private boolean savePrices;
	private int priceMedicIndex;
	
	private String message;
	private String messageIcon;
	
		
}
