package benchmark;

import java.util.ArrayList;
import java.util.List;

import benchmark.util.StartArguments;

public class ServiceIdNames {
	
	private final String tokenizerName;
	private final String parserName;
	private final String outlinerName;
	private final String codeCompletionerName;
	
	public ServiceIdNames(String tokenizerName, String parserName, String outlinerName, String codeCompletionerName) {
		super();
		this.tokenizerName = tokenizerName;
		this.parserName = parserName;
		this.outlinerName = outlinerName;
		this.codeCompletionerName = codeCompletionerName;
	}

	public String getTokenizerName() {
		return tokenizerName;
	}

	public String getParserName() {
		return parserName;
	}

	public String getOutlinerName() {
		return outlinerName;
	}

	public String getCodeCompletionerName() {
		return codeCompletionerName;
	}
	
	public static String convertServicesToArgument(List<String> services, ServiceIdNames serviceIdNames){
		StringBuilder sb = new StringBuilder();
		
		for (String serviceName : services){
			
			if(serviceName.equals(serviceIdNames.getTokenizerName())){
				sb.append(StartArguments.TOKENIZER);
				sb.append(StartArguments.ARGUMENTSEPERATION);
			}
			if(serviceName.equals(serviceIdNames.getParserName())){
				sb.append(StartArguments.PARSER);
				sb.append(StartArguments.ARGUMENTSEPERATION);
			}
			if(serviceName.equals(serviceIdNames.getOutlinerName())){
				sb.append(StartArguments.OUTLINER);
				sb.append(StartArguments.ARGUMENTSEPERATION);
			}
			if(serviceName.equals(serviceIdNames.getCodeCompletionerName())){
				sb.append(StartArguments.CODECOMPLETION);
				sb.append(StartArguments.ARGUMENTSEPERATION);
			}
		}
		
		return sb.toString();
	}
	
	public List<List<String>> getAllValidServiceCombinations(){
		List<List<String>> validCombinations = new ArrayList<List<String>>();
		
		validCombinations.add(getCombination(tokenizerName, parserName, 
				outlinerName, codeCompletionerName));
		
		validCombinations.add(getCombination(tokenizerName, parserName, 
				codeCompletionerName));
		
		validCombinations.add(getCombination(tokenizerName, parserName, 
				outlinerName));
		
		validCombinations.add(getCombination(parserName, 
				outlinerName, codeCompletionerName));
		
		validCombinations.add(getCombination(parserName, 
				 codeCompletionerName));
		
		validCombinations.add(getCombination(parserName, 
				outlinerName));
		
		validCombinations.add(getCombination(tokenizerName, parserName));
		
		validCombinations.add(getCombination(parserName));
		
		validCombinations.add(getCombination(tokenizerName));
		
		return validCombinations;
	}
	
	private static List<String> getCombination(String... serviceIds){
		List<String> combination = new ArrayList<String>();
		for (String serviceId : serviceIds){
			combination.add(serviceId);
		}
		return combination;
	}
	
	
	
}
