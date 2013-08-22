package pharmacie.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import pharmacie.entities.Achat;
import pharmacie.entities.Medicament;
import pharmacie.entities.STATUS;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@ViewScoped
public class CancelingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CancelingBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void changeSelectedAchatAnnule() {
		//System.out.println("Id achat: "+this.selectedIndex);
		if(this.achatAnnule== null || this.achatAnnule.isEmpty()) {
			return;
		}
		
		if(this.achatAnnule.size()<=this.selectedIndex) {
			System.out.println("Trying to access wrong index...");
			return;
		}
		
		this.selectedAchatAnnule = this.achatAnnule.get(this.selectedIndex);
		
		//this.selectedAchatAnnule.setIdAchat(this.idAchatAnnuler);
		String typeAchat = this.selectedAchatAnnule.findTypeAchat(this.idAchatAnnuler);
		this.selectedAchatAnnule.setTypeAchat(typeAchat.equalsIgnoreCase("")?"Cash":typeAchat);
		this.selectedAchatAnnule.setVenteCash(this.selectedAchatAnnule.getTypeAchat().equalsIgnoreCase("Cash"));
	}
	
	public void refuserAnnulation() {
		
		if(this.achatAnnule== null || this.achatAnnule.isEmpty()) {
			return;
		}
		
		if(this.achatAnnule.size()<=this.selectedIndex) {
			System.out.println("Trying to access wrong index...");
			return;
		}
		
		this.selectedAchatAnnule = this.achatAnnule.get(this.selectedIndex);
		
		if(this.selectedAchatAnnule.refuserAnnulation()) {
			this.idAchatAnnuler = 0;
			//this.selectedAchatAnnule = new Achat();
			
			this.getAchatAnnule();
			int idAchat = this.selectedAchatAnnule.getIdAchat();
			
			this.setUpNotification(STATUS.INFO, RessourceBundleUtil.getUIMessages().getString("cancelRefused")+" "+idAchat+"");
			this.achatAnnule.remove(this.selectedIndex);
		}else{
			String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
		
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
	
	public void accepterAnnulation() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		if(this.achatAnnule== null || this.achatAnnule.isEmpty()) {
			return;
		}
		
		if(this.achatAnnule.size()<=this.selectedIndex) {
			System.out.println("Trying to access wrong index...");
			return;
		}
		
		this.selectedAchatAnnule = this.achatAnnule.get(this.selectedIndex);
		
		List<Medicament> returning = this.selectedAchatAnnule.getMedicamentsAchete();
		for(Medicament m : returning) {
			//System.out.println("Quantite Achete: "+m.getQuantiteAchete()+":"+m.getIdMedicament()+""+m.getIdStock());
			if(!m.restoreMedicine()){
				this.setUpNotification(STATUS.ERROR, errorMsg);
				return;
			}
		}
		
		int idA = this.selectedAchatAnnule.getIdAchat();
		System.out.println("Id achat A"+idA);
		if(this.selectedAchatAnnule.deleteAchat()) {
			this.setUpNotification(STATUS.INFO, RessourceBundleUtil.getUIMessages().getString("cancelConfirmed")+" "+idA+"");
			this.achatAnnule.remove(this.selectedIndex);
		}else{
			
			this.setUpNotification(STATUS.ERROR, errorMsg);
		}
	}
	
	//getters and setters
	public List<Achat> getAchatAnnule() {
		if(achatAnnule == null) {
			achatAnnule = new Achat().achatAnnule();
		}
		return achatAnnule;
	}

	public void setAchatAnnule(List<Achat> achatAnnule) {
		this.achatAnnule = achatAnnule;
	}
	
	public Achat getSelectedAchatAnnule() {
		if(this.idAchatAnnuler==0) {
			if(this.getAchatAnnule()!=null && !this.getAchatAnnule().isEmpty()){
				this.idAchatAnnuler = this.getAchatAnnule().get(0).getIdAchat();
				
				this.changeSelectedAchatAnnule();
			}
		}
		return selectedAchatAnnule;
	}

	public void setSelectedAchatAnnule(Achat selectedAchatAnnule) {
		this.selectedAchatAnnule = selectedAchatAnnule;
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
	


	public int getIdAchatAnnuler() {
		return idAchatAnnuler;
	}

	public void setIdAchatAnnuler(int idAchatAnnuler) {
		this.idAchatAnnuler = idAchatAnnuler;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}


	//attributes
	private List<Achat> achatAnnule = null;
	private Achat selectedAchatAnnule = new Achat();
	private int idAchatAnnuler = 0;
	private int selectedIndex = 0;
	//message
	private String message;
	private String messageIcon;
	

}
