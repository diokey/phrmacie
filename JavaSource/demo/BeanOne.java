package demo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="BBeanOne")
@ViewScoped
public class BeanOne {
	
	private  Map<String, Integer> data = new LinkedHashMap<String,Integer>();
    private  ArrayList<String> colors = new ArrayList<String>();
    private  ArrayList<String> names = new ArrayList<String>();
    private Integer dataChn = 51;
    private Integer dataUSA = 36;
    private Integer dataRus = 23;
    
    public BeanOne() {
        super();
        generateData();
    }
    
    private void generateData() {
        data.put("Russia", dataRus);
        data.put("USA", dataUSA);
        data.put("China", dataChn);
        names.add("Gold");
        colors.add("#DAA520");
    }

    public Map<String, Integer> getData() {
        return data;
    }
    
    public ArrayList<String> getNames() {
        return names;
    }
    
    public ArrayList<String> getColors() {
        return colors;
    }

}
