package benchmark;

import java.util.ArrayList;
import java.util.List;

import org.zeromq.ZMQ.Socket;

import benchmark.util.BenchmarkUtils;
import benchmark.util.Waiter;
import monto.service.message.VersionMessage;

public class TestMode {
	
	private List<VersionMessage> testMessages;
	private List<String> servicesToBeEnabled;
	private Socket publisherSocket;
	private Socket subscriberSocket;
	private Services services;
	private int numberOfRepetitions;
	private long waitTime;
	private String acronym;
	

	
	public TestMode(List<VersionMessage> testMessages, List<String> servicesToBeEnabled,
			Socket publisherSocket, Socket subscriberSocket, Services services, int numberOfRepetitions, long waitTime, String acronym) {
		super();
		this.testMessages = testMessages;
		this.servicesToBeEnabled = servicesToBeEnabled;
		this.publisherSocket = publisherSocket;
		this.subscriberSocket = subscriberSocket;
		this.services = services;
		this.numberOfRepetitions = numberOfRepetitions;
		this.waitTime = waitTime;
		this.acronym =  acronym;//ServiceIdName.convertServicesToArgument(servicesToBeEnabled);
	}
	
	public List<TestCaseResult> performTests(){
		
//		String startArguments = BenchmarkUtil.convertServicesToArgument(servicesToBeEnabled);
		
		setupServices(acronym);
		
		int numberOfActiveServices = servicesToBeEnabled.size();
		
		List<TestCaseResult> result = new ArrayList<>();
		for (VersionMessage testMessage : testMessages){
			TestCase test = new TestCase(publisherSocket, subscriberSocket, testMessage, servicesToBeEnabled, waitTime);
			List<RecordedTimes> recordedTimes = test.performTests(numberOfRepetitions);
			
			TestCaseResult testCaseResult = new TestCaseResult(testMessage.getSource().toString(), recordedTimes);
			
			result.add(testCaseResult); 
//			Printer.printResult(recordedTimes, BenchmarkUtil.generateOutputName(services.getClass().getName(), startArguments, testMessage.getSource().toString(), numberOfRepetitions));
		}
		
		Waiter.waitFor(BenchmarkUtils.WAIT_TIME_BETWEEN_MODES);
		
		shutdownServices();
		
		return result;
	}
	
	
	private void setupServices(String variableArguments){
		services.start(variableArguments);
		Waiter.waitFor(BenchmarkUtils.WAIT_TIME_BETWEEN_MODES);
	}

	private void shutdownServices(){
		services.stop();
		Waiter.waitFor(BenchmarkUtils.WAIT_TIME_BETWEEN_MODES);
	}
	
	public String getAcronym(){
		return acronym;
	}

}
