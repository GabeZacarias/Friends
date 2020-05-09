package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

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
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		//initializations
		int current = g.map.get(p1);
		boolean[] v = new boolean[g.members.length];
		ArrayList<String> tempList = new ArrayList<>();
		ArrayList<String> list;
		Queue<ArrayList<String>> path = new Queue<>();
		Queue<Person> q = new Queue<>();

		//bfs
		q.enqueue(g.members[current]);
		boolean empty = q.isEmpty();
		tempList.add(g.members[current].name);
		path.enqueue(tempList);

		//traverse
		while(empty == false) 
		{
			if(q.isEmpty() == true)
				break;

			Person student = q.dequeue();
			int index = g.map.get(student.name);
			v[index] = true;
			list = path.dequeue();

			for(Friend temp = g.members[index].first; temp != null; temp = temp.next) 
			{
				if(v[temp.fnum] == false) 
				{
					//create end path
					ArrayList<String> result = new ArrayList<>(list);
					String studentName = g.members[temp.fnum].name;
					result.add(studentName);

					//check if end student found
					if(p2.equals(studentName) == false)
					{
						q.enqueue(g.members[temp.fnum]);
						path.enqueue(result);
					}
					else
						return result;
				}
			}
		}
		return null;
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		//initializations
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		Queue<Integer> q = new Queue<Integer>();
		Person[] name = g.members;

		//traverse members
		for (Person member : name)
		{
			ArrayList<String> temp = new ArrayList<String>();
			int i = g.map.get(member.name);

			//if not visited and member
			if (visited[i] == false && school.equals(member.school) && member.school != null)
			{
				visited[i] = true;
				q.enqueue(i);
				temp.add(member.name);

				while (q.isEmpty() == false)
				{

					//traverse neighbors
					for (Friend nbr = g.members[q.dequeue()].first; nbr != null; nbr = nbr.next)
					{
						Person ptr = g.members[nbr.fnum];

						//if unvisited and a member, mark true, and add
						if (visited[nbr.fnum] == false && school.equals(ptr.school) && ptr.school != null)
						{
							visited[nbr.fnum] = true;
							q.enqueue(nbr.fnum);
							temp.add(g.members[nbr.fnum].name);
						}

					}
				}
				
				//add to arrayList
				cliques.add(temp);
			}
			
		}

		return cliques;
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g){
		//initializations
		ArrayList<String> results = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		boolean[] nbr = new boolean[g.members.length];

		//call helper method
		for(int i = 0; i < g.members.length; i++) 
		{
			if(visited[i] == false)
				connectorsDFS(g, i, visited, dfsnum, back, i, results, nbr);
			else
				continue;
		}

		return results;
	}

	//dfs helper
	private static void connectorsDFS(Graph g, int v, boolean[] visited, int[] dfsnum, int back[],  int i, ArrayList<String> connectors, boolean[] nbr){
		
		//initializations
		visited[v] = true;
		dfsnum[v] = dfsnum[i] + 1;
		back[v] = dfsnum[v];

		//traverse
		for(Friend ptr = g.members[v].first; ptr != null; ptr = ptr.next) 
		{
			int w = ptr.fnum;

			//check if node visited
			if(visited[w] == false)
			{
				connectorsDFS(g, ptr.fnum, visited, dfsnum, back, v, connectors, nbr);

				//check if back up from neighbor
				if((dfsnum[v] <= back[w]) && (connectors.contains(g.members[v].name) == false) && (v != i || nbr[v])) {
					connectors.add(g.members[v].name);
				}

				if(dfsnum[v] > back[w]) {
					back[v] = Math.min(back[v], back[w]);
				}

				else {
					nbr[v] = true;
				}
			}

			//neighbor already visited
			else {
				back[v] = Math.min(back[v], dfsnum[w]);
			}
			
		}
		
	}

	
}