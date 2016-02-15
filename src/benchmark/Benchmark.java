package benchmark;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.plist.ParseException;
import org.apache.commons.io.IOUtils;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import benchmark.util.Waiter;
import monto.service.types.Language;
import monto.service.types.Languages;
import monto.service.types.LongKey;
import monto.service.types.Selection;
import monto.service.types.Source;
import monto.service.version.VersionMessage;

public class Benchmark {
	
	private static Socket publisherSocket;
	private static Socket subscriberSocket;

	public static void main(String[] args) {
		System.out.println("Benchmark execution starts.");
		
		String pubAddress = "tcp://*:5000";
		String subAddress = "tcp://*:5001";
		

		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder =
		    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
		    .configure(params.xml()
		        .setFileName("config.xml")
		        .setValidating(false));

		try{
			XMLConfiguration config = builder.getConfiguration();
			int timeout = config.getInt("timeout");
			int waitTime = config.getInt("waitTime");
			int repetitions = config.getInt("repetitions");
			boolean doesPrint = config.getBoolean("doesPrint");
			
			initializeSockets(pubAddress, subAddress, timeout);
			
			String serviceName = null;
			int i = 0;
			String currentServicePrefix = "services.service(0)";
			while((serviceName = config.getString(currentServicePrefix + ".name")) != null){
				String servicePath = config.getString(currentServicePrefix + ".path");
				
				List<VersionMessage> testMessages = new ArrayList<VersionMessage>();
				
				String messageName = null;
				int j = 0;
				String currentMessagePrefix = "messages.message(0)";
				String combinedPrefix = currentServicePrefix + "." + currentMessagePrefix;
				while ((messageName = config.getString(combinedPrefix+ ".name")) != null){
					String messagePath = config.getString(combinedPrefix + ".path");
					int selectionOffset = config.getInt(combinedPrefix + ".selection.startOffset");
					int selectionLength = config.getInt(combinedPrefix + ".selection.length");
					Selection selection = new Selection(selectionOffset, selectionLength);
					
					FileInputStream inputStream = new FileInputStream(messagePath);
					try {
					    String content = IOUtils.toString(inputStream);
					    testMessages.add(constructVersionMessage(serviceName, messageName, content, selection));
					} finally {
						inputStream.close();
					}
					
					j++;
					currentMessagePrefix = "messages.message(" + j + ")";
					combinedPrefix = currentServicePrefix + "." + currentMessagePrefix;
				}
				
				Services services = new Services(servicePath, doesPrint);
				ServiceIdNames serviceIdNames = getServiceIdNames(serviceName);
				List<List<String>> validServiceCombinations = serviceIdNames.getAllValidServiceCombinations();
				
				TestEnvironment environment = new TestEnvironment(testMessages, validServiceCombinations, repetitions, waitTime, services, publisherSocket, subscriberSocket, serviceIdNames);
				environment.performTests();
				i++;
				currentServicePrefix = "services.service(" + i + ")";
			}
		} catch(ConfigurationException cex){
			cex.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("Benchmark execution finished.");
		closeSockets();
		System.exit(0);
		

	}
	
	private static VersionMessage constructVersionMessage(String languageName, String messageName, String content, Selection selection) throws ParseException{
		return new VersionMessage(new LongKey(0L), new Source(messageName), getLanguage(languageName), content, new ArrayList<Selection>(Arrays.asList(selection)));
	}
	
	private static Language getLanguage(String languageName) throws ParseException{
		switch(languageName){
		case "python": return Languages.PYTHON;
		case "java": return Languages.JAVA;
		case "javascript": return Languages.JAVASCRIPT;
		default: throw new ParseException("No Language found with name = " + languageName);
		}
	}
	
	private static ServiceIdNames getServiceIdNames(String languageName) throws ParseException{
		switch(languageName){
		case "python": return new benchmark.ServiceIdNames("pythonTokenizer", "pythonParser", "pythonOutliner", "pythonCodeCompletion");
		case "java": return new ServiceIdNames("javaTokenizer", "javaParser", "javaOutliner", "javaCodeCompletioner");
		case "javascript": return new ServiceIdNames("javascriptTokenizer", "javascriptParser", "javascriptOutliner", "javascriptCompletioner");
		default: throw new ParseException("No Language found with name = " + languageName);
		}
	}
	

	private static void initializeSockets(String pubAddress, String subAddress, int timeout
			){
		@SuppressWarnings("resource")
		ZContext context = new ZContext(1);
		
		publisherSocket = context.createSocket(ZMQ.PUB);
		publisherSocket.connect(pubAddress);
		
		subscriberSocket = context.createSocket(ZMQ.SUB);
		subscriberSocket.connect(subAddress);
		subscriberSocket.subscribe(new byte[] {});
		subscriberSocket.setReceiveTimeOut(timeout);
		
		Waiter.wait1Sec();
	}
	
	private static void closeSockets(){
		publisherSocket.close();
		subscriberSocket.close();
	}
	
}
