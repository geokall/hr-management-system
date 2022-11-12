package entity;

import enums.EmployeeStatusEnum;
import enums.GenderEnum;
import enums.JobStatusEnum;
import enums.MaritalStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "HUA_USER")
@Getter
@Setter
@NoArgsConstructor
public class HuaUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 25, unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 25, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "name", length = 20)
    private String name;

    @Column(name = "surname", length = 20)
    private String surname;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(name = "work_number", length = 20)
    private String workNumber;

    @Column(name = "home_number", length = 20)
    private String homeNumber;

    @Column(name = "employee_number", length = 20)
    private Long employeeNumber;

    @Column(name = "vat_number", length = 20)
    private String vatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatusEnum maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatusEnum jobStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_status")
    private EmployeeStatusEnum employeeStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "hire_date")
    private Date hireDate;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", referencedColumnName = "id")
    private HuaDivision division;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private HuaLocation location;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<HuaRole> roles = new HashSet<>();


    public void addRole(HuaRole role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(HuaRole role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaUser huaUser = (HuaUser) o;
        return Objects.equals(id, huaUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
