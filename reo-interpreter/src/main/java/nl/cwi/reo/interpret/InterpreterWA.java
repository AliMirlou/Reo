package nl.cwi.reo.interpret;

import java.util.List;

import nl.cwi.reo.interpret.listeners.ListenerWA;
import nl.cwi.reo.semantics.SemanticsType;
import nl.cwi.reo.workautomata.WorkAutomaton;

public class InterpreterWA extends Interpreter<WorkAutomaton> {
	
	/**
	 * Constructs a Reo interpreter for Work Automaton semantics.
	 * @param dirs		list of directories of Reo components
	 */
	public InterpreterWA(List<String> dirs) {
		super(SemanticsType.WA, dirs, new ListenerWA());	
	}	
}
