package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import pharmacie.entities.Historique;
import pharmacie.entities.Medicament;
import pharmacie.util.RessourceBundleUtil;


@ManagedBean
@ViewScoped
public class HistoriqueBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Medicament> allMedicaments;
	private List<Historique> historiqueAchatMedicament;
	private List<Historique> historiqueVenteMedicament;
	private String selectedMedicament;
	private String codeMedicament;
	private String nomMedicament;
	private int idMedicament;
	
	
	//constructor
	public HistoriqueBean() {
		// TODO Auto-generated constructor stub
		this.allMedicaments = new Medicament().nomMedicaments();
	}
	
	public void validatetSelectedMedicament(FacesContext fc, UIComponent ref, Object value) throws ValidatorException{
		
		System.out.println("validating..."+fc+" 2: "+ref+" 3: "+value);
		if(fc==null || ref==null || value == null)
			return;
		
		String msg="";
		
		int medicamentId = 0;
		String data[] = ((String)value).split("-");
		if(data.length < 2) {
			medicamentId = Medicament.getIdMedicamentByCode(data[0]);
			
			if(medicamentId==0) {
				medicamentId = Medicament.getIdMedicamentByName(data[0]);
			}
			
		}else{
			medicamentId = Medicament.getIdMedicamentByCode(data[0]);
		}
		
		if(medicamentId==0) {
			msg = RessourceBundleUtil.getUIMessages().getString("unknownmedic");
			this.historiqueVenteMedicament.clear();
			this.historiqueVenteMedicament.clear();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,msg,msg);
			
			throw new ValidatorException(message);
		}else{
			this.idMedicament = medicamentId;
			this.selectedMedicament = (String) value;
			this.historiqueAchatMedicament = null;
			this.historiqueVenteMedicament = null;
		}
		
		System.out.println("Id medicament: "+this.idMedicament);
		
	}
	
	public List<Historique> getHistoriqueVenteMedicament() {
		if(historiqueVenteMedicament==null) {
			historiqueVenteMedicament = new Historique().getHistoriqueVente(idMedicament);
		}
		return historiqueVenteMedicament;
	}

	public void setHistoriqueVenteMedicament(
			List<Historique> historiqueVenteMedicament) {
		this.historiqueVenteMedicament = historiqueVenteMedicament;
	}

	public List<Historique> getHistoriqueAchatMedicament() {
		if(historiqueAchatMedicament==null) {
			historiqueAchatMedicament = new Historique().getHistoriqueAchat(idMedicament);
		}
		return historiqueAchatMedicament;
	}

	public void setHistoriqueAchatMedicament(
			List<Historique> historiqueAchatMedicament) {
		this.historiqueAchatMedicament = historiqueAchatMedicament;
	}

	public String getNomMedicament() {
		return nomMedicament;
	}

	public void setNomMedicament(String nomMedicament) {
		this.nomMedicament = nomMedicament;
	}

	public int getIdMedicament() {
		return idMedicament;
	}

	public void setIdMedicament(int idMedicament) {
		this.idMedicament = idMedicament;
	}

	public List<Medicament> suggestMedicament(String prefix) {
		prefix = prefix.toLowerCase();
		List<Medicament> result = new ArrayList<Medicament>();
		
		for(Medicament m : this.allMedicaments) {
			if(m.getNomMedicament().toLowerCase().startsWith(prefix) || m.getCodeMedicament().toLowerCase().startsWith(prefix)) {
				result.add(m);
			}
		}
		
		return result;
	}

	//getters and setters
	public List<Medicament> getAllMedicaments() {
		return allMedicaments;
	}

	public void setAllMedicaments(List<Medicament> allMedicaments) {
		this.allMedicaments = allMedicaments;
	}

	public String getSelectedMedicament() {
		return selectedMedicament;
	}

	public void setSelectedMedicament(String selectedMedicament) {
		this.selectedMedicament = selectedMedicament;
	}

	public String getCodeMedicament() {
		return codeMedicament;
	}

	public void setCodeMedicament(String codeMedicament) {
		this.codeMedicament = codeMedicament;
	}
	

}
