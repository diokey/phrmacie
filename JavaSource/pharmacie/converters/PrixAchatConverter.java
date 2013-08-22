package pharmacie.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import pharmacie.entities.PrixAchat;

public class PrixAchatConverter implements Converter {

	public PrixAchatConverter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		// TODO Auto-generated method stub
		String text[] = arg2.split(":");
		if(text.length<3) {
			System.err.println("Not set");
			return null;
		}
		/*String code = text[0];
		String nomMedicament = text[1];
		String type = text[2];*/
		
		return new PrixAchat();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		// TODO Auto-generated method stub
		return arg2.toString();
	}

}
