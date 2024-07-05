package com.ukrcarservice.ucs_support_telegrabm_bot.service;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.EmployeeRole;
import com.ukrcarservice.ucs_support_telegrabm_bot.repository.EmployeeRoleRepository;
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
public class EmployeeRoleService {

    private final EmployeeRoleRepository employeeRoleRepository;

    @Cacheable(cacheNames="EmployeeRoles")
    public List<EmployeeRole> findAll(){
        Iterator<EmployeeRole> employeeRoleIterator = employeeRoleRepository.findAll().iterator();
        if(employeeRoleIterator == null){
            return null;
        }
        List<EmployeeRole> employeeRoleList = new ArrayList<>();
        while(employeeRoleIterator.hasNext()){
            employeeRoleList.add(employeeRoleIterator.next());
        }
        return employeeRoleList;
    }

    @Cacheable(cacheNames="EmployeeRolesPageable")
    public Page<EmployeeRole> findAllPageable(Pageable pageable){
        return employeeRoleRepository.findAll(pageable);
    }

    public EmployeeRole save(EmployeeRole employeeRole) {
        return employeeRoleRepository.save(employeeRole);
    }

    @Cacheable(cacheNames="EmployeeRole", key = "#employeeRoleId")
    public EmployeeRole findById(Integer employeeRoleId) {
        Optional<EmployeeRole> employeeRoleOptional = employeeRoleRepository.findById(employeeRoleId);
        if(employeeRoleOptional.isPresent()) {
            return employeeRoleOptional.get();
        }
        return null;
    }

    @Cacheable(cacheNames="EmployeeRolesOrdered")
    public List<EmployeeRole> findAllByOrderByEmployeeRoleIdAsc(){
        return employeeRoleRepository.findAllByOrderByEmployeeRoleIdAsc();
    }

    @Cacheable(cacheNames="EmployeeRolesList")
    public List<EmployeeRole> findAllEmployeeRolesWithoutOwner(){
        List<EmployeeRole> list = findAllByOrderByEmployeeRoleIdAsc();
        list.remove(0);
        return list;
    }

}
