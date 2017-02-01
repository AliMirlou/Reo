package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerPA;
import nl.cwi.reo.portautomata.PortAutomaton;

public class InterpreterPA extends Interpreter<PortAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Port Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterPA(List<String> dirs, List<String> params) {
		super(new PortAutomaton(), new ListenerPA(), dirs, params);	
	}	
}
