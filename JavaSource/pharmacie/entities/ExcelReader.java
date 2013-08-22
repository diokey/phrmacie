package pharmacie.entities;

import java.io.FileInputStream;
import java.util.Vector;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {

	public ExcelReader() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main( String [] args ) {

    	String fileName="/home/geek/test2.xls";
    	//Read an Excel File and Store in a Vector
    	Vector<Vector<HSSFCell>> dataHolder=readExcelFile(fileName);
    	//Print the data read
    	printCellDataToConsole(dataHolder);
    }
	
	public static Vector<Vector<HSSFCell>> readExcelFile(String fileName)
    {
    	/** --Define a Vector
    	 	--Holds Vectors Of Cells
    	 */
    	Vector<Vector<HSSFCell>> cellVectorHolder = new Vector<Vector<HSSFCell>>();

    	try{
    	/** Creating Input Stream**/
    	//InputStream myInput= ReadExcelFile.class.getResourceAsStream( fileName );
    	FileInputStream myInput = new FileInputStream(fileName);
    	
    	/** Create a POIFSFileSystem object**/
    	POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

    	/** Create a workbook using the File System**/
         HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

         /** Get the first sheet from workbook**/
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

        /** We now need something to iterate through the cells.**/
          Iterator<Row> rowIter = mySheet.rowIterator();

          while(rowIter.hasNext()){
        	  HSSFRow myRow = (HSSFRow) rowIter.next();
        	  Iterator<Cell> cellIter = myRow.cellIterator();
        	  Vector<HSSFCell> cellStoreVector=new Vector<HSSFCell>();
        	  while(cellIter.hasNext()){
        		  HSSFCell myCell = (HSSFCell) cellIter.next();
        		  cellStoreVector.addElement(myCell);
        	  }
        	  cellVectorHolder.addElement(cellStoreVector);
          }
    	}catch (Exception e){e.printStackTrace(); }
    	return cellVectorHolder;
    }

	private static void printCellDataToConsole(Vector<Vector<HSSFCell>> dataHolder) {

		for (int i=0;i<dataHolder.size();i++) {                   
                      Vector<HSSFCell> cellStoreVector=(Vector<HSSFCell>)dataHolder.elementAt(i);
			for (int j=0; j< cellStoreVector.size();j++){
				HSSFCell myCell = (HSSFCell)cellStoreVector.elementAt(j);
				String stringCellValue = myCell.toString();
				System.out.print(stringCellValue+"\t");
			}
			System.out.println();
		}
	}

}
