package telran.employees.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore.Entry;
import java.util.*;
public class CompanyImpl implements Company {
  LinkedHashMap<Long, Employee> employees = new LinkedHashMap<>();
  TreeMap<Integer, Collection<Employee>> employeesSalary = new TreeMap<>();
 
  @Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		Employee emplRes = employees.putIfAbsent(empl.id(), empl);
		if(emplRes == null) {
			res = true;
			addEmployeeSalary(empl);
		}
		return  res;
	}

	private void addEmployeeSalary(Employee empl) {
		int salary = empl.salary();
		employeesSalary.computeIfAbsent(salary, k -> new HashSet<>()).
		add(empl);
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee res = employees.remove(id);
		if(res != null) {
			removeEmployeeSalary(res);
		}
		return res;
	}

	private void removeEmployeeSalary(Employee empl) {
		int salary = empl.salary();
		Collection<Employee> employeesCol = employeesSalary.get(salary);
		employeesCol.remove(empl);
		if(employeesCol.isEmpty()) {
			employeesSalary.remove(salary);
		}	
	}
	
	@Override
	public Employee getEmployee(long id) {
		return employees.get(id);
	}

	@Override
	public List<Employee> getEmployees() {
		return new ArrayList<>(employees.values());
	}

	@Override
	public List<DepartmentSalary> getDepartmentSalaryDistribution() {
		
		return employees.values().stream()
				.collect(Collectors.groupingBy(Employee::department,
						Collectors.averagingInt(Employee::salary)))
				.entrySet().stream().map(e -> new DepartmentSalary(e.getKey(),
						e.getValue())).toList();
	}

	@Override
	public List<SalaryDistribution> getSalaryDistribution(int interval) {
		return employees
			.values()
			.stream()
			.collect(Collectors.groupingBy(employee -> 
				    Integer.valueOf(employee.salary() / interval), 
					Collectors.counting()))
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue() != 0)
			.map(entry -> new SalaryDistribution(entry.getKey() * interval, entry.getKey() * interval + interval - 1, entry.getValue().intValue()))
			.toList();

	}

	@Override
	public void restore(String filePath) {
		try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
			employees = (LinkedHashMap<Long, Employee>) objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void save(String filePath) {
		try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
			objectOutputStream.writeObject(employees);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Employee> getEmployeesByDepartment(String department) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Employee updateDepartment(long id, String newDepartment) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
