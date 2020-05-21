///////////////////////////////////////////////////////////////////////////////
//
// Title:           PackageManagerTest
// Files:           PackageManagerTest, PackageManager, Graph, GraphTest
// Course:          CS400 LEC001, Spring, 2020
//
// Author:          Binh Quoc Trinh (Bon)
// Email:           btrinh@wisc.edu
// Lecturer's Name: Debra Deppeler
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////


// org.junit.Assert.*; 
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the test suite for the graph class
 * @author Bon
 *
 */
public class GraphTest {
	Graph g; // The graph used for the test
	
	/**
	 * Called before each test
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		g = new Graph();
	}
	
	/**
	 * Called after each test
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		g = null;
	}
	
	/**
	 * Test if the addVetex method not throwing exception for null argument
	 */
	@Test
	public void test00_addVertex_null_not_throw_exception() {
		try {
			g.addVertex(null);
		}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if the addVertex method not throwing exception if the vertex
	 * is already in the graph
	 */
	@Test
	public void test01_addVertex_already_exist_not_throw_exception() {
		try {
			g.addVertex("A");
			g.addVertex("A");
		}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if the addVertex method add the vertex correctly
	 */
	@Test
	public void test02_addVertex() {
		try {
			// Add multiple vertex
			g.addVertex("A");
			g.addVertex("B");
			// Add a duplicate one
			g.addVertex("A");
			g.addVertex("C");
			// Check the order
			assert(g.order()==3);
		}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if the removeVertex removes the vertex correctly
	 */
	@Test
	public void test03_removeVertex() {
		// Add multiple vertex
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("A");
		g.addVertex("C");
		// Remove one vertex
		g.removeVertex("A");
		// Remove non-exist vertex
		g.removeVertex("F");
		// Remove duplicate
		g.removeVertex("A");
		// Check the order
		assert(g.order() == 2);
	}
	
	/**
	 * Test if addEdge adds edges correctly
	 */
	@Test
	public void test04_addEdge() {
		// Add multiple vertex
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		// Add edges for existing vertex
		g.addEdge("A", "B");
		// Add edges with one new vertex
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Check size and order
		assert(g.order()==5);
		assert(g.size()==3);
	}
	
	/**
	 * Test if removeEdge removes edges correctly
	 */
	@Test
	public void test05_removeEdge() {
		// Add vertices and edges
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B");
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Remove edges with both vertices were in the graph in the first place
		g.removeEdge("A", "B");
		// Check size and order
		assert(g.size()==2);
		assert(g.order()==5);
		// Remove edges with one vertex was in the graph in the first place
		g.removeEdge("A", "D");
		assert(g.size()==1);
		assert(g.order()==5);
		g.removeEdge("E", "B");
		assert(g.size()==0);
		assert(g.order()==5);
	}
	
	/**
	 * Test if getAllVertices return the correct list
	 */
	@Test
	public void test06_getAllVertices() {
		// Add multiple vertices
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		// Add edges with new vertices
		g.addEdge("A", "B");
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Remove an edge to test if it affects the getAllVertices method
		g.removeEdge("A", "D");
		Set<String> v = g.getAllVertices();
		// Create the answer
		Set<String> ans = new HashSet<String>();
		ans.addAll(Arrays.asList(new String[] {"A", "B", "C", "D", "E"}));
		// Remove all the elements in the answer, if the list is empty, it is true, false otherwise
		v.removeAll(ans);
		assert(v.size()==0);
	}
	
	/**
	 * Test if getAdjacentVerticesOf works properly
	 */
	@Test
	public void test07_getAdjacentVerticesOf() {
		// Add multiple vertices and edges
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B");
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Test getting the adjacent vertices of A
		List<String> a = g.getAdjacentVerticesOf("A");
		assert(a.remove("B"));
		assert(a.remove("D"));
		assert(a.size()==0);
	}
	
	/**
	 * Test if the order method works properly
	 */
	@Test
	public void test08_order() {
		// Add multiple vertices
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B");
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Test the order
		assert(g.order()==5);
	}
	
	/**
	 * Test if the size method works properly
	 */
	@Test
	public void test09_size() {
		// Add multiple vertices and edges
		g.addVertex("A");
		g.addVertex("B");
		g.addVertex("C");
		g.addEdge("A", "B");
		g.addEdge("A", "D");
		g.addEdge("E", "B");
		// Test the size
		assert(g.size()==3);
	}
}
