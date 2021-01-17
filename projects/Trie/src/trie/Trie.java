package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author: kelci mensah, klm371, kelci.mensah@rutgers.edu
 *
 */
public class Trie {
	
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		//CASE 1: if array[] of Strings has no words, RETURN null
			//ELSE --> add first word as tree.child
		//CASE 2: NO commonalities (cursor can have infinite # of siblings), RETURN new sibling of tree.child
		//CASE 3: there are commonalities!
			//CASE 3.A: partial match: anywhere between (first letter) and (before last letter)
			//CASE 3.B: full match: there's an entire string match

		if (allWords.length == 0) { //CASE 1: ARRAY[] HAS NO WORDS
			  return null;
		}
			             //TRIENODE: (substr, first child, sibling)
		TrieNode tree = new TrieNode(null, null, null);	//root of the *trie*
								           //INDEXES: (string index, start index, end index)
		tree.firstChild = new TrieNode(new Indexes(0, (short) 0, (short) (allWords[0].length() - 1)), null, null);

		int pathfinder = -1;		//method call to PRECHECK(): numeric variable will determine path
			TrieNode prev = tree;
			TrieNode cursor = tree.firstChild;
		
		for(int i = 1; i < allWords.length; i += 1) {
			TrieNode prefixCursor = null;
			
			//PATH DETERMINERS
				//T --> go to x.path_name
				//F --> do not go to x.path_name
			boolean siblingpath = false;
			boolean childpath = true;
			
			boolean determineInsert = false;

			while(cursor != null) {	//CASE 2 or CASE 3: DETERMINE IF THERE ARE COMMONALITIES
				pathfinder = preCheck(allWords[cursor.substr.wordIndex].substring(0, cursor.substr.endIndex+1), allWords[i]);

				if (pathfinder == 0) {	//CASE 2: NO MATCH --> becomes a cursor.sibling
					prev = cursor;
					cursor = cursor.sibling;
					childpath = false;
				} else {		//CASE 3: THERE ARE COMMONALITIES
					if ((pathfinder - 1) == cursor.substr.endIndex) {   	//CASE 3.B: FULL MATCH
						prev = cursor;
						cursor = cursor.firstChild;
						
						childpath = true;
						siblingpath = true;
						
						prefixCursor = prev;
					} else { //CASE 3.A: PARTIAL MATCH
						Indexes prefix = null;
						Indexes child1 = null;
						Indexes child2 = null;
						
						if (siblingpath == true) {
							if ((pathfinder-1) != prefixCursor.substr.endIndex) {
								prefix = new Indexes(cursor.substr.wordIndex, (short) (cursor.substr.endIndex + 1), (short) (pathfinder - 1));
								child1 = new Indexes(i, (short) pathfinder, (short) (allWords[i].length() - 1));
								child2 =  new Indexes(cursor.substr.wordIndex, (short) pathfinder, (short) cursor.substr.endIndex);
								
								cursor = new TrieNode(child2, cursor.firstChild, cursor.sibling);
								TrieNode term = new TrieNode(child1, null, null);
								TrieNode updater = new TrieNode(prefix, cursor, cursor.sibling);
								cursor.sibling = term;
								
								if (childpath == false) {
									prev.sibling = updater;
								} else {
									prev.firstChild = updater;
								}
								
								determineInsert = true;
								break;
							} else {		//continue
								prev = cursor;
								cursor = cursor.sibling;
								childpath = false;
							}
						} else {
							prefix = new Indexes(cursor.substr.wordIndex, (short) 0, (short) (pathfinder - 1));
							child1 = new Indexes(i, (short) pathfinder, (short) (allWords[i].length() - 1));
							child2 =  new Indexes(cursor.substr.wordIndex, (short) pathfinder, (short) cursor.substr.endIndex);
							
							cursor = new TrieNode(child2, cursor.firstChild, cursor.sibling);
							TrieNode term = new TrieNode(child1, null, null);
							TrieNode updater = new TrieNode(prefix, cursor, cursor.sibling);
							cursor.sibling = term;
							
							if (childpath == false) {
								prev.sibling = updater;
							} else {
								prev.firstChild = updater;
							}
							
							determineInsert = true;
							break;
						}
					}
				} //END CASE 3
			} //ENDWHLE
				
				
			if (determineInsert == false) {	//CASE 2: NO COMMONALITIES
				if (siblingpath == true) {
					Indexes stringer = new Indexes(i, (short) (prefixCursor.substr.endIndex+1), (short) (allWords[i].length()-1));
					TrieNode updater = new TrieNode(stringer, null, null);
					prev.sibling = updater;
				} else {
					Indexes stringer = new Indexes(i, (short) 0, (short) (allWords[i].length()-1));
					TrieNode updater = new TrieNode(stringer, null, null);
					prev.sibling = updater;
				}
			}

			//RESET POINTERS TO DEFAULT
			prev = tree;
			cursor = tree.firstChild;

		}
		return tree;	
	}

	private static int preCheck(String string1, String string2) {	 //determines path based on compared strings
		//RETURN 0 --> NO MATCH

		int indicator = 0;
		int length = Math.min(string1.length(), string2.length());
		
		for (int i = 0; i<length; i++) {
			if(string1.charAt(indicator) == string2.charAt(indicator)) {
				indicator++;
			}
		}
		
		//while(indicator < str1.length() && indicator < str2.length() && str1.charAt(indicator) == str2.charAt(indicator)) {
		//   indicator++;
		//	}

		return indicator;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		//CASE 1: RETURN null if "tree" or array[] is empty
		//CASE 2: determine whether node is a prefix or a word
			//CASE 2.A: whole word? --> immediately add to list
			//CASE 2.B: prefix? --> recursive call to completionList until reached 'x.firstchild == null'
		//CASE 3: RETURN null when list is empty
		
		if (root == null || allWords.length == 0) { //CASE 1: EITHER IS NULL
			return null;
		}
		
		TrieNode cursor = root;
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();	 // list of words with matches

		while(cursor != null) {
			if(cursor.substr == null) {
				cursor = cursor.firstChild;
			}
			
			String string = allWords[cursor.substr.wordIndex];
			String substring = string.substring(0, cursor.substr.endIndex+1);
			
			//CASE 2: (partial word is prefix) OR (word is whole prefix)
			if ( (prefix.startsWith(substring) || string.startsWith(prefix)) && string.length() >= prefix.length()) {
				if (cursor.firstChild == null) {   //CASE 2.A: WHOLE WORD MATCH
					list.add(cursor);
				} else {   //CASE 2.B: PREFIX IS A PARTIAL WORD --> RECURSE . . .
					ArrayList<TrieNode> temporaryList = completionList(cursor.firstChild, allWords, prefix);
					if (temporaryList == null) {	//if temporary list is empty, keep moving
						cursor = cursor.sibling;
						continue;
					} else {
						list.addAll(temporaryList);	//add every leaf node left
					}
				}
			}
				cursor = cursor.sibling;
		}  //ENDWHILE
				
		
		if (list.size() == 0) {	//CASE 3: LIST IS EMPTY --> RETURN null . . .
			return null;
		}
		
		return list;
	}
		
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
