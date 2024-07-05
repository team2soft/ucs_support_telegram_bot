package com.ukrcarservice.ucs_support_telegrabm_bot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee", schema = "admin")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", updatable = false, nullable = false)
    private Integer employeeId;
    @Column(name = "employee_status_id")
    private Integer statusId;
    @Column(name = "employee_role_id")
    private Integer roleId;
    @Column(name = "username")
    private String username;
    @Column(name = "f_name")
    private String firstName;
    @Column(name = "s_name")
    private String secondName;
    @Column(name = "p_name")
    private String parentName;
    @Column(name = "photo")
    private String photo;
    @Column(name = "is_active")
    private String isActive;
    @Column(name = "date_registration")
    private Date dateRegistration;

    @Column(name = "encrypted_password")
    private String encryptedPassword;

    // ссылка на объект EmployeeRole
    // сотрудник может иметь только одну роль (с обратной стороны - одна и та же роль может использоваться для множества сотрудников)
    @ManyToOne
    @JoinColumn(name = "employee_role_id", referencedColumnName = "employee_role_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private EmployeeRole employeeRole;

//    // ссылка на объект EmployeeStatus
//    // сотрудник может иметь только один статус (с обратной стороны - один и тот же статус может использоваться для множества сотрудников)
    @ManyToOne
    @JoinColumn(name = "employee_status_id", referencedColumnName = "employee_status_id", insertable = false, updatable = false) // по каким полям связывать (foreign key)
    private EmployeeStatus employeeStatus;

    public String getEmployeeIdStr(){
        if(employeeId == null) { return ""; }
        return this.employeeId.toString().replaceAll(" ","");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId.equals(employee.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
    public String getEmployeeIdString() {
        String employeeIdStr = employeeId.toString();
        return employeeIdStr.replaceAll(" ","");
    }

    public String getDateCreateString() {
        return dateRegistration == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(dateRegistration);
    }

    public String getFIO(){
        return (secondName != null ? secondName.trim() + " " : "") +(firstName != null ? firstName.trim() + " " : "") + (parentName != null ? parentName.trim() : "");
    }

    public String getIO(){
        return (firstName != null ? firstName.trim() + " " : "") + (parentName != null ? parentName.trim() : "");
    }

    public String getFI(){
        return (firstName != null ? firstName.trim() + " " : "") + (secondName != null ? secondName.trim() : "");
    }
}
