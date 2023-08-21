package telran.employees.controller;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import telran.employees.service.Company;
import telran.view.InputOutput;
import telran.view.Item;


public class CompanyController {
	private static final long MIN_ID = 100000;
	private static final long MAX_ID = 999999;
	private static final int MIN_SALARY = 6000;
	private static final int MAX_SALARY = 50000;
	private static final int MAX_AGE = 75;
	private static final int MIN_AGE = 20;
	
	private static Company company;
	
	public static ArrayList<Item> getCompanyItems(Company company) {
		CompanyController.company = company;
		ArrayList<Item> res = new ArrayList<>(Arrays.asList(
				getItems()));
		return res;
	}
	private static Item[] getItems() {

		return new Item[] {
				Item.of("Add new Employee", CompanyController::addEmployeeItem),
				Item.of("Remove Employee", CompanyController::removeEmployeeItem),
				Item.of("All Employees", CompanyController::getEmployeesItem),
				Item.of("Data about Employee", CompanyController::getEmployeeItem),
				Item.of(" Employees by Salary", CompanyController::getEmployeesBySalaryItem),
				Item.of("Employees by Department", CompanyController::getEmployeesByDepartmentItem),
				Item.of("Update salary", CompanyController::updateSalaryItem),
				Item.of("Departments and Salary", CompanyController::getDepartmentSalaryDistributionItem),
				Item.of("Distribution by Salary", CompanyController::getSalaryDistributionItem),
				Item.of("Employees by Age", CompanyController::getEmployeesByAgeItem),
				Item.of("Update Department", CompanyController::updateDepartmentItem)
		};
	}

	static private Set<String> departments = new HashSet<>(Arrays.asList(new String[] {
			"QA", "Development", "Audit", "Management", "Accounting"
	}));

	static private long readId(boolean isPresentInDB, InputOutput io) {
		Long id = null;

		do {
			if(id != null) {
				if(isPresentInDB) {
					io.writeLine(String.format("There is not employee with such id: %d, there are only follow employees: ", id));
					getEmployeesItem(io);
				} else {
					io.writeLine(String.format("Employee with id %d already exists: ", id));
				}
			}	
			id = io.readLong("Enter Employee identity", "Wrong identity value", MIN_ID, MAX_ID);
		} while(isPresentInDB ? company.getEmployee(id) == null : company.getEmployee(id) != null);
		return id;
	}

	static private Employee getEmployee(InputOutput io) throws Exception {
		Employee employee = null;
		if(!company.getEmployees().isEmpty()) {
			employee = company.getEmployee(readId(true, io));
			if (employee == null)
				throw new Exception("Error of getting employee");
			return employee;
		} else {
			throw new Exception("List of employee is empty");
		}
	}
	
	static private <T> void writeEmptyPossibleList(List<T> list, InputOutput io) {
		if(list.isEmpty())
			io.writeLine("List is empty");
		else
			list.stream().forEach(e -> io.writeLine(e.toString()));
	}
	
	static void addEmployeeItem(InputOutput io) {
		//id
		Long id = readId(false, io);

		//name
		String name = io.readString("Enter name", "Wrong name",
				str -> str.matches("[A-Z][a-z]+"));
		//department
		String department = io.readString(String.format("Enter department: %s", departments.toString()), "Wrong department", departments);
		//salary
		int salary = io.readInt(String.format("Enter salary from %d to %d", MIN_SALARY, MAX_SALARY), "Wrong salary", MIN_SALARY, MAX_SALARY);
		//age
		LocalDate birthDate = io.readDate("Enter birth data", "Wrong birth date entered",
				LocalDate.now().minusYears(MAX_AGE + 1), LocalDate.now().minusYears(MIN_AGE).minusDays(1));
		//result processing
		boolean res = company.addEmployee(new Employee(id, name, department, salary, birthDate));
		io.writeLine(res ? String.format("Employee with id %d has been added", id) : 
			String.format("Additing error Employee with id %d", id));
	}

	
	static void removeEmployeeItem(InputOutput io) {

		try {
			Employee employee = getEmployee(io);
			Employee removedEmployee = company.removeEmployee(employee.id());
			io.writeLine(removedEmployee != null ? String.format("Employee with id %d has been removed", removedEmployee.id()) : 
				String.format("Removing error Employee with id %d", employee.id()));
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		} 
	}

	static void getEmployeeItem(InputOutput io) {
		try {
			io.writeLine(getEmployee(io).toString());
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}
	}

	static void getEmployeesItem(InputOutput io) {
		writeEmptyPossibleList(company.getEmployees(), io);
	}

	static void getDepartmentSalaryDistributionItem(InputOutput io) {
		writeEmptyPossibleList(company.getDepartmentSalaryDistribution(), io);
	}
	
	static void getSalaryDistributionItem(InputOutput io) {
		int distribution = io.readIntPositive("Enter distribution amount", "Wrong value");
		writeEmptyPossibleList(company.getSalaryDistribution(distribution), io);
	}

	static void getEmployeesByDepartmentItem(InputOutput io) {
		String department = io.readString(String.format("Enter department: %s", departments.toString()), "Wrong department", departments);
		writeEmptyPossibleList(company.getEmployeesByDepartment(department), io);
	}

	static void getEmployeesBySalaryItem(InputOutput io) {
		int[] fromToInts = io.readFromToIntsPositive("Enter salary range", "Wrong value, \"from\" value must be lower than \"to\" value");
		writeEmptyPossibleList(company.getEmployeesBySalary(fromToInts[0], fromToInts[1]), io);
	}

	static void getEmployeesByAgeItem(InputOutput io) {
		int[] fromToInts = io.readFromToIntsPositive("Enter age range", "Wrong value, \"from\" value must be lower than \"to\" value");
		writeEmptyPossibleList(company.getEmployeesByAge(fromToInts[0], fromToInts[1]), io);
	}
	

	
	static void updateSalaryItem(InputOutput io) {
		try {
			Employee employee = getEmployee(io);
			int currentEmployeeSalary = employee.salary();
			int newSalary = io.readIntPositive("Enter new salary amount", "Wrong value");
			Employee updatedEmployee = company.updateSalary(employee.id(), newSalary);
			io.writeLine(updatedEmployee != null ? String.format("Salary of employee with id %d has been updated from %d to %d", updatedEmployee.id(), currentEmployeeSalary, updatedEmployee.salary()) : 
				String.format("Updating error Employee with id %d", employee.id()));
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}
	}

	static void updateDepartmentItem(InputOutput io) {
		try {
			Employee employee = getEmployee(io);
			String currentEmployeeDepartment = employee.department();
			String newDepartment = io.readString(String.format("Enter department: %s", departments.toString()), "Wrong value", departments);
			Employee updatedEmployee = company.updateDepartment(employee.id(), newDepartment);
			io.writeLine(updatedEmployee != null ? String.format("Department of employee with id %d has been updated from %s to %s", updatedEmployee.id(), currentEmployeeDepartment, updatedEmployee.department()) : 
				String.format("Upddating error Employee with id %d", employee.id()));
		} catch (Exception e) {
			io.writeLine(e.getMessage());
		}
	}
}
