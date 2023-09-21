package telran.employees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;

import telran.employees.dto.Employee;
import telran.employees.dto.FromTo;
import telran.employees.dto.UpdateData;
import telran.employees.service.Company;
import telran.net.ApplProtocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements ApplProtocol {

	private Company company;

	public CompanyProtocol(Company company) {
		this.company = company;
	}

	ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(); 
	Lock readLock = reentrantReadWriteLock.readLock();
	Lock writeLock = reentrantReadWriteLock.writeLock();
	private Function<Serializable, Serializable> readLockSupplier = new Function<Serializable, Serializable>() {
		@Override
		public Serializable apply(Serializable t) {
			readLock.lock();
			try {
				return t;
			} finally {
				readLock.unlock();
			}
		}
	};
	
	private Function<Serializable, Serializable> writeLockSupplier = new Function<Serializable, Serializable>() {
		@Override
		public Serializable apply(Serializable t) {
			writeLock.lock();
			try {
				return t;
			} finally {
				writeLock.unlock();
			}
		}
	};
	
	
	@Override
	public Response getResponse(Request request) {
		Response response = null;
		String requestType = request.requestType();
		Serializable data = request.requestData();
		try {
			Serializable responseData = switch(requestType) {
			case "employee/add" -> writeLockSupplier.apply(employee_add(data));
			case "employee/get" ->  readLockSupplier.apply(employee_get(data));
			case "employees/get" -> readLockSupplier.apply(employees_get(data));
			case "employees/department" -> readLockSupplier.apply(employees_department(data));
			case "employees/salary" -> readLockSupplier.apply(employees_salary(data));
			case "employees/age" -> readLockSupplier.apply(employees_age(data));
			case "department/update" ->  writeLockSupplier.apply(department_update(data));
			case "salary/update" -> writeLockSupplier.apply(salary_update(data));
			case "employee/remove" -> writeLockSupplier.apply(employee_remove(data));
			case "department/salary/distribution" -> readLockSupplier.apply(department_salary_distribution(data));
			case "salary/distribution" -> readLockSupplier.apply(salary_distribution(data));
			default -> new Response(ResponseCode.WRONG_TYPE, requestType +
			    		" is unsupported in the Company Protocol");
			};
			response = (responseData instanceof Response) ? 
					(Response) responseData :
				new Response(ResponseCode.OK, responseData);
			
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_DATA, e.toString());
		}
		return response;
	}
	
	 private Serializable salary_update(Serializable data) {
		@SuppressWarnings("unchecked")
		UpdateData<Integer> updateData = (UpdateData<Integer>) data;
		long id = updateData.id();
		int newSalary = updateData.data();
		return company.updateSalary(id, newSalary);
	}
	
	private Serializable employees_age(Serializable data) {
		FromTo fromTo = (FromTo) data;
		return new ArrayList<> (company.getEmployeesByAge(fromTo.from(), fromTo.to()));
	}
	
	private Serializable employees_salary(Serializable data) {
		FromTo fromTo = (FromTo) data;
		return new ArrayList<> (company.getEmployeesBySalary(fromTo.from(), fromTo.to()));
	}
	
	private Serializable employees_department(Serializable data) {
		String department = (String) data;
		return new ArrayList<> (company.getEmployeesByDepartment(department));
	}
	
	
	private Serializable salary_distribution(Serializable data) {
		int interval = (int) data;
		return new ArrayList<> (company.getSalaryDistribution(interval));
	}
	
	private Serializable department_salary_distribution(Serializable data) {
		Serializable a = (Serializable) company.getDepartmentSalaryDistribution();
		List<Employee> b = (List<Employee>) a;
		System.out.println((a == b) ? "==" : "!=");
		
		return new ArrayList<> (company.getDepartmentSalaryDistribution());
	}
	
	private Serializable employee_remove(Serializable data) {
		long id = (long) data;
		return company.removeEmployee(id);
	}
	
	 private Serializable department_update(Serializable data) {
		@SuppressWarnings("unchecked")
		UpdateData<String> updateData = (UpdateData<String>) data;
		long id = updateData.id();
		String department = updateData.data();
		return company.updateDepartment(id, department);
	}

	Serializable employees_get(Serializable data) {
		
		return new ArrayList<> (company.getEmployees());
	}

	Serializable employee_get(Serializable data) {
		long id = (long) data;
		return company.getEmployee(id );
	}

	Serializable employee_add(Serializable data) {
		
		Employee empl = (Employee) data;
		return company.addEmployee(empl);
	}

}
