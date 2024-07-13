package com.ukrcarservice.ucs_support_telegram_bot.service;

import com.ukrcarservice.ucs_support_telegram_bot.entity.Employee;
import com.ukrcarservice.ucs_support_telegram_bot.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames="EmployeeById")
    public Employee findById(Integer employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if(employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        return null;
    }

    @Cacheable(cacheNames="EmployeesActive")
    public List<Employee> findAllByIsActiveOrderBySecondName(String isActive){
        return employeeRepository.findAllByIsActiveOrderBySecondName(isActive.toUpperCase());
    }

    @Cacheable(cacheNames="Employees")
    public List<Employee> findAll() {
        Iterator<Employee> employeeIterator = employeeRepository.findAll().iterator();
        if(employeeIterator == null){
            return null;
        }
        List<Employee> employeeList = new ArrayList<>();
        while(employeeIterator.hasNext()){
            employeeList.add(employeeIterator.next());
        }
        return employeeList;
    }

    @Cacheable(cacheNames="EmployeesPageable")
    public Page<Employee> findAllPageable(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee save(Employee employee) {
        cacheManager.getCache("EmployeeById").clear();
        cacheManager.getCache("Employees").clear();
        cacheManager.getCache("EmployeeByUsername").clear();
        cacheManager.getCache("EmployeeByUsernameAndIsActive").clear();
        cacheManager.getCache("EmployeesPageable").clear();
        log.warn("All employees caches have been cleared!");
        return employeeRepository.save(employee);
    }

    @Cacheable(cacheNames="EmployeeByUsername")
    public Employee findByUsername(String userName){
        return employeeRepository.findByUsername(userName);
    }

    @Cacheable(cacheNames="EmployeeByUsernameAndIsActive")
    public Employee findByUsernameAndIsActive(String userName, String isActive){
        return employeeRepository.findByUsernameAndIsActive(userName,isActive);
    }

}
