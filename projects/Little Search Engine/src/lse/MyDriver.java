package lse;

import java.io.*;
import java.util.*;

//@author Kelci Mensah

public class MyDriver {
	
	public static void main (String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
			System.out.print("Enter the FILE NAME: ");
		String doc = scanner.nextLine();
	
		if (doc.length() < 1) {
			System.out.print("You must enter a FILE:");
			doc = scanner.nextLine();
		}
		
			System.out.print("Select a NOISE FILE: ");
		String noise = scanner.nextLine();
		if (noise.length() < 1) {
			System.out.print("You must enter a FILE:");
			noise = scanner.nextLine();
		}
		
		System.out.println();
		
		LittleSearchEngine bing = new LittleSearchEngine();
			bing.makeIndex(doc, noise);
			for (String key : bing.keywordsIndex.keySet()) {
				System.out.println (key + "\t :: " + bing.keywordsIndex.get(key).toString());
			}	
		System.out.println();
		System.out.println();
		
			System.out.print("key 1: ");
		String key1 = scanner.next();
			System.out.print("key 2: ");
		String key2 = scanner.next();
		scanner.close();
		
		ArrayList<String> search = bing.top5search(key1,  key2);
			System.out.println(search);
		System.exit(0);
		
	}
}