package com.ukrcarservice.ucs_support_telegrabm_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_status_s1", schema = "admin")
public class EmployeeStatus {

    @Id
    @Column(name = "employee_status_id")
    private Integer employeeStatusId;
    @Column(name = "employee_status_name")
    private String employeeStatusName;
    @Column(name = "employee_status_description")
    private String employeeStatusDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeStatus employeeStatus = (EmployeeStatus) o;
        return employeeStatusId.equals(employeeStatus.employeeStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeStatusId);
    }

}
