package benchmark;

import java.util.List;

public class TestCaseResult {
	
	private final String testMessage;
	private final List<RecordedTimes> records;
	
	public TestCaseResult(String testMessage, List<RecordedTimes> records) {
		super();
		this.testMessage = testMessage;
		this.records = records;
	}
	public String getTestMessage() {
		return testMessage;
	}
	public List<RecordedTimes> getRecords() {
		return records;
	}
	
	

}
