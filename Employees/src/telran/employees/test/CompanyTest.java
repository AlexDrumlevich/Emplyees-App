package telran.employees.test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import telran.employees.dto.*;
import telran.employees.service.Company;
import telran.employees.service.CompanyImpl;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyTest {
	private static final long ID1 = 123;
	private static final String DEP1 = "dep1";
	private static final int SALARY1 = 10000;
	private static final int YEAR1 = 2000;
	private static final LocalDate DATE1 = LocalDate.ofYearDay(YEAR1, 100);
	private static final long ID2 = 124;
	private static final long ID3 = 125;
	private static final long ID4 = 126;
	private static final long ID5 = 127;
	private static final String DEP2 = "dep2";
	private static final String DEP3 = "dep3";
	private static final int SALARY2 = 5000;
	private static final int SALARY3 = 15000;
	private static final int YEAR2 = 1990;
	private static final LocalDate DATE2 = LocalDate.ofYearDay(YEAR2, 100);
	private static final int YEAR3 = 2003;
	private static final LocalDate DATE3 = LocalDate.ofYearDay(YEAR3, 100);
	private static final long ID_NOT_EXIST = 10000000;
	private static final String TEST_DATA = "test.data";
	Employee empl1 = new Employee(ID1, "name", DEP1, SALARY1, DATE1);
	Employee empl2 = new Employee(ID2, "name", DEP2, SALARY2, DATE2);
	Employee empl3 = new Employee(ID3, "name", DEP1, SALARY1, DATE1);
	Employee empl4 = new Employee(ID4, "name", DEP2, SALARY2, DATE2);
	Employee empl5 = new Employee(ID5, "name", DEP3, SALARY3, DATE3);
	Employee[] employees = {empl1, empl2, empl3, empl4, empl5};
	Company company;

	@BeforeEach
	void setUp() throws Exception {
		company = new CompanyImpl();
		for(Employee empl: employees) {
			company.addEmployee(empl);
		}
	}

	@Test
	void testAddEmployee() {
		assertFalse(company.addEmployee(empl1));
		assertTrue(company.addEmployee(new Employee(ID_NOT_EXIST, "name", DEP1, SALARY1, DATE1)));
	}

	
	@Test
	void testRemoveEmployee() {
		assertNull(company.removeEmployee(ID_NOT_EXIST));
		assertEquals(empl1, company.removeEmployee(ID1));
		Employee[] expected = {empl2, empl3, empl4, empl5};
		assertArrayEquals(expected, company.getEmployees()
				.toArray(Employee[]::new));
		
	}

	@Test
	void testGetEmployee() {
		assertEquals(empl1, company.getEmployee(ID1));
		assertNull(company.getEmployee(ID_NOT_EXIST));
	}

	@Test
	void testGetEmployees() {
		assertArrayEquals(employees, company.getEmployees()
				.toArray(Employee[]::new));
	}

	@Test
	void testGetDepartmentSalaryDistribution() {
		DepartmentSalary [] expected = {
			new DepartmentSalary(DEP2, SALARY2),
			new DepartmentSalary(DEP1, SALARY1),
			new DepartmentSalary(DEP3, SALARY3)
		};
		DepartmentSalary [] actual = company.getDepartmentSalaryDistribution()
				.stream().sorted((ds1, ds2) -> Double.compare(ds1.salary(), ds2.salary())).
				toArray(DepartmentSalary[]::new);
		assertArrayEquals(expected, actual);
	}

	@Test
	void testGetSalaryDistribution() {
		int interval = 5000;
		SalaryDistribution[] expected = {
				new SalaryDistribution(SALARY2, SALARY2 + interval - 1, 2),
				new SalaryDistribution(SALARY1, SALARY1 + interval - 1, 3),
				new SalaryDistribution(SALARY3, SALARY3 + interval - 1, 1),
		};
		company.addEmployee(new Employee(ID_NOT_EXIST, DEP2, DEP2, 13000,  DATE1));
		SalaryDistribution[] actual =
				company.getSalaryDistribution(interval)
				.toArray(SalaryDistribution[]::new);
		assertArrayEquals(expected, actual);
	}

	@Test
	@Order(2)
	void testRestore() {
		Company newCompany = new CompanyImpl();
		newCompany.restore(TEST_DATA);
		assertArrayEquals(employees, newCompany.getEmployees()
				.toArray(Employee[]::new));
		
	}

	@Test
	@Order(1)
	void testSave() {
		company.save(TEST_DATA);
	}

	
	@Test
	void getEmployesByDepartmentTest() {
		Employee[] expectedDep1_1 = {empl1, empl3};
		Employee[] actualDep1_1 = company.getEmployeesByDepartment(DEP1).stream().sorted((e1, e2) -> Long.compare(e1.id(), e2.id())).toArray(Employee[]::new);
		assertArrayEquals(expectedDep1_1, actualDep1_1);
		
		Employee[] expectedDep1_2 = {empl3};
		company.removeEmployee(empl1.id());
		Employee[] actualDep1_2 = company.getEmployeesByDepartment(DEP1).stream().sorted((e1, e2) -> Long.compare(e1.id(), e2.id())).toArray(Employee[]::new);
		assertArrayEquals(expectedDep1_2, actualDep1_2);
		
		company.removeEmployee(empl3.id());
		Employee[] actualDep1_3 = company.getEmployeesByDepartment(DEP1).stream().sorted((e1, e2) -> Long.compare(e1.id(), e2.id())).toArray(Employee[]::new);
		assertTrue(actualDep1_3.length == 0);
		
		company.addEmployee(empl1);
		Employee[] expectedDep1_4 = {empl1};
		Employee[] actualDep1_4 = company.getEmployeesByDepartment(DEP1).stream().sorted((e1, e2) -> Long.compare(e1.id(), e2.id())).toArray(Employee[]::new);
		assertArrayEquals(expectedDep1_4, actualDep1_4);
		
	}
	
	
	@Test
	void getEmployeesBySalaryTest() {
		assertTrue(company.getEmployeesBySalary(0, 1000).isEmpty());
		assertTrue(company.getEmployeesBySalary(15001, 10000000).isEmpty());
		
		Employee[] expectedSalary1 = {empl2, empl4};
		assertArrayEquals(expectedSalary1, 
				company.getEmployeesBySalary(0, 5000)
					.stream()
					.sorted((e1, e2) -> Long.compare(e1.id(), e2.id()))
					.toArray(Employee[]::new));
		
		assertArrayEquals(expectedSalary1, 
				company.getEmployeesBySalary(5000, 5000)
					.stream()
					.sorted((e1, e2) -> Long.compare(e1.id(), e2.id()))
					.toArray(Employee[]::new));
		
		Employee[] expectedSalary2 = {empl1, empl3, empl5};
		assertArrayEquals(expectedSalary2, 
				company.getEmployeesBySalary(10000, 15000)
					.stream()
					.sorted((e1, e2) -> Long.compare(e1.id(), e2.id()))
					.toArray(Employee[]::new));
		
		assertThrowsExactly(IllegalArgumentException.class, () -> company.getEmployeesBySalary(10, 9));
		assertThrowsExactly(IllegalArgumentException.class, () -> company.getEmployeesBySalary(-1, 9));
		assertThrowsExactly(IllegalArgumentException.class, () -> company.getEmployeesBySalary(10, -9));
		
	}
	
	@Test
	void updateSalaryTest() {
		company.updateSalary(ID1, SALARY2);
		assertEquals(company.getEmployee(ID1).salary(), SALARY2);
	}
	
	@Test
	void updateDepartmenTest() {
		company.updateDepartment(ID1, DEP2);
		assertEquals(company.getEmployee(ID1).department(), DEP2);
	}
	
	@Test
	//has to be corrected depends on time test executing (age changes during the time)
	void getEmployeesByAgeTest() {
		assertTrue(company.getEmployeesByAge(10, 12).isEmpty());
		assertTrue(company.getEmployeesByAge(-3, -1).isEmpty());
		
		Employee[] expectedAge1 = employees;
		assertArrayEquals(expectedAge1, 
				getArraySortedByIdFrom(company.getEmployeesByAge(0, 33)));
		
		Employee[] expectedAge2 = {empl1, empl3, empl5};
		assertArrayEquals(expectedAge2, 
				getArraySortedByIdFrom(company.getEmployeesByAge(0, 32)));
		
		Employee[] expectedAge3 = {empl2, empl4};
		assertArrayEquals(expectedAge3, 
				getArraySortedByIdFrom(company.getEmployeesByAge(32, 33)));
		
		
		//LocalDate has to be corrected 
			//employee6: current day; current month; current year - 37
			//employee7: current day-1; current month; current year - 37
		Employee employee6 = new Employee(6, "6", DEP1, SALARY1, LocalDate.of(1986, 8, 14));
		Employee employee7 = new Employee(7, "7", DEP1, SALARY1, LocalDate.of(1986, 8, 13));
		company.addEmployee(employee6);
		company.addEmployee(employee7);
		
		Employee[] expectedAge4 = {employee7};
		assertArrayEquals(expectedAge4, 
				getArraySortedByIdFrom(company.getEmployeesByAge(37, 38)));
		
		Employee[] expectedAge5 = {employee6, employee7};
		assertArrayEquals(expectedAge5, 
				getArraySortedByIdFrom(company.getEmployeesByAge(36, 38)));
	
		Employee[] expectedAge6 = {employee6};
		assertArrayEquals(expectedAge6, getArraySortedByIdFrom(company.getEmployeesByAge(36, 36)));
				
					
		
		assertThrowsExactly(IllegalArgumentException.class, () -> company.getEmployeesByAge(10, 9));
		assertThrowsExactly(IllegalArgumentException.class, () -> company.getEmployeesByAge(10, -9));
		
	}
	
	
	private Employee[] getArraySortedByIdFrom (Collection<Employee> employees) {
		return employees
				.stream()
				.sorted((e1, e2) -> Long.compare(e1.id(), e2.id()))
				.toArray(Employee[]::new);
	}

	
}
