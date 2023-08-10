package telran.employees.service;
import java.util.stream.Collectors;
import telran.employees.dto.DepartmentSalary;
import telran.employees.dto.Employee;
import telran.employees.dto.SalaryDistribution;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;


public class CompanyImpl implements Company {

	private LinkedHashMap<Long, Employee> employees = new LinkedHashMap<>();
	private TreeMap<Integer, Collection<Employee>> employeesSalary = new TreeMap<>();
	private Map<String, Collection<Employee>> employeesDepartment = new HashMap<>();
  
  //ADD
  @Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		Employee emplRes = employees.putIfAbsent(empl.id(), empl);
		if(emplRes == null) {
			res = true;
			addEmployeeSalary(empl);
			addEmployeeDepartment(empl);
		}
		return  res;
	}

	private void addEmployeeSalary(Employee empl) {
		int salary = empl.salary();
		employeesSalary.computeIfAbsent(salary, k -> new HashSet<>()).
		add(empl);
	}
	private void addEmployeeDepartment(Employee empl) {
		String department = empl.department();
		employeesDepartment.computeIfAbsent(department, k -> new HashSet<>()).
		add(empl);
	}
	
	//REMOVE
	@Override
	public Employee removeEmployee(long id) {
		Employee res = employees.remove(id);
		if(res != null) {
			removeEmployeeSalary(res);
			removeEmployeeDepartment(res);
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
	private void removeEmployeeDepartment(Employee empl) {
		String department = empl.department();
		Collection<Employee> employeesCol = employeesDepartment.get(department);
		employeesCol.remove(empl);
		if(employeesCol.isEmpty()) {
			employeesDepartment.remove(department);
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
		List<Employee> listEmployee;
		Collection<Employee> collectionEmployees = employeesDepartment.get(department);
		if(collectionEmployees != null) {
			if(collectionEmployees instanceof List)
				listEmployee = (List<Employee>)collectionEmployees;
			else
				listEmployee = new ArrayList<Employee>(collectionEmployees);
		} else {
			listEmployee = new ArrayList<>();
		}
		return listEmployee;
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
