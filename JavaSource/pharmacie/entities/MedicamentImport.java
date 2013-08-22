package pharmacie.entities;


public class MedicamentImport extends Medicament {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MedicamentImport() {
		// TODO Auto-generated constructor stub
	}

	
	public MedicamentImport(Medicament m) {
		super(m);
	}
	
		
	public String getStatusIcon() {
		return statusIcon;
	}

	public void setStatusIcon(String statusIcon) {
		this.statusIcon = statusIcon;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}



	private String statusIcon;
	private String statusMessage;
	private boolean exists;
}
