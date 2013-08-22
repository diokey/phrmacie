package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pharmacie.connection.DB;
import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class Service implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Service() {
		// TODO Auto-generated constructor stub
	}

	
	public Service(Service s) {
		super();
		this.idService = s.idService;
		this.idAssureur = s.idAssureur;
		this.reduction = s.reduction;
		this.nomService = s.nomService;
	}


	public List<Service> serviceList() {
		String requete="SELECT * FROM Service";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		List<Service> services = new ArrayList<Service>();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				Service s = new Service();
				s.idService=res.getInt("idService");
				s.idAssureur = res.getInt("idAssureur");
				s.nomService = res.getString("nomService");
				s.reduction = res.getInt("reduction");
				services.add(s);
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
		
		return services;
	}
	
	public List<Service> assureurServiceList(int idAssureur) {
		
			String requete="SELECT * FROM Service where idAssureur="+idAssureur;
			
			Connection con=SingletonConnection.getInstance();
			ResultSet res=null;
			
			Statement statement = null;
			//System.out.print(requete);
			List<Service> services = new ArrayList<Service>();
			try {
				statement=con.createStatement();
				res=statement.executeQuery(requete);
				while(res.next()) {
					Service s = new Service();
					s.idService=res.getInt("idService");
					s.idAssureur = res.getInt("idAssureur");
					s.nomService = res.getString("nomService");
					s.reduction = res.getInt("reduction")==0?null:res.getInt("reduction");
					services.add(s);
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
			
			return services;
	}
	
	public int getServiceId() {
		
		int idService = getId();
		if(idService<=0) {
			idService = save();
		}
		return idService;
	}
	
	public Service findService(int idService) {
		String requete="SELCT * FROM Service WHERE idService="+idService;
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		Service s = new Service();
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				s.idService=res.getInt("idService");
				s.idAssureur = res.getInt("idAssureur");
				s.nomService = res.getString("nomService");
				s.reduction = res.getInt("reduction");
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
				
		return s;
	}
	
	public int save() {
		if(this.reduction!=null && this.reduction==0)
			this.reduction = null;
		
		String requete="INSERT INTO Service(nomService,idAssureur,reduction) values('"+this.nomService+"',"+this.idAssureur+","+this.reduction+")";
		
		if(new DB().update(requete)>0) {
			return CommonUtils.getLastId("Service", "idService");
		}
		return -1;
	}
	
	public boolean update() {
		String requete="UPDATE Service set nomService='"+this.nomService+"',reduction="+this.reduction+",idAssureur="+this.idAssureur+" where idService="+this.idService;
		return new DB().update(requete)>0;
	}
	
	private int getId() {
		String requete="SELECT idService FROM Service where nomService='"+this.nomService+"' and idAssureur="+this.idAssureur+" limit 1";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		int rep = -1;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			if(res.next()) {
				rep = res.getInt("idService");
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
	
	public boolean exists() {
		return getId()>0;
	}
		
	//getters and setters
	
	@Override
	public String toString() {
		return "Service [idService=" + idService + ", idAssureur=" + idAssureur
				+ ", reduction=" + reduction + ", nomService=" + nomService
				+ "]";
	}

	public int getIdService() {
		return idService;
	}
	public int getIdAssureur() {
		return idAssureur;
	}

	public void setIdAssureur(int idAssureur) {
		this.idAssureur = idAssureur;
	}

	public void setIdService(int idService) {
		this.idService = idService;
	}
	public String getNomService() {
		return nomService;
	}
	public void setNomService(String nomService) {
		this.nomService = nomService;
	}
		
	public Integer getReduction() {
		return reduction;
	}

	public void setReduction(Integer reduction) {
		this.reduction = reduction;
	}

	//property
	private int idService;
	private int idAssureur;
	private Integer reduction;
	private String nomService;
}
