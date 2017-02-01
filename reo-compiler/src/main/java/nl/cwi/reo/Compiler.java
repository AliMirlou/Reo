package nl.cwi.reo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import nl.cwi.reo.interpret.Interpreter;
import nl.cwi.reo.interpret.InterpreterPA;
import nl.cwi.reo.portautomata.PortAutomaton;
import nl.cwi.reo.semantics.api.Connector;

/**
 * A compiler for the coordination language Reo.
 */
public class Compiler {
		
	/**
	 * List of provided Reo source files.
	 */
	@Parameter(description = ".treo files")
	private List<String> files = new ArrayList<String>();
	
	/**
	 * List of parameters for the main component.
	 */
	@Parameter(names = {"-p", "--params"}, variableArity = true, description = "list of parameters to instantiate the main component")
	public List<String> params = new ArrayList<String>();

	/**
	 * List of of directories that contain all necessary Reo components
	 */
    @Parameter(names = {"-cp", "--compath"}, variableArity = true, description = "list of directories that contain all necessary Reo components")
    private List<String> directories = new ArrayList<String>();

	/**
	 * List of of directories that contain all necessary Reo components
	 */
    @Parameter(names = {"-h", "--help"}, description = "shows all available options", help = true)
    private boolean help;

	public static void main(String[] args) {	
		Compiler compiler = new Compiler();
		JCommander jc = new JCommander(compiler, args);
		jc.setProgramName("reoc"); 
		if (compiler.files.size() == 0) {
			jc.usage();
		} else {
	        compiler.run();			
		}
	}
	
    public void run() {
		directories.add(".");
		String comppath = System.getenv("COMPATH");
		if (comppath != null)
			directories.addAll(Arrays.asList(comppath.split(File.pathSeparator)));

		// Interpret the program
		Interpreter<PortAutomaton> interpreter = new InterpreterPA(directories, params);
		Connector<PortAutomaton> connector = interpreter.interpret(files.get(0));
		
		System.out.println(connector.toString());
		
//		ST hello = new ST("Hello, <name>");
//		hello.add("name", "World");
//		System.out.println(hello.render());
//		
//		Integer[] num =
//			    new Integer[] {3,9,20,2,1,4,6,32,5,6,77,888,2,1,6,32,5,6,77,
//			        4,9,20,2,1,4,63,9,20,2,1,4,6,32,5,6,77,6,32,5,6,77,
//			        3,9,20,2,1,4,6,32,5,6,77,888,1,6,32,5};
//		List<Integer> ints = Arrays.asList(num);
//		String t = ST.format(30, "int <%1>[] = { <%2; wrap, anchor, separator=\", \"> };", "a", ints);
//		System.out.println(t);
//		
//		List<String> strings = Arrays.asList("a;", "b;", "c;");
//		ST blocks = new ST("{\n  <item; separator=\"\n\">\n}");
//		blocks.add("item", strings);
//		System.out.println(blocks.render());
		
		
//		if (connector != null) {
//			for (Component<PortAutomaton> X : flatConnector) System.out.println(X);
//			
//			if (!flatConnector.isEmpty()) {
//				PortAutomaton product = connector.get(0).compose(connector.subList(1, connector.size()));
//				PortAutomaton hide = product.restrict(connector.getInterface());
//				
//				System.out.println("Product automaton : \n");
//				System.out.println(hide);
//			}
//		}
//		// Generate the classes.
//		JavaCompiler JC = new JavaCompiler(name, "");
//		JC.compile(program);
	}
}

