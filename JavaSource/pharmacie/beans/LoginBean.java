package pharmacie.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import pharmacie.entities.User;
import pharmacie.util.Constantes;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@RequestScoped
public class LoginBean {
	
	//methods
	
	//constructor
	public LoginBean() {
		
	}

	public String connect() {
		User user = new User().logUserIn(username, password);
		if(user!=null){
			if(user.isBanned()) {
				String msg = RessourceBundleUtil.getUIMessages().getString("userBanned");
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,msg,msg);
				FacesUtil.addMessage("connexionForm:connectBtn", message);
				
				return null;
			}
			
			FacesUtil.setSessionAttribute(Constantes.CONNECTED_USER, user);
			if(user.getRole().equalsIgnoreCase("gerant")) {
				return "manager";
			}else{
				if(user.getRole().equalsIgnoreCase("user")) {
					return "vente";
				}else{
					if(user.getRole().equalsIgnoreCase("patron")) {
						return "boss";
					}
				}
			}
		}else{
			System.out.println("Before call :");
			String msg = RessourceBundleUtil.getUIMessages().getString("invalidLogin");
			System.out.println("After call "+msg);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,msg,msg);
			FacesUtil.addMessage("connexionForm:connectBtn", message);
		}
		return null;
	}
	//getters and setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	//member variables
	
	private String username;
	
	private String password;
	
	//static variables
	
	
}
