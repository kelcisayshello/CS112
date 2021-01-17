package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String, ArrayList<Occurrence>> keywordsIndex;
	
	HashSet<String> noiseWords;
	
	public LittleSearchEngine() {	//Creates the keyWordsIndex and noiseWords hash tables
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
			throws FileNotFoundException {
		
		//1) code precheck
			//1) check if @param docFile is empty
			//1b) check if file exists/is a file --> if not, throw new EXCEPTION
		//2) read document file from method header
		//3) split each line into an array of strings (only looking at one line at a time)
		//4) check if string is a keyword
			//4a) if its a keyword, put it into the hashmap
			//4b) check if word is already in the hash table
		
		if (docFile == null) {
			throw new FileNotFoundException("file not found");
		}
		
		File document = new File(docFile);
		
		if (!document.exists() || !(document.isFile())) {	//1
			throw new FileNotFoundException("not allowed");
		}
		
		HashMap<String,Occurrence> keywords = new HashMap<String,Occurrence>();
		Scanner scan = new Scanner(document);	//2
		
		while (scan.hasNextLine()) {
			String nLine = scan.nextLine();	//3
			if (!nLine.trim().isEmpty() && !(nLine == null)) {
				String[] tkn = nLine.split(" ");
				for(int i = 0; i<tkn.length; i++) {
					String string = getKeyword(tkn[i]);	//4
					if (string != null) {
						if (keywords.containsKey(string)) {	//4a
							Occurrence key = keywords.get(string);
							key.frequency += 1;
							keywords.put(string, key);	
						} else {	//4b
							Occurrence key = new Occurrence (docFile, 1);
							keywords.put(string, key);	
						}
					} 
				} 
			}
		}
		scan.close();
		
		return keywords;
	}
	
	/**
	 * Puts the keywords hashmap from document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		
		//1) iterate through document hashmap
		//2) merge keys into master hashmap 
			//2a) check if keyword is not a duplicate
			//2b) keyword is a duplicate --> make chain
		
		for (String key : kws.keySet()) {	//1
			Occurrence position = kws.get(key);
			
			if (!(keywordsIndex.containsKey(key))) {	//2a
				ArrayList<Occurrence> temporary = new ArrayList<Occurrence>();
				temporary.add(position);
				keywordsIndex.put(key, temporary);	//2b
			} else {
				ArrayList<Occurrence> temporaryOccur = keywordsIndex.get(key);
				temporaryOccur.add(position);
				insertLastOccurrence(keywordsIndex.get(key));
			}
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		//1) if character is not a letter
		//2) character locator
			//2a) checks for punctuation
			//2b) checks for non-alpha characters
		//3) check if 'noiseWords' hashset already has the word or if the word is empty, return NULL

		//nonletters include: "!\\\"#$%&'()*+,-./:;?@[\\\\]^_`{|}~ '";
		String lowercase = word.toLowerCase();
		int size = lowercase.length();
	
		for (int i = 0; i < lowercase.length(); i++) {
			
			char c = lowercase.charAt(i);	
			if (!Character.isLetter(c)) {	//1
				if (!punctuation(c) || (i+1 < size && Character.isLetter(lowercase.charAt(i+1)))) {
					return null;
				} else if (punctuation(c)){	//2
					for (int j = i; j < size; j++) {
						if (Character.isLetter(lowercase.charAt(j)))
							return null;
					}
					
					lowercase = lowercase.substring(0, i);
					break;
				}
			}
		}
		
		if (noiseWords.contains(lowercase) || lowercase.isEmpty()) {	//3
			return null;
		} else {
			return lowercase;
		}
	}
	
	private boolean punctuation(char c) {
		//method checks for whether a character is one of the 5 punctuation
		
		if (c == '.' || c == ',' || c == '?' || c == ':' || c == ';' || c == '!') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Inserts the last occurrence, in the parameter list, in the correct position in same
	 * list based on ordering occurrences in descending frequencies. The elements
	 * 0 . . . n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		//1) check whether list is empty, return NULL
		//2) binary search for correct position
			//2a) check if last frequency == midpoint frequency
			//2b) check if last frequency is greater than midpoint
		//3) return updated list of frequencies sorted 0 . . . n
		
		if(occs == null || occs.size() == 1){	//1
			return null;
		}
		
		int left = 0; int mid = 0;
		int right = occs.size()-2;
		Occurrence last = occs.get(occs.size() - 1);
		
		ArrayList<Integer> midpts = new ArrayList<Integer>();
		
		while(left <= right) {	//2
			mid = (left + right)/2;
			midpts.add(mid);
			int frequency = occs.get(mid).frequency;
			
			if (frequency == last.frequency) {	//2a
				break;
			} else if (frequency > last.frequency) {	//2b
				left = mid+1;
			} else {
				right = mid-1;
			}
		}
		
		int item = occs.size() - 1;
		
		Occurrence temp = occs.remove(item);
		occs.add(mid+1, temp);

		if (right < left) {
			occs.add(left, occs.remove(item));
		}
		
		return midpts;	//3
		  
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile)
			throws FileNotFoundException {

		//1) check if docsFile exist/is even a file --> if not, throw new EXCEPTION
		//2) check if docsFile exist/is even a file --> if not, throw new EXCEPTION
		//3) place strings into noisewords hashSet
		//4) call 'loadkeywordsFromDocument()' to populate document hashmap
		//5) merge document hashmap with master hashmap 'keywordIndexes' using 'mergeKeywords()'

		
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
				
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		//1) check if arraylists are empty, or do not containt respective keys, return NULL
		//2) add all items to temporary list
			//2a) list1 is not empty --> add all to temporary arraylist
			//2b) list2 is not empty --> add all to temporary arraylist
		//3) add elements to top5 while the list is 5 or less
		//4) check so there are no duplicates

		
		ArrayList<Occurrence> list1 = keywordsIndex.get(kw1.toLowerCase());
		ArrayList<Occurrence> list2 = keywordsIndex.get(kw2.toLowerCase());
		
		if (list1 == null && list2 == null || 
				(keywordsIndex.isEmpty()) || 
				(!keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2))) {	//1
			return null;
		} 
		
		ArrayList<Occurrence> temporary = new ArrayList<Occurrence>();
			
		if (list1 != null) {	//2a
			temporary.addAll(list1);
		}
		
		if (list2 != null) {	//2b
			temporary.addAll(list2);
		}
			
	
		String string = "";
		int position = 0;
		
		ArrayList<String> top5 = new ArrayList<String>();
		while (top5.size() < 5) {	//3
			int limit = 0;
			
			for (int i = 0; i < temporary.size(); i++) {
				int frequency = temporary.get(i).frequency;
				if (frequency > limit) {
					limit = frequency;
					string = temporary.get(i).document;
					position = i;
				}
			}
			
			temporary.remove(position);
	
			if (!top5.contains(string)) {	//4
				top5.add(string);		
			}
						
			if (temporary.size() == 0) {
				break;	//exit while loop
			}
		}
		
		return top5;
		
	}
}
