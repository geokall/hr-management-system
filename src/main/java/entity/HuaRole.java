package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "HUA_ROLE")
public class HuaRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<HuaUser> roles = new HashSet<>();

    public HuaRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HuaUser> getRoles() {
        return roles;
    }

    public void setRoles(Set<HuaUser> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuaRole huaRole = (HuaRole) o;
        return Objects.equals(id, huaRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
