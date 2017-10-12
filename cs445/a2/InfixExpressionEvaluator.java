package cs445.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;


/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream.
 */
public class InfixExpressionEvaluator {
    // Tokenizer to break up our input into tokens
    StreamTokenizer tokenizer;

    // Stacks for operators (for converting to postfix) and operands (for
    // evaluating)
    StackInterface<Character> operators;
    StackInterface<Double> operands;
	//String to help with error processing for operators and operands
	String lastTokenProcessed;
	//counters to help with error processing for brackets
	int openBrackets = 0;
	int closeBrackets = 0;
	int openParentheses = 0;
	int closeParentheses = 0;
    /**
     * Initializes the solver to read an infix expression from input.
     */
    public InfixExpressionEvaluator(InputStream input) {
        // Initialize the tokenizer to read from the given InputStream
        tokenizer = new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(input)));

        // Declare that - and / are regular characters (ignore their regex
        // meaning)
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        // Allow the tokenizer to recognize end-of-line
        tokenizer.eolIsSignificant(true);

        // Initialize the stacks
        operators = new ArrayStack<Character>();
        operands = new ArrayStack<Double>();
    }

    /**
     * A type of runtime exception thrown when the given expression is found to
     * be invalid
     */
    class ExpressionError extends RuntimeException {
        ExpressionError(String msg) {
            super(msg);
        }
    }

    /**
     * Creates an InfixExpressionEvaluator object to read from System.in, then
     * evaluates its input and prints the result.
     */
    public static void main(String[] args) {
        InfixExpressionEvaluator solver =
                        new InfixExpressionEvaluator(System.in);
		System.out.println("Type expression to evaluate");
        Double value = solver.evaluate();
        if (value != null) {
            System.out.println(value);
        }
    }

    /**
     * Evaluates the expression parsed by the tokenizer and returns the
     * resulting value.
     */
    public Double evaluate() throws ExpressionError {
        // Get the first token. If an IO exception occurs, replace it with a
        // runtime exception, causing an immediate crash.
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Continue processing tokens until we find end-of-line
        while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            // Consider possible token types
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    // If the token is a number, process it as a double-valued
                    // operand
                    processOperand((double)tokenizer.nval);
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    // If the token is any of the above characters, process it
                    // is an operator
                    processOperator((char)tokenizer.ttype);
                    break;
                case '(':
                case '[':
                    // If the token is open bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processOpenBracket((char)tokenizer.ttype);
                    break;
                case ')':
                case ']':
                    // If the token is close bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    processCloseBracket((char)tokenizer.ttype);
                    break;
                case StreamTokenizer.TT_WORD:
                    // If the token is a "word", throw an expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    tokenizer.sval);
                default:
                    // If the token is any other type or value, throw an
                    // expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    String.valueOf((char)tokenizer.ttype));
            }

            // Read the next token, again converting any potential IO exception
            try {
                tokenizer.nextToken();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Almost done now, but we may have to process remaining operators in
        // the operators stack
        processRemainingOperators();

        // Return the result of the evaluation
        // TODO: Fix this return statement
		double result = operands.peek();
        return result;
    }

    /**
     * Processes an operand.
     */
    void processOperand(double operand)throws ExpressionError{
        // TODO: Complete this method
		//error for using spaces
		if (lastTokenProcessed == "operand"){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		//add operand to proper stack
		operands.push(operand);
		//track that the operand was processed
		lastTokenProcessed = "operand";
    }

    /**
     * Processes an operator.
     */
    void processOperator(char operator) throws ExpressionError{
        // TODO: Complete this method
		//process various expressionerrors
		//line starts with operator
		if (operands.isEmpty()){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		//two operators in a row
		if (lastTokenProcessed == "operator"){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		//openbracket then operator
		if (lastTokenProcessed == "openBracket"){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		//do math according to order of operations
		while (!operators.isEmpty() && (determinePrecedence(operator, operators.peek()) != '1')){
			char topOperator = operators.pop();
			double second = operands.pop();
			double first = operands.pop();
			double result = doMath(first, second, topOperator);
			operands.push(result);
		}
		//add operator to proper stack 
		operators.push(operator);
		lastTokenProcessed = "operator";
		
    }

    /**
     * Processes an open bracket.
     */
    void processOpenBracket(char openBracket) {
        // TODO: Complete this method
		//add to proper stack
		operators.push(openBracket);
		if (openBracket == '['){
			openBrackets++;
		}else if (openBracket == '('){
			openParentheses++;
		}
		lastTokenProcessed = "openBracket";
		
    }

    /**
     * Processes a close bracket.
     */
    void processCloseBracket(char closeBracket) throws ExpressionError{
        // TODO: Complete this method
		if (closeBracket == ']'){
			closeBrackets++;
		}else if (closeBracket == ')'){
			closeParentheses++;
		}
		
		//make sure there are the same number of open and close brackets
		if (openBrackets != closeBrackets){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		if (openParentheses != closeParentheses){
			throw new ExpressionError("\nEXPRESSION ERROR: SYNTAX");
		}
		//do math operations that are in the brackets
		char topOperator = operators.pop();
		while (topOperator != '(' && topOperator != '['){
			double second = operands.pop();
			double first = operands.pop();
			double result = doMath(first, second, topOperator);
			operands.push(result);
			topOperator = operators.pop();
		}
    }

    /**
     * Processes any remaining operators leftover on the operators stack
     */
	 //do any math that's left
    void processRemainingOperators() {
        // TODO: Complete this method
		
		while (!operators.isEmpty()){
			char topOperator = operators.pop();
			double second = operands.pop();
			double first = operands.pop();
			double result = doMath(first, second, topOperator);
			operands.push(result);
		}
    }
	//determine the precedence for two operations according to PEMDAS
	//we only need to see whether the first has precedence, due to the algorithm
	char determinePrecedence(char one, char two){
		//make sure it doesn't try to do math with a bracket in processOperator();
		if (two == '(' || two == '['){
			return '1';
		}
		if (one != '+' && one != '-'){
			if (two == '+' || two == '-'){
				return '1';
			}
		}else if (one != '+' && one != '-' && one != '*' && one != '/'){
			if (two == '+' || two == '-' || two == '*' || two == '/'){
				return '1';
			}
		}
		return 'n';
	}
	//do math with 2 given numbers and an operation
	double doMath (double x, double y, char op)throws ExpressionError{
		if (op == '+'){
			return x + y;
		}else if (op == '-'){
			return x - y;
		}else if (op == '*'){
			return x * y;
		}else if (op == '/'){
			if (y == 0){
				throw new ExpressionError("\nEXPRESSION ERROR: DIVIDE BY ZERO");
			}
			return x / y;
		}else if (op == '^'){
			return Math.pow(x, y);
		}
		return 0.0;
	}

}

