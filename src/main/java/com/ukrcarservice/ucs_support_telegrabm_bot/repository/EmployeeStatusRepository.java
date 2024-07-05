package com.ukrcarservice.ucs_support_telegrabm_bot.repository;

import com.ukrcarservice.ucs_support_telegrabm_bot.entity.EmployeeStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusRepository extends PagingAndSortingRepository<EmployeeStatus, Integer> {
}
