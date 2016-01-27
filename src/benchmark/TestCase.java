package benchmark;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import benchmark.util.Waiter;
import monto.service.message.Languages;
import monto.service.message.LongKey;
import monto.service.message.ParseException;
import monto.service.message.ProductMessage;
import monto.service.message.ProductMessages;
import monto.service.message.Selection;
import monto.service.message.Source;
import monto.service.message.VersionMessage;
import monto.service.message.VersionMessages;

public class TestCase {

	
	private Socket publisherSocket;
	private Socket subscriberSocket;
	private VersionMessage testMessage;
	private int numberOfActiveServices;
	private long waitTime;
	
	public TestCase(Socket publisherSocket, Socket subscriberSocket, VersionMessage testMessage, int numberOfActiveServices, long waitTime){
		
		this.publisherSocket = publisherSocket;
		this.subscriberSocket = subscriberSocket;
		this.testMessage = testMessage;
		this.numberOfActiveServices = numberOfActiveServices;
		this.waitTime = waitTime;
		
	}
	
	public RecordedTimes performTest(String testMsgAsJSON){
		ArrayList<String> prdMessagesOfService = new ArrayList<String>(numberOfActiveServices);
		ArrayList<Long> delaysOfServices = new ArrayList<Long>(numberOfActiveServices);
		
		long timeSend = System.currentTimeMillis();
		publisherSocket.send(testMsgAsJSON);
		
//		Waiter.waitFor(waitTime);
		
		for (int i = 0; i<numberOfActiveServices; i++){
			//first message is the header
			subscriberSocket.recvStr();
//			Waiter.waitFor(waitTime);
			String productMsgAsJson = subscriberSocket.recvStr();
			long now = System.currentTimeMillis();
			long timeWhenReceived = now;// - (2*waitTime);
			prdMessagesOfService.add(productMsgAsJson);
			delaysOfServices.add(timeWhenReceived);
		}
		
		RecordedTimes testRecord = new RecordedTimes(timeSend, prdMessagesOfService, delaysOfServices);
		
		Waiter.waitFor(waitTime);
		
		return testRecord;
	}
	
	public List<RecordedTimes> performTests(int numberOfRepetitions){
		ArrayList<RecordedTimes> testRecords = new ArrayList<RecordedTimes>();
		
		for(int i=0;i<numberOfRepetitions;i++){
			System.out.println("performTests iteration: " + i);
			
			String testMsgAsJSON = VersionMessages.encode(testMessage).toJSONString();
			testRecords.add(performTest(testMsgAsJSON));
		}
		
		return testRecords;
	}
	

	
}
