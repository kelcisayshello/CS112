package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate(), add() and multiply() for polynomials
 * 
 * @author kelci mensah, klm371, kelci.mensah@rutgers.edu
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) //reads a polynomial from an input source
			throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {

		if (poly1 == null && poly2 == null) {
			return new Node(0,0,null);
		}
		
		Node cursor1 = poly1;
		Node cursor2 = poly2;
		Node temp = new Node(0,0, null);
		Node addSum = temp;
		
		while(cursor1 != null && cursor2 != null){
			if(cursor1.term.degree == cursor2.term.degree && (cursor1.term.coeff + cursor2.term.coeff) != 0){
				
					temp.next = new Node(cursor1.term.coeff+cursor2.term.coeff, cursor1.term.degree, null);
					temp = temp.next;
					cursor1 = cursor1.next;
					cursor2 = cursor2.next;
			}
			else if(cursor1.term.degree < cursor2.term.degree){
					temp.next = new Node(cursor1.term.coeff, cursor1.term.degree, null);
					temp = temp.next;
					cursor1 = cursor1.next;
			}
			else if (cursor1.term.degree > cursor2.term.degree){
					temp.next = new Node(cursor2.term.coeff, cursor2.term.degree, null);
					temp = temp.next;
					cursor2 = cursor2.next;
			}
			else {
					cursor1 = cursor1.next;
					cursor2 = cursor2.next;
			}
		}
		
		while(cursor1 != null){
			temp.next = new Node(cursor1.term.coeff, cursor1.term.degree, null);
			temp = temp.next;
			cursor1 = cursor1.next;
		}
		while(cursor2 != null){
			temp.next = new Node(cursor2.term.coeff, cursor2.term.degree, null);
			temp = temp.next;
			cursor2 = cursor2.next;
		}
		
		return addSum.next;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		
		if (poly1 == null || poly2 == null) {
			return null;
		}
		
		Node cursor1 = poly1;
		Node cursor2 = poly2;
		Node temp = new Node (0, 0, null);
		Node multiply = temp;
		
		int maxDeg = 0;
		
		while (cursor1 != null) {	//successfully multiplied node
			while (cursor2 != null) {
				
				int xDeg = cursor1.term.degree + cursor2.term.degree;
				temp.next = new Node(cursor1.term.coeff*cursor2.term.coeff, xDeg, null);
				temp = temp.next;
				cursor2 = cursor2.next;
				
				if (xDeg > maxDeg) {
					maxDeg = xDeg;
				}
				
			}
			cursor1 = cursor1.next;
			cursor2 = poly2;
		}
		
		Node multiplied = null; //combining like terms (add method)
		for (int i = maxDeg; i >= 0; i--) {
			Node cursor = multiply.next;
			float cSum = 0;
			while (cursor != null) {
				if (cursor.term.degree == i) {
					cSum += cursor.term.coeff;
				}
				cursor = cursor.next;
			}
			
			if (cSum != 0) {
				multiplied = new Node(cSum, i, multiplied);
			}
//			multiplied = new Node(cSum, i, multiplied);
//			if (multiplied.next.term.coeff == 0) {
//				multiplied.next = null;
//			}
		}
		
        return multiplied;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		
		if (poly == null) {
			return 0;
		}
		
		Node cursor = poly;
		double eval;
		float total = 0;
		
		while (cursor != null) {
			eval = cursor.term.coeff * (Math.pow(x, cursor.term.degree));
			total += eval;
			cursor = cursor.next;		
		}
		
	 return total;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
