package nl.cwi.reo.interpret.signatures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Token;
import org.junit.Test;

import nl.cwi.reo.errors.CompilationException;
import nl.cwi.reo.interpret.expressions.ValueList;
import nl.cwi.reo.interpret.integers.IntegerVariable;
import nl.cwi.reo.interpret.variables.VariableExpression;
import nl.cwi.reo.interpret.variables.Variable;
import nl.cwi.reo.interpret.variables.VariableList;
import nl.cwi.reo.interpret.variables.VariableRange;
import nl.cwi.reo.semantics.Port;
import nl.cwi.reo.semantics.PortType;
import nl.cwi.reo.semantics.PrioType;
import nl.cwi.reo.semantics.expressions.IntegerExpression;
import nl.cwi.reo.semantics.expressions.IntegerValue;

public class SignatureExpressionTest {

   @Test
   public void evaluate_NoParametersAndExplicitNodes() {
	   ParameterList params = new ParameterList();
	   NodeList nodes = new NodeList();
	   Token token = new CommonToken(0);
	   nodes.add(new Node(new Variable("a", token), NodeType.SOURCE, new TypeTag("int")));
	   nodes.add(new Node(new Variable("b", token), NodeType.SINK, new TypeTag("bool")));
	   nodes.add(new Node(new Variable("c", token), NodeType.MIXED, new TypeTag("string")));
	   SignatureExpression e = new SignatureExpression(params, nodes, token);
	   
	   ValueList values = new ValueList();
	   List<Variable> newnodes = new ArrayList<Variable>();
	   newnodes.add(new Variable("x", token));
	   newnodes.add(new Variable("y", token));
	   newnodes.add(new Variable("z", token));
	   VariableList iface = new VariableList(newnodes, token);
	   SignatureConcrete s = null;
	   boolean hasException = false;
	   
	   try {
		   s = e.evaluate(values, iface);
		   
		   assertTrue(s.getDefinitions().isEmpty());
		   assertEquals(s.get(new Port("a")), new Port("x", PortType.IN, PrioType.NONE, "int", false));
		   assertEquals(s.get(new Port("b")), new Port("y", PortType.OUT, PrioType.NONE, "bool", false));
		   assertEquals(s.get(new Port("c")), new Port("z", PortType.NONE, PrioType.NONE, "string", false));
	   } catch (CompilationException ce) {
		   hasException = true;
	   } finally {
		   assertFalse(hasException);		   
	   }
   }

   @Test
   public void evaluate_NoParametersAndNodeRanges() {
	   ParameterList params = new ParameterList();
	   NodeList nodes = new NodeList();
	   Token token = new CommonToken(0);
	   List<List<IntegerExpression>> indices = new ArrayList<List<IntegerExpression>>();
	   List<IntegerExpression> rng = new ArrayList<IntegerExpression>();
	   rng.add(new IntegerValue(1));
	   rng.add(new IntegerVariable(new Variable("k", token)));
	   indices.add(rng);
	   VariableExpression var = new VariableRange("a", indices, token);
	   nodes.add(new Node(var, NodeType.SOURCE, new TypeTag("int")));
	   SignatureExpression e = new SignatureExpression(params, nodes, token);

	   ValueList values = new ValueList();
	   List<Variable> newnodes = new ArrayList<Variable>();
	   newnodes.add(new Variable("x", token));
	   newnodes.add(new Variable("y", token));
	   newnodes.add(new Variable("z", token));
	   VariableList iface = new VariableList(newnodes, token);
	   SignatureConcrete s = null;
	   boolean hasException = false;
	   
	   try {
		   s = e.evaluate(values, iface);

		   assertEquals(s.getDefinitions().get("k"), new IntegerValue(3));
		   assertNull(s.get(new Port("a[0]")));
		   assertEquals(s.get(new Port("a[1]")), new Port("x", PortType.IN, PrioType.NONE, "int", false));
		   assertEquals(s.get(new Port("a[2]")), new Port("y", PortType.IN, PrioType.NONE, "int", false));
		   assertEquals(s.get(new Port("a[3]")), new Port("z", PortType.IN, PrioType.NONE, "int", false));
		   assertNull(s.get(new Port("a[4]")));
	   } catch (CompilationException ce) {
		   ce.printStackTrace();
		   hasException = true;
	   } finally {
		   assertFalse(hasException);
	   }	  
   }   
}