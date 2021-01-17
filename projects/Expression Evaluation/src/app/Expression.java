package app;

import java.io.*;

import java.util.*;
import java.util.regex.*;

import structures.Stack;
import java.util.StringTokenizer;

/* @author: kelci mensah, klm371, kelci.mensah@rutgers.edu */

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	
	public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	
	ArrayList<Variable> vars2 = new ArrayList<Variable>();
	ArrayList<Array> arrays2 = new ArrayList<Array>();
    	StringTokenizer expression = new StringTokenizer(expr, " \t*+-/()]");
    	String temp = "";
    	
    	while(expression.hasMoreTokens()) {
    		temp = expression.nextToken();
    		
    		if (temp.equals(" ")) {
    			continue;
    		}
    		char chari = temp.charAt(0);
    		if (Character.isDigit(chari)) {
    			continue;
    		}
    		
    		if (temp.contains("[")) {
    			String[] list = temp.split("\\[", 0);
    			for(int i = 0; i < list.length-1; i++) {
    				if (arrays2.contains(list[i]) == false) {
    					Array item = new Array(list[i]);
        				arrays2.add(item);
    				}
    			}
    			
    			String last = list[list.length-1];
    			char ascii = last.charAt(0);
    			if(last.equals(" ")) {
    				continue;
    			} else if (Character.isDigit(ascii)) {
    				continue;
    			} else {
    				if (vars2.contains(temp) == false) {
    					Variable variable = new Variable(last);
        				vars2.add(variable);
    				}
    			}
    		} else {
    			Variable var = new Variable(temp);
    			vars2.add(var);
    		}
    			
    	}
    	
    	for(Array item : arrays2) {
    		if (!arrays.contains(item)) {
    			arrays.add(item);
    		}
    	}
    	
    	for(Variable item : vars2) {
    		if (!vars.contains(item)) {
    			vars.add(item);
    		}
    	}
    		
    		
    	System.out.println("Arrays: " + arrays);
    	System.out.println("Variables: " + vars);
    	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    		throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
	    	expr = expr.replaceAll("\\s+", "");	//removing extra white space
	    	char[] keys = expr.toCharArray();
	    	
	    	Stack<Character> operator = new Stack<Character>();
	    	Stack<Float> digit = new Stack<Float>();
	    	Stack<String> arraysStack = new Stack<String>();
    	
	    	for (int i = 0; i < keys.length; i++) {
	    		if (keys[i] >= '0' && keys[i] <= '9') {
	    			String strNum = "";
	    			int j = i;
	    			
	    			while (j < keys.length && keys[j] >= '0' && keys[j] <= '9') {
	    				strNum += keys[j];
	    				j = j + 1;
	    			}
	    			
	    			i = j - 1;
	    			digit.push( (float)Integer.parseInt(strNum) );
	    		}
	    		else if ((keys[i] >= 'a' && keys[i] <= 'z') || (keys[i] >= 'A' && keys[i] <= 'Z')) {//if its lowercase or uppercase
	    			String variable = "";
	    			int j = i;
	    			
	    			while (j < keys.length && ((keys[j] >= 'a' && keys[j] <= 'z') || (keys[j] >= 'A' && keys[j] <= 'Z'))) {
	    				variable += keys[j];
	    				j++;
	    			}
	    			
	    			i = j - 1;
	    			
	    			if (i == keys.length - 1) {	//CASE: last index == variable
	    				for (int k = 0; k < vars.size(); k++) {
	        				if (variable.equals(vars.get(k).name)) {
	        					digit.push((float)vars.get(k).value);
	        					break;
	        				}
	        			}
	    			}
	    			
	    			else if (i <= keys.length - 1 && keys[i + 1] != '[') {	//CASE: if no [ afterwards, its a long variable name
	    				for (int k = 0; k < vars.size(); k++) {
	        				if (variable.equals(vars.get(k).name)) {
	        					digit.push((float)vars.get(k).value);
	        					break;
	        				}
	        			}
	    			}
	    			
	    			else if (i <= keys.length - 1 && keys[i + 1] == '[') {	//CASE: if its an array name
	    				for (int k = 0; k < arrays.size(); k++) {
	    					if (variable.equals(arrays.get(k).name)) {
	    						arraysStack.push(arrays.get(k).name);
	    					}
	    				}
	    			}
	    			
	    		}
	    		
	    		else if (keys[i] == '(') {	//find open (
	    			operator.push(keys[i]);
	    		}
	    		
	    		else if (keys[i] == ')') {	//find right )
	    			while (operator.peek() != '(') {
	    				digit.push(calculate(operator.pop(), digit.pop(), digit.pop()));
	    			}
	    			operator.pop();
	    		}
	    		
	    		else if (keys[i] == '[') { //find left [
	    			operator.push(keys[i]);
	    		}
	    		
	    		else if (keys[i] == ']') {	//find right ]
	    			while (operator.peek() != '[') {
	    				digit.push(calculate(operator.pop(), digit.pop(), digit.pop()));
	    			}
	    			
	    			float temp = digit.pop();
	    			
				for (int k = 0; k < arrays.size(); k++) {
					if (arraysStack.peek().equals(arrays.get(k).name)) {
						digit.push( (float)arrays.get(k).values[(int) temp]);
						arraysStack.pop();
						break;
					}
				}
				operator.pop();
	    		}
	    		
	    		else if (keys[i] == '+' || keys[i] == '-' || keys[i] == '*' || keys[i] == '/') {	//find correct operator
	    			while (!operator.isEmpty() && pemdas(keys[i], operator.peek())) {
	    				digit.push(calculate(operator.pop(), digit.pop(), digit.pop()));
	    			}
	    			operator.push(keys[i]);
	    		}
	    	}
	    	
	    	while (!operator.isEmpty()) {
	    		digit.push(calculate(operator.pop(), digit.pop(), digit.pop()));
	    	}
	    	
	   return digit.pop();
	}
    
    
 //helpers 1) order of operations 2) calculator
    private static boolean pemdas(char currentOp, char stackOp) {
	    	if (stackOp == '(' || stackOp == ')') {
	    		return false;
	    	}
	    	
	    	if (stackOp == '[' || stackOp == ']') {
	    		return false;
	    	}
	    	
	    	if ((currentOp == '*' || currentOp == '/') && (stackOp == '+' || stackOp == '-')) {
	    		return false;
	    	} else {
	    		return true;
	    	}
    }
	    
    private static float calculate(char operator, float a, float b) {
    		if (operator == '+') {
    	 		return a + b;
    	 	} else if (operator == '-') {
    	 		return b - a;
    	 	} else if (operator == '*') {
    	 		return a * b;
    	 	} else if (operator == '/') {
    	 		if (b == 0) {
    	 			// do nothing
    	 		} else {
    	 			return b / a;
    	 		}
    	 	}
    	 	
	    	return 0;
    }
}
