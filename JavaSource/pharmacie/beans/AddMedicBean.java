package pharmacie.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import pharmacie.entities.Assureur;
import pharmacie.entities.Medicament;
import pharmacie.entities.Mention;
import pharmacie.util.RessourceBundleUtil;

@ViewScoped
@ManagedBean
public class AddMedicBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public AddMedicBean() {
		// TODO Auto-generated constructor stub
	}
	
	//methods
	@PostConstruct
	public void init() {
		this.medicament = new Medicament();
		this.medicament.setDateArrive(new Date());
		this.codeRender.add("codeMedicMsg");
	}
	
	public void findGeneriqueId() {
		/*if(this.medicament.getGenerique()==null || this.medicament.getGenerique().isEmpty()){
			System.out.println("Not Set... exiting...");
		}*/
		if(this.medicament.getGenerique().split("-").length<2) {
			return;
		}
		String codeGenerique = this.medicament.getGenerique().split("-")[0];
		
		this.medicament.setcodeGenerique(codeGenerique);
		
	}
	
	public void onlistClick() {
		if(this.code==null)
			return;
		for(Medicament m : this.suggestionMedicaments) {
			
			if(this.code.equalsIgnoreCase(m.getCodeMedicament())) {
				this.medicament = new Medicament(m);
				this.medicament.setExists(true);
				this.medicament.setDateArrive(new Date());
				
				for(Assureur s : m.getListAssureur()) {
					System.out.println(s.getNomAssureur()+" : "+s.getPrix().getPrix());
					
				}
				return;
			}
		}
	}
	
	public void saveMedic() {
		
		this.message="";
		this.messageImg="";
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		String successMsg = RessourceBundleUtil.getUIMessages().getString("addMedicSuccess");
		//d'abbord on recupere le medicament
		System.out.println("code "+medicament.getCodeMedicament()+" NOm: "+medicament.getNomMedicament()+" Type: "+medicament.getTypeMedic());
	
		System.out.println("Type medcamnent: "+this.medicament.getTypeMedic());
		int idStock = 0;
		
		if(!this.medicament.isExists()) {
			idStock = this.medicament.addMedicament();
		}else{
			idStock = this.medicament.putIntoStock(this.medicament.getIdMedicament());
			//System.out.println("Id medicament: "+this.medicament.getIdMedicament());
			
		}
		
		if(idStock<=0) {
			this.messageImg="/images/stop.png";
			this.message=errorMsg;
			return;
		}
		
		for(Assureur assureur : this.medicament.getListAssureur()) {
			if(!assureur.saveTarifAssureur(idStock)){
				this.messageImg="/images/stop.png";
				this.message=errorMsg;
				return;
			}
		}
		
		this.messageImg="/images/accept-24.png";
		this.message=successMsg;
		//reinistialize the form
		this.medicament = new Medicament();
		this.medicament.setDateArrive(new Date());
		this.listAssureur = null;
		this.suggestionMedicaments = null;
		//System.out.println("Generique medicament"+this.medicament.getIdGenerique());
		
	}
	
	public List<Medicament> suggestMedicament(String prefix) {
		
		List<Medicament> suggestions = new ArrayList<Medicament>();
		if(this.suggestionMedicaments == null)
			this.suggestionMedicaments = new Medicament().listSuggestion();
		
		for(Medicament m : this.suggestionMedicaments) {
			
			if(m.getCodeMedicament().toLowerCase().startsWith(prefix.toLowerCase())) {
				suggestions.add(m);
			}
		}
		
		return suggestions;
	}
	
	public List<Medicament> suggestGenerique(String prefix) {
		
		List<Medicament> suggestions = new ArrayList<Medicament>();
		if(this.suggestionMedicaments == null)
			this.suggestionMedicaments = new Medicament().listSuggestion();
		for(Medicament m : this.suggestionMedicaments) {
			if((m.getNomMedicament().toLowerCase().startsWith(prefix.toLowerCase()) && m.getTypeMedic().equalsIgnoreCase("generique")) ||
					(m.getCodeMedicament().toLowerCase().startsWith(prefix.toLowerCase()) && m.getTypeMedic().equalsIgnoreCase("generique"))) {
				if(!containsElement(suggestions,m)) {
					suggestions.add(m);
				}
			}
		}
		
		return suggestions;
	}
	
	private boolean containsElement(List<Medicament> medics, Medicament medicament) {
		for(Medicament m : medics) {
			return m.getCodeMedicament().equalsIgnoreCase(medicament.getCodeMedicament()) ||
			m.getNomMedicament().equalsIgnoreCase(medicament.getNomMedicament());
		}
		return false;
	}
	
	public void validateManifactureDate(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException {
		//validation manifacture date.. 
		if(fc==null || comp == null || ref == null) {
			
			return;
		}
			
		final Date manDate=(Date) ref;
		
		Date today=new Date();
		
		if(manDate.compareTo(today)>=0) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Date Invalide","Date Invalide");
			throw new ValidatorException(message);
		}
	}
	
	public void validateCodeMedicament(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException{
		if(fc == null || comp == null || ref == null)
			return;
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("required");
		final String codeRef = (String) ref;
		if(codeRef.length()<1){
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,errorMsg);
			throw new ValidatorException(message);
		}
		
		if(this.code==null){
			this.code = codeRef;
			
			this.onlistClick();
			if(this.codeRender != null) {
				this.codeRender.clear();
				this.codeRender.add("medic-data");
				this.codeRender.add("prix-panel");
			}
		}
		
	}
	
	public void validateGenerique(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException {
		if(fc == null || comp == null || ref == null)
			return;
		
		final String codeRef = (String) ref;
		if(codeRef.isEmpty()) {
			System.out.println("empty");
			return;
		}
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("noGenerique");
		String data[]=codeRef.split("-");
		if(data.length<2) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,errorMsg);
			throw new ValidatorException(message);
		}
		String codeGenerique = codeRef.split("-")[0];
		String nomGenerique = codeRef.split("-")[1];
		
		Medicament generique = null;
		
		for(Medicament m : this.suggestionMedicaments) {
			if((m.getCodeMedicament().equalsIgnoreCase(codeGenerique) && m.getTypeMedic().equalsIgnoreCase("generique"))||
					(m.getNomMedicament().equalsIgnoreCase(nomGenerique) && m.getTypeMedic().equalsIgnoreCase("generique"))) {
				generique = m;
			}
		}
		
		if(generique == null) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,errorMsg,errorMsg);
			throw new ValidatorException(message);
		}
		
		this.medicament.setGenerique(codeRef);
		this.findGeneriqueId();
		
	}
	
	private void suggestMention() {
		// TODO Auto-generated method stub
		if(this.medicament!=null) {
			List<Mention> listMention = this.medicament.getListMention();
			if(listMention==null || listMention.isEmpty())
				return;
			this.mentions = new ArrayList<SelectItem>();
			this.mentions.add(new SelectItem(0,""));
			for(Mention m : listMention) {
				this.mentions.add(new SelectItem(m.getIdMention(),m.getDescription()));
			}
		}
	}
		
	//getters and setters
	public Medicament getMedicament() {
		return medicament;
	}

	public void setMedicament(Medicament medicament) {
		this.medicament = medicament;
	}
		
	public List<Assureur> getListAssureur() {
		if(this.listAssureur == null)
			this.listAssureur = new Assureur().listAssureur();
		return listAssureur;
	}

	public void setListAssureur(List<Assureur> listAssureur) {
		this.listAssureur = listAssureur;
	}
	
	public List<SelectItem> getMentions() {
		if(mentions == null)
			this.suggestMention();
		return mentions;
	}
	
	public void setMentions(List<SelectItem> mentions) {
		this.mentions = mentions;
	}

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessageImg() {
		return messageImg;
	}

	public void setMessageImg(String messageImg) {
		this.messageImg = messageImg;
	}
	
	public List<Medicament> getSuggestionMedicaments() {
		return suggestionMedicaments;
	}

	public void setSuggestionMedicaments(List<Medicament> suggestionMedicaments) {
		this.suggestionMedicaments = suggestionMedicaments;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
			
	public List<String> getCodeRender() {
		return codeRender;
	}

	public void setCodeRender(List<String> codeRender) {
		this.codeRender = codeRender;
	}


	//private properties
	private Medicament medicament;
	private List<Assureur> listAssureur;
	private List<SelectItem> mentions;
	
	private String message;
	private String messageImg;
	private List<Medicament> suggestionMedicaments = new Medicament().listSuggestion();
	private String code;
	private List<String> codeRender = new ArrayList<String>();
	
	//temp
	

}
