import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Filename:   Graph.java
 * Project:    p4
 * Authors:    Bon
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
	/**
	 * The graph Node
	 * @author Bon
	 *
	 */
	class Node {
		String name;
		Hashtable<String, Node> adjacent;
		
		/**
		 * Constructor of the graph node
		 * @param name
		 */
		public Node(String name) {
			this.name = name;
			adjacent = new Hashtable<String, Node>();
		}
	}
	
	Hashtable<String, Node> v_table; // implement graph using hashtable
	private int size;
	private int order;
	/*
	 * Default no-argument constructor
	 */ 
	public Graph() {
		v_table = new Hashtable<String, Node>();
		size = 0;
	}

	/**
	 * Add vertex to the graph, do nothing if the vertex is null
	 * @param vertex - the vertex needs adding
	 */
	@Override
	public void addVertex(String vertex) {
		if (vertex == null) {
			return;
		}
		// Increase the order if the vertex is not already in the graph
		if (v_table.putIfAbsent(vertex, new Node(vertex)) == null)
			order++;
	}

	/**
	 * Remove the vertex from the graph
	 * @param vertex - the vertex needs removing
	 */
	@Override
	public void removeVertex(String vertex) {
		// Check if the vertex is null and is in the graph or not
		if (vertex != null && v_table.remove(vertex) != null) {
			order--; // decrease the order if it is
			v_table.forEach((k, v) -> { // remove the edges to the vertex
				v.adjacent.remove(vertex);
			});
		}
	}

	/**
	 * Add the edge to the vertex
	 * @param vertex1 - the starting point
	 * @param vertex2 - the destination
	 */
	@Override
	public void addEdge(String vertex1, String vertex2) {
		// Check if any of the vertex is null
		if (vertex1 != null && vertex2 != null) {
			// If any of the vertex is not in the graph, add it in and increase the order
			if (v_table.putIfAbsent(vertex1, new Node(vertex1))==null) {
				order++;
			}
			if (v_table.putIfAbsent(vertex2, new Node(vertex2))==null) {
				order++;
			}
			// Add the edge and increase the size if the edge is not already in the graph
			if (v_table.get(vertex1).adjacent.putIfAbsent(vertex2, v_table.get(vertex2)) == null) {
				size++;
			}
		}
	}

	/**
	 * Remove the edge from the graph
	 * @param vertex1 - the starting point
	 * @param vertex2 - the destination
	 */
	@Override
	public void removeEdge(String vertex1, String vertex2) {
		// Check if both vertex are non-null and in the graph
		if (vertex1 != null && vertex2 != null && v_table.containsKey(vertex1) && v_table.containsKey(vertex2)) {
			// Remove the edge and decrease the size
			v_table.get(vertex1).adjacent.remove(vertex2);
			size--;
		}
	}

	/**
	 * Return the set of all vertices in the graph
	 * @return set of all vertices
	 */
	@Override
	public Set<String> getAllVertices() {
		Set<String> vertices = new HashSet<String>();
		// Get all the vertices in the graph
		v_table.forEach((k, v) -> {
			vertices.add(k);
		});
		return vertices;
	}

	/**
	 * Return the list of all vertices adjacent to the given one
	 * @param vertex - the vertex needs finding adjacent vertices
	 * @return the list of all adjacent vertices
	 */
	@Override
	public List<String> getAdjacentVerticesOf(String vertex) {
		List<String> adjacentVertices = new LinkedList<String>();
		// Get all the adjacent vertices
		v_table.get(vertex).adjacent.forEach((k,v) -> {
			adjacentVertices.add(k);
		});
		return adjacentVertices;
	}

	/**
	 * Return the size of the graph
	 * @return size
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Return the order of graph
	 * @return order
	 */
	@Override
	public int order() {
		return order;
	}

	// TODO: implement all the methods declared in GraphADT

}
