package rithm.commands;

import java.util.ArrayList;

import rithm.core.DataFactory;
import rithm.core.ParserPlugin;
import rithm.core.PredicateEvaluator;
import rithm.core.RiTHMMonitor;

public class RiTHMParameters {
	public String userName;
	public String keyword;
	public String specFile;
	public String specString;
	public boolean specsFromFile;
	public String dataFile;
	public String dataFormat;
	public boolean isProcessingDatafile;
	public 	String traceParserClass;
	public String specParserClass;
	public String monitorClass;
	public String pEvaluatorName;
	public String pEvaluatorPath;
	public String outFileName;
	public String plotFileName;
	public int pipeCount;
	public ArrayList<String> specsForPipes;
	public ArrayList<String> parsersForPipes;
	public ArrayList<String> monitorsForPipes;
	public ArrayList<String> predEvalsForPipes;
	public ArrayList<String> predEvalNamesrPipes;
	public RiTHMParameters()
	{
		specsForPipes=new ArrayList<>();
		monitorsForPipes = new ArrayList<>();
		predEvalsForPipes = new ArrayList<>();
		parsersForPipes = new ArrayList<>();
		predEvalNamesrPipes = new ArrayList<>();
	}
}
