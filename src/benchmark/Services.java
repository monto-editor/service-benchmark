package benchmark;

import java.io.IOException;

public class Services {
	
	private Process services = null;
	private final String fixedStartArguments = "-address tcp://* -registration tcp://*:5004 -configuration tcp://*:5007 -resources 8000";;
	private String absolutePath;
	private StreamGobbler errorGobbler;
	private StreamGobbler outputGobbler;
	private boolean doesPrint;
	
	
	
	
	
	public Services(String absolutePath, boolean doesPrint) {
		super();
		this.absolutePath = absolutePath;
		this.doesPrint = doesPrint;
	}

	public void start(String variableArguments){
		Runtime runtime = Runtime.getRuntime();
		
//		String absolutePath = "/home/stefan/Development/Evaluation/global-master/services-java-master/dist/services-java.jar";
		
		String startProgramCommand = "java -jar " + absolutePath + " ";
		
		
		String arguments = variableArguments + fixedStartArguments;
		
		String fullCommand = startProgramCommand + arguments;
		
		try {
			services = runtime.exec(fullCommand);
			errorGobbler = new 
            StreamGobbler(services.getErrorStream(), "ERROR", doesPrint);            
            outputGobbler = new 
            StreamGobbler(services.getInputStream(), "OUTPUT", doesPrint);
	            
            errorGobbler.start();
            outputGobbler.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	
	public boolean stop(){
		boolean success = false;
		
		if (services == null){
			return false;
		} else {
			try {
				errorGobbler.terminate();
				outputGobbler.terminate();
//				errorGobbler.join();
//				outputGobbler.join();
				errorGobbler.join(1000L);
				outputGobbler.join(1000L);
				services.destroy();
				success = true;
			} catch (InterruptedException e) {
				e.printStackTrace();
				success = false;
			}
			return success;
		}
	};

}
