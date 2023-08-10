package telran.employees.service;

import telran.employees.dto.*;
import java.util.*;
public interface Company {
	boolean addEmployee(Employee empl);
	Employee removeEmployee(long id);
	Employee getEmployee(long id);
	List<Employee> getEmployees();
	List<DepartmentSalary> getDepartmentSalaryDistribution();//returns list of all departments with average salary
	List<SalaryDistribution> getSalaryDistribution(int interval);//returns salary values distribution 
	void restore(String filePath);
	void save (String filePath);
	
	//New methods for HW #36
	List<Employee> getEmployeesByDepartment(String department);
	List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo);
	List<Employee> getEmployeesByAge(int ageFrom, int ageTo);
	Employee updateSalary(long id, int newSalary);
	Employee updateDepartment(long id, String newDepartment);
}
