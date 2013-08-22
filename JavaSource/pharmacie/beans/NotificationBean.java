package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import pharmacie.entities.Message;
import pharmacie.entities.STATUS;
import pharmacie.entities.User;
import pharmacie.util.CommonUtils;
import pharmacie.util.Constantes;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@ManagedBean
@ViewScoped
public class NotificationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public NotificationBean() {
		// TODO Auto-generated constructor stub
		this.sender = this.findConncted();
		this.selectedContact = this.getContacts().get(0);
		this.selectedContact.setStyleClass("selected");
	}
	
	public void autoUpdate() {
		System.out.println("autoupdated");
		//this.contacts = null;
		this.selectedContact.setStyleClass("selected");
	}
	
	private void removeConnected() {
		
		List<User> intrus = new ArrayList<User>();
		intrus.add(this.sender);
		
		contacts = new User().allExcept(intrus);
		User u = new User();
		u.setUsername("system");
		u.setRole("system");
		u.setUserId(0);
		
		contacts.add(u);
	}
	
	public void changeReceiver() {
		for(User u : this.getContacts()) {
			u.setStyleClass("");
		}
		this.selectedContact = this.getContacts().get(this.selectedIndex);
		if(selectedContact!=null) {
			this.selectedContact.setConversations(null);
			this.selectedContact.setStyleClass("selected");
			this.message.updateReadMessages(this.selectedContact.getUserId(),this.sender.getUserId());
			
			System.out.println("Selected: "+this.selectedContact.getUsername());
		}else{
			System.out.println("Null selected");
		}
	}
	
	public void sendMessage() {
		System.out.println("Receiver: "+this.recievers+" Message : "+this.message.getMessageTitle()+" Titre: "+this.message.getMessage());
		
		this.message.setSender(this.sender);
		this.message.setReceiver(selectedContact);
		
		String messageText = RessourceBundleUtil.getUIMessages().getString("msgSent");
		String errorText = RessourceBundleUtil.getUIMessages().getString("msgNotSent")+" ";
		boolean error = false;
		int idMessage = 0;
		
		//save message first
		if(this.message.saveMessage()) {
			idMessage = CommonUtils.getLastId("Message", "idMessage");
			if(idMessage<=0) {
				errorText+=this.message.getReceiver().getUsername()+" .";
				error = true;
			}else{
				if(this.message.saveConversion(idMessage)) {
					messageText +=this.message.getReceiver().getUsername()+" .";
				}else{
					errorText+=this.message.getReceiver().getUsername()+" .";
					error = true;
					System.out.println("Error 1: "+error);
				}
			}
			
		}else{
			errorText+=this.message.getReceiver().getUsername()+" .";
			error = true;
			System.out.println("Error 3: "+error);
			this.setUpNotification(STATUS.ERROR, errorText);
			return;
		}
		
		//second send it to receivers
		String [] cc = this.recievers.split(",");
		int i=0;
		if(cc!=null && cc.length>0) {
			for(String c : cc) {
				
				if(c==null || c.isEmpty())
					continue;
				User receiver = new User().find(c);
				if(receiver==null) {
					errorText+=" "+c+"";
					
					error=true;
				}else{
					
					this.message.setReceiver(receiver);
					if(this.message.saveConversion(idMessage)) {
						if(i==0) 
							messageText+="Une Copie a ete transmis a";
						messageText+=" "+this.message.getReceiver().getUsername();
					}else{
						errorText+=" "+c+"";
						error = true;
						System.out.println("Error 2: "+error);
					}
				}
				i++;
			}
		}
		
		if(error) {
			messageText+=" <br />"+errorText;
		}
		
		this.selectedContact.setConversations(null);		
		this.setUpNotification(STATUS.SUCCESS, messageText);
		this.message = new Message();
	}
	
	private User findConncted() {
		if(FacesUtil.memberConnected()) {
			User u = (User) FacesUtil.getSessionAttribute(Constantes.CONNECTED_USER);
			return u;
		}
		return null;
	}
	
	public List<User> getSuggestionList() {
		List<User> suggestions = new ArrayList<User>();
		
		for(User u : this.getContacts()) {
			if(u.getUserId()!=this.selectedContact.getUserId()) {
				suggestions.add(u);
			}
		}
		
		return suggestions;
	}
	private void setUpNotification(STATUS status, String message) {
		
		
		this.messageInfo=message;
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

	public List<User> getContacts() {
		if(contacts==null) {
			removeConnected();
		}
		return contacts;
	}

	public void setContacts(List<User> contacts) {
		this.contacts = contacts;
	}
	
	public Message getMessage() {
		return message;
	}


	public void setMessage(Message message) {
		this.message = message;
	}
	
	public String getRecievers() {
		return recievers;
	}


	public void setRecievers(String recievers) {
		this.recievers = recievers;
	}

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public String getMessageIcon() {
		return messageIcon;
	}

	public void setMessageIcon(String messageIcon) {
		this.messageIcon = messageIcon;
	}

	
	public User getSelectedContact() {
		if(selectedContact==null) {
			selectedContact = this.getContacts().get(0);
		}
		return selectedContact;
	}

	public void setSelectedContact(User selectedContact) {
		this.selectedContact = selectedContact;
	}
	
	public User getSender() {
		if(sender==null) {
			sender = this.findConncted();
		}
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public int getNewMessageCount() {
		this.newMessageCount = this.message.messageCount(this.getSender().getUserId());
		return newMessageCount;
	}

	public void setNewMessageCount(int newMessageCount) {
		this.newMessageCount = newMessageCount;
	}


	private List<User> contacts = null; 
	private Message message = new Message();
	private User selectedContact = null;
	private User sender = null;
	
	private String recievers = new String();
	
	private String messageInfo;
	private String messageIcon;
	private int selectedIndex = 0;
	private int newMessageCount = 0;
	

}
