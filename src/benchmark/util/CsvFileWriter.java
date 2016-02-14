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
 
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);
            int i = 0;
            
            
            
            for (OutputCsvRow outputRow : outPutRows) {
            	
                fileWriter.append(String.valueOf(i));
                fileWriter.append(COMMA_DELIMITER);
                appendIfNotNull(outputRow.getMode(), fileWriter);
                appendIfNotNull(outputRow.getTestMessage(), fileWriter);
                appendIfNotNull(outputRow.getTokenizerDelay(), fileWriter);
                appendIfNotNull(outputRow.getParserDelay(), fileWriter);
                appendIfNotNull(outputRow.getOutlinerDelay(), fileWriter);
                appendIfNotNull(outputRow.getCodeCompletionerDelay(), fileWriter, true);
                fileWriter.append(NEW_LINE_SEPARATOR);
                i++;
            }
 
            System.out.println("CSV file was created successfully!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter!");
            e.printStackTrace();
        } finally {
             
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter!");
                e.printStackTrace();
            }
             
        }
    }
    
    private static void appendIfNotNull(Object toWrite, FileWriter fileWriter){
    	appendIfNotNull(toWrite, fileWriter, false);
    }
    
    private static void appendIfNotNull(Object toWrite, FileWriter fileWriter, boolean isLastElement){
    	if (toWrite != null){
    		try {
    			Object checkedToWrite = handleTimedOut(toWrite);
    			fileWriter.append(String.valueOf(checkedToWrite));
    			if (!isLastElement)
    				fileWriter.append(COMMA_DELIMITER);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else {
    		try {
    			fileWriter.append(EMPTY_FIELD);
    			if (!isLastElement)
    				fileWriter.append(COMMA_DELIMITER);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    private static Object handleTimedOut(Object toWrite){
    	Object result = toWrite;
    	try {
    		Long value = (Long) toWrite;
    		if (value.equals(BenchmarkUtils.TIMEDOUT_RECEIVE)){
    			result = BenchmarkUtils.TIMEDOUT_VALUE_IN_CSV;
    		}
    	} catch (ClassCastException e){
    		//ignore;
    	}
    	return result;
    }

}
