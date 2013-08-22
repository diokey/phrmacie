package pharmacie.entities;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class ExcelConverter {
	/**
	 * @author diokey olivier
	 *  This class Converts uplaod excel file into a List of Medics
	 */
	
	/**
	 * @param fileName the file Name of the file to convert.
	 * Must be a .xls file. .xlsx file is not supported yet
	 * @throws ParseException 
	 */
	public ExcelConverter(String fileName) throws ParseException{
		// TODO Auto-generated constructor stub
		this.fileName = fileName;
		this.medicament = new Medicament();
		this.listMedicament = new ArrayList<Medicament>();
		this.standardColumnNames = new ArrayList<String>();
		this.standardColumnNames.add("Code Medicament");
		this.standardColumnNames.add("Nom Medicament");
		this.standardColumnNames.add("Type");
		this.standardColumnNames.add("Ref Generique");
		this.standardColumnNames.add("Lot");
		this.standardColumnNames.add("Quantite");
		this.standardColumnNames.add("Date Fabrication");
		this.standardColumnNames.add("Date Expiration");
		
		//now we start conversion
		convert();
	}

	private void convert() throws ParseException{
		//process conversion
		
		List<String> columnNames = new ArrayList<String>();
		
		
		//first we get all rows in the file
		Vector<Vector<HSSFCell>> rows = ExcelReader.readExcelFile(this.fileName);
		if(rows==null) {
			throw new ParseException("Invalid data in the file",10);
		}
		
		//we get the first row to get column names
		Vector<HSSFCell> firstRow = rows.firstElement();		
		if(firstRow==null) {
			throw new ParseException("Invalid data in the file",11);
		}
		int columnCount = firstRow.size();
		for(int i=0 ; i<columnCount; i++) {
			HSSFCell column = firstRow.elementAt(i);
			System.out.println("Column "+i+":"+column.toString());
			columnNames.add(column.toString());
		}
		
		//check if we have at least 9 columns
		if(columnNames.size()<9) {
			throw new ParseException("Too few Columns",1);
		}
		
		//make sure column Order is exactely the same
		if(!columnMatch(columnNames)) {
			throw new ParseException("Column dismatch",2);
		}
		
		int rowCount = rows.size();
		//start from the second because the first is the column header
		for(int i=1 ; i<rowCount; i++) {
			Vector<HSSFCell> row = rows.elementAt(i);
			columnCount = row.size();
			this.medicament = new Medicament();
			HSSFCell cell1 = row.elementAt(0);
			String codeMedicament="";
			int codeIndex = cell1.toString().indexOf(".")==-1?0:cell1.toString().indexOf(".");
			codeMedicament = cell1.toString().substring(0,codeIndex);
			try{
				Integer.parseInt(codeMedicament);
			}catch(NumberFormatException e) {
				codeMedicament = cell1.toString();
			}
			this.medicament.setCodeMedicament(codeMedicament);
			HSSFCell cell2 = row.elementAt(1);
			this.medicament.setNomMedicament(cell2.toString());
			HSSFCell cell3 = row.elementAt(2);
			this.medicament.setTypeMedic(cell3.toString().toLowerCase());
			HSSFCell cell4 = row.elementAt(3);
			
			String generique="";
			
			if(!cell4.toString().equalsIgnoreCase("-")) {
				int generiqueIndex = cell4.toString().indexOf(".")==-1?0:cell4.toString().indexOf(".");
				generique = cell4.toString().substring(0,generiqueIndex);
				try{
					Integer.parseInt(generique);
				}catch(NumberFormatException e) {
					generique = cell4.toString();
				}
			}
			this.medicament.setcodeGenerique(generique);
			
			HSSFCell cell5 = row.elementAt(4);
			this.medicament.setBatch(cell5.toString());
			HSSFCell cell6 = row.elementAt(5);
			String quantite = cell6.toString().substring(0,cell6.toString().indexOf("."));
			this.medicament.setQuantiteStock(Integer.parseInt(quantite));
			HSSFCell cell7 = row.elementAt(6);
			Date manDate = cell7.getDateCellValue();
			this.medicament.setManifactureDate(manDate);
			HSSFCell cell8 = row.elementAt(7);
			this.medicament.setExpiryDate(cell8.getDateCellValue());
			HSSFCell cell9 = row.elementAt(8);
			String prix = cell9.toString().substring(0,cell9.toString().indexOf("."));
			this.medicament.getListAssureur().get(0).getPrix().setPrix(Integer.parseInt(prix));
			
			
			this.listMedicament.add(this.medicament);
			
			int assureurs = this.medicament.getListAssureur().size()-1; //minus 1 for cash payement
			
			System.out.println("Columns: "+columnCount+" Assureurs : "+assureurs);
			
			int assureurFournis = (columnCount-9)/3;
			
			System.out.println("Assureur fournis: "+assureurFournis);
			
			if(assureurFournis<assureurs) {
				System.err.println("Certains prix des assureurs ne sont pas fournis");
			}else{
				if(assureurFournis>assureurs){
					System.err.println("Certains prix correspondent aux fournisseur non existant");
				}
			}
			
			int count=1;
			this.medicament.getListAssureur().get(0).getPrix().setReduction(0);
			for(int x= 9; x<columnCount; x+=3) {
				HSSFCell prixCell = row.elementAt(x);
				prix = prixCell.toString().substring(0,prixCell.toString().indexOf("."));			
				this.medicament.getListAssureur().get(count).getPrix().setPrix(Integer.parseInt(prix));
				HSSFCell reductionCell = row.elementAt(x+1);
				String red = reductionCell.toString().substring(0,reductionCell.toString().indexOf("."));
				Integer reduction = red.equalsIgnoreCase("")?null:Integer.parseInt(red);
				this.medicament.getListAssureur().get(count).getPrix().setReduction(reduction);
				String mention = row.elementAt(x+2).toString();
				if(mention.equalsIgnoreCase("-")) {
					mention="";
				}else{
					if(!this.contains(mention)) {
						mention="";
					}
				}
				this.medicament.getListAssureur().get(count).getPrix().setMentionString(mention);
				count++;
				//System.out.println("count: "+count+" "+prix+":"+reduction+":"+mention+"\t");
				
			}
		}
		//the 8th is just medic information.
		//Now let's take the medic prices and specification
		
		
		printMedic();
	}
	
	public boolean contains(String mention) {
		for(Mention m : this.listeMention) {
			if(m.getDescription().equalsIgnoreCase(mention))
				return true;
		}
		
		return false;
	}
	
	public void printMedic() {
		for(Medicament m : this.listMedicament) {
			System.out.print(m.getCodeMedicament()+"\t");
			System.out.print(m.getNomMedicament()+"\t");
			System.out.print(m.getTypeMedic()+"\t");
			System.out.print(m.getcodeGenerique()+"\t");
			System.out.print(m.getBatch()+"\t");
			System.out.print(m.getQuantiteStock()+"\t");
			System.out.print(m.getManifactureDate()+"\t");
			System.out.print(m.getExpiryDate()+"\t");
			List<Assureur> assureurs = m.getListAssureur();
			for(Assureur ass : assureurs) {
				System.out.print(ass.getPrix().getPrix()+"\t");
				System.out.print(ass.getPrix().getMentionString()+"\t");
				System.out.print(ass.getPrix().getReduction()+"\t");
			}
			
			System.out.println();
		}
	}
	
	private boolean columnMatch(List<String> param) {
		int i=0;
		for(String name : this.standardColumnNames) {
			String name2 = param.get(i);
			if(!name2.equalsIgnoreCase(name)) {
				return false;
			}
			i++;
		}
		return true;
	}
		
	public Medicament getMedicament() {
		return medicament;
	}

	public void setMedicament(Medicament medicament) {
		this.medicament = medicament;
	}

	public List<String> getStandardColumnNames() {
		return standardColumnNames;
	}

	public void setStandardColumnNames(List<String> standardColumnNames) {
		this.standardColumnNames = standardColumnNames;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<Medicament> getListMedicament() {
		return listMedicament;
	}

	public void setListMedicament(List<Medicament> listMedicament) {
		this.listMedicament = listMedicament;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
		new ExcelConverter("/home/geek/test2.xls");
		}catch(ParseException e) {
			
		}
	}
	
	private Medicament medicament;
	private List<Medicament> listMedicament;
	private List<String> standardColumnNames;
	private String fileName;
	private List<Mention> listeMention = new Mention().listMention();
	
}
