/*
 * Leanna Pangan
 * ID: 913074778    
 * Email: lspangan@mail.sfsu.edu
 */
package evaluator;


import java.util.*; 

    
abstract class Operator {
    // creates a hashmap to store operators and operands  
    final static HashMap<String,Operator> operators = new HashMap<>();
    
    // inserts operators and instances of the operator subclasses
    static {
        operators.put("+",new AdditionOperator());
        operators.put("-",new SubtractionOperator());
        operators.put("*",new MultiplicationOperator());
        operators.put("/",new DivisionOperator());
        operators.put("^",new PowerOperator());
        operators.put("#",new SharpOperator());
        operators.put("!",new ExclamationOperator());
        operators.put("(",new OpenParenOperator());
        operators.put(")",new ClosedParenOperator());  
    }
    
    // abstract methods for operator subclasses
    // establishes operator precedence when evaluating expression
    public abstract int in_priority(); 
    
    // checks if the token is an operator
    public static boolean check(String tok) {
        if (operators.containsKey(tok)) {
            return true;
        }
        return false;
    }
    
    // abstract method for operator subclasses
    // executes each operation
    public abstract Operand execute(Operand opd1, Operand op2);
    
}

// operator subclasses extend Operator class

class AdditionOperator extends Operator {
    @Override
    public int in_priority() {
        return 4;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        Operand sum = new Operand(op1.getValue() + op2.getValue());
        return sum;
    }
} 

class SubtractionOperator extends Operator {

    @Override
    public int in_priority() {
        return 4;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        return new Operand(op1.getValue() - op2.getValue());
    }
}

class MultiplicationOperator extends Operator {

    @Override
    public int in_priority() {
        return 5;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        return new Operand(op1.getValue() * op2.getValue());
    }
}

class DivisionOperator extends Operator {

    @Override
    public int in_priority() {
        return 5;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        return new Operand(op1.getValue() / op2.getValue());
    }
}

class PowerOperator extends Operator {

    @Override
    public int in_priority() {
        return 6;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        int result = (int)Math.pow(op1.getValue(), op2.getValue());
        return new Operand(result);
    }
}
 
class SharpOperator extends Operator {

    @Override
    public int in_priority() {
        return 0;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        return op1;
    }
}

class ExclamationOperator extends Operator {

    @Override
    public int in_priority() {
        return 1;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        return op1;
    }
}

class OpenParenOperator extends Operator {

    @Override
    public int in_priority() {
        return 3;
    }
    
    @Override
    public Operand execute(Operand op1, Operand op2) {
        return op1;
    }
}

class ClosedParenOperator extends Operator {

    @Override
    public int in_priority() {
        return 2;
    }

    @Override
    public Operand execute(Operand op1, Operand op2) {
        return op1;
    }
}
    
class Operand {
    // variable for Operand value
    int num;
    
    // constructor takes in a string and converts the string to an int
    Operand(String tok) {
        num = Integer.parseInt(tok);
    }
    
    // constructor takes in an int
    Operand(int value) {
        this.num = value;
    }
    
    // checks if token is an Operand
    public static boolean check(String tok) {
        if (!Operator.check(tok)) {
            return true;
        }
        return false;
    }
    
    // returns the value of the Operand
    int getValue() {
        return num;
    }

}

public class Evaluator {
    
    final private Stack<Operand> opdStack; 
    final private Stack<Operator> oprStack;
    
    public Evaluator() {
        opdStack = new Stack<>(); 
        oprStack = new Stack<>();
    }
    
    // keep evaluating the stack
    // repeat while loop
    public void execute(Stack<Operand> opd, Stack<Operator> opr) {
        while (!opr.peek().equals(Operator.operators.get("#"))) {
            Operator oldOpr = ((Operator)opr.pop());
            Operand op2 = (Operand)opd.pop();
            Operand op1 = (Operand)opd.pop();
            opd.push(oldOpr.execute(op1,op2));
        }
    }
    
    public int eval(String expr) {

        String tok;
        // init stack - necessary with operator priority schema;
        // the priority of any operator in the operator stack other then 
        // the usual operators - "+-*/" - should be less than the priority of the usual operators
        oprStack.push(Operator.operators.get("#"));
        
        // insert ! at end of expr
        expr = expr.concat("!");

        // so the stack Stack will have +/! and ! priority is < /
        String delimiters = "+-*/^#!() ";
        
        // the 3rd arg is true to indicate to use the delimiters as tokens, too but we'll filter out spaces
        StringTokenizer st = new StringTokenizer(expr, delimiters, true);

        while (st.hasMoreTokens()) {
            if (!(tok = st.nextToken()).equals(" ")) {
                // filter out spaces
                // check if tok is an operand
                if (Operand.check(tok)) {
                    opdStack.push(new Operand(tok));
                } else {
                    if (!Operator.check(tok)) {
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }
                    
                    // creates a pointer to the next operator
                    Operator newOpr = Operator.operators.get(tok); // POINT 1
                    
                    // compares operator precedence
                    // oprStack and newOpr cannot both be "^" in order to fulfill right associativity
                    // of power operator
                    // newOpr cannot be "(" because "(" is just pushed to the stack
                    while (((Operator) oprStack.peek()).in_priority() >= newOpr.in_priority()
                            && !newOpr.equals(Operator.operators.get("^")) 
                            && !newOpr.equals(Operator.operators.get("("))) {
                        // handles the case where newOpr is ")"
                        // evaluates whats inside parentheses
                        // pops the operator from oprStack and the two operands from opdStack
                        // executes operation using the operator subclasses
                        if (newOpr.in_priority() == 2){ // ")" priority = 2
                            while (((Operator) oprStack.peek()).in_priority() != 0 // "#" priority = 0
                                    && ((Operator) oprStack.peek()).in_priority() != 3) { // "(" priority = 3
                                Operator oldOpr = ((Operator) oprStack.pop());
                                Operand op2 = (Operand) opdStack.pop();
                                Operand op1 = (Operand) opdStack.pop();
                                opdStack.push(oldOpr.execute(op1, op2));
                            }
                            if (((Operator) oprStack.peek()).in_priority() != 0){
                                oprStack.pop(); // pops "("
                            }
                        } else {
                            // Control gets here when we've picked up all of the tokens; you must add
                            // code to complete the evaluation - consider how the code given here
                            // will evaluate the expression 1+2*3
                            // When we have no more tokens to scan, the operand stack will contain 1 2 // and the operator stack will have + * with 2 and * on the top;
                            // In order to complete the evaluation we must empty the stacks (except
                            // the init operator on the operator stack); that is, we should keep
                            // evaluating the operator stack until it only contains the init operator;
                            // Suggestion: create a method that takes an operator as argument and
                            // then executes the while loop; also, move the stacks out of the main
                            // method                            
                            execute(opdStack,oprStack);
                        }
                    }
                    if (newOpr.in_priority() != 2)
                        oprStack.push(newOpr);
                }   
            }
        }
        // clears the stack
        oprStack.clear();
        
        // returns the final value from opdStack
        return ((Operand)opdStack.pop()).getValue();
    }
}