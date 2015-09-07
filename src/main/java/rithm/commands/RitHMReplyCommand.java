package rithm.commands;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class RiTHMReplyCommand.
 */
public class RitHMReplyCommand extends RitHMCommand{
	
	/** The response. */
	public HashMap<String, String> response;
	
	/**
	 * Instantiates a new ri thm reply command.
	 *
	 * @param cString the c string
	 */
	public RitHMReplyCommand(String cString)
	{
		super(cString);
		response = new HashMap<>();
	}
}
