package pharmacie.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import pharmacie.entities.Achat;
import pharmacie.entities.Client;
import pharmacie.entities.Medicament;
import pharmacie.entities.Stats;
import pharmacie.util.CommonUtils;
import pharmacie.util.RessourceBundleUtil;

@ViewScoped
@ManagedBean
public class StatsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public StatsBean() {
		// TODO Auto-generated constructor stub
		
		le = RessourceBundleUtil.getUIMessages().getString("le");
		
		cal = Calendar.getInstance();
		
		this.month = cal.get(Calendar.MONDAY) +1;
		this.year = cal.get(Calendar.YEAR);
		
		String [] commandeData = Stats.commandeDuJour(new Date());
		
		
		this.nombreCommande = Integer.parseInt(commandeData[0]);
		if(commandeData[1]==null || commandeData[1].equalsIgnoreCase("null")) {
			this.prixCommande = 0;
		}else{
			this.prixCommande = Integer.parseInt(commandeData[1]);
		}
		
		if(commandeData[2]==null || commandeData[2].equalsIgnoreCase("null")) {
			this.quantiteCommande = 0;
		}else{
			this.quantiteCommande = Integer.parseInt(commandeData[2]);
		}
		
		commandeMensuelData();
		
		
		this.endDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		this.startDate = calendar.getTime();
		
	}
	
	private void commandeMensuelData() {
		
		String [] commandeMensuelData = Stats.commandeDuJour(this.cal);
		this.nombreCommandeMensuel = Integer.parseInt(commandeMensuelData[0]);
		if(commandeMensuelData[1]==null || commandeMensuelData[1].equalsIgnoreCase("null")) {
			this.prixCommandeMensuel = 0;
		}else{
			this.prixCommandeMensuel = Integer.parseInt(commandeMensuelData[1]);
		}
		
		if(commandeMensuelData[1]==null || commandeMensuelData[1].equalsIgnoreCase("null")) {
			this.quantiteCommandeMensuel = 0;
		}else{
			this.quantiteCommandeMensuel = Integer.parseInt(commandeMensuelData[2]);
		}
	}
		
	
	public String getDateFormat() {
		String langue = RessourceBundleUtil.getLang();
		dateFormat = getLocaleFormat(langue);
		return dateFormat;
	}
	
	private String getLocaleFormat(String lang) {
		
		if(lang.equalsIgnoreCase("Fr_fr")) {
			return "'Le' dd-MM-yyyy "+" 'à' HH:mm:ss";
		}else{
			if(lang.equalsIgnoreCase("en_US")) {
				return "'' yyyy-MM-dd "+" 'at' HH:mm:ss";
			}else{
				if(lang.equalsIgnoreCase("ki_BI")) {
					return "'kuwa ' dd-MM-yyyy "+" 'isaha' HH:mm:ss";
				}
				
			}
		}
		return "dd-MM-yyyy "+" à HH:mm:ss";
	}
	
	public void anneeChanged(ValueChangeEvent event) {
		int year = Integer.parseInt(""+event.getNewValue());
		this.cal.set(Calendar.YEAR, year);
		this.commandeMensuelData();
	}
	
	public void moisChanged(ValueChangeEvent event) {
		
		int mois = Integer.parseInt(""+event.getNewValue());
		
		System.out.println(mois);
		
		this.cal.set(Calendar.MONTH, mois-1);
		
		this.commandeMensuelData();
	}
	
	public String getToday() {
		Date now = new Date();
		today = CommonUtils.date2String(now, getDateFormat());
		return today;
	}


	public void setToday(String today) {
		this.today = today;
	}

	public List<Achat> getVentes() {
		ventes = new Achat().sommeAchatDuJour(0, null, 0);
		this.totalVente = 0;
		this.totalVentePaye = 0;
		
		for(Achat v : ventes) {
			this.totalVente+= v.getSommeTotal();
			this.totalVentePaye+= v.getSommeTotalReduit();
		}
		return ventes;
	}


	public void setVentes(List<Achat> ventes) {
		this.ventes = ventes;
	}
	
		
	public List<Achat> getVentesMensuel() {
		ventesMensuel = new Achat().sommeAchatDuMois(0, this.cal, 0);
		this.totalVenteMensuel = 0;
		this.totalVentePayeMensuel = 0;
		for(Achat v : ventesMensuel) {
			this.totalVenteMensuel+= v.getSommeTotal();
			this.totalVentePayeMensuel+= v.getSommeTotalReduit();
		}
		
		return ventesMensuel;
	}


	public void setVentesMensuel(List<Achat> ventesMensuel) {
		this.ventesMensuel = ventesMensuel;
	}
	
	public List<Client> getBestCustomers() {
		return Stats.bestCustomers(this.startDate, this.endDate);
	}


	public long getTotalVente() {
		return totalVente;
	}


	public void setTotalVente(long totalVente) {
		this.totalVente = totalVente;
	}


	public long getTotalVentePaye() {
		return totalVentePaye;
	}


	public void setTotalVentePaye(long totalVentePaye) {
		this.totalVentePaye = totalVentePaye;
	}


	public static void main(String args[]) {
		StatsBean s = new StatsBean();
		s.getVentesMensuel();
	}
	
	public int getNombreProduitsVente() {
		return Stats.quantiteProduitsAchetee(new Date());
	}
	
	public int getNombreProduitsVenteMensuel() {
		return Stats.quantiteProduitsAchetee(this.cal);
	}


	public void setNombreProduitsVente(int nombreProduitsVente) {
	}


	public int getQuantiteProduitStock() {
		return Stats.quantiteStock();
	}


	public void setQuantiteProduitStock(int quantiteProduitStock) {
	}
	
	
	public int getNombreCommande() {
		return nombreCommande;
	}


	public void setNombreCommande(int nombreCommande) {
		this.nombreCommande = nombreCommande;
	}


	public long getPrixCommande() {
		return prixCommande;
	}


	public void setPrixCommande(long PrixCommande) {
		prixCommande = PrixCommande;
	}


	public int getQuantiteCommande() {
		return quantiteCommande;
	}


	public void setQuantiteCommande(int quantiteCommande) {
		this.quantiteCommande = quantiteCommande;
	}

	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}
	

	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}
	
	public long getTotalVenteMensuel() {
		return totalVenteMensuel;
	}


	public void setTotalVenteMensuel(long totalVenteMensuel) {
		this.totalVenteMensuel = totalVenteMensuel;
	}


	public long getTotalVentePayeMensuel() {
		return totalVentePayeMensuel;
	}


	public void setTotalVentePayeMensuel(long totalVentePayeMensuel) {
		this.totalVentePayeMensuel = totalVentePayeMensuel;
	}
	
	
	public int getNombreCommandeMensuel() {
		return nombreCommandeMensuel;
	}


	public void setNombreCommandeMensuel(int nombreCommandeMensuel) {
		this.nombreCommandeMensuel = nombreCommandeMensuel;
	}


	public long getPrixCommandeMensuel() {
		return prixCommandeMensuel;
	}


	public void setPrixCommandeMensuel(long prixCommandeMensuel) {
		this.prixCommandeMensuel = prixCommandeMensuel;
	}


	public int getQuantiteCommandeMensuel() {
		return quantiteCommandeMensuel;
	}


	public void setQuantiteCommandeMensuel(int quantiteCommandeMensuel) {
		this.quantiteCommandeMensuel = quantiteCommandeMensuel;
	}
	
		
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public List<Medicament> getMedicsPhare() {
		return Stats.listProduitsPhare(startDate, endDate, "DESC");
	}
	
	public List<Medicament> getMedicsNoPhare() {
		return Stats.listProduitsPhare(startDate, endDate, "ASC");
	}
	
	public String getBestSells() {
		String bestSellsData[] = Stats.bestSells(this.startDate, this.endDate, "DESC");
		if(bestSellsData[0].isEmpty())
			return bestSellsData[1];
		return bestSellsData[1]+" "+le+" "+bestSellsData[0];
	}

	public String getWorstSells() {
		String worstSellsData[] = Stats.bestSells(this.startDate, this.endDate, "ASC");
		if(worstSellsData[0].isEmpty())
			return worstSellsData[1];
		return worstSellsData[1]+" "+le+" "+worstSellsData[0];
	}

	public double getMoyenneVente() {
		return Stats.moyenneDesVentes(this.startDate, this.endDate);
	}
	
	public double getQuantiteVente(){
		return Stats.moyenneProduitsVendus(this.startDate,this.endDate);
	}
	
	public double getSommeMoyenne() {
		return Stats.sommeMoyenneVente(this.startDate, this.endDate);
	}





	private String dateFormat;
	private String today;
	private List<Achat> ventes;
	private List<Achat> ventesMensuel;
	private long totalVente;
	private long totalVenteMensuel;
	private long totalVentePaye;
	private long totalVentePayeMensuel;
	private int nombreCommande;
	private int nombreCommandeMensuel;
	private long prixCommande;
	private long prixCommandeMensuel;
	private int quantiteCommandeMensuel;
	private int quantiteCommande;
	private int month = 1;
	private int year;
	private Calendar cal;
	private Date startDate;
	private Date endDate;
	
	
	private String le;
	

}
