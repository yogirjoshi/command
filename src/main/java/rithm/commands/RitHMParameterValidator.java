package rithm.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;










import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import antlr.collections.List;

// TODO: Auto-generated Javadoc
/**
 * The Class RiTHMParameterValidator.
 */
public class RitHMParameterValidator {

	/** The rith params. */
	protected RitHMParameters rithParams;
	
	/** The propmode. */
	protected final short PROPMODE = 1;
	
	/** The cmdmode. */
	protected final short CMDMODE  = 2;

	/** The curr mode. */
	protected short currMode;
	
	/** The prop. */
	protected Properties prop = null; 
	
	/** The cmd line. */
	protected CommandLine cmdLine;
	
	/** The options. */
	protected Options options;
	
	/** The hlp formatter. */
	protected HelpFormatter hlpFormatter;
	
	/** The Constant logger. */
	final static Logger logger = Logger.getLogger(RitHMParameterValidator.class);
	
	/**
	 * Instantiates a new ri thm parameter validator.
	 */
	public RitHMParameterValidator()
	{
		rithParams = new RitHMParameters();
		currMode = PROPMODE;
	}
	
	/**
	 * Instantiates a new ri thm parameter validator.
	 *
	 * @param mode the mode
	 */
	public RitHMParameterValidator(short mode)
	{
		rithParams = new RitHMParameters();
		this.currMode = mode;
	}
	
	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode(short mode)
	{
		currMode = mode;
	}
	
	/**
	 * Sets the cmd options.
	 *
	 * @param cmdLine the cmd line
	 * @param options the options
	 * @param hlpFormatter the hlp formatter
	 */
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
	protected Properties fetchPropertyFromConfig(String keytoFetch)
	{
		switch (currMode) {
			case PROPMODE:
				Properties perOptionProps = new Properties();
				String allOptions = (String)prop.getProperty(keytoFetch);
				if(allOptions == null)
					return null;
				for(String eachOption: allOptions.split("#"))
				{
					String pair[] = eachOption.split("=");
					if(pair.length != 2){
						logger.fatal("Illegal option " + eachOption);
						return null;
					}
					perOptionProps.put(pair[0], pair[1]);
				}
				return perOptionProps;
			case CMDMODE:
				if(cmdLine.hasOption(keytoFetch))
					return cmdLine.getOptionProperties(keytoFetch);
				else
					return null;
	
			default:
				break;
		}
		throw new IllegalArgumentException("Unknown source to fetch "+ keytoFetch);
	}
	/**
	 * Fetch from config.
	 *
	 * @param keytoFetch the keyto fetch
	 * @return the string
	 */
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
//		cmdLine.get
		throw new IllegalArgumentException("Unknown source to fetch "+ keytoFetch);
	}
	
	/**
	 * Fetch config file.
	 *
	 * @return the string
	 */
	public String fetchConfigFile()
	{
		if(cmdLine.hasOption("configFile"))
			return cmdLine.getOptionValue("configFile");
		return null;	
	}
	
	/**
	 * Fetch bool dual mode.
	 *
	 * @param keyToFetch the key to fetch
	 * @return true, if successful
	 */
	public boolean fetchBoolDualMode(String keyToFetch)
	{
		if(cmdLine.hasOption(keyToFetch))
//			return Boolean.parseBoolean(cmdLine.getOptionValue(keyToFetch));
			return true;
		else
		{
			String propFile = fetchConfigFile();
			if(propFile!=null)
				if(prop.getProperty(keyToFetch) != null)
//					return Boolean.parseBoolean(prop.getProperty(keyToFetch));
					return true;
		}
		return false;	
	}
	
	/**
	 * Prints the help.
	 */
	public final void printHelp()
	{
		Appender appender = Logger.getRootLogger().getAppender("RitHMFile") ;
		if(appender != null){
			if(appender instanceof FileAppender)
			{
				FileAppender fileAp = (FileAppender)appender;
				hlpFormatter.printHelp("RiTHMBrewer","RiTHM Options",options,"Check " + fileAp.getFile() + " for more log messages",true);
				return;
			}
		}
		hlpFormatter.printHelp("RiTHMBrewer","RiTHM Options",options,"Configure log4j properties file for RitHM log messages",true);
	}
	
	/**
	 * Load prop file.
	 *
	 * @param propFile the prop file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException the file not found exception
	 */
	public void loadPropFile(String propFile) throws IOException,FileNotFoundException
	{
		InputStream is = null;
		is = new FileInputStream(propFile);
		prop = new Properties();
		prop.load(is);
		is.close();
	}
	
	/**
	 * Validate pipe params.
	 *
	 * @return true, if successful
	 */
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
	
	/**
	 * Validate.
	 *
	 * @param propFile the prop file
	 * @return the ri thm parameters
	 */
	public RitHMParameters validate(String propFile)
	{
		rithParams = new RitHMParameters();
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
					if(currMode == CMDMODE)
						rithParams.specString.replaceAll("#", "\n");
					rithParams.specsFromFile = false;
					logger.info("Loading specifications from " + rithParams.specString );
				}
			}
			rithParams.resetOnViolation = Boolean.valueOf(fetchFromConfig("resetOnViolation"));
			logger.info("Reset monitor after violation is handled:" + rithParams.resetOnViolation);
			rithParams.plotFileName = fetchFromConfig("plotFileName");
//			if(rithParams.plotFileName == null)
//				rithParams.plotFileName = "TruthValuePlot";
			
			//TODO fix below
			rithParams.outFileName = fetchFromConfig("monitorOutputLog");
			if(rithParams.outFileName == null)
			{
				rithParams.outFileName = fetchFromConfig("outputFile");
//				rithParams.outFileName = (rithParams.outFileName == null)? "defaultMonitorOutput" : rithParams.outFileName;
			}
			
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
					if(fetchBoolDualMode("serverMode")){
						logger.fatal("dataFormat/dataFile must be provided");
						printHelp();
						return null;
					}else{
						rithParams.isProcessingDatafile = true;
						logger.info("Will process trace from " + System.in.toString());
					}
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
				if(fetchPropertyFromConfig("T") != null)
					rithParams.customArgumentsProperties.put("T", fetchPropertyFromConfig("T"));
				rithParams.monEventListenerName = (String)fetchFromConfig("monEventListener");
				if(rithParams.monEventListenerName == null)
					logger.info("DefaultMonitoringEventListener will be used");
				
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
