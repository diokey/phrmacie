package pharmacie.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import pharmacie.entities.User;

@ManagedBean
@RequestScoped
public class ValidatorBean {

	public ValidatorBean() {
		// TODO Auto-generated constructor stub
	}
	
	public void validateUsername(FacesContext fc, UIComponent comp, Object ref) throws ValidatorException {
		//validation manifacture date.. 
		if(fc==null || comp == null || ref == null) {
			
			return;
		}
			
		final String username= (String) ref;
		if(username.length()<3) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username Too Short","Username Too Short");
			throw new ValidatorException(message);
		}
		
		if(User.usernameExist(username)) {
			FacesMessage message=new FacesMessage(FacesMessage.SEVERITY_ERROR,"Username Exists","Ce nom Utilisateur est deja utilise");
			throw new ValidatorException(message);
		}
	}

}
