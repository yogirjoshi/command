package rithm.commands;

import java.util.ArrayList;
import java.util.Properties;

import rithm.core.DataFactory;
import rithm.core.ParserPlugin;
import rithm.core.PredicateEvaluator;
import rithm.core.RitHMMonitor;

// TODO: Auto-generated Javadoc
/**
 * The Class RiTHMParameters.
 */
public class RitHMParameters {
	
	/** The user name. */
	public String userName;
	
	/** The keyword. */
	public String keyword;
	
	/** The spec file. */
	public String specFile;
	
	/** The spec string. */
	public String specString;
	
	/** The specs from file. */
	public boolean specsFromFile;
	

	/** The reset on violation. */
	public boolean resetOnViolation;
	
	/** The data file. */
	public String dataFile;
	
	/** The data format. */
	public String dataFormat;
	
	/** The is processing datafile. */
	public boolean isProcessingDatafile;
	
	/** The trace parser class. */
	public 	String traceParserClass;
	
	/** The spec parser class. */
	public String specParserClass;
	
	/** The monitor class. */
	public String monitorClass;
	
	/** The p evaluator name. */
	public String pEvaluatorName;
	
	/** The p evaluator path. */
	public String pEvaluatorPath;
	
	/** The out file name. */
	public String outFileName;
	
	/** The plot file name. */
	public String plotFileName;
	
	/** The mon event listener name. */
	public String monEventListenerName;
	
	/** The pipe count. */
	public int pipeCount;
	
	/** The specs for pipes. */
	public ArrayList<String> specsForPipes;
	
	/** The parsers for pipes. */
	public ArrayList<String> parsersForPipes;
	
	/** The monitors for pipes. */
	public ArrayList<String> monitorsForPipes;
	
	/** The pred evals for pipes. */
	public ArrayList<String> predEvalsForPipes;
	
	/** The pred eval namesr pipes. */
	public ArrayList<String> predEvalNamesrPipes;
	
	/** The mon event listeners pipes. */
	public ArrayList<String> monEventListenersPipes;
	
	/** The custom arguments properties. */
	public Properties customArgumentsProperties;
	/**
	 * Instantiates a new ri thm parameters.
	 */
	public RitHMParameters()
	{
		specsForPipes=new ArrayList<>();
		monitorsForPipes = new ArrayList<>();
		predEvalsForPipes = new ArrayList<>();
		parsersForPipes = new ArrayList<>();
		predEvalNamesrPipes = new ArrayList<>();
		monEventListenersPipes = new ArrayList<>();
		customArgumentsProperties = new Properties();
	}
}
