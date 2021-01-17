package trie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TrieApp {

	static Scanner stdin = new Scanner(System.in);
	
	public static void main(String[] args) 
	throws IOException {
		System.out.print("Enter words file name => ");
		String wordsFile = stdin.nextLine();
		Scanner sc = new Scanner(new File(wordsFile));
		// words appear one per line in input file
		// first line has number of words
		int numWords = Integer.parseInt(sc.nextLine());
		String[] allWords = new String[numWords];
		for (int i=0; i < allWords.length; i++) {
			allWords[i] = sc.nextLine().trim().toLowerCase();
		}
		sc.close();
		
		// build Trie
		TrieNode root = Trie.buildTrie(allWords);
		// print it for verification
		Trie.print(root, allWords);
		// do completion lists
		completionLists(root, allWords);
	}
	
	private static void completionLists(TrieNode root, String[] allWords) {
		System.out.print("\ncompletion list for (enter prefix, or 'quit'): ");
		String prefix = stdin.nextLine().trim().toLowerCase();
		while (!"quit".equals(prefix)) {
			ArrayList<TrieNode> matches = Trie.completionList(root, allWords, prefix);
			printMatches(matches, allWords);
			System.out.print("\ncompletion list for: ");
			prefix = stdin.nextLine().trim().toLowerCase();
		}
	}
	
	private static void printMatches(ArrayList<TrieNode> matches, String[] allWords) {
		if (matches == null) {
			System.out.println("No match");
			return;
		}
		System.out.print(allWords[matches.get(0).substr.wordIndex]);
		for (int i=1; i < matches.size(); i++) {
			System.out.print(","+allWords[matches.get(i).substr.wordIndex]);
		}
		System.out.println();
	}
	
}
