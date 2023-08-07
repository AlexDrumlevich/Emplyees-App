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
	
  @Override
	public boolean addEmployee(Employee empl) {		
		return employees.putIfAbsent(empl.id(), empl) == null;
	}

	@Override
	public Employee removeEmployee(long id) {
		return employees.remove(id);
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
			.entrySet()
			.stream()
			.collect(Collectors.groupingBy(entry -> 
				    Integer.valueOf(entry.getValue().salary() / interval), 
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
	
}
