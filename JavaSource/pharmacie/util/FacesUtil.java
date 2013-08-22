package pharmacie.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


public class FacesUtil {

	public FacesUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static HttpSession getSession(boolean b) {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(b);
	}
	
	public static UIViewRoot getViewRoot() {
		return FacesContext.getCurrentInstance().getViewRoot();
	}
	
	public static UIComponent getComponent(String id) {
		return getViewRoot().findComponent(id);
	}
	
	public static boolean memberConnected() {
		if((getSessionAttribute(Constantes.CONNECTED_USER))!=null) {
			return true;
		}
		return false;
	}
	
	public static Object getSessionAttribute(String attrib) {
		HttpSession session=getSession(true);
		
		return session.getAttribute(attrib);		
	}
	public static void setSessionAttribute(String attrib, Object value) {
		HttpSession session=getSession(true);
		session.setAttribute(attrib, value);
	}
	
	public static boolean logOut() {
		if(memberConnected()){
			getSession(false).invalidate();
			return true;
		}
		return false;		
	}
	
	public static void addMessage(String componentId, FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(componentId, message);
	}
	
	public static String getRessourcePath(String resource) {
		
			ServletContext cont=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String rep=cont.getRealPath(resource);
			
		return rep;
	}
	
	public static void main(String args[]) {
		FacesUtil.getRessourcePath("");
	}
}
