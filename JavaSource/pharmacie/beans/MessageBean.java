package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import pharmacie.entities.Message;
import pharmacie.entities.User;

@ManagedBean
@ViewScoped
public class MessageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageBean() {
		
	}	
	
	public void sendMessage() {
		System.out.println("Receiver: "+this.receiver+" Message : "+this.message.getMessageTitle()+" Titre: "+this.message.getMessage());
		
		
	}
	
	
	
	public User getSelectedContact() {
		if(selectedContact==null) {
			this.selectedContact = this.getContacts().get(0);
		}
		return selectedContact;
	}


	public void setSelectedContact(User selectedContact) {
		this.selectedContact = selectedContact;
	}


	public List<User> getContacts() {
		
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
		
	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	
	public List<User> getContactList() {
		return contactList;
	}

	public void setContactList(List<User> contactList) {
		this.contactList = contactList;
	}



	private List<User> contacts = new ArrayList<User>();
	private List<User> contactList = new ArrayList<User>();
	private String receiver = new String();
	private User selectedContact;
	private Message message = new Message();
	
	
}
