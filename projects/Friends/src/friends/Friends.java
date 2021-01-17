package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

/* @author: kelci mensah, klm371, kelci.mensah@rutgers.edu */

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		//1) check whether any @params are NULL
		//2) loop through graph using BFS with a queue
			//2a) if no first friend, return NULL b/c there is no chain
			//2b) there is ATLEAST a first friend
				//2b.1) check whether p2 matches the target
				//2b.2) retrace steps from p2 back to p1
				//2b.3) add p1 name to front of list and return
		//3) return null if target not found
		
		if (g == null || p1 == null || p2 == null) {	//1
			return null;
		}
		
		ArrayList<String> chain = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];	//because you must visit all members
		Queue<Person> bfsqueue = new Queue<Person>();	//used in 'breadth-first search'
				
		Person[] passed = new Person[g.members.length];
				
		int i = g.map.get(p1);	//index in visited array
				
		bfsqueue.enqueue(g.members[i]);
		visited[i] = true;
		
		while (bfsqueue.isEmpty() == false) {	//2	
			Person cursor = bfsqueue.dequeue();
			int iTurn = g.map.get(cursor.name);
			visited[iTurn] = true;
			Friend friendCursor = cursor.first;
			
			if (friendCursor == null) {	//2a
				return null;
			}
					
			while (friendCursor != null) {	//2b
				if (visited[friendCursor.fnum] == false) {
					visited[friendCursor.fnum] = true;	//now visited
					
					passed[friendCursor.fnum] = cursor; 
					bfsqueue.enqueue(g.members[friendCursor.fnum]);
							
					if (g.members[friendCursor.fnum].name.equals(p2)) {	//2b.1
						cursor = g.members[friendCursor.fnum];	//set cursor to this point
								
						while (cursor.name.equals(p1) == false) {	//2b.2
							chain.add(0, cursor.name);
							cursor = passed[g.map.get(cursor.name)];
						}
						
						chain.add(0, p1);	//2b.3
						return chain;
					}
				}
				
				friendCursor = friendCursor.next;
			}
		}
		
		return null;	//3
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		//1) check whether @params are null, return NULL immediately
		//2) loop through entire graph
			//2a) only consider people who are students AND have same school name
			//2b) conduct a BFS to populate arraylist with clique members
			//2c) only add clique to list IF clique consists of more than 1 person
		
		if (g == null || school == null) {	//1
			return null;
		}
		
		String academy = school.toLowerCase();	//graph input for school might include uppercase (i.e. "Rutgers")
		academy = academy.replaceAll("\\s+", "");	//no spaces (i.e. Penn State)
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		
		for(int i = 0; i < g.members.length; i++) {	//2
			if(g.members[i].student == true && visited[g.map.get(g.members[i].name)] == false)	{	//2a
				ArrayList<String> clique = new ArrayList<>();
				cliqueBFS( g, academy, clique, g.members[i], visited);	//2b
				if(clique.size() > 0) {
					list.add(clique);
				}
			}
		}
		
		return list;
	}
	
	private static void cliqueBFS(Graph g, String school, ArrayList<String> clique, Person cursor, boolean[] visited) {
		//1) check if they are a student and haven't been looked at
			//1a) if school name matches, add name to clique list
		//2) while friend does not equal null
			//2a) if friend you are looking at is a student and not visited
			//2b) check school name matches for RECURSIVE call
			
		int cursorIndex = g.map.get(cursor.name);
		
		if (visited[cursorIndex] == false && cursor.student == true) {	//1
			if(cursor.school.replaceAll("\\s+", "").equals(school)) {	//1a
				clique.add(cursor.name);
			}	
		}
		
		visited[cursorIndex] = true;
		
		Friend friend = cursor.first;
		while (friend != null) {
			int i = friend.fnum;
			Person friendCursor = g.members[i];
			if(friendCursor.student == true && visited[i] == false) {	//2a
				if (friendCursor.school.replaceAll("\\s+", "").equals(school)) {	//2b
					cliqueBFS(g, school, clique, g.members[i], visited);
				}
			}
			friend = friend.next;
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		//1) check if @param is null
		//2) conduct dfs search for each person in graph not visited
		//3) check if only once friend for that person
			//3a) person must be a connector, add to list
		//4) check if person is vertex w/ 1 edge
			//4a) (reassurance) remove item if found to have no edges or only 1 edge
		
		if (g == null) {	//1
			return null;
		}
		
		ArrayList<String> connectors = new ArrayList<String>();
		int[] passed = new int[g.members.length];
		int[] dfs = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		
		for (Person person : g.members) {
			if (visited[g.map.get(person.name)] == false) {	//2
				dfs = new int[g.members.length];
				int i = g.map.get(person.name);
				dfSearch(g, i, i, visited, dfs, passed, connectors);
			} else {
				break;
			}
		}
		
		for (Person cursor : g.members) {
			int k = cursor.first.fnum;
			
			if (cursor.first.next == null) {	//3
				if (connectors.contains(g.members[k].name) == false) {
					connectors.add(g.members[k].name);	//3a
				}
			}
			
			for (int j = 0; j < connectors.size(); j++) {	//4
				Friend friend = g.members[g.map.get(connectors.get(j))].first;
				int total = 0;
				
				while (friend != null) {	//4
					total++;
					friend = friend.next;
				}
				
				if (total == 1 || total == 0) {	//4a
					connectors.remove(j);
				}
			}
		}
		
		return connectors;
		
	}
	
	private static void dfSearch(Graph g, int i, int n, boolean[] visited, int[] dfs, int[] passed, ArrayList<String> result) {
		//1) check if dfs is set correct
		//2) loop through friends and continue while current friend doesn't equal null
		//3) recursive call if not visited
			//3a) isn't a connector, adjust position
		
		int count = length(dfs) + 1;
		Person person = g.members[n];
		Friend friend = person.first;
		
		if (passed[n] == 0 && dfs[n] == 0) {	//1
			dfs[n] = count;
			passed[n] = dfs[n];
		}
		
		visited[g.map.get(person.name)] = true;
		
		while (friend != null) {	//2
			int k = friend.fnum;
			if (visited[k] == false) {
				dfSearch(g, i, k, visited, dfs, passed, result);	//3
			
				if (dfs[n] > passed[k]) {
					passed[n] = Math.min(passed[n], passed[k]);
				} else {	//3a
					int hold01 = Math.abs(dfs[n] - passed[k]);
					int hold02 = Math.abs(dfs[n] = dfs[k]);
					
					if (hold01 < 1 && hold02 <= 1 && passed[k] == 1 && n == i) {
							continue;
					}
					
					if (dfs[n] <= passed[k] && (n != i || passed[k] == 1)){
						String string = g.members[n].name;
						if (result.contains(string) == false) {
							result.add(string);
						}
					}
				}
			} else {
				passed[n] = Math.min(passed[n], dfs[friend.fnum]);
			}
			
			friend = friend.next;
		}
	}
	
	private static int length(int[] array) {
		//1) loop through array
		//2) add +1 to count for every element that isn't 0
		//3) return count
		
		int count = 0;
		
		for(int i = 0; i < array.length; i++) {	//1
			if (array[i] != 0) {
				count++;	//2
			}
		}
		
		return count;
	}
}

