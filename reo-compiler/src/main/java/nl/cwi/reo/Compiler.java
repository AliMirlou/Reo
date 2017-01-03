package nl.cwi.reo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import nl.cwi.reo.automata.Automaton;
import nl.cwi.reo.automata.Void;
import nl.cwi.reo.automata.State;
import nl.cwi.reo.automata.Transition;
import nl.cwi.reo.graphgames.GameGraph;
import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.workautomata.WorkAutomaton;

/**
 * A compiler for the coordination language Reo.
 */
public class Compiler {

	/**
	 * Entry for the Reo compiler.
	 * @param args		command line parameters
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			
			//testAutomata();
			
			//testWorkAutomata();
			
			//testGameGraph();

			// Print a standard message if no arguments are given
			System.out.println("Usage: java -jar reoc.jar <options> <reo source files>");
			System.out.println("where possible options include:");
			System.out.println("  -cp <dir>(;<dir>)*   colon separated list of directories");
			System.out.println("  -q                   quiet");
			System.out.println("  -v                   version information");

		} else {

			Interpreter<WorkAutomaton> interpreter = new Interpreter<WorkAutomaton>(new WorkAutomaton());
			
			List<WorkAutomaton> program = interpreter.getProgram(args[0]);
			
			System.out.println(program);

//			// Generate the classes.
//			JavaCompiler JC = new JavaCompiler(name, "");
//			JC.compile(program);
		}
	}
	
//	/**
//	 * Just a function to test my WorkAutomata implementation
//	 */
//	public static void testWorkAutomata() {
//		
//		WorkAutomaton A = new WorkAutomaton();
//		A.addTransition("0; 1; ; x==2");
//		A.addTransition("1; 2; ; x==2");
//		A.addTransition("1; 0; a; x<=2");
//		A.addTransition("2; 1; a; x<=2");
//		WorkAutomaton B = new WorkAutomaton();
//		B.addTransition("0; 1; a; y==1 & z==0");
//		B.addTransition("1; 0; ; y==0 & z==3");
//
//		//WorkAutomaton P = WorkAutomaton.product(A, B);
//		//A.outputDOT("A");
//		//B.outputDOT("B");
//		//P.outputDOT("P");
//
//		WorkAutomaton C = new WorkAutomaton();
//		C.addTransition("0; 0; ; x==1&y<=1");
//		C.addTransition("0; 0; ; x==5&y==0");
//		C.addTransition("0; 0; a; x==0&y==1");
//		WorkAutomaton.outputDOT(C, "C");
//
//		GameGraph G = new GameGraph(C, "a");
//		G.synthesize();
//
//		GameGraph.outputDOT(G, "schedulinggame");
//
//		// presentation
//		WorkAutomaton X = new WorkAutomaton();
//		X.addTransition("0; 0; a; x==5");
//		X.addTransition("0; 0; ; x==1");
//		WorkAutomaton Y = new WorkAutomaton();
//		Y.addTransition("0; 1; a; true");
//		Y.addTransition("1; 0; b; true");
//		WorkAutomaton Z = new WorkAutomaton();
//		Z.addTransition("0; 1; b; y==2");
//		Z.addTransition("1; 0; ; y==7");
//
//		WorkAutomaton product = WorkAutomaton.product(X, Y, Z);
//		WorkAutomaton.outputDOT(product, "product");
//	}
	
	/**
	 * Just a function to test my GameGraph implementation
	 */
	public static void testGameGraph() {
		
		GameGraph G = new GameGraph("x", 1);
		G.addEdge("x", 1, "z", 0, 3, 1);
		G.addEdge("z", 0, "w", 0, 1, 1);
		G.addEdge("w", 0, "v", 1, -4, 1);
		G.addEdge("v", 1, "w", 0, 0, 1);
		G.addEdge("v", 1, "z", 0, 1, 1);
		G.addEdge("z", 0, "y", 0, -3, 1);
		G.addEdge("y", 0, "z", 0, 1, 1);
		G.addEdge("y", 0, "x", 1, 2, 1);

		//HashMap<Vertex, Double> f = G.minimalCredit(0, 1);
		G.synthesize();

		GameGraph.outputDOT(G, "graph");
		// to render graph.dot, run:
		// dot -Tps graph.dot -o graph.ps
	}
	
	/**
	 * Just a function to test my generic automata implementation
	 */
	public static void testAutomata() {
		
		Set<State> Q = new HashSet<State>();
		Set<String> P = new HashSet<String>();
		P.add("a");
		P.add("b");
		Map<State, List<Transition<Void>>> T = new HashMap<State, List<Transition<Void>>>();
		State q0 = new State("q0");
		State q1 = new State("q1");
		Q.add(q0);
		Q.add(q1);
		List<Transition<Void>> out0 = new ArrayList<Transition<Void>>();
		List<Transition<Void>> out1 = new ArrayList<Transition<Void>>();
		TreeSet<String> N0 = new TreeSet<String>();
		TreeSet<String> N1 = new TreeSet<String>();
		N0.add("a");
		N1.add("b");
		Transition<Void> t0 = new Transition<Void>(q0, q1, N0, null);
		Transition<Void> t1 = new Transition<Void>(q1, q0, N1, null);
		out0.add(t0);
		out1.add(t1);
		T.put(q0, out0);
		T.put(q1, out1);
		
		Automaton<Void> A = new Automaton<Void>(Q, P, T, q0);

		System.out.println(A);
		
		Map<String, String> links = new HashMap<String, String>();
		links.put("a", "c");
		links.put("b", "d");
		Automaton<Void> B = A.rename(links);
		
		System.out.println(B);
		
		System.out.println(A.compose(B));
		
		//System.out.println(A.compose(A));
		
		// Automaton.outputDOT(A, "A") 
		// to render the file, run:
		// dot -Tps A.dot -o A.ps
	}
}

