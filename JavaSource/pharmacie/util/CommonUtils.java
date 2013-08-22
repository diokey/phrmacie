package pharmacie.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pharmacie.connection.SingletonConnection;

public class CommonUtils {
	
	public static int daysOfMonth(int month) {
		switch(month) {
		case 1 : return 31;
			
		case 2 : return 29;
		
		case 3 : return 31;
		
		case 4 : return 30;
		
		case 5 : return 31;
		
		case 6 : return 30;
		
		case 7 : return 31;
		
		case 8 : return 31;
		
		case 9 : return 30;
		
		case 10 : return 31;
		
		case 11 : return 30;
		
		case 12 : return 31;
		
		default : return 0;
		}
	}
	
	
	
	public static String date2String(Date d) {
		if(d==null)
			return "";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		return format.format(d);
	}
	
	public static String date2String(Date date, String pattern) {
		if(date==null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		
		return format.format(date);
	}
	/*
	 * N.B For this method to work the date format must be of this pattern
	 * yyyy-MM-dd
	 */
	public static Date string2Date(String d) {
		if(d==null || d.isEmpty())
			return null;
		String dateString[] = d.split("-");
		if(dateString.length<3)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(dateString[0]),Integer.parseInt(dateString[1])-1, Integer.parseInt(dateString[2]));
		return cal.getTime();
	}

	public static Date string2Date(String d, String pattern) {
		if(d==null || d.isEmpty())
			return null;
		String dateString[] = d.split(pattern);
		if(dateString.length<3)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(dateString[0]),Integer.parseInt(dateString[1])-1, Integer.parseInt(dateString[2]));
		return cal.getTime();
	}
	
	public static int getLastId(String tableName, String pk) {
        
        String requete ="SELECT "+pk+" FROM "+tableName+" order by "+pk+" DESC LIMIT 1";
       
        int rep = 0;
        
        Connection con=SingletonConnection.getInstance();
		ResultSet res=null;
		
		Statement statement = null;
		
        try {
        	statement=con.createStatement();
			res=statement.executeQuery(requete);
            while(res.next()) {
                rep = res.getInt(pk);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
        	try {
				res.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
        return rep;
    }
	
	/*
	 * THis function write a boolean true in reload.txt
	 * this flag indicates that medics has been changes and needs reload for 
	 * updating the UI.
	 */
	public static void triggerReload() {
		PrintWriter outputStream = null;
		String path = FacesUtil.getRessourcePath("reload.txt");
		try {
			outputStream = new PrintWriter(new FileWriter(path));
			outputStream.println(true);
			//System.out.println("writen");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
	}
	
	/*
	 * This function does the opposite of trigger reload. 
	 * it resets the reload attributes to false after the reload has occured
	 */
	public static void cancelReload() {
		String path = FacesUtil.getRessourcePath("reload.txt");
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileWriter(path));
			outputStream.print(false);
			//System.out.println("writen");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
	}
	

	/*
	 * This function determines if medics need to be reloaded or not
	 * @return a boolean indicating if we need to reload medics or not.	
	 */
	public static boolean medicsNeedReload() {
		String path = FacesUtil.getRessourcePath("reload.txt");
		//System.out.println("Path:"+path);
		BufferedReader inputStream = null;
		try {
			inputStream = 
			    new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String rep = null;
		
		try {
			if( (rep = inputStream.readLine())!=null) {
				//System.out.println(rep);
				return rep.equals("true");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(inputStream==null)
				return false;
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        
		//System.out.println(reload+":"+reload.equals("1"));
		return false;
		
	}
	
	public static void main(String args[]) {
		cancelReload();
		
	}
}
