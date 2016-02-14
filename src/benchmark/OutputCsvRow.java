package benchmark;

public class OutputCsvRow {

	
	private String mode;
	private String testMessage;
	
	private Long tokenizerDelay;
	private Long parserDelay;
	private Long outlinerDelay;
	private Long codeCompletionerDelay;
	
	public OutputCsvRow(){
		super();
	}
	
	public OutputCsvRow(String mode, String testMessage, Long tokenizerDelay, Long parserDelay,
			Long outlinerDelay, Long codeCompletionDelay) {
		super();
		this.mode = mode;
		this.testMessage = testMessage;
		this.tokenizerDelay = tokenizerDelay;
		this.parserDelay = parserDelay;
		this.outlinerDelay = outlinerDelay;
		this.codeCompletionerDelay = codeCompletionDelay;
	}


	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTestMessage() {
		return testMessage;
	}

	public void setTestMessage(String testMessage) {
		this.testMessage = testMessage;
	}

	public Long getTokenizerDelay() {
		return tokenizerDelay;
	}
	public void setTokenizerDelay(Long tokenizerDelay) {
		this.tokenizerDelay = tokenizerDelay;
	}
	public Long getParserDelay() {
		return parserDelay;
	}
	public void setParserDelay(Long parserDelay) {
		this.parserDelay = parserDelay;
	}
	public Long getOutlinerDelay() {
		return outlinerDelay;
	}
	public void setOutlinerDelay(Long outlinerDelay) {
		this.outlinerDelay = outlinerDelay;
	}
	public Long getCodeCompletionerDelay() {
		return codeCompletionerDelay;
	}
	public void setCodeCompletionerDelay(Long codeCompletionDelay) {
		this.codeCompletionerDelay = codeCompletionDelay;
	}
	
	
	
	
	
}
