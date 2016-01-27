package benchmark.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import benchmark.OutputCsvRow;

public class CsvFileWriter {
	
	private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String EMPTY_FIELD = " ";
    
    
    public static void writeCsvFile(List<OutputCsvRow> outPutRows, String fileName, String fileHeader){
    	FileWriter fileWriter = null;
    	
        try {
            fileWriter = new FileWriter(fileName);
 
            //Write the CSV file header
//            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(fileHeader);
             
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
            //Write a new student object list to the CSV file
            int i = 0;
            
            
            
            for (OutputCsvRow outputRow : outPutRows) {
            	
                fileWriter.append(String.valueOf(i));
                fileWriter.append(COMMA_DELIMITER);
                appendIfNotNull(outputRow.getMode(), fileWriter);
                appendIfNotNull(outputRow.getTestMessage(), fileWriter);
                appendIfNotNull(outputRow.getTokenizerDelay(), fileWriter);
                appendIfNotNull(outputRow.getParserDelay(), fileWriter);
                appendIfNotNull(outputRow.getOutlinerDelay(), fileWriter);
                appendIfNotNull(outputRow.getCodeCompletionDelay(), fileWriter);
                fileWriter.append(NEW_LINE_SEPARATOR);
                i++;
            }
 
            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
             
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
             
        }
    }
    
    private static void appendIfNotNull(Object delay, FileWriter fileWriter){
    	if (delay != null){
        	try {
				fileWriter.append(String.valueOf(delay));
				fileWriter.append(COMMA_DELIMITER);
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else {
        	try {
				fileWriter.append(EMPTY_FIELD);
				fileWriter.append(COMMA_DELIMITER);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }

}
