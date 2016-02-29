package benchmark;

import java.util.ArrayList;
import java.util.List;

import org.zeromq.ZMQ.Socket;

import benchmark.util.Waiter;
import monto.service.source.SourceMessage;
import monto.service.source.SourceMessages;

public class TestCase {

	
	private Socket publisherSocket;
	private Socket subscriberSocket;
	private SourceMessage testMessage;
	private List<String> activeServices;
	private long waitTime;
	
	public TestCase(Socket publisherSocket, Socket subscriberSocket, SourceMessage testMessage, List<String> activeServices, long waitTime){
		
		this.publisherSocket = publisherSocket;
		this.subscriberSocket = subscriberSocket;
		this.testMessage = testMessage;
		this.activeServices = activeServices;
		this.waitTime = waitTime;
		
	}
	
	public RecordedTimes performTest(String testMsgAsJSON){
		int numberOfActiveServices = activeServices.size();
		ArrayList<String> prdMessagesOfService = new ArrayList<String>(numberOfActiveServices);
		ArrayList<Long> delaysOfServices = new ArrayList<Long>(numberOfActiveServices);
		
		long timeSend = System.currentTimeMillis();
		publisherSocket.send(testMsgAsJSON);
		
		
		for (int i = 0; i<numberOfActiveServices; i++){
			//first message is the header
			subscriberSocket.recvStr();
			String productMsgAsJson = subscriberSocket.recvStr();
			
			long timeWhenReceived = System.currentTimeMillis();
			prdMessagesOfService.add(productMsgAsJson);
			delaysOfServices.add(timeWhenReceived);
		}
		
		RecordedTimes testRecord = new RecordedTimes(timeSend, prdMessagesOfService, delaysOfServices, activeServices);
		
		Waiter.waitFor(waitTime);
		
		return testRecord;
	}
	
	public List<RecordedTimes> performTests(int numberOfRepetitions){
		ArrayList<RecordedTimes> testRecords = new ArrayList<RecordedTimes>();
		
		for(int i=0;i<numberOfRepetitions;i++){
			System.out.println("performTests iteration: " + i);
			
			String testMsgAsJSON = SourceMessages.encode(testMessage).toJSONString();
			testRecords.add(performTest(testMsgAsJSON));
		}
		
		return testRecords;
	}
	

	
}
