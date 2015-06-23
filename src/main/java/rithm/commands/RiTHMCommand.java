package rithm.commands;

import java.util.HashMap;

import rithm.core.ProgState;

public class RiTHMCommand {
	protected String commandString;
	protected RiTHMParameters rtParams;
	
	public RiTHMCommand(String cosString)
	{
		this.commandString = cosString;
		
	}
	public RiTHMCommand()
	{
		rtParams = null;
	}
	public String getCommandString()
	{
		return commandString;	
	}
	public void setCommandString(String cString)
	{
		this.commandString = cString;
	}
	public RiTHMParameters getRiTHMParameters()
	{
		return rtParams;
	}
	public void setRiTHMParameters(RiTHMParameters rtParams)
	{
		this.rtParams = rtParams;
	}
}
