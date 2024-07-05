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
@Table(name = "employee_role_s1", schema = "admin")
public class EmployeeRole {

    @Id
    @Column(name = "employee_role_id")
    private Integer employeeRoleId;
    @Column(name = "employee_role_name")
    private String employeeRoleName;
    @Column(name = "employee_role_description")
    private String employeeRoleDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeRole that = (EmployeeRole) o;
        return employeeRoleId.equals(that.employeeRoleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeRoleId);
    }
}
