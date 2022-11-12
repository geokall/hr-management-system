package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "HUA_ROLE")
@Getter
@Setter
@NoArgsConstructor
public class HuaRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<HuaUser> users = new HashSet<>();

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
