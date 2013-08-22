package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import pharmacie.entities.Assureur;
import pharmacie.entities.Medicament;
import pharmacie.entities.Mention;
import pharmacie.entities.STATUS;
import pharmacie.entities.ToogleButtonData;
import pharmacie.util.CommonUtils;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@ViewScoped
public class StockBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockBean() {
		// TODO Auto-generated constructor stub
	}
	
	//methods
	public void cancelUpdate() {
		this.listMedicaments = null;
		this.setUpNotification(STATUS.INFO, RessourceBundleUtil.getUIMessages().getString("canceled"));
	}
	
	public Assureur findAssureur(Medicament m ) {
		List<Assureur> listAssureur = m.getListAssureur();
		Assureur a = null;
		
		for(Assureur ass : listAssureur) {
			if(ass.getIdAssureur()==this.idTypeAchat)
				return ass;
		}
		
		return a;
	}
	
	
	public void confirmUpdate() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		for(Medicament m : this.listMedicaments) {
			
			Assureur a = null;
			
			a = findAssureur(m);
			
			if(a==null) {
				System.out.println("Error : Impossible de trouver l'assureur");
				return;
			}
			
			if(m.updateStockRow()) {
				a.getPrix().setIdMention(m.getIdMention());
				int idPrix = a.getPrix().getIdPrix();
				a.getPrix().setPrix(m.getPrix());
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
			
			/*for(Assureur a : listAssureur) {
				String mention = a.getPrix().getMentionString()==null?"":a.getPrix().getMentionString();
				//System.out.println("Assureur: "+a.getNomAssureur());
				Integer idMention = m.findMentionId(mention);
				
				a.getPrix().setIdMention(idMention);
				int idPrix = a.getPrix().getIdPrix();
				if(idPrix==0) {
					if(!a.saveTarifAssureur(m.getIdStock())){
						//this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}else{
					if(!a.updateTarifAssureur()) {
						//this.setUpNotification(STATUS.ERROR, errorMsg);
						return;
					}
				}
				System.out.println("Mention: "+mention+" id: "+m.getIdMedicament());
			}*/
		}
		
		this.setUpNotification(STATUS.SUCCESS, RessourceBundleUtil.getUIMessages().getString("assureurPa"));
		CommonUtils.triggerReload();
	}
	
	public void reloadMedicament() {
		this.listMedicaments = null;
		this.listMedicaments = new Medicament().listAllMedicament(this.idTypeAchat,false);
		
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
	
	public void selectByPrice() {
		this.listMedicaments = null;
	}
	
	
	//getters and setters
	public List<Medicament> getListMedicaments() {
		if(listMedicaments==null) 
			this.listMedicaments = new Medicament().listAllMedicament(this.idTypeAchat,false);
		return listMedicaments;
	}

	public void setListMedicaments(List<Medicament> listMedicaments) {
		this.listMedicaments = listMedicaments;
	}
	
	public List<ToogleButtonData> getButtons() {
		if(buttons == null){
			buttons = new ToogleButtonData().getButtons();
			String requiredClasses = buttons.get(0).getClasses();
			buttons.get(1).setClasses(requiredClasses);
			buttons.remove(0);
		}
		for(ToogleButtonData d : buttons) {
			String classes[] = d.getClasses().split(" ");
			if(d.getValue()==this.idTypeAchat) {				
				if(classes.length==2) {
					d.setClasses(d.getClasses()+" toggle-selected");
				}
			}else{
				if(classes.length==3) {
					
					d.setClasses(classes[0]+" "+classes[1]);
				}
			}
		}
		return buttons;
	}

	public void setButtons(List<ToogleButtonData> buttons) {
		this.buttons = buttons;
	}
		
	public int getIdTypeAchat() {
		return idTypeAchat;
	}

	public void setIdTypeAchat(int idTypeAchat) {
		this.idTypeAchat = idTypeAchat;
	}
	
	public List<SelectItem> getMentions() {
		if(mentions==null) {
			mentions = new ArrayList<SelectItem>();
			mentions.add(new SelectItem(0,""));
			List<Mention> mentionList = new Mention().listMention();
			
			for(Mention m : mentionList) {
				String des = m.getDescription().length()>20?m.getDescription().substring(0,17)+"...":m.getDescription();
				mentions.add(new SelectItem(m.getIdMention(),des));
			}
		}
		return mentions;
	}

	public void setMentions(List<SelectItem> mentions) {
		this.mentions = mentions;
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





	//member variables
	private List<Medicament> listMedicaments;
	private List<ToogleButtonData> buttons;
	private List<SelectItem> mentions;
	private String notificationMessage;
	private String notificationIcon;
	int idTypeAchat=1;

}
