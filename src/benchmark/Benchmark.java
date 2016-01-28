package benchmark;

import java.util.ArrayList;
import java.util.List;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import benchmark.java.JavaTestContents;
import benchmark.javascript.JavaScriptTestContents;
import benchmark.python.PythonTestContents;
import benchmark.util.Waiter;
import monto.service.message.VersionMessage;

public class Benchmark {
	
	private static Socket publisherSocket;
	private static Socket subscriberSocket;

	public static void main(String[] args) {
		System.out.println("Benchmark execution starts.");
		
		String pubAddress = "tcp://*:5000";
		String subAddress = "tcp://*:5001";
		
		
		
		////////////////
		// Basic Config
		long waitTime = 3000;
		int numberOfRepetitions = 100;
		boolean doesPrint = false;
		////////////////		
		
		
		
		String basePath = System.getProperty("user.dir");
		String pythonPathExtension = "/services/services-python.jar";
		String javaPathExtension = "/services/services-java.jar";
		String javascriptPathExtension = "/services/services-javascript.jar";
		
		
		initializeSockets(pubAddress, subAddress);
		
		/////////////////////////////////		
		
		List<VersionMessage> testMessages = new ArrayList<VersionMessage>();
		testMessages.add(PythonTestContents.TESTMESSAGE_LINES10);
		testMessages.add(PythonTestContents.TESTMESSAGE_LINES100);
		testMessages.add(PythonTestContents.TESTMESSAGE_LINES1000);
		
		Services pythonServices = new Services(basePath + pythonPathExtension, doesPrint);
		
		
		benchmark.ServiceIdNames pythonServiceNames = new benchmark.ServiceIdNames("pythonTokenizer", "pythonParser", "pythonOutliner", "pythonCodeCompletion");
		List<List<String>> validServiceCombinations = pythonServiceNames.getAllValidServiceCombinations();
		
		TestEnvironment pythonBench = new TestEnvironment(testMessages, validServiceCombinations, numberOfRepetitions, waitTime, pythonServices, publisherSocket, subscriberSocket, pythonServiceNames);
		pythonBench.performTests();
		
		/////////////////////////////////
//		
		List<VersionMessage> javaTestMessages = new ArrayList<VersionMessage>();
		javaTestMessages.add(JavaTestContents.TESTMESSAGE_LINES10);
		javaTestMessages.add(JavaTestContents.TESTMESSAGE_LINES100);
		javaTestMessages.add(JavaTestContents.TESTMESSAGE_LINES1000);
		
		ServiceIdNames javaServiceNames = new ServiceIdNames("javaTokenizer", "javaParser", "javaOutliner", "javaCodeCompletioner");
		List<List<String>> validJavaServiceCombinations = javaServiceNames.getAllValidServiceCombinations();
		
		
		Services javaServices = new Services(basePath + javaPathExtension, doesPrint);
		
		TestEnvironment javaBench = new TestEnvironment(javaTestMessages, validJavaServiceCombinations, numberOfRepetitions, waitTime, javaServices, publisherSocket, subscriberSocket, javaServiceNames);
		javaBench.performTests();
		/////////////////////////////////
		
		List<VersionMessage> javaScriptTestMessages = new ArrayList<VersionMessage>();
		javaScriptTestMessages.add(JavaScriptTestContents.TESTMESSAGE_LINES10);
		javaScriptTestMessages.add(JavaScriptTestContents.TESTMESSAGE_LINES100);
		javaScriptTestMessages.add(JavaScriptTestContents.TESTMESSAGE_LINES1000);
		
		ServiceIdNames javascriptServiceNames = new ServiceIdNames("javascriptTokenizer", "javascriptParser", "javascriptOutliner", "javascriptCompletioner");
		List<List<String>> valiJavaScriptServiceCombinations = javascriptServiceNames.getAllValidServiceCombinations();
		
		Services javaScriptServices = new Services(basePath + javascriptPathExtension, doesPrint);
		
		TestEnvironment javascriptBench = new TestEnvironment(javaScriptTestMessages, valiJavaScriptServiceCombinations, numberOfRepetitions, waitTime, javaScriptServices, publisherSocket, subscriberSocket, javascriptServiceNames);
		javascriptBench.performTests();
		
		////////////////////////////////
		
		System.out.println("Benchmark execution finished.");
		closeSockets();
		System.exit(0);
		

	}
	

	private static void initializeSockets(String pubAddress, String subAddress
			){
		@SuppressWarnings("resource")
		ZContext context = new ZContext(1);
		
		publisherSocket = context.createSocket(ZMQ.PUB);
		publisherSocket.connect(pubAddress);
		
		subscriberSocket = context.createSocket(ZMQ.SUB);
		subscriberSocket.connect(subAddress);
		subscriberSocket.subscribe(new byte[] {});
		
		Waiter.wait1Sec();
	}
	
	private static void closeSockets(){
		publisherSocket.close();
		subscriberSocket.close();
	}

}
