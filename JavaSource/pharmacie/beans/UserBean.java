package pharmacie.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import pharmacie.entities.User;
import pharmacie.util.CommonUtils;
import pharmacie.util.Constantes;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@ViewScoped
public class UserBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserBean() {
		// TODO Auto-generated constructor stub
	}
	
		
	public boolean isConnected() {
		return FacesUtil.memberConnected();
	}
	public String logOut() {
		CommonUtils.cancelReload();
		if(FacesUtil.logOut()){
			
			return "logOut";
		}
		return "";
	}

	public void updateUser() {
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String successMessage = RessourceBundleUtil.getUIMessages().getString("updateProfilSuccess");
		if(this.user.updateUser()) {
			if(!this.user.getPassword().isEmpty())
				this.user = this.user.logUserIn(this.user.getUsername(), this.user.getPassword());
			if(FacesUtil.logOut()){				
				if(this.user!=null) {
					
					FacesUtil.setSessionAttribute(Constantes.CONNECTED_USER, this.user);
					System.out.println("user: "+this.user.isNewUser());
					this.user = null;
					this.messageIcon="/images/accept-24.png";
					this.message= successMessage;
				}else{
					this.messageIcon="/images/error.png";
					this.message=errorMsg;
				}
			}else{
				this.messageIcon="/images/error.png";
				this.message= errorMsg;
			}
			
		}else{
			this.messageIcon="/images/error.png";
			this.message="Une erreur s'est produite. Reesayer 3";
		}
		
		System.out.println(this.message+" "+this.messageIcon);
	}
	
	public User getUser() {
		
			if(FacesUtil.memberConnected()) {
				this.user=(User) FacesUtil.getSessionAttribute(Constantes.CONNECTED_USER);
			}
		
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
	public String getMessageIcon() {
		return messageIcon;
	}
	
	public void setMessageIcon(String messageIcon) {
		this.messageIcon = messageIcon;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	private User user;
	private String messageIcon;
	private String message;
	
	
}
