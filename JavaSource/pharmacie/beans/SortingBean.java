package pharmacie.beans;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;

import pharmacie.entities.Medicament;

@ManagedBean
@ViewScoped
public class SortingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SortingBean() {
		// TODO Auto-generated constructor stub
	}
	
	private void modifySortProperty(Map<String, SortOrder> orders, String sortProperty, SortOrder currentOrder) {
		
		if ((currentOrder == null) || (currentOrder == SortOrder.ascending)) {
			orders.put(sortProperty, SortOrder.descending);
		} else {
			orders.put(sortProperty, SortOrder.ascending);
		}
	}
	
	/*private String getSortProperty() {
		String sortProperty = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap().get(SORT_PROPERTY);
		return sortProperty;
	}*/
	
	public Comparator<Medicament> getDateArriveComparator() {
		return new Comparator<Medicament>() {
			@Override
			public int compare(Medicament o1, Medicament o2) {
				return o1.getDateArrive().compareTo(o2.getDateArrive());
			}
		};
	}
	
	public Comparator<Medicament> getExpiryDateComparator() {
		return new Comparator<Medicament>(){

			@Override
			public int compare(Medicament arg0, Medicament arg1) {
				// TODO Auto-generated method stub
				return arg0.getExpiryDate().compareTo(arg1.getExpiryDate());
			}
			
		};
	}

	public Comparator<Medicament> getManufactureDateComparator() {
		return new Comparator<Medicament>(){

			@Override
			public int compare(Medicament arg0, Medicament arg1) {
				// TODO Auto-generated method stub
				return arg0.getManifactureDate().compareTo(arg1.getManifactureDate());
			}
			
		};
	}

	public void sort() {
		System.out.println("call");
		//String sortProperty = getSortProperty();
		System.out.println(sortProperty);
		SortOrder currentOrder = sortOrders.get(sortProperty);
		System.out.println(currentOrder);
		sortOrders.clear();
		modifySortProperty(sortOrders, sortProperty, currentOrder);
	}

	
	public Map<String, SortOrder> getSortOrders() {
		return sortOrders;
	}

	public void setSortOrders(Map<String, SortOrder> sortOrders) {
		this.sortOrders = sortOrders;
	}
	


	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}



	private Map<String, SortOrder> sortOrders = new HashMap<String, SortOrder>();
	private String sortProperty;

}
