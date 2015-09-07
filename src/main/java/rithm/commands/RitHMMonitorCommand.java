package rithm.commands;

import rithm.core.ProgState;

// TODO: Auto-generated Javadoc
/**
 * The Class RiTHMMonitorCommand.
 */
public class RitHMMonitorCommand extends RitHMCommand{
	
	/** The curr prog state. */
	protected ProgState currProgState;
	
	/**
	 * Instantiates a new ri thm monitor command.
	 *
	 * @param pState the state
	 */
	public RitHMMonitorCommand(ProgState pState)
	{
		super();
		this.currProgState = pState;
	}
	
	/**
	 * Gets the prog state.
	 *
	 * @return the prog state
	 */
	public ProgState getProgState()
	{
		return currProgState;
	}
}
