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
	
}
