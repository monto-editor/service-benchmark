package benchmark;

import java.util.ArrayList;
import java.util.List;


import org.zeromq.ZMQ.Socket;

import benchmark.util.BenchmarkUtils;
import benchmark.util.CsvFileWriter;
import monto.service.message.IAbstractProductMessage;
import monto.service.message.ParseException;
import monto.service.message.ProductMessageWithContents;
import monto.service.message.ProductMessages;
import monto.service.message.VersionMessage;

public class TestEnvironment {
	
	protected List<VersionMessage> testMessages;
	protected List<List<String>> validModi;
	protected int numberOfRepetitions;
	protected long waitTimeInMs;
	protected Services services;
	protected Socket publisherSocket;
	protected Socket subscriberSocket;
	private benchmark.ServiceIdNames serviceNames;
	
	public TestEnvironment(List<VersionMessage> testMessages, List<List<String>> validModi,
			int numberOfRepetitions, long waitTimeInMs, Services services, Socket publisherSocket,
			Socket subscriberSocket, benchmark.ServiceIdNames serviceNames) {
		super();
		this.testMessages = testMessages;
		this.validModi = validModi;
		this.numberOfRepetitions = numberOfRepetitions;
		this.waitTimeInMs = waitTimeInMs;
		this.services = services;
		this.publisherSocket = publisherSocket;
		this.subscriberSocket = subscriberSocket;
		this.serviceNames = serviceNames;
	}

	public void performTests(){
		List<TestMode> modes = new ArrayList<TestMode>();
		
		for (List<String> serviceCombo : validModi){
			TestMode mode = new TestMode(testMessages, serviceCombo, publisherSocket, subscriberSocket, services, numberOfRepetitions, waitTimeInMs, ServiceIdNames.convertServicesToArgument(serviceCombo, serviceNames));
			modes.add(mode);
		}
		
		
//		List<List<RecordedTimes>> testResultsOfMode = 
		List<OutputCsvRow> outputRows = new ArrayList<>();
		for(TestMode testMode : modes){
			System.out.println("Mode: " + testMode.getAcronym());
			List<TestCaseResult> testResultsOfMode = testMode.performTests();
			
			outputRows.addAll(convertRecordingsToOutput(testResultsOfMode, testMode.getAcronym()));
			
			
		}
		String fileHeader = "Id,Mode,TestSource,TokenizerDelay,ParserDelay,OutlinerDelay,CodeCompletionerDelay";
		String fileName = testMessages.get(0).getLanguage().toString();
		CsvFileWriter.writeCsvFile(outputRows, fileName + ".csv", fileHeader);
		
		
	};
	
	private List<OutputCsvRow> convertRecordingsToOutput(List<TestCaseResult> testResults, String mode){
		List<OutputCsvRow> result = new ArrayList<>();
		
		for (TestCaseResult record : testResults){
			
			List<RecordedTimes> recordedTimes = record.getRecords();
			
			for (RecordedTimes recording : recordedTimes){
				
				OutputCsvRow outPutRow = convertRecordedTimes(recording);
				outPutRow.setMode(mode);
				outPutRow.setTestMessage(record.getTestMessage());
				
				result.add(outPutRow);
			}
			
		}
		return result;
	}
	
	private OutputCsvRow convertRecordedTimes(RecordedTimes recording){
		
		OutputCsvRow outPutRow = new OutputCsvRow();
		long timesend = recording.getTimeSend();
		ArrayList<String> prdMessages = recording.getPrdMsgOfService();
		ArrayList<Long> serviceDelays = recording.getServiceReceivedPointOfTimes();
		
		for(int i=0; i<prdMessages.size(); i++){
			String prdMessageAsJSON = prdMessages.get(i);
			long serviceReceivedTime = serviceDelays.get(i);
			
			IAbstractProductMessage prdMsg = null;
			try {
				if(prdMessageAsJSON == null){
					continue;
				}
				prdMsg = ProductMessages.decode(prdMessageAsJSON);
			} catch (ParseException e) {
				continue;
			} 
			
			long delay = serviceReceivedTime - timesend;
			
			String serviceIdName= prdMsg.getServiceID().toString();
			
			
			if(serviceIdName.equals(serviceNames.getTokenizerName())){
				outPutRow.setTokenizerDelay(delay);
			}
			if(serviceIdName.equals(serviceNames.getParserName())){
				outPutRow.setParserDelay(delay);
			}
			if(serviceIdName.equals(serviceNames.getOutlinerName())){
				outPutRow.setOutlinerDelay(delay);
			}
			if(serviceIdName.equals(serviceNames.getCodeCompletionerName())){
				outPutRow.setCodeCompletionerDelay(delay);
			}
		}
		
		//check if all Services, that should have been set, actually have been set
		//if not -> the socket received a timeout
		List<String> enabledServices = recording.getActiveServices();
		for(String service : enabledServices){
			if(service == serviceNames.getTokenizerName()){
				if(outPutRow.getTokenizerDelay() == null){
					outPutRow.setTokenizerDelay(BenchmarkUtils.TIMEDOUT_RECEIVE);
				}
			}
			if(service == serviceNames.getParserName()){
				if(outPutRow.getParserDelay() == null){
					outPutRow.setParserDelay(BenchmarkUtils.TIMEDOUT_RECEIVE);
				}
			}
			if(service == serviceNames.getOutlinerName()){
				if(outPutRow.getOutlinerDelay() == null){
					outPutRow.setOutlinerDelay(BenchmarkUtils.TIMEDOUT_RECEIVE);
				}
			}
			if(service == serviceNames.getCodeCompletionerName()){
				if(outPutRow.getCodeCompletionerDelay() == null){
					outPutRow.setCodeCompletionerDelay(BenchmarkUtils.TIMEDOUT_RECEIVE);
				}
			}
		}
		
		return outPutRow;
		
	}
	
	
	

}
