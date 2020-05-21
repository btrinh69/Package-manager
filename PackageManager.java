import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Filename:   PackageManager.java
 * Project:    p4
 * Authors:    Bon
 * 
 * PackageManager is used to process json package dependency files
 * and provide function that make that information available to other users.
 * 
 * Each package that depends upon other packages has its own
 * entry in the json file.  
 * 
 * Package dependencies are important when building software, 
 * as you must install packages in an order such that each package 
 * is installed after all of the packages that it depends on 
 * have been installed.
 * 
 * For example: package A depends upon package B,
 * then package B must be installed before package A.
 * 
 * This program will read package information and 
 * provide information about the packages that must be 
 * installed before any given package can be installed.
 * all of the packages in
 * 
 * You may add a main method, but we will test all methods with
 * our own Test classes.
 */

public class PackageManager {
	   
    private Graph graph;
    
    /*
     * Package Manager default no-argument constructor.
     */
    public PackageManager() {
        graph = new Graph();
    }
    
    /**
     * Takes in a file path for a json file and builds the
     * package dependency graph from it. 
     * 
     * @param jsonFilepath the name of json data file with package dependency information
     * @throws FileNotFoundException if file path is incorrect
     * @throws IOException if the give file cannot be read
     * @throws ParseException if the given json cannot be parsed 
     */
    public void constructGraph(String jsonFilepath) throws FileNotFoundException, IOException, ParseException {
    	Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
    	JSONObject jo = (JSONObject) obj;
    	JSONArray packageJSONArray = (JSONArray) jo.get("packages");
    	
    	for (int i = 0; i < packageJSONArray.size(); i++) {
    		JSONObject jsonPackage = (JSONObject) packageJSONArray.get(i);
    		String name = (String) jsonPackage.get("name");
    		graph.addVertex(name);
    		
    		JSONArray dependencies = (JSONArray) jsonPackage.get("dependencies");
    		for (int j = 0; j < dependencies.size(); j++) { //"dependencies": 
    			String dependentVertex = (String) dependencies.get(j);
    			graph.addEdge(name, dependentVertex);
    		}
    	}
    	
    }
    
    /**
     * Helper method to get all packages in the graph.
     * 
     * @return Set<String> of all the packages
     */
    public Set<String> getAllPackages() {
        return graph.getAllVertices();
    }
    
    /**
     * Given a package name, returns a list of packages in a
     * valid installation order.  
     * 
     * Valid installation order means that each package is listed 
     * before any packages that depend upon that package.
     * 
     * @return List<String>, order in which the packages have to be installed
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the installation order for a particular package. Tip: Cycles in some other
     * part of the graph that do not affect the installation order for the 
     * specified package, should not throw this exception.
     * 
     * @throws PackageNotFoundException if the package passed does not exist in the 
     * dependency graph.
     */
    public List<String> getInstallationOrder(String pkg) throws CycleException, PackageNotFoundException {
    	Set<String> set = graph.getAllVertices();
    	if (!set.contains(pkg))
    		throw new PackageNotFoundException();
        return topoOrder(pkg);
    }
    
    /**
     * Given two packages - one to be installed and the other installed, 
     * return a List of the packages that need to be newly installed. 
     * 
     * For example, refer to shared_dependecies.json - toInstall("A","B") 
     * If package A needs to be installed and packageB is already installed, 
     * return the list ["A", "C"] since D will have been installed when 
     * B was previously installed.
     * 
     * @return List<String>, packages that need to be newly installed.
     * 
     * @throws CycleException if you encounter a cycle in the graph while finding
     * the dependencies of the given packages. If there is a cycle in some other
     * part of the graph that doesn't affect the parsing of these dependencies, 
     * cycle exception should not be thrown.
     * 
     * @throws PackageNotFoundException if any of the packages passed 
     * do not exist in the dependency graph.
     */
    public List<String> toInstall(String newPkg, String installedPkg) throws CycleException, PackageNotFoundException {
    	Set<String> set = graph.getAllVertices();
    	// Throw exception if either package is not in the graph
    	if (!set.contains(newPkg) || !set.contains(installedPkg))
    		throw new PackageNotFoundException();
    	List<String> all = topoOrder(newPkg);
    	List<String> have = topoOrder(installedPkg);
    	List<String> need = new LinkedList<String>();
    	
    	// Filter all packages that should have been installed to install the installedPkg
    	for (String i : all) {
    		boolean take = true;
    		for (String j : have) {
    			if (i.equals(j)) {
    				take = false;
    				break;
    			}
    		}
    		if (take) {
				need.add(i);
			}
    	}
        return need;
    }
    
    /**
     * Return a valid global installation order of all the packages in the 
     * dependency graph.
     * 
     * assumes: no package has been installed and you are required to install 
     * all the packages
     * 
     * returns a valid installation order that will not violate any dependencies
     * 
     * @return List<String>, order in which all the packages have to be installed
     * @throws CycleException if you encounter a cycle in the graph
     */
    public List<String> getInstallationOrderForAllPackages() throws CycleException {
    	Set<String> vertices = new HashSet<String>(graph.getAllVertices());
    	Set<String> check = new HashSet<String>();
    	
    	// Get all vertices that have at least one "predecessor"
    	for (String i : vertices) {
    		for (String j: graph.getAdjacentVerticesOf(i)) {
    			check.add(j);
    		}
    	}
    	
    	// Remove all those to get the vertices that have no "predecessor"
    	vertices.removeAll(check);
    	
    	// If none of them have no "predecessor", the graph is cyclic
    	if (vertices.size() == 0) {
    		throw new CycleException();
    	}
    	
    	LinkedList<String> visited = new LinkedList<String>();
    	Stack<String> tmp = new Stack<String>();
    	List<String> order = new LinkedList<String>();
    	
    	// Push all vertices with no "predecessor" to the stack and visited list
    	for (String i : vertices) {
    		tmp.push(i);
    		visited.add(i);
    	}
    	// Perform topological order algorithm
    	while (!tmp.isEmpty()) {
    		String curr = tmp.peek();
    		boolean allVisited = true;
    		List<String> succ = graph.getAdjacentVerticesOf(curr);
    		for (String i : succ) {
    			if (tmp.search(i) != -1) {
    				throw new CycleException();
    			}
    			boolean v = false;
    			for (int j = 0; j < visited.size(); j++) {
    				if (i.equals(visited.get(j))) {
    					v = true;
    					break;
    				}
    			}
    			if (!v) {
    				tmp.push(i);
    				visited.add(i);
    				allVisited = false;
    				break;
    			}
    		}
    		if (allVisited) {
    			order.add(tmp.pop());
    		}
    	}
    	
    	// If the size of the InstallationOrderForAllPackage list does not contain all vertices,
    	// it terminated when encountered a cycle
    	if (order.size() < graph.getAllVertices().size())
    		throw new CycleException();
    	return order;
    }
    
    /**
     * Find and return the name of the package with the maximum number of dependencies.
     * 
     * Tip: it's not just the number of dependencies given in the json file.  
     * The number of dependencies includes the dependencies of its dependencies.  
     * But, if a package is listed in multiple places, it is only counted once.
     * 
     * Example: if A depends on B and C, and B depends on C, and C depends on D.  
     * Then,  A has 3 dependencies - B,C and D.
     * 
     * @return String, name of the package with most dependencies.
     * @throws CycleException if you encounter a cycle in the graph
     */
    public String getPackageWithMaxDependencies() throws CycleException {
    	String maxP = "";
    	int maxD = 0;
    	for (String i : graph.getAllVertices()) {
    		try {
    			// Check the size of the installation order for every vertex
    			int tmp = getInstallationOrder(i).size();
    			// Save the one with the highest dependencies
				if (tmp > maxD) {
					maxP = i;
					maxD = tmp;
				}
			} catch (PackageNotFoundException e) {}
    	}
        return maxP;
    }

    public static void main (String [] args) {
        
    }
    
    /**
     * Helper method to get the topological order from one package
     * @param pkg
     * @return the list in topological order
     * @throws CycleException if cycle is detected
     */
    private List<String> topoOrder(String pkg) throws CycleException {
    	LinkedList<String> visited = new LinkedList<String>();
    	Stack<String> tmp = new Stack<String>();
    	List<String> order = new LinkedList<String>();
    	// Push the package needs installing into the stack and visited list
    	tmp.push(pkg);
    	visited.add(pkg);
    	
    	while (!tmp.isEmpty()) {
    		String curr = tmp.peek();
    		boolean allVisited = true;
    		for (String i : graph.getAdjacentVerticesOf(curr)) {
    			// If the adjacent vertex of the current vertex is in the previous
    			// topo order to the package, it is a cycle
    			if (tmp.search(i) != -1) {
    				throw new CycleException();
    			}
    			// Check if it is visited
    			boolean v = false;
    			for (int j = 0; j < visited.size(); j++) {
    				if (i.equals(visited.get(j))) {
    					v = true;
    					break;
    				}
    			}
    			// If it is not, push it in the stack and visited list
    			if (!v) {
    				tmp.push(i);
    				visited.add(i);
    				// If at least one vertex is not visited, allVisited is false
    				allVisited = false;
    				break;
    			}
    		}
    		// If all adjacent vertex is visited, pop the stack
    		if (allVisited) {
    			order.add(tmp.pop());
    		}
    	}
    	
    	return order;
    }
    
}
