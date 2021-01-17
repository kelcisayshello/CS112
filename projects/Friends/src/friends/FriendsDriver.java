package friends;
import java.io.*;
import java.util.*;

import friends.Friends;
import friends.Graph;

/* @author: kelci mensah, klm371, kelci.mensah@rutgers.edu */

public class FriendsDriver {
	
	private static void cliquesTest(Graph g, Scanner sc) {
		System.out.print("School name: ");
		String school = sc.next();
		System.out.println();
		ArrayList<ArrayList<String>> list = Friends.cliques(g, school);
		for (int i = 0; i < list.size(); i++) {
			ArrayList<String> temp = list.get(i);
			Collections.sort(temp);
			System.out.print(temp.toString() + "\n");
		}
	}
	
	private static void connectionsTest(Graph g) {
		ArrayList<String> list = Friends.connectors(g);
		Collections.sort(list);
		System.out.print(list.toString() + "\n");
	}
	
	private static void shortestChainTest(Graph g, Scanner sc) {
		System.out.print("start at: ");
			String start = sc.next();
		System.out.print("end at: \n");
			String end = sc.next();
		ArrayList<String> list = Friends.shortestChain(g, start, end);
		System.out.print(list.toString() + "\n");
	}
	
	private static void fileTester(File file, Scanner sc) throws FileNotFoundException, NoSuchElementException{
		Scanner fileReader = new Scanner(file);
		Graph g = new Graph(fileReader);
		fileReader.close();
		String option = "";
		while (true) {
			System.out.println("\n'Friends' METHOD Menu");
			System.out.println("\t(1) shortestChain()");
			System.out.println("\t(2) cliques()");
			System.out.println("\t(3) connectors()");
			System.out.println("\t(4) <-- back to menu");
			System.out.println("\t(5) kill program");
			System.out.print("Choose a method :: ");
			option = sc.next();
			boolean backToMenu = false;
			switch (option) {
			case "1" : 
				shortestChainTest(g,sc);
				break;
			case "2" : 
				cliquesTest(g, sc);
				break;
			case "3" : 
				connectionsTest(g);
				break;
			case "4" : 
				backToMenu = true;
				break;
			case "5" : 
				System.exit(0);
			default :
				System.out.print("You have not entered a valid option :(\n");
				System.out.println("-------------------------------------");
			}
			if (backToMenu) break;
		}
		
	}
	
	private static void driver() {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("'Friends' DRIVER Menu ~");
			System.out.println("\t(1) enter a file name");
			System.out.println("\t(2) quit menu");
			System.out.print("Type in an option :: ");
				String option = " ";
				String fileName = " ";
				
			try {
				option = sc.next();
				if ("2".equals(option)) break;
				if (!"1".equals(option)) throw new InputMismatchException("Sorry, but you have entered an invalid option. Try again\n");
				System.out.print("enter file name :: ");
				fileName = sc.next();
				fileTester(new File(fileName), sc);
			} catch (FileNotFoundException e) {
				System.out.print("\n" + fileName + " is not a valid file name. \n");
				System.out.println("-------------------------------------\n");
				continue;
			} catch (NoSuchElementException n) {
				if (n instanceof InputMismatchException) {
					System.out.print(n.getMessage());
				} else {
					System.out.print(fileName + " is missing information\n");
					System.out.println("-------------------------------------");
				}
				continue;
			}
		}
		sc.close();
	}

	public static void main(String[] args) {
		driver();
	}
}
