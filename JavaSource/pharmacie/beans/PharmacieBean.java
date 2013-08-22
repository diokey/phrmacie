package pharmacie.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import pharmacie.entities.OwnerData;

@RequestScoped
@ManagedBean
public class PharmacieBean {

	public PharmacieBean() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	//getters and setters
	public OwnerData getPharmacie() {
		return pharmacie;
	}

	public void setPharmacie(OwnerData pharmacie) {
		this.pharmacie = pharmacie;
	}


	//private members
	OwnerData pharmacie = new OwnerData();

}
