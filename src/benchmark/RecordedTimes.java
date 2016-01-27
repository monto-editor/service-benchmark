package benchmark;

import java.util.ArrayList;

public class RecordedTimes {
	
	private final long timeSend;
	private final ArrayList<String> prdMessagesOfService;
	private final ArrayList<Long> serviceReceivedPointOfTimes;
	

	public RecordedTimes(long timeSend, ArrayList<String> prdMsgOfService, ArrayList<Long> serviceDelay) {
		super();
		this.timeSend = timeSend;
		this.prdMessagesOfService = prdMsgOfService;
		this.serviceReceivedPointOfTimes = serviceDelay;
	}

	public long getTimeSend() {
		return timeSend;
	}

	public ArrayList<String> getPrdMsgOfService() {
		return prdMessagesOfService;
	}
	
	

	public ArrayList<Long> getServiceReceivedPointOfTimes() {
		return serviceReceivedPointOfTimes;
	}



	
	
	
	
}
