package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import pharmacie.entities.Achat;
import pharmacie.entities.STATUS;
import pharmacie.entities.ToogleButtonData;
import pharmacie.entities.User;
import pharmacie.util.CommonUtils;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@SessionScoped
public class CaisseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CaisseBean() {
		// TODO Auto-generated constructor stub
		this.idAchat = 0;
	}
	
	//methods
	private User getUser() {
		if(user==null)
			return new UserBean().getUser();
		return user;
	}
	
	public void reset() {		
		
		//this.idAchat = this.getListAchat().get(0).getIdAchat();
		//
		this.listAchat = null;
		this.selectedAchat = new Achat();
		this.idAchat = 0;
		changeSelectedAchat();
		//System.out.println("id achat ="+this.selectedAchat.getIdAchat());
	}
	public void findAchatDuJour(AjaxBehaviorEvent event) {
		//System.out.println("Called: "+this.selectedDate);
		this.listAchat = null;
		this.selectedAchat = new Achat();
		this.idAchat = 0;
	}
	
	public void selectVenteUtilisateur() {
		for(User u : this.pharmacienList) {
			if(u.getUserId().equals(this.idUtilisateur)) {
				this.utilisateur = u;
			}
		}
		this.listAchat = null;
		this.selectedAchat = new Achat();
		this.idAchat = 0;
	}
	
	
	public void changeSelectedAchat() {
		//System.out.println("Id achat: "+this.idAchat);
		this.selectedAchat = new Achat();
		
		this.selectedAchat.setIdAchat(this.idAchat);
		String typeAchat = this.selectedAchat.findTypeAchat(this.idAchat);
		this.selectedAchat.setTypeAchat(typeAchat.equalsIgnoreCase("")?"Cash":typeAchat);
		this.selectedAchat.setVenteCash(this.selectedAchat.getTypeAchat().equalsIgnoreCase("Cash"));
		//System.out.println("size: "+this.selectedAchat.getMedicamentsAchete().size());
	}
	
	@Deprecated
	public void changeSelectedAchatAnnule() {
		System.out.println("Id achat: "+this.idAchatAnnuler);
		this.selectedAchatAnnule = new Achat();
		this.selectedAchatAnnule.setIdAchat(this.idAchatAnnuler);
		String typeAchat = this.selectedAchatAnnule.findTypeAchat(this.idAchatAnnuler);
		this.selectedAchatAnnule.setTypeAchat(typeAchat.equalsIgnoreCase("")?"Cash":typeAchat);
		this.selectedAchatAnnule.setVenteCash(this.selectedAchat.getTypeAchat().equalsIgnoreCase("Cash"));
	}
	
	public void changeTypeAchat() {
		//System.out.println("Called change: "+this.selectedDate);
		this.listAchat = null;
		this.selectedAchat = new Achat();
		this.idAchat = 0;
	}
	
		
	@Deprecated
	public void refuserAnnulation() {
		
		this.selectedAchatAnnule.setIdAchat(this.idAchatAnnuler);
		
		if(this.selectedAchatAnnule.refuserAnnulation()) {
			this.selectedAchatAnnule = new Achat();
			this.achatAnnule = null;
			this.getAchatAnnule();
			this.idAchatAnnuler = 0;
			
			this.setUpNotification(STATUS.INFO, "Demande d'annulation de la commande numero "+this.idAchatAnnuler+" refusé !");
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
	
	//getters and setters
	
	public List<Achat> getListAchat() {
		
			if(this.getUser()==null)
				return null;
			if(this.getUser().isPharmacien()) {
				this.listAchat = new Achat().achatDuJour(this.getUser().getUserId(),this.selectedDate, this.idTypeAchat);
			}else{
				
				this.listAchat = new Achat().achatDuJour(this.getUtilisateur().getUserId(), this.selectedDate, this.idTypeAchat);
			}
				
		return listAchat;
	}
	
	private long countPrixTotalVente() {
		long total=0;
		if(this.getListAchat()==null)
			return 0;
		for(Achat a : this.getListAchat()) {
			total+=a.getSommeTotal();
		}
		return total;
	}
	
	public void setListAchat(List<Achat> listAchat) {
		this.listAchat = listAchat;
	}
	
	public List<Achat> getAchatAnnule() {
		if(achatAnnule == null) {
			achatAnnule = new Achat().achatAnnule();
		}
		return achatAnnule;
	}

	public void setAchatAnnule(List<Achat> achatAnnule) {
		this.achatAnnule = achatAnnule;
	}

	public Achat getSelectedAchat() {
		if(this.idAchat==0) {
			if(this.getListAchat()!=null && !this.getListAchat().isEmpty()){
				this.idAchat = this.getListAchat().get(0).getIdAchat();
				
				this.changeSelectedAchat();
			}
		}
		
		return selectedAchat;
	}
	public void setSelectedAchat(Achat selectedAchat) {
		this.selectedAchat = selectedAchat;
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

	public Date getSelectedDate() {
		if(selectedDate==null)
			selectedDate = new Date();
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	

	public int getIdAchat() {
		return idAchat;
	}

	public void setIdAchat(int idAchat) {
		this.idAchat = idAchat;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getTotalVenteDuJour() {
			totalVenteDuJour=countPrixTotalVente();
		return totalVenteDuJour;
	}

	public void setTotalVenteDuJour(long totalVenteDuJour) {
		this.totalVenteDuJour = totalVenteDuJour;
	}

	public String getSelectedDateString() {
		if(this.selectedDate!=null)
			this.selectedDateString = CommonUtils.date2String(selectedDate, "dd/MM/yyyy");
		return selectedDateString;
	}

	public void setSelectedDateString(String selectedDateString) {
		this.selectedDateString = selectedDateString;
	}
	
	public String getTypeAchat() {
		return typeAchat;
	}

	public void setTypeAchat(String typeAchat) {
		this.typeAchat = typeAchat;
	}

	public int getIdTypeAchat() {
		return idTypeAchat;
	}

	public void setIdTypeAchat(int idTypeAchat) {
		this.idTypeAchat = idTypeAchat;
	}
	

	public List<ToogleButtonData> getButtons() {
		if(buttons == null){
			buttons = new ToogleButtonData().getButtons();
			
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
	
	public User getUtilisateur() {
		if(utilisateur==null) {
			this.getPharmacienList().get(0);
		}
		return utilisateur;
	}

	public void setUtilisateur(User utilisateur) {
		this.utilisateur = utilisateur;
	}

	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(int idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}
	
	public List<User> getPharmacienList() {
		if(pharmacienList==null){
			pharmacienList = new ArrayList<User>();
			User u = new User();
			u.setUsername("Tous");
			u.setUserId(0);
			pharmacienList.add(u);
			List<User> allUser = new User().readAll();
			
			for(User user : allUser) {
				
				if(user.isPharmacien()) {
					
					pharmacienList.add(user);
				}
			}
			this.utilisateur = this.pharmacienList.get(0);	
			
		}
		
		return pharmacienList;
	}

	public void setPharmacienList(List<User> pharmacienList) {
		this.pharmacienList = pharmacienList;
	}
	
	public String getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(String selectedUser) {
		this.selectedUser = selectedUser;
	}

	public int getIdAchatAnnuler() {
		return idAchatAnnuler;
	}

	public void setIdAchatAnnuler(int idAchatAnnuler) {
		this.idAchatAnnuler = idAchatAnnuler;
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





	//attributes
	private List<Achat> listAchat = null;
	private List<Achat> achatAnnule = null;
	
	private Achat selectedAchat = new Achat();
	private Achat selectedAchatAnnule = new Achat();
	private User user;
	//this is used by admin actions
	private User utilisateur = new User();
	private Date selectedDate = new Date();
	private String selectedDateString;
	private String typeAchat;
	private String selectedUser;
	private int idTypeAchat = 0;
	private int idAchat;
	private int idAchatAnnuler = 0;
	private int idUtilisateur;
	private long totalVenteDuJour;
	private List<ToogleButtonData> buttons;
	private List<User> pharmacienList;
	
	//message
	private String message;
	private String messageIcon;
}
