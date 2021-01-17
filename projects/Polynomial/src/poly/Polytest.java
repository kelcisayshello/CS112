package poly;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Polytest {
	static Scanner sc1, sc2;
	static Node poly1, poly2;
	
	public static final int ADD = 1;
	public static final int MULTIPLY = 2;
	public static final int EVALUATE = 3;
	public static final int QUIT = 4;
	
	public static int getChoice() 
	throws IOException {
		System.out.println();
		System.out.println(ADD + ". ADD polynomial");
		System.out.println(MULTIPLY + ". MULTIPLY polynomial");
		System.out.println(EVALUATE + ". EVALUATE polynomial");
		System.out.println(QUIT + ". QUIT");
		System.out.print("\tEnter choice # => ");
		return (Integer.parseInt(sc1.nextLine()));
	}
	
	public static void add() 
	throws IOException {
		System.out.print("Enter the file containing the polynomial to add => ");
		sc2 = new Scanner(new File(sc1.nextLine()));
		poly2 = Polynomial.read(sc2);
		System.out.println("\n" + Polynomial.toString(poly2) + "\n");
		System.out.println("Sum: " + 
						Polynomial.toString(Polynomial.add(poly1,poly2)) + "\n");
	}
	
	public static void multiply() 
	throws IOException {
		System.out.print("Enter the file containing the polynomial to multiply  => ");
		sc2 = new Scanner(new File(sc1.nextLine()));
		poly2 = Polynomial.read(sc2);
		System.out.println("\n" + Polynomial.toString(poly2) + "\n");
		System.out.println("Product: " + 
				Polynomial.toString(Polynomial.multiply(poly1,poly2)) + "\n");
	}
	
	public static void evaluate() 
	throws IOException {
		System.out.print("Enter the evaluation point x  => ");
		float x = Float.parseFloat(sc1.nextLine());
		System.out.println("Value at " + x + ": " + Polynomial.evaluate(poly1,x) + "\n");
	}
	
	public static void main(String[] args) throws IOException {
		sc1 = new Scanner(System.in);
		System.out.print("Enter the name of the polynomial file => ");
		sc2 = new Scanner(new File(sc1.nextLine()));
		
		poly1 = Polynomial.read(sc2);
		System.out.println("\n" + Polynomial.toString(poly1) + "\n");
		
		int choice = getChoice();
		while (choice != QUIT) {
			if (choice < 1 || choice > QUIT) {
				System.out.println("\tIncorrect choice " + choice);
			} else {
				switch (choice) {
				case ADD: add(); break;
				case MULTIPLY: multiply(); break;
				case EVALUATE: evaluate(); break;
				default: break;
				}
			}
			choice = getChoice();
		}
		
	}
}
