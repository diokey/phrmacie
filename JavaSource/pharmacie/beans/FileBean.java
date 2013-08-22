package pharmacie.beans;


import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;

import pharmacie.util.FacesUtil;

public class FileBean implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String Name;
	 private long length;
	 private byte[] data;
	 
	 public byte[] getData() {
	       return data;
	 }
	 public void setData(byte[] data) {
	   this.data = data;
	 }
	 public String getName() {
	   return Name;
	 }
	 public void setName(String name) {
	   Name = name;
	   
	    }
	    public long getLength() {
	        return length;
	    }
	    public void setLength(long length) {
	        this.length = length;
	    }
	    
		public void saveFile() {
	    	RandomAccessFile picture=null;
	    	try {
	    		picture=new RandomAccessFile("JavaJSF/temp/images/"+this.Name,"rw");
				picture.write(this.data);
				picture.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
	    public void saveFile(String destDir) {
	    	RandomAccessFile picture=null;
	    	String realPath=FacesUtil.getRessourcePath(destDir);
	    	System.out.println("Real Path: "+realPath);
	    	try {
	    		picture=new RandomAccessFile(realPath+"/"+this.Name,"rw");
	    		System.out.println(this.Name);
				picture.write(this.data);
				picture.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    
	    public FileBean getFile(String fileName) {
	    	//String realName=fileName.substring(fileName.lastIndexOf("/"));
	    	String realPath=FacesUtil.getRessourcePath(fileName);
	    	
	    	File f=new File(realPath);	    	
	    	FileBean result=new FileBean();
	    	result.setName(f.getName());
	    	long length=f.length();
	    	result.setLength(length);
	    	RandomAccessFile picture=null;
	    	int i=0;
	    	try {
				picture=new RandomAccessFile(realPath,"r");
				this.data=new byte[(int) length];
				while(picture.getFilePointer()<length) {
					try{
						this.data[i]=picture.readByte();
						i++;
					}catch(EOFException e) {
						break;
					}
				}
				result.setData(this.data);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	return result;
	    }
	    
	   
	    public static void main(String args[]) {
	    	FileBean b=new FileBean().getFile("./WebContent/uploads/Avatars/big.jpg");
	    	if(b!=null){
	    		
	    		System.out.println(b.getName());
	    		
	    		System.out.println(b.getData());
	    		System.out.println(b.getLength());
	    	}else{
	    		System.out.println("Null");
	    	}
	    }
	
}