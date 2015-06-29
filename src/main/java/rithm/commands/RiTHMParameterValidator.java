package rithm.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;









import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import antlr.collections.List;


public class RiTHMParameterValidator {

	protected RiTHMParameters rithParams;
	protected final short PROPMODE = 1;
	protected final short CMDMODE  = 2;

	protected short currMode;
	protected Properties prop = null; 
	protected CommandLine cmdLine;
	protected Options options;
	protected HelpFormatter hlpFormatter;
	final static Logger logger = Logger.getLogger(RiTHMParameterValidator.class);
	public RiTHMParameterValidator()
	{
		rithParams = new RiTHMParameters();
		currMode = PROPMODE;
	}
	public RiTHMParameterValidator(short mode)
	{
		rithParams = new RiTHMParameters();
		this.currMode = mode;
	}
	public void setMode(short mode)
	{
		currMode = mode;
	}
	public void setCmdOptions(CommandLine cmdLine, Options options,HelpFormatter hlpFormatter)
	{
		if(currMode == CMDMODE){
			this.cmdLine = cmdLine;
			this.options = options;
			this.hlpFormatter = hlpFormatter;
			return;
		}
		throw new IllegalArgumentException("mode must be " + CMDMODE);
	}
	protected String fetchFromConfig(String keytoFetch)
	{
		switch (currMode) {
		case PROPMODE:
			return (String)prop.getProperty(keytoFetch);
		case CMDMODE:
			if(cmdLine.hasOption(keytoFetch))
				return cmdLine.getOptionValue(keytoFetch);
			else
				return null;

		default:
			break;
		}
		throw new IllegalArgumentException("Unknown source to fetch "+ keytoFetch);
	}
	public String fetchConfigFile()
	{
		if(cmdLine.hasOption("configFile"))
			return cmdLine.getOptionValue("configFile");
		return null;	
	}
	public boolean fetchBoolDualMode(String keyToFetch)
	{
		if(cmdLine.hasOption(keyToFetch))
			return Boolean.parseBoolean(cmdLine.getOptionValue(keyToFetch));
		else
		{
			String propFile = fetchConfigFile();
			if(propFile!=null)
				if(prop.getProperty(keyToFetch) != null)
					return Boolean.parseBoolean(prop.getProperty(keyToFetch));
		}
		return false;	
	}
	protected final void printHelp()
	{
		if(currMode == CMDMODE)
			hlpFormatter.printHelp("RiTHMBrewer","RiTHM Options",options,"Check RiTHMLog for more log message",true);
	}
	public void loadPropFile(String propFile) throws IOException,FileNotFoundException
	{
		InputStream is = null;
		is = new FileInputStream(propFile);
		prop = new Properties();
		prop.load(is);
		is.close();
	}
	protected boolean validatePipeParams()
	{
		for(int i = 1; i <= rithParams.pipeCount;i++)
		{
			String key = Integer.toString(i); 
			String val = (String)fetchFromConfig("specsPipe" + key);
			if(val == null)
			{
				logger.fatal("specsPipe" + key +" not found in pipeMode");
				return false;
			}
			int j = i-1;
			rithParams.specsForPipes.add(j,val);
			logger.debug("Added to specifications for pipe" + j + "=" + val);
			val = (String)fetchFromConfig("specParsersPipe" + key);
			if(val == null)
			{
				logger.fatal("specParsersPipe" + key +" not found in pipeMode");
				return false;
			}
			rithParams.parsersForPipes.add(j,val);
			logger.debug("Added to specParsers for pipe" + j + "=" + val);
			val = (String)fetchFromConfig("monitorsPipe" + key);
			if(val == null)
			{
				logger.fatal("monitorsPipe" + key +" not found in pipeMode");
				return false;
			}
			rithParams.monitorsForPipes.add(j,val);
			logger.debug("Added to monitors for pipe" + j + "=" + val);
			
			//TODO: Fix below to read file as string
			val = (String)fetchFromConfig("predEvaluatorNamePipe" + key);
			if(val == null){
				logger.debug("Using DefaultPredicateEvaluator for pipe" + j);
			}
			rithParams.predEvalNamesrPipes.add(j,val);
			val = (String)fetchFromConfig("predEvaluatorScriptPipe" + key);
			rithParams.predEvalsForPipes.add(j,val);
		}
		return true;
	}
	public RiTHMParameters validate(String propFile)
	{
		rithParams = new RiTHMParameters();
		InputStream is = null;
		try
		{
			if(currMode == PROPMODE){
				is = new FileInputStream(propFile);
				prop = new Properties();
				prop.load(is);
			}
			rithParams.userName = (String)fetchFromConfig("userName");
			rithParams.keyword = (String)fetchFromConfig("password");
			
			String pipeCountStr = (String)fetchFromConfig("pipeCount");
			boolean pipeMode = false;
			if(pipeCountStr != null)
			{
				rithParams.pipeCount = Integer.parseInt(pipeCountStr);
				logger.debug("No of pipes:" + pipeCountStr);
				pipeMode = true;
			}
			else
			{
				rithParams.pipeCount = 0;
			}
			if(!pipeMode){
				rithParams.specString = (String)fetchFromConfig("specifications");
				if(rithParams.specString == null)
				{
					rithParams.specFile = (String)fetchFromConfig("specFile");
					if(rithParams.specFile == null)
					{
						logger.fatal("specifications/specFile must be provided");
						printHelp();
						return null;
					}
					else
					{
						logger.info("Loading specifications from " + rithParams.specFile );
						rithParams.specsFromFile = true;
					}
				}else
				{
					rithParams.specsFromFile = false;
					logger.info("Loading specifications from " + rithParams.specString );
				}
			}
			rithParams.plotFileName = fetchFromConfig("plotFileName");
			if(rithParams.plotFileName == null)
				rithParams.plotFileName = "TruthValuePlot";
			rithParams.outFileName = fetchFromConfig("monitorOutputLog");
			if(rithParams.outFileName == null)
				rithParams.outFileName = "defaultMonitorOutput";
			rithParams.dataFile = (String)fetchFromConfig("dataFile");
			if(rithParams.dataFile != null)
			{
				rithParams.isProcessingDatafile = true;
				logger.info("Will process trace from " + rithParams.dataFile);
			}
			else
			{
				rithParams.dataFormat = (String)fetchFromConfig("dataFormat");
				if(rithParams.dataFormat == null)
				{
					logger.fatal("dataFormat/dataFile must be provided");
					printHelp();
					return null;
				}
				else
				{
					rithParams.isProcessingDatafile = false;
					logger.info("Will listen for trace as " + rithParams.dataFormat );
				}
			}

			if(rithParams.isProcessingDatafile){
				rithParams.traceParserClass = (String)fetchFromConfig("traceParserClass");
				if(rithParams.traceParserClass == null)
				{
					logger.fatal("traceParserClass must be provided");
					printHelp();
					return null;
				}
			}
			else
				logger.info("Data to be received on socket as "+ rithParams.dataFormat);
			
			if(!pipeMode){
					
				rithParams.specParserClass = (String)fetchFromConfig("specParserClass");
				if(rithParams.specParserClass == null)
				{
					logger.fatal("specParserClass must be provided");
					printHelp();
					return null;
				}
				else
					logger.info("specParserClass is "+ rithParams.specParserClass);

				rithParams.monitorClass = (String)fetchFromConfig("monitorClass");
				if(rithParams.monitorClass == null)
				{
					logger.fatal("monitorClass must be provided");
					printHelp();
					return null;
				}
				else
					logger.info("monitorClass is "+ rithParams.monitorClass);
				rithParams.pEvaluatorName = (String)fetchFromConfig("predicateEvaluatorType");
				rithParams.pEvaluatorPath= (String)fetchFromConfig("predicateEvaluatorScriptFile");

				if(rithParams.pEvaluatorPath != null)
					rithParams.pEvaluatorPath = 
					com.google.common.io.Files.toString(new File((String)fetchFromConfig("predicateEvaluatorScriptFile")), Charset.defaultCharset());
			}
			if(pipeMode)
				if(!validatePipeParams())
					return null;
			if(currMode == CMDMODE)
				if(cmdLine.hasOption("help"))
					printHelp();
		}
		catch(IOException ie)
		{
			logger.fatal("IO error " + ie.getMessage());
			return null;
		}
		finally{
			try {
				if(currMode == PROPMODE)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.fatal("IO error " + e.getMessage());
			}
		}
		return rithParams;
	}

}
