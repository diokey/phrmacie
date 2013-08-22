package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.Constantes;
import pharmacie.util.FacesUtil;
import pharmacie.util.HashUtil;

public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//methods
	
	public String getNewMessageStyle() {
		User connected = (User) FacesUtil.getSessionAttribute(Constantes.CONNECTED_USER);
		if(connected==null)
			return "";
		
		for(Message m : this.getConversations()) {
			if(!m.isSeen() && m.getReceiver().getUserId()==connected.getUserId()) {
				//System.out.println("New: ");
				return "new";
			}
		}
		return "";
	}
	
	public User logUserIn(String username, String password){
		//String requete="SELECT * FROM User where username='"+username+"' and password=" +
				//"'"+HashUtil.hash(password)+"'";
		
		String req = "SELECT * FROM User where username=? and password = ?";
		User u=null;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement(req);
			stmt.setString(1, username);
			stmt.setString(2, HashUtil.hash(password));
			
			res = stmt.executeQuery();
			
			if(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
				return u;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			
			try {
				res.close();
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
				
		return u;
	}
	
	public User find(int userId) {
		String requete="SELECT * FROM User where UserId="+userId;
		User u=null;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			if(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
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
		return u;
	}
	
	public User find(String username) {
		
		String requete="SELECT * FROM User where username='"+username+"'";
		User u=null;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
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
		return u;
	}
	
	public List<User> readAll() {
		List<User> allUsers = new ArrayList<User>();
		String requete="SELECT * FROM User";
		User u=null;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
				allUsers.add(u);
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
		return allUsers;
	}
	
	public List<User> allExcept(List<User> exceptions) {
		List<User> allUsers = new ArrayList<User>();
		String requete = "Select * From User ";
		int length = exceptions.size();
		if(length>0) {
			requete +=" where ";
		}
		for(int i=0; i<length; i++) {
			User u = exceptions.get(i);
			
			requete+=" userId != "+u.getUserId();
			if(i<(length-1)) {
				requete+=" or ";
			}
		}
		System.out.println(requete);
		User u=null;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
				allUsers.add(u);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				
			}
		}
		return allUsers;
	}
	public List<User> userInRole(String role) {
		List<User> allUsers = new ArrayList<User>();
		String requete="SELECT * FROM User where ";
		String[] roles = role.split(",");
		
		int nb = roles.length;
		
		for(int i=0; i< nb; i++) {
			requete+="role = '"+roles[i].trim()+"' ";
			if(i<(nb-1)) {
				requete+=" or ";
			}
		}
		
		User u=null;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			
			while(res.next()) {
				u = new User();
				u.userId=res.getInt("userId");
				u.username=res.getString("username");
				u.nom=res.getString("nom");
				u.prenom = res.getString("prenom");
				u.cni = res.getString("cni");
				u.adresse = res.getString("adresse");
				u.role = res.getString("role");
				u.newUser = res.getBoolean("new");
				u.email = res.getString("email");
				u.tel = res.getString("tel");
				u.banned = res.getBoolean("banned");
				
				allUsers.add(u);
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
		return allUsers;
	}
	
	/*public static void main(String args[]) {
		User u = new User();
		List<String> roles = new ArrayList<String>();
		roles.add("admin");
		roles.add("patron");
		System.out.println(u.userInRole(roles));
	}*/
	
	public boolean saveUser() {
		String requete="INSERT INTO User(username,password,role) values('"+this.username+"','"+HashUtil.hash(this.password)+"','"+this.role+"')";
		
		return new DB().update(requete)>0;
	}
	
	public boolean updateUser() {
		
		String requete="UPDATE User SET username='"+this.username+"'";
		if(!this.password.isEmpty())
			requete+=",password='"+HashUtil.hash(this.password)+"'";
		requete+=",nom='"+this.nom+"',prenom='"+this.prenom+"',cni='"+this.cni+"',adresse='"+this.adresse+"',tel='"+this.tel+"',email='"+this.email+"',new='0' where userId="+this.userId;
		System.out.println(requete);
		return new DB().update(requete)>0;
	}
	
	public boolean updateRole(String newRole) {
		
		String requete="UPDATE User set role='"+newRole+"' where userId="+this.userId;
		return new DB().update(requete)>0;
	}
	public static boolean usernameExist(String username) {
		
		String requete="SELECT username FROM User where username='"+username+"'";
		
		ResultSet res = new DB().read(requete);
		try {
			return res.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
		
	private List<Message> readConversations() {
		if(!FacesUtil.memberConnected()) {
			return null;
		}
		User connectedUser = (User) FacesUtil.getSessionAttribute(Constantes.CONNECTED_USER);
		if(connectedUser==null)
			return null;
				
		return new Message().readMessage(this.userId,connectedUser.getUserId());
	}
	
	//constuctor
	public User() {
		
	}
	
	//getters and setters
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
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
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
	}

	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isNewUser() {
		return newUser;
	}

	public void setNewUser(boolean newUser) {
		this.newUser = newUser;
	}
	
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean isPharmacien() {
		pharmacien = this.getRole()==null?false:this.getRole().equalsIgnoreCase("user");
		return pharmacien;
	}

	public void setPharmacien(boolean pharmacien) {
		this.pharmacien = pharmacien;
	}

	public boolean isGerant() {
		gerant = this.getRole()==null?false:this.getRole().equalsIgnoreCase("gerant");
		return gerant;
	}

	public void setGerant(boolean gerant) {
		this.gerant = gerant;
	}

	public boolean isPatron() {
		patron = this.getRole()==null?false:this.getRole().equalsIgnoreCase("patron");
		return patron;
	}

	public void setPatron(boolean patron) {
		this.patron = patron;
	}
	
	public List<Message> getConversations() {
		
			conversations = this.readConversations();
		
		return conversations;
	}

	public void setConversations(List<Message> conversations) {
		this.conversations = conversations;
	}
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}



	//member variables
	private Integer userId;
	private String username;
	private String password;
	private String nom;
	private String prenom;
	private String cni;
	private String adresse;
	private String role;
	private String tel;
	private String email;
	private boolean newUser;
	private boolean pharmacien;
	private boolean gerant;
	private boolean patron;
	private boolean banned;
	private String styleClass;
	
	private List<Message> conversations;
	
	
	//static variables

}
