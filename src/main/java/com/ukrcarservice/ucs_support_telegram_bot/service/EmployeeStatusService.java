package com.ukrcarservice.ucs_support_telegram_bot.service;

import com.ukrcarservice.ucs_support_telegram_bot.entity.EmployeeStatus;
import com.ukrcarservice.ucs_support_telegram_bot.repository.EmployeeStatusRepository;
import lombok.RequiredArgsConstructor;
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
public class EmployeeStatusService {

    private final EmployeeStatusRepository employeeStatusRepository;

    @Cacheable(cacheNames="EmployeeStatuses")
    public List<EmployeeStatus> findAll() {
        Iterator<EmployeeStatus> employeeStatusIterator = employeeStatusRepository.findAll().iterator();
        if(employeeStatusIterator == null){
            return null;
        }
        List<EmployeeStatus> employeeStatusList = new ArrayList<>();
        while(employeeStatusIterator.hasNext()){
            employeeStatusList.add(employeeStatusIterator.next());
        }
        return employeeStatusList;
    }

    @Cacheable(cacheNames="EmployeeStatusesPageable")
    public Page<EmployeeStatus> findAllPageable(Pageable pageable){
        return employeeStatusRepository.findAll(pageable);
    }

    public EmployeeStatus save(EmployeeStatus employeeStatus) {
        return employeeStatusRepository.save(employeeStatus);
    }

    @Cacheable(cacheNames="EmployeeStatus", key = "#employeeStatusId")
    public EmployeeStatus findById(Integer employeeStatusId) {
        Optional<EmployeeStatus> employeeStatusOptional = employeeStatusRepository.findById(employeeStatusId);
        if(employeeStatusOptional.isPresent()) {
            return employeeStatusOptional.get();
        }
        return null;
    }

}

