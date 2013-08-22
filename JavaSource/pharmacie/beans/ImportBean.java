package pharmacie.beans;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import pharmacie.entities.Assureur;
import pharmacie.entities.ExcelConverter;
import pharmacie.entities.Medicament;
import pharmacie.entities.MedicamentImport;
import pharmacie.entities.Mention;
import pharmacie.util.FacesUtil;
import pharmacie.util.RessourceBundleUtil;

@SessionScoped
@ManagedBean
public class ImportBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ImportBean() {
		// TODO Auto-generated constructor stub
		this.file = new FileBean();
		//this.listMedicament = new ArrayList<Medicament>();
	}

	public void uploadFile(FileUploadEvent event) {
		UploadedFile f = event.getUploadedFile();
		this.file.setName(f.getName());
		this.file.setLength(f.getSize());
		this.file.setData(f.getData());
		this.file.saveFile("/upload/");
		
		this.fileNameLocation = FacesUtil.getRessourcePath("/upload/"+this.file.getName());
		
		System.out.println(this.fileNameLocation);
	}
	
	public void processImport(AjaxBehaviorEvent event) {
		System.out.println("processing...");
		this.message="";
		this.messageImg="";
		
		try {
			ExcelConverter converter = new ExcelConverter(this.fileNameLocation);
			List<Medicament>  medicaments = converter.getListMedicament();
			
			if(this.listMedicament == null)
				this.listMedicament = new ArrayList<MedicamentImport>();
			
			for(Medicament m : medicaments) {
				this.listMedicament.add(new MedicamentImport(m));
			}
			if(this.listMedicament == null) {
				this.message="Il parait qu'il n'y a aucune donnee a Importer";
				this.messageImg="/images/warning.png";
				return;
			}
			
			
			this.columnList = converter.getStandardColumnNames();
			
			
			
			this.message= RessourceBundleUtil.getUIMessages().getString("importSuccess");
			this.messageImg="/images/accept-24.png";
			
			processValidation();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			this.message = RessourceBundleUtil.getUIMessages().getString("error")+" : "+e.getMessage();
			this.messageImg ="/images/stop.png";
		}
		
	}
	
	/**
	 * @param void
	 * @return void
	 * checks if the medicament is unique in the list
	 */
	private void checkUniquenessInList() {
		String message = RessourceBundleUtil.getUIMessages().getString("importcodeMedicExist");
		for(MedicamentImport m : this.listMedicament) {
			if(occurences(m)) {
				m.setStatusIcon("/images/error.png");
				m.setStatusMessage(message);
				
			}
		}
	}
	
	/**
	 * @param void
	 * @return void
	 * checks if the code medicament already exists in the database
	 */
	private void checkUniquenessInData() {
		String m1 = RessourceBundleUtil.getUIMessages().getString("importcodeMedicExistsInDB");
		String m2 = RessourceBundleUtil.getUIMessages().getString("importMedicExists");
		for(MedicamentImport m : this.listMedicament) {
			Medicament dbMedic = Medicament.getMedicamentByCode(m.getCodeMedicament());
			if(dbMedic != null) {
				if(!dbMedic.getNomMedicament().equalsIgnoreCase(m.getNomMedicament())) {
					m.setExists(false);
					m.setStatusIcon("/images/error.png");
					m.setStatusMessage(m1);
				}else{
					m.setExists(true);
					m.setStatusIcon("/images/warning.png");
					m.setStatusMessage(m2);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param void 
	 * @return void
	 * check if generiques references already exists in the list
	 */
	private void checkGeneriqueReferences() {
		String msg = RessourceBundleUtil.getUIMessages().getString("importgeneriqueNotExists");
		for(MedicamentImport med : this.listMedicament) {
			if(med.getTypeMedic().equalsIgnoreCase("specialite") && !med.getcodeGenerique().isEmpty()) {
				if(!codeGeneriqueExists(med.getcodeGenerique()) && ! existsInDB(med.getcodeGenerique())){
					med.setStatusIcon("/images/error.png");
					med.setStatusMessage(msg);
				}
			}
		}
	}
	
	private boolean existsInDB(String codeGenerique) {
		// TODO Auto-generated method stub		
		return Medicament.getIdMedicamentByCode(codeGenerique)>0;		
	}

	public boolean codeGeneriqueExists(String codeGenerique) {
		for(Medicament m : this.listMedicament) {
			if(m.getTypeMedic().equalsIgnoreCase("generique") && m.getCodeMedicament().equalsIgnoreCase(codeGenerique)){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean occurences(MedicamentImport medic) {
		int count=0;
		final String code = medic.getCodeMedicament();
		for(MedicamentImport med : this.listMedicament) {
			if(med.getCodeMedicament().equalsIgnoreCase(code)){
				count++;
				if(count>=2) {
					return true;
				}
				
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param medics
	 * @return List<Medicament> reordered in a way that generique medicaments appears in the first of the list
	 */
	private List<MedicamentImport> reorderMedication(List<MedicamentImport> medics) {
		
		List<MedicamentImport> tempGenerique = new ArrayList<MedicamentImport>();
		List<MedicamentImport> tempSpecialite = new ArrayList<MedicamentImport>();
		for(MedicamentImport med : medics) {
			//we first add only generic medicine in the list
			if(med.getTypeMedic().equalsIgnoreCase("generique") && med.getcodeGenerique().isEmpty()) {
				tempGenerique.add(med);
			}else{
				if(med.getTypeMedic().equalsIgnoreCase("specialite") && ! med.getcodeGenerique().isEmpty()) {
					tempSpecialite.add(med);
				}
			}
		}
		
		tempGenerique.addAll(tempSpecialite);
		
		return tempGenerique;
	}
	
	public void saveUploadedData() {
		this.validationImg="";
		this.validationMsg="";
		
		String errorMsg = RessourceBundleUtil.getUIMessages().getString("error");
		
		this.listMedicament = reorderMedication(this.listMedicament);
		
		for(MedicamentImport m : this.listMedicament) {
			int idStock=0;
			m.setDateArrive(new Date());
			if(m.isExists()) {
				if(Integer.parseInt(this.action)==2){
					idStock = m.putIntoStock(Medicament.getIdMedicamentByCode(m.getCodeMedicament()));
				}else{				
					continue;
				}
			}else{
				idStock = m.addMedicament();
			}
			
			if(idStock<=0) {
				this.messageImg="/images/stop.png";
				this.message=errorMsg;
				return;
			}
			
			for(Assureur assureur : m.getListAssureur()) {
				assureur.getPrix().setIdMention(Mention.getId(assureur.getPrix().getMentionString()));
				if(!assureur.saveTarifAssureur(idStock)){
					this.messageImg="/images/stop.png";
					this.message=errorMsg;
					return;
				}
			}
		}
		
		this.messageImg="/images/accept-24.png";
		this.message=RessourceBundleUtil.getUIMessages().getString("importedsavedSuccess");
		this.listMedicament=null;
		
	}
		
	public void validateData() {
		this.message="";
		this.messageImg="";
		processValidation();
	}
	
	public void processValidation() {
		
		for(MedicamentImport m : this.listMedicament) {
			m.setStatusIcon("/images/accept-24.png");
			m.setStatusMessage("OK");
		}
		checkUniquenessInList();
		checkUniquenessInData();
		checkGeneriqueReferences();
		
		getErrorsAndWarnings();
		
	}
	
	private void getErrorsAndWarnings() {
		
		this.validationMsg = RessourceBundleUtil.getUIMessages().getString("Ilya");
		int nbErrors=0;
		int nbWarnings=0;
		for(MedicamentImport m : this.listMedicament) {
			String icon = m.getStatusIcon();
			if(icon.equalsIgnoreCase("/images/error.png")) {
				nbErrors++;
			}
			if(icon.equalsIgnoreCase("/images/warning.png")) {
				nbWarnings++;
			}
		}
		this.validationMsg+=" "+nbErrors+" "+RessourceBundleUtil.getUIMessages().getString("importError")+" , "+nbWarnings+" "+RessourceBundleUtil.getUIMessages().getString("importwarning");
	
		if(nbErrors>0) {
			this.validationMsg+="<br /> "+RessourceBundleUtil.getUIMessages().getString("importCorrectBefore");
			this.validationMsg="<span style='color:red'>"+this.validationMsg+"</span>";
			this.validationImg="/images/error.png";
		}else{
			if(nbErrors==0) {
				this.validationMsg+="<br /> "+RessourceBundleUtil.getUIMessages().getString("importcanSave");
				this.validationMsg="<span style='color:black'>"+this.validationMsg+"</span>";
				this.validationImg="/images/info.png";
			}
		}
		
	}
	
	public void printData() {
		System.out.println("-------------------------");
		for(MedicamentImport m : this.listMedicament) {
			
			System.out.print(m.getCodeMedicament()+"\t");
			System.out.print(m.getNomMedicament()+"\t");
			System.out.print(m.getcodeGenerique()+"\t");
			System.out.println(m.getTypeMedic()+"\t");
			System.out.println();
		}
	}
	
	public void annulerImport() {
		this.listMedicament = null;
		this.message="";
		this.messageImg="";
		this.validationImg="";
		this.validationMsg="";
		System.out.println("reset");
	}
	
	public void processRemoval() {
		if(this.selectIndex<0) {
			System.out.println("No index selected");
			return ;
		}
		
		System.out.println("index selected"+this.selectIndex);
		
		if(!this.listMedicament.isEmpty()) {
			System.out.println("size: "+this.listMedicament.size());
			this.listMedicament.remove(selectIndex);
		}
	}
	
	public int getAssureurCount() {
		if(this.getListAssureur()!=null) {
			return this.getListAssureur().size();
		}
		return 0;
	}
	
	public int getAllColumnsCount() {
		return getAssureurCount() * 3;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public FileInputStream getFileStream() {
		return fileStream;
	}

	public void setFileStream(FileInputStream fileStream) {
		this.fileStream = fileStream;
	}

	public FileBean getFile() {
		return file;
	}

	public void setFile(FileBean file) {
		this.file = file;
	}

	public String getFileNameLocation() {
		return fileNameLocation;
	}

	public void setFileNameLocation(String fileNameLocation) {
		this.fileNameLocation = fileNameLocation;
	}

	public List<MedicamentImport> getListMedicament() {
		return listMedicament;
	}

	public void setListMedicament(List<MedicamentImport> listMedicament) {
		this.listMedicament = listMedicament;
	}

	public List<String> getColumnList() {
		
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<Assureur> getListAssureur() {
		return listAssureur;
	}

	public void setListAssureur(List<Assureur> listAssureur) {
		this.listAssureur = listAssureur;
	}
		
	public boolean isErrors() {
		errors = false;
		if(this.listMedicament!=null) {
			for(MedicamentImport m : this.listMedicament) {
				if(m.getStatusIcon().equalsIgnoreCase("/images/error.png")){
					return true;
				}
			}
		}
		return errors;
	}

	public void setErrors(boolean errors) {
		this.errors = errors;
	}

	
	public boolean isWarnings() {
		warnings = false;
		if(this.listMedicament!=null) {
			for(MedicamentImport m : this.listMedicament) {
				if(m.getStatusIcon().equalsIgnoreCase("/images/warning.png")){
					if(!isErrors()){
						return true;
					}
				}
			}
		}
		return warnings;
	}

	public void setWarnings(boolean warnings) {
		this.warnings = warnings;
	}

	public List<SelectItem> getMentions() {
		if(mentions == null) {
			mentions = new ArrayList<SelectItem>();
			List<Mention> listMention = new Mention().listMention();
			mentions.add(new SelectItem(""));
			for(Mention m : listMention){
				mentions.add(new SelectItem(m.getDescription()));
			}
		}
		return mentions;
	}

	public void setMentions(List<SelectItem> mentions) {
		this.mentions = mentions;
	}
	
	public String getValidationMsg() {
		return validationMsg;
	}

	public void setValidationMsg(String validationMsg) {
		this.validationMsg = validationMsg;
	}

	public String getValidationImg() {
		return validationImg;
	}

	public void setValidationImg(String validationImg) {
		this.validationImg = validationImg;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}


	//private properties
	private String message;
	private String messageImg;
	private String validationMsg;
	private String validationImg;
	private String action="1";
	private boolean disabled;
	private int selectIndex = -1;
	private FileInputStream fileStream;
	FileBean file;
	String fileNameLocation;
	
	private List<MedicamentImport> listMedicament;
	
	private List<String> columnList;
	private List<Assureur> listAssureur = new Medicament().getListAssureur();
	
	private List<SelectItem> mentions;
	
	private boolean errors;
	private boolean warnings;
}
