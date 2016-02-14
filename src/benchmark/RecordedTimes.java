package benchmark;

import java.util.ArrayList;
import java.util.List;

public class RecordedTimes {
	
	private final long timeSend;
	private final ArrayList<String> prdMessagesOfService;
	private final ArrayList<Long> serviceReceivedPointOfTimes;
	private final List<String> activeServices;
	

	public RecordedTimes(long timeSend, ArrayList<String> prdMsgOfService, ArrayList<Long> serviceDelay, List<String> activeServices) {
		super();
		this.timeSend = timeSend;
		this.prdMessagesOfService = prdMsgOfService;
		this.serviceReceivedPointOfTimes = serviceDelay;
		this.activeServices = activeServices;
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

	public List<String> getActiveServices() {
		return activeServices;
	}



	
	
	
	
}
