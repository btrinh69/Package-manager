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

import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is the test suite for the PackageManager class
 * @author Bon
 *
 */
public class PackageManagerTest {
	PackageManager pm; // The PackageManager for object used for the test
	
	/**
	 * Called before every test
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		pm = new PackageManager();
	}
	
	/**
	 * Called after every test
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		pm = null;
	}
	
	/**
	 * Test if getAllPackages works properly
	 */
	@Test
	public void test00_getAllPackages() {
		try {
			pm.constructGraph("valid.json");
			Set<String> pkg = pm.getAllPackages();
			// Remove all the packages expected in the list
			pkg.remove("A");
			pkg.remove("B");
			pkg.remove("C");
			pkg.remove("D");
			pkg.remove("E");
			// If the list is not empty, fail
			if (!pkg.isEmpty()) {
				fail();
			}
		// If any exception occurs, fail
		} catch (Exception e) {
			fail();
		}
		
	}
	
	/**
	 * Test if getInstallationOrder return the correct order
	 */
	@Test
	public void test01_getInstallationOrder() {
		try {
			pm.constructGraph("valid.json");
			List<String> a = pm.getInstallationOrder("A");
			String[] answer = {"C", "D", "B", "A"};
			// Check if the first two packages are C and D (regardless of order) and the third is B, the last one is A
			if (
					!( (a.get(0).equals(answer[0]) && a.get(1).equals(answer[1])) || (a.get(0).equals(answer[1]) && a.get(1).equals(answer[0])))
					|| !a.get(2).equals(answer[2])
					|| !a.get(3).equals(answer[3])) {
				fail();
			}
		} catch (Exception e) {
			fail();
		} 
		
	}
	
	/**
	 * Test if getInstallationOrder can detect cycle 
	 */
	@Test
	public void test02_getInstallationOrder_with_cycle() {
		try {
			// Call the method on a cyclic graph
			pm.constructGraph("cyclic.json");
			pm.getInstallationOrder("A");
			// Fail if the exception is not thrown
			fail();
		}
		// Fail if any exception besides CycleException is thrown
		catch (CycleException e) {}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if toInstall method works properly 
	 */
	@Test
	public void test03_toInstall() {
		try {
			pm.constructGraph("valid.json");
			List<String> a = pm.toInstall("A", "B");
			if (a.size() != 1 && !a.get(0).equals("A")) {
				fail();
			}
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if getInstallationOrderForAllPackages works properly
	 */
	@Test
	public void test04_getInstallationOrderForAllPackages() {
		try {
			pm.constructGraph("valid.json");
			List<String> a = pm.getInstallationOrderForAllPackages();
			String[] answer = {"C", "D", "B", "A", "E"};
			// Test if the first two packages installed is C and D (regardless of order) and the following in order
			if (
					!((a.get(0).equals(answer[0]) && a.get(1).equals(answer[1])) 
							|| (a.get(0).equals(answer[1]) && a.get(1).equals(answer[0])))
					|| !a.get(2).equals(answer[2])
					|| !((a.get(3).equals(answer[3]) && a.get(4).equals(answer[4])) 
							|| (a.get(3).equals(answer[4]) && a.get(4).equals(answer[3])))) {
				fail();
			}
		}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if getInstallationOrderForAllPackages detects cycle
	 */
	@Test
	public void test05_getInstallationOrderForAllPackages_with_cycle() {
		try {
			// Call the method on a cyclic graph
			pm.constructGraph("cyclic.json");
			pm.getInstallationOrderForAllPackages();
			fail();
		} catch (CycleException e) {}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if getPackageWithMaxDependencies works properly
	 */
	@Test
	public void test06_getPackageWithMaxDependencies() {
		try {
			pm.constructGraph("valid.json");
			String ans = pm.getPackageWithMaxDependencies();
			if (!ans.equals("A") && !ans.equals("E"))
				fail();
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if getPackageWithMaxDependencies detects cycle
	 */
	@Test
	public void test07_getPackageWithMaxDependencies_with_cycle() {
		try {
			pm.constructGraph("cyclic.json");
			pm.getPackageWithMaxDependencies();
			fail();
		}
		catch (CycleException e) {}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if getInstallationOrder throws PackageNotFoundException properly
	 */
	@Test
	public void test08_getInstallationOrder_throws_PackageNotFoundException() {
		try {
			pm.constructGraph("valid.json");
			pm.getInstallationOrder("F");
			fail();
		}
		catch (PackageNotFoundException e) {}
		catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test if toInstall throws PackageNotFoundException properly
	 */
	@Test
	public void test09_toInstall_throws_PackageNotFoundException() {
		try {
			pm.constructGraph("valid.json");
			pm.toInstall("T", "B");
			fail();
		}
		catch (PackageNotFoundException e) {}
		catch (Exception e) {
			fail();
		}
	}
}
