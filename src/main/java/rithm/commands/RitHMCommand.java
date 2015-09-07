package rithm.commands;

import java.util.HashMap;

import rithm.core.ProgState;

// TODO: Auto-generated Javadoc
/**
 * The Class RiTHMCommand.
 */
public class RitHMCommand {
	
	/** The command string. */
	protected String commandString;
	
	/** The rt params. */
	protected RitHMParameters rtParams;
	
	/**
	 * Instantiates a new ri thm command.
	 *
	 * @param cosString the cos string
	 */
	public RitHMCommand(String cosString)
	{
		this.commandString = cosString;
		
	}
	
	/**
	 * Instantiates a new ri thm command.
	 */
	public RitHMCommand()
	{
		rtParams = null;
	}
	
	/**
	 * Gets the command string.
	 *
	 * @return the command string
	 */
	public String getCommandString()
	{
		return commandString;	
	}
	
	/**
	 * Sets the command string.
	 *
	 * @param cString the new command string
	 */
	public void setCommandString(String cString)
	{
		this.commandString = cString;
	}
	
	/**
	 * Gets the ri thm parameters.
	 *
	 * @return the ri thm parameters
	 */
	public RitHMParameters getRiTHMParameters()
	{
		return rtParams;
	}
	
	/**
	 * Sets the ri thm parameters.
	 *
	 * @param rtParams the new ri thm parameters
	 */
	public void setRiTHMParameters(RitHMParameters rtParams)
	{
		this.rtParams = rtParams;
	}
}
