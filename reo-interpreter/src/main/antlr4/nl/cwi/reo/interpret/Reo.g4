grammar Reo;

import Tokens, PA, WA, CAM, SA;

file      : secn? imps* ID '='? comp EOF;
secn      : 'section' name ';' ;
imps      : 'import' name ';' ;
component : var                                                   # component_variable
          | sign '{' atom ('|' source)? '}'                      # component_atomic
          | sign multiset                                         # component_composite ;
atom      : pa | cam | wa | sa ;
source    : LANG ':' STRING ;
sign      : params? nodes ;
params    : '<' '>' | '<' param (',' param)* '>' ;
param     : var? ':' type | var? sign ;
nodes     : '(' ')' | '(' node (',' node)* ')' ;
node      : var | var? io=(IN | OUT | MIX) type? ;
type      : ID | ID ('*' type) | '(' type ':' type ')'; 
interface : '(' ')' | '(' port (',' port)* ')' ;
port      : prio=(ADD | AMP)? var ;
var       : name indices* ;
name      : (ID '.')* ID ;
indices   : '[' term ']' | '[' term '..' term ']' ;
multiset  : inst                                                  # multiset_constraint
          | strg? '{' multiset* ('|' formula)? '}'                # multiset_setbuilder
          | mset '\' mset                                         # multiset_scintersection
          | mset '/' mset                                         # multiset_scunion 
          | 'for 'ID '=' intr '..' intr multiset                  # multiset_iteration
          | 'if' formula multiset ('else' formula multiset)* 
          ('else' multiset)?                                      # multiset_condition ;
instance  : component list? interface                             # instance_atomic
          | instance strg instance                                # instance_composition
          | instance '*' instance                                 # instance_product
          | instance '+' instance                                 # instance_sum 
          | instance ';' instance                                 # instance_semicolon ;
formula   : BOOL                                                  # formula_boolean      
          | '(' pred ')'                                          # formula_brackets
          | '!' pred                                              # formula_negation
          | var component                                         # formula_componentdefn
          | 'struct' ID '{' param (',' param)* '}'                # formula_structdefn
          | term op=(LEQ | LT | GEQ | GT | EQ | NEQ) term         # formula_binaryrelation
          | pred AND pred                                         # formula_conjunction
          | pred OR pred                                          # formula_disjunction ;
term      : NAT                                                   # term_natural
          | BOOL                                                  # term_boolean
          | STRING                                                # term_string
          | DEC                                                   # term_decimal
          | component                                             # term_componentdefn
          | var                                                   # term_variable
          | list                                                  # term_brackets
          | '(' term ')'                                          # term_brackets
          | <assoc=right> term POW term                           # term_exponent
          | MIN term                                              # term_unarymin
          | term op=(MUL | DIV | MOD) term                        # term_multdivrem
          | term op=(ADD | MIN) term                              # term_addsub
list      : '<' '>' | '<' term (',' term)* '>' ;


