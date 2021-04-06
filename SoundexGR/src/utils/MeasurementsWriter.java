/**
 * 
 */
package utils;

import java.io.FileWriter;

/**
 * @author Yannis Tzitzikas (yannistzitzik@gmail.com)
 * Writes measurements in a file for subsequent analysis
 */

public class MeasurementsWriter {
  private FileWriter fr; 	
  
  public void write(String str) {
	  try {
		  fr.write(str);
		  //fr.write("\n");
		  //fr.write("1st column, second column" + "\n");
		  //fr.write("2nd row" + str+ "\n");
	  } catch (Exception e) {
		  System.out.println(e);
	  }
  }
  
  public void close() {
	  try {
		  fr.close();
	  } catch (Exception e) {
		  System.out.println(e);
	  }
  }
  public MeasurementsWriter(String fileName) {
	  try {
	  fr = new FileWriter(fileName,false); // overwrite file if it exists

	  } catch (Exception e) {
		  System.out.println(e);
	  }
  }
  
}


class MeasurementsWriterClient {
	public static void main(String[] lala) {
		MeasurementsWriter mw = new MeasurementsWriter("Resources/measurements/measurementsTest.csv");
		mw.write("row1cell1, row1cell2");
		mw.write("row2cell1, row2cell2");
		mw.close();
	}
}