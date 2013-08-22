package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean updateReadMessages(int idSender,int receiver) {
		String req ="";
		if(idSender==0) {
			req="update Conversation set seen = 1 where receiver="+receiver+" and sender is NULL";
		}else{
			req = "update Conversation set seen = 1 where sender = "+idSender+" and receiver="+receiver;
		}
		
		//System.out.println(req);
		
		return new DB().update(req)>0;
	}
	
	public int messageCount(int idReceiver) {
		
		String requete="SELECT count(*) nbMessage FROM Conversation where receiver="+idReceiver+" and seen = 0";
		int rep = 0;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				rep = res.getInt("nbMessage");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		return rep;
	}
	public List<Message> readMessage(Integer sender, int receiver) {
				
		List<Message> conversations = new ArrayList<Message>();
		String requete = "";
		if (sender == 0)
			requete="select * from Conversation co join Message m on co.idMessage=m.idMessage " +
					"where co.receiver = "+receiver+" and co.sender IS NULL order by dateSent desc limit 0,30";
		else
			requete="select * from Conversation co join Message m on co.idMessage=m.idMessage " +
				"where co.sender="+sender+" and co.receiver = "+receiver+" or co.sender="+receiver+" and co.receiver="+sender
				+" order by dateSent desc limit 0,30";
		
		
		//System.out.println(requete);
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		Message message = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				message = new Message();
				message.setMessageTitle(res.getString("titre"));
				message.setMessage(res.getString("message"));
				message.setSendDate(res.getDate("dateSent"));
				message.setSeen(res.getBoolean("seen"));
				message.setPriority(res.getString("priority"));
				User s = new User().find(res.getInt("sender"));
				if(s==null) {
					s = new User();
					s.setUsername("System");
					s.setRole("system");
					s.setUserId(0);
				}
				message.setSender(s);
				
				message.setReceiver(new User().find(res.getInt("receiver")));
				
				conversations.add(message);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		
		return conversations;
	}
	
	public boolean send() {
		if(saveMessage()){
			int idMessage = CommonUtils.getLastId("Message", "idMessage");
			if(idMessage<=0)
				return false;
			return saveConversion(idMessage);
			
		}else{
			return false;
		}
	}
	
	public boolean saveMessage() {
		Connection connection = SingletonConnection.getInstance();
		String requete="INSERT INTO Message(titre,message) values (?,?)";
		PreparedStatement prepared = null;
		try {
			prepared = connection.prepareStatement(requete);
			prepared.setString(1, this.messageTitle);
			prepared.setString(2, this.message);
			
			return prepared.executeUpdate()>0;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				prepared.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e) {
				
			}
		}
		return false;
	}
	
	public boolean saveConversion(int idMessage) {
		Connection connection = SingletonConnection.getInstance();
		String requete=null;
		PreparedStatement prepared = null;
		
		if(this.sender!= null && this.sender.getUserId()!=0) {
			requete="INSERT INTO Conversation(sender,receiver,idMessage,dateSent,priority,seen) " +
				"values(?,?,?,NOW(),?,?)";
		
			try {
				prepared = connection.prepareStatement(requete);
				prepared.setInt(1, this.sender.getUserId());
				prepared.setInt(2, this.receiver.getUserId());
				prepared.setInt(3, idMessage);
				prepared.setString(4, this.priority);
				prepared.setBoolean(5, this.seen);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}else{
			requete="INSERT INTO Conversation(receiver,idMessage,dateSent,priority,seen) " +
			"values(?,?,NOW(),?,?)";
			
			try {
				prepared = connection.prepareStatement(requete);
				prepared.setInt(1, this.receiver.getUserId());
				prepared.setInt(2, idMessage);
				prepared.setString(3, this.priority);
				prepared.setBoolean(4, this.seen);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(prepared==null)
			return false;
		
		try {
			return prepared.executeUpdate()>0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally{
			try {
				prepared.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e) {
				
			}
		}
	}
	
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getMessageTitle() {
		return messageTitle;
	}
	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}


	private User sender;
	private User receiver;
	private Date sendDate;
	private String messageTitle;
	private String message;
	private boolean seen;
	private String priority="/images/info.png";
	//private Connection connection = SingletonConnection.getInstance();

}
