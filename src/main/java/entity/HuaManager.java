package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "HUA_MANAGER")
@Getter
@Setter
@NoArgsConstructor
public class HuaManager extends HuaUser {

    @ManyToMany(mappedBy = "managers")
    private Set<HuaUser> users = new HashSet<>();

}
