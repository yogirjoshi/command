package rithm.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

import com.google.common.*;

import org.apache.log4j.Logger;

import antlr.collections.List;


public class RiTHMParameterValidator {

	RiTHMParameters rithParams;
	final static Logger logger = Logger.getLogger(RiTHMParameterValidator.class);
	public RiTHMParameterValidator()
	{
		rithParams = new RiTHMParameters();
	}
	public RiTHMParameters validate(String propFile)
	{
		rithParams = new RiTHMParameters();
		InputStream is = null;
		try
		{
			is = new FileInputStream(propFile);
			Properties prop = new Properties();
			prop.load(is);
			rithParams.userName = (String)prop.getProperty("userName");
			rithParams.keyword = (String)prop.getProperty("keyword");
			rithParams.specString = (String)prop.getProperty("specifications");
			if(rithParams.specString == null)
			{
				rithParams.specFile = (String)prop.getProperty("sFilename");
				if(rithParams.specFile == null)
				{
					logger.fatal("specifications/sFilename must be provided");
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
			rithParams.dataFile = (String)prop.getProperty("dFilename");
			if(rithParams.dataFile != null)
			{
				rithParams.isProcessingDatafile = true;
				logger.info("Will process trace from " + rithParams.dataFile);
			}
			else
			{
				rithParams.dataFormat = (String)prop.getProperty("dataFormat");
				if(rithParams.dataFormat == null)
				{
					logger.fatal("dataFormat/dFilename must be provided");
					return null;
				}
				else
				{
					rithParams.isProcessingDatafile = false;
					logger.info("Will listen for trace as " + rithParams.dataFormat );
				}
			}

			if(rithParams.isProcessingDatafile){
				rithParams.traceParserClass = (String)prop.getProperty("traceParserClass");
				if(rithParams.traceParserClass == null)
				{
					logger.fatal("traceParserClass must be provided");
					return null;
				}
			}
			else
				logger.info("Data to be received on socket as "+ rithParams.dataFormat);
			rithParams.specParserClass = (String)prop.getProperty("specParserClass");
			if(rithParams.specParserClass == null)
			{
				logger.fatal("specParserClass must be provided");
				return null;
			}
			else
				logger.info("specParserClass is "+ rithParams.specParserClass);
			rithParams.monitorClass = (String)prop.getProperty("monitorClass");
			if(rithParams.monitorClass == null)
			{
				logger.fatal("monitorClass must be provided");
				return null;
			}
			else
				logger.info("monitorClass is "+ rithParams.monitorClass);
			rithParams.pEvaluatorName = (String)prop.getProperty("predicateEvaluatorType");
			rithParams.pEvaluatorPath= (String)prop.getProperty("predicateEvaluatorScriptFile");
//			Files.readAllLines((String)prop.getProperty("predicateEvaluatorScriptFile"));
			rithParams.outFileName = prop.getProperty("monitorOutputLog");
			if(rithParams.outFileName == null)
				rithParams.outFileName = "defaultMonitorOutput.log";
			if(rithParams.pEvaluatorPath != null)
				rithParams.pEvaluatorPath = 
				com.google.common.io.Files.toString(new File((String)prop.getProperty("predicateEvaluatorScriptFile")), Charset.defaultCharset());
		}
		catch(IOException ie)
		{
			logger.fatal("IO error " + ie.getMessage());
			return null;
		}
		return rithParams;
	}

}
