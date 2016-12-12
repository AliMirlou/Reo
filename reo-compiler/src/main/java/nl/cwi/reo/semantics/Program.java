package nl.cwi.reo.semantics;

import java.util.HashSet;
import java.util.Set;

/**
 * A program that consist of a set of component instances {@link nl.cwi.reo.semantics.Atom}.
 * Instances synchronize via put and get operations on shared nodes.
 */
public class Program {
	
	/**
	 * Set of component instances.
	 */
	private Set<Atom> atoms;
	
	/**
	 * Constructs an empty program.
	 */
	public Program() {
		this.atoms = new HashSet<Atom>();
	}
	
	/**
	 * Constructs a single atom program.
	 */
	public Program(Atom atom) {
		this.atoms = new HashSet<Atom>();
		atoms.add(atom);
	}
	
	/**
	 * Composes a set of programs into a single program.
	 * @param progs		set of component instances
	 */
	public Program(Set<Program> progs) {
		this.atoms = new HashSet<Atom>();
		int i = 0;
		for (Program p : progs) {
			p.addSuffixToHidden(i++);
			this.atoms.addAll(p.getInstances());
		}
	}
	
	/**
	 * Gets the component instances.
	 * @return set of component instances.
	 */
	public Set<Atom> getInstances() {
		return this.atoms;
	}
	
	/**
	 * Adds an integer suffix to each internal port of each atom.
	 * @return set of component instances.
	 */
	public void addSuffixToHidden(int suffix) {
		for (Atom a : this.atoms) {
			a.addSuffixToHidden("" + suffix);
		}
	}
	
	/**
	 * Get the string representation of a program.
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		int i = 0;
		for (Atom a : this.atoms)
			str.append("Component " + ++i + ": \n" + a + "\n");
		 
		return str.toString();
		
	}
}
