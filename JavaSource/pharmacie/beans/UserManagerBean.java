package pharmacie.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import pharmacie.connection.DB;
import pharmacie.entities.*;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@RequestScoped
public class UserManagerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserManagerBean() {
		// TODO Auto-generated constructor stub
		this.user = new User();
	}	
	
	//methods
	public void addNewuser() {
		if(this.user.saveUser()) {
			this.messageIcon="/images/accept-24.png";
			this.message= RessourceBundleUtil.getUIMessages().getString("userAdded");
			this.user = new User();
			this.allUsers = null;
			
		}else{
			this.messageIcon="/images/stop.png";
			this.message= RessourceBundleUtil.getUIMessages().getString("error");
		}
		
	}
	
	public void updateUser() {
		System.out.println("New role: "+this.newRole);
		User u = this.getAllUsers().get(this.selectedUserIndex);
		
		if(u==null)
			return;
		u.setRole(this.newRole);
		
		u.updateRole(newRole);
		
		this.allUsers = null;
	}
	
	public void banUser() {
		
		User u = this.getAllUsers().get(this.selectedUserIndex);
		String req="";
		if(u==null)
			return;
		
		req = "UPDATE User SET  banned = 1 where userId="+u.getUserId();
		
		new DB().update(req);
		
		this.allUsers = null;
	}
	
	public void unBanUser() {
		
		
		User u = this.getAllUsers().get(this.selectedUserIndex);
		String req="";
		if(u==null)
			return;
		
		req = "UPDATE User SET  banned = 0 where userId="+u.getUserId();
		
		new DB().update(req);
		
		this.allUsers = null;
	}
	
		
	//getters and setters	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	
	public List<User> getAllUsers() {
		if(allUsers == null) {
			allUsers = this.user.readAll();
		}
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}
	
	public int getSelectedUserIndex() {
		return selectedUserIndex;
	}

	public void setSelectedUserIndex(int selectedUserIndex) {
		this.selectedUserIndex = selectedUserIndex;
	}
	
	public String getNewRole() {
		return newRole;
	}

	public void setNewRole(String newRole) {
		this.newRole = newRole;
	}






	//private properties
	private User user;
	
	private List<User> allUsers;
	
	private String message;
	private String messageIcon;
	private String newRole;
	
	private int selectedUserIndex;

}
