package pharmacie.entities;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pharmacie.connection.SingletonConnection;
import pharmacie.util.CommonUtils;

public class Stats implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Stats() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<String []> getVenteStats() {
		List<String[]> result = new ArrayList<String[]>();
		String requete="SELECT sum(a.sommeTotal) sommeTotal,t.nomTypeAchat FROM Achat a join TypeAchat t on t.idTypeAchat=a.idTypeAchat group by (a.idTypeAchat)";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				String[] row = new String[2];
				row[0] = res.getString("sommeTotal");
				row[1] = res.getString("nomTypeAchat");
				
				result.add(row);
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
		
		return result;
	}
	
	public static int quantiteProduitsAchetee(Date date) {
		if(date==null)
			date = new Date();
		String start = CommonUtils.date2String(date,"yyyy-MM-dd")+" 00:00:00";
		String end = CommonUtils.date2String(date,"yyyy-MM-dd")+" 23:59:59";
		int rep = 0;
		
		String req = "SELECT SUM( p.quantite ) quantite, a.dateAchat " +
				"FROM Panier p " +
				"JOIN Achat a ON a.idAchat = p.idAchat " +
				"WHERE dateAchat " +
				"BETWEEN  '"+start+"' " +
				"AND  '"+end+"'";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				rep = res.getInt("quantite");
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
	
	public static int quantiteProduitsAchetee(Calendar date) {
		if(date==null)
			date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int startDay = 1;
		int endDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		int rep = 0;
		
		String startDate = year+"-"+month+"-"+startDay+" 00:00:00";
		String endDate = year+"-"+month+"-"+endDay+" 23:59:59";
		
		
		String req = "SELECT SUM( p.quantite ) quantite, a.dateAchat " +
				"FROM Panier p " +
				"JOIN Achat a ON a.idAchat = p.idAchat " +
				"WHERE dateAchat " +
				"BETWEEN  '"+startDate+"' " +
				"AND  '"+endDate+"'";
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			
			if(res.next()) {
				rep = res.getInt("quantite");
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
	
	public static String[] commandeDuJour(Calendar date) {
		if(date==null)
			date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH) + 1;
		int startDay = 1;
		int endDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		String startDate = year+"-"+month+"-"+startDay+" 00:00:00";
		String endDate = year+"-"+month+"-"+endDay+" 23:59:59";
		
		String req = "SELECT COUNT( DISTINCT c.idCommande ) commande, SUM( DISTINCT c.prixTotal ) prixTotal, SUM( m.quantite ) quantite " +
				"FROM  Commande c " +
				"JOIN MedicamentCommande m ON c.idCommande = m.idCommande " +
				"WHERE dateCommande " +
				"BETWEEN  '"+startDate+"' " +
				"AND  '"+endDate+"'";
		
		//System.out.println(req);
		String reponse [] = new String[3];
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				reponse[0] = res.getString("commande");
				reponse[1] = res.getString("prixTotal");
				reponse[2] = res.getString("quantite");
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
		
		return reponse;
	}
	
	public static String[] commandeDuJour(Date date) {
		if(date==null)
			date = new Date();
		String start = CommonUtils.date2String(date,"yyyy-MM-dd")+" 00:00:00";
		String end = CommonUtils.date2String(date,"yyyy-MM-dd")+" 23:59:59";
		String req = "SELECT COUNT( DISTINCT c.idCommande ) commande, SUM( DISTINCT c.prixTotal ) prixTotal, SUM( m.quantite ) quantite " +
				"FROM  Commande c " +
				"JOIN MedicamentCommande m ON c.idCommande = m.idCommande " +
				"WHERE dateCommande " +
				"BETWEEN  '"+start+"' " +
				"AND  '"+end+"'";
		String reponse [] = new String[3];
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				reponse[0] = res.getString("commande");
				reponse[1] = res.getString("prixTotal");
				reponse[2] = res.getString("quantite");
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
			}
		}
		
		return reponse;
	}
	
	public static int quantiteStock() {
		String req = "SELECT SUM( quantite ) quantite FROM  Stock";
		int rep = 0;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				rep = res.getInt("quantite");
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
	
	public static long sommeTotalVenteDuJour(int typeSomme, String date) {
		long rep = 0;
		String requete="";
		switch(typeSomme) {
		case 1 : requete="SELECT DAY(dateAchat) jour, sum( a.sommeTotal ) somme " +
		"FROM Achat a " +
		"WHERE dateAchat between '" +date+" 00:00:00' and '"+date+" 23:59:59' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat";
		break;
		case 3 : requete="SELECT DAY(dateAchat) jour, sum( a.sommeTotalReduit ) somme " +
		"FROM Achat a " +
		"WHERE dateAchat between '" +date+" 00:00:00' and '"+date+" 23:59:59' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat";
		break;
		case 2 : requete="SELECT DAY(dateAchat) jour, sum( (a.sommeTotal - a.sommeTotalReduit) ) somme " +
		"FROM Achat a " +
		"WHERE dateAchat between '" +date+" 00:00:00' and '"+date+" 23:59:59' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat";
		break;
		
		default: return 0;
		
		}
		
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				rep = res.getLong("somme");
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
	
	public static List<String[]> produitsVendusParMois(int idAssureur, String startDate, String endDate) {
		List<String[]> result = new ArrayList<String[]>();
		String requete="SELECT a.idAchat, DAY(dateAchat) jour, sum( p.quantite ) quantiteAchete " +
				"FROM Achat a " +
				"JOIN Panier p ON p.idAchat = a.idAchat " +
				"WHERE idTypeAchat="+idAssureur+" and dateAchat between '" +startDate+"' and '"+endDate+"' "+
				"GROUP BY DATE( dateAchat ) "+ 
				"ORDER BY dateAchat";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				String[] row = new String[2];
				row[0] = res.getString("jour");
				row[1] = res.getString("quantiteAchete");
				
				result.add(row);
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
		
		List<String[]> monthlyResult = new ArrayList<String[]>();
		
		for(int i=1; i<=31; i++) {
			String[] day = new String[2];
			day[0] = ""+i;
			day[1]=""+0;
			String[] data = exists(i,result);
			if(data!=null) {
				day = data;
			}
			
			monthlyResult.add(day);
		}
		
		return monthlyResult;
	}
	
	public static List<String[]> ventesMensuel(int idAssureur, int typeSomme, String startDate, String endDate) {
		List<String[]> result = new ArrayList<String[]>();
		
		String requete="";
		switch(typeSomme) {
		case 1 : requete="SELECT a.idAchat, DAY(dateAchat) jour, sum( a.sommeTotal ) somme " +
		"FROM Achat a " +
		"WHERE idTypeAchat="+idAssureur+" and dateAchat between '" +startDate+"' and '"+endDate+"' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat";
		break;
		case 3 : requete="SELECT a.idAchat, DAY(dateAchat) jour, sum( a.sommeTotalReduit ) somme " +
		"FROM Achat a " +
		"WHERE idTypeAchat="+idAssureur+" and dateAchat between '" +startDate+"' and '"+endDate+"' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat";
		break;
		case 2 : requete="SELECT a.idAchat, DAY(dateAchat) jour, sum( (a.sommeTotal - a.sommeTotalReduit) ) somme " +
		"FROM Achat a " +
		"WHERE idTypeAchat="+idAssureur+" and dateAchat between '" +startDate+"' and '"+endDate+"' "+
		"GROUP BY DATE( dateAchat ) "+ 
		"ORDER BY dateAchat"; 
		}
		
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				String[] row = new String[2];
				row[0] = res.getString("jour");
				row[1] = res.getString("somme");
				
				result.add(row);
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
		
		List<String[]> monthlyResult = new ArrayList<String[]>();
		
		for(int i=1; i<=31; i++) {
			String[] day = new String[2];
			day[0] = ""+i;
			day[1]=""+0;
			String[] data = exists(i,result);
			if(data!=null) {
				day = data;
			}
			
			monthlyResult.add(day);
		}
		
		return monthlyResult;
	}
	
	public static List<String[]> produitsVendusParMois(String startDate, String endDate) {
		List<String[]> result = new ArrayList<String[]>();
		String requete="SELECT a.idAchat, DAY(dateAchat) jour, sum( p.quantite ) quantiteAchete " +
				"FROM Achat a " +
				"JOIN Panier p ON p.idAchat = a.idAchat " +
				"WHERE dateAchat between '" +startDate+"' and '"+endDate+"' "+
				"GROUP BY DATE( dateAchat ) "+ 
				"ORDER BY dateAchat";
		//System.out.println(requete);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(requete);
			while(res.next()) {
				String[] row = new String[2];
				row[0] = res.getString("jour");
				row[1] = res.getString("quantiteAchete");
				
				result.add(row);
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
		
		List<String[]> monthlyResult = new ArrayList<String[]>();
		
		for(int i=1; i<=31; i++) {
			String[] day = new String[2];
			day[0] = ""+i;
			day[1]=""+0;
			String[] data = exists(i,result);
			if(data!=null) {
				day = data;
			}
			
			monthlyResult.add(day);
		}
		
		return monthlyResult;
	}
	
	private static String[] exists(int month,List<String[]> data) {
		
		for(String[] l : data) {
			if(Integer.parseInt(l[0])==month)
				return l;
		}
	
		return null;
	}
	
	public static List<Medicament> listProduitsPhare(Date startDate, Date endDate, String order) {
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		
		
		
		String req="SELECT m.nomMedicament, SUM( p.quantite ) quantite, SUM( p.prixTotal ) prixTotal " +
				"FROM Achat a " +
				"JOIN Panier p ON a.idAchat = p.idAchat " +
				"LEFT JOIN Stock s ON s.idStock = p.idStock " +
				"LEFT JOIN Medicament m ON m.idMedicament = s.idMedicament " +
				"WHERE a.dateAchat BETWEEN  '"+startDay+"' AND  '"+endDay+"' " +
				"GROUP BY s.idMedicament " +
				"ORDER BY  `quantite` "+order+" limit 0,10";
		
		List<Medicament> medics = new ArrayList<Medicament>();
		//System.out.println(req);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			while(res.next()) {
				Medicament m = new Medicament();
				m.setNomMedicament(res.getString("nomMedicament"));
				m.setQuantiteAchete(res.getInt("quantite"));
				m.setPrix(res.getInt("prixTotal"));
				
				medics.add(m);
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
		
		return medics;
	}
	
	public static List<Medicament> top5Products(Date startDate, Date endDate, String order) {
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		
		
		
		String req="SELECT m.nomMedicament, SUM( p.quantite ) quantite, SUM( p.prixTotal ) prixTotal " +
				"FROM Achat a " +
				"JOIN Panier p ON a.idAchat = p.idAchat " +
				"LEFT JOIN Stock s ON s.idStock = p.idStock " +
				"LEFT JOIN Medicament m ON m.idMedicament = s.idMedicament " +
				"WHERE a.dateAchat BETWEEN  '"+startDay+"' AND  '"+endDay+"' " +
				"GROUP BY s.idMedicament " +
				"ORDER BY  `quantite` "+order+" limit 0,5";
		
		List<Medicament> medics = new ArrayList<Medicament>();
		//System.out.println(req);
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			while(res.next()) {
				Medicament m = new Medicament();
				m.setNomMedicament(res.getString("nomMedicament"));
				m.setQuantiteAchete(res.getInt("quantite"));
				m.setPrix(res.getInt("prixTotal"));
				
				medics.add(m);
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
		
		return medics;
	}
	
	public static List<Client> bestCustomers(Date startDate, Date endDate) {
		
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		String req="SELECT concat(c.nom,' ',c.prenom) nom,sum(sommeTotal) sommeTotal,s.nomService FROM `Achat` a " +
				"join Client c on a.idClient=c.IdClient " +
				"join Service s on c.idService=s.idService " +
				"WHERE a.idClient IS NOT NULL AND dateAchat BETWEEN '" +startDay+"' AND '"+endDay+"' "+
				"GROUP BY a.idClient order by sommeTotal desc limit 0,5";
		
		//System.out.println(req);
		List<Client> result = new ArrayList<Client>();
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			while(res.next()) {
				Client c = new Client();
				c.setNomClient(res.getString("nom"));
				c.setNomService(res.getString("nomService"));
				c.setSommeAchat(res.getLong("sommeTotal"));
				
				result.add(c);
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
		
		return result;
	}
	
	public static String[] bestSells(Date startDate, Date endDate,String order) {
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		
		String req = "SELECT DATE(dateAchat) dateAch,sum(sommeTotal) somTotal FROM `Achat` " +
				"where dateAchat BETWEEN '"+startDay+"' AND '"+endDay+"' group by dateAch order by somTotal "+order+" limit 1";
		//System.out.println(req);		
		String rep[] = new String[2];
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				rep[0] = res.getString("dateAch");
				rep[1] = res.getString("somTotal");
			}else{
				rep[0] = "";
				rep[1] = "0";
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
	
	public static double sommeMoyenneVente(Date startDate, Date endDate) {
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		String req = "SELECT AVG( sommeTotal ) moyenne " +
				"FROM  `Achat` " +
				"WHERE dateAchat " +
				"BETWEEN  '"+startDay+"' AND  '"+endDay+"' " ;
		double rep = 0;
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				rep = res.getDouble("moyenne");
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
	
	public static double moyenneDesVentes(Date startDate, Date endDate) {
		double rep = 0;
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		
		String req="SELECT DATEDIFF(  '"+endDay+"',  '"+startDay+"' ) diff, " +
				"(SELECT COUNT( idAchat ) " +
				"FROM  `Achat`  " +
				"WHERE dateAchat " +
				"BETWEEN  '"+startDay+"' " +
				"AND  '"+endDay+"') moyenne";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				if(res.getLong("diff")==0)
					rep = 0;
				else 
					rep = res.getDouble("moyenne") / res.getDouble("diff");
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
	
	public static double moyenneProduitsVendus(Date startDate, Date endDate) {
		
		double rep = 0;
		String startDay= CommonUtils.date2String(startDate,"yyyy-MM-dd")+" 00:00:00";
		String endDay = CommonUtils.date2String(endDate,"yyyy-MM-dd")+" 23:59:59";
		
		String req="select DATEDIFF('"+endDay+"','"+startDay+"') diff,(SELECT sum(p.quantite) " +
				"FROM  `Achat` a join Panier p on p.idAchat=a.idAchat " +
				"WHERE dateAchat " +
				"BETWEEN  '"+startDay+"' " +
				"AND  '"+endDay+"') moyenne";
		
		Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		try {
			statement=con.createStatement();
			res=statement.executeQuery(req);
			if(res.next()) {
				
				if(res.getLong("diff")==0)
					rep = 0;
				else
					rep = res.getDouble("moyenne") / res.getDouble("diff");
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

}
