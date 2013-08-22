package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.jsflot.components.FlotChartClickedEvent;
import org.jsflot.components.FlotChartRendererData;
import org.jsflot.xydata.XYDataList;
import org.jsflot.xydata.XYDataPoint;
import org.jsflot.xydata.XYDataSetCollection;

import pharmacie.entities.Assureur;
import pharmacie.entities.Medicament;
import pharmacie.entities.Stats;
import pharmacie.util.RessourceBundleUtil;

@RequestScoped
@ManagedBean
public class ChartBean implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ChartBean() {
		// TODO Auto-generated constructor stub
		
       Calendar cal = Calendar.getInstance();
       this.monthList = new ArrayList<SelectItem>();
       
       String monthString = RessourceBundleUtil.getUIMessages().getString("months");
       String[] months = monthString.split(",");
       
       for(int i=0 ; i<months.length; i++) {
    	   this.monthList.add(new SelectItem((i+1),months[i]));
       }
             
       this.venteBarChartOption = new ArrayList<SelectItem>();
       String sommeTotal = RessourceBundleUtil.getUIMessages().getString("sommeTotal");
       this.venteBarChartOption.add(new SelectItem(1,sommeTotal));
       String sommeCash = RessourceBundleUtil.getUIMessages().getString("sommeCash");
       this.venteBarChartOption.add(new SelectItem(2,sommeCash));
       String sommeAfilie = RessourceBundleUtil.getUIMessages().getString("sommeAffilie");
       this.venteBarChartOption.add(new SelectItem(3,sommeAfilie));
       
       this.typeSomme = 1;
       
       this.mois = cal.get(Calendar.MONTH) + 1;
       this.annee = cal.get(Calendar.YEAR);
       
       this.dateDebut = new Date();
       cal.set(Calendar.DAY_OF_MONTH, 1);
       cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
       this.dateDebut = cal.getTime();
       
       this.dateFin = new Date();
       this.startDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+"1";
       this.endDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
              
	}
	
	public void dateChanged(ValueChangeEvent event) {
		System.out.println("Changing...");
		this.generateProduitPieChart();
	}
	
	private void generatePieChart() {
		chartData = new FlotChartRendererData();
		String repartitionVente = RessourceBundleUtil.getUIMessages().getString("repartitionVente");
		chartData.setTitle(repartitionVente);
		String tousVente = RessourceBundleUtil.getUIMessages().getString("venteEffectue");
		chartData.setSubtitle(tousVente);
		chartData.setShowYaxisLabels(false);
		chartData.setShowXaxisLabels(false);
		chartData.setShowDataPoints(false);
		chartData.setHeight("400");
		
        chartData.setChartClickable(true);
        chartData.setChartDraggable(false);
        chartData.setTooltipFollowMouse(true);
        chartData.setChartZoomable(false);
        chartData.setShowTooltip(false);
        chartData.setChartType("pie");
        
		chartSeries = new XYDataSetCollection();
		List<String[]> data = Stats.getVenteStats();
		
		for(String[] d : data) {
			piexydataList = new XYDataList();
			piexydataList.addDataPoint(new XYDataPoint(Integer.parseInt(d[0]),Integer.parseInt(d[0]),d[1]));
			piexydataList.setLabel(d[1]);
	
			chartSeries.addDataList(piexydataList);
		}
	
	}
	
	private void generateProduitPieChart() {
		
		this.produitPieChartData = new FlotChartRendererData();
		String top5 = RessourceBundleUtil.getUIMessages().getString("top5Vente");
		this.produitPieChartData.setTitle(top5);
		this.produitPieChartData.setShowYaxisLabels(false);
		this.produitPieChartData.setShowXaxisLabels(false);
		this.produitPieChartData.setShowDataPoints(false);
		this.produitPieChartData.setHeight("400");
		
		this.produitPieChartData.setChartClickable(true);
		this.produitPieChartData.setChartDraggable(false);
		this.produitPieChartData.setTooltipFollowMouse(true);
		this.produitPieChartData.setChartZoomable(false);
		this.produitPieChartData.setShowTooltip(true);
		this.produitPieChartData.setChartType("pie");
		
		//data
		this.produitPieChartSeries = new XYDataSetCollection();
		
		List<Medicament> medicaments = Stats.top5Products(this.dateDebut, this.dateFin, "DESC");
		
		for(Medicament m : medicaments) {
			this.produitPieDataList = new XYDataList();
			this.produitPieDataList.addDataPoint(new XYDataPoint(m.getQuantiteAchete(),m.getQuantiteAchete(),m.getNomMedicament()));
			this.produitPieDataList.setLabel(m.getNomMedicament());
			
			this.produitPieChartSeries.addDataList(this.produitPieDataList);
		}
		
		
	}
		
	public void barChartActionListener(ActionEvent event) {
		if (event instanceof FlotChartClickedEvent) {
			 FlotChartClickedEvent flotEvent = (FlotChartClickedEvent)event;
			 
			 double xd = Double.parseDouble(""+flotEvent.getClickedDataPoint().getX());
			 
			 int x = (int) xd;
			 
			 String date[] = this.startDate.split("-");
			 if(date.length<2) {
				 return;
			 }
			 String selectedDate =date[0]+"-"+date[1]+"-"+x;
			 this.sommeTotal = Stats.sommeTotalVenteDuJour(this.typeSomme,selectedDate);
			 
			 String msgText = RessourceBundleUtil.getUIMessages().getString("sommeTotalVendu");
			 this.sommeTotalMessage = msgText+" "+selectedDate+" : "+this.sommeTotal+" FBU";
			 
			 System.out.println(this.sommeTotalMessage);
			 
		 }else{
			 System.out.println("Dragged");
		 }
	}
	
	public void chartActionListener(ActionEvent event) {
		 
	}
	
	private void generateVenteBarChart() {
		
		ventesChartRenderer = new FlotChartRendererData();
		ventesChartRenderer.setHeight("400");
		String revenue = RessourceBundleUtil.getUIMessages().getString("revenuRealise");
		ventesChartRenderer.setTitle(revenue);
		String du = RessourceBundleUtil.getUIMessages().getString("du");
		String au = RessourceBundleUtil.getUIMessages().getString("au");;
		ventesChartRenderer.setSubtitle(du+" "+this.startDate+" "+au+" "+this.endDate);
		ventesChartRenderer.setShowYaxisLabels(true);
		ventesChartRenderer.setShowXaxisLabels(true);
		ventesChartRenderer.setShowDataPoints(true);
		String jour = RessourceBundleUtil.getUIMessages().getString("jour");
		ventesChartRenderer.setXaxisTitle(jour);
		String revenueFBU = RessourceBundleUtil.getUIMessages().getString("revenuFbu");
		ventesChartRenderer.setYaxisTitle(revenueFBU);
        ventesChartRenderer.setChartClickable(true);
        ventesChartRenderer.setChartDraggable(false);
        ventesChartRenderer.setTooltipFollowMouse(false);
        ventesChartRenderer.setTooltipPosition("nw");
        ventesChartRenderer.setChartZoomable(false);
        ventesChartRenderer.setShowTooltip(true);
        ventesChartRenderer.setShowLines(false);
        ventesChartRenderer.setMarkers(false);
        ventesChartRenderer.setChartType("stackedBar");
        
        ventesChartSeries = new XYDataSetCollection();
        
        List<String[]> data = null;
        
        List<Assureur> listAssureur = new Assureur().listAssureur();
        String toolTipMsg = RessourceBundleUtil.getUIMessages().getString("sommeTotalVendu");
        for(Assureur a : listAssureur) {
			data = Stats.ventesMensuel(a.getIdAssureur(),this.typeSomme, startDate, endDate);
			
			ventedataList = new XYDataList();
			ventedataList.setLabel(a.getNomAssureur());
			ventedataList.setMarkers(false);
			ventedataList.setShowDataPoints(true);			
			
			for(String[] d : data) {
				
				ventedataList.addDataPoint(new XYDataPoint(Integer.parseInt(d[0]),Integer.parseInt(d[1]),a.getNomAssureur()+ ": "+toolTipMsg+" "+d[0]+" : "+d[1]+" FBU"));
				
			}
			ventesChartSeries.addDataList(ventedataList);
			
		}
	}
	
	private void generateProduitVenduLineChart() {
		
		produitVenduChartData = new FlotChartRendererData();
		produitVenduChartData.setHeight("400");
		String evolutionVente = RessourceBundleUtil.getUIMessages().getString("evolutionVente");
		produitVenduChartData.setTitle(evolutionVente);
		produitVenduChartData.setSubtitle("du "+this.startDate+" au "+this.endDate);
		produitVenduChartData.setShowYaxisLabels(true);
		produitVenduChartData.setShowXaxisLabels(true);
		produitVenduChartData.setShowDataPoints(true);
		String jour = RessourceBundleUtil.getUIMessages().getString("jour");
		produitVenduChartData.setXaxisTitle(jour);
		String qte = RessourceBundleUtil.getUIMessages().getString("quantite");
		produitVenduChartData.setYaxisTitle(qte);
        produitVenduChartData.setChartClickable(true);
        produitVenduChartData.setChartDraggable(false);
        produitVenduChartData.setTooltipFollowMouse(true);
        produitVenduChartData.setChartZoomable(false);
        produitVenduChartData.setShowTooltip(true);
        produitVenduChartData.setShowLines(true);
        
		//chartSeries = new XYDataSetCollection();
		List<String[]> data = Stats.produitsVendusParMois(this.startDate,this.endDate);
		
		xydataList = new XYDataList();
		String allSells = RessourceBundleUtil.getUIMessages().getString("allSells");
		xydataList.setLabel(allSells);
		xydataList.setMarkers(true);
		xydataList.setShowDataPoints(true);
		
		produitVenduChartSeries = new XYDataSetCollection();
		
		for(String[] d : data) {
			
			xydataList.addDataPoint(new XYDataPoint(Integer.parseInt(d[0]),Integer.parseInt(d[1]),d[1]));
			
		}
		
		produitVenduChartSeries.addDataList(xydataList);
		
		List<Assureur> listAssureur = new Assureur().listAssureur();
		
		for(Assureur a : listAssureur) {
			data = Stats.produitsVendusParMois(a.getIdAssureur(), startDate, endDate);
			
			xydataList = new XYDataList();
			xydataList.setLabel(a.getNomAssureur());
			xydataList.setMarkers(true);
			xydataList.setShowDataPoints(true);
			
			for(String[] d : data) {
				
				xydataList.addDataPoint(new XYDataPoint(Integer.parseInt(d[0]),Integer.parseInt(d[1]),d[1]));
				
			}
			produitVenduChartSeries.addDataList(xydataList);
		}
	}
	
	public void monthChanged(ValueChangeEvent e) {
		
		this.mois = Integer.parseInt(""+e.getNewValue());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, this.mois-1);
		this.startDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+"1";
	    this.endDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    
	    /*System.out.println(this.startDate);
	    System.out.println(this.endDate);*/
	    
	    generateProduitVenduLineChart();
	}
	
	public void venteBarChartMonthChanged(ValueChangeEvent e) {
		this.mois = Integer.parseInt(""+e.getNewValue());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, this.mois-1);
		this.startDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+"1";
	    this.endDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    
	    generateVenteBarChart();
	}
	public void anneeChanged(ValueChangeEvent e) {
		this.annee = Integer.parseInt(""+e.getNewValue());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.annee);
		
		this.startDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+"1";
	    this.endDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    
	    generateProduitVenduLineChart();
	}
	
	public void venteBarAnneeChanged(ValueChangeEvent e) {
		this.annee = Integer.parseInt(""+e.getNewValue());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.annee);
		
		this.startDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+"1";
	    this.endDate = String.valueOf(this.annee)+"-"+String.valueOf(this.mois)+"-"+String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
	    
	    generateVenteBarChart();
	}
	
	public void typeSommeChanged(ValueChangeEvent e) {
		System.out.println(e.getNewValue());
		generateVenteBarChart();
	}
	

	public long getSommeTotal() {
		return sommeTotal;
	}

	public void setSommeTotal(long sommeTotal) {
		this.sommeTotal = sommeTotal;
	}

	public String getSommeTotalMessage() {
		return sommeTotalMessage;
	}

	public void setSommeTotalMessage(String sommeTotalMessage) {
		this.sommeTotalMessage = sommeTotalMessage;
	}

	public FlotChartRendererData getChartData() {
		
		generatePieChart();
		
		return chartData;
	}

	public void setChartData(FlotChartRendererData chartData) {
		this.chartData = chartData;
	}

	public XYDataSetCollection getChartSeries() {
		return chartSeries;
	}

	public void setChartSeries(XYDataSetCollection chartSeries) {
		this.chartSeries = chartSeries;
	}

	public XYDataList getPiexydataList() {
		return piexydataList;
	}

	public void setPiexydataList(XYDataList piexydataList) {
		this.piexydataList = piexydataList;
	}

	public XYDataList getXydataList() {
		return xydataList;
	}

	public void setXydataList(XYDataList xydataList) {
		this.xydataList = xydataList;
	}
		
	public String getChartUrl() {
		return chartUrl;
	}

	public void setChartUrl(String chartUrl) {
		this.chartUrl = chartUrl;
	}

	public String getPanelTitle() {
		return panelTitle;
	}

	public void setPanelTitle(String panelTitle) {
		this.panelTitle = panelTitle;
	}
	

	public FlotChartRendererData getProduitVenduChartData() {
		return produitVenduChartData;
	}

	public void setProduitVenduChartData(FlotChartRendererData produitVenduChartData) {
		this.produitVenduChartData = produitVenduChartData;
	}

	public XYDataSetCollection getProduitVenduChartSeries() {
		generateProduitVenduLineChart();
		return produitVenduChartSeries;
	}

	public void setProduitVenduChartSeries(
			XYDataSetCollection produitVenduChartSeries) {
		this.produitVenduChartSeries = produitVenduChartSeries;
	}
	

	public FlotChartRendererData getVentesChartRenderer() {
		generateVenteBarChart();
		return ventesChartRenderer;
	}

	public void setVentesChartRenderer(FlotChartRendererData ventesChartRenderer) {
		this.ventesChartRenderer = ventesChartRenderer;
	}

	public XYDataSetCollection getVentesChartSeries() {
		return ventesChartSeries;
	}

	public void setVentesChartSeries(XYDataSetCollection ventesChartSeries) {
		this.ventesChartSeries = ventesChartSeries;
	}

	public XYDataList getVentedataList() {
		return ventedataList;
	}

	public void setVentedataList(XYDataList ventedataList) {
		this.ventedataList = ventedataList;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public List<SelectItem> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<SelectItem> monthList) {
		this.monthList = monthList;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}
	
	public List<SelectItem> getVenteBarChartOption() {
		return venteBarChartOption;
	}

	public void setVenteBarChartOption(List<SelectItem> venteBarChartOption) {
		this.venteBarChartOption = venteBarChartOption;
	}
	
	public int getTypeSomme() {
		return typeSomme;
	}

	public void setTypeSomme(int typeSomme) {
		this.typeSomme = typeSomme;
	}
	
	public FlotChartRendererData getProduitPieChartData() {
		return produitPieChartData;
	}

	public void setProduitPieChartData(FlotChartRendererData produitPieChartData) {
		this.produitPieChartData = produitPieChartData;
	}
	
	public XYDataSetCollection getProduitPieChartSeries() {
		this.generateProduitPieChart();
		return produitPieChartSeries;
	}

	public void setProduitPieChartSeries(XYDataSetCollection produitPieChartSeries) {
		this.produitPieChartSeries = produitPieChartSeries;
	}

	public XYDataList getProduitPieDataList() {
		return produitPieDataList;
	}

	public void setProduitPieDataList(XYDataList produitPieDataList) {
		this.produitPieDataList = produitPieDataList;
	}
	
	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}





	private FlotChartRendererData chartData;
	private FlotChartRendererData produitPieChartData;
	private FlotChartRendererData produitVenduChartData;
	private FlotChartRendererData ventesChartRenderer;
	
	private XYDataSetCollection chartSeries = null;
	private XYDataSetCollection produitPieChartSeries = null;
	private XYDataSetCollection produitVenduChartSeries = null;
	private XYDataSetCollection ventesChartSeries = null;
	
	private XYDataList xydataList = new XYDataList();
	private XYDataList produitPieDataList = new XYDataList();
	private XYDataList piexydataList = new XYDataList();
	private XYDataList ventedataList = new XYDataList();
	
	private String startDate;
	private String endDate;
	private Date dateDebut;
	private Date dateFin;
	
	private String chartUrl;
	private String panelTitle;
	
	List<SelectItem> monthList;
	List<SelectItem> venteBarChartOption;
	
	private long sommeTotal;
	private String sommeTotalMessage;
	
	private int mois;
	private int annee;
	private int typeSomme; 
}
