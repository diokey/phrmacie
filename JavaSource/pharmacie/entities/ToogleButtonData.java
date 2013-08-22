package pharmacie.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ToogleButtonData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ToogleButtonData() {
		// TODO Auto-generated constructor stub
	}

	private void builButtons() {
		List<Assureur> assureurs = new Assureur().listAssureur();
		this.buttons = new ArrayList<ToogleButtonData>();
		ToogleButtonData first = new ToogleButtonData();
		first.classes="toggle-item toggle-item-first";
		first.label="Tous";
		first.id="allCmd";
		first.value = 0;
		first.style="#ef7e05";
		this.buttons.add(first);
		int nbAssureur = assureurs.size();
		Assureur a = null;
		for(int i=0 ;i< nbAssureur -1 ; i++) {
			a = assureurs.get(i);
			first = new ToogleButtonData();
			first.classes="toggle-item toogle-item-middle";
			first.label=a.getNomAssureur();
			first.id=a.getNomAssureur()+"Cmd";
			first.value = a.getIdAssureur();
			first.style="#005000";
			buttons.add(first);
		}
		
		a = assureurs.get(nbAssureur-1);
		first = new ToogleButtonData();
		first.classes="toggle-item toggle-item-last";
		first.label=a.getNomAssureur();
		first.id=a.getNomAssureur()+"Cmd";
		first.value = a.getIdAssureur();
		buttons.add(first);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	public List<ToogleButtonData> getButtons() {
		if(buttons == null) {
			this.builButtons();
		}
		return buttons;
	}

	public void setButtons(List<ToogleButtonData> buttons) {
		this.buttons = buttons;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}



	private String id;
	private int value;
	private String label;
	private String classes;
	private String style;
	
	private List<ToogleButtonData> buttons;
}
