package pharmacie.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;


@ManagedBean
@SessionScoped
public class ControllerBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ControllerBean() {
		// TODO Auto-generated constructor stub
		
		if(u.isConnected() && u.getUser().isPatron()) {
			this.statistique = true;
			this.vente = false;
		}else{
			this.vente = true;
			this.statistique = false;
		}
	}
	
	public void reload(ActionEvent event){
		
		
		if(u.isConnected() && u.getUser().isPatron()) {
			this.statistique = true;
			this.vente = false;
		}else{
			this.vente = true;
			this.statistique = false;
		}
		
		this.caisse=false;
		this.stock=false;
		this.credit=false;
	}
	
	public void changePage(ActionEvent event) {
		
		this.menuId = event.getComponent().getId();
		
		if(this.menuId.equals("venteCmd"))
			this.vente=true;
		else
			this.vente=false;
		
		if(this.menuId.equals("caisseCmd"))
			this.caisse=true;
		else
			this.caisse=false;
		if(this.menuId.equals("creditCmd"))
			this.credit=true;
		else
			this.credit=false;
		if(this.menuId.equals("medicamentCmd"))
			this.stock=true;
		else
			this.stock=false;
		if(this.menuId.endsWith("statistiqueCmd"))
			this.statistique = true;
		else
			this.statistique = false;
			
	}
	
	public boolean isVente() {
		return vente;
	}

	public void setVente(boolean vente) {
		this.vente = vente;
	}

	public boolean isCredit() {
		return credit;
	}

	public void setCredit(boolean credit) {
		this.credit = credit;
	}

	public boolean isCaisse() {
		return caisse;
	}

	public void setCaisse(boolean caisse) {
		this.caisse = caisse;
	}

	public boolean isStock() {
		return stock;
	}

	public void setStock(boolean stock) {
		this.stock = stock;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getPanelURI() {
		return panelURI;
	}

	public void setPanelURI(String panelURI) {
		this.panelURI = panelURI;
	}
	
		
	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean isAutosized() {
		return autosized;
	}

	public void setAutosized(boolean autosized) {
		this.autosized = autosized;
	}
	
	public String getPanelInfoURI() {
		return panelInfoURI;
	}

	public void setPanelInfoURI(String panelInfoURI) {
		this.panelInfoURI = panelInfoURI;
	}
	
	public boolean isClose() {
		
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
	
	
	public boolean isStatistique() {
		return statistique;
	}

	public void setStatistique(boolean statistique) {
		this.statistique = statistique;
	}


	private String menuId;
	
	private boolean vente = true;
	private boolean credit = false;
	private boolean caisse = false;
	private boolean stock = false;
	private boolean close = false;
	private boolean statistique = true;
	
	UserBean u = new UserBean();
	
	private String panelURI="/panels/updateProfile.xhtml";
	
	private String panelInfoURI;
	
	private boolean resizable;
	private boolean movable;
	private boolean autosized;

}
