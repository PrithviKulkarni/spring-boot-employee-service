package com.fdmgroup.employeeApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fdmgroup.employeeApi.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

	@Query("SELECT e FROM Employee e WHERE e.firstName = :firstName AND e.lastName = :lastName")
	Optional<Employee> findByFirstAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
