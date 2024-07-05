package com.ukrcarservice.ucs_support_telegrabm_bot.repository;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, Integer> {
    Employee findByUsername(String username);
    Employee findByUsernameAndIsActive(String username, String isActive);
//    Employee findByUsername(String userName);
    List<Employee> findAllByIsActiveOrderBySecondName(String isActive);
}
