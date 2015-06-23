package rithm.commands;

import java.util.HashMap;

public class RiTHMReplyCommand extends RiTHMCommand{
	public HashMap<String, String> response;
	public RiTHMReplyCommand(String cString)
	{
		super(cString);
		response = new HashMap<>();
	}
}
