package benchmark.util;

public class Waiter {
	
	public static void wait1Sec(){
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void waitFor(long delayInMs){
		try {
			Thread.sleep(delayInMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

}
