package lse;

import java.io.*;
import java.util.*;

//@author Kelci Mensah

public class Driver {
	
	LittleSearchEngine lse;
	
	public Driver() {
		lse = new LittleSearchEngine();
	}
	
	public void loadNoiseWords() throws FileNotFoundException {
		Scanner sc = new Scanner(new File("noisewords.txt"));
		while (sc.hasNext()) {
			String word = sc.next();
			this.lse.noiseWords.add(word);
		}
		sc.close();
	}
	
	public void getKeywordTester() throws FileNotFoundException {
		this.loadNoiseWords();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Now Testing 'getKeyword()' . . .\n");
		System.out.print("Enter string sequence: ");
		
		String str = lse.getKeyword(scanner.next());
		
		System.out.println("Your keyword --> " + str);
		scanner.close();
	}
	
	public void loadKeywordsFromDocumentTester() throws FileNotFoundException {
		this.loadNoiseWords();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Now Testing 'loadKeywordsFromDocumentTester()' . . .\n");

		System.out.print("Enter file name: ");
		HashMap<String,Occurrence> keyHash = lse.loadKeywordsFromDocument(scanner.next());
		
		scanner.close();
		
		Set<String> keySet = keyHash.keySet();
		Iterator<String> cursor = keySet.iterator();
		
		while (cursor.hasNext()) {
			String string = cursor.next();
			System.out.print(string + " " + keyHash.get(string).frequency + "\n");
		}
	}
	
	public void makeIndexTester() throws FileNotFoundException {
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the docFile: ");
		String docFile = scanner.next();
		System.out.println();
		System.out.print("Enter the noiseFile: ");
		String noiseWordsFile = scanner.next();
		System.out.println();
		
		this.lse.makeIndex(docFile, noiseWordsFile);
		
		for (String key : this.lse.keywordsIndex.keySet()) {
			System.out.println (key + "\t :: " + this.lse.keywordsIndex.get(key).toString());
		}	
		scanner.close();
	}
	
	public void insertLastOccuranceTester() {
		System.out.println("Now Testing 'insertLastOccurancer()' . . .\n");
		Scanner sc = new Scanner(System.in);
		
		int size = (int) (Math.random() * 11);
		
		ArrayList<Occurrence> occArr = new ArrayList<>(size);
		
		for (int i = 0; i < size; i++) {
			Occurrence temp = new Occurrence(null, (int)(Math.random()*31));
			occArr.add(temp);
			lse.insertLastOccurrence(occArr);
		}
		
		System.out.print("Here is the occurrence list: [");
		for (int i = 0; i < occArr.size(); i++) {
			if (i==occArr.size() - 1) {
				System.out.print(occArr.get(i).frequency);
			}
			else System.out.print(occArr.get(i).frequency + ", ");
		}
		System.out.println("]");
		
		System.out.print("Enter a frequency: ");
		
		String input = sc.next();
		
		System.out.println();
		
		while (!"quit".equals(input)) {
			int freq = Integer.parseInt(input);
			Occurrence temp = new Occurrence(null, freq);
			occArr.add(temp);
			ArrayList<Integer> midPts = lse.insertLastOccurrence(occArr);
			System.out.print("Here is the occurrence list: [");
			for (int i = 0; i < occArr.size(); i++) {
				if (i==occArr.size() - 1) {
					System.out.print(occArr.get(i).frequency);
				}
				else System.out.print(occArr.get(i).frequency + ", ");
			}
			System.out.println("]");
			System.out.print("Here are the midpoints: [");
			for (int i = 0; i < midPts.size(); i++) {
				if (i==midPts.size() - 1) {
					System.out.print(midPts.get(i));
				}
				else System.out.print(midPts.get(i) + ", ");
			}
			System.out.println("]");
			System.out.print("Enter another frequency or quit: ");
			input = sc.next();
			System.out.println();
		}
		
		sc.close();
	}
	
	public void top5SearchTest() throws FileNotFoundException {
		System.out.println("Now Testing 'top5Search()' . . .");
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("\nEnter the DOCUMENT name: ");
			String doc = scanner.next();
		System.out.print("Enter the NOISE WORDS file name: ");
		String noiseWordsFile = scanner.next();
		
		this.lse.makeIndex(doc, noiseWordsFile);
		
		String exit = "pending";
		while (!"Q".equals(exit)) {
			System.out.print("Enter keyword 1 :: ");
				String key1 = scanner.next();
			System.out.print("Enter keyword 2 :: ");
				String key2 = scanner.next();
			System.out.println();
			
			ArrayList<String> result = this.lse.top5search(key1, key2);
			
			for (int i = 0; i < result.size(); i++) {
				System.out.print("Found In: " + "[" + result.get(i) + "]" + "\t");
			}
			System.out.println();
			System.out.print("Enter 'Q' to quit, or press any key to continue: ");
				exit = scanner.next();
		}
		scanner.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		Driver driver = new Driver();
		
		Scanner user = new Scanner(System.in);
		
		System.out.println(" ~ METHOD TESTER MENU ~");
		System.out.println("\t1 - getKeyword");
		System.out.println("\t2 - loadKeywordsFromDocument");
		System.out.println("\t3 - makeIndex");
		System.out.println("\t4 - top5Search");
		System.out.println("\t5 - insertLastOccurance");
		
		System.out.print("\nEnter your choice :: ");
		
		String choice = user.next();
		
		switch (choice) {
			case "1" :
				driver.getKeywordTester();
				break;
			case "2" :
				driver.loadKeywordsFromDocumentTester();
				break;
			case "3" :
				driver.makeIndexTester();
				break;
			case "4" :
				driver.top5SearchTest();
				break;
			case "5" :
				driver.insertLastOccuranceTester();
				break;
			default :
				user.close();
		}
	}

}
