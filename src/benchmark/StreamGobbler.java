package benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class StreamGobbler extends Thread
{
    private InputStream is;
    private String type;
    private volatile boolean running;
    private volatile boolean doesPrint;
    
    StreamGobbler(InputStream is, String type, boolean doesPrint)
    {
        this.is = is;
        this.type = type;
        this.running = true;
        this.doesPrint = doesPrint;
    }
    
    public void run()
    {
    	while (running){
    		try
    		{
    			InputStreamReader isr = new InputStreamReader(is);
    			BufferedReader br = new BufferedReader(isr);
    			String line = null;
            while ( (line = br.readLine()) != null){
            	if (doesPrint){
            		System.out.println(type + ">" + line);    
            	}
            }
    		} catch (IOException e)
    		{
    			running = false;
    		}
    	}
    }
    
    public void terminate(){
    	running = false;
    }
}