package pharmacie.util;

import java.io.Serializable;
import java.util.ResourceBundle;

import pharmacie.lang.Locale;

public class RessourceBundleUtil implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String lang = Locale.getSelectedLanguageBis();
	//private static ResourceBundle uiMessages = ResourceBundle.getBundle( "ui_messages_"+getLang() );
	
	public RessourceBundleUtil() {
		// TODO Auto-generated constructor stub
	}

	public static String getLang() {
		lang = Locale.getSelectedLanguageBis();
		return lang;
	}

	public static void setLang(String lang) {
		RessourceBundleUtil.lang = lang;
	}
	
	public static ResourceBundle getUIMessages() {
		return ResourceBundle.getBundle( "ui_messages_"+getLang() );
	}
	
	
}
