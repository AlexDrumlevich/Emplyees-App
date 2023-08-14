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
import java.security.KeyStore.Entry;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;


public class CompanyImpl implements Company {

	private LinkedHashMap<Long, Employee> employees = new LinkedHashMap<>();
	private TreeMap<Integer, Collection<Employee>> employeesSalary = new TreeMap<>();
	private Map<String, Collection<Employee>> employeesDepartment = new HashMap<>();
	private TreeMap<LocalDate, Collection<Employee>> employeesAge = new TreeMap<>();
	
	
  //ADD
  @Override
	public boolean addEmployee(Employee empl) {
		boolean res = false;
		Employee emplRes = employees.putIfAbsent(empl.id(), empl);
		if(emplRes == null) {
			res = true;
			addToMap(empl, employeesSalary, empl.salary());
			addToMap(empl, employeesAge, empl.birthDate());
			addToMap(empl, employeesDepartment, empl.department());
		}
		return  res;
	}


	/* V.R.   addToMap
	 *  T - type of key (Integer, String and so on)
	 *  empl - employee of type Employee
	 *  map - the map for saving collection of employees grouped
	 *  by key (salary, department, age and so on)
	 *  key of type T - the key of the map 
	 *  (salary, department, age and so on)
	 */
  private <T> void addToMap(
  		Employee empl, Map<T,Collection<Employee>> map, T key ) {
      map.computeIfAbsent(key, collection -> new HashSet<>()).add(empl);
  }
  
	//REMOVE
	@Override
	public Employee removeEmployee(long id) {
		Employee res = employees.remove(id);
		if(res != null) {
			removeFromMap(res, employeesSalary, res.salary());
			removeFromMap(res, employeesAge, res.birthDate());
			removeFromMap(res, employeesDepartment, res.department());
		}
		return res;
	}

	private <T> void removeFromMap(
    		Employee empl, Map<T,Collection<Employee>> map, T key ) {
        Collection<Employee> collection = map.get(key);
        collection.remove(empl);
        if (collection.isEmpty()) {
        	map.remove(key);
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
			employees.values().stream().forEach(e -> {
				addToMap(e, employeesSalary, e.salary());
				addToMap(e, employeesAge, e.birthDate());
				addToMap(e, employeesDepartment, e.department());
			});
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
		//complexity O[1] if employeesDepartment implements List
		Collection<Employee> collectionEmployees = employeesDepartment.get(department);
		List<Employee> listEmployee;
		if(collectionEmployees != null) {
			if(collectionEmployees instanceof List)
				listEmployee = (List<Employee>)collectionEmployees;
			else
				listEmployee = collectionEmployees.stream().toList();
		} else {
			listEmployee = new ArrayList<>();
		}
		return listEmployee;
		
		//the other way complexity O[n] or O[1] ?
		//return employeesDepartment.get(department).stream().toList();
	}

	@Override
	public List<Employee> getEmployeesBySalary(int salaryFrom, int salaryTo) {
		if(salaryFrom > salaryTo || salaryFrom < 0) {
			throw new IllegalArgumentException();
		}
		return employeesSalary
					.subMap(salaryFrom, true, salaryTo, true)
					.values()
					.stream()
					.flatMap(e -> e.stream())
					.toList();
	}

	@Override
	public List<Employee> getEmployeesByAge(int ageFrom, int ageTo) {
		LocalDate dateTo = LocalDate.now().minusYears(ageFrom);
		LocalDate dateFrom = LocalDate.now().minusYears(ageTo);
		return employeesAge.subMap(dateFrom, true, dateTo, true)
				.values()
				.stream()
				.flatMap(col -> col.stream())
				.toList();
	}

	@Override
	public Employee updateSalary(long id, int newSalary) {
		Employee employee = employees.get(id);
		if(employee != null) {
			employee = new Employee(employee.id(), employee.name(), employee.department(), newSalary, employee.birthDate());
			removeEmployee(employee.id());
			addEmployee(employee);
		}
		return employee;
	}

	@Override
	public Employee updateDepartment(long id, String newDepartment) {
		Employee employee = employees.get(id);
		if(employee != null) {
			employee = new Employee(employee.id(), employee.name(), newDepartment, employee.salary(), employee.birthDate());
			removeEmployee(employee.id());
			addEmployee(employee);
		}
		return employee;
	}
	
}
