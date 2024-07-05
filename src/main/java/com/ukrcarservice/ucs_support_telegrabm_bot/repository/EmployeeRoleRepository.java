package com.ukrcarservice.ucs_support_telegrabm_bot.repository;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.EmployeeRole;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRoleRepository extends PagingAndSortingRepository<EmployeeRole, Integer> {
    List<EmployeeRole> findAllByOrderByEmployeeRoleIdAsc();
}
